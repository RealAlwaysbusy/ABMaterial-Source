package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

@Author("Alain Bailleul")  
@Hide
public class ABMRowCell implements java.io.Serializable {
	
	private static final long serialVersionUID = 7879001598078247893L;
	String rowId="";
	String cellId="";
	
	ABMRowCell(String rId, String cId) {
		rowId=rId;
		cellId=cId;
	}
	
	ABMRowCell() {
		
	}
}
