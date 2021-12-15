package com.ab.abmaterial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")  
@ShortName("ABMSocialOAuth")
public class ABMSocialOAuth extends ABMComponent{
	
	private static final long serialVersionUID = 4744277315277406598L;
	protected List<String> Networks = new ArrayList<String>();
	protected List<String> RegIds = new ArrayList<String>();
	protected List<String> Titles = new ArrayList<String>();
	
	public void Initialize(ABMPage page, String id) {
		this.ID = id;
		this.Type = ABMaterial.UITYPE_SOCIALOAUTH;
		this.Page = page;			
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
	
	/**
	 *  Register your domain with Facebook at https://developers.facebook.com/ and add here
	 */
	public void AddFacebookButton(String domain, String registrationId, String title) {
		Networks.add(ABMaterial.SOCIALOAUTH_FACEBOOK);		
		RegIds.add("{'" + domain + "' : '" + registrationId + "',}[window.location.hostname] || '" + registrationId + "'");
		Titles.add(title);
	}
	
	/**
     *  Register your domain with Twitter at https://apps.twitter.com/ and add here
	 */
	public void AddTwitterButton(String domain, String registrationId, String title) {
		Networks.add(ABMaterial.SOCIALOAUTH_TWITTER);		
		RegIds.add("{'" + domain + "' : '" + registrationId + "',}[window.location.hostname] || '" + registrationId + "'");
		Titles.add(title);
	}
	
	/**
     *  Google Register --  https://console.developers.google.com
	 */
	public void AddGooglePlusButton(String registrationId, String title) {
		Networks.add(ABMaterial.SOCIALOAUTH_GOOGLEPLUS);		
		RegIds.add("'" + registrationId + "'");
		Titles.add(title);
	}
	
	/**
     *  Register your domain with Windows Live at http://manage.dev.live.com and add here
	 */
	public void AddWindowsLiveButton(String domain, String registrationId, String title) {
		Networks.add(ABMaterial.SOCIALOAUTH_WINDOWSLIVE);		
		RegIds.add("{'" + domain + "' : '" + registrationId + "',}[window.location.hostname] || '" + registrationId + "'");
		Titles.add(title);
	}
	
	/**
     *  LinkedIn Register - https://www.linkedin.com/secure/developer
	 */
	public void AddLinkedInButton(String registrationId, String title) {
		Networks.add(ABMaterial.SOCIALOAUTH_LINKEDIN);		
		RegIds.add("'" + registrationId + "'");
		Titles.add(title);
	}
	
	/**
     *  Yahoo Register - https://developer.yahoo.com/
	 */
	public void AddYahooButton(String domain, String registrationId, String title) {
		Networks.add(ABMaterial.SOCIALOAUTH_YAHOO);		
		RegIds.add("{'" + domain + "' : '" + registrationId + "',}[window.location.hostname]");
		Titles.add(title);
	}	

	/**
     *  SoundCloud Register - http://soundcloud.com/you/apps/
	 */
	public void AddSoundCloudButton(String domain, String registrationId, String title) {
		Networks.add(ABMaterial.SOCIALOAUTH_SOUNDCLOUD);		
		RegIds.add("{'" + domain + "' : '" + registrationId + "',}[window.location.hostname]");
		Titles.add(title);
	}
	
	/**
     *  FourSquare Register - https://foursquare.com/developers/apps
	 */
	public void AddFourSquareButton(String domain, String registrationId, String title) {
		Networks.add(ABMaterial.SOCIALOAUTH_FOURSQUARE);		
		RegIds.add("{'" + domain + "' : '" + registrationId + "',}[window.location.hostname]");
		Titles.add(title);
	}
	
	/**
     *  Instagram Register - http://instagram.com/developer/clients/manage/
	 */
	public void AddInstagramButton(String domain, String registrationId, String title) {
		Networks.add(ABMaterial.SOCIALOAUTH_INSTAGRAM);		
		RegIds.add("{'" + domain + "' : '" + registrationId + "',}[window.location.hostname]");
		Titles.add(title);
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
		super.FirstRun();
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		// TODO extraClasses not working
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
		
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("SocialOAuth Refresh: " + ID);}
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
		s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"abmsoa notselectable " + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() +"\">\n");
		for (int i=0;i<Networks.size();i++) {			
			s.append("<button onclick=\"hello('" + Networks.get(i) + "').login()\" title=\"" + Titles.get(i) + "\" class=\"zocial icon " + Networks.get(i) + "\"></button>\n");
		}
		s.append("</div>\n");
		IsBuild=true;
		return s.toString();		
	}
	
	@Hide
	protected String BuildClass() {
		return "";
	}
	
	@Hide
	protected String BuildBody() {		
		return "";
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMSocialOAuth c = new ABMSocialOAuth();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;		
		c.Type = Type;
		c.mVisibility = mVisibility;	
		for (String s: Networks) {
			c.Networks.add(s);
		}
		return c;
	}
}
