package com.ab.abmaterial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;

@ShortName("ABMNavigationBar")
public class ABMNavigationBar extends ABMObject {

	private static final long serialVersionUID = 7734573663534588883L;
	protected String ID="";
	protected transient ABMPage Page;
	protected ThemeNavigationBar Theme=new ThemeNavigationBar();
	protected boolean IsBuild=false;
	protected String Visibility=ABMaterial.VISIBILITY_ALL;
		
	protected String mTitle="";
	protected ABMComponent mTitleComp=null;
	protected String mTitleHTML="";

	protected List<ABMNavBarItem> TopItems = new ArrayList<ABMNavBarItem>();
	protected Map<String,List<ABMNavBarItem>> TopSubItems = new HashMap<String,List<ABMNavBarItem>>();
	protected List<ABMNavBarItem> SideItems = new ArrayList<ABMNavBarItem>();
	protected Map<String,List<ABMNavBarItem>> SideSubItems = new HashMap<String,List<ABMNavBarItem>>();
	protected int NBWidthPx=240;
	protected int HeightPx=56;
	
	public int TopBarHeightPx=56;

	protected ABMComponent mSideBarTopComponent=null;
	protected boolean TopBarDropDownBelow=true;

	protected boolean TopBarFixed=false;
	protected String SideBarCollapseType="accordion";
	
	public int SideBarLogoHeight=49;
	protected String ActiveTopReturnName="";
	protected String ActiveSideReturnName="";
	protected String ActiveSideSubReturnName="";
	protected boolean HasFadeEffect=false;
	protected double FadeEffectValue=0.75;
	
	public boolean TopBarDropdownConstrainWidth=true;
	public boolean IsInitialized=false;
	
	protected String BarType=ABMaterial.SIDEBAR_MANUAL_HIDEMEDIUMSMALL;
	
	protected int IDCounter=0;
	
	public boolean IsTextSelectable=true;
	
	protected boolean DoTopBar=false;
	protected boolean DoSideBar=false;
	
	protected ABMContainer mExtraContent = new ABMContainer();
	protected ABMButton mExtraHalfFloatingButton = new ABMButton();
	
	protected boolean WithExtraContent=false;
	protected boolean WithExtraHalfButton=false;
	protected boolean WithExtraSearch=false;	
	protected String ExtraSearchID="";
	protected int ExtraSearchS=180;
	protected int ExtraSearchM=240;
	protected int ExtraSearchL=360;
	protected String ExtraSearchVisibility="";
	
	public boolean TopBarDropdownHideArrow=false;
	public String SideBarSubItemsArrowUnicodeChar="&#9660";
	public boolean SideBarSubItemsArrowAlignRight=false;
	protected boolean ExtraContentOpen=true;
	
	public boolean ShowDebugInfo=false;
	
	protected String AltScrollBarColor="";
	protected double AltScrollBarColorOpacity=0.5;
	
	protected String mHidden="";
	
	private int mRightToLeft=0;
	
	public boolean forceFloatIconsLeft=false;
	
	public void Initialize(ABMPage page, String id, String barType, String title, boolean topBarDropDownBelow, boolean topBarFixed, int sideBarWidthPx, int sideBarItemHeightPx, ABMComponent sideBarTopComponent, String sideBarCollapseType, String themeName) {
		this.Type = ABMaterial.UITYPE_NAVBAR;
		this.Page = page;
		this.ID = id;
		UseTheme(themeName);
		this.mTitle = title;
		this.BarType = barType;
		this.NBWidthPx = sideBarWidthPx;
		this.mSideBarTopComponent=sideBarTopComponent;
		this.TopBarDropDownBelow = topBarDropDownBelow;
		this.TopBarFixed=topBarFixed;
		this.HeightPx=sideBarItemHeightPx;
		this.SideBarCollapseType = sideBarCollapseType;
		IsInitialized=true;
	}
	
	public void UseAlternativeScrollBar(String color, String colorIntensity, double opacity) {
		AltScrollBarColor=ABMaterial.GetColorStrMap(color, colorIntensity);
		AltScrollBarColorOpacity=opacity;	
	}
	
	public void setRightToLeft(boolean rtl) {
		if (rtl) {
			mRightToLeft=1;
		} else {
			mRightToLeft=-1;
		}
	}
	
	public boolean getRightToLeft() {
		if (mRightToLeft==0) {
			return Page.getRightToLeft();
		} else {
			if (mRightToLeft==1) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	/*
	 * Returns a previously added item. Only works with items added with AddSideBarComponent, AddSidebarComponentEx, AddSidebarSubComponent or AddSidebarSubComponentEx
	 */
	public ABMComponent GetSidebarComponent(String returnName) {
		for (ABMNavBarItem item: SideItems) {
			if (item.ItemReturnName.equalsIgnoreCase(returnName)) {
				return item.Component;				
			}
		}		
		for (Entry<String,List<ABMNavBarItem>> entry: SideSubItems.entrySet()) {
			for (ABMNavBarItem item: entry.getValue()) {
				if (item.ItemReturnName.equalsIgnoreCase(returnName)) {
					return item.Component;						
				}
			}				
		}	
		BA.Log("No item found with return name " + returnName);
		return null;
	}
	
	public void setActiveTopReturnName(String returnName) {
		ActiveTopReturnName = returnName;
		DoTopBar=true;
	}
	
	public String getActiveTopReturnName() {
		return ActiveTopReturnName;
	}
	
	public void setActiveSideReturnName(String returnName) {
		ActiveSideReturnName = returnName;
		DoSideBar=true;
	}
	
	public String getActiveSideReturnName() {
		return ActiveSideReturnName;
	}
	
	public void setActiveSideSubReturnName(String returnName) {
		ActiveSideSubReturnName = returnName;
		DoSideBar=true;
	}
	
	public String getActiveSideSubReturnName() {
		return ActiveSideSubReturnName;
	}
	
	/**
	 * CAN NOT BE CALLED IN Build()!
	 */
	public void OpenExtraContent() {
		if (!WithExtraContent) {
			BA.LogError("No Extra Content initialized!");
			return;
		}
		if (Page!=null) {
			if (Page.ws!=null) {
				Page.ws.Eval("$('#abmextracontent').slideDown('fast')", null); 
				try {
					if (Page.ws.getOpen()) {
						if (Page.ShowDebugFlush) {BA.Log("NavigationBar OpenExtraContent: " + ID);}
						Page.ws.Flush();Page.RunFlushed();
						ExtraContentOpen=true;
					}
				} catch (IOException e) {			
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * CAN NOT BE CALLED IN Build()!
	 */
	public void CloseExtraContent() {
		if (!WithExtraContent) {
			BA.LogError("No Extra Content initialized!");
			return;
		}
		if (Page!=null) {
			if (Page.ws!=null) {
				Page.ws.Eval("$('#abmextracontent').slideUp('fast')", null); 
				try {
					if (Page.ws.getOpen()) {
						if (Page.ShowDebugFlush) {BA.Log("NavigationBar CloseExtraContent: " + ID);}
						Page.ws.Flush();Page.RunFlushed();
						ExtraContentOpen=false;
					}
				} catch (IOException e) {			
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * CAN NOT BE CALLED IN Build()!
	 */
	public void OpenExtraContent2(int speed) {
		if (!WithExtraContent) {
			BA.LogError("No Extra Content initialized!");
			return;
		}
		if (Page!=null) {
			if (Page.ws!=null) {
				Page.ws.Eval("$('#abmextracontent').slideDown(" + speed + ")", null); 
				try {
					if (Page.ws.getOpen()) {
						if (Page.ShowDebugFlush) {BA.Log("NavigationBar OpenExtraContent: " + ID);}
						Page.ws.Flush();Page.RunFlushed();
						ExtraContentOpen=true;
					}
				} catch (IOException e) {			
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * CAN NOT BE CALLED IN Build()!
	 */
	public void CloseExtraContent2(int speed) {
		if (!WithExtraContent) {
			BA.LogError("No Extra Content initialized!");
			return;
		}
		if (Page!=null) {
			if (Page.ws!=null) {
				Page.ws.Eval("$('#abmextracontent').slideUp(" + speed + ")", null); 
				try {
					if (Page.ws.getOpen()) {
						if (Page.ShowDebugFlush) {BA.Log("NavigationBar CloseExtraContent: " + ID);}
						Page.ws.Flush();Page.RunFlushed();
						ExtraContentOpen=false;
					}
				} catch (IOException e) {			
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * MUST BE INITIALIZED IN Build()!
	 */
	public void InitializeSearchButton(int smallWidthPx, int mediumWidthPx, int largeWidthPx, String visibility) {
		WithExtraSearch=true;
		ExtraSearchVisibility=visibility;
		ExtraSearchID = "abmnavextrasearch";		
		ExtraSearchS = smallWidthPx;
		ExtraSearchM = mediumWidthPx;
		ExtraSearchL = largeWidthPx;
	}
	
	/**
	 * Change the visibility of the Search button
	 */
	public void SetSearchButtonVisibility(String visibility) {
		ExtraSearchVisibility=visibility;		
	}
	
	public void ClearSearchText() {
		if (Page!=null) {
			if (Page.ws!=null) {
				Page.ws.Eval("$('#" + ExtraSearchID + "').val('');", null);
				try {
					if (Page.ws.getOpen()) {
						if (Page.ShowDebugFlush) {BA.Log("ClearSearchText" + ID);}
						Page.ws.Flush();Page.RunFlushed();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}	
	
	/**
	 * MUST BE INITIALIZED IN Build()!
	 * 
	 * You can then use the Page.NavigationBar.ExtraContent to build your grid and add components
	 * ExtraContent is an ABMContainer 
	 */ 
	public void InitializeExtraContent(String id, boolean open, String themeName) {
		if (WithExtraContent) {
			BA.LogError("Extra Content already initialized!");
			return;
		}
		WithExtraContent=true;
		mExtraContent.Initialize(Page, id, themeName);		
		ExtraContentOpen=open;
	}
	
	public ABMContainer getExtraContent() {
		if (!WithExtraContent) {
			BA.LogError("Extra Content has not been initialized!");
			return null;
		} 
		return mExtraContent;
	}
	
	/**
	 * MUST BE INITIALIZED IN Build()! 
	 * 
	 * use the ABM.HALFBUTTON_ constants for the position 
	 * use the ABM.BUTTONSIZE_ constants for the size
	 * 
	 * You can then use the Page.NavigationBar.ExtraHalfButton to access its properties (e.g. visibility)
	*/	
	public void InitializeExtraHalfButton(String id, String iconName, String size, String position, String themeName) {
		WithExtraHalfButton = true;
		mExtraHalfFloatingButton.InitializeFloating(Page, id, iconName, themeName);
		mExtraHalfFloatingButton.Size = size;
		mExtraHalfFloatingButton.HalfPosition = position;
	}	
	
	public ABMButton ExtraHalfButton() {
		if (!WithExtraHalfButton) {
			BA.LogError("Extra half button has not been initialized!");
			return null;
		} 
		return mExtraHalfFloatingButton;
	}
	
	/**
	 * Starts with the set opacity and on scroll goes to opacity 1
	 * On return to top, goes back to fromOpacity
	 * Note: the first row is the trigger!
	 */
	public void SetFadeEffect(double fromOpacity) {
		this.HasFadeEffect=true;
		this.FadeEffectValue = fromOpacity;
	}
	
	protected void ResetTheme() {
		UseTheme(Theme.ThemeName);
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	public void setTitle(String title) {
		mTitle = title;
	}
	
	public void setBeforeTitleHTML(String html) {
		mTitleHTML = html;
	}
		
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.NavBars.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.NavBars.get(themeName.toLowerCase()).Clone();				
			}
		}
	}
	
	/**
	 * Return the SideBar top component 
	 */
	public ABMComponent getSideBarTopComponent() {
		return mSideBarTopComponent;
	}
	
	/**
	 * Scrolls the side bar component into view. Only works with items added with AddSideBarComponent, AddSidebarComponentEx, AddSidebarSubComponent or AddSidebarSubComponentEx
	 */
	public void ScrollSideBarComponentIntoView(String returnName) {
		if (Page!=null) {
			ABMComponent comp = GetSidebarComponent(returnName);
			if (comp!=null) {
				Page.ws.Eval("$('#" + comp.getID() + "').parent()[0].scrollIntoView();", null);			
				try {
					if (Page.ws.getOpen()) {
						if (Page.ShowDebugFlush) {BA.Log("ScrollIntoView: " + ID);}
						Page.ws.Flush();Page.RunFlushed();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void Clear() {
		TopItems = new ArrayList<ABMNavBarItem>();
		TopSubItems = new HashMap<String,List<ABMNavBarItem>>();
		SideItems = new ArrayList<ABMNavBarItem>();
		SideSubItems = new HashMap<String,List<ABMNavBarItem>>();
		IDCounter=0;
		DoTopBar=true;
		DoSideBar=true;
		if (ShowDebugInfo) {
			BA.Log("[NB]Clear: Page=" + Page.Name + " DoTopBar=" + DoTopBar + " DoSideBar=" + DoSideBar + " IsBuild=" + IsBuild);
		}
	}
	
	public void ClearTopBarItems() {
		TopItems = new ArrayList<ABMNavBarItem>();
		TopSubItems = new HashMap<String,List<ABMNavBarItem>>();		
		IDCounter=0;
		DoTopBar=true;
		if (ShowDebugInfo) {
			BA.Log("[NB]ClearTopBarItems: Page=" + Page.Name + " DoTopBar=" + DoTopBar + " DoSideBar=" + DoSideBar + " IsBuild=" + IsBuild);
		}
	}
	
	public void ClearSideBarItems() {
		SideItems = new ArrayList<ABMNavBarItem>();
		SideSubItems = new HashMap<String,List<ABMNavBarItem>>();		
		DoSideBar=true;
		if (ShowDebugInfo) {
			BA.Log("[NB]ClearSideBarItems: Page=" + Page.Name + " DoTopBar=" + DoTopBar + " DoSideBar=" + DoSideBar + " IsBuild=" + IsBuild);
		}
	}
	
	public void AddTopItem(String topItemReturnName, String itemText, String iconName, String url, String visibility) {
		if (visibility.equalsIgnoreCase("true")) {
			visibility=ABMaterial.VISIBILITY_HIDE_ON_MEDIUM_AND_BELOW;
		}
		if (visibility.equalsIgnoreCase("false")) {
			visibility=ABMaterial.VISIBILITY_ALL;
		}
		ABMNavBarItem n = new ABMNavBarItem(topItemReturnName, itemText, iconName, url, visibility);
		IDCounter++;
		n.ID = ID.toLowerCase() + "TI" + IDCounter;
		TopItems.add(n);
		DoTopBar=true;	
	}
	
	/**
	 * Use the ABM.ICONALIGN_ constants 
	 */
	public void AddTopItemEx(String topItemReturnName, String itemText, String iconName, String url, String visibility, String iconColor, String iconColorIntensity, String iconAlign) {
		if (visibility.equalsIgnoreCase("true")) {
			visibility=ABMaterial.VISIBILITY_HIDE_ON_MEDIUM_AND_BELOW;
		}
		if (visibility.equalsIgnoreCase("false")) {
			visibility=ABMaterial.VISIBILITY_ALL;
		}
		ABMNavBarItem n = new ABMNavBarItem(topItemReturnName, itemText, iconName, url, visibility, iconColor, iconColorIntensity);
		n.IconAlign = iconAlign;		
		IDCounter++;
		n.ID = ID.toLowerCase() + "TI" + IDCounter;
		TopItems.add(n);		
		DoTopBar=true;	
	}

	public void AddTopItemWithSideBar(String topItemReturnName, String itemText, String iconName, String url, String visibility, ABMSideBar sideBar) {
		if (visibility.equalsIgnoreCase("true")) {
			visibility=ABMaterial.VISIBILITY_HIDE_ON_MEDIUM_AND_BELOW;
		}
		if (visibility.equalsIgnoreCase("false")) {
			visibility=ABMaterial.VISIBILITY_ALL;
		}
		ABMNavBarItem topItem = new ABMNavBarItem(topItemReturnName, itemText, iconName, url, visibility);
		sideBar.TopBarItemReturnName = topItemReturnName.toLowerCase();
		sideBar.ShowDebugInfo = ShowDebugInfo;
		topItem.SideBar = sideBar;
		IDCounter++;
		topItem.ID = ID.toLowerCase() + "TI" + IDCounter;
		TopItems.add(topItem);
		DoTopBar=true;
	}
	
	
	/**
	 * Use the ABM.ICONALIGN_ constants 
	 */
	public void AddTopItemWithSideBarEx(String topItemReturnName, String itemText, String iconName, String url, String visibility, String iconColor, String iconColorIntensity, String iconAlign, ABMSideBar sideBar) {
		if (visibility.equalsIgnoreCase("true")) {
			visibility=ABMaterial.VISIBILITY_HIDE_ON_MEDIUM_AND_BELOW;
		}
		if (visibility.equalsIgnoreCase("false")) {
			visibility=ABMaterial.VISIBILITY_ALL;
		}
		ABMNavBarItem topItem = new ABMNavBarItem(topItemReturnName, itemText, iconName, url, visibility, iconColor, iconColorIntensity);
		sideBar.TopBarItemReturnName = topItemReturnName.toLowerCase();
		sideBar.ShowDebugInfo = ShowDebugInfo;
		topItem.SideBar = sideBar;
		topItem.IconAlign = iconAlign;
		IDCounter++;
		topItem.ID = ID.toLowerCase() + "TI" + IDCounter;
		TopItems.add(topItem);		
		DoTopBar=true;		
	}
	
	public ABMSideBar GetSideBarFromTopItem(String topItemReturnName) {
		for (ABMNavBarItem item: TopItems) {
			if (item.SideBar!=null) {
				if (item.SideBar.TopBarItemReturnName.equalsIgnoreCase(topItemReturnName)) {
					return item.SideBar;
				}
			}
		}
		BA.Log("No sidebar found for topbar item: "+ topItemReturnName );
		return null;
	}
		
	public void AddTopFullScreenIcon() {
		TopItems.add(new ABMNavBarItem("togglefullscreen", "", "mdi-action-settings-overscan", "", ABMaterial.VISIBILITY_ALL));
		DoTopBar=true;	
	}
		
	public void AddTopSubItem(String topItemReturnName, String topSubItemReturnName, String itemText, String iconName, String url) {
		if (!TopSubItems.containsKey(topItemReturnName.toLowerCase())) {
			TopSubItems.put(topItemReturnName.toLowerCase(), new ArrayList<ABMNavBarItem>());
		}
		TopSubItems.get(topItemReturnName.toLowerCase()).add(new ABMNavBarItem(topSubItemReturnName, itemText , iconName, url, ABMaterial.VISIBILITY_ALL));
		DoTopBar=true;	
	}
	
	public void AddTopSubItemEx(String topItemReturnName, String topSubItemReturnName, String itemText, String iconName, String url, String visibility, String iconColor, String iconColorIntensity) {
		if (!TopSubItems.containsKey(topItemReturnName.toLowerCase())) {
			TopSubItems.put(topItemReturnName.toLowerCase(), new ArrayList<ABMNavBarItem>());
		}
		TopSubItems.get(topItemReturnName.toLowerCase()).add(new ABMNavBarItem(topSubItemReturnName, itemText , iconName, url, visibility, iconColor, iconColorIntensity));
		DoTopBar=true;
	}
	
	public void AddTopSubDivider(String topItemReturnName) { 
		if (!TopSubItems.containsKey(topItemReturnName.toLowerCase())) {
			TopSubItems.put(topItemReturnName.toLowerCase(), new ArrayList<ABMNavBarItem>());
		}
		TopSubItems.get(topItemReturnName.toLowerCase()).add(new ABMNavBarItem(Theme.ThemeName));
		DoTopBar=true;	
	}
	
	public void AddSideBarItem(String sideItemReturnName, String itemText, String iconName, String url) { 
		if (BarType.equals(ABMaterial.SIDEBAR_AUTO)) {
			BA.Log("AddSideBarItem ignored, barType = SIDEBAR_AUTO!");
			return;
		}
		SideItems.add(new ABMNavBarItem(sideItemReturnName, itemText, iconName, url, ABMaterial.VISIBILITY_ALL));
		DoSideBar=true;
	}
		
	public void AddSideBarComponent(String sideItemReturnName, ABMComponent component, String url) { 
		if (BarType.equals(ABMaterial.SIDEBAR_AUTO)) {
			BA.Log("AddSideBarItem ignored, barType = SIDEBAR_AUTO!");
			return;
		}
		SideItems.add(new ABMNavBarItem(sideItemReturnName, component, url, ABMaterial.VISIBILITY_ALL));
		DoSideBar=true;	
	}
	
	public void AddSideBarComponentEx(String sideItemReturnName, ABMComponent component, String url, String minHeight, boolean verticalCenter) { 
		if (BarType.equals(ABMaterial.SIDEBAR_AUTO)) {
			BA.Log("AddSideBarItem ignored, barType = SIDEBAR_AUTO!");
			return;
		}
		SideItems.add(new ABMNavBarItem(sideItemReturnName, component, url, ABMaterial.VISIBILITY_ALL, minHeight, verticalCenter));
		DoSideBar=true;
	}
	
	public void AddSideBarDivider() {
		if (BarType.equals(ABMaterial.SIDEBAR_AUTO)) {
			BA.Log("AddSideBarDivider ignored, barType = SIDEBAR_AUTO!");
			return;
		}
		SideItems.add(new ABMNavBarItem(Theme.ThemeName));
		DoSideBar=true;		
	}
	
	public void AddSideBarSubItem(String sideItemReturnName, String sideSubItemReturnName, String itemText, String iconName, String url) {
		if (BarType.equals(ABMaterial.SIDEBAR_AUTO)) {
			BA.Log("AddSideBarSubItem ignored, barType = SIDEBAR_AUTO!");
			return;
		}
		if (!SideSubItems.containsKey(sideItemReturnName.toLowerCase())) {
			SideSubItems.put(sideItemReturnName.toLowerCase(), new ArrayList<ABMNavBarItem>());
		}
		SideSubItems.get(sideItemReturnName.toLowerCase()).add(new ABMNavBarItem(sideSubItemReturnName, itemText, iconName, url, ABMaterial.VISIBILITY_ALL));
		DoSideBar=true;	
	}	
	
	public void AddSideBarSubItemEx(String sideItemReturnName, String sideSubItemReturnName, String itemText, String iconName, String url, String iconExtraStyle) {
		if (BarType.equals(ABMaterial.SIDEBAR_AUTO)) {
			BA.Log("AddSideBarSubItemEx ignored, barType = SIDEBAR_AUTO!");
			return;
		}
		if (!SideSubItems.containsKey(sideItemReturnName.toLowerCase())) {
			SideSubItems.put(sideItemReturnName.toLowerCase(), new ArrayList<ABMNavBarItem>());
		}
		ABMNavBarItem n = new ABMNavBarItem(sideSubItemReturnName, itemText, iconName, url, ABMaterial.VISIBILITY_ALL);
		n.iconExtraStyle = iconExtraStyle;
		SideSubItems.get(sideItemReturnName.toLowerCase()).add(n);
		DoSideBar=true;
	}
	
	public void AddSideBarSubComponent(String sideItemReturnName, String sideSubItemReturnName, ABMComponent component, String url) {
		if (BarType.equals(ABMaterial.SIDEBAR_AUTO)) {
			BA.Log("AddSideBarSubComponent ignored, barType = SIDEBAR_AUTO!");
			return;
		}
		if (!SideSubItems.containsKey(sideItemReturnName.toLowerCase())) {
			SideSubItems.put(sideItemReturnName.toLowerCase(), new ArrayList<ABMNavBarItem>());
		}
		SideSubItems.get(sideItemReturnName.toLowerCase()).add(new ABMNavBarItem(sideSubItemReturnName, component, url, ABMaterial.VISIBILITY_ALL));
		DoSideBar=true;	
	}	
	
	public void AddSideBarSubComponentEx(String sideItemReturnName, String sideSubItemReturnName, ABMComponent component, String url, String minHeight, boolean verticalCenter) {
		if (BarType.equals(ABMaterial.SIDEBAR_AUTO)) {
			BA.Log("AddSideBarSubComponent ignored, barType = SIDEBAR_AUTO!");
			return;
		}
		if (!SideSubItems.containsKey(sideItemReturnName.toLowerCase())) {
			SideSubItems.put(sideItemReturnName.toLowerCase(), new ArrayList<ABMNavBarItem>());
		}
		SideSubItems.get(sideItemReturnName.toLowerCase()).add(new ABMNavBarItem(sideSubItemReturnName, component, url, ABMaterial.VISIBILITY_ALL, minHeight, verticalCenter));
		DoSideBar=true;
	}	
	
	public void AddSideBarSubDivider(String sideItemReturnName) {
		if (BarType.equals(ABMaterial.SIDEBAR_AUTO)) {
			BA.Log("AddSideBarSubDivider ignored, barType = SIDEBAR_AUTO!");
			return;
		}
		if (!SideSubItems.containsKey(sideItemReturnName.toLowerCase())) {
			SideSubItems.put(sideItemReturnName.toLowerCase(), new ArrayList<ABMNavBarItem>());
		}
		SideSubItems.get(sideItemReturnName.toLowerCase()).add(new ABMNavBarItem(Theme.ThemeName));
		DoSideBar=true;		
	}
	
	public String getID() {
		return this.ID;
	}	
	
	@Hide
	protected void Prepare() {
		if (ShowDebugInfo) {
			BA.Log("[NB]Prepare: Page=" + Page.Name + " DoTopBar=" + DoTopBar + " DoSideBar=" + DoSideBar + " IsBuild=" + IsBuild);
		}
		this.IsBuild = true;
		DoTopBar=false;
		DoSideBar=false;
		if (WithExtraContent) {
			mExtraContent.Prepare();
		}
		if (WithExtraHalfButton) {
			this.mExtraHalfFloatingButton.Prepare();
		}
		for (ABMNavBarItem item: TopItems) {
			if (item.Component!=null) {
				item.Component.Prepare();
			}
			if (item.SideBar!=null) {
				item.SideBar.Prepare();
			}
			for (Entry<String,List<ABMNavBarItem>> si: TopSubItems.entrySet()) {
				for (ABMNavBarItem siitem: si.getValue()) {
					if (siitem.Component!=null) {
						siitem.Component.Prepare();
					}					
				}
			}
		}
		for (ABMNavBarItem item: SideItems) {
			if (item.Component!=null) {
				item.Component.Prepare();
			}
			if (item.SideBar!=null) {
				item.SideBar.Prepare();
			}
			for (Entry<String,List<ABMNavBarItem>> si: SideSubItems.entrySet()) {
				for (ABMNavBarItem siitem: si.getValue()) {
					if (siitem.Component!=null) {
						siitem.Component.Prepare();
					}					
				}
			}
		}
	}
	
	@Hide
	protected static class ABMNavBarItem implements java.io.Serializable{
		
		private static final long serialVersionUID = -8515941746626262188L;
		public String ItemText="";
		public String IconName="";
		public String IconAwesomeExtra="";
		public String ItemReturnName="";
		public Boolean IsDivider=false;
		public String InternalThemeName="";
		public String Url="";
		public String Visibility=ABMaterial.VISIBILITY_ALL;
		public ABMComponent Component=null;
		protected ABMSideBar SideBar=null;
		public String IconColor="";
		public String IconColorIntensity="";
		protected String ID="";
		public String IconBadge="";
		public String IconAlign="left";
		protected boolean IconBadgeChanged=false;
		protected String MinHeight="";
		protected boolean Center=false;
		protected String ToolTipText="";
		protected String ToolTipPosition=""; 
		protected int ToolTipDelay=0;
		protected String iconExtraStyle="";
		
		ABMNavBarItem() {
			
		}
		
		public void SetTooltip(String text, String position, int delay) {
			this.ToolTipText = text;
			this.ToolTipPosition = position;
			this.ToolTipDelay = delay;			
		}
		
		ABMNavBarItem(String itemReturnName, String itemText, String iconName, String url, String visibility) {
			this.ItemReturnName = itemReturnName;
			this.ItemText = itemText;
			this.IconName = iconName;
			this.Url = url;					
			this.Visibility=visibility;
		}

		ABMNavBarItem(String itemReturnName, String itemText, String iconName, String url, String visibility, String iconColor, String iconColorIntensity) {
			this.ItemReturnName = itemReturnName;
			this.ItemText = itemText;
			this.IconName = iconName;
			this.Url = url;
			this.Visibility=visibility;
			this.IconColor = iconColor;
			this.IconColorIntensity = iconColorIntensity;
		}
		
		ABMNavBarItem(String itemReturnName, ABMComponent comp, String url, String visibility, String minHeight, boolean center) {
			this.ItemReturnName = itemReturnName;
			this.Component = comp;
			this.Url = url;
			this.Visibility=visibility;
			this.MinHeight = minHeight;
			this.Center = center;			
		}

		ABMNavBarItem(String itemReturnName, ABMComponent comp, String url, String visibility) {
			this.ItemReturnName = itemReturnName;
			this.Component = comp;
			this.Url = url;
			this.Visibility=visibility;
		}
		
		ABMNavBarItem(String themeName) {
			this.IsDivider = true;
			this.InternalThemeName=themeName;
		}
		
		protected boolean HideOnMidAndDown() {			
			switch (Visibility) {
			case ABMaterial.VISIBILITY_HIDE_ALL:
				return false;				
			default:
				return true;
			}			
		}
		
		public ABMNavBarItem Clone() {
			ABMNavBarItem c = new ABMNavBarItem();
			c.ItemText = ItemText;
			c.IconName = IconName;
			c.ItemReturnName = ItemReturnName;
			c.IsDivider = IsDivider;
			c.InternalThemeName = InternalThemeName;
			c.Url=Url;
			c.Visibility=Visibility;
			c.Component = Component;
			c.IconAwesomeExtra=IconAwesomeExtra;
			c.IconColor = IconColor;
			c.IconColorIntensity=IconColorIntensity;
			c.ID=ID;
			c.IconBadge=IconBadge;
			c.IconBadgeChanged=IconBadgeChanged;
			c.Center = Center;
			c.MinHeight = MinHeight;
			c.ToolTipText=ToolTipText;
			c.ToolTipPosition=ToolTipPosition;
			c.ToolTipDelay=ToolTipDelay;
			c.iconExtraStyle=iconExtraStyle;
			return c;
		}
		
		
	}
	
	@Hide		
	protected String Build() {
		if (ShowDebugInfo) {
			BA.Log("[NB]Build: Page=" + Page.Name + " DoTopBar=" + DoTopBar + " DoSideBar=" + DoSideBar + " IsBuild=" + IsBuild);
		}
		if (Theme.ThemeName.equals("default")) {
			Theme.Colorize(Page.CompleteTheme.MainColor);
		}
		if (BarType.equals(ABMaterial.SIDEBAR_AUTO)) {
			SideItems = new ArrayList<ABMNavBarItem>();
			SideSubItems = new HashMap<String,List<ABMNavBarItem>>();
			if (!TopItems.isEmpty()) {
				for (ABMNavBarItem item: TopItems) {
					if (!item.IsDivider && item.HideOnMidAndDown()) {
						SideItems.add(item.Clone());
						if (TopSubItems.containsKey(item.ItemReturnName.toLowerCase())) {
							List<ABMNavBarItem> subitems = new ArrayList<ABMNavBarItem>();
							for (ABMNavBarItem subitem: TopSubItems.get(item.ItemReturnName.toLowerCase())) {
								subitems.add(subitem.Clone());
							}
							SideSubItems.put(item.ItemReturnName.toLowerCase(), subitems);
						}
					}
				}
			}
		}
		
		StringBuilder s = new StringBuilder();	
		ThemeNavigationBar nb = Theme;
		s.append("<div id=\"" + this.ID.toLowerCase() + "topsubs" + "\" class=\"no-print\">\n");
		s.append(BuildTopSubItems());
		s.append("</div>\n");
		
		s.append("<div id=\"" + this.ID.toLowerCase() + "extrasidebars" + "\" class=\"no-print\">\n");
		for (ABMNavBarItem item: TopItems) {
			if (item.SideBar!=null) {
				s.append(item.SideBar.Build());
			}
		}		
		s.append("</div>\n");
		
		String extraContent="";
		if (WithExtraContent) {
			extraContent = " nav-extended ";
		}
		if (WithExtraHalfButton) {
			extraContent = " nav-extended ";
		}
		String MMenu="";
		
		if (TopBarFixed) {
			s.append("<nav id=\"" + this.ID.toLowerCase() + "\" class=\"no-print " + ABMaterial.GetColorStr(nb.TopBarBackColor,nb.TopBarBackColorIntensity, "") + " fixed-top" + extraContent + " " + nb.TopBarZDepth + "\"" + MMenu + ">\n");			
		} else {
			s.append("<nav id=\"" + this.ID.toLowerCase() + "\" class=\"no-print " + ABMaterial.GetColorStr(nb.TopBarBackColor,nb.TopBarBackColorIntensity, "") + " floating-top" + extraContent + " " + nb.TopBarZDepth + "\"" + MMenu + ">\n");
		}
		
		String datasimplebar="";
		if (!AltScrollBarColor.equals("")) {
			datasimplebar = "data-simplebar";
		}
		String rtl="";
		if (this.getRightToLeft()) {
			rtl = " abmrtl ";
		}
		s.append(BuildBody());
		if (!SideItems.isEmpty()) {
			switch (BarType) {
			case ABMaterial.SIDEBAR_MANUAL_ALWAYSHIDE:
				s.append("<div class=\"no-print container\"><a id=\"sidenavbutton\" href=\"\" data-activates=\"nav-mobile\" class=\"button-collapse top-nav full\"><i id=\"" + this.ID.toLowerCase() + "menu\" class=\"mdi-navigation-menu " + ABMaterial.GetColorStr(nb.TopBarForeColor, nb.TopBarForeColorIntensity, "text") + " \"></i></a></div>\n");
				s.append("<ul id=\"nav-mobile\" class=\"no-print side-nav side-nav"+ nb.ThemeName.toLowerCase() + " " + ABMaterial.GetColorStr(nb.SideBarBackgroundColor, nb.SideBarBackgroundColorIntensity, "") + " " + nb.SideBarZDepth + "\" " + datasimplebar + rtl + ">\n");
				break;
			case ABMaterial.SIDEBAR_AUTO:
				s.append("<div class=\"container no-print\"><a id=\"sidenavbutton\" href=\"\" data-activates=\"nav-mobile\" class=\"button-collapse top-nav full hide-on-large-only\"><i id=\"" + this.ID.toLowerCase() + "menu\" class=\"mdi-navigation-menu " + ABMaterial.GetColorStr(nb.TopBarForeColor, nb.TopBarForeColorIntensity, "text") + " \"></i></a></div>\n");
				s.append("<ul id=\"nav-mobile\" class=\"no-print side-nav side-nav"+ nb.ThemeName.toLowerCase() + " " + ABMaterial.GetColorStr(nb.SideBarBackgroundColor, nb.SideBarBackgroundColorIntensity, "") + " " + nb.SideBarZDepth + "\" " + datasimplebar + rtl + ">\n");
				break;
			default:
				s.append("<div class=\"container no-print\"><a id=\"sidenavbutton\" href=\"\" data-activates=\"nav-mobile\" class=\"button-collapse top-nav full hide-on-large-only\"><i id=\"" + this.ID.toLowerCase() + "menu\" class=\"mdi-navigation-menu " + ABMaterial.GetColorStr(nb.TopBarForeColor, nb.TopBarForeColorIntensity, "text") + " \"></i></a></div>\n");
				s.append("<ul id=\"nav-mobile\" class=\"no-print side-nav side-nav"+ nb.ThemeName.toLowerCase() + " fixed " + ABMaterial.GetColorStr(nb.SideBarBackgroundColor, nb.SideBarBackgroundColorIntensity, "") + " " + nb.SideBarZDepth + "\" " + datasimplebar + rtl + ">\n");
				break;
			}
			s.append(BuildSideBar());
			s.append("</ul>\n");
		}
		DoTopBar=false;
		DoSideBar=false;
		IsBuild=true;
		return s.toString();
	}	
	
	public void UpdateTopbarItem(String topItemReturnName, String iconName, String itemText) {
		UpdateTopbarItemInternal(topItemReturnName, iconName, itemText, true);
	}
		
	private void UpdateTopbarItemInternal(String topItemReturnName, String iconName, String itemText, boolean DoFlush) {
		
		for (ABMNavBarItem item: TopItems) {
			if (item.ItemReturnName.equalsIgnoreCase(topItemReturnName)) {
				item.ItemText = itemText;
				item.IconName=iconName;
				
				ThemeNavigationBar nb = Theme;
				
				String ExtraStyleClass="";
				if (!item.IconColor.equals("")) {
					ExtraStyleClass = " " + ABMaterial.GetColorStr(item.IconColor, item.IconColorIntensity, "text") + " ";
				} else {
					ExtraStyleClass = " " + ABMaterial.GetColorStr(nb.TopBarForeColor, nb.TopBarForeColorIntensity, "text") + " ";
				}
				
				String icon="";
				if (!item.IconName.equals("")) {
					switch (item.IconName.substring(0, 3).toLowerCase()) {
					case "mdi":
						if (item.ItemText.equals("")) {
							icon="<i id=\"" + item.ID + "icon\" class=\"" + item.IconName + ExtraStyleClass + " " + item.IconAlign + "\" style=\"transform: initial;line-height: inherit; text-align: center; font-size: " + nb.TopBarIconSize + ";display: block; width: 2rem\">" + item.IconBadge + "</i>";
						} else {
							icon="<i id=\"" + item.ID + "icon\" class=\"" + item.IconName + ExtraStyleClass + " " + item.IconAlign + "\" style=\"transform: initial;line-height: inherit; text-align: center; font-size: " + nb.TopBarIconSize + ";display: block; width: 2rem\">" + item.IconBadge + "</i>";
						}
						break;
					case "fa ":
					case "fa-":
						if (item.ItemText.equals("")) {
							icon="<i id=\"" + item.ID + "icon\" class=\"" + item.IconAwesomeExtra + " " + item.IconName + ExtraStyleClass + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.TopBarIconSize + ";display: block; width: 2rem\">" + item.IconBadge + "</i>";
						} else {
							icon="<i id=\"" + item.ID + "icon\" class=\"" + item.IconAwesomeExtra + " " + item.IconName + ExtraStyleClass + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.TopBarIconSize + ";display: block; width: 2rem\">" + item.IconBadge + "</i>";
						}
						break;
					case "abm":
						if (item.ItemText.equals("")) {
							icon="<i id=\"" + item.ID + "icon\" class=\"" + ExtraStyleClass + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.TopBarIconSize + ";display: block; width: 2rem\">" + Page.svgIconmap.getOrDefault(item.IconName.toLowerCase(), "") + item.IconBadge + "</i>";
						} else {
							icon="<i id=\"" + item.ID + "icon\" class=\"" + ExtraStyleClass + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.TopBarIconSize + ";display: block; width: 2rem\">" + Page.svgIconmap.getOrDefault(item.IconName.toLowerCase(), "") + item.IconBadge + "</i>";
						}						
						break;
					default:								
						if (item.ItemText.equals("")) {
							icon="<i id=\"" + item.ID + "icon\" class=\"material-icons" + ExtraStyleClass + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.TopBarIconSize + ";display: block; width: 2rem\">" + ABMaterial.HTMLConv().htmlEscape(item.IconName, Page.PageCharset) + "" + item.IconBadge + "</i>";
						} else {
							icon="<i id=\"" + item.ID + "icon\" class=\"material-icons" + ExtraStyleClass + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.TopBarIconSize + ";display: block; width: 2rem\">" + ABMaterial.HTMLConv().htmlEscape(item.IconName, Page.PageCharset) + "" + item.IconBadge + "</i>";
						}
					}
				}				
				
				String html="";
				if (!TopSubItems.containsKey(item.ItemReturnName.toLowerCase())) {
					if (item.SideBar!=null) {
						if (item.ItemReturnName.equalsIgnoreCase(ActiveTopReturnName)) {
							html = icon + BuildText(item.ItemText);
						} else {
							html = icon + BuildText(item.ItemText);
						}
					} else {
						if (item.ItemReturnName.equalsIgnoreCase(ActiveTopReturnName)) {
							html = icon + BuildText(item.ItemText);
						} else {
							html = icon + BuildText(item.ItemText);
						}
					}
				} else { // dropdown
					if (!item.ItemText.equals("")) {
						if (!TopBarDropdownHideArrow) {
							html = icon + BuildText(item.ItemText) + "<span class=\"caret\">&#9660;</span>";
						} else {
							html = icon + BuildText(item.ItemText);
						}
					} else {
						if (!TopBarDropdownHideArrow) {
							html = icon + "<span class=\"caret\">&nbsp;&#9660;</span>";
						} else {
							html = icon;
						}
						
					}
				}
				Page.ws.Eval("$('#" + item.ID + "').tooltip(\"remove\");", null);
				if (!item.ToolTipText.equals("")) {
					Page.ws.Eval("$('#" + item.ID + "').tooltip({delay:" + item.ToolTipDelay + "});", null);
				}
				if (!html.equals("")) {
					html = "$('#" + item.ID + "').find('.abmicbdg').html('" + html + "');";
					Page.ws.Eval(html, null);
					if (DoFlush) {
						try {
							if (Page.ws.getOpen()) {
								if (Page.ShowDebugFlush) {BA.Log("NavigationBar UpdateTopBarItem: " + ID);}
								Page.ws.Flush();Page.RunFlushed();
							}
						} catch (IOException e) {			
							e.printStackTrace();
						}
					}
				}
				
				return;
			}
		}		
	}
		
	/**
	 * Does not work in BuildPage()!
	 *  
	 * position: Use one of the ABM.ICONBADGE_POSITION_ constants
	 */
	public void SetTopBarBadgeOnIcon(String topItemReturnName, String value, String position, String backgroundColor, String backgroundColorIntensity, String foreColor, String foreColorIntensity,String borderColor, String borderColorIntensity) {
		for (ABMNavBarItem item: TopItems) {
			if (item.ItemReturnName.equalsIgnoreCase(topItemReturnName)) {
				switch (position) {
				case ABMaterial.ICONBADGE_POSITION_BOTTOMLEFT:
				case ABMaterial.ICONBADGE_POSITION_TOPLEFT:
					position+=" iosb-fontextraleft "; 
					break;
				case ABMaterial.ICONBADGE_POSITION_BOTTOMRIGHT:
				case ABMaterial.ICONBADGE_POSITION_TOPRIGHT:
					position+=" iosb-fontextraright ";
					break;
				}			
				
				
				if (value.matches("[-+]?\\d*\\.?\\d+")) {
					item.IconBadge = "<div class=\"iosb iosb-24 " + position + "\" style=\"background: " + ABMaterial.GetColorStrMap(borderColor, borderColorIntensity) + " !important\"><div class=\"iosb-inner\" style=\"background: " + ABMaterial.GetColorStrMap(backgroundColor, backgroundColorIntensity) + " !important\"><div class=\"iosb-content iosb-number\" style=\"color: " + ABMaterial.GetColorStrMap(foreColor, foreColorIntensity) + " !important\">" + ABMaterial.HTMLConv().htmlEscape(value, Page.PageCharset) + "</div></div></div>";
				} else {
					item.IconBadge = "<div class=\"iosb iosb-24 " + position + "\" style=\"background: " + ABMaterial.GetColorStrMap(borderColor, borderColorIntensity) + " !important\"><div class=\"iosb-inner\" style=\"background: " + ABMaterial.GetColorStrMap(backgroundColor, backgroundColorIntensity) + " !important\"><div class=\"iosb-content iosb-string\" style=\"color: " + ABMaterial.GetColorStrMap(foreColor, foreColorIntensity) + " !important\">" + ABMaterial.HTMLConv().htmlEscape(value, Page.PageCharset) + "</div></div></div>";
				}
				item.IconBadgeChanged = true;
				UpdateTopbarItem(topItemReturnName, item.IconName, item.ItemText);
				break;
			}
		}			
	}
	
	public void SetTopBarTooltip(String topItemReturnName, String text, String position, int delay) {
		for (ABMNavBarItem item: TopItems) {
			if (item.ItemReturnName.equalsIgnoreCase(topItemReturnName)) {
				item.ToolTipText = text;
				item.ToolTipPosition = position;
				item.ToolTipDelay = delay;
			}
		}
	}
	
	public void HideTopBarBadgeOnIcon(String topItemReturnName) {
		for (ABMNavBarItem item: TopItems) {
			if (item.ItemReturnName.equalsIgnoreCase(topItemReturnName)) {
				item.IconBadge = "";
				item.IconBadgeChanged=true;
				UpdateTopbarItem(topItemReturnName, item.IconName, item.ItemText);
				break;
			}
		}
	}
	
	public void SetVisibilityTopbarItem(String topItemReturnName, String visibility, boolean DoFlush) {
		for (ABMNavBarItem item: TopItems) {
			if (item.ItemReturnName.equalsIgnoreCase(topItemReturnName)) {
				ABMaterial.ChangeVisibilityNoLowercase(Page, item.ID, visibility);
				if (DoFlush) {
					try {
						if (Page.ws.getOpen()) {
							if (Page.ShowDebugFlush) {BA.Log("SetVisibilityTopbarItem: " + topItemReturnName);}
							Page.ws.Flush();Page.RunFlushed();
						}
					} catch (IOException e) {			
						e.printStackTrace();
					}
				}
				return;
			}
		}
	}
	
	public void UpdateTopbarItemEx(String topItemReturnName, String iconName, String itemText, String iconColor, String iconColorIntensity) {
		for (ABMNavBarItem item: TopItems) {
			if (item.ItemReturnName.equalsIgnoreCase(topItemReturnName)) {
				item.ItemText = itemText;
				item.IconColor=iconColor;
				item.IconName=iconName;
				item.IconColorIntensity=iconColorIntensity;
				
				ThemeNavigationBar nb = Theme;
				
				String ExtraStyleClass="";
				if (!item.IconColor.equals("")) {
					ExtraStyleClass = " " + ABMaterial.GetColorStr(item.IconColor, item.IconColorIntensity, "text") + " ";
				} else {
					ExtraStyleClass = " " + ABMaterial.GetColorStr(nb.TopBarForeColor, nb.TopBarForeColorIntensity, "text") + " ";
				}
				
				String forceFloat="";
				if (forceFloatIconsLeft) {
					forceFloat = ";float:  left";
				}
				
				String icon="";
				if (!item.IconName.equals("")) {
					switch (item.IconName.substring(0, 3).toLowerCase()) {
					case "mdi":
						if (item.ItemText.equals("")) {
							icon="<i id=\"" + item.ID + "icon\" class=\"" + item.IconName + ExtraStyleClass + " " + item.IconAlign + "\" style=\"transform: initial;line-height: inherit; text-align: center; font-size: " + nb.TopBarIconSize + ";display: block; width: 2rem\">" + item.IconBadge + "</i>";
						} else {
							icon="<i id=\"" + item.ID + "icon\" class=\"" + item.IconName + ExtraStyleClass + " " + item.IconAlign + "\" style=\"transform: initial;line-height: inherit; text-align: center; font-size: " + nb.TopBarIconSize + ";display: block; width: 2rem\">" + item.IconBadge + "</i>";
						}
						break;
					case "fa ":
					case "fa-":
						if (item.ItemText.equals("")) {
							icon="<i id=\"" + item.ID + "icon\" class=\"" + item.IconAwesomeExtra + " " + item.IconName + ExtraStyleClass + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.TopBarIconSize + ";display: block; width: 2rem" + forceFloat + "\">" + item.IconBadge + "</i>";
						} else {
							icon="<i id=\"" + item.ID + "icon\" class=\"" + item.IconAwesomeExtra + " " + item.IconName + ExtraStyleClass + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.TopBarIconSize + ";display: block; width: 2rem" + forceFloat + "\">" + item.IconBadge + "</i>";
						}
						break;
					case "abm":
						if (item.ItemText.equals("")) {
							icon="<i id=\"" + item.ID + "icon\" class=\"" + ExtraStyleClass + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.TopBarIconSize + ";display: block; width: 2rem\">" + Page.svgIconmap.getOrDefault(item.IconName.toLowerCase(), "") + item.IconBadge + "</i>";
						} else {
							icon="<i id=\"" + item.ID + "icon\" class=\"" + ExtraStyleClass + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.TopBarIconSize + ";display: block; width: 2rem\">" + Page.svgIconmap.getOrDefault(item.IconName.toLowerCase(), "") + item.IconBadge + "</i>";
						}
						break;
					default:								
						if (item.ItemText.equals("")) {
							icon="<i id=\"" + item.ID + "icon\" class=\"material-icons" + ExtraStyleClass + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.TopBarIconSize + ";display: block; width: 2rem\">" + ABMaterial.HTMLConv().htmlEscape(item.IconName, Page.PageCharset) + "" + item.IconBadge + "</i>";
						} else {
							icon="<i id=\"" + item.ID + "icon\" class=\"material-icons" + ExtraStyleClass + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.TopBarIconSize + ";display: block; width: 2rem\">" + ABMaterial.HTMLConv().htmlEscape(item.IconName, Page.PageCharset) + "" + item.IconBadge + "</i>";
						}
					}
				}		
				
				String html="";
				
				if (!TopSubItems.containsKey(item.ItemReturnName.toLowerCase())) {
					if (item.SideBar!=null) {
						if (item.ItemReturnName.equalsIgnoreCase(ActiveTopReturnName)) {
							html = icon + BuildText(item.ItemText);
						} else {
							html = icon + BuildText(item.ItemText);
						}
					} else {
						if (item.ItemReturnName.equalsIgnoreCase(ActiveTopReturnName)) {
							html = icon + BuildText(item.ItemText);
						} else {
							html = icon + BuildText(item.ItemText);
						}
					}
				} else { // dropdown
					if (!item.ItemText.equals("")) {
						if (!TopBarDropdownHideArrow) {
							html = icon + BuildText(item.ItemText) + "<span class=\"caret\">&#9660;</span>";
						} else {
							html = icon + BuildText(item.ItemText);
						}
					} else {
						if (!TopBarDropdownHideArrow) {
							html = icon + "<span class=\"caret\">&nbsp;&#9660;</span>";
						} else {
							html = icon;
						}
						
					}
				}
				if (!html.equals("")) {
					html = "$('#" + item.ID + "').find('.abmicbdg').html('" + html + "');";
					Page.ws.Eval(html, null);
					try {
						if (Page.ws.getOpen()) {
							if (Page.ShowDebugFlush) {BA.Log("NavigationBar UpdateTopBarItemEx: " + ID);}
							Page.ws.Flush();Page.RunFlushed();
						}
					} catch (IOException e) {			
						e.printStackTrace();
					}
				}
				
				return;
			}	
		}		
	}
			
	public void Refresh() {
		if (ShowDebugInfo) {
			BA.Log("[NB]Refresh: Page=" + Page.Name + " DoTopBar=" + DoTopBar + " DoSideBar=" + DoSideBar + " IsBuild=" + IsBuild);
		}
		if (BarType.equals(ABMaterial.SIDEBAR_AUTO)) {
			SideItems = new ArrayList<ABMNavBarItem>();
			SideSubItems = new HashMap<String,List<ABMNavBarItem>>();
			if (!TopItems.isEmpty()) {
				for (ABMNavBarItem item: TopItems) {
					//if (!item.IsDivider && item.HideOnMidAndDown) {
					if (!item.IsDivider && item.HideOnMidAndDown()) {
						SideItems.add(item.Clone());
						if (TopSubItems.containsKey(item.ItemReturnName.toLowerCase())) {
							List<ABMNavBarItem> subitems = new ArrayList<ABMNavBarItem>();
							for (ABMNavBarItem subitem: TopSubItems.get(item.ItemReturnName.toLowerCase())) {
								subitems.add(subitem.Clone());
							}
							SideSubItems.put(item.ItemReturnName.toLowerCase(), subitems);
						}
					}
				}
			}
		}
		
		JQueryElement j = Page.ws.GetElementById("pagenavbar");
		
		if (mTitleComp==null) {
			if (mTitleHTML.equals("")) {			
				j.SetHtml(BuildText(mTitle));
			} else {
				j.SetHtml(mTitleHTML + BuildText(mTitle));
			}	
		} else {
			j.SetHtml(mTitleComp.Build());			
		}
		
		if (DoTopBar) {
			j = Page.ws.GetElementById(this.ID.toLowerCase() + "topsubs");
			j.SetHtml(BuildTopSubItems());
		}
				
		if (DoTopBar) {
			String prevSBID="";
			for (ABMNavBarItem item: TopItems) {
				if (item.SideBar!=null) {				
					if (item.SideBar.IsBuild) {
						item.SideBar.RefreshInternal(false);
					} else {
						if (prevSBID.equals("")) {
							ABMaterial.AddHTML(Page, this.ID.toLowerCase() + "extrasidebars", item.SideBar.Build());							
						} else {											
							ABMaterial.InsertHTMLAfter(Page, prevSBID, item.SideBar.Build());	
						}
					}			
				
					prevSBID = item.SideBar.ID.toLowerCase();
				}
			}
		}
		
		j = Page.ws.GetElementById(this.ID.toLowerCase());
		ThemeNavigationBar nb = Theme;
		String extraContent="";
		if (WithExtraContent) {
			extraContent = " nav-extended ";
		}
		if (WithExtraHalfButton) {
			extraContent = " nav-extended ";
		}
		if (TopBarFixed) {
			j.SetProp("class", ABMaterial.GetColorStr(nb.TopBarBackColor,nb.TopBarBackColorIntensity, "") + " fixed-top" + extraContent + " no-print" + " " + nb.TopBarZDepth);
		} else {
			j.SetProp("class", ABMaterial.GetColorStr(nb.TopBarBackColor,nb.TopBarBackColorIntensity, "") + " floating-top" + extraContent + " no-print" + " " + nb.TopBarZDepth);
		}
		if (DoTopBar) {
			j.SetHtml(BuildBody());
		}	
		
		String rtl="";
		if (this.getRightToLeft()) {
			rtl = " abmrtl ";
		}
		if (!SideItems.isEmpty()) {
			switch (BarType) {
			case ABMaterial.SIDEBAR_MANUAL_ALWAYSHIDE:
				j = Page.ws.GetElementById(this.ID.toLowerCase() + "menu");
				j.SetProp("class", "mdi-navigation-menu " + ABMaterial.GetColorStr(nb.TopBarForeColor, nb.TopBarForeColorIntensity, "text"));
				j = Page.ws.GetElementById("nav-mobile");
				j.SetProp("class", "no-print side-nav side-nav"+ nb.ThemeName.toLowerCase() + " " + ABMaterial.GetColorStr(nb.SideBarBackgroundColor, nb.SideBarBackgroundColorIntensity, "") + " " + nb.SideBarZDepth + rtl);
				break;
			case ABMaterial.SIDEBAR_AUTO:
				j = Page.ws.GetElementById(this.ID.toLowerCase() + "menu");
				j.SetProp("class", "mdi-navigation-menu " + ABMaterial.GetColorStr(nb.TopBarForeColor, nb.TopBarForeColorIntensity, "text"));
				j = Page.ws.GetElementById("nav-mobile");
				j.SetProp("class", "no-print side-nav side-nav"+ nb.ThemeName.toLowerCase() + " " + ABMaterial.GetColorStr(nb.SideBarBackgroundColor, nb.SideBarBackgroundColorIntensity, "") + " " + nb.SideBarZDepth + rtl);
			default:
				j = Page.ws.GetElementById(this.ID.toLowerCase() + "menu");
				j.SetProp("class", "mdi-navigation-menu " + ABMaterial.GetColorStr(nb.TopBarForeColor, nb.TopBarForeColorIntensity, "text"));
				j = Page.ws.GetElementById("nav-mobile");
				j.SetProp("class", "no-print side-nav side-nav"+ nb.ThemeName.toLowerCase() + " fixed " + ABMaterial.GetColorStr(nb.SideBarBackgroundColor, nb.SideBarBackgroundColorIntensity, "") + " " + nb.SideBarZDepth + rtl);
				break;
			}
			if (DoSideBar) {
				j.SetHtml(BuildSideBar());
			}
		} else {
			if (DoSideBar) {
				j = Page.ws.GetElementById("nav-mobile");
				j.SetHtml(BuildSideBar());
			}
		}
		
		if (DoTopBar) {
			if (WithExtraContent) {
				if (ExtraContentOpen) {
					Page.ws.Eval("$('#abmextracontent').css('display', 'block');", null);
				} else {
					Page.ws.Eval("$('#abmextracontent').css('display', 'none');", null);
				}
			}
		}
		
		
		for (ABMNavBarItem item: TopItems) {
			UpdateTopbarItemInternal(item.ItemReturnName, item.IconName, item.ItemText, false);		
			Page.ws.Eval("$('#" + item.ID + "').tooltip(\"remove\");", null);
			if (!item.ToolTipText.equals("")) {
				Page.ws.Eval("$('#" + item.ID + "').tooltip({delay:" + item.ToolTipDelay + "});", null);
			}			
		}		
		
		Page.ws.RunFunction("initplugins", null);
		Page.ws.RunFunction("reinitdropdownhover", null);
		if (DoTopBar) {
			StringBuilder se = new StringBuilder();
			for (ABMNavBarItem item: TopItems) {
				if (item.SideBar!=null) {
					String exPos = "right";
					se.append("$('#btnextrasidebar" + item.SideBar.ID.toLowerCase() + "').abmsideNav({edge: '" + exPos + "', closeOnClick: false, menuWidth: "  + item.SideBar.WidthPx + ", menuWidthS: "  + item.SideBar.WidthSPx + ", menuWidthM: "  + item.SideBar.WidthMPx + ", menuWidthL: "  + item.SideBar.WidthPx + " });");
				}
			}
			
			Page.ws.Eval(se.toString(), null);
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("NavigationBar Refresh 1: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
		
		if (DoSideBar) {
			if (this.mSideBarTopComponent!=null) {
				switch (this.mSideBarTopComponent.Type) {
				case ABMaterial.UITYPE_ABMCONTAINER:
					ABMContainer cont = (ABMContainer) this.mSideBarTopComponent;
					cont.RunAllFirstRunsInternal(false);
					break;
				case ABMaterial.UITYPE_CUSTOMCARD:
					ABMCustomCard card = (ABMCustomCard) this.mSideBarTopComponent;
					card.FirstRun();
					break;
				default:
					this.mSideBarTopComponent.FirstRun();
				}
			}		
		
			for (ABMNavBarItem item: SideItems) {
				if (item.Component!=null) {
					switch (item.Component.Type) {
					case ABMaterial.UITYPE_ABMCONTAINER:
						ABMContainer cont = (ABMContainer) item.Component;
						cont.RunAllFirstRunsInternal(false);
						break;
					case ABMaterial.UITYPE_CUSTOMCARD:
						ABMCustomCard card = (ABMCustomCard) item.Component;
						card.FirstRun();
						break;
					default:
						item.Component.FirstRun();
					}
				}
			}
		}
		
		if (DoTopBar) {
			for (ABMNavBarItem item: TopItems) {
				if (item.SideBar!=null) {
					item.SideBar.Content.SetEventHandler();
					item.SideBar.FirstRun();				
				}
			}
					
			if (WithExtraContent) {
				mExtraContent.SetEventHandler();
				mExtraContent.RunAllFirstRunsInternal(false);
			}
			if (WithExtraHalfButton && mExtraHalfFloatingButton.IsInitialized) {
				mExtraHalfFloatingButton.FirstRun();
							
			}
			if (WithExtraSearch) {
				StringBuilder se = new StringBuilder();
				se.append("$('#" + ExtraSearchID + "').off('keydown').on('keydown', function(e) {");
				se.append("if (e.keyCode==13) {");
				se.append("b4j_raiseEvent('page_parseevent',{'eventname':'page_navigationbarsearch','eventparams':'search','search':$('#" + ExtraSearchID + "').val()});");
				se.append("$('body').removeClass('search-open');");
				se.append("}");
				se.append("});");
				Page.ws.Eval(se.toString(), null);
			}
		}
		DoTopBar=false;
		DoSideBar=false;
		
		if (mHidden.equals("")) {
			Page.ws.Eval("$('header').removeClass('hide')", null);
		} else {
			Page.ws.Eval("$('header').addClass('hide')", null);
		}
		
		if (!AltScrollBarColor.equals("")) {
			Page.ws.Eval("var el = new SimpleBar(document.getElementById('nav-mobile'));el.recalculate();", null);
		}
		
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("NavigationBar Refresh 2: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
		
	}
	
	@Hide
	protected String BuildTopSubItems() {
		StringBuilder s = new StringBuilder();	
		ThemeNavigationBar nb = Theme;
		
		String forceFloat="";
		if (forceFloatIconsLeft) {
			forceFloat = ";float:  left";
		}
		
		if (!TopItems.isEmpty()) {
			for (ABMNavBarItem item: TopItems) {
				if (TopSubItems.containsKey(item.ItemReturnName.toLowerCase())) {
					// drop down lists
					s.append("<ul id=\"" + item.ItemReturnName + "dropdown1\" class=\"dropdown-content dropdown-content-n-" + nb.ThemeName.toLowerCase() + " " + ABMaterial.GetColorStr(nb.TopBarSubBackColor,nb.TopBarSubBackColorIntensity, "") + "\" style=\"white-space: nowrap\">\n");
					for (ABMNavBarItem subitem: TopSubItems.get(item.ItemReturnName.toLowerCase())) {
						String active="";
						if (subitem.ItemReturnName.equalsIgnoreCase(ActiveTopReturnName)) {active="class=\"active active2\" ";}
						String icon="";	
						String ExtraStyle="";
						if (!subitem.IconColor.equals("")) {
							if (subitem.IconColor.startsWith("#")) {
								ExtraStyle = ";color: " + subitem.IconColor + ";";
							} else {
								ExtraStyle = ";color: " + ABMaterial.GetColorStrMap(subitem.IconColor, subitem.IconColorIntensity) + ";";
							}
						}
						
						if (!subitem.IconName.equals("")) {
							switch (subitem.IconName.substring(0, 3).toLowerCase()) {
							case "mdi":
								if (subitem.ItemText.equals("")) {
									icon="<i class=\"" + subitem.IconName + "\" style=\"transform: initial;line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ExtraStyle + "\"></i>";
								} else {
									icon="<i class=\"" + subitem.IconName + " " + item.IconAlign + "\" style=\"transform: initial;line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ExtraStyle + "\"></i>";
								}
								break;
							case "fa ":
							case "fa-":
								if (subitem.ItemText.equals("")) {
									icon="<i class=\"" + subitem.IconAwesomeExtra + " " + subitem.IconName + "\" style=\"line-height: inherit; text-align: left; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + forceFloat + "" + ExtraStyle + "margin-right: 0.5rem;\"></i>";
								} else {
									icon="<i class=\"" + subitem.IconAwesomeExtra + " "+ subitem.IconName + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: left; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + forceFloat + "" + ExtraStyle + "margin-right: 0.5rem;\"></i>";
								}
								break;
							case "abm":
								if (subitem.ItemText.equals("")) {
									icon="<i class=\"\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ExtraStyle + "\">" + Page.svgIconmap.getOrDefault(subitem.IconName.toLowerCase(), "") + "</i>";
								} else {
									icon="<i class=\"" + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ExtraStyle + "\">" + Page.svgIconmap.getOrDefault(subitem.IconName.toLowerCase(), "") + "</i>";
								}
								break;
							default:								
								if (subitem.ItemText.equals("")) {
									icon="<i class=\"material-icons\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ExtraStyle + "\">" + ABMaterial.HTMLConv().htmlEscape(subitem.IconName, Page.PageCharset) + "</i>";
								} else {
									icon="<i class=\"material-icons " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ExtraStyle + "\">" + ABMaterial.HTMLConv().htmlEscape(subitem.IconName, Page.PageCharset) + "</i>";
								}
							}
							
						}
						if (subitem.IsDivider) {							
							s.append("<li class=\"divider " + ABMaterial.GetColorStr(nb.TopBarSubDividerColor, nb.TopBarSubDividerColorIntensity, "") + "\"></li>\n");
						} else {
							String bold="";
							if (nb.TopBarBold) {bold=" bold ";}
							String FullScreenToggle = "";
							if (subitem.ItemReturnName.equalsIgnoreCase("togglefullscreen")) {
								FullScreenToggle = " toggle-fullscreen " ;
							}
							String tmpUrl = subitem.Url;
							
							String fntStyle="";
							if (!nb.SideBarSubFontSize.equals("")) {
								fntStyle=" style=\"font-size: " + nb.SideBarSubFontSize + "\" ";								
							}
							String marginStyle="";
							if (!icon.equals("")) {
								marginStyle=" style=\"margin-right: 32px\" ";
							}
							
							s.append("<li id=\"" + subitem.ItemReturnName.toLowerCase() + "\" " + active + " onclick=\"navclick('" + Page.Name.toLowerCase() + "','" + subitem.ItemReturnName + "','" + tmpUrl + "')\"" + marginStyle + "><" + nb.SideBarTextSize + " class=\"" + FullScreenToggle + bold + ABMaterial.GetColorStr(nb.TopBarSubForeColor, nb.TopBarSubForeColorIntensity, "text") + " " + ABMaterial.GetWavesEffect(nb.SideBarWavesEffect, nb.SideBarWavesCircle) + " " + "\" " + fntStyle + ">" + icon + BuildText(subitem.ItemText) + "</" + nb.SideBarTextSize + "></li>\n" );
						}
					}
					s.append("</ul>\n");
				}
			}
		}
		return s.toString();
	}
	
	
	@Hide
	protected String BuildBody() {
		StringBuilder s = new StringBuilder();	
		ThemeNavigationBar nb = Theme;
		
		s.append("<div>");
		
		if (nb.TopBarWide) {
			s.append("<div class=\"nav-wrapper containerW\">\n");
		} else {
			s.append("<div class=\"nav-wrapper container\">\n");
		}
				
		String Extra="";
		
		if (mTitleComp==null) {
			switch (BarType) {
			case ABMaterial.SIDEBAR_MANUAL_ALWAYSHIDE:
				if (mTitleHTML.equals("")) {
					s.append("<a id=\"pagenavbar\" class=\"brand-logo " + ABMaterial.GetColorStr(nb.TopBarTitleColor, nb.TopBarTitleColorIntensity, "text") + " " + nb.TopBarTitleVisibility + " center\">" + BuildText(mTitle) + "</a>");
				} else {
					s.append(mTitleHTML + "<a id=\"pagenavbar\" class=\"brand-logo " + ABMaterial.GetColorStr(nb.TopBarTitleColor, nb.TopBarTitleColorIntensity, "text") + " " + nb.TopBarTitleVisibility + " center\">" + BuildText(mTitle) + "</a>");
				}
				break;
			default:
				if (mTitleHTML.equals("")) {
					s.append("<a id=\"pagenavbar\" class=\"brand-logo " + ABMaterial.GetColorStr(nb.TopBarTitleColor, nb.TopBarTitleColorIntensity, "text") + " " + nb.TopBarTitleVisibility + "\">" + BuildText(mTitle) + "</a>\n");
				} else {
					s.append(mTitleHTML + "<a id=\"pagenavbar\" class=\"brand-logo " + ABMaterial.GetColorStr(nb.TopBarTitleColor, nb.TopBarTitleColorIntensity, "text") + " " + nb.TopBarTitleVisibility + "\">" + BuildText(mTitle) + "</a>\n");
				}
				break;
			}
		} else {
			switch (BarType) {
			case ABMaterial.SIDEBAR_MANUAL_ALWAYSHIDE:
				s.append("<a id=\"pagenavbar\" class=\"brand-logo " + ABMaterial.GetColorStr(nb.TopBarTitleColor, nb.TopBarTitleColorIntensity, "text") + " " + nb.TopBarTitleVisibility + " center\">" + mTitleComp.Build() + "</a>");
				break;
			default:
				s.append("<a id=\"pagenavbar\" class=\"brand-logo " + ABMaterial.GetColorStr(nb.TopBarTitleColor, nb.TopBarTitleColorIntensity, "text") + " " + nb.TopBarTitleVisibility + "\">" + mTitleComp.Build() + "</a>\n");
				break;
			}
		}
		
		Extra = "";		
		
		String forceFloat="";
		if (forceFloatIconsLeft) {
			forceFloat = ";float:  left";
		}
		
		if (!TopItems.isEmpty() || WithExtraSearch) {
			s.append("<ul class=\"right\">\n");
			
			for (ABMNavBarItem item: TopItems) {
				
				String active="";
				Extra = " " + item.Visibility + " ";
				
				String ExtraStyleClass="";
				if (!item.IconColor.equals("")) {
					ExtraStyleClass = " " + ABMaterial.GetColorStr(item.IconColor, item.IconColorIntensity, "text") + " ";
				} else {
					ExtraStyleClass = " " + ABMaterial.GetColorStr(nb.TopBarForeColor, nb.TopBarForeColorIntensity, "text") + " ";
				}
				String toolTip="";
				if (!item.ToolTipText.equals("")) {
					toolTip=" data-position=\"" + item.ToolTipPosition + "\" data-delay=\"" + item.ToolTipDelay + "\" data-tooltip=\"" + item.ToolTipText + "\" "; 
				}
				if (item.ItemReturnName.equalsIgnoreCase(ActiveTopReturnName)) {
					if (nb.TopBarBold) {
						active=" class=\"bold active active2 " + Extra + "\" ";
					} else {
						active=" class=\"active active2 " + Extra + "\" ";
					}
				} else {
					if (nb.TopBarBold) {
						active=" class=\"bold" + Extra + "\" ";
					} else {
						active=" class=\"" + Extra + "\" "; 
					}
				}
				
				String icon="";
				if (!item.IconName.equals("")) {
					switch (item.IconName.substring(0, 3).toLowerCase()) {
					case "mdi":
						if (item.ItemText.equals("")) {
							icon="<i id=\"" + item.ID + "icon\" class=\"" + item.IconName + ExtraStyleClass + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.TopBarIconSize + ";display: block; width: 2rem\">&nbsp;</i>";							
						} else {
							icon="<i id=\"" + item.ID + "icon\" class=\"" + item.IconName + ExtraStyleClass + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.TopBarIconSize + ";display: block; width: 2rem\"></i>";
						}
						break;
					case "fa ":
					case "fa-":
						if (item.ItemText.equals("")) {
							icon="<i id=\"" + item.ID + "icon\" class=\"" + item.IconAwesomeExtra + " " + item.IconName + ExtraStyleClass + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.TopBarIconSize + ";display: block; width: 2rem" + forceFloat + "\"></i>";
						} else {
							icon="<i id=\"" + item.ID + "icon\" class=\"" + item.IconAwesomeExtra + " " + item.IconName + ExtraStyleClass + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.TopBarIconSize + ";display: block; width: 2rem" + forceFloat + "\"></i>";
						}
						break;
					case "abm":
						if (item.ItemText.equals("")) {
							icon="<i id=\"" + item.ID + "icon\" class=\"" + ExtraStyleClass + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.TopBarIconSize + ";display: block; width: 2rem\">" + Page.svgIconmap.getOrDefault(item.IconName.toLowerCase(), "") + "</i>";
						} else {
							icon="<i id=\"" + item.ID + "icon\" class=\"" + ExtraStyleClass + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.TopBarIconSize + ";display: block; width: 2rem\">" + Page.svgIconmap.getOrDefault(item.IconName.toLowerCase(), "") + "</i>";
						}
						break;
					default:								
						if (item.ItemText.equals("")) {
							icon="<i id=\"" + item.ID + "icon\" class=\"material-icons" + ExtraStyleClass + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.TopBarIconSize + ";display: block; width: 2rem\">" + ABMaterial.HTMLConv().htmlEscape(item.IconName, Page.PageCharset) + "</i>";
						} else {
							icon="<i id=\"" + item.ID + "icon\" class=\"material-icons" + ExtraStyleClass + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.TopBarIconSize + ";display: block; width: 2rem\">" + ABMaterial.HTMLConv().htmlEscape(item.IconName, Page.PageCharset) + "</i>";
						}
					}
				}
				
				if (!TopSubItems.containsKey(item.ItemReturnName.toLowerCase())) {
					String FullScreenToggle = "";
					if (item.ItemReturnName.equalsIgnoreCase("togglefullscreen")) {
						FullScreenToggle = " toggle-fullscreen " ;
					}
					String tmpUrl = item.Url;
					
					String fntStyle="";
					if (!nb.TopBarFontSize.equals("")) {
						fntStyle=" style=\"font-size: " + nb.TopBarFontSize + "\" ";
					}
					if (item.SideBar!=null) {
						if (item.ItemReturnName.equalsIgnoreCase(ActiveTopReturnName)) {
							s.append("<li " + active + toolTip + " id=\"" + item.ID + "\"><" + nb.TopBarTextSize + " onclick=\"extrabar('" + item.SideBar.ID.toLowerCase() + "')\" id=\"btnextrasidebar" + item.SideBar.ID.toLowerCase() + "\" class=\"abmicbdg " + ABMaterial.GetColorStr(nb.TopBarActiveForeColor, nb.TopBarActiveForeColorIntensity, "text") + " " + ABMaterial.GetWavesEffect(nb.TopBarWavesEffect, nb.TopBarWavesCircle) + " " + "\" " + fntStyle + " data-activates=\"" + item.SideBar.ID.toLowerCase() + "\">" + icon + BuildText(item.ItemText) + "</" + nb.TopBarTextSize + "></li>\n" );
						} else {
							s.append("<li " + active + toolTip + " id=\"" + item.ID + "\"><" + nb.TopBarTextSize + " onclick=\"extrabar('" + item.SideBar.ID.toLowerCase() + "')\" id=\"btnextrasidebar" + item.SideBar.ID.toLowerCase() + "\" class=\"abmicbdg " + ABMaterial.GetColorStr(nb.TopBarForeColor, nb.TopBarForeColorIntensity, "text") + " " + ABMaterial.GetWavesEffect(nb.TopBarWavesEffect, nb.TopBarWavesCircle) + " " + "\" " + fntStyle + " data-activates=\"" + item.SideBar.ID.toLowerCase() + "\">" + icon + BuildText(item.ItemText) + "</" + nb.TopBarTextSize + "></li>\n" );
						}
					} else {
						if (item.ItemReturnName.equalsIgnoreCase(ActiveTopReturnName)) {
							s.append("<li " + active + toolTip + " id=\"" + item.ID + "\" onclick=\"navclick('" + Page.Name.toLowerCase() + "','" + item.ItemReturnName + "','" + tmpUrl + "')\"><" + nb.TopBarTextSize + " class=\"abmicbdg " + FullScreenToggle + ABMaterial.GetColorStr(nb.TopBarActiveForeColor, nb.TopBarActiveForeColorIntensity, "text") + " " + ABMaterial.GetWavesEffect(nb.TopBarWavesEffect, nb.TopBarWavesCircle) + " " + "\" " + fntStyle + ">" + icon + BuildText(item.ItemText) + "</" + nb.TopBarTextSize + "></li>\n" );
						} else {
							s.append("<li " + active + toolTip + " id=\"" + item.ID + "\" onclick=\"navclick('" + Page.Name.toLowerCase() + "','" + item.ItemReturnName + "','" + tmpUrl + "')\"><" + nb.TopBarTextSize + " class=\"abmicbdg " + FullScreenToggle + ABMaterial.GetColorStr(nb.TopBarForeColor, nb.TopBarForeColorIntensity, "text") + " " + ABMaterial.GetWavesEffect(nb.TopBarWavesEffect, nb.TopBarWavesCircle) + " " + "\" " + fntStyle + ">" + icon + BuildText(item.ItemText) + "</" + nb.TopBarTextSize + "></li>\n" );
						}
					}
				} else { // dropdown
					String DataBelow="";
					if (TopBarDropDownBelow) {
						DataBelow=" data-beloworigin=\"true\" ";
					}
					String DataConstrain="";
					if (!TopBarDropdownConstrainWidth) {
						DataConstrain=" data-constrainwidth=\"false\" ";
					}
					String fntStyle="";
					if (!nb.SideBarFontSize.equals("")) {
						fntStyle=" style=\"font-size: " + nb.SideBarFontSize + "\" ";
					}
					if (!item.ItemText.equals("")) {
						if (!TopBarDropdownHideArrow) {
							s.append("<li " + active + toolTip + " id=\"" + item.ID + "\"><" + nb.SideBarTextSize + " class=\"abmicbdg dropdown-button "+ ABMaterial.GetColorStr(nb.TopBarForeColor, nb.TopBarForeColorIntensity, "text") + " " + ABMaterial.GetWavesEffect(nb.TopBarWavesEffect, nb.TopBarWavesCircle) + " " + "\" data-activates=\"" + item.ItemReturnName + "dropdown1\" " + DataBelow + DataConstrain + fntStyle + ">" + icon + BuildText(item.ItemText) + "<span class=\"caret\">&nbsp;&#9660;</span></" + nb.SideBarTextSize + "></li>\n" );
						} else {
							s.append("<li " + active + toolTip + " id=\"" + item.ID + "\"><" + nb.SideBarTextSize + " class=\"abmicbdg dropdown-button "+ ABMaterial.GetColorStr(nb.TopBarForeColor, nb.TopBarForeColorIntensity, "text") + " " + ABMaterial.GetWavesEffect(nb.TopBarWavesEffect, nb.TopBarWavesCircle) + " " + "\" data-activates=\"" + item.ItemReturnName + "dropdown1\" " + DataBelow + DataConstrain + fntStyle + ">" + icon + BuildText(item.ItemText) + "</" + nb.SideBarTextSize + "></li>\n" );
						}
					} else {
						if (!TopBarDropdownHideArrow) {
							s.append("<li " + active + toolTip + " id=\"" + item.ID + "\"><" + nb.SideBarTextSize + " class=\"abmicbdg dropdown-button "+ ABMaterial.GetColorStr(nb.TopBarForeColor, nb.TopBarForeColorIntensity, "text") + " " + ABMaterial.GetWavesEffect(nb.TopBarWavesEffect, nb.TopBarWavesCircle) + " " + "\" data-activates=\"" + item.ItemReturnName + "dropdown1\" " + DataBelow + DataConstrain + fntStyle + ">" + icon + "<span class=\"caret\">&#9660;</span></" + nb.SideBarTextSize + "></li>\n" );
						}  else {
							s.append("<li " + active + toolTip + " id=\"" + item.ID + "\"><" + nb.SideBarTextSize + " class=\"abmicbdg dropdown-button "+ ABMaterial.GetColorStr(nb.TopBarForeColor, nb.TopBarForeColorIntensity, "text") + " " + ABMaterial.GetWavesEffect(nb.TopBarWavesEffect, nb.TopBarWavesCircle) + " " + "\" data-activates=\"" + item.ItemReturnName + "dropdown1\" " + DataBelow + DataConstrain + fntStyle + ">" + icon + "</" + nb.SideBarTextSize + "></li>\n" );
						}
						
					}
				}				
			}
			if (WithExtraSearch) {
				s.append("<li id=\"" + ExtraSearchID + "li\" class=\"search-li " + ExtraSearchVisibility + "\">\n");
				s.append("<div class=\"search-widget\">\n");
				s.append("<button class=\"search-button\" onclick=\"navSearch()\"><i class=\"material-icons "+ ABMaterial.GetColorStr(nb.TopBarForeColor, nb.TopBarForeColorIntensity, "text") + "\">search</i></button>\n");
				s.append("<button class=\"search-close-button\" onclick=\"navSearchClose()\"><i class=\"material-icons "+ ABMaterial.GetColorStr(nb.TopBarForeColor, nb.TopBarForeColorIntensity, "text") + "\">close</i></button>\n");
				s.append("<input class=\"search-inputfield\" type=\"text\" id=\"" + ExtraSearchID + "\" autocomplete=\"off\">\n");         
				s.append("</div>\n");
				s.append("</li>\n");
			}
			s.append("</ul>\n");
		}
		s.append("</div>\n");
		String extraContent="";
		String extraStyle="";
		if (!ExtraContentOpen) {
			extraStyle=" style=\"display: none;\"";
		}
		
		if (WithExtraContent) {
			extraContent = " nav-extended ";
		}
		if (WithExtraHalfButton) {
			extraContent = " nav-extended ";
		}
		if (!extraContent.equals("")) {			
			s.append("<div id=\"abmextracontent\" class=\"nav-content\"" + extraStyle + ">\n");
						
			if (WithExtraContent) {
				s.append(mExtraContent.Build());
			}
			
			s.append("</div>\n");
			if (WithExtraHalfButton && mExtraHalfFloatingButton.IsInitialized) {
				s.append(mExtraHalfFloatingButton.Build());
			}
		}
		s.append("</div>\n");
		s.append("</nav>\n");
		
		
		return s.toString();
	}
	
	public void Freeze() {
		if (Page!=null) {
			if (Page.ws!=null) {
				Page.ws.Eval("document.cancelClose=true;", null);
				try {
					if (Page.ws.getOpen()) {
						if (Page.ShowDebugFlush) {BA.Log("Freeze" + ID);}
						Page.ws.Flush();Page.RunFlushed();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void UnFreeze() {
		if (Page!=null) {
			if (Page.ws!=null) {
				Page.ws.Eval("document.cancelClose=false;", null);
				try {
					if (Page.ws.getOpen()) {
						if (Page.ShowDebugFlush) {BA.Log("Unfreeze" + ID);}
						Page.ws.Flush();Page.RunFlushed();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void Open() {
		if (Page!=null) {
			if (Page.ws!=null) {
				Page.ws.Eval("$('.sidenav').sideNav('show');", null);
				try {
					if (Page.ws.getOpen()) {
						if (Page.ShowDebugFlush) {BA.Log("Open" + ID);}
						Page.ws.Flush();Page.RunFlushed();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void Close() {
		if (Page!=null) {
			if (Page.ws!=null) {
				Page.ws.Eval("$('.sidenav').sideNav('hide');", null);
				try {
					if (Page.ws.getOpen()) {
						if (Page.ShowDebugFlush) {BA.Log("Close" + ID);}
						Page.ws.Flush();Page.RunFlushed();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected String BuildSideBar() {
		
		StringBuilder s = new StringBuilder();	
		ThemeNavigationBar nb = Theme;
		if (!SideItems.isEmpty()) {
			if (mSideBarTopComponent!=null) {
				s.append("<li class=\"logo " + ABMaterial.GetColorStr(nb.LogoBackColor,nb.LogoBackColorIntensity, "") + " " + nb.LogoZDepth + "\" style=\"padding: 0\">\n<a id=\"logo-container\" class=\"brand-logo\">\n");
				s.append(mSideBarTopComponent.Build());
				s.append("</a>\n</li>");
			}
			
			s.append("<li class=\"no-padding\">\n");
			s.append("<ul class=\"collapsible " + ABMaterial.GetColorStr(nb.SideBarBackColor, nb.SideBarBackColorIntensity, "") + "\" data-collapsible=\"" + SideBarCollapseType + "\">\n");
			for (ABMNavBarItem item: SideItems) {					
				String active="";
				String activeColor="";
				String activedisplay="";
				String bold="";
				if (nb.SideBarBold) {
					bold=" class=\"bold\" ";
				}
				if (item.ItemReturnName.equalsIgnoreCase(ActiveSideReturnName)) {
					active = " active active2 ";
					activeColor = " color: " + ABMaterial.GetColorStrMap(nb.SideBarActiveForeColor,nb.SideBarActiveForeColorIntensity) + " !important;"; 
				}
				String icon="";
				
				if (SideSubItems.containsKey(item.ItemReturnName.toLowerCase())) {
					
					String ltr="";
					if (this.getRightToLeft()) {
						ltr = ";float: right !important;margin-right: 0px !important;margin-left: 1rem !important;";
					}
					if (!item.IconName.equals("")) {
						switch (item.IconName.substring(0, 3).toLowerCase()) {
						case "mdi":
							if (item.ItemText.equals("")) {
								icon="<i class=\"" + item.IconName + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ltr + ";\"></i>";
							} else {
								icon="<i class=\"" + item.IconName + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ltr + ";\"></i>";
							}
							break;
						case "fa ":
						case "fa-":
							if (item.ItemText.equals("")) {
								icon="<i class=\"" + item.IconAwesomeExtra + " " + item.IconName + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ltr + ";\"></i>";
							} else {
								icon="<i class=\"" + item.IconAwesomeExtra + " " + item.IconName + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ltr + ";\"></i>";
							}
							break;
						case "abm":
							if (item.ItemText.equals("")) {
								icon="<i class=\"" + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ltr + ";\">" + Page.svgIconmap.getOrDefault(item.IconName.toLowerCase(), "") + "</i>";
							} else {
								icon="<i class=\"" + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ltr + ";\">" + Page.svgIconmap.getOrDefault(item.IconName.toLowerCase(), "") + "</i>";
							}
							break;
						default:								
							if (item.ItemText.equals("")) {
								icon="<i class=\"material-icons " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ltr + ";\">" + ABMaterial.HTMLConv().htmlEscape(item.IconName, Page.PageCharset) + "</i>";
							} else {
								icon="<i class=\"material-icons " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ltr + ";\">" + ABMaterial.HTMLConv().htmlEscape(item.IconName, Page.PageCharset) + "</i>";
							}
						}								
					}
					
					String fntStyle="";
					if (item.Component==null) {
						if (!nb.SideBarFontSize.equals("")) {
							fntStyle=" font-size: " + nb.SideBarFontSize + ";";
						}
						if (SideBarSubItemsArrowUnicodeChar.equals("")) {
							s.append("<li " + bold + " style=\"line-height: " + HeightPx + "px;\"><" + nb.SideBarTextSize + " class=\"collapsible-header " + active + ABMaterial.GetColorStr(nb.SideBarForeColor, nb.SideBarForeColorIntensity, "text") + " " + ABMaterial.GetWavesEffect(nb.SideBarWavesEffect, nb.SideBarWavesCircle) + "\" style=\"line-height: " + HeightPx + "px; height: " + HeightPx + "px;" + fntStyle + "\">" + icon + BuildText(item.ItemText) + "</" + nb.SideBarTextSize + ">\n" );
						} else {
							if (SideBarSubItemsArrowAlignRight) {
								if (this.getRightToLeft()) {
									s.append("<li " + bold + " style=\"line-height: " + HeightPx + "px;\"><" + nb.SideBarTextSize + " class=\"collapsible-header " + active + ABMaterial.GetColorStr(nb.SideBarForeColor, nb.SideBarForeColorIntensity, "text") + " " + ABMaterial.GetWavesEffect(nb.SideBarWavesEffect, nb.SideBarWavesCircle) + "\" style=\"line-height: " + HeightPx + "px; height: " + HeightPx + "px;" + fntStyle + "\">" + icon + BuildText(item.ItemText) + "<span class=\"caret\" style=\"float: left\">" + SideBarSubItemsArrowUnicodeChar + ";&nbsp;</span></" + nb.SideBarTextSize + ">\n" );
								} else {
									s.append("<li " + bold + " style=\"line-height: " + HeightPx + "px;\"><" + nb.SideBarTextSize + " class=\"collapsible-header " + active + ABMaterial.GetColorStr(nb.SideBarForeColor, nb.SideBarForeColorIntensity, "text") + " " + ABMaterial.GetWavesEffect(nb.SideBarWavesEffect, nb.SideBarWavesCircle) + "\" style=\"line-height: " + HeightPx + "px; height: " + HeightPx + "px;" + fntStyle + "\">" + icon + BuildText(item.ItemText) + "<span class=\"caret\" style=\"float: right\">&nbsp;" + SideBarSubItemsArrowUnicodeChar + ";</span></" + nb.SideBarTextSize + ">\n" );
								}
							} else {
								if (this.getRightToLeft()) {
									s.append("<li " + bold + " style=\"line-height: " + HeightPx + "px;\"><" + nb.SideBarTextSize + " class=\"collapsible-header " + active + ABMaterial.GetColorStr(nb.SideBarForeColor, nb.SideBarForeColorIntensity, "text") + " " + ABMaterial.GetWavesEffect(nb.SideBarWavesEffect, nb.SideBarWavesCircle) + "\" style=\"line-height: " + HeightPx + "px; height: " + HeightPx + "px;" + fntStyle + "\">" + icon + BuildText(item.ItemText) + "<span class=\"caret\">" + SideBarSubItemsArrowUnicodeChar + ";&nbsp;</span></" + nb.SideBarTextSize + ">\n" );
								} else {
									s.append("<li " + bold + " style=\"line-height: " + HeightPx + "px;\"><" + nb.SideBarTextSize + " class=\"collapsible-header " + active + ABMaterial.GetColorStr(nb.SideBarForeColor, nb.SideBarForeColorIntensity, "text") + " " + ABMaterial.GetWavesEffect(nb.SideBarWavesEffect, nb.SideBarWavesCircle) + "\" style=\"line-height: " + HeightPx + "px; height: " + HeightPx + "px;" + fntStyle + "\">" + icon + BuildText(item.ItemText) + "<span class=\"caret\">&nbsp;" + SideBarSubItemsArrowUnicodeChar + ";</span></" + nb.SideBarTextSize + ">\n" );
								}
							}
						}	
					} else {
						if (item.Center) {
							if (item.MinHeight.equals("")) {
								s.append("<li><" + nb.SideBarTextSize + " class=\"collapsible-header " + active + ABMaterial.GetColorStr(nb.SideBarForeColor, nb.SideBarForeColorIntensity, "text") + " " + ABMaterial.GetWavesEffect(nb.SideBarWavesEffect, nb.SideBarWavesCircle) + "\"><div class=\"valign-wrapper\">" + item.Component.Build() + "</div></" + nb.SideBarTextSize + ">\n" );
							} else {
								s.append("<li><" + nb.SideBarTextSize + " class=\"collapsible-header " + active + ABMaterial.GetColorStr(nb.SideBarForeColor, nb.SideBarForeColorIntensity, "text") + " " + ABMaterial.GetWavesEffect(nb.SideBarWavesEffect, nb.SideBarWavesCircle) + "\"><div class=\"valign-wrapper\" style=\"min-height:" + item.MinHeight + "\">" + item.Component.Build() + "</div></" + nb.SideBarTextSize + ">\n" );
							}
						} else {
							if (item.MinHeight.equals("")) {
								s.append("<li><" + nb.SideBarTextSize + " class=\"collapsible-header " + active + ABMaterial.GetColorStr(nb.SideBarForeColor, nb.SideBarForeColorIntensity, "text") + " " + ABMaterial.GetWavesEffect(nb.SideBarWavesEffect, nb.SideBarWavesCircle) + "\">" + item.Component.Build() + "</" + nb.SideBarTextSize + ">\n" );
							} else {
								s.append("<li><" + nb.SideBarTextSize + " class=\"collapsible-header " + active + ABMaterial.GetColorStr(nb.SideBarForeColor, nb.SideBarForeColorIntensity, "text") + " " + ABMaterial.GetWavesEffect(nb.SideBarWavesEffect, nb.SideBarWavesCircle) + "\"><div style=\"min-height:" + item.MinHeight + "\">" + item.Component.Build() + "</div></" + nb.SideBarTextSize + ">\n" );		
							}
						}								
					}
					s.append("<div class=\"collapsible-body\"" + activedisplay + ">\n<ul class=\"" + ABMaterial.GetColorStr(nb.SideBarSubBackColor, nb.SideBarSubBackColorIntensity, "") + "\">\n");
					for (ABMNavBarItem subitem: SideSubItems.get(item.ItemReturnName.toLowerCase())) {
						String activesub="";
						String activesubColor="";
						if (subitem.ItemReturnName.equalsIgnoreCase(ActiveSideSubReturnName)) {
							if (nb.SideBarSubBold) {
								activesub=" class=\"bold active active2\" ";
							} else {
								activesub=" class=\"active active2\" ";
							}
							activesubColor = " color: " + ABMaterial.GetColorStrMap(nb.SideBarSubActiveForeColor,nb.SideBarSubActiveForeColorIntensity) + " !important;";
						} else {
							if (nb.SideBarSubBold) {
								activesub=" class=\"bold\" ";
							} else {
								activesub="";
							}
						}
						String iconsub="";
						if (!subitem.IconName.equals("")) {
							switch (subitem.IconName.substring(0, 3).toLowerCase()) {
							case "mdi":
								if (subitem.ItemText.equals("")) {
									iconsub="<i class=\"" + subitem.IconName + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ltr + ";" + subitem.iconExtraStyle + "\"></i>";
								} else {
									iconsub="<i class=\"" + subitem.IconName + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ltr + ";" + subitem.iconExtraStyle + "\"></i>";
								}
								break;
							case "fa ":
							case "fa-":
								if (subitem.ItemText.equals("")) {
									iconsub="<i class=\"" + subitem.IconAwesomeExtra + " " + subitem.IconName + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ltr + ";" + subitem.iconExtraStyle + "\"></i>";
								} else {
									iconsub="<i class=\"" + subitem.IconAwesomeExtra + " " + subitem.IconName + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ltr + ";" + subitem.iconExtraStyle + "\"></i>";
								}
								break;
							case "abm":
								if (subitem.ItemText.equals("")) {
									iconsub="<i class=\"\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ltr + ";" + subitem.iconExtraStyle + "\">" + Page.svgIconmap.getOrDefault(subitem.IconName.toLowerCase(), "") + "</i>";
								} else {
									iconsub="<i class=\"" + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ltr + ";" + subitem.iconExtraStyle + "\">" + Page.svgIconmap.getOrDefault(subitem.IconName.toLowerCase(), "") + "</i>";
								}
								break;
							default:								
								if (subitem.ItemText.equals("")) {
									iconsub="<i class=\"material-icons\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ltr + ";" + subitem.iconExtraStyle + "\">" + ABMaterial.HTMLConv().htmlEscape(subitem.IconName, Page.PageCharset) + "</i>";
								} else {
									iconsub="<i class=\"material-icons " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ltr + ";" + subitem.iconExtraStyle + "\">" + ABMaterial.HTMLConv().htmlEscape(subitem.IconName, Page.PageCharset) + "</i>";
								}
							}
						}
						if (subitem.IsDivider) {									
							s.append("<li class=\"divider " + ABMaterial.GetColorStr(nb.SideBarSubDividerColor, nb.SideBarSubDividerColorIntensity, "") + "\"></li>\n");
						} else {
							String FullScreenToggle = "";
							if (subitem.ItemReturnName.equalsIgnoreCase("togglefullscreen")) {
								FullScreenToggle = " toggle-fullscreen " ;
							}
							String tmpUrl = subitem.Url;
							
							if (subitem.Component==null) {
								if (!nb.SideBarSubFontSize.equals("")) {
									fntStyle=" font-size: " + nb.SideBarSubFontSize + "; ";
								}
								s.append("<li " + activesub + " data-nosub onclick=\"navclick('" + Page.Name.toLowerCase() + "','" + subitem.ItemReturnName + "','" + tmpUrl + "')\" style=\"line-height: " + HeightPx + "px;\"><" + nb.SideBarSubTextSize + " class=\"" + FullScreenToggle + ABMaterial.GetColorStr(nb.SideBarSubForeColor, nb.SideBarSubForeColorIntensity, "text") + " " + ABMaterial.GetWavesEffect(nb.SideBarSubWavesEffect, nb.SideBarSubWavesCircle) + "\" style=\"line-height: " + HeightPx + "px; height: " + HeightPx + "px;" + fntStyle + activesubColor + "\">" + iconsub + BuildText(subitem.ItemText) + "</" + nb.SideBarSubTextSize + "></li>\n" );
							} else {
								if (subitem.Center) {
									if (subitem.MinHeight.equals("")) {
										s.append("<li data-nosub onclick=\"navclick('" + Page.Name.toLowerCase() + "','" + subitem.ItemReturnName + "','" + tmpUrl + "')\" ><" + nb.SideBarSubTextSize + " class=\"" + ABMaterial.GetWavesEffect(nb.SideBarSubWavesEffect, nb.SideBarSubWavesCircle) + "\" ><div class=\"valign-wrapper\">" + subitem.Component.Build() + "</div></" + nb.SideBarSubTextSize + "></li>\n" );
									} else {
										s.append("<li data-nosub onclick=\"navclick('" + Page.Name.toLowerCase() + "','" + subitem.ItemReturnName + "','" + tmpUrl + "')\" ><" + nb.SideBarSubTextSize + " class=\"" + ABMaterial.GetWavesEffect(nb.SideBarSubWavesEffect, nb.SideBarSubWavesCircle) + "\" ><div class=\"valign-wrapper\" style=\"min-height:" + subitem.MinHeight + "\">" + subitem.Component.Build() + "</div></" + nb.SideBarSubTextSize + "></li>\n" );
									}
								} else {
									if (subitem.MinHeight.equals("")) {
										s.append("<li data-nosub onclick=\"navclick('" + Page.Name.toLowerCase() + "','" + subitem.ItemReturnName + "','" + tmpUrl + "')\" ><" + nb.SideBarSubTextSize + " class=\"" + ABMaterial.GetWavesEffect(nb.SideBarSubWavesEffect, nb.SideBarSubWavesCircle) + "\" >" + subitem.Component.Build() + "</" + nb.SideBarSubTextSize + "></li>\n" );												
									} else {
										s.append("<li data-nosub onclick=\"navclick('" + Page.Name.toLowerCase() + "','" + subitem.ItemReturnName + "','" + tmpUrl + "')\" ><" + nb.SideBarSubTextSize + " class=\"" + ABMaterial.GetWavesEffect(nb.SideBarSubWavesEffect, nb.SideBarSubWavesCircle) + "\" ><div style=\"min-height:" + subitem.MinHeight + "\">" + subitem.Component.Build() + "</div></" + nb.SideBarSubTextSize + "></li>\n" );
									}
								}										
							}
						}
					}
					s.append("</ul>\n</div>\n</li>\n");								
													
				} else {							
					String ltr="";
					if (this.getRightToLeft()) {
						ltr = ";float: right !important;margin-right: 0px !important;margin-left: 1rem !important;";
					}
					if (!item.IconName.equals("")) {
						switch (item.IconName.substring(0, 3).toLowerCase()) {
						case "mdi":
							if (item.ItemText.equals("")) {
								icon="<i class=\"" + item.IconName + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ltr + "\"></i>";
							} else {
								icon="<i class=\"" + item.IconName + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ltr + "\"></i>";
							}
							break;
						case "fa ":
						case "fa-":
							if (item.ItemText.equals("")) {
								icon="<i class=\"" + item.IconAwesomeExtra + " " + item.IconName + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ltr + "\"></i>";
							} else {
								icon="<i class=\"" + item.IconAwesomeExtra + " " + item.IconName + " " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ltr + "\"></i>";
							}
							break;
						case "abm":
							if (item.ItemText.equals("")) {
								icon="<i class=\"\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ltr + "\">" + Page.svgIconmap.getOrDefault(item.IconName.toLowerCase(), "") + "</i>";
							} else {
								icon="<i class=\"" + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ltr + "\">" + Page.svgIconmap.getOrDefault(item.IconName.toLowerCase(), "") + "</i>";
							}
							break;
						default:								
							if (item.ItemText.equals("")) {
								icon="<i class=\"material-icons\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ltr + "\">" + ABMaterial.HTMLConv().htmlEscape(item.IconName, Page.PageCharset) + "</i>";
							} else {
								icon="<i class=\"material-icons " + item.IconAlign + "\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem" + ltr + "\">" + ABMaterial.HTMLConv().htmlEscape(item.IconName, Page.PageCharset) + "</i>";
							}
						}								
					}
					
					if (item.IsDivider) {								
						s.append("<li class=\"divider " + ABMaterial.GetColorStr(nb.SideBarDividerColor, nb.SideBarDividerColorIntensity, "") + "\"></li>\n");
					} else {								
						String tmpUrl = item.Url;
					
						String fntStyle="";
						if (item.Component==null) {
							if (!nb.SideBarFontSize.equals("")) {
								fntStyle=" font-size: " + nb.SideBarFontSize + "; ";
							}
							
							s.append("<li " + bold + " data-nosub onclick=\"navclick('" + Page.Name.toLowerCase() + "','" + item.ItemReturnName + "','" + tmpUrl + "')\" style=\"line-height: " + HeightPx + "px;\"><" + nb.SideBarTextSize + " class=\"collapsible-header " + active + ABMaterial.GetColorStr(nb.SideBarForeColor, nb.SideBarForeColorIntensity, "text") + " " + ABMaterial.GetWavesEffect(nb.SideBarWavesEffect, nb.SideBarWavesCircle) + "\" style=\"line-height: " + HeightPx + "px; height: " + HeightPx + "px;" + fntStyle + activeColor + "\">" + icon + BuildText(item.ItemText) + "</" + nb.SideBarTextSize + "></li>\n" );
						} else {
							if (item.Center) {
								if (item.MinHeight.equals("")) {
									s.append("<li data-nosub onclick=\"navclick('" + Page.Name.toLowerCase() + "','" + item.ItemReturnName + "','" + tmpUrl + "')\" ><" + nb.SideBarTextSize + " class=\"collapsible-header " + active + ABMaterial.GetWavesEffect(nb.SideBarWavesEffect, nb.SideBarWavesCircle) + "\" ><div class=\"valign-wrapper\">" + item.Component.Build() + "</div>" + item.Component.Build() + "</div></" + nb.SideBarTextSize + "></li>\n" );
								} else {
									s.append("<li data-nosub onclick=\"navclick('" + Page.Name.toLowerCase() + "','" + item.ItemReturnName + "','" + tmpUrl + "')\" ><" + nb.SideBarTextSize + " class=\"collapsible-header " + active + ABMaterial.GetWavesEffect(nb.SideBarWavesEffect, nb.SideBarWavesCircle) + "\" ><div class=\"valign-wrapper\" style=\"min-height:" + item.MinHeight + "\">" + item.Component.Build() + "</div></" + nb.SideBarTextSize + "></li>\n" );
								}
							} else {
								if (item.MinHeight.equals("")) {
									s.append("<li data-nosub onclick=\"navclick('" + Page.Name.toLowerCase() + "','" + item.ItemReturnName + "','" + tmpUrl + "')\" ><" + nb.SideBarTextSize + " class=\"collapsible-header " + active + ABMaterial.GetWavesEffect(nb.SideBarWavesEffect, nb.SideBarWavesCircle) + "\" >" + item.Component.Build() + "</" + nb.SideBarTextSize + "></li>\n" );
								} else {
									s.append("<li data-nosub onclick=\"navclick('" + Page.Name.toLowerCase() + "','" + item.ItemReturnName + "','" + tmpUrl + "')\" ><" + nb.SideBarTextSize + " class=\"collapsible-header " + active + ABMaterial.GetWavesEffect(nb.SideBarWavesEffect, nb.SideBarWavesCircle) + "\" ><div style=\"min-height:" + item.MinHeight + "\">" + item.Component.Build() + "</div></" + nb.SideBarTextSize + "></li>\n" );
								}
							}
						}								
					}
				}
			}		
			
			s.append("<a class=\"" + ABMaterial.GetColorStr(nb.SideBarBackgroundColor, nb.SideBarBackgroundColorIntensity, "") + "\" style=\"line-height: 60px;\"></a></ul></li>\n");					
		} else {
			if (mSideBarTopComponent!=null) {
				s.append("<li class=\"logo " + ABMaterial.GetColorStr(nb.LogoBackColor,nb.LogoBackColorIntensity, "") + " " + nb.LogoZDepth + "\" style=\"padding: 0\">\n<a id=\"logo-container\" class=\"brand-logo\">\n");
				s.append(mSideBarTopComponent.Build());
				s.append("</a>\n</li>");
			}
		}
		return s.toString();		
		
	}
	
	
	public void setIsVisible(boolean isVisible) {
		if (isVisible) {
			mHidden="";
		} else {
			mHidden=" hide ";
		}
	}
	
	public boolean getIsVisible() {
		return (mHidden.equals(""));
	}
	
	
	protected String BuildText(String text) {
		StringBuilder s = new StringBuilder();	
		
		String v = ABMaterial.HTMLConv().htmlEscape(text, Page.PageCharset);
		v=v.replaceAll("(\r\n|\n\r|\r|\n)", "<br>");
		v=v.replace("{B}", "<b>");
		v=v.replace("{/B}", "</b>");
		v=v.replace("{I}", "<i>");
		v=v.replace("{/I}", "</i>");
		v=v.replace("{U}", "<ins>");
		v=v.replace("{/U}", "</ins>");
		v=v.replace("{SUB}", "<sub>");
		v=v.replace("{/SUB}", "</sub>");
		v=v.replace("{SUP}", "<sup>");
		v=v.replace("{/SUP}", "</sup>");
		v=v.replace("{BR}", "<br>");
		v=v.replace("{WBR}", "<wbr>");
		v=v.replace("{NBSP}", "&nbsp;");
		v=v.replace("{AL}", "<a rel=\"nofollow\" target=\"_blank\" href=\"");
		v=v.replace("{AT}", "\">");
		v=v.replace("{/AL}", "</a>");
		v=v.replace("{AS}", " title=\"");
		v=v.replace("{/AS}", "\"");
		
		while (v.indexOf("{C:") > -1) {
			int vvi = v.indexOf("{C:");
			v=v.replaceFirst("\\{C:", "<span style=\"color:");
			v=v.substring(0,vvi) + v.substring(vvi).replaceFirst("\\}", "\">");
			v=v.replaceFirst("\\{/C}", "</span>");	
		}

		v = v.replace("{CODE}", "<div class=\"abmcode\"><pre><code>");
		v = v.replace("{/CODE}", "</code></pre></div>");
		while (v.indexOf("{ST:") > -1) {
			int vvi = v.indexOf("{ST:");
			v=v.replaceFirst("\\{ST:", "<span style=\"");			
			v=v.substring(0,vvi) + v.substring(vvi).replaceFirst("\\}", "\">");
			v=v.replaceFirst("\\{/ST}", "</span>");	
		}	
		
		int start = v.indexOf("{IC:");
		while (start > -1) {
			int stop = v.indexOf("{/IC}");
			String vv = "";
			if (stop>0) {
				vv = v.substring(start, stop+5);
			} else {
				break;
			}
			String IconColor = vv.substring(4, 11);
			String IconName = vv.substring(12,vv.length()-5);
			String repl="";
			switch (IconName.substring(0, 3).toLowerCase()) {
			case "mdi":
				repl = "<i style=\"color: " + IconColor + "\" class=\"" + IconName + "\"></i>";
				break;
			case "fa ":
			case "fa-":
				repl = "<i style=\"color: " + IconColor + "\" class=\"" + IconName + "\"></i>";
				break;
			case "abm":
				repl = "<i>" + Page.svgIconmap.getOrDefault(IconName.toLowerCase(), "") + "</i>"; 
				break;
			default:
				repl = "<i style=\"color: " + IconColor + "\" class=\"material-icons\">" + IconName + "</i>";
			}
			v=v.replace(vv,repl );
			start = v.indexOf("{IC:");
		}
		
		s.append(v);
		
		return s.toString();
	}

}
