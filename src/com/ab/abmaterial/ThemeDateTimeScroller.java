package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeDateTimeScroller implements java.io.Serializable {
	private static final long serialVersionUID = -6492843369819372879L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String ScrollerBackColor=ABMaterial.COLOR_WHITE;
	public String ScrollerBackColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String ScrollerForeColor=MainColor;
	public String ScrollerForeColorIntensity=ABMaterial.INTENSITY_NORMAL;	
	public String ScrollerValueColor=ABMaterial.COLOR_BLACK;
	public String ScrollerValueColorIntensity=ABMaterial.INTENSITY_NORMAL;	
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
		
	ThemeDateTimeScroller() {
		
	}
	
	ThemeDateTimeScroller(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;
		
		ScrollerForeColor=MainColor;
		InputFocusForeColor=MainColor;		
	}
	
	@Hide
	public ThemeDateTimeScroller Clone() {
		ThemeDateTimeScroller c = new ThemeDateTimeScroller();
		c.MainColor = MainColor;
		c.ThemeName=ThemeName;
		c.ScrollerBackColor = ScrollerBackColor;
		c.ScrollerBackColorIntensity = ScrollerBackColorIntensity;		
		c.ScrollerForeColor = ScrollerForeColor;
		c.ScrollerForeColorIntensity = ScrollerForeColorIntensity;	
		c.ScrollerValueColor = ScrollerValueColor;
		c.ScrollerValueColorIntensity = ScrollerValueColorIntensity;
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
