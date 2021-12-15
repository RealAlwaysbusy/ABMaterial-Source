package com.ab.b4js;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ab.b4js.B4JS.SCOPE;

import anywheresoftware.b4a.BA.Hide;

@Hide
public class B4JSExtractResultMethod {
	public Map<String, Boolean> Needs = new LinkedHashMap<String, Boolean>();
	public Map<String, String> GlobalNeeds = new LinkedHashMap<String, String>();
	public Map<String, String> Calls = new LinkedHashMap<String, String>();
	public Map<String, Boolean> OwnCalls = new LinkedHashMap<String, Boolean>();
	public List<B4JSExtractLine> Lines = new ArrayList<B4JSExtractLine>();
	public String Name="";
	public String InModule="";
	protected SCOPE Scope=SCOPE.PUBLIC; 
		
	protected String FinalJS="";
	
	public B4JSExtractResultMethod() {
		
	}
	
	public B4JSExtractResultMethod(String name, String inModule) {
		Name=name;
		InModule = inModule;
	}	
	
	public B4JSExtractResultMethod Clone() {
		B4JSExtractResultMethod c = new B4JSExtractResultMethod();
		for (Entry<String,Boolean>entry: Needs.entrySet()) {
			c.Needs.put(entry.getKey(), entry.getValue());
		}
		for (Entry<String,String>entry: GlobalNeeds.entrySet()) {
			c.GlobalNeeds.put(entry.getKey(), entry.getValue());
		}
		for (Entry<String,String>entry: Calls.entrySet()) {
			c.Calls.put(entry.getKey(), entry.getValue());
		}
		for (Entry<String,Boolean>entry: OwnCalls.entrySet()) {
			c.OwnCalls.put(entry.getKey(), entry.getValue());
		}
		c.Lines.addAll(Lines);
		c.Name = Name;
		c.InModule = InModule;
		c.Scope = Scope;
		c.FinalJS = FinalJS;
		return c;
	}
		
	
	
}
