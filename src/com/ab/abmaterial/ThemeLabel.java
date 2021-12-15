package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeLabel implements java.io.Serializable {
	private static final long serialVersionUID = -46991567324288908L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String BackColor="transparent";
	public String BackColorIntensity="";	
	public String BlockquoteColor=MainColor;
	public String BlockquoteColorIntensity="";
	public int BlockquoteWidthPx=5;
	public String ForeColor="black";
	public String ForeColorIntensity="";
	public String IconColor="black";
	public String IconColorIntensity="";
	
	public String Align="";
	public boolean UseStrikethrough=false;
	public String StrikethroughColor=ABMaterial.COLOR_RED;
	public String StrikethroughColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String FontWeight="";
	
	public boolean HasReset=false;
	public String ResetIconUrl="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAA7DAAAOwwHHb6hkAAAAB3RJTUUH4ggNDxc4c5/4AAAAAPVJREFUOMvF0rFKA0EQxvFfNKUQW0GwU1ELERtZ0is+RrhK23uKbTStxJdIaSNErolvYim20WYP1r27OtMsO7v/b2a/HbYdo3xT1/UJ7vAcY/wtzvbxmM6+OwJ1XR/jHQdYoGpFEvyGa3zgNsb4A+OsyH2CYZbACpMMhoAzrEuBJ5y3cFrHKdfCm9TZesiDEV4ykTxa+DVP7uabpmmEEJY4wmUh0IFhp6fSBBc9+ZvUocEOCrfLuMJhCGHZNE1XoAfeoMJXgntF8ic89Li9SCKL7N4M0z4P5mlI/rmdhikXmWM19I17OI0xfpYGJAOnWJVjvt34AwIhVxpEiGRUAAAAAElFTkSuQmCC";
	
	public String ResetAlignRight="";
	
	public String PlaceHolderColor=ABMaterial.COLOR_GREY;
	public String PlaceHolderColorIntensity = ABMaterial.INTENSITY_LIGHTEN1;
	
	public String ZDepth="";
	
	ThemeLabel() {
		
	}
	
	ThemeLabel(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;
		BlockquoteColor=MainColor;				
	}
	
	@Hide
	public ThemeLabel Clone() {
		ThemeLabel l = new ThemeLabel();
		
		l.MainColor=MainColor;
		l.ThemeName=ThemeName;
		l.BackColor=BackColor;
		l.BackColorIntensity=BackColorIntensity;	
		l.ForeColor=ForeColor;
		l.ForeColorIntensity=ForeColorIntensity;
		l.BlockquoteColor=BlockquoteColor;
		l.BlockquoteColorIntensity=BlockquoteColorIntensity;
		l.Align=Align;
		l.IconColor=IconColor;
		l.IconColorIntensity=IconColorIntensity;
		l.BlockquoteWidthPx=BlockquoteWidthPx;
		l.ZDepth=ZDepth;
		l.UseStrikethrough=UseStrikethrough;
		l.StrikethroughColor=StrikethroughColor;
		l.StrikethroughColorIntensity=StrikethroughColorIntensity;
		l.FontWeight=FontWeight;
		
		l.HasReset=HasReset;
		l.ResetIconUrl = ResetIconUrl;
		
		l.ResetAlignRight=ResetAlignRight;
		
		l.PlaceHolderColor=PlaceHolderColor;
		l.PlaceHolderColorIntensity=PlaceHolderColorIntensity;
		return l;
	}
}
