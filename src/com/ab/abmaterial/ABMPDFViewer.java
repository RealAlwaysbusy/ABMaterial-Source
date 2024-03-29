package com.ab.abmaterial;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;

@Author("Alain Bailleul")  
@ShortName("ABMPDFViewer")
public class ABMPDFViewer extends ABMComponent{
	private static final long serialVersionUID = -8262939164237579960L;
	protected ThemePDFViewer Theme=new ThemePDFViewer();
	protected String mDocumentURL="";
	protected int mHeightPx=0;
	protected boolean IsDirty=false;
	
	public String ReadDirection="ltr";
	public boolean AllowViewBookmark=false;;
	public boolean AllowPrint=true;
	public boolean AllowDownload=true;
	public boolean AllowSideBar=true;
	public boolean AllowOpen=false;
	public String PreparePrintingText="Preparing document for printing...";
	public String ForcedLanguage="";
		
	public void Initialize(ABMPage page, String id, int heightPx, String documentURL, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_PDFVIEWER;
		this.mHeightPx=heightPx;
		this.mDocumentURL=documentURL;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.PDFViewers.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.PDFViewers.get(themeName.toLowerCase()).Clone();				
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
		return ArrayName.toLowerCase() + ID.toLowerCase() + "-parent";
	}
	
	public void SetDocument(String documentURL) {
		this.mDocumentURL = documentURL;
		this.IsDirty=true;
	}
	
		
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}	
		
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.PDFViewers.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.PDFViewers.get(themeName.toLowerCase()).Clone();				
			}
		}
	}		
		
	@Override
	protected void CleanUp() {
		super.CleanUp();
	}
	
	@Override
	protected void RemoveMe() {
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent");
	}
	
	@Override
	protected void FirstRun() {
		super.FirstRun();
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		super.Refresh();
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		j.SetProp("class", BuildClass());
		if (IsDirty) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "src", "./viewer" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + ".html?file=" + mDocumentURL);
		}
		IsDirty=false;
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("PDFViewer Refresh: " + ID);}
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
		
		if (Page.ws!=null) {
			String sFile= "";
			if (Page.mStaticFilesFolder.equals("")) {
				sFile = anywheresoftware.b4a.objects.streams.File.getDirApp().replace("\\", "/") + Page.ws.getUpgradeRequest().getRequestURI().replace("/ws/", "/www/");
			} else {
				sFile = Page.mStaticFilesFolder + "/" + Page.mAppName + "/" + Page.Name; 
			}
			File f = new File(sFile);
			Page.CurrentDir = f.getAbsolutePath();
		}
		
		String viewer = WriteViewer();
		BufferedWriter writer = null;
        try {        	        	
        	File buildFile = new File(Page.CurrentDir, "viewer" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + ".html");
        	
            writer = new BufferedWriter(new FileWriter(buildFile));
            writer.write(viewer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
        if (ABMaterial.GZip) {        	
        	File buildFile = new File(Page.CurrentDir, "viewer" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + ".html.gz");
        	buildFile.delete();
        	buildFile = new File(Page.CurrentDir, "viewer" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + ".html");
        	if (buildFile.length()>ABMaterial.minGZipSize) {
        		Page.GzipIt(buildFile.getAbsolutePath());
        	}
        }
		
		s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent\" class=\"");
		s.append(BuildClass());
		s.append("\">");
		s.append("<iframe id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"width:100%;height:" + mHeightPx + "px;border: 0px\" src=\"./viewer" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + ".html?file=" + mDocumentURL + "\"></iframe>");
		s.append("</div>");
		IsBuild=true;
		return s.toString();
	}
	
	protected String WriteViewer() {
		StringBuilder s = new StringBuilder();
		s.append("<!DOCTYPE html>\n");
		
		String AllowViewBookmarkStr="";
		String AllowPrintStr="";
		String AllowDownloadStr="";
		String AllowSideBarStr="";
		String AllowOpenStr="";
		if (!AllowViewBookmark) {
			AllowViewBookmarkStr=" hidden ";
		}
		if (!AllowPrint) {
			AllowPrintStr=" hidden ";
		}
		if (!AllowDownload) {
			AllowDownloadStr=" hidden ";
		}
		if (!AllowSideBar) {
			AllowSideBarStr=" hidden ";
		}
		if (!AllowOpen) {
			AllowOpenStr=" hidden ";
		}
		String lang="";
		if (!ForcedLanguage.equals("")) {
			lang = " forcedlang=\"" + ForcedLanguage + "\"";
		}
		
		if (ABMaterial.CDN) {
			s.append("<html dir=\"" + ReadDirection + "\" mozdisallowselectionprint moznomarginboxes> <head> <meta charset=\"utf-8\"> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\"> <meta name=\"google\" content=\"notranslate\"> <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"> <title>ABMPDFViewer</title> <link rel=\"stylesheet\" href=\"" + ABMaterial.mCDNUrl + "js/web/viewer.min.css\"/> <script src=\"" + ABMaterial.mCDNUrl + "js/web/compatibility.js\"></script>	<link rel=\"resource\" type=\"application/l10n\" href=\"" + ABMaterial.mCDNUrl + "js/web/locale/locale.properties\"/>	<script src=\"" + ABMaterial.mCDNUrl + "js/web/l10n.js\"></script>	<script src=\"" + ABMaterial.mCDNUrl + "js/build/pdf.min.js\"></script>	<script src=\"" + ABMaterial.mCDNUrl + "js/web/debugger.min.js\"></script>	<script src=\"" + ABMaterial.mCDNUrl + "js/web/viewer.min.js\"></script> </head> <body tabindex=\"1\" class=\"loadingInProgress\"> <script>PDFJS.workerSrc='" + ABMaterial.mCDNUrl + "js/build/pdf.worker.min.js';</script><div id=\"outerContainer\"" + lang + ">	 <div id=\"sidebarContainer\"> <div id=\"toolbarSidebar\"> <div class=\"splitToolbarButton toggled\"> <button id=\"viewThumbnail\" class=\"toolbarButton group toggled\" title=\"Show Thumbnails\" tabindex=\"2\" data-l10n-id=\"thumbs\"> <span data-l10n-id=\"thumbs_label\">Thumbnails</span> </button> <button id=\"viewOutline\" class=\"toolbarButton group\" title=\"Show Document Outline\" tabindex=\"3\" data-l10n-id=\"outline\"> <span data-l10n-id=\"outline_label\">Document Outline</span> </button> <button id=\"viewAttachments\" class=\"toolbarButton group\" title=\"Show Attachments\" tabindex=\"4\" data-l10n-id=\"attachments\"> <span data-l10n-id=\"attachments_label\">Attachments</span> </button> </div> </div> <div id=\"sidebarContent\"> <div id=\"thumbnailView\"> </div> <div id=\"outlineView\" class=\"hidden\"> </div> <div id=\"attachmentsView\" class=\"hidden\"> </div> </div> </div> <div id=\"mainContainer\">	 <div class=\"findbar hidden doorHanger hiddenSmallView\" id=\"findbar\"> <label for=\"findInput\" class=\"toolbarLabel\" data-l10n-id=\"find_label\">Find:</label> <input id=\"findInput\" class=\"toolbarField\" tabindex=\"91\"> <div class=\"splitToolbarButton\"> <button class=\"toolbarButton findPrevious\" title=\"\" id=\"findPrevious\" tabindex=\"92\" data-l10n-id=\"find_previous\"> <span data-l10n-id=\"find_previous_label\">Previous</span> </button> <button class=\"toolbarButton findNext\" title=\"\" id=\"findNext\" tabindex=\"93\" data-l10n-id=\"find_next\"> <span data-l10n-id=\"find_next_label\">Next</span> </button> </div> <input type=\"checkbox\" id=\"findHighlightAll\" class=\"toolbarField\" tabindex=\"94\"> <label for=\"findHighlightAll\" class=\"toolbarLabel\" data-l10n-id=\"find_highlight\">Highlight all</label> <input type=\"checkbox\" id=\"findMatchCase\" class=\"toolbarField\" tabindex=\"95\"> <label for=\"findMatchCase\" class=\"toolbarLabel\" data-l10n-id=\"find_match_case_label\">Match case</label> <span id=\"findResultsCount\" class=\"toolbarLabel hidden\"></span> <span id=\"findMsg\" class=\"toolbarLabel\"></span> </div> <div id=\"secondaryToolbar\" class=\"secondaryToolbar hidden doorHangerRight\"> <div id=\"secondaryToolbarButtonContainer\"> <button id=\"secondaryPresentationMode\" class=\"secondaryToolbarButton presentationMode visibleLargeView\" title=\"Switch to Presentation Mode\" tabindex=\"51\" data-l10n-id=\"presentation_mode\"> <span data-l10n-id=\"presentation_mode_label\">Presentation Mode</span> </button> <button id=\"secondaryOpenFile\" class=\"secondaryToolbarButton openFile visibleLargeView " + AllowOpenStr + "\" title=\"Open File\" tabindex=\"52\" data-l10n-id=\"open_file\"> <span data-l10n-id=\"open_file_label\">Open</span> </button> <button id=\"secondaryPrint\" class=\"secondaryToolbarButton print visibleMediumView" + AllowPrintStr + "\" title=\"Print\" tabindex=\"53\" data-l10n-id=\"print\"> <span data-l10n-id=\"print_label\">Print</span> </button> <button id=\"secondaryDownload\" class=\"secondaryToolbarButton download visibleMediumView" + AllowDownloadStr + "\" title=\"Download\" tabindex=\"54\" data-l10n-id=\"download\"> <span data-l10n-id=\"download_label\">Download</span> </button> <a href=\"#\" id=\"secondaryViewBookmark\" class=\"secondaryToolbarButton bookmark visibleSmallView " + AllowViewBookmarkStr + "\" title=\"Current view (copy or open in new window)\" tabindex=\"55\" data-l10n-id=\"bookmark\"> <span data-l10n-id=\"bookmark_label\">Current View</span> </a> <div class=\"horizontalToolbarSeparator visibleLargeView\"></div> <button id=\"firstPage\" class=\"secondaryToolbarButton firstPage\" title=\"Go to First Page\" tabindex=\"56\" data-l10n-id=\"first_page\"> <span data-l10n-id=\"first_page_label\">Go to First Page</span> </button> <button id=\"lastPage\" class=\"secondaryToolbarButton lastPage\" title=\"Go to Last Page\" tabindex=\"57\" data-l10n-id=\"last_page\"> <span data-l10n-id=\"last_page_label\">Go to Last Page</span> </button> <div class=\"horizontalToolbarSeparator\"></div> <button id=\"pageRotateCw\" class=\"secondaryToolbarButton rotateCw\" title=\"Rotate Clockwise\" tabindex=\"58\" data-l10n-id=\"page_rotate_cw\"> <span data-l10n-id=\"page_rotate_cw_label\">Rotate Clockwise</span> </button> <button id=\"pageRotateCcw\" class=\"secondaryToolbarButton rotateCcw\" title=\"Rotate Counterclockwise\" tabindex=\"59\" data-l10n-id=\"page_rotate_ccw\"> <span data-l10n-id=\"page_rotate_ccw_label\">Rotate Counterclockwise</span> </button> <div class=\"horizontalToolbarSeparator\"></div> <button id=\"toggleHandTool\" class=\"secondaryToolbarButton handTool\" title=\"Enable hand tool\" tabindex=\"60\" data-l10n-id=\"hand_tool_enable\"> <span data-l10n-id=\"hand_tool_enable_label\">Enable hand tool</span> </button> <div class=\"horizontalToolbarSeparator\"></div> <button id=\"documentProperties\" class=\"secondaryToolbarButton documentProperties\" title=\"Document Properties…\" tabindex=\"61\" data-l10n-id=\"document_properties\"> <span data-l10n-id=\"document_properties_label\">Document Properties…</span> </button> </div> </div> <div class=\"toolbar\"> <div id=\"toolbarContainer\"> <div id=\"toolbarViewer\"> <div id=\"toolbarViewerLeft\"> <button id=\"sidebarToggle\" class=\"toolbarButton " + AllowSideBarStr +"\" title=\"Toggle Sidebar\" tabindex=\"11\" data-l10n-id=\"toggle_sidebar\"> <span data-l10n-id=\"toggle_sidebar_label\">Toggle Sidebar</span> </button> <div class=\"toolbarButtonSpacer\"></div> <button id=\"viewFind\" class=\"toolbarButton group hiddenSmallView hidden\" title=\"Find in Document\" tabindex=\"12\" data-l10n-id=\"findbar\"> <span data-l10n-id=\"findbar_label\">Find</span> </button> <div class=\"splitToolbarButton\"> <button class=\"toolbarButton pageUp\" title=\"Previous Page\" id=\"previous\" tabindex=\"13\" data-l10n-id=\"previous\"> <span data-l10n-id=\"previous_label\">Previous</span> </button> <button class=\"toolbarButton pageDown\" title=\"Next Page\" id=\"next\" tabindex=\"14\" data-l10n-id=\"next\"> <span data-l10n-id=\"next_label\">Next</span> </button> </div> </div> <div id=\"toolbarViewerRight\"> <button id=\"presentationMode\" class=\"toolbarButton presentationMode hiddenLargeView\" title=\"Switch to Presentation Mode\" tabindex=\"31\" data-l10n-id=\"presentation_mode\"> <span data-l10n-id=\"presentation_mode_label\">Presentation Mode</span> </button> <button id=\"openFile\" class=\"toolbarButton openFile hiddenLargeView " + AllowOpenStr + "\" title=\"Open File\" tabindex=\"32\" data-l10n-id=\"open_file\"> <span data-l10n-id=\"open_file_label\">Open</span> </button> <button id=\"print\" class=\"toolbarButton print hiddenMediumView" + AllowPrintStr + "\" title=\"Print\" tabindex=\"33\" data-l10n-id=\"print\"> <span data-l10n-id=\"print_label\">Print</span> </button> <button id=\"download\" class=\"toolbarButton download hiddenMediumView " + AllowDownloadStr + "\" title=\"Download\" tabindex=\"34\" data-l10n-id=\"download\"> <span data-l10n-id=\"download_label\">Download</span> </button> <a href=\"#\" id=\"viewBookmark\" class=\"toolbarButton bookmark hiddenSmallView " + AllowViewBookmarkStr + "\" title=\"Current view (copy or open in new window)\" tabindex=\"35\" data-l10n-id=\"bookmark\"> <span data-l10n-id=\"bookmark_label\">Current View</span> </a> <button id=\"secondaryToolbarToggle\" class=\"toolbarButton\" title=\"Tools\" tabindex=\"36\" data-l10n-id=\"tools\"> <span data-l10n-id=\"tools_label\">Tools</span> </button> </div> <div class=\"outerCenter\"> <div class=\"innerCenter\" id=\"toolbarViewerMiddle\"> <div class=\"splitToolbarButton\"> <button id=\"zoomOut\" class=\"toolbarButton zoomOut\" title=\"Zoom Out\" tabindex=\"21\" data-l10n-id=\"zoom_out\"> <span data-l10n-id=\"zoom_out_label\">Zoom Out</span> </button> <button id=\"zoomIn\" class=\"toolbarButton zoomIn\" title=\"Zoom In\" tabindex=\"22\" data-l10n-id=\"zoom_in\"> <span data-l10n-id=\"zoom_in_label\">Zoom In</span> </button> </div> </div> </div> </div> <div id=\"loadingBar\"> <div class=\"progress\"> <div class=\"glimmer\"> </div> </div> </div> </div> </div> <menu type=\"context\" id=\"viewerContextMenu\"> <menuitem id=\"contextFirstPage\" label=\"First Page\" data-l10n-id=\"first_page\"></menuitem> <menuitem id=\"contextLastPage\" label=\"Last Page\" data-l10n-id=\"last_page\"></menuitem> <menuitem id=\"contextPageRotateCw\" label=\"Rotate Clockwise\" data-l10n-id=\"page_rotate_cw\"></menuitem> <menuitem id=\"contextPageRotateCcw\" label=\"Rotate Counter-Clockwise\" data-l10n-id=\"page_rotate_ccw\"></menuitem> </menu> <div id=\"viewerContainer\" tabindex=\"0\"> <div id=\"viewer\" class=\"pdfViewer\"></div> </div> <div id=\"errorWrapper\" hidden='true'> <div id=\"errorMessageLeft\"> <span id=\"errorMessage\"></span> <button id=\"errorShowMore\" data-l10n-id=\"error_more_info\"> More Information </button> <button id=\"errorShowLess\" data-l10n-id=\"error_less_info\" hidden='true'> Less Information </button> </div> <div id=\"errorMessageRight\"> <button id=\"errorClose\" data-l10n-id=\"error_close\"> Close </button> </div> <div class=\"clearBoth\"></div> <textarea id=\"errorMoreInfo\" hidden='true' readonly=\"readonly\"></textarea> </div> </div> <div id=\"overlayContainer\" class=\"hidden\"> <div id=\"passwordOverlay\" class=\"container hidden\"> <div class=\"dialog\"> <div class=\"row\"> <p id=\"passwordText\" data-l10n-id=\"password_label\">Enter the password to open this PDF file:</p> </div> <div class=\"row\"> <input type=\"password\" id=\"password\" class=\"toolbarField\" /> </div> <div class=\"buttonRow\"> <button id=\"passwordCancel\" class=\"overlayButton\"><span data-l10n-id=\"password_cancel\">Cancel</span></button> <button id=\"passwordSubmit\" class=\"overlayButton\"><span data-l10n-id=\"password_ok\">OK</span></button> </div> </div> </div> <div id=\"documentPropertiesOverlay\" class=\"container hidden\"> <div class=\"dialog\"> <div class=\"row\"> <span data-l10n-id=\"document_properties_file_name\">File name:</span> <p id=\"fileNameField\">-</p> </div> <div class=\"row\"> <span data-l10n-id=\"document_properties_file_size\">File size:</span> <p id=\"fileSizeField\">-</p> </div> <div class=\"separator\"></div> <div class=\"row\"> <span data-l10n-id=\"document_properties_title\">Title:</span> <p id=\"titleField\">-</p> </div> <div class=\"row\"> <span data-l10n-id=\"document_properties_author\">Author:</span> <p id=\"authorField\">-</p> </div> <div class=\"row\"> <span data-l10n-id=\"document_properties_subject\">Subject:</span> <p id=\"subjectField\">-</p> </div> <div class=\"row\"> <span data-l10n-id=\"document_properties_keywords\">Keywords:</span> <p id=\"keywordsField\">-</p> </div> <div class=\"row\"> <span data-l10n-id=\"document_properties_creation_date\">Creation Date:</span> <p id=\"creationDateField\">-</p> </div> <div class=\"row\"> <span data-l10n-id=\"document_properties_modification_date\">Modification Date:</span> <p id=\"modificationDateField\">-</p> </div> <div class=\"row\"> <span data-l10n-id=\"document_properties_creator\">Creator:</span> <p id=\"creatorField\">-</p> </div> <div class=\"separator\"></div> <div class=\"row\"> <span data-l10n-id=\"document_properties_producer\">PDF Producer:</span> <p id=\"producerField\">-</p> </div> <div class=\"row\"> <span data-l10n-id=\"document_properties_version\">PDF Version:</span> <p id=\"versionField\">-</p> </div> <div class=\"row\"> <span data-l10n-id=\"document_properties_page_count\">Page Count:</span> <p id=\"pageCountField\">-</p> </div> <div class=\"buttonRow\"> <button id=\"documentPropertiesClose\" class=\"overlayButton\"><span data-l10n-id=\"document_properties_close\">Close</span></button> </div> </div> </div> </div> </div> <div id=\"printContainer\"></div><div id=\"mozPrintCallback-shim\" hidden> <style>@media print { #printContainer div { page-break-after: always; page-break-inside: avoid; }} </style> <style scoped>#mozPrintCallback-shim { position: fixed; top: 0; left: 0; height: 100%; width: 100%; z-index: 9999999; display: block; text-align: center; background-color: rgba(0, 0, 0, 0.5);}#mozPrintCallback-shim[hidden] { display: none;}@media print { #mozPrintCallback-shim { display: none; }}#mozPrintCallback-shim .mozPrintCallback-dialog-box { display: inline-block; margin: -50px auto 0; position: relative; top: 45%; left: 0; min-width: 220px; max-width: 400px; padding: 9px; border: 1px solid hsla(0, 0%, 0%, .5); border-radius: 2px; box-shadow: 0 1px 4px rgba(0, 0, 0, 0.3); background-color: #474747; color: hsl(0, 0%, 85%); font-size: 16px; line-height: 20px;}#mozPrintCallback-shim .progress-row { clear: both; padding: 1em 0;}#mozPrintCallback-shim progress { width: 100%;}#mozPrintCallback-shim .relative-progress { clear: both; float: right;}#mozPrintCallback-shim .progress-actions { clear: both;}#body {background-color: " + ABMaterial.GetColorStrMap(Theme.BackColor,Theme.BackColorIntensity) + ");}#toolbarSidebar {background-color: " + ABMaterial.GetColorStrMap(Theme.ToolbarColor,Theme.ToolbarColorIntensity) + ";}#toolbarContainer, .findbar, .secondaryToolbar {background-color: " + ABMaterial.GetColorStrMap(Theme.ToolbarColor,Theme.ToolbarColorIntensity) + ";}#overlayContainer > .container > .dialog {background-color: " + ABMaterial.GetColorStrMap(Theme.ToolbarColor,Theme.ToolbarColorIntensity) + ";} </style> <div class=\"mozPrintCallback-dialog-box\"> " + PreparePrintingText + " <div class=\"progress-row\"> <progress value=\"0\" max=\"100\"></progress> <span class=\"relative-progress\">0%</span> </div> <div class=\"progress-actions\"> <input type=\"button\" value=\"Cancel\" class=\"mozPrintCallback-cancel\"> </div> </div></div> </body></html>");
		} else {
			s.append("<html dir=\"" + ReadDirection + "\" mozdisallowselectionprint moznomarginboxes> <head> <meta charset=\"utf-8\"> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\"> <meta name=\"google\" content=\"notranslate\"> <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"> <title>ABMPDFViewer</title> <link rel=\"stylesheet\" href=\"../../js/web/viewer.min.css\"/> <script src=\"../../js/web/compatibility.js\"></script>	<link rel=\"resource\" type=\"application/l10n\" href=\"../../js/web/locale/locale.properties\"/>	<script src=\"../../js/web/l10n.js\"></script>	<script src=\"../../js/build/pdf.min.js\"></script>	<script src=\"../../js/web/debugger.min.js\"></script>	<script src=\"../../js/web/viewer.min.js\"></script> </head> <body tabindex=\"1\" class=\"loadingInProgress\">  <div id=\"outerContainer\"" + lang + ">	 <div id=\"sidebarContainer\"> <div id=\"toolbarSidebar\"> <div class=\"splitToolbarButton toggled\"> <button id=\"viewThumbnail\" class=\"toolbarButton group toggled\" title=\"Show Thumbnails\" tabindex=\"2\" data-l10n-id=\"thumbs\"> <span data-l10n-id=\"thumbs_label\">Thumbnails</span> </button> <button id=\"viewOutline\" class=\"toolbarButton group\" title=\"Show Document Outline\" tabindex=\"3\" data-l10n-id=\"outline\"> <span data-l10n-id=\"outline_label\">Document Outline</span> </button> <button id=\"viewAttachments\" class=\"toolbarButton group\" title=\"Show Attachments\" tabindex=\"4\" data-l10n-id=\"attachments\"> <span data-l10n-id=\"attachments_label\">Attachments</span> </button> </div> </div> <div id=\"sidebarContent\"> <div id=\"thumbnailView\"> </div> <div id=\"outlineView\" class=\"hidden\"> </div> <div id=\"attachmentsView\" class=\"hidden\"> </div> </div> </div> <div id=\"mainContainer\">	 <div class=\"findbar hidden doorHanger hiddenSmallView\" id=\"findbar\"> <label for=\"findInput\" class=\"toolbarLabel\" data-l10n-id=\"find_label\">Find:</label> <input id=\"findInput\" class=\"toolbarField\" tabindex=\"91\"> <div class=\"splitToolbarButton\"> <button class=\"toolbarButton findPrevious\" title=\"\" id=\"findPrevious\" tabindex=\"92\" data-l10n-id=\"find_previous\"> <span data-l10n-id=\"find_previous_label\">Previous</span> </button> <button class=\"toolbarButton findNext\" title=\"\" id=\"findNext\" tabindex=\"93\" data-l10n-id=\"find_next\"> <span data-l10n-id=\"find_next_label\">Next</span> </button> </div> <input type=\"checkbox\" id=\"findHighlightAll\" class=\"toolbarField\" tabindex=\"94\"> <label for=\"findHighlightAll\" class=\"toolbarLabel\" data-l10n-id=\"find_highlight\">Highlight all</label> <input type=\"checkbox\" id=\"findMatchCase\" class=\"toolbarField\" tabindex=\"95\"> <label for=\"findMatchCase\" class=\"toolbarLabel\" data-l10n-id=\"find_match_case_label\">Match case</label> <span id=\"findResultsCount\" class=\"toolbarLabel hidden\"></span> <span id=\"findMsg\" class=\"toolbarLabel\"></span> </div> <div id=\"secondaryToolbar\" class=\"secondaryToolbar hidden doorHangerRight\"> <div id=\"secondaryToolbarButtonContainer\"> <button id=\"secondaryPresentationMode\" class=\"secondaryToolbarButton presentationMode visibleLargeView\" title=\"Switch to Presentation Mode\" tabindex=\"51\" data-l10n-id=\"presentation_mode\"> <span data-l10n-id=\"presentation_mode_label\">Presentation Mode</span> </button> <button id=\"secondaryOpenFile\" class=\"secondaryToolbarButton openFile visibleLargeView " + AllowOpenStr + "\" title=\"Open File\" tabindex=\"52\" data-l10n-id=\"open_file\"> <span data-l10n-id=\"open_file_label\">Open</span> </button> <button id=\"secondaryPrint\" class=\"secondaryToolbarButton print visibleMediumView" + AllowPrintStr + "\" title=\"Print\" tabindex=\"53\" data-l10n-id=\"print\"> <span data-l10n-id=\"print_label\">Print</span> </button> <button id=\"secondaryDownload\" class=\"secondaryToolbarButton download visibleMediumView" + AllowDownloadStr + "\" title=\"Download\" tabindex=\"54\" data-l10n-id=\"download\"> <span data-l10n-id=\"download_label\">Download</span> </button> <a href=\"#\" id=\"secondaryViewBookmark\" class=\"secondaryToolbarButton bookmark visibleSmallView " + AllowViewBookmarkStr + "\" title=\"Current view (copy or open in new window)\" tabindex=\"55\" data-l10n-id=\"bookmark\"> <span data-l10n-id=\"bookmark_label\">Current View</span> </a> <div class=\"horizontalToolbarSeparator visibleLargeView\"></div> <button id=\"firstPage\" class=\"secondaryToolbarButton firstPage\" title=\"Go to First Page\" tabindex=\"56\" data-l10n-id=\"first_page\"> <span data-l10n-id=\"first_page_label\">Go to First Page</span> </button> <button id=\"lastPage\" class=\"secondaryToolbarButton lastPage\" title=\"Go to Last Page\" tabindex=\"57\" data-l10n-id=\"last_page\"> <span data-l10n-id=\"last_page_label\">Go to Last Page</span> </button> <div class=\"horizontalToolbarSeparator\"></div> <button id=\"pageRotateCw\" class=\"secondaryToolbarButton rotateCw\" title=\"Rotate Clockwise\" tabindex=\"58\" data-l10n-id=\"page_rotate_cw\"> <span data-l10n-id=\"page_rotate_cw_label\">Rotate Clockwise</span> </button> <button id=\"pageRotateCcw\" class=\"secondaryToolbarButton rotateCcw\" title=\"Rotate Counterclockwise\" tabindex=\"59\" data-l10n-id=\"page_rotate_ccw\"> <span data-l10n-id=\"page_rotate_ccw_label\">Rotate Counterclockwise</span> </button> <div class=\"horizontalToolbarSeparator\"></div> <button id=\"toggleHandTool\" class=\"secondaryToolbarButton handTool\" title=\"Enable hand tool\" tabindex=\"60\" data-l10n-id=\"hand_tool_enable\"> <span data-l10n-id=\"hand_tool_enable_label\">Enable hand tool</span> </button> <div class=\"horizontalToolbarSeparator\"></div> <button id=\"documentProperties\" class=\"secondaryToolbarButton documentProperties\" title=\"Document Properties…\" tabindex=\"61\" data-l10n-id=\"document_properties\"> <span data-l10n-id=\"document_properties_label\">Document Properties…</span> </button> </div> </div> <div class=\"toolbar\"> <div id=\"toolbarContainer\"> <div id=\"toolbarViewer\"> <div id=\"toolbarViewerLeft\"> <button id=\"sidebarToggle\" class=\"toolbarButton " + AllowSideBarStr +"\" title=\"Toggle Sidebar\" tabindex=\"11\" data-l10n-id=\"toggle_sidebar\"> <span data-l10n-id=\"toggle_sidebar_label\">Toggle Sidebar</span> </button> <div class=\"toolbarButtonSpacer\"></div> <button id=\"viewFind\" class=\"toolbarButton group hiddenSmallView hidden\" title=\"Find in Document\" tabindex=\"12\" data-l10n-id=\"findbar\"> <span data-l10n-id=\"findbar_label\">Find</span> </button> <div class=\"splitToolbarButton\"> <button class=\"toolbarButton pageUp\" title=\"Previous Page\" id=\"previous\" tabindex=\"13\" data-l10n-id=\"previous\"> <span data-l10n-id=\"previous_label\">Previous</span> </button> <button class=\"toolbarButton pageDown\" title=\"Next Page\" id=\"next\" tabindex=\"14\" data-l10n-id=\"next\"> <span data-l10n-id=\"next_label\">Next</span> </button> </div> </div> <div id=\"toolbarViewerRight\"> <button id=\"presentationMode\" class=\"toolbarButton presentationMode hiddenLargeView\" title=\"Switch to Presentation Mode\" tabindex=\"31\" data-l10n-id=\"presentation_mode\"> <span data-l10n-id=\"presentation_mode_label\">Presentation Mode</span> </button> <button id=\"openFile\" class=\"toolbarButton openFile hiddenLargeView " + AllowOpenStr + "\" title=\"Open File\" tabindex=\"32\" data-l10n-id=\"open_file\"> <span data-l10n-id=\"open_file_label\">Open</span> </button> <button id=\"print\" class=\"toolbarButton print hiddenMediumView" + AllowPrintStr + "\" title=\"Print\" tabindex=\"33\" data-l10n-id=\"print\"> <span data-l10n-id=\"print_label\">Print</span> </button> <button id=\"download\" class=\"toolbarButton download hiddenMediumView " + AllowDownloadStr + "\" title=\"Download\" tabindex=\"34\" data-l10n-id=\"download\"> <span data-l10n-id=\"download_label\">Download</span> </button> <a href=\"#\" id=\"viewBookmark\" class=\"toolbarButton bookmark hiddenSmallView " + AllowViewBookmarkStr + "\" title=\"Current view (copy or open in new window)\" tabindex=\"35\" data-l10n-id=\"bookmark\"> <span data-l10n-id=\"bookmark_label\">Current View</span> </a> <button id=\"secondaryToolbarToggle\" class=\"toolbarButton\" title=\"Tools\" tabindex=\"36\" data-l10n-id=\"tools\"> <span data-l10n-id=\"tools_label\">Tools</span> </button> </div> <div class=\"outerCenter\"> <div class=\"innerCenter\" id=\"toolbarViewerMiddle\"> <div class=\"splitToolbarButton\"> <button id=\"zoomOut\" class=\"toolbarButton zoomOut\" title=\"Zoom Out\" tabindex=\"21\" data-l10n-id=\"zoom_out\"> <span data-l10n-id=\"zoom_out_label\">Zoom Out</span> </button> <button id=\"zoomIn\" class=\"toolbarButton zoomIn\" title=\"Zoom In\" tabindex=\"22\" data-l10n-id=\"zoom_in\"> <span data-l10n-id=\"zoom_in_label\">Zoom In</span> </button> </div> </div> </div> </div> <div id=\"loadingBar\"> <div class=\"progress\"> <div class=\"glimmer\"> </div> </div> </div> </div> </div> <menu type=\"context\" id=\"viewerContextMenu\"> <menuitem id=\"contextFirstPage\" label=\"First Page\" data-l10n-id=\"first_page\"></menuitem> <menuitem id=\"contextLastPage\" label=\"Last Page\" data-l10n-id=\"last_page\"></menuitem> <menuitem id=\"contextPageRotateCw\" label=\"Rotate Clockwise\" data-l10n-id=\"page_rotate_cw\"></menuitem> <menuitem id=\"contextPageRotateCcw\" label=\"Rotate Counter-Clockwise\" data-l10n-id=\"page_rotate_ccw\"></menuitem> </menu> <div id=\"viewerContainer\" tabindex=\"0\"> <div id=\"viewer\" class=\"pdfViewer\"></div> </div> <div id=\"errorWrapper\" hidden='true'> <div id=\"errorMessageLeft\"> <span id=\"errorMessage\"></span> <button id=\"errorShowMore\" data-l10n-id=\"error_more_info\"> More Information </button> <button id=\"errorShowLess\" data-l10n-id=\"error_less_info\" hidden='true'> Less Information </button> </div> <div id=\"errorMessageRight\"> <button id=\"errorClose\" data-l10n-id=\"error_close\"> Close </button> </div> <div class=\"clearBoth\"></div> <textarea id=\"errorMoreInfo\" hidden='true' readonly=\"readonly\"></textarea> </div> </div> <div id=\"overlayContainer\" class=\"hidden\"> <div id=\"passwordOverlay\" class=\"container hidden\"> <div class=\"dialog\"> <div class=\"row\"> <p id=\"passwordText\" data-l10n-id=\"password_label\">Enter the password to open this PDF file:</p> </div> <div class=\"row\"> <input type=\"password\" id=\"password\" class=\"toolbarField\" /> </div> <div class=\"buttonRow\"> <button id=\"passwordCancel\" class=\"overlayButton\"><span data-l10n-id=\"password_cancel\">Cancel</span></button> <button id=\"passwordSubmit\" class=\"overlayButton\"><span data-l10n-id=\"password_ok\">OK</span></button> </div> </div> </div> <div id=\"documentPropertiesOverlay\" class=\"container hidden\"> <div class=\"dialog\"> <div class=\"row\"> <span data-l10n-id=\"document_properties_file_name\">File name:</span> <p id=\"fileNameField\">-</p> </div> <div class=\"row\"> <span data-l10n-id=\"document_properties_file_size\">File size:</span> <p id=\"fileSizeField\">-</p> </div> <div class=\"separator\"></div> <div class=\"row\"> <span data-l10n-id=\"document_properties_title\">Title:</span> <p id=\"titleField\">-</p> </div> <div class=\"row\"> <span data-l10n-id=\"document_properties_author\">Author:</span> <p id=\"authorField\">-</p> </div> <div class=\"row\"> <span data-l10n-id=\"document_properties_subject\">Subject:</span> <p id=\"subjectField\">-</p> </div> <div class=\"row\"> <span data-l10n-id=\"document_properties_keywords\">Keywords:</span> <p id=\"keywordsField\">-</p> </div> <div class=\"row\"> <span data-l10n-id=\"document_properties_creation_date\">Creation Date:</span> <p id=\"creationDateField\">-</p> </div> <div class=\"row\"> <span data-l10n-id=\"document_properties_modification_date\">Modification Date:</span> <p id=\"modificationDateField\">-</p> </div> <div class=\"row\"> <span data-l10n-id=\"document_properties_creator\">Creator:</span> <p id=\"creatorField\">-</p> </div> <div class=\"separator\"></div> <div class=\"row\"> <span data-l10n-id=\"document_properties_producer\">PDF Producer:</span> <p id=\"producerField\">-</p> </div> <div class=\"row\"> <span data-l10n-id=\"document_properties_version\">PDF Version:</span> <p id=\"versionField\">-</p> </div> <div class=\"row\"> <span data-l10n-id=\"document_properties_page_count\">Page Count:</span> <p id=\"pageCountField\">-</p> </div> <div class=\"buttonRow\"> <button id=\"documentPropertiesClose\" class=\"overlayButton\"><span data-l10n-id=\"document_properties_close\">Close</span></button> </div> </div> </div> </div> </div> <div id=\"printContainer\"></div><div id=\"mozPrintCallback-shim\" hidden> <style>@media print { #printContainer div { page-break-after: always; page-break-inside: avoid; }} </style> <style scoped>#mozPrintCallback-shim { position: fixed; top: 0; left: 0; height: 100%; width: 100%; z-index: 9999999; display: block; text-align: center; background-color: rgba(0, 0, 0, 0.5);}#mozPrintCallback-shim[hidden] { display: none;}@media print { #mozPrintCallback-shim { display: none; }}#mozPrintCallback-shim .mozPrintCallback-dialog-box { display: inline-block; margin: -50px auto 0; position: relative; top: 45%; left: 0; min-width: 220px; max-width: 400px; padding: 9px; border: 1px solid hsla(0, 0%, 0%, .5); border-radius: 2px; box-shadow: 0 1px 4px rgba(0, 0, 0, 0.3); background-color: #474747; color: hsl(0, 0%, 85%); font-size: 16px; line-height: 20px;}#mozPrintCallback-shim .progress-row { clear: both; padding: 1em 0;}#mozPrintCallback-shim progress { width: 100%;}#mozPrintCallback-shim .relative-progress { clear: both; float: right;}#mozPrintCallback-shim .progress-actions { clear: both;}#body {background-color: " + ABMaterial.GetColorStrMap(Theme.BackColor,Theme.BackColorIntensity) + ");}#toolbarSidebar {background-color: " + ABMaterial.GetColorStrMap(Theme.ToolbarColor,Theme.ToolbarColorIntensity) + ";}#toolbarContainer, .findbar, .secondaryToolbar {background-color: " + ABMaterial.GetColorStrMap(Theme.ToolbarColor,Theme.ToolbarColorIntensity) + ";}#overlayContainer > .container > .dialog {background-color: " + ABMaterial.GetColorStrMap(Theme.ToolbarColor,Theme.ToolbarColorIntensity) + ";} </style> <div class=\"mozPrintCallback-dialog-box\"> " + PreparePrintingText + " <div class=\"progress-row\"> <progress value=\"0\" max=\"100\"></progress> <span class=\"relative-progress\">0%</span> </div> <div class=\"progress-actions\"> <input type=\"button\" value=\"Cancel\" class=\"mozPrintCallback-cancel\"> </div> </div></div> </body></html>");
		}		
		return s.toString();
	}
	
	@Hide
	protected String BuildClass() {			
		StringBuilder s = new StringBuilder();	
		s.append(super.BuildExtraClasses());
		s.append(mVisibility + " ");	
		s.append(mIsPrintableClass);
		s.append(mIsOnlyForPrintClass);
		return s.toString(); 
	}
	
	@Hide
	protected String BuildBody() {		
		return "";
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMPDFViewer c = new ABMPDFViewer();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;
		c.Theme = Theme.Clone();
		c.Type = Type;
		c.mVisibility = mVisibility;		
		return c;
	}

}
