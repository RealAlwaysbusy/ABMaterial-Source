package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeMsgBox implements java.io.Serializable {
	
	private static final long serialVersionUID = -6595549614844930090L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String BackColor="white";
	public String BackColorIntensity="";
	public String IconColor=MainColor;
	public String IconColorIntensity="";
	public String ConfirmButtonColor=MainColor;
	public String ConfirmButtonColorIntensity="";
	public String ConfirmButtonTextColor="white";
	public String ConfirmButtonTextColorIntensity="";
	public String CancelButtonColor=MainColor;
	public String CancelButtonColorIntensity="";
	public String CancelButtonTextColor="white";
	public String CancelButtonTextColorIntensity="";
	public String CloseButtonColor="grey";
	public String CloseButtonColorIntensity="";
	public String TitleTextColor="black";
	public String TitleTextColorIntensity="";
	public String MessageTextColor="black";
	public String MessageTextColorIntensity="";
	public String CheckboxTextColor="black";
	public String CheckboxTextColorIntensity="";
	public String RadioTextColor="black";
	public String RadioTextColorIntensity="";
	public String TextAreaTextColor="black";
	public String TextAreaTextColorIntensity="";
	public String InputFieldTheme="default";
	protected String ImageUrl="";
	protected int ImageWidth=80;
	protected int ImageHeight=80;
	public boolean RightToLeft=false;
		
	ThemeMsgBox() {
	
	}
	
	ThemeMsgBox(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;		
		ConfirmButtonColor=MainColor;		
		CancelButtonColor=MainColor;	
		IconColor=MainColor;
	}
	
	public void SetImage(String imageUrl, int imageWidthPx, int imageHeightPx) {
		this.ImageUrl=imageUrl;
		this.ImageWidth=imageWidthPx;
		this.ImageHeight=imageHeightPx;
	}
	
	@Hide
	public ThemeMsgBox Clone() {
		ThemeMsgBox c = new ThemeMsgBox();
		
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;
		c.BackColor=BackColor;
		c.BackColorIntensity=BackColorIntensity;
		c.IconColor=IconColor;
		c.IconColorIntensity=IconColorIntensity;
		
		c.ConfirmButtonColor=ConfirmButtonColor;
		c.ConfirmButtonColorIntensity=ConfirmButtonColorIntensity;
		c.ConfirmButtonTextColor=ConfirmButtonTextColor;
		c.ConfirmButtonTextColorIntensity=ConfirmButtonTextColorIntensity;
		
		c.CancelButtonColor=CancelButtonColor;
		c.CancelButtonColorIntensity=CancelButtonColorIntensity;
		c.CancelButtonTextColor=CancelButtonTextColor;
		c.CancelButtonTextColorIntensity=CancelButtonTextColorIntensity;
		
		c.CheckboxTextColor=CheckboxTextColor;
		c.CheckboxTextColorIntensity=CheckboxTextColorIntensity;
		c.RadioTextColor=RadioTextColor;
		c.RadioTextColorIntensity=RadioTextColorIntensity;
		c.TextAreaTextColor=TextAreaTextColor;
		c.TextAreaTextColorIntensity=TextAreaTextColorIntensity;
		c.CloseButtonColor=CloseButtonColor;
		c.CloseButtonColorIntensity=CloseButtonColorIntensity;
		c.ImageUrl=ImageUrl;
		c.ImageWidth=ImageWidth;
		c.ImageHeight=ImageHeight;
		c.InputFieldTheme=InputFieldTheme;
		c.RightToLeft=RightToLeft;
		return c;
	}
	
	@Hide
	public String AsB4JSVar() {
		StringBuilder s = new StringBuilder();
		s.append("var _b4js_msgbox_" + ThemeName.toLowerCase() + "={};");
		s.append("_b4js_msgbox_" + ThemeName.toLowerCase() + ".ThemeName='" + ThemeName.toLowerCase() + "';");
		s.append("_b4js_msgbox_" + ThemeName.toLowerCase() + ".BackColor='" + ABMaterial.GetColorStrMap(BackColor, BackColorIntensity) + "';");
		s.append("_b4js_msgbox_" + ThemeName.toLowerCase() + ".ConfirmButtonColor='" + ABMaterial.GetColorStrMap(ConfirmButtonColor, ConfirmButtonColorIntensity) + "';");
		s.append("_b4js_msgbox_" + ThemeName.toLowerCase() + ".ConfirmButtonTextColor='" + ABMaterial.GetColorStrMap(ConfirmButtonTextColor, ConfirmButtonTextColorIntensity) + "';");
		s.append("_b4js_msgbox_" + ThemeName.toLowerCase() + ".CancelButtonColor='" + ABMaterial.GetColorStrMap(CancelButtonColor, CancelButtonColorIntensity) + "';");
		s.append("_b4js_msgbox_" + ThemeName.toLowerCase() + ".CancelButtonTextColor='" + ABMaterial.GetColorStrMap(CancelButtonTextColor, CancelButtonTextColorIntensity) + "';");
		s.append("_b4js_msgbox_" + ThemeName.toLowerCase() + ".CheckboxTextColor='" + ABMaterial.GetColorStrMap(CheckboxTextColor, CheckboxTextColorIntensity) + "';");
		s.append("_b4js_msgbox_" + ThemeName.toLowerCase() + ".RadioTextColor='" + ABMaterial.GetColorStrMap(RadioTextColor, RadioTextColorIntensity) + "';");
		s.append("_b4js_msgbox_" + ThemeName.toLowerCase() + ".TextAreaTextColor='" + ABMaterial.GetColorStrMap(TextAreaTextColor, TextAreaTextColorIntensity) + "';");
		s.append("_b4js_msgbox_" + ThemeName.toLowerCase() + ".CloseButtonColor='" + ABMaterial.GetColorStrMap(CloseButtonColor, CloseButtonColorIntensity) + "';");
		s.append("_b4js_msgbox_" + ThemeName.toLowerCase() + ".IconColor='" + ABMaterial.GetColorStrMap(IconColor, IconColorIntensity) + "';");
		s.append("_b4js_msgbox_" + ThemeName.toLowerCase() + ".ImageUrl='" + ImageUrl + "';");
		s.append("_b4js_msgbox_" + ThemeName.toLowerCase() + ".ImageWidth=" + ImageWidth + ";");
		s.append("_b4js_msgbox_" + ThemeName.toLowerCase() + ".ImageHeight=" + ImageHeight + ";");
		s.append("_b4js_msgbox_" + ThemeName.toLowerCase() + ".InputFieldTheme='" + InputFieldTheme + "';");
		s.append("_b4js_msgbox_" + ThemeName.toLowerCase() + ".RightToLeft=" + RightToLeft + ";");
		s.append("_b4jsthemes['msgbox-" + ThemeName.toLowerCase() + "']=_b4js_msgbox_" + ThemeName.toLowerCase() + ";");
		return s.toString();
	}
}
