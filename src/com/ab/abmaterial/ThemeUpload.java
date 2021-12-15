package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeUpload implements java.io.Serializable {
	private static final long serialVersionUID = 5224845295448129497L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String BackColor=MainColor;
	public String BackColorIntensity=ABMaterial.INTENSITY_DARKEN2;
	public String DropZoneBackColor=MainColor;
	public String DropZoneBackColorIntensity=ABMaterial.INTENSITY_DARKEN3;
	public String ForeColor=MainColor;
	public String ForeColorIntensity=ABMaterial.INTENSITY_DARKEN1;
	public String ButtonBackColor=MainColor;
	public String ButtonBackColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String ButtonForeColor=ABMaterial.COLOR_WHITE;
	public String ButtonForeColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String ButtonHoverBackColor=MainColor;
	public String ButtonHoverBackColorIntensity=ABMaterial.INTENSITY_LIGHTEN1;
	public String UploadBackColor=MainColor;
	public String UploadBackColorIntensity=ABMaterial.INTENSITY_DARKEN2;
	public String UploadFileColor=ABMaterial.COLOR_WHITE;
	public String UploadFileColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String UploadFileSizeColor=ABMaterial.COLOR_WHITE;
	public String UploadFileSizeColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String UploadProgressColor=MainColor;
	public String UploadProgressColorIntensity=ABMaterial.INTENSITY_LIGHTEN1;
	public String UploadBorderColor=MainColor;
	public String UploadBorderColorIntensity=ABMaterial.INTENSITY_DARKEN3;	
	public String ErrorColor=ABMaterial.COLOR_WHITE;
	public String ErrorColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String ZDepth=ABMaterial.ZDEPTH_1;
	
	ThemeUpload() {
		
	}
	
	ThemeUpload(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;	
		
		BackColor=MainColor;
		DropZoneBackColor=MainColor;
		ForeColor=MainColor;
		ButtonBackColor=MainColor;
		ButtonHoverBackColor=MainColor;
		UploadBackColor=MainColor;
		UploadProgressColor=MainColor;
		UploadBorderColor=MainColor;		
	}
	
	@Hide
	public ThemeUpload Clone() {
		ThemeUpload c = new ThemeUpload();
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;
		c.BackColor = BackColor;
		c.BackColorIntensity = BackColorIntensity;
		c.ZDepth=ZDepth;
		c.ForeColor = ForeColor;
		c.ForeColorIntensity = ForeColorIntensity;		
		c.DropZoneBackColor=DropZoneBackColor;
		c.DropZoneBackColorIntensity=DropZoneBackColorIntensity;
		c.ButtonBackColor=ButtonBackColor;
		c.ButtonBackColorIntensity=ButtonBackColorIntensity;
		c.ButtonForeColor=ButtonForeColor;
		c.ButtonForeColorIntensity=ButtonForeColorIntensity;
		c.ButtonHoverBackColor=ButtonHoverBackColor;
		c.ButtonHoverBackColorIntensity=ButtonHoverBackColorIntensity;
		c.UploadBackColor=UploadBackColor;
		c.UploadBackColorIntensity=UploadBackColorIntensity;
		c.UploadFileColor=UploadFileColor;
		c.UploadFileColorIntensity=UploadFileColorIntensity;
		c.UploadFileSizeColor=UploadFileSizeColor;
		c.UploadFileSizeColorIntensity=UploadFileSizeColorIntensity;
		c.UploadProgressColor=UploadProgressColor;
		c.UploadProgressColorIntensity=UploadProgressColorIntensity;
		c.UploadBorderColor=UploadBorderColor;
		c.UploadBorderColorIntensity=UploadBorderColorIntensity;	
		c.ErrorColor=ErrorColor;
		c.ErrorColorIntensity=ErrorColorIntensity;
		return c;
	}
}
