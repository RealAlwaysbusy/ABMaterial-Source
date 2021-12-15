package com.ab.abmaterial;

import java.io.IOException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;

@Author("Alain Bailleul")  
public class ABMFirebase {
	public String ApiKey="";
	public String AuthDomain="";
	public String DatabaseURL="";
	public String StorageBucket="";
	
	public ABMFirebaseAuth Auth=new ABMFirebaseAuth();
	public ABMFirebaseStorage Storage=new ABMFirebaseStorage();
	
	protected ABMPage page = null;
	
	// still to do
	private ABMFirebaseDatabase Database=new ABMFirebaseDatabase();
	
	protected boolean internalIsInitialized=false;
	
	protected void SetPage(ABMPage page) {
		this.page = page;
		Auth.SetPage(page);
		Storage.SetPage(page);
		Database.SetPage(page);
		internalIsInitialized = true;
	}
	
	protected void CleanUp() {
		page=null;
		Auth.CleanUp();
		Storage.CleanUp();
		Database.CleanUp();
		internalIsInitialized = false;
	}
	
	public void CheckAuthorized() {
		StringBuilder s = new StringBuilder();
		s.append("if (firebase.auth().currentUser) {");
		s.append("console.log('true');");
		s.append("return true;");
		s.append("} else {");
		s.append("console.log('false');");
		s.append("return false;");
		s.append("}");
		page.ws.Eval(s.toString(), null);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("CheckAuthorized");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}		
	}
	
	
}
