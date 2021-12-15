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
public class ThemeChat implements java.io.Serializable {
	private static final long serialVersionUID = -864324152317012344L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;	
	public String BackgroundColor="white";
	public String BackgroundColorIntensity="";
	protected Map<String,ThemeChatBubble> Bubbles = new LinkedHashMap<String,ThemeChatBubble>();
	
	ThemeChat() {
		ThemeChatBubble def = new ThemeChatBubble();
		Bubbles.put(def.ThemeName, def);
	}
	
	ThemeChat(String themeName) {
		ThemeChatBubble def = new ThemeChatBubble();
		Bubbles.put(def.ThemeName, def);
		this.ThemeName = themeName;		
	}
	
	public void Colorize(String color) {
		MainColor = color;	
	}
	
	public void AddBubble(String themeName) {
		ThemeChatBubble bub = new ThemeChatBubble();
		bub.ThemeName = themeName.toLowerCase();
		Bubbles.put(bub.ThemeName, bub);
	}
	
	public ThemeChatBubble Bubble(String themeName) {
		return Bubbles.getOrDefault(themeName.toLowerCase(), null);
	}
	
	@Hide
	public ThemeChat Clone() {
		ThemeChat c = new ThemeChat();
		
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;			
		c.BackgroundColor=BackgroundColor;
		c.BackgroundColorIntensity=BackgroundColorIntensity;		
		c.Bubbles = new LinkedHashMap<String,ThemeChatBubble>();
		for (Entry<String,ThemeChatBubble> entry: Bubbles.entrySet()) {
			c.Bubbles.put(entry.getKey(), entry.getValue().Clone());
		}
		return c;
	}
	
	/**
	 * Can not be created within B4J, But its properties and methods are available through another ABM class.
	 */
	public class ThemeChatBubble  implements java.io.Serializable {
		private static final long serialVersionUID = 8166117366337117768L;
		
		protected String ThemeName="default";
		public String Color=ABMaterial.COLOR_GREY;
		public String ColorIntensity=ABMaterial.INTENSITY_LIGHTEN2;
		public String TextColor="black";
		public String TextColorIntensity="";
		public String ExtraTextColor=ABMaterial.COLOR_GREY;
		public String ExtraTextColorIntensity=ABMaterial.INTENSITY_LIGHTEN1;
		
		ThemeChatBubble() {
			
		}
		
		public void Colorize(String color) {
		
		}
		
		@Hide
		public ThemeChatBubble Clone() {
			ThemeChatBubble c = new ThemeChatBubble();
			
			c.ThemeName=ThemeName;		
			c.Color=Color;
			c.ColorIntensity=ColorIntensity;
			c.TextColor=TextColor;
			c.TextColorIntensity=TextColorIntensity;
			c.ExtraTextColor=ExtraTextColor;
			c.ExtraTextColorIntensity=ExtraTextColorIntensity;
			return c;
		}
		
	}
}
