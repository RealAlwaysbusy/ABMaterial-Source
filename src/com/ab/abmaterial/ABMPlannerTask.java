package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")  
@ShortName("ABMPlannerTask")
public class ABMPlannerTask  implements Comparable<ABMPlannerTask>{
	protected String mTaskID="";
	protected int mStartHour=0;
	protected int mStartMinututesPer=0;
	protected int mLengthMinutesPer=0;
	protected String mText="";
	protected boolean IsDirty=true;
	public Object Tag=null;
	protected int mDay=-1;
	protected int mPrevStartHour=0;
	protected int mPrevStartMinututesPer=0;
	protected int mPrevLengthMinutesPer=0;
	protected String mPrevText="";
	protected int mPrevDay=-1;
	protected int per=5;
	public int ThemeColorIndex;
	
	public void Initialize(int day, String taskID, int startHour, int startMinututesPer, int lengthMinutesPer, String text, int themeColorIndex) {
		this.mDay=day;
		this.mPrevDay=day;
		this.mTaskID=taskID;
		this.mStartHour=startHour;
		this.mStartMinututesPer=startMinututesPer;
		this.mLengthMinutesPer=lengthMinutesPer;
		this.mText = text;
		this.ThemeColorIndex=themeColorIndex;
		IsDirty=true;
	}
	
	@Override
	public int compareTo(ABMPlannerTask other) {
		int a=mStartHour*60+mStartMinututesPer*per;
		int b=other.mStartHour*60+other.mStartMinututesPer*per;
		return a > b ? +1 : a < b ? -1 : 0;		
	}
	
	protected void IsDone() {
		IsDirty=false;
		mPrevStartHour=mStartHour;
		mPrevStartMinututesPer=mStartMinututesPer;
		mPrevLengthMinutesPer=mLengthMinutesPer;
		mPrevText=mText;		
		mPrevDay=mDay;
	}
	
	public ABMPlannerTask Clone(String newTaskID) {
		ABMPlannerTask c = new ABMPlannerTask();
		c.Initialize(mDay, newTaskID, mStartHour, mStartMinututesPer, mLengthMinutesPer, mText, ThemeColorIndex);
		c.IsDirty = true;
		c.Tag = Tag;
		c.mPrevDay = mPrevDay;
		c.mPrevStartHour = mPrevStartHour;
		c.mPrevStartMinututesPer=mPrevStartMinututesPer;
		c.mPrevLengthMinutesPer=mPrevLengthMinutesPer;
		c.mPrevText=mPrevText;
		c.per=per;
		return c;
	}
	
	public String getTaskID() {
		return mTaskID;
	}
	
	public void setTaskID(String taskID) {
		IsDirty=true;
		mTaskID=taskID;
	}
	
	public int getStartHour() {
		return mStartHour;
	}
	
	public void setStartHour(int startHour) {
		IsDirty=true;
		mStartHour=startHour;
	}
	
	public int getStartMinututesPer() {
		return mStartMinututesPer;
	}
	
	public void setStartMinututesPer(int startMinututesPer) {
		IsDirty=true;
		mStartMinututesPer=startMinututesPer;
	}
	
	public int getLengthMinutesPer() {
		return mLengthMinutesPer;
	}
	
	public void setLengthMinutesPer(int lengthMinutesPer) {
		IsDirty=true;
		mLengthMinutesPer=lengthMinutesPer;
	}
	
	public String getText() {
		return mText;
	}
	
	public void setText(String text) {
		IsDirty=true;
		mText=text;
	}
	
	public int getDay() {
		return mDay;
	}
	
	public void setDay(int day) {
		IsDirty=true;
		mPrevDay=mDay;
		mDay=day;		
	}
	
}
