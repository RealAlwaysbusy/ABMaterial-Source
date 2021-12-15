package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemePagination implements java.io.Serializable {
	private static final long serialVersionUID = 7237652490376364407L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String NavigationButtonsBackColor=MainColor;
	public String NavigationButtonsBackColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String ActivePageBackColor=ABMaterial.COLOR_WHITE;
	public String ActivePageBackColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String InActivePageBackColor=ABMaterial.COLOR_TRANSPARENT;
	public String InActivePageBackColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String ZDepth=ABMaterial.ZDEPTH_DEFAULT;
	public String NavigationButtonsForeColor=ABMaterial.COLOR_WHITE;
	public String NavigationButtonsForeColorIntensity=ABMaterial.INTENSITY_NORMAL;	
	public String ActivePageForeColor=MainColor;
	public String ActivePageForeColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String InActivePageForeColor=ABMaterial.COLOR_BLACK;
	public String InActivePageForeColorIntensity=ABMaterial.INTENSITY_NORMAL;
	
	ThemePagination() {
		
	}
	
	ThemePagination(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;			
		NavigationButtonsBackColor=MainColor;
		ActivePageForeColor=MainColor;
	}
	
	@Hide
	public ThemePagination Clone() {
		ThemePagination c = new ThemePagination();
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;
		c.NavigationButtonsBackColor = NavigationButtonsBackColor;
		c.NavigationButtonsBackColorIntensity = NavigationButtonsBackColorIntensity;
		c.ActivePageBackColor = ActivePageBackColor;
		c.ActivePageBackColorIntensity = ActivePageBackColorIntensity;
		c.InActivePageBackColor = InActivePageBackColor;
		c.InActivePageBackColorIntensity = InActivePageBackColorIntensity;
		c.ZDepth=ZDepth;
		c.NavigationButtonsForeColor = NavigationButtonsForeColor;
		c.NavigationButtonsForeColorIntensity = NavigationButtonsForeColorIntensity;
		c.ActivePageForeColor = ActivePageForeColor;
		c.ActivePageForeColorIntensity = ActivePageForeColorIntensity;
		c.InActivePageForeColor = InActivePageForeColor;
		c.InActivePageForeColorIntensity = InActivePageForeColorIntensity;
		return c;
	}
}
