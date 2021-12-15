package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeInput implements java.io.Serializable {
	private static final long serialVersionUID = 5458633570810682995L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	
	public String BackColor="transparent";
	public String BackColorIntensity="";
	public String ForeColor="grey";
	public String ForeColorIntensity="darken-2";
	public String FocusForeColor=MainColor;
	public String FocusForeColorIntensity="";
	public String FocusBackColor="transparent";
	public String FocusBackColorIntensity="";
	public int BorderRadiusPx=0;
	public String InputColor="black";
	public String InputColorIntensity="";	
	public String ValidColor="green";
	public String ValidColorIntensity="";
	public String InvalidColor="red";		
	public String InvalidColorIntensity="";	
	public String AutoCompleteBackColor="white";
	public String AutoCompleteBackColorIntensity="";
	public String AutoCompleteForeColor="black";
	public String AutoCompleteForeColorIntensity="";
	public String AutoCompleteHoverBackColor=MainColor;
	public String AutoCompleteHoverBackColorIntensity="";
	public String AutoCompleteHoverForeColor="white";
	public String AutoCompleteHoverForeColorIntensity="";
	
	public String PlaceholderColor=ABMaterial.COLOR_GREY;
	public String PlaceholderColorIntensity="";
	public String DisabledForeColor=ABMaterial.COLOR_GREY;
	public String DisabledForeColorIntensity="";
	
	public String ExtraStyle="";
	public String ExtraStyleInput="";
	public String ExtraStyleIcon="";
	
	public String AutoCompleteZDepth="";
	public boolean Bold=false;
	public boolean Italic=false;
	public boolean Underlined=false;
	
	public String ZDepth="";
	
	public boolean HasReset=false;
	public String ResetIconUrl="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAA7DAAAOwwHHb6hkAAAAB3RJTUUH4ggNDxc4c5/4AAAAAPVJREFUOMvF0rFKA0EQxvFfNKUQW0GwU1ELERtZ0is+RrhK23uKbTStxJdIaSNErolvYim20WYP1r27OtMsO7v/b2a/HbYdo3xT1/UJ7vAcY/wtzvbxmM6+OwJ1XR/jHQdYoGpFEvyGa3zgNsb4A+OsyH2CYZbACpMMhoAzrEuBJ5y3cFrHKdfCm9TZesiDEV4ykTxa+DVP7uabpmmEEJY4wmUh0IFhp6fSBBc9+ZvUocEOCrfLuMJhCGHZNE1XoAfeoMJXgntF8ic89Li9SCKL7N4M0z4P5mlI/rmdhikXmWM19I17OI0xfpYGJAOnWJVjvt34AwIhVxpEiGRUAAAAAElFTkSuQmCC";
	public String ResetTop="-12px";
	
	public boolean ForINPUT_DATE=true;
	public boolean ForINPUT_DATETIMELOCAL=true;
	public boolean ForINPUT_EMAIL=true;
	public boolean ForINPUT_NUMBER=true;
	public boolean ForINPUT_PASSWORD=true;
	public boolean ForINPUT_SEARCH=true;
	public boolean ForINPUT_TEL=true;
	public boolean ForINPUT_TEXT=true;
	public boolean ForINPUT_TIME=true;
	public boolean ForINPUT_URL=true;
		
	ThemeInput() {
		
	}
	
	ThemeInput(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;
		FocusForeColor=MainColor;				
	}
	
	@Hide
	public ThemeInput Clone() {
		ThemeInput cl = new ThemeInput();
		
		cl.MainColor=MainColor;
		cl.ThemeName = ThemeName;
		cl.BackColor=BackColor;
		cl.BackColorIntensity=BackColorIntensity;
		cl.ForeColor=ForeColor;
		cl.ForeColorIntensity=ForeColorIntensity;
		cl.FocusForeColor=FocusForeColor;
		cl.FocusForeColorIntensity=FocusForeColorIntensity;
		cl.FocusBackColor=FocusBackColor;
		cl.FocusBackColorIntensity=FocusBackColorIntensity;
		cl.BorderRadiusPx=BorderRadiusPx;
		cl.InputColor=InputColor;
		cl.InputColorIntensity=InputColorIntensity;	
		cl.ValidColor=ValidColor;
		cl.ValidColorIntensity=ValidColorIntensity;
		cl.InvalidColor=InvalidColor;		
		cl.InvalidColorIntensity=InvalidColorIntensity;
		
		cl.AutoCompleteBackColor=AutoCompleteBackColor;
		cl.AutoCompleteBackColorIntensity=AutoCompleteBackColorIntensity;
		cl.AutoCompleteForeColor=AutoCompleteForeColor;
		cl.AutoCompleteForeColorIntensity=AutoCompleteForeColorIntensity;
		cl.AutoCompleteHoverBackColor=AutoCompleteHoverBackColor;
		cl.AutoCompleteHoverBackColorIntensity=AutoCompleteHoverBackColorIntensity;
		cl.AutoCompleteHoverForeColor=AutoCompleteHoverForeColor;
		cl.AutoCompleteHoverForeColorIntensity=AutoCompleteHoverForeColorIntensity;
		cl.AutoCompleteZDepth = AutoCompleteZDepth;
		
		cl.Bold=Bold;
		cl.Italic=Italic;
		cl.Underlined=Underlined;
		
		cl.PlaceholderColor=PlaceholderColor;
		cl.PlaceholderColorIntensity=PlaceholderColorIntensity;
		cl.DisabledForeColor=DisabledForeColor;
		cl.DisabledForeColorIntensity=DisabledForeColorIntensity;
		
		cl.ExtraStyle = ExtraStyle;
		cl.ExtraStyleInput = ExtraStyleInput;
		cl.ExtraStyleIcon = ExtraStyleIcon;
		
		cl.HasReset=HasReset;
		cl.ResetIconUrl = ResetIconUrl;
		cl.ResetTop = ResetTop;
		
		cl.ZDepth=ZDepth;	
		
		cl.ForINPUT_DATE=ForINPUT_DATE;
		cl.ForINPUT_DATETIMELOCAL=ForINPUT_DATETIMELOCAL;
		cl.ForINPUT_EMAIL=ForINPUT_EMAIL;
		cl.ForINPUT_NUMBER=ForINPUT_NUMBER;
		cl.ForINPUT_PASSWORD=ForINPUT_PASSWORD;
		cl.ForINPUT_SEARCH=ForINPUT_SEARCH;
		cl.ForINPUT_TEL=ForINPUT_TEL;
		cl.ForINPUT_TEXT=ForINPUT_TEXT;
		cl.ForINPUT_TIME=ForINPUT_TIME;
		cl.ForINPUT_URL=ForINPUT_URL;
		return cl;
	}
	
	@Hide
	public String AsB4JSVar() {
		StringBuilder s = new StringBuilder();
		s.append("var _b4js_input_" + ThemeName.toLowerCase() + "={};");
		s.append("_b4js_input_" + ThemeName.toLowerCase() + ".ThemeName='" + ThemeName.toLowerCase() + "';");
		s.append("_b4js_input_" + ThemeName.toLowerCase() + ".BackColor='" + ABMaterial.GetColorStrMap(BackColor, BackColorIntensity) + "';");
		s.append("_b4js_input_" + ThemeName.toLowerCase() + ".ForeColor='" + ABMaterial.GetColorStr(ForeColor, ForeColorIntensity, "text") + "';");
		s.append("_b4jsthemes['input-" + ThemeName.toLowerCase() + "']=_b4js_input_" + ThemeName.toLowerCase() + ";");
		return s.toString();
	}
}
