package com.ab.abmaterial;

import java.util.ArrayList;
import java.util.List;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")  
@ShortName("ABMGeneratorCRUDDefinition")
public class ABMGeneratorCRUDDefinition {
	protected String Title = "";
	protected String IDFieldName="";
	protected String TableName="";
	protected String SaveButtonText="";
	protected String CancelButtonText="";
	protected int MaxRow=0;
	
	protected List<String> FieldNames = new ArrayList<String>();
	protected List<Integer> Types = new ArrayList<Integer>();
	protected List<Integer> Rows = new ArrayList<Integer>();
	protected List<Integer> OSmalls = new ArrayList<Integer>();
	protected List<Integer> OMediums = new ArrayList<Integer>();
	protected List<Integer> OLarges = new ArrayList<Integer>();
	protected List<Integer> SSmalls = new ArrayList<Integer>();
	protected List<Integer> SMediums = new ArrayList<Integer>();
	protected List<Integer> SLarges = new ArrayList<Integer>();
	protected List<Boolean> Enableds = new ArrayList<Boolean>();
	protected List<Boolean> UseInUpdates = new ArrayList<Boolean>();
	protected List<String> Labels = new ArrayList<String>();
	protected List<String> Defaults = new ArrayList<String>();
	protected List<String> Queries = new ArrayList<String>();
	protected List<String> Validations = new ArrayList<String>();
	protected List<anywheresoftware.b4a.objects.collections.Map> Items = new ArrayList<anywheresoftware.b4a.objects.collections.Map>();
	protected String InvalidInputMessage="";
	protected String DeleteMessage="";
	protected String YesButtonText="Yes";
	protected String NoButtonText="No";
	protected String CloseButtonText="Close";
	protected String uniqueName="";
	protected String EditColumnTitle="Open/Edit";
	protected String DeleteColumnTitle="Delete";
	protected String SearchInputCaption="Search";
	protected String TableTheme="";
	
	protected List<Boolean> OverviewSorts = new ArrayList<Boolean>();
	protected List<Boolean> OverviewVisibles = new ArrayList<Boolean>();
	protected List<Boolean> OverviewSearches = new ArrayList<Boolean>();
	protected List<String> OverviewHeaderThemes = new ArrayList<String>();
	protected List<String> OverviewThemes = new ArrayList<String>();
	protected List<Integer> OverviewHeights = new ArrayList<Integer>();
	protected List<Integer> OverviewWidths = new ArrayList<Integer>();
		
	public GeneratorDateTimeScroller DateTimeScroller = new GeneratorDateTimeScroller();
	public GeneratorDateTimePicker DateTimePicker = new GeneratorDateTimePicker();
	
	public boolean IgnoreAutoNumberInINSERT=false;
	public boolean OverviewIsScrollable=false;
	
	protected boolean oHasEdit=false;
	protected boolean oHasDelete=false;
	protected boolean oHasSearch=false;
	protected boolean oHasPagination=false;
	protected boolean oHasAddButton=false;
	protected int oRowsPerPage=10;
	protected String FooterString="";
	protected boolean oIsResponsive=false;
	
	public boolean GenerateQueries=true;
	public boolean MarkToDocument=false;
	public boolean IsInitialized=false;
		
	/**
	 * IDFieldName: name of the ID field
	 * tableName: name of the table in your database
	 * title: title that will appear on top of the modal form
	 * saveButtonText: text on the SAVE button
	 * cancelButtonText: text on the CANCEL button
	 * deleteMessage: text to show in the messagebox when the user wants to delete a record
	 * invalidInputMessage: text to show in the messagebox when not all input fields are valid
	 */
	public void Initialize(String IDFieldName, String tableName, String title, String saveButtonText, String cancelButtonText, String YesButtonText, String NoButtonText, String closeButtonText, String deleteMessage, String invalidInputMessage) {
		this.Title = title;
		this.IDFieldName=IDFieldName;
		this.TableName=tableName;
		this.SaveButtonText=saveButtonText;
		this.CancelButtonText=cancelButtonText;
		this.InvalidInputMessage=invalidInputMessage;
		this.DeleteMessage=deleteMessage;
		this.YesButtonText = YesButtonText;
		this.NoButtonText = NoButtonText;
		this.CloseButtonText = closeButtonText;
		IsInitialized=true;
	}	
	
	/**
	 * Set the table theme 
	 */
	public void OverviewSetTheme(String themeName) {
		this.TableTheme = themeName;
	}
	
	/**
	 * Does the overview table have an Edit column 
	 */
	public void OverviewHasEdit(String overviewEditColumnTitle) {
		this.oHasEdit = true;
		this.EditColumnTitle = overviewEditColumnTitle;
	}
	
	/**
	 * Does the overview table have a Delete column 
	 */
	public void OverviewHasDelete(String overviewDeleteColumnTitle) {
		this.oHasDelete = true;
		this.DeleteColumnTitle = overviewDeleteColumnTitle;
	}
	
	/**
	 * Does the overview have a search box 
	 */
	public void OverviewHasSearchBox(String searchInputCaption) {
		this.oHasSearch = true;
		this.SearchInputCaption = searchInputCaption;
	}
	
	/**
	 * Does the overview use pagination 
	 */
	public void OverviewHasPagination(int rowsPerPage) {
		this.oHasPagination=true;
		this.oRowsPerPage = rowsPerPage;
	}
	
	/**
	 * Create an action button on the bottom right of the screen to add a record
	 */
	public void OverviewHasAddButton() {
		this.oHasAddButton=true;
	}
	
	/**
	 * Create the overview table as a responsive table (on mobiles it becomes a horizontal list)
	 */
	public void OverviewIsResponsive() {
		this.oIsResponsive=true;
	}
	
	/**
	 * Generator can only create a footer as a string 
	 */
	public void OverviewFooter(String footer) {
		this.FooterString = footer;
	}
	
	/**
	 * Array as String (real names in your database!)
	 * Very important! The order of the visible fields MUST be in the order they need to appear in the ModalSheet:
	 * 
	 * Per row, Cells from left to right:
	 * 
	 * e.g. |   field1   |  field2             |
	 *      |   field3                         |
	 *      |   field4   |  field5   | field6  |
	 *      |   field7                         |
	 *      
	 * Becomes: Array As String(notvisible1, notvisible2, field1, field2, field3, field4, field5, field6, field7)
	 */
	public void Set01FieldNames(anywheresoftware.b4a.objects.collections.List Values) {
		for (int i=0;i<Values.getSize();i++) {
			FieldNames.add((String) Values.Get(i));
		}
	}
	
	/**
	 * Array as String, allowed formats:
	 *    R:O:S where R=Row, O=offset, S=size (Row starts from 1, All O+S in one row must add-up to 12!)
	 *    R:OS:OM:OL:SS:SM:SL where R=Row, OS=offsetSmall, OM=offsetMedium, OL=offsetLarge, SS=sizeSmall, SM=sizeMedium, SL=sizeLarge
	 *    Empty string if not visible, e.g. an ID field
	 */
	public void Set02RowOffsetSizes(anywheresoftware.b4a.objects.collections.List Values) {
		for (int i=0;i<Values.getSize();i++) {
			String s = (String)Values.Get(i);
			if (s.equals("")) {
				Rows.add(0);
				OSmalls.add(0);
				OMediums.add(0);
				OLarges.add(0);
				SSmalls.add(0);
				SMediums.add(0);
				SLarges.add(0);
			} else {
				String[] parts = s.split(":");
				if (Integer.parseInt(parts[0])>MaxRow) {
					MaxRow=Integer.parseInt(parts[0]);
				}
				if (parts.length==3) {
					Rows.add(Integer.parseInt(parts[0]));
					OSmalls.add(Integer.parseInt(parts[1]));
					OMediums.add(Integer.parseInt(parts[1]));
					OLarges.add(Integer.parseInt(parts[1]));
					SSmalls.add(Integer.parseInt(parts[2]));
					SMediums.add(Integer.parseInt(parts[2]));
					SLarges.add(Integer.parseInt(parts[2]));
				} else {
					Rows.add(Integer.parseInt(parts[0]));
					OSmalls.add(Integer.parseInt(parts[1]));
					OMediums.add(Integer.parseInt(parts[2]));
					OLarges.add(Integer.parseInt(parts[3]));
					SSmalls.add(Integer.parseInt(parts[4]));
					SMediums.add(Integer.parseInt(parts[5]));
					SLarges.add(Integer.parseInt(parts[6]));
				}
			}
		}
	}	
	
	/**
	 * Array as string, labels on the component.
	 * Note: in case of a switch the format is:
	 *    "OnText,offText"
	 */
	public void Set03LabelTexts(anywheresoftware.b4a.objects.collections.List Values) {
		for (int i=0;i<Values.getSize();i++) {
			Labels.add((String) Values.Get(i));
		}
	}
	
	/**
	 * Array as int, using CRUDTYPE constants
	 */
	public void Set04ComponentTypes(anywheresoftware.b4a.objects.collections.List Values) {
		for (int i=0;i<Values.getSize();i++) {
			Types.add((int) Values.Get(i));
		}
	}
	
	/**
	 * Array as string, default value when the user creates a new record.
	 * Notes: 
	 *    For Combos this must be the returnId (int, most of the time a database id, eg "123")!
	 *    For Switches/Checkboxes this must be "true" or "false"
	 *    For Dates must be a long (e.g. 145671250000), a date in the format you specified (e.g. '22-01-2016T10:20:00') or you can use "NOW" 
	 */	
	public void Set05DefaultValues(anywheresoftware.b4a.objects.collections.List Values) {
		for (int i=0;i<Values.getSize();i++) {
			Defaults.add((String) Values.Get(i));
		}
	}
	
	
	
	/**
	 * Array of String, combo query in format: SELECT returnId, TextToShowInCombo FROM Table WHERE your_where ORDER BY order
	 * where can contain variables that are other fields using '$', e.g. a combo that has other values depending on who was selected in the
	 * person combo.
	 * 
	 * You have a combo prsId to pick a person:
	 * 
	 * SELECT tPersons.prsID, tPersons.prsName 
	 *    FROM tPersons 
	 *    ORDER BY tPersons.prsName;
	 * 
	 * You have a combo comId to pick a command and depends on who the person is:
	 * 
	 * SELECT tCommands.comId, tCommands.comName 
	 *    FROM tCommands INNER JOIN tCommandsPersons ON tCommand.comId = tCommandPersons.comprsComId 
	 *    WHERE tCommandPersons.comprsPrsId=$prsId$
	 *    ORDER BY tCommands.comName
	 *    
	 * The value of what is in the prsId combo will be used in the query of the comId combo.   
	 */
	public void Set06ComboQueries(anywheresoftware.b4a.objects.collections.List Values) {
		for (int i=0;i<Values.getSize();i++) {
			Queries.add((String) Values.Get(i));
		}
	}
	
	/**
	 * Array of Maps, where key = returnId and value = TextToShowInCombo
	 * Use Null if not applicable
	 */
	public void Set07ComboLists(anywheresoftware.b4a.objects.collections.List Values) {
		for (int i=0;i<Values.getSize();i++) {
			Items.add((anywheresoftware.b4a.objects.collections.Map) Values.Get(i));
		}
	}
	
	/**
	 * Array of strings, containing the Method names to validate an input field
	 * Methods will be autogenerated as:
	 * 
	 * Sub MethodName(value as String) as Boolean
	 * 
	 * End Sub
	 */
	public void Set08ValidationMethods(anywheresoftware.b4a.objects.collections.List Values) {
		for (int i=0;i<Values.getSize();i++) {
			Validations.add((String) Values.Get(i));
		}
	}
		
	/**
	 * Array of booleans, containing booleans with enabled or not	  
	 */
	public void Set09Enableds(anywheresoftware.b4a.objects.collections.List Values) {
		for (int i=0;i<Values.getSize();i++) {
			Enableds.add((boolean) Values.Get(i));
		}
	}
	
	/**
	 * Array of booleans, containing booleans if it should overwrite the field when updating a record	 
	 * Note: the ID field will never be overwritten! 
	 */
	public void Set10UseInUpdates(anywheresoftware.b4a.objects.collections.List Values) {
		for (int i=0;i<Values.getSize();i++) {
			UseInUpdates.add((boolean) Values.Get(i));
		}
	}
	
	/**
	 * Array of booleans, containing booleans if they show up in the overview
	 */
	public void Set11OverviewVisible(anywheresoftware.b4a.objects.collections.List Values) {
		for (int i=0;i<Values.getSize();i++) {
			OverviewVisibles.add((boolean) Values.Get(i));
		}
	}
	
	/**
	 * Array of Strings, the header column theme 
	 */
	public void Set12OverviewHeaderThemes(anywheresoftware.b4a.objects.collections.List Values) {
		for (int i=0;i<Values.getSize();i++) {
			OverviewHeaderThemes.add((String) Values.Get(i));
		}
	}
	
	/**
	 * Array of Strings, the cell column theme 
	 */
	public void Set13OverviewCellThemes(anywheresoftware.b4a.objects.collections.List Values) {
		for (int i=0;i<Values.getSize();i++) {
			OverviewThemes.add((String) Values.Get(i));
		}
	}
	
	/**
	 * Array as boolean, if they should be used in the search query (only applicable if OverviewHasSearchBox() is set to true) 
	 */
	public void Set14OverviewIncludeInSearch(anywheresoftware.b4a.objects.collections.List Values) {
		for (int i=0;i<Values.getSize();i++) {
			OverviewSearches.add((boolean) Values.Get(i));
		}
	}
	
	/**
	 * Array as boolean, if the column can be sorted 
	 */
	public void Set15OverviewSortable(anywheresoftware.b4a.objects.collections.List Values) {
		for (int i=0;i<Values.getSize();i++) {
			OverviewSorts.add((boolean) Values.Get(i));
		}
	}	
	
	/**
	 * Array as int, 0 for auto 
	 */
	public void Set16OverviewHeaderHeights(anywheresoftware.b4a.objects.collections.List Values) {
		for (int i=0;i<Values.getSize();i++) {
			OverviewHeights.add((int) Values.Get(i));
		}
	}
	
	/**
	 * Array as int, 0 for auto 
	 */
	public void Set17OverviewColumnWidths(anywheresoftware.b4a.objects.collections.List Values) {
		for (int i=0;i<Values.getSize();i++) {
			OverviewWidths.add((int) Values.Get(i));
		}
	}
	
}
