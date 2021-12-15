package com.ab.abmaterial;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Random;

import anywheresoftware.b4a.BA;
//import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;

@Author("Alain Bailleul")  
@ShortName("ABMPivotTable")
@Events(values={"Loaded(Success As Boolean)","RendererChanged(newRenderer As String)","Clicked(records As String)"})
public class ABMPivotTable extends ABMComponent {
	private static final long serialVersionUID = -8577988457177503820L;
	protected ThemePivotTable Theme=new ThemePivotTable();
	protected String mDocumentURL="";
	protected int mHeightPx=0;
	protected boolean IsDirty=false;
	protected String mRows="";
	protected String mCols="";
	protected String mHides="";
	protected String mX="";
	protected String mY="";
	public String ForcedLanguage="en";
	public boolean UsesGoogleCharts=false;
	public boolean UsesSubTotals=false;
	protected String defTableType="";
	protected String defAggregatorType="";
	protected String defAggregatorValues="";
	protected boolean ReadOnly=false;	
	
	public boolean HideAllXYAxes=false;
	public boolean HideRenderer=false;
	public boolean HideAggregations = false;
	public boolean StickyHeader=false;
	public boolean MarkClickedLine=false;
	protected String LastName="";
	public boolean UnusedShowVertical=true;
	
	public int CollapseRowsAt=-1;
	
	public String CSVSperator=",";
	
	public String ONETWO_PER_TEXT="";
	public boolean ISONETWO=false;
	private int mZoom=100;
	
	public String RealCSVForExport="";
	
	public void Initialize(ABMPage page, String id, int heightPx, String documentURL, String columns, String rows, String hiddenFields, boolean readOnly, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_PIVOTTABLE;
		this.mHeightPx=heightPx;
		this.mDocumentURL=documentURL;
		this.mRows = rows;
		this.mCols = columns;
		this.mHides = hiddenFields;
		this.ReadOnly=readOnly;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.PivotTables.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.PivotTables.get(themeName.toLowerCase()).Clone();				
			}
		}	
		IsInitialized=true;
	}
	
	/**
	 * You can only use this initialization if you have set the cell height by page.Cell(x,y).SetFixedHeightFromBottom() or page.Cell(x,y).SetFixedHeight()!  
	 */
	public void InitializeAutoHeight(ABMPage page, String id, String documentURL, String columns, String rows, String hiddenFields, boolean readOnly, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_PIVOTTABLE;
		this.mHeightPx=-999999;
		this.mDocumentURL=documentURL;
		this.mRows = rows;
		this.mCols = columns;
		this.mHides = hiddenFields;
		this.ReadOnly=readOnly;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.PivotTables.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.PivotTables.get(themeName.toLowerCase()).Clone();				
			}
		}	
		IsInitialized=true;
	}
	
	public void InitializeEx(ABMPage page, String id, int heightPx, String documentURL, String columns, String rows, String hiddenFields, String noDropX, String noDropY, boolean readOnly, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_PIVOTTABLE;
		this.mHeightPx=heightPx;
		this.mDocumentURL=documentURL;
		this.mRows = rows;
		this.mCols = columns;
		this.mHides = hiddenFields;
		this.mX = noDropX;
		this.mY = noDropY;
		this.ReadOnly=readOnly;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.PivotTables.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.PivotTables.get(themeName.toLowerCase()).Clone();				
			}
		}	
		IsInitialized=true;
	}
	
	/**
	 * zoom in percentage 
	 */
	public void SetZoom(int zoom) {
		mZoom = zoom;
		
		String s = "rapsetzoom($('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').contents().find('body'), 'zoom" + mZoom + "')";
		Page.ws.Eval(s, null);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("PivotTable FirstRun: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	/**
	 * You can only use this initialization if you have set the cell height by page.Cell(x,y).SetFixedHeightFromBottom() or page.Cell(x,y).SetFixedHeight()!  
	 */
	public void InitializeAutoHeightEx(ABMPage page, String id, String documentURL, String columns, String rows, String hiddenFields, String noDropX, String noDropY, boolean readOnly, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_PIVOTTABLE;
		this.mHeightPx=-999999;
		this.mDocumentURL=documentURL;
		this.mRows = rows;
		this.mCols = columns;
		this.mHides = hiddenFields;
		this.mX = noDropX;
		this.mY = noDropY;
		this.ReadOnly=readOnly;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.PivotTables.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.PivotTables.get(themeName.toLowerCase()).Clone();				
			}
		}	
		IsInitialized=true;
	}
	
	/*
	 * tableType: use ABM.PIVOT_TABLETYPE_ constants
	 * aggregatorType: use ABM.PIVOT_AGGRTYPE_ constants
	 */
	public void SetPreselection(String tableType, String aggregatorType, String aggregatorValues) {
		defTableType=tableType;
		defAggregatorType=aggregatorType;		
		defAggregatorValues=aggregatorValues;
		
	}
	
	@Override
	protected void ResetTheme() {
		UseTheme(Theme.ThemeName);
	}
	
	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase() + "-parent";
	}
	
	public String GetCurrentRows() {
		return "";
	}
	
	public String GetCurrentColumns() {
		return "";
	}
	
	public void SetDocument(String documentURL, String columns, String rows, String hiddenFields) {
		this.mDocumentURL = documentURL;
		this.mRows = rows;
		this.mCols = columns;
		this.mHides = hiddenFields;
		this.IsDirty=true;		
	}
	
	public void SetDocumentEx(String documentURL, String columns, String rows, String hiddenFields, String noDropX, String noDropY) {
		this.mDocumentURL = documentURL;
		this.mRows = rows;
		this.mCols = columns;
		this.mHides = hiddenFields;
		this.mX = noDropX;
		this.mY = noDropY;
		this.IsDirty=true;		
	}
	
		
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}	
		
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.PivotTables.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.PivotTables.get(themeName.toLowerCase()).Clone();				
			}
		}
	}		
		
	@Override
	protected void CleanUp() {
		super.CleanUp();
	}
	
	@Override
	protected void RemoveMe() {
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent");
	}
	
	@Override
	protected void FirstRun() {
		
		Page.ws.Eval(BuildJavaScript(), null);
		SaveInLocalStorage(Page, "fileKeyCSV", RealCSVForExport);
		SaveInLocalStorage(Page, "fileKey", mDocumentURL);
		SaveInLocalStorage(Page, "rowKey", mRows);
		SaveInLocalStorage(Page, "colKey", mCols);
		SaveInLocalStorage(Page, "hideKey", mHides);
		SaveInLocalStorage(Page, "nodropXKey", mX);
		SaveInLocalStorage(Page, "nodropYKey", mY);
		SaveInLocalStorage(Page, "valsKey", this.defAggregatorValues);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("PivotTable FirstRun: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
		IsDirty=true; // exceptional
		Refresh(); // exceptional
		super.FirstRun();
	}
	
	protected String BuildJavaScript() {
		StringBuilder s = new StringBuilder();
	
		return s.toString();
	}
	
	
	public void ExportToXLS(String fileName, String srcDecimalPoint, String srcThousandsPoint, String tgtDecimalPoint, String tgtThousandsPoint) {
		
		String optionsJson = "{type:'excel', excelstyles:['border-bottom', 'border-top', 'border-left', 'border-right', 'background-color','vertical-align','white-space'],htmlContent: false, fileName: '" + fileName + "', numbers: {html: {decimalMark: '" + srcDecimalPoint + "',thousandsSeparator: '" + srcThousandsPoint + "'},output: {decimalMark: '" + tgtDecimalPoint + "',thousandsSeparator: '" + tgtThousandsPoint + "'}}}";
		String s = "";
		
		s += "$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').contents().find('.pvtTable').tableExport(" + optionsJson + ");";
		Page.ws.Eval(s, null);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("PivotTable ExportToXLS: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
				}
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	
	public void ExportToXLSX(String fileName, String srcDecimalPoint, String srcThousandsPoint, String tgtDecimalPoint, String tgtThousandsPoint) {
		String optionsJson = "{type:'xlsx', excelstyles:['border-bottom', 'border-top', 'border-left', 'border-right', 'background-color','vertical-align','white-space'],htmlContent: false, fileName: '" + fileName + "', numbers: {html: {decimalMark: '" + srcDecimalPoint + "',thousandsSeparator: '" + srcThousandsPoint + "'},output: {decimalMark: '" + tgtDecimalPoint + "',thousandsSeparator: '" + tgtThousandsPoint + "'}}}";
		String s = "";
		
		s += "$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').contents().find('.pvtTable').tableExport(" + optionsJson + ");";
		Page.ws.Eval(s, null);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("PivotTable ExportToXLSX: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
				}
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	
	/**
	 * Let's the user download the raw csv of json data 
	 */
	public void ExportRAW(String fileName) {
		StringBuilder s = new StringBuilder();
		String optionsJson = "";
		if (CSVSperator.equals(",")) {
			optionsJson = "{type:'abm', planid: '" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "', fileName: '" + fileName + "'}";
		} else {
			optionsJson = "{type:'abm', planid: '" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "', sep:'" + CSVSperator + "', fileName: '" + fileName + "'}";
		}
		
		s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').contents().find('.pvtTable').tableExport(" + optionsJson + ");");
		Page.ws.Eval(s.toString(), null);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("PivotTable ExportToRaw: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
				}
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	
	/**
	 * Needs:
	 * 
	 *  page.AddExtraJavaScriptFile("https://cdnjs.cloudflare.com/ajax/libs/jspdf/1.5.3/jspdf.min.js")
	 *  page.AddExtraJavaScriptFile("https://html2canvas.hertzen.com/dist/html2canvas.js")
	 */
	public void ExportPDF(String fileName) {
		
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();		
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		Params.Add(fileName);
		Page.ws.RunFunction("PrintToPDF", Params);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("PivotTable ExportToRaw: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
				}
		} catch (IOException e) {
			//e.printStackTrace();
		}		
	}
		
	protected void SaveInLocalStorage(ABMPage page, String key, String value) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();		
		Params.Add(key);
		Params.Add(value);
		page.ws.RunFunction("customsaveinlocalstorage", Params);		
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		super.Refresh();
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		j.SetProp("class", BuildClass());
		if (IsDirty) {
			Page.ws.Eval(BuildJavaScript(), null);
			SaveInLocalStorage(Page, "fileKeyCSV", RealCSVForExport);
			SaveInLocalStorage(Page, "fileKey", mDocumentURL);
			SaveInLocalStorage(Page, "rowKey", mRows);
			SaveInLocalStorage(Page, "colKey", mCols);
			SaveInLocalStorage(Page, "hideKey", mHides);
			SaveInLocalStorage(Page, "nodropXKey", mX);
			SaveInLocalStorage(Page, "nodropYKey", mY);
			SaveInLocalStorage(Page, "valsKey", this.defAggregatorValues);
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("PivotTable Refresh 1: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}
			
			Random randomGenerator = new Random();
			int randomInt = randomGenerator.nextInt(100000);
			LocalDate todayD = LocalDate.now(ZoneId.of("UTC"));
			LocalTime nowD = LocalTime.now(ZoneId.of("UTC"));
			long today = todayD.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
			long now = nowD.toNanoOfDay();
			
			
			File directory = new File(Page.CurrentDir);
			for (File f : directory.listFiles()) {
				if (f.getName().startsWith("abmpivot" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase()) && f.getName().compareTo("abmpivot" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + today + "_") < 0) {
			        f.delete();
			    }
			}	
						
			LastName="";
			
			if (!mDocumentURL.equals("")) {
				LastName = "abmpivot" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + today + "_" + now + "-" + randomInt;
				String viewer = WriteViewer();
				BufferedWriter writer = null;
			    try {        	        	
			       	File buildFile = new File(Page.CurrentDir, LastName + ".html");
			       	
			        writer = new BufferedWriter(new FileWriter(buildFile));
			        writer.write(viewer);
			    } catch (Exception e) {
			        e.printStackTrace();
			    } finally {
			    	try {
			           writer.close();
			        } catch (Exception e) {
			        }
			    }
			    if (ABMaterial.GZip) {
			        	File buildFile = new File(Page.CurrentDir, LastName + ".html.gz");
			        	buildFile.delete();
			        	buildFile = new File(Page.CurrentDir, LastName + ".html");
			        	if (buildFile.length()>ABMaterial.minGZipSize) {
			        		Page.GzipIt(buildFile.getAbsolutePath());
			        	}
			    }
			    ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "src", "./" + LastName + ".html");			
			} else {
				ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "src", "");
			}
		}
		IsDirty=false;
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("PivotTable Refresh 2: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
					}
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}	
	}
	
	public String GetCurrentTempNameAfterBuild() {
		return LastName;
	}	
	
	@Override
	protected String Build() {
		if (Theme.ThemeName.equals("default")) {
			Theme.Colorize(Page.CompleteTheme.MainColor);
		}
		StringBuilder s = new StringBuilder();
		
		if (Page.ws!=null) {
			String sFile= "";
			if (Page.mStaticFilesFolder.equals("")) {
				sFile = anywheresoftware.b4a.objects.streams.File.getDirApp().replace("\\", "/") + Page.ws.getUpgradeRequest().getRequestURI().replace("/ws/", "/www/");
			} else {
				sFile = Page.mStaticFilesFolder + "/" + Page.mAppName + "/" + Page.Name; 
			}
			File f = new File(sFile);
			Page.CurrentDir = f.getAbsolutePath();
		}
		
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(100000);
		LocalDate todayD = LocalDate.now(ZoneId.of("UTC"));
		LocalTime nowD = LocalTime.now(ZoneId.of("UTC"));
		long today = todayD.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
		long now = nowD.toNanoOfDay();
		
		
		File directory = new File(Page.CurrentDir);
		for (File f : directory.listFiles()) {
			if (f.getName().startsWith("abmpivot" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase()) && f.getName().compareTo("abmpivot" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + today + "_") < 0) {
		        f.delete();
		    }
		}
				
		if (!mDocumentURL.equals("")) {
			LastName = "abmpivot" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + today + "_" + now + "-" + randomInt;
			String viewer = WriteViewer();
			BufferedWriter writer = null;
	        try {        	        	
	        	File buildFile = new File(Page.CurrentDir, LastName + ".html");
	        	
	            writer = new BufferedWriter(new FileWriter(buildFile));
	            writer.write(viewer);
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                writer.close();
	            } catch (Exception e) {
	            }
	        }
	        if (ABMaterial.GZip) {
	        	File buildFile = new File(Page.CurrentDir, LastName + ".html.gz");
	        	buildFile.delete();
	        	buildFile = new File(Page.CurrentDir, LastName + ".html");
	        	if (buildFile.length()>ABMaterial.minGZipSize) {
	        		Page.GzipIt(buildFile.getAbsolutePath());
	        	}
	        }
		}
		
        if (mHeightPx>-999999) {
        	s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent\" class=\"");
        	s.append(BuildClass());
        	s.append("\">");
        	if (!mDocumentURL.equals("")) {
        		s.append("<iframe id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"width:100%;height:" + mHeightPx + "px;border: 0px\" src=\"./" + LastName + ".html\"></iframe>");
        	} else {
        		s.append("<iframe id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"width:100%;height:" + mHeightPx + "px;border: 0px\" src=\"\"></iframe>");
        	}
        	s.append("</div>");
        } else {
        	s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent\" class=\"");
        	s.append(BuildClass());
        	s.append("\" style=\"height: 100%\">");
        	if (!mDocumentURL.equals("")) {
        		s.append("<iframe id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"width:100%;height:100%;border: 0px\" src=\"./" + LastName + ".html\"></iframe>");
        	} else {
        		s.append("<iframe id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"width:100%;height:100%;border: 0px\" src=\"\"></iframe>");
        	}
        	s.append("</div>");
        }
		IsBuild=true;
		return s.toString();
	}
	
	
	protected String WriteViewer() {
		StringBuilder s = new StringBuilder();
		String styleCenter="";
		
		s.append("<!DOCTYPE html>");
		s.append("<html " + styleCenter + ">");
		s.append("<head>");
		s.append("<meta charset=\"utf-8\">");
		s.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">");
		s.append("<meta name=\"google\" content=\"notranslate\">");
		s.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">");
		if (mDocumentURL.equals("")) {
			s.append("</head>");
			s.append("<body class=\"zoom" + mZoom + "\">");
			s.append("</body>");
			s.append("</html>");
			return s.toString();
		}
		if (ABMaterial.CDN) {
			s.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + ABMaterial.mCDNUrl + "css/abmpivot.min.css\">");
			if (UsesSubTotals) {
				s.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + ABMaterial.mCDNUrl + "css/subtotal.min.css\">");
			}
			s.append("<script type=\"text/javascript\" src=\"" + ABMaterial.mCDNUrl + "js/jquery-1.11.2.min.js\"></script>");
			s.append("<script type=\"text/javascript\" src=\"" + ABMaterial.mCDNUrl + "js/jquery-ui.min.js\"></script>");
			s.append("<script type=\"text/javascript\" src=\"" + ABMaterial.mCDNUrl + "js/jquery.csv.min.js\"></script>");
			if (UsesGoogleCharts) {
				s.append("<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>");
			}		
			s.append("<script type=\"text/javascript\" src=\"" + ABMaterial.mCDNUrl + "js/abmpivot" + ABMaterial.JSAbmPivot + ".min.js\"></script>");
			if (!ForcedLanguage.equalsIgnoreCase("en")) {
				s.append("<script type=\"text/javascript\" src=\"" + ABMaterial.mCDNUrl + "js/pivot." + ForcedLanguage + ".min.js\"></script>");
			}
			s.append("<script type=\"text/javascript\" src=\"" + ABMaterial.mCDNUrl + "js/jquery.ui.touch-punch.min.js\"></script>");
			if (UsesSubTotals) {
				s.append("<script type=\"text/javascript\" src=\"" + ABMaterial.mCDNUrl + "js/subtotal.min.js\"></script>");
			}
			if (UsesGoogleCharts) {	
				s.append("<script type=\"text/javascript\" src=\"" + ABMaterial.mCDNUrl + "js/gchart_renderers.min.js\"></script>");
			}
		} else {
			s.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/abmpivot.min.css\">");
			if (UsesSubTotals) {
				s.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/subtotal.min.css\">");
			}
			s.append("<script type=\"text/javascript\" src=\"../../js/jquery-1.11.2.min.js\"></script>");
			s.append("<script type=\"text/javascript\" src=\"../../js/jquery-ui.min.js\"></script>");
			s.append("<script type=\"text/javascript\" src=\"../../js/jquery.csv.min.js\"></script>");
			if (UsesGoogleCharts) {
				s.append("<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>");
			}		
			s.append("<script type=\"text/javascript\" src=\"../../js/abmpivot" + ABMaterial.JSAbmPivot + ".min.js\"></script>");
			if (!ForcedLanguage.equalsIgnoreCase("en")) {
				s.append("<script type=\"text/javascript\" src=\"../../js/pivot." + ForcedLanguage + ".min.js\"></script>");
			}
			s.append("<script type=\"text/javascript\" src=\"../../js/jquery.ui.touch-punch.min.js\"></script>");
			if (UsesSubTotals) {
				s.append("<script type=\"text/javascript\" src=\"../../js/subtotal.min.js\"></script>");
			}
			if (UsesGoogleCharts) {	
				s.append("<script type=\"text/javascript\" src=\"../../js/gchart_renderers.min.js\"></script>");
			}
		}
		String Mark="";
		if (MarkClickedLine) {
			Mark="$(\".pvtVal\").on(\"click\",function(t){t.preventDefault(),$(this).hasClass(\"pivothover\")?$(\".pvtVal, .pvtTotal\").removeClass(\"pivothover\"):($(\".pvtVal\").removeClass(\"pivothover\"),$(\".pvtTotal\").removeClass(\"pivothover\"),$(this).parent().children(\"td\").each(function(t){$(this).addClass(\"pivothover\")}))});";
		}
		if (StickyHeader) {
			s.append("<script src=\"https://unpkg.com/sticky-table-headers\"></script>");			
			s.append("<script>function FixWait(){$(\".pvtTable\").waitUntilExists(Fix,!0)}function Fix(){$(\".pvtTable\").stickyTableHeaders();" + Mark + "}!function(s,r){var o={},u=function(t){o[t]&&(r.clearInterval(o[t]),o[t]=null)},c=\"waitUntilExists.found\";s.fn.waitUntilExists=function(t,n,i){var a=this.selector,e=s(a),l=e.not(function(){return s(this).data(c)});return\"remove\"===t?u(a):(l.each(t).data(c,!0),n&&e.length?u(a):i||(o[a]=r.setInterval(function(){e.waitUntilExists(t,n,!0)},500))),e}}(jQuery,window);</script>");
		}
		s.append("<style>");
		s.append(".pvtAxisContainer, .pvtVals {");
		s.append("background: " + ABMaterial.GetColorStrMap(Theme.DragDropAreaColor, ABMaterial.INTENSITY_LIGHTEN4) + ";"); 
		s.append("}");
		s.append("table.pvtTable tr th, table.pvtTable tr th {");
		s.append("background-color: " + ABMaterial.GetColorStrMap(Theme.DragDropAreaColor, ABMaterial.INTENSITY_LIGHTEN5) + ";");
		s.append("border: 1px solid " + ABMaterial.GetColorStrMap(Theme.DragDropAreaColor, ABMaterial.INTENSITY_LIGHTEN4) + ";");
		s.append("white-space: nowrap !important;");
		s.append("}");
		s.append("table.pvtTable tr td, table.pvtTable tr td {");
		s.append("white-space: nowrap !important;");
		s.append("}");
		s.append(".pvtUnused {");
		s.append("background-color: " + ABMaterial.GetColorStrMap(Theme.DragDropAreaColor, ABMaterial.INTENSITY_LIGHTEN3) + ";");
		s.append("border: 1px solid " + ABMaterial.GetColorStrMap(Theme.DragDropAreaColor, ABMaterial.INTENSITY_LIGHTEN2) + ";");
		s.append("}");
		s.append("select:focus {");
		s.append("outline: 0;");
		s.append("}");
		if (HideRenderer) {
			s.append(".pvtRenderer {display: none}");
		}
		if (HideAllXYAxes) {	
			if (HideAggregations) {
				s.append(".pvtAxisContainer, .pvtVals { display: none;padding: 0px}");
			} else {
				s.append(".pvtAxisContainer { display: none;padding: 0px}");
			}
		} else {
			if (HideAggregations) {
				s.append(".pvtVals { visibility: hidden}");
			}
		}
		if (HideRenderer && HideAllXYAxes) {
			s.append("body{margin: 0px} body tr td {padding: 0px}");
		}
		s.append(".pivothover{border-bottom: 2px solid " + ABMaterial.GetColorStrMap(Theme.MarkerColor, Theme.MarkerColorIntensity) + " !important;}");
		
		for (int zz=50;zz<=200;zz+=5) {
			s.append(" .zoom" + zz + " {");
			double zzDbl = (zz/100.0);
			s.append("-moz-transform: scale(" + zzDbl + ");");
			s.append("-ms-transform: scale(" + zzDbl + ");");
			s.append("-webkit-transform: scale(" + zzDbl + ");");
			s.append("-o-transform: scale(" + zzDbl + ");");
			s.append("transform: scale(" + zzDbl + ");");
			s.append("-moz-transform-origin: left top;");
			s.append("-ms-transform-origin: left top;");
			s.append("-webkit-transform-origin: left top;");
			s.append("transform-origin: left top;");
			s.append("}");    			
		}
		s.append("}");
		
		s.append("@media only print {");
		s.append("-moz-transform: unset;");
		s.append("-ms-transform: unset;");
		s.append("-webkit-transform: unset;");
		s.append("-o-transform: unset;");
		s.append("transform: unset;");
		s.append("-moz-transform-origin: unset;");
		s.append("-ms-transform-origin: unset;");
		s.append("-webkit-transform-origin: unset;");
		s.append("transform-origin: unset;");
		s.append("}");
		
		s.append("</style>");
		s.append("<script>");
		s.append("var raw;");
		s.append("var rawType ='';");
				
		s.append("</script>");
		s.append("</head>");
		if (ReadOnly) {
			s.append("<body style=\"background-color:" + ABMaterial.GetColorStrMap(Theme.BackColor, Theme.BackColorIntensity) + ";pointer-events: none;\">");
		} else {
			s.append("<body style=\"background-color:" + ABMaterial.GetColorStrMap(Theme.BackColor, Theme.BackColorIntensity) + "\">");
		}
		s.append("<script type=\"text/javascript\">");		
		if (UsesGoogleCharts) {
			s.append("google.load(\"visualization\", \"1\", {packages:[\"corechart\", \"charteditor\"]});");		
		}
		if (!mDocumentURL.equals("")) {
			s.append("$(function(){");
			String googleCharts="";
			if (UsesGoogleCharts) {
				googleCharts = ",$.pivotUtilities.gchart_renderers";
			}		
			if (UsesSubTotals) {
				if (UsesGoogleCharts) {
					s.append("var renderers = $.extend($.pivotUtilities.subtotal_renderers" + googleCharts + ");");
				} else {
					s.append("var renderers = $.pivotUtilities.subtotal_renderers;");
				}
				s.append("var dataClass=$.pivotUtilities.SubtotalPivotData;");
			} else {
				if (UsesGoogleCharts) {
					s.append("var renderers = $.extend($.pivotUtilities.renderers" + googleCharts + ");");
				} else {
					s.append("var renderers = $.pivotUtilities.renderers;");
				}
			}
			s.append("var fileName = localStorage.getItem('fileKey');");
			s.append("var tmpRows = localStorage.getItem('rowKey').split(\",\");");
			s.append("var tmpCols = localStorage.getItem('colKey').split(\",\");");
			s.append("var tmpHides = localStorage.getItem('hideKey').split(\",\");");
			s.append("var tmpVals = localStorage.getItem('valsKey').split(\",\");");
			
			s.append("var tmpnodropX = localStorage.getItem('nodropXKey').split(\",\");");
			s.append("var tmpnodropY = localStorage.getItem('nodropYKey').split(\",\");");
			
			s.append("$(function(){");
			if (mDocumentURL.toLowerCase().endsWith(".json")) {
				s.append("$.getJSON(fileName, function(mps) {");
				s.append("raw = mps;");
				s.append("rawType='json';");
				s.append("var input = mps;");
			} else {
				s.append("$.get(fileName, function(mps) {");
				s.append("raw = mps;");
				s.append("rawType='csv';");
				s.append("var input = $.csv.toArrays(mps);");
			}
		
			s.append("$('#output').pivotUI(input, {");
			s.append("renderers: renderers,");
			if (UsesSubTotals) {
				s.append("dataClass: dataClass,");
			}			
			
			s.append("rows: tmpRows,");
			s.append("cols: tmpCols,");
			s.append("hides: tmpHides,");
			if (ISONETWO) {
				s.append("hiddenAttributes: ['PER'],");
				s.append("derivedAttributes: {'" + ONETWO_PER_TEXT + "': $.pivotUtilities.derivers.dateFormat('PER', '%d/%m/%y', false)},");
			}
			s.append("nodropX: tmpnodropX,");
			s.append("nodropY: tmpnodropY,");
			if (ISONETWO) {
				s.append("sorters: {");
				s.append("\"" + ONETWO_PER_TEXT + "\": function(a, b) {");
				s.append("const [di, me, an] = a.split('/');");
				s.append("const [di2, me2, an2] = b.split('/');");
				s.append("var da = new Date(`${an}-${me}-${di}`).getTime();");
				s.append("var db = new Date(`${an2}-${me2}-${di2}`).getTime();");
				s.append("return da < db ? -1 : da > db ? 1 : 0");
				s.append("}");
				s.append("},");
			}
			s.append("unusedAttrsVertical: " + UnusedShowVertical + ",");
			StringBuilder rendererChanged=new StringBuilder();
			rendererChanged.append("function rendererChanged(value) {");
			rendererChanged.append("window.parent.postMessage({");
			rendererChanged.append("'func': 'RaiseChanged',");
			rendererChanged.append("'id': '" + ID.toLowerCase() +"',");
			rendererChanged.append("'success': value");
			rendererChanged.append("}, \"*\");");
			rendererChanged.append("}");
			s.append("rendererChanged: " + rendererChanged.toString() + ",");
			String collapseClick="";
			if (CollapseRowsAt!=-1) {
				collapseClick=";var cnt = $('.tableFloatingHeaderOriginal > tr > th').length;$($('.tableFloatingHeaderOriginal > tr > th').get().reverse()).each(function() {if (cnt > " + CollapseRowsAt + ") {$(this)[0].click();}cnt--;});";
			}
			if (StickyHeader) {
				s.append("onRefresh: function(config){FixWait()" + collapseClick + "},");
			}
		
			if (!defTableType.equals("")) {
				String defTableType2=defTableType;
				if (UsesSubTotals) {
					switch(defTableType) {
					case ABMaterial.PIVOT_TABLETYPE_TABLE:
						defTableType2 = ABMaterial.PIVOT_TABLETYPE_TABLEST;
						break;
					case ABMaterial.PIVOT_TABLETYPE_TABLEBARCHART:
						defTableType2 = ABMaterial.PIVOT_TABLETYPE_TABLEBARCHARTST;
						break;
					case ABMaterial.PIVOT_TABLETYPE_HEATMAP:
						defTableType2 = ABMaterial.PIVOT_TABLETYPE_HEATMAPST;
						break;
					case ABMaterial.PIVOT_TABLETYPE_ROWHEATMAP:
						defTableType2 = ABMaterial.PIVOT_TABLETYPE_ROWHEATMAPST;
						break;
					case ABMaterial.PIVOT_TABLETYPE_COLHEATMAP:
						defTableType2 = ABMaterial.PIVOT_TABLETYPE_COLHEATMAP;
						break;
					}
				}
				s.append("rendererName: '" + defTableType2 + "',");
			}
			if (!defAggregatorType.equals("")) {
				s.append("aggregatorName: '" + defAggregatorType + "',");
				if (!defAggregatorValues.equals("")) {
					s.append("vals: tmpVals,");
				}
			}
			
			s.append("heatcol: '" + ABMaterial.GetColorStrMap(Theme.HeatMapColor, ABMaterial.INTENSITY_NORMAL) + "',");
			s.append("barchartcol: '" + ABMaterial.GetColorStrMap(Theme.BarChartColor, Theme.BarChartColorIntensity) + "'");		
			s.append("},false,'" + ForcedLanguage + "');");
			s.append("window.parent.postMessage({");
			s.append("'func': 'RaiseLoaded',");
			s.append("'id': '" + ID.toLowerCase() +"',");
			s.append("'success': true");
			s.append("}, \"*\");");
			if (HideRenderer) {
				s.append("$('.pvtRenderer').closest('tr').css({'display': 'none'});");
			}
			s.append("}).fail(function (jqxhr, textStatus, error) {");
			s.append("window.parent.postMessage({");
			s.append("'func': 'RaiseLoaded',");
			s.append("'id': '" + ID.toLowerCase() +"',");
			s.append("'success': false");
			s.append("}, \"*\");");
			s.append("});");
			s.append("});"); 
			s.append("});");
			
		}
		s.append("</script>");
		s.append("<div id=\"output\"></div>");
		s.append("</body>");
		s.append("</html>");		
		
		return s.toString();
	}
		
	@Hide
	@Override
	protected void Prepare() {	
		this.IsBuild = true;
		this.IsDirty=true;
		Refresh();
	}
	
	@Hide
	protected String BuildClass() {			
		StringBuilder s = new StringBuilder();
		s.append(super.BuildExtraClasses());
		s.append(mVisibility + " ");	
		s.append(mIsPrintableClass);		
		s.append(mIsOnlyForPrintClass);
		return s.toString(); 
	}
	
	@Hide
	protected String BuildBody() {		
		return "";
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMPivotTable c = new ABMPivotTable();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;
		c.Theme = Theme.Clone();
		c.Type = Type;
		c.mVisibility = mVisibility;		
		return c;
	}
}
