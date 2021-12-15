package com.ab.abmaterial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;

@Author("Alain Bailleul")  
@ShortName("ABMImageSlider")
public class ABMImageSlider extends ABMComponent {
	private static final long serialVersionUID = 3325948167888733954L;
	protected ThemeImageSlider Theme=new ThemeImageSlider();
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	protected String PositionType="";
	protected int px=0;
	protected int py=0;
	protected transient List<SlideSheet> Sheets = new ArrayList<SlideSheet>();
	protected boolean IsDirty=false;
		
	public void Initialize(ABMPage page, String id,String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_IMAGESLIDER;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.ImageSliders.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.ImageSliders.get(themeName.toLowerCase()).Clone();				
			}
		}
		IsInitialized=true;
	}
	
	@Override
	protected void ResetTheme() {
		UseTheme(Theme.ThemeName);
	}
	
	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase();
	}
	
	public void AddSlideImage(String image, String title, String subTitle, String slideType) {
		SlideSheet sheet = new SlideSheet();
		sheet.Image = image;
		sheet.Title = title;
		sheet.SubTitle = subTitle;
		sheet.SlideType = slideType;
		Sheets.add(sheet);
		IsDirty=true;
	}
	
	public void Clear() {
		Sheets.clear();;
		IsDirty=true;
	}
	
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}
	
	public void SetRelativePosition(String positionType, int x, int y) {
		this.PositionType = positionType;
		this.px = x;
		this.py = y;
	}
	
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.ImageSliders.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.ImageSliders.get(themeName.toLowerCase()).Clone();				
			}
		}
	}
	
	public void SetTooltip(String text, String position, int delay) {
		this.ToolTipText = text;
		this.ToolTipPosition = position;
		this.ToolTipDelay = delay;			
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
		Page.ws.Eval(BuildJavaScript(), null);		
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());		
		Page.ws.RunFunction("inittooltipped", Params);		
		super.FirstRun();
	}
	
	protected String BuildJavaScript() {
		StringBuilder s = new StringBuilder();
		s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').slider({full_width:" + Theme.FullWidth + ",indicators:" + Theme.Indicators + ",height:" + Theme.Height + ",transition:" + Theme.Transition + ",interval:" + Theme.Interval+ "});");
		return s.toString();
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		super.Refresh();
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		String position="";
		switch (PositionType) {
		case ABMaterial.POSITION_TOPLEFT:
			position = "position: absolute; top: " + py + "px; left: " + px + "px";
			break;
		case ABMaterial.POSITION_TOPRIGHT:
			position = "position: absolute; top: " + py + "px; right: " + px + "px";
			break;
		case ABMaterial.POSITION_BOTTOMLEFT:
			position = "position: absolute; bottom: " + py + "px; left: " + px + "px";
			break;
		case ABMaterial.POSITION_BOTTOMRIGHT:
			position = "position: absolute; bottom: " + py + "px; right: " + px + "px";
			break;
		}
		
		ThemeImageSlider sl = Theme;
		
		String toolTip2="";
		if (!ToolTipText.equals("")) {
			toolTip2=" tooltipped ";
		}
		
		j.SetProp("style", position);
		j.SetProp("class", "slider slider" + sl.ThemeName.toLowerCase() + " " + toolTip2 + " " + sl.ZDepth + " " + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses());
		
		if (!ToolTipText.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-position", ToolTipPosition);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-delay", "" + ToolTipDelay);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-tooltip", ToolTipText);
		}
				
		int counter=1;
		StringBuilder s = new StringBuilder();
		s.append("<ul id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "sheets\" class=\"slides\">\n");
		for (SlideSheet sh: Sheets) {
			s.append("<li>\n");
			s.append("<img id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "image" + counter + "\" src=\"" + sh.Image + "\">\n");
			s.append("<div class=\"caption " + sh.SlideType + "\">\n");
			
			String v = ABMaterial.HTMLConv().htmlEscape(sh.Title, Page.PageCharset);
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
			v=v.replace("{AL}", "<a rel=\"nofollow\" target=\"_blank\" href=\"");
			v=v.replace("{AT}", "\">");
			v=v.replace("{/AL}", "</a>");						
			
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
					repl = "<i class=\"" + IconName + " " + IconColor + "\"></i>";
					break;
				case "fa ":
				case "fa-":
					repl = "<i class=\"" + IconName + " " + IconColor + "\"></i>";
					break;
				case "abm":
					repl = "<i>" + Page.svgIconmap.getOrDefault(IconName.toLowerCase(), "") + "</i>"; 
					break;
				default:
					repl = "<i class=\"material-icons " + IconColor + "\">" + IconName + "</i>";
				}
				v=v.replace(vv,repl );
				start = v.indexOf("{IC:");
			}
			
			s.append("<h3 id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "title" + counter + "\" class=\"" + ABMaterial.GetColorStr(sl.TitleForeColor, sl.TitleForeColorIntensity, "text") + "\">" + v + "</h3>\n");
			
			v = ABMaterial.HTMLConv().htmlEscape(sh.SubTitle, Page.PageCharset);
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
			v=v.replace("{AL}", "<a rel=\"nofollow\" target=\"_blank\" href=\"");
			v=v.replace("{AT}", "\">");
			v=v.replace("{/AL}", "</a>");					
			
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
			
			start = v.indexOf("{IC:");
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
					repl = "<i class=\"" + IconName + " " + IconColor + "\"></i>";
					break;
				case "fa ":
				case "fa-":
					repl = "<i class=\"" + IconName + " " + IconColor + "\"></i>";
					break;
				case "abm":
					repl = "<i>" + Page.svgIconmap.getOrDefault(IconName.toLowerCase(), "") + "</i>"; 
					break;
				default:
					repl = "<i class=\"material-icons " + IconColor + "\">" + IconName + "</i>";
				}
				v=v.replace(vv,repl );
				start = v.indexOf("{IC:");
			}
			
			s.append("<h5 id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "subtitle" + counter + "\" class=\"" + ABMaterial.GetColorStr(sl.SubTitleForeColor, sl.SubTitleForeColorIntensity, "text") + "\">" + v + "</h5>\n");
			s.append("</div>\n");
			s.append("</li>\n");
			counter+=1;
		}
		s.append("</ul>\n");
		j.SetHtml(s.toString());
		
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		Params.Add(this.Theme.FullWidth);
		Params.Add(this.Theme.Indicators);
		Params.Add(this.Theme.Height);
		Params.Add(this.Theme.Transition);
		Params.Add(this.Theme.Interval);

		Page.ws.RunFunction("reinitslider", Params);

		IsDirty=false;
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("ImageSlider Refresh: " + ID);}
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
		ThemeImageSlider sl=Theme;
		String toolTip2="";
		String toolTip="";
		if (!ToolTipText.equals("")) {
			toolTip=" data-position=\"" + ToolTipPosition + "\" data-delay=\"" + ToolTipDelay + "\" data-tooltip=\"" + ToolTipText + "\" "; 
			toolTip2=" tooltipped ";
		}
		String position="";
		switch (PositionType) {
		case ABMaterial.POSITION_TOPLEFT:
			position = " style=\"position: absolute; top: " + py + "px; left: " + px + "px\" ";
			break;
		case ABMaterial.POSITION_TOPRIGHT:
			position = " style=\"position: absolute; top: " + py + "px; right: " + px + "px\" ";
			break;
		case ABMaterial.POSITION_BOTTOMLEFT:
			position = " style=\"position: absolute; bottom: " + py + "px; left: " + px + "px\" ";
			break;
		case ABMaterial.POSITION_BOTTOMRIGHT:
			position = " style=\"position: absolute; bottom: " + py + "px; right: " + px + "px\" ";
			break;
		}
		
		s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" " + toolTip + " class=\"slider abmltr slider" + sl.ThemeName.toLowerCase() + " " + toolTip2 + " " + sl.ZDepth + " " + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() + "\" " + position +">\n");
		s.append("<ul id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "sheets\" class=\"slides\">\n");
		int counter=1;
		for (SlideSheet sh: Sheets) {
			s.append("<li>\n");
			s.append("<img id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "image" + counter + "\" src=\"" + sh.Image + "\">\n");
			s.append("<div class=\"caption " + sh.SlideType + "\">\n");
			
			String v = ABMaterial.HTMLConv().htmlEscape(sh.Title, Page.PageCharset);
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
			v=v.replace("{AL}", "<a rel=\"nofollow\" target=\"_blank\" href=\"");
			v=v.replace("{AT}", "\">");
			v=v.replace("{/AL}", "</a>");	
			
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
					repl = "<i class=\"" + IconName + " " + IconColor + "\"></i>";
					break;
				case "fa ":
				case "fa-":
					repl = "<i class=\"" + IconName + " " + IconColor + "\"></i>";
					break;
				case "abm":
					repl = "<i>" + Page.svgIconmap.getOrDefault(IconName.toLowerCase(), "") + "</i>"; 
					break;
				default:
					repl = "<i class=\"material-icons " + IconColor + "\">" + IconName + "</i>";
				}
				v=v.replace(vv,repl );
				start = v.indexOf("{IC:");
			}
			
			s.append("<h3 id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "title" + counter + "\" class=\"" + ABMaterial.GetColorStr(sl.TitleForeColor, sl.TitleForeColorIntensity, "text") + "\">" + v + "</h3>\n");
			
			v = ABMaterial.HTMLConv().htmlEscape(sh.SubTitle, Page.PageCharset);
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
			v=v.replace("{AL}", "<a rel=\"nofollow\" target=\"_blank\" href=\"");
			v=v.replace("{AT}", "\">");
			v=v.replace("{/AL}", "</a>");	
			
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
			
			start = v.indexOf("{IC:");
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
			
			s.append("<h5 id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "subtitle" + counter + "\" class=\"" + ABMaterial.GetColorStr(sl.SubTitleForeColor, sl.SubTitleForeColorIntensity, "text") + "\">" + v + "</h5>\n");
			s.append("</div>\n");
			s.append("</li>\n");
			counter+=1;
		}
		s.append("</ul>\n");
		s.append("</div>\n");		
		IsBuild=true;
		IsDirty=false;
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
	
		return s.toString();
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMImageSlider c = new ABMImageSlider();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;
		c.Theme = Theme.Clone();
		c.Type = Type;
		c.mVisibility = mVisibility;	
		c.ToolTipDelay = ToolTipDelay;
		c.ToolTipPosition = ToolTipPosition;
		c.ToolTipText = ToolTipText;
		c.PositionType = PositionType;
		c.px = px;
		c.py = py;
		for (SlideSheet s: Sheets) {
			c.Sheets.add(s.Clone());
		}
		return c;
	}
	
	protected class SlideSheet {
		protected String Image="";
		protected String Title="";
		protected String SubTitle="";
		protected String SlideType="center-align";		
		
		protected SlideSheet Clone() {
			SlideSheet s = new SlideSheet();
			s.Image = Image;
			s.Title = Title;
			s.SubTitle = SubTitle;
			s.SlideType = SlideType;
			return s;
		}
	}

}
