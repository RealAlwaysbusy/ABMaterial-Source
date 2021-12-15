package com.ab.abmaterial;

import java.util.LinkedHashMap;
import java.util.Map;

import anywheresoftware.b4a.BA.Hide;

@Hide
public class HandlerClass {
	protected boolean IsArray=false;
	protected Map<String,Object> Handlers = new LinkedHashMap<String,Object>();
	
	protected Object GetHandler(String ID, Object me) {
		return Handlers.getOrDefault(ID.toLowerCase(), me);
	}
}
