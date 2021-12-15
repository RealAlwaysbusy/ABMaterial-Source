package com.ab.abmaterial;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.RaisesSynchronousEvents;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.objects.collections.List;

@Author("Alain Bailleul")  
@ShortName("ABMReport")
@Events(values={"Build(internalPage as ABMPage, internalID as String)", "Refresh(internalPage as ABMPage, internalID as String)", "FirstRun(internalPage as ABMPage, internalID as String)", "CleanUp(internalPage as ABMPage, internalID as String)"})
public class ABMReport extends ABMComponent {
	private static final long serialVersionUID = 1191902034014904611L;
	public ABMReportBlock Header = new ABMReportBlock();
	protected List Body = new List();
	protected Map<String,Integer> BodyMap = new LinkedHashMap<String,Integer>();
	public ABMReportBlock Footer = new ABMReportBlock();
	
	protected String res="";
	
	protected transient Object caller=null;
	protected transient BA _ba=null;
	protected String evName="";
	protected String HTML="";
	protected String CSS="";
	
	protected String mPageBreak="";
	
	protected boolean IsType2=false;
	protected int PageHeaderHeight=0;
	protected int PageFooterHeight=0;
	
	protected String mExtraStyle="margin-left: -0.75rem;margin-right: -0.75rem;";
	
	
	protected Map<Integer,String> Opens = new LinkedHashMap<Integer,String>();
	protected Map<Integer,String> Closes = new LinkedHashMap<Integer,String>();
	protected Map<Integer,String> OpensCSS = new LinkedHashMap<Integer,String>();
	protected Map<Integer,String> ClosesCSS = new LinkedHashMap<Integer,String>();
	
	protected Map<Integer,String> RowOpensCSS = new LinkedHashMap<Integer,String>();
	protected Map<Integer,String> RowClosesCSS = new LinkedHashMap<Integer,String>();
	
	protected String mZoom="100";
	
	public boolean IsSafari=false;
	public boolean DoZoomFromLeft=false;
		
	/**
	 * pageBreak: use the ABMaterial.PRINT_PAGEBREAK_ constants, empty string is default 
	 */
	public void Initialize(final BA ba, String eventName, Object callObject, ABMPage page, String id, String css, String pageBreak) {
		this.ID = id;
		this.Type = ABMaterial.UITYPE_REPORTCOMPONENT;
		this.Page = page;
		this._ba = ba;
		this.caller = callObject;
		this.evName = eventName.toLowerCase();
		Body.Initialize();
		CSS=css;
		mPageBreak = pageBreak;
		IsInitialized=true;
	}
	
	public void InitializeType2(final BA ba, String eventName, Object callObject, ABMPage page, String id, String css, String pageBreak) {
		this.ID = id;
		this.Type = ABMaterial.UITYPE_REPORTCOMPONENT;
		this.Page = page;
		this._ba = ba;
		this.caller = callObject;
		this.evName = eventName.toLowerCase();
		Body.Initialize();
		CSS=css;
		mPageBreak = pageBreak;
		IsInitialized=true;
		IsType2 = true;
	}	
	
	public void SetExtraStyle(String style) {
		mExtraStyle = style;
	}
	
	public ABMReportBlock GetBlockByDataValue(String dataValue) {
		int m = BodyMap.getOrDefault(dataValue, -1);
		if (m>-1) {
			ABMReportBlock b = (ABMReportBlock) Body.Get(m);
			return b;
		}
		return null;
	}
			
	public void AddBodyBlock(ABMReportBlock block) {
		if (block.BlockType==2) {
			block.mBA = _ba;
			block.mCaller = caller;
			block.mEventName = evName;
		}
		Body.Add(block);
		BodyMap.put(block.mDataValue1 + ";" + block.mDataValue2 + ";" + block.mDataValue3 + ";" + block.mDataValue4 + ";" + block.mDataValue5, Body.getSize()-1);
		
		String s = "";
		String sCSS = "";
		if (!Opens.containsKey(block.mLineType)) {
			s = "[";
			sCSS = "[";
			for (int i=0;i<block.mVisibleIfOpen.getSize();i++) {
				if (i>0) {
					s = s + ",";
					sCSS = sCSS + ",";
				}
				Boolean v = (Boolean) block.mVisibleIfOpen.Get(i);
				if (v) {
					s = s + "true";
				} else {
					s = s + "false";
				}
				sCSS = sCSS + "'" + (String) block.mClassesOpen.Get(i) + "'";
			}
			s = s + "]";
			sCSS = sCSS + "]";
			Opens.put(block.mLineType, s);
			OpensCSS.put(block.mLineType, sCSS);
			RowOpensCSS.put(block.mLineType, block.mRowClassesOpen);
			s = "[";
			sCSS = "[";
			for (int i=0;i<block.mVisibleIfClosed.getSize();i++) {
				if (i>0) {
					s = s + ",";
					sCSS = sCSS + ",";
				}
				Boolean v = (Boolean) block.mVisibleIfClosed.Get(i);
				if (v) {
					s = s + "true";
				} else {
					s = s + "false";
				}
				sCSS = sCSS + "'" + (String) block.mClassesClosed.Get(i) + "'";
			}
			s = s + "]";
			sCSS = sCSS + "]";
			Closes.put(block.mLineType, s);
			ClosesCSS.put(block.mLineType, sCSS);
			RowClosesCSS.put(block.mLineType, block.mRowClassesClosed);
		}
	}
	
	public void AddBodySubReport(ABMReport report) {
		ABMReportBlock block = new ABMReportBlock();
		block.setSubReport(report);
		AddBodyBlock(block);
	}
	
	public void AddBodyBlockPagebreak(ABMReportBlock block) {
		block.IsPageBreak = true;
		AddBodyBlock(block);
	}
	
	@Override
	protected void ResetTheme() {
		// not used
	}
	
	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase();
	}
	
	@Override
	@RaisesSynchronousEvents
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
		RefreshInternal(true);
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
		ABMaterial.ChangeVisibility(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), mVisibility);
		if (mIsOnlyForPrintClass.equals("")) {
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "only-print");
		} else {
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "only-print");
		}
		
	}
	
	@Override
	protected void Prepare() {
		super.Prepare();		
	}
	
	@Override
	@RaisesSynchronousEvents
	protected void FirstRun() {	
		StringBuilder s = new StringBuilder();
		if (!CSS.equals("")) {
			s.append("var head = document.getElementsByTagName('head')[0];");
			s.append("var sty = document.getElementById('" + evName.toLowerCase() + "-css');");
			s.append("if (sty) {");
			s.append("sty[0].innerHTML=\"" + CSS.replaceAll("\"", "'").replaceAll("\t", " ").replaceAll("\r",  "").replaceAll("\n",  " ") + "\";");
			s.append("} else {");
			s.append("var s = document.createElement('style');");
			s.append("s.setAttribute('type', 'text/css');");
			s.append("s.setAttribute('id', '" + evName.toLowerCase() + "-css');");
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
						Page.ws.Flush();				
					}
				} catch (IOException e) {			
				
				}
			}
			
		}
		if (Header!=null) { // Hier moet een andere check komen
			Header.FirstRun();
		}
		for (int i=0;i<Body.getSize();i++) {
			ABMReportBlock block = (ABMReportBlock) Body.Get(i);
			block.FirstRun();
		}
		if (Footer!=null) {
			Footer.FirstRun();
		}
		
		s = new StringBuilder();		
		for (Entry<Integer,String>entry: Opens.entrySet() ) {
			s.append("currABMrep['" + entry.getKey() + "_O'] = " + entry.getValue() + ";");
			s.append("currABMrep['" + entry.getKey() + "_OS'] = " + OpensCSS.get(entry.getKey()) + ";");
			s.append("currABMrep['" + entry.getKey() + "_C'] = " + Closes.get(entry.getKey()) + ";");
			s.append("currABMrep['" + entry.getKey() + "_CS'] = " + ClosesCSS.get(entry.getKey()) + ";");
			s.append("currABMrep['" + entry.getKey() + "_ROS'] = '" + RowOpensCSS.get(entry.getKey()) + "';");
			s.append("currABMrep['" + entry.getKey() + "_RCS'] = '" + RowClosesCSS.get(entry.getKey()) + "';");
		}
				
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
		
		_ba.raiseEvent(caller, evName + "_firstrun", new Object[] {Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase()});		
		
		super.FirstRun();
		
	}
	
	public void SetZoom(int zoom) {
		mZoom = "" + zoom;
		if (Page.ws!=null) {
			String s = "rapsetzoom($('.abmrepparent'), 'zoom" + mZoom + "')";
			Page.ws.Eval(s, null);
			try {
				if (Page.ws.getOpen()) {
					Page.ws.Flush();				
				}
			} catch (IOException e) {			
			
			}
		}	
		
	}
		
	@Override	
	@RaisesSynchronousEvents
	protected String Build() {
		_ba.raiseEvent(caller, evName + "_build", new Object[] {Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase()});
		HTML = RunInternal(Page);
		if (DoZoomFromLeft) {
			HTML = "<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + mPageBreak + "\" class=\"abmrepparent zoomL zoom" + mZoom + " " + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() + "\">" + HTML + "</div>";
		} else {
			HTML = "<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + mPageBreak + "\" class=\"abmrepparent zoomC zoom" + mZoom + " " + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() + "\">" + HTML + "</div>";
		}	
		IsBuild = true;		
		return HTML;
	}	
	
	@Override
	protected ABMComponent Clone() {
		return null;		
	}	
			
	public String RunInternal(ABMPage page) {
		
		StringBuilder s = new StringBuilder();
		
		
		if (IsType2) {
			String visH = "";
			String visF="NONE";
			s.append("<table>");
			if (Header!=null) {
				if (Header.BlockType!=0) {
					s.append("<thead><tr><td style='padding: 5px 0px'><div class='report-innerheader " + Header.mExtraClasses + "'>&nbsp;</div>");
					Header.IsHeader=false;
					Header.IsType2=false;
					Header.setIsPrintable(IsSafari);
					visH=Header.Visibility;
					
					s.append(Header.Build(page));
					s.append("</td></tr></thead>");
				}
			}
			s.append("<tbody><tr><td style='padding: 0px 0px'><div class='reportcontent'>");
			String prevData="";
			for (int i=0;i<Body.getSize();i++) {
				ABMReportBlock block = (ABMReportBlock) Body.Get(i);
				if (block.IsPageBreak) {
					s.append("</div></td></tr></tbody>");
					if (Footer!=null) {
						if (Footer.BlockType!=0) {
							s.append("<tfoot class=\"repinner\" " + prevData + "><tr><td style='padding: 5px 0px'><div class='report-innerfooter " + Footer.mExtraClasses + "'>&nbsp;</div>");
							Footer.IsFooter=true;
							Footer.IsType2=true;
							Footer.setIsPrintable(false);
							if (visF.equals("NONE")) {
								visF=Footer.Visibility;
							}
							
							s.append(Footer.Build(page));
							s.append("</td></tr></tfoot>");
						}
					}
					s.append("</table>");
					s.append(block.Build(page));
					s.append("<table>");
					if (Header!=null) {
						if (Header.BlockType!=0) {
							s.append("<thead class=\"repinner\" " + prevData + "><tr><td style='padding: 5px 0px'><div class='report-innerheader " + Header.mExtraClasses + "'>&nbsp;</div>");
							Header.IsHeader=false;
							Header.IsType2=false;
							Header.setIsPrintable(IsSafari);
							
							s.append(Header.Build(page));
							s.append("</td></tr></thead>");
						}
					}
					s.append("<tbody><tr><td style='padding: 0px 0px'><div class='reportcontent'>");
				} else {
					s.append(block.Build(page));
					prevData = block.GetDataValue();
				}
				
			}
			if (visF.equals("NONE")) {
				visF= "";
			}
			s.append("</div></td></tr></tbody>");
			if (Footer!=null) {
				if (Footer.BlockType!=0) {
					s.append("<tfoot><tr><td style='padding: 5px 0px'><div class='report-innerfooter " + Footer.mExtraClasses + "'>&nbsp;</div>");
					Footer.IsFooter=true;
					Footer.IsType2=true;
					Footer.setIsPrintable(false);
					visF=Footer.Visibility;
					
					s.append(Footer.Build(page));
					s.append("</td></tr></tfoot>");
				}
			}
			s.append("</table>");
			if (!IsSafari) {
				if (Header!=null) {
					if (Header.BlockType!=0) {
						Header.IsHeader=true;
						Header.IsType2=true;
						Header.setIsPrintable(true);
						Header.Visibility = "hide";

						s.append(Header.Build(page));
						Header.Visibility=visH;
					}
				}
				if (Footer!=null) {
					if (Footer.BlockType!=0) {
						Footer.IsFooter=true;
						Footer.IsType2=true;
						Footer.setIsPrintable(true);
						Footer.Visibility = "hide";
						s.append(Footer.Build(page));
						Footer.Visibility = visF;
					}
				}
			}
		} else {		
			if (Header!=null) {
				if (Header.BlockType!=0) {
					s.append(Header.Build(page));
				}
			}
			for (int i=0;i<Body.getSize();i++) {
				ABMReportBlock block = (ABMReportBlock) Body.Get(i);
				s.append(block.Build(page));
			}
			if (Footer!=null) {
				if (Header.BlockType!=0) {
					s.append(Footer.Build(page));
				}
			}
		}	
		res = s.toString();
		return res;
	}
}
