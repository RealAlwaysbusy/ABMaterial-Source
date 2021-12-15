package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeToast implements java.io.Serializable {
	private static final long serialVersionUID = -5741334709266757974L;
	protected String ThemeName="default";	
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String BackColor="grey";
	public String BackColorIntensity="";
	public String ForeColor="white";
	public String ForeColorIntensity="";
	public String ActionForeColor=MainColor;
	public String ActionForeColorIntensity="";
	public String CloseButtonColor="grey";
	public String CloseButtonColorIntensity=ABMaterial.INTENSITY_LIGHTEN3;
	public boolean Rounded=false;
	
	ThemeToast() {
		
	}
	
	ThemeToast(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;		
		ActionForeColor=MainColor;
	}
	
	@Hide
	public ThemeToast Clone() {
		ThemeToast c = new ThemeToast();
		
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;
		c.BackColor=BackColor;
		c.BackColorIntensity=BackColorIntensity;
		c.ForeColor=ForeColor;
		c.ForeColorIntensity=ForeColorIntensity;
		c.ActionForeColor=ActionForeColor;
		c.ActionForeColorIntensity=ActionForeColorIntensity;	
		c.Rounded = Rounded;
		c.CloseButtonColor=CloseButtonColor;
		c.CloseButtonColorIntensity=CloseButtonColorIntensity;
		return c;
	}
	
	@Hide
	public String AsB4JSVar() {
		StringBuilder s = new StringBuilder();
		s.append("var _b4js_toast_" + ThemeName.toLowerCase() + "={};");
		s.append("_b4js_toast_" + ThemeName.toLowerCase() + ".ThemeName='" + ThemeName.toLowerCase() + "';");
		s.append("_b4js_toast_" + ThemeName.toLowerCase() + ".BackColor='" + ABMaterial.GetColorStrMap(BackColor, BackColorIntensity) + "';");
		s.append("_b4js_toast_" + ThemeName.toLowerCase() + ".ForeColor='" + ABMaterial.GetColorStr(ForeColor, ForeColorIntensity, "text") + "';");
		s.append("_b4js_toast_" + ThemeName.toLowerCase() + ".ActionForeColor='" + ABMaterial.GetColorStr(ActionForeColor, ActionForeColorIntensity, "text") + "';");
		s.append("_b4js_toast_" + ThemeName.toLowerCase() + ".CloseButtonColor='" + ABMaterial.GetColorStr(CloseButtonColor, CloseButtonColorIntensity, "text") + "';");
		if (Rounded) {
			s.append("_b4js_toast_" + ThemeName.toLowerCase() + ".Rounded='rounded';");
		} else {
			s.append("_b4js_toast_" + ThemeName.toLowerCase() + ".Rounded='';");
		}		
		s.append("_b4jsthemes['toast-" + ThemeName.toLowerCase() + "']=_b4js_toast_" + ThemeName.toLowerCase() + ";");
		return s.toString();
	}
}
