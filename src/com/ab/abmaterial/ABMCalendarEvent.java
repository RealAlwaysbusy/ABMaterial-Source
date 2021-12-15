package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")
@ShortName("ABMCalendarEvent")
public class ABMCalendarEvent implements java.io.Serializable {
	private static final long serialVersionUID = -6334212252875931446L;
	public String SharedId="";
	public String EventId="";
	public String Title="";
	public boolean AllDay=false;
	public String StartTime="";
	public String EndTime="";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String BackgroundColor=MainColor;
	public String BackgroundColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String TextColor=ABMaterial.COLOR_WHITE;
	public String TextColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public boolean IsInitialized=false;
	
	/**
	 * 
	 * Start and End should be in the ISO8601 format (e.g. 2015-11-18 or 2015-11-18T16:00:00)
	 */
	public void Initialize(String eventId, String title, String startTime) {
		this.EventId=eventId;
		this.Title=title;
		this.StartTime=startTime;
		IsInitialized=true;
	}
	
	@Hide
	public ABMCalendarEvent Clone() {
		ABMCalendarEvent c = new ABMCalendarEvent();
		c.SharedId=SharedId;
		c.EventId=EventId;
		c.Title=Title;
		c.AllDay=AllDay;
		c.StartTime=StartTime;
		c.EndTime=EndTime;
		c.BackgroundColor=BackgroundColor;
		c.BackgroundColorIntensity=BackgroundColorIntensity;
		c.TextColor=TextColor;
		c.TextColorIntensity=TextColorIntensity;
		return c;
	}
}
