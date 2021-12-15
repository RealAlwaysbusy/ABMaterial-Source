package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeCustomCard implements java.io.Serializable {
	
	private static final long serialVersionUID = -1813578198224523938L;
	protected String ThemeName="default";		
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String BackColor="white";
	public String BackColorIntensity="";
	public String RevealColor="black";
	public String RevealColorIntensity="";
	public String ZDepth="";
	public String WavesEffect=ABMaterial.WAVESEFFECT_NONE;
	public boolean WavesCircle=false;
	
	ThemeCustomCard() {
		
	}
	
	ThemeCustomCard(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;
	}
	
	@Hide
	public ThemeCustomCard Clone() {
		ThemeCustomCard c = new ThemeCustomCard();
		c.MainColor = MainColor;
		c.ThemeName = ThemeName;
		c.BackColor=BackColor;
		c.BackColorIntensity=BackColorIntensity;
		c.RevealColor=RevealColor;
		c.RevealColorIntensity=RevealColorIntensity;
		c.ZDepth=ZDepth;
		c.WavesEffect=WavesEffect;
		c.WavesCircle=WavesCircle;
		return c;
	}
}

