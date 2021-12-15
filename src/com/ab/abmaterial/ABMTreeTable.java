package com.ab.abmaterial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ab.abmaterial.ThemeTreeTable.TreeTableCell;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;

@Author("Alain Bailleul")  
@ShortName("ABMTreeTable")
@Events(values={"Clicked(TreeRowId as String, TreeCellId as String)","NeedsRefreshChildren(rowId As String)", "Dropped(Params as Map)"})
public class ABMTreeTable extends ABMComponent {
	
	private static final long serialVersionUID = -3235967900629786977L;
	protected ThemeTreeTable Theme=new ThemeTreeTable();
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	protected boolean IsResponsive=false;
	protected Map<String, ABMTreeTableRow> Rows = new LinkedHashMap<String, ABMTreeTableRow>();
	public boolean IgnoreFormattingCodes=false;
	protected boolean IsInteractive=false;
	protected String ParentArrayName="";
	protected Map<Integer, String> Depths = new LinkedHashMap<Integer,String>();	
	public int ColWidthPx=15;
	public int TotalColSpan=0;	
	public String CollapseTooltip="Collapse";
	public String ExpandTooltip="Expand";
	protected boolean IsTreeInitialized=false;
	protected int counter=0;
	public int ReverseDuration=300;
	
	public boolean IsTextSelectable=true;
	
	public void Initialize(ABMPage page, String id, boolean isInteractive, String themeName, int colWidthPx, anywheresoftware.b4a.objects.collections.List depthThemes, int totalColspan) {
		this.ID = id;			
		this.Page = page;		
		this.Type = ABMaterial.UITYPE_TREETABLE;
		this.IsInteractive=isInteractive;
		this.ColWidthPx = colWidthPx;
		this.TotalColSpan = totalColspan;
		SetDepthsThemes(depthThemes);
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.TreeTables.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.TreeTables.get(themeName.toLowerCase()).Clone();				
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
		return ArrayName.toLowerCase() + ID.toLowerCase();
	}
	
	@Hide
	protected void SetDepthsThemes(anywheresoftware.b4a.objects.collections.List themes) {
		for (int Index=0;Index<themes.getSize();Index++) {
			Depths.put(Index+1, (String) themes.Get(Index));
		}
	}
		
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;		
	}
	
	public void CollapseAll() {
		ABMaterial.TreeTableCollapseAll(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
	}
	
	public void ExpandAll() {
		ABMaterial.TreeTableExpandAll(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
	}
	
	public void Collapse(String rowId) {
		ABMTreeTableRow lrow = Rows.getOrDefault(rowId.toLowerCase(), null);
		if (lrow==null) {
			BA.Log("No row found with id " + rowId);
			return;
		}
		ABMaterial.TreeTableCollapse(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), lrow.TreeRowId);
	}
	
	public void Expand(String rowId) {
		ABMTreeTableRow lrow = Rows.getOrDefault(rowId.toLowerCase(), null);
		if (lrow==null) {
			BA.Log("No row found with id " + rowId);
			return;
		}
		ABMaterial.TreeTableExpand(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), lrow.TreeRowId);
	}
	
	public void AddRow(ABMTreeTableRow row) {
		String theme=Depths.get(row.Depth);
		if (!row.OverruleDefaultDepthThemeName.equals("") && !row.OverruleDefaultDepthThemeName.equalsIgnoreCase("default")) {
			theme = row.OverruleDefaultDepthThemeName;
		}
		for (int i=0;i<row.cells.size();i++) {
			ABMTreeTableCell cell = (ABMTreeTableCell) row.cells.get(i);
			cell.Level = row.Depth;
			if (cell.theme.equals("")) {
				cell.theme = theme;
			}
		}
		if (!row.IsFooterForRowId.equals("")) {
			ABMTreeTableRow headRow = Rows.getOrDefault(row.IsFooterForRowId.toLowerCase(), null);
			if (headRow==null) {
				BA.Log("No header row found with id " + row.IsFooterForRowId);				
			} else {
				headRow.HasFooterRowId=row.TreeRowId;
			}
		}
		if (!row.TreeRowParentId.equals("")) {
			ABMTreeTableRow lrowpar = Rows.getOrDefault(row.TreeRowParentId.toLowerCase(), null);
			if (lrowpar==null) {
				BA.Log("No parent row found with id " + row.TreeRowParentId);				
			} else {
				lrowpar.TreeRowChildIds.add(row.TreeRowId);
			}
		}
		row.Status = 1; //' new
		Rows.put(row.TreeRowId.toLowerCase(),row);
	}
	
	public void RemoveRow(String rowId) {
		ABMTreeTableRow lrow = Rows.getOrDefault(rowId.toLowerCase(), null);
		if (lrow==null) {
			BA.Log("No row found with id " + rowId);
			return;
		}
		lrow.Status = 2; // delete
		if (!lrow.HasFooterRowId.equals("")) {
			ABMTreeTableRow lrowfoot = Rows.getOrDefault(lrow.HasFooterRowId.toLowerCase(), null);
			lrowfoot.Status = 2;
		}
		if (!lrow.IsFooterForRowId.equals("")) {
			ABMTreeTableRow lrowhead = Rows.getOrDefault(lrow.IsFooterForRowId.toLowerCase(), null);
			lrowhead.Status = 2;
		}
				
	}
	
	public void Sync(String rowId, String oldRowParentId, String newRowParentId) {
		ABMTreeTableRow lrow = Rows.getOrDefault(rowId.toLowerCase(), null);
		if (lrow==null) {
			BA.Log("No row found with id " + rowId);
			return;
		}
		ABMTreeTableRow lrowpar = Rows.getOrDefault(newRowParentId.toLowerCase(), null);
		if (lrowpar==null) {
			BA.Log("No parent row found with id " + newRowParentId);
			return;
		}
		ABMTreeTableRow lrowprev = Rows.getOrDefault(oldRowParentId.toLowerCase(), null);
		if (lrowprev==null) {
			BA.Log("No previous parent row found with id " + oldRowParentId);
			return;
		}
		int toRem=-1;
		for (int j=0;j<lrowprev.TreeRowChildIds.size();j++) {
			if (lrowprev.TreeRowChildIds.get(j).equals(rowId)) {
				toRem=j;
				break;
			}				
		}
		if (toRem>-1) {
			lrowprev.TreeRowChildIds.remove(toRem);
		}
		lrowpar.TreeRowChildIds.add(rowId);
		lrow.TreeRowParentId=newRowParentId;
		lrow.Depth=lrowpar.Depth+1;
		UpdateChildren(lrow,lrow.Depth+1);	
	}
	
	/**
	 * Get a sequential list of rowIds from a branch, including all siblings
	 * If the rowId has a footer, it will also be returned
	 * Using an empty string as rowId will return the whole tree 
	 * 
	 * NOTE: if you did not use sorting, the returned order is not guaranteed the order you see in the tree in the browser!
	 */
	public anywheresoftware.b4a.objects.collections.List GetBranch(String rowId) {
		anywheresoftware.b4a.objects.collections.List tree = new anywheresoftware.b4a.objects.collections.List();
		tree.Initialize();
		ABMTreeTableRow lrow=null;
		if (rowId.equals("")) {
			ArrayList<SortClass> tmpList = new ArrayList<SortClass>();
			int p=0;
			for (Entry<String, ABMTreeTableRow> row : Rows.entrySet()) {
				lrow = row.getValue();
				if (lrow.IsFooterForRowId.equals("")) {
					if (lrow.TreeRowParentId.equals("")) {
						tmpList.add(new SortClass("" + p, lrow.TreeRowId));						
					}
				}							
			}
			for (int pp=0;pp<tmpList.size();pp++) {
				String s = tmpList.get(pp).Extra;
				ABMTreeTableRow lrowch = Rows.getOrDefault(s.toLowerCase(), null);
				if (lrowch==null) {
					BA.Log("No row found with id " + s);
					//return tree;
				}
				tree = TraverseBranch(lrowch, tree);
			}
		} else {
			lrow = Rows.getOrDefault(rowId.toLowerCase(), null);
			if (lrow==null) {
				BA.Log("No row found with id " + rowId);
				return tree;
			}
			tree = TraverseBranch(lrow, tree);
		}
		
		return tree;
	}
	
	protected anywheresoftware.b4a.objects.collections.List TraverseBranch(ABMTreeTableRow lrow, anywheresoftware.b4a.objects.collections.List tree) {
		tree.Add(lrow.TreeRowId);
		String Footer=lrow.HasFooterRowId;
		if (Footer.equals("")) {
			Footer = "-ABM$1-2";
		}
		
		ArrayList<SortClass> tmpList = new ArrayList<SortClass>();
		
		int sortIndex=1;
		if (lrow.SortIndex>-1) {
			sortIndex=lrow.SortIndex;
		}
		for (int p=0;p<lrow.TreeRowChildIds.size();p++) {
			String s = (String)lrow.TreeRowChildIds.get(p);
			ABMTreeTableRow lrowch = Rows.getOrDefault(s.toLowerCase(), null);
			if (lrowch.IsFooterForRowId.equals("")) { 
			
				if (sortIndex>0) { 
					ABMTreeTableCell c = (ABMTreeTableCell) lrowch.cells.get(sortIndex-1);
					
					if (c.StringValue.equals("")) {
						tmpList.add(new SortClass("" + p, s));					
					} else {
						tmpList.add(new SortClass(c.StringValue, s));					
					}
				} else {
					tmpList.add(new SortClass("" + p, s));
				}
			}			
		}

		Collections.sort(tmpList, new Comparator<SortClass>() {
			  public int compare(SortClass c1, SortClass c2) {
				 return c1.Sorter.compareTo(c2.Sorter);
	    }});
		
		for (int p=0;p<tmpList.size();p++) {
			String s = tmpList.get(p).Extra;
			
			ABMTreeTableRow lrowch = Rows.getOrDefault(s.toLowerCase(), null);
			if (lrowch==null) {
				BA.Log("No row found with id " + s);			
			}
			tree = TraverseBranch(lrowch, tree);
		}
		if (!Footer.equals("-ABM$1-2")) {
			tree.Add(Footer);
		}
		return tree;
	}
	
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.TreeTables.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.TreeTables.get(themeName.toLowerCase()).Clone();				
			}
		}
	}
	
	public String GetString(String rowId, int cellIndex) {
		ABMTreeTableRow lrow = Rows.getOrDefault(rowId.toLowerCase(), null);
		if (lrow==null) {
			BA.Log("No row found with id " + rowId);
			return "";
		}
		if (cellIndex<1 || cellIndex>lrow.cells.size()) {
			BA.Log("No cell found with index " + cellIndex);
			return "";
		}
		ABMTreeTableCell tbv = (ABMTreeTableCell) lrow.cells.get(cellIndex-1);
		return tbv.GetStringValue();		
	}
	
	public void SetString(String rowId, int cellIndex, String value) {
		ABMTreeTableRow lrow = Rows.getOrDefault(rowId.toLowerCase(), null);
		if (lrow==null) {
			BA.Log("No row found with id " + rowId);
			return;
		}
		if (cellIndex<1 || cellIndex>lrow.cells.size()) {
			BA.Log("No cell found with index " + cellIndex);
			return;
		}
		ABMTreeTableCell tbv = (ABMTreeTableCell) lrow.cells.get(cellIndex-1);
		tbv.StringValue=value;		
	}
	
	public ABMComponent GetComponent(String rowId, int cellIndex) {
		ABMTreeTableRow lrow = Rows.getOrDefault(rowId.toLowerCase(), null);
		if (lrow==null) {
			BA.Log("No row found with id " + rowId);
			return null;
		}
		return lrow.GetCellComponent(Page, cellIndex);		
	}
	
	public void Clear() {		
		for (Entry<String, ABMTreeTableRow> row : Rows.entrySet()) {
			row.getValue().CleanUp();
		}
		Rows.clear();
		IsTreeInitialized=false;
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
		CleanUp();
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
	}
	
	@Override
	protected void FirstRun() {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());		
		Page.ws.RunFunction("inittooltipped", Params);
		super.FirstRun();
	}
	
	public void RefreshChildren(String rowId) {
		ABMTreeTableRow lrow = Rows.getOrDefault(rowId.toLowerCase(), null);
		if (lrow==null) {
			BA.Log("No row found with id " + rowId);
			return;
		}
		lrow.Reset(lrow.Depth);
		if (lrow.TreeRowParentId.equals(rowId)) {				
			ABMTreeTableCell cell = lrow.GetNextCell("");
			while (cell!=null) {
				if (cell.value!=null) {						
					if (cell.value.IsBuild) {
						cell.value.Refresh();				
					}
				}
				cell = lrow.GetNextCell("");
			}
		}
		for (int p=0;p<lrow.TreeRowChildIds.size();p++) {
			String chid = (String) lrow.TreeRowChildIds.get(p);
			RefreshChildren(chid);
		}
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("TreeTable RefrshChildren: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		super.Refresh();
		JQueryElement j;
		j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-nscr");			
		
		if (!IsTreeInitialized) {
			
			j.SetHtml(BuildBody());
			anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
			Params.Initialize();
			Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());			
			Params.Add(false);
			Params.Add(false);	
			Params.Add(true); // IsCollapse
			
			Page.ws.RunFunction("inittable", Params);
			
			anywheresoftware.b4a.objects.collections.List Params3 = new anywheresoftware.b4a.objects.collections.List();
			Params3.Initialize();
			Params3.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
			Page.ws.RunFunction("sorttree", Params3);
			
			anywheresoftware.b4a.objects.collections.List Params2 = new anywheresoftware.b4a.objects.collections.List();
			Params2.Initialize();
			Params2.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
			Params2.Add(ReverseDuration);
			Page.ws.RunFunction("reinitclickstreetable", Params2);
			
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("TreeTable Refresh 1: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
			
			IsTreeInitialized=true;
			return;
		}
		
		List<String> ToRemove=new ArrayList<String>();
			
		
		for (Entry<String, ABMTreeTableRow> row : Rows.entrySet()) {
			ABMTreeTableRow lrow = row.getValue();
			lrow.Reset(lrow.Depth);
			ABMTreeTableCell cell = lrow.GetNextCell("");
			
			while (cell!=null) {
				if (cell.value!=null) {
					if (cell.value.IsBuild && !cell.value.TreeNeedsFirstRun ) {
						cell.value.RefreshInternal(DoFlush);				
					} else {
						cell.value.TreeNeedsFirstRun=true;
					}					
				} else {
					String cellId = "";
					if (cell.id.startsWith("empty")) {
						cellId = cell.id;
					} else {
						cellId = ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + cell.id;
					}
					
					if (!cell.IconName.equals("")) {
						j = Page.ws.GetElementById(cellId + "-icon");
						TreeTableCell l = Theme.Cell(cell.theme);
						
						switch (cell.IconName.substring(0, 3).toLowerCase()) {
						case "mdi":
							j.SetProp("class", cell.IconName + " " + ABMaterial.GetColorStr(l.ForeColor, l.ForeColorIntensity, "text") + " small");
							break;
						case "fa ":
						case "fa-":
							j.SetProp("class", cell.IconAwesomeExtra + " " + cell.IconName + " " + ABMaterial.GetColorStr(l.ForeColor, l.ForeColorIntensity, "text") + " small");
							break;
						case "abm":
							j.SetHtml(Page.svgIconmap.getOrDefault(cell.IconName.toLowerCase(), ""));
							break;
						default:
							j.SetProp("class", "material-icons " + ABMaterial.GetColorStr(l.ForeColor, l.ForeColorIntensity, "text") + " small");
							j.SetText(ABMaterial.HTMLConv().htmlEscape(cell.IconName, Page.PageCharset));							
						}
					} else {
						j = Page.ws.GetElementById(cellId + "-text");
						if (IgnoreFormattingCodes) {							
							j.SetHtml(ABMaterial.HTMLConv().htmlEscape(cell.StringValue, Page.PageCharset));							
						} else {
							String vv = ABMaterial.HTMLConv().htmlEscape(cell.StringValue, Page.PageCharset);
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
							
							j.SetHtml(vv);								
						}
						
					}
					
				}
				cell = lrow.GetNextCell("");
			}				
			switch (lrow.Status) {
			case 0: 
				
				break;
			case 1: // new
				anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
				Params.Initialize();
				Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());			
				Params.Add(lrow.TreeRowParentId);
				Params.Add(BuildNode(lrow));
				Page.ws.RunFunction("treetableaddnode", Params);
				lrow.Status=0;
				break;
			case 2: // delete
				ToRemove.add(row.getKey());
				anywheresoftware.b4a.objects.collections.List Params2 = new anywheresoftware.b4a.objects.collections.List();
				Params2.Initialize();
				Params2.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
				Params2.Add(lrow.TreeRowId);			
				Page.ws.RunFunction("treetableremovenode", Params2);
				break;
			case 3: // replace
				
				break;
			}
			
		}					

		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("TreeTable Refresh 2: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
		
		for (int k=0;k<ToRemove.size();k++) {
			ABMTreeTableRow lrow = Rows.get(ToRemove.get(k).toLowerCase());
			if (!lrow.TreeRowParentId.equals("")) {
				ABMTreeTableRow lrowpar = Rows.getOrDefault(lrow.TreeRowParentId.toLowerCase(), null);
				if (lrowpar!=null) {
					int toRem=-1;
					for (int n=0;n<lrowpar.TreeRowChildIds.size();n++) {
						if (lrowpar.TreeRowChildIds.get(n).equals(ToRemove.get(k).toLowerCase())) {
							toRem=n;
							break;
						}				
					}
					if (toRem>-1) {
						lrowpar.TreeRowChildIds.remove(toRem);
					}
				}
			}
			lrow.Reset(lrow.Depth);
			ABMTreeTableCell cell = lrow.GetNextCell("");
			while (cell!=null) {
				if (cell.value!=null) {
					cell.value.RemoveMe();
				}
				cell = lrow.GetNextCell("");
			}
			RemoveChildren(lrow);
				
			Rows.remove(ToRemove.get(k));
		}
			
		for (Entry<String, ABMTreeTableRow> row : Rows.entrySet()) {
			ABMTreeTableRow lrow = row.getValue();
			lrow.Reset(lrow.Depth);
			ABMTreeTableCell cell = lrow.GetNextCell("");
			while (cell!=null) {
				if (cell.value!=null) {
					if (cell.value.TreeNeedsFirstRun) {
						cell.value.FirstRun();				
					}
					cell.value.TreeNeedsFirstRun=false;
				}
				cell = lrow.GetNextCell("");
			}
		}
		
		anywheresoftware.b4a.objects.collections.List Params3 = new anywheresoftware.b4a.objects.collections.List();
		Params3.Initialize();
		Params3.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		Page.ws.RunFunction("sorttree", Params3);
		
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		Params.Add(ReverseDuration);
		Page.ws.RunFunction("reinitclickstreetable", Params);
		
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("TreeTable Refresh 3: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}		
	}
	
	protected void RemoveChildren(ABMTreeTableRow lrowpar) {
		for (int p=0;p<lrowpar.TreeRowChildIds.size();p++) {
			String chid = (String) lrowpar.TreeRowChildIds.get(p);
			ABMTreeTableRow lrow = Rows.get(chid.toLowerCase());
			lrow.Reset(lrow.Depth);
			ABMTreeTableCell cell = lrow.GetNextCell("");
			while (cell!=null) {
				if (cell.value!=null) {
					cell.value.RemoveMe();
				}
				cell = lrow.GetNextCell("");
			}
			RemoveChildren(lrow);
				
			Rows.remove(chid.toLowerCase());
		}
	}
	
	protected void UpdateChildren(ABMTreeTableRow lrowpar, int newDepth) {
		for (int p=0;p<lrowpar.TreeRowChildIds.size();p++) {
			String chid = (String) lrowpar.TreeRowChildIds.get(p);
			ABMTreeTableRow lrow = Rows.get(chid.toLowerCase());
			lrow.Depth=newDepth;
			UpdateChildren(lrow, newDepth+1);
		}
	}
	
	@Override
	protected String Build() {
		if (Theme.ThemeName.equals("default")) {
			Theme.Colorize(Page.CompleteTheme.MainColor);
		}
		StringBuilder s = new StringBuilder();
		String selectable="";
		if (!IsTextSelectable) {
			selectable = " notselectable ";
		}
		s.append("<div  id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-nscr\" class=\"" + selectable + mIsPrintableClass + mIsOnlyForPrintClass + "\">\n");
		s.append(BuildBody());	
		s.append("</div>\n");
		IsBuild=true;
		IsTreeInitialized=true;
		return s.toString();
	}
	
	@Hide
	protected String BuildClass() {			
		StringBuilder s = new StringBuilder();
		s.append(super.BuildExtraClasses());
		ThemeTreeTable l=Theme;
		s.append(ABMaterial.GetColorStr(l.BackColor, l.BackColorIntensity, ""));
		if (!ToolTipText.equals("")) {
			s.append(" tooltipped ");
		}		
		s.append(l.ZDepth + " ");
		if (IsResponsive) {
			s.append(" responsive-table ");
		}	
		if (IsInteractive) {
			s.append(" tableinteractive ");
		}
		s.append(" " + mVisibility + " ");
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
		s.append("<table " + toolTip + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" colstring=\"" + CollapseTooltip + "\" expstring=\"" + ExpandTooltip + "\" name=\"" + ID.toLowerCase() + "\" evname=\"" + ID.toLowerCase() + "\" class=\"" );		
		s.append(BuildClass());
		
		s.append("\" style=\"table-layout: fixed\">");			
		
		s.append("<tbody>\n");			
		for (Entry<String, ABMTreeTableRow> row : Rows.entrySet()) {
			ABMTreeTableRow lrow = row.getValue();
			s.append(BuildNode(lrow));
			lrow.Status=0;
		}		
		s.append("</tbody>\n");
		
		s.append("</table>\n");
		return s.toString();
	}
	
	protected Map<Integer, String> GetParentThemes(ABMTreeTableRow lrow) {
		Map<Integer,String >ret = new LinkedHashMap<Integer,String>();
		ABMTreeTableRow par=lrow.CloneNoCells();
		for (int lev=lrow.Depth-1;lev>0;lev--) {
			if (par==null) {
				ret.put(lev, Depths.get(lev));
			} else {
				par = Rows.getOrDefault(par.TreeRowParentId.toLowerCase(), null);
				if (par==null) {
					ret.put(lev, Depths.get(lev));
				} else {
					if (par.OverruleDefaultDepthThemeName.equals("") || par.OverruleDefaultDepthThemeName.equalsIgnoreCase("default")) {
						ret.put(lev, Depths.get(lev));
					} else {
						ret.put(lev, par.OverruleDefaultDepthThemeName);
					}
				}
			}			
		}
		return ret;
	}
	
	protected String GetCleanSortValue(ABMTreeTableRow lrow, boolean isFooter) {
		String sorter="";
		if (isFooter) {
			ABMTreeTableRow lrowpar = Rows.getOrDefault(lrow.IsFooterForRowId.toLowerCase(), null);
			ABMTreeTableCell c = (ABMTreeTableCell) lrowpar.cells.get(lrowpar.SortIndex-1);
			sorter = c.StringValue;
		} else {
			ABMTreeTableCell c = (ABMTreeTableCell) lrow.cells.get(lrow.SortIndex-1);
			sorter = c.StringValue;
		}
		sorter = sorter.replace("\"", " ");
		sorter = sorter.replace("\n", " ");
		sorter = sorter.replace("\r", " ");
		return sorter.toUpperCase();
	}
	
	protected String BuildNode(ABMTreeTableRow lrow) {
		Map<Integer, String> OverruleDepths = GetParentThemes(lrow);
		StringBuilder s = new StringBuilder();
		String parentId="";
		if (!lrow.TreeRowParentId.equals("")) {
			parentId="data-tt-parent-id=\"" + lrow.TreeRowParentId + "\"";
		}
		String myDrops="";
		String sortingClass="";
		String sortingIndex="";
		if (lrow.SortIndex>-1) {
			sortingClass=" sortabletree ";
			sortingIndex = " data-tt-sort=\"" + (lrow.SortIndex) + "\" ";
		}
		String footerId="";
		if (!lrow.HasFooterRowId.equals("")) {
			footerId = " data-tt-footer=\"" + lrow.HasFooterRowId + "\" ";	
		}
		String footerforId="";
		if (!lrow.IsFooterForRowId.equals("")) {
			footerforId = " data-tt-footerfor=\"" + lrow.IsFooterForRowId + "\" ";
		}
		
		if (!lrow.mDragDropTreeRowName.equals("")) {
			if (lrow.DragDropTreeRowNames.isEmpty()) {
				s.append("<tr " + sortingIndex + " data-tt-view=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" data-tt-id=\"" + lrow.TreeRowId + "\" " + parentId + " data-tt-column=\"" + (lrow.Depth-1) + "\" data-tt-color=\"" + lrow.IconColorThemeName.toLowerCase() + "\" data-tt-drag=\"" + lrow.mDragDropTreeRowName.toLowerCase() + "\" class=\"abmtreedraggable" + sortingClass + "\"" + footerId + footerforId + ">\n");
			} else {
				myDrops=",";
				for (String skey: lrow.DragDropTreeRowNames.keySet()) {
					myDrops+=skey+",";
				}
				s.append("<tr " + sortingIndex + " data-tt-view=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" data-tt-id=\"" + lrow.TreeRowId + "\" " + parentId + " data-tt-column=\"" + (lrow.Depth-1) + "\" data-tt-color=\"" + lrow.IconColorThemeName.toLowerCase() + "\" data-tt-drag=\"" + lrow.mDragDropTreeRowName.toLowerCase() + "\" class=\"abmtreedraggable abmtreedroppable" + sortingClass + "\" data-tt-drops=\"" + myDrops + "\"" + footerId + footerforId + ">\n");
			}
		} else {
			if (lrow.DragDropTreeRowNames.isEmpty()) {
				s.append("<tr " + sortingIndex + " data-tt-view=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" data-tt-id=\"" + lrow.TreeRowId + "\" " + parentId + " data-tt-column=\"" + (lrow.Depth-1) + "\" data-tt-color=\"" + lrow.IconColorThemeName.toLowerCase() + "\" class=\"" + sortingClass + "\"" + footerId + footerforId + ">\n");
			} else {
				myDrops=",";
				for (String skey: lrow.DragDropTreeRowNames.keySet()) {
					myDrops+=skey+",";
				}
				s.append("<tr " + sortingIndex + " data-tt-view=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" data-tt-id=\"" + lrow.TreeRowId + "\" " + parentId + " data-tt-column=\"" + (lrow.Depth-1) + "\" data-tt-color=\"" + lrow.IconColorThemeName.toLowerCase() + "\" class=\"abmtreedroppable" + sortingClass + "\" data-tt-drops=\"" + myDrops + "\"" + footerId + footerforId + ">\n");
			}
		}
		String theme = ""; 		
		if (lrow.OverruleDefaultDepthThemeName.equals("") || lrow.OverruleDefaultDepthThemeName.equalsIgnoreCase("default")) {
			theme = Depths.get(lrow.Depth);
		} else {
			theme = lrow.OverruleDefaultDepthThemeName;
		}
		int LastColEnd=0;
		for (int lev=1;lev<lrow.Depth;lev++) {
			s.append("<td id=\"emptyprelcars_" + counter + "_" + (lev) + "__\" class=\"abmprelcars " + BuildCellClass(OverruleDepths.get(lev)) + "\" name=\"" + ID.toLowerCase() + "\" evname=\"" + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(BuildCellClass(OverruleDepths.get(lev)), 1) + "\">");
			LastColEnd++;
		}
		lrow.Reset(lrow.Depth);
		ABMTreeTableCell cell = lrow.GetNextCell(theme);
		while (cell!=null) {
			LastColEnd += cell.ColSpan;
			String colVisible="";
			if (!cell.IsVisible) {
				colVisible = ";display: none";					
			}
			String Editable="";
			if (cell.IsEditable) {
				Editable = " contenteditable=\"true\" ";
			}
			String colSpan="";
			colSpan=" colspan=\"" + cell.ColSpan + "\"";
			
			String cellId = "";
			if (cell.id.startsWith("empty")) {
				cellId = cell.id;
			} else {
				cellId = ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + cell.id;
			}			
			s.append("<td " + colSpan + Editable + " id=\"" + cellId + "\" class=\"" + BuildCellClass(cell.theme) + "\" name=\"" + ID.toLowerCase() + "\" evname=\"" + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(cell.theme, cell.ColSpan) + colVisible + "\">");
			if (cell.value!=null) {
				s.append(cell.value.Build());	
				cell.value.TreeNeedsFirstRun=true;
			} else {
				if (!cell.IconName.equals("")) {
					TreeTableCell l = Theme.Cell(cell.theme);
					
					switch (cell.IconName.substring(0, 3).toLowerCase()) {
					case "mdi":
						s.append("<i id=\"" + cellId + "-icon\" class=\"" + cell.IconName + " " + ABMaterial.GetColorStr(l.ForeColor, l.ForeColorIntensity, "text") + " small\"></i>");
						break;
					case "fa ":
					case "fa-":
						s.append("<i id=\"" + cellId + "-icon\" class=\"" + cell.IconAwesomeExtra + " " + cell.IconName + " " + ABMaterial.GetColorStr(l.ForeColor, l.ForeColorIntensity, "text") + " small\"></i>");
						break;
					case "abm":
						s.append("<i id=\"" + cellId + "-icon\" class=\"" + ABMaterial.GetColorStr(l.ForeColor, l.ForeColorIntensity, "text") + " small\">" + Page.svgIconmap.getOrDefault(cell.IconName.toLowerCase(), "") + "</i>");
						break;
					default:
						s.append("<i id=\"" + cellId + "-icon\" class=\"material-icons " + ABMaterial.GetColorStr(l.ForeColor, l.ForeColorIntensity, "text") + " small\">" + ABMaterial.HTMLConv().htmlEscape(cell.IconName, Page.PageCharset) + "</i>");													
					}
				} else {
					s.append("<span style=\"padding: 0\" id=\"" + cellId + "-text\">");
					if (IgnoreFormattingCodes) {
						s.append(ABMaterial.HTMLConv().htmlEscape(cell.StringValue, Page.PageCharset));
					} else {
						String vv = ABMaterial.HTMLConv().htmlEscape(cell.StringValue, Page.PageCharset);
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
					s.append("</span>\n");
				}
			}
			s.append("</td>\n");
			cell = lrow.GetNextCell(theme);
			counter++;
		}
		if (LastColEnd<TotalColSpan) {
			s.append("<td colspan=\"" + (TotalColSpan-LastColEnd) + "\" id=\"emptyinnerlcars\" class=\"" + BuildCellClass(theme) + "\" name=\"" + ID.toLowerCase() + "\" evname=\"" + ID.toLowerCase() + "\" style=\"" + BuildCellStyle(BuildCellClass(theme), TotalColSpan-LastColEnd) + "\">");
			
		}
		s.append("</tr>\n");
		return s.toString();
	}
	
	protected String BuildCellClass(String themeName) {
		TreeTableCell l = Theme.Cell(themeName);		
		StringBuilder s = new StringBuilder();
		s.append(l.ZDepth + " ");
		s.append(l.Align + " ");
		s.append(ABMaterial.GetColorStr(l.BackColor, l.BackColorIntensity, "") + " " + ABMaterial.GetColorStr(l.ForeColor, l.ForeColorIntensity, "text"));
		return s.toString();
	}
	
	protected String BuildCellStyle(String themeName, int colSpan) {
		TreeTableCell l = Theme.Cell(themeName);
		return "padding: 2px;width: " + colSpan*ColWidthPx + "px;border: " + l.BorderWidth + "px solid " + ABMaterial.GetColorStrMap(l.BorderColor, l.BorderColorIntensity) + "; vertical-align: " + l.VerticalAlign;
		
	}
	
	@Override
	protected void Prepare() {	
		this.IsBuild = true;
		for (Entry<String, ABMTreeTableRow> row : Rows.entrySet()) {
			ABMTreeTableRow lrow = row.getValue();
			lrow.Status=0;
		}		
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());			
		Params.Add(false);
		Params.Add(false);	
		Params.Add(true); // IsCollapse
		
		Page.ws.RunFunction("inittable", Params);
		anywheresoftware.b4a.objects.collections.List Params2 = new anywheresoftware.b4a.objects.collections.List();
		Params2.Initialize();
		Params2.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		Params2.Add(ReverseDuration);
		Page.ws.RunFunction("reinitclickstreetable", Params2);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("TreeTable Prepare: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
		this.IsTreeInitialized=true;		
	}	
	
	@Override
	protected ABMComponent Clone() {
		ABMTreeTable c = new ABMTreeTable();
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
		c.IsResponsive = IsResponsive;	
		c.IsTreeInitialized=IsTreeInitialized;
		for (Entry<String, ABMTreeTableRow> r : Rows.entrySet()) {
			c.Rows.put(r.getKey(), r.getValue().Clone());
		}
		return c;
	}
	
}


