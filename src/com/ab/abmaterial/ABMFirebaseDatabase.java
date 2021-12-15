package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;

@Author("Alain Bailleul")
public class ABMFirebaseDatabase {
	protected ABMPage page=null;
	
	protected void SetPage(ABMPage page) {
		this.page = page;
	}
	
	protected void CleanUp() {
		this.page = null;
	}
}
