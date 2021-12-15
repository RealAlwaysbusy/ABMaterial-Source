package com.ab.abmaterial;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.keywords.Regex;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;
import anywheresoftware.b4j.object.WebSocket.SimpleFuture;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class ABMRow { 
	protected Map<String, ABMCell> Cells = new LinkedHashMap<String,ABMCell>();	
	protected boolean CenterInPage;	
	
	protected ThemeRow Theme=new ThemeRow();
	protected String ID="";
	protected ABMPage Page=null;
	protected boolean IsBuild=false;
	protected String mVisibility=ABMaterial.VISIBILITY_ALL;
	protected String ArrayName="";
	protected ABMDivider Divider=null;	
	protected String ParentString="";
	public String PaddingLeft=""; //"0rem";
	public String PaddingRight=""; //"0rem";
	public String PaddingTop=""; //"0rem";
	public String PaddingBottom=""; //"0rem";
	public String MarginTop=""; //"0px";
	public String MarginBottom=""; //"20px";
	public String MarginLeft=""; //"auto";
	public String MarginRight=""; //"auto";
	
	public ZAbstractDesignerDefinition ZABDEF=new ZAbstractDesignerDefinition();
	protected String ClickableTarget="";
	
	protected String mIsPrintableClass="";
	protected String mPrintPageBreak="";
	protected String mIsOnlyForPrintClass=" only-print ";
	
	protected String RowType="";
		
	protected boolean mZABMode=false;	
	
	protected String mB4JSUniqueKey="";
	protected boolean HasHover=false;
	
	protected int mZoom=Integer.MIN_VALUE;
	
	protected String ExtraStyle="";
	protected String ExtraClasses="";
	
	protected boolean ForReport=false;
	
	
	ABMRow() {

	}
		
	public void ZABInit() {		
		ZABDEF.InitProperty("Center in page", true, "", true, "boolean", true,1);
		
		ZABDEF.InitProperty("Margin Top", "0", "", true, "string", true,1);
		ZABDEF.InitProperty("Margin Bottom", "20", "", true, "string", true,1);
		ZABDEF.InitProperty("Margin Left", "0", "", true, "string", true,1);
		ZABDEF.InitProperty("Margin Right", "0", "", true, "string", true,1);
		ZABDEF.InitProperty("Theme Name", "", "", true, "string", false,1);
		ZABDEF.InitProperty("Visibility", "VISIBILITY_ALL", "VISIBILITY_ALL:VISIBILITY_HIDE_ALL:VISIBILITY_HIDE_ON_SMALL_ONLY:VISIBILITY_HIDE_ON_MEDIUM_ONLY:VISIBILITY_HIDE_ON_MEDIUM_AND_BELOW:VISIBILITY_HIDE_ON_MEDIUM_AND_ABOVE:VISIBILITY_HIDE_ON_LARGE_ONLY:VISIBILITY_SHOW_ON_SMALL_ONLY:VISIBILITY_SHOW_ON_MEDIUM_ONLY:VISIBILITY_SHOW_ON_MEDIUM_AND_ABOVE:VISIBILITY_SHOW_ON_MEDIUM_AND_BELOW:VISIBILITY_SHOW_ON_LARGE_ONLY", true, "string", true, 1);
		
		ZABDEF.InitProperty("ParentString", ParentString, "", false, "string", false,0);
		ZABDEF.InitProperty("isbuild", IsBuild, "", false, "boolean", false,1);
		ZABDEF.InitProperty("Array Name", ArrayName, "", false, "string", false, 0);
		ZABDEF.InitProperty("ID", ID, "", false, "string", false, 0);
	}
	
	public void ZABBuild(ABMPage page) throws NoSuchFieldException, SecurityException {
		this.Page = page;
		String ts = (String) ZABDEF.Property("theme name").Value;
		CenterInPage = (boolean)  ZABDEF.Property("center in page").Value;
		mVisibility = ABMaterial.GetConst((String) ZABDEF.Property("visibility").Value);
				
		MarginTop = (String) ZABDEF.Property("margin top").Value;
		MarginBottom = (String) ZABDEF.Property("margin bottom").Value;
		MarginLeft = (String) ZABDEF.Property("margin left").Value;
		MarginRight = (String) ZABDEF.Property("margin right").Value;
		
		ParentString = (String) ZABDEF.Property("parentstring").Value;
		IsBuild = (Boolean) ZABDEF.Property("isbuild").Value;
		String s = (String) ZABDEF.Property("array name").Value;
		if (!s.equals("")) {
			AddArrayName(s);
		}
		ID = (String) ZABDEF.Property("id").Value;
		UseTheme(ts);
	}
	
	public void ZABFromZABDEF(ZAbstractDesignerDefinition zabdef) {
		ZABDEF = zabdef.Clone();		
	}
	
	/**
	 * Enable/Disable this row from being printed 
	 */
	public void setIsPrintable(Boolean printme) {
		if (printme) {
			mIsPrintableClass="";
			mIsOnlyForPrintClass=" only-print ";
		} else {
			mIsPrintableClass=" no-print ";
			mIsOnlyForPrintClass="";
		}
	}
	
	public void SetIsForReport() {
		ForReport=true;
	}
	
	protected void SetZAB(boolean ZABMode) {
		mZABMode = ZABMode;
	}
	
	public boolean getIsPrintable() {
		return mIsPrintableClass.equals("");
	}
	
	public void SetZoom(int zoom) {
		mZoom = zoom;
	}
	
	/**
	 * This can NOT be a random generated key!
	 * 
	 * It must be UNIQUE, NOT contain spaces or double quotes and ALWAYS the same for the component.
	 * 
	 * This is needed for B4JS to work properly if you later want to make a reference to the component
	 * e.g. to set a property of this component in B4JS later.
	 * 
	 * Dim MyLabel as ABMLabel
	 * MyLabel.B4JSUniqueKey = "uniqueKey"
	 * myLabel.B4JSText = "My new text"
	 * log(myLabel.B4JSText) 
	 */
	public void setB4JSUniqueKey(String uniqueKey) {
		mB4JSUniqueKey = uniqueKey.toLowerCase();
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
	
	protected void PrepareEvent(String EventName, String B4JSClassName, String B4JSMethod, anywheresoftware.b4a.objects.collections.List Params) {
		if (mB4JSUniqueKey.equals("")) {
			BA.LogError("You cannot use B4JS events before setting the B4JSUniqueKey property!");
			return;
		}
					
		Page.B4JSComponents.put(mB4JSUniqueKey + "_" + EventName, 0);
		String pars = "";
		if (Params.IsInitialized()) {
			for (int i=0;i<Params.getSize();i++) {
				if (i>0) {
					pars=pars + ", "; 
				}
				if (Params.Get(i) instanceof String) {
					pars = pars + "\"" + (String) Params.Get(i) + "\""; 
				} else {
					pars = pars + Params.Get(i);
				}
			}
		}
		StringBuilder s = new StringBuilder();
		s.append("if (_b4jsclasses['" + mB4JSUniqueKey + "'] === undefined) {");
		s.append("_b4jsclasses['" + mB4JSUniqueKey + "'] = new b4js_" + B4JSClassName.toLowerCase() + "();");
		s.append("_b4jsclasses['" + mB4JSUniqueKey + "'].initializeb4js();");
		s.append("}\n");			
		
		s.append("_b4jsvars['" + mB4JSUniqueKey + "_" + EventName + "']='" + B4JSMethod.toLowerCase() + "(" + pars + ")';");
		Page.ws.Eval(s.toString() , null);
	}
		
	/**
	 * pageBreak: use the ABMaterial.PRINT_PAGEBREAK_ constants 
	 */
	public void setPrintPageBreak(String pageBreak) {
		mPrintPageBreak=pageBreak;
	}
	
	public String getPrintPageBreak() {
		return mPrintPageBreak;
	}
	
	public String getID() {
		return this.ID;
	}
	
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase();
	}
	
	public String GetDebugID() {
		return "Parent='" + ParentString + "',ArrayName='" + this.ArrayName.toLowerCase() + "',ID='" + this.ID.toLowerCase() + "'";
	}
	
	protected String Serialize() throws UnsupportedEncodingException {
		StringBuilder s = new StringBuilder();
		s.append("ID:" + ID + ";");
		s.append("CenterInPage:" + CenterInPage + ";");
		s.append("Theme:" + SerializeTheme() + ";");
		s.append("Visibility:" + mVisibility + ";");
		s.append("ArrayName:" + ArrayName + ";");
		s.append("Divider:" + SerializeDivider() + ";");
		s.append("ParentString:" + ParentString + ";");
		s.append("PaddingLeft:" + PaddingLeft + ";");
		s.append("PaddingRight:" + PaddingRight + ";");
		s.append("PaddingTop:" + PaddingTop + ";");
		s.append("PaddingBottom:" + PaddingBottom + ";");
		s.append("MarginTop:" + MarginTop + ";");
		s.append("MarginBottom:" + MarginBottom + ";");
		s.append("MarginLeft:" + MarginLeft + ";");
		s.append("MarginRight:" + MarginRight + ";");
		for (Entry<String, ABMCell> entry: Cells.entrySet()) {
			s.append(entry.getKey() + ":" + entry.getValue().Serialize() + ";");
		}
		
		return new String(Base64.getEncoder().encode(s.toString().getBytes("UTF-8")));
	}
	
	protected void Deserialize(String value, ABMPage page) throws UnsupportedEncodingException {
		byte b[] = Base64.getDecoder().decode(value.getBytes("UTF-8"));
		this.Page = page;
		String decod = new String(b, "UTF-8");
		
		String[] values = Regex.Split(";", decod);
		for (String s: values) {
			//BA.Log(s);
			String[] val = Regex.Split(":", s);
			if (val.length==2) {
				switch (val[0]) {
					case "ID":
						ID = val[1];
						break;
					case "CenterInPage":
						CenterInPage = (val[1].equals("true"));
						break;
					case "Theme":
						DeserializeTheme(val[1]);
						break;
					case "Visibility":
						mVisibility = val[1];
						break;
					case "ArrayName": 
						ArrayName = val[1];
						break;
					case "Divider": 
						DeserializeDivider(val[1]);
						break;
					case "ParentString":
						ParentString = val[1];
						break;
					case "PaddingLeft":
						PaddingLeft = val[1];
						break;
					case "PaddingRight":
						PaddingRight = val[1];
						break;
					case "PaddingTop":
						PaddingTop = val[1];
						break;
					case "PaddingBottom":
						PaddingBottom = val[1];
						break;
					case "MarginTop":
						MarginTop = val[1];
						break;
					case "MarginBottom":
						MarginBottom = val[1];
						break;
					case "MarginLeft":
						MarginLeft = val[1];
						break;
					case "MarginRight":
						MarginRight = val[1];
						break;
					default:
						ABMCell cell = new ABMCell();
						cell.Deserialize(val[1]);
						cell.Page = Page;
						Cells.put(val[0], cell);
						break;
						
				}
			}				
		}
	}
	
	private String SerializeTheme() throws UnsupportedEncodingException {
		StringBuilder s = new StringBuilder();
		s.append(Theme.VerticalAlign + ";" + Theme.BackColor + ";" + Theme.BackColorIntensity + ";" + Theme.BorderColor + ";" + Theme.BorderColorIntensity + ";" + Theme.BorderWidth + ";" + Theme.MainColor + ";" + Theme.ThemeName + ";" + Theme.ZDepth + ";THEEND");
		return new String(Base64.getEncoder().encode(s.toString().getBytes("UTF-8")));
	}
	
	private void DeserializeTheme(String value) throws UnsupportedEncodingException {
		byte b[] = Base64.getDecoder().decode(value.getBytes("UTF-8"));
		String decod = new String(b, "UTF-8");
		String[] values = Regex.Split(";", decod);
		Theme = new ThemeRow();
		Theme.VerticalAlign =  (values[0].equals("true"));			
		Theme.BackColor = values[1];
		Theme.BackColorIntensity = values[2];
		Theme.BorderColor = values[3];
		Theme.BorderColorIntensity = values[4];
		Theme.BorderWidth = Integer.parseInt(values[5]);
		Theme.MainColor = values[6];
		Theme.ThemeName = values[7];
		Theme.ZDepth = values[8];		
	}
	
	private String SerializeDivider() throws UnsupportedEncodingException {
		if (Divider!=null) {
			StringBuilder s = new StringBuilder();
			s.append(Divider.ID + ";");
			s.append(Divider.ArrayName + ";");
			s.append(Divider.mVisibility + ";");
			s.append(Divider.Theme.ForeColor + ";");
			s.append(Divider.Theme.ForeColorIntensity + ";");
			s.append(Divider.Theme.MainColor + ";");
			s.append(Divider.Theme.ThemeName + ";");
			return new String(Base64.getEncoder().encode(s.toString().getBytes("UTF-8")));
		} else {
			return "null";
		}
	}
	
	private void DeserializeDivider(String value) throws UnsupportedEncodingException {
		if (!value.equals("null")) {
			byte b[] = Base64.getDecoder().decode(value.getBytes("UTF-8"));
			String decod = new String(b, "UTF-8");
			String[] values = Regex.Split(";", decod);
			Divider = new ABMDivider();
			Divider.Initialize(Page, values[0], values[6]);
			Divider.ArrayName = values[1];
			Divider.mVisibility = values[2];
			Divider.Theme = new ThemeDivider();
			Divider.Theme.ForeColor = values[3];
			Divider.Theme.ForeColorIntensity = values[4];
			Divider.Theme.MainColor = values[5];
			Divider.Theme.ThemeName = values[6];
		}
	}
	
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
		Map<String, ABMCell> newCells = new LinkedHashMap<String,ABMCell>();
		for (Map.Entry<String, ABMCell> entry : Cells.entrySet()) {
			entry.getValue().AddArrayName(arrayName);			
			newCells.put(entry.getKey(), entry.getValue());
		}
		Cells.clear();
		Cells.putAll(newCells);
	}
	
	protected void SetParentStringAndClick(String parentName) {
		this.ParentString = parentName + "-";
		this.ClickableTarget = parentName;
		for (Map.Entry<String, ABMCell> entry : Cells.entrySet()) {
			entry.getValue().SetParentStringAndClick(parentName);		
		}
	}
	
	public void RemoveAllComponents() {
		for (Map.Entry<String, ABMCell> entry : Cells.entrySet()) {
			entry.getValue().RemoveAllComponents();
		}		
	}
	
	protected void ResetTheme() {
		for (Map.Entry<String, ABMCell> entry : this.Cells.entrySet()) {
			entry.getValue().ResetTheme();
		}
	}
	
	protected void CleanUp() {
		for (Map.Entry<String, ABMCell> entry : this.Cells.entrySet()) {
			entry.getValue().CleanUp();
		}
		this.Cells.clear();
		this.Page=null;
	}	
	
	public ABMCell Cell(int cell) {
		if (cell==-1) return null;
		String cellId = "C" + cell;
		ABMCell ccell = Cells.getOrDefault(ID.toLowerCase() + cellId.toLowerCase(), null);
		if (ccell==null) {			
			BA.Log("No cell " + cellId + " found in row " + ID);;
			return null;
		}
		return ccell;
	}
	
	public void SetFixedHeight(int fixedHeightPx, boolean scrollIfContentBigger) {
		for (Entry<String, ABMCell> cell : Cells.entrySet()) {
			cell.getValue().SetFixedHeight(fixedHeightPx, scrollIfContentBigger);
		}
	}
	
	public void SetFixedHeightFromBottom(int fixedHeightFrombottomPx, boolean scrollIfContentBigger) {
		for (Entry<String, ABMCell> cell : Cells.entrySet()) {
			cell.getValue().SetFixedHeightFromBottom(fixedHeightFrombottomPx, scrollIfContentBigger);
		}
	}
	
	protected ABMCell CellInternal(String cellId) {
		ABMCell ccell = Cells.getOrDefault(cellId.toLowerCase(), null);
		if (ccell==null) {
			BA.Log("No cell " + cellId + " found in row " + ID);;
			return null;
		}
		return ccell;
	}	
	
	public void SetDivider(ABMDivider divider) {
		this.Divider = (ABMDivider) divider.Clone();
	}	
	
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Rows.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Rows.get(themeName.toLowerCase()).Clone();				
			}
		}
	}
	
	@Hide
	protected String Build() {
		if (Page!=null) {
			if (Theme.ThemeName.equals("default")) {
				Theme.Colorize(Page.CompleteTheme.MainColor);
			}
		}
		StringBuilder s = new StringBuilder();
		if (CenterInPage) {
			s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "_cont\" class=\"container\">\n");
    	}
		String clickObj="";		
		if (ClickableTarget.equals("")) {
			clickObj="Page";
		} else {
			clickObj=ClickableTarget;
		}
		switch (RowType) {
		case "LayoutFixedFluidFixed": // TODO for FixedFluidFixed
			
			break;
		default:
			String B4JSData="";
			if (!mB4JSUniqueKey.equals("")) {
				B4JSData = " data-b4js=\"" + mB4JSUniqueKey + "\" data-b4jsextra=\"" + HasHover + "\" ";
			} else {
				B4JSData = " data-b4js=\"\" data-b4jsextra=\"" + HasHover + "\" ";
			}
			s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" " + B4JSData + " class=\"" + BuildClass() + "\" style=\"" + BuildStyle() + "\" data-click=\"" + clickObj + "," + ID.substring(1) + "\">\n");
		}
			
		s.append(BuildBody());
		
		if (Divider!=null) {
			s.append(Divider.Build());
		}
		
		s.append("</div>\n");	
		if (CenterInPage) {
    		s.append("</div>\n");
    	}
		
		IsBuild=true;
		return s.toString();
	}
	
	@Hide
	protected String BuildClass() {
		StringBuilder s = new StringBuilder();
		String rtl="";
		
		if (ForReport) {
			s.append(" " + ABMaterial.GetColorStr(Theme.BackColor,Theme.BackColorIntensity + " " + mVisibility + " " + Theme.ZDepth + mIsPrintableClass + mIsOnlyForPrintClass + rtl, ""));
		} else {
			s.append("row " + ABMaterial.GetColorStr(Theme.BackColor,Theme.BackColorIntensity + " " + mVisibility + " " + Theme.ZDepth + mIsPrintableClass + mIsOnlyForPrintClass + rtl, ""));
		}
		if (Theme.Clickable || mZABMode) {
			s.append(" rclick " + ABMaterial.GetWavesEffect(Theme.ClickableWavesEffect, Theme.ClickableWavesCircle));
		}
		if (mZABMode) {
			s.append(" zabrow ");
		}
		String tmpZoom="";
		if (mZoom!=Integer.MIN_VALUE) {
			tmpZoom = " zoom" + mZoom + " zoomL ";
		}
		s.append(tmpZoom);
		s.append(ExtraClasses + " ");
		s.append(mPrintPageBreak);		
		return s.toString();
	}
	
	public void SetExtraStyle(String extraStyle) {
		if (extraStyle.length()>0) {
			if (!extraStyle.endsWith(";")) {
				extraStyle = extraStyle + ";";
			}
		}
		ExtraStyle = extraStyle;
	}
	
	public void SetExtraClasses(String extraClasses) {
		ExtraClasses = extraClasses;
	}
	
	@Hide
	protected String BuildStyle() {
		
		StringBuilder s = new StringBuilder();
		if (MarginTop!="") s.append(" margin-top: " + MarginTop + ";");
		if (MarginBottom!="") s.append(" margin-bottom: " + MarginBottom + ";");
		if (MarginLeft!="") s.append(" margin-left: " + MarginLeft + ";");
		if (MarginRight!="") s.append(" margin-right: " + MarginRight + ";");
		if (PaddingTop!="") s.append(" padding-top: " + PaddingTop + ";");
		if (PaddingBottom!="") s.append(" padding-bottom: " + PaddingBottom + ";");
		if (PaddingLeft!="") s.append(" padding-left: " + PaddingLeft + ";");
		if (PaddingRight!="") s.append(" padding-right: " + PaddingRight + ";");
		if (Theme.BorderWidth > 0) {
			s.append(" border: " + Theme.BorderWidth + "px solid " + ABMaterial.GetColorStrMap(Theme.BorderColor,  Theme.BorderColorIntensity) + ";");
		}
		if (Theme.BorderRadiusPx>0) {
			s.append(" border-radius: " + Theme.BorderRadiusPx + "px;");
		} else {
			if (Theme.BorderRadiusBottomLeftPx + Theme.BorderRadiusBottomRightPx + Theme.BorderRadiusTopLeftPx + Theme.BorderRadiusTopRightPx>0) {
				s.append(" border-top-left-radius: " + Theme.BorderRadiusTopLeftPx + "px;");
				s.append(" border-top-right-radius: " + Theme.BorderRadiusTopRightPx + "px;");
				s.append(" border-bottom-left-radius: " + Theme.BorderRadiusBottomLeftPx + "px;");
				s.append(" border-bottom-right-radius: " + Theme.BorderRadiusBottomRightPx + "px;");
			}
		}
		if (!Theme.BorderStyle.equals("")) {
			s.append(" border-style: " + Theme.BorderStyle + ";");
		}
		
		s.append(ExtraStyle);
		return s.toString();
	}	
	
	public void Refresh() {
		RefreshInternal(false, true, true);
	}
	
	
	protected void CheckIfInBrowser(StringBuilder ToCheckSB) {		
		String compIDExtra="";			
		compIDExtra = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();		
		ToCheckSB.append("if (document.getElementById('" + compIDExtra + "')==null) {notExists+=';" + compIDExtra + ";'} else {Exists+=';" + compIDExtra + ";'};");
		
		for (Entry<String, ABMCell> cell : Cells.entrySet()) {
			cell.getValue().CheckIfInBrowser(ToCheckSB);
			
		}
	}
	
	public String getVisibility() {		
		return mVisibility;
	}
		
	public void setVisibility(String visibility) {
		mVisibility=visibility;		
	}
	
	public void SetVisibilityFlush(String visibility, boolean DoFlush) {
		mVisibility=visibility;		
		ABMaterial.ChangeVisibility(Page, getID(), mVisibility);
		switch (mVisibility) {
		case ABMaterial.VISIBILITY_ALL:
		case ABMaterial.VISIBILITY_SHOW_ON_SMALL_ONLY:
		case ABMaterial.VISIBILITY_SHOW_ON_MEDIUM_ONLY:
		case ABMaterial.VISIBILITY_SHOW_ON_LARGE_ONLY:
		case ABMaterial.VISIBILITY_SHOW_ON_EXTRALARGE_ONLY:				
		case ABMaterial.VISIBILITY_SHOW_ON_MEDIUM_AND_ABOVE:
		case ABMaterial.VISIBILITY_SHOW_ON_MEDIUM_AND_BELOW:	
		case ABMaterial.VISIBILITY_SHOW_ON_LARGE_AND_ABOVE:
		case ABMaterial.VISIBILITY_SHOW_ON_LARGE_AND_BELOW:
			Page.ws.Eval("syncHeaderFooters()", null);
		}
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Component SetVisibilityFlush: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
		
	/**
	 * NOTE: if using the rows hide and show methods, you can NOT use the visibility property (is always VISIBILITY_HIDE_ALL)!
	 *       if you don't have a refresh of something after it, you will need to do a page.ws.flush
	 */	
	public void Hide() {
		mVisibility = ABMaterial.VISIBILITY_HIDE_ALL;
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		j.SetProp("class", BuildClass());		
	}	
	
	/**
	 * NOTE: if using the rows hide and show methods, you can NOT use the visibility property! (is always VISIBILITY_ALL)!
	 *       if you don't have a refresh of something after it, you will need to do a page.ws.flush
	 */	
	public void Show() {
		mVisibility = ABMaterial.VISIBILITY_ALL;
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		j.SetProp("class", BuildClass());		
	}
	
	
	/**
	 * This is a limited version of Refresh() which will only refresh the row properties + each cell properties, NOT its contents.
	 */
	public void RefreshNoContents(boolean DoFlush) {
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		j.SetProp("class", BuildClass());
		j.SetProp("style", BuildStyle());
		
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
		
		for (Entry<String, ABMCell> cell : Cells.entrySet()) {	
			cell.getValue().SetZAB(mZABMode);
			cell.getValue().RefreshNoContents(false);
		}
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("RefreshNoContents");}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
	}
	
	
	protected boolean ResetNotInBrowser(String ToCheck, String OKCheck) {
		boolean ret = true;
		String compIDExtra="";
		compIDExtra = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();		
		if (ToCheck.contains(";" + compIDExtra + ";")) {
			IsBuild = false;			
			ret = false;
		}
		if (OKCheck.contains(";" + compIDExtra + ";")) {												
			IsBuild = true;			
		} 
		
		for (Entry<String, ABMCell> cell : Cells.entrySet()) {
			if (!cell.getValue().ResetNotInBrowser(ToCheck, OKCheck)) {
				ret = false;
			}
		}
		
		return ret;
	}
	
	
	protected void RefreshInternal(boolean onlyNew, boolean DoTheChecks, boolean DoFlush) {
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		j.SetProp("class", BuildClass());
		j.SetProp("style", BuildStyle());
		
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
		
		int Counter = 0;
		String prevID="";
		String compID="";
		
		
		StringBuilder ToCheckSB = new StringBuilder();		
		String ToCheck="";
		String OKCheck="";
		@SuppressWarnings("unused")
		boolean ret = true;
		if (DoTheChecks && Page.ws!=null) {
			for (Entry<String, ABMCell> cell : Cells.entrySet()) {
				cell.getValue().SetZAB(mZABMode);
				for(Iterator<Map.Entry<String, ABMComponent>>it=cell.getValue().Components.entrySet().iterator();it.hasNext();){
					Map.Entry<String, ABMComponent> comp = it.next();
					if (!comp.getValue().HadInBrowserCheck) {
						compID = comp.getValue().getID();
						ToCheckSB.append("if (document.getElementById('" + compID + "')==null) {notExists+=';" + compID + ";'} else {Exists+=';" + compID + ";'};");
					}
					switch (comp.getValue().Type) {								
					case ABMaterial.UITYPE_ABMCONTAINER:
						ABMContainer cont = (ABMContainer) comp.getValue();
						cont.CheckIfInBrowser(ToCheckSB);
						break;
				    }
				}
			}
			ToCheck = ToCheckSB.toString();
			if (!ToCheck.equals("")) {
				SimpleFuture fut = Page.ws.EvalWithResult("var notExists=';$%';var Exists='$%';var varbody=document.body.innerHTML;" + ToCheck + "return notExists + ':' + Exists + ':';", null);
				ToCheck="";
				try {				
					ToCheck = (String) fut.getValue();
					String[] spl = ToCheck.split(":");
					ToCheck = spl[0];
					OKCheck = spl[1];
				} catch (InterruptedException e) {						
				} catch (TimeoutException e) {						
				} catch (ExecutionException e) {						
				} catch (IOException e) {						
				}
			}

			for (Entry<String, ABMCell> cell : Cells.entrySet()) {					
				if (!cell.getValue().ResetNotInBrowser(ToCheck, OKCheck)) {
					ret = false;
				}					
			}
		} 
			
		
		for (Entry<String, ABMCell> cell : Cells.entrySet()) {	
			cell.getValue().ForReport = ForReport;
			cell.getValue().SetZAB(mZABMode);
			if (cell.getValue().IsBuild) {
				if (!onlyNew) {
					if (!cell.getValue().RefreshInternalExtra(onlyNew, DoTheChecks, false)); {
						//IsBuild=false;
					}
				}
				prevID = cell.getValue().RootID();
			} else {
				if (Counter==0) {			
					ABMaterial.AddHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), cell.getValue().Build());
					prevID = cell.getValue().RootID();
				} else {
					ABMaterial.InsertHTMLAfter(Page, cell.getValue().ParentString + prevID, cell.getValue().Build());
					prevID = cell.getValue().RootID();						
				}
				
				Counter++;
			}
				
		}
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Row RefreshInternal");}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
	}
	
	protected String BuildBody() {
		StringBuilder s = new StringBuilder();
		for (Entry<String, ABMCell> cell : Cells.entrySet()) {
			cell.getValue().SetZAB(mZABMode);
			s.append(cell.getValue().Build());
		}
		return s.toString();
	}	
	
	@Hide
	protected void Prepare() {
		IsBuild=true;
		for (Entry<String, ABMCell> cell : Cells.entrySet()) {
			cell.getValue().SetZAB(mZABMode);
			cell.getValue().Prepare();							
        }
	}
	
	protected void ResetSVGs() {
		for (Entry<String, ABMCell> cell : Cells.entrySet()) {
			cell.getValue().ResetSVGs();
		}
	}
	
	protected void RunAllFirstRuns() {
		RunAllFirstRunsInternal(true);
	}
	
	@Hide
	protected void RunAllFirstRunsInternal(boolean DoFlush) {
		FirstRun();
		for (Entry<String, ABMCell> cell : Cells.entrySet()) {
			cell.getValue().RunAllFirstRunsInternal(false);							
        }
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Row RunAllFirstRunsInternal");}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
	}
	
	protected void FirstRun() {
		
	}
	
	protected void SetEventHandlerParent(Object parentEventHandler) {
		for (Entry<String, ABMCell> cell : Cells.entrySet()) {
			cell.getValue().SetEventHandlerParent(parentEventHandler);							
        }
	}	
	
	protected ABMRow Clone() {
		ABMRow r = new ABMRow();
		r.ArrayName = ArrayName;
		r.CenterInPage = CenterInPage;
		r.ID = ID;
		r.Page = Page;
		r.Theme = Theme.Clone();
		r.mVisibility = mVisibility;
		r.ClickableTarget = ClickableTarget; 
		r.MarginLeft=MarginLeft;
		r.MarginRight=MarginRight;
		r.MarginTop=MarginTop;
		r.MarginBottom=MarginBottom;
		r.PaddingTop=PaddingTop;
		r.PaddingBottom=PaddingBottom;
		r.PaddingLeft=PaddingLeft;
		r.PaddingRight=PaddingRight;
		r.mIsPrintableClass=mIsPrintableClass;
		r.mIsOnlyForPrintClass=mIsOnlyForPrintClass;
		r.mPrintPageBreak=mPrintPageBreak;
		r.mZABMode = mZABMode;
		for (Map.Entry<String, ABMCell> entry : Cells.entrySet()) {
			r.Cells.put(entry.getKey(), entry.getValue().Clone());
		}		
		return r;		
	}
}
