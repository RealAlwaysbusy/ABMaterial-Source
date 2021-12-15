package com.ab.abmaterial;

import java.util.LinkedHashMap;
import java.util.Map;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeChart implements java.io.Serializable{
	private static final long serialVersionUID = 2004406250668143102L;
	protected String ThemeName="default";	
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String BackColor="transparent";
	public String BackColorIntensity="";
	public String LabelColor="grey";
	public String LabelColorIntensity="";
	public int LabelFontSizePx=12;
	
	protected Map<String,ThemeSerie> Series = new LinkedHashMap<String,ThemeSerie>();
	public String ZDepth="";
	
	ThemeChart() {
		Series.put(ABMaterial.CHART_SERIEINDEX_A, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_B, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_C, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_D, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_E, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_F, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_G, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_H, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_I, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_J, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_K, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_L, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_M, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_N, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_O, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_Z, new ThemeSerie());
		Serie(ABMaterial.CHART_SERIEINDEX_A).Color = "light-blue";
		Serie(ABMaterial.CHART_SERIEINDEX_B).Color = "red";
		Serie(ABMaterial.CHART_SERIEINDEX_C).Color = "green";
		Serie(ABMaterial.CHART_SERIEINDEX_D).Color = "yellow";
		Serie(ABMaterial.CHART_SERIEINDEX_E).Color = "brown";
		Serie(ABMaterial.CHART_SERIEINDEX_F).Color = "deep-purple";
		Serie(ABMaterial.CHART_SERIEINDEX_G).Color = "lime";
		Serie(ABMaterial.CHART_SERIEINDEX_H).Color = "purple";
		Serie(ABMaterial.CHART_SERIEINDEX_I).Color = "teal";
		Serie(ABMaterial.CHART_SERIEINDEX_J).Color = "cyan";
		Serie(ABMaterial.CHART_SERIEINDEX_K).Color = "amber";
		Serie(ABMaterial.CHART_SERIEINDEX_L).Color = "light-green";
		Serie(ABMaterial.CHART_SERIEINDEX_M).Color = "blue";
		Serie(ABMaterial.CHART_SERIEINDEX_N).Color = "indigo";
		Serie(ABMaterial.CHART_SERIEINDEX_O).Color = "pink";
		Serie(ABMaterial.CHART_SERIEINDEX_Z).Color = "transparent";
	}
	
	ThemeChart(String themeName) {
		Series.put(ABMaterial.CHART_SERIEINDEX_A, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_B, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_C, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_D, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_E, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_F, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_G, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_H, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_I, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_J, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_K, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_L, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_M, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_N, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_O, new ThemeSerie());
		Series.put(ABMaterial.CHART_SERIEINDEX_Z, new ThemeSerie());
		Serie(ABMaterial.CHART_SERIEINDEX_A).Color = "light-blue";
		Serie(ABMaterial.CHART_SERIEINDEX_B).Color = "red";
		Serie(ABMaterial.CHART_SERIEINDEX_C).Color = "green";
		Serie(ABMaterial.CHART_SERIEINDEX_D).Color = "yellow";
		Serie(ABMaterial.CHART_SERIEINDEX_E).Color = "brown";
		Serie(ABMaterial.CHART_SERIEINDEX_F).Color = "deep-purple";
		Serie(ABMaterial.CHART_SERIEINDEX_G).Color = "lime";
		Serie(ABMaterial.CHART_SERIEINDEX_H).Color = "purple";
		Serie(ABMaterial.CHART_SERIEINDEX_I).Color = "teal";
		Serie(ABMaterial.CHART_SERIEINDEX_J).Color = "cyan";
		Serie(ABMaterial.CHART_SERIEINDEX_K).Color = "amber";
		Serie(ABMaterial.CHART_SERIEINDEX_L).Color = "light-green";
		Serie(ABMaterial.CHART_SERIEINDEX_M).Color = "blue";
		Serie(ABMaterial.CHART_SERIEINDEX_N).Color = "indigo";
		Serie(ABMaterial.CHART_SERIEINDEX_O).Color = "pink";
		Serie(ABMaterial.CHART_SERIEINDEX_Z).Color = "transparent";
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;		
	}
	
	public ThemeSerie Serie(String serieIndex) {
		return Series.getOrDefault(serieIndex.toLowerCase(), null);
	}
	
	@Hide
	public ThemeChart Clone() {
		ThemeChart c = new ThemeChart();
		c.MainColor=MainColor;
		c.ThemeName = ThemeName;
		c.BackColor=BackColor;
		c.BackColorIntensity=BackColorIntensity;
		c.LabelColor=LabelColor;
		c.LabelColorIntensity=LabelColorIntensity;
		c.ZDepth=ZDepth;
		c.Series.put(ABMaterial.CHART_SERIEINDEX_A, Series.get(ABMaterial.CHART_SERIEINDEX_A).Clone());
		c.Series.put(ABMaterial.CHART_SERIEINDEX_B, Series.get(ABMaterial.CHART_SERIEINDEX_B).Clone());
		c.Series.put(ABMaterial.CHART_SERIEINDEX_C, Series.get(ABMaterial.CHART_SERIEINDEX_C).Clone());
		c.Series.put(ABMaterial.CHART_SERIEINDEX_D, Series.get(ABMaterial.CHART_SERIEINDEX_D).Clone());
		c.Series.put(ABMaterial.CHART_SERIEINDEX_E, Series.get(ABMaterial.CHART_SERIEINDEX_E).Clone());
		c.Series.put(ABMaterial.CHART_SERIEINDEX_F, Series.get(ABMaterial.CHART_SERIEINDEX_F).Clone());
		c.Series.put(ABMaterial.CHART_SERIEINDEX_G, Series.get(ABMaterial.CHART_SERIEINDEX_G).Clone());
		c.Series.put(ABMaterial.CHART_SERIEINDEX_H, Series.get(ABMaterial.CHART_SERIEINDEX_H).Clone());
		c.Series.put(ABMaterial.CHART_SERIEINDEX_I, Series.get(ABMaterial.CHART_SERIEINDEX_I).Clone());
		c.Series.put(ABMaterial.CHART_SERIEINDEX_J, Series.get(ABMaterial.CHART_SERIEINDEX_J).Clone());
		c.Series.put(ABMaterial.CHART_SERIEINDEX_K, Series.get(ABMaterial.CHART_SERIEINDEX_K).Clone());
		c.Series.put(ABMaterial.CHART_SERIEINDEX_L, Series.get(ABMaterial.CHART_SERIEINDEX_L).Clone());
		c.Series.put(ABMaterial.CHART_SERIEINDEX_M, Series.get(ABMaterial.CHART_SERIEINDEX_M).Clone());
		c.Series.put(ABMaterial.CHART_SERIEINDEX_N, Series.get(ABMaterial.CHART_SERIEINDEX_N).Clone());
		c.Series.put(ABMaterial.CHART_SERIEINDEX_O, Series.get(ABMaterial.CHART_SERIEINDEX_O).Clone());
		c.Series.put(ABMaterial.CHART_SERIEINDEX_Z, Series.get(ABMaterial.CHART_SERIEINDEX_Z).Clone());		
		return c;
	}	
	
	/**
	 * Can not be created within B4J, But its properties and methods are available through another ABM class.
	 */
	public class ThemeSerie implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7055738293308800199L;
		public String Color=MainColor;
		public String ColorIntensity="";
		public int LineStrokeWidthPx=5;
		public String LineCap="round";
		public int LinePointStrokeWidthPx=20;
		public String LinePointCap="round";
		public int BarStrokeWidthPx=10;
			
		@Hide
		public ThemeSerie Clone() {
			ThemeSerie c = new ThemeSerie();
			c.Color = Color;
			c.ColorIntensity=ColorIntensity;
			c.LineStrokeWidthPx=LineStrokeWidthPx;
			c.LinePointStrokeWidthPx=LinePointStrokeWidthPx;
			c.LineCap=LineCap;
			c.LinePointCap=LinePointCap;	
			c.BarStrokeWidthPx=BarStrokeWidthPx;
			return c;
		}
		
		
	}
}
