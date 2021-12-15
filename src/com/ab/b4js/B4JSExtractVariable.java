package com.ab.b4js;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.ab.b4js.B4JS.SCOPE;

import anywheresoftware.b4a.BA.Hide;

@Hide
public class B4JSExtractVariable {
	protected String Type="";
	protected String OrigType="";
	protected String Name="";
	protected String JSName="";
	protected Map<String,B4JSExtractVariable> Members = new LinkedHashMap<String,B4JSExtractVariable>();	
	protected int Depth=0;
	protected boolean isArray=false;
	protected String Dimension="";
	protected String InitValue="";
	protected String ArrayInitValue="";
	protected boolean ValidType=true;
	protected boolean IsValues=false;
	protected SCOPE Scope=SCOPE.PUBLIC;
	protected String CurrentValue="";
	protected String EventName="";
	protected String ExtraVar="";
	
	
	public B4JSExtractVariable() {
		
	}
	
	public B4JSExtractVariable(SCOPE scope, String type, String name, String value, int depth) {
		Type = type;
		Scope = scope;
		Name = name.toLowerCase();
		JSName = "_" + Name;
		Depth = depth;
		InitValue=value;
		CurrentValue=value;
	}
	
	
	protected B4JSExtractVariable Clone() {
		B4JSExtractVariable ret = new B4JSExtractVariable();
		ret.Type = Type;
		ret.OrigType = OrigType;
		ret.Name = Name;
		ret.JSName = JSName;
		ret.Depth = Depth;
		ret.isArray=isArray;
		ret.Dimension = Dimension;
		ret.InitValue = "";
		ret.ArrayInitValue=ArrayInitValue;
		ret.ValidType = ValidType;
		ret.IsValues = IsValues;
		ret.Scope = Scope;
		ret.CurrentValue = CurrentValue;
		ret.EventName=EventName;
		ret.ExtraVar="";
		for (Entry<String,B4JSExtractVariable> entry: Members.entrySet()) {
			ret.Members.put(entry.getKey(), entry.getValue().Clone());
		}
		return ret;
	}
	
	public String toString() {
		return JSName + "(" + Depth + ")";
	}
	
	protected void SetDefaultInitValue() {
		if (InitValue.equals("")) {
			if (isArray) {
				InitValue = "[]";
				if (ArrayInitValue.equals("")) {
					switch (OrigType) {
						case "int":
						case "double":
						case "long":
						case "float":
							ArrayInitValue="0";
							break;
						case "boolean":
							ArrayInitValue="false";
							break;
						case "string":
							ArrayInitValue="\"\"";
							break;
						case "list":
							ArrayInitValue = "[]";
							break;
						case "map":
							ArrayInitValue = "{}";
							break;
						case "byte":
							ArrayInitValue = "0";
							break;
						case "jsonparser":
							ArrayInitValue = "{}";
							ExtraVar = "_" + JSName + "={}";
							break;
						case "jsongenerator":
							ArrayInitValue = "{}";							
							break;
						case "timer":
							ArrayInitValue = "null";							
							break;							
						case "stringbuilder":
							ArrayInitValue=" new StringBuilder()";
							break;
						case "b4jsjquery":
							break;							
						case "b4jsjqueryelement":
							ArrayInitValue = "null";
							break;
						case "abmlabel":							
						case "abmbutton":							
						case "abminput":
						case "abmswitch":
						case "abmcheckbox":
						case "abmradiogroup":
						case "abmactionbutton":
						case "abmbadge":
						case "abmchip":
						case "abmrange":
						case "abmslider":							
						case "abmtabs":
							ArrayInitValue="{}";
							break;
						default:
							break;
					}
				}
			} else {
				switch (OrigType) {
					case "int":
					case "double":
					case "long":
					case "float":
						InitValue="0";
						break;
					case "boolean":
						InitValue="false";
						break;
					case "string":
						InitValue="\"\"";
						break;
					case "list":
						InitValue = "[]";
						break;
					case "map":
						InitValue = "{}";
						break;
					case "byte":
						InitValue = "0";
						break;
					case "jsonparser":
						InitValue="{}";						
						break;
					case "jsongenerator":
						InitValue = "{}";							
						break;
					case "timer":
						InitValue = "null";
						ExtraVar = "_" + JSName + "={}";
						break;
					case "stringbuilder":
						InitValue = "new StringBuilder()";
						break;
					case "b4jsjquery":
						break;						
					case "b4jsjqueryelement":
						InitValue = "null";
						break;
					case "abmlabel":
					case "abmbutton":					
					case "abminput":
					case "abmswitch":
					case "abmcheckbox":
					case "abmradiogroup":
					case "abmactionbutton":
					case "abmbadge":
					case "abmchip":
					case "abmrange":
					case "abmslider":
					case "abmtabs":
						InitValue="{}";
						break;
					default:
						break;
				}
			}
		}
	}
}

