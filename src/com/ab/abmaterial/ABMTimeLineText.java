package com.ab.abmaterial;

import org.json.JSONException;
import org.json.JSONObject;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")
@ShortName("ABMTimeLineText")
public class ABMTimeLineText implements java.io.Serializable {
	
	private static final long serialVersionUID = 6154195542836027014L;
	public String Text="";
	public String Headline="";
	protected String ComponentText="";
	public boolean IsInitialized=false;
		
	public void Initialize() {
		IsInitialized=true;
	}
	
	protected JSONObject toJson() throws JSONException {
		JSONObject j = new JSONObject();
		if (!Text.equals("") || !ComponentText.equals("")) {
			if (!ComponentText.equals("")) {
				j.put("text", Text + "[ABMComponent]" + ComponentText); //.replace("'", "\\'")
			} else {
				j.put("text", Text);
			}
		}
		if (!Headline.equals("")) j.put("headline", Headline);
		return j;
	}
	
	protected void fromJson(JSONObject j) throws JSONException {
		 if (j.has("text")) {
			 this.Text = j.getString("text");
	     }
	     if (j.has("headline")) {
	    	 this.Headline = j.getString("headline");
	     }	        
	}
}
