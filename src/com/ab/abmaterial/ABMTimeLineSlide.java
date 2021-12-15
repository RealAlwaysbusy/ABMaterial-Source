package com.ab.abmaterial;

import org.json.JSONException;
import org.json.JSONObject;

import com.ab.abmaterial.ThemeTimeline.ThemeTimelineSlide;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")
@ShortName("ABMTimeLineSlide")
public class ABMTimeLineSlide implements java.io.Serializable {
	
	private static final long serialVersionUID = -4022447265206381410L;
	protected String SlideTheme="default";
	public ABMTimeLineDate StartDate=new ABMTimeLineDate();
	public ABMTimeLineDate EndDate=new ABMTimeLineDate();
	public ABMTimeLineText Text=new ABMTimeLineText();
	public ABMTimeLineMedia Media=new ABMTimeLineMedia();
	public ABMTimeLineLocation Location = new ABMTimeLineLocation();
	public String Group="";
	public String DisplayDate="";
	public boolean AutoLink=true;
	public String BackgroundURL="";
	public String BackgroundOpacity="50";
	protected String UniqueID="";
	public String SlideType="overview";
	public ABMContainer Container=new ABMContainer();
	protected String RandomUUIDString="";
	public boolean IsInitialized=false;
	
	public void Initialize(String uniqueID, int startYear, String slideTheme) {
		StartDate.Year = startYear;
		UniqueID = uniqueID;
		if (!slideTheme.equals("")) {
			SlideTheme=slideTheme;
		}
		IsInitialized=true;
	}
	
	protected JSONObject toJson(String parentString, String arrayName, String ID, ThemeTimeline theme) throws JSONException {
		JSONObject j = new JSONObject();
		if (StartDate.Year!=Integer.MIN_VALUE) j.put("start_date", StartDate.toJSON());
		if (EndDate.Year!=Integer.MIN_VALUE) j.put("end_date", EndDate.toJSON());
		if (!Text.Text.equals("") || (!Text.Headline.equals("")) || (!Container.ID.equals(""))) {
			if (!Container.ID.equals("")) {
				Text.ComponentText = Container.Build();
			}
			j.put("text", Text.toJson());
		}
		if (!Media.URL.equals("")) j.put("media", Media.toJson());
		if (!DisplayDate.equals("")) j.put("display_date", DisplayDate);
		JSONObject bg = new JSONObject();
		bg.put("opacity", BackgroundOpacity);
		if (!BackgroundURL.equals("")) {			
			bg.put("url", BackgroundURL);
		}
		if (!SlideTheme.equals("default")) {
			ThemeTimelineSlide th = theme.Slides.getOrDefault(SlideTheme, null);
			if (th!=null) {
				bg.put("color", ABMaterial.GetColorStrMap(th.BackgroundColor, th.BackgroundColorIntensity));
			}
		}
		j.put("background", bg);
		if (!Group.equals("")) j.put("group", Group);
		if (!AutoLink) j.put("autolink", AutoLink);
		if (!Location.IconURL.equals("") && Location.Latitude!=Float.MIN_VALUE && Location.Longitude!=Float.MIN_VALUE) j.put("location", Location.toJson());
		j.put("unique_id", UniqueID);
		j.put("type", SlideType);
		return j;
	}
	
	protected void fromJson(String jsonString) throws JSONException {
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
		if (j.has("media")) {
			this.Media = new ABMTimeLineMedia();
			this.Media.fromJson(j.getJSONObject("media"));
		}
		if (j.has("location")) {
			this.Location = new ABMTimeLineLocation();
			this.Location.fromJson(j.getJSONObject("location"));
		}
		if (j.has("group")) {
			this.Group=j.getString("group");
		}
		if (j.has("display_date")) {
			this.DisplayDate=j.getString("display_date");
		}
		if (j.has("unique_id")) {
			this.UniqueID=j.getString("unique_id");
		}
		if (j.has("type")) {
			this.SlideType=j.getString("type");
		}
		if (j.has("background")) {
			JSONObject bg = j.getJSONObject("background");
			if (bg.has("url")) {
				this.BackgroundURL=bg.getString("url");
			}
			if (bg.has("opacity")) {
				this.BackgroundOpacity=bg.getString("opacity");
			}
		}
	}	
	
	
}
