package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeChip implements java.io.Serializable {	
	private static final long serialVersionUID = 4111335702037504230L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String BackColor=MainColor;
	public String BackColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String ZDepth=ABMaterial.ZDEPTH_DEFAULT;
	public String ForeColor=ABMaterial.COLOR_WHITE;
	public String ForeColorIntensity=ABMaterial.INTENSITY_NORMAL;	
	
	ThemeChip() {
		
	}
	
	ThemeChip(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;	
		BackColor=MainColor;
	}
	
	@Hide
	public ThemeChip Clone() {
		ThemeChip c = new ThemeChip();
		c.MainColor = MainColor;
		c.ThemeName=ThemeName;
		c.BackColor = BackColor;
		c.BackColorIntensity = BackColorIntensity;
		c.ZDepth=ZDepth;
		c.ForeColor = ForeColor;
		c.ForeColorIntensity = ForeColorIntensity;		
		return c;
	}
}
