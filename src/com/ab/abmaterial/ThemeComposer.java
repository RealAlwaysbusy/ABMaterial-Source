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
public class ThemeComposer implements java.io.Serializable {	
	private static final long serialVersionUID = -8246011095443736779L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String BackgroundColor=ABMaterial.COLOR_TRANSPARENT;
	public String BackgroundColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String BorderRadius="8px";
	public String BorderWidth="2px";
	public String BorderStyle="solid";
	public String BorderColor=ABMaterial.COLOR_GREY;
	public String BorderColorIntensity=ABMaterial.INTENSITY_LIGHTEN2;
	
	protected Map<String,ThemeComposerBlock> Blocks = new LinkedHashMap<String,ThemeComposerBlock>();
	
	public void AddBlockTheme(String themeName) {
		Blocks.put(themeName.toLowerCase(), new ThemeComposerBlock(themeName));
	}
	
	public ThemeComposerBlock Block(String themeName) {
		ThemeComposerBlock c = Blocks.getOrDefault(themeName.toLowerCase(), null);
		if (c==null) {
			c = new ThemeComposerBlock();
		}
		return c;
	}
	
	ThemeComposer() {
		
	}
	
	ThemeComposer(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;		
		for (Entry<String,ThemeComposerBlock> entry: Blocks.entrySet()) {
			entry.getValue().Colorize(color);
		}
	}
	
	@Hide
	public void AddDefaultBlock() {
		ThemeComposerBlock bl = new ThemeComposerBlock("default");
		Blocks.put("default", bl);
	}
	
	@Hide
	public ThemeComposer Clone() {
		ThemeComposer c = new ThemeComposer();
		c.MainColor = MainColor;
		c.ThemeName=ThemeName;
		c.BackgroundColor=BackgroundColor;
		c.BackgroundColorIntensity=BackgroundColorIntensity;
		c.BorderRadius=BorderRadius;
		c.BorderWidth=BorderWidth;
		c.BorderStyle=BorderStyle;
		c.BorderColor=BorderColor;
		c.BorderColorIntensity=BorderColorIntensity;
		
		for (Entry<String,ThemeComposerBlock> entry: Blocks.entrySet()) {
			c.Blocks.put(entry.getKey(), entry.getValue().Clone());
		}			
		return c;
	}
	
	public class ThemeComposerBlock implements java.io.Serializable {
		private static final long serialVersionUID = 8564828715955839730L;
		
		protected String ThemeName="default";
		protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
		public String LabelColor=ABMaterial.COLOR_GREY;
		public String LabelColorIntensity=ABMaterial.INTENSITY_LIGHTEN2;
		public String ButtonColor=ABMaterial.COLOR_GREY;
		public String ButtonColorIntensity=ABMaterial.INTENSITY_LIGHTEN1;
		public String SidebarBulletColor=MainColor;
		public String SidebarBulletColorIntensity=ABMaterial.INTENSITY_NORMAL;
		public String SidebarBulletBorderColor=ABMaterial.COLOR_GREY;
		public String SidebarBulletBorderColorIntensity=ABMaterial.INTENSITY_LIGHTEN2;
		public String BackgroundColor=ABMaterial.COLOR_TRANSPARENT;
		public String BackgroundColorIntensity=ABMaterial.INTENSITY_NORMAL;
		public String BorderRadius="8px";
		public String BorderWidth="1px";
		public String BorderStyle="solid";
		public String BorderColor=ABMaterial.COLOR_GREY;
		public String BorderColorIntensity=ABMaterial.INTENSITY_LIGHTEN2;
		public String SidebarColor=ABMaterial.COLOR_GREY;
		public String SidebarColorIntensity=ABMaterial.INTENSITY_LIGHTEN2;
					
		ThemeComposerBlock() {
			
		}
		
		ThemeComposerBlock(String themeName) {
			this.ThemeName = themeName;
		}
		
		public void Colorize(String color) {
			MainColor = color;
			SidebarBulletColor = color;
		}
		
		@Hide
		public ThemeComposerBlock Clone() {
			ThemeComposerBlock c = new ThemeComposerBlock();
			c.MainColor = MainColor;
			c.ThemeName=ThemeName;
			
			c.LabelColor=LabelColor;
			c.LabelColorIntensity=LabelColorIntensity;
			c.ButtonColor=ButtonColor;
			c.ButtonColorIntensity=ButtonColorIntensity;
			c.SidebarBulletColor=SidebarBulletColor;
			c.SidebarBulletColorIntensity=SidebarBulletColorIntensity;
			c.SidebarBulletBorderColor=SidebarBulletBorderColor;
			c.SidebarBulletBorderColorIntensity=SidebarBulletBorderColorIntensity;
			c.BackgroundColor=BackgroundColor;
			c.BackgroundColorIntensity=BackgroundColorIntensity;
			c.BorderRadius=BorderRadius;
			c.BorderWidth=BorderWidth;
			c.BorderStyle=BorderStyle;
			c.BorderColor=BorderColor;
			c.BorderColorIntensity=BorderColorIntensity;
			c.SidebarColor=SidebarColor;
			c.SidebarColorIntensity=SidebarColorIntensity;
			
			return c;
		}
	}
}
