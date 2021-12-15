package com.ab.abmaterial;

import java.util.ArrayList;
import java.util.List;

public class ABMCanvasCommand implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5266132972083200458L;
	protected int type=0;
	protected transient List<Object> Params = new ArrayList<Object>();
	protected boolean IsBuild=false;
	protected String Str="";
	
	ABMCanvasCommand() {
		
	}
	
	ABMCanvasCommand(int type) {
		this.type=type;
	}
	
	protected List<Object> GetParams() {
		List<Object> tmpParams = new ArrayList<Object>();
		tmpParams.add(type);
		tmpParams.addAll(Params);
		return tmpParams;
	}
	
	protected String ToSendString() {
		if (IsBuild) {
			return Str;
		}
		StringBuilder s = new StringBuilder();
		s.append("[" + type);
		for (int index=0;index<Params.size();index++) {
			s.append(Params.get(index));
			if (index<Params.size()-1) {
				s.append(",");
			}
		}
		s.append("]");
		Str = s.toString();
		IsBuild=true;
		return Str;
	}
}
