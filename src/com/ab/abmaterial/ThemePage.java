package com.ab.abmaterial;


import java.util.HashMap;
import java.util.Map;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemePage implements java.io.Serializable {
	private static final long serialVersionUID = -8156646279775273889L;
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	public String BackColor=ABMaterial.COLOR_TRANSPARENT;
	public String BackColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String PlaceHolderColor=ABMaterial.COLOR_GREY;
	public String PlaceHolderColorIntensity=ABMaterial.INTENSITY_NORMAL;
	protected Map<String,ExtraColor> ExtraColors = new HashMap<String,ExtraColor>();
	protected Map<String,Section> Sections = new HashMap<String,Section>();
	public String ConnectedIndicatorColor=ABMaterial.COLOR_GREEN;
	public String ConnectedIndicatorColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String DisconnectedIndicatorColor=ABMaterial.COLOR_RED;
	public String DisconnectedIndicatorColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String SearchMarkBackColor=ABMaterial.COLOR_YELLOW;
	public String SearchMarkBackColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public String SearchMarkForeColor=ABMaterial.COLOR_BLACK;
	public String SearchMarkForeColorIntensity=ABMaterial.INTENSITY_NORMAL;
		
	@Hide
	public class ExtraColor {
		protected String Name="";
		protected String Intensity="";		
		protected double Opacity=0;
		protected String HexValue;
		
		public ExtraColor Clone() {
			ExtraColor e = new ExtraColor();
			e.Name = Name;
			e.Intensity = Intensity;
			e.Opacity = Opacity;
			e.HexValue = HexValue;
			return e;
		}
	}
	
	@Hide
	public class Section {
		protected String Name="default";
		protected String NavigationButtonColor="black";
		protected String NavigationButtonColorIntensity="";		
		protected String NavigationMenuColor="black";
		protected String NavigationMenuColorIntensity="";		
		protected String NavigationButtonType="section04";
		
		public Section Clone() {
			Section s = new Section();
			s.Name = Name;
			s.NavigationButtonColor = NavigationButtonColor;
			s.NavigationButtonColorIntensity = NavigationButtonColorIntensity;
			s.NavigationMenuColor = NavigationMenuColor;
			s.NavigationMenuColorIntensity = NavigationMenuColorIntensity;
			s.NavigationButtonType=NavigationButtonType;
			return s;
		}
	}
	
	protected Section GetSection(String sectionName) {
		return Sections.getOrDefault(sectionName.toLowerCase(), new Section());
	}
	
	/**
	 * Use a new unique name per color range and an existing ABMaterial intensity constant. 
	 * hexValue e.g. #FFFFFF
	 * opacity: between 0 and 1
	 * 
	 */
	public void AddColorDefinition(String colorName, String colorIntensity, String hexValue, double opacity) {
		ExtraColor col = new ExtraColor();
		col.Name = colorName.toLowerCase();
		col.Intensity = colorIntensity.toLowerCase();
		col.HexValue = hexValue;
		col.Opacity = opacity;
		ExtraColors.put((col.Name + " " + col.Intensity).trim(), col);
	}
	
	public void AddSection(String themeSectionName, String navigationButtonColor, String navigationButtonColorIntensity, String navigationMenuColor, String navigationMenuColorIntensity) {
		Section s = new Section();
		s.Name = themeSectionName;
		s.NavigationButtonColor = navigationButtonColor;
		s.NavigationButtonColorIntensity = navigationButtonColorIntensity;
		s.NavigationMenuColor = navigationMenuColor;
		s.NavigationMenuColorIntensity = navigationMenuColorIntensity;
		s.NavigationButtonType = "section04";
		Sections.put(s.Name.toLowerCase(), s);
	}
	
	@Hide
	public ThemePage Clone() {
		ThemePage c = new ThemePage();
		c.MainColor=MainColor;
		c.BackColor = BackColor;
		c.BackColorIntensity = BackColorIntensity;
		c.PlaceHolderColor=PlaceHolderColor;
		c.PlaceHolderColorIntensity=PlaceHolderColorIntensity;
		c.ConnectedIndicatorColor=ConnectedIndicatorColor;
		c.ConnectedIndicatorColorIntensity=ConnectedIndicatorColorIntensity;
		c.DisconnectedIndicatorColor=DisconnectedIndicatorColor;
		c.DisconnectedIndicatorColorIntensity=DisconnectedIndicatorColorIntensity;
		c.SearchMarkBackColor=SearchMarkBackColor;
		c.SearchMarkBackColorIntensity=SearchMarkBackColorIntensity;
		c.SearchMarkForeColor=SearchMarkForeColor;
		c.SearchMarkForeColorIntensity=SearchMarkForeColorIntensity;
		
		for (Map.Entry<String, Section> entry : Sections.entrySet()) {
			c.Sections.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ExtraColor> entry : ExtraColors.entrySet()) {
			c.ExtraColors.put(entry.getKey(), entry.getValue().Clone());
		}
		return c;
	}
	
	
	
	public void Colorize(String color) {
		MainColor = color;						
	}	
	
}
