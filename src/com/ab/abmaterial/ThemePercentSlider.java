package com.ab.abmaterial;

import java.util.ArrayList;
import java.util.List;

import anywheresoftware.b4a.BA.Author;
//import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemePercentSlider implements java.io.Serializable{

	private static final long serialVersionUID = -1807671958098994883L;
	
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String BackColor="white";
	public String BackColorIntensity="";	
	
	public String BorderColor=ABMaterial.COLOR_GREY;
	public String BorderIntensity="";
	public String ActiveLabelColor=ABMaterial.COLOR_WHITE;
	public String ActiveLabelColorIntensity="";
	public String InactiveLabelColor=ABMaterial.COLOR_GREY;
	public String InactiveLabelColorIntensity=ABMaterial.INTENSITY_DARKEN3;
	
	protected List<String> BlockColors = new ArrayList<String>();
	protected List<String> BlockColorsIntensity = new ArrayList<String>();
	
	public String UncheckedArrowColor=ABMaterial.COLOR_GREY;
	public String UncheckedArrowColorIntensity="";
	public String CheckedArrowColor=ABMaterial.COLOR_BLACK;
	public String CheckedArrowColorIntensity="";
	
	public String IndicatorLabelColor=ABMaterial.COLOR_BLACK;
	public String IndicatorLabelColorIntensity="";
		
	public String ZDepth="";
	
	ThemePercentSlider() {
		for (int i=0;i<20;i++) {
			BlockColors.add(MainColor);
			BlockColorsIntensity.add("");
		}
	}
	
	ThemePercentSlider(String themeName) {
		for (int i=0;i<20;i++) {
			BlockColors.add(MainColor);
			BlockColorsIntensity.add("");
		}
		this.ThemeName = themeName;
	}
	
	/**
	 * index 0 to 19 
	 */
	public void BlockColor(int index, String blockColor) {
		BlockColors.set(index, blockColor);
	}
	
	/**
	 * index 0 to 19 
	 */
	public void BlockColorIntensity(int index, String blockColorIntensity) {
		BlockColorsIntensity.set(index, blockColorIntensity);
	}
	
	public void Colorize(String color) {
		MainColor = color;
		BlockColors = new ArrayList<String>();
		BlockColorsIntensity = new ArrayList<String>();
		for (int i=0;i<20;i++) {
			BlockColors.add(MainColor);
			BlockColorsIntensity.add("");
		}	
	}
	
	@Hide
	public ThemePercentSlider Clone() {
		ThemePercentSlider l = new ThemePercentSlider();
		
		l.MainColor=MainColor;
		l.ThemeName=ThemeName;
		l.BackColor=BackColor;
		l.BackColorIntensity=BackColorIntensity;	
		
		l.BorderColor=BorderColor;
		l.BorderIntensity=BorderIntensity;
		l.ActiveLabelColor=ActiveLabelColor;
		l.ActiveLabelColorIntensity=ActiveLabelColorIntensity;
		l.InactiveLabelColor=InactiveLabelColor;
		l.InactiveLabelColorIntensity=InactiveLabelColorIntensity;
		
		l.BlockColors = new ArrayList<String>();
		l.BlockColorsIntensity = new ArrayList<String>();
		for (int i=0;i<20;i++) {
			l.BlockColors.add(BlockColors.get(i));
			l.BlockColorsIntensity.add(BlockColorsIntensity.get(i));
		}
		
		l.UncheckedArrowColor=UncheckedArrowColor;
		l.UncheckedArrowColorIntensity=UncheckedArrowColorIntensity;
		l.CheckedArrowColor=CheckedArrowColor;
		l.CheckedArrowColorIntensity=CheckedArrowColorIntensity;
		
		l.IndicatorLabelColor=IndicatorLabelColor;
		l.IndicatorLabelColorIntensity=IndicatorLabelColorIntensity;
		
		l.ZDepth=ZDepth;
		return l;
	}

}
