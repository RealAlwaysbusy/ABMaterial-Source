package com.ab.abmaterial;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;

@Author("Alain Bailleul")  
@ShortName("ABMTabs")
@Events(values={"Clicked(TabReturnName as String)"})
public class ABMTabs extends ABMComponent {	
	private static final long serialVersionUID = 8058271097167776028L;
	protected ThemeTabs Theme=new ThemeTabs();
	protected Map<String, ABMTab> Tabs = new LinkedHashMap<String, ABMTab>();
	protected int FixedHeightPx=Integer.MIN_VALUE;
	protected int MarginTopPx=0;
	protected int IconTopPx=0;
	
	public String ForceStyle="";
		
	public void Initialize(ABMPage page, String id, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_TABS;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Tabs.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Tabs.get(themeName.toLowerCase()).Clone();				
			}
		}
		IsInitialized=true;
		IsVisibilityDirty=true;
	}	
	
	public void Initialize2(ABMPage page, String id, int fixedHeightPx, int marginTopPx, int iconTopPx, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_TABS;
		this.FixedHeightPx = fixedHeightPx;
		this.MarginTopPx = marginTopPx;
		this.IconTopPx = iconTopPx;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Tabs.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Tabs.get(themeName.toLowerCase()).Clone();				
			}
		}
		IsInitialized=true;
		IsVisibilityDirty=false;
	}	
	
	@Override
	protected void ResetTheme() {
		UseTheme(Theme.ThemeName);
		for (Map.Entry<String, ABMTab> entry : Tabs.entrySet()) {
			entry.getValue().TabPage.ResetTheme();
		}
	}

	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase();
	}
	
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}
	
	public void AddTab(String tabReturnName, String tabText, ABMContainer tabPage, int tabSizeSmall, int tabSizeMedium, int tabSizeLarge, int tabPageSizeSmall, int tabPageSizeMedium, int tabPageSizeLarge , boolean enabled, boolean active, String iconName, String iconAwesomeExtra) {
		ABMTab tab = new ABMTab();
		tab.ReturnName = tabReturnName;
		tab.IconAwesomeExtra = iconAwesomeExtra;
		tab.TabText= tabText;
		tab.TabPage = tabPage;
		tab.TabSizeSmall = tabSizeSmall;
		tab.TabSizeMedium = tabSizeMedium;
		tab.TabSizeLarge = tabSizeLarge;
		tab.TabPageSizeSmall = tabPageSizeSmall;
		tab.TabPageSizeMedium = tabPageSizeMedium;
		tab.TabPageSizeLarge = tabPageSizeLarge;
		tab.Active = active;
		tab.Enabled = enabled;
		tab.IconName = iconName;
		Tabs.put(tabReturnName.toLowerCase(), tab);		
	}
	
	public ABMContainer GetTabPage(String tabReturnName) {
		ABMTab tab = Tabs.getOrDefault(tabReturnName.toLowerCase(), null);
		if (tab!=null) {
			return tab.TabPage;
		}
		return null;
	}
	
	public void SetEnabled(String tabReturnName, boolean enabled) {
		ABMTab tab = Tabs.getOrDefault(tabReturnName.toLowerCase(), null);
		if (tab!=null) {
			tab.Enabled = enabled;
		}
	}
	
	public void SetActive(String tmpReturnName) {
		boolean IsEnabled=false;
		int Counter=0;
		for (Map.Entry<String, ABMTab> entry : Tabs.entrySet()) {
			if (entry.getValue().ReturnName.equalsIgnoreCase(tmpReturnName)) {
				entry.getValue().Active=true;
				IsEnabled = entry.getValue().Enabled;
				Counter=entry.getValue().Counter;
			} else {
				entry.getValue().Active=false;
			}
		}
		if (!IsEnabled) {
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + Counter, "disabled");
		} else {
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + Counter, "disabled");
		}
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		Params.Add(tmpReturnName.toLowerCase());
		Page.ws.RunFunction("setactivetab", Params);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("Tabs SetActive: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
		
	public String GetActive() {
		String ret = ABMaterial.GetActiveTab(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() );
		if (!ret.equals("")) {
			ret=ret.substring(1);
		}
		String ret2="";
		for (Map.Entry<String, ABMTab> entry : Tabs.entrySet()) {
			if (entry.getValue().hRef.equalsIgnoreCase(ret)) {
				entry.getValue().Active=true;
				ret2 = entry.getValue().ReturnName;
			} else {
				entry.getValue().Active=false;
			}
		}
		return ret2;
	}
	
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Tabs.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Tabs.get(themeName.toLowerCase()).Clone();				
			}
		}
	}
	
	@Override
	protected void CleanUp() {
		super.CleanUp();
		for (Map.Entry<String, ABMTab> entry : Tabs.entrySet()) {
			if (entry.getValue().TabPage!=null) {
				entry.getValue().TabPage.CleanUp();
			}
		}
		Tabs.clear();		
	}
	
	@Override
	protected void RemoveMe() {
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
	}
	
	@Override
	protected void FirstRun() {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());			
		
		Page.ws.RunFunction("reinittabs", Params);
		for (Map.Entry<String, ABMTab> entry : Tabs.entrySet()) {
			ABMTab tab = entry.getValue();
			if (entry.getValue().TabPage!=null) {
				tab.TabPage.Refresh();
			}
        }
		
		super.FirstRun();
	}
	
	@Override
	public void Prepare() {
		IsBuild=true;
		for (Map.Entry<String, ABMTab> entry : Tabs.entrySet()) {
			ABMTab tab = entry.getValue();
			tab.hRef = ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + tab.ReturnName.toLowerCase();
			if (tab.TabPage!=null) {
				tab.TabPage.Prepare();
			}
        }
	}
	
	@Override
	protected void RunAllFirstRuns() {
		
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());			
		
		Page.ws.RunFunction("reinittabs", Params);
		for (Map.Entry<String, ABMTab> entry : Tabs.entrySet()) {
			ABMTab tab = entry.getValue();
			if (tab.TabPage!=null) {
				tab.TabPage.RefreshInternalExtra(false, false, false);
			}
        }
		for (Map.Entry<String, ABMTab> entry : Tabs.entrySet()) {
			ABMTab tab = entry.getValue();
			tab.hRef = ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + tab.ReturnName.toLowerCase();
			if (tab.TabPage!=null) {
				tab.TabPage.RunAllFirstRunsInternal(false);
			}
        }
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
	
	/**
	 * This is a limited version of Refresh() which will only refresh the tabs properties + it tab pages, NOT its contents.		 
	 */
	public void RefreshNoContents(boolean DoFlush) {
		super.RefreshInternal(DoFlush);
		
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		j.SetProp("class", BuildClass());
		
		getVisibility();
		GetActive();
		
		String B4JSData1="";
		if (!mB4JSUniqueKey.equals("")) {
			B4JSData1 = mB4JSUniqueKey ;
		} else {
			B4JSData1 = "";
		}
		String B4JSData2="";
		
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-b4js", B4JSData1);
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-b4jsextra", B4JSData2);
		
		int Counter=0;
		for (Map.Entry<String, ABMTab> entry : Tabs.entrySet()) {
			ABMTab tab = entry.getValue();	
			tab.Counter=Counter;
			if (tab.Active) {
				ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + Counter, "active");
			} else {
				ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + Counter, "active");
			}
			if (!tab.Enabled) {
				ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + Counter, "disabled");
			} else {
				ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + Counter, "disabled");
			}
			if (tab.IconName.equals("")) {
				j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-text" + Counter);
				j.SetVal(BuildBody(tab.TabText));
			} else {
				
				j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon" + Counter);
				switch (tab.IconName.substring(0, 3).toLowerCase()) {
				case "mdi":
					j.SetProp("class", tab.IconName + " " + ABMaterial.ICONSIZE_SMALL);
					break;
				case "fa ":
				case "fa-":
					j.SetProp("class", tab.IconAwesomeExtra + " " + tab.IconName + " " + ABMaterial.ICONSIZE_SMALL);
					break;
				case "abm":
					j.SetHtml(Page.svgIconmap.getOrDefault(tab.IconName.toLowerCase(), ""));
					break;
				default:
					j.SetProp("class", "material-icons " + tab.IconName + " " + ABMaterial.ICONSIZE_SMALL);
					j.SetText(ABMaterial.HTMLConv().htmlEscape(tab.IconName, Page.PageCharset));					
				}
				
				
				j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-text" + Counter);
				j.SetVal(BuildBody(tab.TabText));
			}
			Counter++;
		}
		
		for (Map.Entry<String, ABMTab> entry : Tabs.entrySet()) {
			ABMTab tab = entry.getValue();
			if (tab.TabPage!=null) {
				tab.TabPage.mVisibility = mVisibility;
				tab.hRef = ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + tab.ReturnName.toLowerCase();
				
				tab.TabPage.RefreshNoContents(false);
			}
		}
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Tabs RefreshNoContents : " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
	
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		super.RefreshInternal(DoFlush);
				
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		j.SetProp("class", BuildClass());
		
		getVisibility();
		GetActive();
		
		String B4JSData1="";
		if (!mB4JSUniqueKey.equals("")) {
			B4JSData1 = mB4JSUniqueKey ;
		} else {
			B4JSData1 = "";
		}
		String B4JSData2="";
		
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-b4js", B4JSData1);
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-b4jsextra", B4JSData2);
		
		int Counter=0;
		for (Map.Entry<String, ABMTab> entry : Tabs.entrySet()) {
			ABMTab tab = entry.getValue();		
			tab.Counter=Counter;
			if (tab.Active) {
				ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + Counter, "active");
			} else {
				ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + Counter, "active");
			}
			if (!tab.Enabled) {
				ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + Counter, "disabled");
			} else {
				ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + Counter, "disabled");
			}
			if (tab.IconName.equals("")) {
				j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-text" + Counter);
				j.SetVal(BuildBody(tab.TabText));
			} else {
				
				j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon" + Counter);
				switch (tab.IconName.substring(0, 3).toLowerCase()) {
				case "mdi":
					j.SetProp("class", tab.IconName + " " + ABMaterial.ICONSIZE_SMALL);
					break;
				case "fa ":
				case "fa-":
					j.SetProp("class", tab.IconAwesomeExtra + " " + tab.IconName + " " + ABMaterial.ICONSIZE_SMALL);
					break;
				case "abm":
					j.SetHtml(Page.svgIconmap.getOrDefault(tab.IconName.toLowerCase(), ""));
					break;
				default:
					j.SetProp("class", "material-icons " + tab.IconName + " " + ABMaterial.ICONSIZE_SMALL);
					j.SetText(ABMaterial.HTMLConv().htmlEscape(tab.IconName, Page.PageCharset));					
				}
				
				
				j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-text" + Counter);
				j.SetVal(BuildBody(tab.TabText));
			}
			Counter++;
		}
		
		for (Map.Entry<String, ABMTab> entry : Tabs.entrySet()) {
			ABMTab tab = entry.getValue();
			if (tab.TabPage!=null) {
				tab.TabPage.mVisibility = mVisibility;
				tab.hRef = ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + tab.ReturnName.toLowerCase();			
				tab.TabPage.RefreshInternal(false);			
			}
		}
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Tabs Refresh : " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
	}
	
	@Override
	protected String Build() {
		if (Theme.ThemeName.equals("default")) {
			Theme.Colorize(Page.CompleteTheme.MainColor);
		}
		StringBuilder s = new StringBuilder();
		
		String B4JSData="";
		if (!mB4JSUniqueKey.equals("")) {
			B4JSData = " data-b4js=\"" + mB4JSUniqueKey + "\" data-b4jsextra=\"\" ";
		} else {
			B4JSData = " data-b4js=\"\" data-b4jsextra=\"\" ";
		}
		
		if (FixedHeightPx==Integer.MIN_VALUE) {
			if (ForceStyle.equals("")) {
				s.append("<ul id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" " + B4JSData + " class=\"" + BuildClass() + "\">\n");
			} else {
				s.append("<ul id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" " + B4JSData + " forcestyle=\"true\" class=\"" + BuildClass() + "\" style=\"" + ForceStyle + "\">\n");
			}
		} else {
			if (ForceStyle.equals("")) {
				s.append("<ul id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" " + B4JSData + " class=\"" + BuildClass() + "\" style=\"height: " + FixedHeightPx + "px\">\n");
			} else {
				s.append("<ul id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" " + B4JSData + " forcestyle=\"true\"  class=\"" + BuildClass() + "\" style=\"height: " + FixedHeightPx + "px;" + ForceStyle + "\">\n");
			}
		}
		s.append(BuildBody());
		IsBuild=true;
		return s.toString();
	}
	
	@Hide
	protected String BuildClass() {			
		StringBuilder s = new StringBuilder();
		s.append(super.BuildExtraClasses());
		ThemeTabs l=Theme;
		s.append("tabs " + "tabs" + l.ThemeName.toLowerCase() + " ");
		s.append(mVisibility + " ");
		s.append(l.ZDepth + " ");
		s.append(ABMaterial.GetColorStr(l.BackColor, l.BackColorIntensity, "")); // + " " + ABMaterial.GetColorStr(l.ForeColor, l.ForeColorIntensity, "text"));
		s.append(mIsPrintableClass);
		s.append(mIsOnlyForPrintClass);
		return s.toString(); 
	}
	
	@Hide
	protected String BuildBody() {
		StringBuilder s = new StringBuilder();
		
		int Counter=0;
		for (Map.Entry<String, ABMTab> entry : Tabs.entrySet()) {
			ABMTab tab = entry.getValue();
			tab.Counter=Counter;
			String active="";
			String disabled="";
			if (tab.Active) {
				active = " active ";
			}
			if (!tab.Enabled) {
				disabled = " disabled ";
			}
			if (FixedHeightPx==Integer.MIN_VALUE) {
				s.append("<li id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + Counter + "\" class=\"tab col s" + tab.TabSizeSmall + " m" + tab.TabSizeMedium + " l" + tab.TabSizeLarge + " " + disabled + "\">\n");
				if (tab.IconName.equals("")) {
					s.append("<a id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-text" + Counter + "\" onClick=\"tabsclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "','" + tab.ReturnName + "')\" href=\"#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + tab.ReturnName.toLowerCase() + "\" class=\"truncate " + active + "\">" + BuildBody(tab.TabText) + "</a>\n");
				} else {
					switch (tab.IconName.substring(0, 3).toLowerCase()) {
					case "mdi":
						s.append("<a id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-text" + Counter + "\" onClick=\"tabsclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "','" + tab.ReturnName + "')\" href=\"#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + tab.ReturnName.toLowerCase() + "\" class=\"truncate tabhide " + active + "\"><i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon" + Counter + "\" class=\"" + tab.IconName + " " + ABMaterial.ICONSIZE_SMALL + " \"></i>" + BuildBody(tab.TabText) + "</a>\n");
						break;
					case "fa ":
					case "fa-":
						s.append("<a id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-text" + Counter + "\" onClick=\"tabsclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "','" + tab.ReturnName + "')\" href=\"#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + tab.ReturnName.toLowerCase() + "\" class=\"truncate tabhide " + active + "\"><i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon" + Counter + "\" class=\"" + tab.IconAwesomeExtra + " " + tab.IconName + " " + ABMaterial.ICONSIZE_SMALL + " \"></i>" + BuildBody(tab.TabText) + "</a>\n");
						break;
					case "abm":
						s.append("<a id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-text" + Counter + "\" onClick=\"tabsclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "','" + tab.ReturnName + "')\" href=\"#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + tab.ReturnName.toLowerCase() + "\" class=\"truncate tabhide " + active + "\"><i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon" + Counter + "\" class=\"" + ABMaterial.ICONSIZE_SMALL + " \"></i>" + Page.svgIconmap.getOrDefault(tab.IconName.toLowerCase(), "") + BuildBody(tab.TabText) + "</a>\n");
						break;
					default:
						s.append("<a id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-text" + Counter + "\" onClick=\"tabsclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "','" + tab.ReturnName + "')\" href=\"#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + tab.ReturnName.toLowerCase() + "\" class=\"truncate tabhide " + active + "\"><i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon" + Counter + "\" class=\"material-icons " + ABMaterial.ICONSIZE_SMALL + " \">" + ABMaterial.HTMLConv().htmlEscape(tab.IconName, Page.PageCharset) + "</i>" + BuildBody(tab.TabText) + "</a>\n");
					}									
				}
			} else {
				s.append("<li id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + Counter + "\" class=\"tab col s" + tab.TabSizeSmall + " m" + tab.TabSizeMedium + " l" + tab.TabSizeLarge + " " + disabled + "\" style=\"height:" + FixedHeightPx + "px\">\n");
				if (tab.IconName.equals("")) {
					s.append("<a id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-text" + Counter + "\" onClick=\"tabsclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "','" + tab.ReturnName + "')\" href=\"#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + tab.ReturnName.toLowerCase() + "\" class=\"truncate " + active + "\" style=\"margin-top: " + MarginTopPx + "px\">" + BuildBody(tab.TabText) + "</a>\n");
				} else {
					switch (tab.IconName.substring(0, 3).toLowerCase()) {
					case "mdi":
						s.append("<a id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-text" + Counter + "\" onClick=\"tabsclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "','" + tab.ReturnName + "')\" href=\"#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + tab.ReturnName.toLowerCase() + "\" class=\"truncate tabhide " + active + "\" style=\"margin-top: " + MarginTopPx + "px\"><i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon" + Counter + "\" class=\"" + tab.IconName + " " + ABMaterial.ICONSIZE_SMALL + " \" style=\"position: relative;top: " + IconTopPx + "px\"></i>" + BuildBody(tab.TabText) + "</a>\n");
						break;
					case "fa ":
					case "fa-":
						s.append("<a id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-text" + Counter + "\" onClick=\"tabsclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "','" + tab.ReturnName + "')\" href=\"#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + tab.ReturnName.toLowerCase() + "\" class=\"truncate tabhide " + active + "\" style=\"margin-top: " + MarginTopPx + "px\"><i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon" + Counter + "\" class=\"" + tab.IconAwesomeExtra + " " + tab.IconName + " " + ABMaterial.ICONSIZE_SMALL + " \" style=\"position: relative;top: " + IconTopPx + "px\"></i>" + BuildBody(tab.TabText) + "</a>\n");
						break;
					case "abm":
						s.append("<a id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-text" + Counter + "\" onClick=\"tabsclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "','" + tab.ReturnName + "')\" href=\"#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + tab.ReturnName.toLowerCase() + "\" class=\"truncate tabhide " + active + "\" style=\"margin-top: " + MarginTopPx + "px\"><i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon" + Counter + "\" class=\"" + ABMaterial.ICONSIZE_SMALL + " \" style=\"position: relative;top: " + IconTopPx + "px\"></i>" + Page.svgIconmap.getOrDefault(tab.IconName.toLowerCase(), "") + BuildBody(tab.TabText) + "</a>\n");
						break;
					default:
						s.append("<a id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-text" + Counter + "\" onClick=\"tabsclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "','" + tab.ReturnName + "')\" href=\"#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + tab.ReturnName.toLowerCase() + "\" class=\"truncate tabhide " + active + "\" style=\"margin-top: " + MarginTopPx + "px\"><i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon" + Counter + "\" class=\"material-icons " + ABMaterial.ICONSIZE_SMALL + " \" style=\"position: relative;top: " + IconTopPx + "px\">" + ABMaterial.HTMLConv().htmlEscape(tab.IconName, Page.PageCharset) + "</i>" + BuildBody(tab.TabText) + "</a>\n");
					}									
				}
			}

			s.append("</li>\n");
			Counter++;
		}
		s.append("</ul>\n");
		for (Map.Entry<String, ABMTab> entry : Tabs.entrySet()) {
			ABMTab tab = entry.getValue();
			if (tab.TabPage!=null) {
				tab.hRef = ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + tab.ReturnName.toLowerCase();
				s.append("<div id=\"" + tab.hRef + "\" class=\"col s" + tab.TabPageSizeSmall + " m" + tab.TabPageSizeMedium + " l" + tab.TabPageSizeLarge + "\" style=\"padding: 0rem\">\n");
				s.append(tab.TabPage.Build());
				s.append("</div>\n");
			}
		}
		return s.toString();
	}
	
	protected String BuildBody(String text) {
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
	
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public void SetB4JSActive(String tmpReturnName) {
		
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public String GetB4JSActive() {
		return "B4JS Dummy";
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public void setB4JSVisibility(String visibility) {
		
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public String getB4JSVisibility() {
		return "B4JS Dummy";
	}
	
	/**
	 * Needs at least the param B4JS_PARAM_TABSRETURNNAME
	 * 
	 * every method you want to call with a B4JSOn... call MUST return a boolean
	 * returning true will consume the event in the browser and not call the B4J event (if any)
	 *
	 * e.g. myButton.B4JSOnClicked("MyJavascript", "AddToLabel", Array As Object(myCounter))
	 * if AddToLabel return true, then myButton_Clicked() will not be raised.
	 * if AddToLabel returns false, then myButton_Clicked() will be raised AFTER the B4JS method is done.
	 * 
	 * public Sub AddToLabel(MyCounter As Int) As Boolean
	 *      if myCounter mod 2 = 0 then	       
	 *		   Return True
	 *      else
	 *         Return False
	 *      end if
	 * End Sub
	 */
	public void B4JSOnClick(String B4JSClassName, String B4JSMethod, anywheresoftware.b4a.objects.collections.List Params) {
		PrepareEvent("B4JSOnClick", B4JSClassName, B4JSMethod, Params);				
	}
	
	@Override
	public String getVisibility() {
		if (GotLastVisibility) {
			return mVisibility;
		}
		GotLastVisibility=true;
		if (!mB4JSUniqueKey.equals("") && !IsVisibilityDirty) {
			if (JQ!=null) {
				anywheresoftware.b4a.objects.collections.List par = new anywheresoftware.b4a.objects.collections.List();
				par.Initialize();
				par.Add(ParentString + RootID());
				FutureVisibility = JQ.RunMethodWithResult("GetVisibility", par);				
			} else {
				FutureVisibility=null;				
			}
			if (!(FutureVisibility==null)) {
				try {
					//BA.Log(ID);
					this.mVisibility = ((String)FutureVisibility.getValue());					
				} catch (InterruptedException e) {					
					e.printStackTrace();
				} catch (TimeoutException e) {					
					e.printStackTrace();
				} catch (ExecutionException e) {					
					e.printStackTrace();
				} catch (IOException e) {					
					e.printStackTrace();
				}
			} else {
				//BA.Log("FutureText = null");
				
			}	
			return mVisibility;
		}
		
		return mVisibility;		
	}
	
	@Hide
	protected static class ABMTab {
		protected String ReturnName="";
		protected String TabText="";
		protected ABMContainer TabPage=null;
		protected int TabSizeSmall=0;
		protected int TabSizeMedium=0;
		protected int TabSizeLarge=0;
		protected int TabPageSizeSmall=0;
		protected int TabPageSizeMedium=0;
		protected int TabPageSizeLarge=0;
		protected boolean Enabled=true;
		protected boolean Active=false;
		protected String IconName="";
		protected String IconAwesomeExtra="";
		protected String hRef="";
		protected String IconBadge="";
		protected int Counter=0;
		
		ABMTab() {			
		}
		
		protected ABMTab Clone() {
			ABMTab t = new ABMTab();
			t.ReturnName=ReturnName;
			t.TabText=TabText;
			t.TabPage=TabPage.Clone();
			t.TabSizeSmall=TabSizeSmall;
			t.TabSizeMedium=TabSizeMedium;
			t.TabSizeLarge=TabSizeLarge;
			t.TabPageSizeSmall=TabPageSizeSmall;
			t.TabPageSizeMedium=TabPageSizeMedium;
			t.TabPageSizeLarge=TabPageSizeLarge;
			t.Enabled=Enabled;
			t.Active=Active;
			t.IconName = IconName;
			t.IconAwesomeExtra=IconAwesomeExtra;
			t.hRef = hRef;
			t.IconBadge=IconBadge;
			t.Counter = Counter;
			return t;
		}
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMTabs c = new ABMTabs();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;
		c.Theme = Theme.Clone();
		c.Type = Type;
		c.mVisibility = mVisibility;	
		for (Map.Entry<String, ABMTab> entry : Tabs.entrySet()) {
			c.Tabs.put(entry.getKey(), entry.getValue().Clone());
		}
		return c;
	}
}
