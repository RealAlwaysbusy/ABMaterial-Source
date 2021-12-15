package com.ab.b4js;

import java.io.BufferedReader;
//import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
//import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
//import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ab.abmaterial.ABMaterial;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
//import anywheresoftware.b4a.BA.DesignerName;
import anywheresoftware.b4a.BA.Hide;
//import anywheresoftware.b4a.BA.ShortName;
//import anywheresoftware.b4a.BA.Version;
import anywheresoftware.b4a.keywords.Regex;
                                
@Author("Alain Bailleul")  
@Hide
public class B4JS {
	protected String ClassName="";
	protected Map<String,String> GlobalStrings = new LinkedHashMap<String,String>();
	protected Map<String,String> GlobalSmartStrings = new LinkedHashMap<String,String>();
	protected List<B4JSExtractLine> Lines = new ArrayList<B4JSExtractLine>();
	protected Map<String,B4JSExtractResultMethod> Methods = new LinkedHashMap<String,B4JSExtractResultMethod>();

	protected StringBuilder ExtraJS=new StringBuilder();
	@Hide
	public List<String> NewVars=new ArrayList<String>();
	
	protected int counter = 0;
	
	private final static Pattern LTRIM = Pattern.compile("^\\s+");
	private final static Pattern RTRIM = Pattern.compile("\\s+$");
	
	protected enum SCOPE {
		PRIVATE, PUBLIC, GLOBAL;
	}
	
	protected int index=0;
	protected int ind=0;
	protected int line=0;
	protected B4JSExtractLine currentLine=null;
	protected StringBuilder methodJS=null;
	protected Map<String,B4JSExtractResultMethod> moduleJS=null;
	protected String NL = "\n";
	protected B4JSExtractResultMethod method=null;
	protected boolean HasError=false;
	protected String methodName="";
	protected String fullMethodName="";
	protected boolean isGlobal=false;
	protected int depth=0;
	protected int TrackFromLineNumber=-1;
	protected int TrackToLineNumber=-1;
	public boolean DebugTrack=false;
	protected String TrackModuleName="";
	protected boolean IsTrackingVars=false;
		
	protected Map<String,B4JSExtractVariable> Vars = new LinkedHashMap<String,B4JSExtractVariable>();
	
	protected Map<String,Boolean> SupportedTypes = new LinkedHashMap<String, Boolean>();
	
	protected int varCounter=0;
	
	@Hide
	public B4JS Clone() {
		B4JS c = new B4JS();
		
		c.ClassName=ClassName;
		for (Entry<String,String>entry: GlobalStrings.entrySet()) {
			c.GlobalStrings.put(entry.getKey(), entry.getValue());
		}
		c.Lines.addAll(Lines);
		for (Entry<String,B4JSExtractResultMethod>entry: Methods.entrySet()) {
			c.Methods.put(entry.getKey(), entry.getValue().Clone());
		}
		c.counter = counter;
		
		c.index=index;
		c.ind=ind;
		c.line=line;
		c.currentLine=currentLine;
		c.methodJS=methodJS;
		if (moduleJS!=null) {
			for (Entry<String,B4JSExtractResultMethod>entry: moduleJS.entrySet()) {
				c.moduleJS.put(entry.getKey(), entry.getValue().Clone());
			}
		}
		
		c.NL = NL;
		if (c.method!=null) {
			c.method=method.Clone();
		}
		c.HasError=HasError;
		c.methodName=methodName;
		c.fullMethodName=fullMethodName;
		c.isGlobal=isGlobal;
		c.depth=depth;
		c.TrackFromLineNumber=TrackFromLineNumber;
		c.TrackToLineNumber=TrackToLineNumber;
		c.DebugTrack=DebugTrack;
		c.TrackModuleName=TrackModuleName;
		c.IsTrackingVars=IsTrackingVars;
		
		for (Entry<String,B4JSExtractVariable>entry: Vars.entrySet()) {
			c.Vars.put(entry.getKey(), entry.getValue().Clone());
		}
		
		for (Entry<String,Boolean>entry: SupportedTypes.entrySet()) {
			c.SupportedTypes.put(entry.getKey(), entry.getValue());
		}
		
		return c;
	}
	
	public void Initialize(String className) {
		ClassName=className;
		
		SupportedTypes.put("INT", true);
		SupportedTypes.put("LONG", true);
		SupportedTypes.put("DOUBLE", true);
		SupportedTypes.put("FLOAT", true);
		SupportedTypes.put("STRING", true);
		SupportedTypes.put("BOOLEAN", true);
		SupportedTypes.put("LIST", true);
		SupportedTypes.put("MAP", true);
		SupportedTypes.put("BYTE", true);
		SupportedTypes.put("TIMER", true);
		SupportedTypes.put("STRINGBUILDER", true);
		SupportedTypes.put("ABMPAGE", true);
		SupportedTypes.put("ABMINPUT", true);
		SupportedTypes.put("ABMLABEL", true);
		SupportedTypes.put("ABMBUTTON", true);
		SupportedTypes.put("ABMSWITCH", true);
		SupportedTypes.put("ABMCHECKBOX", true);
		SupportedTypes.put("ABMRADIOGROUP", true);
		SupportedTypes.put("ABMACTIONBUTTON", true);
		SupportedTypes.put("ABMCONTAINER", true);
		SupportedTypes.put("JSONPARSER", true);
		SupportedTypes.put("JSONGENERATOR", true);
		SupportedTypes.put("ABMRANGE", true);
		SupportedTypes.put("ABMTABS", true);
		SupportedTypes.put("B4JSJQUERY", true);
		SupportedTypes.put("B4JSJQUERYELEMENT", true);		
	}
		
	public void Build(boolean isFirst, boolean isLast, Map<String,String> B4JSClassGenerated, boolean IsDebugMode) {
		if (IsDebugMode==false) {
			NL="";
		}
		
		Map<String,File> OrderedFiles = new LinkedHashMap<String, File>();
		Map<String,String> OrderedFilesAllowedNew = new LinkedHashMap<String, String>(); 
		
		String dirApp = anywheresoftware.b4a.objects.streams.File.getDirApp();
		File SourceFolder=null;
		SourceFolder = new File(dirApp);
		SourceFolder = new File(SourceFolder.getParent()); // .bas files
		
    	if (!SourceFolder.exists()){
    		return;
    	}
		File[] files = SourceFolder.listFiles(new FileFilter() {
    	    @Override
    	    public boolean accept(File pathname) {
    	        String name = pathname.getName().toLowerCase();
    	        return (name.endsWith(".b4j")) && pathname.isFile();
    	    }
    	});
    	if (files!=null) {
    		if (files.length==0) {
    			return;
    		}
    		
    		for (File f: files) {
    			String fn = f.getName();
    			if (fn.endsWith(".b4j")) {
    				OrderedFilesAllowedNew = GetUsedModules(f);    				
    				break;
    			}	    				
    		} 
    		
    		for (Entry<String,String> entry: OrderedFilesAllowedNew.entrySet()) {
    			String fName = entry.getKey() + ".bas";
    			File f=null;
    			if (fName.startsWith("|relative|")) {
    				fName = fName.substring(10);
    				File b = new File(SourceFolder, fName);
    				try {
						String absolute = b.getCanonicalPath();
						f = new File(absolute);
					} catch (IOException e) {
						e.printStackTrace();
					} // may throw IOException
    			} else {
    				if (fName.startsWith("|absolute|")) {
    					fName = fName.substring(10);
	    				f = new File(fName);
    				} else {
    					// in the SourceFolder
    					f = new File(SourceFolder.getAbsoluteFile().getPath() + "/" + fName);
    				}
    			}
    			
    			String fn = f.getName();
    			String ModuleName = Left(fn,fn.length()-4); 
    			OrderedFiles.put(ModuleName.toLowerCase(), f);
    		}    		
    		
    		try {
    			for (Entry<String,File> entry: OrderedFiles.entrySet()) {
    				if (entry.getKey().equalsIgnoreCase(ClassName)) {
    					ConvertToJS(OrderedFiles, ClassName, entry.getValue(), isFirst, isLast, B4JSClassGenerated);
    					break;
    				}
    			}
    		} catch (Exception e) {
		        e.printStackTrace();
		    } finally {
		        try {
		        	
		        } catch (Exception e) {
		        }
		    }
    	} else {
    		return;
    	}
    	
    	
	}
	
	protected void ConvertToJS(Map<String,File> OrderedFiles, String moduleName, File f, boolean isFirst, boolean isLast, Map<String,String> B4JSClassGenerated) {
		TrackFromLineNumber=-1;
		TrackToLineNumber=-1; //Integer.MAX_VALUE;
		
    	moduleJS = new LinkedHashMap<String,B4JSExtractResultMethod>();
		
		BufferedReader br=null;
		try {
			String s="";
			br = new BufferedReader(new FileReader(f));
			StringBuilder allS = new StringBuilder(); 
			while ((s = br.readLine()) != null) {				
				allS.append(s + " \n");
			}
			
			s = allS.toString();
			s = ExtractJavascript(s);
			s = Phase2(Phase1(s, "$\"", "\"$"),"\"", "\"");
			
			String All[] = s.split("\n");
		    int Start = 0;
			for (int i=0;i<All.length;i++) {
				String tmpS = All[i]; 
				if (tmpS.contains("@EndOfDesignText@")) {
					tmpS = "" + Start + ";" + tmpS;
					Start = 1;
				} else {
					if (Start>0) {						
						tmpS = "" + Start + ";" + tmpS;
						Start++;
					} else {
						tmpS = "" + Start + ";" + tmpS;
					}
				}
				All[i] = tmpS;
			}
			
			s = String.join("\n",All);			
					    
		    s = s.replace("\t",  " ");
			while (s.indexOf("  ")>-1) {
				s = s.replace("  ", " ");
			}
			
			All = s.split("\n");
			List<String> AllSJoined = new LinkedList<String>(Arrays.asList(All));
			
			for (int i=0;i<AllSJoined.size();i++) {
				String tmpS = Clean(AllSJoined.get(i)); 				
				if (tmpS.endsWith(" _") && i < AllSJoined.size()-1) {
					tmpS = tmpS.substring(0, tmpS.length()-1);
					String sNext = AllSJoined.get(i+1);
					int lineNum = sNext.indexOf(";");					
					sNext = sNext.substring(lineNum+1);
					if (tmpS.trim().endsWith(".") || sNext.trim().startsWith(".")) {
						tmpS = rtrim(tmpS) + ltrim(sNext);
					} else {
						tmpS = rtrim(tmpS) + sNext;
					}
					AllSJoined.set(i, tmpS);
					AllSJoined.remove(i+1);
					
					i--;					
				} else {
					if (tmpS.endsWith(" _ ") && i < AllSJoined.size()-1) {
						tmpS = tmpS.substring(0, tmpS.length()-2);
						String sNext = AllSJoined.get(i+1);
						int lineNum = sNext.indexOf(";");					
						sNext = sNext.substring(lineNum+1);
						if (tmpS.trim().endsWith(".") || sNext.trim().startsWith(".")) {
							tmpS = rtrim(tmpS) + ltrim(sNext);
						} else {
							tmpS = rtrim(tmpS) + sNext;
						}
						AllSJoined.set(i, tmpS);
						AllSJoined.remove(i+1);

						i--;
					}
				}
			}
			
			
			All = AllSJoined.toArray(new String[0]);
			s = String.join("\n",All);
									
			All = s.split("\n");
			for (int i=0;i<All.length;i++) {
				B4JSExtractLine eLine = new B4JSExtractLine();				
				s = All[i];
				
				int lineNum = s.indexOf(";");
				
				eLine.LineNumber = Integer.valueOf(s.substring(0, lineNum));
				s = s.substring(lineNum+1);
				eLine.OriginalLine = s;	
				s = s.toLowerCase();
				String ss = s;
				
				while (s.indexOf("  ")>-1) {
					s = s.replace("  ", " ");
				}
				eLine.CurrentLine=Clean(s);
								
				ss = ss.replace("=", " = ");
				ss = ss.replace("<", " < ");
				ss = ss.replace(">", " > ");
				ss = ss.replace("<  >", " != ");
				ss = ss.replace("<  =", " <= ");
				ss = ss.replace(">  =", " >= ");
				ss = ss.replace("+", " + ");
				ss = ss.replace("&", " + ");
				ss = ss.replace("*", " * ");
				ss = ss.replace("/", " / ");
				ss = ss.replace("^", " ^ ");				
				ss = ss.replace("-", " - "); 
				ss = ss.replace(".", " . ");
				ss = ss.replace(",", " , ");
				ss = ss.replaceAll("\\(", " \\( ");
				ss = ss.replaceAll("\\)", " \\) ");
				ss = Clean(ss);				
				while (ss.indexOf("  ")>-1) {
					ss = ss.replace("  ", " ");
				}		
				String[] WordsSplit = ss.split("[\\s]");
				int leng = WordsSplit.length;
				for(int j=0;j<leng;j++) {
					 String w = WordsSplit[j];
					
				     if(!w.trim().equals("")) {				    	 
				    	 String wordValue = w;
				    	 if (w.equals(".") && j<leng && eLine.Words.size() > 0) {
				    		 String ww = eLine.Words.get(eLine.Words.size()-1);
				    		 Character ch = ww.charAt(0);
				    		 if (Character.isDigit(ch) || ww.startsWith("-")) {
				    			 ch = WordsSplit[j+1].charAt(0);
				    			 if (Character.isDigit(ch)) {
				    				 wordValue = ww + "." + WordsSplit[j+1];
				    				 eLine.Words.remove(eLine.Words.size()-1);
				    				 j+=1;
				    			 }
				    		 }
				    	 }
				    	 eLine.Words.add(wordValue);				    	 
				       }
				}				
				Lines.add(eLine);
			}
			
			ExtractMethods(OrderedFiles);
				
			moduleJS = new LinkedHashMap<String,B4JSExtractResultMethod>();
	    	line = 0;
	    	ind=1;
	    	while (line<Lines.size()) {
	    		currentLine = Lines.get(line);
	    		index=0;	    		
	    		String next = NextToken();
	    		switch (next) {
	    		case "private":

	    		case "public":
	    			next = NextToken();
	    		case "sub":
	    			methodJS = new StringBuilder();
	    			fullMethodName = NextToken();
	    			method = Methods.getOrDefault(fullMethodName, null);	    			
	    			if (method!=null) {
	    				isGlobal = fullMethodName.equalsIgnoreCase("process_globals") || fullMethodName.equalsIgnoreCase("class_globals");
	    				int checkName = fullMethodName.indexOf("_");
	    				if (checkName>0 && !isGlobal) {
	    					methodName = fullMethodName.toLowerCase();
	    				} else {
	    					methodName = fullMethodName.toLowerCase();
	    				}
	    				
    					if (!isGlobal) {
    						depth=1;
    						next = NextToken();	    						
    						Map<String, B4JSExtractVariable> tmpVars = GetMethodParams();
    						
    						for (Entry<String, B4JSExtractVariable> entryVar: tmpVars.entrySet()) {
    							if (IsTrackingVars) {
    								BA.Log("[1:" + ClassName + "," + fullMethodName + ", line: " + currentLine.LineNumber + "] Adding var: " + entryVar.getKey());
    							}
    							Vars.put(entryVar.getKey(), entryVar.getValue().Clone());
    						}
    						methodJS.append(GetInd(ind) + "this." + methodName + "=function(" + BuildMethodParams(tmpVars) + "){" + NL);
    						if (methodName.equalsIgnoreCase("initializeb4js")) {
    							methodJS.append(GetInd(ind+1) + "self=this;" + NL);
	    					}
    						ind++;
    						methodJS.append(GetInd(ind) + "try {" + NL);
    					} else {
    						depth=0;
    					}
    					ind++;
    					line++;
    					ProcessMethodDeclaration();
    					depth=1;
    					CleanVariables();
    					ind--;
    					if (!isGlobal) {
    						methodJS.append(GetInd(ind) + "}" + NL);
    						methodJS.append(GetInd(ind) + "catch(err) {" + NL);
    						if (!NL.equals("")) {
    							methodJS.append(GetInd(ind+1) + "console.log(err.message + ' ' + err.stack);" + NL);
    						}
    						methodJS.append(GetInd(ind) + "}" + NL);
    						ind--;
    						methodJS.append(GetInd(ind) + "};" + NL);	    						
    					}	    					
    					method.FinalJS = methodJS.toString();
    					
    					moduleJS.put(fullMethodName, method);
	    			} else {
	    				HasError=true;
	    				if (!isGlobal) {
	    					BA.LogError("ERROR: [" + ClassName + "," + fullMethodName + ", line: " + currentLine.LineNumber + "] Method " + fullMethodName + " not found!");
	    				}
	    			}
	    			break;
	    		}
	    		line++;
	    	}
	    	
	    	StringBuilder ModuleFinalJS = new StringBuilder();
	    	int BasicInt=-1;
	    	
	    	if (isFirst) {
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var b4jsglobal=this;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var DateTime;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "(function () {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var DateFormat = \"MM/dd/yyyy\";" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var TimeFormat = \"HH:mm:ss\";" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var _TZOffset = new Date().getTimezoneOffset()*-1/60;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var self = this;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var DateTime = {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "DateParse: function(s) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var ddres = Date.parse(s);" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return DateTime.WithTZ(ddres).getTime();" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "TimeParse: function(s) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var dd = new Date();" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "s = dd.format('yyyy-MM-dd') + ' ' + s;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var ddres = Date.parse(s);" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return DateTime.WithTZ(ddres).getTime();" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "Date: function(n) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var ddres = DateTime.WithTZ(n);" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return ddres.format(DateFormat);" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "Time: function(n) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var ddres = DateTime.WithTZ(n);" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return ddres.format(TimeFormat);" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "GetDateFormat: function() {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return DateFormat;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "SetDateFormat: function(s) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "DateFormat=s;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "GetTimeFormat: function() {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return TimeFormat;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "SetTimeFormat: function(s) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "TimeFormat=s;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "Add: function(dd, y, m, d) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var ddres = DateTime.WithTZ(dd);" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var res = DateTime.InnerAdd(ddres, y, 'y');" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "res = DateTime.InnerAdd(res, m, 'm');" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "res = DateTime.InnerAdd(res, d, 'd');" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return res.getTime();" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "InnerAdd: function(d, c, t){                        " + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "c = (c === undefined) ? 0 : c;			" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var result = null;			" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "switch(t){" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "case 'mill':" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "result = new Date(d.setMilliseconds(d.getMilliseconds()+c));" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "break;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "case 's':" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "result = new Date(d.setSeconds(d.getSeconds()+c));" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "break;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "case 'min':" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "result = new Date(d.setMinutes(d.getMinutes()+c));" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "break;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "case 'h':" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "result = new Date(d.setHours(d.getHours()+c));					" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "break;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "case 'd':" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "result = new Date(d.setDate(d.getDate()+c));" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "break;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "case 'm':" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "result = new Date(d.setMonth(d.getMonth()+c));" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "break;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "case 'y':" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "result = new Date(d.setFullYear(d.getFullYear()+c));" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "break;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "default:" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "console.error(\"[timeSolver.js] Input Type Error\");" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "break;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return result;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "WithTZ: function(n) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "if (_TZOffset!=new Date().getTimezoneOffset()*-1/60) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var d = DateTime.InnerAdd(new Date(n),new Date(n).getTimezoneOffset()/60, 'h');" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "d = DateTime.InnerAdd(d, _TZOffset, 'h');" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return d;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "} else {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return new Date(n);" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "Now: function() {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return new Date().getTime();" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "},		" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "GetYear: function(n) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return DateTime.WithTZ(new Date(n)).getFullYear();" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "GetMonth: function(n) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return DateTime.WithTZ(new Date(n)).getMonth()+1;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "GetDayOfMonth: function(n) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return DateTime.WithTZ(new Date(n)).getDate();" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "GetDayOfYear: function(n) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var t = new Date(n);" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var dayCount = [0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334];" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var mn = t.getMonth();" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var dn = t.getDate();" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var dayOfYear = dayCount[mn] + dn;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var year = t.getFullYear();" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var isLeap=false;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "if((year & 3) != 0) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "isLeap=false;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "} else {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "isLeap = ((year % 100) != 0 || (year % 400) == 0);" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "if(mn > 1 && isLeap) dayOfYear++;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return dayOfYear;			" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "GetDayOfWeek: function(n) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return DateTime.WithTZ(new Date(n)).getDay()+1;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "GetHour: function(n) {			" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return DateTime.WithTZ(new Date(n)).getHours();" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "GetMinute: function(n) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return DateTime.WithTZ(new Date(n)).getMinutes();" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "GetSecond: function(n) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return DateTime.WithTZ(new Date(n)).getSeconds();" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "GetTimeZoneOffsetAt: function(n) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "if (_TZOffset!=new Date().getTimezoneOffset()*-1/60) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return _TZOffset;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "} else {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return new Date(n).getTimezoneOffset()*-1/60;			" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "GetWeekInYear: function(n) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var onejan = new Date(n.getFullYear(), 0, 1);" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return Math.ceil( (((n - onejan) / 86400000) + onejan.getDay() + 1) / 7 );" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "GetWeekInMonth: function(n, exact) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var month = n.getMonth()" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + ", year = n.getFullYear()" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + ", firstWeekday = new Date(year, month, 1).getDay()" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + ", lastDateOfMonth = new Date(year, month + 1, 0).getDate()" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + ", offsetDate = n.getDate() + firstWeekday - 1" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + ", index = 1" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + ", weeksInMonth = index + Math.ceil((lastDateOfMonth + firstWeekday - 7) / 7)" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + ", week = index + Math.floor(offsetDate / 7)" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + ";" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "if (exact || week < 2 + index) return week;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return week === weeksInMonth ? index + 5 : week;" + NL);
	    		
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "TicksPerDay: function() {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return 86400000;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "TicksPerHour: function() {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return 3600000;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "TicksPerMinute: function() {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return 60000;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "TicksPerSecond: function() {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return 1000;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "TimeZoneOffset: function() {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return _TZOffset;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "SetTimeZone: function(n) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "_TZOffset = n;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "};	" + NL);
	    		
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "if (typeof module !== 'undefined' && typeof module.exports !== 'undefined') {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "module.exports = DateTime;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "} else {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "if (typeof window !== 'undefined') {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "window.DateTime = DateTime;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "} else {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "b4jsglobal.DateTime = DateTime;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}" + NL);
	    		
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "})();" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var dateFormat = function () {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var	token = /d{1,4}|m{1,4}|yy(?:yy)?|([HhMsTt])\\1?|[LloSZ]|\"[^\"]*\"|'[^']*'/g," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "timezone = /\\b(?:[PMCEA][SDP]T|(?:Pacific|Mountain|Central|Eastern|Atlantic) (?:Standard|Daylight|Prevailing) Time|(?:GMT|UTC)(?:[-+]\\d{4})?)\\b/g," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "timezoneClip = /[^-+\\dA-Z]/g," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "pad = function (val, len) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "val = String(val);" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "len = len || 2;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "while (val.length < len) val = \"0\" + val;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return val;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "};" + NL);
	    		
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return function (date, mask, utc) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var dF = dateFormat;" + NL);
	    		
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "if (arguments.length == 1 && Object.prototype.toString.call(date) == \"[object String]\" && !/\\d/.test(date)) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "mask = date;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "date = undefined;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}" + NL);
	    		
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "date = date ? new Date(date) : new Date;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "if (isNaN(date)) throw SyntaxError(\"invalid date\");" + NL);
	    		
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "if (mask.slice(0, 4) == \"UTC:\") {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "mask = mask.slice(4);" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "utc = true;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}" + NL);
	    		
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "var	_ = utc ? \"getUTC\" : \"get\"," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "d = date[_ + \"Date\"]()," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "D = date[_ + \"Day\"]()," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "m = date[_ + \"Month\"]()," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "y = date[_ + \"FullYear\"]()," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "H = date[_ + \"Hours\"]()," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "M = date[_ + \"Minutes\"]()," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "s = date[_ + \"Seconds\"]()," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "L = date[_ + \"Milliseconds\"]()," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "o = utc ? 0 : date.getTimezoneOffset()," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "DD = DateTime.GetDayOfYear(date)," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "w = DateTime.GetWeekInYear(date)," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "W = DateTime.GetWeekInMonth(date, true)," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "flags = {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "d:    d," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "dd:   pad(d)," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "E:    dF.i18n.dayNames[D]," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "EE:   dF.i18n.dayNames[D + 7]," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "M:    m + 1," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "MM:   pad(m + 1)," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "MMM:  dF.i18n.monthNames[m]," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "MMMM: dF.i18n.monthNames[m + 12]," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "yy:   String(y).slice(2)," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "yyyy: y," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "h:    H % 12 || 12," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "K:    pad(H % 12 || 12)," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "H:    H," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "HH:   pad(H)," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "m:    M," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "mm:   pad(M)," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "s:    s," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "ss:   pad(s)," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "S:    pad(L > 99 ? Math.round(L / 10) : L)," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "a:    H < 12 ? \"AM\" : \"PM\"," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "Z:    utc ? \"UTC\" : (String(date).match(timezone) || [\"\"]).pop().replace(timezoneClip, \"\")," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "z:    utc ? \"UTC\" : (String(date).match(timezone) || [\"\"]).pop().replace(timezoneClip, \"\")," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "X:    utc ? \"UTC\" : (String(date).match(timezone) || [\"\"]).pop().replace(timezoneClip, \"\")," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "o:    (o > 0 ? \"-\" : \"+\") + pad(Math.floor(Math.abs(o) / 60) * 100 + Math.abs(o) % 60, 4)," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "u:    D," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "D:	  DD," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "w:    w," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "W:    W" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "};" + NL);
	    		
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return mask.replace(token, function ($0) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return $0 in flags ? flags[$0] : $0.slice(1, $0.length - 1);" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "});" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "};" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "}();" + NL);
	    		
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "dateFormat.i18n = {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "dayNames: [" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "\"Sun\", \"Mon\", \"Tue\", \"Wed\", \"Thu\", \"Fri\", \"Sat\"," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "\"Sunday\", \"Monday\", \"Tuesday\", \"Wednesday\", \"Thursday\", \"Friday\", \"Saturday\"" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "]," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "monthNames: [" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "\"Jan\", \"Feb\", \"Mar\", \"Apr\", \"May\", \"Jun\", \"Jul\", \"Aug\", \"Sep\", \"Oct\", \"Nov\", \"Dec\"," + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "\"January\", \"February\", \"March\", \"April\", \"May\", \"June\", \"July\", \"August\", \"September\", \"October\", \"November\", \"December\"" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "]" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "};" + NL);
	    		
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "Date.prototype.format = function (mask, utc) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "return dateFormat(this, mask, utc);" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "};" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "String.prototype.equalsIgnoreCase = function(otherString) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "return (this.toUpperCase().localeCompare(otherString.toUpperCase())===0);" + NL);
		    	ModuleFinalJS.append(GetInd(BasicInt+1) + "};" + NL);
		    	ModuleFinalJS.append(GetInd(BasicInt+1) + "String.prototype.contains = function(otherString) {" + NL);
		    	ModuleFinalJS.append(GetInd(BasicInt+2) + "return (this.indexOf(otherString)>-1);" + NL);
		    	ModuleFinalJS.append(GetInd(BasicInt+1) + "};" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "String.prototype.getBytes = function() {" + NL);	    			 
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "var utf8 = [];" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "var str = this;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "for (var i=0; i < str.length; i++) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+3) + "var charcode = str.charCodeAt(i);" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+3) + "if (charcode < 0x80) utf8.push(charcode);" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+3) + "else if (charcode < 0x800) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+4) + "utf8.push(0xffffffc0 | (charcode >> 6), 0xffffff80 | (charcode & 0x3f));" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+3) + "} else if (charcode < 0xd800 || charcode >= 0xe000) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+4) + "utf8.push(0xffffffe0 | (charcode >> 12), 0xffffff80 | ((charcode>>6) & 0x3f), 0xffffff80 | (charcode & 0x3f));" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+3) + "} else {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+4) + "utf8.push(0xef, 0xbf, 0xbd);" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+3) + "}" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "return utf8;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "};" + NL);
		    	
				ModuleFinalJS.append(GetInd(BasicInt+1) + "function b4js_bytesToString(array) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "var str = '',i;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "var data = new Uint8Array(array);" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "for (i = 0; i < data.length; i++) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+3) + "var value = data[i];" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+3) + "if (value < 0x80) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+4) + "str += String.fromCharCode(value);" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+3) + "} else if (value > 0xBF && value < 0xE0) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+4) + "str += String.fromCharCode((value & 0x1F) << 6 | data[i + 1] & 0x3F);" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+4) + "i += 1;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+3) + "} else if (value > 0xDF && value < 0xF0) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+4) + "str += String.fromCharCode((value & 0x0F) << 12 | (data[i + 1] & 0x3F) << 6 | data[i + 2] & 0x3F);" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+4) + "i += 2;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+3) + "}" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "return str;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "};" + NL);
	    	
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "function b4js_getB4JKeyAt(outof, index) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "var getkeyatkeycounter=0;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "for (var getkeyatkey in outof) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+3) + "if (outof.hasOwnProperty(getkeyatkey)) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+4) + "if (getkeyatkeycounter==index) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+5) + "return getkeyatkey;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+4) + "}" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+3) + "getkeyatkeycounter++;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+3) + "}" + NL);				    
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "return '';" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "};" + NL);

	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "function b4js_getB4JValueAt(outof, index) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "var getkeyatkeycounter=0;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "for (var getkeyatkey in outof) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+3) + "if (outof.hasOwnProperty(getkeyatkey)) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+4) + "if (getkeyatkeycounter==index) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+5) + "return outof[getkeyatkey];" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+4) + "}" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+3) + "getkeyatkeycounter++;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+3) + "}" + NL);				    
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "return '';" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "};" + NL);

	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "function StringBuilder() {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "this.strings = new Array(\"\");" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "};" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "StringBuilder.prototype.append = function (value) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "var self=this;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "if (value) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+3) + "this.strings.push(value);" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "return self;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "};" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "StringBuilder.prototype.insert = function (offset, value) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "var self=this;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "var str = this.strings.join(\"\");" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "this.strings = new Array(\"\");" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "this.strings.push([str.slice(0, offset), value, str.slice(offset)].join(''));" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "return self;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "};" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "StringBuilder.prototype.remove = function (startoffset, endoffset) {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "var self=this;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "var str = this.strings.join(\"\");" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "this.strings = new Array(\"\");" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "this.strings.push([str.slice(0, startoffset), str.slice(endoffset)].join(''));" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "return self;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "};" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "StringBuilder.prototype.length = function() {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "return this.strings.join(\"\").length;" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "};" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "StringBuilder.prototype.toString = function () {" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+2) + "return this.strings.join(\"\");" + NL);
	    		ModuleFinalJS.append(GetInd(BasicInt+1) + "};" + NL);
	    		
				ModuleFinalJS.append(GetInd(BasicInt+1) + "function b4js_buildtext(_text,_onlynbsp){" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "try {" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+3) + "var _s=new StringBuilder();" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+3) + "var _v='' + _text;" + NL); //encodeURI(_text);" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+3) + "if (_onlynbsp) {" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "_v = _v.split(\"{NBSP}\").join(\" \");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+3) + "} else {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+4) + "_v = _v.split(\"(\\r\\n|\\n\\r|\\r|\\n)\").join(\"<br>\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "_v = _v.split(\"{B}\").join(\"<b>\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "_v = _v.split(\"{/B}\").join(\"</b>\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "_v = _v.split(\"{I}\").join(\"<i>\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "_v = _v.split(\"{/I}\").join(\"</i>\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "_v = _v.split(\"{U}\").join(\"<ins>\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "_v = _v.split(\"{/U}\").join(\"</ins>\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "_v = _v.split(\"{SUB}\").join(\"<sub>\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "_v = _v.split(\"{/SUB}\").join(\"</sub>\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "_v = _v.split(\"{SUP}\").join(\"<sup>\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "_v = _v.split(\"{/SUP}\").join(\"</sup>\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "_v = _v.split(\"{BR}\").join(\"<br>\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "_v = _v.split(\"{WBR}\").join(\"<wbr>\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "_v = _v.split(\"{NBSP}\").join(\"&nbsp;\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "_v = _v.split(\"{AL}\").join(\"<a rel=\\\"nofollow\\\" target=\\\"_blank\\\" href=\\\"\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "_v = _v.split(\"{AT}\").join(\"\\\">\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "_v = _v.split(\"{/AL}\").join(\"</a>\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "_v = _v.split(\"{AS}\").join(\" title=\\\"\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "_v = _v.split(\"{/AS}\").join(\"\\\"\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "while (_v.indexOf(\"{C:\")>-1) {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+5) + "_v = self.replacefirst(_v,\"{C:\",\"<span style=\\\"color:\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+5) + "_v = self.replacefirst(_v,\"}\",\"\\\">\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+5) + "_v = self.replacefirst(_v,\"{/C}\",\"</span>\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "}" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "_v = _v.split(\"{CODE}\").join(\"<div class=\\\"abmcode\\\"><pre><code>\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "_v = _v.split(\"{/CODE}\").join(\"</code></pre></div>\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "while (_v.indexOf(\"{ST:\")>-1) {" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+5) + "_v = self.replacefirst(_v,\"{ST:\",\"<span style=\\\"\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+5) + "_v = self.replacefirst(_v,\"}\",\"\\\">\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+5) + "_v = self.replacefirst(_v,\"{/ST}\",\"</span>\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "}" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "var _start=_v.indexOf(\"{IC:\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "while (_start>-1) {" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+5) + "var _stop=_v.indexOf(\"{/IC}\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+5) + "var _vv=\"\";" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+5) + "if (_stop>0) {" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+6) + "_vv = _v.substring(_start,_stop+5);" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+5) + "} else {" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+6) + "break;" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+5) + "}" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+5) + "var _iconcolor=_vv.substring(4,11);" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+5) + "var _iconname=_vv.substring(12,_vv.length-5);" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+5) + "var _repl=\"\";" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+5) + "switch (_iconname.substring(0,3).toLowerCase()) {" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+6) + "case \"mdi\":" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+7) + "_repl = \"<i style=\\\"color: \"+_iconcolor+\"\\\" class=\\\"\"+_iconname+\"\\\"></i>\";" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+7) + "break;" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+6) + "case \"fa \":" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+7) + "_repl = \"<i style=\\\"color: \"+_iconcolor+\"\\\" class=\\\"\"+_iconname+\"\\\"></i>\";" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+7) + "break;" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+6) + "case \"fa-\":" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+7) + "_repl = \"<i style=\\\"color: \"+_iconcolor+\"\\\" class=\\\"\"+_iconname+\"\\\"></i>\";" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+7) + "break;" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+6) + "default:" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+7) + "_repl = \"<i style=\\\"color: \"+_iconcolor+\"\\\" class=\\\"material-icons\\\">\"+_iconname+\"</i>\";" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+7) + "break;" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+5) + "}" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+5) + "_v = _v.split(_vv).join(_repl);" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+5) + "_start = _v.indexOf(\"{IC:\");" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "}" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+3) + "}" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+3) + "_s.append(_v);" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+3) + "return _s.toString();" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "catch(err) {" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+3) + "console.log(err.message + ' ' + err.stack);" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+1) + "};" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+1) + "function replacefirst(_s,_searchfor,_replacewith){" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "try {" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+3) + "var _i=_s.indexOf(_searchfor);" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+3) + "if (_i>-1) {" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "return _s.substring(0,_i)+_replacewith+_s.substring(_i+_searchfor.length);" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+3) + "} else {" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+4) + "return _s;" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+3) + "}" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "catch(err) {" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+3) + "console.log(err.message + ' ' + err.stack);" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+1) + "};" + NL);
	    		
	    	    ModuleFinalJS.append(GetInd(BasicInt+1) + "function b4js_msgbox(ThemeName, Title, HTML, confirmText, pos, select, targetObj,_b4js_returnname) {" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "swal({" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "background: _b4jsthemes[\"msgbox-\" + ThemeName].BackColor," + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "rtl: _b4jsthemes[\"msgbox-\" + ThemeName].RightToLeft," + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "title: b4js_buildtext(Title, false)," + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "html: b4js_buildtext(HTML, false)," + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "type: 'info'," + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "titleColor: _b4jsthemes[\"msgbox-\" + ThemeName].TitleTextColor," + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "contentColor: _b4jsthemes[\"msgbox-\" + ThemeName].MessageTextColor," + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "imageUrl: _b4jsthemes[\"msgbox-\" + ThemeName].ImageUrl," + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "imageWidth: _b4jsthemes[\"msgbox-\" + ThemeName].ImageWidth," + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "imageHeight: _b4jsthemes[\"msgbox-\" + ThemeName].ImageHeight," + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "confirmButtonColor: _b4jsthemes[\"msgbox-\" + ThemeName].ConfirmButtonColor," + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "confirmButtonTextColor: _b4jsthemes[\"msgbox-\" + ThemeName].ConfirmButtonTextColor," + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "confirmButtonText: b4js_buildtext(confirmText, false)," + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "showCancelButton: false," + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "allowOutsideClick: false," + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "allowEscapeKey: false," + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "buttonsStyling: true" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "}).then(" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "function(){" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "if (typeof targetObj.page_b4jsmsgboxresult === \"function\") {" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "if (!targetObj.page_b4jsmsgboxresult(_b4js_returnname,'abmok')) {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+3) + "b4j_raiseEvent('page_parseevent', {'eventname': 'page_msgboxresult','eventparams':'returnname,result','returnname': _b4js_returnname,'result':'abmok'});" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + ").catch(swal.noop)\n");
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "$('.swal2-modal').alterClass('swal2pos-*',pos);" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "if (!select) {" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "$('.swal2-container').removeClass('notselectable');" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "$('.swal2-container').addClass('notselectable')" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "} else {" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "$('.swal2-container').removeClass('notselectable')" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "$('.swal2-container').find('.swal2-icon').css({'color': _b4jsthemes[\"msgbox-\" + ThemeName].IconColor, 'border-color': _b4jsthemes[\"msgbox-\" + ThemeName].IconColor});" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "$('.swal2-container').find('.swal2-close').css({'color': _b4jsthemes[\"msgbox-\" + ThemeName].CloseButtonColor});" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "$('.swal2-container').find('.swal2-validationerror').css({'background-color': _b4jsthemes[\"msgbox-\" + ThemeName].BackColor});" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+1) + "};" + NL);
	    	    
	    	    ModuleFinalJS.append(GetInd(BasicInt+1) + "function b4js_msgbox2(ThemeName, Title, HTML, type, showClose, confirmText, cancelText, allowOutside, pos, select, targetObj,_b4js_returnname) {" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "swal({" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "background: _b4jsthemes[\"msgbox-\" + ThemeName].BackColor," + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "rtl: _b4jsthemes[\"msgbox-\" + ThemeName].RightToLeft," + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "title: b4js_buildtext(Title, false)," + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "html: b4js_buildtext(HTML, false)," + NL);				
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "type: type," + NL);					
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "showCloseButton: showClose," + NL);					
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "titleColor: _b4jsthemes[\"msgbox-\" + ThemeName].TitleTextColor," + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "contentColor: _b4jsthemes[\"msgbox-\" + ThemeName].MessageTextColor," + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "imageUrl: _b4jsthemes[\"msgbox-\" + ThemeName].ImageUrl," + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "imageWidth: _b4jsthemes[\"msgbox-\" + ThemeName].ImageWidth," + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "imageHeight: _b4jsthemes[\"msgbox-\" + ThemeName].ImageHeight," + NL);
				
				ModuleFinalJS.append(GetInd(BasicInt+2) + "showConfirmButton: (confirmText.length != 0)," + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "confirmButtonColor: _b4jsthemes[\"msgbox-\" + ThemeName].ConfirmButtonColor," + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "confirmButtonTextColor: _b4jsthemes[\"msgbox-\" + ThemeName].ConfirmButtonTextColor," + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "confirmButtonText: b4js_buildtext(confirmText, false)," + NL);
				
    			ModuleFinalJS.append(GetInd(BasicInt+2) + "showCancelButton: (cancelText.length != 0)," + NL);
    			ModuleFinalJS.append(GetInd(BasicInt+2) + "cancelButtonColor: _b4jsthemes[\"msgbox-\" + ThemeName].CancelButtonColor," + NL);
    			ModuleFinalJS.append(GetInd(BasicInt+2) + "cancelButtonTextColor: _b4jsthemes[\"msgbox-\" + ThemeName].CancelButtonTextColor," + NL);
    			ModuleFinalJS.append(GetInd(BasicInt+2) + "cancelButtonText: b4js_buildtext(cancelText, false)," + NL);
    			
				ModuleFinalJS.append(GetInd(BasicInt+2) + "allowOutsideClick: allowOutside," + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "allowEscapeKey: allowOutside," + NL);
				
				ModuleFinalJS.append(GetInd(BasicInt+2) + "buttonsStyling: true" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}).then(" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "function(){" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "if (typeof targetObj.page_b4jsmsgboxresult === \"function\") {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "if (!targetObj.page_b4jsmsgboxresult(_b4js_returnname,'abmok')) {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+3) + "b4j_raiseEvent('page_parseevent', {'eventname': 'page_msgboxresult','eventparams':'returnname,result','returnname': _b4js_returnname,'result':'abmok'});" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
				
				ModuleFinalJS.append(GetInd(BasicInt+2) + ", function(dismiss){" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "if (typeof targetObj.page_b4jsmsgboxresult === \"function\") {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "if (!targetObj.page_b4jsmsgboxresult(_b4js_returnname,dismiss)) {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+3) + "b4j_raiseEvent('page_parseevent', {'eventname': 'page_msgboxresult','eventparams':'returnname,result','returnname': _b4js_returnname,'result':dismiss});" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
				
				ModuleFinalJS.append(GetInd(BasicInt+2) + ").catch(swal.noop)\n");
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "$('.swal2-modal').alterClass('swal2pos-*',pos);" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "if (!select) {" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "$('.swal2-container').removeClass('notselectable');" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "$('.swal2-container').addClass('notselectable')" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "} else {" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "$('.swal2-container').removeClass('notselectable')" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "$('.swal2-container').find('.swal2-icon').css({'color': _b4jsthemes[\"msgbox-\" + ThemeName].IconColor, 'border-color': _b4jsthemes[\"msgbox-\" + ThemeName].IconColor});" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "$('.swal2-container').find('.swal2-close').css({'color': _b4jsthemes[\"msgbox-\" + ThemeName].CloseButtonColor});" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "$('.swal2-container').find('.swal2-validationerror').css({'background-color': _b4jsthemes[\"msgbox-\" + ThemeName].BackColor});" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+1) + "};" + NL);
	    	    		    	    
	    	    ModuleFinalJS.append(GetInd(BasicInt+1) + "function b4js_inputbox(ThemeName, Title, Input, InputValue, invalidInputText, InputOptions, InputPlaceholder, Type, showCloseButton, confirmText, cancelText, allowOutside, pos, select, targetObj,_b4js_returnname) {" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "var swaloptions={};" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.background=_b4jsthemes[\"msgbox-\" + ThemeName].BackColor;" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.rtl=_b4jsthemes[\"msgbox-\" + ThemeName].RightToLeft;" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.title=b4js_buildtext(Title, false);" + NL);					
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.input=Input;" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "if (InputValue!='') {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.inputValue=InputValue;" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "if (Input=='email') {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.invalidEmail=invalidInputText;" + NL);	
				ModuleFinalJS.append(GetInd(BasicInt+2) + "} else {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "if (invalidInputText!='') {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.inputValidator=function (result) {return new Promise(function (resolve, reject) {if (result) {resolve();} else {reject(invalidInputText);}})};" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);			
				ModuleFinalJS.append(GetInd(BasicInt+2) + "if (!$.isEmptyObject(InputOptions)) {" + NL);					
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.inputOptions=InputOptions;" + NL);				
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "if (InputPlaceholder!='') {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.inputPlaceholder=InputPlaceholder;" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.type=Type;" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.showCloseButton=showCloseButton;" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.titleColor=_b4jsthemes[\"msgbox-\" + ThemeName].TitleTextColor;" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.contentColor=_b4jsthemes[\"msgbox-\" + ThemeName].MessageTextColor;" + NL);				
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.imageUrl=_b4jsthemes[\"msgbox-\" + ThemeName].ImageUrl;" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.imageWidth=_b4jsthemes[\"msgbox-\" + ThemeName].ImageWidth;" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.imageHeight=_b4jsthemes[\"msgbox-\" + ThemeName].ImageHeight;" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "if (confirmText!='') {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.showConfirmButton=true;" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.confirmButtonColor=_b4jsthemes[\"msgbox-\" + ThemeName].ConfirmButtonColor;" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.confirmButtonTextColor=_b4jsthemes[\"msgbox-\" + ThemeName].ConfirmButtonTextColor;" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.confirmButtonText=b4js_buildtext(confirmText, false);" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "} else {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.showConfirmButton=false;" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);		
				ModuleFinalJS.append(GetInd(BasicInt+2) + "if (cancelText!='') {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.showCancelButton=true;" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.cancelButtonColor=_b4jsthemes[\"msgbox-\" + ThemeName].CancelButtonColor;" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.cancelButtonTextColor=_b4jsthemes[\"msgbox-\" + ThemeName].CancelButtonTextColor;" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.cancelButtonText=b4js_buildtext(cancelText, false);" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "} else {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.showCancelButton=false;" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "if (!allowOutside) {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.allowOutsideClick=false;" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.allowEscapeKey=false;" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.buttonsStyling=true;" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.checkboxTextColor=_b4jsthemes[\"msgbox-\" + ThemeName].CheckboxTextColor;" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.radioTextColor=_b4jsthemes[\"msgbox-\" + ThemeName].RadioTextColor;" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.textareaTextColor=_b4jsthemes[\"msgbox-\" + ThemeName].TextAreaTextColor;" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swaloptions.inputTextClass='input-field input-field' + _b4jsthemes[\"msgbox-\" + ThemeName].InputFieldTheme;" + NL);					
				ModuleFinalJS.append(GetInd(BasicInt+2) + "swal(swaloptions).then(" + NL);					
				ModuleFinalJS.append(GetInd(BasicInt+2) + "function(value){" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "if (Input=='checkbox') {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "var ret='0';" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "if (value) {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "ret='1';" + NL);	
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "if (typeof targetObj.page_b4jsinputboxresult === \"function\") {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "if (!targetObj.page_b4jsinputboxresult(_b4js_returnname,ret)) {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+3) + "b4j_raiseEvent('page_parseevent', {'eventname': 'page_inputboxresult','eventparams':'returnname,result','returnname': _b4js_returnname,'result':ret});" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "} else {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "if (typeof targetObj.page_b4jsinputboxresult === \"function\") {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "if (!targetObj.page_b4jsinputboxresult(_b4js_returnname,value)) {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+3) + "b4j_raiseEvent('page_parseevent', {'eventname': 'page_inputboxresult','eventparams':'returnname,result','returnname': _b4js_returnname,'result':value});" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + ", function(dismiss){" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "if (cancelText!='') {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "if (typeof targetObj.page_b4jsinputboxresult === \"function\") {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "if (targetObj.page_b4jsinputboxresult(_b4js_returnname,dismiss)) {" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+3) + "b4j_raiseEvent('page_parseevent', {'eventname': 'page_inputboxresult','eventparams':'returnname,result','returnname': _b4js_returnname,'result':dismiss});" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
				ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);					
				ModuleFinalJS.append(GetInd(BasicInt+2) + ").catch(swal.noop)\n");
									
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "$('.swal2-modal').alterClass('swal2pos-*',pos);" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "if (!select) {" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "$('.swal2-container').removeClass('notselectable');" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "$('.swal2-container').addClass('notselectable')" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "} else {" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "$('.swal2-container').removeClass('notselectable')" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "}" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "$('.swal2-container').find('.swal2-icon').css({'color': _b4jsthemes[\"msgbox-\" + ThemeName].IconColor, 'border-color': _b4jsthemes[\"msgbox-\" + ThemeName].IconColor});" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "$('.swal2-container').find('.swal2-close').css({'color': _b4jsthemes[\"msgbox-\" + ThemeName].CloseButtonColor});" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+2) + "$('.swal2-container').find('.swal2-validationerror').css({'background-color': _b4jsthemes[\"msgbox-\" + ThemeName].BackColor});" + NL);
	    	    ModuleFinalJS.append(GetInd(BasicInt+1) + "};" + NL);
	    	    		    	    
	    	    B4JSClassGenerated.put("b4js", ModuleFinalJS.toString());
	    	}
	    	
	    	ModuleFinalJS = new StringBuilder();
	    	
	    	if (!ExtraJS.toString().equals("")) {
				ModuleFinalJS.append(GetInd(BasicInt+1) + ExtraJS.toString() + NL);
			}
	    	
	    	ModuleFinalJS.append("function b4js_" + ClassName.toLowerCase() + "() {" + NL);
	    	ModuleFinalJS.append(GetInd(BasicInt+2) + "var self;" + NL);
	    		
			for (Entry<String,B4JSExtractResultMethod> methentry: moduleJS.entrySet()) {
				B4JSExtractResultMethod meth = methentry.getValue();
				ModuleFinalJS.append(meth.FinalJS);					
			}
			
			ModuleFinalJS.append("};" + NL);
			
			if (isLast) {
				for (Entry<String,String>entry: ABMaterial.GlobalVars.entrySet()) {
					ModuleFinalJS.append(entry.getValue());
				}
			}
				
			B4JSClassGenerated.put("b4js_" + ClassName.toLowerCase(), ModuleFinalJS.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	protected void ExtractMethods(Map<String,File> OrderedFiles) {
		String MethodName="";
		B4JSExtractResultMethod method=null;
		Map<String, Boolean> allMethods = new LinkedHashMap<String, Boolean>();	
		
				
		for (int i=0;i<Lines.size();i++) {
			String s = Lines.get(i).CurrentLine;
			
			if (s.startsWith("sub ") || s.startsWith("private sub ") || s.startsWith("public sub ")) {
				int k = s.indexOf("sub ");
				int j = s.indexOf("(");
				if (j>-1) {
					MethodName = s.substring(k+4, j).trim();
				} else {
					j = s.indexOf(" ",k+5);
					if (j>-1) {
						MethodName = s.substring(k+4, j).trim();
					} else {
						MethodName = s.substring(k+4).trim();
					}					
				}				
				method = new B4JSExtractResultMethod(MethodName, ClassName);
				if (s.startsWith("private")) {
					method.Scope = SCOPE.PRIVATE;
				}
				
				
				method.Lines.add(Lines.get(i));
				
				
				allMethods.put(MethodName.toLowerCase(), true);
			} else {
				if (s.startsWith("end sub")) {
					
					method.Lines.add(Lines.get(i));					
					
					Methods.put(MethodName.toLowerCase(), method);					
					MethodName="";
				} else {
					if (!MethodName.equals("")) {
						
						method.Lines.add(Lines.get(i));
						
						String[] spl = s.split(",");
						for (int sp=0;sp<spl.length;sp++) {
							int j = spl[sp].indexOf(" as abm");
							if (j>-1) {
								String varName = spl[sp].substring(0, j);
								String varType="";
								int k1 = spl[sp].indexOf("=",j);
								int k2 = spl[sp].indexOf(",",j);
								int k=999999999;
								if (k1>-1) {
									k=k1;
								}
								if (k2<k && k2>-1) {
									k=k2;
								}
								if (k!=999999999) {
									varType = spl[sp].substring(j+4,k).trim();
								} else {
									varType = spl[sp].substring(j+4).trim();
								}
								method.Needs.put(varType.toLowerCase(), true);
								if (MethodName.equalsIgnoreCase("process_globals")) {
									method.GlobalNeeds.put(varType.toLowerCase(), varName);
								}
							} 
							  else {
								j = spl[sp].indexOf(" as ");
								if (j>-1) {
									String varType="";
									int k1 = spl[sp].indexOf("=",j);
									int k2 = spl[sp].indexOf(",",j);
									int k=999999999;
									if (k1>-1) {
										k=k1;
									}
									if (k2<k && k2>-1) {
										k=k2;
									}
									if (k!=999999999) {
										varType = spl[sp].substring(j+4,k).trim();
									} else {
										varType = spl[sp].substring(j+4).trim();
									}
									if (OrderedFiles.containsKey(varType.toLowerCase())) {										
										
									}
								}
							}
													
							for (Entry<String,File> entry: OrderedFiles.entrySet()) {
								int j1 = spl[sp].indexOf(" as " + entry.getKey().toLowerCase() + " ");
								int j2 = spl[sp].indexOf(" as " + entry.getKey().toLowerCase() + "\t");
								int j3 = spl[sp].indexOf(" as " + entry.getKey().toLowerCase() + "\n");
								int j4 = spl[sp].indexOf(" as " + entry.getKey().toLowerCase() + ",");
								j=999999999;
								if (j1>-1) {
									j=j1;
								}
								if (j2<j && j2>-1) {
									j=j2;
								}
								if (j3<j && j3>-1) {
									j=j3;
								}
								if (j4<j && j4>-1) {
									j=j4;
								}
								if (j!=999999999) {
									String varName = spl[sp].substring(0, j);
									String varType="";
									int k = spl[sp].indexOf("=",j);
									if (k>-1) {
										varType = spl[sp].substring(j+4,k).trim();
									} else {
										varType = spl[sp].substring(j+4).trim();
									}
									
									method.Needs.put(varType.toLowerCase(), true);
									if (MethodName.equalsIgnoreCase("process_globals")) {
										method.GlobalNeeds.put(varType.toLowerCase(), varName);
									}
								}
							}
						}
						char[] search = {'(', ' ', ')', '\n'};
						for (Entry<String,File> entry: OrderedFiles.entrySet()) {
							String v = entry.getKey().toLowerCase();
							int j=s.indexOf(v + ".");
							if (j>-1) {
								int g = indexOfAny(j,s, search);
								if (g>-1) {
									String call = s.substring(j+v.length()+1, g);
									int j1=call.indexOf(".");
									int j2=call.indexOf(",");
									j=999999999;
									if (j1>-1) {
										j=j1;
									}
									if (j2<j && j2>-1) {
										j=j2;
									}
									if (j!=999999999) {
										call=call.substring(0, j);
									}
									
									method.Calls.put(call, v);
								}
							}
						}
					}
				}
			}
		}
		// check call in your own
		char[] search = {'(', ' ', ')','\n'};
		for (int i=0;i<Lines.size();i++) {

			String s = Lines.get(i).CurrentLine;
			if (s.startsWith("sub ") || s.startsWith("private sub ") || s.startsWith("public sub ")) {
					
					int k = s.indexOf("sub ");
					int j = s.indexOf("(");
					if (j>-1) {
						MethodName = s.substring(k+4, j).trim();
					} else {
						j = s.indexOf(" ",k+5);
						if (j>-1) {
							MethodName = s.substring(k+4, j).trim();
						} else {
							MethodName = s.substring(k+4).trim();
						}
					}		
					method = Methods.get(MethodName.toLowerCase());					
			} else {
					if (s.trim().startsWith("end sub")) {
						MethodName="";
					} else {
						if (!MethodName.equals("")) {
							for (Entry<String,Boolean> entry: allMethods.entrySet()) {								
								String v = entry.getKey().toLowerCase();
								int j=s.indexOf(v);
								if (j>-1) {
									int g = indexOfAny(j,s, search);
									if (g>-1) {
										String call = s.substring(j, g);
										
										method.OwnCalls.put(call, true);
									}
								}
							}
						}
					}
			}	
		}
	}
	
	
	@Hide
	protected Map<String,String> GetUsedModules(File fn) {
		Map<String,String> files = new LinkedHashMap<String, String>();
		BufferedReader br=null;
		try {
			String s;
			br = new BufferedReader(new FileReader(fn));
			StringBuilder allS = new StringBuilder(); 
			while ((s = br.readLine()) != null) {
				String sEx = s.toLowerCase();
				if (sEx.contains("@endofdesigntext@")) {
					
					br.close();
					return files;
				}
				if (sEx.startsWith("module")) {
					int i = sEx.indexOf("=");
					sEx = s.substring(i+1);
										
					files.put(sEx, "extract");
				}

				allS.append(s + " \n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}	
		return files;
	}
	
	protected String RemoveBetween(String s, String first, String last) {
		int begin=s.indexOf(first);
		int stop=s.indexOf(last, begin+1);
		int len = last.length();
		while (begin>-1 && stop>-1) {
			
			s = s.substring(0,begin) + s.substring(stop+len);
			begin=s.indexOf(first);
			stop=s.indexOf(last, begin+1);
		}
		return s;
	}
	
	protected String RemoveBetweenNoEnter(String s, String first, String last) {
		int begin=s.indexOf(first);
		int stop=s.indexOf(last, begin+1);
		int enter = s.indexOf("\n",begin+1);
		if (enter>begin && enter <stop) {
			stop=-1;
		}
		int len = last.length();
		while (begin>-1 && stop>-1) {
			
			s = s.substring(0,begin) + s.substring(stop+len);
			begin=s.indexOf(first);
			stop=s.indexOf(last, begin+1);
			enter = s.indexOf("\n",begin+1);
			if (enter>begin && enter <stop) {
				stop=-1;
			}
		}
		return s;
	}	
	
	protected String ExtractJavascript(String s) {
		String first = "#if javascript";
		String last = "#end if";
		String sLC = s.toLowerCase();
		int len = last.length();
		int firstlen = first.length();
		int begin=sLC.indexOf(first);
		while (begin>-1) {
			int stop=sLC.indexOf(last, begin+1);
			ExtraJS.append(s.substring(begin+firstlen,stop));
			s = s.substring(0,begin) + s.substring(stop+len);
			begin=s.indexOf(first);
		}		
		return s;
	}
	
	protected String Phase1(String s, String first, String last) {		
		s = s.replace("\"${", "\"~VV~");
		
		int begin=s.indexOf(first);
		int stop=s.indexOf(last, begin+1);			
		int len = last.length();
		counter=0;
		while (begin>-1 && stop>-1) {
			String tmpS = s.substring(begin,stop+len).replace("$\"", "").replace("\"$", "");
			tmpS = tmpS.replaceAll("\"", "\\\\\"");
			tmpS = tmpS.replaceAll("\r\n", "~BR~");
			tmpS = tmpS.replaceAll("\n", "~BR~");
			tmpS = PrepareVars1(tmpS, "${", "}");
			tmpS = PrepareVars2(tmpS, "~VV~", "}");
			GlobalStrings.put("{" + counter  + "}", tmpS) ;
			s = s.substring(0,begin) + " {" + counter  + "} " + s.substring(stop+len);
			begin=s.indexOf(first);
			stop=s.indexOf(last, begin+1);				
			counter++;
		}			
		return s;
	}
	
	protected String PrepareVars1(String s, String first, String last) {
		int begin=s.indexOf(first);
		int stop=s.indexOf(last, begin+1);
		int len = last.length();
		while (begin>-1 && stop>-1) {
			String tmpS = s.substring(begin,stop+len).replace("${", "").replace("}", "");
			tmpS = tmpS.toLowerCase();
			if (tmpS.contains(".") || tmpS.contains("(")) {
				BA.LogError("Error: SmartString variables can only contain simple variables.  Put " + tmpS + " in a simple variable first!");
			}
			GlobalSmartStrings.put("{~" + varCounter  + "}", tmpS) ;
			s = s.substring(0,begin) + "\" + {~" + varCounter  + "} + \"" + s.substring(stop+len);
			begin=s.indexOf(first);
			stop=s.indexOf(last, begin+1);
			varCounter++;
		}
		return s;
	}
	
	protected String PrepareVars2(String s, String first, String last) {
		int begin=s.indexOf(first);
		int stop=s.indexOf(last, begin+1);
		int len = last.length();
		while (begin>-1 && stop>-1) {
			String tmpS = s.substring(begin,stop+len).replace("~VV~", "").replace("}", "");
			tmpS = tmpS.toLowerCase();
			if (tmpS.contains(".") || tmpS.contains("(")) {
				BA.LogError("Error: SmartString variables can only contain simple variables.  Put " + tmpS + " in a simple variable first!");
			}
			GlobalSmartStrings.put("{~" + varCounter  + "}", tmpS) ;
			s = s.substring(0,begin) + "\" + {~" + varCounter  + "} + \"" + s.substring(stop+len);
			begin=s.indexOf(first);
			stop=s.indexOf(last, begin+1);
			varCounter++;
		}
		return s;
	}
	
	protected String Phase2(String s, String first, String last) {
		int begin=s.indexOf(first);
		int stop=s.indexOf(last, begin+1);
		int enter = s.indexOf("\n",begin+1);
		
		while (enter>begin && enter <stop && enter>-1 && begin > -1 && stop > -1) {
			begin=stop;
			stop=s.indexOf(last, begin+1);
			enter = s.indexOf("\n",begin+1);
		}		
		
		int len = last.length();
		while (begin>-1 && stop>-1) {
			GlobalStrings.put("{" + counter  + "}", s.substring(begin+1,stop+len-1).replaceAll("\r\n", "\\n"));
			s = s.substring(0,begin) + "~¨{" + counter  + "}~¨" + s.substring(stop+len);
			begin=s.indexOf(first);
			stop=s.indexOf(last, begin+1);
			enter = s.indexOf("\n",begin+1);
			
			while (enter>begin && enter <stop && enter>-1 && begin > -1 && stop > -1) {
				begin=stop;
				stop=s.indexOf(last, begin+1);
				enter = s.indexOf("\n",begin+1);
			}
			
			counter++;
		}	

		int i = s.indexOf("}~¨~¨{");
		int k=0;
		while (i>0) {
			String c1 = "}";
			k = i-1;			
			while (k>=0 && s.charAt(k)!='{') {
				c1 = s.substring(k, k+1) + c1; 
				k--;
			}	
			c1 = "{" + c1;
			String c2 = "{";
			k = i+6;
			while (k<s.length() && s.charAt(k)!='}') {
				c2+=s.substring(k, k+1); 
				k++;
			}
			c2+="}";
			String s1 = GlobalStrings.get(c1);
			String s2 = GlobalStrings.get(c2);
			GlobalStrings.remove(c1);
			GlobalStrings.remove(c2);
			s1=s1 + "\\\"" + s2;
			GlobalStrings.put("{" + counter  + "}", s1) ;
			s = s.replace("~¨" + c1 + "~¨~¨" + c2 + "~¨", "~¨{" + counter  + "}~¨");
			counter++;
			i = s.indexOf("}~¨~¨{");
		}
		s = s.replace("~¨", " ");
		return s;
	}
	
	protected String GetString(String value) {
		if (GlobalStrings.containsKey(value)) {
			return "\"" + GlobalStrings.get(value) + "\"";
		}
		return value;
	}	
	
	protected String GetSmartString(String value) {
		if (GlobalStrings.containsKey(value)) {
			return GlobalStrings.get(value);
		}
		return value;
	}	
	
	protected String ltrim(String s) {
	    return LTRIM.matcher(s).replaceAll("");
	}

	protected String rtrim(String s) {
	    return RTRIM.matcher(s).replaceAll("");
	}
	 
	protected int indexOfAny(int begin, String str, char[] searchChars) {
		   if (str.equals("") || searchChars.length==0) {
		     return -1;
		   }
		   for (int i = begin; i < str.length(); i++) {
		     char ch = str.charAt(i);
		       for (int j = 0; j < searchChars.length; j++) {
		         if (searchChars[j] == ch) {
		           return i;
		         }
		       }
		     }
		   return -1;
	 }
	
	protected String Clean(String s) {	
		
		int j = s.indexOf("'");
		if (j>-1) {
			return Left(s,j).trim() + " ";
		}		
		return s.trim();
	}
	
	@Hide
	protected List<String> Splitter(String s) {
		List<String> tokensList = new ArrayList<String>();
		boolean inQuotes = false;
		StringBuilder b = new StringBuilder();
		for (char c : s.toCharArray()) {
		    switch (c) {
		    case ',':
		        if (inQuotes) {
		            b.append(c);
		        } else {
		            tokensList.add(b.toString());
		            b = new StringBuilder();
		        }
		        break;
		    case '"':
		        inQuotes = !inQuotes;
		    default:
		        b.append(c);
		    break;
		    }
		}
		tokensList.add(b.toString());
		return tokensList;
	}
	
	@Hide
	protected String ExtractBetween(String s, String first, String last) {
		int i = s.indexOf(first);
		int j = s.indexOf("'");
		if (j > -1 && j < i) {
			return "";
		}
		if (s.indexOf(last)==-1) {
			return s.substring(s.indexOf(first) + first.length()).trim();
		}
		return s.substring(s.indexOf(first) + first.length(), s.indexOf(last)).trim();
	}
	
	@Hide
	protected String Left(String Text, int Length) {
	   if (Length>Text.length()) {
		   Length=Text.length();
	   }
	   return Text.substring(0, Length);
	}
	
	@Hide
	protected String Right(String Text, int Length) {
	   if (Length>Text.length()) {
		   Length=Text.length();
	   }
	   return Text.substring(Text.length() - Length);
	}

	@Hide
	protected String Mid(String Text, int Start, int Length) {
		return Text.substring(Start-1,Start+Length-1);
	}
	
	@Hide
	protected String Mid(String Text, int Start) {
		return Text.substring(Start-1);
	}
	
	@Hide
	protected String[] Split(String Text, String Delimiter) {
		return Regex.Split(Delimiter,Text);
	}
	
	@Hide
	protected int Len(String Text) {
		return Text.length();
	}
	
	@Hide
	protected String Replace(String Text, String sFind, String sReplaceWith) {
		return Text.replace(sFind, sReplaceWith);
	}
	
	@Hide
	protected String Trim(String Text) {
	    return Text.trim();
	}

	@Hide
	protected int Instr(String Text, String sFind) {
		return Text.indexOf(sFind);
	}
	
	@Hide
	protected int Instr(int iStart, String Text, String sFind) {
		return Text.indexOf(sFind, iStart);
	}
	
	@Hide
	protected int InstrRev(String Text, String sFind) {
		return Text.lastIndexOf(sFind);
	}
	
	protected Map<String, B4JSExtractVariable> GetMethodParams() {
		Map<String, B4JSExtractVariable> ret = new LinkedHashMap<String,B4JSExtractVariable>();
		String next = PeekToken(index);
		if (DebugTrack) {
			BA.Log(new PrettyPrintingList<String>(currentLine.Words).toString());
		}
		int inBracket=1;
		B4JSExtractVariable var = null;
		boolean isDefining=false;
		while (true) {
			switch (next) {				
				case "(":
					next = NextToken();
					inBracket++;
					if (var!=null) {
						Map<String, B4JSExtractVariable> internalRet = GetMethodParams();
						for(Iterator<Map.Entry<String, B4JSExtractVariable>> it = internalRet.entrySet().iterator(); it.hasNext(); ) {
							Map.Entry<String, B4JSExtractVariable> entry = it.next();
							var.Members.put(entry.getKey(), entry.getValue().Clone());
						}
					}
					break;
				case ")":
					next = NextToken();
					inBracket--;
					if (inBracket==0) {
						if (var!=null) {
							ret.put(var.Name, var.Clone());
						}
						return ret;
					}
					break;				
				case ",":
					next = NextToken();	
					break;					
				case "":
					if (var!=null) {
						ret.put(var.Name, var.Clone());
					}
					return ret;
				case "as":
					next = NextToken();
					isDefining = true;
					break;
				default:
					next = NextToken();
					if (isDefining) {
						if (var!=null) {
							var.Type = next;
							var.OrigType = var.Type;
							if (!CheckType(var.Type)) {
								HasError=true;
								if (!isGlobal) {
									BA.LogError("ERROR: [" + ClassName + "," + methodName + ", line: " + currentLine.LineNumber + "] The type '" + var.OrigType + "' cannot be transpiled to JavaScript!");
								} 
			    				var.ValidType = false;
							}
						}
						isDefining=false;
					} else {
						if (var!=null) {
							ret.put(var.Name, var.Clone());
						}
						var = new B4JSExtractVariable();
						var.Scope = SCOPE.PRIVATE;
						var.Name = next.toLowerCase();
						var.JSName = "_" + var.Name;
						var.Depth = depth;
					}
					break;
			}
			next = PeekToken(index);	
		}		
	}
	
	protected List<String> GetParams() {
		return GetParamsExtra(false);
	}
	
	protected List<String> GetParamsExtra(boolean showExtraDebug) {
		List<String> params = new ArrayList<String>();
		String s = EvaluateExtra("(", Arrays.asList(new String[] {")"}), true, showExtraDebug);
		
		if (showExtraDebug) {
			BA.Log("GetParams: " + s);
		}
		int start = 0;
		int inBracket = 0;
		boolean inQuotes = false;
		for (int current = 0; current < s.length(); current++) {
		    if (s.charAt(current) == '(' && !inQuotes) {
		    	inBracket++;
		    } 
		    if (s.charAt(current) == ')' && !inQuotes) {
		    	inBracket--;
		    } 
		    if (s.charAt(current) == '\"') {
		    	inQuotes = !inQuotes;
		    }
		    boolean atLastChar = (current == s.length() - 1);
		    if(atLastChar) {
		    	String tt = s.substring(start);
		    	
		    	params.add(tt);		    	
		    }
		    else if (s.charAt(current) == ',' && (inBracket==0) && !inQuotes) {
		    	String tt = s.substring(start, current);
		    	
		        params.add(tt);
		    	start = current + 1;
		    }
		}		
		return params;
	}
	
	protected boolean CheckType(String type) {
		return SupportedTypes.getOrDefault(type.toUpperCase(), false);
	}
	
	protected String BuildMethodParams(Map<String,B4JSExtractVariable> params) {
		StringBuilder s = new StringBuilder();
		boolean NotFirst=false;
		for(Iterator<Map.Entry<String, B4JSExtractVariable>> it = params.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<String, B4JSExtractVariable> entry = it.next();
			if (NotFirst) {
				s.append(",");
			}
			s.append(entry.getValue().JSName);
			NotFirst=true;
		}
		return s.toString();
	}
	
	protected void CleanVariables() {
		for(Iterator<Map.Entry<String, B4JSExtractVariable>> it = Vars.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<String, B4JSExtractVariable> entry = it.next();
			if (entry.getValue().Depth>=depth) {				
				it.remove();
			}
		}
	}
	
	protected String NextToken() {
		if (index<currentLine.Words.size()) {
			String s = currentLine.Words.get(index);
			index++;
			return s;
		}
		return "";
	}
	
	protected String PeekToken(int tmpIndex) {		
		if (tmpIndex<currentLine.Words.size()) {
			return currentLine.Words.get(tmpIndex);
		}
		return "";
	}
	
	protected void DebugPeekTokens(int tmpIndex) {
		int i = tmpIndex;
		while (i<currentLine.Words.size()) {
			BA.Log(currentLine.Words.get(i));
			i++;
		}		
	}
	
	protected void PrintWords() {
		String s = "";
		for (int i=0;i<currentLine.Words.size();i++) {
			s+="|" + currentLine.Words.get(i);
		}
		BA.Log(s);
		BA.Log("------------------------------------------------------------");
	}
	
	protected String PeekFirstToken(int tmpIndex, List<String> tokens) {
		String ret = "";
		int First = Integer.MAX_VALUE;
		for (int i=tmpIndex;i<currentLine.Words.size();i++) {
			for (int j=0;j<tokens.size();j++) {
				if (currentLine.Words.get(i).equals(tokens.get(j))) {
					if (i<First) {
						ret = tokens.get(j);
						First = i;
					}
				}
			}
		}
		return ret;
	}
	
	protected int Find(List<String> Words, String toFind) {
		for (int i=index;i<Words.size();i++) {
			if (Words.get(i).equals(toFind)) {
				return i;
			}
		}
		return -1;
	}
	
	protected int CheckStringNumeric(String str) {	 
		char localeMinusSign="-".charAt(0); 
	    if (!Character.isDigit(str.charAt(0)) && str.charAt(0) != localeMinusSign) {
	    	return 0;
	    }
	    int ret = 1;
	    if (str.charAt(0) == localeMinusSign) {
	    	ret = -1;
	    }
	    char localeDecimalSeparator = ".".charAt(0); 

	    for (char c:str.substring(1).toCharArray()) {
	        if (!Character.isDigit(c) && c!=localeDecimalSeparator) {	           
	            return 0;
	        }
	    }
	    return ret;
	}	
	
	protected String GetInd(int ind) {
		if (NL.equals("")) {
			return "";
		}		
		String s = "";
		for (int i=0;i<ind;i++) {
			s+="     ";
		}
		return s;
	}
	protected B4JSExtractResultMethod GetMethod(String methodName) {
		B4JSExtractResultMethod meth = Methods.getOrDefault(methodName.toLowerCase(), null);
		return meth;				
	}
	
	public class PrettyPrintingMap<K, V> {
	    private Map<K, V> map;

	    public PrettyPrintingMap(Map<K, V> map) {
	        this.map = map;
	    }

	    public String toString() {
	        StringBuilder sb = new StringBuilder();
	        Iterator<Entry<K, V>> iter = map.entrySet().iterator();
	        while (iter.hasNext()) {
	            Entry<K, V> entry = iter.next();
	            sb.append(entry.getKey().toString());
	            sb.append('=').append('"');
	            sb.append(entry.getValue().toString());
	            sb.append('"');
	            if (iter.hasNext()) {
	                sb.append(',').append(' ');
	            }
	        }
	        return sb.toString();

	    }
	}
	
	public class PrettyPrintingList<K> {
	    private List<K> list;

	    public PrettyPrintingList(List<K> list) {
	        this.list = list;
	    }

	    public String toString() {
	        StringBuilder sb = new StringBuilder();
	        for (int i=0;i<list.size();i++) {
	        	sb.append("{" + list.get(i).toString() + "}");
	        	if (i+1<list.size()) {
	        		sb.append(',').append(' ');
	        	}
	        }	        
	        return sb.toString();
	    }
	}
	
	protected void ProcessDim(boolean IsPrivate) {
		Map<String,B4JSExtractVariable> tmpVars = new LinkedHashMap<String,B4JSExtractVariable>();
		String next = PeekToken(index);
		boolean isDefining=false;
		String lastVar = "";
		while (true) {
			if (DebugTrack) {
				BA.Log("ProcessDim: " + next);
			}
			switch (next) {
				case "(":
					next = NextToken(); // (
					tmpVars.get(lastVar).isArray = true; 
					tmpVars.get(lastVar).Dimension = Evaluate("", Arrays.asList(new String[] {")"}), false);
					next = NextToken(); // )
					break;
				case "":
					int extraInd=0;
					for(Entry<String,B4JSExtractVariable> ventry: tmpVars.entrySet()) {
						B4JSExtractVariable v = ventry.getValue();
												
						if (v.Type.equals("")) {
							v.Type = "string";
							v.OrigType = "string";
						}
						String writeType="var ";
							
						if (IsPrivate) {
							if (isGlobal) {
								v.Scope = SCOPE.PUBLIC;
								extraInd=-1;
								writeType="this.";
							}
							
							if (v.isArray && v.InitValue.equals("")) {
								v.InitValue = v.ArrayInitValue;
							}
							if (v.InitValue.equals("")) {
								v.SetDefaultInitValue();								
								if (v.InitValue.equals("")) {
									methodJS.append(GetInd(ind+extraInd) + writeType + v.JSName + ";" + NL);								
								} else {
									methodJS.append(GetInd(ind+extraInd) + writeType + v.JSName + "=" + v.InitValue + ";" + NL);
								}
								if (!v.ExtraVar.equals("")) {
									methodJS.append(GetInd(ind+extraInd) + writeType + v.ExtraVar + ";" + NL);
								}								
							} else {
								methodJS.append(GetInd(ind+extraInd) + writeType + v.JSName + "=" + v.InitValue + ";" + NL);
								if (!v.ExtraVar.equals("")) {
									methodJS.append(GetInd(ind+extraInd) + writeType + v.ExtraVar + ";" + NL);
								}								
							}
							if (v.isArray && (v.InitValue.equals("") || v.InitValue.equals("[]"))) {
								if (v.Dimension.equals("")) {
									v.Dimension = "0";
								}
								
									methodJS.append(GetInd(ind+extraInd) + v.JSName + ".length=" + v.Dimension + ";" + NL);
									if (!v.Dimension.equals("0")) {
										methodJS.append(GetInd(ind+extraInd) + "for (var _initvar=0;_initvar<" + v.Dimension + ";_initvar++){" + NL);
										methodJS.append(GetInd(ind+1+extraInd) + v.JSName + "[_initvar]=" + v.ArrayInitValue + ";" + NL);
										methodJS.append(GetInd(ind+extraInd) + "}" + NL);
									}
							} 
							if (isGlobal) {
								v.Depth = 0;
							}
							if (DebugTrack) {
								BA.Log("VariableDim1: " + v.Name + " " + v.isArray + " " + v.InitValue + " " + v.ArrayInitValue + " " + v.Depth);
							}
							Vars.put(v.Name, v);
							if (IsTrackingVars) {
								BA.Log("[5:" + ClassName + "," + fullMethodName + ", line: " + currentLine.LineNumber + "] Adding var: " + v.Name);
							}
						
						} else {
							if (isGlobal) {
								// a hack to loose self.
								v.Scope = SCOPE.PRIVATE;
								if (v.isArray && v.InitValue.equals("")) {
									v.InitValue = v.ArrayInitValue;
								}
								if (v.InitValue.equals("")) {
									v.SetDefaultInitValue();								
									if (v.InitValue.equals("")) {
										ABMaterial.GlobalVars.put(v.JSName.toLowerCase(), GetInd(0) + writeType + v.JSName + ";" + NL);								
									} else {
										ABMaterial.GlobalVars.put(v.JSName.toLowerCase(), GetInd(0) + writeType + v.JSName + "=" + v.InitValue + ";" + NL);
									}
									if (!v.ExtraVar.equals("")) {
										ABMaterial.GlobalVars.put(v.JSName.toLowerCase(), GetInd(0) + writeType + v.ExtraVar + ";" + NL);
									}								
								} else {
									ABMaterial.GlobalVars.put(v.JSName.toLowerCase(), GetInd(0) + writeType + v.JSName + "=" + v.InitValue + ";" + NL);
									if (!v.ExtraVar.equals("")) {
										ABMaterial.GlobalVars.put(v.JSName.toLowerCase(), GetInd(0) + writeType + v.ExtraVar + ";" + NL);
									}								
								}
								if (v.isArray && (v.InitValue.equals("") || v.InitValue.equals("[]"))) {
									if (v.Dimension.equals("")) {
										v.Dimension = "0";
									}
									
									ABMaterial.GlobalVars.put(v.JSName.toLowerCase(), GetInd(0) + v.JSName + ".length=" + v.Dimension + ";" + NL);
										if (!v.Dimension.equals("0")) {
											ABMaterial.GlobalVars.put(v.JSName.toLowerCase(), GetInd(0) + "for (var _initvar=0;_initvar<" + v.Dimension + ";_initvar++){" + NL);
											ABMaterial.GlobalVars.put(v.JSName.toLowerCase(), GetInd(1) + v.JSName + "[_initvar]=" + v.ArrayInitValue + ";" + NL);
											ABMaterial.GlobalVars.put(v.JSName.toLowerCase(), GetInd(0) + "}" + NL);
										}
								} 
								if (isGlobal) {
									v.Depth = 0;
								}
								if (DebugTrack) {
									BA.Log("VariableDim2: " + v.Name + " " + v.isArray + " " + v.InitValue + " " + v.ArrayInitValue + " " + v.Depth);
								}
								Vars.put(v.Name, v);
								if (IsTrackingVars) {
									BA.Log("[5:" + ClassName + "," + fullMethodName + ", line: " + currentLine.LineNumber + "] Adding var: " + v.Name);
								}
							} else {
								if (v.isArray && v.InitValue.equals("")) {
									v.InitValue = v.ArrayInitValue;
								}
								if (v.InitValue.equals("")) {
									v.SetDefaultInitValue();								
									if (v.InitValue.equals("")) {
										methodJS.append(GetInd(ind+extraInd) + writeType + v.JSName + ";" + NL);								
									} else {
										methodJS.append(GetInd(ind+extraInd) + writeType + v.JSName + "=" + v.InitValue + ";" + NL);
									}
									if (!v.ExtraVar.equals("")) {
										methodJS.append(GetInd(ind+extraInd) + writeType + v.ExtraVar + ";" + NL);
									}								
								} else {
									methodJS.append(GetInd(ind+extraInd) + writeType + v.JSName + "=" + v.InitValue + ";" + NL);
									if (!v.ExtraVar.equals("")) {
										methodJS.append(GetInd(ind+extraInd) + writeType + v.ExtraVar + ";" + NL);
									}								
								}
								if (v.isArray && (v.InitValue.equals("") || v.InitValue.equals("[]"))) {
									if (v.Dimension.equals("")) {
										v.Dimension = "0";
									}
									
										methodJS.append(GetInd(ind+extraInd) + v.JSName + ".length=" + v.Dimension + ";" + NL);
										if (!v.Dimension.equals("0")) {
											methodJS.append(GetInd(ind+extraInd) + "for (var _initvar=0;_initvar<" + v.Dimension + ";_initvar++){" + NL);
											methodJS.append(GetInd(ind+1+extraInd) + v.JSName + "[_initvar]=" + v.ArrayInitValue + ";" + NL);
											methodJS.append(GetInd(ind+extraInd) + "}" + NL);
										}
								} 
								if (isGlobal) {
									v.Depth = 0;
								}
								if (DebugTrack) {
									BA.Log("VariableDim3: " + v.Name + " " + v.isArray + " " + v.InitValue + " " + v.ArrayInitValue + " " + v.Depth);
								}
								Vars.put(v.Name, v);
								if (IsTrackingVars) {
									BA.Log("[5:" + ClassName + "," + fullMethodName + ", line: " + currentLine.LineNumber + "] Adding var: " + v.Name);
								}
							}
						}
					}					
					return;
				case ",":
					next = NextToken();
					break;
				case "as":
					next = NextToken();
					isDefining = true;
					break;
				case "=":
					next = NextToken();
					
					if (tmpVars.get(lastVar).isArray) {
						next = PeekToken(index);
						if (next.equals("array")) {
							tmpVars.get(lastVar).ArrayInitValue = Evaluate("", Arrays.asList(new String[] {""}), true);
						} else {
							tmpVars.get(lastVar).InitValue = Evaluate("", Arrays.asList(new String[] {"as", ","}), true);
						}
					} else {
						tmpVars.get(lastVar).InitValue = Evaluate("", Arrays.asList(new String[] {"as", ","}), true);
					}
					break;
				default:
					next = NextToken();
					if (isDefining) {
						if (!CheckType(next)) {
							if (!isGlobal) {
								HasError=true;
								if (!isGlobal) {
									BA.LogError("ERROR: [" + ClassName + "," + methodName + ", line: " + currentLine.LineNumber + "] The type '" + next + "' cannot be transpiled to JavaScript!");
								} 
								return ;
							} else {
								for(Entry<String,B4JSExtractVariable> ventry: tmpVars.entrySet()) {
									B4JSExtractVariable v = ventry.getValue();
									if (v.Type.equals("")) {
										v.Type = next;
										v.OrigType = v.Type;
									}									
								}
							}
						} else {
							for(Entry<String,B4JSExtractVariable> ventry: tmpVars.entrySet()) {
								B4JSExtractVariable v = ventry.getValue();
								if (v.Type.equals("")) {
									v.Type = next;
									v.OrigType = next;
								}
							}
						}						
						isDefining=false;
					} else {
						B4JSExtractVariable var = new B4JSExtractVariable();
						var.Name = next.toLowerCase();
						var.JSName = "_" + var.Name;
						var.Depth = depth;
						var.Scope = SCOPE.PRIVATE;
						if (isGlobal) {
							if (IsPrivate) {
								var.Scope = SCOPE.PRIVATE;
							} else {
								var.Scope = SCOPE.PUBLIC;
							}
						}
						if (DebugTrack) {
							BA.Log("VariableDim new: " + var.Name + " " + var.Depth);
						}
						
						tmpVars.put(var.Name, var);						
						lastVar = var.Name;						
					}
					break;
			}
			next = PeekToken(index);
		}
	}
	
	protected void ProcessMethodDeclaration() {
		List<Boolean> selects = new ArrayList<Boolean>();
		int selectTeller=-1;
		int checkInline=0;
		while (line<Lines.size()) {
			currentLine =Lines.get(line);
			index=0;
			String next = PeekToken(index);
			if (DebugTrack) {
				BA.Log("ProcessMethodDeclaration: " + next);
			}
			boolean IsPrivate=false;
			switch (next) {
    			case "private":
    				IsPrivate=true;
    			case "public":
    			case "dim":
    				next = NextToken();
    				
    				ProcessDim(IsPrivate);
    				break;
    			case "end":
    				next = NextToken();
    				if (PeekToken(index).equals("sub")) {
    					return;   				
    				}
    				if (PeekToken(index).equals("if")) {
    					ind--; 
    					CleanVariables();
    					depth--;
    					methodJS.append(GetInd(ind) + "}" + NL);
    				}
    				if (PeekToken(index).equals("try")) {
    					ind--; 
    					CleanVariables();
    					depth--;
    					methodJS.append(GetInd(ind) + "}" + NL);
    				}
    				if (PeekToken(index).equals("select")) {
    					if (selects.get(selectTeller)) {
							methodJS.append(GetInd(ind) + "break;" + NL);
							ind--;
						} 
						ind--;
						CleanVariables();
						depth--;
						methodJS.append(GetInd(ind) + "}" + NL);
						selects.remove(selectTeller);
    					selectTeller--;
    				}
    				break;
    			case "if":
    				next = NextToken(); // if
    				methodJS.append(GetInd(ind) + "if (" + Evaluate("", Arrays.asList(new String[] {"then"}), true) + ") {" + NL);
    				index++;
    				next = PeekFirstToken(index, Arrays.asList(new String[] {"else"}));
    				if (!next.equals("")) {
    					checkInline = Find(currentLine.Words, "else");
    					if (checkInline==-1) {
							methodJS.append(GetInd(ind+1) + Evaluate("", Arrays.asList(new String[] {""}), false) + ";" + NL + GetInd(ind) + "}" + NL);
						} else {
							methodJS.append(GetInd(ind+1) + Evaluate("", Arrays.asList(new String[] {"else"}), false) + ";" + NL + GetInd(ind) + "} else {" + NL);
							index++;
							methodJS.append(GetInd(ind+1) + Evaluate("", Arrays.asList(new String[] {""}), false) + ";" + NL + GetInd(ind) + "}" + NL);
						}
    				} else {
    					depth++;
    					ind++;
    				}
    				break;
    			case "else":
    				next = PeekToken(index+1);
    				if (next.equals("if")) {
    					next = NextToken(); // if
    					next = NextToken(); // if
    					ind--;
        				methodJS.append(GetInd(ind) + "} else if (" + Evaluate("", Arrays.asList(new String[] {"then"}), true) + ") {" + NL);
        				index++;
        				next = PeekFirstToken(index, Arrays.asList(new String[] {"else"}));
        				if (!next.equals("")) {
        					checkInline = Find(currentLine.Words, "else");
        					if (checkInline==-1) {
    							methodJS.append(GetInd(ind+1) + Evaluate("", Arrays.asList(new String[] {""}), false) + ";" + NL + GetInd(ind) + "}" + NL);
    						} else {
    							methodJS.append(GetInd(ind+1) + Evaluate("", Arrays.asList(new String[] {"else"}), false) + ";" + NL + GetInd(ind) + "} else {" + NL);
    							index++;
    							methodJS.append(GetInd(ind+1) + Evaluate("", Arrays.asList(new String[] {""}), false) + ";" + NL + GetInd(ind) + "}" + NL);
    						}
        				} else {
        					depth++;
        					ind++;
        				}
    				} else {
    					ind--;
    					CleanVariables();
    					methodJS.append(GetInd(ind) + "} else {" + NL);
    					ind++;
    				}
					break;
    				
    			case "try":
    				next = NextToken(); // try				
    				methodJS.append(GetInd(ind) + "try {" + NL);		
    				ind++;
    				depth++;
    				break;
    			case "catch":
    				next = NextToken(); // catch
    				ind--;
    				methodJS.append(GetInd(ind) + "} catch(err) {" + NL);
    				ind++;
    				break;
    			case "for":
    				next = NextToken(); // for
    				next = PeekToken(index);
    				String forVar = "";
    				String forStart = "";
    				String forTo = "";
    				String forStep = "++";
    				String forDirection="<=";  
    				String forEach="";
    				depth++;
    				B4JSExtractVariable forVariable = new B4JSExtractVariable();
    				forVariable.Scope = SCOPE.PRIVATE;
    				forVariable.Depth = depth;
    				if (next.equals("each")) {
    					next = NextToken();
    					forVar = Evaluate("", Arrays.asList(new String[] {"as"}), true);
    					next = NextToken();
    					next = NextToken(); // var type
    					forVariable.Type = next;
    					forVariable.OrigType = forVariable.Type;
    					if (!CheckType(forVariable.OrigType)) {
    						HasError=true;
    						if (!isGlobal) {
    							BA.LogError("ERROR: [" + ClassName + "," + methodName + ", line: " + currentLine.LineNumber + "] The type '" + forVariable.OrigType + "' cannot be transpiled to JavaScript!");
    						} 
    						forVariable.ValidType = false;
    					}
    					next = NextToken(); // in
    					forEach = Evaluate("", Arrays.asList(new String[] {""}), true);
    					String testForEach=forEach;
    					if (forEach.startsWith("this._")) {
    						testForEach = forEach.substring(6);
    					}
    					B4JSExtractVariable forEachVariable = Vars.getOrDefault(testForEach,null);
    					if (forEachVariable!=null) {
    						if (forEachVariable.IsValues) {
    							methodJS.append(GetInd(ind) + "for (var _" + forVar + "KEY in " + forEach + ") {" + NL);
    							methodJS.append(GetInd(ind+1) + "var _" + forVar + "=" + forEach + "[" + "_" + forVar + "KEY];" + NL);
    	    					forVariable.Name = forVar;
    	    					forVariable.JSName = "_" + forVar;
    	    					forVariable.IsValues = true;
    	    					Vars.put(forVar,  forVariable);
    	    					if (IsTrackingVars) {
    	    						BA.Log("[2:" + ClassName + "," + fullMethodName + ", line: " + currentLine.LineNumber + "] Adding var: " + forVariable.Name);
    	    					}
    						} else {
    							methodJS.append(GetInd(ind) + "for (var _" + forVar + " in " + forEach + ") {" + NL);
    	    					forVariable.Name = forVar;
    	    					forVariable.JSName = "_" + forVar;
    	    					Vars.put(forVar,  forVariable);
    	    					if (IsTrackingVars) {
    	    						BA.Log("[3:" + ClassName + "," + fullMethodName + ", line: " + currentLine.LineNumber + "] Adding var: " + forVariable.Name);
    	    					}
    						}
    					} else {
    						HasError=true;
    						if (!isGlobal) {
    							BA.LogError("ERROR: [" + ClassName + "," + methodName + ", line: " + currentLine.LineNumber + "] " + testForEach + " cannot be found as a variable!");
    						} 
    						index = currentLine.Words.size();
    						break;
    					}    					
    				} else {    					
    					forVar = Evaluate("", Arrays.asList(new String[] {"="}), true);
    					next = NextToken();
    					forStart = Evaluate("", Arrays.asList(new String[] {"to"}), true);
    					next = NextToken();
    					checkInline = Find(currentLine.Words, "step");
    					if (checkInline==-1) {
    						forTo = Evaluate("", Arrays.asList(new String[] {""}), true);
    					} else {
    						forTo = Evaluate("", Arrays.asList(new String[] {"step"}), true);
    						next = NextToken();
    						forStep = Evaluate("", Arrays.asList(new String[] {""}), true);
    						if (forStep.startsWith("-")) {
    							forDirection=">=";
    							forStep = "-=" + forStep.substring(1);
    						} else {
    							forStep="+=" + forStep;
    						}
    					}
    					methodJS.append(GetInd(ind) + "for (var _" + forVar + "=" + forStart + ";_" + forVar + forDirection + forTo + ";_" + forVar + forStep + ") {" + NL);
    					forVariable.Name = forVar;
    					forVariable.JSName = "_" + forVar;
    					Vars.put(forVar,  forVariable);
    					if (IsTrackingVars) {
    						BA.Log("[4:" + ClassName + "," + fullMethodName + ", line: " + currentLine.LineNumber + "] Adding var: " + forVariable.Name);
    					}    					
    				}
    				ind++;
    				break;
    			case "next":
    				ind--;
    				CleanVariables();
    				methodJS.append(GetInd(ind) + "}" + NL);
    				depth--;
    				break;
    			case "do": // while or until
    				next = NextToken(); // select
    				next = PeekToken(index);
    				if (next.equals("while")) {
    					next = NextToken();
    					methodJS.append(GetInd(ind) + "while (" + Evaluate("", Arrays.asList(new String[] {}), true) + ") {" + NL);
    					ind++;
    					depth++;
    				} else {
    					if (next.equals("until")) {
    						next = NextToken();
        					methodJS.append(GetInd(ind) + "while (!(" + Evaluate("", Arrays.asList(new String[] {}), true) + ")) {" + NL);
        					ind++;
        					depth++;
    					} else {
    						HasError=true;
    						if (!isGlobal) {
    							BA.LogError("ERROR: [" + ClassName + "," + fullMethodName + ", line: " + currentLine.LineNumber + "] Do " + next + " is unknown for B4JS!");
    						}
							index = currentLine.Words.size();
							break;
    					}
    				}
    				
    				break;
    			case "loop": // end while, end until
    				ind--;
    				methodJS.append(GetInd(ind) + "}" + NL);
    				CleanVariables();
    				depth--;
    				break;
    			case "select":
    				next = NextToken(); // select
    				next = PeekToken(index);
    				if (next.equals("case")) {
    					next = NextToken(); // case
    				}
    				methodJS.append(GetInd(ind) + "switch (\"\" + " + Evaluate("", Arrays.asList(new String[] {}), true) + ") {" + NL);
    				ind++;
    				depth++;
    				selectTeller++;
    				selects.add(false);
    				break;
    			case "case":
    				next = NextToken(); 
    				if (selects.get(selectTeller)) {
    					methodJS.append(GetInd(ind) + "break;" + NL);
						ind--;
					}
					
    				next = PeekToken(index); 
					if (next.equals("else")) {
						methodJS.append(GetInd(ind) + "default:" + NL);
					} else {
						String CaseVars = Evaluate("", Arrays.asList(new String[] {}), true);
						String[] spl = CaseVars.split(",");
						for (String CaseVar: spl) {
							methodJS.append(GetInd(ind) + "case \"\" + " + CaseVar + ":" + NL);
						}
					}
					ind++;
					selects.set(selectTeller, true);					
					break;
    			default:
    				String tmpEvaluation = Evaluate("", Arrays.asList(new String[] {}), false).trim();
    				if (!tmpEvaluation.equals("")) {
    					if (tmpEvaluation.endsWith("\n")) {
    						methodJS.append(GetInd(ind) + tmpEvaluation);
    					} else {
    						if (tmpEvaluation.endsWith("{")) {
    							methodJS.append(GetInd(ind) + tmpEvaluation + NL);
    						} else {
    							methodJS.append(GetInd(ind) + tmpEvaluation + ";" + NL);
    						}
    					}
    				}
    				break;
    		}
    		index=0;
			line++;
		}
	}
	
	protected String ResolveConst(String s) {	
		String Const=s;
		try {
			Field f = ABMaterial.class.getDeclaredField(s.toUpperCase());
			if (f!=null) {
				Class<?> t = f.getType();
				if (t!=null) {
					try {
						if(t == int.class){
							return String.valueOf(f.getInt(null));
						}else if(t == String.class){
							return "'" + (String) (f.get(null)) + "'";
						}
					} catch (IllegalArgumentException e) {
						//e.printStackTrace();
					} catch (IllegalAccessException e) {
						//e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
				
		}
		return Const;
	}	
		
	protected String ProcessVariable(B4JSExtractVariable var, boolean isCondition) {
		boolean showExtraDebug=false;
		
		return ProcessVariableExtra(var, isCondition, showExtraDebug);
	}
	
	protected String ProcessVariableExtra(B4JSExtractVariable var, boolean isCondition, boolean showExtraDebug) {
		if (showExtraDebug) {
			BA.Log("ProcessVariable: " + var.Name + " " + var.OrigType + " " + var.isArray + " " + var.Scope);
		}
		String extra = "self.";
		if (var.Scope.equals(SCOPE.PRIVATE)) {
			extra = "";
		}
		if (var.isArray) {
			return ProcessArray(var, isCondition);
		} else {
			switch (var.OrigType) {
				case "list":
					return ProcessList(var, isCondition);
				case "map":
					return ProcessMap(var, isCondition);
				case "timer":
					return ProcessTimer(var, isCondition);
				case "stringbuilder":
					return ProcessStringBuilder(var, isCondition);
				case "abmlabel":
					return ProcessABMLabel(var, isCondition);
				case "abmbutton":
					return ProcessABMButton(var, isCondition);
				case "abmpage":
					return ProcessABMPage(var, isCondition);
				case "abminput":
					return ProcessABMInput(var, isCondition);
				case "abmswitch":
					return ProcessABMSwitch(var, isCondition);
				case "abmcheckbox":
					return ProcessABMCheckBox(var, isCondition);
				case "abmradiogroup":
					return ProcessABMRadioGroup(var, isCondition);
				case "jsonparser":
					return ProcessJSONParser(var, isCondition);
				case "jsongenerator":
					return ProcessJSONGenerator(var, isCondition);
				case "abmcontainer":
					return ProcessABMContainer(var, isCondition);
				case "abmactionbutton":
					return ProcessABMActionButton(var, isCondition);
				case "abmrange":
					return ProcessABMRange(var, isCondition);
				case "abmslider":
					return ProcessABMSlider(var, isCondition);
				case "abmtabs":
					return ProcessABMTabs(var, isCondition);
				case "b4jsjquery":
					return ProcessJQuery(var, isCondition);
				case "b4jsjqueryelement":
					return ProcessJQueryElement(var, isCondition);
				default:
					return extra + var.JSName;
			}
		}		
	}
	
	protected String ProcessArray(B4JSExtractVariable var, boolean isCondition) {
		String extra = "self.";
		//if (var.Depth==0) {
		if (var.Scope.equals(SCOPE.PRIVATE)) {
			extra = "";
		}
		StringBuilder s = new StringBuilder();
		String next = PeekToken(index);
		if (DebugTrack) {
			BA.Log("ProcessArray: " + next);
		}
		switch (next) {
		case "(":
			s.append(extra + var.JSName + "[" + Evaluate("(", Arrays.asList(new String[] {")"}), isCondition) +"]");
			next = NextToken(); // )
			return s.toString();
		default:
			return extra + var.JSName;				
		}
	}
		
	protected String ProcessABMPage(B4JSExtractVariable var, boolean isCondition) {
		String extra = "self.";
		if (var.Scope.equals(SCOPE.PRIVATE)) {
			extra = "";
		}
		StringBuilder s = new StringBuilder();
		String next = PeekToken(index);

		List<String> params;
		String ThemeName="default";
		while (true) {
			if (DebugTrack) {
				BA.Log("ProcessABMPage: " + next);
			}
			switch (next) {
			case ".":
				next = NextToken();
				break;
			case "b4jsshowtoast":
				next = NextToken();
				params = GetParams();
				if (params.get(4).equalsIgnoreCase("false")) {
					s.append("var _b4js_selectable=' notselectable ';\n");
				} else {
					s.append("var _b4js_selectable='';\n");
				}
				if (params.get(1).toLowerCase().equals("")) {
					ThemeName="default";
				} else {
					ThemeName=params.get(1).toLowerCase();
				}
				s.append(GetInd(ind) + "var _b4js_toastid=" + params.get(0).toLowerCase() + ";" + NL);
				s.append(GetInd(ind) + "var _b4js_html=\"<span id='\" + _b4js_toastid  + \"' class='\" + _b4jsthemes[\"toast-\" + " + ThemeName + "].ForeColor + \" \" + _b4js_selectable + \"'>\" + b4js_buildtext(" + params.get(2) + ", false) + \"<span>\";" + NL);
				s.append(GetInd(ind) + "RunToastB4JS(" + params.get(3) + ", _b4jsthemes[\"toast-\" + " + ThemeName + "].Rounded, _b4js_html,  _b4js_toastid, _b4jsthemes[\"toast-\" + " + ThemeName + "].BackColor, \"toast-container\" + "+ ThemeName + ")");
				next = NextToken();
				return s.toString();				
			case "b4jsmsgbox":
				next = NextToken();
				params = GetParams();
				
				if (params.get(6).trim().equals("\"\"")) {
					ThemeName="\"default\"";
				} else {
					ThemeName=params.get(6).toLowerCase();
				}
				s.append("var _b4js_returnname=" + params.get(0) + ";" + NL);
				s.append(GetInd(ind) + "b4js_msgbox(" + ThemeName + "," + params.get(2) + "," + params.get(1) + "," + params.get(3) + "," + ResolveConst(params.get(5)) + ", " + params.get(4) + ",self, _b4js_returnname)");
				
				next = NextToken();
				return s.toString();
			case "b4jsmsgbox2":
				next = NextToken();
				params = GetParams();
				if (params.get(9).trim().equals("\"\"")) {
					ThemeName="\"default\"";
				} else {
					ThemeName=params.get(9).toLowerCase();
				}
				
				s.append("var _b4js_returnname=" + params.get(0) + ";" + NL);
				s.append(GetInd(ind) + "b4js_msgbox2(" + ThemeName + "," + params.get(2) + "," + params.get(1) + "," + ResolveConst(params.get(6)) + "," + params.get(9) +"," + params.get(3) + "," + params.get(4) + "," + params.get(5) + "," + ResolveConst(params.get(8)) + "," + params.get(7) + ",self, _b4js_returnname)");
				
				next = NextToken();
				return s.toString();
			case "b4jsinputbox":				
				next = NextToken();
				params = GetParams();
				if (params.get(13).trim().equals("\"\"")) {
					ThemeName="\"default\"";
				} else {
					ThemeName=params.get(13).toLowerCase();
				}
				
				s.append("var _b4js_returnname=" + params.get(0) + ";" + NL);
				s.append(GetInd(ind) + "b4js_inputbox(" + ThemeName + "," + params.get(1) + "," + ResolveConst(params.get(6)) + "," + params.get(7) + "," + params.get(10) + "," + params.get(9) + "," + params.get(8) + "," + ResolveConst(params.get(5)) + "," + params.get(4)  + "," + params.get(2) + "," + params.get(3) + "," + params.get(4) + "," + params.get(12) + "," + params.get(11) + ",self, _b4js_returnname)");
				
				next = NextToken();
				return s.toString();
			case "b4jsrunmethod":
				next = NextToken();
				params = GetParams();
				
				String className = params.get(0).replace("\"", "");
				String varMeth = params.get(1).replace("\"", "");
				if (params.get(2).equalsIgnoreCase("null")) {
					s.append("_b4jsclasses[\"b4jspagekey" + className.toLowerCase() + "\"]." + varMeth.toLowerCase() + "()");
				} else {
					params.remove(0);						
					params.remove(0);
					String varPars=String.join(", ", params);
					
					s.append("_b4jsclasses[\"b4jspagekey" + className.toLowerCase() + "\"]." + varMeth.toLowerCase() + "(" + varPars.substring(1, varPars.length() - 1) + ")"); //removing the [ and ]
				}
				
				next = NextToken();
				return s.toString();
			case "b4jsruninlinejavascriptmethod":
				next = NextToken();
				params = GetParams();
				
				String varMeth2 = params.get(0).replace("\"", "");
				if (params.get(1).equalsIgnoreCase("null")) {
					s.append(varMeth2 + "()");
				} else {
					params.remove(0);						
					String varPars=String.join(", ", params);
					
					s.append(varMeth2 + "(" + varPars.substring(1, varPars.length() - 1) + ")"); //removing the [ and ]
				}
				
				next = NextToken();
				return s.toString();
			case "b4jscallajax":
				next = NextToken();
				params = GetParams();
				boolean fromb4js=true;
				if (params.get(5).equals("")) {
					fromb4js = false;
				}
				s.append("callAjax(" + params.get(1) + "," + params.get(2) + "," + params.get(3) + ", " + params.get(4) + "," + params.get(0) + ", " + fromb4js + "," + params.get(5).toLowerCase() + ")");
				
				next = NextToken();
				return s.toString();
			case "b4jsgetcomponentidfromuniqueid":
				next = NextToken();
				params = GetParams();
				s.append("$('[data-b4js=' + " + params.get(0).toLowerCase() + " + ']').attr('id')");
				next = NextToken();
				return s.toString();
			case "b4jsprintpage":
				next = NextToken();
				s.append("window.print();");
				next = NextToken();
				return s.toString();
			case "b4jsshowsidebar":
				next = NextToken();
				params = GetParams();
				s.append("if(($('#sidenav-overlay-sidebar').length == 0)) {$('body').append($('<div id=\"sidenav-overlay-sidebar\" style=\"opacity:0\"></div>'));$('#btnextrasidebar" + params.get(0).toLowerCase() + "').trigger('click');$('#sidenav-overlay-sidebar').remove();} else {if ($('#" + params.get(0).toLowerCase() + "').css('right').indexOf('-')>-1){$('#btnextrasidebar" + params.get(0).toLowerCase() + "').trigger('click');$('#sidenav-overlay-sidebar').remove();}}");
				next = NextToken();
				return s.toString();
			case "b4jstogglesidebar":
				next = NextToken();
				params = GetParams();
				s.append("if($('#sidenav-overlay-sidebar').length == 0) {$('body').append($('<div id=\"sidenav-overlay-sidebar\" style=\"opacity:0\"></div>'));}$('#btnextrasidebar" + params.get(0).toLowerCase() + "').trigger('click');$('#sidenav-overlay-sidebar').remove();");
				next = NextToken();
				return s.toString();
			case "b4jsclosesidebar":
				next = NextToken();
				params = GetParams();
				s.append("$('#btnextrasidebar" +  params.get(0).toLowerCase() + "').abmsideNav('hide');");
				next = NextToken();
				return s.toString();
			case "":
				return extra + var.JSName;
			default:
				BA.LogError(next + " is NOT supported in B4JS!");
				index = currentLine.Words.size();
				break;
			}
			next = PeekToken(index);			
		}		
	}
	
	protected String ProcessJSONParser(B4JSExtractVariable var, boolean isCondition) {
			String extra = "self.";
			if (var.Scope.equals(SCOPE.PRIVATE)) {
				extra = "";
			}
			StringBuilder s = new StringBuilder();
			String next = PeekToken(index);
			List<String> params;		
			while (true) {
				if (DebugTrack) {
					BA.Log("ProcessJSONParser: " + next);
				}
				switch (next) {
				case ".":
					next = NextToken();
					break;
				case "initialize":
					next = NextToken();
					params = GetParams();
					s.append(GetInd(ind) + "var b4jsJSON=" + params.get(0).replaceAll("\\\\n",  "").replace("&quote;", "\\\"") + ";" + NL);
					s.append(GetInd(ind) + extra + var.JSName + "=JSON.parse(b4jsJSON)");
					
					next = NextToken();
					return s.toString();
				case "nextobject":					
					next = NextToken();
					return extra + var.JSName;
					
				case "nextarray":
					next = NextToken();
					return extra + var.JSName;
				case "":
					return extra + var.JSName;
				default:
					BA.LogError(next + " is NOT supported in B4JS!");
					index = currentLine.Words.size();
					break;
				}
				next = PeekToken(index);			
			}			
	}
	
	protected String RemoveOuterQuotes(String myString) {
		if (myString.startsWith("\"")) {
			return myString.substring(1, myString.length()-1);
		}
		return myString;
	}
	
	protected String ProcessJQuery(B4JSExtractVariable var, boolean isCondition) {		
		StringBuilder s = new StringBuilder();
		//String ret = "";
		String next = PeekToken(index);
		List<String> params;	
		boolean showExtraDebug=false;
		while (true) {
			if (showExtraDebug) {
				BA.Log("ProcessJQuery: " + next);
			}
			switch (next) {
			case ".":
				next = NextToken();
				break;	
			case "get":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("$(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);				
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));
					next = NextToken();
				} else {
					next = NextToken();
				}
				
				return s.toString();
			case "getbyuniquekey":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				
				s.append("$('[data-b4js=' + " + params.get(0).toLowerCase() + " + ']')");
				next = NextToken();
				next = PeekToken(index);				
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));
					next = NextToken();
				} else {
					next = NextToken();
				}
				
				return s.toString();
			case "b4jsparam_jqueryindex":
				next = NextToken();
				s.append("_b4jsparam_jqueryindex");
				next = NextToken();
				return s.toString();
			
			case "this":
				next = NextToken();				
				s.append("$(this)");
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));
					next = NextToken();
				} else {
					next = NextToken();
				}
				
				return s.toString();
			case "globaleval":
				next = NextToken();
				next = EvaluateExtra("(", Arrays.asList(new String[] {")"}), isCondition, showExtraDebug);
				s.append("$.globalEval(" + next + ")");
				next = NextToken();				
				return s.toString();
			case "isemptyobject":
				next = NextToken();
				next = EvaluateExtra("(", Arrays.asList(new String[] {")"}), isCondition, showExtraDebug);
				s.append("$.isEmptyObject(" + next + ")");
				next = NextToken();				
				return s.toString();
			case "isnumeric":
				next = NextToken();
				next = EvaluateExtra("(", Arrays.asList(new String[] {")"}), isCondition, showExtraDebug);
				s.append("$.isNumeric(" + next + ")");
				next = NextToken();				
				return s.toString();
				
			case "trim":
				next = NextToken();
				next = EvaluateExtra("(", Arrays.asList(new String[] {")"}), isCondition, showExtraDebug);
				s.append("$.trim(" + next + ")");
				next = NextToken();				
				return s.toString();
			case "endsub":
				next = NextToken();				
				s.append("})");
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));
					next = NextToken();
				} else {
					next = NextToken();
				}
				
				return s.toString();
			case "documentreadysub":
				next = NextToken();
				
				s.append("$(document).ready(function() {");
				next = NextToken();
				
				return s.toString();
			case "b4jsparam_jqueryevent":
				next = NextToken();
				
				s.append("_b4jsparam_jqueryevent");
				s.append(ProcessJQueryEvent(isCondition));
				
				return s.toString();				
			case "parseint":
				next = NextToken();
				next = EvaluateExtra("(", Arrays.asList(new String[] {")"}), isCondition, showExtraDebug);
				s.append("parseInt(" + next + ")");
				next = NextToken();				
				return s.toString();
			case "":
				return s.toString();
			default:
				if (!next.equals(")")) {
					BA.LogError("ProcessJQuery: " + next + " is NOT supported in B4JS!");
				}
				index = currentLine.Words.size();
				break;
			}
			next = PeekToken(index);			
		}			
	}
	
	protected String ProcessJQueryEvent(boolean isCondition) {
		StringBuilder s = new StringBuilder();
		String next = PeekToken(index);
			
		boolean showExtraDebug=false;
		while (true) {
			if (showExtraDebug) {
				BA.Log("ProcessJQueryEvent: " + next);
			}
			switch (next) {
			case ".":
				next = NextToken();
				s.append(".");		
				break;
			case "target":
				next = NextToken();
				s.append("target");
				return s.toString();
			case "timestamp":
				next = NextToken();
				s.append("timeStamp");
				return s.toString();
			case "relatedtarget":
				next = NextToken();
				s.append("relatedTarget");
				return s.toString();
			case "pagex":
				next = NextToken();
				s.append("pageX");
				return s.toString();
			case "pagey":
				next = NextToken();
				s.append("pageY");
				return s.toString();
			case "which":
				next = NextToken();
				s.append("which");
				return s.toString();
			case "metakey":
				next = NextToken();
				s.append("metaKey");
				return s.toString();
			case "altkey":
				next = NextToken();
				s.append("altKey");
				return s.toString();
			case "bubbles":
				next = NextToken();
				s.append("bubbles");
				return s.toString();
			case "button":
				next = NextToken();
				s.append("button");
				return s.toString();
			case "buttons":
				next = NextToken();
				s.append("buttons");
				return s.toString();
			case "cancelable":
				next = NextToken();
				s.append("cancelable");
				return s.toString();
			case "char":
				next = NextToken();
				s.append("char");
				return s.toString();
			case "charcode":
				next = NextToken();
				s.append("charCode");
				return s.toString();
			case "clientx":
				next = NextToken();
				s.append("clientX");
				return s.toString();
			case "clienty":
				next = NextToken();
				s.append("clientY");
				return s.toString();
			case "ctrlkey":
				next = NextToken();
				s.append("ctrlKey");
				return s.toString();
			case "currenttarget":
				next = NextToken();
				s.append("currentTarget");
				return s.toString();
			case "detail":
				next = NextToken();
				s.append("detail");
				return s.toString();
			case "eventphase":
				next = NextToken();
				s.append("eventPhase");
				return s.toString();
			case "key":
				next = NextToken();
				s.append("key");
				return s.toString();
			case "keycode":
				next = NextToken();
				s.append("keyCode");
				return s.toString();
			case "offsetx":
				next = NextToken();
				s.append("offsetX");
				return s.toString();
			case "offsety":
				next = NextToken();
				s.append("offsetY");
				return s.toString();
			case "originaltarget":
				next = NextToken();
				s.append("originalTarget");
				return s.toString();
			case "screenx":
				next = NextToken();
				s.append("screenX");
				return s.toString();
			case "screeny":
				next = NextToken();
				s.append("screenY");
				return s.toString();
			case "shiftkey":
				next = NextToken();
				s.append("shiftKey");
				return s.toString();
			case "view":
				next = NextToken();
				s.append("view");
				return s.toString();
			case "result":			
				next = NextToken();
				s.append("result");
				return s.toString();
			case "type":
				next = NextToken();
				s.append("type");
				return s.toString();
			case "namespace":
				next = NextToken();
				s.append("namespace");
				return s.toString();
			case "preventdefault":
				next = NextToken();
				s.append("preventDefault()");
				return s.toString();
			case "stopimmediatepropagation":
				next = NextToken();
				s.append("stopImmediatePropagation()");
				return s.toString();
			case "stoppropagation":
				next = NextToken();
				s.append("stopPropagation()");
				return s.toString();
			case "ispropagationstopped":
				next = NextToken();
				s.append("isPropagationStopped()");
				return s.toString();
			case "isimmediatepropagationstopped":
				next = NextToken();
				s.append("isImmediatePropagationStopped()");
				return s.toString();
			case "isdefaultprevented":
				next = NextToken();
				s.append("isDefaultPrevented()");
				return s.toString();
			case "":
				return s.toString();
			default:
				BA.LogError("ProcessJQueryEvent: " + next + " is NOT supported in B4JS!");
				index = currentLine.Words.size();
				break;
			}
			next = PeekToken(index);			
		}				
		
	}
	
	protected String ProcessJQueryElement(B4JSExtractVariable var, boolean isCondition) {
		String extra = "self.";
		if (var.Scope.equals(SCOPE.PRIVATE)) {
			extra = "";
		}
		StringBuilder s = new StringBuilder();
		String ret = "";
		String next = PeekToken(index);

		boolean showExtraDebug=false;
		while (true) {
			if (showExtraDebug) {
				BA.Log("ProcessJQueryElement: " + next + " var: " + var);
			}
			switch (next) {
			case ".":
				next = NextToken();
				if (PeekToken(index).equals("endsub")) {
					next = NextToken();
					s.append("})");
					next = PeekToken(index);
					if (next.equals(".")) {
						s.append(ProcessJQueryElementChain(isCondition));					
					} else {
						next = NextToken();
					}
					next = NextToken();
					return s.toString();
				} else {
					s.append(extra + var.JSName + ".");
					s.append(ProcessJQueryElementChain(isCondition));
				}
					
				return s.toString();
			case "=":				
				if (isCondition) {
					next = EvaluateExtra("", Arrays.asList(new String[] {"then"}), isCondition, showExtraDebug);
					ret = extra + var.JSName + " == " + next;
					next = NextToken();
					return ret;
				} else {
					next = NextToken(); // =
					next = EvaluateExtra("", Arrays.asList(new String[] {""}), isCondition, showExtraDebug);
					ret = extra + var.JSName + " = " + next;
					next = NextToken();
					return ret;
				}
			case "":
				return extra + var.JSName;
			default:
				BA.LogError("ProcessJQueryElement: " + next + " is NOT supported in B4JS!");
				index = currentLine.Words.size();
				break;
			}
			next = PeekToken(index);			
		}			
	}
	
	protected String ProcessJQueryElementChain(boolean isCondition) {
		StringBuilder s = new StringBuilder();
		
		String next = PeekToken(index);
		List<String> params;		
		boolean showExtraDebug=false;
		while (true) {
			if (showExtraDebug) {
				BA.Log("ProcessJQueryElementChain: " + next);
			}
			switch (next) {
			case ".":
				next = NextToken();
				s.append(".");
				s.append(ProcessJQueryElementChain(isCondition));
				return s.toString();
			case "addclass":
				next = NextToken();
				next = EvaluateExtra("(", Arrays.asList(new String[] {")"}), isCondition, showExtraDebug);
				s.append("addClass(" + next + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));
					return s.toString();
				} else {
					next = NextToken();
				}
				return s.toString();
			case "getattr":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("attr(" + params.get(0) + ")");
				next = NextToken();
				next = NextToken();
				return s.toString();
			case "setattr":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("attr(" + params.get(0) + "," + params.get(1) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "hasclass":
				next = NextToken();
				next = EvaluateExtra("(", Arrays.asList(new String[] {")"}), isCondition, showExtraDebug);
				s.append("hasClass(" + next + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "gethtml":
				next = NextToken();
				s.append("html()");
				
				return s.toString();
			case "sethtml":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("html(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "getprop":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("prop(" + params.get(0) + ")");
				next = NextToken();
				next = NextToken();
				return s.toString();
			case "setprop":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("prop(" + params.get(0) + "," + params.get(1) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "removeattr":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("removeAttr(" + params.get(0) + ")");
				next = NextToken();
				next = NextToken();
				return s.toString();
			case "removeclass":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("removeClass(" + params.get(0) + ")");
				next = NextToken();
				next = NextToken();
				return s.toString();
			case "removeprop":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("removeProp(" + params.get(0) + ")");
				next = NextToken();
				next = NextToken();
				return s.toString();
			case "toggleclass":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("toggleClass(" + params.get(0) + ")");
				next = NextToken();
				next = NextToken();
				return s.toString();
			case "getval":
				next = NextToken();
				s.append("val()");
				
				return s.toString();
			case "setval":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("val(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "getcss":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("css(" + params.get(0) + ")");
				next = NextToken();
				next = NextToken();
				return s.toString();
			case "setcss":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("css(" + params.get(0) + "," + params.get(1) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
				
			case "getheight":
				next = NextToken();
				s.append("height()");
				
				return s.toString();
			case "setheight":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("height(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "getinnerheight":
				next = NextToken();
				s.append("innerHeight()");
				
				return s.toString();
			case "setinnerheight":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("innerHeight(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "getinnerwidth":
				next = NextToken();
				s.append("innerWidth()");
				
				return s.toString();
			case "setinnerwidth":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("innerWidth(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "getoffsetleft":
				next = NextToken();
				s.append("offset().left");
				
				return s.toString();	
			case "getoffsettop":
				next = NextToken();
				s.append("offset().top");
				
				return s.toString();
			case "setoffset":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("offset({top: " + params.get(0) + ", left: " + params.get(1) + "})");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();	
			case "getouterheight":
				next = NextToken();
				s.append("outerHeight()");
				
				return s.toString();
			case "setouterheight":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("outerHeight(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "getouterwidth":
				next = NextToken();
				s.append("outerWidth()");
				
				return s.toString();
			case "setouterwidth":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("outerWidth(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "getpositionleft":
				next = NextToken();
				s.append("position().left");
				
				return s.toString();	
			case "getpositiontop":
				next = NextToken();
				s.append("position().top");
				
				return s.toString();
			case "getscrollleft":
				next = NextToken();
				s.append("scrollLeft()");
				
				return s.toString();	
			case "setscrollleft":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("scrollLeft(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();	
			case "getscrolltop":
				next = NextToken();
				s.append("scrollTop()");
				
				return s.toString();	
			case "setscrolltop":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("scrollTop(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "getwidth":
				next = NextToken();
				s.append("width()");
				
				return s.toString();
			case "setwidth":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("width(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "getdata":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("data(" + params.get(0) + ")");
				next = NextToken();
				next = NextToken();
				return s.toString();
			case "setdata":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("data(" + params.get(0) + "," + params.get(1) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "hasdata":
				next = NextToken();
				next = EvaluateExtra("(", Arrays.asList(new String[] {")"}), isCondition, showExtraDebug);
				s.append("hasData(" + next + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "removedata":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("removeData(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "append":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("append(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "appendto":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("appendTo(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "prepend":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("prepend(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "prependto":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("prependTo(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "gettext":
				next = NextToken();
				s.append("text()");
			
				return s.toString();
			case "settext":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("text(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "children":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("children(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();				
			case "closest":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("closest(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();					
			case "subeach":
				next = NextToken();
				s.append("each(function(_b4jsparam_jqueryindex) {");
				//next = NextToken();
				//next = NextToken();
				return s.toString();	
			case "eq":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("eq(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();	
			case "filter":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("filter(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();	
			case "find":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("find(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "first":
				next = NextToken();
				s.append("first()");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();	
			case "has":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("has(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();	
			case "is":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("is(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "last":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("last(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();	
			case "next":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("next(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "nextall":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("nextAll(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();	
			case "nextuntil":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("nextUntil(" + params.get(0) + "," + params.get(1) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "not":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("not(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "offsetparent":
				next = NextToken();
				s.append("offsetParent()");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();	
			case "parent":
				next = NextToken();
				s.append("parent()");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "parents":
				next = NextToken();
				s.append("parents()");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "parentsuntil":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("parentUntil(" + params.get(0) + "," + params.get(1) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "prev":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("prev(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "prevall":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("prevAll(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();	
			case "prevuntil":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("prevUntil(" + params.get(0) + "," + params.get(1) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "siblings":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("siblings(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();	
			case "slice":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("slice(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "slice2":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("slice(" + params.get(0) + "," + params.get(1) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "length":
				next = NextToken();
				s.append("length");				
				return s.toString();
			case "subslideup":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("slideUp(" + params.get(0) + ", " + params.get(1) + ", function() {");
				next = NextToken();
				next = NextToken();
				return s.toString();	
			case "subslidedown":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("slideDown(" + params.get(0) + ", " + params.get(1) + ", function() {");
				next = NextToken();
				next = NextToken();
				return s.toString();	
			case "subslidetoggle":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("slideToggle(" + params.get(0) + ", " + params.get(1) + ", function() {");
				next = NextToken();
				next = NextToken();
				return s.toString();	
			case "subfadein":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("fadeIn(" + params.get(0) + ", " + params.get(1) + ", function() {");
				next = NextToken();
				next = NextToken();
				return s.toString();	
			case "subfadeout":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("fadeOut(" + params.get(0) + ", " + params.get(1) + ", function() {");
				next = NextToken();
				next = NextToken();
				return s.toString();	
			case "subfadeto":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("fadeTo(" + params.get(0) + ", " + params.get(2)  + ", " + params.get(1) + ", function() {");
				next = NextToken();
				next = NextToken();
				return s.toString();	
			case "subfadetoggle":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("fadeToggle(" + params.get(0) + ", " + params.get(1) + ", function() {");
				next = NextToken();
				next = NextToken();
				return s.toString();	
			case "subhide":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("hide(" + params.get(0) + ", " + params.get(1) + ", function() {");
				next = NextToken();
				next = NextToken();
				return s.toString();	
			case "subshow":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("show(" + params.get(0) + ", " + params.get(1) + ", function() {");
				next = NextToken();
				next = NextToken();
				return s.toString();	
			case "subtoggle":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("toggle(" + params.get(0) + ", " + params.get(1) + ", function() {");
				next = NextToken();
				next = NextToken();
				return s.toString();	
			case "slideup":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("slideUp(" + params.get(0) + ", " + params.get(1) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "slidedown":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("slideDown(" + params.get(0) + ", " + params.get(1) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "slidetoggle":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("slideToggle(" + params.get(0) + ", " + params.get(1) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "fadein":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("fadeIn(" + params.get(0) + ", " + params.get(1) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "fadeout":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("fadeOut(" + params.get(0) + ", " + params.get(1) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "fadeto":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("fadeTo(" + params.get(0) + ", " + params.get(2)  + ", " + params.get(1) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "fadetoggle":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("fadeToggle(" + params.get(0) + ", " + params.get(1) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "hide":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("hide(" + params.get(0) + ", " + params.get(1) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "show":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("show(" + params.get(0) + ", " + params.get(1) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "toggle":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("toggle(" + params.get(0) + ", " + params.get(1) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "subonevents":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				if (params.get(1).equals("\"\"") || params.get(1).equals("''")) {
					s.append("on(" + params.get(0) + ",function(_b4jsparam_jqueryevent) {");					
				} else {
					s.append("on(" + params.get(0) + "," + params.get(1) + ",function(_b4jsparam_jqueryevent) {");
				}
				next = NextToken();
				next = NextToken();
				return s.toString();	
			case "suboffevents":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				if (params.get(1).equals("\"\"") || params.get(1).equals("''")) {
					s.append("off(" + params.get(0) + ",function(_b4jsparam_jqueryevent) {");					
				} else {
					s.append("off(" + params.get(0) + "," + params.get(1) + ",function(_b4jsparam_jqueryevent) {");
				}
				next = NextToken();
				next = NextToken();
				return s.toString();
			case "trigger":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				if (params.get(1).equals("\"\"") || params.get(1).equals("''")) {
					s.append("trigger(" + params.get(0) + ")");					
				} else {
					s.append("trigger(" + params.get(0) + "," + params.get(1) + ")");
				}
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();	
			case "subready":
				next = NextToken();
				s.append("ready(function() {");				
				return s.toString();
			case "empty":
				next = NextToken();
				s.append("empty()");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "detach":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				if (params.get(0).equals("\"\"") || params.get(0).equals("''")) {
					s.append("detach()");					
				} else {
					s.append("detach(" + params.get(0) + ")");
				}
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "remove":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				if (params.get(0).equals("\"\"") || params.get(0).equals("''")) {
					s.append("remove()");					
				} else {
					s.append("remove(" + params.get(0) + ")");
				}
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "unwrap":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				if (params.get(0).equals("\"\"") || params.get(0).equals("''")) {
					s.append("unwrap()");					
				} else {
					s.append("unwrap(" + params.get(0) + ")");
				}
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "wrap":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("wrap(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "subwrap":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("wrap(function() {");
				next = NextToken();
				next = NextToken();
				return s.toString();
			case "clone":
				next = NextToken();
				s.append("clone()");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "clone2":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("clone(" + params.get(0) + ", " + params.get(1) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "wrapall":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("wrapall(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "subwrapall":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("wrapall(function() {");
				next = NextToken();
				next = NextToken();
				return s.toString();
			case "wrapinner":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("wrapinner(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "subwrapinner":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("wrapinner(function() {");
				next = NextToken();
				next = NextToken();
				return s.toString();
			case "after":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("after(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "subafter":
				next = NextToken();
				s.append("after(function() {");				
				return s.toString();
			case "before":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("before(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "subbefore":
				next = NextToken();
				s.append("before(function() {");
				
				return s.toString();
			case "insertafter":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("insertAfter(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "insertbefore":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("insertBefore(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "replaceall":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("replaceAll(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "replacewith":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("replaceWith(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "subreplacewith":
				next = NextToken();
				s.append("replaceWith(function() {");
				
				return s.toString();
			case "add":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("add(" + params.get(0) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "addback":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				if (params.get(0).equals("\"\"") || params.get(0).equals("''")) {
					s.append("addBack()");					
				} else {
					s.append("addBack(" + params.get(0) + ")");
				}
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "contents":
				next = NextToken();
				s.append("contents()");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "end":
				next = NextToken();
				s.append("end()");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "clearqueue":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				if (params.get(0).equals("\"\"") || params.get(0).equals("''")) {
					s.append("clearQueue()");					
				} else {
					s.append("clearQueue(" + params.get(0) + ")");
				}
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "dequeue":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				if (params.get(0).equals("\"\"") || params.get(0).equals("''")) {
					s.append("dequeue()");					
				} else {
					s.append("dequeue(" + params.get(0) + ")");
				}
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "queue":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				if (params.get(0).equals("\"\"") || params.get(0).equals("''")) {
					s.append("queue()");					
				} else {
					s.append("queue(" + params.get(0) + ")");
				}
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "stop":
				next = NextToken();
				s.append("stop()");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "stop2":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				if (params.get(0).equals("\"\"") || params.get(0).equals("''")) {
					s.append("stop(" + params.get(1) + "," + params.get(2) + ")");					
				} else {
					s.append("stop(" + params.get(0) + "," + params.get(1) + "," + params.get(2) + ")");
				}
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "delay":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				if (params.get(1).equals("\"\"") || params.get(1).equals("''")) {
					s.append("delay(" + params.get(0) + ")");					
				} else {
					s.append("delay(" + params.get(0) + "," + params.get(1) + ")");
				}
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "finish":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				if (params.get(0).equals("\"\"") || params.get(0).equals("''")) {
					s.append("finish()");					
				} else {
					s.append("finish(" + params.get(0) + ")");
				}
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "animate":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("animate(" + RemoveOuterQuotes(params.get(0)) + "," + params.get(1) + "," + params.get(2) + ")");
				next = NextToken();
				next = PeekToken(index);
				if (next.equals(".")) {
					s.append(ProcessJQueryElementChain(isCondition));					
				} else {
					next = NextToken();
				}
				return s.toString();
			case "subanimate":
				next = NextToken();
				params = GetParamsExtra(showExtraDebug);
				s.append("animate(" + RemoveOuterQuotes(params.get(0)) + "," + params.get(1) + "," + params.get(2) + ", function() {");
				next = NextToken();
				next = NextToken();
				return s.toString();			
			case "":
				return s.toString();
			default:
				BA.LogError("ProcessJQueryElementChain: " + next + " is NOT supported in B4JS!");
				index = currentLine.Words.size();
				break;
			}
			next = PeekToken(index);			
		}			
	}
	
	protected String ProcessJSONGenerator(B4JSExtractVariable var, boolean isCondition) {
		String extra = "self.";
		if (var.Scope.equals(SCOPE.PRIVATE)) {
			extra = "";
		}
		StringBuilder s = new StringBuilder();
		String ret = "";
		String next = PeekToken(index);
		List<String> params;		
		while (true) {
			if (DebugTrack) {
				BA.Log("ProcessJSONGenerator: " + next);
			}
			switch (next) {
			case ".":
				next = NextToken();
				break;
			case "initialize":
				next = NextToken();
				params = GetParams();
				s.append(GetInd(ind) + extra + var.JSName + "=" + params.get(0));
				next = NextToken();
				return s.toString();
			case "initialize2":
				next = NextToken();
				params = GetParams();
				s.append(GetInd(ind) + extra + var.JSName + "=" + params.get(0));
				next = NextToken();
				return s.toString();
			case "tostring":
				next = NextToken();
				ret = "JSON.stringify(" + extra + var.JSName + ")";
				next = NextToken();
				return ret;
			case "toprettystring":
				next = NextToken();
				params = GetParams();
				ret = "JSON.stringify(" + extra + var.JSName + ", null, " + params.get(0) + ")";
				next = NextToken();
				return ret;
			case "":
				return extra + var.JSName;
			default:
				BA.LogError(next + " is NOT supported in B4JS!");
				index = currentLine.Words.size();
				break;
			}
			next = PeekToken(index);			
		}			
	}
		
	protected String ProcessABMLabel(B4JSExtractVariable var, boolean isCondition) {
		String extra = "self.";
		if (var.Scope.equals(SCOPE.PRIVATE)) {
			extra = "";
		}
		
		String ret = "";
		String next = PeekToken(index);
			
		while (true) {
			if (DebugTrack) {
				BA.Log("ProcessABMLabel: " + next);
			}
			switch (next) {
			case ".":
				next = NextToken();
				break;
			case "b4jsid":
				next = NextToken();
				
				ret = extra + var.JSName + ".b4jsvar.attr('id')";
				
				return ret;				
			case "initialize":
				next = NextToken();
				return "";
			case "b4jsuniquekey":
				next = NextToken(); //b4jsuniquekey				
				next = NextToken(); // =
				next = Evaluate("", Arrays.asList(new String[] {""}), isCondition); // the key
				
				ret = extra + var.JSName + ".b4jsvar=$('[data-b4js=' + " + next.toLowerCase() + " + ']')";
				next = NextToken();
				return ret;
			case "b4jstext":
				next = NextToken(); // text
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = extra + var.JSName + ".b4jsvar.html()" + next;
						next = NextToken();
						return ret;
					} else {
						next = NextToken(); // =
						next = Evaluate("", Arrays.asList(new String[] {""}), isCondition);
						ret = extra + var.JSName + ".b4jsvar.html(b4js_buildtext(" + next + ", false))";
						next = NextToken();
						return ret;
					}
				} else {
					ret = extra + var.JSName + ".b4jsvar.html()";					
					return ret;
				}
			case "b4jsvisibility":
				next = NextToken(); // text
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = "GetVisibility(" + extra + var.JSName + ".b4jsvar.attr('id'))"  + next;
						next = NextToken();
						return ret;
					} else {
						next = NextToken(); // =
						next = Evaluate("", Arrays.asList(new String[] {""}), isCondition);
						ret = "ChangeVisibility(" + extra + var.JSName + ".b4jsvar.attr('id')," + next + ")";
						next = NextToken();
						return ret;
					}
				} else {
					ret = "GetVisibility(" + extra + var.JSName + ".b4jsvar.attr('id'))";					
					return ret;
				}			
			case "":
				return extra + var.JSName;
			default:
				BA.LogError(next + " is NOT supported in B4JS!");
				index = currentLine.Words.size();
				break;
			}
			next = PeekToken(index);			
		}		
	}
	
	protected String ProcessABMInput(B4JSExtractVariable var, boolean isCondition) {
		String extra = "self.";
		if (var.Scope.equals(SCOPE.PRIVATE)) {
			extra = "";
		}
		
		String ret = "";
		String next = PeekToken(index);
		
		while (true) {
			if (DebugTrack) {
				BA.Log("ProcessABMInput: " + next);
			}
			switch (next) {
			case ".":
				next = NextToken();
				break;
			case "b4jsid":
				next = NextToken();
				
				ret = extra + var.JSName + ".b4jsvar.attr('id')";
				
				return ret;	
			case "initialize":
				next = NextToken();
				return "";
			case "b4jsuniquekey":
				next = NextToken(); //b4jsuniquekey				
				next = NextToken(); // =
				next = Evaluate("", Arrays.asList(new String[] {""}), isCondition); // the key
				
				ret = extra + var.JSName + ".b4jsvar=$('[data-b4js=' + " + next.toLowerCase() + " + ']')";
				next = NextToken();
				return ret;
			case "b4jstext":
				next = NextToken(); // text
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = extra + var.JSName + ".b4jsvar.val()" + next;
						next = NextToken();
						return ret;
					} else {
						next = NextToken(); // =
						next = Evaluate("", Arrays.asList(new String[] {""}), isCondition);
						ret = extra + var.JSName + ".b4jsvar.val(b4js_buildtext(" + next + ", false))";
						next = NextToken();
						return ret;
					}
				} else {
					ret = extra + var.JSName + ".b4jsvar.val()";					
					return ret;
				}
			case "b4jssetfocus":
				next = NextToken();
				ret = extra + var.JSName + ".b4jsvar.focus()";
				next = NextToken();
				return ret;		
			case "b4jsvisibility":
				next = NextToken(); // text
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = "GetVisibility(" + extra + var.JSName + ".b4jsvar.attr('id') + \"-parent\")"  + next;
						next = NextToken();
						return ret;
					} else {
						next = NextToken(); // =
						next = Evaluate("", Arrays.asList(new String[] {""}), isCondition);
						ret = "ChangeVisibility(" + extra + var.JSName + ".b4jsvar.attr('id') + \"-parent\"," + next + ")";
						next = NextToken();
						return ret;
					}
				} else {
					ret = "GetVisibility(" + extra + var.JSName + ".b4jsvar.attr('id') + \"-parent\")";					
					return ret;
				}	
			case "b4jsenabled":
				next = NextToken(); // text
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = "GetEnabled(" + extra + var.JSName + ".b4jsvar.attr('id'))"  + next;
						next = NextToken();
						return ret;
					} else {
						next = NextToken(); // =
						next = Evaluate("", Arrays.asList(new String[] {""}), isCondition);
						ret = "SetEnabled(" + extra + var.JSName + ".b4jsvar.attr('id')," + next + ")";
						next = NextToken();
						return ret;
					}
				} else {
					ret = "GetEnabled(" + extra + var.JSName + ".b4jsvar.attr('id'))";					
					return ret;
				}
			case "":
				return extra + var.JSName;
			default:
				BA.LogError(next + " is NOT supported in B4JS!");
				index = currentLine.Words.size();
				break;
			}
			next = PeekToken(index);			
		}		
	}
	
	protected String ProcessABMContainer(B4JSExtractVariable var, boolean isCondition) {
		String extra = "self.";
		if (var.Scope.equals(SCOPE.PRIVATE)) {
			extra = "";
		}
		
		String ret = "";
		String next = PeekToken(index);
		
		while (true) {
			if (DebugTrack) {
				BA.Log("ProcessABMContainer: " + next);
			}
			switch (next) {
			case ".":
				next = NextToken();
				break;
			case "b4jsid":
				next = NextToken();
				
				ret = extra + var.JSName + ".b4jsvar.attr('id')";
				
				return ret;	
			case "initialize":
				next = NextToken();
				return "";
			case "b4jsuniquekey":
				next = NextToken(); //b4jsuniquekey				
				next = NextToken(); // =
				next = Evaluate("", Arrays.asList(new String[] {""}), isCondition); // the key
				
				ret = extra + var.JSName + ".b4jsvar=$('[data-b4js=' + " + next.toLowerCase() + " + ']')";
				next = NextToken();
				return ret;			
			case "b4jsvisibility":
				next = NextToken(); // text
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = "GetVisibility(" + extra + var.JSName + ".b4jsvar.attr('id') + \"-parent\")"  + next;
						next = NextToken();
						return ret;
					} else {
						next = NextToken(); // =
						next = Evaluate("", Arrays.asList(new String[] {""}), isCondition);
						ret = "ChangeVisibility(" + extra + var.JSName + ".b4jsvar.attr('id') + \"-parent\"," + next + ")";
						next = NextToken();
						return ret;
					}
				} else {
					ret = "GetVisibility(" + extra + var.JSName + ".b4jsvar.attr('id') + \"-parent\")";					
					return ret;
				}				
			case "":
				return extra + var.JSName;
			default:
				BA.LogError(next + " is NOT supported in B4JS!");
				index = currentLine.Words.size();
				break;
			}
			next = PeekToken(index);			
		}		
	}
	
	protected String ProcessABMSwitch(B4JSExtractVariable var, boolean isCondition) {
		String extra = "self.";
		if (var.Scope.equals(SCOPE.PRIVATE)) {
			extra = "";
		}
		
		String ret = "";
		String next = PeekToken(index);
		
		while (true) {
			if (DebugTrack) {
				BA.Log("ProcessABMSwitch: " + next);
			}
			switch (next) {
			case ".":
				next = NextToken();
				break;
			case "b4jsid":
				next = NextToken();
				
				ret = extra + var.JSName + ".b4jsvar.attr('id')";
				
				return ret;	
			case "initialize":
				next = NextToken();
				return "";
			case "b4jsuniquekey":
				next = NextToken(); //b4jsuniquekey				
				next = NextToken(); // =
				next = Evaluate("", Arrays.asList(new String[] {""}), isCondition); // the key
				
				ret = extra + var.JSName + ".b4jsvar=$('[data-b4js=' + " + next.toLowerCase() + " + ']')";
				next = NextToken();
				return ret;
			case "b4jsstate":
				next = NextToken(); // text
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = extra + var.JSName + ".b4jsvar.find('#' + " + extra + var.JSName + ".b4jsvar.attr('id') + 'input').prop('checked')" + next;
						next = NextToken();
						return ret;
					} else {
						next = NextToken(); // =
						next = Evaluate("", Arrays.asList(new String[] {""}), isCondition);
						ret = "SetChecked(" + extra + var.JSName + ".b4jsvar.attr('id') + 'input'," + next + ")";
						next = NextToken();
						return ret;
					}
				} else {
					ret = extra + var.JSName + ".b4jsvar.find('#' + " + extra + var.JSName + ".b4jsvar.attr('id') + 'input').prop('checked')";					
					return ret;
				}
			case "b4jsvisibility":
				next = NextToken(); // text
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = "GetVisibility(" + extra + var.JSName + ".b4jsvar.attr('id'))"  + next;
						next = NextToken();
						return ret;
					} else {
						next = NextToken(); // =
						next = Evaluate("", Arrays.asList(new String[] {""}), isCondition);
						ret = "ChangeVisibility(" + extra + var.JSName + ".b4jsvar.attr('id')," + next + ")";
						next = NextToken();
						return ret;
					}
				} else {
					ret = "GetVisibility(" + extra + var.JSName + ".b4jsvar.attr('id'))";					
					return ret;
				}
			case "b4jsenabled":
				next = NextToken(); // text
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = "GetEnabled(" + extra + var.JSName + ".b4jsvar.attr('id') + \"input\")"  + next;
						next = NextToken();
						return ret;
					} else {
						next = NextToken(); // =
						next = Evaluate("", Arrays.asList(new String[] {""}), isCondition);
						ret = "SetEnabled(" + extra + var.JSName + ".b4jsvar.attr('id') + \"input\"," + next + ")";
						next = NextToken();
						return ret;
					}
				} else {
					ret = "GetEnabled(" + extra + var.JSName + ".b4jsvar.attr('id') + \"input\")";					
					return ret;
				}
			case "":
				return extra + var.JSName;
			default:
				BA.LogError(next + " is NOT supported in B4JS!");
				index = currentLine.Words.size();
				break;
			}
			next = PeekToken(index);			
		}		
	}
	
	protected String ProcessABMCheckBox(B4JSExtractVariable var, boolean isCondition) {
		String extra = "self.";
		if (var.Scope.equals(SCOPE.PRIVATE)) {
			extra = "";
		}
		
		String ret = "";
		String next = PeekToken(index);
				
		while (true) {
			if (DebugTrack) {
				BA.Log("ProcessABMCheckBox: " + next);
			}
			switch (next) {
			case ".":
				next = NextToken();
				break;
			case "b4jsid":
				next = NextToken();
		
				ret = extra + var.JSName + ".b4jsvar.find('input').attr('id')";
		
				return ret;	
			case "initialize":
				next = NextToken();
				return "";
			case "b4jsuniquekey":
				next = NextToken(); //b4jsuniquekey				
				next = NextToken(); // =
				next = Evaluate("", Arrays.asList(new String[] {""}), isCondition); // the key
		
				ret = extra + var.JSName + ".b4jsvar=$('[data-b4js=' + " + next.toLowerCase() + " + ']')";
				next = NextToken();
				return ret;
			case "b4jsstate":
				next = NextToken(); // text
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = extra + var.JSName + ".b4jsvar.find('input').prop('checked')" + next;
						next = NextToken();
						return ret;
					} else {
						next = NextToken(); // =
						next = Evaluate("", Arrays.asList(new String[] {""}), isCondition);
						ret = "SetChecked(" + extra + var.JSName + ".b4jsvar.find('input').attr('id')," + next + ")";
						next = NextToken();
						return ret;
					}
				} else {
					ret = extra + var.JSName + ".b4jsvar.find('input').prop('checked')";					
					return ret;
				}
			case "b4jsvisibility":
				next = NextToken(); // text
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = "GetVisibility(" + extra + var.JSName + ".b4jsvar.attr('id') + \"-parent\")"  + next;
						next = NextToken();
						return ret;
					} else {
						next = NextToken(); // =
						next = Evaluate("", Arrays.asList(new String[] {""}), isCondition);
						ret = "ChangeVisibility(" + extra + var.JSName + ".b4jsvar.attr('id') + \"-parent\"," + next + ")";
						next = NextToken();
						return ret;
					}
				} else {
					ret = "GetVisibility(" + extra + var.JSName + ".b4jsvar.attr('id') + \"-parent\")";					
					return ret;
				}	
			case "b4jsenabled":
				next = NextToken(); // text
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = "GetEnabled(" + extra + var.JSName + ".b4jsvar.attr('id') + \"input\")"  + next;
						next = NextToken();
						return ret;
					} else {
						next = NextToken(); // =
						next = Evaluate("", Arrays.asList(new String[] {""}), isCondition);
						ret = "SetEnabled(" + extra + var.JSName + ".b4jsvar.attr('id') + \"input\"," + next + ")";
						next = NextToken();
						return ret;
					}
				} else {
					ret = "GetEnabled(" + extra + var.JSName + ".b4jsvar.attr('id') + \"input\")";					
					return ret;
				}
			case "":
				return extra + var.JSName;
			default:
				BA.LogError(next + " is NOT supported in B4JS!");
				index = currentLine.Words.size();
				break;
			}
			next = PeekToken(index);			
		}		
	}
	
	protected String ProcessABMRadioGroup(B4JSExtractVariable var, boolean isCondition) {
		String extra = "self.";
		if (var.Scope.equals(SCOPE.PRIVATE)) {
			extra = "";
		}
		
		String ret = "";
		String next = PeekToken(index);
				
		while (true) {
			if (DebugTrack) {
				BA.Log("ProcessABMCheckBox: " + next);
			}
			switch (next) {
			case ".":
				next = NextToken();
				break;
			case "b4jsid":
				next = NextToken();
		
				ret = extra + var.JSName + ".b4jsvar.attr('id')";
		
				return ret;	
			case "initialize":
				next = NextToken();
				return "";
			case "b4jsuniquekey":
				next = NextToken(); //b4jsuniquekey				
				next = NextToken(); // =
				next = Evaluate("", Arrays.asList(new String[] {""}), isCondition); // the key
		
				ret = extra + var.JSName + ".b4jsvar=$('[data-b4js=' + " + next.toLowerCase() + " + ']')";
				next = NextToken();
				return ret;
			case "b4jsgetactive":
				next = NextToken(); // text
				
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = "getactiveradiobutton(" + extra + var.JSName + ".b4jsvar.attr('id'))" + next;
						next = NextToken();
						return ret;
					} else {
						next = NextToken(); // =
						next = Evaluate("", Arrays.asList(new String[] {""}), isCondition);
						ret = "getactiveradiobutton(" + extra + var.JSName + ".b4jsvar.attr('id'))";
						next = NextToken();
						return ret;
					}
				} else {
					ret = "getactiveradiobutton(" + extra + var.JSName + ".b4jsvar.attr('id'))";					
					return ret;
				}
			case "b4jssetactive":
				next = NextToken(); // text
				next = NextToken(); // =
				next = Evaluate("", Arrays.asList(new String[] {""}), isCondition);
				ret = "setactiveradiobutton(" + extra + var.JSName + ".b4jsvar.attr('id')," + next;
				next = NextToken();
				return ret;
			case "b4jsvisibility":
				next = NextToken(); // text
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = "GetVisibility(" + extra + var.JSName + ".b4jsvar.attr('id'))"  + next;
						next = NextToken();
						return ret;
					} else {
						next = NextToken(); // =
						next = Evaluate("", Arrays.asList(new String[] {""}), isCondition);
						ret = "ChangeVisibility(" + extra + var.JSName + ".b4jsvar.attr('id')," + next + ")";
						next = NextToken();
						return ret;
					}
				} else {
					ret = "GetVisibility(" + extra + var.JSName + ".b4jsvar.attr('id'))";					
					return ret;
				}
			
			case "":
				return extra + var.JSName;
			default:
				BA.LogError(next + " is NOT supported in B4JS!");
				index = currentLine.Words.size();
				break;
			}
			next = PeekToken(index);			
		}		
	}
	
	protected String ProcessABMButton(B4JSExtractVariable var, boolean isCondition) {
		String extra = "self.";
		if (var.Scope.equals(SCOPE.PRIVATE)) {
			extra = "";
		}
		
		String ret = "";
		String next = PeekToken(index);
				
		while (true) {
			if (DebugTrack) {
				BA.Log("ProcessABMButton: " + next);
			}
			switch (next) {
			case ".":
				next = NextToken();
				break;
			case "b4jsid":
				next = NextToken();
		
				ret = extra + var.JSName + ".b4jsvar.attr('id')";
		
				return ret;	
			case "initialize":
				next = NextToken();
				return "";
			case "b4jsuniquekey":
				next = NextToken(); //b4jsuniquekey				
				next = NextToken(); // =
				next = Evaluate("", Arrays.asList(new String[] {""}), isCondition); // the key
		
				ret = extra + var.JSName + ".b4jsvar=$('[data-b4js=' + " + next.toLowerCase() + " + ']')";
				next = NextToken();
				return ret;
			case "b4jstext":
				next = NextToken(); // text
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = extra + var.JSName + ".b4jsvar.html()" + next;
						next = NextToken();
						return ret;
					} else {
						next = NextToken(); // =
						next = Evaluate("", Arrays.asList(new String[] {""}), isCondition);
						ret = extra + var.JSName + ".b4jsvar.html(b4js_buildtext(" + next + ", false))";
						next = NextToken();
						return ret;
					}
				} else {
					ret = extra + var.JSName + ".b4jsvar.html()";					
					return ret;
				}
			case "b4jsvisibility":
				next = NextToken(); // text
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = "GetVisibility(" + extra + var.JSName + ".b4jsvar.attr('id'))"  + next;
						next = NextToken();
						return ret;
					} else {
						next = NextToken(); // =
						next = Evaluate("", Arrays.asList(new String[] {""}), isCondition);
						ret = "ChangeVisibility(" + extra + var.JSName + ".b4jsvar.attr('id')," + next + ")";
						next = NextToken();
						return ret;
					}
				} else {
					ret = "GetVisibility(" + extra + var.JSName + ".b4jsvar.attr('id'))";					
					return ret;
				}
			case "b4jsenabled":
				next = NextToken(); // text
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = "GetEnabled(" + extra + var.JSName + ".b4jsvar.attr('id'))"  + next;
						next = NextToken();
						return ret;
					} else {
						next = NextToken(); // =
						next = Evaluate("", Arrays.asList(new String[] {""}), isCondition);
						ret = "SetEnabled(" + extra + var.JSName + ".b4jsvar.attr('id')," + next + ")";
						next = NextToken();
						return ret;
					}
				} else {
					ret = "GetEnabled(" + extra + var.JSName + ".b4jsvar.attr('id'))";					
					return ret;
				}
			case "":
				return extra + var.JSName;
			default:
				BA.LogError(next + " is NOT supported in B4JS!");
				index = currentLine.Words.size();
				break;
			}
			next = PeekToken(index);			
		}		
	}
	
	protected String ProcessABMRange(B4JSExtractVariable var, boolean isCondition) {
		String extra = "self.";
		if (var.Scope.equals(SCOPE.PRIVATE)) {
			extra = "";
		}
		
		String ret = "";
		String next = PeekToken(index);
		List<String> params;		
		while (true) {
			if (DebugTrack) {
				BA.Log("ProcessABMRange: " + next);
			}
			switch (next) {
			case ".":
				next = NextToken();
				break;
			case "b4jsid":
				next = NextToken();
		
				ret = extra + var.JSName + ".b4jsvar.attr('id')";
		
				return ret;	
			case "initialize":
				next = NextToken();
				return "";
			case "b4jsuniquekey":
				next = NextToken(); //b4jsuniquekey				
				next = NextToken(); // =
				next = Evaluate("", Arrays.asList(new String[] {""}), isCondition); // the key
		
				ret = extra + var.JSName + ".b4jsvar=$('[data-b4js=' + " + next.toLowerCase() + " + ']')";
				next = NextToken();
				return ret;			
			case "b4jsvisibility":
				next = NextToken(); // text
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = "GetVisibility(" + extra + var.JSName + ".b4jsvar.attr('id'))"  + next;
						next = NextToken();
						return ret;
					} else {
						next = NextToken(); // =
						next = Evaluate("", Arrays.asList(new String[] {""}), isCondition);
						ret = "ChangeVisibility(" + extra + var.JSName + ".b4jsvar.attr('id')," + next + ")";
						next = NextToken();
						return ret;
					}
				} else {
					ret = "GetVisibility(" + extra + var.JSName + ".b4jsvar.attr('id'))";					
					return ret;
				}
			case "b4jsgetstart":
				next = NextToken(); // text
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = "GetRangeStart(" + extra + var.JSName + ".b4jsvar.attr('id'))"  + next;
						next = NextToken();
						return ret;
					}
				} else {
					ret = "GetRangeStart(" + extra + var.JSName + ".b4jsvar.attr('id'))";					
					return ret;
				}
			case "b4jsgetstop":
				next = NextToken(); // text
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = "GetRangeStop(" + extra + var.JSName + ".b4jsvar.attr('id')," + extra + var.JSName + ".b4jsvar.data('b4jsextra'))"  + next;
						next = NextToken();
						return ret;
					}
				} else {
					ret = "GetRangeStop(" + extra + var.JSName + ".b4jsvar.attr('id')," + extra + var.JSName + ".b4jsvar.data('b4jsextra'))";					
					return ret;
				}
			case "b4jssetstart":
				next = NextToken(); // text
				params = GetParams();
				ret = "SetRangeStart(" + extra + var.JSName + ".b4jsvar.attr('id')," + extra + var.JSName + ".b4jsvar.data('b4jsextra')," + params.get(0) + ")";
				next = NextToken();
				return ret;
			case "b4jssetstop":
				next = NextToken(); // text
				params = GetParams();
				ret = "SetRangeStop(" + extra + var.JSName + ".b4jsvar.attr('id')," + extra + var.JSName + ".b4jsvar.data('b4jsextra')," + params.get(0) + ")";
				next = NextToken();
				return ret;
			case "":
				return extra + var.JSName;
			default:
				BA.LogError(next + " is NOT supported in B4JS!");
				index = currentLine.Words.size();
				break;
			}
			next = PeekToken(index);			
		}		
	}
	
	protected String ProcessABMSlider(B4JSExtractVariable var, boolean isCondition) {
		String extra = "self.";
		if (var.Scope.equals(SCOPE.PRIVATE)) {
			extra = "";
		}
		
		String ret = "";
		String next = PeekToken(index);
		List<String> params;		
		while (true) {
			if (DebugTrack) {
				BA.Log("ProcessABMSlider: " + next);
			}
			switch (next) {
			case ".":
				next = NextToken();
				break;
			case "b4jsid":
				next = NextToken();
		
				ret = extra + var.JSName + ".b4jsvar.attr('id')";
		
				return ret;	
			case "initialize":
				next = NextToken();
				return "";
			case "b4jsuniquekey":
				next = NextToken(); //b4jsuniquekey				
				next = NextToken(); // =
				next = Evaluate("", Arrays.asList(new String[] {""}), isCondition); // the key
		
				ret = extra + var.JSName + ".b4jsvar=$('[data-b4js=' + " + next.toLowerCase() + " + ']')";
				next = NextToken();
				return ret;			
			case "b4jsvisibility":
				next = NextToken(); // text
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = "GetVisibility(" + extra + var.JSName + ".b4jsvar.attr('id'))"  + next;
						next = NextToken();
						return ret;
					} else {
						next = NextToken(); // =
						next = Evaluate("", Arrays.asList(new String[] {""}), isCondition);
						ret = "ChangeVisibility(" + extra + var.JSName + ".b4jsvar.attr('id')," + next + ")";
						next = NextToken();
						return ret;
					}
				} else {
					ret = "GetVisibility(" + extra + var.JSName + ".b4jsvar.attr('id'))";					
					return ret;
				}
			case "b4jsgetvalue":
				next = NextToken(); // text
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = "GetSliderValue(" + extra + var.JSName + ".b4jsvar.attr('id'))"  + next;
						next = NextToken();
						return ret;
					}
				} else {
					ret = "GetSliderValue(" + extra + var.JSName + ".b4jsvar.attr('id'))";					
					return ret;
				}
			case "b4jssetvalue":
				next = NextToken(); // text
				params = GetParams();
				ret = "SetSliderValue(" + extra + var.JSName + ".b4jsvar.attr('id')," + extra + var.JSName + ".b4jsvar.data('b4jsextra')," + params.get(0) + ")";
				next = NextToken();
				return ret;
			case "":
				return extra + var.JSName;
			default:
				BA.LogError(next + " is NOT supported in B4JS!");
				index = currentLine.Words.size();
				break;
			}
			next = PeekToken(index);			
		}		
	}
	
	protected String ProcessABMTabs(B4JSExtractVariable var, boolean isCondition) {
		String extra = "self.";
		if (var.Scope.equals(SCOPE.PRIVATE)) {
			extra = "";
		}
		//StringBuilder s = new StringBuilder();
		String ret = "";
		String next = PeekToken(index);
		List<String> params;		
		while (true) {
			if (DebugTrack) {
				BA.Log("ProcessABMTabs: " + next);
			}
			switch (next) {
			case ".":
				next = NextToken();
				break;
			case "b4jsid":
				next = NextToken();
				
				ret = extra + var.JSName + ".b4jsvar.attr('id')";
				
				return ret;	
			case "initialize":
				next = NextToken();
				return "";
			case "b4jsuniquekey":
				next = NextToken(); //b4jsuniquekey				
				next = NextToken(); // =
				next = Evaluate("", Arrays.asList(new String[] {""}), isCondition); // the key
				
				ret = extra + var.JSName + ".b4jsvar=$('[data-b4js=' + " + next.toLowerCase() + " + ']')";
				next = NextToken();
				return ret;			
			case "b4jsvisibility":
				next = NextToken(); // text
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = "GetVisibility(" + extra + var.JSName + ".b4jsvar.attr('id'))"  + next;
						next = NextToken();
						return ret;
					} else {
						next = NextToken(); // =
						next = Evaluate("", Arrays.asList(new String[] {""}), isCondition);
						ret = "ChangeVisibility(" + extra + var.JSName + ".b4jsvar.attr('id')," + next + ")";
						next = NextToken();
						return ret;
					}
				} else {
					ret = "GetVisibility(" + extra + var.JSName + ".b4jsvar.attr('id'))";					
					return ret;
				}
			case "b4jsgetactive":
				next = NextToken(); // text
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = "getactivetab(" + extra + var.JSName + ".b4jsvar.attr('id'))"  + next;
						next = NextToken();
						return ret;
					}
				} else {
					ret = "getactivetab(" + extra + var.JSName + ".b4jsvar.attr('id'))";					
					return ret;
				}
			case "b4jssetactive":
				next = NextToken(); // text
				params = GetParams();
				ret = "setactivetab(" + extra + var.JSName + ".b4jsvar.attr('id')," + params.get(0) + ")";
				next = NextToken();
				return ret;
			case "":
				return extra + var.JSName;
			default:
				BA.LogError(next + " is NOT supported in B4JS!");
				index = currentLine.Words.size();
				break;
			}
			next = PeekToken(index);			
		}		
	}
	
	protected String ProcessABMActionButton(B4JSExtractVariable var, boolean isCondition) {
		String extra = "self.";
		if (var.Scope.equals(SCOPE.PRIVATE)) {
			extra = "";
		}
		
		String ret = "";
		String next = PeekToken(index);
				
		while (true) {
			if (DebugTrack) {
				BA.Log("ProcessABMActionButton: " + next);
			}
			switch (next) {
			case ".":
				next = NextToken();
				break;
			case "b4jsid":
				next = NextToken();
		
				ret = extra + var.JSName + ".b4jsvar.attr('id')";
		
				return ret;	
			case "initialize":
				next = NextToken();
				return "";
			case "b4jsuniquekey":
				next = NextToken(); //b4jsuniquekey				
				next = NextToken(); // =
				next = Evaluate("", Arrays.asList(new String[] {""}), isCondition); // the key
		
				ret = extra + var.JSName + ".b4jsvar=$('[data-b4js=' + " + next.toLowerCase() + " + ']')";
				next = NextToken();
				return ret;			
			case "b4jsvisibility":
				next = NextToken(); // text
				next = PeekToken(index);
				if (next.equals("=")) {
					if (isCondition) {
						next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
						ret = "GetVisibility(" + extra + var.JSName + ".b4jsvar.attr('id'))"  + next;
						next = NextToken();
						return ret;
					} else {
						next = NextToken(); // =
						next = Evaluate("", Arrays.asList(new String[] {""}), isCondition);
						ret = "ChangeVisibility(" + extra + var.JSName + ".b4jsvar.attr('id')," + next + ")";
						next = NextToken();
						return ret;
					}
				} else {
					ret = "GetVisibility(" + extra + var.JSName + ".b4jsvar.attr('id'))";					
					return ret;
				}
			case "":
				return extra + var.JSName;
			default:
				BA.LogError(next + " is NOT supported in B4JS!");
				index = currentLine.Words.size();
				break;
			}
			next = PeekToken(index);			
		}		
	}
	
	protected String ProcessTimer(B4JSExtractVariable var, boolean isCondition) {
		String extra = "self.";
		if (var.Scope.equals(SCOPE.PRIVATE)) {
			extra = "";
		}
		StringBuilder s = new StringBuilder();
		String next = PeekToken(index);
		
		List<String> params;
		
		while (true) {
			if (DebugTrack) {
				BA.Log("ProcessTimer: " + next);
			}
			switch (next) {
			case ".":
				next = NextToken();
				break;
			case "initialize":
				next = NextToken();
				params = GetParams();			
				B4JSExtractResultMethod meth = GetMethod(params.get(0).toLowerCase().substring(1, params.get(0).length()-1) + "_tick");
				if (meth!=null) {
					var.EventName = params.get(0).toLowerCase().substring(1, params.get(0).length()-1);
					s.append(extra + "_" + var.JSName + ".eventname=\"" + var.EventName + "\";" + NL);
					s.append(GetInd(ind) + extra + "_" + var.JSName + ".interval=" + params.get(1) + ";" + NL);
					s.append(GetInd(ind) + extra + "_" + var.JSName + ".enabled=false;" + NL);
					s.append(GetInd(ind) + extra + "_" + var.JSName + ".isinitialized=true");
				} else {
					BA.LogError("No timer event called " + params.get(0).toLowerCase().substring(1, params.get(0).length()-1) + "_tick" + " found!");
				}
				next = NextToken();
				return s.toString();
			case "enabled":	
				next = NextToken();
				//next = NextToken();				
				if (isCondition) {
					next = Evaluate("", Arrays.asList(new String[] {"then"}), isCondition);
					return extra + "_" + var.JSName + ".enabled" + next;
				}
				next = NextToken();
				next = Evaluate("", Arrays.asList(new String[] {""}), isCondition);
				s.append(extra + "_" + var.JSName + ".enabled=" + next + ";" + NL);
				if (next.equals("true")) {
					B4JSExtractResultMethod meth2 = GetMethod(var.EventName + "_tick");
					if (meth2!=null) {						
						s.append(GetInd(ind) + extra + var.JSName + "=setInterval(self." + var.EventName + "_tick, " + extra + "_" + var.JSName + ".interval)");
					} else {
						BA.LogError("No timer event called " + var.EventName + "_tick" + " found!");
					}					
				} else {
					s.append(GetInd(ind) + "if (" + extra + var.JSName + ") {" + NL);
					s.append(GetInd(ind+1) + "clearInterval(" + extra + var.JSName + ");" + NL);
					s.append(GetInd(ind+1) + extra + var.JSName + "=null;" + NL);
					s.append(GetInd(ind) + "}");
				}
				next = NextToken();
				return s.toString();
			case "isinitialized":
				next = NextToken(); // isinitialized
				return extra + "_" + var.JSName + ".isinitialized";				
			case "interval":
				next = NextToken();	
				return extra + "_" + var.JSName + ".interval";				
			case "":
				return extra + var.JSName;
			default:
				next = NextToken();
				break;
			}
			next = PeekToken(index);			
		}
		
	}
	
	protected String ProcessStringBuilder(B4JSExtractVariable var, boolean isCondition) {
		String extra = "self.";
		if (var.Scope.equals(SCOPE.PRIVATE)) {
			extra = "";
		}
		StringBuilder s = new StringBuilder();
		String next = PeekToken(index);
		
		List<String> params;
		
		while (true) {
			if (DebugTrack) {
				BA.Log("ProcessStringBuilder: " + next);
			}
			switch (next) {
			case ".":
				next = NextToken();
				break;
			case "initialize":
		
				next = NextToken();
		
				return s.toString();
			case "append":	
				next = NextToken();
				params = GetParams();
				s.append(extra + var.JSName + ".append(" + params.get(0) + ")");
				next = NextToken();
				return s.toString();
			case "insert":
				next = NextToken();
				params = GetParams();
				s.append(extra + var.JSName + ".insert(" + params.get(0) + ", " + params.get(1) + ")");
				next = NextToken();
				return s.toString();
			case "remove":	
				next = NextToken();
				params = GetParams();
				s.append(extra + var.JSName + ".remove(" + params.get(0) + ", " + params.get(1) + ")");
				next = NextToken();
				return s.toString();
			case "length":	
				next = NextToken();
				s.append(extra + var.JSName + ".length()");
				next = NextToken();
				return s.toString();
			case "tostring":
				next = NextToken();
				s.append(extra + var.JSName + ".toString()");
				next = NextToken();
				return s.toString();
			case "isinitialized":
				next = NextToken(); // isinitialized
				return "true"; //extra + var.JSName + ".isinitialized";				
			case "":
				return extra + var.JSName;
			default:
				next = NextToken();
				break;
			}
			next = PeekToken(index);			
		}
		
	}
	
	protected String ProcessList(B4JSExtractVariable var, boolean isCondition) {
		String extra = "self.";
		if (var.Scope.equals(SCOPE.PRIVATE)) {
			extra = "";
		}
		StringBuilder s = new StringBuilder();
		String next = PeekToken(index);
		String tmpEval="";
		List<String> params;
		String arrIndex="";
		while (true) {
			if (DebugTrack) {
				BA.Log("ProcessList: " + next);
			}
			switch (next) {
			case ".":
				next = NextToken();
				break;
			case "initialize":
				next = NextToken();
				return "";
			case "initialize2":
				next = NextToken(); // initialize2
				next = NextToken(); // ( // has to, don't know why yet
				tmpEval = Evaluate("(", Arrays.asList(new String[] {")"}), isCondition);
				s.append(extra + var.JSName + "=" + tmpEval);
				next = NextToken(); // )
				return s.toString();
			case "get":
				next = NextToken(); // get
				
				s.append(extra + var.JSName + "[" + Evaluate("(", Arrays.asList(new String[] {")"}), isCondition) +"]");
				next = NextToken(); // )
				return s.toString();
			case "add":
				next = NextToken(); // add
				
				s.append(extra + var.JSName + ".push(" + Evaluate("(", Arrays.asList(new String[] {")"}), isCondition) +")");
				next = NextToken(); // )
				return s.toString();
			case "addallat":
				next = NextToken(); // addallat
				
				params = GetParams();
				arrIndex = params.get(0);
				params.remove(0);	
				s.append(extra + var.JSName + ".splice(" + arrIndex + ",0," + String.join(",", params) + ")");
				next = NextToken(); // )
				return s.toString();
			case "addall":
				next = NextToken(); // addall
				
				params = GetParams();
				s.append(extra + var.JSName + ".splice(" + extra + var.JSName + ".length,0," + String.join(",", params) + ")");
				next = NextToken(); // )
				return s.toString();
			case "clear": 
				next = NextToken(); // clear
				s.append(extra + var.JSName + ".length=0");
				return s.toString();
			case "indexof":
				next = NextToken(); // indexof
				
				s.append(extra + var.JSName + ".indexOf(" + Evaluate("(", Arrays.asList(new String[] {")"}), isCondition) + ")");
				next = NextToken(); // )
				return s.toString();
			case "insertat":
				next = NextToken(); // insertat
				
				params = GetParams();
				arrIndex = params.get(0);
				params.remove(0);	
				
				s.append(extra + var.JSName + ".splice.apply(" + extra + var.JSName + ",[" + arrIndex + ",0].concat(" + String.join(",", params) + "))");
				next = NextToken(); // )
				return s.toString();
			case "isinitialized":
				next = NextToken(); // isinitialized
				return "true";
			case "removeat":
				next = NextToken(); // removeat
				
				s.append(extra + var.JSName + ".splice(0," + Evaluate("(", Arrays.asList(new String[] {")"}), isCondition) + ")");
				next = NextToken(); // )
				return s.toString();
			case "size":
				next = NextToken(); // size
				s.append(extra + var.JSName + ".length");
				return s.toString();
			case "set":
				next = NextToken(); // set
				
				params = GetParams();
				s.append(extra + var.JSName + "[" + params.get(0) + "]=" + params.get(1));
				next = NextToken(); // )
				return s.toString();
			case "sort":
				next = NextToken(); // sort
				
				params = GetParams();
				if (params.get(0).equals("true")) {
					s.append(GetInd(ind+1) + "if (!isNaN(parseFloat(" + extra + var.JSName +"[0])) && isFinite(" + extra + var.JSName +"[0])) {" + NL);
					s.append(GetInd(ind+2) + extra + var.JSName + ".sort(function(a, b){return a - b});" + NL);
					s.append(GetInd(ind+1) + "} else {" + NL);
					s.append(GetInd(ind+2) + extra + var.JSName + ".sort();" + NL);
					s.append(GetInd(ind+1) + "}\n"); // \n needed! No not use NL
				} else {
					if (params.get(0).equals("false")) {
						s.append(GetInd(ind+1) + "if (!isNaN(parseFloat(" + extra + var.JSName +"[0])) && isFinite(" + extra + var.JSName +"[0])) {" + NL);
						s.append(GetInd(ind+2) + extra + var.JSName + ".sort(function(a, b){return b - a});" + NL);
						s.append(GetInd(ind+1) + "} else {" + NL);
						s.append(GetInd(ind+2) + extra + var.JSName + ".sort(function(a, b){if (a>b) {return -1;} else {if (a<b) {return 1;} else {return 0;}}});" + NL);
						s.append(GetInd(ind+1) + "}\n"); // \n needed! No not use NL
					} else {
						s.append("if (" + params.get(0) + "==true) {" + NL);
						s.append(GetInd(ind+1) + "if (!isNaN(parseFloat(" + extra + var.JSName +"[0])) && isFinite(" + extra + var.JSName +"[0])) {" + NL);
						s.append(GetInd(ind+2) + extra + var.JSName + ".sort(function(a, b){return a - b});" + NL);
						s.append(GetInd(ind+1) + "} else {" + NL);
						s.append(GetInd(ind+2) + extra + var.JSName + ".sort();" + NL);
						s.append(GetInd(ind+1) + "}" + NL);
						s.append(GetInd(ind) + "} else {" + NL);
						s.append(GetInd(ind+1) + "if (!isNaN(parseFloat(" + extra + var.JSName +"[0])) && isFinite(" + extra + var.JSName +"[0])) {" + NL);
						s.append(GetInd(ind+2) + extra + var.JSName + ".sort(function(a, b){return b - a});" + NL);
						s.append(GetInd(ind+1) + "} else {" + NL);
						s.append(GetInd(ind+2) + extra + var.JSName + ".sort(function(a, b){if (a>b) {return -1;} else {if (a<b) {return 1;} else {return 0;}}});" + NL);
						s.append(GetInd(ind+1) + "}" + NL);
						s.append(GetInd(ind) + "}\n"); // \n needed! No not use NL
					}
				}				
				next = NextToken(); // )
				return s.toString();
			case "sortcaseinsensitive":
				next = NextToken(); // sortcaseinsensitive
				
				params = GetParams();
				if (params.get(0).equals("true")) {
					s.append(GetInd(ind+1) + "if (!isNaN(parseFloat(" + extra + var.JSName +"[0])) && isFinite(" + extra + var.JSName +"[0])) {" + NL);
					s.append(GetInd(ind+2) + extra + var.JSName + ".sort(function(a, b){return a - b});" + NL);
					s.append(GetInd(ind+1) + "} else {" + NL);
					s.append(GetInd(ind+2) + extra + var.JSName + ".sort(function (a, b) {return a.toUpperCase().localeCompare(b.toUpperCase());});" + NL);
					s.append(GetInd(ind+1) + "}\n"); // \n needed! No not use NL
				} else {
					if (params.get(0).equals("false")) {
						s.append(GetInd(ind+1) + "if (!isNaN(parseFloat(" + extra + var.JSName +"[0])) && isFinite(" + extra + var.JSName +"[0])) {" + NL);
						s.append(GetInd(ind+2) + extra + var.JSName + ".sort(function(a, b){return b - a});" + NL);
						s.append(GetInd(ind+1) + "} else {" + NL);
						s.append(GetInd(ind+2) + extra + var.JSName + ".sort(function(a, b){if (a.toUpperCase()>b.toUpperCase()) {return -1;} else {if (a.toUpperCase()<b.toUpperCase()) {return 1;} else {return 0;}}});" + NL);
						s.append(GetInd(ind+1) + "}\n"); // \n needed! No not use NL
					} else {
						s.append("if (" + params.get(0) + "==true) {" + NL);
						s.append(GetInd(ind+1) + "if (!isNaN(parseFloat(" + extra + var.JSName +"[0])) && isFinite(" + extra + var.JSName +"[0])) {" + NL);
						s.append(GetInd(ind+2) + extra + var.JSName + ".sort(function(a, b){return a - b});" + NL);
						s.append(GetInd(ind+1) + "} else {" + NL);
						s.append(GetInd(ind+2) + extra + var.JSName + ".sort(function (a, b) {return a.toUpperCase().localeCompare(b.toUpperCase());});" + NL);
						s.append(GetInd(ind+1) + "}" + NL);
						s.append(GetInd(ind) + "} else {" + NL);
						s.append(GetInd(ind+1) + "if (!isNaN(parseFloat(" + extra + var.JSName +"[0])) && isFinite(" + extra + var.JSName +"[0])) {" + NL);
						s.append(GetInd(ind+2) + extra + var.JSName + ".sort(function(a, b){return b - a});" + NL);
						s.append(GetInd(ind+1) + "} else {" + NL);
						s.append(GetInd(ind+2) + extra + var.JSName + ".sort(function(a, b){if (a.toUpperCase()>b.toUpperCase()) {return -1;} else {if (a.toUpperCase()<b.toUpperCase()) {return 1;} else {return 0;}}});" + NL);
						s.append(GetInd(ind+1) + "}" + NL);
						s.append(GetInd(ind) + "}\n"); // \n needed! No not use NL
					}
				}
				next = NextToken(); // )
				return s.toString();
			case "sorttype":
				next = NextToken();
				HasError=true;
				if (!isGlobal) {
					BA.LogError("ERROR: [" + ClassName + "," + fullMethodName + ", line: " + currentLine.LineNumber + "] SortType is not supported in B4JS!");
				} 
				index = currentLine.Words.size();
				return "";
			case "sorttypecaseinsensitive":
				HasError=true;
				if (!isGlobal) {
					BA.LogError("ERROR: [" + ClassName + "," + fullMethodName + ", line: " + currentLine.LineNumber + "] SortTypeCaseInsensitive is not supported in B4JS!");
				} 
				index = currentLine.Words.size();
				return "";
			case "=":
			case "":
				return extra + var.JSName;
			default:
				next = NextToken();
				break;
			}
			next = PeekToken(index);			
		}
		
	}
	
	protected String ProcessMap(B4JSExtractVariable var, boolean isCondition) {
		String extra = "self.";
		if (var.Scope.equals(SCOPE.PRIVATE)) {
			extra = "";
		}
		StringBuilder s = new StringBuilder();
		String next = PeekToken(index);
		
		List<String> params;
		
		while (true) {
			if (DebugTrack) {
				BA.Log("ProcessMap: " + next);
			}
			switch (next) {
			case ".":
				next = NextToken();
				break;
			case "initialize":
				next = NextToken();
				return "";
			case "clear":
				next = NextToken(); // clear
				s.append(extra + var.JSName + "={}");
				return s.toString();
			case "containskey":
				next = NextToken(); // containskey
		
				params = GetParams();
				s.append("(" + params.get(0) + " in " + extra + var.JSName + ")");
				next = NextToken(); // )
				return s.toString();
			case "get":
				next = NextToken(); // get
		
				params = GetParams();
				s.append(extra + var.JSName + "[" + params.get(0) + "]");
				next = NextToken(); // )
				return s.toString();		
			case "getdefault":
				next = NextToken(); // get
		
				params = GetParams();
				s.append("(" + extra + var.JSName + "[" + params.get(0) + "] || " + params.get(1) + ")");
				next = NextToken(); // )
				return s.toString();
			case "getkeyat":
				next = NextToken(); // get
		
				params = GetParams();
				s.append("b4js_getB4JKeyAt(" + extra + var.JSName + "," + params.get(0) + ")");
				next = NextToken(); // )
				return s.toString();
				
			case "getvalueat":
				next = NextToken(); // get
		
				params = GetParams();
				s.append("b4js_getB4JValueAt(" + extra + var.JSName + "," + params.get(0) + ")");
				next = NextToken(); // )
				return s.toString();
				
			case "isinitialized":
				next = NextToken(); // isinitialized
				return "true";
			case "remove":
				next = NextToken(); // removeat

				s.append("delete " + extra + var.JSName + "[" + Evaluate("(", Arrays.asList(new String[] {")"}), isCondition) + "]");	
				next = NextToken(); // )
				return s.toString();
			case "size":
				next = NextToken(); // size
				s.append("$.map(" + extra + var.JSName + ", function(n, i) { return i; }).length");
				return s.toString();
			case "values":
				next = NextToken(); // values
				var.IsValues = true;
				return extra + var.JSName;					
			case "keys":
				next = NextToken(); // keys
				return extra + var.JSName;	
			case "put":
				next = NextToken(); // put
				params = GetParams();
				s.append(extra + var.JSName + "[" + params.get(0) + "]=" + params.get(1));	
				next = NextToken(); // )
				return s.toString().trim();
			case "=":
			case "":
				return extra + var.JSName;
			default:
				next = NextToken();
				break;
			}
			next = PeekToken(index);			
		}
	}
	
	protected String Evaluate(String fromToken, List<String> toTokens, boolean isCondition) {
		return EvaluateExtra(fromToken, toTokens, isCondition, false);
	}
	
	protected String EvaluateExtra(String fromToken, List<String> toTokens, boolean isCondition, boolean showExtraDebug) {
		StringBuilder evaluation = new StringBuilder();
		String next = PeekToken(index);
		
		int tokenCounter = 0;
		B4JSExtractVariable tmpVar = null;
		List<String> params;
		String tmpEvaluate = "";
		
		// little hack to avoid it triggering nothing
		if (fromToken.equals("")) {
			fromToken = "$µù^¨*£%$";
		}
		if (toTokens.isEmpty()) {
			toTokens = Arrays.asList(new String[] {"$µù^¨*£%$"});
		}
		while (true) {
			if (showExtraDebug) {
				BA.Log("----------------------------------------------");
				BA.Log("    Next: " + next);
				BA.Log("        Current output: " + evaluation.toString());
				BA.Log("        tokenCounter: " + tokenCounter);
			}
			if (next.equals(fromToken)) {
				next = NextToken();
				if (tokenCounter>0) {
					evaluation.append(next);
				}
				next = PeekToken(index);
				tokenCounter++;				
			}
			if (showExtraDebug) {
				BA.Log("    After fromToken");
				BA.Log("        Next: " + next);
				BA.Log("        tokenCounter: " + tokenCounter);
			}
			for (int i=0;i<toTokens.size();i++) {
				if (next.equals(toTokens.get(i))) {
					tokenCounter--;
					if (tokenCounter<=0) {	
						if (showExtraDebug) {
							BA.Log("    Unequal tokenCounter");
							BA.Log("        Next: " + next);
							BA.Log("        tokenCounter: " + tokenCounter);
						}
						return evaluation.toString();					
					} 
				}
			}
			if (showExtraDebug) {
				BA.Log("    After toTokens");
				BA.Log("        Next: " + next);
				BA.Log("        tokenCounter: " + tokenCounter);
			}
			switch (next) {
			case "":
				return evaluation.toString();		
			case "=":
				next = NextToken();
				if (isCondition) {
					evaluation.append(" == ");
				} else {
					evaluation.append(" = ");
					isCondition = true;
				}	
				break;			
			case "array":
				next = NextToken();
				next = PeekToken(index);
				if (next.equals("as")) {
					next = NextToken(); // as
					next = NextToken(); // type
				}				
				evaluation.append("[");
				evaluation.append(Evaluate("(", Arrays.asList(new String[] {")"}), false));
				evaluation.append("]");
				next = NextToken(); // )
				break;
			case "and":
				next = NextToken();
				evaluation.append(" && ");
				break;
			case "or":
				next = NextToken();
				evaluation.append(" || ");
				break;
			case "mod":
				next = NextToken();
				evaluation.append(" % ");
				break;
			case "return":
				next = NextToken();
				evaluation.append(" return ");			
				break;
			case "rnd":
				next = NextToken(); // rnd
				params = GetParams();
				evaluation.append("(Math.floor(Math.random() * (" + params.get(1) + " - " + params.get(0) + ") + " + params.get(0) + "))");
				next = NextToken(); // )
				break;
			case "abs":
				next = NextToken(); // abs
				evaluation.append("(Math.abs(" + Evaluate("(", Arrays.asList(new String[] {")"}), false)  + "))");
				next = NextToken(); // )
				break;
			case "not":
				next = NextToken(); // not
				next = NextToken(); // (	
				evaluation.append("!(" + Evaluate("", Arrays.asList(new String[] {")"}), true)  + ")"); // was false
				next = NextToken(); // )
				break;			
			case "exp":
				HasError=true;
				if (!isGlobal) {
					BA.LogError("ERROR: [" + ClassName + "," + fullMethodName + ", line: " + currentLine.LineNumber + "] B4JS does not support ^! Use Power instead.");
				} 
				index = currentLine.Words.size();
				break;
			case "cosd":
				next = NextToken(); // cosd
				evaluation.append("(Math.cos((" + Evaluate("(", Arrays.asList(new String[] {")"}), false)  + ")/(180/Math.PI)))");
				next = NextToken(); // )
				break;				
			case "cos":
				next = NextToken(); // cos
				evaluation.append("(Math.cos(" + Evaluate("(", Arrays.asList(new String[] {")"}), false)  + "))");
				next = NextToken(); // )
				break;
			case "sind":
				next = NextToken(); // sind
				evaluation.append("(Math.sin((" + Evaluate("(", Arrays.asList(new String[] {")"}), false)  + ")/(180/Math.PI)))");
				next = NextToken(); // )
				break;				
			case "sin":
				next = NextToken(); // sin
				evaluation.append("(Math.sin(" + Evaluate("(", Arrays.asList(new String[] {")"}), false)  + "))");
				next = NextToken(); // )
				break;
			case "acosd":
				next = NextToken(); // acosd
				evaluation.append("(Math.acos(" + Evaluate("(", Arrays.asList(new String[] {")"}), false)  + ")*(180/Math.PI))");
				next = NextToken(); // )
				break;				
			case "acos":
				next = NextToken(); // acos
				evaluation.append("(Math.acos(" + Evaluate("(", Arrays.asList(new String[] {")"}), false)  + "))");
				next = NextToken(); // )
				break;
			case "chr":
				next = NextToken(); // chr
				evaluation.append("String.fromCharCode(" + Evaluate("(", Arrays.asList(new String[] {")"}), false)  + ")");
				next = NextToken(); // )
				break;
			case "asc":
				next = NextToken(); // asc
				evaluation.append(Evaluate("(", Arrays.asList(new String[] {")"}), false) + ".charCodeAt(0)");
				next = NextToken(); // )
				break;
			case "asind":
				next = NextToken(); // asind
				evaluation.append("(Math.asin(" + Evaluate("(", Arrays.asList(new String[] {")"}), false)  + ")*(180/Math.PI))");
				next = NextToken(); // )
				break;				
			case "asin":
				next = NextToken(); // asin
				evaluation.append("(Math.asin(" + Evaluate("(", Arrays.asList(new String[] {")"}), false)  + "))");
				next = NextToken(); // )
				break;
			case "atand":
				next = NextToken(); // atand
				evaluation.append("(Math.atan(" + Evaluate("(", Arrays.asList(new String[] {")"}), false)  + ")*(180/Math.PI))");
				next = NextToken(); // )
				break;				
			case "atan":
				next = NextToken(); // atan
				evaluation.append("(Math.atan(" + Evaluate("(", Arrays.asList(new String[] {")"}), false)  + "))");
				next = NextToken(); // )
				break;
			case "atan2d":
				next = NextToken(); // atan2d
				params = GetParams();
				evaluation.append("(Math.atan2(" + params.get(0) + "," + params.get(1)  + ")*(180/Math.PI))");
				next = NextToken(); // )
				break;				
			case "atan2":
				next = NextToken(); // atan2
				params = GetParams();
				evaluation.append("(Math.atan2(" + params.get(0) + "," + params.get(1)  + "))");
				next = NextToken(); // )
				break;			
			case "lastexception":
				next = NextToken(); // lastexception			
				evaluation.append("err.message");	
				next = PeekToken(index);
				if (next.equals(".")) {
					next = NextToken(); // .
					next = NextToken(); // message
				}
				break;
			case "ce":
				next = NextToken(); // cE			
				evaluation.append("2.7182818284590452353602874713527");				
				break;				
			case "ceil":
				next = NextToken(); // ceil
				evaluation.append("(Math.ceil(" + Evaluate("(", Arrays.asList(new String[] {")"}), false)  + "))");
				next = NextToken(); // )
				break;
			case "continue":
				next = NextToken(); // continue			
				evaluation.append("continue");				
				break;
			case "cpi":
				next = NextToken(); // cPI			
				evaluation.append("Math.PI");				
				break;
			case "crlf":
				next = NextToken();
				evaluation.append("\"\\n\"");
				break;
			case "exit":
				next = NextToken(); // exit			
				evaluation.append("break");				
				break;
			case "floor":
				next = NextToken(); // floor
				evaluation.append("(Math.floor(" + Evaluate("(", Arrays.asList(new String[] {")"}), false)  + "))");
				next = NextToken(); // )
				break;
			case "isnumber":
				next = NextToken(); // isnumber
				tmpEvaluate = Evaluate("(", Arrays.asList(new String[] {")"}), false);
				evaluation.append("(!isNaN(parseFloat(" + tmpEvaluate + ")) && isFinite(" + tmpEvaluate + "))");
				next = NextToken(); // )
				break;
			case "logdebug":
			case "log":
				next = NextToken(); // log
				next = NextToken(); // (
				tmpEvaluate = Evaluate("(", Arrays.asList(new String[] {")"}), true);
				if (!NL.equals("")) {
					evaluation.append("console.log(" + tmpEvaluate + ")"); // ???????????? Why not? + ")");
				}
				next = NextToken(); // )
				break;
			case "logarithm":
				next = NextToken(); // logarithm
				params = GetParams();
				evaluation.append("(Math.log(" + params.get(0) + ")/Math.log(" + params.get(1) + "))");
				next = NextToken(); // )
				break;		
			case "max":
				next = NextToken(); // max
				params = GetParams();
				evaluation.append("Math.max(" + params.get(0) + "," + params.get(1) + ")");
				next = NextToken(); // )
				break;	
			case "min":
				next = NextToken(); // max
				params = GetParams();
				evaluation.append("Math.min(" + params.get(0) + "," + params.get(1) + ")");
				next = NextToken(); // )
				break;
			case "null":
				next = NextToken(); // null
				evaluation.append("null");				
				break;	
			case "power":
				next = NextToken(); // pow
				params = GetParams();
				evaluation.append("Math.pow(" + params.get(0) + "," + params.get(1) + ")");
				next = NextToken(); // )
				break;
			case "quote":
				HasError=true;
				if (!isGlobal) {
					BA.LogError("ERROR: [" + ClassName + "," + fullMethodName + ", line: " + currentLine.LineNumber + "] QUOTE is not supported in B4JS!");
				} 
				index = currentLine.Words.size();
				break;
			case "rndseed":
				next = NextToken(); // rndseed							
				break;
			case "round":
				next = NextToken(); // round
				evaluation.append("Math.round(" + Evaluate("(", Arrays.asList(new String[] {")"}), false) + ")");
				next = NextToken(); // )
				break;
			case "round2":
				next = NextToken(); // round
				params = GetParams();
				evaluation.append("(+(Math.round(" + params.get(0) + "+\"e+\"+" + params.get(1) + ")+\"e-\"+" + params.get(1) + "))");
				next = NextToken(); // )
				break;
			case "sqrt":
				next = NextToken(); // sqrt
				evaluation.append("(Math.sqrt(" + Evaluate("(", Arrays.asList(new String[] {")"}), false)  + "))");
				next = NextToken(); // )
				break;
			case "tab":
				HasError=true;
				if (!isGlobal) {
					BA.LogError("ERROR: [" + ClassName + "," + fullMethodName + ", line: " + currentLine.LineNumber + "] TAB is not supported in B4JS!");
				}
				index = currentLine.Words.size();
				break;
			case "tand":
				next = NextToken(); // tand
				evaluation.append("(Math.tan((" + Evaluate("(", Arrays.asList(new String[] {")"}), false)  + ")/(180/Math.PI)))");
				next = NextToken(); // )
				break;				
			case "tan":
				next = NextToken(); // tan
				evaluation.append("(Math.tan(" + Evaluate("(", Arrays.asList(new String[] {")"}), false)  + "))");
				next = NextToken(); // )
				break;			
			case "datetime":
				next = NextToken(); // datetime
				next = NextToken(); // .
				next = NextToken();
				switch (next){
				case "now":
					evaluation.append("DateTime.Now()");
					break;
				case "dateparse":
					next = NextToken();
					evaluation.append("DateTime.DateParse(" + Evaluate("(", Arrays.asList(new String[] {")"}), false) + ")");
					next = NextToken(); // )
					break;
				case "timeparse":
					next = NextToken();
					evaluation.append("DateTime.TimeParse(" + Evaluate("(", Arrays.asList(new String[] {")"}), false) + ")");
					next = NextToken(); // )
					break;
				case "date":
					next = NextToken();
					evaluation.append("DateTime.Date(" + Evaluate("(", Arrays.asList(new String[] {")"}), false) + ")");
					next = NextToken(); // )
					break;
				case "time":
					next = NextToken();
					evaluation.append("DateTime.Time(" + Evaluate("(", Arrays.asList(new String[] {")"}), false) + ")");
					next = NextToken(); // )
					break;
				case "dateformat":
					next = NextToken();
					if (isCondition) {
						evaluation.append("DateTime.GetDateFormat()");
					} else {
						evaluation.append("DateTime.SetDateFormat(" + Evaluate("(", Arrays.asList(new String[] {")"}), false) + ")");
					}	
					
					break;
				case "timeformat":
					next = NextToken();
					if (isCondition) {
						evaluation.append("DateTime.GetTimeFormat()");
					} else {
						evaluation.append("DateTime.SetTimeFormat(" + Evaluate("(", Arrays.asList(new String[] {")"}), false) + ")");
					}
					break;	
				case "add":
					next = NextToken();
					evaluation.append("DateTime.Add(" + Evaluate("(", Arrays.asList(new String[] {")"}), false) + ")");
					next = NextToken(); // )					
					break;
				case "getyear":
					next = NextToken();
					evaluation.append("DateTime.GetYear(" + Evaluate("(", Arrays.asList(new String[] {")"}), false) + ")");
					next = NextToken(); // )					
					break;
				case "getmonth":
					next = NextToken();
					evaluation.append("DateTime.GetMonth(" + Evaluate("(", Arrays.asList(new String[] {")"}), false) + ")");
					next = NextToken(); // )					
					break;
				case "getdayofmonth":
					next = NextToken();
					evaluation.append("DateTime.GetDayOfMonth(" + Evaluate("(", Arrays.asList(new String[] {")"}), false) + ")");
					next = NextToken(); // )					
					break;
				case "getdayofyear":
					next = NextToken();
					evaluation.append("DateTime.GetDayOfYear(" + Evaluate("(", Arrays.asList(new String[] {")"}), false) + ")");
					next = NextToken(); // )					
					break;
				case "getdayofweek":
					next = NextToken();
					evaluation.append("DateTime.GetDayOfWeek(" + Evaluate("(", Arrays.asList(new String[] {")"}), false) + ")");
					next = NextToken(); // )					
					break;
				case "gethour":
					next = NextToken();
					evaluation.append("DateTime.GetHour(" + Evaluate("(", Arrays.asList(new String[] {")"}), false) + ")");
					next = NextToken(); // )					
					break;
				case "getminute":
					next = NextToken();
					evaluation.append("DateTime.GetMinute(" + Evaluate("(", Arrays.asList(new String[] {")"}), false) + ")");
					next = NextToken(); // )					
					break;
				case "getsecond":
					next = NextToken();
					evaluation.append("DateTime.GetSecond(" + Evaluate("(", Arrays.asList(new String[] {")"}), false) + ")");
					next = NextToken(); // )					
					break;
				case "gettimezoneoffsetat":
					next = NextToken();
					evaluation.append("DateTime.GetTimeZoneOffsetAt(" + Evaluate("(", Arrays.asList(new String[] {")"}), false) + ")");
					next = NextToken(); // )					
					break;
				case "ticksperday":
					next = NextToken();
					evaluation.append("DateTime.TicksPerDay()");
					break;
				case "ticksperhour":
					next = NextToken();
					evaluation.append("DateTime.TicksPerHour()");
					break;
				case "ticksperminute":
					next = NextToken();
					evaluation.append("DateTime.TicksPerMinute()");
					break;
				case "tickspersecond":
					next = NextToken();
					evaluation.append("DateTime.TicksPerSecond()");
					break;
				case "timezoneoffset":
					next = NextToken();
					evaluation.append("DateTime.TimeZoneOffset()");
					next = NextToken(); // )					
					break;	
				case "settimezone":
					next = NextToken();
					evaluation.append("DateTime.SetTimeZone(" + Evaluate("(", Arrays.asList(new String[] {")"}), false) + ")");
					next = NextToken(); // )					
					break;	
				default:
					HasError=true;
					if (!isGlobal) {
						BA.LogError("ERROR: [" + ClassName + "," + fullMethodName + ", line: " + currentLine.LineNumber + "] datetime." + next + " is not supported in B4JS!");
					}
					index = currentLine.Words.size();
					break;
				}
				break;			
			case "bit":
				next = NextToken(); // bit
				next = NextToken(); // .
				next = NextToken();
				switch (next){
				case "and":
					next = NextToken();
					params = GetParams();
					evaluation.append("(" + params.get(0) + "&" + params.get(1) + ")");
					next = NextToken(); // )
					break;					
				case "not":
					next = NextToken();
					evaluation.append("~" + Evaluate("(", Arrays.asList(new String[] {")"}), false));
					next = NextToken(); // )
					break;	
				case "or":
					next = NextToken();
					params = GetParams();
					evaluation.append("(" + params.get(0) + "|" + params.get(1) + ")");
					next = NextToken(); // )
					break;
				case "xor":
					next = NextToken();
					params = GetParams();
					evaluation.append("(" + params.get(0) + "^" + params.get(1) + ")");
					next = NextToken(); // )
					break;
				case "shiftleft":
					next = NextToken();
					params = GetParams();
					evaluation.append("(" + params.get(0) + "<<" + params.get(1) + ")");
					next = NextToken(); // )
					break;
				case "shiftright":
					next = NextToken();
					params = GetParams();
					evaluation.append("(" + params.get(0) + ">>" + params.get(1) + ")");
					next = NextToken(); // )
					break;
				case "unsignedshiftright":
					next = NextToken();
					params = GetParams();
					evaluation.append("(" + params.get(0) + ">>>" + params.get(1) + ")");
					next = NextToken(); // )
					break;
				case "tohexstring":
					next = NextToken();
					params = GetParams();
					evaluation.append(params.get(0) + ".toString(16)");
					next = NextToken(); // )
					break;	
				case "tooctalstring":
					next = NextToken();
					params = GetParams();
					evaluation.append(params.get(0) + ".toString(8)");
					next = NextToken(); // )
					break;
				case "tobinarystring":
					next = NextToken();
					params = GetParams();
					evaluation.append(params.get(0) + ".toString(2)");
					next = NextToken(); // )
					break;
				case "parseint":
					next = NextToken();
					params = GetParams();
					evaluation.append("parseInt(" + params.get(0) + "," + params.get(1) + ")");
					next = NextToken(); // )
					break;
				default:
					HasError=true;
					if (!isGlobal) {
						BA.LogError("ERROR: [" + ClassName + "," + fullMethodName + ", line: " + currentLine.LineNumber + "] bit." + next + " is not supported in B4JS!");
					}
					index = currentLine.Words.size();
					break;
				}
				break;
			// String
			case "charat":
				next = NextToken();
				params = GetParams();
				evaluation.append("charAt(" + params.get(0) + ")");
				next = NextToken(); // )
				break;		
			case "compareto":
				next = NextToken();
				params = GetParams();
				evaluation.append("localeCompare(" + params.get(0) + ")");
				next = NextToken(); // )
				break;	
			case "contains":
				next = NextToken();
				params = GetParams();
				evaluation.append("contains(" + params.get(0) + ")");
				next = NextToken(); // )
				break;	
			case "endswith":
				next = NextToken();
				params = GetParams();
				evaluation.append("endsWith(" + params.get(0) + ")");
				next = NextToken(); // )
				break;	
			case "equalsignorecase":
				next = NextToken();
				params = GetParams();
				evaluation.append("equalsIgnoreCase(" + params.get(0) + ")");
				next = NextToken(); // )
				break;	
			case "indexof":
				next = NextToken();
				params = GetParams();
				evaluation.append("indexOf(" + params.get(0) + ")");
				next = NextToken(); // )
				break;	
			case "indexof2":
				next = NextToken();
				params = GetParams();
				evaluation.append("indexOf(" + params.get(0) + "," + params.get(1) + ")");
				next = NextToken(); // )
				break;	
			case "lastindexof":
				next = NextToken();
				params = GetParams();
				evaluation.append("lastIndexOf(" + params.get(0) + ")");
				next = NextToken(); // )
				break;	
			case "lastindexof2":
				next = NextToken();
				params = GetParams();
				evaluation.append("lastIndexOf(" + params.get(0) + "," + params.get(1) + ")");
				next = NextToken(); // )
				break;	
			case "length":
				next = NextToken();				
				evaluation.append("length");				
				break;	
			case "replace":
				next = NextToken();
				params = GetParams();
				evaluation.append("split(" + params.get(0) + ").join(" + params.get(1) + ")");
				next = NextToken(); // )
				break;	
			case "startswith":
				next = NextToken();
				params = GetParams();
				evaluation.append("startsWith(" + params.get(0) + ")");
				next = NextToken(); // )
				break;	
			case "substring":
				next = NextToken();
				params = GetParams();
				evaluation.append("substring(" + params.get(0) + ")");
				next = NextToken(); // )
				break;	
			case "substring2":
				next = NextToken();
				params = GetParams();
				evaluation.append("substring(" + params.get(0) + "," + params.get(1) + ")");
				next = NextToken(); // )
				break;	
			case "tolowercase":
				next = NextToken();
				evaluation.append("toLowerCase()");
				break;	
			case "touppercase":
				next = NextToken();
				evaluation.append("toUpperCase()");
				break;	
			case "trim":
				next = NextToken();			
				evaluation.append("trim()");			
				break;
			case "getbytes":
				next = NextToken();
				params = GetParams();
				evaluation.append("getBytes()");
				next = NextToken(); // )
				break;
			case "bytestostring":
				//NeedsBytesToString=true;
				next = NextToken();
				params = GetParams();
				evaluation.append("b4js_bytesToString(" + params.get(0) + ".slice(" + params.get(1) + "," + params.get(2) + "))");
				next = NextToken(); // )
				break;
			case "abm":
				next = NextToken(); // )				
				next = NextToken(); // .				
				next = NextToken();
				evaluation.append(ResolveConst(next));
				
				break;
				
			// unsupported
			case "callsub":
			case "callsub2":
			case "callsub3":
			case "callsubdelayed":
			case "callsubdelayed2":
			case "callsubdelayed3":
			case "createmap":
			case "charstostring":	
			case "density":
			case "diptocurrent":
			case "exitapplication":
			case "exitapplication2":
			case "file":
			case "getenvironmentvariable":
			case "getsystemproperty":
			case "gettype":
			case "isdevtool":	
			case "me":
			case "numberformat":
			case "numberformat2":
			case "regex":
			case "sender":
			case "setsystemproperty":
			case "smartstringformatter":
			case "stopmessageloop":
			case "startmessageloop":
			case "subexists":
			case "wait":
			case "sleep":
			case "type":			
				HasError=true;
				if (!isGlobal) {
					BA.LogError("ERROR: [" + ClassName + "," + fullMethodName + ", line: " + currentLine.LineNumber + "] " + next + " is not supported in B4JS!");
				}
				index = currentLine.Words.size();
				break;
			case "is":
				next = NextToken();
				evaluation.append(" == ");		
				break;			
			default:
				next = NextToken();
				if (DebugTrack) {
					BA.Log("Evaluate default: " + next);
				}
				if (next.startsWith("{")) {
					String smartString = GetString(next);
					if (smartString.contains("{~")) {
						for (Entry<String,String>entry: GlobalSmartStrings.entrySet()) {
							if (smartString.indexOf(entry.getKey())>-1) {
								CharSequence cs1 = entry.getKey();						
								
								B4JSExtractVariable smartVar = Vars.getOrDefault(entry.getValue().toLowerCase(), null);
								if (smartVar!=null) {
									CharSequence cs2;								
									if (smartVar.Scope == SCOPE.PRIVATE) {
										cs2 = "_" + entry.getValue();
									} else {
										cs2 = "self._" + entry.getValue();
									}
									smartString = smartString.replace(cs1, cs2);
								} else {
									CharSequence cs2 = entry.getValue();
									smartString = smartString.replace(cs1, cs2);
								}
								
							}
						}
						
					}
					evaluation.append(smartString.replace("~BR~", "\\n"));					
				} else {
					tmpVar = Vars.getOrDefault(next, null);
					if (tmpVar==null) {
						B4JSExtractResultMethod meth = GetMethod(next);
						if (meth==null) {
							if (DebugTrack) {
								BA.Log("Evaluate appending: " + next);
							}
							evaluation.append(next);							
						} else {
							evaluation.append("self." + next);
							next = PeekToken(index);
							if (next.equals("(")) {
								evaluation.append("(");
								evaluation.append(Evaluate("(", Arrays.asList(new String[] {")"}), true));
								next = NextToken(); // )
								evaluation.append(")");
							} else {
								evaluation.append("()");
							}
						}						
						
					} else {
						if (tmpVar.ValidType) {
							evaluation.append(ProcessVariable(tmpVar, isCondition));
						} else {							
							HasError=true;
							if (!isGlobal) {
								BA.LogError("ERROR: [" + ClassName + "," + fullMethodName + ", line: " + currentLine.LineNumber + "] You are using " + tmpVar.Name + " which is not a B4JS variable!");
							}
							index = currentLine.Words.size();
							break;
							
						}
						
					}
				}
			}
			next = PeekToken(index);
		}
	}
	
}
