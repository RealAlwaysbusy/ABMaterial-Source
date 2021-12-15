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
import anywheresoftware.b4j.object.WebSocket.SimpleFuture;

@Author("Alain Bailleul")  
@ShortName("ABMCombo")
@Events(values={"Clicked(itemId as String)","LostFocus()","GotFocus()","Changed(value As String)"})
public class ABMCombo extends ABMComponent {
	private static final long serialVersionUID = -3279104850241782980L;
	protected ThemeCombo Theme=null;
	
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	protected String sReturnId="";
	public String Title="";		
	protected String InputType=ABMaterial.INPUT_TEXT;
	protected boolean mValidate=true;
	protected boolean mEnabled=true;
	public String IconName="";
	public String IconAwesomeExtra="";
	public String SuccessMessage="";
	public String WrongMessage="";
	public String PlaceHolderText="";
	public String DataBelow=ABMaterial.COMBO_DATA_BELOWINPUT;
	protected int SizeSmall=12;
	protected int SizeLarge=12;
	protected int SizeMedium=12;	
	protected int OffsetSmall=0;
	protected int OffsetMedium=0;
	protected int OffsetLarge=0;
	protected transient SimpleFuture FutureText;
	protected boolean IsDirty=false;
	protected String PositionType="";
	protected int px=0;
	protected int py=0;
	protected transient Map<String, ABMComboItem> Items = new LinkedHashMap<String, ABMComboItem>();
	protected int MaxHeight=0;
	public boolean Narrow=false;
	public String IsValid="";
	public boolean RaiseChangedEvent=false;	
	public boolean IsTextSelectable=true;
		
	public void Initialize(ABMPage page, String id, String title, int maxHeightPx, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_COMBO;
		this.Title = title;
		this.SizeSmall = 12;
		this.SizeMedium = 12;
		this.SizeLarge = 12;
		this.OffsetSmall=0;
		this.OffsetMedium=0;
		this.OffsetLarge=0;
		this.MaxHeight=maxHeightPx;
		Theme = new ThemeCombo();
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Combos.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Combos.get(themeName.toLowerCase()).Clone();				
			}
		}	
		if (Theme.ThemeName.equals("default")) {
			Theme.Colorize(Page.CompleteTheme.MainColor);
		}
		IsInitialized=true;
	}	
	
	public void InitializeWithSize(ABMPage page, String id, String title, int maxHeightPx, int offsetSmall, int offsetMedium, int offsetLarge, int sizeSmall, int sizeMedium, int sizeLarge, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_COMBO;
		this.Title = title;
		this.SizeSmall = sizeSmall;
		this.SizeMedium = sizeMedium;
		this.SizeLarge = sizeLarge;
		this.OffsetSmall=offsetSmall;
		this.OffsetMedium=offsetMedium;
		this.OffsetLarge=offsetLarge;
		this.MaxHeight=maxHeightPx;
		Theme = new ThemeCombo();
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Combos.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Combos.get(themeName.toLowerCase()).Clone();				
			}
		}			
		IsInitialized=true;
	}
	
	public void setValid(String valid) {
		IsValid = valid;		
		mValidate=false;
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
		Page.ws.RunFunction("comboscrollto", Params);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("combo ScrollTo: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public boolean getEnabled() {		
		return mEnabled;
	}
	
	public void setEnabled(boolean enabled) {
		IsEnabledDirty=true;
		GotLastEnabled=true;
		mEnabled=enabled;
	}
	
	public String getValid() {
		return IsValid;
	}
	
	public void setValidate(boolean valid) {
		this.mValidate=valid;
		IsValid="";
	}
	
	public boolean getValidate() {
		return mValidate;
	}
	
	@Override
	protected void ResetTheme() {
		UseTheme(Theme.ThemeName);
		for (Map.Entry<String, ABMComboItem> entry : Items.entrySet()) {
			if (entry.getValue().Value!=null) {
				entry.getValue().Value.ResetTheme();
			}
		}
	}
	
	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase() + "-parent";
	}
	
	public void SetFocus() {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());		
		Page.ws.RunFunction("inputsetfocus", Params);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("Combo SetFocus: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
			
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
		
	}
	
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Combos.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Combos.get(themeName.toLowerCase()).Clone();				
			}
		}
	}
	
	public void AddItem(String returnId, String inputText, ABMComponent component) {
		ABMComboItem value = new ABMComboItem();
		value.Value = component;
		value.returnId = returnId;
		value.inputText = inputText;
		Items.put(returnId.toLowerCase(), value);
	}
	
	public void Clear(){
		for (Map.Entry<String, ABMComboItem> entry : Items.entrySet()) {
			entry.getValue().Value.CleanUp();			
		}
		Items.clear();		
	}
	
	public ABMComponent GetComponent(String returnId) {
		ABMComboItem item = Items.getOrDefault(returnId.toLowerCase(), null);
		//BA.Log("Parent: " + parent);
		if (item==null) {
			return null;
		}
		return item.Value;
	}
	
	public void SetRelativePosition(String positionType, int x, int y) {
		this.PositionType = positionType;
		this.px = x;
		this.py = y;
	}
	
	public void SetActiveItemId(String returnId) {
		this.sReturnId = returnId;
		this.IsDirty = true;
	}
	
	public String GetActiveItemId() {
		if (!(FutureText==null)) {
			try {
				this.sReturnId = (String) FutureText.getValue();				
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
			BA.Log("FutureText = null");
		}
		if (sReturnId.equals("")) {
			sReturnId="-1";
		}
		return sReturnId;
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
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent");
	}
	
	@Override
	protected void FirstRun() {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());		
		Page.ws.RunFunction("inittooltipped", Params);
		anywheresoftware.b4a.objects.collections.List Params2 = new anywheresoftware.b4a.objects.collections.List();
		Params2.Initialize();
		Params2.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());		
		Page.ws.RunFunction("reinitcombo", Params2);
		
		super.FirstRun();
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent");			
		ThemeCombo in = Theme;
		String selectable="";
		if (!IsTextSelectable) {
			selectable = " notselectable ";
		}
		j.SetProp("class", BuildClassParent(in));
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
		if (Narrow) {
			position += "; margin-top: 0";
		}
		j.SetProp("style", position);
		if (!ToolTipText.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-position", ToolTipPosition);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-delay", "" + ToolTipDelay);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-tooltip", ToolTipText);
		}
		j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		if (Narrow) {
			j.SetProp("style", "cursor: pointer;margin-bottom: 0");
		}
		
		if (IsDirty ) {
			ABMComboItem item = Items.getOrDefault(sReturnId.toLowerCase(), null);
			String tmpReturnId="";
			String sText = "";
			if (item!=null) {
				sText = item.inputText;
				tmpReturnId = item.returnId;
			}			 
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "value", sText);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "returnid", tmpReturnId);
			Page.ws.Eval("$('#" +  ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').val('" + sText + "');",null);
		}
		String toolTip2="";
		if (!ToolTipText.equals("")) {
			toolTip2 = " tooltipped ";
		}
		if (mValidate) {
			j.SetProp("class", toolTip2 + " validate dropdown-button2 " + ABMaterial.GetColorStr(in.InputColor, in.InputColorIntensity, "text") + selectable);
		} else {
			j.SetProp("class", toolTip2 + " " + IsValid + " dropdown-button2 " + ABMaterial.GetColorStr(in.InputColor, in.InputColorIntensity, "text") + selectable);
		}
		if (!PlaceHolderText.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "placeholder",BuildBodyInfo(PlaceHolderText,true));
		} else {
			ABMaterial.RemoveProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "placeholder");
		}
				
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		Params.Add(!mEnabled);
		Page.ws.RunFunction("SetDisabled", Params);
		
		int gutter=0;
		if (!IconName.equals("")) {
			gutter = 45;
		}
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-gutter", ""+gutter );
				
		j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label");
		if (!sReturnId.equals("") || !PlaceHolderText.equals("")) {
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label", "active");
		} else {
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label", "active");
		}
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label", "activecolor", ABMaterial.GetColorStrMap(in.FocusForeColor, in.FocusForeColorIntensity) );
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label", "inactivecolor", ABMaterial.GetColorStrMap(in.ForeColor, in.ForeColorIntensity) );
				
		j.SetHtml(BuildBodyInfo(Title, false) + "<span class=\"caret\">&nbsp;&#9660;</span>");
		
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label", "data-error", ABMaterial.HTMLConv().htmlEscape(WrongMessage, Page.PageCharset));
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label", "data-success", ABMaterial.HTMLConv().htmlEscape(SuccessMessage, Page.PageCharset));
		
		j = Page.ws.GetElementById("dropdown" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		ABMaterial.SetStyleProperty(Page, "dropdown" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "background-color", ABMaterial.GetColorStrMap(Theme.ContentBackColor,Theme.ContentBackColorIntensity));
		j.SetHtml(BuildBody());
		
		anywheresoftware.b4a.objects.collections.List Params2 = new anywheresoftware.b4a.objects.collections.List();
		Params2.Initialize();
		Params2.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());			
		Page.ws.RunFunction("initdropdown", Params2);
		Page.ws.Eval("Materialize.updateTextFields();", null);
		
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Combo Refresh: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
		super.Refresh();
		IsDirty=false;
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
		ThemeCombo in = Theme;
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
		default:
			if (Narrow) {
				position = " style=\"margin-top: 0\" ";
			}
		}
		s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent\" class=\"" + BuildClassParent(in) +"\" " + position + " >\n");
		String disabled="";
		if (!mEnabled) {
			disabled= " disabled ";
		}
		if (!IconName.equals("")) {
			switch (IconName.substring(0, 3).toLowerCase()) {
			case "mdi":
				s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent-icon\" class=\"" + IconName + " prefix\"></i>\n");
				break;
			case "fa ":
			case "fa-":
				s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent-icon\" class=\"" + IconAwesomeExtra + " " + IconName + " prefix\"></i>\n");
				break;
			case "abm":
				s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent-icon\" class=\"prefix\">" + Page.svgIconmap.getOrDefault(IconName.toLowerCase(), "") + "</i>\n");
				break;
			default:
				s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent-icon\" class=\"material-icons prefix\">" + ABMaterial.HTMLConv().htmlEscape(IconName, Page.PageCharset) + "</i>\n");				
			}			
		}
		String placeholder="";
		if (!PlaceHolderText.equals("")){
			placeholder = " placeholder=\"" + BuildBodyInfo(PlaceHolderText, true) + "\" ";
		}
		int gutter=0;
		if (!IconName.equals("")) {
			gutter = 45;
		}
		ABMComboItem item = Items.getOrDefault(sReturnId.toLowerCase(), null);
		String sText = "";
		String tmpReturnId="";
		if (item!=null) {
			sText = item.inputText;
			tmpReturnId = item.returnId;
		}	
		String NarrowStr="";
		if (Narrow) {
			NarrowStr = ";margin-bottom: 0";
		}
		String selectable="";
		if (!IsTextSelectable) {
			selectable = " notselectable ";
		}
		if (this.RaiseChangedEvent) {
			s.append("<input returnid=\"" + tmpReturnId + "\" " + placeholder + toolTip + disabled + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"\" type=\"text\" class=\"" + BuildClass() + selectable + " raisechanged " + "\" data-beloworigin=\"" + DataBelow + "\" data-gutter=\"" + gutter +"\" data-activates=\"dropdown" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" value=\"" + sText + "\" style=\"cursor: pointer" + NarrowStr + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\">\n");
		} else {
			s.append("<input returnid=\"" + tmpReturnId + "\" " + placeholder + toolTip + disabled + "readonly=\"true\" id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"\" type=\"text\" class=\"" + BuildClass() + selectable + "\" data-beloworigin=\"" + DataBelow + "\" data-gutter=\"" + gutter +"\" data-activates=\"dropdown" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" value=\"" + sText + "\" style=\"cursor: pointer" + NarrowStr + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\">\n");
		}
		s.append("<label style=\"cursor: pointer\" id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label\" for=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\"" + BuildErrorMessages() + " class=\"active" + selectable + "\" activecolor=\"" + ABMaterial.GetColorStrMap(in.FocusForeColor, in.FocusForeColorIntensity) + "\"  inactivecolor=\"" + ABMaterial.GetColorStrMap(in.ForeColor, in.ForeColorIntensity) + "\" iconid=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent-icon\">" + BuildBodyInfo(Title, false) + "<span class=\"caret\">&nbsp;&#9660;</span></label>\n");
		s.append("<ul id=\"dropdown" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"dropdown-content" + selectable + "\" style=\"overflow-x: hidden;max-height: " + MaxHeight + "px;background-color:" + ABMaterial.GetColorStrMap(Theme.ContentBackColor,Theme.ContentBackColorIntensity)  + "\">\n");
		s.append(BuildBody());
		s.append("</ul>\n");
		s.append("</div>\n");	
		IsBuild=true;
		return s.toString();
	}
	
	@Hide
	protected String BuildClass() {			
		StringBuilder s = new StringBuilder();
		s.append(super.BuildExtraClasses());
		ThemeCombo l=Theme;
		s.append("dropdown-button2 ");
		if (mValidate) {
			s.append(" validate ");
		}
		s.append(" " + IsValid + " ");
		String toolTip2="";
		if (!ToolTipText.equals("")) {
			toolTip2=" tooltipped ";
		}
		s.append(mVisibility + " ");	
		s.append(toolTip2);
		s.append("transparent " + ABMaterial.GetColorStr(l.InputColor, l.InputColorIntensity, "text"));
		return s.toString(); 
	}	
	
	@Hide	
	protected String BuildClassParent(ThemeCombo in) {			
		StringBuilder s = new StringBuilder();
		String rtl="";
		if (this.getRightToLeft()) {
			rtl = "abmrtl ";
		}
		s.append("input-field combo" + in.ThemeName.toLowerCase()  + " col s" + SizeSmall + " m" + SizeMedium + " l" + SizeLarge + " offset-s" + OffsetSmall + " offset-m" + OffsetMedium + " offset-l" + OffsetLarge + " " + mVisibility + " " + in.ZDepth + " " + rtl + ABMaterial.GetColorStr(in.BackColor, in.BackColorIntensity, "") + " " + ABMaterial.GetColorStr(in.ForeColor, in.ForeColorIntensity + mIsPrintableClass + mIsOnlyForPrintClass, "text"));			
		return s.toString(); 
	}
	
	@Hide	
	protected String BuildErrorMessages() {			
		StringBuilder s = new StringBuilder();
		if (!WrongMessage.equals("")) {
			s.append(" data-error=\"" + ABMaterial.HTMLConv().htmlEscape(WrongMessage, Page.PageCharset) + "\"");
		}
		if (!SuccessMessage.equals("")) {
			s.append(" data-success=\"" + ABMaterial.HTMLConv().htmlEscape(SuccessMessage, Page.PageCharset) + "\"");
		}
					
		return s.toString(); 
	}
	

	@Hide
	protected String BuildBody() {
		StringBuilder s = new StringBuilder();
		ThemeCombo l = Theme;
		for (Map.Entry<String, ABMComboItem> entry : Items.entrySet()) {
			s.append("<li>\n"); 
			s.append("<div onclick=\"comboclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "','" + entry.getValue().inputText.replace("\\","\\\\").replace("'", "\\\'").replace("\"", "&quot;") + "','" + entry.getValue().returnId + "')\" id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + entry.getValue().returnId + "\" style=\"border-bottom: 1px solid " + ABMaterial.GetColorStrMap(l.ItemDividerColor, l.ItemDividerColorIntensity) + "\" returnid=\"" + entry.getValue().returnId + "\">\n"); // + ";padding: 0px 1rem;\">\n");
			s.append("<div class=\"valign-wrapper\">\n");
			s.append("<div style=\"min-width: 100%;\">\n");
			s.append(entry.getValue().Value.Build());
			s.append("</div>\n");
			s.append("</div>\n");
			
			s.append("</div>\n");
			s.append("</li>\n");
		}
		return s.toString();
	}
	
	protected String BuildBodyInfo(String text, boolean OnlyNBSP) {
		StringBuilder s = new StringBuilder();	
		
		String v = ABMaterial.HTMLConv().htmlEscape(text, Page.PageCharset);
		if (OnlyNBSP) {
			v=v.replace("{NBSP}", " ");
		} else {
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
		}
		s.append(v);
		
		return s.toString();
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMCombo c = new ABMCombo();
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
		c.mEnabled = mEnabled;
		c.FutureText = FutureText;		
		c.IconName = IconName;
		c.IconAwesomeExtra = IconAwesomeExtra;
		c.InputType = InputType;
		c.IsDirty = false;
		c.OffsetLarge = OffsetLarge;
		c.OffsetMedium = OffsetMedium;
		c.OffsetSmall = OffsetSmall;
		c.PlaceHolderText = PlaceHolderText;
		c.SizeLarge = SizeLarge;
		c.SizeMedium = SizeMedium;
		c.SizeSmall = SizeSmall;
		c.sReturnId = "";
		c.SuccessMessage = SuccessMessage;
		c.Title = Title;
		c.mValidate = mValidate;
		c.IsValid=IsValid;
		c.PositionType = PositionType;
		c.px = px;
		c.py = py;
		c.WrongMessage = WrongMessage;	
		c.MaxHeight = MaxHeight;
		return c;
	}
	
	protected class ABMComboItem implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1941158221933651835L;
		protected String returnId="";
		protected String inputText="";
		protected ABMComponent Value=null;		
		
		protected ABMComboItem Clone() {
			ABMComboItem c = new ABMComboItem();			
			c.Value = Value;
			c.returnId = returnId;		
			c.inputText=inputText;
			return c;		
		}
	}

}
