package com.ab.abmaterial;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import anywheresoftware.b4a.BA.Hide;

@Hide
public class ExtractResult {
	protected Map<String,ExtractResultModule> Modules = new LinkedHashMap<String,ExtractResultModule>();
	protected Map<String,String> OptimizeTrans = new LinkedHashMap<String,String>();
	protected List<String> oTranslations = new ArrayList<String>();
	protected Map<String,List<String>> wrongTranslations = new LinkedHashMap<String,List<String>>();
	protected Map<String,String> OptimizeDefs = new LinkedHashMap<String,String>();
	protected List<String> oDefaults = new ArrayList<String>();
	protected Map<String,List<String>> wrongDefs = new LinkedHashMap<String,List<String>>();
		
	protected int index=0;
	protected int ind=0;
	protected int line=0;
	protected ExtractResultModule module=null;
	protected ExtractLine currentLine=null;
	protected StringBuilder methodJS=null;
	protected Map<String,ExtractResultMethod> moduleJS=null;
	
	protected String NL="";
	protected ExtractResultMethod method=null;
	protected boolean HasError=false;
	protected String methodName="";
	protected String fullMethodName="";
	protected boolean isGlobal=false;
	protected int depth=0;
	protected int TrackFromLineNumber=-1;
	protected int TrackToLineNumber=-1;
	protected boolean IsTracking=false;
	protected String TrackModuleName="";
	protected boolean IsTrackingVars=false;
		
	protected Map<String,ExtractVariable> Vars = new LinkedHashMap<String,ExtractVariable>();
	
	protected Map<String,Boolean> SupportedTypes = new LinkedHashMap<String, Boolean>();
	
	protected BufferedWriter analyseLog = null;
	
	protected boolean NeedsInputMask=false;
}
