package com.ab.abmaterial;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import java.util.Map.Entry;

import com.ab.abmaterial.ThemeList.ItemTheme;

import anywheresoftware.b4a.BA;
//import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;

@Author("Alain Bailleul")  
@ShortName("ABMList")
@Events(values={"Clicked(ItemId as String)"})
public class ABMList extends ABMComponent {			
	private static final long serialVersionUID = -2819633702144349076L;
	protected ThemeList Theme=new ThemeList();	
	protected transient Map<String, String> Items = new LinkedHashMap<String, String>();
	protected transient Map<String, ABMItem> Parents = new LinkedHashMap<String,ABMItem>();
	protected int MaxHeight=0;
	protected String ListCollapseType="accordion";
	public int SubItemLeftPadding=5;
	
	public boolean IsTextSelectable=true;

	public void Initialize(ABMPage page, String id, String listCollapseType, String themeName) {
		this.ID = id;			
		this.Type = ABMaterial.UITYPE_LIST;
		this.Page = page;	
		this.ListCollapseType = listCollapseType;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Lists.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Lists.get(themeName.toLowerCase()).Clone();				
			}
		}	
		IsInitialized=true;
	}	
	
	public void InitializeWithMaxHeight(ABMPage page, String id, String listCollapseType, int maxHeight, String themeName) {
		this.ID = id;			
		this.Type = ABMaterial.UITYPE_LIST;
		this.Page = page;	
		this.MaxHeight=maxHeight;	
		this.ListCollapseType = listCollapseType;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Lists.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Lists.get(themeName.toLowerCase()).Clone();				
			}
		}		
		IsInitialized=true;
	}	
	
	@Override
	protected void ResetTheme() {
		UseTheme(Theme.ThemeName);
		for (Map.Entry<String, ABMItem> entry : Parents.entrySet()) {
			if (entry.getValue().Value!=null) {
				entry.getValue().Value.ResetTheme();
			}
			for (Map.Entry<String, ABMItem> subentry : entry.getValue().SubValues.entrySet()) {
				if (subentry.getValue().Value!=null) {
					subentry.getValue().Value.ResetTheme();
				}
			}
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
	
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Lists.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Lists.get(themeName.toLowerCase()).Clone();				
			}
		}
	}
	
	@Override
	protected void CleanUp() {
		super.CleanUp();
	}
	
	@Override
	protected void RemoveMe() {
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
	}
	
	@Override
	protected void FirstRun() {			
		if (!this.ListCollapseType.equals("")) {
			anywheresoftware.b4a.objects.collections.List Params2 = new anywheresoftware.b4a.objects.collections.List();
			Params2.Initialize();
			Params2.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());		
			Page.ws.RunFunction("reinitcollapsable", Params2);			
		}
		Page.ws.Eval(BuildJavaScript(), null);
		
		super.FirstRun();
	}
	
	protected String BuildJavaScript() {
		StringBuilder s = new StringBuilder();
		
		
    	return s.toString();
	}	
	
	public void ScrollTo(String returnId, boolean smooth) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		Params.Add(returnId);
		if (smooth) {
			Params.Add("1");
		} else {
			Params.Add("0");
		}
		Page.ws.RunFunction("listscrollto", Params);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("List ScrollTo: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
		
	public void AddItem(String returnId, ABMComponent component) {
		Items.put(returnId.toLowerCase(), "");
		ABMItem value = new ABMItem();
		value.Value = component;
		value.returnId = returnId;
		Parents.put(returnId.toLowerCase(), value);
	}
	
	public void AddItemWithTheme(String returnId, ABMComponent component, String themeName ) {
		Items.put(returnId.toLowerCase(), "");
		ABMItem value = new ABMItem();
		value.Value = component;
		value.returnId = returnId;
		value.ItemTheme = themeName;
		Parents.put(returnId.toLowerCase(), value);
	}
	
	public void AddItemEx(String returnId, ABMComponent component, String RAWHTML, String themeName, String style, String liExtraStyle ) {
		Items.put(returnId.toLowerCase(), "");
		ABMItem value = new ABMItem();
		value.Value = component;
		value.returnId = returnId;
		value.ItemTheme = themeName;
		value.ExtraStyle = style;
		value.LiExtraStyle = liExtraStyle;
		value.RAW = RAWHTML;
		Parents.put(returnId.toLowerCase(), value);
	}
		
	public void AddSubItem(String parentReturnId, String returnId, ABMComponent component) {
		Items.put(returnId.toLowerCase(), parentReturnId);
		ABMItem value = new ABMItem();		
		value.Value = component;
		value.returnId= returnId;
		ABMItem par = Parents.getOrDefault(parentReturnId.toLowerCase(), null);
		if (par!=null) {
			par.SubValues.put(returnId.toLowerCase(),value);
		}
	}
	
	public void AddSubItemWithTheme(String parentReturnId, String returnId, ABMComponent component, String themeName) {
		Items.put(returnId.toLowerCase(), parentReturnId);
		ABMItem value = new ABMItem();		
		value.Value = component;
		value.returnId= returnId;
		value.ItemTheme = themeName;
		ABMItem par = Parents.getOrDefault(parentReturnId.toLowerCase(), null);
		if (par!=null) {
			par.SubValues.put(returnId.toLowerCase(),value);
		}
	}
	
	public void AddSubItemEx(String parentReturnId, String returnId, ABMComponent component, String RAWHTML, String themeName, String style, String liExtraStyle ) {
		Items.put(returnId.toLowerCase(), parentReturnId);
		ABMItem value = new ABMItem();		
		value.Value = component;
		value.returnId= returnId;
		value.ItemTheme = themeName;
		value.ExtraStyle = style;
		value.LiExtraStyle = liExtraStyle;
		value.RAW = RAWHTML;
		ABMItem par = Parents.getOrDefault(parentReturnId.toLowerCase(), null);
		if (par!=null) {
			par.SubValues.put(returnId.toLowerCase(),value);
		}
	}
		
	public void Clear(){
		for (Map.Entry<String, ABMItem> entry : Parents.entrySet()) {
			if (entry.getValue().Value!=null) {
				entry.getValue().Value.CleanUp();
			}			
		}
		Items.clear();
		Parents.clear();
	}
	
	public void Remove(String returnId) {
		ABMItem item = Parents.getOrDefault(returnId.toLowerCase(), null);
		if (item==null) {
			for (Entry<String,ABMItem> entry: Parents.entrySet()) {
				ABMItem item2 = entry.getValue().SubValues.getOrDefault(returnId.toLowerCase(), null);
				if (item2!=null) {
					entry.getValue().SubValues.remove(returnId.toLowerCase());
					return;
				}
			}
		} else {
			item.SubValues.clear();
			Parents.remove(returnId.toLowerCase());
		}
	}
		
	public ABMComponent Item(String returnId) {
		String parent = Items.getOrDefault(returnId.toLowerCase(), "$NOTFOUND$");
		if (parent.equals("$NOTFOUND$")) {
			return null;
		}
		if (parent.equals("")) {
			ABMItem item = Parents.getOrDefault(returnId.toLowerCase(), null);
			if (item!=null) {
				return item.Value;
			}
			return null;
		} else {
			ABMItem item = Parents.getOrDefault(parent.toLowerCase(), null);
			if (item!=null) {
				ABMItem subitem = item.SubValues.getOrDefault(returnId.toLowerCase(), null);
				if (subitem!=null) {
					return subitem.Value;
				}
			}
			return null;
		}
	}
	
	public void SetActive(String returnId, boolean active) {
		ABMItem item = Parents.getOrDefault(returnId.toLowerCase(), null);
		if (item==null) {
			for (Entry<String,ABMItem> entry: Parents.entrySet()) {
				ABMItem item2 = entry.getValue().SubValues.getOrDefault(returnId.toLowerCase(), null);
				if (item2!=null) {
					item2.IsActive=active;
					return;
				}
			}
		} else {
			item.IsActive=active;
		}
	}
	
	public void SetAllActive(boolean active) {
		for (Map.Entry<String, ABMItem> entry : Parents.entrySet()) {
			entry.getValue().IsActive = active;
			for (Map.Entry<String, ABMItem> subentry : entry.getValue().SubValues.entrySet()) {
				subentry.getValue().IsActive = active;
			}
		}	
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		super.Refresh();
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		String cl="";
		ThemeList l = Theme;
		String collapsible = "";
		if (!this.ListCollapseType.equals("")) {
			collapsible="collapsible";			
		}
		if (MaxHeight==0) {
			cl = mVisibility + " " + collapsible + " " + ABMaterial.GetColorStr(l.BackColor, l.BackColorIntensity, "") + " " + l.ZDepth;
		} else {
			cl = mVisibility + " " + collapsible + " " + ABMaterial.GetColorStr(l.BackColor, l.BackColorIntensity, "") + " " + l.ZDepth;
		}
		String selectable="";
		if (!IsTextSelectable) {
			selectable = " notselectable ";
		}
		j.SetProp("class", cl + selectable + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses());
		j.SetProp("style", BuildStyle());
		
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());			
		Page.ws.RunFunction("getactiveheaders", Params);
				
		j.SetHtml(BuildBody());		
				
		Page.ws.RunFunction("initcollapse", Params);
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("List Refresh 2: " + ID);}
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
		ThemeList l = Theme;
		String collapsible = "";
		String datacollapsible="";
		if (!this.ListCollapseType.equals("")) {
			collapsible="collapsible";
			datacollapsible = " data-collapsible=\"" + ListCollapseType + "\" ";
		}
		String selectable="";
		if (!IsTextSelectable) {
			selectable = " notselectable ";
		}
		s.append("<ul class=\" " + mVisibility + " " + collapsible + " " + ABMaterial.GetColorStr(l.BackColor, l.BackColorIntensity, "") + " " + l.ZDepth + selectable + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() + "\" " + datacollapsible + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildStyle() + "\">\n");		
		s.append(BuildBody());
		s.append("</ul>\n");
		IsBuild=true;
		return s.toString();		
	}
	
	@Hide
	protected String BuildStyle() {
		StringBuilder s = new StringBuilder();
		if (MaxHeight>0) {
			s.append(" overflow:auto;height:" + MaxHeight + "px;");
		}
		if (Theme.BorderWidth > 0) {
			s.append(" border-top: " + Theme.BorderWidth + "px solid " + ABMaterial.GetColorStrMap(Theme.BorderColor,  Theme.BorderColorIntensity) + ";");
			s.append(" border-right: " + Theme.BorderWidth + "px solid " + ABMaterial.GetColorStrMap(Theme.BorderColor,  Theme.BorderColorIntensity) + ";");
			s.append(" border-left: " + Theme.BorderWidth + "px solid " + ABMaterial.GetColorStrMap(Theme.BorderColor,  Theme.BorderColorIntensity) + ";");
		}
		
		if (!Theme.BorderStyle.equals("")) {
			s.append(" border-style: " + Theme.BorderStyle + ";");
		}
		return s.toString();
	}
	
	@Hide
	protected String BuildClass() {		
		StringBuilder s = new StringBuilder();
		return s.toString();
	}
	
	@Hide
	protected String BuildBody() {
		StringBuilder s = new StringBuilder();
		ThemeList l = Theme;
		String collapsibleheader = "";
		String collapsiblebody = "";
		String subbody = "";
		if (!this.ListCollapseType.equals("")) {
			collapsibleheader="collapsible-header";
			collapsiblebody="collapsible-body";		
		}
		for (Map.Entry<String, ABMItem> entry : Parents.entrySet()) {
			
			if (!(entry.getValue().LiExtraStyle.equals(""))) {
				s.append("<li style=\"" + entry.getValue().LiExtraStyle + "\">\n");
			} else {
				s.append("<li>\n");
			}	
			
			ItemTheme it2 = l.Item(entry.getValue().ItemTheme);
			if (it2!=null) {
				subbody = "subbodyitem" + l.ThemeName.toLowerCase() + it2.ThemeName.toLowerCase();
			} else {
				subbody = "bodyitem" + l.ThemeName.toLowerCase();
			}
			String Active="";
			if (entry.getValue().IsActive) {
				Active = " active ";
			}
			s.append("<div returnid=\"" + entry.getValue().returnId + "\" class=\"" + collapsibleheader + " " + subbody + Active + "\" onclick=\"listclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "','" + entry.getValue().returnId + "')\" id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + entry.getValue().returnId.toLowerCase() + "\" style=\"border-bottom-color: " + ABMaterial.GetColorStrMap(l.ItemDividerColor, l.ItemDividerColorIntensity) + ";-webkit-tap-highlight-color: rgba(0,0,0,0);" + entry.getValue().ExtraStyle + "\">\n");
			if (entry.getValue().RAW.equals("")) {
				s.append(entry.getValue().Value.Build());
			} else {
				s.append(entry.getValue().RAW);
			}
			s.append("</div>");
			if (!entry.getValue().SubValues.isEmpty()) {
				s.append("<div class=\"" + collapsiblebody + " " + "\" style=\"cursor: pointer; border-bottom-style: none;-webkit-tap-highlight-color: rgba(0,0,0,0);" + entry.getValue().ExtraStyle + "\">\n");
				for (Map.Entry<String, ABMItem> subentry : entry.getValue().SubValues.entrySet()) {
					ItemTheme it = l.Item(subentry.getValue().ItemTheme);
					if (it!=null) {
						subbody = "subbodyitem" + l.ThemeName.toLowerCase() + it.ThemeName.toLowerCase();
					} else {
						subbody = "subbody" + l.ThemeName.toLowerCase();
					}
					String SubActive="";
					if (entry.getValue().IsActive) {
						SubActive = " active ";
					}
					s.append("<div class=\"" + subbody + SubActive + "\">\n");
					
					s.append("<div returnid=\"" + subentry.getValue().returnId + "\" onclick=\"listclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "','" + subentry.getValue().returnId + "')\" id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + subentry.getValue().returnId + "\" style=\"border-bottom: 1px solid " + ABMaterial.GetColorStrMap(l.SubItemDividerColor, l.SubItemDividerColorIntensity) + ";padding-left: 6px;padding-right: 6px;" + subentry.getValue().ExtraStyle + "\">\n"); //+ ";padding: 6px 1rem;\">\n");
					if (subentry.getValue().RAW.equals("")) {
						s.append(subentry.getValue().Value.Build());
					} else {
						s.append(subentry.getValue().RAW);
					}
					s.append("</div>\n");
					s.append("</div>\n");
					
				}				
			}	
			s.append("</li>\n");
		}	
		
		return s.toString();
	}
	
	protected class ABMItem implements java.io.Serializable {
		
		private static final long serialVersionUID = 2122987105873955787L;
		protected String returnId="";
		protected ABMComponent Value=null;		
		protected Map<String, ABMItem> SubValues = new LinkedHashMap<String, ABMItem>();
		protected String ItemTheme="default";
		protected boolean IsActive=false;
		protected String ExtraStyle="";
		protected String LiExtraStyle="";
		protected String RAW="";
		
		protected ABMItem Clone() {
			ABMItem c = new ABMItem();			
			c.Value = Value;
			c.returnId = returnId;
			c.ItemTheme = ItemTheme;
			c.IsActive=IsActive;
			c.ExtraStyle = ExtraStyle;
			c.LiExtraStyle = LiExtraStyle;
			c.RAW = RAW;
			for (Map.Entry<String, ABMItem> entry : SubValues.entrySet()) {
				c.SubValues.put(entry.getKey(), entry.getValue().Clone());
			}	
			return c;		
		}
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMList c = new ABMList();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;
		c.Type = Type;
		c.mVisibility = mVisibility;	
		c.Theme = Theme.Clone();
				
		for (Map.Entry<String, String> entry : Items.entrySet()) {
			c.Items.put(entry.getKey(), entry.getValue());
		}
		for (Map.Entry<String, ABMItem> entry : Parents.entrySet()) {
			c.Parents.put(entry.getKey(), entry.getValue().Clone());
		}		
		c.MaxHeight = MaxHeight;		
		return c;
	}
	
}
