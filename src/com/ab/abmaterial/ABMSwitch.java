package com.ab.abmaterial;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;
import anywheresoftware.b4j.object.WebSocket.SimpleFuture;

@Author("Alain Bailleul")  
@ShortName("ABMSwitch")
@Events(values={"Clicked(Target As String)"})
public class ABMSwitch extends ABMComponent {
	
	private static final long serialVersionUID = -87916805227792559L;
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	protected ThemeSwitch Theme=new ThemeSwitch();
	public String OnText="";
	public String OffText="";
	public String Title="";
	protected boolean sState=false;
	public transient SimpleFuture FutureChecked=null;
	protected boolean mEnabled=true;
	protected boolean IsDirty=false;
	
	protected boolean GotLastState=true;
	
	
	public void Initialize(ABMPage page, String id, String onText, String offText, boolean state, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_SWITCH;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Switches.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Switches.get(themeName.toLowerCase()).Clone();				
			}
		}		
		this.OnText=onText;
		this.OffText=offText;
		this.sState=state;
		IsInitialized=true;
		IsDirty=false;
		IsVisibilityDirty=false;
		IsEnabledDirty=false;
	}
	
	public void Initialize2(ABMPage page, String id, String title, String onText, String offText, boolean state, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_SWITCH;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Switches.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Switches.get(themeName.toLowerCase()).Clone();				
			}
		}			
		this.Title = title;
		this.OnText=onText;
		this.OffText=offText;
		this.sState=state;
		IsInitialized=true;
		IsDirty=false;
		IsVisibilityDirty=false;
		IsEnabledDirty=false;
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
	
	public void setState(boolean state) {
		this.sState = state;		
		IsDirty = true;
	}
	
	public boolean getEnabled() {	
		if (GotLastEnabled) {
			return mEnabled;
		}
		GotLastEnabled=true;
		if (!mB4JSUniqueKey.equals("") && !IsEnabledDirty) {
			if (JQ!=null) {
				anywheresoftware.b4a.objects.collections.List par = new anywheresoftware.b4a.objects.collections.List();
				par.Initialize();
				par.Add(ParentString + RootID() + "input");
				FutureEnabled = JQ.RunMethodWithResult("GetEnabled", par);				
			} else {
				FutureEnabled=null;				
			}
			if (!(FutureEnabled==null)) {
				try {
					//BA.Log(ID);
					this.mEnabled = ((Boolean)FutureEnabled.getValue());					
				} catch (InterruptedException e) {					
					e.printStackTrace();
				} catch (TimeoutException e) {					
					e.printStackTrace();
				} catch (ExecutionException e) {					
					e.printStackTrace();
				} catch (IOException e) {					
					e.printStackTrace();
				}
			} else {
				//BA.Log("FutureText = null");
				
			}	
			return mEnabled;
		}
		return mEnabled;
	}
	
	public void setEnabled(boolean enabled) {
		IsEnabledDirty=true;
		GotLastEnabled=true;
		mEnabled=enabled;
	}
	
	public boolean getState() {
		if (GotLastState) {
			return sState;
		}
		GotLastState=true;
		if (!IsDirty) {
			if (FutureChecked!=null) {
				try {
					this.sState = (boolean) FutureChecked.getValue();						
				} catch (InterruptedException e) {					
					e.printStackTrace();
				} catch (TimeoutException e) {					
					e.printStackTrace();
				} catch (ExecutionException e) {					
					e.printStackTrace();
				} catch (IOException e) {					
					e.printStackTrace();
				}
			} else {
				BA.Log("FutureChecked = null");
			}
		}
		return sState;
	}
	
	@Override
	public String getVisibility() {
		if (GotLastVisibility) {
			return mVisibility;
		}
		GotLastVisibility=true;
		if (!mB4JSUniqueKey.equals("") && !IsVisibilityDirty) {
			if (JQ!=null) {
				anywheresoftware.b4a.objects.collections.List par = new anywheresoftware.b4a.objects.collections.List();
				par.Initialize();
				par.Add(ParentString + RootID());
				FutureVisibility = JQ.RunMethodWithResult("GetVisibility", par);				
			} else {
				FutureVisibility=null;				
			}
			if (!(FutureVisibility==null)) {
				try {
					//BA.Log(ID);
					this.mVisibility = ((String)FutureVisibility.getValue());					
				} catch (InterruptedException e) {					
					e.printStackTrace();
				} catch (TimeoutException e) {					
					e.printStackTrace();
				} catch (ExecutionException e) {					
					e.printStackTrace();
				} catch (IOException e) {					
					e.printStackTrace();
				}
			} else {
				//BA.Log("FutureText = null");
				
			}	
			return mVisibility;
		}
		
		return mVisibility;		
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public void setB4JSState(boolean state) {
		
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public boolean getB4JSState() {		
		return false;
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public void setB4JSVisibility(String visibility) {
		
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public String getB4JSVisibility() {
		return "B4JS Dummy";
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public boolean getB4JSEnabled() {		
		return false;
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public void setB4JSEnabled(boolean enabled) {
		
	}
	
	/**
	 * every method you want to call with a B4JSOn... call MUST return a boolean
	 * returning true will consume the event in the browser and not call the B4J event (if any)
	 *
	 * e.g. myButton.B4JSOnClicked("MyJavascript", "AddToLabel", Array As Object(myCounter))
	 * if AddToLabel return true, then myButton_Clicked() will not be raised.
	 * if AddToLabel returns false, then myButton_Clicked() will be raised AFTER the B4JS method is done.
	 * 
	 * public Sub AddToLabel(MyCounter As Int) As Boolean
	 *      if myCounter mod 2 = 0 then	       
	 *		   Return True
	 *      else
	 *         Return False
	 *      end if
	 * End Sub
	 */
	public void B4JSOnClick(String B4JSClassName, String B4JSMethod, anywheresoftware.b4a.objects.collections.List Params) {
		PrepareEvent("B4JSOnClick", B4JSClassName, B4JSMethod, Params);
	}
		
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Switches.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Switches.get(themeName.toLowerCase()).Clone();				
			}
		}
	}
	
	public void SetTooltip(String text, String position, int delay) {
		this.ToolTipText = text;
		this.ToolTipPosition = position;
		this.ToolTipDelay = delay;			
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
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "input");		
		Page.ws.RunFunction("inittooltipped", Params);
		
		super.FirstRun();
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		super.RefreshInternal(DoFlush);
		
		getState();
		getVisibility();
		getEnabled();
		
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		
		ThemeSwitch sw = Theme;
		j.SetProp("class", "switch" + sw.ThemeName + " " + mVisibility + " " + sw.ZDepth + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses());
		
		j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label");	
		j.SetProp("class", ABMaterial.GetColorStr(sw.LabelColor, sw.LabelColorIntensity, "text"));
		
		if (!Title.equals("")) {
			j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "title");	
			j.SetProp("class", ABMaterial.GetColorStr(sw.TitleColor, sw.TitleColorIntensity, "text"));
		}
		
		j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "input");
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "input");
		Params.Add(!mEnabled);
		Page.ws.RunFunction("SetDisabled", Params);
		
		if (IsDirty) {
			anywheresoftware.b4a.objects.collections.List ParamsCh = new anywheresoftware.b4a.objects.collections.List();
			ParamsCh.Initialize();
			ParamsCh.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "input");
			ParamsCh.Add(sState);
			Page.ws.RunFunction("SetChecked", ParamsCh);
		}
		
		if (!ToolTipText.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "input", "data-position", ToolTipPosition);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "input", "data-delay", "" + ToolTipDelay);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "input", "data-tooltip", ToolTipText);
		}
		String toolTip2="";
		if (!ToolTipText.equals("")) {
			toolTip2=" tooltipped ";
		}
		j.SetProp("class", toolTip2);
		
		j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-on");
		j.SetHtml(BuildBodyInfo(OnText));
		j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-off");
		j.SetHtml(BuildBodyInfo(OffText));
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Switch Refresh : " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		IsDirty=false;
	}
	
	@Override
	protected String Build() {
		if (Theme.ThemeName.equals("default")) {
			Theme.Colorize(Page.CompleteTheme.MainColor);
		}
		StringBuilder s = new StringBuilder();
		ThemeSwitch sw=Theme;
		String toolTip="";
		String toolTip2="";
		if (!ToolTipText.equals("")) {
			toolTip=" data-position=\"" + ToolTipPosition + "\" data-delay=\"" + ToolTipDelay + "\" data-tooltip=\"" + ToolTipText + "\" "; 
			toolTip2=" tooltipped ";
		}
		if (!Title.equals("")) {
			s.append("<label id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "title\" class=\"" + ABMaterial.GetColorStr(sw.TitleColor, sw.TitleColorIntensity, "text") + " " + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() + "\">" + BuildBodyInfo(Title) + "</label>\n");
		}
		String B4JSData="";
		if (!mB4JSUniqueKey.equals("")) {
			B4JSData = " data-b4js=\"" + mB4JSUniqueKey + "\" data-b4jsextra=\"\" ";
		} else {
			B4JSData = " data-b4js=\"\" data-b4jsextra=\"\" ";
		}
		s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\"" + B4JSData + "class=\"switch" + sw.ThemeName + " " + mVisibility + " " + sw.ZDepth + mIsPrintableClass + mIsOnlyForPrintClass + "\">\n");
		s.append("<label id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label\" class=\"" + ABMaterial.GetColorStr(sw.LabelColor, sw.LabelColorIntensity, "text") + "\">\n");
		
		s.append("<span id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-off\">" + BuildBodyInfo(OffText) + "</span>");
		String disabledStr="";
		if (!mEnabled) {
			disabledStr = " disabled ";
		}
		if (sState) {
			s.append("<input id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "input\" checked " + disabledStr + toolTip + " type=\"checkbox\" onclick=\"switchclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "')\" class=\"" + toolTip2 + "\">\n");
		} else {
			s.append("<input id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "input\" " + disabledStr + toolTip + " type=\"checkbox\" onclick=\"switchclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "')\" class=\"" + toolTip2 + "\">\n");
		}
		s.append("<span class=\"lever" + sw.ThemeName + "\"></span>\n" );
		s.append("<span id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-on\">" + BuildBodyInfo(OnText) + "</span>");
		s.append("</label>\n");
		s.append("</div>\n");
		
		IsBuild=true;
		IsDirty=false;
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
	
	protected String BuildBodyInfo(String text) {
		StringBuilder s = new StringBuilder();	
		
		String v = ABMaterial.HTMLConv().htmlEscape(text, Page.PageCharset);
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
		ABMSwitch c = new ABMSwitch();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;
		c.Theme = Theme.Clone();
		c.Type = Type;
		c.mVisibility = mVisibility;	
		c.ToolTipDelay = ToolTipDelay;
		c.ToolTipPosition = ToolTipPosition;
		c.ToolTipText = ToolTipText;
		c.OnText=OnText;
		c.OffText=OffText;
		c.sState=sState;
		c.mEnabled = mEnabled;
		return c;
	}
}
