package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemePatternLock implements java.io.Serializable {
	private static final long serialVersionUID = 5154299811256805841L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String BackColor="white";
	public String BackColorIntensity="";
	public String DotColor=MainColor;
	public String DotColorIntensity="";
	public String LineColor="grey";
	public String LineColorIntensity="";
	public String CorrectColor="green";
	public String CorrectColorIntensity="";
	public String WrongColor="red";
	public String WrongColorIntensity="";
	public String ZDepth=ABMaterial.ZDEPTH_1;
	
	ThemePatternLock() {
	
	}
	
	ThemePatternLock(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;			
		DotColor=MainColor;		
	}
	
	@Hide
	public ThemePatternLock Clone() {
		ThemePatternLock c = new ThemePatternLock();
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;
		c.BackColor=BackColor;
		c.BackColorIntensity=BackColorIntensity;
		c.DotColor=DotColor;
		c.DotColorIntensity=DotColorIntensity;
		c.LineColor=LineColor;
		c.LineColorIntensity=LineColorIntensity;
		c.CorrectColor=CorrectColor;
		c.CorrectColorIntensity=CorrectColorIntensity;
		c.WrongColor=WrongColor;
		c.WrongColorIntensity=WrongColorIntensity;
		c.ZDepth=ZDepth;
		return c;
	}
}
