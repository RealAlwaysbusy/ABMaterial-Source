package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeFileManager implements java.io.Serializable {
	private static final long serialVersionUID = -5942730115677945809L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String ZDepth=ABMaterial.ZDEPTH_1;
	
	public String BackgroundColor=ABMaterial.COLOR_WHITE;
	public String BackgroundColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String HeaderFooterBackgroundColor=MainColor;
	public String HeaderFooterBackgroundColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String HeaderFooterForeColor=ABMaterial.COLOR_WHITE;
	public String HeaderFooterForeColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String SideBarBackgroundColor=ABMaterial.COLOR_GREY;
	public String SideBarBackgroundColorIntensity=ABMaterial.INTENSITY_LIGHTEN5;
	public String SideBarBorderColor=ABMaterial.COLOR_GREY;
	public String SideBarBorderColorIntensity=ABMaterial.INTENSITY_LIGHTEN4;
	public String SideBarForeColor=ABMaterial.COLOR_GREY;
	public String SideBarForeColorIntensity=ABMaterial.INTENSITY_DARKEN3;
	public String SideBarActiveForeColor=MainColor;
	public String SideBarActiveForeColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String FileActiveForeColor=MainColor;
	public String FileActiveForeColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String FileActiveCopyBackgroundColor=ABMaterial.COLOR_GREY;
	public String FileActiveCopyBackgroundColorIntensity=ABMaterial.INTENSITY_LIGHTEN5;
	public String SearchPlaceholderColor=ABMaterial.COLOR_GREY;
	public String SearchPlaceholderColorIntensity=ABMaterial.INTENSITY_LIGHTEN5;
	public String SearchForeColor=ABMaterial.COLOR_WHITE;
	public String SearchForeColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String ListHeaderBorderColor=ABMaterial.COLOR_GREY;
	public String ListHeaderBorderColorIntensity=ABMaterial.INTENSITY_LIGHTEN3;
	public String ListHeaderForeColor=MainColor;
	public String ListHeaderForeColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String ListItemDefaultForeColor=ABMaterial.COLOR_GREY;
	public String ListItemDefaultForeColorIntensity=ABMaterial.INTENSITY_DARKEN3;
	public String ToolTipBackgroundColor=ABMaterial.COLOR_BLACK;
	public String ToolTipBackgroundColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String ToolTipForeColor=ABMaterial.COLOR_WHITE;
	public String ToolTipForeColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String DragZoneForeColor=ABMaterial.COLOR_GREY;
	public String DragZoneForeColorIntensity=ABMaterial.INTENSITY_LIGHTEN4;	
		
	ThemeFileManager() {
	
	}
	
	ThemeFileManager(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;			
		HeaderFooterBackgroundColor = MainColor;
		SideBarActiveForeColor = MainColor;
		FileActiveForeColor = MainColor;
		ListHeaderForeColor = MainColor;
	}
	
	@Hide
	public ThemeFileManager Clone() {
		ThemeFileManager c = new ThemeFileManager();
		
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;		
		c.ZDepth = ZDepth;
		
		c.BackgroundColor=BackgroundColor;
		c.BackgroundColorIntensity=BackgroundColorIntensity;
		c.HeaderFooterBackgroundColor=HeaderFooterBackgroundColor;
		c.HeaderFooterBackgroundColorIntensity=HeaderFooterBackgroundColorIntensity;
		c.HeaderFooterForeColor=HeaderFooterForeColor;
		c.HeaderFooterForeColorIntensity=HeaderFooterForeColorIntensity;
		c.SideBarBackgroundColor=SideBarBackgroundColor;
		c.SideBarBackgroundColorIntensity=SideBarBackgroundColorIntensity;
		c.SideBarBorderColor=SideBarBorderColor;
		c.SideBarBorderColorIntensity=SideBarBorderColorIntensity;
		c.SideBarForeColor=SideBarForeColor;
		c.SideBarForeColorIntensity=SideBarForeColorIntensity;
		c.SideBarActiveForeColor=SideBarActiveForeColor;
		c.SideBarActiveForeColorIntensity=SideBarActiveForeColorIntensity;
		c.FileActiveForeColor=FileActiveForeColor;
		c.FileActiveForeColorIntensity=FileActiveForeColorIntensity;
		c.FileActiveCopyBackgroundColor=FileActiveCopyBackgroundColor;
		c.FileActiveCopyBackgroundColorIntensity=FileActiveCopyBackgroundColorIntensity;
		c.SearchPlaceholderColor=SearchPlaceholderColor;
		c.SearchPlaceholderColorIntensity=SearchPlaceholderColorIntensity;
		c.SearchForeColor=SearchForeColor;
		c.SearchForeColorIntensity=SearchForeColorIntensity;
		c.ListHeaderBorderColor=ListHeaderBorderColor;
		c.ListHeaderBorderColorIntensity=ListHeaderBorderColorIntensity;
		c.ListHeaderForeColor=ListHeaderForeColor;
		c.ListHeaderForeColorIntensity=ListHeaderForeColorIntensity;
		c.ListItemDefaultForeColor=ListItemDefaultForeColor;
		c.ListItemDefaultForeColorIntensity=ListItemDefaultForeColorIntensity;
		c.ToolTipBackgroundColor=ToolTipBackgroundColor;
		c.ToolTipBackgroundColorIntensity=ToolTipBackgroundColorIntensity;
		c.ToolTipForeColor=ToolTipForeColor;
		c.ToolTipForeColorIntensity=ToolTipForeColorIntensity;
		c.DragZoneForeColor=DragZoneForeColor;
		c.DragZoneForeColorIntensity=DragZoneForeColorIntensity;
		
		return c;
	}
}

