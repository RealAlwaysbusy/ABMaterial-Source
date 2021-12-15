package com.ab.abmaterial;

import javax.script.ScriptException;

//import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.ShortName;

@ShortName("B4JSServerVariable")
public class B4JSVariable {
	protected String VarName="";
	protected String _B4JSClassName="";
	
	public void Initialize(String varName, String B4JSClassName) {
		VarName = varName.toLowerCase();
		_B4JSClassName =  B4JSClassName;
		if (!B4JSClassName.equals("")) {
			StringBuilder s = new StringBuilder();
			s.append("if (_b4jsclasses['b4jspagekey" + VarName + "'] === undefined) {");
			s.append("_b4jsclasses['b4jspagekey" + VarName + "'] = new b4js_" + B4JSClassName.toLowerCase() + "();");
			s.append("_b4jsclasses['b4jspagekey" + VarName + "'].initializeb4js();");
			s.append("}");		
			try {
				ABMaterial.jsEngine.eval(s.toString());
			} catch (ScriptException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void Initialize(String varName) {
		VarName = varName.toLowerCase();		
	}
	
	public Object B4JSRunMethod(String methodName, anywheresoftware.b4a.objects.collections.List Params) {		
		String pars = "";
		if (Params.IsInitialized()) {
			for (int i=0;i<Params.getSize();i++) {
				if (i>0) {
					pars=pars + ", "; 
				}
				if (Params.Get(i) instanceof String) {
					pars = pars + "\"" + (String) Params.Get(i) + "\""; 
				} else {
					pars = pars + Params.Get(i);
				}
			}
		}
		Object res=null;
		try {
			if (_B4JSClassName.equals("")) {				
				res = ABMaterial.jsEngine.eval(methodName + "(" + pars + ");");
			} else {	
				StringBuilder s = new StringBuilder();
				
				s.append("var codeToExecute = 'return _b4jsclasses[\"b4jspagekey" + VarName + "\"]." + methodName.toLowerCase() + "(" + pars + ")';");        	
				s.append("var tmpFunc = new Function(codeToExecute);");
				s.append("if (typeof _b4jsclasses[\"b4jspagekey" + VarName + "\"]." + methodName.toLowerCase() + " === \"function\") {");
					s.append("var fret;");
					s.append("try { ");
						s.append("fret = tmpFunc();");	
					s.append("} catch(err) {");
					s.append("}");
				s.append("} else {");
					s.append("var tmpFunc = new Function('return " + methodName + "(" + pars + ")');");
					s.append("if (typeof " + methodName + " === \"function\") {");
						s.append("var fret;");
						s.append("try { ");
							s.append("fret = tmpFunc();");	
						s.append("} catch(err) {");
						s.append("}");
					s.append("}");
				s.append("}");
				
				res = ABMaterial.jsEngine.eval(s.toString());
			}			
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return res;
	}
}
