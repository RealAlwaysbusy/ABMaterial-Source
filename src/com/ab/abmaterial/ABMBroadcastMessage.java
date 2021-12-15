package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")  
@ShortName("ABMBroadcastMessage")
public class ABMBroadcastMessage {
	protected String mID = "";
	public String MessageType = "";
	public String FromType = "";
	public String From = "";
	public anywheresoftware.b4a.objects.collections.Map Params = new anywheresoftware.b4a.objects.collections.Map();
	
	public void Initialize() {
		Params.Initialize();
		
	}
	
	public String getID() {
		return mID;
	}
}
