package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")
@ShortName("ABMGoogleMarker")
@Hide
public class ABMGoogleMarker {
	public String markerId="";
	public double Latitude=0.0;
	public double Longitude=0.0;
	public String MarkerColor="";
	public String Title="";
	public String infoWindow="";
	public Object Tag=null;
	public String iconPNGUrl="";
	public boolean infoWindowOpen=false;
	public boolean Draggable=false;
	public double AnchorPointX=Double.MIN_VALUE;
	public double AnchorPointY=Double.MIN_VALUE;
	
	public void Initialize(String markerId, double Latitude, double Longitude, String MarkerColor, String Title, String infoWindow) {
		this.markerId = markerId;
		this.Latitude = Latitude;
		this.Longitude = Longitude;
		this.MarkerColor = MarkerColor;
		this.Title = Title;
		this.infoWindow = infoWindow;
	}
	
	public void InitializeEx(String markerId, double Latitude, double Longitude, String Title, String infoWindow, String iconPNGUrl, boolean infoWindowOpen) {
		this.markerId = markerId;
		this.Latitude = Latitude;
		this.Longitude = Longitude;
		this.Title = Title;
		this.infoWindow = infoWindow;
		this.iconPNGUrl = iconPNGUrl;
		this.infoWindowOpen=infoWindowOpen;
	}
	
}
