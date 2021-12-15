package com.ab.abmaterial;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
//import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

//import anywheresoftware.b4a.B4AClass;
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
public class ABMCell extends ABMObject{	
	private static final long serialVersionUID = 1332421709861314078L;
	protected Map<String, ABMComponent> Components = new LinkedHashMap<String, ABMComponent>();	
	protected String ID="";
	protected transient ABMPage Page;	
	protected int SizeSmall=1;
	protected int SizeMedium=1;
	protected int SizeLarge=1;
	protected int OffsetSmall=0;
	protected int OffsetMedium=0;
	protected int OffsetLarge=0;
	protected String RowId="";
	protected ThemeCell Theme=new ThemeCell();
	protected boolean IsBuild=false;
	public String PaddingLeft=""; //"0.75rem";
	public String PaddingRight=""; //"0.75rem";
	public String PaddingTop=""; //"0rem";
	public String PaddingBottom=""; //"0rem";
	public String MarginTop=""; //"0px";
	public String MarginBottom=""; //"0px";
	public String MarginLeft=""; //"0px";
	public String MarginRight=""; //"0px";
	protected String ArrayName="";
	protected int FixedHeight=-99999;
	protected String CellInfo;
	protected transient ABMContainer Parent=null;
	protected String ParentString="";
	protected int FixedHeightFromBottom=-99999;
	protected String overflowY="";
	
	protected String mVisibility=ABMaterial.VISIBILITY_ALL;
	
	protected String ClickableTarget="";
	protected String DragDropName="";
	protected String DragDropGroupName="";
	protected boolean NeedsRefreshMatchHeight=false;
	protected int DragDropMinHeight=0;
	
	protected String mIsPrintableClass="";
	protected String mPrintPageBreak="";
	
	protected boolean mZABMode=false;
	
	protected String CellType="";
	protected int CellWidth=0;
	protected int cCounter=0;
	
	protected boolean mIsMultiContainerCell=false;	
	protected ABMTheme AppTheme=null;
	
	protected String mCurrentMultiContainer="";
	protected String mCurrentContainerClass="";
	protected Map<String,Boolean> mIsContainerBuild = new LinkedHashMap<String, Boolean>();

	protected Map<String,Object> PageMap = new LinkedHashMap<String,Object>();
	protected Map<String,String> PageMapContainerIds = new LinkedHashMap<String,String>();
	
	protected String mB4JSUniqueKey="";
	protected boolean HasHover=false;
	
	protected String HelpTip="";
	protected boolean HelpTipIsDirty=false;
	
	private int mRightToLeft=0;
	
	protected String ExtraStyle="";
	protected String ExtraClasses="";
	
	protected boolean ForReport=false;
		
	ABMCell() {
		this.Type = ABMaterial.UITYPE_CELL;
	}	
	
	public void setRightToLeft(boolean rtl) {
		if (rtl) {
			mRightToLeft=1;
		} else {
			mRightToLeft=-1;
		}
	}
	
	public boolean getRightToLeft() {
		if (mRightToLeft==0) {
			return Page.getRightToLeft();
		} else {
			if (mRightToLeft==1) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	@Hide
	public void ZABInit() {
		ZABDEF.InitProperty("Margin Top", "0", "", true, "string", true,1);
		ZABDEF.InitProperty("Margin Bottom", "0", "", true, "string", true,1);
		ZABDEF.InitProperty("Padding Left", "0", "", true, "string", true,1);
		ZABDEF.InitProperty("Padding Right", "0", "", true, "string", true,1);
		
		ZABDEF.InitProperty("Theme Name", "", "", true, "string", false,1);
		ZABDEF.InitProperty("Visibility", "VISIBILITY_ALL", "VISIBILITY_ALL:VISIBILITY_HIDE_ALL:VISIBILITY_HIDE_ON_SMALL_ONLY:VISIBILITY_HIDE_ON_MEDIUM_ONLY:VISIBILITY_HIDE_ON_MEDIUM_AND_BELOW:VISIBILITY_HIDE_ON_MEDIUM_AND_ABOVE:VISIBILITY_HIDE_ON_LARGE_ONLY:VISIBILITY_SHOW_ON_SMALL_ONLY:VISIBILITY_SHOW_ON_MEDIUM_ONLY:VISIBILITY_SHOW_ON_MEDIUM_AND_ABOVE:VISIBILITY_SHOW_ON_MEDIUM_AND_BELOW:VISIBILITY_SHOW_ON_LARGE_ONLY", true, "string", true, 1);
		
		ZABDEF.InitProperty("ParentString", ParentString, "", false, "string", false,0);
		ZABDEF.InitProperty("isbuild", IsBuild, "", false, "boolean", false,1);
		ZABDEF.InitProperty("Array Name", ArrayName, "", false, "string", false, 0);
		ZABDEF.InitProperty("ID", ID, "", false, "string", false, 0);
		ZABDEF.InitProperty("ZABMode", mZABMode, "", false, "boolean", false,0);
		
		ZABDEF.InitProperty("SizeSmall", SizeSmall, "", false, "int", false, 0);
		ZABDEF.InitProperty("SizeMedium", SizeMedium, "", false, "int", false, 0);
		ZABDEF.InitProperty("SizeLarge", SizeLarge, "", false, "int", false, 0);
		
		ZABDEF.InitProperty("OffsetSmall", OffsetSmall, "", false, "int", false, 0);
		ZABDEF.InitProperty("OffsetMedium", OffsetMedium, "", false, "int", false, 0);
		ZABDEF.InitProperty("OffsetLarge", OffsetLarge, "", false, "int", false, 0);
		
		ZABDEF.InitProperty("RowId", RowId, "", false, "string", false, 0);
		
		ZABDEF.InitProperty("CellInfo", CellInfo, "", false, "string", false, 0);
	}
	
	@Hide
	public void ZABBuild(ABMPage page) throws NoSuchFieldException, SecurityException {
		this.Page = page;
		String ts = (String) ZABDEF.Property("theme name").Value;
		mVisibility = ABMaterial.GetConst((String) ZABDEF.Property("visibility").Value);
		
		PaddingLeft = (String) ZABDEF.Property("padding left").Value;
		PaddingRight = (String) ZABDEF.Property("padding right").Value;
		MarginTop = (String) ZABDEF.Property("margin top").Value;
		MarginBottom = (String) ZABDEF.Property("margin bottom").Value;
		FixedHeight = (int) ZABDEF.Property("Fixed Height").Value;
		if (FixedHeight==0) {
			FixedHeight=-99999;
		}
		
		ParentString = (String) ZABDEF.Property("parentstring").Value;
		IsBuild = (Boolean) ZABDEF.Property("isbuild").Value;
		String s = (String) ZABDEF.Property("array name").Value;
		if (!s.equals("")) {
			AddArrayName(s);
		}
		ID = (String) ZABDEF.Property("id").Value;
		mZABMode= (Boolean) ZABDEF.Property("zabmode").Value;
		
		SizeSmall = (int) ZABDEF.Property("sizesmall").Value;
		SizeMedium = (int) ZABDEF.Property("sizemedium").Value;
		SizeLarge = (int) ZABDEF.Property("sizelarge").Value;
		
		OffsetSmall = (int) ZABDEF.Property("offsetsmall").Value;
		OffsetMedium = (int) ZABDEF.Property("offsetmedium").Value;
		OffsetLarge = (int) ZABDEF.Property("offsetlarge").Value;
		
		RowId = (String) ZABDEF.Property("rowid").Value;
		
		CellInfo = (String) ZABDEF.Property("cellinfo").Value;
		
		UseTheme(ts);
		
		
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
	
	@Hide
	public void ZABFromZABDEF(ZAbstractDesignerDefinition zabdef) {
		ZABDEF = zabdef.Clone();
	}
	
	/**
	 * Enable/Disable this component from being printed 
	 */
	public void setIsPrintable(Boolean printme) {
		if (printme) {
			mIsPrintableClass="";
		} else {
			mIsPrintableClass=" no-print ";
		}
	}
	
	public boolean getIsPrintable() {
		return mIsPrintableClass.equals("");
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
	
	protected void SetZAB(boolean ZABMode) {
		mZABMode = ZABMode;
	}
	
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;		
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
	
	protected void SetParentStringAndClick(String parentName) {
		this.ParentString = parentName + "-";
		this.ClickableTarget = parentName;
		for (Map.Entry<String, ABMComponent> entry : Components.entrySet()) {
			entry.getValue().ParentString = this.ParentString;
		}
	}
	
	protected String Serialize() throws UnsupportedEncodingException {
		StringBuilder s = new StringBuilder();
		s.append("ID:" + ID + ";");
		s.append("SizeSmall:" + SizeSmall + ";");
		s.append("SizeMedium:" + SizeMedium + ";");
		s.append("SizeLarge:" + SizeLarge + ";");
		s.append("OffsetSmall:" + OffsetSmall + ";");
		s.append("OffsetMedium:" + OffsetMedium + ";");
		s.append("OffsetLarge:" + OffsetLarge + ";");
		s.append("RowId:" + RowId + ";");
		s.append("Theme:" + SerializeTheme() + ";");
		s.append("ParentString:" + ParentString + ";");
		s.append("PaddingLeft:" + PaddingLeft + ";");
		s.append("PaddingRight:" + PaddingRight + ";");
		s.append("PaddingTop:" + PaddingTop + ";");
		s.append("PaddingBottom:" + PaddingBottom + ";");
		s.append("MarginTop:" + MarginTop + ";");
		s.append("MarginBottom:" + MarginBottom + ";");
		s.append("MarginLeft:" + MarginLeft + ";");
		s.append("MarginRight:" + MarginRight + ";");
		s.append("ArrayName:" + ArrayName + ";");
		s.append("FixedHeight:" + FixedHeight + ";");
		s.append("CellInfo:" + new String(Base64.getEncoder().encode(CellInfo.toString().getBytes("UTF-8"))) + ";");
				
		return new String(Base64.getEncoder().encode(s.toString().getBytes("UTF-8")));
	}
	
	protected void Deserialize(String value) throws UnsupportedEncodingException {
		byte b[] = Base64.getDecoder().decode(value.getBytes("UTF-8"));
		String decod = new String(b, "UTF-8");
		String[] values = Regex.Split(";", decod);
		for (String s: values) {
			String[] val = Regex.Split(":", s);
			if (val.length==2) {
				switch (val[0]) {
				case "ID":
					ID = val[1];
					break;
				case "SizeSmall":
					SizeSmall = Integer.parseInt(val[1]);
					break;
				case "SizeMedium":
					SizeMedium = Integer.parseInt(val[1]);
					break;
				case "SizeLarge":
					SizeLarge = Integer.parseInt(val[1]);
					break;
				case "OffsetSmall":
					OffsetSmall = Integer.parseInt(val[1]);
					break;
				case "OffsetMedium":
					OffsetMedium = Integer.parseInt(val[1]);
					break;
				case "OffsetLarge":
					OffsetLarge = Integer.parseInt(val[1]);
					break;
				case "RowId":
					RowId = val[1];
					break;
				case "Theme":
					DeserializeTheme(val[1]);
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
				case "ArrayName": 
					ArrayName = val[1];
					break;
				case "FixedHeight":
					FixedHeight = Integer.parseInt(val[1]);
					break;
				case "CellInfo":
					byte b2[] = Base64.getDecoder().decode(val[1].getBytes("UTF-8"));
					CellInfo = new String(b2, "UTF-8");
					break;
				}
			}
		}
	}
	
	private String SerializeTheme() throws UnsupportedEncodingException {
		StringBuilder s = new StringBuilder();
		s.append(Theme.Align + ";" + Theme.BackColor + ";" + Theme.BackColorIntensity + ";" + Theme.BorderColor + ";" + Theme.BorderColorIntensity + ";" + Theme.BorderWidth + ";" + Theme.MainColor + ";" + Theme.ThemeName + ";" + Theme.ZDepth + ";THEEND");
		return new String(Base64.getEncoder().encode(s.toString().getBytes("UTF-8")));
	}
	
	private void DeserializeTheme(String value) throws UnsupportedEncodingException {
		byte b[] = Base64.getDecoder().decode(value.getBytes("UTF-8"));
		String decod = new String(b, "UTF-8");
		String[] values = Regex.Split(";", decod);
		Theme = new ThemeCell();
		Theme.Align = values[0];
		Theme.BackColor = values[1];
		Theme.BackColorIntensity = values[2];
		Theme.BorderColor = values[3];
		Theme.BorderColorIntensity = values[4];
		Theme.BorderWidth = Integer.parseInt(values[5]);
		Theme.MainColor = values[6];
		Theme.ThemeName = values[7];
		Theme.ZDepth = values[8];		
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
	
	protected void ResetTheme() {
		for (Map.Entry<String, ABMComponent> entry : this.Components.entrySet()) {
			entry.getValue().ResetTheme();
		}
	}
			
	protected void CleanUp() {
		for (Map.Entry<String, ABMComponent> entry : this.Components.entrySet()) {
			entry.getValue().CleanUp();
		}
		this.Components.clear();	
		this.Page=null;
		this.Parent=null;
	}
	
	public void RemoveAllComponents() {
		for (Map.Entry<String, ABMComponent> entry : this.Components.entrySet()) {
			ABMComponent comp = (ABMComponent) entry.getValue();		
			String componentId=comp.ArrayName.toLowerCase() + comp.ID.toLowerCase();
			
			if (comp.EventHandler!=null) {				
				Page.EventHandlers.remove(comp.ID.toLowerCase());
			}
			
			if (Parent==null) {
				Page.AllComponents.remove(componentId.toLowerCase());
			} else {
				Parent.AllComponents.remove(componentId.toLowerCase());
			}
			ABMaterial.RemoveComponent(Page, comp);
			comp.CleanUp();

			comp=null;
		}
		ABMaterial.EmptyHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		Components.clear();		
		mCurrentMultiContainer="";
	}
				
	public void SetFixedHeight(int fixedHeightPx, boolean scrollIfContentBigger) {
		this.FixedHeightFromBottom=-99999;
		this.FixedHeight=fixedHeightPx;
		if (scrollIfContentBigger) {
			overflowY= "overflow-y: auto;overflow-x: hidden;";
		}
	}
	
	public void SetFixedHeightFromBottom(int fixedHeightFrombottomPx, boolean scrollIfContentBigger) {
		this.FixedHeight = -99999;
		this.FixedHeightFromBottom=fixedHeightFrombottomPx;
		if (scrollIfContentBigger) {
			overflowY= "overflow-y: auto;overflow-x: hidden;";
		}
	}
	
	
	protected void ZABSetActive(boolean active) {
		try {
			if (Page.ws.getOpen()) {
				if (active) {
					ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "zabactive");
				} else {
					ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "zabactive");
				}
				if (Page.ShowDebugFlush) {BA.Log("ZABSetActive");}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}		
	
	public void AddComponent(ABMComponent component) {
		if (!component.mB4JSUniqueKey.equals("")) {
			String tmpComp=Page.B4JSUniqueComponents.getOrDefault(component.mB4JSUniqueKey.toLowerCase(), "");
			if (!tmpComp.equals("")) {
				BA.Log("The B4JSUniqueKey " + component.mB4JSUniqueKey + " is already used by component " + tmpComp + "!");				
			} else {
				Page.B4JSUniqueComponents.put(component.mB4JSUniqueKey.toLowerCase(), component.getID());
			}
		}
		if (mIsMultiContainerCell) {
			BA.LogError("Cell " + RowId + " " + ID + " is a multi container cell.  Use SetIsMultiContainerCell() or use AddMultiContainer() instead!");
			return;
		}
		if (!DragDropName.equals("")) {
			BA.LogError("Cell " + RowId + " " + ID + " is a DragDropZone.  Use AddArrayComponent() instead!");
			return;
		}
		if (Components.getOrDefault(component.ID.toLowerCase(), null)==null && Components.getOrDefault("upload" + component.ID.toLowerCase(), null)==null) {
			component.CellId = ID;
			component.RowId = RowId;	
			if (component.Type!=ABMaterial.UITYPE_XPLAY) {
				component.ParentString = this.ParentString;
			} else {
				component.ParentString = "";
			}
			component.CellAlign = Theme.Align;
			component.SetEventHandler();
		
			String extra = ABMaterial.AddComponent(Page, component);		
			if (Parent==null) {
				Page.AllComponents.put(extra+component.ID.toLowerCase(), new ABMRowCell(component.RowId, component.CellId));
			} else {
				Parent.AllComponents.put(extra+component.ID.toLowerCase(), new ABMRowCell(component.RowId, component.CellId));
			}
			Components.put(extra+component.ID.toLowerCase(), component);
		}
	}
	
	/**
	 * 
	 * IsVisible: Set the initial showing container (only one can show at the same time!)
	 */
	private void AddMultiContainer(String className, ABMContainer container, boolean IsVisible) {
		if (!mIsMultiContainerCell) {
			BA.LogError("Cell " + RowId + " " + ID + " is not a multi container cell. Use SetIsMultiContainerCell() or use AddComponent() instead!");
			return;
		}		
		if (!DragDropName.equals("")) {
			BA.LogError("Cell " + RowId + " " + ID + " is a DragDropZone.  Use AddArrayComponent() instead!");
			return;
		}
		if (!container.mB4JSUniqueKey.equals("")) {
			String tmpComp=Page.B4JSUniqueComponents.getOrDefault(container.mB4JSUniqueKey.toLowerCase(), "");
			if (!tmpComp.equals("")) {
				BA.Log("The B4JSUniqueKey " + container.mB4JSUniqueKey + " is already used by component " + tmpComp + "!");
				//return;
			} else {
				Page.B4JSUniqueComponents.put(container.mB4JSUniqueKey.toLowerCase(), container.getID());
			}
		}
		if (Components.getOrDefault(container.ID.toLowerCase(), null)==null && Components.getOrDefault("upload" + container.ID.toLowerCase(), null)==null) {
			container.CellId = ID;
			container.RowId = RowId;	
			if (container.Type!=ABMaterial.UITYPE_XPLAY) {
				container.ParentString = this.ParentString;
			} else {
				container.ParentString = "";
			}
			container.CellAlign = Theme.Align;
			container.SetEventHandler();
			
			if (!IsVisible) {
				container.mVisibility = ABMaterial.VISIBILITY_HIDE_ALL;				
			} else {
				container.mVisibility = ABMaterial.VISIBILITY_ALL;
			}
		
			String extra = ABMaterial.AddComponent(Page, container);		
			if (Parent==null) {
				Page.AllComponents.put(extra+container.ID.toLowerCase(), new ABMRowCell(container.RowId, container.CellId));
			} else {
				Parent.AllComponents.put(extra+container.ID.toLowerCase(), new ABMRowCell(container.RowId, container.CellId));
			}
			if (IsVisible) {
				mCurrentMultiContainer=extra+container.ID.toLowerCase();
			}
			Components.put(extra+container.ID.toLowerCase(), container);
			PageMapContainerIds.put(className.toLowerCase(), extra+container.ID.toLowerCase());
		}
	}
	
	public void SetIsMultiContainerCell(ABMTheme appTheme) {		
		AppTheme = appTheme;
		mIsMultiContainerCell = true;
	}
	
	public void AddMultiCellContainerClass(String className, Object containerClass) {
		if (!mIsMultiContainerCell) {
			BA.LogError("Cell " + RowId + " " + ID + " is not a multi container cell! Use SetIsMultiContainerCell()");
			return;
		}
		PageMap.put(className.toLowerCase(), containerClass);
	}
		
	public Object GetMultiCellContainerClass(String className) {
		if (!mIsMultiContainerCell) {
			BA.LogError("Cell " + RowId + " " + ID + " is not a multi container cell! Use SetIsMultiContainerCell()");
			return null;
		}
		return PageMap.getOrDefault(className.toLowerCase(), null);
	}
	
	public Object GetMultiCellCurrentContainerClass() {
		return GetMultiCellContainerClass(mCurrentContainerClass);
	}
	
	public void StartContainerClasses(BA ba, String initialPage) throws Exception {
		mIsContainerBuild = new LinkedHashMap<String, Boolean>();
		if (!mIsMultiContainerCell) {
			BA.LogError("Cell " + RowId + " " + ID + " is not a multi container cell! Use SetIsMultiContainerCell()");
			return;
		}
		for (Entry<String,Object> entry: PageMap.entrySet()) {
			ABMContainer containerPage = null;
			if (BA.debugMode) {
				containerPage = (ABMContainer) anywheresoftware.b4a.keywords.Common.CallSubDebug(ba, entry.getValue(), "GetContainerPage");
			} else {
				containerPage = (ABMContainer) anywheresoftware.b4a.keywords.Common.CallSubNew(ba, entry.getValue(), "GetContainerPage");
			}			
			if (entry.getKey().equalsIgnoreCase(initialPage)) {
				mCurrentContainerClass = initialPage;
				if (BA.debugMode) {
					anywheresoftware.b4a.keywords.Common.CallSubDebug(ba, entry.getValue(), "BuildContainerPage");
					mIsContainerBuild.put(initialPage.toLowerCase(), true);
					AddMultiContainer(entry.getKey(), containerPage, true);
					anywheresoftware.b4a.keywords.Common.CallSubDebug(ba, entry.getValue(), "ConnectContainerPage");
				} else {
					anywheresoftware.b4a.keywords.Common.CallSubNew(ba, entry.getValue(), "BuildContainerPage");
					mIsContainerBuild.put(initialPage.toLowerCase(), true);
					AddMultiContainer(entry.getKey(), containerPage, true);
					anywheresoftware.b4a.keywords.Common.CallSubNew(ba, entry.getValue(), "ConnectContainerPage");
				}				
			} else {
				AddMultiContainer(entry.getKey(), containerPage, false);				
			}
			
		}
		
		Page.ws.Eval("ElementQueries.update();", null);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("StartContainerClasses");}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public void GoToMultiContainerClass(BA ba, String className, String easing, int transitionTimeMs) throws Exception {
		if (easing.equals("")) {
			easing = "linear";
		}
		if (!mIsMultiContainerCell) {
			BA.LogError("Cell " + RowId + " " + ID + " is not a multi container cell! Use SetIsMultiContainerCell()");
			return;
		}
		if (BA.debugMode) {
			if (!mIsContainerBuild.getOrDefault(className.toLowerCase(), false)) {
				anywheresoftware.b4a.keywords.Common.CallSubDebug(ba, GetMultiCellContainerClass(className), "BuildContainerPage");
				mIsContainerBuild.put(className.toLowerCase(), true);
			}
			anywheresoftware.b4a.keywords.Common.CallSubDebug(ba, GetMultiCellContainerClass(className), "ConnectContainerPage");	
		} else {
			if (!mIsContainerBuild.getOrDefault(className.toLowerCase(), false)) {
				anywheresoftware.b4a.keywords.Common.CallSubNew(ba, GetMultiCellContainerClass(className), "BuildContainerPage");
				mIsContainerBuild.put(className.toLowerCase(), true);
			}
			anywheresoftware.b4a.keywords.Common.CallSubNew(ba, GetMultiCellContainerClass(className), "ConnectContainerPage");
		}
		String containerId = PageMapContainerIds.get(className.toLowerCase());
		if (!mCurrentMultiContainer.equals("")) {
			ABMContainer prevCont = (ABMContainer) Component(mCurrentMultiContainer);
			if (prevCont!=null) {
				ABMContainer nextCont = (ABMContainer) Component(containerId);
				if (nextCont!=null) {
					
					StringBuilder sh = new StringBuilder();
			
					prevCont.mVisibility = ABMaterial.VISIBILITY_HIDE_ALL;
					nextCont.mVisibility = ABMaterial.VISIBILITY_ALL;
					sh.append("$('#" + prevCont.ParentString + prevCont.ArrayName.toLowerCase() + prevCont.ID.toLowerCase() + "').fadeTo(" + (transitionTimeMs/2)  + ", 0.0, '" + easing + "', function() {"); 
					sh.append("$('#" + prevCont.ParentString + prevCont.ArrayName.toLowerCase() + prevCont.ID.toLowerCase() + "').addClass('hide');");
					sh.append("window.scrollTo(0, 0);");
					sh.append("$('#" + nextCont.ParentString + nextCont.ArrayName.toLowerCase() + nextCont.ID.toLowerCase() + "').removeClass('hide');");
					sh.append("ElementQueries.update();");
					sh.append("$('#" + nextCont.ParentString + nextCont.ArrayName.toLowerCase() + nextCont.ID.toLowerCase() + "').fadeTo(" + (transitionTimeMs/2)  + ", 1.0, '" + easing + "', function() {");
					/*
					 * Target
					 * Row
					 * Cell
					 * PreviousContainerId
					 * CurrentContainerId
					 */
					int row = Integer.parseInt(RowId.substring(1));
					int cell = Integer.parseInt(ID.substring(RowId.length()+1));
					String target = ParentString + ArrayName.toLowerCase();
					if (target.equals("")) {
						target = "Page";
					}
					sh.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_multicellfinished','eventparams':'target,row,cell,previouscontainerid,currentcontainerid','target': '" + target + "','row':" +  row + ",'cell':" + cell + ",'previouscontainerid':'" + mCurrentMultiContainer + "','currentcontainerid':'" + containerId + "'});");
					sh.append("});");
					sh.append("});");
					
					Page.ws.Eval(sh.toString(), null);
					try {
						if (Page.ws.getOpen()) {
							if (Page.ShowDebugFlush) {BA.Log("GoToMultiContainer");}
							Page.ws.Flush();Page.RunFlushed();
						}
					} catch (IOException e) {			
						e.printStackTrace();
					}
				} 
			}
		} else {
			ABMContainer nextCont = (ABMContainer) Component(containerId);
			if (nextCont!=null) {
				//BA.Log("next is not null");
				StringBuilder sh = new StringBuilder();
		
				nextCont.mVisibility = ABMaterial.VISIBILITY_ALL;
				sh.append("window.scrollTo(0, 0);");
				sh.append("$('#" + nextCont.ParentString + nextCont.ArrayName.toLowerCase() + nextCont.ID.toLowerCase() + "').removeClass('hide');");
				sh.append("ElementQueries.update();");
				sh.append("$('#" + nextCont.ParentString + nextCont.ArrayName.toLowerCase() + nextCont.ID.toLowerCase() + "').fadeTo(" + (transitionTimeMs/2)  + ", 1.0, '" + easing + "', function() {");
				/*
				 * Target
				 * Row
				 * Cell
				 * PreviousContainerId
				 * CurrentContainerId
				 */
				int row = Integer.parseInt(RowId.substring(1));
				int cell = Integer.parseInt(ID.substring(RowId.length()+1));
				String target = ParentString + ArrayName.toLowerCase();
				if (target.equals("")) {
					target = "Page";
				}
				sh.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_multicellfinished','eventparams':'target,row,cell,previouscontainerid,currentcontainerid','target': '" + target + "','row':" +  row + ",'cell':" + cell + ",'previouscontainerid':'" + mCurrentMultiContainer + "','currentcontainerid':'" + containerId + "'});");
				sh.append("});");				
				
				Page.ws.Eval(sh.toString(), null);
				try {
					if (Page.ws.getOpen()) {
						if (Page.ShowDebugFlush) {BA.Log("GoToMultiContainer");}
						Page.ws.Flush();Page.RunFlushed();
					}
				} catch (IOException e) {			
					e.printStackTrace();
				}
			}
		}
		mCurrentMultiContainer = containerId;
		mCurrentContainerClass = className;
	}
	
	protected void AddComponentDD(ABMComponent component) {		
		if (Components.getOrDefault(component.ID.toLowerCase(), null)==null && Components.getOrDefault("upload" + component.ID.toLowerCase(), null)==null) {
			component.CellId = ID;
			component.RowId = RowId;	
			component.mBelongsToDropZone = DragDropName;
			if (component.Type!=ABMaterial.UITYPE_XPLAY) {
				component.ParentString = this.ParentString;
			} else {
				component.ParentString = "";
			}
			component.CellAlign = Theme.Align;
		
			String extra = ABMaterial.AddComponent(Page, component);		
			if (Parent==null) {
				Page.AllComponents.put(extra+component.ID.toLowerCase(), new ABMRowCell(component.RowId, component.CellId));
			} else {
				Parent.AllComponents.put(extra+component.ID.toLowerCase(), new ABMRowCell(component.RowId, component.CellId));
			}
			Components.put(extra+component.ID.toLowerCase(), component);
		}
	}
	
	
	public void AddArrayComponent(ABMComponent component, String arrayName) {
		if (mIsMultiContainerCell) {
			BA.LogError("Cell " + RowId + " " + ID + " is a multi container cell.  Use AddMultiContainer() instead!");
			return;
		}
		if (!component.mB4JSUniqueKey.equals("")) {
			String tmpComp=Page.B4JSUniqueComponents.getOrDefault(component.mB4JSUniqueKey.toLowerCase(), "");
			if (!tmpComp.equals("")) {
				BA.Log("The B4JSUniqueKey " + component.mB4JSUniqueKey + " is already used by component " + tmpComp + "!");				
			} else {
				Page.B4JSUniqueComponents.put(component.mB4JSUniqueKey.toLowerCase(), component.getID());
			}
		}
		if (!DragDropName.equals("")) {
			component.mBelongsToDropZone = DragDropName;
			NeedsRefreshMatchHeight = true;				
		}
		switch (component.Type) {
		case ABMaterial.UITYPE_CUSTOMCARD:
			if (Components.getOrDefault(component.ArrayName.toLowerCase() + arrayName.toLowerCase() + component.ID.toLowerCase(), null)==null && Components.getOrDefault("upload" + component.ArrayName.toLowerCase() + arrayName.toLowerCase() + component.ID.toLowerCase(), null)==null) {
				if (component.Type==ABMaterial.UITYPE_XPLAY) {
					BA.Log("An ABMXPlay component cannot be added as an Array!)");
					return;
				}
				component.CellId = ID;
				component.RowId = RowId;
				component.AddArrayName(arrayName);
				component.CellAlign = Theme.Align;
				component.SetEventHandler();
							
				String extra = ABMaterial.AddComponent(Page, component);
				if (Parent==null) {
					Page.AllComponents.put(extra+component.ArrayName.toLowerCase()+component.ID.toLowerCase(), new ABMRowCell(component.RowId, component.CellId));
				} else {
					Parent.AllComponents.put(extra+component.ArrayName.toLowerCase()+component.ID.toLowerCase(), new ABMRowCell(component.RowId, component.CellId));
				}
				Components.put(extra + component.ArrayName.toLowerCase() + component.ID.toLowerCase(), component);
			}
			break;
		default:
			if (Components.getOrDefault(component.ArrayName.toLowerCase() + arrayName.toLowerCase() + component.ID.toLowerCase(), null)==null && Components.getOrDefault("upload" + component.ArrayName.toLowerCase()  + arrayName.toLowerCase() + component.ID.toLowerCase(), null)==null) {
				if (component.Type==ABMaterial.UITYPE_XPLAY) {
					BA.Log("An ABMXPlay component cannot be added as an Array!)");
					return;
				}
				component.CellId = ID;
				component.RowId = RowId;
				component.AddArrayName(arrayName);
				component.ParentString = this.ParentString;
				component.CellAlign = Theme.Align;
				component.SetEventHandler();
			
				String extra = ABMaterial.AddComponent(Page, component);
				if (Parent==null) {
					Page.AllComponents.put(extra+component.ArrayName.toLowerCase()+component.ID.toLowerCase(), new ABMRowCell(component.RowId, component.CellId));
				} else {
					Parent.AllComponents.put(extra+component.ArrayName.toLowerCase()+component.ID.toLowerCase(), new ABMRowCell(component.RowId, component.CellId));
				}
				Components.put(extra + component.ArrayName.toLowerCase() + component.ID.toLowerCase(), component);
			}
		}
		
	}
		
	public void InsertComponentBefore(ABMComponent component, String beforeID) {
		component.beforeID = beforeID.toLowerCase();
		AddComponent(component);
	}
	
	public void InsertArrayComponentBefore(ABMComponent component, String arrayName, String beforeID) {
		component.beforeID = beforeID.toLowerCase();
		AddArrayComponent(component, arrayName);
	}
	
	public ABMComponent Component(String componentId) {		
		
		ABMComponent obj = Components.getOrDefault(componentId.toLowerCase(), null);
		if (obj==null) {
			if (Page.ShowDebugFlush) {
				for (Entry<String,ABMComponent> comp : Components.entrySet()) {
					BA.Log("Cell components available = " + comp.getKey());
				}
				BA.Log("Cell No component found with id=" + componentId);
			}
			return null;
		}
	
		return ABMaterial.GetComponent(Page, obj, componentId, obj.ParentString);
	}
	
	public ABMComponent ComponentWriteOnly(String componentId) {
		
		ABMComponent obj = Components.getOrDefault(componentId.toLowerCase(), null);
		if (obj==null) {
			if (Page.ShowDebugFlush) {
				for (Entry<String,ABMComponent> comp : Components.entrySet()) {
					BA.Log("Cell components available = " + comp.getKey());
				}
				BA.Log("Cell No component found with id=" + componentId);
			}
			return null;
		}
	
		return ABMaterial.CastABMComponent(obj);
	}
	
	public void RemoveComponent(String componentId) {
		
		ABMComponent comp = (ABMComponent) Components.getOrDefault(componentId.toLowerCase(), null);
		if (comp==null) {			
			return;
		}
		if (comp.EventHandler!=null) {				
			Page.EventHandlers.remove(comp.ID.toLowerCase());
		}
		if (Parent==null) {
			Page.AllComponents.remove(componentId.toLowerCase());
		} else {
			Parent.AllComponents.remove(componentId.toLowerCase());
		}
		ABMaterial.RemoveComponent(Page, comp);
		comp.RemoveMe();
		comp.CleanUp();
		
		Components.remove(componentId.toLowerCase());
		if (!comp.mB4JSUniqueKey.equals("")) {			
			Page.B4JSUniqueComponents.remove(comp.mB4JSUniqueKey.toLowerCase());
			
		}
		comp=null;
		if (Components.size()==0) {
			mCurrentMultiContainer="";
		}
		
	}
	
	public void SetOffsetSize(int offsetSmall, int offsetMedium, int offsetLarge, int sizeSmall, int sizeMedium, int sizeLarge) {
		this.OffsetSmall = offsetSmall;
		this.OffsetMedium = offsetMedium;
		this.OffsetLarge = offsetLarge;
		this.SizeSmall = sizeSmall;
		this.SizeMedium = sizeMedium;
		this.SizeLarge = sizeLarge;		
	}
	
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Cells.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Cells.get(themeName.toLowerCase()).Clone();				
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
		String fixedBottom="";
		if (FixedHeightFromBottom>0) {
			fixedBottom=" data-fixedbottom=\"" + FixedHeightFromBottom + "\" ";
		}
		String R="";
		String C="";
		String[] spl = ID.split("C");
		R = spl[0].substring(1);
		C = spl[1];
		String clickObj="";
		if (ClickableTarget.equals("")) {
			clickObj="Page";
		} else {
			clickObj=ClickableTarget; //ParentString.substring(0, ParentString.length()-1);
		}
		String Masonry="";
		if (Theme.MasonryColumnsSmall>-9999) {
			Masonry= " data-columns ";			
		}
		
		switch (CellType) {
		case "Fixed": // TODO
			
			break;			
		case "Fluid": // TODO
			
			break;
		default:
			String B4JSData="";
			if (!mB4JSUniqueKey.equals("")) {
				B4JSData = " data-b4js=\"" + mB4JSUniqueKey + "\" data-b4jsextra=\"" + HasHover + "\" ";
			} else {
				B4JSData = " data-b4js=\"\" data-b4jsextra=\"" + HasHover + "\" ";
			}
			if (DragDropGroupName.equals("")) {
				s.append("<div name=\"" + CellInfo + "\" " + B4JSData + Masonry + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"" + BuildClass() + "\" style=\"" + BuildStyle() + overflowY + "\" " + fixedBottom + " data-click=\"" + clickObj + "," + R + ";" + C + "\">\n");
			} else {
				s.append("<div name=\"" + CellInfo + "\" " + B4JSData + Masonry + "id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" data-ddgn=\"" + DragDropGroupName + "\" data-dgn=\"" + DragDropName + "\" class=\"" + BuildClass() + "\" style=\"" + BuildStyle() + overflowY + "\" " + fixedBottom + " data-click=\"" + clickObj + "," + R + ";" + C + "\">\n");
			}
		}
		if (!HelpTip.equals("")) {
			s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-helptip\" class=\"help-tip help-tip" + Theme.ThemeName.toLowerCase() + "\"><p>" + BuildBodyInfo(HelpTip, false) + "</p></div>");
		}
		s.append(BuildBody());
		s.append("</div>");	
		IsBuild=true;
		HelpTipIsDirty=false;
		return s.toString();
	}
	
	@Hide
	protected String BuildClass() {		
		StringBuilder s = new StringBuilder();
		String rtl="";
		if (this.getRightToLeft()) {
			rtl = " abmrtl right ";
		}
		if (Theme.MasonryColumnsSmall>-9999) {
			s.append("mason" + Theme.ThemeName.toLowerCase() + " ");
		} else {
			if (ForReport) {
				s.append(" s" + SizeSmall + " m" + SizeMedium + " l" + SizeLarge + " offset-s" + OffsetSmall + " offset-m" + OffsetMedium + " offset-l" + OffsetLarge + " " + ABMaterial.GetColorStr(Theme.BackColor,Theme.BackColorIntensity + " " + mVisibility + " " + Theme.ZDepth + " " + rtl + Theme.Align, ""));
			} else {
				s.append("col s" + SizeSmall + " m" + SizeMedium + " l" + SizeLarge + " offset-s" + OffsetSmall + " offset-m" + OffsetMedium + " offset-l" + OffsetLarge + " " + ABMaterial.GetColorStr(Theme.BackColor,Theme.BackColorIntensity + " " + mVisibility + " " + Theme.ZDepth + " " + rtl + Theme.Align, ""));
			}
		}
		if (Theme.VerticalAlign) {
			String vWrap=" valign-wrapper ";
			switch (Theme.Align) {
			case ABMaterial.CELL_ALIGN_CENTER:
				vWrap=" vhcalign-wrapper ";
				break;
			case ABMaterial.CELL_ALIGN_RIGHT:
				vWrap=" vhralign-wrapper ";
				break;
			}
			s.append(vWrap);
		}
		if (Theme.Clickable || mZABMode) {
			s.append(" cclick " + ABMaterial.GetWavesEffect(Theme.ClickableWavesEffect, Theme.ClickableWavesCircle));
		}
		if (mZABMode) {
			s.append("  zabcell ");
		}	
		
		s.append(ExtraClasses + " ");
		s.append(mIsPrintableClass);
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
		
		if (FixedHeight>0) {
			s.append(" height:" + FixedHeight + "px; ");
			if (!DragDropName.equals("")) {
				s.append(" max-height:" + FixedHeight + "px; ");
			}
		}
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
		if (DragDropMinHeight>0) {
			s.append(" min-height: " + DragDropMinHeight + "px;");
		}
		s.append(ExtraStyle);
		return s.toString();
	}
	
	@Hide
	protected String BuildBody() {
		StringBuilder s = new StringBuilder();
		List<ABMComponent> comps = new ArrayList<ABMComponent>();
		for (Entry<String, ABMComponent> comp : Components.entrySet()) {
			if (comp.getValue().beforeID.equals("")) {
				comps.add(comp.getValue());
			}
			
		}		
		for (Entry<String, ABMComponent> comp : Components.entrySet()) {
			String beforeID=comp.getValue().beforeID;
			if (!beforeID.equals("")) {				
				for (int j=0;j<comps.size();j++) {
					if (comps.get(j).RootID().equals(beforeID)) {
						comps.add(j, comp.getValue());
						break;
					}
				}				
			}			
		}
		for (int j=0;j<comps.size();j++) {
			if (Theme.MasonryColumnsSmall>-9999) {
				s.append("<div>" + comps.get(j).Build() + "</div>");
			} else {
				s.append(comps.get(j).Build());
			}
		}
		return s.toString();
	}
	
	
	public boolean Refresh() {
		return RefreshInternalExtra(false, true, true);
	}
	
	public boolean RefreshOnlyNew() {
		return RefreshInternalExtra(true, true, true);
	}
	
	public boolean RefreshFlush(boolean doFlush ) {
		return RefreshInternalExtra(false, false, doFlush);
	}	
	
	protected void CheckIfInBrowser(StringBuilder ToCheckSB) {		
		String compIDExtra="";
		compIDExtra = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();		
		ToCheckSB.append("if (document.getElementById('" + compIDExtra + "')==null) {notExists+=';" + compIDExtra + ";'} else {Exists+=';" + compIDExtra + ";'};");
		
		for(Iterator<Map.Entry<String, ABMComponent>>it=Components.entrySet().iterator();it.hasNext();){
			Map.Entry<String, ABMComponent> comp = it.next();
			if (!comp.getValue().HadInBrowserCheck) {
				compIDExtra = comp.getValue().ParentString + comp.getValue().RootID();
				switch (comp.getValue().Type) {
				case ABMaterial.UITYPE_UPLOAD:					
					ToCheckSB.append("if (document.getElementById('upload" + compIDExtra + "')==null) {notExists+=';upload" + compIDExtra + ";'} else {Exists+=';upload" + compIDExtra + ";'};");
					break;
				default:
					ToCheckSB.append("if (document.getElementById('" + compIDExtra + "')==null) {notExists+=';" + compIDExtra + ";'} else {Exists+=';" + compIDExtra + ";'};");
				}
				
				
			}
			switch (comp.getValue().Type) {								
			case ABMaterial.UITYPE_ABMCONTAINER:
				ABMContainer cont = (ABMContainer) comp.getValue();
				cont.CheckIfInBrowser(ToCheckSB);
				break;
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
		
		for (Entry<String,ABMComponent> entry: Components.entrySet()) {
			ABMComponent comp = entry.getValue();
			if (comp!=null) {	
				if (ToCheck.contains(";" + entry.getKey() + ";")) {												
					comp.IsBuild = false;
					comp.HadFirstRun = false;							
					ret = false;
				} 
				switch (comp.Type) {								
				case ABMaterial.UITYPE_ABMCONTAINER:
					ABMContainer cont = (ABMContainer) comp;
					if (!cont.ResetNotInBrowser(ToCheck, OKCheck)) {
						ret = false;
					}
					break;			
				}					
				if (OKCheck.contains(";" + entry.getKey() + ";")) {												
					comp.IsBuild = true;
					comp.HadFirstRun = true;							
					//ret = false;
				} 
				switch (comp.Type) {								
				case ABMaterial.UITYPE_ABMCONTAINER:
					ABMContainer cont = (ABMContainer) comp;
					if (!cont.ResetNotInBrowser(ToCheck, OKCheck)) {
						//ret = false;
					}
					break;			
				}					
			}
		}
		
		return ret;
	}
		
	protected void ResizeFixedBottomHeights() {
		if (this.FixedHeightFromBottom>0) {
			StringBuilder sh = new StringBuilder();
			
			sh.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').height(function() {"); 
			sh.append("return window.innerHeight - $(this).offset().top - parseInt($(this).css('padding-top')) - parseInt($(this).css('padding-bottom')) - " + FixedHeightFromBottom + ";");
			sh.append("});");
			
			Page.ws.Eval(sh.toString(), null);
		}
	}
	
	/**
	 * This is a limited version of Refresh() which will only refresh the cell properties, NOT its contents.
	 */
	public void RefreshNoContents(boolean DoFlush) {
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		j.SetProp("class", BuildClass());
		j.SetProp("style", BuildStyle());
		
		StringBuilder ss = new StringBuilder();
		
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
		if (FixedHeightFromBottom>0) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-fixedbottom",  ""+FixedHeightFromBottom);
		}
		if (Theme.MasonryColumnsSmall!=-9999) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-columns",  "");			
		}
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-b4js", B4JSData1);
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-b4jsextra", B4JSData2);
		
		if (this.FixedHeightFromBottom>0) {
			StringBuilder sh = new StringBuilder();
			
			sh.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').height(function() {"); 
			sh.append("return window.innerHeight - $(this).offset().top - parseInt($(this).css('padding-top')) - parseInt($(this).css('padding-bottom')) - " + FixedHeightFromBottom + ";");
			sh.append("});");
			
			Page.ws.Eval(sh.toString(), null);
		}
		
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-dgn", DragDropName);
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-ddgn", DragDropGroupName);
		
		if (NeedsRefreshMatchHeight) {
			
			ss.append("$(\"[data-ddgn='" + DragDropGroupName + "']\").matchHeight({remove: true});");
			ss.append("$(\"[data-ddgn='" + DragDropGroupName + "']\").matchHeight({minHeight: " + DragDropMinHeight + "});");
			NeedsRefreshMatchHeight=false;
		}
				
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("RefreshNoContents");}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
	}
	
	protected void SetEventHandlerParent(Object parentEventHandler) {
		for(Iterator<Map.Entry<String, ABMComponent>>it=Components.entrySet().iterator();it.hasNext();){
			Map.Entry<String, ABMComponent> comp = it.next();
			comp.getValue().SetEventHandlerParent(parentEventHandler);
		}
	}
	
	protected boolean RefreshInternalExtra(boolean onlyNew, boolean DoTheChecks, boolean DoFlush) {
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		if (j!=null) {
			j.SetProp("class", BuildClass());
			j.SetProp("style", BuildStyle() + overflowY);
			if (this.FixedHeightFromBottom>0) {
				StringBuilder sh = new StringBuilder();
				
				sh.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').height(function() {"); 
				sh.append("return window.innerHeight - $(this).offset().top - parseInt($(this).css('padding-top')) - parseInt($(this).css('padding-bottom')) - " + FixedHeightFromBottom + ";");
				sh.append("});");
				
				Page.ws.Eval(sh.toString(), null);
			}
			
		}
		
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
		if (FixedHeightFromBottom>0) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-fixedbottom",  ""+FixedHeightFromBottom);
		}
		if (Theme.MasonryColumnsSmall!=-9999) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-columns",  "");			
		}
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-b4js", B4JSData1);
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-b4jsextra", B4JSData2);
		
		if (HelpTipIsDirty) {
			if (HelpTip.equals("")) {
				ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-helptip");
			} else {
				ABMaterial.ReplaceMyInnerHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-helptip", "<p>" + BuildBodyInfo(HelpTip, false) + "</p>");
			}
		}
		HelpTipIsDirty=false;
		
		int Counter=0;
		String prevID="";
		
		boolean ret = true;
		String compIDExtra="";
		
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-dgn", DragDropName);
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-ddgn", DragDropGroupName);
		
		StringBuilder ToCheckSB = new StringBuilder();		
		String ToCheck="";
		String OKCheck="";
		if (DoTheChecks && Page.ws!=null) {
			for(Iterator<Map.Entry<String, ABMComponent>>it=Components.entrySet().iterator();it.hasNext();){
			    Map.Entry<String, ABMComponent> comp = it.next();
			    if (!comp.getValue().HadInBrowserCheck) {
			    	compIDExtra = comp.getValue().ParentString + comp.getValue().RootID();			    	
			    	switch (comp.getValue().Type) {
			    	case ABMaterial.UITYPE_UPLOAD:					
						ToCheckSB.append("if (document.getElementById('upload" + compIDExtra + "')==null) {notExists+=';upload" + compIDExtra + ";'} else {Exists+=';upload" + compIDExtra + ";'};");
						break;
					default:
						ToCheckSB.append("if (document.getElementById('" + compIDExtra + "')==null) {notExists+=';" + compIDExtra + ";'} else {Exists+=';" + compIDExtra + ";'};");
					}
			    }
			    switch (comp.getValue().Type) {								
				case ABMaterial.UITYPE_ABMCONTAINER:
					ABMContainer cont = (ABMContainer) comp.getValue();
					cont.CheckIfInBrowser(ToCheckSB);
					break;
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
			
			for (Entry<String,ABMComponent> entry: Components.entrySet()) {
				ABMComponent comp = entry.getValue();
				if (comp!=null) {	
					if (ToCheck.contains(";" + entry.getKey() + ";")) {												
						comp.IsBuild = false;
						comp.HadFirstRun = false;							
						ret = false;
					}
					switch (comp.Type) {								
					case ABMaterial.UITYPE_ABMCONTAINER:
						ABMContainer cont = (ABMContainer) comp;
						cont.ResetNotInBrowser(ToCheck, OKCheck);
						break;
					}						
					if (OKCheck.contains(";" + entry.getKey() + ";")) {												
						comp.IsBuild = true;
						comp.HadFirstRun = true;							
						//ret = false;
					} 
					switch (comp.Type) {								
					case ABMaterial.UITYPE_ABMCONTAINER:
						ABMContainer cont = (ABMContainer) comp;
						if (!cont.ResetNotInBrowser(ToCheck, OKCheck)) {
							//ret = false;
						}
						break;			
					}						
				}
			}			
		} 			
				
		for(Iterator<Map.Entry<String, ABMComponent>>it=Components.entrySet().iterator();it.hasNext();){
		    Map.Entry<String, ABMComponent> comp = it.next();
		   if (comp.getValue().IsBuild) {
				if (!onlyNew) {
					switch (comp.getValue().Type) {								
					case ABMaterial.UITYPE_ABMCONTAINER:
						ABMContainer cont = (ABMContainer) comp.getValue();
						if (!cont.HadFirstRun) {
							cont.RunAllFirstRunsInternal(false);
							String s = cont.RunInitialAnimation();
							if (!s.equals("")) {
								if (Page.ws.getOpen())
								Page.ws.Eval(s, null);								
							}							
						}
						cont.RefreshInternalExtra(onlyNew, DoTheChecks,false);
						break;								
					case ABMaterial.UITYPE_FLEXWALL:
						ABMFlexWall wall = (ABMFlexWall) comp.getValue();	
						if (!wall.HadFirstRun) {												
							wall.RunAllFirstRunsInternal(false);													
						}
						wall.RefreshInternalExtra(onlyNew, DoTheChecks, false);
						break;						
					case ABMaterial.UITYPE_TABEL:
						ABMTable table = (ABMTable) comp.getValue();
						if (!table.HadFirstRun) {												
							table.FirstRunInternal(false);														
						}						
						table.RefreshInternal(false);
						break;
					case ABMaterial.UITYPE_CUSTOMCOMPONENT:
						ABMCustomComponent ccomp = (ABMCustomComponent) comp.getValue();
						if (!ccomp.HadFirstRun) {												
							ccomp.RunAllFirstRuns();													
						}						
						ccomp.RefreshInternal(false);
						break;
					case ABMaterial.UITYPE_TABS:
						ABMTabs tabs = (ABMTabs) comp.getValue();
						if (!tabs.HadFirstRun) {												
							tabs.RunAllFirstRuns();													
						}
						tabs.RefreshInternal(false);
						break;
					default:
						if (!comp.getValue().HadFirstRun) {
							comp.getValue().FirstRun();											
						}
						comp.getValue().RefreshInternal(false);	
					}
					
				}
				prevID = comp.getValue().RootID();
			} else {	
				if (Counter==0) {
					if (Theme.MasonryColumnsSmall>-9999) {
						ABMaterial.AddHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "<div>" + comp.getValue().Build() + "</div>");
					} else {
						ABMaterial.AddHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), comp.getValue().Build());
					}
					prevID = comp.getValue().RootID();
				} else {
					if (comp.getValue().beforeID.equals("")) {	
						if (Theme.MasonryColumnsSmall>-9999) {
							ABMaterial.InsertHTMLAfterParent(Page, comp.getValue().ParentString + prevID, "<div>" + comp.getValue().Build() + "</div>");							
						} else {
							ABMaterial.InsertHTMLAfter(Page, comp.getValue().ParentString + prevID, comp.getValue().Build());
						}
						prevID = comp.getValue().RootID();
					} else {				
						if (Theme.MasonryColumnsSmall>-9999) {
							ABMaterial.InsertHTMLBeforeParent(Page, comp.getValue().ParentString + ArrayName.toLowerCase() + comp.getValue().beforeID, "<div>" + comp.getValue().Build() + "</div>");
						} else {
							ABMaterial.InsertHTMLBefore(Page, comp.getValue().ParentString + ArrayName.toLowerCase() + comp.getValue().beforeID, comp.getValue().Build());
						}
					}
				}		
				
				switch (comp.getValue().Type) {
					case ABMaterial.UITYPE_ABMCONTAINER:
						ABMContainer cont = (ABMContainer) comp.getValue();
						cont.RunAllFirstRunsInternal(false);
						String s = cont.RunInitialAnimation();
						if (!s.equals("")) {
							Page.ws.Eval(s, null);							
						}
						break;
					case ABMaterial.UITYPE_FLEXWALL:
						ABMFlexWall wall = (ABMFlexWall) comp.getValue();						
						wall.RunAllFirstRunsInternal(false);
						break;
					case ABMaterial.UITYPE_TABEL:						
						ABMTable table = (ABMTable) comp.getValue();						
						table.FirstRunInternal(false);						
						break;
					case ABMaterial.UITYPE_CUSTOMCOMPONENT:
						ABMCustomComponent ccomp = (ABMCustomComponent) comp.getValue();																	
						ccomp.RunAllFirstRuns();
						break;
					case ABMaterial.UITYPE_TABS:
						ABMTabs tabs = (ABMTabs) comp.getValue();																	
						tabs.RunAllFirstRuns();
						break;
					default:
						comp.getValue().RunAllFirstRuns();
				}
				
			}			
			
			Counter++;
		}	
		
		if (NeedsRefreshMatchHeight) {
			StringBuilder ss = new StringBuilder();
			ss.append("$(\"[data-ddgn='" + DragDropGroupName + "']\").matchHeight({remove: true});");
			ss.append("$(\"[data-ddgn='" + DragDropGroupName + "']\").matchHeight({minHeight: " + DragDropMinHeight + "});");
			NeedsRefreshMatchHeight=false;
		}
		if (Theme.MasonryColumnsSmall>-9999) {
			Page.ws.Eval("salvattore.rescanMediaQueries();", null);
			
			for(Iterator<Map.Entry<String, ABMComponent>>it=Components.entrySet().iterator();it.hasNext();){
			    Map.Entry<String, ABMComponent> comp = it.next();
			    comp.getValue().RefreshInternal(false);
			}
			
			
			
		}
		
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Cell RefreshInternal after " + this.CellInfo);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
		
		
		return ret;
        
	}
	
	protected void ResetSVGs() {
		for(Iterator<Map.Entry<String, ABMComponent>>it=Components.entrySet().iterator();it.hasNext();){
		    Map.Entry<String, ABMComponent> comp = it.next();
		    switch (comp.getValue().Type) {
			case ABMaterial.UITYPE_ABMCONTAINER:
				((ABMContainer) comp.getValue()).ResetSVGs();
			case ABMaterial.UITYPE_SVGSURFACE:
				((ABMSVGSurface) comp.getValue()).Reset();
		    }
		}
	}
	
	
	@Hide
	protected void Prepare() {
		IsBuild=true;
		for (Entry<String, ABMComponent> comp : Components.entrySet()) {
			comp.getValue().Prepare();	
		}		
	}	
	
	protected void RunAllFirstRuns() {
		RunAllFirstRunsInternal(true);
		FirstRun();
	}
	
	@Hide
	protected void RunAllFirstRunsInternal(boolean DoFlush) {		
		for (Entry<String, ABMComponent> comp : Components.entrySet()) {
			switch (comp.getValue().Type) {
			case ABMaterial.UITYPE_ABMCONTAINER:
				ABMContainer cont = (ABMContainer) comp.getValue();
				cont.RunAllFirstRunsInternal(false);
				String s = cont.RunInitialAnimation();
				if (!s.equals("")) {
					Page.ws.Eval(s, null);							
				}
				break;
			case ABMaterial.UITYPE_FLEXWALL:
				ABMFlexWall wall = (ABMFlexWall) comp.getValue();						
				wall.RunAllFirstRunsInternal(false);
				break;
			case ABMaterial.UITYPE_TABEL:						
				ABMTable table = (ABMTable) comp.getValue();						
				table.FirstRunInternal(false);						
				break;
			case ABMaterial.UITYPE_CUSTOMCOMPONENT:
				ABMCustomComponent ccomp = (ABMCustomComponent) comp.getValue();																	
				ccomp.RunAllFirstRuns();
				break;
			case ABMaterial.UITYPE_TABS:
				ABMTabs tabs = (ABMTabs) comp.getValue();						
				tabs.RunAllFirstRuns();
				break;
			default:
				comp.getValue().RunAllFirstRuns();
			}
		}
		FirstRun();
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Cell RunAllFirstRunsInternal");}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
		FirstRun();
	}	
	
	
	protected void FirstRun() {
		// dummy
	}
	
	protected ABMCell Clone() {
		ABMCell c = new ABMCell();
		c.ID = ID;
		c.OffsetLarge = OffsetLarge;
		c.OffsetMedium = OffsetMedium;
		c.OffsetSmall = OffsetSmall;
		c.Page = Page;
		c.RowId = RowId;
		c.SizeLarge = SizeLarge;
		c.SizeMedium = SizeMedium;
		c.SizeSmall = SizeSmall;
		c.Theme = Theme.Clone();
		c.Type = Type;
		c.mVisibility = mVisibility;
		c.PaddingLeft=PaddingLeft;
		c.PaddingRight=PaddingRight;
		c.ArrayName=ArrayName;
		c.MarginBottom=MarginBottom;
		c.MarginTop=MarginTop;
		c.ClickableTarget = ClickableTarget; 
		c.DragDropName = DragDropName;
		c.MarginLeft=MarginLeft;
		c.MarginRight=MarginRight;
		c.PaddingTop=PaddingTop;
		c.PaddingBottom=PaddingBottom;
		c.mIsPrintableClass=mIsPrintableClass;
		c.mZABMode = mZABMode;
		for (Map.Entry<String, ABMComponent> entry : Components.entrySet()) {
			c.Components.put(entry.getKey(), entry.getValue().Clone());
		}
		c.Parent=Parent;
		return c;
	}
	
	protected String BuildBodyInfo(String text, boolean OnlyNBSP) {
		StringBuilder s = new StringBuilder();	
		
		String v = ABMaterial.HTMLConv().htmlEscape(text, Page.PageCharset);
		if (OnlyNBSP) {
			v=v.replace("{NBSP}", " ");
		} else {
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
				default:
					repl = "<i style=\"color: " + IconColor + "\" class=\"material-icons\">" + IconName + "</i>";
				}
				v=v.replace(vv,repl );
				start = v.indexOf("{IC:");
			}
		}
		s.append(v);
		
		return s.toString();
	}
		
	protected ABMComponent RemoveComponentForMove(String componentId) {
		if (!Components.containsKey(componentId.toLowerCase())) {
			return null;
		}
					
		if (Parent==null) {
			Page.AllComponents.remove(componentId.toLowerCase());
		} else {
			Parent.AllComponents.remove(componentId.toLowerCase());
		}
				
		ABMComponent comp = (ABMComponent) Components.remove(componentId.toLowerCase());
		return comp;		
	}
	
	
	protected void AddComponentFromMove(ABMComponent beforeComponent, ABMComponent component) {
		if (beforeComponent==null) {
			if (Components.getOrDefault(component.ArrayName.toLowerCase() + component.ID.toLowerCase(), null)==null && Components.getOrDefault("upload" + component.ArrayName.toLowerCase() + component.ID.toLowerCase(), null)==null) {
				component.CellId = ID;
				component.RowId = RowId;
				component.mBelongsToDropZone = DragDropName; 
				component.CellAlign = Theme.Align;
			
				String extra = ABMaterial.AddComponent(Page, component);	
				
				if (Parent==null) {
					Page.AllComponents.put(extra+component.ArrayName.toLowerCase()+component.ID.toLowerCase(), new ABMRowCell(component.RowId, component.CellId));
				} else {
					Parent.AllComponents.put(extra+component.ArrayName.toLowerCase()+component.ID.toLowerCase(), new ABMRowCell(component.RowId, component.CellId));
				}
				Components.put(extra + component.ArrayName.toLowerCase() + component.ID.toLowerCase(), component);				
			}
		} else {
			if (Components.getOrDefault(component.ArrayName.toLowerCase() + component.ID.toLowerCase(), null)==null && Components.getOrDefault("upload" + component.ArrayName.toLowerCase() + component.ID.toLowerCase(), null)==null) {
				component.beforeID = beforeComponent.RootID();
				component.CellId = ID;
				component.RowId = RowId;
				component.mBelongsToDropZone = DragDropName; 
				component.CellAlign = Theme.Align;
				
				String extra = ABMaterial.AddComponent(Page, component);
				
				if (Parent==null) {
					Page.AllComponents.put(extra+component.ArrayName.toLowerCase()+component.ID.toLowerCase(), new ABMRowCell(component.RowId, component.CellId));
				} else {
					Parent.AllComponents.put(extra+component.ArrayName.toLowerCase()+component.ID.toLowerCase(), new ABMRowCell(component.RowId, component.CellId));
				}				
				
				List<Entry<String, ABMComponent>> rest = new ArrayList<Entry<String, ABMComponent>>();
				boolean CanAdd=false;
				for (Entry<String, ABMComponent> entry : Components.entrySet()) {
					  if (!entry.getValue().DragDropIsHeader) {
						  if (entry.getValue().ID.equalsIgnoreCase(beforeComponent.ID)) {
							  rest.add(entry);
							  CanAdd=true;
						  } else {
							  if (CanAdd) {
								  rest.add(entry);
							  }
						  }
					  }
				}
				Components.put(extra + component.ArrayName.toLowerCase() + component.ID.toLowerCase(), component);
				for (int j = 0; j < rest.size(); j++) {
				    Entry<String, ABMComponent> entry = rest.get(j);
				    Components.remove(entry.getKey());
				    Components.put(entry.getKey(), entry.getValue());
				}
				
			}
		}		
	}
	
	protected void GetAllComponentsDebug() {
		for (Entry<String, ABMComponent> comp : Components.entrySet()) {
			BA.Log(comp.getValue().GetDebugID());
		}
		
	}	
	
}
