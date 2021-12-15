package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeDocumentViewer implements java.io.Serializable {
	private static final long serialVersionUID = 430159421743230000L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	
	ThemeDocumentViewer() {
	
	}
	
	ThemeDocumentViewer(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;
						
	}
	
	@Hide
	public ThemeDocumentViewer Clone() {
		ThemeDocumentViewer c = new ThemeDocumentViewer();
		
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;
		return c;
	}
}
