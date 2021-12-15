package com.ab.abmaterial;

import java.util.ArrayList;
import java.util.List;

import anywheresoftware.b4a.BA;

public class ABMComposerHeader {
	protected List<ABMComposerPart>Parts = new ArrayList<ABMComposerPart>();
	protected boolean IsDirty=true;
	protected String GroupID="";
	
	protected ABMPage Page=null;
	
	protected int Counter=0;
	
	public ABMComposerHeader(ABMPage page) {
		Page=page;
	}
	
	public void AddString(String text) {
		ABMComposerPart p = new ABMComposerPart();
		p.TextValue = text;
		Counter++;
		p.TextID=GroupID + "-" + Counter;
		Parts.add(p);
	}
	
	public void AddComponent(ABMComponent comp) {
		ABMComposerPart p = new ABMComposerPart();
		p.CompValue = comp;
		p.Type = 1;
		Parts.add(p);
	}
	
	public void Remove(int index) {
		if (index>=Parts.size()) {
			BA.LogError("index is greater than size!");
			return;
		}
		Parts.remove(index);
	}
	
	public void InsertStringBefore(int index, String text) {
		if (index>=Parts.size()) {
			AddString(text);
			return;
		}
		ABMComposerPart p = new ABMComposerPart();
		p.TextValue = text;
		Parts.add(index, p);
	}
	
	public void InsertComponentBefore(int index, ABMComponent comp) {
		if (index>=Parts.size()) {
			AddComponent(comp);
			return;
		}
		ABMComposerPart p = new ABMComposerPart();
		p.CompValue = comp;
		p.Type = 1;
		Parts.add(index, p);
	}
	
	public void Clear() {
		for(int i=0;i<Parts.size();i++) {
			if (Parts.get(i).Type==0) {
			
			} else {
				ABMComponent comp = (ABMComponent) Parts.get(i).CompValue;
				comp.RemoveMe();
				comp.CleanUp();					
			}
		}			
		Parts.clear();
	}
	
	protected String Build(int BodyType, String BlockThemeName) {
		IsDirty=false;
		StringBuilder s = new StringBuilder();
		
		s.append("<div id=\"" + GroupID.toLowerCase() + "-hdr\" class=\"abmcpr-hdr " + GroupID.toLowerCase() + "\">");
		if (BodyType==0) {
			s.append("<div class=\"abmcpr-empw\">");
		} else {
			s.append("<div id=\"" + GroupID.toLowerCase() + "-toggle\" class=\"abmcpr-arrw abmcpr-arrw" + BlockThemeName.toLowerCase() + "\" data-for=\"" + GroupID.toLowerCase() + "\">");
			s.append("<div id=\"" + GroupID.toLowerCase() + "-hdrarr\" class=\"abmcpr-arr abmcpr-arr" + BlockThemeName.toLowerCase() + " abmcpr-down\"></div>");
		}
		s.append("</div>");
		s.append("<div class=\"abmcpr-cont\">");
		
		for (int i=0;i<Parts.size();i++) {
			if (Parts.get(i).Type==0) {
				s.append("<div id=\"" + Parts.get(i).TextID + "\" class=\"abmcpr-txt\">" + BuildBodyText(Parts.get(i).TextValue) + "</div>");
			} else {
				ABMComponent comp = Parts.get(i).CompValue;
				s.append(comp.Build());
				comp.RunAllFirstRuns();
			}
			Parts.get(i).IsBuild=true;
		}
		s.append("</div>");
		s.append("</div>");
		
		return s.toString();
	}
	
	protected void SetTheme(String oBlockThemeName, String BlockThemeName) {
		ABMaterial.RemoveClass(Page, GroupID.toLowerCase() + "-hdrarr", "abmcpr-arr" + oBlockThemeName);
		ABMaterial.AddClass(Page, GroupID.toLowerCase() + "-hdrarr", "abmcpr-arr" + BlockThemeName);
	}
	
	protected void Refresh(int BodyType, String BlockThemeName) {
		if (IsDirty) {
			
			String prevID="";
			int Counter=0;
			for (int i=0;i<Parts.size();i++) {
				if (Parts.get(i).Type==0) {
					if (!Parts.get(i).IsBuild) {
						if (Counter==0) {
							ABMaterial.AddHTML(Page, GroupID.toLowerCase() + "-hdr", "<div id=\"" + Parts.get(i).TextID + "\" class=\"abmcpr-txt\">" + BuildBodyText(Parts.get(i).TextValue) + "</div>");
							prevID = Parts.get(i).TextID;
						} else {
							ABMaterial.InsertHTMLAfter(Page, prevID, "<div id=\"" + Parts.get(i).TextID + "\" class=\"abmcpr-txt\">" + BuildBodyText(Parts.get(i).TextValue) + "</div>");
							prevID = Parts.get(i).TextID;
						}	
					}
					Parts.get(i).IsBuild = true;
				} else {
					ABMComponent comp = Parts.get(i).CompValue;
					if (comp.IsBuild) {						
						switch (comp.Type) {								
						case ABMaterial.UITYPE_ABMCONTAINER:
							ABMContainer cont = (ABMContainer) comp;
							if (!cont.HadFirstRun) {
								cont.RunAllFirstRunsInternal(false);
								String s = cont.RunInitialAnimation();
								if (!s.equals("")) {
									if (Page.ws.getOpen())
									Page.ws.Eval(s, null);								
								}							
							}
							cont.RefreshInternalExtra(false, false,false);
							break;								
						case ABMaterial.UITYPE_FLEXWALL:
							ABMFlexWall wall = (ABMFlexWall) comp;	
							if (!wall.HadFirstRun) {												
								wall.RunAllFirstRunsInternal(false);													
							}
							wall.RefreshInternalExtra(false, false, false);
							break;						
						case ABMaterial.UITYPE_TABEL:
							ABMTable table = (ABMTable) comp;
							if (!table.HadFirstRun) {												
								table.FirstRunInternal(false);														
							}						
							table.RefreshInternal(false);
							break;
						case ABMaterial.UITYPE_CUSTOMCOMPONENT:
							ABMCustomComponent ccomp = (ABMCustomComponent) comp;
							if (!ccomp.HadFirstRun) {												
								ccomp.RunAllFirstRuns();													
							}						
							ccomp.RefreshInternal(false);
							break;
						case ABMaterial.UITYPE_TABS:
							ABMTabs tabs = (ABMTabs) comp;
							if (!tabs.HadFirstRun) {												
								tabs.RunAllFirstRuns();													
							}
							tabs.RefreshInternal(false);
							break;
						default:
							if (!comp.HadFirstRun) {
								comp.FirstRun();											
							}
							comp.RefreshInternal(false);	
						}
						
						prevID = comp.RootID();
					} else {	
						if (Counter==0) {
							ABMaterial.AddHTML(Page, GroupID.toLowerCase() + "-hdr", comp.Build());
							prevID = comp.RootID();
						} else {
							if (comp.beforeID.equals("")) {					
								ABMaterial.InsertHTMLAfter(Page, comp.ParentString + prevID, comp.Build());
								prevID = comp.RootID();
							} else {				
								ABMaterial.InsertHTMLBefore(Page, comp.ParentString + comp.ArrayName.toLowerCase() + comp.beforeID, comp.Build());
							}
						}		
						
						switch (comp.Type) {
							case ABMaterial.UITYPE_ABMCONTAINER:
								ABMContainer cont = (ABMContainer) comp;
								cont.RunAllFirstRunsInternal(false);
								String s = cont.RunInitialAnimation();
								if (!s.equals("")) {
									Page.ws.Eval(s, null);							
								}
								break;
							case ABMaterial.UITYPE_FLEXWALL:
								ABMFlexWall wall = (ABMFlexWall) comp;						
								wall.RunAllFirstRunsInternal(false);
								break;
							case ABMaterial.UITYPE_TABEL:						
								ABMTable table = (ABMTable) comp;						
								table.FirstRunInternal(false);						
								break;
							case ABMaterial.UITYPE_CUSTOMCOMPONENT:
								ABMCustomComponent ccomp = (ABMCustomComponent) comp;																	
								ccomp.RunAllFirstRuns();
								break;
							case ABMaterial.UITYPE_TABS:
								ABMTabs tabs = (ABMTabs) comp;																	
								tabs.RunAllFirstRuns();
								break;
							default:
								comp.RunAllFirstRuns();
						}
						
					}			
					
					Counter++;
				}
			}	
		}
		IsDirty=false;
	}
	
	protected String BuildBodyText(String Text) {
		StringBuilder s = new StringBuilder();		
	
		
		String v = ABMaterial.HTMLConv().htmlEscape(Text, Page.PageCharset);
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
}
