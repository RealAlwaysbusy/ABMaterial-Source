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
@ShortName("ABMLabel")
@Events(values={"Clicked(Target As String)","ResetClicked(Target As String)"})
public class ABMLabel extends ABMComponent {
	private static final long serialVersionUID = 1147268758445726065L;
	protected ThemeLabel Theme=new ThemeLabel();
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	protected String mText="";
	protected String mRawHTML="";
	protected String Size="p";
	public String IconName="";
	public String IconAwesomeExtra="";
	public String IconSize="small";
	public String IconAlign="";
	protected boolean WithPadding=false;
	public boolean VerticalAlign=false;
	public boolean Truncate=false;		
	public boolean IsBlockQuote=false;
	public boolean IsFlowText=false;	
	protected String FontStr="";
	public boolean Clickable=false;
	
	public boolean IsTextSelectable=true;
	
	public String PaddingLeft=""; //"0.75rem";
	public String PaddingRight=""; //"0.75rem";
	public String PaddingTop=""; //"0rem";
	public String PaddingBottom=""; //"0rem";
	public String MarginTop=""; //"0px";
	public String MarginBottom=""; //"0px";
	public String MarginLeft=""; //"0px";
	public String MarginRight=""; //"0px";
	
	public boolean SupportEmoji=false;
	protected transient SimpleFuture FutureText;
	
	protected boolean IsDirty=false;
	protected boolean GotLastText=true;
	protected boolean HasHover=false;
	
	protected String PlaceHolder="";
	protected String PlaceHolderCSS=""; 
	
	protected String mExtraStyle="";
	
	private String B4JSExtra="";
		
	/**
	 * With ExtraStyle you can adjust the styling of the placeholder with CSS.
	 * This can be useful e.g. because the margin of the normal label (or its parents) can have an effect.
	 * 
	 *  e.g. "margin-top: calc(-1em - 15px);margin-left: -5px" ' beware, css needs the spaces in a calc!
	 */
	public void SetPlaceHolder(String placeHolder, String ExtraStyle) {
		PlaceHolder = placeHolder;
		PlaceHolderCSS = ExtraStyle;
	}
	
	public void setText(String text) {
		mText=text;
		mRawHTML="";
		IsDirty=true;
	}
	
	public String getText() {
		if (!mRawHTML.equals("")) {
			BA.Log("WARNING: Text was last set as RAW HTML, so the result of .Text can be unpredictable");
		}
		if (GotLastText) {
			return mText;
		}
		GotLastText=true;
		if (mB4JSUniqueKey.equals("")) {
			return mText;
		}
		if (!mB4JSUniqueKey.equals("") && !IsDirty) {
			if (!(FutureText==null)) {
				try {
					this.mText = ReverseBuildBody((String) FutureText.getValue());					
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
		}
		return mText;
	}
	
	public void setTextAsRAWHTML(String HTML) {
		mRawHTML=HTML;
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
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public void setB4JSText(String text) {
		
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public String getB4JSText() {
		return "B4JS Dummy";
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
	
	/**
	 * every method you want to call with a B4JSOn... call MUST return a boolean
	 * returning true will consume the event in the browser and not call the B4J event (if any)
	 *
	 * e.g. myButton.B4JSOnResetClicked("MyJavascript", "AddToLabel", Array As Object(myCounter))
	 * if AddToLabel return true, then myButton_ResetClicked() will not be raised.
	 * if AddToLabel returns false, then myButton_ResetClicked() will be raised AFTER the B4JS method is done.
	 * 
	 * public Sub AddToLabel(MyCounter As Int) As Boolean
	 *      if myCounter mod 2 = 0 then	       
	 *		   Return True
	 *      else
	 *         Return False
	 *      end if
	 * End Sub
	 */
	public void B4JSOnResetClick(String B4JSClassName, String B4JSMethod, anywheresoftware.b4a.objects.collections.List Params) {
		PrepareEvent("B4JSOnResetClick", B4JSClassName, B4JSMethod, Params);				
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
		PrepareEvent("B4JSOnMouseEnter", B4JSClassName, B4JSMethod, Params);	
		HasHover=true;
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
		PrepareEvent("B4JSOnMouseLeave", B4JSClassName, B4JSMethod, Params);	
		HasHover=true;
	}
	
	public void Initialize(ABMPage page, String id, String text, String size, boolean withPadding, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_LABEL;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Labels.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Labels.get(themeName.toLowerCase()).Clone();				
			}
		}			
		this.mText = text;
		this.Size = size;
		this.WithPadding = withPadding;
		IsInitialized=true;
		IsDirty=false;
		IsVisibilityDirty=false;
	}	
	
	@Override
	protected void ResetTheme() {
		UseTheme(Theme.ThemeName);
	}
	
	@Override
	protected String RootID() {
		if (WithPadding) {
			return ArrayName.toLowerCase() + ID.toLowerCase()+ "-section";			
		}		
		if (VerticalAlign) {
			return ArrayName.toLowerCase() + ID.toLowerCase() + "-wrapper";
		}
		if (IsBlockQuote) {
			return ArrayName.toLowerCase() + ID.toLowerCase() + "-bq";			
		}
		if (!IconName.equals("") && !mText.equals("")) {
			return ArrayName.toLowerCase() + ID.toLowerCase() + "-icon";
		}
		return ArrayName.toLowerCase() + ID.toLowerCase();
	}
	
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}
	
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Labels.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Labels.get(themeName.toLowerCase()).Clone();				
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
		if (WithPadding) {
			ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase()+ "-section");
			return;			
		}		
		if (VerticalAlign) {
			ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-wrapper");		
			return;
		}
		if (IsBlockQuote) {
			ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-bq");
			return;			
		}
		if (!IconName.equals("") && !mText.equals("")) {
			ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon");
			return;			
		}
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
	}
	
	@Override
	protected void FirstRun() {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());		
		Page.ws.RunFunction("inittooltipped", Params);
		StringBuilder s = new StringBuilder();
		if (HasHover) {
			s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').on({");
				s.append("mouseenter: function () {");
			        //stuff to do on mouse enter
					s.append("var codeToExecute = 'return _b4jsclasses[\"" + mB4JSUniqueKey + "\"].' + _b4jsvars[" + mB4JSUniqueKey + " + '_B4JSOnMouseEnter'];");        	
					s.append("var tmpFunc = new Function(codeToExecute);");
					s.append("var fret=false;");
					s.append("try { ");
					s.append("fret = tmpFunc()");	
					s.append("} catch(err) {}");
					s.append("if (fret==true) {return;}");
				s.append("},");
				s.append("mouseleave: function () {");
			        //stuff to do on mouse leave
					s.append("var codeToExecute = 'return _b4jsclasses[\"" + mB4JSUniqueKey + "\"].' + _b4jsvars[" + mB4JSUniqueKey + " + '_B4JSOnMouseLeave'];");        	
					s.append("var tmpFunc = new Function(codeToExecute);");
					s.append("var fret=false;");
					s.append("try { ");
					s.append("fret = tmpFunc()");	
					s.append("} catch(err) {}");
					s.append("if (fret==true) {return;}");
				s.append("}");
			s.append("});");
			Page.ws.Eval(s.toString(), null);
		}		
		super.FirstRun();
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		super.RefreshInternal(DoFlush);
		getText();
		getVisibility();
		
		JQueryElement j;
		String cursor="";
		if (Clickable && mCursor.equals("")) {
			cursor = " cursor-pointing ";
		}
		if (!mCursor.equals("")) {
			cursor = " " + mCursor + " ";
		}
		String onClick="";
		if (Clickable) {
			onClick="labelclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "')";
		}
		if (WithPadding) {
			j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-section");
			j.SetProp("class", "section" + cursor + ABMaterial.GetColorStr(Theme.BackColor, Theme.BackColorIntensity, ""));
			ABMaterial.RemoveProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-section", "onclick");
			if (!onClick.equals("")) {
				ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-section", "onclick", onClick);
			}
			onClick="";
		}
		if (VerticalAlign) {
			j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-wrapper");
			String vWrap="valign-wrapper";
			
			switch (this.CellAlign) {
			case ABMaterial.CELL_ALIGN_CENTER:
				vWrap="vhcalign-wrapper";
				break;
			case ABMaterial.CELL_ALIGN_RIGHT:
				vWrap="vhralign-wrapper";
				break;	
			case ABMaterial.CELL_ALIGN_JUSTIFY:
				vWrap="vhjalign-wrapper";
				break;
			}
			j.SetProp("class", vWrap + cursor);
			ABMaterial.RemoveProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-wrapper", "onclick");
			if (!onClick.equals("")) {
				ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-wrapper", "onclick", onClick);
			}
			onClick="";
		}
		ThemeLabel l=Theme;
		if (IsBlockQuote) {			
			j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-bq");
			String rtl="";
			if (getRightToLeft()) {
				rtl = " abmrtl ";
			}
			j.SetProp("class", "blockquote" + l.ThemeName.toLowerCase() + cursor + rtl);
			ABMaterial.RemoveProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-bq", "onclick");
			if (!onClick.equals("")) {
				ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-bq", "onclick", onClick);
			}
			onClick="";
		}
		
		j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		ABMaterial.RemoveProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "onclick");
		if (!onClick.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "onclick", onClick);
		}
		String selectable="";
		if (!IsTextSelectable) {
			selectable = " notselectable ";
		}
		j.SetProp("class", BuildClass() + selectable);
		
		j.SetProp("style", BuildStyle());
		
		if (!ToolTipText.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-position", ToolTipPosition);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-delay", ""+ToolTipDelay);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-tooltip", ToolTipText);
		}		
		if (IconName.equals("")) {
			j.SetHtml(BuildBody(mText, false));
		} else {
			j.SetHtml(BuildBody(mText, false));			
			if (!IconName.equals("")) {
				StringBuilder s = new StringBuilder();
				if (!IconName.equals("") && !mText.equals("")) {
					switch (IconName.substring(0, 3).toLowerCase()) {
					case "mdi":
						s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon\" class=\"" + cursor + IconName + " " + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text") + " " + IconSize + " " + IconAlign + "\"></i>");
						break;
					case "fa ":
					case "fa-":
						s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon\" class=\"" + cursor + IconAwesomeExtra + " " + IconName + " " + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text") + " " + IconSize + " " + IconAlign + "\"></i>");
						break;
					case "abm":
						s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon\" class=\"" + cursor + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text") + " " + IconSize + " " + IconAlign + "\">" + Page.svgIconmap.getOrDefault(IconName.toLowerCase(), "") + "</i>");
						break;
					default:
						s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon\" class=\"" + cursor + " material-icons " + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text") + " " + IconSize + " " + IconAlign + "\">" + ABMaterial.HTMLConv().htmlEscape(IconName, Page.PageCharset) + "</i>");
					}
				}
				ABMaterial.ReplaceMyHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon", s.toString());
			}
		}
		
		if (!PlaceHolder.equals("")) {
			j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-placeholder");
			if (mText.replace("{NBSP}", "").equals("")) {
				j.SetProp("style", PlaceHolderCSS + ";z-index: 0;color: " + ABMaterial.GetColorStrMap(l.PlaceHolderColor, l.PlaceHolderColorIntensity) + " !important");				
			} else {
				j.SetProp("style", PlaceHolderCSS + ";z-index: 0;color: transparent !important");
			}
			j.SetHtml(BuildBody(PlaceHolder, true));
		}		
		super.Refresh();
		IsDirty=false;
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Label Refresh: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
	}
	
	@Override
	protected String Build() {
		if (Theme.ThemeName.equals("default")) {
			Theme.Colorize(Page.CompleteTheme.MainColor);
		}
		String onClick="";
		if (Clickable) {
			onClick=" onclick=\"labelclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "')\" ";			
		}
		StringBuilder s = new StringBuilder();
		ThemeLabel l=Theme;
		String cursor = "";
		if (Clickable && mCursor.equals("")) {
			cursor = " cursor-pointing ";
		}
		if (!mCursor.equals("")) {
			cursor = " " + mCursor + " ";
		}
		if (WithPadding) {
			s.append("<div class=\"section" + cursor + ABMaterial.GetColorStr(l.BackColor, l.BackColorIntensity, "") + "\" " + onClick + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-section\">\n");	
			onClick="";
		}
		String toolTip="";
		if (!ToolTipText.equals("")) {
			toolTip=" data-position=\"" + ToolTipPosition + "\" data-delay=\"" + ToolTipDelay + "\" data-tooltip=\"" + ToolTipText + "\" "; 
		}		
		if (VerticalAlign) {
			String vWrap="valign-wrapper";
			
			switch (this.CellAlign) {
			case ABMaterial.CELL_ALIGN_CENTER:
				vWrap="vhcalign-wrapper";
				break;
			case ABMaterial.CELL_ALIGN_RIGHT:
				vWrap="vhralign-wrapper";
				break;	
			case ABMaterial.CELL_ALIGN_JUSTIFY:
				vWrap="vhjalign-wrapper";
				break;
			}
			
			s.append("<div class=\"" + vWrap + cursor + "\" " + onClick + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-wrapper\">\n");
			onClick="";
		}
		String rtl="";
		if (getRightToLeft()) {
			rtl = " abmrtl ";
		}
		if (IsBlockQuote) {
			s.append("<blockquote class=\"blockquote" + l.ThemeName.toLowerCase() + cursor + rtl + "\" " + onClick + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-bq\">");
			onClick="";
			
		}
		if (!IconName.equals("") && !mText.equals("")) {
			switch (IconName.substring(0, 3).toLowerCase()) {
			case "mdi":
				s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon\" class=\"" + IconName + " " + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text") + " " + IconSize + " " + IconAlign + "\"></i>");
				break;
			case "fa ":
			case "fa-":
				s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon\" class=\"" + IconAwesomeExtra + " " + IconName + " " + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text") + " " + IconSize + " " + IconAlign + "\"></i>");
				break;
			case "abm":
				s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon\" class=\"" + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text") + " " + IconSize + " " + IconAlign + "\">" + Page.svgIconmap.getOrDefault(IconName.toLowerCase(), "") + "</i>");
				break;
			default:
				s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon\" class=\"material-icons " + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text") + " " + IconSize + " " + IconAlign + "\">" + ABMaterial.HTMLConv().htmlEscape(IconName, Page.PageCharset) + "</i>");
			}			
		}
		String B4JSData="";
		if (!mB4JSUniqueKey.equals("")) {
			B4JSData = " data-b4js=\"" + mB4JSUniqueKey + "\" data-b4jsextra=\"" + B4JSExtra + "\" ";
		} else {
			B4JSData = " data-b4js=\"\" data-b4jsextra=\"" + B4JSExtra + "\" ";
		}
		
		s.append("<" + Size + toolTip + onClick + B4JSData + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"");
		String selectable="";
		if (!IsTextSelectable) {
			selectable = " notselectable ";
		}
		s.append(BuildClass() + selectable);
		s.append("\" style=\"" + BuildStyle() + "\">");
		if (!IconName.equals("")) {
			if (mText.equals("")) {	
				switch (IconName.substring(0, 3).toLowerCase()) {
				case "mdi":
					s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon\" class=\"" + IconName + " " + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text") + " " + IconSize + " " + IconAlign +  "\"></i>");
					break;
				case "fa ":
				case "fa-":
					s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon\" class=\"" + IconAwesomeExtra + " " + IconName + " " + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text") + " " + IconSize + " " + IconAlign +  "\"></i>");
					break;
				case "abm":
					s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon\" class=\"" + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text") + " " + IconSize + " " + IconAlign +  "\">" + Page.svgIconmap.getOrDefault(IconName.toLowerCase(), "") + "</i>");
					break;
				default:
					s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon\" class=\"material-icons " + ABMaterial.GetColorStr(l.IconColor, l.IconColorIntensity, "text") + " " + IconSize + " " + IconAlign +  "\">" + ABMaterial.HTMLConv().htmlEscape(IconName, Page.PageCharset) + "</i>");
				}				
				s.append("&nbsp;");
			} else {
				s.append(BuildBody(mText, false) + "\n");
			}
		} else {
			s.append(BuildBody(mText, false) + "\n");
			
		}		
		s.append("</" + Size + ">\n");
		if (!PlaceHolder.equals("")) {
			if (mText.replace("{NBSP}", "").equals("")) {
				s.append("<" + Size + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-placeholder\" style=\"" + PlaceHolderCSS + ";z-index: 0;color: " + ABMaterial.GetColorStrMap(l.PlaceHolderColor, l.PlaceHolderColorIntensity) +" !important;\">" + BuildBody(PlaceHolder, true));
				s.append("</" + Size + ">\n");
			} else {
				s.append("<" + Size + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-placeholder\" style=\"" + PlaceHolderCSS + ";z-index: 0;color: transparent !important;\">" + BuildBody(PlaceHolder, true));
				s.append("</" + Size + ">\n");
			}
			
		}
		if (IsBlockQuote) {
			s.append("</blockquote>");
		}
		if (VerticalAlign) {
			s.append("</div>\n");
		}
		if (WithPadding) {
			s.append("</div>\n");
		}
		IsBuild=true;
		IsDirty=false;
		return s.toString();
	}
	
	@Hide
	protected String BuildClass() {			
		StringBuilder s = new StringBuilder();
		s.append(super.BuildExtraClasses());
		ThemeLabel l=Theme;
		if (!ToolTipText.equals("")) {
			s.append("tooltipped ");
		}
		if (IsFlowText) {
			s.append("flow-text ");
		}
		if (this.getRightToLeft()) {
			s.append("abmrtl ");
		}
		if (!l.Align.equals("")) {
			s.append(l.Align + "-align ");
		} else {
			if (this.getRightToLeft()) {
				s.append("right-align ");
			}
		}
		if (VerticalAlign) {
			s.append("valign ");
		}
		s.append(mVisibility + " ");
		if (Truncate) {
			s.append("truncate ");
		}
		s.append("lblstrikethrough" + l.ThemeName.toLowerCase() + " ");
		if (l.UseStrikethrough) {
			s.append("strikethrough ");
		}		
		if (Clickable && mCursor.equals("")) {
			s.append("cursor-pointing ");
		}
		
		if (!mCursor.equals("")) {
			s.append(mCursor + " ");
		}
		s.append(l.ZDepth + " ");
		s.append(ABMaterial.GetColorStr(l.BackColor, l.BackColorIntensity, "") + " " + ABMaterial.GetColorStr(l.ForeColor, l.ForeColorIntensity, "text"));
		s.append(mIsPrintableClass);
		s.append(mIsOnlyForPrintClass);
		return s.toString(); 
	}
	
	/**
	 * Depreciated! Use the margin and padding properties instead! 
	 */
	public void Margins(String top, String right, String bottom, String left) {
		MarginTop = top;
		MarginRight=right;
		MarginLeft=left;
		MarginBottom=bottom;
	}
	
	public void FontSize(String size) {
		FontStr = "font-size: " + size + ";";
	}
	
	public void SetExtraStyle(String extraStyle) {
		if (extraStyle.length()>0) {
			if (!extraStyle.endsWith(";")) {
				extraStyle = extraStyle + ";";
			}
		}
		mExtraStyle = extraStyle;
	}
		
	@Hide
	protected String BuildStyle() {			
		StringBuilder s = new StringBuilder();
		ThemeLabel l=Theme;
		if (!l.FontWeight.equals("")) {
			s.append(" font-weight: " + l.FontWeight + ";");
		}
		
		if (MarginTop!="") s.append(" margin-top: " + MarginTop + ";");
		if (MarginBottom!="") s.append(" margin-bottom: " + MarginBottom + ";");
		if (MarginLeft!="") s.append(" margin-left: " + MarginLeft + ";");
		if (MarginRight!="") s.append(" margin-right: " + MarginRight + ";");
		if (PaddingTop!="") s.append(" padding-top: " + PaddingTop + ";");
		if (PaddingBottom!="") s.append(" padding-bottom: " + PaddingBottom + ";");
		if (PaddingLeft!="") s.append(" padding-left: " + PaddingLeft + ";");
		if (PaddingRight!="") s.append(" padding-right: " + PaddingRight + ";");
		
		if (!FontStr.equals("")) {
			s.append(FontStr);
		}
		
		s.append(mExtraStyle);
		
		return s.toString();
	}
	
	@Hide
	protected String BuildBody(String text, boolean isPlaceHolder) {
		if (!mRawHTML.equals("")) {
			return mRawHTML;
		}
		StringBuilder s = new StringBuilder();	
		
		String v = PrepareEmoji(text);
		
		v = ABMaterial.HTMLConv().htmlEscape(v, Page.PageCharset);
		v = ConvertEmoji(v);
		
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
		v=v.replace("{AL2}", "<a href=\"");
		v=v.replace("{AT}", "\">");
		v=v.replace("{/AL}", "</a>");
		v=v.replace("{AS}", " title=\"");
		v=v.replace("{/AS}", "\"");
		v=v.replace("{TCX}", " <tcxspan tcxhref=\"");
		v=v.replace("{TCTB}", "\" title=\"");
		v=v.replace("{TCTE}", "\">");
		v=v.replace("{/TCX}", "</tcxspan>");
		
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
		
		int start = v.indexOf("{SI:");
		while (start > -1) {
			int stop = v.indexOf("{/SI}");
			String vv = "";
			if (stop>0) {
				vv = v.substring(start, stop+5);
			} else {
				break;
			}
			String IconStyle = "";
			int closing = vv.indexOf("}");
			IconStyle = vv.substring(4,closing);
			String IconName = vv.substring(closing+1,vv.length()-5);
			String repl="";
			switch (IconName.substring(0, 3).toLowerCase()) {
			case "mdi":
				repl = "<i style=\"" + IconStyle + "\" class=\"" + IconName + "\"></i>";
				break;
			case "fa ":
			case "fa-":
				repl = "<i style=\"" + IconStyle + "\" class=\"" + IconName + "\"></i>";
				break;
			case "abm":
				repl = "<i>" + Page.svgIconmap.getOrDefault(IconName.toLowerCase(), "") + "</i>"; 
				break;
			default:
				repl = "<i style=\"" + IconStyle + "\" class=\"material-icons\">" + IconName + "</i>";
			}
			v=v.replace(vv,repl );
			start = v.indexOf("{SI:");
		}
		
		start = v.indexOf("{IC:");
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
		
		if (Theme.HasReset && !isPlaceHolder) {
			if (mText.replace("{NBSP}", "").equals("") || !Clickable) {
				s.append("<span id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-reset\" class=\"abmclose-icon abmclose-icon" + Theme.ThemeName.toLowerCase() + " hide\" onclick=\"labelresetclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "')\"></span>");
			} else {
				s.append("<span id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-reset\" class=\"abmclose-icon abmclose-icon" + Theme.ThemeName.toLowerCase() + "\" onclick=\"labelresetclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "')\"></span>");
			}			
		}
		
		return s.toString();
	}
	
	
	
	protected String ReverseBuildBody(String text) {
		StringBuilder s = new StringBuilder();	
		String v = text;
		v = ABMaterial.HTMLConv().htmlUnescape(v);
		
		v=v.replaceAll("<br>","\r\n");
		v=v.replace("<b>","{B}");
		v=v.replace("</b>", "{/B}");
		v=v.replace("<i>","{I}");
		v=v.replace("</i>","{/I}");
		v=v.replace("<ins>","{U}");
		v=v.replace("</ins>","{/U}");
		v=v.replace("<sub>","{SUB}");
		v=v.replace("</sub>","{/SUB}");
		v=v.replace("<sup>","{SUP}");
		v=v.replace("</sup>","{/SUP}");
		v=v.replace("<br>","{BR}");
		v=v.replace("<wbr>","{WBR}");
		v=v.replace("&nbsp;","{NBSP}");
						
		v=v.replace(" <tcxspan tcxhref=\"","{TCX}");
		v=v.replace("\" title=\"","{TCTB}");
		v=v.replace("\">","{TCTE}");
		v=v.replace("</tcxspan>","{/TCX}");
						
		while (v.indexOf("<span style=\"color:") > -1) {
			v=v.replaceFirst("<span style=\"color:","\\{C:");			
			v=v.replaceFirst("\">","\\}");		
			v=v.replaceFirst("</span>","\\{/C}");	
		}		
		
		
		v = v.replace("<div class=\"abmcode\"><pre><code>","{CODE}");
		v = v.replace("</code></pre></div>","{/CODE}");
		while (v.indexOf("<span style=\"") > -1) {
			v=v.replaceFirst("<span style=\"","\\{ST:");			
			v=v.replaceFirst("\">","\\}");		
			v=v.replaceFirst("</span>","\\{/ST}");	
		}			
		
		s.append(v);
		
		return s.toString();
	}
	
	protected String PrepareEmoji(String text) {
		if (!SupportEmoji) return text;
		String v = text;
		
		// 5
		v = v.replace("*\\0/*", "y:yok_womany:y");
		v = v.replace("*\\O/*", "y:yok_womany:y");
		v = v.replace("*\\o/*", "y:yok_womany:y");
		v = v.replace("-___-", "y:yexpressionlessy:y");
		
		// 4
		v = v.replace(":'-)", "y:yjoyy:y");
		v = v.replace("':-)", "y:ysweat_smiley:y");
		v = v.replace("':-D", "y:ysweat_smiley:y");
		v = v.replace("':-d", "y:ysweat_smiley:y");
		v = v.replace(">:-)", "y:ylaughingy:y");
		v = v.replace("':-(", "y:ysweaty:y");
		v = v.replace(">:-(", "y:yangryy:y");
		v = v.replace(":'-(", "y:ycryy:y");
		v = v.replace("O:-)", "y:yinnocenty:y");
		v = v.replace("o:-)", "y:yinnocenty:y");
		v = v.replace("0:-3", "y:yinnocenty:y");
		v = v.replace("0:-)", "y:yinnocenty:y");
		v = v.replace("0;^)", "y:yinnocenty:y");
		v = v.replace("O:-)", "y:yinnocenty:y");
		v = v.replace("o:-)", "y:yinnocenty:y");
		v = v.replace("O;-)", "y:yinnocenty:y");
		v = v.replace("o;-)", "y:yinnocenty:y");
		v = v.replace("0;-)", "y:yinnocenty:y");
		v = v.replace("O:-3", "y:yinnocenty:y");
		v = v.replace("o:-3", "y:yinnocenty:y");
		v = v.replace("-__-", "y:yexpressionlessy:y");
				
		//3
		v = v.replace("</3", "y:ybroken_hearty:y");
		v = v.replace(":')", "y:yjoyy:y");
		v = v.replace(":-D", "y:ysmileyy:y");
		v = v.replace(":-d", "y:ysmileyy:y");
		v = v.replace(":-)", "y:yslight_smiley:y");
		v = v.replace("':)", "y:ysweat_smiley:y");		
		v = v.replace("'=)", "y:ysweat_smiley:y");
		v = v.replace("':D", "y:ysweat_smiley:y");
		v = v.replace("'=D", "y:ysweat_smiley:y");
		v = v.replace("':d", "y:ysweat_smiley:y");
		v = v.replace("'=d", "y:ysweat_smiley:y");
		v = v.replace(">:)", "y:ylaughingy:y");
		v = v.replace(">;)", "y:ylaughingy:y");
		v = v.replace(">=)", "y:ylaughingy:y");
		v = v.replace(";-)", "y:ywinky:y");
		v = v.replace("*-)", "y:ywinky:y");
		v = v.replace(";-]", "y:ywinky:y");		
		v = v.replace(";^)", "y:ywinky:y");
		v = v.replace("':(", "y:ysweaty:y");
		v = v.replace("'=(", "y:ysweaty:y");
		v = v.replace(":-*", "y:ykissing_hearty:y");		
		v = v.replace(":^*", "y:ykissing_hearty:y");
		v = v.replace(">:P", "y:ystuck_out_tongue_winking_eyey:y");
		v = v.replace(">:p", "y:ystuck_out_tongue_winking_eyey:y");
		v = v.replace("X-P", "y:ystuck_out_tongue_winking_eyey:y");
		v = v.replace("x-p", "y:ystuck_out_tongue_winking_eyey:y");
		v = v.replace(">:[", "y:ydisappointedy:y");
		v = v.replace(":-(", "y:ydisappointedy:y");		
		v = v.replace(":-[", "y:ydisappointedy:y");
		v = v.replace(">:(", "y:yangryy:y");
		v = v.replace(":'(", "y:ycryy:y");
		v = v.replace(";-(", "y:ycryy:y");
		v = v.replace(">.<", "y:yperseverey:y");
		v = v.replace("#-)", "y:ydizzy_facey:y");
		v = v.replace("%-)", "y:ydizzy_facey:y");
		v = v.replace("X-)", "y:ydizzy_facey:y");
		v = v.replace("x-)", "y:ydizzy_facey:y");
		v = v.replace("\\0/", "y:yok_womany:y");
		v = v.replace("\\O/", "y:yok_womany:y");
		v = v.replace("\\o/", "y:yok_womany:y");
		v = v.replace("0:3", "y:yinnocenty:y");
		v = v.replace("0:)", "y:yinnocenty:y");
		v = v.replace("O:)", "y:yinnocenty:y");
		v = v.replace("o:)", "y:yinnocenty:y");
		v = v.replace("O=)", "y:yinnocenty:y");
		v = v.replace("o=)", "y:yinnocenty:y");
		v = v.replace("O:3", "y:yinnocenty:y");
		v = v.replace("o:3", "y:yinnocenty:y");
		v = v.replace("B-)", "y:ysunglassesy:y");
		v = v.replace("b-)", "y:ysunglassesy:y");
		v = v.replace("8-)", "y:ysunglassesy:y");
		v = v.replace("B-D", "y:ysunglassesy:y");
		v = v.replace("8-D", "y:ysunglassesy:y");
		v = v.replace("8-d", "y:ysunglassesy:y");
		v = v.replace("-_-", "y:yexpressionlessy:y");
		v = v.replace(">:\\", "y:yconfusedy:y");
		v = v.replace(">:/", "y:yconfusedy:y");
		v = v.replace(":-/", "y:yconfusedy:y");
		v = v.replace(":-.", "y:yconfusedy:y");
		v = v.replace(":-P", "y:ystuck_out_tonguey:y");
		v = v.replace(":-p", "y:ystuck_out_tonguey:y");
		v = v.replace(":-Þ", "y:ystuck_out_tonguey:y");
		v = v.replace(":-b", "y:ystuck_out_tonguey:y");
		v = v.replace(":-O", "y:yopen_mouthy:y");
		v = v.replace(":-o", "y:yopen_mouthy:y");
		v = v.replace("O_O", "y:yopen_mouthy:y");
		v = v.replace(">:O", "y:yopen_mouthy:y");
		v = v.replace(":-X", "y:yno_mouthy:y");
		v = v.replace(":-#", "y:yno_mouthy:y");
		v = v.replace(":-x", "y:yno_mouthy:y");
				
		//2		
		v = v.replace("<3", "y:yhearty:y");
		v = v.replace(":D", "y:ysmileyy:y");		
		v = v.replace("=D", "y:ysmileyy:y");		
		v = v.replace(":d", "y:ysmileyy:y");
		v = v.replace("=d", "y:ysmileyy:y");
		
		v = v.replace(":)", "y:yslight_smiley:y");
		v = v.replace("=]", "y:yslight_smiley:y");
		v = v.replace("=)", "y:yslight_smiley:y");
		v = v.replace(":]", "y:yslight_smiley:y");
		
		v = v.replace(";)", "y:ywinky:y");
		v = v.replace("*)", "y:ywinky:y");
		v = v.replace(";]", "y:ywinky:y");
		v = v.replace(";D", "y:ywinky:y");
		v = v.replace(";d", "y:ywinky:y");
		
		v = v.replace(":*", "y:ykissing_hearty:y");
		v = v.replace("=*", "y:ykissing_hearty:y");
		
		v = v.replace(":(", "y:ydisappointedy:y");
		v = v.replace(":[", "y:ydisappointedy:y");
		v = v.replace("=(", "y:ydisappointedy:y");
		
		v = v.replace(":@", "y:yangryy:y");
		
		v = v.replace(";(", "y:ycryy:y");
		
		v = v.replace("D:", "y:yfearfuly:y");
				
		v = v.replace(":$", "y:yflushedy:y");
		v = v.replace("=$", "y:yflushedy:y");		
		
		v = v.replace("#)", "y:ydizzy_facey:y");
		v = v.replace("%)", "y:ydizzy_facey:y");
		v = v.replace("X)", "y:ydizzy_facey:y");
		v = v.replace("x)", "y:ydizzy_facey:y");
				
		v = v.replace("B)", "y:ysunglassesy:y");
		v = v.replace("b)", "y:ysunglassesy:y");
		v = v.replace("8)", "y:ysunglassesy:y");
				
		v = v.replace(":/", "y:yconfusedy:y");
		v = v.replace(":\\", "y:yconfusedy:y");
		v = v.replace("=/", "y:yconfusedy:y");
		v = v.replace("=\\", "y:yconfusedy:y");
		v = v.replace(":L", "y:yconfusedy:y");
		v = v.replace("=L", "y:yconfusedy:y");
		
		v = v.replace(":P", "y:ystuck_out_tonguey:y");
		v = v.replace("=P", "y:ystuck_out_tonguey:y");
		v = v.replace(":p", "y:ystuck_out_tonguey:y");
		v = v.replace("=p", "y:ystuck_out_tonguey:y");
		v = v.replace(":Þ", "y:ystuck_out_tonguey:y");
		v = v.replace(":þ", "y:ystuck_out_tonguey:y");
		v = v.replace("-þ", "y:ystuck_out_tonguey:y");
		v = v.replace(":b", "y:ystuck_out_tonguey:y");
		v = v.replace(":B", "y:ystuck_out_tonguey:y");
		v = v.replace("d:", "y:ystuck_out_tonguey:y");
		
		v = v.replace(":O", "y:yopen_mouthy:y");
		v = v.replace(":o", "y:yopen_mouthy:y");
		
		
		v = v.replace(":X", "y:yno_mouthy:y");
		v = v.replace(":#", "y:yno_mouthy:y");
		v = v.replace("=X", "y:yno_mouthy:y");
		v = v.replace("=x", "y:yno_mouthy:y");
		v = v.replace(":x", "y:yno_mouthy:y");
		v = v.replace("=#", "y:yno_mouthy:y");
		return v;
	}
	
	protected String ConvertEmoji(String text) {
		if (!SupportEmoji) return text;
		String v = text;
		v = v.replace("y:yhearty:y", ABMaterial.heart);		
		v = v.replace("y:ybroken_hearty:y", ABMaterial.brokenheart);		
		v = v.replace("y:yjoyy:y", ABMaterial.joy);				
		v = v.replace("y:ysmileyy:y", ABMaterial.smiley);		
		v = v.replace("y:yslight_smiley:y", ABMaterial.slightsmile);
		v = v.replace("y:ysweat_smiley:y", ABMaterial.sweatsmile);
		v = v.replace("y:ylaughingy:y", ABMaterial.laughing);
		v = v.replace("y:ywinky:y", ABMaterial.wink);
		v = v.replace("y:ysweaty:y", ABMaterial.sweat);
		v = v.replace("y:ykissing_hearty:y", ABMaterial.kissingheart);
		v = v.replace("y:ystuck_out_tongue_winking_eyey:y", ABMaterial.stuckouttonguewink);
		v = v.replace("y:ydisappointedy:y", ABMaterial.dissappointed);
		v = v.replace("y:yangryy:y", ABMaterial.angry);
		v = v.replace("y:ycryy:y", ABMaterial.cry);
		v = v.replace("y:yperseverey:y", ABMaterial.persevere);
		v = v.replace("y:yfearfuly:y", ABMaterial.fearful);
		v = v.replace("y:yflushedy:y", ABMaterial.flushed);
		v = v.replace("y:ydizzy_facey:y", ABMaterial.dizzyface);
		v = v.replace("y:yok_womany:y", ABMaterial.okwoman);
		v = v.replace("y:yinnocenty:y", ABMaterial.innocent);
		v = v.replace("y:ysunglassesy:y", ABMaterial.sunglasses);
		v = v.replace("y:yexpressionlessy:y", ABMaterial.expressionless);
		v = v.replace("y:yconfusedy:y", ABMaterial.confused);
		v = v.replace("y:ystuck_out_tonguey:y", ABMaterial.stuckouttongue);
		v = v.replace("y:yopen_mouthy:y", ABMaterial.openmouth);
		v = v.replace("y:yno_mouthy:y", ABMaterial.nomouth);
		
		return v;
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMLabel c = new ABMLabel();
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
		c.IconName = IconName;
		c.IconAwesomeExtra=IconAwesomeExtra;
		c.IconSize = IconSize;
		c.IconAlign = IconAlign;
		c.VerticalAlign = VerticalAlign;
		c.Size = Size;
		c.mText = mText;
		c.WithPadding = WithPadding;
		return c;
	}
}
