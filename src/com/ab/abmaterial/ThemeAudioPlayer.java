package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeAudioPlayer implements java.io.Serializable{
	private static final long serialVersionUID = 6622555693110430112L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String PlayPauseColor=ABMaterial.COLOR_WHITE;
	public String ForeColor=MainColor;
	public String BackColor=ABMaterial.COLOR_WHITE;
	public String PlayBarBackColor=ABMaterial.COLOR_GREY;
	public String VolumeBackColor=ABMaterial.COLOR_GREY;	
	public String ZDepth=ABMaterial.ZDEPTH_1;
	
	ThemeAudioPlayer() {
	
	}
	
	ThemeAudioPlayer(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;
		ForeColor=MainColor;
		
	}
	
	@Hide
	public ThemeAudioPlayer Clone() {
		ThemeAudioPlayer c = new ThemeAudioPlayer();
		
		c.ThemeName=ThemeName;
		c.MainColor=MainColor;
		c.PlayPauseColor=PlayPauseColor;
		c.ForeColor=ForeColor;
		c.BackColor=BackColor;
		c.PlayBarBackColor=PlayBarBackColor;
		c.VolumeBackColor=VolumeBackColor;
		c.ZDepth=ZDepth;
		return c;
	}
}
