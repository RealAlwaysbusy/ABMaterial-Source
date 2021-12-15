package com.ab.abmaterial;

import java.util.HashMap;
import java.util.Map;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeList implements java.io.Serializable {
	
	private static final long serialVersionUID = 2267159227379583914L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String BackColor="transparent";
	public String BackColorIntensity="";
	public String ItemBackColor="white";
	public String ItemBackColorIntensity="";		
	public String SubItemBackColor="white";
	public String SubItemBackColorIntensity="";	
	public String ItemDividerColor="grey";
	public String ItemDividerColorIntensity="";		
	public String SubItemDividerColor="grey";
	public String SubItemDividerColorIntensity="";
	public String ItemHoverColor="grey";
	public String ItemHoverColorIntensity=ABMaterial.INTENSITY_LIGHTEN3;
	public String SubItemHoverColor="grey";
	public String SubItemHoverColorIntensity=ABMaterial.INTENSITY_LIGHTEN3;
	public String ItemActiveColor=MainColor;
	public String ItemActiveColorIntensity=ABMaterial.INTENSITY_LIGHTEN2;
	public String SubItemActiveColor=MainColor;
	public String SubItemActiveColorIntensity=ABMaterial.INTENSITY_LIGHTEN2;
	public String BorderColor=ABMaterial.COLOR_GREY;
	public String BorderColorIntensity=ABMaterial.INTENSITY_LIGHTEN2;
	public int BorderWidth=1;
	
	public String BorderStyle="";
	
	protected Map<String, ItemTheme> Items = new HashMap<String, ItemTheme>();
	
	public String ZDepth="";
	
	public void AddItemTheme(String themeName) {
		Items.put(themeName.toLowerCase(), new ItemTheme(themeName));
	}
	
	public ItemTheme Item(String themeName) {
		ItemTheme c = Items.getOrDefault(themeName.toLowerCase(), null);
		if (c==null) {
			c = new ItemTheme();
		}
		return c;
	}
	
	ThemeList() {
		
	}
	
	ThemeList(String themeName) {
		this.ThemeName = themeName;		
	}
	
	public void Colorize(String color) {
		MainColor = color;			
		ItemActiveColor = MainColor;
		SubItemActiveColor = MainColor;
		for (String k: Items.keySet()) {			
			Items.get(k).Colorize(MainColor);
		}
	}
	
	@Hide
	public ThemeList Clone() {
		ThemeList l = new ThemeList();
		l.MainColor=MainColor;
		l.ThemeName=ThemeName;
		l.BackColor=BackColor;
		l.BackColorIntensity=BackColorIntensity;
		l.ItemBackColor=ItemBackColor;
		l.ItemBackColorIntensity=ItemBackColorIntensity;	
		l.SubItemBackColor=SubItemBackColor;
		l.SubItemBackColorIntensity=SubItemBackColorIntensity;		
		l.ItemDividerColor=ItemDividerColor;
		l.ItemDividerColorIntensity=ItemDividerColorIntensity;
		l.SubItemDividerColor=SubItemDividerColor;
		l.SubItemDividerColorIntensity=SubItemDividerColorIntensity;
		l.ZDepth=ZDepth;
		l.ItemHoverColor=ItemHoverColor;
		l.ItemHoverColorIntensity=ItemHoverColorIntensity;
		l.SubItemHoverColor=SubItemHoverColor;
		l.SubItemHoverColorIntensity=SubItemHoverColorIntensity;
		l.ItemActiveColor=ItemActiveColor;
		l.ItemActiveColorIntensity=ItemActiveColorIntensity;
		l.SubItemActiveColor=SubItemActiveColor;
		l.SubItemActiveColorIntensity=SubItemActiveColorIntensity;
		l.BorderColor = BorderColor;
		l.BorderColorIntensity = BorderColorIntensity;
		l.BorderWidth=BorderWidth;
		
		l.BorderStyle=BorderStyle;
		
		for (String k: Items.keySet()) {			
			l.Items.put(k, Items.get(k).Clone());
		}
		
		return l;
	}
	
	public static class ItemTheme implements java.io.Serializable {
		
		private static final long serialVersionUID = -3146194694495322419L;
		protected String ThemeName="default";
		public String BackColor="white";
		public String BackColorIntensity="";
		protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
		public String HoverColor="grey";
		public String HoverColorIntensity=ABMaterial.INTENSITY_LIGHTEN3;
		public String ActiveColor=MainColor;
		public String ActiveColorIntensity=ABMaterial.INTENSITY_LIGHTEN2;
				
		ItemTheme() {
			
		}
		
		ItemTheme(String themeName) {
			this.ThemeName = themeName;
		}
		
		public void Colorize(String color) {
			MainColor = color;
			ActiveColor = MainColor;
		}
		
		@Hide
		public ItemTheme Clone() {
			ItemTheme c = new ItemTheme();
			c.MainColor = MainColor;
			c.ThemeName=ThemeName;
			c.BackColor = BackColor;
			c.BackColorIntensity = BackColorIntensity;
			c.HoverColor=HoverColor;
			c.HoverColorIntensity=HoverColorIntensity;
			c.ActiveColor=ActiveColor;
			c.ActiveColorIntensity=ActiveColorIntensity;

			return c;
		}
	}
}
