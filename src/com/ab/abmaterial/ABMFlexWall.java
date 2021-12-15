package com.ab.abmaterial;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")  
@ShortName("ABMFlexWall")
public class ABMFlexWall extends ABMComponent{
	private static final long serialVersionUID = 1145216518430975916L;
	protected transient Map<String,ABMImage> AllImages = new LinkedHashMap<String, ABMImage>();
	protected int RowHeight=200;
	
	public boolean IsTextSelectable=true;
		
	public void Initialize(ABMPage page, String id, int rowHeightPx) {
		this.ID = id;
		this.Page = page;
		this.Type = ABMaterial.UITYPE_FLEXWALL;		
		this.RowHeight = rowHeightPx;
		IsInitialized=true;
	}
	
	public void AddImage(ABMImage image, int widthPx, int heightPx) {
		image.CellId = ID;
		image.RowId = RowId;	
		image.ParentString = this.ParentString;
		image.FlexWallExtra = "<div class=\"flexwallitem\" data-w=\"" + widthPx + "\" data-h=\"" + heightPx  + "\">";
		
		String extra = ABMaterial.AddComponent(Page, image);		
		Page.AllComponents.put(extra+image.ID.toLowerCase(), new ABMRowCell(image.RowId, image.CellId));
		AllImages.put(extra+image.ID.toLowerCase(), image);
	}
	
	public void AddArrayImage(ABMImage image, int widthPx, int heightPx, String arrayName) {		
		image.CellId = ID;
		image.RowId = RowId;
		image.AddArrayName(arrayName);
		image.ParentString = this.ParentString;
		image.FlexWallExtra = "<div class=\"flexwallitem\" data-w=\"" + widthPx + "\" data-h=\"" + heightPx  + "\">";
		
		String extra = ABMaterial.AddComponent(Page, image);
		Page.AllComponents.put(extra+image.ArrayName.toLowerCase()+image.ID.toLowerCase(), new ABMRowCell(image.RowId, image.CellId));
		AllImages.put(extra + image.ArrayName.toLowerCase() + image.ID.toLowerCase(), image);
	}
	
	public void RemoveImage(String componentId) {
		ABMImage image = AllImages.getOrDefault(componentId.toLowerCase(), null);
		if (image==null) {			
			return;
		}
			
		Page.AllComponents.remove(componentId.toLowerCase());
		ABMaterial.RemoveComponent(Page, image);
		image.RemoveMe();
		image.CleanUp();
		
		AllImages.remove(componentId.toLowerCase());
		image=null;
	}
	
	public ABMImage Image(String componentId) {		
		ABMImage obj = AllImages.getOrDefault(componentId.toLowerCase(), null);
		if (obj==null) {
			for (Entry<String,ABMImage> comp : AllImages.entrySet()) {
				BA.Log("Cell components available = " + comp.getKey());
			}
			BA.Log("Cell No component found with id=" + componentId);
			return null;
		}
	
		return (ABMImage) ABMaterial.GetComponent(Page, obj, componentId, obj.ParentString);
	}
	
	@Override
	protected void ResetTheme() {
		
	}
	
	@Override
	protected String RootID() {
		return ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
	}
	
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
		Map<String, ABMImage> newComps = new LinkedHashMap<String,ABMImage>();
		for (Map.Entry<String, ABMImage> entry : AllImages.entrySet()) {
			ABMImage comp = entry.getValue();
			comp.AddArrayName(arrayName+ID.toLowerCase());	
			newComps.put(entry.getKey(), comp);	
		}
		AllImages.clear();
		AllImages.putAll(newComps);
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
		super.FirstRun();
	}

	protected String BuildJavaScript() {
		StringBuilder s = new StringBuilder();
		String id = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		s.append("$('#" + id + "').flexImages({rowHeight: " + RowHeight + "});");
			
		return s.toString();
	}	
	
	@Override
	public void Refresh() {
		RefreshInternalExtra(false,true, true);
	}
	
	protected void RefreshInternalExtra(boolean onlyNew, boolean DoTheChecks, boolean DoFlush) {
		// TODO ExtraClasses not working		
		super.Refresh();
		//JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		
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
		
		int Counter=0;
		String prevID="";			
		
		for(Iterator<Map.Entry<String, ABMImage>>it=AllImages.entrySet().iterator();it.hasNext();){
		    Map.Entry<String, ABMImage> comp = it.next();
			
			if (comp.getValue().IsBuild) {
				comp.getValue().RefreshInternal(DoFlush);				
			} else {
				if (Counter==0) {
					String bValue = comp.getValue().FlexWallExtra + comp.getValue().Build() + "</div>";
					ABMaterial.AddHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), bValue);							
				} else {					
					String bValue = comp.getValue().FlexWallExtra + comp.getValue().Build() + "</div>";
					ABMaterial.InsertHTMLAfter(Page, comp.getValue().ParentString + prevID, bValue);
				}
				comp.getValue().FirstRun();
			
			}
			prevID = comp.getValue().RootID();
			Counter++;
		}	
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("FlexWall RefreshInternal: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
		FirstRun();
	}	
	
	@Override
	protected String Build() {
		StringBuilder s = new StringBuilder();
		String selectable="";
		if (!IsTextSelectable) {
			selectable = " notselectable ";
		}
		s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"flexwall-images" + selectable + " " + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() + "\" style=\"width: 100%\">");
		s.append(BuildBody());
		s.append("</div>");
		IsBuild=true;
		return s.toString();		
	}
	
	@Hide
	protected String BuildClass() {
		return "";
	}
	
	@Hide
	protected String BuildBody() {
		StringBuilder s = new StringBuilder();
		for(Iterator<Map.Entry<String, ABMImage>>it=AllImages.entrySet().iterator();it.hasNext();){
		    Map.Entry<String, ABMImage> comp = it.next();
		    s.append(comp.getValue().FlexWallExtra + comp.getValue().Build() + "</div>");
		}
		return s.toString();
	}
	
	@Hide
	protected void Prepare() {
		IsBuild=true;
		for (Entry<String, ABMImage> comp : AllImages.entrySet()) {
			comp.getValue().Prepare();	
		}		
	}	
	
	@Hide
	protected void RunAllFirstRuns() {
		RunAllFirstRunsInternal(true);
	}	
	
	protected void RunAllFirstRunsInternal(boolean DoFlush) {
		FirstRun();
		for (Entry<String, ABMImage> comp : AllImages.entrySet()) {
			comp.getValue().RunAllFirstRuns();	
		}		
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("FlexWall RefreshInternal: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		} 
	}
		
	@Override
	protected ABMComponent Clone() {
		ABMFlexWall c = new ABMFlexWall();
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
