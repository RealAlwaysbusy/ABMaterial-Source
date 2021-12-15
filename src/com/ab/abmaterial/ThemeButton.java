package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeButton implements java.io.Serializable{
	private static final long serialVersionUID = 7413910830962779120L;
	protected String ThemeName="default";		
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String BackColor=MainColor;
	public String BackColorIntensity="";
	public String ForeColor="white";
	public String ForeColorIntensity="";	
	public String WavesEffect="waves-effect";
	public boolean WavesCircle=false;
	public String MenuBackColor="white";
	public String MenuBackColorIntensity="";
	public String MenuForeColor="black";
	public String MenuForeColorIntensity="";
	public String MenuDividerColor=MainColor;
	public String MenuDividerColorIntensity="";
	public String MenuHoverBackColor="grey";
	public String MenuHoverBackColorIntensity=ABMaterial.INTENSITY_LIGHTEN3;
	public String ZDepth="";
	
	ThemeButton() {
		
	}
	
	ThemeButton(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;
		MenuDividerColor = MainColor;
		BackColor=MainColor;
	}
	
	@Hide
	public ThemeButton Clone() {
		ThemeButton b = new ThemeButton();
		
		b.MainColor=MainColor;
		b.ThemeName=ThemeName;
		b.BackColor=BackColor;
		b.BackColorIntensity=BackColorIntensity;
		b.ForeColor=ForeColor;
		b.ForeColorIntensity=ForeColorIntensity;	
		b.WavesEffect=WavesEffect;
		b.WavesCircle=WavesCircle;
		b.MenuBackColor=MenuBackColor;
		b.MenuBackColorIntensity=MenuBackColorIntensity;
		b.MenuForeColor=MenuForeColor;
		b.MenuForeColorIntensity=MenuForeColorIntensity;
		b.MenuDividerColor=MenuDividerColor;
		b.MenuDividerColorIntensity=MenuDividerColorIntensity;
		b.MenuHoverBackColor=MenuHoverBackColor;
		b.MenuHoverBackColorIntensity=MenuHoverBackColorIntensity;
		b.ZDepth=ZDepth;
		return b;
	}
}
