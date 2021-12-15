package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ThemeSmartWizard implements java.io.Serializable {
	private static final long serialVersionUID = 4835877128178025272L;
	protected String ThemeName="default";
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	
	public String BackColor="transparent";
	public String BackColorIntensity="";
	public String NavigationBackColor="grey";
	public String NavigationBackColorIntensity=ABMaterial.INTENSITY_LIGHTEN4;
	
	public String ButtonNextBackColor=MainColor;
	public String ButtonNextBackColorIntensity="";
	public String ButtonNextForeColor="white";
	public String ButtonNextForeColorIntensity="";     
	public String ButtonPreviousBackColor=MainColor;
	public String ButtonPreviousBackColorIntensity="";
	public String ButtonPreviousForeColor="white";
	public String ButtonPreviousColorIntensity="";
	public String ButtonFinishBackColor=MainColor;
	public String ButtonFinishBackColorIntensity="";
	public String ButtonFinishForeColor="white";
	public String ButtonFinishColorIntensity="";
	
	public String StateDoneBackColor=MainColor;
	public String StateDoneBackColorIntensity="";
	public String StateDoneForeColor="white";
	public String StateDoneForeColorIntensity="";
	
	public String StateActiveBackColor=MainColor;
	public String StateActiveBackColorIntensity=ABMaterial.INTENSITY_DARKEN2;
	public String StateActiveForeColor="white";
	public String StateActiveForeColorIntensity="";
	
	public String StateErrorBackColor="red";
	public String StateErrorBackColorIntensity=ABMaterial.INTENSITY_DARKEN2;
	public String StateErrorForeColor="white";
	public String StateErrorForeColorIntensity="";
	
	public String StateDisabledBackColor="grey";
	public String StateDisabledBackColorIntensity=ABMaterial.INTENSITY_LIGHTEN4;
	public String StateDisabledForeColor="grey";
	public String StateDisabledForeColorIntensity=ABMaterial.INTENSITY_LIGHTEN1;
	
	public String LoadingColor=MainColor;
	public String LoadingColorIntensity="";
	
	protected int ResponsiveBehaviourThresholdPx=601;
	protected boolean IsResponsive=true;
			
	public String ZDepth=ABMaterial.ZDEPTH_1;
	
	
	
	ThemeSmartWizard() {		
	}
	
	ThemeSmartWizard(String themeName) {
		this.ThemeName = themeName;
	}
	
	/**
	 * responsiveType: use the ABM.SMARTWIZARD_RESPONSIVE_ constants
	 */
	public void SetResponsiveType(String responsiveType, int responsiveThresholdPx) {
		ResponsiveBehaviourThresholdPx = responsiveThresholdPx;
		switch (responsiveType) {
		case ABMaterial.SMARTWIZARD_RESPONSIVE_OVERUNDER:
			IsResponsive=true;
			break;
		case ABMaterial.SMARTWIZARD_RESPONSIVE_USEALTICON:
			IsResponsive=false;
			break;			
		}
	}
	
	public void Colorize(String color) {
		MainColor = color;	
		ButtonNextBackColor = MainColor;
		ButtonPreviousBackColor = MainColor;
		ButtonNextBackColor = MainColor;
		ButtonFinishBackColor = MainColor;
		StateDoneBackColor = MainColor;
		StateActiveBackColor = MainColor;
		LoadingColor=MainColor;		
	}
	
	@Hide
	public ThemeSmartWizard Clone() {
		ThemeSmartWizard c = new ThemeSmartWizard();
		
		c.MainColor=MainColor;
		c.ThemeName=ThemeName;
		
		c.BackColor=BackColor;
		c.BackColorIntensity=BackColorIntensity;
		c.NavigationBackColor=NavigationBackColor;
		c.NavigationBackColorIntensity=NavigationBackColorIntensity;
		
		c.ButtonNextBackColor=ButtonNextBackColor;
		c.ButtonNextBackColorIntensity=ButtonNextBackColorIntensity;
		c.ButtonNextForeColor=ButtonNextForeColor;
		c.ButtonNextForeColorIntensity=ButtonNextForeColorIntensity;     
		c.ButtonPreviousBackColor=ButtonPreviousBackColor;
		c.ButtonPreviousBackColorIntensity=ButtonPreviousBackColorIntensity;
		c.ButtonPreviousForeColor=ButtonPreviousForeColor;
		c.ButtonPreviousColorIntensity=ButtonPreviousColorIntensity;
		c.ButtonFinishBackColor=ButtonFinishBackColor;
		c.ButtonFinishBackColorIntensity=ButtonFinishBackColorIntensity;
		c.ButtonFinishForeColor=ButtonFinishForeColor;
		c.ButtonFinishColorIntensity=ButtonFinishColorIntensity;
		
		c.StateDoneBackColor=StateDoneBackColor;
		c.StateDoneBackColorIntensity=StateDoneBackColorIntensity;
		c.StateDoneForeColor=StateDoneForeColor;
		c.StateDoneForeColorIntensity=StateDoneForeColorIntensity;
		
		c.StateActiveBackColor=StateActiveBackColor;
		c.StateActiveBackColorIntensity=StateActiveBackColorIntensity;
		c.StateActiveForeColor=StateActiveForeColor;
		c.StateActiveForeColorIntensity=StateActiveForeColorIntensity;
		
		c.StateErrorBackColor=StateErrorBackColor;
		c.StateErrorBackColorIntensity=StateErrorBackColorIntensity;
		c.StateErrorForeColor=StateErrorForeColor;
		c.StateErrorForeColorIntensity=StateErrorForeColorIntensity;
		
		c.StateDisabledBackColor=StateDisabledBackColor;
		c.StateDisabledBackColorIntensity=StateDisabledBackColorIntensity;
		c.StateDisabledForeColor=StateDisabledForeColor;
		c.StateDisabledForeColorIntensity=StateDisabledForeColorIntensity;
		
		c.ResponsiveBehaviourThresholdPx=ResponsiveBehaviourThresholdPx;
		c.IsResponsive=IsResponsive;
		
		c.LoadingColor=LoadingColor;
		c.LoadingColorIntensity=LoadingColorIntensity;
		
		c.ZDepth=ZDepth;
		return c;
	}
}
