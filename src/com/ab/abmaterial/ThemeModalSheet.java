package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeModalSheet implements java.io.Serializable {
	private static final long serialVersionUID = -1640584073834743679L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String ContentBackColor=ABMaterial.COLOR_TRANSPARENT;
	public String ContentBackColorIntensity="";
	public String HeaderBackColor=MainColor;
	public String HeaderBackColorIntensity="";
	public String FooterBackColor=MainColor;
	public String FooterBackColorIntensity="";
	
	ThemeModalSheet() {
	
	}
	
	ThemeModalSheet(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;
		FooterBackColor=MainColor;
		HeaderBackColor=MainColor;
	}
	
	@Hide
	public ThemeModalSheet Clone() {
		ThemeModalSheet c = new ThemeModalSheet();
		
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;
		c.HeaderBackColor=HeaderBackColor;
		c.HeaderBackColorIntensity=HeaderBackColorIntensity;
		c.ContentBackColor=ContentBackColor;
		c.ContentBackColorIntensity=ContentBackColorIntensity;
		c.FooterBackColor=FooterBackColor;
		c.FooterBackColorIntensity=FooterBackColorIntensity;
		return c;
	}
}
