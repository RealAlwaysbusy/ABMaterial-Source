package com.ab.abmaterial;

import org.json.JSONException;
import org.json.JSONObject;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")
@ShortName("ABMTimeLineEra")
public class ABMTimeLineEra implements java.io.Serializable {
	
	private static final long serialVersionUID = 5828761103724996160L;
	public ABMTimeLineDate StartDate=new ABMTimeLineDate();
	public ABMTimeLineDate EndDate=new ABMTimeLineDate();
	public ABMTimeLineText Text=new ABMTimeLineText();
	public boolean IsInitialized=false;
	
	public void Initialize(int startYear, int endYear) {
		StartDate.Year = startYear;
		EndDate.Year = endYear;
		IsInitialized=true;
	}
	
	protected JSONObject toJson() throws JSONException {
		JSONObject j = new JSONObject();
		if (StartDate.Year!=Integer.MIN_VALUE) j.put("start_date", StartDate.toJSON());
		if (EndDate.Year!=Integer.MIN_VALUE) j.put("end_date", EndDate.toJSON());
		if (!Text.Text.equals("") && (!Text.Headline.equals(""))) j.put("text", Text.toJson());		
		return j;
	}
	
	public void fromJson(String jsonString) throws JSONException {
		JSONObject j = new JSONObject(jsonString);
		if (j.has("start_date")) {
			this.StartDate = new ABMTimeLineDate();
			this.StartDate.fromJson(j.getJSONObject("start_date"));
		}
		if (j.has("end_date")) {
			this.EndDate = new ABMTimeLineDate();
			this.EndDate.fromJson(j.getJSONObject("end_date"));
		}
		if (j.has("text")) {
			this.Text = new ABMTimeLineText();
			this.Text.fromJson(j.getJSONObject("text"));
		}		
	}	
}
