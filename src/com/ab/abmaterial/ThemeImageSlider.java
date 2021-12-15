package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeImageSlider implements java.io.Serializable {
	private static final long serialVersionUID = 1957626945885033739L;
	protected String ThemeName="default";	
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String BackColor="transparent";
	public String BackColorIntensity="";
	public String ActiveBulletColor=MainColor;
	public String ActiveBulletColorIntensity="";
	public String TitleForeColor="white";
	public String TitleForeColorIntensity="";
	public String SubTitleForeColor="white";
	public String SubTitleForeColorIntensity="";
	public String InactiveBulletColor="grey";
	public String InactiveBulletColorIntensity="";
	public boolean FullWidth=true;
	public boolean Indicators=true;
	public int Height=400;
	public int Transition=500;
	public int Interval=6000;
	public String ZDepth="";
	
	ThemeImageSlider() {
		
	}
	
	ThemeImageSlider(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;
		ActiveBulletColorIntensity="";				
	}
	
	@Hide
	public ThemeImageSlider Clone() {
		ThemeImageSlider c = new ThemeImageSlider();
		
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;
		c.ActiveBulletColor=ActiveBulletColor;
		c.ActiveBulletColorIntensity=ActiveBulletColorIntensity;
		c.BackColor=BackColor;
		c.BackColorIntensity=BackColorIntensity;
		c.InactiveBulletColor=InactiveBulletColor;
		c.InactiveBulletColorIntensity=InactiveBulletColorIntensity;
		c.TitleForeColor=TitleForeColor;
		c.TitleForeColorIntensity=TitleForeColorIntensity;
		c.SubTitleForeColor=SubTitleForeColor;
		c.SubTitleForeColorIntensity=SubTitleForeColorIntensity;
		c.FullWidth=FullWidth;
		c.Indicators=Indicators;
		c.Height=Height;
		c.Transition=Transition;
		c.Interval=Interval;
		c.ZDepth=ZDepth;
		return c;
	}
}
