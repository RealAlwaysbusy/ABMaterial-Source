package com.ab.abmaterial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.SimpleFuture;

@Author("Alain Bailleul")  
@ShortName("ABMPlanner")
@Events(values={"ActiveDayChanged(day As Int)","MinutesClicked(Value As String)","Refreshed()", "MenuClicked(MenuType As String, Value as String)"})
public class ABMPlanner extends ABMComponent {
	private static final long serialVersionUID = 4923443962647778422L;
	protected ThemePlanner Theme=new ThemePlanner();
	
	protected int FromHour=0;
	protected int ToHour=23;
	protected String[] dayLabels = new String[7];
	
	protected Day[] days = new Day[7];
	protected String[] dayCodes = new String[7];
	protected String[] minCodes = new String[12];
	
	protected String Active = "abmactive";
	
	protected int mActiveDay=-1;
	
	protected boolean IsFullPlannerEmpty=false;
	protected int Per=5;
	protected int PerDivider=3;
	protected int PerTotal=12;
	
	protected boolean ReadOnly=false;
	
	protected boolean mUseHeatMap=false;
	protected List<XY> mHeatMap = new ArrayList<XY>();
	
	/**
	 * minutesInterval can only be per 5 minutes, 10, 15, 20 or 30 
	 */
	public void Initialize(ABMPage page, String id, boolean showWeekend, int fromHour, int toHour, int minutesInterval, boolean readOnly, String themeName) {	
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_PLANNER;	
		this.FromHour = fromHour;
		this.ToHour = toHour;
		int per = minutesInterval;
		if (per!=5 && per!=10 && per!=15 && per!=20 && per!=30) {
			BA.LogError("minutesInterval can only be 5, 10, 15, 20 or 30");
			return;
		}
		this.Per=per;
		switch (per) {
		case 5:
			PerDivider=3;
			PerTotal=12;
			minCodes = new String[12];
			minCodes[0]="00";
			minCodes[1]="05";
			minCodes[2]="10";
			minCodes[3]="15";
			minCodes[4]="20";
			minCodes[5]="25";
			minCodes[6]="30";
			minCodes[7]="35";
			minCodes[8]="40";
			minCodes[9]="45";
			minCodes[10]="50";
			minCodes[11]="55";
			break;
		case 10:
			PerDivider=2;
			PerTotal=6;
			minCodes = new String[6];
			minCodes[0]="00";
			minCodes[1]="10";
			minCodes[2]="20";
			minCodes[3]="30";
			minCodes[4]="40";
			minCodes[5]="50";			
			break;
		case 15:
			PerDivider=2;
			PerTotal=4;
			minCodes = new String[4];
			minCodes[0]="00";
			minCodes[1]="15";
			minCodes[2]="30";
			minCodes[3]="45";
			break;
		case 20:
			PerDivider=1;
			PerTotal=3;
			minCodes = new String[3];
			minCodes[0]="00";
			minCodes[1]="20";
			minCodes[2]="40";			
			break;
		case 30:
			PerDivider=1;
			PerTotal=2;
			minCodes = new String[2];
			minCodes[0]="00";
			minCodes[1]="30";			
			break;
		}
		
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Planners.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Planners.get(themeName.toLowerCase()).Clone();				
			}
		}	
		if (!showWeekend) {
			days = new Day[5];
			Active = "abmactivenw";
		}
		for (int i=0;i<days.length;i++) {
			days[i]=new Day();
		}
		dayCodes[0]="MA";
		dayCodes[1]="DI";
		dayCodes[2]="WO";
		dayCodes[3]="DO";
		dayCodes[4]="VR";
		dayCodes[5]="ZA";
		dayCodes[6]="ZO";
		
		
		
		dayLabels[0]="MO";
		dayLabels[1]="TU";
		dayLabels[2]="WE";
		dayLabels[3]="TH";
		dayLabels[4]="FR";
		dayLabels[5]="SA";
		dayLabels[6]="SU";
		IsInitialized=true;
	}
	
	public boolean ShowsWeekend() {
		return (days.length==7);
	}
	
	@Override
	protected void ResetTheme() {
		UseTheme(Theme.ThemeName);
	}
	
	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase();
	}
		
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}	
		
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Planners.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Planners.get(themeName.toLowerCase()).Clone();				
			}
		}
	}
	
	public void SetDayLabels(String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday) {
		dayLabels[0] = monday;
		dayLabels[1] = tuesday;
		dayLabels[2] = wednesday;
		dayLabels[3] = thursday;
		dayLabels[4] = friday;
		dayLabels[5] = saturday;
		dayLabels[6] = sunday;
	}
		
	/**
	 * MUST be called at the end if a MenuClicked event to actually do the menu action of the day and hour menu in the users browser
	 */
	public void PerfromDayHourMenuAction() {
		if (Page.ws!=null) {			
			Page.ws.Eval("PlannerDoAction('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "');", null);
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Planner do action " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}				
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
	}
	
	/**
	 * themeIntervals of lenghts, e.g. "1,3,6" means:
	 * if length <= 1: use theme 0
	 * if length >1 and <=3: use theme 1
	 * if length >3 and <=6: use theme 2
	 * if length >6: use theme 4
	 */
	public void UseHeatMap(String themeIntervals) {
		mHeatMap = new ArrayList<XY>();
		String spl[] = themeIntervals.split(",");
		int PrevValue=0;
		for (int i=0;i<spl.length;i++) {
			XY xy = new XY();
			if (i==0) {
				xy.Y = Integer.parseInt(spl[i]);
				PrevValue = xy.Y;
			} else {
				if (i==spl.length-1) {
					xy.X=PrevValue;
				} else {
					xy.X=PrevValue;
					xy.Y=Integer.parseInt(spl[i]);
					PrevValue = xy.Y;
				}
			}
			mHeatMap.add(xy);
		}
		mUseHeatMap=true;
	}
	
	public void AddTask2(int day, String taskID, int startHour, int startMinututesPer, int lengthMinutesPer, String text, int themeColorIndex) {
		ABMPlannerTask task = new ABMPlannerTask();
		task.mDay=day;
		task.mTaskID=taskID;
		task.mStartHour=startHour;
		task.mStartMinututesPer=startMinututesPer;
		task.mLengthMinutesPer=lengthMinutesPer;
		task.mText=text;
		task.per=Per;
		task.ThemeColorIndex =themeColorIndex;
		AddTask(task);
	}
	
	public void AddTask(ABMPlannerTask task) {
		task.per=Per;
		days[task.mDay].Tasks.add(task.Clone(task.mTaskID));
		days[task.mDay].IsDirty=true;	
		days[task.mDay].IsFullDayEmpty=false;
	}
	
	public void UpdateTask(ABMPlannerTask task) {
		task.per=Per;
		MoveTaskInner(task.mPrevDay, task.mDay, task);		
	}
	
	protected void MoveTaskInner(int fromDay, int toDay, ABMPlannerTask task) {
		days[fromDay].IsDirty=true;
		days[toDay].IsDirty=true;
		ABMPlannerTask tmpTask = RemoveTaskInner(fromDay, task);
		if (tmpTask!=null) {
			AddTask(tmpTask);
		} else {
			AddTask(task);
		}
	}
	
	public void RemoveTask(ABMPlannerTask task) {
		days[task.mDay].IsDirty=true;
		RemoveTaskInner(task.mDay, task);
	}
	
	public void RemoveTask2(String taskID) {
		ABMPlannerTask task = GetTask(taskID);
		if (task==null) {
			return;
		}
		days[task.mDay].IsDirty=true;
		RemoveTaskInner(task.mDay, task);
	}
	
	protected ABMPlannerTask RemoveTaskInner(int day, ABMPlannerTask task) {
		String taskID = task.mTaskID;
		for (int i=0;i<days[day].Tasks.size();i++) {
			if (days[day].Tasks.get(i).mTaskID.equalsIgnoreCase(taskID)) {
				ABMPlannerTask c = days[day].Tasks.get(i).Clone(taskID); 
				days[day].Tasks.remove(i);
				
				if (days[day].Tasks.size()==0) {
					days[task.mDay].IsFullDayEmpty=true;
				}
				return c;
			}
		}	
		BA.Log("No task found with ID: " + taskID);
		return null;
	}
	
	public ABMPlannerTask GetTask(String taskID) {
		for (int day=0;day<days.length;day++) {
			for (int i=0;i<days[day].Tasks.size();i++) {
				if (days[day].Tasks.get(i).mTaskID.equalsIgnoreCase(taskID)) {
					return days[day].Tasks.get(i);
				}
			}
		}
		BA.Log("No task found with ID: " + taskID);
		return null;
	}
	
	public void ClearDay(int day) {
		for (int i=0;i<days[day].Tasks.size();i++) {
			RemoveTask(days[day].Tasks.get(i));			
		}
		days[day] = new Day();			
	}
	
	public void ClearPlanner() {
		for (int i=0;i<days.length;i++) {
			ClearDay(i);
		}
		IsFullPlannerEmpty=true;		
	}
	
	public void SetDayStatus(int day, boolean isFree) {
		for (int i=0;i<23;i++) {
			days[day].isFrees[i] = isFree;
		}
		days[day].IsFullDayNA=isFree;
		days[day].IsDirty=true;
	}
	
	public void SetHourStatus(int day, int hour, boolean isFree) {
		days[day].isFrees[hour] = isFree;
		days[day].IsDirty=true;
	}
	
	public void SetTaskTheme(String taskID, int themeColorIndex) {
		for (int day=0;day<days.length;day++) {
			for (int i=0;i<days[day].Tasks.size();i++) {
				if (days[day].Tasks.get(i).mTaskID.equalsIgnoreCase(taskID)) {
					days[day].Tasks.get(i).ThemeColorIndex=themeColorIndex;
					return;
				}
			}
		}
		BA.Log("No task found with ID: " + taskID);
	}
	
	public int GetTaskTheme(String taskID) {
		for (int day=0;day<days.length;day++) {
			for (int i=0;i<days[day].Tasks.size();i++) {
				if (days[day].Tasks.get(i).mTaskID.equalsIgnoreCase(taskID)) {	
					return days[day].Tasks.get(i).ThemeColorIndex;
				}
			}
		}
		BA.Log("No task found with ID: " + taskID);
		return -1;
	}
	
	public void SetActiveDay(int day) {
		if (mActiveDay>-1) {
			days[mActiveDay].IsDirty=true;
		}
		if (day>days.length-1) {
			BA.LogError("Day " + day + " is not visible!");
			day=0;
		}
		days[day].IsDirty=true;
		mActiveDay=day;
	}
	
	public int GetActiveDay() {
		int ret = -1;
		if (Page.ws!=null) {
			SimpleFuture fut = Page.ws.EvalWithResult("return planners['" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "'];", null);
			
			try {				
				ret = (Integer) fut.getValue();				
			} catch (InterruptedException e) {				
			} catch (TimeoutException e) {						
			} catch (ExecutionException e) {						
			} catch (IOException e) {						
			}
		}
		return ret;
	}
		
	@Override
	protected void CleanUp() {
		super.CleanUp();
	}
	
	@Override
	protected void RemoveMe() {
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
	}
	
	@Override
	protected void FirstRun() {
		Page.ws.Eval(BuildJavaScript(), null);
		
		super.FirstRun();
	}
	
	protected String BuildJavaScript() {
		StringBuilder s = new StringBuilder();		
				
		s.append("planners['" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "']=" + mActiveDay + ";");			
		for (int k=0;k<days.length;k++) {
			if (days[k].IsFullDayNA) {
				s.append("SPDNA('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "','" + dayCodes[k] + "');");
			} else {
				s.append("SPDA('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "','" + dayCodes[k] + "');");
			}
		}
		
		ThemePlanner l = Theme;
		
		s.append("$('.plannernamenu-itemdef').attr('class','plannernamenu-item plannernamenu-item" + l.ThemeName.toLowerCase() + " plannernamenu-itemdef z-depth-2');");
		s.append("$('.plannerccpmenu-item').attr('class','plannerccpmenu-item plannerccpmenu-item" + l.ThemeName.toLowerCase() + " z-depth-2');");
		s.append("$('.plannernamenuNA').attr('class','plannernamenu-item plannernamenuNA" + l.ThemeName.toLowerCase() + " z-depth-2');");
		s.append("ResizePlanner();");
		return s.toString();
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		
		super.Refresh();
		
		for (int k=0;k<days.length;k++) {
			days[k].Sort();
			days[k].BuildStructure();			
		}
		
		ThemePlanner l = Theme;
				
		StringBuilder s = new StringBuilder();
		s.append("$('.plannernamenu-container').removeClass('hide');");
		s.append("$('.plannerccpmenu-container').removeClass('hide');");
		
		s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').attr('class', 'abmplannerwrapper abmplannerwrapper" + l.ThemeName.toLowerCase() + " " + mVisibility + " " + l.ZDepth + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() + "');");
		s.append("plannerID=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\";");
		s.append("oldActiveDay=planners[plannerID];");
		s.append("planners[plannerID]=" + mActiveDay + ";");
				
		String[] lasts = new String[7];
				
		if (IsFullPlannerEmpty) {
			s.append("SPE(plannerID);");
		} else {
			for (int k=0;k<days.length;k++) {
				if (days[k].IsDirty) {
					if (days[k].IsFullDayEmpty) {
						s.append("SPDE(plannerID,'" + dayCodes[k] + "');");
					}
				}
			}
		}		
		
		s.append(RefreshInternalDayLabels());
		
		String tFirstID="";
		String tText="";
		int tTheme=0;
		for (int k=0;k<days.length;k++) {
			if (days[k].IsDirty) {
				int First=-1;
				int Last=-1;
				for (int j=FromHour;j<=ToHour;j++) {					
					if (!days[k].IsFullDayEmpty) {						
						for (int v=0;v<PerTotal;v++) {
							ABMPlannerTask t = days[k].Structure.get(j*PerTotal+v);
							if (t!=null) {
								String tID = t.mTaskID;
								
								if (tID.equals(lasts[k])) {
									Last=(j*PerTotal+v);									
								} else {									
									if (Last>-1) {
										if (mUseHeatMap) {
											int nextBegin = t.mStartHour*PerTotal+t.mStartMinututesPer;
											if (nextBegin-1<=Last) {
												int range = nextBegin-First-1;
												for (int p=0;p<mHeatMap.size();p++) {
													if (range>=mHeatMap.get(p).X && range<mHeatMap.get(p).Y) {
														tTheme=p;
														break;
													}
												}
												for (int p=First;p<=Last;p++) {
													days[k].Structure.get(p).ThemeColorIndex=tTheme;
												}
											}
										}
										s.append("SPT('" + dayCodes[k] + "','" + tFirstID + "'," + First + "," + Last + ",'" + tText + "'," + tTheme + ");\n");
									}
									First=(j*PerTotal+v);
									Last=(j*PerTotal+v);
									tFirstID=tID;
									tText=BuildText(t.mText);
									tTheme=t.ThemeColorIndex;
								}
								
								lasts[k]=tID;
								t.IsDone();
							} else {
								if (Last>-1) {
									s.append("SPT('" + dayCodes[k] + "','" + tFirstID + "'," + First + "," + Last + ",'" + tText + "'," + tTheme + ");\n");
								}
								First=-1;
								Last=-1;
								lasts[k]="";
							}
							
						}
					}
					if (Last>-1) {
						s.append("SPT('" + dayCodes[k] + "','" + tFirstID + "'," + First + "," + Last + ",'" + tText + "'," + tTheme + ");\n");
					}
					if (!days[k].IsFullDayNA) {
						if (!days[k].isFrees[j]) {
							s.append("SPHNA(plannerID,'" + dayCodes[k] + "'," + j + ");");
						} else {
							if (!days[k].IsFullDayEmpty) {
								s.append("SPHA(plannerID,'" + dayCodes[k] + "'," + j + ");");
							}
						}
					}
				}				
			}
		}

		// + set active day
		if (days.length==5) {
			s.append("SPAD('nw','" + mActiveDay + "');");
		} else {
			s.append("SPAD('','" + mActiveDay + "');");
		}
		s.append("b4j_raiseEvent('page_parseevent', {'eventname': '" + ID.toLowerCase() + "_refreshed','eventparams':''});");
		
		s.append("$('.plannernamenu-itemdef').attr('class','plannernamenu-item plannernamenu-item" + l.ThemeName.toLowerCase() + " plannernamenu-itemdef z-depth-2');");
		s.append("$('.plannerccpmenu-item').attr('class','plannerccpmenu-item plannerccpmenu-item" + l.ThemeName.toLowerCase() + " z-depth-2');");
		s.append("$('.plannernamenuNA').attr('class','plannernamenu-item plannernamenuNA" + l.ThemeName.toLowerCase() + " z-depth-2');");
		s.append("ResizePlanner();");
		
		Page.ws.Eval(s.toString(), null);
		
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Planner Refresh " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}		 
	}
	
	protected String RefreshInternalDayLabels() {
		StringBuilder s = new StringBuilder();
		for (int j=0;j<days.length;j++) {
			s.append("$('#' + plannerID + '" + dayCodes[j] + "').html('" + BuildText(dayLabels[j]) + "');");
		}
		return s.toString();
	}
	
	@Override
	protected String Build() {
		if (Theme.ThemeName.equals("default")) {
			Theme.Colorize(Page.CompleteTheme.MainColor);
		}
		ThemePlanner l = Theme;
		StringBuilder s = new StringBuilder();
		for (int k=0;k<days.length;k++) {
			days[k].Sort();
			days[k].BuildStructure();			
		}
		String HasMenu="";
		
		String viewType="all";
		String NW="";
		if (days.length==5) {
			viewType="nw";
			NW="abmdaynw";
		}
		String readOnly="";
		if (ReadOnly) {
			readOnly="pointer-events: none;";
		}
		s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" data-viewtype=\"" + viewType + "\" class=\"abmplannerwrapper abmplannerwrapper" + l.ThemeName.toLowerCase()  + " " + mVisibility + " " + l.ZDepth + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() + "\" style=\"height: 100%;overflow: hidden;" + readOnly + "\" eName=\"" + ID.toLowerCase() + "\">");
			s.append("<div class=\"abmdaywrapperouter\">");
				s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "daywrapper\" class=\"abmdaywrapper abmdaywrapper" + l.ThemeName.toLowerCase() + "\" style=\"height:42px;\">");
				String alt="alt";
				for (int j=0;j<days.length;j++) {
					alt="alt";
					if ((j % 2)==0) {
						alt="";
					}
					if (j==mActiveDay) {
						s.append("<div class=\"abmday " + NW + " abmday" + alt + l.ThemeName.toLowerCase() + " " + Active + " " + HasMenu + "\" data-day=\"" + dayCodes[j] + "\" data-dayn=\"" + j + "\"><span id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + dayCodes[j] + "\">" + BuildText(dayLabels[j]) + "</span>");
					} else {
						s.append("<div class=\"abmday " + NW + " abmday" + alt + l.ThemeName.toLowerCase() + " " + HasMenu + "\" data-day=\"" + dayCodes[j] + "\" data-dayn=\"" + j + "\"><span id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + dayCodes[j] + "\">" + BuildText(dayLabels[j]) + "</span>"); 
					}
					
					s.append("</div>");
				}
				s.append("</div>");
			s.append("</div>");
			
			s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "plannerparent\" class=\"abmplannerscroller\" style=\"height: calc(100% - 42px)\">");
				s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "planner\" class=\"abmplanner\">");					
		
					s.append(BuildBody());
		
				s.append("</div>");
			s.append("</div>");
		s.append("</div>");
		IsBuild=true;
		return s.toString();
	}
	
	@Hide
	protected String BuildClass() {			
		StringBuilder s = new StringBuilder();			
		return s.toString(); 
	}
	
	@Hide
	protected String BuildBody() {
		StringBuilder s = new StringBuilder();
		ThemePlanner l = Theme;
		
		String[] lasts = new String[7];
		String NW="";
		if (days.length==5) {
			NW = "nw";
		}		
		String HasMenu="";
		
		for (int j=FromHour;j<=ToHour;j++) {
			s.append("<div class=\"abmplannerhour abmplannerhour" + l.ThemeName.toLowerCase() + "\">");
			String hour=String.valueOf(j);
			if (hour.length()==1) {
				hour="0"+hour;
			}
			hour+=":00";
			String alt="";
			for (int k=0;k<days.length;k++) {
				alt="alt";
				if ((k % 2)==0) {
					alt="";
				}
				if (k==mActiveDay) {
					s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "day" + dayCodes[k] + "\" class=\"abmplannerday" + NW + " " + Active + "\" data-day=\"" + dayCodes[k] + "\">");
					s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "hour" + dayCodes[k] + j + "\" data-dayhr=\"" + k + ";" + j + "\" class=\"abmhour abmhour" + alt + l.ThemeName.toLowerCase() + " " + Active + " " + HasMenu + "\">" + BuildText(hour)); 
				} else {
					s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "day" + dayCodes[k] + "\"  class=\"abmplannerday" + NW + "\" data-day=\"" + dayCodes[k] + "\">");
					s.append("<div  id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "hour" + dayCodes[k] + j + "\" data-dayhr=\"" + k + ";" + j + "\"  class=\"abmhour abmhour" + alt + l.ThemeName.toLowerCase() + " " + HasMenu + "\">" + BuildText(hour)); 
				}
					
				s.append("</div>");
				s.append("<div class=\"abmhourcenter\" data-hour=\"" + j + "\">");
				s.append("<div class=\"abmhr abmhr" + l.ThemeName.toLowerCase() + "\">");
				for (int v=0;v<PerTotal;v++) {
					ABMPlannerTask t = days[k].Structure.get(j*PerTotal+v);
					if (v>0 && (v % PerDivider)==0) {
						s.append("</div>");
						s.append("<div class=\"abmhr abmhr" + l.ThemeName.toLowerCase() + "\">");
					}
					if (t!=null) {
						String tID = t.mTaskID;
						
						if (tID.equals(lasts[k])) {
							if (k==mActiveDay) {
								s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "min" + dayCodes[k] + (j*PerTotal+v) + "\" data-id=\"" + tID + "\" data-val=\"" + k + ";" + j + ";" + v + "\" class=\"abmhc abmhc" + PerDivider + " abmhc" + l.ThemeName.toLowerCase() + " abmocc abmocc" + t.ThemeColorIndex + "\" data-th=\"" + t.ThemeColorIndex + "\">" + minCodes[v]);
									s.append("<p id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "txt" + dayCodes[k] + (j*PerTotal+v) + "\" class=\"abmwho\">&larr;</p>");
								s.append("</div>");
							} else {
								s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "min" + dayCodes[k] + (j*PerTotal+v) + "\" data-id=\"" + tID + "\" data-val=\"" + k + ";" + j + ";" + v + "\" class=\"abmhc abmhc" + PerDivider + " abmhc" + l.ThemeName.toLowerCase() + " abmocc abmocc" + t.ThemeColorIndex + "\" data-th=\"" + t.ThemeColorIndex + ">" + minCodes[v]);
									s.append("<p id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "txt" + dayCodes[k] + (j*PerTotal+v) + "\" class=\"abmwho abmhidden\">&larr;</p>");
								s.append("</div>");
							}
						} else {
							if (k==mActiveDay) {
								s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "min" + dayCodes[k] + (j*PerTotal+v) + "\" data-id=\"" + tID + "\" data-val=\"" + k + ";" + j + ";" + v + "\" class=\"abmhc abmhc" + PerDivider + " abmhc" + l.ThemeName.toLowerCase() + " abmocc abmocc" + t.ThemeColorIndex + "\" data-th=\"" + t.ThemeColorIndex + ">" + minCodes[v]);
									s.append("<p id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "txt" + dayCodes[k] + (j*PerTotal+v) + "\" class=\"abmwho\">" + BuildText(t.mText) + "</p>");
								s.append("</div>");
							} else {
								s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "min" + dayCodes[k] + (j*PerTotal+v) + "\" data-id=\"" + tID + "\" data-val=\"" + k + ";" + j + ";" + v + "\" class=\"abmhc abmhc" + PerDivider + " abmhc" + l.ThemeName.toLowerCase() + " abmocc abmocc" + t.ThemeColorIndex + "\" data-th=\"" + t.ThemeColorIndex + ">" + minCodes[v]);
									s.append("<p id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "txt" + dayCodes[k] + (j*PerTotal+v) + "\" class=\"abmwho abmhidden\">" + BuildText(t.mText) + "</p>");
								s.append("</div>");
							}
						}
						
						lasts[k]=tID;
						t.IsDone();
					} else {
						String NA="";
						if (!days[k].isFrees[j]) {
							NA=" abmna ";
						}
						if (k==mActiveDay) {
							s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "min" + dayCodes[k] + (j*PerTotal+v) + "\" data-id=\"\" data-val=\"" + k + ";" + j + ";" + v + "\" class=\"abmhc abmhc" + PerDivider + " abmhc" + l.ThemeName.toLowerCase() + NA + "\">" + minCodes[v]);
								s.append("<p id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "txt" + dayCodes[k] + (j*PerTotal+v) + "\" class=\"abmwho\">&nbsp;</p>");
							s.append("</div>");
						} else {
							s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "min" + dayCodes[k] + (j*PerTotal+v) + "\" data-id=\"\" data-val=\"" + k + ";" + j + ";" + v + "\" class=\"abmhc abmhc" + PerDivider + " abmhc" + l.ThemeName.toLowerCase() + NA + "\">" + minCodes[v]);
								s.append("<p id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "txt" + dayCodes[k] + (j*PerTotal+v) + "\" class=\"abmwho abmhidden\">&nbsp;</p>");
							s.append("</div>");
						}
						lasts[k]="";
					}
				}
				s.append("</div>");
				s.append("</div>");
				s.append("</div>");				
			}
			s.append("</div>");
		}	
		for (int k=0;k<days.length;k++) {
			days[k].IsDirty=false;
		}
		return s.toString();
	}
	
	@Hide
	protected String BuildText(String Text) {
		StringBuilder s = new StringBuilder();	
		
		String v = ABMaterial.HTMLConv().htmlEscape(Text, Page.PageCharset);
		v=v.replaceAll("(\r\n|\n\r|\r|\n)", "<br>");
		v=v.replace("{B}", "<b>");
		v=v.replace("{/B}", "</b>");
		v=v.replace("{I}", "<i>");
		v=v.replace("{/I}", "</i>");
		v=v.replace("{U}", "<ins>");
		v=v.replace("{/U}", "</ins>");
		v=v.replace("{SUB}", "<sub>");
		v=v.replace("{/SUB}", "</sub>");
		v=v.replace("{SUP}", "<sup>");
		v=v.replace("{/SUP}", "</sup>");
		v=v.replace("{BR}", "<br>");
		v=v.replace("{WBR}", "<wbr>");
		v=v.replace("{NBSP}", "&nbsp;");
		v=v.replace("{AL}", "<a rel=\"nofollow\" target=\"_blank\" href=\"");
		v=v.replace("{AT}", "\">");
		v=v.replace("{/AL}", "</a>");
		v=v.replace("{AS}", " title=\"");
		v=v.replace("{/AS}", "\"");
		
		while (v.indexOf("{C:") > -1) {
			int vvi = v.indexOf("{C:");
			v=v.replaceFirst("\\{C:", "<span style=\"color:");
			v=v.substring(0,vvi) + v.substring(vvi).replaceFirst("\\}", "\">");
			v=v.replaceFirst("\\{/C}", "</span>");	
		}

		v = v.replace("{CODE}", "<div class=\"abmcode\"><pre><code>");
		v = v.replace("{/CODE}", "</code></pre></div>");
		while (v.indexOf("{ST:") > -1) {
			int vvi = v.indexOf("{ST:");
			v=v.replaceFirst("\\{ST:", "<span style=\"");			
			v=v.substring(0,vvi) + v.substring(vvi).replaceFirst("\\}", "\">");
			v=v.replaceFirst("\\{/ST}", "</span>");	
		}	
		
		int start = v.indexOf("{IC:");
		while (start > -1) {
			int stop = v.indexOf("{/IC}");
			String vv = "";
			if (stop>0) {
				vv = v.substring(start, stop+5);
			} else {
				break;
			}
 
			String IconColor = vv.substring(4, 11);
			String IconName = vv.substring(12,vv.length()-5);
			String repl="";
			switch (IconName.substring(0, 3).toLowerCase()) {
			case "mdi":
				repl = "<i style=\"color: " + IconColor + "\" class=\"" + IconName + "\"></i>";
				break;
			case "fa ":
			case "fa-":
				repl = "<i style=\"color: " + IconColor + "\" class=\"" + IconName + "\"></i>";
				break;
			case "abm":
				repl = "<i>" + Page.svgIconmap.getOrDefault(IconName.toLowerCase(), "") + "</i>"; 
				break;
			default:
				repl = "<i style=\"color: " + IconColor + "\" class=\"material-icons\">" + IconName + "</i>";
			}
			v=v.replace(vv,repl );
			start = v.indexOf("{IC:");
		}
		
		s.append(v);
		
		return s.toString();
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMPlanner c = new ABMPlanner();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;
		c.Theme = Theme.Clone();
		c.Type = Type;
		c.mVisibility = mVisibility;		
		return c;
	}
	
	protected class Day {
		protected List<ABMPlannerTask> Tasks = new ArrayList<ABMPlannerTask>();
		protected List<ABMPlannerTask> Structure = new ArrayList<ABMPlannerTask>();
		protected Boolean[] isFrees = new Boolean[24];
		protected boolean IsDirty=true;
		protected boolean IsFullDayEmpty=true;
		protected boolean IsFullDayNA=false;
		
		public Day() {
			Tasks = new ArrayList<ABMPlannerTask>();
			Structure = new ArrayList<ABMPlannerTask>();
			isFrees = new Boolean[24];
			for (int i=0;i<isFrees.length;i++) {
				isFrees[i] = true;
			}
		}
		
		public void Sort() {
			Collections.sort(Tasks);
		}
		
		public ABMPlannerTask GetNextAfter(String taskID, int begin, int len) {
			int c = begin+1;
			while (c < len) {
				ABMPlannerTask tmpTask = Structure.get(c);
				if (tmpTask!=null) {
					if (!tmpTask.mTaskID.equals(taskID)) {
						return tmpTask;
					}
				}
				c++;
			}
			return null;
		}
		
		public void BuildStructure() {			
			Structure = new ArrayList<ABMPlannerTask>();
			for (int i=0;i<24;i++) {
				for (int j=0;j<PerTotal;j++) {
					Structure.add(null);
				}
			}
			for (int i=0;i<Tasks.size();i++) {
				ABMPlannerTask t = Tasks.get(i);
				Structure.set(t.mStartHour*PerTotal+t.mStartMinututesPer, t);
				for (int j=1;j<t.mLengthMinutesPer;j++) {
					Structure.set(t.mStartHour*PerTotal+t.mStartMinututesPer+j, t);
				}
			}
			
		}
	}
	
	protected class Menu {
		String ReturnID="";
		
		String IconName="";
		String IconAwesomeExtra="";
		
		Menu(String returnID, String iconName, String iconAwesomeExtra) {
			this.ReturnID=returnID;
			this.IconName = iconName;
			this.IconAwesomeExtra=iconAwesomeExtra;		
		}
	}
	
	protected class XY {
		int X=Integer.MIN_VALUE;
		int Y=Integer.MAX_VALUE;
	}
}
