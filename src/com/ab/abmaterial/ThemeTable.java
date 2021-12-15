package com.ab.abmaterial;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeTable implements java.io.Serializable {
	private static final long serialVersionUID = 5613274063451668648L;
	public String ZDepth="";
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	protected Map<String, TableCell> Cells = new HashMap<String, TableCell>();
	public String BackColor="transparent";
	public String BackColorIntensity="";
	
	public void AddCellTheme(String themeName) {
		TableCell c = new TableCell(themeName);
		c.Colorize(MainColor);
		Cells.put(themeName.toLowerCase(), c);
	}
	
	public TableCell Cell(String themeName) {
		TableCell c = Cells.getOrDefault(themeName.toLowerCase(), null);
		if (c==null) {
			c = new TableCell();
		}
		return c;
	}
	
	ThemeTable() {
		Cells.put("default", new TableCell("default"));
		TableCell c = new TableCell("defaultheaderfooter");
		c.BackColor = MainColor;
		c.ForeColor = ABMaterial.COLOR_WHITE;
		Cells.put("defaultheaderfooter", c);
		
	}
	
	ThemeTable(String themeName) {
		this.ThemeName = themeName;
		Cells.put("default", new TableCell("default"));
		TableCell c = new TableCell("defaultheaderfooter");
		c.BackColor = MainColor;
		c.ForeColor = ABMaterial.COLOR_WHITE;
		Cells.put("defaultheaderfooter", c);
	}
	
	public void Colorize(String color) {
		MainColor = color;	
		for (Entry<String, TableCell> entry: Cells.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		Cell("defaultheaderfooter").BackColor = MainColor;
	}
	
	@Hide
	public ThemeTable Clone() {
		ThemeTable c = new ThemeTable();
		
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;
		c.BackColor=BackColor;
		c.BackColorIntensity=BackColorIntensity;
		c.ZDepth=ZDepth;
		for (String k: Cells.keySet()) {			
			c.Cells.put(k, Cells.get(k).Clone());
		}
		return c;
	}
	
	/**
	 * Can not be created within B4J, But its properties and methods are available through another ABM class.
	 */
	public static class TableCell implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 6295840974257562712L;
		protected String ThemeName="default";
		protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
		public String BackColor=ABMaterial.COLOR_TRANSPARENT;
		public String BackColorIntensity=ABMaterial.INTENSITY_NORMAL;
		public String ForeColor=ABMaterial.COLOR_BLACK;
		public String ForeColorIntensity=ABMaterial.INTENSITY_NORMAL;
		public String ActiveBackColor=MainColor;
		public String ActiveBackColorIntensity=ABMaterial.INTENSITY_LIGHTEN3;
		public String ZDepth="";
		public String BorderColor=ABMaterial.COLOR_TRANSPARENT;
		public String BorderColorIntensity=ABMaterial.INTENSITY_NORMAL;
		public int BorderWidth=0;
		public String Align="left-align";	
		public String VerticalAlign="middle";
		public boolean IsEditable = false;
		public String InputMask="";
		public boolean AllowEnterKey=true;
		public int FontSize=15;
		public String PaddingLeft=""; //"0rem";
		public String PaddingRight=""; //"0rem";
		public String PaddingTop=""; //"0rem";
		public String PaddingBottom=""; //"0rem";
		public String MarginTop=""; //"0px";
		public String MarginBottom=""; //"20px";
		public String MarginLeft=""; //"auto";
		public String MarginRight=""; //"auto";
		
		public boolean UseStrikethrough=false;
		public String StrikethroughColor=ABMaterial.COLOR_RED;
		public String StrikethroughColorIntensity=ABMaterial.INTENSITY_NORMAL;
		
		public String DraggableHeaderSeperatorColor=ABMaterial.COLOR_TRANSPARENT;
		public String DraggableHeaderSeperatorColorIntensity="";
				
		public String SameValueForeColor="";
		public String SameValueForeColorIntensity="";
		
		public String WarningBulletColor=ABMaterial.COLOR_TRANSPARENT;
		public String WarningBulletColorIntensity="";
		
		public String ExtraStyle="";
				
		TableCell() {
			
		}
		
		TableCell(String themeName) {
			this.ThemeName = themeName;
		}
		
		public void Colorize(String color) {
			MainColor = color;	
			ActiveBackColor=MainColor;
		}
		
		@Hide
		public TableCell Clone() {
			TableCell c = new TableCell();
			c.MainColor=MainColor;
			c.ThemeName=ThemeName;
			c.BackColor = BackColor;
			c.BackColorIntensity = BackColorIntensity;
			c.ForeColor = ForeColor;
			c.ForeColorIntensity = ForeColorIntensity;
			c.ActiveBackColor = ActiveBackColor;
			c.ActiveBackColorIntensity = ActiveBackColorIntensity;
			c.ZDepth=ZDepth;
			c.BorderColor = BorderColor;
			c.BorderColorIntensity = BorderColorIntensity;
			c.BorderWidth=BorderWidth;
			c.Align=Align;
			c.VerticalAlign=VerticalAlign;
			c.IsEditable=IsEditable;
			c.FontSize=FontSize;
			c.InputMask=InputMask;
			c.AllowEnterKey=AllowEnterKey;
			c.UseStrikethrough=UseStrikethrough;
			c.StrikethroughColor=StrikethroughColor;
			c.StrikethroughColorIntensity=StrikethroughColorIntensity;
			c.PaddingLeft=PaddingLeft;
			c.PaddingRight=PaddingRight;
			c.PaddingTop=PaddingTop;
			c.PaddingBottom=PaddingBottom;
			c.MarginTop=MarginTop;
			c.MarginBottom=MarginBottom;
			c.MarginLeft=MarginLeft;
			c.MarginRight=MarginRight;
			
			c.SameValueForeColor=SameValueForeColor;
			c.SameValueForeColorIntensity=SameValueForeColorIntensity;
			
			c.WarningBulletColor=WarningBulletColor;
			c.WarningBulletColorIntensity=WarningBulletColorIntensity;
			
			c.DraggableHeaderSeperatorColor=DraggableHeaderSeperatorColor;
			c.DraggableHeaderSeperatorColorIntensity=DraggableHeaderSeperatorColorIntensity;
			
			c.ExtraStyle=ExtraStyle;
			return c;
		}
				
	}
	

}
