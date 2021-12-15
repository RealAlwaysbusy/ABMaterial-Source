package com.ab.abmaterial;

import java.io.IOException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;

@Author("Alain Bailleul")  
@ShortName("ABMCodeLabel")
public class ABMCodeLabel extends ABMComponent {
	private static final long serialVersionUID = -8641935345322705667L;
	public String Text="";
	public String Language="";
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
		
	public void Initialize(ABMPage page, String id, String text, String language) {
		this.ID = id;
		this.Text = text;
		this.Language = language;	
		this.Type=ABMaterial.UITYPE_CODELABEL;
		IsInitialized=true;
	}
	
	@Override
	protected void ResetTheme() {
		// Dummy		
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
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
	}
	
	@Override
	protected void FirstRun() {	
		Page.ws.Eval(BuildJavaScript(), null);
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());		
		Page.ws.RunFunction("inittooltipped", Params);
		
		super.FirstRun();		
	}
	
	protected String BuildJavaScript() {
		StringBuilder s = new StringBuilder();		
		s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').removeClass('prettyprinted');");
		s.append("PR.prettyPrint();");		
		return s.toString();
	}
		
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		j.SetProp("class", "prettyprint lang-" + Language + " " + mVisibility + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses());
		if (!ToolTipText.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-position", ToolTipPosition);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-delay", ""+ToolTipDelay);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-tooltip", ToolTipText);
		}
		j.SetText(Text);
		Page.ws.Eval(BuildJavaScript(), null);
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("CodeLabel Refresh: " + ID);}
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
		String toolTip="";
		if (!ToolTipText.equals("")) {
			toolTip=" data-position=\"" + ToolTipPosition + "\" data-delay=\"" + ToolTipDelay + "\" data-tooltip=\"" + ToolTipText + "\" "; 
		}
		s.append("<pre " + toolTip + " class=\"prettyprint lang-" + Language + " " + mVisibility + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() + "\" id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"direction: ltr\">\n" );
		s.append(Text);
		s.append("</pre>\n");
		IsBuild=true;
		return s.toString();
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMCodeLabel c = new ABMCodeLabel();
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
		c.Text = Text;
		c.Language = Language;
		return c;
	}
}
