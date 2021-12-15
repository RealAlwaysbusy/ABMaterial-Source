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
@ShortName("ABMCanvas")
@Events(values={"CanvasDown(x as int, y as int)","CanvasUp(x as int, y as int)","ObjectDown(objectId as String)","ObjectUp(objectId as String)","ObjectClicked(objectId as String)"})
public class ABMCanvas extends ABMComponent{
	private static final long serialVersionUID = -7266982245583171919L;
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	protected transient Map<String, ABMCanvasObject> Objects = new LinkedHashMap<String, ABMCanvasObject>();
	protected int Width=0;
	protected int Height=0;
	protected transient Map<String, String> CanvasCommands = new LinkedHashMap<String, String>();
	protected String BackColor=ABMaterial.COLOR_TRANSPARENT;
	protected String BackColorIntensity=ABMaterial.INTENSITY_NORMAL;
	protected boolean ScaleToFit=false;
	private boolean internalIsInitialized=false;
	public String UploadHandler="abmuploadhandler";

	public void Initialize(ABMPage page, String id, String backColor, String backColorIntensity, int widthPx, int heightPx,  boolean scaleToFit) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_CANVAS;	
		this.BackColor = backColor;
		this.BackColorIntensity=backColorIntensity;
		this.ScaleToFit=scaleToFit;
		this.Width = widthPx;
		this.Height = heightPx;
		IsInitialized=true;
	}	
	
	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase();
	}
				
	public void AddObject(ABMCanvasObject obj) {
		obj.MyCanvas=this;
		Objects.put(obj.ID.toLowerCase(), obj);
		CanvasCommands.put(obj.ID.toLowerCase(), "0");
	}
	
	public void RemoveObject(String objectId) {
		ABMCanvasObject obj = Objects.getOrDefault(objectId.toLowerCase(), null);
		if (obj==null) {
			BA.Log("No object found with id " + objectId);
			return;
		}
		obj.CleanUp();
		CanvasCommands.put(obj.ID.toLowerCase(), "2");
	}
	
	public ABMCanvasObject GetObject(String objectId) {
		ABMCanvasObject obj = Objects.getOrDefault(objectId.toLowerCase(), null);
		if (obj==null) {
			BA.Log("No object found with id " + objectId);
		}
		return obj;
	}
	
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}		
	
	public void SetTooltip(String text, String position, int delay) {
		this.ToolTipText = text;
		this.ToolTipPosition = position;
		this.ToolTipDelay = delay;			
	}	
	
	@Override
	protected void CleanUp() {
		for (Entry<String, ABMCanvasObject> comp : Objects.entrySet()) {
			comp.getValue().CleanUp();
		}
		Objects.clear();
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

	
	public void GetDrawingURI(String fileName) {
		ABMaterial.GetDrawingURICanvas(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), fileName, UploadHandler.toLowerCase());				
	}	

	protected String BuildJavaScript() {
		StringBuilder s = new StringBuilder();
		String tmpVar = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		tmpVar = tmpVar.replace("-", "_");
		s.append("var theCanvas" + tmpVar + "=document.getElementById(\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\");");
		s.append("var context" + tmpVar + "=theCanvas" + tmpVar + ".getContext(\"2d\");");
		s.append("cans['" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "']=new ABMCanvas(theCanvas" + tmpVar + ",context" + tmpVar + ", '" + ABMaterial.GetColorStrMap(BackColor, BackColorIntensity) + "'," + ScaleToFit + ");");
		s.append("cans['" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "'].drawcanvas();");
		internalIsInitialized=true;
		return s.toString();
	}
	
	
	protected void CheckIfInBrowser(StringBuilder ToCheckSB) {		
		String compIDExtra="";		
		if (HadInBrowserCheck) {				
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
		if (!internalIsInitialized) return;
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
		
		ABMaterial.CanvasRefresh(Page, this, true);	
		CanvasCommands.clear();
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Canvas Refresh: " + ID);}
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
		String cl = "";
		if (ScaleToFit) {
			cl = cl + " canvastofit ";
		}
		cl = cl + mIsPrintableClass + mIsOnlyForPrintClass;
		cl = cl + " " + mVisibility + " ";
		s.append("<canvas class=\"" + super.BuildExtraClasses() + cl + "\" id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" width=\"" + Width + "\" height=\"" + Height + "\">");
		s.append("</canvas>");
		IsBuild=true;
		return s.toString();
	}
	
	@Hide
	protected String BuildClass() {			
		return "";
	}
	
	@Hide
	protected String BuildBody() {
		return "";
	}
	
	@Hide
	@Override
	protected void Prepare() {	
		this.IsBuild = true;		
		internalIsInitialized=true;		
	}
	
	
	@Override
	protected ABMComponent Clone() {
		ABMDivider c = new ABMDivider();
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
