package com.ab.abmaterial;

import java.io.IOException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")  
@ShortName("ABMSignaturePad")
public class ABMSignaturePad extends ABMComponent {
	
	private static final long serialVersionUID = 930296549392657585L;
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	protected String ClearColor=ABMaterial.COLOR_WHITE;
	protected String ClearColorIntensity=ABMaterial.INTENSITY_NORMAL;
	protected String PenColor=ABMaterial.COLOR_BLACK;
	protected String PenColorIntensity=ABMaterial.INTENSITY_NORMAL;
	protected String ZDepth="";
	protected int Width=0;
	protected int Height=0;
	protected String BorderColor=ABMaterial.COLOR_BLACK;
	protected String BorderColorIntensity=ABMaterial.INTENSITY_NORMAL;
	protected int BorderWidthPx=0;
	protected String BorderStyle="";
	public String UploadHandler="abmuploadhandler";
	
	public void Initialize(ABMPage page, String id, int widthPx, int heightPx, String clearColor, String clearColorIntensity, String penColor, String penColorIntensity, String zDepth) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_SIGNATUREPAD;
		this.ClearColor=clearColor;
		this.ClearColorIntensity=clearColorIntensity;
		this.PenColor=penColor;
		this.PenColorIntensity=penColorIntensity;
		this.ZDepth=zDepth;
		this.Width=widthPx;
		this.Height=heightPx;
		IsInitialized=true;
	}
	
	/**
	 * borderStyle: use the ABMaterial.BORDER_ constants
	 */
	public void SetBorder(String borderColor, String borderColorIntensity, int borderWidthPx, String borderStyle) {
		BorderColor = borderColor;
		BorderColorIntensity=borderColorIntensity;
		BorderWidthPx=borderWidthPx;
		BorderStyle=borderStyle;
	}
	
	@Override
	protected void ResetTheme() {
		//not used
	}
	
	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase();
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
		super.CleanUp();
	}
	
	@Override
	protected void RemoveMe() {
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent");
	}
	
	@Override
	protected void FirstRun() {
		Page.ws.Eval(BuildJavaScript(), null);
		
		super.FirstRun();
	}
	
	protected String BuildJavaScript() {
		StringBuilder s = new StringBuilder();		
		s.append("signaturesCanvas['" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "']=document.getElementById(\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\");");
		s.append("signatures['" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "']=new SignaturePad(signaturesCanvas['" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "']);");
		s.append("signatures['" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "'].penColor=\"" + ABMaterial.GetColorStrMap(PenColor, PenColorIntensity) + "\";");
		s.append("signatures['" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "'].backgroundColor=\"" + ABMaterial.GetColorStrMap(ClearColor, ClearColorIntensity) + "\";");
		s.append("signatures['" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "'].clear();");
		return s.toString();
	}
	
	
	
	public void Clear() {
		ABMaterial.ClearSignature(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
	}
	
	public void GetDrawingURI(String fileName) {
		ABMaterial.GetDrawingURI(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), fileName, UploadHandler.toLowerCase());	
		
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		// TODO ExtraClasses not working
		super.Refresh();
		
		ABMaterial.ChangeVisibility(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent", mVisibility);
		if (mIsPrintableClass.equals("")) {
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent", "no-print");
		} else {
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent", "no-print");
		}
		if (mIsOnlyForPrintClass.equals("")) {
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent", "only-print");
		} else {
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent", "only-print");
		}
		
		if (!ToolTipText.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-position", ToolTipPosition);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-delay", ""+ToolTipDelay);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-tooltip", ToolTipText);
		}
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("SignaturePad Refresh: " + ID);}
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
		
		String Border="";
		if (BorderWidthPx>0) {
			Border = "border: " + BorderWidthPx + "px solid " + ABMaterial.GetColorStrMap(BorderColor, BorderColorIntensity) + ";border-style: " + BorderStyle + ";padding: " + BorderWidthPx + "px;margin: -" + BorderWidthPx + "px;";
		}
		s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent\" class=\"" + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() + "\" style=\"" + Border + "\">\n");
		s.append("<canvas id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" width=\"" + Width + "\" height=\"" + Height + "\" class=\"canvastofit " + ZDepth + "\" style=\"cursor:pointer\"></canvas>\n");
		s.append("</div>\n");	    
				
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
	
	@Override
	protected ABMComponent Clone() {
		ABMSignaturePad c = new ABMSignaturePad();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;
		c.Type = Type;
		c.mVisibility = mVisibility;	
		c.ToolTipDelay = ToolTipDelay;
		c.ToolTipPosition = ToolTipPosition;
		c.ToolTipText = ToolTipText;
		c.ClearColor=ClearColor;
		c.ClearColorIntensity=ClearColorIntensity;
		c.PenColor=PenColor;
		c.PenColorIntensity=PenColorIntensity;
		c.ZDepth=ZDepth;
		return c;
	}
}
