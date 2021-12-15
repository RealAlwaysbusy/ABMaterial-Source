package com.ab.abmaterial;

import java.io.IOException;
//import java.io.IOException;
import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")  
@ShortName("ABMSocialShare")
public class ABMSocialShare extends ABMComponent {
	
	private static final long serialVersionUID = -6119906021049704331L;
	protected String TwitterVia="";
	protected String TwitterHashTags="";
	protected String PintrestMediaUrl="";
	
	protected String FacebookAppID="";
	protected String FacebookCaption="";
	protected String FacebookDescription="";
	protected String FacebookRedirectUrl="";
	protected String FacebookImageUrl="";
	
	protected String Url="";
	protected String Text="";
	protected String ButtonType=ABMaterial.SOCIALSHAREBUTTONTYPE_BASIC;
	protected String ShowLabel="";
	protected String ShowCount="";
	protected boolean UsePopup=false;
	
	protected List<String> Networks = new ArrayList<String>();
	
	protected ABMComponent JumpToComponent=null;
			
	public void Initialize(ABMPage page, String id, String url, ABMComponent jumpToComponent, String text, String buttonType) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_SOCIALSHARE;	
		this.Url = url;
		this.Text = text;
		this.JumpToComponent=jumpToComponent;
		switch (buttonType) {
		case ABMaterial.SOCIALSHAREBUTTONTYPE_BASIC:
			ShowLabel="";
			ShowCount="";
			break;
		case ABMaterial.SOCIALSHAREBUTTONTYPE_ICONSONLY:
			ShowLabel="false";
			ShowCount="false";
			break;
		case ABMaterial.SOCIALSHAREBUTTONTYPE_ICONSCOUNTINSIDE:
			ShowLabel="false";
			ShowCount="'inside'";
			break;
		case ABMaterial.SOCIALSHAREBUTTONTYPE_BUTTONLABEL:
			ShowLabel="true";
			ShowCount="false";
			break;
		case ABMaterial.SOCIALSHAREBUTTONTYPE_ICONSCOUNT:
			ShowLabel="false";
			ShowCount="true";
			break;
		case ABMaterial.SOCIALSHAREBUTTONTYPE_BUTTONLABELCOUNT:
			ShowLabel="true";
			ShowCount="true";
			break;
		}
		IsInitialized=true;
	}
	
	@Override
	protected void ResetTheme() {
		// not used
	}
	
	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase();
	}
	
	public void AddEmail() {
		Networks.add(ABMaterial.SOCIALSHARE_EMAIL);
	}
	
	/**
	 * via: adds a @name to the tweet
	 * hashTags: adds hashtags to the tweet, e.g. "hashtag1,hashtag2" 
	 */
	public void AddTwitter(String via, String hashTags) {
		Networks.add(ABMaterial.SOCIALSHARE_TWITTER);
		TwitterVia=via;
		TwitterHashTags=hashTags;
	}
	
	public void AddFacebook() {
		Networks.add(ABMaterial.SOCIALSHARE_FACEBOOK);
	}
	
	public void AddFacebookExtended(String appId, String caption, String description, String redirectUrl, String imageUrl) throws UnsupportedEncodingException {
		this.FacebookAppID=appId;
		this.FacebookCaption=caption;
		this.FacebookDescription=description;
		this.FacebookRedirectUrl=redirectUrl;
		this.FacebookImageUrl=imageUrl;
		Networks.add(ABMaterial.SOCIALSHARE_FACEBOOKEXTENDED);
	}
	
	public void AddFacebookLike() {
		Networks.add(ABMaterial.SOCIALSHARE_FACEBOOKLIKE);
	}
	
	public void AddGooglePlus() {
		Networks.add(ABMaterial.SOCIALSHARE_GOOGLEPLUS);
	}
	
	public void AddLinkedIn() {
		Networks.add(ABMaterial.SOCIALSHARE_LINKEDIN);		
	}
	
	/**
	 * mediaUrl: an url of media to share 
	 */
	public void AddPintrest(String mediaUrl) {
		Networks.add(ABMaterial.SOCIALSHARE_PINTREST);
		PintrestMediaUrl=mediaUrl;
	}
	
	public void AddWhatsApp() {
		Networks.add(ABMaterial.SOCIALSHARE_WHATSAPP);
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
	
	@Override
	protected void FirstRun() {
		Page.ws.Eval(BuildJavaScript(), null);
		
		super.FirstRun();
	}
	
	protected String BuildJavaScript() {
		StringBuilder s = new StringBuilder();	
		s.append("$(\"#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\").jsSocials({");
		if (JumpToComponent!=null) {
			s.append("url: '" + Url + "#" + JumpToComponent.ParentString + JumpToComponent.RootID() + "',");
		} else {
			s.append("url: '" + Url + "',");
		}
		s.append("text: '" + Text.replace("\\","\\\\").replace("'", "\\\'").replace("\"", "&quot;") + "',");
        if (!ShowLabel.equals("")) {
           	s.append("showLabel: " + ShowLabel + ",");
           }	
		if (!ShowCount.equals("")) {
			s.append("showCount: " + ShowCount + ",");
		}
		if (UsePopup) {
			s.append("_getShareUrl: function() {");
			s.append("var url = jsSocials.Socials.prototype._getShareUrl.apply(this, arguments);");
			s.append("return \"javascript:window.openShareWindow('\" + url + \"');\";");
			s.append("},");
		}
		s.append("shares: [");
        for (int i=0;i<Networks.size();i++) {
        	if (i>0) {
        		s.append(",");
        	}
        	switch (Networks.get(i)) {
        	case ABMaterial.SOCIALSHARE_FACEBOOKEXTENDED:
        		s.append("{ share: \"facebookextended\", appid: \"" + FacebookAppID + "\", caption: \"" + FacebookCaption + "\", description: \"" + FacebookDescription + "\", redirecturl: \""+ FacebookRedirectUrl + "\", pictureurl: \"" + FacebookImageUrl + "\" }");
        		break;
        	case ABMaterial.SOCIALSHARE_TWITTER:
        		s.append("{ share: \"twitter\", via: \"" + TwitterVia + "\", hashtags: \"" + TwitterHashTags + "\" }");
        		break;
        	case ABMaterial.SOCIALSHARE_PINTREST:
        		s.append("{ share: \"pinterest\", media: \"" + PintrestMediaUrl + "\" }");
        		break;
        	default:
        		s.append("\"" + Networks.get(i) + "\"");
        	}
        }
		s.append("]");
		s.append("});");
		return s.toString();
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		// TODO extraClasses not working
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
		
		super.Refresh();
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("SocialShare Refresh: " + ID);}
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
		s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"" + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() + "\">");
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
		return s.toString();
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMSocialShare c = new ABMSocialShare();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;
		c.Type = Type;
		c.mVisibility = mVisibility;		
		return c;
	}
		

}
