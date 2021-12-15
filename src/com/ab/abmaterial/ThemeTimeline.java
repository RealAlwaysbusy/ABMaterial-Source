package com.ab.abmaterial;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeTimeline implements java.io.Serializable {
	private static final long serialVersionUID = 9171368303360530312L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String DefaultBackgroundColor=ABMaterial.COLOR_WHITE;
	public String DefaultBackgroundColorIntensity=ABMaterial.INTENSITY_NORMAL;
	
	protected Map<String,ThemeTimelineSlide> Slides = new LinkedHashMap<String,ThemeTimelineSlide>();
	
	public String ZDepth="z-depth-1";	
	
	public void AddSlideTheme(String themeName) {
		Slides.put(themeName.toLowerCase(), new ThemeTimelineSlide(themeName));
	}
	
	public ThemeTimelineSlide Slide(String themeName) {
		ThemeTimelineSlide c = Slides.getOrDefault(themeName.toLowerCase(), null);
		if (c==null) {
			c = new ThemeTimelineSlide();
		}
		return c;
	}
	
	ThemeTimeline() {
		
	}
	
	ThemeTimeline(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;			
	}
	
	@Hide
	public ThemeTimeline Clone() {
		ThemeTimeline c = new ThemeTimeline();
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;
		c.DefaultBackgroundColor = DefaultBackgroundColor;
		c.DefaultBackgroundColorIntensity = DefaultBackgroundColorIntensity;
		c.ZDepth=ZDepth;
		for (Entry<String,ThemeTimelineSlide> entry: Slides.entrySet()) {
			c.Slides.put(entry.getKey(), entry.getValue().Clone());
		}			
		return c;
	}
	
	/**
	 * Can not be created within B4J, But its properties and methods are available through another ABM class.
	 */
	public class ThemeTimelineSlide {
		protected String ThemeName="default";	
		public String BackgroundColor=ABMaterial.COLOR_WHITE;
		public String BackgroundColorIntensity=ABMaterial.INTENSITY_NORMAL;
		
		ThemeTimelineSlide() {
			
		}
		
		ThemeTimelineSlide(String themeName) {
			this.ThemeName = themeName;
		}		
		
		@Hide
		public ThemeTimelineSlide Clone() {
			ThemeTimelineSlide c = new ThemeTimelineSlide();
			c.ThemeName=ThemeName;
			c.BackgroundColor = BackgroundColor;
			c.BackgroundColorIntensity = BackgroundColorIntensity;
			return c;
		}
	}
}
