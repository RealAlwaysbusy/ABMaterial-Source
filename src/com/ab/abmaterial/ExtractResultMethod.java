package com.ab.abmaterial;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ab.abmaterial.ABMaterial.SCOPE;

import anywheresoftware.b4a.BA.Hide;

@Hide
public class ExtractResultMethod {
	public Map<String, Boolean> Needs = new LinkedHashMap<String, Boolean>();
	public Map<String, String> GlobalNeeds = new LinkedHashMap<String, String>();
	public Map<String, String> Calls = new LinkedHashMap<String, String>();
	public Map<String, Boolean> OwnCalls = new LinkedHashMap<String, Boolean>();
	public List<ExtractLine> Lines = new ArrayList<ExtractLine>();
	public String Name="";
	public String InModule="";
	protected SCOPE Scope=SCOPE.PUBLIC; 
		
	protected String FinalJS="";
		
	public ExtractResultMethod() {
		
	}
	
	public ExtractResultMethod(String name, String inModule) {
		Name=name;
		InModule = inModule;
	}	
	
	
		
	
	
}
