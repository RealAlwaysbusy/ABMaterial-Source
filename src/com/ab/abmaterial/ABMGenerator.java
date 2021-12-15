package com.ab.abmaterial;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")  
@ShortName("ABMGenerator")
public class ABMGenerator {

	
	/*
	 * Will generate two files with B4J source code you can cut and paste.
	 * One with the code for the CRUD modal sheet, used in your page class
	 * One with shared database methods you have to put in a module, called DBM.
	 * 
	 * Generating multiple CRUD sheets will create the same database methods, so you only
	 * have to add them once in your code
	 */
	public void GenerateCRUDSheet(String dir, String uniqueName, ABMGeneratorCRUDDefinition definition, int modalSheetSize) {
		String Mark="";
		if (definition.MarkToDocument) {
			Mark = " 'GENTODOC";
		}
		
		boolean DoesHaveOverview = true;
		definition.uniqueName = uniqueName;
		StringBuilder s = new StringBuilder();
		
		if (definition.Rows.isEmpty()) {
			anywheresoftware.b4a.objects.collections.List mList = new anywheresoftware.b4a.objects.collections.List();
			mList.Initialize();
			for (int i=0;i<definition.FieldNames.size();i++) {
				if (definition.FieldNames.get(i).toLowerCase().equals(definition.IDFieldName.toLowerCase())) {
					mList.Add("");
				} else {
					mList.Add((i+1) + ":0:12");
				}
			}
			definition.Set02RowOffsetSizes(mList);
		}
		if (definition.Rows.size()!=definition.FieldNames.size()) {
			BA.Log("Size of Set02RowOffsetSizes does not match Set01FieldNames. Aborted.");
			return;
		}
		if (definition.Labels.isEmpty()) {
			anywheresoftware.b4a.objects.collections.List mList = new anywheresoftware.b4a.objects.collections.List();
			mList.Initialize();
			for (int i=0;i<definition.FieldNames.size();i++) {
				if (definition.FieldNames.get(i).toLowerCase().equals(definition.IDFieldName.toLowerCase())) {
					mList.Add("");
				} else {
					mList.Add(definition.FieldNames.get(i));
				}
			}
			definition.Set03LabelTexts(mList);
		}
		if (definition.Labels.size()!=definition.FieldNames.size()) {
			BA.Log("Size of Set03LabelTexts does not match Set01FieldNames. Aborted.");
			return;
		}
		if (definition.Types.isEmpty()) {
			anywheresoftware.b4a.objects.collections.List mList = new anywheresoftware.b4a.objects.collections.List();
			mList.Initialize();
			for (int i=0;i<definition.FieldNames.size();i++) {
				if (definition.FieldNames.get(i).toLowerCase().equals(definition.IDFieldName.toLowerCase())) {
					mList.Add(ABMaterial.GEN_NONE);
				} else {
					mList.Add(ABMaterial.GEN_TEXT);
				}
			}
			definition.Set04ComponentTypes(mList);
		}
		if (definition.Types.size()!=definition.FieldNames.size()) {
			BA.Log("Size of Set04ComponentTypes does not match Set01FieldNames. Aborted.");
			return;
		}		
		if (definition.OverviewHeaderThemes.isEmpty()) {
			anywheresoftware.b4a.objects.collections.List mList = new anywheresoftware.b4a.objects.collections.List();
			mList.Initialize();
			for (int i=0;i<definition.FieldNames.size();i++) {
				mList.Add("");
			}
			definition.Set12OverviewHeaderThemes(mList);
		}
		if (definition.OverviewHeaderThemes.size()!=definition.FieldNames.size()) {
			BA.Log("Size of Set12OverviewHeaderThemes does not match Set01FieldNames. Aborted.");
			return;
		}
		if (definition.OverviewThemes.isEmpty()) {
			anywheresoftware.b4a.objects.collections.List mList = new anywheresoftware.b4a.objects.collections.List();
			mList.Initialize();
			for (int i=0;i<definition.FieldNames.size();i++) {
				mList.Add("");
			}
			definition.Set13OverviewCellThemes(mList);
		}
		if (definition.OverviewThemes.size()!=definition.FieldNames.size()) {
			BA.Log("Size of Set13OverviewCellThemes does not match Set01FieldNames. Aborted.");
			return;
		}
		if (definition.OverviewSorts.isEmpty()) {
			anywheresoftware.b4a.objects.collections.List mList = new anywheresoftware.b4a.objects.collections.List();
			mList.Initialize();
			for (int i=0;i<definition.FieldNames.size();i++) {
				mList.Add(false);				
			}
			definition.Set15OverviewSortable(mList);
		}
		if (definition.OverviewSorts.size()!=definition.FieldNames.size()) {
			BA.Log("Size of Set15OverviewSortable does not match Set01FieldNames. Aborted.");
			return;
		}
		if (definition.OverviewVisibles.isEmpty()) {
			DoesHaveOverview = false;
			anywheresoftware.b4a.objects.collections.List mList = new anywheresoftware.b4a.objects.collections.List();
			mList.Initialize();
			for (int i=0;i<definition.FieldNames.size();i++) {
				mList.Add(false);				
			}
			definition.Set11OverviewVisible(mList);
		}
		if (definition.OverviewVisibles.size()!=definition.FieldNames.size()) {
			BA.Log("Size of Set11OverviewVisibles does not match Set01FieldNames. Aborted.");
			return;
		}
		if (definition.OverviewSearches.isEmpty()) {
			anywheresoftware.b4a.objects.collections.List mList = new anywheresoftware.b4a.objects.collections.List();
			mList.Initialize();
			for (int i=0;i<definition.FieldNames.size();i++) {
				mList.Add(false);				
			}
			definition.Set14OverviewIncludeInSearch(mList);
		}
		if (definition.OverviewSearches.size()!=definition.FieldNames.size()) {
			BA.Log("Size of Set14OverviewIncludeInSearch does not match Set01FieldNames. Aborted.");
			return;
		}
		if (definition.OverviewHeights.isEmpty()) {
			anywheresoftware.b4a.objects.collections.List mList = new anywheresoftware.b4a.objects.collections.List();
			mList.Initialize();
			for (int i=0;i<definition.FieldNames.size();i++) {
				mList.Add(0);				
			}
			definition.Set16OverviewHeaderHeights(mList);
		}
		if (!definition.OverviewHeights.isEmpty()) {
			if (definition.OverviewHeights.size()!=definition.FieldNames.size()) {
				BA.Log("Size of Set16OverviewHeaderHeights does not match Set01FieldNames. Aborted.");
				return;
			}
		}
		if (definition.OverviewWidths.isEmpty()) {
			anywheresoftware.b4a.objects.collections.List mList = new anywheresoftware.b4a.objects.collections.List();
			mList.Initialize();
			for (int i=0;i<definition.FieldNames.size();i++) {
				mList.Add(0);				
			}
			definition.Set17OverviewColumnWidths(mList);
		}
		if (!definition.OverviewWidths.isEmpty()) {
			if (definition.OverviewWidths.size()!=definition.FieldNames.size()) {
				BA.Log("Size of Set17OverviewColumnWidths does not match Set01FieldNames. Aborted.");
				return;
			}
		}
		if (definition.Defaults.isEmpty()) {
			anywheresoftware.b4a.objects.collections.List mList = new anywheresoftware.b4a.objects.collections.List();
			mList.Initialize();
			for (int i=0;i<definition.FieldNames.size();i++) {
				//mList.Add("");
				switch (definition.Types.get(i)) {
				case ABMaterial.GEN_NONE:
					mList.Add("");
					break;
				case ABMaterial.GEN_TEXT:
					mList.Add("");
					break;
				case ABMaterial.GEN_DOUBLE:
					mList.Add("0");
					break;
				case ABMaterial.GEN_INTEGER:
					mList.Add("0");
					break;
				case ABMaterial.GEN_TEXTAREA:
					mList.Add("");
					break;
				case ABMaterial.GEN_CHECKBOX:
					mList.Add("false");
					break;
				case ABMaterial.GEN_COMBOLIST:
					mList.Add("");
					break;
				case ABMaterial.GEN_COMBOSQL:
					mList.Add("");
					break;
				case ABMaterial.GEN_SWITCH:
					mList.Add("false");
					break;	
				case ABMaterial.GEN_DATE_SCROLL:
					mList.Add("now");
					break;	
				case ABMaterial.GEN_TIME_SCROLL:
					mList.Add("now");
					break;
				case ABMaterial.GEN_DATETIME_SCROLL:
					mList.Add("now");
					break;
				case ABMaterial.GEN_DATE_PICK:
					mList.Add("now");	
					break;
				case ABMaterial.GEN_TIME_PICK:
					mList.Add("now");
					break;
				case ABMaterial.GEN_DATETIME_PICK:
					mList.Add("now");
					break;
				}			
			}
			definition.Set05DefaultValues(mList);
		}
		if (definition.Defaults.size()!=definition.FieldNames.size()) {
			BA.Log("Size of Set05DefaultValues does not match Set01FieldNames. Aborted.");
			return;
		}
		
		if (definition.Queries.isEmpty()) {
			anywheresoftware.b4a.objects.collections.List mList = new anywheresoftware.b4a.objects.collections.List();
			mList.Initialize();
			for (int i=0;i<definition.FieldNames.size();i++) {
				mList.Add("");				
			}
			definition.Set06ComboQueries(mList);
		}
		if (definition.Queries.size()!=definition.FieldNames.size()) {
			BA.Log("Size of Set06ComboQueries does not match Set01FieldNames. Aborted.");
			return;
		}
		
		if (definition.Items.isEmpty()) {
			anywheresoftware.b4a.objects.collections.List mList = new anywheresoftware.b4a.objects.collections.List();
			mList.Initialize();
			for (int i=0;i<definition.FieldNames.size();i++) {
				mList.Add(null);				
			}
			definition.Set07ComboLists(mList);
		}
		if (definition.Items.size()!=definition.FieldNames.size()) {
			BA.Log("Size of Set07ComboLists does not match Set01FieldNames. Aborted.");
			return;
		}
		if (definition.Validations.isEmpty()) {
			anywheresoftware.b4a.objects.collections.List mList = new anywheresoftware.b4a.objects.collections.List();
			mList.Initialize();
			for (int i=0;i<definition.FieldNames.size();i++) {
				mList.Add("");				
			}
			definition.Set08ValidationMethods(mList);
		}
		if (definition.Validations.size()!=definition.FieldNames.size()) {
			BA.Log("Size of Set08ValidationMethods does not match Set01FieldNames. Aborted.");
			return;
		}
		if (definition.Enableds.isEmpty()) {
			anywheresoftware.b4a.objects.collections.List mList = new anywheresoftware.b4a.objects.collections.List();
			mList.Initialize();
			for (int i=0;i<definition.FieldNames.size();i++) {
				mList.Add(true);				
			}
			definition.Set09Enableds(mList);
		}
		if (definition.Validations.size()!=definition.FieldNames.size()) {
			BA.Log("Size of Set09Enableds does not match Set01FieldNames. Aborted.");
			return;
		}
		if (definition.UseInUpdates.isEmpty()) {
			anywheresoftware.b4a.objects.collections.List mList = new anywheresoftware.b4a.objects.collections.List();
			mList.Initialize();
			for (int i=0;i<definition.FieldNames.size();i++) {
				mList.Add(true);				
			}
			definition.Set10UseInUpdates(mList);
		}
		if (definition.UseInUpdates.size()!=definition.FieldNames.size()) {
			BA.Log("Size of Set10UseInUpdates does not match Set01FieldNames. Aborted.");
			return;
		}
		
		s.append("--------------------------------------------------------------\n");
		s.append("' Add in Sub Class_Globals()\n");
		s.append("--------------------------------------------------------------\n");
		s.append("	Dim Active" + uniqueName + definition.IDFieldName + " As Int\n");
		s.append("	Dim IsNew" + uniqueName + " As Boolean\n");
		if ( DoesHaveOverview) {
			s.append("	Dim Filter" + uniqueName + " As String\n");
			s.append("	Dim LastSort" + uniqueName + " As String\n");
		}
		s.append("\n");
		
		if ( DoesHaveOverview) {
			s.append("--------------------------------------------------------------\n");
			s.append("' Add in BuildPage()\n");
			s.append("--------------------------------------------------------------\n");
			s.append("	' minimum page grid needed\n");
			s.append("	page.AddRowsM(1,True,0,0, \"\").AddCells12(1,\"\")\n");
			if (definition.oHasSearch) {
				s.append("	page.AddRows(1,True, \"\").AddCellsOS(1,0,0,0,10,10,11,\"\").AddCellsOSMP(1,0,0,0,2,2,1,14,0,0,0,\"\")\n");
			}
			s.append("	page.AddRows(2,True, \"\").AddCells12(1,\"\")\n");	
			s.append("	page.BuildGrid 'IMPORTANT once you loaded the complete grid AND before you start adding components\n");
			s.append("	\n");
		}
		
		
		s.append("--------------------------------------------------------------\n");
		s.append("' Add in BuildPage() or ConnectPage()\n");
		s.append("--------------------------------------------------------------\n");
		s.append("	page.AddModalSheetTemplate(ABMGenBuild" + uniqueName + ")\n");
		s.append("	page.AddModalSheetTemplate(ABMGenBuild" + uniqueName + "Delete)\n");
		s.append("	page.AddModalSheetTemplate(ABMGenBuild" + uniqueName + "BadInput)\n");
		s.append("\n");
			
		if (definition.oHasSearch && DoesHaveOverview) {
			s.append("	Dim ABMGenSearch" + uniqueName + " As ABMInput\n");
			s.append("	ABMGenSearch" + uniqueName + ".Initialize(page, \"ABMGenSearch" + uniqueName + "\", ABM.INPUT_TEXT, \"" + definition.SearchInputCaption + "\", False, \"\")\n");
			s.append("	page.CellR(1,1).AddComponent(ABMGenSearch" + uniqueName + ")\n");
			s.append("	\n");	
			
			s.append("	Dim ABMGenSearchBtn" + uniqueName + " As ABMButton\n");
			s.append("	ABMGenSearchBtn" + uniqueName + ".InitializeFloating(page, \"ABMGenSearchBtn" + uniqueName + "\", \"mdi-action-search\", \"\")\n");
			s.append("	page.CellR(0,2).AddComponent(ABMGenSearchBtn" + uniqueName + ")\n");
			s.append("	\n");
		}	
		if (definition.oHasPagination && DoesHaveOverview) {
			s.append("	Dim ABMGenPagination" + uniqueName + " As ABMPagination\n");
			s.append("	ABMGenPagination" + uniqueName + ".Initialize(page, \"ABMGenPagination" + uniqueName + "\", " + definition.oRowsPerPage + ", True, True, \"\")\n");
			s.append("	ABMGenPagination" + uniqueName + ".SetTotalNumberOfPages(0)\n");
			s.append("	page.CellR(1,1).AddComponent(ABMGenPagination" + uniqueName + ")\n");
			s.append("	\n");
		}
		if (DoesHaveOverview) {
			s.append("	' create the ABMGenTable" + uniqueName + " table\n");
			s.append("	Dim ABMGenTable" + uniqueName + " As ABMTable\n");
			s.append("	' IMPORTANT: we set usingQueriesToSort = true because we are going to handle the sorting, not the javascript sorting library\n");
			s.append("	' When using Pagination, the sorting library does not know all the data\n");
			s.append("	' SetColumnDataFields() is used when the user clicks on a column head to sort to return in the SortChanged() event and the GetSortColumn() And SetSortColumn() methods.\n");
			
			s.append("	ABMGenTable" + uniqueName + ".Initialize(page, \"ABMGenTable" + uniqueName + "\", True, True, True, \"" + definition.TableTheme + "\")" + Mark  + "\n");
			
			if (definition.OverviewIsScrollable) {	
				s.append("	ABMGenTable" + uniqueName + ".IsScrollable=true)\n");
				s.append("	'ERROR: You must use SetVolumnWidths() if the table is scrollable!!!");
				if (definition.OverviewWidths.isEmpty()) {
					BA.Log("ERROR: You must use SetVolumnWidths() if the table is scrollable!!!"); 
				}
			}
			if (definition.oIsResponsive) {
				s.append("	ABMGenTable" + uniqueName + ".IsResponsive = True\n");
			}
			List<Integer> columnWidths = CalcWidths(definition); 
			s.append("	ABMGenTable" + uniqueName + ".SetHeaders(         Array As String (" + BuildOverviewHeaders(definition, columnWidths) + "))" + Mark  + "\n");
			s.append("	ABMGenTable" + uniqueName + ".SetHeaderThemes(    Array As String (" + BuildOverviewHeaderThemes(definition, columnWidths, uniqueName) + "))\n");
			if (!definition.OverviewHeights.isEmpty()) {
				s.append("	ABMGenTable" + uniqueName + ".SetHeaderHeights(   Array As Int    (" + BuildOverviewHeaderHeights(definition, columnWidths) + "))\n");
			}
			if (!definition.OverviewWidths.isEmpty()) {
				s.append("	ABMGenTable" + uniqueName + ".SetColumnWidths(    Array As Int    (" + BuildOverviewColumnWidths(definition, columnWidths) + "))\n");
			}
			s.append("	ABMGenTable" + uniqueName + ".SetColumnVisible(   Array As Boolean(" + BuildOverviewVisibles(definition, columnWidths) + "))" + Mark  + "\n");
			s.append("	ABMGenTable" + uniqueName + ".SetColumnSortable(  Array As Boolean(" + BuildOverviewSortables(definition, columnWidths) + "))" + Mark  + "\n");
			s.append("	ABMGenTable" + uniqueName + ".SetColumnDataFields(Array As String (" + BuildOverviewDataFields(definition, columnWidths) + "))" + Mark  + "\n");
			s.append("	ABMGenTable" + uniqueName + ".SetFooter(\"" + definition.FooterString + "\", 12,\"\")\n");
			
			s.append("	page.CellR(1,1).AddComponent(ABMGenTable" + uniqueName + ")\n");	
			s.append("\n");
		}
		if (definition.oHasAddButton && DoesHaveOverview) {
			s.append("	' create the add " + uniqueName + " action button\n");
			s.append("	Dim ABMGenTableAdd" + uniqueName + " As ABMActionButton\n");
			s.append("	ABMGenTableAdd" + uniqueName + ".Initialize(page, \"ABMGenTableAdd" + uniqueName + "\", \"mdi-content-add\", \"\", \"\")\n");
			s.append("	ABMGenTableAdd" + uniqueName + ".MainButton.Size = ABM.BUTTONSIZE_LARGE\n");
			s.append("	\n");
			s.append("	' add to page\n");
			s.append("	page.AddActionButton(ABMGenTableAdd" + uniqueName + ")\n");
			s.append("\n");
		}
		s.append("	' IMPORTANT: If you added the above code to BuildPage you have to put this line as the last in Websocket_Connected()\n");
		s.append("	LoadABMGenTable" + uniqueName + "(1)\n");
		s.append("	' 			 If you added the above code to ConnectPage you have to put these lines as the last in ConnectPage()\n");
		s.append("	page.Refresh ' IMPORTANT\n");
		s.append("	\n");
		s.append("	' NEW, because we use ShowLoaderType=ABM.LOADER_TYPE_MANUAL\n");
		s.append("	page.FinishedLoading 'IMPORTANT\n");
		s.append("	\n");
		s.append("	DoSearch" + uniqueName + "\n");
		s.append("	\n");
		
				
		//
		
		s.append("--------------------------------------------------------------\n");
		s.append("' Add in the class\n");
		s.append("--------------------------------------------------------------\n");
		
		if (DoesHaveOverview) {
			s.append("#Region ABMGenTable" + uniqueName + " table\n");
			s.append("Private Sub LoadABMGenTable" + uniqueName + "(fromPage As Int)" + Mark  + "\n");
			s.append("	Dim ABMGenTable" + uniqueName + " As ABMTable = page.Component(\"ABMGenTable" + uniqueName + "\")\n");	
			s.append("\n");		
			s.append("	Dim SQL As SQL = DBM.GetSQL\n");
			s.append("\n");		
			s.append("	Dim numFound As Int = DBM.ABMGenSQLSelectSingleResult(SQL, \"SELECT Count(" + definition.IDFieldName + ") as IDS FROM " + definition.TableName + " \" & Filter" + uniqueName + ", null)" + Mark  + "\n");
			if (definition.oHasPagination) {
				s.append("	Dim results As List = DBM.ABMGenSQLSelect(SQL, " + BuildSELECTOverview(definition) + " FROM " + definition.TableName + " \" & Filter" + uniqueName + " & \" \" & LastSort" + uniqueName + " & \" LIMIT \" & ((fromPage - 1) * " + definition.oRowsPerPage + ") & \", " + definition.oRowsPerPage + "\", null)" + Mark  + "\n");
			} else {
				s.append("	Dim results As List = DBM.ABMGenSQLSelect(SQL, " + BuildSELECTOverview(definition) + " FROM " + definition.TableName + " \" & Filter" + uniqueName + " & \" \" & LastSort" + uniqueName + "\", null)" + Mark  + "\n");
			}
			s.append("	If results.Size = 0 And fromPage > 1 Then\n");
			s.append("		' we are on a page without any lines (maybe removed by other user?)\n"); 
			s.append("		DBM.CloseSQL(SQL)\n");
			s.append("		fromPage = fromPage - 1\n");
			s.append("		LoadABMGenTable" + uniqueName + "(fromPage)\n");		
			s.append("		Return\n");
			s.append("	End If\n");
			s.append("	' You could for example set here the number of rows found\n");
			s.append("	' ABMGenTable" + uniqueName + ".SetFooter(\"Total number of rows: \" & numFound, 12,\"\")\n");
			s.append("	ABMGenTable" + uniqueName + ".Clear\n");
			s.append("\n");
			
			s.append("	For i = 0 To results.Size - 1\n");
			s.append("		Dim tblFields As Map = results.Get(i)\n");
			s.append("		Dim rCellValues As List\n");
			s.append("		Dim rCellThemes As List\n");
			s.append("		rCellValues.Initialize\n");
			s.append("		rCellThemes.Initialize\n");
			s.append("\n");
			
			for (int j=0;j<definition.FieldNames.size();j++) {
				s.append("		rCellValues.Add(tblFields.Get(\"" + definition.FieldNames.get(j).toLowerCase() + "\"))\n"); 
				s.append("		rCellThemes.Add(\"" + definition.OverviewThemes.get(j) + "\")\n");
				s.append("\n");
			}
			if (definition.oHasEdit) {
				s.append("		Dim ABMGenTableEdit" + uniqueName + " As ABMButton\n");
				s.append("		ABMGenTableEdit" + uniqueName + ".InitializeFloating(page, \"ABMGenTableEdit" + uniqueName + "\", \"mdi-action-visibility\", \"\")\n");
				s.append("		rCellValues.Add(ABMGenTableEdit" + uniqueName + ")\n");
				s.append("		rCellThemes.Add(\"\")\n");
				s.append("\n");
			}
			if (definition.oHasDelete) {
				s.append("		Dim ABMGenTableDelete" + uniqueName + " As ABMButton\n");
				s.append("		ABMGenTableDelete" + uniqueName + ".InitializeFloating(page, \"ABMGenTableDelete" + uniqueName + "\", \"mdi-action-delete\", \"\")\n");
				s.append("		rCellValues.Add(ABMGenTableDelete" + uniqueName + ")\n");
				s.append("		rCellThemes.Add(\"\")\n");
				s.append("\n");
			}
			s.append("		ABMGenTable" + uniqueName + ".AddRow(\"uid" + uniqueName + "\" & i, rCellValues)\n");
			s.append("		ABMGenTable" + uniqueName + ".SetRowThemes(rCellThemes) ' make sure you have as many items in rCellThemes as in rCellValues!  Must follow IMMEDIATELY AFTER AddRow!\n");
			
			s.append("	Next\n");
			s.append("	ABMGenTable" + uniqueName + ".Refresh\n");
			s.append("\n");
			s.append("	DBM.CloseSQL(SQL)\n");
			s.append("\n");
			if (definition.oHasPagination) {
				s.append("	Dim ABMGenPagination" + uniqueName + " As ABMPagination = page.Component(\"ABMGenPagination" + uniqueName + "\")\n");
				s.append("	If (numFound mod " + definition.oRowsPerPage + " > 0) Or (numFound = 0) Then\n");
				s.append("		numFound = numFound/" + definition.oRowsPerPage + " + 1\n");
				s.append("	Else\n");
				s.append("		numFound = numFound/" + definition.oRowsPerPage + "\n");
				s.append("	End If\n");
				s.append("	ABMGenPagination" + uniqueName + ".SetTotalNumberOfPages(numFound)\n");
				s.append("	ABMGenPagination" + uniqueName + ".SetActivePage(fromPage)\n");
				s.append("	ABMGenPagination" + uniqueName + ".Refresh\n");
			}
			s.append("\n");
			s.append("End Sub" + Mark  + "\n");
			s.append("\n");
			s.append("Sub ABMGenPagination" + uniqueName + "_PageChanged(OldPage As Int, NewPage As Int)\n");
			s.append("	' do your stuff\n");
			s.append("	LoadABMGenTable" + uniqueName + "(NewPage)\n");
			s.append("End Sub\n");
			s.append("\n");
			
			s.append("Sub ABMGenTableAdd" + uniqueName + "_Clicked(Target As String, SubTarget As String)\n");	
			s.append("	' reset all the values on the form\n");
			s.append("	Active" + uniqueName + definition.IDFieldName + " = 0\n");
			s.append("	ABMGen" + uniqueName + "New\n");
			s.append("End Sub\n");
			s.append("\n");
			
			s.append("Sub ABMGenTable" + uniqueName + "_Clicked(PassedRowsAndColumns As List)\n");
			s.append("	' fill with the active values\n");
			s.append("	Dim tblCellInfo As ABMTableCell = PassedRowsAndColumns.Get(0)\n");
			s.append("	Dim ABMGenTable" + uniqueName + " As ABMTable = page.Component(tblCellInfo.TableName)\n");
			int IDFieldColumn = FindIDFieldColumn(definition);
			s.append("	If tblCellInfo.Column = " + definition.FieldNames.size() + " Then ' edit\n");
			if (IDFieldColumn==-1) {
				s.append("		ERROR: could not retrieve the column of the ID Field!!!\n");
			}
			s.append("		Active" + uniqueName + definition.IDFieldName + " = ABMGenTable" + uniqueName + ".GetString(tblCellInfo.row, " + IDFieldColumn + ")\n");
			s.append("		ABMGen" + uniqueName + "Edit(Active" + uniqueName + definition.IDFieldName + ")\n");
			s.append("	End If\n");
			s.append("	If tblCellInfo.Column = " + (definition.FieldNames.size()+1) + " Then ' delete\n");
			if (IDFieldColumn==-1) {
				s.append("		ERROR: could not retrieve the column of the ID Field!!!\n");
			}
			s.append("		Active" + uniqueName + definition.IDFieldName + " = ABMGenTable" + uniqueName + ".GetString(tblCellInfo.row, " + IDFieldColumn + ")\n");
			s.append("		ABMGen" + uniqueName + "Delete(Active" + uniqueName + definition.IDFieldName + ")\n");
			s.append("	End If\n");
			s.append("End Sub\n");
			s.append("\n");
			
			s.append("Sub ABMGenTable" + uniqueName + "_SortChanged(DataField As String, Order As String)\n");
			s.append("	Select Case DataField\n");
			s.append("		Case " + BuildSorts(definition) + "\n");
			s.append("			LastSort" + uniqueName + " = \" ORDER BY \" & DataField & \" \" & Order & \" \"\n");
			s.append("		Case Else\n");
			s.append("			LastSort" + uniqueName + " = \"\"\n");
			s.append("	End Select\n");
			s.append("	' reload the table\n");
			if (definition.oHasPagination) {
				s.append("	Dim ABMGenPagination" + uniqueName + " As ABMPagination = page.Component(\"ABMGenPagination" + uniqueName + "\")\n");		
				s.append("	LoadABMGenTable" + uniqueName + "(ABMGenPagination" + uniqueName + ".GetActivePage())\n");
			} else {
				s.append("	LoadABMGenTable" + uniqueName + "(1)\n");
			}
			s.append("End Sub\n");
			s.append("\n");
			
			s.append("Sub ABMGenSearchBtn" + uniqueName + "_Clicked(Target As String)\n");
			s.append("	DoSearch" + uniqueName + "\n");
			s.append("End Sub\n");
			s.append("\n");

			s.append("Sub ABMGenSearch" + uniqueName + "_EnterPressed(value As String)\n");
			s.append("	DoSearch" + uniqueName + "\n");
			s.append("End Sub\n");
			s.append("\n");

			s.append("Sub DoSearch" + uniqueName + "()" + Mark  + "\n");
			s.append("	Dim ABMGenSearch" + uniqueName + " As ABMInput = page.Component(\"ABMGenSearch" + uniqueName + "\")\n");
			s.append("	Filter" + uniqueName + " = ABMGenSearch" + uniqueName + ".Text\n");
			s.append("	If Filter" + uniqueName + " <> \"\" Then" + Mark  + "\n");
			s.append("		Filter" + uniqueName + " = \" WHERE (" + BuildFilter(definition, uniqueName) + ") \"" + Mark  + "\n");
			s.append("	Else" + Mark  + "\n");
			s.append("		Filter" + uniqueName + " = \"\"" + Mark  + "\n");
			s.append("	End If" + Mark  + "\n");
			s.append("	' reload the table\n");
			s.append("	LoadABMGenTable" + uniqueName + "(1)\n");
			s.append("End Sub" + Mark  + "\n");
			
			s.append("#End Region\n");
			s.append("\n");
		}
		
		
		s.append("#Region " + uniqueName + "\n");
		s.append("Sub ABMGenBuild" + uniqueName + "() As ABMModalSheet" + Mark  + "\n");
		s.append("	Dim ABMGen" + uniqueName + "Modal As ABMModalSheet\n");
		s.append("	ABMGen" + uniqueName + "Modal.Initialize(page, \"" + uniqueName + "\", True, False, \"\")\n");
		s.append("	ABMGen" + uniqueName + "Modal.IsDismissible = False\n");		
		switch (modalSheetSize) {
			case ABMaterial.MODALSHEET_SIZE_NORMAL:
				s.append("	ABMGen" + uniqueName + "Modal.Size = ABM.MODALSHEET_SIZE_NORMAL\n");
				break;
			case ABMaterial.MODALSHEET_SIZE_LARGE:
				s.append("	ABMGen" + uniqueName + "Modal.Size = ABM.MODALSHEET_SIZE_LARGE\n");
				break;
			case ABMaterial.MODALSHEET_SIZE_FULL:
				s.append("	ABMGen" + uniqueName + "Modal.Size = ABM.MODALSHEET_SIZE_FULL\n");
				break;
		}	
		
		// content
		s.append("\n");
		if (!definition.Title.equals("")) {
			s.append("	ABMGen" + uniqueName + "Modal.Content.AddRowsM(1, True, 0, 0, \"\").AddCells12(1, \"\")\n");
		}
		int rowCounter=1;
		String prevRow="";
		String currRow="";
		for (int i=0;i<definition.MaxRow;i++) {
			
			boolean HasCells=false;
			for (int j=0;j<definition.Rows.size();j++) {
				if ((definition.Rows.get(j)-1)==i) {
					currRow += ".AddCellsOS(1, " + definition.OSmalls.get(j) + ", " + definition.OMediums.get(j) + ", " + definition.OLarges.get(j) + ", " + definition.SSmalls.get(j) + ", " + definition.SMediums.get(j) + ", " + definition.SLarges.get(j) + ", \"\")";
					HasCells=true;
				}
			}
			if (!HasCells) {
				currRow += ".AddCells12(1, \"\")";
			}
			
			currRow += "\n";
			if (currRow.equals(prevRow)) {				
				rowCounter++;				
			} else {
				if (!prevRow.equals("")) {
					s.append("	ABMGen" + uniqueName + "Modal.Content.AddRowsM(" + rowCounter + ", True, 0, 0, \"\")" + prevRow);
				}
				rowCounter=1;
			}
			prevRow = currRow;
			currRow="";			
		}
		if (!prevRow.equals("")) {
			s.append("	ABMGen" + uniqueName + "Modal.Content.AddRowsM(" + rowCounter + ", True, 0, 0, \"\")" + prevRow);
		}
		int lastRow=1;
		s.append("	ABMGen" + uniqueName + "Modal.Content.BuildGrid 'IMPORTANT once you loaded the complete grid AND before you start adding components\n\n");
		if (!definition.Title.equals("")) {
			s.append("	Dim ABMGen" + uniqueName + "TitleMessage As ABMLabel\n");
			s.append("	ABMGen" + uniqueName + "TitleMessage.Initialize(page, \"ABMGen" + uniqueName + "TitleMessage\", \"" + definition.Title + "\" , ABM.SIZE_PARAGRAPH, False, \"\")\n");
			s.append("	ABMGen" + uniqueName + "TitleMessage.IsBlockQuote = True\n");
			s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(0,1).AddComponent(ABMGen" + uniqueName + "TitleMessage)\n\n");			
		}
		
		int CurrCell=0;
		Map<String,Integer> fieldParams = new HashMap<String,Integer>();
		Map<String,List<String>> fieldReloads = new HashMap<String,List<String>>();
		Map<String,String> fieldSQLs = new HashMap<String,String>();
		Map<String,String> fieldDefaults = new HashMap<String,String>();
		for (int j=0;j<definition.Rows.size();j++) {
			if (!definition.Title.equals("")) {
				int val = definition.Rows.get(j);
				if (val!=0) {
					definition.Rows.set(j, val+1); 
				}
			}
			definition.FieldNames.get(j);
			fieldParams.put(definition.FieldNames.get(j).toLowerCase(), j);
			fieldReloads.put(definition.FieldNames.get(j).toLowerCase(), new ArrayList<String>());
			fieldSQLs.put(definition.FieldNames.get(j).toLowerCase(), definition.Queries.get(j));
			fieldDefaults.put(definition.FieldNames.get(j).toLowerCase(), definition.Defaults.get(j));
		}
		String ScrollerMode="ABM.DATETIMESCROLLER_MODE_MIXED";
		switch (definition.DateTimeScroller.Mode) {
		case ABMaterial.DATETIMESCROLLER_MODE_SCROLLER:
			ScrollerMode="ABM.DATETIMESCROLLER_MODE_SCROLLER";
			break;
		case ABMaterial.DATETIMESCROLLER_MODE_CLICKPICK:
			ScrollerMode="ABM.DATETIMESCROLLER_MODE_CLICKPICK";
			break;
		case ABMaterial.DATETIMESCROLLER_MODE_MIXED:
			ScrollerMode="ABM.DATETIMESCROLLER_MODE_MIXED";
			break;
		}
		for (int j=0;j<definition.Rows.size();j++) {
		  if (definition.Rows.get(j)!=0) {
			String compName="ABMGen" + uniqueName + definition.FieldNames.get(j);  
			switch (definition.Types.get(j)) {
				case ABMaterial.GEN_TEXT:
				case ABMaterial.GEN_DOUBLE:
					s.append("	Dim " + compName + " As ABMInput\n");
					s.append("	" + compName + ".Initialize(page, \"" + compName + "\", ABM.INPUT_TEXT, \"" + definition.Labels.get(j) + "\", False, \"\")\n");
					if (!definition.Enableds.get(j)) {
						s.append("	" + compName + ".Enabled=false\n");
					}
					CurrCell++;
					if (definition.Rows.get(j)>lastRow) {
						CurrCell=1;
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(" + (definition.Rows.get(j) - lastRow) + "," + CurrCell + ").AddComponent(" + compName + ")\n");
						lastRow = definition.Rows.get(j);				
					} else {
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(0," + CurrCell + ").AddComponent(" + compName + ")\n");
					}
					s.append("	" + compName + ".Text = \"" + definition.Defaults.get(j) + "\"\n\n");
					break;
				case ABMaterial.GEN_INTEGER:
					s.append("	Dim " + compName + " As ABMInput\n");
					s.append("	" + compName + ".Initialize(page, \"" + compName + "\", ABM.INPUT_TEXT, \"" + definition.Labels.get(j) + "\", False, \"\")\n");
					if (!definition.Enableds.get(j)) {
						s.append("	" + compName + ".Enabled=false\n");
					}
					CurrCell++;
					if (definition.Rows.get(j)>lastRow) {
						CurrCell=1;
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(" + (definition.Rows.get(j) - lastRow) + "," + CurrCell + ").AddComponent(" + compName + ")\n");
						lastRow = definition.Rows.get(j);				
					} else {
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(0," + CurrCell + ").AddComponent(" + compName + ")\n");
					}
					s.append("	" + compName + ".Text = \"" + definition.Defaults.get(j) + "\"\n\n");
					break;
				case ABMaterial.GEN_TEXTAREA:
					s.append("	Dim " + compName + " As ABMInput\n");
					s.append("	" + compName + ".Initialize(page, \"" + compName + "\", ABM.INPUT_TEXT, \"" + definition.Labels.get(j) + "\", True, \"\")\n");
					if (!definition.Enableds.get(j)) {
						s.append("	" + compName + ".Enabled=false\n");
					}
					CurrCell++;
					if (definition.Rows.get(j)>lastRow) {
						CurrCell=1;
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(" + (definition.Rows.get(j) - lastRow) + "," + CurrCell + ").AddComponent(" + compName + ")\n");
						lastRow = definition.Rows.get(j);				
					} else {
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(0," + CurrCell + ").AddComponent(" + compName + ")\n");
					}
					s.append("	" + compName + ".Text = \"" + definition.Defaults.get(j) + "\"\n\n");
					break;
				case ABMaterial.GEN_CHECKBOX:
					s.append("	Dim " + compName + " As ABMCheckbox\n");
					s.append("	" + compName + ".Initialize(page, \"" + compName + "\", \"" + definition.Labels.get(j) + "\", " + definition.Defaults.get(j) + ", \"\")\n");
					if (!definition.Enableds.get(j)) {
						s.append("	" + compName + ".Enabled=false\n");
					}
					CurrCell++;
					if (definition.Rows.get(j)>lastRow) {
						CurrCell=1;
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(" + (definition.Rows.get(j) - lastRow) + "," + CurrCell + ").AddComponent(" + compName + ")\n");
						lastRow = definition.Rows.get(j);				
					} else {
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(0," + CurrCell + ").AddComponent(" + compName + ")\n");
					}
					s.append("	" + compName + ".State = " + definition.Defaults.get(j) + "\n\n");
					break;
				case ABMaterial.GEN_COMBOLIST:
					s.append("	Dim " + compName + " As ABMCombo\n");
					s.append("	" + compName + ".Initialize(page, \"" + compName + "\", \"" + definition.Labels.get(j) + "\", 500, \"\")\n");
					if (!definition.Enableds.get(j)) {
						s.append("	" + compName + ".Enabled=false\n");
					}
					anywheresoftware.b4a.objects.collections.Map m = (anywheresoftware.b4a.objects.collections.Map) definition.Items.get(j);
					if (m!=null && m.IsInitialized()) {
						for (int k=0;k<m.getSize();k++) {
							s.append("	" + compName + ".AddItem(\"" + (String)m.GetKeyAt(k) + "\", \"" + (String)m.GetValueAt(k) + "\", ABMGenBuild" + uniqueName + "ComboItem(\"" + compName + k + "\", \"" + (String)m.GetValueAt(k) + "\"))\n");
						}
					}
					CurrCell++;
					if (definition.Rows.get(j)>lastRow) {
						CurrCell=1;
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(" + (definition.Rows.get(j) - lastRow) + "," + CurrCell + ").AddComponent(" + compName + ")\n");
						lastRow = definition.Rows.get(j);				
					} else {
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(0," + CurrCell + ").AddComponent(" + compName + ")\n");
					}
					s.append("	" + compName + ".SetActiveItemId(\"" + definition.Defaults.get(j) + "\")\n\n");
					break;
				case ABMaterial.GEN_COMBOSQL:
					s.append("	Dim " + compName + " As ABMCombo\n");
					s.append("	" + compName + ".Initialize(page, \"" + compName + "\", \"" + definition.Labels.get(j) + "\", 500, \"\")\n");
					if (!definition.Enableds.get(j)) {
						s.append("	" + compName + ".Enabled=false\n");
					}
					String mySQL = definition.Queries.get(j);
					Pattern pattern = Pattern.compile("\\$(.*?)\\$");
					Matcher matcher = pattern.matcher(mySQL);
					while (matcher.find())
					{
						
						int tmpV = fieldParams.getOrDefault(matcher.group(1).toLowerCase(), -1);
						if (tmpV!=-1) {
							mySQL = mySQL.replaceAll("\\$" + matcher.group(1) + "\\$", definition.Defaults.get(tmpV));	
						}
						List<String> myList = fieldReloads.get(matcher.group(1).toLowerCase());
						if (myList!=null) {
							myList.add(definition.FieldNames.get(j));
							fieldReloads.put(matcher.group(1).toLowerCase(), myList);
						}
					}
					
					s.append("	Dim SQL as SQL = DBM.GetSQL\n");
					s.append("	Dim SQL_Str as String = \"" + mySQL + "\"" + Mark  + "\n");
					s.append("	Dim results As List = DBM.ABMGenSQLSelect(SQL, SQL_Str, null)\n");
					s.append("	For l = 0 To results.Size - 1\n");
					s.append("		Dim res As Map = results.Get(l)\n");								
					s.append("		" + compName + ".AddItem(res.GetValueAt(0), res.GetValueAt(1), ABMGenBuild" + uniqueName + "ComboItem(\"" + compName + "\" & (l+1), res.GetValueAt(1)))\n");
					s.append("	Next\n");
					s.append("	DBM.CloseSQL(SQL)\n");		
					CurrCell++;
					if (definition.Rows.get(j)>lastRow) {
						CurrCell=1;
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(" + (definition.Rows.get(j) - lastRow) + "," + CurrCell + ").AddComponent(" + compName + ")\n");
						lastRow = definition.Rows.get(j);				
					} else {
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(0," + CurrCell + ").AddComponent(" + compName + ")\n");
					}
					s.append("	" + compName + ".SetActiveItemId(\"" + definition.Defaults.get(j) + "\")\n\n");					
					break;
				case ABMaterial.GEN_SWITCH:
					s.append("	Dim " + compName + " As ABMSwitch\n");
					String spl[] = definition.Labels.get(j).split(",");
					String spl0="";
					String spl1="";
					switch (spl.length) {
						case 0:
							break;
						case 1:
							spl0 = spl[0];
							break;
						case 2:
							spl0 = spl[0];
							spl1 = spl[1];
							break;
					}
					s.append("	" + compName + ".Initialize(page, \"" + compName + "\", \"" + spl0 + "\", \"" + spl1 + "\", " + definition.Defaults.get(j) + ", \"\")\n");
					if (!definition.Enableds.get(j)) {
						s.append("	" + compName + ".Enabled=false\n");
					}
					CurrCell++;
					if (definition.Rows.get(j)>lastRow) {
						CurrCell=1;
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(" + (definition.Rows.get(j) - lastRow) + "," + CurrCell + ").AddComponent(" + compName + ")\n");
						lastRow = definition.Rows.get(j);				
					} else {
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(0," + CurrCell + ").AddComponent(" + compName + ")\n");
					}
					s.append("	" + compName + ".State = " + definition.Defaults.get(j) + "\n\n");
					break;	
				case ABMaterial.GEN_DATE_SCROLL:
					s.append("	Dim " + compName + " As ABMDateTimeScroller\n");
					s.append("	Dim NewDate As Long = DateTime.Now 'ignore\n");
					s.append("	" + compName + ".Initialize(page, \"" + compName + "\", ABM.DATETIMESCROLLER_TYPE_DATE, " + ScrollerMode + ", NewDate, \"" + definition.Labels.get(j) + "\", \"\")\n");
					if (!definition.Enableds.get(j)) {
						s.append("	" + compName + ".Enabled=false\n");
					}
					
					s.append("	" + compName + ".TitleDateFormat = \"" + definition.DateTimeScroller.TitleDateFormat + "\"\n"); 
					s.append("	" + compName + ".ReturnDateFormat = \"" + definition.DateTimeScroller.ReturnDateFormat + "\"\n");
					s.append("	" + compName + ".DateOrder = \"" + definition.DateTimeScroller.DateOrder + "\"\n");
					s.append("	" + compName + ".DateStartYearNowMinus = " + definition.DateTimeScroller.DateStartYearNowMinus + "\n");
					s.append("	" + compName + ".DateEndYearNowPlus = " + definition.DateTimeScroller.DateEndYearNowPlus + "\n");
					s.append("	" + compName + ".DateMonthNames = \"" + definition.DateTimeScroller.DateMonthNames + "\"\n");
					s.append("	" + compName + ".DateMonthNamesShort = \"" + definition.DateTimeScroller.DateMonthNamesShort + "\"\n");
					s.append("	" + compName + ".DateDayNames = \"" + definition.DateTimeScroller.DateDayNames + "\"\n");
					s.append("	" + compName + ".DateDayNamesShort = \"" + definition.DateTimeScroller.DateDayNamesShort + "\"\n");
					s.append("	" + compName + ".DateMonthText = \"" + definition.DateTimeScroller.DateMonthText + "\"\n");
					s.append("	" + compName + ".DateDayText = \"" + definition.DateTimeScroller.DateDayText + "\"\n");
					s.append("	" + compName + ".DateYearText = \"" + definition.DateTimeScroller.DateYearText + "\"\n");
					s.append("	" + compName + ".DateShortYearCutoff = \"" + definition.DateTimeScroller.DateShortYearCutoff + "\"\n");
					s.append("	" + compName + ".PickText = \"" + definition.DateTimeScroller.PickText + "\"\n");
					s.append("	" + compName + ".CancelText = \"" + definition.DateTimeScroller.CancelText + "\"\n");
					
					CurrCell++;
					if (definition.Rows.get(j)>lastRow) {
						CurrCell=1;
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(" + (definition.Rows.get(j) - lastRow) + "," + CurrCell + ").AddComponent(" + compName + ")\n\n");
						lastRow = definition.Rows.get(j);				
					} else {
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(0," + CurrCell + ").AddComponent(" + compName + ")\n\n");
					}
					break;					
				case ABMaterial.GEN_TIME_SCROLL:
					s.append("	Dim " + compName + " As ABMDateTimeScroller\n");
					s.append("	Dim NewDate As Long = DateTime.Now 'ignore\n");
					s.append("	" + compName + ".Initialize(page, \"" + compName + "\", ABM.DATETIMESCROLLER_TYPE_TIME, ABM.DATETIMESCROLLER_MODE_MIXED, NewDate, \"" + definition.Labels.get(j) + "\", \"\")\n");
					if (!definition.Enableds.get(j)) {
						s.append("	" + compName + ".Enabled=false\n");
					}
					
					s.append("	" + compName + ".TimeShowAMPM = " + definition.DateTimeScroller.TimeShowAMPM + "\n"); 
					s.append("	" + compName + ".TimeShowSeconds = " + definition.DateTimeScroller.TimeShowSeconds + "\n");
					s.append("	" + compName + ".TitleTimeFormat = \"" + definition.DateTimeScroller.TitleTimeFormat + "\"\n");
					s.append("	" + compName + ".ReturnTimeFormat = \"" + definition.DateTimeScroller.ReturnTimeFormat + "\"\n");
					s.append("	" + compName + ".TimeHourText = \"" + definition.DateTimeScroller.TimeHourText + "\"\n");
					s.append("	" + compName + ".TimeMinuteText = \"" + definition.DateTimeScroller.TimeMinuteText + "\"\n");
					s.append("	" + compName + ".TimeSecondsText = \"" + definition.DateTimeScroller.TimeSecondsText + "\"\n");
					s.append("	" + compName + ".TimeAMPMText = \"" + definition.DateTimeScroller.TimeAMPMText + "\"\n");
					s.append("	" + compName + ".PickText = \"" + definition.DateTimeScroller.PickText + "\"\n");
					s.append("	" + compName + ".CancelText = \"" + definition.DateTimeScroller.CancelText + "\"\n");
					
					CurrCell++;
					if (definition.Rows.get(j)>lastRow) {
						CurrCell=1;
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(" + (definition.Rows.get(j) - lastRow) + "," + CurrCell + ").AddComponent(" + compName + ")\n\n");
						lastRow = definition.Rows.get(j);				
					} else {
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(0," + CurrCell + ").AddComponent(" + compName + ")\n\n");
					}
					break;
				case ABMaterial.GEN_DATETIME_SCROLL:
					s.append("	Dim " + compName + " As ABMDateTimeScroller\n");
					s.append("	Dim NewDate As Long = DateTime.Now 'ignore\n");
					s.append("	" + compName + ".Initialize(page, \"" + compName + "\", ABM.DATETIMESCROLLER_TYPE_DATETIME, ABM.DATETIMESCROLLER_MODE_MIXED, NewDate, \"" + definition.Labels.get(j) + "\", \"\")\n");
					if (!definition.Enableds.get(j)) {
						s.append("	" + compName + ".Enabled=false\n");
					}
					
					s.append("	" + compName + ".TitleDateFormat = \"" + definition.DateTimeScroller.TitleDateFormat + "\"\n"); 
					s.append("	" + compName + ".ReturnDateFormat = \"" + definition.DateTimeScroller.ReturnDateFormat + "\"\n");
					s.append("	" + compName + ".DateOrder = \"" + definition.DateTimeScroller.DateOrder + "\"\n");
					s.append("	" + compName + ".TimeShowAMPM = " + definition.DateTimeScroller.TimeShowAMPM + "\n"); 
					s.append("	" + compName + ".TimeShowSeconds = " + definition.DateTimeScroller.TimeShowSeconds + "\n");
					s.append("	" + compName + ".TitleTimeFormat = \"" + definition.DateTimeScroller.TitleTimeFormat + "\"\n");
					s.append("	" + compName + ".ReturnTimeFormat = \"" + definition.DateTimeScroller.ReturnTimeFormat + "\"\n");
					s.append("	" + compName + ".DateStartYearNowMinus = " + definition.DateTimeScroller.DateStartYearNowMinus + "\n");
					s.append("	" + compName + ".DateEndYearNowPlus = " + definition.DateTimeScroller.DateEndYearNowPlus + "\n");
					s.append("	" + compName + ".DateMonthNames = \"" + definition.DateTimeScroller.DateMonthNames + "\"\n");
					s.append("	" + compName + ".DateMonthNamesShort = \"" + definition.DateTimeScroller.DateMonthNamesShort + "\"\n");
					s.append("	" + compName + ".DateDayNames = \"" + definition.DateTimeScroller.DateDayNames + "\"\n");
					s.append("	" + compName + ".DateDayNamesShort = \"" + definition.DateTimeScroller.DateDayNamesShort + "\"\n");
					s.append("	" + compName + ".DateMonthText = \"" + definition.DateTimeScroller.DateMonthText + "\"\n");
					s.append("	" + compName + ".DateDayText = \"" + definition.DateTimeScroller.DateDayText + "\"\n");
					s.append("	" + compName + ".DateYearText = \"" + definition.DateTimeScroller.DateYearText + "\"\n");
					s.append("	" + compName + ".DateShortYearCutoff = \"" + definition.DateTimeScroller.DateShortYearCutoff + "\"\n");
					s.append("	" + compName + ".TimeHourText = \"" + definition.DateTimeScroller.TimeHourText + "\"\n");
					s.append("	" + compName + ".TimeMinuteText = \"" + definition.DateTimeScroller.TimeMinuteText + "\"\n");
					s.append("	" + compName + ".TimeSecondsText = \"" + definition.DateTimeScroller.TimeSecondsText + "\"\n");
					s.append("	" + compName + ".TimeAMPMText = \"" + definition.DateTimeScroller.TimeAMPMText + "\"\n");
					s.append("	" + compName + ".PickText = \"" + definition.DateTimeScroller.PickText + "\"\n");
					s.append("	" + compName + ".CancelText = \"" + definition.DateTimeScroller.CancelText + "\"\n");
					
					CurrCell++;
					if (definition.Rows.get(j)>lastRow) {
						CurrCell=1;
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(" + (definition.Rows.get(j) - lastRow) + "," + CurrCell + ").AddComponent(" + compName + ")\n\n");
						lastRow = definition.Rows.get(j);				
					} else {
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(0," + CurrCell + ").AddComponent(" + compName + ")\n\n");
					}
					break;
				case ABMaterial.GEN_DATE_PICK:
					s.append("	Dim " + compName + " As ABMDateTimePicker\n");
					s.append("	Dim NewDate As Long = DateTime.Now 'ignore\n");
					s.append("	" + compName + ".Initialize(page, \"" + compName + "\", ABM.DATETIMEPICKER_TYPE_DATE, NewDate, \"" + definition.Labels.get(j) + "\", \"\")\n");
					if (!definition.Enableds.get(j)) {
						s.append("	" + compName + ".Enabled=false\n");
					}
					
					s.append("	" + compName + ".ReturnDateFormat = \"" + definition.DateTimePicker.ReturnDateFormat + "\"\n");
					s.append("	" + compName + ".MinimumDate = " + definition.DateTimePicker.MinimumDate + "\n");
					s.append("	" + compName + ".MaximumDate = " + definition.DateTimePicker.MaximumDate + "\n");
					s.append("	" + compName + ".Language = \"" + definition.DateTimePicker.Language + "\"\n");
					s.append("	" + compName + ".FirstDayOfWeek = "  + definition.DateTimePicker.FirstDayOfWeek + "\n");
					s.append("	" + compName + ".PickText = \"" + definition.DateTimePicker.PickText + "\"\n");
					s.append("	" + compName + ".CancelText = \"" + definition.DateTimePicker.CancelText + "\"\n");
					s.append("	" + compName + ".TodayText = \"" + definition.DateTimePicker.TodayText + "\"\n");
					
					CurrCell++;
					if (definition.Rows.get(j)>lastRow) {
						CurrCell=1;
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(" + (definition.Rows.get(j) - lastRow) + "," + CurrCell + ").AddComponent(" + compName + ")\n\n");
						lastRow = definition.Rows.get(j);				
					} else {
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(0," + CurrCell + ").AddComponent(" + compName + ")\n\n");
					}
					break;	
				case ABMaterial.GEN_TIME_PICK:
					s.append("	Dim " + compName + " As ABMDateTimePicker\n");
					s.append("	Dim NewDate As Long = DateTime.Now 'ignore\n");
					s.append("	" + compName + ".Initialize(page, \"" + compName + "\", ABM.DATETIMEPICKER_TYPE_TIMEE, NewDate, \"" + definition.Labels.get(j) + "\", \"\")\n");
					if (!definition.Enableds.get(j)) {
						s.append("	" + compName + ".Enabled=false\n");
					}
					
					s.append("	" + compName + ".ReturnTimeFormat = \"" + definition.DateTimePicker.ReturnTimeFormat + "\"\n");
					s.append("	" + compName + ".Language = \"" + definition.DateTimePicker.Language + "\"\n");
					s.append("	" + compName + ".ShortTime = " + definition.DateTimePicker.ShortTime + "\n");
					s.append("	" + compName + ".PickText = \"" + definition.DateTimePicker.PickText + "\"\n");
					s.append("	" + compName + ".CancelText = \"" + definition.DateTimePicker.CancelText + "\"\n");
					
					CurrCell++;
					if (definition.Rows.get(j)>lastRow) {
						CurrCell=1;
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(" + (definition.Rows.get(j) - lastRow) + "," + CurrCell + ").AddComponent(" + compName + ")\n\n");
						lastRow = definition.Rows.get(j);				
					} else {
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(0," + CurrCell + ").AddComponent(" + compName + ")\n\n");
					}
					break;
				case ABMaterial.GEN_DATETIME_PICK:
					s.append("	Dim " + compName + " As ABMDateTimePicker\n");
					s.append("	Dim NewDate As Long = DateTime.Now 'ignore\n");
					s.append("	" + compName + ".Initialize(page, \"" + compName + "\", ABM.DATETIMEPICKER_TYPE_DATETIME, NewDate, \"" + definition.Labels.get(j) + "\", \"\")\n");
					if (!definition.Enableds.get(j)) {
						s.append("	" + compName + ".Enabled=false\n");
					}
					
					s.append("	" + compName + ".ReturnDateFormat = \"" + definition.DateTimePicker.ReturnDateFormat + "\"\n");
					s.append("	" + compName + ".ReturnTimeFormat = \"" + definition.DateTimePicker.ReturnTimeFormat + "\"\n");
					s.append("	" + compName + ".MinimumDate = " + definition.DateTimePicker.MinimumDate + "\n");
					s.append("	" + compName + ".MaximumDate = " + definition.DateTimePicker.MaximumDate + "\n");
					s.append("	" + compName + ".Language = \"" + definition.DateTimePicker.Language + "\"\n");
					s.append("	" + compName + ".FirstDayOfWeek = "  + definition.DateTimePicker.FirstDayOfWeek + "\n");
					s.append("	" + compName + ".ShortTime = " + definition.DateTimePicker.ShortTime + "\n");
					s.append("	" + compName + ".PickText = \"" + definition.DateTimePicker.PickText + "\"\n");
					s.append("	" + compName + ".CancelText = \"" + definition.DateTimePicker.CancelText + "\"\n");
					s.append("	" + compName + ".TodayText = \"" + definition.DateTimePicker.TodayText + "\"\n");
					
					CurrCell++;
					if (definition.Rows.get(j)>lastRow) {
						CurrCell=1;
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(" + (definition.Rows.get(j) - lastRow) + "," + CurrCell + ").AddComponent(" + compName + ")\n\n");
						lastRow = definition.Rows.get(j);				
					} else {
						s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(0," + CurrCell + ").AddComponent(" + compName + ")\n\n");
					}
					break;
			}
		  }
		}
		
		// footer
		s.append("	ABMGen" + uniqueName + "Modal.Footer.AddRowsM(1,True,0,0, \"\").AddCells12(1,\"\")\n");
		s.append("	ABMGen" + uniqueName + "Modal.Footer.BuildGrid 'IMPORTANT once you loaded the complete grid AND before you start adding components\n");
		s.append("\n");	
		s.append("	' create the buttons for the footer, create in opposite order as aligned right in a footer\n");
		s.append("	Dim ABMGen" + uniqueName + "Cancel As ABMButton\n");
		s.append("	ABMGen" + uniqueName + "Cancel.InitializeFlat(page, \"ABMGen" + uniqueName + "Cancel\", \"\", \"\", \"" + definition.CancelButtonText + "\", \"transparent\")\n");
		s.append("	ABMGen" + uniqueName + "Modal.Footer.Cell(1,1).AddComponent(ABMGen" + uniqueName + "Cancel)\n");
		s.append("	\n");
		s.append("	Dim ABMGen" + uniqueName + "Save As ABMButton\n");
		s.append("	ABMGen" + uniqueName + "Save.InitializeFlat(page, \"ABMGen" + uniqueName + "Save\", \"\", \"\", \"" + definition.SaveButtonText + "\", \"transparent\")\n");
		s.append("	ABMGen" + uniqueName + "Modal.Footer.Cell(1,1).AddComponent(ABMGen" + uniqueName + "Save)\n");
		s.append("\n");
		s.append("	Return ABMGen" + uniqueName + "Modal\n");
		s.append("End Sub" + Mark  + "\n");
		s.append("\n");	
		
		for (Entry <String, List<String>> entry: fieldReloads.entrySet()) {
			String compName="ABMGen" + uniqueName + entry.getKey();
			List<String> myList = entry.getValue();
			if (!myList.isEmpty()) {
				s.append("Sub " + compName + "_Clicked(Target as String)" + Mark  + "\n");
				s.append("	Dim ABMGen" + uniqueName + "Modal As ABMModalSheet = page.ModalSheet(\"" + uniqueName + "\")\n\n");
				s.append("	Dim SQL as SQL = DBM.GetSQL\n");
				for (int j=0;j<myList.size();j++) {
					String mySQL = fieldSQLs.get(myList.get(j).toLowerCase());
					s.append("	Dim SQL_Str as String = \"" + mySQL + "\"" + Mark  + "\n\n");
					String compNameTgt="ABMGen" + uniqueName + myList.get(j);
					s.append("	Dim " + compNameTgt + " As ABMCombo = ABMGen" + uniqueName + "Modal.Content.Component(\"" + compNameTgt + "\")\n");
					s.append("	" + compNameTgt + ".Clear\n");
							
					Pattern pattern = Pattern.compile("\\$(.*?)\\$");
					Matcher matcher = pattern.matcher(mySQL);
					while (matcher.find())
					{
						String compNameSrc = "ABMGen" + uniqueName + matcher.group(1);
						s.append("	Dim " + compNameSrc + " AS ABMCombo = ABMGen" + uniqueName + "Modal.Content.Component(\"" + compNameSrc + "\")\n");
						s.append("	SQL_str = ABMShared.ReplaceAll(SQL_str, \"" + matcher.group(0) + "\", " + compNameSrc + ".GetActiveItemId)\n\n");												
					}					
					
					s.append("	Dim results As List = DBM.ABMGenSQLSelect(SQL, SQL_Str, null)\n");
					s.append("	For l = 0 To results.Size - 1\n");
					s.append("		Dim res As Map = results.Get(l)\n");								
					s.append("		" + compNameTgt + ".AddItem(res.GetValueAt(0), res.GetValueAt(1), ABMGenBuild" + uniqueName + "ComboItem(\"" + compNameTgt + "\" & (l+1), res.GetValueAt(1)))\n");
					s.append("	Next\n\n");
					s.append("	" + compNameTgt + ".SetActiveItemId(\"" + fieldDefaults.get(myList.get(j).toLowerCase()) + "\")\n\n");
					s.append("	" + compNameTgt + ".Refresh(\"\")\n\n");
				}	
				s.append("	DBM.CloseSQL(SQL)\n");	
				s.append("End Sub" + Mark  + "\n\n");
			}
		}
		
		s.append("' method you can call when the user wants to add a new record\n");
		s.append("Sub ABMGen" + uniqueName + "New()\n");
		s.append("	Dim ABMGen" + uniqueName + "Modal As ABMModalSheet = page.ModalSheet(\"" + uniqueName + "\")\n");
		s.append("	Active" + uniqueName + definition.IDFieldName + " = 0\n\n");
		s.append("	Dim NewDate As Long = DateTime.Now 'ignore\n");
		for (int j=0;j<definition.Rows.size();j++) {
			if (definition.Rows.get(j)!=0) {
				s.append(BuildDIMS(uniqueName, definition, j, "	"));
			}
		} 
		s.append("\n");	
		for (int j=0;j<definition.Rows.size();j++) {
			if (definition.Rows.get(j)!=0) {
				s.append(BuildSETSNew(uniqueName, definition, j));
			}
		}
		s.append("	IsNew" + uniqueName + "=true\n");
		s.append("	page.ShowModalSheet(\"" + uniqueName + "\")\n");
		s.append("End Sub\n");
		s.append("\n");		
		
		s.append("' method you can call when the user wants to edit an existing record\n");
		s.append("Sub ABMGen" + uniqueName + "Edit(openId As int)" + Mark  + "\n");
		s.append("	Dim ABMGen" + uniqueName + "Modal As ABMModalSheet = page.ModalSheet(\"" + uniqueName + "\")\n");
		s.append("	Active" + uniqueName + definition.IDFieldName + " = openId\n");
		s.append("	Dim NewDate As long = DateTime.Now 'ignore\n");
		s.append("	Dim SQL As SQL = DBM.GetSQL\n");
		s.append("	Dim SQL_str As String = " + BuildSELECT(definition) + "" + Mark  + "\n");
		s.append("	Dim results As List = DBM.ABMGenSQLSelect(SQL, SQL_Str, Array As Int(Active" + uniqueName + definition.IDFieldName + "))" + Mark + "\n");
		s.append("	If results.Size>0 Then\n");
		s.append("		Dim m as Map = results.Get(0)\n");
		for (int j=0;j<definition.Rows.size();j++) {
			if (definition.Rows.get(j)!=0) {
				s.append(BuildDIMS(uniqueName, definition, j, "		"));
			}
		} 
		s.append("\n");	
		for (int j=0;j<definition.Rows.size();j++) {
			if (definition.Rows.get(j)!=0) {
				s.append(BuildSETSEdit(uniqueName, definition, j));
			}
		}
		s.append("		IsNew" + uniqueName + "=false\n");
		s.append("		page.ShowModalSheet(\"" + uniqueName + "\")\n");	
		s.append("	Else\n");
		s.append("		log(\"No record found\")\n");
		s.append("	End If\n");	
		s.append("	DBM.CloseSQL(SQL)\n");
		s.append("End Sub" + Mark  + "\n");
		s.append("\n");
		
		s.append("' the user clicked save the form\n");
		s.append("Sub ABMGen" + uniqueName + "Save_Clicked(Target As String)" + Mark  + "\n");		
		s.append("	Dim ABMGen" + uniqueName + "Modal As ABMModalSheet = page.ModalSheet(\"" + uniqueName + "\")\n");
		
		s.append("	Dim Variables as List\n");
		s.append("	Variables.Initialize\n");
		s.append("	Dim NewDate as Long = DateTime.Now 'ignore\n\n");
		s.append("	Dim SQL as SQL = DBM.GetSQL\n");
		s.append("	Dim valueDouble as Double 'ignore\n");		
		s.append("	Dim valueString as String 'ignore\n");
		s.append("	Dim valueInt as Int 'ignore\n");
		s.append("	Dim valueBoolean as Boolean 'ignore\n");
		s.append("	Dim valueLong as Long 'ignore\n");
		s.append("	Dim ret as int 'ignore\n\n");
		
		s.append("	If IsNew" + uniqueName + " = false Then ' check if still exists" + Mark  + "\n");
		s.append("		Dim SQL_chk As String = " + BuildSELECTCheck(definition) + "" + Mark  + "\n");
		s.append("		IsNew" + uniqueName + " = (DBM.ABMGenSQLSelectSingleResult(SQL, SQL_chk, Array As Int(Active" + uniqueName + definition.IDFieldName + "))<=DBM.DBOK)" + Mark + "\n");
		s.append("	End If" + Mark  + "\n\n");
		s.append("	If IsNew" + uniqueName + " Then" + Mark  + "\n");
		for (int j=0;j<definition.Rows.size();j++) {
		  	s.append(BuildDIMS(uniqueName, definition,j, "		"));		  	
		}
		s.append("\n");		
		for (int j=0;j<definition.Rows.size();j++) {
		  	s.append(BuildGETS(uniqueName, definition,j));		  
		}
		s.append("		ret = DBM.ABMGenSQLInsert(SQL, \"" + BuildINSERT(definition) + "\", Variables)" + Mark  + "\n");
		s.append("	Else" + Mark  + "\n");
		for (int j=0;j<definition.Rows.size();j++) {
			if (definition.UseInUpdates.get(j)) {  	
				s.append(BuildDIMS(uniqueName, definition,j, "		"));
			}	
		}
		s.append("\n");
		for (int j=0;j<definition.Rows.size();j++) {
			if (definition.UseInUpdates.get(j)) {  	
				s.append(BuildGETS(uniqueName, definition,j));
			}	
		}
		s.append("		ret = DBM.ABMGenSQLUpdate(SQL, \"" + BuildUPDATE(definition) + ", Variables)" + Mark  + "\n");
		s.append("	End If" + Mark  + "\n");
		s.append("	DBM.CloseSQL(SQL)\n");
		s.append("	page.CloseModalSheet(\"" + uniqueName + "\")\n\n");
		s.append("	Active" + uniqueName + definition.IDFieldName + " = 0\n");
		if (definition.oHasPagination && DoesHaveOverview) {
			s.append("\n");
			s.append("	Dim ABMGenPagination" + uniqueName + " As ABMPagination = page.Component(\"ABMGenPagination" + uniqueName + "\")\n");		
			s.append("	LoadABMGenTable" + uniqueName + "(ABMGenPagination" + uniqueName + ".GetActivePage())\n");
		} else {
			s.append("\n");
			s.append("	LoadABMGenTable" + uniqueName + "(1)\n");
		}
		s.append("End Sub" + Mark  + "\n");
		s.append("\n");
		
		s.append("' the user clicked on cancel on the save form\n");
		s.append("Sub ABMGen" + uniqueName + "Cancel_Clicked(Target As String)\n");
		s.append("	Active" + uniqueName + definition.IDFieldName + " = 0\n");
		s.append("	page.CloseModalSheet(\"" + uniqueName + "\")\n");
		s.append("End Sub\n");
		s.append("\n");
		
		Map<String,String> don = new HashMap<String,String>();
		for (int i=0;i<definition.Validations.size();i++) {
			if (!definition.Validations.get(i).equals("")) {
				if (!don.containsKey(definition.Validations.get(i).toLowerCase())) {
					String varType="";
					switch (definition.Types.get(i)) {
					case ABMaterial.GEN_TEXT:
						varType="String";
						break;
					case ABMaterial.GEN_DOUBLE:
						varType="Double";
						break;
					case ABMaterial.GEN_INTEGER:
						varType="Int";
						break;
					case ABMaterial.GEN_TEXTAREA:
						varType="String";
						break;
					case ABMaterial.GEN_CHECKBOX:
						varType="Boolean";
						break;
					case ABMaterial.GEN_COMBOLIST:
						varType="String";
						break;
					case ABMaterial.GEN_COMBOSQL:
						varType="String";
						break;
					case ABMaterial.GEN_SWITCH:
						varType="Boolean";
						break;
					case ABMaterial.GEN_DATE_SCROLL:
						varType="Long";
						break;
					case ABMaterial.GEN_TIME_SCROLL:
						varType="Long";
						break;
					case ABMaterial.GEN_DATETIME_SCROLL:
						varType="Long";
						break;
					case ABMaterial.GEN_DATE_PICK:
						varType="Long";
						break;
					case ABMaterial.GEN_TIME_PICK:
						varType="Long";
						break;
					case ABMaterial.GEN_DATETIME_PICK:
						varType="Long";
						break;
					}	
					s.append("' method to check if a certain field is valid\n");
					s.append("Sub ABMGen" + definition.Validations.get(i) + "(value As " + varType + ") As Boolean\n");
					s.append("	' TODO by the programmer!\n");
					s.append("	Return true\n");
					s.append("End Sub\n");
					s.append("\n");
					don.put(definition.Validations.get(i).toLowerCase(), definition.Validations.get(i));
				}
			}
		}
		
		s.append(InnerGenerateMessageBox(dir, uniqueName + "Delete", definition.DeleteMessage, definition.YesButtonText, definition.NoButtonText, "", false));
		s.append("\n");
		
		s.append("' method you can call when the user wants to delete a record\n");
		s.append("Sub ABMGen" + uniqueName + "Delete(deleteId As int)\n");
		s.append("	Active" + uniqueName + definition.IDFieldName + " = deleteId\n");
		s.append("	page.ShowModalSheet(\"" + uniqueName + "Delete" + "\")\n");
		s.append("End Sub\n");
		s.append("\n");
		
		s.append("' the user clicked yes on the delete messagebox\n");
		s.append("Sub ABMGen" + uniqueName + "DeleteYes_Clicked(Target As String)" + Mark  + "\n");
		s.append("	Dim SQL as SQL = DBM.GetSQL\n");
		s.append("	DBM.ABMGenSQLDelete(SQL, " + BuildDELETE(definition) + ", Array as Int(Active" + uniqueName + definition.IDFieldName + "))" + Mark  + "\n");
		s.append("	DBM.CloseSQL(SQL)\n");
		s.append("	page.CloseModalSheet(\"" + uniqueName + "Delete\")\n");
		s.append("	Active" + uniqueName + definition.IDFieldName + " = 0\n");
		
		if (definition.oHasPagination && DoesHaveOverview) {
			s.append("\n");
			s.append("	Dim ABMGenPagination" + uniqueName + " As ABMPagination = page.Component(\"ABMGenPagination" + uniqueName + "\")\n");		
			s.append("	LoadABMGenTable" + uniqueName + "(ABMGenPagination" + uniqueName + ".GetActivePage())\n");
		} else {
			s.append("\n");
			s.append("	LoadABMGenTable" + uniqueName + "(1)\n");
		}
		
		s.append("End Sub" + Mark  + "\n");
		s.append("\n");

		s.append("Sub ABMGen" + uniqueName + "DeleteNo_Clicked(Target As String)\n");
		s.append("	page.CloseModalSheet(\"" + uniqueName + "Delete\")\n");
		s.append("End Sub\n");
		s.append("\n");
		
		s.append(InnerGenerateMessageBox(dir, uniqueName + "BadInput", definition.InvalidInputMessage, "", "", definition.CloseButtonText, false));
		s.append("\n");
		
		s.append("' the user clicked ok on the Bad Input messagebox\n");
		s.append("Sub ABMGen" + uniqueName + "BadInputCancel_Clicked(Target As String)\n");
		s.append("	page.CloseModalSheet(\"" + uniqueName + "BadInput\")\n");
		s.append("End Sub\n");
		s.append("\n");
		
		s.append("Sub ABMGenBuild" + uniqueName + "ComboItem(id As String, title as String) as ABMLabel 'ignore\n");
		s.append("	Dim ABMGenLabel As ABMLabel\n");
		s.append("	ABMGenLabel.Initialize(page, id, \"{NBSP}\" & title, ABM.SIZE_H6, True, \"\")\n");
		s.append("	ABMGenLabel.VerticalAlign = True\n");
		s.append("	return ABMGenLabel\n");
		s.append("End Sub\n");
		
		s.append("#End Region\n");
		s.append("\n");
		
		
		BufferedWriter writer = null;
        try {
        	Files.createDirectories(Paths.get(dir));        	
        	File buildFile = new File(dir, uniqueName + ".txt");
        	
            writer = new BufferedWriter(new FileWriter(buildFile));
            writer.write(s.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
        writer = null;
		
        s = new StringBuilder();
		s.append("--------------------------------------------------------------\n");
		s.append("' Add a module, called DBM\n");
		s.append("--------------------------------------------------------------\n");
		
		s.append("Sub Process_Globals\n");
		s.append("	Private pool As ConnectionPool\n");
		s.append("	Private SQLite As SQL\n");
		s.append("	Private DatabaseType As Int\n");
		s.append("	Public DBOK As Int = 0\n");
		s.append("	Public DBERROR As Int = -1\n");
		s.append("	Public DBNORESULTS As Int = -2\n");
		s.append("End Sub\n");
		s.append("\n");
		
		s.append("Sub InitializeSQLite(Dir As String, fileName As String, createIfNeeded As Boolean) 'ignore\n");
		s.append("	SQLite.InitializeSQLite(Dir, fileName, createIfNeeded)\n");
		s.append("	DatabaseType = 0\n");
		s.append("End Sub\n");
		s.append("\n");
		
		s.append("Sub InitializeMySQL(jdbcUrl As String ,login As String, password As String, poolSize As Int) 'ignore\n");
		s.append("	DatabaseType = 1\n");
		s.append("	Try\n");
		s.append("		pool.Initialize(\"com.mysql.jdbc.Driver\", jdbcUrl, login, password)\n");
		s.append("	Catch\n");
		s.append("		Log(\"Last Pool Init Except: \" & LastException.Message)\n");
		s.append("	End Try\n");
		s.append("\n");
		s.append("	' change pool size...\n");
		s.append("	Dim jo As JavaObject = pool\n");
		s.append("	jo.RunMethod(\"setMaxPoolSize\", Array(poolSize))\n");	
		s.append("End Sub\n");
		s.append("\n");
		
		s.append("Sub InitializeMSSQL(jdbcUrl As String ,login As String, password As String, poolSize As Int) 'ignore\n");
		s.append("	DatabaseType = 2\n");
		s.append("	Try\n");
		s.append("		pool.Initialize(\"net.sourceforge.jtds.jdbc.Driver\", jdbcUrl, login, password)\n");
		s.append("	Catch\n");
		s.append("		Log(\"Last Pool Init Except: \" & LastException.Message)\n");
		s.append("	End Try\n");
		s.append("\n");
		s.append("	' change pool size...\n");
		s.append("	Dim jo As JavaObject = pool\n");
		s.append("	jo.RunMethod(\"setMaxPoolSize\", Array(poolSize))\n");	
		s.append("End Sub\n");
		s.append("\n");
		
		s.append("Sub GetSQL() As SQL\n");
		s.append("	If DatabaseType = 0 Then\n");
		s.append("		Return SQLite\n");
		s.append("	Else\n");
		s.append("		Return pool.GetConnection\n");		
		s.append("	End If\n");	
		s.append("End Sub\n");
		s.append("\n");
		
		s.append("Sub CloseSQL(mySQL As SQL)\n");
		s.append("	If DatabaseType <> 0 Then\n");
		s.append("		mySQL.Close\n");
		s.append("	End If\n");	
		s.append("End Sub\n");
		s.append("\n");
		
		s.append("Sub ABMGenSQLSelect(SQL As SQL, Query As String, Args As List) As List\n");
		s.append("	Dim l As List\n");
		s.append("	l.Initialize\n");
		s.append("	Dim cur As ResultSet\n");
		s.append("	Try\n");
		s.append("		cur = SQL.ExecQuery2(Query, Args)\n");
		s.append("	Catch\n");
		s.append("		Log(LastException)\n");
		s.append("		Return l\n");
		s.append("	End Try\n");
		s.append("	Do While cur.NextRow\n");
		s.append("		Dim res As Map\n");
		s.append("		res.Initialize\n");
		s.append("		For i = 0 To cur.ColumnCount - 1\n");
		s.append("			res.Put(cur.GetColumnName(i).ToLowerCase, cur.GetString2(i))\n");
		s.append("		Next\n");
		s.append("		l.Add(res)\n");
		s.append("	Loop\n");
		s.append("	cur.Close\n");
		s.append("	Return l\n");
		s.append("End Sub\n");
		s.append("\n");
		
		s.append("Sub ABMGenSQLDelete(SQL As SQL, Query As String, Args as List) As Int\n");
		s.append("	Dim res As Int\n");
		s.append("	Try\n");
		s.append("		SQL.ExecNonQuery2(Query, Args)\n");
		s.append("		res = DBOK\n");
		s.append("	Catch\n");
		s.append("		Log(LastException)\n");
		s.append("		res = DBERROR\n");
		s.append("	End Try\n");
		s.append("	Return res\n");
		s.append("End Sub\n");
		s.append("\n");
		
		s.append("Sub ABMGenSQLInsert(SQL As SQL, Query As String, Args As List) As Int\n");
		s.append("	Dim res As Int\n");
		s.append("	Try\n");
		s.append("		Select Case DatabaseType\n");
		s.append("		Case 0\n");
		s.append("			SQL.ExecNonQuery2(Query, Args)\n");
		s.append("			res	= ABMGenSQLSelectSingleResult(SQL, \"SELECT last_insert_rowid()\", Null)\n");
		s.append("		Case 1\n");
		s.append("			SQL.ExecNonQuery2(Query, Args)\n");
		s.append("			res = ABMGenSQLSelectSingleResult(SQL, \"SELECT LAST_INSERT_ID()\", Null)\n");
		s.append("		Case 2\n");
		s.append("			Dim ABSQL As ABSQLExt\n");
		s.append("			res = ABSQL.ExecNonQuery3(SQL, Query, Args)\n");			
		s.append("		End Select\n");
		s.append("	Catch\n");
		s.append("		Log(LastException)\n");
		s.append("		res = DBERROR\n");
		s.append("	End Try\n");
		s.append("	Return res\n");
		s.append("End Sub\n");
		s.append("\n");
		
		s.append("Sub ABMGenSQLUpdate(SQL As SQL, Query As String, Args As List) As Int\n");
		s.append("	Dim res As Int\n");
		s.append("	Try\n");
		s.append("		SQL.ExecNonQuery2(Query, Args)\n");				
		s.append("		res = DBOK\n");
		s.append("	Catch\n");
		s.append("		Log(LastException)\n");		
		s.append("		res = DBERROR\n");
		s.append("	End Try\n");			
		s.append("	Return res\n");
		s.append("End Sub\n");
		s.append("\n");	
		
		s.append("Sub ABMGenSQLSelectSingleResult(SQL As SQL, Query As String, Args as List) As String\n");
		s.append("	Dim res As String\n");
		s.append("	Try\n");
		s.append("		res = SQL.ExecQuerySingleResult2(Query, Args)\n");		
		s.append("	Catch\n");
		s.append("		Log(LastException)\n");
		s.append("		res = DBERROR\n");
		s.append("	End Try\n");	
		s.append("	If res = Null Then\n");
		s.append("		res = DBNORESULTS\n");		
		s.append("	End If\n");
		s.append("	Return res\n");
		s.append("End Sub\n");
		
				
		try {
        	Files.createDirectories(Paths.get(dir));        	
        	File buildFile = new File(dir, "DBM.txt");
        	
            writer = new BufferedWriter(new FileWriter(buildFile));
            writer.write(s.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
        writer = null;        
	}
	
	protected String AddSpaces(String s, int w) {
		while (s.length()<w) {
			s+=" ";
		}
		return s;
	}
	
	protected int FindIDFieldColumn(ABMGeneratorCRUDDefinition definition) {
		for (int i=0;i<definition.FieldNames.size();i++) {
			if (definition.FieldNames.get(i).toLowerCase().equals(definition.IDFieldName.toLowerCase())) {
				return i;
			}
		}
		return -1;
	}
	
	protected List<Integer> CalcWidths(ABMGeneratorCRUDDefinition definition) {
		List<Integer> ret = new ArrayList<Integer>();
		for (int i=0;i<definition.FieldNames.size();i++) {
			int maxWidth=5; // length of false		
			if (definition.OverviewHeaderThemes.get(i).length()+2 > maxWidth) {
				maxWidth = definition.OverviewHeaderThemes.get(i).length()+2;
			}
			if (definition.OverviewThemes.get(i).length()+2 > maxWidth) {
				maxWidth = definition.OverviewThemes.get(i).length()+2;
			}
			if (definition.FieldNames.get(i).length()+2 > maxWidth) {
				maxWidth = definition.FieldNames.get(i).length()+2;
			}
			if (definition.Labels.get(i).length()+2 > maxWidth) {
				maxWidth = definition.Labels.get(i).length()+2;
			}
			ret.add(maxWidth);
		}
		if (definition.oHasEdit) {
			if (definition.EditColumnTitle.length()+2 > 7) {
				ret.add(definition.EditColumnTitle.length()+2);
			} else {
				ret.add(7);
			}
		}
		if (definition.oHasDelete) {
			if (definition.DeleteColumnTitle.length()+2 > 7) {
				ret.add(definition.DeleteColumnTitle.length()+2);
			} else {
				ret.add(7);
			}
		}
		return ret;
	}
	
	protected String BuildOverviewDataFields(ABMGeneratorCRUDDefinition definition, List<Integer> widths) {
		StringBuilder s = new StringBuilder();
		for (int i=0;i<definition.FieldNames.size();i++) {
			if (i>0) {
				s.append(",");
			}
			s.append(AddSpaces("\"" + definition.FieldNames.get(i) + "\"", widths.get(i)));
		}
		int i = definition.FieldNames.size();
		if (definition.oHasEdit) {
			s.append("," + AddSpaces("\"\"",widths.get(i)));
			i++;
		}
		if (definition.oHasDelete) {
			s.append("," + AddSpaces("\"\"", widths.get(i)));
		}
		return s.toString();
	}
	
	protected String BuildFilter(ABMGeneratorCRUDDefinition definition, String uniqueName) {
		StringBuilder s = new StringBuilder();
		int k=0;
		for (int i=0;i<definition.FieldNames.size();i++) {
			if (definition.OverviewSearches.get(i)) {
				if (k>0) {
					s.append(" OR ");
				}
				s.append(definition.FieldNames.get(i) + " LIKE '%\" & Filter" + uniqueName + " & \"%' ");
				k++;
			}
		}
		
		return s.toString();
	}
	
	protected String BuildSorts(ABMGeneratorCRUDDefinition definition) {
		StringBuilder s = new StringBuilder();
		int k=0;
		for (int i=0;i<definition.FieldNames.size();i++) {
			if (definition.OverviewSorts.get(i)) {
				if (k>0) {
					s.append(",");
				}
				s.append("\"" + definition.FieldNames.get(i) + "\"");
				k++;
			}
		}
		return s.toString();
	}
	
	protected String BuildOverviewHeaders(ABMGeneratorCRUDDefinition definition, List<Integer> widths) {
		StringBuilder s = new StringBuilder();
		for (int i=0;i<definition.Labels.size();i++) {
			if (i>0) {
				s.append(",");
			}
			s.append(AddSpaces("\"" + definition.Labels.get(i) + "\"", widths.get(i)));
		}
		int i = definition.FieldNames.size();
		if (definition.oHasEdit) {
			s.append("," + AddSpaces("\"" + definition.EditColumnTitle + "\"",widths.get(i)));
			i++;
		}
		if (definition.oHasDelete) {
			s.append("," + AddSpaces("\"" + definition.DeleteColumnTitle + "\"", widths.get(i)));
		}
		return s.toString();
	}
	
	protected String BuildOverviewHeaderThemes(ABMGeneratorCRUDDefinition definition, List<Integer> widths, String uniqueName) {
		StringBuilder s = new StringBuilder();
		for (int i=0;i<definition.OverviewHeaderThemes.size();i++) {
			if (i>0) {
				s.append(",");
			}
			s.append(AddSpaces("\"" + definition.OverviewHeaderThemes.get(i) + "\"", widths.get(i)));
		}
		int i = definition.OverviewHeaderThemes.size();
		if (definition.oHasEdit) {
			s.append("," + AddSpaces("\"\"", widths.get(i)));
			i++;
		}
		if (definition.oHasDelete) {
			s.append("," + AddSpaces("\"\"", widths.get(i)));
		}
		return s.toString();
	}
	
	protected String BuildOverviewHeaderHeights(ABMGeneratorCRUDDefinition definition, List<Integer> widths) {
		StringBuilder s = new StringBuilder();
		for (int i=0;i<definition.FieldNames.size();i++) {
			if (i>0) {
				s.append(",");
			}
			s.append(AddSpaces("" + definition.OverviewHeights.get(i), widths.get(i)));
		}
		int i = definition.FieldNames.size();
		if (definition.oHasEdit) {
			s.append("," + AddSpaces("0", widths.get(i)));
			i++;
		}
		if (definition.oHasDelete) {
			s.append("," + AddSpaces("0", widths.get(i)));
		}
		return s.toString();
	}
	
	protected String BuildOverviewColumnWidths(ABMGeneratorCRUDDefinition definition, List<Integer> widths) {
		StringBuilder s = new StringBuilder();
		for (int i=0;i<definition.FieldNames.size();i++) {
			if (i>0) {
				s.append(",");
			}
			s.append(AddSpaces("" + definition.OverviewWidths.get(i), widths.get(i)));
		}
		int i = definition.FieldNames.size();
		if (definition.oIsResponsive==false) {
			if (definition.oHasEdit) {
				s.append("," + AddSpaces("70", widths.get(i)));
				i++;
			}
			if (definition.oHasDelete) {
				s.append("," + AddSpaces("70", widths.get(i)));
			}
		} else {
			if (definition.oHasEdit) {
				s.append("," + AddSpaces("0", widths.get(i)));
				i++;
			}
			if (definition.oHasDelete) {
				s.append("," + AddSpaces("0", widths.get(i)));
			}
		}
		return s.toString();
	}
	
	protected String BuildOverviewVisibles(ABMGeneratorCRUDDefinition definition, List<Integer> widths) {
		StringBuilder s = new StringBuilder();
		for (int i=0;i<definition.OverviewVisibles.size();i++) {
			if (i>0) {
				s.append(",");
			}
			if (definition.OverviewVisibles.get(i)) {
				s.append(AddSpaces("True", widths.get(i)));
			} else {
				s.append(AddSpaces("False", widths.get(i)));
			}
		}
		int i = definition.OverviewVisibles.size();
		if (definition.oHasEdit) {
			s.append("," + AddSpaces("True", widths.get(i)));
			i++;
		}
		if (definition.oHasDelete) {
			s.append("," + AddSpaces("True", widths.get(i)));
		}
		return s.toString();
	}
	
	protected String BuildOverviewSortables(ABMGeneratorCRUDDefinition definition, List<Integer> widths) {
		StringBuilder s = new StringBuilder();
		for (int i=0;i<definition.OverviewSorts.size();i++) {
			if (i>0) {
				s.append(",");
			}
			if (definition.OverviewSorts.get(i)) {
				s.append(AddSpaces("True", widths.get(i)));
			} else {
				s.append(AddSpaces("False", widths.get(i)));
			}
		}
		int i = definition.OverviewSorts.size();
		if (definition.oHasEdit) {
			s.append("," + AddSpaces("False", widths.get(i)));
			i++;
		}
		if (definition.oHasDelete) {
			s.append("," + AddSpaces("False", widths.get(i)));
		}
		return s.toString();
	}
	
	protected String BuildDIMS(String uniqueName, ABMGeneratorCRUDDefinition definition, int j, String tabs) {
		StringBuilder s = new StringBuilder();
		String compName="ABMGen" + uniqueName + definition.FieldNames.get(j);
		
		switch (definition.Types.get(j)) {
		case ABMaterial.GEN_TEXT:
			if (definition.Rows.get(j)>0) {
				s.append(tabs + "Dim " + compName + " As ABMInput = ABMGen" + uniqueName + "Modal.Content.Component(\"" + compName + "\")\n");
			}
			break;
		case ABMaterial.GEN_DOUBLE:
			if (definition.Rows.get(j)>0) {
				s.append(tabs + "Dim " + compName + " As ABMInput = ABMGen" + uniqueName + "Modal.Content.Component(\"" + compName + "\")\n");
			}
			break;
		case ABMaterial.GEN_INTEGER:
			if (definition.Rows.get(j)>0) {
				s.append(tabs + "Dim " + compName + " As ABMInput = ABMGen" + uniqueName + "Modal.Content.Component(\"" + compName + "\")\n");
			}
			break;
		case ABMaterial.GEN_TEXTAREA:
			if (definition.Rows.get(j)>0) {
				s.append(tabs + "Dim " + compName + " As ABMInput = ABMGen" + uniqueName + "Modal.Content.Component(\"" + compName + "\")\n");
			}
			break;
		case ABMaterial.GEN_CHECKBOX:
			if (definition.Rows.get(j)>0) {
				s.append(tabs + "Dim " + compName + " As ABMCheckbox = ABMGen" + uniqueName + "Modal.Content.Component(\"" + compName + "\")\n");
			}
			break;
		case ABMaterial.GEN_COMBOLIST:
			if (definition.Rows.get(j)>0) {
				s.append(tabs + "Dim " + compName + " As ABMCombo = ABMGen" + uniqueName + "Modal.Content.Component(\"" + compName + "\")\n");
			}
			break;
		case ABMaterial.GEN_COMBOSQL:
			if (definition.Rows.get(j)>0) {
				s.append(tabs + "Dim " + compName + " As ABMCombo = ABMGen" + uniqueName + "Modal.Content.Component(\"" + compName + "\")\n");
			}
			break;
		case ABMaterial.GEN_SWITCH:
			if (definition.Rows.get(j)>0) {
				s.append(tabs + "Dim " + compName + " As ABMSwitch = ABMGen" + uniqueName + "Modal.Content.Component(\"" + compName + "\")\n");
			}
			break;	
		case ABMaterial.GEN_DATE_SCROLL:
			if (!definition.DateTimeScroller.SQLSaveDateTimeFormat.equals("")) {
				if (definition.Rows.get(j)>0) {
					s.append(tabs + "Dim " + compName + " As ABMDateTimeScroller = ABMGen" + uniqueName + "Modal.Content.Component(\"" + compName + "\")\n");
				}		
			} else {	
				if (definition.Rows.get(j)>0) {
					s.append(tabs + "Dim " + compName + " As ABMDateTimeScroller = ABMGen" + uniqueName + "Modal.Content.Component(\"" + compName + "\")\n");
				}
			}
			break;
		case ABMaterial.GEN_TIME_SCROLL:
			if (!definition.DateTimeScroller.SQLSaveDateTimeFormat.equals("")) {
				if (definition.Rows.get(j)>0) {
					s.append(tabs + "Dim " + compName + " As ABMDateTimeScroller = ABMGen" + uniqueName + "Modal.Content.Component(\"" + compName + "\")\n");
				}		
			} else {	
				if (definition.Rows.get(j)>0) {
					s.append(tabs + "Dim " + compName + " As ABMDateTimeScroller = ABMGen" + uniqueName + "Modal.Content.Component(\"" + compName + "\")\n");
				}
			}
			break;
		case ABMaterial.GEN_DATETIME_SCROLL:
			if (!definition.DateTimeScroller.SQLSaveDateTimeFormat.equals("")) {
				if (definition.Rows.get(j)>0) {
					s.append(tabs + "Dim " + compName + " As ABMDateTimeScroller = ABMGen" + uniqueName + "Modal.Content.Component(\"" + compName + "\")\n");
				}		
			} else {	
				if (definition.Rows.get(j)>0) {
					s.append(tabs + "Dim " + compName + " As ABMDateTimeScroller = ABMGen" + uniqueName + "Modal.Content.Component(\"" + compName + "\")\n");
				}
			}
			break;
		case ABMaterial.GEN_DATE_PICK:
			if (!definition.DateTimePicker.SQLSaveDateTimeFormat.equals("")) {
				if (definition.Rows.get(j)>0) {
					s.append(tabs + "Dim " + compName + " As ABMDateTimePicker = ABMGen" + uniqueName + "Modal.Content.Component(\"" + compName + "\")\n");
				}		
			} else {	
				if (definition.Rows.get(j)>0) {
					s.append(tabs + "Dim " + compName + " As ABMDateTimePicker = ABMGen" + uniqueName + "Modal.Content.Component(\"" + compName + "\")\n");
				}
			}
			break;
		case ABMaterial.GEN_TIME_PICK:
			if (!definition.DateTimePicker.SQLSaveDateTimeFormat.equals("")) {
				if (definition.Rows.get(j)>0) {
					s.append(tabs + "Dim " + compName + " As ABMDateTimePicker = ABMGen" + uniqueName + "Modal.Content.Component(\"" + compName + "\")\n");
				}		
			} else {	
				if (definition.Rows.get(j)>0) {
					s.append(tabs + "Dim " + compName + " As ABMDateTimePicker = ABMGen" + uniqueName + "Modal.Content.Component(\"" + compName + "\")\n");
				}
			}
			break;
		case ABMaterial.GEN_DATETIME_PICK:
			if (!definition.DateTimePicker.SQLSaveDateTimeFormat.equals("")) {
				if (definition.Rows.get(j)>0) {
					s.append(tabs + "Dim " + compName + " As ABMDateTimePicker = ABMGen" + uniqueName + "Modal.Content.Component(\"" + compName + "\")\n");
				}		
			} else {	
				if (definition.Rows.get(j)>0) {
					s.append(tabs + "Dim " + compName + " As ABMDateTimePicker = ABMGen" + uniqueName + "Modal.Content.Component(\"" + compName + "\")\n");
				}
			}
			break;
		}
		return s.toString();
	}
	
	protected String BuildGETS(String uniqueName, ABMGeneratorCRUDDefinition definition, int j) {
		StringBuilder s = new StringBuilder();
		String compName="ABMGen" + uniqueName + definition.FieldNames.get(j);
		
		switch (definition.Types.get(j)) {
		case ABMaterial.GEN_TEXT:
			if (definition.Rows.get(j)==0) {
				s.append("		valueString = \"" + definition.Defaults.get(j) + "\"\n");
			} else {
				s.append("		valueString = " + compName + ".Text\n");
			}
			if (!definition.Validations.get(j).equals("")) {
				s.append("		If ABMGen" + definition.Validations.get(j) + "(valueString) = false Then\n");
				s.append("			Dim ABMGen" + uniqueName + "BadInputMessage As ABMLabel = ABMGen" + uniqueName + "BadInput.Content.Component(\"ABMGen" + uniqueName + "BadInputMessage\")\n");
				s.append("			ABMGen" + uniqueName + "BadInputMessage.Text=\"" + definition.InvalidInputMessage + ": " + definition.Labels.get(j) + "\"\n");
				s.append("			page.ShowModalSheet(\"" + uniqueName + "BadInput" + "\")\n");
				s.append("			Return\n");
				s.append("		End If\n");
			}
			s.append("		Variables.Add(valueString)\n\n");
			break;
		case ABMaterial.GEN_DOUBLE:
			if (definition.Rows.get(j)==0) {
				s.append("		valueDouble = " + definition.Defaults.get(j) + "\n");
			} else {
				s.append("		valueDouble = " + compName + ".Text\n");
			}
			if (!definition.Validations.get(j).equals("")) {
				s.append("		If ABMGen" + definition.Validations.get(j) + "(valueDouble) = false Then\n");
				s.append("			Dim ABMGen" + uniqueName + "BadInputMessage As ABMLabel = ABMGen" + uniqueName + "BadInput.Content.Component(\"ABMGen" + uniqueName + "BadInputMessage\")\n");
				s.append("			ABMGen" + uniqueName + "BadInputMessage.Text=\"" + definition.InvalidInputMessage + ": " + definition.Labels.get(j) + "\"\n");
				s.append("			page.ShowModalSheet(\"" + uniqueName + "BadInput" + "\")\n");
				s.append("			Return\n");
				s.append("		End If\n");
			}
			s.append("		Variables.Add(valueDouble)\n\n");
			break;
		case ABMaterial.GEN_INTEGER:
			if (definition.Rows.get(j)==0) {
				s.append("		valueInt = " + definition.Defaults.get(j) + "\n");
			} else {
				s.append("		valueInt = " + compName + ".Text\n");
			}
			if (!definition.Validations.get(j).equals("")) {
				s.append("		If ABMGen" + definition.Validations.get(j) + "(valueInt) = false Then\n");
				s.append("			Dim ABMGen" + uniqueName + "BadInputMessage As ABMLabel = ABMGen" + uniqueName + "BadInput.Content.Component(\"ABMGen" + uniqueName + "BadInputMessage\")\n");
				s.append("			ABMGen" + uniqueName + "BadInputMessage.Text=\"" + definition.InvalidInputMessage + ": " + definition.Labels.get(j) + "\"\n");
				s.append("			page.ShowModalSheet(\"" + uniqueName + "BadInput" + "\")\n");
				s.append("			Return\n");
				s.append("		End If\n");
			}
			s.append("		Variables.Add(valueInt)\n\n");
			break;
		case ABMaterial.GEN_TEXTAREA:
			if (definition.Rows.get(j)==0) {
				s.append("		valueString = \"" + definition.Defaults.get(j) + "\"\n");
			} else {
				s.append("		valueString = " + compName + ".Text\n");
			}
			if (!definition.Validations.get(j).equals("")) {
				s.append("		If ABMGen" + definition.Validations.get(j) + "(valueString) = false Then\n");
				s.append("			Dim ABMGen" + uniqueName + "BadInputMessage As ABMLabel = ABMGen" + uniqueName + "BadInput.Content.Component(\"ABMGen" + uniqueName + "BadInputMessage\")\n");
				s.append("			ABMGen" + uniqueName + "BadInputMessage.Text=\"" + definition.InvalidInputMessage + ": " + definition.Labels.get(j) + "\"\n");
				s.append("			page.ShowModalSheet(\"" + uniqueName + "BadInput" + "\")\n");
				s.append("			Return\n");
				s.append("		End If\n");
			}
			s.append("		Variables.Add(valueString)\n\n");
			break;
		case ABMaterial.GEN_CHECKBOX:
			if (definition.Rows.get(j)==0) {
				s.append("		valueBoolean = " + definition.Defaults.get(j) + "\n");
			} else {
				s.append("		valueBoolean = " + compName + ".State\n");
			}
			if (!definition.Validations.get(j).equals("")) {
				s.append("		If ABMGen" + definition.Validations.get(j) + "(valueBoolean) = false Then\n");
				s.append("			Dim ABMGen" + uniqueName + "BadInputMessage As ABMLabel = ABMGen" + uniqueName + "BadInput.Content.Component(\"ABMGen" + uniqueName + "BadInputMessage\")\n");
				s.append("			ABMGen" + uniqueName + "BadInputMessage.Text=\"" + definition.InvalidInputMessage + ": " + definition.Labels.get(j) + "\"\n");
				s.append("			page.ShowModalSheet(\"" + uniqueName + "BadInput" + "\")\n");
				s.append("			Return\n");
				s.append("		End If\n");
			}
			s.append("		Variables.Add(valueBoolean)\n\n");
			break;			
		case ABMaterial.GEN_COMBOLIST:
			if (definition.Rows.get(j)==0) {
				s.append("		valueInt = " + definition.Defaults.get(j) + "\n");
			} else {
				s.append("		valueInt = " + compName + ".GetActiveItemId\n");
			}
			if (!definition.Validations.get(j).equals("")) {
				s.append("		If ABMGen" + definition.Validations.get(j) + "(valueInt) = false Then\n");
				s.append("			Dim ABMGen" + uniqueName + "BadInputMessage As ABMLabel = ABMGen" + uniqueName + "BadInput.Content.Component(\"ABMGen" + uniqueName + "BadInputMessage\")\n");
				s.append("			ABMGen" + uniqueName + "BadInputMessage.Text=\"" + definition.InvalidInputMessage + ": " + definition.Labels.get(j) + "\"\n");
				s.append("			page.ShowModalSheet(\"" + uniqueName + "BadInput" + "\")\n");
				s.append("			Return\n");
				s.append("		End If\n");
			}
			s.append("		Variables.Add(valueInt)\n\n");
			break;
		case ABMaterial.GEN_COMBOSQL:
			if (definition.Rows.get(j)==0) {
				s.append("		valueInt = " + definition.Defaults.get(j) + "\n");
			} else {
				s.append("		valueInt = " + compName + ".GetActiveItemId\n");
			}
			if (!definition.Validations.get(j).equals("")) {
				s.append("		If ABMGen" + definition.Validations.get(j) + "(valueInt) = false Then\n");
				s.append("			Dim ABMGen" + uniqueName + "BadInputMessage As ABMLabel = ABMGen" + uniqueName + "BadInput.Content.Component(\"ABMGen" + uniqueName + "BadInputMessage\")\n");
				s.append("			ABMGen" + uniqueName + "BadInputMessage.Text=\"" + definition.InvalidInputMessage + ": " + definition.Labels.get(j) + "\"\n");
				s.append("			page.ShowModalSheet(\"" + uniqueName + "BadInput" + "\")\n");
				s.append("			Return\n");
				s.append("		End If\n");
			}
			s.append("		Variables.Add(valueInt)\n\n");
			break;
		case ABMaterial.GEN_SWITCH:
			if (definition.Rows.get(j)==0) {
				s.append("		valueBoolean = " + definition.Defaults.get(j) + "\n");
			} else {
				s.append("		valueBoolean = " + compName + ".State\n");
			}
			if (!definition.Validations.get(j).equals("")) {
				s.append("		If ABMGen" + definition.Validations.get(j) + "(valueBoolean) = false Then\n");
				s.append("			Dim ABMGen" + uniqueName + "BadInputMessage As ABMLabel = ABMGen" + uniqueName + "BadInput.Content.Component(\"ABMGen" + uniqueName + "BadInputMessage\")\n");
				s.append("			ABMGen" + uniqueName + "BadInputMessage.Text=\"" + definition.InvalidInputMessage + ": " + definition.Labels.get(j) + "\"\n");
				s.append("			page.ShowModalSheet(\"" + uniqueName + "BadInput" + "\")\n");
				s.append("			Return\n");
				s.append("		End If\n");
			}
			s.append("		Variables.Add(valueBoolean)\n\n");
			break;	
		case ABMaterial.GEN_DATE_SCROLL:			
		case ABMaterial.GEN_TIME_SCROLL:			
		case ABMaterial.GEN_DATETIME_SCROLL:
			if (definition.Rows.get(j)==0) {
				if (definition.Defaults.get(j).toLowerCase().equals("now")) {
					s.append("		valueLong = NewDate\n");
				} else {
					s.append("		valueLong = definition.Defaults.get(j)\n");
				}
			} else {
				s.append("		valueLong = " + compName + ".GetDate\n");
			}
			if (!definition.Validations.get(j).equals("")) {
				s.append("		If ABMGen" + definition.Validations.get(j) + "(valueLong) = false Then\n");
				s.append("			Dim ABMGen" + uniqueName + "BadInputMessage As ABMLabel = ABMGen" + uniqueName + "BadInput.Content.Component(\"ABMGen" + uniqueName + "BadInputMessage\")\n");
				s.append("			ABMGen" + uniqueName + "BadInputMessage.Text=\"" + definition.InvalidInputMessage + ": " + definition.Labels.get(j) + "\"\n");
				s.append("			page.ShowModalSheet(\"" + uniqueName + "BadInput" + "\")\n");
				s.append("			Return\n");
				s.append("		End If\n");
			}
			
			if (!definition.DateTimeScroller.SQLSaveDateTimeFormat.equals("")) {
				s.append("		Variables.Add(ABM.ConvertToDateTimeString(valueLong, \"" + definition.DateTimeScroller.SQLSaveDateTimeFormat + "\"))\n\n");						
			} else {	
				s.append("		Variables.Add(valueLong)\n\n");				
			}
			break;
		case ABMaterial.GEN_DATE_PICK:			
		case ABMaterial.GEN_TIME_PICK:			
		case ABMaterial.GEN_DATETIME_PICK:
			if (definition.Rows.get(j)==0) {
				if (definition.Defaults.get(j).toLowerCase().equals("now")) {
					s.append("		valueLong = NewDate\n");
				} else {
					s.append("		valueLong = definition.Defaults.get(j)\n");
				}
			} else {
				s.append("		valueLong = " + compName + ".GetDate\n");
			}
			if (!definition.Validations.get(j).equals("")) {
				s.append("		If ABMGen" + definition.Validations.get(j) + "(valueLong) = false Then\n");
				s.append("			Dim ABMGen" + uniqueName + "BadInputMessage As ABMLabel = ABMGen" + uniqueName + "BadInput.Content.Component(\"ABMGen" + uniqueName + "BadInputMessage\")\n");
				s.append("			ABMGen" + uniqueName + "BadInputMessage.Text=\"" + definition.InvalidInputMessage + ": " + definition.Labels.get(j) + "\"\n");
				s.append("			page.ShowModalSheet(\"" + uniqueName + "BadInput" + "\")\n");
				s.append("			Return\n");
				s.append("		End If\n");
			}
			
			if (!definition.DateTimePicker.SQLSaveDateTimeFormat.equals("")) {
				s.append("		Variables.Add(ABM.ConvertToDateTimeString(valueLong, \"" + definition.DateTimePicker.SQLSaveDateTimeFormat + "\"))\n\n");						
			} else {	
				s.append("		Variables.Add(valueLong)\n\n");				
			}
			break;
		}
		return s.toString();
	}	
	
	protected String BuildSETSEdit(String uniqueName, ABMGeneratorCRUDDefinition definition, int j) {
		StringBuilder s = new StringBuilder();
		String compName="ABMGen" + uniqueName + definition.FieldNames.get(j);
		
		switch (definition.Types.get(j)) {
		case ABMaterial.GEN_TEXT:
			s.append("		If m.GetDefault(\"" + definition.FieldNames.get(j).toLowerCase() + "\", null) = null Then \n");
			s.append("			" + compName + ".Text = \"" + definition.Defaults.get(j) + "\"\n");
			s.append("		Else\n");
			s.append("			" + compName + ".Text = m.Get(\"" + definition.FieldNames.get(j).toLowerCase() + "\") \n");
			s.append("		End If\n\n");
			break;
		case ABMaterial.GEN_DOUBLE:
			s.append("		If m.GetDefault(\"" + definition.FieldNames.get(j).toLowerCase() + "\", null) = null Then \n");
			s.append("			" + compName + ".Text = \"" + definition.Defaults.get(j) + "\"\n");
			s.append("		Else\n");
			s.append("			" + compName + ".Text = m.Get(\"" + definition.FieldNames.get(j).toLowerCase() + "\")\n");
			s.append("		End If\n\n");
			break;
		case ABMaterial.GEN_INTEGER:
			s.append("		If m.GetDefault(\"" + definition.FieldNames.get(j).toLowerCase() + "\", null) = null Then \n");
			s.append("			" + compName + ".Text = \"" + definition.Defaults.get(j) + "\"\n");
			s.append("		Else\n");
			s.append("			" + compName + ".Text = m.Get(\"" + definition.FieldNames.get(j).toLowerCase() + "\")\n");
			s.append("		End If\n\n");
			break;
		case ABMaterial.GEN_TEXTAREA:
			s.append("		If m.GetDefault(\"" + definition.FieldNames.get(j).toLowerCase() + "\", null) = null Then \n");
			s.append("			" + compName + ".Text = \"" + definition.Defaults.get(j) + "\"\n");
			s.append("		Else\n");
			s.append("			" + compName + ".Text = m.Get(\"" + definition.FieldNames.get(j).toLowerCase() + "\") \n");
			s.append("		End If\n\n");
			break;
		case ABMaterial.GEN_CHECKBOX:
			s.append("		If m.GetDefault(\"" + definition.FieldNames.get(j).toLowerCase() + "\", null) = null Then \n");
			s.append("			" + compName + ".State = " + definition.Defaults.get(j).toLowerCase().equals("true") + "\n");
			s.append("		Else\n");
			s.append("			" + compName + ".State = m.Get(\"" + definition.FieldNames.get(j).toLowerCase() + "\")\n");
			s.append("		End If\n\n");
			break;
		case ABMaterial.GEN_COMBOLIST:
			s.append("		If m.GetDefault(\"" + definition.FieldNames.get(j).toLowerCase() + "\", null) = null Then \n");
			s.append("			" + compName + ".SetActiveItemId(\"" + definition.Defaults.get(j) + "\")\n");
			s.append("		Else\n");
			s.append("			" + compName + ".SetActiveItemId(m.Get(\"" + definition.FieldNames.get(j).toLowerCase() + "\"))\n");
			s.append("		End If\n\n");
			break;
		case ABMaterial.GEN_COMBOSQL:
			s.append("		If m.GetDefault(\"" + definition.FieldNames.get(j).toLowerCase() + "\", null) = null Then \n");
			s.append("			" + compName + ".SetActiveItemId(\"" + definition.Defaults.get(j) + "\")\n");
			s.append("		Else\n");
			s.append("			" + compName + ".SetActiveItemId(m.Get(\"" + definition.FieldNames.get(j).toLowerCase() + "\"))\n");
			s.append("		End If\n\n");
			break;
		case ABMaterial.GEN_SWITCH:
			s.append("		If m.GetDefault(\"" + definition.FieldNames.get(j).toLowerCase() + "\", null) = null Then \n");
			s.append("			" + compName + ".State = " + definition.Defaults.get(j).toLowerCase().equals("true") + "\n");
			s.append("		Else\n");
			s.append("			" + compName + ".State = m.Get(\"" + definition.FieldNames.get(j).toLowerCase() + "\")\n");
			s.append("		End If\n\n");
			break;	
		case ABMaterial.GEN_DATE_SCROLL:
			s.append("		If m.GetDefault(\"" + definition.FieldNames.get(j).toLowerCase() + "\", null) = null Then \n");
			if (!definition.Defaults.get(j).toLowerCase().equals("now")) {
				if (!definition.DateTimeScroller.SQLSaveDateTimeFormat.equals("")) {
					s.append("			" + compName + ".SetDate(ABM.ConvertFromDateTimeString(" + definition.Defaults.get(j) + ", \"" + definition.DateTimeScroller.SQLSaveDateTimeFormat + "\"))\n");
				} else {
					s.append("			" + compName + ".SetDate(" + definition.Defaults.get(j) + ")\n");
				}
			} else {
				s.append("			" + compName + ".SetDate(NewDate)\n");
			}				
			s.append("		Else\n");
			if (!definition.DateTimeScroller.SQLSaveDateTimeFormat.equals("")) {
				s.append("			" + compName + ".SetDate(ABM.ConvertFromDateTimeString(m.Get(\"" + definition.FieldNames.get(j).toLowerCase() + "\"), \"" + definition.DateTimeScroller.SQLSaveDateTimeFormat + "\"))\n");
			} else {
				s.append("			" + compName + ".SetDate(m.Get(\"" + definition.FieldNames.get(j).toLowerCase() + "\"))\n");
			}
			s.append("		End If\n\n");
			break;	
		case ABMaterial.GEN_TIME_SCROLL:
			s.append("		If m.GetDefault(\"" + definition.FieldNames.get(j).toLowerCase() + "\", null) = null Then \n");
			if (!definition.Defaults.get(j).toLowerCase().equals("now")) {
				if (!definition.DateTimeScroller.SQLSaveDateTimeFormat.equals("")) {
					s.append("			" + compName + ".SetDate(ABM.ConvertFromDateTimeString(" + definition.Defaults.get(j) + ", \"" + definition.DateTimeScroller.SQLSaveDateTimeFormat + "\"))\n");
				} else {
					s.append("			" + compName + ".SetDate(" + definition.Defaults.get(j) + ")\n");
				}
			} else {
				s.append("			" + compName + ".SetDate(NewDate)\n");
			}
			s.append("		Else\n");
			
			if (!definition.DateTimeScroller.SQLSaveDateTimeFormat.equals("")) {
				s.append("			" + compName + ".SetDate(ABM.ConvertFromDateTimeString(m.Get(\"" + definition.FieldNames.get(j).toLowerCase() + "\"), \"" + definition.DateTimeScroller.SQLSaveDateTimeFormat + "\"))\n");
			} else {
				s.append("			" + compName + ".SetDate(m.Get(\"" + definition.FieldNames.get(j).toLowerCase() + "\"))\n");
			}
			s.append("		End If\n\n");
			break;
		case ABMaterial.GEN_DATETIME_SCROLL:
			s.append("		If m.GetDefault(\"" + definition.FieldNames.get(j).toLowerCase() + "\", null) = null Then \n");
			if (!definition.Defaults.get(j).toLowerCase().equals("now")) {
				if (!definition.DateTimeScroller.SQLSaveDateTimeFormat.equals("")) {
					s.append("			" + compName + ".SetDate(ABM.ConvertFromDateTimeString(" + definition.Defaults.get(j) + ", \"" + definition.DateTimeScroller.SQLSaveDateTimeFormat + "\"))\n");
				} else {
					s.append("			" + compName + ".SetDate(" + definition.Defaults.get(j) + ")\n");
				}
			} else {
				s.append("			" + compName + ".SetDate(NewDate)\n");
			}
			s.append("		Else\n");
			
			if (!definition.DateTimeScroller.SQLSaveDateTimeFormat.equals("")) {
				s.append("			" + compName + ".SetDate(ABM.ConvertFromDateTimeString(m.Get(\"" + definition.FieldNames.get(j).toLowerCase() + "\"), \"" + definition.DateTimeScroller.SQLSaveDateTimeFormat + "\"))\n");
			} else {
				s.append("			" + compName + ".SetDate(m.Get(\"" + definition.FieldNames.get(j).toLowerCase() + "\"))\n");
			}
			s.append("		End If\n\n");
			break;	
		case ABMaterial.GEN_DATE_PICK:
			s.append("		If m.GetDefault(\"" + definition.FieldNames.get(j).toLowerCase() + "\", null) = null Then \n");
			if (!definition.Defaults.get(j).toLowerCase().equals("now")) {
				if (!definition.DateTimePicker.SQLSaveDateTimeFormat.equals("")) {
					s.append("			" + compName + ".SetDate(ABM.ConvertFromDateTimeString(" + definition.Defaults.get(j) + ", \"" + definition.DateTimePicker.SQLSaveDateTimeFormat + "\"))\n");
				} else {
					s.append("			" + compName + ".SetDate(" + definition.Defaults.get(j) + ")\n");
				}
			} else {
				s.append("			" + compName + ".SetDate(NewDate)\n");
			}
			s.append("		Else\n");
			
			if (!definition.DateTimePicker.SQLSaveDateTimeFormat.equals("")) {
				s.append("			" + compName + ".SetDate(ABM.ConvertFromDateTimeString(m.Get(\"" + definition.FieldNames.get(j).toLowerCase() + "\"), \"" + definition.DateTimePicker.SQLSaveDateTimeFormat + "\"))\n");
			} else {
				s.append("			" + compName + ".SetDate(m.Get(\"" + definition.FieldNames.get(j).toLowerCase() + "\"))\n");
			}
			s.append("		End If\n\n");
			break;
		case ABMaterial.GEN_TIME_PICK:
			s.append("		If m.GetDefault(\"" + definition.FieldNames.get(j).toLowerCase() + "\", null) = null Then \n");
			if (!definition.Defaults.get(j).toLowerCase().equals("now")) {
				if (!definition.DateTimePicker.SQLSaveDateTimeFormat.equals("")) {
					s.append("			" + compName + ".SetDate(ABM.ConvertFromDateTimeString(" + definition.Defaults.get(j) + ", \"" + definition.DateTimePicker.SQLSaveDateTimeFormat + "\"))\n");
				} else {
					s.append("			" + compName + ".SetDate(" + definition.Defaults.get(j) + ")\n");
				}
			} else {
				s.append("			" + compName + ".SetDate(NewDate)\n");
			}
			s.append("		Else\n");
			
			if (!definition.DateTimePicker.SQLSaveDateTimeFormat.equals("")) {
				s.append("			" + compName + ".SetDate(ABM.ConvertFromDateTimeString(m.Get(\"" + definition.FieldNames.get(j).toLowerCase() + "\"), \"" + definition.DateTimePicker.SQLSaveDateTimeFormat + "\"))\n");
			} else {
				s.append("			" + compName + ".SetDate(m.Get(\"" + definition.FieldNames.get(j).toLowerCase() + "\"))\n");
			}
			s.append("		End If\n\n");
			break;
		case ABMaterial.GEN_DATETIME_PICK:
			s.append("		If m.GetDefault(\"" + definition.FieldNames.get(j).toLowerCase() + "\", null) = null Then \n");
			if (!definition.Defaults.get(j).toLowerCase().equals("now")) {
				if (!definition.DateTimePicker.SQLSaveDateTimeFormat.equals("")) {
					s.append("			" + compName + ".SetDate(ABM.ConvertFromDateTimeString(" + definition.Defaults.get(j) + ", \"" + definition.DateTimePicker.SQLSaveDateTimeFormat + "\"))\n");
				} else {
					s.append("			" + compName + ".SetDate(" + definition.Defaults.get(j) + ")\n");
				}
			} else {
				s.append("			" + compName + ".SetDate(NewDate)\n");
			}
			s.append("		Else\n");
			
			if (!definition.DateTimePicker.SQLSaveDateTimeFormat.equals("")) {
				s.append("			" + compName + ".SetDate(ABM.ConvertFromDateTimeString(m.Get(\"" + definition.FieldNames.get(j).toLowerCase() + "\"), \"" + definition.DateTimePicker.SQLSaveDateTimeFormat + "\"))\n");
			} else {
				s.append("			" + compName + ".SetDate(m.Get(\"" + definition.FieldNames.get(j).toLowerCase() + "\"))\n");
			}
			s.append("		End If\n\n");
			break;
		}
		return s.toString();
	}
	
	protected String BuildSETSNew(String uniqueName, ABMGeneratorCRUDDefinition definition, int j) {
		StringBuilder s = new StringBuilder();
		
		String compName="ABMGen" + uniqueName + definition.FieldNames.get(j);
		
		switch (definition.Types.get(j)) {
		case ABMaterial.GEN_TEXT:
			s.append("	" + compName + ".Text = \"" + definition.Defaults.get(j) + "\"\n");
			break;
		case ABMaterial.GEN_DOUBLE:
			s.append("	" + compName + ".Text = \"" + definition.Defaults.get(j) + "\"\n");
			break;
		case ABMaterial.GEN_INTEGER:
			s.append("	" + compName + ".Text = \"" + definition.Defaults.get(j) + "\"\n");
			break;
		case ABMaterial.GEN_TEXTAREA:
			s.append("	" + compName + ".Text = \"" + definition.Defaults.get(j) + "\"\n");
			break;
		case ABMaterial.GEN_CHECKBOX:
			s.append("	" + compName + ".State = " + definition.Defaults.get(j).toLowerCase().equals("true") + "\n");
			break;
		case ABMaterial.GEN_COMBOLIST:
			s.append("	" + compName + ".SetActiveItemId(\"" + definition.Defaults.get(j) + "\")\n");
			break;
		case ABMaterial.GEN_COMBOSQL:
			s.append("	" + compName + ".SetActiveItemId(\"" + definition.Defaults.get(j) + "\")\n");
			break;
		case ABMaterial.GEN_SWITCH:
			s.append("	" + compName + ".State = " + definition.Defaults.get(j).toLowerCase().equals("true") + "\n");
			break;	
		case ABMaterial.GEN_DATE_SCROLL:
			if (!definition.Defaults.get(j).toLowerCase().equals("now")) {
				if (!definition.DateTimeScroller.SQLSaveDateTimeFormat.equals("")) {
					s.append("	" + compName + ".SetDate(ABM.ConvertFromDateTimeString(" + definition.Defaults.get(j) + ", \"" + definition.DateTimeScroller.SQLSaveDateTimeFormat + "\"))\n");
				} else {
					s.append("	" + compName + ".SetDate(ABM.ConvertFromDateTimeString(" + definition.Defaults.get(j) + ", \"" + definition.DateTimeScroller.SQLSaveDateTimeFormat + "\"))\n");
				}
			} else {
				s.append("	" + compName + ".SetDate(NewDate)\n");
			}
			break;	
		case ABMaterial.GEN_TIME_SCROLL:
			if (!definition.Defaults.get(j).toLowerCase().equals("now")) {
				if (!definition.DateTimeScroller.SQLSaveDateTimeFormat.equals("")) {
					s.append("	" + compName + ".SetDate(ABM.ConvertFromDateTimeString(" + definition.Defaults.get(j) + ", \"" + definition.DateTimeScroller.SQLSaveDateTimeFormat + "\"))\n");
				} else {
					s.append("	" + compName + ".SetDate(ABM.ConvertFromDateTimeString(" + definition.Defaults.get(j) + ", \"" + definition.DateTimeScroller.SQLSaveDateTimeFormat + "\"))\n");
				}
			} else {
				s.append("	" + compName + ".SetDate(NewDate)\n");
			}
			break;
		case ABMaterial.GEN_DATETIME_SCROLL:
			if (!definition.Defaults.get(j).toLowerCase().equals("now")) {
				if (!definition.DateTimeScroller.SQLSaveDateTimeFormat.equals("")) {
					s.append("	" + compName + ".SetDate(ABM.ConvertFromDateTimeString(" + definition.Defaults.get(j) + ", \"" + definition.DateTimeScroller.SQLSaveDateTimeFormat + "\"))\n");
				} else {
					s.append("	" + compName + ".SetDate(ABM.ConvertFromDateTimeString(" + definition.Defaults.get(j) + ", \"" + definition.DateTimeScroller.SQLSaveDateTimeFormat + "\"))\n");
				}
			} else {
				s.append("	" + compName + ".SetDate(NewDate)\n");
			}
			break;
		case ABMaterial.GEN_DATE_PICK:
			if (!definition.Defaults.get(j).toLowerCase().equals("now")) {
				if (!definition.DateTimePicker.SQLSaveDateTimeFormat.equals("")) {
					s.append("	" + compName + ".SetDate(ABM.ConvertFromDateTimeString(" + definition.Defaults.get(j) + ", \"" + definition.DateTimePicker.SQLSaveDateTimeFormat + "\"))\n");
				} else {
					s.append("	" + compName + ".SetDate(ABM.ConvertFromDateTimeString(" + definition.Defaults.get(j) + ", \"" + definition.DateTimePicker.SQLSaveDateTimeFormat + "\"))\n");
				}
			} else {
				s.append("	" + compName + ".SetDate(NewDate)\n");
			}
			break;
		case ABMaterial.GEN_TIME_PICK:
			if (!definition.Defaults.get(j).toLowerCase().equals("now")) {
				if (!definition.DateTimePicker.SQLSaveDateTimeFormat.equals("")) {
					s.append("	" + compName + ".SetDate(ABM.ConvertFromDateTimeString(" + definition.Defaults.get(j) + ", \"" + definition.DateTimePicker.SQLSaveDateTimeFormat + "\"))\n");
				} else {
					s.append("	" + compName + ".SetDate(ABM.ConvertFromDateTimeString(" + definition.Defaults.get(j) + ", \"" + definition.DateTimePicker.SQLSaveDateTimeFormat + "\"))\n");
				}
			} else {
				s.append("	" + compName + ".SetDate(NewDate)\n");
			}
			break;
		case ABMaterial.GEN_DATETIME_PICK:
			if (!definition.Defaults.get(j).toLowerCase().equals("now")) {
				if (!definition.DateTimePicker.SQLSaveDateTimeFormat.equals("")) {
					s.append("	" + compName + ".SetDate(ABM.ConvertFromDateTimeString(" + definition.Defaults.get(j) + ", \"" + definition.DateTimePicker.SQLSaveDateTimeFormat + "\"))\n");
				} else {
					s.append("	" + compName + ".SetDate(ABM.ConvertFromDateTimeString(" + definition.Defaults.get(j) + ", \"" + definition.DateTimePicker.SQLSaveDateTimeFormat + "\"))\n");
				}
			} else {
				s.append("	" + compName + ".SetDate(NewDate)\n");
			}
			break;
		}
		
		return s.toString();
	}
	
	/*
	 * Will generate a file with B4J source code you can cut and paste
	 */
	public void GenerateMessageBox(String dir, String uniqueName, String Message, String YesButton, String NoButton, String CancelButton) {
		InnerGenerateMessageBox(dir, uniqueName, Message, YesButton, NoButton, CancelButton, true);
	}
	
	protected String InnerGenerateMessageBox(String dir, String uniqueName, String Message, String YesButton, String NoButton, String CancelButton, boolean doWrite) {
		StringBuilder s = new StringBuilder();
		
		if (doWrite) {
			s.append("--------------------------------------------------------------\n");
			s.append("' Add in BuildPage()\n");
			s.append("--------------------------------------------------------------\n");
			s.append("page.AddModalSheetTemplate(ABMGenBuild" + uniqueName + ")\n");
			s.append("\n");
			s.append("--------------------------------------------------------------\n");
			s.append("' Add in the class\n");
			s.append("--------------------------------------------------------------\n");
			s.append("#Region " + uniqueName + "\n");
		}
		s.append("Sub ABMGenBuild" + uniqueName + "() As ABMModalSheet\n");
		s.append("	Dim ABMGen" + uniqueName + "Modal As ABMModalSheet\n");
		s.append("	ABMGen" + uniqueName + "Modal.Initialize(page, \"" + uniqueName + "\", False, False, \"\")\n");
		s.append("	ABMGen" + uniqueName + "Modal.IsDismissible = False\n");
		s.append("\n");
		s.append("	' Build the content grid \n");
		s.append("	ABMGen" + uniqueName + "Modal.Content.AddRowsM(1, True,0,0, \"\").AddCells12(1, \"\")\n");
		s.append("	ABMGen" + uniqueName + "Modal.Content.BuildGrid  'IMPORTANT once you loaded the complete grid AND before you start adding components\n");
		s.append("\n");
		s.append("	' add message label\n");
		s.append("	Dim ABMGen" + uniqueName + "Message As ABMLabel\n");	
		s.append("	ABMGen" + uniqueName + "Message.Initialize(page, \"ABMGen" + uniqueName + "Message\", \"" + Message + "\" , ABM.SIZE_PARAGRAPH, False, \"\")\n");
		s.append("	ABMGen" + uniqueName + "Modal.Content.CellR(0,1).AddComponent(ABMGen" + uniqueName + "Message)\n");
		s.append("\n");
		s.append("	' Build the footer grid \n");
		s.append("	ABMGen" + uniqueName + "Modal.Footer.AddRowsM(1,True,0,0, \"\").AddCellsOS(1,6,6,6,3,3,3,\"\").AddCellsOS(1,0,0,0,3,3,3, \"\")\n");
		s.append("	ABMGen" + uniqueName + "Modal.Footer.BuildGrid 'IMPORTANT once you loaded the complete grid AND before you start adding components\n");
		s.append("\n");
		s.append("	' create the buttons for the footer\n");
		if (!YesButton.equals("")) {
			s.append("	Dim ABMGen" + uniqueName + "Yes As ABMButton\n");
			s.append("	ABMGen" + uniqueName + "Yes.InitializeFlat(page, \"ABMGen" + uniqueName + "Yes\", \"\", \"\", \"" + YesButton + "\", \"transparent\")\n");
			s.append("	ABMGen" + uniqueName + "Modal.Footer.Cell(1,1).AddComponent(ABMGen" + uniqueName + "Yes)\n");
			s.append("\n");
		}
		if (!NoButton.equals("")) {
			s.append("	Dim ABMGen" + uniqueName + "No As ABMButton\n");
			s.append("	ABMGen" + uniqueName + "No.InitializeFlat(page, \"ABMGen" + uniqueName + "No\", \"\", \"\", \"" + NoButton + "\", \"transparent\")\n");
			s.append("	ABMGen" + uniqueName + "Modal.Footer.Cell(1,2).AddComponent(ABMGen" + uniqueName + "No)\n");
			s.append("\n");
		}
		if (!CancelButton.equals("")) {
			s.append("	Dim ABMGen" + uniqueName + "Cancel As ABMButton\n");
			s.append("	ABMGen" + uniqueName + "Cancel.InitializeFlat(page, \"ABMGen" + uniqueName + "Cancel\", \"\", \"\", \"" + CancelButton + "\", \"transparent\")\n");
			s.append("	ABMGen" + uniqueName + "Modal.Footer.Cell(1,2).AddComponent(ABMGen" + uniqueName + "Cancel)\n");
			s.append("\n");
		}
		s.append("	Return ABMGen" + uniqueName + "Modal\n");
		s.append("End Sub\n");
		if (doWrite) {
			if (!YesButton.equals("")) {
				s.append("\n");
				s.append("Sub ABMGen" + uniqueName + "Yes_Clicked(Target As String)\n");
				s.append("\n");
				s.append("	page.CloseModalSheet(\"" + uniqueName + "\")\n");
				s.append("End Sub\n");
			}
		
			if (!NoButton.equals("")) {
				s.append("\n");
				s.append("Sub ABMGen" + uniqueName + "No_Clicked(Target As String)\n");
				s.append("\n");
				s.append("	page.CloseModalSheet(\"" + uniqueName + "\")\n");
				s.append("End Sub\n");
			}
		
			if (!CancelButton.equals("")) {
				s.append("\n");
				s.append("Sub ABMGen" + uniqueName + "Cancel_Clicked(Target As String)\n");
				s.append("	page.CloseModalSheet(\"" + uniqueName + "\")\n");
				s.append("End Sub\n");
			}	
		
		
			s.append("#End Region\n");
			s.append("\n");
			s.append("--------------------------------------------------------------\n");
			s.append("' calling the message box in your code\n");
			s.append("--------------------------------------------------------------\n");
			s.append("Dim ABMGen" + uniqueName + "Modal As ABMModalSheet = page.ModalSheet(\"" + uniqueName + "\")\n");
			s.append("Dim ABMGen" + uniqueName + "Message As ABMLabel = ABMGen" + uniqueName + "Modal.Content.Component(\"ABMGen" + uniqueName + "Message\")\n");
			s.append("ABMGen" + uniqueName + "Message.Text = \"PUTHEREYOURMESSAGE\"\n");
			s.append("page.ShowModalSheet(\"" + uniqueName + "\")\n");
			s.append("--------------------------------------------------------------\n");
		
			BufferedWriter writer = null;
			try {
				Files.createDirectories(Paths.get(dir));        	
				File buildFile = new File(dir, uniqueName + ".txt");
        	
				writer = new BufferedWriter(new FileWriter(buildFile));
				writer.write(s.toString());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					writer.close();
				} catch (Exception e) {
				}
			}
			writer = null;
		}
		return s.toString();
	}
	
	protected String BuildSELECT(ABMGeneratorCRUDDefinition d) {
		if (!d.GenerateQueries) {
			return "TODO";
		}
		StringBuilder s = new StringBuilder();
		s.append("\"SELECT ");
		for (int i=0;i<d.FieldNames.size();i++) {
			if (i>0) s.append(", ");
			s.append(d.FieldNames.get(i));
			
		}
		s.append(" FROM " + d.TableName);
		s.append(" WHERE " + d.IDFieldName + " = ? \"");
		return s.toString();			
	}
	
	protected String BuildSELECTOverview(ABMGeneratorCRUDDefinition d) {
		if (!d.GenerateQueries) {
			return "TODO";
		}
		StringBuilder s = new StringBuilder();
		s.append("\"SELECT ");
		for (int i=0;i<d.FieldNames.size();i++) {
			if (i>0) s.append(", ");
			s.append(d.FieldNames.get(i));
			
		}
		return s.toString();			
	}
	
	protected String BuildSELECTCheck(ABMGeneratorCRUDDefinition d) {
		if (!d.GenerateQueries) {
			return "TODO";
		}
		StringBuilder s = new StringBuilder();
		s.append("\"SELECT " + d.IDFieldName);
		s.append(" FROM " + d.TableName);
		s.append(" WHERE " + d.IDFieldName + " = ? \"");
		return s.toString();			
	}
	
	protected String BuildUPDATE(ABMGeneratorCRUDDefinition d) {
		if (!d.GenerateQueries) {
			return "TODO";
		}
		StringBuilder s = new StringBuilder();
		s.append("UPDATE " + d.TableName + " SET ");
		
		boolean DoAnd=false;
		for (int i=0;i<d.FieldNames.size();i++) {
			if (!d.FieldNames.get(i).toLowerCase().equals(d.IDFieldName.toLowerCase())) {
				if (d.UseInUpdates.get(i)) {  	
					if (DoAnd) s.append(", ");
					s.append(d.FieldNames.get(i) + "=?");
				
					DoAnd=true;
				}
			}
		}
		s.append(" WHERE " + d.IDFieldName + "=\" & Active" + d.uniqueName + d.IDFieldName);
		return s.toString();
	}	
	
	protected String BuildINSERT(ABMGeneratorCRUDDefinition d) {
		if (!d.GenerateQueries) {
			return "TODO";
		}
		StringBuilder s = new StringBuilder();
		s.append("INSERT INTO " + d.TableName + "(");
		boolean DoAnd=false;
		for (int i=0;i<d.FieldNames.size();i++) {
			if (d.IgnoreAutoNumberInINSERT) {
				if (!d.FieldNames.get(i).toLowerCase().equals(d.IDFieldName.toLowerCase())) {
					if (DoAnd) s.append(", ");
					s.append(d.FieldNames.get(i));
					DoAnd=true;
				}
			} else {
				if (DoAnd) s.append(", ");
				s.append(d.FieldNames.get(i));
				DoAnd=true;
			}
		}
		s.append(") VALUES(");
		DoAnd=false;
		for (int i=0;i<d.FieldNames.size();i++) {		
			if (d.IgnoreAutoNumberInINSERT) {
				if (!d.FieldNames.get(i).toLowerCase().equals(d.IDFieldName.toLowerCase())) {
					if (DoAnd) s.append(", ");				
					s.append("?");				
					DoAnd=true;
				}
			} else {
				if (DoAnd) s.append(", ");				
				s.append("?");				
				DoAnd=true;
			}
		}
		
		s.append(")");
		return s.toString();
	}
	
	
	protected String BuildDELETE(ABMGeneratorCRUDDefinition d) {
		if (!d.GenerateQueries) {
			return "TODO";
		}
		StringBuilder s = new StringBuilder();
		s.append("\"DELETE FROM " + d.TableName + " WHERE " + d.IDFieldName + " = ? \"");
		return s.toString();
	}
}
