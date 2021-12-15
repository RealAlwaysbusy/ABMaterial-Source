package com.ab.abmaterial;

import java.util.ArrayList;
import java.util.List;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemePlanner implements java.io.Serializable {
	private static final long serialVersionUID = 6296905902389348152L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String BackColor="transparent";
	public String BackColorIntensity="";	
	public String BorderColor=MainColor;
	public String BorderColorIntensity="";
	public String HourMinutesBorderColor="grey";
	public String HourMinutesBorderColorIntensity="";
	public String HourMinutesFreeColor="grey";
	public String HourMinutesFreeColorIntensity=ABMaterial.INTENSITY_LIGHTEN4;
	public String HourMinutesFreeTextColor="black";
	public String HourMinutesFreeTextColorIntensity="";
	public String HourMinutesNotAvailableColor="red";
	public String HourMinutesNotAvailableColorIntensity="";
	public String HourMinutesNotAvailableTextColor="white";
	public String HourMinutesNotAvailableTextColorIntensity="";
	protected List<String> HourMinutesUsedColors = new ArrayList<String>();
	protected List<String> HourMinutesUsedColorsIntensity = new ArrayList<String>();
	protected List<String> HourMinutesUsedTextColors= new ArrayList<String>();
	protected List<String> HourMinutesUsedTextColorsIntensity = new ArrayList<String>();
	public String HourColor=MainColor;
	public String HourColorIntensity=ABMaterial.INTENSITY_LIGHTEN2;
	public String HourBottomBorderColor=MainColor;
	public String HourBottomBorderColorIntensity=ABMaterial.INTENSITY_LIGHTEN3;
	public String HourTextColor="white";
	public String HourTextColorIntensity="";
	public String HourAltColor=MainColor;
	public String HourAltColorIntensity=ABMaterial.INTENSITY_LIGHTEN1;
	public String HourAltTextColor="white";
	public String HourAltTextColorIntensity="";
	public String DayColor="white";
	public String DayColorIntensity="";
	public String DayBorderColor=MainColor;
	public String DayBorderColorIntensity="";
	public String DayTextColor="grey";
	public String DayTextColorIntensity="";	
	public String DayAltTextColor="black";
	public String DayAltTextColorIntensity="";
	
	public String MenuColor=MainColor;
	public String MenuColorIntensity=ABMaterial.INTENSITY_LIGHTEN2;
	public String MenuTextColor="white";
	public String MenuTextColorIntensity="";
	
	public String ZDepth="";
	
	ThemePlanner() {
		for (int i=0;i<20;i++) {
			HourMinutesUsedColors.add(MainColor);
			HourMinutesUsedColorsIntensity.add(i, "");
			HourMinutesUsedTextColors.add("white");
			HourMinutesUsedTextColorsIntensity.add(i, "");
		}
	}
	
	ThemePlanner(String themeName) {		
		for (int i=0;i<20;i++) {
			HourMinutesUsedColors.add(MainColor);
			HourMinutesUsedColorsIntensity.add(i, "");
			HourMinutesUsedTextColors.add("white");
			HourMinutesUsedTextColorsIntensity.add(i, "");
		}
		this.ThemeName = themeName;
	}
	
	/**
	 * index 0 to 19 
	 */
	public void HourMinutesUsedColors(int index, String hourMinutesUsedColor) {
		HourMinutesUsedColors.set(index,hourMinutesUsedColor);		
	}
	
	/**
	 * index 0 to 19 
	 */
	public void HourMinutesUsedColorsIntensity(int index, String hourMinutesUsedColorIntensity) {
		HourMinutesUsedColorsIntensity.set(index,hourMinutesUsedColorIntensity);		
	}
	
	/**
	 * index 0 to 19 
	 */
	public void HourMinutesUsedTextColors(int index, String hourMinutesUsedTextColor) {
		HourMinutesUsedTextColors.set(index,hourMinutesUsedTextColor);		
	}
	
	/**
	 * index 0 to 19 
	 */
	public void HourMinutesUsedTextColorsIntensity(int index, String hourMinutesUsedTextColorIntensity) {
		HourMinutesUsedTextColorsIntensity.set(index,hourMinutesUsedTextColorIntensity);		
	}
	
	public void Colorize(String color) {
		MainColor = color;	
		
		for (int i=0;i<20;i++) {
			HourMinutesUsedColors.set(i,MainColor);			
		}
		HourColor = MainColor;
		HourAltColor = MainColor;
		
		BorderColor=MainColor;
		DayBorderColor=MainColor;
		HourBottomBorderColor=MainColor;
		MenuColor=MainColor;
	}	
	
	@Hide
	public ThemePlanner Clone() {
		ThemePlanner l = new ThemePlanner();
		
		l.MainColor=MainColor;
		l.ThemeName=ThemeName;
		l.BackColor=BackColor;
		l.BackColorIntensity=BackColorIntensity;
		
		l.BorderColor=BorderColor;
		l.BorderColorIntensity=BorderColorIntensity;
		l.HourMinutesBorderColor=HourMinutesBorderColor;
		l.HourMinutesBorderColorIntensity=HourMinutesBorderColorIntensity;
		l.HourMinutesFreeColor=HourMinutesFreeColor;
		l.HourMinutesFreeColorIntensity=HourMinutesFreeColorIntensity;
		l.HourMinutesFreeTextColor=HourMinutesFreeTextColor;
		l.HourMinutesFreeTextColorIntensity=HourMinutesFreeTextColorIntensity;
		l.HourMinutesNotAvailableColor=HourMinutesNotAvailableColor;
		l.HourMinutesNotAvailableColorIntensity=HourMinutesNotAvailableColorIntensity;
		l.HourMinutesNotAvailableTextColor=HourMinutesNotAvailableTextColor;
		l.HourMinutesNotAvailableTextColorIntensity=HourMinutesNotAvailableTextColorIntensity;
		for (int i=0;i<20;i++) {
			l.HourMinutesUsedColors.add(HourMinutesUsedColors.get(i));
			l.HourMinutesUsedColorsIntensity.add(HourMinutesUsedColorsIntensity.get(i));
			l.HourMinutesUsedTextColors.add(HourMinutesUsedTextColors.get(i));
			l.HourMinutesUsedTextColorsIntensity.add(HourMinutesUsedTextColorsIntensity.get(i));
		}
		l.HourColor=HourColor;
		l.HourColorIntensity=HourColorIntensity;
		l.HourTextColor=HourTextColor;
		l.HourTextColorIntensity=HourTextColorIntensity;
		l.HourAltColor=HourAltColor;
		l.HourAltColorIntensity=HourAltColorIntensity;
		l.HourAltTextColor=HourAltTextColor;
		l.HourAltTextColorIntensity=HourAltTextColorIntensity;
		l.DayColor=DayColor;
		l.DayColorIntensity=DayColorIntensity;
		l.DayBorderColor=DayBorderColor;
		l.DayBorderColorIntensity=DayBorderColorIntensity;
		l.DayTextColor=DayTextColor;
		l.DayTextColorIntensity=DayTextColorIntensity;
		
		l.DayAltTextColor=DayAltTextColor;
		l.DayAltTextColorIntensity=DayAltTextColorIntensity;
		
		l.HourBottomBorderColor=HourBottomBorderColor;
		l.HourBottomBorderColorIntensity=HourBottomBorderColorIntensity;
		
		l.MenuColor=MenuColor;
		l.MenuColorIntensity=MenuColorIntensity;
		l.MenuTextColor=MenuTextColor;
		l.MenuTextColorIntensity=MenuTextColorIntensity;
		
		l.ZDepth=ZDepth;		
		return l;
	}
}
