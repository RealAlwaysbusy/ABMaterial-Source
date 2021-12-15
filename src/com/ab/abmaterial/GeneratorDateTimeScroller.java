package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class GeneratorDateTimeScroller {
	public String Mode=ABMaterial.DATETIMESCROLLER_MODE_SCROLLER;
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
	public String SQLSaveDateTimeFormat = "";
}
