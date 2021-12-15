package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemePDFViewer implements java.io.Serializable {
	
	private static final long serialVersionUID = 5790789457064466233L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String BackColor="grey";
	public String BackColorIntensity="darken-2";
	public String ToolbarColor=MainColor;
	public String ToolbarColorIntensity="";
	
	ThemePDFViewer() {
	
	}
	
	ThemePDFViewer(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;
		ToolbarColor=MainColor;
						
	}
	
	@Hide
	public ThemePDFViewer Clone() {
		ThemePDFViewer c = new ThemePDFViewer();
		
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;
		c.BackColor=BackColor;
		c.BackColorIntensity=BackColorIntensity;
		c.ToolbarColor=ToolbarColor;
		c.ToolbarColorIntensity=ToolbarColorIntensity;
		return c;
	}
}
