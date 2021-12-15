package com.ab.abmaterial;

import org.json.JSONException;
import org.json.JSONObject;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")
@ShortName("ABMTimeLineLocation")
public class ABMTimeLineLocation implements java.io.Serializable {
	
	private static final long serialVersionUID = -8883925574591762485L;
	public String IconURL="";
	public float Latitude=Float.MIN_VALUE;
	public float Longitude=Float.MIN_VALUE;
	public boolean Line=true;
	public String Name="";
	public int Zoom=1;
	public boolean IsInitialized=false;
	
	public void Initialize(String iconURL, float latitude, float longitude) {
		this.IconURL=iconURL;
		this.Latitude=latitude;
		this.Longitude=longitude;
		IsInitialized=true;
	}
	
	protected JSONObject toJson() throws JSONException {
		JSONObject j = new JSONObject();
		j.put("icon", IconURL);
		j.put("lat", Latitude);
		j.put("lon", Longitude);
		j.put("line", Line);
		if (!Name.equals("")) j.put("name", Name);
		j.put("zoom", Zoom);
		return j;
	}
	
	protected void fromJson(JSONObject j) throws JSONException {
		this.IconURL = j.getString("icon");
		this.Latitude = (float) j.getDouble("lat");
		this.Longitude = (float) j.getDouble("lon");
		if (j.has("line")) {
			this.Line = j.getBoolean("line");
		}
		if (j.has("name")) {
			 this.Name = j.getString("name");
	    }
		if (j.has("zoom")) {
			 this.Zoom = j.getInt("zoom");
	    }			    	        
	}
}
