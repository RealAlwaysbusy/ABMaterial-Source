package com.ab.abmaterial;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;


@Author("Alain Bailleul")
@Events(values={"BackToStart()", "DataLoaded()", "MovedToSlide(uniqueID as String)", "Added(uniqueID as String)", "Removed(uniqueID as String)"})
@ShortName("ABMTimeLine")
public class ABMTimeLine  extends ABMComponent {
	
	private static final long serialVersionUID = 2593671562418849250L;
	//https://github.com/NUKnightLab/TimelineJS#using-a-method-advanced
	protected ThemeTimeline Theme=new ThemeTimeline();
	protected boolean JSONIsBuild=false;
	protected String FileName="";
	protected ABMTimeLineSlide TitleSlide=new ABMTimeLineSlide();
	public String Scale="human";
	
	protected Map<String,ABMTimeLineSlide> slides = new LinkedHashMap<String,ABMTimeLineSlide>();
	protected List<ABMTimeLineEra> eras = new ArrayList<ABMTimeLineEra>();
	
	protected String ScriptPath="../../js/";
	public String Width="100%";
	public int ScaleFactor=2;
	public int InitialZoomPosInSoomSequence=0;
	public String ZoomSequence="[0.5,1,2,3,5,8,13,21,34,55,89]";
	public boolean TimeNavBottom=true;
	public int OptimalTickWidthPx=100;
	public int TimeNavHeightPx=150;
	public int TimeNavHeightPercentage=25;
	public int TimeNavHeightPercentageMobile=40;
	public int TimeNavMinHeightPx=150;
	public int MarkerMinHeightPx=30;
	public int MarkerMinWidthPx=100;
	public int MarkerPaddingPx=5;
	public int StartAtSlideNumber=Integer.MIN_VALUE;
	public boolean StartAtEnd=false;
	public int AnimationDuration=1000;
	public String AnimationEase="TL.Ease.easeInOutQuint";
	public boolean Dragging=true;
	public boolean TrackResize=true;
	public int SlidePaddingLeftRightPx=100;
	public int SlideDefaultFadePercent=0;
	public String Language="en";
	public String GoogleAnalyticsID="";
	public boolean HideSwipeMessage=false;
	protected String TrackEvents="['back_to_start','data_loaded', 'change', 'added', 'removed']";
	protected boolean DidFirstRun=false;
	
	/**
	 * leave titleSlideUniqueID empty if you do not want a title slide. 
	 */
	public void Initialize(ABMPage page, String id, String titleSlideUniqueID, String titleSlideThemeName, String fileName, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_TIMELINE;	
		this.TitleSlide.Initialize(titleSlideUniqueID, Integer.MIN_VALUE, titleSlideThemeName);
		this.FileName = fileName;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Timelines.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Timelines.get(themeName.toLowerCase()).Clone();				
			}
		}	
		IsInitialized=true;
	}
	
	@Override
	protected void ResetTheme() {
		UseTheme(Theme.ThemeName);		
	}

	
	public void PrepareComponentsForAllSlides() {
		for (Entry<String,ABMTimeLineSlide> entry: slides.entrySet()) {
			ABMTimeLineSlide slide = entry.getValue();
			if (!slide.Container.ID.equals("")) {
				slide.Container.RunAllFirstRuns();					
			}
		}
	}
	
	public void PrepareComponentsForSlide(String uniqueID) {
		ABMTimeLineSlide slide = slides.getOrDefault(uniqueID, null);
		if (slide!=null) {
			String sFile= "";
			if (Page.mStaticFilesFolder.equals("")) {
				sFile = anywheresoftware.b4a.objects.streams.File.getDirApp().replace("\\", "/") + Page.ws.getUpgradeRequest().getRequestURI().replace("/ws/", "/www/");
			} else {
				sFile = Page.mStaticFilesFolder + "/" + Page.mAppName + "/" + Page.Name; 
			}
			File fCurrDir = new File(sFile);
			Page.CurrentDir = fCurrDir.getAbsolutePath();
			File f = new File(Page.CurrentDir, slide.RandomUUIDString + ".json");
			if (f.exists()) {
				f.delete();
			}
			if (!slide.Container.ID.equals("")) {
				slide.Container.RunAllFirstRuns();
				try {
					if (Page.ws.getOpen()) {
						if (Page.ShowDebugFlush) {BA.Log("TimeLine PrepareComponentForSlide: " + ID);}
						Page.ws.Flush();Page.RunFlushed();
					}
				} catch (IOException e) {			
					e.printStackTrace();
				}							
			}
		}
	}
	
	public void DeleteJSONFile(String dir, String fileName) {
		File f = new File(dir,fileName);
		if (f.exists()) {
			f.delete();
		}
	}
	
	public void AddSlide(ABMTimeLineSlide slide) {
		slides.put(slide.UniqueID, slide);
	}
	
	public void RemoveSlide(String uniqueID) {
		slides.remove(uniqueID);
	}
	
	public void AddSlideRunTime(ABMTimeLineSlide slide) {
		StringBuilder s = new StringBuilder();
		String tmpVar = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		tmpVar = tmpVar.replace("-", "_");
		s.append("if ('" + tmpVar + "' in timelines) {");
		s.append("var tmptl = timelines['" + tmpVar + "'];");
		String jsonString="";
		try {
			String sFile= "";
			if (Page.mStaticFilesFolder.equals("")) {
				sFile = anywheresoftware.b4a.objects.streams.File.getDirApp().replace("\\", "/") + Page.ws.getUpgradeRequest().getRequestURI().replace("/ws/", "/www/");
			} else {
				sFile = Page.mStaticFilesFolder + "/" + Page.mAppName + "/" + Page.Name; 
			}
			File fCurrDir = new File(sFile);
			Page.CurrentDir = fCurrDir.getAbsolutePath();
			UUID uuid = UUID.randomUUID();
            slide.RandomUUIDString = uuid.toString();
			File f = new File(Page.CurrentDir, slide.RandomUUIDString + ".json");
			if (f.exists()) {
				f.delete();
			}
			jsonString = slide.toJson(ParentString, ArrayName.toLowerCase(),ArrayName.toLowerCase() + ID.toLowerCase(), Theme).toString();
			BufferedWriter writerDefaults = null;
		    try {
		       	writerDefaults = new BufferedWriter(new FileWriter(f, true));
		       	writerDefaults.write(jsonString);	       	
		    } catch (Exception e) {
		           e.printStackTrace();
		    } finally {
		    	try {
		        	writerDefaults.close();
		        } catch (Exception e) {
		        	e.printStackTrace();
		        }
		    } 
			s.append("$.getJSON('" + slide.RandomUUIDString + ".json', function(data) {");
			
			s.append("tmptl.add(data);");
			s.append("});");
			s.append("}");
			Page.ws.Eval(s.toString(), null);
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("TimeLine AddSlideRuntime: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
			
		} catch (JSONException e1) {			
			e1.printStackTrace();
			return;
		}
		
		AddSlide(slide);
	}
	
	
	
	public void RemoveSlideRunTime(String uniqueID) {
		StringBuilder s = new StringBuilder();
		String tmpVar = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		tmpVar = tmpVar.replace("-", "_");
		s.append("if ('" + tmpVar + "' in timelines) {");
		s.append("var tmptl = timelines['" + tmpVar + "'];");
		s.append("tmptl.removeId('" + uniqueID + "');");
		s.append("}");
		Page.ws.Eval(s.toString(), null);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("TimeLine RemoveSlideRuntime: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
				}
		} catch (IOException e) {			
			e.printStackTrace();
		}		
	}
	
	public ABMTimeLineSlide GetSlide(String uniqueID) {
		return slides.getOrDefault(uniqueID, null);
	}
	
	public void AddEra(ABMTimeLineEra era) {
		eras.add(era);
	}
	
	public void Goto(String uniqueID, boolean fast) {
		StringBuilder s = new StringBuilder();
		String tmpVar = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		tmpVar = tmpVar.replace("-", "_");
		s.append("if ('" + tmpVar + "' in timelines) {");
		s.append("var tmptl = timelines['" + tmpVar + "'];");
		s.append("tmptl.goToId('" + uniqueID + "', " + fast + ");");
		s.append("}");
		Page.ws.Eval(s.toString(), null);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("TimeLine Goto: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}	
	}
	
	public void GotoNext() {
		StringBuilder s = new StringBuilder();
		String tmpVar = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		tmpVar = tmpVar.replace("-", "_");
		s.append("if ('" + tmpVar + "' in timelines) {");
		s.append("var tmptl = timelines['" + tmpVar + "'];");
		s.append("tmptl.goToNext()");
		s.append("}");
		Page.ws.Eval(s.toString(), null);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("TimeLine GotoNext: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}	
	}
	
	public void GotoPrevious() {
		StringBuilder s = new StringBuilder();
		String tmpVar = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		tmpVar = tmpVar.replace("-", "_");
		s.append("if ('" + tmpVar + "' in timelines) {");
		s.append("var tmptl = timelines['" + tmpVar + "'];");
		s.append("tmptl.goToPrev()");
		s.append("}");
		Page.ws.Eval(s.toString(), null);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("TimeLine GotoPrevious: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}	
	}
	
	public void GotoStart() {
		StringBuilder s = new StringBuilder();
		String tmpVar = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		tmpVar = tmpVar.replace("-", "_");
		s.append("if ('" + tmpVar + "' in timelines) {");
		s.append("var tmptl = timelines['" + tmpVar + "'];");
		s.append("tmptl.goToStart()");
		s.append("}");
		Page.ws.Eval(s.toString(), null);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("TimeLine GotoStart: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}	
	}
	
	public void GotoEnd() {
		StringBuilder s = new StringBuilder();
		String tmpVar = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		tmpVar = tmpVar.replace("-", "_");
		s.append("if ('" + tmpVar + "' in timelines) {");
		s.append("var tmptl = timelines['" + tmpVar + "'];");
		s.append("tmptl.goToEnd()");
		s.append("}");
		Page.ws.Eval(s.toString(), null);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("TimeLine GotoEnd: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}	
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
			if (Page.CompleteTheme.Timelines.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Timelines.get(themeName.toLowerCase()).Clone();				
			}
		}
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
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("TimeLine Refresh: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
		if (!DidFirstRun) {			
			for (Entry<String,ABMTimeLineSlide> entry: slides.entrySet()) {
				ABMTimeLineSlide slide = entry.getValue();
				if (!slide.Container.ID.equals("")) {
					slide.Container.RunAllFirstRuns();					
				}
			}
			DidFirstRun=true;
		}
		super.FirstRun();
		
	}
	
	protected String BuildJavaScript() {
		StringBuilder s = new StringBuilder();	
		String tmpVar = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		tmpVar = tmpVar.replace("-", "_");
		s.append("var options = {");
		s.append("script_path: '" + ScriptPath + "',");
		s.append("width: '" + Width + "',");
		s.append("is_full_embed: true,");
		s.append("zdepth: '" + Theme.ZDepth + "',");
		s.append("hideswipemessage: " + HideSwipeMessage + ",");
		s.append("parentString: '" + ParentString + "',");
		s.append("arrayName: '" + ArrayName.toLowerCase() + "',");
		s.append("elemID: '" + ArrayName.toLowerCase() + ID.toLowerCase() + "',");
		s.append("default_bg_color: '" + ABMaterial.GetColorStrMap(Theme.DefaultBackgroundColor, Theme.DefaultBackgroundColorIntensity) + "',");
		s.append("scale_factor: " + ScaleFactor + ",");
		if (this.InitialZoomPosInSoomSequence!=0) s.append("initial_zoom: " + this.InitialZoomPosInSoomSequence + ",");
		s.append("zoom_sequence: " + this.ZoomSequence + ",");
		if (ABMaterial.CDN) {
			s.append("cdnurl: '" + ABMaterial.mCDNUrl + "',");
		} else {
			s.append("cdnurl: '/',");
		}
		if (!this.TimeNavBottom) s.append("timenav_position: 'top',");
		if (this.OptimalTickWidthPx!=100) s.append("optimal_tick_width: " + this.OptimalTickWidthPx + ",");
		if (this.TimeNavHeightPx!=150) s.append("timenav_height: " + this.TimeNavHeightPx + ",");
		if (this.TimeNavHeightPercentage!=25) s.append("timenav_height_percentage: " + this.TimeNavHeightPercentage + ",");
		if (this.TimeNavHeightPercentageMobile!=40) s.append("timenav_mobile_height_percentage: " + this.TimeNavHeightPercentageMobile + ",");
		if (this.TimeNavMinHeightPx!=150) s.append("timenav_height_min: " + this.TimeNavMinHeightPx + ",");
		if (this.MarkerMinHeightPx!=30) s.append("marker_height_min: " + this.MarkerMinHeightPx + ",");
		if (this.MarkerMinWidthPx!=100) s.append("marker_width_min: " + this.MarkerMinWidthPx + ",");
		if (this.MarkerPaddingPx!=5) s.append("marker_padding: " + this.MarkerPaddingPx + ",");
		if (this.StartAtSlideNumber!=Integer.MIN_VALUE) s.append("start_at_slide: " + this.StartAtSlideNumber + ",");
		if (this.StartAtEnd) s.append("start_at_end: true,");
		if (this.AnimationDuration!=1000) s.append("duration: " + this.AnimationDuration + ",");
		if (!this.AnimationEase.equals("TL.Ease.easeInOutQuint")) s.append("ease: " + this.AnimationEase + ",");
		if (!this.Dragging) s.append("dragging: false,");
		if (!this.TrackResize) s.append("trackResize: false,");
		if (this.SlidePaddingLeftRightPx!=100) s.append("slide_padding_lr: " + this.SlidePaddingLeftRightPx + ",");
		if (this.SlideDefaultFadePercent!=0) s.append("slide_default_fade: '" + this.SlideDefaultFadePercent + "%',");
		if (!this.Language.equals("en")) s.append("language: '" + this.Language + "',");
		if (!this.GoogleAnalyticsID.equals("")) s.append("ga_property_id: '" + this.GoogleAnalyticsID + "',");
		s.append("track_events: " + this.TrackEvents);
		s.append("}\n");
		s.append("timelines['" + tmpVar + "'] = new TL.Timeline('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "','" + FileName + "',options);" );
		return s.toString();
	}
	
	protected void BuildJSON() throws JSONException {
		JSONObject tl = new JSONObject();
		tl.put("scale", Scale);
		if (!TitleSlide.UniqueID.equals("")) {
			tl.put("title", TitleSlide.toJson(ParentString, ArrayName.toLowerCase(), ArrayName.toLowerCase() + ID.toLowerCase(), Theme));
		}
		if (eras.size()>0) {
			JSONArray ers = new JSONArray();
			for (int i=0;i<eras.size();i++) {
				ers.put(eras.get(i).toJson());
			}
			tl.put("eras", ers);
		}
		if (slides.size()>0) {
			JSONArray evs = new JSONArray();
			for (Entry<String,ABMTimeLineSlide> entry: slides.entrySet()) {
				evs.put(entry.getValue().toJson(ParentString, ArrayName.toLowerCase(), ArrayName.toLowerCase() + ID.toLowerCase(), Theme));
			}
			tl.put("events", evs);
		}
		
		File f = new File(Page.CurrentDir, FileName);
		if (f.exists()) {
			f.delete();
		}
		BufferedWriter writerDefaults = null;
	    try {
	       	writerDefaults = new BufferedWriter(new FileWriter(f, true));
	       	writerDefaults.write(tl.toString());	       	
	    } catch (Exception e) {
	           e.printStackTrace();
	    } finally {
	    	try {
	        	writerDefaults.close();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	    } 
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		// TODO ExtraClasses not working
		super.Refresh();
		ABMaterial.ChangeVisibility(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), mVisibility);
		if (mIsPrintableClass.equals("")) {
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "no-print");
		} else {
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "no-print");
		}
		if (mIsOnlyForPrintClass.equals("")) {
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "only-print");
		} else {
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "only-print");
		}
		
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Timeline Refresh : " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		
	}
	
	@Override
	protected String Build() {
		if (Page.ws!=null) {
			String sFile= "";
			if (Page.mStaticFilesFolder.equals("")) {
				sFile = anywheresoftware.b4a.objects.streams.File.getDirApp().replace("\\", "/") + Page.ws.getUpgradeRequest().getRequestURI().replace("/ws/", "/www/");
			} else {
				sFile = Page.mStaticFilesFolder + "/" + Page.mAppName + "/" + Page.Name; 
			}
			File f = new File(sFile);
			
			Page.CurrentDir = f.getAbsolutePath();
		}
		if (!JSONIsBuild) {
			BA.Log("Building JSON");
			try {
				BuildJSON();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			JSONIsBuild=true;
		}
		if (Theme.ThemeName.equals("default")) {
			Theme.Colorize(Page.CompleteTheme.MainColor);
		}
		StringBuilder s = new StringBuilder();	
		s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"" + Theme.ZDepth + " " + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() + "\">");
		s.append(BuildBody());
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
		return s.toString();
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMTimeLine c = new ABMTimeLine();
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
		
}
