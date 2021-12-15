package com.ab.abmaterial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
@ShortName("ABMButton")
@Events(values={"Clicked(Target As String)", "MenuClicked(Target As String, returnName As String)"})
public class ABMButton extends ABMComponent {
	private static final long serialVersionUID = 2197450018731393809L;
	protected ThemeButton Theme=new ThemeButton();
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	
	protected static final int RAISED=0;
	protected static final int FLOATING=1;
	protected static final int FLAT=2;
	protected int ButtonType=RAISED;		
	public String IconName="";
	public String IconAwesomeExtra="";
	protected String mText="";
	public String IconAlign=ABMaterial.ICONALIGN_LEFT;
	public String Size=ABMaterial.BUTTONSIZE_NORMAL;
	protected String IDActionButton="";
	protected boolean IsActionSubButton=false;
	protected boolean IsFileButton=false;
	protected String IDFileButton="";
	protected String MultipleFile="";
	protected boolean HasSubButtons = true;
	protected String IDPagination="";
	protected String TypePagination="";
	protected int NumberPagination=0;
	protected int TotalNumberPagesPagination=0;
	protected String ActivePagination="";
	public boolean TabStop=true;
	public boolean UseFullCellWidth =false;
	protected String IconBadge="";
	public String PaddingLeftFlatRaised="";
	public String PaddingRightFlatRaised="";
	
	protected List<ButtonMenuItem> menuItems = new ArrayList<ButtonMenuItem>();
	protected String HalfPosition="";
	
	public boolean DropdownHideArrow=false;
	public boolean DropdownShowBelow=false;
	public boolean DropdownConstrainWidth=false;
	
	public boolean ModalFooterForceLeft=false;
	
	protected String BorderStyle="";
	protected transient SimpleFuture FutureText;
	
	protected boolean IsDirty=false;
	protected boolean GotLastText=false;
	protected boolean HasHover=false;
	
	protected boolean mEnabled=true;
	
	public boolean StopPropagation=false;
	
	@Hide
	@Override
	public void ZABInit() {
		super.ZABInit();
		ZABDEF.InitProperty("Theme Name", "", "", true, "string", false,1);
		ZABDEF.InitProperty("ToolTip Position", "TOOLTIP_TOP", "TOOLTIP_TOP:TOOLTIP_BOTTOM:TOOLTIP_LEFT:TOOLTIP_RIGHT", true, "string", true,1);
		ZABDEF.InitProperty("ToolTip Text", ToolTipText, "", true, "string", false,1);
		ZABDEF.InitProperty("ToolTip Delay", ToolTipDelay, "", true, "int", true,1);
		
		ZABDEF.InitProperty("Button Type", "RAISED", "RAISED:FLOATING:FLAT", true, "string", true,1);
		ZABDEF.InitProperty("Icon Name", IconName, "", true, "string", false,1);
		ZABDEF.InitProperty("Icon Awesome Extra", IconAwesomeExtra, "", true, "string", false, 1);
		ZABDEF.InitProperty("Icon Align", "ICONALIGN_LEFT", "ICONALIGN_LEFT:ICONALIGN_RIGHT", true, "string", true,1);
		ZABDEF.InitProperty("Size", "BUTTONSIZE_NORMAL", "BUTTONSIZE_SMALL:BUTTONSIZE_NORMAL:BUTTONSIZE_LARGE", true, "string", true,1);
		ZABDEF.InitProperty("Text", mText, "", true, "string", false,1);
		ZABDEF.InitProperty("Use full cell width", UseFullCellWidth, "", true, "boolean", true,1);
		ZABDEF.InitProperty("Padding Left Flat Raised", PaddingLeftFlatRaised, "", true, "string", false,1);
		ZABDEF.InitProperty("Padding Right Flat Raised", PaddingRightFlatRaised, "", true, "string", false,1);
		ZABDEF.InitProperty("Dropdown Hide Arrow", DropdownHideArrow, "", true, "boolean", true,1);
		ZABDEF.InitProperty("Dropdown Show Below", DropdownShowBelow, "", true, "boolean", true,1);
		ZABDEF.InitProperty("Dropdown Constrain Width", DropdownConstrainWidth, "", true, "boolean", true,1);
		ZABDEF.InitProperty("Border Style", BorderStyle, "", true, "string", false,1);
		
		
		ZABDEF.InitProperty("Enabled", mEnabled, "", true, "boolean", true,3);
		ZABDEF.InitProperty("Has Tabstop", TabStop, "", true, "boolean", true,3);		
		
		ZABDEF.InitProperty("IDActionButton", IDActionButton, "", false, "string", false,0);
		ZABDEF.InitProperty("IsActionSubButton", IsActionSubButton, "", false, "boolean", false,0);
		ZABDEF.InitProperty("IsFileButton", IsFileButton, "", false, "boolean", false,0);
		ZABDEF.InitProperty("IDFileButton", IDFileButton, "", false, "string", false,0);
		ZABDEF.InitProperty("MultipleFile", MultipleFile, "", false, "string", false,0);
		ZABDEF.InitProperty("HasSubButtons", HasSubButtons, "", false, "boolean", false,0);
		ZABDEF.InitProperty("IDPagination", IDPagination, "", false, "string", false,0);
		ZABDEF.InitProperty("TypePagination", TypePagination, "", false, "string", false,0);
		ZABDEF.InitProperty("NumberPagination", NumberPagination, "", false, "int", false,0);
		ZABDEF.InitProperty("TotalNumberPagesPagination", TotalNumberPagesPagination, "", false, "int", false,0);
		ZABDEF.InitProperty("ActivePagination", ActivePagination, "", false, "string", false,0);
	}
	
	@Override
	public void ZABBuild(ABMPage page) throws NoSuchFieldException, SecurityException {
		super.ZABBuild(page);
		SetPage(page);
		String ts = (String) ZABDEF.Property("theme name").Value;
		mText = (String) ZABDEF.Property("text").Value;
		String bs = (String) ZABDEF.Property("button type").Value;
		String is = (String) ZABDEF.Property("icon name").Value;
		IconAwesomeExtra = (String) ZABDEF.Property("icon awesome extra").Value;
		String ia = ABMaterial.GetConst((String) ZABDEF.Property("icon align").Value);
		switch (bs) {
		case "RAISED":
			InitializeRaised(page,ID, is, ia, mText, ts);
			break;
		case "FLOATING":
			InitializeFloating(page,ID, is, ts);
			break;
		case "FLAT":
			InitializeFlat(page,ID, is, ia, mText, ts);
			break;
		}
		String s = (String) ZABDEF.Property("array name").Value;
		if (!s.equals("")) {
			AddArrayName(s);
		}
		ToolTipPosition=ABMaterial.GetConst((String) ZABDEF.Property("tooltip position").Value);
		ToolTipText = (String) ZABDEF.Property("tooltip text").Value;
		ToolTipDelay = (int) ZABDEF.Property("tooltip delay").Value;
		mEnabled = (boolean)  ZABDEF.Property("enabled").Value;
		UseFullCellWidth = (boolean)  ZABDEF.Property("use full cell width").Value;
		Size=ABMaterial.GetConst((String) ZABDEF.Property("size").Value);
		TabStop = (boolean)  ZABDEF.Property("has tabstop").Value;
		IDActionButton = (String) ZABDEF.Property("idactionbutton").Value;
		IsActionSubButton = (boolean)  ZABDEF.Property("isactionsubbutton").Value;
		IsFileButton = (boolean)  ZABDEF.Property("isfilebutton").Value;
		IDFileButton = (String) ZABDEF.Property("idfilebutton").Value;
		MultipleFile = (String) ZABDEF.Property("multiplefile").Value;
		HasSubButtons = (boolean)  ZABDEF.Property("hassubbuttons").Value;
		IDPagination = (String) ZABDEF.Property("idpagination").Value;
		TypePagination = (String) ZABDEF.Property("typepagination").Value;
		NumberPagination = (int) ZABDEF.Property("numberpagination").Value;
		TotalNumberPagesPagination = (int) ZABDEF.Property("totalnumberpagespagination").Value;
		ActivePagination = (String)  ZABDEF.Property("activepagination").Value;
		
		PaddingLeftFlatRaised = (String) ZABDEF.Property("padding left flat raised").Value;
		PaddingRightFlatRaised = (String) ZABDEF.Property("padding right flat raised").Value;
		
		DropdownHideArrow = (boolean)  ZABDEF.Property("dropdown hide arrow").Value;
		DropdownShowBelow = (boolean)  ZABDEF.Property("dropdown show below").Value;
		DropdownConstrainWidth = (boolean)  ZABDEF.Property("dropdown constrain width").Value;
		BorderStyle = (String) ZABDEF.Property("border style").Value;
	}
	
	@Override
	public ZAbstractDesignerDefinition getZABDEF() {
		if (!ZABDEF.IsInitialized) {
			ZABDEF.properties.Initialize();
			super.ZABInit();
			ZABInit();
			ZABDEF.IsInitialized=true;
		}
		return ZABDEF;
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
				par.Add(ParentString + RootID());
				FutureEnabled = JQ.RunMethodWithResult("GetEnabled", par);				
			} else {
				FutureEnabled=null;				
			}
			if (!(FutureEnabled==null)) {
				try {
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
		
	public void setText(String text) {
		mText=text;
		IsDirty=true;
	}
	
	public String getText() {
		if (GotLastText) {
			return mText;
		}
		GotLastText=true;
		if (!IsDirty) {
			if (mB4JSUniqueKey.equals("")) {
				return mText;
			}
			if (!mB4JSUniqueKey.equals("") && !IsDirty) {
				if (!(FutureText==null)) {
					try {
						this.mText = ReverseBuildBody((String)FutureText.getValue());					
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
			}		
		}
		return mText;
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
					
	public void InitializeRaised(ABMPage page, String id, String iconName, String iconAlign, String Text, String themeName) {
		this.ID = id;
		this.Page = page;
		this.Type = ABMaterial.UITYPE_BUTTON;
		this.ButtonType = RAISED;
		this.IconName = iconName;
		this.IconAlign = iconAlign;
		this.mText = Text;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Buttons.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Buttons.get(themeName.toLowerCase()).Clone();				
			}
		}
		IsInitialized=true;
		IsDirty=true;
		IsVisibilityDirty=false;
		IsEnabledDirty=false;
	}				
	
	public void InitializeFloating(ABMPage page, String id, String iconName, String themeName) {	
		this.ID = id;
		this.Page = page;
		this.Type = ABMaterial.UITYPE_BUTTON;
		this.ButtonType = FLOATING;
		this.IconName = iconName;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Buttons.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Buttons.get(themeName.toLowerCase()).Clone();				
			}
		}
		IsInitialized=true;
		IsDirty=true;
		IsVisibilityDirty=false;
		IsEnabledDirty=false;
	}		
	
	public void InitializeFlat(ABMPage page, String id,  String iconName, String iconAlign, String Text, String themeName) {
		this.ID = id;
		this.Page = page;
		this.Type = ABMaterial.UITYPE_BUTTON;
		this.ButtonType = FLAT;
		this.IconName = iconName;
		this.IconAlign = iconAlign;
		this.mText = Text;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Buttons.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Buttons.get(themeName.toLowerCase()).Clone();				
			}
		}
		IsInitialized=true;
		IsDirty=false;
		IsVisibilityDirty=false;
		IsEnabledDirty=false;
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
	
	public void SetBorder(String color, String intensity, int widthPx, String borderStyle) {
		if (borderStyle==ABMaterial.CONTAINERBORDER_NONE) {
			BorderStyle="";
			return;
		}
		BorderStyle = "border: " + ABMaterial.GetColorStrMap(color, intensity) + ";" + "border-width:" + widthPx + "px;border-style:" + borderStyle;
	}
	
	public void SetBorderEx(String color, String intensity, int widthPx, String borderStyle, String radius) {
		if (borderStyle==ABMaterial.CONTAINERBORDER_NONE) {
			BorderStyle="";
			return;
		}
		BorderStyle = "border: " + ABMaterial.GetColorStrMap(color, intensity) + ";" + "border-width:" + widthPx + "px;border-style:" + borderStyle + ";border-radius:" + radius ;
	}
	
	public void AddMenuItem(String itemReturnName, String itemText) {
		ButtonMenuItem m = new ButtonMenuItem();
		m.returnValue = itemReturnName;
		m.text = itemText;
		menuItems.add(m);
	}
	
	public void AddMenuDivider() {
		ButtonMenuItem m = new ButtonMenuItem();
		m.isDivider = true;
		menuItems.add(m);
	}
	
	public void ClearMenu() {
		menuItems = new ArrayList<ButtonMenuItem>();
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
			if (Page.CompleteTheme.Buttons.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Buttons.get(themeName.toLowerCase()).Clone();				
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
		super.FirstRun();
		
		StringBuilder s = new StringBuilder();
		
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());		
		Page.ws.RunFunction("inittooltipped", Params);
		
		if (menuItems.size()>0) {
			s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').dropdown({");
			s.append("inDuration: 300,");
			s.append("outDuration: 225,");
			s.append("constrainWidth: " + DropdownConstrainWidth + ",");
			s.append("hover: false,");
			s.append("gutter: 0,");
			s.append("belowOrigin: " + DropdownShowBelow + ",");
			s.append("alignment: 'left',");
			s.append("stopPropagation: true");
			s.append("}");
			s.append(");");
			Page.ws.Eval(s.toString(), null);
		}
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
		getEnabled();
				
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		j.SetProp("class", BuildClass());
		
		String B4JSData1="";
		if (!mB4JSUniqueKey.equals("")) {
			B4JSData1 = mB4JSUniqueKey ;
		} else {
			B4JSData1 = "";
		}
		String B4JSData2="";
		if (!mB4JSUniqueKey.equals("")) {
			B4JSData2 = "" + HasHover;
		} else {
			B4JSData2 = "";
		}
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-b4js", B4JSData1);
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-b4jsextra", B4JSData2);
		
		if (!ToolTipText.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-position", ToolTipPosition);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-delay", ""+ToolTipDelay);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-tooltip", ToolTipText);
		}
		if (menuItems.size()>0) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-activates", ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-dropdown");
		} else {
			ABMaterial.RemoveProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-activates");
		}		
		if (DropdownShowBelow && menuItems.size()>0) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-beloworigin", "true");			
		} else {
			ABMaterial.RemoveProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-beloworigin");
		}
		if (!DropdownConstrainWidth && menuItems.size()>0) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-constrainwidth", "false");			
		} else {
			ABMaterial.RemoveProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-constrainwidth");
		}
		String s = BuildBody();
		if (IsFileButton) {
			s+="<input id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "input\" type=\"file\" " + MultipleFile + ">";
		}
		j.SetHtml(s);
		StringBuilder ss = new StringBuilder();
		if (menuItems.size()>0) {			
			for (int i=0;i<menuItems.size();i++) {
				if (menuItems.get(i).isDivider) {
					ss.append("<li class=\"divider\" style=\"background-color: " + ABMaterial.GetColorStrMap(Theme.MenuDividerColor, Theme.MenuDividerColorIntensity) + "\"></li>");
				} else {
					ss.append("<li class=\"\" style=\"padding: 0px;\"><a onclick=\"buttonmenuclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "','" + menuItems.get(i).returnValue  + "')\" style=\"color: " + ABMaterial.GetColorStrMap(Theme.MenuForeColor, Theme.MenuForeColorIntensity) + ";line-height: initial;height: initial;\">" + BuildText(menuItems.get(i).text) + "</a></li>");
				}
			}			
		}
		j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-dropdown");
		j.SetHtml(ss.toString());
		StringBuilder sss = new StringBuilder();
		if (menuItems.size()>0) {
			sss.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').dropdown({");
			sss.append("inDuration: 300,");
			sss.append("outDuration: 225,");
			sss.append("constrainWidth: " + DropdownConstrainWidth + ",");
			sss.append("hover: false,");
			sss.append("gutter: 0,");
			sss.append("belowOrigin: " + DropdownShowBelow + ",");
			sss.append("alignment: 'left',");
			sss.append("stopPropagation: true");
			sss.append("}");
			sss.append(");");
			Page.ws.Eval(sss.toString(), null);
		}
		IsDirty=false;
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Button Refresh: " + ID);}
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
		if (Theme.ThemeName.equals("default")) {
			Theme.Colorize(Page.CompleteTheme.MainColor);
		}
		StringBuilder s = new StringBuilder();
		String toolTip="";
		if (!ToolTipText.equals("")) {
			toolTip=" data-position=\"" + ToolTipPosition + "\" data-delay=\"" + ToolTipDelay + "\" data-tooltip=\"" + ToolTipText + "\" "; 
		}
		String TabStopStr = "";
		if (!TabStop) {
			TabStopStr = " tabindex=\"-1\"";
		}
		String dataActivates="";
		if (menuItems.size()>0) {
			dataActivates = " data-activates=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-dropdown\" ";
		}
		String DataBelow="";
		if (DropdownShowBelow && menuItems.size()>0) {
			DataBelow=" data-beloworigin=\"true\" ";
		}
		String DataConstrain="";
		if (!DropdownConstrainWidth && menuItems.size()>0) {
			DataConstrain=" data-constrainwidth=\"false\" ";
		}
		
		String style="";
		switch (ButtonType) {			
		case ABMButton.RAISED:	
		case ABMButton.FLAT:
			if (!PaddingLeftFlatRaised.equals("")) {
				style = "padding-left: " + PaddingLeftFlatRaised + " !important;";
			}
			if (!PaddingRightFlatRaised.equals("")) {
				style += "padding-right: " + PaddingRightFlatRaised + " !important;";
			}
		}	
		if (!BorderStyle.equals("")) {
			style += BorderStyle;
		}
		
		String B4JSData="";
		if (!mB4JSUniqueKey.equals("")) {
			B4JSData = " data-b4js=\"" + mB4JSUniqueKey + "\" data-b4jsextra=\"" + HasHover + "\" ";
		} else {
			B4JSData = " data-b4js=\"\" data-b4jsextra=\"" + HasHover + "\" ";
		}
		
		String tmpCopyToClipboardTarget="";
		
		String SP="";
		if (StopPropagation) {
			SP = "SP";
		}
		if (IDActionButton.equals("") && IDFileButton.equals("")) {
			if (IDPagination.equals("")) {				
				if (this.UseFullCellWidth) {
					s.append("<button " + TabStopStr + toolTip + dataActivates + DataBelow + DataConstrain + B4JSData + tmpCopyToClipboardTarget + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" onclick=\"buttonclickarray" + SP + "(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "')\" style=\"" + style + "width:100%\" class=\"");
				} else {
					s.append("<button " + TabStopStr + toolTip + dataActivates+ DataBelow + DataConstrain + B4JSData + tmpCopyToClipboardTarget + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" onclick=\"buttonclickarray" + SP + "(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "')\" style=\"" + style + "\" class=\"");
				}
				
			} else {
				if (this.UseFullCellWidth) {
					s.append("<button " + TabStopStr + toolTip + dataActivates + B4JSData + tmpCopyToClipboardTarget + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" onclick=\"paginationclickarray(event,'" + ParentString + "','" + IDPagination.toLowerCase() + "','" + TypePagination + "', " + NumberPagination + ", " + TotalNumberPagesPagination + ")\" style=\"" + style + "width: 100%;font-size: 1.1rem\" class=\"");
				} else {
					s.append("<button " + TabStopStr + toolTip + dataActivates + B4JSData + tmpCopyToClipboardTarget + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" onclick=\"paginationclickarray(event,'" + ParentString + "','" + IDPagination.toLowerCase() + "','" + TypePagination + "', " + NumberPagination + ", " + TotalNumberPagesPagination + ")\" style=\"" + style + "font-size: 1.1rem\" class=\"");
				}
			}
		} else {
			String sty="";
			if (this.UseFullCellWidth) {
				sty = "style=\"" + style + "width: 100%\"";
			} else {
				sty = "style=\"" + style + "\"";
			}
			if (!IsFileButton) {
				if (!IsActionSubButton) {
					s.append("<button " + TabStopStr + toolTip + dataActivates + DataBelow + DataConstrain + B4JSData + tmpCopyToClipboardTarget + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" onclick=\"actionbuttonclickarray(event,'" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + IDActionButton.toLowerCase() + "', '')\" " + sty + " class=\"");
				} else {
					s.append("<button " + TabStopStr + toolTip + dataActivates + B4JSData + tmpCopyToClipboardTarget + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" onclick=\"actionbuttonclickarray(event,'" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + IDActionButton.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase()  + "')\" " + sty + " class=\"");
				}
			} else {
				s.append("<div " + TabStopStr + toolTip + dataActivates + B4JSData + tmpCopyToClipboardTarget + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" onclick=\"buttonclickarray" + SP + "(event,'" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + IDFileButton.toLowerCase() + "')\" " + sty + " class=\"");
			}
		}
		s.append(BuildClass());	
		s.append(" " + ActivePagination);		
		
		s.append("\">");		
		s.append(BuildBody());	
		if (IsFileButton) {
			s.append("<input id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "input\" type=\"file\" " + MultipleFile + ">");
		}
		if (!IsFileButton) {
			s.append("</button>\n");
		} else {
			s.append("</div>\n");
		}
		if (menuItems.size()>0) {
			s.append("<ul id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-dropdown\" class=\"dropdown-content dropdown-content-b-" + Theme.ThemeName.toLowerCase() + "\" style=\"background-color: " + ABMaterial.GetColorStrMap(Theme.MenuBackColor, Theme.MenuBackColorIntensity) + ";white-space: nowrap\">");
			for (int i=0;i<menuItems.size();i++) {
				if (menuItems.get(i).isDivider) {
					s.append("<li class=\"divider\" style=\"background-color: " + ABMaterial.GetColorStrMap(Theme.MenuDividerColor, Theme.MenuDividerColorIntensity) + "\"></li>");
				} else {
					s.append("<li class=\"\" style=\"padding: 0px;\"><a onclick=\"buttonmenuclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "','" + menuItems.get(i).returnValue  + "')\" style=\"color: " + ABMaterial.GetColorStrMap(Theme.MenuForeColor, Theme.MenuForeColorIntensity) + ";line-height: initial;height: initial;\">" + BuildText(menuItems.get(i).text) + "</a></li>");
				}
			}
			s.append("</ul>");
		}
		IsBuild=true;
		IsDirty=false;
		return s.toString();		
	}
	
	@Hide
	protected String BuildClass() {
		ThemeButton b = Theme; 
		StringBuilder s = new StringBuilder();
		s.append(super.BuildExtraClasses());
		String toolTip="";
		if (!ToolTipText.equals("")) {
			toolTip = "tooltipped ";
		}
		String disabled="";
		if (!mEnabled) {
			disabled = " disabled ";
		}
		String dropdown="";
		if (menuItems.size()>0) {
			dropdown = " dropdown-button ";
		}
		String modalLeft="";
		if (ModalFooterForceLeft) {
			modalLeft = " modalleft ";
		}
		String rtl="";
		if (getRightToLeft()) {
			rtl = "abmrtl right ";
		}
		switch (ButtonType) {			
		case ABMButton.RAISED:				
			s.append("btn " + modalLeft + mIsPrintableClass + mIsOnlyForPrintClass + rtl + dropdown + disabled + toolTip + ABMaterial.GetWavesEffect(b.WavesEffect, b.WavesCircle) + " " + ABMaterial.GetColorStr(b.BackColor,b.BackColorIntensity, "") + " " + ABMaterial.GetColorStr(b.ForeColor, b.ForeColorIntensity, "text") + " " + mVisibility + " " + b.ZDepth);
			switch (Size) {
			case ABMaterial.BUTTONSIZE_LARGE:
				s.append(" btn-large");
				break;
			case ABMaterial.BUTTONSIZE_SMALL:
				s.append(" btn-small");
				break;
			}
			return s.toString();
		case ABMButton.FLOATING:				
			s.append("btn-floating " + mIsPrintableClass + mIsOnlyForPrintClass + dropdown + disabled + toolTip + ABMaterial.GetWavesEffect(b.WavesEffect, b.WavesCircle) + " " + ABMaterial.GetColorStr(b.BackColor,b.BackColorIntensity, "") + " " + ABMaterial.GetColorStr(b.ForeColor, b.ForeColorIntensity, "text") + " " + mVisibility + " " + b.ZDepth);
			switch (Size) {
			case ABMaterial.BUTTONSIZE_LARGE:
				s.append(" btn-large");
				break;
			case ABMaterial.BUTTONSIZE_SMALL:
				s.append(" btn-small");
				break;
			}
			if (!HalfPosition.equals("")) {
				s.append(" halfway-fab " + HalfPosition + " ");
			}
			return s.toString();
		case ABMButton.FLAT:				
			s.append("btn-flat " + modalLeft + mIsPrintableClass + dropdown + rtl + mIsOnlyForPrintClass + disabled + toolTip + ABMaterial.GetWavesEffect(b.WavesEffect, b.WavesCircle) + " " + ABMaterial.GetColorStr(b.BackColor,b.BackColorIntensity, "") + " " + ABMaterial.GetColorStr(b.ForeColor, b.ForeColorIntensity, "text") + " " + mVisibility + " " + b.ZDepth);
			switch (Size) {
			case ABMaterial.BUTTONSIZE_LARGE:
				s.append(" btn-large");
				break;
			case ABMaterial.BUTTONSIZE_SMALL:
				s.append(" btn-small");
				break;
			}
			return s.toString();
		}
		return ""; 
	}
	
	@Hide
	protected String BuildBody() {
		StringBuilder s = new StringBuilder();
		switch (ButtonType) {
		case ABMButton.RAISED:
			if (!IconName.equals("")) {
				if (IconAlign.equals("")) {
					IconAlign=ABMaterial.ICONALIGN_LEFT;
				}
				if (IconAlign.equals(ABMaterial.ICONALIGN_CENTER) && !mText.equals("")) {
					IconAlign=ABMaterial.ICONALIGN_LEFT;
				}
				switch (IconName.substring(0, 3).toLowerCase()) {
				case "mdi":
					s.append("<i class=\"" + IconName + " " + IconAlign + " " + ABMaterial.GetColorStr(Theme.ForeColor, Theme.ForeColorIntensity, "text") +  "\">" + IconBadge + "</i>");
					break;
				case "fa ":
				case "fa-":
					s.append("<i class=\"" + IconAwesomeExtra + " " + IconName + " " + IconAlign + " " + ABMaterial.GetColorStr(Theme.ForeColor, Theme.ForeColorIntensity, "text") +  "\">" + IconBadge + "</i>");
					break;
				case "abm":
					s.append("<i class=\"" + IconAlign + " " + ABMaterial.GetColorStr(Theme.ForeColor, Theme.ForeColorIntensity, "text") +  "\">" + Page.svgIconmap.getOrDefault(IconName.toLowerCase(), "") + IconBadge + "</i>");
					break;
				default:
					s.append("<i class=\"material-icons " + IconAlign + " " + ABMaterial.GetColorStr(Theme.ForeColor, Theme.ForeColorIntensity, "text") +  "\">" + ABMaterial.HTMLConv().htmlEscape(IconName, Page.PageCharset) + "" + IconBadge + "</i>");
				}
			}
			if (getRightToLeft()) {				 
				if (!DropdownHideArrow && menuItems.size()>0) {
					s.append("<span class=\"caret\">&#9660;&nbsp;</span>");
				}
				s.append(BuildText(mText));
			} else {
				s.append(BuildText(mText)); 
				if (!DropdownHideArrow && menuItems.size()>0) {
					s.append("<span class=\"caret\">&nbsp;&#9660;</span>");
				}
			}
			return s.toString();
		case ABMButton.FLOATING:
			if (!IconName.equals("")) {
				switch (IconName.substring(0, 3).toLowerCase()) {
				case "mdi":
					s.append("<i class=\"" + IconName + " " + ABMaterial.GetColorStr(Theme.ForeColor, Theme.ForeColorIntensity, "text") +  "\">" + IconBadge + "</i>");
					break;
				case "fa ":
				case "fa-":
					s.append("<i class=\"" + IconAwesomeExtra + " " + IconName + " " + ABMaterial.GetColorStr(Theme.ForeColor, Theme.ForeColorIntensity, "text") + "\">" + IconBadge + "</i>");
					break;
				case "abm":
					s.append("<i class=\"" + ABMaterial.GetColorStr(Theme.ForeColor, Theme.ForeColorIntensity, "text") + "\">" + Page.svgIconmap.getOrDefault(IconName, "") + IconBadge + "</i>");
					break;
				default:
					s.append("<i class=\"material-icons " + ABMaterial.GetColorStr(Theme.ForeColor, Theme.ForeColorIntensity, "text") + "\">" + ABMaterial.HTMLConv().htmlEscape(IconName.toLowerCase(), Page.PageCharset) + "" + IconBadge + "</i>");
				}
			} else {
				s.append("<i class=\"material-icons " + ABMaterial.GetColorStr(Theme.ForeColor, Theme.ForeColorIntensity, "text") + "\">" + s.append(BuildText(mText)) + "" + IconBadge + "</i>");
			}
			return s.toString();
		case ABMButton.FLAT:
			if (!IconName.equals("")) {
				if (IconAlign.equals("")) {
					IconAlign=ABMaterial.ICONALIGN_LEFT;
				}
				if (IconAlign.equals(ABMaterial.ICONALIGN_CENTER) && !mText.equals("")) {
					IconAlign=ABMaterial.ICONALIGN_LEFT;
				}
				switch (IconName.substring(0, 3).toLowerCase()) {
				case "mdi":
					s.append("<i class=\"" + IconName + " " + IconAlign + " " + ABMaterial.GetColorStr(Theme.ForeColor, Theme.ForeColorIntensity, "text") + "\">" + IconBadge + "</i>");
					break;
				case "fa ":
				case "fa-":
					s.append("<i class=\"" + IconAwesomeExtra + " " + IconName + " " + IconAlign + " " + ABMaterial.GetColorStr(Theme.ForeColor, Theme.ForeColorIntensity, "text") + "\">" + IconBadge + "</i>");
					break;
				case "abm":
					s.append("<i class=\"" + IconAlign + " " + ABMaterial.GetColorStr(Theme.ForeColor, Theme.ForeColorIntensity, "text") + "\">" + Page.svgIconmap.getOrDefault(IconName.toLowerCase(), "") + IconBadge + "</i>");
					break;
				default:
					s.append("<i class=\"material-icons " + IconAlign + " " + ABMaterial.GetColorStr(Theme.ForeColor, Theme.ForeColorIntensity, "text") +  "\">" + ABMaterial.HTMLConv().htmlEscape(IconName, Page.PageCharset) + "" + IconBadge + "</i>");
				}
			}
			if (getRightToLeft()) {
				if (!DropdownHideArrow && menuItems.size()>0) {
					s.append("<span class=\"caret\">&#9660;&nbsp;</span>");
				}
				s.append(BuildText(mText));
			} else {
				s.append(BuildText(mText));
				if (!DropdownHideArrow && menuItems.size()>0) {
					s.append("<span class=\"caret\">&nbsp;&#9660;</span>");
				}
			}
			return s.toString(); 
		}
		return "";
	}
	
	protected String BuildText(String text) {
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
	
	@Override
	protected ABMComponent Clone() {
		ABMButton c = new ABMButton();
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
		c.mEnabled = mEnabled;
		c.IconAlign = IconAlign;
		c.IconName = IconName;
		c.IDActionButton = IDActionButton;
		c.IsActionSubButton = IsFileButton;
		c.IDFileButton = IDActionButton;
		c.IsFileButton = IsFileButton;
		c.Size=Size;
		c.mText = mText;
		return c;
	}
	
	protected class ButtonMenuItem {
		protected String text="";
		protected String returnValue;
		protected boolean isDivider=false;		
	}
}
