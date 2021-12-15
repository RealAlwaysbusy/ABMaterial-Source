package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeBadge implements java.io.Serializable {
	private static final long serialVersionUID = 4518579833041605896L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String NewBackColor=MainColor;
	public String NewBackColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String ZDepth=ABMaterial.ZDEPTH_DEFAULT;
	public String NewForeColor=ABMaterial.COLOR_WHITE;
	public String NewForeColorIntensity=ABMaterial.INTENSITY_NORMAL;	
	public String ForeColor=ABMaterial.COLOR_BLACK;
	public String ForeColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String NewText="new";
	
	ThemeBadge() {
		
	}
	
	ThemeBadge(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;
		
		NewBackColor=MainColor;
	}
	
	@Hide
	public ThemeBadge Clone() {
		ThemeBadge c = new ThemeBadge();
		c.MainColor = MainColor;
		c.ThemeName=ThemeName;
		c.NewBackColor = NewBackColor;
		c.NewBackColorIntensity = NewBackColorIntensity;
		c.ZDepth=ZDepth;
		c.ForeColor = ForeColor;
		c.ForeColorIntensity = ForeColorIntensity;	
		c.NewForeColor = NewForeColor;
		c.NewForeColorIntensity = NewForeColorIntensity;
		c.NewText=NewText;
		return c;
	}
}
