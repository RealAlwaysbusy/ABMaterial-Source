package com.ab.abmaterial;

import java.util.HashMap;
import java.util.Map;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeTreeTable implements java.io.Serializable {
	private static final long serialVersionUID = 8812674438963701541L;
	public String ZDepth="";
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String BackColor=ABMaterial.COLOR_BLACK;
	public String BackColorIntensity=ABMaterial.INTENSITY_NORMAL;
	protected Map<String, TreeTableCell> Cells = new HashMap<String, TreeTableCell>();
	protected Map<String, TreeIcon> TreeIcons = new HashMap<String, TreeIcon>();
	
	public void AddTreeIconColorTheme(String themeName) {
		TreeIcons.put(themeName.toLowerCase(), new TreeIcon(themeName));
	}
	
	public TreeIcon TreeIconColor(String themeName) {
		TreeIcon c = TreeIcons.getOrDefault(themeName.toLowerCase(), null);
		if (c==null) {
			c = new TreeIcon();
		}
		return c;
	}
	
	public void AddCellTheme(String themeName) {
		Cells.put(themeName.toLowerCase(), new TreeTableCell(themeName));
	}
	
	public TreeTableCell Cell(String themeName) {
		TreeTableCell c = Cells.getOrDefault(themeName.toLowerCase(), null);
		if (c==null) {
			c = new TreeTableCell();
		}
		return c;
	}
	
	ThemeTreeTable() {
		
	}
	
	ThemeTreeTable(String themeName) {
		this.ThemeName = themeName;
	}
	
	public void Colorize(String color) {
		MainColor = color;			
	}
	
	@Hide
	public ThemeTreeTable Clone() {
		ThemeTreeTable c = new ThemeTreeTable();
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;
		c.BackColor=BackColor;
		c.BackColorIntensity=BackColorIntensity;
		c.ZDepth=ZDepth;
		for (String k: Cells.keySet()) {			
			c.Cells.put(k, Cells.get(k).Clone());
		}
		for (String k: TreeIcons.keySet()) {			
			c.TreeIcons.put(k, TreeIcons.get(k).Clone());
		}
		return c;
	}
	
	/**
	 * Can not be created within B4J, But its properties and methods are available through another ABM class.
	 */
	public static class TreeTableCell {
		protected String ThemeName="default";
		public String BackColor=ABMaterial.COLOR_TRANSPARENT;
		public String BackColorIntensity=ABMaterial.INTENSITY_NORMAL;
		public String ForeColor=ABMaterial.COLOR_BLACK;
		public String ForeColorIntensity=ABMaterial.INTENSITY_NORMAL;
		public String ZDepth="";
		public String BorderColor=ABMaterial.COLOR_TRANSPARENT;
		public String BorderColorIntensity=ABMaterial.INTENSITY_NORMAL;
		public int BorderWidth=0;
		public String Align="left-align";	
		public String VerticalAlign="middle";
		public boolean IsEditable = false;
		
		TreeTableCell() {
			
		}
		
		TreeTableCell(String themeName) {
			this.ThemeName = themeName;
		}
		
		@Hide
		public TreeTableCell Clone() {
			TreeTableCell c = new TreeTableCell();
			c.ThemeName=ThemeName;
			c.BackColor = BackColor;
			c.BackColorIntensity = BackColorIntensity;
			c.ForeColor = ForeColor;
			c.ForeColorIntensity = ForeColorIntensity;
			c.ZDepth=ZDepth;
			c.BorderColor = BorderColor;
			c.BorderColorIntensity = BorderColorIntensity;
			c.BorderWidth=BorderWidth;
			c.Align=Align;
			c.VerticalAlign=VerticalAlign;
			c.IsEditable=IsEditable;
			return c;
		}
	}
	
	/**
	 * Can not be created within B4J, But its properties and methods are available through another ABM class.
	 */
	public static class TreeIcon {
		protected String ThemeName="default";
		public String ForeColor=ABMaterial.COLOR_BLACK;
		public String ForeColorIntensity=ABMaterial.INTENSITY_NORMAL;
		
		TreeIcon() {
			
		}
		
		TreeIcon(String themeName) {
			this.ThemeName = themeName;
		}
		
		@Hide
		public TreeIcon Clone() {
			TreeIcon c = new TreeIcon();
			c.ThemeName=ThemeName;
			c.ForeColor = ForeColor;
			c.ForeColorIntensity = ForeColorIntensity;
			return c;
		}
	}
	
}

