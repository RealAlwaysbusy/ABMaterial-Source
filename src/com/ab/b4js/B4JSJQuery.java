package com.ab.b4js;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")
@ShortName("B4JSJQuery")
public class B4JSJQuery {
	public static String B4JSPARAM_JQUERYINDEX="_b4jsparam_jqueryindex";
	public static B4JSJQueryEvent B4JSPARAM_JQUERYEVENT=new B4JSJQueryEvent();
	
	public B4JSJQueryElement Get(String selector) {
		return new B4JSJQueryElement();
	}
	
	public B4JSJQueryElement GetByUniqueKey(String uniqueKey) {
		return new B4JSJQueryElement();
	}
	
	public B4JSJQueryElement This() {
		return new B4JSJQueryElement();
	}
	
	public Object GlobalEval(String code) {
		return null;
	}
	
	public boolean IsEmptyObject(Object obj) {
		return false;
	}
	
	public boolean IsNumeric(Object value) {
		return false;
	}
	
	public String Trim(String value) {
		return "";
	}
	
	public B4JSJQueryElement EndSub() {
		return new B4JSJQueryElement();
	}
	
	public void DocumentReadySub() {
		
	}
	
	public int ParseInt(Object value) {
		return 0;
	}
		
}
