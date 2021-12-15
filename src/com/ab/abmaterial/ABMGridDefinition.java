package com.ab.abmaterial;

import java.util.ArrayList;
import java.util.List;

//import org.json.JSONException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;

@Author("Alain Bailleul")  
public class ABMGridDefinition implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5750945312227404058L;
	protected String GridBaseId="R";
	protected List<RowDef> Rows = new ArrayList<RowDef>();
	
	public ABMGridDefinition Clone() {
		ABMGridDefinition c= new ABMGridDefinition();
		c.GridBaseId = GridBaseId;
		for (RowDef r: Rows) {
			c.Rows.add(r.Clone());
		}
		return c;
	}
	
	protected RowDef Row(int index) {
		return Rows.get(index);
	}
	
	
	protected void AddRowLayoutFixedFluidFixed(int leftWidth, String leftVisibility, int rightWidth, String rightVisibility) {
		RowDef rowDef = new RowDef();
		rowDef.RowType="LayoutFixedFluidFixed";
		rowDef.AddFixedCell(leftWidth, leftVisibility).AddFixedCell(rightWidth, rightVisibility).AddFluidCell();
		Rows.add(rowDef);
	}
	
	protected void ZABInsertRowBefore(int before) {
		RowDef rowDef = new RowDef();
		rowDef.NumberOfrows = 1;
		rowDef.CenterInPage = true;
		rowDef.MarginTopPx=0;
		rowDef.MarginBottomPx=20;
		rowDef.ThemeName = "";
		Rows.add(before, rowDef);		
	}
	
	protected void ZABInsertRowAfter(int after) {
		RowDef rowDef = new RowDef();
		rowDef.NumberOfrows = 1;
		rowDef.CenterInPage = true;
		rowDef.MarginTopPx=0;
		rowDef.MarginBottomPx=20;
		rowDef.ThemeName = "";
		if (after==Rows.size()) {
			Rows.add(rowDef);
		} else {
			Rows.add(after+1, rowDef);
		}
	}	
	
	protected void ZABDeleteRow(int index) {
		if (index>=0 && index < Rows.size()) {
			Rows.get(index).IsForRemove=true;
		}
	}
	
	/**
	 * creates one row with the number of cells specified, all equal in size 
	 * Possible values for numberOfCells are: 1, 2, 3, 4, 6, 12  
	 */
	public RowDef AddRow(int numberOfCells, boolean centerInPage, String rowThemeName, String cellThemeName) {
		RowDef rowDef = new RowDef();
		rowDef.NumberOfrows = 1;
		rowDef.CenterInPage = centerInPage;
		rowDef.MarginTopPx=0;
		rowDef.MarginBottomPx=20;
		rowDef.ThemeName = rowThemeName;
		switch (numberOfCells) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 6:
		case 12:
			int sizeCells = 12/numberOfCells;
			rowDef.AddCellsOS(numberOfCells, 0, 0, 0, sizeCells, sizeCells, sizeCells, cellThemeName);
			break;
		default:
			BA.Log("numberOfCells can only be: 1, 2, 3, 4, 6 or 12");
		}
		Rows.add(rowDef);
		return rowDef;
	}
	
	public RowDef AddRows(int numberOfRows, boolean centerInPage, String themeName) {
		RowDef rowDef = new RowDef();
		rowDef.NumberOfrows = numberOfRows;
		rowDef.CenterInPage = centerInPage;
		rowDef.MarginTopPx=0;
		rowDef.MarginBottomPx=20;
		rowDef.ThemeName = themeName;
		Rows.add(rowDef);
		return rowDef;
	}
	
	public RowDef AddRowsM(int numberOfRows, boolean centerInPage, int marginTopPx, int marginBottomPx, String themeName) {
		RowDef rowDef = new RowDef();
		rowDef.NumberOfrows = numberOfRows;
		rowDef.CenterInPage = centerInPage;
		rowDef.MarginTopPx=marginTopPx;
		rowDef.MarginBottomPx=marginBottomPx;
		rowDef.ThemeName = themeName;
		Rows.add(rowDef);
		return rowDef;
	}
	
	public RowDef AddRowsM2(int numberOfRows, boolean centerInPage, int marginTopPx, int marginBottomPx, int marginLeftPx, int marginRightPx, String themeName) {
		RowDef rowDef = new RowDef();
		rowDef.NumberOfrows = numberOfRows;
		rowDef.CenterInPage = centerInPage;
		rowDef.MarginTopPx=marginTopPx;
		rowDef.MarginBottomPx=marginBottomPx;
		rowDef.MarginLeftPx=marginLeftPx;
		rowDef.MarginRightPx=marginRightPx;
		rowDef.ThemeName = themeName;
		Rows.add(rowDef);
		return rowDef;
	}
	
	public RowDef AddRowV(int numberOfCells, boolean centerInPage, String rowThemeName, String visibility, String cellThemeName) {
		RowDef rowDef = new RowDef();
		rowDef.NumberOfrows = 1;
		rowDef.CenterInPage = centerInPage;
		rowDef.MarginTopPx=0;
		rowDef.MarginBottomPx=20;
		rowDef.ThemeName = rowThemeName;
		rowDef.Visibility=visibility;
		switch (numberOfCells) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 6:
		case 12:
			int sizeCells = 12/numberOfCells;
			rowDef.AddCellsOS(numberOfCells, 0, 0, 0, sizeCells, sizeCells, sizeCells, cellThemeName);
			break;
		default:
			BA.Log("numberOfCells can only be: 1, 2, 3, 4, 6 or 12");
		}
		Rows.add(rowDef);
		return rowDef;
	}
	
	public RowDef AddRowsV(int numberOfRows, boolean centerInPage, String visibility, String themeName) {
		RowDef rowDef = new RowDef();
		rowDef.NumberOfrows = numberOfRows;
		rowDef.CenterInPage = centerInPage;
		rowDef.MarginTopPx=0;
		rowDef.MarginBottomPx=20;
		rowDef.ThemeName = themeName;
		rowDef.Visibility=visibility;
		Rows.add(rowDef);
		return rowDef;
	}
	
	public RowDef AddRowsMV(int numberOfRows, boolean centerInPage, int marginTopPx, int marginBottomPx, String visibility, String themeName) {
		RowDef rowDef = new RowDef();
		rowDef.NumberOfrows = numberOfRows;
		rowDef.CenterInPage = centerInPage;
		rowDef.MarginTopPx=marginTopPx;
		rowDef.MarginBottomPx=marginBottomPx;
		rowDef.ThemeName = themeName;
		rowDef.Visibility=visibility;
		Rows.add(rowDef);
		return rowDef;
	}
	
	public RowDef AddRowsMV2(int numberOfRows, boolean centerInPage, int marginTopPx, int marginBottomPx, int marginLeftPx, int marginRightPx, String visibility, String themeName) {
		RowDef rowDef = new RowDef();
		rowDef.NumberOfrows = numberOfRows;
		rowDef.CenterInPage = centerInPage;
		rowDef.MarginTopPx=marginTopPx;
		rowDef.MarginBottomPx=marginBottomPx;
		rowDef.MarginLeftPx=marginLeftPx;
		rowDef.MarginRightPx=marginRightPx;
		rowDef.ThemeName = themeName;
		rowDef.Visibility=visibility;
		Rows.add(rowDef);
		return rowDef;
	}
	
	/**
	 * Can not be created within B4J, But its properties and methods are available through another ABM class.
	 */
	public static class RowDef implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 4113158641873529849L;
		protected int NumberOfrows=0;
		protected boolean CenterInPage=false;
		protected int MarginTopPx=20;
		protected int MarginBottomPx=20;
		protected int MarginLeftPx=-9999;
		protected int MarginRightPx=-9999;
		protected String ThemeName="";
		protected CellDef cellDef=null;
		protected boolean IsBuild=false;
		protected String Visibility=ABMaterial.VISIBILITY_ALL;
		protected List<String> RowIDs = new ArrayList<String>();
		protected List<String> OldRowIDs = new ArrayList<String>();
				
		protected String RowType="";
		protected Boolean IsForRemove=false;
				
		public RowDef Clone() {
			RowDef c = new RowDef();
			c.NumberOfrows = NumberOfrows;
			c.CenterInPage = CenterInPage;
			c.MarginTopPx = MarginTopPx;
			c.MarginBottomPx = MarginBottomPx;
			c.MarginLeftPx = MarginLeftPx;
			c.MarginRightPx = MarginRightPx;
			c.ThemeName = ThemeName;
			if (cellDef!=null) {
				c.cellDef = cellDef.Clone();
			}
			for (int i=0;i<RowIDs.size();i++) {
				c.RowIDs.add(RowIDs.get(i));
			}
			for (int i=0;i<OldRowIDs.size();i++) {
				c.OldRowIDs.add(OldRowIDs.get(i));
			}
			c.IsBuild = IsBuild;
			c.Visibility=Visibility;
			c.RowType = RowType;
			return c;
		}
		
		protected CellDef AddFixedCell(int width, String visibility) {
			cellDef = new CellDef();
			cellDef.CellType="Fixed";
			cellDef.Width=width;
			cellDef.Visibility = visibility;
			return cellDef;
		}
		
		protected CellDef AddFluidCell() {
			cellDef = new CellDef();
			cellDef.CellType="Fluid";
			return cellDef;
		}
		
		public CellDef AddCells12(int numberOfCells, String themeName) {
			cellDef = new CellDef();
			cellDef.OffsetSmall = 0;
			cellDef.OffsetMedium = 0;
			cellDef.OffsetLarge = 0;
			cellDef.SizeSmall = 12;
			cellDef.SizeMedium = 12;
			cellDef.SizeLarge = 12;
			cellDef.ThemeName = themeName;
			cellDef.PaddingLeftPx = 0;
			cellDef.PaddingRightPx = 0;
			cellDef.MarginTopPx=0;
			cellDef.MarginBottomPx=0;
			cellDef.NumberOfCells = numberOfCells;
			return cellDef;
		}
		
		public CellDef AddCellsOSMP(int numberOfCells, int offsetSmall, int offsetMedium, int offsetLarge, int sizeSmall, int sizeMedium, int sizeLarge, int marginTopPx, int marginBottomPx, int paddingLeftPx, int paddingRightPx, String themeName) {
			cellDef = new CellDef();
			cellDef.OffsetSmall = offsetSmall;
			cellDef.OffsetMedium = offsetMedium;
			cellDef.OffsetLarge = offsetLarge;
			cellDef.SizeSmall = sizeSmall;
			cellDef.SizeMedium = sizeMedium;
			cellDef.SizeLarge = sizeLarge;
			cellDef.ThemeName = themeName;
			cellDef.PaddingLeftPx = paddingLeftPx;
			cellDef.PaddingRightPx = paddingRightPx;
			cellDef.MarginTopPx=marginTopPx;
			cellDef.MarginBottomPx=marginBottomPx;
			cellDef.NumberOfCells = numberOfCells;
			return cellDef;
		}
		
		public CellDef AddCellsOS(int numberOfCells, int offsetSmall, int offsetMedium, int offsetLarge, int sizeSmall, int sizeMedium, int sizeLarge, String themeName) {
			cellDef = new CellDef();
			cellDef.OffsetSmall = offsetSmall;
			cellDef.OffsetMedium = offsetMedium;
			cellDef.OffsetLarge = offsetLarge;
			cellDef.SizeSmall = sizeSmall;
			cellDef.SizeMedium = sizeMedium;
			cellDef.SizeLarge = sizeLarge;
			cellDef.ThemeName = themeName;
			cellDef.PaddingLeftPx = 0;
			cellDef.PaddingRightPx = 0;
			cellDef.MarginTopPx=0;
			cellDef.MarginBottomPx=0;
			cellDef.NumberOfCells = numberOfCells;
			return cellDef;
		}
		
		public CellDef AddCells12MP(int numberOfCells, int marginTopPx, int marginBottomPx, int paddingLeftPx, int paddingRightPx, String themeName) {
			cellDef = new CellDef();
			cellDef.OffsetSmall = 0;
			cellDef.OffsetMedium = 0;
			cellDef.OffsetLarge = 0;
			cellDef.SizeSmall = 12;
			cellDef.SizeMedium = 12;
			cellDef.SizeLarge = 12;
			cellDef.ThemeName = themeName;
			cellDef.PaddingLeftPx = paddingLeftPx;
			cellDef.PaddingRightPx = paddingRightPx;
			cellDef.MarginTopPx=marginTopPx;
			cellDef.MarginBottomPx=marginBottomPx;
			cellDef.NumberOfCells = numberOfCells;			
			return cellDef;
		}
		
		public CellDef AddCells12V(int numberOfCells, String visibility, String themeName) {
			cellDef = new CellDef();
			cellDef.OffsetSmall = 0;
			cellDef.OffsetMedium = 0;
			cellDef.OffsetLarge = 0;
			cellDef.SizeSmall = 12;
			cellDef.SizeMedium = 12;
			cellDef.SizeLarge = 12;
			cellDef.ThemeName = themeName;
			cellDef.PaddingLeftPx = 0;
			cellDef.PaddingRightPx = 0;
			cellDef.MarginTopPx=0;
			cellDef.MarginBottomPx=0;
			cellDef.NumberOfCells = numberOfCells;
			cellDef.Visibility=visibility;
			return cellDef;
		}
		
		public CellDef AddCellsOSMPV(int numberOfCells, int offsetSmall, int offsetMedium, int offsetLarge, int sizeSmall, int sizeMedium, int sizeLarge, int marginTopPx, int marginBottomPx, int paddingLeftPx, int paddingRightPx, String visibility, String themeName) {
			cellDef = new CellDef();
			cellDef.OffsetSmall = offsetSmall;
			cellDef.OffsetMedium = offsetMedium;
			cellDef.OffsetLarge = offsetLarge;
			cellDef.SizeSmall = sizeSmall;
			cellDef.SizeMedium = sizeMedium;
			cellDef.SizeLarge = sizeLarge;
			cellDef.ThemeName = themeName;
			cellDef.PaddingLeftPx = paddingLeftPx;
			cellDef.PaddingRightPx = paddingRightPx;
			cellDef.MarginTopPx=marginTopPx;
			cellDef.MarginBottomPx=marginBottomPx;
			cellDef.NumberOfCells = numberOfCells;
			cellDef.Visibility=visibility;
			return cellDef;
		}
		
		public CellDef AddCellsOSV(int numberOfCells, int offsetSmall, int offsetMedium, int offsetLarge, int sizeSmall, int sizeMedium, int sizeLarge, String visibility, String themeName) {
			cellDef = new CellDef();
			cellDef.OffsetSmall = offsetSmall;
			cellDef.OffsetMedium = offsetMedium;
			cellDef.OffsetLarge = offsetLarge;
			cellDef.SizeSmall = sizeSmall;
			cellDef.SizeMedium = sizeMedium;
			cellDef.SizeLarge = sizeLarge;
			cellDef.ThemeName = themeName;
			cellDef.PaddingLeftPx = 0;
			cellDef.PaddingRightPx = 0;
			cellDef.MarginTopPx=0;
			cellDef.MarginBottomPx=0;
			cellDef.NumberOfCells = numberOfCells;
			cellDef.Visibility=visibility;
			return cellDef;
		}
		
		public CellDef AddCells12MPV(int numberOfCells, int marginTopPx, int marginBottomPx, int paddingLeftPx, int paddingRightPx, String visibility, String themeName) {
			cellDef = new CellDef();
			cellDef.OffsetSmall = 0;
			cellDef.OffsetMedium = 0;
			cellDef.OffsetLarge = 0;
			cellDef.SizeSmall = 12;
			cellDef.SizeMedium = 12;
			cellDef.SizeLarge = 12;
			cellDef.ThemeName = themeName;
			cellDef.PaddingLeftPx = paddingLeftPx;
			cellDef.PaddingRightPx = paddingRightPx;
			cellDef.MarginTopPx=marginTopPx;
			cellDef.MarginBottomPx=marginBottomPx;
			cellDef.NumberOfCells = numberOfCells;
			cellDef.Visibility=visibility;
			return cellDef;
		}
	}
	
	/**
	 * Can not be created within B4J, But its properties and methods are available through another ABM class.
	 */
	public static class CellDef implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1994278928446229800L;
		protected int OffsetSmall=0;
		protected int OffsetMedium=0;
		protected int OffsetLarge=0;
		protected int SizeSmall=0;
		protected int SizeMedium=0;
		protected int SizeLarge=0;
		protected String ThemeName="";
		protected CellDef cellDef=null;
		protected int PaddingLeftPx=0;
		protected int PaddingRightPx=0;
		protected int MarginTopPx=0;
		protected int MarginBottomPx=0;
		protected int NumberOfCells=0;
		protected String Visibility=ABMaterial.VISIBILITY_ALL;

		protected int Width=0;
		
		protected String CellType="";
		
		public CellDef Clone() {
			CellDef c = new CellDef();
			c.OffsetSmall = OffsetSmall;
			c.OffsetMedium = OffsetMedium;
			c.OffsetLarge = OffsetLarge;
			c.SizeSmall = SizeSmall;
			c.SizeMedium = SizeMedium;
			c.SizeLarge = SizeLarge;
			c.ThemeName = ThemeName;
			if (cellDef!=null) {
				c.cellDef = cellDef.Clone();
			}
			c.PaddingLeftPx = PaddingLeftPx;
			c.PaddingRightPx = PaddingRightPx;
			c.MarginTopPx = MarginTopPx;
			c.MarginBottomPx = MarginBottomPx;
			c.NumberOfCells = NumberOfCells;
			c.Visibility=Visibility;
			c.CellType=CellType;
			c.Width = Width;
			return c;
		}
		
		protected CellDef AddFixedCell(int width, String visibility) {
			cellDef = new CellDef();
			cellDef.CellType="Fixed";
			cellDef.Width=width;
			cellDef.Visibility = visibility;
			return cellDef;
		}
		
		protected CellDef AddFluidCell() {
			cellDef = new CellDef();
			cellDef.CellType="Fluid";
			return cellDef;
		}
		
		public CellDef AddCells12(int numberOfCells, String themeName) {
			cellDef = new CellDef();
			cellDef.OffsetSmall = 0;
			cellDef.OffsetMedium = 0;
			cellDef.OffsetLarge = 0;
			cellDef.SizeSmall = 12;
			cellDef.SizeMedium = 12;
			cellDef.SizeLarge = 12;
			cellDef.ThemeName = themeName;
			cellDef.PaddingLeftPx = 0;
			cellDef.PaddingRightPx = 0;
			cellDef.MarginTopPx=0;
			cellDef.MarginBottomPx=0;
			cellDef.NumberOfCells = numberOfCells;
			return cellDef;
		}
		
		public CellDef AddCellsOSMP(int numberOfCells, int offsetSmall, int offsetMedium, int offsetLarge, int sizeSmall, int sizeMedium, int sizeLarge, int marginTopPx, int marginBottomPx, int paddingLeftPx, int paddingRightPx, String themeName) {
			cellDef = new CellDef();
			cellDef.OffsetSmall = offsetSmall;
			cellDef.OffsetMedium = offsetMedium;
			cellDef.OffsetLarge = offsetLarge;
			cellDef.SizeSmall = sizeSmall;
			cellDef.SizeMedium = sizeMedium;
			cellDef.SizeLarge = sizeLarge;
			cellDef.ThemeName = themeName;
			cellDef.PaddingLeftPx = paddingLeftPx;
			cellDef.PaddingRightPx = paddingRightPx;
			cellDef.MarginTopPx=marginTopPx;
			cellDef.MarginBottomPx=marginBottomPx;
			cellDef.NumberOfCells = numberOfCells;
			return cellDef;
		}
		
		public CellDef AddCellsOS(int numberOfCells, int offsetSmall, int offsetMedium, int offsetLarge, int sizeSmall, int sizeMedium, int sizeLarge, String themeName) {
			cellDef = new CellDef();
			cellDef.OffsetSmall = offsetSmall;
			cellDef.OffsetMedium = offsetMedium;
			cellDef.OffsetLarge = offsetLarge;
			cellDef.SizeSmall = sizeSmall;
			cellDef.SizeMedium = sizeMedium;
			cellDef.SizeLarge = sizeLarge;
			cellDef.ThemeName = themeName;
			cellDef.PaddingLeftPx = 0;
			cellDef.PaddingRightPx = 0;
			cellDef.MarginTopPx=0;
			cellDef.MarginBottomPx=0;
			cellDef.NumberOfCells = numberOfCells;
			return cellDef;
		}
		
		public CellDef AddCells12MP(int numberOfCells, int marginTopPx, int marginBottomPx, int paddingLeftPx, int paddingRightPx, String themeName) {
			cellDef = new CellDef();
			cellDef.OffsetSmall = 0;
			cellDef.OffsetMedium = 0;
			cellDef.OffsetLarge = 0;
			cellDef.SizeSmall = 12;
			cellDef.SizeMedium = 12;
			cellDef.SizeLarge = 12;
			cellDef.ThemeName = themeName;
			cellDef.PaddingLeftPx = paddingLeftPx;
			cellDef.PaddingRightPx = paddingRightPx;
			cellDef.MarginTopPx=marginTopPx;
			cellDef.MarginBottomPx=marginBottomPx;
			cellDef.NumberOfCells = numberOfCells;
			return cellDef;
		}
		
		public CellDef AddCells12V(int numberOfCells, String visibility, String themeName) {
			cellDef = new CellDef();
			cellDef.OffsetSmall = 0;
			cellDef.OffsetMedium = 0;
			cellDef.OffsetLarge = 0;
			cellDef.SizeSmall = 12;
			cellDef.SizeMedium = 12;
			cellDef.SizeLarge = 12;
			cellDef.ThemeName = themeName;
			cellDef.PaddingLeftPx = 0;
			cellDef.PaddingRightPx = 0;
			cellDef.MarginTopPx=0;
			cellDef.MarginBottomPx=0;
			cellDef.NumberOfCells = numberOfCells;
			cellDef.Visibility=visibility;
			return cellDef;
		}
		
		public CellDef AddCellsOSMPV(int numberOfCells, int offsetSmall, int offsetMedium, int offsetLarge, int sizeSmall, int sizeMedium, int sizeLarge, int marginTopPx, int marginBottomPx, int paddingLeftPx, int paddingRightPx, String visibility, String themeName) {
			cellDef = new CellDef();
			cellDef.OffsetSmall = offsetSmall;
			cellDef.OffsetMedium = offsetMedium;
			cellDef.OffsetLarge = offsetLarge;
			cellDef.SizeSmall = sizeSmall;
			cellDef.SizeMedium = sizeMedium;
			cellDef.SizeLarge = sizeLarge;
			cellDef.ThemeName = themeName;
			cellDef.PaddingLeftPx = paddingLeftPx;
			cellDef.PaddingRightPx = paddingRightPx;
			cellDef.MarginTopPx=marginTopPx;
			cellDef.MarginBottomPx=marginBottomPx;
			cellDef.NumberOfCells = numberOfCells;
			cellDef.Visibility=visibility;
			return cellDef;
		}
		
		public CellDef AddCellsOSV(int numberOfCells, int offsetSmall, int offsetMedium, int offsetLarge, int sizeSmall, int sizeMedium, int sizeLarge, String visibility, String themeName) {
			cellDef = new CellDef();
			cellDef.OffsetSmall = offsetSmall;
			cellDef.OffsetMedium = offsetMedium;
			cellDef.OffsetLarge = offsetLarge;
			cellDef.SizeSmall = sizeSmall;
			cellDef.SizeMedium = sizeMedium;
			cellDef.SizeLarge = sizeLarge;
			cellDef.ThemeName = themeName;
			cellDef.PaddingLeftPx = 0;
			cellDef.PaddingRightPx = 0;
			cellDef.MarginTopPx=0;
			cellDef.MarginBottomPx=0;
			cellDef.NumberOfCells = numberOfCells;
			cellDef.Visibility=visibility;
			return cellDef;
		}
		
		public CellDef AddCells12MPV(int numberOfCells, int marginTopPx, int marginBottomPx, int paddingLeftPx, int paddingRightPx, String visibility, String themeName) {
			cellDef = new CellDef();
			cellDef.OffsetSmall = 0;
			cellDef.OffsetMedium = 0;
			cellDef.OffsetLarge = 0;
			cellDef.SizeSmall = 12;
			cellDef.SizeMedium = 12;
			cellDef.SizeLarge = 12;
			cellDef.ThemeName = themeName;
			cellDef.PaddingLeftPx = paddingLeftPx;
			cellDef.PaddingRightPx = paddingRightPx;
			cellDef.MarginTopPx=marginTopPx;
			cellDef.MarginBottomPx=marginBottomPx;
			cellDef.NumberOfCells = numberOfCells;
			cellDef.Visibility=visibility;
			return cellDef;
		}
	}
	
	public String DebugPrintGrid(String ID) {
		StringBuilder s = new StringBuilder();
		int rCounter=1;
		for (RowDef row: Rows) {
			for (int i=0;i<row.NumberOfrows;i++) {
				if (ID.equals("")) {
					s.append("ROW:  " + GridBaseId + rCounter + "\n");
				} else {
					s.append("ROW:  " + GridBaseId + rCounter + " (" + ID + ")\n");
				}
				CellDef cell = row.cellDef;
				int cCounter=1;
				while (cell!=null) {
					for (int j=0;j<cell.NumberOfCells;j++) {
						if (ID.equals("")) {
							s.append("     CELL: " + GridBaseId + rCounter + "C" + cCounter + "\n");
						} else {
							s.append("     CELL: " + GridBaseId + rCounter + "C" + cCounter + " (" + ID + ")\n");
						}
					
						cCounter++;
					}
					cell = cell.cellDef;
				}
				rCounter++;
			}
		}
		return s.toString();
	}
	
}
