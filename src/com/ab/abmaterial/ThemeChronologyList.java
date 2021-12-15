package com.ab.abmaterial;

import java.util.HashMap;
import java.util.Map;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeChronologyList implements java.io.Serializable {
	private static final long serialVersionUID = 2715599145898270873L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	protected Map<String, ThemeChronologyBadge> Badges = new HashMap<String, ThemeChronologyBadge>();
		
	public void AddChronologyBadgeTheme(String themeName) {
		Badges.put(themeName.toLowerCase(), new ThemeChronologyBadge(themeName));
	}
	
	public ThemeChronologyBadge ChronologyBadge(String themeName) {
		ThemeChronologyBadge c = Badges.getOrDefault(themeName.toLowerCase(), null);
		if (c==null) {
			c = new ThemeChronologyBadge("default");
		}
		return c;
	}
	
	ThemeChronologyList() {
		ThemeChronologyBadge def = new ThemeChronologyBadge();
		Badges.put("default", def);
	}
	
	ThemeChronologyList(String themeName) {
		ThemeChronologyBadge def = new ThemeChronologyBadge();
		Badges.put("default", def);
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;
		ChronologyBadge("default").Colorize(MainColor);
	}
	
	@Hide
	public ThemeChronologyList Clone() {
		ThemeChronologyList c = new ThemeChronologyList();
		c.MainColor = MainColor;
		c.ThemeName=ThemeName;
		for (String k: Badges.keySet()) {			
			c.Badges.put(k, Badges.get(k).Clone());
		}
		return c;
	}
	
	/**
	 * Can not be created within B4J, But its properties and methods are available through another ABM class.
	 */
	public static class ThemeChronologyBadge implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -9118353761601165646L;
		protected String ThemeName="default";
		protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
		public String ContentBackColor=ABMaterial.COLOR_TRANSPARENT;
		public String ContentBackColorIntensity="";
		public String BackColor=MainColor;
		public String BackColorIntensity=ABMaterial.INTENSITY_NORMAL;
		public String IconName="";
		public String IconAwesomeExtra="";
		public String IconColor=ABMaterial.COLOR_WHITE;
		public String IconColorIntensity=ABMaterial.INTENSITY_NORMAL;
		public boolean ColorizePointer=false;
		
		ThemeChronologyBadge() {
			
		}
		
		ThemeChronologyBadge(String themeName) {
			this.ThemeName = themeName;
		}
		
		public void Colorize(String color) {
			MainColor = color;
			BackColor = MainColor;
		}
		
		@Hide
		public ThemeChronologyBadge Clone() {
			ThemeChronologyBadge c = new ThemeChronologyBadge();
			c.MainColor = MainColor;
			c.ThemeName=ThemeName;
			c.BackColor = BackColor;
			c.BackColorIntensity=BackColorIntensity;
			c.IconName=IconName;
			c.IconAwesomeExtra = IconAwesomeExtra;
			c.IconColor=IconColor;
			c.IconColorIntensity=IconColorIntensity;
			c.ColorizePointer=ColorizePointer;
			c.ContentBackColor = ContentBackColor;
			c.ContentBackColorIntensity = ContentBackColorIntensity;
			return c;
		}
	}
}
