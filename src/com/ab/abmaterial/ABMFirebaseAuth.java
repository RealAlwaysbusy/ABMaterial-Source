package com.ab.abmaterial;

import java.io.IOException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.objects.collections.List;

@Author("Alain Bailleul")  
public class ABMFirebaseAuth {
	protected ABMPage page=null;
	protected ABMFirebaseUser mCurrentUser = new ABMFirebaseUser();
	
	protected void SetPage(ABMPage page) {
		this.page = page;	
		mCurrentUser.SetPage(page);
	}
	
	protected void CleanUp() {		
		this.page = null;
	}
	
	public void SignOut() {
		if (page==null) return;
		if (page.ws==null) return;
		StringBuilder s = new StringBuilder();
		
		s.append("firebase.auth().signOut().then(function() {");
  		// Sign-out successful.
		s.append("}, function(error) {");
		s.append("var errorCode = error.code;");
		s.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_firebaseautherror','eventparams':'extra','extra':errorCode});");
		s.append("});");
		
		page.ws.Eval(s.toString(), null);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("SignOut");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void CreateUserWithEmailAndPassword(String email, String password) {
		if (page==null) return;
		if (page.ws==null) return;
		StringBuilder s = new StringBuilder();
		s.append("firebase.auth().createUserWithEmailAndPassword(email, password).catch(function(error) {");
		s.append("var errorCode = error.code;");
		s.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_firebaseautherror','eventparams':'extra','extra':errorCode});");
  		s.append("});");
  		page.ws.Eval(s.toString(), null);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("CreateUserWithEmailAndPassword");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void SignInWithEmailAndPassword(String email, String password) {
		if (page==null) return;
		if (page.ws==null) return;
		StringBuilder s = new StringBuilder();				
		s.append("firebase.auth().signInWithEmailAndPassword(email, password).catch(function(error) {");
		s.append("var errorCode = error.code;");
		s.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_firebaseautherror','eventparams':'extra','extra':errorCode});");
		s.append("});");
		page.ws.Eval(s.toString(), null);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("SignInWithEmailAndPassword");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void SignInWithGoogle(boolean withPopup) {
		if (withPopup) {
			SignInWithPopupWithScopes(ABMaterial.FIREBASE_PROVIDER_GOOGLE, null);
		} else {
			SignInWithRedirectWithScopes(ABMaterial.FIREBASE_PROVIDER_GOOGLE, null);
		}
	}
	
	public void SignInWithFacebook(boolean withPopup) {
		if (withPopup) {
			SignInWithPopupWithScopes(ABMaterial.FIREBASE_PROVIDER_FACEBOOK, null);
		} else {
			SignInWithRedirectWithScopes(ABMaterial.FIREBASE_PROVIDER_FACEBOOK, null);
		}
	}
	
	public void SignInWithTwitter(boolean withPopup) {
		if (withPopup) {
			SignInWithPopupWithScopes(ABMaterial.FIREBASE_PROVIDER_TWITTER, null);
		} else {
			SignInWithRedirectWithScopes(ABMaterial.FIREBASE_PROVIDER_TWITTER, null);
		}
	}
	
	public void SignWithGithub(boolean withPopup) {
		if (withPopup) {
			SignInWithPopupWithScopes(ABMaterial.FIREBASE_PROVIDER_GITHUB, null);
		} else {
			SignInWithRedirectWithScopes(ABMaterial.FIREBASE_PROVIDER_GITHUB, null);
		}
	}
	
	public void SignInWithGoogleWithScopes(boolean withPopup, List scopes) {
		if (withPopup) {
			SignInWithPopupWithScopes(ABMaterial.FIREBASE_PROVIDER_GOOGLE, scopes);
		} else {
			SignInWithRedirectWithScopes(ABMaterial.FIREBASE_PROVIDER_GOOGLE, scopes);
		}
	}
	
	public void SignInWithFacebookWithScopes(boolean withPopup, List scopes) {
		if (withPopup) {
			SignInWithPopupWithScopes(ABMaterial.FIREBASE_PROVIDER_FACEBOOK, scopes);
		} else {
			SignInWithRedirectWithScopes(ABMaterial.FIREBASE_PROVIDER_FACEBOOK, scopes);
		}
	}
	
	public void SignInWithTwitterWithScopes(boolean withPopup, List scopes) {
		if (withPopup) {
			SignInWithPopupWithScopes(ABMaterial.FIREBASE_PROVIDER_TWITTER, scopes);
		} else {
			SignInWithRedirectWithScopes(ABMaterial.FIREBASE_PROVIDER_TWITTER, scopes);
		}
	}
	
	public void SignWithGithubWithScopes(boolean withPopup, List scopes) {
		if (withPopup) {
			SignInWithPopupWithScopes(ABMaterial.FIREBASE_PROVIDER_GITHUB, scopes);
		} else {
			SignInWithRedirectWithScopes(ABMaterial.FIREBASE_PROVIDER_GITHUB, scopes);
		}
	}
	
	protected void SignInWithPopupWithScopes(String provider, List scopes) {
		if (page==null) return;
		if (page.ws==null) return;
		StringBuilder s = new StringBuilder();
		switch (provider) {
		case ABMaterial.FIREBASE_PROVIDER_GOOGLE:
			s.append("var provider = new firebase.auth.GoogleAuthProvider();");
			break;
		case ABMaterial.FIREBASE_PROVIDER_FACEBOOK:
			s.append("var provider = new firebase.auth.FacebookAuthProvider();");
			
			break;
		case ABMaterial.FIREBASE_PROVIDER_TWITTER:
			s.append("var provider = new firebase.auth.TwitterAuthProvider();");
			break;
		case ABMaterial.FIREBASE_PROVIDER_GITHUB:
			s.append("var provider = new firebase.auth.GithubAuthProvider();");
			break;
		}
		if (scopes!=null) {
			if (scopes.IsInitialized()) {
				for (int i=0;i<scopes.getSize();i++) {
					s.append("provider.addScope('" + (String)scopes.Get(i) + "');");
				}
			}
		}
		
		s.append("firebase.auth().signInWithPopup(provider).then(function(result) {");
			
		s.append("}).catch(function(error) {");
			
		s.append("var errorCode = error.code;");
		
		s.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_firebaseautherror','eventparams':'extra','extra':errorCode});");
				// ...
		s.append("});");
		page.ws.Eval(s.toString(), null);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("SignInWithPopupWithScopes");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
		
	protected void SignInWithRedirectWithScopes(String provider, List scopes) {
		if (page==null) return;
		if (page.ws==null) return;
		StringBuilder s = new StringBuilder();
		switch (provider) {
		case ABMaterial.FIREBASE_PROVIDER_GOOGLE:
			s.append("var provider = new firebase.auth.GoogleAuthProvider();");
			break;
		case ABMaterial.FIREBASE_PROVIDER_FACEBOOK:
			s.append("var provider = new firebase.auth.FacebookAuthProvider();");
			break;
		case ABMaterial.FIREBASE_PROVIDER_TWITTER:
			s.append("var provider = new firebase.auth.TwitterAuthProvider();");
			break;
		case ABMaterial.FIREBASE_PROVIDER_GITHUB:
			s.append("var provider = new firebase.auth.GithubAuthProvider();");
			break;
		}
		if (scopes!=null) {
			if (scopes.IsInitialized()) {
				for (int i=0;i<scopes.getSize();i++) {
					s.append("provider.addScope('" + (String)scopes.Get(i) + "');");
				}
			}
		}
		
		s.append("firebase.auth().signInWithRedirect(provider);");  
		
		s.append("firebase.auth().getRedirectResult().then(function(result) {");
			
  		s.append("}).catch(function(error) {");
  			
  		s.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_firebaseautherror','eventparams':'extra','extra':errorCode});");
  			
  		s.append("});");
		page.ws.Eval(s.toString(), null);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("SignInWithRedirectWithScopes");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	
	public void SignInWithCustomToken(String token) {
		if (page==null) return;
		if (page.ws==null) return;
		StringBuilder s = new StringBuilder();
		s.append("firebase.auth().signInWithCustomToken(token).catch(function(error) {");
		s.append("var errorCode = error.code;");
		s.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_firebaseautherror','eventparams':'extra','extra':errorCode});");
		s.append("});");
		page.ws.Eval(s.toString(), null);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("SignInWithCustomToken");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void SignInAnonymously() {
		if (page==null) return;
		if (page.ws==null) return;
		StringBuilder s = new StringBuilder();
		s.append("firebase.auth().signInAnonymously().catch(function(error) {");			  
		s.append("var errorCode = error.code;");
		s.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_firebaseautherror','eventparams':'extra','extra':errorCode});");
		s.append("});");
		
		page.ws.Eval(s.toString(), null);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("SignInAnonymously");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	public ABMFirebaseUser CurrentUser() {		
		return mCurrentUser;		
	}

}
