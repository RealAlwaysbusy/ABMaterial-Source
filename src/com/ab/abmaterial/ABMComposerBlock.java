package com.ab.abmaterial;

import anywheresoftware.b4a.BA.ShortName;

@ShortName("ABMComposerBlock")
public class ABMComposerBlock {
	public ABMComposerHeader Header;  
	public ABMComposerBody Body;
	public ABMComposerFooter Footer;
	protected boolean IsDirty=true;
	
	protected String BlockTheme="default";
	protected String oBlockTheme="default";
	protected String oLabelText="";
	protected String LabelText="";
	protected String oIconImageSrc="";
	protected String IconImageSrc="";
	protected int IconWidth=18;
	protected int IconHeight=18;
	protected String IconMarginTop="";
	
	protected String ID="";
	
	protected boolean IsBuild=false;
	
	protected ABMPage Page=null;
	
	protected String mParentString="";
	protected String mArrayName="";
	protected String mMarginLeft="";
	
	public void Initialize(ABMPage page, String ID, String blockTheme) {
		this.ID = ID;
		if (!blockTheme.equals("")) {
			BlockTheme = blockTheme;
		}
		Page = page;
		Header=new ABMComposerHeader(Page);  
		Body=new ABMComposerBody(Page);
		Footer=new ABMComposerFooter(Page);
		Header.GroupID = ID;
		Body.GroupID = ID;
		Footer.GroupID = ID;
	}
	
	protected void SetParams(String parentString, String arrayName, String marginLeft, String ID) {
		mParentString = parentString;
		mArrayName = arrayName;
		mMarginLeft = marginLeft;
		Footer.SetParams(parentString, arrayName, ID);		
	}
	
	public void SetLabelText(String labelText) {
		IsDirty=true;
		oLabelText=LabelText;
		LabelText = labelText;
	}
	
	/**
	 * 
	 * iconImageSrc: a link to a image file like a png
	 * iconWidth: default = 18
	 * iconHeight: default = 18
	 * iconMarginTop: default = "10px"
	 */
	public void SetIcon(String iconImageSrc, int iconWidth, int iconHeight, String iconMarginTop) {
		IsDirty=true;
		oIconImageSrc=IconImageSrc;
		IconImageSrc = iconImageSrc;
		IconWidth=iconWidth;
		IconHeight=iconHeight;
		IconMarginTop=iconMarginTop;
	}
	
	public void SetTheme(String blockTheme) {
		IsDirty=true;
		oBlockTheme = BlockTheme;
		BlockTheme = blockTheme;
	}
	
	public void Refresh() {
		if (IsDirty) {
			if (!oBlockTheme.equals("")) {
				ABMaterial.RemoveClass(Page, mParentString + mArrayName.toLowerCase() + ID.toLowerCase() + "-sb", "abmcpr-sb" + oBlockTheme.toLowerCase());
				ABMaterial.AddClass(Page, mParentString + mArrayName.toLowerCase() + ID.toLowerCase() + "-sb", "abmcpr-sb" + BlockTheme.toLowerCase());
				ABMaterial.RemoveClass(Page, mParentString + mArrayName.toLowerCase() + ID.toLowerCase() + "-bg", "abmcpr-bg" + oBlockTheme.toLowerCase());
				ABMaterial.AddClass(Page, mParentString + mArrayName.toLowerCase() + ID.toLowerCase() + "-bg", "abmcpr-bg" + BlockTheme.toLowerCase());
				ABMaterial.RemoveClass(Page, mParentString + mArrayName.toLowerCase() + ID.toLowerCase() + "-lbl", "abmcpr-lbl" + oBlockTheme.toLowerCase());
				ABMaterial.AddClass(Page, mParentString + mArrayName.toLowerCase() + ID.toLowerCase() + "-lbl", "abmcpr-lbl" + BlockTheme.toLowerCase());					
			}	
			if (!oIconImageSrc.equals(IconImageSrc)) {
				ABMaterial.ReplaceMyInnerHTML(Page, mParentString + mArrayName.toLowerCase() + ID.toLowerCase() + "-icn", "<img width=\"" + IconWidth + "\" height=\"" + IconHeight + "\" src=\"" + IconImageSrc + "\" style=\"margin-top: " + IconMarginTop + "\">");
			}
			if (!oLabelText.equals(LabelText)) {
				ABMaterial.SetProperty(Page, mParentString + mArrayName.toLowerCase() + ID.toLowerCase() + "-lbl", "data-label", LabelText);
			}
			Header.SetTheme(oBlockTheme, BlockTheme);
			Footer.SetTheme(oBlockTheme, BlockTheme);
			oLabelText = LabelText;
			oIconImageSrc = IconImageSrc;
			oBlockTheme="";
			IsDirty=false;
		}
		Header.Refresh(Body.Parts.size(), BlockTheme);
		Body.Refresh();
		Footer.Refresh(BlockTheme);
	}
	
	protected String Build(String extra) {
		StringBuilder s = new StringBuilder();
		
		s.append("<div id=\"" + ID.toLowerCase() + "\" class=\"abmcpr-sec abmcpr-sec" + extra + "\" style=\"margin-left: " + mMarginLeft + ";\">");
		s.append("<div id=\"" + ID.toLowerCase() + "-sb\" class=\"abmcpr-sb abmcpr-sb" + extra + " abmcpr-sb" + BlockTheme.toLowerCase() + "\">");
		s.append("<div id=\"" + ID.toLowerCase() + "-bg\" class=\"abmcpr-bg abmcpr-bg" + BlockTheme.toLowerCase() + "\">");
		s.append("<span id=\"" + ID.toLowerCase() + "-lbl\" class=\"abmcpr-lbl abmcpr-lbl" + BlockTheme.toLowerCase() + "\" data-label=\"" + LabelText + "\"></span>");
		oLabelText = LabelText;
		s.append("<span id=\"" + ID.toLowerCase() + "-icn\">");
		if (!IconImageSrc.equals("")) {
			s.append("<img width=\"" + IconWidth + "\" height=\"" + IconHeight + "\" src=\"" + IconImageSrc + "\" style=\"margin-top: " + IconMarginTop + "\">");
			oIconImageSrc=IconImageSrc;
		}
		s.append("</span>");
		s.append("</div>");
		s.append("</div>");
		s.append("<div class=\"abmcpr-flds abmcpr-flds" + BlockTheme.toLowerCase() + "\">");
		s.append(Header.Build(Body.Parts.size(), BlockTheme));
		s.append(Body.Build());
		s.append(Footer.Build(BlockTheme));
		s.append("</div>"); 
		s.append("</div>");  
		
		IsBuild=true;
		
		return s.toString();
	}
	
	protected void CleanUp() {
		Page = null;
		Header.Page = null;
		Body.Page = null;
		Footer.Page = null;
	}
}
