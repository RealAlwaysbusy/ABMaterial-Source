package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeRadioGroup implements java.io.Serializable {
	private static final long serialVersionUID = -160136663619280316L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String LabelColor="black";
	public String LabelColorIntensity="";
	public String RadioOnColor=MainColor;
	public String RadioOnColorIntensity="";
	public String TitleColor="grey";
	public String TitleColorIntensity="darken-2";
	
	public String DisabledTitleColor="grey";
	public String DisabledTitleColorIntensity="lighten-1";
	public String BorderColor="grey";
	public String BorderColorIntensity="lighten-1";	
	public String DisabledLabelColor="black";
	public String DisabledLabelColorIntensity="";
	public String DisabledRadioOnColor="grey";
	public String DisabledRadioOnColorIntensity="lighten-1";
	public String DisabledBorderColor="grey";
	public String DisabledBorderColorIntensity="lighten-1";
	
	private boolean WithGab=false;
	
	ThemeRadioGroup() {		
	}
	
	ThemeRadioGroup(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;			
		RadioOnColor=MainColor;
	}
	
	@Hide
	public ThemeRadioGroup Clone() {
		ThemeRadioGroup c = new ThemeRadioGroup();
		
		c.ThemeName=ThemeName;
		c.LabelColor=LabelColor;
		c.LabelColorIntensity=LabelColorIntensity;
		c.RadioOnColor=RadioOnColor;
		c.RadioOnColorIntensity=RadioOnColorIntensity;
		c.TitleColor = TitleColor;
		c.TitleColorIntensity = TitleColorIntensity;
		
		c.DisabledTitleColor = DisabledTitleColor;
		c.DisabledTitleColorIntensity = DisabledTitleColorIntensity;
		c.BorderColor=BorderColor;
		c.BorderColorIntensity=BorderColorIntensity;	
		c.DisabledLabelColor=DisabledLabelColor;
		c.DisabledLabelColorIntensity=DisabledLabelColorIntensity;
		c.DisabledRadioOnColor=DisabledRadioOnColor;
		c.DisabledRadioOnColorIntensity=DisabledRadioOnColorIntensity;
		c.DisabledBorderColor=DisabledBorderColor;
		c.DisabledBorderColorIntensity=DisabledBorderColorIntensity;
		c.WithGab=WithGab;
		return c;
	}
}
