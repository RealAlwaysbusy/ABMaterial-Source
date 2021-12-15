package com.ab.abmaterial;

import java.io.IOException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;

@ShortName("ABMParallax")
public class ABMParallax extends ABMComponent {
	private static final long serialVersionUID = 4044200649751155243L;
	public String ImageName="";
	public int Height=500;
	public int ScaledHeightSmall=200;
	public int ScaledHeightMedium=350;
	public int ScaledHeightLarge=500;
	protected ABMContainer Container;
	protected boolean IsScaled=false;
		
	public void Initialize(ABMPage page, String id, String imageName, int height) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_PARALLAX;
		this.ImageName=imageName;
		this.Height = height;	
		IsInitialized=true;
	}
	
	public void InitializeWithContainer(ABMPage page, String id, String imageName, int height, ABMContainer container, int containerTopPx) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_PARALLAX;
		this.ImageName=imageName;
		this.Container = container;
		this.Container.ContainerTop = containerTopPx;
		this.Height = height;
		IsInitialized=true;
	}		
	
	public void InitializeScaled(ABMPage page, String id, String imageName, int heightSmall, int heightMedium, int heightLarge) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_PARALLAX;
		this.ImageName=imageName;
		this.ScaledHeightSmall = heightSmall;
		this.ScaledHeightMedium = heightMedium;
		this.ScaledHeightLarge = heightLarge;
		IsInitialized=true;
		IsScaled=true;
	}
	
	public void InitializeScaledWithContainer(ABMPage page, String id, String imageName, int heightSmall, int heightMedium, int heightLarge, ABMContainer container, int containerTopSmallPx, int containerTopMediumPx, int containerTopLargePx) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_PARALLAX;
		this.ImageName=imageName;
		this.Container = container;
		this.Container.ContainerTopSmall = containerTopSmallPx;
		this.Container.ContainerTopMedium = containerTopMediumPx;
		this.Container.ContainerTopLarge = containerTopLargePx;
		
		this.ScaledHeightSmall = heightSmall;
		this.ScaledHeightMedium = heightMedium;
		this.ScaledHeightLarge = heightLarge;
		IsScaled=true;
		IsInitialized=true;
	}
	
	
	@Override
	protected void ResetTheme() {
		// not used
	}
	
	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase() + "-parent";
	}
	
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}
	
	@Override
	protected void CleanUp() {
		super.CleanUp();
	}
	
	@Override
	protected void RemoveMe() {
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "parent");
	}
	
	@Override
	protected void FirstRun() {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "img");	
		
		int SML600=ABMaterial.ThresholdPxConsiderdSmall;
    	int SML992=ABMaterial.ThresholdPxConsiderdMedium;
    	int SML993=SML992+1;
    	
		if (IsScaled) {
			StringBuilder ss = new StringBuilder();
			ss.append("$('body').append(\"<style type='text/css'>");
			ss.append("@media only screen and (min-width : " + SML993 + "px) {.paral" + ID.toLowerCase() + " {height: " + ScaledHeightLarge + "px !important;}}");
			ss.append("@media only screen and (min-width: " + SML600 + "px) and (max-width: 992px) {.paral" + ID.toLowerCase() + " {height: " + ScaledHeightMedium + "px !important;}}");
			ss.append("@media only screen and (max-width : " + SML600 + "px) {.paral" + ID.toLowerCase() + " {height: " + ScaledHeightSmall + "px !important;}}");
			ss.append("</style>\");");
			Page.ws.Eval(ss.toString(), null);		
		}
		
		Page.ws.RunFunction("reinitparallax", Params);
		if (Container!=null) {
			Container.FirstRun();
		}
		
		super.FirstRun();
		
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		//TODO ExtraClasses not working
		
		super.Refresh();
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "parent");
		if (mIsPrintableClass.equals("")) {
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "parent", "no-print");
		} else {
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "parent", "no-print");
		}
		if (mIsOnlyForPrintClass.equals("")) {
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "parent", "only-print");
		} else {
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "parent", "only-print");
		}
		
		if (IsScaled) {
			j.SetProp("style", "height: " + Height + "px");
		}
		
		j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "img");
		j.SetProp("src", ImageName);
		
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "img");			
		Page.ws.RunFunction("initparallax", Params);
		
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Parallax Refresh: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
	}
	
	@Override
	protected String Build() {	
		StringBuilder s = new StringBuilder();
		if (IsScaled) {
			s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "parent\" class=\"parallax-container paral" + ID.toLowerCase() + " " + mVisibility + " " +mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() +"\" >\n");
			s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "img\" class=\"parallax\">\n");
		
			s.append("<img src=\"" + ImageName + "\" style=\"width:100% !important;height:auto !important\">\n");
		} else {
			s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "parent\" class=\"parallax-container" + " " + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() + "\" style=\"height: " + Height + "px\">\n");
			s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "img\" class=\"parallax\">\n");
		
			s.append("<img src=\"" + ImageName + "\">\n");
		}
		if (Container!=null) {
			s.append(Container.Build());
		}
		s.append("</div>\n");
		s.append("</div>\n");
		IsBuild=true;
		return s.toString();
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMParallax c = new ABMParallax();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;		
		c.Type = Type;
		c.mVisibility = mVisibility;	
		c.Height = Height;
		c.ImageName = ImageName;
		return c;
	}

}
