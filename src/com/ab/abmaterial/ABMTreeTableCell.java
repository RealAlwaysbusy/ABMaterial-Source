package com.ab.abmaterial;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.SimpleFuture;

@Author("Alain Bailleul")  
@ShortName("ABMTreeTableCell")
public class ABMTreeTableCell implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2723696630550266654L;
	protected ABMComponent value=null;
	protected String StringValue="";
	protected String IconName="";
	protected String IconAwesomeExtra="";
	public String theme="";
	public String id="";
	public SimpleFuture j=null;
	public boolean IsEditable=false;
	public int ColOffset=0;
	public int ColSpan=1;
	public int Level=0;
	public boolean IsVisible=true;
	protected String componentId="";
	public boolean IsInitialized=false;
	
	public String GetStringValue() {
		if (IsEditable) {
			try {
				StringValue = (String) j.getValue();
					
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		return StringValue;
	}
	
	public String GetCleanStringValue(ABMPage page) {
			String vv = ABMaterial.HTMLConv().htmlEscape(StringValue, page.PageCharset);
			vv=vv.replaceAll("(\r\n|\n\r|\r|\n)", "<br>");
			vv=vv.replace("{B}", "");
			vv=vv.replace("{/B}", "");
			vv=vv.replace("{I}", "");
			vv=vv.replace("{/I}", "");
			vv=vv.replace("{U}", "");
			vv=vv.replace("{/U}", "");
			vv=vv.replace("{SUB}", "");
			vv=vv.replace("{/SUB}", "");
			vv=vv.replace("{SUP}", "");
			vv=vv.replace("{/SUP}", "");
			vv=vv.replace("{BR}", "");
			vv=vv.replace("{WBR}", "<wbr>");
			vv=vv.replace("{NBSP}", "");
			vv=vv.replace("{AL}", "");
			vv=vv.replace("{AT}", "");
			vv=vv.replace("{/AL}", "");				
			
			while (vv.indexOf("{C:") > -1) {
				int vvi = vv.indexOf("{C:");
				vv=vv.replaceFirst("\\{C:", "<span style=\"color:");
				vv=vv.substring(0,vvi) + vv.substring(vvi).replaceFirst("\\}", "\">");
				vv=vv.replaceFirst("\\{/C}", "</span>");	
			}

			vv = vv.replace("{CODE}", "<div class=\"abmcode\"><pre><code>");
			vv = vv.replace("{/CODE}", "</code></pre></div>");
			while (vv.indexOf("{ST:") > -1) {
				int vvi = vv.indexOf("{ST:");
				vv=vv.replaceFirst("\\{ST:", "<span style=\"");			
				vv=vv.substring(0,vvi) + vv.substring(vvi).replaceFirst("\\}", "\">");
				vv=vv.replaceFirst("\\{/ST}", "</span>");	
			}	
			
			int start = vv.indexOf("{IC:");
			while (start > -1) {
				int stop = vv.indexOf("{/IC}");
				String vvv = "";
				if (stop>0) {
					vvv = vv.substring(start, stop+5);
				} else {
					break;
				}
				 
				String IconColor = vvv.substring(4, 11);
				String IconName = vvv.substring(12,vvv.length()-5);
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
					repl = "<i>" + page.svgIconmap.getOrDefault(IconName.toLowerCase(), "") + "</i>"; 
					break;
				default:
					repl = "<i style=\"color: " + IconColor + "\" class=\"material-icons\">" + IconName + "</i>";
				}
				vv=vv.replace(vvv,repl );
				start = vv.indexOf("{IC:");
			}
			return vv.toString();		
	}
	
	public void Initialize(String id, String stringValue, int colOffset, int colSpan, boolean isEditable, String theme) {
		this.id=id;
		this.StringValue=stringValue;
		this.IsEditable = isEditable;
		this.theme = theme;
		this.ColOffset = colOffset;
		this.ColSpan=colSpan;
		IsInitialized=true;
	}
	
	public void InitializeAsIcon(String id, String iconName, String iconAwesomeExtra, int colOffset, int colSpan, String theme) {
		this.id=id;
		this.IconName = iconName;
		this.IconAwesomeExtra = iconAwesomeExtra;
		this.IsEditable = false;
		this.theme = theme;
		this.ColOffset = colOffset;
		this.ColSpan=colSpan;	
		IsInitialized=true;
	}
	
	public void InitializeAsComponent(String id, ABMComponent component, String arrayName, int colOffset, int colSpan, String theme) {
		this.id=id;
		component.ArrayName = arrayName;
		this.value = component;		
		this.componentId = component.ArrayName.toLowerCase() + component.ID.toLowerCase();
		this.IsEditable = false;
		this.theme = theme;
		this.ColOffset = colOffset;
		this.ColSpan=colSpan;	
		IsInitialized=true;		
	}
	
	protected void CleanUp() {
		if (value!=null) {
			value.RemoveMe();
			value.CleanUp();
		}
	}
	
	protected ABMTreeTableCell Clone() {
		ABMTreeTableCell v = new ABMTreeTableCell();
		v.StringValue=StringValue;
		v.theme = theme;
		v.j = j;
		v.ColOffset = ColOffset;
		v.ColSpan = ColSpan;
		v.Level=Level;
		v.IconName=IconName;
		v.IconAwesomeExtra=IconAwesomeExtra;
		v.IsEditable=IsEditable;
		v.IsVisible=IsVisible;
		v.id=id;
		v.componentId=componentId;
		if (value!=null) {
			v.value=value.Clone();
		}
		
		return v;			
	}
}


