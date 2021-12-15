package com.ab.abmaterial;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;

@Author("Alain Bailleul")  
@ShortName("ABMActionButton")
@Events(values={"Clicked(Target As String, SubTarget As String)"})
public class ABMActionButton extends ABMComponent {
	private static final long serialVersionUID = 6207983898606585971L;
	protected boolean ForPage=false;
	protected int Bottom=35;
	protected int Right=24;	
		
	protected String PositionMainButton="bottomright";
	protected int PositionLeftRightPx=0;
	protected int PositionTopBottomPx=0;
	
	protected transient ABMButton mMainButton=new ABMButton();;
	protected transient Map<String, ABMButton> mButtons = new LinkedHashMap<String, ABMButton>();
	protected String IconName="";
	protected String IconAwesomeExtra="";
	protected String ThemeName="";
	protected String Direction="up";
	protected boolean IsInitialize2=false;
	
	protected boolean IsDirty=false;	
	
	@Override
	protected void SetPage(ABMPage page) {
		super.SetPage(page);
		if (mMainButton!=null) {
			mMainButton.SetPage(page);
		}
		for (Entry<String,ABMButton> button: mButtons.entrySet()) {
			button.getValue().SetPage(page);
		}
	}
		
	public void Initialize(ABMPage page, String id, String iconName, String iconAwesomeExtra, String themeName) {		
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_ACTIONBUTTON;		
		this.ThemeName = themeName;
		mMainButton = new ABMButton();
		mMainButton.InitializeFloating(page, id+"mainbutton", iconName, themeName);
		mMainButton.IconAwesomeExtra = iconAwesomeExtra;
		mMainButton.IDActionButton=id;
		mMainButton.Size = ABMaterial.BUTTONSIZE_LARGE;
		IsInitialized=true;
		IsDirty=false;
		IsVisibilityDirty=false;
	}	
	
	/**
	 * openDirection: see ACTIONBUTTON_DIRECTION_ constants
	 * positionMainButton: see ACTIONBUTTON_POSITION_ constants
	 * depending from your selection of positionMainButton, it is the top OR bottom px AND the left OR right px
	 */
	public void Initialize2(ABMPage page, String id, String iconName, String iconAwesomeExtra, String openDirection, String positionMainButton, int positionTopBottomPx, int positionLeftRightPx, String themeName) {		
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_ACTIONBUTTON;		
		this.ThemeName = themeName;
		this.Direction = openDirection;		
		this.PositionTopBottomPx=positionTopBottomPx;
		this.PositionLeftRightPx=positionLeftRightPx;
		this.PositionMainButton=positionMainButton;
		IsInitialize2=true;
		mMainButton = new ABMButton();
		mMainButton.InitializeFloating(page, id+"mainbutton", iconName, themeName);
		mMainButton.IconAwesomeExtra = iconAwesomeExtra;
		mMainButton.IDActionButton=id;
		mMainButton.Size = ABMaterial.BUTTONSIZE_LARGE;
		IsInitialized=true;
		IsDirty=false;
		IsVisibilityDirty=false;
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
			}	
			return mVisibility;
		}
		
		return mVisibility;		
	}
	
	@Override
	protected void ResetTheme() {
		mMainButton.ResetTheme();
		for (Map.Entry<String, ABMButton> entry : mButtons.entrySet()) {
			entry.getValue().ResetTheme();						
		}
	}
	
	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase();
	}
	
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
		mMainButton.AddArrayName(arrayName);
		Map<String, ABMButton> newButtons = new LinkedHashMap<String, ABMButton>();
		for (Map.Entry<String, ABMButton> entry : mButtons.entrySet()) {
			entry.getValue().AddArrayName(arrayName);
			newButtons.put(arrayName.toLowerCase()+entry.getKey(), (ABMButton) entry);			
		}
		mButtons.clear();
		mButtons.putAll(newButtons);
	}
	
	public void AddMenuButton(ABMButton button) {
		button.IDActionButton=ID;
		button.IsActionSubButton=true;
		mMainButton.HasSubButtons = true;
		mButtons.put(button.ID.toLowerCase(), button);
	}
	
	public ABMButton MenuButtons(String buttonId) {
		return mButtons.getOrDefault(buttonId.toLowerCase(), null);
	}
	
	public ABMButton MainButton() {
		return mMainButton;
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
		super.FirstRun();		
	}	
	
	
	protected void CheckIfInBrowser(StringBuilder ToCheckSB) {		
		String compIDExtra="";
		if (!HadInBrowserCheck) {
			compIDExtra = ParentString + RootID();		
			ToCheckSB.append("if (document.getElementById('" + compIDExtra + "')==null) {notExists+=';" + compIDExtra + ";'} else {Exists+=';" + compIDExtra + ";'};");
		}		
	}
	
	protected boolean ResetNotInBrowser(String ToCheck, String OKCheck) {
		boolean ret = true;
		String compIDExtra = ParentString + RootID();
		if (ToCheck.contains(";" + compIDExtra + ";")) {												
			IsBuild = false;
			HadFirstRun = false;							
			ret = false;
		} 		
		if (OKCheck.contains(";" + compIDExtra + ";")) {												
			IsBuild = true;
			HadFirstRun = true;	
		} 		
		
		return ret;
	}
	
	@Override
	public void Refresh() {
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		super.RefreshInternal(DoFlush);
		getVisibility();
				
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		if (IsInitialize2) {
			// dummy
		} else {
			if (ForPage) {
				j.SetProp("style" , "bottom: " + Bottom + "px; right: "+ Right +"px;");
			} else {
				j.SetProp("style" , "position: absolute; bottom: " + Bottom + "px; right: "+ Right +"px;");
			}
		}
		mMainButton.mVisibility = mVisibility;
		mMainButton.RefreshInternal(DoFlush);
		for (Map.Entry<String, ABMButton> entry : mButtons.entrySet()) {
			entry.getValue().RefreshInternal(DoFlush);
		}
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("ActionButton Refresh: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		super.Refresh();
	}
	
	@Override
	protected String Build() {
		StringBuilder s = new StringBuilder();
		if (IsInitialize2) {
			String cl = "";
			String st = "";
			String ulStyle="";
			String liStyle="";
			switch (PositionMainButton) {
			case ABMaterial.ACTIONBUTTON_POSITION_TOPLEFT:
				if (ForPage) {
					st = "top: " + PositionTopBottomPx + "px; left: "+ PositionLeftRightPx +"px;display: inline-table;";
				} else {
					st = "top: " + PositionTopBottomPx + "px; left: "+ PositionLeftRightPx +"px;display: inline-table;position: absolute;";
				}
				break;
			case ABMaterial.ACTIONBUTTON_POSITION_TOPRIGHT:
				if (ForPage) {
					st = "top: " + PositionTopBottomPx + "px; right: "+ PositionLeftRightPx +"px;display: inline-table;";
				} else {
					st = "top: " + PositionTopBottomPx + "px; right: "+ PositionLeftRightPx +"px;display: inline-table;position: absolute;";
				}
				break;
			case ABMaterial.ACTIONBUTTON_POSITION_BOTTOMLEFT:
				if (ForPage) {
					st = "bottom: " + PositionTopBottomPx + "px; left: "+ PositionLeftRightPx +"px;display: inline-table;";
				} else {
					st = "bottom: " + PositionTopBottomPx + "px; left: "+ PositionLeftRightPx +"px;display: inline-table;position: absolute;";
				}
				break;
			default: //case ABMaterial.ACTIONBUTTON_POSITION_BOTTOMRIGHT:
				if (ForPage) {
					st = "bottom: " + PositionTopBottomPx + "px; right: "+ PositionLeftRightPx +"px;display: inline-table;";
				} else {
					st = "bottom: " + PositionTopBottomPx + "px; right: "+ PositionLeftRightPx +"px;display: inline-table;position: absolute;";
				}
			}
			switch (Direction) {
			case "down":
				if (ForPage) {
					cl = "fixed-action-btn fixed-action-btn-down";
				} else {
					cl = "fixed-action-btn fixed-action-btn-down";
				}
				if (mMainButton.Size.equals(ABMaterial.BUTTONSIZE_LARGE)) {
					ulStyle = "top: 72px;bottom: auto;";
					liStyle = "margin-top: 15px;margin-bottom: auto;";
				} else {
					ulStyle = "top: 56px;bottom: auto;";
					liStyle = "margin-top: 8px;margin-bottom: auto;";
				}
				break;
			case "left":
				if (ForPage) {
					cl = "fixed-action-btn horizontal fixed-action-btn-up";
				} else {
					cl = "fixed-action-btn horizontal fixed-action-btn-up";
				}
				if (mMainButton.Size.equals(ABMaterial.BUTTONSIZE_LARGE)) {
					ulStyle = "text-align: right;right: 56px;top: 50%;-webkit-transform: translateY(-50%);transform: translateY(-50%);height: 100%;left: auto;width: 500px;";
					liStyle = "margin: 8px 15px 8px 0;";
				} else {
					ulStyle = "text-align: right;right: 40px;top: 50%;-webkit-transform: translateY(-50%);transform: translateY(-50%);height: 100%;left: auto;width: 500px;";
					liStyle = "margin: 0 15px 8px 0;";
				}				
				break;
			case "right":
				if (ForPage) {
					cl = "fixed-action-btn horizontal fixed-action-btn-right";
				} else {
					cl = "fixed-action-btn horizontal fixed-action-btn-right";
				}
				if (mMainButton.Size.equals(ABMaterial.BUTTONSIZE_LARGE)) {
					ulStyle = "text-align: left;right: auto;top: 50%;-webkit-transform: translateY(-50%);transform: translateY(-50%);height: 100%;left: 56px;width: 500px;";
					liStyle = "margin: 8px 0px 8px 15px;";
				} else {
					ulStyle = "text-align: left;right: auto;top: 50%;-webkit-transform: translateY(-50%);transform: translateY(-50%);height: 100%;left: 40px;width: 500px;";
					liStyle = "margin: 0 0 8px 15px;";
				}
				break;
			default: // up
				if (ForPage) {
					cl = "fixed-action-btn fixed-action-btn-up";
				} else {
					cl = "fixed-action-btn fixed-action-btn-up";
				}
				if (mMainButton.Size.equals(ABMaterial.BUTTONSIZE_LARGE)) {
					ulStyle = "top: auto;bottom: 58px;";
					liStyle = "margin-bottom: 15px;margin-top: auto;";
				} else {
					ulStyle = "top: auto;bottom: 44px;";
					liStyle = "margin-bottom: 8px;margin-top: auto;";
				}
			}
			if (ForPage) {
				s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"" + super.BuildExtraClasses() + cl + " " + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + "\" style=\"" + st + "\">\n");
			} else {
				s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"" + super.BuildExtraClasses() + cl + " " + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + "\" style=\"" + st + "\">\n");
			}
			mMainButton.mVisibility = mVisibility;
			s.append(mMainButton.Build());
			s.append("<ul style=\"" + ulStyle + "\">\n");	
			for (Map.Entry<String, ABMButton> entry : mButtons.entrySet()) {
				s.append("<li style=\"" + liStyle + "\">" + entry.getValue().Build() + "</li>\n");
			}
			s.append("</ul>\n");
			s.append("</div>\n");
			
			
		} else {
			if (ForPage) {
				s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"fixed-action-btn " + super.BuildExtraClasses() + " " + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + "\" style=\"bottom: " + Bottom + "px; right: "+ Right +"px;\">\n");
			} else {
				s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"fixed-action-btn " + super.BuildExtraClasses() + " " + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + "\" style=\"position: absolute; bottom: " + Bottom + "px;right: "+ Right +"px;\">\n");
			}
			mMainButton.mVisibility = mVisibility;
			s.append(mMainButton.Build());
			s.append("<ul>\n");	
			for (Map.Entry<String, ABMButton> entry : mButtons.entrySet()) {
				s.append("<li>" + entry.getValue().Build() + "</li>\n");
			}
			s.append("</ul>\n");
			s.append("</div>\n");
		}
		IsBuild=true;
		IsDirty=false;
		return s.toString();		
	}
	
	/** 
	 * Needs at least the params ABM.B4JS_PARAM_ACTIONMAINBUTTON and ABM.B4JS_PARAM_ACTIONSUBBUTTON
	 * 
	 * every method you want to call with a B4JSOn... call MUST return a boolean
	 * returning true will consume the event in the browser and not call the B4J event (if any)
	 *
	 * e.g. myButton.B4JSOnClicked("MyJavascript", "AddToLabel", Array As Object(ABM.B4JS_PARAM_ACTIONMAINBUTTON, ABM.B4JS_PARAM_ACTIONSUBBUTTON, myCounter))
	 * if AddToLabel return true, then myButton_Clicked() will not be raised.
	 * if AddToLabel returns false, then myButton_Clicked() will be raised AFTER the B4JS method is done.
	 * 
	 * public Sub AddToLabel(Target as String, SubTarget as String, MyCounter As Int) As Boolean
	 *      if myCounter mod 2 = 0 then	       
	 *		   Return True
	 *      else
	 *         Return False
	 *      end if
	 * End Sub
	 */
	public void B4JSOnClick(String B4JSClassName, String B4JSMethod, anywheresoftware.b4a.objects.collections.List Params) {
		mMainButton.PrepareEvent("B4JSOnClick", B4JSClassName, B4JSMethod, Params);
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
	public void B4JSOnMouseEnter(String B4JSClassName, String B4JSMethod, anywheresoftware.b4a.objects.collections.List Params) {
		mMainButton.PrepareEvent("B4JSOnMouseEnter", B4JSClassName, B4JSMethod, Params);
		mMainButton.HasHover=true;
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
	public void B4JSOnMouseLeave(String B4JSClassName, String B4JSMethod, anywheresoftware.b4a.objects.collections.List Params) {
		mMainButton.PrepareEvent("B4JSOnMouseLeave", B4JSClassName, B4JSMethod, Params);
		mMainButton.HasHover=true;
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
	
	
	
	@Override
	protected ABMComponent Clone() {
		ABMActionButton c = new ABMActionButton();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;
		c.Type = Type;
		c.mVisibility = mVisibility;	
		c.Bottom = Bottom;
		c.Right = Right;
		c.ForPage = ForPage;
		c.mMainButton = (ABMButton) mMainButton.Clone();
		for (Map.Entry<String, ABMButton> entry : mButtons.entrySet()) {
			c.mButtons.put(entry.getKey(), (ABMButton) entry.getValue().Clone());
		}
		c.PositionMainButton=PositionMainButton;
		c.PositionLeftRightPx=PositionLeftRightPx;
		c.PositionTopBottomPx=PositionTopBottomPx;
		c.Direction=Direction;
		c.IsInitialize2=IsInitialize2;
		return c;		
	}
	
	
}
