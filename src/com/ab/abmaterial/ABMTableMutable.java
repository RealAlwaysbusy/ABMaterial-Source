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
@ShortName("ABMTableMutable")
@Events(values={"Clicked(PassedRowsAndColumns as List)","Changed(Params as Map)", "SortChanged(DataField as String, Order as String)"})
public class ABMTableMutable extends ABMComponent {
	
	private static final long serialVersionUID = 2466497784823863252L;
	protected ThemeTable Theme=new ThemeTable();
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	public boolean IsBordered=false;
	public boolean IsResponsive=false;
	public int HeaderHeightPx=0;
	protected TableRowMutable header=null;	
	protected TableRowMutable footer=null;
	protected Map<String, TableRowMutable> Rows = new LinkedHashMap<String, TableRowMutable>();
	protected int FooterColSpan=0;
	protected List<Integer> colWidths = new ArrayList<Integer>();
	protected List<Boolean> colVisibles = new ArrayList<Boolean>();
	protected List<Boolean> colSortables = new ArrayList<Boolean>();
	protected List<String> colDataFields = new ArrayList<String>();
	protected boolean IsSortable=false;
	public boolean IgnoreFormattingCodes=false;
	protected boolean IsInteractive=false;
	protected String ParentArrayName="";
	protected String mActiveRowId="";	
	protected boolean UsingQueriesToSort=false;
	
	protected List<String> RowsList = new ArrayList<String>();
	
	public boolean IsTextSelectable=true;
	
	public void Initialize(ABMPage page, String id, boolean isSortable, boolean usingQueriesToSort, boolean isInteractive, String themeName) {
		this.ID = id;			
		this.Page = page;		
		this.Type = ABMaterial.UITYPE_TABELMUTABLE;
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
	
	@Override
	protected void ResetTheme() {
		UseTheme(Theme.ThemeName);
	}
	
	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase() + "-nscr";		
	}	
		
	/**
	 * Make sure this list has an equal number of items as there as columns! (boolean)
	 */
	public void SetColumnVisible(anywheresoftware.b4a.objects.collections.List Visibles) {
		colVisibles.clear();
		for (int i=0;i<Visibles.getSize();i++) {
			colVisibles.add((boolean)Visibles.Get(i));			
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
		String lastSort = GetSortColumn(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		String Order="";
		if (lastSort.startsWith("DESC_")) {
			Order="DESC";
			lastSort=lastSort.substring(5);
		}
		if (!Order.equals("")) {
			return lastSort + " " + Order;
		} else {
			return lastSort;
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
			ABMaterial.SetSortColumn(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), dataField, Order);
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
		header=new TableRowMutable();	
		header.UniqueId = "header";
		for (int i=0;i<HeaderValues.getSize();i++) {
			if (HeaderValues.Get(i) instanceof String) {
				TableValueMutable tv = new TableValueMutable(header.UniqueId + "_" + i + "__", null, (String) HeaderValues.Get(i) , false);
				tv.theme = "defaultheaderfooter";
				header.values.put(header.UniqueId + "_" + i + "__", tv);				
			} else {
				TableValueMutable tv = new TableValueMutable(header.UniqueId + "_" + i + "__", (ABMComponent)HeaderValues.Get(i), "", false);
				tv.theme = "defaultheaderfooter";
				header.values.put(header.UniqueId + "_" + i + "__", tv);
			}
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
		footer=new TableRowMutable();	
		footer.UniqueId="footer";
		this.FooterColSpan=colSpanWidth;
		if (FooterValue instanceof String) {
			footer.values.put(footer.UniqueId + "_0__", new TableValueMutable(footer.UniqueId + "_0__", null, (String) FooterValue,  false));
			if (!footerTheme.equals("")) {
				footer.values.get(footer.UniqueId + "_0__").theme = footerTheme.toLowerCase();
			} else {
				footer.values.get(footer.UniqueId + "_0__").theme = "defaultheaderfooter";
			}
		} else {
			footer.values.put(footer.UniqueId + "_0__", new TableValueMutable(footer.UniqueId + "_0__", (ABMComponent)FooterValue, "", false));
			if (!footerTheme.equals("")) {
				footer.values.get(footer.UniqueId + "_0__").theme = footerTheme.toLowerCase();
			} else {
				footer.values.get(footer.UniqueId + "_0__").theme = "defaultheaderfooter";
			}
		}			
	}	

	public void SetFooters(anywheresoftware.b4a.objects.collections.List FooterValues) {
		footer=new TableRowMutable();	
		footer.UniqueId="footer";
		for (int i=0;i<FooterValues.getSize();i++) {
			if (FooterValues.Get(i) instanceof String) {
				footer.values.put(footer.UniqueId + "_" + i + "__", new TableValueMutable(footer.UniqueId + "_" + i + "__", null, (String) FooterValues.Get(i), false));
			} else {
				footer.values.put(footer.UniqueId + "_" + i + "__", new TableValueMutable(footer.UniqueId + "_" + i + "__", (ABMComponent)FooterValues.Get(i), "", false));
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
	
	protected void RefreshInsert(TableRowMutable r) {
		if (IsBuild) {			
			if (r!=null) {
				String val = BuildRow(r);
				if (!r.InsertBefore.equals("")) {
					ABMaterial.InsertHTMLBefore(Page, "_" + r.InsertBefore, val);						
				} 
				if (!r.InsertAfter.equals("")) {
					ABMaterial.InsertHTMLAfter(Page, "_" + r.InsertAfter, val);						
				}
				if (r.InsertBefore.equals("") && r.InsertAfter.equals("")) {
					ABMaterial.AddHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "_tbody", val);						
				}					
			}
			
			anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
			Params.Initialize();
			Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());			
			Params.Add(false);
			Params.Add(IsSortable);	
			Params.Add(false); // IsCollapse 
			Page.ws.RunFunction("inittable", Params);
			
			String s = "$('[data-abminputmask=\"1\"]').inputmask();";
			Page.ws.Eval(s, null);
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("TableMutable RefreshInsert: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		} 
	}
	
	public void InsertRowAfter(String afterUniqueId, String uniqueId, anywheresoftware.b4a.objects.collections.List RowValues, anywheresoftware.b4a.objects.collections.List themeNames ) {
		if (afterUniqueId=="" && Rows.size()>0) {
			BA.Log("You need to set afterUniqueId!");
			return;
		}
		TableRowMutable row =new TableRowMutable();
		row.UniqueId="_" + uniqueId.toLowerCase();
		row.InsertBefore="";
		row.InsertAfter=afterUniqueId.toLowerCase();
		for (int i=0;i<RowValues.getSize();i++) {
			if (RowValues.Get(i) instanceof String) {
				row.values.put(row.UniqueId.toLowerCase() + "_" + i + "__", new TableValueMutable(row.UniqueId.toLowerCase() + "_" + i + "__",null, (String)RowValues.Get(i), false));
				row.componentKeys.put(row.UniqueId.toLowerCase() + "_" + i + "__",row.UniqueId.toLowerCase() + "_" + i + "__");
			} else {
				TableValueMutable tbv = new TableValueMutable(row.UniqueId.toLowerCase() + "_" + i + "__",(ABMComponent)RowValues.Get(i), "", false);
				row.values.put(row.UniqueId.toLowerCase() + "_" + i + "__", tbv);
				row.componentKeys.put(tbv.value.ID.toLowerCase(),row.UniqueId.toLowerCase() + "_" + i + "__");
			}			
		}
		Rows.put(row.UniqueId,row);
		String rId= "_" + afterUniqueId.toLowerCase();
		if (afterUniqueId.equals("")) {
			RowsList.add(row.UniqueId);
		} else {
			int index=0;
			for(String s : RowsList) {
				if (s.equalsIgnoreCase(rId)) {
					index++;
					RowsList.add(index, row.UniqueId);
					break;
				}
				index++;
			}
		}
		SetRowThemes(uniqueId, themeNames);
			
		RefreshInsert(row);
		
	}
	
	public void InsertRowAfterFixedHeight(String afterUniqueId, String uniqueId, anywheresoftware.b4a.objects.collections.List RowValues, anywheresoftware.b4a.objects.collections.List themeNames , int height) {
		if (afterUniqueId=="" && Rows.size()>0) {
			BA.Log("You need to set afterUniqueId!");
			return;
		}
		TableRowMutable row =new TableRowMutable();
		row.UniqueId="_" + uniqueId.toLowerCase();
		row.InsertBefore="";
		row.InsertAfter=afterUniqueId.toLowerCase();
		row.FixedHeight = height;		
		for (int i=0;i<RowValues.getSize();i++) {
			if (RowValues.Get(i) instanceof String) {
				row.values.put(row.UniqueId.toLowerCase() + "_" + i + "__", new TableValueMutable(row.UniqueId.toLowerCase() + "_" + i + "__",null, (String)RowValues.Get(i), false));
				row.componentKeys.put(row.UniqueId.toLowerCase() + "_" + i + "__",row.UniqueId.toLowerCase() + "_" + i + "__");
			} else {
				TableValueMutable tbv = new TableValueMutable(row.UniqueId.toLowerCase() + "_" + i + "__",(ABMComponent)RowValues.Get(i), "", false);
				row.values.put(row.UniqueId.toLowerCase() + "_" + i + "__", tbv);
				row.componentKeys.put(tbv.value.ID.toLowerCase(),row.UniqueId.toLowerCase() + "_" + i + "__");
			}			
		}
		Rows.put(row.UniqueId,row);
		String rId= "_" + afterUniqueId.toLowerCase();
		if (afterUniqueId.equals("")) {
			RowsList.add(row.UniqueId);
		} else {
			int index=0;
			for(String s : RowsList) {
				if (s.equalsIgnoreCase(rId)) {
					index++;
					RowsList.add(index, row.UniqueId);
					break;
				}
				index++;
			}
		}
		SetRowThemes(uniqueId, themeNames);
		
		RefreshInsert(row);
		
	}	

	public void InsertRowBefore(String beforeUniqueId, String uniqueId, anywheresoftware.b4a.objects.collections.List RowValues, anywheresoftware.b4a.objects.collections.List themeNames ) {
		if (beforeUniqueId=="" && Rows.size()>0) {
			BA.Log("You need to set beforeUniqueId!");
			return;
		}
		
		TableRowMutable row =new TableRowMutable();
		row.UniqueId="_" + uniqueId.toLowerCase();
		row.InsertBefore=beforeUniqueId.toLowerCase();
		row.InsertAfter="";
		for (int i=0;i<RowValues.getSize();i++) {
			if (RowValues.Get(i) instanceof String) {
				row.values.put(row.UniqueId.toLowerCase() + "_" + i + "__", new TableValueMutable(row.UniqueId.toLowerCase() + "_" + i + "__",null, (String)RowValues.Get(i), false));
				row.componentKeys.put(row.UniqueId.toLowerCase() + "_" + i + "__",row.UniqueId.toLowerCase() + "_" + i + "__");
			} else {
				TableValueMutable tbv = new TableValueMutable(row.UniqueId.toLowerCase() + "_" + i + "__",(ABMComponent)RowValues.Get(i), "", false);
				row.values.put(row.UniqueId.toLowerCase() + "_" + i + "__", tbv);
				row.componentKeys.put(tbv.value.ID.toLowerCase(),row.UniqueId.toLowerCase() + "_" + i + "__");
			}			
		}
		Rows.put(row.UniqueId,row);
		String rId= "_" + beforeUniqueId.toLowerCase();
		if (beforeUniqueId.equals("")) {
			RowsList.add(row.UniqueId);
		} else {
			int index=0;
			for(String s : RowsList) {
				if (s.equalsIgnoreCase(rId)) {
					RowsList.add(index, row.UniqueId);
					break;
				}
				index++;
			}
		}
		SetRowThemes(uniqueId, themeNames);
		RefreshInsert(row);
		
	}
	
	
	public void InsertRowBeforeFixedHeight(String beforeUniqueId,String uniqueId, anywheresoftware.b4a.objects.collections.List RowValues, anywheresoftware.b4a.objects.collections.List themeNames, int height) {
		if (beforeUniqueId=="" && Rows.size()>0) {
			BA.Log("You need to set beforeUniqueId!");
			return;
		}
		
		TableRowMutable row =new TableRowMutable();
		row.UniqueId="_" + uniqueId.toLowerCase();
		row.InsertBefore=beforeUniqueId.toLowerCase();
		row.InsertAfter="";
		row.FixedHeight = height;
		for (int i=0;i<RowValues.getSize();i++) {
			if (RowValues.Get(i) instanceof String) {
				row.values.put(row.UniqueId.toLowerCase() + "_" + i + "__", new TableValueMutable(row.UniqueId.toLowerCase() + "_" + i + "__",null, (String)RowValues.Get(i), false));
				row.componentKeys.put(row.UniqueId.toLowerCase() + "_" + i + "__",row.UniqueId.toLowerCase() + "_" + i + "__");
			} else {
				TableValueMutable tbv = new TableValueMutable(row.UniqueId.toLowerCase() + "_" + i + "__",(ABMComponent)RowValues.Get(i), "", false);
				row.values.put(row.UniqueId.toLowerCase() + "_" + i + "__", tbv);
				row.componentKeys.put(tbv.value.ID.toLowerCase(),row.UniqueId.toLowerCase() + "_" + i + "__");
			}			
		}
		Rows.put(row.UniqueId,row);
		String rId= "_" + beforeUniqueId.toLowerCase();
		if (beforeUniqueId.equals("")) {
			RowsList.add(row.UniqueId);
		} else {
			int index=0;
			for(String s : RowsList) {
				if (s.equalsIgnoreCase(rId)) {
					RowsList.add(index, row.UniqueId);
					break;
				}
				index++;
			}
		}
		SetRowThemes(uniqueId, themeNames);
		RefreshInsert(row);
		
	}	
		
	public void RemoveRow(String uniqueId) {
		String rId= "_" + uniqueId.toLowerCase();
		TableRowMutable r = Rows.getOrDefault(rId, null);
		if (r==null) {
			BA.Log("Row not found: " + uniqueId);
			return;
		}
		String firstCellId="";
		for (Entry<String, TableValueMutable> v : r.values.entrySet()) {
			TableValueMutable c = v.getValue();
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
		ABMaterial.RemoveHTMLParent(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + rId);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("TableMutable RemoveRow: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	private void SetRowThemes(String uniqueId, anywheresoftware.b4a.objects.collections.List themeNames) {
		String rowId = "_" + uniqueId.toLowerCase();
		TableRowMutable row = Rows.get(rowId);
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
		TableRowMutable r = Rows.getOrDefault(rowId, null);
		if (r!=null) {
			r.values.get(rowId + "_" + Column + "__").theme = themeName.toLowerCase();
		}
	}
	
	public String GetCellTheme(String uniqueId, int Column) {
		String rowId = "_" + uniqueId.toLowerCase();
		TableRowMutable r = Rows.getOrDefault(rowId, null);
		if (r!=null) {
			return r.values.get(rowId + "_" + Column + "__").theme;
		}
		return "";
	}
	
	public void SetCellTag(String uniqueId,int Column, Object tag) {
		String rowId = "_" + uniqueId.toLowerCase();
		TableRowMutable r = Rows.getOrDefault(rowId, null);
		if (r!=null) {
			r.values.get(rowId + "_" + Column + "__").tag = tag;
		}
	}
	
	public Object GetCellTag(String uniqueId,int Column) {
		String rowId = "_" + uniqueId.toLowerCase();
		TableRowMutable r = Rows.getOrDefault(rowId, null);
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
		TableRowMutable r = Rows.getOrDefault(rId, null);
		if (r==null) {
			BA.Log("Row not found: " + uniqueId);
			return null;
		}
		String key = r.componentKeys.getOrDefault(componentId.toLowerCase(), "");
		if (key.equals("")) {
			BA.Log("No component found with id = " + componentId + ". Is it not a normal string you get with GetString(row,column)?");
			return null;
		}
		TableValueMutable tbv = r.values.getOrDefault(key, null);
		if (tbv==null) {
			BA.Log("No component found with id = " + componentId + ". Is it not a normal string you get with GetString(row,column)?");
			return null;
		}
		return ABMaterial.GetComponent(Page, tbv.value, tbv.value.ID.toLowerCase(), tbv.value.ParentString);
	}	
	
	public ABMComponent GetComponentWriteOnly(String uniqueId, String componentId) {
		String rId= "_" + uniqueId.toLowerCase();
		TableRowMutable r = Rows.getOrDefault(rId, null);
		if (r==null) {
			BA.Log("Row not found: " + uniqueId);
			return null;
		}
		String key = r.componentKeys.getOrDefault(componentId.toLowerCase(), "");
		if (key.equals("")) {
			BA.Log("No component found with id = " + componentId + ". Is it not a normal string you get with GetString(row,column)?");
			return null;
		}
		TableValueMutable tbv = r.values.getOrDefault(key, null);
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
			TableRowMutable row = Rows.getOrDefault(RowsList.get(k), null);
			if (row==null) {
				BA.Log("Something is wrong with the sync of the table. " + RowsList.get(k) + " does not exist!");
				return;
			}
			int cCounter = 0;
			for (Entry<String, TableValueMutable> v : row.values.entrySet()) {
				TableValueMutable r = v.getValue();
				if (r.IsEditable) {
					String cellId = "_" + row.UniqueId + "_" + (cCounter) + "__";
					ABMaterial.GetTableMutableStringValue(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + cellId, r);					
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
		TableRowMutable r = Rows.getOrDefault(rId, null);
		if (r==null) {
			BA.Log("Row not found: " + uniqueId);
			return null;
		}
		String cellId = rId + "_" + (column) + "__";
		TableValueMutable tbv = r.values.getOrDefault(cellId, null);
		if (tbv==null) {
			BA.Log("No string found in row " + uniqueId);
			return null;
		}
		return tbv.GetStringValue();
	}
	
	public void SetString(String uniqueId, int column, String value) {
		String rId= "_" + uniqueId.toLowerCase();
		TableRowMutable r = Rows.getOrDefault(rId, null);
		if (r==null) {
			BA.Log("Row not found: " + uniqueId);
			return;
		}
		TableValueMutable tbv = r.values.getOrDefault(rId + "_" + (column) + "__", null);
		if (tbv==null) {
			BA.Log("No string found in row " + uniqueId);
			return;
		}
		tbv.StringValue=value;
	}
	
	public void RefreshRow(String uniqueId) {
		String rId= "_" + uniqueId.toLowerCase();
		TableRowMutable r = Rows.getOrDefault(rId, null);
		if (r==null) {
			BA.Log("Row not found: " + uniqueId);
			return;
		}
		for (int i=0;i<r.values.size();i++) {
			RefreshCell(uniqueId, i);
		}
		String s = "$('[data-abminputmask=\"1\"]').inputmask();";
		Page.ws.Eval(s, null);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("TableMutable RefreshRow: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public void RefreshFooter() {
		StringBuilder s = new StringBuilder();
		s.append("<tr>\n");
		int counter=0;
		for (Entry<String, TableValueMutable> v : footer.values.entrySet()) {
			TableValueMutable f = v.getValue();
			if (FooterColSpan>0) {
				s.append("<td colspan=\"" + FooterColSpan + "\" id=\"" + f.id + "\" class=\"" + BuildCellClass(f.theme, false) + "\" style=\"" + BuildCellStyle(f.theme, -9999, false) + "\">");
			} else {
				String colVisible="";
				if (!colVisibles.isEmpty()) {
					if (!colVisibles.get(counter)) {
						colVisible = ";display: none";
					}
				}
				
				if (colWidths.isEmpty()) {
					s.append("<td id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + f.id + "\" class=\"" + BuildCellClass(f.theme, false) + "\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(f.theme, -9999, false) + colVisible + "\">");
				} else {
					s.append("<td id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + f.id + "\" class=\"" + BuildCellClass(f.theme, false) + "\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(f.theme, counter, false) + colVisible + "\">");
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
		ABMaterial.ReplaceMyInnerHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "_tfoot", s.toString());
	}
	
	public void RefreshCell(String uniqueId, int column) {
		String rId= "_" + uniqueId.toLowerCase();
		TableRowMutable r = Rows.getOrDefault(rId, null);
		if (r==null) {
			BA.Log("Row not found: " + uniqueId);
			return;
		}
		TableValueMutable tbv = r.values.getOrDefault(rId + "_" + (column) + "__", null);
		if (tbv==null) {
			BA.Log("No string found in row " + uniqueId);
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
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("TableMutable RefreshCell 1: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
			ABMaterial.AddHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + tbv.id, tbv.value.Build());
			
		}
		
		boolean IsActive = r.UniqueId.equals("_" + mActiveRowId);
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + tbv.id);
		j.SetProp("class", BuildCellClass(tbv.theme, IsActive));
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("TableMutable RefreshCell 2: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}	
		
	public void Clear() {
		for (Entry<String, TableRowMutable> row : Rows.entrySet()) {
			row.getValue().CleanUp();
		}
		Rows.clear();
		RowsList.clear();
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
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-nscr");		
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
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		super.Refresh();
		
		if (IsInteractive) {
			GetActiveRow();
		}
		String lastSort = GetSortColumn();
		
		JQueryElement j;
		
		j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-nscr");			
		j.SetHtml(BuildBody());
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());			
		Params.Add(false);
		Params.Add(IsSortable);	
		Params.Add(false); // IsCollapse 
		Page.ws.RunFunction("inittable", Params);
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
	}		
	
	@Override
	protected String Build() {
		if (Theme.ThemeName.equals("default")) {
			Theme.Colorize(Page.CompleteTheme.MainColor);
		}
		StringBuilder s = new StringBuilder();
		ThemeTable l=Theme;
		
		s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-nscr\" class=\"" + l.ZDepth + "\">\n");
		s.append(BuildBody());
		s.append("</div>\n");		
		
		IsBuild=true;
		return s.toString();
	}
	
	@Hide
	protected String BuildClass() {			
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
	
	protected String BuildRow(TableRowMutable row) {
		StringBuilder s = new StringBuilder();
		
		if (row==null) {
			BA.Log("Something is wrong with the sync of the table. Row is null!");
			return "";
		}
		String uniqueId=row.UniqueId;
		boolean IsActive = uniqueId.equals("_" + mActiveRowId);
		if (row.FixedHeight==0) {
			s.append("<tr id=\"" + uniqueId + "\" uniqueid=\"" + uniqueId + "\">\n");
		} else {
			s.append("<tr id=\"" + uniqueId + "\" uniqueid=\"" + uniqueId + "\" style=\"height:" + row.FixedHeight + "px\">\n");
		}
		int counter=0;
		
		
		for (Entry<String, TableValueMutable> v : row.values.entrySet()) {
			String colVisible="";
			TableValueMutable r = v.getValue();
			if (!colVisibles.isEmpty()) {
				if (!colVisibles.get(counter)) {
					colVisible = ";display: none";
				}
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
			
			if (colWidths.isEmpty()) {
				s.append("<td " + colSpan + Editable + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + r.id + "\" class=\"" + BuildCellClass(r.theme, IsActive) + "\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(r.theme, -9999, IsActive) + colVisible + "\">");
			} else {
				s.append("<td " + colSpan + Editable + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + r.id + "\" class=\"" + BuildCellClass(r.theme, IsActive) + "\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(r.theme, counter, IsActive) + colVisible + "\">");
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
		
		s.append("\">");
		int counter=0;
		if (header!=null) {
			s.append("<thead>\n");
			s.append("<tr>\n");
			counter=0;
			
			for (Entry<String, TableValueMutable> v : header.values.entrySet()) {
				TableValueMutable h = v.getValue();
				String colVisible="";
				if (!colVisibles.isEmpty()) {
					if (!colVisibles.get(counter)) {
						colVisible = ";display: none";
					}
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
				
				if (colWidths.isEmpty()) {
					s.append("<th fieldname=\"" + dataField + "\"  id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + h.id + "\" class=\"" + Sortable + BuildCellClass(h.theme, false) + "\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(h.theme,-9999, false) + colVisible + HeaderHeightPxStr + "\">");
				} else {
					s.append("<th fieldname=\"" + dataField + "\"  id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + h.id + "\" class=\"" + Sortable + BuildCellClass(h.theme, false) + "\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(h.theme,counter, false) + colVisible + HeaderHeightPxStr + "\">");
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
		s.append("<tbody id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "_tbody\" class=\"" +  Theme.BackColor + " " + Theme.BackColorIntensity + "\">\n");			
		
		for (int k=0;k<RowsList.size();k++) {
			s.append(BuildRow(Rows.getOrDefault(RowsList.get(k),null)));			
		}		
		s.append("</tbody>\n");
		if (footer!=null) {
			s.append("<tfoot id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "_tfoot\">\n");
			s.append("<tr>\n");
			counter=0;
			for (Entry<String, TableValueMutable> v : footer.values.entrySet()) {
				TableValueMutable f = v.getValue();
				if (FooterColSpan>0) {
					s.append("<td colspan=\"" + FooterColSpan + "\" id=\"" + f.id + "\" class=\"" + BuildCellClass(f.theme, false) + "\" style=\"" + BuildCellStyle(f.theme, -9999, false) + "\">");
				} else {
					String colVisible="";
					if (!colVisibles.isEmpty()) {
						if (!colVisibles.get(counter)) {
							colVisible = ";display: none";
						}
					}
					
					if (colWidths.isEmpty()) {
						s.append("<td id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + f.id + "\" class=\"" + BuildCellClass(f.theme, false) + "\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(f.theme, -9999, false) + colVisible + "\">");
					} else {
						s.append("<td id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + f.id + "\" class=\"" + BuildCellClass(f.theme, false) + "\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(f.theme, counter, false) + colVisible + "\">");
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
					return "border: " + l.BorderWidth + "px solid " + ABMaterial.GetColorStrMap(l.BorderColor, l.BorderColorIntensity) + "; vertical-align: " + l.VerticalAlign;
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
	
	protected class TableRowMutable implements java.io.Serializable {
		
		private static final long serialVersionUID = -6575821914322594837L;
		
		public String UniqueId="";
		public Map<String, TableValueMutable> values = new LinkedHashMap<String,TableValueMutable>();
		public Map<String,String> componentKeys = new LinkedHashMap<String,String>();
		public int FixedHeight=0;
		public String InsertBefore="";
		public String InsertAfter="";
		
		TableRowMutable() {
			
		}
		
		public TableRowMutable Clone() {
			TableRowMutable r = new TableRowMutable();
			for (Entry<String, TableValueMutable> v : values.entrySet()) {
				r.values.put(v.getKey(), v.getValue().Clone());				
			}
			for (Entry<String, String> v : componentKeys.entrySet()) {
				r.componentKeys.put(v.getKey(), v.getValue());				
			}
			
			r.UniqueId=UniqueId;
			r.FixedHeight=FixedHeight;
			return r;
		}
		
		public void CleanUp() {
			for (Entry<String, TableValueMutable> v : values.entrySet()) {
				v.getValue().value=null;			
			}
		}
	}
	
	protected class TableValueMutable implements java.io.Serializable {
		
		private static final long serialVersionUID = 5613766983236213770L;
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
		
		TableValueMutable() {
			
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
		
		TableValueMutable(String id, ABMComponent value, String stringValue, boolean isEditable) {
			this.id=id;
			this.StringValue=stringValue;
			this.value = value;
			this.IsEditable = isEditable;
			if (value!=null) {
				value.AddArrayName(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + id.toLowerCase());
			}
		}
		
		TableValueMutable(String id, ABMComponent value, String stringValue, boolean isEditable, int colSpan) {
			this.id=id;
			this.StringValue=stringValue;
			this.value = value;
			this.IsEditable = isEditable;
			if (value!=null) {
				value.AddArrayName(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + id.toLowerCase());
			}
			this.ColSpan=colSpan;
		}
		
		protected TableValueMutable Clone() {
			TableValueMutable v = new TableValueMutable();
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
		ABMTableMutable c = new ABMTableMutable();
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
		for (Entry<String, TableRowMutable> r : Rows.entrySet()) {
			c.Rows.put(r.getKey(), r.getValue().Clone());
		}
		for (String s: RowsList) {
			c.RowsList.add(s);
		}
		return c;
	}
	
}

