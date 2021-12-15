package com.ab.abmaterial;

import java.util.LinkedHashMap;
import java.util.Map;

import anywheresoftware.b4a.BA.Hide;

@Hide
public class ABMDragDropGroup {
	Map<String,ABMCell> Cells = new LinkedHashMap<String,ABMCell>();
	boolean IsBuild=false;
	int minHeight=0;
	
	ABMDragDropGroup() {
		IsBuild=false;
	}
	
	ABMDragDropGroup(String name, ABMCell cell) {
		Cells.put(name.toLowerCase(), cell);
		IsBuild=false;
	}
	
	
}
