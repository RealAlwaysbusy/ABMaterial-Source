package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeContainer implements java.io.Serializable{
	private static final long serialVersionUID = -8263773734192345908L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_TRANSPARENT;
	public String BackColor=MainColor;
	public String BackColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String ZDepth="";
	public String TitleColor="grey";
	public String TitleColorIntensity="darken-2";
	public double NoPrintOpacity=1.0;
	
	ThemeContainer() {
		
	}
	
	ThemeContainer(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;		
	}
	
	@Hide
	public ThemeContainer Clone() {
		ThemeContainer c = new ThemeContainer();
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;
		c.BackColor = BackColor;
		c.BackColorIntensity = BackColorIntensity;	
		c.ZDepth=ZDepth;
		c.TitleColor = TitleColor;
		c.TitleColorIntensity = TitleColorIntensity;
		c.NoPrintOpacity=NoPrintOpacity;
		return c;
	}
	
	
}
