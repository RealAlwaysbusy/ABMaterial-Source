package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class GeneratorDateTimePicker {
	public String ReturnDateFormat="YYYY-MM-DD";
	public String ReturnTimeFormat="HH:mm";
	public long MinimumDate=0;
	public long MaximumDate=0;
	public String Language="en";
	public int FirstDayOfWeek=0;
	public boolean ShortTime=false;
	public String PickText = "OK";
	public String CancelText = "Cancel";
	public String TodayText = "Today";	
	public String SQLSaveDateTimeFormat = "";
}
