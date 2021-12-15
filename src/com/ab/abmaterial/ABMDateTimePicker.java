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
import anywheresoftware.b4a.keywords.Regex;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;
import anywheresoftware.b4j.object.WebSocket.SimpleFuture;

@Author("Alain Bailleul")  
@ShortName("ABMDateTimePicker")
@Events(values={"Changed(Target as String, dateMilliseconds as String)", "ChangedISO(Target as String, dateISO as String)", "ChangedWeek(Target as String, WeekString as String)", "ChangedWeekISO(Target as String, WeekString as String)"})
public class ABMDateTimePicker extends ABMComponent {	
	private static final long serialVersionUID = 2149325525454480118L;
	protected ThemeDateTimePicker Theme=new ThemeDateTimePicker();	
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	protected String mType=ABMaterial.DATETIMEPICKER_TYPE_DATETIME;
	protected boolean NeedsDate=true;
	protected boolean NeedsTime=true;
	
	public String ReturnDateFormat="YYYY-MM-DD";
	public String ReturnTimeFormat="HH:mm";
	public long MinimumDate=0;
	public long MaximumDate=0;
	public String MinimumDateISO="";
	public String MaximumDateISO="";
	public String Language="en";
	public int FirstDayOfWeek=0;
	public boolean ShortTime=false;
	public String ClearText = "Clear";
	public String PickText = "OK";
	public String CancelText = "Cancel";
	public String TodayText = "Today";
	public String WeekText = "Week";
	
		
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
	public boolean ClickThrough=false;
	public boolean Narrow=false;
	
	protected boolean NeedsReInit=false;
	
	public boolean IsTextSelectable=true;
		
	public void Initialize(ABMPage page, String id, String dtsType, long date, String title, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_DTPICKER;			
		this.mType = dtsType;
		switch (mType) {
		case ABMaterial.DATETIMEPICKER_TYPE_DATE:
		case ABMaterial.DATETIMEPICKER_TYPE_WEEK:	
			NeedsDate = true;
			NeedsTime = false;
			break;
		case ABMaterial.DATETIMEPICKER_TYPE_DATETIME:
			NeedsDate = true;
			NeedsTime = true;
			break;
		case ABMaterial.DATETIMEPICKER_TYPE_TIME:
			NeedsDate = false;
			NeedsTime = true;
			break;
		}
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
			if (Page.CompleteTheme.DateTimePickers.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.DateTimePickers.get(themeName.toLowerCase()).Clone();				
			}
		}	
		IsInitialized=true;
	}
	
	public void InitializeWithSize(ABMPage page, String id, String dtsType, long date, String title, int offsetSmall, int offsetMedium, int offsetLarge, int sizeSmall, int sizeMedium, int sizeLarge, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_DTPICKER;			
		this.mType = dtsType;
		switch (mType) {
		case ABMaterial.DATETIMEPICKER_TYPE_DATE:
		case ABMaterial.DATETIMEPICKER_TYPE_WEEK:	
			NeedsDate = true;
			NeedsTime = false;
			break;
		case ABMaterial.DATETIMEPICKER_TYPE_DATETIME:
			NeedsDate = true;
			NeedsTime = true;
			break;
		case ABMaterial.DATETIMEPICKER_TYPE_TIME:
			NeedsDate = false;
			NeedsTime = true;
			break;
		}
		if (date==ABMaterial.NOW) {
			this.mDate = DateTime.getNow();
		} else {
			this.mDate = date;
		}
		this.Title = title;
		this.SizeSmall = sizeSmall;
		this.SizeMedium = sizeMedium;
		this.SizeLarge = sizeLarge;
		this.OffsetSmall=offsetSmall;
		this.OffsetMedium=offsetMedium;
		this.OffsetLarge=offsetLarge;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.DateTimePickers.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.DateTimePickers.get(themeName.toLowerCase()).Clone();				
			}
		}	
		IsInitialized=true;
	}
	
	public void InitializeISO(ABMPage page, String id, String dtsType, String date, String title, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_DTPICKER;			
		this.mType = dtsType;
		switch (mType) {
		case ABMaterial.DATETIMEPICKER_TYPE_DATE:
		case ABMaterial.DATETIMEPICKER_TYPE_WEEK:	
			NeedsDate = true;
			NeedsTime = false;
			break;
		case ABMaterial.DATETIMEPICKER_TYPE_DATETIME:
			NeedsDate = true;
			NeedsTime = true;
			break;
		case ABMaterial.DATETIMEPICKER_TYPE_TIME:
			NeedsDate = false;
			NeedsTime = true;
			break;
		}
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
			if (Page.CompleteTheme.DateTimePickers.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.DateTimePickers.get(themeName.toLowerCase()).Clone();				
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
	
	public void InitializeWithSizeISO(ABMPage page, String id, String dtpType, String date, String title, int offsetSmall, int offsetMedium, int offsetLarge, int sizeSmall, int sizeMedium, int sizeLarge, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_DTPICKER;			
		this.mType = dtpType;
		switch (mType) {
		case ABMaterial.DATETIMEPICKER_TYPE_DATE:
		case ABMaterial.DATETIMEPICKER_TYPE_WEEK:	
			NeedsDate = true;
			NeedsTime = false;
			break;
		case ABMaterial.DATETIMEPICKER_TYPE_DATETIME:
			NeedsDate = true;
			NeedsTime = true;
			break;
		case ABMaterial.DATETIMEPICKER_TYPE_TIME:
			NeedsDate = false;
			NeedsTime = true;
			break;
		}
		this.mDateISO = date;
		this.Title = title;
		this.SizeSmall = sizeSmall;
		this.SizeMedium = sizeMedium;
		this.SizeLarge = sizeLarge;
		this.OffsetSmall=offsetSmall;
		this.OffsetMedium=offsetMedium;
		this.OffsetLarge=offsetLarge;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.DateTimePickers.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.DateTimePickers.get(themeName.toLowerCase()).Clone();				
			}
		}		
		IsInitialized=true;
	}	
		
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}	
	
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.DateTimePickers.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.DateTimePickers.get(themeName.toLowerCase()).Clone();	
				NeedsReInit=true;
			}
		}
	}
	
	public void SetTooltip(String text, String position, int delay) {
		this.ToolTipText = text;
		this.ToolTipPosition = position;
		this.ToolTipDelay = delay;			
	}
	
	/**
	 *  in case of a week, pass the startdate of the week
	 */
	public void SetDate(long date) {
		this.mDateISO="";
		this.mDate = date;
		this.IsDirty = true;
	}
	
	public void SetDateISO(String date) {
		this.mDate=0;
		this.mDateISO = date;
		this.IsDirty = true;
	}
	
	public long GetDate() {	
		if (mType==ABMaterial.DATETIMEPICKER_TYPE_WEEK) {
			BA.Log("Use GetDateWeek instead!");
			return 0;
		}
		if (!(FutureText==null)) {
			try {	
				String s = (String) FutureText.getValue();
				mDate = Long.parseLong(s);
				mDateISO = (String) FutureTextISO.getValue();
				mDateISOZ = (String) FutureTextISOZ.getValue();
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
		if (mType==ABMaterial.DATETIMEPICKER_TYPE_WEEK) {
			BA.Log("Use GetDateWeek instead!");
			return "";
		}
		if (!(FutureText==null)) {
			try {	
				String s = (String) FutureText.getValue();
				mDate = Long.parseLong(s);
				mDateISO = (String) FutureTextISO.getValue();	
				mDateISOZ = (String) FutureTextISOZ.getValue();
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
	
	public boolean getEnabled() {		
		return mEnabled;
	}
	
	public void setEnabled(boolean enabled) {
		IsEnabledDirty=true;
		GotLastEnabled=true;
		mEnabled=enabled;
	}
		
	/**
	 * Returns a ; delimited string format: WeekNumberInt;StartDateLong;EndDateLong 
	 * if an error it will look as 0;SelectedDayDateLong;0
	 */
	public String GetDateWeek() {
		String ret = "";
		if (mType!=ABMaterial.DATETIMEPICKER_TYPE_WEEK) {
			BA.Log("Use GetDate instead!");
			return "";
		}
		
		if (!(FutureText==null)) {
			try {	
				String s = (String) FutureText.getValue();
				String ss[] = Regex.Split(";", s);
				mDate = Long.parseLong(ss[1]);
				ret = s;
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
		if (ret.equals("")) {
			ret = "0;" + mDate + ";0";
		}		
		return ret;
	}
	
	public void Clear() {
		mDate=0;
		mDateISO="";
		mDateISOZ="";
		Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"').bootstrapMaterialDatePicker('clear', '');", null);
		try {
			if (Page.ws.getOpen())
			Page.ws.Flush();Page.RunFlushed();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns a ; delimited string format: WeekNumberInt;StartISO;EndISO 
	 * if an error it will look as 0;SelectedDayISO;
	 */
	public String GetDateWeekISO() {
		String ret = "";
		if (mType!=ABMaterial.DATETIMEPICKER_TYPE_WEEK) {
			BA.Log("Use GetDate instead!");
			return "";
		}
		
		if (!(FutureText==null)) {
			try {	
				String s = (String) FutureText.getValue();
				mDateISO = (String) FutureTextISO.getValue();
				mDateISOZ = (String) FutureTextISOZ.getValue();
				String ss[] = Regex.Split(";", s);
				mDate = Long.parseLong(ss[1]);
				ret = mDateISO;
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
		if (ret.equals("")) {
			ret = "0;" + mDate + ";";
		}
		
		return ret;
	}
	
	@Override
	protected void CleanUp() {
		StringBuilder s = new StringBuilder();
		s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"').bootstrapMaterialDatePicker('destroy');");
		Page.ws.Eval(s.toString(), null);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("DateTimePicker CleanUp: " + ID);}
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
		s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"').bootstrapMaterialDatePicker('destroy');");
		Page.ws.Eval(s.toString(), null);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("DateTimePicker RemoveMe: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent");
	}	
	
	/*
	Input		Example				Description
	YYYY		2014				4 or 2 digit year
	YY			14					2 digit year
	Q			1..4				Quarter of year. Sets month to first month in quarter.
	M MM		1..12				Month number
	MMM MMMM	Jan..December		Month name in locale set by moment.locale()
	D DD		1..31				Day of month
	Do			1st..31st			Day of month with ordinal
	DDD DDDD	1..365				Day of year
	X			1410715640.579		Unix timestamp
	x			1410715640579		Unix ms timestamp

	gggg		2014				Locale 4 digit week year
	gg			14					Locale 2 digit week year
	w ww		1..53				Locale week of year
	e			1..7				Locale day of week
	ddd dddd	Mon...Sunday		Day name in locale set by moment.locale()
	GGGG		2014				ISO 4 digit week year
	GG			14					ISO 2 digit week year
	W WW		1..53				ISO week of year
	E			1..7				ISO day of week

	H HH		0..23				24 hour time
	h hh		1..12				12 hour time used with a A.
	a A			am pm				Post or ante meridiem
	m mm		0..59				Minutes
	s ss		0..59				Seconds
	S SS SSS	0..999				Fractional seconds
	Z ZZ		+12:00				Offset from UTC as +-HH:mm, +-HHmm, or Z
	*/
	
	protected String BuildJavaScript() {
		StringBuilder s = new StringBuilder();
		s.append("var date = new Date();");
		s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"').bootstrapMaterialDatePicker({");
		s.append("date : " + this.NeedsDate + ", ");
		s.append("time : " + this.NeedsTime + ", ");
		s.append("format : '" +  (this.ReturnDateFormat + " " + this.ReturnTimeFormat).trim() + "', ");
		s.append("minDate : null, ");
		s.append("maxDate : null, ");
		s.append("currentDate : null, ");
		s.append("lang : '" + this.Language + "', ");
		s.append("weekStart : " + this.FirstDayOfWeek + ", ");
		s.append("shortTime : " + this.ShortTime + ", ");
		s.append("'cancelText' : '" + this.CancelText + "', ");
		s.append("'okText' : '" + this.PickText + "', ");
		s.append("'clearText' : '" + this.ClearText + "', ");
		s.append("'todayText' : '" + this.TodayText + "', ");
		s.append("abmTheme: '" + Theme.ThemeName.toLowerCase() + "',");
		s.append("abmClickThrough: " + this.ClickThrough + ",");
		s.append("abmCloseColor: '" + ABMaterial.GetColorStr(Theme.PickerHeaderDayNameForeColor, Theme.PickerHeaderDayNameForeColorIntensity, "text") + "',");
		if (mType==ABMaterial.DATETIMEPICKER_TYPE_WEEK) {
			s.append("doweek: true,");
		} else {
			s.append("doweek: false,");
		}
		s.append("weekText: '" + this.WeekText + "'");
		s.append("});");
		
		s.append("var dtptemp=$('#' + $('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"').attr('data-dtp'));");
		if (this.CancelText.equals("")) {
			s.append("dtptemp.find('.dtp-btn-cancel').addClass('hide');");
		}
		if (this.PickText.equals("")) {
			s.append("dtptemp.find('.dtp-btn-ok').addClass('hide');");
		}
		if (this.ClearText.equals("")) {
			s.append("dtptemp.find('.dtp-btn-clear').addClass('hide');");
		}
		if (this.TodayText.equals("")) {
			s.append("dtptemp.find('.dtp-btn-today').addClass('hide');");
		}
		
		if (mDate!=0) {
			s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"').bootstrapMaterialDatePicker('setDate', '" + mDate + "');");
		}
		if (!mDateISO.equals("")) {
			s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"').bootstrapMaterialDatePicker('setDateISO', '" + mDateISO + "');");
		}
		if (MinimumDate!=0) {
			s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"').bootstrapMaterialDatePicker('setMinDate', '" + MinimumDate + "');");
		}
		if (!MinimumDateISO.equals("")) {
			s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"').bootstrapMaterialDatePicker('setMinDateISO', '" + MinimumDateISO + "');");
		}
		if (MaximumDate!=0) {
			s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"').bootstrapMaterialDatePicker('setMaxDate', '" + MaximumDate + "');");
		}
		if (!MaximumDateISO.equals("")) {
			s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"').bootstrapMaterialDatePicker('setMaxDateISO', '" + MaximumDateISO + "');");
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

		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent");			
		ThemeDateTimePicker in = Theme;
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
				
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		Params.Add(!mEnabled);
		Page.ws.RunFunction("SetDisabled", Params);
						
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
			Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"').bootstrapMaterialDatePicker('setDate', '" + mDate + "');", null);
		} else {
			if (!mDateISO.equals("") && IsDirty) {			
				Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"').bootstrapMaterialDatePicker('setDateISO', '" + mDateISO + "');", null);
			}
		}
		IsDirty=false;
		
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("DateTimePicker Refresh: " + ID);}
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
		ThemeDateTimePicker in = Theme;
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
				s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent-icon\" class=\" prefix\">" + Page.svgIconmap.getOrDefault(IconName, "") + "</i>\n");
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
			s.append("<input eventname=\"" + ID.toLowerCase() + "\" eventid=\""  + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" returnvalue=\"0\" " + placeholder + toolTip + disabled + "readonly=\"true\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"\" id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"\" type=\"text\" class=\"" + BuildClass() + selectable + "\" style=\"cursor: pointer" + NarrowStr + "\">\n");
		} else {
			s.append("<input eventname=\"" + ArrayName.toLowerCase() + "\" eventid=\""  + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" returnvalue=\"0\" " + placeholder + toolTip + disabled + "readonly=\"true\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"\" id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() +"\" type=\"text\" class=\"" + BuildClass() + selectable + "\" style=\"cursor: pointer" + NarrowStr + "\">\n");
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
		ThemeDateTimePicker l=Theme;		
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
	protected String BuildClassParent(ThemeDateTimePicker in) {			
		StringBuilder s = new StringBuilder();
		String rtl="";
		if (this.getRightToLeft()) {
			rtl = " abmrtl ";
		}
		s.append("input-field input-field" +  Theme.ThemeName.toLowerCase() + "dtp col s" + SizeSmall + " m" + SizeMedium + " l" + SizeLarge + " offset-s" + OffsetSmall + " offset-m" + OffsetMedium + " offset-l" + OffsetLarge + " " + mVisibility + " " + in.InputZDepth + " " + rtl + ABMaterial.GetColorStr(in.InputBackColor, in.InputBackColorIntensity, "") + " " + ABMaterial.GetColorStr(in.InputForeColor, in.InputForeColorIntensity, "text"));
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
	protected ABMComponent Clone() {
		ABMDateTimePicker c = new ABMDateTimePicker();
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
