package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")  
@ShortName("ABMTableCell")
public class ABMTableCell implements java.io.Serializable {
	
	private static final long serialVersionUID = 6804189992677414060L;
	public String TableName="";
	public int Row=0;
	public String RowUniqueId="";
	public int Column=0;	
	public boolean IsInitialized=false;
	
	public void Initialize() {
		IsInitialized=true;
	}
}
