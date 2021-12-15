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
@ShortName("ABMSmartWizard")
@Events(values={"NavigationToStep(fromReturnName As String, toReturnName as String)","NavigationFinished(ReturnName as String)"})
public class ABMSmartWizard extends ABMComponent {
	
	private static final long serialVersionUID = 1288881392121525873L;
	protected ThemeSmartWizard Theme=new ThemeSmartWizard();
	protected Map<String, ABMSmartWizardStep> Steps = new LinkedHashMap<String, ABMSmartWizardStep>();
	protected int Selected=0;
	protected String NextButton="Next";
	protected String PreviousButton="Previous";
	protected String FinishButton="Finish";
	protected String Errors="";
	protected String Hiddens="";
	protected String Disableds="";
	
	protected boolean ShowPreviousButton=true;
	protected boolean ShowNextButton=true;
	protected boolean ShowFinishButton=true;
	
	protected String ToolbarType="arrows"; // for others, need extra css!
	protected String ToolbarPosition="bottom";
	
	/**
	 * 
	 * Leave the ButtonText empty if you do not want to show the navigation button
	 * if all buttonTexts are empty then no toolbar will be shown at the bottom
	 */
	public void Initialize(ABMPage page, String id, String previousButtonText, String nextButtonText, String finishButtonText, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_SMARTWIZARD;	
		this.PreviousButton=previousButtonText;
		this.NextButton=nextButtonText;
		this.FinishButton=finishButtonText;
		if (NextButton.equals("")) {
			ShowNextButton=false;
		}
		if (PreviousButton.equals("")) {
			ShowPreviousButton=false;
		}
		if (finishButtonText.equals("")) {
			ShowFinishButton=false;
		}
		if (!ShowNextButton && !ShowPreviousButton && !ShowFinishButton) {
			ToolbarPosition="none";
		}
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.SmartWizards.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.SmartWizards.get(themeName.toLowerCase()).Clone();				
			}
		}	
		IsInitialized=true;
	}
	
	/**
	 * Does not work on this component!
	 */
	@Override
	protected void ResetTheme() {
		// not used
	}
	
	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase();
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
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
	}
	
	public void AddStep(String returnName, String title, String description, String altIcon, ABMContainer containerStep, String wizardStepState) {
		ABMSmartWizardStep step = new ABMSmartWizardStep();
		step.ReturnName = returnName;
		step.Container = containerStep;
		step.StepCounter = Steps.size();
		step.State=wizardStepState;
		step.Title=title;
		step.Description=description;
		if (!altIcon.equals("")) {
			switch (altIcon.substring(0, 3).toLowerCase()) {
			case "mdi":
				step.AltIcon="<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon\" class=\""  + altIcon + " small center\"></i>";
				break;
			case "fa ":
			case "fa-":
				step.AltIcon="<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon\" class=\"" +  altIcon + " small center\"></i>";
				break;
			case "abm":
				step.AltIcon="<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon\" class=\"" + " small center\">" + Page.svgIconmap.getOrDefault(altIcon.toLowerCase(), "") + "</i>";
				break;
			default:
				step.AltIcon="<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-icon\" class=\"material-icons small center\">" + ABMaterial.HTMLConv().htmlEscape(altIcon, Page.PageCharset) + "</i>";
			}
		}
		
		switch (wizardStepState) {
		case ABMaterial.SMARTWIZARD_STATE_DISABLED:
			if (Disableds.length()>0) {
				Disableds+=",";
			}
			Disableds+=step.StepCounter;
			break;
		case ABMaterial.SMARTWIZARD_STATE_ERROR:
			if (Errors.length()>0) {
				Errors+=",";
			}
			Errors+=step.StepCounter;
			break;
		case ABMaterial.SMARTWIZARD_STATE_HIDDEN:
			if (Hiddens.length()>0) {
				Hiddens+=",";
			}
			Disableds+=step.StepCounter;
			break;
		}
		
		Steps.put(returnName.toLowerCase(), step);
	}
	
	public ABMContainer GetStep(String returnName) {
		ABMSmartWizardStep step = Steps.getOrDefault(returnName.toLowerCase(), null);
		if (step!=null) {
			return step.Container;
		}
		return null;
	}
	
	public String GetAllStepStates() {
		return "";
	}
		
	public void NavigateGoto(String returnName) {
		if (Page.ws!=null) {
			ABMSmartWizardStep step = Steps.getOrDefault(returnName.toLowerCase(), null);
			if (step!=null) {
				Selected = step.StepCounter;
				Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').smartWizard('showStep', " + step.StepCounter + ")", null);				
			}
		}
	}
	
	public void NavigateCancel(String returnName) {
		if (Page.ws!=null) {
			ABMSmartWizardStep step = Steps.getOrDefault(returnName.toLowerCase(), null);
			if (step!=null) {
				Selected = step.StepCounter;
				Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').smartWizard('cancel', " + step.StepCounter + ")", null);				
			}
		}
	}	
	
	public void SetStepState(String returnName, String wizardStepState) {
		if (Page.ws!=null) {
			ABMSmartWizardStep step = Steps.getOrDefault(returnName.toLowerCase(), null);
			if (step!=null) {
				Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').smartWizard('stepState', [" + step.StepCounter + "], '" + wizardStepState + "')", null);				
			}
		}
	}
	
	public void SetStepText(String returnName, String title, String description) {
		ABMSmartWizardStep step = Steps.getOrDefault(returnName.toLowerCase(), null);
		if (step!=null) {
			Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').smartWizard('setTexts', " + step.StepCounter + ", '" + BuildText(title) + "', '" + BuildText(description) + "')", null);
		}
	}
		
	@Override
	protected void FirstRun() {
		Page.ws.Eval(BuildJavaScript(), null);	
		for (Entry<String,ABMSmartWizardStep> entry: Steps.entrySet()) {
			ABMSmartWizardStep step = entry.getValue();
			step.Container.RunAllFirstRunsInternal(false);		
		}
		Page.ws.Eval("initplugins();", null);		
		super.FirstRun();
	}
	
	protected String BuildJavaScript() {
		StringBuilder s = new StringBuilder();	
		ThemeSmartWizard l = Theme;
		s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').smartWizard({");
		s.append("themeName: '" + l.ThemeName.toLowerCase() + "',");
		s.append("responsivethreshold: " + l.ResponsiveBehaviourThresholdPx + ",");
		s.append("isresponsive: " + l.IsResponsive + ",");
		s.append("selected: " + Selected + ",");  // Initial selected step, 0 = first step 
		s.append("keyNavigation: false,"); // Enable/Disable keyboard navigation(left and right keys are used if enabled)
		s.append("autoAdjustHeight: false,"); // Automatically adjust content height
		s.append("cycleSteps: false,"); // Allows to cycle the navigation of steps
		s.append("backButtonSupport: false,"); // Enable the back button support
		s.append("useURLhash: false,"); // Enable selection of the step based on url hash
		s.append("showStepURLhash: false,");
		s.append("lang: {");  // Language variables
		s.append("next: '" + NextButton + "',"); 
		s.append("previous: '" + PreviousButton + "',");
		s.append("finish: '" + FinishButton + "'");
		s.append("},");
		s.append("toolbarSettings: {");
		s.append("toolbarPosition: '" + ToolbarPosition + "',"); // none, top, bottom, both
		s.append("toolbarButtonPosition: 'right',"); // left, right
		s.append("showNextButton: " + ShowNextButton + ","); // show/hide a Next button
		s.append("showPreviousButton: " + ShowPreviousButton + ","); // show/hide a Previous button
		s.append("showFinishButton: " + ShowFinishButton + ",");
		s.append("toolbarExtraButtons: []");
		s.append("},"); 
		s.append("anchorSettings: {");
		s.append("anchorClickable: true,"); // Enable/Disable anchor navigation
		s.append("enableAllAnchors: false,"); // Activates all anchors clickable all times
		s.append("markDoneStep: false,"); // add done css
		s.append("enableAnchorOnDoneStep: true"); // Enable/Disable the done steps navigation
		s.append("},");     
		s.append("nextColor: '" + ABMaterial.GetColorStr(l.ButtonNextBackColor, l.ButtonNextBackColorIntensity, "") + " " + ABMaterial.GetColorStr(l.ButtonNextForeColor, l.ButtonNextForeColorIntensity, "text") + "',");     
		s.append("prevColor: '" + ABMaterial.GetColorStr(l.ButtonPreviousBackColor, l.ButtonPreviousBackColorIntensity, "") + " " + ABMaterial.GetColorStr(l.ButtonPreviousForeColor, l.ButtonPreviousColorIntensity, "text") + "',");
		s.append("finishColor: '" + ABMaterial.GetColorStr(l.ButtonFinishBackColor, l.ButtonFinishBackColorIntensity, "") + " " + ABMaterial.GetColorStr(l.ButtonFinishForeColor, l.ButtonFinishColorIntensity, "text") + "',");     
		s.append("contentURL: null,"); // content url, Enables Ajax content loading. can set as data data-content-url on anchor
		s.append("disabledSteps: [" + Disableds + "],");    // Array Steps disabled
		s.append("errorSteps: [" + Errors + "],");    // Highlight step with errors
		s.append("hiddenSteps: [" + Hiddens + "],");    // array step with hidden
		s.append("theme: '" + ToolbarType + "',");
		s.append("transitionEffect: 'none',"); // Effect on navigation, none/slide/fade
		s.append("transitionSpeed: '200'");
		s.append("});");				
		return s.toString();
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
	
	/**
	 * This is a limited version of Refresh() which will only refresh the smartwizard properties + it steps, NOT its contents.		 
	 */
	public void RefreshNoContents(boolean DoFlush) {
		super.Refresh();
		
		for (Entry<String,ABMSmartWizardStep> entry: Steps.entrySet()) {
			ABMSmartWizardStep step = entry.getValue();
			step.Container.RefreshNoContents(false);			
		}
		if (Page!=null) {
			if (Page.ws!=null) {
				Page.ws.Eval("$(window).trigger('resize');", null);
			}
		}
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("SmartWizard RefreshNoContents: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		// TODO ExtraClasses not working
		super.Refresh();
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
		
		for (Entry<String,ABMSmartWizardStep> entry: Steps.entrySet()) {
			ABMSmartWizardStep step = entry.getValue();
			step.Container.RefreshInternalExtra(false, true, false);			
		}
		if (Page!=null) {
			if (Page.ws!=null) {
				Page.ws.Eval("$(window).trigger('resize');", null);
			}
		}
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("SmartWizard Refresh: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
		
	}
	
	@Override
	protected String Build() {
		if (Theme.ThemeName.equals("default")) {
			Theme.Colorize(Page.CompleteTheme.MainColor);
		}
		StringBuilder s = new StringBuilder();	
		s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"abmsw " + Theme.ZDepth + " " + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + " " + ABMaterial.GetColorStr(Theme.BackColor, Theme.BackColorIntensity, "") + super.BuildExtraClasses() + "\">");
		s.append(BuildBody());
		s.append("</div>");
		IsBuild=true;
		return s.toString();
	}
	
	@Hide
	protected String BuildClass() {			
		StringBuilder s = new StringBuilder();			
		return s.toString(); 
	}
	
	@Hide
	protected String BuildBody() {
		StringBuilder s = new StringBuilder();
		s.append("<ul style=\"margin-top: 0;margin-bottom: 0;\" >");
		int counter=1;
		for (Entry<String,ABMSmartWizardStep> entry: Steps.entrySet()) {
			ABMSmartWizardStep step = entry.getValue();
			String span="";
			if (counter==1) {
				span="<div class=\"sw-block-first hide\" style=\"pointer-events: none;\">" + step.AltIcon + "</div>";
			} else {
				span="<div class=\"sw-block hide\" style=\"pointer-events: none;\">" + step.AltIcon + "</div>";
			}
			counter++;
			s.append("<li style=\"float: left;\" class=\"" + step.State + "\"><a data-content-url=\"" + step.ReturnName + "\" data-evname=\"" + ID.toLowerCase() + "\" href=\"#step-" + step.StepCounter + "\"><div class=\"abmalt hide\">&nbsp;<br><small>&nbsp;</small></div><div class=\"abmreal\">" + BuildText(step.Title) + "<br><small>" + BuildText(step.Description) + "</small></div></a>" + span + "</li>");
		}
		s.append("</ul>");
		s.append("<div>");
		for (Entry<String,ABMSmartWizardStep> entry: Steps.entrySet()) {
			ABMSmartWizardStep step = entry.getValue();
			s.append("<div id=\"step-" + step.StepCounter + "\" class=\"\">");
			s.append(step.Container.Build());
			s.append("</div>");
		}
		s.append("</div>");
		return s.toString();
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
	
	@Override
	protected ABMComponent Clone() {
		ABMSmartWizard c = new ABMSmartWizard();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;
		c.Theme = Theme.Clone();
		c.Type = Type;
		c.mVisibility = mVisibility;		
		return c;
	}
	
	@Hide
	protected class ABMSmartWizardStep {
		protected String ReturnName="";
		protected ABMContainer Container=null;
		protected int StepCounter=0;
		protected String State="";
		protected String Title="";
		protected String Description="";
		protected String AltIcon="";
	}
		

}

