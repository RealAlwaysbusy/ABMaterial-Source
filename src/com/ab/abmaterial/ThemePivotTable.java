package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemePivotTable implements java.io.Serializable {
	
	private static final long serialVersionUID = 8891937356290567621L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String DragDropAreaColor=MainColor;
	public String HeatMapColor=ABMaterial.COLOR_RED;
	public String BarChartColor=ABMaterial.COLOR_LIGHTGREEN;
	public String BarChartColorIntensity=ABMaterial.INTENSITY_LIGHTEN3;
	public String BackColor=ABMaterial.COLOR_TRANSPARENT;
	public String BackColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String MarkerColor=ABMaterial.COLOR_ORANGE;
	public String MarkerColorIntensity=ABMaterial.INTENSITY_NORMAL;
	
	ThemePivotTable() {
	
	}
	
	ThemePivotTable(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;		
		this.DragDropAreaColor=color;
	}
	
	@Hide
	public ThemePivotTable Clone() {
		ThemePivotTable c = new ThemePivotTable();
		
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;
		c.DragDropAreaColor=DragDropAreaColor;
		c.HeatMapColor=HeatMapColor;
		c.BarChartColor=BarChartColor;
		c.BarChartColorIntensity=BarChartColorIntensity;
		c.BackColor=BackColor;
		c.BackColorIntensity=BackColorIntensity;
		c.MarkerColor=MarkerColor;
		c.MarkerColorIntensity=MarkerColorIntensity;
		return c;
	}
}
