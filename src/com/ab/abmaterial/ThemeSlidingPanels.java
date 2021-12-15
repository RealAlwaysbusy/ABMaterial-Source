package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeSlidingPanels implements java.io.Serializable {
	private static final long serialVersionUID = -7088011123027185253L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String TitleForeColor="black";
	public String TitleForeColorIntensity="";
	public String DescriptionForeColor="black";
	public String DescriptionForeColorIntensity="";
	
	ThemeSlidingPanels() {
		
	}
	
	ThemeSlidingPanels(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;						
	}
	
	@Hide
	public ThemeSlidingPanels Clone() {
		ThemeSlidingPanels l = new ThemeSlidingPanels();
		
		l.MainColor=MainColor;
		l.ThemeName=ThemeName;
		l.TitleForeColor=TitleForeColor;
		l.TitleForeColorIntensity=TitleForeColorIntensity;
		l.DescriptionForeColor=DescriptionForeColor;
		l.DescriptionForeColorIntensity=DescriptionForeColorIntensity;
		
		return l;
	}

}
