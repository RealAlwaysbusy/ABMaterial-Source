package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeSwitch implements java.io.Serializable {
	private static final long serialVersionUID = 52294972088439990L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String LabelColor="black";
	public String LabelColorIntensity="";
	public String LabelFontSize="0.8rem";
	public String BarOnColor=MainColor;
	public String BarOnColorIntensity="lighten-2";
	public String SwitchOnColor=MainColor;
	public String SwitchOnColorIntensity="";
	public String BarOffColor="grey";
	public String BarOffColorIntensity="lighten-2";
	public String SwitchOffColor="grey";
	public String SwitchOffColorIntensity="lighten-2";
	public String TitleColor="grey";
	public String TitleColorIntensity="";
	public String ZDepth="";
	
	ThemeSwitch() {		
	}
	
	ThemeSwitch(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;		
		BarOnColor=MainColor;
		SwitchOnColor=MainColor;
	}
	
	@Hide
	public ThemeSwitch Clone() {
		ThemeSwitch c = new ThemeSwitch();
		
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;
		c.LabelColor=LabelColor;
		c.LabelColorIntensity=LabelColorIntensity;
		c.BarOnColor=BarOnColor;
		c.BarOnColorIntensity=BarOnColorIntensity;
		c.SwitchOnColor=SwitchOnColor;
		c.SwitchOnColorIntensity=SwitchOnColorIntensity;	
		c.BarOffColor=BarOffColor;
		c.BarOffColorIntensity=BarOffColorIntensity;
		c.SwitchOffColor=SwitchOffColor;
		c.SwitchOffColorIntensity=SwitchOffColorIntensity;
		c.LabelFontSize=LabelFontSize;
		c.TitleColor=TitleColor;
		c.TitleColorIntensity=TitleColorIntensity;
		c.ZDepth=ZDepth;
		return c;
	}
}
