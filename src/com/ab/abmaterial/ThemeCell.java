package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeCell implements java.io.Serializable {
	private static final long serialVersionUID = -3902912492375974101L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String BackColor=ABMaterial.COLOR_TRANSPARENT;
	public String BackColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String ZDepth=ABMaterial.ZDEPTH_DEFAULT;
	public String BorderColor=ABMaterial.COLOR_TRANSPARENT;
	public String BorderColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public int BorderWidth=0;
	public Boolean VerticalAlign=false;
	public String Align=ABMaterial.CELL_ALIGN_LEFT;
	public boolean Clickable=false;
	public String ClickableWavesEffect=""; //"waves-effect";
	public boolean ClickableWavesCircle=false;
	public int BorderRadiusPx=0;
	public int BorderRadiusBottomLeftPx=0;
	public int BorderRadiusBottomRightPx=0;
	public int BorderRadiusTopLeftPx=0;
	public int BorderRadiusTopRightPx=0;
	public String BorderStyle="";
	
	protected int MasonryColumnsSmall=-9999;
	protected int MasonryColumnsMedium=-9999;
	protected int MasonryColumnsLarge=-9999;
	protected int MasonryColumnsExtraLarge=-9999;
	protected String MasonColumnsExtraCSS="";
			
	ThemeCell() {
		
	}
	
	ThemeCell(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;		
	}
	
	public void SetMasonryColumns(int NumberOfColumnsSmall, int NumberOfColumnsMedium, int NumberOfColumnsLarge, int NumberOfColumnsExtraLarge, String ColumnExtraStyle) {
		MasonryColumnsSmall=NumberOfColumnsSmall;
		MasonryColumnsMedium=NumberOfColumnsMedium;
		MasonryColumnsLarge=NumberOfColumnsLarge;
		MasonryColumnsExtraLarge=NumberOfColumnsExtraLarge;
		MasonColumnsExtraCSS=ColumnExtraStyle;
	}
	
	@Hide
	public ThemeCell Clone() {
		ThemeCell c = new ThemeCell();
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;
		c.BackColor = BackColor;
		c.BackColorIntensity = BackColorIntensity;
		c.ZDepth=ZDepth;
		c.BorderColor = BorderColor;
		c.BorderColorIntensity = BorderColorIntensity;
		c.BorderWidth=BorderWidth;
		c.VerticalAlign=VerticalAlign;
		c.Align=Align;
		c.Clickable=Clickable;
		c.ClickableWavesEffect=ClickableWavesEffect;
		c.ClickableWavesCircle=ClickableWavesCircle;
		c.BorderRadiusPx=BorderRadiusPx;
		c.BorderRadiusBottomLeftPx=BorderRadiusBottomLeftPx;
		c.BorderRadiusBottomRightPx=BorderRadiusBottomRightPx;
		c.BorderRadiusTopLeftPx=BorderRadiusTopLeftPx;
		c.BorderRadiusTopRightPx=BorderRadiusTopRightPx;
		c.BorderStyle=BorderStyle;
		
		c.MasonryColumnsSmall=MasonryColumnsSmall;
		c.MasonryColumnsMedium=MasonryColumnsMedium;
		c.MasonryColumnsLarge=MasonryColumnsLarge;
		c.MasonryColumnsExtraLarge=MasonryColumnsExtraLarge;
		c.MasonColumnsExtraCSS=MasonColumnsExtraCSS;
		
		return c;
	}
}
