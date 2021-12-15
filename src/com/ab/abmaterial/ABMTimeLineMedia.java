package com.ab.abmaterial;

import org.json.JSONException;
import org.json.JSONObject;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")
@ShortName("ABMTimeLineMedia")
public class ABMTimeLineMedia implements java.io.Serializable {
	
	private static final long serialVersionUID = -7162873561808688468L;
	public String URL="";
	public String Caption="";
	public String Credit="";
	public String ThumbnailURL="";
	public boolean IsInitialized=false;
	
	public void Initialize(String url) {
		URL=url;
		IsInitialized=true;
	}
	
	protected JSONObject toJson() throws JSONException {
		JSONObject j = new JSONObject();
		j.put("url", URL);
		if (!Caption.equals("")) j.put("caption", Caption);
		if (!Credit.equals("")) j.put("credit", Credit);
		if (!ThumbnailURL.equals("")) j.put("thumbnail", ThumbnailURL);
		return j;
	}
	
	protected void fromJson(JSONObject j) throws JSONException {
		this.URL = j.getString("url");
		if (j.has("caption")) {
			this.Caption = j.getString("caption");
		}
		if (j.has("credit")) {
			this.Credit = j.getString("credit");
		}			    	        
		if (j.has("thumbnail")) {
			this.ThumbnailURL = j.getString("thumbnail");
		}
	}
}
