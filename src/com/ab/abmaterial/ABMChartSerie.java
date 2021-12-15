package com.ab.abmaterial;

import java.util.ArrayList;
import java.util.List;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")
@ShortName("ABMChartSerie")
public class ABMChartSerie implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8909911429066840073L;
	public String SerieIndex="";
	protected List<Double> ValuesX = new ArrayList<Double>();
	protected List<Double> ValuesY = new ArrayList<Double>();;
	protected boolean HasXY=false;
	public String Name="";
	public boolean IsInitialized=false;
	
	/**
	 * If you use this method on a line chart, you're setting the Y values.  The X values will be an increasing number starting from 1 to length of the Y list. 
	 */
	public void SetValues(anywheresoftware.b4a.objects.collections.List values) {
		HasXY=false;
		ValuesX.clear();
		ValuesY.clear();
		for (int i=0;i<values.getSize();i++) {
			ValuesX.add((double)(i+1));
			if (values.Get(i) instanceof Double) {
				ValuesY.add((double)values.Get(i));
			} else {
				int v = (int) values.Get(i);				
				Double d = Double.valueOf(v);
				ValuesY.add(d);
			}
		}		
	}

	/**
	 * Only to use with a line chart. The size of valuesX and valuesY must be the same! 
	 */
	public void SetValuesXY(anywheresoftware.b4a.objects.collections.List valuesX, anywheresoftware.b4a.objects.collections.List valuesY) {
		HasXY=true;
		ValuesX.clear();
		ValuesY.clear();
		
		for (int i=0;i<valuesX.getSize();i++) {
			if (valuesX.Get(i) instanceof Double) {
				ValuesX.add((double)valuesX.Get(i));
			} else {
				int v = (int) valuesX.Get(i);
				Double d = Double.valueOf(v);
				ValuesX.add(d);
			}
		}
		for (int i=0;i<valuesY.getSize();i++) {
			if (valuesY.Get(i) instanceof Double) {
				ValuesY.add((double)valuesY.Get(i));
			} else {
				int v = (int) valuesY.Get(i);
				//Double d = new Double(v);
				Double d = Double.valueOf(v);
				ValuesY.add(d);
			}
		}
	}
	
	public double GetValueX(int index) {
		return ValuesX.get(index);
	}
	
	public double GetValueY(int index) {
		return ValuesY.get(index);
	}
	
	public void ClearValueX() {
		ValuesX = new ArrayList<Double>();
	}
	
	public void ClearValueY() {
		ValuesY = new ArrayList<Double>();
	}
	
	/**
	 * As a line, you can specify a SERIE (CHART_SERIEINDEX_A -> CHART_SERIEINDEX_O) because of a hack 
	 */
	public void InitializeForLine(String serieIndex) {
		SerieIndex=serieIndex;
		IsInitialized=true;
	}
	
	/**
	 * As a bar, you cannot specify a SERIE. The hack for line does not work. First added will be A, second B, etc in the theme
	 */
	public void InitializeForBar() {
		IsInitialized=true;
	}
	
	/**
	 * As a pie, gauge or donut, you cannot specify a SERIE. The hack for line does not work. First added will be A, second B, etc in the theme
	 */
	public void InitializeForPie() {
		IsInitialized=true;
	}
}
