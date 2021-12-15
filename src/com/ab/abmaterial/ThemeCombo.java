package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeCombo implements java.io.Serializable {
	private static final long serialVersionUID = 3531801821131550362L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String BackColor="transparent";
	public String BackColorIntensity="";
	public String ContentBackColor="white";
	public String ContentBackColorIntensity="";
	public String ForeColor="grey";
	public String ForeColorIntensity="darken-2";
	public String FocusForeColor=MainColor;
	public String FocusForeColorIntensity="";
	public String FocusBackColor="transparent";
	public String FocusBackColorIntensity="";
	public int BorderRadiusPx=0;
	public String InputColor="black";
	public String InputColorIntensity="";	
	public String ValidColor="green";
	public String ValidColorIntensity="";
	public String InvalidColor="red";		
	public String InvalidColorIntensity="";	
	public String ItemDividerColor="grey";
	public String ItemDividerColorIntensity="";
	public String PlaceholderColor=ABMaterial.COLOR_GREY;
	public String PlaceholderColorIntensity="";
	public String ZDepth="";
	
	ThemeCombo() {
		
	}
	
	ThemeCombo(String themeName) {		
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;	
		FocusForeColor=MainColor;
	}
	
	@Hide
	public ThemeCombo Clone() {
		ThemeCombo cl = new ThemeCombo();
		cl.MainColor=MainColor;
		cl.ThemeName = ThemeName;
		cl.BackColor=BackColor;
		cl.BackColorIntensity=BackColorIntensity;
		cl.ForeColor=ForeColor;
		cl.ForeColorIntensity=ForeColorIntensity;
		cl.FocusForeColor=FocusForeColor;
		cl.FocusForeColorIntensity=FocusForeColorIntensity;
		cl.FocusBackColor=FocusBackColor;
		cl.FocusBackColorIntensity=FocusBackColorIntensity;
		cl.BorderRadiusPx=BorderRadiusPx;
		cl.InputColor=InputColor;
		cl.InputColorIntensity=InputColorIntensity;	
		cl.ValidColor=ValidColor;
		cl.ValidColorIntensity=ValidColorIntensity;
		cl.InvalidColor=InvalidColor;		
		cl.InvalidColorIntensity=InvalidColorIntensity;
		cl.ItemDividerColor=ItemDividerColor;
		cl.ItemDividerColorIntensity=ItemDividerColorIntensity;
		cl.ContentBackColor=ContentBackColor;
		cl.ContentBackColorIntensity=ContentBackColorIntensity;
		cl.PlaceholderColor=PlaceholderColor;
		cl.PlaceholderColorIntensity=PlaceholderColorIntensity;
		cl.ZDepth=ZDepth;	
		return cl;
	}
}
