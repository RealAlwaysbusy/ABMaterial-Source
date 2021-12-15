package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeCalendar implements java.io.Serializable{
	private static final long serialVersionUID = 1143910774096254413L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String ZDepth="z-depth-1";
	public String ActiveColor=MainColor;
	public String ActiveColorIntensity=ABMaterial.INTENSITY_NORMAL;
	
	ThemeCalendar() {
		
	}
	
	ThemeCalendar(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;		
		ActiveColor=MainColor;
	}
	
	@Hide
	public ThemeCalendar Clone() {
		ThemeCalendar c = new ThemeCalendar();
		c.MainColor = MainColor;
		c.ThemeName = ThemeName;
		c.ActiveColor=ActiveColor;
		c.ActiveColorIntensity=ActiveColorIntensity;
		c.ZDepth=ZDepth;
		return c;
	}
}
