package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Hide;

@Hide
public class ABMObject implements java.io.Serializable {
	private static final long serialVersionUID = -8753749123428151778L;
	public int Type=ABMaterial.UITYPE_UNDEFINED;	
	protected boolean LoadAtConnect=true;
	protected boolean HadInBrowserCheck=false;
	
	protected ZAbstractDesignerDefinition ZABDEF=new ZAbstractDesignerDefinition();
	
	public void ZABInit() {
		ZABDEF.InitProperty("type", Type, null, false, "int", false, 0);
		ZABDEF.InitProperty("Load At Connect", LoadAtConnect, "", true, "boolean", true, 0);
	}
	
	public void ZABBuild(ABMPage page) throws NoSuchFieldException, SecurityException {
		Type = (int) ZABDEF.Property("type").Value;
		LoadAtConnect = (boolean) ZABDEF.Property("load at connect").Value;
	}
	
	public ZAbstractDesignerDefinition getZABDEF() {
		return ZABDEF;
	}
	
	
}
