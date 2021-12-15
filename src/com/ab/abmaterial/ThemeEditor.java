package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeEditor implements java.io.Serializable {
	private static final long serialVersionUID = -716371558982624218L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;	
	public String ActiveMenuColor=MainColor;
	public String ActiveMenuColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String ActiveBorderColor=MainColor;
	public String ActiveBorderColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String InactiveBorderColor=ABMaterial.COLOR_GREY;
	public String InactiveBorderColorIntensity=ABMaterial.INTENSITY_LIGHTEN2;
	public String TextColor=ABMaterial.COLOR_BLACK;
	public String TextColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String BlockQuoteColor=MainColor;
	public String BlockQuoteColorIntensity=ABMaterial.INTENSITY_NORMAL;
	
	public String MenuBackColor=ABMaterial.COLOR_TRANSPARENT;
	public String MenuBackColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String BackColor=ABMaterial.COLOR_TRANSPARENT;
	public String BackColorIntensity=ABMaterial.INTENSITY_NORMAL;
	
	public String DropboxButtonBackColor=MainColor;
	public String DropboxButtonBackColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String DropboxButtonForeColor=ABMaterial.COLOR_WHITE;
	public String DropboxButtonForeColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String DropboxTitleColor=MainColor;
	public String DropboxTitleColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String DropboxTextColor=ABMaterial.COLOR_BLACK;
	public String DropboxTextColorIntensity=ABMaterial.INTENSITY_NORMAL;
	
			
	
	ThemeEditor() {
	
	}
	
	ThemeEditor(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;	
		this.ActiveMenuColor=MainColor;
		this.ActiveBorderColor=MainColor;			
		this.BlockQuoteColor=MainColor;
		this.DropboxButtonBackColor=MainColor;
		this.DropboxTitleColor=MainColor;
	}
	
	@Hide
	public ThemeEditor Clone() {
		ThemeEditor c = new ThemeEditor();
		
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;
		c.ActiveMenuColor=ActiveMenuColor;
		c.ActiveMenuColorIntensity=ActiveMenuColorIntensity;
		c.ActiveBorderColor=ActiveBorderColor;
		c.ActiveBorderColorIntensity=ActiveBorderColorIntensity;
		c.InactiveBorderColor=InactiveBorderColor;
		c.InactiveBorderColorIntensity=InactiveBorderColorIntensity;
		c.TextColor=TextColor;
		c.TextColorIntensity=TextColorIntensity;
		c.BlockQuoteColor=BlockQuoteColor;
		c.BlockQuoteColorIntensity=BlockQuoteColorIntensity;
		
		c.MenuBackColor=MenuBackColor;
		c.MenuBackColorIntensity=MenuBackColorIntensity;
		c.BackColor=BackColor;
		c.BackColorIntensity=BackColorIntensity;
		
		c.DropboxButtonBackColor=DropboxButtonBackColor;
		c.DropboxButtonBackColorIntensity=DropboxButtonBackColorIntensity;
		c.DropboxButtonForeColor=DropboxButtonForeColor;
		c.DropboxButtonForeColorIntensity=DropboxButtonForeColorIntensity;
		c.DropboxTitleColor=DropboxTitleColor;
		c.DropboxTitleColorIntensity=DropboxTitleColorIntensity;
		c.DropboxTextColor=DropboxTitleColor;
		c.DropboxTextColorIntensity=DropboxTitleColorIntensity;
		
		return c;
	}
}
