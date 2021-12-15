package com.ab.abmaterial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;
import anywheresoftware.b4j.object.WebSocket.SimpleFuture;

@ShortName("ABMSideBar")
@Events(values={"Closed()"})
public class ABMSideBar extends ABMObject {
	private static final long serialVersionUID = -1511212907378089970L;

	protected String ID="";
	protected transient ABMPage Page;
	protected ThemeNavigationBar Theme=new ThemeNavigationBar();
	protected boolean IsBuild=false;
	protected String Visibility=ABMaterial.VISIBILITY_ALL;
		
	protected List<ABMNavBarItem> SideItems = new ArrayList<ABMNavBarItem>();
	private Map<String,List<ABMNavBarItem>> SideSubItems = new HashMap<String,List<ABMNavBarItem>>();
	protected int WidthPx=240;
	protected int WidthSPx=240;
	protected int WidthMPx=240;
	
	protected int HeightPx=56;
	protected int TopPx=56;
	protected ABMComponent SideBarTopComponent=null;
	protected String SideBarCollapseType="accordion";
	
	public int SideBarLogoHeight=49;
	public String ActiveSideReturnName="";
	public String ActiveSideSubReturnName="";
	
	protected boolean HasFadeEffect=false;
	protected double FadeEffectValue=0.75;
	
	public boolean TopBarDropDownConstrainWidth=true;
	public boolean IsInitialized=false;
	protected String TopBarItemReturnName="";
	protected boolean IsPanel=false;
	
	public ABMContainer Content=new ABMContainer();
	
	public boolean IsTextSelectable=true;
	
	protected boolean ShowDebugInfo=false;
	
	private int mRightToLeft=0;
	
	public void Initialize(ABMPage page, String id, int sideBarWidthPx, int sideBarItemHeightPx, int sideBarStartTopPx, ABMComponent sideBarTopComponent, String sideBarCollapseType, String themeName) {
		this.Type = ABMaterial.UITYPE_NAVBAR;
		this.Page = page;
		this.ID = id;
		UseTheme(themeName);
		this.WidthPx = sideBarWidthPx;
		this.SideBarTopComponent=sideBarTopComponent;
		this.TopPx = sideBarStartTopPx;
		this.HeightPx=sideBarItemHeightPx;
		this.SideBarCollapseType = sideBarCollapseType;	
		IsInitialized=true;
	}
	
	public void InitializeAsPanel(ABMPage page, String id, int sideBarWidthPx, int sideBarStartTopPx, ABMComponent sideBarTopComponent, String themeName, String panelId, String panelThemeName) {
		this.Type = ABMaterial.UITYPE_NAVBAR;
		this.Page = page;
		this.ID = id;
		UseTheme(themeName);
		this.SideBarTopComponent=sideBarTopComponent;
		this.Content.Initialize(page, panelId, panelThemeName);
		this.TopPx = sideBarStartTopPx;
		this.IsPanel = true;
		IsInitialized=true;
	}
	
	public void InitializeResponsive(ABMPage page, String id, int sideBarWidthSmallPx, int sideBarWidthMediumPx, int sideBarWidthLargePx, int sideBarItemHeightPx, int sideBarStartTopPx, ABMComponent sideBarTopComponent, String sideBarCollapseType, String themeName) {
		this.Type = ABMaterial.UITYPE_NAVBAR;
		this.Page = page;
		this.ID = id;
		UseTheme(themeName);
		this.WidthPx = sideBarWidthLargePx;
		this.WidthMPx = sideBarWidthMediumPx;
		this.WidthSPx = sideBarWidthSmallPx;
		this.SideBarTopComponent=sideBarTopComponent;
		this.TopPx = sideBarStartTopPx;
		this.HeightPx=sideBarItemHeightPx;
		this.SideBarCollapseType = sideBarCollapseType;	
		IsInitialized=true;
	}
	
	public void InitializeResponsiveAsPanel(ABMPage page, String id, int sideBarWidthSmallPx, int sideBarWidthMediumPx, int sideBarWidthLargePx, int sideBarStartTopPx, ABMComponent sideBarTopComponent, String themeName, String panelId, String panelThemeName) {
		this.Type = ABMaterial.UITYPE_NAVBAR;
		this.Page = page;
		this.ID = id;
		UseTheme(themeName);
		
		this.WidthPx = sideBarWidthLargePx;
		this.WidthMPx = sideBarWidthMediumPx;
		this.WidthSPx = sideBarWidthSmallPx;
		
		this.SideBarTopComponent=sideBarTopComponent;
		this.Content.Initialize(page, panelId, panelThemeName);
		this.TopPx = sideBarStartTopPx;
		this.IsPanel = true;		
		IsInitialized=true;
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
	
	protected void ResetTheme() {
		UseTheme(Theme.ThemeName);
	}
		
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.NavBars.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.NavBars.get(themeName.toLowerCase()).Clone();				
			}
		}
	}
	
	public boolean IsOpen() {
		boolean open=false;
		if (Page.ws!=null) {
			String s = "return $('#" + ID.toLowerCase() + "').css('right');";
			SimpleFuture ret = Page.ws.EvalWithResult(s, null);
			try {				
				String v = (String) ret.getValue();
				if (!v.startsWith("-")) {
					open=true;
				}
			} catch (InterruptedException e) {					
				e.printStackTrace();
			} catch (TimeoutException e) {					
				e.printStackTrace();
			} catch (ExecutionException e) {					
				e.printStackTrace();
			} catch (IOException e) {					
				e.printStackTrace();
			}
		}
		return open;
	}
	
	public void Clear() {
		SideItems = new ArrayList<ABMNavBarItem>();
		SideSubItems = new HashMap<String,List<ABMNavBarItem>>();
		if (ShowDebugInfo) {
			BA.Log("[SB]Clear: Page=" + Page.Name + " ID=" + ID + " IsBuild=" + IsBuild);
		}
	}
	
	public void AddSideBarItem(String sideItemReturnName, String itemText, String iconName) { 
		if (IsPanel) {
			BA.Log("Is created as panel.  Use .Content!");
			return;
		}
		SideItems.add(new ABMNavBarItem(sideItemReturnName, itemText, iconName, "", false));		
	}
	
	public void AddSideBarComponent(String sideItemReturnName, ABMComponent component) {
		if (IsPanel) {
			BA.Log("Is created as panel.  Use .Content!");
			return;
		}
		SideItems.add(new ABMNavBarItem(sideItemReturnName, component, "", false));		
		
	}
	
	public void AddSideBarDivider(String themeName) {
		if (IsPanel) {
			BA.Log("Is created as panel.  Use .Content!");
			return;
		}
		SideItems.add(new ABMNavBarItem(themeName));		
	}
	
	public void AddSideBarSubItem(String sideItemReturnName, String sideSubItemReturnName, String itemText, String iconName) {
		if (IsPanel) {
			BA.Log("Is created as panel.  Use .Content!");
			return;
		}
		if (!SideSubItems.containsKey(sideItemReturnName.toLowerCase())) {
			SideSubItems.put(sideItemReturnName.toLowerCase(), new ArrayList<ABMNavBarItem>());
		}
		SideSubItems.get(sideItemReturnName.toLowerCase()).add(new ABMNavBarItem(sideSubItemReturnName, itemText, iconName, "", false));		
	}	
	
	public void AddSideBarSubComponent(String sideItemReturnName, String sideSubItemReturnName, ABMComponent component) {
		if (IsPanel) {
			BA.Log("Is created as panel.  Use .Content!");
			return;
		}
		if (!SideSubItems.containsKey(sideItemReturnName.toLowerCase())) {
			SideSubItems.put(sideItemReturnName.toLowerCase(), new ArrayList<ABMNavBarItem>());
		}
		SideSubItems.get(sideItemReturnName.toLowerCase()).add(new ABMNavBarItem(sideSubItemReturnName, component, "",  false));		
	}	
	
	public void AddSideBarSubDivider(String sideItemReturnName,String themeName) {
		if (IsPanel) {
			BA.Log("Is created as panel.  Use .Content!");
			return;
		}
		if (!SideSubItems.containsKey(sideItemReturnName.toLowerCase())) {
			SideSubItems.put(sideItemReturnName.toLowerCase(), new ArrayList<ABMNavBarItem>());
		}
		SideSubItems.get(sideItemReturnName.toLowerCase()).add(new ABMNavBarItem(themeName));		
	}
	
	public String getID() {
		return this.ID;
	}	
	
	@Hide
	protected void Prepare() {
		if (ShowDebugInfo) {
			BA.Log("[SB]Prepare: Page=" + Page.Name + " ID=" + ID + " IsBuild=" + IsBuild);
		}
		this.IsBuild = true;
		if (IsPanel) {
			Content.Prepare();
		} else {
			for (ABMNavBarItem item: SideItems) {
				if (item.Component!=null) {
					item.Component.Prepare();
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
		public boolean HideOnMidAndDown=false;
		public ABMComponent Component=null;
		
		ABMNavBarItem() {
			
		}
		
		ABMNavBarItem(String itemReturnName, String itemText, String iconName, String url, boolean hideOnMidAndDown) {
			this.ItemReturnName = itemReturnName;
			this.ItemText = itemText;
			this.IconName = iconName;
			this.Url = url;
			this.HideOnMidAndDown = hideOnMidAndDown;
		}
		
		ABMNavBarItem(String itemReturnName, ABMComponent comp, String url, boolean hideOnMidAndDown) {
			this.ItemReturnName = itemReturnName;
			this.Component = comp;
			this.Url = url;
			this.HideOnMidAndDown = hideOnMidAndDown;
		}
		
		ABMNavBarItem(String themeName) {
			this.IsDivider = true;
			this.InternalThemeName=themeName;
		}
		
		public ABMNavBarItem Clone() {
			ABMNavBarItem c = new ABMNavBarItem();
			c.ItemText = ItemText;
			c.IconName = IconName;
			c.ItemReturnName = ItemReturnName;
			c.IsDivider = IsDivider;
			c.InternalThemeName = InternalThemeName;
			c.Url=Url;
			c.HideOnMidAndDown=HideOnMidAndDown;
			c.Component = Component;
			c.IconAwesomeExtra=IconAwesomeExtra;
			return c;
		}
	}
	
	@Hide		
	protected String Build() {
		if (ShowDebugInfo) {
			BA.Log("[SB]Build: Page=" + Page.Name + " ID=" + ID + " IsBuild=" + IsBuild);
		}
		if (Theme.ThemeName.equals("default")) {
			Theme.Colorize(Page.CompleteTheme.MainColor);
		}
		
		StringBuilder s = new StringBuilder();	
		ThemeNavigationBar nb = Theme;
		String selectable="";
		if (!IsTextSelectable) {
			selectable = " notselectable ";
		}
		String rtl="";
		if (this.getRightToLeft()) {
			rtl = " abmrtl ";
		}
		if (IsPanel) {
			s.append("<div id=\"" + this.ID.toLowerCase() + "\" evname=\"" + ID.toLowerCase() + "\" class=\"no-print side-nav " + ABMaterial.GetColorStr(nb.SideBarBackColor, nb.SideBarBackColorIntensity, "") + selectable + rtl + " extrasidebar right-aligned\" style=\"top: " + TopPx + "px;height:-o-calc(100% - " + TopPx + "px);height:-webkit-calc(100% - " + TopPx + "px);height:-moz-calc(100% - " + TopPx + "px);height:calc(100% - " + TopPx + "px);overflow-x: hidden;padding-bottom:0px;\">\n");
			s.append(BuildSideBarPanel());
			s.append("</div>\n");			
		} else {
			s.append("<ul id=\"" + this.ID.toLowerCase() + "\" evname=\"" + ID.toLowerCase() + "\" class=\"no-print side-nav " + ABMaterial.GetColorStr(nb.SideBarBackColor, nb.SideBarBackColorIntensity, "") + selectable + rtl + " extrasidebar right-aligned\" style=\"top: " + TopPx + "px;height:-o-calc(100% - " + TopPx + "px);height:-webkit-calc(100% - " + TopPx + "px);height:-moz-calc(100% - " + TopPx + "px);height:calc(100% - " + TopPx + "px);overflow-x: hidden;padding-bottom:0px;\">\n");
			s.append(BuildSideBar());
			s.append("</ul>\n");			
		}
		IsBuild=true;
		return s.toString();
	}	
		
	public void Refresh() {	
		RefreshInternal(true);
	}	
	
	/**
	 * This is a limited version of Refresh() which will only refresh the sidebar properties + each row properties + each cell properties, NOT its contents.		 
	 */
	public void RefreshNoContents(boolean DoFlush) {
		if (ShowDebugInfo) {
			BA.Log("[SB]RefreshNoContents: Page=" + Page.Name + " ID=" + ID + " IsBuild=" + IsBuild);
		}
		ThemeNavigationBar nb = Theme;
		JQueryElement j = Page.ws.GetElementById(this.ID.toLowerCase());
		String selectable="";
		if (!IsTextSelectable) {
			selectable = " notselectable ";
		}
		String rtl="";
		if (this.getRightToLeft()) {
			rtl = " abmrtl ";
		}
		j.SetProp("class", "side-nav " + ABMaterial.GetColorStr(nb.SideBarBackColor, nb.SideBarBackColorIntensity, "") + selectable + rtl + " extrasidebar right-aligned no-print ");
		if (IsPanel) {			
			Content.RefreshNoContents(false);
		} else {
			
		}
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Sidebar RefreshNoContents: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}		
	}
	
	protected void RefreshInternal(boolean DoFlush) {
		if (ShowDebugInfo) {
			BA.Log("[SB]RefreshInternal: Page=" + Page.Name + " ID=" + ID + " IsBuild=" + IsBuild);
		}
		ThemeNavigationBar nb = Theme;
		JQueryElement j = Page.ws.GetElementById(this.ID.toLowerCase());
		String selectable="";
		if (!IsTextSelectable) {
			selectable = " notselectable ";
		}
		String rtl="";
		if (this.getRightToLeft()) {
			rtl = " abmrtl ";
		}
		j.SetProp("class", "side-nav " + ABMaterial.GetColorStr(nb.SideBarBackColor, nb.SideBarBackColorIntensity, "") + selectable + rtl + " extrasidebar right-aligned no-print ");
		if (IsPanel) {
			j.SetHtml(BuildSideBarPanel());
			Content.RunAllFirstRuns();
		} else {
			j.SetHtml(BuildSideBar());			
		}
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Sidebar Refresh: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}		
	}
	
	protected void FirstRun() {
		if (ShowDebugInfo) {
			BA.Log("[SB]FirstRun: Page=" + Page.Name + " ID=" + ID + " IsBuild=" + IsBuild);
		}
		if (SideBarTopComponent!=null) {
			SideBarTopComponent.FirstRun();			
		}
		if (IsPanel) {
			Content.RunAllFirstRunsInternal(false);
		} else {
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
	}
	
	protected String BuildSideBar() {
		
		StringBuilder s = new StringBuilder();	
		ThemeNavigationBar nb = Theme;
		if (!SideItems.isEmpty()) {
			if (SideBarTopComponent!=null) {
				s.append("<li class=\"logo " + ABMaterial.GetColorStr(nb.TopBarBackColor,nb.TopBarBackColorIntensity, "") + " z-depth-1\">\n<a id=\"logo-container\" class=\"brand-logo\">\n");
				s.append(SideBarTopComponent.Build());
				s.append("</a>\n</li>");
			}
			
			s.append("<li class=\"no-padding\">\n");
			s.append("<ul class=\"collapsible " + ABMaterial.GetColorStr(nb.SideBarBackColor, nb.SideBarBackColorIntensity, "") + "\" data-collapsible=\"" + SideBarCollapseType + "\">\n");
			for (ABMNavBarItem item: SideItems) {					
				String active="";
				String activedisplay="";
				String bold="";
				if (nb.SideBarBold) {
					bold=" class=\"bold\" ";
				}
				if (item.ItemReturnName.equalsIgnoreCase(ActiveSideReturnName)) {
					active = " active ";	
				}
				String icon="";
				
				if (SideSubItems.containsKey(item.ItemReturnName.toLowerCase())) {
					
					if (!item.IconName.equals("")) {
						switch (item.IconName.substring(0, 3).toLowerCase()) {
						case "mdi":
							if (item.ItemText.equals("")) {
								icon="<i class=\"" + item.IconName + " left\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem\"></i>";
							} else {
								icon="<i class=\"" + item.IconName + " left\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem\"></i>";
							}
							break;
						case "fa ":
						case "fa-":
							if (item.ItemText.equals("")) {
								icon="<i class=\"" + item.IconAwesomeExtra + " " + item.IconName + " left\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem\"></i>";
							} else {
								icon="<i class=\"" + item.IconAwesomeExtra + " " + item.IconName + " left\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem\"></i>";
							}
							break;
						case "abm":
							if (item.ItemText.equals("")) {
								icon="<i class=\"left\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem\">" + Page.svgIconmap.getOrDefault(item.IconName.toLowerCase(), "") + "</i>";
							} else {
								icon="<i class=\"left\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem\">" + Page.svgIconmap.getOrDefault(item.IconName.toLowerCase(), "") + "</i>";
							}
							break;
						default:								
							if (item.ItemText.equals("")) {
								icon="<i class=\"material-icons\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem\">" + ABMaterial.HTMLConv().htmlEscape(item.IconName, Page.PageCharset) + "</i>";
							} else {
								icon="<i class=\"material-icons left\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem\">" + ABMaterial.HTMLConv().htmlEscape(item.IconName, Page.PageCharset) + "</i>";
							}
						}
					}
					
					String fntStyle="";
					if (item.Component==null) {
						if (!nb.SideBarFontSize.equals("")) {
							fntStyle=" font-size: " + nb.SideBarFontSize + ";";
						}
						s.append("<li " + bold + " style=\"line-height: " + HeightPx + "px;\"><" + nb.SideBarTextSize + " class=\"collapsible-header " + active + ABMaterial.GetColorStr(nb.SideBarForeColor, nb.SideBarForeColorIntensity, "text") + " " + ABMaterial.GetWavesEffect(nb.SideBarWavesEffect, nb.SideBarWavesCircle) + "\" style=\"line-height: " + HeightPx + "px; height: " + HeightPx + "px;" + fntStyle + "\">" + icon + ABMaterial.HTMLConv().htmlEscape(item.ItemText, Page.PageCharset) + "<span class=\"caret\">&nbsp;&#9660;</span></" + nb.SideBarTextSize + ">\n" );
					} else {
						s.append("<li><" + nb.SideBarTextSize + " class=\"collapsible-header " + active + ABMaterial.GetColorStr(nb.SideBarForeColor, nb.SideBarForeColorIntensity, "text") + " " + ABMaterial.GetWavesEffect(nb.SideBarWavesEffect, nb.SideBarWavesCircle) + "\">" + item.Component.Build() + "</" + nb.SideBarTextSize + ">\n" );
					}
					s.append("<div class=\"collapsible-body\"" + activedisplay + ">\n<ul class=\"" + ABMaterial.GetColorStr(nb.SideBarSubBackColor, nb.SideBarSubBackColorIntensity, "") + "\">\n");
					for (ABMNavBarItem subitem: SideSubItems.get(item.ItemReturnName.toLowerCase())) {
						String activesub="";
						if (subitem.ItemReturnName.equalsIgnoreCase(ActiveSideSubReturnName)) {
							if (nb.SideBarSubBold) {
								activesub=" class=\"bold active\" ";
							} else {
								activesub=" class=\"active\" ";
							}
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
									iconsub="<i class=\"" + subitem.IconName + " left\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem\"></i>";
								} else {
									iconsub="<i class=\"" + subitem.IconName + " left\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem\"></i>";
								}
								break;
							case "fa ":
							case "fa-":
								if (subitem.ItemText.equals("")) {
									iconsub="<i class=\"" + subitem.IconAwesomeExtra + " " + subitem.IconName + " left\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem\"></i>";
								} else {
									iconsub="<i class=\"" + subitem.IconAwesomeExtra + " " + subitem.IconName + " left\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem\"></i>";
								}
								break;										
							case "abm":
								if (subitem.ItemText.equals("")) {
									iconsub="<i class=\"left\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem\">" + Page.svgIconmap.getOrDefault(subitem.IconName.toLowerCase(), "") + "</i>";
								} else {
									iconsub="<i class=\"left\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem\">" + Page.svgIconmap.getOrDefault(subitem.IconName.toLowerCase(), "") + "</i>";
								}
								break;
							default:								
								if (subitem.ItemText.equals("")) {
									iconsub="<i class=\"material-icons\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem\">" + ABMaterial.HTMLConv().htmlEscape(subitem.IconName, Page.PageCharset) + "</i>";
								} else {
									iconsub="<i class=\"material-icons left\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem\">" + ABMaterial.HTMLConv().htmlEscape(subitem.IconName, Page.PageCharset) + "</i>";
								}
							}
						}
						if (subitem.IsDivider) {
							ThemeDivider d = Page.CompleteTheme.Dividers.getOrDefault(subitem.InternalThemeName, null);
							if (d==null) {
								d = Page.CompleteTheme.CreateThemeDivider();
							}
							s.append("<li class=\"divider " + ABMaterial.GetColorStr(d.ForeColor, d.ForeColorIntensity, "text") + "\"></li>\n");
						} else {
							String FullScreenToggle = "";
							if (subitem.ItemReturnName.equalsIgnoreCase("togglefullscreen")) {
								FullScreenToggle = " toggle-fullscreen " ;
							}
							if (subitem.Component==null) {
								if (!nb.SideBarSubFontSize.equals("")) {
									fntStyle=" font-size: " + nb.SideBarSubFontSize + "; ";
								}										
								s.append("<li " + activesub + " style=\"line-height: " + HeightPx + "px;\"><" + nb.SideBarSubTextSize + " class=\"" + FullScreenToggle + ABMaterial.GetColorStr(nb.SideBarSubForeColor, nb.SideBarSubForeColorIntensity, "text") + " " + ABMaterial.GetWavesEffect(nb.SideBarSubWavesEffect, nb.SideBarSubWavesCircle) + "\" style=\"line-height: " + HeightPx + "px; height: " + HeightPx + "px;" + fntStyle + "\">" + iconsub + ABMaterial.HTMLConv().htmlEscape(subitem.ItemText, Page.PageCharset) + "</" + nb.SideBarSubTextSize + "></li>\n" );
							} else {
								s.append("<li><" + nb.SideBarSubTextSize + " class=\"" + ABMaterial.GetWavesEffect(nb.SideBarSubWavesEffect, nb.SideBarSubWavesCircle) + "\" >" + subitem.Component.Build() + "</" + nb.SideBarSubTextSize + "></li>\n" );
							}
						}
					}
					s.append("</ul>\n</div>\n</li>\n");								
													
				} else {							
					if (!item.IconName.equals("")) {
						switch (item.IconName.substring(0, 3).toLowerCase()) {
						case "mdi":
							if (item.ItemText.equals("")) {
								icon="<i class=\"" + item.IconName + " left\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem\"></i>";
							} else {
								icon="<i class=\"" + item.IconName + " left\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem\"></i>";
							}
							break;
						case "fa ":
						case "fa-":
							if (item.ItemText.equals("")) {
								icon="<i class=\"" + item.IconAwesomeExtra + " " + item.IconName + " left\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem\"></i>";
							} else {
								icon="<i class=\"" + item.IconAwesomeExtra + " " + item.IconName + " left\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem\"></i>";
							}
							break;									
						case "abm":
							if (item.ItemText.equals("")) {
								icon="<i class=\"left\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem\">" + Page.svgIconmap.getOrDefault(item.IconName.toLowerCase(), "") + "</i>";
							} else {
								icon="<i class=\"left\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem\">" + Page.svgIconmap.getOrDefault(item.IconName.toLowerCase(), "") + "</i>";
							}
							break;
						default:								
							if (item.ItemText.equals("")) {
								icon="<i class=\"material-icons\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem\">" + ABMaterial.HTMLConv().htmlEscape(item.IconName, Page.PageCharset) + "</i>";
							} else {
								icon="<i class=\"material-icons left\" style=\"line-height: inherit; text-align: center; font-size: " + nb.SideBarIconSize + "; display: block; width: 2rem\">" + ABMaterial.HTMLConv().htmlEscape(item.IconName, Page.PageCharset) + "</i>";
							}
						}
					}
					
					if (item.IsDivider) {
						ThemeDivider d = Page.CompleteTheme.Dividers.getOrDefault(item.InternalThemeName, null);
						if (d==null) {
							d = Page.CompleteTheme.CreateThemeDivider();
						}
						s.append("<li class=\"divider " + ABMaterial.GetColorStr(d.ForeColor, d.ForeColorIntensity, "text") + "\"></li>\n");
					} else {								
						String fntStyle="";
						if (item.Component==null) {
							if (!nb.SideBarFontSize.equals("")) {
								fntStyle=" font-size: " + nb.SideBarFontSize + "; ";
							}
							
							s.append("<li " + bold + " style=\"line-height: " + HeightPx + "px;\"><" + nb.SideBarTextSize + " class=\"collapsible-header " + active + ABMaterial.GetColorStr(nb.SideBarForeColor, nb.SideBarForeColorIntensity, "text") + " " + ABMaterial.GetWavesEffect(nb.SideBarWavesEffect, nb.SideBarWavesCircle) + "\" style=\"line-height: " + HeightPx + "px; height: " + HeightPx + "px;" + fntStyle + "\">" + icon + ABMaterial.HTMLConv().htmlEscape(item.ItemText, Page.PageCharset) + "</" + nb.SideBarTextSize + "></li>\n" );
						} else {
							s.append("<li><" + nb.SideBarTextSize + " class=\"collapsible-header " + active + ABMaterial.GetWavesEffect(nb.SideBarWavesEffect, nb.SideBarWavesCircle) + "\" >" + item.Component.Build() + "</" + nb.SideBarTextSize + "></li>\n" );
						}
						
					}
				}
			}		
			
			s.append("<a style=\"line-height: 60px;\"></a></ul></li>\n");

		}
		return s.toString();		
		
	}
	
	protected String BuildSideBarPanel() {
		StringBuilder s = new StringBuilder();	
		ThemeNavigationBar nb = Theme;
		
		if (SideBarTopComponent!=null) {
			s.append("<li class=\"logo " + ABMaterial.GetColorStr(nb.TopBarBackColor,nb.TopBarBackColorIntensity, "") + " z-depth-1\">\n<a id=\"logo-container\" class=\"brand-logo\">\n");
			s.append(SideBarTopComponent.Build());
			s.append("</a>\n</li>");
		}
		s.append(Content.Build());
		return s.toString();
	}

}
