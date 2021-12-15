package com.ab.abmaterial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import com.ab.abmaterial.ThemeTable.TableCell;

import anywheresoftware.b4a.BA;
//import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;
import anywheresoftware.b4j.object.WebSocket.SimpleFuture;

@Author("Alain Bailleul")  
@ShortName("ABMTable")
@Events(values={"Clicked(PassedRowsAndColumns as List)","Changed(Params as Map)", "SortChanged(DataField as String, Order as String)"})
public class ABMTable extends ABMComponent {
	
	private static final long serialVersionUID = 7178087509237552393L;
	protected ThemeTable Theme=new ThemeTable();
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	public boolean IsBordered=false;
	public boolean IsResponsive=false;
	public int HeaderHeightPx=0;
	protected TableRow header=null;	
	protected TableRow footer=null;
	public boolean IsScrollable=false;
	protected Map<String, TableRow> Rows = new LinkedHashMap<String, TableRow>();
	protected int FooterColSpan=0;
	protected List<Integer> colWidths = new ArrayList<Integer>();
	
	protected List<String> colVisibles = new ArrayList<String>();
	protected List<Boolean> colSortables = new ArrayList<Boolean>();
	protected List<String> colDataFields = new ArrayList<String>();
	protected boolean IsSortable=false;
	public boolean IgnoreFormattingCodes=false;
	protected boolean IsInteractive=false;
	protected String ParentArrayName="";
	protected String mActiveRowId="";	
	protected boolean UsingQueriesToSort=false;
	
	public boolean IsTextSelectable=true;
	
	public void Initialize(ABMPage page, String id, boolean isSortable, boolean usingQueriesToSort, boolean isInteractive, String themeName) {
		this.ID = id;			
		this.Page = page;		
		this.Type = ABMaterial.UITYPE_TABEL;
		this.IsScrollable=false;
		this.IsSortable=isSortable;
		this.IsInteractive=isInteractive;
		this.UsingQueriesToSort=usingQueriesToSort;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Tables.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Tables.get(themeName.toLowerCase()).Clone();				
			}
		}
		IsInitialized=true;
	}	
	
	/**
	 * Depreciated.  Better use Initialize, set IsScrollable=true and then use SetColumnWidths() to set the widths of the columns
	 */
	public void InitializeScrollable(ABMPage page, String id, boolean isSortable, boolean usingQueriesToSort, boolean isInteractive, anywheresoftware.b4a.objects.collections.List Widths, String themeName) {
		this.ID = id;			
		this.Page = page;		
		this.Type = ABMaterial.UITYPE_TABEL;
		this.IsScrollable=true;
		
		this.IsSortable=isSortable;
		this.IsInteractive=isInteractive;
		this.UsingQueriesToSort=usingQueriesToSort;
		if (Widths!=null && Widths.IsInitialized()) {
			for (int i=0;i<Widths.getSize();i++) {
				colWidths.add((int)Widths.Get(i));			
			}
		}
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Tables.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Tables.get(themeName.toLowerCase()).Clone();				
			}
		}
		IsInitialized=true;
	}
	
	@Override
	protected void ResetTheme() {
		UseTheme(Theme.ThemeName);
	}
	
	@Override
	protected String RootID() {
		if (!IsScrollable) {
			return ArrayName.toLowerCase() + ID.toLowerCase() + "-nscr";
		} else {
			return ArrayName.toLowerCase() + ID.toLowerCase();
		}
	}
	
	public void ScrollTo(String uniqueId, boolean smooth) {
		if (IsScrollable) {
			anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
			Params.Initialize();
			Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
			Params.Add(uniqueId);
			if (smooth) {
				Params.Add("1");
			} else {
				Params.Add("0");
			}
			Page.ws.RunFunction("tablescrollto", Params);
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Table ScrollTo: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
			
			
		}
	}
	
	/**
	 * Make sure this list has an equal number of items as there as columns! (boolean)
	 */
	public void SetColumnVisible(anywheresoftware.b4a.objects.collections.List Visibles) {
		colVisibles.clear();
		for (int i=0;i<Visibles.getSize();i++) {
			String v = "";
			if (Visibles.Get(i) instanceof Boolean) {
				Boolean b = (Boolean)Visibles.Get(i);
				if (b) {
					v = ABMaterial.VISIBILITY_ALL;
				} else {
					v = ABMaterial.VISIBILITY_HIDE_ALL;
				}
			} else {
				v = (String)Visibles.Get(i);
				if (v.toLowerCase().equals("true")) {
					v = ABMaterial.VISIBILITY_ALL;
				}
				if (v.toLowerCase().equals("false")) {
					v = ABMaterial.VISIBILITY_HIDE_ALL;
				}
			}
			
			colVisibles.add(v);			
		}
	}
	
	/**
	 * Make sure this list has an equal number of items as there as columns! (boolean)
	 */
	public void SetColumnSortable(anywheresoftware.b4a.objects.collections.List Sortables) {
		colSortables.clear();
		for (int i=0;i<Sortables.getSize();i++) {
			colSortables.add((boolean)Sortables.Get(i));			
		}
	}
	
	/**
	 * Make sure this list has an equal number of items as there as columns! (String)
	 * This is used when the user clicks on a column head to sort to return in the SortChanged() event
	 * and the GetSortColumn() and SetSortColumn() methods. 
	 */
	public void SetColumnDataFields(anywheresoftware.b4a.objects.collections.List dataFields) {
		colDataFields.clear();
		for (int i=0;i<dataFields.getSize();i++) {
			colDataFields.add((String)dataFields.Get(i));			
		}
	}
	
	/**
	 * 
	 * Returns the fieldname and optional space + DESC. e.g. 'fldName DESC'
	 */
	public String GetSortColumn() {
		String lastSort = InnerGetSortColumn(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		String Order="";
		if (lastSort!=null) {
			if (lastSort.startsWith("DESC_")) {
				Order="DESC";
				lastSort=lastSort.substring(5);
			}
		
			if (!Order.equals("")) {
				return lastSort + " " + Order;
			} else {
				return lastSort;
			}
		} else {
			return "";
		}
	}	
	
	/**
	 * Use fieldname and optional space + DESC. e.g. 'fldName DESC'
	 */
	public void SetSortColumn(String dataField) {
		String Order="";
		if (dataField.endsWith(" DESC")) {
			Order = "DESC";
			dataField = dataField.substring(0, dataField.length() - 5).trim();
		} 
		if (Page!=null) {			
			anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
			Params.Initialize();
			Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
			Params.Add(dataField);
			Params.Add(Order);
			Page.ws.RunFunction("setsortcolumn", Params);
		}
	}
	
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;		
	}
	
	public void SetActiveRow(String uniqueId) {
		mActiveRowId=uniqueId;
		ABMaterial.SetActiveTableRow(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), uniqueId);
	}
	
	public String GetActiveRow() {
		
		mActiveRowId = GetActiveTableRow(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		return mActiveRowId;
	}	
	
	@Hide
	public String GetActiveTableRow(String ID) {
		String s = "";
		if (Page.ws.getOpen()) {
			anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		
			Params.Initialize();
			Params.Add(ID.toLowerCase());				
			SimpleFuture j = Page.ws.RunFunctionWithResult("getactivetablerow", Params);			
			if (j!=null) {
				try {
					s = (String) j.getValue();			
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (TimeoutException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return s;
	}
	
	@Hide
	public String InnerGetSortColumn(String ID) {
		String s = "";
		if (Page!=null) {
			if (Page.ws.getOpen()) {
				anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		
				Params.Initialize();
				Params.Add(ID.toLowerCase());				
				SimpleFuture j = Page.ws.RunFunctionWithResult("getsortcolumn", Params);				
				if (j!=null) {
					try {			
						s = (String) j.getValue();			
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (TimeoutException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return s;
	}
	
	
	public void SetHeaders(anywheresoftware.b4a.objects.collections.List HeaderValues) {
		header=new TableRow();	
		header.id="header";
		for (int i=0;i<HeaderValues.getSize();i++) {
			if (HeaderValues.Get(i) instanceof String) {
				TableValue tv = new TableValue(header.id + "_" + i + "__", null, (String) HeaderValues.Get(i) , false);
				tv.theme = "defaultheaderfooter";
				header.values.put(header.id + "_" + i + "__", tv);				
			} else {
				TableValue tv = new TableValue(header.id + "_" + i + "__", (ABMComponent)HeaderValues.Get(i), "", false);
				tv.theme = "defaultheaderfooter";
				header.values.put(header.id + "_" + i + "__", tv);
			}
		}	
	}
	
	public void SetHeaderHeights(anywheresoftware.b4a.objects.collections.List HeaderHeights) {
		for (int i=0;i<HeaderHeights.getSize();i++) {
			int th = (int) HeaderHeights.Get(i);
			header.values.get(header.id + "_" + i + "__").height = th;
			
		}	
	}
	
	public void SetColumnWidths(anywheresoftware.b4a.objects.collections.List ColumnWidths) {
		colWidths = new ArrayList<Integer>();
		for (int i=0;i<ColumnWidths.getSize();i++) {
			int th = (int) ColumnWidths.Get(i);
			
			colWidths.add(th);
		}	
	}
	
	public void SetHeaderThemes(anywheresoftware.b4a.objects.collections.List themeNames) {
		for (int i=0;i<themeNames.getSize();i++) {
			String th = (String) themeNames.Get(i);
			if (!th.equals("")) {
				header.values.get(header.id + "_" + i + "__").theme = th.toLowerCase();
			}
		}
	}
	
	/**
	 * Shortcut method if your footer only contains one cell 
	 */
	public void SetFooter(Object FooterValue, int colSpanWidth, String footerTheme) {
		footer=new TableRow();	
		footer.id="footer";
		this.FooterColSpan=colSpanWidth;
		if (FooterValue instanceof String) {
			footer.values.put(footer.id + "_0__", new TableValue(footer.id + "_0__", null, (String) FooterValue,  false));
			if (!footerTheme.equals("")) {
				footer.values.get(footer.id + "_0__").theme = footerTheme;
			} else {
				footer.values.get(footer.id + "_0__").theme = "defaultheaderfooter";
			}
		} else {
			footer.values.put(footer.id + "_0__", new TableValue(footer.id + "_0__", (ABMComponent)FooterValue, "", false));
			if (!footerTheme.equals("")) {
				footer.values.get(footer.id + "_0__").theme = footerTheme;
			} else {
				footer.values.get(footer.id + "_0__").theme = "defaultheaderfooter";
			}
		}			
	}
	
	public void SetFooters(anywheresoftware.b4a.objects.collections.List FooterValues) {
		footer=new TableRow();	
		footer.id="footer";
		for (int i=0;i<FooterValues.getSize();i++) {
			if (FooterValues.Get(i) instanceof String) {
				footer.values.put(footer.id + "_" + i + "__", new TableValue(footer.id + "_" + i + "__", null, (String) FooterValues.Get(i), false));
			} else {
				footer.values.put(footer.id + "_" + i + "__", new TableValue(footer.id + "_" + i + "__", (ABMComponent)FooterValues.Get(i), "", false));
			}
		}	
	}
	
	public void SetFooterThemes(anywheresoftware.b4a.objects.collections.List themeNames) {
		for (int i=0;i<themeNames.getSize();i++) {
			String th = (String) themeNames.Get(i);
			if (!th.equals("")) {
				footer.values.get(footer.id + "_" + i + "__").theme = th.toLowerCase();
			}
		}
	}
	
	public void AddRow(String uniqueId, anywheresoftware.b4a.objects.collections.List RowValues) {
		TableRow row =new TableRow();
		row.UniqueId=uniqueId;
		int rCounter = Rows.size();
		row.id="_" + rCounter;
		for (int i=0;i<RowValues.getSize();i++) {
			if (RowValues.Get(i) instanceof String) {
				row.values.put(row.id + "_" + i + "__", new TableValue(row.id + "_" + i + "__",null, (String)RowValues.Get(i), false));
				row.componentKeys.put(row.id + "_" + i + "__",row.id + "_" + i + "__");
			} else {
				TableValue tbv = new TableValue(row.id + "_" + i + "__",(ABMComponent)RowValues.Get(i), "", false);
				row.values.put(row.id + "_" + i + "__", tbv);
				row.componentKeys.put(tbv.value.ID.toLowerCase(),row.id + "_" + i + "__");
			}			
		}
		Rows.put(row.id,row);
	}
	
	public void AddRowFixedHeight(String uniqueId, anywheresoftware.b4a.objects.collections.List RowValues, int height) {
		TableRow row =new TableRow();
		row.UniqueId=uniqueId;
		int rCounter = Rows.size();
		row.id="_" + rCounter;
		row.FixedHeight = height;
		for (int i=0;i<RowValues.getSize();i++) {
			if (RowValues.Get(i) instanceof String) {
				row.values.put(row.id + "_" + i + "__", new TableValue(row.id + "_" + i + "__",null, (String)RowValues.Get(i), false));
				row.componentKeys.put(row.id + "_" + i + "__",row.id + "_" + i + "__");
			} else {
				TableValue tbv = new TableValue(row.id + "_" + i + "__",(ABMComponent)RowValues.Get(i), "", false);
				row.values.put(row.id + "_" + i + "__", tbv);
				row.componentKeys.put(tbv.value.ID.toLowerCase(),row.id + "_" + i + "__");
			}			
		}
		Rows.put(row.id,row);
	}	
	
	public void SetRowThemes(anywheresoftware.b4a.objects.collections.List themeNames) {
		String rowId = "_" + (Rows.size()-1);
		TableRow row = Rows.get(rowId);
		for (int i=0;i<themeNames.getSize();i++) {
			String th = (String) themeNames.Get(i);
			if (!th.equals("")) {
				boolean edit =  Theme.Cell(th).IsEditable;
				row.values.get(rowId + "_" + i + "__").theme = th.toLowerCase();
				row.values.get(rowId + "_" + i + "__").IsEditable = edit;
				row.values.get(rowId + "_" + i + "__").InputMask = Theme.Cell(th).InputMask;
				row.values.get(rowId + "_" + i + "__").AllowEnterKey = Theme.Cell(th).AllowEnterKey;
			}
		}
	}
	
	public void UseCellTheme(int Row, int Column, String themeName) {
		String rowId = "_" + (Row);
		TableRow r = Rows.getOrDefault(rowId, null);
		if (r!=null) {
			r.values.get(rowId + "_" + Column + "__").theme = themeName.toLowerCase();
		}
	}
	
	public String GetCellTheme(int Row, int Column) {
		String rowId = "_" + (Row);
		TableRow r = Rows.getOrDefault(rowId, null);
		if (r!=null) {
			return r.values.get(rowId + "_" + Column + "__").theme;
		}
		return "";
	}
	
	public void SetCellTag(int Row, int Column, Object tag) {
		String rowId = "_" + (Row);
		TableRow r = Rows.getOrDefault(rowId, null);
		if (r!=null) {
			r.values.get(rowId + "_" + Column + "__").tag = tag;
		}
	}
	
	public Object GetCellTag(int Row, int Column) {
		String rowId = "_" + (Row);
		TableRow r = Rows.getOrDefault(rowId, null);
		if (r!=null) {
			return r.values.get(rowId + "_" + Column + "__").tag;
		}
		return null;
	}
	
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Tables.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Tables.get(themeName.toLowerCase()).Clone();				
			}
		}
	}
	
	public ABMComponent GetComponent(int row, String componentId) {
		String rId= "_" + (row);
		TableRow r = Rows.getOrDefault(rId, null);
		if (r==null) {
			BA.Log("Row not found: " + row);
			return null;
		}
		String key = r.componentKeys.getOrDefault(componentId.toLowerCase(), "");
		if (key.equals("")) {
			BA.Log("No component found with id = " + componentId + ". Is it not a normal string you get with GetString(row,column)?");
			return null;
		}
		TableValue tbv = r.values.getOrDefault(key, null);
		if (tbv==null) {
			BA.Log("No component found with id = " + componentId + ". Is it not a normal string you get with GetString(row,column)?");
			return null;
		}
		return ABMaterial.GetComponent(Page, tbv.value, tbv.value.ID.toLowerCase(), this.ParentString + this.ArrayName.toLowerCase() + this.ID.toLowerCase() + tbv.id);
	}
	
	public ABMComponent GetComponentWriteOnly(int row, String componentId) {
		String rId= "_" + (row);
		TableRow r = Rows.getOrDefault(rId, null);
		if (r==null) {
			BA.Log("Row not found: " + row);
			return null;
		}
		String key = r.componentKeys.getOrDefault(componentId.toLowerCase(), "");
		if (key.equals("")) {
			BA.Log("No component found with id = " + componentId + ". Is it not a normal string you get with GetString(row,column)?");
			return null;
		}
		TableValue tbv = r.values.getOrDefault(key, null);
		if (tbv==null) {
			BA.Log("No component found with id = " + componentId + ". Is it not a normal string you get with GetString(row,column)?");
			return null;
		}
		return ABMaterial.CastABMComponent(tbv.value);
	}
	
	/**
	 * This method will prepare ALL editable cells for retrieval.  You have to call this only once before each batch of GetString() methods.
	 * e.g. tbl.PrepareTableForRetrieval
	 *      s = tbl.GetString(0,1)
	 *      s = tbl.GetString(1,1)
	 *      s = tbl.GetString(2,1)
	 */
	public void PrepareTableForRetrieval() {
		int rCounter=0;
		for (Entry<String, TableRow> row : Rows.entrySet()) {
			int cCounter = 0;
			for (Entry<String, TableValue> v : row.getValue().values.entrySet()) {
				TableValue r = v.getValue();
				if (r.IsEditable) {
					String cellId = "_" + (rCounter) + "_" + (cCounter) + "__";
					ABMaterial.GetTableStringValue(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + cellId, r);					
				}
				cCounter++;
			}
			
			rCounter++;
		}
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("Table PrepareTableForRetrieval: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * If you want to get the new values of editable columns, make sure you've called PrepareTableforRetrieval() BEFORE calling this function.
	 * This will create all SimpleFutures for each cell value and update them to the new values 
	 */
	public String GetString(int row, int column) {
		String rId= "_" + (row);
		TableRow r = Rows.getOrDefault(rId, null);
		if (r==null) {
			BA.Log("Row not found: " + row);
			return null;
		}
		String cellId = rId + "_" + (column) + "__";
		TableValue tbv = r.values.getOrDefault(cellId, null);
		if (tbv==null) {
			BA.Log("No string found in row " + row);
			return null;
		}
		return tbv.GetStringValue();
	}
	
	public void SetString(int row, int column, String value) {
		String rId= "_" + (row);
		TableRow r = Rows.getOrDefault(rId, null);
		if (r==null) {
			BA.Log("Row not found: " + row);
			return;
		}
		TableValue tbv = r.values.getOrDefault(rId + "_" + (column) + "__", null);
		if (tbv==null) {
			BA.Log("No string found in row " + row);
			return;
		}
		tbv.StringValue=value;
	}
	
	
	public void RefreshRow(int row) {
		
		String rId= "_" + (row);
		TableRow r = Rows.getOrDefault(rId, null);
		if (r==null) {
			BA.Log("Row not found: " + row);
			return;
		}
		for (int i=0;i<r.values.size();i++) {
			RefreshCellInternal(row, i, false);
		}
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("Table RefreshRow: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
	}
	
	public void RefreshCell(int row, int column) {
		RefreshCellInternal(row, column, true);
	}
	
	protected void RefreshCellInternal(int row, int column, boolean doFlush ) {
		String rId= "_" + (row);
		TableRow r = Rows.getOrDefault(rId, null);
		if (r==null) {
			BA.Log("Row not found: " + row);
			return;
		}
		TableValue tbv = r.values.getOrDefault(rId + "_" + (column) + "__", null);
		if (tbv==null) {
			BA.Log("No string found in row " + row);
			return;
		}
		
		if (tbv.value==null) {
			if (tbv.StringValue.startsWith("[ICON]")) {
				String s = "<i class=\"" + tbv.StringValue.substring(6) + " " + BuildCellIconClass(tbv.theme) + "\"></i>";
				ABMaterial.ReplaceMyInnerHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + tbv.id, s);
			} else {
				String s = "";
				if (IgnoreFormattingCodes) {
					 s = ABMaterial.HTMLConv().htmlEscape(tbv.StringValue, Page.PageCharset);
				} else {
					String vv = ABMaterial.HTMLConv().htmlEscape(tbv.StringValue, Page.PageCharset);
					vv=vv.replaceAll("(\r\n|\n\r|\r|\n)", "<br>");
					vv=vv.replace("{B}", "<b>");
					vv=vv.replace("{/B}", "</b>");
					vv=vv.replace("{I}", "<i>");
					vv=vv.replace("{/I}", "</i>");
					vv=vv.replace("{U}", "<ins>");
					vv=vv.replace("{/U}", "</ins>");
					vv=vv.replace("{SUB}", "<sub>");
					vv=vv.replace("{/SUB}", "</sub>");
					vv=vv.replace("{SUP}", "<sup>");
					vv=vv.replace("{/SUP}", "</sup>");
					vv=vv.replace("{BR}", "<br>");
					vv=vv.replace("{WBR}", "<wbr>");
					vv=vv.replace("{NBSP}", "&nbsp;");
					vv=vv.replace("{AL}", "<a rel=\"nofollow\" target=\"_blank\" href=\"");
					vv=vv.replace("{AT}", "\">");
					vv=vv.replace("{/AL}", "</a>");	
					
					while (vv.indexOf("{C:") > -1) {
						int vvi = vv.indexOf("{C:");
						vv=vv.replaceFirst("\\{C:", "<span style=\"color:");
						vv=vv.substring(0,vvi) + vv.substring(vvi).replaceFirst("\\}", "\">");
						vv=vv.replaceFirst("\\{/C}", "</span>");	
					}

					vv = vv.replace("{CODE}", "<div class=\"abmcode\"><pre><code>");
					vv = vv.replace("{/CODE}", "</code></pre></div>");
					while (vv.indexOf("{ST:") > -1) {
						int vvi = vv.indexOf("{ST:");
						vv=vv.replaceFirst("\\{ST:", "<span style=\"");			
						vv=vv.substring(0,vvi) + vv.substring(vvi).replaceFirst("\\}", "\">");
						vv=vv.replaceFirst("\\{/ST}", "</span>");	
					}	
					
					int start = vv.indexOf("{IC:");
					while (start > -1) {
						int stop = vv.indexOf("{/IC}");
						String vvv = "";
						if (stop>0) {
							vvv = vv.substring(start, stop+5);
						} else {
							break;
						}
						 
						String IconColor = vvv.substring(4, 11);
						String IconName = vvv.substring(12,vvv.length()-5);
						String repl="";
						
						switch (IconName.substring(0, 3).toLowerCase()) {
						case "mdi":
							repl = "<i style=\"color: " + IconColor + "\" class=\"" + IconName + "\"></i>";
							break;
						case "fa ":
						case "fa-":
							repl = "<i style=\"color: " + IconColor + "\" class=\"" + IconName + "\"></i>";
							break;
						case "abm":
							repl = "<i>" + Page.svgIconmap.getOrDefault(IconName.toLowerCase(), "") + "</i>"; 
							break;
						default:
							repl = "<i style=\"color: " + IconColor + "\" class=\"material-icons\">" + IconName + "</i>";
						}
						vv=vv.replace(vvv,repl );
						start = vv.indexOf("{IC:");
					}
					
					s = vv;
				}
				ABMaterial.ReplaceMyInnerHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + tbv.id, s);
			}
		} else {			
			tbv.value.RemoveMe();
			
			ABMaterial.AddHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + tbv.id, tbv.value.Build());
			
		}
		boolean IsActive = r.UniqueId.equals(mActiveRowId);
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + tbv.id);
		String colVisible="";
		if (!colVisibles.isEmpty()) {
			colVisible=colVisibles.get(column);
			
		}
		j.SetProp("class", BuildCellClass(tbv.theme, IsActive) + colVisible);
				
		if (tbv.value!=null) {
			tbv.value.Refresh();
		}
		
		if (doFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Table RefreshCellInternal: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
	}
	
	public void Clear() {
		for (Entry<String, TableRow> row : Rows.entrySet()) {
			row.getValue().CleanUp();
		}
		Rows.clear();
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
		if (!IsScrollable) {
			ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-nscr");
		} else {
			ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		}
	}
	
	@Override
	protected void FirstRun() {
		FirstRunInternal(true);
	}
		
	protected void FirstRunInternal(boolean DoFlush) {
		
		Page.ws.Eval(BuildJavaScript(), null);
		
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());		
		Page.ws.RunFunction("inittooltipped", Params);
		
		RefreshInternal(DoFlush);
		
		for (Entry<String, TableRow> row : Rows.entrySet()) {
			for (Entry<String, TableValue> v : row.getValue().values.entrySet()) {
				if (v.getValue().value!=null) {
					if (v.getValue().value.HadFirstRun==false) { 
						v.getValue().value.FirstRun();
					}
				}
			}
		}
		
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Table FirstRunInternalFlush: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
		
		super.FirstRun();
	}
	
	protected String BuildJavaScript() {
		StringBuilder s = new StringBuilder();
		if (IsScrollable) {				
			s.append("jQuery('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').ABMTableScroll();\n");			
			s.append("activetablerows['" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "']='';");
		}
		return s.toString();
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		super.Refresh();
		
		if (IsInteractive) {
			GetActiveRow();
		}
		String lastSort = "";
		if (IsSortable) {
			lastSort = GetSortColumn();
		}
		
		JQueryElement j;
		if (!IsScrollable) {
			j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-nscr");			
			j.SetHtml(BuildBody());
			anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
			Params.Initialize();
			Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());			
			Params.Add(IsScrollable);
			Params.Add(IsSortable);	
			Params.Add(false); // IsCollapse 
			Page.ws.RunFunction("inittable", Params);		
		} else {
			anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
			Params.Initialize();
			Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "_toplevel");			
			Params.Add(BuildBody());
			Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "_innerbody");
			Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());			
			Params.Add(IsScrollable);
			Params.Add(IsSortable);
			Params.Add(false); // IsCollapse
	
			Page.ws.RunFunction("replacemewithresetpos", Params);

		}
		if (!lastSort.equals("")) {
			SetSortColumn(lastSort);
		}
		String s = "$('[data-abminputmask=\"1\"]').inputmask();";
		Page.ws.Eval(s, null);
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Table RefreshInternalFlush 3: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
		for (Entry<String, TableRow> row : Rows.entrySet()) {
			for (Entry<String, TableValue> v : row.getValue().values.entrySet()) {
				if (v.getValue().value!=null) {
					switch (v.getValue().value.Type) {
					case ABMaterial.UITYPE_ABMCONTAINER:
						ABMContainer cont = (ABMContainer) v.getValue().value;						
						cont.RunAllFirstRunsInternal(false);
						String ss = cont.RunInitialAnimation();
						if (!ss.equals("")) {
							if (Page.ws.getOpen())
								Page.ws.Eval(ss, null);
						}												
						cont.RefreshInternalExtra(false, false,false);
						break;								
					case ABMaterial.UITYPE_FLEXWALL:
						ABMFlexWall wall = (ABMFlexWall) v.getValue().value;
						wall.RunAllFirstRunsInternal(false);													
						wall.RefreshInternalExtra(false, false, false);
						break;						
					case ABMaterial.UITYPE_TABEL:
						ABMTable table = (ABMTable) v.getValue().value;
						if (!table.HadFirstRun) {												
							table.FirstRunInternal(false);														
						}
						table.RefreshInternal(false);
						break;
					case ABMaterial.UITYPE_CUSTOMCOMPONENT:
						ABMCustomComponent ccomp = (ABMCustomComponent) v.getValue().value;
						ccomp.RunAllFirstRuns();													
						ccomp.RefreshInternal(false);
						break;
					default:
						v.getValue().value.FirstRun();						
						v.getValue().value.RefreshInternal(false);	
					}
				}
			}
		}
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Table RefreshInternalFlush 4: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}		
	}
	
	@Override
	protected String Build() {
		if (Theme.ThemeName.equals("default")) {
			Theme.Colorize(Page.CompleteTheme.MainColor);
		}
		StringBuilder s = new StringBuilder();
		ThemeTable l=Theme;
		if (!IsScrollable) {
			s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-nscr\" class=\"" + l.ZDepth + "\">\n");
		} 
		s.append(BuildBody());	
		if (!IsScrollable) {
			s.append("</div>\n");
		}
		
		IsBuild=true;		
		return s.toString();
	}
	
	@Hide
	protected String BuildClass() {			
		StringBuilder s = new StringBuilder();
		s.append(super.BuildExtraClasses());
		ThemeTable l=Theme;
		if (!ToolTipText.equals("")) {
			s.append(" tooltipped ");
		}		
		if (IsScrollable) {
			s.append(l.ZDepth + " ");
		}
		if (IsBordered) {
			s.append(" bordered ");
		}
		if (IsResponsive) {
			s.append(" responsive-table ");
		}	
		if (IsSortable) {
			s.append(" sortable ");
		}
		if (IsInteractive) {
			s.append(" tableinteractive ");
		}
		s.append(mVisibility + " ");	
		if (this.UsingQueriesToSort) {
			s.append(" noautosort ");
			
		}
		s.append(mIsPrintableClass);
		s.append(mIsOnlyForPrintClass);
		return s.toString(); 
	}
	
	@Hide
	protected String BuildBody() {
		StringBuilder s = new StringBuilder();
		String toolTip="";
		if (!ToolTipText.equals("")) {
			toolTip=" data-position=\"" + ToolTipPosition + "\" data-delay=\"" + ToolTipDelay + "\" data-tooltip=\"" + ToolTipText + "\" "; 
		}
		String selectable="";
		if (!IsTextSelectable) {
			selectable = " notselectable ";
		}
		s.append("<table " + toolTip + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"" );		
		s.append(BuildClass() + selectable);
		
		if (IsScrollable) {
			s.append("\" style=\"table-layout: fixed\">");			
		} else {
			s.append("\">");
		}
		int counter=0;
		if (header!=null) {
			s.append("<thead>\n");
			s.append("<tr>\n");
			counter=0;
			
			for (Entry<String, TableValue> v : header.values.entrySet()) {
				TableValue h = v.getValue();
				String colVisible="";
				if (!colVisibles.isEmpty()) {
					colVisible=colVisibles.get(counter);					
				}
				String Sortable=""; 
				if (!colSortables.isEmpty()) {
					if (!colSortables.get(counter)) {
						Sortable = " sorttable_nosort ";
					}
				}
				String dataField=""; 
				if (!colDataFields.isEmpty()) {
					dataField = colDataFields.get(counter);
				}
				String HeaderHeightPxStr="";
				if (h.height>0) {
					HeaderHeightPxStr=";height: " + h.height + "px";
				}
				if (IsScrollable) {
					s.append("<th fieldname=\"" + dataField + "\" id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + h.id + "\" class=\"" + Sortable + BuildCellClass(h.theme, false) + colVisible + "\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(h.theme, counter, false) + HeaderHeightPxStr +  "\">");
				} else {
					if (colWidths.isEmpty()) {
						s.append("<th fieldname=\"" + dataField + "\"  id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + h.id + "\" class=\"" + Sortable + BuildCellClass(h.theme, false) + colVisible + "\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(h.theme,-9999, false) + HeaderHeightPxStr + "\">");
					} else {
						s.append("<th fieldname=\"" + dataField + "\"  id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + h.id + "\" class=\"" + Sortable + BuildCellClass(h.theme, false) + colVisible + "\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(h.theme,counter, false) + HeaderHeightPxStr + "\">");
					}
				}
				if (h.value==null) {
					if (h.StringValue.startsWith("[ICON]")) {
						s.append("<i class=\"" + h.StringValue.substring(6) + " " + BuildCellIconClass(h.theme) + "\"></i>");
					} else {
						if (IgnoreFormattingCodes) {
							s.append(ABMaterial.HTMLConv().htmlEscape(h.StringValue, Page.PageCharset));
						} else {
							String vv = ABMaterial.HTMLConv().htmlEscape(h.StringValue, Page.PageCharset);
							vv=vv.replaceAll("(\r\n|\n\r|\r|\n)", "<br>");
							vv=vv.replace("{B}", "<b>");
							vv=vv.replace("{/B}", "</b>");
							vv=vv.replace("{I}", "<i>");
							vv=vv.replace("{/I}", "</i>");
							vv=vv.replace("{U}", "<ins>");
							vv=vv.replace("{/U}", "</ins>");
							vv=vv.replace("{SUB}", "<sub>");
							vv=vv.replace("{/SUB}", "</sub>");
							vv=vv.replace("{SUP}", "<sup>");
							vv=vv.replace("{/SUP}", "</sup>");
							vv=vv.replace("{BR}", "<br>");
							vv=vv.replace("{WBR}", "<wbr>");
							vv=vv.replace("{NBSP}", "&nbsp;");
							vv=vv.replace("{AL}", "<a rel=\"nofollow\" target=\"_blank\" href=\"");
							vv=vv.replace("{AT}", "\">");
							vv=vv.replace("{/AL}", "</a>");	
							
							while (vv.indexOf("{C:") > -1) {
								int vvi = vv.indexOf("{C:");
								vv=vv.replaceFirst("\\{C:", "<span style=\"color:");
								vv=vv.substring(0,vvi) + vv.substring(vvi).replaceFirst("\\}", "\">");
								vv=vv.replaceFirst("\\{/C}", "</span>");	
							}

							vv = vv.replace("{CODE}", "<div class=\"abmcode\"><pre><code>");
							vv = vv.replace("{/CODE}", "</code></pre></div>");
							while (vv.indexOf("{ST:") > -1) {
								int vvi = vv.indexOf("{ST:");
								vv=vv.replaceFirst("\\{ST:", "<span style=\"");			
								vv=vv.substring(0,vvi) + vv.substring(vvi).replaceFirst("\\}", "\">");
								vv=vv.replaceFirst("\\{/ST}", "</span>");	
							}	
							
							int start = vv.indexOf("{IC:");
							while (start > -1) {
								int stop = vv.indexOf("{/IC}");
								String vvv = "";
								if (stop>0) {
									vvv = vv.substring(start, stop+5);
								} else {
									break;
								}
								 
								String IconColor = vvv.substring(4, 11);
								String IconName = vvv.substring(12,vvv.length()-5);
								String repl="";
								switch (IconName.substring(0, 3).toLowerCase()) {
								case "mdi":
									repl = "<i style=\"color: " + IconColor + "\" class=\"" + IconName + "\"></i>";
									break;
								case "fa ":
								case "fa-":
									repl = "<i style=\"color: " + IconColor + "\" class=\"" + IconName + "\"></i>";
									break;
								case "abm":
									repl = "<i>" + Page.svgIconmap.getOrDefault(IconName.toLowerCase(), "") + "</i>"; 
									break;
								default:
									repl = "<i style=\"color: " + IconColor + "\" class=\"material-icons\">" + IconName + "</i>";
								}
								vv=vv.replace(vvv,repl );
								start = vv.indexOf("{IC:");
							}
							s.append(vv);
						}
					}
				} else {
					s.append(h.value.Build());
				}
				s.append("</th>\n");
				counter++;
			}
			s.append("</tr>\n");
			s.append("</thead>\n");
		}	
		s.append("<tbody class=\"" +  Theme.BackColor + " " + Theme.BackColorIntensity + "\">\n");			
		
		for (Entry<String, TableRow> row : Rows.entrySet()) {
			String uniqueId=row.getValue().UniqueId;
			boolean IsActive = uniqueId.equals(mActiveRowId);
			
			if (row.getValue().FixedHeight==0) {
				s.append("<tr uniqueid=\"" + uniqueId + "\">\n");
			} else {
				s.append("<tr uniqueid=\"" + uniqueId + "\" style=\"height:" + row.getValue().FixedHeight + "px\">\n");
			}
			counter=0;
			
			for (Entry<String, TableValue> v : row.getValue().values.entrySet()) {
				String colVisible="";
				TableValue r = v.getValue();
				if (!colVisibles.isEmpty()) {
					colVisible=colVisibles.get(counter);
				}
				String Editable="";
				if (r.IsEditable) {
					Editable = " contenteditable=\"true\" ";
					if (r.InputMask!="") {
						Editable += " data-abminputmask=\"1\" data-inputmask=\"" + r.InputMask + ", 'showMaskOnHover':false\" ";
						
						if (r.AllowEnterKey==false) {
							Editable += " allownoenter ";
						}
						
					}
				}
				String colSpan="";
				if (r.ColSpan>1) {
					colSpan=" colspan=\"" + r.ColSpan + "\"";
				}
				if (IsScrollable) {
					s.append("<td " + colSpan + Editable + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + r.id + "\" class=\"" + BuildCellClass(r.theme, IsActive) + colVisible + "\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(r.theme, counter, IsActive) + "\">");
				} else {
					if (colWidths.isEmpty()) {
						s.append("<td " + colSpan + Editable + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + r.id + "\" class=\"" + BuildCellClass(r.theme, IsActive) + colVisible + "\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(r.theme, -9999, IsActive) + "\">");
					} else {
						s.append("<td " + colSpan + Editable + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + r.id + "\" class=\"" + BuildCellClass(r.theme, IsActive) + colVisible + "\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(r.theme, counter, IsActive) + "\">");
					}
				}
				if (r.value==null) {
					if (r.StringValue.startsWith("[ICON]")) {
						s.append("<i class=\"" + r.StringValue.substring(6) + " " + BuildCellIconClass(r.theme) + "\"></i>");
					} else {
						if (IgnoreFormattingCodes) {
							s.append(ABMaterial.HTMLConv().htmlEscape(r.StringValue, Page.PageCharset));
						} else {
							String vv = ABMaterial.HTMLConv().htmlEscape(r.StringValue, Page.PageCharset);
							vv=vv.replaceAll("(\r\n|\n\r|\r|\n)", "<br>");
							vv=vv.replace("{B}", "<b>");
							vv=vv.replace("{/B}", "</b>");
							vv=vv.replace("{I}", "<i>");
							vv=vv.replace("{/I}", "</i>");
							vv=vv.replace("{U}", "<ins>");
							vv=vv.replace("{/U}", "</ins>");
							vv=vv.replace("{SUB}", "<sub>");
							vv=vv.replace("{/SUB}", "</sub>");
							vv=vv.replace("{SUP}", "<sup>");
							vv=vv.replace("{/SUP}", "</sup>");
							vv=vv.replace("{BR}", "<br>");
							vv=vv.replace("{WBR}", "<wbr>");
							vv=vv.replace("{NBSP}", "&nbsp;");
							vv=vv.replace("{AL}", "<a rel=\"nofollow\" target=\"_blank\" href=\"");
							vv=vv.replace("{AT}", "\">");
							vv=vv.replace("{/AL}", "</a>");	
							
							while (vv.indexOf("{C:") > -1) {
								int vvi = vv.indexOf("{C:");
								vv=vv.replaceFirst("\\{C:", "<span style=\"color:");
								vv=vv.substring(0,vvi) + vv.substring(vvi).replaceFirst("\\}", "\">");
								vv=vv.replaceFirst("\\{/C}", "</span>");	
							}

							vv = vv.replace("{CODE}", "<div class=\"abmcode\"><pre><code>");
							vv = vv.replace("{/CODE}", "</code></pre></div>");
							while (vv.indexOf("{ST:") > -1) {
								int vvi = vv.indexOf("{ST:");
								vv=vv.replaceFirst("\\{ST:", "<span style=\"");			
								vv=vv.substring(0,vvi) + vv.substring(vvi).replaceFirst("\\}", "\">");
								vv=vv.replaceFirst("\\{/ST}", "</span>");	
							}	
							
							int start = vv.indexOf("{IC:");
							while (start > -1) {
								int stop = vv.indexOf("{/IC}");
								String vvv = "";
								if (stop>0) {
									vvv = vv.substring(start, stop+5);
								} else {
									break;
								}
								 
								String IconColor = vvv.substring(4, 11);
								String IconName = vvv.substring(12,vvv.length()-5);
								String repl="";
								
								switch (IconName.substring(0, 3).toLowerCase()) {
								case "mdi":
									repl = "<i style=\"color: " + IconColor + "\" class=\"" + IconName + "\"></i>";
									break;
								case "fa ":
								case "fa-":
									repl = "<i style=\"color: " + IconColor + "\" class=\"" + IconName + "\"></i>";
									break;
								case "abm":
									repl = "<i>" + Page.svgIconmap.getOrDefault(IconName.toLowerCase(), "") + "</i>"; 
									break;
								default:
									repl = "<i style=\"color: " + IconColor + "\" class=\"material-icons\">" + IconName + "</i>";
								}
								vv=vv.replace(vvv,repl );
								start = vv.indexOf("{IC:");
							}
							s.append(vv);
						}
					}
				} else {
					s.append(r.value.Build());
				}
				s.append("</td>\n");
				counter++;
			}
			s.append("</tr>\n");
			
		}		
		s.append("</tbody>\n");
		if (footer!=null) {
			s.append("<tfoot>\n");
			s.append("<tr>\n");
			counter=0;
			for (Entry<String, TableValue> v : footer.values.entrySet()) {
				TableValue f = v.getValue();
				if (FooterColSpan>0) {
					s.append("<td colspan=\"" + FooterColSpan + "\" id=\"" + f.id + "\" class=\"" + BuildCellClass(f.theme, false) + "\" style=\"" + BuildCellStyle(f.theme, -9999, false) + "\">");
				} else {
					String colVisible="";
					if (!colVisibles.isEmpty()) {
						colVisible=colVisibles.get(counter);						
					}
					if (IsScrollable) {
						s.append("<td id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + f.id + "\" class=\"" + BuildCellClass(f.theme, false) + colVisible + "\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(f.theme, counter, false) + "\">");
					} else {
						if (colWidths.isEmpty()) {
							s.append("<td id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + f.id + "\" class=\"" + BuildCellClass(f.theme, false) + colVisible + "\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(f.theme, -9999, false) + "\">");
						} else {
							s.append("<td id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + f.id + "\" class=\"" + BuildCellClass(f.theme, false) + colVisible + "\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(f.theme, counter, false) + "\">");
						}
					}
					
				}
				if (f.value==null) {
					if (f.StringValue.startsWith("[ICON]")) {
						s.append("<i class=\"" + f.StringValue.substring(6) + " " + BuildCellIconClass(f.theme) + "\"></i>");
					} else {
						if (IgnoreFormattingCodes) {
							s.append(ABMaterial.HTMLConv().htmlEscape(f.StringValue, Page.PageCharset));
						} else {
							String vv = ABMaterial.HTMLConv().htmlEscape(f.StringValue, Page.PageCharset);
							vv=vv.replaceAll("(\r\n|\n\r|\r|\n)", "<br>");
							vv=vv.replace("{B}", "<b>");
							vv=vv.replace("{/B}", "</b>");
							vv=vv.replace("{I}", "<i>");
							vv=vv.replace("{/I}", "</i>");
							vv=vv.replace("{U}", "<ins>");
							vv=vv.replace("{/U}", "</ins>");
							vv=vv.replace("{SUB}", "<sub>");
							vv=vv.replace("{/SUB}", "</sub>");
							vv=vv.replace("{SUP}", "<sup>");
							vv=vv.replace("{/SUP}", "</sup>");
							vv=vv.replace("{BR}", "<br>");
							vv=vv.replace("{WBR}", "<wbr>");
							vv=vv.replace("{NBSP}", "&nbsp;");
							vv=vv.replace("{AL}", "<a rel=\"nofollow\" target=\"_blank\" href=\"");
							vv=vv.replace("{AT}", "\">");
							vv=vv.replace("{/AL}", "</a>");
							
							while (vv.indexOf("{C:") > -1) {
								int vvi = vv.indexOf("{C:");
								vv=vv.replaceFirst("\\{C:", "<span style=\"color:");
								vv=vv.substring(0,vvi) + vv.substring(vvi).replaceFirst("\\}", "\">");
								vv=vv.replaceFirst("\\{/C}", "</span>");	
							}

							vv = vv.replace("{CODE}", "<div class=\"abmcode\"><pre><code>");
							vv = vv.replace("{/CODE}", "</code></pre></div>");
							while (vv.indexOf("{ST:") > -1) {
								int vvi = vv.indexOf("{ST:");
								vv=vv.replaceFirst("\\{ST:", "<span style=\"");			
								vv=vv.substring(0,vvi) + vv.substring(vvi).replaceFirst("\\}", "\">");
								vv=vv.replaceFirst("\\{/ST}", "</span>");	
							}	
							
							int start = vv.indexOf("{IC:");
							while (start > -1) {
								int stop = vv.indexOf("{/IC}");
								String vvv = "";
								if (stop>0) {
									vvv = vv.substring(start, stop+5);
								} else {
									break;
								}

								String IconColor = vvv.substring(4, 11);
								String IconName = vvv.substring(12,vvv.length()-5);
								String repl="";
								
								switch (IconName.substring(0, 3).toLowerCase()) {
								case "mdi":
									repl = "<i style=\"color: " + IconColor + "\" class=\"" + IconName + "\"></i>";
									break;
								case "fa ":
								case "fa-":
									repl = "<i style=\"color: " + IconColor + "\" class=\"" + IconName + "\"></i>";
									break;
								case "abm":
									repl = "<i>" + Page.svgIconmap.getOrDefault(IconName.toLowerCase(), "") + "</i>"; 
									break;
								default:
									repl = "<i style=\"color: " + IconColor + "\" class=\"material-icons\">" + IconName + "</i>";
								}
								vv=vv.replace(vvv,repl );
								start = vv.indexOf("{IC:");
							}
							s.append(vv);
						}
					}
				} else {
					s.append(f.value.Build());
				}
				s.append("</td>\n");
				counter++;
			}
			s.append("</tr>\n");
			s.append("</tfoot>\n");
		}	
		s.append("</table>\n");
		return s.toString();
	}
	
	protected String BuildCellClass(String themeName, boolean IsActive) {
		StringBuilder s = new StringBuilder();
		TableCell l = Theme.Cell(themeName.toLowerCase());	
		if (IsScrollable) {
			s.append(l.ZDepth + " ");
		}
		s.append(l.Align + " ");
		if (IsActive) {
			s.append("tblcell" + Theme.ThemeName.toLowerCase() + "_" + themeName.toLowerCase() + " selected ");
		} else {
			s.append("tblcell" + Theme.ThemeName.toLowerCase() + "_" + themeName.toLowerCase() + " ");
		}
		s.append(ABMaterial.GetColorStr(l.ForeColor, l.ForeColorIntensity, "text") + " ");
		s.append("strikethrough" + Theme.ThemeName.toLowerCase() + "_" + l.ThemeName.toLowerCase() + " ");
		if (l.UseStrikethrough) {
			s.append("strikethrough ");
		}
		return s.toString();
	}
	
	protected String BuildCellIconClass(String themeName) {
		StringBuilder s = new StringBuilder();
		TableCell l = Theme.Cell(themeName.toLowerCase());		
		s.append(ABMaterial.GetColorStr(l.ForeColor, l.ForeColorIntensity, "text") + " small");
		return s.toString();
	}
	
	protected String BuildCellStyle(String themeName, int counter, boolean IsActive) {
		TableCell l = Theme.Cell(themeName.toLowerCase());
		
		if (counter==-9999) {
			if (l.FontSize!=15) {
				return "border: " + l.BorderWidth + "px solid " + ABMaterial.GetColorStrMap(l.BorderColor, l.BorderColorIntensity) + "; vertical-align: " + l.VerticalAlign + ";font-size: " + l.FontSize + "px" + ";" + l.ExtraStyle;
			} else {
				return "border: " + l.BorderWidth + "px solid " + ABMaterial.GetColorStrMap(l.BorderColor, l.BorderColorIntensity) + "; vertical-align: " + l.VerticalAlign + ";" + l.ExtraStyle;
			}			
		} else {
			if (colWidths.get(counter)==0) {
				if (l.FontSize!=15) {
					return "border: " + l.BorderWidth + "px solid " + ABMaterial.GetColorStrMap(l.BorderColor, l.BorderColorIntensity) + "; vertical-align: " + l.VerticalAlign + ";font-size: " + l.FontSize + "px" + ";" + l.ExtraStyle;
				} else {
					return "border: " + l.BorderWidth + "px solid " + ABMaterial.GetColorStrMap(l.BorderColor, l.BorderColorIntensity) + "; vertical-align: " + l.VerticalAlign + ";" + l.ExtraStyle;
				}
			} else {
				if (l.FontSize!=15) {
					return "width: " + colWidths.get(counter) + "px;border: " + l.BorderWidth + "px solid " + ABMaterial.GetColorStrMap(l.BorderColor, l.BorderColorIntensity) + "; vertical-align: " + l.VerticalAlign + ";font-size: " + l.FontSize + "px" + ";" + l.ExtraStyle;
				} else {
					return "width: " + colWidths.get(counter) + "px;border: " + l.BorderWidth + "px solid " + ABMaterial.GetColorStrMap(l.BorderColor, l.BorderColorIntensity) + "; vertical-align: " + l.VerticalAlign + ";" + l.ExtraStyle;
				}
			}
		}
	}
	
	protected class TableRow implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1849801586236339742L;
		public String id="";
		public String UniqueId="";
		public Map<String, TableValue> values = new LinkedHashMap<String,TableValue>();
		public Map<String,String> componentKeys = new LinkedHashMap<String,String>();
		public int FixedHeight=0;;
		
		TableRow() {
			
		}
		
		public TableRow Clone() {
			TableRow r = new TableRow();
			for (Entry<String, TableValue> v : values.entrySet()) {
				r.values.put(v.getKey(), v.getValue().Clone());				
			}
			for (Entry<String, String> v : componentKeys.entrySet()) {
				r.componentKeys.put(v.getKey(), v.getValue());				
			}
			r.id = id;
			r.UniqueId=UniqueId;
			r.FixedHeight=FixedHeight;
			return r;
		}
		
		public void CleanUp() {
			for (Entry<String, TableValue> v : values.entrySet()) {
				v.getValue().value=null;			
			}
		}
	}
	
	protected class TableValue implements java.io.Serializable {
		
		private static final long serialVersionUID = 4859232129828949534L;
		public ABMComponent value=null;
		public String StringValue="";
		public String theme="default";
		
		public String id="";
		public SimpleFuture j=null;
		public boolean IsEditable=false;
		public int ColSpan=1;
		
		public int height=0;
		protected Object tag=null;
		public String InputMask;
		public boolean AllowEnterKey=true;
		
		TableValue() {
			
		}
		
		protected String GetStringValue() {
			if (IsEditable) {
				try {
					StringValue = (String) j.getValue();
						
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (TimeoutException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}			
			}
			return StringValue;
		}
		
		TableValue(String id, ABMComponent value, String stringValue, boolean isEditable) {
			this.id=id;
			this.StringValue=stringValue;
			this.value = value;
			this.IsEditable = isEditable;
			if (value!=null) {
				value.AddArrayName(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + id.toLowerCase());
			}
			
			
		}
		
		TableValue(String id, ABMComponent value, String stringValue, boolean isEditable, int colSpan) {
			this.id=id;
			this.StringValue=stringValue;
			this.value = value;
			this.IsEditable = isEditable;
			if (value!=null) {
				value.AddArrayName(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + id.toLowerCase());
			}
			
			this.ColSpan=colSpan;
		}
		
		protected TableValue Clone() {
			TableValue v = new TableValue();
			
			if (value!=null) {
				v.value = value.Clone();
			}
			v.StringValue=StringValue;
			v.theme = theme;
			v.IsEditable=IsEditable;
			
			v.tag=tag;
			v.j = j;
			v.height=height;
			return v;			
		}
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMTable c = new ABMTable();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;
		c.mActiveRowId=mActiveRowId;
		c.Theme = Theme.Clone();
		c.Type = Type;
		c.mVisibility = mVisibility;	
		c.ToolTipDelay = ToolTipDelay;
		c.ToolTipPosition = ToolTipPosition;
		c.ToolTipText = ToolTipText;
		c.header = header.Clone();
		c.IsBordered = IsBordered;
		c.IsResponsive = IsResponsive;		
		for (Entry<String, TableRow> r : Rows.entrySet()) {
			c.Rows.put(r.getKey(), r.getValue().Clone());
		}
		return c;
	}
	
}
