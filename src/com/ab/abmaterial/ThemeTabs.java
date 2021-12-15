package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeTabs implements java.io.Serializable {
	private static final long serialVersionUID = 4611552545748478361L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String BackColor="white";
	public String BackColorIntensity="";
	public String ForeColor="black";
	public String ForeColorIntensity="";
	public String HoverForeColor=MainColor;
	public String HoverForeColorIntensity="";	
	public String DisabledForeColor="grey";
	public String DisabledForeColorIntensity="";
	public String IndicatorColor=MainColor;
	public String IndicatorColorIntensity="";
	public String ZDepth="z-depth-1";	
	
	ThemeTabs() {
		
	}
	
	ThemeTabs(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;		
		HoverForeColor=MainColor;
		IndicatorColor=MainColor;
	}
	
	@Hide
	public ThemeTabs Clone() {
		ThemeTabs c = new ThemeTabs();
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;
		c.BackColor=BackColor;
		c.BackColorIntensity=BackColorIntensity;
		c.ForeColor=ForeColor;
		c.ForeColorIntensity=ForeColorIntensity;
		c.HoverForeColor=HoverForeColor;
		c.HoverForeColorIntensity=HoverForeColorIntensity;
		c.DisabledForeColor=DisabledForeColor;
		c.DisabledForeColorIntensity=DisabledForeColorIntensity;
		c.IndicatorColor=IndicatorColor;
		c.IndicatorColorIntensity=IndicatorColorIntensity;
		c.ZDepth=ZDepth;
		return c;
	}
}
