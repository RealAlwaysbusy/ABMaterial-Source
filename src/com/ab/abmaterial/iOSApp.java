package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class iOSApp {
	public String ApplicationName="";
	public String ApplicationTitle="";
	public String ApplicationIconPng40Path="";
	public String ApplicationIconPng60="";
	public String ApplicationIconPng60_2xPath="";
	public String ApplicationIconPng76Path="";
	public String ApplicationIconPng76_2xPath="";
	
	public String PackageName="";
	public String Version="1.0.0";
		
	public String ABMaterialURL="";
	
	public String NoConnectionMessage="You need a connection to the internet to run this application! Click retry or press the home button to leave.";
	public String NoConnectionRetry = "Retry";
	public String NoConnectionCancel = "Cancel";
	
	protected void Generate(ABMPage page) {
		
	}
}
