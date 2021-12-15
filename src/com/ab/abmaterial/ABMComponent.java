package com.ab.abmaterial;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;
import anywheresoftware.b4j.object.WebSocket.SimpleFuture;

@Author("Alain Bailleul")  
@ShortName("ABMComponent")
public class ABMComponent extends ABMObject {
	private static final long serialVersionUID = -7732776636770660215L;
	protected String ID="";
	protected transient ABMPage Page;
	protected String ArrayName="";
	protected String CellId="";	
	protected String RowId="";
	protected boolean IsBuild=false;
	protected String mVisibility=ABMaterial.VISIBILITY_ALL;
	protected String ParentString="";
	protected String CellAlign=ABMaterial.CELL_ALIGN_LEFT;
	protected boolean TreeNeedsFirstRun=false;
	public transient Object Tag=null;
	protected String mRootID="";
	protected String FlexWallExtra="";
	protected String beforeID="";
	public boolean IsInitialized=false;
	protected boolean HadFirstRun=false;
	
	protected String extra="";
	
	public Object EventHandler=null;
	
	protected JQueryElement JQ=null;
	
	protected String mCursor="";
	
	protected Map<String,Boolean> B4JSClassNames = new LinkedHashMap<String, Boolean>();
	
	protected boolean DragDropIsHeader=false;
	protected String mBelongsToDropZone="";
	
	
	protected String mIsPrintableClass="";
	protected String mIsOnlyForPrintClass=" only-print ";
	
	protected Map<String,String> ExtraClasses = new LinkedHashMap<String,String>();
	
	protected String mB4JSUniqueKey="";
	
	protected boolean GotLastVisibility=false;
	protected boolean IsVisibilityDirty=false;
	
	
	protected boolean GotLastEnabled=false;
	protected boolean IsEnabledDirty=false;
	
	protected SimpleFuture FutureVisibility;
	protected SimpleFuture FutureEnabled;
	
	private int mRightToLeft=0;
	
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
	
	public String getVisibility() {		
		return mVisibility;
	}
	
	public void setVisibility(String visibility) {
		IsVisibilityDirty=true;
		GotLastVisibility=true;
		mVisibility=visibility;		
	}
	
	public void SetVisibilityFlush(String visibility, boolean DoFlush) {
		IsVisibilityDirty=true;
		GotLastVisibility=true;
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
	
	public ABMComponent() {
		
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
	 * Enable/Disable this component from being printed 
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
	
	public boolean getIsPrintable() {
		return mIsPrintableClass.equals("");
	}
	
	public void Rotate(double degrees) {
		if (Page!=null) {
			if (IsBuild==false) {
				Page.Refresh();
			}
			Page.ws.Eval("$('#" + ParentString + RootID() + "').rotate(" + degrees + ", {duration:1,easing: 'linear'});", null);
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Component Rotate: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void RotateAnimated(double degrees, int durationMs, String easing, boolean continious) {
		if (Page!=null) {
			if (IsBuild==false) {
				Page.Refresh();
			}
			Page.ws.Eval("$('#" + ParentString + RootID() + "').rotate(" + degrees + ", {duration:" + durationMs + ",easing: '" + easing + "',continious:" + continious + "});", null);
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Component RotateAnimated: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
			
	public String BelongsToDropZone() {
		return mBelongsToDropZone;
	}
	
	protected void SetPage(ABMPage page) {
		Page = page;
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public String getB4JSID() {
		return "";
	}
	
	public String getID() {
		return ParentString + this.ArrayName.toLowerCase() + this.ID.toLowerCase();
	}	
	
	protected String RootID() {
		return "";
	}
	
	public String GetDebugID() {
		return "Parent='" + ParentString + "',ArrayName='" + this.ArrayName.toLowerCase() + "',ID='" + this.ID.toLowerCase() + "'";
	}
	
	protected void CleanUp() {
		this.Page=null;		
	}
	
	protected void PrepareEvent(String EventName, String B4JSClassName, String B4JSMethod, anywheresoftware.b4a.objects.collections.List Params) {
		if (mB4JSUniqueKey.equals("")) {
			BA.LogError("You cannot use B4JS events before setting the B4JSUniqueKey property!");
			return;
		}
		
		B4JSClassNames.put(B4JSClassName.toLowerCase(), true);
							
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
	 * Cannot be set in BuildPage()! Must be done at runtime. 
	 */
	public void SetCursor(String cursor) {
		mCursor=cursor;		
	}
	
	@Hide
	protected void Prepare() {	
		this.IsBuild = true;
	}
	
	@Hide
	protected void RunAllFirstRuns() {	
		FirstRun();
	}
	
	
	@Hide
	public void Refresh() {
		
	}
		
	/**
	 * You must set DebugConsoleEnable in BuildPage() first!
	 */
	public void DebugConsoleGetHTML() {
		if (!Page.DebugConzole) {
			BA.LogError("You must set DebugConsoleEnable in BuildPage() first!");
			return;
		}
		if (Page.ws!=null) {			
			Page.ws.Eval("conzole.log($('#" + ParentString + RootID() + "').html());", null);			
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("DebugConsoleLog");}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
	}
	
	@Hide
	protected void RefreshInternal(boolean DoFlush) {
		StringBuilder s = new StringBuilder();
		String tmpVar = ParentString + this.ArrayName.toLowerCase() + this.ID.toLowerCase();
		tmpVar = tmpVar.replace("-", "_");
		for (Entry<String, Boolean>entry: B4JSClassNames.entrySet()) {
			s.append("if (!(\"" + tmpVar + "_" + entry.getKey().toLowerCase() + "\" in _b4jsvars)) {");
			s.append("_b4jsvars[\"" + tmpVar + "_" + entry.getKey().toLowerCase() + "\"]=new b4js_" + entry.getKey().toLowerCase() + "();");
			s.append("_b4jsvars[\"" + tmpVar + "_" + entry.getKey().toLowerCase() + "\"].initialize();");
			s.append("}");	
		}			
	}
		
	@Hide
	protected void RemoveMe() {
		
	}
	
	@Hide
	protected void FirstRun() {		
		HadFirstRun=true;
	}
	
	@Hide
	protected String Build() {		
		return "";
	}
	
	@Hide
	protected String BuildExtraClasses() {
		StringBuilder s = new StringBuilder();
		for (Entry<String,String> entry: ExtraClasses.entrySet()) {
			s.append(" " + entry.getValue());
		}
		return s.toString() + " ";
	}
	
	protected void SetEventHandler() {
		if (EventHandler!=null) {
			if (!ArrayName.equals("")) {
				Page.EventHandlers.put(ArrayName.toLowerCase(), EventHandler);
			} else {
				Page.EventHandlers.put(ID.toLowerCase(), EventHandler);
			}
			
						
		}
	}
	
	protected void SetEventHandlerParent(Object parentEventHandler) {
		if (EventHandler==null) {
			EventHandler = parentEventHandler;
		}
		SetEventHandler();
	}
	
	
	protected void AddArrayName(String arrayName) {	
		
	}	
	
	protected void ResetTheme() {
		
	}
	
	protected ABMComponent Clone() {
		return null;		
	}
		
	/**
	 * the is the ONLY HTMLxxx method that also can be used BEFORE the component is added
	 */
	public void HTMLAddClass(String Class) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(getID().toLowerCase());
		Params.Add(Class);
		ExtraClasses.put(Class.toLowerCase(), Class);
		if (IsBuild) {
			Page.ws.RunFunction("AddClass", Params);
		}
	}
	
	/**
	 * Only works AFTER the component has been added and the parent has done a refresh 
	 */
	public void HTMLRemoveClass(String Class) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(getID().toLowerCase());
		Params.Add(Class);
		ExtraClasses.remove(Class.toLowerCase());
		Page.ws.RunFunction("RemoveClass", Params);		
	}
	
	/**
	 * Only works AFTER the component has been added and the parent has done a refresh 
	 */
	public void HTMLReplaceClass(String oldClass, String newClass) {
		HTMLRemoveClass(oldClass);
		HTMLAddClass(newClass);
	}
	
	/**
	 * Only works AFTER the component has been added and the parent has done a refresh 
	 */
	public boolean HTMLHasClass(String cl) {
		String ret = "";
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(getID().toLowerCase());
		Params.Add(cl);		
		SimpleFuture j = Page.ws.RunFunctionWithResult("HasClass", Params);
		if (j!=null) {
			try {
				ret = (String) j.getValue();
			} catch (InterruptedException e) {
				//e.printStackTrace();
			} catch (TimeoutException e) {
				//e.printStackTrace();
			} catch (ExecutionException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		return (ret.equals("true"));			
	}	
}
