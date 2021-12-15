package com.ab.abmaterial;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.zip.GZIPOutputStream;
import com.ab.abmaterial.ABMGridDefinition.CellDef;
import com.ab.abmaterial.ABMGridDefinition.RowDef;
import com.ab.abmaterial.ABMNavigationBar.ABMNavBarItem;
import com.ab.abmaterial.ThemeChart.ThemeSerie;
import com.ab.abmaterial.ThemeChat.ThemeChatBubble;
import com.ab.abmaterial.ThemeChronologyList.ThemeChronologyBadge;
import com.ab.abmaterial.ThemeComposer.ThemeComposerBlock;
import com.ab.abmaterial.ThemeList.ItemTheme;
import com.ab.abmaterial.ThemePage.ExtraColor;
import com.ab.abmaterial.ThemePage.Section;
import com.ab.abmaterial.ThemeTable.TableCell;
import com.ab.abmaterial.ThemeTreeTable.TreeIcon;

import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.keywords.Regex;
import anywheresoftware.b4j.object.JServlet.ServletRequestWrapper;
import anywheresoftware.b4j.object.WebSocket;
import anywheresoftware.b4j.object.WebSocket.SimpleFuture;

@Author("Alain Bailleul")
@Events(values={"NavigationbarClicked(Action As String, Value As String)", "ToastDismissed(ToastId As String)", "ToastClicked(ToastId As String, Action As String)", "ParseEvent(Params As Map)","FileUploaded(FileName as String, success as Boolean)","Authenticated(Params As Map)","SignedOffSocialNetwork(Network as String, Extra as String)","NextContent(TriggerComponent As String)","ModalSheetReady(ModalSheetName as String)","ModalSheetDismissed(ModalSheetName as String)", "FirebaseAuthStateChanged(IsLoggedIn as boolean)", "FirebaseAuthError(extra As String)", "FirebaseStorageError(jobID As String, extra As String)","FirebaseStorageResult(jobID As String, extra As String)", "MsgboxResult(returnName As String, result As String)", "InputboxResult(returnName As String, result As String)", "NativeResponse(jsonMessage As String)", "CellClicked(Target as String, RowCell as String)", "RowClicked(Target as String, Row as String)", "NavigationbarSearch(search as String)","DebugConsole(message As String)", "Dropped(Params as Map)", "DragStart(component as String, source as String)", "DragCancelled(component as String, source as String)", "SizeChanged(previous as String, current as String)", "MultiCellFinished(Params As Map)","VisibilityState(State as String)", "B4JSMsgboxResult(returnName As String, result As String) As Boolean", "B4JSInputboxResult(returnName As String, result As String) As Boolean","AjaxResult(uniqueId as String, result as String)", "AjaxError(uniqueId as String, error as String)","B4JSAjaxResult(uniqueId as String, result as String)", "B4JSAjaxError(uniqueId as String, error as String)", "IncomingBroadcast(message As ABMBroadcastMessage)", "OnResize()", "Expired()", "StillCached()"}) //, "ReportClicked(DataValue as String, IsOn as boolean)"})
@ShortName("ABMPage")
public class ABMPage{
	protected static boolean mUseWebWorker=false;;
	private static ScheduledExecutorService scheduler = null;
	private ScheduledFuture<?> pauseTimer=null;
		
	public boolean ShowConnectedIndicator=false;
	public String IndicatorPosition=ABMaterial.INDICATOR_RIGHT;
	
	protected String NativePlatform="NOTSET";
	
	protected String mPrintPageSize="A4";
	protected boolean mPrintPageLandscape=false;
	
	protected boolean mIsTablet=false;
	protected boolean mIsPhone=false;
	protected Object caller=null;
	
	public String PageLanguage="en";
	public boolean PageTranslatable=false;
	public String PageCharset="UTF-8";

	public String PageDescription="";
	public String PageTitle="";
	public String PageKeywords="";
	public String PageHTMLName="index.html";
	public long PageExpires=0;
	public String PageCSSInline=ABMaterial.PAGE_INLINE_USEAPPSETTING;
	public String PageJSInline=ABMaterial.PAGE_INLINE_USEAPPSETTING;
	
	public String PageSiteMapPriority="";
	public String PageSiteMapFrequency="none";
	
	public String GoogleMapsAPIExtras="";
	
	public anywheresoftware.b4a.objects.collections.Map OpenGraphMetaTags = new anywheresoftware.b4a.objects.collections.Map(); 
	
	public String SocialShareTheme=ABMaterial.SOCIALSHARTHEMETYPE_FLAT;
	
	protected boolean mAlwaysShowVerticalScrollBar=false;
	protected boolean mNeverShowVerticalScrollBar=false;
	
	public WebSocket ws;
	
	public boolean ShowDebugFlush=false;
	public boolean PageRaiseOnResize=false;
	
	protected int SessionMaxInactiveInterval=-1;
		
	protected List<ABMSection> Sections = new ArrayList<ABMSection>();
	
	protected Map<String,String> svgIconmap = new LinkedHashMap<String,String>();
	protected Map<String,String> svgIconmapContent =  new LinkedHashMap<String,String>();
	
	public boolean NeedsCodeLabel=false;
	public boolean NeedsYouTube=false;
	public boolean NeedsGoogleMap=false;
	public boolean NeedsCanvas=false;
	public boolean NeedsInput=false;
	public boolean NeedsFileInput=false;
	public boolean NeedsRadio=false;
	public boolean NeedsTabs=false;
	public boolean NeedsTable=false;
	public boolean NeedsSortingTable=false;
	public boolean NeedsJQueryUI=false;
	public boolean NeedsBadge=false;
	public boolean NeedsCheckbox=false;
	public boolean NeedsCombo=false;
	public boolean NeedsImageSlider=false;
	public boolean NeedsTextArea=false;
	public boolean NeedsSwitch=false;
	public boolean NeedsUpload=false;
	public boolean NeedsHTML5Video=false;
	public boolean NeedsChips=false;
	public boolean NeedsActionButton=false;
	public boolean NeedsLists=false;
	public boolean NeedsCards=false;
	public boolean NeedsParallax=false;
	public boolean NeedsSignature=false;
	public boolean NeedsChart=false;
	public boolean NeedsOAuth=false;
	public boolean NeedsCalendar=false;
	public boolean NeedsMask=false;
	public boolean NeedsTreeTable=false;
	public boolean NeedsPagination=false;
	public boolean NeedsSlider=false;
	public boolean NeedsRange=false;
	public boolean NeedsDateTimeScroller=false;
	public boolean NeedsDateTimePicker=false;
	public boolean NeedsEditor=false;
	public boolean NeedsInfiniteScroller=false;
	public boolean NeedsSocialShare=false;
	public boolean NeedsPDFViewer=false;
	public boolean NeedsPlatform=false;
	public boolean NeedsAudioPlayer=false;
	public boolean NeedsTimeline=false;
	public boolean NeedsFlexWall=false; 
	public boolean NeedsSVGSurface=false;
	public boolean NeedsPatternLock=false;
	public boolean NeedsChronologyList=false;
	protected boolean NeedsXPlay=false;
	public boolean NeedsCustomCards=false;
	public boolean NeedsChat=false;
	public boolean NeedsPlanner=false;
	public boolean NeedsPivot=false;
	public boolean NeedsPercentSlider=false;
	public boolean NeedsSmartWizard=false;
	public boolean NeedsComposer=false;
	public boolean NeedsFileManager=false;
	public boolean NeedsResponsiveContainer=false;
	public boolean NeedsB4JS=false;
	
	public boolean LoadCode128Font=false;
	
	public boolean NeedsFixPasswordAutoFill=false;
	
	public boolean NeedsSimpleBar=false;
	
	protected boolean NeedsXPlayAll=false;
	
	public boolean DisableBackButton=false;
	public boolean IsPaused=false;
	
	public boolean IsBuild=false;
	public int PaddingTop=0;
	public int PaddingBottom=0;
	
	public ABMContainer Footer=new ABMContainer();
	public boolean IsFixedFooter=false;
	
	public boolean NeedsBackgroundImage=false;
	public boolean NeedsBackgroundVideo=false;
	public Boolean ShowGridInfo=false;
	public int CurrentRow=1;

	public boolean DisablePageReloadOnSwipeDown=false;
	public boolean IsInitialized=false;
	
	protected String mStaticFilesFolder="";
	protected String mAppName="";
	
	protected List<String> AcceptedLanguages = new ArrayList<String>();
	protected String DefaultLanguage="en";
	protected String ActiveLanguage="en";
	protected String ActiveSubLanguage="en";
	protected String ActiveDef="";
	
	protected B4AClass mCurrentContentPageClass=null;
	protected String mCurrentContentPageClassPageID="";
	protected ABMPage PreHandlerPage=null;
	@Hide
	public boolean IsAppPage=false;
	
	protected String CurrentDir="";	
	protected String MainColor=ABMaterial.COLOR_LIGHTBLUE;	
	protected Map<String,ABMAnimation> Animations = new HashMap<String, ABMAnimation>();	
	protected String Handler = "";
	
	protected String Name="";
	protected ABMTheme CompleteTheme=null;
	protected Map<String, ABMTheme> CompleteThemes = new LinkedHashMap<String, ABMTheme>();
	protected ThemePage Theme=null;
	protected Map<String,ABMRowCell> AllComponents = new LinkedHashMap<String, ABMRowCell>();
	protected ABMNavigationBar navigationBar=null;
	protected Map<String,LayoutDef> Layouts = new LinkedHashMap<String,LayoutDef>();
	
	protected Map<String,Integer> B4JSComponents = new LinkedHashMap<String, Integer>();
	protected Map<String,String> B4JSUniqueComponents = new LinkedHashMap<String, String>();
	
	public anywheresoftware.b4a.objects.collections.List NeedsIcons = new anywheresoftware.b4a.objects.collections.List();
	public anywheresoftware.b4a.objects.collections.Map CustomVariables = new anywheresoftware.b4a.objects.collections.Map();
		
	protected boolean NeedsCodeLabelAll=false;
	protected boolean NeedsYouTubeAll=false;
	protected boolean NeedsGoogleMapAll=false;
	protected boolean NeedsCanvasAll=false;
	protected boolean NeedsInputAll=false;
	protected boolean NeedsFileInputAll=false;
	protected boolean NeedsRadioAll=false;
	protected boolean NeedsTabsAll=false;
	protected boolean NeedsTableAll=false;
	protected boolean NeedsJQueryUIAll=false;
	protected boolean NeedsSortingTableAll=false;
	protected boolean NeedsBadgeAll=false;
	protected boolean NeedsCheckboxAll=false;
	protected boolean NeedsComboAll=false;
	protected boolean NeedsImageSliderAll=false;
	protected boolean NeedsTextAreaAll=false;
	protected boolean NeedsSwitchAll=false;
	protected boolean NeedsUploadAll=false;
	protected boolean NeedsHTML5VideoAll=false;
	protected boolean NeedsChipsAll=false;
	protected boolean NeedsActionButtonAll=false;
	protected boolean NeedsListsAll=false;
	protected boolean NeedsCardsAll=false;
	protected boolean NeedsParallaxAll=false;
	protected boolean NeedsSignatureAll=false;
	protected boolean NeedsChartAll=false;
	protected boolean NeedsOAuthAll=false;
	protected boolean NeedsCalendarAll=false;
	protected boolean NeedsMaskAll=false;
	protected boolean NeedsTreeTableAll=false;
	protected boolean NeedsPaginationAll=false;
	protected boolean NeedsSliderAll=false;
	protected boolean NeedsRangeAll=false;
	protected boolean NeedsDateTimeScrollerAll=false;
	protected boolean NeedsDateTimePickerAll=false;
	protected boolean NeedsEditorAll=false;
	protected boolean NeedsSocialShareAll=false;
	protected boolean NeedsPDFViewerAll=false;
	protected boolean NeedsPlatformAll=false;
	protected boolean NeedsAudioPlayerAll=false;
	protected boolean NeedsTimelineAll=false;
	protected boolean NeedsFlexWallAll=false;
	protected boolean NeedsSVGSurfaceAll=false;
	protected boolean NeedsPatternLockAll=false;
	protected boolean NeedsChronologyListAll=false;
	protected boolean NeedsCustomCardsAll=false;
	protected boolean NeedsChatAll=false;
	protected boolean NeedsPlannerAll=false;
	protected boolean NeedsPivotAll=false;
	protected boolean NeedsPercentSliderAll=false;
	protected boolean NeedsSmartWizardAll=false;
	protected boolean NeedsSimpleBarAll=false;
	protected boolean NeedsComposerAll=false;
	protected boolean NeedsFileManagerAll=false;
	protected boolean NeedsResponsiveContainerAll=false;
	protected boolean NeedsB4JSAll=false;
	
	protected Map<String,Boolean> PushPop = new HashMap<String,Boolean>();
	
	protected List<String> AppleTouchIcons=new ArrayList<String>();
	protected List<String> MSTileIcons=new ArrayList<String>();
	protected List<String> FavorityIcons=new ArrayList<String>();
	protected List<String> AppleTouchIconSizes=new ArrayList<String>();
	protected List<String> MSTileIconSizes=new ArrayList<String>();
	protected List<String> FavorityIconSizes=new ArrayList<String>();
	protected List<String> ScrollFires = new ArrayList<String>();
		
	protected Map<String,ABMList> Lists=new LinkedHashMap<String, ABMList>();
	protected Map<String,ABMInput> Inputs=new LinkedHashMap<String, ABMInput>();
	protected Map<String,ABMSignaturePad> signatures=new LinkedHashMap<String, ABMSignaturePad>();
	protected Map<String,ABMGoogleMap> gmaps=new LinkedHashMap<String, ABMGoogleMap>();
	protected Map<String,ABMUpload> uploads=new LinkedHashMap<String,ABMUpload>();
	protected Map<String,ABMSocialOAuth> Networks=new LinkedHashMap<String,ABMSocialOAuth>(); // NOT DYNAMICALLY ADDABLE!
	protected Map<String,ABMChart> Charts=new LinkedHashMap<String,ABMChart>();
	protected Map<String,ABMCalendar> Calendars=new LinkedHashMap<String,ABMCalendar>();
	protected Map<String,ABMCanvas> Canvases=new LinkedHashMap<String,ABMCanvas>();
	protected Map<String,ABMTable> Tables=new LinkedHashMap<String,ABMTable>();
	protected Map<String,ABMCustomComponent> customcomps=new LinkedHashMap<String,ABMCustomComponent>();
	protected Map<String,ABMSlider> sliders=new LinkedHashMap<String,ABMSlider>();
	protected Map<String,ABMRange> ranges=new LinkedHashMap<String,ABMRange>();
	protected Map<String,ABMDateTimeScroller> dateTimeScrollers=new LinkedHashMap<String,ABMDateTimeScroller>();
	protected Map<String,ABMDateTimePicker> dateTimePickers=new LinkedHashMap<String,ABMDateTimePicker>();
	protected Map<String,ABMEditor> Editors=new LinkedHashMap<String,ABMEditor>();
	protected Map<String,ABMSocialShare> SocialShares=new LinkedHashMap<String,ABMSocialShare>();
	protected Map<String,ABMContainer> Containers=new LinkedHashMap<String,ABMContainer>();
	protected Map<String,ABMContainer> FloatingContainers=new LinkedHashMap<String,ABMContainer>();
	protected Map<String,ABMTimeLine> TimeLines=new LinkedHashMap<String,ABMTimeLine>();
	protected Map<String,ABMFlexWall> FlexWalls=new LinkedHashMap<String,ABMFlexWall>();
	protected Map<String,ABMSVGSurface> SVGSurfaces=new LinkedHashMap<String,ABMSVGSurface>();
	protected Map<String,ABMPatternLock> PatternLocks=new LinkedHashMap<String,ABMPatternLock>();
	protected Map<String,ABMChronologyList> ChronoLists=new LinkedHashMap<String,ABMChronologyList>();
		
	protected Map<String, ABMRow> Rows = new LinkedHashMap<String,ABMRow>();
	protected Map<String, ABMModalSheet> ModalSheets = new LinkedHashMap<String,ABMModalSheet>();
	protected Map<String, ABMActionButton> ActionButtons = new LinkedHashMap<String,ABMActionButton>();
	
	protected boolean mShowLoader=false;
	protected int mShowLoaderType=ABMaterial.LOADER_TYPE_AUTO;
	protected double mLoaderOpacity=1;
	protected boolean CenterInPageWithPadding=true;
	
	protected ABMGridDefinition Grid = new ABMGridDefinition();
	protected List<String> Images = new ArrayList<String>();
	protected List<String> ImageIds = new ArrayList<String>();
	protected List<String> ExtraJS=new ArrayList<String>();
	protected List<String> ExtraCSS=new ArrayList<String>();
	protected List<String> ExtraCSSStrings=new ArrayList<String>();
	protected String BackgroundImage="";
	protected String BackgroundVideoWebm="";
	protected String BackgroundVideoMp4="";
	protected String BackgroundVideoOgv="";
	protected boolean BackgroundVideoMute=true;
	protected boolean BackgroundVideoHasOverlay=false;
	protected String BackgroundVideoImage="";
	protected int BackgroundVideoVolume=0;
	protected String FontStack="\"Roboto\",sans-serif";
	protected boolean UsesFirebase=false;	
	protected ABMFirebase mFirebase = new ABMFirebase();
	protected String BASFileName="";
	
	protected String mOldUUID="";
	
	protected String mB4JSUniqueKey="b4jspagekey";
	
	public boolean ComesFromPageCache=false;
	
	protected String mPageID="";
	protected String UUID="";
	
	private static String URelPath="";
	
	protected boolean DebugConzole=false;
	protected boolean DebugConzoleOnOpen=false;
	protected int DebugConzoleWidth=300;
	
	protected static int LoaderType=0;
	protected static final int LOADERANIM_DEFAULT=0;
	protected static final int LOADERANIM_JUMPINGBALL=1;
	protected static final int LOADERANIM_JUGGLINGBALLS=2;
	protected static final int LOADERANIM_HEARTBEAT=3;
	protected static final int LOADERANIM_DEVICESWITCH=4;
	protected static final int LOADERANIM_METALGEARSOLID=5;
	protected static final int LOADERANIM_ROTATINGBOXES=6;
	protected static String L1Color="light-blue";
	protected static String L1ColorIntensity="";
	protected static String L1ColorTemp="light-blue";
	protected static String L1ColorTempIntensity="";
	protected static String L1Text="NOW LOADING";
	protected static String L1TextTemp="NOW LOADING";
	protected static String L2B1Color="light-blue";
	protected static String L2B1ColorIntensity="";
	protected static String L2B1ColorTemp="light-blue";
	protected static String L2B1ColorTempIntensity="";
	protected static String L2B2Color="orange";
	protected static String L2B2ColorIntensity="";
	protected static String L2B2ColorTemp="orange";
	protected static String L2B2ColorTempIntensity="";
	protected static String L2B3Color=ABMaterial.COLOR_PURPLE;
	protected static String L2B3ColorIntensity="";
	protected static String L2B3ColorTemp=ABMaterial.COLOR_PURPLE;
	protected static String L2B3ColorTempIntensity="";
	protected static String L3GColor=ABMaterial.COLOR_GREY;
	protected static String L3GColorIntensity="";
	protected static String L3GColorTemp=ABMaterial.COLOR_GREY;
	protected static String L3GColorTempIntensity="";	
	protected static String L3BColor="light-blue";
	protected static String L3BColorIntensity="";
	protected static String L3BColorTemp="light-blue";
	protected static String L3BColorTempIntensity="";
	protected static String L5Color="light-blue";
	protected static String L5ColorIntensity="";
	protected static String L5ColorTemp="light-blue";
	protected static String L5ColorTempIntensity="";
	protected static String L6ColorA="light-blue";
	protected static String L6ColorAIntensity="";
	protected static String L6ColorATemp="light-blue";
	protected static String L6ColorATempIntensity="";
	protected static String L6ColorB="red";
	protected static String L6ColorBIntensity="";
	protected static String L6ColorBTemp="red";
	protected static String L6ColorBTempIntensity="";
	
	private int mRightToLeft=0;
	
	protected Map<String,ABMDragDropGroup> dragDropGroups = new LinkedHashMap<String,ABMDragDropGroup>();
	protected Map<String,ABMCell> DragDropCells = new LinkedHashMap<String,ABMCell>();
	
	protected Map<String,Object> EventHandlers = new LinkedHashMap<String,Object>();
	
	protected String TrackingID="";
	protected anywheresoftware.b4a.objects.collections.List InitialCommands = new anywheresoftware.b4a.objects.collections.List();
		
	protected String loadAsync=""; //" async "; <--- does not work!!!
	protected int HeartBeatSecs=-1;
	
	public boolean NeedsJQueryUICore=false;
		
	/**
	 * Set a minimum height each cell in the group must be (so empty drop zones are visible too)	
	 */
	public void DragDropCreateGroup(String groupName, int minHeight) {
		ABMDragDropGroup ddg=new ABMDragDropGroup();
		ddg.minHeight = minHeight;
		dragDropGroups.put(groupName.toLowerCase(), ddg);
	}
	
	/**
	 * set headerComponent = null if you do not want to use it.
	 */
	public void DragDropAddZone(String groupName, String zoneDragDropName, ABMCell cell, ABMComponent headerComponent) {
		ABMDragDropGroup ddg=null;
		if (dragDropGroups.containsKey(groupName.toLowerCase())) {
			if (headerComponent!=null) {
				headerComponent.DragDropIsHeader=true;
				cell.AddComponentDD(headerComponent);
			}			
			ddg = dragDropGroups.get(groupName.toLowerCase());
			cell.DragDropName = zoneDragDropName.toLowerCase();
			cell.DragDropGroupName = groupName.toLowerCase();
			cell.DragDropMinHeight = ddg.minHeight;
			ddg.Cells.put(zoneDragDropName.toLowerCase(), cell);
		} else {
			BA.Log("No group '" + groupName + "' found. Use DragDropCreateGroup() first.");
			return;
		}
		dragDropGroups.put(groupName.toLowerCase(), ddg);
	}
	
	public anywheresoftware.b4a.objects.collections.List GetAllDragDropComponentsFromGroup(String groupName) {
		anywheresoftware.b4a.objects.collections.List ret = new anywheresoftware.b4a.objects.collections.List();
		ret.Initialize();
		if (dragDropGroups.containsKey(groupName.toLowerCase())) {
			ABMDragDropGroup ddg = dragDropGroups.get(groupName.toLowerCase());
			for (Entry<String,ABMCell> entry: ddg.Cells.entrySet()) {
				ABMCell c = entry.getValue();
				for (Entry<String, ABMComponent> comp : c.Components.entrySet()) {
					if (comp.getValue().Type == ABMaterial.UITYPE_ABMCONTAINER) {
						ABMContainer cont = (ABMContainer) comp.getValue();
						if (cont.DragDropGroupNames.containsKey(groupName.toLowerCase())) {
							ret.Add(comp.getValue());
						}
					}
				}
			}			
		} else {
			BA.LogError("No group '" + groupName + "found!");
			return ret;			
		}
		return ret;
	}
	
	public void setRightToLeft(boolean rtl) {
		if (rtl) {
			mRightToLeft=1;
		} else {
			mRightToLeft=-1;
		}
	}
	
	public boolean getRightToLeft() {
		if (mRightToLeft==0) {
			return ABMaterial.RightToLeft;
		} else {
			if (mRightToLeft==1) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * Enable/Disable the main section from being printed 
	 */
	public void setIsPrintableMain(Boolean printme) {
		if (ws!=null) {
			try {
				if (printme) {
					ws.Eval("$('main').removeClass('no-print');", null);
				} else {
					ws.Eval("$('main').addClass('no-print');", null);
				}
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("IsPrintableMain");}
					ws.Flush();RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}			
		}
	}
	
	public void setAlwaysShowVerticalScrollBar(boolean bool) {
		mAlwaysShowVerticalScrollBar=bool;
		if (ws!=null) {
			try {
				if (bool) {
					ws.Eval("$('#print-body').addClass('alwaysoverflow');", null);
				} else {
					ws.Eval("$('#print-body').removeClass('alwaysoverflow');", null);
				}
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("AlwaysShowVerticalScrollBar");}
					ws.Flush();RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}			
		}
	}
	
	public boolean getAlwaysShowVerticalScrollBar() {
		return mAlwaysShowVerticalScrollBar;
	}
	
	public void setNeverShowVerticalScrollBar(boolean bool) {
		mNeverShowVerticalScrollBar=bool;
		if (ws!=null) {
			try {
				if (bool) {
					ws.Eval("$('#print-body').addClass('neveroverflow');", null);
				} else {
					ws.Eval("$('#print-body').removeClass('neveroverflow');", null);
				}
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("NeverShowVerticalScrollBar");}
					ws.Flush();RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}			
		}
	}
	
	public boolean getNeverShowVerticalScrollBar() {
		return mNeverShowVerticalScrollBar;
	}
	
	/**
	 * Only valid AFTER running the UpdateFromCache() method
	 */
	public boolean IsPhone() {
		return mIsPhone;
	}
	
	/**
	 * Only valid AFTER running the UpdateFromCache() method
	 */
	public boolean IsTablet() {
		return mIsTablet;
	}
	
	public String GetPageIDDebug() {
		return mPageID;
	}
		
	/**
	 * Only valid AFTER running the UpdateFromCache() method
	 */
	public boolean IsDesktop() {
		return ( mIsPhone==false && mIsTablet == false);
	}
	
	public anywheresoftware.b4a.objects.collections.List GetAllDragDropComponentsFromGroupZone(String groupName, String zoneDragDropName) {
		anywheresoftware.b4a.objects.collections.List ret = new anywheresoftware.b4a.objects.collections.List();
		ret.Initialize();
		if (dragDropGroups.containsKey(groupName.toLowerCase())) {
			ABMDragDropGroup ddg = dragDropGroups.get(groupName.toLowerCase());
			ABMCell c = ddg.Cells.getOrDefault(zoneDragDropName.toLowerCase(), null);
			if (c==null) {
				BA.LogError("No zone '" + zoneDragDropName+ "' found in group '" + groupName + "!");
				return ret;
			}
			for (Entry<String, ABMComponent> comp : c.Components.entrySet()) {
				if (comp.getValue().Type == ABMaterial.UITYPE_ABMCONTAINER) {
					ABMContainer cont = (ABMContainer) comp.getValue();
					if (cont.DragDropGroupNames.containsKey(groupName.toLowerCase())) {
						ret.Add(comp.getValue());
					}
				}
			}						
		} else {
			BA.LogError("No group '" + groupName + "found!");
			return ret;
		}
		return ret;
	}
	
	@Hide
	public B4AClass getCurrentContentPageClass() {
		return mCurrentContentPageClass;
	}
		
	/** 
	 * returns a list of ABMComponents.  
	 * You will need to check its type and Cast it to the correct ABMComponent to access its properties and methods.
	 * 
	 * Note: for a ModalSheet use myModal.Content.GetAllComponents 
	 * 
	 * e.g. 
	 * Dim comps as List = Page.GetAllComponents
	 * for i = 0 to comps.size - 1
	 *      Dim comp As ABMComponent = comps.Get(i)
	 * 		Select case comp.Type
	 * 			Case ABM.UITYPE_LABEL
	 *              Dim lbl as ABMLabel = ABM.CastABMComponent(comp)
	 *              ...
	 *          Case ABM.UITYPE_CARD
	 *              Dim card as ABMCard = ABM.CastABMComponent(card)
	 *              ...
	 *          Case ...
	 *          
	 *      End Select
	 * next
	 */
	public anywheresoftware.b4a.objects.collections.List GetAllComponents() {
		anywheresoftware.b4a.objects.collections.List ret = new anywheresoftware.b4a.objects.collections.List();
		ret.Initialize();
		for (Entry<String,ABMRowCell>entry: AllComponents.entrySet()) {
			ABMRowCell rc = entry.getValue();			
			ABMCell ccell = CellInternal(rc.rowId, rc.cellId);
			if (ccell!=null) {
				for (Entry<String,ABMComponent> comps: ccell.Components.entrySet()) {
					String componentId = comps.getValue().extra + comps.getValue().ArrayName.toLowerCase() + comps.getValue().ID.toLowerCase();
					ret.Add(ccell.Component(componentId));
					//ret.Add(comps.getValue())
				}
			}
		}
		return ret;
	}
	
	/**
	 * for type use the ABM.UITYPE_ constants
	 * 
	 * returns a list of components of that type
	 * You will need to check its type and Cast it to the correct ABMComponent to access its properties and methods.
	 * 
	 * Note: for a ModalSheet use myModal.Content.GetAllComponentsOfType(ABM.UITYPE_LABEL)
	 *  
	 * e.g.
	 * Dim Labels as List = Page.GetAllComponentsOfType(ABM.UITYPE_LABEL)
	 * for i = 0 to Labels.size - 1
	 *      Dim lbl As ABMLabel = ABM.CastABMComponent(Labels.Get(i))
	 *      ...
	 * next
	 */
	public anywheresoftware.b4a.objects.collections.List GetAllComponentsOfType(int type) {
		anywheresoftware.b4a.objects.collections.List ret = new anywheresoftware.b4a.objects.collections.List();
		ret.Initialize();
		for (Entry<String,ABMRowCell>entry: AllComponents.entrySet()) {
			ABMRowCell rc = entry.getValue();			
			ABMCell ccell = CellInternal(rc.rowId, rc.cellId);
			if (ccell!=null) {
				for (Entry<String,ABMComponent> comps: ccell.Components.entrySet()) {
					if (comps.getValue().Type==type) {
						String componentId = comps.getValue().extra + comps.getValue().ArrayName.toLowerCase() + comps.getValue().ID.toLowerCase();
						ret.Add(ccell.Component(componentId));
						//ret.Add(comps.getValue());
					}
				}
			}
		}
		return ret;
	}
	
	protected String GetPrintSize() {
		if (mPrintPageLandscape) {
			switch (mPrintPageSize) {
			case ABMaterial.PRINT_SIZE_A3:				
				return "420mm";
			case ABMaterial.PRINT_SIZE_A5:				
				return "210mm";
			case ABMaterial.PRINT_SIZE_LETTER:
				return "216mm";
			default: // A4
				return "297mm";
			}
		} else {
			switch (mPrintPageSize) {
			case ABMaterial.PRINT_SIZE_A3:				
				return "297mm";
			case ABMaterial.PRINT_SIZE_A5:				
				return "148mm";
			case ABMaterial.PRINT_SIZE_LETTER:
				return "216mm";
			default: // A4
				return "210mm";
			}
		}
	}
	
	/**
	 * pageSize: see the ABMaterial.PRINT_SIZE_ constants. Default = A4
	 */
	public void setPrintPageSize(String pageSize) {
		mPrintPageSize=pageSize;
		if (ws!=null) {
			StringBuilder s = new StringBuilder();
			if (mPrintPageLandscape) {
				s.append("$('#print-id').html('@page { size: " + mPrintPageSize + " landscape}');");
			} else {
				s.append("$('#print-id').html('@page { size: " + mPrintPageSize + "}');");
			}
			
			String print = mPrintPageSize + " ";
			String printSizeMM=GetPrintSize();
	        if (mPrintPageLandscape) {
	          	print += "landscape ";
	        } 
			if (mAlwaysShowVerticalScrollBar) {
				s.append("$('#print-body').attr('class', '" + print + ABMaterial.GetColorStr(Theme.BackColor,Theme.BackColorIntensity, "") + " alwaysoverflow');");
	        } else {
	        	if (mNeverShowVerticalScrollBar) {
	        		s.append("$('#print-body').attr('class', '" + print + ABMaterial.GetColorStr(Theme.BackColor,Theme.BackColorIntensity, "") + " neveroverflow'');");
	        	} else {
	        		s.append("$('#print-body').attr('class', '" + print + ABMaterial.GetColorStr(Theme.BackColor,Theme.BackColorIntensity, "") + "');");
	        	}
	        }
			s.append("$('#print-body').attr('data-print', '" + printSizeMM + "');");
			
			ws.Eval(s.toString(), null);
			try {
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("PrintPageSize");}
					ws.Flush();RunFlushed();				
				}
			} catch (IOException e) {			
				
			}
		}
	}
	
	/**
	 * Print in landscape mode (does not work in all browsers)  
	 */	
	public void setPrintPageLandscape(boolean landscape) {
		mPrintPageLandscape = landscape;
		if (ws!=null) {
			StringBuilder s = new StringBuilder();
			if (mPrintPageLandscape) {
				s.append("$('#print-id').html('@page { size: " + mPrintPageSize + " landscape}');");
			} else {
				s.append("$('#print-id').html('@page { size: " + mPrintPageSize + "}');");
			}
			
			String print = mPrintPageSize + " ";
			String printSizeMM=GetPrintSize();
	        if (mPrintPageLandscape) {
	          	print += "landscape ";
	        }
			if (mAlwaysShowVerticalScrollBar) {
				s.append("$('#print-body').attr('class', '" + print + ABMaterial.GetColorStr(Theme.BackColor,Theme.BackColorIntensity, "") + " alwaysoverflow');");
	        } else {
	        	if (mNeverShowVerticalScrollBar) {
	        		s.append("$('#print-body').attr('class', '" + print + ABMaterial.GetColorStr(Theme.BackColor,Theme.BackColorIntensity, "") + " neveroverflow');");
	        	} else {
	        		s.append("$('#print-body').attr('class', '" + print + ABMaterial.GetColorStr(Theme.BackColor,Theme.BackColorIntensity, "") + "');");
	        	}
	        }
			s.append("$('#print-body').attr('data-print', '" + printSizeMM + "');");
			
			ws.Eval(s.toString(), null);
			try {
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("PrintPageSize");}
					ws.Flush();RunFlushed();				
				}
			} catch (IOException e) {			
				
			}
		}
	}
	
	/**
	 * Experimental and may not work because browsers may ignore this
	 * 
	 * every method you want to call with a B4JSOn... call MUST return a boolean
	 * returning true will consume the event in the browser and not call the B4J event (if any)
	 *
	 * e.g. myButton.B4JSOnClicked("MyJavascript", "AddToLabel", Array As Object(myCounter))
	 * if AddToLabel return true, then myButton_Clicked() will not be raised.
	 * if AddToLabel returns false, then myButton_Clicked() will be raised AFTER the B4JS method is done.
	 * 
	 * public Sub AddToLabel(MyCounter As Int) As Boolean
	 *      if myCounter mod 2 = 0 then	       
	 *		   Return True
	 *      else
	 *         Return False
	 *      end if
	 * End Sub
	 */
	public void B4JSOnBeforeUnload(String B4JSClassName, String B4JSMethod, anywheresoftware.b4a.objects.collections.List Params) {
		if (mB4JSUniqueKey.equals("")) {
			BA.LogError("You cannot use B4JS events before setting the B4JSUniqueKey property!");
			return;
		}
		String EventName="B4JSBeforeUnload";
		
		B4JSComponents.put(mB4JSUniqueKey + "_" + EventName, 0);
		String pars = "";
		if (Params.IsInitialized()) {
			for (int i=0;i<Params.getSize();i++) {
				if (i>0) {
					pars=pars + ", "; 
				}
				if (Params.Get(i) instanceof String) {
					pars = pars + "\"" + (String) Params.Get(i) + "\""; 
				} else {
					pars = pars + Params.Get(i);
				}
			}
		}
		StringBuilder s = new StringBuilder();
		s.append("if (_b4jsclasses['" + mB4JSUniqueKey + "'] === undefined) {");
		s.append("_b4jsclasses['" + mB4JSUniqueKey + "'] = new b4js_" + B4JSClassName.toLowerCase() + "();");
		s.append("_b4jsclasses['" + mB4JSUniqueKey + "'].initializeb4js();");
		s.append("}\n");			
		
		s.append("_b4jsvars['" + mB4JSUniqueKey + "_" + EventName + "']='" + B4JSMethod.toLowerCase() + "(" + pars + ")';");
		ws.Eval(s.toString() , null);
				
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public String B4JSGetComponentIDFromUniqueID(String uniqueID) {
		String ret = "";
		if (ws!=null) {
			SimpleFuture fut = ws.EvalWithResult("$('[data-b4js=\"" + uniqueID.toLowerCase() + "\"]').attr('id');", null);
			
			try {				
				ret = (String) fut.getValue();				
			} catch (InterruptedException e) {				
			} catch (TimeoutException e) {						
			} catch (ExecutionException e) {						
			} catch (IOException e) {						
			}
		}
		return ret;
	}
	
	/**
	 * name: MUST start with abm-, e.g. abm-myspecial-icon
	 * 
	 * Your SVG tag looks typical like this: (ALL properties are required!)
	 * 
	 * &lt;svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 1024 1024" style="margin-top: 14px;fill: black"&gt;...&lt;/svg&gt;
	 * 
	 * Note that in this case the styling must be done in the svgTagContent
	 * 
	 */
	public void AddEmbeddedSVGIcon(String name, String svgTagContent) {
		svgIconmap.put(name.toLowerCase(), "<span>" + svgTagContent.replace("\n", "").replace("\r", "") + "</span>");
	}
	
	/**
	 * 
	 * name: MUST start with abm-, e.g. abm-myspecial-icon, so make sure the &lt;symbol> id is ALSO the same! (same case too)
	 * 
	 * Your SVG tag looks typical like this: (ALL properties are required!)
	 * 
	 * &lt;svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.1" style="width:0;height:0;position:absolute;overflow:hidden;"&gt;...&lt;/svg&gt;
	 * 
	 * In the SVG tag there must be a &lt;symbol&gt; tag with an id property.  This id property MUST be the same as the name you passed to AddSVGIcon!
	 * You must provide the target width and height and an optional styling e.g. "margin-top: 14px;fill: black"
	 * 
	 * IMPORTANT: as this is a link, such an SVG icon MUST be added in the BuildPage() method!!!
	 * 
	 * You can find 10.000+ of such icons here: https://leungwensen.github.io/svg-icon/
	 * 
	 */
	public void AddSVGIcon(String name, String svgTagContent, int width, int height, String style) {
		svgIconmap.put(name.toLowerCase(), "<span><svg width=\"" + width + "\" height=\"" + height +"\" style=\"" + style + "\"><use xlink:href=\"#" + name + "\"/></svg></span>");
		svgIconmapContent.put(name.toLowerCase(), svgTagContent.replace("\n", "").replace("\r", ""));
	}
	
	/**
	 * 
	 * name: MUST start with abm-, e.g. abm-myspecial-icon
	 * 
	 * you must provide the target width and height and an optional styling e.g. "margin-top: 14px;fill: black"
	 * 
	 * You can also set the 'alt' property of the image.
	 * 
	 */	
	public void AddImageIcon(String name, String src, int width, int height, String style, String alt) {
		svgIconmap.put(name.toLowerCase(), "<span><img width=\"" + width + "\" height=\"" + height +"\" src=\"" + src + "\" style=\"" + style + "\" alt=\"" + alt + "\"></span>");
	}	
	
	/**
	 * Print the current web page
	 */
	public void PrintPage() {
		//window.print();
		if (ws!=null) {
			ws.Eval("window.print();", null);
			try {
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("PrintPage");}
					ws.Flush();RunFlushed();				
				}
			} catch (IOException e) {			
				
			}
		}
	}
		
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */ 	 
	public void B4JSPrintPage() {
		
	}
	
	public String GetPageID() {
		if (mPageID.equals("")) {
			mPageID = ABMaterial.GetPageID(this, Name, ws);
		}
		return mPageID;
	}
	
	protected void RunFlushed() {
	
	}
	
			
	/**
	 * Returns empty if no multiple themes where used 
	 */
	public String GetCurrentThemeName() {
		if (this.CompleteThemes.size()==0) {
			return "";
		}
		return this.CompleteTheme.Name;
	}
	
	public String GetProtocol() {
		String ret = "";
		if (ws!=null) {
			SimpleFuture fut = ws.EvalWithResult("return location.protocol;", null);
			
			try {				
				ret = (String) fut.getValue();				
			} catch (InterruptedException e) {				
			} catch (TimeoutException e) {						
			} catch (ExecutionException e) {						
			} catch (IOException e) {						
			}
		}
		return ret;
	}
	
	/**
	 * Returns the current page size (phone, tablet, desktop)
	 * You can use this method in ConnectPage to determine the state of the page now
	 * 
	 * An event Page_SizeChanged will return this value in the current parameter, IF the user changes the window size.
	 * It also returns the previous state. 
	 * 
	 * Note that this event is NOT raised at load time!
	 */
	public String GetCurrentPageSize() {
		String ret = "";
		if (ws!=null) {
			SimpleFuture fut = ws.RunFunctionWithResult("getcurrentsizestr", null);
			
			try {				
				ret = (String) fut.getValue();				
			} catch (InterruptedException e) {				
			} catch (TimeoutException e) {						
			} catch (ExecutionException e) {						
			} catch (IOException e) {						
			}
		}
		return ret;
	}
	
	public void SetAcceptedLanguages(anywheresoftware.b4a.objects.collections.List acceptedLanguages, String defaultLanguage) {
		for (int i=0;i<acceptedLanguages.getSize();i++) {
			AcceptedLanguages.add((String) acceptedLanguages.Get(i));
		}
		DefaultLanguage=defaultLanguage;
		ActiveLanguage=defaultLanguage;
	}
		
	public String DetectLanguage(String BrowserLanguages) {
		if (ws!=null) {
			if (BrowserLanguages!="") {
				String[] Languages = Regex.Split(",",BrowserLanguages);
				for (int i=0;i<Languages.length;i++) {
					for (int j=0;j<AcceptedLanguages.size();j++) {
						if (Languages[i].toLowerCase().contains(AcceptedLanguages.get(j).toLowerCase())) {
							return AcceptedLanguages.get(j);
						}
					}
				}				
			}			
		}
		return DefaultLanguage;
	}
		
	/**
	 * Returns one of the ABM.NATIVE_PLATFORM_ constants
	 */
	public String NativePlatform() {
		if (NativePlatform.equals("NOTSET")) {
			if (ws!=null) {
				anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
				Params.Initialize();
				SimpleFuture j = this.ws.RunFunctionWithResult("getnative", Params);
				if (j!=null) {					
						try {
							NativePlatform = (String) j.getValue();
						} catch (InterruptedException e) {							
						} catch (TimeoutException e) {							
						} catch (ExecutionException e) {
						} catch (IOException e) {
						}
				}		
			}
		}
		return NativePlatform;
	}
		
	public boolean NativeRequest(String jsonMessage) {
		String s= "";
		if (NativePlatform.equals("NOTSET")) {
			if (ws!=null) {
				anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
				Params.Initialize();
				SimpleFuture j = this.ws.RunFunctionWithResult("getnative", Params);
				if (j!=null) {					
						try {
							NativePlatform = (String) j.getValue();
						} catch (InterruptedException e) {							
						} catch (TimeoutException e) {							
						} catch (ExecutionException e) {
						} catch (IOException e) {
						}
				}		
			}
		}
		BA.Log("NativePlatform: " + NativePlatform);
		boolean ret=false;
		switch (NativePlatform) {
		case ABMaterial.NATIVE_PLATFORM_B4J:
			s = "NativeRequest('" + jsonMessage +"')";
			ret = true;
			break;
		case ABMaterial.NATIVE_PLATFORM_B4A:
			s = "B4A.CallSub('NativeRequest', true, '" + jsonMessage +"')";
			ret = true;
			break;
		case ABMaterial.NATIVE_PLATFORM_B4i:
			BA.Log("B4i is not yet available!");
			s = "";
			break;
		default:
			BA.Log("This webapp is not running as a native app!");
			break;
		}
		
		if (ws!=null) {
			ws.Eval(s.toString(), null);
			try {
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("NativeRequest");}
					ws.Flush();RunFlushed();
				} else {
					ret = false;
				}
			} catch (IOException e) {			
				ret = false;
			}
		}
		return ret;
	}
	
	/**
	 * Marks in the page the text entered in the SearchButton on the Navigation Bar (full body search)
	 */
	public void MarkSearchInPage() {		
			if (ws!=null) {
				ws.Eval("var keyword = $(\"#abmnavextrasearch\").val(); var options = {}; $(\"body\").unmark({ done: function() { $(\"body\").mark(keyword, options); }});", null);
				try {
					if (ws.getOpen()) {
						ws.Flush();						
					}
				} catch (IOException e) {			
					e.printStackTrace();
				}
			}		
	}
	
	/**
	 * Marks in the page the text entered in the SearchButton on the Navigation Bar (search in specific html ID, case sensitive!)
	 */
	public void MarkSearchInHtmlID(String htmlID) {		
			if (ws!=null) {
				ws.Eval("var keyword = $(\"#abmnavextrasearch\").val(); var options = {}; $(\"#" + htmlID + "\").unmark({ done: function() { $(\"#" + htmlID + "\").mark(keyword, options); }});", null);
				try {
					if (ws.getOpen()) {
						ws.Flush();						
					}
				} catch (IOException e) {			
					e.printStackTrace();
				}
			}		
	}
	
	/**
	 * Marks in the page the search string passed in the method (full body search)
	 */
	public void MarkSearchInPage2(String search) {
		if (ws!=null) {
				ws.Eval("var options = {}; $(\"body\").unmark({ done: function() { $(\"body\").mark(\"" + search + "\", options); }});", null);
				try {
					if (ws.getOpen()) {
						ws.Flush();						
					}
				} catch (IOException e) {			
					e.printStackTrace();
				}
			}	
	}
	
	/**
	 * Marks in the page the text entered in the SearchButton on the Navigation Bar (search in specific html ID, case sensitive!)
	 */
	public void MarkSearchInHtmlID2(String htmlID, String search) {		
			if (ws!=null) {
				ws.Eval("var options = {}; $(\"#" + htmlID + "\").unmark({ done: function() { $(\"#" + htmlID + "\").mark(\"" + search + "\", options); }});", null);
				try {
					if (ws.getOpen()) {
						ws.Flush();						
					}
				} catch (IOException e) {			
					e.printStackTrace();
				}
			}		
	}
	
	
	/**
	 * Unmarks the previously marked search 
	 */
	public void UnMarkSearchInPage() {		
			if (ws!=null) {
				ws.Eval("$(\"body\").unmark({ done: function() {}});", null);
				try {
					if (ws.getOpen()) {
						ws.Flush();						
					}
				} catch (IOException e) {			
					e.printStackTrace();
				}
			
			}
		
	}
	
	/**
	 * Unmarks the previously marked search 
	 */
	public void UnMarkSearchInHTMLID(String htmlID) {		
			if (ws!=null) {
				ws.Eval("$(\"#" + htmlID + "\").unmark({ done: function() {}});", null);
				try {
					if (ws.getOpen()) {
						ws.Flush();						
					}
				} catch (IOException e) {			
					e.printStackTrace();
				}
			
			}
		
	}
	
	/**
	 * You can use extra to make specific files (e.g. for a specific client)
	 * e.g.  using SetActiveLanguage("en", "myclient") will do the following:
	 * 
	 * 1. reading en.lng
	 * 2. reading en_myclient.lng (overwiting all entries "en.lng" had.
	 * 
	 * ---> en.lng
	 * thispage_0001;Name
	 * thispage_0002;Address
	 * thispage_0003;Persons
	 * thispage_0004;Articles
	 * 
	 * ---> en_myclient.lng
	 * thispage_0003;Employees
	 * 
	 */
	public void SetActiveLanguage(String language, String extra) {
		ActiveSubLanguage = (language + "_" + extra).toLowerCase();
		ActiveLanguage = (language).toLowerCase();		
	}
	
	/**
	 * You can use extra to make specific files (e.g. for a specific client)
	 * e.g.  using SetActiveDefaults("alldefaults", "myclient") will do the following:
	 * 
	 * 1. reading alldefaults.def
	 * 2. reading alldefaults_myclient.def (overwiting all entries "alldefaults.def" had.
	 * 
	 * ---> alldefaults.def
	 * thispage_0001;litre
	 * thispage_0002;km
	 * thispage_0003;cm
	 * thispage_0004;metre
	 * 
	 * ---> alldefaults_myclient.def
	 * thispage_0002;miles
	 * 
	 */
	public void SetActiveDefaults(String defaultsCode, String extra) {
		ActiveDef = (defaultsCode + "_" + extra).toLowerCase();
	}	
	
	public String GetActiveLanguage() {
		return ActiveLanguage;
	}
	
	public String XTR(BA caller, String Code, String Text) {
		String tmpActiveLanguage=ActiveLanguage;
		String tmpActiveSubLanguage = ActiveSubLanguage;
		String prefix = caller.getClassNameWithoutPackage();
		if (ABMaterial.Trans.containsKey(tmpActiveSubLanguage)) {
			return ABMaterial.Trans.get(tmpActiveSubLanguage).getOrDefault(prefix + "_" + Code, Text);
		}
		if (ABMaterial.Trans.containsKey(tmpActiveLanguage)) {
			return ABMaterial.Trans.get(tmpActiveLanguage).getOrDefault(prefix + "_" + Code, Text);
		}
		return Text;
	}
	
	public String XDF(BA caller, String Code, String Default) {
		String tmpActiveDef=ActiveDef;
		String prefix = caller.getClassNameWithoutPackage();
		if (ABMaterial.Defs.containsKey(tmpActiveDef)) {
			return ABMaterial.Defs.get(tmpActiveDef).getOrDefault(prefix + "_" + Code, Default);
		}
		return Default;
	}
	
	public void InjectCSS(String css) {
		if (ws!=null) {
			css = css.replaceAll("\"", "'").replaceAll("\t", " ").replaceAll("\r",  "").replaceAll("\n",  " ");
			while (css.indexOf("  ")>-1) {
				css = css.replaceAll("  ", " ");
			}
			String script = "document.querySelector('style').textContent += \"" + css + "\";"; 
			ws.Eval(script, null);
			try {
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("InjectCSS");}
					ws.Flush();RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
	}
	
	public void DebugConsoleEnable(boolean openOnLoad, int width) {
		DebugConzole = true;
		DebugConzoleOnOpen = openOnLoad;
		DebugConzoleWidth = width;
	}
		
	public void ShowCookiesMessage(String messageText, String BackColorHex, String messageColorHex, String messageLinkColorHex, String CloseButtonBackColorHex, String CloseButtonForeColorHex, String acceptText, String infoText, String infoUrl, int expireDays) {
		StringBuilder s = new StringBuilder();
		
		s.append("$.CookiesMessage({");
		s.append("messageText: \"" + messageText + "\",");
		s.append("messageBg: \"#" + BackColorHex + "\",");
		s.append("messageColor: \"#" + messageColorHex + "\",");
		s.append("messageLinkColor: \"#" + messageLinkColorHex + "\",");
		s.append("closeEnable: true,");
		s.append("closeColor: \"#" + CloseButtonForeColorHex + "\",");
		s.append("closeBgColor: \"#" + CloseButtonBackColorHex + "\",");
		s.append("acceptEnable: true,");
		s.append("acceptText: \"" + acceptText + "\",");
		s.append("infoEnable: true,");
		s.append("infoText: \"" + infoText + "\",");
		s.append("infoUrl: \"" + infoUrl + "\",");
		s.append("cookieExpire: " + expireDays + "");
		s.append("});");
		if (ws!=null) {
			ws.Eval(s.toString(), null);
			try {
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("ShowCookiesMessage");}
					ws.Flush();RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * You must set DebugConsoleEnable in BuildPage() first!
	 */
	public void DebugConsoleShow() {
		if (!DebugConzole) {
			BA.LogError("You must set DebugConsoleEnable in BuildPage() first!");
			return;
		}
		if (ws!=null) {
			ws.Eval("conzole.open();\nconzole.setWidth(" + DebugConzoleWidth + ");", null);
			try {
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("DebugConsoleShow");}
					ws.Flush();RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
	}
	
	/**
	 * You must set DebugConsoleEnable in BuildPage() first!
	 */
	public void DebugConsoleHide() {
		if (!DebugConzole) {
			BA.LogError("You must set DebugConsoleEnable in BuildPage() first!");
			return;
		}
		if (ws!=null) {
			ws.Eval("conzole.close();", null);
			try {
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("DebugConsoleHide");}
					ws.Flush();RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
	}
	
	/**
	 * You must set DebugConsoleEnable in BuildPage() first!
	 * Note: String must be between single quotes! 
	 * e.g. page.DebugConsoleLog("'hello'");
	 */
	public void DebugConsoleLog(String s) {
		if (!DebugConzole) {
			BA.LogError("You must set DebugConsoleEnable in BuildPage() first!");
			return;
		}
		if (ws!=null) {
			ws.Eval("console.log(" + s + ");", null);
			try {
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("DebugConsoleLog");}
					ws.Flush();RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * You must set DebugConsoleEnable in BuildPage() first!
	 * You can only start the B4J log AFTER the websocket is connected.
	 * A good place to use this method could be in the WebSocket_Connected() event, after ABM.UpdateFromCache
	 */
	public void DebugConsoleB4JStart() {
		if (!DebugConzole) {
			BA.LogError("You must set DebugConsoleEnable in BuildPage() first!");
			return;
		}
		if (ws!=null) {
			ws.Eval("conzole.start();", null);
			try {
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("DebugConsoleLog");}
					ws.Flush();RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
	}
	
	/**
	 * You must set DebugConsoleEnable in BuildPage() first!
	 * A good place to use this method could be before you navigate to another page.
	 */
	public void DebugConsoleB4JStop() {
		if (!DebugConzole) {
			BA.LogError("You must set DebugConsoleEnable in BuildPage() first!");
			return;
		}
		if (ws!=null) {
			ws.Eval("conzole.stop();", null);
			try {
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("DebugConsoleLog");}
					ws.Flush();RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
	}
	
	public void ScrollToRow(int row, int scrollTop, int speed) {
		ABMRow r = Row(row);
		if (r==null) {
			BA.Log("Row " + row + " not found!");
			return;
		}
		String scrollID = "";
		if (r.CenterInPage) {
			scrollID = r.ParentString + r.ArrayName.toLowerCase() + r.ID.toLowerCase() + "_cont";
		} else {
			scrollID = r.ParentString + r.ArrayName.toLowerCase() + r.ID.toLowerCase();
		}
		
		if (ws!=null) {				
			ws.Eval("$('html, body').animate({scrollTop: ($('#" + scrollID +"').offset().top - " + scrollTop + ")}," + speed + ");", null);				
			try {
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("ScrollToRow");}
					ws.Flush();RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
		
	}
	
	public void ScrollToComponent(ABMComponent component, int scrollTop, int speed) {
		String scrollID = component.ParentString + component.RootID();
		if (ws!=null) {				
			ws.Eval("$('html, body').animate({scrollTop: ($('#" + scrollID +"').offset().top - " + scrollTop + ")}," + speed + ");", null);				
			try {
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("ScrollToComponent");}
					ws.Flush();RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
	}
	
	public void ScrollToSection(String sectionID, int scrollTop, int speed) {
		if (ws!=null) {				
			ws.Eval("$('html, body').animate({scrollTop: ($('#" + sectionID.toLowerCase() +"').offset().top - " + scrollTop + ")}," + speed + ");", null);				
			try {
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("ScrollToSection");}
					ws.Flush();RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Can ONLY be used in the PageBuild() phase!
	 * 
	 * You can use the same constants for the background image as with an ABMContainer. 
	 * 
	 * repeat: use the ABM.CONTAINERIMAGE_REPEAT_ constants
	 * position: use the ABM.CONTAINERIMAGE_POSITION_ constants or a valid CSS position string
	 * 
	 * minHeight: can be any CSS string: e.g. calc(100vh - 56px) to make it the full page height, minus the header height.
	 * scrollTop: to where the scroll, measured from the top of the window, should scroll: e.g. 56
	 * scrollSpeed: in ms, 0 to jump without animation
	 */
	public void CreateSection(String sectionID, int fromRow, int toRow, String color, String colorIntensity, String source, String repeat, String position, String minHeight, int scrollTop, int scrollSpeed, String visibility, String themeSectionName) {
		//Section st = Theme.Sections.getOrDefault(themeSectionName.toLowerCase(), null);
		//st = new ThemePage.Section();
		String BackgroundImage="";
		String extra = "";
		if (!source.equals("")) {
			if (position.equals("cover")) {
				repeat = "no-repeat";
				position = "center center";
				extra = "-webkit-background-size: cover; -moz-background-size: cover; -o-background-size: cover; background-size: cover;";
			}
			BackgroundImage = " background: " + ABMaterial.GetColorStrMap(color, colorIntensity) + " url('" + source + "') " + repeat + "  " + position + ";" + extra;
		}
		ABMSection sect = new ABMSection(sectionID, "R" + fromRow, "R" + toRow, color, colorIntensity, BackgroundImage, minHeight, scrollTop, scrollSpeed, visibility, themeSectionName);
		Sections.add(sect);
	}
	
	/**
	 * Can ONLY be used in the PageBuild() phase!
	 * 
	 * buttonType: use the SECTION_BUTTONTYPE_ constants
	 * 
	 * Leave the jumpToSectionID empty if you do not want to use it
	 */
	public void SetSectionNavigation(String sectionID, String buttonJumpToSectionID, String buttonText, String buttonType, String menuJumpToSectionID, String menuText) {
		for (ABMSection sect: Sections) {
			if (sect.SectionName.equalsIgnoreCase(sectionID)) {
				sect.ButtonJumpToSectionID = buttonJumpToSectionID;
				sect.ButtonText = buttonText;
				sect.ButtonType = buttonType;
				sect.MenuJumpToSectionID = menuJumpToSectionID;
				sect.MenuText = menuText;
				//sect.AddToNavigationMenu = addToNavigationMenu;
				return;
			}
		}
	}
	
	public void SetSectionVisibility(String sectionID, String visibility) {
		for (ABMSection sect: Sections) {
			if (sect.SectionName.equalsIgnoreCase(sectionID)) {
				sect.Visibility = visibility;
				if (ws!=null) {
					ABMaterial.ChangeVisibility(this, sectionID.toLowerCase(), visibility);
					ABMaterial.ChangeVisibility(this, sectionID.toLowerCase() + "-section-menu", visibility);
				}
				try {
					if (ws.getOpen()) {
						if (ShowDebugFlush) {BA.Log("Section refresh");}
						ws.Flush();RunFlushed();
					}
				} catch (IOException e) {			
					//e.printStackTrace();
				}
				return;
			}
		}
	}	
	
	protected ABMSection GetIfStartSection(String row) {
		for (ABMSection sect: Sections) {
			if (sect.FromRow.equals(row)) {
				return sect;
			}
		}
		return null;
	}
	
	@Hide
	protected class ABMSection {
		protected String SectionName="";
		protected String FromRow="";
		protected String ToRow="";
		protected String Color="";
		protected String ColorIntensity="";
		protected String BackgroundImage="";
		protected String ButtonText="";
		protected String MenuText="";
		protected String ButtonJumpToSectionID="";
		protected String MenuJumpToSectionID="";
		protected String ThemeSectionName="";
		protected String MinHeight = "";
		protected int ScrollTop = 0;
		protected int ScrollSpeed = 0;
		protected String ButtonType="";
		protected String Visibility="";
		
		ABMSection(String sectionName, String fromRow, String toRow, String color, String colorIntensity, String backgroundImage, String minHeight, int scrollTop, int scrollSpeed, String visibility, String themeSectionName) {
			this.SectionName = sectionName;
			this.FromRow = fromRow;
			this.ToRow = toRow;
			this.Color = color;
			this.ColorIntensity = colorIntensity;
			this.BackgroundImage = backgroundImage;
			this.ThemeSectionName = themeSectionName;
			this.MinHeight = minHeight;
			this.ScrollTop = scrollTop;
			this.ScrollSpeed = scrollSpeed;
		}
		
		public ABMSection Clone() {
			ABMSection s = new ABMSection(SectionName, FromRow, ToRow, Color, ColorIntensity, BackgroundImage, MinHeight, ScrollTop, ScrollSpeed, Visibility, ThemeSectionName);
			s.ButtonText = ButtonText;
			s.MenuText = MenuText;
			s.ButtonType = ButtonType;
			s.ButtonJumpToSectionID = ButtonJumpToSectionID;
			s.MenuJumpToSectionID = MenuJumpToSectionID;
			return s;
		}
	}
		
	protected void UpdateAllNeeds() {
		if (NeedsCodeLabel) {			
			ABMaterial.AllNeeds.put("NeedsCodeLabel", true);
		}
		if (NeedsYouTube) {
			ABMaterial.AllNeeds.put("NeedsYouTube", true);
		}
		if (NeedsGoogleMap) {
			ABMaterial.AllNeeds.put("NeedsGoogleMap", true);
		}
		if (NeedsCanvas) {
			ABMaterial.AllNeeds.put("NeedsCanvas", true);
		}
		if (NeedsInput) {
			ABMaterial.AllNeeds.put("NeedsInput", true);
		}
		if (NeedsFileInput) {
			ABMaterial.AllNeeds.put("NeedsFileInput", true);
		}
		if (NeedsRadio) {
			ABMaterial.AllNeeds.put("NeedsRadio", true);
		}
		if (NeedsTabs) {
			ABMaterial.AllNeeds.put("NeedsTabs", true);
		}
		if (NeedsTable) {
			ABMaterial.AllNeeds.put("NeedsTable", true);
		}
		if (NeedsJQueryUI) {
			ABMaterial.AllNeeds.put("NeedsJQueryUI", true);
		}
		if (NeedsSortingTable) {
			ABMaterial.AllNeeds.put("NeedsSortingTable", true);
		}
		if (NeedsBadge) {
			ABMaterial.AllNeeds.put("NeedsBadge", true);
		}
		if (NeedsCheckbox) {
			ABMaterial.AllNeeds.put("NeedsCheckbox", true);
		}
		if (NeedsCombo) {
			ABMaterial.AllNeeds.put("NeedsCombo", true);
		}
		if (NeedsImageSlider) {
			ABMaterial.AllNeeds.put("NeedsImageSlider", true);
		}
		if (NeedsTextArea) {
			ABMaterial.AllNeeds.put("NeedsTextArea", true);
		}
		if (NeedsSwitch) {
			ABMaterial.AllNeeds.put("NeedsSwitch", true);
		}
		if (NeedsUpload) {
			ABMaterial.AllNeeds.put("NeedsUpload", true);
		}
		if (NeedsHTML5Video) {
			ABMaterial.AllNeeds.put("NeedsHTML5Video", true);
		}
		if (NeedsChips) {
			ABMaterial.AllNeeds.put("NeedsChips", true);
		}
		if (NeedsActionButton) {
			ABMaterial.AllNeeds.put("NeedsActionButton", true);
		}
		if (NeedsLists) {
			ABMaterial.AllNeeds.put("NeedsLists", true);
		}
		if (NeedsCards) {
			ABMaterial.AllNeeds.put("NeedsCards", true);
		}
		if (NeedsParallax) {
			ABMaterial.AllNeeds.put("NeedsParallax", true);
		}
		if (NeedsSignature) {
			ABMaterial.AllNeeds.put("NeedsSignature", true);
		}
		if (NeedsChart) {
			ABMaterial.AllNeeds.put("NeedsChart", true);
		}
		if (NeedsOAuth) {
			ABMaterial.AllNeeds.put("NeedsOAuth", true);
		}
		if (NeedsCalendar) {
			ABMaterial.AllNeeds.put("NeedsCalendar", true);
		}
		if (NeedsMask) {
			ABMaterial.AllNeeds.put("NeedsMask", true);
		}
		if (NeedsTreeTable) {
			ABMaterial.AllNeeds.put("NeedsTreeTable", true);
		}
		if (NeedsPagination) {
			ABMaterial.AllNeeds.put("NeedsPagination", true);
		}
		if (NeedsSlider) {
			ABMaterial.AllNeeds.put("NeedsSlider", true);
		}
		if (NeedsRange) {
			ABMaterial.AllNeeds.put("NeedsRange", true);
		}
		if (NeedsDateTimeScroller) {
			ABMaterial.AllNeeds.put("NeedsDateTimeScroller", true);
		}
		if (NeedsDateTimePicker) {
			ABMaterial.AllNeeds.put("NeedsDateTimePicker", true);
		}
		if (NeedsEditor) {
			ABMaterial.AllNeeds.put("NeedsEditor", true);
		}		
		if (NeedsSocialShare) {
			ABMaterial.AllNeeds.put("NeedsSocialShare", true);
		}
		if (NeedsPDFViewer) {
			ABMaterial.AllNeeds.put("NeedsPDFViewer", true);
		}
		if (NeedsPlatform) {
			ABMaterial.AllNeeds.put("NeedsPlatform", true);
		}
		if (NeedsAudioPlayer) {
			ABMaterial.AllNeeds.put("NeedsAudioPlayer", true);
		}
		if (NeedsTimeline) {
			ABMaterial.AllNeeds.put("NeedsTimeline", true);
		}
		if (NeedsFlexWall) {
			ABMaterial.AllNeeds.put("NeedsFlexWall", true);
		}
		if (NeedsSVGSurface) {
			ABMaterial.AllNeeds.put("NeedsSVGSurface", true);
		}
		if (NeedsPatternLock) {
			ABMaterial.AllNeeds.put("NeedsPatternLock", true);
		}
		if (NeedsChronologyList) {
			ABMaterial.AllNeeds.put("NeedsChronologyList", true);
		}
		if (NeedsXPlay) {
			ABMaterial.AllNeeds.put("NeedsXPlay", true);
		}
		if (NeedsCustomCards) {
			ABMaterial.AllNeeds.put("NeedsCustomCards", true);
		}		
		if (NeedsChat) {
			ABMaterial.AllNeeds.put("NeedsChat", true);
		}
		if (NeedsPlanner) {
			ABMaterial.AllNeeds.put("NeedsPlanner", true);
		}
		if (NeedsPivot) {
			ABMaterial.AllNeeds.put("NeedsPivot", true);
		}
		if (NeedsPercentSlider) {
			ABMaterial.AllNeeds.put("NeedsPercentSlider", true);
		}
		if (NeedsSmartWizard) {
			ABMaterial.AllNeeds.put("NeedsSmartWizard", true);
		}
		if (NeedsSimpleBar) {
			ABMaterial.AllNeeds.put("NeedsSimpleBar", true);
		}
		if (NeedsComposer) {
			ABMaterial.AllNeeds.put("NeedsComposer", true);
		}
		if (NeedsFileManager) {
			ABMaterial.AllNeeds.put("NeedsFileManager", true);
		}
		if (NeedsResponsiveContainer) {
			ABMaterial.AllNeeds.put("NeedsResponsiveContainer", true);
		}
		if (NeedsB4JS) {
			ABMaterial.AllNeeds.put("NeedsB4JS", true);
		}		
	}
	
	public void CloseAnySidebar() {
		ws.Eval("$('#sidenav-overlay-sidebar').trigger('click')", null);
		try {
			if (ws.getOpen()) {
				if (ShowDebugFlush) {BA.Log("CloseAnySidebar");}
				ws.Flush();RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	public void ShowSideBar(String sidebarID) {
		ws.Eval("if(($('#sidenav-overlay-sidebar').length == 0)) {$('body').append($('<div id=\"sidenav-overlay-sidebar\" style=\"opacity:0;\"></div>'));$('#btnextrasidebar" + sidebarID.toLowerCase() + "').trigger('click');$('#sidenav-overlay-sidebar').remove();} else {if ($('#" + sidebarID.toLowerCase() + "').css('right').indexOf('-')>-1){$('#btnextrasidebar" + sidebarID.toLowerCase() + "').trigger('click');$('#sidenav-overlay-sidebar').remove();}}", null);
		try {
			if (ws.getOpen()) {
				if (ShowDebugFlush) {BA.Log("Sidebar Refresh: " + sidebarID);}
				ws.Flush();RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
		
	public void ToggleSideBar(String sidebarID) {
		ws.Eval("if($('#sidenav-overlay-sidebar').length == 0) {$('body').append($('<div id=\"sidenav-overlay-sidebar\" style=\"opacity:0;\"></div>'));}$('#btnextrasidebar" + sidebarID.toLowerCase() + "').trigger('click');$('#sidenav-overlay-sidebar').remove();", null);
		try {
			if (ws.getOpen()) {
				if (ShowDebugFlush) {BA.Log("Sidebar Refresh: " + sidebarID);}
				ws.Flush();RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	
	public void CloseSideBar(String sidebarID) {
		ws.Eval("$('#btnextrasidebar" +  sidebarID.toLowerCase() + "').abmsideNav('hide');", null);
		try {
			if (ws.getOpen()) {
				if (ShowDebugFlush) {BA.Log("Sidebar Refresh: " + sidebarID);}
				ws.Flush();RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */ 
	public void B4JSShowSideBar(String sidebarID) {
		ws.Eval("if(($('#sidenav-overlay-sidebar').length == 0)) {$('body').append($('<div id=\"sidenav-overlay-sidebar\" style=\"opacity:0;\"></div>'));$('#btnextrasidebar" + sidebarID.toLowerCase() + "').trigger('click');$('#sidenav-overlay-sidebar').remove();} else {if ($('#" + sidebarID.toLowerCase() + "').css('right').indexOf('-')>-1){$('#btnextrasidebar" + sidebarID.toLowerCase() + "').trigger('click');$('#sidenav-overlay-sidebar').remove();}}", null);
		try {
			if (ws.getOpen()) {
				if (ShowDebugFlush) {BA.Log("Sidebar Refresh: " + sidebarID);}
				ws.Flush();RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */ 
	public void B4JSToggleSideBar(String sidebarID) {
		ws.Eval("if($('#sidenav-overlay-sidebar').length == 0) {$('body').append($('<div id=\"sidenav-overlay-sidebar\" style=\"opacity:0;\"></div>'));}$('#btnextrasidebar" + sidebarID.toLowerCase() + "').trigger('click');$('#sidenav-overlay-sidebar').remove();", null);
		try {
			if (ws.getOpen()) {
				if (ShowDebugFlush) {BA.Log("Sidebar Refresh: " + sidebarID);}
				ws.Flush();RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */ 
	public void B4JSCloseSideBar(String sidebarID) {
		ws.Eval("$('#btnextrasidebar" +  sidebarID.toLowerCase() + "').abmsideNav('hide');", null);
		try {
			if (ws.getOpen()) {
				if (ShowDebugFlush) {BA.Log("Sidebar Refresh: " + sidebarID);}
				ws.Flush();RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	public ABMSideBar GetSideBar(String topItemReturnName) {
		if (navigationBar!=null) {
			return navigationBar.GetSideBarFromTopItem(topItemReturnName);
		}
		return null;
	}
	
	
	public ABMFirebase Firebase() {
		this.UsesFirebase = true;
		if (mFirebase.internalIsInitialized==false) {
			mFirebase.SetPage(this);
		}
		return mFirebase;
	}
	
	protected void GetAllNeeds() {
		NeedsCodeLabelAll=ABMaterial.AllNeeds.getOrDefault("NeedsCodeLabel", false);
		NeedsYouTubeAll=ABMaterial.AllNeeds.getOrDefault("NeedsYouTube", false);
		NeedsGoogleMapAll=ABMaterial.AllNeeds.getOrDefault("NeedsGoogleMap", false);
		NeedsCanvasAll=ABMaterial.AllNeeds.getOrDefault("NeedsCanvas", false);
		NeedsInputAll=ABMaterial.AllNeeds.getOrDefault("NeedsInput", false);
		NeedsFileInputAll=ABMaterial.AllNeeds.getOrDefault("NeedsFileInput", false);
		NeedsRadioAll=ABMaterial.AllNeeds.getOrDefault("NeedsRadio", false);
		NeedsTabsAll=ABMaterial.AllNeeds.getOrDefault("NeedsTabs", false);
		NeedsTableAll=ABMaterial.AllNeeds.getOrDefault("NeedsTable", false);
		NeedsJQueryUIAll=ABMaterial.AllNeeds.getOrDefault("NeedsJQueryUI", false);
		NeedsSortingTableAll=ABMaterial.AllNeeds.getOrDefault("NeedsSortingTable", false);
		NeedsBadgeAll=ABMaterial.AllNeeds.getOrDefault("NeedsBadge", false);
		NeedsCheckboxAll=ABMaterial.AllNeeds.getOrDefault("NeedsCheckbox", false);
		NeedsComboAll=ABMaterial.AllNeeds.getOrDefault("NeedsCombo", false);
		NeedsImageSliderAll=ABMaterial.AllNeeds.getOrDefault("NeedsImageSlider", false);
		NeedsTextAreaAll=ABMaterial.AllNeeds.getOrDefault("NeedsTextArea", false);
		NeedsSwitchAll=ABMaterial.AllNeeds.getOrDefault("NeedsSwitch", false);
		NeedsUploadAll=ABMaterial.AllNeeds.getOrDefault("NeedsUpload", false);
		NeedsHTML5VideoAll=ABMaterial.AllNeeds.getOrDefault("NeedsHTML5Video", false);
		NeedsChipsAll=ABMaterial.AllNeeds.getOrDefault("NeedsChips", false);
		NeedsActionButtonAll=ABMaterial.AllNeeds.getOrDefault("NeedsActionButton", false);
		NeedsListsAll=ABMaterial.AllNeeds.getOrDefault("NeedsLists", false);
		NeedsCardsAll=ABMaterial.AllNeeds.getOrDefault("NeedsCards", false);
		NeedsParallaxAll=ABMaterial.AllNeeds.getOrDefault("NeedsParallax", false);
		NeedsSignatureAll=ABMaterial.AllNeeds.getOrDefault("NeedsSignature", false);
		NeedsChartAll=ABMaterial.AllNeeds.getOrDefault("NeedsChart", false);
		NeedsOAuthAll=ABMaterial.AllNeeds.getOrDefault("NeedsOAuth", false);
		NeedsCalendarAll=ABMaterial.AllNeeds.getOrDefault("NeedsCalendar", false);
		NeedsMaskAll=ABMaterial.AllNeeds.getOrDefault("NeedsMask", false);
		NeedsTreeTableAll=ABMaterial.AllNeeds.getOrDefault("NeedsTreeTable", false);
		NeedsPaginationAll=ABMaterial.AllNeeds.getOrDefault("NeedsPagination", false);
		NeedsSliderAll=ABMaterial.AllNeeds.getOrDefault("NeedsSlider", false);
		NeedsRangeAll=ABMaterial.AllNeeds.getOrDefault("NeedsRange", false);
		NeedsDateTimeScrollerAll=ABMaterial.AllNeeds.getOrDefault("NeedsDateTimeScroller", false);
		NeedsDateTimePickerAll=ABMaterial.AllNeeds.getOrDefault("NeedsDateTimePicker", false);
		NeedsEditorAll=ABMaterial.AllNeeds.getOrDefault("NeedsEditor", false);
		NeedsSocialShareAll=ABMaterial.AllNeeds.getOrDefault("NeedsSocialShare", false);
		NeedsPDFViewerAll=ABMaterial.AllNeeds.getOrDefault("NeedsPDFViewer", false);
		NeedsPlatformAll=ABMaterial.AllNeeds.getOrDefault("NeedsPlatform", false);
		NeedsAudioPlayerAll=ABMaterial.AllNeeds.getOrDefault("NeedsAudioPlayer", false);
		NeedsTimelineAll=ABMaterial.AllNeeds.getOrDefault("NeedsTimeline", false);
		NeedsFlexWallAll=ABMaterial.AllNeeds.getOrDefault("NeedsFlexWall", false);
		NeedsSVGSurfaceAll=ABMaterial.AllNeeds.getOrDefault("NeedsSVGSurface", false);
		NeedsPatternLockAll=ABMaterial.AllNeeds.getOrDefault("NeedsPatternLock", false);
		NeedsChronologyListAll=ABMaterial.AllNeeds.getOrDefault("NeedsChronologyList", false);
		NeedsXPlayAll=ABMaterial.AllNeeds.getOrDefault("NeedsXPlay", false);
		NeedsCustomCardsAll=ABMaterial.AllNeeds.getOrDefault("NeedsCustomCards", false);
		NeedsChatAll=ABMaterial.AllNeeds.getOrDefault("NeedsChat", false);
		NeedsPlannerAll=ABMaterial.AllNeeds.getOrDefault("NeedsPlanner", false);
		NeedsPivotAll=ABMaterial.AllNeeds.getOrDefault("NeedsPivot", false);
		NeedsPercentSliderAll=ABMaterial.AllNeeds.getOrDefault("NeedsPercentSlider", false);
		NeedsSmartWizardAll=ABMaterial.AllNeeds.getOrDefault("NeedsSmartWizard", false);
		NeedsSimpleBarAll=ABMaterial.AllNeeds.getOrDefault("NeedsSimpleBar", false);
		NeedsComposerAll=ABMaterial.AllNeeds.getOrDefault("NeedsComposer", false);
		NeedsFileManagerAll=ABMaterial.AllNeeds.getOrDefault("NeedsFileManager", false);
		NeedsResponsiveContainerAll=ABMaterial.AllNeeds.getOrDefault("NeedsResponsiveContainer", false);
		NeedsB4JS=ABMaterial.AllNeeds.getOrDefault("NeedsB4JS", false);		
	}
	
	/**
	 * by default if initialCommands = null, the following commands are run:
	 *  
	 *  ga('create', 'UA-YOURTRACKINGID', 'auto');
  	 *	ga('send', 'pageview');
	 */
	public void UseGoogleAnalytics(String trackingID, anywheresoftware.b4a.objects.collections.List initialCommands ) {
		InitialCommands.Initialize();
		if (initialCommands!=null && initialCommands.IsInitialized()) {
			for (int i=0;i<initialCommands.getSize();i++ ) {
				InitialCommands.Add(initialCommands.Get(i));
			}
		} else {
			InitialCommands.Add("'create', '" + trackingID + "', 'auto'");
			InitialCommands.Add("'send', 'pageview'");
		}
		TrackingID=trackingID;
	}
	
	/**
	 * 
	 * see https://developers.google.com/analytics/devguides/collection/analyticsjs/command-queue-reference on how to build a command
	 */
	public void RunGoogleAnalyticsCommand(String command) {
		if (ws!=null) {
			ws.Eval("ga(" + command + ");", null);
			try {
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("Page RunGoogleAnalyticsCommand");}
					ws.Flush();RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
	}
	
	/**	 
	 * Note: you can use the text tags (e.g. {B}, {/B}
	 * 
	 * returnName: this is the name that will be returned in the page_msgboxresult() event to identify the msgbox.
	 * message: the dialog message
	 * title: the dialog title
	 * confirmButton: text of the first button. Must be something
	 * position: use one of the MSGBOX_POS_ constants
	 * theme: themeName previously added
	 * 
	 * Raises event page_MsgboxResult(returnName AS String, result AS String)
	 * Result is always MSGBOX_RESULT_OK
	 */	
	public void Msgbox(String returnName, String message, String title, String confirmButton, boolean IsTextSelectable, String position, String theme) {
		StringBuilder s = new StringBuilder();
		ThemeMsgBox l=null;
		if (!theme.equals("")) {
			l = CompleteTheme.MsgBox(theme);
		}
		if (l==null) {
			l = new ThemeMsgBox();
		}
		s.append("swal({");
		s.append("background: '" + ABMaterial.GetColorStrMap(l.BackColor, l.BackColorIntensity) + "',");
		s.append("rtl: " + l.RightToLeft + ",");
		s.append("title: \"" + BuildTextMsgbox(title) + "\",");
		s.append("html: \"" + BuildTextMsgbox(message) + "\",");
		s.append("type: 'info',");
		s.append("titleColor: '" + ABMaterial.GetColorStrMap(l.TitleTextColor, l.TitleTextColorIntensity) + "',");
		s.append("contentColor: '" + ABMaterial.GetColorStrMap(l.MessageTextColor, l.MessageTextColorIntensity) + "',");
		if (!l.ImageUrl.equals("")) {
			s.append("imageUrl: '" + l.ImageUrl + "',");
			s.append("imageWidth: " + l.ImageWidth + ",");
			s.append("imageHeight: " + l.ImageHeight + ",");
		}
		s.append("confirmButtonColor: '" + ABMaterial.GetColorStrMap(l.ConfirmButtonColor, l.ConfirmButtonColorIntensity) + "',");
		s.append("confirmButtonTextColor: '" + ABMaterial.GetColorStrMap(l.ConfirmButtonTextColor, l.ConfirmButtonTextColorIntensity) + "',");
		s.append("confirmButtonText: \"" + BuildTextMsgbox(confirmButton).toUpperCase() + "\",");
		s.append("showCancelButton: false,");
		s.append("allowOutsideClick: false,");
		s.append("allowEscapeKey: false,");
		s.append("buttonsStyling: true");
		s.append("}).then(");
		s.append("function(){");
		s.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_msgboxresult','eventparams':'returnname,result','returnname': '" + returnName + "','result':'abmok'});"); 
		s.append("}");
		s.append(").catch(swal.noop)\n");
		s.append("$('.swal2-modal').alterClass('swal2pos-*','" + position + "');");
		s.append("$('.swal2-container').removeClass('notselectable');");
		if (!IsTextSelectable) {
			s.append("$('.swal2-container').addClass('notselectable');");
		}
		s.append("$('.swal2-container').find('.swal2-icon').css({'color': '" + ABMaterial.GetColorStrMap(l.IconColor, l.IconColorIntensity) + "', 'border-color': '" + ABMaterial.GetColorStrMap(l.IconColor, l.IconColorIntensity) + "'});");
		s.append("$('.swal2-container').find('.swal2-close').css({'color': '" + ABMaterial.GetColorStrMap(l.CloseButtonColor, l.CloseButtonColorIntensity) + "'});");
		s.append("$('.swal2-container').find('.swal2-validationerror').css({'background-color': '" + ABMaterial.GetColorStrMap(l.BackColor, l.BackColorIntensity) + "'});");
		
		if (ws!=null) {
			ws.Eval(s.toString(), null);
			try {
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("Page Msgbox");}
					ws.Flush();RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
	}
		 
	public Object B4JSRunMethod(String className, String methodName, anywheresoftware.b4a.objects.collections.List Params) {
		String pars = "";
		if (Params.IsInitialized()) {
			for (int i=0;i<Params.getSize();i++) {
				if (i>0) {
					pars=pars + ", "; 
				}
				if (Params.Get(i) instanceof String) {
					pars = pars + "\"" + (String) Params.Get(i) + "\""; 
				} else {
					pars = pars + Params.Get(i);
				}
			}
		}
		Object b=null;
		if (this.ws!=null) {
			StringBuilder s = new StringBuilder();			
			s.append("var codeToExecute = 'return _b4jsclasses[\"b4jspagekey" + className.toLowerCase() + "\"]." + methodName.toLowerCase() + "(" + pars + ")';");        	
			s.append("var tmpFunc = new Function(codeToExecute);");
			s.append("if (typeof _b4jsclasses[\"b4jspagekey" + className.toLowerCase() + "\"]." + methodName.toLowerCase() + " === \"function\") {");				
				s.append("var fret;");
				s.append("try { ");
					s.append("fret = tmpFunc();");	
				s.append("} catch(err) {");
				s.append("}");
			s.append("} else {");
				s.append("var tmpFunc = new Function('return " + methodName + "(" + pars + ")');");
				s.append("if (typeof " + methodName + " === \"function\") {");
					s.append("var fret;");
					s.append("try { ");
						s.append("fret = tmpFunc();");	
					s.append("} catch(err) {");
					s.append("}");
				s.append("}");
			s.append("}");
			SimpleFuture j = this.ws.EvalWithResult(s.toString(), Params);
			if (j!=null) {
				try {
					b = j.getValue();
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
		return b;
	}
	
	public Object B4JSRunInlineJavascriptMethod(String methodName, anywheresoftware.b4a.objects.collections.List Params) {		
		Object b=null;
		if (this.ws!=null) {			
			SimpleFuture j = this.ws.RunFunctionWithResult(methodName, Params);
			if (j!=null) {
				try {
					b = j.getValue();
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
		return b;
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 * 	 
	 * Note: you can use the text tags for the title and message (e.g. {B}, {/B})
	 * 
	 * returnName: this is the name that will be returned in the page_B4JSmsgboxresult() event to identify the msgbox.
	 * message: the dialog message
	 * title: the dialog title
	 * confirmButton: text of the first button. leave empty "" if you do not need it
	 * cancelButton: text of the second button, leave empty "" if you do not need it.
	 * dialogType: use one of the MSGBOX_TYPE_ constants	   
	 * position: use one of the MSGBOX_POS_ constants
	 * theme: themeName previously added
	 * 
	 * Raises event page_MsgboxResult(returnName AS String, result AS String) 
	 * Result can be any of the MSGBOX_RESULT_ constants
	 */
	public void B4JSMsgbox(String returnName, String message, String title, String confirmButton, boolean IsTextSelectable, String position, String theme) {
		
	}
	
	/**	 
	 * Note: you can use the text tags for the title and message (e.g. {B}, {/B})
	 * 
	 * returnName: this is the name that will be returned in the page_msgboxresult() event to identify the msgbox.
	 * message: the dialog message
	 * title: the dialog title
	 * confirmButton: text of the first button. leave empty "" if you do not need it
	 * cancelButton: text of the second button, leave empty "" if you do not need it.
	 * dialogType: use one of the MSGBOX_TYPE_ constants	   
	 * position: use one of the MSGBOX_POS_ constants
	 * theme: themeName previously added
	 * 
	 * Raises event page_MsgboxResult(returnName AS String, result AS String) 
	 * Result can be any of the MSGBOX_RESULT_ constants
	 */	
	public void Msgbox2(String returnName, String message, String title, String confirmButton, String cancelButton, boolean hasCloseButton, String dialogType, boolean IsTextSelectable, String position, String theme) {
		StringBuilder s = new StringBuilder();
		ThemeMsgBox l=null;
		if (!theme.equals("")) {
			l = CompleteTheme.MsgBox(theme);
		}
		if (l==null) {
			l = new ThemeMsgBox();
		}
		s.append("swal({");
		s.append("background: '" + ABMaterial.GetColorStrMap(l.BackColor, l.BackColorIntensity) + "',");
		s.append("rtl: " + l.RightToLeft + ",");
		s.append("title: \"" + BuildTextMsgbox(title) + "\",");
		s.append("html: \"" + BuildTextMsgbox(message) + "\",");
		s.append("type: '" + dialogType + "',");
		if (hasCloseButton) {
			s.append("showCloseButton: true,");
		}
		s.append("titleColor: '" + ABMaterial.GetColorStrMap(l.TitleTextColor, l.TitleTextColorIntensity) + "',");
		s.append("contentColor: '" + ABMaterial.GetColorStrMap(l.MessageTextColor, l.MessageTextColorIntensity) + "',");
		if (!l.ImageUrl.equals("")) {
			s.append("imageUrl: '" + l.ImageUrl + "',");
			s.append("imageWidth: " + l.ImageWidth + ",");
			s.append("imageHeight: " + l.ImageHeight + ",");
		}
		if (!confirmButton.equals("")) {
			s.append("showConfirmButton: true,");
			s.append("confirmButtonColor: '" + ABMaterial.GetColorStrMap(l.ConfirmButtonColor, l.ConfirmButtonColorIntensity) + "',");
			s.append("confirmButtonTextColor: '" + ABMaterial.GetColorStrMap(l.ConfirmButtonTextColor, l.ConfirmButtonTextColorIntensity) + "',");
			s.append("confirmButtonText: \"" + BuildTextMsgbox(confirmButton).toUpperCase() + "\",");
		} else {
			s.append("showConfirmButton: false,");
		}		
		if (!cancelButton.equals("")) {
			s.append("showCancelButton: true,");
			s.append("cancelButtonColor: '" + ABMaterial.GetColorStrMap(l.CancelButtonColor, l.CancelButtonColorIntensity) + "',");
			s.append("cancelButtonTextColor: '" + ABMaterial.GetColorStrMap(l.CancelButtonTextColor, l.CancelButtonTextColorIntensity) + "',");
			s.append("cancelButtonText: \"" + BuildTextMsgbox(cancelButton).toUpperCase() + "\",");
		} else {
			s.append("showCancelButton: false,");
		}
		if (!hasCloseButton) {
			s.append("allowOutsideClick: false,");
			s.append("allowEscapeKey: false,");
		}
		s.append("buttonsStyling: true");
		s.append("}).then(");
		s.append("function(){");
		s.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_msgboxresult','eventparams':'returnname,result','returnname': '" + returnName + "','result':'abmok'});"); 
		s.append("}");
		if (!cancelButton.equals("")) {
			s.append(", function(dismiss){");
			s.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_msgboxresult','eventparams':'returnname,result','returnname': '" + returnName + "','result': dismiss});"); 
			s.append("}");
		}
		s.append(").catch(swal.noop)\n");
		s.append("$('.swal2-modal').alterClass('swal2pos-*','" + position + "');");
		s.append("$('.swal2-container').removeClass('notselectable');");
		if (!IsTextSelectable) {
			s.append("$('.swal2-container').addClass('notselectable');");
		}
		s.append("$('.swal2-container').find('.swal2-icon').css({'color': '" + ABMaterial.GetColorStrMap(l.IconColor, l.IconColorIntensity) + "', 'border-color': '" + ABMaterial.GetColorStrMap(l.IconColor, l.IconColorIntensity) + "'});");
		s.append("$('.swal2-container').find('.swal2-close').css({'color': '" + ABMaterial.GetColorStrMap(l.CloseButtonColor, l.CloseButtonColorIntensity) + "'});");
		s.append("$('.swal2-container').find('.swal2-validationerror').css({'background-color': '" + ABMaterial.GetColorStrMap(l.BackColor, l.BackColorIntensity) + "'});");
		if (ws!=null) {
			ws.Eval(s.toString(), null);
			try {
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("Page Msgbox2");}
					ws.Flush();RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 * 	 
	 * Note: you can use the text tags for the title and message (e.g. {B}, {/B})
	 * 
	 * returnName: this is the name that will be returned in the page_B4JSmsgboxresult() event to identify the msgbox.
	 * message: the dialog message
	 * title: the dialog title
	 * confirmButton: text of the first button. leave empty "" if you do not need it
	 * cancelButton: text of the second button, leave empty "" if you do not need it.
	 * dialogType: use one of the MSGBOX_TYPE_ constants	   
	 * position: use one of the MSGBOX_POS_ constants
	 * theme: themeName previously added
	 * 
	 * Raises event page_MsgboxResult(returnName AS String, result AS String) 
	 * Result can be any of the MSGBOX_RESULT_ constants
	 */	
	public void B4JSMsgbox2(String returnName, String message, String title, String confirmButton, String cancelButton, boolean hasCloseButton, String dialogType, boolean IsTextSelectable, String position, String theme) {
		
	}
	
	/**	 
	 * Note: you can use the text tags for the title and message (e.g. {B}, {/B})
	 * 
	 * returnName: this is the name that will be returned in the page_inputboxresult() event to identify the inputbox.
	 * message: the dialog message
	 * title: the dialog title
	 * confirmButton: text of the first button. Must be something
	 * cancelButton: text of the second button, leave empty "" if you do not need it.
	 * dialogType: use one of the INPUTBOX_TYPE_ constants
	 * inputType: use this param to select the input type with the INPUT_QUESTIONTYPE_ constants
	 * inputValue: default value
	 * inputPlaceHolder: the placeholder text (only used for some inputTypes)   
	 * inputOptions: some types need an extra parameter (only used for some inputTypes)	 
	 * 		INPUT_QUESTIONTYPE_RADIO: {'#ff0000': 'Red','#00ff00': 'Green','#0000ff': 'Blue'}
	 * 		INPUT_QUESTIONTYPE_EMAIL: Invalid email address
	 * invalidInputText: the text to display if the input is invalid. Put an empty string if it should not be validated (email is always validated!)
	 * position: use one of the MSGBOX_POS_ constants  		
	 * theme: themeName previously added
	 * 	 
	 * Raises event page_MsgboxResult(returnName AS String, result AS String) 
	 * Result can be any of the INPUTBOX_RESULT_ constants OR the input value	 * 
	 */	
	public void InputBox(String returnName, String title, String confirmButton, String cancelButton, boolean hasCloseButton, String dialogType, String inputType, String inputValue, String inputPlaceHolder, String inputOptions, String invalidInputText, boolean IsTextSelectable, String position, String theme) {
		StringBuilder s = new StringBuilder();
		ThemeMsgBox l=null;
		if (!theme.equals("")) {
			l = CompleteTheme.MsgBox(theme);
		}
		if (l==null) {
			l = new ThemeMsgBox();
		}
		s.append("swal({");
		s.append("background: '" + ABMaterial.GetColorStrMap(l.BackColor, l.BackColorIntensity) + "',");
		s.append("rtl: " + l.RightToLeft + ",");
		s.append("title: \"" + BuildTextMsgbox(title) + "\",");
		s.append("input: \"" + inputType + "\",");
		if (!inputValue.equals("")) {
			s.append("inputValue: \"" + inputValue + "\",");
		}		
		if (inputType==ABMaterial.INPUTBOX_QUESTIONTYPE_EMAIL) {
			s.append("invalidEmail: \"" + invalidInputText + "\",");	
		} else {
			if (!invalidInputText.equals("")) {
				s.append("inputValidator: function (result) {");
				s.append("return new Promise(function (resolve, reject) {");
				s.append("if (result) {");
				s.append("resolve();");
				s.append("} else {");
				s.append("reject(\"" + invalidInputText + "\");");
				s.append("}");
				s.append("})");
				s.append("},");
			}
		}
		
		if (!inputOptions.equals("")) {
			s.append("inputOptions: " + inputOptions + ",");			
		}
		if (!inputPlaceHolder.equals("")) {
			s.append("inputPlaceholder: \"" + inputPlaceHolder + "\",");
		}
		s.append("type: '" + dialogType + "',");
		if (hasCloseButton) {
			s.append("showCloseButton: true,");
		}
		s.append("titleColor: '" + ABMaterial.GetColorStrMap(l.TitleTextColor, l.TitleTextColorIntensity) + "',");
		s.append("contentColor: '" + ABMaterial.GetColorStrMap(l.MessageTextColor, l.MessageTextColorIntensity) + "',");
		if (!l.ImageUrl.equals("")) {
			s.append("imageUrl: '" + l.ImageUrl + "',");
			s.append("imageWidth: " + l.ImageWidth + ",");
			s.append("imageHeight: " + l.ImageHeight + ",");
		}
		s.append("confirmButtonColor: '" + ABMaterial.GetColorStrMap(l.ConfirmButtonColor, l.ConfirmButtonColorIntensity) + "',");
		s.append("confirmButtonTextColor: '" + ABMaterial.GetColorStrMap(l.ConfirmButtonTextColor, l.ConfirmButtonTextColorIntensity) + "',");
		s.append("confirmButtonText: \"" + BuildTextMsgbox(confirmButton) + "\",");
		if (!cancelButton.equals("")) {
			s.append("showCancelButton: true,");
			s.append("cancelButtonColor: '" + ABMaterial.GetColorStrMap(l.CancelButtonColor, l.CancelButtonColorIntensity) + "',");
			s.append("cancelButtonTextColor: '" + ABMaterial.GetColorStrMap(l.CancelButtonTextColor, l.CancelButtonTextColorIntensity) + "',");
			s.append("cancelButtonText: \"" + BuildTextMsgbox(cancelButton) + "\",");
		} else {
			s.append("showCancelButton: false,");
		}
		if (!hasCloseButton) {
			s.append("allowOutsideClick: false,");
			s.append("allowEscapeKey: false,");
		}
		s.append("buttonsStyling: true,");
		s.append("checkboxTextColor: '" + ABMaterial.GetColorStrMap(l.CheckboxTextColor, l.CheckboxTextColorIntensity) + "',");
		s.append("radioTextColor: '" + ABMaterial.GetColorStrMap(l.RadioTextColor, l.RadioTextColorIntensity) + "',");
		s.append("textareaTextColor: '" + ABMaterial.GetColorStrMap(l.TextAreaTextColor, l.TextAreaTextColorIntensity) + "',");
		s.append("inputTextClass: 'input-field input-field" + l.InputFieldTheme + "'");
		s.append("}).then(");
		s.append("function(value){");
		if (inputType.equals(ABMaterial.INPUTBOX_QUESTIONTYPE_CHECKBOX)) {
			s.append("var ret='0';");
			s.append("if (value) {");
			s.append("ret='1';");	
			s.append("}");
			s.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_inputboxresult','eventparams':'returnname,result','returnname': '" + returnName + "','result': ret});");
		} else {
			s.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_inputboxresult','eventparams':'returnname,result','returnname': '" + returnName + "','result': value});");
		}
		s.append("}");
		if (!cancelButton.equals("")) {
			s.append(", function(dismiss){");
			s.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_inputboxresult','eventparams':'returnname,result','returnname': '" + returnName + "','result': dismiss});"); 
			s.append("}");
		}
		s.append(").catch(swal.noop)\n");
		s.append("$('.swal2-modal').alterClass('swal2pos-*','" + position + "');");
		s.append("$('.swal2-container').removeClass('notselectable');");
		if (!IsTextSelectable) {
			s.append("$('.swal2-container').addClass('notselectable');");
		}
		s.append("$('.swal2-container').find('.swal2-icon').css({'color': '" + ABMaterial.GetColorStrMap(l.IconColor, l.IconColorIntensity) + "', 'border-color': '" + ABMaterial.GetColorStrMap(l.IconColor, l.IconColorIntensity) + "'});");
		s.append("$('.swal2-container').find('.swal2-close').css({'color': '" + ABMaterial.GetColorStrMap(l.CloseButtonColor, l.CloseButtonColorIntensity) + "'});");
		s.append("$('.swal2-container').find('.swal2-validationerror').css({'background-color': '" + ABMaterial.GetColorStrMap(l.BackColor, l.BackColorIntensity) + "'});");
		if (ws!=null) {
			ws.Eval(s.toString(), null);
			try {
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("Page Msgbox2");}
					ws.Flush();RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
	}
	
	/**	 
	 * ONLY TO USE IN A B4JS CLASS!
	 * 
	 * Note: you can use the text tags for the title and message (e.g. {B}, {/B})
	 * 
	 * returnName: this is the name that will be returned in the page_B4JSinputboxresult() event to identify the inputbox.
	 * message: the dialog message
	 * title: the dialog title
	 * confirmButton: text of the first button. Must be something
	 * cancelButton: text of the second button, leave empty "" if you do not need it.
	 * dialogType: use one of the INPUTBOX_TYPE_ constants
	 * inputType: use this param to select the input type with the INPUT_QUESTIONTYPE_ constants
	 * inputValue: default value
	 * inputPlaceHolder: the placeholder text (only used for some inputTypes)   
	 * inputOptions: some types need an extra parameter (only used for some inputTypes)	 
	 * 		INPUT_QUESTIONTYPE_RADIO: {'#ff0000': 'Red','#00ff00': 'Green','#0000ff': 'Blue'}
	 * 		INPUT_QUESTIONTYPE_EMAIL: Invalid email address
	 * invalidInputText: the text to display if the input is invalid. Put an empty string if it should not be validated (email is always validated!)
	 * position: use one of the MSGBOX_POS_ constants  		
	 * theme: themeName previously added
	 * 	 
	 * Raises event page_MsgboxResult(returnName AS String, result AS String) 
	 * Result can be any of the INPUTBOX_RESULT_ constants OR the input value	 * 
	 */	
	public void B4JSInputBox(String returnName, String title, String confirmButton, String cancelButton, boolean hasCloseButton, String dialogType, String inputType, String inputValue, String inputPlaceHolder, String inputOptions, String invalidInputText, boolean IsTextSelectable, String position, String theme) {
		
	}
			
	public void AddAnimation(ABMAnimation animation) {
		Animations.put(animation.Name.toLowerCase(), animation);
	}
	
	public void setShowLoader(boolean doShow) {
		mShowLoader = doShow;
		mLoaderOpacity = 1;
	}
	
	public void setShowLoaderType(int loaderType) {
		mShowLoaderType=loaderType;
	}
	
	public int getShowLoaderType() {
		return mShowLoaderType;
	}
	
	public boolean getShowLoader() {
		return mShowLoader;
	}
	
	public void SetShowLoaderWithOpacity(boolean doShow, double opacity) {
		mShowLoader = doShow;
		mLoaderOpacity = opacity;
	}
	
	public void SetLoaderJUMPINGBALL(String foreColorAppLoading, String foreColorAppLoadingIntensity,String foreColorPause, String foreColorIntensityPause, String text, String pauzeText) {
		L1Color=foreColorAppLoading;
		L1ColorIntensity=foreColorAppLoadingIntensity;
		L1ColorTemp=foreColorPause;
		L1ColorTempIntensity=foreColorIntensityPause;
		L1Text=text;
		L1TextTemp=pauzeText;
		LoaderType=LOADERANIM_JUMPINGBALL;
	}
	
	public void SetLoaderJUMPINGBALLTexts(String text, String pauzeText) {
		L1Text=text;
		L1TextTemp=pauzeText;
		if (ws!=null) {
			if (ws.getOpen()) {
				ws.Eval("$('#L1text').html('" + BuildText(L1Text) + "');", null);
				ws.Eval("$('#L1textTemp').html('" + BuildText(L1TextTemp) + "');", null);
				try {
					if (ws.getOpen()) {
						if (ShowDebugFlush) {BA.Log("JUMPINGBALLText Refresh");}
						ws.Flush();RunFlushed();				
					}
				} catch (IOException e) {			
					e.printStackTrace();
				}
			}
		}
	}
	
	public void SetLoaderJUGGLINGBALLS(String ball1AppLoading, String ball1AppLoadingIntensity,String ball2AppLoading, String ball2AppLoadingIntensity,String ball3AppLoading, String ball3AppLoadingIntensity,String ball1ColorPause, String ball1ColorIntensityPause,String ball2ColorPause, String ball2ColorIntensityPause,String ball3ColorPause, String ball3ColorIntensityPause) {
		L2B1Color=ball1AppLoading;
		L2B1ColorIntensity=ball1AppLoadingIntensity;
		L2B1ColorTemp=ball1ColorPause;
		L2B1ColorTempIntensity=ball1ColorIntensityPause;
		L2B2Color=ball2AppLoading;
		L2B2ColorIntensity=ball2AppLoadingIntensity;
		L2B2ColorTemp=ball2ColorPause;
		L2B2ColorTempIntensity=ball2ColorIntensityPause;
		L2B3Color=ball3AppLoading;
		L2B3ColorIntensity=ball3AppLoadingIntensity;
		L2B3ColorTemp=ball3ColorPause;
		L2B3ColorTempIntensity=ball3ColorIntensityPause;
		LoaderType=LOADERANIM_JUGGLINGBALLS;
	}
	
	public void SetLoaderHEARTBEAT(String graphColorAppLoading, String graphColorAppLoadingIntensity, String beatColorAppLoading, String beatColorAppLoadingIntensity, String graphColorPause, String graphColorPauseIntensity, String beatColorPause, String beatColorPauseIntensity) {
		L3GColor=graphColorAppLoading;
		L3GColorIntensity=graphColorAppLoadingIntensity;
		L3GColorTemp=graphColorPause;
		L3GColorTempIntensity=graphColorPauseIntensity;	
		L3BColor=beatColorPause;
		L3BColorIntensity=beatColorPauseIntensity;
		L3BColorTemp=beatColorPause;
		L3BColorTempIntensity=beatColorPauseIntensity;	
		LoaderType=LOADERANIM_HEARTBEAT;
	}
	
	public void SetLoaderDEVICESWITCH() {
		LoaderType=LOADERANIM_DEVICESWITCH;
	}
	
	public void SetLoaderMETALGEARSOLID(String foreColorAppLoading, String foreColorAppLoadingIntensity,String foreColorPause, String foreColorIntensityPause) {
		L5Color=foreColorAppLoading;
		L5ColorIntensity=foreColorAppLoadingIntensity;
		L5ColorTemp=foreColorPause;
		L5ColorTempIntensity=foreColorIntensityPause;
		
		LoaderType=LOADERANIM_METALGEARSOLID;
	}
	
	public void SetLoaderROTATINGBOXES(String ColorAAppLoading, String ColorIntensityAAppLoading,String ColorBAppLoading, String ColorIntensityBAppLoading, String ColorAPause, String ColorIntensityAPause,String ColorBPause, String ColorIntensityBPause) {
		L6ColorA=ColorAAppLoading;
		L6ColorAIntensity=ColorIntensityAAppLoading;
		L6ColorATemp=ColorAPause;
		L6ColorATempIntensity=ColorIntensityAPause;
		L6ColorB=ColorBAppLoading;
		L6ColorBIntensity=ColorIntensityBAppLoading;
		L6ColorBTemp=ColorBPause;
		L6ColorBTempIntensity=ColorIntensityBPause;
		
		LoaderType=LOADERANIM_ROTATINGBOXES;
	}
	
	public void FinishedLoading() {
		StringBuilder s = new StringBuilder();
		s.append("$('#isloaderwrapper').removeClass('isloading');");
		s.append("$('body').addClass('loaded');");
		s.append("$('#loader-wrapper').remove();");
		if (NeedsCodeLabel) {
			s.append("$('.prettyprint').removeClass('prettyprinted');");
			s.append("PR.prettyPrint();");
        }
		s.append("var foot = $('body').find('footer');");
		if (IsFixedFooter) {
			s.append("foot.removeClass('footerfloating');");
			s.append("foot.addClass('footerfixed');");
		} else {
			s.append("foot.removeClass('footerfixed');");
			s.append("foot.addClass('footerfloating');");			
		}
		s.append("$('main').css('padding-bottom', '" + PaddingBottom + "px');");
		ws.Eval(s.toString(), null);
		try {
			if (ws.getOpen()) {
				if (ShowDebugFlush) {BA.Log("Page FinishedLoading");}
				ws.Flush();RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}		
	}
	
	protected String Serialize(Object obj, String rowcell, int Type) {		
		if (!((obj instanceof Serializable) || (obj instanceof Externalizable))) {
			BA.LogError("There was a problem Serializing " + rowcell);
			return "";
		}
		String redisString="";
		try {
             ByteArrayOutputStream bo = new ByteArrayOutputStream();
             ObjectOutputStream so = new ObjectOutputStream(bo);
             so.writeObject(obj);
             if (ws.getOpen()) so.flush();
             redisString = new String(Base64.getEncoder().encode(bo.toByteArray()));          
		} catch (Exception e) {
             e.printStackTrace();        
        }
		return redisString;
	}
	
	protected Object Deserialize(String value) {
		Object obj = null;
		try {
			byte b[] = Base64.getDecoder().decode(value.getBytes("UTF-8")); 
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            obj = si.readObject();                     
		} catch (Exception e) {
            e.printStackTrace();        
		}
		return obj;
	}
		
	public boolean BlockEvent(String event) {
		if (PushPop.containsKey(event.toLowerCase())) {
			return true;
		}
		PushPop.put(event.toLowerCase(), true);
		return false;
	}
	
	public void UnblockEvent(String event) {
		PushPop.remove(event.toLowerCase());
	}
	
	public boolean IsBlockedEvent(String event) {
		return PushPop.getOrDefault(event.toLowerCase(), false);
	}
	
	public ABMPlatform GetPlatform() {
		ABMPlatform p = new ABMPlatform();
		
		if (this.ws!=null) {
			anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
			Params.Initialize();
			SimpleFuture j = this.ws.RunFunctionWithResult("getplatform", Params);
			if (j!=null) {
				try {
					String ret = (String) j.getValue();
					ret = ret.replaceAll("null", "");
					String[] parts = ret.split(";ABM;");
					//platform.manufacturer+;ABM;+platform.name+;ABM;+platform.prerelease+;ABM;+platform.product+;ABM;+platform.version+;ABM;+platform.os.architecture+;ABM;+platform.os.family+;ABM;+platform.os.version+;ABM;+platform.description+;ABM;+platform.ua
					p.Manufacturer = parts[0];
					p.Name = parts[1];
					p.Prerelease = parts[2];
					p.Product = parts[3];
					p.Version = parts[4];
					p.OSArchitecture = parts[5];
					p.OSFamily = parts[6];
					p.OSVersion = parts[7];
					p.Description = parts[8];
					p.UserAgent = parts[9];
					double dbl = Double.valueOf(parts[10]);
					p.DevicePixelRatio = (int) Math.ceil(dbl);
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
		return p;
	}
					
	public void InitializeWithTheme(String name, String handler, boolean centerInPageWithPadding, int SessionMaxInactiveIntervalSeconds, ABMTheme theme) {
		this.Name = name;
		this.Handler = handler;
		this.CenterInPageWithPadding = centerInPageWithPadding;
		this.OpenGraphMetaTags.Initialize();
		this.SessionMaxInactiveInterval=SessionMaxInactiveIntervalSeconds;
		CustomVariables.Initialize();
			
		if (theme==null) {
			this.CompleteTheme=new ABMTheme();
		} else {
			this.CompleteTheme=theme;
		}
		
		Theme=this.CompleteTheme.Page.Clone();
		this.CompleteTheme.MainColor = CompleteTheme.Page.MainColor; 
		Footer.Initialize(this, "page-footer", "");
		
		NeedsIcons.Initialize();
		
		IsInitialized=true;
	}	
	
	/**
	 * Only to be used if the page was initialized with InitializeWithThemes() and to be used in Websocket_Connected() after page.SetWebSocket(ws)
	 */
	public void SetActiveTheme(String useThemeName) {
		if (ws!=null) {
			anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
			Params.Initialize();
			Params.Add(useThemeName);
			ws.RunFunction("settheme", Params);
			try {
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("Page SetActiveTheme");}
					ws.Flush();RunFlushed();
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		
		ABMTheme theme = this.CompleteThemes.getOrDefault(useThemeName, null);
		if (theme==null) {
			BA.Log("ERROR: No theme found with the name '" + useThemeName + "'. Using the first theme found.");
			return;
		}
		this.CompleteTheme=theme;
		Theme=this.CompleteTheme.Page.Clone();
		this.CompleteTheme.MainColor = CompleteTheme.Page.MainColor;
		
		if (navigationBar!=null) {
			navigationBar.ResetTheme();
		}
		
		for (Entry<String, ABMRow> row : Rows.entrySet()) {
			row.getValue().ResetTheme();
		}
		
		for (Entry<String, ABMModalSheet> sheet : ModalSheets.entrySet()) {
			sheet.getValue().ResetTheme();												
        }
		for (Entry<String, ABMActionButton> button : ActionButtons.entrySet()) {
			button.getValue().ResetTheme();									
        }
		for (Entry<String,ABMContainer> cont: FloatingContainers.entrySet()) {
			cont.getValue().ResetTheme();
		}
		for (Entry<String,ABMCanvas> ec: Canvases.entrySet()) {
			ec.getValue().ResetTheme();
		}
		
		if (Footer!=null) {
			Footer.ResetTheme();
		}
	}
	
		
	/**
	 * When connected (Websocket_Connected()), call the method SetActiveTheme() after page.SetWebSocket(ws)
	 * e.g. 
	 * page.InitializeWithThemes(Name, fullPath, False, themes)
	 */
	public void InitializeWithThemes(String name, String handler, boolean centerInPageWithPadding, int SessionMaxInactiveIntervalSeconds, anywheresoftware.b4a.objects.collections.List themes) {
		this.Name = name;
		this.Handler = handler;
		this.SessionMaxInactiveInterval=SessionMaxInactiveIntervalSeconds;
		//this._ba = ba;
		this.CenterInPageWithPadding = centerInPageWithPadding;
		this.OpenGraphMetaTags.Initialize();
	
		if (themes.getSize()==0) {
			this.CompleteTheme=new ABMTheme();			
		} else {
			if (themes.getSize()==1) {
				ABMTheme th = (ABMTheme) themes.Get(0);
				this.CompleteTheme=th;
			} else {
				for (int i=0;i<themes.getSize();i++) {
					ABMTheme th = (ABMTheme) themes.Get(i);
					this.CompleteThemes.put(th.Name, th);
					if (i==0) {
						this.CompleteTheme=th;
					}
				}
			}
		}	
		
		NeedsIcons.Initialize();
		CustomVariables.Initialize();
		
		Theme=this.CompleteTheme.Page.Clone();
		this.CompleteTheme.MainColor = CompleteTheme.Page.MainColor; 
		Footer.Initialize(this, "page-footer", "");
				
		IsInitialized=true;
	}	
	
	public void Initialize(BA ba, String name, String handler, boolean centerInPageWithPadding, int SessionMaxInactiveIntervalSeconds) {
		this.Name = name;
		this.Handler = handler;
		this.SessionMaxInactiveInterval=SessionMaxInactiveIntervalSeconds;
		this.CenterInPageWithPadding=centerInPageWithPadding;
		this.OpenGraphMetaTags.Initialize();

		this.CompleteTheme=new ABMTheme();		
		Theme=this.CompleteTheme.Page.Clone();
		Footer.Initialize(this, "page-footer", "");
		
		NeedsIcons.Initialize();
		CustomVariables.Initialize();
		
		IsInitialized=true;
	}
	/**
	 * Must be set in the BuildPage() method! 
	 */
	public void SetBackgroundImage(String image) {
		this.BackgroundImage=image;
		this.NeedsBackgroundImage=true;
	}
	
	/**
	 * Must be set in the BuildPage() method! May not work on all devices, so provide a backup image (will override SetBackgroundImage().
	 */
	public void SetBackgroundVideo(String srcWebm, String srcMp4, String srcOgv, boolean mute, int volume, boolean hasOverlay, String backupImage) {
		this.BackgroundVideoMp4=srcMp4;
		this.BackgroundVideoOgv=srcOgv;
		this.BackgroundVideoWebm=srcWebm;
		this.BackgroundVideoMute=mute;
		this.BackgroundVideoVolume=volume;
		this.BackgroundVideoImage=backupImage;
		this.NeedsBackgroundVideo=true;
		this.BackgroundVideoHasOverlay=hasOverlay;
	}
	
	public void SignOffSocialNetwork(String network, String registrationId, String returnExtra) {
		ABMaterial.SignOffSocialNetwork(this, network, registrationId, returnExtra);
	}
	
	
	public void SaveNavigationBarPosition() {
		if (navigationBar!=null) {
			SaveNavigationBarPosition(this.ws);
		}
	}

	public void RestoreNavigationBarPosition() {
		if (navigationBar!=null) {
			RestoreNavigationBarPosition(this.ws);
		}
	}
	
	public Object GetEventHandler(Object me, String eventName) {
		int i = eventName.indexOf("_");
		if (i>-1) {
			eventName = eventName.substring(0, i);
		}
		return EventHandlers.getOrDefault(eventName.toLowerCase(), me);
	}
	
	@Hide
	public void SaveNavigationBarPosition(WebSocket ws) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();		
		SimpleFuture j = ws.RunFunctionWithResult("SaveNavigationBarPosition", Params);		
		if (j!=null) {
			try {			
				ws.getSession().SetAttribute(UUID +  "_navbarpos", j.getValue());
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
	
	protected void CheckDeviceInternal(ServletRequestWrapper req) {
		UAgentInfo dev = new UAgentInfo(req.GetHeader("User-Agent"), req.GetHeader("Accept"));
		mIsTablet = dev.isTierTablet;
		mIsPhone = dev.isTierIphone;		
	}
	
	public String CheckDevice(ServletRequestWrapper req) {
		UAgentInfo dev = new UAgentInfo(req.GetHeader("User-Agent"), req.GetHeader("Accept"));
		mIsTablet = dev.isTierTablet;
		mIsPhone = dev.isTierIphone;
		StringBuilder s = new StringBuilder();
		s.append("IsPhone:" + dev.isTierIphone + ";");
		s.append("IsTablet: " + dev.isTierTablet + ";");
		s.append("IsDesktop: " + ( dev.isTierIphone==false && dev.isTierTablet == false));
		if (dev.isTierIphone) {
			ws.Eval("$('#devicetype').html('phone');", null);
		} else {
			if (dev.isTierTablet) {
				ws.Eval("$('#devicetype').html('tablet');", null);
			} else {
				ws.Eval("$('#devicetype').html('desktop');", null);
			}
		}
		try {
			if (ws.getOpen()) {
				if (ShowDebugFlush) {BA.Log("Setting devicetype");}
				ws.Flush();RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
		return s.toString();
	}
	
	@Hide
	public void RestoreNavigationBarPosition(WebSocket ws) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();			
		String navpos="0";
		try {
			navpos = "" + ws.getSession().GetAttribute(UUID +  "_navbarpos");
		} catch (Exception e) {
			//e.printStackTrace();
			navpos="0";
		}
		Params.Add(navpos);
		ws.RunFunction("RestoreNavigationBarPosition", Params);
		try {
			if (ws.getOpen()) {
				if (ShowDebugFlush) {BA.Log("Page RestoreNavigationBarPosition");}
				ws.Flush();RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public void SetFontStack(String fontStack) {
		this.FontStack=fontStack;
	}
	
	public void AddExtraJavaScriptFile(String fileName) {
		if (!fileName.trim().equals("")) {
			this.ExtraJS.add(fileName);
		}
	}	
	
	public void AddExtraCSSFile(String fileName) {
		if (!fileName.trim().equals("")) {
			this.ExtraCSS.add(fileName);
		}
	}
	
	public void AddExtraCSSString(String css) {
		this.ExtraCSSStrings.add(css);
	}
	
	protected void AddAppleTouchIcon(String image, String size) {
		AppleTouchIcons.add(image);
		AppleTouchIconSizes.add(size);
	}
	
	protected void AddMSTileIcon(String image, String size) {
		MSTileIcons.add(image);
		MSTileIconSizes.add(size);
	}
	
	protected void AddFavorityIcon(String image, String size) {
		FavorityIcons.add(image);
		FavorityIconSizes.add(size);
	}
	
	public void AddCanvasImage(String imageId, String image) {
		Images.add(image);
		ImageIds.add(imageId);
		
		if (ws!=null) {
			anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
			Params.Initialize();
			Params.Add(imageId);
			Params.Add(image);
			ws.RunFunction("AfterLoadCanvasImage", Params);
			try {
				ws.Flush();
			} catch (IOException e) {
				
			}
		}		
		
	}
	
	public String ConvertABMTextTagsToHTML(String text) {
		StringBuilder s = new StringBuilder();	
		
		String v = ABMaterial.HTMLConv().htmlEscape(text, PageCharset);
		v=v.replaceAll("(\r\n|\n\r|\r|\n)", "<br>");
		v=v.replace("{B}", "<b>");
		v=v.replace("{/B}", "</b>");
		v=v.replace("{I}", "<i>");
		v=v.replace("{/I}", "</i>");
		v=v.replace("{U}", "<ins>");
		v=v.replace("{/U}", "</ins>");
		v=v.replace("{SUB}", "<sub>");
		v=v.replace("{/SUB}", "</sub>");
		v=v.replace("{SUP}", "<sup>");
		v=v.replace("{/SUP}", "</sup>");
		v=v.replace("{BR}", "<br>");
		v=v.replace("{WBR}", "<wbr>");
		v=v.replace("{NBSP}", "&nbsp;");
		v=v.replace("{AL}", "<a rel=\"nofollow\" target=\"_blank\" href=\"");
		v=v.replace("{AT}", "\">");
		v=v.replace("{/AL}", "</a>");
		v=v.replace("{AS}", " title=\"");
		v=v.replace("{/AS}", "\"");
		
		while (v.indexOf("{C:") > -1) {
			int vvi = v.indexOf("{C:");
			v=v.replaceFirst("\\{C:", "<span style=\"color:");
			v=v.substring(0,vvi) + v.substring(vvi).replaceFirst("\\}", "\">");
			v=v.replaceFirst("\\{/C}", "</span>");	
		}

		v = v.replace("{CODE}", "<div class=\"abmcode\"><pre><code>");
		v = v.replace("{/CODE}", "</code></pre></div>");
		while (v.indexOf("{ST:") > -1) {
			int vvi = v.indexOf("{ST:");
			v=v.replaceFirst("\\{ST:", "<span style=\"");			
			v=v.substring(0,vvi) + v.substring(vvi).replaceFirst("\\}", "\">");
			v=v.replaceFirst("\\{/ST}", "</span>");	
		}	
		
		int start = v.indexOf("{IC:");
		while (start > -1) {
			int stop = v.indexOf("{/IC}");
			String vv = "";
			if (stop>0) {
				vv = v.substring(start, stop+5);
			} else {
				break;
			}
			String IconColor = vv.substring(4, 11);
			String IconName = vv.substring(12,vv.length()-5);
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
				repl = "<i>" + svgIconmap.getOrDefault(IconName.toLowerCase(), "") + "</i>"; 
				break;
			default:
				repl = "<i style=\"color: " + IconColor + "\" class=\"material-icons\">" + IconName + "</i>";
			}
			v=v.replace(vv,repl );
			start = v.indexOf("{IC:");
		}
		
		s.append(v);
		
		return s.toString();
	}
	
	protected String BuildText(String text) {
		StringBuilder s = new StringBuilder();	
		
		String v = ABMaterial.HTMLConv().htmlEscape(text, PageCharset);
		v=v.replaceAll("(\r\n|\n\r|\r|\n)", "<br>");
		v=v.replace("{B}", "<b>");
		v=v.replace("{/B}", "</b>");
		v=v.replace("{I}", "<i>");
		v=v.replace("{/I}", "</i>");
		v=v.replace("{U}", "<ins>");
		v=v.replace("{/U}", "</ins>");
		v=v.replace("{SUB}", "<sub>");
		v=v.replace("{/SUB}", "</sub>");
		v=v.replace("{SUP}", "<sup>");
		v=v.replace("{/SUP}", "</sup>");
		v=v.replace("{BR}", "<br>");
		v=v.replace("{WBR}", "<wbr>");
		v=v.replace("{NBSP}", "&nbsp;");
		v=v.replace("{AL}", "<a rel=\"nofollow\" target=\"_blank\" href=\"");
		v=v.replace("{AT}", "\">");
		v=v.replace("{/AL}", "</a>");
		v=v.replace("{AS}", " title=\"");
		v=v.replace("{/AS}", "\"");
		
		while (v.indexOf("{C:") > -1) {
			int vvi = v.indexOf("{C:");
			v=v.replaceFirst("\\{C:", "<span style=\"color:");
			v=v.substring(0,vvi) + v.substring(vvi).replaceFirst("\\}", "\">");
			v=v.replaceFirst("\\{/C}", "</span>");	
		}

		v = v.replace("{CODE}", "<div class=\"abmcode\"><pre><code>");
		v = v.replace("{/CODE}", "</code></pre></div>");
		while (v.indexOf("{ST:") > -1) {
			int vvi = v.indexOf("{ST:");
			v=v.replaceFirst("\\{ST:", "<span style=\"");			
			v=v.substring(0,vvi) + v.substring(vvi).replaceFirst("\\}", "\">");
			v=v.replaceFirst("\\{/ST}", "</span>");	
		}	
		
		int start = v.indexOf("{IC:");
		while (start > -1) {
			int stop = v.indexOf("{/IC}");
			String vv = "";
			if (stop>0) {
				vv = v.substring(start, stop+5);
			} else {
				break;
			}
			String IconColor = vv.substring(4, 11);
			String IconName = vv.substring(12,vv.length()-5);
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
				repl = "<i>" + svgIconmap.getOrDefault(IconName.toLowerCase(), "") + "</i>"; 
				break;
			default:
				repl = "<i style=\"color: " + IconColor + "\" class=\"material-icons\">" + IconName + "</i>";
			}
			v=v.replace(vv,repl );
			start = v.indexOf("{IC:");
		}
		
		s.append(v);
		
		return s.toString();
	}
	
	protected String BuildTextMsgbox(String text) {
		StringBuilder s = new StringBuilder();	
		
		String v = ABMaterial.HTMLConv().htmlEscape(text, PageCharset);
		v=v.replaceAll("(\r\n|\n\r|\r|\n)", "<br>");
		v=v.replace("{B}", "<b>");
		v=v.replace("{/B}", "</b>");
		v=v.replace("{I}", "<i>");
		v=v.replace("{/I}", "</i>");
		v=v.replace("{U}", "<ins>");
		v=v.replace("{/U}", "</ins>");
		v=v.replace("{SUB}", "<sub>");
		v=v.replace("{/SUB}", "</sub>");
		v=v.replace("{SUP}", "<sup>");
		v=v.replace("{/SUP}", "</sup>");
		v=v.replace("{BR}", "<br>");
		v=v.replace("{WBR}", "<wbr>");
		v=v.replace("{NBSP}", "&nbsp;");
		v=v.replace("{AL}", "<a rel='nofollow' target='_blank' href='");
		v=v.replace("{AT}", "'>");
		v=v.replace("{/AL}", "</a>");
		v=v.replace("{AS}", " title=\"");
		v=v.replace("{/AS}", "\"");
				
		while (v.indexOf("{C:") > -1) {
			int vvi = v.indexOf("{C:");
			v=v.replaceFirst("\\{C:", "<span style=\"color:");
			v=v.substring(0,vvi) + v.substring(vvi).replaceFirst("\\}", "\">");
			v=v.replaceFirst("\\{/C}", "</span>");	
		}

		v = v.replace("{CODE}", "<div class=\"abmcode\"><pre><code>");
		v = v.replace("{/CODE}", "</code></pre></div>");
		while (v.indexOf("{ST:") > -1) {
			int vvi = v.indexOf("{ST:");
			v=v.replaceFirst("\\{ST:", "<span style=\"");			
			v=v.substring(0,vvi) + v.substring(vvi).replaceFirst("\\}", "\">");
			v=v.replaceFirst("\\{/ST}", "</span>");	
		}	
		
		int start = v.indexOf("{IC:");
		while (start > -1) {
			int stop = v.indexOf("{/IC}");
			String vv = "";
			if (stop>0) {
				vv = v.substring(start, stop+5);
			} else {
				break;
			}
			String IconColor = vv.substring(4, 11);
			String IconName = vv.substring(12,vv.length()-5);
			String repl="";
			switch (IconName.substring(0, 3).toLowerCase()) {
			case "mdi":
				repl = "<i style='color: " + IconColor + "' class='" + IconName + "'></i>";
				break;
			case "fa ":
			case "fa-":
				repl = "<i style='color: " + IconColor + "' class='" + IconName + "'></i>";
				break;
			case "abm":
				repl = "<i>" + svgIconmap.getOrDefault(IconName.toLowerCase(), "") + "</i>"; 
				break;
			default:
				repl = "<i style='color: " + IconColor + "' class='material-icons'>" + IconName + "</i>";
			}
			v=v.replace(vv,repl );
			start = v.indexOf("{IC:");
		}
		
		s.append(v);
		
		return s.toString();
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public void B4JSShowToast(String toastId, String themeName, String message, int duration, boolean IsTextSelectable) {
		
	}
	
	public void ShowToast(String toastId, String themeName, String message, int duration, boolean IsTextSelectable) {
		String s = "";
		ThemeToast tt;
		if (themeName.equals("")) {
			tt = this.CompleteTheme.CreateThemeToast();
		} else {
			if (this.CompleteTheme.Toasts.containsKey(themeName.toLowerCase())) {
				tt = this.CompleteTheme.Toasts.get(themeName.toLowerCase()).Clone();
			} else {
				tt = this.CompleteTheme.CreateThemeToast();
			}
		}
		String selectable="";
		if (!IsTextSelectable) {
			selectable = " notselectable ";
		}
		s = "<span id=\"" + toastId.toLowerCase() + "\" class=\"" + ABMaterial.GetColorStr(tt.ForeColor, tt.ForeColorIntensity, "text") + selectable + "\">" + BuildText(message) + "</span>";
		
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(duration);
		if (tt.Rounded) {
			Params.Add("rounded");
		} else {
			Params.Add("");
		}
		Params.Add(s);
		Params.Add(toastId.toLowerCase());
		Params.Add(ABMaterial.GetColorStrMap(tt.BackColor, tt.BackColorIntensity));
		Params.Add(".toast-container" + tt.ThemeName.toLowerCase());
		ws.RunFunction("RunToast", Params);
		try {
			if (ws.getOpen()) {
				if (ShowDebugFlush) {BA.Log("ShowToast");}
				ws.Flush();RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
		
	public void ShowToastWithReturns(String toastId, String themeName, String message, int duration, anywheresoftware.b4a.objects.collections.List linksText, anywheresoftware.b4a.objects.collections.List linksReturnStrings, boolean IsTextSelectable) {
		StringBuilder sb = new StringBuilder();
		ThemeToast tt;
		if (themeName.equals("")) {
			tt = this.CompleteTheme.CreateThemeToast();
		} else {
			if (this.CompleteTheme.Toasts.containsKey(themeName.toLowerCase())) {
				tt = this.CompleteTheme.Toasts.get(themeName.toLowerCase()).Clone();
			} else {
				tt = this.CompleteTheme.CreateThemeToast();
			}
		}
		String selectable="";
		if (!IsTextSelectable) {
			selectable = " notselectable ";
		}
		sb.append("<span id=\"" + toastId.toLowerCase() + "\" class=\"" + ABMaterial.GetColorStr(tt.ForeColor, tt.ForeColorIntensity, "text") + "\">" + BuildText(message) + "</span>");
		if (linksText!=null) {
			for (int i=0;i<linksText.getSize();i++) {
				sb.append("<a onclick=\"toastclick('" + toastId + "','" + linksReturnStrings.Get(i) + "')\" data-dismiss class=\"" + ABMaterial.GetColorStr(tt.ActionForeColor, tt.ActionForeColorIntensity, "text") + selectable + "\" style=\"cursor: pointer; display: inline-block; padding: 0 2rem\">" + BuildText((String) linksText.Get(i)) + "</a>");
				
			}
		}
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(duration);
		if (tt.Rounded) {
			Params.Add("rounded");
		} else {
			Params.Add("");
		}
		Params.Add(sb.toString());
		Params.Add(toastId.toLowerCase());
		Params.Add(ABMaterial.GetColorStrMap(tt.BackColor, tt.BackColorIntensity));
		Params.Add(".toast-container" + tt.ThemeName.toLowerCase());
		ws.RunFunction("RunToast", Params);
		try {
			if (ws.getOpen()) {
				if (ShowDebugFlush) {BA.Log("ShowToastWithReturns");}
				ws.Flush();RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public void DismissToast(String toastId) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(toastId.toLowerCase());
		ws.RunFunction("dismisstoast", Params);
		try {
			if (ws.getOpen()) {
				if (ShowDebugFlush) {BA.Log("DismissToast");}
				ws.Flush();RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}	
			
	public void AddFXToast(String startComponentId, int offset, String toastId, String themeName, String message, int duration) {
		StringBuilder sb = new StringBuilder();
		ThemeToast tt;
		if (themeName.equals("")) {
			tt = this.CompleteTheme.CreateThemeToast();
		} else {
			if (this.CompleteTheme.Toasts.containsKey(themeName.toLowerCase())) {
				tt = this.CompleteTheme.Toasts.get(themeName.toLowerCase()).Clone();
			} else {
				tt = this.CompleteTheme.CreateThemeToast();
			}
		}
		// GetColorStr(tt.BackColor, tt.BackColorIntensity, "") + " " + does not work
		sb.append("<span id=" + toastId.toLowerCase() + " class=" + ABMaterial.GetColorStr(tt.ForeColor, tt.ForeColorIntensity, "text") + ">" + ABMaterial.HTMLConv().htmlEscape(message, PageCharset) + "</span>");
		
		String rounded="";
		if (tt.Rounded) {
			rounded = "rounded";
		}
		
		ScrollFires.add("{selector: '#" + startComponentId.toLowerCase() + "', offset: "+ offset + ", callback: 'RunToast("+duration+", \""+rounded+"\", \""+sb.toString()+"\",\""+toastId.toLowerCase()+"\", \""+ABMaterial.GetColorStrMap(tt.BackColor, tt.BackColorIntensity)+"\", \"toast-container" + tt.ThemeName.toLowerCase()+"\")' }");		

	}	
	
	public void FadeInImage(String imageId ) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(imageId.toLowerCase());
		ws.RunFunction("fadeinimg", Params);		
		try {
			if (ws.getOpen()) {
				if (ShowDebugFlush) {BA.Log("Page FadeInImage");}
				ws.Flush();RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public void AddFXFadeInImage(String startComponentId, int offset, String imageId ) {
		ScrollFires.add("{selector: '#" + startComponentId.toLowerCase() + "', offset: "+ offset + ", callback: 'Materialize.fadeInImage(\"#" + imageId.toLowerCase() + "\")'}");
	}
	
	public void RaiseNextContentOnComponent(ABMComponent component, int offsetBottom) {
		if (ws==null) {
			return;
		}
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(component.ParentString + component.ArrayName.toLowerCase() + component.ID.toLowerCase());
		Params.Add(offsetBottom);
		ws.RunFunction("nextcontent", Params);
		try {
			if (ws.getOpen()) {
				if (ShowDebugFlush) {BA.Log("Page RaiseNextContentOnComponent");}
				ws.Flush();RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public ABMComponent ComponentRowCell(int row, int cell, String componentId) {
		ABMCell ccell = Cell(row, cell);
		if (ccell==null) {
			return null;
		}
		return ccell.Component(componentId);
	}	
	
	public ABMComponent ComponentRowCellWriteOnly(int row, int cell, String componentId) {
		ABMCell ccell = Cell(row, cell);
		if (ccell==null) {
			return null;
		}
		return ccell.ComponentWriteOnly(componentId);
	}	
	
	public ABMComponent Component(String componentId) {
		if (ActionButtons.containsKey(componentId.toLowerCase())) {
			return ActionButtons.get(componentId.toLowerCase());
		}
		ABMRowCell rc = AllComponents.getOrDefault(componentId.toLowerCase(), null);
		if (rc==null) {
			BA.Log("No component found with id " + componentId );
			return null;
		}	
		ABMCell ccell = CellInternal(rc.rowId, rc.cellId);
		if (ccell==null) {
			return null;
		}
		return ccell.Component(componentId);
	}
	
	public ABMComponent ComponentWriteOnly(String componentId) {
		if (ActionButtons.containsKey(componentId.toLowerCase())) {
			return ActionButtons.get(componentId.toLowerCase());
		}
		ABMRowCell rc = AllComponents.getOrDefault(componentId.toLowerCase(), null);
		if (rc==null) {
			BA.Log("No component found with id " + componentId );
			return null;
		}	
		ABMCell ccell = CellInternal(rc.rowId, rc.cellId);
		if (ccell==null) {
			return null;
		}
		return ccell.ComponentWriteOnly(componentId);
	}	
	
	public void ShowModalSheet(String modalSheetId) {
		ABMModalSheet sheet = ModalSheets.getOrDefault(modalSheetId.toLowerCase(), null);
		if (sheet==null) {
			BA.Log("No modal sheet found with id=" + modalSheetId);
			return;
		}
		sheet.RefreshInternalExtra(false, false,false);
		ABMaterial.ShowModal(this,modalSheetId, sheet.IsDismissible, sheet.Size);
	}
	
	
	/**
	* if using fixedWidth then cellWidth is overruled
	 */
	public void ShowModalSheetRelativeCell(String modalSheetId, int row, int cell, int cellWidth, String fixedWidth) {
		ABMModalSheet sheet = ModalSheets.getOrDefault(modalSheetId.toLowerCase(), null);
		if (sheet==null) {
			BA.Log("No modal sheet found with id=" + modalSheetId);
			return;
		}
		sheet.Refresh();
		ABMCell c = Cell(row,cell);
		if (c!=null) {
			ABMaterial.ShowModalRelativeCell(this,modalSheetId, sheet.IsDismissible, c.ParentString + c.ArrayName.toLowerCase() + c.ID.toLowerCase(),cellWidth, fixedWidth);
		}
	}
	
	public void ShowModalSheetRelativeComponent(String modalSheetId, ABMComponent component, String fixedWidth) {
		ABMModalSheet sheet = ModalSheets.getOrDefault(modalSheetId.toLowerCase(), null);
		if (sheet==null) {
			BA.Log("No modal sheet found with id=" + modalSheetId);
			return;
		}
		sheet.Refresh();		
		ABMaterial.ShowModalRelativeComponent(this,modalSheetId, sheet.IsDismissible, component.ParentString + component.ArrayName.toLowerCase() + component.ID.toLowerCase(),fixedWidth);
		
	}
	
	public void ShowModalSheetAbsolute(String modalSheetId, String left, String top, String width, String height) {
		ABMModalSheet sheet = ModalSheets.getOrDefault(modalSheetId.toLowerCase(), null);
		if (sheet==null) {
			BA.Log("No modal sheet found with id=" + modalSheetId);
			return;
		}
		sheet.Refresh();		
		ABMaterial.ShowModalAbsolute(this,modalSheetId, sheet.IsDismissible, left, top, width, height);
		
	}
		
	public void CloseModalSheet(String modalSheetId) {
		ABMModalSheet sheet = ModalSheets.getOrDefault(modalSheetId.toLowerCase(), null);
		if (sheet==null) {
			BA.Log("No modal sheet found with id=" + modalSheetId);
			return;
		}
		ABMaterial.CloseModal(this,modalSheetId);
	}
	
	/**
	 * if you make this call right after CloseModalSheet it will still return true.
	 * it has to check in the browser what the display state is of the modal sheet as on the server side it can never be sure.
	 * 
	 * You can put in a little delay using e.g. Sleep(300) so that the 'closing animation' in the browser has finished.
	 */
	public boolean IsModalsheetOpen(String modalSheetId) {
		ABMModalSheet sheet = ModalSheets.getOrDefault(modalSheetId.toLowerCase(), null);
		if (sheet==null) {
			BA.Log("No modal sheet found with id=" + modalSheetId);
			return false;
		}
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(modalSheetId.toLowerCase());
		SimpleFuture j = ws.RunFunctionWithResult("IsModalOpen", Params);			
		if (j!=null) {
			try {
				return (boolean) j.getValue();				
			} catch (InterruptedException e) {
				//e.printStackTrace();
			} catch (TimeoutException e) {
				//e.printStackTrace();
			} catch (ExecutionException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			}				
		}
		return false;
	}
		
	public void AddModalSheetTemplate(ABMModalSheet sheet) {
		ModalSheets.put(sheet.ID.toLowerCase(), sheet);		
	}
	
	public void AddFloatingContainer(ABMContainer floatingContainer, String fromTopOrBottom, String topOrBottomValue) {
		floatingContainer.FromTopOrBottom = fromTopOrBottom;
		floatingContainer.TopOrBottomValue = topOrBottomValue;
		FloatingContainers.put(floatingContainer.ID.toLowerCase(), floatingContainer);
	}
	
	public void AddFloatingContainerHideOnScroll(ABMContainer floatingContainer, String fromTopOrBottom, String topOrBottomValue, double onScrollStartOpacity, double onScrollStopOpacity) {
		floatingContainer.FromTopOrBottom = fromTopOrBottom;
		floatingContainer.TopOrBottomValue = topOrBottomValue;
		floatingContainer.OnScrollStartOpacity=onScrollStartOpacity;
		floatingContainer.OnScrollStopOpacity=onScrollStopOpacity;
		FloatingContainers.put(floatingContainer.ID.toLowerCase(), floatingContainer);
	}
	
	public ABMModalSheet ModalSheet(String modalSheetId) {
		ABMModalSheet sheet = ModalSheets.getOrDefault(modalSheetId.toLowerCase(), null);
		if (sheet==null) {
			BA.Log("No modal sheet found with id=" + modalSheetId);
			return null;
		}
		return sheet;
	}
	
	public ABMContainer FloatingContainer(String floatingContainerId) {
		ABMContainer cont = FloatingContainers.getOrDefault(floatingContainerId.toLowerCase(), null);
		if (cont==null) {
			BA.Log("No floating container found with id=" + floatingContainerId);
			return null;
		}
		cont.Refresh();
		return cont;		
	}
		
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 * 
	 * type: e.g. "GET" or "POST"
	 * dataType: e.g. "text", "html", ""application/json; charset=utf-8"", ...
	 * 
	 * returnB4JSClass: put an empty string if you want to raise the events to the ABMPage instead on the server
	 */
	public void B4JSCallAjax(String uniqueId, String url, String type, String dataType, String data, String returnB4JSClass) {
		
	}
	
	/*
	 * type: e.g. "GET" or "POST"
	 * dataType: e.g. "text", "html", ""application/json; charset=utf-8"", ...
	 */
	public void CallAjax(String uniqueId, String url, String type, String dataType, String data) {		
		// url, type, dataType, data, uniqueid, fromb4js
		if (ws!=null) {		
			anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
			Params.Initialize();
			Params.Add(url);
			Params.Add(type);
			Params.Add(dataType);
			Params.Add(data);
			Params.Add(uniqueId);
			Params.Add(false);
			Params.Add("");
			
			ws.RunFunction("callAjax", Params);
			try {
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("Call Ajax");}
					ws.Flush();RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
		
	}
	
	public void AddActionButton(ABMActionButton actionButton) {
		actionButton.ForPage=true;
		this.NeedsActionButton=true;
		ActionButtons.put(actionButton.ID.toLowerCase(), actionButton);
	}
	
	/**
	 * Use default 0 margin-top, 20 margin-bottom 
	 */
	public RowDef AddRows(int numberOfRows, boolean centerInPage, String themeName) {
		return Grid.AddRows(numberOfRows, centerInPage, themeName);
	}
	
	public RowDef AddRowsM(int numberOfRows, boolean centerInPage, int marginTopPx, int marginBottomPx, String themeName) {
		return Grid.AddRowsM(numberOfRows, centerInPage, marginTopPx, marginBottomPx, themeName);
	}
	
	public RowDef AddRowsM2(int numberOfRows, boolean centerInPage, int marginTopPx, int marginBottomPx, int marginLeftPx, int marginRightPx, String themeName) {
		return Grid.AddRowsM2(numberOfRows, centerInPage, marginTopPx, marginBottomPx, marginLeftPx, marginRightPx, themeName);
	}
	
	/**
	 * creates one row with the number of cells specified, all equal in size 
	 * Possible values for numberOfCells are: 1, 2, 3, 4, 6, 12  
	 */
	public RowDef AddRow(int numberOfCells, boolean centerInPage, String rowThemeName, String cellThemeName) {
		return Grid.AddRow(numberOfCells, centerInPage, rowThemeName, cellThemeName);
	}
	
	/**
	 * Use default 0 margin-top, 20 margin-bottom 
	 */
	public RowDef AddRowsV(int numberOfRows, boolean centerInPage, String visibility, String themeName) {
		return Grid.AddRowsV(numberOfRows, centerInPage, visibility, themeName);
	}
	
	public RowDef AddRowsMV(int numberOfRows, boolean centerInPage, int marginTopPx, int marginBottomPx, String visibility, String themeName) {
		return Grid.AddRowsMV(numberOfRows, centerInPage, marginTopPx, marginBottomPx, visibility, themeName);
	}
	
	public RowDef AddRowsMV2(int numberOfRows, boolean centerInPage, int marginTopPx, int marginBottomPx, int marginLeftPx, int marginRightPx, String visibility, String themeName) {
		return Grid.AddRowsMV2(numberOfRows, centerInPage, marginTopPx, marginBottomPx, marginLeftPx, marginRightPx, visibility, themeName);
	}
	
	/**
	 * creates one row with the number of cells specified, all equal in size 
	 * Possible values for numberOfCells are: 1, 2, 3, 4, 6, 12  
	 */
	public RowDef AddRow(int numberOfCells, boolean centerInPage, String rowThemeName, String visibility, String cellThemeName) {
		return Grid.AddRowV(numberOfCells, centerInPage, rowThemeName, visibility, cellThemeName);
	}
		
	public void BuildGrid() {
		AddGridDefinition(Grid);
	}
	
	@Hide
	protected void AddGridDefinition(ABMGridDefinition gridDef) {
		int rCounter=1;
		for (RowDef row: gridDef.Rows) {
			if (!row.IsBuild) {
				for (int i=0;i<row.NumberOfrows;i++) {
					switch (row.RowType) {
					case "LayoutFixedFluidFixed":
						ABMRow r= new ABMRow();
						r.RowType = row.RowType;
						r.ID = gridDef.GridBaseId + rCounter;
						r.Page= this;
						
						CellDef cell2 = row.cellDef;
						int cCounter2=1;
						while (cell2!=null) {
							ABMCell c = new ABMCell();
							c.CellInfo = "page.cell(" + rCounter + "," + cCounter2 + ")";
							c.CellType = cell2.CellType;
							c.CellWidth = cell2.Width;
							c.ID = gridDef.GridBaseId + rCounter + "C" + cCounter2;						
							c.Page = this;
							c.RowId = r.ID;
							c.mVisibility=cell2.Visibility;
							r.Cells.put(c.ID.toLowerCase(), c);	
							cCounter2++;
							cell2 = cell2.cellDef;
						}					
						
						break;
					default:
						AddRowInternal(gridDef.GridBaseId + rCounter, row.CenterInPage, row.MarginTopPx, row.MarginBottomPx, row.MarginLeftPx, row.MarginRightPx, row.Visibility, row.ThemeName);
						CellDef cell = row.cellDef;
						int cCounter=1;
						while (cell!=null) {
							for (int j=0;j<cell.NumberOfCells;j++) {
								AddCell(gridDef.GridBaseId + rCounter, gridDef.GridBaseId + rCounter + "C" + cCounter, cell.OffsetSmall, cell.OffsetMedium, cell.OffsetLarge, cell.SizeSmall, cell.SizeMedium, cell.SizeLarge, cell.MarginTopPx, cell.MarginBottomPx, cell.PaddingLeftPx, cell.PaddingRightPx, cell.Visibility, cell.ThemeName, "" + rCounter + "," + cCounter);
								cCounter++;
							}
							cell = cell.cellDef;						
						}
					}
					
					rCounter++;
				}
				row.IsBuild=true;
			} else {
				rCounter+=row.NumberOfrows;
			}
		}
	}
	
	public void DebugPrintGrid() {
		BA.Log(Grid.DebugPrintGrid(""));
	}
		
	@Hide
	public void AddRowInternal(String rowId, boolean centerInPage, int marginTopPx, int marginBottomPx, int marginLeftPx, int marginRightPx, String visibility, String themeName) {
		AddRowToCell("", rowId, centerInPage, marginTopPx, marginBottomPx, marginLeftPx, marginRightPx, visibility, themeName);
	}
	
	@Hide
	public void AddRowToCell(String cellId, String rowId, boolean centerInPage, int marginTopPx, int marginBottomPx, int marginLeftPx, int marginRightPx, String visibility, String themeName) {
		ABMRow row = new ABMRow();
		row.ID = rowId;	
		row.CenterInPage = centerInPage;
		row.Page= this;
		row.MarginTop=marginTopPx + "px";
		row.MarginBottom=marginBottomPx + "px";
		if (marginLeftPx!=-9999) {
			row.MarginLeft = marginLeftPx + "px";
		}
		if (marginRightPx!=-9999) {
			row.MarginRight = marginRightPx + "px";
		}
		row.mVisibility=visibility;
		if (!themeName.equals("")) {
			if (CompleteTheme.Rows.containsKey(themeName.toLowerCase())) {
				row.Theme = CompleteTheme.Rows.get(themeName.toLowerCase()).Clone();				
			}
		}
		
		Rows.put(rowId.toLowerCase(),row);		
	}	
	
	public void StaticFilesFolder(String staticFilesFolder, String appName) {
		this.mStaticFilesFolder = staticFilesFolder;
		this.mAppName = appName;
	}	
		
	public ABMRow Row(int row) {
		String rowId="R" + row;
		return RowInternal(rowId);
	}	
	
	protected ABMRow RowInternal(String rowId) {
		ABMRow rrow = Rows.getOrDefault(rowId.toLowerCase(), null);
		if (rrow==null) {
			BA.Log("No row found with rowId=" + rowId + "!");
			return null;
		}
		return rrow;
	}
	
	@Hide
	public void AddCell(String rowId, String cellId, int offsetSmall, int offsetMedium, int offsetLarge, int sizeSmall, int sizeMedium, int sizeLarge, int marginTopPx, int marginBottomPx, int paddingLeftPx, int paddingRightPx, String visibility, String themeName, String cellInfo) {		
		ABMRow row = RowInternal(rowId);
		if (row==null) {
			return;
		}
		ABMCell cell = new ABMCell();
		cell.CellInfo = "page.cell(" + cellInfo + ")";
		cell.ID = cellId;
		cell.PaddingLeft=paddingLeftPx + "px";
		cell.PaddingRight=paddingRightPx + "px";
		cell.MarginTop=marginTopPx + "px";
		cell.MarginBottom=marginBottomPx + "px";
		cell.OffsetSmall = offsetSmall;
		cell.OffsetMedium = offsetMedium;
		cell.OffsetLarge = offsetLarge;
		cell.SizeSmall = sizeSmall;
		cell.SizeMedium = sizeMedium;
		cell.SizeLarge = sizeLarge;			
		cell.Page = this;
		cell.RowId = rowId;
		cell.mVisibility=visibility;
		if (!themeName.equals("")) {
			if (CompleteTheme.Cells.containsKey(themeName.toLowerCase())) {
				cell.Theme = CompleteTheme.Cells.get(themeName.toLowerCase()).Clone();				
			}
		}
		row.Cells.put(cellId.toLowerCase(), cell);
	}	
	
	public ABMCell Cell(int row, int cell) {
		CurrentRow=row;
		ABMRow rrow = RowInternal("R" + row);
		if (rrow==null) {
			return null;
		}
		return rrow.Cell(cell);		
	}
	
	public ABMCell CellR(int moveNumberOfRows, int cell) {
		CurrentRow+=moveNumberOfRows;
		ABMRow rrow = RowInternal("R" + CurrentRow);
		if (rrow==null) {
			return null;
		}
		return rrow.Cell(cell);		
	}
	
	protected ABMCell CellInternal(String rowId, String cellId) {
		ABMRow rrow = RowInternal(rowId);
		if (rrow==null) {
			return null;
		}
		return rrow.CellInternal(cellId);	
	}
			
	public ThemePage getTheme() {
		if (this.Theme==null) {
			this.Theme = CompleteTheme.Page.Clone();
		}
		return this.Theme;
	}
		
	public String getName() {
		return this.Name;
	}
		
	public String getHandler() {
		return this.Handler;
	}	
			
	public void SetWebsocket(WebSocket webSocket) {
		this.ws = webSocket;
	}
	
	public WebSocket GetWebsocket() {
		return this.ws;
	}
	
	@Hide
	protected String Build() {
		AddGridDefinition(Grid);
		StringBuilder s = new StringBuilder();
		
		if (!Rows.isEmpty()) {			
			s.append(BuildBody());			
		}
		for (Entry<String, ABMModalSheet> sheet : ModalSheets.entrySet()) {
			s.append(sheet.getValue().Build());							
        }
		for (Entry<String, ABMContainer> cont : FloatingContainers.entrySet()) {
			s.append(cont.getValue().Build());							
        }
		for (Entry<String, ABMActionButton> button : ActionButtons.entrySet()) {
			s.append(button.getValue().Build());							
        }		
		IsBuild=true;
		return s.toString();
	}
	
	@Hide
	protected String BuildBody() {
		StringBuilder s = new StringBuilder();
		ABMSection section = null;
		ABMSection CloseSection = null;
		for (Entry<String, ABMRow> row : Rows.entrySet()) {
			section = this.GetIfStartSection(row.getValue().ID);
			if (section!=null) {
				Section st = Theme.GetSection(section.ThemeSectionName.toLowerCase());
				String mHeight="";
				if (!section.MinHeight.equals("")) {
					mHeight = " min-height: " + section.MinHeight + ";";
				}
				if (!section.ButtonJumpToSectionID.equals("")) {					
					s.append("<div id=\"" + section.SectionName.toLowerCase() + "\" class=\"" + st.Name.toLowerCase() + "-" + st.NavigationButtonType + " " + ABMaterial.GetColorStr(section.Color, section.ColorIntensity, "") + "\" style=\"" + section.BackgroundImage + mHeight + " position: relative;\">");
				} else {
					s.append("<div id=\"" + section.SectionName.toLowerCase() + "\" class=\"" + ABMaterial.GetColorStr(section.Color, section.ColorIntensity, "") + "\" style=\"" + section.BackgroundImage + mHeight + " position: relative\">");
				}
				CloseSection = section.Clone();
			}
			s.append(row.getValue().Build());
			if (CloseSection!=null) {
				if (CloseSection.ToRow.equals(row.getValue().ID)) {
					if (!CloseSection.ButtonJumpToSectionID.equals("")) {	
						String Up="";
						if (CloseSection.ButtonType.equals("up")) {
							Up = " abmup ";
						}
						s.append("<a class=\"abmnextsectionbutton " + Up + "\" data-speed=\"" + CloseSection.ScrollSpeed + "\" data-top=\"" + CloseSection.ScrollTop + "\" href=\"#" + CloseSection.ButtonJumpToSectionID + "\"><span></span>" + BuildText(CloseSection.ButtonText) + "</a>");
					}
					s.append("</div>");
					CloseSection = null;
				}
			}
        }
		if (CloseSection!=null) {
			if (!CloseSection.ButtonJumpToSectionID.equals("")) {	
				String Up="";
				if (CloseSection.ButtonType.equals("up")) {
					Up = " abmup ";
				}
				s.append("<a class=\"abmnextsectionbutton " + Up + "\" data-speed=\"" + CloseSection.ScrollSpeed + "\" data-top=\"" + CloseSection.ScrollTop + "\" href=\"#" + CloseSection.ButtonJumpToSectionID + "\"><span></span>" + BuildText(CloseSection.ButtonText) + "</a>");
			}
			s.append("</div>");
			CloseSection = null;	
		}
		boolean NeedsSectionMenu=false;
		for (ABMSection sect: Sections) {
			if (!sect.MenuJumpToSectionID.equals("")) {
				NeedsSectionMenu=true;
				break;
			}
		}
		if (NeedsSectionMenu) {
			s.append("<div class=\"section-menu\" id=\"section-menu\"><ul>");
			for (ABMSection sect: Sections) {
				if (!sect.MenuJumpToSectionID.equals("")) {
					s.append("<a id=\"" + sect.SectionName.toLowerCase() + "-section-menu\" href=\"#" + sect.MenuJumpToSectionID + "\" class=\"abmnextsectionmenu abmnextsectionmenu" + sect.SectionName.toLowerCase() + " " + sect.Visibility + "\" data-speed=\"" + sect.ScrollSpeed + "\" data-top=\"" + sect.ScrollTop + "\">");
					s.append("<span>" + BuildText(sect.MenuText) + "</span>");
					s.append("</a>");
				}
			}
			s.append("</ul></div>");
		}
		return s.toString();
	}
	
	protected void CheckIfInBrowser(StringBuilder ToCheckSB) {		
		for (Entry<String, ABMRow> row : Rows.entrySet()) {	
			row.getValue().CheckIfInBrowser(ToCheckSB);
		}	
		for (Entry<String, ABMModalSheet> sheet : ModalSheets.entrySet()) {			
			sheet.getValue().CheckIfInBrowser(ToCheckSB);										
        }
		for (Entry<String, ABMContainer> cont : FloatingContainers.entrySet()) {
			cont.getValue().CheckIfInBrowser(ToCheckSB);												
        }
		for (Entry<String, ABMActionButton> button : ActionButtons.entrySet()) {
			button.getValue().CheckIfInBrowser(ToCheckSB);				
        }		
		for (Entry<String,ABMCanvas> ec: Canvases.entrySet()) {
			ec.getValue().CheckIfInBrowser(ToCheckSB);	
		}
	}
	
	protected boolean ResetNotInBrowser(String ToCheck, String OKCheck) {
		boolean ret = true;		
		for (Entry<String, ABMRow> row : Rows.entrySet()) {	
			if (!row.getValue().ResetNotInBrowser(ToCheck, OKCheck)) {
				ret = false;
			}
		}
		
		for (Entry<String, ABMModalSheet> sheet : ModalSheets.entrySet()) {			
			if (!sheet.getValue().ResetNotInBrowser(ToCheck, OKCheck)) {
				ret = false;
			}									
        }
		for (Entry<String, ABMContainer> cont : FloatingContainers.entrySet()) {
			if (!cont.getValue().ResetNotInBrowser(ToCheck, OKCheck)) {
				ret = false;
			}												
        }
		for (Entry<String, ABMActionButton> button : ActionButtons.entrySet()) {
			if (!button.getValue().ResetNotInBrowser(ToCheck, OKCheck)) {
				ret = false;
			}				
        }		
		for (Entry<String,ABMCanvas> ec: Canvases.entrySet()) {
			if (!ec.getValue().ResetNotInBrowser(ToCheck, OKCheck)) {
				ret = false;
			}	
		}
        
		return ret;
	}
				
	public void Refresh() {			
		StringBuilder ToCheckSB = new StringBuilder();
		CheckIfInBrowser(ToCheckSB);
		
		String ToCheck=ToCheckSB.toString();	
		String OKCheck="";

		SimpleFuture fut = ws.EvalWithResult("var notExists=';$%';var Exists='$%';var varbody=document.body.innerHTML;" + ToCheck + "return notExists + ':' + Exists + ':';", null);
		ToCheck="";
		
		try {				
			ToCheck = (String) fut.getValue();
			String[] spl = ToCheck.split(":");
			ToCheck = spl[0];
			OKCheck = spl[1];
		} catch (InterruptedException e) {				
		} catch (TimeoutException e) {						
		} catch (ExecutionException e) {						
		} catch (IOException e) {						
		}
		
		ResetNotInBrowser(ToCheck, OKCheck);		
		
		AddGridDefinition(Grid);	
		
		if (navigationBar!=null) {
			navigationBar.Refresh();
		}
		
		int Counter=0;
		String prevID="";
		boolean isContainer=false;
		for (Entry<String, ABMRow> row : Rows.entrySet()) {	
			boolean wasBuild = row.getValue().IsBuild;
			if (wasBuild) {
				row.getValue().RefreshInternal(false, false, false);	
				prevID = row.getValue().RootID();
				isContainer = row.getValue().CenterInPage;
			} else {
				if (Counter==0) {						
					ABMaterial.AddHTMLToMain(this, row.getValue().Build());
					prevID = row.getValue().RootID();
					isContainer = row.getValue().CenterInPage;
				} else {
					if (isContainer) {
						ABMaterial.InsertHTMLAfter(this, row.getValue().ParentString + prevID + "_cont", row.getValue().Build());
					} else {
						ABMaterial.InsertHTMLAfter(this, row.getValue().ParentString + prevID, row.getValue().Build());
					}
					prevID = row.getValue().RootID();
					isContainer = row.getValue().CenterInPage;
				}						
			}
			if (!wasBuild && ABMaterial.IsSinglePageApp) {
				row.getValue().RefreshInternal(false, false, false);	
			}
			
			Counter++;
		}	
		
		for (Entry<String, ABMModalSheet> sheet : ModalSheets.entrySet()) {
			if (sheet.getValue().IsBuild) {
				sheet.getValue().RefreshInternalExtra(false,false,false);
			} else {
				ABMaterial.AddHTMLPageComponent(this, sheet.getValue().Build());
				sheet.getValue().RefreshInternalExtra(false,false,false);
			}									
        }
		for (Entry<String, ABMContainer> cont : FloatingContainers.entrySet()) {
			if (cont.getValue().IsBuild) {
				cont.getValue().RefreshInternalExtra(false,false,false);
			} else {
				ABMaterial.AddHTMLPageComponent(this, cont.getValue().Build());
				cont.getValue().RefreshInternalExtra(false,false,false);
			}									
        }
		for (Entry<String, ABMActionButton> button : ActionButtons.entrySet()) {
			if (button.getValue().IsBuild) {
				button.getValue().RefreshInternal(false);
			} else {
				ABMaterial.AddHTMLPageComponent(this, button.getValue().Build());
				button.getValue().RefreshInternal(false);
			}						
        }	
		StringBuilder ss = new StringBuilder();
		if (dragDropGroups.size()>0) {
			ss.append("window.removeEventListener( 'touchmove', function() {});");
			ss.append("window.addEventListener( 'touchmove', function() {});");
			ss.append("if (drake) {drake.destroy();}\n");
			ss.append("drake=dragula([");
			int cellTell=0;
			String headerIDs="";
			String footerIDs="";
			for (Entry<String,ABMDragDropGroup> entry: dragDropGroups.entrySet()) {
				ABMDragDropGroup ddg = entry.getValue();
				for (Entry<String,ABMCell> entryCell: ddg.Cells.entrySet()) {
					DragDropCells.put(entryCell.getValue().ParentString + entryCell.getValue().RootID(), entryCell.getValue());
					if (cellTell>0) {
						ss.append(",document.querySelector('#" + entryCell.getValue().ParentString + entryCell.getValue().RootID() + "')");
					} else {
						ss.append("document.querySelector('#" + entryCell.getValue().ParentString + entryCell.getValue().RootID() + "')");
					}
					for (Entry<String,ABMComponent> entryComp: entryCell.getValue().Components.entrySet()) {
						ABMComponent comp = entryComp.getValue();
						if (comp.DragDropIsHeader) {
							if (headerIDs.equals("")) {
								headerIDs = ":" + comp.getID() + ":";
							} else {
								headerIDs = headerIDs + comp.getID() + ":";
							}
						}
					}
					cellTell++;
				}
			}
			ss.append("]\n, {");
			ss.append("isContainer: function (el) {");
			ss.append("return false;");
			ss.append("},");
			ss.append("moves: function (el, source, handle, sibling) {");
			ss.append("return $(el).attr('data-dropto');");
			ss.append("},");
			ss.append("accepts: function (el, target, source, sibling) {");
			ss.append("if ($(el).attr('data-dropto')) { ");
			ss.append("return ($(el).attr('data-dropto').indexOf($(target).attr('data-dgn')) !== -1);");
			ss.append("}\n");
			ss.append("return false;");
			ss.append("},");
			ss.append("invalid: function (el, handle) {");
			ss.append("if ($(el).hasClass('gm-style')) {return true;}");
			ss.append("return false;");
			ss.append("},");				
			ss.append("direction: 'vertical',");             
			ss.append("copy: false,");          
			ss.append("copySortSource: false,");             
			ss.append("revertOnSpill: true,");              
			ss.append("removeOnSpill: false,");              
			ss.append("mirrorContainer: document.body,");    
			ss.append("ignoreInputTextSelection: true,");
			ss.append("headerIDs: '" + headerIDs + "',");
			ss.append("footerIDs: '" + footerIDs + "',");
			ss.append("}).on('drop', function(el, target, source, sibling) {");
			for (Entry<String,ABMDragDropGroup> entry: dragDropGroups.entrySet()) {
				ABMDragDropGroup ddg = entry.getValue();
				ss.append("$(\"[data-ddgn='" + entry.getKey() + "']\").matchHeight({minHeight: " + ddg.minHeight + "});");
			}
			ss.append("if (sibling) {");
			ss.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_dropped','eventparams':'component,source,target,before','component':$(el).attr('id'), 'source':$(source).attr('id'), 'target': $(target).attr('id'), 'before': $(sibling).attr('id')});");
			ss.append("} else {");
			ss.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_dropped','eventparams':'component,source,target,before','component':$(el).attr('id'), 'source':$(source).attr('id'), 'target': $(target).attr('id'), 'before': ''});");
			ss.append("}");
			ss.append("}).on('drag', function(el,source) {;\n");
			ss.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_dragstart','eventparams':'component,source','component':$(el).attr('id'), 'source':$(source).attr('id')});");		
			ss.append("}).on('cancel', function(el,container,source) {;\n");
			ss.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_dragcancelled','eventparams':'component,source','component':$(el).attr('id'), 'source':$(source).attr('id')});");		
			ss.append("});\n");
			for (Entry<String,ABMDragDropGroup> entry: dragDropGroups.entrySet()) {
				ABMDragDropGroup ddg = entry.getValue();
				ss.append("$(\"[data-ddgn='" + entry.getKey() + "']\").matchHeight({minHeight: " + ddg.minHeight + "});");
			}		
			
		}
		if (!ss.toString().equals("")) {	
			ws.Eval(ss.toString(), null);
		}
		
		for (Entry<String,ABMCanvas> ec: Canvases.entrySet()) {
			ec.getValue().RefreshInternal(false);
		}
		
		Footer.RefreshInternalExtra(false,false,false);
				
		ws.Eval("Materialize.updateTextFields();", null);
		
		StringBuilder s = new StringBuilder();
		if (this.navigationBar!=null) {
			if (!this.navigationBar.SideItems.isEmpty()) {		
				s.append("if ($('.nav-wrapper').length>0) {");
				s.append("var parWidth = $('.nav-wrapper').width();");
				s.append("var myLeft = $('#pagenavbar').position().left;");
				s.append("var myRight = parseInt($('#sidenavbutton').css('margin-left'))*2 + $('#sidenavbutton').outerWidth();");	
				s.append("if ($('.nav-wrapper ul.right:last-child').length>0) {");
				s.append("myRight = parWidth - $('.nav-wrapper ul.right:last-child').position().left;");
				s.append("}");
				s.append("if (myLeft<56) {myLeft = 56;}");
				s.append("if ((parWidth - myLeft - myRight) < 10) {$('#pagenavbar').outerWidth(0);} else {$('#pagenavbar').outerWidth(parWidth - myLeft - myRight);}");
				s.append("}");
			}
		}

		if (this.ShowGridInfo) {
			s.append("$('.col:not(.input-field)').each(function() {");
			s.append("$(this).css('border','1px solid red');});");
		}

		s.append("setTimeout(function() {");
        s.append("resizecells();");
        s.append("}, 200);");
		if (NeedsResponsiveContainer) {
			s.append("ElementQueries.update();");
		}
		ws.Eval(s.toString(), null);
		try {
			if (ws.getOpen()) {
				if (ShowDebugFlush) {BA.Log("Page Refresh");}
				ws.Flush();RunFlushed();				
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}				
	}
	
	public void ForceResizeCells(int delayMs, boolean DoFlush) {
		ws.Eval("setTimeout(function() { resizecellsOnly() }, " + delayMs + ")", null);
		if (DoFlush) {
			try {
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("ForceResizeCells");}
					ws.Flush();RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
	}
	
	public void ProcessDroppedEvent(anywheresoftware.b4a.objects.collections.Map params) {
		ABMCell source = DragDropCells.getOrDefault((String)params.Get("source"), null);
				
		if (source==null) {
			BA.Log("Source cell " + (String)params.Get("source") + " not found");
			return;
		}
		
		ABMCell target = DragDropCells.getOrDefault((String)params.Get("target"), null);
		if (target==null) {
			BA.Log("Target cell " + (String)params.Get("target") + " not found");
			return;
		}
		
		String compStr = (String)params.Get("component");
		int k = compStr.lastIndexOf("-");
		if (k>-1) {
			compStr = compStr.substring(k+1);
		}
		ABMComponent comp = source.RemoveComponentForMove(compStr);
		if (comp==null) {
			BA.Log("Component " + (String)params.Get("component") + " not found");
			return;
		}
		
		if (((String)params.Get("before")).equals("")) {
			target.AddComponentFromMove(null, comp);
		} else {
			String before = (String)params.Get("before");
			int j = before.lastIndexOf("-");
			if (j>-1) {
				before = before.substring(j+1);
			}
			ABMComponent beforeComp = target.Components.get(before.toLowerCase());
			if (beforeComp==null) {
				BA.Log("Before component " + (String)params.Get("before") + " not found");
				return;
			}
			target.AddComponentFromMove(beforeComp, comp);			
		}		
	}
		
	public void Prepare() {
		IsBuild=true;
		if (!(navigationBar==null)) {
			navigationBar.Prepare();
		}		
		for (Entry<String,ABMCanvas> ec: Canvases.entrySet()) {
			ABMaterial.CanvasRefresh(this, ec.getValue(), true);
		}			
		for (Entry<String, ABMRow> row : Rows.entrySet()) {
			row.getValue().Prepare();							
        }
		for (Entry<String, ABMModalSheet> sheet : ModalSheets.entrySet()) {
			sheet.getValue().Prepare();							
        }
		for (Entry<String, ABMContainer> cont : FloatingContainers.entrySet()) {
			cont.getValue().Prepare();							
        }
		for (Entry<String,ABMCustomComponent> cc: customcomps.entrySet()) {
			cc.getValue().FirstRun();
		}	
		Footer.Prepare();
		
		try {
			if (ws.getOpen()) {
				if (ShowDebugFlush) {BA.Log("Page Prepare");}
				ws.Flush();RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		
		ABMaterial.ShowPage(this);		
	}	
	
	public ABMNavigationBar NavigationBar() {
		if (navigationBar==null) {
			navigationBar = new ABMNavigationBar();
		}
		return navigationBar;
	}
	
	public void Pause() {
		if (IsPaused) return;
		IsPaused=true;
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		ws.RunFunction("PauseApp", Params);
		try {
			if (ws.getOpen()) {
				if (ShowDebugFlush) {BA.Log("Page PauseApp");}
				ws.Flush();RunFlushed();
			}		
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	/**
	 * pause the app if page.resume is not called after milliSeconds 
	 */
	public void PauseDelayed(int milliSeconds) {
		if (IsPaused) return;
		scheduler = Executors.newScheduledThreadPool(1);
		pauseTimer = scheduler.schedule(new Runnable() {
            @Override
            public void run() {
            	if (IsPaused==false) {
            		IsPaused=true;
            		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
            		Params.Initialize();
            		ws.RunFunction("PauseApp", Params);
            		try {
            			if (ws.getOpen()) {
            				if (ShowDebugFlush) {BA.Log("Page PauseApp");}
            				ws.Flush();RunFlushed();
            			}		
            		} catch (IOException e) {			
            			e.printStackTrace();
            		}
            	}            	
                
            }}, milliSeconds, TimeUnit.MILLISECONDS);		
	}
	
	public void Resume() {
		if (pauseTimer!=null) {
			IsPaused=false;
			pauseTimer.cancel(false);	
			scheduler.shutdown();
		}
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		ws.RunFunction("ResumeApp", Params);
		try {
			if (ws.getOpen()) {
				if (ShowDebugFlush) {BA.Log("Page ResumeApp");}
				ws.Flush();RunFlushed();
			}	
		} catch (IOException e) {			
			e.printStackTrace();
		}
		IsPaused=false;
	}
	
	
	public boolean IsCookiesEnabled() {
		boolean isEnabled=false;
		if (ws!=null) {
			SimpleFuture j = ws.EvalWithResult("return Cookies.enabled;", null);			
			if (j!=null) {
				try {
					isEnabled = (boolean) j.getValue();				
				} catch (InterruptedException e) {
					//e.printStackTrace();
				} catch (TimeoutException e) {
					//e.printStackTrace();
				} catch (ExecutionException e) {
					//e.printStackTrace();
				} catch (IOException e) {
					//e.printStackTrace();
				}	
			}
		}
		return isEnabled;
	}
	
	
	/**
	 * Example options: "domain: 'www.example.com', secure: true, expires: '01/01/2017'"
	 */	
	public void CookieSet(String key, String value, String options) {
		if (ws!=null) {
			if (!options.equals("")) {
				ws.Eval("if (Cookies.enabled) {Cookies.set('" + key + "', '" + value + "', {" + options + "});};", null);
			} else {
				ws.Eval("if (Cookies.enabled) {Cookies.set('" + key + "', '" + value+ "');};", null);
			}
			try {
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("Page CookieSet");}
					ws.Flush();RunFlushed();
				}		
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}					
	}
	
	public String CookieGet(String key) {
		
		if (ws!=null) {
			return ABMaterial.CookieGet(this, key);
		}
		return "";
	}
	
	/**
	 * Example options: "expires: '01/01/2017'"
	 */
	public void CookieExpire(String key, String options) {
		if (ws!=null) {
			if (!options.equals("")) {
				ws.Eval("if (Cookies.enabled) {Cookies.expire('" + key + "', {" + options + "});};", null);
			} else {
				ws.Eval("if (Cookies.enabled) {Cookies.expire('" + key + "');};", null);
			}
			try {
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("Page CookieExpire");}
					ws.Flush();RunFlushed();
				}			
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}	
	}
	
	protected void WritePageToDisk(String dir, String fileName, boolean NeedsAutorization, boolean IsApp) {
		for (Entry<String,ExtraColor> entry: this.Theme.ExtraColors.entrySet()) {
			ABMaterial.ExtraColors.put(entry.getKey(), entry.getValue());
		}
		if (this.CompleteThemes.size()==0) {
			WritePageToDiskPerTheme(dir, fileName, NeedsAutorization, IsApp);
		} else {		
			for (Entry<String,ABMTheme> entry: this.CompleteThemes.entrySet()) {
				this.CompleteTheme = entry.getValue();
				Theme=this.CompleteTheme.Page.Clone();
				this.CompleteTheme.MainColor = CompleteTheme.Page.MainColor; 				

				if (navigationBar!=null) {
					navigationBar.ResetTheme();
				}
				
				for (Entry<String, ABMRow> row : Rows.entrySet()) {
					row.getValue().ResetTheme();
				}
				
				for (Entry<String, ABMModalSheet> sheet : ModalSheets.entrySet()) {
					sheet.getValue().ResetTheme();												
		        }
				for (Entry<String, ABMContainer> cont : FloatingContainers.entrySet()) {
					cont.getValue().ResetTheme();												
		        }
				for (Entry<String, ABMActionButton> button : ActionButtons.entrySet()) {
					button.getValue().ResetTheme();									
		        }
				
				for (Entry<String,ABMCanvas> ec: Canvases.entrySet()) {
					ec.getValue().ResetTheme();
				}
				if (Footer!=null) {
					Footer.ResetTheme();
				}
				WritePageToDiskPerTheme(dir, fileName, NeedsAutorization, IsApp);
				
			}
		}
	}
	
	/**
	 * Inserts a css rule.  Using !important, in some cases you may overrule the default Materialize CSS with this method
	 */
	public void InsertCSSRule(String selector, String css, boolean flush) {
		if (ws!=null) {
			StringBuilder s = new StringBuilder();
			s.append("var sheet = $('#globals').get(0).sheet;");	
			s.append("var rules =  (typeof sheet.cssRules ? sheet.cssRules : sheet.rules);");
			s.append("var crn = rules.length;");
			s.append("var haveRule=false;");
			s.append("for (var j = 0; j < crn; j++) {");
			s.append("if (rules[j].selectorText == '" + selector + "') {");
			s.append("haveRule = true;");
			s.append("break;");
			s.append("}");
			s.append("}");
			s.append("if (!haveRule) {");
			s.append("sheet.insertRule(\"" + selector + "{" + css + "}\", crn);");
			s.append("}");
			ws.Eval(s.toString(), null);
			if (flush) {
				try {
					if (ws.getOpen()) {
						if (ShowDebugFlush) {BA.Log("InsertCSSRule");}
						ws.Flush();RunFlushed();				
					}
				} catch (IOException e) {			
					
				}
			}
			
		}
	}
	
	/**
	 * Replace a previously inserted CSS rule (by InsertCSSRule only)  
	 */
	public void ReplaceCSSRule(String selector, String css, boolean flush) {
		if (ws!=null) {
			StringBuilder s = new StringBuilder();
			s.append("var sheet = $('#globals').get(0).sheet;");	
			s.append("var rules =  (typeof sheet.cssRules ? sheet.cssRules : sheet.rules);");
			s.append("var crn = rules.length;");
			s.append("for (var j = 0; j < crn; j++) {");
			s.append("if (rules[j].selectorText == '" + selector + "') {");
			s.append("sheet.deleteRule(j);");
			s.append("crn--;");
			s.append("break;");
			s.append("}");
			s.append("}");
			s.append("sheet.insertRule(\"" + selector + "{" + css + "}\", crn);");
			
			ws.Eval(s.toString(), null);
			if (flush) {
				try {
					if (ws.getOpen()) {
						if (ShowDebugFlush) {BA.Log("ReplaceCSSRule");}
						ws.Flush();RunFlushed();				
					}
				} catch (IOException e) {			
					
				}
			}
		}
	}
	
	/*
	 * Only works with CSS rules set by InsertCSSRule or ReplaceCSSRule
	 */
	public void RemoveCSSRule(String selector, boolean flush) {
		if (ws!=null) {
			StringBuilder s = new StringBuilder();
			s.append("var sheet = $('#globals').get(0).sheet;");	
			s.append("var rules =  (typeof sheet.cssRules ? sheet.cssRules : sheet.rules);");
			s.append("var crn = rules.length;");
			s.append("for (var j = 0; j < crn; j++) {");
			s.append("if (rules[j].selectorText == '" + selector + "') {");
			s.append("sheet.deleteRule(j);");
			s.append("break;");
			s.append("}");
			s.append("}");	
			ws.Eval(s.toString(), null);
			if (flush) {
				try {
					if (ws.getOpen()) {
						if (ShowDebugFlush) {BA.Log("RemoveCSSRule");}
						ws.Flush();RunFlushed();				
					}
				} catch (IOException e) {			
					
				}
			}
		}
		
	}
		
	protected String LoadScript2(BufferedWriter writer, String script, String fileName, boolean Needs, boolean NeedsAll, boolean UseWebWorker) {
		 String ret = "";
		 
		 if (Needs) {
			 try {
					writer.write("<script " + loadAsync + "type=\"text/javascript\" src=\"" + script + "\"></script>\n");
				} catch (IOException e) {				
					e.printStackTrace();
				}
		 } else {
			 if (NeedsAll) {
				 if (UseWebWorker) {
					 String size="";
					 File f = new File(fileName);
					 //BA.Log(fileName + ' ' + f.length());
					 size = new DecimalFormat("#,##0.#").format(f.length());
					 ret = ",{\"type\":\"SCRIPT\", \"source\":\"" + script + "\", \"size\":" + size + "}";
				 } else {
					 try {
						writer.write("<script " + loadAsync + "type=\"text/javascript\" src=\"" + script + "\"></script>\n");
					 } catch (IOException e) {				
						e.printStackTrace();
					 }
				 }
			 }
		 }
		 
		 return ret;
	}
		
	public boolean WebsocketReconnected() {
		boolean ret=false;
		if (ABMaterial.IsSinglePageApp) {
			if (!this.IsAppPage) {
				return ret;
			}
		}
		if (ws!=null) {
			anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
			Params.Initialize();
			SimpleFuture fut = this.ws.RunFunctionWithResult("GetIsReconnect", Params);
			
			try {	
				ret = (boolean) fut.getValue();								
			} catch (InterruptedException e) {				
			} catch (TimeoutException e) {						
			} catch (ExecutionException e) {						
			} catch (IOException e) {						
			}
		}
		return ret;
	}
		
	protected String LoadScript(BufferedWriter writer, String script, String fileName, boolean Needs, boolean NeedsAll, boolean UseWebWorker) {
		 String ret = "";			 
		 
		 if (Needs) {
        	try {
				writer.write("<script " + loadAsync + "type=\"text/javascript\" src=\"" + script + "\"></script>\n");
			} catch (IOException e) {				
				e.printStackTrace();
			}
        } else {
       	 if (NeedsAll) {
       		 if (UseWebWorker) {       			
       			ret=",'" + script + "'";       			 
       		 } else {
       			 try {
						writer.write("<script " + loadAsync + "type=\"text/javascript\" src=\"" + script + "\"></script>\n");
					} catch (IOException e) {						
						e.printStackTrace();
					}
       		 }
       	 }
        }
        
		return ret;
	}
	
	protected String LoadScriptWithID(BufferedWriter writer, String script, String fileName, boolean Needs, boolean NeedsAll, boolean UseWebWorker, String id) {
		 String ret = "";			 
		 
		 if (Needs) {
       	try {
				writer.write("<script " + loadAsync + " " + "id=\"" + id + "\"" + " type=\"text/javascript\" src=\"" + script + "\"></script>\n");
			} catch (IOException e) {				
				e.printStackTrace();
			}
       } else {
      	 if (NeedsAll) {
      		 if (UseWebWorker) {       			
      			ret=",'" + script + "'";       			 
      		 } else {
      			 try {
						writer.write("<script " + loadAsync + " " + "id=\"" + id + "\"" + " type=\"text/javascript\" src=\"" + script + "\"></script>\n");
					} catch (IOException e) {						
						e.printStackTrace();
					}
      		 }
      	 }
       }       
		return ret;
	}
		
	protected void purgeDirectoryButKeepSubDirectories(File dir) {
		for (File file: dir.listFiles()) {
	        if (file.getName().contains("." + ABMaterial.getAppVersion() + ".")) {
	        	return;
	        }
	    }
	    for (File file: dir.listFiles()) {
	        if (!file.isDirectory()) file.delete();
	    }
	}
	
	public void SetHeartBeat(int seconds) {
		HeartBeatSecs = seconds;
	}
	
	protected void WritePageToDiskPerTheme(String pageDir, String fileName, boolean NeedsAutorization, boolean IsApp) {
		
		MainColor = CompleteTheme.MainColor;
		String relPath="";
		File staticFilesFolder = new File(pageDir);
    	
    	boolean PageCSSInlineBool=false;
    	switch (PageCSSInline) {
    	case ABMaterial.PAGE_INLINE_USEAPPSETTING:
    		PageCSSInlineBool=ABMaterial.AppDefaultPageCSSInline;
    		break;
    	case ABMaterial.PAGE_INLINE_FALSE:
    		PageCSSInlineBool=false;
    		break;
    	case ABMaterial.PAGE_INLINE_TRUE:
    		PageCSSInlineBool=true;
    		break;
    	}
    	
    	boolean PageJSInlineBool=false;
    	switch (PageJSInline) {
    	case ABMaterial.PAGE_INLINE_USEAPPSETTING:
    		PageJSInlineBool=ABMaterial.AppDefaultPageJSInline;
    		break;
    	case ABMaterial.PAGE_INLINE_FALSE:
    		PageJSInlineBool=false;
    		break;
    	case ABMaterial.PAGE_INLINE_TRUE:
    		PageJSInlineBool=true;
    		break;
    	}
    	
    	if (this.CompleteThemes.size()>0) {
    		PageCSSInlineBool=false;
    	}
    	
    	if (ABMaterial.IsSinglePageApp) {
			ABMaterial.PreloadAllJavascriptAndCSSFiles=true;
			PageCSSInlineBool=false;
			PageJSInlineBool=false;
		}
    	
    	if (ABMaterial.LoadJavascriptsAfterDOM) {
    		PageJSInlineBool=false;
    	}
    	
    	if (ABMaterial.LoadCSSAfterDOM) {
    		PageCSSInlineBool=false;
    	}
    	
    	String jsDir="";
    	if (mStaticFilesFolder.equals("")) {
			while (!staticFilesFolder.getPath().endsWith("www")) {
				relPath+="../";
				staticFilesFolder = new File(staticFilesFolder.getParent());
			}
			
			jsDir=staticFilesFolder + File.separator + "js";
		} else {
			staticFilesFolder = new File(mStaticFilesFolder);	
			if (IsApp) {
				pageDir = staticFilesFolder.getAbsolutePath() + File.separator + mAppName;
				relPath+="../";
				jsDir=staticFilesFolder.getAbsolutePath() + File.separator + "js";
			} else {
				pageDir = staticFilesFolder.getAbsolutePath() + File.separator + mAppName + File.separator + this.Name;
				relPath+="../../";				
			}
			
			
		}
    	boolean UseCDN = (ABMaterial.CDN);
    	if (ABMaterial.CDN) {
    		URelPath=ABMaterial.mCDNUrl;
    	} else {
    		URelPath=relPath;
    	}
    	String cssPath=staticFilesFolder.getAbsolutePath() + File.separator + "css" + File.separator;
    	String jsPath=staticFilesFolder.getAbsolutePath() + File.separator;
		
    	CurrentDir=pageDir;
    	
    	String DebugMin=".min";
    	if (ABMaterial.UseDebugScriptsAndCSS) {
    		DebugMin = "";    		
    	}
    	
    	boolean JoinAllJS=false;
    	
    	String relPathApp=relPath.substring(0, relPath.length()-3);
    	
    	String ReloadString="";
    	String ReloadStringInner="";
    	if (!ABMaterial.getAppVersion().equals("")) {
    		ReloadString = "?" + ABMaterial.getAppVersion(); 
    		ReloadStringInner = ABMaterial.getAppVersion();
    	}
    	
    	if (!IsApp) {
    		StringBuilder siteMap = new StringBuilder();
    		if (!ABMaterial.AppPublishedStartURL.equals("")) {
    			if (!ABMaterial.AppPublishedStartURL.endsWith("/")) {
    				ABMaterial.AppPublishedStartURL+="/";
    			}
    			siteMap.append("<url>\n");
    			siteMap.append("<loc>" + ABMaterial.AppPublishedStartURL + Name + "/" + PageHTMLName + "</loc>\n");
    			siteMap.append("<lastmod>" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+'00:00").format(new Date()) + "</lastmod>\n");
    			if (!PageSiteMapFrequency.equals(ABMaterial.SITEMAP_FREQ_NONE)) {
    				siteMap.append("<changefreq>" + PageSiteMapFrequency + "</changefreq>\n");
    			}
    			double tmpPageSiteMapPriority=0.0;
			
    			if (!PageSiteMapPriority.equals("")) {
    				siteMap.append("<priority>" + PageSiteMapPriority + "</priority>\n");		
    				tmpPageSiteMapPriority = Double.parseDouble(PageSiteMapPriority);
    			}
    			siteMap.append("</url>\n");
    			ABMaterial.SiteMaps.add(new Entity(siteMap.toString(), tmpPageSiteMapPriority));
    		}
    	} else {
    		File buildFileJS=null;
    		switch (ABMaterial.CacheSystem) {
    		case "2.0":
    		case "3.0":
    			buildFileJS = new File(jsDir, "sessioncreator.js");
    			if (buildFileJS.exists()) {
    				buildFileJS.delete();
    			}
    			break;
    		default:
    			BufferedWriter writerJS = null;
    			buildFileJS = new File(jsDir, "sessioncreator.js");
    			if (!buildFileJS.exists()) {
    				BA.Log("Creating: " + buildFileJS.getAbsolutePath());
    				try {
    					writerJS = new BufferedWriter(new FileWriter(buildFileJS));	 
    					writerJS.write("");        	
    				} catch (Exception e) {
    					e.printStackTrace();
    				} finally {
    					try {
    						writerJS.close();
    					} catch (Exception e) {
    					}
    				}
    			}
    			break;
    		}
    	}
    	
		// 2017/09/21 no more core.min.css if CDN
    	List<String> CSSFiles = new ArrayList<String>();
    	List<String> JSFiles = new ArrayList<String>();
    	
    	List<String> CSSAfterDOM = new ArrayList<String>();
    	List<String> CSSAfterDOMScript = new ArrayList<String>();    	
    	
		BufferedWriter writer = null;
        try {
          Files.createDirectories(Paths.get(pageDir));   
          purgeDirectoryButKeepSubDirectories(new File(pageDir));
        
          if (!ABMaterial.IsSinglePageApp || IsApp) {
        	File buildFile = new File(pageDir, fileName);  
        	        	
            writer = new BufferedWriter(new FileWriter(buildFile));
            writer.write("<!DOCTYPE html>\n");
            String notrans="";
            if (PageTranslatable==false ) {
            	notrans = " translate=\"no\" ";
            }
            if (!PageLanguage.equals("")) {
            	writer.write("<html lang=\"" + PageLanguage + "\"" + notrans + ">\n<head>\n");
            } else {
            	writer.write("<html" + notrans + ">\n<head>\n");
            }
            if ((ABMaterial.CW)) {
            	String z = ABMaterial.encrypt("This WebApp/WebSite was generated using ABMaterial v" + ABMaterial.Version + ", a B4X library written by Alain Bailleul 2015-2018. See http://alwaysbusycorner.com/abmaterial");
            	writer.write("<!-- " + z + " -->\n");
            } else {
            	writer.write("<!-- This WebApp/WebSite was generated using ABMaterial v" + ABMaterial.Version + ", a B4X library written by Alain Bailleul 2015-2018. See http://alwaysbusycorner.com/abmaterial -->\n");
            }
      
            writer.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + PageCharset + "\">\n");            	
            
            if (!PageKeywords.equals("")) {
            	 writer.write("<meta name=\"keywords\" content=\"" + PageKeywords + "\">\n");
            }
            
            if (ABMaterial.AllowZoom) {
            	writer.write("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.01\">\n");
            } else {
            	writer.write("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no, minimal-ui\">\n");
            }
            
            Date expdate= new Date();
            if (PageExpires==0) {  
            	if (ABMaterial.SiteExpires==0) {
            		LocalDate futureDate = LocalDate.now().plusWeeks(1);
            		expdate = Date.from(futureDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            	} else {
            		expdate.setTime(ABMaterial.SiteExpires);
            	}
            }else {
            	expdate.setTime(PageExpires);            	
            }
            
            DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
            df.setTimeZone(TimeZone.getTimeZone("GMT"));
            
            
            writer.write("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n");
            writer.write("<meta name=\"msapplication-tap-highlight\" content=\"no\">\n");
            writer.write("<meta name=\"description\" content=\"" + ABMaterial.HTMLConv().htmlEscape(PageDescription, PageCharset) + "\">\n");
            writer.write("<meta name=\"apple-mobile-web-app-capable\" content=\"yes\">\n");
            writer.write("<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"" + ABMaterial.GetColorStrMap(ABMaterial.AppleTitleBarThemeColor,ABMaterial.AppleTitleBarThemeColorIntensity) + "\"/>");
            writer.write("<meta name=\"apple-mobile-web-app-title\" content=\"" + ABMaterial.HTMLConv().htmlEscape(PageTitle, PageCharset) + "\" />\n");
                        
            writer.write("<title>" + ABMaterial.HTMLConv().htmlEscape(PageTitle, PageCharset) + "</title>\n");
            
            for (int index=0;index<AppleTouchIcons.size();index++) {
            	writer.write("<link rel=\"apple-touch-icon\" href=\"" + relPathApp + "images/" + AppleTouchIcons.get(index) + "\" sizes=\"" + AppleTouchIconSizes.get(index) + "\">\n");
            }
            
            for (int index=0;index<FavorityIcons.size();index++) {
            	if (FavorityIcons.get(index).startsWith("http")) {
            		writer.write("<link rel=\"icon\" href=\"" + FavorityIcons.get(index) + "\" sizes=\"" + FavorityIconSizes.get(index) + "\">\n");
            		writer.write("<link rel=\"shortcut icon\" href=\"" + FavorityIcons.get(index) + "\" sizes=\"" + FavorityIconSizes.get(index) + "\">\n");
            	} else {
            		writer.write("<link rel=\"icon\" href=\"" + relPathApp + "images/" + FavorityIcons.get(index) + "\" sizes=\"" + FavorityIconSizes.get(index) + "\">\n");
            		writer.write("<link rel=\"shortcut icon\" href=\"" + relPathApp + "images/" + FavorityIcons.get(index) + "\" sizes=\"" + FavorityIconSizes.get(index) + "\">\n");
            	}
            }
            
            if (!ABMaterial.Manifest.equals("")) {
            	writer.write("<link rel=\"manifest\" href=\"" + relPathApp + "images/" + ABMaterial.Manifest + "\">\n");
            }
            
            if (!ABMaterial.MaskIcon.equals("")) {
            	String IconColor=ABMaterial.GetColorStrMap(ABMaterial.MaskIconColor, ABMaterial.MaskIconColorIntensity);
            	writer.write("<link rel=\"mask-icon\" href=\"" + relPathApp + "images/" + ABMaterial.MaskIcon + "\" color=\"" + IconColor + "\">\n");
            }
            if (ABMaterial.UseMaterialFontsFromWebLink) {
            	writer.write("<link href=\"https://fonts.googleapis.com/icon?family=Material+Icons\" rel=\"stylesheet\">");
            }
            
            String TileColor = ABMaterial.GetColorStrMap(ABMaterial.MSTileColor, ABMaterial.MSTileColorIntensity);
            writer.write("<meta name=\"msapplication-TileColor\" content=\"#" + TileColor + "\">\n");
            for (int index=0;index<MSTileIcons.size();index++) {
            	writer.write("<meta name=\"msapplication-TileImage\" content=\"" + relPathApp + "images/" + MSTileIcons.get(index) + "\" sizes=\"" + MSTileIconSizes.get(index)+ "\">\n");
            }
            
           
            writer.write("<meta name=\"theme-color\" content=\"" + ABMaterial.GetColorStrMap(ABMaterial.AndroidChromeThemeColor,ABMaterial.AndroidChromeThemeColorIntensity) + "\">\n");
            
            if (!ABMaterial.BrowserConfig.equals("")) {
            	writer.write("<meta name=\"msapplication-config\" content=\"" + ABMaterial.BrowserConfig + "\" />\n");
            }
            
            for (int i=0;i<OpenGraphMetaTags.getSize();i++) {
            	String key=(String) OpenGraphMetaTags.GetKeyAt(i);
            	String value=(String) OpenGraphMetaTags.GetValueAt(i);
            	writer.write("<meta property=\"" + key + "\" content=\"" + ABMaterial.HTMLConv().htmlEscape(value, PageCharset) + "\"/>\n");
            }
            
            if (!ABMaterial.LoadCSSAfterDOM) {
            	writer.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/materialize" + ABMaterial.CSSMaterialize + DebugMin + ".css\"  media=\"screen,projection,print\"/>\n");
            } else {
            	CSSAfterDOMScript.add(URelPath + "css/materialize" + ABMaterial.CSSMaterialize + DebugMin + ".css");
            }
            	
            	
    		if (NeedsSimpleBar || NeedsSimpleBarAll) {
    			CSSFiles.add(cssPath + "simplebar" + DebugMin + ".css"); // 0
    			if (NeedsSimpleBar || (NeedsSimpleBarAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) { 
    				if (!ABMaterial.LoadCSSAfterDOM) {
    					if (UseCDN) {writer.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/simplebar" + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				} else {
    					if (UseCDN) {CSSAfterDOM.add("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/simplebar" + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}    					
    				}
    			}
    		}
    		
    		if (NeedsCalendar || NeedsCalendarAll) {
    			CSSFiles.add(cssPath + "fullcalendar" + DebugMin + ".css"); // 1
    			if (NeedsCalendar || (NeedsCalendarAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) { 
    				if (!ABMaterial.LoadCSSAfterDOM) {
    					if (UseCDN) {writer.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/fullcalendar" + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				} else {
   						if (UseCDN) {CSSAfterDOM.add("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/fullcalendar" + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				}    				
    					
    			}	

    		}
    		if (NeedsDateTimeScroller || NeedsDateTimeScrollerAll) {
    			CSSFiles.add(cssPath + "mobiscroll" + DebugMin + ".css"); // 2
    			if (NeedsDateTimeScroller || (NeedsDateTimeScrollerAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
    				if (!ABMaterial.LoadCSSAfterDOM) {
    					if (UseCDN) {writer.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/mobiscroll" + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				} else {
   						if (UseCDN) {CSSAfterDOM.add("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/mobiscroll" + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				}
    				
    			}
    		}
    		if (NeedsDateTimePicker || NeedsDateTimePickerAll) {
    			CSSFiles.add(cssPath + "bootstrap-material-datetimepicker" + DebugMin + ".css"); // 3
    			if (NeedsDateTimePicker || (NeedsDateTimePickerAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
    				if (!ABMaterial.LoadCSSAfterDOM) {
    					if (UseCDN) {writer.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/bootstrap-material-datetimepicker" + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				} else {
    					if (UseCDN) {CSSAfterDOM.add("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/bootstrap-material-datetimepicker" + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				}    				
    			}
    		}
    		if (NeedsEditor || NeedsEditorAll) {
    			CSSFiles.add(cssPath + "Squire-UI" + ABMaterial.CSSSquire + DebugMin + ".css"); // 4
    			if (NeedsEditor || (NeedsEditorAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
    				if (!ABMaterial.LoadCSSAfterDOM) {
    					if (UseCDN) {writer.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/Squire-UI" + ABMaterial.CSSSquire + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				} else {
    					if (UseCDN) {CSSAfterDOM.add("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/Squire-UI" + ABMaterial.CSSSquire + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				}
    			}
    		}
    		if (NeedsOAuth || NeedsOAuthAll) {
    			CSSFiles.add(cssPath + "zocial" + DebugMin + ".css"); //5
    			if (NeedsOAuth || (NeedsOAuthAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
    				if (!ABMaterial.LoadCSSAfterDOM) {
    					if (UseCDN) {writer.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/zocial" + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				} else {
    					if (UseCDN) {CSSAfterDOM.add("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/zocial" + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				}
    				
    			}
    		}
    		if (NeedsSocialShare || NeedsSocialShareAll) {
    			CSSFiles.add(cssPath + "jssocials.css"); // 6
    			CSSFiles.add(cssPath + "jssocials-theme-" + SocialShareTheme + ".css");
    			if (NeedsSocialShare || (NeedsSocialShareAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
    				if (!ABMaterial.LoadCSSAfterDOM) {
    					if (UseCDN) {writer.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/jssocials.css" + "\"  media=\"screen,projection,print\"/>\n");}
        				if (UseCDN) {writer.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/jssocials-theme-" + SocialShareTheme + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				} else {
    					if (UseCDN) {CSSAfterDOM.add("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/jssocials.css" + "\"  media=\"screen,projection,print\"/>\n");}
    					if (UseCDN) {CSSAfterDOM.add("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/jssocials-theme-" + SocialShareTheme + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				}
    				
    			}
    		}   
    		if (NeedsChart || NeedsChartAll) {
    			CSSFiles.add(cssPath + "chartist" + DebugMin + ".css"); // 7
    			if (NeedsChart || (NeedsChartAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
    				if (!ABMaterial.LoadCSSAfterDOM) {
    					if (UseCDN) {writer.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/chartist" + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				} else {
    					if (UseCDN) {CSSAfterDOM.add("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/chartist" + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				}
    				
    			}
    		}
    		if (NeedsTreeTable || NeedsTreeTableAll) {
    			CSSFiles.add(cssPath + "jquery.treetable" + DebugMin + ".css"); // 8
    			if (NeedsTreeTable || (NeedsTreeTableAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
    				if (!ABMaterial.LoadCSSAfterDOM) {
    					if (UseCDN) {writer.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/jquery.treetable" + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				} else {
    					if (UseCDN) {CSSAfterDOM.add("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/jquery.treetable" + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				}
    				
    			}
        	}
        	if (NeedsSlider || NeedsRange || NeedsSliderAll || NeedsRangeAll) {
        		CSSFiles.add(cssPath + "nouislider" + DebugMin + ".css");  // 9
        		if (NeedsSlider || NeedsRange || (NeedsSliderAll && ABMaterial.PreloadAllJavascriptAndCSSFiles) || (NeedsRangeAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
        			if (!ABMaterial.LoadCSSAfterDOM) {
        				if (UseCDN) {writer.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/nouislider" + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				} else {
    					if (UseCDN) {CSSAfterDOM.add("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/nouislider" + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				}
        			
        		}
        	}
        	if (NeedsSmartWizard || NeedsSmartWizardAll) {
        		CSSFiles.add(cssPath + "smart_wizard" + DebugMin + ".css"); // 10
        		if (NeedsSmartWizard || (NeedsSmartWizardAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
        			if (!ABMaterial.LoadCSSAfterDOM) {
        				if (UseCDN) {writer.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/smart_wizard" + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				} else {
    					if (UseCDN) {CSSAfterDOM.add("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/smart_wizard" + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				}
        			
        		}
        	}
        	if (NeedsComposer || NeedsComposerAll) {
        		CSSFiles.add(cssPath + "abmcomposer" + DebugMin + ".css"); // 10
        		if (NeedsComposer || (NeedsComposerAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
        			if (!ABMaterial.LoadCSSAfterDOM) {
        				if (UseCDN) {writer.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/abmcomposer" + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				} else {
    					//if (!(ABMaterial.CloudCDN && ABMaterial.CloudUploadGeneratedJSCSS)) {
    						if (UseCDN) {CSSAfterDOM.add("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/abmcomposer" + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    					//}
    				}
        			
        		}
        	}
        	if (NeedsFileManager || NeedsFileManagerAll) {
        		CSSFiles.add(cssPath + "abmfilemanager" + ABMaterial.CSSFileManager + DebugMin + ".css"); // 10
        		if (NeedsFileManager || (NeedsFileManagerAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
        			if (!ABMaterial.LoadCSSAfterDOM) {
        				if (UseCDN) {writer.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/abmfilemanager" + ABMaterial.CSSFileManager + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				} else {
    					if (UseCDN) {CSSAfterDOM.add("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/abmfilemanager" + ABMaterial.CSSFileManager + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				}
        			
        		}
        	}
               
        	if (NeedsAudioPlayer || NeedsAudioPlayerAll) {
        		CSSFiles.add(cssPath + "APlayer" + DebugMin + ".css"); // 11
        		if (NeedsAudioPlayer || (NeedsAudioPlayerAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
        			if (!ABMaterial.LoadCSSAfterDOM) {
        				if (UseCDN) {writer.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/APlayer" + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				} else {
    					if (UseCDN) {CSSAfterDOM.add("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/APlayer" + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				}        			
        		}
        	}
        
        	if (NeedsTimeline || (NeedsTimelineAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
        		if (!ABMaterial.LoadCSSAfterDOM) {
        			if (UseCDN) {writer.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/timeline" + ABMaterial.CSSTimeline + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
				} else {
					if (UseCDN) {CSSAfterDOM.add("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/timeline" + ABMaterial.CSSTimeline + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
				}        		                		
        	}
        
        	if (NeedsChronologyList || NeedsChronologyListAll) {
        		CSSFiles.add(cssPath + "chronology" + ABMaterial.CSSChronology + DebugMin + ".css"); // 12
        		if (NeedsChronologyList || (NeedsChronologyListAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
        			if (!ABMaterial.LoadCSSAfterDOM) {
        				if (UseCDN) {writer.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/chronology" + ABMaterial.CSSChronology + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				} else {
    					if (UseCDN) {CSSAfterDOM.add("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + URelPath + "css/chronology" + ABMaterial.CSSChronology + DebugMin + ".css" + "\"  media=\"screen,projection,print\"/>\n");}
    				}        			
        		}
        	}
            	
            // 2017/09/25 core only contains icons if UseCDN!               
        	if (CSSFiles.size()>0 || !ABMaterial.BuildedIconsCSS.equals("")) {
        		String CloudCSSPath="";
        		File buildFileTmp = new File(pageDir, "core.min." + ReloadStringInner + ".css");
            	CloudCSSPath=buildFileTmp.getAbsolutePath();
            	BufferedWriter tmpCSS = null;
            	try {
            		tmpCSS = new BufferedWriter(new FileWriter(buildFileTmp, true));
            		if (ABMaterial.UseMaterialFontsFromWebLink==false) {
            			tmpCSS.write("@font-face{font-family:'Material Icons';font-style:normal;font-weight:400;src:url(../font/iconfont/MaterialIcons-Regular.eot);src:local('Material Icons'),local(MaterialIcons-Regular),url(../font/iconfont/MaterialIcons-Regular.woff2) format(\"woff2\"),url(../font/iconfont/MaterialIcons-Regular.woff) format(\"woff\"),url(../font/iconfont/MaterialIcons-Regular.ttf) format(\"truetype\")}.material-icons{font-family:'Material Icons';font-weight:400;font-style:normal;font-size:24px;display:inline-block;line-height:1;text-transform:none;letter-spacing:normal;word-wrap:normal;white-space:nowrap;direction:ltr;text-rendering:optimizeLegibility;font-feature-settings:liga}");
                	}
            		tmpCSS.write(ABMaterial.BuildedIconsCSS);            		
            		
            	} catch (Exception e) {
        			e.printStackTrace();
            	} finally {
            		try {
            			tmpCSS.close();
            		} catch (Exception e) {
        			}
            	}
            	if (ABMaterial.CloudCDN && ABMaterial.CloudUploadGeneratedJSCSS) {
            		
        			String CSSUrl = "";
        			if (ABMaterial.CloudCoreCSSUrl.equals("")) {
        				BA.Log("Uploading to Cloudinary.  Depending on the size of your app this can take several minutes...");
        				CSSUrl = ABMaterial.CloudinaryUploadRawFileInternal(ABMaterial.CloudAppName + "/css/core.min." + ReloadStringInner + ".css", CloudCSSPath);
        				ABMaterial.CloudCoreCSSUrl = CSSUrl;        				
        			} else {
        				CSSUrl = ABMaterial.CloudCoreCSSUrl;
        			}
        			if (!ABMaterial.LoadCSSAfterDOM) {
        				writer.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + CSSUrl + "\" media=\"screen,projection,print\"/>\n");
        			} else {
        				CSSAfterDOMScript.add(CSSUrl); 
        			}
            	} else {
            		if (!ABMaterial.LoadCSSAfterDOM) {
            			writer.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + relPathApp + "core" + DebugMin + "." + ReloadStringInner + ".css\" media=\"screen,projection,print\"/>\n");
            		} else {
            			CSSAfterDOMScript.add(relPathApp + "core" + DebugMin + "." + ReloadStringInner + ".css");
            		}
            	}
            	buildFileTmp.delete();
        	}
        	
        	
        	if (NeedsUpload || (NeedsUploadAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
        		if (!ABMaterial.LoadCSSAfterDOM) {
        			writer.write("<link href=\"https://fonts.googleapis.com/css?family=PT+Sans+Narrow:400,700\" rel='stylesheet' />\n");
        		} else {
        			CSSAfterDOM.add("<link href=\"https://fonts.googleapis.com/css?family=PT+Sans+Narrow:400,700\" rel='stylesheet' />\n");        			
        		}
        	} 
        	
        	for (String s: ExtraCSS) {
        		if (s.startsWith("http")) {
        			if (!ABMaterial.LoadCSSAfterDOM) {
        				writer.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + s + "\"  media=\"screen,projection,print\"/>\n");
    				} else {
   						CSSAfterDOM.add("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + s + "\"  media=\"screen,projection,print\"/>\n");
    				}
        			
        		} else {
        			String CloudCSSPath="";
        			buildFile = new File(cssPath, s);
        			CloudCSSPath = buildFile.getAbsolutePath();
        			if (ABMaterial.GZip) {
                		buildFile = new File(cssPath, s + ".gz");
                    	buildFile.delete();
                    	
                    	buildFile = new File(cssPath, s);
                    	CloudCSSPath = buildFile.getAbsolutePath();
                    	if (buildFile.length()>ABMaterial.minGZipSize) {
                    		GzipIt(buildFile.getAbsolutePath());
                    	}
                	}
        			if (ABMaterial.CloudCDN && ABMaterial.CloudUploadGeneratedJSCSS)       {
            			String CSSUrl = ABMaterial.CloudinaryUploadRawFileInternal(ABMaterial.CloudAppName + "/css/" + s, CloudCSSPath);  
            			if (!ABMaterial.LoadCSSAfterDOM) {
            				writer.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + CSSUrl + "\"  media=\"screen,projection,print\"/>\n");
        				} else {
        					CSSAfterDOM.add("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + CSSUrl + "\"  media=\"screen,projection,print\"/>\n");  
        				}
        	      		
                	} else {
                		if (!ABMaterial.LoadCSSAfterDOM) {
                			writer.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + relPath + "css/" + s + "\"  media=\"screen,projection,print\"/>\n");
        				} else {
        					CSSAfterDOM.add("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + relPath + "css/" + s + "\"  media=\"screen,projection,print\"/>\n");        					
        				}
                		
                	}
        		}
        	}
        	
        	
        	int SML600=ABMaterial.ThresholdPxConsiderdSmall;
        	int SML992=ABMaterial.ThresholdPxConsiderdMedium;
        	int SML1200=ABMaterial.ThresholdPxConsiderdLarge;
        	
        	if (!PageCSSInlineBool) {
        		writer.write("<style>.isloading{display:hidden;visibility:hidden}\n");
        		
        		if (LoadCode128Font) {
        			writer.write("@font-face { font-family: 'code128'; src: url('" + URelPath + "font/code128_M.woff'); }.code_128_XXL { font-family: code128; font-size:48px }");
        		}
        		if (ABMaterial.LoadCSSAfterDOM) {
        			writer.write("@charset \"UTF-8\";.row:after{clear:both}.transparent{background-color:transparent!important}body{margin:0}main{display:block}html{-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%;box-sizing:border-box}*,:after,:before{box-sizing:inherit}.row .col{-moz-box-sizing:border-box}.row .col{-webkit-box-sizing:border-box}html{font-weight:400}@media only screen{.hide{display:none!important}}.row .col{float:left}.row:after{content:\"\"}.container{margin:0 auto;max-width:1280px;width:90%}.container .row{margin-left:-.75rem;margin-right:-.75rem}.row{margin-left:auto;margin-right:auto;margin-bottom:20px}.row:after{display:table}.row .col{box-sizing:border-box;padding:0 .75rem}.row .col.s12{width:100%;margin-left:0}@font-face{font-family:Roboto;src:local(Roboto Thin),url(" + URelPath + "font/roboto/Roboto-Thin.woff2) format(\"woff2\"),url(" + URelPath + "font/roboto/Roboto-Thin.woff) format(\"woff\"),url(" + URelPath + "font/roboto/Roboto-Thin.ttf) format(\"truetype\");font-weight:200}@font-face{font-family:Roboto;src:local(Roboto Light),url(" + URelPath + "font/roboto/Roboto-Light.woff2) format(\"woff2\"),url(" + URelPath + "font/roboto/Roboto-Light.woff) format(\"woff\"),url(" + URelPath + "font/roboto/Roboto-Light.ttf) format(\"truetype\");font-weight:300}@font-face{font-family:Roboto;src:local(Roboto Regular),url(" + URelPath + "font/roboto/Roboto-Regular.woff2) format(\"woff2\"),url(" + URelPath + "font/roboto/Roboto-Regular.woff) format(\"woff\"),url(" + URelPath + "font/roboto/Roboto-Regular.ttf) format(\"truetype\");font-weight:400}@font-face{font-family:Roboto;src:url(" + URelPath + "font/roboto/Roboto-Medium.woff2) format(\"woff2\"),url(" + URelPath + "font/roboto/Roboto-Medium.woff) format(\"woff\"),url(" + URelPath + "font/roboto/Roboto-Medium.ttf) format(\"truetype\");font-weight:500}@font-face{font-family:Roboto;src:url(" + URelPath + "font/roboto/Roboto-Bold.woff2) format(\"woff2\"),url(" + URelPath + "font/roboto/Roboto-Bold.woff) format(\"woff\"),url(" + URelPath + "font/roboto/Roboto-Bold.ttf) format(\"truetype\");font-weight:700}html{line-height:1.5;font-family:Roboto,sans-serif;color:rgba(0,0,0,.87)}::-webkit-input-placeholder{color:#d1d1d1}:-moz-placeholder{color:#d1d1d1}::-moz-placeholder{color:#d1d1d1}:-ms-input-placeholder{color:#d1d1d1}");
        			
        		}
        		
        		if (!FontStack.equals("")) {
            		writer.write("html{font-family: " + FontStack + ";}\n");
            	}
        		if (ABMaterial.FontPercentSmall!=100) {
        			writer.write("@media screen and (max-width: " + SML600 + "px) {html {font-size: " + ABMaterial.FontPercentSmall + "% !important}}");
        		}
        		if (ABMaterial.FontPercentMedium!=100) {
        			writer.write("@media screen and (max-width: " + SML992 + "px) and (min-width: " + (SML600+1) + "px) {html {font-size: " + ABMaterial.FontPercentMedium + "% !important}}");
        		}
        		
        		if (ABMaterial.FontPercentLarge!=100) {
        			writer.write("@media screen and (max-width: " + SML1200 + "px) and (min-width: " + (SML992+1) + "px) {html {font-size: " + ABMaterial.FontPercentMedium + "% !important}}");
        		}
        		if (ABMaterial.FontPercentExtraLarge!=100) {
        			writer.write("@media screen and (min-width: " + SML1200+1 + "px) {html {font-size: " + ABMaterial.FontPercentExtraLarge + "% !important}}");
        		}
        		writer.write(".notselectable, .dtp-content, .tabs, .chip, .jssocials{-webkit-touch-callout:none;-webkit-user-select:none;-khtml-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;-webkit-tap-highlight-color:rgba(0,0,0,0);}\n");
        		writer.write("div.abmcode code {font-family: monospace,monospace; padding: .2rem .5rem; margin: 0 .2rem; font-size: 90%; background: #F1F1F1; border: 1px solid #E1E1E1; border-radius: 4px; font-style: normal;} div.abmcode pre > code {    overflow: auto; display: block; white-space: pre;}");
        		writer.write("mark{background: " + ABMaterial.GetColorStrMap(Theme.SearchMarkBackColor, Theme.SearchMarkBackColorIntensity) + " !important; color: " + ABMaterial.GetColorStrMap(Theme.SearchMarkForeColor, Theme.SearchMarkForeColorIntensity) + " !important;}");
        		
        		if (ABMaterial.TryToActivate3DAcceleration) {
        			writer.write("#abm3dacc{-moz-transform:translateX(0);-ms-transform:translateX(0);-o-transform:translateX(0);-webkit-transform:translateX(0);transform:translateX(0);-moz-transform:translateZ(0) translateX(0);-ms-transform:translateZ(0) translateX(0);-o-transform:translateZ(0) translateX(0);-webkit-transform:translateZ(0) translateX(0);transform:translateZ(0) translateX(0);-moz-transform:translate3d(0,0,0);-ms-transform:translate3d(0,0,0);-o-transform:translate3d(0,0,0);-webkit-transform:translate3d(0,0,0);transform:translate3d(0,0,0);-moz-backface-visibility:hidden;-ms-backface-visibility:hidden;-o-backface-visibility:hidden;-webkit-backface-visibility:hidden;backface-visibility:hidden;-moz-perspective:1000;-ms-perspective:1000;-o-perspective:1000;-webkit-perspective:1000;perspective:1000px}");
        		}
        		
        		// rtl
        		writer.write(".abmltr{direction:ltr}.abmrtl{direction:rtl}.input-field.abmrtl label{right:.75rem;padding-left:1.5rem}.input-field.abmrtl > i + input[type=date],.input-field.abmrtl > i + input[type=datetime-local],.input-field.abmrtl > i + input[type=email],.input-field.abmrtl > i + input[type=number],.input-field.abmrtl > i + input[type=password],.input-field.abmrtl > i + input[type=search],.input-field.abmrtl > i + input[type=tel],.input-field.abmrtl > i + input[type=text],.input-field.abmrtl > i + input[type=time],.input-field.abmrtl > i + input[type=url],.input-field.abmrtl > i + textarea.materialize-textarea{margin-left:0!important;margin-right:3rem}.input-field.abmrtl > i + input[type=date] + label,.input-field.abmrtl > i + input[type=datetime-local] + label,.input-field.abmrtl > i + input[type=email] + label,.input-field.abmrtl > i + input[type=number] + label,.input-field.abmrtl > i + input[type=password] + label,.input-field.abmrtl > i + input[type=search] + label,.input-field.abmrtl > i + input[type=tel] + label,.input-field.abmrtl > i + input[type=text] + label,.input-field.abmrtl > i + input[type=time] + label,.input-field.abmrtl > i + input[type=url] + label,.input-field.abmrtl > i + textarea.materialize-textarea + label{right:3.75rem;margin-left:0;padding-left:4.5rem}.input-field.abmrtl > i + input[type=text] + .dropdown-content + label{direction:rtl;right:3.75rem;margin-left:0;padding-left:4.5rem}.input-field.abmrtl > i + input[type=text] + .dropdown-content{direction:rtl;left:.75rem!important}.abmrtl > [type=\"checkbox\"]+label:before,[type=\"checkbox\"]+label:after{right:0!important}.abmrtl > [type=\"checkbox\"].filled-in:checked+label:before{right:11px!important}.abmrtl > [type=\"checkbox\"]+label{padding-right:27px!important}.chip.abmrtl{float:right}");
        		
        		writer.write("#loader-wrapper{position:fixed;top:0;left:0;width:100%;height: 100%;z-index:3000;opacity:" + mLoaderOpacity + ";-webkit-transition:all 0.3s ease-in;transition:all 0.3s ease-in;}");
            	switch (LoaderType) {
            	case LOADERANIM_DEFAULT:
            		writer.write("#loader {display:block;position:relative;left:50%;top:50%;width:150px;height:150px;margin:-75px 0 0 -75px;border-radius:50%;border:3px solid transparent;border-top-color:#3498db;-webkit-animation:spin 2s linear infinite;animation:spin 2s linear infinite;z-index:3001;}");
                	writer.write("#loader:before{content:'';position:absolute;top:5px;left:5px;right:5px;bottom:5px;border-radius:50%;border:3px solid transparent;border-top-color:#e74c3c;-webkit-animation:spin 3s linear infinite;animation:spin 3s linear infinite;}");
                	writer.write("#loader:after{content:'';position:absolute;top:15px;left:15px;right:15px;bottom:15px;border-radius:50%;border:3px solid transparent;border-top-color:#f9c922;-webkit-animation:spin 1.5s linear infinite;animation:spin 1.5s linear infinite;}");
                	writer.write("@-webkit-keyframes spin{0%{-webkit-transform:rotate(0deg);-ms-transform:rotate(0deg);transform:rotate(0deg);} 100%{-webkit-transform: rotate(360deg);-ms-transform:rotate(360deg);transform:rotate(360deg);}}");
                	writer.write("@keyframes spin {0%{-webkit-transform:rotate(0deg);-ms-transform: rotate(0deg);transform: rotate(0deg);} 100%{-webkit-transform: rotate(360deg);-ms-transform: rotate(360deg);transform:rotate(360deg);}}");
                	writer.write("#loader-wrapper .loader-section {position:fixed;top:0;width:51%;height:100%;background:" + ABMaterial.GetColorStrMap(Theme.BackColor,Theme.BackColorIntensity) +"; z-index:3000;-webkit-transform: translateX(0);-ms-transform: translateX(0);transform:translateX(0);}");
                	writer.write("#loader-wrapper .loader-section.section-left{left:0;}");
                	writer.write("#loader-wrapper .loader-section.section-right{right:0;}");
                	writer.write(".loaded #loader {opacity:0;-webkit-transition:all 0.3s ease-out;transition:all 0.3s ease-out;}");
                	writer.write(".loaded #loader-wrapper{opacity:0;-webkit-transition:all 0.3s ease-out; transition:all 0.3s ease-out;}");
                	writer.write(".loaded #loader-wrapper{visibility: hidden;}");
                	writer.write(".progress{background-color:rgba(255,64,129,0.22);}");
                	
                	writer.write("#loader-wrappertemp{position:fixed;top:0;left:0;width:100%;height: 100%;z-index:3000;opacity:1;-webkit-transition:all 0.3s ease-in;transition:all 0.3s ease-in;}");
                	writer.write("#loader-wrappertemp .loader-section {position:fixed;top:0;width:50%;height:100%;background: rgba(0, 0, 0, 0.3); z-index:3000;}");
                	writer.write("#loader-wrappertemp .loader-section.section-left{left:0;}");
                	writer.write("#loader-wrappertemp .loader-section.section-right{right:0;}");
                	writer.write(".loaded #loader-wrappertemp{opacity:0;-webkit-transition:all 0.3s ease-out; transition:all 0.3s ease-out;}");        	
                	writer.write(".loaded #loader-wrappertemp{visibility: hidden;}");
            		break;
            	case LOADERANIM_JUMPINGBALL:
            		writer.write(".L1wrap {position: absolute;top: 50%;left: 50%;-webkit-transform: translate(-50%, -50%);transform: translate(-50%, -50%);}");
            		writer.write(".L1text {color:" + ABMaterial.GetColorStrMap(L1Color,L1ColorIntensity) +";display: inline-block;margin-left: 15px;font-size:2rem;}");
            		writer.write(".L1bounceball {position: relative;display: inline-block;height: 37px;width: 15px;}");
            		writer.write(".L1bounceball:before {position: absolute;content: '';display: block;top: 0;width: 15px;height: 15px;border-radius: 50%;background-color: " + ABMaterial.GetColorStrMap(L1Color,L1ColorIntensity) +";-webkit-transform-origin: 50%;transform-origin: 50%;-webkit-animation: bounce 500ms alternate infinite ease;animation: bounce 500ms alternate infinite ease;}");
            		
            		writer.write("#loader-wrappertemp{position:fixed;top:0;left:0;width:100%;height: 100%;z-index:3000;background-color: rgba(0,0,0,0.6);}");
            		writer.write(".L1wraptemp {position: absolute;top: 50%;left: 50%;-webkit-transform: translate(-50%, -50%);transform: translate(-50%, -50%);}");
            		writer.write(".L1texttemp {color:" + ABMaterial.GetColorStrMap(L1ColorTemp,L1ColorTempIntensity) +";display: inline-block;margin-left: 15px;font-size:2rem;}");
            		writer.write(".L1bounceballtemp {position: relative;display: inline-block;height: 37px;width: 15px;}");
            		writer.write(".L1bounceballtemp:before {position: absolute;content: '';display: block;top: 0;width: 15px;height: 15px;border-radius: 50%;background-color: " + ABMaterial.GetColorStrMap(L1ColorTemp,L1ColorTempIntensity) +";-webkit-transform-origin: 50%;transform-origin: 50%;-webkit-animation: bounce 500ms alternate infinite ease;animation: bounce 500ms alternate infinite ease;}");
            		
            		writer.write("@-webkit-keyframes bounce { 0% {top: 30px;height: 5px;border-radius: 60px 60px 20px 20px;-webkit-transform: scaleX(2);transform: scaleX(2);} 35% {height: 15px;border-radius: 50%;-webkit-transform: scaleX(1);transform: scaleX(1);} 100% {top: 0;}}");
            		writer.write("@keyframes bounce { 0% {top: 30px;height: 5px;border-radius: 60px 60px 20px 20px;-webkit-transform: scaleX(2);transform: scaleX(2);} 35% {height: 15px;border-radius: 50%;-webkit-transform: scaleX(1);transform: scaleX(1);} 100% {top: 0;}}");
            		break;
            	case LOADERANIM_JUGGLINGBALLS:
            		writer.write(".L2circle {animation: spin 3s linear infinite both;background: " + ABMaterial.GetColorStrMap(L2B1Color,L2B1ColorIntensity) +";border-radius: 100vmax;margin: 60vh calc(50vw - 1vmin);height: 5vmin;position: absolute;width: 5vmin;}");
            		writer.write(".L2circle + .L2circle {animation: spin 3s linear 1s infinite both;background: " + ABMaterial.GetColorStrMap(L2B2Color,L2B2ColorIntensity) +";}");
            		writer.write(".L2circle + .L2circle + .L2circle {animation: spin 3s linear 2s infinite both;background: " + ABMaterial.GetColorStrMap(L2B3Color,L2B3ColorIntensity) +";}");
            		writer.write("@keyframes spin { 0% { transform: rotate(360deg) translate(0vmax, -10vmax);} 50% {transform: rotate(180deg) translate(0vmax, 0vmax);} 100% {transform: rotate(0deg) translate(0vmax, -10vmax);}}");
            		        		
            		writer.write("#loader-wrappertemp{position:fixed;top:0;left:0;width:100%;height: 100%;z-index:3000;background-color: rgba(0,0,0,0.6);}");
            		writer.write(".L2circletemp {animation: spin 3s linear infinite both;background: " + ABMaterial.GetColorStrMap(L2B1ColorTemp,L2B1ColorTempIntensity) +";border-radius: 100vmax;margin: 60vh calc(50vw - 1vmin);height: 35px;position: absolute;width: 35px;}");
            		writer.write(".L2circletemp + .L2circletemp {animation: spin 3s linear 1s infinite both;background: " + ABMaterial.GetColorStrMap(L2B2ColorTemp,L2B2ColorTempIntensity) +";}");
            		writer.write(".L2circletemp + .L2circletemp + .L2circletemp {animation: spin 3s linear 2s infinite both;background: " + ABMaterial.GetColorStrMap(L2B3ColorTemp,L2B3ColorTempIntensity) +";}");
            		break;
            	case LOADERANIM_HEARTBEAT:
            		writer.write(".L3container {position: absolute;top:0;bottom: 0;left: 0;right: 0;width: 250px;height: 250px;margin: auto;}");
            		writer.write(".L3part {width: 250px;position: absolute;}");
            		writer.write(".L3svgpath {fill: " + ABMaterial.GetColorStrMapRGBA(L3BColor,L3BColorIntensity,0) + ";stroke: " + ABMaterial.GetColorStrMap(L3BColor,L3BColorIntensity) + ";stroke-width: 1.5px;stroke-dasharray: 1000;stroke-linecap: round;z-index: 2;}");
            		writer.write(".L3svgbg {fill: " + ABMaterial.GetColorStrMap(L3GColor,L3GColorIntensity) + ";z-index: 1;}");
            		writer.write(".L3playload {animation: dash 2.5s reverse ease-in-out infinite;}");
            		writer.write("@keyframes dash {to {stroke-dashoffset: 2000;}}");
            		
            		writer.write("#loader-wrappertemp{position:fixed;top:0;left:0;width:100%;height: 100%;z-index:3000;background-color: rgba(0,0,0,0.6);}");
            		writer.write(".L3containertemp {position: absolute;top:0;bottom: 0;left: 0;right: 0;width: 250px;height: 250px;margin: auto;}");
            		writer.write(".L3parttemp {width: 250px;position: absolute;}");
            		writer.write(".L3svgpathtemp {fill: " + ABMaterial.GetColorStrMapRGBA(L3BColorTemp,L3BColorTempIntensity,0) + ";stroke: " + ABMaterial.GetColorStrMap(L3BColorTemp,L3BColorTempIntensity) + ";stroke-width: 1.5px;stroke-dasharray: 1000;stroke-linecap: round;z-index: 2;}");
            		writer.write(".L3svgbgtemp {fill: " + ABMaterial.GetColorStrMap(L3GColorTemp,L3GColorTempIntensity) + ";z-index: 1;}");
            		writer.write(".L3playloadtemp {animation: dash 2.5s reverse ease-in-out infinite;}");
            		break;
            	case LOADERANIM_DEVICESWITCH:
            		writer.write(".L4dot,.L4loader{position:absolute;top:50%;left:50%}.L4loader{width:200px;height:200px;margin-top:-100px;margin-left:-100px}.L4dot{z-index:10;width:160px;height:100px;margin-top:-50px;margin-left:-80px;border-radius:5px;background-color:#1e3f57;transform-type:preserve-3d;animation:dot1 3s cubic-bezier(.55,.3,.24,.99) infinite}.L4dot:nth-child(2){z-index:11;width:150px;height:90px;margin-top:-45px;margin-left:-75px;border-radius:3px;background-color:#3c617d;animation-name:dot2}.L4dot:nth-child(3){z-index:12;width:40px;height:20px;margin-top:50px;margin-left:-20px;border-radius:0 0 5px 5px;background-color:#6bb2cd;animation-name:dot3}@keyframes dot1{3%,97%{width:160px;height:100px;margin-top:-50px;margin-left:-80px}30%,36%{width:80px;height:120px;margin-top:-60px;margin-left:-40px}63%,69%{width:40px;height:80px;margin-top:-40px;margin-left:-20px}}@keyframes dot2{3%,97%{width:150px;height:90px;margin-top:-45px;margin-left:-75px}30%,36%{width:70px;height:96px;margin-top:-48px;margin-left:-35px}63%,69%{width:32px;height:60px;margin-top:-30px;margin-left:-16px}}@keyframes dot3{3%,97%{width:40px;height:20px;margin-top:50px;margin-left:-20px}30%,36%{width:8px;height:8px;margin-top:49px;margin-left:-5px;border-radius:8px}63%,69%{width:16px;height:4px;margin-top:-37px;margin-left:-8px;border-radius:10px}}");
            		
            		writer.write("#loader-wrappertemp{position:fixed;top:0;left:0;width:100%;height: 100%;z-index:3000;background-color: rgba(0,0,0,0.6);}");
            		writer.write(".L4dottemp,.L4loadertemp{position:absolute;top:50%;left:50%}.L4loadertemp{width:200px;height:200px;margin-top:-100px;margin-left:-100px}.L4dottemp{z-index:10;width:160px;height:100px;margin-top:-50px;margin-left:-80px;border-radius:5px;background-color:#1e3f57;transform-type:preserve-3d;animation:dot1 3s cubic-bezier(.55,.3,.24,.99) infinite}.L4dottemp:nth-child(2){z-index:11;width:150px;height:90px;margin-top:-45px;margin-left:-75px;border-radius:3px;background-color:#3c617d;animation-name:dot2}.L4dottemp:nth-child(3){z-index:12;width:40px;height:20px;margin-top:50px;margin-left:-20px;border-radius:0 0 5px 5px;background-color:#6bb2cd;animation-name:dot3}@keyframes dot1{3%,97%{width:160px;height:100px;margin-top:-50px;margin-left:-80px}30%,36%{width:80px;height:120px;margin-top:-60px;margin-left:-40px}63%,69%{width:40px;height:80px;margin-top:-40px;margin-left:-20px}}@keyframes dot2{3%,97%{width:150px;height:90px;margin-top:-45px;margin-left:-75px}30%,36%{width:70px;height:96px;margin-top:-48px;margin-left:-35px}63%,69%{width:32px;height:60px;margin-top:-30px;margin-left:-16px}}@keyframes dot3{3%,97%{width:40px;height:20px;margin-top:50px;margin-left:-20px}30%,36%{width:8px;height:8px;margin-top:49px;margin-left:-5px;border-radius:8px}63%,69%{width:16px;height:4px;margin-top:-37px;margin-left:-8px;border-radius:10px}}");
            		break;
            	case LOADERANIM_METALGEARSOLID:
            		writer.write(".L5loader {position: absolute;top:0;bottom: 0;left: 0;right: 0;display: block;width: 100px;height: 100px;margin: auto}");
            		writer.write(".L5box {width: 45%;height: 45%;background: " + ABMaterial.GetColorStrMapRGBA(L5Color,L5ColorIntensity,0.7) + ";opacity: 0;-webkit-filter: blur(1px);filter: blur(1px);-webkit-animation: load 1s linear infinite;animation: load 1s linear infinite;}");
            		writer.write(".L5box:nth-of-type(1) {position: absolute;top: 2.5%;left: 2.5%;}");
            		writer.write(".L5box:nth-of-type(2) {position: absolute;top: 2.5%;right: 2.5%;-webkit-animation-delay: -0.25s;animation-delay: -0.25s;}");
            		writer.write(".L5box:nth-of-type(3) {position: absolute;bottom: 2.5%;right: 2.5%;-webkit-animation-delay: -0.5s;animation-delay: -0.5s;}");
            		writer.write(".L5box:nth-of-type(4) {position: absolute;bottom: 2.5%;left: 2.5%;-webkit-animation-delay: -0.75s;animation-delay: -0.75s;}");
            		writer.write("@-webkit-keyframes load { 0% {opacity: 0;} 30% {opacity: 0; } 90% {opacity: 1;} 100% {opacity: 0;}}");
            		writer.write("@keyframes load {0% {opacity: 0;} 30% {opacity: 0; } 90% {opacity: 1; } 100% { opacity: 0; }}");
            		
            		writer.write("#loader-wrappertemp{position:fixed;top:0;left:0;width:100%;height: 100%;z-index:3000;background-color: rgba(0,0,0,0.6);}");
            		writer.write(".L5loadertemp {position: absolute;top:0;bottom: 0;left: 0;right: 0;display: block;width: 100px;height: 100px;margin: auto}");
            		writer.write(".L5boxtemp {width: 45%;height: 45%;background: " + ABMaterial.GetColorStrMapRGBA(L5ColorTemp,L5ColorTempIntensity,0.7) + ";opacity: 0;-webkit-filter: blur(1px);filter: blur(1px);-webkit-animation: load 1s linear infinite;animation: load 1s linear infinite;}");
            		writer.write(".L5boxtemp:nth-of-type(1) {position: absolute;top: 2.5%;left: 2.5%;}");
            		writer.write(".L5boxtemp:nth-of-type(2) {position: absolute;top: 2.5%;right: 2.5%;-webkit-animation-delay: -0.25s;animation-delay: -0.25s;}");
            		writer.write(".L5boxtemp:nth-of-type(3) {position: absolute;bottom: 2.5%;right: 2.5%;-webkit-animation-delay: -0.5s;animation-delay: -0.5s;}");
            		writer.write(".L5boxtemp:nth-of-type(4) {position: absolute;bottom: 2.5%;left: 2.5%;-webkit-animation-delay: -0.75s;animation-delay: -0.75s;}");
            		break;
            	case LOADERANIM_ROTATINGBOXES:
            		writer.write(".L6thing {position:fixed;top: 50%;left: 50%;margin-top:-75px;margin-left:-75px;width:150px;height:150px;transform: rotate(45deg);animation: rotateit 1.5s ease infinite;}");
            		writer.write(".L6beam {width:50px;height:50px;border-radius:10px;position:absolute;}");
            		writer.write(".L6r {background-color:" + ABMaterial.GetColorStrMap(L6ColorA,L6ColorAIntensity) + ";}");
            		writer.write(".L6b {background-color:" + ABMaterial.GetColorStrMap(L6ColorB,L6ColorBIntensity) + ";}");
            		writer.write(".L6r1 {transform: rotate(90deg);left:50px;top:0px;animation: move1r 1.5s ease infinite;}");
            		writer.write(".L6r3 {top:50px;animation: move2 1.5s ease infinite;}");
            		writer.write(".L6b1 {transform: rotate(90deg);left:50px;top:100px;animation: move1b 1.5s ease infinite;}");
            		writer.write(".L6b3 {top:50px;left:100px;animation: move2 1.5s ease infinite;}");
            		writer.write("@-webkit-keyframes rotateit { 0% {transform: rotate(0deg);} 50% {transform: rotate(0deg); } 100% {transform: rotate(90deg); }}");
            		writer.write("@keyframes rotateit { 0% {transform: rotate(0deg);} 50% {transform: rotate(0deg); } 100% {transform: rotate(90deg);}}");
            		writer.write("@keyframes move1r { 0% {top:0px; transform: rotate(0deg);} 45% {top:100px;} 50% {transform: rotate(0deg);} 100% {top:100px;transform: rotate(-90deg);}}");
            		writer.write("@keyframes move2 { 50% {transform: rotate(0deg);} 100% {transform: rotate(-90deg);}}");
            		writer.write("@keyframes move1b { 0% {top:100px; transform: rotate(0deg);} 45% {top:0px;} 50% {transform: rotate(0deg); } 100% { top:0px;transform: rotate(-90deg);}}");
            		
            		writer.write("#loader-wrappertemp{position:fixed;top:0;left:0;width:100%;height: 100%;z-index:3000;background-color: rgba(0,0,0,0.6);}");
            		writer.write(".L6thingtemp {position:fixed;top: 50%;left: 50%;margin-top:-75px;margin-left:-75px;width:150px;height:150px;transform: rotate(45deg);animation: rotateit 1.5s ease infinite;}");
            		writer.write(".L6beamtemp {width:50px;height:50px;border-radius:10px;position:absolute;}");
            		writer.write(".L6rtemp {background-color:" + ABMaterial.GetColorStrMap(L6ColorATemp,L6ColorATempIntensity) + ";}");
            		writer.write(".L6btemp {background-color:" + ABMaterial.GetColorStrMap(L6ColorBTemp,L6ColorBTempIntensity) + ";}");
            		writer.write(".L6r1temp {transform: rotate(90deg);left:50px;top:0px;animation: move1r 1.5s ease infinite;}");
            		writer.write(".L6r3temp {top:50px;animation: move2 1.5s ease infinite;}");
            		writer.write(".L6b1temp {transform: rotate(90deg);left:50px;top:100px;animation: move1b 1.5s ease infinite;}");
            		writer.write(".L6b3temp {top:50px;left:100px;animation: move2 1.5s ease infinite;}");
            		break;
            	}
            	writer.write(".no-js #loader-wrapper{display:none;}");
            	writer.write(".no-js #loader-wrappertemp{display:none;}");
            	
            	writer.write("@page{margin:0;orphans:4; widows:2;}@media print{body{margin:0}body.A3{height:419mm}body.A3.landscape{height:296mm;width:420mm}body.A4{height:296mm}body.A4.landscape,body.A5{height:209mm}body.A5.landscape{height:147mm}body.A3,body.A4.landscape{width:297mm}body.A4,body.A5.landscape{width:210mm}body.A5{width:148mm}body.Letter {width: 216mm;height: 279mm;} body.Letter.landscape {width: 279mm;height: 216mm;}.no-print,.no-print *{display:none !important}, .Squire-UI,.Squire-UI*{display:none !important} main{padding-left:0;background-color:#fff !important} tr, .ct-chart, img, svg, .card, .chronology-slide, .prettyprint, .abmrg, canvas, .abmsw, .abmsoa, .tabs, .responsive-video, .video-container, .abmplannerwrapper{ page-break-inside : avoid;}}");
        		
        		writer.write("</style>\n");
   
    			BufferedWriter writerCSS = null;
    			String CloudCSSPath="";
	        	try {
	        		if (this.CompleteThemes.size()==0) {
	        			File buildFileCSS = new File(pageDir, Name + "." + ReloadStringInner + ".css");	
	        			writerCSS = new BufferedWriter(new FileWriter(buildFileCSS));
	        			if (ABMaterial.CW) {
	                    	String z = ABMaterial.encrypt("This WebApp/WebSite was generated using ABMaterial v" + ABMaterial.Version + ", a B4X library written by Alain Bailleul 2015-2018. See http://alwaysbusycorner.com/abmaterial");
	                    	writerCSS.write("/* " + z + "*/\n");
	                    } else {
	                    	writerCSS.write("/* This WebApp/WebSite was generated using ABMaterial v" + ABMaterial.Version + ", a B4X library written by Alain Bailleul 2015-2018. See http://alwaysbusycorner.com/abmaterial*/\n");
	                    }
	        			WriteCSSPage(writerCSS, relPath);
	        		} else {
	    				File buildFileCSS = new File(pageDir, this.CompleteTheme.Name + Name + "." + ReloadStringInner + ".css");	
	    				writerCSS = new BufferedWriter(new FileWriter(buildFileCSS));
	    				if (ABMaterial.CW) {
	    	            	String z = ABMaterial.encrypt("This WebApp/WebSite was generated using ABMaterial v" + ABMaterial.Version + ", a B4X library written by Alain Bailleul 2015-2018. See http://alwaysbusycorner.com/abmaterial");
	    	            	writerCSS.write("/* " + z + "*/\n");
	    	            } else {
	    	            	writerCSS.write("/* This WebApp/WebSite was generated using ABMaterial v" + ABMaterial.Version + ", a B4X library written by Alain Bailleul 2015-2018. See http://alwaysbusycorner.com/abmaterial*/\n");
	    	            }
	    				WriteCSSPage(writerCSS, relPath);
	        		}
	        	
	        	} catch (Exception e) {
	        			e.printStackTrace();
	        	} finally {
	        		try {
	        			writerCSS.close();
	        	} catch (Exception e) {
	        			}
	        	}
	        	
	        	if (this.CompleteThemes.size()==0) {
	        		if (ABMaterial.GZip) {
	        			File buildFileCSS = new File(pageDir, Name + "." + ReloadStringInner + ".css.gz");
	        			buildFileCSS.delete();
	        			buildFileCSS = new File(pageDir, Name + "." + ReloadStringInner + ".css");
	        			CloudCSSPath=buildFileCSS.getAbsolutePath();
	        			if (buildFileCSS.length()>ABMaterial.minGZipSize) {
	        				GzipIt(buildFileCSS.getAbsolutePath());
	        			}
	        		} else {
	        			File buildFileCSS = new File(pageDir, Name + "." + ReloadStringInner + ".css.gz");
	        			buildFileCSS.delete();
	        			buildFileCSS = new File(pageDir, Name + "." + ReloadStringInner + ".css");
	        			CloudCSSPath=buildFileCSS.getAbsolutePath();
	        		}
	        	} else {
	        		if (ABMaterial.GZip) {
	        			File buildFileCSS = new File(pageDir, this.CompleteTheme.Name + Name + "." + ReloadStringInner + ".css.gz");
	        			buildFileCSS.delete();
	        			buildFileCSS = new File(pageDir, this.CompleteTheme.Name +  Name + "." + ReloadStringInner + ".css");
	        			if (buildFileCSS.length()>ABMaterial.minGZipSize) {
	        				GzipIt(buildFileCSS.getAbsolutePath());
	        			}
	        		} else {
	        			File buildFileCSS = new File(pageDir, this.CompleteTheme.Name + Name + "." + ReloadStringInner + ".css.gz");
	        			buildFileCSS.delete();
	        			buildFileCSS = new File(pageDir, Name + "." + ReloadStringInner + ".css");
	        			CloudCSSPath=buildFileCSS.getAbsolutePath();
	        		}
	        	}
	        	if (ABMaterial.CloudCDN && ABMaterial.CloudUploadGeneratedJSCSS)       {
	        		String CSSUrl = ABMaterial.CloudinaryUploadRawFileInternal(ABMaterial.CloudAppName + "/css/" + Name + "." + ReloadStringInner + ".css", CloudCSSPath);    	        			
	        		if (this.CompleteThemes.size()==0) {   
	        			if (!ABMaterial.LoadCSSAfterDOM) {
	        				writer.write("<link id=\"pagestyle\" type=\"text/css\" rel=\"stylesheet\" href=\"" + CSSUrl + "\" media=\"screen,projection,print\"/>\n");
	        			} else {
	        				CSSAfterDOMScript.add(CSSUrl);
	        			}
	        		}
	        	} else {
	        		if (this.CompleteThemes.size()==0) {
	        			if (!ABMaterial.LoadCSSAfterDOM) {
	        				writer.write("<link id=\"pagestyle\" type=\"text/css\" rel=\"stylesheet\" href=\"" + Name + "." + ReloadStringInner + ".css\" media=\"screen,projection,print\"/>\n");
	        			} else {
	        				CSSAfterDOMScript.add(Name + "." + ReloadStringInner + ".css");
	        			}
	        		}        		
	        	}    	        	
        		
        	} else {
        		writer.write("<style>\n");
        		
        		if (!FontStack.equals("")) {
            		writer.write("html{font-family: " + FontStack + ";}");
            	}
        		
        		if (!FontStack.equals("")) {
            		writer.write("html{font-family: " + FontStack + ";}\n");
            	}
        		if (ABMaterial.FontPercentSmall!=100) {
        			writer.write("@media screen and (max-width: " + SML600 + "px) {html {font-size: " + ABMaterial.FontPercentSmall + "% !important}}");
        		}
        		if (ABMaterial.FontPercentMedium!=100) {
        			writer.write("@media screen and (max-width: " + SML992 + "px) and (min-width: " + (SML600+1) + "px) {html {font-size: " + ABMaterial.FontPercentMedium + "% !important}}");
        		}
        		
        		if (ABMaterial.FontPercentLarge!=100) {
        			writer.write("@media screen and (max-width: " + SML1200 + "px) and (min-width: " + (SML992+1) + "px) {html {font-size: " + ABMaterial.FontPercentMedium + "% !important}}");
        		}
        		if (ABMaterial.FontPercentExtraLarge!=100) {
        			writer.write("@media screen and (min-width: " + SML1200+1 + "px) {html {font-size: " + ABMaterial.FontPercentExtraLarge + "% !important}}");
        		}
        		
        		writer.write(".notselectable, .dtp-content, .tabs, .chip, .jssocials{-webkit-touch-callout:none;-webkit-user-select:none;-khtml-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;-webkit-tap-highlight-color:rgba(0,0,0,0);}\n");
        		writer.write("div.abmcode code {font-family: monospace,monospace; padding: .2rem .5rem; margin: 0 .2rem; font-size: 90%; background: #F1F1F1; border: 1px solid #E1E1E1; border-radius: 4px; font-style: normal;} div.abmcode pre > code {    overflow: auto; display: block; white-space: pre;}");
        		writer.write("mark{background: " + ABMaterial.GetColorStrMap(Theme.SearchMarkBackColor, Theme.SearchMarkBackColorIntensity) + " !important; color: " + ABMaterial.GetColorStrMap(Theme.SearchMarkForeColor, Theme.SearchMarkForeColorIntensity) + " !important;}");
        		
        		if (ABMaterial.TryToActivate3DAcceleration) {
        			writer.write("#abm3dacc{-moz-transform:translateX(0);-ms-transform:translateX(0);-o-transform:translateX(0);-webkit-transform:translateX(0);transform:translateX(0);-moz-transform:translateZ(0) translateX(0);-ms-transform:translateZ(0) translateX(0);-o-transform:translateZ(0) translateX(0);-webkit-transform:translateZ(0) translateX(0);transform:translateZ(0) translateX(0);-moz-transform:translate3d(0,0,0);-ms-transform:translate3d(0,0,0);-o-transform:translate3d(0,0,0);-webkit-transform:translate3d(0,0,0);transform:translate3d(0,0,0);-moz-backface-visibility:hidden;-ms-backface-visibility:hidden;-o-backface-visibility:hidden;-webkit-backface-visibility:hidden;backface-visibility:hidden;-moz-perspective:1000;-ms-perspective:1000;-o-perspective:1000;-webkit-perspective:1000;perspective:1000px}");
        		}
        		
        		if (this.CompleteThemes.size()==0) {
	      			File buildFileCSS = new File(pageDir, Name + "." + ReloadStringInner + ".css.gz");
	      			buildFileCSS.delete();
	      			buildFileCSS = new File(pageDir, Name + "." + ReloadStringInner + ".css");
	      			buildFileCSS.delete();
	      		} else {
	      			File buildFileCSS = new File(pageDir, this.CompleteTheme.Name + Name + "." + ReloadStringInner + ".css.gz");
	      			buildFileCSS.delete();
	      			buildFileCSS = new File(pageDir, this.CompleteTheme.Name + Name + "." + ReloadStringInner + ".css");
	      			buildFileCSS.delete();
	      		}
        		
        		// rtl
        		writer.write(".abmltr{direction:ltr}.abmrtl{direction:rtl}.input-field.abmrtl label{right:.75rem;padding-left:1.5rem}.input-field.abmrtl > i + input[type=date],.input-field.abmrtl > i + input[type=datetime-local],.input-field.abmrtl > i + input[type=email],.input-field.abmrtl > i + input[type=number],.input-field.abmrtl > i + input[type=password],.input-field.abmrtl > i + input[type=search],.input-field.abmrtl > i + input[type=tel],.input-field.abmrtl > i + input[type=text],.input-field.abmrtl > i + input[type=time],.input-field.abmrtl > i + input[type=url],.input-field.abmrtl > i + textarea.materialize-textarea{margin-left:0!important;margin-right:3rem}.input-field.abmrtl > i + input[type=date] + label,.input-field.abmrtl > i + input[type=datetime-local] + label,.input-field.abmrtl > i + input[type=email] + label,.input-field.abmrtl > i + input[type=number] + label,.input-field.abmrtl > i + input[type=password] + label,.input-field.abmrtl > i + input[type=search] + label,.input-field.abmrtl > i + input[type=tel] + label,.input-field.abmrtl > i + input[type=text] + label,.input-field.abmrtl > i + input[type=time] + label,.input-field.abmrtl > i + input[type=url] + label,.input-field.abmrtl > i + textarea.materialize-textarea + label{right:3.75rem;margin-left:0;padding-left:4.5rem}.input-field.abmrtl > i + input[type=text] + .dropdown-content + label{direction:rtl;right:3.75rem;margin-left:0;padding-left:4.5rem}.input-field.abmrtl > i + input[type=text] + .dropdown-content{direction:rtl;left:.75rem!important}.abmrtl > [type=\"checkbox\"]+label:before,[type=\"checkbox\"]+label:after{right:0!important}.abmrtl > [type=\"checkbox\"].filled-in:checked+label:before{right:11px!important}.abmrtl > [type=\"checkbox\"]+label{padding-right:27px!important}.chip.abmrtl{float:right}");
        		
        		writer.write("@page{margin:0;orphans:4; widows:2;}@media print{body{margin:0}body.A3{height:419mm}body.A3.landscape{height:296mm;width:420mm}body.A4{height:296mm}body.A4.landscape,body.A5{height:209mm}body.A5.landscape{height:147mm}body.A3,body.A4.landscape{width:297mm}body.A4,body.A5.landscape{width:210mm}body.A5{width:148mm}body.Letter {width: 216mm;height: 279mm;} body.Letter.landscape {width: 279mm;height: 216mm;}.no-print,.no-print *{display:none !important}, .Squire-UI,.Squire-UI*{display:none !important} main{padding-left:0;background-color:#fff !important} tr, .ct-chart, img, svg, .card, .chronology-slide, .prettyprint, .abmrg, canvas, .abmsw, .abmsoa, .tabs, .responsive-video, .video-container, .abmplannerwrapper{ page-break-inside : avoid;}}");
        		
        		WriteCSSPage(writer, relPath);
        		
        		writer.write("</style>\n");
        	}
            
            if (!TrackingID.equals("")) {
            	writer.write("<script>\n");
            	writer.write("if(navigator.userAgent.indexOf(\"Speed Insights\") == -1) {");
            	writer.write("(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){\n");
            	writer.write("(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),\n");
            	writer.write("m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)\n");
            	writer.write("})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');\n");
            	for (int ga=0;ga<InitialCommands.getSize();ga++) {
            		writer.write("ga(" + InitialCommands.Get(ga) + ");\n");
            	}
            	writer.write("}");
            	writer.write("</script>\n");
            }    
            
            if (UsesFirebase) {
            	if (!ABMaterial.IsSinglePageApp) {
            		writer.write("<script src=\"https://www.gstatic.com/firebasejs/live/3.0/firebase.js\"></script>");
            		writer.write("<script>");
            		writer.write("var config = {");
            		writer.write("apiKey: \"" + mFirebase.ApiKey + "\",");
            		writer.write("authDomain: \"" + mFirebase.AuthDomain + "\",");
            		if (!mFirebase.DatabaseURL.equals("")) {
            			writer.write("databaseURL: \"" + mFirebase.DatabaseURL + "\",");
            		}
            		if (!mFirebase.StorageBucket.equals("")) {
            			writer.write("storageBucket: \"" + mFirebase.StorageBucket + "\",");
            		}
            		writer.write("};");
            		writer.write("firebase.initializeApp(config);");
            		writer.write("firebase.auth().onAuthStateChanged(function(user) {");
            		writer.write("if (user) {");
            		writer.write("b4j_raiseEvent('page_parseevent', {'eventname': 'page_firebaseauthstatechanged','eventparams':'isloggedin','isloggedin':true});"); 
            		writer.write("} else {");
            		writer.write("b4j_raiseEvent('page_parseevent', {'eventname': 'page_firebaseauthstatechanged','eventparams':'isloggedin','isloggedin':false});");
            		writer.write("}");	       
            		writer.write("});");            	
            		writer.write("</script>");
            	}            	
            }
            
            writer.write("<style type=\"text/css\" media=\"print\">");
            writer.write(".pba-auto {page-break-after: auto!important;}");
            writer.write(".pba-always {page-break-after: always!important;}");
            writer.write(".pba-avoid {page-break-after: avoid!important;}");
            writer.write(".pba-left {page-break-after: left!important;}");
            writer.write(".pba-right {page-break-after: right!important;}");
            writer.write(".pba-initial {page-break-after: initial!important;}");
            writer.write(".pba-inherit {page-break-after: inherit!important;}");
        	
            writer.write(".pbb-auto {page-break-before: auto!important;}");
            writer.write(".pbb-always {page-break-before: always!important;}");
            writer.write(".pbb-avoid {page-break-before: avoid!important;}");
            writer.write(".pbb-left {page-break-before: left!important;}");
            writer.write(".pbb-right {page-break-before: right!important;}");
            writer.write(".pbb-initial {page-break-before: initial!important;}");
            writer.write(".pbb-inherit {page-break-before: inherit!important;}");
        	
            writer.write(".pbi-auto {page-break-inside: auto!important;}");
            writer.write(".pbi-avoid {page-break-inside: avoid!important;}");
            writer.write(".pbi-initial {page-break-inside: initial!important;}");
            writer.write(".pbi-inherit {page-break-inside: inherit!important;}");
            writer.write("</style>");
            
            if (mPrintPageLandscape) {
    			writer.write("<style id=\"print-id\">@page { size: " + mPrintPageSize + " landscape}</style>");
    		} else {
    			writer.write("<style id=\"print-id\">@page { size: " + mPrintPageSize + " } </style>");
    		}  
            
            writer.write("<style id=\"globals\" media=\"screen,projection\"></style>");
            
            for (Entry<String,String> entry: svgIconmapContent.entrySet()) {
            	writer.write(entry.getValue());
            }
            
            writer.write("</head>\n");
            String print = mPrintPageSize + " ";
            if (mPrintPageLandscape) {
            	print += "landscape ";
            }
            String mmenu="";
            
            String Acc3D="";
            if (ABMaterial.TryToActivate3DAcceleration) {
            	Acc3D="<div id=\"abm3dacc hide\"></div>";
            }
            
          
            if (mAlwaysShowVerticalScrollBar) {
            	writer.write("<body id=\"print-body\" class=\"" + print + ABMaterial.GetColorStr(Theme.BackColor,Theme.BackColorIntensity, "") + " alwaysoverflow\"\" data-print=\"" + GetPrintSize() + "\">" + Acc3D + mmenu + "\n");
            } else {
            	if (mNeverShowVerticalScrollBar) {
            		writer.write("<body id=\"print-body\" class=\"" + print + ABMaterial.GetColorStr(Theme.BackColor,Theme.BackColorIntensity, "") + " neveroverflow\" data-print=\"" + GetPrintSize() + "\">" + Acc3D + mmenu + "\n");
                } else {                	
                	writer.write("<body id=\"print-body\" class=\"" + print + ABMaterial.GetColorStr(Theme.BackColor,Theme.BackColorIntensity, "") + "\" data-print=\"" + GetPrintSize() + "\">" + Acc3D + mmenu + "\n");
                }
            	
            }
            if (ShowConnectedIndicator) {
            	writer.write("<div id=\"pageconnectedindicator\" class=\"no-print indicatorunknown " + IndicatorPosition + "\"></div>\n");
    		}
            writer.write("<div id=\"devicetype\" class=\"hide no-print\">desktop</div>\n");
                        
            if (!IsApp) {
            	if (NeedsPlanner || NeedsPlannerAll) {
            		writer.write("<div class=\"plannernamenu-container hide\">");
            		writer.write("<div class=\"plannernamenu-item plannernamenu-itemdef z-depth-2\" data-id=\"1\"><i class=\"fa fa-play\"></i></div>");
            		writer.write("<div class=\"plannernamenu-item plannernamenuNA z-depth-2\" data-id=\"2\"><i class=\"fa fa-pause\"></i></div>");
            		writer.write("</div>");
            		writer.write("<div class=\"plannerccpmenu-container hide\">");            
            		writer.write("<div class=\"plannerccpmenu-item z-depth-2\" data-id=\"6\"><i class=\"fa fa-scissors\"></i></div>");
            		writer.write("<div class=\"plannerccpmenu-item z-depth-2\" data-id=\"5\"><i class=\"fa fa-files-o\"></i></div>");
            		writer.write("<div class=\"plannerccpmenu-item z-depth-2\" data-id=\"4\"><i class=\"fa fa-clipboard\"></i></div>");
            		writer.write("<div class=\"plannerccpmenu-item z-depth-2\" data-id=\"3\"><i class=\"fa fa-trash-o\"></i></div>");
            		writer.write("</div>");
            	}
            }
            
            if (mShowLoader) {
            	writer.write("<div id=\"loader-wrapper\">\n");
            	String LoaderWrapper="";
            	switch (LoaderType) {
            		case LOADERANIM_DEFAULT:
            			LoaderWrapper = "<div id=\"loader\"></div><div class=\"loader-section section-left\"></div><div class=\"loader-section section-right\"></div>";
            			break;
            		case LOADERANIM_JUMPINGBALL:
            			LoaderWrapper = "<div class=\"L1wrap\"><div class=\"L1loading\"><div class=\"L1bounceball\"></div><div id=\"L1text\" class=\"L1text\">" + BuildText(L1Text) + "</div></div></div>";
            			break;
            		case LOADERANIM_JUGGLINGBALLS:
            			LoaderWrapper = "<div class=\"L2circle\"></div><div class=\"L2circle\"></div><div class=\"L2circle\"></div>";
            			break;
            		case LOADERANIM_HEARTBEAT:
            			LoaderWrapper = "<div class=\"L3container\"><svg class=\"L3part\" x=\"0px\" y=\"0px\" viewBox=\"0 0 256 256\" style=\"enable-background:new 0 0 256 256;\" xml:space=\"preserve\"><path class=\"L3svgpath L3playload\" d=\"M189.5,140.5c-6.6,29.1-32.6,50.9-63.7,50.9c-36.1,0-65.3-29.3-65.3-65.3 c0,0,17,0,23.5,0c10.4,0,6.6-45.9,11-46c5.2-0.1,3.6,94.8,7.4,94.8c4.1,0,4.1-92.9,8.2-92.9c4.1,0,4.1,83,8.1,83 	c4.1,0,4.1-73.6,8.1-73.6c4.1,0,4.1,63.9,8.1,63.9c4.1,0,4.1-53.9,8.1-53.9c4.1,0,4.1,44.1,8.2,44.1c4.1,0,3.1-34.5,7.2-34.5 	c4.1,0,3.1,24.6,7.2,24.6c4.1,0,2.5-14.5,5.2-14.5c2.2,0,0.8,5.1,4.2,4.9c0.4,0,13.1,0,13.1,0c0-34.4-27.9-62.3-62.3-62.3 c-27.4,0-50.7,17.7-59,42.3\" /><path class=\"L3svgbg\"  d=\"M61,126c0,0,16.4,0,23,0c10.4,0,6.6-45.9,11-46c5.2-0.1,3.6,94.8,7.4,94.8c4.1,0,4.1-92.9,8.2-92.9 c4.1,0,4.1,83,8.1,83c4.1,0,4.1-73.6,8.1-73.6c4.1,0,4.1,63.9,8.1,63.9c4.1,0,4.1-53.9,8.1-53.9c4.1,0,4.1,44.1,8.2,44.1 	c4.1,0,3.1-34.5,7.2-34.5c4.1,0,3.1,24.6,7.2,24.6c4.1,0,2.5-14.5,5.2-14.5c2.2,0,0.8,5.1,4.2,4.9c0.4,0,22.5,0,23,0\" /></svg></div>";
            			break;
            		case LOADERANIM_DEVICESWITCH:
            			LoaderWrapper = "<div class=\"L4loader\"><div class=\"L4dot\"></div><div class=\"L4dot\"></div><div class=\"L4dot\"></div></div>";
            			break;
            		case LOADERANIM_METALGEARSOLID:
            			LoaderWrapper = "<div class=\"L5loader\"><div class=\"L5box\"></div><div class=\"L5box\"></div><div class=\"L5box\"></div><div class=\"L5box\"></div></div>";
            			break;
            		case LOADERANIM_ROTATINGBOXES:
            			LoaderWrapper = "<div class=\"L6thing\"><div class=\"L6beam L6r1 L6r\"></div><div class=\"L6beam L6r3 L6r\"></div><div class=\"L6beam L6b1 L6b\"></div><div class=\"L6beam L6b3 L6b\"></div></div>";
            			break;
            	}
            	writer.write(LoaderWrapper);            	
            	writer.write("</div>\n");
            }
            writer.write("<div id=\"isloaderwrapper\" class=\"isloading\">");
            String container="";
            if (CenterInPageWithPadding)  {
            	container = "container";
            }
            
            if (navigationBar!=null) {
            	writer.write("<header class=\"" + navigationBar.mHidden + "\">\n");
            	writer.write(navigationBar.Build());
            	writer.write("</header>\n");
            	if (this.navigationBar.WithExtraContent || this.navigationBar.WithExtraHalfButton) {
            		writer.write("<main class=\"" + container + "\" style=\"padding-top: " + (PaddingTop + 64) + "px;padding-bottom: " + PaddingBottom + "px\">\n");
            	} else {
            		writer.write("<main class=\"" + container + "\" style=\"padding-top: " + (PaddingTop + 56) + "px;padding-bottom: " + PaddingBottom + "px\">\n");
            	}
            } else {
            	writer.write("<main class=\"" + container + "\" style=\"padding-top: " + PaddingTop + "px;padding-bottom: " + PaddingBottom + "px\">\n");
            }                     
            
            writer.write(Build());
           
            writer.write("</main>\n");
            
            if (!IsApp) {
            	mUseWebWorker=false;            	
            } else {
            	mUseWebWorker=ABMaterial.mUseWebWorker;
            }
            @SuppressWarnings("unused")
			String WebLoader="";
            
            if (!Footer.Rows.isEmpty()) {
            	if (IsFixedFooter) {
            		writer.write("<footer class=\"no-print footerfixed " + ABMaterial.GetColorStr(Footer.Theme.BackColor, Footer.Theme.BackColorIntensity, "") + "\">\n");
            	} else {
            		writer.write("<footer class=\"no-print footerfloating " + ABMaterial.GetColorStr(Footer.Theme.BackColor, Footer.Theme.BackColorIntensity, "") + "\">\n");
            	}
            	writer.write(Footer.Build());
            	writer.write("</footer>\n");
            }
            
            if (ABMaterial.LoadCSSAfterDOM) {
            	for (String cssStr: CSSAfterDOM) {
            		writer.write(cssStr);
            	}
            }
            
            switch (ABMaterial.CacheSystem) {
            case "2.0":
            	writer.write("<script " + loadAsync + "type=\"text/javascript\" src=\"" + relPath + "js/sessioncreator.js" + "\"></script>\n");
            	break;
            case "3.0":
            	break;
            default:
            	writer.write("<script " + loadAsync + "type=\"text/javascript\" src=\"" + relPath + "js/sessioncreator.js" + ReloadString + "\"></script>\n");
            	break;
            }
            
            if (!ABMaterial.LoadJavascriptsAfterDOM) { 
            	if (NeedsJQueryUICore) {
            		writer.write("<script " + loadAsync + "type=\"text/javascript\" src=\"" + URelPath + "js/core" + ABMaterial.JSCore + ".UI" + DebugMin + ".js\"></script>\n");
            	} else {
            		writer.write("<script " + loadAsync + "type=\"text/javascript\" src=\"" + URelPath + "js/core" + ABMaterial.JSCore + DebugMin + ".js\"></script>\n");
            	}
            } else {
            	if (NeedsJQueryUICore) {
            		JSFiles.add(URelPath + "js/core" + ABMaterial.JSCore + ".UI" + DebugMin + ".js");
            	} else {
            		JSFiles.add(URelPath + "js/core" + ABMaterial.JSCore + DebugMin + ".js");
            	}
            }
            
            if (NeedsGoogleMap || (NeedsGoogleMapAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	String tmpKey="";            
            	tmpKey = GoogleMapsAPIExtras;
            	if (tmpKey.equals("")) {
            		tmpKey="?sensor=true";
            	} else {
            		tmpKey = "?" + tmpKey;
            	}
            	
            	if (!ABMaterial.LoadJavascriptsAfterDOM) { 
            		writer.write("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/markerclustererplus/2.1.4/markerclusterer.min.js\"></script>\n");
            		writer.write("<script " + loadAsync + "type=\"text/javascript\" src=\"https://maps.googleapis.com/maps/api/js" + tmpKey + "\"></script>\n");
            		if (UseCDN || !JoinAllJS) {writer.write("<script " + loadAsync + "type=\"text/javascript\" src=\"" + URelPath + "js/gmaps" + ABMaterial.JSGmaps + DebugMin + ".js\"></script>\n");}
            	} else {
            		JSFiles.add("https://cdnjs.cloudflare.com/ajax/libs/markerclustererplus/2.1.4/markerclusterer.min.js");
            		JSFiles.add("https://maps.googleapis.com/maps/api/js" + tmpKey);
            		JSFiles.add(URelPath + "js/gmaps" + ABMaterial.JSGmaps + DebugMin + ".js");
            	}
            	
            }
            
            if (!ABMaterial.LoadJavascriptsAfterDOM) {
            	if (!UseCDN && JoinAllJS) {writer.write("<script " + loadAsync + "type=\"text/javascript\" src=\"" + relPathApp + "core" + DebugMin + "." + ReloadStringInner + ".js\"></script>\n");}            	
            }
            
            if (DebugConzole) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/conzole" + ABMaterial.JSConzole + DebugMin + ".js", jsPath + "js/conzole" + DebugMin + ".js", DebugConzole, DebugConzole, mUseWebWorker);}
            	} else {
            		JSFiles.add(URelPath + "js/conzole" + ABMaterial.JSConzole + DebugMin + ".js");
            	}
            }
            
            if (NeedsSimpleBar || (NeedsSimpleBarAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/simplebar" + DebugMin + ".js", jsPath + "js/simplebar" + DebugMin + ".js", NeedsSimpleBar, NeedsSimpleBarAll, mUseWebWorker);}
            	} else {
            		JSFiles.add(URelPath + "js/simplebar" + DebugMin + ".js");
            	}
            }
            
            if (NeedsSmartWizard || (NeedsSmartWizardAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/jquery.smartWizard" + ABMaterial.JSSmartWizard + DebugMin + ".js", jsPath + "js/jquery.smartWizard" + DebugMin + ".js", NeedsSmartWizard, NeedsSmartWizardAll, mUseWebWorker);}
            	} else {
            		JSFiles.add(URelPath + "js/jquery.smartWizard" + ABMaterial.JSSmartWizard + DebugMin + ".js");
            	}
            }            
            
            if (this.navigationBar!=null) {
            	if (this.navigationBar.HasFadeEffect) {
            		if (!ABMaterial.LoadJavascriptsAfterDOM) {
            			if (UseCDN || !JoinAllJS) {writer.write("<script type=\"text/javascript\" src=\"" + URelPath + "js/jquery.abscrollfire" + ABMaterial.JSABScrollFire + DebugMin + ".js\"></script>\n");}
            		} else {
            			JSFiles.add(URelPath + "js/jquery.abscrollfire" + ABMaterial.JSABScrollFire + DebugMin + ".js");
            		}
            	}
            }
            
            if (NeedsCalendar || NeedsDateTimeScroller || NeedsDateTimePicker || (NeedsCalendarAll && ABMaterial.PreloadAllJavascriptAndCSSFiles) || (NeedsDateTimeScrollerAll && ABMaterial.PreloadAllJavascriptAndCSSFiles) || (NeedsDateTimePickerAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/moment-with-locales" + ABMaterial.JSMoment + DebugMin + ".js", jsPath + "js/moment-with-locales" + DebugMin + ".js", (NeedsCalendar || NeedsDateTimeScroller || NeedsDateTimePicker), (NeedsCalendarAll || NeedsDateTimeScrollerAll || NeedsDateTimePickerAll), mUseWebWorker);}
            	} else {
            		JSFiles.add(URelPath + "js/moment-with-locales" + ABMaterial.JSMoment + DebugMin + ".js");
            	}
            	
            }
                       
            if (NeedsTreeTable || (NeedsTreeTableAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/abmtreetable" + ABMaterial.JSAbmTreeTable + DebugMin + ".js", jsPath + "js/abmtreetable" + DebugMin + ".js", (NeedsTreeTable), (NeedsTreeTableAll), mUseWebWorker);}
            	} else {
            		JSFiles.add(URelPath + "js/abmtreetable" + ABMaterial.JSAbmTreeTable + DebugMin + ".js");
            	}
            }
            
            if (NeedsFileManager || (NeedsFileManagerAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/abmfilemanager" + ABMaterial.JSFileManager + DebugMin + ".js", jsPath + "js/abmfilemanager" + DebugMin + ".js", (NeedsFileManager), (NeedsFileManagerAll), mUseWebWorker);}
            	} else {
            		JSFiles.add(URelPath + "js/abmfilemanager" + ABMaterial.JSFileManager + DebugMin + ".js");
            	}
            	
            }
                       
            if (NeedsCalendar || (NeedsCalendarAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {
            			WebLoader+=LoadScript(writer, URelPath + "js/abmcalendar" + ABMaterial.JSAbmCalendar + DebugMin + ".js", jsPath + "js/abmcalendar" + DebugMin + ".js", (NeedsCalendar), (NeedsCalendarAll), mUseWebWorker);
            			WebLoader+=LoadScript(writer, URelPath + "js/lang-all" + ABMaterial.JSLangAll + ".js", jsPath + "js/lang-all" + ABMaterial.JSLangAll + ".js" , (NeedsCalendar), (NeedsCalendarAll), mUseWebWorker);
            		}
            	} else {
            		JSFiles.add(URelPath + "js/abmcalendar" + ABMaterial.JSAbmCalendar + DebugMin + ".js");
            		JSFiles.add(URelPath + "js/lang-all" + ABMaterial.JSLangAll + ".js");
            	}
            }
                       
            if (NeedsMask || NeedsTable || (NeedsMaskAll && ABMaterial.PreloadAllJavascriptAndCSSFiles) || (NeedsTableAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (ABMaterial.InputMaskNewVersion==false) {
	            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
	            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/jquery.inputmask.bundle" + ABMaterial.JSInputMask + DebugMin + ".js", jsPath + "js/jquery.inputmask.bundle" + DebugMin + ".js", (NeedsMask || NeedsTable), (NeedsMaskAll || NeedsTableAll), mUseWebWorker);}
	            	} else {
	            		JSFiles.add(URelPath + "js/jquery.inputmask.bundle" + ABMaterial.JSInputMask + DebugMin + ".js");
	            	}
            	} else {
            		if (!ABMaterial.LoadJavascriptsAfterDOM) {
	            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/jquery.inputmask.abm" + ABMaterial.JSInputMask + DebugMin + ".js", jsPath + "js/jquery.inputmask.abm" + DebugMin + ".js", (NeedsMask || NeedsTable), (NeedsMaskAll || NeedsTableAll), mUseWebWorker);}
	            	} else {
	            		JSFiles.add(URelPath + "js/jquery.inputmask.abm" + ABMaterial.JSInputMask + DebugMin + ".js");
	            	}
            	}
            }
            
            if (NeedsJQueryUI || (NeedsJQueryUIAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/jquery-ui-tableinfinite" + DebugMin + ".js", jsPath + "js/jquery-ui-tableinfinite" + DebugMin + ".js", NeedsJQueryUI, NeedsJQueryUI, mUseWebWorker);}
            	} else {
            		JSFiles.add(URelPath + "js/jquery-ui-tableinfinite" + DebugMin + ".js");
            	}            	
            }
            
            if (NeedsUpload || (NeedsUploadAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/abmupload" + ABMaterial.JSAbmUpload + DebugMin + ".js", jsPath + "js/abmupload" + DebugMin + ".js", (NeedsUpload), (NeedsUploadAll), mUseWebWorker);}
            	} else {
            		JSFiles.add(URelPath + "js/abmupload" + ABMaterial.JSAbmUpload + DebugMin + ".js");
            	}
            }                     
            
            if (NeedsChart || (NeedsChartAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/chartist" + ABMaterial.JSChartist + DebugMin + ".js", jsPath + "js/chartist" + DebugMin + ".js", (NeedsChart), (NeedsChartAll), mUseWebWorker);}
            	} else {
            		JSFiles.add(URelPath + "js/chartist" + ABMaterial.JSChartist + DebugMin + ".js");
            	}
            }                     
            
            if (!ABMaterial.LoadJavascriptsAfterDOM) {
            	String CloudJSPath="";
            	for (String s: ExtraJS) {
            		if (!s.equalsIgnoreCase("sessioncreator.js")) {
            			if (s.startsWith("http")) {
            				writer.write("<script " + loadAsync + "type=\"text/javascript\" src=\"" + s + "\"></script>\n");            				
            			} else {
            				buildFile = new File(jsPath + "js", s);
                        	CloudJSPath=buildFile.getAbsolutePath();
            				if (ABMaterial.GZip) {
            					buildFile = new File(jsPath + "js", s + ".gz");
                            	buildFile.delete();
                            	buildFile = new File(jsPath + "js", s);
                            	CloudJSPath=buildFile.getAbsolutePath(); 
                            	if (buildFile.length()>ABMaterial.minGZipSize) {
                            		GzipIt(buildFile.getAbsolutePath());
                            	}
                        	}
            				if (ABMaterial.CloudCDN && ABMaterial.CloudUploadGeneratedJSCSS)       {
                    			String JSUrl = ABMaterial.CloudinaryUploadRawFileInternal(ABMaterial.CloudAppName + "/js/" + s, CloudJSPath);                    			
                	      		writer.write("<script " + loadAsync + "type=\"text/javascript\" src=\"" + JSUrl + "\"></script>\n");
                        	} else {
                        		writer.write("<script " + loadAsync + "type=\"text/javascript\" src=\"" + relPath + "js/" + s + "\"></script>\n");
                        	}
            			}
            			
            		}
            	}
            } else {
            	String CloudJSPath="";
            	for (String s: ExtraJS) {            		
            		if (!s.equalsIgnoreCase("sessioncreator.js")) {
            			if (s.startsWith("http")) {
            				JSFiles.add(s);
            			} else {
            				buildFile = new File(jsPath + "js", s);
                        	CloudJSPath=buildFile.getAbsolutePath();
            				if (ABMaterial.GZip) {
            					buildFile = new File(jsPath + "js", s + ".gz");
                            	buildFile.delete();
                            	buildFile = new File(jsPath + "js", s);
                            	CloudJSPath=buildFile.getAbsolutePath();
                            	if (buildFile.length()>ABMaterial.minGZipSize) {
                            		GzipIt(buildFile.getAbsolutePath());
                            	}
                        	}
            				if (ABMaterial.CloudCDN && ABMaterial.CloudUploadGeneratedJSCSS)       {
                    			String JSUrl = ABMaterial.CloudinaryUploadRawFileInternal(ABMaterial.CloudAppName + "/js/" + s, CloudJSPath);                    			
                	      		JSFiles.add(JSUrl);
                        	} else {
                        		JSFiles.add(relPath + "js/" + s);
                        	}
            			}
            		}
            	}
            }
            
            if (!ABMaterial.IsSinglePageApp) {
            	if (NeedsYouTube) {
            		if (!ABMaterial.LoadJavascriptsAfterDOM) {
            			writer.write("<script " + loadAsync + "src=\"https://www.youtube.com/iframe_api\"></script>\n");
            		} else {
            			JSFiles.add("https://www.youtube.com/iframe_api");
            		}
            	}
            } else {
            	if (NeedsYouTubeAll) {
            		if (!ABMaterial.LoadJavascriptsAfterDOM) {
            			writer.write("<script " + loadAsync + "src=\"https://www.youtube.com/iframe_api\"></script>\n");
            		} else {
            			JSFiles.add("https://www.youtube.com/iframe_api");
            		}
            	}
            }
            
            if (NeedsCanvas || (NeedsCanvasAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/abmcanvas" + ABMaterial.JSAbmCanvas + DebugMin + ".js", jsPath + "js/abmcanvas" + DebugMin + ".js", (NeedsCanvas), (NeedsCanvasAll), mUseWebWorker);}
            	} else {
            		JSFiles.add(URelPath + "js/abmcanvas" + ABMaterial.JSAbmCanvas + DebugMin + ".js");
            	}
            }
           
            if (NeedsSortingTable || (NeedsSortingTableAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/sorttable" + ABMaterial.JSSortTable + DebugMin + ".js", jsPath + "js/sorttable" + DebugMin + ".js", (NeedsSortingTable), (NeedsSortingTableAll), mUseWebWorker);}
            	} else {
            		JSFiles.add(URelPath + "js/sorttable" + ABMaterial.JSSortTable + DebugMin + ".js");
            	}
            }	
            
            if (NeedsTable || NeedsTreeTable || (NeedsTableAll && ABMaterial.PreloadAllJavascriptAndCSSFiles) || (NeedsTreeTableAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/jquery.ABMTableScroll" + ABMaterial.JSABMTableScroll + DebugMin + ".js", jsPath + "js/jquery.ABMTableScroll" + DebugMin + ".js", (NeedsTable || NeedsTreeTable), (NeedsTableAll || NeedsTreeTableAll), mUseWebWorker);}
            	} else {
            		JSFiles.add(URelPath + "js/jquery.ABMTableScroll" + ABMaterial.JSABMTableScroll + DebugMin + ".js");
            	}
            }
                                 
            if (NeedsSignature || (NeedsSignatureAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/signature_pad" + ABMaterial.JSSignaturePad + DebugMin + ".js", jsPath + "js/signature_pad" + DebugMin + ".js", (NeedsSignature), (NeedsSignatureAll), mUseWebWorker);}
            	} else {
            		JSFiles.add(URelPath + "js/signature_pad" + ABMaterial.JSSignaturePad + DebugMin + ".js");
            	}
        	}
            
            if (NeedsOAuth || (NeedsOAuthAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/hello.all" + ABMaterial.JSHello + DebugMin + ".js", jsPath + "js/hello.all" + DebugMin + ".js", (NeedsOAuth), (NeedsOAuthAll), mUseWebWorker);}
            	} else {
            	JSFiles.add(URelPath + "js/hello.all" + ABMaterial.JSHello + DebugMin + ".js");
            	}
            }
            
            if  (NeedsSlider || NeedsRange || (NeedsSliderAll && ABMaterial.PreloadAllJavascriptAndCSSFiles) || (NeedsRangeAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            	if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/nouislider" + ABMaterial.JSNoUISlider + DebugMin + ".js", jsPath + "js/nouislider" + DebugMin + ".js", (NeedsSlider || NeedsRange), (NeedsSliderAll || NeedsRangeAll), mUseWebWorker);}
            	} else {
            		JSFiles.add(URelPath + "js/nouislider" + ABMaterial.JSNoUISlider + DebugMin + ".js");
            	}
            }
           
            if (NeedsDateTimeScroller || (NeedsDateTimeScrollerAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/mobiscroll" + ABMaterial.JSMobiScroll + DebugMin + ".js", jsPath + "js/mobiscroll" + DebugMin + ".js", (NeedsDateTimeScroller ), (NeedsDateTimeScrollerAll), mUseWebWorker);}
            	} else {
            		JSFiles.add(URelPath + "js/mobiscroll" + ABMaterial.JSMobiScroll + DebugMin + ".js");
            	}
            }
           
            if (NeedsDateTimePicker || (NeedsDateTimePickerAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/bootstrap-material-datetimepicker" + ABMaterial.JSDateTimePicker + DebugMin + ".js", jsPath + "js/bootstrap-material-datetimepicker" + DebugMin + ".js", (NeedsDateTimePicker), (NeedsDateTimePickerAll), mUseWebWorker);}
            	} else {
            		JSFiles.add(URelPath + "js/bootstrap-material-datetimepicker" + ABMaterial.JSDateTimePicker + DebugMin + ".js");
            	}
            }
            
            if (NeedsEditor || (NeedsEditorAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/abmsquire" + ABMaterial.JSAbmSquire + DebugMin + ".js", jsPath + "js/abmsquire" + DebugMin + ".js", (NeedsEditor), (NeedsEditorAll), mUseWebWorker);}
            	} else {
            		JSFiles.add(URelPath + "js/abmsquire" + ABMaterial.JSAbmSquire + DebugMin + ".js");
            	}
            }
            
            if (NeedsSocialShare || (NeedsSocialShareAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/jssocials" + ABMaterial.JSJSSocials + DebugMin + ".js", jsPath + "js/jssocials" + DebugMin + ".js", (NeedsSocialShare), (NeedsSocialShareAll), mUseWebWorker);}
            	} else {
            		JSFiles.add(URelPath + "js/jssocials" + ABMaterial.JSJSSocials + DebugMin + ".js");
            	}
            }
            
            if (NeedsAudioPlayer || (NeedsAudioPlayerAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/APlayer" + ABMaterial.JSAPlayer + DebugMin + ".js", jsPath + "js/APlayer" + DebugMin + ".js", (NeedsAudioPlayer), (NeedsAudioPlayerAll), mUseWebWorker);};
            	} else {
            		JSFiles.add(URelPath + "js/APlayer" + ABMaterial.JSAPlayer + DebugMin + ".js");
            	}
            }
            
            if (NeedsPatternLock || (NeedsPatternLockAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/patternLock" + ABMaterial.JSPatternLock + DebugMin + ".js", jsPath + "js/patternLock" + DebugMin + ".js", (NeedsPatternLock), (NeedsPatternLockAll), mUseWebWorker);}
            	} else {
            		JSFiles.add(URelPath + "js/patternLock" + ABMaterial.JSPatternLock + DebugMin + ".js");
            	}
            }
            
            if (NeedsPlanner || (NeedsPlannerAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/abmplanner" + ABMaterial.JSAbmPlanner + DebugMin + ".js", jsPath + "js/abmplanner" + DebugMin + ".js", (NeedsPlanner), (NeedsPlannerAll), mUseWebWorker);}
            	} else {
            		JSFiles.add(URelPath + "js/abmplanner" + ABMaterial.JSAbmPlanner + DebugMin + ".js");
            	}
            }
            
            if (NeedsPivot || (NeedsPivotAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/abmpivotexport" + ABMaterial.JSAbmPivotExport + DebugMin + ".js", jsPath + "js/abmpivotexport" + DebugMin + ".js", (NeedsPivot), (NeedsPivotAll), mUseWebWorker);}
            	} else {
            		//JSFiles.add(jsPath + "js/abmpivotexport" + ABMaterial.JSAbmPivotExport + DebugMin + ".js"); // does not work, needs another system
            		JSFiles.add(URelPath + "js/abmpivotexport" + ABMaterial.JSAbmPivotExport + DebugMin + ".js");
            	}
            }
                        
            if (NeedsTimeline || (NeedsTimelineAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/timeline" + ABMaterial.JSTimeline + DebugMin + ".js", jsPath + "js/timeline" + DebugMin + ".js", (NeedsTimeline), (NeedsTimelineAll), mUseWebWorker);}
            	} else {
            		JSFiles.add(URelPath + "js/timeline" + ABMaterial.JSTimeline + DebugMin + ".js");
            	}
            }
            
            if (NeedsFlexWall || (NeedsFlexWallAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/jquery.flex-images" + ABMaterial.JSFlexImages + DebugMin + ".js", jsPath + "js/jquery.flex-images" + DebugMin + ".js", (NeedsFlexWall), (NeedsFlexWallAll), mUseWebWorker);}
            	} else {
            		JSFiles.add(URelPath + "js/jquery.flex-images" + ABMaterial.JSFlexImages + DebugMin + ".js");
            	}
            }
            
            if (NeedsSVGSurface || (NeedsSVGSurfaceAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (UseCDN || !JoinAllJS) {WebLoader+=LoadScript(writer, URelPath + "js/abmsvgsurface" + ABMaterial.JSSVGSurface + DebugMin + ".js", jsPath + "js/abmsvgsurface" + DebugMin + ".js", (NeedsSVGSurface), (NeedsSVGSurfaceAll), mUseWebWorker);}
            	} else {
            		JSFiles.add(URelPath + "js/abmsvgsurface" + ABMaterial.JSSVGSurface + DebugMin + ".js");
            	}
            }
            
            if (NeedsCodeLabel || (NeedsCodeLabelAll && ABMaterial.PreloadAllJavascriptAndCSSFiles)) {
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		//writer.write("<script type=\"text/javascript\" " + loadAsync + "src=\"https://cdn.rawgit.com/google/code-prettify/master/loader/prettify.js?skin=sunburst\"></script>\n");
            		writer.write("<script type=\"text/javascript\" " + loadAsync + "src=\"" + URelPath + "js/prettify.js\"></script>\n");
            	} else {
            		JSFiles.add(URelPath + "js/prettify.js");
            	}
            }
            
            if (NeedsB4JS) {
        		if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		writer.write("<script type=\"text/javascript\" " + loadAsync + "src=\"" + relPathApp + "b4js." + ReloadStringInner + ".js\"></script>\n");
            	} else {
            		JSFiles.add(relPathApp + "b4js." + ReloadStringInner + ".js");
            	}
        	}
            
            if (!PageJSInlineBool) {          
            	String CloudJSPath="";
            	BufferedWriter writerJS = null;
	        	try {        		
	    			File buildFileJS = new File(pageDir, Name + "." + ReloadStringInner + ".js");	
	    			CloudJSPath = buildFileJS.getAbsolutePath();
	    			writerJS = new BufferedWriter(new FileWriter(buildFileJS));	       
	    			if (ABMaterial.CW) {
	                	String z = ABMaterial.encrypt("This WebApp/WebSite was generated using ABMaterial v" + ABMaterial.Version + ", a B4X library written by Alain Bailleul 2015-2018. See http://alwaysbusycorner.com/abmaterial");
	                	writerJS.write("// " + z + "\n");
	                } else {
	                	writerJS.write("// This WebApp/WebSite was generated using ABMaterial v" + ABMaterial.Version + ", a B4X library written by Alain Bailleul 2015-2018. See http://alwaysbusycorner.com/abmaterial\n");
	                }
	    			WriteJSPage(writerJS, relPath, NeedsAutorization,ReloadStringInner, IsApp);
	        	} catch (Exception e) {
	        		e.printStackTrace();
	        	} finally {
	        		try {
	        			writerJS.close();
	        		} catch (Exception e) {
	        		
	        		}
	        	}
	        	
	        	
	    		if (ABMaterial.GZip) {
	    			File buildFileJS = new File(pageDir, Name + "." + ReloadStringInner + ".js.gz");
	    			buildFileJS.delete();
	    			buildFileJS = new File(pageDir, Name + "." + ReloadStringInner + ".js");
	    			CloudJSPath = buildFileJS.getAbsolutePath();
	    			if (buildFileJS.length()>ABMaterial.minGZipSize) {
	    				GzipIt(buildFileJS.getAbsolutePath());
	    			}
	    		} else {
	    			File buildFileJS = new File(pageDir, Name + "." + ReloadStringInner + ".js.gz");
	    			buildFileJS.delete();
	    			buildFileJS = new File(pageDir, Name + "." + ReloadStringInner + ".js");
	    			CloudJSPath = buildFileJS.getAbsolutePath();
	    		}
            	
            	if (!ABMaterial.LoadJavascriptsAfterDOM) {
            		if (ABMaterial.CloudCDN && ABMaterial.CloudUploadGeneratedJSCSS)       {
            			String JSUrl = ABMaterial.CloudinaryUploadRawFileInternal(ABMaterial.CloudAppName + "/js/" + Name + "." + ReloadStringInner + ".js", CloudJSPath);            			
        	      		WebLoader+=LoadScriptWithID(writer, JSUrl, JSUrl, true, false, false, "pagescript");
            	      	
        	      	} else {
        	      		WebLoader+=LoadScriptWithID(writer, Name + "." + ReloadStringInner + ".js", Name + "." + ReloadStringInner + ".js", true, false, false, "pagescript");
        	      	}
        	    } else {
        	    	if (ABMaterial.CloudCDN && ABMaterial.CloudUploadGeneratedJSCSS)       {
        	    		String JSUrl = ABMaterial.CloudinaryUploadRawFileInternal(ABMaterial.CloudAppName + "/js/" + Name + "." + ReloadStringInner + ".js", CloudJSPath);        	    		
        	      		JSFiles.add(JSUrl);
        	      	} else {
        	      		JSFiles.add(Name + "." + ReloadStringInner + ".js");
        	      	}
            	}            	
            } else {
            	File buildFileJS = new File(pageDir, Name + "." + ReloadStringInner + ".js.gz");
        		buildFileJS.delete();
        		buildFileJS = new File(pageDir, this.CompleteTheme.Name + Name + "." + ReloadStringInner + ".js");
        		buildFileJS.delete();   
            	
            	writer.write("<script>\n");
            	WriteJSPage(writer, relPath, NeedsAutorization, ReloadStringInner, IsApp);
            	writer.write("</script>\n");
            }
            
            if (ABMaterial.LoadCSSAfterDOM) {
            	writer.write("<script>");
            	writer.write("function loadCSSInOrder(){if(cssorder!=cssMap.length){var r=cssMap[cssorder],e=document.createElement(\"link\");e.href=r,e.type='text/css',e.rel='stylesheet',e.onload=function(){cssorder++,loadCSSInOrder()},document.getElementsByTagName(\"head\")[0].appendChild(e)}}var cssMap=[");
            	boolean isNotFirst=false;
            	for (String sCSS: CSSAfterDOMScript) {
            		if (isNotFirst) {
            			writer.write(",");
            		}
            		isNotFirst=true;
            		writer.write("\"" + sCSS + "\"");
            	}
            	writer.write("],cssorder=0;loadCSSInOrder();");
            	writer.write("</script>");
            }
            
            if (ABMaterial.LoadJavascriptsAfterDOM) {
            	writer.write("<script>");
            	writer.write("function loadScriptInOrder(){if(order!=scriptMap.length){var r=scriptMap[order],e=document.createElement(\"script\");e.src=r,e.onload=function(){order++,loadScriptInOrder()},document.getElementsByTagName(\"body\")[0].appendChild(e)}}var scriptMap=[");
            	boolean isNotFirst=false;
            	for (String sJS: JSFiles) {
            		if (isNotFirst) {
            			writer.write(",");
            		}
            		isNotFirst=true;
            		writer.write("\"" + sJS + "\"");
            	}
            	writer.write("],order=0;loadScriptInOrder();");
            	writer.write("</script>");
            }
            
                        
            writer.write("</div>\n");            
            writer.write("</body>\n");
            writer.write("</html>\n");
          }  
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
                
        if ((IsApp && !UseCDN)) { // || ((IsApp && ABMaterial.CloudCDN && ABMaterial.CloudUploadGeneratedJSCSS))) {
        	String CloudCSSPath="";
        	BA.Log("Building core.min." + ReloadStringInner + ".css...");
        	File buildFile = new File(pageDir, "core.min." + ReloadStringInner + ".css");
        	buildFile.delete();
        	CloudCSSPath=buildFile.getAbsolutePath();
        	Path outputCSS = Paths.get(pageDir + File.separator + "core.min." + ReloadStringInner + ".css");
        	Charset charset = StandardCharsets.UTF_8;
        	for (String sCSS: CSSFiles) {
        		Path pCSS = Paths.get(sCSS);
        		List<String> lines;
				try {
					lines = Files.readAllLines(pCSS, charset);
					Files.write(outputCSS, lines, charset, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
				} catch (IOException e) {					
					e.printStackTrace();
				}                
        	}
        	BufferedWriter IconCSS = null;
        	try {
        		IconCSS = new BufferedWriter(new FileWriter(buildFile, true));
        		if (ABMaterial.UseMaterialFontsFromWebLink==false) {
        			IconCSS.write("@font-face{font-family:'Material Icons';font-style:normal;font-weight:400;src:url(../font/iconfont/MaterialIcons-Regular.eot);src:local('Material Icons'),local(MaterialIcons-Regular),url(../font/iconfont/MaterialIcons-Regular.woff2) format(\"woff2\"),url(../font/iconfont/MaterialIcons-Regular.woff) format(\"woff\"),url(../font/iconfont/MaterialIcons-Regular.ttf) format(\"truetype\")}.material-icons{font-family:'Material Icons';font-weight:400;font-style:normal;font-size:24px;display:inline-block;line-height:1;text-transform:none;letter-spacing:normal;word-wrap:normal;white-space:nowrap;direction:ltr;text-rendering:optimizeLegibility;font-feature-settings:liga}");
            	}
        		IconCSS.write(ABMaterial.BuildedIconsCSS);
        		
        	} catch (Exception e) {
    			e.printStackTrace();
        	} finally {
        		try {
        			IconCSS.close();
        		} catch (Exception e) {
    			}
        	}
        	
        	if (ABMaterial.GZip) {
        		buildFile = new File(pageDir, "core.min." + ReloadStringInner + ".css.gz");
            	buildFile.delete();
            	
            	buildFile = new File(pageDir, "core.min." + ReloadStringInner + ".css");
            	CloudCSSPath=buildFile.getAbsolutePath();
            	if (buildFile.length()>ABMaterial.minGZipSize) {
            		GzipIt(buildFile.getAbsolutePath());
            	}
        	}
        	
        	if (ABMaterial.CloudCDN && ABMaterial.CloudUploadGeneratedJSCSS)       {
    			ABMaterial.CloudinaryUploadRawFileInternal(ABMaterial.CloudAppName + "/css/core.min." + ReloadStringInner + ".css", CloudCSSPath);
    			ABMaterial.CloudinaryDeleteAllOldFiles();
        	}        	
        	
        } else {
        	if (IsApp && UseCDN) {
        		String CloudCSSPath="";
        		BA.Log("Building core.min." + ReloadStringInner + ".css...");
            	File buildFile = new File(pageDir, "core.min." + ReloadStringInner + ".css");
            	CloudCSSPath=buildFile.getAbsolutePath();
            	buildFile.delete();
            	BufferedWriter IconCSS = null;
            	try {
            		IconCSS = new BufferedWriter(new FileWriter(buildFile, true));
            		if (ABMaterial.UseMaterialFontsFromWebLink==false) {
            			IconCSS.write("@font-face{font-family:'Material Icons';font-style:normal;font-weight:400;src:url(../font/iconfont/MaterialIcons-Regular.eot);src:local('Material Icons'),local(MaterialIcons-Regular),url(../font/iconfont/MaterialIcons-Regular.woff2) format(\"woff2\"),url(../font/iconfont/MaterialIcons-Regular.woff) format(\"woff\"),url(../font/iconfont/MaterialIcons-Regular.ttf) format(\"truetype\")}.material-icons{font-family:'Material Icons';font-weight:400;font-style:normal;font-size:24px;display:inline-block;line-height:1;text-transform:none;letter-spacing:normal;word-wrap:normal;white-space:nowrap;direction:ltr;text-rendering:optimizeLegibility;font-feature-settings:liga}");
                	}
            		IconCSS.write(ABMaterial.BuildedIconsCSS);
            		
            	} catch (Exception e) {
        			e.printStackTrace();
            	} finally {
            		try {
            			IconCSS.close();
            		} catch (Exception e) {
        			}
            	}
            	
            	
            	if (ABMaterial.GZip) {
            		buildFile = new File(pageDir, "core.min." + ReloadStringInner + ".css.gz");
                	buildFile.delete();
                	buildFile = new File(pageDir, "core.min." + ReloadStringInner + ".css");
                	CloudCSSPath=buildFile.getAbsolutePath();
                	if (buildFile.length()>ABMaterial.minGZipSize) {
                		GzipIt(buildFile.getAbsolutePath());
                	}
            	}
            	
            	
            	if (ABMaterial.CloudCDN && ABMaterial.CloudUploadGeneratedJSCSS)       {
        			ABMaterial.CloudinaryUploadRawFileInternal(ABMaterial.CloudAppName + "/css/core.min." + ReloadStringInner + ".css", CloudCSSPath);
            	}
            	
        	}
        }
        
        
        if (ABMaterial.GZip) {
        	File buildFile = new File(pageDir, fileName + ".gz");
        	buildFile.delete();
        	buildFile = new File(pageDir, fileName);
        	if (buildFile.length()>ABMaterial.minGZipSize) {
        		GzipIt(buildFile.getAbsolutePath());
        	}
        } else {
        	File buildFile = new File(pageDir, fileName + ".gz");
        	buildFile.delete();
        }
        
        File buildFile = new File(pageDir, "donotdelete.conn");
        if (buildFile.exists()) {
        	buildFile.delete();
        }
        
       
	}
	
	public void CloseCurrentBrowserTab() {
		if (ws!=null) {
			try {
				ws.RunFunction("windowClose", null);			
				if (ws.getOpen()) {
					if (ShowDebugFlush) {BA.Log("CloseCurrentBrowserTab");}
					ws.Flush();RunFlushed();
				}
			} catch (IOException e) {			

			}
			
		}
	}
	
	protected void WriteJSPageExtra(BufferedWriter writer, boolean NeedsAutorization, String ReloadStringInner, boolean IsApp) throws IOException {		
		writer.write("var pageVisibility=function(){var i,t=function(i,t){return\"\"!==i?i+t.slice(0,1).toUpperCase()+t.slice(1):t},e=function(){var e=!1;return\"number\"==typeof window.screenX&&[\"webkit\",\"moz\",\"ms\",\"o\",\"\"].forEach(function(n){0==e&&void 0!=document[t(n,\"hidden\")]&&(i=n,e=!0)}),e}(),n=function(){if(e)return document[t(i,\"hidden\")]},s=function(){if(e)return document[t(i,\"visibilityState\")]};return{hidden:n(),visibilityState:s(),visibilitychange:function(t,a){if(a=!1,e&&\"function\"==typeof t)return document.addEventListener(i+\"visibilitychange\",function(i){this.hidden=n(),this.visibilityState=s(),t.call(this,i)}.bind(this),a)}}}();pageVisibility.visibilitychange(function(){b4j_raiseEvent(\"page_parseevent\",{eventname:\"page_visibilitystate\",eventparams:\"state\",state:this.visibilityState})});");
				
        writer.write("var nativePlatform=''\nfunction getnative() {return nativePlatform;}\nfunction setnative(nat) {nativePlatform = nat;}\nfunction NativeRequest(json) {ABMController.NativeRequest(json);}\n");
        writer.write("var _b4jsthemes={};");
        writer.write("var _b4jsvars={};");
        writer.write("var _b4jsclasses={};");
        writer.write("var abmdz=null;");
        writer.write("var timeLines = [];");
        writer.write("var isSyncing = {};");
        writer.write("var currABMrep = {};");
        writer.write("function SyncTimelines(me, curs) {");
        writer.write("for (var k=0;k<timeLines.length;k++) {");
        writer.write("if (timeLines[k]!=me) {");
        writer.write("isSyncing[timeLines[k]] = true;");
        writer.write("$('#' + timeLines[k]).dateline('cursor', curs);");
        writer.write("}");
        writer.write("}");
        writer.write("}");
        
        if (NeedsB4JS) {
        	for (Entry<String,String> entry: ABMaterial.SavedB4JSClasses.entrySet()) {
        		if (!entry.getKey().equals("b4js")) {
        			String className = entry.getKey();
        			writer.write("if (_b4jsclasses[\"b4jspagekey" + className.toLowerCase() + "\"] === undefined) {");
        			writer.write("_b4jsclasses[\"b4jspagekey" + className.toLowerCase() + "\"] = new b4js_" + className.toLowerCase() + "();");
        			writer.write("_b4jsclasses[\"b4jspagekey" + className.toLowerCase() + "\"].initializeb4js();");
        			writer.write("}");
        		}
        	}
        }
        
        if (NeedsPatternLock) {
        	writer.write("var patlocks={};");
        	for (Entry<String,ABMPatternLock> pl: PatternLocks.entrySet()) {
        		writer.write(pl.getValue().BuildJavaScript() + "\n");
        	}
        }
        
        if (NeedsDateTimeScroller) {
        	writer.write("$(document).bind('mobileinit',function(){\n");
        	writer.write("$.mobile.changePage.defaults.changeHash = false;\n");
        	writer.write("$.mobile.hashListeningEnabled = false;\n");
        	writer.write("$.mobile.pushStateEnabled = false;\n");
        	writer.write("});\n");  
        }
        
        if (NeedsSVGSurface) {
        	writer.write("var svgs={};");
        	writer.write("var svgse={};");
        	writer.write("var svgst={};");
        	writer.write("var svgstop={};");
        	for (Entry<String,ABMSVGSurface> sf: SVGSurfaces.entrySet()) {
        		writer.write(sf.getValue().BuildJavaScript() + "\n");
        	}
        	writer.write("function polarToCartesian(centerX, centerY, radius, angleInDegrees) {");
        	writer.write("var angleInRadians = (angleInDegrees-90) * Math.PI / 180.0;");
        	writer.write("return {x: centerX + (radius * Math.cos(angleInRadians)),y: centerY + (radius * Math.sin(angleInRadians))};");
        	writer.write("}\n");
        }
                   
        if (NeedsChronologyList) {
        	writer.write("var Arrive=function(a,b,c){\"use strict\";function l(a,b,c){e.addMethod(b,c,a.unbindEvent),e.addMethod(b,c,a.unbindEventWithSelectorOrCallback),e.addMethod(b,c,a.unbindEventWithSelectorAndCallback)}function m(a){a.arrive=j.bindEvent,l(j,a,\"unbindArrive\"),a.leave=k.bindEvent,l(k,a,\"unbindLeave\")}if(a.MutationObserver&&\"undefined\"!=typeof HTMLElement){var d=0,e=function(){var b=HTMLElement.prototype.matches||HTMLElement.prototype.webkitMatchesSelector||HTMLElement.prototype.mozMatchesSelector||HTMLElement.prototype.msMatchesSelector;return{matchesSelector:function(a,c){return a instanceof HTMLElement&&b.call(a,c)},addMethod:function(a,b,c){var d=a[b];a[b]=function(){return c.length==arguments.length?c.apply(this,arguments):\"function\"==typeof d?d.apply(this,arguments):void 0}},callCallbacks:function(a){for(var c,b=0;c=a[b];b++)c.callback.call(c.elem)},checkChildNodesRecursively:function(a,b,c,d){for(var g,f=0;g=a[f];f++)c(g,b,d)&&d.push({callback:b.callback,elem:g}),g.childNodes.length>0&&e.checkChildNodesRecursively(g.childNodes,b,c,d)},mergeArrays:function(a,b){var d,c={};for(d in a)c[d]=a[d];for(d in b)c[d]=b[d];return c},toElementsArray:function(b){return\"undefined\"==typeof b||\"number\"==typeof b.length&&b!==a||(b=[b]),b}}}(),f=function(){var a=function(){this._eventsBucket=[],this._beforeAdding=null,this._beforeRemoving=null};return a.prototype.addEvent=function(a,b,c,d){var e={target:a,selector:b,options:c,callback:d,firedElems:[]};return this._beforeAdding&&this._beforeAdding(e),this._eventsBucket.push(e),e},a.prototype.removeEvent=function(a){for(var c,b=this._eventsBucket.length-1;c=this._eventsBucket[b];b--)a(c)&&(this._beforeRemoving&&this._beforeRemoving(c),this._eventsBucket.splice(b,1))},a.prototype.beforeAdding=function(a){this._beforeAdding=a},a.prototype.beforeRemoving=function(a){this._beforeRemoving=a},a}(),g=function(b,d){var g=new f,h=this,i={fireOnAttributesModification:!1};return g.beforeAdding(function(c){var i,e=c.target;c.selector,c.callback;(e===a.document||e===a)&&(e=document.getElementsByTagName(\"html\")[0]),i=new MutationObserver(function(a){d.call(this,a,c)});var j=b(c.options);i.observe(e,j),c.observer=i,c.me=h}),g.beforeRemoving(function(a){a.observer.disconnect()}),this.bindEvent=function(a,b,c){b=e.mergeArrays(i,b);for(var d=e.toElementsArray(this),f=0;f<d.length;f++)g.addEvent(d[f],a,b,c)},this.unbindEvent=function(){var a=e.toElementsArray(this);g.removeEvent(function(b){for(var d=0;d<a.length;d++)if(this===c||b.target===a[d])return!0;return!1})},this.unbindEventWithSelectorOrCallback=function(a){var f,b=e.toElementsArray(this),d=a;f=\"function\"==typeof a?function(a){for(var e=0;e<b.length;e++)if((this===c||a.target===b[e])&&a.callback===d)return!0;return!1}:function(d){for(var e=0;e<b.length;e++)if((this===c||d.target===b[e])&&d.selector===a)return!0;return!1},g.removeEvent(f)},this.unbindEventWithSelectorAndCallback=function(a,b){var d=e.toElementsArray(this);g.removeEvent(function(e){for(var f=0;f<d.length;f++)if((this===c||e.target===d[f])&&e.selector===a&&e.callback===b)return!0;return!1})},this},h=function(){function h(a){var b={attributes:!1,childList:!0,subtree:!0};return a.fireOnAttributesModification&&(b.attributes=!0),b}function i(a,b){a.forEach(function(a){var c=a.addedNodes,d=a.target,f=[];null!==c&&c.length>0?e.checkChildNodesRecursively(c,b,k,f):\"attributes\"===a.type&&k(d,b,f)&&f.push({callback:b.callback,elem:node}),e.callCallbacks(f)})}function k(a,b,f){if(e.matchesSelector(a,b.selector)&&(a._id===c&&(a._id=d++),-1==b.firedElems.indexOf(a._id))){if(b.options.onceOnly){if(0!==b.firedElems.length)return;b.me.unbindEventWithSelectorAndCallback.call(b.target,b.selector,b.callback)}b.firedElems.push(a._id),f.push({callback:b.callback,elem:a})}}var f={fireOnAttributesModification:!1,onceOnly:!1,existing:!1};j=new g(h,i);var l=j.bindEvent;return j.bindEvent=function(a,b,c){\"undefined\"==typeof c?(c=b,b=f):b=e.mergeArrays(f,b);var d=e.toElementsArray(this);if(b.existing){for(var g=[],h=0;h<d.length;h++)for(var i=d[h].querySelectorAll(a),j=0;j<i.length;j++)g.push({callback:c,elem:i[j]});if(b.onceOnly&&g.length)return c.call(g[0].elem);setTimeout(e.callCallbacks,1,g)}l.call(this,a,b,c)},j},i=function(){function d(a){var b={childList:!0,subtree:!0};return b}function f(a,b){a.forEach(function(a){var c=a.removedNodes,f=(a.target,[]);null!==c&&c.length>0&&e.checkChildNodesRecursively(c,b,h,f),e.callCallbacks(f)})}function h(a,b){return e.matchesSelector(a,b.selector)}var c={};k=new g(d,f);var i=k.bindEvent;return k.bindEvent=function(a,b,d){\"undefined\"==typeof d?(d=b,b=c):b=e.mergeArrays(c,b),i.call(this,a,b,d)},k},j=new h,k=new i;b&&m(b.fn),m(HTMLElement.prototype),m(NodeList.prototype),m(HTMLCollection.prototype),m(HTMLDocument.prototype),m(Window.prototype);var n={};return l(j,n,\"unbindAllArrive\"),l(k,n,\"unbindAllLeave\"),n}}(window,\"undefined\"==typeof jQuery?null:jQuery,void 0);\n");
        	
        	writer.write("function PositionChronos(id) {");
        	writer.write("var slideCounter=0;");
        	writer.write("var lastLeft=0;");
        	writer.write("var lastRight=0;");
        	writer.write("var lastTop=0;");
        	writer.write("$('#' + id + ' > li').each(function() {");
        	writer.write("$(this).removeClass('chronology-inverted');");
        	writer.write("$(this).css('margin-top',0);");
        	writer.write("});	");
        	writer.write("var mq = window.matchMedia( \"(max-width: 767px)\" );");
        	writer.write("if (mq.matches && !IsPrinting) {");
        	writer.write("return;");
        	writer.write("}");
        	writer.write("$('#' + id + ' > li').each(function() {");
        	writer.write("slideCounter++;");
        	writer.write("var height = $(this).outerHeight();");
        	writer.write("var bottom = $(this).offset().top + height;");
        	writer.write("if (slideCounter>2) {");
        	writer.write("if (lastLeft<lastRight) {");
        	writer.write("$(this).removeClass('chronology-inverted');");
        	writer.write("var mar = lastTop - lastLeft;");
        	writer.write("if (mar>-50) {");
        	writer.write("$(this).css('margin-top',mar+50);"); 
        	writer.write("} else {");
        	writer.write("$(this).css('margin-top',0);"); 
        	writer.write("}");
        	writer.write("bottom = $(this).offset().top + height;");
        	writer.write("lastLeft = bottom;");
        	writer.write("lastTop = $(this).offset().top;");
        	writer.write("} else {");
        	writer.write("$(this).addClass('chronology-inverted');");
        	writer.write("var mar = lastTop - lastRight;");
        	writer.write("if (mar>-50) {");
        	writer.write("$(this).css('margin-top',mar+50);"); 
        	writer.write("} else {");
        	writer.write("$(this).css('margin-top',0);"); 
        	writer.write("}");
        	writer.write("bottom = $(this).offset().top + height;");
        	writer.write("lastRight=bottom;");
        	writer.write("lastTop = $(this).offset().top;");
        	writer.write("}");
        	writer.write("} else {");
        	writer.write("if (slideCounter==1) {");
        	writer.write("lastLeft = bottom;");
        	writer.write("lastTop = $(this).offset().top;");
        	writer.write("} else {");
        	writer.write("$(this).addClass('chronology-inverted');");
        	writer.write("$(this).css('margin-top','60px');");
        	writer.write("bottom = $(this).offset().top + height;");
        	writer.write("lastRight = bottom;");
        	writer.write("lastTop = $(this).offset().top;");
        	writer.write("}");
        	writer.write("}");	
        	writer.write("});");	
        	writer.write("};");
        	writer.write("function PositionChronosAll() {");
        	writer.write("$('.chronology').each(function() {");
        	writer.write("PositionChronos(this.id);");
        	writer.write("});");
        	writer.write("};");
        	writer.write("$(document).arrive('.materialboxed', {fireOnAttributesModification: true, existing: true}, function() {");
        	writer.write("var $newElem = $(this);");
        	writer.write("$newElem.one('load',function() {");
        	writer.write("PositionChronosAll();");
        	writer.write("});");	
        	writer.write("});\n");
        } else {
        	writer.write("function PositionChronosAll() {};");
        }
        
        writer.write("var allobjectids={};");
                
        if (!ABMaterial.IsSinglePageApp) {
        	writer.write("setTimeout(function repeat() {\n");
            writer.write("console.log('heartbeat');\n");
            if (IsApp) {
            	writer.write("KeepAlive('./session.heartbeat');\n");
            } else {
            	writer.write("KeepAlive('../session.heartbeat');\n");
            }
            if (HeartBeatSecs==-1) {
            	if (SessionMaxInactiveInterval==-1) {
            		writer.write("setTimeout(repeat, 600000);\n");
            		writer.write("}, 600000);\n");
            	} else {
            		writer.write("setTimeout(repeat, " + (SessionMaxInactiveInterval*500) + ");\n");
            		writer.write("}, " + (SessionMaxInactiveInterval*500) + ");\n");
            	}
            } else {
            	writer.write("setTimeout(repeat, " + (HeartBeatSecs*1000) + ");\n");
        		writer.write("}, " + (HeartBeatSecs*1000) + ");\n");            	
            }
		} else {
			if (IsApp) {
				writer.write("setTimeout(function repeat() {\n");
	             writer.write("console.log('heartbeat');\n");
	             writer.write("KeepAlive('./session.heartbeat');\n");
	             if (HeartBeatSecs==-1) {
	             	if (SessionMaxInactiveInterval==-1) {
	             		writer.write("setTimeout(repeat, 600000);\n");
	             		writer.write("}, 600000);\n");
	             	} else {
	             		writer.write("setTimeout(repeat, " + (SessionMaxInactiveInterval*500) + ");\n");
	             		writer.write("}, " + (SessionMaxInactiveInterval*500) + ");\n");
	             	}
	             } else {
	             	writer.write("setTimeout(repeat, " + (HeartBeatSecs*1000) + ");\n");
	         		writer.write("}, " + (HeartBeatSecs*1000) + ");\n");            	
	             }     
			}
		}
        
        writer.write("function windowClose() {");
        writer.write("window.open('','_parent','');");
        writer.write("window.close();");
        writer.write("};");
        
        if (DisableBackButton) {
        
        	writer.write("history.pushState(null, null, '');\n");
        	writer.write("window.addEventListener('popstate', function(event) {\n");
        	writer.write("history.pushState(null, null, '');\n");
        	writer.write("});\n");
        
   	 	}
        
        if (CompleteThemes.size()>0) {
        	writer.write("function settheme(themeParam) {");
        	writer.write("var ls = document.createElement('link');");
        	writer.write("ls.type=\"text/css\";");
        	writer.write("ls.rel=\"stylesheet\";");

        	writer.write("ls.href=themeParam+\"" + Name + "." + ReloadStringInner + ".css\";");
        	writer.write("ls.media=\"screen,projection,print\";");
        	writer.write("document.getElementsByTagName('head')[0].appendChild(ls);\n");
        	writer.write("};\n");
        }
   	 	            
        writer.write("\n"); // NEEDED
        
        for (Entry<String,ABMContainer> cont: Containers.entrySet()) {
        	String s = cont.getValue().RunInitialAnimation();
        	if (!s.equals("")) {
        		writer.write(s);
        	}
    	}
        if (this.DisablePageReloadOnSwipeDown) {
        	writer.write("\nvar maybePreventPullToRefresh = false;");
        	writer.write("var lastTouchY = 0;");
        	writer.write("var touchstartHandler = function(e) {");
        	writer.write("if (e.touches.length != 1) return;");
        	writer.write("lastTouchY = e.touches[0].clientY;");
        	writer.write("maybePreventPullToRefresh = (window.pageYOffset == 0);");
        	writer.write("};");
        	writer.write("var touchmoveHandler = function(e) {");
        	writer.write("var touchY = e.touches[0].clientY;");
        	writer.write("var touchYDelta = touchY - lastTouchY;");
        	writer.write("lastTouchY = touchY;");
        	writer.write("if (maybePreventPullToRefresh) {");
        	writer.write("maybePreventPullToRefresh = false;");
        	writer.write("if (touchYDelta > 0) {");
        	writer.write("e.preventDefault();");
        	writer.write("return;");
        	writer.write("}");
        	writer.write("}");       	    
        	writer.write("};");
        	writer.write("document.addEventListener('touchstart', touchstartHandler, false);");
        	writer.write("document.addEventListener('touchmove', touchmoveHandler, false);\n");
        }
        
        writer.write("var fpCharts={};");
        writer.write("function ChangePrintVisibility(e){e?($(\".only-print.hide\").removeClass(\"hide\").addClass(\"hideP\"),$(\".only-print.hide-on-small-only\").removeClass(\"hide-on-small-only\").addClass(\"hide-on-small-onlyP\"),$(\".only-print.hide-on-med-only\").removeClass(\"hide-on-med-only\").addClass(\"hide-on-med-onlyP\"),$(\".only-print.hide-on-med-and-down\").removeClass(\"hide-on-med-and-down\").addClass(\"hide-on-med-and-downP\"),$(\".only-print.hide-on-med-and-up\").removeClass(\"hide-on-med-and-up\").addClass(\"hide-on-med-and-upP\"),$(\".only-print.hide-on-large-only\").removeClass(\"hide-on-large-only\").addClass(\"hide-on-large-onlyP\"),$(\".only-print.hide-on-extralarge-only\").removeClass(\"hide-on-extralarge-only\").addClass(\"hide-on-extralarge-onlyP\"),$(\".only-print.hide-on-large-and-down\").removeClass(\"hide-on-large-and-down\").addClass(\"hide-on-large-and-downP\"),$(\".only-print.hide-on-large-and-up\").removeClass(\"hide-on-large-and-up\").addClass(\"hide-on-large-and-upP\"),$(\".only-print.show-on-extralarge\").removeClass(\"show-on-extralarge\").addClass(\"show-on-extralargeP\"),$(\".only-print.show-on-large-and-up\").removeClass(\"show-on-large-and-up\").addClass(\"show-on-large-and-upP\"),$(\".only-print.show-on-large-and-down\").removeClass(\"show-on-large-and-down\").addClass(\"show-on-large-and-downP\")):($(\".only-print.hideP\").removeClass(\"hideP\").addClass(\"hide\"),$(\".only-print.hide-on-small-onlyP\").removeClass(\"hide-on-small-onlyP\").addClass(\"hide-on-small-only\"),$(\".only-print.hide-on-med-onlyP\").removeClass(\"hide-on-med-onlyP\").addClass(\"hide-on-med-only\"),$(\".only-print.hide-on-med-and-downP\").removeClass(\"hide-on-med-and-downP\").addClass(\"hide-on-med-and-down\"),$(\".only-print.hide-on-med-and-upP\").removeClass(\"hide-on-med-and-upP\").addClass(\"hide-on-med-and-up\"),$(\".only-print.hide-on-large-onlyP\").removeClass(\"hide-on-large-onlyP\").addClass(\"hide-on-large-only\"),$(\".only-print.hide-on-extralarge-onlyP\").removeClass(\"hide-on-extralarge-onlyP\").addClass(\"hide-on-extralarge-only\"),$(\".only-print.hide-on-large-and-downP\").removeClass(\"hide-on-large-and-downP\").addClass(\"hide-on-large-and-down\"),$(\".only-print.hide-on-large-and-upP\").removeClass(\"hide-on-large-and-upP\").addClass(\"hide-on-large-and-up\"),$(\".only-print.show-on-extralargeP\").removeClass(\"show-on-extralargeP\").addClass(\"show-on-extralarge\"),$(\".only-print.show-on-large-and-upP\").removeClass(\"show-on-large-and-upP\").addClass(\"show-on-large-and-up\"),$(\".only-print.show-on-large-and-downP\").removeClass(\"show-on-large-and-downP\").addClass(\"show-on-large-and-down\"))}   var IsPrinting=!1;!function(){var e=function(){var e=$(\"body\").attr(\"data-print\");$(\"body\").attr(\"style\",\"width: \"+e+\" !important\"),ChangePrintVisibility(!0),IsPrinting=!0;var n=document.querySelector(\".ct-chart\");if(n)for(var d in n.style.width=\"auto\",charts)charts[d].update();if(!$.isEmptyObject(fpCharts)){var i=navigator.userAgent.toLowerCase();if(i.indexOf(\"firefox\")>-1||i.indexOf(\"edge\")>-1)for(var d in fpCharts){var o=document.getElementById(fpCharts[d].parent.id),a=document.getElementById(\"print-body\").getAttribute(\"data-print\").replace(\"mm\",\"\"),t=$(o).closest(\".row\").width(),r=o.clientWidth/t*a;o.style.width=r+\"mm\",fpCharts[d].refresh()}else for(var d in fpCharts)fpCharts[d].refresh()}PositionChronosAll(),IsPrinting=!1},n=function(){$(\"body\").removeAttr(\"style\"),ChangePrintVisibility(!1),IsPrinting=!0;var e=document.querySelector(\".ct-chart\");if(e)for(var n in e.style.width=\"auto\",charts)charts[n].update();if(!$.isEmptyObject(fpCharts)){var d=navigator.userAgent.toLowerCase();if(d.indexOf(\"firefox\")>-1||d.indexOf(\"edge\")>-1)for(var i in fpCharts){document.getElementById(fpCharts[i].parent.id).style.width=\"100%\",fpCharts[i].refresh()}else for(var i in fpCharts)fpCharts[i].refresh()}PositionChronosAll(),IsPrinting=!1};window.matchMedia&&window.matchMedia(\"print\").addListener(function(d){d.matches?e():n()}),window.onbeforeprint=e,window.onafterprint=n}();");
        
        writer.write("function RefreshSharts() {");
        writer.write("var n = document.querySelector('.ct-chart');");
        writer.write("if (n) {");
        writer.write("n.style.width = 'auto';");
        writer.write("for (var e in charts) charts[e].update()");
        writer.write("}");
        writer.write("if (!$.isEmptyObject(fpCharts)) {");
        writer.write("var ua = navigator.userAgent.toLowerCase();");
        writer.write("if (ua.indexOf('firefox') > -1 || ua.indexOf('edge') > -1) {");
        writer.write("for (var d in fpCharts) {");
        writer.write("var chart = document.getElementById(fpCharts[d].parent.id);");
        writer.write("chart.style.width = '100%';");
        writer.write("fpCharts[d].refresh();");
        writer.write("}");
        writer.write("} else {");
        writer.write("for (var d in fpCharts) {");				
        writer.write("fpCharts[d].refresh();");
        writer.write("}");
        writer.write("}");
        writer.write("}");
        writer.write("}");
        
        if (NeedsTable || NeedsTreeTable) {            	
        	for (Entry<String,ABMTable> t: Tables.entrySet()) {
        		writer.write("jQuery('#" + t.getValue().ParentString + t.getValue().ArrayName.toLowerCase() + t.getValue().ID.toLowerCase() + "').ABMTableScroll();\n");
        	}            	
        }
        if (NeedsTimeline) {
        	writer.write("var timelines={};");
        	for (Entry<String,ABMTimeLine> tl: TimeLines.entrySet()) {
        		writer.write(tl.getValue().BuildJavaScript() + "\n");
        	}
        }
        if (NeedsFlexWall) {            	
        	for (Entry<String,ABMFlexWall> fw: FlexWalls.entrySet()) {
        		writer.write(fw.getValue().BuildJavaScript() + "\n");
        	}
        }
        if (NeedsSlider) {
        	for (Entry<String,ABMSlider> sl: sliders.entrySet()) {
        		writer.write(sl.getValue().BuildJavaScript());
        	}
        }
        if (NeedsRange) {
        	for (Entry<String,ABMRange> ra: ranges.entrySet()) {
        		writer.write(ra.getValue().BuildJavaScript());
        	}
        }
        if (NeedsDateTimeScroller) {              	
        	for (Entry<String,ABMDateTimeScroller> scr: dateTimeScrollers.entrySet()) {
        		writer.write(scr.getValue().BuildJavaScript());
        	}
        }
        if (NeedsDateTimePicker) {              	
        	for (Entry<String,ABMDateTimePicker> scr: dateTimePickers.entrySet()) {
        		writer.write(scr.getValue().BuildJavaScript());
        	}
        }
        if (NeedsSocialShare) {
        	writer.write("window.openShareWindow = function(url) {");
        	writer.write("window.open(url, 'Sharing', 'menubar=no,width=500,height=500');");
        	writer.write("void(0);");
        	writer.write("};");
        	for (Entry<String,ABMSocialShare> socshare: SocialShares.entrySet()) {
        		writer.write(socshare.getValue().BuildJavaScript());
        	}
        }           
        
        if (NeedsInput) {
        	writer.write("\nfunction runautocomplete(id, theme, stype) {");
        	writer.write("var $input = $('#' + id);");
        	writer.write("var searchtype=stype;");
        	writer.write("if ($input.hasClass('autocomplete')) {");
        	writer.write("var $array = $input.data('array');");
        	writer.write("$inputDiv = $input.closest('.input-field');");
        	writer.write("if ($array !== '') {");
        	writer.write("var $html = '<ul id=\"' + id + '-autocomp\" class=\"autocomplete-content' + theme + ' hide\">';");
        	writer.write("var $prevDiv = $inputDiv.children('#' + id + '-autocomp').remove();");
        	writer.write("for (var i = 0; i < $array.length; i++) {");
        	writer.write("if ($array[i]['path'] !== '' && $array[i]['path'] !== undefined && $array[i]['path'] !== null && $array[i]['class'] !== undefined && $array[i]['class'] !== '') {");
        	writer.write("$html += '<li data-id=\"' + $array[i]['id'] + '\" class=\"autocomplete-option\"><img src=\"' + $array[i]['path'] + '\" class=\"' + $array[i]['class'] + '\"><h6>' + $array[i]['value'] + '</h6></li>';");
        	writer.write("} else {");
        	writer.write("$html += '<li data-id=\"' + $array[i]['id'] + '\" class=\"autocomplete-option\"><h6>' + $array[i]['value'] + '</h6></li>';");
        	writer.write("}");
        	writer.write("}");
        	writer.write("$html += '</ul>';");
        	writer.write("$inputDiv.append($html);");		
        				
        	writer.write("function checkme() {");
        	writer.write("var $val = $input.val().trim();");
        	writer.write("var tmpId = $input.attr('id');");
        	writer.write("var $select = $('#' + tmpId + '-autocomp');");
        	writer.write("$select.css('width', $input.width());");
        	writer.write("if ($val != '' && !$input.is('[readonly=\"true\"]')) {");
        	writer.write("$select.children('li').addClass('hide');");
        	writer.write("$select.children('li').filter(function() {");
        	writer.write("$select.removeClass('hide');");	
        	writer.write("var check = false;");
        	writer.write("if (searchtype=='contains') {");
        	writer.write("if ($(this).text().toLowerCase().indexOf($val.toLowerCase()) >= 0) check = true;");
        	writer.write("} else {");
        	writer.write("if ($(this).text().toLowerCase().indexOf($val.toLowerCase()) == 0) check = true;");
        	writer.write("}");
        	writer.write("if (check) {");
        	writer.write("var matchStart = $(this).text().toLowerCase().indexOf(\"\" + $val.toLowerCase() + \"\"),");
        	writer.write("matchEnd = matchStart + $val.length - 1,");
        	writer.write("beforeMatch = $(this).text().slice(0, matchStart),");
        	writer.write("matchText = $(this).text().slice(matchStart, matchEnd + 1),");
        	writer.write("afterMatch = $(this).text().slice(matchEnd + 1);");
        	writer.write("$(this).html(\"<h6>\" + beforeMatch + \"<b>\" + matchText + \"</b>\" + afterMatch + \"</h6>\");");
        	writer.write("}");
        							
        	writer.write("return check ? $(this).text().toLowerCase().indexOf($val.toLowerCase()) !== -1 : false;");
        	writer.write("}).removeClass('hide');");
        	writer.write("} else {");
        	writer.write("if ($input.hasClass('autocompopen')) {");
        	writer.write("$select.children('li').each(function() {");
        	writer.write("$(this).html(\"<h6>\" + $(this).text() + \"</h6>\");");
        	writer.write("$(this).removeClass('hide');");
        	writer.write("});");
        	writer.write("$select.removeClass('hide');");
        	writer.write("} else {");
        	writer.write("$select.children('li').addClass('hide');");
        	writer.write("}");
        	writer.write("}");
        	writer.write("};");	
        	writer.write("$input.blur(function() {");
        	writer.write("var tmpId = $(this).attr('id');");
        	writer.write("var $select = $('#' + tmpId + '-autocomp');");
        	writer.write("$select.children('li').addClass('hide');");
        	writer.write("});");
        	writer.write("$input.focus(function() {");
        	writer.write("checkme();");
        	writer.write("var tmpId = $(this).attr('id');");
        	writer.write("var $select = $('#' + tmpId + '-autocomp');");
        	writer.write("var tmpFound = $select.children('li').not('.hide').length;");
        	writer.write("var $val = $input.val().trim();");
        	writer.write("if (tmpFound==1 && $val==$select.children('li').not('.hide').text()) {");            						
        	writer.write("$select.children('li').addClass('hide');");
        	writer.write("}");
        	writer.write("});");
    
        	writer.write("var touchmoved;");
        	writer.write("$input.closest('.input-field').children('ul').children('li').on('mousedown touchend', function() {");
        	writer.write("if(touchmoved != true){");
        	writer.write("$input.val($(this).text().trim());");
        	writer.write("var tmpId = $input.attr('id');");
        	writer.write("var $select = $('#' + tmpId + '-autocomp');");
        	writer.write("$select.addClass('hide');");
        	writer.write("if ($input.is('[readonly=\"true\"]')) {");
        	writer.write("$input.removeClass('validate');");
        	writer.write("$input.addClass('valid');");
        	writer.write("}");
        	writer.write("var evname = $input.attr('evname');");
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': evname + '_autocompleteclicked',");
        	writer.write("'eventparams': 'uniqueid',");
        	writer.write("'uniqueid': '' + $(this).data('id')");
        	writer.write("});");
        	writer.write("}");
        	writer.write("}).on('touchmove', function(e){");
        	writer.write("touchmoved = true;");
        	writer.write("}).on('touchstart', function(){");
        	writer.write("touchmoved = false;");
        	writer.write("});");
        	writer.write("$(document).on('keyup', $input, function(e) {"); // was keyup
        	writer.write("if ($input.is(\":focus\") == false) {");
        	writer.write("return;");
        	writer.write("}");
        
        	writer.write("checkme();");
        	writer.write("});");
        	writer.write("} else {");
        	writer.write("return false;");
        	writer.write("}");
        	writer.write("}");
        	writer.write("};\n");
        }
        
        writer.write("$(document).on('click', '#toast-container .toast', function() {");
        writer.write("$(this).fadeOut(function(){");
        writer.write("$(this).remove();");
        writer.write("b4j_raiseEvent('page_parseevent',{'eventname':'page_toastdismissed','eventparams':'toastid','toastid':$(this).children('span').first().attr('id')});");
        writer.write("});");
        writer.write("});");
        
        writer.write("var drake;");
        writer.write("var googleac={};");
        for (Entry<String,ABMInput> inpentry: Inputs.entrySet()) {
    		ABMInput inp = inpentry.getValue();

    		writer.write(inp.BuildJavaScript());    		
        }
        writer.write("$('.pac-container').initialize( function(){");
        writer.write("$('.pac-container').css('z-index', '9999');");
        writer.write("});");
        if (NeedsGoogleMap) {
   	 		writer.write("var gms={};");
   	 		writer.write("var gmsF={};");
   	 		writer.write("var gmos={};");
        }
        writer.write("var activetablerows={};");
        if (NeedsPlanner) {
        	writer.write("var planners={};");
        	writer.write("var plannerID='';");
        	writer.write("var oldActiveDay=-1;");
        }
        if (NeedsCanvas) {
        	writer.write("var cans={};");
        	writer.write("var amountPictures=" + Images.size() + ";");
        	writer.write("var loadedPictures=0;");
        }
        if (NeedsSignature) {
        	writer.write("var signatures={};");
        	writer.write("var signaturesCanvas={};");
        }
        if (NeedsFileManager) {
        	writer.write("var fileMans={};");
        }
        
        
        writer.write("autoplay = false;");
        writer.write("function updatefields() {Materialize.updateTextFields();};");
        
        writer.write("var readyTimer = setInterval(function() {");
        
        if (!ABMaterial.IsSinglePageApp) {
        	writer.write("if(document.readyState === 'complete') {");
        	writer.write("clearInterval(readyTimer);");
                    
        	writer.write("ReadyToConnect();");
        	writer.write("} ");
        } else {
        	writer.write("clearInterval(readyTimer);");            
        	writer.write("ReadyToConnect();");
        }
        writer.write("}, 100);");
        
        writer.write("$(function() {");
        writer.write("$('.abmnextsectionbutton, .abmnextsectionmenu').on('click', function(e) {");
        writer.write("e.preventDefault();");

        writer.write("if ($(this).attr('data-speed')==0) {");
        writer.write("$('html, body').scrollTop($($(this).attr('href')).offset().top - $(this).attr('data-top'));");
        writer.write("} else {");
        writer.write("$('html, body').animate({ scrollTop: $($(this).attr('href')).offset().top - $(this).attr('data-top')}, $(this).attr('data-speed'), 'linear');");
        writer.write("}");
        writer.write("});");
        writer.write("});");
        
        writer.write("function ReadyToConnect() {");
        if (!ABMaterial.IsSinglePageApp) {
        	writer.write("console.log('ready to connect the websocket');");
        	writer.write("b4j_connect(\"" + Handler + "\");");
        } else {
        	if (IsApp) {
        		writer.write("console.log('ready to connect the websocket');");
            	writer.write("b4j_connect(\"" + Handler + "\");");
        	} else {
        		writer.write("console.log('ready to update the websocket');");
        	}
        }
        writer.write("initplugins();");           
        
    	if (this.ShowGridInfo) {
    		writer.write("$('.col:not(.input-field)').each(function() {");
    		writer.write("$(this).prepend('<div class=\"showcellinfo\">' + $(this).attr('name')  + '</div>');");
    		writer.write("$(this).css('border','1px solid red');");        	
    		writer.write("});");            	
    	}        
        
        if (!NeedsAutorization && mShowLoaderType==ABMaterial.LOADER_TYPE_AUTO) {
        	writer.write("$('#isloaderwrapper').removeClass('isloading');");
        	writer.write("$('body').addClass('loaded');");
        	writer.write("$('#loader-wrapper').remove();");
        	if (NeedsCodeLabel) {
        		writer.write("$('.prettyprint').removeClass('prettyprinted');");
        		writer.write("PR.prettyPrint();");
            }
        }
        if (NeedsBackgroundVideo) {
        	writer.write("detect_autoplay(500);");
        } else {
        	if (NeedsBackgroundImage) {
        		writer.write("vidbg(document.body, [");                	
            	writer.write("], false, false, true, 0);");
        	}
        }
        
    	for (Entry<String,ABMTable> tbl: Tables.entrySet()) {
    		writer.write("activetablerows['" + tbl.getValue().ParentString + tbl.getValue().ArrayName.toLowerCase() + tbl.getValue().ID.toLowerCase() + "']='';");
    	}
        
        if (Images.size()>0) {
        	for (int nn=0;nn<Images.size();nn++) {
        		writer.write("LoadCanvasImage('" + ImageIds.get(nn) + "','" + Images.get(nn) + "');");
        	}            
        }
        
        if (NeedsCanvas) {
        	int counter=0;
        	for (Entry<String,ABMCanvas> ec: Canvases.entrySet()) {
        		ABMCanvas c = ec.getValue();
        		counter++;
        		writer.write("var theCanvas" + counter + "=document.getElementById(\"" + c.ParentString + c.ArrayName.toLowerCase() + c.ID.toLowerCase() + "\");");
        		writer.write("var context" + counter + "=theCanvas" + counter + ".getContext(\"2d\");");
        		writer.write("cans['" + c.ParentString + c.ArrayName.toLowerCase() + c.ID.toLowerCase() + "']=new ABMCanvas(theCanvas" + counter + ",context" + counter + ", '" + ABMaterial.GetColorStrMap(c.BackColor, c.BackColorIntensity) + "'," + c.ScaleToFit + ");");        		            		
        	}
        	if (Images.size()==0) {
            	writer.write("for (var can in cans) {");
                writer.write("cans[can].drawcanvas();");
                writer.write("}");
    		}
        }
        if (NeedsSignature) {
        	for (Entry<String, ABMSignaturePad> ec: signatures.entrySet()) {
        		ABMSignaturePad c = ec.getValue();
        		writer.write("signaturesCanvas['" + c.ParentString + c.ArrayName.toLowerCase() + c.ID.toLowerCase() + "']=document.getElementById(\"" + c.ParentString + c.ArrayName.toLowerCase() + c.ID.toLowerCase() + "\");");
        		writer.write("signatures['" + c.ParentString + c.ArrayName.toLowerCase() + c.ID.toLowerCase() + "']=new SignaturePad(signaturesCanvas['" + c.ParentString + c.ArrayName.toLowerCase() + c.ID.toLowerCase() + "']);");
        		writer.write("signatures['" + c.ParentString + c.ArrayName.toLowerCase() + c.ID.toLowerCase() + "'].penColor=\"" + ABMaterial.GetColorStrMap(c.PenColor, c.PenColorIntensity) + "\";");
        		writer.write("signatures['" + c.ParentString + c.ArrayName.toLowerCase() + c.ID.toLowerCase() + "'].backgroundColor=\"" + ABMaterial.GetColorStrMap(c.ClearColor, c.ClearColorIntensity) + "\";");
        		writer.write("signatures['" + c.ParentString + c.ArrayName.toLowerCase() + c.ID.toLowerCase() + "'].clear();");
        	}
        }
        
        if (NeedsGoogleMap) {
   	 		for (Entry<String, ABMGoogleMap> em: gmaps.entrySet()) {
   	 		  ABMGoogleMap m = em.getValue();
   	 		  if (m.MapType.equals("panorama")) {
   	 			writer.write("gms['" + m.ParentString + m.ArrayName.toLowerCase() + m.ID.toLowerCase() + "']=new GMaps.createPanorama({");
   	 			writer.write("el:'#" + m.ParentString + m.ArrayName.toLowerCase() + m.ID.toLowerCase() + "',");
   	 			writer.write("lat:" + m.Latitude + ",");
   	 		    writer.write("lng:" + m.Longitude + ","); 
   	 		    writer.write("pov:{zoom:" + m.Zoom + ",heading:" + m.Heading + ",pitch:" + m.Pitch + "},");       	 		   
   	 		    writer.write("idle: function(e){gmready('" + m.ParentString + m.ArrayName.toLowerCase() + m.ID.toLowerCase() + "');},");
   	 		    writer.write("});");
   	 	      } else {       	 			
   	 			writer.write("gms['" + m.ParentString + m.ArrayName.toLowerCase() + m.ID.toLowerCase() + "']=new GMaps({");
   	 			writer.write("el:'#" + m.ParentString + m.ArrayName.toLowerCase() + m.ID.toLowerCase() + "',");
   	 			writer.write("lat:" + m.Latitude + ",");
   	 		    writer.write("lng:" + m.Longitude + ",");
   	 		    writer.write("mapTypeId:'" + m.MapType + "',");
   	 		    writer.write("zoom:" + m.Zoom + ",");
   	 		    writer.write("click:function(e){gmclickarray('" + m.ParentString + m.ArrayName.toLowerCase() + m.ID.toLowerCase() + "', e.latLng);  },");
   	 		    writer.write("idle:function(e){gmready('" + m.ParentString + m.ArrayName.toLowerCase() + m.ID.toLowerCase() + "');  },");
   	 		    if (!m.Draggable) {
   	 		    	writer.write("draggable:false,");
   	 		    }
   	 		    if (m.HasMapTypeControl) {
   	 		    	writer.write("mapTypeControlOptions: {");
   	 		        writer.write("mapTypeIds:[");
   	 		    	for (int index=0;index<m.mapTypes.size();index++) {
   	 		    		if (index<m.mapTypes.size()-1) {
   	 		    			writer.write("'" + m.mapTypes.get(index) + "',");
   	 		    		} else {
   	 		    		writer.write("'" + m.mapTypes.get(index) + "'");
   	 		    		}
   	 		    	}
       	 			writer.write("]");
       	 			writer.write("},");
       	 			
   	 		    }
   	 		    if (m.HasZoomControl) {
   	 		    	writer.write("zoomControl:true,");
   	 		    	writer.write("zoomControlOpt:{");
   	 		        writer.write("style:'SMALL',");
   	 		        writer.write("position:'TOP_LEFT'");
   	 		        writer.write("},");
   	 		    } else {
   	 		    	writer.write("zoomControl:false,");
   	 		    }
   	 		  
   	 			if (m.HasStreetViewControl) {
   	 				writer.write("streetViewControl:true,");
   	 			} else {
   	 				writer.write("streetViewControl:false,");
   	 			}
   	 			if (m.HasMapTypeControl) {
   	 				writer.write("mapTypeControl:true,");
   	 			} else {
   	 				writer.write("mapTypeControl:false,");
   	 			}
   	 			
   	 			writer.write("});");
   	 	      }
   	 		}
   	 	}
        if (NeedsCalendar) {        	
        	writer.write("initcalendars();");
        	
        }
        if (NeedsMask) {
        	if (ABMaterial.InputMaskNewVersion==false) {
        		writer.write("$(\":input\").inputmask();");
        		writer.write("$('[data-abminputmask=\"1\"]').inputmask();");
        	}
        }
        
        writer.write("};"); //ALAIN 161126
       
        writer.write("$('.toggle-fullscreen').click(function(){");
        writer.write("toggleFullScreen();");
        writer.write("});");
        
       	writer.write("function ShowPage() {");
       	if (mShowLoaderType==ABMaterial.LOADER_TYPE_AUTO) {
       		writer.write("$('#isloaderwrapper').removeClass('isloading');");
       		writer.write("$('body').addClass('loaded');");
       		writer.write("$('#loader-wrapper').remove();");
       		if (NeedsCodeLabel) {
           		writer.write("$('.prettyprint').removeClass('prettyprinted');");
           		writer.write("PR.prettyPrint();");
            }
       	}
        	
       	if (NeedsEditor) {
       		for (Entry<String,ABMEditor> ed: Editors.entrySet()) {
           		writer.write(ed.getValue().BuildJavaScript());
           	}
        }
        	
       	writer.write("}");
        
        writer.write("$('.materialboxed').materialbox();");
                    
        if (NeedsCanvas) {
        	writer.write("var images={};");
        	writer.write("function LoadCanvasImage(imageid, image){");
        	writer.write("images[imageid]=new Image();");
        	writer.write("images[imageid].onload=function(){");
        	writer.write("if (++loadedPictures>=amountPictures) {");               
        	writer.write("for (var can in cans){");
        	writer.write("cans[can].drawcanvas();");
        	writer.write("}");
        	writer.write("}");
        	writer.write("};");
        	writer.write("images[imageid].src=image;");
        	writer.write("}");
        	
        	writer.write("function AfterLoadCanvasImage(imageid, image){");
        	writer.write("images[imageid]=new Image();");
        	writer.write("images[imageid].onload=function(){");
        	writer.write("for (var can in cans){");
        	writer.write("cans[can].drawcanvas();");
        	writer.write("}");
        	writer.write("};");
        	writer.write("images[imageid].src=image;");
        	writer.write("}");
                    
        	writer.write("function updateinnercanvas(params){");
        	writer.write("var can=params[0];");
        	writer.write("params.shift();");
        	writer.write("cans[can].updatecanvas(params,true);");
        	writer.write("}");
        
        	writer.write("function initinnercanvas(params){");
        	writer.write("var can=params[0];");
        	writer.write("params.shift();");
        	writer.write("cans[can].updatecanvas(params,false);");
        	writer.write("}");
        
        	writer.write("function canvasposition(canvasid, id){");
        	writer.write("return cans[canvasid].getposition(id);");
        	writer.write("}");
        	
        	writer.write("function getDataURLCanvas(id, fileName, handler, ext) {");
        	
        	writer.write("var Pic = cans[id].theCanvas.toDataURL('image/' + ext);");
        	writer.write("Pic = Pic.replace('data:image/' + ext + ';base64,', '');");
        	writer.write("var boundary = '------------------------------';");
        	writer.write("var disposition = 'form-data; name=\"imageFromCanvas\"; filename=\"' + fileName + '\"';");
        	writer.write("var xhr = new XMLHttpRequest();");
        	writer.write("xhr.onreadystatechange = function() {");
        	writer.write("if (xhr.readyState == 4) {");
        	writer.write("if ((xhr.status >= 200 && xhr.status <= 200) || xhr.status == 304) {");
        	writer.write("if (xhr.responseText != '') {");
        	writer.write("console.log(xhr.responseText);");
        	writer.write("}");
        	writer.write("}");
        	writer.write("};");
        	writer.write("}\n"); // \n is NEEDED!!!!!
        	
        	writer.write("xhr.open('POST', handler);");
        	writer.write("xhr.setRequestHeader('Content-Type', 'multipart/form-data; boundary=' + boundary);");
        	writer.write("xhr.sendAsBinary(['--' + boundary, 'Content-Disposition: ' + disposition, 'Content-Type: ' + 'image/' + ext, '', atob(Pic), '--' + boundary + '--'].join('\\r\\n'));");
        	writer.write("}");

        	writer.write("if (XMLHttpRequest.prototype.sendAsBinary === undefined) {");
        	writer.write("XMLHttpRequest.prototype.sendAsBinary = function(string) {");
        	writer.write("var bytes = Array.prototype.map.call(string, function(c) {");
        	writer.write("return c.charCodeAt(0) & 0xff;");
        	writer.write("});");
        	writer.write("this.send(new Uint8Array(bytes).buffer);");
        	writer.write("};");
        	writer.write("}");
        } else {
        	writer.write("function LoadCanvasImage(imageid, image){}\n");
        }
        
        if (NeedsUpload) {
        	if (uploads.size()==0) {
        		writer.write("function runupload() {");            	
            	writer.write("};");
        	} else {
        		for (Entry<String,ABMUpload> eup: uploads.entrySet()) {
        			ABMUpload up = eup.getValue();
        			WriteJSUpload(writer, up.Theme, up.ParentString + up.ArrayName.toLowerCase() + up.ID.toLowerCase());
        		}
        	}
        }
        
        writer.write("function prntgl(id,op) {");
        writer.write("if ($('#' + id).hasClass('no-print')) {");
        writer.write("$('#' + id).removeClass('no-print');");
        writer.write("$('#' + id).addClass('only-print');");
        writer.write("$('#' + id).css({'opacity': 1.0 });");
        writer.write("$('#' + id).find('.barcode').css({'opacity' : 1.0});");
        writer.write("$('#' + id).find('.prntA').removeClass('prntNA');");
        writer.write("} else {");
        writer.write("$('#' + id).removeClass('only-print');");
        writer.write("$('#' + id).addClass('no-print');");
        writer.write("$('#' + id).css({'opacity': op });");
        writer.write("$('#' + id).find('.barcode').css({'opacity' : 0.0});");
        writer.write("$('#' + id).find('.prntA').addClass('prntNA');");
        writer.write("}");
        writer.write("}");
                
        writer.write("function togglerep(el, forceIsOpen) {");
        writer.write("var row = $(el).parent().parent();");
        writer.write("var isOpen;");
        writer.write("if (forceIsOpen == undefined) {");
        writer.write("isOpen = ($(el).attr('data-r0') == '1');");
        writer.write("} else {");
        writer.write("isOpen = forceIsOpen");
        writer.write("}");
        writer.write("togglerepsetcolumnsto(row, !isOpen);");
        writer.write("var d1 = '',");
        writer.write("d2 = '',");
        writer.write("d3 = '',");
        writer.write("d4 = '',");
        writer.write("d5 = '';");
        writer.write("var stop = false;");
        writer.write("var inners = false;");
        writer.write("d1 = row.attr('data-r1');");
        writer.write("d1 = '[data-r1=\"' + d1 + '\"]';");
        writer.write("d2 = row.attr('data-r2');");
        writer.write("if (d2 != undefined) {");
        writer.write("d2 = '[data-r2=\"' + d2 + '\"]';");
        writer.write("} else {");
        writer.write("d2 = '[data-r2!=\"\"][data-r2]';");
        writer.write("inners = true;");
        writer.write("stop = true;");
        writer.write("}");
        writer.write("if (stop == false) {");
        writer.write("d3 = row.attr('data-r3');");
        writer.write("if (d3 != undefined) {");
        writer.write("d3 = '[data-r3=\"' + d3 + '\"]';");
        writer.write("} else {");
        writer.write("d3 = '[data-r3!=\"\"][data-r3]';");
        writer.write("stop = true;");
        writer.write("}");
        writer.write("}");
        writer.write("if (stop == false) {");
        writer.write("d4 = row.attr('data-r4');");
        writer.write("if (d4 != undefined) {");
        writer.write("d4 = '[data-r4=\"' + d4 + '\"]';");
        writer.write("} else {");
        writer.write("d4 = '[data-r4!=\"\"][data-r4]';");
        writer.write("stop = true;");
        writer.write("}");
        writer.write("}");
        writer.write("if (stop == false) {");
        writer.write("d5 = row.attr('data-r5');");
        writer.write("if (d5 != undefined) {");
        writer.write("d5 = '[data-r5=\"' + d5 + '\"]';");
        writer.write("} else {");
        writer.write("d5 = '[data-r5!=\"\"][data-r5]';");
        writer.write("}");
        writer.write("}");
        writer.write("var chk = 'div.reportrow' + d1 + d2 + d3 + d4 + d5;");
        writer.write("var row =$(el).parent().parent();");
        writer.write("var linetype = row.attr('data-reptype');");
        writer.write("var states;");
        writer.write("var statesSOld;");
        writer.write("var statesSNew;");
        writer.write("if (!isOpen) {");
        writer.write("states = currABMrep[linetype + '_O'];");
        writer.write("statesSNew = currABMrep[linetype + '_ROS'];");
        writer.write("statesSOld = currABMrep[linetype + '_RCS'];");
        writer.write("} else {");
        writer.write("states = currABMrep[linetype + '_C'];");
        writer.write("statesSNew = currABMrep[linetype + '_RCS'];");
        writer.write("statesSOld = currABMrep[linetype + '_ROS'];");
        writer.write("}");
        writer.write("if (isOpen) {");
        writer.write("$(chk).parent().addClass('hide no-print');");
        writer.write("$(el).attr('data-r0', '0');");
        writer.write("$(el).removeClass('fa-minus-cirlce').addClass('fa-plus-circle');");
        writer.write("if (inners) {$('.repinner' + d1 + '[data-r2=\"Z1\"]').addClass('hide no-print').addClass('only-print')};");
        writer.write("} else {");
        writer.write("$(chk).parent().removeClass('hide no-print');");
        writer.write("$(el).attr('data-r0', '1');");
        writer.write("$(el).removeClass('fa-plus-circle').addClass('fa-minus-circle');");
        writer.write("if (inners) {$('.repinner' + d1 + '[data-r2=\"Z1\"]').removeClass('no-print').addClass('hide only-print')};");
        writer.write("}");
        writer.write("row.removeClass(statesSOld).addClass(statesSNew);");
        writer.write("togglerepsetallchildrento(chk + '.repcol', !isOpen);");	
        writer.write("};");

        writer.write("function togglerepsetallchildrento(el, open) {");
        writer.write("$(el).each(function() {");
        writer.write("togglerepsetcolumnsto(this, open);");			
        writer.write("});");
        writer.write("};");

        writer.write("function togglerepsetcolumnsto(el, open) {");
        writer.write("var linetype = $(el).attr('data-reptype');");
        writer.write("var states;");
        writer.write("var statesSOld;");
        writer.write("var statesSNew;");
        writer.write("if (open) {");
        writer.write("states = currABMrep[linetype + '_O'];");
        writer.write("statesSNew = currABMrep[linetype + '_OS'];");
        writer.write("statesSOld = currABMrep[linetype + '_CS'];");
        writer.write("} else {");
        writer.write("states = currABMrep[linetype + '_C'];");
        writer.write("statesSNew = currABMrep[linetype + '_CS'];");
        writer.write("statesSOld = currABMrep[linetype + '_OS'];");
        writer.write("}");
        writer.write("$(el).children('div.reportcol').each(function(index) {");
        writer.write("if ($(this).text()!='\\xA0') {");
        writer.write("if (states[index]) {");
        writer.write("$(this).removeClass('hide no-print');");
        writer.write("} else {");
        writer.write("$(this).addClass('hide no-print');");
        writer.write("}");
        writer.write("} else {");
        writer.write("$(this).removeClass('hide no-print');");
        writer.write("}");
        
        writer.write("$(this).removeClass(statesSOld[index]).addClass(statesSNew[index]);");
        writer.write("});");
        writer.write("if (open) {");
        writer.write("$(el).find('.repicon-i').each(function() {");
        writer.write("$(this).attr('data-r0', '1');");
        writer.write("$(this).removeClass('fa-plus-circle').addClass('fa-minus-circle');");
        writer.write("});");
        writer.write("} else {");
        writer.write("$(el).find('.repicon-i').each(function() {");
        writer.write("$(this).attr('data-r0', '0');");
        writer.write("$(this).removeClass('fa-minus-cirlce').addClass('fa-plus-circle');");
        writer.write("});");
        writer.write("}");
        writer.write("};");
                
        writer.write("function prntglall(id,cl,op) {");
        writer.write("var dopr = $('#' + id).hasClass('prntglall');");
        writer.write("if (dopr) {");
        writer.write("$('.' + cl).removeClass('no-print');");
        writer.write("$('.' + cl).addClass('only-print');");
        writer.write("$('.' + cl).css({'opacity': 1.0 });");
        writer.write("$('#' + id).removeClass('prntglall');");
        writer.write("$('#' + id + '-tglicon').css({'opacity': 1.0 });");
        writer.write("$('.' + cl).find('.barcode').css({'opacity' : 1.0});");
        writer.write("$('.' + cl).find('.prntA').removeClass('prntNA');");
        writer.write("} else {");
        writer.write("$('.' + cl).removeClass('only-print');");
        writer.write("$('.' + cl).addClass('no-print');");
        writer.write("$('.' + cl).css({'opacity': op });");
        writer.write("$('#' + id).addClass('prntglall');");
        writer.write("$('#' + id + '-tglicon').css({'opacity': op });");
        writer.write("$('.' + cl).find('.barcode').css({'opacity' : 0.0});");
        writer.write("$('.' + cl).find('.prntA').addClass('prntNA');");
        writer.write("}");
        writer.write("}");
        
        writer.write("function prntglallsetto(id,cl,op,dopr) {");
        writer.write("if (dopr) {");
        writer.write("$('.' + cl).removeClass('no-print');");
        writer.write("$('.' + cl).addClass('only-print');");
        writer.write("$('.' + cl).css({'opacity': 1.0 });");
        writer.write("$('#' + id).addClass('prntglall');");
        writer.write("$('#' + id + '-tglicon').css({'opacity': 1.0 });");
        writer.write("$('.' + cl).find('.barcode').css({'opacity' : 1.0});");
        writer.write("$('.' + cl).find('.prntA').removeClass('prntNA');");
        writer.write("} else {");
        writer.write("$('.' + cl).removeClass('only-print');");
        writer.write("$('.' + cl).addClass('no-print');");
        writer.write("$('.' + cl).css({'opacity': op });");
        writer.write("$('#' + id).removeClass('prntglall');");
        writer.write("$('#' + id + '-tglicon').css({'opacity': op });");
        writer.write("$('.' + cl).find('.barcode').css({'opacity' : 0.0});");
        writer.write("$('.' + cl).find('.prntA').addClass('prntNA');");
        writer.write("}");
        writer.write("}");
        
        writer.write("function prntglgetselected(cl) {");
        writer.write("var ret='';");
        writer.write("$('.' + cl + '.only-print').each(function(index) {");
        writer.write("ret=ret+$(this).attr('data-b4jsextra')+',';");
        writer.write("});");
        writer.write("if (ret.length>0) {");
        writer.write("return ret.substring(0,ret.length - 1);");
        writer.write("} else {");
        writer.write("return '';");
        writer.write("}");
        writer.write("}");
        
        writer.write("function prntglgetstats(cl) {");
        writer.write("return $('.' + cl + '.only-print').length + ';' + $('.' + cl + '.no-print').length");
        writer.write("}");
        
        writer.write("function prntglsetselected(cl,ret,op) {");
        writer.write("$('.' + cl).removeClass('only-print');");
        writer.write("$('.' + cl).addClass('no-print');");
        writer.write("$('.' + cl).css({'opacity': op });"); 
        writer.write("$('.' + cl).find('.barcode').css({'opacity' : 0.0});");
        writer.write("$('.' + cl).find('.prntA').addClass('prntNA');");
        writer.write("var ids = ret.split(',');");
        writer.write("var len = ids.length;");
        writer.write("for (var i=0;i<len;i++) {");
        writer.write("var id=ids[i];");
        writer.write("var $id=$('.' + cl + '[data-b4jsextra=\"' + id + '\"]');");
        writer.write("$id.removeClass('no-print');");
        writer.write("$id.addClass('only-print');");
        writer.write("$id.css({'opacity': 1.0 });");
        writer.write("$id.find('.barcode').css({'opacity' : 1.0});");
        writer.write("$id.find('.prntA').removeClass('prntNA');");
        writer.write("}");
        writer.write("}");
        
        writer.write("function rapsetzoom(el, zoom) {");
        writer.write("var z;");
        writer.write("for (z=50;z<=200;z+=5) {");
        writer.write("el.removeClass('zoom' + z);");
        writer.write("}");
        writer.write("el.addClass(zoom);");
        writer.write("}");
               
        writer.write("function SaveNavigationBarPosition() {");
        if (NeedsSimpleBar) {
        	writer.write("return $('.simplebar-scroll-content').scrollTop();");
        } else {
        	writer.write("return $('#nav-mobile').scrollTop();");
        }
        writer.write("}");
        
        writer.write("function RestoreNavigationBarPosition(value) {");
        if (NeedsSimpleBar) {
        	writer.write("$('.simplebar-scroll-content').scrollTop(value);");
        } else {
        	writer.write("$('#nav-mobile').scrollTop(value);");
        }

        writer.write("}");
        
        writer.write("function SaveTablePosition(id) {");
        writer.write("return $('#' + id).scrollTop();");
        writer.write("}");
        
        writer.write("function RestoreTablePosition(id, value) {");
        writer.write("$('#' + id).scrollTop(value);");
        writer.write("}");
        
        if (NeedsSignature) {
        	            	
        	writer.write("function clearSignaturePad(id) {");
        	writer.write("signatures[id].clear()");
        	writer.write("}");            	
        	
        	writer.write("function getDataURLSignaturePad(id, fileName, handler, ext) {");
        	writer.write("if (signatures[id].isEmpty()) {");
        	writer.write("return '';");
        	writer.write("} else {");
        	writer.write("var Pic = signatures[id].toDataURL('image/' + ext);");
        	writer.write("Pic = Pic.replace('data:image/' + ext + ';base64,', '');");
        	writer.write("var boundary = '------------------------------';");
        	writer.write("var disposition = 'form-data; name=\"imageFromCanvas\"; filename=\"' + fileName + '\"';");
        	writer.write("var xhr = new XMLHttpRequest();");
        	writer.write("xhr.onreadystatechange = function() {");
        	writer.write("if (xhr.readyState == 4) {");
        	writer.write("if ((xhr.status >= 200 && xhr.status <= 200) || xhr.status == 304) {");
        	writer.write("if (xhr.responseText != '') {");
        	writer.write("console.log(xhr.responseText);");
        	writer.write("}");
        	writer.write("}");
        	writer.write("};");
        	writer.write("}\n"); // \n is NEEDED!!!!!

        	writer.write("xhr.open('POST', handler);");
        	writer.write("xhr.setRequestHeader('Content-Type', 'multipart/form-data; boundary=' + boundary);");
        	writer.write("xhr.sendAsBinary(['--' + boundary, 'Content-Disposition: ' + disposition, 'Content-Type: ' + 'image/' + ext, '', atob(Pic), '--' + boundary + '--'].join('\\r\\n'));");
        	writer.write("}");
        	writer.write("}");
        	writer.write("if (XMLHttpRequest.prototype.sendAsBinary === undefined) {");
        	writer.write("XMLHttpRequest.prototype.sendAsBinary = function(string) {");
        	writer.write("var bytes = Array.prototype.map.call(string, function(c) {");
        	writer.write("return c.charCodeAt(0) & 0xff;");
        	writer.write("});");
        	writer.write("this.send(new Uint8Array(bytes).buffer);");
        	writer.write("};");
        	writer.write("}");

        }
        
        writer.write("function getplatform() {");
                    
        writer.write("var ret=platform.manufacturer+';ABM;'+platform.name+';ABM;'+platform.prerelease+';ABM;'+platform.product+';ABM;'+platform.version+';ABM;'+platform.os.architecture+';ABM;'+platform.os.family+';ABM;'+platform.os.version+';ABM;'+platform.description+';ABM;'+platform.ua+';ABM;'+window.devicePixelRatio;");
        writer.write("return ret;");
        writer.write("}\n");
        
        if (NeedsBackgroundVideo || NeedsBackgroundImage) {
        	writer.write("function vidbg(element, sources, overlay, hasbackgroundvideo, muted, volume) {");
        	writer.write("var container, item, html = [], index = sources.length;");
        	writer.write("if ((!document.createElement('video').canPlayType)||!autoplay) {");
        	writer.write("if (overlay) {");
        	writer.write("html.push('<div class=\"vidbg-overlay\"></div>');");
        	writer.write("}");
        	writer.write("container = document.createElement('div');");
        	writer.write("container.setAttribute('class', 'vidbg');");
        	writer.write("container.innerHTML = html.join('');");
        	writer.write("element.appendChild(container);");
        	writer.write("return;");
        	writer.write("}");
        	writer.write("if (hasbackgroundvideo) {");
        	writer.write("if (muted) {");
        	writer.write("html.push('<video autoplay=\"true\" loop=\"loop\" muted=\"muted\" volume=\"0\">');");
        	writer.write("} else {");
        	writer.write("html.push('<video autoplay=\"true\" loop=\"loop\" volume=\"' + volume + '\">');");
        	writer.write("}");
        	writer.write("sources.reverse();");
        	writer.write("while (index--) {");
        	writer.write("item = sources[index];");
        	writer.write("html.push('<source src=\"', item.src, '\" type=\"video/', item.type, '\" />');");
        	writer.write("}");
        	writer.write("html.push('</video>');");
        	writer.write("if (overlay) {");
        	writer.write("html.push('<div class=\"vidbg-overlay\"></div>');");
        	writer.write("}");
        	writer.write("}");
        	writer.write("container = document.createElement('div');");
        	writer.write("container.setAttribute('class', 'vidbg');");
        	writer.write("container.innerHTML = html.join('');");
        	writer.write("element.appendChild(container);");
        	writer.write("}");
        	writer.write("function detect_autoplay(A){var B=document.createElement('video'),E=document.createElement('source');E.src='data:video/mp4;base64,AAAAFGZ0eXBNU05WAAACAE1TTlYAAAOUbW9vdgAAAGxtdmhkAAAAAM9ghv7PYIb+AAACWAAACu8AAQAAAQAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAnh0cmFrAAAAXHRraGQAAAAHz2CG/s9ghv4AAAABAAAAAAAACu8AAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAABAAAAAAFAAAAA4AAAAAAHgbWRpYQAAACBtZGhkAAAAAM9ghv7PYIb+AAALuAAANq8AAAAAAAAAIWhkbHIAAAAAbWhscnZpZGVBVlMgAAAAAAABAB4AAAABl21pbmYAAAAUdm1oZAAAAAAAAAAAAAAAAAAAACRkaW5mAAAAHGRyZWYAAAAAAAAAAQAAAAx1cmwgAAAAAQAAAVdzdGJsAAAAp3N0c2QAAAAAAAAAAQAAAJdhdmMxAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAFAAOABIAAAASAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAGP//AAAAEmNvbHJuY2xjAAEAAQABAAAAL2F2Y0MBTUAz/+EAGGdNQDOadCk/LgIgAAADACAAAAMA0eMGVAEABGjuPIAAAAAYc3R0cwAAAAAAAAABAAAADgAAA+gAAAAUc3RzcwAAAAAAAAABAAAAAQAAABxzdHNjAAAAAAAAAAEAAAABAAAADgAAAAEAAABMc3RzegAAAAAAAAAAAAAADgAAAE8AAAAOAAAADQAAAA0AAAANAAAADQAAAA0AAAANAAAADQAAAA0AAAANAAAADQAAAA4AAAAOAAAAFHN0Y28AAAAAAAAAAQAAA7AAAAA0dXVpZFVTTVQh0k/Ou4hpXPrJx0AAAAAcTVREVAABABIAAAAKVcQAAAAAAAEAAAAAAAAAqHV1aWRVU01UIdJPzruIaVz6ycdAAAAAkE1URFQABAAMAAAAC1XEAAACHAAeAAAABBXHAAEAQQBWAFMAIABNAGUAZABpAGEAAAAqAAAAASoOAAEAZABlAHQAZQBjAHQAXwBhAHUAdABvAHAAbABhAHkAAAAyAAAAA1XEAAEAMgAwADAANQBtAGUALwAwADcALwAwADYAMAA2ACAAMwA6ADUAOgAwAAABA21kYXQAAAAYZ01AM5p0KT8uAiAAAAMAIAAAAwDR4wZUAAAABGjuPIAAAAAnZYiAIAAR//eBLT+oL1eA2Nlb/edvwWZflzEVLlhlXtJvSAEGRA3ZAAAACkGaAQCyJ/8AFBAAAAAJQZoCATP/AOmBAAAACUGaAwGz/wDpgAAAAAlBmgQCM/8A6YEAAAAJQZoFArP/AOmBAAAACUGaBgMz/wDpgQAAAAlBmgcDs/8A6YEAAAAJQZoIBDP/AOmAAAAACUGaCQSz/wDpgAAAAAlBmgoFM/8A6YEAAAAJQZoLBbP/AOmAAAAACkGaDAYyJ/8AFBAAAAAKQZoNBrIv/4cMeQ==';var Q=document.createElement('source');Q.src='data:video/webm;base64,GkXfo49CgoR3ZWJtQoeBAUKFgQEYU4BnAQAAAAAAF60RTZt0vE27jFOrhBVJqWZTrIIQA027jFOrhBZUrmtTrIIQbE27jFOrhBFNm3RTrIIXmU27jFOrhBxTu2tTrIIWs+xPvwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFUmpZuQq17GDD0JATYCjbGliZWJtbCB2MC43LjcgKyBsaWJtYXRyb3NrYSB2MC44LjFXQY9BVlNNYXRyb3NrYUZpbGVEiYRFnEAARGGIBc2Lz1QNtgBzpJCy3XZ0KNuKNZS4+fDpFxzUFlSua9iu1teBAXPFhL4G+bmDgQG5gQGIgQFVqoEAnIEAbeeBASMxT4Q/gAAAVe6BAIaFVl9WUDiqgQEj44OEE95DVSK1nIN1bmTgkbCBULqBPJqBAFSwgVBUuoE87EQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB9DtnVB4eeBAKC4obaBAAAAkAMAnQEqUAA8AABHCIWFiIWEiAICAAamYnoOC6cfJa8f5Zvda4D+/7YOf//nNefQYACgnKGWgQFNANEBAAEQEAAYABhYL/QACIhgAPuC/rOgnKGWgQKbANEBAAEQEAAYABhYL/QACIhgAPuC/rKgnKGWgQPoANEBAAEQEAAYABhYL/QACIhgAPuC/rOgnKGWgQU1ANEBAAEQEAAYABhYL/QACIhgAPuC/rOgnKGWgQaDANEBAAEQEAAYABhYL/QACIhgAPuC/rKgnKGWgQfQANEBAAEQEAAYABhYL/QACIhgAPuC/rOgnKGWgQkdANEBAAEQEBRgAGFgv9AAIiGAAPuC/rOgnKGWgQprANEBAAEQEAAYABhYL/QACIhgAPuC/rKgnKGWgQu4ANEBAAEQEAAYABhYL/QACIhgAPuC/rOgnKGWgQ0FANEBAAEQEAAYABhYL/QACIhgAPuC/rOgnKGWgQ5TANEBAAEQEAAYABhYL/QACIhgAPuC/rKgnKGWgQ+gANEBAAEQEAAYABhYL/QACIhgAPuC/rOgnKGWgRDtANEBAAEQEAAYABhYL/QACIhgAPuC/rOgnKGWgRI7ANEBAAEQEAAYABhYL/QACIhgAPuC/rIcU7trQOC7jLOBALeH94EB8YIUzLuNs4IBTbeH94EB8YIUzLuNs4ICm7eH94EB8YIUzLuNs4ID6LeH94EB8YIUzLuNs4IFNbeH94EB8YIUzLuNs4IGg7eH94EB8YIUzLuNs4IH0LeH94EB8YIUzLuNs4IJHbeH94EB8YIUzLuNs4IKa7eH94EB8YIUzLuNs4ILuLeH94EB8YIUzLuNs4INBbeH94EB8YIUzLuNs4IOU7eH94EB8YIUzLuNs4IPoLeH94EB8YIUzLuNs4IQ7beH94EB8YIUzLuNs4ISO7eH94EB8YIUzBFNm3SPTbuMU6uEH0O2dVOsghTM',B.appendChild(Q),B.appendChild(E),B.id='base64_test_video',B.autoplay=!0,B.style.position='fixed',B.style.left='10000px',document.getElementsByTagName('body')[0].appendChild(B);var e=document.getElementById('base64_test_video');setTimeout(function(){e.paused||(autoplay=!0),document.getElementsByTagName('body')[0].removeChild(B);");
        	writer.write("vidbg(document.body, [");
        	if (!BackgroundVideoOgv.equals("")) {
        		writer.write("{src: '" + BackgroundVideoOgv + "', type: 'ogg'},");
        	}
        	if (!BackgroundVideoWebm.equals("")) {
        		writer.write("{src: '" + BackgroundVideoWebm + "', type: 'webm'},");
        	}
        	if (!BackgroundVideoMp4.equals("")) {
        		writer.write("{src: '" + BackgroundVideoMp4 + "', type: 'mp4'},");
        	}          	
        	
        	writer.write("], " + this.BackgroundVideoHasOverlay + ", true, " + this.BackgroundVideoMute + ", " + this.BackgroundVideoVolume + ");");
        
        	writer.write("},A)}");
        }        
       
    	if (ShowGridInfo) {
    		writer.write("function ShowGridInfo(id) {");
    		writer.write("$('#' + id + ' .col:not(.input-field)').each(function() {");
    		writer.write("$(this).prepend('<div class=\"showcellinfo\">' + $(this).attr('name')  + '</div>');");
    		writer.write("$(this).css('border','1px solid red');");
    		writer.write("});");
    		writer.write("};");
    	}
                  
        writer.write("function GetBrowserTimeZone() {");
        writer.write("var tz = jstz.determine();");
        writer.write("return tz.name();"); 
        writer.write("};");
        
        writer.write("function PauseApp() {");
        writer.write("$('body').removeClass('loaded');");

        String LoaderWrapper="";
    	switch (LoaderType) {
    		case LOADERANIM_DEFAULT:
    			LoaderWrapper = "<div id=\"loader\"></div><div class=\"loader-section section-left\"></div><div class=\"loader-section section-right\"></div>";
    			break;
    		case LOADERANIM_JUMPINGBALL:
    			LoaderWrapper = "<div class=\"L1wraptemp\"><div class=\"L1loadingtemp\"><div class=\"L1bounceballtemp\"></div><div id=\"L1textTemp\" class=\"L1texttemp\">" + BuildText(L1TextTemp) + "</div></div></div>";
    			break;
    		case LOADERANIM_JUGGLINGBALLS:
    			LoaderWrapper = "<div class=\"L2circletemp\"></div><div class=\"L2circletemp\"></div><div class=\"L2circletemp\"></div>";
    			break;
    		case LOADERANIM_HEARTBEAT:
    			LoaderWrapper = "<div class=\"L3containertemp\"><svg class=\"L3parttemp\" x=\"0px\" y=\"0px\" viewBox=\"0 0 256 256\" style=\"enable-background:new 0 0 256 256;\" xml:space=\"preserve\"><path class=\"L3svgpathtemp L3playloadtemp\" d=\"M189.5,140.5c-6.6,29.1-32.6,50.9-63.7,50.9c-36.1,0-65.3-29.3-65.3-65.3 c0,0,17,0,23.5,0c10.4,0,6.6-45.9,11-46c5.2-0.1,3.6,94.8,7.4,94.8c4.1,0,4.1-92.9,8.2-92.9c4.1,0,4.1,83,8.1,83 	c4.1,0,4.1-73.6,8.1-73.6c4.1,0,4.1,63.9,8.1,63.9c4.1,0,4.1-53.9,8.1-53.9c4.1,0,4.1,44.1,8.2,44.1c4.1,0,3.1-34.5,7.2-34.5 c4.1,0,3.1,24.6,7.2,24.6c4.1,0,2.5-14.5,5.2-14.5c2.2,0,0.8,5.1,4.2,4.9c0.4,0,13.1,0,13.1,0c0-34.4-27.9-62.3-62.3-62.3 c-27.4,0-50.7,17.7-59,42.3\" /><path class=\"L3svgbgtemp\"  d=\"M61,126c0,0,16.4,0,23,0c10.4,0,6.6-45.9,11-46c5.2-0.1,3.6,94.8,7.4,94.8c4.1,0,4.1-92.9,8.2-92.9 c4.1,0,4.1,83,8.1,83c4.1,0,4.1-73.6,8.1-73.6c4.1,0,4.1,63.9,8.1,63.9c4.1,0,4.1-53.9,8.1-53.9c4.1,0,4.1,44.1,8.2,44.1 	c4.1,0,3.1-34.5,7.2-34.5c4.1,0,3.1,24.6,7.2,24.6c4.1,0,2.5-14.5,5.2-14.5c2.2,0,0.8,5.1,4.2,4.9c0.4,0,22.5,0,23,0\" /></svg></div>";
    			break;	
    		case LOADERANIM_DEVICESWITCH:
    			LoaderWrapper = "<div class=\"L4loadertemp\"><div class=\"L4dottemp\"></div><div class=\"L4dottemp\"></div><div class=\"L4dottemp\"></div></div>";
    			break;
    		case LOADERANIM_METALGEARSOLID:
    			LoaderWrapper = "<div class=\"L5loadertemp\"><div class=\"L5boxtemp\"></div><div class=\"L5boxtemp\"></div><div class=\"L5boxtemp\"></div><div class=\"L5boxtemp\"></div></div>";
    			break;
    		case LOADERANIM_ROTATINGBOXES:
    			LoaderWrapper = "<div class=\"L6thingtemp\"><div class=\"L6beamtemp L6r1temp L6rtemp\"></div><div class=\"L6beamtemp L6r3temp L6rtemp\"></div><div class=\"L6beamtemp L6b1temp L6btemp\"></div><div class=\"L6beamtemp L6b3temp L6btemp\"></div></div>";
    			break;
    	}
        
        writer.write("$('body').prepend('<div id=\"loader-wrappertemp\">" + LoaderWrapper + "</div>');");
        writer.write("};");
        
        writer.write("function ResumeApp() {");
        writer.write("$('body').addClass('loaded');");
        writer.write("$('#loader-wrappertemp').remove();");
        writer.write("};");
                   
        boolean HasFloatingContainerScroll=false;
        for (Entry<String, ABMContainer> cont : FloatingContainers.entrySet()) {
        	if (cont.getValue().OnScrollStartOpacity>Double.MIN_VALUE) {
        		HasFloatingContainerScroll=true;
        		break;
        	}
        }        
        
        if (HasFloatingContainerScroll) {
        	writer.write("\n!function(){var a=jQuery.event.special,b='D'+ +new Date,c='D'+(+new Date+1);a.scrollstart={setup:function(){var c,d=function(b){var d=this,e=arguments;c?clearTimeout(c):(b.type='scrollstart',jQuery.event.handle.apply(d,e)),c=setTimeout(function(){c=null},a.scrollstop.latency)};jQuery(this).bind('scroll',d).data(b,d)},teardown:function(){jQuery(this).unbind('scroll',jQuery(this).data(b))}},a.scrollstop={latency:300,setup:function(){var b,d=function(c){var d=this,e=arguments;b&&clearTimeout(b),b=setTimeout(function(){b=null,c.type='scrollstop',jQuery.event.handle.apply(d,e)},a.scrollstop.latency)};jQuery(this).bind('scroll',d).data(c,d)},teardown:function(){jQuery(this).unbind('scroll',jQuery(this).data(c))}}}();");
        	writer.write("var abmTO = false;");
        	writer.write("var abmscroll_static = true;");
        	writer.write("$(window).scroll(function(){");
        	writer.write("if (abmscroll_static){");
        	for (Entry<String, ABMContainer> cont : FloatingContainers.entrySet()) {
        		if (cont.getValue().OnScrollStartOpacity>Double.MIN_VALUE) {
        			String floatcont = cont.getValue().ParentString + cont.getValue().ArrayName.toLowerCase() + cont.getValue().ID.toLowerCase();
        			writer.write("$(\"#" + floatcont + "\").css({\"opacity\":\"" + cont.getValue().OnScrollStartOpacity + "\",\"transition\":\"0.5s\"});");
        		}
        	}
        	writer.write("abmscroll_static = false;");
        	writer.write("}");
        	writer.write("if (abmTO !== false){clearTimeout(abmTO); }");           
        	writer.write("abmTO = setTimeout(abmScrollStopFunction, 500);"); 
        	writer.write("});");
        	writer.write("function abmScrollStopFunction() {");
        	writer.write("abmscroll_static = true;");
        	for (Entry<String, ABMContainer> cont : FloatingContainers.entrySet()) {
        		if (cont.getValue().OnScrollStartOpacity>Double.MIN_VALUE) {
        			String floatcont = cont.getValue().ParentString + cont.getValue().ArrayName.toLowerCase() + cont.getValue().ID.toLowerCase();
        			writer.write("$(\"#" + floatcont + "\").css({\"opacity\":\"" + cont.getValue().OnScrollStopOpacity + "\",\"transition\":\"0.5s\"});");
        		}
        	}            	
        	writer.write("};");
        }
        
        if (NeedsOAuth) {
        	writer.write("hello.init({");
        	for (Entry<String, ABMSocialOAuth> enetwork: Networks.entrySet()) {
        		ABMSocialOAuth network =enetwork.getValue();
        		for (int w=0;w<network.Networks.size();w++) {
        			writer.write(network.Networks.get(w) + ": " + network.RegIds.get(w) + ",");
        		}            		
        	}
        	writer.write("}, {redirect_uri: ''});");
        	writer.write("hello.on('auth.login', function(auth) {");
        	writer.write("hello(auth.network).api('/me').then(function(r) {");
        	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': 'page_authenticated','eventparams':'network,name,thumbnail','network': auth.network , 'name': r.name ,'thumbnail': r.thumbnail});");
        	writer.write("});");
        	writer.write("});");
            writer.write("function signout(network,registrationId,returnextra) {");
            writer.write("hello.init({");
            writer.write("network : registrationId");
            writer.write("}, {redirect_uri: ''});");
            writer.write("hello(network).logout().then(function() {");
            writer.write("b4j_raiseEvent('page_parseevent', {'eventname': 'page_signedoffsocialnetwork','eventparams':'network,extra','network': network,'extra' : returnextra});");
            writer.write("});");
            writer.write("};");        
        }
        
        if (!ScrollFires.isEmpty()) {
        	writer.write("var options=[");
        	for (int i=0;i<ScrollFires.size()-1;i++) {
        		writer.write(ScrollFires.get(i) + ",");
        	}
        	writer.write(ScrollFires.get(ScrollFires.size()-1) );
        	writer.write("];");
        	writer.write("Materialize.scrollFire(options);");        	
        }
        
        writer.write("if (window.addEventListener) {");
        writer.write("window.addEventListener('message', onMessage, false);");        
        writer.write("}");
        writer.write("else if (window.attachEvent) {");
        writer.write("window.attachEvent('onmessage', onMessage, false);");
        writer.write("}");
        writer.write("function onMessage(event) {");
        writer.write("var data = event.data;");
        writer.write("if (typeof(window[data.func]) == 'function') {");
        writer.write("window[data.func].call(null, data.id, data.success);");
        writer.write("}");
        writer.write("}");
        
        if (NeedsPivot || NeedsPlanner) {
        	writer.write("function RaiseLoaded(id, success) {");
        	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': id + '_loaded','eventparams':'success', 'success': success});");
        	writer.write("}");
        	writer.write("function RaiseChanged(id, value) {");
        	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': id + '_rendererchanged','eventparams':'newrenderer', 'newrenderer': value});");
        	writer.write("}");
        	
        	if (NeedsPivot) {
        		writer.write("function PrintToPDF(elemID, fileName) {");
        		writer.write("var margin = 15;");
        		writer.write("var elem = $('#' + elemID).contents().find('.pvtTable');");
        		writer.write("var elemW = elem.width()/2;");
        		writer.write("var elemH = elem.height()/2;");
        		writer.write("pdf = new jsPDF('p', 'pt', 'a4');");
        		writer.write("var pageW = pdf.internal.pageSize.width - margin*2;");
        		writer.write("var pageH = pdf.internal.pageSize.height - margin*2;");
        			
        		writer.write("var direction='p';");
        		writer.write("var ratio;");
        		writer.write("var elemWS;");
        		writer.write("var elemHS;");
        		writer.write("if (elemW > pageW) {");
        		writer.write("direction = 'l';");
        		writer.write("pageW = pdf.internal.pageSize.height - margin*2;");
        		writer.write("pageH = pdf.internal.pageSize.width - margin*2;");
        		writer.write("ratio = pageW / elemW;");
        		writer.write("elemWS = pageW;");
        		writer.write("elemHS = elemH * ratio;");
        		writer.write("if (elemHS <= pageH) {");
        					// one page
        		writer.write("html2canvas(elem[0]).then(function (canvas) {");
        		writer.write("pdf = new jsPDF(direction, 'pt', 'a4');");
        		writer.write("var imgData = canvas.toDataURL('image/png', 1.0);");
        		writer.write("pdf.addImage(imgData, 'PNG', margin, margin, elemWS,elemHS);");
        		writer.write("pdf.save(fileName);");
        		writer.write("});");
        		writer.write("} else {");
        					// multi page, so back to portrait
        		writer.write("elemW = elem.width()/2;");
        		writer.write("elemH = elem.height()/2;");
        		writer.write("pageW = pdf.internal.pageSize.width - margin*2;");
        		writer.write("pageH = pdf.internal.pageSize.height - margin*2;");
        		writer.write("direction='p';");
        		writer.write("ratio = pageW / elemW;");
        		writer.write("elemWS = pageW;");
        		writer.write("elemHS = elemH * ratio;");
        		writer.write("if (elemHS <= pageH) {");
    				// one page
        		writer.write("html2canvas(elem[0]).then(function (canvas) {");
        		writer.write("pdf = new jsPDF(direction, 'pt', 'a4');");
        		writer.write("var imgData = canvas.toDataURL('image/png', 1.0);");
        		writer.write("pdf.addImage(imgData, 'PNG', margin, margin, elemW,elemH);");
        		writer.write("pdf.save(fileName);");
        		writer.write("});");
        		writer.write("} else {");
    				// multi page
        		writer.write("var totalPDFPages = Math.ceil(elemHS / pageH) - 1;");
        		writer.write("html2canvas(elem[0]).then(function (canvas) {");
        		writer.write("pdf = new jsPDF(direction, 'pt', 'a4');");
        		writer.write("var imgData = canvas.toDataURL('image/png', 1.0);");
        		writer.write("pdf.addImage(imgData, 'PNG', margin, margin, elemWS, elemHS);");
        		writer.write("pageH = pdf.internal.pageSize.height;");
        		writer.write("var heightLeft = elemHS - pageH;");
        		writer.write("var position = margin;");
    					
        		writer.write("while (heightLeft >= 0) {");
        		writer.write("position = heightLeft - elemHS;");        				
        		writer.write("pdf.addPage(pdf.internal.pageSize.width, pdf.internal.pageSize.height);");    					
        		writer.write("pdf.addImage(imgData, 'PNG', margin, position, elemWS, elemHS);");
        		writer.write("heightLeft -= pageH;");
        		writer.write("}");
        		writer.write("pdf.save(fileName);");
        		writer.write("});");
        		writer.write("}");
        		
        		writer.write("}");
        		writer.write("} else {");
        		writer.write("ratio = pageW / elemW;");
        		writer.write("elemWS = pageW;");
        		writer.write("elemHS = elemH * ratio;");
        		writer.write("if (elemHS <= pageH) {");
        					// one page
        		writer.write("html2canvas(elem[0]).then(function (canvas) {");
        		writer.write("pdf = new jsPDF(direction, 'pt', 'a4');");
        		writer.write("var imgData = canvas.toDataURL('image/png', 1.0);");
        		writer.write("pdf.addImage(imgData, 'PNG', margin, margin, elemW,elemH);");
        		writer.write("pdf.save(fileName);");
        		writer.write("});");
        		writer.write("} else {");
        					// multi page
        		writer.write("var totalPDFPages = Math.ceil(elemHS / pageH) - 1;");
        		writer.write("html2canvas(elem[0]).then(function (canvas) {");
        		writer.write("pdf = new jsPDF(direction, 'pt', 'a4');");
        		writer.write("var imgData = canvas.toDataURL('image/png', 1.0);");
        		writer.write("pdf.addImage(imgData, 'PNG', margin, margin, elemWS, elemHS);");
        		writer.write("pageH = pdf.internal.pageSize.height;");
        		writer.write("var heightLeft = elemHS - pageH;");
        		writer.write("var position = margin;");        						
        		writer.write("while (heightLeft >= 0) {");
        		writer.write("position = heightLeft - elemHS;");
        		writer.write("pdf.addPage(pdf.internal.pageSize.width, pdf.internal.pageSize.height);");
        		writer.write("pdf.addImage(imgData, 'PNG', margin, position, elemWS, elemHS);");
        		writer.write("heightLeft -= pageH;");
        		writer.write("}");
        		writer.write("pdf.save(fileName);");
        		writer.write("});");
        		writer.write("}");
        		writer.write("}");

        		writer.write("};");
        	}
        }
        
        writer.write("function contentinview (el) {");
        writer.write("if (typeof jQuery === \"function\" && el instanceof jQuery) {");
        writer.write("el = el[0];");
        writer.write("}");
        writer.write("var rect = el.getBoundingClientRect();");
        writer.write("return rect.bottom > 0 &&");
        writer.write("rect.right > 0 &&");
        writer.write("rect.left < (window.innerWidth || document.documentElement.clientWidth) &&");
        writer.write("rect.top < (window.innerHeight || document.documentElement.clientHeight);");
        writer.write("}");
                                           
        writer.write("function nextcontent(id,offset) {");           
        writer.write("if (!$('#' + id).hasClass(\"scrollLoaded\")) {");
        writer.write("$('#' + id).addClass(\"scrollLoaded\");");           
        writer.write("if (contentinview($('#' + id)[0])) {");
        writer.write("b4j_raiseEvent('page_parseevent', {'eventname':'page_nextcontent','eventparams':'triggercomponent', 'triggercomponent': id });");
        writer.write("return;");
        writer.write("}\n");
        writer.write("}");            
        writer.write("var opts=[{");
        writer.write("'selector': '#' + id,"); 
        writer.write("offset: offset, ");
        writer.write("callback: 'b4j_raiseEvent(\"page_parseevent\", {\"eventname\":\"page_nextcontent\",\"eventparams\":\"triggercomponent\", \"triggercomponent\": \"' + id + '\"})'}];");
    	writer.write("Materialize.scrollFire(opts);");
    	writer.write("};");
    	    	
    	writer.write("function nextcontentrow(id, offset, table) {");
    	writer.write("var tmpid = \"_\" + id.toLowerCase();");
    	writer.write("if (!$('#' + tmpid).hasClass('scrollLoaded')) {");
    	writer.write("$('#' + tmpid).addClass('scrollLoaded');");
    	writer.write("if (contentinview($('#' + tmpid)[0])) {");
    	writer.write("b4j_raiseEvent('page_parseevent', {");
    	writer.write("'eventname': table + '_nextcontent',");
    	writer.write("'eventparams': 'triggerrow',");
    	writer.write("'triggerrow': id");
    	writer.write("});");
    	writer.write("return;");
    	writer.write("}");
    	writer.write("}");
    	
    	writer.write("$('#' + table + '-body').parent().off('scroll').on('scroll', function () {");
    	//writer.write("if ($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight-0.9999) {");
    	writer.write("if ($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight-1) {");
    	writer.write("b4j_raiseEvent('page_parseevent', {");
    	writer.write("'eventname': table + '_nextcontent',");
    	writer.write("'eventparams': 'triggerrow',");
    	writer.write("'triggerrow': id");
    	writer.write("});");
    	writer.write("}");
    	writer.write("});");
    	writer.write("};");
    	
    	writer.write("function nextcontentcontainer(par, arrayname, id, containerid, rc, offset) {");       
        writer.write("if (!$('#' + par + arrayname + id).hasClass(\"scrollLoaded\")) {");
        writer.write("$('#' + par + arrayname + id).addClass(\"scrollLoaded\");");           

        writer.write("}");            
        writer.write("var opts=[{");
        writer.write("'selector': '#' + par + arrayname + id,"); 
        writer.write("offset: offset, ");
        writer.write("callback: 'b4j_raiseEvent(\"page_parseevent\", {\"eventname\":\"' + containerid + '_nextcontent\",\"eventparams\":\"triggercomponent\", \"triggercomponent\": \"' + id + '\"})'}];");
    	writer.write("Materialize.scrollFireDiv(opts, $('#' + rc));");
    	writer.write("};");
		        	
    	writer.write("function resizeAllEditors() {");
    	writer.write("$('.abmiframe').each(function () {");
    	writer.write("resizeFrame($(this)[0]);");
    	writer.write("});");
    	writer.write("};");
    	
        if (NeedsChart) {
        	writer.write("var chartdata = {};");
        	writer.write("var chartoptions = {};");
        	writer.write("var charts = {};");
        	writer.write("var chartresets = {};");
        	writer.write("function BuildLineChart(id) {");
        	writer.write("charts[id]=new Chartist.Line('#' + id, chartdata[id], chartoptions[id]);" );
			writer.write("};");
			writer.write("function BuildBarChart(id) {");
        	writer.write("charts[id]=new Chartist.Bar('#' + id, chartdata[id], chartoptions[id]);" );
			writer.write("};");
			writer.write("function BuildPieChart(id) {");
        	writer.write("charts[id]=new Chartist.Pie('#' + id, chartdata[id], chartoptions[id]);" );
			writer.write("};");
			
			writer.write("function onZoom(chart, reset) {");
			writer.write("chart.resetFnc = reset;");
			writer.write("}");
			writer.write("function resetchart(id) {");
			writer.write("charts[id].resetFnc && charts[id].resetFnc();");
			writer.write("charts[id].resetFnc = null;");
			writer.write("}");
        	for (Entry<String,ABMChart> chart: Charts.entrySet()) {
        		writer.write(chart.getValue().BuildScript());
        	}
        	writer.write("function chartupdate(id, data, options) {");
        	writer.write("var data2 = jQuery.parseJSON(data);");

        	writer.write("charts[id].update(data2);");
        	writer.write("};");        
        }
        
        if (NeedsFixPasswordAutoFill) {
        	writer.write("var fpcount = 0;"); 
        	writer.write("var fpinterval = setInterval(function () {"); 
        	writer.write("fpcount++;"); 
        	writer.write("$('input[type=password]:-webkit-autofill').siblings('label').addClass('active');"); 
        	writer.write("if (fpcount > 500) {"); 
        	writer.write("clearInterval(fpinterval);"); 
        	writer.write("}"); 
        	writer.write("}, 10);"); 
        }
	}
        
    protected void WriteJSPage(BufferedWriter writer, String relPath, boolean NeedsAutorization, String ReloadStringInner, boolean IsApp) throws IOException {
    	if (DebugConzoleOnOpen) {
    		writer.write("window.onload=function() {");
    		writer.write("conzole.open();\nconzole.setWidth(" + DebugConzoleWidth + ");");
    		writer.write("};");
    	}
    	
    	if (this.navigationBar!=null) {
    		if (this.navigationBar.WithExtraContent) {
    			writer.write("(function ($) {");
    			writer.write("$.fn.heightChanged = function (handleFunction) {");
    			writer.write("var element = this;");
    			writer.write("var lastHeight = element.height();");
    			writer.write("setInterval(function () {");
    			writer.write("if (lastHeight === element.height())");
    			writer.write("return;");
    			writer.write("if (typeof (handleFunction) == 'function') {");
    			writer.write("handleFunction({ height: lastHeight }, { height: element.height() });");
    			writer.write("lastHeight = element.height();");
    			writer.write("}");
    			writer.write("}, 200);");
    			writer.write("return element;");
    			writer.write("};");
    			writer.write("}(jQuery));");
    			writer.write("var navPaddingTop=" + PaddingTop + ";");
    			writer.write("$('#" + this.navigationBar.ID.toLowerCase() + "').heightChanged(function(){");
    			writer.write("$('main').css('padding-top', $('#" + this.navigationBar.ID.toLowerCase() + "').height()+navPaddingTop);");
    			writer.write("});");
    			
    		}
    		
    		if (!(navigationBar==null)) {
    			writer.write("if (!$('.button-collapse').hasClass('abminitialized')) {");
    			writer.write("$(\".button-collapse\").sideNav({menuWidth:" + navigationBar.NBWidthPx  + ",mediumSize: " + ABMaterial.ThresholdPxConsiderdMedium + "});");
    			writer.write("$('.button-collapse').addClass('abminitialized');");
    			writer.write("}");
        		for (ABMNavBarItem item: navigationBar.TopItems) {
    				if (item.SideBar!=null) {
    					String exPos = "right";
    					writer.write("$('#btnextrasidebar" + item.SideBar.ID.toLowerCase() + "').abmsideNav({edge: '" + exPos + "', closeOnClick: false, menuWidth: "  + item.SideBar.WidthPx + ", menuWidthS: "  + item.SideBar.WidthSPx + ", menuWidthM: "  + item.SideBar.WidthMPx + ", menuWidthL: "  + item.SideBar.WidthPx + " });");
    				}
    			}
        		if (navigationBar.WithExtraSearch) {
        			writer.write("function navSearch() {");
        			writer.write("if ($('body').hasClass('search-open')) {");
        			writer.write("b4j_raiseEvent('page_parseevent',{'eventname':'page_navigationbarsearch','eventparams':'search','search':$('#" + navigationBar.ExtraSearchID + "').val()});");
        			writer.write("$('body').removeClass('search-open');");
        			writer.write("$('.brand-logo').removeClass('search-open');");
        			writer.write("} else {");
        			writer.write("$('body').addClass('search-open');}");
        			writer.write("$('.brand-logo').addClass('search-open');");
        			writer.write("$('#" + navigationBar.ExtraSearchID + "').focus();");
        			writer.write("}\n");
        			writer.write("function navSearchClose() {");
        			writer.write("$('body').removeClass('search-open');");
        			writer.write("$('.brand-logo').removeClass('search-open');");
        			writer.write("}\n");
        			writer.write("$('#" + navigationBar.ExtraSearchID + "').off('keydown').on('keydown', function(e) {");
        			writer.write("if (e.keyCode==13) {");
        			writer.write("b4j_raiseEvent('page_parseevent',{'eventname':'page_navigationbarsearch','eventparams':'search','search':$('#" + navigationBar.ExtraSearchID + "').val()});");
        			writer.write("$('body').removeClass('search-open');");
        			writer.write("$('.brand-logo').removeClass('search-open');");
        			writer.write("}");
        			writer.write("});\n");
        		}
        	} else {
        		writer.write("if (!$('.button-collapse').hasClass('abminitialized')) {");
       			writer.write("$(\".button-collapse\").sideNav({menuWidth:240,mediumSize: " + ABMaterial.ThresholdPxConsiderdMedium + "});");
    			writer.write("$('.button-collapse').addClass('abminitialized');");
    			writer.write("}");
        	}    
    		
    		writer.write("var lastSize='0px';");
    		writer.write("var lastSizeStr='initial';");
    		writer.write("$(window).resize(function() {");
    		
    		writer.write("var currSize = $('#print-body').css('outline-width');");
    		writer.write("var currSizeStr=lastSizeStr;");
    		writer.write("if (lastSize!=currSize) {");
    		writer.write("if (currSize=='1px') {currSizeStr='phone'};");
    		writer.write("if (currSize=='2px') {currSizeStr='tablet'};");
    		writer.write("if (currSize=='3px') {currSizeStr='desktop'};");	
    		
    		writer.write("$('#devicetype').html('' + currSizeStr);");
    		if (NeedsResponsiveContainer) {
    			writer.write("ElementQueries.update();");
    		}
    		
    		writer.write("b4j_raiseEvent('page_parseevent', {'eventname':'page_sizechanged','eventparams':'previous,current', 'previous': lastSizeStr, 'current': currSizeStr});");
    		writer.write("}");
    		if (PageRaiseOnResize) {
    			writer.write("b4j_raiseEvent('page_parseevent', {'eventname': 'page_onresize','eventparams': ''});");
    		}
    		writer.write("lastSize=currSize;");
    		writer.write("lastSizeStr=currSizeStr;");

    		writer.write("$('.extrasidebar').sideNav('hide');");
    		writer.write("$('.dropdown-button').dropdown('close');");
    		writer.write("$('.dropdown-content').hide();");
    		if (this.navigationBar!=null) {
    			if (!this.navigationBar.SideItems.isEmpty()) {
    				writer.write("if ($('.nav-wrapper').length>0) {");
    				writer.write("var parWidth = $('.nav-wrapper').width();");
    				writer.write("var myLeft = $('#pagenavbar').position().left;");
    				writer.write("var myRight = parseInt($('#sidenavbutton').css('margin-left'))*2 + $('#sidenavbutton').outerWidth();");
    				writer.write("if ($('.nav-wrapper ul.right:last-child').length>0) {");
    				writer.write("myRight = parWidth - $('.nav-wrapper ul.right:last-child').position().left;");
    				writer.write("}");
    				writer.write("if (myLeft<56) {myLeft = 56;}");
    				writer.write("if ((parWidth - myLeft - myRight) < 10) {$('#pagenavbar').outerWidth(0);} else {$('#pagenavbar').outerWidth(parWidth - myLeft - myRight);}");
    				
    				writer.write("if (parseInt($('main').css('padding-left')) > 0) {");
    				writer.write("setTimeout(function(){");
    				writer.write("if (parseInt($('main').css('padding-left')) > 0) {");
    				writer.write("$('#nav-mobile').css('left', 0);");			
    				writer.write("}");
    				writer.write("},200);");
    				writer.write("}");
    				
    				writer.write("}");
    			}
    		}
    		if (NeedsChronologyList) {
    			writer.write("PositionChronosAll();");
    		}
            if (NeedsPlanner) {
            	writer.write("plannernamenu.retract(0);");
            	writer.write("plannerccpmenu.retract(0);");
            	writer.write("ResizePlanner();");
            }
            writer.write("setTimeout(function() {");
            writer.write("resizecells();");
            writer.write("}, 200);"); 
            writer.write("});");
    		if (NeedsPlanner) {
            	writer.write("$(window).off('mouseup touchend').on('mouseup touchend', function() {");
    			writer.write("plannernamenu.retract(0);");
            	writer.write("plannerccpmenu.retract(0);");
            	writer.write("});");
            }
    		
    		writer.write("function extrabar(id) {");
    		writer.write("$('.extrasidebar:not(\"#' + id + '\")').abmsideNav('hide');");
    		writer.write("};");
    	} else {
    		if (!(navigationBar==null)) {
    			writer.write("if (!$('.button-collapse').hasClass('abminitialized')) {");
    			writer.write("$(\".button-collapse\").sideNav({menuWidth:" + navigationBar.NBWidthPx  + ",mediumSize: " + ABMaterial.ThresholdPxConsiderdMedium + "});");
    			writer.write("$('.button-collapse').addClass('abminitialized');");
    			writer.write("}");        	
        	} else {
        		writer.write("if (!$('.button-collapse').hasClass('abminitialized')) {");
       			writer.write("$(\".button-collapse\").sideNav({menuWidth:240,mediumSize: " + ABMaterial.ThresholdPxConsiderdMedium + "});");
    			writer.write("$('.button-collapse').addClass('abminitialized');");
    			writer.write("}");
        	}    
    		
    		writer.write("var lastSize='0px';");
    		writer.write("var lastSizeStr='initial';");
    		writer.write("$(window).resize(function() {");
    		
    		writer.write("var currSize = $('#print-body').css('outline-width');");
    		writer.write("var currSizeStr=lastSizeStr;");
    		
    		writer.write("if (lastSize!=currSize) {");
    		writer.write("if (currSize=='1px') {currSizeStr='phone'};");
    		writer.write("if (currSize=='2px') {currSizeStr='tablet'};");
    		writer.write("if (currSize=='3px') {currSizeStr='desktop'};");	
    		writer.write("$('#devicetype').html('' + currSizeStr);");
    		    		
    		if (NeedsResponsiveContainer) {
    			writer.write("ElementQueries.update();");
    		}
    		
    		writer.write("b4j_raiseEvent('page_parseevent', {'eventname':'page_sizechanged','eventparams':'previous,current', 'previous': lastSizeStr, 'current': currSizeStr});");
    		writer.write("}");
    		if (PageRaiseOnResize) {
    			writer.write("b4j_raiseEvent('page_parseevent', {'eventname': 'page_onresize','eventparams': ''});");
    		}
    		writer.write("lastSize=currSize;");
    		writer.write("lastSizeStr=currSizeStr;");    		
    		
    		//writer.write("resizecells();");
    		writer.write("$('.dropdown-button').dropdown('close');");
    		writer.write("$('.dropdown-content').hide();");
    		if (NeedsChronologyList) {
    			writer.write("PositionChronosAll();");
    		}
            if (NeedsPlanner) {
            	writer.write("plannernamenu.retract(0);");
            	writer.write("plannerccpmenu.retract(0);");
            	writer.write("ResizePlanner();");
            }
            writer.write("setTimeout(function() {");
            writer.write("resizecells();");
            writer.write("}, 200);"); 
            writer.write("});");
            if (NeedsPlanner) {
            	writer.write("$(window).unbind('mouseup touchend').bind('mouseup touchend', function() {");
            	writer.write("plannernamenu.retract(0);");
            	writer.write("plannerccpmenu.retract(0);");
            	writer.write("});");
            }            
    	}
    	
    	writer.write("function getcurrentsizestr() {");
    	writer.write("var currSize = $('#print-body').css('outline-width');");
    	writer.write("var currSizeStr=lastSizeStr;");
    	writer.write("if (lastSize!=currSize) {");
    	writer.write("if (currSize=='1px') {currSizeStr='phone'};");
    	writer.write("if (currSize=='2px') {currSizeStr='tablet'};");
    	writer.write("if (currSize=='3px') {currSizeStr='desktop'};");
    	writer.write("$('#devicetype').html('' + currSizeStr);");
    	writer.write("}");
    	writer.write("lastSize=currSize;");
    	writer.write("lastSizeStr=currSizeStr;");
    	if (NeedsResponsiveContainer) {
			writer.write("ElementQueries.update();");
		}
    	writer.write("return lastSizeStr;");
    	writer.write("}\n");
    	
    	writer.write("function newGuid() {");
    	writer.write("return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {");
    	writer.write("var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);");
    	writer.write("return v.toString(16);");
    	writer.write("});");
    	writer.write("};");
    	writer.write("function getGuid() {");
    	writer.write("if (window.name==='undefined') {");
    	writer.write("return '';");		
    	writer.write("} else {");
    	writer.write("var myUUID=window.name;");
    	writer.write("if (myUUID==='') {myUUID=newGuid();window.name=myUUID;}");
    	writer.write("}");
    	writer.write("return myUUID;");
    	writer.write("}\n");
    	
    	writer.write("function initrowcellclicks() {");
    	
    	writer.write("$('.rclick').each(function() {");
    	 writer.write("$(this).off('mouseenter');");
         writer.write("$(this).off('mouseleave');");
         writer.write("var myB4JSKey = $(this).attr('data-b4js');");
         writer.write("if ($(this).attr('data-b4jsextra')===\"true\") {");
         writer.write("$(this).on({");
         writer.write("mouseenter: function () {");
 	        //stuff to do on mouse enter
         	writer.write("var codeToExecute = 'return _b4jsclasses[\"' + myB4JSKey + '\"].' + _b4jsvars[myB4JSKey + '_B4JSOnMouseEnter'];");        	
         	writer.write("var tmpFunc = new Function(codeToExecute);");
         	writer.write("var fret=false;");
         	writer.write("try { ");
         	writer.write("fret = tmpFunc()");	
         	writer.write("} catch(err) {}");
         	writer.write("if (fret==true) {return;}");
         writer.write("},");
         writer.write("mouseleave: function () {");
 	        //stuff to do on mouse leave
         	writer.write("var codeToExecute = 'return _b4jsclasses[\"' + myB4JSKey + '\"].' + _b4jsvars[myB4JSKey + '_B4JSOnMouseLeave'];");        	
         	writer.write("var tmpFunc = new Function(codeToExecute);");
         	writer.write("var fret=false;");
         	writer.write("try { ");
         	writer.write("fret = tmpFunc()");	
         	writer.write("} catch(err) {}");
         	writer.write("if (fret==true) {return;}");
         writer.write("}");
         writer.write("});");
         writer.write("}");
        writer.write("});");

        writer.write("$('.rclick').off('click').on('click', function() {");
        writer.write("if (abmdragging) return false;");        
        writer.write("var a = $(this).attr('data-click');");
        writer.write("if (a) {");        
        writer.write("var myB4JSKey = $(this).attr('data-b4js');");
        writer.write("if (myB4JSKey!='') {");
        	writer.write("var codeToExecute = 'return _b4jsclasses[\"' + myB4JSKey + '\"].' + _b4jsvars[myB4JSKey + '_B4JSOnClick'];");        	
        	writer.write("var tmpFunc = new Function(codeToExecute);");
        	writer.write("var fret=false;");
        	writer.write("try { ");
        	writer.write("fret = tmpFunc()");	
        	writer.write("} catch(err) {}");
        	writer.write("if (fret==true) {return;}");
        writer.write("}");
        
        writer.write("var res = a.split(',');");
        writer.write("b4j_raiseEvent('page_parseevent', {'eventname':'page_rowclicked','eventparams':'target,row', 'target': res[0], 'row': res[1]});");
        writer.write("}");
        writer.write("return false;});");
        
        writer.write("$('.cclick').each(function() {");
   	 	writer.write("$(this).off('mouseenter');");
        writer.write("$(this).off('mouseleave');");
        writer.write("var myB4JSKey = $(this).attr('data-b4js');");
        writer.write("if ($(this).attr('data-b4jsextra')===\"true\") {");
        writer.write("$(this).on({");
        writer.write("mouseenter: function () {");
	        //stuff to do on mouse enter
        	writer.write("var codeToExecute = 'return _b4jsclasses[\"' + myB4JSKey + '\"].' + _b4jsvars[myB4JSKey + '_B4JSOnMouseEnter'];");        	
        	writer.write("var tmpFunc = new Function(codeToExecute);");
        	writer.write("var fret=false;");
        	writer.write("try { ");
        	writer.write("fret = tmpFunc()");	
        	writer.write("} catch(err) {}");
        	writer.write("if (fret==true) {return;}");
        writer.write("},");
        writer.write("mouseleave: function () {");
	        //stuff to do on mouse leave
        	writer.write("var codeToExecute = 'return _b4jsclasses[\"' + myB4JSKey + '\"].' + _b4jsvars[myB4JSKey + '_B4JSOnMouseLeave'];");        	
        	writer.write("var tmpFunc = new Function(codeToExecute);");
        	writer.write("var fret=false;");
        	writer.write("try { ");
        	writer.write("fret = tmpFunc()");	
        	writer.write("} catch(err) {}");
        	writer.write("if (fret==true) {return;}");
        writer.write("}");
        writer.write("});");
        writer.write("}");
        writer.write("});");

        writer.write("$('.cclick').off('click').on('click', function() {");
        writer.write("if (abmdragging) return false;");
        writer.write("var a = $(this).attr('data-click');");
        writer.write("if (a) {");        
        writer.write("var myB4JSKey = $(this).attr('data-b4js');");
        writer.write("if (myB4JSKey!='') {");
        	writer.write("var codeToExecute = 'return _b4jsclasses[\"' + myB4JSKey + '\"].' + _b4jsvars[myB4JSKey + '_B4JSOnClick'];");        	
        	writer.write("var tmpFunc = new Function(codeToExecute);");
        	writer.write("var fret=false;");
        	writer.write("try { ");
        	writer.write("fret = tmpFunc()");	
        	writer.write("} catch(err) {}");
        	writer.write("if (fret==true) {return;}");
        writer.write("}");
        
        writer.write("var res = a.split(',');");	
        writer.write("b4j_raiseEvent('page_parseevent', {'eventname':'page_cellclicked','eventparams':'target,rowcell', 'target': res[0], 'rowcell': res[1]});");
        writer.write("}");
        writer.write("return false;});");
        writer.write("};");
        
        writer.write("function resizecellsOnly() {");
        writer.write("$('[data-fixedbottom]').each(function() {");
        writer.write("var bheight = $(this).attr('data-fixedbottom');");
        writer.write("var newHeight = window.innerHeight - $(this).offset().top - parseInt($(this).css('padding-top')) - parseInt($(this).css('padding-bottom')) - bheight;");
        writer.write("$(this).height(newHeight);");
        writer.write("});");
        writer.write("syncHeaderFooters();");
        writer.write("salvattore.rescanMediaQueries();");
        writer.write("};");
    	
    	writer.write("function resizecells() {");   
    	writer.write("initrowcellclicks();");
    	writer.write("resizecellsOnly();");
    	writer.write("};");
    	
    	writer.write("function initplugins(){");
        writer.write("$('.materialboxed').materialbox();");
        writer.write("$('.tooltipped').tooltip('remove');");
        writer.write("$('.tooltipped').tooltip({delay:50});");
        writer.write("$('.collapsible').collapsible();");
        
        writer.write("InitEditEnterFields();");
        
        writer.write("$('#sidenavbutton').on('click', function() {");
        writer.write("$('.dropdown-button').dropdown('close');");
        writer.write("$('.dropdown-content').hide();");
        writer.write("});");

        if (NeedsPlanner) {
        	writer.write("InitializeWeekviews();;");
        }
        writer.write("setTimeout(function() {");
        writer.write("resizecells();");
        writer.write("}, 200);");
        
        writer.write("var currSize = $('#print-body').css('outline-width');");
        writer.write("var currSizeStr=lastSizeStr;");
        writer.write("if (lastSize!=currSize) {");
        writer.write("if (currSize=='1px') {currSizeStr='phone'};");
        writer.write("if (currSize=='2px') {currSizeStr='tablet'};");
        writer.write("if (currSize=='3px') {currSizeStr='desktop'};");	
        writer.write("}");
        writer.write("$('#devicetype').html('' + currSizeStr);");
        writer.write("lastSize=currSize;");
        writer.write("lastSizeStr=currSizeStr;");		
        
        if (NeedsParallax) {
        	writer.write("$('.parallax').parallax();");
        }
        if (NeedsTabs) {
        	writer.write("$('ul.tabs').tabs();");
        	
        }
        
        if (NeedsImageSlider) {
        	for (Map.Entry<String, ThemeImageSlider> entry : CompleteTheme.ImageSliders.entrySet()) {
        		ThemeImageSlider sl = entry.getValue();
        		writer.write("$('.slider" + sl.ThemeName.toLowerCase() + "').slider({full_width:" + sl.FullWidth + ",indicators:" + sl.Indicators + ",height:" + sl.Height + ",transition:" + sl.Transition + ",interval:" + sl.Interval+ "});");
        	}
        	writer.write("$('.sliderdefault').slider({full_width:true,indicators:true,height:400,transition:500,interval:6000});");
        }
        if (NeedsSlider) {
    		
    	}
        if (NeedsCombo) {
        	writer.write("$('.dropdown-button2').dropdown2();");        	
        	writer.write("$('.dropdown-button2.raisechanged').off('keyup').on('keyup', function(e) {");
        	writer.write("$(this).attr('value',$(this).val());");
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': $(this).attr('evname') + '_changed',");
        	writer.write("'eventparams': 'value',");
        	writer.write("'value': $(this).val()");
        	writer.write("});");        	
        	writer.write("});");        	
        }
        if (NeedsUpload) {
        	writer.write("runupload();");
        }
        if (this.navigationBar!=null) {
        	if (this.navigationBar.HasFadeEffect) {
        		writer.write("$('#r1').abscrollfire({");
        		writer.write("offset: 0, topOffset: " + navigationBar.HeightPx +", bottomOffset: 0, ");
        		writer.write("onTopIn: function( elm, distance_scrolled ) {$('#" + navigationBar.ID.toLowerCase() + "').stop().animate({opacity: " + this.navigationBar.FadeEffectValue + "}, 500);},");
        		writer.write("onTopOut: function( elm, distance_scrolled ) {$('#" + navigationBar.ID.toLowerCase() + "').stop().animate({opacity: 1.0}, 500);}");
        		writer.write("});");
        	}        	
        }
        
        writer.write("}\n");
        writer.write("var abmdragging=false;");
        writer.write("$('body').on('touchmove', function(){");
    	writer.write("abmdragging = true;");
    	writer.write("});");
    	writer.write("$('body').on('touchstart', function(){");
    	writer.write("abmdragging = false;");
    	writer.write("});");
        
        if (NeedsPlanner) {
        	int c = 0x2190;

        	writer.write("var ABMPlanner = function(menuContainer, options) {");
        	writer.write("var parentel = $('.abmplannerwrapper');");
        	writer.write("var parent = parentel.attr('id');");
        	writer.write("var ename = parentel.attr('eName');");
        	writer.write("menuContainer.children().each(function() {");
        	writer.write("var iconRadius = $(this).width() / 2;");
        	writer.write("$(this).css({");
        	writer.write("'border-radius': iconRadius + 'px',");
        	writer.write("'top': '-' + iconRadius + 'px',");
        	writer.write("'left': '-' + iconRadius + 'px',");
        	writer.write("'line-height': $(this).width() + 'px'");
        	writer.write("});");
        	writer.write("$(this).off('mouseup touchend').on('mouseup touchend', function() {");
        	writer.write("if (event.stopPropagation) {");
        	writer.write("event.stopPropagation();");
        	writer.write("} else if (window.event) {");
        	writer.write("window.event.cancelBubble = true;");
        	writer.write("}");
        	writer.write("event.preventDefault();");
        	writer.write("plannerSleeping=true;");
        	writer.write("var radius = 0,");
        	writer.write("icons = menuContainer.children(),");
        	writer.write("width = 300,");
        	writer.write("height = 200,");
        	writer.write("angle = rotate,");
        	writer.write("step = (2 * Math.PI) / icons.length / settings.circleSize;");
        	writer.write("icons.each(function() {");
        	writer.write("var x = Math.round(width / 2 + radius * Math.cos(angle) - $(this).width() / 2);");
        	writer.write("var y = Math.round(height / 2 + radius * Math.sin(angle) - $(this).height() / 2);");
        	writer.write("$(this).animate({");
        	writer.write("left: x + 'px',");
        	writer.write("top: y + 'px',");
        	writer.write("margin: '0px',");
        	writer.write("opacity: 0");
        	writer.write("}, {");
        	writer.write("duration: 150,");
        	writer.write("queue: false,");
        	writer.write("complete: function() {");
        	writer.write("menuContainer.css({");
        	writer.write("top: '0px',");
        	writer.write("left: '0px',");
        	writer.write("display: 'none'");							
        	writer.write("});");
        	writer.write("menuContainer.attr('isopen', '');");
        	writer.write("if (plannerSleep) {");
        	writer.write("clearTimeout(plannerSleep);");
        	writer.write("}");				
        	writer.write("plannerSleep = setTimeout(function() {");
        	writer.write("plannerSleeping=false;");
        	writer.write("}, 150)");
        	writer.write("}");
        	writer.write("});");
        	writer.write("angle += step;");
        	writer.write("});");        	
        	writer.write("var btnType = $(this).attr('data-id');");
        	writer.write("if (plannercurrday != '') {");
        	writer.write("plannerAction = btnType + ';day;' + plannercurrday;");
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': ename + '_menuclicked',");
        	writer.write("'eventparams': 'menutype,value',");
        	writer.write("'menutype': 'day',");
        	writer.write("'value': btnType + ';' + plannercurrday");
        	writer.write("});");
        	writer.write("} else {");
        	writer.write("if (plannercurrhour != '') {");
        	writer.write("plannerAction = btnType + ';hour;' + plannercurrhour;");
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': ename + '_menuclicked',");
        	writer.write("'eventparams': 'menutype,value',");
        	writer.write("'menutype': 'hour',");
        	writer.write("'value': btnType + ';' + plannercurrhour");
        	writer.write("});");
        	writer.write("} else {");
        	writer.write("plannerAction = '';");
        	writer.write("if (btnType == 5 || btnType == 6) {");
        	writer.write("plannermode = 1;");
        	writer.write("} else {");
        	writer.write("if (PlannerLastBtnType!=5) {");
        	writer.write("plannermode = 0;");
        	writer.write("}");
        	writer.write("}");
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': ename + '_menuclicked',");
        	writer.write("'eventparams': 'menutype,value',");
        	writer.write("'menutype': 'min',");
        	writer.write("'value': btnType + ';' + plannercurrmin");
        	writer.write("});");
        	writer.write("}");
        	writer.write("}");
        	writer.write("if (btnType!=4) {");
        	writer.write("PlannerLastBtnType = btnType;");
        	writer.write("}");
        	writer.write("});");
        	writer.write("});");
        	writer.write("var settings = $.extend({");
        	writer.write("rotate: 0,");
        	writer.write("radius: 200,");
        	writer.write("circleSize: 2,");
        	writer.write("speed: 500");
        	writer.write("}, options);");
        	writer.write("if (settings.rotate == 0) {");
        	writer.write("var rotate = 0;");
        	writer.write("} else {");
        	writer.write("var rotate = (Math.PI) * 2 * settings.rotate / 360;");
        	writer.write("}");
        	writer.write("if (settings.circleSize == 0) {");
        	writer.write("var rotate = 0;");
        	writer.write("} else {");
        	writer.write("var circleSize = settings.circleSize;");
        	writer.write("}");
        	writer.write("this.expand = function(caller, e) {");
        	writer.write("var parentel = caller.closest('.abmplannerwrapper');");
        	writer.write("if (menuContainer.hasClass('plannerccpmenu-container')) {");
        	writer.write("switch (plannermode) {");
        	writer.write("case -1:");
        	writer.write("$('.plannerccpmenu-item[data-id=3]').addClass('hide');");
        	writer.write("$('.plannerccpmenu-item[data-id=5]').addClass('hide');");
        	writer.write("$('.plannerccpmenu-item[data-id=6]').addClass('hide');");
        	writer.write("$('.plannerccpmenu-item[data-id=4]').addClass('hide');");
        	writer.write("break;");
        	writer.write("case 0:");
        	writer.write("$('.plannerccpmenu-item[data-id=3]').removeClass('hide');");
        	writer.write("$('.plannerccpmenu-item[data-id=5]').removeClass('hide');");
        	writer.write("$('.plannerccpmenu-item[data-id=6]').removeClass('hide');");
        	writer.write("$('.plannerccpmenu-item[data-id=4]').addClass('hide');");
        	writer.write("break;");
        	writer.write("case 1:");
        	writer.write("var txt = caller.find('p').html();");
        	writer.write("if (txt == '&nbsp;') {");
        	writer.write("$('.plannerccpmenu-item[data-id=3]').removeClass('hide').addClass('hide');");
        	writer.write("$('.plannerccpmenu-item[data-id=5]').addClass('hide');");
        	writer.write("$('.plannerccpmenu-item[data-id=6]').addClass('hide');");
        	writer.write("$('.plannerccpmenu-item[data-id=4]').removeClass('hide');");
        	writer.write("} else {");
        	writer.write("$('.plannerccpmenu-item[data-id=3]').removeClass('hide');");
        	writer.write("$('.plannerccpmenu-item[data-id=5]').removeClass('hide');");
        	writer.write("$('.plannerccpmenu-item[data-id=6]').removeClass('hide');");
        	writer.write("if (txt == '" + Character.toString((char)c) + "') {");
        	writer.write("$('.plannerccpmenu-item[data-id=4]').removeClass('hide');");
        	writer.write("} else {");
        	writer.write("$('.plannerccpmenu-item[data-id=4]').removeClass('hide').addClass('hide');");
        	writer.write("}");
        	writer.write("}");
        	writer.write("break;");
        	writer.write("}");
        	writer.write("}");
        	writer.write("var radius = settings.radius,");
        	writer.write("icons = menuContainer.children(':not(.hide)'),");
        	writer.write("width = 300,");
        	writer.write("height = 200,");
        	writer.write("step = (2 * Math.PI) / icons.length / settings.circleSize,");
        	writer.write("angle = rotate + (step / 2);");
        	writer.write("menuContainer.position({");
        	writer.write("\"my\": \"center center\",");
        	writer.write("\"at\": \"center center\",");
        	writer.write("\"of\": caller,");
        	writer.write("\"extra\": parentel");
        	writer.write("});");
        	writer.write("menuContainer.css({");
        	writer.write("display: 'block'");
        	writer.write("});");
        	
        	writer.write("var extra=100;");
        	writer.write("if (caller.width()<220) {");
        	writer.write("if (menuContainer.position().left-parentel.offset().left<=0) {");
        	writer.write("var rotatetmp = (Math.PI) * 2 * 270 / 360;");
        	writer.write("angle = rotatetmp + (step / 2);");
        	writer.write("extra = 50;");
        	writer.write("}");    		
        	writer.write("if (menuContainer.position().left+260-parentel.offset().left>parentel.width()) {");
        	writer.write("var rotatetmp = (Math.PI) * 2 * 90 / 360;");
        	writer.write("angle = rotatetmp + (step / 2);");
        	writer.write("extra = 50;");
        	writer.write("}");
        	writer.write("}");
        	
        	writer.write("icons.each(function() {");
        	writer.write("var x = Math.round(width / 2 + radius * Math.cos(angle) - $(this).width() / 2);");
        	writer.write("var y = Math.round(height / 2 + radius * Math.sin(angle) - $(this).height() / 2);");
        	writer.write("$(this).animate({");
        	writer.write("left: x + 'px',");
        	writer.write("top: (y + extra) + 'px',");
        	writer.write("margin: '0px',");
        	writer.write("opacity: 1");
        	writer.write("}, {");
        	writer.write("duration: 250,");
        	writer.write("queue: false,");
        	writer.write("complete: function() {");
        	writer.write("menuContainer.attr('isopen', 'true');");
        	writer.write("}");
        	writer.write("});");
        	writer.write("angle += step;");
        	writer.write("});");
        	writer.write("};");
        	writer.write("this.retract = function(speed) {");
        	writer.write("plannerSleeping=true;");
        	writer.write("var radius = 0,");
        	writer.write("icons = menuContainer.children(),");
        	writer.write("width = 300,");
        	writer.write("height = 200,");
        	writer.write("angle = rotate,");
        	writer.write("step = (2 * Math.PI) / icons.length / settings.circleSize;");
        	writer.write("icons.each(function() {");
        	writer.write("var x = Math.round(width / 2 + radius * Math.cos(angle) - $(this).width() / 2);");
        	writer.write("var y = Math.round(height / 2 + radius * Math.sin(angle) - $(this).height() / 2);");
        	writer.write("$(this).animate({");
        	writer.write("left: x + 'px',");
        	writer.write("top: y + 'px',");
        	writer.write("margin: '0px',");
        	writer.write("opacity: 0");
        	writer.write("}, {");
        	writer.write("duration: speed,");
        	writer.write("queue: false,");
        	writer.write("complete: function() {");
        	writer.write("menuContainer.css({");
        	writer.write("top: '0px',");
        	writer.write("left: '0px',");
        	writer.write("display: 'none'");
        	writer.write("});");
        	writer.write("menuContainer.attr('isopen', '');");
        	writer.write("if (plannerSleep) {");
        	writer.write("clearTimeout(plannerSleep);");
        	writer.write("}");
        	writer.write("if (speed>0) {");
        	writer.write("plannerSleep = setTimeout(function() {");
        	writer.write("plannerSleeping=false;");
        	writer.write("}, 150)");
        	writer.write("} else {");
        	writer.write("plannerSleeping=false;");
        	writer.write("}");
        	writer.write("}");
        	writer.write("});");
        	writer.write("angle += step;");
        	writer.write("});");
        	writer.write("};");
        	writer.write("};\n");
        	
        	writer.write("var plannernamenu = new ABMPlanner($('.plannernamenu-container'), {rotate: 0,	radius:110,	circleSize: 2});");
        	writer.write("var plannerccpmenu = new ABMPlanner($('.plannerccpmenu-container'), {rotate: 180,	radius:110,	circleSize: 2});");
        	
        	writer.write("var plannerStartTime;");
        	writer.write("var plannercurrday='';");
        	writer.write("var plannercurrhour='';");
        	writer.write("var plannercurrmin='';");
        	writer.write("var plannermode=0;");
        	writer.write("var plannerIsDown=false;");
        	writer.write("var plannerTimer;");
        	writer.write("var PlannerLastBtnType;");
        	writer.write("var plannerSleep;");
        	writer.write("var plannerSleeping=false;");
        	
        	writer.write("function InitializeWeekviews() {");
        	writer.write("ResizePlanner();");

        	writer.write("$('.abmday').off('mouseup touchend').on('mouseup touchend', function(event) {");
        	writer.write("plannercurrday = '';");
        	writer.write("plannercurrhour = '';");
        	writer.write("plannerccpmenu.retract(0);");
        	writer.write("if (plannerSleeping) return;");
        	writer.write("if (abmdragging) return;");
        	writer.write("var el = $(this);");
        	writer.write("var parentel = el.closest('.abmplannerwrapper');");
        	writer.write("var parent = parentel.attr('id');");
        	writer.write("var ename = parentel.attr('eName');");
        	writer.write("if (parentel.data('viewtype')=='nw') {");
        	writer.write("if (el.hasClass('abmactivenw')) {");
        	writer.write("if ($(\".plannernamenu-container\").css('display')=='none') {");
        	writer.write("plannercurrday = $(this).attr('data-dayn');");
        	writer.write("plannernamenu.expand(el, event);");
        	writer.write("} else {");
        	writer.write("plannernamenu.retract(150);");
        	writer.write("}");
        	writer.write("} else {");
        	writer.write("plannernamenu.retract(0);");
        	writer.write("var day=el.data('day');");
        	writer.write("var act = $('#' + parent).find('.abmactivenw');");
        	writer.write("act.find('.abmhour').removeClass('abmactivenw');");
        	writer.write("act.find('.abmwho').addClass('abmhidden');");
        	writer.write("act.removeClass('abmactivenw');");
        	writer.write("var dayel = $('#' + parent).find('[data-day=\"' + day + '\"]');");
        	writer.write("dayel.addClass('abmactivenw');");
        	writer.write("dayel.find('.abmwho').removeClass('abmhidden');");
        	writer.write("dayel.find('.abmhour').addClass('abmactivenw');");        	
        	writer.write("var days={'MA': 0,'DI': 1,'WO': 2,'DO': 3,'VR': 4,'ZA': 5,'ZO': 6};");
        	writer.write("planners[parent]=days[day];");
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': ename + '_activedaychanged',");
        	writer.write("'eventparams': 'day',");
        	writer.write("'day': days[day]");
        	writer.write("});");        	
        	writer.write("}");
        	writer.write("} else {");
        	writer.write("if (el.hasClass('abmactive')) {");
        	writer.write("if ($(\".plannernamenu-container\").css('display')=='none') {");
        	writer.write("plannercurrday = $(this).attr('data-dayn');");
        	writer.write("plannernamenu.expand(el, event);");
        	writer.write("} else {");
        	writer.write("plannernamenu.retract(150);");
        	writer.write("}");
        	writer.write("} else {");
        	writer.write("plannernamenu.retract(0);");
        	writer.write("var day=el.data('day');");
        	writer.write("var act = $('#' + parent).find('.abmactive');");
        	writer.write("act.find('.abmhour').removeClass('abmactive');");
        	writer.write("act.find('.abmwho').addClass('abmhidden');");
        	writer.write("act.removeClass('abmactive');");
        	writer.write("var dayel = $('#' + parent).find('[data-day=\"' + day + '\"]');");
        	writer.write("dayel.addClass('abmactive');");
        	writer.write("dayel.find('.abmwho').removeClass('abmhidden');");
        	writer.write("dayel.find('.abmhour').addClass('abmactive');");        	
        	writer.write("var days={'MA': 0,'DI': 1,'WO': 2,'DO': 3,'VR': 4,'ZA': 5,'ZO': 6};");
        	writer.write("planners[parent]=days[day];");
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': ename + '_activedaychanged',");
        	writer.write("'eventparams': 'day',");
        	writer.write("'day': days[day]");
        	writer.write("});");        	
        	writer.write("}");
        	writer.write("}");
        	writer.write("PositionPlanner(el, parent);");
        	writer.write("if (event.stopPropagation) {");
            writer.write("event.stopPropagation();");
            writer.write("} else if (window.event) {");
        	writer.write("window.event.cancelBubble = true;");
            writer.write("}");
            writer.write("event.preventDefault();");
        	writer.write("});");   
        	
        	writer.write("$('.abmhc').off('mousedown touchstart').on('mousedown touchstart', function(e) {");   
        	writer.write("plannercurrday = '';");   
        	writer.write("plannercurrhour = '';");
        	writer.write("plannernamenu.retract(0);");
        	writer.write("if (plannerSleeping) return;");
        	writer.write("if (abmdragging) return;");   
        	writer.write("var el = $(this);");   
        	writer.write("var parentel = el.closest('.abmplannerwrapper');");   
        	writer.write("var parent = parentel.attr('id');");   
        	writer.write("var status = 0;");   
        	writer.write("if (el.closest('.abmhr').hasClass('abmna')) {");   
        	writer.write("status += 1;");   
        	writer.write("};");   
        	writer.write("var value = el.data('val');");   
        	writer.write("var taskid = el.attr('data-id');");   
        	writer.write("if (taskid != '') {");   
        	writer.write("status += 2;");   
        	writer.write("} else {");   
        	writer.write("taskid = 'FREE';");   
        	writer.write("}");   
        	writer.write("var txt = el.find('p').html();");   
        	writer.write("if (plannermode == -1) {");
        	writer.write("plannermode = 0;");
        	writer.write("}");
        	writer.write("if (txt == '&nbsp;') {");
        	writer.write("if (PlannerLastBtnType != 5) {");
        	writer.write("if (plannermode == 0) {");
        	writer.write("plannermode = -1;");
        	writer.write("}");
        	writer.write("}");
        	writer.write("}");
            
        	writer.write("plannercurrmin = value + ';' + status + ';' + taskid;");   
        	writer.write("plannerStartTime = e.timeStamp;");   
        	writer.write("if (plannerTimer) {");   
        	writer.write("clearTimeout(plannerTimer);");   
        	writer.write("}");   
        	writer.write("if (parentel.data('viewtype') == 'nw') {");   
        	writer.write("if (el.closest('.abmplannerdaynw').hasClass('abmactivenw')) {");   
        	writer.write("plannerIsDown = true;");   
        	writer.write("plannerTimer = setTimeout(function () {");   
        	writer.write("if ($(\".plannerccpmenu-container\").css('display') == 'none') {");   
        	writer.write("plannerccpmenu.expand(el, event);");   
        	writer.write("}");   
        	writer.write("}, 350)");   
        	writer.write("} else {");   
        	writer.write("plannerIsDown=false;");   
        	writer.write("}");   
        	writer.write("} else {");   
        	writer.write("if (el.closest('.abmplannerday').hasClass('abmactive')) {");   
        	writer.write("plannerIsDown = true;");   
        	writer.write("plannerTimer = setTimeout(function () {");   
        	writer.write("if ($(\".plannerccpmenu-container\").css('display') == 'none') {");   
        	writer.write("plannerccpmenu.expand(el, event);");   
        	writer.write("}");   
        	writer.write("}, 350)");   
        	writer.write("} else {");   
        	writer.write("plannerIsDown=false;");   
        	writer.write("}");   
        	writer.write("}");   
        	writer.write("});\n");
        	
        	writer.write("$('.plannerccpmenu-container').off('mousedown touchstart').on('mousedown touchstart', function(e) {");
        	writer.write("plannerIsDown=false;");		
        	writer.write("});\n");
        	
        	writer.write("$('.plannerccpmenu-container').off('mouseup touchend').on('mouseup touchend', function(e) {");
        	writer.write("if (plannerIsDown==false) {");
        	writer.write("plannerccpmenu.retract(0);");
        	writer.write("}");
        	writer.write("plannerIsDown=false;");
        	writer.write("if (event.stopPropagation) {");
        	writer.write("event.stopPropagation();");
        	writer.write("} else if (window.event) {");
        	writer.write("window.event.cancelBubble = true;");
        	writer.write("}");
        	writer.write("event.preventDefault();");
        	writer.write("});\n");
        	
        	writer.write("$('.abmhc').off('mouseup touchend').on('mouseup touchend', function(e) {");
        	writer.write("plannernamenu.retract(0);");
        	writer.write("if (plannerSleeping) return;");
        	writer.write("if (abmdragging) return;");
        	writer.write("var el = $(this);");
        	writer.write("var parentel = el.closest('.abmplannerwrapper');");
        	writer.write("var parent = parentel.attr('id');");
        	writer.write("var ename = parentel.attr('eName');");
        	writer.write("plannerIsDown = false;");
        	writer.write("if (parentel.data('viewtype') == 'nw') {");
        	writer.write("if (el.find('.abmhidden').length != 0) {");
        	writer.write("plannerccpmenu.retract(0);");
        	writer.write("var day = el.closest('.abmplannerdaynw').data('day');");
        	writer.write("var act = $('#' + parent).find('.abmactivenw');");
        	writer.write("act.find('.abmhour').removeClass('abmactivenw');");
        	writer.write("act.find('.abmwho').addClass('abmhidden');");
        	writer.write("act.removeClass('abmactivenw');");
        	writer.write("var dayel = $('#' + parent).find('[data-day=\"' + day + '\"]');");
        	writer.write("dayel.addClass('abmactivenw');");
        	writer.write("dayel.find('.abmwho').removeClass('abmhidden');");
        	writer.write("dayel.find('.abmhour').addClass('abmactivenw');");
        	
        	writer.write("var days={'MA': 0,'DI': 1,'WO': 2,'DO': 3,'VR': 4,'ZA': 5,'ZO': 6};");
        	writer.write("planners[parent]=days[day];");
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': ename + '_activedaychanged',");
        	writer.write("'eventparams': 'day',");
        	writer.write("'day': days[day]");
        	writer.write("});");
        	
        	writer.write("} else {");
        	writer.write("if (((e.timeStamp - plannerStartTime) < 350)) {");
        	writer.write("if (plannerTimer) {");
        	writer.write("clearTimeout(plannerTimer);");
        	writer.write("}");
        	writer.write("if ($(\".plannerccpmenu-container\").attr('isopen') == 'true') {");
        	writer.write("plannerccpmenu.retract(0);");
        	writer.write("}");
        	writer.write("var status = 0;");
        	writer.write("if (el.closest('.abmhr').hasClass('abmna')) {");
        	writer.write("status += 1;");
        	writer.write("};");
        	writer.write("var value = el.data('val');");
        	writer.write("var taskid = el.attr('data-id');");
        	writer.write("if (taskid != '') {");
        	writer.write("status += 2;");
        	writer.write("} else {");
        	writer.write("taskid = 'FREE';");
        	writer.write("}");
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': ename + '_minutesclicked',");
        	writer.write("'eventparams': 'value',");
        	writer.write("'value': '0;' + value + ';' + status + ';' + taskid");
        	writer.write("});");
        	writer.write("}");
        	writer.write("}");
        	writer.write("} else {");
        	writer.write("if (el.find('.abmhidden').length != 0) {");
        	writer.write("plannerccpmenu.retract(0);");
        	writer.write("var day = el.closest('.abmplannerday').data('day');");
        	writer.write("var act = $('#' + parent).find('.abmactive');");
        	writer.write("act.removeClass('abmactive');");
        	writer.write("act.find('.abmwho').addClass('abmhidden');");
        	writer.write("act.removeClass('abmactive');");
        	writer.write("var dayel = $('#' + parent).find('[data-day=\"' + day + '\"]');");
        	writer.write("dayel.addClass('abmactive');");
        	writer.write("dayel.find('.abmwho').removeClass('abmhidden');");
        	writer.write("dayel.find('.abmhour').addClass('abmactive');");        	
        	writer.write("var days={'MA': 0,'DI': 1,'WO': 2,'DO': 3,'VR': 4,'ZA': 5,'ZO': 6};");
        	writer.write("planners[parent]=days[day];");
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': ename + '_activedaychanged',");
        	writer.write("'eventparams': 'day',");
        	writer.write("'day': days[day]");
        	writer.write("});");        	
        	writer.write("} else {");
        	writer.write("if (((e.timeStamp - plannerStartTime) < 350)) {");
        	writer.write("if (plannerTimer) {");
        	writer.write("clearTimeout(plannerTimer);");
        	writer.write("}");
        	writer.write("if ($(\".plannerccpmenu-container\").attr('isopen') == 'true') {");
        	writer.write("plannerccpmenu.retract(0);");
        	writer.write("}");
        	writer.write("var status = 0;");
        	writer.write("if (el.closest('.abmhr').hasClass('abmna')) {");
        	writer.write("status += 1;");
        	writer.write("};");
        	writer.write("var value = el.data('val');");
        	writer.write("var taskid = el.attr('data-id');");
        	writer.write("if (taskid != '') {");
        	writer.write("status += 2;");
        	writer.write("} else {");
        	writer.write("taskid = 'FREE';");
        	writer.write("}");
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': ename + '_minutesclicked',");
        	writer.write("'eventparams': 'value',");
        	writer.write("'value': '0;' + value + ';' + status + ';' + taskid");
        	writer.write("});");
        	writer.write("}");				
        	writer.write("}");
        	writer.write("}");
        	writer.write("PositionPlanner(el, parent);");
        	writer.write("if (event.stopPropagation) {");
        	writer.write("event.stopPropagation();");
        	writer.write("} else if (window.event) {");
        	writer.write("window.event.cancelBubble = true;");
        	writer.write("}");
        	writer.write("event.preventDefault();");
        	writer.write("});\n");
        	
        	writer.write("$('.abmhour').off('mouseup touchend').on('mouseup touchend', function(event) {");
        	writer.write("plannerccpmenu.retract(0);");
        	writer.write("if (plannerSleeping) return;");
        	writer.write("plannercurrday = '';");
        	writer.write("plannercurrhour = '';");
        	writer.write("if (abmdragging) return;");
        	writer.write("var el = $(this);");
        	writer.write("var parentel = el.closest('.abmplannerwrapper');");
        	writer.write("var parent = parentel.attr('id');");
        	writer.write("var ename = parentel.attr('eName');");
        	writer.write("if (parentel.data('viewtype') == 'nw') {");
        	writer.write("if (el.hasClass('abmactivenw')) {");
        	writer.write("if ($(\".plannernamenu-container\").css('display')=='none') {");
        	writer.write("plannercurrhour = $(this).attr('data-dayhr');");
        	writer.write("plannernamenu.expand(el, event);");
        	writer.write("} else {");
        	writer.write("plannernamenu.retract(150);");
        	writer.write("}");
        	writer.write("} else {");
        	writer.write("plannernamenu.retract(0);");
        	writer.write("var day = el.closest('.abmplannerdaynw').data('day');");
        	writer.write("var act = $('#' + parent).find('.abmactivenw');");
        	writer.write("act.find('.abmhour').removeClass('abmactivenw');");
        	writer.write("act.find('.abmwho').addClass('abmhidden');");
        	writer.write("act.removeClass('abmactivenw');");
        	writer.write("var dayel = $('#' + parent).find('[data-day=\"' + day + '\"]');");
        	writer.write("dayel.addClass('abmactivenw');");
        	writer.write("dayel.find('.abmwho').removeClass('abmhidden');");
        	writer.write("dayel.find('.abmhour').addClass('abmactivenw');");          	
        	writer.write("var days={'MA': 0,'DI': 1,'WO': 2,'DO': 3,'VR': 4,'ZA': 5,'ZO': 6};");
        	writer.write("planners[parent]=days[day];");
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': ename + '_activedaychanged',");
        	writer.write("'eventparams': 'day',");
        	writer.write("'day': days[day]");
        	writer.write("});");        	
        	writer.write("}");
        	writer.write("} else {");
        	writer.write("if (el.hasClass('abmactive')) {");
        	writer.write("if ($(\".plannernamenu-container\").css('display')=='none') {");
        	writer.write("plannercurrhour = $(this).attr('data-dayhr');");
        	writer.write("plannernamenu.expand(el, event);");
        	writer.write("} else {");
        	writer.write("plannernamenu.retract(150);");
        	writer.write("}");
        	writer.write("} else {");
        	writer.write("plannernamenu.retract(0);");
        	writer.write("var day = el.closest('.abmplannerday').data('day');");
        	writer.write("var act = $('#' + parent).find('.abmactive');");
        	writer.write("act.find('.abmhour').removeClass('abmactive');");
        	writer.write("act.find('.abmwho').addClass('abmhidden');");
        	writer.write("act.removeClass('abmactive');");
        	writer.write("var dayel = $('#' + parent).find('[data-day=\"' + day + '\"]');");
        	writer.write("dayel.addClass('abmactive');");
        	writer.write("dayel.find('.abmwho').removeClass('abmhidden');");
        	writer.write("dayel.find('.abmhour').addClass('abmactive');");        	
        	writer.write("var days={'MA': 0,'DI': 1,'WO': 2,'DO': 3,'VR': 4,'ZA': 5,'ZO': 6};");
        	writer.write("planners[parent]=days[day];");
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': ename + '_activedaychanged',");
        	writer.write("'eventparams': 'day',");
        	writer.write("'day': days[day]");
        	writer.write("});");        	
        	writer.write("}");
        	writer.write("}");
        	writer.write("PositionPlanner(el, parent);");
        	writer.write("if (event.stopPropagation) {");
            writer.write("event.stopPropagation();");
            writer.write("} else if (window.event) {");
        	writer.write("window.event.cancelBubble = true;");
            writer.write("}");
            writer.write("event.preventDefault();");
        	writer.write("});\n");
        	writer.write("}\n");
        	
        	writer.write("function PositionPlanner(el, parent) {");
        	writer.write("var scr = el.closest('.abmplannerwrapper');");
        	writer.write("var wrleft = scr.position().left;");
        	writer.write("scr = scr.find('.abmplannerscroller');");	
        	writer.write("if (!el.hasClass('abday')) {");
        	writer.write("el = el.closest('[data-day]');");
        	writer.write("var day = el.data('day');");
        	writer.write("el = $('#' + parent).find('[data-day=\"' + day + '\"]');");
        	writer.write("}");
        	writer.write("scr.scrollLeft(Math.abs(el.position().left+scr.scrollLeft())-wrleft);");
        	writer.write("}");
        	
        	writer.write("function ResizePlanner() {");
        	writer.write("$('.abmplannerwrapper').each(function() {");
        	writer.write("var planid = $(this).attr('id');");
        	writer.write("var parWidth=$('#' + planid + 'daywrapper').parent().parent().parent().width();");        		
        	writer.write("if (Math.max( $(window).width(), window.innerWidth)>=" + (ABMaterial.ThresholdPxConsiderdSmall+201) + ") {");
        	writer.write("$('#' + planid + 'daywrapper').width(parWidth-27);");
        	writer.write("$('#' + planid + 'daywrapper').css('margin-left','0px');");
        	writer.write("$('#' + planid + 'planner').width(parWidth-25);");
        	writer.write("$('#' + planid + 'plannerparent').width(parWidth);");
        	writer.write("$('#' + planid + 'plannerparent').off('scroll');");
        	writer.write("} else {");
        	writer.write("$('#' + planid + 'daywrapper').width(parWidth*2);");
        	writer.write("$('#' + planid + 'planner').width(parWidth*2);");
        	writer.write("$('#' + planid + 'plannerparent').width(parWidth);");
        	writer.write("$('#' + planid + 'plannerparent').off('scroll').on('scroll', function() {");
        	writer.write("$('#' + planid + 'daywrapper').css('margin-left',-$('#' + planid + 'plannerparent').scrollLeft() + 'px');");
        	writer.write("plannernamenu.retract(0);");
        	writer.write("plannerccpmenu.retract(0);");
        	writer.write("});");
        	writer.write("}");
        	writer.write("});");
        	writer.write("}\n");        	

        	writer.write("function SPDE(parent, day) {");
        	writer.write("var ocss = $('#' + parent).find('[data-day=\"' + day + '\"]').find('.abmocc');");
        	writer.write("ocss.find(\"p\").html('&nbsp;');");
        	writer.write("ocss.attr('data-id', '');");
        	writer.write("ocss.attr('data-th', '');");  
        	writer.write("ocss.removeClass('abmocc');");
        	writer.write("for (var k=0;k<20;k++) {");
        	writer.write("ocss.removeClass('abmocc' + k);");
        	writer.write("}");        	
        	writer.write("}\n");
     
        	writer.write("function SPE(parent) {");
        	writer.write("SPDE(parent, 'MA');");
        	writer.write("SPDE(parent, 'DI');");
        	writer.write("SPDE(parent, 'WO');");
        	writer.write("SPDE(parent, 'DO');");
        	writer.write("SPDE(parent, 'VR');");
        	writer.write("SPDE(parent, 'ZA');");
        	writer.write("SPDE(parent, 'ZO');");        	
        	writer.write("}\n");

        	writer.write("function SPDNA(parent, day) {");
        	writer.write("var ocss = $('#' + parent).find('[data-day=\"' + day + '\"]').find('.abmhc');"); //.find(\":not('.abmocc')\");");	
        	writer.write("ocss.addClass('abmna');");
        	writer.write("}\n");
        			
        	writer.write("function SPDA(parent, day) {");
        	writer.write("var ocss = $('#' + parent).find('[data-day=\"' + day + '\"]').find('.abmhc');"); //.find(\":not('.abmocc')\");");	
        	writer.write("ocss.removeClass('abmna');");
        	writer.write("}\n");
        	
        	writer.write("function SPHNA(parent, day, hour) {");
        	writer.write("var ocss = $('#' + parent).find('[data-day=\"' + day + '\"]').find('[data-hour=\"' + hour + '\"]').find('.abmhc');"); //.find(\":not('.abmocc')\");");	
        	writer.write("ocss.addClass('abmna');");
        	writer.write("}\n");

        	writer.write("function SPHA(parent, day, hour) {");
        	writer.write("var ocss = $('#' + parent).find('[data-day=\"' + day + '\"]').find('[data-hour=\"' + hour + '\"]').find('.abmhc');"); //.find(\":not('.abmocc')\");");	
        	writer.write("ocss.removeClass('abmna');");
        	writer.write("}\n");
        	
        	writer.write("function PlannerDoAction(parent) {");
        	writer.write("var days=['MA','DI','WO','DO','VR','ZA','ZO'];");
        	writer.write("if (plannerAction!='') {");
        	writer.write("var spl = plannerAction.split(';');");
        	writer.write("switch (spl[0]) {");
        	writer.write("case '1':");
        	writer.write("switch (spl[1]) {");
        	writer.write("case 'day':");
        	writer.write("SPDA(parent, days[spl[2]]);");
        	writer.write("break;");
        	writer.write("case 'hour':");
        	writer.write("SPHA(parent, days[spl[2]], spl[3]);");
        	writer.write("break;");
        	writer.write("}");
        	writer.write("break;");
        	writer.write("case '2':");
        	writer.write("switch (spl[1]) {");
        	writer.write("case 'day':");
        	writer.write("SPDNA(parent, days[spl[2]]);");
        	writer.write("break;");
        	writer.write("case 'hour':");
        	writer.write("SPHNA(parent, days[spl[2]], spl[3]);");
        	writer.write("break;");
        	writer.write("}");
        	writer.write("break;");
        	writer.write("}");
        	writer.write("}");
        	writer.write("}\n");
        	
        	writer.write("function isInputDirSupported() {");
        	writer.write("var tmpInput = document.createElement('input');");
        	writer.write("if ('webkitdirectory' in tmpInput ");
        	writer.write("|| 'mozdirectory' in tmpInput"); 
        	writer.write("|| 'odirectory' in tmpInput ");
        	writer.write("|| 'msdirectory' in tmpInput ");
        	writer.write("|| 'directory' in tmpInput) return true;");
        	writer.write("return false;");
        	writer.write("}\n");
        		
        	
        	writer.write("function SPT(day, id, first, last, text, theme) {");
        	writer.write("var el = document.getElementById(plannerID + 'min' + day + first);");
        	writer.write("el.className += ' abmocc abmocc' + theme + ' ';");
        	writer.write("el.setAttribute('data-id', id);"); 
        	writer.write("el.setAttribute('data-th', theme);");
        	writer.write("document.getElementById(plannerID + 'txt' + day + first).innerHTML = text;");
        	writer.write("for (var i = first + 1; i <= last; i++) {");     
        	writer.write("var el = document.getElementById(plannerID + 'min' + day + i);");
        	writer.write("el.className += ' abmocc abmocc' + theme + ' ';");    
        	writer.write("el.setAttribute('data-id', id);"); 
        	writer.write("el.setAttribute('data-th', theme);");
        	writer.write("document.getElementById(plannerID + 'txt' + day + i).innerHTML = '&larr;';");
        	writer.write("}");
        	writer.write("};\n");

        	writer.write("function SPAD(viewtype, day) {");
        	writer.write("if (oldActiveDay!=planners[plannerID]) {");
        	writer.write("var days=['MA','DI','WO','DO','VR','ZA','ZO'];");
        	writer.write("var f1 = $('#' + plannerID).find('.abmactivenw');");
        	writer.write("var f2 = $('#' + plannerID).find('[data-day=\"' + days[day] + '\"]');");
        	writer.write("if (viewtype=='nw') {");        	
        	writer.write("f1.find('.abmhour').removeClass('abmactivenw');");
        	writer.write("f1.find('.abmwho').addClass('abmhidden');");
        	writer.write("f1.removeClass('abmactivenw');");
        	writer.write("f2.addClass('abmactivenw');");
        	writer.write("f2.find('.abmwho').removeClass('abmhidden');");
        	writer.write("f2.find('.abmhour').addClass('abmactivenw');");
        	writer.write("} else {");
        	writer.write("f1.find('.abmhour').removeClass('abmactive');");
        	writer.write("f1.find('.abmwho').addClass('abmhidden');");
        	writer.write("f1.removeClass('abmactive');");
        	writer.write("f2.addClass('abmactive');");
        	writer.write("f2.find('.abmwho').removeClass('abmhidden');");
        	writer.write("f2.find('.abmhour').addClass('abmactive');");
        	writer.write("}");
        	writer.write("PositionPlanner(f2, plannerID);");
        	writer.write("}");
        	writer.write("};\n");
        }        
        
        writer.write("function reinitmaterialbox(id) {");
    	writer.write("$('#' + id).materialbox();");
    	writer.write("}\n");
    	
    	writer.write("function reinitcollapsable(id) {");
     	writer.write("initcollapse(id);");
     	writer.write("}\n");
     	
     	writer.write("function reinitdropdown(id) {");
     	writer.write("$(\".dropdown-button\").dropdown();");
     	writer.write("}\n");
     	
     	writer.write("function reinitdropdownhover() {");
     	writer.write("$(\".abmicbdg.dropdown-button\").dropdown({'hover': true});");
     	writer.write("}\n");
     	
     	if (NeedsParallax) {
     		writer.write("function reinitparallax(id) {");
     		writer.write("$('#' + id).parallax();");
     		writer.write("}\n");
     	}
     	
     	if (NeedsTabs) {
     		writer.write("function reinittabs(id) {");
     		writer.write("$('#' + id).tabs();");
     		writer.write("}\n");
     	}
     	
     	if (NeedsRange) {
     		writer.write("function GetRangeStart(id,tmpVar) {");
     			writer.write("tmpVar = document.getElementById(id);");
     			writer.write("var elements=tmpVar.noUiSlider.get();");
     			writer.write("return elements[0];");
     		writer.write("};");
     		writer.write("function GetRangeStop(id,tmpVar) {");
 				writer.write("tmpVar = document.getElementById(id);");
 				writer.write("var elements=tmpVar.noUiSlider.get();");
 				writer.write("return elements[1];");
 			writer.write("};");
 			writer.write("function SetRangeStart(id,tmpVar, mStart) {");
 				writer.write("var mStop=GetRangeStop(id,tmpVar);");
 				writer.write("tmpVar = document.getElementById(id);");
 				writer.write("tmpVar.noUiSlider.set([mStart,mStop]);");
 			writer.write("};");
 			writer.write("function SetRangeStop(id,tmpVar, mStop) {");
				writer.write("var mStart=GetRangeStop(id,tmpVar);");
				writer.write("tmpVar = document.getElementById(id);");
				writer.write("tmpVar.noUiSlider.set([mStart,mStop]);");
			writer.write("};");
 			
     	}
     	
     	if (NeedsSlider) {
     		writer.write("function GetSliderValue(id,tmpVar) {");
     			writer.write("tmpVar = document.getElementById(id);");
     			writer.write(" return tmpVar.noUiSlider.get();");     			
     		writer.write("};");
     		writer.write("function SetSliderValue(id,tmpVar, mValue) {");
 				writer.write("tmpVar = document.getElementById(id);");
 				writer.write("tmpVar.noUiSlider.set(mValue);");
 			writer.write("};");
     	}
     	
     	if (NeedsImageSlider) {
     		writer.write("function reinitslider(id,fullwidth,indicators,height,transition,interval) {");    
     		writer.write("$('#' + id).find('li').removeClass('active');");
     		writer.write("$('#' + id).find('.indicator-item').removeClass('active');");
     		writer.write("$('#' + id).find('.indicators').remove();");
     		writer.write("$('#' + id).slider({full_width: fullwidth ,indicators: indicators ,height: height ,transition: transition ,interval: interval});");     		
     		writer.write("}\n");
    	}
      
    	if (NeedsCombo) {
    		writer.write("function reinitcombo(id) {");
    		writer.write("$('#' + id).dropdown2();");
    		writer.write("$('#' + id).off('keyup').on('keyup', function(e) {");
    		writer.write("$(this).attr('value',$(this).val());");
    		writer.write("b4j_raiseEvent('page_parseevent', {");
    		writer.write("'eventname': $(this).attr('evname') + '_changed',");
    		writer.write("'eventparams': 'value',");
    		writer.write("'value': $(this).val()");
    		writer.write("});");
    		writer.write("});");
    		writer.write("}\n");
    	}
     	
     	if (NeedsUpload) {
     		writer.write("function reinituploads() {");
     		writer.write("runupload();");
     		writer.write("}\n");
     	}     	
     	
 		writer.write("function reinitinputfields() {");
 		writer.write("InitEditEnterFields();");
 		writer.write("TextAreaAllResize();");
 		writer.write("$('.dropdown-button2').dropdown2();");     		
 		writer.write("$('.dropdown-button2.raisechanged').off('keyup').on('keyup', function(e) {");
 		writer.write("$(this).attr('value',$(this).val());");
    	writer.write("b4j_raiseEvent('page_parseevent', {");
    	writer.write("'eventname': $(this).attr('evname') + '_changed',");
    	writer.write("'eventparams': 'value',");
    	writer.write("'value': $(this).val()");
    	writer.write("});");
    	writer.write("});");        	
 		writer.write("}\n");
     	     	
     	writer.write("function inittooltipped(id){");
     	writer.write("$('#' + id).tooltip('remove');");
        writer.write("$('#' + id).tooltip({delay:50});");
        writer.write("};");
        
        if (NeedsTabs) {
        	writer.write("function setactivetab(id,tabpage) {");
        	writer.write("$('#' + id).tabs('select_tab', id + tabpage);");
        	writer.write("}\n");
        }
        
        if (NeedsEditor) {
        	writer.write("function geteditorhtml(id) {");
        	writer.write("return $('#' + id + '-iframe').first()[0].contentWindow.editor.getHTML();");
        	writer.write("}\n");
        	        	
        	writer.write("function editorenable(id,enab,hastoolbar) {");
        	writer.write("var x = document.getElementById(id + '-iframe');");
        	writer.write("var y = (x.contentWindow || x.contentDocument);");
        	writer.write("if (y.document)y = y.document;");
        	writer.write("y.body.contentEditable = enab;");            	
        	writer.write("if (enab=='false') {");
        	writer.write("$('#' + id + '-menu').addClass('hide');");
        	writer.write("$('#' + id + '-iframe').attr('abmEnabled', 'false');");
        	writer.write("} else {");
        	writer.write("if (hastoolbar=='false') {");
        	writer.write("$('#' + id + '-menu').addClass('hide');");
        	writer.write("$('#' + id + '-iframe').attr('abmEnabled', 'true');");
        	writer.write("} else {");
        	writer.write("$('#' + id + '-menu').removeClass('hide');");
        	writer.write("$('#' + id + '-iframe').attr('abmEnabled', 'true');");
        	writer.write("}");
        	writer.write("}");
        	writer.write("}\n");
        	
        	writer.write("function editorresize(id) {");
        	writer.write("resizeFrame($('#' + id + '-iframe').first()[0]);");
        	writer.write("}\n");
        }
        
        if (NeedsTreeTable) {
        	writer.write("function treetableaddnode(id, parentid, html) {");
        	writer.write("var node = $('#' + id).treetable('node', parentid);");
        	writer.write("$('#' + id).treetable('loadBranch',node,html);");
        	writer.write("}\n");
        	writer.write("function treetableremovenode(id,rowid) {");
        	writer.write("$('#' + id).treetable('removeNode',rowid);");
        	writer.write("}\n");
        
        	writer.write("function sorttree(id) {");
        	writer.write("$(\"#\" + id + \" .sortabletree\").each(function() {");
        	writer.write("var node = $('#' + id).treetable('node', $(this).attr(\"data-tt-id\"));");
        	writer.write("$('#' + id).treetable('sortBranch', node, (parseInt($(this).attr(\"data-tt-sort\"))+parseInt($(this).attr(\"data-tt-column\"))));");
        	writer.write("});");
        	writer.write("}\n");
        }  
          	
        if (NeedsTreeTable) {
        	writer.write("function reinitclickstreetable(id, revdur) {");
        	writer.write("$(\"#\" + id + \" .abmtreedraggable\").draggable({");
        	writer.write("helper: \"clone\",");
        	writer.write("start: function(e, ui) {");
        	writer.write("var $originals = $(this).children();");
        	writer.write("ui.helper.children().each(function(index) {");
        	writer.write("$(this).width($originals.eq(index).width())");
        	writer.write("});");
        	writer.write("},");
        	writer.write("opacity: .55,");
        	writer.write("refreshPositions: true,");
        	writer.write("revert: \"invalid\",");
        	writer.write("revertDuration: revdur,");
        	writer.write("scroll: true");
        	writer.write("});");
       
        	writer.write("$(\"#\" + id + \" .abmtreedroppable\").each(function() {");
        	writer.write("$(this).droppable({");
        	writer.write("accept: \".abmtreedraggable\",");
        	writer.write("drop: function(e, ui) {");
        	writer.write("var droppedEl = ui.draggable;");
        	writer.write("if (droppedEl.is(\"[data-tt-drag]\") && $(this).is(\"[data-tt-drops]\")) {");
        	writer.write("var tst = $(this).attr(\"data-tt-drops\");");
        	writer.write("var treerowid = droppedEl.attr(\"data-tt-id\");");
        	writer.write("var fromtreerowid = droppedEl.attr(\"data-tt-parent-id\");");
        	writer.write("var ontreerowid = $(this).attr(\"data-tt-id\");");
        	writer.write("var tablename = $('#' + id).attr('name');");
        	writer.write("var evname = $('#' + id).attr('evname');"); //160403
        	writer.write("var footerid = droppedEl.attr(\"data-tt-footer\") || \"\";");
        	writer.write("if (tst.indexOf(\",\" + droppedEl.attr(\"data-tt-drag\") + \",\") > -1 && (parseInt(droppedEl.attr(\"data-tt-column\")) - 1) == parseInt($(this).attr(\"data-tt-column\")))  {");
        	writer.write("ui.draggable.draggable('option', 'revert', false);");
        	writer.write("if (this != droppedEl[0] && !$(this).is(\".expanded\")) {");
        	writer.write("$(\"#\" + id).treetable(\"expandNode\", $(this).data(\"ttId\"));");
        	writer.write("}");
        	writer.write("$(\"#\" + id).treetable(\"move\", droppedEl.data(\"ttId\"), $(this).data(\"ttId\"));");
        	writer.write("$(\"[data-tt-id=\" + treerowid + \"]\").attr(\"data-tt-parent-id\", ontreerowid);");
        	writer.write("if (footerid.length>0) {");
        	writer.write("var footer = $(\"[data-tt-id=\" + footerid + \"]\");");
        	writer.write("$(\"#\" + id).treetable(\"move\", footer.data(\"ttId\"), $(this).data(\"ttId\"));");
        	writer.write("$(\"[data-tt-id=\" + footerid + \"]\").attr(\"data-tt-parent-id\", ontreerowid);");
        	writer.write("}");
        	writer.write("sorttree(id);");
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': evname + '_dropped',"); //160403
        	writer.write("'eventparams': 'success,treerowid,fromtreerowid,ontreerowid',");
        	writer.write("'success': true,");
        	writer.write("'treerowid': treerowid,");
        	writer.write("'fromtreerowid': fromtreerowid,");
        	writer.write("'ontreerowid': ontreerowid");
        	writer.write("});");
        	writer.write("} else {");
        	writer.write("ui.draggable.draggable('option', 'revert', true);");
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': evname + '_dropped',"); //160403
        	writer.write("'eventparams': 'success,treerowid,fromtreerowid,ontreerowid',");
        	writer.write("'success': false,");
        	writer.write("'treerowid': treerowid,");
        	writer.write("'fromtreerowid': fromtreerowid,");
        	writer.write("'ontreerowid': ontreerowid");
        	writer.write("});");
        	writer.write("}");
        	writer.write("}");
        	writer.write("}");
        	writer.write("});");
        	writer.write("});");
    
        	writer.write("$('#' + id + ' td').off('click').on('click', function(e) {");
        	writer.write("$(this).closest('tbody').children('tr').each(function() {");
        	writer.write("$(this).removeClass(\"abmselected\");");
        	writer.write("});");
        	writer.write("$(this).closest('tr').addClass(\"abmselected\");");            
        	writer.write("if ($(this).attr('id').slice(0, 5) == 'empty') {");
        	writer.write("return;");
        	writer.write("}");
                    
        	writer.write("if ($(this).closest('tr').is(\"[data-tt-drag]\")) {");
        	writer.write("var tablename = $('#' + id).attr('name');");
        	writer.write("var evname = $('#' + id).attr('evname');"); //160403
        	writer.write("var currthis = $(this);");
        	writer.write("var treerowid = $(this).closest('tr').attr('data-tt-id');");
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': evname + '_clicked',"); // 160403
        	writer.write("'eventparams': 'treerowid,treecellid',");
        	writer.write("'treerowid': treerowid,");
        	writer.write("'treecellid': currthis.attr('id')");
        	writer.write("});");
        	writer.write("return;");
        	writer.write("}");
        	writer.write("var tablename = $('#' + id).attr('name');");
        	writer.write("var currthis = $(this);");
        	writer.write("var treerowid = $(this).closest('tr').attr('data-tt-id');");
        	writer.write("if (e.stopPropagation) {");
        	writer.write("e.stopPropagation();");
        	writer.write("} else if (window.event) {");
        	writer.write("window.event.cancelBubble = true;");
        	writer.write("}");
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': evname + '_clicked',"); //160403
        	writer.write("'eventparams': 'treerowid,treecellid',");
        	writer.write("'treerowid': treerowid,");
        	writer.write("'treecellid': currthis.attr('id')");
        	writer.write("});");
        	writer.write("});");
        	writer.write("}");
        }

        if (NeedsTreeTable || NeedsTable) {
        	writer.write("function inittable(id, hasscroll, sortable, collapsable) {");
        	writer.write("if (collapsable) {");
        	writer.write("$('#' + id).treetable({");
        	writer.write("expandable: true,");
        	writer.write("stringCollapse: $('#' + id).attr('colstring'),");
        	writer.write("stringExpand: $('#' + id).attr('expstring')");
        	writer.write("});");            
        	writer.write("$(this).closest('tbody').children('tr').each(function() {");
        	writer.write("$(this).removeClass(\"abmselected\");");
        	writer.write("});");
        	writer.write("$(this).closest('tr').addClass(\"abmselected\");");            
        	writer.write("$('#' + id + ' td').off('click').on('click', function(e) {");
        	writer.write("if ($(this).attr('id').slice(0, 5) == 'empty') {");
        	writer.write("return;");
        	writer.write("}");            
        	writer.write("if ($(this).closest('tr').is(\"[data-tt-drag]\")) {");
        	writer.write("var tablename = $('#' + id).attr('name');");
        	writer.write("var evname = $('#' + id).attr('evname');"); //160403
        	writer.write("var currthis = $(this);");
        	writer.write("var treerowid = $(this).closest('tr').attr('data-tt-id');");
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': evname + '_clicked',"); //160403
        	writer.write("'eventparams': 'treerowid,treecellid',");
        	writer.write("'treerowid': treerowid,");
        	writer.write("'treecellid': currthis.attr('id')");
        	writer.write("});");
        	writer.write("return;");
        	writer.write("}");
        	writer.write("var tablename = $('#' + id).attr('name');");
        	writer.write("var currthis = $(this);");
        	writer.write("var treerowid = $(this).closest('tr').attr('data-tt-id');");
        	writer.write("if (e.stopPropagation) {");
        	writer.write("e.stopPropagation();");
        	writer.write("} else if (window.event) {");
        	writer.write("window.event.cancelBubble = true;");
        	writer.write("}");
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': evname + '_clicked',"); //160403
        	writer.write("'eventparams': 'treerowid,treecellid',");
        	writer.write("'treerowid': treerowid,");
        	writer.write("'treecellid': currthis.attr('id')");
        	writer.write("});");
        	writer.write("});");
        	writer.write("return;");
        	writer.write("}");
        	writer.write("if (sortable) {");
        	writer.write("sorttable.makeSortable(document.getElementById(id))");
        	writer.write("}");
        	writer.write("if (hasscroll) {");
        	writer.write("jQuery('#' + id).ABMTableScroll();");
        	writer.write("}");
        	writer.write("if ($('#' + id).hasClass('tableinteractive')) {");
        	// NEW ALAIN FOR ABMTableMutable add off
        	writer.write("$('#' + id + ' tbody td').off('click').on('click', function(e) {");
        	writer.write("var tablename = $('#' + id).attr('name');");
        	writer.write("var evname = $('#' + id).attr('evname');"); //160403
        	writer.write("var currthis = $(this);");
        	writer.write("$('#' + tablename + ' tr').children('td').each(function() {");
        	writer.write("$(this).removeClass(\"selected\");");
        	writer.write("});");
        	writer.write("currthis.closest('tr').children('td').each(function() {");
        	writer.write("$(this).addClass(\"selected\");");
        	writer.write("});");
        	writer.write("if (e.stopPropagation) {");
        	writer.write("e.stopPropagation();");
        	writer.write("} else if (window.event) {");
        	writer.write("window.event.cancelBubble = true;");
        	writer.write("}");

        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': evname + '_clicked',"); //160403
        	writer.write("'eventparams': 'abmistable,target',");
        	writer.write("'abmistable': 'abmistable',");
        	writer.write("'target': currthis.attr('id')");
        	writer.write("});");
        	writer.write("});");
        	// NEW ALAIN FOR ABMTableMutable add off
        	writer.write("$('#' + id + ' th').off('click').on('click', function(e) {");
        	writer.write("var tablename = $('#' + id).attr('name');");
        	writer.write("var evname = $('#' + id).attr('evname');"); //160403
        	writer.write("var currthis = $(this);");
        	writer.write("var order = 'ASC';");
        	writer.write("if ($(this).hasClass('sorttable_nosort')) {");
        	writer.write("return;");
        	writer.write("}");
        	writer.write("if ($(this).hasClass('sorttable_sorted')) {");
        	writer.write("order = 'DESC';");
        	writer.write("}");
        	writer.write("if ($(this).hasClass('sorttable_sorted_reverse')) {");
        	writer.write("order = 'ASC';");
        	writer.write("}");
        	writer.write("if (e.stopPropagation) {");
        	writer.write("e.stopPropagation();");
        	writer.write("} else if (window.event) {");
        	writer.write("window.event.cancelBubble = true;");
        	writer.write("}");
        	writer.write("e.preventDefault();");
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': evname + '_sortchanged',"); //160403
        	writer.write("'eventparams': 'datafield,order',");
        	writer.write("'datafield': currthis.attr('fieldname'),");
        	writer.write("'order': order");
        	writer.write("});");
        	writer.write("});");
        	writer.write("$('#' + id + ' tbody tr').off('focus').on('focus', function(e) {");
        	writer.write("var tablename = $('#' + id).attr('name');");
        	writer.write("var evname = $('#' + id).attr('evname');");
        	writer.write("var currthis = $(this);");
        	writer.write("$('#' + tablename + ' tr').children('td').each(function() {");
        	writer.write("$(this).removeClass(\"selected\");");
        	writer.write("});");
        	writer.write("currthis.closest('tr').children('td').each(function() {");
        	writer.write("$(this).addClass(\"selected\");");
        	writer.write("});");
        	writer.write("if (e.stopPropagation) {");
        	writer.write("e.stopPropagation();");
        	writer.write("} else if (window.event) {");
        	writer.write("window.event.cancelBubble = true;");
        	writer.write("}");
        	writer.write("});");
        	writer.write("}");  
        	writer.write("$('[contenteditable=true]')");
        	writer.write(".focus(function() {");
        	writer.write("$(this).data('initialText', $(this).html());");
        	writer.write("var el = document.getElementById($(this).attr('id'));");
        	writer.write("requestAnimationFrame(function() {selectElementContents(el);});");
        	writer.write("});");
        	writer.write("$('[contenteditable=true]')");
        	writer.write(".blur(function() {");
        	writer.write("if ($(this).data('initialText') !== $(this).html()) {");
        	writer.write("var sp = $(this).attr('id').split('_');");
        	writer.write("var row = sp[1];");
        	writer.write("var col = sp[2];");
        	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': $(this).attr('evname') + '_changed', 'eventparams': 'row,column,value', 'row': row, 'column': col, 'value': $(this).html()});");		
        	writer.write("}");
        	writer.write("});");    	
        	writer.write("};");	
        	
        	writer.write("function inittablescr(id, draggable) {");
        	writer.write("if (draggable) {");
        	writer.write("var dragElem=null;");
        	writer.write("$('#' + id + '-header').dragtable({");
        	writer.write("maxMovingRows:1,");
        	writer.write("id: id,");
        	writer.write("dragaccept: '.abminfdrag',");
        	writer.write("beforeMoving: function(ui) {");
        	writer.write("dragElem=$('#' + id + '-header > thead > tr > th:eq(' + (ui.startIndex-1) + ')');");
        	writer.write("},");
        	writer.write("beforeStop: function(ui) {");		
        	writer.write("var toElem=$('#' + id + '-header > thead > tr > th:eq(' + (ui.endIndex) + ')');");			
        	writer.write("var tablename = $('#' + id + '-body').attr('name');");
        	writer.write("var evname = $('#' + id + '-body').attr('evname');");	
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': evname + '_columnmoved',");
        	writer.write("'eventparams': 'drag,droppedbefore',");
        	writer.write("'drag': dragElem.attr('fieldname'),");
        	writer.write("'droppedbefore': toElem.attr('fieldname')");
        	writer.write("});");        	
        	writer.write("}");		
        	writer.write("});");
        	writer.write("}");        	
        	writer.write("$('.abminfclick').off('mousedown').on('mousedown', function(e) {");
        	writer.write("if (e.stopPropagation) {");
        	writer.write("e.stopPropagation();");
        	writer.write("} else if (window.event) {");
        	writer.write("window.event.cancelBubble = true;");
        	writer.write("}");
        	writer.write("var tablename = $('#' + id + '-header').attr('name');");
        	writer.write("var evname = $('#' + id + '-header').attr('evname');");
        	writer.write("var currthis = $(this).parent();");
        	writer.write("var order='ASC';");
        	writer.write("if (currthis.hasClass('abminfdesc')) {");
        	writer.write("order = 'DESC';");
        	writer.write("}");
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': evname + '_sortchanged',");
        	writer.write("'eventparams': 'datafield,order',");
        	writer.write("'datafield': currthis.attr('fieldname'),");
        	writer.write("'order': order");
        	writer.write("});");
        	writer.write("});");        	
        	writer.write("$('.abminfclickable').off('mousedown').on('mousedown', function(e) {");
        	writer.write("if (e.stopPropagation) {");
        	writer.write("e.stopPropagation();");
        	writer.write("} else if (window.event) {");
        	writer.write("window.event.cancelBubble = true;");
        	writer.write("}");
        	writer.write("var tablename = $('#' + id + '-header').attr('name');");
        	writer.write("var evname = $('#' + id + '-header').attr('evname');");
        	writer.write("var currthis = $(this);");        	
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': evname + '_headerclicked',");
        	writer.write("'eventparams': 'datafield',");
        	writer.write("'datafield': currthis.attr('fieldname')");
        	writer.write("});");
        	writer.write("});");         	
        	writer.write("if ($('#' + id + '-body').hasClass('tableinteractive')) {");
        	writer.write("$('#' + id + '-body tbody tr td').off('click').on('click', function(e) {");
        	writer.write("var tablename = $('#' + id + '-body').attr('name');");
        	writer.write("var evname = $('#' + id + '-body').attr('evname');");
        	writer.write("var currthis = $(this);");
        	writer.write("$('#' + id + '-body tbody tr').children('td').each(function() {");
        	writer.write("$(this).removeClass('selected');");
        	writer.write("});");
        	writer.write("currthis.closest('tr').children('td').each(function() {");
        	writer.write("$(this).addClass('selected');");
        	writer.write("});");
        	writer.write("if (e.stopPropagation) {");
        	writer.write("e.stopPropagation();");
        	writer.write("} else if (window.event) {");
        	writer.write("window.event.cancelBubble = true;");
        	writer.write("}");
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': evname + '_clicked',");
        	writer.write("'eventparams': 'abmistable,target',");
        	writer.write("'abmistable': 'abmistable',");
        	writer.write("'target': currthis.attr('id')");
        	writer.write("});");
        	writer.write("});"); 
        	writer.write("$('#' + id + '-body tr').off('focus').on('focus', function(e) {");
        	writer.write("var tablename = $('#' + id).attr('name');");
        	writer.write("var evname = $('#' + id).attr('evname');");
        	writer.write("var currthis = $(this);");
        	writer.write("$('#' + id + '-body tr').children('td').each(function() {");
        	writer.write("$(this).removeClass('selected');");
        	writer.write("});");
        	writer.write("currthis.closest('tr').children('td').each(function() {");
        	writer.write("$(this).addClass('selected');");
        	writer.write("});");
        	writer.write("if (e.stopPropagation) {");
        	writer.write("e.stopPropagation();");
        	writer.write("} else if (window.event) {");
        	writer.write("window.event.cancelBubble = true;");
        	writer.write("}");
        	writer.write("});");
        	writer.write("}");
        	writer.write("$('[contenteditable=true]').focus(function() {");
        	writer.write("$(this).data('initialText', $(this).html());");
        	writer.write("var el = document.getElementById($(this).attr('id'));");
        	writer.write("requestAnimationFrame(function() {");
        	writer.write("selectElementContents(el);");
        	writer.write("});");
        	writer.write("});");
        	writer.write("$('[contenteditable=true]').blur(function() {");
        	writer.write("if ($(this).data('initialText') !== $(this).html()) {");
        	writer.write("var sp = $(this).attr('id').split('_');");
        	writer.write("var row = sp[1];");
        	writer.write("var col = sp[2];");
        	writer.write("b4j_raiseEvent('page_parseevent', {");
        	writer.write("'eventname': $(this).attr('evname') + '_changed',");
        	writer.write("'eventparams': 'row,column,value',");
        	writer.write("'row': row,");
        	writer.write("'column': col,");
        	writer.write("'value': $(this).html()");
        	writer.write("});");
        	writer.write("}");
        	writer.write("});");
        	writer.write("syncHeaderFooter(id);");
        	writer.write("$('#' + id + '-body').parent()[0].onscroll = function() {");
        	writer.write("var lastTopElement = lastTopElements[id];");
        	writer.write("var hh = $('#' + id + '-header').height();");
        	writer.write("var scrleft = $('#' + id + '-body').parent().scrollLeft() * -1;");
        	writer.write("$('#' + id + '-header').parent().css('margin-left', scrleft);");
        	writer.write("$('#' + id + '-footer').parent().css('margin-left', scrleft);");
        	writer.write("var table = $('#' + id + '_tbody');");
        	writer.write("var innerRows = table[0].rows;");
        	writer.write("var done = false;");
        	writer.write("for (var i = 0; i < innerRows.length && !done; i++) {");
        	writer.write("if (innerRows[i].offsetTop + (table.position().top - (hh / 3 * 2)) > 0) {");
        	writer.write("done = true;");
        	writer.write("var innerCells = innerRows[i].getElementsByTagName('td');");
        	writer.write("for (var j = 0; j <= innerCells.length; j++) {");
        	writer.write("if ($(innerCells[j]).hasClass('abmtmsame')) {");
        	writer.write("$(innerCells[j]).addClass('abmtmsameactive');");
        	writer.write("}");
        	writer.write("}");
        	writer.write("if (lastTopElement != null && lastTopElement != innerRows[i]) {");
        	writer.write("innerCells = lastTopElement.getElementsByTagName('td');");
        	writer.write("for (var j = 0; j <= innerCells.length; j++) {");
        	writer.write("$(innerCells[j]).removeClass('abmtmsameactive');");
        	writer.write("}");
        	writer.write("}");
        	writer.write("lastTopElements[id] = innerRows[i];");
        	writer.write("}");
        	writer.write("}");
        	writer.write("};");
        	writer.write("};");
        	        	
        	writer.write("var lastTopElements={};");
        	
        	writer.write("function syncHeaderFooters() {");	
        	writer.write("$('.abmscrollltbl').each(function() {");	
        	writer.write("var id = $(this).attr('id');");	
        	writer.write("syncHeaderFooter(id.substring(0,id.length - 4));");	
        	writer.write("});");	
        	writer.write("}");	

        	writer.write("function syncHeaderFooter(id) {");
        	writer.write("var el = $('#' + id + '-scr');");
        	writer.write("var elheader = $('#' + id + '-header');");
        	writer.write("var elbody = $('#' + id + '-body');");
        	writer.write("var elbody2 = $('#' + id + '_tbody');");
        	writer.write("var elfooter = $('#' + id + '-footer');");
        	writer.write("var i = 0;");
        	writer.write("var bodyw = el.width();");
        	writer.write("elbody2.children('tr.abmch:first').children('td').each(function() {");
        	writer.write("var w = $(this).width();");
        	writer.write("$(elheader.first().find('th')[i]).css({'min-width': (w+10) + 'px'});");  // + 10 is for padding
        	writer.write("i++;");
        	writer.write("});");
        	writer.write("var last = elheader.find('th:last-child');");	
        	writer.write("var hh = elheader.height();");
        	writer.write("if (hh>0) {");
        	writer.write("elheader.parent().css({");
        	writer.write("'width': (elbody.width()) + 'px'");
        	writer.write("});");
        	writer.write("var lastf = elfooter.find('td:last-child');");
        	writer.write("var hhf = elfooter.height();");
        	writer.write("elfooter.parent().parent().css({");
        	writer.write("'height': hhf + 'px',");
        	writer.write("'width': bodyw + 'px',");
        	writer.write("'background-color': lastf.css('background-color')");
        	writer.write("});");
        	writer.write("elfooter.parent().css({");
        	writer.write("'width': (bodyw) + 'px'");
        	writer.write("});");
        	writer.write("var ch = el.closest('.col').height();");        	   
        	writer.write("elbody.parent().css({");
        	// NEW 18/10/16
        	writer.write("'margin-top': '-6px',");
        	writer.write("'height': (ch - hh) + 'px'");
        	writer.write("});");
        	writer.write("elheader.parent().parent().css({");
        	// NEW 18/10/16
        	writer.write("'height': hh + 'px',");
        	writer.write("'width': (bodyw) + 'px',");
        	writer.write("'background-color': last.css('background-color')");
        	writer.write("});");
        	writer.write("}");
        	writer.write("};");
        	
        	writer.write("function gettablestringvalue(id,cellid){");
        	writer.write("return $('#' + cellid).text();");
        	writer.write("};");
        } else {
        	writer.write("function syncHeaderFooter(id) {");
        	writer.write("}");	
        	writer.write("function syncHeaderFooters() {");
        	writer.write("}");	
        }
        
        if (NeedsComposer) {
        	writer.write("function abmcprOnUpEvent(event) {");
        	writer.write("var elem = $('#' + event.data.el);");
        	writer.write("$('.abmcpr-bdy.' + elem.data('for')).toggleClass('active');");								
        	writer.write("if ($('.abmcpr-bdy.' + elem.data('for')).hasClass('active')) {");
        	writer.write("elem.children('.abmcpr-arr').removeClass('abmcpr-down').addClass('abmcpr-up');");
        	writer.write("} else {");
        	writer.write("elem.children('.abmcpr-arr').removeClass('abmcpr-up').addClass('abmcpr-down');");
        	writer.write("}");
        	writer.write("};");        	
        }
        
    	writer.write("function replacemewith(id,html) {");
    	writer.write("$('#' + id).replaceWith(html);");
    	writer.write("}");
    	
    	writer.write("function AddHTMLOrReplaceMe(parentid, id, html) {");
    	writer.write("if ($('#' + parentid + ' #' + id).length) {");
    	writer.write("replacemewith(id,html);");
    	writer.write("} else {");
    	writer.write("AddHTML(parentid, html);");
    	writer.write("}");
    	writer.write("}");
    	
    	writer.write("function replacemeinnerwith(id,html) {");
    	writer.write("$('#' + id).html(html);");
    	writer.write("}");
    	    	    	
    	if (NeedsTreeTable || NeedsTable) {
    		writer.write("function replacemewithresetpos(id,html,scrollid,tableid,hasscroll,sortable,collapsable, realid) {");
    		writer.write("var currpos=SaveTablePosition(scrollid);");
    		writer.write("$('#' + id).replaceWith(html);");
    		writer.write("inittable(tableid,hasscroll,sortable,collapsable);");
    		writer.write("RestoreTablePosition(scrollid,currpos);");
    		writer.write("}");
    	}
    	
        writer.write("function initboxed(){");
        writer.write("$('.materialboxed').materialbox();");
        writer.write("};");
        writer.write("function inittooltipped(){");
        writer.write("$('.tooltipped').tooltip('remove');");
        writer.write("$('.tooltipped').tooltip({delay:50});");
        writer.write("};");
        writer.write("function initcollapse(id){");           
        writer.write("setactiveheaders(gahret);");
        writer.write("$('#' + id).collapsible();");           
        writer.write("};");
        
        if (NeedsParallax) {
        	writer.write("function initparallax(){");
        	writer.write("$('.parallax').parallax();");
        	writer.write("};");
        }
        if (NeedsTabs) {
        	writer.write("function inittabs(){");
        	writer.write("$('ul.tabs').tabs();");
        	writer.write("};");
        }
        if (NeedsImageSlider) {
        	writer.write("function initsliders(){");
        	for (Map.Entry<String, ThemeImageSlider> entry : CompleteTheme.ImageSliders.entrySet()) {
        		ThemeImageSlider sl = entry.getValue();
        		writer.write("$('.slider" + sl.ThemeName.toLowerCase() + "').slider({full_width:" + sl.FullWidth + ",indicators:" + sl.Indicators + ",height:" + sl.Height + ",transition:" + sl.Transition + ",interval:" + sl.Interval+ "});");
        	}
        	writer.write("$('.sliderdefault').slider({full_width:true,indicators:true,height:400,transition:500,interval:6000});");
        	writer.write("};");
        }
        
    	writer.write("function initdropdown(){");
    	writer.write("$('.dropdown-button2').dropdown2();");    	
    	writer.write("$('.dropdown-button2.raisechanged').off('keyup').on('keyup', function(e) {");
    	writer.write("$(this).attr('value',$(this).val());");
    	writer.write("b4j_raiseEvent('page_parseevent', {");
    	writer.write("'eventname': $(this).attr('evname') + '_changed',");
    	writer.write("'eventparams': 'value',");
    	writer.write("'value': $(this).val()");
    	writer.write("});");
    	writer.write("});");    	
        writer.write("};");
        
        writer.write("function GetEnabled(item) {");
        writer.write("return $('#' + item).hasClass('disabled');");
        writer.write("}");
        
        writer.write("function SetEnabled(item, b) {");
        writer.write("if (b) {");
        writer.write("$('#' + item).removeClass('disabled');");
        writer.write("} else {");
        writer.write("if (!$('#' + item).hasClass('disabled')) {");
        writer.write("$('#' + item).addClass('disabled');");
        writer.write("}");
        writer.write("}");
        writer.write("}");
        
        writer.write("function SetEnabledR(item, b) {");
        writer.write("if (b) {");
        writer.write("$('#' + item + 'label').removeClass('disabled');");
        writer.write("} else {");
        writer.write("if (!$('#' + item).hasClass('disabled')) {");
        writer.write("$('#' + item + 'label').addClass('disabled');");
        writer.write("}");
        writer.write("}");
        writer.write("}");
    	
    	
        writer.write("function SetDisabled(item, b) {");
        writer.write("$('#' + item).prop('disabled', b);");
        writer.write("}");
        writer.write("function SetChecked(item, b) {");
        writer.write("$('#' + item).prop('checked', b);");
        writer.write("}");
        
        if (NeedsTreeTable) {
        	writer.write("function ttcollapseall(id) {");
        	writer.write("$('#' + id).treetable('collapseAll'); return false;");
        	writer.write("}");
        
        	writer.write("function ttexpandall(id) {");
        	writer.write("$('#' + id).treetable('expandAll'); return false;");
        	writer.write("}");
        	
        	writer.write("function ttexpand(id, nodeid) {");
        	writer.write("$('#' + id).treetable('expandNode',nodeid); return false;");
        	writer.write("}");
        	
        	writer.write("function ttcollapse(id, nodeid) {");
        	writer.write("$('#' + id).treetable('collapseNode',nodeid); return false;");
        	writer.write("}");
        }
        
        writer.write("function EmptyMe(id) {");
        writer.write("$('#' + id).empty();");
        writer.write("}");
        writer.write("function RemoveMe(id) {");
        writer.write("$('#' + id).remove();");
        writer.write("}");
        writer.write("function RemoveBubbles(id) {");
        writer.write("$('.bubble-typing'+id).remove();");
        writer.write("}");
        writer.write("function RemoveMeParent(id) {");
        writer.write("$('#' + id).parent().remove();");
        writer.write("}");
        writer.write("function AddHTMLPageComponent(html) {");
        writer.write("$('main').append(html);");
        writer.write("}");
        
        writer.write("function AddHTML(parentid, html) {");
        writer.write("$('#' + parentid).append(html);");
        writer.write("}");        
               
        writer.write("function PrependHTML(parentid, html) {");
        writer.write("$('#' + parentid).prepend(html);");
        writer.write("}");
                
        writer.write("function AddHTMLToMain(html) {");
        writer.write("$('main').append(html);");
        writer.write("}");
        
        writer.write("function InsertHTMLAfter(parentid, html) {");
        writer.write("$(html).insertAfter($('#' + parentid));");
        writer.write("}");
        writer.write("function InsertHTMLBefore(parentid, html) {");
        writer.write("$(html).insertBefore($('#' + parentid));");
        writer.write("}");
        writer.write("function InsertHTMLAfterParent(parentid, html) {");
        writer.write("$(html).insertAfter($('#' + parentid).parent());");
        writer.write("}");
        writer.write("function InsertHTMLBeforeParent(parentid, html) {");
        writer.write("$(html).insertBefore($('#' + parentid).parent());");
        writer.write("}");
        
        writer.write("function AddClass(id, cl) {");
        writer.write("if ($('#' + id).hasClass(cl)) {return};");
        writer.write("$('#' + id).addClass(cl);");
        writer.write("}");
        writer.write("function RemoveClass(id, cl) {");
        writer.write("$('#' + id).removeClass(cl);");
        writer.write("}");
        
        writer.write("function ChangeVisibility(id, cl) {");
        writer.write("var el = $('#' + id);");
        writer.write("if (cl!='') {");
        writer.write("if (el.hasClass(cl)) {return};");
        writer.write("}");
        writer.write("if (el.hasClass('hide')) {el.removeClass('hide');el.addClass(cl);return;};");
        writer.write("if (el.hasClass('hide-on-small-only')) {el.removeClass('hide-on-small-only');el.addClass(cl);return;};");
        writer.write("if (el.hasClass('hide-on-med-only')) {el.removeClass('hide-on-med-only');el.addClass(cl);return;};");
        writer.write("if (el.hasClass('hide-on-med-and-down')) {el.removeClass('hide-on-med-and-down');el.addClass(cl);return;};");
        writer.write("if (el.hasClass('hide-on-med-and-up')) {el.removeClass('hide-on-med-and-up');el.addClass(cl);return;};");
        writer.write("if (el.hasClass('hide-on-large-only')) {el.removeClass('hide-on-large-only');el.addClass(cl);return;};");
        writer.write("if (el.hasClass('show-on-large')) {el.removeClass('show-on-large');el.addClass(cl);return;};");
        writer.write("if (el.hasClass('show-on-medium')) {el.removeClass('show-on-medium');el.addClass(cl);return;};");
        writer.write("if (el.hasClass('show-on-small')) {el.removeClass('show-on-small');el.addClass(cl);return;};");
        writer.write("if (el.hasClass('show-on-medium-and-up')) {el.removeClass('show-on-medium-and-up');el.addClass(cl);return;};");
        writer.write("if (el.hasClass('show-on-medium-and-down')) {el.removeClass('show-on-medium-and-down');el.addClass(cl);return;};");        
        writer.write("if (el.hasClass('hide-on-extralarge-only')) {el.removeClass('hide-on-extralarge-only');el.addClass(cl);return;};");
        writer.write("if (el.hasClass('hide-on-large-and-down')) {el.removeClass('hide-on-large-and-down');el.addClass(cl);return;};");
        writer.write("if (el.hasClass('hide-on-large-and-up')) {el.removeClass('hide-on-large-and-up');el.addClass(cl);return;};");
        writer.write("if (el.hasClass('show-on-extralarge')) {el.removeClass('show-on-extralarge');el.addClass(cl);return;};");
        writer.write("if (el.hasClass('show-on-large-and-up')) {el.removeClass('show-on-large-and-up');el.addClass(cl);return;};");
        writer.write("if (el.hasClass('show-on-large-and-down')) {el.removeClass('show-on-large-and-down');el.addClass(cl);return;};");        
        writer.write("el.addClass(cl);");
        writer.write("}\n");
        
        writer.write("var abmtim = function(name) {");
        writer.write("var start = new Date();");
        writer.write("return {");
        writer.write("stop: function() {");
        writer.write("var end  = new Date();");
        writer.write("var time = end.getTime() - start.getTime();");
        writer.write("console.log('ABMTim:', name, 'finished in', time, 'ms');");
        writer.write("}");
        writer.write("}");
        writer.write("};");
        
        writer.write("function GetVisibility(id) {");
        writer.write("var el = $('#' + id);");
        writer.write("if (el.hasClass('hide')) {return 'hide';};");
        writer.write("if (el.hasClass('hide-on-small-only')) {return 'hide-on-small-only';};");
        writer.write("if (el.hasClass('hide-on-med-only')) {return 'hide-on-med-only';};");
        writer.write("if (el.hasClass('hide-on-med-and-down')) {return 'hide-on-med-and-down';};");
        writer.write("if (el.hasClass('hide-on-med-and-up')) {return 'hide-on-med-and-up';};");
        writer.write("if (el.hasClass('hide-on-large-only')) {return 'hide-on-large-only';};");
        writer.write("if (el.hasClass('show-on-large')) {return 'show-on-large';};");
        writer.write("if (el.hasClass('show-on-medium')) {return 'show-on-medium';};");
        writer.write("if (el.hasClass('show-on-small')) {return 'show-on-small';};");
        writer.write("if (el.hasClass('show-on-medium-and-up')) {return 'show-on-medium-and-up';};");
        writer.write("if (el.hasClass('show-on-medium-and-down')) {return 'show-on-medium-and-down';};");
        writer.write("if (el.hasClass('hide-on-extralarge-only')) {return 'hide-on-extralarge-only';};");
        writer.write("if (el.hasClass('hide-on-large-and-down')) {return 'hide-on-large-and-down';};");
        writer.write("if (el.hasClass('hide-on-large-and-up')) {return 'hide-on-large-and-up';};");
        writer.write("if (el.hasClass('show-on-extralarge')) {return 'show-on-extralarge'};");
        writer.write("if (el.hasClass('show-on-large-and-up')) {return 'show-on-large-and-up';};");
        writer.write("if (el.hasClass('show-on-large-and-down')) {return 'show-on-large-and-down';};");        
        writer.write("return '';");
        writer.write("}\n");
        
        writer.write("function callAjax(url, type, dataType, data, uniqueid, fromb4js, className) {");  
        writer.write("if (fromb4js) {");
        writer.write("var xhrcall = $.ajax({");
        writer.write("type: type,");
        writer.write("url: url,");
        writer.write("dataType: dataType,");
        writer.write("data: data,");
        writer.write("success: function(result, status, xhr) {");
        writer.write("var res='';");
        writer.write("if (xhr.returnType=='json') {");
        writer.write("res = JSON.stringify(result);");
        writer.write("} else {");
        writer.write("res = result;");
        writer.write("}");
        writer.write("var fret=false;");
        writer.write("try { ");
        writer.write("fret = _b4jsclasses[\"b4jspagekey\" + xhr.className].page_b4jsajaxresult(xhr.uniqueid, res);");	
        writer.write("} catch(err) {}");
        writer.write("if (fret===true) {return;}");
        writer.write("b4j_raiseEvent('page_parseevent',{'eventname':'page_ajaxresult','eventparams':'uniqueid,result', 'uniqueid': xhr.uniqueid,'result':res});");
        writer.write("},");
        writer.write("error: function(xhr, status, error) {");
        writer.write("var fret=false;");
        writer.write("try { ");
        writer.write("fret = _b4jsclasses[\"b4jspagekey\" + xhr.className].page_b4jsajaxerror(xhr.uniqueid, error);");	
        writer.write("} catch(err) {}");
        writer.write("if (fret===true) {return;}");
        writer.write("b4j_raiseEvent('page_parseevent',{'eventname':'page_ajaxerror','eventparams':'uniqueid,error', 'uniqueid': xhr.uniqueid, 'error': error});");
        writer.write("}");
        writer.write("});");
        writer.write("xhrcall.className = className.toLowerCase();");
        writer.write("} else {");
        writer.write("var xhrcall = $.ajax({");
        writer.write("type: type,");
        writer.write("url: url,");
        writer.write("dataType: dataType,");
        writer.write("data: data,");
        writer.write("success: function(result, status, xhr) {");
        writer.write("var res='';");
        writer.write("if (xhr.returnType=='json') {");
        writer.write("res = JSON.stringify(result);");
        writer.write("} else {");
        writer.write("res = result;");
        writer.write("}");
        writer.write("b4j_raiseEvent('page_parseevent',{'eventname':'page_ajaxresult','eventparams':'uniqueid,result', 'uniqueid': xhr.uniqueid,'result':res});");
        writer.write("},");
        writer.write("error: function(xhr, status, error) {");
        writer.write("b4j_raiseEvent('page_parseevent',{'eventname':'page_ajaxerror','eventparams':'uniqueid,error', 'uniqueid': xhr.uniqueid, 'error': error});");
        writer.write("}");
        writer.write("});");
        writer.write("}");
        writer.write("xhrcall.uniqueid = uniqueid;");
        writer.write("var returnType='';");
        writer.write("if (dataType.indexOf('json')>-1) {");
        writer.write("returnType='json';");
        writer.write("}");
        writer.write("xhrcall.returnType = returnType;");
        writer.write("};");
       
    	writer.write("function ChangeCursor(id, cu) {");
        writer.write("var el = $('#' + id);");
        writer.write("if (cu!='') {");
        writer.write("if (el.hasClass(cu)) {return};");
        writer.write("}");
        writer.write("if (el.hasClass('cursor-default')) {el.removeClass('cursor-default');};");
        writer.write("if (el.hasClass('cursor-pointing')) {el.removeClass('cursor-pointing');};");
        writer.write("if (el.hasClass('cursor-waiting')) {el.removeClass('cursor-waiting');};");
        writer.write("if (el.hasClass('cursor-loading')) {el.removeClass('cursor-loading');};");
        writer.write("if (el.hasClass('cursor-moving')) {el.removeClass('cursor-moving');};");
        writer.write("if (el.hasClass('cursor-h-resizing')) {el.removeClass('cursor-h-resizing');};");
        writer.write("if (el.hasClass('cursor-v-resizing')) {el.removeClass('cursor-v-resizing');};");
        writer.write("if (el.hasClass('cursor-typing')) {el.removeClass('cursor-typing');};");
        writer.write("if (el.hasClass('cursor-zoom-in')) {el.removeClass('cursor-zoom-in');};");
        writer.write("if (el.hasClass('cursor-zoom-out')) {el.removeClass('cursor-zoom-out');};");
        writer.write("if (el.hasClass('cursor-cell')) {el.removeClass('cursor-cell');};");
        writer.write("if (el.hasClass('cursor-col-resizing')) {el.removeClass('cursor-col-resizing');};");
        writer.write("if (el.hasClass('cursor-none')) {el.removeClass('cursor-none');};");
        writer.write("if (el.hasClass('cursor-deny')) {el.removeClass('cursor-deny');};");
        writer.write("el.addClass(cu);");
        writer.write("}\n");
        
        writer.write("function RunToast(duration, rounded, html, extra, backgroundcolor, theme) {");
        writer.write("Materialize.toast(html, duration, rounded, function(){b4j_raiseEvent('page_parseevent',{'eventname':'page_toastdismissed','eventparams':'toastid','toastid':extra})});");
        writer.write("$('#' + extra).parent().css('background-color', backgroundcolor);");
        writer.write("$('#' + extra).parent().parent().addClass(theme);");
        writer.write("}");
        
        writer.write("function RunToastB4JS(duration, rounded, html, extra, backgroundcolor, theme) {");
        writer.write("Materialize.toast(html, duration, rounded, function(){});");
        writer.write("$('#' + extra).parent().css('background-color', backgroundcolor);");
        writer.write("$('#' + extra).parent().parent().addClass(theme);");
        writer.write("}");
        
        writer.write("function RunToastNoConnection(counter) {");
        writer.write("Materialize.toast('<span id=\"toasterror' + counter + '\" class=\"white-text\">No connection!</span>',5000,'rounded');");
        writer.write("$('#toasterror'+counter).parent().css('background-color','#FF0000');");
        writer.write("}");
        
        writer.write("function toastclick(extra, butid) {");
        writer.write("b4j_raiseEvent('page_parseevent', {'eventname':'page_toastclicked','eventparams':'toastid,action','toastid':extra,'action':butid});");
        writer.write("}");
        writer.write("function dismisstoast(toastid) {");
        writer.write("Vel($('#' + toastid).parent(), {'opacity':0,marginTop:'-40px'},{duration:375,easing:'easeOutExpo',queue:false,complete:function(){$('#' + toastid).parent().remove();}});");
        writer.write("}");    
        writer.write("function navclick(page, item, url) {");
        writer.write("if (item!='togglefullscreen') {");
        writer.write("if ($('#sidenavbutton').is(':visible')) {");
        writer.write("$('.button-collapse').sideNav('hide');");
        writer.write("}");
        writer.write("b4j_raiseEvent('page_parseevent', {'eventname': 'page_navigationbarclicked','eventparams': 'action,value', 'action': item, 'value': url});");
        writer.write("}");
        writer.write("}");
        writer.write("function SetProperty(id, name, value) {");
        writer.write("$('#' + id).attr(name, value);");
        writer.write("}");
        writer.write("function GetProperty(id, name) {");
        writer.write("return $('#' + id).attr(name);");
        writer.write("}");
        writer.write("function SetCSS(id, name, value) {");
        writer.write("$('#' + id).css(name, value);");
        writer.write("}");
        writer.write("function SetCSS2(tgt, name, value) {");
        writer.write("$(tgt).css(name, value);");
        writer.write("}");
        writer.write("function GetCSS(id, name) {");
        writer.write("return $('#' + id).css(name);");
        writer.write("}");
        writer.write("function RemoveCSS(id, name) {");
        writer.write("$('#' + id).css(name, '');");
        writer.write("}");
        writer.write("function RemoveProperty(id, name) {");
        writer.write("$('#' + id).removeAttr(name);");
        writer.write("}");
        writer.write("function ShowModal(id, dismissible, size) {");
        writer.write("initdropdown();");
        if (NeedsUpload) {
        	writer.write("runupload();");
        }        
        
        writer.write("InitEditEnterFields();");
        writer.write("$('#' + id + '_modalcontent').animate({ scrollTop: 0 }, 'fast');");
        writer.write("$('#' + id).openModal({'dismissible': dismissible, 'end_top': size, ready: function(modal, trigger) {b4j_raiseEvent('page_parseevent', {'eventname':  'page_modalsheetready','eventparams': 'modalsheetname', 'modalsheetname': id});}, 'complete': function() {b4j_raiseEvent('page_parseevent', {'eventname':  'page_modalsheetdismissed','eventparams': 'modalsheetname', 'modalsheetname': id});}});");
        writer.write("$(window).trigger('resize');");
        writer.write("$('#' + id).focus();");
        writer.write("Materialize.updateTextFields();");
        writer.write("}");
        
        writer.write("function ShowModalRelativeCell(id, dismissible, tlcellid, cellwidth) {");
        writer.write("initdropdown();");
        if (NeedsUpload) {
        	writer.write("runupload();");
        }        
        writer.write("InitEditEnterFields();");
        writer.write("var tlcell=$('#' + tlcellid);");
        writer.write("var mleft=tlcell.offset().left;");
        writer.write("var mtop=tlcell.offset().top;");
        
        writer.write("var windowHeight;");
        writer.write("if (self.innerHeight) {");
        writer.write("windowHeight = self.innerHeight;");
        writer.write("} else if (document.documentElement && document.documentElement.clientHeight) {");
        writer.write("windowHeight = document.documentElement.clientHeight;");
        writer.write("} else if (document.body) {");
        writer.write("windowHeight = document.body.clientHeight;");
        writer.write("}");
        writer.write("if ((tlcell[0].getBoundingClientRect().top+tlcell.height()/2)>windowHeight/2) {");
        writer.write("mtop-=$('#' + id).height();");
        writer.write("} else {");
        writer.write("mtop+=tlcell.height();");
        writer.write("}");
            
        writer.write("var mwidth=tlcell.outerWidth();");
        writer.write("var counter=1;");
        writer.write("tlcell.siblings().each(function() {");
        writer.write("if ((counter<cellwidth) && (tlcellid<$(this).attr('id'))) {");
        writer.write("mwidth+=$(this).outerWidth();");
        writer.write("counter++;");
        writer.write("}");
        writer.write("});");
        writer.write("$('#' + id + '_modalcontent').animate({ scrollTop: 0 }, 'fast');");
        writer.write("$('#' + id).css({position: 'absolute',margin: 0, left: mleft, width: mwidth});");
        writer.write("$('#' + id).openModal({'dismissible': dismissible, 'end_top': mtop, ready: function(modal, trigger) {b4j_raiseEvent('page_parseevent', {'eventname':  'page_modalsheetready','eventparams': 'modalsheetname', 'modalsheetname': id});}, 'complete': function() {b4j_raiseEvent('page_parseevent', {'eventname':  'page_modalsheetdismissed','eventparams': 'modalsheetname', 'modalsheetname': id});}});");
        writer.write("$('body').css('overflow', '');");
        writer.write("$(window).trigger('resize');");
        writer.write("$('#' + id).focus();");
        writer.write("Materialize.updateTextFields();");
        writer.write("}");
        
        writer.write("function ShowModalRelativeComponent(id, dismissible, tlcellid, fixedwidth) {");
        writer.write("initdropdown();");
        if (NeedsUpload) {
        	writer.write("runupload();");
        }        
        writer.write("InitEditEnterFields();");
        writer.write("var tlcell=$('#' + tlcellid);");
        writer.write("var mleft=tlcell.offset().left;");
        writer.write("var mtop=tlcell.offset().top;");
        
        writer.write("var windowHeight;");
        writer.write("if (self.innerHeight) {");
        writer.write("windowHeight = self.innerHeight;");
        writer.write("} else if (document.documentElement && document.documentElement.clientHeight) {");
        writer.write("windowHeight = document.documentElement.clientHeight;");
        writer.write("} else if (document.body) {");
        writer.write("windowHeight = document.body.clientHeight;");
        writer.write("}");
        writer.write("if ((tlcell[0].getBoundingClientRect().top+tlcell.height()/2)>windowHeight/2) {");
        writer.write("mtop-=$('#' + id).height();");
        writer.write("} else {");
        writer.write("mtop+=tlcell.height();");
        writer.write("}");
 
        writer.write("$('#' + id + '_modalcontent').animate({ scrollTop: 0 }, 'fast');");
        writer.write("$('#' + id).css({position: 'absolute',margin: 0, left: mleft, width: fixedwidth});");
        writer.write("$('#' + id).openModal({'dismissible': dismissible, 'end_top': mtop, ready: function(modal, trigger) {b4j_raiseEvent('page_parseevent', {'eventname':  'page_modalsheetready','eventparams': 'modalsheetname', 'modalsheetname': id});}, 'complete': function() {b4j_raiseEvent('page_parseevent', {'eventname':  'page_modalsheetdismissed','eventparams': 'modalsheetname', 'modalsheetname': id});}});");
        writer.write("$('body').css('overflow', '');");
        writer.write("$(window).trigger('resize');");
        writer.write("$('#' + id).focus();");
        writer.write("Materialize.updateTextFields();");
        writer.write("}");
        
        writer.write("function ShowModalAbsolute(id, dismissible, mleft, mtop, mwidth, mheight) {");
        writer.write("initdropdown();");
        if (NeedsUpload) {
        	writer.write("runupload();");
        }        
        writer.write("InitEditEnterFields();");
               
        writer.write("$('#' + id + '_modalcontent').animate({ scrollTop: 0 }, 'fast');");
        writer.write("$('#' + id).css({margin: 0, left: mleft, width: mwidth, height: mheight});");
        writer.write("$('#' + id).openModal({'dismissible': dismissible, 'end_top': mtop, ready: function(modal, trigger) {b4j_raiseEvent('page_parseevent', {'eventname':  'page_modalsheetready','eventparams': 'modalsheetname', 'modalsheetname': id});}, 'complete': function() {b4j_raiseEvent('page_parseevent', {'eventname':  'page_modalsheetdismissed','eventparams': 'modalsheetname', 'modalsheetname': id});}});");
        writer.write("$(window).trigger('resize');");
        writer.write("Materialize.updateTextFields();");
        writer.write("}");
        
        writer.write("function ReShowModal(id) {");
        writer.write("initdropdown();");
        if (NeedsUpload) {
        	writer.write("runupload();");
        }
        writer.write("InitEditEnterFields();");
        writer.write("$(window).trigger('resize');");
        writer.write("$('#' + id).focus();");
        writer.write("Materialize.updateTextFields();");
        writer.write("}");
        
        writer.write("function CloseModal(id) {");
        writer.write("$('#' + id).closeModal({");
        writer.write("complete: function() {");
        writer.write("b4j_raiseEvent('page_parseevent', {'eventname':  'page_modalsheetdismissed','eventparams': 'modalsheetname', 'modalsheetname': id});");
        writer.write("}");
        writer.write("});");
        writer.write("}");
        writer.write("function fadeinimg(id) {");
        writer.write("Materialize.fadeInImage('#' + id);");
        writer.write("}");
        
        writer.write("function IsModalOpen(id) {");
        writer.write("return $('#' + id).css('display')=='block';");
        writer.write("}");
        
        writer.write("function buttonclickarray(event, par, arrayname, button) {");
        writer.write("if($('#' + par + arrayname + button).hasClass('disabled')) {return;}");
        writer.write("if(arrayname!='' && $('#' + par + button).hasClass('disabled')) {return;}");
        writer.write("var myself = '#' + par + arrayname + button;");
        writer.write("var myB4JSKey = $(myself).data('b4js');");
        writer.write("if (myB4JSKey!='') {");
        	writer.write("var codeToExecute = 'return _b4jsclasses[\"' + myB4JSKey + '\"].' + _b4jsvars[myB4JSKey + '_B4JSOnClick'];");        	
        	writer.write("var tmpFunc = new Function(codeToExecute);");
        	writer.write("var fret=false;");
        	writer.write("try { ");
        	writer.write("fret = tmpFunc()");	
        	writer.write("} catch(err) {}");
        	writer.write("if (fret==true) {return;}");
        writer.write("}");
        
        writer.write("$('.dropdown-button').not(myself).dropdown('close');");
        writer.write("$('.dropdown-content').hide();");
        writer.write("if($('#' + par + arrayname + button).hasClass('disabled')) {return;}");
        writer.write("if (arrayname=='') {");
        writer.write("b4j_raiseEvent('page_parseevent', {'eventname': button + '_clicked','eventparams': 'target', 'target': par+button});");	
        writer.write("} else {");
        writer.write("b4j_raiseEvent('page_parseevent', {'eventname': arrayname + '_clicked','eventparams': 'target', 'target': par+button});");
        writer.write("}");
        writer.write("}");
        
        writer.write("function buttonclickarraySP(event, par, arrayname, button) {");
        writer.write("event.stopPropagation();");
        writer.write("if($('#' + par + arrayname + button).hasClass('disabled')) {return;}");
        writer.write("if(arrayname!='' && $('#' + par + button).hasClass('disabled')) {return;}");
        writer.write("var myself = '#' + par + arrayname + button;");
        writer.write("var myB4JSKey = $(myself).data('b4js');");
        writer.write("if (myB4JSKey!='') {");
        	writer.write("var codeToExecute = 'return _b4jsclasses[\"' + myB4JSKey + '\"].' + _b4jsvars[myB4JSKey + '_B4JSOnClick'];");        	
        	writer.write("var tmpFunc = new Function(codeToExecute);");
        	writer.write("var fret=false;");
        	writer.write("try { ");
        	writer.write("fret = tmpFunc()");	
        	writer.write("} catch(err) {}");
        	writer.write("if (fret==true) {return;}");
        writer.write("}");
        
        writer.write("$('.dropdown-button').not(myself).dropdown('close');");
        writer.write("$('.dropdown-content').hide();");
        writer.write("if($('#' + par + arrayname + button).hasClass('disabled')) {return;}");
        writer.write("if (arrayname=='') {");
        writer.write("b4j_raiseEvent('page_parseevent', {'eventname': button + '_clicked','eventparams': 'target', 'target': par+button});");	
        writer.write("} else {");
        writer.write("b4j_raiseEvent('page_parseevent', {'eventname': arrayname + '_clicked','eventparams': 'target', 'target': par+button});");
        writer.write("}");
        writer.write("}");
        
        writer.write("function buttonmenuclickarray(event, par, arrayname, button, ret) {");               
        writer.write("if($('#' + par + arrayname + button).hasClass('disabled')) {return;}");
        writer.write("if (arrayname=='') {");
        writer.write("b4j_raiseEvent('page_parseevent', {'eventname': button + '_menuclicked','eventparams': 'target,returnname', 'target': par+button, 'returnname': ret});");	
        writer.write("} else {");
        writer.write("b4j_raiseEvent('page_parseevent', {'eventname': arrayname + '_menuclicked','eventparams': 'target,returnname', 'target': par+button, 'returnname': ret});");
        writer.write("}");
        writer.write("}");
        
        //cpreditarray
        writer.write("function cpreditarray(event, par, arrayname, button, ret) {");               
        writer.write("if (arrayname=='') {");
        writer.write("b4j_raiseEvent('page_parseevent', {'eventname': button + '_edit','eventparams': 'blockid', 'blockid': ret});");	
        writer.write("} else {");
        writer.write("b4j_raiseEvent('page_parseevent', {'eventname': arrayname + '_edit','eventparams': 'blockid', 'blockid': ret});");
        writer.write("}");
        writer.write("}");
        
        writer.write("function cprdelarray(event, par, arrayname, button, ret) {");               
        writer.write("if (arrayname=='') {");
        writer.write("b4j_raiseEvent('page_parseevent', {'eventname': button + '_remove','eventparams': 'blockid', 'blockid': ret});");	
        writer.write("} else {");
        writer.write("b4j_raiseEvent('page_parseevent', {'eventname': arrayname + '_remove','eventparams': 'blockid', 'blockid': ret});");
        writer.write("}");
        writer.write("}");
        
        writer.write("function cpraddarray(event, par, arrayname, button, ret) {");               
        writer.write("if (arrayname=='') {");
        writer.write("b4j_raiseEvent('page_parseevent', {'eventname': button + '_add','eventparams': 'blockid', 'blockid': ret});");	
        writer.write("} else {");
        writer.write("b4j_raiseEvent('page_parseevent', {'eventname': arrayname + '_add','eventparams': 'blockid', 'blockid': ret});");
        writer.write("}");
        writer.write("}");
                
        writer.write("function labelclickarray(event, par, arrayname, label) {");
        writer.write("if($('#' + par + arrayname + label).hasClass('disabled')) {return;}");
        writer.write("var myself = '#' + par + arrayname + label;");
        writer.write("var myB4JSKey = $(myself).data('b4js');");
        writer.write("if (myB4JSKey!='') {");
        	writer.write("var codeToExecute = 'return _b4jsclasses[\"' + myB4JSKey + '\"].' + _b4jsvars[myB4JSKey + '_B4JSOnClick'];");        	
        	writer.write("var tmpFunc = new Function(codeToExecute);");
        	writer.write("var fret=false;");
        	writer.write("try { ");
        	writer.write("fret = tmpFunc()");	
        	writer.write("} catch(err) {}");
        	writer.write("if (fret==true) {event.stopPropagation();return false;}");
        writer.write("}");
        
        writer.write("if (arrayname=='') {");
        writer.write("b4j_raiseEvent('page_parseevent', {'eventname': label + '_clicked','eventparams': 'target', 'target': par+label});");	
        writer.write("} else {");
        writer.write("b4j_raiseEvent('page_parseevent', {'eventname': arrayname + '_clicked','eventparams': 'target', 'target': par+label});");
        writer.write("}");
        writer.write("event.stopPropagation();");
        writer.write("return false;");
        writer.write("}");
        
        writer.write("function labelresetclickarray(event, par, arrayname, label) {");
        writer.write("if($('#' + par + arrayname + label).hasClass('disabled')) {return;}");
        writer.write("var myself = '#' + par + arrayname + label;");
        writer.write("var myB4JSKey = $(myself).data('b4js');");
        writer.write("if (myB4JSKey!='') {");
        	writer.write("var codeToExecute = 'return _b4jsclasses[\"' + myB4JSKey + '\"].' + _b4jsvars[myB4JSKey + '_B4JSOnResetClick'];");        	
        	writer.write("var tmpFunc = new Function(codeToExecute);");
        	writer.write("var fret=false;");
        	writer.write("try { ");
        	writer.write("fret = tmpFunc()");	
        	writer.write("} catch(err) {}");
        	writer.write("if (fret==true) {event.stopPropagation();return false;}");
        writer.write("}");
        
        writer.write("if (arrayname=='') {");
        writer.write("b4j_raiseEvent('page_parseevent', {'eventname': label + '_resetclicked','eventparams': 'target', 'target': par+label});");	
        writer.write("} else {");
        writer.write("b4j_raiseEvent('page_parseevent', {'eventname': arrayname + '_resetclicked','eventparams': 'target', 'target': par+label});");
        writer.write("}");
        writer.write("event.stopPropagation();");
        writer.write("return false;");
        writer.write("}");
        
        writer.write("function inputresetclickarray(event, par, arrayname, label) {");
        writer.write("if($('#' + par + arrayname + label).hasClass('disabled')) {return;}");
        writer.write("var myself = '#' + par + arrayname + label;");
        writer.write("var myB4JSKey = $(myself).data('b4js');");
        writer.write("if (myB4JSKey!='') {");
        	writer.write("var codeToExecute = 'return _b4jsclasses[\"' + myB4JSKey + '\"].' + _b4jsvars[myB4JSKey + '_B4JSOnResetClick'];");        	
        	writer.write("var tmpFunc = new Function(codeToExecute);");
        	writer.write("var fret=false;");
        	writer.write("try { ");
        	writer.write("fret = tmpFunc()");	
        	writer.write("} catch(err) {}");
        	writer.write("if (fret==true) {event.stopPropagation();return false;}");
        writer.write("}");
        
        writer.write("if (arrayname=='') {");
        writer.write("b4j_raiseEvent('page_parseevent', {'eventname': label + '_resetclicked','eventparams': 'target', 'target': par+label});");	
        writer.write("} else {");
        writer.write("b4j_raiseEvent('page_parseevent', {'eventname': arrayname + '_resetclicked','eventparams': 'target', 'target': par+label});");
        writer.write("}");
        writer.write("event.stopPropagation();");
        writer.write("return false;");
        writer.write("}");
        
        if (NeedsTabs) {
        	writer.write("function tabsclickarray(event, par, arrayname, tabs, tab) {");
        	writer.write("if($('#' + par + arrayname + tabs).hasClass('disabled')) {return;}");
        	
        	 writer.write("var myself = '#' + par + arrayname + tabs;");
             writer.write("var myB4JSKey = $(myself).data('b4js');");
             writer.write("if (myB4JSKey!='') {");
             	writer.write("var codeToExecute = 'return _b4jsclasses[\"' + myB4JSKey + '\"].' + _b4jsvars[myB4JSKey + '_B4JSOnClick'];");    
             	writer.write("codeToExecute = codeToExecute.replace(/B4JS#!#TABSRN/g, tab);");
             	writer.write("var tmpFunc = new Function(codeToExecute);");
             	writer.write("var fret=false;");
             	writer.write("try { ");
             	writer.write("fret = tmpFunc()");	
             	writer.write("} catch(err) {}");
             	writer.write("if (fret==true) {return;}");
             writer.write("}");
        	
        	writer.write("if (arrayname=='') {");
        	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': tabs + '_clicked','eventparams': 'tabreturnname', 'tabreturnname': tab});");	
        	writer.write("} else {");
        	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': arrayname + '_clicked','eventparams': 'tabreturnname', 'tabreturnname': tab});");
        	writer.write("}");
        	writer.write("}");
        }
        
    	writer.write("function imageclickarray(event, par, arrayname, image) {");
    	
    	writer.write("if($('#' + par + arrayname + image).hasClass('disabled')) {return;}");
    	writer.write("if (arrayname=='') {");
    	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': image + '_clicked','eventparams': 'target', 'target': par+image});");	
    	writer.write("} else {");
    	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': arrayname + '_clicked','eventparams': 'target', 'target': par+image});");
    	writer.write("}");
    	writer.write("}");
        
        if (NeedsChips) {
        	writer.write("function chipclickarray(event, par, arrayname, chip) {");
        	
        	writer.write("if($('#' + par + arrayname + chip).hasClass('disabled')) {return;}");
        	writer.write("if (arrayname=='') {");
        	writer.write("setTimeout(function() { b4j_raiseEvent('page_parseevent', {'eventname': chip + '_clicked','eventparams': 'target', 'target': par+chip});}, 100);");	
        	writer.write("} else {");
        	writer.write("setTimeout(function() { b4j_raiseEvent('page_parseevent', {'eventname': arrayname + '_clicked','eventparams': 'target', 'target': par+chip});}, 100);");
        	writer.write("}");
        	writer.write("}");
        	
        	writer.write("function chipclosedarray(event, par, arrayname, chip) {");        	
        	writer.write("if($('#' + par + arrayname + chip).hasClass('disabled')) {return;}");
        	writer.write("if (arrayname=='') {");
        	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': chip + '_closed','eventparams': 'target', 'target': par+chip});");	
        	writer.write("} else {");
        	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': arrayname + '_closed','eventparams': 'target', 'target': par+chip});");
        	writer.write("}");
        	writer.write("}");
        }
        if (NeedsActionButton) {
        	writer.write("function actionbuttonclickarray(event, par, arrayname, button, subbutton) {");
        	writer.write("if($('#' + par + arrayname + button).hasClass('disabled')) {return;}");
        	 writer.write("var myself = '#' + par + arrayname + button;");
             writer.write("var myB4JSKey = $(myself).data('b4js');");             
             writer.write("if (myB4JSKey!='') {");
             	writer.write("var codeToExecute = 'return _b4jsclasses[\"' + myB4JSKey + '\"].' + _b4jsvars[myB4JSKey + '_B4JSOnClick'];"); 
             	writer.write("codeToExecute = codeToExecute.replace(/B4JS#!#AB/g, button);");
             	writer.write("codeToExecute = codeToExecute.replace(/B4JS#!#ASB/g, subbutton);");
             	writer.write("var tmpFunc = new Function(codeToExecute);");
             	writer.write("var fret=false;");
             	writer.write("try { ");
             	writer.write("fret = tmpFunc()");	
             	writer.write("} catch(err) {}");
             	writer.write("if (fret===true) {return;}");
             writer.write("}");
        	writer.write("if (arrayname=='') {");
        	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': button + '_clicked','eventparams': 'target,subtarget', 'target': par+button, 'subtarget': subbutton});");	
        	writer.write("} else {");
        	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': arrayname + '_clicked','eventparams': 'target,subtarget', 'target': par+button, 'subtarget': subbutton});");
        	writer.write("}");
        	writer.write("}");
        }
        if (NeedsPagination) {
        	writer.write("function paginationclickarray(event, par, pagid, pagtype, pagenumber,lastpagenumber) {");
        	writer.write("if (event.stopPropagation) {");
            writer.write("event.stopPropagation();");
            writer.write("} else if (window.event) {");
        	writer.write("window.event.cancelBubble = true;");
            writer.write("}");
            writer.write("var oldpagenumber=0;");
        	writer.write("$('#' + par + pagid).children('button').each(function() {");
        	writer.write("if ($(this).hasClass('selected')) {");
        	writer.write("oldpagenumber=parseInt($(this).children(':first').text());");
        	writer.write("}");
        	writer.write("});");
        	writer.write("switch (pagtype) {");
        	writer.write("case 'first':");
        	writer.write("pagenumber=1;");
        	writer.write("break;");
        	writer.write("case 'last':");
        	writer.write("pagenumber=lastpagenumber;");
        	writer.write("break;");
        	writer.write("case 'prev':");
        	writer.write("if (oldpagenumber>1) {");
        	writer.write("pagenumber=oldpagenumber-1;");
        	writer.write("} else {");
        	writer.write("pagenumber=1;");
        	writer.write("}");
        	writer.write("break;");
        	writer.write("case 'next':");
        	writer.write("if (oldpagenumber<lastpagenumber) {");
        	writer.write("pagenumber=oldpagenumber+1;");
        	writer.write("} else {");
        	writer.write("pagenumber=lastpagenumber;");
        	writer.write("}");
        	writer.write("break;");
        	writer.write("default:");
        	writer.write("}");
        	writer.write("if (pagenumber!=oldpagenumber) {");
        	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': pagid + '_pagechanged','eventparams': 'oldpage,newpage', 'oldpage': oldpagenumber, 'newpage': pagenumber});");
        	writer.write("}");
        	writer.write("}");
        }
        
        if (NeedsCards) {
        	writer.write("function cardclickarray(event, par, arrayname, card, action) {");
        	writer.write("if (event.stopPropagation) {");
            writer.write("event.stopPropagation();");
            writer.write("} else if (window.event) {");
        	writer.write("window.event.cancelBubble = true;");
            writer.write("}");
        	writer.write("if($('#' + par + arrayname + card).hasClass('disabled')) {return;}");
        	writer.write("if (arrayname=='') {");
        	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': card + '_linkclicked','eventparams': 'target,action', 'target': par+card, 'action': action});");	
        	writer.write("} else {");
        	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': arrayname + '_linkclicked','eventparams': 'target,action', 'target': par+card, 'action': action});");
        	writer.write("}");
        	writer.write("}");
        }
        if (NeedsLists) {
        	for (Entry<String,ABMList> lsts: Lists.entrySet()) {
        		writer.write(lsts.getValue().BuildJavaScript() + "\n");
        	}
        	writer.write("function listclickarray(event, par, arrayname, list, item) {");
        	writer.write("if (event.stopPropagation) {");
            writer.write("event.stopPropagation();");
            writer.write("} else if (window.event) {");
        	writer.write("window.event.cancelBubble = true;");
            writer.write("}");
        	writer.write("if($('#' + par + arrayname + list).hasClass('disabled')) {return;}");
        	
        	writer.write("$('#' + par + arrayname + list).children('li').each(function() {");
        	writer.write("$(this).removeClass('active');");
        	writer.write("$(this).children('div').removeClass('active');");
        	writer.write("});");
        	
        	writer.write("if (arrayname=='') {");
        	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': list + '_clicked','eventparams': 'itemid', 'itemid': item});");	
        	writer.write("} else {");
        	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': arrayname + '_clicked','eventparams': 'itemid', 'itemid': item});");
        	writer.write("}");
        	writer.write("}\n");
        	
        	writer.write("function listsetactiveitem(event, par, arrayname, list, item) {");	
        	writer.write("$('#' + par + arrayname + list).children('li').each(function() {");
        	writer.write("$(this).removeClass('active');");
        	writer.write("$(this).children('div').removeClass('active');");
        	writer.write("if ($(this).children('div').attr('returnid')==item) {");
        	writer.write("$(this).addClass('active');");
        	writer.write("$(this).children('div').addClass('active');");
        	writer.write("}");
        	writer.write("});");
        	writer.write("}\n");
        }
        if (NeedsCheckbox) {
        	writer.write("function checkboxclickarray(event, par, arrayname, ch) {");
        	writer.write("if (event.stopPropagation) {");
            writer.write("event.stopPropagation();");
            writer.write("} else if (window.event) {");
        	writer.write("window.event.cancelBubble = true;");
            writer.write("}");
        	writer.write("if($('#' + par + arrayname + ch).hasClass('disabled')) {return;}");
        	writer.write("var myself = '#' + par + arrayname + ch + \"-parent\";");
            writer.write("var myB4JSKey = $(myself).data('b4js');");
            writer.write("if (myB4JSKey!='') {");
            	writer.write("var codeToExecute = 'return _b4jsclasses[\"' + myB4JSKey + '\"].' + _b4jsvars[myB4JSKey + '_B4JSOnClick'];");        	
            	writer.write("var tmpFunc = new Function(codeToExecute);");
            	writer.write("var fret=false;");
            	writer.write("try { ");
            	writer.write("fret = tmpFunc()");	
            	writer.write("} catch(err) {}");
            	writer.write("if (fret===true) {return;}");
            writer.write("}");
        	writer.write("if (arrayname=='') {");
        	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': ch + '_clicked','eventparams': 'target', 'target': par+ch});");	
        	writer.write("} else {");
        	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': arrayname + '_clicked','eventparams': 'target', 'target': par+ch});");
        	writer.write("}");
        	writer.write("}");
        }
        if (NeedsRadio) {
        	writer.write("function radioclickarray(event, par, arrayname, rd) {");
        	writer.write("if (event.stopPropagation) {");
            writer.write("event.stopPropagation();");
            writer.write("} else if (window.event) {");
        	writer.write("window.event.cancelBubble = true;");
            writer.write("}");
        	writer.write("if($('#' + par + arrayname + rd).hasClass('disabled')) {return;}");
        	writer.write("var myself = '#' + par + arrayname + rd;");
            writer.write("var myB4JSKey = $(myself).data('b4js');");
            writer.write("if (myB4JSKey!='') {");
            	writer.write("var codeToExecute = 'return _b4jsclasses[\"' + myB4JSKey + '\"].' + _b4jsvars[myB4JSKey + '_B4JSOnClick'];");        	
            	writer.write("var tmpFunc = new Function(codeToExecute);");
            	writer.write("var fret=false;");
            	writer.write("try { ");
            	writer.write("fret = tmpFunc()");	
            	writer.write("} catch(err) {}");
            	writer.write("if (fret===true) {return;}");
            writer.write("}");
        	writer.write("if (arrayname=='') {");
        	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': rd + '_clicked','eventparams': 'target', 'target': par+rd});");	
        	writer.write("} else {");
        	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': arrayname + '_clicked','eventparams': 'target', 'target': par+rd});");
        	writer.write("}");
        	writer.write("}");
        }
        
    	writer.write("function imagetogglearray(event, par, id, state, src, src2) {"); 
    	writer.write("if (event.stopPropagation) {");
        writer.write("event.stopPropagation();");
        writer.write("} else if (window.event) {");
    	writer.write("window.event.cancelBubble = true;");
        writer.write("}");
    	writer.write("if($('#' + par + id).hasClass('disabled')) {return;}");
    	writer.write("$('#' + par + id).attr('abmtoggle', state);");
    	writer.write("$('#' + par + id).attr('src', src);");
    	writer.write("if (state=='0') {");
    	writer.write("$('#' + par + id).attr('onclick', \"imagetogglearray(event,'\" + par + \"','\" + id + \"','1','\" + src2 + \"','\" + src + \"')\");");
    	writer.write("} else {");
    	writer.write("$('#' + par + id).attr('onclick', \"imagetogglearray(event,'\" + par + \"','\" + id + \"','0','\" + src2 + \"','\" + src + \"')\");");
    	writer.write("}");
    	writer.write("};");
        
        if (NeedsSwitch) {
        	writer.write("function switchclickarray(event, par, arrayname, sw) {");
        	writer.write("if (event.stopPropagation) {");
            writer.write("event.stopPropagation();");
            writer.write("} else if (window.event) {");
        	writer.write("window.event.cancelBubble = true;");
            writer.write("}");
        	 writer.write("var myself = '#' + par + arrayname + sw;");
             writer.write("var myB4JSKey = $(myself).data('b4js');");
             writer.write("if (myB4JSKey!='') {");
             	writer.write("var codeToExecute = 'return _b4jsclasses[\"' + myB4JSKey + '\"].' + _b4jsvars[myB4JSKey + '_B4JSOnClick'];");        	
             	writer.write("var tmpFunc = new Function(codeToExecute);");
             	writer.write("var fret=false;");
             	writer.write("try { ");
             	writer.write("fret = tmpFunc()");	
             	writer.write("} catch(err) {}");
             	writer.write("if (fret===true) {return;}");
             writer.write("}");
        	
        	writer.write("if($('#' + par + arrayname + sw).hasClass('disabled')) {return;}");
        	writer.write("if (arrayname=='') {");
        	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': sw + '_clicked','eventparams': 'target', 'target': par+sw});");	
        	writer.write("} else {");
        	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': arrayname + '_clicked','eventparams': 'target', 'target': par+sw});");
        	writer.write("}");
        	writer.write("};");
        }
        if (NeedsCombo) {
        	writer.write("function comboclickarray(event, par, arrayname, combo, item, itemid) {");
        	writer.write("if (event.stopPropagation) {");
            writer.write("event.stopPropagation();");
            writer.write("} else if (window.event) {");
        	writer.write("window.event.cancelBubble = true;");
            writer.write("}");
            writer.write("var cont = $('#' + $('#' + par + combo).attr('data-activates'));");
            writer.write("cont.removeClass('active');");
            writer.write("cont.css('display','none');");
        	writer.write("if($('#' + par + arrayname + combo).hasClass('disabled')) {return;}");
        	writer.write("$('#' + par + combo).removeClass('invalid');");
        	writer.write("$('#' + par + combo).addClass('valid');");
        	writer.write("$('#' + par + combo + 'label').addClass('active');");        	
        	writer.write("$('#' + par + combo).attr('value', item);");
        	writer.write("$('#' + par + combo).val(item);");
        	writer.write("$('#' + par + combo).attr('returnid', itemid);");
        	writer.write("if (arrayname=='') {");
        	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': combo + '_clicked','eventparams': 'itemid', 'itemid': itemid});");
        	writer.write("} else {");
        	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': arrayname + '_clicked','eventparams': 'itemid', 'itemid': itemid});");
        	writer.write("}");
        	writer.write("}");
        }
        
        writer.write("function inputscrollintofocus(id,smooth) {");
        writer.write("if (smooth=='0') {");
        writer.write("$('#' + id).scrollIntoView(false);");
        writer.write("} else {");
        writer.write("$('#' + id).scrollIntoView();");
        writer.write("}");
        writer.write("}\n");
       
        writer.write("function tablescrolltoinf(tableid, uniqueid, smooth) {");
        writer.write("var inner = tableid + '-src .abmscrollltbl-body';");
        writer.write("var ypos = $('#' + inner).find('[uniqueid=\"' + uniqueid + '\"]').offset().top;");
        writer.write("if (smooth=='0') {");
        writer.write("$('#' + inner).scrollTop($('#' + inner).scrollTop()+ypos);");
        writer.write("} else {");
        writer.write("$('#' + inner).animate({scrollTop: $('#' + inner).scrollTop()+ypos}, 300);");
        writer.write("}");        
        writer.write("}\n");
        
        writer.write("function listscrollto(listid, uniqueid, smooth) {");
        writer.write("var inner = listid;");
        writer.write("var ypos = $('#' + inner).find('[returnid=\"' + uniqueid + '\"]').position().top;");
        writer.write("if (smooth=='0') {");
        writer.write("$('#' + inner).scrollTop($('#' + inner).scrollTop()+ypos);");
        writer.write("} else {");
        writer.write("$('#' + inner).animate({scrollTop: $('#' + inner).scrollTop()+ypos}, 300);");
        writer.write("}");        
        writer.write("}\n");
        
        writer.write("function comboscrollto(comboid, uniqueid, smooth) {");
        writer.write("var inner = 'dropdown' + comboid;");
        writer.write("if (!$('#' + inner).hasClass('active')) {"); 
        writer.write("$('#' + comboid).trigger('click');");
        writer.write("}");
        writer.write("var retelem = $('#' + inner).find('[returnid=\"' + uniqueid + '\"]');");
        writer.write("if (retelem) {");
        writer.write("$('#' + comboid).attr('returnid', uniqueid);");
        writer.write("var ypos = retelem.position().top;");
        writer.write("if (smooth=='0') {");
        writer.write("$('#' + inner).scrollTop($('#' + inner).scrollTop()+ypos);");
        writer.write("} else {");
        writer.write("$('#' + inner).animate({scrollTop: $('#' + inner).scrollTop()+ypos}, 300);");
        writer.write("}");
        writer.write("}");        
        writer.write("}\n");
        
        writer.write("function inputsetfocus(id) {");
        writer.write("$('#' + id).focus();");
        writer.write("}\n");
             
        if (NeedsTabs) {
        	writer.write("function getactivetab(id) {");
        	writer.write("var ret='';");
        	writer.write("if (document.getElementById(id)=='undefined') {return ret;};");
        	writer.write("$('#' + id).children('li').children('a').each(function () {");
        	writer.write("if ($(this).hasClass('active')) {");
        	writer.write("ret=$(this).attr('href');");
        	writer.write("}");
        	writer.write("});");
        	writer.write("return ret;");
        	writer.write("}");
        }   
        
        if (NeedsTable || NeedsTreeTable) {
        	writer.write("function getactivetablerow(id) {");
        	writer.write("var ret = '';");
        	writer.write("if (document.getElementById(id) == null) {");
        	writer.write("if (document.getElementById(id + '-body') == null) {");
        	writer.write("return ret;");
        	writer.write("} else {");
        	writer.write("return getactivetablerow(id + '-body');");
        	writer.write("}");
        	writer.write("};");
        	writer.write("$('#' + id + ' tbody').children('tr').each(function() {");
        	writer.write("var currthis = $(this);");
        	writer.write("var tablename = currthis.attr('name');");
        	writer.write("$(this).children('td').each(function() {");
        	writer.write("if ($(this).hasClass('selected')) {");
        	writer.write("ret = currthis.attr('uniqueid');");
        	writer.write("activetablerows['#' + id] = ret;");
        	writer.write("return ret;");
        	writer.write("}");
        	writer.write("});");
        	writer.write("});");
        	writer.write("activetablerows['#' + id] = ret;");
        	writer.write("return ret;");
        	writer.write("};");
        	
        	writer.write("function getsortcolumn(id) {");
        	writer.write("var ret = '';");
        	writer.write("if (document.getElementById(id) == null) {");
        	writer.write("if (document.getElementById(id + '-header') == null) {");
        	writer.write("return ret;");
        	writer.write("} else {");
        	writer.write("return getsortcolumn(id + '-header');");
        	writer.write("}");
        	writer.write("};");
        	writer.write("$('#' + id + ' thead tr').children('th').each(function() {");
        	writer.write("if ($(this).hasClass('sorttable_sorted')) {");
        	writer.write("ret = $(this).attr('fieldname');");
        	writer.write("}");
        	writer.write("if ($(this).hasClass('sorttable_sorted_reverse')) {");
        	writer.write("ret = 'DESC_' + $(this).attr('fieldname');");
        	writer.write("}");
        	writer.write("});");
        	writer.write("return ret;");
        	writer.write("};");

        	writer.write("function setsortcolumn(id, fieldname, order) {");
        	writer.write("if (document.getElementById(id) == null) {");
        	writer.write("if (document.getElementById(id + '-header') == null) {");
        	writer.write("return;");
        	writer.write("} else {");
        	writer.write("setsortcolumn(id + '-header', fieldname, order);");
        	writer.write("return;");
        	writer.write("}");
        	writer.write("};");
        	writer.write("if (fieldname == '') {");
        	writer.write("return");
        	writer.write("};");
        	writer.write("$('#' + id + ' thead tr').children('th').each(function() {");
        	writer.write("if ($(this).attr('fieldname') == fieldname) {");
        	writer.write("sorttable.innerSortFunction.apply($(this)[0], []);");
        	writer.write("if (order == 'DESC') {");
        	writer.write("sorttable.innerSortFunction.apply($(this)[0], []);");
        	writer.write("}");
        	writer.write("}");
        	writer.write("});");
        	writer.write("};");
        	
        	writer.write("function setactivetablerow(id,uniqueid) {");        
        	writer.write("if (document.getElementById(id) == null) {");
        	writer.write("if (document.getElementById(id + '-body') == null) {");
        	writer.write("return;");
        	writer.write("} else {");
        	writer.write("setactivetablerow(id + '-body', uniqueid);");
        	writer.write("}");
        	writer.write("};");
        	writer.write("$('#' + id + ' tbody').children('tr').each(function() {");
        	writer.write("if ($(this).attr('uniqueid')==uniqueid) {");
        	writer.write("$(this).children('td').addClass(\"selected\");");
        	writer.write("} else {");
        	writer.write("$(this).children('td').removeClass(\"selected\");");
        	writer.write("}");
        	writer.write("});");
        	writer.write("activetablerows['#' + id]=uniqueid;");
        	writer.write("};");
        	
        }
    	
        writer.write("var gahret=[];");
    	writer.write("function getactiveheaders(id) {");
    	writer.write("gahret=[];");
    	writer.write("$('#' + id).children('li').children('div').each(function () {");
    	writer.write("if ($(this).hasClass('active')){");
    	writer.write("gahret.push($(this).attr('id'));");
    	writer.write("}");
    	writer.write("});");
    	writer.write("};");
    	
    	writer.write("function setactiveheaders(gahret) {");   
    	writer.write("var arrayLength = gahret.length;");
    	writer.write("for (var i = 0; i < arrayLength; i++) {");
     	writer.write("$('#' + gahret[i]).addClass('active');");
     	writer.write("$('#' + gahret[i]).addClass('active2');");
     	writer.write("}");   	
     	writer.write("};");
        
        writer.write("function HasClass(id, cl) {");
        writer.write("var ret='false';");
        writer.write("if ($('#' + id).hasClass(cl)) {");
        writer.write("ret='true';");
        writer.write("}");
        writer.write("return ret;");
        writer.write("};");
                
    	writer.write("function TextAreaResize(item) {");
    	writer.write("$('#' + item).trigger('autoresize')");
    	writer.write("};");
    	writer.write("function TextAreaAllResize() {");
    	writer.write("$('.materialize-textarea').trigger('autoresize')");
    	writer.write("};");
                
        if (NeedsRadio) {
        	writer.write("function getactiveradiobutton(id) {");
        	writer.write("var ret='';");
        	writer.write("$('#' + id).children('p').children('input').each(function () {");
        	writer.write("if ($(this).is(':checked')) {");
        	writer.write("ret=$(this).attr('rbnr');");
        	writer.write("}");
        	writer.write("});");
        	writer.write("return ret;");
        	writer.write("}");
        	
        	writer.write("function setactiveradiobutton(id, act) {");
        	writer.write("$('#' + id + 'input' + act).prop('checked', true);");
        	writer.write("}");
        }
                
        writer.write("function saveinlocalstorage(app,login,pwd) {");
        writer.write("if(typeof(Storage) !== \"undefined\") {");
        writer.write("localStorage.setItem('abm' + app + 'login', login);");
        writer.write("localStorage.setItem('abm' + app + 'pwd', pwd);");
        writer.write("}");
        writer.write("};");
        
        writer.write("function loadfromlocalstorage(app) {");
        writer.write("if(typeof(Storage) !== \"undefined\") {");
        writer.write("var v1=localStorage.getItem('abm' + app + 'login');");
        writer.write("var v2=localStorage.getItem('abm' + app + 'pwd');");
        writer.write("if (v1!==null && v2!==null && v1!=='undefined' && v2!=='undefined' && v1!=='' && v2!=='') {");
        writer.write("return localStorage.getItem('abm' + app + 'login') + ';' + localStorage.getItem('abm' + app + 'pwd');");
        writer.write("}");
        writer.write("}");
        writer.write("return '';");
        writer.write("};");
        
        writer.write("function clearlocalstorage(app) {");
        writer.write("if(typeof(Storage) !== \"undefined\") {");
        writer.write("localStorage.removeItem('abm' + app + 'login');");
        writer.write("localStorage.removeItem('abm' + app + 'pwd');");
        writer.write("}");
        writer.write("};");
        
        writer.write("function saveinlocalstorage2(app,stor) {");
        writer.write("if(typeof(Storage) !== \"undefined\") {");
        writer.write("localStorage.setItem('abm' + app, stor);");
        writer.write("}");
        writer.write("};");
        
        writer.write("function loadfromlocalstorage2(app) {");
        writer.write("if(typeof(Storage) !== \"undefined\") {");
        writer.write("var v1=localStorage.getItem('abm' + app);");
        writer.write("if (v1!==null && v1!=='undefined' && v1!=='') {");
        writer.write("return localStorage.getItem('abm' + app);");
        writer.write("}");
        writer.write("}");
        writer.write("return '';");
        writer.write("};");
        
        writer.write("function clearlocalstorage2(app) {");
        writer.write("if(typeof(Storage) !== \"undefined\") {");
        writer.write("localStorage.removeItem('abm' + app);");
        writer.write("}");
        writer.write("};");
        
        writer.write("function customsaveinlocalstorage(key,value) {");
        writer.write("if(typeof(Storage) !== \"undefined\") {");
        writer.write("localStorage.setItem(key, value);");
        writer.write("}");
        writer.write("};");
        
        writer.write("function customloadfromlocalstorage(key) {");
        writer.write("if(typeof(Storage) !== \"undefined\") {");
        writer.write("return localStorage.getItem(key);");
        writer.write("}");
        writer.write("};");
        
        writer.write("function customdeletefromlocalstorage(key) {");
        writer.write("if(typeof(Storage) !== \"undefined\") {");
        writer.write("localStorage.removeItem(key);");
        writer.write("}");
        writer.write("};");
        
        writer.write("function getbrowserwidthheight() {");
        writer.write("return $(window).width()+';'+$(window).height();");
        writer.write("};");
        
        writer.write("function selectElementContents(el) {");
        writer.write("var range = document.createRange();");
        writer.write("range.selectNodeContents(el);");
        writer.write("var sel = window.getSelection();");
        writer.write("sel.removeAllRanges();");
        writer.write("sel.addRange(range);};");        
        
    	writer.write("function InitEditEnterFields() {");
    	writer.write("$('input:not([type=file],.search-inputfield,.dropdown-button2)').off('keydown').on('keydown', function(e) {");
    	writer.write("if (e.keyCode == \"undefined\") { return };");        	
    	writer.write("var myself = this;");
    	writer.write("var myB4JSKey = $(myself).data('b4js');");
    	writer.write("if (myB4JSKey != '') {");
    	writer.write("var codeToExecute = 'return _b4jsclasses[\"' + myB4JSKey + '\"].' + _b4jsvars[myB4JSKey + '_B4JSOnKeyDown'];");  
    	writer.write("codeToExecute = codeToExecute.replace(/B4JS#!#KEY/g, e.key);");
    	writer.write("codeToExecute = codeToExecute.replace(/B4JS#!#KCODE/g, e.keyCode);");
    	writer.write("var tmpFunc = new Function(codeToExecute);");
    	writer.write("var fret = false;");
    	writer.write("try {");
    	writer.write("fret = tmpFunc()");
    	writer.write("} catch (err) {}");
    	writer.write("if (fret===true) {e.preventDefault();return;}");
    	writer.write("}");        	
    	writer.write("});");
    	
    	writer.write("$('textarea:not([readonly])').off('keydown').on('keydown', function(e) {");
    	writer.write("if (e.keyCode == \"undefined\") { return };");    
    	writer.write("var myself = this;");        	
    	writer.write("var myB4JSKey = $(myself).data('b4js');");
    	writer.write("if (myB4JSKey != '') {");
    	writer.write("var codeToExecute = 'return _b4jsclasses[\"' + myB4JSKey + '\"].' + _b4jsvars[myB4JSKey + '_B4JSOnKeyDown'];");  
    	writer.write("codeToExecute = codeToExecute.replace(/B4JS#!#KEY/g, e.key);");
    	writer.write("codeToExecute = codeToExecute.replace(/B4JS#!#KCODE/g, e.keyCode);");
    	writer.write("var tmpFunc = new Function(codeToExecute);");
    	writer.write("var fret = false;");
    	writer.write("try {");
    	writer.write("fret = tmpFunc()");
    	writer.write("} catch (err) {}");
    	writer.write("if (fret===true) {e.preventDefault();return;}");
    	writer.write("}");
    	writer.write("if (e.keyCode == 9) {");
    	writer.write("if ($(myself).attr('allowtab') == 'true') {");
    	writer.write("e.preventDefault();");
    	writer.write("var val = myself.value,");
    	writer.write("start = myself.selectionStart,");
    	writer.write("end = myself.selectionEnd;");
    	writer.write("myself.value = val.substring(0, start) + '	' + val.substring(end);");
    	writer.write("myself.selectionStart = myself.selectionEnd = start + 1;");
    	writer.write("}");             
    	writer.write("}");
    	writer.write("});");
        	
    	writer.write("$('textarea:not([readonly])').off('keyup').on('keyup', function(e) {");
    	writer.write("if (e.keyCode == \"undefined\") { return };");    
    	writer.write("var myself = this;");        	
    	writer.write("var myB4JSKey = $(myself).data('b4js');");
    	writer.write("if (myB4JSKey != '') {");
    	writer.write("var codeToExecute = 'return _b4jsclasses[\"' + myB4JSKey + '\"].' + _b4jsvars[myB4JSKey + '_B4JSOnKeyUp'];"); 
    	writer.write("codeToExecute = codeToExecute.replace(/B4JS#!#KEY/g, e.key);");
    	writer.write("codeToExecute = codeToExecute.replace(/B4JS#!#KCODE/g, e.keyCode);");
    	writer.write("var tmpFunc = new Function(codeToExecute);");
    	writer.write("var fret = false;");
    	writer.write("try {");
    	writer.write("fret = tmpFunc()");
    	writer.write("} catch (err) {}");
    	writer.write("if (fret===true) {e.preventDefault();return;}");
    	writer.write("}");    
    	writer.write("var done = false;");
    	writer.write("if (e.keyCode == 9) {");
    	writer.write("if ($(myself).attr('allowtab') == 'true') {");
    	writer.write("e.preventDefault();");
    	writer.write("var val = myself.value,");
    	writer.write("start = myself.selectionStart,");
    	writer.write("end = myself.selectionEnd;");
    	writer.write("myself.value = val.substring(0, start) + '	' + val.substring(end);");
    	writer.write("myself.selectionStart = myself.selectionEnd = start + 1;");
    	writer.write("b4j_raiseEvent('page_parseevent', {");
    	writer.write("'eventname': $(this).attr('evname') + '_tabpressed',");
    	writer.write("'eventparams': 'target,value',");
    	writer.write("'target': $(this).attr('evname'),");
    	writer.write("'value': $(this).val()");
    	writer.write("});");
    	writer.write("done = true;");
    	writer.write("}");
    	writer.write("}");
    	writer.write("if (e.keyCode == 13) {");
    	writer.write("b4j_raiseEvent('page_parseevent', {");
    	writer.write("'eventname': $(this).attr('evname') + '_enterpressed',");
    	writer.write("'eventparams': 'value',");
    	writer.write("'value': $(this).val()");
    	writer.write("});");
    	writer.write("done = true;");
    	writer.write("}");
    	writer.write("if (done == false) {");
    	writer.write("if ($(this).hasClass('raisechanged')) {");
    	writer.write("b4j_raiseEvent('page_parseevent', {");
    	writer.write("'eventname': $(this).attr('evname') + '_changed',");
    	writer.write("'eventparams': 'value',");
    	writer.write("'value': $(this).val()");
    	writer.write("});");
    	writer.write("}");
    	writer.write("if ($(this).val().length == 0) {");
    	writer.write("$('#' + $(this).attr('id') + 'req').removeClass('hide');");
    	writer.write("} else {");
    	writer.write("$('#' + $(this).attr('id') + 'req').addClass('hide');");
    	writer.write("}");
    	writer.write("}");
    	writer.write("var nextthis = $(this).next();");
    	writer.write("if (nextthis.hasClass('abmcloseinp-icon')) {");
    	writer.write("if ($(this).val() == '') {");
    	writer.write("nextthis.addClass('hide');");
    	writer.write("} else {");
    	writer.write("nextthis.removeClass('hide');");
    	writer.write("}");
    	writer.write("}");
    	writer.write("});");
    	
    	writer.write("$('input:not([type=file],.search-inputfield,.dropdown-button2)').off('keyup').on('keyup', function(e) {");
    	writer.write("if (e.keyCode == \"undefined\") { return };");        	
    	writer.write("var myself = this;");
    	writer.write("var myB4JSKey = $(myself).data('b4js');");
    	writer.write("if (myB4JSKey != '') {");
    	writer.write("var codeToExecute = 'return _b4jsclasses[\"' + myB4JSKey + '\"].' + _b4jsvars[myB4JSKey + '_B4JSOnKeyUp'];");
    	writer.write("codeToExecute = codeToExecute.replace(/B4JS#!#KEY/g, e.key);");
    	writer.write("codeToExecute = codeToExecute.replace(/B4JS#!#KCODE/g, e.keyCode);");
    	writer.write("var tmpFunc = new Function(codeToExecute);");
    	writer.write("var fret = false;");
    	writer.write("try {");
    	writer.write("fret = tmpFunc()");
    	writer.write("} catch (err) {}");
    	writer.write("if (fret===true) {e.preventDefault();return;}");
    	writer.write("}");
        	   	
    	writer.write("var done=false;");
    	writer.write("if (e.keyCode==9) {");        	
    	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': $(this).attr('evname') + '_tabpressed','eventparams': 'target,value', 'target':$(this).attr('evname') ,'value':$(this).val()});");
    	writer.write("done=true;");
    	writer.write("}");
    	writer.write("if(e.keyCode==13) {");
    	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': $(this).attr('evname') + '_enterpressed','eventparams': 'value', 'value':$(this).val()});");
    	writer.write("done=true;");
    	writer.write("}");  
    	writer.write("if (done==false) {");
    	writer.write("if ($(this).hasClass('raisechanged')) {");
    	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': $(this).attr('evname') + '_changed','eventparams': 'value', 'value':$(this).val()});");
    	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': $(this).attr('evname') + '_changedarray','eventparams': 'target,value', 'target': $(this).attr('idname'), 'value':$(this).val()});");
    	writer.write("}");  
    	writer.write("if ($(this).val().length==0) {");
    	writer.write("$('#' + $(this).attr('id') + 'req').removeClass('hide');");
    	writer.write("} else {");
    	writer.write("$('#' + $(this).attr('id') + 'req').addClass('hide');");
    	writer.write("}");
    	writer.write("if ($(this).hasClass('raisechangedcheck')) {");
    	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': $(this).attr('evname') + '_changed','eventparams': 'value', 'value':$(this).val()});");
    	writer.write("if ($(this).hasClass('validateabm')) {$(this).removeClass(\"invalid\");$(this).removeClass(\"valid\");if ($(this).val().length==0) {$(this).addClass(\"invalid\");} else {$(this).addClass(\"valid\");}};");
    	writer.write("}");
    	writer.write("}");
    	writer.write("var nextthis = $(this).next();");
    	writer.write("if (nextthis.hasClass('abmcloseinp-icon')) {");
        	writer.write("if ($(this).val() == '') {");
				writer.write("nextthis.addClass('hide');");
            writer.write("} else {");
            	writer.write("nextthis.removeClass('hide');");
            writer.write("}");
        writer.write("}");
    	writer.write("});");
    	        	
    	writer.write("$('input:not([type=file],.search-inputfield,.dropdown-button2)').off('change paste').on('change paste', function(e) {");
    	writer.write("if (e.keyCode == \"undefined\") { return };");
    	writer.write("if ($(this).hasClass('raisechanged')) {");
    	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': $(this).attr('evname') + '_changed','eventparams': 'value', 'value':$(this).val()});");
    	writer.write("}"); 
    	writer.write("if ($(this).val().length==0) {");
    	writer.write("$('#' + $(this).attr('id') + 'req').removeClass('hide');");
    	writer.write("} else {");
    	writer.write("$('#' + $(this).attr('id') + 'req').addClass('hide');");
    	writer.write("}");
    	writer.write("if ($(this).hasClass('raisechangedcheck')) {");
    	writer.write("b4j_raiseEvent('page_parseevent', {'eventname': $(this).attr('evname') + '_changed','eventparams': 'value', 'value':$(this).val()});");
    	writer.write("if ($(this).hasClass('validateabm')) {$(this).removeClass(\"invalid\");$(this).removeClass(\"valid\");if ($(this).val().length==0) {$(this).addClass(\"invalid\");} else {$(this).addClass(\"valid\");}};");
    	writer.write("}");    	
    	writer.write("var nextthis = $(this).next();");
    	writer.write("if (nextthis.hasClass('abmcloseinp-icon')) {");
        	writer.write("if ($(this).val() == '') {");
				writer.write("nextthis.addClass('hide');");
            writer.write("} else {");
            	writer.write("nextthis.removeClass('hide');");
            writer.write("}");
		writer.write("}");    	   	
    	writer.write("});");
    	        	        	
    	writer.write("}");
        
        if (NeedsCombo) {
        	writer.write("$.fn.dropdown2=function(a){var b={inDuration:300,outDuration:225,constrain_width:!0,hover:!1,gutter:0,belowOrigin:!1,alignment:\"left\"};this.each(function(){function c(){void 0!==f.data(\"induration\")&&(g.inDuration=f.data(\"inDuration\")),void 0!==f.data(\"outduration\")&&(g.outDuration=f.data(\"outDuration\")),void 0!==f.data(\"constrainwidth\")&&(g.constrain_width=f.data(\"constrainwidth\")),void 0!==f.data(\"hover\")&&(g.hover=f.data(\"hover\")),void 0!==f.data(\"gutter\")&&(g.gutter=f.data(\"gutter\")),void 0!==f.data(\"beloworigin\")&&(g.belowOrigin=f.data(\"beloworigin\")),void 0!==f.data(\"alignment\")&&(g.alignment=f.data(\"alignment\"))}function d(){c(),$(\"#\"+f.attr(\"id\")+\"label\").css(\"color\",$(\"#\"+f.attr(\"id\")+\"label\").attr(\"activecolor\")),h.addClass(\"active\"),g.constrain_width===!0?h.css(\"width\",f.outerWidth()):h.css(\"white-space\",\"nowrap\");var a=0;g.belowOrigin===!0&&(a=f.height());var b,d,e,i=f.offset().left;i+h.innerWidth()>$(window).width()?g.alignment=\"right\":i-h.innerWidth()+f.innerWidth()<0&&(g.alignment=\"left\"),\"left\"===g.alignment?(d=0,e=g.gutter,b=f.position().left+d+e,h.css({left:b})):\"right\"===g.alignment&&($(window).width()-i-f.innerWidth(),d=0,e=g.gutter,b=$(window).width()-f.position().left-f.innerWidth()+e,h.css({right:b})),h.css({position:\"absolute\",top:f.position().top+a}),h.stop(!0,!0).css(\"opacity\",0).slideDown({queue:!1,duration:g.inDuration,easing:\"easeOutCubic\",complete:function(){$(this).css(\"height\",\"\")}}).animate({opacity:1},{queue:!1,duration:g.inDuration,easing:\"easeOutSine\"})}function e(){h.fadeOut(g.outDuration),h.removeClass(\"active\"),$(f).removeClass(\"invalid\"),$(f).removeClass(\"valid\"),$(\"#\"+f.attr(\"id\")+\"label\").css(\"color\",$(\"#\"+f.attr(\"id\")+\"label\").attr(\"inactivecolor\")),0==f.val().length?($(f).addClass(\"invalid\"),$(\"#\"+f.attr(\"id\")+\"label\").removeClass(\"active\")):($(f).addClass(\"valid\"),$(\"#\"+$(\"#\"+f.attr(\"id\")+\"label\").attr(\"iconid\")).css(\"color\",$(\"#\"+f.attr(\"id\")+\"label\").attr(\"activecolor\")),$(\"#\"+f.attr(\"id\")+\"label\").addClass(\"active\"))}var f=$(this),g=$.extend({},b,a),h=$(\"#\"+f.attr(\"data-activates\"));if(label=$(\"#\"+f.attr(\"id\")+\"label\"),c(),f.after(h),g.hover){var i=!1;f.unbind(\"click.\"+f.attr(\"id\")),f.on(\"mouseenter\",function(){i===!1&&(d(),i=!0)}),f.on(\"mouseleave\",function(a){var b=a.toElement||a.relatedTarget;$(b).closest(\".dropdown-content\").is(h)||(h.stop(!0,!0),e(),i=!1)}),h.on(\"mouseleave\",function(a){var b=a.toElement||a.relatedTarget;$(b).closest(\".dropdown-button2\").is(f)||(h.stop(!0,!0),e(),i=!1)})}else f.unbind(\"click.\"+f.attr(\"id\")),f.bind(\"click.\"+f.attr(\"id\"),function(a){f[0]==a.currentTarget&&0===$(a.target).closest(\".dropdown-content\").length?d():f.hasClass(\"active\")&&(e(),$(document).unbind(\"click.\"+h.attr(\"id\"))),h.hasClass(\"active\")&&$(document).bind(\"click.\"+h.attr(\"id\"),function(a){!h.is(a.target)&&!f.is(a.target)&&!f.find(a.target).length>0&&(e(),$(document).unbind(\"click.\"+h.attr(\"id\")))}),f.on(\"keydown\",function(a){9==a.keyCode&&e()})});f.on(\"open\",d),f.on(\"close\",e),0==f.val().length?$(\"#\"+f.attr(\"id\")+\"label\").removeClass(\"active\"):$(\"#\"+f.attr(\"id\")+\"label\").addClass(\"active\")})};");
        } else {
        	writer.write("$.fn.dropdown2=function(t){};"); // for modal sheet refresh
        }
        if (this.NeedsYouTube) {
        	 writer.write("var players = {};");
        	 writer.write("function onYouTubePlayerAPIReady(){");
        	 writer.write("window.players={};");  
        	 writer.write("$('.video-container').each( function () {");
        	 writer.write("var frameId = $(this).children('iframe').attr('id');");
        	 writer.write("video = document.getElementById(frameId);");		
        	 writer.write("players[frameId]=new YT.Player( frameId, {");
        	 writer.write("events: {");
        	 writer.write("'onReady': function (event) {");
        	 writer.write("players[frameId].addEventListener('onStateChange', function(e) {");
        	 writer.write("});");
        	 writer.write("},");
        	 writer.write("'onStateChange': function (event) {");
        	 writer.write("var video=event.target.getIframe().getAttribute('id');");
        	 writer.write("b4j_raiseEvent('page_parseevent', {'eventname': video + '_youtubestatechanged','eventparams': 'state', 'state': event.data});");
        	 writer.write("}");
        	 writer.write("}");			
        	 writer.write("});");
        	 writer.write("});");
        	 writer.write("}");   	            	
        	 
        	 writer.write("function playyoutube(id) {");
        	 writer.write("players[id].playVideo();");
        	 writer.write("}");
        	 writer.write("function stopyoutube(id) {");
        	 writer.write("players[id].stopVideo();");
        	 writer.write("players[id].seekTo(0);");
        	 writer.write("}");
        	 writer.write("function pauseyoutube(id) {");
        	 writer.write("players[id].pauseVideo();");
        	 writer.write("}");
        	 writer.write("function setvolumeyoutube(id, volume) {");
        	 writer.write("players[id].setVolume(volume);");
        	 writer.write("}");
        	 writer.write("function muteyoutube(id) {");
        	 writer.write("players[id].mute();");
        	 writer.write("}");
        	 writer.write("function unmuteyoutube(id) {");
        	 writer.write("players[id].unMute();");
        	 writer.write("}");
        	 writer.write("function cueyoutube(id, videoid) {");
        	 writer.write("players[id].cueVideoById(videoid);");
        	 writer.write("}");
        	 writer.write("function cueplaynextyoutube(id) {");
        	 writer.write("players[id].nextVideo();");
        	 writer.write("}");
        }
        if (NeedsHTML5Video) {
        	writer.write("function playhtml5(id) {");
        	writer.write("var myVideo = document.getElementById(id + '-parent');"); 
   	 		writer.write("myVideo.play();");
   	 		writer.write("}");
   	 		writer.write("function stophtml5(id) {");
   	 		writer.write("var myVideo = document.getElementById(id + '-parent');");
   	 		writer.write("myVideo.load();");
   	 		writer.write("}");
   	 		writer.write("function pausehtml5(id) {");
   	 		writer.write("var myVideo = document.getElementById(id + '-parent');");
   	 		writer.write("myVideo.pause();");
   	 		writer.write("}");
   	 		writer.write("function setvolumehtml5(id, volume) {");
   	 		writer.write("var myVideo = document.getElementById(id + '-parent');");
   	 		writer.write("myVideo.volume = volume/100;");
   	 		writer.write("}");
   	 		writer.write("function mutehtml5(id) {");
   	 		writer.write("var myVideo = document.getElementById(id + '-parent');");
   	 		writer.write("myVideo.muted = true;");
   	 		writer.write("}");
   	 		writer.write("function unmutehtml5(id) {");
   	 		writer.write("var myVideo = document.getElementById(id + '-parent');");
   	 		writer.write("myVideo.muted = false;");
   	 		writer.write("}");
        }
   	 	
   	 	if (NeedsGoogleMap) {
   	 		writer.write("function markerclickarray(id, markerid) {");
   	 		writer.write("	b4j_raiseEvent('page_parseevent', {'eventname': id + '_markerclicked','eventparams': 'markerid', 'markerid': markerid});");	
   	 		writer.write("}");
   	 		
   	 		writer.write("function markerdragstart(id, markerid) {");
	 		writer.write("	b4j_raiseEvent('page_parseevent', {'eventname': id + '_markerdragstart','eventparams': 'markerid', 'markerid': markerid});");	
	 		writer.write("}");
	 		
	 		writer.write("function markerdragend(id, lat, lng) {");
	 		writer.write("	b4j_raiseEvent('page_parseevent', {'eventname': id + '_markerdragend','eventparams': 'latitude,longitude', 'latitude': lat, 'longitude': lng});");	
	 		writer.write("}");
   	 		
   	 		writer.write("function gmclickarray(id, ll) {");
   	 		writer.write("	b4j_raiseEvent('page_parseevent', {'eventname': id + '_clicked','eventparams': 'latitude,longitude', 'latitude': ll.lat(), 'longitude': ll.lng()});");	
   	 		writer.write("}");
   	 		writer.write("function gmready(id) {");
   	 		writer.write("	b4j_raiseEvent('page_parseevent', {'eventname': id + '_ready','eventparams': ''});");	
   	 		writer.write("}");
   	 		writer.write("function gmgeolocate(id) {");
   	 		writer.write("GMaps.geolocate({");
   	 		writer.write("success: function(position){");
   	        writer.write("b4j_raiseEvent('page_parseevent', {'eventname': id + '_currentlocation','eventparams': 'latitude,longitude', 'latitude': position.coords.latitude, 'longitude': position.coords.longitude});");
   	        writer.write("},");
   	        writer.write("error: function(error){");
   	        writer.write("b4j_raiseEvent('page_parseevent', {'eventname': id + '_error','eventparams': 'errormessage', 'errormessage' : 'Geolocation failed: '+error.message});");
   	        writer.write("},");
   	        writer.write("not_supported: function(){");
   	        writer.write("b4j_raiseEvent('page_parseevent', {'eventname': id + '_error','eventparams': 'errormessage', 'errormessage' : 'Your browser does not support geolocation'});");
   	        writer.write("},");       	        
   	        writer.write("});");
   	        writer.write("}");
   	        writer.write("function gmsetlocation(id,latitude,longitude) {");	
	 		writer.write("gms[id].setCenter(latitude, longitude);");	
	 		writer.write("}");
	 		
	 		
	 		writer.write("function gmgeocode(id,addr) {");
	 		writer.write("GMaps.geocode({");
	 		writer.write("address: \"'\" + addr + \"'\",");
	 		writer.write("callback: function(results, status){");
	 		writer.write("if(status=='OK'){");
	 		writer.write("var latlng = results[0].geometry.location;");
	 	    writer.write("b4j_raiseEvent('page_parseevent', {'eventname': id + '_geocoderesult','eventparams': 'latitude,longitude', 'latitude': latlng.lat(), 'longitude': latlng.lng()});");
	 	    writer.write("} else {");
	 	    writer.write("b4j_raiseEvent('page_parseevent', {'eventname': id + '_error','eventparams': 'errormessage', 'errormessage' : 'Error finding address'});");	
	 	    writer.write("}");
	 	    writer.write("}");
	 	    writer.write("});");
	 	    writer.write("}");
	 	    
	 	    writer.write("function gmreversegeocode(id,lat,lng) {");
	 	    writer.write("var latlng = new google.maps.LatLng(lat, lng);");
 			writer.write("GMaps.geocode({");
 			writer.write("latLng: latlng,");
 			writer.write("callback: function(results, status){");
 			writer.write("if(status=='OK'){");
 			writer.write("b4j_raiseEvent('page_parseevent', {'eventname': id + '_reversegeocoderesult','eventparams': 'address', 'address': results[0].formatted_address});");
 			writer.write("} else {");
 			writer.write("b4j_raiseEvent('page_parseevent', {'eventname': id + '_error','eventparams': 'errormessage', 'errormessage' : 'Error finding address'});");	
 			writer.write("}");
 			writer.write("}");
 			writer.write("});");
 			writer.write("}");
   	 		       	 		
   	 		writer.write("function gmrefresh(id, height, width) {");
   	 		writer.write("$('#' + id).css(\"width\", width);");
   	 		writer.write("$('#' + id).css(\"height\", height);");
   	 		writer.write("gms[id].refresh();");
   	 		writer.write("}");
   	 		
   	 		writer.write("function gmremovemarkers(id) {");
   	 		writer.write("gms[id].removeMarkers();");
   	 		writer.write("}");   	 		 
   	 		
	 		writer.write("function gmremovemarker(el) {");
	 			writer.write("var id = $(el).data(\"marker-id\");");
	 			writer.write("var markerid = $(el).data(\"marker-markid\");");
	 			writer.write("$.each(gms[id].markers, function(index, marker) {");
	 			writer.write("if (marker) {");
	 				writer.write("if (marker.get('id')==markerid) {");
	 					writer.write("gms[id].removeMarker(gms[id].markers[index]);");
	 					writer.write("b4j_raiseEvent('page_parseevent', {'eventname': id + '_markerremoved','eventparams': 'markerid', 'markerid': markerid});");
	 					writer.write("return;");
	 				writer.write("}");
	 				writer.write("}");
	 			writer.write("});");
	 		writer.write("};");
	 		
	 		writer.write("function gmremovemarkerman(id, markerid) {");
 			writer.write("$.each(gms[id].markers, function(index, marker) {");
 				writer.write("if (marker) {");
 				writer.write("if (marker.get('id')==markerid) {");
 					writer.write("gms[id].removeMarker(gms[id].markers[index]);");
 					writer.write("b4j_raiseEvent('page_parseevent', {'eventname': id + '_markerremoved','eventparams': 'markerid', 'markerid': markerid});");
 					writer.write("return;");
 				writer.write("}");
 				writer.write("}");
 			writer.write("});");
	 		writer.write("};");
	 		
	 		writer.write("function gmgetmarker(id, markerid) {");
	 		writer.write("var ret;");
 			writer.write("$.each(gms[id].markers, function(index, marker) {");
 				writer.write("if (marker) {");
 				writer.write("if (marker.get('id')==markerid) {");
 					writer.write("ret = marker;");
 					writer.write("return marker;");
 				writer.write("}");
 				writer.write("}");
 			writer.write("});");
 			writer.write("return ret;");
	 		writer.write("};");
	 		
	 		writer.write("function gmbuildfences(id,pids) {");
	 		writer.write("var ret=[];");
	 		writer.write("var arrayLength = pids.length;");
	 		writer.write("for (var i = 0; i < arrayLength; i++) {");
	 		writer.write("var s = pids[i];");
	 		writer.write("ret.push(gmgetpolygon(id,s));");
	 		writer.write("}");
	 		writer.write("return ret;");
	 		writer.write("}");
	   	 		
	 		writer.write("function gmaddmarker(id, markerid, latitude, longitude, markercolor, title, infowindow, showdel, pids) {"); 
	 		writer.write("var pids2 = eval(pids);");
	 		writer.write("var fcs=gmbuildfences(id,pids2);");
	 		writer.write("if (infowindow=='') {");
	 		writer.write("if (fcs.length) {");
	 			writer.write("gms[id].addMarker({id: markerid, lat: latitude,  lng: longitude,  icon: \"http://maps.google.com/mapfiles/ms/icons/\" + markercolor + \".png\", title: title,fences: fcs,outside: function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_outsidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});},inside: function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_insidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});},  click: function(e) {    markerclickarray(id, markerid);  }});");
	 		writer.write("} else {");
	 			writer.write("gms[id].addMarker({id: markerid, lat: latitude,  lng: longitude,  icon: \"http://maps.google.com/mapfiles/ms/icons/\" + markercolor + \".png\", title: title,  click: function(e) {    markerclickarray(id, markerid);  }});");
	 		writer.write("}");
	 		writer.write("} else {");
	 		writer.write("var infodel=\"\";");
	 		writer.write("if (showdel) {");
	 		writer.write("infodel = '<a id=\"'+markerid+'-iw\" onclick=\"gmremovemarker(this)\" class=\"btn-floating btn-small waves-effect waves-light red remove-marker\" data-marker-id=\"'+id+'\" data-marker-markid=\"'+markerid+'\" style=\"margin-left:50%\"><i class=\"material-icons\">delete</i></a>';");
	 		writer.write("}");
	 		writer.write("if (fcs.length) {");
	 			writer.write("gms[id].addMarker({id: markerid, lat: latitude,  lng: longitude,  icon: \"http://maps.google.com/mapfiles/ms/icons/\" + markercolor + \".png\", title: title,fences: fcs,outside: function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_outsidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});},inside: function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_insidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});},  click: function(e) {    markerclickarray(id, markerid);  }, infoWindow: {content: '<p>' + infowindow + '</p>' + infodel}});");
	 		writer.write("} else {");
	 			writer.write("gms[id].addMarker({id: markerid, lat: latitude,  lng: longitude,  icon: \"http://maps.google.com/mapfiles/ms/icons/\" + markercolor + \".png\", title: title,  click: function(e) {    markerclickarray(id, markerid);  }, infoWindow: {content: '<p>' + infowindow + '</p>' + infodel}});");
	 		writer.write("}");
	 		writer.write("}");
	 		writer.write("}");
	 		
	 		writer.write("function gmaddmarkerdraggable(id, markerid, latitude, longitude, markercolor, title, infowindow, showcross, showdel, pids) {");
	 		writer.write("var pids2 = eval(pids);");
	 		writer.write("var fcs=gmbuildfences(id,pids2);");
	 		writer.write("if (infowindow=='') {");
	 		writer.write("if (fcs.length) {");
	 			writer.write("gms[id].addMarker({id: markerid, lat: latitude,  lng: longitude, 'draggable': true, crossOnDrag: showcross, dragstart: function() {markerdragstart(id, markerid)}, dragend: function(event) {markerdragend(id,event.latLng.lat(),event.latLng.lng())},  icon: \"http://maps.google.com/mapfiles/ms/icons/\" + markercolor + \".png\", title: title,fences: fcs,outside: function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_outsidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});},inside: function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_insidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});}, click: function(e) {    markerclickarray(id, markerid);  }});");
	 		writer.write("} else {");
	 			writer.write("gms[id].addMarker({id: markerid, lat: latitude,  lng: longitude, 'draggable': true, crossOnDrag: showcross, dragstart: function() {markerdragstart(id, markerid)}, dragend: function(event) {markerdragend(id,event.latLng.lat(),event.latLng.lng())},  icon: \"http://maps.google.com/mapfiles/ms/icons/\" + markercolor + \".png\", title: title,  click: function(e) {    markerclickarray(id, markerid);  }});");
	 		writer.write("}");	
	 		writer.write("} else {");
	 		writer.write("var infodel=\"\";");
	 		writer.write("if (showdel) {");
	 		writer.write("infodel = '<a id=\"'+markerid+'-iw\" onclick=\"gmremovemarker(this)\" class=\"btn-floating btn-small waves-effect waves-light red remove-marker\" data-marker-id=\"'+id+'\" data-marker-markid=\"'+markerid+'\" style=\"margin-left:50%\"><i class=\"material-icons\">delete</i></a>';");
	 		writer.write("}");
	 		writer.write("if (fcs.length) {");
	 			writer.write("gms[id].addMarker({id: markerid, lat: latitude,  lng: longitude, 'draggable': true, crossOnDrag: showcross, dragstart: function() {markerdragstart(id, markerid)}, dragend: function(event) {markerdragend(id,event.latLng.lat(),event.latLng.lng())},  icon: \"http://maps.google.com/mapfiles/ms/icons/\" + markercolor + \".png\", title: title,fences: fcs,outside: function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_outsidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});},inside: function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_insidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});},  click: function(e) {    markerclickarray(id, markerid);  }, infoWindow: {content: '<p>' + infowindow + '</p>' + infodel}});");
	 		writer.write("} else {");
	 			writer.write("gms[id].addMarker({id: markerid, lat: latitude,  lng: longitude, 'draggable': true, crossOnDrag: showcross, dragstart: function() {markerdragstart(id, markerid)}, dragend: function(event) {markerdragend(id,event.latLng.lat(),event.latLng.lng())},  icon: \"http://maps.google.com/mapfiles/ms/icons/\" + markercolor + \".png\", title: title,  click: function(e) {    markerclickarray(id, markerid);  }, infoWindow: {content: '<p>' + infowindow + '</p>' + infodel}});");
		 	writer.write("}");	
	 		writer.write("}");
	 		writer.write("}");
	 		
	 		writer.write("function gmaddmarkerex(id, markerid, latitude, longitude, title, infowindow, iconpngurl, permanent, showdel, pids) {");
	 		writer.write("var pids2 = eval(pids);");
	 		writer.write("var fcs=gmbuildfences(id,pids2);");
	 		writer.write("if (infowindow=='') {");
	 		writer.write("if (fcs.length) {");
	 			writer.write("gms[id].addMarker({id: markerid, lat: latitude,  lng: longitude,  icon: iconpngurl, title: title,fences: fcs,outside: function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_outsidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});},inside: function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_insidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});},  click: function(e) {    markerclickarray(id, markerid);  }});");
	 		writer.write("} else {");
	 			writer.write("gms[id].addMarker({id: markerid, lat: latitude,  lng: longitude,  icon: iconpngurl, title: title,  click: function(e) {    markerclickarray(id, markerid);  }});");
		 	writer.write("}");	
	 		writer.write("} else {");
	 		writer.write("var infodel=\"\";");
	 		writer.write("if (showdel) {");
	 		writer.write("infodel = '<a id=\"'+markerid+'-iw\" onclick=\"gmremovemarker(this)\" class=\"btn-floating btn-small waves-effect waves-light red remove-marker\" data-marker-id=\"'+id+'\" data-marker-markid=\"'+markerid+'\" style=\"margin-left:50%\"><i class=\"material-icons\">delete</i></a>';");
	 		writer.write("}");
	 		writer.write("if (fcs.length) {");
	 			writer.write("gms[id].addMarker({id: markerid, lat: latitude,  lng: longitude,  icon: iconpngurl, title: title,fences: fcs,outside: function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_outsidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});},inside: function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_insidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});},  click: function(e) {    markerclickarray(id, markerid);  }, infoWindow: {content: '<p>' + infowindow + '</p>' + infodel}, infoWindowOpen: permanent});");
	 		writer.write("} else {");
	 			writer.write("gms[id].addMarker({id: markerid, lat: latitude,  lng: longitude,  icon: iconpngurl, title: title,  click: function(e) {    markerclickarray(id, markerid);  }, infoWindow: {content: '<p>' + infowindow + '</p>' + infodel}, infoWindowOpen: permanent});");
		 	writer.write("}");	
	 		writer.write("}");
	 		writer.write("}");
	 		
	 		writer.write("function gmaddmarkerexdraggable(id, markerid, latitude, longitude, title, infowindow, iconpngurl, permanent, showcross, showdel, pids) {");
	 		writer.write("var pids2 = eval(pids);");
	 		writer.write("var fcs=gmbuildfences(id,pids2);");
	 		writer.write("if (infowindow=='') {");
	 		writer.write("if (fcs.length) {");
	 			writer.write("gms[id].addMarker({id: markerid, lat: latitude,  lng: longitude, 'draggable': true, crossOnDrag: showcross, dragstart: function() {markerdragstart(id, markerid)}, dragend: function(event) {markerdragend(id,event.latLng.lat(),event.latLng.lng())},  icon: iconpngurl, title: title,fences: fcs,outside: function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_outsidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});},inside: function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_insidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});},  click: function(e) {    markerclickarray(id, markerid);  }});");
	 		writer.write("} else {");
	 			writer.write("gms[id].addMarker({id: markerid, lat: latitude,  lng: longitude, 'draggable': true, crossOnDrag: showcross, dragstart: function() {markerdragstart(id, markerid)}, dragend: function(event) {markerdragend(id,event.latLng.lat(),event.latLng.lng())},  icon: iconpngurl, title: title,  click: function(e) {    markerclickarray(id, markerid);  }});");
		 	writer.write("}");	
	 		writer.write("} else {");
	 		writer.write("var infodel=\"\";");
	 		writer.write("if (showdel) {");
	 		writer.write("infodel = '<a id=\"'+markerid+'-iw\" onclick=\"gmremovemarker(this)\" class=\"btn-floating btn-small waves-effect waves-light red remove-marker\" data-marker-id=\"'+id+'\" data-marker-markid=\"'+markerid+'\" style=\"margin-left:50%\"><i class=\"material-icons\">delete</i></a>';");
	 		writer.write("}");
	 		writer.write("if (fcs.length) {");
	 			writer.write("gms[id].addMarker({id: markerid, lat: latitude,  lng: longitude, 'draggable': true, crossOnDrag: showcross, dragstart: function() {markerdragstart(id, markerid)}, dragend: function(event) {markerdragend(id,event.latLng.lat(),event.latLng.lng())},  icon: iconpngurl, title: title,fences: fcs,outside: function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_outsidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});},inside: function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_insidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});},  click: function(e) {    markerclickarray(id, markerid);  }, infoWindow: {content: '<p>' + infowindow + '</p>' + infodel}, infoWindowOpen: permanent});");
	 		writer.write("} else {");
	 			writer.write("gms[id].addMarker({id: markerid, lat: latitude,  lng: longitude, 'draggable': true, crossOnDrag: showcross, dragstart: function() {markerdragstart(id, markerid)}, dragend: function(event) {markerdragend(id,event.latLng.lat(),event.latLng.lng())},  icon: iconpngurl, title: title,  click: function(e) {    markerclickarray(id, markerid);  }, infoWindow: {content: '<p>' + infowindow + '</p>' + infodel}, infoWindowOpen: permanent});");
		 	writer.write("}");	
	 		writer.write("}");
	 		writer.write("}");
	 		
	 		writer.write("function gmaddmarkerex2(id, markerid, latitude, longitude, title, infowindow, iconpngurl, permanent, anchorx, anchory, showdel, pids) {");
	 		writer.write("var pids2 = eval(pids);");
	 		writer.write("var fcs=gmbuildfences(id,pids2);");
	 		writer.write("if (infowindow=='') {");
	 		writer.write("if (fcs.length) {");
	 			writer.write("gms[id].addMarker({id: markerid, lat: latitude,  lng: longitude, 'icon': {'url': iconpngurl, 'anchor': new google.maps.Point(anchorx, anchory)}, title: title,fences: fcs,outside: function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_outsidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});},inside: function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_insidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});},  click: function(e) {    markerclickarray(id, markerid);  }});");
	 		writer.write("} else {");
	 			writer.write("gms[id].addMarker({id: markerid, lat: latitude,  lng: longitude, 'icon': {'url': iconpngurl, 'anchor': new google.maps.Point(anchorx, anchory)}, title: title,  click: function(e) {    markerclickarray(id, markerid);  }});");
		 	writer.write("}");	
	 		writer.write("} else {");
	 		writer.write("var infodel=\"\";");
	 		writer.write("if (showdel) {");
	 		writer.write("infodel = '<a id=\"'+markerid+'-iw\" onclick=\"gmremovemarker(this)\" class=\"btn-floating btn-small waves-effect waves-light red remove-marker\" data-marker-id=\"'+id+'\" data-marker-markid=\"'+markerid+'\" style=\"margin-left:50%\"><i class=\"material-icons\">delete</i></a>';");
	 		writer.write("}");
	 		writer.write("if (fcs.length) {");
	 			writer.write("gms[id].addMarker({id: markerid, lat: latitude,  lng: longitude, 'icon': {'url': iconpngurl, 'anchor': new google.maps.Point(anchorx, anchory)}, title: title,fences: fcs,outside: function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_outsidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});},inside: function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_insidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});},  click: function(e) {    markerclickarray(id, markerid);  }, infoWindow: {content: '<p>' + infowindow + '</p>' + infodel}, infoWindowOpen: permanent});");
	 		writer.write("} else {");
	 			writer.write("gms[id].addMarker({id: markerid, lat: latitude,  lng: longitude, 'icon': {'url': iconpngurl, 'anchor': new google.maps.Point(anchorx, anchory)}, title: title,  click: function(e) {    markerclickarray(id, markerid);  }, infoWindow: {content: '<p>' + infowindow + '</p>' + infodel}, infoWindowOpen: permanent});");
		 	writer.write("}");	
	 		writer.write("}");
	 		writer.write("}");
	 		
	 		writer.write("function gmaddmarkerex2draggable(id, markerid, latitude, longitude, title, infowindow, iconpngurl, permanent, anchorx, anchory, showcross, showdel, pids) {");
	 		writer.write("var pids2 = eval(pids);");
	 		writer.write("var fcs=gmbuildfences(id,pids2);");
	 		writer.write("if (infowindow=='') {");
	 		writer.write("if (fcs.length) {");
	 			writer.write("gms[id].addMarker({id: markerid, lat: latitude,  lng: longitude, 'draggable': true, crossOnDrag: showcross, dragstart: function() {markerdragstart(id, markerid)}, dragend: function(event) {markerdragend(id,event.latLng.lat(),event.latLng.lng())},  'icon': {'url': iconpngurl, 'anchor': new google.maps.Point(anchorx, anchory)}, title: title,fences: fcs,outside: function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_outsidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});},inside: function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_insidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});},  click: function(e) {    markerclickarray(id, markerid);  }});");
	 		writer.write("} else {");
	 			writer.write("gms[id].addMarker({id: markerid, lat: latitude,  lng: longitude, 'draggable': true, crossOnDrag: showcross, dragstart: function() {markerdragstart(id, markerid)}, dragend: function(event) {markerdragend(id,event.latLng.lat(),event.latLng.lng())},  'icon': {'url': iconpngurl, 'anchor': new google.maps.Point(anchorx, anchory)}, title: title,  click: function(e) {    markerclickarray(id, markerid);  }});");	
		 	writer.write("}");	
	 		writer.write("} else {");
	 		writer.write("var infodel=\"\";");
	 		writer.write("if (showdel) {");
	 		writer.write("infodel = '<a id=\"'+markerid+'-iw\" onclick=\"gmremovemarker(this)\" class=\"btn-floating btn-small waves-effect waves-light red remove-marker\" data-marker-id=\"'+id+'\" data-marker-markid=\"'+markerid+'\" style=\"margin-left:50%\"><i class=\"material-icons\">delete</i></a>';");
	 		writer.write("}");
	 		writer.write("if (fcs.length) {");
	 			writer.write("gms[id].addMarker({id: markerid, lat: latitude,  lng: longitude, 'draggable': true, crossOnDrag: showcross, dragstart: function() {markerdragstart(id, markerid)}, dragend: function(event) {markerdragend(id,event.latLng.lat(),event.latLng.lng())},  'icon': {'url': iconpngurl, 'anchor': new google.maps.Point(anchorx, anchory)}, title: title,fences: fcs,outside: function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_outsidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});},inside: function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_insidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});},  click: function(e) {    markerclickarray(id, markerid);  }, infoWindow: {content: '<p>' + infowindow + '</p>' + infodel}, infoWindowOpen: permanent});");
	 		writer.write("} else {");
	 			writer.write("gms[id].addMarker({id: markerid, lat: latitude,  lng: longitude, 'draggable': true, crossOnDrag: showcross, dragstart: function() {markerdragstart(id, markerid)}, dragend: function(event) {markerdragend(id,event.latLng.lat(),event.latLng.lng())},  'icon': {'url': iconpngurl, 'anchor': new google.maps.Point(anchorx, anchory)}, title: title,  click: function(e) {    markerclickarray(id, markerid);  }, infoWindow: {content: '<p>' + infowindow + '</p>' + infodel}, infoWindowOpen: permanent});");
		 	writer.write("}");	
	 		writer.write("}");
	 		writer.write("}");
	 		
	 		writer.write("function gmcheckgeofence(id,lat,lng,pid) {");
	 		writer.write("var pol = gmgetpolygon(id,pid);");
	 		writer.write("return gms[id].checkGeofence(lat,lng,pol);");
	 		writer.write("}");
	 		
	 		writer.write("function gmcheckmarkergeofence(id,markerid) {");
	 		writer.write("var pol = gmgetmarker(id,markerid);");
	 		writer.write("gms[id].checkMarkerGeofence(pol,function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_outsidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});},function(marker, fence) {b4j_raiseEvent('page_parseevent', {'eventname': id + '_insidegeofence','eventparams': 'markerid,fenceid', 'markerid': marker.get('id'), 'fenceid': fence.get('id')});});");
	 		writer.write("}");
	 		
	 		writer.write("function gmfitzoom(id) {");
	 		writer.write("gms[id].fitZoom();");
	 		writer.write("}");
	 		
	 		writer.write("function gmgetzoom(id) {");
	 		writer.write("return gms[id].map.getZoom();");
	 		writer.write("}");
	 		
	 		writer.write("function gmsetoverlay(id, n, s, e, w, url) {");
	 		writer.write("var imageBounds = {");
	 		writer.write("north: n,");
	 		writer.write("south: s,");
	 		writer.write("east: e,");
	 		writer.write("west: w");
	 		writer.write("};");
	 		writer.write("gmos[id] = new google.maps.GroundOverlay(url, imageBounds);");
	 		writer.write("gmos[id].setMap(gms[id].map);");
	 		writer.write("}");
	 		
	 		writer.write("function gmremoveoverlay(id) {");
	 		writer.write("gmos[id].setMap(null);");
	 		writer.write("}");
	 			 		
	 		writer.write("function gmsetzoom(id,zoom) {");
	 		writer.write("gms[id].zoom=zoom;");
	 		writer.write("gms[id].map.setZoom(zoom);");
	 		writer.write("}");	 		
	 		
	 		writer.write("function gmaddpolyline(id, path, col, op, wei, polylineid) {");
	 		writer.write("var arrpath = eval(path);");
	 		writer.write("gms[id].drawPolyline({id: polylineid, path: arrpath,  strokeColor: col, strokeOpacity: op, strokeWeight: wei});");
	 		writer.write("}");
	 		
	 		writer.write("function gmremovepolylines(id) {");
	 		writer.write("gms[id].removePolylines();");
	 		writer.write("}");
	 		
	 		writer.write("function gmgetpolyline(id, polylineid) {");
	 		writer.write("var ret;");
	 		writer.write("$.each(gms[id].polylines, function(index, poly) {");
	 			writer.write("if (poly.get('id')==polylineid) {");
	 				writer.write("ret = poly;");
	 				writer.write("return poly;");
	 			writer.write("}");
	 		writer.write("});");	 		
	 		writer.write("return ret;");
	 		writer.write("};");
	 		
	 		writer.write("function gmremovepolyline(id, polylineid) {");
	 		writer.write("$.each(gms[id].polylines, function(index, poly) {");
	 			writer.write("if (poly.get('id')==polylineid) {");
	 				writer.write("gms[id].removePolyline(gms[id].polylines[index]);");
	 				writer.write("return;");
	 			writer.write("}");
	 		writer.write("});");	 		
	 		writer.write("};");
	 		
	 		writer.write("function gmaddpolygon(id, path, col, op, wei, colf, opf, polygonid) {");
	 		writer.write("var arrpath = eval(path);");
	 		writer.write("gms[id].drawPolygon({id: polygonid, paths: arrpath,  strokeColor: col, strokeOpacity: op, strokeWeight: wei, fillColor: colf, fillOpacity: opf});");
	 		writer.write("}");
	 		
	 		writer.write("function gmremovepolygons(id) {");
	 		writer.write("gms[id].removePolygons();");
	 		writer.write("}");
	 		
	 		writer.write("function gmremovepolygon(id, polygonid) {");
	 		writer.write("$.each(gms[id].polygons, function(index, poly) {");
	 			writer.write("if (poly.get('id')==polygonid) {");
	 				writer.write("gms[id].removePolygon(gms[id].polygons[index]);");
	 				writer.write("return;");
	 				writer.write("}");
	 			writer.write("});");
	 		writer.write("}");
	 		
	 		writer.write("function gmgetpolygon(id, polygonid) {");
	 		writer.write("var ret;");
	 		writer.write("$.each(gms[id].polygons, function(index, poly) {");
	 			writer.write("if (poly.get('id')==polygonid) {");
	 				writer.write("ret = poly;");
	 				writer.write("return poly;");
	 				writer.write("}");
	 			writer.write("});");
	 			writer.write("return ret;");	
	 		writer.write("}");
	 	
	 		writer.write("function gmaddroute(id, o, d, tmode, col, op, wei) {");  
	 		writer.write("var org = eval(o);");
	 		writer.write("var des = eval(d);");
	 		writer.write("gms[id].drawRoute({origin: org, destination: des, travelMode: tmode,  strokeColor: col, strokeOpacity: op, strokeWeight: wei});");
	 		writer.write("}");
	 		
	 		writer.write("function gmremoveroutes(id) {");
	 		writer.write("gms[id].cleanRoute();");
	 		writer.write("}\n");   	 
   	 	}
   	 	
   	 	if (NeedsCalendar) {
   	 		writer.write("var currentClickedEvent;");
   	 		writer.write("function initcalendars() {");
   	 		for (Entry<String,ABMCalendar> ecal : Calendars.entrySet()) {
   	 			ABMCalendar cal = ecal.getValue();

   	 			writer.write("$('#" + cal.ParentString + cal.ID.toLowerCase() + "').fullCalendar({");
   	 			writer.write("header: {");
   	 			String Left="";
   	 			if (cal.HasPreviousButton) {
   	 				Left+="prev,";
   	 			}
   	 			if (cal.HasNextButton) {
   	 				Left+="next,";
   	 			}
   	 			if (cal.HasTodayButton) {
   	 				Left+="today,";
   	 			}
   	 			
   	 			if (!Left.equals("")) {
   	 				Left= Left.substring(0,Left.length()-1);
   	 				writer.write("left: '" + Left + "',");
   	 			}
   	 			writer.write("center: 'title',");
   	 			
   	 			String Right="";
   	 			if (cal.HasMonthButton) {
   	 				Right+="month,";
   	 			}
   	 			if (cal.HasWeekButton) {
   	 				Right+="agendaWeek,";
   	 			}
   	 			if (cal.HasDayButton) {
   	 				Right+="agendaDay,";
   	 			}
   	 			if (!Right.equals("")) {
   	 				Right= Right.substring(0,Right.length()-1);
   	 				writer.write("right: '" + Right + "',");
   	 			}       	 			
   	 			writer.write("},");
   	 			writer.write("defaultDate: '" + cal.DefaultDate + "',");
   	 			writer.write("editable: true,");
   	 			writer.write("firstDay: " + cal.FirstDayOfWeek + ",");
   	 			writer.write("defaultView: '" + cal.DefaultView + "',");

   	 			if (!cal.HasScrollBars ) {
   	 				writer.write("contentHeight: 'auto',");
   	 			}
   	 			writer.write("eventLimit: false,"); 
   	 			writer.write("displayEventTime: " + cal.DisplayEventTime + ",");
   	 			writer.write("displayEventEnd: " + cal.DisplayEventEnd + ",");
   	 			writer.write("slotDuration: '" + cal.SlotDuration + "',");
   	 			writer.write("lang: '" + cal.Language + "',");
   	 			writer.write("weekends: " + cal.ShowWeekends + ",");
   	 			writer.write("minTime: '" + cal.MinTime + "',");
   	 			writer.write("maxTime: '" + cal.MaxTime + "',");
   	 			writer.write("dayClick: function(date, jsEvent, view) {");
   	 			writer.write("  b4j_raiseEvent('page_parseevent', {'eventname': '" + cal.ParentString + cal.ID.toLowerCase() + "' + '_dayclicked','eventparams':'date','date':date.format()});");
   	 			writer.write("},");
   	 			writer.write("eventClick: function(event, jsEvent, view) {");
   	 			writer.write("currentClickedEvent=event;");
   	 			writer.write("  b4j_raiseEvent('page_parseevent', {'eventname': '" + cal.ParentString + cal.ID.toLowerCase() + "' + '_eventclicked','eventparams':'eventid','eventid':event.eventid});");
   	 			writer.write("},");
   	 			writer.write("events: function(start, end, timezone, callback) {");
   	 			writer.write("  b4j_raiseEvent('page_parseevent', {'eventname': '" + cal.ParentString + cal.ID.toLowerCase() + "' + '_fetchdata','eventparams':'datestart,dateend','datestart':start.format(),'dateend':end.format()});");
   	 			writer.write("},");	  
   	 			
   	 			writer.write("eventResize: function(event, delta, revertFunc) {");
   	 			writer.write("  b4j_raiseEvent('page_parseevent', {'eventname': '" + cal.ParentString + cal.ID.toLowerCase() + "' + '_eventendchanged','eventparams':'eventid,newend','eventid':event.eventid,'newend':event.end.format()});");
   	 			writer.write("},");
   	 			
   	 			writer.write("eventDrop: function(event, delta, revertFunc) {");
   	 			writer.write("  if (event.allDay) {");
   	 			writer.write("  } else {");
   	 			writer.write("     if (event.end===null) {");
   	 			writer.write("     } else {");
   	 			writer.write("        b4j_raiseEvent('page_parseevent', {'eventname': '" + cal.ParentString + cal.ID.toLowerCase() + "' + '_eventstartchanged','eventparams':'eventid,newstart,newend,allday','eventid':event.eventid,'newstart':event.start.format(),'newend':event.end.format(), 'allday': event.allDay});");
   	 			writer.write("     }");
   	 			writer.write("  }");
   	 			writer.write("},");
   	 			
   	 			writer.write("});\n");
   	 		}
   	 		writer.write("}\n");
   	 		
   	 		writer.write("function calsetevents(id,events) {");
   	 		writer.write("var data = jQuery.parseJSON(events);");
   	 		writer.write("var arrayLength = data.length;");
   	 		writer.write("$('#' + id).fullCalendar('removeEvents');");
   	 		writer.write("for (var i = 0; i < arrayLength; i++) {");	
   	 		writer.write("var data2 = data[i];");
   	 		writer.write("$('#' + id).fullCalendar( 'renderEvent',data2, true)");
   	 		writer.write("};");
   	 		writer.write("};\n");
   	 		
   	 		writer.write("function calgotodate(id,newdate) {");
   	 		writer.write("$('#' + id).fullCalendar('gotoDate', '\"' + newdate + '\"');"); 
   	 		writer.write("};\n");
   	 		
   	 		writer.write("function caladdevent(id,event) {");
   	 		writer.write("var data = jQuery.parseJSON(event);");
   	 		writer.write("$('#' + id).fullCalendar( 'renderEvent',data, true)");
   	 		writer.write("};\n");
   	 		
   	 		writer.write("function calremoveevent(id,eventid) {");
   	 		writer.write("$('#' + id).fullCalendar('removeEvents', function(event) {;");
   	 		writer.write("return event.className == eventid;");
   	 		writer.write("});\n");
   	 		writer.write("};\n");    
   	 		
   	 		writer.write("function calupdateevent(id,event) {");
   	 		writer.write("var data = jQuery.parseJSON(event);");
   	 		writer.write("$.extend(currentClickedEvent, data);");
   	 		writer.write("if (!data.hasOwnProperty('end') && currentClickedEvent.hasOwnProperty('end')) {delete currentClickedEvent['end']; }");
   	 		writer.write("if (!data.hasOwnProperty('allDay') && currentClickedEvent.hasOwnProperty('allDay')) {delete currentClickedEvent['allDay']; }");
   	 		writer.write("if (!data.hasOwnProperty('id') && currentClickedEvent.hasOwnProperty('id')) {delete currentClickedEvent['id']; }");
   	 		writer.write("$('#' + id).fullCalendar( 'updateEvent',currentClickedEvent)");
   	 		writer.write("};\n");
		
   	 		writer.write("function calgettitle(id) {");
   	 		writer.write("return $('#' + id).fullCalendar('getView').title;");
   	 		writer.write("};\n");
		
   	 		writer.write("function calrefresh() {");
   	 		writer.write("$(window).trigger('resize');");
   	 		writer.write("};\n");
   	 		
   	 		writer.write("function calchangeview(id, viewName) {");
   	 		writer.write("$('#' + id).fullCalendar( 'changeView', viewName )");
   	 		writer.write("};\n");
   	 		
   	 		writer.write("function calchangedefaultview(id, viewName) {");
   	 		writer.write("$('#' + id).fullCalendar( 'option', 'defaultView', viewName )");
   	 		writer.write("};\n");
   	 		
   	 		writer.write("function calchangeeventseditable(id, editable) {");
   	 		writer.write("$('#' + id).fullCalendar('clientEvents', function(ev) {ev.editable=editable});");
   	 		writer.write("$('#' + id).fullCalendar( 'render' );");
   	 		writer.write("};\n");
   	 		
   	 		writer.write("function calrefetchdata(id) {");
   	 		writer.write("$('#' + id).fullCalendar( 'refetchEvents' )");
   	 		writer.write("};\n");
   	 	}
   	 	
   	 	if (NeedsResponsiveContainer) {
   	 		writer.write("\n!function(e,t){\"function\"==typeof define&&define.amd?define(t):\"object\"==typeof exports?module.exports=t():e.ResizeSensor=t()}(\"undefined\"!=typeof window?window:this,function(){function e(e,t){var n=Object.prototype.toString.call(e),i=\"[object Array]\"===n||\"[object NodeList]\"===n||\"[object HTMLCollection]\"===n||\"[object Object]\"===n||\"undefined\"!=typeof jQuery&&e instanceof jQuery||\"undefined\"!=typeof Elements&&e instanceof Elements,o=0,r=e.length;if(i)for(;o<r;o++)t(e[o]);else t(e)}function t(e){if(!e.getBoundingClientRect)return{width:e.offsetWidth,height:e.offsetHeight};var t=e.getBoundingClientRect();return{width:Math.round(t.width),height:Math.round(t.height)}}if(\"undefined\"==typeof window)return null;var n=window.requestAnimationFrame||window.mozRequestAnimationFrame||window.webkitRequestAnimationFrame||function(e){return window.setTimeout(e,20)},i=function(o,r){function s(){var e=[];this.add=function(t){e.push(t)};var t,n;this.call=function(){for(t=0,n=e.length;t<n;t++)e[t].call()},this.remove=function(i){var o=[];for(t=0,n=e.length;t<n;t++)e[t]!==i&&o.push(e[t]);e=o},this.length=function(){return e.length}}function d(e,i){if(e)if(e.resizedAttached)e.resizedAttached.add(i);else{e.resizedAttached=new s,e.resizedAttached.add(i),e.resizeSensor=document.createElement(\"div\"),e.resizeSensor.dir=\"ltr\",e.resizeSensor.className=\"resize-sensor\";var o=\"position: absolute; left: -10px; top: -10px; right: 0; bottom: 0; overflow: hidden; z-index: -1; visibility: hidden;\",r=\"position: absolute; left: 0; top: 0; transition: 0s;\";e.resizeSensor.style.cssText=o,e.resizeSensor.innerHTML='<div class=\"resize-sensor-expand\" style=\"'+o+'\"><div style=\"'+r+'\"></div></div><div class=\"resize-sensor-shrink\" style=\"'+o+'\"><div style=\"'+r+' width: 200%; height: 200%\"></div></div>',e.appendChild(e.resizeSensor),e.resizeSensor.offsetParent!==e&&(e.style.position=\"relative\");var d,a,l=e.resizeSensor.childNodes[0],c=l.childNodes[0],u=e.resizeSensor.childNodes[1],h=t(e),f=h.width,m=h.height,p=function(){c.style.width=\"100000px\",c.style.height=\"100000px\",l.scrollLeft=1e5,l.scrollTop=1e5,u.scrollLeft=1e5,u.scrollTop=1e5};p();var v=function(){a=0,d&&(f=void 0,m=void 0,e.resizedAttached&&e.resizedAttached.call())},g=function(){var i=t(e),o=i.width,r=i.height;(d=o!=f||r!=m)&&!a&&(a=n(v)),p()},w=function(e,t,n){e.attachEvent?e.attachEvent(\"on\"+t,n):e.addEventListener(t,n)};w(l,\"scroll\",g),w(u,\"scroll\",g)}}e(o,function(e){d(e,r)}),this.detach=function(e){i.detach(o,e)}};return i.detach=function(t,n){e(t,function(e){e&&(e.resizedAttached&&\"function\"==typeof n&&(e.resizedAttached.remove(n),e.resizedAttached.length())||e.resizeSensor&&(e.contains(e.resizeSensor)&&e.removeChild(e.resizeSensor),delete e.resizeSensor,delete e.resizedAttached))})},i}),function(e,t){\"function\"==typeof define&&define.amd?define([\"./ResizeSensor.js\"],t):\"object\"==typeof exports?module.exports=t(require(\"./ResizeSensor.js\")):(e.ElementQueries=t(e.ResizeSensor),e.ElementQueries.listen())}(\"undefined\"!=typeof window?window:this,function(e){var t=function(){function n(e){e||(e=document.documentElement);var t=window.getComputedStyle(e,null).fontSize;return parseFloat(t)||16}function i(e){if(!e.getBoundingClientRect)return{width:e.offsetWidth,height:e.offsetHeight};var t=e.getBoundingClientRect();return{width:Math.round(t.width),height:Math.round(t.height)}}function o(e,t){var i=t.split(/\\d/),o=i[i.length-1];switch(t=parseFloat(t),o){case\"px\":return t;case\"em\":return t*n(e);case\"rem\":return t*n();case\"vw\":return t*document.documentElement.clientWidth/100;case\"vh\":return t*document.documentElement.clientHeight/100;case\"vmin\":case\"vmax\":var r=document.documentElement.clientWidth/100,s=document.documentElement.clientHeight/100;return t*(0,Math[\"vmin\"===o?\"min\":\"max\"])(r,s);default:return t}}function r(e){this.element=e,this.options={};var t,n,r,s,d,a,l,c;this.addOption=function(e){var t=[e.mode,e.property,e.value].join(\",\");this.options[t]=e};var u=[\"min-width\",\"min-height\",\"max-width\",\"max-height\"];this.call=function(){r=i(this.element),a={};for(t in this.options)this.options.hasOwnProperty(t)&&(n=this.options[t],s=o(this.element,n.value),d=\"width\"==n.property?r.width:r.height,c=n.mode+\"-\"+n.property,l=\"\",\"min\"==n.mode&&d>=s&&(l+=n.value),\"max\"==n.mode&&d<=s&&(l+=n.value),a[c]||(a[c]=\"\"),l&&-1===(\" \"+a[c]+\" \").indexOf(\" \"+l+\" \")&&(a[c]+=\" \"+l));for(var e in u)u.hasOwnProperty(e)&&(a[u[e]]?this.element.setAttribute(u[e],a[u[e]].substr(1)):this.element.removeAttribute(u[e]))}}function s(t,n){t.elementQueriesSetupInformation?t.elementQueriesSetupInformation.addOption(n):(t.elementQueriesSetupInformation=new r(t),t.elementQueriesSetupInformation.addOption(n),t.elementQueriesSensor=new e(t,function(){t.elementQueriesSetupInformation.call()})),t.elementQueriesSetupInformation.call(),m&&p.indexOf(t)<0&&p.push(t)}function d(e,t,n,i){void 0===v[t]&&(v[t]={}),void 0===v[t][n]&&(v[t][n]={}),void 0===v[t][n][i]?v[t][n][i]=e:v[t][n][i]+=\",\"+e}function a(e){var t;if(document.querySelectorAll&&(t=e?e.querySelectorAll.bind(e):document.querySelectorAll.bind(document)),t||\"undefined\"==typeof $$||(t=$$),t||\"undefined\"==typeof jQuery||(t=jQuery),!t)throw\"No document.querySelectorAll, jQuery or Mootools's $$ found.\";return t}function l(e){var t=a(e);for(var n in v)if(v.hasOwnProperty(n))for(var i in v[n])if(v[n].hasOwnProperty(i))for(var o in v[n][i])if(v[n][i].hasOwnProperty(o))for(var r=t(v[n][i][o],e),d=0,l=r.length;d<l;d++)$(r[d]).hasClass(\"hide\")||s(r[d],{mode:n,property:i,value:o})}function c(t){function n(){var e,n=!1;for(e in i)i.hasOwnProperty(e)&&o[e].minWidth&&t.offsetWidth>o[e].minWidth&&(n=e);if(n||(n=s),d!=n)if(a[n])i[d].style.display=\"none\",i[n].style.display=\"block\",d=n;else{var l=new Image;l.onload=function(){i[n].src=r[n],i[d].style.display=\"none\",i[n].style.display=\"block\",a[n]=!0,d=n},l.src=r[n]}else i[n].src=r[n]}var i=[],o=[],r=[],s=0,d=-1,a=[];for(var l in t.children)if(t.children.hasOwnProperty(l)&&t.children[l].tagName&&\"img\"===t.children[l].tagName.toLowerCase()){i.push(t.children[l]);var c=t.children[l].getAttribute(\"min-width\")||t.children[l].getAttribute(\"data-min-width\"),u=t.children[l].getAttribute(\"data-src\")||t.children[l].getAttribute(\"url\");r.push(u);var h={minWidth:c};o.push(h),c?t.children[l].style.display=\"none\":(s=i.length-1,t.children[l].style.display=\"block\")}d=s,t.resizeSensor=new e(t,n),n(),m&&p.push(t)}function u(){for(var e=a()(\"[data-responsive-image],[responsive-image]\"),t=0,n=e.length;t<n;t++)c(e[t])}function h(e){var t,n,i,o;for(e=e.replace(/'/g,'\"');null!==(t=g.exec(e));)for(n=t[1]+t[3],i=t[2];null!==(o=w.exec(i));)d(n,o[1],o[2],o[3])}function f(e){var t=\"\";if(e)if(\"string\"==typeof e)-1===(e=e.toLowerCase()).indexOf(\"min-width\")&&-1===e.indexOf(\"max-width\")||h(e);else for(var n=0,i=e.length;n<i;n++)1===e[n].type?-1!==(t=e[n].selectorText||e[n].cssText).indexOf(\"min-height\")||-1!==t.indexOf(\"max-height\")?h(t):-1===t.indexOf(\"min-width\")&&-1===t.indexOf(\"max-width\")||h(t):4===e[n].type?f(e[n].cssRules||e[n].rules):3===e[n].type&&f(e[n].styleSheet.cssRules)}var m=!1,p=[],v={},g=/,?[\\s\\t]*([^,\\n]*?)((?:\\[[\\s\\t]*?(?:min|max)-(?:width|height)[\\s\\t]*?[~$\\^]?=[\\s\\t]*?\"[^\"]*?\"[\\s\\t]*?])+)([^,\\n\\s\\{]*)/gim,w=/\\[[\\s\\t]*?(min|max)-(width|height)[\\s\\t]*?[~$\\^]?=[\\s\\t]*?\"([^\"]*?)\"[\\s\\t]*?]/gim,y=!1;this.init=function(e){m=void 0!==e&&e;for(var t=0,n=document.styleSheets.length;t<n;t++)try{f(document.styleSheets[t].cssRules||document.styleSheets[t].rules||document.styleSheets[t].cssText)}catch(e){if(\"SecurityError\"!==e.name&&\"InvalidAccessError\"!==e.name)throw e}if(!y){var i=document.createElement(\"style\");i.type=\"text/css\",i.innerHTML=\"[responsive-image] > img, [data-responsive-image] {overflow: hidden; padding: 0; } [responsive-image] > img, [data-responsive-image] > img { width: 100%;}\",document.getElementsByTagName(\"head\")[0].appendChild(i),y=!0}l(),u()},this.findElementQueriesElements=function(e){l(e)},this.update=function(e){this.init(e)},this.detach=function(){if(!m)throw\"withTracking is not enabled. We can not detach elements since we don not store it.Use ElementQueries.withTracking = true; before domready or call ElementQueryes.update(true).\";for(var e;e=p.pop();)t.detach(e);p=[]}};t.update=function(e){t.instance.update(e)},t.detach=function(e){e.elementQueriesSetupInformation?(e.elementQueriesSensor.detach(),delete e.elementQueriesSetupInformation,delete e.elementQueriesSensor):e.resizeSensor&&(e.resizeSensor.detach(),delete e.resizeSensor)},t.withTracking=!1,t.init=function(){t.instance||(t.instance=new t),t.instance.init(t.withTracking)};var n=function(e){if(document.addEventListener)document.addEventListener(\"DOMContentLoaded\",e,!1);else if(/KHTML|WebKit|iCab/i.test(navigator.userAgent))var t=setInterval(function(){/loaded|complete/i.test(document.readyState)&&(e(),clearInterval(t))},10);else window.onload=e};return t.findElementQueriesElements=function(e){t.instance.findElementQueriesElements(e)},t.listen=function(){n(t.init)},t});\n");
		}
   	 	
   	 	if (ABMaterial.res!=null) {
   	 		ExtractResultModule extMod = ABMaterial.res.Modules.getOrDefault(BASFileName, null);
   	 		if (extMod!=null) {
   	 			writer.write(extMod.FinalJS);
   	 		}
   	 	}
   	 	
   	 WriteJSPageExtra(writer, NeedsAutorization, ReloadStringInner, IsApp);
   	 
   	 WriteB4JS(writer);
       
    }
    
    protected void WriteB4JS(BufferedWriter writer) throws IOException {
    	for (Map.Entry<String, ThemeToast> entry : CompleteTheme.Toasts.entrySet()) {
			writer.write(entry.getValue().AsB4JSVar());
		}
		ThemeToast todef = new ThemeToast();
		todef.Colorize(CompleteTheme.MainColor);
		writer.write(todef.AsB4JSVar());
		
		for (Map.Entry<String, ThemeMsgBox> entry : CompleteTheme.Msgboxes.entrySet()) {
			writer.write(entry.getValue().AsB4JSVar());
		}
		ThemeMsgBox tomsg = new ThemeMsgBox();
		tomsg.Colorize(CompleteTheme.MainColor);
		writer.write(tomsg.AsB4JSVar());
    }
	
	protected void WriteCSSPage(BufferedWriter writer, String relPath) throws IOException {
			if (ABMaterial.CDN) {
				URelPath=ABMaterial.mCDNUrl;
			} else {
				URelPath=relPath;
			}
			
        	int SML600=ABMaterial.ThresholdPxConsiderdSmall;
        	int SML992=ABMaterial.ThresholdPxConsiderdMedium;
        	int SML1200=ABMaterial.ThresholdPxConsiderdLarge;
        	int SML993=SML992+1;
        	int SML601=SML600+1;
        	int SML1201=SML1200+1;
        	
        	int LogoHeight=56;
        	
        	writer.write("@font-face{font-family:fontello-onetwo;src:url(../../font/fontello-onetwo/fontello-onetwo.eot?69915108);src:url(../../font/fontello-onetwo/fontello-onetwo.eot?69915108#iefix) format('embedded-opentype'),url(../../font/fontello-onetwo/fontello-onetwo.woff2?69915108) format('woff2'),url(../../font/fontello-onetwo/fontello-onetwo.woff?69915108) format('woff'),url(../../font/fontello-onetwo/fontello-onetwo.ttf?69915108) format('truetype'),url(../../font/fontello-onetwo/fontello-onetwo.svg?69915108#fontello) format('svg');font-weight:400;font-style:normal}[class*=\" fa-ot-\"]:before,[class^=fa-ot-]:before{font-family:fontello-onetwo;font-style:normal;font-weight:400;speak:never;font-variant:normal;text-transform:none;-webkit-font-smoothing:antialiased;-moz-osx-font-smoothing:grayscale}.fa-ot-scanner:before{content:'\\e801'}.fa-ot-in:before { content: '\\e807'; }.fa-ot-inventory:before { content: '\\e809'; }.fa-ot-out:before { content: '\\e80a'; }");
        	if (!(navigationBar==null)) {
        		writer.write("nav.nav-extended {height: auto;} nav.nav-extended .nav-wrapper {min-height: 56px; height: auto;} nav.nav-extended .nav-content { position: relative; line-height: normal;}");
        		if (!navigationBar.IsTextSelectable) {
        			writer.write("#nav-mobile, nav{-webkit-touch-callout:none;-webkit-user-select:none;-khtml-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;-webkit-tap-highlight-color:rgba(0,0,0,0);}");
        		}
        		LogoHeight=navigationBar.SideBarLogoHeight+6;
        		if (navigationBar.mSideBarTopComponent==null) {
            		LogoHeight=0;
            	}
        		if (navigationBar.WithExtraSearch) {
        			writer.write(".search-widget label {white-space: nowrap; overflow: hidden}");
        			writer.write(".search-widget.focused input {outline: 0}");
        			writer.write(".search-widget, .search-widget .search-close-button, .search-widget label:after {transition: .2s cubic-bezier(.4, 0, .2, 1)}");
        			writer.write(".search-widget { font-size: 16px; height: 29px; margin: 0; padding: 0; position: fixed; right: 24px; top: 15px; width: 0; z-index: 2}");
        			writer.write(".search-widget .search-button, .search-widget .search-close-button, .search-widget label, .search-widget label:after {position: absolute; box-sizing: border-box}");
        			writer.write(".search-widget input, .search-widget input[type=text] { box-sizing: border-box; border: none; border-radius: 0; display: block; font-size: 16px; font-weight: 400; margin: 0; padding: 5px 0; width: 100%; text-align: left; background: 0; box-shadow: none; height: initial !important;}");
        			writer.write(".search-widget input:focus, .search-widget input:hover, .search-widget input[type=text]:focus, .search-widget input[type=text]:hover { box-shadow: none; height: initial !important;}");
        			writer.write(".search-widget label { bottom: 0; font-size: 16px; font-weight: 300; left: 0; right: 0; pointer-events: none; display: block; top: 7px; width: 100%}");
        			writer.write(".search-widget label:after { bottom: 0; content: ''; height: 2px; width: 10px; left: 45%; visibility: hidden}");
        			writer.write(".search-widget .search-button { left: -48px; top: -16px; margin-left: 7px;}");
        			writer.write(".search-widget .search-close-button { right: -16px; top: -16px;  display: none; opacity: 0}");
        			writer.write(".search-widget.text-entered label {visibility: hidden}");
        			writer.write(".search-widget.focused label:after {left: 0; width: 100%; visibility: visible}");
        			writer.write(".search-open .search-widget .search-close-button {display: block; opacity: 1}");
        			writer.write(".search-li {width: 0px}");
        			writer.write(".search-li:hover, search-li.active {background-color: rgba(0, 0, 0, 0);}");
        			
        			ThemeNavigationBar nt = navigationBar.Theme;
        			
        			writer.write(".search-widget { color: " + ABMaterial.GetColorStrMap(nt.TopBarForeColor, nt.TopBarForeColorIntensity) + "}");
        			writer.write("@media screen and (max-width: " + SML600 + "px) { .search-widget { right: 16px; top: 12px}}");
        			writer.write("@media only print { .search-widget { right: 16px; top: 12px}}");
        			writer.write(".search-widget input, .search-widget input[type=text] { border-bottom: 1px solid " + ABMaterial.GetColorStrMapRGBA(nt.TopBarForeColor, nt.TopBarForeColorIntensity,0.87) + "; color: " + ABMaterial.GetColorStrMap(nt.TopBarForeColor, nt.TopBarForeColorIntensity) + "}");
        			writer.write(".search-widget input:focus, .search-widget input:hover, .search-widget input[type=text]:focus, .search-widget input[type=text]:hover { border-bottom: 1px solid " + ABMaterial.GetColorStrMapRGBA(nt.TopBarForeColor, nt.TopBarForeColorIntensity,0.87) + "}");
        			writer.write(".search-widget label { color: " + ABMaterial.GetColorStrMapRGBA(nt.TopBarForeColor, nt.TopBarForeColorIntensity,0.54) + "}");
        			writer.write(".search-widget label:after { background-color: " + ABMaterial.GetColorStrMapRGBA(nt.TopBarForeColor, nt.TopBarForeColorIntensity,0.87) + "}");
        			writer.write(".search-widget .search-button { color: " + ABMaterial.GetColorStrMap(nt.TopBarForeColor, nt.TopBarForeColorIntensity) + "; background-color: transparent; border: 0px solid " + ABMaterial.GetColorStrMap(nt.TopBarForeColor, nt.TopBarForeColorIntensity) + ";}");
        			writer.write(".search-widget .search-close-button { color: " + ABMaterial.GetColorStrMap(nt.TopBarForeColor, nt.TopBarForeColorIntensity) + "; background-color: transparent; border: 0px solid " + ABMaterial.GetColorStrMap(nt.TopBarForeColor, nt.TopBarForeColorIntensity) + ";}");
        			
        			writer.write("@media screen and (max-width: " + SML600 + "px) {.search-widget .search-button {top: 0px;} .search-widget .search-close-button {top: 0px; }}");
        			writer.write("@media only print {.search-widget .search-button {top: -4px;} .search-widget .search-close-button {top: -4px; }}");
        			writer.write(".search-open .search-widget { width: " + navigationBar.ExtraSearchL +"px}");
        			writer.write("@media screen and (max-width: " + SML992 + "px) { .search-open .search-widget { width: " + navigationBar.ExtraSearchM +"px }}");
        			writer.write("@media only print { .search-open .search-widget { width: " + navigationBar.ExtraSearchM +"px }}");
        			writer.write("@media screen and (max-width: " + SML600 + "px) { .search-open .search-widget { width: " + navigationBar.ExtraSearchS +"px }}");
        			writer.write("@media only print { .search-open .search-widget { width: " + navigationBar.ExtraSearchS +"px }}");
        			writer.write(".search-open .search-li {width: " + navigationBar.ExtraSearchL +"px}");
        			writer.write("@media screen and (max-width: " + SML992 + "px) {.search-open .search-li {width: " + navigationBar.ExtraSearchM +"px}}");
        			writer.write("@media only print {.search-open .search-li {width: " + navigationBar.ExtraSearchM +"px}}");
        			writer.write("@media screen and (max-width: " + SML600 + "px) {.search-open .search-li {width: " + navigationBar.ExtraSearchS +"px}}");
        			writer.write("@media only print {.search-open .search-li {width: " + navigationBar.ExtraSearchS +"px}}");
        			writer.write("@media only screen and (max-width: " + SML600 + "px) {.brand-logo.search-open {display: none!important}}");
        		}
        	}
        	
        	
        	
        	writer.write(".prntNA {font-weight: 300;}");
        	
       		writer.write("@media only screen and (max-width : " + SML992 + "px) { .pagination {width: 100%; }.pagination li.prev, .pagination li.next {width: 10%; }.pagination li.pages {width: 80%;overflow: hidden;white-space: nowrap; } }");
        		
    		writer.write("@media only screen and (max-width : " + SML600 + "px) { .hide-on-small-only, .hide-on-small-and-down { display: none !important; } }");
    		writer.write("@media only screen and (max-width : " + SML992 + "px) { .hide-on-med-and-down { display: none !important; } }");
    		writer.write("@media only screen and (min-width : " + SML601 + "px) { .hide-on-med-and-up { display: none !important; } }");
    		writer.write("@media only screen and (max-width : " + SML1200 + "px) { .hide-on-large-and-down { display: none !important; } }");
    		writer.write("@media only screen and (min-width : " + SML993 + "px) { .hide-on-large-and-up { display: none !important; } }");        		
    		writer.write("@media only screen and (min-width: " + SML600 + "px) and (max-width: " + SML992 + "px) { .hide-on-med-only { display: none !important; } }");
    		writer.write("@media only screen and (min-width: " + SML992 + "px) and (max-width: " + SML1200 + "px) { .hide-on-large-only { display: none !important; } }");
    		writer.write("@media only screen and (min-width : " + SML1201 + "px) { .hide-on-extralarge-only { display: none !important; } }");
    		writer.write("@media only screen and (min-width : " + SML1201 + "px) { .show-on-extralarge { display: initial !important; } }");
    		writer.write("@media only screen and (min-width: " + SML992 + "px) and (max-width: " + SML1200 + "px) { .show-on-large { display: initial !important; } }");
    		writer.write("@media only screen and (min-width: " + SML600 + "px) and (max-width: " + SML992 + "px) { .show-on-medium { display: initial !important; } }");
    		writer.write("@media only screen and (max-width : " + SML600 + "px) { .show-on-small { display: initial !important; } }");
    		writer.write("@media only screen and (min-width : " + SML601 + "px) { .show-on-medium-and-up { display: initial !important; } }");
    		writer.write("@media only screen and (max-width : " + SML992 + "px) { .show-on-medium-and-down { display: initial !important; } }");
    		writer.write("@media only screen and (min-width : " + SML993 + "px) { .show-on-large-and-up { display: initial !important; } }");
    		writer.write("@media only screen and (max-width : " + SML1200 + "px) { .show-on-large-and-down { display: initial !important; } }");
    		        		
    		writer.write("@media only screen and (max-width : " + SML600 + "px) { .center-on-small-only { text-align: center; } }");
    		
    		writer.write("@media only screen and (max-width : " + SML992 + "px) { table.responsive-table{width:100%;border-collapse:collapse;border-spacing:0;display:block;position:relative}table.responsive-table td,table.responsive-table th{margin:0;vertical-align:top}table.responsive-table thead{display:block;float:left;border:0;border-right:1px solid #d0d0d0}table.responsive-table thead tr{display:block;padding:0 10px 0 0}table.responsive-table thead tr th::before{content:\"\\00a0\"}table.responsive-table tbody{display:block;width:auto;position:relative;overflow-x:auto;white-space:nowrap}table.responsive-table tbody tr{display:inline-block;vertical-align:top}table.responsive-table th{display:block;text-align:right}table.responsive-table td{display:block;min-height:1.25em;text-align:left}table.responsive-table tr{padding:0 10px}table.responsive-table.bordered th{border-bottom:0;border-left:0}table.responsive-table.bordered td{border-left:0;border-right:0;border-bottom:0}table.responsive-table.bordered tr{border:0}table.responsive-table.bordered tbody tr{border-right:1px solid #d0d0d0}}");
    		
    		writer.write("@media only screen and (min-width : " + SML601 + "px) { .container {width: 85%; } }");
    		writer.write("@media only screen and (min-width : " + SML993 + "px) { .container { width: 70%; } }");
    		writer.write("@media only screen and (min-width : " + SML601 + "px){.row .col.m1{width:8.33333%;margin-left:0}.row .col.m2{width:16.66667%;margin-left:0}.row .col.m3{width:25%;margin-left:0}.row .col.m4{width:33.33333%;margin-left:0}.row .col.m5{width:41.66667%;margin-left:0}.row .col.m6{width:50%;margin-left:0}.row .col.m7{width:58.33333%;margin-left:0}.row .col.m8{width:66.66667%;margin-left:0}.row .col.m9{width:75%;margin-left:0}.row .col.m10{width:83.33333%;margin-left:0}.row .col.m11{width:91.66667%;margin-left:0}.row .col.m12{width:100%;margin-left:0}.row .col.offset-m1{margin-left:8.33333%}.row .col.offset-m2{margin-left:16.66667%}.row .col.offset-m3{margin-left:25%}.row .col.offset-m4{margin-left:33.33333%}.row .col.offset-m5{margin-left:41.66667%}.row .col.offset-m6{margin-left:50%}.row .col.offset-m7{margin-left:58.33333%}.row .col.offset-m8{margin-left:66.66667%}.row .col.offset-m9{margin-left:75%}.row .col.offset-m10{margin-left:83.33333%}.row .col.offset-m11{margin-left:91.66667%}.row .col.offset-m12{margin-left:100%}}");
    		writer.write("@media only screen and (min-width : " + SML993 + "px){.row .col.l1{width:8.33333%;margin-left:0}.row .col.l2{width:16.66667%;margin-left:0}.row .col.l3{width:25%;margin-left:0}.row .col.l4{width:33.33333%;margin-left:0}.row .col.l5{width:41.66667%;margin-left:0}.row .col.l6{width:50%;margin-left:0}.row .col.l7{width:58.33333%;margin-left:0}.row .col.l8{width:66.66667%;margin-left:0}.row .col.l9{width:75%;margin-left:0}.row .col.l10{width:83.33333%;margin-left:0}.row .col.l11{width:91.66667%;margin-left:0}.row .col.l12{width:100%;margin-left:0}.row .col.offset-l1{margin-left:8.33333%}.row .col.offset-l2{margin-left:16.66667%}.row .col.offset-l3{margin-left:25%}.row .col.offset-l4{margin-left:33.33333%}.row .col.offset-l5{margin-left:41.66667%}.row .col.offset-l6{margin-left:50%}.row .col.offset-l7{margin-left:58.33333%}.row .col.offset-l8{margin-left:66.66667%}.row .col.offset-l9{margin-left:75%}.row .col.offset-l10{margin-left:83.33333%}.row .col.offset-l11{margin-left:91.66667%}.row .col.offset-l12{margin-left:100%}}");
    		writer.write("@media only screen and (min-width : " + SML993 + "px) { nav a.button-collapse { display: none; } }");
    		
    		writer.write("@media only screen and (min-width: " + SML601 + "px) .containerW {margin-left: 0rem;}");
    		writer.write(".containerW {margin: 0 auto;margin-left: 1rem;margin-right: 1rem;}");
    		
    		
    		
    		// rtl
    		writer.write(".row .col.abmrtl.offset-s1{margin-right:8.33333%}.row .col.abmrtl.offset-s2{margin-right:16.66667%}.row .col.abmrtl.offset-s3{margin-right:25%}.row .col.abmrtl.offset-s4{margin-right:33.33333%}.row .col.abmrtl.offset-s5{margin-right:41.66667%}.row .col.abmrtl.offset-s6{margin-right:50%}.row .col.abmrtl.offset-s7{margin-right:58.33333%}.row .col.abmrtl.offset-s8{margin-right:66.66667%}.row .col.abmrtl.offset-s9{margin-right:75%}.row .col.abmrtl.offset-s10{margin-right:83.33333%}.row .col.abmrtl.offset-s11{margin-right:91.66667%}.row .col.abmrtl.offset-s12{margin-right:100%}");
    		writer.write("@media only screen and (min-width : " + SML601 + "px){.row .col.abmrtl.offset-m1{margin-right:8.33333%}.row .col.abmrtl.offset-m2{margin-right:16.66667%}.row .col.abmrtl.offset-m3{margin-right:25%}.row .col.abmrtl.offset-m4{margin-right:33.33333%}.row .col.abmrtl.offset-m5{margin-right:41.66667%}.row .col.abmrtl.offset-m6{margin-right:50%}.row .col.abmrtl.offset-m7{margin-right:58.33333%}.row .col.abmrtl.offset-m8{margin-right:66.66667%}.row .col.abmrtl.offset-m9{margin-right:75%}.row .col.abmrtl.offset-m10{margin-right:83.33333%}.row .col.abmrtl.offset-m11{margin-right:91.66667%}.row .col.abmrtl.offset-m12{margin-right:100%}}");
    		writer.write("@media only screen and (min-width : " + SML993 + "px){.row .col.abmrtl.offset-l1{margin-right:8.33333%}.row .col.abmrtl.offset-l2{margin-right:16.66667%}.row .col.abmrtl.offset-l3{margin-right:25%}.row .col.abmrtl.offset-l4{margin-right:33.33333%}.row .col.abmrtl.offset-l5{margin-right:41.66667%}.row .col.abmrtl.offset-l6{margin-right:50%}.row .col.abmrtl.offset-l7{margin-right:58.33333%}.row .col.abmrtl.offset-l8{margin-right:66.66667%}.row .col.abmrtl.offset-l9{margin-right:75%}.row .col.abmrtl.offset-l10{margin-right:83.33333%}.row .col.abmrtl.offset-l11{margin-right:91.66667%}.row .col.abmrtl.offset-l12{margin-right:100%}}");
    		
    		writer.write("@media only screen and (max-width : " + SML992 + "px){nav .brand-logo{left:50%;-webkit-transform:translateX(-50%);-moz-transform:translateX(-50%);-ms-transform:translateX(-50%);-o-transform:translateX(-50%);transform:translateX(-50%)}nav .brand-logo.left,nav .brand-logo.right{padding:0;-webkit-transform:none;-moz-transform:none;-ms-transform:none;-o-transform:none;transform:none}nav .brand-logo.left{left:.5rem}nav .brand-logo.right{right:.5rem;left:auto}}");
    		if (navigationBar!=null) {
    			writer.write("@media only screen and (min-width : " + SML601 + "px) { nav.nav-extended .nav-wrapper {min-height: " + navigationBar.TopBarHeightPx + "px;} nav, nav .nav-wrapper i, nav a.button-collapse, nav a.button-collapse i { height: " + navigationBar.TopBarHeightPx + "px; line-height: " + navigationBar.TopBarHeightPx + "px; } .navbar-fixed { height: " + navigationBar.TopBarHeightPx + "px; } }");
    		} else {
    			writer.write("@media only screen and (min-width : " + SML601 + "px) { nav.nav-extended .nav-wrapper {min-height: 64px;} nav, nav .nav-wrapper i, nav a.button-collapse, nav a.button-collapse i { height: 64px; line-height: 64px; } .navbar-fixed { height: 64px; } }");
    		}
    		
    		writer.write("@media only screen and (min-width: " + SML992 + "px) { html { font-size: 14.5px; } }");
    		writer.write("@media only screen and (min-width: " + SML1200 + "px) { html { font-size: 15px; } }");
    		writer.write("@media only screen and (max-width : " + SML600 + "px) { #toast-container { min-width: 100%; bottom: 0%; } }");
    		writer.write("@media only screen and (min-width : " + SML601 + "px) and (max-width : " + SML992 + "px) { #toast-container { min-width: 30%; left: 5%; bottom: 7%; } }");
    		writer.write("@media only screen and (min-width : " + SML993 + "px) { #toast-container { min-width: 8%; top: 10%; right: 7%; } }");
    		writer.write("@media only screen and (max-width : " + SML600 + "px) { .toast { width: 100%; border-radius: 0; } }");
    		writer.write("@media only screen and (min-width : " + SML601 + "px) and (max-width : " + SML992 + "px) { .toast { float: left; } }");
    		writer.write("@media only screen and (min-width : " + SML993 + "px) { .toast { float: right; } }");
    		writer.write("@media only screen and (max-width : " + SML992 + "px) { .modal { width: 80%; } }");
    		writer.write("@media only screen and (max-width : " + SML992 + "px) { .input-field .prefix ~ input { width: 86%; width: calc(100% - 3rem); } }");
    		writer.write("@media only screen and (max-width : " + SML600 + "px) { .input-field .prefix ~ input { width: 80%; width: calc(100% - 3rem); } }");
    		writer.write("@media only screen and (max-width : " + SML992 + "px) { .side-nav.fixed { left: -105%; } .side-nav.fixed.right-aligned { right: -105%; left: auto; } }\n");
    		
    		writer.write("@media only screen and (max-width: " + SML600 + "px) {");
        	writer.write("#print-body {outline: 1px solid transparent;}");
        	writer.write("}");
        	writer.write("@media only screen and (min-width: " + SML601 + "px) and (max-width: " + SML992 + "px) {");
        	writer.write("#print-body {outline: 2px solid transparent;}");
        	writer.write("}");
        	writer.write("@media only screen and (min-width: " + SML993 + "px) and (max-width: " + SML1200 + "px) {");
    		writer.write("#print-body {outline: 3px solid transparent;}");
    		writer.write("}");
    		writer.write("@media only screen and (min-width: " + SML1201 + "px) {");
    		writer.write("#print-body {outline: 3px solid transparent;}"); // zou 4px kunnen zijn?
    		writer.write("}");

    		for (int d=0;d<ExtraCSSStrings.size();d++) {
    			writer.write(ExtraCSSStrings.get(d));
    		}
    		
    		// rtl
    		writer.write(".side-nav.abmrtl .collapsible-body li a { margin: 0px 4rem 0px 1rem;}");
    		
    		// NEW 18/10/16
    		writer.write(".abmscrollltbl-headerwrapper{position: static;display: inline-block}");
    		writer.write(".abmscrollltbl-header{position: static;display: inline-block;user-select: none;}");    		
    		writer.write(".abmscrollltbl-body{overflow-y: auto;display: block;}"); 
    		
    		writer.write(".abmscrollltbl-footer{display: block;}");
    		writer.write(".abmscrollltbl-footerwrapper{display: block;}");
    		
    		writer.write("#band-cookies{position:fixed;bottom:0;left:0;z-index:200;width:100%;padding:15px 0;text-align:center;font-size:14px;line-height:1.1;background-color:#151515;color:#FFF;box-shadow:0 -3px 3px 0 rgba(0,0,0,.15)}#band-cookies p{margin:0;padding:0 50px}#band-cookies-info,#band-cookies-ok{display:inline-block;color:#F0FFAA;font-weight:700;text-decoration:underline;margin-left:10px;cursor:pointer}#band-cookies-close{box-sizing: content-box;height:16px;width:16px;padding:8px;position:absolute;right:7px;top:50%;margin-top:-16px;-moz-border-radius:16px;-webkit-border-radius:16px;border-radius:16px;background-color:#000}@media (max-width:" + SML992 + "px){#band-cookies p{padding:15px 15px 0}#band-cookies-info,#band-cookies-ok{display:block;text-decoration:none;padding:10px 5px;margin-top:10px;background-color:#444;-moz-border-radius:2px;-webkit-border-radius:2px;border-radius:2px}#band-cookies-close{left:50%;margin-left:-16px;top:0}}");
    		
    		writer.write("#toast-container {pointer-events: none;}");
    		writer.write(".toast {pointer-events: auto;}");
    		
    		writer.write("@media only print { #band-cookies {display: none !important}} ");
    		writer.write("@media only print { table.responsive-table{width:100%;border-collapse:collapse;border-spacing:0;display:block;position:relative}table.responsive-table td,table.responsive-table th{margin:0;vertical-align:top}table.responsive-table thead{display:block;float:left;border:0;border-right:1px solid #d0d0d0}table.responsive-table thead tr{display:block;padding:0 10px 0 0}table.responsive-table thead tr th::before{content:\"\\00a0\"}table.responsive-table tbody{display:block;width:auto;position:relative;overflow-x:auto;white-space:nowrap}table.responsive-table tbody tr{display:inline-block;vertical-align:top}table.responsive-table th{display:block;text-align:right}table.responsive-table td{display:block;min-height:1.25em;text-align:left}table.responsive-table tr{padding:0 10px}table.responsive-table.bordered th{border-bottom:0;border-left:0}table.responsive-table.bordered td{border-left:0;border-right:0;border-bottom:0}table.responsive-table.bordered tr{border:0}table.responsive-table.bordered tbody tr{border-right:1px solid #d0d0d0}}");
    		writer.write("@media only print {.container {width: 85%; } }");
    		writer.write("@media only print {.row .col.s1{width:8.33333%;margin-left:0}.row .col.s2{width:16.66667%;margin-left:0}.row .col.s3{width:25%;margin-left:0}.row .col.s4{width:33.33333%;margin-left:0}.row .col.s5{width:41.66667%;margin-left:0}.row .col.s6{width:50%;margin-left:0}.row .col.s7{width:58.33333%;margin-left:0}.row .col.s8{width:66.66667%;margin-left:0}.row .col.s9{width:75%;margin-left:0}.row .col.s10{width:83.33333%;margin-left:0}.row .col.s11{width:91.66667%;margin-left:0}.row .col.s12{width:100%;margin-left:0}.row .col.offset-s1{margin-left:8.33333%}.row .col.offset-s2{margin-left:16.66667%}.row .col.offset-s3{margin-left:25%}.row .col.offset-s4{margin-left:33.33333%}.row .col.offset-s5{margin-left:41.66667%}.row .col.offset-s6{margin-left:50%}.row .col.offset-s7{margin-left:58.33333%}.row .col.offset-s8{margin-left:66.66667%}.row .col.offset-s9{margin-left:75%}.row .col.offset-s10{margin-left:83.33333%}.row .col.offset-s11{margin-left:91.66667%}.row .col.offset-s12{margin-left:100%}}");
    		writer.write("@media only print {.row .col.m1{width:8.33333%;margin-left:0}.row .col.m2{width:16.66667%;margin-left:0}.row .col.m3{width:25%;margin-left:0}.row .col.m4{width:33.33333%;margin-left:0}.row .col.m5{width:41.66667%;margin-left:0}.row .col.m6{width:50%;margin-left:0}.row .col.m7{width:58.33333%;margin-left:0}.row .col.m8{width:66.66667%;margin-left:0}.row .col.m9{width:75%;margin-left:0}.row .col.m10{width:83.33333%;margin-left:0}.row .col.m11{width:91.66667%;margin-left:0}.row .col.m12{width:100%;margin-left:0}.row .col.offset-m1{margin-left:8.33333%}.row .col.offset-m2{margin-left:16.66667%}.row .col.offset-m3{margin-left:25%}.row .col.offset-m4{margin-left:33.33333%}.row .col.offset-m5{margin-left:41.66667%}.row .col.offset-m6{margin-left:50%}.row .col.offset-m7{margin-left:58.33333%}.row .col.offset-m8{margin-left:66.66667%}.row .col.offset-m9{margin-left:75%}.row .col.offset-m10{margin-left:83.33333%}.row .col.offset-m11{margin-left:91.66667%}.row .col.offset-m12{margin-left:100%}}");
    		writer.write("@media only print {.row .col.l1{width:8.33333%;margin-left:0}.row .col.l2{width:16.66667%;margin-left:0}.row .col.l3{width:25%;margin-left:0}.row .col.l4{width:33.33333%;margin-left:0}.row .col.l5{width:41.66667%;margin-left:0}.row .col.l6{width:50%;margin-left:0}.row .col.l7{width:58.33333%;margin-left:0}.row .col.l8{width:66.66667%;margin-left:0}.row .col.l9{width:75%;margin-left:0}.row .col.l10{width:83.33333%;margin-left:0}.row .col.l11{width:91.66667%;margin-left:0}.row .col.l12{width:100%;margin-left:0}.row .col.offset-l1{margin-left:8.33333%}.row .col.offset-l2{margin-left:16.66667%}.row .col.offset-l3{margin-left:25%}.row .col.offset-l4{margin-left:33.33333%}.row .col.offset-l5{margin-left:41.66667%}.row .col.offset-l6{margin-left:50%}.row .col.offset-l7{margin-left:58.33333%}.row .col.offset-l8{margin-left:66.66667%}.row .col.offset-l9{margin-left:75%}.row .col.offset-l10{margin-left:83.33333%}.row .col.offset-l11{margin-left:91.66667%}.row .col.offset-l12{margin-left:100%}}");
    		writer.write("@media only print { html { font-size: 14.5px; } }");
    		writer.write("@media only print {.modal { width: 80%; } }");
    		writer.write("@media only print {.input-field .prefix ~ input { width: 86%; width: calc(100% - 3rem); } }");
    		writer.write("@media only print {.input-field .prefix ~ input { width: 80%; width: calc(100% - 3rem); } }");
    		writer.write("@media only screen {");
    		for (int zz=50;zz<=200;zz+=5) {
    			writer.write(" .zoom" + zz + " {");
    			double zzDbl = (zz/100.0);
    			writer.write("-moz-transform: scale(" + zzDbl + ");");
    			writer.write("-ms-transform: scale(" + zzDbl + ");");
    			writer.write("-webkit-transform: scale(" + zzDbl + ");");
    			writer.write("-o-transform: scale(" + zzDbl + ");");
    			writer.write("transform: scale(" + zzDbl + ");");
    			writer.write("}");    			
    		}
    		writer.write(" .zoomC {");
    		writer.write("-moz-transform-origin: 50% top;");
			writer.write("-ms-transform-origin: 50% top;");
			writer.write("-webkit-transform-origin: 50% top;");
			writer.write("transform-origin: 50% top;");
			writer.write("}");
			writer.write(" .zoomL {");
    		writer.write("-moz-transform-origin: left top;");
			writer.write("-ms-transform-origin: left top;");
			writer.write("-webkit-transform-origin: left top;");
			writer.write("transform-origin: left top;");
			writer.write("}");
    		writer.write("}");

    		writer.write("@media only print {");
    		writer.write("-moz-transform: unset;");
			writer.write("-ms-transform: unset;");
			writer.write("-webkit-transform: unset;");
			writer.write("-o-transform: unset;");
			writer.write("transform: unset;");
			writer.write("-moz-transform-origin: unset;");
			writer.write("-ms-transform-origin: unset;");
			writer.write("-webkit-transform-origin: unset;");
			writer.write("transform-origin: unset;");
			writer.write("}");
    		
    		writer.write("@media only print {.side-nav.fixed { left: -105%; } .side-nav.fixed.right-aligned { right: -105%; left: auto; } }\n");
    	
    		writer.write(".input-field label.active {-webkit-transform: translateY(-160%) !important;-moz-transform: translateY(-160%) !important;-ms-transform: translateY(-160%) !important;-o-transform: translateY(-160%) !important;transform: translateY(-160%) !important}");
    		
    		writer.write("input[type=date].invalid+label:after, input[type=date]:focus.invalid+label:after, input[type=datetime-local].invalid+label:after, input[type=datetime-local]:focus.invalid+label:after, input[type=email].invalid+label:after, input[type=email]:focus.invalid+label:after, input[type=number].invalid+label:after, input[type=number]:focus.invalid+label:after, input[type=password].invalid+label:after, input[type=password]:focus.invalid+label:after, input[type=search].invalid+label:after, input[type=search]:focus.invalid+label:after, input[type=tel].invalid+label:after, input[type=tel]:focus.invalid+label:after, input[type=text].invalid+label:after, input[type=text]:focus.invalid+label:after, input[type=time].invalid+label:after, input[type=time]:focus.invalid+label:after, input[type=url].invalid+label:after, input[type=url]:focus.invalid+label:after, textarea.materialize-textarea.invalid+label:after, textarea.materialize-textarea:focus.invalid+label:after{margin-top:8px;}");
    		writer.write("input[type=date]:focus.valid+label:after, input[type=datetime-local].valid+label:after, input[type=datetime-local]:focus.valid+label:after, input[type=email].valid+label:after, input[type=email]:focus.valid+label:after, input[type=number].valid+label:after, input[type=number]:focus.valid+label:after, input[type=password].valid+label:after, input[type=password]:focus.valid+label:after, input[type=search].valid+label:after, input[type=search]:focus.valid+label:after, input[type=tel].valid+label:after, input[type=tel]:focus.valid+label:after, input[type=text].valid+label:after, input[type=text]:focus.valid+label:after, input[type=time].valid+label:after, input[type=time]:focus.valid+label:after, input[type=url].valid+label:after, input[type=url]:focus.valid+label:after, textarea.materialize-textarea.valid+label:after, textarea.materialize-textarea:focus.valid+label:after{margin-top:8px;}");

    		writer.write("@media only screen and (min-width: " + SML601 + "px) and (max-width: " + SML992 + "px) {#toast-container {right: 5% !important;}}");
    		writer.write("@media only screen and (min-width: " + SML993 + "px) {#toast-container {left: 7% !important;}}");
    		writer.write(".toast {height: auto !important; min-height: 48px !important; line-height: 1.5em !important; word-break: break-all !important; padding: 10px 25px !important; display:-webkit-box !important; display: -webkit-flex !important; display: -ms-flexbox !important; display: flex !important; -webkit-box-align: center !important; -webkit-align-items: center !important; -ms-flex-align: center !important; align-items: center !important; -webkit-box-pack: justify !important; -webkit-justify-content: space-between !important; -ms-flex-pack: justify !important; justify-content: space-between !important;}");
    		
        	if (!(navigationBar==null)) {
        		if (navigationBar.SideItems.isEmpty() || navigationBar.BarType.equals(ABMaterial.SIDEBAR_MANUAL_ALWAYSHIDE) || navigationBar.BarType.equals(ABMaterial.SIDEBAR_AUTO)) {
        			writer.write("header,main,footer{padding-left:0px;}");
        		} else {
        			writer.write("header,main,footer{padding-left:" + navigationBar.NBWidthPx + "px;}");
        		}
        	} else {
        		writer.write("header,main,footer{padding-left:0px;}");
        	}
        	writer.write(".col{position: relative;}");
        	writer.write(".section{padding-top:6px;padding-bottom:6px;}");
        	
        	if (NeedsResponsiveContainer) {
        		writer.write(".responsive-cont[max-width~=\"" + SML600 + "\"] .row .col.s1{width:8.3333333333%;margin-left:auto;left:auto;right:auto}.responsive-cont[max-width~=\"" + SML600 + "\"] .row .col.s2{width:16.6666666667%;margin-left:auto;left:auto;right:auto}.responsive-cont[max-width~=\"" + SML600 + "\"] .row .col.s3{width:25%;margin-left:auto;left:auto;right:auto}.responsive-cont[max-width~=\"" + SML600 + "\"] .row .col.s4{width:33.3333333333%;margin-left:auto;left:auto;right:auto}.responsive-cont[max-width~=\"" + SML600 + "\"] .row .col.s5{width:41.6666666667%;margin-left:auto;left:auto;right:auto}.responsive-cont[max-width~=\"" + SML600 + "\"] .row .col.s6{width:50%;margin-left:auto;left:auto;right:auto}.responsive-cont[max-width~=\"" + SML600 + "\"] .row .col.s7{width:58.3333333333%;margin-left:auto;left:auto;right:auto}.responsive-cont[max-width~=\"" + SML600 + "\"] .row .col.s8{width:66.6666666667%;margin-left:auto;left:auto;right:auto}.responsive-cont[max-width~=\"" + SML600 + "\"] .row .col.s9{width:75%;margin-left:auto;left:auto;right:auto}.responsive-cont[max-width~=\"" + SML600 + "\"] .row .col.s10{width:83.3333333333%;margin-left:auto;left:auto;right:auto}.responsive-cont[max-width~=\"" + SML600 + "\"] .row .col.s11{width:91.6666666667%;margin-left:auto;left:auto;right:auto}.responsive-cont[max-width~=\"" + SML600 + "\"] .row .col.s12{width:100%;margin-left:auto;left:auto;right:auto}.responsive-cont[max-width~=\"" + SML600 + "\"] .row .col.offset-s1{margin-left:8.3333333333%}.responsive-cont[max-width~=\"" + SML600 + "\"] .row .col.offset-s2{margin-left:16.6666666667%}.responsive-cont[max-width~=\"" + SML600 + "\"] .row .col.offset-s3{margin-left:25%}.responsive-cont[max-width~=\"" + SML600 + "\"] .row .col.offset-s4{margin-left:33.3333333333%}.responsive-cont[max-width~=\"" + SML600 + "\"] .row .col.offset-s5{margin-left:41.6666666667%}.responsive-cont[max-width~=\"" + SML600 + "\"] .row .col.offset-s6{margin-left:50%}.responsive-cont[max-width~=\"" + SML600 + "\"] .row .col.offset-s7{margin-left:58.3333333333%}.responsive-cont[max-width~=\"" + SML600 + "\"] .row .col.offset-s8{margin-left:66.6666666667%}.responsive-cont[max-width~=\"" + SML600 + "\"] .row .col.offset-s9{margin-left:75%}.responsive-cont[max-width~=\"" + SML600 + "\"] .row .col.offset-s10{margin-left:83.3333333333%}.responsive-cont[max-width~=\"" + SML600 + "\"] .row .col.offset-s11{margin-left:91.6666666667%}.responsive-cont[max-width~=\"" + SML600 + "\"] .row .col.offset-s12{margin-left:100%}.responsive-cont[min-width~=\"" + SML601 + "\"] .row .col.m1{width:8.3333333333%;margin-left:auto;left:auto;right:auto}.responsive-cont[min-width~=\"" + SML601 + "\"] .row .col.m2{width:16.6666666667%;margin-left:auto;left:auto;right:auto}.responsive-cont[min-width~=\"" + SML601 + "\"] .row .col.m3{width:25%;margin-left:auto;left:auto;right:auto}.responsive-cont[min-width~=\"" + SML601 + "\"] .row .col.m4{width:33.3333333333%;margin-left:auto;left:auto;right:auto}.responsive-cont[min-width~=\"" + SML601 + "\"] .row .col.m5{width:41.6666666667%;margin-left:auto;left:auto;right:auto}.responsive-cont[min-width~=\"" + SML601 + "\"] .row .col.m6{width:50%;margin-left:auto;left:auto;right:auto}.responsive-cont[min-width~=\"" + SML601 + "\"] .row .col.m7{width:58.3333333333%;margin-left:auto;left:auto;right:auto}.responsive-cont[min-width~=\"" + SML601 + "\"] .row .col.m8{width:66.6666666667%;margin-left:auto;left:auto;right:auto}.responsive-cont[min-width~=\"" + SML601 + "\"] .row .col.m9{width:75%;margin-left:auto;left:auto;right:auto}.responsive-cont[min-width~=\"" + SML601 + "\"] .row .col.m10{width:83.3333333333%;margin-left:auto;left:auto;right:auto}.responsive-cont[min-width~=\"" + SML601 + "\"] .row .col.m11{width:91.6666666667%;margin-left:auto;left:auto;right:auto}.responsive-cont[min-width~=\"" + SML601 + "\"] .row .col.m12{width:100%;margin-left:auto;left:auto;right:auto}.responsive-cont[min-width~=\"" + SML601 + "\"] .row .col.offset-m1{margin-left:8.3333333333%}.responsive-cont[min-width~=\"" + SML601 + "\"] .row .col.offset-m2{margin-left:16.6666666667%}.responsive-cont[min-width~=\"" + SML601 + "\"] .row .col.offset-m3{margin-left:25%}.responsive-cont[min-width~=\"" + SML601 + "\"] .row .col.offset-m4{margin-left:33.3333333333%}.responsive-cont[min-width~=\"" + SML601 + "\"] .row .col.offset-m5{margin-left:41.6666666667%}.responsive-cont[min-width~=\"" + SML601 + "\"] .row .col.offset-m6{margin-left:50%}.responsive-cont[min-width~=\"" + SML601 + "\"] .row .col.offset-m7{margin-left:58.3333333333%}.responsive-cont[min-width~=\"" + SML601 + "\"] .row .col.offset-m8{margin-left:66.6666666667%}.responsive-cont[min-width~=\"" + SML601 + "\"] .row .col.offset-m9{margin-left:75%}.responsive-cont[min-width~=\"" + SML601 + "\"] .row .col.offset-m10{margin-left:83.3333333333%}.responsive-cont[min-width~=\"" + SML601 + "\"] .row .col.offset-m11{margin-left:91.6666666667%}.responsive-cont[min-width~=\"" + SML601 + "\"] .row .col.offset-m12{margin-left:100%}.responsive-cont[min-width~=\"" + SML993 + "\"] .row .col.l1{width:8.3333333333%;margin-left:auto;left:auto;right:auto}.responsive-cont[min-width~=\"" + SML993 + "\"] .row .col.l2{width:16.6666666667%;margin-left:auto;left:auto;right:auto}.responsive-cont[min-width~=\"" + SML993 + "\"] .row .col.l3{width:25%;margin-left:auto;left:auto;right:auto}.responsive-cont[min-width~=\"" + SML993 + "\"] .row .col.l4{width:33.3333333333%;margin-left:auto;left:auto;right:auto}.responsive-cont[min-width~=\"" + SML993 + "\"] .row .col.l5{width:41.6666666667%;margin-left:auto;left:auto;right:auto}.responsive-cont[min-width~=\"" + SML993 + "\"] .row .col.l6{width:50%;margin-left:auto;left:auto;right:auto}.responsive-cont[min-width~=\"" + SML993 + "\"] .row .col.l7{width:58.3333333333%;margin-left:auto;left:auto;right:auto}.responsive-cont[min-width~=\"" + SML993 + "\"] .row .col.l8{width:66.6666666667%;margin-left:auto;left:auto;right:auto}.responsive-cont[min-width~=\"" + SML993 + "\"] .row .col.l9{width:75%;margin-left:auto;left:auto;right:auto}.responsive-cont[min-width~=\"" + SML993 + "\"] .row .col.l10{width:83.3333333333%;margin-left:auto;left:auto;right:auto}.responsive-cont[min-width~=\"" + SML993 + "\"] .row .col.l11{width:91.6666666667%;margin-left:auto;left:auto;right:auto}.responsive-cont[min-width~=\"" + SML993 + "\"] .row .col.l12{width:100%;margin-left:auto;left:auto;right:auto}.responsive-cont[min-width~=\"" + SML993 + "\"] .row .col.offset-l1{margin-left:8.3333333333%}.responsive-cont[min-width~=\"" + SML993 + "\"] .row .col.offset-l2{margin-left:16.6666666667%}.responsive-cont[min-width~=\"" + SML993 + "\"] .row .col.offset-l3{margin-left:25%}.responsive-cont[min-width~=\"" + SML993 + "\"] .row .col.offset-l4{margin-left:33.3333333333%}.responsive-cont[min-width~=\"" + SML993 + "\"] .row .col.offset-l5{margin-left:41.6666666667%}.responsive-cont[min-width~=\"" + SML993 + "\"] .row .col.offset-l6{margin-left:50%}.responsive-cont[min-width~=\"" + SML993 + "\"] .row .col.offset-l7{margin-left:58.3333333333%}.responsive-cont[min-width~=\"" + SML993 + "\"] .row .col.offset-l8{margin-left:66.6666666667%}.responsive-cont[min-width~=\"" + SML993 + "\"] .row .col.offset-l9{margin-left:75%}.responsive-cont[min-width~=\"" + SML993 + "\"] .row .col.offset-l10{margin-left:83.3333333333%}.responsive-cont[min-width~=\"" + SML993 + "\"] .row .col.offset-l11{margin-left:91.6666666667%}.responsive-cont[min-width~=\"" + SML993 + "\"] .row .col.offset-l12{margin-left:100%}");        		
        	}
        	
        	writer.write(".footerfixed{position:fixed;right:0px;left:0px;bottom:0;overflow:hidden;z-index:997;box-shadow: 0 0px 10px 0 rgba(0,0,0,0.16)}");
        	writer.write("@media only screen and (min-width:" + SML993 + "px){.footerfixed {position:fixed;right:0px;left:0px;bottom:0;padding-right:0px;overflow:hidden;z-index:997;}}");
        	writer.write("@media only print {.footerfixed {position:fixed;right:0px;left:0px;bottom:0;padding-right:0px;overflow:hidden;z-index:997;}}");
        	writer.write(".footerfloating{position:bottom;right:0px;left:0px;bottom:0;overflow:hidden;}");
        	writer.write("@media only screen and (min-width:" + SML993 + "px){.footerfloating{position:bottom;right:0px;left:0px;bottom:0;padding-right:0px;overflow:hidden;}}");
        	writer.write("@media only print {.footerfloating{position:bottom;right:0px;left:0px;bottom:0;padding-right:0px;overflow:hidden;}}");
        	
        	writer.write("@media only screen and (max-width:" + SML992 + "px){header,main,footer{padding-left:0;}}");
        	writer.write("@media only print {header,main,footer{padding-left:0;}}");
        	writer.write("#responsive-img{width:80%;display:block;margin:0 auto;}");
        	writer.write("table{border-collapse:collapse;border-spacing:0;}");
        	writer.write("td,th{padding:5px 5px;display:table-cell;border-radius:0px;}");
        	writer.write(".abmnowrap{white-space: nowrap}");
        	
        	writer.write(".alwaysoverflow {overflow-y: scroll}");
        	writer.write(".neveroverflow {overflow: hidden}");
        	writer.write("nav ul li {height:56px;}");
        	
        	writer.write(".masoncolumn { float: left; }");
        	writer.write(".size-1of1 { width: 100%; }");
        	writer.write(".size-1of2 { width: 50%; }");
        	writer.write(".size-1of3 { width: 33.333%; }");
        	writer.write(".size-1of4 { width: 25%; }");
        	writer.write(".size-1of5 { width: 20%; }");
        	writer.write(".size-1of6 { width: 16.666%; }");
        	writer.write(".size-1of7 { width: 14.285%; }");
        	writer.write(".size-1of8 { width: 12.5%; }");
        	writer.write(".size-1of9 { width: 11.111%; }");
        	writer.write(".size-1of10 { width: 10%; }");
        	writer.write(".size-1of11 { width: 9.090%; }");
        	writer.write(".size-1of12 { width: 8.333%; }");
        	
        	// toolbar
        	if (NeedsMask || NeedsTable) {
        		if (ABMaterial.InputMaskNewVersion) {
        			writer.write("mark.im-caret{animation:1s blink step-end infinite!important}mark.im-caret-select{background-color:rgba(0,0,0,.25)}@keyframes blink{from,to{border-right-color:#000}50%{border-right-color:transparent}}span.im-static{color:grey}div.im-colormask{display:inline-block;border-style:inset;border-width:2px;appearance:textfield;cursor:text}div.im-colormask>input,div.im-colormask>input:-webkit-autofill{position:absolute!important;display:inline-block;background-color:transparent;color:transparent;-webkit-text-fill-color:transparent;transition:background-color 5000s ease-in-out 0s;caret-color:transparent;text-shadow:none;appearance:caret;border-style:none;left:0}div.im-colormask>input:focus{outline:0}div.im-colormask>input::selection{background:0 0}div.im-colormask>input::-moz-selection{background:0 0}div.im-colormask>input:-webkit-autofill~div{background-color:#faffbd}div.im-colormask>div{color:#000;display:inline-block;width:100px}");
        		}
        	}
        	
        	if (navigationBar!=null) {
        		if (NeedsSimpleBar) {
        			writer.write(".side-nav {height:100%; padding-bottom: 0px !important}");
        			writer.write("ul.side-nav.fixed { overflow-x: hidden !important; overflow-y: hidden !important;}");
        			writer.write(".simplebar-scrollbar {background: " + navigationBar.AltScrollBarColor + " !important}.simplebar-track:hover .simplebar-scrollbar{opacity:" + navigationBar.AltScrollBarColorOpacity + " !important}.simplebar-track .simplebar-scrollbar.visible{opacity:" + navigationBar.AltScrollBarColorOpacity + " !important}");
        		} else {
        			writer.write(".side-nav {height:100%;}");
        		}
        	} else {
        		writer.write(".side-nav {height:100%}");
        	}
        	writer.write(".side-nav .collapsible-body li.active,.side-nav.fixed .collapsible-body li.active{ background-color:rgba(0,0,0,0.05);}");
        	
        	writer.write(".side-nav .collapsible-body li.active a,.side-nav.fixed .collapsible-body li.active a {color:#fff;}");
        	writer.write(".side-nav li:hover, .side-nav li.active {background-color: transparent;}");
        	for (Map.Entry<String, ThemeNavigationBar> entry : CompleteTheme.NavBars.entrySet()) {
    			WriteCSSNavBar(writer, entry.getValue());
    		}
    		ThemeNavigationBar defnb = new ThemeNavigationBar();
    		defnb.Colorize(CompleteTheme.MainColor);
    		WriteCSSNavBar(writer, defnb);
    		
    		writer.write("a.abmnextsectionbutton {	position: absolute;	bottom: -10px;left: 50%;z-index: 2;display: inline-block;-webkit-transform: translate(-50%, -50%);transform: translate(-50%, -50%);font : normal 400 18px/1 'Josefin Sans', sans-serif;letter-spacing: .1em;text-decoration: none;transition: opacity .3s;}");
        	
    		writer.write(".section-menu  {display: table;position: fixed;top: 0;right: 20px;height: 100%;}");
    		writer.write(".section-menu ul {display: table-cell;margin: 0; padding: 0; list-style: none; vertical-align: middle;}");
    		writer.write(".section-menu li {display: block; }");
    		writer.write(".section-menu a {display: block; position: relative;	height: 30px; width: 30px; line-height: 30px;	text-align: right; white-space: nowrap;	transition: all 0.5s ease; margin-top: 30px; margin-bottom: 30px;}");
    		writer.write(".section-menu a:before {content: \"\"; display: block;	position: relative;	border-radius: 15px; width: 100%; height: 100%;	-webkit-transform: scale(0.6); -moz-transform: scale(0.6); -ms-transform: scale(0.6); transform: scale(0.6); -moz-box-shadow:  0 8px 17px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19); -webkit-box-shadow:  0 8px 17px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19); box-shadow:  0 8px 17px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19); transition: all 0.3s ease; z-index: 999;}");
    		writer.write(".section-menu a span { position: absolute; top: 0; right: 0; padding-right: 45px;	opacity: 0;	transition: all 0.3s ease; font: normal 400 18px/1 'Josefin Sans', sans-serif; letter-spacing: .1em; padding-top: 6px; background-color: rgba(0,0,0,0.3); border-radius: 15px; height: 30px; padding-left: 8px; z-index: 998;}");
    		writer.write(".section-menu a:hover span {opacity: 1;}");
    		
    		for (Map.Entry<String, Section> entry : CompleteTheme.Page.Sections.entrySet()) {
    			WriteCSSSection(writer, entry.getValue());
    		}
    		Section defsec = CompleteTheme.Page.GetSection("default");
    		WriteCSSSection(writer, defsec);
    		writer.write("@-webkit-keyframes sdb04 { 0% { -webkit-transform: rotate(-45deg) translate(0, 0);} 20% { -webkit-transform: rotate(-45deg) translate(-10px, 10px); } 40% { -webkit-transform: rotate(-45deg) translate(0, 0); }}");
    		writer.write("@keyframes sdb04 { 0% {transform: rotate(-45deg) translate(0, 0);} 20% { transform: rotate(-45deg) translate(-10px, 10px); } 40% { transform: rotate(-45deg) translate(0, 0); }}");
    		writer.write("@-webkit-keyframes sdb04u { 0% { -webkit-transform: rotate(135deg) translate(0, 0); } 20% { -webkit-transform: rotate(135deg) translate(-10px, 10px); } 40% { -webkit-transform: rotate(135deg) translate(0, 0);}}");
    		writer.write("@keyframes sdb04u { 0% {transform: rotate(135deg) translate(0, 0);} 20% {transform: rotate(135deg) translate(-10px, 10px);} 40% {transform: rotate(135deg) translate(0, 0);}}");
    		
    		for (Map.Entry<String, ThemeCell> entry : CompleteTheme.Cells.entrySet()) {
    			WriteCSSCell(writer, entry.getValue());
    		}
        	
        	if (!(navigationBar==null)) {
    			writer.write("#sidenav-overlay-sidebar {position: fixed;top: " + navigationBar.TopBarHeightPx + "px;left: 0;right: 0;height: 120vh;background-color: rgba(0,0,0,0.5);z-index: 998;will-change: opacity;}");
    		} else {
    			writer.write("#sidenav-overlay-sidebar {position: fixed;top: 0;left: 0;right: 0;height: 120vh;background-color: rgba(0,0,0,0.5);z-index: 998;will-change: opacity;}");
    		}
        	
        	writer.write("ul.side-nav.fixed li.logo{text-align:center;margin-top:0px;margin-bottom:12px;border-bottom:1px transparent #ddd;}ul.side-nav.fixed li.logo:hover{background-color:transparent;}");
        	writer.write("ul.side-nav.fixed {overflow-x:hidden; overflow-y: auto}");
        	writer.write("ul.side-nav.fixed li {line-height:56px;}");
        	writer.write("ul.side-nav.fixed li a {font-size:1rem;line-height:3.5rem;height:3.5rem;}");
        	writer.write("ul.side-nav.fixed ul.collapsible-accordion {background-color:#FFF;}");
        	writer.write("ul.side-nav.fixed:hover{overflow-y:auto;}");
        	writer.write(".side-nav .collapsible-body li a {margin:0px 1rem 0px 4rem;}");
        	writer.write(".bold > a {font-weight: bold;}");
        	writer.write("#logo-container{height:" + LogoHeight + "px;margin-bottom:4px;padding:0;}");
        	writer.write("nav.top-nav {height:56px;box-shadow: none;}");
        	writer.write("nav.top-nav a.page-title {line-height:56px;font-size:48px;}");

        	writer.write("@media only screen and (max-width: " + SML600 + "px) {nav div.container {margin-left:0;}}");
        	writer.write("@media only print {nav div.container {width:100%;}}");
        	
        	if (!(navigationBar==null)) {
        		if (navigationBar.TopBarFixed) {
        			writer.write("a.button-collapse.top-nav{position:fixed;text-align:center;height:56px;width:56px;left:3rem;top:0;float:none;margin-left:1.5rem;color:#fff;font-size:36px;z-index:999;}");
        		} else {
        			writer.write("a.button-collapse.top-nav{position:absolute;text-align:center;height:56px;width:56px;left:3rem;top:0;float:none;margin-left:1.5rem;color:#fff;font-size:36px;z-index:999;}\n");
        		}
        	} else {
        		writer.write("a.button-collapse.top-nav{position:absolute;text-align:center;height:56px;width:56px;left:3rem;top:0;float:none;margin-left:1.5rem;color:#fff;font-size:36px;z-index:999;}");
        	}
        	
        	writer.write("@media only screen and (max-width: " + SML600 + "px) {a.button-collapse.top-nav {margin-left: 0rem;}}");
        	writer.write("@media only print {a.button-collapse.top-nav {margin-left: 0rem;}}");
        	
        	writer.write(".indicatorunknown.indleft{right: auto;left: 5px;}");
        	writer.write("@media only screen and (min-width:" + SML993 + "px){.indicatorunknown.indleftright{right: auto;left: 5px;}}");
        	
        	writer.write(".indicatorunknown{position: fixed !important;content: '' !important;right: 5px;top: 5px !important;width: 12px !important;height: 12px !important;background: transparent !important;border-radius: 50% !important;transition: all .3s ease !important;z-index:100000}");
        	writer.write(".indicatoractive{position: fixed !important;content: '' !important;right: 5px !important;top: 5px !important;width: 12px !important;height: 12px !important;background: " + ABMaterial.GetColorStrMap(CompleteTheme.Page.ConnectedIndicatorColor,CompleteTheme.Page.ConnectedIndicatorColorIntensity) + " !important;border-radius: 50% !important;transition: all .3s ease !important;z-index:100000}");
    		writer.write(".indicatorinactive{position: fixed !important;content: '' !important;right: 5px !important;top: 5px !important;width: 12px !important;height: 12px !important;background: " + ABMaterial.GetColorStrMap(CompleteTheme.Page.DisconnectedIndicatorColor,CompleteTheme.Page.DisconnectedIndicatorColorIntensity) + " !important;border-radius: 50% !important;transition: all .3s ease !important;z-index:100000}");
        	
        	writer.write("a.button-collapse.top-nav.full{line-height:56px;}");
        	writer.write("@media only screen and (max-width:" + SML992 + "px){a.button-collapse.top-nav{left:0%;font-size: 30px;}}");
        	writer.write("@media only screen and (max-width:" + SML600 + "px){a.button-collapse.top-nav{left:0%;font-size: 24px;}}");
        	writer.write("@media only screen and (max-width:" + SML992 + "px){nav .nav-wrapper{text-align: center;}nav .nav-wrapper a.page-title{font-size:48px;}}");
        	writer.write("@media only screen and (min-width:" + SML993 + "px){.container{width: 85%;}}");
        	writer.write("@media only screen and (min-width:" + SML601 + "px){nav, nav .nav-wrapper i,nav a.button-collapse,nav a.button-collapse i {height:56px;line-height:56px;} .navbar-fixed{height:56px;}}");
        	
        	writer.write("@media only print {a.button-collapse.top-nav{left:0%;font-size: 30px;}}");
        	writer.write("@media only print {nav .nav-wrapper{text-align: center;}nav .nav-wrapper a.page-title{font-size:48px;}}");
        	writer.write("@media only print {.container{width: 85%;}}");
        	writer.write("@media only print {nav, nav .nav-wrapper i,nav a.button-collapse,nav a.button-collapse i {height:56px;line-height:56px;} .navbar-fixed{height:56px;}}");
        	
        	if (!(navigationBar==null)) {        		
        		if (navigationBar.SideItems.isEmpty() || navigationBar.BarType.equals(ABMaterial.SIDEBAR_MANUAL_ALWAYSHIDE ) || navigationBar.BarType.equals(ABMaterial.SIDEBAR_AUTO )) {
        			writer.write(".fixed-top {position:fixed;right:0px;left:0px;z-index:997;}");
        			writer.write("@media only screen and (min-width:" + SML993 + "px){.fixed-top{position:fixed;right:0px;left:0px;z-index:997;}}");
        			writer.write("@media only print {.fixed-top{position:fixed;right:0px;left:0px;z-index:997;}}");
        		} else {
        			writer.write(".fixed-top {position:fixed;right:0px;left:0px;z-index:997;}");
                	writer.write("@media only screen and (min-width:" + SML993 + "px){.fixed-top{position:fixed;right:0px;left:" + navigationBar.NBWidthPx + "px;z-index:997;padding-right:" + navigationBar.NBWidthPx + "px;}}");
                	writer.write("@media only print {.fixed-top{position:fixed;right:0px;left:" + navigationBar.NBWidthPx + "px;z-index:997;padding-right:" + navigationBar.NBWidthPx + "px;}}");
        		}
        	} else {
        		writer.write(".fixed-top{position:fixed;right:0px;left:0px;z-index:997;}");
            	writer.write("@media only screen and (min-width:" + SML993 + "px){.fixed-top{position:fixed;right:0px;left:0px;z-index:997;}}");
            	writer.write("@media only print {.fixed-top{position:fixed;right:0px;left:0px;z-index:997;}}");
        	}
        	writer.write(".floating-top{position:absolute;right:0px;left:0px;z-index:997;}");
        	if (!(navigationBar==null)) {
        		if (navigationBar.SideItems.isEmpty() || navigationBar.BarType.equals(ABMaterial.SIDEBAR_MANUAL_ALWAYSHIDE ) || navigationBar.BarType.equals(ABMaterial.SIDEBAR_AUTO )) {
        			writer.write("@media only screen and (min-width:" + SML993 + "px){.floating-top{position:absolute;right:0px;left:0px;z-index:997;padding-right:0px;padding-left:0px;}}"); // was z-index: 1
        			writer.write("@media only print {.floating-top{position:absolute;right:0px;left:0px;z-index:997;padding-right:0px;padding-left:0px;}}"); // was z-index: 1
        		} else {
        			writer.write("@media only screen and (min-width:" + SML993 + "px){.floating-top{position:absolute;right:0px;left:0px;z-index:997;padding-right:0px;padding-left:" + navigationBar.NBWidthPx + "px;}}");  // was z-index: 1
        			writer.write("@media only print {.floating-top{position:absolute;right:0px;left:0px;z-index:997;padding-right:0px;padding-left:" + navigationBar.NBWidthPx + "px;}}");  // was z-index: 1
        		}
        	} else {
        		writer.write("@media only screen and (min-width:" + SML993 + "px){.floating-top{position:absolute;right:0px;left:0px;z-index:997;padding-right:0px;padding-left:240px;}}");  // was z-index: 1
        		writer.write("@media only print {.floating-top{position:absolute;right:0px;left:0px;z-index:997;padding-right:0px;padding-left:240px;}}");  // was z-index: 1
        	}
        	if (!(navigationBar==null)) {
        		if (navigationBar.BarType.equals(ABMaterial.SIDEBAR_MANUAL_ALWAYSHIDE)) {
        			writer.write("nav .brand-logo{font-size:1.8rem;left: 50%;-webkit-transform: translateX(-50%);-moz-transform: translateX(-50%);-ms-transform: translateX(-50%);-o-transform: translateX(-50%);transform: translateX(-50%);}");        			
        		} else {
        			if (navigationBar.BarType.equals(ABMaterial.SIDEBAR_AUTO )) {
            			writer.write("nav .brand-logo{font-size:1.8rem;}");
            		} else {
            			writer.write("nav .brand-logo{font-size:1.8rem;}");
            		}
        		}
        	} else {
        		writer.write("nav .brand-logo{font-size:1.8rem;}");
        	}
        	
        	writer.write(".swal2pos-tl {margin-left: 0px;margin-top:0px}");
        	writer.write(".swal2pos-tc {margin-top:0px}");
        	writer.write(".swal2pos-tr {margin-right: 0px;margin-top:0px}");
        	writer.write(".swal2pos-cl {margin-left: 0px}");
        	writer.write(".swal2pos-cc {}");
        	writer.write(".swal2pos-cr {margin-right: 0px}");
        	writer.write(".swal2pos-bl {margin-left: 0px;margin-bottom:0px}");
        	writer.write(".swal2pos-bc {margin-bottom:0px}");
        	writer.write(".swal2pos-br {margin-right: 0px;margin-bottom:0px}");
        	
        	writer.write(".swal2-modal {font-family: roboto,arial,sans-serif}");
        	writer.write(".swal2-modal, .swal2-modal .swal2-styled {border-radius:2px}");
        	writer.write(".swal2-modal .swal2-styled {font-weight:inherit}");
        	writer.write(".swal2-modal .swal2-input {border-radius:0!important; box-shadow:0 0 0 0!important; border-width:0 0 1px 0!important}");
        	writer.write(".swal2-modal .swal2-input:focus {box-shadow:0 1px 0 0!important}");
        	
        	for (Map.Entry<String, ThemeMsgBox> entry : CompleteTheme.Msgboxes.entrySet()) {
    			WriteCSSMsgbox(writer, entry.getValue());
    		}
    		ThemeMsgBox defmsgbox = new ThemeMsgBox();
    		defmsgbox.Colorize(CompleteTheme.MainColor);
    		WriteCSSMsgbox(writer, defmsgbox);
        	
        	writer.write("@media only screen and (max-width:" + SML993 + "px){nav .brand-logo{font-size:1.6rem;left: 60px;text-overflow: clip;-webkit-transform: translateX(0%);-moz-transform: translateX(0%);-ms-transform: translateX(0%);-o-transform: translateX(0%);transform: translateX(0%);overflow: hidden;text-align: left}}");
        	writer.write("@media only screen and (max-width:" + SML601 + "px){nav .brand-logo{font-size:1.4rem;left: 60px;text-overflow: clip;-webkit-transform: translateX(8%);-moz-transform: translateX(8%);-ms-transform: translateX(8%);-o-transform: translateX(8%);transform: translateX(8%);overflow: hidden;text-align: left}}");
        	
        	writer.write("@media only print {nav .brand-logo{font-size:1.6rem;left: 60px;text-overflow: clip;-webkit-transform: translateX(0%);-moz-transform: translateX(0%);-ms-transform: translateX(0%);-o-transform: translateX(0%);transform: translateX(0%);overflow: hidden;text-align: left}}");
        	
        	writer.write(".fixed-action-btn ul button.btn-floating{opacity:0;}");
        	writer.write(".collapsible-header{display:block;cursor: pointer;min-height:3rem;line-height:0rem;padding:0 1rem;background-color:#fff;border-bottom:1px solid #ddd;}");
        	writer.write(".collapsible-body{display:none;cursor:pointer;border-bottom:1px solid #ddd;-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;}");
        	writer.write(".collapsible-body i {width:2rem;font-size:1.6rem;line-height:0rem;display:block;float:left;text-align:center;margin-right:1rem;}");
        	
        	writer.write(".iosb{position:absolute;z-index:20;background:#fff}.iosb-content{text-align:center;font-weight:700;font-family:Arial,sans-serif}.iosb-24{-webkit-box-shadow:0 2px 4px rgba(68,68,68,.8),0 2px rgba(255,255,255,.3) inset;-moz-box-shadow:0 2px 4px rgba(68,68,68,.8),0 2px rgba(255,255,255,.3) inset;box-shadow:0 2px 4px rgba(68,68,68,.8),0 2px rgba(255,255,255,.3) inset;min-width:24px;height:24px}.iosb-24,.iosb-24 .iosb-inner{-moz-border-radius:12px;border-radius:12px}.iosb-24 .iosb-inner{margin:2px;min-width:20px;height:20px}.iosb-24 .iosb-content{padding:0 6px;line-height:20px;height:20px}.iosb-24.iosb-top-left{top:3px;left:-11px}.iosb-24.iosb-top-right{top:3px;right:-11px}.iosb-24.iosb-bottom-left{bottom:3px;left:-11px}.iosb-24.iosb-bottom-right{bottom:3px;right:-11px}.iosb-24 .iosb-number,.iosb-24 .iosb-string{font-size:12px}");
        	writer.write(".iosb-24.iosb-fontextraright{margin-right: 11px !important} .iosb-24.iosb-fontextraleft{margin-left: 11px !important} .iosb-24.iosb-fontextrarightnbsp{margin-right: 30px !important}");
        	
        	writer.write(".modal.top-sheet {top: -100%;bottom: auto;margin: 0;width: 100%;max-height: 45%; border-radius: 0;will-change: top, opacity;}");
        	
        	writer.write(".rclick, .cclick{cursor: pointer;}");
        	writer.write(".rclick.waves-effect, .cclick.waves-effect{display: block !important;}");
        	
        	writer.write(".btn-floating.halfway-fab { position: absolute; bottom: 0; -webkit-transform: translateY(50%); transform: translateY(50%);}");
        	writer.write(".btn-floating.halfway-fab.left {right: auto; left: 24px;}");
        	writer.write(".btn-floating.halfway-fab.right {left: auto; right: 24px}");
        	writer.write(".btn-floating.halfway-fab.center {left: 50%; top: 100%; -webkit-transform: translate(-50%, -50%);transform: translate(-50%, -50%);}");
        	if (!(navigationBar==null)) {        		
        		if (navigationBar.SideItems.isEmpty() || navigationBar.BarType.equals(ABMaterial.SIDEBAR_MANUAL_ALWAYSHIDE ) || navigationBar.BarType.equals(ABMaterial.SIDEBAR_AUTO )) {
        			// nothing
        		} else {
        			writer.write("@media only screen and (min-width:" + SML993 + "px){.btn-floating.halfway-fab.right {right: " + (24 + navigationBar.NBWidthPx) + "px} }");
        			writer.write("@media only print {.btn-floating.halfway-fab.right {right: " + (24 + navigationBar.NBWidthPx) + "px} }");
        			int half = (int) Math.floor((navigationBar.NBWidthPx/2));
        			if (navigationBar.TopBarFixed) {
        				writer.write("@media only screen and (min-width:" + SML993 + "px){.btn-floating.halfway-fab.center {left: calc(50% - " + half + "px);} }");
        				writer.write("@media only print {.btn-floating.halfway-fab.center {left: calc(50% - " + half + "px);} }");
        			} else {
        				writer.write("@media only screen and (min-width:" + SML993 + "px){.btn-floating.halfway-fab.center {left: calc(50% + " + half + "px);} }");
        				writer.write("@media only print {.btn-floating.halfway-fab.center {left: calc(50% + " + half + "px);} }");
        			}
        		}
        	}
        	
        	for (Map.Entry<String, ExtraColor> entry : CompleteTheme.Page.ExtraColors.entrySet()) {
        		ExtraColor col = entry.getValue();
        		if (col.Intensity.equals("")) {
        			writer.write("." + col.Name + " {background-color: " + ABMaterial.hex2Rgba(col.HexValue, col.Opacity) + " !important;}");
        			writer.write("." + col.Name + "-text {color: " + ABMaterial.hex2Rgba(col.HexValue, col.Opacity) + " !important;}");
        		} else {
        			writer.write("." + col.Name + "." + col.Intensity + " {background-color: " + ABMaterial.hex2Rgba(col.HexValue, col.Opacity) + " !important;}");
        			writer.write("." + col.Name + "-text.text-" + col.Intensity + " {color: " + ABMaterial.hex2Rgba(col.HexValue, col.Opacity) + " !important;}");
        		}
        	}
        	
        	if (NeedsFlexWall) {
        		writer.write(".flexwall-images .flexwallitem { float: left;}");
        	}
        	
        	if (NeedsTabs) {
        		writer.write("@media screen and (max-width: " + SML600 + "px) {.tabhide {font-size: 0;}.tabhide i:before {margin-left: 10px}}");
        		writer.write("@media only print {.tabhide {font-size: 0;}.tabhide i:before {margin-left: 10px}}");
        		for (Map.Entry<String, ThemeTabs> entry : CompleteTheme.Tabs.entrySet()) {
        			WriteCSSTabs(writer, entry.getValue());
        		}
        		ThemeTabs deftabs = new ThemeTabs();
        		deftabs.Colorize(CompleteTheme.MainColor);
        		WriteCSSTabs(writer, deftabs);
        	}
        	
        	if (NeedsComposer) {
        		for (Map.Entry<String, ThemeComposer> entry : CompleteTheme.Composers.entrySet()) {
        			if (!entry.getValue().Blocks.containsKey("default")) {
        				entry.getValue().AddDefaultBlock();
        			}
        			WriteCSSComposer(writer, entry.getValue());
        		}
        		ThemeComposer defcomp = new ThemeComposer();
        		defcomp.AddDefaultBlock();
        		defcomp.Colorize(CompleteTheme.MainColor);
        		WriteCSSComposer(writer, defcomp);
        	}
        	
        	if (NeedsFileManager) {
        		for (Map.Entry<String, ThemeFileManager> entry : CompleteTheme.FileManagers.entrySet()) {
        			WriteCSSFileManager(writer, entry.getValue());
        		}
        		ThemeFileManager deffilem = new ThemeFileManager();
        		deffilem.Colorize(CompleteTheme.MainColor);
        		WriteCSSFileManager(writer, deffilem);
        	}
        	
        	writer.write(".reportrow {display: table-row; width: auto; clear: both;}");
        	writer.write(".reportcol {display: table-column; float: left;}");
        	
        	writer.write(".zab {}");
        	writer.write(".zabcell {min-height: 38px;border: silver dashed 2px;;margin-bottom: 2px !important;margin-top: 2px !important;}");
        	writer.write(".zabcell.zabactive {border: #03a9f4 dashed 4px}");
        	writer.write(".zabrow {outline: silver solid 1px; outline-offset: 1px;margin-bottom: 4px !important;margin-top: 4px !important;margin-right: -32px !important; padding-right: 32px !important;min-height: 38px}");
        	writer.write(".zabrow.zabactive {outline: #03a9f4 solid 4px;}");
        	writer.write(".zabrow.zabactive.zabcellactive {outline: silver solid 4px;}");
        	
        	if (NeedsTable) {
        		writer.write(".dz-preview {");
        		writer.write("	display: none;");
        		writer.write("}");
        		writer.write(".abmwatermark {");
        		writer.write("	position: absolute;");
        		writer.write("	color: #9e9e9e;");
        		writer.write("	opacity: 0.25;");
        		writer.write("	width: 100%;");
        		writer.write("	top: 45%;");
        		writer.write("	text-align: center;");
        		writer.write("	z-index: 0;");
        		writer.write("	cursor: pointer;");
        		writer.write("	pointer-events: none;");
        		writer.write("	user-select: none;");
        		writer.write("}");
        		writer.write(".flow-textbig{font-weight:400}@media only screen and (min-width:360px){.flow-textbig{font-size:2.4rem}}@media only screen and (min-width:390px){.flow-textbig{font-size:2.448rem}}@media only screen and (min-width:420px){.flow-textbig{font-size:2.496rem}}@media only screen and (min-width:450px){.flow-textbig{font-size:2.544rem}}@media only screen and (min-width:480px){.flow-textbig{font-size:2.592rem}}@media only screen and (min-width:510px){.flow-textbig{font-size:2.64rem}}@media only screen and (min-width:540px){.flow-textbig{font-size:2.688rem}}@media only screen and (min-width:570px){.flow-textbig{font-size:2.736rem}}@media only screen and (min-width:600px){.flow-textbig{font-size:2.784rem}}@media only screen and (min-width:630px){.flow-textbig{font-size:2.832rem}}@media only screen and (min-width:660px){.flow-textbig{font-size:2.88rem}}@media only screen and (min-width:690px){.flow-textbig{font-size:2.928rem}}@media only screen and (min-width:720px){.flow-textbig{font-size:2.976rem}}@media only screen and (min-width:750px){.flow-textbig{font-size:3.024rem}}@media only screen and (min-width:780px){.flow-textbig{font-size:3.072rem}}@media only screen and (min-width:810px){.flow-textbig{font-size:3.12rem}}@media only screen and (min-width:840px){.flow-textbig{font-size:3.168rem}}@media only screen and (min-width:870px){.flow-textbig{font-size:3.216rem}}@media only screen and (min-width:900px){.flow-textbig{font-size:3.264rem}}@media only screen and (min-width:930px){.flow-textbig{font-size:3.312rem}}@media only screen and (min-width:960px){.flow-textbig{font-size:3.36rem}}@media only screen and (max-width:360px){.flow-textbig{font-size:2.4rem}}");;
        	}
        	
        	if (NeedsChat) {
        		writer.write("section.bubbles div {word-wrap: break-word;margin-bottom: 10px;line-height: 20px;}");
        		writer.write(".bubbles-clear {clear: both;}");
        		writer.write(".bubbles-from-me {position: relative;padding: 10px 20px;border-radius: 25px;float: right;margin-right:10px}");
        		writer.write(".bubbles-from-me p {margin-top: 0px;margin-bottom: 0px;}");
        		writer.write("@-webkit-keyframes blink {0% {opacity: .2;} 20% {opacity: 1;} 100% {opacity: .2;}}");
        		writer.write("@keyframes blink {0% {opacity: .2;} 20% {opacity: 1;} 100% {opacity: .2;}}");
        		writer.write(".bubbles-from-me:before {content: \"\";position: absolute;z-index: -1;bottom: -2px;right: -7px;height: 20px;border-bottom-left-radius: 16px 14px;-webkit-transform: translate(0, -2px);-moz-transform: translate(3px, -2px) rotate(16deg);-ms-transform: translate(0, -2px);-o-transform: translate(0, -2px);}"); //transform: translate(3px, -2px) rotate(16deg);}");
        		writer.write(".bubbles-from-me:after {content: \"\";position: absolute;z-index: -1;bottom: -2px;right: -56px;width: 26px;height: 25px;border-bottom-left-radius: 10px;-webkit-transform: translate(-30px, -2px);-moz-transform: translate(-30px, -2px);-ms-transform: translate(-30px, -2px);-o-transform: translate(-30px, -2px);transform: translate(-30px, -2px);}");
        		writer.write(".bubbles-from-them {position: relative;padding: 10px 20px;border-radius: 25px;float: left;margin-left:10px}");
        		writer.write(".bubbles-from-them p {margin-top: 0px;margin-bottom: 0px;}");
        		writer.write(".bubbles-from-them span {-webkit-animation-name: blink;animation-name: blink;-webkit-animation-duration: 1.4s;animation-duration: 1.4s;-webkit-animation-iteration-count: infinite;animation-iteration-count: infinite;-webkit-animation-fill-mode: both;animation-fill-mode: both;font-weight: bold}");
        		writer.write(".bubbles-from-them span:nth-child(2) {-webkit-animation-delay: .2s;animation-delay: .2s;}");
        		writer.write(".bubbles-from-them span:nth-child(3) {-webkit-animation-delay: .4s;animation-delay: .4s;}");
        		writer.write(".bubbles-from-them:before {content: \"\";position: absolute;z-index: -1;bottom: -2px;left: -6px;height: 20px;border-bottom-right-radius: 16px 14px;-webkit-transform: translate(0, -2px);-moz-transform: translate(-3px, -2px) rotate(-16deg);-ms-transform: translate(0, -2px);-o-transform: translate(0, -2px);}"); //transform: translate(-3px, -2px) rotate(-16deg);}");
        		writer.write(".bubbles-from-them:after {content: \"\";position: absolute;z-index: -1;bottom: -2px;left: 4px;width: 26px;height: 25px;border-bottom-right-radius: 10px;-webkit-transform: translate(-30px, -2px);-moz-transform: translate(-30px, -2px);-ms-transform: translate(-30px, -2px);-o-transform: translate(-30px, -2px);transform: translate(-30px, -2px);}");
        		
        		for (Map.Entry<String, ThemeChat> entry : CompleteTheme.Chats.entrySet()) {
        			WriteCSSChat(writer, entry.getValue());
        		}
        		ThemeChat defconv = new ThemeChat();
        		defconv.Colorize(CompleteTheme.MainColor);
        		WriteCSSChat(writer, defconv);
        	}
        	if (NeedsPlanner) {
        		writer.write(".abmplannerwrapper {width: 100%;-webkit-touch-callout: none;-webkit-user-select: none;-khtml-user-select: none;-moz-user-select: none;-ms-user-select: none;user-select: none;}");
        		writer.write(".abmplannerscroller {position: relative;overflow: auto;}"); //width: 1100px;
        		writer.write(".abmplanner {position: absolute;height:100%;}"); //;width:1100px
        		writer.write(".abmplannerday {width: 8.333%;float: left;}");
        		writer.write(".abmplannerdaynw {width: 12.5%;float: left;}");
        		writer.write(".abmplannerday.abmactive {width: 50%;}");
        		writer.write(".abmplannerdaynw.abmactivenw {width: 50%;}");
        		writer.write(".abmplannerhour {overflow: hidden;box-sizing: border-box;-moz-box-sizing: border-box;-webkit-box-sizing: border-box;margin-bottom: 5px;}");	
        		writer.write(".abmhr {width:100%;overflow: hidden;}");		
        		writer.write(".abmhc {box-sizing: border-box;-moz-box-sizing: border-box;-webkit-box-sizing: border-box;float: left;text-align: center;font-size: 12px;cursor: pointer;}");
        		writer.write(".abmhc3 {width:33.333%;}");		
        		writer.write(".abmhc2 {width:50%;}");
        		writer.write(".abmhc1 {width: 100%;}");
        		writer.write(".abmhour {position: relative;text-align: center;margin-bottom: 5px;font-weight: bold;padding-top: 2px;cursor: pointer;}");		
        		writer.write(".abmhour.abmactive {height:43px;font-size: 22px;padding-top: 6px;margin-top: -5px;padding-bottom: 5px;}");		
        		writer.write(".abmhour.abmactivenw {height:43px;font-size: 22px;padding-top: 6px;margin-top: -5px;padding-bottom: 5px;}");
        		writer.write(".abmwho {text-align: center;font-size: 12px;margin-top: 6px;margin-bottom: 6px;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;}");
        		writer.write(".abmhourcenter {width: 98%;padding-left: 1%;padding-bottom: 5px;}");
        		writer.write(".abmwho.abmhidden {display: none;}");	
        		writer.write(".abmdaywrapperouter {width: 100%;margin: 0px;}");
        		writer.write(".abmdaywrapper {padding-top:5px;box-sizing: border-box;-moz-box-sizing: border-box;-webkit-box-sizing: border-box;}");
        		writer.write(".abmday {position: relative;float: left;text-align: center;margin-bottom: 5px;font-weight: bold;padding-top: 2px;width:8.333%;cursor: pointer;font-size: 18px}");
        		writer.write(".abmdaynw {width:12.5%}");
        		writer.write(".abmday.abmactive {width:50%;font-size: 24px;padding-top: 0px;margin-top: -5px;margin-bottom: 5px}");
        		writer.write(".abmday.abmactivenw {width:50%;font-size: 24px;padding-top: 0px;margin-top: -5px;margin-bottom: 5px}");
        		writer.write(".abmfirsthour {padding-top: 0px;margin-top: 5px;}");
        		
        		writer.write(".plannernamenu-container {position:absolute;width:300px;height:300px;margin:0 auto;display: none;z-index: 5}");
        		writer.write(".plannernamenu-item {width:50px;height:50px;position:absolute;background:#000000;margin:50%;text-align:center;color:#ffffff;font-size:20px;cursor:pointer;}");
        		writer.write(".plannerccpmenu-container {position:absolute;width:300px;height:300px;margin:0 auto;display: none;z-index: 5}");
        		writer.write(".plannerccpmenu-item {width:50px;height:50px;position:absolute;background:#000000;margin:50%;text-align:center;color:#ffffff;font-size:20px;cursor:pointer;}");
        		
        		for (Map.Entry<String, ThemePlanner> entry : CompleteTheme.Planners.entrySet()) {
        			WriteCSSPlanner(writer, entry.getValue());
        		}
        		ThemePlanner defplan = new ThemePlanner();
        		defplan.Colorize(CompleteTheme.MainColor);
        		WriteCSSPlanner(writer, defplan);
        	}
        	if (NeedsSmartWizard) {
        		for (Map.Entry<String, ThemeSmartWizard> entry : CompleteTheme.SmartWizards.entrySet()) {
        			WriteCSSSmartWizard(writer, entry.getValue());
        		}
        		ThemeSmartWizard defwiz = new ThemeSmartWizard();
        		defwiz.Colorize(CompleteTheme.MainColor);
        		WriteCSSSmartWizard(writer, defwiz);
        	}
        	if (NeedsPercentSlider) {
        		writer.write(".dgradio-sb, .dgradio-sb .dg-label, .dgradio-sb .dg-bg {border-radius: 5px;margin: 0;padding: 0;border: 0;-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;}");
        		writer.write(".dgradio-sb {position: relative;display: block;clear: both;font-size: 1em;height: 3.25em;margin-top: 2em;margin-bottom: 1em;}");
        		writer.write("@media only screen and (max-width: " + ABMaterial.ThresholdPxConsiderdMedium + "px) {.dgradio-sb .dg-label {font-size: 0.875em !important;}}");
        		writer.write("@media only screen and (max-width: " + ABMaterial.ThresholdPxConsiderdSmall + "px) {.dgradio-sb .dg-label {font-size: 0.8125em !important;}}");
        		writer.write("@media only print {.dgradio-sb .dg-label {font-size: 0.875em !important;}}");
        		writer.write(".dgradio-db .dg-label {position: absolute;word-wrap: nowrap !important;}");
        		writer.write(".dgradio-sb .dg-item {-webkit-appearance: none;-moz-appearance: none;appearance: none;display: none !important;}");
        		writer.write(".dgradio-sb .dg-label, .dgradio-sb .dg-label .dg-bg {position: absolute;top: 0;bottom: 0;left: 0;right: auto;height: 100% !important;font-size: 0.875em;line-height: 1 !important;text-align: right;padding-left: 0 !important}");
        		writer.write(".dgradio-sb .dg-label {cursor: pointer;padding: 1.125em 0.5em;transform-style: preserve-3d;letter-spacing: 1px;}");
        		writer.write("@media only screen and (max-width: " + ABMaterial.ThresholdPxConsiderdMedium + "px) {.dgradio-sb .dg-label {letter-spacing: normal; padding: 1.125emem 0.5em 1.125em 0;}}");
        		writer.write("@media only print {.dgradio-sb .dg-label {letter-spacing: normal; padding: 1.125emem 0.5em 1.125em 0;}}");
        		for (int i=1;i<21;i++) {
        			writer.write(".dgradio-sb .dg-label.dg-" + (i*5) + " {width: " + (i*5) + "%; z-index: " + (21-i) + ";}");
        		}
        		writer.write(".dgradio-sb .dg-label:before {content: '';position: absolute; width: 0; height: 0; top: -12px !important; bottom: 0; left: auto; right: -4px; border-left: 6px solid transparent !important;border-right: 6px solid transparent !important; border-bottom: 0px solid transparent !important}");
        		writer.write(".dgradio-sb .dg-label:after {content: attr(data-caption); position: absolute; letter-spacing: 1px;bottom: calc(100% + 1em);right: 0;opacity: 0;transition: opacity 0.5s ease; max-width: 100%;margin-top:-25px;width:100%;padding-right:10px;word-wrap: nowrap !important;}");
        		writer.write("@media only screen and (max-width: " + ABMaterial.ThresholdPxConsiderdMedium + "px) {.dgradio-sb .dg-label:after {letter-spacing: normal;}}");
        		writer.write("@media only print {.dgradio-sb .dg-label:after {letter-spacing: normal;}}");
        		writer.write(".dgradio-sb .dg-label:before, .dgradio-sb .dg-label:hover:before {transition: border 0.5s ease;}");
        		writer.write(".dgradio-sb .dg-label:hover:before {-webkit-animation: dg-bounce  1s infinite;-moz-animation: dg-bounce  1s infinite;-o-animation: dg-bounce  1s infinite;animation: dg-bounce  1s infinite;}");
        		writer.write(".dgradio-sb .dg-label:hover:after {opacity: 1;transform: scale(1);	border: 0px solid transparent;background-color: transparent;}");
        		writer.write(".dgradio-sb .dg-label .dg-bg {width: 0;z-index: -1;}");
        		writer.write(".dgradio-sb .dg-item + .dg-label, .dgradio-sb .dg-item + .dg-label .dg-bg {transition-duration: 0.5s , 0.5s;transition-timing-function: cubic-bezier(0.17, 0.67, 0.5, 0.96);}");
        		writer.write(".dgradio-sb .dg-item + .dg-label {transition-property: color;}");
        		writer.write(".dgradio-sb .dg-item + .dg-label .dg-bg {transition-property: width, opacity;width: 100%;opacity: 1;}");
        		writer.write(".dgradio-sb .dg-item:checked ~ :not(:checked) + .dg-label, .dgradio-sb .dg-item:checked ~ :not(:checked) + .dg-label .dg-bg {transition-duration: 0.8s, 0.5s;transition-timing-function: cubic-bezier(0.55, 0, 0.63, 0.91);}");
        		writer.write(".dgradio-sb .dg-item:checked ~ :not(:checked) + .dg-label {transition-property: color;}");
        		writer.write(".dgradio-sb .dg-item:checked ~ :not(:checked) + .dg-label:hover {transition: color 0.4s ease;}");
        		writer.write(".dgradio-sb .dg-item:checked ~ :not(:checked) + .dg-label .dg-bg {transition-property: width, opacity; width: 0%; opacity: 0;}");
        		writer.write(".dgradio-sb .dg-item:checked + .dg-label:after {opacity: 1;border: 0px solid transparent;background-color: transparent}");
        		writer.write(".dgradio-sb:hover .dg-label:not(:hover):after {opacity: 0;}");
        		writer.write("@-webkit-keyframes $name { 0% { -webkit-transform: translateY(0);} 25% {-webkit-transform: translateY(-3px); } 50% {-webkit-transform: translateY(0); } 100% { -webkit-transform: translateY(0);}}");
        		writer.write("@-moz-keyframes $name { 0% {-moz-transform: translateY(0);} 25% {-moz-transform: translateY(-3px);} 50% { -moz-transform: translateY(0);} 100% {-moz-transform: translateY(0);}}");
        		writer.write("@-o-keyframes $name { 0% {-o-transform: translateY(0);} 25% {-o-transform: translateY(-3px); } 50% {-o-transform: translateY(0);}  100% {-o-transform: translateY(0);}}");
        		writer.write("@-ms-keyframes $name { 0% {-ms-transform: translateY(0);} 25% {-ms-transform: translateY(-3px);} 50% {-ms-transform: translateY(0);} 100% {-ms-transform: translateY(0);}}");
        		writer.write("@keyframes $name { 0% {transform: translateY(0); } 25% {transform: translateY(-3px);} 50% {transform: translateY(0); } 100% {transform: translateY(0);}}");
        		
        		for (Map.Entry<String, ThemePercentSlider> entry : CompleteTheme.PercentSliders.entrySet()) {
        			WriteCSSPercentSlider(writer, entry.getValue());
        		}
        		ThemePercentSlider defpslider = new ThemePercentSlider();
        		defpslider.Colorize(CompleteTheme.MainColor);
        		WriteCSSPercentSlider(writer, defpslider);
        	}
        	
        	if (NeedsChronologyList) {
        		for (Map.Entry<String, ThemeChronologyList> entry : CompleteTheme.ChronologyLists.entrySet()) {
        			WriteCSSChronoList(writer, entry.getValue());
        		}
        		ThemeChronologyList defchr = new ThemeChronologyList();
        		defchr.Colorize(CompleteTheme.MainColor);
        		WriteCSSChronoList(writer, defchr);
        	}
        	if (NeedsLists) {
        		for (Map.Entry<String, ThemeList> entry : CompleteTheme.Lists.entrySet()) {
        			WriteCSSLists(writer, entry.getValue());
        		}
        		ThemeList deflist = new ThemeList();
        		deflist.Colorize(CompleteTheme.MainColor);
        		WriteCSSLists(writer, deflist);
        	}
        	
        	writer.write(".abmclose-icon {border: 0px solid transparent;background-color: transparent;display: inline-block;vertical-align: middle;outline: 0;cursor: pointer;padding: 0px;}");        	
        	writer.write(".abmcloseinp-icon {border: 0px solid transparent;background-color: transparent;display: inline-block;vertical-align: middle;outline: 0;cursor: pointer;padding: 0px;}");
        	writer.write(".abmcloseinp::-ms-clear {  display: none; width : 0; height: 0; }");
            writer.write(".abmclose-icon:after {width: 19px;height: 19px;position: absolute;z-index: 1;top: 0;bottom: 0;margin: auto;padding: 2px;text-align: center;cursor: pointer;margin-left: 15px}");
            writer.write(".abmcloseinp-icon:after {width: 19px;height: 19px;position: absolute;z-index: 1;bottom: 0;margin: auto;padding: 2px;text-align: center;cursor: pointer;margin-left: -25px}");
        	       	
        	
        	
        	for (Map.Entry<String, ThemeLabel> entry : CompleteTheme.Labels.entrySet()) {
        		ThemeLabel t = entry.getValue();
    			writer.write(".blockquote" + t.ThemeName.toLowerCase() + " {margin: 20px 0;padding-left: 1.5rem;border-left: " + t.BlockquoteWidthPx + "px solid " + ABMaterial.GetColorStrMap(t.BlockquoteColor,t.BlockquoteColorIntensity) + "; }");
    			writer.write(".blockquote" + t.ThemeName.toLowerCase() + ".abmrtl {margin: 20px 0;padding-right: 1.5rem;border-right: " + t.BlockquoteWidthPx + "px solid " + ABMaterial.GetColorStrMap(t.BlockquoteColor,t.BlockquoteColorIntensity) + ";padding-left: 0rem;border-left: 0px solid transparent;}");
    			writer.write(".lblstrikethrough" + t.ThemeName.toLowerCase() + ".strikethrough {position: relative;}.lblstrikethrough" + t.ThemeName.toLowerCase() + ".strikethrough:before {position: absolute;content: \"\";left: 0;top: 48%;right: 0;border-top: 3px solid " + ABMaterial.GetColorStrMapRGBA(t.StrikethroughColor, t.StrikethroughColorIntensity,0.5) + ";}");
    			if (t.HasReset) {
    				if (!t.ResetAlignRight.equals("")) {
    					writer.write(".abmclose-icon" + t.ThemeName.toLowerCase() + ":after {content: url('" + t.ResetIconUrl + "');right: " + t.ResetAlignRight + "}");
    				} else {
    					writer.write(".abmclose-icon" + t.ThemeName.toLowerCase() + ":after {content: url('" + t.ResetIconUrl + "')}");
    				}
    			}
    		}
        	ThemeLabel deflabel = new ThemeLabel();
    		deflabel.Colorize(CompleteTheme.MainColor);
    		writer.write(".blockquote" + deflabel.ThemeName.toLowerCase() + " {margin: 20px 0;padding-left: 1.5rem;border-left: " + deflabel.BlockquoteWidthPx + "px solid " + ABMaterial.GetColorStrMap(deflabel.BlockquoteColor,deflabel.BlockquoteColorIntensity) + "; }");
        	writer.write(".blockquote" + deflabel.ThemeName.toLowerCase() + ".abmrtl {margin: 20px 0;padding-right: 1.5rem;border-right: " + deflabel.BlockquoteWidthPx + "px solid " + ABMaterial.GetColorStrMap(deflabel.BlockquoteColor,deflabel.BlockquoteColorIntensity) + ";padding-left: 0rem;border-left: 0px solid transparent;}");
        	writer.write(".lblstrikethrough" + deflabel.ThemeName.toLowerCase() + ".strikethrough {position: relative;}.lblstrikethrough" + deflabel.ThemeName.toLowerCase() + ".strikethrough:before {position: absolute;content: \"\";left: 0;top: 48%;right: 0;border-top: 3px solid " + ABMaterial.GetColorStrMapRGBA(deflabel.StrikethroughColor, deflabel.StrikethroughColorIntensity,0.5) + ";}");
        	if (deflabel.HasReset) {
        		if (!deflabel.ResetAlignRight.equals("")) {
					writer.write(".abmclose-icon" + deflabel.ThemeName.toLowerCase() + ":after {content: url('" + deflabel.ResetIconUrl + "');right: " + deflabel.ResetAlignRight + "}");
				} else {
					writer.write(".abmclose-icon" + deflabel.ThemeName.toLowerCase() + ":after {content: url('" + deflabel.ResetIconUrl + "')}");
				}        		
			}
        	
        	writer.write(".buttonnohover:hover {background-color: transparent !important}");
        	
        	writer.write(".fabTI:hover .fabTI-buttons{opacity:1;visibility:visible}.fabTI:hover .fabTI-buttons__link{transform:scaleY(1) scaleX(1) translateY(-16px) translateX(0px)}.fabTI-action-button:hover + .fabTI-buttons .fabTI-buttons__link:before{visibility:visible;opacity:1;transform:scale(1);transform-origin:right center 0;transition-delay:.3s}.fabTI-buttons{position:absolute;left:0;right:0;bottom:50px;list-style:none;margin:0;padding:0;opacity:0;visibility:hidden;transition:.2s}.fabTI-buttons__item{display:block;text-align:center;margin:12px 0}.fabTI-buttons__link{display:inline-block;width:40px;height:40px;text-decoration:none;background-color:#fff;border-radius:50%;box-shadow:0 2px 2px 0 rgba(0,0,0,0.14),0 1px 5px 0 rgba(0,0,0,0.12),0 3px 1px -2px rgba(0,0,0,0.2);transform:scaleY(0.5) scaleX(0.5) translateY(0px) translateX(0px);-moz-transition:.3s;-webkit-transition:.3s;-o-transition:.3s;transition:.3s}[data-tooltip]:before{top:50%;margin-top:-11px;font-weight:600;border-radius:2px;background:#585858;color:#fff;content:attr(data-tooltip);font-size:12px;text-decoration:none;visibility:hidden;opacity:0;padding:4px 7px;margin-right:12px;position:absolute;transform:scale(0);right:100%;white-space:nowrap;transform-origin:top right;transition:all .3s cubic-bezier(.25,.8,.25,1)}[data-tooltip]:hover:before{visibility:visible;opacity:1;transform:scale(1);transform-origin:right center 0}");
        	
        	if (NeedsImageSlider) {
        		for (Map.Entry<String, ThemeImageSlider> entry : CompleteTheme.ImageSliders.entrySet()) {
        			WriteCSSImageSlider(writer, entry.getValue());
        		}
        		ThemeImageSlider defimgslider = new ThemeImageSlider();
        		defimgslider.Colorize(CompleteTheme.MainColor);
        		WriteCSSImageSlider(writer, defimgslider);
        	}
        	if (NeedsSlider) {
        		
        	}
        	if (NeedsPatternLock) {
        		for (Map.Entry<String, ThemePatternLock> entry : CompleteTheme.PatternLocks.entrySet()) {
        			WriteCSSPatternLock(writer, entry.getValue());
        		}
        		ThemePatternLock defplock = new ThemePatternLock();
        		defplock.Colorize(CompleteTheme.MainColor);
        		WriteCSSPatternLock(writer, defplock);
        	}
        	if (NeedsCheckbox) {
        		for (Map.Entry<String, ThemeCheckbox> entry : CompleteTheme.Checkboxes.entrySet()) {
        			WriteCSSCheckbox(writer, entry.getValue());
        		}
        		ThemeCheckbox defcheckbox = new ThemeCheckbox();
        		defcheckbox.Colorize(CompleteTheme.MainColor);
        		WriteCSSCheckbox(writer, defcheckbox);
        	}
        	
        	writer.write(".repicon {margin-top: 4px;margin-bottom: 0px;width: 16px;margin-left: -16px;position: absolute;cursor: pointer}");
        	writer.write(".repicon-i {color: #bdbdbd;margin-left: -10px;margin-top: -10px;margin-bottom: 10px;margin-right: 10px}");
        	
        	if (NeedsInput) { // 18/09/18
	        	writer.write("textarea.materialize-textarea {padding-top: 0.8rem !important}");
	    		for (Map.Entry<String, ThemeInput> entry : CompleteTheme.Inputs.entrySet()) {
	    			ThemeInput in = entry.getValue();
	    			
	    			if (in.HasReset) {
	    				writer.write(".abmcloseinp-icon" + in.ThemeName.toLowerCase() + ":after {content: url('" + in.ResetIconUrl + "');top: " + in.ResetTop + "}");
	    			}
					    			
	    			writer.write(".input-field" + in.ThemeName.toLowerCase() + " label {color:" + ABMaterial.GetColorStrMap(in.ForeColor,in.ForeColorIntensity) + ";width:100%;}");			  
	    			writer.write(".input-field" + in.ThemeName.toLowerCase() + " .prefix.active {color: " + ABMaterial.GetColorStrMap(in.FocusForeColor,in.FocusForeColorIntensity) + ";}");
	    			writer.write(".input-field" + in.ThemeName.toLowerCase() + " {border-radius: " + in.BorderRadiusPx + "px;}");
	    			writer.write(".input-field" + in.ThemeName.toLowerCase() + ".active {background-color: " + ABMaterial.GetColorStrMap(in.FocusBackColor,in.FocusBackColorIntensity) + " !important;}");  
	    			writer.write(".input-field" + in.ThemeName.toLowerCase() + " {" + in.ExtraStyle + "}");
	    			writer.write(".input-field" + in.ThemeName.toLowerCase() + ".active {background-color: " + ABMaterial.GetColorStrMap(in.FocusBackColor,in.FocusBackColorIntensity) + " !important;}");
	    			if (in.ForINPUT_DATE) {
	    				WriteCSSInput(writer, in,ABMaterial.INPUT_DATE);
	    			}
	    			if (in.ForINPUT_DATETIMELOCAL) {
	    				WriteCSSInput(writer, in,ABMaterial.INPUT_DATETIMELOCAL);
	    			}
	    			if (in.ForINPUT_EMAIL) {
	    				WriteCSSInput(writer, in,ABMaterial.INPUT_EMAIL);
	    			}
	    			if (in.ForINPUT_NUMBER) {
	    				WriteCSSInput(writer, in,ABMaterial.INPUT_NUMBER);
	    			}
	    			if (in.ForINPUT_PASSWORD) {
	    				WriteCSSInput(writer, in,ABMaterial.INPUT_PASSWORD);
	    			}
	    			if (in.ForINPUT_SEARCH) {
	    				WriteCSSInput(writer, in,ABMaterial.INPUT_SEARCH);
	    				writer.write(".input-field" + in.ThemeName.toLowerCase() + " input[type="+ ABMaterial.INPUT_SEARCH +"] {display: inline-block; line-height: normal; padding-left: 0px; width: 100%;}");
	    			}
	    			if (in.ForINPUT_TEL) {
	    				WriteCSSInput(writer, in,ABMaterial.INPUT_TEL);
	    			}
	    			if (in.ForINPUT_TEXT) {
	    				WriteCSSInput(writer, in,ABMaterial.INPUT_TEXT);
	    			}
	    			if (in.ForINPUT_TIME) {
	    				WriteCSSInput(writer, in,ABMaterial.INPUT_TIME);
	    			}
	    			if (in.ForINPUT_URL) {
	    				WriteCSSInput(writer, in,ABMaterial.INPUT_URL);
	    			}
	    			if (NeedsFileInput) {
	    				WriteCSSInput(writer, in,"file");
	    			}
	    			WriteCSSInputExtra(writer, in);
	    			if (NeedsTextArea) {
	    				WriteCSSInputArea(writer, in);
	    			}
	    		}
    		
    			ThemeInput definput = new ThemeInput();
	    		definput.Colorize(CompleteTheme.MainColor);
	    		
	    		if (definput.HasReset) {
	    			writer.write(".abmcloseinp-icon" + definput.ThemeName.toLowerCase() + ":after {content: url('" + definput.ResetIconUrl + "');top: " + definput.ResetTop + "}");
	    		}
	    		
	    		writer.write(".input-field" + definput.ThemeName.toLowerCase() + " label {color:" + ABMaterial.GetColorStrMap(definput.ForeColor,definput.ForeColorIntensity) + ";width:100%;}");			  
				writer.write(".input-field" + definput.ThemeName.toLowerCase() + " .prefix.active {color: " + ABMaterial.GetColorStrMap(definput.FocusForeColor,definput.FocusForeColorIntensity) + ";}");
				writer.write(".input-field" + definput.ThemeName.toLowerCase() + " {border-radius: " + definput.BorderRadiusPx + "px;}");
				writer.write(".input-field" + definput.ThemeName.toLowerCase() + ".active {background-color: " + ABMaterial.GetColorStrMap(definput.FocusBackColor,definput.FocusBackColorIntensity) + " !important;}"); 
				if (!definput.ExtraStyle.equals("")) {
					writer.write(".input-field" + definput.ThemeName.toLowerCase() + " {" + definput.ExtraStyle + "}");
				}
				writer.write(".input-field" + definput.ThemeName.toLowerCase() + ".active {background-color: " + ABMaterial.GetColorStrMap(definput.FocusBackColor,definput.FocusBackColorIntensity) + " !important;}");
	    		WriteCSSInput(writer, definput,ABMaterial.INPUT_DATE);
				WriteCSSInput(writer, definput,ABMaterial.INPUT_DATETIMELOCAL);
				WriteCSSInput(writer, definput,ABMaterial.INPUT_EMAIL);
				WriteCSSInput(writer, definput,ABMaterial.INPUT_NUMBER);
				WriteCSSInput(writer, definput,ABMaterial.INPUT_PASSWORD);
				WriteCSSInput(writer, definput,ABMaterial.INPUT_SEARCH);
				writer.write(".input-field" + definput.ThemeName.toLowerCase() + " input[type="+ ABMaterial.INPUT_SEARCH +"] {display: inline-block; line-height: normal; padding-left: 0px; width: 100%;}");
				WriteCSSInput(writer, definput,ABMaterial.INPUT_TEL);
				WriteCSSInput(writer, definput,ABMaterial.INPUT_TEXT);
				WriteCSSInput(writer, definput,ABMaterial.INPUT_TIME);
				WriteCSSInput(writer, definput,ABMaterial.INPUT_URL);
				if (NeedsFileInput) {
					WriteCSSInput(writer, definput,"file");
				}
				WriteCSSInputExtra(writer, definput);
	    		if (NeedsTextArea) {
	    			ThemeInput definputarea = new ThemeInput();
	        		definputarea.Colorize(CompleteTheme.MainColor);
	        		WriteCSSInputArea(writer, definputarea);
	        	}
    		}
        	
        	if (NeedsSlider) {
        		for (Map.Entry<String, ThemeSlider> entry : CompleteTheme.Sliders.entrySet()) {
        			WriteCSSSlider(writer, entry.getValue());
        		}
        		ThemeSlider defslider = new ThemeSlider();
        		defslider.Colorize(CompleteTheme.MainColor);
        		WriteCSSSlider(writer, defslider);
        	}
        	if (NeedsEditor) {
        		for (Map.Entry<String, ThemeEditor> entry : CompleteTheme.Editors.entrySet()) {
        			ThemeEditor ed = entry.getValue();
        			writer.write(".menu" + ed.ThemeName.toLowerCase() + " .group .item:hover, .menu" + ed.ThemeName.toLowerCase() + " .item:first-child:hover {border-bottom: 3px " + ABMaterial.GetColorStrMap(ed.ActiveMenuColor,  ed.ActiveMenuColorIntensity) + " solid;}");
        		}
        		ThemeEditor defeditor = new ThemeEditor();
        		defeditor.Colorize(CompleteTheme.MainColor);
        		writer.write(".menu" + defeditor.ThemeName.toLowerCase() + " .group .item:hover, .menu" + defeditor.ThemeName.toLowerCase() + " .item:first-child:hover {border-bottom: 3px " + ABMaterial.GetColorStrMap(defeditor.ActiveMenuColor,  defeditor.ActiveMenuColorIntensity) + " solid;}");
        	}
        	if (NeedsRange) {
        		for (Map.Entry<String, ThemeRange> entry : CompleteTheme.Ranges.entrySet()) {
        			WriteCSSRange(writer, entry.getValue());
        		}
        		ThemeRange defrange = new ThemeRange();
        		defrange.Colorize(CompleteTheme.MainColor);
        		WriteCSSRange(writer, defrange);
        	}
        	writer.write("::-webkit-input-placeholder {color:" + ABMaterial.GetColorStrMap(Theme.PlaceHolderColor,Theme.PlaceHolderColorIntensity) + ";}");
        	writer.write(":-moz-placeholder {color:" + ABMaterial.GetColorStrMap(Theme.PlaceHolderColor,Theme.PlaceHolderColorIntensity) + ";}");
        	writer.write("::-moz-placeholder {color:" + ABMaterial.GetColorStrMap(Theme.PlaceHolderColor,Theme.PlaceHolderColorIntensity) + ";}");
        	writer.write(":-ms-input-placeholder {color:" + ABMaterial.GetColorStrMap(Theme.PlaceHolderColor,Theme.PlaceHolderColorIntensity) + ";}");
        	
        	writer.write(".card-panel span, .card-content p {-webkit-font-smoothing:antialiased;}");
        	writer.write("#images .card-panel .row {margin-bottom:0;}");
        	
        	if (NeedsCodeLabel) {
        		writer.write("pre .str, code .str { color: #65B042; }");
        		writer.write("pre .kwd, code .kwd { color: #E28964; }");
        		writer.write("pre .com, code .com { color: #AEAEAE; font-style: italic; }");
        		writer.write("pre .typ, code .typ { color: #89bdff; }");
        		writer.write("pre .lit, code .lit { color: #3387CC; }");
        		writer.write("pre .pun, code .pun { color: #fff; }");
        		writer.write("pre .pln, code .pln { color: #fff; }");
        		writer.write("pre .tag, code .tag { color: #89bdff; }");
        		writer.write("pre .atn, code .atn { color: #bdb76b; }");
        		writer.write("pre .atv, code .atv { color: #65B042; }");
        		writer.write("pre .dec, code .dec { color: #3387CC; }");
        		writer.write("pre.prettyprint, code.prettyprint {background-color: #000;-moz-border-radius: 8px;-webkit-border-radius: 8px;-o-border-radius: 8px;-ms-border-radius: 8px;-khtml-border-radius: 8px;border-radius: 8px;}");
        		writer.write("pre.prettyprint {width: 95%;margin: 1em auto;padding: 1em;white-space: pre-wrap;}");
        		writer.write("ol.linenums { margin-top: 0; margin-bottom: 0; color: #AEAEAE; }");
        		writer.write("li.L0,li.L1,li.L2,li.L3,li.L5,li.L6,li.L7,li.L8 { list-style-type: none }");
        		writer.write("li.L1,li.L3,li.L5,li.L7,li.L9 { }");
        		writer.write("@media print {");
        		writer.write("pre .str, code .str { color: #060; }");
        		writer.write("pre .kwd, code .kwd { color: #006; font-weight: bold; }");
        		writer.write("pre .com, code .com { color: #600; font-style: italic; }");
        		writer.write("pre .typ, code .typ { color: #404; font-weight: bold; }");
        		writer.write("pre .lit, code .lit { color: #044; }");
        		writer.write("pre .pun, code .pun { color: #440; }");
        		writer.write("pre .pln, code .pln { color: #000; }");
        		writer.write("pre .tag, code .tag { color: #006; font-weight: bold; }");
        		writer.write("pre .atn, code .atn { color: #404; }");
        		writer.write("pre .atv, code .atv { color: #060; }");
        		writer.write("}");
        		writer.write("pre.prettyprint, code.prettyprint {font-size:12px;}");
        		writer.write("code .str,pre .str{color:#65B042}code .kwd,pre .kwd{color:#E28964}code .com,pre .com{color:#AEAEAE;font-style:italic}code .typ,pre .typ{color:#89bdff}code .lit,pre .lit{color:#3387CC}code .pln,code .pun,pre .pln,pre .pun{color:#fff}code .tag,pre .tag{color:#89bdff}code .atn,pre .atn{color:#bdb76b}code .atv,pre .atv{color:#65B042}code .dec,pre .dec{color:#3387CC}code.prettyprint,pre.prettyprint{background-color:#000;border-radius:8px}pre.prettyprint{width:95%;margin:1em auto;padding:1em;white-space:pre-wrap}ol.linenums{margin-top:0;margin-bottom:0;color:#AEAEAE}li.L0,li.L1,li.L2,li.L3,li.L5,li.L6,li.L7,li.L8{list-style-type:none}@media print{code .str,pre .str{color:#060}code .kwd,pre .kwd{color:#006;font-weight:700}code .com,pre .com{color:#600;font-style:italic}code .typ,pre .typ{color:#404;font-weight:700}code .lit,pre .lit{color:#044}code .pun,pre .pun{color:#440}code .pln,pre .pln{color:#000}code .tag,pre .tag{color:#006;font-weight:700}code .atn,pre .atn{color:#404}code .atv,pre .atv{color:#060}}");
        	}
        	writer.write("thead {border-bottom:0px solid transparent;}");
        	if (NeedsSwitch) {
        		writer.write("input[type=checkbox]:checked:not(:disabled) ~ .lever:active:after {box-shadow: 0 1px 3px 1px rgba(0, 0, 0, 0.4), 0 0 0 15px rgba(38, 166, 154, 0.1); }");
        		writer.write("input[type=checkbox]:not(:disabled) ~ .lever:active:after {box-shadow: 0 1px 3px 1px rgba(0, 0, 0, 0.4), 0 0 0 15px rgba(0, 0, 0, 0.08); }");
        		for (Map.Entry<String, ThemeSwitch> entry : CompleteTheme.Switches.entrySet()) {
        			WriteCSSSwitch(writer, entry.getValue());
        		}
        		ThemeSwitch defswitch = new ThemeSwitch();
        		defswitch.Colorize(CompleteTheme.MainColor);
        		WriteCSSSwitch(writer, defswitch);
        	}
        	if (NeedsRadio) {
        		for (Map.Entry<String, ThemeRadioGroup> entry : CompleteTheme.RadioGroups.entrySet()) {
        			WriteCSSRadioGroup(writer, entry.getValue());
        		}
        		ThemeRadioGroup defrg = new ThemeRadioGroup();
        		defrg.Colorize(CompleteTheme.MainColor);
        		WriteCSSRadioGroup(writer, defrg);
        	}
        	if (NeedsCombo) {
        		for (Map.Entry<String, ThemeCombo> entry : CompleteTheme.Combos.entrySet()) {
        			WriteCSSCombo(writer, entry.getValue());
        		}
        		ThemeCombo defcombo = new ThemeCombo();
        		defcombo.Colorize(CompleteTheme.MainColor);
        		WriteCSSCombo(writer, defcombo);
        	}
        	if (NeedsBadge) {
        		for (Map.Entry<String, ThemeBadge> entry : CompleteTheme.Badges.entrySet()) {
        			WriteCSSBadge(writer, entry.getValue());
        		}
        		ThemeBadge defbadge = new ThemeBadge();
        		defbadge.Colorize(CompleteTheme.MainColor);
        		WriteCSSBadge(writer, defbadge);
        	}
        	for (Map.Entry<String, ThemeButton> entry : CompleteTheme.Buttons.entrySet()) {
    			WriteCSSButton(writer, entry.getValue());
    		}
    		ThemeButton defbut = new ThemeButton();
    		defbut.Colorize(CompleteTheme.MainColor);
    		WriteCSSButton(writer, defbut);
        	if (NeedsDateTimeScroller) {
        		for (Map.Entry<String, ThemeDateTimeScroller> entry : CompleteTheme.DateTimeScrollers.entrySet()) {
        			WriteCSSDTScroller(writer, entry.getValue());
        		}
        		ThemeDateTimeScroller defscroller = new ThemeDateTimeScroller();
        		defscroller.Colorize(CompleteTheme.MainColor);
        		WriteCSSDTScroller(writer, defscroller);
        	}
        	if (NeedsDateTimePicker) {
        		writer.write(".dtp .dtp-buttons {display: flex;justify-content: flex-end;}");
        		for (Map.Entry<String, ThemeDateTimePicker> entry : CompleteTheme.DateTimePickers.entrySet()) {
        			WriteCSSDTPicker(writer, entry.getValue());
        		}
        		ThemeDateTimePicker defpicker = new ThemeDateTimePicker();
        		defpicker.Colorize(CompleteTheme.MainColor);
        		WriteCSSDTPicker(writer, defpicker);
        	}
        	String navigationBarWidthPx="0";
        	if (navigationBar!=null) {
        		navigationBarWidthPx = "" + navigationBar.NBWidthPx;
        	}
        	if (FloatingContainers.size()>0) {
        		writer.write(".floatingpointer {pointer-events: auto !important}");
        	}
        	for (Entry<String, ABMContainer> cont : FloatingContainers.entrySet()) {
        		String floatcont = cont.getValue().ParentString + cont.getValue().ArrayName.toLowerCase() + cont.getValue().ID.toLowerCase();
        		writer.write("#" + floatcont +" {position: fixed;" + cont.getValue().FromTopOrBottom + ": " + cont.getValue().TopOrBottomValue + ";width:100%;padding-right:" + navigationBarWidthPx + "px;pointer-events: none;z-index:998}");
        		writer.write("@media only screen and (max-width: " + SML992 + "px) {#" + floatcont +"  {padding-right:0px;}}");
        		writer.write("@media only print {#" + floatcont +"  {padding-right:0px;}}");
            }
        	
        	writer.write(".containertitledefault {color: " + ABMaterial.GetColorStrMap("grey","darken-2") + "}");
        	for (Map.Entry<String, ThemeContainer> entry : CompleteTheme.Containers.entrySet()) {
        		writer.write(".containertitle" + entry.getValue().ThemeName.toLowerCase() + " {color: " + ABMaterial.GetColorStrMap(entry.getValue().TitleColor,entry.getValue().TitleColorIntensity) + "}");
        	}
        	
        	if (NeedsCanvas || NeedsSignature) {
        		writer.write(".canvastofit {position:relative;left:0;right:0;top:0;bottom:0;margin:auto;width:100%;height:100%;}");
        	}
        	
        	if (NeedsUpload) {
       	 		for (Entry<String,ABMUpload> eup: uploads.entrySet()) {
       	 			ABMUpload up = eup.getValue();
       	 			WriteCSSUpload(writer, up.Theme, up.ParentString + up.ArrayName.toLowerCase() + up.ID.toLowerCase(), URelPath);
       	 		}
       	 	}
        	
        	if (NeedsSortingTable) {
        		writer.write("table.sortable thead {cursor:pointer;}");
        	}
        	
        	writer.write(".abminfclickable {");
        	writer.write("cursor: pointer;");
        	writer.write("}");
        	writer.write(".abminfdrag {");
        	writer.write("cursor: move;");
        	writer.write("}");        	
        	writer.write(".dragtable-sortable {");
        	writer.write("list-style-type: none; margin: 0; padding: 0; -moz-user-select: none;");
        	writer.write("}");
        	writer.write(".dragtable-sortable li {");
        	writer.write("margin: 0; padding: 0; float: left; font-size: 1em; background: white;"); 
        	writer.write("}");
        	writer.write(".dragtable-sortable th, .dragtable-sortable td{");
        	writer.write("border-left: 0px;");
        	writer.write("}");
        	writer.write(".dragtable-sortable li:first-child th, .dragtable-sortable li:first-child td {");
        	writer.write("border-left: 1px solid #CCC;"); 
        	writer.write("}");
        	writer.write(".ui-sortable-helper {");
        	writer.write("opacity: 0.7;filter: alpha(opacity=70);");
        	writer.write("}");
        	writer.write(".ui-sortable-placeholder {"); 
        	writer.write("border-bottom: 1px solid #CCCCCC;");
        	writer.write("border-top: 1px solid #CCCCCC;");
        	writer.write("visibility: visible !important;");
        	writer.write("background: #EFEFEF !important;");
        	writer.write("visibility: visible !important;");
        	writer.write("}");
        	writer.write(".ui-sortable-placeholder * {"); 
        	writer.write("opacity: 0.0; visibility: hidden;"); 
        	writer.write("}");
        	
        	writer.write(".tableinteractive tbody, .tableinteractive tfoot {cursor: pointer;}");
        	for (Map.Entry<String, ThemeTable> entry : CompleteTheme.Tables.entrySet()) {
        		WriteCSSTable(writer, entry.getValue());
        	}
        	ThemeTable deftable = new ThemeTable();
        	deftable.Colorize(CompleteTheme.MainColor);
        	WriteCSSTable(writer, deftable);
        	
        	if (NeedsBackgroundVideo || NeedsBackgroundImage) {
        		if (NeedsBackgroundVideo) {
        			writer.write(".vidbg {display: block; position: fixed; overflow: hidden; right: 0; bottom: 0; min-width: 100%; min-height: 100%; width: auto; height: auto; z-index: -101;  background: url(" + this.BackgroundVideoImage + ") no-repeat; background-size: cover;}");
        		} else {
        			writer.write(".vidbg {display: block; position: fixed; overflow: hidden; right: 0; bottom: 0; min-width: 100%; min-height: 100%; width: auto; height: auto; z-index: -101;  background: url(" + this.BackgroundImage + ") no-repeat; background-size: cover;}");
        		}
        		writer.write(".vidbg video {display: block; position: fixed; right: 0; bottom: 0; min-width: 100%; min-height: 100%; width: auto; height: auto; z-index: -100;}");
        		writer.write(".vidbg .vidbg-overlay {display: block; position: fixed; right: 0; bottom: 0; min-width: 100%; min-height: 100%; width: auto; height: auto; z-index: -99; opacity: 0.6; background: url(" + URelPath + "js/screen.png);}");
        	}
        	if (NeedsChart) {
        		writer.write(".ct-legend{position:absolute;z-index:10;list-style:none;right:10px}.ct-legend li{position:relative;padding-left:23px;margin-bottom:3px;cursor:pointer}.ct-legend li:before{width:18px;height:18px;position:absolute;left:0;content:'';border:2px solid transparent;border-radius:2px}.ct-legend li.inactive:before{background:transparent}.ct-legend.ct-legend-inside{position:absolute;top:0;right:0}");        		
        		writer.write(".ct-zoom-rect {fill: rgba(0, 0, 0, 0.3);stroke: black;}");
        		for (Entry<String,ABMChart> echart: Charts.entrySet()) {
        			ABMChart chart = echart.getValue();
        			WriteCSSChart(writer, chart.Theme, chart.ParentString + chart.ArrayName.toLowerCase() + chart.ID.toLowerCase());
        		}
        	}
        	if (NeedsAudioPlayer) {
        		for (Map.Entry<String, ThemeAudioPlayer> entry : CompleteTheme.AudioPlayers.entrySet()) {
        			WriteCSSAudio(writer, entry.getValue());
        		}
        		ThemeAudioPlayer ap = new ThemeAudioPlayer();
        		ap.Colorize(CompleteTheme.MainColor);
        		WriteCSSAudio(writer, ap);
        	}
        	
        	writer.write("@media only screen and (min-width : " + SML993 + "px) {");
        	writer.write("#toast-container .toast {cursor: pointer;}");
        	writer.write("#toast-container .toast::after {content: \"\\00d7\";-webkit-font-feature-settings: 'liga';font-size: 1.5rem;font-weight: 300;float: right;padding-left: 3rem;}");
        	writer.write("}");
        	for (Map.Entry<String, ThemeToast> entry : CompleteTheme.Toasts.entrySet()) {
    			WriteCSSToast(writer, entry.getValue());
    		}
    		ThemeToast todef = new ThemeToast();
    		todef.Colorize(CompleteTheme.MainColor);
    		WriteCSSToast(writer, todef);
        	
        	if (this.ShowGridInfo) {
        		writer.write(".showcellinfo {font-size: 10px;color: red} ");        		
        	}
        	
        	writer.write(".modalbig {max-height: 90%;width: 80%;} .modal.modal-fixed-footerbig {height: 80%;} @media only screen and (max-width : " + SML992 + "px) {.modalbig {width: 95%;} .modal.modal-fixed-footerbig {height: 95%;}} ");
        	writer.write(".modalbig {max-height: 90%;width: 80%;} .modal.modal-fixed-footerbig {height: 80%;} @media only print {.modalbig {width: 95%;} .modal.modal-fixed-footerbig {height: 95%;}} ");
        	writer.write(".modalxlbig {max-height: 100%;width: 100%;max-width: 100%;height: 100%;} .modal.modal-fixed-footerxlbig {width:100%;height: 100%;max-height:100%;max-width: 100%;} @media only screen and (max-width : " + SML992 + "px) {.modalxlbig {width:100%;height: 100%;max-height:100%;max-width: 100%;} .modal.modal-fixed-footerxlbig {width:100%;height: 100%;max-height:100%;max-width: 100%;}} ");
        	writer.write(".modalxlbig {max-height: 100%;width: 100%;max-width: 100%;height: 100%;} .modal.modal-fixed-footerxlbig {width:100%;height: 100%;max-height:100%;max-width: 100%;} @media only print {.modalxlbig {width:100%;height: 100%;max-height:100%;max-width: 100%;} .modal.modal-fixed-footerxlbig {width:100%;height: 100%;max-height:100%;max-width: 100%;}} ");
        	writer.write(".modalbig .modal-footer .btn, .modalbig .modal-footer .btn-large, .modalbig .modal-footer .btn-flat {float: right; margin: 6px 6px; } ");
        	writer.write(".modalxlbig .modal-footer .btn, .modalxlbig .modal-footer .btn-large, .modalxlbig .modal-footer .btn-flat {float: right; margin: 6px 6px; } ");
        	
        	writer.write(".modal .modal-footer .btn.modalleft, .modal .modal-footer .btn-large.modalleft, .modal .modal-footer .btn-flat.modalleft {float: left !important; margin: 6px 6px; } ");
        	writer.write(".modalbig .modal-footer .btn.modalleft, .modalbig .modal-footer .btn-large.modalleft, .modalbig .modal-footer .btn-flat.modalleft {float: left !important; margin: 6px 6px; } ");
        	writer.write(".modalxlbig .modal-footer .btn.modalleft, .modalxlbig .modal-footer .btn-large.modalleft, .modalxlbig .modal-footer .btn-flat.modalleft {float: left !important; margin: 6px 6px; } ");
        	
        	writer.write(".btn-floating.btn-small { width: 24px; height: 24px; }");
        	writer.write(".btn-floating.btn-small i { line-height: 24px; font-size: 0.9rem;}");	 
        	writer.write(".btn-small {height: 24px; line-height: 24px; }");
        	writer.write(".btn-small i {font-size: 0.9rem; }");
        	
        	if (this.NeedsCalendar) {
        		writer.write(".full-calendar {padding-top: 30px;} ");
        		writer.write("#external-events {padding-top: 50px;} ");
        		writer.write("#external-events .fc-event {color: #fff;text-decoration: none;padding: 5px;margin-bottom: 10px;cursor: all-scroll;border: none;} ");
        		writer.write(".fc button {background: #fff;} ");
        		writer.write(".fc td, .fc th {border-width: 0px !important;} ");
        		writer.write(".fc-today {opacity: 1; border: none;}");
        		writer.write(".fc-time-grid-event.fc-v-event.fc-event {border-radius: 4px;border: none; padding: 5px; opacity: .65; left: 5% !important; right: 5% !important;}");
        		writer.write(".fc td, .fc th {border-style: none !important; border-width: 1px !important; padding: 0 !important; vertical-align: top !important;}");
        		writer.write(".fc-event .fc-bg {z-index: 1 !important; background: inherit !important; opacity: .25 !important;}");
        		writer.write(".fc-ltr .fc-h-event.fc-not-end, .fc-rtl .fc-h-event.fc-not-start {opacity: .65 !important; margin-left: 12px !important;padding: 5px! important;}");
        		writer.write(".fc-day-grid-event {opacity: .65 !important;margin-left: 12px !important;padding: 5px! important;}");
        		writer.write("hr.fc-divider {border-color: transparent;background: transparent;border-width: 0px 0;}");
        		writer.write(".fc-time-grid .fc-slats .fc-minor td {border-color: transparent;}");
        		writer.write(".fc-unthemed .fc-divider, .fc-unthemed .fc-popover, .fc-unthemed .fc-row, .fc-unthemed tbody, .fc-unthemed td, .fc-unthemed th, .fc-unthemed thead {border-color: transparent;}");
        		writer.write(".fc-state-active, .fc-state-down {color: #3498db !important;}");
        		writer.write(".fc-day-number {text-align: center;}");
        		writer.write(".calnoselect {-webkit-touch-callout: none; -webkit-user-select: none; -khtml-user-select: none; -moz-user-select: none; -ms-user-select: none;  user-select: none;}");
        		
        		for (Entry<String,ABMCalendar> ecal: Calendars.entrySet()) {
        			ABMCalendar cal = ecal.getValue();
        			WriteCSSCalendar(writer, cal.ParentString + cal.ArrayName.toLowerCase() + cal.ID.toLowerCase(), cal.Theme);
        		}
        	}
        	
        	if (NeedsTreeTable) {
        		String b64="";
        		for (Entry<String, ThemeTreeTable> ttt: CompleteTheme.TreeTables.entrySet()) {
        			for (Entry<String, TreeIcon> eti: ttt.getValue().TreeIcons.entrySet() ) {
        				TreeIcon ti = eti.getValue();
        				b64 = "<?xml version='1.0' encoding='UTF-8'?><!DOCTYPE svg PUBLIC '-//W3C//DTD SVG 1.1//EN' 'http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd'><svg xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink' version='1.1' width='24' height='24' viewBox='0 0 24 24'><path style='fill: " + ABMaterial.GetColorStrMap(ti.ForeColor,  ti.ForeColorIntensity) + "' d='M12,20C7.59,20 4,16.41 4,12C4,7.59 7.59,4 12,4C16.41,4 20,7.59 20,12C20,16.41 16.41,20 12,20M12,2A10,10 0 0,0 2,12A10,10 0 0,0 12,22A10,10 0 0,0 22,12A10,10 0 0,0 12,2M13,7H11V11H7V13H11V17H13V13H17V11H13V7Z' /></svg>";
        				b64 = Base64.getEncoder().encodeToString(b64.getBytes("utf-8"));
        				writer.write("table.treetable tr.collapsed span.indenter a.ic" + ti.ThemeName.toLowerCase() + " { background-image: url(\"data:image/svg+xml;base64," + b64 + "\");}");
        				b64 = "<?xml version='1.0' encoding='UTF-8'?><!DOCTYPE svg PUBLIC '-//W3C//DTD SVG 1.1//EN' 'http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd'><svg xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink' version='1.1' width='24' height='24' viewBox='0 0 24 24'><path style='fill: " + ABMaterial.GetColorStrMap(ti.ForeColor,  ti.ForeColorIntensity) + "' d='M12,20C7.59,20 4,16.41 4,12C4,7.59 7.59,4 12,4C16.41,4 20,7.59 20,12C20,16.41 16.41,20 12,20M12,2A10,10 0 0,0 2,12A10,10 0 0,0 12,22A10,10 0 0,0 22,12A10,10 0 0,0 12,2M7,13H17V11H7' /></svg>"; 
        				b64 = Base64.getEncoder().encodeToString(b64.getBytes("utf-8"));
        				writer.write("table.treetable tr.expanded span.indenter a.ic" + ti.ThemeName.toLowerCase() + " { background-image: url(\"data:image/svg+xml;base64," + b64 + "\");}");
        				
        			}
        		}
        		b64 = "<?xml version='1.0' encoding='UTF-8'?><!DOCTYPE svg PUBLIC '-//W3C//DTD SVG 1.1//EN' 'http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd'><svg xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink' version='1.1' width='24' height='24' viewBox='0 0 24 24'><path style='fill: #000000' d='M12,20C7.59,20 4,16.41 4,12C4,7.59 7.59,4 12,4C16.41,4 20,7.59 20,12C20,16.41 16.41,20 12,20M12,2A10,10 0 0,0 2,12A10,10 0 0,0 12,22A10,10 0 0,0 22,12A10,10 0 0,0 12,2M13,7H11V11H7V13H11V17H13V13H17V11H13V7Z' /></svg>";
				b64 = Base64.getEncoder().encodeToString(b64.getBytes("utf-8"));
				writer.write("table.treetable tr.collapsed span.indenter a.icdefault { background-image: url(\"data:image/svg+xml;base64," + b64 + "\");}");
				b64 = "<?xml version='1.0' encoding='UTF-8'?><!DOCTYPE svg PUBLIC '-//W3C//DTD SVG 1.1//EN' 'http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd'><svg xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink' version='1.1' width='24' height='24' viewBox='0 0 24 24'><path style='fill: #000000' d='M12,20C7.59,20 4,16.41 4,12C4,7.59 7.59,4 12,4C16.41,4 20,7.59 20,12C20,16.41 16.41,20 12,20M12,2A10,10 0 0,0 2,12A10,10 0 0,0 12,22A10,10 0 0,0 22,12A10,10 0 0,0 12,2M7,13H17V11H7' /></svg>"; 
				b64 = Base64.getEncoder().encodeToString(b64.getBytes("utf-8"));
				writer.write("table.treetable tr.expanded span.indenter a.icdefault { background-image: url(\"data:image/svg+xml;base64," + b64 + "\");}");
				writer.write("table.treetable tr.abmselected {filter: alpha(opacity=90); -moz-opacity: 0.9; opacity: 0.9; }");
        	}
        	
        	writer.write("* { -webkit-tap-highlight-color: transparent;-moz-tap-highlight-color: transparent;}");
        	
        	writer.write(".justify-align {text-align: justify; }");
        	
        	writer.write(".vhcalign-wrapper { display: -webkit-box; display: -moz-box; display: -ms-flexbox; display: -webkit-flex; display: flex; -webkit-flex-align: center; -ms-flex-align: center; -webkit-align-items: center; align-items: center; justify-content: center;-webkit-justify-content: center;} .vhcalign-wrapper .valign { display: block; }");        	
        	writer.write(".vhralign-wrapper { display: -webkit-box; display: -moz-box; display: -ms-flexbox; display: -webkit-flex; display: flex; -webkit-flex-align: center; -ms-flex-align: center; -webkit-align-items: center; align-items: center; justify-content: flex-end;-webkit-justify-content: flex-end;} .vhralign-wrapper .valign { display: block; }");
        	writer.write(".vhjalign-wrapper { display: -webkit-box; display: -moz-box; display: -ms-flexbox; display: -webkit-flex; display: flex; -webkit-flex-align: center; -ms-flex-align: center; -webkit-align-items: center; align-items: center; justify-content: space-around;-webkit-justify-content: space-around;} .vhjalign-wrapper .valign { display: block; }");
        
        	writer.write(".hcalign-wrapper { display: flex; flex-direction: row; flex-wrap: wrap; justify-content: center;-webkit-justify-content: center; align-items: center;}");
        	writer.write(".hralign-wrapper { display: flex; flex-direction: row; flex-wrap: wrap; justify-content: flex-end;-webkit-justify-content: flex-end; align-items: center;}");
        	writer.write(".hjalign-wrapper { display: flex; flex-direction: row; flex-wrap: wrap; justify-content: space-around;-webkit-justify-content: space-around; align-items: center;}");
        	
        	writer.write("@media print{.modal{position:absolute;left:0;top:0;margin:0;padding:0;overflow:visible!important;box-shadow:none;background-color:#fff!important}.lean-overlay{display:none!important}.modal-header {display: none}.ABMrap {outline: none !important}}");
	}
	
	protected void GzipIt(String source) {
		 byte[] buffer = new byte[1024];
		 
		 try{
		    	GZIPOutputStream gzos = 
		    		new GZIPOutputStream(new FileOutputStream(source + ".gz"));
		 
		        FileInputStream in = 
		            new FileInputStream(source);
		 
		        int len;
		        while ((len = in.read(buffer)) > 0) {
		        	gzos.write(buffer, 0, len);
		        }
		 
		        in.close();
		    	
		    	gzos.finish();
		    	gzos.close();
		    	
		    }catch(IOException ex){
		       ex.printStackTrace();   
		    }		 
	}
	
	@Hide
	protected void WriteCSSChart(BufferedWriter writer, ThemeChart in, String id) throws IOException {
		String[] series = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o"};
		String prefix = "";
		ThemeSerie serie = null;
		
		writer.write(".ct-chart" + id + in.ThemeName.toLowerCase() + " .ct-label {fill: " + ABMaterial.GetColorStrMap(in.LabelColor,in.LabelColorIntensity) + " ;color: " + ABMaterial.GetColorStrMap(in.LabelColor,in.LabelColorIntensity) + " ;font-size: " + in.LabelFontSizePx + "px;}");
		
		for (int i=0;i<15;i++) {
			prefix = ".ct-chart" + id + in.ThemeName.toLowerCase() + " .ct-series.ct-series-" + series[i];
			serie = in.Serie(series[i]);
			writer.write(prefix + " .ct-bar, " + prefix + " .ct-line," + prefix + " .ct-point, " + prefix + " .ct-slice-donut {stroke: " + ABMaterial.GetColorStrMap(serie.Color,serie.ColorIntensity) + "} ");
			writer.write(prefix + " .ct-area, " + prefix + " .ct-slice-pie {fill: " + ABMaterial.GetColorStrMap(serie.Color,serie.ColorIntensity) + "} ");
			writer.write(prefix + " .ct-line {stroke-width: " + serie.LineStrokeWidthPx + "px;stroke-linecap: " + serie.LineCap + "} ");
			writer.write(prefix + " .ct-point {stroke-width: " + serie.LinePointStrokeWidthPx + "px;stroke-linecap: " + serie.LinePointCap + "} ");
			writer.write(prefix + " .ct-bar {stroke-width: " + serie.BarStrokeWidthPx + "px;} ");
		}
		
	}
	
	@Hide
	protected void WriteCSSToast(BufferedWriter writer, ThemeToast in) throws IOException {
		writer.write("#toast-container.toast-container" + in.ThemeName.toLowerCase() + " .toast::after {color: " + ABMaterial.GetColorStrMap(in.CloseButtonColor, in.CloseButtonColorIntensity) + " !important;}");
	}
	
	@Hide
	protected void WriteCSSCell(BufferedWriter writer, ThemeCell in) throws IOException {
		if (in.MasonryColumnsSmall>-9999) {
			String grid = "mason" + in.ThemeName.toLowerCase();
			writer.write(".masoncolumn" + in.ThemeName.toLowerCase() + "{" + in.MasonColumnsExtraCSS + "}");
			writer.write("@media screen and (max-width: " + ABMaterial.ThresholdPxConsiderdSmall + "px){." + grid + "[data-columns]::before {content: '" + in.MasonryColumnsSmall + " .masoncolumn.masoncolumn" + in.ThemeName.toLowerCase() + ".size-1of" + in.MasonryColumnsSmall + "';}}");
			writer.write("@media screen and (min-width: " + (ABMaterial.ThresholdPxConsiderdSmall+1) + "px) and (max-width: " + ABMaterial.ThresholdPxConsiderdMedium + "px) {." + grid + "[data-columns]::before {content: '" + in.MasonryColumnsMedium + " .masoncolumn.masoncolumn" + in.ThemeName.toLowerCase() + ".size-1of" + in.MasonryColumnsMedium + "';}}");
			writer.write("@media screen and (min-width: " + (ABMaterial.ThresholdPxConsiderdMedium+1) + "px) and (max-width: " + ABMaterial.ThresholdPxConsiderdLarge + "px) {." + grid + "[data-columns]::before {content: '" + in.MasonryColumnsLarge + " .masoncolumn.masoncolumn" + in.ThemeName.toLowerCase() + ".size-1of" + in.MasonryColumnsLarge + "';}}");
			writer.write("@media screen and (min-width: " + (ABMaterial.ThresholdPxConsiderdLarge+1) + "px) {." + grid + "[data-columns]::before {content: '" + in.MasonryColumnsExtraLarge + " .masoncolumn.masoncolumn" + in.ThemeName.toLowerCase() + ".size-1of" + in.MasonryColumnsExtraLarge + "';}}");
		}		
	}

	@Hide
	protected void WriteCSSMsgbox(BufferedWriter writer, ThemeMsgBox in) throws IOException {
		

	}
	
	@Hide
	protected void WriteCSSSection(BufferedWriter writer, Section in) throws IOException {
		String col = ABMaterial.GetColorStrMap(in.NavigationButtonColor, in.NavigationButtonColorIntensity);
		String colM = ABMaterial.GetColorStrMap(in.NavigationMenuColor, in.NavigationMenuColorIntensity);
		
		writer.write(".abmnextsectionmenu" + in.Name.toLowerCase() + " {color: " + colM + ";}");
		writer.write(".abmnextsectionmenu" + in.Name.toLowerCase() + ":before {background: " + colM + ";}");
		
		switch (in.NavigationButtonType) {
		case "section01": 
			writer.write("." + in.Name.toLowerCase() + "-section01 a.abmnextsectionbutton {padding-top: 60px;color: " + col + ";}");
			writer.write("." + in.Name.toLowerCase() + "-section01 a.abmnextsectionbutton span { position: absolute; top: 0; left: 50%; width: 24px;height: 24px; margin-left: -12px; border-left: 1px solid " + col + "; border-bottom: 1px solid " + col + "; -webkit-transform: rotate(-45deg); transform: rotate(-45deg);	box-sizing: border-box;}");
			writer.write("." + in.Name.toLowerCase() + "-section01 a.abmnextsectionbutton.abmup span {-webkit-transform: rotate(135deg); transform: rotate(135deg);}");
			break;
		case "section02": 
			writer.write("." + in.Name.toLowerCase() + "-section02 a.abmnextsectionbutton {padding-top: 60px;color: " + col + ";}");
			writer.write("." + in.Name.toLowerCase() + "-section02 a.abmnextsectionbutton span {position: absolute;top: 0;left: 50%;width: 46px;height: 46px;	margin-left: -23px;	border: 1px solid " + col + ";	border-radius: 100%;box-sizing: border-box;}");
			writer.write("." + in.Name.toLowerCase() + "-section02 a.abmnextsectionbutton span::after {position: absolute;top: 50%;left: 50%;	content: '';width: 16px;height: 16px;margin: -12px 0 0 -8px; border-left: 1px solid " + col + "; border-bottom: 1px solid " + col + ";-webkit-transform: rotate(-45deg); transform: rotate(-45deg);box-sizing: border-box;}");
			writer.write("." + in.Name.toLowerCase() + "-section02 a.abmnextsectionbutton.abmup span::after {-webkit-transform: rotate(135deg); transform: rotate(135deg)}");
			break;
		case "section04":
			writer.write("." + in.Name.toLowerCase() + "-section04 a.abmnextsectionbutton {padding-top: 60px;color: " + col + ";}");
			writer.write("." + in.Name.toLowerCase() + "-section04 a.abmnextsectionbutton span { position: absolute; top: 0; left: 50%; width: 24px;height: 24px; margin-left: -12px; border-left: 1px solid " + col + "; border-bottom: 1px solid " + col + "; -webkit-transform: rotate(-45deg); transform: rotate(-45deg);-webkit-animation: sdb04 2s infinite;animation: sdb04 2s infinite;box-sizing: border-box;}");
			writer.write("." + in.Name.toLowerCase() + "-section04 a.abmnextsectionbutton.abmup span {top: 25px;-webkit-transform: rotate(135deg); transform: rotate(135deg);-webkit-animation: sdb04u 2s infinite;animation: sdb04u 2s infinite}");
			break;
		}
	}
	
	@Hide
	protected void WriteCSSChat(BufferedWriter writer, ThemeChat in) throws IOException {
		for (Entry<String,ThemeChatBubble> entry: in.Bubbles.entrySet()) {
			ThemeChatBubble inb = entry.getValue();
			writer.write(".bubbles-from-me" + in.ThemeName.toLowerCase() + "-" + inb.ThemeName.toLowerCase() + " {z-index:5;color: " + ABMaterial.GetColorStrMap(inb.TextColor,inb.TextColorIntensity) + ";background: " + ABMaterial.GetColorStrMap(inb.Color,inb.ColorIntensity) + ";}");
			writer.write(".bubbles-from-me" + in.ThemeName.toLowerCase() + "-" + inb.ThemeName.toLowerCase() + ":before {border-right: 20px solid " + ABMaterial.GetColorStrMap(inb.Color,inb.ColorIntensity) + ";color: " + ABMaterial.GetColorStrMap(inb.Color,inb.ColorIntensity) + ";}");
			writer.write(".bubbles-from-me" + in.ThemeName.toLowerCase() + "-" + inb.ThemeName.toLowerCase() + ":after {background: " + ABMaterial.GetColorStrMap(in.BackgroundColor,in.BackgroundColorIntensity) + ";}");
			writer.write(".bubbles-from-meex" + in.ThemeName.toLowerCase() + "-" + inb.ThemeName.toLowerCase() + " {position: relative;color: " + ABMaterial.GetColorStrMap(inb.ExtraTextColor,inb.ExtraTextColorIntensity) + ";font-size: 10px;float: right;margin-top: -8px;margin-bottom: -10px;}");
			
			writer.write(".bubbles-from-them" + in.ThemeName.toLowerCase() + "-" + inb.ThemeName.toLowerCase() + " {z-index:5;color: " + ABMaterial.GetColorStrMap(inb.TextColor,inb.TextColorIntensity) + ";background: " + ABMaterial.GetColorStrMap(inb.Color,inb.ColorIntensity) + ";}");
			writer.write(".bubbles-from-them" + in.ThemeName.toLowerCase() + "-" + inb.ThemeName.toLowerCase() + ":before {border-left: 20px solid " + ABMaterial.GetColorStrMap(inb.Color,inb.ColorIntensity) + ";color: " + ABMaterial.GetColorStrMap(inb.Color,inb.ColorIntensity) + ";}");
			writer.write(".bubbles-from-them" + in.ThemeName.toLowerCase() + "-" + inb.ThemeName.toLowerCase() + ":after {background: " + ABMaterial.GetColorStrMap(in.BackgroundColor,in.BackgroundColorIntensity) + ";}");
			writer.write(".bubbles-from-themex" + in.ThemeName.toLowerCase() + "-" + inb.ThemeName.toLowerCase() + " {position: relative;color: " + ABMaterial.GetColorStrMap(inb.ExtraTextColor,inb.ExtraTextColorIntensity) + ";font-size: 10px;float: left;margin-top: -8px;margin-bottom: -10px;}");
		}
		
	}
	
	@Hide 
	protected void WriteCSSPlanner(BufferedWriter writer, ThemePlanner in) throws IOException {
		writer.write(".abmplannerwrapper" + in.ThemeName.toLowerCase() + " {background: " + ABMaterial.GetColorStrMap(in.BackColor,in.BackColorIntensity) + ";}");
		writer.write(".abmplannerhour" + in.ThemeName.toLowerCase() + "{border: 1px solid " + ABMaterial.GetColorStrMap(in.BorderColor,in.BorderColorIntensity) + ";}");
		writer.write(".abmhr" + in.ThemeName.toLowerCase() + ":last-child {border-bottom: 1px solid " + ABMaterial.GetColorStrMap(in.HourMinutesBorderColor,in.HourMinutesBorderColorIntensity) + ";}");
		writer.write(".abmhc" + in.ThemeName.toLowerCase() + " {color: " + ABMaterial.GetColorStrMap(in.HourMinutesFreeTextColor,in.HourMinutesFreeTextColorIntensity) + ";background: " + ABMaterial.GetColorStrMap(in.HourMinutesFreeColor,in.HourMinutesFreeColorIntensity) + ";border-left: 1px solid " + ABMaterial.GetColorStrMap(in.HourMinutesBorderColor,in.HourMinutesBorderColorIntensity) + ";border-top: 1px solid " + ABMaterial.GetColorStrMap(in.HourMinutesBorderColor,in.HourMinutesBorderColorIntensity) + ";}");
		writer.write(".abmhc" + in.ThemeName.toLowerCase() + ":last-child {border-right: 1px solid " + ABMaterial.GetColorStrMap(in.HourMinutesBorderColor,in.HourMinutesBorderColorIntensity) + ";}");
		writer.write(".abmhc" + in.ThemeName.toLowerCase() + ".abmna {background: " + ABMaterial.GetColorStrMap(in.HourMinutesNotAvailableColor,in.HourMinutesNotAvailableColorIntensity) + ";color: " + ABMaterial.GetColorStrMap(in.HourMinutesNotAvailableTextColor,in.HourMinutesNotAvailableTextColorIntensity) + ";}");
		for (int i=0;i<20;i++) {
			writer.write(".abmhc" + in.ThemeName.toLowerCase() + ".abmocc" + i + " {background: " + ABMaterial.GetColorStrMap(in.HourMinutesUsedColors.get(i),in.HourMinutesUsedColorsIntensity.get(i)) + ";color: " + ABMaterial.GetColorStrMap(in.HourMinutesUsedTextColors.get(i),in.HourMinutesUsedTextColorsIntensity.get(i)) + ";}");		
		}
		writer.write(".abmhour" + in.ThemeName.toLowerCase() + " {color: " + ABMaterial.GetColorStrMap(in.HourTextColor,in.HourTextColorIntensity) + ";background: " + ABMaterial.GetColorStrMap(in.HourColor,in.HourColorIntensity) + ";border-bottom: 1px solid " + ABMaterial.GetColorStrMap(in.HourBottomBorderColor,in.HourBottomBorderColorIntensity) + ";}");
		writer.write(".abmhouralt" + in.ThemeName.toLowerCase() + " {color: " + ABMaterial.GetColorStrMap(in.HourAltTextColor,in.HourAltTextColorIntensity) + ";background: " + ABMaterial.GetColorStrMap(in.HourAltColor,in.HourAltColorIntensity) + ";border-bottom: 1px solid " + ABMaterial.GetColorStrMap(in.HourBottomBorderColor,in.HourBottomBorderColorIntensity) + ";}");
		writer.write(".abmdaywrapper" + in.ThemeName.toLowerCase() + " {background: " + ABMaterial.GetColorStrMap(in.DayColor,in.DayColorIntensity) + ";border: 1px solid " + ABMaterial.GetColorStrMap(in.DayBorderColor,in.DayBorderColorIntensity) + ";}");	
		writer.write(".abmday" + in.ThemeName.toLowerCase() + " {color: " + ABMaterial.GetColorStrMap(in.DayTextColor,in.DayTextColorIntensity) + ";background: " + ABMaterial.GetColorStrMap(in.DayColor,in.DayColorIntensity) + "}");
		writer.write(".abmdayalt" + in.ThemeName.toLowerCase() + " {color: " + ABMaterial.GetColorStrMap(in.DayAltTextColor,in.DayAltTextColorIntensity) + ";background: " + ABMaterial.GetColorStrMap(in.DayColor,in.DayColorIntensity) + "}");
		writer.write(".plannernamenuNA" + in.ThemeName.toLowerCase() + " {background: " + ABMaterial.GetColorStrMap(in.HourMinutesNotAvailableColor,in.HourMinutesNotAvailableColorIntensity) + ";color: " + ABMaterial.GetColorStrMap(in.HourMinutesNotAvailableTextColor,in.HourMinutesNotAvailableTextColorIntensity) + ";}");
		writer.write(".plannernamenu-item" + in.ThemeName.toLowerCase() + " {background: " + ABMaterial.GetColorStrMap(in.MenuColor,in.MenuColorIntensity) + ";color: " + ABMaterial.GetColorStrMap(in.MenuTextColor,in.MenuTextColorIntensity) + ";}");
		writer.write(".plannerccpmenu-item" + in.ThemeName.toLowerCase() + " {background: " + ABMaterial.GetColorStrMap(in.MenuColor,in.MenuColorIntensity) + ";color: " + ABMaterial.GetColorStrMap(in.MenuTextColor,in.MenuTextColorIntensity) + ";}");
	}
	
	@Hide 
	protected void WriteCSSPercentSlider(BufferedWriter writer, ThemePercentSlider in) throws IOException {
		writer.write(".dgradio-sb" + in.ThemeName.toLowerCase() + " {background: " + ABMaterial.GetColorStrMap(in.BackColor, in.BackColorIntensity) + ";border: 2px solid " + ABMaterial.GetColorStrMap(in.BorderColor, in.BorderIntensity) + ";}");
		writer.write(".dgradio-sb" + in.ThemeName.toLowerCase() + " .dg-label {color: " + ABMaterial.GetColorStrMap(in.ActiveLabelColor, in.ActiveLabelColorIntensity) + ";}");
		for (int i=0;i<20;i++) {
			writer.write(".dgradio-sb" + in.ThemeName.toLowerCase() + " .dg-label:nth-of-type(" + (i+1) + ") .dg-bg {background: " + ABMaterial.GetColorStrMap(in.BlockColors.get(i), in.BlockColorsIntensity.get(i)) + ";}");			
		}		
		writer.write(".dgradio-sb" + in.ThemeName.toLowerCase() + " .dg-label:before {border-top: 6px solid " + ABMaterial.GetColorStrMap(in.UncheckedArrowColor, in.UncheckedArrowColorIntensity) + " !important;}");
		writer.write(".dgradio-sb" + in.ThemeName.toLowerCase() + " .dg-label:after {color: " + ABMaterial.GetColorStrMap(in.IndicatorLabelColor, in.IndicatorLabelColorIntensity) + ";}");
		writer.write(".dgradio-sb" + in.ThemeName.toLowerCase() + " .dg-label:hover:before {border-top-color: " + ABMaterial.GetColorStrMap(in.CheckedArrowColor, in.CheckedArrowColorIntensity) + ";}");
		writer.write(".dgradio-sb" + in.ThemeName.toLowerCase() + " .dg-label .dg-bg {border: 2px solid " + ABMaterial.GetColorStrMap(in.BackColor, in.BackColorIntensity) + ";}");
		writer.write(".dgradio-sb" + in.ThemeName.toLowerCase() + " .dg-item + .dg-label {position: absolute;color: " + ABMaterial.GetColorStrMap(in.ActiveLabelColor, in.ActiveLabelColorIntensity) + ";}");
		writer.write(".dgradio-sb" + in.ThemeName.toLowerCase() + " .dg-item:checked ~ :not(:checked) + .dg-label {color: " + ABMaterial.GetColorStrMap(in.InactiveLabelColor, in.InactiveLabelColorIntensity) + ";}");
		writer.write(".dgradio-sb" + in.ThemeName.toLowerCase() + " .dg-item:checked ~ :not(:checked) + .dg-label:hover {color: " + ABMaterial.GetColorStrMap(in.InactiveLabelColor, in.InactiveLabelColorIntensity) + ";}");
		writer.write(".dgradio-sb" + in.ThemeName.toLowerCase() + " .dg-item:checked + .dg-label:before {border-top-color: " + ABMaterial.GetColorStrMap(in.InactiveLabelColor, in.InactiveLabelColorIntensity) + ";}");
	}
	
	@Hide 
	protected void WriteCSSSmartWizard(BufferedWriter writer, ThemeSmartWizard in) throws IOException {
		writer.write(".ws-main" + in.ThemeName.toLowerCase() + " {background-color: " + ABMaterial.GetColorStrMap(in.BackColor, in.BackColorIntensity) + " !important}");
		writer.write(".step-anchor" + in.ThemeName.toLowerCase() + " {background-color: " + ABMaterial.GetColorStrMap(in.NavigationBackColor, in.NavigationBackColorIntensity) + " !important}");
		writer.write(".sw-theme-arrows > ul.step-anchor > li.disabled" + in.ThemeName.toLowerCase() + " > a { border-color: #5bc0de !important; color: " + ABMaterial.GetColorStrMap(in.StateDisabledForeColor, in.StateDisabledForeColorIntensity) + " !important; background: " + ABMaterial.GetColorStrMap(in.StateDisabledBackColor, in.StateDisabledBackColorIntensity) + " !important;}");
		writer.write(".sw-theme-arrows > ul.step-anchor > li.disabled" + in.ThemeName.toLowerCase() + " > a:after {border-left: 30px solid " + ABMaterial.GetColorStrMap(in.StateDisabledBackColor, in.StateDisabledBackColorIntensity) + " !important;}");
		writer.write(".sw-theme-arrows > ul.step-anchor > li.active" + in.ThemeName.toLowerCase() + " > a {border-color: #5bc0de !important; color: " + ABMaterial.GetColorStrMap(in.StateActiveForeColor, in.StateActiveForeColorIntensity) + " !important; background: " + ABMaterial.GetColorStrMap(in.StateActiveBackColor, in.StateActiveBackColorIntensity) + " !important;}");
		writer.write(".sw-theme-arrows > ul.step-anchor > li.active" + in.ThemeName.toLowerCase() + " > a:after {border-left: 30px solid " + ABMaterial.GetColorStrMap(in.StateActiveBackColor, in.StateActiveBackColorIntensity) + " !important;}");
		writer.write(".sw-theme-arrows > ul.step-anchor > li.done" + in.ThemeName.toLowerCase() + " > a {border-color: #5cb85c !important; color: " + ABMaterial.GetColorStrMap(in.StateDoneForeColor, in.StateDoneForeColorIntensity) + " !important; background: " + ABMaterial.GetColorStrMap(in.StateDoneBackColor, in.StateDoneBackColorIntensity) + " !important;}");
		writer.write(".sw-theme-arrows > ul.step-anchor > li.done" + in.ThemeName.toLowerCase() + " > a:after {border-left: 30px solid " + ABMaterial.GetColorStrMap(in.StateDoneBackColor, in.StateDoneBackColorIntensity) + ";}");
		writer.write(".sw-theme-arrows > ul.step-anchor > li.danger" + in.ThemeName.toLowerCase() + " > a { border-color: #aa0000 !important; color: " + ABMaterial.GetColorStrMap(in.StateErrorForeColor, in.StateErrorForeColorIntensity) + " !important; background: " + ABMaterial.GetColorStrMap(in.StateErrorBackColor, in.StateErrorBackColorIntensity) + " !important;}");
		writer.write(".sw-theme-arrows > ul.step-anchor > li.danger" + in.ThemeName.toLowerCase() + "> a:after {border-left: 30px solid " + ABMaterial.GetColorStrMap(in.StateErrorBackColor, in.StateErrorBackColorIntensity) + ";}");
		writer.write(".sw-theme-arrows > ul.step-anchor > li.disabled" + in.ThemeName.toLowerCase() + " > span {color: " + ABMaterial.GetColorStrMap(in.StateDisabledForeColor, in.StateDisabledForeColorIntensity) + " !important; background: " + ABMaterial.GetColorStrMap(in.StateDisabledBackColor, in.StateDisabledBackColorIntensity) + " !important;}");
		writer.write(".sw-theme-arrows > ul.step-anchor > li.active" + in.ThemeName.toLowerCase() + " > span {color: " + ABMaterial.GetColorStrMap(in.StateActiveForeColor, in.StateActiveForeColorIntensity) + " !important; background: " + ABMaterial.GetColorStrMap(in.StateActiveBackColor, in.StateActiveBackColorIntensity) + " !important;}");		
		writer.write(".sw-theme-arrows > ul.step-anchor > li.done" + in.ThemeName.toLowerCase() + " > span {color: " + ABMaterial.GetColorStrMap(in.StateDoneForeColor, in.StateDoneForeColorIntensity) + " !important; background: " + ABMaterial.GetColorStrMap(in.StateDoneBackColor, in.StateDoneBackColorIntensity) + " !important;}");		
		writer.write(".sw-theme-arrows > ul.step-anchor > li.danger" + in.ThemeName.toLowerCase() + " > span {color: " + ABMaterial.GetColorStrMap(in.StateErrorForeColor, in.StateErrorForeColorIntensity) + " !important; background: " + ABMaterial.GetColorStrMap(in.StateErrorBackColor, in.StateErrorBackColorIntensity) + " !important;}");		
		writer.write(".sw-theme-arrows > ul.step-anchor > li.loading" + in.ThemeName.toLowerCase() + ":before {border-left-color: " + ABMaterial.GetColorStrMap(in.LoadingColor, in.LoadingColorIntensity) + " !important;border-right-color: " + ABMaterial.GetColorStrMap(in.LoadingColor, in.LoadingColorIntensity) + " !important;}");
		if (in.IsResponsive) {
			writer.write("@media screen and (max-width: " + in.ResponsiveBehaviourThresholdPx + "px) {.sw-theme-arrows" + in.ThemeName.toLowerCase() + " > ul.step-anchor{border: 0 !important;background: #ddd !important;} .sw-theme-arrows" + in.ThemeName.toLowerCase() + " > .nav-tabs > li { float: none !important; margin-bottom: 0 !important; } .sw-theme-arrows" + in.ThemeName.toLowerCase() + " > ul.step-anchor > li > a, .sw-theme-arrows" + in.ThemeName.toLowerCase() + " > ul.step-anchor > li > a:hover { padding-left: 15px !important; margin-right: 0 !important; margin-bottom: 1px !important; } .sw-theme-arrows" + in.ThemeName.toLowerCase() + " > ul.step-anchor > li > a:after, .sw-theme-arrows" + in.ThemeName.toLowerCase() + " > ul.step-anchor > li > a:before { display: none !important; }}");
		}
	}
	
	@Hide 
	protected void WriteCSSPatternLock(BufferedWriter writer, ThemePatternLock in) throws IOException {
		writer.write(".patt-holder" + in.ThemeName.toLowerCase() + "{background:" + ABMaterial.GetColorStrMap(in.BackColor,in.BackColorIntensity) + ";  -ms-touch-action: none;}");
		writer.write(".patt-wrap" + in.ThemeName.toLowerCase() + "{position:relative; cursor:pointer;}");
		writer.write(".patt-wrap" + in.ThemeName.toLowerCase() + " ul, .patt-wrap" + in.ThemeName.toLowerCase() + " li{list-style: none;margin:0;padding: 0;}");
		writer.write(".patt-circ" + in.ThemeName.toLowerCase() + "{position:relative;float: left;box-sizing: border-box;-moz-box-sizing: border-box;}");
		writer.write(".patt-circ" + in.ThemeName.toLowerCase() + ".hovered{border:3px solid " + ABMaterial.GetColorStrMap(in.CorrectColor,in.CorrectColorIntensity) + ";}");
		writer.write(".patt-error" + in.ThemeName.toLowerCase() + " .patt-circ" + in.ThemeName.toLowerCase() + ".hovered{border:3px solid " + ABMaterial.GetColorStrMap(in.WrongColor,in.WrongColorIntensity) + ";}");
		writer.write(".patt-hidden" + in.ThemeName.toLowerCase() + " .patt-circ" + in.ThemeName.toLowerCase() + ".hovered{border:0;}");
		writer.write(".patt-dots" + in.ThemeName.toLowerCase() + "{background: " + ABMaterial.GetColorStrMap(in.DotColor,in.DotColorIntensity) + ";width: 10px;height: 10px;border-radius:5px;position:absolute;top:50%;left:50%;margin-top:-5px;margin-left:-5px;}");
		writer.write(".patt-lines" + in.ThemeName.toLowerCase() + "{border-radius:5px;height:10px;background:" + ABMaterial.GetColorStrMapRGBA(in.LineColor, in.LineColorIntensity, 0.7) + ";position:absolute;transform-origin:5px 5px;-ms-transform-origin:5px 5px;-webkit-transform-origin:5px 5px;}");
		writer.write(".patt-hidden" + in.ThemeName.toLowerCase() + " .patt-lines" + in.ThemeName.toLowerCase() + "{display:none;}");
	}	
	
	@Hide
	protected void WriteCSSTabs(BufferedWriter writer, ThemeTabs in) throws IOException {
		writer.write(".tabs" + in.ThemeName.toLowerCase() + " {background-color:" + ABMaterial.GetColorStrMap(in.BackColor,in.BackColorIntensity) + ";}");
		writer.write(".tabs" + in.ThemeName.toLowerCase() + " .tab a {color:" + ABMaterial.GetColorStrMap(in.ForeColor,in.ForeColorIntensity) + ";}");
		writer.write(".tabs" + in.ThemeName.toLowerCase() + " .tab a:hover {color: " + ABMaterial.GetColorStrMap(in.HoverForeColor,in.HoverForeColorIntensity) + "; }");
		writer.write(".tabs" + in.ThemeName.toLowerCase() + " .tab.disabled a {color: " + ABMaterial.GetColorStrMap(in.DisabledForeColor,in.DisabledForeColorIntensity) + ";}");
		writer.write(".tabs" + in.ThemeName.toLowerCase() + " .indicator {background-color: " + ABMaterial.GetColorStrMap(in.IndicatorColor,in.IndicatorColorIntensity) + ";}");
		writer.write(".tabs" + in.ThemeName.toLowerCase() + " li:hover, .tabs" + in.ThemeName.toLowerCase() + " li.active {background-color: transparent;}");
	}
	
	@Hide
	protected void WriteCSSComposer(BufferedWriter writer, ThemeComposer in) throws IOException {
		writer.write(".abmcpr" + in.ThemeName.toLowerCase() + " {background-color: " + ABMaterial.GetColorStrMap(in.BackgroundColor,in.BackgroundColorIntensity) + ";border-radius: " + in.BorderRadius + ";border: " + in.BorderWidth + " " + in.BorderStyle + " " + ABMaterial.GetColorStrMap(in.BorderColor,in.BorderColorIntensity) + ";}");
		for (Entry<String,ThemeComposerBlock> entry: in.Blocks.entrySet()) {
			ThemeComposerBlock in2 = entry.getValue();
			writer.write(".abmcpr-sb" + in2.ThemeName.toLowerCase() + ":before {background-color: " + ABMaterial.GetColorStrMap(in2.SidebarColor,in2.SidebarColorIntensity) + ";}");
			writer.write(".abmcpr-lbl" + in2.ThemeName.toLowerCase() + ":after {color: " + ABMaterial.GetColorStrMap(in2.LabelColor,in2.LabelColorIntensity) + ";}");
			writer.write(".abmcpr-arr" + in2.ThemeName.toLowerCase() + " {border: solid " + ABMaterial.GetColorStrMap(in2.ButtonColor,in2.ButtonColorIntensity) + ";	border-width: 0 3px 3px 0;}");
			writer.write(".abmcpr-btn" + in2.ThemeName.toLowerCase() + " {color: " + ABMaterial.GetColorStrMap(in2.ButtonColor,in2.ButtonColorIntensity) + ";}");
			writer.write(".abmcpr-bg" + in2.ThemeName.toLowerCase() + " {background-color: " + ABMaterial.GetColorStrMap(in2.SidebarBulletColor,in2.SidebarBulletColorIntensity) + "; border: 0.25em solid " + ABMaterial.GetColorStrMap(in2.SidebarBulletBorderColor,in2.SidebarBulletBorderColorIntensity) + ";}");
			writer.write(".abmcpr-flds" + in2.ThemeName.toLowerCase() + " {background-color: " + ABMaterial.GetColorStrMap(in2.BackgroundColor,in2.BackgroundColorIntensity) + ";border-radius: " + in2.BorderRadius + ";border: " + in2.BorderWidth + " " + in2.BorderStyle + " " + ABMaterial.GetColorStrMap(in2.BorderColor,in2.BorderColorIntensity) + ";}");
		}
	}
	
	@Hide
	protected void WriteCSSFileManager(BufferedWriter writer, ThemeFileManager in) throws IOException {
		writer.write(".abmfilemanager" + in.ThemeName.toLowerCase() + " {background-color: " + ABMaterial.GetColorStrMap(in.BackgroundColor,in.BackgroundColorIntensity) + ";}");
		writer.write(".abmfileheader" + in.ThemeName.toLowerCase() + " > .row, .abmfilefooter" + in.ThemeName.toLowerCase() + " > .row {background-color: " + ABMaterial.GetColorStrMap(in.HeaderFooterBackgroundColor,in.HeaderFooterBackgroundColorIntensity) + ";}");
		writer.write(".abmfilebreadcrumb" + in.ThemeName.toLowerCase() + ", .abmfilebreadcrumbsep" + in.ThemeName.toLowerCase() + " {color: " + ABMaterial.GetColorStrMap(in.HeaderFooterForeColor,in.HeaderFooterForeColorIntensity) + " !important;}");
		writer.write(".abmfilesidebar" + in.ThemeName.toLowerCase() + " {background-color: " + ABMaterial.GetColorStrMap(in.SideBarBackgroundColor, in.SideBarBackgroundColorIntensity) + ";border-right: 1px solid " + ABMaterial.GetColorStrMap(in.SideBarBorderColor,in.SideBarBorderColorIntensity) + ";}");
		writer.write(".abmfilefolder" + in.ThemeName.toLowerCase() + " > a, .abmfilefolder" + in.ThemeName.toLowerCase() + " > a > span {color: " + ABMaterial.GetColorStrMap(in.SideBarForeColor,in.SideBarForeColorIntensity) + " !important;}");
		writer.write(".abmfilefolder" + in.ThemeName.toLowerCase() + ".active, .abmfilefolder" + in.ThemeName.toLowerCase() + ".active > a, .abmfilefolder" + in.ThemeName.toLowerCase() + ".active > a > span {color: " + ABMaterial.GetColorStrMap(in.SideBarActiveForeColor,in.SideBarActiveForeColorIntensity) + " !important;}");
		writer.write(".abmfilebig" + in.ThemeName.toLowerCase() + ".active {border: 2px solid " + ABMaterial.GetColorStrMap(in.FileActiveForeColor,in.FileActiveForeColorIntensity) + " !important;}");
		writer.write(".abmfilebig" + in.ThemeName.toLowerCase() + ".active.move {border: 2px dashed " + ABMaterial.GetColorStrMap(in.FileActiveForeColor,in.FileActiveForeColorIntensity) + " !important;}");
		writer.write(".abmfilebig" + in.ThemeName.toLowerCase() + ".active.copy {background-color: " + ABMaterial.GetColorStrMap(in.FileActiveCopyBackgroundColor,in.FileActiveCopyBackgroundColorIntensity) + ";}");
		writer.write(".abmfilebig" + in.ThemeName.toLowerCase() + " {color: " + ABMaterial.GetColorStrMap(in.FileActiveForeColor,in.FileActiveForeColorIntensity) + " !important;}");
		writer.write(".abmfileinput" + in.ThemeName.toLowerCase() + ":-moz-placeholder {color: " + ABMaterial.GetColorStrMap(in.SearchPlaceholderColor,in.SearchPlaceholderColorIntensity) + ";}");
		writer.write(".abmfileinput" + in.ThemeName.toLowerCase() + "::-webkit-input-placeholder {color: " + ABMaterial.GetColorStrMap(in.SearchPlaceholderColor,in.SearchPlaceholderColorIntensity) + ";}");
		writer.write(".abmfileinput" + in.ThemeName.toLowerCase() + "[type=search] {color: " + ABMaterial.GetColorStrMap(in.SearchForeColor,in.SearchForeColorIntensity) + ";border-bottom: 1px solid " + ABMaterial.GetColorStrMap(in.SearchForeColor,in.SearchForeColorIntensity) + " !important;}");
		writer.write(".abmfilelistheader" + in.ThemeName.toLowerCase() + " {border-bottom: " + ABMaterial.GetColorStrMap(in.ListHeaderBorderColor,in.ListHeaderBorderColorIntensity) + ";}");
		writer.write(".abmfilelistheader" + in.ThemeName.toLowerCase() + " > .col > a > span {color: " + ABMaterial.GetColorStrMap(in.ListHeaderForeColor,in.ListHeaderForeColorIntensity) + " !important;}");
		writer.write(".abmfilelistitem" + in.ThemeName.toLowerCase() + " {border-bottom: 1px solid rgb(238,238,238);color: " + ABMaterial.GetColorStrMap(in.ListItemDefaultForeColor,in.ListItemDefaultForeColorIntensity) + ";}");
		writer.write(".abmfilelistitem-file" + in.ThemeName.toLowerCase() + " {color: " + ABMaterial.GetColorStrMap(in.FileActiveForeColor,in.FileActiveForeColorIntensity) + " !important;}");
		writer.write(".abmfilelistitem" + in.ThemeName.toLowerCase() + ".active {border: 2px solid " + ABMaterial.GetColorStrMap(in.FileActiveForeColor,in.FileActiveForeColorIntensity) + " !important;}");
		writer.write(".abmfilelistitem" + in.ThemeName.toLowerCase() + ".active.move {border: 2px dashed " + ABMaterial.GetColorStrMap(in.FileActiveForeColor,in.FileActiveForeColorIntensity) + " !important;}");
		writer.write(".abmfilelistitem" + in.ThemeName.toLowerCase() + ".active.copy {background-color: " + ABMaterial.GetColorStrMap(in.FileActiveCopyBackgroundColor,in.FileActiveCopyBackgroundColorIntensity) + ";}");
		writer.write(".abmfileinfo" + in.ThemeName.toLowerCase() + " > span {color: " + ABMaterial.GetColorStrMap(in.HeaderFooterForeColor,in.HeaderFooterForeColorIntensity) + ";}");
		writer.write(".abmfiletooltip" + in.ThemeName.toLowerCase() + " .abmfiletooltiptext" + in.ThemeName.toLowerCase() + " {background-color: " + ABMaterial.GetColorStrMap(in.ToolTipBackgroundColor,in.ToolTipBackgroundColorIntensity) + ";color: " + ABMaterial.GetColorStrMap(in.ToolTipForeColor,in.ToolTipForeColorIntensity) + ";}");
		writer.write(".abmfiledropzone" + in.ThemeName.toLowerCase() + " {color: " + ABMaterial.GetColorStrMap(in.DragZoneForeColor, in.DragZoneForeColorIntensity) + ";}");
	}
	
	@Hide
	protected void WriteCSSChronoList(BufferedWriter writer, ThemeChronologyList in) throws IOException {
		for (Map.Entry<String, ThemeChronologyBadge> entry : in.Badges.entrySet()) {
			ThemeChronologyBadge bad = entry.getValue();
			writer.write(".chronology-badge" + in.ThemeName.toLowerCase() + "-" + bad.ThemeName.toLowerCase() + "{background-color: " + ABMaterial.GetColorStrMap(bad.BackColor,bad.BackColorIntensity) +" !important;}");
			if (bad.ColorizePointer) {
				writer.write(".chronology > li > .chronology-panel" + in.ThemeName.toLowerCase() + "-" + bad.ThemeName.toLowerCase() + ":before {");
				writer.write("border-left: 15px solid " + ABMaterial.GetColorStrMap(bad.BackColor,bad.BackColorIntensity) +" !important;");
				writer.write("border-right: 0 solid " + ABMaterial.GetColorStrMap(bad.BackColor,bad.BackColorIntensity) +" !important;");  
				writer.write("}");
				writer.write(".chronology > li > .chronology-panel" + in.ThemeName.toLowerCase() + "-" + bad.ThemeName.toLowerCase() + ":after {");
				writer.write("border-left: 14px solid transparent !important;");
				writer.write("border-right: 0 solid transparent !important;");
				writer.write("}");
			}
		}
	}
	
	@Hide
	protected void WriteCSSLists(BufferedWriter writer, ThemeList in) throws IOException {
		writer.write(".itembody" + in.ThemeName.toLowerCase() + " {background-color:" + ABMaterial.GetColorStrMap(in.ItemBackColor,in.ItemBackColorIntensity) + ";}");
		writer.write(".itembody" + in.ThemeName.toLowerCase() + ":hover {background-color:" + ABMaterial.GetColorStrMap(in.ItemHoverColor,in.ItemHoverColorIntensity) + ";}");
		writer.write(".itembody" + in.ThemeName.toLowerCase() + ".active {background-color:" + ABMaterial.GetColorStrMap(in.ItemActiveColor,in.ItemActiveColorIntensity) + ";}");
		
		writer.write(".subbody" + in.ThemeName.toLowerCase() + " {background-color:" + ABMaterial.GetColorStrMap(in.SubItemBackColor,in.SubItemBackColorIntensity) + ";}");
		writer.write(".subbody" + in.ThemeName.toLowerCase() + ":hover {background-color:" + ABMaterial.GetColorStrMap(in.SubItemHoverColor,in.SubItemHoverColorIntensity) + ";}");
		writer.write(".subbody" + in.ThemeName.toLowerCase() + ".active {background-color:" + ABMaterial.GetColorStrMap(in.SubItemActiveColor,in.SubItemActiveColorIntensity) + ";}");
		
		for (Map.Entry<String, ItemTheme> entry : in.Items.entrySet()) {
			ItemTheme it = entry.getValue();
			writer.write(".subbodyitem" + in.ThemeName.toLowerCase() + it.ThemeName.toLowerCase() + " {background-color:" + ABMaterial.GetColorStrMap(it.BackColor,it.BackColorIntensity) + ";}");
			writer.write(".subbodyitem" + in.ThemeName.toLowerCase() + it.ThemeName.toLowerCase() + ":hover {background-color:" + ABMaterial.GetColorStrMap(it.HoverColor,it.HoverColorIntensity) + ";}");
			writer.write(".subbodyitem" + in.ThemeName.toLowerCase() + it.ThemeName.toLowerCase() + ".active {background-color:" + ABMaterial.GetColorStrMap(it.ActiveColor,it.ActiveColorIntensity) + ";}");
		}
		ItemTheme defitem = new ItemTheme();
		defitem.Colorize(in.MainColor);
		writer.write(".subbodyitem" + in.ThemeName.toLowerCase() + defitem.ThemeName.toLowerCase() + " {background-color:" + ABMaterial.GetColorStrMap(defitem.BackColor,defitem.BackColorIntensity) + ";}");
		writer.write(".subbodyitem" + in.ThemeName.toLowerCase() + defitem.ThemeName.toLowerCase() + ":hover {background-color:" + ABMaterial.GetColorStrMap(defitem.HoverColor,defitem.HoverColorIntensity) + ";}");
		writer.write(".subbodyitem" + in.ThemeName.toLowerCase() + defitem.ThemeName.toLowerCase() + ".active {background-color:" + ABMaterial.GetColorStrMap(defitem.ActiveColor,defitem.ActiveColorIntensity) + ";}");
	}
	
	@Hide
	protected void WriteCSSAudio(BufferedWriter writer, ThemeAudioPlayer in) throws IOException {
		String ThemeName = in.ThemeName.toLowerCase();
		writer.write(".aplayer .aplayer-pic .aplayer-button" + ThemeName + " {color: " + ABMaterial.GetColorStrMap(in.PlayPauseColor,ABMaterial.INTENSITY_NORMAL) + " !important; /*playpause*/}");
		writer.write(".aplayer .aplayer-pic .aplayer-play" + ThemeName + " {border: 2px solid " + ABMaterial.GetColorStrMap(in.PlayPauseColor,ABMaterial.INTENSITY_NORMAL) + " !important; /*playpause*/}");
		writer.write(".aplayer .aplayer-pic .aplayer-pause" + ThemeName + " {border: 2px solid " + ABMaterial.GetColorStrMap(in.PlayPauseColor,ABMaterial.INTENSITY_NORMAL) + " !important; /*playpause*/}");

		writer.write(".aplayer .aplayer-info" + ThemeName + " {background: " + ABMaterial.GetColorStrMap(in.BackColor,ABMaterial.INTENSITY_NORMAL) + " !important; /*background*/}");
		writer.write(".aplayer .aplayer-info .aplayer-controller .aplayer-bar-wrap .aplayer-bar .aplayer-played .aplayer-thumb" + ThemeName + " {background: " + ABMaterial.GetColorStrMap(in.BackColor,ABMaterial.INTENSITY_NORMAL) + " !important; /*background*/}");

		writer.write(".aplayer .aplayer-info .aplayer-music .aplayer-title" + ThemeName + " {color: " + ABMaterial.GetColorStrMap(in.ForeColor,ABMaterial.INTENSITY_DARKEN2) + " !important; /*maincolordark*/}");
		writer.write(".aplayer .aplayer-info .aplayer-music .aplayer-author" + ThemeName + " {color: " + ABMaterial.GetColorStrMap(in.ForeColor,ABMaterial.INTENSITY_DARKEN2) + " !important; /*maincolordark*/}");
		writer.write(".aplayer .aplayer-info .aplayer-controller .aplayer-time" + ThemeName + " i {color: " + ABMaterial.GetColorStrMap(in.ForeColor,ABMaterial.INTENSITY_DARKEN2) + " !important; /*maincolordark*/}");
		writer.write(".aplayer-ptime" + ThemeName + " i {color: " + ABMaterial.GetColorStrMap(in.ForeColor,ABMaterial.INTENSITY_DARKEN2) + "; /*maincolordark*/}");
		writer.write(".aplayer .aplayer-lrc" + ThemeName + " p {color: " + ABMaterial.GetColorStrMap(in.ForeColor,ABMaterial.INTENSITY_DARKEN2) + "; /*maincolordark*/}");
		writer.write(".aplayer-timeparent" + ThemeName + " {color: " + ABMaterial.GetColorStrMap(in.ForeColor,ABMaterial.INTENSITY_DARKEN2) + " !important; /*maincolordark*/}");

		writer.write(".aplayer .aplayer-info .aplayer-controller .aplayer-time i.aplayer-noloop" + ThemeName + " {color: " + ABMaterial.GetColorStrMap(in.ForeColor,ABMaterial.INTENSITY_LIGHTEN2) + " !important; /*maincolorlighter*/}");

		writer.write(".aplayer .aplayer-info .aplayer-controller .aplayer-time i.aplayer-noloop" +  ThemeName+ ":hover {color: " + ABMaterial.GetColorStrMap(in.ForeColor,ABMaterial.INTENSITY_NORMAL) + " !important; /*maincolor*/}");
		writer.write(".aplayer .aplayer-info .aplayer-controller .aplayer-time" + ThemeName + " i:hover {color: " + ABMaterial.GetColorStrMap(in.ForeColor,ABMaterial.INTENSITY_NORMAL) + " !important; /*maincolor*/}");

		writer.write(".aplayer .aplayer-info .aplayer-controller .aplayer-bar-wrap .aplayer-bar" + ThemeName + " {background: " + ABMaterial.GetColorStrMap(in.PlayBarBackColor,ABMaterial.INTENSITY_NORMAL) + " !important; /*playerbar*/}");
		writer.write(".aplayer .aplayer-info .aplayer-controller .aplayer-bar-wrap .aplayer-bar .aplayer-loaded" + ThemeName + " {background: " + ABMaterial.GetColorStrMap(in.PlayBarBackColor,ABMaterial.INTENSITY_DARKEN2) + " !important; /*playerbardarker*/}");

		writer.write(".aplayer .aplayer-info .aplayer-controller .aplayer-volume-wrap .aplayer-volume-bar-wrap .aplayer-volume-bar" + ThemeName + " {background: " + ABMaterial.GetColorStrMap(in.VolumeBackColor,ABMaterial.INTENSITY_NORMAL) + " !important;/*volumebackground*/}");

		writer.write(".aplayer .aplayer-list" + ThemeName + " ol li {border-top: 1px solid " + ABMaterial.GetColorStrMap(in.ForeColor,ABMaterial.INTENSITY_NORMAL) + " !important; /*maincolor*/ cursor: pointer;	background: " + ABMaterial.GetColorStrMap(in.BackColor,ABMaterial.INTENSITY_NORMAL) + " !important; /*background*/	color: " + ABMaterial.GetColorStrMap(in.ForeColor,ABMaterial.INTENSITY_DARKEN2) + " !important;/*maincolordark*/}");
		if (in.BackColor.equalsIgnoreCase(ABMaterial.COLOR_WHITE) || in.BackColor.equalsIgnoreCase(ABMaterial.COLOR_TRANSPARENT)) {
			writer.write(".aplayer .aplayer-list" + ThemeName + " ol li:hover {background: " + ABMaterial.GetColorStrMap(ABMaterial.COLOR_GREY,ABMaterial.INTENSITY_LIGHTEN4) + " !important; /*backgrounddarker*/ color: " + ABMaterial.GetColorStrMap(in.ForeColor,ABMaterial.INTENSITY_DARKEN2) + " !important; /*maincolordark*/}");
			writer.write(".aplayer .aplayer-list ol li.aplayer-list-light" + ThemeName + " {background: " + ABMaterial.GetColorStrMap(ABMaterial.COLOR_GREY,ABMaterial.INTENSITY_LIGHTEN4) + " !important; /*backgrounddarker*/	color: " + ABMaterial.GetColorStrMap(in.ForeColor,ABMaterial.INTENSITY_DARKEN2) + " !important; /*maincolordark*/}");
		} else {
			writer.write(".aplayer .aplayer-list" + ThemeName + " ol li:hover {background: " + ABMaterial.GetColorStrMap(in.BackColor,ABMaterial.INTENSITY_DARKEN2) + " !important; /*backgrounddarker*/ color: " + ABMaterial.GetColorStrMap(in.ForeColor,ABMaterial.INTENSITY_DARKEN2) + " !important; /*maincolordark*/}");
			writer.write(".aplayer .aplayer-list ol li.aplayer-list-light" + ThemeName + " {background: " + ABMaterial.GetColorStrMap(in.BackColor,ABMaterial.INTENSITY_DARKEN2) + " !important; /*backgrounddarker*/	color: " + ABMaterial.GetColorStrMap(in.ForeColor,ABMaterial.INTENSITY_DARKEN2) + " !important; /*maincolordark*/}");
		}

		writer.write(".aplayer .aplayer-list ol li .aplayer-list-index" + ThemeName + " {color: " + ABMaterial.GetColorStrMap(in.ForeColor,ABMaterial.INTENSITY_DARKEN2) + " !important; /*maincolordark*/}");
		writer.write(".aplayer .aplayer-list ol li .aplayer-list-author" + ThemeName + " {color: " + ABMaterial.GetColorStrMap(in.ForeColor,ABMaterial.INTENSITY_DARKEN2) + " !important; /*maincolordark*/}");
	}
	
	@Hide
	protected void WriteCSSDTScroller(BufferedWriter writer, ThemeDateTimeScroller in) throws IOException {
		//Background:
		String col = ABMaterial.GetColorStrMap(in.ScrollerBackColor,in.ScrollerBackColorIntensity);
		writer.write(".android-ics" + in.ThemeName.toLowerCase() + ".light" + in.ThemeName.toLowerCase() + " {background: " + col + "}");
		writer.write(".android-ics" + in.ThemeName.toLowerCase() + ".light" + in.ThemeName.toLowerCase() + " .dwwo {");
		writer.write("background: -webkit-gradient(linear,left bottom,left top,color-stop(0, " + col + "),color-stop(0.52, rgba(245,245,245,0)),color-stop(0.48, rgba(245,245,245,0)),color-stop(1, " + col + "));");
		writer.write("background: -moz-linear-gradient(" + col + " 0%,rgba(245,245,245,0) 52%, rgba(245,245,245,0) 48%, " + col + " 100%);");
		writer.write("background: -ms-linear-gradient(" + col + " 0%,rgba(245,245,245,0) 52%, rgba(245,245,245,0) 48%, " + col + " 100%);");
		writer.write("background: -o-linear-gradient(" + col + " 0%,rgba(245,245,245,0) 52%, rgba(245,245,245,0) 48%, " + col + " 100%);");
		writer.write("}");
		writer.write(".android-ics" + in.ThemeName.toLowerCase() + ".light" + in.ThemeName.toLowerCase() + " .dwwb {background: " + col + ";color: " + col + ";}");

		col = ABMaterial.GetColorStrMap(in.ScrollerForeColor,in.ScrollerForeColorIntensity);
		writer.write(".android-ics" + in.ThemeName.toLowerCase() + ".light" + in.ThemeName.toLowerCase() + " {color: " + col + ";}");
		writer.write(".android-ics" + in.ThemeName.toLowerCase() + ".light" + in.ThemeName.toLowerCase() + " .dwv {border-bottom: 2px solid " + col + ";}");
		writer.write(".android-ics" + in.ThemeName.toLowerCase() + ".light" + in.ThemeName.toLowerCase() + " .dwwbp:after {border-color: transparent transparent " + col + " transparent;}");
		writer.write(".android-ics" + in.ThemeName.toLowerCase() + ".light" + in.ThemeName.toLowerCase() + " .dwwbm:after {border-color: " + col + " transparent transparent transparent;}");
		writer.write(".android-ics" + in.ThemeName.toLowerCase() + ".light" + in.ThemeName.toLowerCase() + " .dwwol {border-top: 2px solid " + col + ";border-bottom: 2px solid " + col + ";}");
		writer.write(".android-ics" + in.ThemeName.toLowerCase() + ".light" + in.ThemeName.toLowerCase() + " .dwbc {border-top: 1px solid " + col + ";}");
		writer.write(".android-ics" + in.ThemeName.toLowerCase() + ".light" + in.ThemeName.toLowerCase() + " .dwb-s .dwb {border-right: 1px solid " + col + ";}");

		writer.write(".android-ics" + in.ThemeName.toLowerCase() + ".light" + in.ThemeName.toLowerCase() + " .dwb {color: " + col + ";}");

		col = ABMaterial.GetColorStrMap(in.ScrollerValueColor,in.ScrollerValueColorIntensity);	
		writer.write(".android-ics" + in.ThemeName.toLowerCase() + " .dww li {color: " + col + ";}");
		writer.write(".android-ics" + in.ThemeName.toLowerCase() + ".light" + in.ThemeName.toLowerCase() + " .dww li {color: " + col + ";}");
		writer.write(".input-field" + in.ThemeName.toLowerCase() + "ics {border-radius: " + in.InputBorderRadiusPx + "px;}");
		writer.write(".input-field" + in.ThemeName.toLowerCase() + "ics.active {background-color: " + ABMaterial.GetColorStrMap(in.InputFocusBackColor,in.InputFocusBackColorIntensity) + " !important;}");
		writer.write(".input-field" + in.ThemeName.toLowerCase() + "ics input[type=text]::-webkit-input-placeholder {color:" + ABMaterial.GetColorStrMap(in.InputPlaceholderColor,in.InputPlaceholderColorIntensity)  + ";}");
		writer.write(".input-field" + in.ThemeName.toLowerCase() + "ics input[type=text]:-moz-placeholder {color: " + ABMaterial.GetColorStrMap(in.InputPlaceholderColor,in.InputPlaceholderColorIntensity)  + ";opacity:  1;}");
		writer.write(".input-field" + in.ThemeName.toLowerCase() + "ics input[type=text]::-moz-placeholder {color: " + ABMaterial.GetColorStrMap(in.InputPlaceholderColor,in.InputPlaceholderColorIntensity)  + ";opacity:  1;}");
		writer.write(".input-field" + in.ThemeName.toLowerCase() + "ics input[type=text]:-ms-input-placeholder {color: " + ABMaterial.GetColorStrMap(in.InputPlaceholderColor,in.InputPlaceholderColorIntensity)  + ";}");
		writer.write(".input-field" + in.ThemeName.toLowerCase() + "ics input[type=text]::-ms-input-placeholder {color: " + ABMaterial.GetColorStrMap(in.InputPlaceholderColor,in.InputPlaceholderColorIntensity)  + ";}");
	}
	
	@Hide
	protected void WriteCSSDTPicker(BufferedWriter writer, ThemeDateTimePicker in) throws IOException {
		writer.write(".dtp" + in.ThemeName.toLowerCase() + " i {margin-top: -6px};");
		writer.write(".dtp" + in.ThemeName.toLowerCase() + " > .dtp-content { background: " + ABMaterial.GetColorStrMap(in.PickerCalendarClockBackColor,in.PickerCalendarClockBackColorIntensity) + ";}");
		writer.write(".dtp" + in.ThemeName.toLowerCase() + " > .dtp-content > .dtp-date-view > header.dtp-header { background: " + ABMaterial.GetColorStrMap(in.PickerHeaderDayNameBackColor,in.PickerHeaderDayNameBackColorIntensity) + "; color: " + ABMaterial.GetColorStrMap(in.PickerHeaderDayNameForeColor,in.PickerHeaderDayNameForeColorIntensity) + ";}");

		writer.write(".dtp" + in.ThemeName.toLowerCase() + " div.dtp-date, .dtp" + in.ThemeName.toLowerCase() + " div.dtp-time { background: " + ABMaterial.GetColorStrMap(in.PickerHeaderDateBackColor,in.PickerHeaderDateBackColorIntensity) + "; padding: 6px;}");
		writer.write(".dtp" + in.ThemeName.toLowerCase() + " div.dtp-actual-month { color: " + ABMaterial.GetColorStrMap(in.PickerHeaderDateMonthForeColor,in.PickerHeaderDateMonthForeColorIntensity) + "; }");
		writer.write(".dtp" + in.ThemeName.toLowerCase() + " div.dtp-actual-num { color: " + ABMaterial.GetColorStrMap(in.PickerHeaderDateDayForeColor, in.PickerHeaderDateDayForeColorIntensity) + "; }");
		writer.write(".dtp" + in.ThemeName.toLowerCase() + " div.dtp-actual-year { color: " + ABMaterial.GetColorStrMap(in.PickerHeaderDateYearForeColor,in.PickerHeaderDateYearForeColorIntensity) + "; }");
		writer.write(".dtp" + in.ThemeName.toLowerCase() + " div.dtp-picker {height: 300px;}");
		
		writer.write(".dtp" + in.ThemeName.toLowerCase() + " div.dtp-picker-month, .dtp" + in.ThemeName.toLowerCase() + " div.dtp-actual-time { color: " + ABMaterial.GetColorStrMap(in.PickerCalendarClockForeColor,in.PickerCalendarClockForeColorIntensity) + ";}");

		writer.write(".dtp" + in.ThemeName.toLowerCase() + " .dtp-close > a { color: " + ABMaterial.GetColorStrMap(in.PickerHeaderDayNameBackColor,in.PickerHeaderDayNameBackColor) + "; }");

		writer.write(".dtp" + in.ThemeName.toLowerCase() + " .dtp-date-view {font-size: 14px;}");
		writer.write(".dtp" + in.ThemeName.toLowerCase() + " table.dtp-picker-days, .dtp" + in.ThemeName.toLowerCase() + " table.dtp-picker-days tr, .dtp" + in.ThemeName.toLowerCase() + " table.dtp-picker-days tr > td { border: none; font-size: 14px;}");
		writer.write(".dtp" + in.ThemeName.toLowerCase() + " table.dtp-picker-days tr > td {  font-weight: 500; padding: 0px; }");
		writer.write(".dtp" + in.ThemeName.toLowerCase() + " table.dtp-picker-days tr > td > span.dtp-select-day { color: " + ABMaterial.GetColorStrMap(in.PickerCalendarDayForeColor,in.PickerCalendarDayForeColorIntensity) + "!important; }");
		
		writer.write(".dtp" + in.ThemeName.toLowerCase() + " table.dtp-picker-days tr.week { border: none;height:30px;background-color: " + ABMaterial.GetColorStrMap(in.PickerCalendarDayActiveBackColor,in.PickerCalendarDayActiveBackColorIntensity) + " }");
		writer.write(".dtp" + in.ThemeName.toLowerCase() + " table.dtp-picker-days tr.week > td > a { background: " + ABMaterial.GetColorStrMap(in.PickerCalendarDayActiveBackColor,in.PickerCalendarDayActiveBackColorIntensity) + "; color: " + ABMaterial.GetColorStrMap(in.PickerCalendarDayActiveForeColor,in.PickerCalendarDayActiveForeColorIntensity) + "; }");
		
		writer.write(".dtp" + in.ThemeName.toLowerCase() + " table.dtp-picker-days tr > td > a, .dtp" + in.ThemeName.toLowerCase() + " .dtp-picker-time > a { color: " + ABMaterial.GetColorStrMap(in.PickerClockHourMinForeColor, in.PickerClockHourMinForeColorIntensity) + "; }");
		writer.write(".dtp" + in.ThemeName.toLowerCase() + " table.dtp-picker-days tr > td > a.selected{padding: 0.5rem 0.5rem 0.5rem 0.5rem; background: " + ABMaterial.GetColorStrMap(in.PickerCalendarDayActiveBackColor,in.PickerCalendarDayActiveBackColorIntensity) + "; color: " + ABMaterial.GetColorStrMap(in.PickerCalendarDayActiveForeColor,in.PickerCalendarDayActiveForeColorIntensity) + "; }");
		writer.write(".dtp" + in.ThemeName.toLowerCase() + " table.dtp-picker-days tr > td > a.currdate {font-weight: 700}");
		writer.write(".dtp" + in.ThemeName.toLowerCase() + " table.dtp-picker-days tr > th { padding: 5px 0px 0px 0px; color: " + ABMaterial.GetColorStrMap(in.PickerCalendarDayNamesForeColor,in.PickerCalendarDayNamesForeColorIntensity) + "; }");

		writer.write(".dtp" + in.ThemeName.toLowerCase() + " .p10 > a { color: " + ABMaterial.GetColorStrMap(in.PickerHeaderDateArrowsColor, in.PickerHeaderDateArrowsColorIntensity) + "; }");

		writer.write(".dtp" + in.ThemeName.toLowerCase() + " a.dtp-meridien-am, .dtp" + in.ThemeName.toLowerCase() + " a.dtp-meridien-pm { color: " + ABMaterial.GetColorStrMap(in.PickerCalendarDayForeColor, in.PickerCalendarDayForeColorIntensity) + "; background: " + ABMaterial.GetColorStrMap(in.PickerClockBackColor,in.PickerClockBackColorIntensity) + "; }");
		writer.write(".dtp" + in.ThemeName.toLowerCase() + " .dtp-actual-meridien a.selected { background: " + ABMaterial.GetColorStrMap(in.PickerClockHourMinActiveBackColor,in.PickerClockHourMinActiveBackColorIntensity) + "; color: " + ABMaterial.GetColorStrMap(in.PickerClockHourMinActiveForeColor, in.PickerClockHourMinActiveForeColorIntensity) + "; }");

		writer.write(".dtp" + in.ThemeName.toLowerCase() + " .dtp-picker-time { color: " + ABMaterial.GetColorStrMap(in.PickerClockHourMinForeColor, in.PickerClockHourMinForeColorIntensity) + ";  }");

		writer.write(".dtp" + in.ThemeName.toLowerCase() + " .dtp-picker-time > a.dtp-select-hour.selected { background: " + ABMaterial.GetColorStrMap(in.PickerClockHourMinActiveBackColor, in.PickerClockHourMinActiveBackColorIntensity) + "; color: " + ABMaterial.GetColorStrMap(in.PickerClockHourMinActiveForeColor, in.PickerClockHourMinActiveForeColorIntensity) + "; }");
		writer.write(".dtp" + in.ThemeName.toLowerCase() + " .dtp-picker-time > a.dtp-select-minute.selected { background: " + ABMaterial.GetColorStrMap(in.PickerClockHourMinActiveBackColor, in.PickerClockHourMinActiveBackColorIntensity) + "; color: " + ABMaterial.GetColorStrMap(in.PickerClockHourMinActiveForeColor, in.PickerClockHourMinActiveForeColorIntensity) + "; }");

		writer.write(".dtp" + in.ThemeName.toLowerCase() + " div.dtp-picker-clock { background: " + ABMaterial.GetColorStrMap(in.PickerClockBackColor, in.PickerClockBackColorIntensity) + "; }");
		writer.write(".dtp" + in.ThemeName.toLowerCase() + " .dtp-clock-center { background: " + ABMaterial.GetColorStrMap(in.PickerClockCenterBackColor, in.PickerClockCenterBackColorIntensity) + "; }");

		writer.write(".dtp" + in.ThemeName.toLowerCase() + " .dtp-hand, .dtp" + in.ThemeName.toLowerCase() + " .dtp-hour-hand { background: " + ABMaterial.GetColorStrMap(in.PickerClockInActiveHandColor, in.PickerClockInActiveHandColorIntensity) + "; }");
		writer.write(".dtp" + in.ThemeName.toLowerCase() + " .dtp-hand.on { background: " + ABMaterial.GetColorStrMap(in.PickerClockActiveHandColor, in.PickerClockActiveHandColorIntensity) + "; }");

		writer.write(".dtp" + in.ThemeName.toLowerCase() + " .dtp-btn-clear {padding: 0 1rem; background: " + ABMaterial.GetColorStrMap(in.PickerCalendarClockButtonBackColor, in.PickerCalendarClockButtonBackColorIntensity) + "; color: " + ABMaterial.GetColorStrMap(in.PickerCalendarClockButtonForeColor, in.PickerCalendarClockButtonForeColorIntensity) + "; }");
		writer.write(".dtp" + in.ThemeName.toLowerCase() + " .dtp-btn-today {padding: 0 1rem; background: " + ABMaterial.GetColorStrMap(in.PickerCalendarClockButtonBackColor, in.PickerCalendarClockButtonBackColorIntensity) + "; color: " + ABMaterial.GetColorStrMap(in.PickerCalendarClockButtonForeColor, in.PickerCalendarClockButtonForeColorIntensity) + "; }");
		writer.write(".dtp" + in.ThemeName.toLowerCase() + " .dtp-btn-ok {padding: 0 1rem; background: " + ABMaterial.GetColorStrMap(in.PickerCalendarClockButtonBackColor, in.PickerCalendarClockButtonBackColorIntensity) + "; color: " + ABMaterial.GetColorStrMap(in.PickerCalendarClockButtonForeColor, in.PickerCalendarClockButtonForeColorIntensity) + "; }");
		writer.write(".dtp" + in.ThemeName.toLowerCase() + " .dtp-btn-cancel {padding: 0 1rem; background: " + ABMaterial.GetColorStrMap(in.PickerCalendarClockButtonBackColor, in.PickerCalendarClockButtonBackColorIntensity) + "; color: " + ABMaterial.GetColorStrMap(in.PickerCalendarClockButtonForeColor, in.PickerCalendarClockButtonForeColorIntensity) + "; }");
		writer.write(".input-field" + in.ThemeName.toLowerCase() + "dtp {border-radius: " + in.InputBorderRadiusPx + "px;}");
		writer.write(".input-field" + in.ThemeName.toLowerCase() + "dtp.active {background-color: " + ABMaterial.GetColorStrMap(in.InputFocusBackColor,in.InputFocusBackColorIntensity) + " !important;}");
		writer.write(".input-field" + in.ThemeName.toLowerCase() + "dtp input[type=text]::-webkit-input-placeholder {color:" + ABMaterial.GetColorStrMap(in.InputPlaceholderColor,in.InputPlaceholderColorIntensity)  + ";}");
		writer.write(".input-field" + in.ThemeName.toLowerCase() + "dtp input[type=text]:-moz-placeholder {color: " + ABMaterial.GetColorStrMap(in.InputPlaceholderColor,in.InputPlaceholderColorIntensity)  + ";opacity:  1;}");
		writer.write(".input-field" + in.ThemeName.toLowerCase() + "dtp input[type=text]::-moz-placeholder {color: " + ABMaterial.GetColorStrMap(in.InputPlaceholderColor,in.InputPlaceholderColorIntensity)  + ";opacity:  1;}");
		writer.write(".input-field" + in.ThemeName.toLowerCase() + "dtp input[type=text]:-ms-input-placeholder {color: " + ABMaterial.GetColorStrMap(in.InputPlaceholderColor,in.InputPlaceholderColorIntensity)  + ";}");
		writer.write(".input-field" + in.ThemeName.toLowerCase() + "dtp input[type=text]::-ms-input-placeholder {color: " + ABMaterial.GetColorStrMap(in.InputPlaceholderColor,in.InputPlaceholderColorIntensity)  + ";}");
	}
	
	@Hide
	protected void WriteCSSSlider(BufferedWriter writer, ThemeSlider in) throws IOException {
		writer.write(".noUi-horizontal .abmsl" + in.ThemeName.toLowerCase() + ".noUi-handle {background-color: " + ABMaterial.GetColorStrMap(in.ButtonColor,in.ButtonColorIntensity) + " !important;}");
		writer.write(".noUi-target .abmsl" + in.ThemeName.toLowerCase() + ".range-label {background-color: " + ABMaterial.GetColorStrMap(in.ButtonColor,in.ButtonColorIntensity) + " !important;}");
		writer.write(".abmsl" + in.ThemeName.toLowerCase() + ".range-label span {color: " + ABMaterial.GetColorStrMap(in.ButtonTextColor,in.ButtonTextColorIntensity) + " !important;}");
		writer.write(".abmsl" + in.ThemeName.toLowerCase() + ".noUi-target.noUi-connect {background-color: " + ABMaterial.GetColorStrMap(in.ConnectLineColor,in.ConnectLineColorIntensity) + " !important;}");
		writer.write(".abmsl" + in.ThemeName.toLowerCase() + ".noUi-background {background-color: " + ABMaterial.GetColorStrMap(in.LineColor,in.LineColorIntensity) + " !important;box-shadow: inset 0 0px 0px #f0f0f0;}");		
	}
	
	@Hide
	protected void WriteCSSRange(BufferedWriter writer, ThemeRange in) throws IOException {
		writer.write(".noUi-horizontal .abmr" + in.ThemeName.toLowerCase() + ".noUi-handle {background-color: " + ABMaterial.GetColorStrMap(in.ButtonColor,in.ButtonColorIntensity) + " !important;}");
		writer.write(".noUi-target .abmr" + in.ThemeName.toLowerCase() + ".range-label {background-color: " + ABMaterial.GetColorStrMap(in.ButtonColor,in.ButtonColorIntensity) + " !important;}");
		writer.write(".abmr" + in.ThemeName.toLowerCase() + ".range-label span {color: " + ABMaterial.GetColorStrMap(in.ButtonTextColor,in.ButtonTextColorIntensity) + " !important;}");
		writer.write(".abmr" + in.ThemeName.toLowerCase() + ".noUi-background {background-color: " + ABMaterial.GetColorStrMap(in.LineColor,in.LineColorIntensity) + " !important;box-shadow: inset 0 0px 0px #f0f0f0;}");
		writer.write(".abmr" + in.ThemeName.toLowerCase() + ".noUi-origin.noUi-connect {background-color: " + ABMaterial.GetColorStrMap(in.ConnectLineColor,in.ConnectLineColorIntensity) + " !important;}");
	}
		
	@Hide
	protected void WriteCSSUpload(BufferedWriter writer, ThemeUpload up, String id, String myRelPath) throws IOException {		
		writer.write("#upload" + id.toLowerCase() + up.ThemeName.toLowerCase() + " {font-family:'PT Sans Narrow', sans-serif;background-color:" + ABMaterial.GetColorStrMap(up.BackColor,up.BackColorIntensity) + ";width:300px;padding:30px;border-radius:3px;margin:0px auto 0px;}");
		writer.write("#drop" + id.toLowerCase() + up.ThemeName.toLowerCase() + " {background-color: " + ABMaterial.GetColorStrMap(up.DropZoneBackColor,up.DropZoneBackColorIntensity) + ";padding: 40px 50px;margin-bottom: 30px;border: 20px solid rgba(0,0,0,0);border-radius: 3px;border-image: url('" + myRelPath + "js/border-image.png') 25 repeat;text-align: center;text-transform: uppercase;font-size:16px;font-weight:bold;color:" + ABMaterial.GetColorStrMap(up.ForeColor,up.ForeColorIntensity) + ";}");
		writer.write("#drop" + id.toLowerCase() + up.ThemeName.toLowerCase() + "  a{background-color:" + ABMaterial.GetColorStrMap(up.ButtonBackColor,up.ButtonBackColorIntensity) + ";padding:12px 26px;color:" + ABMaterial.GetColorStrMap(up.ButtonForeColor,up.ButtonForeColorIntensity) + ";font-size:14px;border-radius:2px;cursor:pointer;display:inline-block;margin-top:12px;line-height:1;margin-left: -20px;margin-right: -20px;}");
		writer.write("#drop" + id.toLowerCase() + up.ThemeName.toLowerCase() + "  a:hover{background-color:" + ABMaterial.GetColorStrMap(up.ButtonHoverBackColor,up.ButtonHoverBackColorIntensity) + ";}");
		writer.write("#drop" + id.toLowerCase() + up.ThemeName.toLowerCase() + "  input{display:none;}");
		writer.write("#upload" + id.toLowerCase() + up.ThemeName.toLowerCase() + "  ul{list-style:none;margin:0 -30px;border-top:1px solid " + ABMaterial.GetColorStrMap(up.UploadBorderColor,up.UploadBorderColorIntensity) + ";border-bottom:1px solid " + ABMaterial.GetColorStrMap(up.UploadBorderColor,up.UploadBorderColorIntensity) + ";}");
		writer.write("#upload" + id.toLowerCase() + up.ThemeName.toLowerCase() + "  ul li{background-color:" + ABMaterial.GetColorStrMap(up.UploadBackColor,up.UploadBackColorIntensity) + ";border-top:1px solid " + ABMaterial.GetColorStrMap(up.UploadBorderColor,up.UploadBorderColorIntensity) + ";border-bottom:1px solid " + ABMaterial.GetColorStrMap(up.UploadBorderColor,up.UploadBorderColorIntensity) + ";padding:15px;height: 78px;position: relative;}");
		writer.write("#upload" + id.toLowerCase() + up.ThemeName.toLowerCase() + "  ul li input{display: none;}");
		writer.write("#upload" + id.toLowerCase() + up.ThemeName.toLowerCase() + "  ul li p{width: 166px;overflow: hidden;white-space: nowrap;color: " + ABMaterial.GetColorStrMap(up.UploadFileColor,up.UploadFileColorIntensity) + ";font-size: 14px;font-weight: bold;position: absolute;top: 0px;left: 78px;}");
		writer.write("#upload" + id.toLowerCase() + up.ThemeName.toLowerCase() + "  ul li i{font-weight: normal;font-style:normal;color:" + ABMaterial.GetColorStrMap(up.UploadFileSizeColor,up.UploadFileSizeColorIntensity) + ";display:block;}");
		writer.write("#upload" + id.toLowerCase() + up.ThemeName.toLowerCase() + "  ul li canvas{top: 15px;left: 15px;position: absolute;}");
		writer.write("#upload" + id.toLowerCase() + up.ThemeName.toLowerCase() + "  ul li span{width: 15px;height: 12px;background: url('" + myRelPath + "js/icons.png') no-repeat;position: absolute;top: 34px;right: 33px;cursor:pointer;}");
		writer.write("#upload" + id.toLowerCase() + up.ThemeName.toLowerCase() + "  ul li.working span{height: 16px;background-position: 0 -12px;}");
		writer.write("#upload" + id.toLowerCase() + up.ThemeName.toLowerCase() + "  ul li.error p{color:" + ABMaterial.GetColorStrMap(up.ErrorColor,up.ErrorColorIntensity) + ";}");		
	}
	
	@Hide
	protected void WriteJSUpload(BufferedWriter writer, ThemeUpload up, String id) throws IOException {
		writer.write("function runupload() {");
		writer.write("var ul = $('#upload" + id.toLowerCase() + up.ThemeName.toLowerCase() + " ul');");
		writer.write("$('#drop" + id.toLowerCase() + up.ThemeName.toLowerCase() + " a').off('click').click(function() {");
		writer.write("$(this).parent().find('input').click();");
		writer.write("});");
		writer.write("$('#upload" + id.toLowerCase() + up.ThemeName.toLowerCase() + "').fileupload({");
		writer.write("dropZone: $('#drop" + id.toLowerCase() + up.ThemeName.toLowerCase() + "'),");
		writer.write("add: function (e, data) {");
		writer.write("var tpl = $('<li class=\"working\"><input type=\"text\" value=\"0\" data-width=\"48\" data-height=\"48\"'+' data-fgColor=\"" + ABMaterial.GetColorStrMap(up.UploadProgressColor,up.UploadProgressColorIntensity) + "\" data-readOnly=\"1\" data-bgColor=\"" + ABMaterial.GetColorStrMap(up.UploadBackColor,up.UploadBackColorIntensity) + "\" /><p></p><span></span></li>');");
		writer.write("tpl.find('p').text(data.files[0].name).append('<br><i>' + formatFileSize(data.files[0].size) + '</i>');");
		writer.write("data.context = tpl.appendTo(ul);");
		writer.write("tpl.find('input').knob();");
		writer.write("tpl.find('span').click(function(){");
		writer.write("if(tpl.hasClass('working')){");
		writer.write("jqXHR.abort();");
		writer.write("}");
		writer.write("tpl.fadeOut(function(){");
		writer.write("tpl.remove();");
		writer.write("});");
		writer.write("});");
		writer.write("var jqXHR = data.submit();");
		writer.write("},");
		writer.write("progress: function(e, data){");
		writer.write("var progress = parseInt(data.loaded / data.total * 100, 10);");
		writer.write("data.context.find('input').val(progress).change();");
		writer.write("if(progress == 100){");
		writer.write("data.context.removeClass('working');");
		writer.write("}");
		writer.write("},");
		writer.write("fail:function(e, data){");            
		writer.write("data.context.addClass('error');");
		writer.write("b4j_raiseEvent('page_parseevent', {'eventname': 'page_fileuploaded','eventparams': 'filename,success', 'filename': data.files[0].name, 'success': false});");
		writer.write("}");
		writer.write("});");    
		writer.write("$(document).on('drop dragover', function (e) {");
		writer.write("e.preventDefault();");
		writer.write("});");    
		writer.write("function formatFileSize(bytes) {");
		writer.write("if (typeof bytes !== 'number') {");
		writer.write("return '';");
		writer.write("}");
		writer.write("if (bytes >= 1000000000) {");
		writer.write("return (bytes / 1000000000).toFixed(2) + ' GB';");
		writer.write("}");
		writer.write("if (bytes >= 1000000) {");
		writer.write("return (bytes / 1000000).toFixed(2) + ' MB';");
		writer.write("}");
		writer.write("return (bytes / 1000).toFixed(2) + ' KB';");
		writer.write("}");
		writer.write("};");
	}
	
	@Hide
	protected void WriteCSSBadge(BufferedWriter writer, ThemeBadge in) throws IOException {
		writer.write("p.badge" + in.ThemeName.toLowerCase() + " {min-width: 3rem;padding:0 6px;text-align:center;font-size:1rem;line-height:inherit;color:" + ABMaterial.GetColorStrMap(in.ForeColor,in.ForeColorIntensity) + ";position:absolute;right:15px;-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;}");
		writer.write("p.badge" + in.ThemeName.toLowerCase() + ".new {font-weight:300;font-size:0.8rem;color:" + ABMaterial.GetColorStrMap(in.NewForeColor,in.NewForeColorIntensity) + ";background-color:" + ABMaterial.GetColorStrMap(in.NewBackColor,in.NewBackColorIntensity) + ";border-radius:2px;line-height:1.8rem;margin-top:2px;}");
		writer.write("p.badge" + in.ThemeName.toLowerCase() + ".new:after {content:\" " + ABMaterial.HTMLConv().htmlEscape(in.NewText, PageCharset) + "\"; }");
		writer.write("nav ul a p.badge" + in.ThemeName.toLowerCase() + " {position:static;margin-left:4px;line-height:0;}");			
	}
	
	@Hide
	protected void WriteCSSButton(BufferedWriter writer, ThemeButton in) throws IOException {		
		writer.write(".dropdown-content-b-" + in.ThemeName.toLowerCase() + " li:hover, .dropdown-content-b" + in.ThemeName.toLowerCase() + " li.active {background-color: " + ABMaterial.GetColorStrMap(in.MenuHoverBackColor,in.MenuHoverBackColorIntensity) + ";}");
	}
	
	@Hide
	protected void WriteCSSNavBar(BufferedWriter writer, ThemeNavigationBar in) throws IOException {		
		writer.write(".dropdown-content-n-" + in.ThemeName.toLowerCase() + " li:hover, .dropdown-content-n" + in.ThemeName.toLowerCase() + " li.active {background-color: " + ABMaterial.GetColorStrMapRGBA(in.TopBarSubHoverBackColor,in.TopBarSubHoverBackColorIntensity, in.TopBarSubHoverOpacity) + "}");
		writer.write(".side-nav" + in.ThemeName.toLowerCase() + " .collapsible-body li:hover, .side-nav" + in.ThemeName.toLowerCase() + ".fixed .collapsible-body li:hover{ background-color:" + ABMaterial.GetColorStrMap(in.SideBarSubHoverBackColor,in.SideBarSubHoverBackColorIntensity) + ";color:" + ABMaterial.GetColorStrMap(in.SideBarSubHoverForeColor,in.SideBarSubHoverForeColorIntensity) + ";}");
		writer.write(".side-nav" + in.ThemeName.toLowerCase() + " .collapsible-body li.active,.side-nav" + in.ThemeName.toLowerCase() + ".fixed .collapsible-body li.active{ background-color:" + ABMaterial.GetColorStrMap(in.SideBarSubActiveBackColor,in.SideBarSubActiveBackColorIntensity) + ";color:" + ABMaterial.GetColorStrMap(in.SideBarSubActiveForeColor,in.SideBarSubActiveForeColorIntensity) + ";}");
    	writer.write(".side-nav" + in.ThemeName.toLowerCase() + " li:hover {background-color: " + ABMaterial.GetColorStrMap(in.SideBarHoverBackColor,in.SideBarHoverBackColorIntensity) + ";color: " + ABMaterial.GetColorStrMap(in.SideBarHoverForeColor,in.SideBarHoverForeColorIntensity) + ";}");
    	writer.write(".side-nav" + in.ThemeName.toLowerCase() + " li.active {background-color: " + ABMaterial.GetColorStrMap(in.SideBarActiveBackColor,in.SideBarActiveBackColorIntensity) + ";color: " + ABMaterial.GetColorStrMap(in.SideBarActiveForeColor,in.SideBarActiveForeColorIntensity) + ";}");
    	writer.write("ul.side-nav" + in.ThemeName.toLowerCase() + ".fixed li:hover {background-color:" + ABMaterial.GetColorStrMap(in.SideBarHoverBackColor,in.SideBarHoverBackColorIntensity) + ";color:" + ABMaterial.GetColorStrMap(in.SideBarHoverForeColor,in.SideBarHoverForeColorIntensity) + ";}");
    	writer.write("ul.side-nav" + in.ThemeName.toLowerCase() + ".fixed li.active {background-color:" + ABMaterial.GetColorStrMap(in.SideBarActiveBackColor,in.SideBarActiveBackColorIntensity) + ";color:" + ABMaterial.GetColorStrMap(in.SideBarActiveForeColor,in.SideBarActiveForeColorIntensity) + ";}");
    	writer.write(".side-nav" + in.ThemeName.toLowerCase() + " .collapsible-body li.active:hover, .side-nav" + in.ThemeName.toLowerCase() + ".fixed .collapsible-body li.active:hover{ background-color:" + ABMaterial.GetColorStrMap(in.SideBarSubActiveHoverBackColor,in.SideBarSubActiveHoverBackColorIntensity) + ";color:" + ABMaterial.GetColorStrMap(in.SideBarSubActiveHoverForeColor,in.SideBarSubActiveHoverForeColorIntensity) + ";}");
		writer.write(".side-nav" + in.ThemeName.toLowerCase() + " li.active:hover {background-color: " + ABMaterial.GetColorStrMap(in.SideBarActiveHoverBackColor,in.SideBarActiveHoverBackColorIntensity) + ";color: " + ABMaterial.GetColorStrMap(in.SideBarActiveHoverForeColor,in.SideBarActiveHoverForeColorIntensity) + ";}");
    	writer.write("ul.side-nav" + in.ThemeName.toLowerCase() + ".fixed li.active:hover {background-color:" + ABMaterial.GetColorStrMap(in.SideBarActiveHoverBackColor,in.SideBarActiveHoverBackColorIntensity) + ";color:" + ABMaterial.GetColorStrMap(in.SideBarActiveHoverForeColor,in.SideBarActiveHoverForeColorIntensity) + ";}");
	}
	
	protected void WriteCSSCalendar(BufferedWriter writer, String id, ThemeCalendar cal) throws IOException {
		writer.write("#" + id + " h2 {font-size: 1.2rem;text-transform: uppercase;line-height: 35px;}");
		writer.write("#" + id + " .fc-day-header {text-transform: uppercase;font-weight: 400;font-size: 0.8rem;}");		
		writer.write("#" + id + " .fc-state-active, .fc-state-down {color: " + ABMaterial.GetColorStrMap(cal.ActiveColor, cal.ActiveColorIntensity) + " !important;} ");
		writer.write("#" + id + " .fc-today {color: " + ABMaterial.GetColorStrMap(cal.ActiveColor, cal.ActiveColorIntensity) + " !important;background-color: transparent !important}");
		writer.write("#" + id + " .fc-widget-content {font-size: 0.8rem}");
	}
	
	@Hide
	protected void WriteCSSInput(BufferedWriter writer, ThemeInput in, String type) throws IOException {
		writer.write(".input-field" + in.ThemeName.toLowerCase() + " input[type="+ type +"]:disabled + label {color:" + ABMaterial.GetColorStrMap(in.DisabledForeColor,in.DisabledForeColorIntensity) + ";}");
		writer.write(".input-field" + in.ThemeName.toLowerCase() + " input[type="+ type +"]:focus + label {color:" + ABMaterial.GetColorStrMap(in.FocusForeColor,in.FocusForeColorIntensity) + ";}");
		writer.write(".input-field" + in.ThemeName.toLowerCase() + " input[type="+ type +"] {border-bottom: 1px solid " + ABMaterial.GetColorStrMap(in.ForeColor,in.ForeColorIntensity) + ";box-shadow: 0 0 0 0 " + ABMaterial.GetColorStrMap(in.ForeColor,in.ForeColorIntensity) + "}");
		writer.write(".input-field" + in.ThemeName.toLowerCase() + " input[type="+ type +"]:focus {border-bottom: 1px solid " + ABMaterial.GetColorStrMap(in.FocusForeColor,in.FocusForeColorIntensity) + ";box-shadow: 0 1px 0 0 " + ABMaterial.GetColorStrMap(in.FocusForeColor,in.FocusForeColorIntensity) + ";background-color: transparent !important;}");
		writer.write(".input-field" + in.ThemeName.toLowerCase() + " input[type="+ type +"].valid {border-bottom: 1px solid " + ABMaterial.GetColorStrMap(in.ValidColor,in.ValidColorIntensity) + ";box-shadow: 0 1px 0 0 " + ABMaterial.GetColorStrMap(in.ValidColor,in.ValidColorIntensity) + ";}");
		writer.write(".input-field" + in.ThemeName.toLowerCase() + " input[type="+ type +"].invalid {border-bottom: 1px solid " + ABMaterial.GetColorStrMap(in.InvalidColor,in.InvalidColorIntensity) + ";box-shadow: 0 1px 0 0 " + ABMaterial.GetColorStrMap(in.InvalidColor,in.InvalidColorIntensity) + ";}");
		writer.write(".input-field" + in.ThemeName.toLowerCase() + " input[type="+ type +"]::-webkit-input-placeholder {color:" + ABMaterial.GetColorStrMap(in.PlaceholderColor,in.PlaceholderColorIntensity)  + ";}");
		writer.write(".input-field" + in.ThemeName.toLowerCase() + " input[type="+ type +"]:-moz-placeholder {color: " + ABMaterial.GetColorStrMap(in.PlaceholderColor,in.PlaceholderColorIntensity)  + ";opacity:  1;}");
		writer.write(".input-field" + in.ThemeName.toLowerCase() + " input[type="+ type +"]::-moz-placeholder {color: " + ABMaterial.GetColorStrMap(in.PlaceholderColor,in.PlaceholderColorIntensity)  + ";opacity:  1;}");
		writer.write(".input-field" + in.ThemeName.toLowerCase() + " input[type="+ type +"]:-ms-input-placeholder {color: " + ABMaterial.GetColorStrMap(in.PlaceholderColor,in.PlaceholderColorIntensity)  + ";}");
		writer.write(".input-field" + in.ThemeName.toLowerCase() + " input[type="+ type +"]::-ms-input-placeholder {color: " + ABMaterial.GetColorStrMap(in.PlaceholderColor,in.PlaceholderColorIntensity)  + ";}");
		if (!in.ExtraStyle.equals("")) {
			writer.write(".input-field" + in.ThemeName.toLowerCase() + " input[type="+ type +"] {" + in.ExtraStyleInput + "}");
		}
	}
		
	@Hide
	protected void WriteCSSInputExtra(BufferedWriter writer, ThemeInput in) throws IOException {
		writer.write(".autocomplete-content" + in.ThemeName.toLowerCase() + " {position: absolute;background: " + ABMaterial.GetColorStrMap(in.AutoCompleteBackColor,in.AutoCompleteBackColorIntensity) + ";margin-top: -.9rem;z-index: 5;max-height:250px;overflow-y:auto }");    
		writer.write(".autocomplete-content" + in.ThemeName.toLowerCase() + " li {clear: both;color: rgba(0, 0, 0, 0.87);cursor: pointer;line-height: 0;width: 100%;text-align: left;text-transform: none;padding: 10px;}");
		writer.write(".autocomplete-content" + in.ThemeName.toLowerCase() + " li > h6 {color: " + ABMaterial.GetColorStrMap(in.AutoCompleteForeColor,in.AutoCompleteForeColorIntensity) + ";display: block;}");
		writer.write(".autocomplete-content" + in.ThemeName.toLowerCase() + " li > h6 .highlight {color: #000000;}");
		writer.write(".autocomplete-content" + in.ThemeName.toLowerCase() + " li img {height: 52px;width: 52px;padding: 5px;margin: 0 15px;}");
		writer.write(".autocomplete-content" + in.ThemeName.toLowerCase() + " li:hover {background: #eee;cursor: pointer;}");
		writer.write(".autocomplete-content" + in.ThemeName.toLowerCase() + " > li:hover {background: " + ABMaterial.GetColorStrMap(in.AutoCompleteHoverBackColor,in.AutoCompleteHoverBackColorIntensity) + ";}");
		writer.write(".autocomplete-content" + in.ThemeName.toLowerCase() + " > li:hover h6 {color:" + ABMaterial.GetColorStrMap(in.AutoCompleteHoverForeColor,in.AutoCompleteHoverForeColorIntensity) + "}");
	}
	
	@Hide
	protected void WriteCSSCombo(BufferedWriter writer, ThemeCombo in) throws IOException {
		writer.write(".combo" + in.ThemeName.toLowerCase() + " label {color: " + ABMaterial.GetColorStrMap(in.ForeColor,in.ForeColorIntensity) + ";width:100%;}");
		writer.write(".combo" + in.ThemeName.toLowerCase() + " input[type=text]:focus + label {color: " + ABMaterial.GetColorStrMap(in.FocusForeColor,in.FocusForeColorIntensity) + ";}");
		writer.write(".combo" + in.ThemeName.toLowerCase() + " input[type=text] {border-bottom: 1px solid " + ABMaterial.GetColorStrMap(in.ForeColor,in.ForeColorIntensity) + ";box-shadow: 0 0 0 0 " + ABMaterial.GetColorStrMap(in.ForeColor,in.ForeColorIntensity) + "}");
		writer.write(".combo" + in.ThemeName.toLowerCase() + " input[type=text]:focus {border-bottom: 1px solid " + ABMaterial.GetColorStrMap(in.FocusForeColor,in.FocusForeColorIntensity) + ";box-shadow: 0 1px 0 0 " + ABMaterial.GetColorStrMap(in.FocusForeColor,in.FocusForeColorIntensity) + "}");
		writer.write(".combo" + in.ThemeName.toLowerCase() + " input[type=text].valid {border-bottom: 1px solid " + ABMaterial.GetColorStrMap(in.ValidColor,in.ValidColorIntensity) + ";box-shadow: 0 1px 0 0 " + ABMaterial.GetColorStrMap(in.ValidColor,in.ValidColorIntensity) + ";}");
		writer.write(".combo" + in.ThemeName.toLowerCase() + " input[type=text].invalid {border-bottom: 1px solid " + ABMaterial.GetColorStrMap(in.InvalidColor,in.InvalidColorIntensity) + ";box-shadow: 0 1px 0 0 " + ABMaterial.GetColorStrMap(in.InvalidColor,in.InvalidColorIntensity) + ";}");
		writer.write(".combo" + in.ThemeName.toLowerCase() + " .prefix.active {color: " + ABMaterial.GetColorStrMap(in.FocusForeColor,in.FocusForeColorIntensity) + ";}");		
		writer.write(".combo" + in.ThemeName.toLowerCase() + " input[type=text].valid + ul + label:after {content: attr(data-success);color: " + ABMaterial.GetColorStrMap(in.ValidColor,in.ValidColorIntensity) + ";opacity: 1;margin-top:5px;}");
		writer.write(".combo" + in.ThemeName.toLowerCase() + " input[type=text].invalid + ul + label:after {content: attr(data-error);color: " + ABMaterial.GetColorStrMap(in.InvalidColor,in.InvalidColorIntensity) + ";opacity: 1;margin-top:5px;}");
		writer.write(".combo" + in.ThemeName.toLowerCase() + " input[type=text] + ul + label:after {display: block;content: \"\";position: absolute;top: 56px;opacity: 0;transition: .2s opacity ease-out, .2s color ease-out;-moz-transition-property:all!important;-o-transition-property:all!important;-webkit-transition-property:all!important;transition-property:all!important;font-size:.8rem;-moz-transform:none;-ms-transform:none;-o-transform:none;-webkit-transform:none;transform:none;top:56px!important}");
		writer.write(".combo" + in.ThemeName.toLowerCase() + " input[type=text] + ul + label:not(.active):after {-moz-transform:translateY(-160%);-ms-transform:translateY(-160%);-o-transform:translateY(-160%);-webkit-transform:translateY(-160%);transform:translateY(-160%);}");
		writer.write(".combo" + in.ThemeName.toLowerCase() + " {border-radius: " + in.BorderRadiusPx + "px;}");
		writer.write(".combo" + in.ThemeName.toLowerCase() + ".active {background-color: " + ABMaterial.GetColorStrMap(in.FocusBackColor,in.FocusBackColorIntensity) + " !important;}");
		writer.write(".combo" + in.ThemeName.toLowerCase() + " input[type=text]::-webkit-input-placeholder {color:" + ABMaterial.GetColorStrMap(in.PlaceholderColor,in.PlaceholderColorIntensity)  + ";}");
		writer.write(".combo" + in.ThemeName.toLowerCase() + " input[type=text]:-moz-placeholder {color: " + ABMaterial.GetColorStrMap(in.PlaceholderColor,in.PlaceholderColorIntensity)  + ";opacity:  1;}");
		writer.write(".combo" + in.ThemeName.toLowerCase() + " input[type=text]::-moz-placeholder {color: " + ABMaterial.GetColorStrMap(in.PlaceholderColor,in.PlaceholderColorIntensity)  + ";opacity:  1;}");
		writer.write(".combo" + in.ThemeName.toLowerCase() + " input[type=text]:-ms-input-placeholder {color: " + ABMaterial.GetColorStrMap(in.PlaceholderColor,in.PlaceholderColorIntensity)  + ";}");
		writer.write(".combo" + in.ThemeName.toLowerCase() + " input[type=text]::-ms-input-placeholder {color: " + ABMaterial.GetColorStrMap(in.PlaceholderColor,in.PlaceholderColorIntensity)  + ";}");
	}
	
	@Hide
	protected void WriteCSSTable(BufferedWriter writer, ThemeTable in) throws IOException {
		for (Entry<String,TableCell> tx: in.Cells.entrySet()) {
			TableCell tc = tx.getValue();
			StringBuilder s=new StringBuilder();
			if (tc.MarginTop!="") s.append(";margin-top:" + tc.MarginTop);
			if (tc.MarginBottom!="") s.append(";margin-bottom: " + tc.MarginBottom);
			if (tc.MarginLeft!="") s.append(";margin-left: " + tc.MarginLeft);
			if (tc.MarginRight!="") s.append(";margin-right: " + tc.MarginRight);
			if (tc.PaddingTop!="") s.append(";padding-top: " + tc.PaddingTop);
			if (tc.PaddingBottom!="") s.append(";padding-bottom: " + tc.PaddingBottom);
			if (tc.PaddingLeft!="") s.append(";padding-left: " + tc.PaddingLeft);
			if (tc.PaddingRight!="") s.append(";padding-right: " + tc.PaddingRight);
			writer.write(".tblcell" + in.ThemeName.toLowerCase() + "_" + tc.ThemeName.toLowerCase() + " {background-color: " + ABMaterial.GetColorStrMap(tc.BackColor,tc.BackColorIntensity) + s.toString() + "}");
			writer.write(".tblcell" + in.ThemeName.toLowerCase() + "_" + tc.ThemeName.toLowerCase() + ".selected {background-color: " + ABMaterial.GetColorStrMap(tc.ActiveBackColor,tc.ActiveBackColorIntensity) + s.toString() + "}");
			writer.write(".strikethrough" + in.ThemeName.toLowerCase() + "_" + tc.ThemeName.toLowerCase() + ".strikethrough {position: relative;}.strikethrough" + in.ThemeName.toLowerCase() + "_" + tc.ThemeName.toLowerCase() + ".strikethrough:before {position: absolute;content: \"\";left: 0;top: 48%;right: 0;border-top: 3px solid " + ABMaterial.GetColorStrMapRGBA(tc.StrikethroughColor, tc.StrikethroughColorIntensity, 0.5) + ";}");
			
			if (!tc.SameValueForeColor.equals("")) {
				writer.write(".tblcell" + in.ThemeName.toLowerCase() + "_" + tc.ThemeName.toLowerCase() + ".abmtmsame:not(.selected) {color: transparent !important}");
				writer.write(".tblcell" + in.ThemeName.toLowerCase() + "_" + tc.ThemeName.toLowerCase() + ".abmtmsame.abmtmsameactive:not(.selected) {color: " + ABMaterial.GetColorStrMap(tc.SameValueForeColor,tc.SameValueForeColorIntensity) + " !important}");
			}
			if (!tc.WarningBulletColor.equals(ABMaterial.COLOR_TRANSPARENT)) {
				writer.write(".tblcell" + in.ThemeName.toLowerCase() + "_" + tc.ThemeName.toLowerCase() + ":after {content: ' \\25CF';color: " + ABMaterial.GetColorStrMap(tc.WarningBulletColor,tc.WarningBulletColorIntensity) + ";}");
				writer.write(".tblcell" + in.ThemeName.toLowerCase() + "_" + tc.ThemeName.toLowerCase() + ".selected:after {content: ' \\25CF';color: " + ABMaterial.GetColorStrMap(tc.WarningBulletColor,tc.WarningBulletColorIntensity) + ";}");
			}
		}				
	}
	
	@Hide
	protected void WriteCSSInputArea(BufferedWriter writer, ThemeInput in) throws IOException {
		writer.write("textarea:not([readonly]).materialize-textarea" + in.ThemeName.toLowerCase() + ":disabled + label {color: " + ABMaterial.GetColorStrMap(in.DisabledForeColor,in.DisabledForeColorIntensity) + ";width:100%;}");
		writer.write("textarea:not([readonly]).materialize-textarea" + in.ThemeName.toLowerCase() + " label {color: " + ABMaterial.GetColorStrMap(in.ForeColor,in.ForeColorIntensity) + ";width:100%;}");			 
		writer.write("textarea:not([readonly]).materialize-textarea" + in.ThemeName.toLowerCase() + ":focus + label {color: " + ABMaterial.GetColorStrMap(in.FocusForeColor,in.FocusForeColorIntensity) + ";}");			
		writer.write("textarea:not([readonly]).materialize-textarea" + in.ThemeName.toLowerCase() + " {border-bottom: 1px solid " + ABMaterial.GetColorStrMap(in.ForeColor,in.ForeColorIntensity) + ";box-shadow: 0 0 0 0 " + ABMaterial.GetColorStrMap(in.ForeColor,in.ForeColorIntensity) + ";}");
		writer.write("textarea:not([readonly]).materialize-textarea" + in.ThemeName.toLowerCase() + ":focus {border-bottom: 1px solid " + ABMaterial.GetColorStrMap(in.FocusForeColor,in.FocusForeColorIntensity) + ";box-shadow: 0 1px 0 0 " + ABMaterial.GetColorStrMap(in.FocusForeColor,in.FocusForeColorIntensity) + ";}");
		writer.write("textarea:not([readonly]).materialize-textarea" + in.ThemeName.toLowerCase() + " .valid {border-bottom: 1px solid " + ABMaterial.GetColorStrMap(in.ValidColor,in.ValidColorIntensity) + ";box-shadow: 0 1px 0 0 " + ABMaterial.GetColorStrMap(in.ValidColor,in.ValidColorIntensity) + ";}");
		writer.write("textarea:not([readonly]).materialize-textarea" + in.ThemeName.toLowerCase() + " .invalid {border-bottom: 1px solid " + ABMaterial.GetColorStrMap(in.InvalidColor,in.InvalidColorIntensity) + ";box-shadow: 0 1px 0 0 " + ABMaterial.GetColorStrMap(in.InvalidColor,in.InvalidColorIntensity) + ";}");
		writer.write("textarea:not([readonly]).materialize-textarea" + in.ThemeName.toLowerCase() + " .prefix.active {color: " + ABMaterial.GetColorStrMap(in.FocusForeColor,in.FocusForeColorIntensity) + ";}");		
		writer.write("textarea:not([readonly]).materialize-textarea" + in.ThemeName.toLowerCase() + "::-webkit-input-placeholder {color:" + ABMaterial.GetColorStrMap(in.PlaceholderColor,in.PlaceholderColorIntensity)  + ";}");
		writer.write("textarea:not([readonly]).materialize-textarea" + in.ThemeName.toLowerCase() + ":-moz-placeholder {color: " + ABMaterial.GetColorStrMap(in.PlaceholderColor,in.PlaceholderColorIntensity)  + ";opacity:  1;}");
		writer.write("textarea:not([readonly]).materialize-textarea" + in.ThemeName.toLowerCase() + "::-moz-placeholder {color: " + ABMaterial.GetColorStrMap(in.PlaceholderColor,in.PlaceholderColorIntensity)  + ";opacity:  1;}");
		writer.write("textarea:not([readonly]).materialize-textarea" + in.ThemeName.toLowerCase() + ":-ms-input-placeholder {color: " + ABMaterial.GetColorStrMap(in.PlaceholderColor,in.PlaceholderColorIntensity)  + ";}");
		writer.write("textarea:not([readonly]).materialize-textarea" + in.ThemeName.toLowerCase() + "::-ms-input-placeholder {color: " + ABMaterial.GetColorStrMap(in.PlaceholderColor,in.PlaceholderColorIntensity)  + ";}");
		writer.write("textarea:not([readonly]).materialize-textarea" + in.ThemeName.toLowerCase() + " {" + in.ExtraStyleInput + "}");
	}
	
	@Hide
	protected void WriteCSSCheckbox(BufferedWriter writer, ThemeCheckbox ch) throws IOException {
		writer.write(".checkui" + ch.ThemeName.toLowerCase() + " [type=\"checkbox\"] + label {position:relative;padding-left:35px;cursor:pointer;display:inline-block;height:25px;line-height:" + ch.LineHeight + ";font-size: " + ch.FontSize + "; -webkit-user-select: none; -moz-user-select: none; -khtml-user-select: none; -ms-user-select: none; color: " + ABMaterial.GetColorStrMap(ch.LabelColor,ch.LabelColorIntensity) + "}");
		writer.write(".checkui" + ch.ThemeName.toLowerCase() + " [type=\"checkbox\"]:disabled + label {color: " + ABMaterial.GetColorStrMap(ch.DisabledLabelColor,ch.DisabledLabelColorIntensity) + "}");
		writer.write(".checkui" + ch.ThemeName.toLowerCase() + " [type=\"checkbox\"] + label:before { content: ''; position: absolute; top: " + ch.BoxTopPx + "px; left: 0; width: 18px; height: 18px; z-index: 0; border: 2px solid " + ABMaterial.GetColorStrMap(ch.BorderColor,ch.BorderColorIntensity) + "; border-radius: 1px; margin-top: 2px; -webkit-transition: 0.2s; -moz-transition: 0.2s; -o-transition: 0.2s; -ms-transition: 0.2s; transition: 0.2s; }");
		
		writer.write(".checkui" + ch.ThemeName.toLowerCase() + " [type=\"checkbox\"]:not(:checked):disabled + label:before { background-color: transparent;border: 2px solid " + ABMaterial.GetColorStrMap(ch.DisabledBorderColor,ch.DisabledBorderColorIntensity) + "; }");
		writer.write(".checkui" + ch.ThemeName.toLowerCase() + " [type=\"checkbox\"]:checked + label:before { top: " + (ch.BoxTopPx-4) + "px; left: -3px; width: 12px; height: 22px; border-top: 2px solid transparent; border-left: 2px solid transparent; border-right: 2px solid " + ABMaterial.GetColorStrMap(ch.CheckedColor,ch.CheckedColorIntensity) + "; border-bottom: 2px solid " + ABMaterial.GetColorStrMap(ch.CheckedColor,ch.CheckedColorIntensity) + "; -webkit-transform: rotate(40deg); -moz-transform: rotate(40deg); -ms-transform: rotate(40deg); -o-transform: rotate(40deg); transform: rotate(40deg); -webkit-backface-visibility: hidden; -webkit-transform-origin: 100% 100%; -moz-transform-origin: 100% 100%; -ms-transform-origin: 100% 100%; -o-transform-origin: 100% 100%; transform-origin: 100% 100%; }");
		
		writer.write(".checkui" + ch.ThemeName.toLowerCase() + " [type=\"checkbox\"]:checked:disabled + label:before { border-right: 2px solid " + ABMaterial.GetColorStrMap(ch.DisabledBorderColor,ch.DisabledBorderColorIntensity) + "; border-bottom: 2px solid " + ABMaterial.GetColorStrMap(ch.DisabledBorderColor,ch.DisabledBorderColorIntensity) + "; }");
		
		writer.write(".checkui" + ch.ThemeName.toLowerCase() + " [type=\"checkbox\"]:indeterminate + label:before { left: -10px; top: -11px; width: 10px; height: 22px; border-top: none; border-left: none; border-right: 2px solid " + ABMaterial.GetColorStrMap(ch.CheckedColor,ch.CheckedColorIntensity) + "; border-bottom: none; -webkit-transform: rotate(90deg); -moz-transform: rotate(90deg); -ms-transform: rotate(90deg); -o-transform: rotate(90deg); transform: rotate(90deg); -webkit-backface-visibility: hidden; -webkit-transform-origin: 100% 100%; -moz-transform-origin: 100% 100%; -ms-transform-origin: 100% 100%; -o-transform-origin: 100% 100%; transform-origin: 100% 100%; }");
		writer.write(".checkui" + ch.ThemeName.toLowerCase() + " [type=\"checkbox\"]:indeterminate:disabled + label:before { border-right: 2px solid " + ABMaterial.GetColorStrMap(ch.DisabledBorderColor,ch.DisabledBorderColorIntensity) + "; background-color: transparent; }");
		
		writer.write(".checkui" + ch.ThemeName.toLowerCase() + " [type=\"checkbox\"].filled-in + label:after { border-radius: 2px; }");
		writer.write(".checkui" + ch.ThemeName.toLowerCase() + " [type=\"checkbox\"].filled-in + label:before, [type=\"checkbox\"].filled-in + label:after { content: ''; left: 0; position: absolute; transition: border .25s, background-color .25s, width .2s .1s, height .2s .1s, top .2s .1s, left .2s .1s; z-index: 1; }");
		writer.write(".checkui" + ch.ThemeName.toLowerCase() + " [type=\"checkbox\"].filled-in:not(:checked) + label:before { width: 0; height: 0; border: 2px solid transparent; left: 6px; top: 10px; -webkit-transform: rotateZ(37deg); transform: rotateZ(37deg); -webkit-transform-origin: 20% 40%; transform-origin: 100% 100%; }");
		writer.write(".checkui" + ch.ThemeName.toLowerCase() + " [type=\"checkbox\"].filled-in:not(:checked) + label:after { height: 20px; width: 20px; background-color: transparent; border: 2px solid " + ABMaterial.GetColorStrMap(ch.BorderColor,ch.BorderColorIntensity) + "; top: 0px; z-index: 0; }");
		writer.write(".checkui" + ch.ThemeName.toLowerCase() + " [type=\"checkbox\"].filled-in:checked + label:before { top: 0; left: 1px; width: 8px; height: 13px; border-top: 2px solid transparent; border-left: 2px solid transparent; border-right: 2px solid " + ABMaterial.GetColorStrMap(ch.CheckedFilledForeColor,ch.CheckedFilledForeColorIntensity) + "; border-bottom: 2px solid " + ABMaterial.GetColorStrMap(ch.CheckedFilledForeColor,ch.CheckedFilledForeColorIntensity) + "; -webkit-transform: rotateZ(37deg); transform: rotateZ(37deg); -webkit-transform-origin: 100% 100%; transform-origin: 100% 100%; }");
		writer.write(".checkui" + ch.ThemeName.toLowerCase() + " [type=\"checkbox\"].filled-in:checked + label:after { top: 0px; width: 20px; height: 20px; border: 2px solid " + ABMaterial.GetColorStrMap(ch.CheckedColor,ch.CheckedColorIntensity) + "; background-color: " + ABMaterial.GetColorStrMap(ch.CheckedColor,ch.CheckedColorIntensity) + "; z-index: 0; }");
		
		writer.write(".checkui" + ch.ThemeName.toLowerCase() + " [type=\"checkbox\"].filled-in:disabled:not(:checked) + label:before { background-color: transparent; border: 2px solid transparent; }");
		writer.write(".checkui" + ch.ThemeName.toLowerCase() + " [type=\"checkbox\"].filled-in:disabled:not(:checked) + label:after { border-color: transparent; background-color: " + ABMaterial.GetColorStrMap(ch.DisabledBorderColor,ch.DisabledBorderColorIntensity) + "; }");
		writer.write(".checkui" + ch.ThemeName.toLowerCase() + " [type=\"checkbox\"].filled-in:disabled:checked + label:before { background-color: transparent;  top: 0; left: 1px; width: 8px; height: 13px; border-top: 2px solid transparent; border-left: 2px solid transparent; border-right: 2px solid " + ABMaterial.GetColorStrMap(ch.DisabledCheckedFilledForeColor,ch.DisabledCheckedFilledForeColorIntensity) + "; border-bottom: 2px solid " + ABMaterial.GetColorStrMap(ch.DisabledCheckedFilledForeColor,ch.DisabledCheckedFilledForeColorIntensity) + "; -webkit-transform: rotateZ(37deg); transform: rotateZ(37deg); -webkit-transform-origin: 100% 100%; transform-origin: 100% 100%; }");
		writer.write(".checkui" + ch.ThemeName.toLowerCase() + " [type=\"checkbox\"].filled-in:disabled:checked + label:after { background-color: " + ABMaterial.GetColorStrMap(ch.DisabledCheckedColor,ch.DisabledCheckedColorIntensity) + "; border-color: " + ABMaterial.GetColorStrMap(ch.DisabledCheckedColor,ch.DisabledCheckedColorIntensity) + "; }");
	}
	
	@Hide
	protected void WriteCSSImageSlider(BufferedWriter writer, ThemeImageSlider sl) throws IOException {
		writer.write(".slider" + sl.ThemeName.toLowerCase() + " { position: relative; height: " + sl.Height + "px; width: 100%; background: " + ABMaterial.GetColorStrMap(sl.BackColor, sl.BackColorIntensity)  + " }");
		writer.write(".slider" + sl.ThemeName.toLowerCase() + ".fullscreen { height: 100%; width: 100%; position: absolute; top: 0; left: 0; right: 0; bottom: 0; background: " + ABMaterial.GetColorStrMap(sl.BackColor, sl.BackColorIntensity)  + " }");
		writer.write(".slider" + sl.ThemeName.toLowerCase() + ".fullscreen ul.slides { height: 100%; }");
		writer.write(".slider" + sl.ThemeName.toLowerCase() + ".fullscreen ul.indicators { z-index: 2; bottom: 30px; }");
		writer.write(".slider" + sl.ThemeName.toLowerCase() + " .slides { background-color: #9e9e9e; margin: 0; height: " + sl.Height + "px; }");
		writer.write(".slider" + sl.ThemeName.toLowerCase() + " .slides li { opacity: 0; position: absolute; top: 0; left: 0; z-index: 1; width: 100%; height: inherit; overflow: hidden; }");
		writer.write(".slider" + sl.ThemeName.toLowerCase() + " .slides li img { height: 100%; width: 100%; background-size: cover; background-position: center; }");
		writer.write(".slider" + sl.ThemeName.toLowerCase() + " .slides li .caption { color: #fff; position: absolute; top: 15%; left: 15%; width: 70%; opacity: 0; }");
		writer.write(".slider" + sl.ThemeName.toLowerCase() + " .slides li .caption p { color: #e0e0e0; }");
		writer.write(".slider" + sl.ThemeName.toLowerCase() + " .slides li.active { z-index: 2; }");
		writer.write(".slider" + sl.ThemeName.toLowerCase() + " .indicators { position: absolute; text-align: center; left: 0; right: 0; bottom: 0; margin: 0; }");
		writer.write(".slider" + sl.ThemeName.toLowerCase() + " .indicators .indicator-item { display: inline-block; position: relative; cursor: pointer; height: 16px; width: 16px; margin: 0 12px; background-color: " + ABMaterial.GetColorStrMap(sl.InactiveBulletColor,sl.InactiveBulletColorIntensity) + "; -webkit-transition: background-color .3s; -moz-transition: background-color .3s; -o-transition: background-color .3s; -ms-transition: background-color .3s; transition: background-color .3s; border-radius: 50%; }");
		writer.write(".slider" + sl.ThemeName.toLowerCase() + " .indicators .indicator-item.active { background-color: " + ABMaterial.GetColorStrMap(sl.ActiveBulletColor,sl.ActiveBulletColorIntensity) + "; }");
	}
	
	@Hide
	protected void WriteCSSSwitch(BufferedWriter writer, ThemeSwitch st) throws IOException {
		writer.write(".switch" + st.ThemeName.toLowerCase() + ", .switch" + st.ThemeName.toLowerCase() + " * {-webkit-user-select: none; -moz-user-select: none; -khtml-user-select: none; -ms-user-select: none; }");
		writer.write(".switch" + st.ThemeName.toLowerCase() + " label {cursor: pointer;font-size: " + st.LabelFontSize + "}");
		writer.write(".switch" + st.ThemeName.toLowerCase() + " label input[type=checkbox] {opacity: 0; width: 0; height: 0; }");
		writer.write(".switch" + st.ThemeName.toLowerCase() + " label input[type=checkbox]:checked + .lever" + st.ThemeName.toLowerCase() + " {background-color: " + ABMaterial.GetColorStrMap(st.BarOnColor,st.BarOnColorIntensity) + "; }");
		writer.write(".switch" + st.ThemeName.toLowerCase() + " label input[type=checkbox]:checked + .lever" + st.ThemeName.toLowerCase() + ":after {background-color: " + ABMaterial.GetColorStrMap(st.SwitchOnColor,st.SwitchOnColorIntensity) + "; }");
		writer.write(".switch" + st.ThemeName.toLowerCase() + " label .lever" + st.ThemeName.toLowerCase() + " {content: \"\"; display: inline-block; position: relative; width: 40px; height: 15px; background-color: " + ABMaterial.GetColorStrMap(st.BarOffColor,st.BarOffColorIntensity) + "; border-radius: 15px; margin-right: 10px; transition: background 0.3s ease; vertical-align: middle; margin: 0 16px; }");
		writer.write(".switch" + st.ThemeName.toLowerCase() + " label .lever" + st.ThemeName.toLowerCase() + ":after {content: \"\"; position: absolute; display: inline-block; width: 21px; height: 21px; background-color: " + ABMaterial.GetColorStrMap(st.SwitchOffColor,st.SwitchOffColorIntensity) + "; border-radius: 21px; box-shadow: 0 1px 3px 1px rgba(0, 0, 0, 0.4); left: -5px; top: -3px; transition: left 0.3s ease, background .3s ease, box-shadow 0.1s ease; }");
		writer.write(".switch" + st.ThemeName.toLowerCase() + " label input[type=checkbox]:checked + .lever" + st.ThemeName.toLowerCase() + ":after {left: 24px; }");
		writer.write(".switch" + st.ThemeName.toLowerCase() + " input[type=checkbox][disabled] + .lever" + st.ThemeName.toLowerCase() + " {cursor: default; }");
		writer.write(".switch" + st.ThemeName.toLowerCase() + " label input[type=checkbox][disabled] + .lever" + st.ThemeName.toLowerCase() + ":after, .switch" + st.ThemeName.toLowerCase() + " label input[type=checkbox][disabled]:checked + .lever" + st.ThemeName.toLowerCase() + ":after { background-color: #BDBDBD; }");
	}
	
	@Hide
	protected void WriteCSSRadioGroup(BufferedWriter writer, ThemeRadioGroup rg) throws IOException {
		writer.write(".radiotitle" + rg.ThemeName.toLowerCase() + " {color: " + ABMaterial.GetColorStrMap(rg.TitleColor,rg.TitleColorIntensity) + "}");
		writer.write(".radiotitle" + rg.ThemeName.toLowerCase() + "disabled {color: " + ABMaterial.GetColorStrMap(rg.DisabledTitleColor,rg.DisabledTitleColorIntensity) + "}");
		writer.write(".radio" + rg.ThemeName.toLowerCase() + "[type=\"radio\"]:not(:checked) + label, .radio" + rg.ThemeName.toLowerCase() + "[type=\"radio\"]:checked + label { position: relative; padding-left: 35px; cursor: pointer; display: inline-block; height: 25px; line-height: 25px; font-size: 1rem; -webkit-transition: .28s ease; -moz-transition: .28s ease; -o-transition: .28s ease; -ms-transition: .28s ease; transition: .28s ease; -webkit-user-select: none; -moz-user-select: none; -khtml-user-select: none; -ms-user-select: none; }");
		writer.write(".radio" + rg.ThemeName.toLowerCase() + "[type=\"radio\"] + label:before, .radio" + rg.ThemeName.toLowerCase() + "[type=\"radio\"] + label:after { content: ''; position: absolute; left: 0; top: 0; margin: 4px; width: 16px; height: 16px; z-index: 0; -webkit-transition: .28s ease; -moz-transition: .28s ease; -o-transition: .28s ease; -ms-transition: .28s ease; transition: .28s ease; }");
		writer.write(".radio" + rg.ThemeName.toLowerCase() + "[type=\"radio\"]:not(:checked) + label:before { border-radius: 50%; border: 2px solid " + ABMaterial.GetColorStrMap(rg.BorderColor,rg.BorderColorIntensity) + "; }");
		writer.write(".radio" + rg.ThemeName.toLowerCase() + "[type=\"radio\"]:not(:checked) + label:after { border-radius: 50%; border: 2px solid " + ABMaterial.GetColorStrMap(rg.BorderColor,rg.BorderColorIntensity) + "; z-index: -1; -webkit-transform: scale(0); -moz-transform: scale(0); -ms-transform: scale(0); -o-transform: scale(0); transform: scale(0); }");
		writer.write(".radio" + rg.ThemeName.toLowerCase() + "[type=\"radio\"]:checked + label:before { border-radius: 50%; border: 2px solid transparent; }");
		writer.write(".radio" + rg.ThemeName.toLowerCase() + "[type=\"radio\"]:checked + label:after { border-radius: 50%; border: 2px solid " + ABMaterial.GetColorStrMap(rg.RadioOnColor,rg.RadioOnColorIntensity) + "; background-color: " + ABMaterial.GetColorStrMap(rg.RadioOnColor,rg.RadioOnColorIntensity) + "; z-index: 0; -webkit-transform: scale(1.02); -moz-transform: scale(1.02); -ms-transform: scale(1.02); -o-transform: scale(1.02); transform: scale(1.02); }");
		writer.write(".radio" + rg.ThemeName.toLowerCase() + "[type=\"radio\"].with-gap:checked + label:before { border-radius: 50%; border: 2px solid " + ABMaterial.GetColorStrMap(rg.RadioOnColor,rg.RadioOnColorIntensity) + "; }");
		writer.write(".radio" + rg.ThemeName.toLowerCase() + "[type=\"radio\"].with-gap:checked + label:after { border-radius: 50%; border: 2px solid " + ABMaterial.GetColorStrMap(rg.RadioOnColor,rg.RadioOnColorIntensity) + "; background-color: " + ABMaterial.GetColorStrMap(rg.RadioOnColor,rg.RadioOnColorIntensity) + "; z-index: 0; -webkit-transform: scale(.5); -moz-transform: scale(.5); -ms-transform: scale(.5); -o-transform: scale(.5); transform: scale(.5); }");
		writer.write(".radio" + rg.ThemeName.toLowerCase() + "[type=\"radio\"].with-gap:disabled:checked + label:before { border: 2px solid " + ABMaterial.GetColorStrMap(rg.DisabledBorderColor,rg.DisabledBorderColorIntensity) + "; }");
		writer.write(".radio" + rg.ThemeName.toLowerCase() + "[type=\"radio\"].with-gap:disabled:checked + label:after { border: none; background-color: rgba(0, 0, 0, 0.26); }");
		writer.write(".radio" + rg.ThemeName.toLowerCase() + "[type=\"radio\"]:disabled:not(:checked) + label:before, { background-color: transparent; border-color: " + ABMaterial.GetColorStrMap(rg.DisabledBorderColor,rg.DisabledBorderColorIntensity) + "; }");
		writer.write(".radio" + rg.ThemeName.toLowerCase() + "[type=\"radio\"]:disabled:checked + label:before { background-color: " + ABMaterial.GetColorStrMap(rg.DisabledRadioOnColor,rg.DisabledRadioOnColorIntensity) + "; border-color: " + ABMaterial.GetColorStrMap(rg.DisabledBorderColor,rg.DisabledBorderColorIntensity) + "; }");
		writer.write(".radio" + rg.ThemeName.toLowerCase() + "[type=\"radio\"]:disabled + label { color: " + ABMaterial.GetColorStrMap(rg.DisabledLabelColor,rg.DisabledLabelColorIntensity) + " !important; }");
		writer.write(".radio" + rg.ThemeName.toLowerCase() + "[type=\"radio\"]:disabled:not(:checked) + label:before { background-color: transparent;border-color: " + ABMaterial.GetColorStrMap(rg.DisabledBorderColor,rg.DisabledBorderColorIntensity) + "; }");
		writer.write(".radio" + rg.ThemeName.toLowerCase() + "[type=\"radio\"]:disabled:checked + label:after { background-color: " + ABMaterial.GetColorStrMap(rg.DisabledRadioOnColor,rg.DisabledRadioOnColorIntensity) + "; border-color: " + ABMaterial.GetColorStrMap(rg.DisabledBorderColor,rg.DisabledBorderColorIntensity) + "; }");
	}
	
}
