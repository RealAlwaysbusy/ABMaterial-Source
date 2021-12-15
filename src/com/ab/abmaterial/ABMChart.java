package com.ab.abmaterial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ab.abmaterial.ThemeChart.ThemeSerie;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;

@Author("Alain Bailleul")
@ShortName("ABMChart")
public class ABMChart extends ABMComponent{
	
	private static final long serialVersionUID = -1586134303697511401L;
	protected ThemeChart Theme=new ThemeChart();
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	protected String ChartType="";
	protected String ChartRatio="";
	protected transient Map<String,ABMChartSerie> Series = new LinkedHashMap<String,ABMChartSerie>();
	protected transient List<String> Labels = new ArrayList<String>();
	protected LineAxisOptions mLine = new LineAxisOptions();
	protected BarAxisOptions mBar = new BarAxisOptions();
	protected PieOptions mPie = new PieOptions();
	protected String[] sseries = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o"};
	protected transient List<String> Plugins = new ArrayList<String>();
			
	public void Initialize(ABMPage page, String id, String chartType, String chartRatio, String themeName) {
		this.ID = id;
		this.Type = ABMaterial.UITYPE_CHART;
		this.Page = page;
		//this.Labels.Initialize();
		//this.Plugins.Initialize();
		this.ChartRatio=chartRatio;
		this.ChartType=chartType;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Charts.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Charts.get(themeName.toLowerCase()).Clone();				
			}
		}	
		IsInitialized=true;
	}
	
	/**
	 * Make sure you added to the Page the needed plugin Javascript file(s) and CSS file(s)!
	 * 
	 * see https://gionkunz.github.io/chartist-js/plugins.html for the PluginDefinition part.
	 * It is the String between plugins: [ and ]. e.g. for the Axis Title Plugin:
	 * 
	 * in the PageBuild() method:
	 *      page.AddExtraJavaScriptFile("chartist-plugin-axistitle.min.js")
	 *  
	 * for your chart:
	 *      chart.AddPluginDefinition($"Chartist.plugins.ctAxisTitle({
     *                   axisX: {
     *                       axisTitle: 'Time (mins)',
     *                       axisClass: 'ct-axis-title',
     *                       offset: {
     *                           x: 0,
     *                           y: 30
     *                       },
     *                       textAnchor: 'middle'
     *                   },
     *                   axisY: {
     *                       axisTitle: 'Goals',
     *                       axisClass: 'ct-axis-title',
     *                       offset: {
     *                           x: 0,
     *                           y: -5
     *                       },
     *                       flipTitle: false
     *                   }
     *               })"$)
	 */
	public void AddPluginDefinition(String PluginDefinition) {
		Plugins.add(PluginDefinition);
	}
	
	public void AddLabel(String value) {
		Labels.add(value);
	}
	
	public void AddLabels(anywheresoftware.b4a.objects.collections.List values) {
		for (int i=0;i<values.getSize();i++) {
			Labels.add((String)values.Get(i));
		}
	}
	
	/**
	 * Removes all previously added and the adds the new ones
	 * Shortcut for:
	 *     chart.ClearLabels(Array as String("1","2","3")) 
	 */
	public void SetLabels(anywheresoftware.b4a.objects.collections.List values) {
		Labels.clear();
		for (int i=0;i<values.getSize();i++) {
			Labels.add((String)values.Get(i));
		}
	}
	
	public void ClearLabels() {
		Labels = new ArrayList<String>();
	}
	
	/**
	 * Only usable if you have defined the Zoom plugin
	 */
	public void ResetZoom() {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		Page.ws.RunFunction("resetchart", Params);
		Page.ws.Eval(BuildScript(), null);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("Chart ResetZoom: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
				}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	@Override
	protected void ResetTheme() {
		UseTheme(Theme.ThemeName);		
	}
	
	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase();
	}
	
	public LineAxisOptions OptionsLine() {
		return mLine;
	}	
	
	public BarAxisOptions OptionsBar() {
		return mBar;
	}
	
	public PieOptions OptionsPie() {
		return mPie;
	}
	
	/**
	 * You can add maximum 15 series CHART_SERIEINDEX_A -> CHART_SERIEINDEX_O! 
	 */
	public void AddSerie(ABMChartSerie serie) {
		if (serie.SerieIndex.equals("")) {
			serie.SerieIndex=sseries[Series.size()];
		}
		Series.put(serie.SerieIndex.toLowerCase(), serie);
	}
	
	public ABMChartSerie GetSerie(String serieIndex) {
		return Series.getOrDefault(serieIndex, null);
	}
	
	public void RemoveSerie(String serieIndex) {
		Series.remove(serieIndex.toLowerCase());
	}
	
	public void Clear() {
		Series.clear();
	}
		
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}
	
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Charts.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Charts.get(themeName.toLowerCase()).Clone();				
			}
		}
	}
	
	public void SetTooltip(String text, String position, int delay) {
		this.ToolTipText = text;
		this.ToolTipPosition = position;
		this.ToolTipDelay = delay;			
	}
	
	@Override
	protected void CleanUp() {
		super.CleanUp();
	}
	
	@Override
	protected void RemoveMe() {
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
	}
	
	@Override
	protected void FirstRun() {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());		
		Page.ws.RunFunction("inittooltipped", Params);
		Page.ws.Eval(BuildScript(), null);
		
		super.FirstRun();
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		j.SetProp("class", BuildClass());
		
		
		if (!ToolTipText.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-position", ToolTipPosition);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-delay", ""+ToolTipDelay);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-tooltip", ToolTipText);
		}	
		
		ABMaterial.ChartUpdate(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), BuildData(), BuildOptions());
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Chart Refresh: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
					}
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		super.Refresh();
	}
	
	@Override
	protected String Build() {
		if (Theme.ThemeName.equals("default")) {
			Theme.Colorize(Page.CompleteTheme.MainColor);
		}
		StringBuilder s = new StringBuilder();
				
		String toolTip="";
		if (!ToolTipText.equals("")) {
			toolTip=" data-position=\"" + ToolTipPosition + "\" data-delay=\"" + ToolTipDelay + "\" data-tooltip=\"" + ToolTipText + "\" "; 
		}
		s.append("<div " + toolTip + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"" + BuildClass() + "\" ></div>");
		
		IsBuild=true;
		return s.toString();
	}
	
	@Hide
	protected String BuildScript() {
		StringBuilder s = new StringBuilder();
		
		String[] series = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o"};
		String prefix = "";
		ThemeSerie serie = null;
		String id = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		
		
		s.append("$.stylesheet('.ct-chart" + id + Theme.ThemeName.toLowerCase() + " .ct-label', {'fill': '" + ABMaterial.GetColorStrMap(Theme.LabelColor,Theme.LabelColorIntensity) + "','color': '" + ABMaterial.GetColorStrMap(Theme.LabelColor,Theme.LabelColorIntensity) + "','font-size': '" + Theme.LabelFontSizePx + "px'});");
		
		for (int i=0;i<15;i++) {
			prefix = ".ct-chart" + id + Theme.ThemeName.toLowerCase() + " .ct-series.ct-series-" + series[i];
			serie = Theme.Serie(series[i]);
			
			s.append("$.stylesheet('.ct-chart" + id + Theme.ThemeName.toLowerCase() + " .ct-legend .ct-series-" + i + ":before', {'background-color': '" + ABMaterial.GetColorStrMap(serie.Color,serie.ColorIntensity) + "','border-color': '" + ABMaterial.GetColorStrMap(serie.Color,serie.ColorIntensity) + "'});");
			
			s.append("$.stylesheet('" + prefix + " .ct-bar', {'stroke': '" + ABMaterial.GetColorStrMap(serie.Color,serie.ColorIntensity) + "'});");
			s.append("$.stylesheet('" + prefix + " .ct-line', {'stroke': '" + ABMaterial.GetColorStrMap(serie.Color,serie.ColorIntensity) + "'});");
			s.append("$.stylesheet('" + prefix + " .ct-point', {'stroke': '" + ABMaterial.GetColorStrMap(serie.Color,serie.ColorIntensity) + "'});");
			s.append("$.stylesheet('" + prefix + " .ct-slice-donut', {'stroke': '" + ABMaterial.GetColorStrMap(serie.Color,serie.ColorIntensity) + "'});");
			
			s.append("$.stylesheet('" + prefix + " .ct-area', {'fill': '" + ABMaterial.GetColorStrMap(serie.Color,serie.ColorIntensity) + "'});");
			s.append("$.stylesheet('" + prefix + " .ct-slice-pie', {'fill': '" + ABMaterial.GetColorStrMap(serie.Color,serie.ColorIntensity) + "'});");
			
			
			s.append("$.stylesheet('" + prefix + " .ct-line', {'stroke-width': '" + serie.LineStrokeWidthPx + "px','stroke-linecap': '" + serie.LineCap + "'});");
			s.append("$.stylesheet('" + prefix + " .ct-point', {'stroke-width': '" + serie.LinePointStrokeWidthPx + "px','stroke-linecap': '" + serie.LinePointCap + "'});");
			s.append("$.stylesheet('" + prefix + " .ct-bar', {'stroke-width': '" + serie.BarStrokeWidthPx + "px'});");
			/*
			*/
		}		
		
		s.append("chartdata['" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "']=");
		s.append(BuildData() + ";");
		s.append("chartoptions['" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "']=");
		s.append(BuildOptions() + ";");
		
		switch (ChartType) {
			case ABMaterial.CHART_TYPELINE:						
				s.append("BuildLineChart('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "');" );
				break;
			case ABMaterial.CHART_TYPEBAR:				
				s.append("BuildBarChart('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "');" );				
				break;
			case ABMaterial.CHART_TYPEPIE:				
				s.append("BuildPieChart('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "');" );				
				break;			
		}
		return s.toString();
	}
	
	@Hide
	protected String BuildData() {
		StringBuilder s = new StringBuilder();
		
		int Counter=0;
		Map<String, ABMChartSerie> NeedsEmpty = new LinkedHashMap<String, ABMChartSerie>();
		double minY=Double.MAX_VALUE;
		double maxY=Double.MIN_VALUE;
		
		switch (ChartType) {
			case ABMaterial.CHART_TYPELINE:
				s.append("{\"labels\":[");
				for (int i=0;i<Labels.size();i++) {
					String txt = "" + Labels.get(i);
					s.append("\"" + ABMaterial.HTMLConv().htmlEscape(txt, Page.PageCharset) + "\"");
					if (i<Labels.size()-1) {
						s.append(", ");
					}
				}
				s.append("], \"series\": [");
				
				Counter=0;
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_A, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_B, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_C, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_D, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_E, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_F, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_G, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_H, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_I, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_J, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_K, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_L, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_M, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_N, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_O, null);
				for (Entry<String, ABMChartSerie> entry : Series.entrySet()) {					
					ABMChartSerie chs = entry.getValue();
					if (chs.ValuesY.size()>0) {
						NeedsEmpty.put(chs.SerieIndex.toLowerCase(), chs);
						for (int k=0;k<chs.ValuesY.size();k++) {
							if (Double.parseDouble("" + chs.ValuesY.get(k)) != ABMaterial.CHART_NULLVALUE) {
								
								double v = Double.valueOf(chs.ValuesY.get(k).toString());
								if (v>maxY) {
									maxY=v;
								}
								if (v<minY) {
									minY=v;
								}
							}
						}
					}
				}
				
				for (Entry<String, ABMChartSerie> entry : NeedsEmpty.entrySet()) {
					ABMChartSerie chs = entry.getValue();
					if (chs==null) {
						s.append("{\"name\": \"series-" + (Counter+1) + "\",");
						s.append("\"data\": [[]]}");
						Theme.Serie(entry.getKey()).Color=ABMaterial.COLOR_TRANSPARENT;
					} else {
						if (!chs.Name.equals("")) {
							s.append("{\"name\": \"" + chs.Name + "\",");
						} else {
							s.append("{\"name\": \"series-" + (Counter+1) + "\",");
						}
						if (chs.HasXY) {
							s.append("\"data\": [");
							for (int j=0;j<chs.ValuesX.size();j++) {
								if (Double.parseDouble("" + chs.ValuesX.get(j)) == ABMaterial.CHART_NULLVALUE) {
									s.append("{x: null,");
								} else {
									s.append("{x: " + chs.ValuesX.get(j) + ",");
								}
								if (Double.parseDouble("" + chs.ValuesY.get(j)) == ABMaterial.CHART_NULLVALUE) {
									s.append("y: null}");
								} else {
									s.append("y: " + chs.ValuesY.get(j) + "}");
								}
								
								if (j<chs.ValuesX.size()-1) {
									s.append(", ");
								}
							}
							
							s.append("]}");
						} else {
							s.append("\"data\": [");
							for (int j=0;j<chs.ValuesY.size();j++) {
								if (Double.parseDouble("" + chs.ValuesY.get(j)) == ABMaterial.CHART_NULLVALUE) {
									s.append("null");
								} else {
									s.append("\"" + chs.ValuesY.get(j) + "\"");
								}
								if (j<chs.ValuesY.size()-1) {
									s.append(", ");
								}
							}
							s.append("]}");
						}
					}
					if (Counter<NeedsEmpty.size()-1) {
						s.append(", ");
					}
					Counter++;
				}
				s.append("]}");
				
				break;
			case ABMaterial.CHART_TYPEBAR:
				
				s.append("{\"labels\":[");
				for (int i=0;i<Labels.size();i++) {
					String txt = "" + Labels.get(i);
					s.append("\"" + ABMaterial.HTMLConv().htmlEscape(txt, Page.PageCharset) + "\"");
					if (i<Labels.size()-1) {
						s.append(", ");
					}
				}
				s.append("], \"series\": [");
				
				Counter=0;
				
				for (Entry<String, ABMChartSerie> entry : Series.entrySet()) {
					ABMChartSerie chs = entry.getValue();
					
					s.append("{\"name\": \"series-" + (Counter+1) + "\",");
					s.append("\"data\": [");
					for (int j=0;j<chs.ValuesY.size();j++) {
						
						if (Double.parseDouble("" + chs.ValuesY.get(j)) == ABMaterial.CHART_NULLVALUE) {
							s.append("null");
						} else {
							s.append("\"" + chs.ValuesY.get(j) + "\"");
						}
						if (j<chs.ValuesY.size()-1) {
							s.append(", ");
						}
					}
					s.append("]}");
					if (Counter<Series.size()-1) {
						s.append(", ");
					}
					Counter++;
				}
				s.append("]}");				
				
				break;
			case ABMaterial.CHART_TYPEPIE:
				
				s.append("{");
				if (Labels.size()>0) {					
					s.append("\"labels\":[");
					for (int i=0;i<Labels.size();i++) {
						String txt = "" + Labels.get(i);
						s.append("\"" + ABMaterial.HTMLConv().htmlEscape(txt, Page.PageCharset) + "\"");
						if (i<Labels.size()-1) {
							s.append(", ");
						}
					}
					s.append("], \"series\": [");
				} else {
					s.append("\"series\": [");
				}
				
				Counter=0;
				
				for (Entry<String, ABMChartSerie> entry : Series.entrySet()) {
					ABMChartSerie chs = entry.getValue();
					
					
					for (int j=0;j<chs.ValuesY.size();j++) {						
						if (Double.parseDouble("" + chs.ValuesY.get(j)) == ABMaterial.CHART_NULLVALUE) {
							s.append("null");
						} else {
							s.append("\"" + chs.ValuesY.get(j) + "\"");
						}
						if (j<chs.ValuesY.size()-1) {
							s.append(", ");
						}
					}
					
				}
				s.append("]}");
						
				break;			
		}
		
		return s.toString();
	}
	
	@Hide
	protected String BuildOptions() {
		StringBuilder s = new StringBuilder();
		
		int Counter=0;
		Map<String, ABMChartSerie> NeedsEmpty = new LinkedHashMap<String, ABMChartSerie>();
				
		switch (ChartType) {
			case ABMaterial.CHART_TYPELINE:		
				Counter=0;
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_A, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_B, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_C, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_D, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_E, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_F, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_G, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_H, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_I, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_J, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_K, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_L, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_M, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_N, null);
				NeedsEmpty.put(ABMaterial.CHART_SERIEINDEX_O, null);
				for (Entry<String, ABMChartSerie> entry : Series.entrySet()) {					
					ABMChartSerie chs = entry.getValue();
					if (chs.ValuesY.size()>0) {
						NeedsEmpty.put(chs.SerieIndex.toLowerCase(), chs);						
					}
				}
				s.append("{");
				s.append("\"axisX\": {");
				
				if (mLine.AxisXLow!=Integer.MAX_VALUE) {
					s.append("\"low\": " + mLine.AxisXLow + ",");
				}
				if (mLine.AxisXHigh!=Integer.MAX_VALUE) {
					s.append("\"high\": " + mLine.AxisXHigh + ",");
				}
				s.append("\"offset\": " + mLine.AxisXOffset + ",");
				s.append("\"position\": \"" + mLine.AxisXPosition + "\",");
				s.append("\"labelOffset\": {\"x\": " + mLine.AxisXLabelOffsetX + ",\"y\":" + mLine.AxisXLabelOffsetY + "},");
				s.append("\"showLabel\": " + mLine.AxisXShowLabel + ",");
				s.append("\"showGrid\": " + mLine.AxisXShowGrid + ",");
				s.append("\"labelInterpolationFnc\": function(value) { return " + mLine.AxisXLabelInterpolation + ";},");
				if (!mLine.AxisXAxisType.equals("")) {
					s.append("\"type\": " + mLine.AxisXAxisType + ",");
				}
				s.append("\"scaleMinSpace\": " + mLine.AxisXScaleMinSpace + ",");

				if (mLine.AxisXReferenceValue!=Integer.MAX_VALUE) {
					s.append("\"onlyInteger\": " + mLine.AxisXOnlyInteger + ",");
					s.append("\"referenceValue\": " + mLine.AxisXReferenceValue + "");
				} else {
					s.append("\"onlyInteger\": " + mLine.AxisXOnlyInteger + "");
				}
				s.append("},");
				s.append("\"axisY\": {");
				
				if (mLine.AxisYLow!=Integer.MAX_VALUE) {
					s.append("\"low\": " + mLine.AxisYLow + ",");
				}
				if (mLine.AxisYHigh!=Integer.MAX_VALUE) {
					s.append("\"high\": " + mLine.AxisYHigh + ",");
				}
				s.append("\"offset\": " + mLine.AxisYOffset + ",");
				s.append("\"position\": \"" + mLine.AxisYPosition + "\",");
				s.append("\"labelOffset\": {\"x\": " + mLine.AxisYLabelOffsetX + ",\"y\":" + mLine.AxisYLabelOffsetY + "},");
				s.append("\"showLabel\": " + mLine.AxisYShowLabel + ",");
				s.append("\"showGrid\": " + mLine.AxisYShowGrid + ",");
				s.append("\"labelInterpolationFnc\": function(value) { return " + mLine.AxisYLabelInterpolation + ";},");
				if (!mLine.AxisYAxisType.equals("")) {
					s.append("\"type\": " + mLine.AxisYAxisType + ",");
				}
				s.append("\"scaleMinSpace\": " + mLine.AxisYScaleMinSpace + ",");
				if (mLine.AxisYReferenceValue!=Integer.MAX_VALUE) {
					s.append("\"onlyInteger\": " + mLine.AxisYOnlyInteger + ",");
					s.append("\"referenceValue\": " + mLine.AxisYReferenceValue + "");
				} else {
					s.append("\"onlyInteger\": " + mLine.AxisYOnlyInteger + "");
				}
				s.append("},");
				if (mLine.FixedWidthPx!=Integer.MAX_VALUE) {
					s.append("\"width\": " + mLine.FixedWidthPx + ",");
				}
				if (mLine.FixedHeightPx!=Integer.MAX_VALUE) {
					s.append("\"height\": " + mLine.FixedHeightPx + ",");
				}
				
				s.append("\"chartPadding\": {\"top\": " + mLine.ChartPaddingTop + ",\"right\": " + mLine.ChartPaddingRight + ",\"bottom\": " + mLine.ChartPaddingBottom + ",\"left\": " + mLine.ChartPaddingLeft + "},");
				s.append("\"fullWidth\": " + mLine.FullWidth + ",");
				s.append("\"reverseData\": " + mLine.ReverseData + ",");
				if (Plugins.size()>0) {
					s.append("plugins: [");
					for (int m=0;m<Plugins.size();m++) {
						String pStr = (String) Plugins.get(m);
						if (m>0) {
							s.append(",");
						}
						s.append(pStr);
					}
					s.append("],");
				}
				s.append("\"series\": {");
				Counter=0;
				for (Entry<String, ABMChartSerie> entry : NeedsEmpty.entrySet()) {
					ABMChartSerie chs = entry.getValue();
					if (chs==null) {
						s.append("\"series-" + (Counter+1) + "\": {");						
						s.append("}");
					} else {
						if (!chs.Name.equals("")) {
							s.append("\"" + chs.Name + "\": {");
						} else {
							s.append("\"series-" + (Counter+1) + "\": {");
						}
						
						s.append("\"showLine\": " + mLine.Serie(entry.getKey()).ShowLine + ",");
						s.append("\"showPoint\": " + mLine.Serie(entry.getKey()).ShowPoint + ",");
						s.append("\"showArea\": " + mLine.Serie(entry.getKey()).ShowArea + ",");
						s.append("\"areaBase\": " + mLine.Serie(entry.getKey()).AreaBase + ",");
						s.append("\"lineSmooth\": " + mLine.Serie(entry.getKey()).LineSmooth + ",");
						s.append("}");
					}
					if (Counter<NeedsEmpty.size()-1) {
						s.append(", ");
					}
					Counter++;
				}
				s.append("}");
				s.append("}");				
				
				
				break;
			case ABMaterial.CHART_TYPEBAR:
			
				s.append("{");
				s.append("axisX: {");
				
				s.append("offset: " + mBar.AxisXOffset + ",");
				s.append("position: \"" + mBar.AxisXPosition + "\",");
				s.append("labelOffset: {x: " + mBar.AxisXLabelOffsetX + ",y:" + mBar.AxisXLabelOffsetY + "},");
				s.append("showLabel: " + mBar.AxisXShowLabel + ",");
				s.append("showGrid: " + mBar.AxisXShowGrid + ",");
				s.append("labelInterpolationFnc: function(value) { return " + mBar.AxisXLabelInterpolation + ";},");
				s.append("scaleMinSpace: " + mBar.AxisXScaleMinSpace + ",");
				s.append("onlyInteger: " + mBar.AxisXOnlyInteger + ",");
				s.append("},");
				s.append("axisY: {");
				
				s.append("offset: " + mBar.AxisYOffset + ",");
				s.append("position: \"" + mBar.AxisYPosition + "\",");
				s.append("labelOffset: {x: " + mBar.AxisYLabelOffsetX + ",y:" + mBar.AxisYLabelOffsetY + "},");
				s.append("showLabel: " + mBar.AxisYShowLabel + ",");
				s.append("showGrid: " + mBar.AxisYShowGrid + ",");
				s.append("labelInterpolationFnc: function(value) { return " + mBar.AxisYLabelInterpolation + ";},");
				s.append("scaleMinSpace: " + mBar.AxisYScaleMinSpace + ",");
				s.append("onlyInteger: " + mBar.AxisYOnlyInteger + ",");
				s.append("},");
				if (mBar.FixedWidthPx!=Integer.MAX_VALUE) {
					s.append("width: " + mBar.FixedWidthPx + ",");
				}
				if (mBar.FixedHeightPx!=Integer.MAX_VALUE) {
					s.append("height: " + mBar.FixedHeightPx + ",");
				}
				if (mBar.Low!=Integer.MAX_VALUE) {
					s.append("low: " + mBar.Low + ",");
				}
				if (mBar.High!=Integer.MAX_VALUE) {
					s.append("high: " + mBar.High + ",");
				}
				s.append("chartPadding: {top: " + mBar.ChartPaddingTop + ",right: " + mBar.ChartPaddingRight + ",bottom: " + mBar.ChartPaddingBottom + ",left: " + mBar.ChartPaddingLeft + "},");
				s.append("seriesBarDistance: " + mBar.SeriesBarDistance + ",");
				s.append("stackBars: " + mBar.StackBars + ",");
				s.append("horizontalBars: " + mBar.HorizontalBars + ",");
				s.append("distributeSeries: " + mBar.DistributeSeries + ",");
				s.append("reverseData: " + mBar.ReverseData + ",");
				if (Plugins.size()>0) {
					s.append("plugins: [");
					for (int m=0;m<Plugins.size();m++) {
						String pStr = Plugins.get(m);
						if (m>0) {
							s.append(",");
						}
						s.append(pStr);
					}
					s.append("],");
				}
				s.append("}");
				
				
				break;
			case ABMaterial.CHART_TYPEPIE:
				boolean HasLabels=false;				
				if (Labels.size()>0) {
					HasLabels = true;	
				}
				
				Counter=0;				
			
				s.append("{");
				if (mPie.FixedWidthPx!=Integer.MAX_VALUE) {
					s.append("width: " + mPie.FixedWidthPx + ",");
				}
				if (mPie.FixedHeightPx!=Integer.MAX_VALUE) {
					s.append("height: " + mPie.FixedHeightPx + ",");
				}
				s.append("chartPadding: {top: " + mPie.ChartPaddingTop + ",right: " + mPie.ChartPaddingRight + ",bottom: " + mPie.ChartPaddingBottom + ",left: " + mPie.ChartPaddingLeft + "},");
				s.append("startAngle: " + mPie.StartAngle + ",");
				if (mPie.Total!=Integer.MAX_VALUE) {
					s.append("total: " + mPie.Total + ",");
				}
				s.append("donut: " + mPie.IsDonut + ",");
				s.append("donutWidth: " + mPie.DonutWidth + ",");
				s.append("showLabel: " + mPie.ShowLabel + ",");
				s.append("labelOffset: " + mPie.LabelOffset + ",");
				s.append("labelPosition: \"" + mPie.LabelPosition + "\",");
				if (!HasLabels) {
					s.append("labelInterpolationFnc: function(value) { return " + mPie.LabelInterpolation + ";},");
				}
				s.append("labelDirection: \"" + mPie.LabelDirection + "\",");
				s.append("reverseData: " + mPie.ReverseData + ",");
				if (Plugins.size()>0) {
					s.append("plugins: [");
					for (int m=0;m<Plugins.size();m++) {
						String pStr = Plugins.get(m);
						if (m>0) {
							s.append(",");
						}
						s.append(pStr);
					}
					s.append("],");
				}
				s.append("}");				
								
				break;			
		}
		return s.toString();
	}
	
	@Hide
	protected String BuildClass() {			
		StringBuilder s = new StringBuilder();
		s.append(super.BuildExtraClasses());
		ThemeChart l=Theme;
		s.append("ct-chart ct-chart" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + l.ThemeName.toLowerCase() + " " + ChartRatio + " ");
		s.append(mVisibility + " ");	
		s.append(l.ZDepth + " ");
		s.append(ABMaterial.GetColorStr(l.BackColor, l.BackColorIntensity, "") + " ");
		s.append(mIsPrintableClass);
		s.append(mIsOnlyForPrintClass);
		return s.toString(); 
	}
	
	@Hide
	protected String BuildBody() {
		StringBuilder s = new StringBuilder();		
		
		return s.toString();
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMChart c = new ABMChart();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;
		c.Theme = Theme.Clone();
		c.Type = Type;
		c.mVisibility = mVisibility;	
		c.ToolTipDelay = ToolTipDelay;
		c.ToolTipPosition = ToolTipPosition;
		c.ToolTipText = ToolTipText;		
		c.ChartRatio=ChartRatio;
		c.ChartType=ChartType;
		
		return c;
	}

	/**
	 * Can not be created within B4J, But its properties and methods are available through another ABM class.
	 */
	public class LineAxisOptions implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5274166763442198699L;
		public int AxisXOffset = 30;
		public String AxisXPosition = "end";
		public int AxisXLabelOffsetX = 0;
		public int AxisXLabelOffsetY = 0;
		public boolean AxisXShowLabel=true;
		public boolean AxisXShowGrid=true;		
		public boolean AxisYOnlyInteger=false;
		public int AxisXScaleMinSpace=20;
		public int AxisXReferenceValue=Integer.MAX_VALUE;
		public String AxisXLabelInterpolation="value";
		public String AxisXAxisType="";
		public int AxisYOffset = 40;
		public String AxisYPosition = "start";
		public int AxisYLabelOffsetX = 0;
		public int AxisYLabelOffsetY = 0;
		public boolean AxisYShowLabel=true;
		public boolean AxisYShowGrid=true;
		public String AxisYLabelInterpolation="value";		
		public String AxisYAxisType="";
		public int AxisYScaleMinSpace=20;
		public int AxisYReferenceValue=Integer.MAX_VALUE;
		public boolean AxisXOnlyInteger=false;
		public int FixedWidthPx=Integer.MAX_VALUE;
		public int FixedHeightPx=Integer.MAX_VALUE;		
		public int AxisXHigh=Integer.MAX_VALUE;
		public int AxisXLow=Integer.MAX_VALUE;
		public int AxisYHigh=Integer.MAX_VALUE;
		public int AxisYLow=Integer.MAX_VALUE;
		public int ChartPaddingTop=15;
		public int ChartPaddingRight=15;
		public int ChartPaddingBottom=5;
		public int ChartPaddingLeft=10;
		public boolean FullWidth=false;
		public boolean ReverseData=false;
		protected Map<String,SerieOptions> mSeries = new LinkedHashMap<String,SerieOptions>();
		
		LineAxisOptions() {
			String[] sseries = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o"};
			for (int i=0;i<15;i++) {
				mSeries.put(sseries[i], new SerieOptions());
			}
		}
		
		public SerieOptions Serie(String serieIndex) {
			return mSeries.get(serieIndex.toLowerCase());
		}
	}
	
	/**
	 * Can not be created within B4J, But its properties and methods are available through another ABM class.
	 */
	public class SerieOptions implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 6952422731085111099L;
		public boolean ShowLine=true;
		public boolean ShowPoint=true;
		public boolean ShowArea=false;
		public int AreaBase=0;
		public String LineSmooth="Chartist.Interpolation.simple()";		
	}
	
	/**
	 * Can not be created within B4J, But its properties and methods are available through another ABM class.
	 */
	public class BarAxisOptions implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3861389901375306905L;
		public int AxisXOffset = 30;
		public String AxisXPosition = "end";
		public int AxisXLabelOffsetX = 0;
		public int AxisXLabelOffsetY = 0;
		public boolean AxisXShowLabel=true;
		public boolean AxisXShowGrid=true;
		public boolean AxisXOnlyInteger=false;
		public String AxisXLabelInterpolation="value";			
		public int AxisXScaleMinSpace=30;
		public int AxisYOffset = 40;
		public String AxisYPosition = "start";
		public int AxisYLabelOffsetX = 0;
		public int AxisYLabelOffsetY = 0;
		public boolean AxisYShowLabel=true;
		public boolean AxisYShowGrid=true;
		public boolean AxisYOnlyInteger=false;		
		public String AxisYLabelInterpolation="value";
		public int AxisYScaleMinSpace=20;
		public int FixedWidthPx=Integer.MAX_VALUE;
		public int FixedHeightPx=Integer.MAX_VALUE;
		public int High=Integer.MAX_VALUE;
		public int Low=Integer.MAX_VALUE;
		public int ChartPaddingTop=15;
		public int ChartPaddingRight=15;
		public int ChartPaddingBottom=5;
		public int ChartPaddingLeft=10;
		public int SeriesBarDistance=15;
		public boolean StackBars=false;
		public boolean HorizontalBars=false;
		public boolean DistributeSeries=false;
		public boolean ReverseData=false;
	}
	
	/**
	 * Can not be created within B4J, But its properties and methods are available through another ABM class.
	 */
	public class PieOptions implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3106712583666001136L;
		public int FixedWidthPx=Integer.MAX_VALUE;
		public int FixedHeightPx=Integer.MAX_VALUE;
		public int ChartPaddingTop=5;
		public int ChartPaddingRight=5;
		public int ChartPaddingBottom=5;
		public int ChartPaddingLeft=5;
		public int StartAngle=0;
		public int Total=Integer.MAX_VALUE;
		public Boolean IsDonut=false;
		public int DonutWidth=60;
		public boolean ShowLabel=true;
		public int LabelOffset=0;
		public String LabelPosition="inside";
		public String LabelInterpolation="value";
		public String LabelDirection="neutral";
		public boolean ReverseData=false;
	}
}
