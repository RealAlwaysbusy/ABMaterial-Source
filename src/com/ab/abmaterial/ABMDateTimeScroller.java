package com.ab.abmaterial;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.keywords.DateTime;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;
import anywheresoftware.b4j.object.WebSocket.SimpleFuture;

@Author("Alain Bailleul")  
@ShortName("ABMDateTimeScroller")
@Events(values={"Changed(dateMilliseconds as String)","ChangedISO(dateISO as String)"})
public class ABMDateTimeScroller extends ABMComponent {
	private static final long serialVersionUID = -222176232151397850L;
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	protected ThemeDateTimeScroller Theme=new ThemeDateTimeScroller();
	protected String mMode=ABMaterial.DATETIMESCROLLER_MODE_SCROLLER;
	protected String mType=ABMaterial.DATETIMESCROLLER_TYPE_DATETIME;
	public String TitleDateFormat="mm/dd/yy";
	public String ReturnDateFormat="mm/dd/yy";
	public String DateOrder="mmddy";
	public boolean TimeShowAMPM=true;
	public boolean TimeShowSeconds=false;
	public String TitleTimeFormat="hh:ii A";
	public String ReturnTimeFormat="hh:ii A";
	public int DateStartYearNowMinus=100;
	public int DateEndYearNowPlus=1;
	public String DateMonthNames = "['January','February','March','April','May','June', 'July','August','September','October','November','December']";
	public String DateMonthNamesShort = "['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']";
	public String DateDayNames = "['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']";
	public String DateDayNamesShort = "['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']";
	public String DateMonthText = "Month";
	public String DateDayText = "Day";
	public String DateYearText = "Year";
	public String DateShortYearCutoff="+10";
	public String TimeHourText = "Hours";
	public String TimeMinuteText = "Minutes";
	public String TimeSecondsText = "Seconds";
	public String TimeAMPMText = "&nbsp;";
	public String PickText = "Set";
	public String CancelText = "Cancel";
	protected transient SimpleFuture FutureText;
	protected transient SimpleFuture FutureTextISO;
	protected transient SimpleFuture FutureTextISOZ;
	protected long mDate=0;
	protected String mDateISO="";
	protected String mDateISOZ="";
	protected boolean IsDirty=false;
	public String Title="";
	protected int SizeSmall=12;
	protected int SizeLarge=12;
	protected int SizeMedium=12;	
	protected int OffsetSmall=0;
	protected int OffsetMedium=0;
	protected int OffsetLarge=0;	
	protected String InputType=ABMaterial.INPUT_TEXT;
	protected boolean mEnabled=true;
	public String IconName="";
	public String IconAwesomeExtra="";
	public String SuccessMessage="";
	public String WrongMessage="";
	public String PlaceHolderText="";
	public boolean Narrow=false;
	public boolean ShowOnFocus=true;
	
	protected boolean NeedsReInit=false;
	
	public boolean IsTextSelectable=true;
		
	public void Initialize(ABMPage page, String id, String dtsType, String dtsMode, long date, String title, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_DTSCROLLER;	
		this.mMode = dtsMode;
		this.mType = dtsType;
		if (date==ABMaterial.NOW) {
			this.mDate = DateTime.getNow();
		} else {
			this.mDate = date;
		}
		this.Title = title;
		this.SizeSmall = 12;
		this.SizeMedium = 12;
		this.SizeLarge = 12;
		this.OffsetSmall=0;
		this.OffsetMedium=0;
		this.OffsetLarge=0;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.DateTimeScrollers.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.DateTimeScrollers.get(themeName.toLowerCase()).Clone();				
			}
		}		
		IsInitialized=true;		
	}
	
	public void InitializeWithSize(ABMPage page, String id, String dtsType, String dtsMode, long date, String title, int offsetSmall, int offsetMedium, int offsetLarge, int sizeSmall, int sizeMedium, int sizeLarge, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_DTSCROLLER;	
		this.mMode = dtsMode;
		this.mType = dtsType;
		this.mDate = date;
		this.Title = title;
		this.SizeSmall = sizeSmall;
		this.SizeMedium = sizeMedium;
		this.SizeLarge = sizeLarge;
		this.OffsetSmall=offsetSmall;
		this.OffsetMedium=offsetMedium;
		this.OffsetLarge=offsetLarge;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.DateTimeScrollers.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.DateTimeScrollers.get(themeName.toLowerCase()).Clone();				
			}
		}		
		IsInitialized=true;		
	}	
	
	public void InitializeISO(ABMPage page, String id, String dtsType, String dtsMode, String date, String title, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_DTSCROLLER;	
		this.mMode = dtsMode;
		this.mType = dtsType;
		if (date.equals(ABMaterial.NOWISO)) {
			TimeZone tz = TimeZone.getDefault();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			df.setTimeZone(tz);
			this.mDateISO = df.format(new Date());
		} else {
			this.mDateISO = date;
		}
		this.Title = title;
		this.SizeSmall = 12;
		this.SizeMedium = 12;
		this.SizeLarge = 12;
		this.OffsetSmall=0;
		this.OffsetMedium=0;
		this.OffsetLarge=0;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.DateTimeScrollers.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.DateTimeScrollers.get(themeName.toLowerCase()).Clone();				
			}
		}		
		IsInitialized=true;		
	}
	
	public void InitializeWithSizeISO(ABMPage page, String id, String dtsType, String dtsMode, String date, String title, int offsetSmall, int offsetMedium, int offsetLarge, int sizeSmall, int sizeMedium, int sizeLarge, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_DTSCROLLER;	
		this.mMode = dtsMode;
		this.mType = dtsType;
		if (date.equals(ABMaterial.NOWISO)) {
			TimeZone tz = TimeZone.getDefault();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			df.setTimeZone(tz);
			this.mDateISO = df.format(new Date());
		} else {
			this.mDateISO = date;
		}
		this.Title = title;
		this.SizeSmall = sizeSmall;
		this.SizeMedium = sizeMedium;
		this.SizeLarge = sizeLarge;
		this.OffsetSmall=offsetSmall;
		this.OffsetMedium=offsetMedium;
		this.OffsetLarge=offsetLarge;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.DateTimeScrollers.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.DateTimeScrollers.get(themeName.toLowerCase()).Clone();				
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
		return ArrayName.toLowerCase() + ID.toLowerCase() + "-parent";
	}
		
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}	
	
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.DateTimeScrollers.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.DateTimeScrollers.get(themeName.toLowerCase()).Clone();	
				NeedsReInit=true;
			}
		}
	}
	
	public void SetTooltip(String text, String position, int delay) {
		this.ToolTipText = text;
		this.ToolTipPosition = position;
		this.ToolTipDelay = delay;			
	}
	
	public void SetDate(long date) {
		this.mDate = date;
		this.IsDirty = true;
	}
	
	public void SetDateISO(String date) {
		this.mDateISO = date;
		this.IsDirty = true;
	}
	
	public boolean getEnabled() {		
		return mEnabled;
	}
	
	public void setEnabled(boolean enabled) {
		IsEnabledDirty=true;
		GotLastEnabled=true;
		mEnabled=enabled;
	}
	
	public long GetDate() {	
		
		if (!(FutureText==null)) {
			try {	
				String s = (String) FutureText.getValue();
				mDateISO = (String) FutureTextISO.getValue();
				mDateISOZ = (String) FutureTextISOZ.getValue();
				mDate = Long.parseLong(s);
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
		
		return mDate;
	}
	
	public String GetDateISO() {	
		
		if (!(FutureText==null)) {
			try {	
				String s = (String) FutureText.getValue();
				mDateISO = (String) FutureTextISO.getValue();
				mDateISOZ = (String) FutureTextISOZ.getValue();
				mDate = Long.parseLong(s);			
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
		
		return mDateISO;
	}
	
	public String GetDateISOZ() {	
		
		if (!(FutureText==null)) {
			try {	
				String s = (String) FutureText.getValue();
				mDateISOZ = (String) FutureTextISOZ.getValue();
				mDate = Long.parseLong(s);			
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
		
		return mDateISOZ;
	}
		
	@Override
	protected void CleanUp() {
		StringBuilder s = new StringBuilder();
		s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"').scroller('destroy');");
		Page.ws.Eval(s.toString(), null);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("DateTimeScroller CleanUp: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
		super.CleanUp();
	}
	
	@Override
	protected void RemoveMe() {
		StringBuilder s = new StringBuilder();
		s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"').scroller('destroy');");
		Page.ws.Eval(s.toString(), null);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("DateTimeScroller RemoveMe: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent");
	}
	
	/*
	'	 DateFormat:
	'	 -----------	
	'    m - month of year (no leading zero)
	'    mm - month of year (two digit)
	'    M - month Name short
	'    MM - month Name long
	'    d - day of month (no leading zero)
	'    dd - day of month (two digit)
	'    D - day of week (short)
	'    DD - day of week (long)
	'    y - year (two digit)
	'    yy - year (four digit)
	'    '...' - literal text
	'    '' - single quote
	'    anything Else - literal text

	'	 TimeFormat:
	'	 -----------	
	'    h - 12 hour format (no leading zero)
	'    hh - 12 hour format (leading zero)
	'    H - 24 hour format (no leading zero)
	'    HH - 24 hour format (leading zero)
	'    i - minutes (no leading zero)
	'    ii - minutes (leading zero)
	'    s - seconds (no leading zero)
	'    ss - seconds (leading zero)
	'    a - lowercase am/pm
	'    A - uppercase AM/PM
	'    '...' - literal text
	'    '' - single quote
	'    anything Else - literal text
	*/
	
	protected String BuildJavaScript() {
		StringBuilder s = new StringBuilder();
		s.append("var date = new Date();");
		s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"').scroller('destroy').scroller({");
		s.append("theme: 'android-ics light',");
		s.append("abmTheme: '" + Theme.ThemeName.toLowerCase() + "',");
		s.append("showOnFocus: " + this.ShowOnFocus + ",");
		s.append("mode: '" + this.mMode + "',");
		s.append("preset: '" + this.mType + "',");
		s.append("dateFormat: '" + this.TitleDateFormat + "',");
		s.append("retDateFormat: '" + this.ReturnDateFormat + "',");
		s.append("disabled: " + !this.mEnabled + ",");
		s.append("dateOrder: '" + this.DateOrder + "',");
		s.append("ampm: " + this.TimeShowAMPM + ",");
		s.append("seconds: " + this.TimeShowSeconds + ",");
		s.append("timeFormat: '" + this.TitleTimeFormat + "',");
		s.append("retTimeFormat: '" + this.ReturnTimeFormat + "',");
		s.append("startYear: date.getFullYear() - " + this.DateStartYearNowMinus + ",");
		s.append("endYear: date.getFullYear() + " + this.DateEndYearNowPlus + ",");
		s.append("monthNames: " + this.DateMonthNames + ",");
		s.append("monthNamesShort: " + this.DateMonthNamesShort + ",");
		s.append("dayNames: " + this.DateDayNames + ",");
		s.append("dayNamesShort: " + this.DateDayNamesShort + ",");
		s.append("shortYearCutoff: '" + this.DateShortYearCutoff + "',");
		s.append("monthText: '" + this.DateMonthText  + "',");
		s.append("dayText: '" + this.DateDayText + "',");
		s.append("yearText: '" + this.DateYearText + "',");
		s.append("hourText: '" + this.TimeHourText  + "',");
		s.append("minuteText: '" + this.TimeMinuteText  + "',");
		s.append("secText: '" + this.TimeSecondsText + "',");
		s.append("ampmText: '" + this.TimeAMPMText + "',");
		s.append("setText: '" + this.PickText + "',");
		s.append("cancelText: '" + this.CancelText + "'");
		s.append("});");
		
		if (mDate!=0) {
			s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"').scroller('setDate', '" + mDate + "', true);");
		}
		if (!mDateISO.equals("")) {
			s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"').scroller('setDateISO', '" + mDateISO + "', true);");
		}
		
		s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"label').css('color', $('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"label').attr('inactivecolor'));");
		return s.toString();
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
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		if (mEnabled) {
			Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"').scroller('enable');", null);
		} else {
			Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"').scroller('disable');", null);
		}
		
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent");			
		ThemeDateTimeScroller in = Theme;
		String selectable="";
		if (!IsTextSelectable) {
			selectable = " notselectable ";
		}
		j.SetProp("class", BuildClassParent(in));
		if (Narrow) {
			j.SetProp("style", "margin-top:0");
		}
		
		if (!ToolTipText.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-position", ToolTipPosition);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-delay", "" + ToolTipDelay);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-tooltip", ToolTipText);
		}
		j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		if (Narrow) {
			j.SetProp("style", "cursor: pointer;margin-bottom:0");
		}
		
		String toolTip2="";
		if (!ToolTipText.equals("")) {
			toolTip2 = " tooltipped ";
		}
		if (mDate!=0) {
			j.SetProp("class", toolTip2 + " valid " + ABMaterial.GetColorStr(in.InputColor, in.InputColorIntensity, "text") + selectable);
		} else {
			j.SetProp("class", toolTip2 + " " + ABMaterial.GetColorStr(in.InputColor, in.InputColorIntensity, "text") + selectable);
		}
		if (!PlaceHolderText.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "placeholder",ABMaterial.HTMLConv().htmlEscape(PlaceHolderText, Page.PageCharset));
		} else {
			ABMaterial.RemoveProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "placeholder");
		}
						
		j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label");
		if (mDate!=0 || !PlaceHolderText.equals("")) {
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label", "active");
		} else {
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label", "active");
		}
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label", "activecolor", ABMaterial.GetColorStrMap(in.InputFocusForeColor, in.InputFocusForeColorIntensity) );
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label", "inactivecolor", ABMaterial.GetColorStrMap(in.InputForeColor, in.InputForeColorIntensity) );
		
		j.SetHtml(BuildBodyInfo(Title, false));
		
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label", "data-error", ABMaterial.HTMLConv().htmlEscape(WrongMessage, Page.PageCharset));
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label", "data-success", ABMaterial.HTMLConv().htmlEscape(SuccessMessage, Page.PageCharset));
		
		if (mDate!=0 && IsDirty) {
			Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"').scroller('setDate', '" + mDate + "', true);", null);			
		}
		if (!mDateISO.equals("") && IsDirty) {
			Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"').scroller('setDateISO', '" + mDateISO + "', true);", null);			
		}
		IsDirty=false;
		
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("DateTimeScroller Refresh 2: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		if (NeedsReInit) {
			FirstRun();
		}
		NeedsReInit=false;
		
		super.Refresh();
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
	protected String Build() {
		if (Theme.ThemeName.equals("default")) {
			Theme.Colorize(Page.CompleteTheme.MainColor);
		}
		StringBuilder s = new StringBuilder();					
		String toolTip="";
		if (!ToolTipText.equals("")) {
			toolTip=" data-position=\"" + ToolTipPosition + "\" data-delay=\"" + ToolTipDelay + "\" data-tooltip=\"" + ToolTipText + "\" ";			
		}
		ThemeDateTimeScroller in = Theme;
		String NarrowStr="";
		if (Narrow) {
			NarrowStr = " style=\"margin-top: 0\"";
		}
		s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent\" class=\"" + BuildClassParent(in) +"\"" + NarrowStr + ">\n");
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
				s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent-icon\" class=\" prefix\">" + Page.svgIconmap.getOrDefault(IconName.toLowerCase(), "") + "</i>\n");
				break;
			default:
				s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent-icon\" class=\"material-icons prefix\">" + ABMaterial.HTMLConv().htmlEscape(IconName, Page.PageCharset) + "</i>\n");																	
			}
		}
		String placeholder="";
		if (!PlaceHolderText.equals("")){
			placeholder = " placeholder=\"" + ABMaterial.HTMLConv().htmlEscape(PlaceHolderText, Page.PageCharset) + "\" ";
		}
					
		NarrowStr = "";
		if (Narrow) {
			NarrowStr = ";margin-bottom: 0";
		}
		String selectable="";
		if (!IsTextSelectable) {
			selectable = " notselectable ";
		}
		if (ArrayName.equals("")) {
			s.append("<input eventname=\"" + ID.toLowerCase() + "\" returnvalue=\"0\" " + placeholder + toolTip + disabled + "readonly=\"true\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"\" id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"\" type=\"text\" class=\"" + BuildClass() + selectable + "\" style=\"cursor: pointer" + NarrowStr + "\">\n");
		} else {
			s.append("<input eventname=\"" + ArrayName.toLowerCase() + "\" returnvalue=\"0\" " + placeholder + toolTip + disabled + "readonly=\"true\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"\" id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"\" type=\"text\" class=\"" + BuildClass() + selectable + "\" style=\"cursor: pointer" + NarrowStr + "\">\n");
		}
		s.append("<label style=\"cursor: pointer\" id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label\" for=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\"" + BuildErrorMessages() + " class=\"active" + selectable + "\" activecolor=\"" + ABMaterial.GetColorStrMap(in.InputFocusForeColor, in.InputFocusForeColorIntensity) + "\"  inactivecolor=\"" + ABMaterial.GetColorStrMap(in.InputForeColor, in.InputForeColorIntensity) + "\" iconid=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent-icon\">" + BuildBodyInfo(Title, false) + "<span class=\"caret\">&nbsp;<i class=\"mdi-action-assignment-turned-in\"></i></span></label>\n");
		s.append(BuildBody());
		s.append("</div>\n");	
		IsBuild=true;
		return s.toString();		
	}
	
	@Hide
	protected String BuildClass() {			
		StringBuilder s = new StringBuilder();
		s.append(super.BuildExtraClasses());
		ThemeDateTimeScroller l=Theme;		
		if (mDate!=0) {
			s.append(" valid ");
		}
		String toolTip2="";
		if (!ToolTipText.equals("")) {
			toolTip2=" tooltipped ";
		}
		s.append(mVisibility + " ");	
		s.append(toolTip2);
		s.append(ABMaterial.GetColorStr(l.InputBackColor, l.InputBackColorIntensity, "") + " " + ABMaterial.GetColorStr(l.InputColor, l.InputColorIntensity, "text"));
		return s.toString();
	}
	
	@Hide	
	protected String BuildClassParent(ThemeDateTimeScroller in) {			
		StringBuilder s = new StringBuilder();
		String rtl="";
		if (this.getRightToLeft()) {
			rtl = " abmrtl ";
		}
		s.append("input-field input-field" +  Theme.ThemeName.toLowerCase() + "ics col s" + SizeSmall + " m" + SizeMedium + " l" + SizeLarge + " offset-s" + OffsetSmall + " offset-m" + OffsetMedium + " offset-l" + OffsetLarge + " " + mVisibility + " " + in.InputZDepth + " " + ABMaterial.GetColorStr(in.InputBackColor, in.InputBackColorIntensity, "") + " " + rtl + ABMaterial.GetColorStr(in.InputForeColor, in.InputForeColorIntensity, "text"));			
		s.append(mIsPrintableClass);
		s.append(mIsOnlyForPrintClass);
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
	
		return s.toString();
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMDateTimeScroller c = new ABMDateTimeScroller();
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
		
		return c;		
	}
}
