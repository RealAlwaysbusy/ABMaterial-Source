package com.ab.abmaterial;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.ab.abmaterial.ABMaterial.SCOPE;
import com.ab.b4js.B4JS;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.keywords.Regex;

@Hide
public class ExtractResultModule {

	protected Map<String,ExtractResultMethod> Methods = new LinkedHashMap<String,ExtractResultMethod>();
	protected Map<String,String> Translations = new LinkedHashMap<String,String>();
	protected Map<String,String> Defaults = new LinkedHashMap<String,String>();
	protected Map<String,String> Needs = new LinkedHashMap<String,String>();
	
	
	protected Map<String,Boolean> NeedsFromClass = new LinkedHashMap<String,Boolean>();
	
	protected String Type="";
	protected String Name="";
	protected Map<String,Boolean> AlreadyChecked = new LinkedHashMap<String,Boolean>();
	protected List<String> MarkedToDocument = new ArrayList<String>();
	protected String debugValue="";
	protected int counter = 0;
	protected Map<String,String> GlobalStrings = new LinkedHashMap<String,String>();
	protected List<ExtractLine> Lines = new ArrayList<ExtractLine>();
		
	protected String FinalJS="";
	protected String WriteDir="";
	
	private final static Pattern LTRIM = Pattern.compile("^\\s+");
	private final static Pattern RTRIM = Pattern.compile("\\s+$");
	
	public ExtractResultModule(String type, String name) {
		Type=type;
		Name = name;
	}
	
	protected String GetString(String value) {
		if (GlobalStrings.containsKey(value)) {
			return "\"" + GlobalStrings.get(value) + "\"";
		}
		return value;
	}	
	
	protected String ltrim(String s) {
	    return LTRIM.matcher(s).replaceAll("");
	}

	protected String rtrim(String s) {
	    return RTRIM.matcher(s).replaceAll("");
	}
	
	public void PrepareModule(File f, String dir, Map<String,File> OrderedFiles, ExtractResult res) {
		List<String> wrongTrans = new ArrayList<String>();
		List<String> wrongDefs = new ArrayList<String>();
						
		BA.Log(f.getName());
		debugValue = f.getName().toLowerCase();
		WriteDir = dir;		
		String logFileName = f.getName();
		try {
			res.analyseLog.write("Analysing: " + f.getName() + "\n");
		} catch (IOException e1) {
		}		
		
		BufferedReader br=null;
		try {
			String s;
			br = new BufferedReader(new FileReader(f));
			StringBuilder allS = new StringBuilder(); 
			while ((s = br.readLine()) != null) {
				String sEx = s.toLowerCase();
				if (sEx.replace(" ", "").contains("'gentodoc")) {
					int i = sEx.indexOf("'gentodoc");
					sEx = s.substring(0, i);
					this.MarkedToDocument.add(sEx);
				}				
				allS.append(s + " \n");
			}
			
			s = allS.toString();
						
			String sOrig=s;
			
			BufferedWriter writerTrans = null;
		    try {
		       	File buildFile = new File(dir, "BaseTranslations" + ".lng");
		       	
		       	writerTrans = new BufferedWriter(new FileWriter(buildFile, true));
		       	
		       	List<String> Translations=ExtractTranslations(sOrig);
		       	
		       	Map<String,String> DoubleTrans = new LinkedHashMap<String,String>();
		       	Map<String,String> WrongDoubleTrans = new LinkedHashMap<String,String>();
		       	for (int j=0;j<Translations.size();j++) {
					List<String> spl = Splitter(Translations.get(j));
					String vTrans = "";
					if (spl.size()==1) {
						String v1 = Name.toLowerCase() + "_" + spl.get(0).trim().replace("\"", "").substring(9).trim();
						vTrans = v1 + ";\n";
						if (WrongDoubleTrans.containsKey(v1)) {
							if (!WrongDoubleTrans.get(v1).equals("")) {
								wrongTrans.add(v1 + " used for both: '" + WrongDoubleTrans.get(v1) + "' AND ''");
							}
						} else {
							WrongDoubleTrans.put(v1, "");
						}
						
					} else {
						String v1 = Name.toLowerCase() + "_" + spl.get(0).trim().replace("\"", "").substring(9).trim();
						String v2=spl.get(1).trim().replace("\"", "");
						v2 = v2.substring(0, v2.length()-1).trim();
						if (res.OptimizeTrans.containsKey(Name.toLowerCase() + "_" + v2)) {
							if (!v1.equals(res.OptimizeTrans.get(Name.toLowerCase() + "_" + v2))) {
								res.oTranslations.add(v1 + " '" + v2 + "' -> " + res.OptimizeTrans.get(Name.toLowerCase() + "_" + v2) + "\n");
							}
						} else {
							res.OptimizeTrans.put(Name.toLowerCase() + "_" + v2, v1);
						}						
						vTrans = v1 + ";" + v2 + "\n";
						if (WrongDoubleTrans.containsKey(v1)) {
							if (!WrongDoubleTrans.get(v1).equals(v2)) {
								wrongTrans.add(v1 + " used for both: '" + WrongDoubleTrans.get(v1) + "' AND '" + v2 +"'");
							}
						} else {
							WrongDoubleTrans.put(v1, v2);
						}
					}
					
					if (!DoubleTrans.containsKey(vTrans)) {
						writerTrans.write(vTrans);
						DoubleTrans.put(vTrans, vTrans);
					}
				}
		    } catch (Exception e) {
		           e.printStackTrace();
		    } finally {
		           try {
		           		writerTrans.close();
		           } catch (Exception e) {
		           }
		    } 
		    
		    try {
		       	File buildFile = new File(dir, "BaseTranslations" + ".lng");
		       	
		       	writerTrans = new BufferedWriter(new FileWriter(buildFile, true));
		       	
		       	List<String> Translations=ExtractGlobalTranslations(sOrig);
		       	Map<String,String> DoubleTrans = new LinkedHashMap<String,String>();
		       	Map<String,String> WrongDoubleTrans = new LinkedHashMap<String,String>();
		       	for (int j=0;j<Translations.size();j++) {
					List<String> spl = Splitter(Translations.get(j));
					
					String vTrans = "";
					if (spl.size()==3) {
						String v1 = "GlobalTranslations_" + spl.get(1).trim().replace("\"", ""); //.substring(2).trim();
						vTrans = v1 + ";\n";
						if (WrongDoubleTrans.containsKey(v1)) {
							if (!WrongDoubleTrans.get(v1).equals("")) {
								wrongTrans.add(v1 + " used for both: '" + WrongDoubleTrans.get(v1) + "' AND ''");
							}
						} else {
							WrongDoubleTrans.put(v1, "");
						}
						
					} else {
						String v1 = "GlobalTranslations_" + spl.get(2).trim().replace("\"", ""); //.substring(2).trim();
						String v2=spl.get(3).trim().replace("\"", "");
						v2 = v2.substring(0, v2.length()-1).trim();
						if (res.OptimizeTrans.containsKey(Name.toLowerCase() + "_" + v2)) {
							if (!v1.equals(res.OptimizeTrans.get(Name.toLowerCase() + "_" + v2))) {
								res.oTranslations.add(v1 + " '" + v2 + "' -> " + res.OptimizeTrans.get(Name.toLowerCase() + "_" + v2) + "\n");
							}
						} else {
							res.OptimizeTrans.put(Name.toLowerCase() + "_" + v2, v1);
						}						
						vTrans = v1 + ";" + v2 + "\n";
						if (WrongDoubleTrans.containsKey(v1)) {
							if (!WrongDoubleTrans.get(v1).equals(v2)) {
								wrongTrans.add(v1 + " used for both: '" + WrongDoubleTrans.get(v1) + "' AND '" + v2 +"'");
							}
						} else {
							WrongDoubleTrans.put(v1, v2);
						}
					}
					
					if (!DoubleTrans.containsKey(vTrans)) {
						writerTrans.write(vTrans);
						DoubleTrans.put(vTrans, vTrans);
					}
				}
		    } catch (Exception e) {
		           e.printStackTrace();
		    } finally {
		           try {
		           		writerTrans.close();
		           } catch (Exception e) {
		           }
		    } 
		    
		        
		    BufferedWriter writerDefaults = null;
		    try {
		       	File buildFile = new File(dir, "BaseDefaults" + ".def");
		        	
		       	writerDefaults = new BufferedWriter(new FileWriter(buildFile, true));
		       	
		       	List<String> Defaults=ExtractDefaults(sOrig);
		       	
		       	Map<String,String> DoubleDefaults = new LinkedHashMap<String,String>();
		       	Map<String,String> WrongDoubleDefs = new LinkedHashMap<String,String>();
				for (int j=0;j<Defaults.size();j++) {
					List<String> spl = Splitter(Defaults.get(j));
					String vDef = "";
					if (spl.size()==1) {
						String v1 = Name.toLowerCase() + "_" + spl.get(0).trim().replace("\"", "").substring(9).trim();
						vDef = v1 + ";\n";
						if (WrongDoubleDefs.containsKey(v1)) {
							if (!WrongDoubleDefs.get(v1).equals("")) {
								wrongDefs.add(v1 + " used for both: '" + WrongDoubleDefs.get(v1) + "' AND ''");
							}
						} else {
							WrongDoubleDefs.put(v1, "");
						}
					} else {
						String v1 = Name.toLowerCase() + "_" + spl.get(0).trim().replace("\"", "").substring(9).trim();
						String v2=spl.get(1).trim().replace("\"", "");
						v2 = v2.substring(0, v2.length()-1).trim();
						if (res.OptimizeDefs.containsKey(Name.toLowerCase() + "_" + v2)) {
							if (!v1.equals(res.OptimizeDefs.get(Name.toLowerCase() + "_" + v2))) {
								res.oDefaults.add(v1 + " '" + v2 + "' -> " + res.OptimizeDefs.get(Name.toLowerCase() + "_" + v2) + "\n");
							}
						} else {
							res.OptimizeDefs.put(v2, v1);
						}
						vDef = v1 + ";" + v2 + "\n";
						if (WrongDoubleDefs.containsKey(v1)) {
							if (!WrongDoubleDefs.get(v1).equals(v2)) {
								wrongTrans.add(v1 + " used for both: '" + WrongDoubleDefs.get(v1) + "' AND '" + v2 +"'");
							}
						} else {
							WrongDoubleDefs.put(v1, v2);
						}
					}
					if (!DoubleDefaults.containsKey(vDef)) {
						writerDefaults.write(vDef);
						DoubleDefaults.put(vDef, vDef);
					}
				}
		    } catch (Exception e) {
		        e.printStackTrace();
		    } finally {
		        try {
		       		writerDefaults.close();
		        } catch (Exception e) {
		        }
		    } 	
		    
		    try {
		       	File buildFile = new File(dir, "BaseDefaults" + ".def");
		        	
		       	writerDefaults = new BufferedWriter(new FileWriter(buildFile, true));
		       	
		       	List<String> Defaults=ExtractGlobalDefaults(sOrig);
		       	Map<String,String> DoubleDefaults = new LinkedHashMap<String,String>();
		       	Map<String,String> WrongDoubleDefs = new LinkedHashMap<String,String>();
				for (int j=0;j<Defaults.size();j++) {
					List<String> spl = Splitter(Defaults.get(j));
					String vDef = "";
					if (spl.size()==3) {
						String v1 = "GlobalDefaults_" + spl.get(1).trim().replace("\"", ""); //.substring(2).trim();
						vDef = v1 + ";\n";
						if (WrongDoubleDefs.containsKey(v1)) {
							if (!WrongDoubleDefs.get(v1).equals("")) {
								wrongDefs.add(v1 + " used for both: '" + WrongDoubleDefs.get(v1) + "' AND ''");
							}
						} else {
							WrongDoubleDefs.put(v1, "");
						}
					} else {
						String v1 = "GlobalDefaults_" + spl.get(2).trim().replace("\"", ""); //.substring(2).trim();
						String v2=spl.get(3).trim().replace("\"", "");
						v2 = v2.substring(0, v2.length()-1).trim();
						if (res.OptimizeDefs.containsKey(Name.toLowerCase() + "_" + v2)) {
							if (!v1.equals(res.OptimizeDefs.get(Name.toLowerCase() + "_" + v2))) {
								res.oDefaults.add(v1 + " '" + v2 + "' -> " + res.OptimizeDefs.get(Name.toLowerCase() + "_" + v2) + "\n");
							}
						} else {
							res.OptimizeDefs.put(v2, v1);
						}
						vDef = v1 + ";" + v2 + "\n";
						if (WrongDoubleDefs.containsKey(v1)) {
							if (!WrongDoubleDefs.get(v1).equals(v2)) {
								wrongTrans.add(v1 + " used for both: '" + WrongDoubleDefs.get(v1) + "' AND '" + v2 +"'");
							}
						} else {
							WrongDoubleDefs.put(v1, v2);
						}
					}
					if (!DoubleDefaults.containsKey(vDef)) {
						writerDefaults.write(vDef);
						DoubleDefaults.put(vDef, vDef);
					}
				}
		    } catch (Exception e) {
		        e.printStackTrace();
		    } finally {
		        try {
		       		writerDefaults.close();
		        } catch (Exception e) {
		        }
		    } 	
		    
		    if (wrongTrans.size()>0) {
		    	res.wrongTranslations.put(Name.toLowerCase(), wrongTrans);
		    }
		    if (wrongDefs.size()>0) {
		    	res.wrongDefs.put(Name.toLowerCase(), wrongDefs);
		    }			        
		    
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
		    
		    s = Phase2(Phase1(s, "$\"", "\"$"),"\"", "\"");
		    
		    String iconName="";
		    int iconI=0;
		    for (Entry<String,String> entry: GlobalStrings.entrySet()) {
		    	String iconS = entry.getValue();
		    	while (iconS.indexOf("  ")>-1) {
	    			iconS = iconS.replace("  ", " ");
				}
		    	iconS = iconS.replace("{", " ");
		    	iconI = iconS.indexOf("mdi-");
		    	if (iconI>-1) {
		    		iconName = iconS.substring(iconI).trim();
		    		iconI = iconName.indexOf(" ");
	    			if (iconI>-1) {
	    				iconName = iconName.substring(0, iconI);	    				
	    			}
	    			ABMaterial.NeededIcons.put(iconName, true);
		    	} else {
		    		iconI = iconS.indexOf("fa fa-");
		    		if (iconI>-1) {
		    			ABMaterial.NeedsAwesomeFont = true;
		    			iconName = iconS.substring(iconI+3).trim();
		    			iconI = iconName.indexOf(" ");
		    			if (iconI>-1) {
		    				iconName = iconName.substring(0, iconI);
		    			}
		    			ABMaterial.NeededIcons.put(iconName, true);
		    		}
		    	}
		    }
		    		
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
				ExtractLine eLine = new ExtractLine();				
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
			
			ExtractMethods(OrderedFiles, res, logFileName);		
				
			
			
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
		
	protected List<String> ExtractTranslations(String s) {
		List<String> matchList = new ArrayList<String>();
		String first="page.xtr(";
		String last="\")";
		s.replaceAll("\"\\s+\\)", "\")");
		String sLower = s.toLowerCase();
		int begin=sLower.indexOf(first);
		int stop=sLower.indexOf(last, begin+9);
		int len = last.length();
		
		while (begin>-1 && stop>-1) {
			matchList.add(s.substring(begin,stop+len));
			s = s.substring(0,begin) + s.substring(stop+len);
			sLower = sLower.substring(0,begin) + sLower.substring(stop+len);
			begin=sLower.indexOf(first);
			stop=sLower.indexOf(last, begin+9);
		}
		return matchList;
	}
	
	protected List<String> ExtractGlobalTranslations(String s) {
		List<String> matchList = new ArrayList<String>();
		String first="abm.xtr(";
		String last="\")";
		s.replaceAll("\"\\s+\\)", "\")");
		String sLower = s.toLowerCase();
		int begin=sLower.indexOf(first);
		int stop=sLower.indexOf(last, begin+9);
		int len = last.length();
		
		while (begin>-1 && stop>-1) {
			matchList.add(s.substring(begin,stop+len));
			s = s.substring(0,begin) + s.substring(stop+len);
			sLower = sLower.substring(0,begin) + sLower.substring(stop+len);
			begin=sLower.indexOf(first);
			stop=sLower.indexOf(last, begin+9);
		}
		return matchList;
	}
	
	protected List<String> ExtractDefaults(String s) {
		List<String> matchList = new ArrayList<String>();
		String first="page.xdf(";
		String last="\")";
		s.replaceAll("\"\\s+\\)", "\")");
		String sLower = s.toLowerCase();
		int begin=sLower.indexOf(first);
		int stop=sLower.indexOf(last, begin+9);
		int len = last.length();
		
		while (begin>-1 && stop>-1) {
			matchList.add(s.substring(begin,stop+len));
			s = s.substring(0,begin) + s.substring(stop+len);
			sLower = sLower.substring(0,begin) + sLower.substring(stop+len);
			begin=sLower.indexOf(first);
			stop=sLower.indexOf(last, begin+9);
		}
		return matchList;
	}
	
	protected List<String> ExtractGlobalDefaults(String s) {
		List<String> matchList = new ArrayList<String>();
		String first="abm.xdf(";
		String last="\")";
		s.replaceAll("\"\\s+\\)", "\")");
		String sLower = s.toLowerCase();
		int begin=sLower.indexOf(first);
		int stop=sLower.indexOf(last, begin+9);
		int len = last.length();
		
		while (begin>-1 && stop>-1) {
			matchList.add(s.substring(begin,stop+len));
			s = s.substring(0,begin) + s.substring(stop+len);
			sLower = sLower.substring(0,begin) + sLower.substring(stop+len);
			begin=sLower.indexOf(first);
			stop=sLower.indexOf(last, begin+9);
		}
		return matchList;
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
	
	protected String Phase1(String s, String first, String last) {
		int begin=s.indexOf(first);
		int stop=s.indexOf(last, begin+1);
		int len = last.length();
		counter=0;
		while (begin>-1 && stop>-1) {
			GlobalStrings.put("{" + counter  + "}", s.substring(begin,stop+len)) ;
			s = s.substring(0,begin) + " {" + counter  + "} " + s.substring(stop+len);
			begin=s.indexOf(first);
			stop=s.indexOf(last, begin+1);
			counter++;
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
	
	public void CollectNeeds(ExtractResult res, Map<String, String> useNeeds, Map<String,File> OrderedFiles) {
		for (Entry<String,ExtractResultMethod> entry: Methods.entrySet()) {
			ExtractResultMethod m = entry.getValue();
			for (Entry<String,Boolean> v: m.Needs.entrySet()) {
				String vKey = v.getKey();
				if (useNeeds.containsKey(vKey)) {
					Needs.put(vKey, useNeeds.get(vKey));
				}
			}
			CheckModules(res,m,useNeeds, OrderedFiles);
		}
		
	}
	
	protected void CheckModules(ExtractResult res, ExtractResultMethod m, Map<String, String> useNeeds, Map<String,File> OrderedFiles ) {
		for (Entry<String,String> v: m.Calls.entrySet()) {
			String vMethod = v.getKey();
			String vModule = v.getValue();
			
			ExtractResultModule mod = res.Modules.getOrDefault(vModule.toLowerCase(), null);
			if (mod!=null) {
				ExtractResultMethod meth = mod.Methods.getOrDefault(vMethod.toLowerCase(), null);
				if (meth!=null) {
					if (meth.Name.equalsIgnoreCase(m.Name)) {
						continue;
					}
					if (AlreadyChecked.containsKey(vModule + "." + meth.Name.toLowerCase())) {
						continue;
					}
					for (Entry<String,Boolean> vs: meth.Needs.entrySet()) {
						String vKey = vs.getKey();
						if (useNeeds.containsKey(vKey)) {
							Needs.put(vKey, useNeeds.get(vKey));
						} else {
							for (Entry<String,File> oe: OrderedFiles.entrySet()) {
								if (vKey.equalsIgnoreCase(oe.getKey())) {
									Needs.put(vKey, "$TOCOLLECT$");
								}
							}
						}
					}
					AlreadyChecked.put(vModule + "." + meth.Name.toLowerCase(), true);
					CheckModules(res, meth, useNeeds, OrderedFiles);
				} else {
					meth = mod.Methods.getOrDefault("process_globals", null);
					if (meth!=null) {
						for (Entry<String,Boolean> vs: meth.Needs.entrySet()) {
							String vKey = vs.getKey();
							String varName = meth.GlobalNeeds.get(vKey.toLowerCase());
							if (varName.indexOf(vMethod)>-1) {
								if (useNeeds.containsKey(vKey)) {
									Needs.put(vKey, useNeeds.get(vKey));
								} 
							}
						}
					}
				}
			} 			
		}		
		for (Entry<String,Boolean> v: m.OwnCalls.entrySet()) {
			String vMethod = v.getKey();
			String vModule = m.InModule;
			
			ExtractResultModule mod = res.Modules.getOrDefault(vModule.toLowerCase(), null);
			if (mod!=null) {
				ExtractResultMethod meth = mod.Methods.getOrDefault(vMethod.toLowerCase(), null);
				if (meth!=null) {
					if (meth.Name.equalsIgnoreCase(m.Name)) {
						continue;
					}
					if (AlreadyChecked.containsKey(vModule + "." + meth.Name.toLowerCase())) {
						continue;
					}
					for (Entry<String,Boolean> vs: meth.Needs.entrySet()) {
						String vKey = vs.getKey();
						if (useNeeds.containsKey(vKey)) {
							Needs.put(vKey, useNeeds.get(vKey));
						} else {
							for (Entry<String,File> oe: OrderedFiles.entrySet()) {
								if (vKey.equalsIgnoreCase(oe.getKey())) {
									Needs.put(vKey, "$TOCOLLECT$");
								}
							}
						}
					}
					AlreadyChecked.put(vModule + "." + meth.Name.toLowerCase(), true);
					CheckModules(res, meth, useNeeds, OrderedFiles);
				}	
			}			
		}			
	}
	
	protected void ExtractMethods(Map<String,File> OrderedFiles, ExtractResult res, String fileName) {
		String MethodName="";
		ExtractResultMethod method=null;
		Map<String, Boolean> allMethods = new LinkedHashMap<String, Boolean>();		
				
		for (int i=0;i<Lines.size();i++) {
			String s = Lines.get(i).CurrentLine;
			try {
				res.analyseLog.write(fileName + "[" + Lines.get(i).LineNumber + "] " + Lines.get(i).OriginalLine + "\n");				
			} catch (IOException e1) {
			}
			
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
								
				method = new ExtractResultMethod(MethodName, Name);
				if (s.startsWith("private")) {
					method.Scope = SCOPE.PRIVATE;
				}
				
				if (MethodName.toLowerCase().contains("b4js") || MethodName.equalsIgnoreCase("process_globals") || MethodName.equalsIgnoreCase("class_globals")) {
					method.Lines.add(Lines.get(i));
				}
				
				if (MethodName.equalsIgnoreCase("initializeb4js")) {
					method.Needs.put("sub initializeb4js", true);
					B4JS B4JSClass = ABMaterial.B4JSClasses.getOrDefault(Name.toLowerCase(), null);
					if (B4JSClass==null) {
						B4JSClass = new B4JS();
						B4JSClass.Initialize(Name);
					}

					ABMaterial.B4JSClasses.put(Name.toLowerCase(), B4JSClass);
				}
				
				allMethods.put(MethodName.toLowerCase(), true);
			} else {
				if (s.startsWith("end sub")) {
					
					if (MethodName.toLowerCase().contains("b4js") || MethodName.equalsIgnoreCase("process_globals") || MethodName.equalsIgnoreCase("class_globals")) {
						method.Lines.add(Lines.get(i));
					}
					
					Methods.put(MethodName.toLowerCase(), method);					
					MethodName="";
				} else {
					if (!MethodName.equals("")) {
						
						if (MethodName.toLowerCase().contains("b4js") || MethodName.equalsIgnoreCase("process_globals") || MethodName.equalsIgnoreCase("class_globals")) {
							method.Lines.add(Lines.get(i));
						}
						
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
										NeedsFromClass.put(varType.toLowerCase(), true);
									}
								}
							}
							
							// NEW 2017/09/08
							j = spl[sp].indexOf(".inputmask");
							if (j>-1) {
								method.Needs.put("inputmask", true);
							}
							j = spl[sp].indexOf(".usealternativescrollbar");
							if (j>-1) {
								method.Needs.put("usealternativescrollbar", true);
							}
							j = spl[sp].indexOf(".iseditable");
							if (j>-1) {
								res.NeedsInputMask=true;
							}
							j = spl[sp].indexOf(".isresponsivecontainer");
							if (j>-1) {
								method.Needs.put("isresponsivecontainer", true);
							}
							j = spl[sp].indexOf(".b4jsuniquekey");
							if (j>-1) {
								method.Needs.put("b4jsuniquekey", true);
							}
							j = spl[sp].indexOf(".b4json");
							if (j>-1) {
								method.Needs.put("b4jsuniquekey", true);
							}
							j = spl[sp].indexOf(".b4jsrunmethod");
							if (j>-1) {
								method.Needs.put("b4jsuniquekey", true);
							}
							
							j = spl[sp].indexOf(".autocomplete_google");
							if (j>-1) {
								method.Needs.put("abmgooglemap", true);
							}
							j = spl[sp].indexOf(".loadabmlayout");
							if (j>-1) {
								String layoutName = GetLayoutName(s);
								File f = new File(anywheresoftware.b4a.objects.streams.File.getDirApp() + "/abmlayouts/", layoutName.toLowerCase() + ".abm");
								BufferedReader br=null;
								boolean isGrid=false;								
								try {
									String sCurrentLine;
									br = new BufferedReader(new FileReader(f));
									while ((sCurrentLine = br.readLine()) != null) {											
										switch (sCurrentLine) {
										case "[GRID]":
											isGrid=true;
											break;
										case "[/GRID]":
											isGrid=false;
											break;
										default:
											if (!sCurrentLine.startsWith("[TYPE]") && !sCurrentLine.startsWith("[PROPERTIES]")){
												if (!isGrid) {
													String[] values = Regex.Split(":", sCurrentLine);
													String[] vals = Regex.Split(",", values[0]);
													String compType = ABMaterial.GetComponentType(vals[2]);
													if (!compType.equals("")) {
														method.Needs.put(compType, true);
														if (MethodName.equalsIgnoreCase("process_globals")) {
															method.GlobalNeeds.put(compType, vals[4]);
														}
													}
												}
											}
											break;
										}
									}
								} catch (IOException e) {
									//e.printStackTrace();
									BA.LogError("ERROR: Cannot find the layout named: " + layoutName);
								} finally {
									try {
										if (br != null)br.close();
									} catch (IOException ex) {
										ex.printStackTrace();
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
										
										if (method!=null) {
											method.OwnCalls.put(call, true);
										}
									}
								}
							}
						}
					}
			}	
		}
	}
	
	protected String GetLayoutName(String str) {
		String paramStr = ExtractBetween(str, "(", ")");
		String[] params = Regex.Split(",",paramStr);
		if (params.length == 1) {
			return GetString(params[0].trim()).replace("\"", "");
		} else {
			return GetString(params[1].trim()).replace("\"", "");
		}
	}
	
	public static int indexOfAny(int begin, String str, char[] searchChars) {
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
	
}
