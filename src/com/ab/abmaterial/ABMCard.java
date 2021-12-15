package com.ab.abmaterial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;

@Author("Alain Bailleul")
@Events(values={"LinkClicked(Card as String, Action as String)"})
@ShortName("ABMCard")
public class ABMCard extends ABMComponent {
	private static final long serialVersionUID = 231622813315834527L;
	protected ThemeCard Theme=new ThemeCard();
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	public String Title="";
	public String Content="";
	public String Image="";
	public boolean IsReveal=false;
	protected boolean IsPanel=false;
	public String Size=ABMaterial.CARD_NOTSPECIFIED;
	protected transient List<String> Actions = new ArrayList<String>();
	public boolean PanelTruncateText=false;
	public boolean IsTextSelectable=true;
	
	public void InitializeAsCard(ABMPage page, String id, String title, String content, String size, String themeName) {
		this.ID = id;
		this.Type = ABMaterial.UITYPE_CARD;
		this.Title = title;
		this.Page = page;
		this.Content = content;
		this.Size = size;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Cards.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Cards.get(themeName.toLowerCase()).Clone();				
			}
		}	
		IsInitialized=true;
	}
	
	public void InitializeAsPanel(ABMPage page, String id, String content, String themeName) {
		this.ID = id;
		this.Type = ABMaterial.UITYPE_CARD;
		this.IsPanel = true;
		this.Content = content;
		this.Page = page;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Cards.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Cards.get(themeName.toLowerCase()).Clone();				
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
	
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}
	
	public void AddAction(String actionTitle) {
		Actions.add(actionTitle);
	}
	
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Cards.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Cards.get(themeName.toLowerCase()).Clone();				
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
		String selectable="";
		if (!IsTextSelectable) {
			selectable = " notselectable ";
		}
		j.SetProp("class", BuildClass() + selectable);
		if (!ToolTipText.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-position", ToolTipPosition);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-delay", ""+ToolTipDelay);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-tooltip", ToolTipText);
		}
		j.SetHtml(BuildBody());
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Card Refresh: " + ID);}
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
		s.append("<div " + toolTip + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"");
		String selectable="";
		if (!IsTextSelectable) {
			selectable = " notselectable ";
		}
		s.append(BuildClass() + selectable);
		s.append("\">");
		s.append(BuildBody());					
		s.append("</div>\n");	
		IsBuild=true;
		return s.toString();
	}
	
	@Hide
	protected String BuildClass() {			
		StringBuilder s = new StringBuilder();	
		s.append(super.BuildExtraClasses());
		ThemeCard card = Theme;
		String toolTip2="";
		String Sticky="";
		
		if (!ToolTipText.equals("")) {
			toolTip2 = " tooltipped ";
		}
		String rtl = "";
		if (this.getRightToLeft()) {
			rtl = " abmrtl ";
		}
		if (IsPanel) {
			s.append("card-panel " + Size + " " + rtl + toolTip2 + ABMaterial.GetColorStr(card.BackColor, card.BackColorIntensity, "") + " " + mVisibility + " " + card.ZDepth);
		} else {
			if (!Image.equals("")) {
				s.append("card " + Sticky + Size + " " + rtl + toolTip2 + ABMaterial.GetColorStr(card.BackColor, card.BackColorIntensity, "") + " " + mVisibility + " " + card.ZDepth);
			} else {
				s.append("card " + Sticky + rtl + toolTip2 + ABMaterial.GetColorStr(card.BackColor, card.BackColorIntensity, "") + " " + mVisibility + " " + card.ZDepth);
			}
		}
		s.append(mIsPrintableClass);
		s.append(mIsOnlyForPrintClass);
		return s.toString(); 
	}
	
	@Hide
	protected String BuildBody() {
		StringBuilder s = new StringBuilder();
		
		ThemeCard card = Theme;
		if (IsPanel) {
			String trunk="";
			if (PanelTruncateText) {
				trunk = " truncate ";
			}
			
			s.append("<span class=\"" + ABMaterial.GetColorStr(card.ContentForeColor, card.ContentForeColorIntensity, "text") + trunk + "\">" + BuildStringBody(Content) + "</span>\n");
		} else {
			if (Image.equals("")) {
				s.append("<div class=\"card-content " + ABMaterial.GetColorStr(card.ContentForeColor, card.ContentForeColorIntensity, "text") + "\">\n");
				s.append("<span class=\"card-title " + ABMaterial.GetColorStr(card.TitleForeColor, card.TitleForeColorIntensity, "text") + "\">" + BuildStringBody(Title) + "</span>\n");
				s.append("<p>" + BuildStringBody(Content) + "</p>\n");
	            s.append("</div>\n");
	            if (Actions.size()>0) {
	            	s.append("<div class=\"card-action\">\n");
	            	for (String act: Actions) {
	            		s.append("<button class=\"btn-flat " + ABMaterial.GetWavesEffect(ABMaterial.WAVESEFFECT_LIGHT, false) + " transparent " + ABMaterial.GetColorStr(card.ActionForeColor, card.ActionForeColorIntensity, "text") + "\" onclick=\"cardclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "', '" + ABMaterial.HTMLConv().htmlEscape(act, Page.PageCharset) + "')\">" + ABMaterial.HTMLConv().htmlEscape(act, Page.PageCharset) + "</button>\n");		            		
	            	}
	            	s.append("</div>\n");
	            }
			} else {
				if (IsReveal) {
					s.append("<div class=\"card-image " + ABMaterial.GetWavesEffect(card.WavesEffect, card.WavesCircle) + "\">\n");
					s.append("<img src=\"" + Image + "\" class=\"activator\">\n");
					s.append("</div>\n");
					s.append("<div class=\"card-content " + ABMaterial.GetColorStr(card.BackColor, card.BackColorIntensity, "") + " " + ABMaterial.GetColorStr(card.ContentForeColor, card.ContentForeColorIntensity, "text") + "\">\n");
					if (this.getRightToLeft()) {
						s.append("<span class=\"card-title activator " + ABMaterial.GetColorStr(card.TitleForeColor, card.TitleForeColorIntensity, "text") + "\">" + BuildStringBody(Title) + "<i class=\"material-icons left\">more_vert</i></span>\n");
					} else {
						s.append("<span class=\"card-title activator " + ABMaterial.GetColorStr(card.TitleForeColor, card.TitleForeColorIntensity, "text") + "\">" + BuildStringBody(Title) + "<i class=\"material-icons right\">more_vert</i></span>\n");
					}
					if (Actions.size()>0) {
						s.append("<div class=\"card-action\">\n");
						for (String act: Actions) {
							s.append("<button class=\"btn-flat " + ABMaterial.GetWavesEffect(ABMaterial.WAVESEFFECT_LIGHT, false) + " transparent " + ABMaterial.GetColorStr(card.ActionForeColor, card.ActionForeColorIntensity, "text") + "\" onclick=\"cardclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "', '" + ABMaterial.HTMLConv().htmlEscape(act, Page.PageCharset) + "')\">" + ABMaterial.HTMLConv().htmlEscape(act, Page.PageCharset) + "</button>\n");
		            	}
						s.append("</div>\n");
					}
					s.append("</div>\n");
					s.append("<div class=\"card-reveal " + ABMaterial.GetColorStr(card.BackColor, card.BackColorIntensity, "") + " " + ABMaterial.GetColorStr(card.BackColor, card.BackColorIntensity, "") + " " + ABMaterial.GetColorStr(card.TitleForeColor, card.TitleForeColorIntensity, "text") + "\">\n");
					if (this.getRightToLeft()) {
						s.append("<span class=\"card-title " + ABMaterial.GetColorStr(card.TitleForeColor, card.TitleForeColorIntensity, "text") + "\">" + BuildStringBody(Title) + "<i class=\"material-icons left\">close</i></span>\n");
					} else {
						s.append("<span class=\"card-title " + ABMaterial.GetColorStr(card.TitleForeColor, card.TitleForeColorIntensity, "text") + "\">" + BuildStringBody(Title) + "<i class=\"material-icons right\">close</i></span>\n");
					}
					s.append("<p>" + BuildStringBody(Content) + "</p>\n");
					s.append("</div>\n");
				} else {
					s.append("<div class=\"card-image\">\n");
					s.append("<img src=\"" + Image + "\">\n");
					s.append("<span class=\"card-title " + ABMaterial.GetColorStr(card.TitleForeColor, card.TitleForeColorIntensity, "text") + "\">" + BuildStringBody(Title) + "</span>\n");
					s.append("</div>\n");
					s.append("<div class=\"card-content " + ABMaterial.GetColorStr(card.BackColor, card.BackColorIntensity, "") + " " + ABMaterial.GetColorStr(card.ContentForeColor, card.ContentForeColorIntensity, "text") + "\">\n");
					s.append("<p>" + BuildStringBody(Content) + "</p>\n");
		            s.append("</div>\n");
		            if (Actions.size()>0) {
						s.append("<div class=\"card-action\">\n");
						for (String act: Actions) {
							s.append("<button class=\"btn-flat " + ABMaterial.GetWavesEffect(ABMaterial.WAVESEFFECT_LIGHT, false) + " transparent " + ABMaterial.GetColorStr(card.ActionForeColor, card.ActionForeColorIntensity, "text") + "\" onclick=\"cardclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "', '" + ABMaterial.HTMLConv().htmlEscape(act, Page.PageCharset) + "')\">" + ABMaterial.HTMLConv().htmlEscape(act, Page.PageCharset) + "</button>\n");
		            	}
						s.append("</div>\n");
					}
				}
			}
		}
		return s.toString();
	}
	
	protected String BuildCustomBody() {
		StringBuilder s = new StringBuilder();
		
		return s.toString();
	}
	
	@Hide
	protected String BuildStringBody(String str) {
		StringBuilder s = new StringBuilder();	
		
		String v = ABMaterial.HTMLConv().htmlEscape(str, Page.PageCharset);
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
	
	@Override
	protected ABMComponent Clone() {
		ABMCard c = new ABMCard();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;
		c.Theme = Theme.Clone();
		c.Type = Type;
		c.mVisibility = mVisibility;			
		for (String a: Actions) {
			c.Actions.add(a);
		}
		c.Content = Content;
		c.Image = Image;
		c.IsPanel = IsPanel;
		c.IsReveal = IsReveal;
		c.Size = Size;
		c.Title = Title;
		c.ToolTipDelay = ToolTipDelay;
		c.ToolTipPosition = ToolTipPosition;
		c.ToolTipText = ToolTipText;
		return c;
		
	}
	
}
