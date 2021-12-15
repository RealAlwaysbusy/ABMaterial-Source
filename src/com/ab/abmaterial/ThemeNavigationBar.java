package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeNavigationBar implements java.io.Serializable {
	private static final long serialVersionUID = 3489145912071728870L;
	protected String ThemeName="default";	
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	
	public String TopBarBackColor=MainColor;
	public String TopBarBackColorIntensity="";
	public String TopBarForeColor="white";
	public String TopBarForeColorIntensity="";	
	public String TopBarTitleColor="white";
	public String TopBarTitleColorIntensity="";	
	
	public String TopBarActiveForeColor="white";
	public String TopBarActiveForeColorIntensity="";
	
	public String TopBarWavesEffect="waves-effect";
	public boolean TopBarWavesCircle=false;
	public boolean TopBarBold=false;
	protected String TopBarTextSize=ABMaterial.SIZE_A;	
	public String TopBarFontSize="";
	public String TopBarIconSize="1.6rem";
	public String TopBarZDepth="";
	
	public String TopBarSubBackColor="white";
	public String TopBarSubBackColorIntensity="";
	public String TopBarSubForeColor="black";
	public String TopBarSubForeColorIntensity="";
	public String TopBarSubDividerColor=MainColor;
	public String TopBarSubDividerColorIntensity="";
	public String TopBarSubHoverBackColor="grey";
	public String TopBarSubHoverBackColorIntensity=ABMaterial.INTENSITY_LIGHTEN3;
	
	// if set to true then the topbar title will not use the 85% container rule.
	public boolean TopBarWide=false;
	public String TopBarTitleVisibility=ABMaterial.VISIBILITY_ALL;
	
	public String SideBarBackgroundColor="white";
	public String SideBarBackgroundColorIntensity="";
	public String SideBarBackColor="white";
	public String SideBarBackColorIntensity="";
	public String SideBarForeColor="black";
	public String SideBarForeColorIntensity="";	
	public String SideBarWavesEffect="waves-effect";
	public boolean SideBarWavesCircle=false;
	public boolean SideBarBold=false;
	public String SideBarSubBackColor="white";
	public String SideBarSubBackColorIntensity="";
	public String SideBarSubForeColor="black";
	public String SideBarSubForeColorIntensity="";		
	public String SideBarSubWavesEffect="waves-effect";
	public boolean SideBarSubWavesCircle=false;
	public boolean SideBarSubBold=false;
	public String SideBarActiveForeColor="black";
	public String SideBarActiveForeColorIntensity="";
	public String SideBarSubActiveForeColor="black";
	public String SideBarSubActiveForeColorIntensity="";
	protected String SideBarTextSize=ABMaterial.SIZE_A;
	protected String SideBarSubTextSize=ABMaterial.SIZE_A;
	public String SideBarFontSize="";
	public String SideBarSubFontSize="";
	public String SideBarIconSize="1.6rem";
	public String SideBarSubIconSize="1.6rem";
	public String SideBarDividerColor=MainColor;
	public String SideBarDividerColorIntensity="";
	public String SideBarSubDividerColor=MainColor;
	public String SideBarSubDividerColorIntensity="";
	public String SideBarSubHoverBackColor="grey";
	public String SideBarSubHoverBackColorIntensity=ABMaterial.INTENSITY_LIGHTEN3;
	public String SideBarHoverBackColor="grey";
	public String SideBarHoverBackColorIntensity=ABMaterial.INTENSITY_LIGHTEN3;
	public String SideBarZDepth="";
	
	public String LogoBackColor=MainColor;
	public String LogoBackColorIntensity="";
	
	public String SideBarSubHoverForeColor="black"; 
	public String SideBarSubHoverForeColorIntensity="";
	public String SideBarSubActiveBackColor="grey";
	public String SideBarSubActiveBackColorIntensity=ABMaterial.INTENSITY_LIGHTEN3;
	public String SideBarHoverForeColor="black";
	public String SideBarHoverForeColorIntensity="";
	public String SideBarActiveBackColor="grey";
	public String SideBarActiveBackColorIntensity=ABMaterial.INTENSITY_LIGHTEN3;
	
	public String SideBarSubActiveHoverBackColor="grey";
	public String SideBarSubActiveHoverBackColorIntensity=ABMaterial.INTENSITY_LIGHTEN2;
	public String SideBarSubActiveHoverForeColor="black";
	public String SideBarSubActiveHoverForeColorIntensity="";	
	public double TopBarSubHoverOpacity=1;
	public String SideBarActiveHoverBackColor="grey";
	public String SideBarActiveHoverBackColorIntensity=ABMaterial.INTENSITY_LIGHTEN2;
	public String SideBarActiveHoverForeColor="black";
	public String SideBarActiveHoverForeColorIntensity="";
		
	public String LogoZDepth="z-depth-1";	
	
	ThemeNavigationBar() {
		
	}
	
	ThemeNavigationBar(String themeName) {
		this.ThemeName = themeName;		
	}
		
	public void Colorize(String color) {
		MainColor = color;			
		TopBarBackColor=MainColor;
		TopBarSubDividerColor = MainColor;
		SideBarDividerColor=MainColor;
		SideBarSubDividerColor=MainColor;
		LogoBackColor=MainColor;
	}
	
	@Hide
	public ThemeNavigationBar Clone() {
		ThemeNavigationBar c = new ThemeNavigationBar();
		
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;
		c.TopBarBackColor=TopBarBackColor;
		c.TopBarBackColorIntensity=TopBarBackColorIntensity;		
		c.TopBarForeColor=TopBarForeColor;
		c.TopBarForeColorIntensity=TopBarForeColorIntensity;	
		c.TopBarTitleColor=TopBarTitleColor;
		c.TopBarTitleColorIntensity=TopBarTitleColorIntensity;		
		c.TopBarWavesEffect=TopBarWavesEffect;
		c.TopBarWavesCircle=TopBarWavesCircle;
		c.TopBarBold=TopBarBold;
		c.SideBarBackColor=SideBarBackColor;
		c.SideBarBackColorIntensity=SideBarBackColorIntensity;		
		c.SideBarForeColor=SideBarForeColor;
		c.SideBarForeColorIntensity=SideBarForeColorIntensity;			
		c.SideBarWavesEffect=SideBarWavesEffect;
		c.SideBarWavesCircle=SideBarWavesCircle;
		c.SideBarBold=SideBarBold;
		c.SideBarSubBackColor=SideBarSubBackColor;
		c.SideBarSubBackColorIntensity=SideBarSubBackColorIntensity;		
		c.SideBarSubForeColor=SideBarSubForeColor;
		c.SideBarSubForeColorIntensity=SideBarSubForeColorIntensity;			
		c.SideBarSubWavesEffect=SideBarSubWavesEffect;
		c.SideBarSubWavesCircle=SideBarSubWavesCircle;
		c.SideBarSubBold=SideBarSubBold;
		c.TopBarTextSize=TopBarTextSize;
		c.SideBarTextSize=SideBarTextSize;
		c.SideBarSubTextSize=SideBarSubTextSize;
		c.TopBarActiveForeColor=TopBarActiveForeColor;
		c.TopBarActiveForeColorIntensity=TopBarActiveForeColorIntensity;
		c.TopBarFontSize=TopBarFontSize;
		
		c.TopBarWide=TopBarWide;
		c.TopBarTitleVisibility=TopBarTitleVisibility;
		
		c.SideBarFontSize=SideBarFontSize;
		c.SideBarSubFontSize=SideBarSubFontSize;
		c.TopBarIconSize=TopBarIconSize;
		c.SideBarIconSize=SideBarIconSize;
		c.SideBarSubIconSize=SideBarSubIconSize;
		
		c.SideBarActiveForeColor=SideBarActiveForeColor;
		c.SideBarActiveForeColorIntensity=SideBarActiveForeColorIntensity;
		c.SideBarSubActiveForeColor=SideBarSubActiveForeColor;
		c.SideBarSubActiveForeColorIntensity=SideBarSubActiveForeColorIntensity;
				
		c.TopBarSubBackColor=TopBarSubBackColor;
		c.TopBarSubBackColorIntensity=TopBarSubBackColorIntensity;
		c.TopBarSubForeColor=TopBarSubForeColor;
		c.TopBarSubForeColorIntensity=TopBarSubForeColorIntensity;
		c.TopBarSubDividerColor=TopBarSubDividerColor;
		c.TopBarSubDividerColorIntensity=TopBarSubDividerColorIntensity;
		c.TopBarSubHoverBackColor=TopBarSubHoverBackColor;
		c.TopBarSubHoverBackColorIntensity=TopBarSubHoverBackColorIntensity;
		c.TopBarSubHoverOpacity=TopBarSubHoverOpacity;
		
		c.SideBarDividerColor=SideBarDividerColor;
		c.SideBarDividerColorIntensity=SideBarDividerColorIntensity;
		c.SideBarSubDividerColor=SideBarSubDividerColor;
		c.SideBarSubDividerColorIntensity=SideBarSubDividerColorIntensity;
		
		c.SideBarSubHoverBackColor=SideBarSubHoverBackColor;
		c.SideBarSubHoverBackColorIntensity=SideBarSubHoverBackColorIntensity;
		
		c.SideBarHoverBackColor=SideBarHoverBackColor;
		c.SideBarHoverBackColorIntensity=SideBarHoverBackColorIntensity;
		
		c.TopBarZDepth=TopBarZDepth;
		c.SideBarZDepth=SideBarZDepth;
		c.LogoZDepth=LogoZDepth;
		c.LogoBackColor=LogoBackColor;
		c.LogoBackColorIntensity=LogoBackColorIntensity;
		
		c.SideBarBackgroundColor=SideBarBackgroundColor;
		c.SideBarBackgroundColorIntensity=SideBarBackgroundColorIntensity;
		
		c.SideBarSubHoverForeColor=SideBarSubHoverForeColor; 
		c.SideBarSubHoverForeColorIntensity=SideBarSubHoverForeColorIntensity;
		c.SideBarSubActiveBackColor=SideBarSubActiveBackColor;
		c.SideBarSubActiveBackColorIntensity=SideBarSubActiveBackColorIntensity;
		c.SideBarHoverForeColor=SideBarHoverForeColor;
		c.SideBarHoverForeColorIntensity=SideBarHoverForeColorIntensity;
		c.SideBarActiveBackColor=SideBarActiveBackColor;
		c.SideBarActiveBackColorIntensity=SideBarActiveBackColorIntensity;
		
		c.SideBarSubActiveHoverBackColor=SideBarSubActiveHoverBackColor;
		c.SideBarSubActiveHoverBackColorIntensity=SideBarSubActiveHoverBackColorIntensity;
		c.SideBarSubActiveHoverForeColor=SideBarSubActiveHoverForeColor;
		c.SideBarSubActiveHoverForeColorIntensity=SideBarSubActiveHoverForeColorIntensity;	
		c.SideBarActiveHoverBackColor=SideBarActiveHoverBackColor;
		c.SideBarActiveHoverBackColorIntensity=SideBarActiveHoverBackColorIntensity;
		c.SideBarActiveHoverForeColor=SideBarActiveHoverForeColor;
		c.SideBarActiveHoverForeColorIntensity=SideBarActiveHoverForeColorIntensity;
		return c;
	}
}
