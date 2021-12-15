package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeDateTimePicker implements java.io.Serializable {
	private static final long serialVersionUID = -2477335172505829432L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String PickerHeaderDayNameBackColor=MainColor;
	public String PickerHeaderDayNameBackColorIntensity=ABMaterial.INTENSITY_DARKEN2;
	public String PickerHeaderDayNameForeColor=ABMaterial.COLOR_WHITE;
	public String PickerHeaderDayNameForeColorIntensity=ABMaterial.INTENSITY_NORMAL;	
	public String PickerHeaderDateBackColor=MainColor;
	public String PickerHeaderDateBackColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String PickerHeaderDateMonthForeColor=ABMaterial.COLOR_WHITE;
	public String PickerHeaderDateMonthForeColorIntensity=ABMaterial.INTENSITY_NORMAL;	
	public String PickerHeaderDateDayForeColor=ABMaterial.COLOR_WHITE;
	public String PickerHeaderDateDayForeColorIntensity=ABMaterial.INTENSITY_NORMAL;	
	public String PickerHeaderDateYearForeColor=ABMaterial.COLOR_WHITE;
	public String PickerHeaderDateYearForeColorIntensity=ABMaterial.INTENSITY_NORMAL;	
	public String PickerHeaderDateArrowsColor=MainColor;
	public String PickerHeaderDateArrowsColorIntensity=ABMaterial.INTENSITY_DARKEN3;
	
	public String PickerCalendarClockBackColor=ABMaterial.COLOR_WHITE;
	public String PickerCalendarClockBackColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String PickerCalendarClockForeColor=ABMaterial.COLOR_BLACK;
	public String PickerCalendarClockForeColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String PickerCalendarClockButtonBackColor=ABMaterial.COLOR_TRANSPARENT;
	public String PickerCalendarClockButtonBackColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String PickerCalendarClockButtonForeColor=MainColor;
	public String PickerCalendarClockButtonForeColorIntensity=ABMaterial.INTENSITY_NORMAL;
		
	public String PickerCalendarDayForeColor=ABMaterial.COLOR_BLACK;
	public String PickerCalendarDayForeColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String PickerCalendarDayActiveBackColor=MainColor;
	public String PickerCalendarDayActiveBackColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String PickerCalendarDayActiveForeColor=ABMaterial.COLOR_WHITE;
	public String PickerCalendarDayActiveForeColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String PickerCalendarDayNamesForeColor=ABMaterial.COLOR_GREY;
	public String PickerCalendarDayNamesForeColorIntensity=ABMaterial.INTENSITY_DARKEN2;
		
	public String PickerClockHourMinForeColor=ABMaterial.COLOR_BLACK;
	public String PickerClockHourMinForeColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String PickerClockHourMinActiveBackColor=MainColor;
	public String PickerClockHourMinActiveBackColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String PickerClockHourMinActiveForeColor=ABMaterial.COLOR_WHITE;
	public String PickerClockHourMinActiveForeColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String PickerClockBackColor=ABMaterial.COLOR_GREY;
	public String PickerClockBackColorIntensity=ABMaterial.INTENSITY_LIGHTEN1;
	public String PickerClockCenterBackColor=ABMaterial.COLOR_GREY;
	public String PickerClockCenterBackColorIntensity=ABMaterial.INTENSITY_DARKEN1;	
	public String PickerClockActiveHandColor=MainColor;
	public String PickerClockActiveHandColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String PickerClockInActiveHandColor=ABMaterial.COLOR_GREY;
	public String PickerClockInActiveHandColorIntensity=ABMaterial.INTENSITY_DARKEN1;
		
	public String InputBackColor="transparent";
	public String InputBackColorIntensity="";
	public String InputForeColor="grey";
	public String InputForeColorIntensity="darken-2";
	public String InputFocusForeColor=MainColor;
	public String InputFocusForeColorIntensity="";
	public String InputFocusBackColor="transparent";
	public String InputFocusBackColorIntensity="";
	public int InputBorderRadiusPx=0;
	public String InputColor="black";
	public String InputColorIntensity="";	
	public String InputValidColor="green";
	public String InputValidColorIntensity="";
	public String InputInvalidColor="red";		
	public String InputInvalidColorIntensity="";
	public String InputPlaceholderColor=ABMaterial.COLOR_GREY;
	public String InputPlaceholderColorIntensity="";
	
	public String InputZDepth="";
		
	ThemeDateTimePicker() {
		
	}
	
	ThemeDateTimePicker(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;
		
		PickerHeaderDayNameBackColor=MainColor;
		PickerHeaderDateBackColor=MainColor;
		PickerHeaderDateArrowsColor=MainColor;
		PickerCalendarClockButtonForeColor=MainColor;
		PickerCalendarDayActiveBackColor=MainColor;
		PickerClockHourMinActiveBackColor=MainColor;
		PickerClockActiveHandColor=MainColor;
		InputFocusForeColor=MainColor;		
	}
	
	@Hide
	public ThemeDateTimePicker Clone() {
		ThemeDateTimePicker c = new ThemeDateTimePicker();
		c.ThemeName=ThemeName;
		
		c.MainColor = MainColor;
		c.PickerHeaderDayNameBackColor=PickerHeaderDayNameBackColor;
		c.PickerHeaderDayNameBackColorIntensity=PickerHeaderDayNameBackColorIntensity;
		c.PickerHeaderDayNameForeColor=PickerHeaderDayNameForeColor;
		c.PickerHeaderDayNameForeColorIntensity=PickerHeaderDayNameForeColorIntensity;	
		c.PickerHeaderDateBackColor=PickerHeaderDateBackColor;
		c.PickerHeaderDateBackColorIntensity=PickerHeaderDateBackColorIntensity;
		c.PickerHeaderDateMonthForeColor=PickerHeaderDateMonthForeColor;
		c.PickerHeaderDateMonthForeColorIntensity=PickerHeaderDateMonthForeColorIntensity;	
		c.PickerHeaderDateDayForeColor=PickerHeaderDateDayForeColor;
		c.PickerHeaderDateDayForeColorIntensity=PickerHeaderDateDayForeColorIntensity;	
		c.PickerHeaderDateYearForeColor=PickerHeaderDateYearForeColor;
		c.PickerHeaderDateYearForeColorIntensity=PickerHeaderDateYearForeColorIntensity;	
		c.PickerHeaderDateArrowsColor=PickerHeaderDateArrowsColor;
		c.PickerHeaderDateArrowsColorIntensity=PickerHeaderDateArrowsColorIntensity;
		
		c.PickerCalendarClockBackColor=PickerCalendarClockBackColor;
		c.PickerCalendarClockBackColorIntensity=PickerCalendarClockBackColorIntensity;
		c.PickerCalendarClockForeColor=PickerCalendarClockForeColor;
		c.PickerCalendarClockForeColorIntensity=PickerCalendarClockForeColorIntensity;
		c.PickerCalendarClockButtonBackColor=PickerCalendarClockButtonBackColor;
		c.PickerCalendarClockButtonBackColorIntensity=PickerCalendarClockButtonBackColorIntensity;
		c.PickerCalendarClockButtonForeColor=PickerCalendarClockButtonForeColor;
		c.PickerCalendarClockButtonForeColorIntensity=PickerCalendarClockButtonForeColorIntensity;
			
		c.PickerCalendarDayForeColor=PickerCalendarDayForeColor;
		c.PickerCalendarDayForeColorIntensity=PickerCalendarDayForeColorIntensity;
		c.PickerCalendarDayActiveBackColor=PickerCalendarDayActiveBackColor;
		c.PickerCalendarDayActiveBackColorIntensity=PickerCalendarDayActiveBackColorIntensity;
		c.PickerCalendarDayActiveForeColor=PickerCalendarDayActiveForeColor;
		c.PickerCalendarDayActiveForeColorIntensity=PickerCalendarDayActiveForeColorIntensity;
		c.PickerCalendarDayNamesForeColor=PickerCalendarDayNamesForeColor;
		c.PickerCalendarDayNamesForeColorIntensity=PickerCalendarDayNamesForeColorIntensity;
			
		c.PickerClockHourMinForeColor=PickerClockHourMinForeColor;
		c.PickerClockHourMinForeColorIntensity=PickerClockHourMinForeColorIntensity;
		c.PickerClockHourMinActiveBackColor=PickerClockHourMinActiveBackColor;
		c.PickerClockHourMinActiveBackColorIntensity=PickerClockHourMinActiveBackColorIntensity;
		c.PickerClockHourMinActiveForeColor=PickerClockHourMinActiveForeColor;
		c.PickerClockHourMinActiveForeColorIntensity=PickerClockHourMinActiveForeColorIntensity;
		c.PickerClockBackColor=PickerClockBackColor;
		c.PickerClockBackColorIntensity=PickerClockBackColorIntensity;
		c.PickerClockCenterBackColor=PickerClockCenterBackColor;
		c.PickerClockCenterBackColorIntensity=PickerClockCenterBackColorIntensity;
		c.PickerClockActiveHandColor=PickerClockActiveHandColor;
		c.PickerClockActiveHandColorIntensity=PickerClockActiveHandColorIntensity;
		c.PickerClockInActiveHandColor=PickerClockInActiveHandColor;
		c.PickerClockInActiveHandColorIntensity=PickerClockInActiveHandColorIntensity;		
		
		c.InputBackColor=InputBackColor;
		c.InputBackColorIntensity=InputBackColorIntensity;
		c.InputForeColor=InputForeColor;
		c.InputForeColorIntensity=InputForeColorIntensity;
		c.InputFocusForeColor=InputFocusForeColor;
		c.InputFocusForeColorIntensity=InputFocusForeColorIntensity;
		c.InputFocusBackColor=InputFocusBackColor;
		c.InputFocusBackColorIntensity=InputFocusBackColorIntensity;
		c.InputBorderRadiusPx=InputBorderRadiusPx;
		c.InputColor=InputColor;
		c.InputColorIntensity=InputColorIntensity;	
		c.InputValidColor=InputValidColor;
		c.InputValidColorIntensity=InputValidColorIntensity;
		c.InputInvalidColor=InputInvalidColor;		
		c.InputInvalidColorIntensity=InputInvalidColorIntensity;	
		c.InputPlaceholderColor=InputPlaceholderColor;
		c.InputPlaceholderColorIntensity=InputPlaceholderColorIntensity;
		c.InputZDepth=InputZDepth;
		return c;
	}
}
