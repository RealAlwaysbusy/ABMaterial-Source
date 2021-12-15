package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeRow implements java.io.Serializable {
	private static final long serialVersionUID = 1391241400824222546L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String BackColor=ABMaterial.COLOR_TRANSPARENT;
	public String BackColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String ZDepth="";
	public boolean VerticalAlign=false;	
	public boolean Clickable=false;
	public String ClickableWavesEffect=""; //"waves-effect";
	public boolean ClickableWavesCircle=false;
	public String BorderColor=ABMaterial.COLOR_TRANSPARENT;
	public String BorderColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public int BorderWidth=0;
	public int BorderRadiusPx=0;
	public int BorderRadiusBottomLeftPx=0;
	public int BorderRadiusBottomRightPx=0;
	public int BorderRadiusTopLeftPx=0;
	public int BorderRadiusTopRightPx=0;
	public String BorderStyle="";
	
	ThemeRow() {
		
	}
	
	ThemeRow(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;		
	}
	
	@Hide
	public ThemeRow Clone() {
		ThemeRow c = new ThemeRow();
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;
		c.BackColor = BackColor;
		c.BackColorIntensity = BackColorIntensity;
		c.ZDepth=ZDepth;
		c.VerticalAlign=VerticalAlign;		
		c.Clickable=Clickable;
		c.ClickableWavesEffect=ClickableWavesEffect;
		c.ClickableWavesCircle=ClickableWavesCircle;
		c.BorderColor = BorderColor;
		c.BorderColorIntensity = BorderColorIntensity;
		c.BorderWidth=BorderWidth;
		c.BorderRadiusPx=BorderRadiusPx;
		c.BorderRadiusBottomLeftPx=BorderRadiusBottomLeftPx;
		c.BorderRadiusBottomRightPx=BorderRadiusBottomRightPx;
		c.BorderRadiusTopLeftPx=BorderRadiusTopLeftPx;
		c.BorderRadiusTopRightPx=BorderRadiusTopRightPx;
		c.BorderStyle=BorderStyle;
		return c;
	}
}
