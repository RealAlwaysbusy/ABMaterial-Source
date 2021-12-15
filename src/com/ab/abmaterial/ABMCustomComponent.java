package com.ab.abmaterial;

import java.io.IOException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")  
@ShortName("ABMCustomComponent")
@Events(values={"Build(internalPage as ABMPage, internalID as String) as String", "Refresh(internalPage as ABMPage, internalID as String)", "FirstRun(internalPage as ABMPage, internalID as String)", "CleanUp(internalPage as ABMPage, internalID as String)"})
public class ABMCustomComponent extends ABMComponent  {
	
	private static final long serialVersionUID = -9211941571504957127L;
	protected transient Object caller=null;
	protected transient BA _ba=null;
	protected String evName="";
	protected String HTML="";
	protected String CSS="";
	protected String Style="";
	protected String Class="";
	public boolean RawBuild=false;
	
	public Boolean DoFlush=true;
		
	public void SetWrapperDivStyle(String style) {
		Style=style;
	}
	
	public void SetWrapperDivClasses(String Classes) {
		Class = Classes;
	}
	
	public void Initialize(final BA ba, String eventName, Object callObject, ABMPage page, String id, String css) {
		this.ID = id;
		this.Type = ABMaterial.UITYPE_CUSTOMCOMPONENT;
		this.Page = page;
		this._ba = ba;
		this.caller = callObject;
		this.evName = eventName.toLowerCase();
		CSS=css;
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
	protected void CleanUp() {
		_ba.raiseEvent(caller, evName + "_cleanup", new Object[] {Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase()});
		super.CleanUp();	
		this._ba = null;
		this.caller = null;
	}
	
	@Override
	protected void RemoveMe() {
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
	}
		
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(DoFlush);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		super.Refresh();
		ABMaterial.ChangeVisibility(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), mVisibility);
		
		if (mIsPrintableClass.equals("")) {
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "no-print");
		} else {
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "no-print");
		}
		ABMaterial.ChangeVisibility(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), mVisibility);
		
		if (mIsOnlyForPrintClass.equals("")) {
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "only-print");
		} else {
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "only-print");
		}
			
		_ba.raiseEvent(caller, evName + "_refresh", new Object[] {Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase()});
		if (DoFlush) {		
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("CustomComponent Refresh: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		
	}
	
	@Override
	protected void Prepare() {
		super.Prepare();		
	}
	
	@Override
	protected void FirstRun() {
		if (!CSS.equals("")) {
			StringBuilder s = new StringBuilder();
			s.append("var head = document.getElementsByTagName('head')[0];");
			s.append("var sty = document.getElementById('" + ID.toLowerCase() + "-css');");
			s.append("if (sty) {");
			s.append("sty[0].innerHTML=\"" + CSS.replaceAll("\"", "'").replaceAll("\t", " ").replaceAll("\r",  "").replaceAll("\n",  " ") + "\";");
			s.append("} else {");
			s.append("var s = document.createElement('style');");
			s.append("s.setAttribute('type', 'text/css');");
			s.append("s.setAttribute('id', '" + ID.toLowerCase() + "-css');");
			s.append("if (s.styleSheet) {");
			s.append("s.styleSheet.cssText = \"" + CSS.replaceAll("\"", "'").replaceAll("\t", " ").replaceAll("\r",  "").replaceAll("\n",  " ") + "\";");
			s.append("} else {");
			s.append("s.appendChild(document.createTextNode(\"" + CSS.replaceAll("\"", "'").replaceAll("\t", " ").replaceAll("\r",  "").replaceAll("\n",  " ") + "\"));");
			s.append("}");

			s.append("head.appendChild(s);");
			s.append("}");
			if (Page.ws!=null) {			
				Page.ws.Eval(s.toString(), null);
				try {
					if (Page.ws.getOpen()) {
						//BA.Log("Flushing");
						Page.ws.Flush();				
					}
				} catch (IOException e) {			
				
				}
			}
		}
		
		_ba.raiseEvent(caller, evName + "_firstrun", new Object[] {Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase()});		

		super.FirstRun();
		
	}	
	
	@Override		
	protected String Build() {
		HTML = (String) _ba.raiseEvent(caller, evName + "_build", new Object[] {Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase()});
		if (RawBuild) {
			//HTML = "<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + Style + "\" class=\"" + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + " " + Class + "\">" + HTML + "</div>";
		} else {
			HTML = "<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + Style + "\" class=\"" + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + " " + Class + "\">" + HTML + "</div>";
		}
		IsBuild = true;		
		return HTML;
	}	
	
	@Override
	protected ABMComponent Clone() {
		return null;		
	}	
}
