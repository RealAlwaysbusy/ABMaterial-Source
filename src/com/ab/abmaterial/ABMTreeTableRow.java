package com.ab.abmaterial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")  
@ShortName("ABMTreeTableRow")
public class ABMTreeTableRow implements java.io.Serializable {
	
	private static final long serialVersionUID = -5894087192321401511L;
	
	protected List<ABMTreeTableCell> cells = new ArrayList<ABMTreeTableCell>();
	
	protected int NextCell=0;
	protected int CurrOffset=0;
	public int Depth=0;
	public String IsFooterForRowId="";
	protected String HasFooterRowId="";
	public String TreeRowId="";
	public String TreeRowParentId="";
	
	protected List<String> TreeRowChildIds = new ArrayList<String>();
	public String IconColorThemeName="default";
	public String OverruleDefaultDepthThemeName="default";
	protected int Status=0;
	protected Map<String,Boolean> DragDropTreeRowNames = new HashMap<String,Boolean>();
	protected String mDragDropTreeRowName=""; 
	protected int SortIndex=-1;
	public boolean IsInitialized=false;
	
	public void Initialize(int depth, String treeRowId, String treeRowParentId, String isFooterForRowId, String dragDropTreeRowName, String iconColorThemeName, String overruleDefaultDepthThemeName) {
		this.IsFooterForRowId = isFooterForRowId;
		this.Depth = depth;
		this.TreeRowId=treeRowId;
		this.TreeRowParentId=treeRowParentId;
		this.mDragDropTreeRowName=dragDropTreeRowName;
		
		if (!overruleDefaultDepthThemeName.equals("")) {
			this.OverruleDefaultDepthThemeName = overruleDefaultDepthThemeName;
		} else {
			this.OverruleDefaultDepthThemeName = "default";
		}
		if (!iconColorThemeName.equals("")) {
			this.IconColorThemeName = iconColorThemeName;
		} else {
			this.IconColorThemeName = "default";
		}
		IsInitialized=true;
	}
	
	public void AddCell(ABMTreeTableCell cell) {
		cells.add(cell);
	}
	
	public void ClearCells() {
		cells.clear();
	}
	
	public int GetCellsSize() {
		return cells.size();
	}
	
	public ABMTreeTableCell GetCell(int index) {
		return cells.get(index);
	}
	
	public void SortChildrenOn(int cellIndex) {
		SortIndex = cellIndex;
	}
	
	public String getDragDropTreeRowName() {
		return mDragDropTreeRowName;
	}
	
	public void SetCanReceiveDropsFrom(anywheresoftware.b4a.objects.collections.List dragDropTreeRowNames) {
		DragDropTreeRowNames = new HashMap<String,Boolean>();
		for (int i=0;i<dragDropTreeRowNames.getSize();i++) {
			String s = (String) dragDropTreeRowNames.Get(i);
			DragDropTreeRowNames.put(s.toLowerCase(), true);
		}
	}
	
	@Hide
	protected void Reset(int Start) {
		NextCell=0;
		CurrOffset = Start;
	}
	
	public ABMTreeTableCell GetNextCell(String theme) {
		if (NextCell>=cells.size()) {
			return null;
		}
		ABMTreeTableCell cell = (ABMTreeTableCell) cells.get(NextCell);
		if(cell.ColOffset==CurrOffset) {
			CurrOffset += cell.ColSpan-1; 
			NextCell++;
			return cell;
		} else {
			ABMTreeTableCell newCell = new ABMTreeTableCell();
			newCell.Initialize("emptyinnerlcars", "", CurrOffset, cell.ColOffset-CurrOffset, false, theme);
			CurrOffset = cell.ColOffset;
			return newCell;
		}
		
	}
	
	protected ABMComponent GetCellComponent(ABMPage page, int cellIndex) {
		if (cellIndex<1 || cellIndex>cells.size()) {
			BA.Log("No cell found with index " + cellIndex);
			return null;
		}		
		ABMTreeTableCell cell = (ABMTreeTableCell) cells.get(cellIndex);
		return ABMaterial.GetComponent(page, cell.value, cell.componentId, cell.value.ParentString);		
	}	
	
	protected ABMComponent GetCellComponentWriteOnly(ABMPage page, int cellIndex) {
		if (cellIndex<1 || cellIndex>cells.size()) {
			BA.Log("No cell found with index " + cellIndex);
			return null;
		}		
		ABMTreeTableCell cell = (ABMTreeTableCell) cells.get(cellIndex);
		return ABMaterial.CastABMComponent(cell.value);		
	}	
	
	protected ABMTreeTableRow Clone() {
		ABMTreeTableRow r = new ABMTreeTableRow();
		for (int i=0;i<cells.size();i++) {
			r.cells.add(((ABMTreeTableCell) cells.get(i)).Clone());				
		}	
		r.Depth = Depth;
		r.TreeRowId=TreeRowId;
		r.TreeRowParentId=TreeRowParentId;
		r.IsFooterForRowId=IsFooterForRowId;
		r.Status=Status;
		r.OverruleDefaultDepthThemeName=OverruleDefaultDepthThemeName;
		r.IconColorThemeName=IconColorThemeName;
		return r;
	}
	
	protected ABMTreeTableRow CloneNoCells() {
		ABMTreeTableRow r = new ABMTreeTableRow();
		r.Depth = Depth;
		r.TreeRowId=TreeRowId;
		r.TreeRowParentId=TreeRowParentId;
		r.IsFooterForRowId=IsFooterForRowId;
		r.Status=Status;
		r.OverruleDefaultDepthThemeName=OverruleDefaultDepthThemeName;
		r.IconColorThemeName=IconColorThemeName;
		return r;
	}
	
	public void CleanUp() {
		for (int i=0;i<cells.size();i++) {
			ABMTreeTableCell cell = (ABMTreeTableCell) cells.get(i);
			cell.CleanUp();
		}
	}
}	

