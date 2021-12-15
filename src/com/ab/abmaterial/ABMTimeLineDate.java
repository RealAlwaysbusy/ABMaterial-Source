package com.ab.abmaterial;

import org.json.JSONException;
import org.json.JSONObject;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")
@ShortName("ABMTimeLineDate")
public class ABMTimeLineDate implements java.io.Serializable {
	
	private static final long serialVersionUID = 1258435102011244227L;
	public int Year=Integer.MIN_VALUE;
	public int Month=Integer.MIN_VALUE;
	public int Day=Integer.MIN_VALUE;
	public int Hour=Integer.MIN_VALUE;
	public int Minute=Integer.MIN_VALUE;
	public int Second=Integer.MIN_VALUE;
	public int Millisecond=Integer.MIN_VALUE;
	public String DisplayDate="";
	public String Format="";
	public boolean IsInitialized=false;
	
	public void Initialize(int year) {
		Hour=Year;
		IsInitialized=true;
	}
	
	protected JSONObject toJSON() throws JSONException {
		JSONObject j = new JSONObject();
		j.put("year", Year);
		if (Month!=Integer.MIN_VALUE) j.put("month", Month);
		if (Day!=Integer.MIN_VALUE) j.put("day", Day);
		if (Hour!=Integer.MIN_VALUE) j.put("hour", Hour);
		if (Minute!=Integer.MIN_VALUE) j.put("minute", Minute);
		if (Second!=Integer.MIN_VALUE) j.put("second", Second);
		if (Millisecond!=Integer.MIN_VALUE) j.put("millisecond", Millisecond);
		if (!DisplayDate.equals("")) j.put("display_date", DisplayDate);
		if (!Format.equals("")) j.put("format", Format);
		return j;
	}
	
	protected void fromJson(JSONObject j) throws JSONException {
		this.Year = j.getInt("year");
		if (j.has("month")) {
			 this.Month = j.getInt("month");
	    }
		if (j.has("day")) {
			 this.Day = j.getInt("day");
	    }
		if (j.has("hour")) {
			 this.Hour = j.getInt("hour");
	    }
		if (j.has("minute")) {
			 this.Minute = j.getInt("minute");
	    }
		if (j.has("second")) {
			 this.Second = j.getInt("second");
	    }
		if (j.has("millisecond")) {
			 this.Millisecond = j.getInt("millisecond");
	    }
		if (j.has("display_date")) {
			 this.DisplayDate = j.getString("display_date");
	    }
		if (j.has("format")) {
			 this.Format = j.getString("format");
	    }	    	        
	}
}
