package com.ab.abmaterial;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;


@Author("Alain Bailleul")  
@ShortName("ABMSVGSurface")
@Events(values={"SVGClicked(elementID as String)","SVGTicked(timerID as String, extra as String)"})
public class ABMSVGSurface extends ABMComponent {
	
	private static final long serialVersionUID = 3037853773764496031L;
	protected String ViewBox="";
	protected Map<String, ABMSVGElement> Elements = new LinkedHashMap<String, ABMSVGElement>();
	protected String PreserveAspectRatio="xMidYmid meet";
	protected String SvgWidthPx="";
	protected String SvgHeightPx="";
	protected ABMContainer svgCont=null;
	
	public void Initialize(ABMPage page, String id, String viewbox, String preserveAspectRatio, String svgWidthPx, String svgHeightPx) {
		this.ID = id;			
		this.Page = page;
		if (viewbox!="") {
			this.ViewBox = " viewBox=\"" + viewbox + "\"";
		}
		if (preserveAspectRatio!="") {
			this.PreserveAspectRatio = " preserveAspectRatio=\"" + preserveAspectRatio + "\"";
		}
		if (svgWidthPx!="") {
			this.SvgWidthPx=" width=\"" + svgWidthPx + "\"";
		}
		if (svgHeightPx!="") {
			this.SvgHeightPx=" height=\"" + svgHeightPx + "\"";
		}
		this.Type = ABMaterial.UITYPE_SVGSURFACE;	
		IsInitialized=true;
	}
	
	@Override
	protected void ResetTheme() {
		
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
		
	}	
	
	
	public void UseAsContainerBackground(ABMContainer container) {
		this.svgCont = container;
	}
	
		
	@Override
	protected void CleanUp() {
		svgCont=null;
		super.CleanUp();
	}
	
	@Override
	protected void RemoveMe() {
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
	}
		
	@Override
	protected void FirstRun() {
		Page.ws.Eval(BuildJavaScript(), null);
		
		if (svgCont!=null) {
			String surID = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
			surID = surID.replace("-", "_");
			
			String tmpStr = "var svg64 = \"data:image/svg+xml;base64,\" + window.btoa(svgs['" + surID + "'].toDataURL()) + \"\";console.log(svg64);$('#" + svgCont.ParentString + svgCont.ArrayName.toLowerCase() + svgCont.ID.toLowerCase() + "').css({'background-image': 'url(' + svg64 + ')', 'background-size': 'cover', 'background-repeat': 'no-repeat'});";
			
			Page.ws.Eval(tmpStr, null);
		}
		super.FirstRun();
	}
	
	protected String BuildJavaScript() {
		StringBuilder s = new StringBuilder();	
		String surID = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		
		surID = surID.replace("-", "_");
				
		s.append("if (typeof svgs['" + surID + "']=='undefined') {svgs['" + surID + "']=Snap('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "');}");
		for (Entry<String, ABMSVGElement> entry: Elements.entrySet()) {
			if (entry.getValue().IsDirty) {
				ABMSVGElement el = entry.getValue();
				String elID = surID + el.getElementID().toLowerCase();
				if (el.IsNew) {
					String v = el.initString.replace("$EL$", "'" + elID + "'").replace("$SUR$", "'" + surID + "'").replace("$SUR2$", surID);
					s.append(v);
					el.IsNew = false;
					if (el.HasEvents) {
						s.append("svgse['" + elID + "'].mouseup( function(e){");
						if (ArrayName!="") {
							s.append("b4j_raiseEvent('page_parseevent', {'eventname':'" + ArrayName.toLowerCase() + "_svgclicked','eventparams':'elementid', 'elementid':'" + el.getElementID() + "'});");
						} else {
							s.append("b4j_raiseEvent('page_parseevent', {'eventname':'" + ID.toLowerCase() + "_svgclicked','eventparams':'elementid', 'elementid':'" + el.getElementID() + "'});");
						}
						s.append("});");
					}
					if (el.HasHover) {
						s.append(el.GetHovers().replace("$EL$", "svgse['" + elID + "']"));
						el.HasHover = false;						
					}
				}
				if (el.mAttributes!="") {
					s.append("svgse['" + elID + "'].attr(" + el.mAttributes + ");");
					el.mAttributes = "";
				}
				if (!el.HasAnim) {
					s.append("svgstop['" + elID + "']='1';");
				} else {
					s.append("svgstop['" + elID + "']='0';");
				}
				if (el.HasAnim) {
					s.append(el.GetAnimations().replace("$EL$", "svgse['" + elID + "']").replace("$EL2$", "'" + elID + "'"));
					el.HasAnim=false;
				}				
				if (el.mMask!="") {
					s.append(el.mMask.replace("$EL$", "'" + elID + "'").replace("$SUR2$", surID));
					el.mMask="";
				}
				if (el.mDirection!="") {
					s.append("svgse['" + elID + "']" + el.mDirection);
					el.mDirection="";
				}
				if (el.mVis!="") {
					s.append("svgse['" + elID + "']" + el.mVis);
					el.mVis="";
				}
				el.IsDirty = false;
			}
		}
		
		return s.toString();
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
		
		StringBuilder s = new StringBuilder();	
		String surID = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		surID = surID.replace("-", "_");
		for (Entry<String, ABMSVGElement> entry: Elements.entrySet()) {
			if (entry.getValue().IsDirty) {
				ABMSVGElement el = entry.getValue();
				String elID = surID + el.getElementID().toLowerCase();
				if (el.IsNew) {
					s.append(el.initString.replace("$EL$", "'" + elID + "'").replace("$SUR$", "'" + surID + "'").replace("$SUR2$", surID));
					el.IsNew = false;
					
					if (el.HasHover) {
						s.append(el.GetHovers().replace("$EL$", "svgse['" + elID + "']"));
						el.HasHover = false;
						
					}
				}
				if (el.mAttributes!="") {
					s.append("svgse['" + elID + "'].attr(" + el.mAttributes + ");");
					el.mAttributes = "";
				}
				if (!el.HasAnim) {
					s.append("svgstop['" + elID + "']='1';");
				} else {
					s.append("svgstop['" + elID + "']='0';");
				}
				if (el.HasAnim) {
					s.append(el.GetAnimations().replace("$EL$", "svgse['" + elID + "']"));
					el.HasAnim=false;
				}
				
				if (el.mMask!="") {
					s.append(el.mMask.replace("$EL$", "'" + elID + "'").replace("$SUR2$", surID));
					el.mMask="";
				}
				if (el.mDirection!="") {
					s.append("svgse['" + elID + "']" + el.mDirection + ";");
					el.mDirection="";
				}
				if (el.mVis!="") {
					s.append("svgse['" + elID + "']" + el.mVis + ";");
					el.mVis="";
				}
				
				el.IsDirty = false;
			}
		}
		if (s.toString()!="") {
			Page.ws.Eval(s.toString(), null);			
			if (DoFlush) {
				try {
					if (Page.ws.getOpen()) {
						if (Page.ShowDebugFlush) {BA.Log("SVGSurface Refresh 1: " + ID);}
						Page.ws.Flush();Page.RunFlushed();
					}
				} catch (IOException e) {			
					//e.printStackTrace();
				}
			}
		}
		
		if (svgCont!=null) {
			String tmpStr = "var svg64 = \"data:image/svg+xml;base64,\" + window.btoa(svgs['" + surID + "'].toDataURL()) + \"\";$('#" + svgCont.ParentString + svgCont.ArrayName.toLowerCase() + svgCont.ID.toLowerCase() + "').css({'background-image': 'url(' + svg64 + ')', 'background-size': 'cover', 'background-repeat': 'no-repeat'});";
			
			Page.ws.Eval(tmpStr, null);
			if (DoFlush) {
				try {
					if (Page.ws.getOpen()) {
						if (Page.ShowDebugFlush) {BA.Log("SVGSurface Refresh 2: " + ID);}
						Page.ws.Flush();Page.RunFlushed();
					}
				} catch (IOException e) {			
					//e.printStackTrace();
				}
			}
		}

		
	}
	
	@Override
	protected String Build() {	
		StringBuilder s = new StringBuilder();	

		s.append("<svg id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"" + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() + "\" " + SvgWidthPx + SvgHeightPx + ViewBox + PreserveAspectRatio + ">");
		s.append(BuildBody());
		s.append("</svg>");
		s.append("<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" width=\"100%\" height=\"0\"></svg>");
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
	
	public ABMSVGElement GetElement(String elementID) {
		return Elements.getOrDefault(elementID.toLowerCase(), null);
	}
	
	public void RemoveElement(String elementID) {
		Elements.remove(elementID.toLowerCase());		
	}
	
	public void Clear() {
		Elements.clear();
		String surID = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		surID = surID.replace("-", "_");
		String s = "svgs['" + surID + "'].clear();";
		Page.ws.Eval(s, null);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("SVGSurface Clear: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public ABMSVGElement Circle(String elementID, int x, int y, int radius) {
		ABMSVGElement el = new ABMSVGElement(true);
		el.Initialize(elementID, ABMaterial.SVGELEMENT_CIRCLE);
		
		el.initString = "svgse[$EL$]=svgs[$SUR$].circle(" + x + "," + y + "," + radius + ");";
		Elements.put(elementID.toLowerCase(), el);
		return el;
	}
	
	public ABMSVGElement Ellipse(String elementID, int x, int y, int radiusX, int radiusY) {
		ABMSVGElement el = new ABMSVGElement(true);
		el.Initialize(elementID, ABMaterial.SVGELEMENT_ELLIPSE);
		
		el.initString = "svgse[$EL$]=svgs[$SUR$].ellipse(" + x + "," + y + "," + radiusX + "," + radiusY + ");";
		Elements.put(elementID.toLowerCase(), el);
		return el;
	}
	
	public ABMSVGElement Image(String elementID, String imageUrl, int x, int y, int width, int height) {
		ABMSVGElement el = new ABMSVGElement(true);
		el.Initialize(elementID, ABMaterial.SVGELEMENT_IMAGE);
			
		el.initString = "svgse[$EL$]=svgs[$SUR$].image('" + imageUrl + "'," + x + "," + y + "," + width + "," + height + ");";
		Elements.put(elementID.toLowerCase(), el);
		return el;
	}
	
	public ABMSVGElement Line(String elementID, int x1, int y1, int x2, int y2) {
		ABMSVGElement el = new ABMSVGElement(true);
		el.Initialize(elementID, ABMaterial.SVGELEMENT_LINE);
			
		el.initString = "svgse[$EL$]=svgs[$SUR$].line(" + x1 + "," + y1 + "," + x2 + "," + y2 + ");";
		Elements.put(elementID.toLowerCase(), el);
		return el;
	}
	
	/**
	 * see https://www.w3.org/TR/SVG/paths.html#PathData for more info on how to build an SVG path 
	 * e.g. M10 10L90 90
	 */
	public ABMSVGElement Path(String elementID, String path) {
		ABMSVGElement el = new ABMSVGElement(true);
		el.Initialize(elementID, ABMaterial.SVGELEMENT_PATH);
			
		el.initString = "svgse[$EL$]=svgs[$SUR$].path('" + path + "');";
		Elements.put(elementID.toLowerCase(), el);
		return el;
	}
	
	/**
	 * string of points, comma delimited
	 * e.g. 10, 10, 100, 100 
	 */
	public ABMSVGElement Polyline(String elementID, String points) {
		ABMSVGElement el = new ABMSVGElement(true);
		el.Initialize(elementID, ABMaterial.SVGELEMENT_POLYLINE);
			
		el.initString = "svgse[$EL$]=svgs[$SUR$].polyline([" + points + "]);";
		Elements.put(elementID.toLowerCase(), el);
		return el;
	}
	
	public ABMSVGElement Rect(String elementID, int x, int y, int width, int height, int radiusX, int radiusY) {
		ABMSVGElement el = new ABMSVGElement(true);
		el.Initialize(elementID, ABMaterial.SVGELEMENT_RECT);
			
		el.initString = "svgse[$EL$]=svgs[$SUR$].rect(" + x + "," + y + "," + width + "," + height + "," + radiusX + "," + radiusY + ");";
		Elements.put(elementID.toLowerCase(), el);
		return el;
	}
	
	/**
	 * text can be a string
	 * e.g. "SNAP" 
	 */
	public ABMSVGElement Text(String elementID, int x, int y, String text) {
		ABMSVGElement el = new ABMSVGElement(true);
		el.Initialize(elementID, ABMaterial.SVGELEMENT_TEXT);
			
		el.initString = "svgse[$EL$]=svgs[$SUR$].text(" + x + "," + y + ",\"" + text + "\");";
		Elements.put(elementID.toLowerCase(), el);
		return el;
	}
	
	/**
	 * text can be an array of strings, comma delimited 
	 * e.g. Array as String("S","n","a","p") 
	 */
	public ABMSVGElement TextArray(String elementID, int x, int y, anywheresoftware.b4a.objects.collections.List textArray) {
		ABMSVGElement el = new ABMSVGElement(true);
		el.Initialize(elementID, ABMaterial.SVGELEMENT_TEXTARRAY);
		
		String str = "";
		for (int i=0;i<textArray.getSize();i++) {
			if (i>0) {
				str += ",";
			}
			str += "\"" + (String)textArray.Get(i) + "\""; 
		}
			
		el.initString = "svgse[$EL$]=svgs[$SUR$].text(" + x + "," + y + ",[" + str + "]);";
		Elements.put(elementID.toLowerCase(), el);
		return el;
	}
	
	/**
	 * string of points, comma delimited
	 * e.g. 10, 10, 100, 100 
	 */
	public ABMSVGElement Polygon(String elementID, String points) {
		ABMSVGElement el = new ABMSVGElement(true);
		el.Initialize(elementID, ABMaterial.SVGELEMENT_POLYGON);
			
		el.initString = "svgse[$EL$]=svgs[$SUR$].polygon([" + points + "]);";
		Elements.put(elementID.toLowerCase(), el);
		return el;
	}
	
	public ABMSVGElement Arc(String elementID, int x, int y, int radius, int startAngle, int endAngle) {
		ABMSVGElement el = new ABMSVGElement(true);
		el.Initialize(elementID, ABMaterial.SVGELEMENT_ARC);
		
		StringBuilder s = new StringBuilder();
			
		s.append("var start = polarToCartesian(" + x + ", " + y+ "," + radius + ", " + endAngle + ");");
		s.append("var end = polarToCartesian(" + x + ", " + y + ", " + radius + ", " + startAngle + ");");
		s.append("var arcSweep = " + endAngle + " - " + startAngle + " <= 180 ? \"0\" : \"1\";");
		s.append("var d = [\"M\", start.x, start.y, \"A\", " + radius + ", " + radius + ", 0, arcSweep, 0, end.x, end.y].join(\" \");");		
		
		el.initString = s.toString();
		
		el.initString += "svgse[$EL$]=svgs[$SUR$].path(d);";
		Elements.put(elementID.toLowerCase(), el);
		return el;
	}
	
	public ABMSVGElement Pie(String elementID, int x, int y, int innerRadius, int outerRadius, int startAngle, int endAngle) {
		ABMSVGElement el = new ABMSVGElement(true);
		el.Initialize(elementID, ABMaterial.SVGELEMENT_TEXT);
			
		el.initString = "svgse[$EL$]=svgs[$SUR$].pie(" + x + "," + y + "," + innerRadius + "," + outerRadius + "," + startAngle + "," + endAngle + ");";
		Elements.put(elementID.toLowerCase(), el);
		return el;
	}
	
	/**
	 * if times is 0 then the timer runs until stopped, killed or paused 
	 * if times is reached, the timer will be killed automatically so,
	 * you have to recreate it if you want to re-use it.
	 * 
	 * Note: these timers have to go back and forth to the server, so use with caution!
	 */
	public void StartTimer(String timerID, long interval, int times) {
		StringBuilder s = new StringBuilder();
		String surID = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		surID = surID.replace("-", "_");
		String elID = surID + timerID.toLowerCase();
		switch (times) {
		case 0:
			s.append("svgst['" + elID + "']=TimersJS.repeater(" + interval + ", function(delta) {");			
			s.append("b4j_raiseEvent('page_parseevent', {'eventname':'" + ArrayName.toLowerCase() + ID.toLowerCase() + "_svgticked','eventparams':'timerid,extra', 'timerid':'" + timerID + "', 'extra':''+delta});");
			s.append("});");
			break;
		case 1:
			s.append("svgst['" + elID + "']=TimersJS.oneShot(" + interval + ", function() {");			
			s.append("b4j_raiseEvent('page_parseevent', {'eventname':'" + ArrayName.toLowerCase() + ID.toLowerCase() + "_svgticked','eventparams':'timerid,extra', 'timerid':'" + timerID + "', 'extra':''});");
			s.append("});");
			break;
		default:
			s.append("svgst['" + elID + "']=TimersJS.multi(" + interval + ", " + times  + ", function(repetition) {");			
			s.append("b4j_raiseEvent('page_parseevent', {'eventname':'" + ArrayName.toLowerCase() + ID.toLowerCase() + "_svgticked','eventparams':'timerid,extra', 'timerid':'" + timerID + "', 'extra':''+repetition});");
			s.append("});");
			break;
		}
		if (Page.ws!=null) {
			Page.ws.Eval(s.toString(), null);
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("SVGSurface StartTimer: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
	}	
	
	public void KillTimer(String timerID) {
		String surID = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		surID = surID.replace("-", "_");
		String elID = surID + timerID.toLowerCase();
		String s = "svgst['" + elID + "'].kill();";
		if (Page.ws!=null) {
			Page.ws.Eval(s, null);
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("SVGSurface KillTimer: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
	}
	
	public void RestartTimer(String timerID) {
		String surID = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		surID = surID.replace("-", "_");
		String elID = surID + timerID.toLowerCase();
		String s = "svgst['" + elID + "'].restart();";
		if (Page.ws!=null) {
			Page.ws.Eval(s, null);
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("SVGSurface RestartTimer: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}		
	}
	
	public void PauseTimer(String timerID) {
		String surID = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		surID = surID.replace("-", "_");
		String elID = surID + timerID.toLowerCase();
		String s = "svgst['" + elID + "'].pause();";
		if (Page.ws!=null) {
			Page.ws.Eval(s, null);
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("SVGSurface PauseTimer: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
	}
	
	public void CancelTimer(String timerID) {
		String surID = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		surID = surID.replace("-", "_");
		String elID = surID + timerID.toLowerCase();
		String s = "svgst['" + elID + "'].cancel();";
		if (Page.ws!=null) {
			Page.ws.Eval(s, null);
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("SVGSurface CancelTimer: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
	}
	
	public ABMSVGElement Group(String groupID, anywheresoftware.b4a.objects.collections.List elementIDs) {
		ABMSVGElement el = new ABMSVGElement(true);
		el.Initialize(groupID, ABMaterial.SVGELEMENT_GROUP);
		
		String grp="";
		for (int i=0;i<elementIDs.getSize();i++) {
			if (i>0) {
				grp += ",";
			}
			grp += "svgse['$SUR2$" + (String)elementIDs.Get(i) + "']"; 
		}
		
		el.initString = "svgse[$EL$]=svgs[$SUR$].group(" + grp + ");";
		Elements.put(groupID.toLowerCase(), el);
		return el;
	}
	
	public void SetMask(String elementID, String maskElementID) {
		ABMSVGElement mask = Elements.getOrDefault(maskElementID.toLowerCase(), null);
		if (mask==null) {
			BA.Log("Mask " + maskElementID + " not found!");
			return;
		}
		String surID = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		surID = surID.replace("-", "_");
		
		String maskID = "$SUR2$" + maskElementID.toLowerCase();				
		mask.SetMask("svgse[$EL$].attr({mask: svgse['" + maskID + "']});");
	}
	
	
	@Hide
	@Override
	protected void Prepare() {	
		for (Entry<String, ABMSVGElement> entry: Elements.entrySet()) {
			ABMSVGElement el = entry.getValue();
			el.IsDirty = false;
			el.IsNew = false;
		}
	}
	
	protected void Reset() {
		for (Entry<String, ABMSVGElement> entry: Elements.entrySet()) {
			entry.getValue().BackToClone();			
		}
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMSVGSurface c = new ABMSVGSurface();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;		
		c.Type = Type;
		c.mVisibility = mVisibility;		
		return c;
	}
		

}
