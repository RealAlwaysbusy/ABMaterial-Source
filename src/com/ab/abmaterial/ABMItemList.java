package com.ab.abmaterial;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")  
@ShortName("ABMItemList")
public class ABMItemList  extends ABMComponent {	
	private static final long serialVersionUID = -2910962292452905807L;
	protected String LabelListType = "disc";
	
	protected Map<String,ABMComponent> Labels = new LinkedHashMap<String,ABMComponent>();
	
	public String PaddingLeft="30px"; //"0.75rem";
	public String PaddingRight="30px"; //"0.75rem";
	public String PaddingTop=""; //"0rem";
	public String PaddingBottom=""; //"0rem";
	public String MarginTop="10px"; //"0px";
	public String MarginBottom="15px"; //"0px";
	public String MarginLeft=""; //"0px";
	public String MarginRight=""; //"0px";
	
	public String BulletColor="black";
	public String BulletColorIntensity="";
	
	/**
	 * use the ABM.ITEMLIST_TYPE_ constants	
	 */
	public void Initialize(ABMPage page, String id, String labelListType) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_ITEMLIST;
		this.LabelListType = labelListType;		
		IsInitialized=true;		
	}	
	
	/**
	 * Has no theme
	 */
	@Override
	protected void ResetTheme() {
		
	}
	
	@Override
	protected String RootID() {		
		return ArrayName.toLowerCase() + ID.toLowerCase();
	}
	
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}
	
	/**
	 * 
	 * Note: You can NOT use addItem AFTER the ABMItemList has been added to the page!  
	 */
	public void AddItem(ABMComponent item, String arrayName) {
		item.ParentString = this.ArrayName.toLowerCase() + ID.toLowerCase() + "-";
		item.AddArrayName(arrayName);
		Labels.put(arrayName.toLowerCase() + item.ID.toLowerCase(),item);
	}
	
	public ABMComponent GetItem(String itemID) {
		return Labels.getOrDefault(itemID.toLowerCase(), null);
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
		super.FirstRun();
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		// TODO ExtraClasses not working
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
		for (Entry<String,ABMComponent> comp: Labels.entrySet()) {
			if (comp.getValue().IsBuild) {					
				comp.getValue().RefreshInternal(false);
									
			} 			 
		}
		
		super.Refresh();
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("LabelList Refresh: " + ID);}
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
		String rtl="";
		if (this.getRightToLeft()) {
			rtl = " abmrtl ";
			
		}
		s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"" + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + rtl + super.BuildExtraClasses() + "\">");
		switch (LabelListType) {
		case ABMaterial.ITEMLIST_TYPE_DISC:
			s.append("<ul style=\"list-style-type:disc;" + BuildStyle() + "\">");
			for (Entry<String,ABMComponent> comp: Labels.entrySet()) {
				if (comp.getValue().Type==ABMaterial.UITYPE_ITEMLIST) {
					s.append(comp.getValue().Build());
				} else {
					s.append("<li style=\"list-style-type:disc\">" + comp.getValue().Build() + "</li>");
				}
			}
			s.append("</ul>");
			break;
		case ABMaterial.ITEMLIST_TYPE_CIRCLE:
			s.append("<ul style=\"list-style-type:circle;" + BuildStyle() + "\">");
			for (Entry<String,ABMComponent> comp: Labels.entrySet()) {
				if (comp.getValue().Type==ABMaterial.UITYPE_ITEMLIST) {
					s.append(comp.getValue().Build());
				} else {
					s.append("<li style=\"list-style-type:circle\">" + comp.getValue().Build() + "</li>");
				}
			}
			s.append("</ul>");
			break;
		case ABMaterial.ITEMLIST_TYPE_SQUARE:
			s.append("<ul style=\"list-style-type:square;" + BuildStyle() + "\">");
			for (Entry<String,ABMComponent> comp: Labels.entrySet()) {
				if (comp.getValue().Type==ABMaterial.UITYPE_ITEMLIST) {
					s.append(comp.getValue().Build());
				} else {
					s.append("<li style=\"list-style-type:square\">" + comp.getValue().Build() + "</li>");
				}
			}
			s.append("</ul>");
			break;
		case ABMaterial.ITEMLIST_TYPE_NONE:
			s.append("<ul style=\"list-style-type:none;" + BuildStyle() + "\">");
			for (Entry<String,ABMComponent> comp: Labels.entrySet()) {
				if (comp.getValue().Type==ABMaterial.UITYPE_ITEMLIST) {
					s.append(comp.getValue().Build());
				} else {
					s.append("<li style=\"list-style-type:none\">" + comp.getValue().Build() + "</li>");
				}
			}
			s.append("</ul>");
			break;
		case ABMaterial.ITEMLIST_TYPE_NUMBERS:
			s.append("<ol type=\"1\" style=\"" + BuildStyle() + "\">");
			for (Entry<String,ABMComponent> comp: Labels.entrySet()) {
				if (comp.getValue().Type==ABMaterial.UITYPE_ITEMLIST) {
					s.append(comp.getValue().Build());
				} else {
					s.append("<li style=\"\">" + comp.getValue().Build() + "</li>");
				}
			}
			s.append("</ol>");
			break;
		case ABMaterial.ITEMLIST_TYPE_ALPHABET_LOWERCASE:
			s.append("<ol type=\"a\" style=\"" + BuildStyle() + "\">");
			for (Entry<String,ABMComponent> comp: Labels.entrySet()) {
				if (comp.getValue().Type==ABMaterial.UITYPE_ITEMLIST) {
					s.append(comp.getValue().Build());
				} else {
					s.append("<li style=\"\">" + comp.getValue().Build() + "</li>");
				}
			}
			s.append("</ol>");
			break;
		case ABMaterial.ITEMLIST_TYPE_ALPHABET_UPPERCASE:
			s.append("<ol type=\"A\" style=\"" + BuildStyle() + "\">");
			for (Entry<String,ABMComponent> comp: Labels.entrySet()) {
				if (comp.getValue().Type==ABMaterial.UITYPE_ITEMLIST) {
					s.append(comp.getValue().Build());
				} else {
					s.append("<li style=\"\">" + comp.getValue().Build() + "</li>");
				}
			}
			s.append("</ol>");
			break;
		case ABMaterial.ITEMLIST_TYPE_ROMAN_LOWERCASE:
			s.append("<ol type=\"i\" style=\"" + BuildStyle() + "\">");
			for (Entry<String,ABMComponent> comp: Labels.entrySet()) {
				if (comp.getValue().Type==ABMaterial.UITYPE_ITEMLIST) {
					s.append(comp.getValue().Build());
				} else {
					s.append("<li style=\"\">" + comp.getValue().Build() + "</li>");
				}
			}
			s.append("</ol>");
			break;
		case ABMaterial.ITEMLIST_TYPE_ROMAN_UPPERCASE:
			s.append("<ol type=\"I\" style=\"" + BuildStyle() + "\">");
			for (Entry<String,ABMComponent> comp: Labels.entrySet()) {
				if (comp.getValue().Type==ABMaterial.UITYPE_ITEMLIST) {
					s.append(comp.getValue().Build());
				} else {
					s.append("<li style=\"\">" + comp.getValue().Build() + "</li>");
				}
			}
			s.append("</ol>");
			break;
		}		
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
	protected String BuildStyle() {			
		StringBuilder s = new StringBuilder();		
		
		if (MarginTop!="") s.append(" margin-top: " + MarginTop + ";");
		if (MarginBottom!="") s.append(" margin-bottom: " + MarginBottom + ";");
		if (MarginLeft!="") s.append(" margin-left: " + MarginLeft + ";");
		if (MarginRight!="") s.append(" margin-right: " + MarginRight + ";");
		if (PaddingTop!="") s.append(" padding-top: " + PaddingTop + ";");
		if (PaddingBottom!="") s.append(" padding-bottom: " + PaddingBottom + ";");
		if (PaddingLeft!="") s.append(" padding-left: " + PaddingLeft + ";");
		if (PaddingRight!="") s.append(" padding-right: " + PaddingRight + ";");
		s.append(" color: " + ABMaterial.GetColorStrMap(BulletColor, BulletColorIntensity));
		
		return s.toString();
	}
		
	
	@Override
	protected ABMComponent Clone() {
		ABMItemList c = new ABMItemList();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;
		c.LabelListType = LabelListType;
		return c;
	}
}
