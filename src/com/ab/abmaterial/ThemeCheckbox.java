package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeCheckbox implements java.io.Serializable {
	private static final long serialVersionUID = 7759822751456207668L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String LabelColor="black";
	public String LabelColorIntensity="";
	public String CheckedColor=MainColor;
	public String CheckedColorIntensity="";
	public String CheckedFilledForeColor="white";
	public String CheckedFilledForeColorIntensity="";
	public String BorderColor="grey";
	public String BorderColorIntensity="lighten-1";
	
	public String DisabledLabelColor="black";
	public String DisabledLabelColorIntensity="";
	public String DisabledCheckedColor="grey";
	public String DisabledCheckedColorIntensity="lighten-1";
	public String DisabledCheckedFilledForeColor="grey";
	public String DisabledCheckedFilledForeColorIntensity="lighten-3";
	public String DisabledBorderColor="grey";
	public String DisabledBorderColorIntensity="lighten-1";
	public String ZDepth="";
	
	public String LineHeight="25px";
	public String FontSize="1rem";
	public int BoxTopPx=0;
	
	ThemeCheckbox() {
		
	}
	
	ThemeCheckbox(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;	
		CheckedColor=MainColor;
	}
	
	@Hide
	public ThemeCheckbox Clone() {
		ThemeCheckbox c = new ThemeCheckbox();
		
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;
		c.LabelColor=LabelColor;
		c.LabelColorIntensity=LabelColorIntensity;
		c.CheckedColor=CheckedColor;
		c.CheckedColorIntensity=CheckedColorIntensity;
		c.CheckedFilledForeColor=CheckedFilledForeColor;
		c.CheckedFilledForeColorIntensity=CheckedFilledForeColorIntensity;
		c.BorderColor=BorderColor;
		c.BorderColorIntensity=BorderColorIntensity;
		
		c.DisabledLabelColor=DisabledLabelColor;
		c.DisabledLabelColorIntensity=DisabledLabelColorIntensity;
		c.DisabledCheckedColor=DisabledCheckedColor;
		c.DisabledCheckedColorIntensity=DisabledCheckedColorIntensity;
		c.DisabledCheckedFilledForeColor=DisabledCheckedFilledForeColor;
		c.DisabledCheckedFilledForeColorIntensity=DisabledCheckedFilledForeColorIntensity;
		c.DisabledBorderColor=DisabledBorderColor;
		c.DisabledBorderColorIntensity=DisabledBorderColorIntensity;
		c.ZDepth=ZDepth;
		
		c.LineHeight=LineHeight;
		c.FontSize=FontSize;
		c.BoxTopPx=BoxTopPx;
		return c;
	}
}
