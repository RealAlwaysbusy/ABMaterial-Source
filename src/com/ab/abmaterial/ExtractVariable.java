package com.ab.abmaterial;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import anywheresoftware.b4a.BA.Hide;

@Hide
public class ExtractVariable {
	protected String Type="";
	protected String OrigType="";
	protected String Name="";
	protected String JSName="";
	protected Map<String,ExtractVariable> Members = new LinkedHashMap<String,ExtractVariable>();	
	protected int Depth=0;
	protected boolean isArray=false;
	protected String Dimension="";
	protected String InitValue="";
	protected String ArrayInitValue="";
	protected boolean ValidType=true;
	protected boolean IsValues=false;
	
	protected ExtractVariable Clone() {
		ExtractVariable ret = new ExtractVariable();
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
		for (Entry<String,ExtractVariable> entry: Members.entrySet()) {
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
						case "float":
						case "b4jsint":
						case "b4jsdouble":
						case "b4jsfloat":
							ArrayInitValue="0";
							break;
						case "boolean":
						case "b4jsboolean":
							ArrayInitValue="false";
							break;
						case "string":
						case "b4jsstring":
							ArrayInitValue="''";
							break;
						case "list":
						case "b4jslist":
							ArrayInitValue = "[]";
							break;
						case "map":
						case "b4jsmap":
							ArrayInitValue = "{}";
							break;
						case "byte":
						case "b4jsbyte":
							ArrayInitValue = "0";
							break;
						default:
							break;
					}
				}
			} else {
				switch (OrigType) {
					case "int":
					case "double":
					case "float":
					case "b4jsint":
					case "b4jsdouble":
					case "b4jsfloat":
						InitValue="0";
						break;
					case "boolean":
					case "b4jsboolean":
						InitValue="false";
						break;
					case "string":
					case "b4jsstring":
						InitValue="''";
						break;
					case "list":
					case "b4jslist":
						InitValue = "[]";
						break;
					case "map":
					case "b4jsmap":
						InitValue = "{}";
						break;
					case "byte":
					case "b4jsbyte":
						InitValue = "0";
						break;
					default:
						break;
				}
			}
		}
	}
}
