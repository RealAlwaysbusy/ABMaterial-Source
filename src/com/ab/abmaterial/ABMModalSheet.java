package com.ab.abmaterial;

import java.io.IOException;
import java.util.Map.Entry;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;

// Changed in materialize.min.js:
// openModal: function(options) { -> end_top is new option

@Author("Alain Bailleul")  
@ShortName("ABMModalSheet")
public class ABMModalSheet extends ABMObject {
	
	private static final long serialVersionUID = 7991099217918444285L;
	public ABMContainer Header=new ABMContainer();
	public ABMContainer Content=new ABMContainer();
	public ABMContainer Footer=new ABMContainer();
	protected boolean FixedFooter=false;
	protected String SheetType=ABMaterial.MODALSHEET_TYPE_NORMAL;
	protected String ID="";
	protected boolean IsBuild=false;
	protected transient ABMPage Page=null;
	public boolean IsDismissible=false;
	protected ThemeModalSheet Theme = new ThemeModalSheet();
	public int Size=ABMaterial.MODALSHEET_SIZE_NORMAL;
	protected boolean IsOpen=false;
	public boolean IsInitialized=false;
	
	public boolean NeverShowVerticalScrollBar=false; //neveroverflow
	
	public boolean IsTextSelectable=true;
	
	public Object EventHandler=null;
	
	public String MaxWidth="";
	public String MaxHeight="";
	public Object Tag=null;
	protected boolean mPrintableContent=false;
			
	/**
	 * modalsheetType: use the ABM.MODALSHEET_TYPE_ constants
	 * 
	 * for backward compatability you can still use true and false: 
	 *     true = .MODALSHEET_TYPE_BOTTOM
	 *     false = .MODALSHEET_TYPE_NORMAL 
	 */
	public void Initialize(ABMPage page, String id, boolean fixedFooter, String modalsheetType, String themeName) {
		this.ID = id;
		this.Page= page;
		this.Type=ABMaterial.UITYPE_MODALSHEET;
		this.FixedFooter = fixedFooter;
		if (modalsheetType.equalsIgnoreCase("true")) {
			modalsheetType=ABMaterial.MODALSHEET_TYPE_BOTTOM;
		}
		if (modalsheetType.equalsIgnoreCase("false")) {
			modalsheetType=ABMaterial.MODALSHEET_TYPE_NORMAL;
		}
		this.SheetType = modalsheetType;
		Header.Initialize(page, id + "-header", "");
		Header.ModalID = id + "-header";
		
		Content.Initialize(page, id + "-content", "");
		Content.ModalID = id + "-content";
		Footer.Initialize(page, id + "-footer", "");
		Footer.ModalID = id + "-footer";
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.ModalSheets.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.ModalSheets.get(themeName.toLowerCase()).Clone();				
			}
		}	
		if (Theme.ThemeName.equals("default")) {
			Theme.Colorize(Page.CompleteTheme.MainColor);
		}
		Header.Theme.BackColor = Theme.HeaderBackColor;
		Header.Theme.BackColorIntensity = Theme.HeaderBackColorIntensity;
		Content.Theme.BackColor = Theme.ContentBackColor;
		Content.Theme.BackColorIntensity = Theme.ContentBackColorIntensity;
		Footer.Theme.BackColor = Theme.FooterBackColor;
		Footer.Theme.BackColorIntensity = Theme.FooterBackColorIntensity;
		IsInitialized=true;
	}
		
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.ModalSheets.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.ModalSheets.get(themeName.toLowerCase()).Clone();				
			}
		}
	}
	
	protected void ResetTheme() {
		UseTheme(Theme.ThemeName);
		if (!Header.Rows.isEmpty()) {			
			for (Entry<String, ABMRow> row : Header.Rows.entrySet()) {
				row.getValue().ResetTheme();							
	        }
		}
		if (!Content.Rows.isEmpty()) {
			for (Entry<String, ABMRow> row : Content.Rows.entrySet()) {
				row.getValue().ResetTheme();							
	        }			
		}		
		if (!Footer.Rows.isEmpty()) {			
			for (Entry<String, ABMRow> row : Footer.Rows.entrySet()) {
				row.getValue().ResetTheme();							
	        }
		}		
	}
	
	/**
	 * Enable/Disable this modalsheet from being printed 
	 */	
	public void setIsPrintableContent(Boolean printme) {
		mPrintableContent = printme;
		if (Page.ws!=null) {
			try {
				if (printme) {
					Page.ws.Eval("$('#" + ID.toLowerCase() + "-modalcontent').addClass('only-print');", null);
					Page.ws.Eval("$('#" + ID.toLowerCase() + "-modalcontent').removeClass('no-print');", null);
				} else {
					Page.ws.Eval("$('#" + ID.toLowerCase() + "-modalcontent').addClass('no-print');", null);
					Page.ws.Eval("$('#" + ID.toLowerCase() + "-modalcontent').removeClass('only-print');", null);
				}
				Page.ws.Eval("$('#" + ID.toLowerCase() + "-modalheader').addClass('no-print');", null);
				Page.ws.Eval("$('#" + ID.toLowerCase() + "-modalfooter').addClass('no-print');", null);
				if (Page.ws.getOpen()) {
					Page.ws.Flush();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}			
		}
	}
		
	protected String Build() {	
		SetEventHandler();
		if (Theme.ThemeName.equals("default")) {
			Theme.Colorize(Page.CompleteTheme.MainColor);
		}
		StringBuilder s = new StringBuilder();	
		String fFooter="";
		String fmodal="";
		String bSheet="";
		switch (SheetType) {
		case ABMaterial.MODALSHEET_TYPE_BOTTOM:
			fmodal = " modal ";
			bSheet = " bottom-sheet ";
			if (FixedFooter) {
				fFooter = " modal-fixed-footer ";			
			}
			break;
		case ABMaterial.MODALSHEET_TYPE_TOP:
			fmodal = " modal ";
			bSheet = " top-sheet ";
			if (FixedFooter) {
				fFooter = " modal-fixed-footer ";			
			}
			break;
		default:
			switch (Size) {
			case ABMaterial.MODALSHEET_SIZE_NORMAL:
				fmodal = " modal "; //modalnorm ";
				break;
			case ABMaterial.MODALSHEET_SIZE_LARGE:
				fmodal = " modal modalbig ";
				break;				
			case ABMaterial.MODALSHEET_SIZE_FULL:
				fmodal = " modal modalxlbig ";
				break;				
			}
			if (FixedFooter) {
				switch (Size) {
				case ABMaterial.MODALSHEET_SIZE_NORMAL:
					fFooter = " modal-fixed-footer "; //modal-fixed-footernorm ";
					break;
				case ABMaterial.MODALSHEET_SIZE_LARGE:
					fFooter = " modal-fixed-footer modal-fixed-footerbig ";
					break;					
				case ABMaterial.MODALSHEET_SIZE_FULL:
					fFooter = " modal-fixed-footer modal-fixed-footerxlbig ";
					break;					
				}
							
			}
		}
		if (NeverShowVerticalScrollBar ) {
			fmodal = fmodal + " neveroverflow "; 
		}
		
		String selectable="";
		if (!IsTextSelectable) {
			selectable = " notselectable ";
		}
		String max="style=\"";
		if (!MaxWidth.equals("")) {
			max=max + "max-width:" + MaxWidth + ";";
		}
		if (!MaxHeight.equals("")) {
			max=max + "max-height:" + MaxHeight + ";";
		}
		max = max + "\"";
		
		s.append("<div id=\"" + ID.toLowerCase() + "\" class=\"" + fmodal + fFooter + bSheet + selectable + "\" tabindex=\"-1\" " + max + ">\n");
		
		String style="";
		if (!Header.Rows.isEmpty()) {
			style = BuildStyleHeader();
			if (!style.equals("")) {
				style = " style=\"" + style + "\"";
			}
			s.append("<div id=\"" + ID.toLowerCase() + "-modalheader\" class=\"modal-header " + ABMaterial.GetColorStr(Footer.Theme.BackColor, Footer.Theme.BackColorIntensity, "") +"\"" + style + ">\n");
			if (!Header.Rows.isEmpty()) {
				s.append(Header.Build());
			}
			s.append("</div>\n");
		}
		style = BuildStyleContent();
		if (!style.equals("")) {
			style = " style=\"" + style + "\"";
		}
		s.append("<div id=\"" + ID.toLowerCase() + "-modalcontent\" class=\"modal-content " + ABMaterial.GetColorStr(Content.Theme.BackColor, Content.Theme.BackColorIntensity, "") +"\"" + style + ">\n");
		if (!Content.Rows.isEmpty()) {
			s.append(Content.Build());
		}
		s.append("</div>\n");	
		if (!Footer.Rows.isEmpty()) {
			style = BuildStyleFooter();
			if (!style.equals("")) {
				style = " style=\"" + style + "\"";
			}
			s.append("<div id=\"" + ID.toLowerCase() + "-modalfooter\" class=\"modal-footer " + ABMaterial.GetColorStr(Footer.Theme.BackColor, Footer.Theme.BackColorIntensity, "") +"\"" + style + ">\n");
			if (!Footer.Rows.isEmpty()) {
				s.append(Footer.Build());
			}
			s.append("</div>\n");
		}
		s.append("</div>\n");
		IsBuild=true;
		return s.toString();
	}
	
	
	protected void CheckIfInBrowser(StringBuilder ToCheckSB) {
		if (!Header.Rows.isEmpty()) {			
			Header.CheckIfInBrowser(ToCheckSB);			
		}
		if (!Content.Rows.isEmpty()) {
			Content.CheckIfInBrowser(ToCheckSB);
		}		
		if (!Footer.Rows.isEmpty()) {	
			Footer.CheckIfInBrowser(ToCheckSB);
		}
	}
	
	protected boolean ResetNotInBrowser(String ToCheck, String OKCheck) {
		boolean ret = true;
		if (!Header.Rows.isEmpty()) {			
			if (!Header.ResetNotInBrowser(ToCheck, OKCheck)) {
				ret = false;
			}			
		}
		if (!Content.Rows.isEmpty()) {
			if (!Content.ResetNotInBrowser(ToCheck, OKCheck)) {
				ret = false;
			}	
		}		
		if (!Footer.Rows.isEmpty()) {	
			if (!Footer.ResetNotInBrowser(ToCheck, OKCheck)) {
				ret = false;
			}	
		}		
		return ret;
	}
	
	
	
	public void Refresh() {
		RefreshInternalExtra(false,true, true);
	}
	
	/**
	 * This is a limited version of Refresh() which will only refresh the modalsheet properties + each row properties + each cell properties, NOT its contents.		 
	 */
	public void RefreshNoContents(boolean DoFlush) {
		SetEventHandler();
		
		if (!MaxWidth.equals("")) {
			ABMaterial.SetStyleProperty(Page, ID.toLowerCase(), "max-width", MaxWidth);
		}
		if (!MaxHeight.equals("")) {
			ABMaterial.SetStyleProperty(Page, ID.toLowerCase(), "max-height", MaxHeight);
		}
		
		String hideHeaderFooter="";
		if (mPrintableContent) {
			hideHeaderFooter=" no-print ";
		}
		
		JQueryElement j = Page.ws.GetElementById(ID.toLowerCase() + "-modalheader");
		j.SetProp("class", "modal-header " + ABMaterial.GetColorStr(Footer.Theme.BackColor, Footer.Theme.BackColorIntensity + hideHeaderFooter, ""));
		String style="";
		style = BuildStyleHeader();
		if (!style.equals("")) {
			j.SetProp("style", style);
		}
		j = Page.ws.GetElementById(ID.toLowerCase() + "-modalcontent");
		j.SetProp("class", "modal-content " + ABMaterial.GetColorStr(Content.Theme.BackColor, Content.Theme.BackColorIntensity, ""));
		style = BuildStyleContent();
		if (!style.equals("")) {
			j.SetProp("style", style);
		}
		j = Page.ws.GetElementById(ID.toLowerCase() + "-modalfooter");
		j.SetProp("class", "modal-footer " + ABMaterial.GetColorStr(Footer.Theme.BackColor, Footer.Theme.BackColorIntensity + hideHeaderFooter, ""));
		style = BuildStyleFooter();
		if (!style.equals("")) {
			j.SetProp("style", style);
		}
		if (!Header.Rows.isEmpty()) {			
			Header.RefreshNoContents(false);			
		}
		if (!Content.Rows.isEmpty()) {			
			Content.RefreshNoContents(false);			
		}		
		if (!Footer.Rows.isEmpty()) {				
			Footer.RefreshNoContents(false);			
		}
		setIsPrintableContent(mPrintableContent);
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("ModalSheet RefreshNoContents: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
	}
	
	protected void RefreshInternalExtra(boolean onlyNew, boolean DoTheChecks, boolean DoFlush) {
		SetEventHandler();
		
		if (!MaxWidth.equals("")) {
			ABMaterial.SetStyleProperty(Page, ID.toLowerCase(), "max-width", MaxWidth);
		}
		if (!MaxHeight.equals("")) {
			ABMaterial.SetStyleProperty(Page, ID.toLowerCase(), "max-height", MaxHeight);
		}
		
		String hideHeaderFooter="";
		if (mPrintableContent) {
			hideHeaderFooter=" no-print ";
		}
		
		JQueryElement j = Page.ws.GetElementById(ID.toLowerCase() + "-modalheader");
		j.SetProp("class", "modal-header " + ABMaterial.GetColorStr(Footer.Theme.BackColor, Footer.Theme.BackColorIntensity + hideHeaderFooter, ""));
		String style="";
		style = BuildStyleHeader();
		if (!style.equals("")) {
			j.SetProp("style", style);
		}
		j = Page.ws.GetElementById(ID.toLowerCase() + "-modalcontent");
		j.SetProp("class", "modal-content " + ABMaterial.GetColorStr(Content.Theme.BackColor, Content.Theme.BackColorIntensity, ""));
		style = BuildStyleContent();
		if (!style.equals("")) {
			j.SetProp("style", style);
		}
		j = Page.ws.GetElementById(ID.toLowerCase() + "-modalfooter");
		j.SetProp("class", "modal-footer " + ABMaterial.GetColorStr(Footer.Theme.BackColor, Footer.Theme.BackColorIntensity + hideHeaderFooter, ""));
		style = BuildStyleFooter();
		if (!style.equals("")) {
			j.SetProp("style", style);
		}
		if (!Header.Rows.isEmpty()) {			
			Header.RefreshInternalExtra(onlyNew, DoTheChecks, false);			
		}
		if (!Content.Rows.isEmpty()) {			
			Content.RefreshInternalExtra(onlyNew, DoTheChecks, false);			
		}		
		if (!Footer.Rows.isEmpty()) {				
			Footer.RefreshInternalExtra(onlyNew, DoTheChecks, false);			
		}
		setIsPrintableContent(mPrintableContent);
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("ModalSheet RefreshInternal: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
	}
	
	protected void SetEventHandler() {
		if (EventHandler!=null) {
			Page.EventHandlers.put(ID.toLowerCase(), EventHandler);	
			if (!Header.Rows.isEmpty()) {			
				Header.SetEventHandlerParent(EventHandler);			
			}
			if (!Content.Rows.isEmpty()) {			
				Content.SetEventHandlerParent(EventHandler);					
			}		
			if (!Footer.Rows.isEmpty()) {				
				Footer.SetEventHandlerParent(EventHandler);					
			}
		}
	}
	
	protected String BuildStyleHeader() {
		StringBuilder s = new StringBuilder();
		
		if (Header.MarginTop!="") s.append(" margin-top: " + Header.MarginTop + ";");
		if (Header.MarginBottom!="") s.append(" margin-bottom: " + Header.MarginBottom + ";");
		if (Header.MarginLeft!="") s.append(" margin-left: " + Header.MarginLeft + ";");
		if (Header.MarginRight!="") s.append(" margin-right: " + Header.MarginRight + ";");
		if (Header.PaddingTop!="") s.append(" padding-top: " + Header.PaddingTop + ";");
		if (Header.PaddingBottom!="") s.append(" padding-bottom: " + Header.PaddingBottom + ";");
		if (Header.PaddingLeft!="") s.append(" padding-left: " + Header.PaddingLeft + ";");
		if (Header.PaddingRight!="") s.append(" padding-right: " + Header.PaddingRight + ";");
		return s.toString();
	}
	
	protected String BuildStyleContent() {
		StringBuilder s = new StringBuilder();
		
		if (Content.MarginTop!="") s.append(" margin-top: " + Content.MarginTop + ";");
		if (Content.MarginBottom!="") s.append(" margin-bottom: " + Content.MarginBottom + ";");
		if (Content.MarginLeft!="") s.append(" margin-left: " + Content.MarginLeft + ";");
		if (Content.MarginRight!="") s.append(" margin-right: " + Content.MarginRight + ";");
		if (Content.PaddingTop!="") s.append(" padding-top: " + Content.PaddingTop + ";");
		if (Content.PaddingBottom!="") s.append(" padding-bottom: " + Content.PaddingBottom + ";");
		if (Content.PaddingLeft!="") s.append(" padding-left: " + Content.PaddingLeft + ";");
		if (Content.PaddingRight!="") s.append(" padding-right: " + Content.PaddingRight + ";");		
		return s.toString();
	}
	
	protected String BuildStyleFooter() {
		StringBuilder s = new StringBuilder();
		
		if (Footer.MarginTop!="") s.append(" margin-top: " + Footer.MarginTop + ";");
		if (Footer.MarginBottom!="") s.append(" margin-bottom: " + Footer.MarginBottom + ";");
		if (Footer.MarginLeft!="") s.append(" margin-left: " + Footer.MarginLeft + ";");
		if (Footer.MarginRight!="") s.append(" margin-right: " + Footer.MarginRight + ";");
		if (Footer.PaddingTop!="") s.append(" padding-top: " + Footer.PaddingTop + ";");
		if (Footer.PaddingBottom!="") s.append(" padding-bottom: " + Footer.PaddingBottom + ";");
		if (Footer.PaddingLeft!="") s.append(" padding-left: " + Footer.PaddingLeft + ";");
		if (Footer.PaddingRight!="") s.append(" padding-right: " + Footer.PaddingRight + ";");
		return s.toString();
	}
	
	protected void Prepare() {
		IsBuild=true;
		if (!Header.Rows.isEmpty()) {			
			Header.Prepare();
		}
		if (!Content.Rows.isEmpty()) {
			Content.Prepare();
		}		
		if (!Footer.Rows.isEmpty()) {			
			Footer.Prepare();
		}
	}
	
	protected void RunAllFirstRuns() {
		FirstRun();
		if (!Header.Rows.isEmpty()) {
			Header.RunAllFirstRuns();
		}	
		if (!Content.Rows.isEmpty()) {
			Content.RunAllFirstRuns();
		}		
		if (!Footer.Rows.isEmpty()) {			
			Footer.RunAllFirstRuns();
		}
	}
	
	protected void FirstRun() {
		
	}
}
