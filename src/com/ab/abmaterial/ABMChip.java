package com.ab.abmaterial;

import java.io.IOException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;

@Author("Alain Bailleul")  
@ShortName("ABMChip")
@Events(values={"Clicked(Target As String)", "Closed(Target As String)"})
public class ABMChip extends ABMComponent {
	private static final long serialVersionUID = 6298026278527839837L;
	protected ThemeChip Theme=new ThemeChip();
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	public String Text="";
	public String Image="";
	protected boolean CanBeClosed=false;
	protected String FrontIcon="";
			
	public void Initialize(ABMPage page, String id, String text, boolean canBeClosed, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_CHIP;
		this.CanBeClosed = canBeClosed;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Chips.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Chips.get(themeName.toLowerCase()).Clone();				
			}
		}			
		this.Text = text;	
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
	
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}	
	
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Chips.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Chips.get(themeName.toLowerCase()).Clone();				
			}
		}
	}
	
	public void SetTooltip(String text, String position, int delay) {
		this.ToolTipText = text;
		this.ToolTipPosition = position;
		this.ToolTipDelay = delay;			
	}
		
	public void SetFrontIcon(String icon, String iconColor, String backgroundColor, String borderColor) {
		FrontIcon = "<div style=\"float: left;width: 32px;height: 32px;background-color: " + backgroundColor + ";margin-left: -12px;border-radius: 50%;border: 1px solid " + borderColor + ";\"><i class=\"" + icon + "\" style=\"font-size: 1rem;text-align: center;width: 32px;color: " + iconColor + ";\"></i></div>";
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
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());		
		Page.ws.RunFunction("inittooltipped", Params);		
		super.FirstRun();
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		j.SetProp("class", BuildClass());
		
		if (!ToolTipText.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-position", ToolTipPosition);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-delay", ""+ToolTipDelay);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-tooltip", ToolTipText);
		}
		StringBuilder s = new StringBuilder();
		if (!Image.equals("")) {
			s.append("<img id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "image\" src=\"" + Image + "\">" );
		} else {
			if (!FrontIcon.equals("")) {
				s.append(FrontIcon);
			}
		}
		s.append(BuildBody());	
		if (CanBeClosed) {
			s.append("<i  onclick=\"chipclosedarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "')\" class=\"material-icons\" >close</i>");
		}
		ABMaterial.ReplaceMyInnerHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() , s.toString());
		
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Chip Refresh: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		super.Refresh();
	}
	
	@Override
	protected String Build() {
		if (Theme.ThemeName.equals("default")) {
			Theme.Colorize(Page.CompleteTheme.MainColor);
		}
		StringBuilder s = new StringBuilder();
		
		String toolTip="";
		if (!ToolTipText.equals("")) {
			toolTip=" data-position=\"" + ToolTipPosition + "\" data-delay=\"" + ToolTipDelay + "\" data-tooltip=\"" + ToolTipText + "\" "; 
		}
		
		s.append("<div onclick=\"chipclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "')\" " + toolTip + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"");
		s.append(BuildClass());	
		s.append("\"  style=\"cursor: pointer\">");
		if (!Image.equals("")) {
			s.append("<img id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "image\" src=\"" + Image + "\">" );
		} else {
			if (!FrontIcon.equals("")) {
				s.append(FrontIcon);
			}
		}
		s.append(BuildBody());	
		if (CanBeClosed) {
			s.append("<i  onclick=\"chipclosedarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "')\" class=\"material-icons\" >close</i>");
		}
		s.append("</div>\n");
	
		IsBuild=true;
		return s.toString();
	}
	
	@Hide
	protected String BuildClass() {			
		StringBuilder s = new StringBuilder();
		s.append(super.BuildExtraClasses());
		ThemeChip l=Theme;
		s.append("chip ");
		if (this.getRightToLeft()) {
			s.append("abmrtl ");
		}
		
		if (!ToolTipText.equals("")) {
			s.append("tooltipped ");
		}		
		s.append(mVisibility + " ");	
		s.append(l.ZDepth + " ");
		s.append(ABMaterial.GetColorStr(l.BackColor, l.BackColorIntensity, "") + " " + ABMaterial.GetColorStr(l.ForeColor, l.ForeColorIntensity, "text"));
		s.append(mIsPrintableClass);
		s.append(mIsOnlyForPrintClass);
		return s.toString(); 
	}
	
	@Hide
	protected String BuildBody() {
		StringBuilder s = new StringBuilder();		
	
		
		String v = ABMaterial.HTMLConv().htmlEscape(Text, Page.PageCharset);
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
	
	@Override
	protected ABMComponent Clone() {
		ABMChip c = new ABMChip();
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
		c.Text = Text;
		c.CanBeClosed=CanBeClosed;
		c.Image=Image;
		return c;
	}

}
