package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeCard implements java.io.Serializable {
	private static final long serialVersionUID = -1068650222005507925L;
	protected String ThemeName="default";		
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String BackColor="white";
	public String BackColorIntensity="";
	public String TitleForeColor="black";
	public String TitleForeColorIntensity="";
	public String ContentForeColor="black";
	public String ContentForeColorIntensity="";
	public String ActionForeColor=MainColor;
	public String ActionForeColorIntensity="";	
	public String WavesEffect="waves-effect";
	public boolean WavesCircle=false;
	public String ZDepth="";
	
	ThemeCard() {
		
	}
	
	ThemeCard(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;	
		
		ActionForeColor=MainColor;
	}
	
	@Hide
	public ThemeCard Clone() {
		ThemeCard c = new ThemeCard();
		c.MainColor = MainColor;
		c.ThemeName = ThemeName;
		c.BackColor=BackColor;
		c.BackColorIntensity=BackColorIntensity;
		c.TitleForeColor=TitleForeColor;
		c.TitleForeColorIntensity=TitleForeColorIntensity;
		c.ContentForeColor=ContentForeColor;
		c.ContentForeColorIntensity=ContentForeColorIntensity;
		c.ActionForeColor=ActionForeColor;
		c.ActionForeColorIntensity=ActionForeColorIntensity;	
		c.WavesEffect=WavesEffect;
		c.WavesCircle=WavesCircle;
		c.ZDepth=ZDepth;
		return c;
	}
}
