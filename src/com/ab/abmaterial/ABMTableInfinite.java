package com.ab.abmaterial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import com.ab.abmaterial.ThemeTable.TableCell;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;	
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;
import anywheresoftware.b4j.object.WebSocket.SimpleFuture;

@Author("Alain Bailleul")  
@ShortName("ABMTableInfinite")
@Events(values={"Clicked(PassedRowsAndColumns as List)","Changed(Params as Map)", "SortChanged(DataField as String, Order as String)","NextContent(rowUniqueId As String)", "ColumnMoved(Drag as String, DroppedBefore as String)","HeaderClicked(DataField as String)"})
public class ABMTableInfinite extends ABMComponent {
	private static final long serialVersionUID = -7681302918095780798L;
	protected ThemeTable Theme=new ThemeTable();
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	public boolean IsBordered=false;
	public boolean IsResponsive=false;
	public int HeaderHeightPx=0;
	protected TableRowInfinite header=null;	
	protected TableRowInfinite footer=null;
	protected Map<String, TableRowInfinite> Rows = new LinkedHashMap<String, TableRowInfinite>();
	protected Map<String, TableRowInfinite> RowsBatch = new LinkedHashMap<String, TableRowInfinite>();
	protected int FooterColSpan=0;
	protected List<Integer> colWidths = new ArrayList<Integer>();
	protected List<Integer> colMinWidths = new ArrayList<Integer>();
	protected List<Integer> colMaxWidths = new ArrayList<Integer>();
	protected List<String> colVisibles = new ArrayList<String>();
	protected List<String> colSorts = new ArrayList<String>();
	protected List<Boolean> colHides = new ArrayList<Boolean>();
	protected List<Boolean> colWrappables = new ArrayList<Boolean>();
	protected List<Boolean> colDraggables = new ArrayList<Boolean>();
	protected List<Boolean> colClickables = new ArrayList<Boolean>();
	protected List<String> colDataFields = new ArrayList<String>();
	protected List<Integer> colOrders = new ArrayList<Integer>();
	protected List<String> colAligns = new ArrayList<String>();
	protected boolean IsSortable=false;
	public boolean IgnoreFormattingCodes=false;
	protected boolean IsInteractive=false;
	protected String ParentArrayName="";
	protected String mActiveRowId="";	
	protected boolean UsingQueriesToSort=false;
	protected List<String> RowsList = new ArrayList<String>();
	protected List<String> RowsListBatch = new ArrayList<String>();
	public boolean IsTextSelectable=true;
	protected String oldActiveRowId="";
	protected boolean UsesNextContentOnRow=false;
	protected boolean NeedsInit=false;
	protected boolean IsDraggable=false;
	
	protected Map<String, Boolean>CheckHides = new HashMap<String,Boolean>();
	protected List<String>CheckHidesList = new ArrayList<String>();
	protected Map<String, Boolean> ToUpdateHides = new HashMap<String,Boolean>();
	
	protected boolean IsDirtyOrder = false;
	
	public boolean OnlyAllowSortOnFirstColumn=false;
	public String DropZoneText="";
	
	protected List<TableValueInfinite> NeedsFirstsToRun = new ArrayList<TableValueInfinite>();
	
	public void Initialize(ABMPage page, String id, boolean isInteractive, String themeName) {
		this.ID = id;			
		this.Page = page;		
		this.Type = ABMaterial.UITYPE_TABELINFINITE;
		this.IsInteractive=isInteractive;
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
		return ArrayName.toLowerCase() + ID.toLowerCase() + "-scr";
	}
		
	/**
	 * Make sure this list has an equal number of items as there as columns! (boolean)
	 */
	public void SetColumnVisibles(anywheresoftware.b4a.objects.collections.List Visibles) {
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
	 * Make sure this list has an equal number of items as there as columns! (String: ASC, DESC or empty if not sortable)
	 */
	public void SetColumnSorts(anywheresoftware.b4a.objects.collections.List Sortables) {
		colSorts.clear();
		IsSortable=false;
		for (int i=0;i<Sortables.getSize();i++) {
			String sort = (String)Sortables.Get(i);
			if (!sort.equals("")) {
				IsSortable=true;
			}
			colSorts.add(sort.toUpperCase());			
		}
	}	
	
	/**
	 * Make sure this list has an equal number of items as there as columns! (constants TEXTALIGN_ or empty (default left))
	 */
	public void SetColumnAligns(anywheresoftware.b4a.objects.collections.List Aligns) {
		colAligns.clear();
		for (int i=0;i<Aligns.getSize();i++) {
			String al = (String)Aligns.Get(i);
			if (al.length()>0) {
				colAligns.add(";text-align: " + al.toLowerCase());
			} else {
				colAligns.add("");
			}
		}
	}
	
	public void UpdateColumnOrder(String fieldName, String beforeFieldName) {
		int col=0;
		int before=0;
		int colK=0;
		
		for (int ord=0;ord<colOrders.size();ord++) {
			int k = colOrders.get(ord);
			if (colDataFields.get(k).equalsIgnoreCase(fieldName)) {
				col = ord;
				colK = k;
				break;
			}
		}
		for (int ord=0;ord<colOrders.size();ord++) {
			int k = colOrders.get(ord);
			if (colDataFields.get(k).equalsIgnoreCase(beforeFieldName)) {
				before = ord;
				break;
			}
		}
		if (col>before) {
			colOrders.remove(col);
			colOrders.add(before, colK);
		} else {
			colOrders.remove(col);
			colOrders.add(before-1, colK);
		}		
	}
	
	public void UpdateSort(String fieldName, String newSort) {
		for (int i=0;i<colDataFields.size();i++) {
			if (fieldName.equalsIgnoreCase(colDataFields.get(i))) {
				colSorts.set(i, newSort.toUpperCase());
				break;
			}
		}		
	}
	
	/**
	 * Because this makes it ready for SQL queries, if ASC, no sorting is added 
	 * 
	 * note: if OnlyAllowSortOnFirstColumn=true, all sorts after the first one will be ASC
	 */
	public anywheresoftware.b4a.objects.collections.List GetCurrentOrderSort() {
		anywheresoftware.b4a.objects.collections.List l = new anywheresoftware.b4a.objects.collections.List();
		l.Initialize();
		for (int ord=0;ord<colOrders.size();ord++) {
			int counter = colOrders.get(ord);
			
			String dataField=""; 
			if (!colDataFields.isEmpty()) {
				dataField = colDataFields.get(counter);				
			}
			String sort="";
			if (!colSorts.isEmpty()) {
				if (colSorts.get(counter).equals("DESC")) {
					if (OnlyAllowSortOnFirstColumn && l.getSize()>0) {
						
					} else {
						sort = " " + colSorts.get(counter);
					}
				}
			}
			if (!dataField.equals("") && !dataField.startsWith("$")) {
				l.Add((dataField + sort).trim());
			}
		}
		
		return l;
	}
		
	/**
	 * Make sure this list has an equal number of items as there as columns! (boolean)
	 */
	public void SetColumnHideSameValues(anywheresoftware.b4a.objects.collections.List Hides) {
		colHides.clear();
		for (int i=0;i<Hides.getSize();i++) {
			colHides.add((boolean)Hides.Get(i));			
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
	 * Make sure this list has an equal number of items as there as columns! (boolean)
	 */
	public void SetColumnDraggables(anywheresoftware.b4a.objects.collections.List draggables) {
		colDraggables.clear();
		IsDraggable=false;
		for (int i=0;i<draggables.getSize();i++) {
			boolean drag = (boolean)draggables.Get(i);
			if (drag) {
				IsDraggable=true;
			}
			colDraggables.add(drag);			
		}
	}
	
	/**
	 * Make sure this list has an equal number of items as there as columns! (boolean)
	 */
	public void SetColumnWrappables(anywheresoftware.b4a.objects.collections.List wrappables) {
		colWrappables.clear();
		for (int i=0;i<wrappables.getSize();i++) {
			boolean drag = (boolean)wrappables.Get(i);
			colWrappables.add(drag);			
		}
	}
	
	/**
	 * Make sure this list has an equal number of items as there as columns! (boolean)
	 */
	public void SetHeaderClickables(anywheresoftware.b4a.objects.collections.List clickables) {
		colClickables.clear();
		for (int i=0;i<clickables.getSize();i++) {
			boolean click = (boolean)clickables.Get(i);
			colClickables.add(click);			
		}
	}
	
	@Override
	public String getID() {
		return ParentString + this.ArrayName.toLowerCase() + this.ID.toLowerCase() + "-scr";				
	}
		
	/**
	 * 
	 * Returns the fieldname and optional space + DESC. e.g. 'fldName DESC'
	 */
	public String GetSortColumn() {
		String lastSort = GetSortColumn(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		String Order="";
		try {
			if (lastSort.startsWith("DESC_")) {
				Order="DESC";
				lastSort=lastSort.substring(5);
			}
			if (!Order.equals("")) {
				return lastSort + " " + Order;
			} else {
				return lastSort;
			}
		} catch(NullPointerException e) {
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
		if (dataField.endsWith(" ASC")) {
			Order = "";
			dataField = dataField.substring(0, dataField.length() - 4).trim();
		}
		if (Page!=null) {
			ABMaterial.SetSortColumn(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), dataField, Order);
		}
	}
	
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;		
	}
	
	public void SetActiveRow(String uniqueId) {
		oldActiveRowId="";
		mActiveRowId=uniqueId;
		ABMaterial.SetActiveTableRow(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "_" + uniqueId);
	}
	
	public String GetActiveRow() {
		oldActiveRowId=mActiveRowId;
		mActiveRowId = GetActiveTableRow(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		if (mActiveRowId.equals("") && !oldActiveRowId.equals("") && UsesNextContentOnRow ) {
			mActiveRowId=oldActiveRowId;
		}
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
			try {
				s = (String) j.getValue();	
				if (s.startsWith("_")) {
					s = s.substring(1);
				}
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
		return s;
	}
	
	@Hide
	public String GetSortColumn(String ID) {
		String s = "";
		if (Page.ws.getOpen()) {
			anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		
			Params.Initialize();
			Params.Add(ID.toLowerCase());				
			SimpleFuture j = Page.ws.RunFunctionWithResult("getsortcolumn", Params);
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
		return s;
	}
	
	public void SetHeaders(anywheresoftware.b4a.objects.collections.List HeaderValues) {
		header=new TableRowInfinite();	
		header.UniqueId = "header";
		colOrders.clear();
		for (int i=0;i<HeaderValues.getSize();i++) {
			if (HeaderValues.Get(i) instanceof String) {
				TableValueInfinite tv = new TableValueInfinite(header.UniqueId + "_" + i + "__", null, (String) HeaderValues.Get(i) , false);
				tv.theme = "defaultheaderfooter";
				header.values.put(header.UniqueId + "_" + i + "__", tv);				
			} else {
				TableValueInfinite tv = new TableValueInfinite(header.UniqueId + "_" + i + "__", (ABMComponent)HeaderValues.Get(i), "", false);
				tv.theme = "defaultheaderfooter";
				header.values.put(header.UniqueId + "_" + i + "__", tv);				
			}		
			colOrders.add(i);
		}	
	}
	
	/**
	 * Alternative way to add columns 
	 */
	public void AddColumn(String fixedHeaderValue, Object headerValue, String fixedHeaderTheme, String theme, int headerHeight, boolean clickable, int width, int minWidth, int maxWidth, Object visible, String dataField, String sort, String align, boolean hideSameValues, boolean draggable, boolean wrappable) {
		if (header==null) {
			header=new TableRowInfinite();	
			header.UniqueId = "header";
			colWidths = new ArrayList<Integer>();
			colMinWidths = new ArrayList<Integer>();
			IsSortable=false;
			IsDraggable=false;
		}
		
		colOrders.add(header.values.size());
		int i = header.values.size();
		if (headerValue instanceof String) {
			TableValueInfinite tv = new TableValueInfinite(header.UniqueId + "_" + header.values.size() + "__", null, (String) headerValue , false);
			if (theme.equals("")) {
				tv.theme = "defaultheaderfooter";				
			} else {
				tv.theme = theme;				
			}
			if (fixedHeaderTheme.equals("")) {
				tv.fixedTheme = "defaultheaderfooter";
			} else {
				tv.fixedTheme = fixedHeaderTheme;
			}
			header.values.put(header.UniqueId + "_" + header.values.size() + "__", tv);
		} else {
			TableValueInfinite tv = new TableValueInfinite(header.UniqueId + "_" + header.values.size() + "__", (ABMComponent) headerValue, "", false);
			tv.theme = "defaultheaderfooter";
			tv.fixedTheme = "defaultheaderfooter";
			header.values.put(header.UniqueId + "_" + header.values.size() + "__", tv);
		}
		header.values.get(header.UniqueId + "_" + i + "__").fixedHeader = fixedHeaderValue;
		if (!fixedHeaderValue.equals("")) {
			header.HasFixedHeader=true;
		}
		header.values.get(header.UniqueId + "_" + i + "__").height = headerHeight;
		colClickables.add(clickable);
		
		colWidths.add(width);
		colMinWidths.add(minWidth);	
		colMaxWidths.add(maxWidth);
			
		String v = "";
		if (visible instanceof Boolean) {
			Boolean b = (Boolean)visible;
			if (b) {
				v = ABMaterial.VISIBILITY_ALL;
			} else {
				v = ABMaterial.VISIBILITY_HIDE_ALL;
			}
		} else {
			v = (String) visible;
			if (v.toLowerCase().equals("true")) {
				v = ABMaterial.VISIBILITY_ALL;
			}
			if (v.toLowerCase().equals("false")) {
				v = ABMaterial.VISIBILITY_HIDE_ALL;
			}
		}		
		colVisibles.add(v);	
		colDataFields.add(dataField);
		if (!sort.equals("")) {
			IsSortable=true;
		}
		colSorts.add(sort.toUpperCase());		
		if (align.length()>0) {
			colAligns.add(";text-align: " + align.toLowerCase());
		} else {
			colAligns.add("");
		}
		colHides.add(hideSameValues);
		if (draggable) {
			IsDraggable=true;
		}
		colDraggables.add(draggable);
		colWrappables.add(wrappable);
	}
	
	/**
	 * Only works if the header is a string 
	 */
	public void UpdateHeader(int index, String headerValue) {
		TableValueInfinite tv = header.values.getOrDefault(header.UniqueId + "_" + index + "__", null);
		if (tv!=null) {
			tv.StringValue=headerValue;
			tv.StringValueSame=CleanString(tv.StringValue);
			JQueryElement j;
			j = Page.ws.GetElementById(header.UniqueId + "_" + index + "__");
			
			j.SetText(tv.StringValue);
			
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("TableMutable UpdateHeader: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		} else {
			BA.Log("No tag found with id: '" + header.UniqueId + "_" + index + "__'");
		}
	}
	
	public void SetHeaderHeights(anywheresoftware.b4a.objects.collections.List HeaderHeights) {
		for (int i=0;i<HeaderHeights.getSize();i++) {
			int th = (int) HeaderHeights.Get(i);
			header.values.get(header.UniqueId + "_" + i + "__").height = th;
			
		}	
	}
	
	public void SetColumnWidths(anywheresoftware.b4a.objects.collections.List ColumnWidths) {
		colWidths = new ArrayList<Integer>();
		for (int i=0;i<ColumnWidths.getSize();i++) {
			int th = (int) ColumnWidths.Get(i);
			colWidths.add(th);					
		}	
	}
	
	public void SetColumnMinWidths(anywheresoftware.b4a.objects.collections.List ColumnWidths) {
		colMinWidths = new ArrayList<Integer>();
		for (int i=0;i<ColumnWidths.getSize();i++) {
			int th = (int) ColumnWidths.Get(i);
			colMinWidths.add(th);					
		}	
	}
	
	public void SetHeaderThemes(anywheresoftware.b4a.objects.collections.List themeNames) {
		for (int i=0;i<themeNames.getSize();i++) {
			String th = (String) themeNames.Get(i);
			if (!th.equals("")) {
				header.values.get(header.UniqueId + "_" + i + "__").theme = th.toLowerCase();
			}
		}
	}
	
	/**
	 * Shortcut method if your footer only contains one cell 
	 */
	public void SetFooter(Object FooterValue, int colSpanWidth, String footerTheme) {
		footer=new TableRowInfinite();	
		footer.UniqueId="footer";
		this.FooterColSpan=colSpanWidth;
		if (FooterValue instanceof String) {
			footer.values.put(footer.UniqueId + "_0__", new TableValueInfinite(footer.UniqueId + "_0__", null, (String) FooterValue,  false));
			if (!footerTheme.equals("")) {
				footer.values.get(footer.UniqueId + "_0__").theme = footerTheme.toLowerCase();
			} else {
				footer.values.get(footer.UniqueId + "_0__").theme = "defaultheaderfooter";
			}
		} else {
			footer.values.put(footer.UniqueId + "_0__", new TableValueInfinite(footer.UniqueId + "_0__", (ABMComponent)FooterValue, "", false));
			if (!footerTheme.equals("")) {
				footer.values.get(footer.UniqueId + "_0__").theme = footerTheme.toLowerCase();
			} else {
				footer.values.get(footer.UniqueId + "_0__").theme = "defaultheaderfooter";
			}
		}			
	}	

	public void SetFooters(anywheresoftware.b4a.objects.collections.List FooterValues) {
		footer=new TableRowInfinite();	
		footer.UniqueId="footer";
		for (int i=0;i<FooterValues.getSize();i++) {
			if (FooterValues.Get(i) instanceof String) {
				footer.values.put(footer.UniqueId + "_" + i + "__", new TableValueInfinite(footer.UniqueId + "_" + i + "__", null, (String) FooterValues.Get(i), false));
			} else {
				footer.values.put(footer.UniqueId + "_" + i + "__", new TableValueInfinite(footer.UniqueId + "_" + i + "__", (ABMComponent)FooterValues.Get(i), "", false));
			}
		}	
	}
	
	public void SetFooterThemes(anywheresoftware.b4a.objects.collections.List themeNames) {
		for (int i=0;i<themeNames.getSize();i++) {
			String th = (String) themeNames.Get(i);
			if (!th.equals("")) {
				footer.values.get(footer.UniqueId + "_" + i + "__").theme = th.toLowerCase();
			}
		}
	}	
		
	public void RaiseNextContentOnRow(String rowUniqueID) {
		UsesNextContentOnRow=true;
		if (Page.ws==null) {
			return;
		}
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		
		Params.Add(rowUniqueID);
		Params.Add(0);
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		//Params.Add(NextContentBlockSize);
		Page.ws.RunFunction("nextcontentrow", Params);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("Page RaiseNextContentOnRow");}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}	
	
	/**
	 * Note: Add [RAW] before the string if you want to use Raw HTML 
	 */
	public void AddRow(String uniqueId, anywheresoftware.b4a.objects.collections.List RowValues, anywheresoftware.b4a.objects.collections.List themeNames) {		
		AddRowFixedHeight(uniqueId, RowValues, themeNames, -9999);
	}	
	
	/**
	 * Note: Add [RAW] before the string if you want to use Raw HTML 
	 */
	public void AddRowFixedHeight(String uniqueId, anywheresoftware.b4a.objects.collections.List RowValues, anywheresoftware.b4a.objects.collections.List themeNames, int height) {		
		TableRowInfinite row =new TableRowInfinite();
		row.UniqueId="_" + uniqueId.toLowerCase();
		row.InsertBefore="";
		row.InsertAfter="";
		row.HD = " abmch ";
		
		if (height!=-9999) {
			row.FixedHeight = height;
		}
		for (int i=0;i<RowValues.getSize();i++) {
			if (RowValues.Get(i) instanceof String) {
				row.values.put(row.UniqueId.toLowerCase() + "_" + i + "__", new TableValueInfinite(row.UniqueId.toLowerCase() + "_" + i + "__",null, (String)RowValues.Get(i), false));
				row.componentKeys.put(row.UniqueId.toLowerCase() + "_" + i + "__",row.UniqueId.toLowerCase() + "_" + i + "__");
			} else {
				TableValueInfinite tbv = new TableValueInfinite(row.UniqueId.toLowerCase() + "_" + i + "__",(ABMComponent)RowValues.Get(i), "", false);
				row.values.put(row.UniqueId.toLowerCase() + "_" + i + "__", tbv);
				row.componentKeys.put(tbv.value.ID.toLowerCase(),row.UniqueId.toLowerCase() + "_" + i + "__");
			}			
		}
		Rows.put(row.UniqueId,row);
		if (!CheckHides.containsKey(row.UniqueId)) {
			CheckHides.put(row.UniqueId, true);
			CheckHidesList.add(row.UniqueId);
		}
		RowsList.add(row.UniqueId);
		RowsBatch.put(row.UniqueId,row);		
		RowsListBatch.add(row.UniqueId);
		SetRowThemes(uniqueId, themeNames);		
	}
	
	/**
	 * Make sure this list has an equal number of items as there as columns! (int)
	 * pass 0 if the column is joined.  Becware that you still have to pass the same number of column values/themes, just use "" for the joined columns
	 * 
	 * a row using colSpans will not be rearranged if the headers are rearranged
	 * 
	 * e.g. 
	 * 
	 * ' rCellValues and rCellThemes both contain 7 items in the list
	 * Table.AddRow(uniqueid, rCellValues,rCellThemes)	  
	 * Table.SetColSpans(uniqueid, Array as int(1,3,0,0,1,1,1))
	 * 
	 * | 1 | 3 | 0 | 0 | 1 | 1 | 1 | ---> |   |           |   |   |   | 
	 */
	public void SetColSpans(String uniqueId, anywheresoftware.b4a.objects.collections.List colSpans) {
		TableRowInfinite row = Rows.getOrDefault(uniqueId, null);
		if (row==null) {
			BA.Log("Something is wrong with the sync of the table. " + uniqueId + " does not exist!");
			return;
		}
		row.SetColspans(colSpans);
	}
	
	/**
	 * Adds all the rows since the last batch flush
	 * 
	 * This is different than Refresh, which will rebuild the whole table
	*/
	public void BatchFlush() {
		if (IsInteractive) {
			GetActiveRow();
		}
		StringBuilder sAdd = new StringBuilder();
				
		UpdateHides();
		
		int counter=0;
		for (Entry<String, TableValueInfinite> v : header.values.entrySet()) {
			TableValueInfinite h = v.getValue();
			String colVisible="";
			if (!colVisibles.isEmpty()) {
				colVisible=colVisibles.get(counter);					
			}
			ABMaterial.ChangeVisibility(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + h.id, colVisible);	
			counter++;
		}
		
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("TableMutable FlushHides: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		boolean OnlyAdds=true;
		
		for(String key : RowsListBatch) {
			TableRowInfinite r = RowsBatch.get(key);
			
			String val = BuildRow(r);
			if (!r.InsertBefore.equals("")) {				
				OnlyAdds=false;
				if (!sAdd.toString().equals("")) {
					ABMaterial.AddHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "_tbody", sAdd.toString());
					sAdd = new StringBuilder();
				}
				ABMaterial.InsertHTMLBefore(Page, "_" + r.InsertBefore, val);										
			} 
			if (!r.InsertAfter.equals("")) {
				OnlyAdds=false;
				if (!sAdd.toString().equals("")) {
					ABMaterial.AddHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "_tbody", sAdd.toString());
					sAdd = new StringBuilder();
				}
				ABMaterial.InsertHTMLAfter(Page, "_" + r.InsertAfter, val);				
			}
			if (r.InsertBefore.equals("") && r.InsertAfter.equals("")) {
				sAdd.append(val);										
			}
			
					
		}
		if (!sAdd.toString().equals("")) {
			ABMaterial.AddHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "_tbody", sAdd.toString());
			sAdd = new StringBuilder();
		}
				
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());			
		Params.Add(IsDraggable);
		Page.ws.RunFunction("inittablescr", Params);
		
		for (TableValueInfinite inf: NeedsFirstsToRun) {
			((ABMContainer) inf.value).RunAllFirstRunsInternal(false);
		}
				
		String ss = "$('[data-abminputmask=\"1\"]').inputmask();";
		Page.ws.Eval(ss, null);
		
		if (!OnlyAdds) {
			Page.ws.Eval(FlushHides(), null);
		}
		Page.ws.Eval("syncHeaderFooter('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "')", null);
		
		InnerRefreshFooter(false);
		
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("TableMutable FlushHides: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		CheckHides.clear();
		RowsListBatch.clear();
		RowsBatch.clear();
		
	}	
	
	protected void UpdateHides() {
		ToUpdateHides = new HashMap<String,Boolean>();
		for (String s: CheckHidesList) {
			UpdateHide(s);
		}
	}	
	
	protected void UpdateHide(String fromID) {
		fromID = fromID.toLowerCase();
		int rowSize = RowsList.size();
		boolean NotStop=true;
		TableRowInfinite prevR;
		if (!colHides.isEmpty()) {
			if (RowsList.size()>0) {
				int nowIndex=-1;
				for(int i=0;i<rowSize;i++) {
					if (fromID.equals(RowsList.get(i))) {						
						nowIndex=i;
						break;
					} 
				}
				switch (nowIndex) {
				case -1:
					break;
				case 0:
					NotStop=true;
					while (nowIndex+1<rowSize && NotStop) {						
						String currID=RowsList.get(nowIndex+1);
						prevR = Rows.get(fromID);
						TableRowInfinite currR = Rows.get(currID);
						boolean DoFurther=true;
						boolean Changed=false;
						for (int ord=0;ord<colOrders.size();ord++) {
							int k = colOrders.get(ord);
							if (colHides.get(k)) {
								TableValueInfinite prevV = prevR.values.get(fromID + "_" + k + "__");
								TableValueInfinite currV = currR.values.get(currID + "_" + k + "__");
								if (prevV==null) {
									BA.Log("PREV: " + fromID + "_" + k + "__");
									for (String s: prevR.values.keySet()) {
										BA.Log(s);
									}									
								}
								if (currV==null) {
									BA.Log("CURR: " + currID + "_" + k + "__");
									for (String s: currR.values.keySet()) {
										BA.Log(s);
									}									
								}
								if (prevV.StringValueSame.equals(currV.StringValueSame) && DoFurther) {
									if (!currV.IsSame) {
										currV.IsSame=true;
										Changed=true;
									}
									
								} else {
									if (currV.IsSame) {
										currV.IsSame=false;
										Changed=true;
										
									}
									DoFurther=false;
								}
							}							
						}
						if (Changed==false) {
							NotStop=false;
						} else {
							ToUpdateHides.put(currID, true);
						}
						fromID=currID;
						nowIndex++;
					}
					break;
				default:
					nowIndex--;
					fromID=RowsList.get(nowIndex);
					NotStop=true;
					while (nowIndex+1<rowSize && NotStop) {
						String currID=RowsList.get(nowIndex+1);
						prevR = Rows.get(fromID);
						TableRowInfinite currR = Rows.get(currID);
						boolean DoFurther=true;
						boolean Changed=false;
						for (int ord=0;ord<colOrders.size();ord++) {
							int k = colOrders.get(ord);
							if (colHides.get(k)) {
								TableValueInfinite prevV = prevR.values.get(fromID + "_" + k + "__");
								TableValueInfinite currV = currR.values.get(currID + "_" + k + "__");
								if (prevV.StringValueSame.equals(currV.StringValueSame) && DoFurther) {
									if (!currV.IsSame) {
										currV.IsSame=true;
										Changed=true;
									}
									
								} else {
									if (currV.IsSame) {
										currV.IsSame=false;
										Changed=true;
										
									}
									DoFurther=false;
								}
							}							
						}
						if (Changed==false) {
							NotStop=false;
						} else {
							ToUpdateHides.put(currID, true);
						}
						fromID=currID;
						nowIndex++;
					}
					break;
				}				
			}
			
		}
	}
	
	protected String FlushHides() {
		StringBuilder s = new StringBuilder();
		for (Entry<String,Boolean> entry: ToUpdateHides.entrySet()) {
			String currID= entry.getKey();
			TableRowInfinite currR = Rows.get(currID);
			for (int k=0;k<colHides.size();k++) {	
				if (colHides.get(k)) {
					TableValueInfinite currV = currR.values.get(currID + "_" + k + "__");
					if (currV.IsSame) {
						s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + currV.id + "').addClass('abmtmsame');");
					} else {
						s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + currV.id + "').removeClass('abmtmsame');");
					}
				}
			}
		}
		ToUpdateHides.clear();
		
		return s.toString();
	}
	
	public void RemoveRow(String uniqueId) {
		String rId= "_" + uniqueId.toLowerCase();
		TableRowInfinite r = Rows.getOrDefault(rId, null);
		if (r==null) {
			BA.Log("Row not found: " + uniqueId);
			return;
		}
		String firstCellId="";
		for (Entry<String, TableValueInfinite> v : r.values.entrySet()) {
			TableValueInfinite c = v.getValue();
			if (firstCellId=="") {
				firstCellId=c.id;
			}
			if (c.value!=null) {
				c.value.RemoveMe();
			}
		}
		Rows.remove(rId);
		int index=0;
		int found=-1;
		for(String s : RowsList) {
			index++;
			if (s.equalsIgnoreCase(rId)) {
				found=index;
				break;
			}			
		}
		if (found>-1) {
			RowsList.remove(found);
		}
		ABMaterial.RemoveHTML(Page, rId);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("TableInfinite RemoveRow: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	private void SetRowThemes(String uniqueId, anywheresoftware.b4a.objects.collections.List themeNames) {
		String rowId = "_" + uniqueId.toLowerCase();
		TableRowInfinite row = Rows.get(rowId);
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
	
	public void UseCellTheme(String uniqueId, int Column, String themeName) {
		String rowId = "_" + uniqueId.toLowerCase();
		TableRowInfinite r = Rows.getOrDefault(rowId, null);
		if (r!=null) {
			r.values.get(rowId + "_" + Column + "__").theme = themeName.toLowerCase();
		}
	}
	
	/**
	 * Set the themes, starting from the startColumn to the themeNames.size (String)  
	 */
	public void UseCellThemes(String uniqueId, int startColumn, anywheresoftware.b4a.objects.collections.List themeNames) {
		String rowId = "_" + uniqueId.toLowerCase();
		TableRowInfinite r = Rows.getOrDefault(rowId, null);
		if (r!=null) {
			for (int i=0;i<themeNames.getSize();i++) {
				String th = (String) themeNames.Get(i);				
				r.values.get(rowId + "_" + (i + startColumn) + "__").theme = th.toLowerCase();
			}
		}
	}
	
	public void SaveCellThemes(String uniqueId) {
		String rowId = "_" + uniqueId.toLowerCase();
		TableRowInfinite r = Rows.getOrDefault(rowId, null);
		if (r!=null) {
			r.SaveThemes();
		}
	}
	
	public void RestoreCellThemes(String uniqueId) {
		String rowId = "_" + uniqueId.toLowerCase();
		TableRowInfinite r = Rows.getOrDefault(rowId, null);
		if (r!=null) {
			r.RestoreThemes();
		}
	}
	
	public String GetCellTheme(String uniqueId, int Column) {
		String rowId = "_" + uniqueId.toLowerCase();
		TableRowInfinite r = Rows.getOrDefault(rowId, null);
		if (r!=null) {
			return r.values.get(rowId + "_" + Column + "__").theme;
		}
		return "";
	}
	
	public void SetCellTag(String uniqueId,int Column, Object tag) {
		String rowId = "_" + uniqueId.toLowerCase();
		TableRowInfinite r = Rows.getOrDefault(rowId, null);
		if (r!=null) {
			r.values.get(rowId + "_" + Column + "__").tag = tag;
		}
	}
	
	public Object GetCellTag(String uniqueId,int Column) {
		String rowId = "_" + uniqueId.toLowerCase();
		TableRowInfinite r = Rows.getOrDefault(rowId, null);
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
	
	public ABMComponent GetComponent(String uniqueId, String componentId) {
		String rId= "_" + uniqueId.toLowerCase();
		TableRowInfinite r = Rows.getOrDefault(rId, null);
		if (r==null) {
			BA.Log("Row not found: " + uniqueId);
			return null;
		}
		String key = r.componentKeys.getOrDefault(componentId.toLowerCase(), "");
		if (key.equals("")) {
			BA.Log("No component found with id = " + componentId + ". Is it not a normal string you get with GetString(row,column)?");
			return null;
		}
		TableValueInfinite tbv = r.values.getOrDefault(key, null);
		if (tbv==null) {
			BA.Log("No component found with id = " + componentId + ". Is it not a normal string you get with GetString(row,column)?");
			return null;
		}
		return ABMaterial.GetComponent(Page, tbv.value, tbv.value.ID.toLowerCase(), tbv.value.ParentString);
	}	
	
	public ABMComponent GetComponentWriteOnly(String uniqueId, String componentId) {
		String rId= "_" + uniqueId.toLowerCase();
		TableRowInfinite r = Rows.getOrDefault(rId, null);
		if (r==null) {
			BA.Log("Row not found: " + uniqueId);
			return null;
		}
		String key = r.componentKeys.getOrDefault(componentId.toLowerCase(), "");
		if (key.equals("")) {
			BA.Log("No component found with id = " + componentId + ". Is it not a normal string you get with GetString(row,column)?");
			return null;
		}
		TableValueInfinite tbv = r.values.getOrDefault(key, null);
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
		for (int k=0;k<RowsList.size();k++) {
			TableRowInfinite row = Rows.getOrDefault(RowsList.get(k), null);
			if (row==null) {
				BA.Log("Something is wrong with the sync of the table. " + RowsList.get(k) + " does not exist!");
				return;
			}
			int cCounter = 0;
			for (Entry<String, TableValueInfinite> v : row.values.entrySet()) {
				TableValueInfinite r = v.getValue();
				if (r.IsEditable) {
					String cellId = "_" + row.UniqueId + "_" + (cCounter) + "__";
					ABMaterial.GetTableInfiniteStringValue(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + cellId, r);					
				}
				cCounter++;
			}
		}
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("TableMutable PrepareTableForRetrieval: " + ID);}
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
	public String GetString(String uniqueId, int column) {
		String rId= "_" + uniqueId.toLowerCase();
		TableRowInfinite r = Rows.getOrDefault(rId, null);
		if (r==null) {
			BA.Log("Row not found: " + uniqueId);
			return null;
		}
		String cellId = rId + "_" + (column) + "__";
		TableValueInfinite tbv = r.values.getOrDefault(cellId, null);
		if (tbv==null) {
			BA.Log("No string found in row " + uniqueId);
			return null;
		}
		return tbv.GetStringValue();
	}
	
	/**
	 * Note: Add [RAW] before the string if you want to use Raw HTML 
	 */
	public void SetValue(String uniqueId, int column, Object value) {
		String rId= "_" + uniqueId.toLowerCase();
		TableRowInfinite r = Rows.getOrDefault(rId, null);
		if (r==null) {
			BA.Log("Row not found: " + uniqueId);
			return;
		}
		TableValueInfinite tbv = r.values.getOrDefault(rId + "_" + (column) + "__", null);
		if (tbv==null) {
			BA.Log("No string found in row " + uniqueId);
			return;
		}
		if (value instanceof String) {
			tbv.StringValue = (String) value;
			if (!CheckHides.containsKey(r.UniqueId)) {
				CheckHides.put(r.UniqueId, true);
				CheckHidesList.add(r.UniqueId);
			}
			tbv.StringValueSame = CleanString(tbv.StringValue);
		} else {
			tbv.value = (ABMComponent) value;
			tbv.value.AddArrayName(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + rId + "_" + (column) + "__");
		}
	}
	
	/**
	 * Set the values, starting from the startColumn to the values.size (String)  
	 * Note: Add [RAW] before the string if you want to use Raw HTML
	 */
	public void SetValues(String uniqueId, int startColumn, anywheresoftware.b4a.objects.collections.List values) {
		String rId= "_" + uniqueId.toLowerCase();
		TableRowInfinite r = Rows.getOrDefault(rId, null);
		if (r==null) {
			BA.Log("Row not found: " + uniqueId);
			return;
		}
		for (int i=0;i<values.getSize();i++) {
			TableValueInfinite tbv = r.values.getOrDefault(rId + "_" + (i + startColumn) + "__", null);
			if (tbv==null) {
				BA.Log("No string found in row " + uniqueId);
				return;
			}
			if (values.Get(i) instanceof String) {
				if (!CheckHides.containsKey(r.UniqueId)) {
					CheckHides.put(r.UniqueId, true);
					CheckHidesList.add(r.UniqueId);
				}
				tbv.StringValue=(String) values.Get(i);
				tbv.StringValueSame = CleanString(tbv.StringValue);
			} else {
				tbv.value = (ABMComponent)values.Get(i);
				tbv.value.AddArrayName(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + rId + "_" + (i + startColumn) + "__");
			}
		}
	}
	
	public void RefreshRow(String uniqueId) {
		String rId= "_" + uniqueId.toLowerCase();
		TableRowInfinite r = Rows.getOrDefault(rId, null);
		if (r==null) {
			BA.Log("Row not found: " + uniqueId);
			return;
		}
		NeedsFirstsToRun.clear();
		for (int i=0;i<r.values.size();i++) {
			RefreshCellInner(uniqueId, i, false);
		}
		for (TableValueInfinite inf: NeedsFirstsToRun) {
			((ABMContainer) inf.value).RunAllFirstRunsInternal(false);
		}
		String s = "$('[data-abminputmask=\"1\"]').inputmask();";
		Page.ws.Eval(s, null);
		
		if (!CheckHides.containsKey(r.UniqueId)) {
			CheckHides.put(r.UniqueId, true);
			CheckHidesList.add(r.UniqueId);
		}
		UpdateHides();
		Page.ws.Eval(FlushHides(), null);
		
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("TableMutable RefreshRow: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		
	}
	
	private String CleanString(String str) {
		if (str.startsWith("[RAW]")) {
			return str.substring(5);
		}
		StringBuilder s = new StringBuilder();
		
		String vv = ABMaterial.HTMLConv().htmlEscape(str, Page.PageCharset);
		vv=vv.replaceAll("(\r\n|\n\r|\r|\n)", "");
		vv=vv.replace("{B}", "");
		vv=vv.replace("{/B}", "");
		vv=vv.replace("{I}", "");
		vv=vv.replace("{/I}", "");
		vv=vv.replace("{U}", "");
		vv=vv.replace("{/U}", "");
		vv=vv.replace("{SUB}", "");
		vv=vv.replace("{/SUB}", "");
		vv=vv.replace("{SUP}", "");
		vv=vv.replace("{/SUP}", "");
		vv=vv.replace("{BR}", "");
		vv=vv.replace("{WBR}", "");
		vv=vv.replace("{NBSP}", "");				
		vv=vv.replace("{AL}", "");
		vv=vv.replace("{AT}", "");
		vv=vv.replace("{/AL}", "");
		
		while (vv.indexOf("{C:") > -1) {
			int vvi = vv.indexOf("{C:");
			vv=vv.replaceFirst("\\{C:", "");
			vv=vv.substring(0,vvi) + vv.substring(vvi).replaceFirst("\\}", "");
			vv=vv.replaceFirst("\\{/C}", "");	
		}

		vv = vv.replace("{CODE}", "");
		vv = vv.replace("{/CODE}", "");
		while (vv.indexOf("{ST:") > -1) {
			int vvi = vv.indexOf("{ST:");
			vv=vv.replaceFirst("\\{ST:", "");			
			vv=vv.substring(0,vvi) + vv.substring(vvi).replaceFirst("\\}", "");
			vv=vv.replaceFirst("\\{/ST}", "");	
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
			vv=vv.replace(vvv,"" );
			start = vv.indexOf("{IC:");
		}
		s.append(vv);
		
		return s.toString();
	}
	
	private String BuildTextValue(TableValueInfinite f) {
		if (f.StringValue.startsWith("[RAW]")) {
			return f.StringValue.substring(5);
		}
		StringBuilder s = new StringBuilder();
		
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
				vv=vv.replace("{AN}", "<a rel=\"nofollow\" href=\"");
				vv=vv.replace("{AL}", "<a rel=\"nofollow\" target=\"_blank\" href=\"");
				vv=vv.replace("{AT}", "\">");
				vv=vv.replace("{/AL}", "</a>");
				vv=vv.replace("{/AN}", "</a>");
				
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
		
		return s.toString();
	}
	
	private String BuildFixedTextValue(TableValueInfinite f) {
		if (f.fixedHeader.startsWith("[RAW]")) {
			return f.fixedHeader.substring(5);
		}
		StringBuilder s = new StringBuilder();
		
		if (f.fixedHeader.startsWith("[ICON]")) {
			s.append("<i class=\"" + f.StringValue.substring(6) + " " + BuildCellIconClass(f.theme) + "\"></i>");
		} else {
			if (IgnoreFormattingCodes) {
				s.append(ABMaterial.HTMLConv().htmlEscape(f.fixedHeader, Page.PageCharset));
			} else {
				String vv = ABMaterial.HTMLConv().htmlEscape(f.fixedHeader, Page.PageCharset);
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
				vv=vv.replace("{AN}", "<a rel=\"nofollow\" href=\"");
				vv=vv.replace("{AL}", "<a rel=\"nofollow\" target=\"_blank\" href=\"");
				vv=vv.replace("{AT}", "\">");
				vv=vv.replace("{/AL}", "</a>");
				vv=vv.replace("{/AN}", "</a>");
				
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
		
		return s.toString();
	}
	
	public void RefreshFooter() {
		InnerRefreshFooter(true);
	}
	
	protected void InnerRefreshFooter(boolean doFlush) {
		StringBuilder s = new StringBuilder();
		s.append("<tr>\n");
		for (int ord=0;ord<colOrders.size();ord++) {				
			int counter = colOrders.get(ord);
			TableValueInfinite f = footer.values.getOrDefault("footer_" + counter + "__",null);
			if (f!=null) {
				if (FooterColSpan>0) {
					s.append("<td colspan=\"" + FooterColSpan + "\" id=\"" + f.id + "\" class=\"" + BuildCellClass(f.theme, false, f) + "\" style=\"" + BuildCellStyle(f.theme, -9999, false, "") + "\">");
				} else {
					String colVisible="";
					if (!colVisibles.isEmpty()) {
						colVisible=colVisibles.get(counter);					
					}
				
					if (colWidths.isEmpty()) {
						s.append("<td id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + f.id + "\" class=\"" + BuildCellClass(f.theme, false, f) + colVisible + "\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(f.theme, -9999, false, "") + "\">");
					} else {
						s.append("<td id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + f.id + "\" class=\"" + BuildCellClass(f.theme, false, f) + colVisible + "\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(f.theme, counter, false, "") + "\">");
					}
				}
				if (f.value==null) {
					s.append(BuildTextValue(f));
				} else {
					s.append(f.value.Build());
				}
				s.append("</td>\n");
			}
		}
		s.append("</tr>\n");
		ABMaterial.ReplaceMyInnerHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "_tfoot", s.toString());
		if (doFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("TableMutable RefreshCell 1: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
	}
	
	public void RefreshCell(String uniqueId, int column) {
		NeedsFirstsToRun.clear();
		RefreshCellInner(uniqueId, column, true);
		for (TableValueInfinite inf: NeedsFirstsToRun) {
			((ABMContainer) inf.value).RunAllFirstRunsInternal(true);
		}		
	}
	
	protected void RefreshCellInner(String uniqueId, int column, boolean DoFlush) {
		String rId= "_" + uniqueId.toLowerCase();
		TableRowInfinite r = Rows.getOrDefault(rId, null);
		if (r==null) {
			BA.Log("Row not found: " + uniqueId);
			return;
		}
		TableValueInfinite tbv = r.values.getOrDefault(rId + "_" + (column) + "__", null);
		if (tbv==null) {
			BA.Log("No string found in row " + uniqueId);
			return;
		}
		
		if (tbv.value==null) {
			String s = BuildTextValue(tbv);
			ABMaterial.ReplaceMyInnerHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + tbv.id, s);
		} else {				
			if (tbv.value instanceof ABMContainer) {
				((ABMContainer)tbv.value).RemoveMeHTMLOnly();
				((ABMContainer)tbv.value).IsBuild = false;
				((ABMContainer)tbv.value).HadFirstRun = false;
				((ABMContainer)tbv.value).ResetSVGs();
			} else {
				tbv.value.RemoveMe();
			}
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("TableMutable RefreshCell 1: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
			ABMaterial.AddHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + tbv.id, tbv.value.Build());
			
			if (tbv.value instanceof ABMContainer) {
				NeedsFirstsToRun.add(tbv);
			}
		}
		
		boolean IsActive = r.UniqueId.equals("_" + mActiveRowId);
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + tbv.id);
		String colVisible="";
		if (!colVisibles.isEmpty()) {
			colVisible=colVisibles.get(column);			
		}
		j.SetProp("class", BuildCellClass(tbv.theme, IsActive, tbv) + colVisible);
		if (DoFlush) {
			for (TableValueInfinite inf: NeedsFirstsToRun) {
				((ABMContainer) inf.value).RunAllFirstRunsInternal(false);				
			}
			UpdateHides();
			Page.ws.Eval(FlushHides(), null);
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("TableMutable RefreshCell 2: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
	}	
		
	public void Clear() {
		for (Entry<String, TableRowInfinite> row : Rows.entrySet()) {
			row.getValue().CleanUp();
		}
		Rows.clear();
		RowsList.clear();
		RowsListBatch.clear();
		RowsBatch.clear();
		NeedsInit=true;		
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
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-scr");		
	}
	
	@Override
	protected void FirstRun() {
		Page.ws.Eval(BuildJavaScript(), null);
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());		
		Page.ws.RunFunction("inittooltipped", Params);
		Refresh();	
		super.FirstRun();
	}
	
	protected String BuildJavaScript() {
		StringBuilder s = new StringBuilder();
		
		return s.toString();
	}
	
	@Override
	public void Refresh() {	
		NeedsFirstsToRun.clear();
		RefreshInternal(true);
	}
	
	public void RefreshNoContents(boolean DoFlush) {
		JQueryElement j;
		String selectable="";
		if (!IsTextSelectable) {
			selectable = " notselectable ";
		}
		j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-header");
		j.SetProp("class", BuildClassScroll() + selectable);
		j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-body");
		j.SetProp("class", BuildClassScroll() + selectable);
		j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-footer");
		j.SetProp("class", BuildClassScroll() + selectable);
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("RefreshNoContents: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
	}
			
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		super.Refresh();
		
		UpdateHides();
		
		if (IsInteractive) {
			GetActiveRow();
		}
		String lastSort = GetSortColumn();
		
		JQueryElement j;
		
		j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-scr");
		ABMaterial.ChangeVisibility(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-scr", mVisibility);
		j.SetHtml(BuildBody());
		for (TableValueInfinite inf: NeedsFirstsToRun) {
			((ABMContainer) inf.value).RunAllFirstRunsInternal(false);
		}
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());			
		Params.Add(IsDraggable);
		Page.ws.RunFunction("inittablescr", Params);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("TableMutable Refresh 1: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		if (!lastSort.equals("")) {
			SetSortColumn(lastSort);
		}
		String s = "$('[data-abminputmask=\"1\"]').inputmask();";
		Page.ws.Eval(s, null);
		
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("TableMutable Refresh 2: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
		RowsListBatch.clear();
		RowsBatch.clear();
		CheckHides.clear();
		CheckHidesList.clear();
		ToUpdateHides.clear();
	}	
	
	@Override
	public void SetVisibilityFlush(String visibility, boolean DoFlush) {
		IsVisibilityDirty=true;
		GotLastVisibility=true;
		mVisibility=visibility;		
		ABMaterial.ChangeVisibility(Page, getID(), mVisibility);
		switch (mVisibility) {
		case ABMaterial.VISIBILITY_ALL:
		case ABMaterial.VISIBILITY_SHOW_ON_SMALL_ONLY:
		case ABMaterial.VISIBILITY_SHOW_ON_MEDIUM_ONLY:
		case ABMaterial.VISIBILITY_SHOW_ON_LARGE_ONLY:
		case ABMaterial.VISIBILITY_SHOW_ON_EXTRALARGE_ONLY:				
		case ABMaterial.VISIBILITY_SHOW_ON_MEDIUM_AND_ABOVE:
		case ABMaterial.VISIBILITY_SHOW_ON_MEDIUM_AND_BELOW:	
		case ABMaterial.VISIBILITY_SHOW_ON_LARGE_AND_ABOVE:
		case ABMaterial.VISIBILITY_SHOW_ON_LARGE_AND_BELOW:
			Page.ws.Eval("syncHeaderFooters()", null);
		}
		if (DoFlush) {
			
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Component SetVisibilityFlush: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected String Build() {
		if (Theme.ThemeName.equals("default")) {
			Theme.Colorize(Page.CompleteTheme.MainColor);
		}
		StringBuilder s = new StringBuilder();
		s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-scr\" class=\"abmscrollltbl " + mVisibility + " " + Theme.ZDepth + "\">\n");
		s.append(BuildBody());
		s.append("</div>\n");		
		
		IsBuild=true;
		return s.toString();
	}
	
	
	protected String BuildClassScroll() {			
		StringBuilder s = new StringBuilder();
		s.append(super.BuildExtraClasses());
		if (!ToolTipText.equals("")) {
			s.append(" tooltipped ");
		}		
		if (IsBordered) {
			s.append(" bordered ");
		}
		if (IsResponsive) {
			s.append(" responsive-table ");
		}
		if (IsInteractive) {
			s.append(" tableinteractive ");
		}		
		s.append(mIsPrintableClass);
		s.append(mIsOnlyForPrintClass);
		return s.toString(); 
	}
	
	public void ShowDropZoneText(boolean bool) {
		if (bool) {
			Page.ws.Eval("$('#abmscrollltbl-dropzone').show();", null);
		} else {
			Page.ws.Eval("$('#abmscrollltbl-dropzone').hide();", null);
		}
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("ShowDropZoneText: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	protected String BuildRow(TableRowInfinite row) {
		StringBuilder s = new StringBuilder();
		
		if (row==null) {
			BA.Log("Something is wrong with the sync of the table. Row is null!");
			return "";
		}
		String uniqueId=row.UniqueId;
		boolean IsActive = uniqueId.equals("_" + mActiveRowId);		
		
		if (row.FixedHeight==0) {
			s.append("<tr id=\"" + uniqueId + "\" uniqueid=\"" + uniqueId + "\" class=\"" + row.HD + "\">\n");
		} else {
			s.append("<tr id=\"" + uniqueId + "\" uniqueid=\"" + uniqueId + "\" class=\"" + row.HD + "\" style=\"height:" + row.FixedHeight + "px\">\n");
		}
		if (!row.HasColspans) {
			for (int ord=0;ord<colOrders.size();ord++) {				
				int counter = colOrders.get(ord);
				TableValueInfinite r = row.values.get(row.UniqueId.toLowerCase() + "_" + counter + "__");
				String colVisible="";
				
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
				
				String Width = "";
				if (!colWidths.isEmpty()) {
					if (colWidths.get(counter)>0) {
						Width = "width: " + colWidths.get(counter) + "px;";
					}
				}
				String minWidth = "";
				if (!colMinWidths.isEmpty()) {
					if (colMinWidths.get(counter)>0) {
						minWidth = "min-width: " + colMinWidths.get(counter) + "px;";
					}
				}
				String maxWidth = "";
				if (!colMaxWidths.isEmpty()) {
					if (colMaxWidths.get(counter)>0) {
						maxWidth = "max-width: " + colMaxWidths.get(counter) + "px;";
					}
				}
				String wrap = "";
				
				if (!colWrappables.isEmpty()) {
					if (!colWrappables.get(counter)) {
						wrap = " abmnowrap ";
					}
				}
				String align="";
				if (!colAligns.isEmpty() ) {
					align = colAligns.get(counter);
				}
				
				if (colWidths.isEmpty()) {
					s.append("<td " + Editable + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + r.id + "\" class=\"" + BuildCellClass(r.theme, IsActive, r) + colVisible + wrap + " shot\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(r.theme, -9999, IsActive, minWidth) + align +"\">");
				} else {
					s.append("<td " + Editable + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + r.id + "\" class=\"" + BuildCellClass(r.theme, IsActive, r) + colVisible + wrap + " shot\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(r.theme, counter, IsActive, minWidth) + align + Width + maxWidth + "\">");
				}
				
				if (r.value==null) {
					s.append(BuildTextValue(r));				
				} else {
					s.append(r.value.Build());
					if (r.value instanceof ABMContainer) {
						NeedsFirstsToRun.add(r);							
					}
				}
				s.append("</td>\n");			
			}
		} else {			
			for (int ord=0;ord<colOrders.size();ord++) {				
				int counter = ord;
				TableValueInfinite r = row.values.get(row.UniqueId.toLowerCase() + "_" + counter + "__");
				String colVisible="";
				
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
				int cspan = row.colSpans.get(counter);
				if (cspan>1) {
					colSpan=" colspan=\"" + cspan + "\" ";
					ord+=(cspan-1);
				}
				
				String minWidth = "";
				if (!colMinWidths.isEmpty()) {
					if (colMinWidths.get(counter)>0) {
						minWidth = "min-width: " + colMinWidths.get(counter) + "px;";
					}
				}
				String wrap = "";
				
				if (!colWrappables.isEmpty()) {
					if (!colWrappables.get(counter)) {
						wrap = " abmnowrap ";
					}
				}
				String align="";
				if (!colAligns.isEmpty() ) {
					align = colAligns.get(counter);
				}
				
				if (colWidths.isEmpty()) {
					s.append("<td " + colSpan + Editable + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + r.id + "\" class=\"" + BuildCellClass(r.theme, IsActive, r) + colVisible + wrap + "\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(r.theme, -9999, IsActive, minWidth) + align + "\">");
				} else {
					s.append("<td " + colSpan + Editable + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + r.id + "\" class=\"" + BuildCellClass(r.theme, IsActive, r) + colVisible + wrap + "\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(r.theme, counter, IsActive, minWidth) + align + "\">");
				}
				
				if (r.value==null) {
					s.append(BuildTextValue(r));				
				} else {
					s.append(r.value.Build());
					if (r.value instanceof ABMContainer) {
						NeedsFirstsToRun.add(r);							
					}
				}
				s.append("</td>\n");			
			}
		}
		s.append("</tr>\n");
		
		return s.toString();
	}
	
	@Hide
	protected String BuildBody() {
		boolean DidSort=false;
		StringBuilder s = new StringBuilder();
		String toolTip="";
		if (!ToolTipText.equals("")) {
			toolTip=" data-position=\"" + ToolTipPosition + "\" data-delay=\"" + ToolTipDelay + "\" data-tooltip=\"" + ToolTipText + "\" "; 
		}
		String selectable="";
		if (!IsTextSelectable) {
			selectable = " notselectable ";
		}

		if (header!=null) {			
			s.append("<div class=\"abmscrollltbl-headerwrapper\">");
			s.append("<div class=\"abmscrollltbl-header\">");
			if (header.HasFixedHeader) {
				s.append("<table>" );		
				
				s.append("<div>\n");
				s.append("<tr>\n");
				for (int ord=0;ord<colOrders.size();ord++) {
					int counter = ord;
					TableValueInfinite h = header.values.get("header_" + counter + "__");
					
					String colVisible="";
					if (!colVisibles.isEmpty()) {
						colVisible=colVisibles.get(counter);					
					}
					
					String align="";
					if (!colAligns.isEmpty() ) {
						align = colAligns.get(counter);
					}
					
					TableCell sepl = Theme.Cell(h.theme);
					String sepColor = ABMaterial.GetColorStrMap(sepl.DraggableHeaderSeperatorColor, sepl.DraggableHeaderSeperatorColorIntensity);
					String Sep=";border-left: 1px solid " + sepColor + ";border-right: 1px solid " + sepColor + " ";
					
					
					String Width = "";
					if (!colWidths.isEmpty()) {
						if (colWidths.get(counter)>0) {
							Width = "width: " + colWidths.get(counter) + "px;";
						}
					}
					String minWidth = "";
					if (!colMinWidths.isEmpty()) {
						if (colMinWidths.get(counter)>0) {
							minWidth = "min-width: " + colMinWidths.get(counter) + "px;";
						}
					}
					String maxWidth = "";
					if (!colMaxWidths.isEmpty()) {
						if (colMaxWidths.get(counter)>0) {
							maxWidth = "max-width: " + colMaxWidths.get(counter) + "px;";
						}
					}
					
					if (colWidths.isEmpty()) {
						s.append("<th class=\"" + BuildCellClass(h.fixedTheme, false, h) + colVisible + "\" style=\"" + BuildCellStyle(h.fixedTheme,-9999, false, "") + Sep + align + "\">");
					} else {
						s.append("<th class=\"" + BuildCellClass(h.fixedTheme, false, h) + colVisible + "\" style=\"" + BuildCellStyle(h.fixedTheme,counter, false, "") + Sep + align + Width + minWidth + maxWidth + "\">");
					}				
					
					s.append(BuildFixedTextValue(h));
					
					s.append("</th>\n");
				}
				s.append("</tr>\n");
				s.append("</div>\n");
				
				s.append("</table>");	
			}
			s.append("<table " + toolTip + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-header\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"" );		
			s.append(BuildClassScroll() + selectable);		
			s.append("\">");
			
			s.append("<thead>\n");
			s.append("<tr>\n");
			for (int ord=0;ord<colOrders.size();ord++) {				
				int counter = colOrders.get(ord);
				TableValueInfinite h = header.values.get("header_" + counter + "__");
				
				String colVisible="";
				if (!colVisibles.isEmpty()) {
					colVisible=colVisibles.get(counter);					
				}
				String dataField=""; 
				if (!colDataFields.isEmpty()) {
					dataField = colDataFields.get(counter);
				}
				String HeaderHeightPxStr="";
				if (h.height>0) {
					HeaderHeightPxStr=";height: " + h.height + "px";
				}
				String drag="";
				if (!colDraggables.isEmpty()) {
					if (colDraggables.get(counter)) {
						drag = " abminfdrag ";
					}
				}
				String click="";
				if (drag.equals("")) {
					if (!colClickables.isEmpty()) {
						if (colClickables.get(counter)) {
							click = " abminfclickable ";
						}
					}
				}
				String align="";
				if (!colAligns.isEmpty() ) {
					align = colAligns.get(counter);
				}
				
				String sort="";
				String sortArrow="";
				if (!colSorts.isEmpty() && DidSort==false) {
					String tmpSort = colSorts.get(counter);
					if (!tmpSort.equals("")) {
						switch (tmpSort) {
						case "ASC":
							sort = " abminfsort abminfdesc";
							sortArrow="&nbsp;&#x25BE;";
							break;
						case "DESC":
							sort = " abminfsort abminfasc ";
							sortArrow="&nbsp;&#x25B4;";
							break;
						}
					}
				}
				if (OnlyAllowSortOnFirstColumn) {
					if (!sort.equals("")) {
						DidSort=true;
					}
				}
				String Sep="";
				if (!drag.equals("")) {
					TableCell sepl = Theme.Cell(h.theme);
					if (!sepl.DraggableHeaderSeperatorColor.equals(ABMaterial.COLOR_TRANSPARENT)) {
						String sepColor = ABMaterial.GetColorStrMap(sepl.DraggableHeaderSeperatorColor, sepl.DraggableHeaderSeperatorColorIntensity);
						Sep=";border-left: 1px solid " + sepColor + ";border-right: 1px solid " + sepColor + " ";
					}
				}
				
				String Width = "";
				if (!colWidths.isEmpty()) {
					if (colWidths.get(counter)>0) {
						Width = "width: " + colWidths.get(counter) + "px;";
					}
				}
				String minWidth = "";
				if (!colMinWidths.isEmpty()) {
					if (colMinWidths.get(counter)>0) {
						minWidth = "min-width: " + colMinWidths.get(counter) + "px;";
					}
				}
				String maxWidth = "";
				if (!colMaxWidths.isEmpty()) {
					if (colMaxWidths.get(counter)>0) {
						maxWidth = "max-width: " + colMaxWidths.get(counter) + "px;";
					}
				}
				
				if (colWidths.isEmpty()) {
					s.append("<th fieldname=\"" + dataField + "\" id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + h.id + "\" class=\"" + BuildCellClass(h.theme, false, h) + colVisible + drag + click + sort + " shot\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(h.theme,-9999, false, "") + Sep + HeaderHeightPxStr + align + "\">");
				} else {
					s.append("<th fieldname=\"" + dataField + "\" id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + h.id + "\" class=\"" + BuildCellClass(h.theme, false, h) + colVisible + drag + click + sort + " shot\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(h.theme,counter, false, "") + Sep + HeaderHeightPxStr + align + Width + minWidth + maxWidth + "\">");
				}				
				if (h.value==null) {
					if (sortArrow.equals("")) {
						s.append(BuildTextValue(h) + sortArrow);
					} else {
						s.append("<span class=\"abminfclick\" style=\"cursor: pointer; padding-top: 7px;padding-bottom:8px\">" + BuildTextValue(h) + sortArrow + "</span>");
					}					
				} else {
					s.append(h.value.Build());					
				}
				s.append("</th>\n");
			}
			s.append("</tr>\n");
			s.append("</thead>\n");
			
			s.append("</table></div></div>");			
		}	
		
		if (!DropZoneText.equals("")) {
			s.append("<div class=\"abmscrollltbl-body\" style=\"cursor: pointer\">");
			s.append("<div id=\"abmscrollltbl-dropzone\" class=\"abmwatermark flow-textbig\">" + DropZoneText + "</div>");
		} else {
			s.append("<div class=\"abmscrollltbl-body\">");
		}
		s.append("<table " + toolTip + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-body\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"" );		
		s.append(BuildClassScroll() + selectable);		
		s.append("\">");
		
		s.append("<tbody id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "_tbody\" class=\"" +  Theme.BackColor + " " + Theme.BackColorIntensity + "\">\n");
		for (int k=0;k<RowsList.size();k++) {
			s.append(BuildRow(Rows.getOrDefault(RowsList.get(k),null)));			
		}
		s.append("</tbody>\n");
		s.append("</table>");
		s.append("</div>");
		
		if (footer!=null) {			
			s.append("<div class=\"abmscrollltbl-footerwrapper\">");
			s.append("<div class=\"abmscrollltbl-footer\">");
			s.append("<table " + toolTip + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-footer\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"" );		
			s.append(BuildClassScroll() + selectable);		
			s.append("\">");
			
			s.append("<tfoot id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "_tfoot\">\n");
			s.append("<tr>\n");
			for (int ord=0;ord<colOrders.size();ord++) {				
				int counter = colOrders.get(ord);
				TableValueInfinite f = footer.values.getOrDefault("footer_" + counter + "__",null);
				if (f!=null) {
					if (FooterColSpan>0) {
						s.append("<td colspan=\"" + FooterColSpan + "\" id=\"" + f.id + "\" class=\"" + BuildCellClass(f.theme, false, f) + "\" style=\"" + BuildCellStyle(f.theme, -9999, false, "") + "\">");
					} else {
						String colVisible="";
						if (!colVisibles.isEmpty()) {
							colVisible=colVisibles.get(counter);
						}
						
						String align="";
						if (!colAligns.isEmpty() ) {
							align = colAligns.get(counter);
						}
					
						String Width = "";
						if (!colWidths.isEmpty()) {
							if (colWidths.get(counter)>0) {
								Width = "width: " + colWidths.get(counter) + "px;";
							}
						}
						String minWidth = "";
						if (!colMinWidths.isEmpty()) {
							if (colMinWidths.get(counter)>0) {
								minWidth = "min-width: " + colMinWidths.get(counter) + "px;";
							}
						}
						String maxWidth = "";
						if (!colMaxWidths.isEmpty()) {
							if (colMaxWidths.get(counter)>0) {
								maxWidth = "max-width: " + colMaxWidths.get(counter) + "px;";
							}
						}
						
						if (colWidths.isEmpty()) {
							s.append("<td id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + f.id + "\" class=\"" + BuildCellClass(f.theme, false, f) + "\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(f.theme, -9999, false, "") + colVisible + align + "\">");
						} else {
							s.append("<td id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + f.id + "\" class=\"" + BuildCellClass(f.theme, false, f) + "\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(f.theme, counter, false, "") + colVisible + align + Width + minWidth + maxWidth + "\">");
						}
					}
					if (f.value==null) {
						s.append(BuildTextValue(f));
					} else {
						s.append(f.value.Build());
					}
					s.append("</td>\n");
				}
			}
			s.append("</tr>\n");
			s.append("</tfoot>\n");
			
			s.append("</table></div></div>");
			
			
		}
		return s.toString();
	}
	
	protected String BuildCellClass(String themeName, boolean IsActive, TableValueInfinite tvm) {
		StringBuilder s = new StringBuilder();
		TableCell l = Theme.Cell(themeName);	
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
		if (tvm.IsSame) {
			s.append("abmtmsame ");
		}
		return s.toString();
	}
	
	protected String BuildCellIconClass(String themeName) {
		StringBuilder s = new StringBuilder();
		TableCell l = Theme.Cell(themeName.toLowerCase());		
		s.append(ABMaterial.GetColorStr(l.ForeColor, l.ForeColorIntensity, "text") + " small");
		return s.toString();
	}
	
	protected String BuildCellStyle(String themeName, int counter, boolean IsActive, String minWidth) {
		TableCell l = Theme.Cell(themeName.toLowerCase());	
		if (counter==-9999) {
			if (l.FontSize!=15) {
				return "border: " + l.BorderWidth + "px solid " + ABMaterial.GetColorStrMap(l.BorderColor, l.BorderColorIntensity) + "; vertical-align: " + l.VerticalAlign + ";font-size: " + l.FontSize + "px;" + minWidth + ";" + l.ExtraStyle;
			} else {
				return "border: " + l.BorderWidth + "px solid " + ABMaterial.GetColorStrMap(l.BorderColor, l.BorderColorIntensity) + "; vertical-align: " + l.VerticalAlign + ";" + minWidth + l.ExtraStyle;
			}			
		} else {
			if (colWidths.get(counter)==0) {
				if (l.FontSize!=15) {
					return "border: " + l.BorderWidth + "px solid " + ABMaterial.GetColorStrMap(l.BorderColor, l.BorderColorIntensity) + "; vertical-align: " + l.VerticalAlign + ";font-size: " + l.FontSize + "px" + minWidth + ";" + l.ExtraStyle;
				} else {
					return "border: " + l.BorderWidth + "px solid " + ABMaterial.GetColorStrMap(l.BorderColor, l.BorderColorIntensity) + "; vertical-align: " + l.VerticalAlign + ";" + minWidth + ";" + l.ExtraStyle;
				}
			} else {
				if (l.FontSize!=15) {
					return "width: " + colWidths.get(counter) + "px;border: " + l.BorderWidth + "px solid " + ABMaterial.GetColorStrMap(l.BorderColor, l.BorderColorIntensity) + "; vertical-align: " + l.VerticalAlign + ";font-size: " + l.FontSize + "px;" + minWidth + ";" + l.ExtraStyle;
				} else {
					return "width: " + colWidths.get(counter) + "px;border: " + l.BorderWidth + "px solid " + ABMaterial.GetColorStrMap(l.BorderColor, l.BorderColorIntensity) + "; vertical-align: " + l.VerticalAlign + ";" + minWidth + ";" + l.ExtraStyle;
				}
			}
		}
	}
	
	protected class TableRowInfinite implements java.io.Serializable {
		private static final long serialVersionUID = -6575821914322594837L;
		public String UniqueId="";
		public Map<String, TableValueInfinite> values = new LinkedHashMap<String,TableValueInfinite>();
		public Map<String,String> componentKeys = new LinkedHashMap<String,String>();
		public int FixedHeight=0;
		public String InsertBefore="";
		public String InsertAfter="";
		public String HD=" abmch ";
		public boolean HasColspans=false;
		public List<Integer> colSpans = new ArrayList<Integer>();
		public boolean HasFixedHeader=false;
		
		TableRowInfinite() {
			
		}
		
		public TableRowInfinite Clone() {
			TableRowInfinite r = new TableRowInfinite();
			for (Entry<String, TableValueInfinite> v : values.entrySet()) {
				r.values.put(v.getKey(), v.getValue().Clone());				
			}
			for (Entry<String, String> v : componentKeys.entrySet()) {
				r.componentKeys.put(v.getKey(), v.getValue());				
			}
			//r.id = id;
			r.UniqueId=UniqueId;
			r.FixedHeight=FixedHeight;
			r.HD=HD;
			r.HasFixedHeader=HasFixedHeader;
			return r;
		}
		
		public void SaveThemes() {
			for (Entry<String, TableValueInfinite> v : values.entrySet()) {
				v.getValue().SavedTheme = v.getValue().theme; 		
			}
		}
		
		public void RestoreThemes() {
			for (Entry<String, TableValueInfinite> v : values.entrySet()) {
				v.getValue().theme = v.getValue().SavedTheme; 		
			}
		}
		
		public void CleanUp() {
			for (Entry<String, TableValueInfinite> v : values.entrySet()) {
				v.getValue().value=null;			
			}
		}
		
		public void SetColspans(anywheresoftware.b4a.objects.collections.List cspans) {
			HasColspans=true;
			for (int i=0;i<cspans.getSize();i++) {
				Integer tmp = (Integer)cspans.Get(i);
				colSpans.add(tmp);
			}
		}
	}
	
	protected class TableValueInfinite implements java.io.Serializable {
		private static final long serialVersionUID = 5613766983236213770L;
		public ABMComponent value=null;
		public String StringValue="";
		public String StringValueSame="";
		public String theme="default";
		public String fixedTheme="default";
		public String id="";
		public SimpleFuture j=null;
		public boolean IsEditable=false;
		public int height=0;
		protected Object tag=null;
		public String InputMask;
		public boolean AllowEnterKey=true;
		public boolean IsSame=false;
		public String fixedHeader="";
		
		public String SavedTheme="";
		
		TableValueInfinite() {
			
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
		
		TableValueInfinite(String id, ABMComponent value, String stringValue, boolean isEditable) {
			this.id=id;
			this.StringValue=stringValue;
			this.StringValueSame=CleanString(this.StringValue);
			this.value = value;
			this.IsEditable = isEditable;
			if (value!=null) {
				value.AddArrayName(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + id.toLowerCase());
			}
		}
		
		TableValueInfinite(String id, ABMComponent value, String stringValue, boolean isEditable, int colSpan) {
			this.id=id;
			this.StringValue=stringValue;
			this.StringValueSame=CleanString(this.StringValue);
			this.value = value;
			this.IsEditable = isEditable;
			if (value!=null) {
				value.AddArrayName(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + id.toLowerCase());
			}			
		}
		
		protected TableValueInfinite Clone() {
			TableValueInfinite v = new TableValueInfinite();
			if (value!=null) {
				v.value = value.Clone();
			}
			v.StringValue=StringValue;
			v.StringValueSame=StringValueSame;
			v.theme = theme;
			v.fixedTheme = fixedTheme;
			v.IsEditable=IsEditable;
			v.tag=tag;
			v.j = j;
			v.height=height;
			v.IsSame=IsSame;
			v.SavedTheme = SavedTheme;
			v.fixedHeader=fixedHeader;
			return v;			
		}
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMTableInfinite c = new ABMTableInfinite();
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
		for (Entry<String, TableRowInfinite> r : Rows.entrySet()) {
			c.Rows.put(r.getKey(), r.getValue().Clone());
		}
		for (String s: RowsList) {
			c.RowsList.add(s);
		}
		return c;
	}
	
}

