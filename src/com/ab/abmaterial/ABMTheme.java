package com.ab.abmaterial;

import java.util.HashMap;
import java.util.Map;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")  
@ShortName("ABMTheme")
public class ABMTheme implements java.io.Serializable {
	
	private static final long serialVersionUID = 1543530693785503653L;
	public String Name="default";
	public ThemePage Page = new ThemePage();
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;
	protected Map<String, ThemeRow> Rows = new HashMap<String, ThemeRow>();
	protected Map<String, ThemeCell> Cells = new HashMap<String, ThemeCell>();
	protected Map<String, ThemeNavigationBar> NavBars = new HashMap<String, ThemeNavigationBar>();
	protected Map<String, ThemeDivider> Dividers = new HashMap<String, ThemeDivider>();
	protected Map<String, ThemeLabel> Labels = new HashMap<String, ThemeLabel>();
	protected Map<String, ThemeTable> Tables = new HashMap<String, ThemeTable>();
	protected Map<String, ThemeContainer> Containers = new HashMap<String, ThemeContainer>();
	protected Map<String, ThemeButton> Buttons = new HashMap<String, ThemeButton>();
	protected Map<String, ThemeToast> Toasts = new HashMap<String, ThemeToast>();
	protected Map<String, ThemeTabs> Tabs = new HashMap<String, ThemeTabs>();
	protected Map<String, ThemeCard> Cards = new HashMap<String, ThemeCard>();
	protected Map<String, ThemeInput> Inputs = new HashMap<String, ThemeInput>();
	protected Map<String, ThemeList> Lists = new HashMap<String, ThemeList>();
	protected Map<String, ThemeCheckbox> Checkboxes = new HashMap<String, ThemeCheckbox>();
	protected Map<String, ThemeImageSlider> ImageSliders = new HashMap<String, ThemeImageSlider>();
	protected Map<String, ThemeSwitch> Switches = new HashMap<String, ThemeSwitch>();
	protected Map<String, ThemeRadioGroup> RadioGroups = new HashMap<String, ThemeRadioGroup>();
	protected Map<String, ThemeCombo> Combos = new HashMap<String, ThemeCombo>();
	protected Map<String, ThemeChip> Chips = new HashMap<String, ThemeChip>();
	protected Map<String, ThemeBadge> Badges = new HashMap<String, ThemeBadge>();
	protected Map<String, ThemeUpload> Uploads = new HashMap<String, ThemeUpload>();
	protected Map<String, ThemeChart> Charts = new HashMap<String, ThemeChart>();
	protected Map<String, ThemeTreeTable> TreeTables = new HashMap<String, ThemeTreeTable>();
	protected Map<String, ThemePagination> Paginations = new HashMap<String, ThemePagination>();
	protected Map<String, ThemeSlider> Sliders = new HashMap<String, ThemeSlider>();
	protected Map<String, ThemeRange> Ranges = new HashMap<String, ThemeRange>();
	protected Map<String, ThemeDateTimeScroller> DateTimeScrollers = new HashMap<String, ThemeDateTimeScroller>();
	protected Map<String, ThemeDateTimePicker> DateTimePickers = new HashMap<String, ThemeDateTimePicker>();
	protected Map<String, ThemeCalendar> Calendars = new HashMap<String, ThemeCalendar>();
	protected Map<String, ThemeEditor> Editors = new HashMap<String, ThemeEditor>();
	protected Map<String, ThemeModalSheet> ModalSheets = new HashMap<String, ThemeModalSheet>();
	protected Map<String, ThemePDFViewer> PDFViewers = new HashMap<String, ThemePDFViewer>();
	protected Map<String, ThemePivotTable> PivotTables = new HashMap<String, ThemePivotTable>();
	protected Map<String, ThemeAudioPlayer> AudioPlayers = new HashMap<String, ThemeAudioPlayer>();
	protected Map<String, ThemeTimeline> Timelines = new HashMap<String, ThemeTimeline>();
	protected Map<String, ThemePatternLock> PatternLocks = new HashMap<String, ThemePatternLock>();
	protected Map<String, ThemeChronologyList> ChronologyLists = new HashMap<String, ThemeChronologyList>();
	protected Map<String, ThemeCustomCard> CustomCards = new HashMap<String, ThemeCustomCard>();
	protected Map<String, ThemeChat> Chats = new HashMap<String, ThemeChat>();
	protected Map<String, ThemePlanner> Planners = new HashMap<String, ThemePlanner>();
	protected Map<String, ThemePercentSlider> PercentSliders = new HashMap<String, ThemePercentSlider>();
	protected Map<String, ThemeMsgBox> Msgboxes = new HashMap<String, ThemeMsgBox>();
	protected Map<String, ThemeSmartWizard> SmartWizards = new HashMap<String, ThemeSmartWizard>();
	protected Map<String, ThemeComposer> Composers = new HashMap<String, ThemeComposer>();
	protected Map<String, ThemeFileManager> FileManagers = new HashMap<String, ThemeFileManager>();
	
	public boolean IsInitialized=false;
		
	public void Initialize(String name) {
		this.Name = name;
		IsInitialized=true;
	}
	
	public void Colorize(String color) {
		MainColor=color;
		Page.Colorize(MainColor);	
	}
	
	public void ColorizeDeep(String color) {
		MainColor=color;
		Page.Colorize(MainColor);	
		for (Map.Entry<String, ThemeRow> entry : Rows.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeCell> entry : Cells.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeNavigationBar> entry : NavBars.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeDivider> entry : Dividers.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeLabel> entry : Labels.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeTable> entry : Tables.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeContainer> entry : Containers.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeButton> entry : Buttons.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeToast> entry : Toasts.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeTabs> entry : Tabs.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeCard> entry : Cards.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeInput> entry : Inputs.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}	
		for (Map.Entry<String, ThemeList> entry : Lists.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}	
		for (Map.Entry<String, ThemeCheckbox> entry : Checkboxes.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeImageSlider> entry : ImageSliders.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeSwitch> entry : Switches.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeRadioGroup> entry : RadioGroups.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeCombo> entry : Combos.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeChip> entry : Chips.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeBadge> entry : Badges.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeUpload> entry : Uploads.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeChart> entry : Charts.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeTreeTable> entry : TreeTables.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemePagination> entry : Paginations.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeSlider> entry : Sliders.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeRange> entry : Ranges.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeDateTimeScroller> entry : DateTimeScrollers.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeDateTimePicker> entry : DateTimePickers.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeCalendar> entry : Calendars.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}	
		for (Map.Entry<String, ThemeEditor> entry : Editors.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeModalSheet> entry : ModalSheets.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemePDFViewer> entry : PDFViewers.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemePivotTable> entry : PivotTables.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeAudioPlayer> entry : AudioPlayers.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeTimeline> entry : Timelines.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemePatternLock> entry : PatternLocks.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeChronologyList> entry : ChronologyLists.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeCustomCard> entry : CustomCards.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeChat> entry : Chats.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemePlanner> entry : Planners.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemePercentSlider> entry : PercentSliders.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeMsgBox> entry : Msgboxes.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeSmartWizard> entry : SmartWizards.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}		
		for (Map.Entry<String, ThemeComposer> entry : Composers.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
		for (Map.Entry<String, ThemeFileManager> entry : FileManagers.entrySet()) {
			entry.getValue().Colorize(MainColor);
		}
	}	
	
	/**
	 * Adds the complete theme, including Page settings and Colorized settings
	 * Useful as an initial copy (e.g. a shared basic theme) 
	 */
	public void AddABMTheme(ABMTheme theme) {
		Page = theme.Page.Clone();
		MainColor = theme.MainColor;
		ExtendWithABMTheme(theme);
	}
	
	public String getMainColor() {
		return this.MainColor;
	}
	
	/**
	 * Adds all the child themes, but does not include Page settings or Colorized settings
	 * Useful to extend a theme with a temporary declared theme that was just created to build
	 * the structure of child themes. 
	 */
	public void ExtendWithABMTheme(ABMTheme theme) {
		
		for (Map.Entry<String, ThemeRow> entry : theme.Rows.entrySet()) {
			Rows.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeCell> entry : theme.Cells.entrySet()) {
			Cells.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeNavigationBar> entry : theme.NavBars.entrySet()) {
			NavBars.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeDivider> entry : theme.Dividers.entrySet()) {
			Dividers.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeLabel> entry : theme.Labels.entrySet()) {
			Labels.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeTable> entry : theme.Tables.entrySet()) {
			Tables.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeContainer> entry : theme.Containers.entrySet()) {
			Containers.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeButton> entry : theme.Buttons.entrySet()) {
			Buttons.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeToast> entry : theme.Toasts.entrySet()) {
			Toasts.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeTabs> entry : theme.Tabs.entrySet()) {
			Tabs.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeCard> entry : theme.Cards.entrySet()) {
			Cards.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeInput> entry : theme.Inputs.entrySet()) {
			Inputs.put(entry.getKey(), entry.getValue().Clone());
		}	
		for (Map.Entry<String, ThemeList> entry : theme.Lists.entrySet()) {
			Lists.put(entry.getKey(), entry.getValue().Clone());
		}	
		for (Map.Entry<String, ThemeCheckbox> entry : theme.Checkboxes.entrySet()) {
			Checkboxes.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeImageSlider> entry : theme.ImageSliders.entrySet()) {
			ImageSliders.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeSwitch> entry : theme.Switches.entrySet()) {
			Switches.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeRadioGroup> entry : theme.RadioGroups.entrySet()) {
			RadioGroups.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeCombo> entry : theme.Combos.entrySet()) {
			Combos.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeChip> entry : theme.Chips.entrySet()) {
			Chips.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeBadge> entry : theme.Badges.entrySet()) {
			Badges.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeUpload> entry : theme.Uploads.entrySet()) {
			Uploads.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeChart> entry : theme.Charts.entrySet()) {
			Charts.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeTreeTable> entry : theme.TreeTables.entrySet()) {
			TreeTables.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemePagination> entry : theme.Paginations.entrySet()) {
			Paginations.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeSlider> entry : theme.Sliders.entrySet()) {
			Sliders.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeRange> entry : theme.Ranges.entrySet()) {
			Ranges.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeDateTimeScroller> entry : theme.DateTimeScrollers.entrySet()) {
			DateTimeScrollers.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeDateTimePicker> entry : theme.DateTimePickers.entrySet()) {
			DateTimePickers.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeCalendar> entry : theme.Calendars.entrySet()) {
			Calendars.put(entry.getKey(), entry.getValue().Clone());
		}	
		for (Map.Entry<String, ThemeEditor> entry : theme.Editors.entrySet()) {
			Editors.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeModalSheet> entry : theme.ModalSheets.entrySet()) {
			ModalSheets.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemePDFViewer> entry : theme.PDFViewers.entrySet()) {
			PDFViewers.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemePivotTable> entry : theme.PivotTables.entrySet()) {
			PivotTables.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeAudioPlayer> entry : theme.AudioPlayers.entrySet()) {
			AudioPlayers.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeTimeline> entry : theme.Timelines.entrySet()) {
			Timelines.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemePatternLock> entry : theme.PatternLocks.entrySet()) {
			PatternLocks.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeChronologyList> entry : theme.ChronologyLists.entrySet()) {
			ChronologyLists.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeCustomCard> entry : theme.CustomCards.entrySet()) {
			CustomCards.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeChat> entry : theme.Chats.entrySet()) {
			Chats.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemePlanner> entry : theme.Planners.entrySet()) {
			Planners.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemePercentSlider> entry : theme.PercentSliders.entrySet()) {
			PercentSliders.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeMsgBox> entry : theme.Msgboxes.entrySet()) {
			Msgboxes.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeSmartWizard> entry : theme.SmartWizards.entrySet()) {
			SmartWizards.put(entry.getKey(), entry.getValue().Clone());
		}		
		for (Map.Entry<String, ThemeComposer> entry : theme.Composers.entrySet()) {
			Composers.put(entry.getKey(), entry.getValue().Clone());
		}
		for (Map.Entry<String, ThemeFileManager> entry : theme.FileManagers.entrySet()) {
			FileManagers.put(entry.getKey(), entry.getValue().Clone());
		}
	}
	
	// Add
	public void AddRowTheme(String themeName) {
		ThemeRow t = new ThemeRow(themeName);
		t.Colorize(MainColor);
		Rows.put(themeName.toLowerCase(), t);
	}
	
	public void AddCellTheme(String themeName) {
		ThemeCell t = new ThemeCell(themeName);
		t.Colorize(MainColor);
		Cells.put(themeName.toLowerCase(), t);
	}
	
	public void AddNavigationBarTheme(String themeName) {
		ThemeNavigationBar t = new ThemeNavigationBar(themeName);
		t.Colorize(MainColor);
		NavBars.put(themeName.toLowerCase(), t);
	}
	
	public void AddDividerTheme(String themeName) {
		ThemeDivider t = new ThemeDivider(themeName);
		t.Colorize(MainColor);
		Dividers.put(themeName.toLowerCase(), t);
	}
	
	public void AddLabelTheme(String themeName) {
		ThemeLabel t = new ThemeLabel(themeName);
		t.Colorize(MainColor);
		Labels.put(themeName.toLowerCase(), t);
	}
	
	public void AddTableTheme(String themeName) {
		ThemeTable t = new ThemeTable(themeName);
		t.Colorize(MainColor);
		Tables.put(themeName.toLowerCase(), t);
	}
	
	public void AddContainerTheme(String themeName) {
		ThemeContainer t = new ThemeContainer(themeName);
		t.Colorize(MainColor);
		Containers.put(themeName.toLowerCase(), t);
	}
	
	public void AddButtonTheme(String themeName) {
		ThemeButton t = new ThemeButton(themeName);
		t.Colorize(MainColor);
		Buttons.put(themeName.toLowerCase(), t);
	}
	
	public void AddToastTheme(String themeName) {
		ThemeToast t =  new ThemeToast(themeName);
		t.Colorize(MainColor);
		Toasts.put(themeName.toLowerCase(), t);
	}
	
	public void AddTabsTheme(String themeName) {
		ThemeTabs t = new ThemeTabs(themeName);
		t.Colorize(MainColor);
		Tabs.put(themeName.toLowerCase(), t);
	}
	
	public void AddCardTheme(String themeName) {
		ThemeCard t = new ThemeCard(themeName);
		t.Colorize(MainColor);
		Cards.put(themeName.toLowerCase(), t);
	}
	
	public void AddInputTheme(String themeName) {
		ThemeInput t = new ThemeInput(themeName);
		t.Colorize(MainColor);
		Inputs.put(themeName.toLowerCase(), t);
	}	
	
	public void AddListTheme(String themeName) {
		ThemeList t = new ThemeList(themeName);
		t.Colorize(MainColor);
		Lists.put(themeName.toLowerCase(), t);
	}
	
	public void AddCheckboxTheme(String themeName) {
		ThemeCheckbox t = new ThemeCheckbox(themeName);
		t.Colorize(MainColor);
		Checkboxes.put(themeName.toLowerCase(), t);
	}
	
	public void AddImageSliderTheme(String themeName) {
		ThemeImageSlider t = new ThemeImageSlider(themeName);
		t.Colorize(MainColor);
		ImageSliders.put(themeName.toLowerCase(), t);
	}
	
	public void AddSwitchTheme(String themeName) {
		ThemeSwitch t = new ThemeSwitch(themeName);
		t.Colorize(MainColor);
		Switches.put(themeName.toLowerCase(), t);
	}
	public void AddRadioGroupTheme(String themeName) {
		ThemeRadioGroup t = new ThemeRadioGroup(themeName);
		t.Colorize(MainColor);
		RadioGroups.put(themeName.toLowerCase(), t);
	}
	public void AddComboTheme(String themeName) {
		ThemeCombo t = new ThemeCombo(themeName);
		t.Colorize(MainColor);
		Combos.put(themeName.toLowerCase(), t);
	}
	public void AddChipTheme(String themeName) {
		ThemeChip t = new ThemeChip(themeName);
		t.Colorize(MainColor);
		Chips.put(themeName.toLowerCase(), t);
	}
	public void AddBadgeTheme(String themeName) {
		ThemeBadge t = new ThemeBadge(themeName);
		t.Colorize(MainColor);
		Badges.put(themeName.toLowerCase(), t);
	}
	public void AddUploadTheme(String themeName) {
		ThemeUpload t = new ThemeUpload(themeName);
		t.Colorize(MainColor);
		Uploads.put(themeName.toLowerCase(), t);
	}
	public void AddChartTheme(String themeName) {
		ThemeChart t = new ThemeChart(themeName);
		t.Colorize(MainColor);
		Charts.put(themeName.toLowerCase(), t);
	}
	public void AddTreeTableTheme(String themeName) {
		ThemeTreeTable t = new ThemeTreeTable(themeName);
		t.Colorize(MainColor);
		TreeTables.put(themeName.toLowerCase(), t);
	}
	public void AddPaginationTheme(String themeName) {
		ThemePagination t = new ThemePagination(themeName);
		t.Colorize(MainColor);
		Paginations.put(themeName.toLowerCase(), t);
	}
	public void AddSliderTheme(String themeName) {
		ThemeSlider t = new ThemeSlider(themeName);
		t.Colorize(MainColor);
		Sliders.put(themeName.toLowerCase(), t);
	}
	public void AddRangeTheme(String themeName) {
		ThemeRange t = new ThemeRange(themeName);
		t.Colorize(MainColor);
		Ranges.put(themeName.toLowerCase(), t);
	}
	public void AddDateTimeScrollerTheme(String themeName) {
		ThemeDateTimeScroller t = new ThemeDateTimeScroller(themeName);
		t.Colorize(MainColor);
		DateTimeScrollers.put(themeName.toLowerCase(), t);
	}
	public void AddDateTimePickerTheme(String themeName) {
		ThemeDateTimePicker t = new ThemeDateTimePicker(themeName);
		t.Colorize(MainColor);
		DateTimePickers.put(themeName.toLowerCase(), t);
	}
	public void AddCalendarTheme(String themeName) {
		ThemeCalendar t =new ThemeCalendar(themeName);
		t.Colorize(MainColor);
		Calendars.put(themeName.toLowerCase(), t);
	}	
	public void AddEditorTheme(String themeName) {
		ThemeEditor t =new ThemeEditor(themeName);
		t.Colorize(MainColor);
		Editors.put(themeName.toLowerCase(), t);
	}	
	public void AddModalSheetTheme(String themeName) {
		ThemeModalSheet t =new ThemeModalSheet(themeName);
		t.Colorize(MainColor);
		ModalSheets.put(themeName.toLowerCase(), t);
	}
	public void AddPDFViewerTheme(String themeName) {
		ThemePDFViewer t =new ThemePDFViewer(themeName);
		t.Colorize(MainColor);
		PDFViewers.put(themeName.toLowerCase(), t);
	}
	public void AddPivotTable(String themeName) {
		ThemePivotTable t =new ThemePivotTable(themeName);
		t.Colorize(MainColor);
		PivotTables.put(themeName.toLowerCase(), t);
	}
	public void AddAudioPlayer(String themeName) {
		ThemeAudioPlayer t =new ThemeAudioPlayer(themeName);
		t.Colorize(MainColor);
		AudioPlayers.put(themeName.toLowerCase(), t);
	}
	public void AddTimeline(String themeName) {
		ThemeTimeline t =new ThemeTimeline(themeName);
		t.Colorize(MainColor);
		Timelines.put(themeName.toLowerCase(), t);
	}
	public void AddPatternLock(String themeName) {
		ThemePatternLock t =new ThemePatternLock(themeName);
		t.Colorize(MainColor);
		PatternLocks.put(themeName.toLowerCase(), t);
	}
	public void AddChronologyList(String themeName) {
		ThemeChronologyList t =new ThemeChronologyList(themeName);
		t.Colorize(MainColor);
		ChronologyLists.put(themeName.toLowerCase(), t);
	}
	public void AddCustomCardTheme(String themeName) {
		ThemeCustomCard t = new ThemeCustomCard(themeName);
		t.Colorize(MainColor);
		CustomCards.put(themeName.toLowerCase(), t);
	}
	public void AddChatTheme(String themeName) {
		ThemeChat t = new ThemeChat(themeName);
		t.Colorize(MainColor);
		Chats.put(themeName.toLowerCase(), t);
	}
	public void AddPlannerTheme(String themeName) {
		ThemePlanner t = new ThemePlanner(themeName);
		t.Colorize(MainColor);
		Planners.put(themeName.toLowerCase(), t);
	}
	public void AddPercentSliderTheme(String themeName) {
		ThemePercentSlider t = new ThemePercentSlider(themeName);
		t.Colorize(MainColor);
		PercentSliders.put(themeName.toLowerCase(), t);
	}
	public void AddMsgBoxTheme(String themeName) {
		ThemeMsgBox t = new ThemeMsgBox(themeName);
		t.Colorize(MainColor);
		Msgboxes.put(themeName.toLowerCase(), t);
	}
	public void AddSmartWizardTheme(String themeName) {
		ThemeSmartWizard t = new ThemeSmartWizard(themeName);
		t.Colorize(MainColor);
		SmartWizards.put(themeName.toLowerCase(), t);
	}
	public void AddComposerTheme(String themeName) {
		ThemeComposer t = new ThemeComposer(themeName);
		t.Colorize(MainColor);
		Composers.put(themeName.toLowerCase(), t);
	}
	public void AddFileManagerTheme(String themeName) {
		ThemeFileManager t = new ThemeFileManager(themeName);
		t.Colorize(MainColor);
		FileManagers.put(themeName.toLowerCase(), t);
	}
		
	// Get
	public ThemeRow Row(String themeName) {
		return Rows.getOrDefault(themeName.toLowerCase(), null);
	}
	
	public ThemeCell Cell(String themeName) {
		return Cells.getOrDefault(themeName.toLowerCase(), null);
	}
	
	public ThemeNavigationBar NavigationBar(String themeName) {
		return NavBars.getOrDefault(themeName.toLowerCase(), null);
	}
	
	public ThemeDivider Divider(String themeName) {
		return Dividers.getOrDefault(themeName.toLowerCase(), null);
	}
	
	public ThemeLabel Label(String themeName) {
		return Labels.getOrDefault(themeName.toLowerCase(), null);
	}
	
	public ThemeTable Table(String themeName) {
		return Tables.getOrDefault(themeName.toLowerCase(), null);
	}
	
	public ThemeContainer Container(String themeName) {
		return Containers.getOrDefault(themeName.toLowerCase(), null);
	}
	
	public ThemeButton Button(String themeName) {
		return Buttons.getOrDefault(themeName.toLowerCase(), null);
	}
	
	public ThemeToast Toast(String themeName) {
		return Toasts.getOrDefault(themeName.toLowerCase(), null);
	}
	
	public ThemeTabs Tabs(String themeName) {
		return Tabs.getOrDefault(themeName.toLowerCase(), null);
	}
	
	public ThemeCard Card(String themeName) {
		return Cards.getOrDefault(themeName.toLowerCase(), null);
	}
	
	public ThemeInput Input(String themeName) {
		return Inputs.getOrDefault(themeName.toLowerCase(), null);
	}	
	
	public ThemeList List(String themeName) {
		return Lists.getOrDefault(themeName.toLowerCase(), null);
	}
	
	public ThemeCheckbox Checkbox(String themeName) {
		return Checkboxes.getOrDefault(themeName.toLowerCase(), null);
	}
	
	public ThemeImageSlider ImageSlider(String themeName) {
		return ImageSliders.getOrDefault(themeName.toLowerCase(), null);
	}
	
	public ThemeSwitch Switch(String themeName) {
		return Switches.getOrDefault(themeName.toLowerCase(), null);
	}
	
	public ThemeRadioGroup RadioGroup(String themeName) {
		return RadioGroups.getOrDefault(themeName.toLowerCase(), null);
	}
	
	public ThemeCombo Combo(String themeName) {
		return Combos.getOrDefault(themeName.toLowerCase(), null);
	}
	
	public ThemeChip Chip(String themeName) {
		return Chips.getOrDefault(themeName.toLowerCase(), null);
	}
	
	public ThemeBadge Badge(String themeName) {
		return Badges.getOrDefault(themeName.toLowerCase(), null);
	}
	public ThemeUpload Upload(String themeName) {
		return Uploads.getOrDefault(themeName.toLowerCase(), null);
	}
	public ThemeChart Chart(String themeName) {
		return Charts.getOrDefault(themeName.toLowerCase(), null);
	}
	public ThemeTreeTable TreeTable(String themeName) {
		return TreeTables.getOrDefault(themeName.toLowerCase(), null);
	}
	public ThemePagination Pagination(String themeName) {
		return Paginations.getOrDefault(themeName.toLowerCase(), null);
	}
	public ThemeSlider Slider(String themeName) {
		return Sliders.getOrDefault(themeName.toLowerCase(), null);
	}
	public ThemeRange Range(String themeName) {
		return Ranges.getOrDefault(themeName.toLowerCase(), null);
	}
	public ThemeDateTimeScroller DateTimeScroller(String themeName) {
		return DateTimeScrollers.getOrDefault(themeName.toLowerCase(), null);
	}
	public ThemeDateTimePicker DateTimePicker(String themeName) {
		return DateTimePickers.getOrDefault(themeName.toLowerCase(), null);
	}	
	public ThemeCalendar Calendar(String themeName) {
		return Calendars.getOrDefault(themeName.toLowerCase(), null);
	}
	public ThemeEditor Editor(String themeName) {
		return Editors.getOrDefault(themeName.toLowerCase(), null);
	}
	public ThemeModalSheet ModalSheet(String themeName) {
		return ModalSheets.getOrDefault(themeName.toLowerCase(), null);
	}
	public ThemePDFViewer PDFViewer(String themeName) {
		return PDFViewers.getOrDefault(themeName.toLowerCase(), null);
	}
	public ThemePivotTable PivotTable(String themeName) {
		return PivotTables.getOrDefault(themeName.toLowerCase(), null);
	}
	public ThemeAudioPlayer AudioPlayer(String themeName) {
		return AudioPlayers.getOrDefault(themeName.toLowerCase(), null);
	}
	public ThemeTimeline Timeline(String themeName) {
		return Timelines.getOrDefault(themeName.toLowerCase(), null);
	}
	public ThemePatternLock PatternLock(String themeName) {
		return PatternLocks.getOrDefault(themeName.toLowerCase(), null);
	}
	public ThemeChronologyList ChronologyList(String themeName) {
		return ChronologyLists.getOrDefault(themeName.toLowerCase(), null);
	}
	public ThemeCustomCard CustomCard(String themeName) {
		return CustomCards.getOrDefault(themeName.toLowerCase(), null);
	}
	public ThemeChat Chat(String themeName) {
		return Chats.getOrDefault(themeName.toLowerCase(), null);
	}
	public ThemePlanner Planner(String themeName) {
		return Planners.getOrDefault(themeName.toLowerCase(), null);
	}
	public ThemePercentSlider PercentSlider(String themeName) {
		return PercentSliders.getOrDefault(themeName.toLowerCase(), null);
	}
	public ThemeMsgBox MsgBox(String themeName) {
		return Msgboxes.getOrDefault(themeName.toLowerCase(), null);
	}
	public ThemeSmartWizard SmartWizard(String themeName) {
		return SmartWizards.getOrDefault(themeName.toLowerCase(), null);
	}
	public ThemeComposer Composer(String themeName) {
		return Composers.getOrDefault(themeName.toLowerCase(), null);
	}
	public ThemeFileManager FileManager(String themeName) {
		return FileManagers.getOrDefault(themeName.toLowerCase(), null);
	}
		
	// create
	@Hide
	public ThemeDivider CreateThemeDivider() {
		ThemeDivider t = new ThemeDivider();
		t.Colorize(MainColor);
		return t;
	}
	
	@Hide
	public ThemeToast CreateThemeToast() {
		ThemeToast t = new ThemeToast();
		t.Colorize(MainColor);
		return t;
	}
}
