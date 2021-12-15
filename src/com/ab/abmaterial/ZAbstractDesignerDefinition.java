package com.ab.abmaterial;

import java.util.LinkedHashMap;

//import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.objects.collections.List;
import anywheresoftware.b4a.objects.collections.Map;

@Author("Alain Bailleul")  
@ShortName("ZAbstractDesignerDefinition")
public class ZAbstractDesignerDefinition implements java.io.Serializable {
	private static final long serialVersionUID = 6610459975144070704L;
	protected Map properties = new Map();
	protected boolean IsInitialized=false;
	
	public String ToString(boolean ExternalOnly) {
		StringBuilder s = new StringBuilder();
		for (int i=0;i<properties.getSize();i++) {
			ZAbstractDesignerProperty prop = (ZAbstractDesignerProperty) properties.GetValueAt(i);
			if (ExternalOnly) {
				if (prop.mExternal) {
					s.append(prop.ToString());
				}
			} else {
				s.append(prop.ToString());
			}
		}
		return s.toString();
	}
	
	public ZAbstractDesignerDefinition Clone() {
		ZAbstractDesignerDefinition c = new ZAbstractDesignerDefinition();
		c.IsInitialized = IsInitialized;
		c.properties.Initialize();
		for (int i=0;i<properties.getSize();i++) {
        	ZAbstractDesignerProperty prop = (ZAbstractDesignerProperty) properties.GetValueAt(i);
        	c.properties.Put(properties.GetKeyAt(i), prop.Clone());
		}
		return c;
	}
	
	public List Groups(int propType) {
		List ret = new List();
		ret.Initialize();
        java.util.Map<Integer,Boolean> didIt= new LinkedHashMap<Integer, Boolean>();
        for (int i=0;i<properties.getSize();i++) {
        	ZAbstractDesignerProperty prop = (ZAbstractDesignerProperty) properties.GetValueAt(i);
        	switch (propType) {
        	case 0: // all
        		if (!didIt.getOrDefault(prop.mGroup, false)) {
        			ret.Add(prop.mGroup);
        			didIt.put(prop.mGroup, true);
        		}
        		break;
        	case 1: // external
        		if (prop.mExternal) {
        			if (!didIt.getOrDefault(prop.mGroup, false)) {
            			ret.Add(prop.mGroup);
            			didIt.put(prop.mGroup, true);
            		}
        		}
        		break;
        	case 2: // internal
        		if (!prop.mExternal) {
        			if (!didIt.getOrDefault(prop.mGroup, false)) {
            			ret.Add(prop.mGroup);
            			didIt.put(prop.mGroup, true);
            		}
        		}
        		break;
        	}        	
        }
		return ret;
	}
	
	public Map Properties(int Group, int propType) {
		Map ret = new Map();
		ret.Initialize();
		for (int i=0;i<properties.getSize();i++) {
			ZAbstractDesignerProperty prop = (ZAbstractDesignerProperty) properties.GetValueAt(i);
			switch (propType) {
			case 0: // all
				if (prop.mGroup==Group) {
					ret.Put(properties.GetKeyAt(i), properties.GetValueAt(i));	        	
				}
				break;
			case 1: // external
				if (prop.mExternal) {
					if (prop.mGroup==Group) {
						ret.Put(properties.GetKeyAt(i), properties.GetValueAt(i));	        	
					}
				}
				break;
			case 2: // internal
				if (!prop.mExternal) {
					if (prop.mGroup==Group) {
						ret.Put(properties.GetKeyAt(i), properties.GetValueAt(i));	        	
					}
				}
				break;
			}
			
		}
		return ret;
	}
	
	public ZAbstractDesignerProperty Property(String prop) {
		for (int i=0;i<properties.getSize();i++) {
			String key = (String) properties.GetKeyAt(i);
			if (key.equalsIgnoreCase(prop)) {
				return (ZAbstractDesignerProperty) properties.GetValueAt(i);				
			}
			
		}
		return null;
	}
	
	@Hide
	protected void InitProperty(String prop, Object value, String selections, boolean external, String Type, boolean needed, int group) {
		if (!properties.IsInitialized()) {
			properties.Initialize();
		}
		ZAbstractDesignerProperty p = new ZAbstractDesignerProperty();
		p.mName = prop;
		p.Value = value;
		p.mSelections = selections;
		p.mExternal = external;
		p.mPropType = Type;
		p.mNeeded = needed;		
		p.mGroup = group;
		properties.Put(prop, p);
	}

}
