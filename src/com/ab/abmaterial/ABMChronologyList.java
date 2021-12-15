package com.ab.abmaterial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ab.abmaterial.ThemeChronologyList.ThemeChronologyBadge;

import anywheresoftware.b4a.BA;
//import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;

@Author("Alain Bailleul")  
@ShortName("ABMChronologyList")
public class ABMChronologyList extends ABMComponent {
	private static final long serialVersionUID = 2384325499273329981L;
	protected ThemeChronologyList Theme=new ThemeChronologyList();
	protected transient Map<String,ChronoSlide> slides = new LinkedHashMap<String,ChronoSlide>();
		
	public void Initialize(ABMPage page, String id, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_CHRONOLIST;	
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.ChronologyLists.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.ChronologyLists.get(themeName.toLowerCase()).Clone();				
			}
		}	
		IsInitialized=true;
	}
	
	public void AddSlide(String uniqueID, ABMContainer container, String chronologyBadgeTheme) {
		ChronoSlide slide = new ChronoSlide();
		slide.uniqueID=uniqueID.toLowerCase();
		slide.containers.add(container);
		if (!chronologyBadgeTheme.equals("")) {
			slide.chronoBadgeTheme = chronologyBadgeTheme;
		}
		slides.put(uniqueID.toLowerCase(), slide);
	}
	
	/**
	 * 
	 * minHeight is a string e.g. 200px
	 * 
	 */
	public void AddSlideMinHeight(String uniqueID, ABMContainer container, String minHeight, String chronologyBadgeTheme) {
		ChronoSlide slide = new ChronoSlide();
		slide.uniqueID=uniqueID.toLowerCase();
		slide.containers.add(container);
		slide.MinHeight = minHeight;
		if (!chronologyBadgeTheme.equals("")) {
			slide.chronoBadgeTheme = chronologyBadgeTheme;
		}
		slides.put(uniqueID.toLowerCase(), slide);
	}
	
	/**
	 * You can add more than one container to a slide (3.75+) 
	 */
	public void AddContainerToSlide(String uniqueID, ABMContainer container) {
		if (slides.containsKey(uniqueID.toLowerCase())) {
			ChronoSlide slide = slides.get(uniqueID.toLowerCase());
			slide.containers.add(container);
		} else {
			BA.Log("No slide found with uniqueID " + uniqueID);
		}
	}
	
	public void SetSlideTheme(String uniqueID, String chronologyBadgeTheme) {
		ChronoSlide slide = slides.getOrDefault(uniqueID.toLowerCase(), null);
		if (slide!=null) {
			slides.get(uniqueID.toLowerCase()).chronoBadgeTheme = chronologyBadgeTheme;
		}
	}
	
	/**
	 * Depreciated.  You should use GetContainerFromSlide() instead (3.75+) 
	 */
	public ABMContainer GetSlideContainer(String uniqueID) {
		BA.Log("Depreciated.  You should use GetContainerFromSlide() instead (3.75+)");
		
		return GetContainerFromSlide(uniqueID, 0);
	}
	
	public ABMContainer GetContainerFromSlide(String uniqueID, int index) {
		ChronoSlide slide = slides.getOrDefault(uniqueID.toLowerCase(), null);
		if (slide!=null) {
			if (index>slide.containers.size()) {
				BA.Log("Index is greater than the number of containers!");
				return null;
			}
			return slide.containers.get(index);			
		}
		return null;
	}
	
	public void PrepareComponentsForAllSlides() {
		for (Entry<String,ChronoSlide> entry: slides.entrySet()) {
			for (ABMContainer cont: entry.getValue().containers) {
				if (!cont.ID.equals("")) {
					cont.RunAllFirstRuns();					
				}
			}
		}
	}
	
	public void PrepareComponentsForSlide(String uniqueID) {
		ChronoSlide slide = slides.getOrDefault(uniqueID.toLowerCase(), null);
		if (slide!=null) {
			for (ABMContainer cont: slide.containers) {
				if (!cont.ID.equals("")) {
					cont.RunAllFirstRuns();			
				}
			}
						
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("ChronologyList PrepareComponentsForSlide: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
			
		}
	}
	
	public void Clear() {
		for (Entry<String,ChronoSlide> entry: slides.entrySet()) {
			for (ABMContainer cont: entry.getValue().containers) {
				cont.CleanUp();
			}
			entry.getValue().containers = new ArrayList<ABMContainer>();			
		}
		slides = new LinkedHashMap<String,ChronoSlide>();
		if (slides.size()>0) {
			for (Entry<String,ChronoSlide> entry: slides.entrySet()) {
				for (ABMContainer cont: entry.getValue().containers) {
					cont.CleanUp();
				}
				entry.getValue().containers = new ArrayList<ABMContainer>();				
			}
			slides = new LinkedHashMap<String,ChronoSlide>();
		}
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		IsBuild=false;
	}
	
	@Override
	protected void ResetTheme() {
		UseTheme(Theme.ThemeName);
	}
	
	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase() + "-parent";
	}
		
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}	
		
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.ChronologyLists.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.ChronologyLists.get(themeName.toLowerCase()).Clone();				
			}
		}
	}		
		
	@Override
	protected void CleanUp() {
		super.CleanUp();
	}
	
	@Override
	protected void RemoveMe() {
		if (slides.size()>0) {
			for (Entry<String,ChronoSlide> entry: slides.entrySet()) {
				for (ABMContainer cont: entry.getValue().containers) {
					cont.CleanUp();
				}
				entry.getValue().containers = new ArrayList<ABMContainer>();				
			}
			slides = new LinkedHashMap<String,ChronoSlide>();
		}
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent");		
	}
	
	
	
	@Override
	protected void FirstRun() {
		PrepareComponentsForAllSlides();
		Page.ws.Eval(BuildJavaScript(), null);
		
		super.FirstRun();
	}
	
	protected String BuildJavaScript() {
		StringBuilder s = new StringBuilder();	
		String tmpVar = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		tmpVar = tmpVar.replace("-", "_");
		s.append("PositionChronos('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "');");	
		return s.toString();
	}
	
	public void RefreshSlide(String uniqueID) {
		ChronoSlide slide = slides.getOrDefault(uniqueID.toLowerCase(), null);
		if (slide!=null) {
			ThemeChronologyBadge l = null;
			if (slide.chronoBadgeTheme.equals("")) {
				l = Theme.ChronologyBadge("default");
			} else {
				l = Theme.ChronologyBadge(slide.chronoBadgeTheme.toLowerCase());
			}
			JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-badge");
			j.SetProp("class", "chronology-badge chronology-badge" + Theme.ThemeName.toLowerCase() + "-" + l.ThemeName.toLowerCase());
			j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-panel");
			if (l.ColorizePointer) {
				j.SetProp("class", "chronology-panel chronology-panel" + Theme.ThemeName.toLowerCase() + "-" + l.ThemeName.toLowerCase() + " " + l.ContentBackColor + " " + l.ContentBackColorIntensity);
			} else {
				j.SetProp("class", "chronology-panel" + " " + l.ContentBackColor + " " + l.ContentBackColorIntensity);
			}
			j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon");
			if (l.IconName.equals("")) {
				j.SetProp("class", "");
				j.SetText("");
			} else {
				switch (l.IconName.substring(0, 3).toLowerCase()) {
				case "mdi":
					j.SetProp("class", l.IconName + " " + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text"));					
					break;
				case "fa ":
				case "fa-":
					j.SetProp("class", l.IconAwesomeExtra + " " + l.IconName + " " + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text"));					
					break;
				case "abm":
					j.SetHtml(Page.svgIconmap.getOrDefault(l.IconName.toLowerCase(), ""));					
					break;
				default:
					j.SetProp("class", "material-icons " + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text"));
					j.SetText(ABMaterial.HTMLConv().htmlEscape(l.IconName, Page.PageCharset));					
				}				
				
			}
			for (ABMContainer cont: slide.containers) {
				cont.Refresh();
			}
		}
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("ChronologyList RefreshSlide: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
	
	/**
	 * This is a limited version of Refresh() which will only refresh the chronology list properties + it slides, NOT its contents.		 
	 */
	public void RefreshNoContents(boolean DoFlush) {
		super.Refresh();	
		if (!IsBuild) {
			ABMaterial.ReplaceMyHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent", Build());			
			FirstRun();
			return;
		}
		
		for (Entry<String,ChronoSlide> entry: slides.entrySet()) {			
			ChronoSlide slide = entry.getValue();
			for (ABMContainer cont: slide.containers) {
				cont.RefreshNoContents(false);;
			}			
		}
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("ChronologyList RefreshNoContents: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}		
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		super.Refresh();
		if (!IsBuild) {
			BA.Log("Rebuilding...");
			ABMaterial.ReplaceMyHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent", Build());
			FirstRun();
			return;
		}
		ABMaterial.ChangeVisibility(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent", mVisibility);
		if (mIsPrintableClass.equals("")) {
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent", "no-print");
		} else {
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"-parent", "no-print");
		}
		if (mIsOnlyForPrintClass.equals("")) {
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent", "only-print");
		} else {
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"-parent", "only-print");
		}
		String LastSlide="";
		for (Entry<String,ChronoSlide> entry: slides.entrySet()) {	
			ChronoSlide slide = entry.getValue();
			if (slide.IsBuild==true) {
				LastSlide=slide.uniqueID.toLowerCase();
			}
		}
		
		for (Entry<String,ChronoSlide> entry: slides.entrySet()) {			
			ChronoSlide slide = entry.getValue();
			if (slide.IsBuild==false) {
				StringBuilder s = new StringBuilder();
				
				s.append("<li id=\"" + slide.uniqueID.toLowerCase() + "\" class=\"chronology-slide\">");
				ThemeChronologyBadge l = null;
				if (slide.chronoBadgeTheme.equals("")) {
					l = Theme.ChronologyBadge("default");
				} else {
					l = Theme.ChronologyBadge(slide.chronoBadgeTheme.toLowerCase());
				}
				s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-badge" + slide.uniqueID.toLowerCase() + "\" class=\"chronology-badge primary chronology-badge" + Theme.ThemeName.toLowerCase() + "-" + l.ThemeName.toLowerCase() + "\">\n");
				if (!l.IconName.equals("")) {
					switch (l.IconName.substring(0, 3).toLowerCase()) {
					case "mdi":
						s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon" + slide.uniqueID.toLowerCase() + "\" class=\"" + l.IconName + " " + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text") + "\"></i>\n");					
						break;
					case "fa ":
					case "fa-":
						s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon" + slide.uniqueID.toLowerCase() + "\" class=\"" + l.IconAwesomeExtra + " " + l.IconName + " " + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text") + "\"></i>\n");					
						break;
					case "abm":
						s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon" + slide.uniqueID.toLowerCase() + "\" class=\"" +  ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text") + "\">" + Page.svgIconmap.getOrDefault(l.IconName.toLowerCase(), "") + "</i>\n");					
						break;
					default:
						s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon" + slide.uniqueID.toLowerCase() + "\" class=\"material-icons " + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text") + "\">" + ABMaterial.HTMLConv().htmlEscape(l.IconName, Page.PageCharset) + "</i>\n");											
					}					
				} else {
					s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon" + slide.uniqueID.toLowerCase() + "\"></i>\n");
				}
				s.append("</div>");
				String MinHeight="";
				if (!slide.MinHeight.equals("")) {
					MinHeight="style=\"min-height: 200px\"";
				}
				if (l.ColorizePointer) {
					s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-panel" + slide.uniqueID.toLowerCase() + "\" class=\"chronology-panel chronology-panel" + Theme.ThemeName.toLowerCase() + "-" + l.ThemeName.toLowerCase()  + " " + l.ContentBackColor + " " + l.ContentBackColorIntensity + " \"" + MinHeight + ">\n");
				} else {
					s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-panel" + slide.uniqueID.toLowerCase() + "\" class=\"chronology-panel" + " " + l.ContentBackColor + " " + l.ContentBackColorIntensity + " \"" + MinHeight + ">\n");
				}
				for (ABMContainer cont: slide.containers) {
					s.append(cont.Build());
				}
				s.append("</div>\n");
				s.append("</li>");
				slide.IsBuild=true;
				
				if (LastSlide.equals("")) {
					ABMaterial.AddHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), s.toString());
				} else {
					ABMaterial.InsertHTMLAfter(Page, LastSlide, s.toString());
				}
				LastSlide=slide.uniqueID.toLowerCase();
				for (ABMContainer cont: slide.containers) {
					cont.RunAllFirstRuns();
				}
			} else {
				ThemeChronologyBadge l = null;
				if (slide.chronoBadgeTheme.equals("")) {
					l = Theme.ChronologyBadge("default");
				} else {
					l = Theme.ChronologyBadge(slide.chronoBadgeTheme.toLowerCase());
				}
				JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-badge" + slide.uniqueID.toLowerCase());
				j.SetProp("class", "chronology-badge chronology-badge" + Theme.ThemeName.toLowerCase() + "-" + l.ThemeName.toLowerCase());
				j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-panel" + slide.uniqueID.toLowerCase());
				if (l.ColorizePointer) {
					j.SetProp("class", "chronology-panel chronology-panel" + Theme.ThemeName.toLowerCase() + "-" + l.ThemeName.toLowerCase() + " " + l.ContentBackColor + " " + l.ContentBackColorIntensity);
				} else {
					j.SetProp("class", "chronology-panel" + " " + l.ContentBackColor + " " + l.ContentBackColorIntensity);
				}
				j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon" + slide.uniqueID.toLowerCase());
				if (l.IconName.equals("")) {
					j.SetProp("class", "");
					j.SetText("");
				} else {
					switch (l.IconName.substring(0, 3).toLowerCase()) {
					case "mdi":
						j.SetProp("class", l.IconName + " " + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text"));					
						break;
					case "fa ":
					case "fa-":
						j.SetProp("class", l.IconAwesomeExtra + " " + l.IconName + " " + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text"));					
						break;
					case "abm":
						j.SetHtml(Page.svgIconmap.getOrDefault(l.IconName.toLowerCase(), ""));					
						break;
					default:
						j.SetProp("class", "material-icons " + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text"));
						j.SetText(ABMaterial.HTMLConv().htmlEscape(l.IconName, Page.PageCharset));					
					}
				}
				if (DoFlush) {
					try {
						if (Page.ws.getOpen()) {
							if (Page.ShowDebugFlush) {BA.Log("ChronologyList Refresh 1: " + ID);}
							Page.ws.Flush();Page.RunFlushed();
						}
					} catch (IOException e) {			
						//e.printStackTrace();
					}
				}
				for (ABMContainer cont: slide.containers) {
					cont.RefreshInternal(DoFlush);
				}
			}
		}
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("ChronologyList Refresh 2: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
		Page.ws.Eval(BuildJavaScript(), null);
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("ChronologyList Refresh 3: " + ID);}
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
		s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent\" class=\"" + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() + "\" style=\"direction: ltr\">\n");
		s.append("<ul id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"chronology\" style=\"display: table;width: 100%\">");
		s.append(BuildBody());
		s.append("</ul>");
		s.append("</div>\n");
		IsBuild=true;
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
		for (Entry<String,ChronoSlide> entry: slides.entrySet()) {
			ChronoSlide slide = entry.getValue();
			if (slide.IsBuild==false) {
				
				s.append("<li id=\"" + slide.uniqueID.toLowerCase() + "\" class=\"chronology-slide\">");
				ThemeChronologyBadge l = null;
				
				if (slide.chronoBadgeTheme.equals("")) {
					l = Theme.ChronologyBadge("default");
				} else {
					l = Theme.ChronologyBadge(slide.chronoBadgeTheme.toLowerCase());
				}
				
				s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-badge" + slide.uniqueID.toLowerCase() + "\" class=\"chronology-badge primary chronology-badge" + Theme.ThemeName.toLowerCase() + "-" + l.ThemeName.toLowerCase() + "\">\n");
				if (!l.IconName.equals("")) {
					switch (l.IconName.substring(0, 3).toLowerCase()) {
					case "mdi":
						s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon" + slide.uniqueID.toLowerCase() + "\" class=\"" + l.IconName + " " + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text") + "\"></i>\n");					
						break;
					case "fa ":
					case "fa-":
						s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon" + slide.uniqueID.toLowerCase() + "\" class=\"" + l.IconAwesomeExtra + " " + l.IconName + " " + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text") + "\"></i>\n");					
						break;
					case "abm":
						s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon" + slide.uniqueID.toLowerCase() + "\" class=\"" + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text") + "\">" + Page.svgIconmap.getOrDefault(l.IconName.toLowerCase(), "") + "</i>\n");					
						break;
					default:
						s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon" + slide.uniqueID.toLowerCase() + "\" class=\"material-icons " + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text") + "\">" + ABMaterial.HTMLConv().htmlEscape(l.IconName, Page.PageCharset) + "</i>\n");											
					}				
				} else {
					s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon" + slide.uniqueID.toLowerCase() + "\"></i>\n");
				}
				s.append("</div>");
				String MinHeight="";
				if (!slide.MinHeight.equals("")) {
					MinHeight="style=\"min-height: 200px\"";
				}
				if (l.ColorizePointer) {
					s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-panel" + slide.uniqueID.toLowerCase() + "\" class=\"chronology-panel chronology-panel" + Theme.ThemeName.toLowerCase() + "-" + l.ThemeName.toLowerCase() + " " + l.ContentBackColor + " " + l.ContentBackColorIntensity + " \"" + MinHeight + ">\n");
				} else {
					s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-panel" + slide.uniqueID.toLowerCase() + "\" class=\"chronology-panel"  + " " + l.ContentBackColor + " " + l.ContentBackColorIntensity+ " \"" + MinHeight + ">\n");
				}
				for (ABMContainer cont: slide.containers) {
					s.append(cont.Build());
				}
				s.append("</div>\n");
				s.append("</li>");
				slide.IsBuild=true;
			}
		}
		return s.toString();
	}
	
	public void OrderSlides() {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		Page.ws.RunFunction("PositionChronos", Params);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("ChronologyList OrderSlides: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMChronologyList c = new ABMChronologyList();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;
		c.Theme = Theme.Clone();
		c.Type = Type;
		c.mVisibility = mVisibility;		
		return c;
	}
		
	@Hide
	protected class ChronoSlide implements java.io.Serializable {
		private static final long serialVersionUID = 7050458770593625908L;
		protected List<ABMContainer> containers = new ArrayList<ABMContainer>();
		protected boolean IsBuild=false;
		protected String chronoBadgeTheme="default";
		protected String uniqueID="";		
		protected String MinHeight="";
	}
}
