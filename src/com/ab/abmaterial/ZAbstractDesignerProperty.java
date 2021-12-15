package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
//import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.objects.collections.List;

@Author("Alain Bailleul")  
@ShortName("ZAbstractDesignerProperty")
public class ZAbstractDesignerProperty implements java.io.Serializable {
	
	private static final long serialVersionUID = -355493996614323630L;
	protected String mName="";
	public String mPropType="";
	public Object Value=null;
	public String mSelections="";
	public boolean mExternal=false;
	public boolean mNeeded=false;
	public int mGroup=0;
	
	public ZAbstractDesignerProperty Clone() {
	
		ZAbstractDesignerProperty c = new ZAbstractDesignerProperty();
		c.mName = mName;
		c.mPropType = mPropType;
		c.Value = Value;
		c.mSelections = mSelections;
		c.mExternal = mExternal;
		c.mNeeded = mNeeded;
		c.mGroup = mGroup;
		return c;
	}
	
	public String getName() {
		return mName;
	}
	
	public String getPropType() {
		return mPropType;
	}
	
	public List getSelections() {
		List l = new List();
		l.Initialize();
		String[] spl = mSelections.split(":");
		for (int i=0;i<spl.length;i++) {
			if (!spl[i].equals("")) {
				l.Add(spl[i]);
			}
		}
		return l;
	}
	
	public boolean getExternal() {
		return mExternal;
	}
	
	public boolean getNeeded() {
		return mNeeded;
	}
	
	public int getGroup() {
		return mGroup;
	}
	
	public String ToString() {
		StringBuilder s = new StringBuilder();
		s.append("" + mName + " = " + Value + "\n");
		s.append("     Type = " + mPropType + "\n");
		s.append("     Selections = " + mSelections + "\n");
		s.append("     External = " + mExternal + "\n");
		s.append("     Needed = " + mNeeded + "\n");
		s.append("     Property Group = " + mGroup + "\n");
		return s.toString();
	}
}
