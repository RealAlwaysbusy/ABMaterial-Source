package com.ab.abmaterial;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.objects.collections.List;
import anywheresoftware.b4j.object.WebSocket.SimpleFuture;

@Author("Alain Bailleul")
public class ABMFirebaseUser {
	protected ABMPage page = null;
	protected String mDisplayName="";
	protected String mEmail = "";
	protected boolean mEmailVerified = false;
	protected String mPhotoURL = "";
	protected boolean mIsAnonymous = false;
	protected String mUid = "";
	protected String mRefreshToken = "";
	protected List mProviderData = new List();
	protected String mProviderId = "";
		
	protected void SetPage(ABMPage page) {
		this.page = page;
		mProviderData.Initialize();		
	}
	
	protected void CleanUp() {
		this.page = null;
	}
	
	@SuppressWarnings("unchecked")
	public void UpdateFromBrowser() {
		if (page==null) return;
		if (page.ws==null) return;
		StringBuilder s = new StringBuilder();
		s.append("if (firebase.auth().currentUser) {");
		s.append("var user = firebase.auth().currentUser;");
		s.append("var displayName = user.displayName;");
		s.append("var email = user.email;");
		s.append("var emailVerified = user.emailVerified;");
		s.append("var photoURL = user.photoURL;");
		s.append("var isAnonymous = user.isAnonymous;");
		s.append("var uid = user.uid;");
		s.append("var refreshToken = user.refreshToken;");
		s.append("var providerData = user.providerData;");
		s.append("var providerId = user.providerId;");
		
		s.append("return JSON.stringify({");
		s.append("displayName: displayName,");
		s.append("email: email,");
		s.append("emailVerified: emailVerified,");
		s.append("photoURL: photoURL,");
		s.append("isAnonymous: isAnonymous,");
		s.append("uid: uid,");
		s.append("refreshToken: refreshToken,");
		s.append("providerData: providerData,");
		s.append("providerId: providerId");
		s.append("}, null, '  ');");
		
		s.append("} else {");
		s.append("return '';");
		s.append("}");
		SimpleFuture ret = page.ws.EvalWithResult(s.toString(), null);
		mDisplayName="";
		mEmail = "";
		mEmailVerified = false;
		mPhotoURL = "";
		mIsAnonymous = false;
		mUid = "";
		mRefreshToken = "";
		mProviderData.Clear();
		
		mProviderId = "";
		try {
			String back =(String) ret.getValue();
			if (!back.equals("")) {
				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(back);
				mDisplayName = (String) obj.getOrDefault("displayName", "");
				mEmail = (String) obj.getOrDefault("email", "");
				mEmailVerified = (boolean) obj.getOrDefault("emailVerified", false);
				mPhotoURL = (String) obj.getOrDefault("photoURL", "");
				mIsAnonymous = (boolean) obj.getOrDefault("isAnonymous", false);
				mUid = (String) obj.getOrDefault("uid", "");
				mRefreshToken = (String) obj.getOrDefault("refreshToken", "");
				JSONArray tmpProviderData = (JSONArray) obj.getOrDefault("providerData", null);
				if (tmpProviderData!=null) {
					for (int i=0;i<tmpProviderData.size();i++) {
						JSONObject objPD = (JSONObject) tmpProviderData.get(i);
						ABMFirebaseUserInfo info = new ABMFirebaseUserInfo();
						info.DisplayName = (String) objPD.getOrDefault("displayName", "");
						info.Email = (String) objPD.getOrDefault("email", "");
						info.PhotoURL = (String) objPD.getOrDefault("photoURL", "");
						info.ProviderId = (String) objPD.getOrDefault("providerId", "");
						info.Uid = (String) objPD.getOrDefault("Uid", "");
						mProviderData.Add(info);
					}
				}
				mProviderId = (String) obj.getOrDefault("providerId", "");
			} 			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		
	}
	
	
	
	public String DisplayName() {
		return mDisplayName;
	}
	
	public String Email() {
		return mEmail;
	}
	
	public boolean EmailVerified() {
		return mEmailVerified;
	}
	
	public String PhotoURL() {
		return mPhotoURL;
	}
	
	public boolean IsAnonymous() {
		return mIsAnonymous;
	}
	
	public String Uid() {
		return mUid;
	}
	
	public String RefreshToken() {
		return mRefreshToken;
	}
	
	/**
	 * a list of ABMFirebaseUserInfo objects 
	 */
	public List ProviderData() {
		return mProviderData;
	}
	
	public String ProviderId() {
		return mProviderId;
	}
	
}
