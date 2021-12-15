package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeSlider implements java.io.Serializable {
	private static final long serialVersionUID = 3808377408363409410L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String ButtonColor=MainColor;
	public String ButtonColorIntensity="";
	public String ButtonTextColor="white";
	public String ButtonTextColorIntensity="";
	public String LineColor="grey";
	public String LineColorIntensity="";
	public String ConnectLineColor=MainColor;
	public String ConnectLineColorIntensity="";
	
	ThemeSlider() {
	
	}
	
	ThemeSlider(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;		
		ButtonColor=MainColor;
		ConnectLineColor=MainColor;
	}
	
	@Hide
	public ThemeSlider Clone() {
		ThemeSlider c = new ThemeSlider();
		
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;
		c.ButtonColor=ButtonColor;
		c.ButtonColorIntensity=ButtonColorIntensity;
		c.ButtonTextColor=ButtonTextColor;
		c.ButtonTextColorIntensity=ButtonTextColorIntensity;
		c.LineColor=LineColor;
		c.LineColorIntensity=LineColorIntensity;
		c.ConnectLineColor=ConnectLineColor;
		c.ConnectLineColorIntensity=ConnectLineColorIntensity;
		return c;
	}
}
