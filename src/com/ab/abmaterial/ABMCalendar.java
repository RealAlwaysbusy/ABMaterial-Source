package com.ab.abmaterial;

//import java.io.IOException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
//import anywheresoftware.b4j.object.WebSocket.JQueryElement;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;

@Author("Alain Bailleul")
@Events(values={"FetchData(dateStart as String, dateEnd as String)","DayClicked(date as String)","EventClicked(eventId as String)", "EventStartChanged(params as Map)","EventEndChanged(eventId as String, NewEnd as String)"}) //, "EventToAllDay(eventId as String, newStart as String)", "EventFromAllDay(eventId as String, newStart as String)"})
@ShortName("ABMCalendar")
public class ABMCalendar extends ABMComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1468499421202272835L;
	protected ThemeCalendar Theme=new ThemeCalendar();
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	public String DefaultDate="";
	public int FirstDayOfWeek=ABMaterial.FIRSTDAYOFWEEK_MONDAY;
	public boolean ShowWeekends=true;
	public String DefaultView=ABMaterial.CALENDAR_DEFAULTVIEW_WEEK;
	public String Language="en";
	public String MinTime="00:00:00";
	public String MaxTime="24:00:00";
	public String SlotDuration="00:30:00";
	protected transient Map<String,ABMCalendarEvent> mEvents = new LinkedHashMap<String,ABMCalendarEvent>();
	public boolean HasPreviousButton=true;
	public boolean HasNextButton=true;
	public boolean HasTodayButton=true;
	public boolean HasWeekButton=true;
	public boolean HasDayButton=true;
	public boolean HasMonthButton=true;
	public boolean Editable=false;
	public boolean DisplayEventTime=true;
	public boolean DisplayEventEnd=true;
	public boolean HasScrollBars=false;
		
	/**
	 * defaultDate should be in the ISO8601 format (e.g. 2015-11-18)
	 */
	public void Initialize(ABMPage page, String id, String defaultDate, int firstDayOfWeek, String language, String defaultView, String themeName) {
	//public void Initialize(ABMPage page, String id, String defaultDate, int firstDayOfWeek, String language, String defaultView, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_CALENDAR;
		this.DefaultDate=defaultDate;
		this.FirstDayOfWeek=firstDayOfWeek;
		this.Language=language;
		this.DefaultView=defaultView;
		
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Calendars.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Calendars.get(themeName.toLowerCase()).Clone();				
			}
		}
		IsInitialized=true;	
	}
	
	@Override
	protected void ResetTheme() {
		UseTheme(Theme.ThemeName);		
	}
	
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Calendars.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Calendars.get(themeName.toLowerCase()).Clone();				
			}
		}
	}
	
	@Override
	protected String RootID() {
		return ID.toLowerCase();
	}
	
	public void SetEvents(anywheresoftware.b4a.objects.collections.List events) {
		mEvents = new LinkedHashMap<String,ABMCalendarEvent>();
		StringBuilder eventString = new StringBuilder();
		eventString.append("[");
		for (int i=0;i<events.getSize();i++) {
			ABMCalendarEvent ev = (ABMCalendarEvent) events.Get(i);
			mEvents.put(ev.EventId.toLowerCase(), ev);
			eventString.append("{");
			eventString.append("\"title\": \"" + ABMaterial.HTMLConv().htmlEscape(ev.Title, Page.PageCharset) + "\",");
			eventString.append("\"start\": \"" + ev.StartTime + "\",");
			if (!ev.EndTime.equals("")) {
				eventString.append("\"end\": \"" + ev.EndTime + "\",");
			}
			if (ev.AllDay) {
				eventString.append("\"allDay\": " + ev.AllDay + ",");
			}
			if (!ev.SharedId.equals("")) {
				eventString.append("\"id\": \"" + ev.SharedId + "\",");
			}
			eventString.append("\"editable\": " + Editable + ",");
			eventString.append("\"overlap\": " + true + ",");
			eventString.append("\"color\": \"" + ABMaterial.GetColorStrMap(ev.BackgroundColor, ev.BackgroundColorIntensity) + "\",");
			eventString.append("\"textColor\": \"" + ABMaterial.GetColorStrMap(ev.TextColor, ev.TextColorIntensity) + "\",");			
			eventString.append("\"eventid\": \"" + ev.EventId + "\"");
			eventString.append("}");
			if (i<events.getSize()-1) {
				eventString.append(", ");
			}
		}
		eventString.append("]");
		ABMaterial.CALSetEvents(Page, ParentString + ID.toLowerCase(), eventString.toString());
	}

	/**
	 * newDate should be in the ISO8601 format (e.g. 2015-11-18)
	 */
	public void GotoDate(String newDate) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ID.toLowerCase());
		Params.Add(newDate);
		Page.ws.RunFunction("calgotodate", Params);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("Calendar Refresh: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		
	}
	
	
	public String GetTitle() {
		return ABMaterial.CALGetTitle(Page, ParentString + ID.toLowerCase());		
	}
		
	public void ChangeView(String newView) {
		ABMaterial.CALChangeView(Page, ParentString + ID.toLowerCase(), newView);
	}
		
	public void ChangeEventsEditable(boolean editable) {
		Editable = editable;
		ABMaterial.CALChangeEventsEditable(Page, ParentString + ID.toLowerCase(), editable);
	}
	
	public void AddEvent(ABMCalendarEvent event) {
		mEvents.put(event.EventId.toLowerCase(), event);
		StringBuilder eventString = new StringBuilder();
		eventString.append("{");
		eventString.append("\"title\": \"" + ABMaterial.HTMLConv().htmlEscape(event.Title, Page.PageCharset) + "\",");
		eventString.append("\"start\": \"" + event.StartTime + "\",");
		if (!event.EndTime.equals("")) {
			eventString.append("\"end\": \"" + event.EndTime + "\",");
		}
		if (event.AllDay) {
			eventString.append("\"allDay\": " + event.AllDay + ",");
		}
		if (!event.SharedId.equals("")) {
			eventString.append("\"id\": \"" + event.SharedId + "\",");
		}
		eventString.append("\"editable\": " + Editable + ",");
		eventString.append("\"overlap\": " + true + ",");
		eventString.append("\"color\": \"" + ABMaterial.GetColorStrMap(event.BackgroundColor, event.BackgroundColorIntensity) + "\",");
		eventString.append("\"textColor\": \"" + ABMaterial.GetColorStrMap(event.TextColor, event.TextColorIntensity) + "\",");			
		eventString.append("\"eventid\": \"" + event.EventId + "\"");
		eventString.append("}");
		ABMaterial.CALAddEvent(Page, ParentString + ID.toLowerCase(), eventString.toString());
	}
	
	public void RemoveEvent(String eventId) {
		ABMaterial.CALRemoveEvent(Page, ParentString + ID.toLowerCase(), eventId);
	}
	
	public void UpdateEvent(ABMCalendarEvent event) {
		mEvents.put(event.EventId.toLowerCase(), event);
		StringBuilder eventString = new StringBuilder();
		eventString.append("{");
		eventString.append("\"title\": \"" + ABMaterial.HTMLConv().htmlEscape(event.Title, Page.PageCharset) + "\",");
		eventString.append("\"start\": \"" + event.StartTime + "\",");
		if (!event.EndTime.equals("")) {
			eventString.append("\"end\": \"" + event.EndTime + "\",");
		}
		if (event.AllDay) {
			eventString.append("\"allDay\": " + event.AllDay + ",");
		}
		if (!event.SharedId.equals("")) {
			eventString.append("\"id\": \"" + event.SharedId + "\",");
		}
		eventString.append("\"editable\": " + Editable + ",");
		eventString.append("\"overlap\": " + true + ",");
		eventString.append("\"color\": \"" + ABMaterial.GetColorStrMap(event.BackgroundColor, event.BackgroundColorIntensity) + "\",");
		eventString.append("\"textColor\": \"" + ABMaterial.GetColorStrMap(event.TextColor, event.TextColorIntensity) + "\",");			
		eventString.append("\"eventid\": \"" + event.EventId + "\"");
		eventString.append("}");
		ABMaterial.CALUpdateEvent(Page, ParentString + ID.toLowerCase(), eventString.toString());
	}
	
	public ABMCalendarEvent GetEvent(String eventId) {
		return mEvents.getOrDefault(eventId.toLowerCase(), null);
	}
	
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName ="";
	}
	
	public void RefetchData() {
		ABMaterial.CALRefetchData(Page, ParentString + ID.toLowerCase());
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
		ABMaterial.RemoveHTMLParent(Page, ParentString + ID.toLowerCase());
	}
	
	@Override
	protected void FirstRun() {
		Page.ws.Eval(BuildJavaScript(), null);
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ID.toLowerCase());		
		Page.ws.RunFunction("inittooltipped", Params);		
		super.FirstRun();
	}
	
	

	protected String BuildJavaScript() {
		StringBuilder s = new StringBuilder();
		String id = ParentString + ID.toLowerCase();
	
		s.append("$.stylesheet('#" + id + " h2', {'font-size': '1.2rem','text-transform': 'uppercase','line-height': '35px'});");
		s.append("$.stylesheet('#" + id + " .fc-day-header', {'text-transform': 'uppercase','font-weight': '400','font-size': '0.8rem'});");		
		s.append("$.stylesheet('#" + id + " .fc-state-active', {'color': '" + ABMaterial.GetColorStrMap(Theme.ActiveColor, Theme.ActiveColorIntensity) + " !important'}); ");
		s.append("$.stylesheet('#" + id + " .fc-state-down', {'color': '" + ABMaterial.GetColorStrMap(Theme.ActiveColor, Theme.ActiveColorIntensity) + " !important'}); ");
		s.append("$.stylesheet('#" + id + " .fc-today', {'color': '" + ABMaterial.GetColorStrMap(Theme.ActiveColor, Theme.ActiveColorIntensity) + " !important','background-color': 'transparent !important'});");
		s.append("$.stylesheet('#" + id + " .fc-widget-content', {'font-size': '0.8rem'});");
		
		s.append("$('#" + ParentString + ID.toLowerCase() + "').fullCalendar({");
		s.append("header: {");
		String Left="";
		if (HasPreviousButton) {
			Left+="prev,";
		}
		if (HasNextButton) {
			Left+="next,";
		}
		if (HasTodayButton) {
			Left+="today,";
		}
		if (!Left.equals("")) {
			Left= Left.substring(0,Left.length()-1);
			s.append("left: '" + Left + "',");
		}
		s.append("center: 'title',");
		String Right="";
		if (HasMonthButton) {
			Right+="month,";
		}
		if (HasWeekButton) {
			Right+="agendaWeek,";
		}
		if (HasDayButton) {
			Right+="agendaDay,";
		}
		if (!Right.equals("")) {
			Right= Right.substring(0,Right.length()-1);
			s.append("right: '" + Right + "'");
		}       	 			
		s.append("},");
		s.append("defaultDate: '" + DefaultDate + "',");
		s.append("editable: true,");
		s.append("firstDay: " + FirstDayOfWeek + ",");
		s.append("defaultView: '" + DefaultView + "',");
		if (!HasScrollBars ) {
			s.append("contentHeight: 'auto',");
		}
		s.append("eventLimit: false,"); 
		s.append("displayEventTime: " + DisplayEventTime + ",");
		s.append("displayEventEnd: " + DisplayEventEnd + ",");
		s.append("slotDuration: '" + SlotDuration + "',");
		s.append("lang: '" + Language + "',");
		s.append("weekends: " + ShowWeekends + ",");
		s.append("minTime: '" + MinTime + "',");
		s.append("maxTime: '" + MaxTime + "',");
		s.append("dayClick: function(date, jsEvent, view) {");
		s.append("  b4j_raiseEvent('page_parseevent', {'eventname': '" + ParentString + ID.toLowerCase() + "' + '_dayclicked','eventparams':'date','date':date.format()});");
		s.append("},");
		s.append("eventClick: function(event, jsEvent, view) {");
		s.append("currentClickedEvent=event;");
		s.append("  b4j_raiseEvent('page_parseevent', {'eventname': '" + ParentString + ID.toLowerCase() + "' + '_eventclicked','eventparams':'eventid','eventid':event.eventid});");
		s.append("},");
		s.append("events: function(start, end, timezone, callback) {");
		s.append("  b4j_raiseEvent('page_parseevent', {'eventname': '" + ParentString + ID.toLowerCase() + "' + '_fetchdata','eventparams':'datestart,dateend','datestart':start.format(),'dateend':end.format()});");
		s.append("},");	  
		s.append("eventResize: function(event, delta, revertFunc) {");
		s.append("  b4j_raiseEvent('page_parseevent', {'eventname': '" + ParentString + ID.toLowerCase() + "' + '_eventendchanged','eventparams':'eventid,newend','eventid':event.eventid,'newend':event.end.format()});");
		s.append("},");
		s.append("eventDrop: function(event, delta, revertFunc) {");
		s.append("  if (event.allDay) {");
		s.append("  } else {");
		s.append("     if (event.end===null) {");
		s.append("     } else {");
		s.append("        b4j_raiseEvent('page_parseevent', {'eventname': '" + ParentString + ID.toLowerCase() + "' + '_eventstartchanged','eventparams':'eventid,newstart,newend,allday','eventid':event.eventid,'newstart':event.start.format(),'newend':event.end.format(), 'allday': event.allDay});");
		s.append("     }");
		s.append("  }");
		s.append("},");
		s.append("});\n");
			
		return s.toString();
	}
	
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {	
		super.Refresh();
		JQueryElement j = Page.ws.GetElementById(ParentString + ID.toLowerCase());
		j.SetProp("class", BuildClass());	
		
		if (!ToolTipText.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-position", ToolTipPosition);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-delay", "" + ToolTipDelay);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-tooltip", ToolTipText);
		}
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Calendar Refresh : " + ID);}
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
		
		String toolTip="";
		if (!ToolTipText.equals("")) {
			toolTip=" data-position=\"" + ToolTipPosition + "\" data-delay=\"" + ToolTipDelay + "\" data-tooltip=\"" + ToolTipText + "\" "; 
		}
		s.append("<div class=\"fc full-calendar\">\n");
		s.append("<div " + toolTip + " id=\"" + ParentString + ID.toLowerCase() + "\" class=\"");
		s.append(BuildClass());	
		s.append("\" style=\"cursor: pointer\">");
		s.append("</div>\n");
		s.append("</div>\n");
		IsBuild=true;
		return s.toString();
	}
	
	@Hide
	protected String BuildClass() {		
		StringBuilder s = new StringBuilder();
		s.append(super.BuildExtraClasses());
		ThemeCalendar l = Theme;
		if (!ToolTipText.equals("")) {
			s.append("tooltipped ");
		}		
		s.append(mVisibility + " ");	
		s.append(l.ZDepth + " ");
		s.append(ABMaterial.GetColorStr(ABMaterial.COLOR_WHITE, ABMaterial.INTENSITY_NORMAL, "") + " " + ABMaterial.GetColorStr(ABMaterial.COLOR_BLACK, ABMaterial.INTENSITY_NORMAL, "text") + " ");
		s.append("calnoselect ");
		s.append(mIsPrintableClass);
		s.append(mIsOnlyForPrintClass);
		return s.toString(); 
	}
	
	@Hide
	protected String BuildBody() {
		StringBuilder s = new StringBuilder();			
		return s.toString();
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMCalendar c = new ABMCalendar();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;		
		c.Type = Type;
		c.mVisibility = mVisibility;	
		c.ToolTipDelay = ToolTipDelay;
		c.ToolTipPosition = ToolTipPosition;
		c.ToolTipText = ToolTipText;		
		c.DefaultDate=DefaultDate;
		c.Theme=Theme.Clone();
		c.FirstDayOfWeek=FirstDayOfWeek;
		c.ShowWeekends=ShowWeekends;
		c.DefaultView=DefaultView;
		c.Language=Language;
		c.MinTime=MinTime;
		c.MaxTime=MaxTime;
		return c;
	}
	

}
