package com.ab.abmaterial;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import anywheresoftware.b4a.BA;
//import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.SimpleFuture;

@Author("Alain Bailleul")  
@ShortName("ABMEditor")
@Events(values={"Loaded(Target as String)", "LostFocus()"})
public class ABMEditor extends ABMComponent {
		private static final long serialVersionUID = -3440473522197533291L;
		protected ThemeEditor Theme=new ThemeEditor();
		protected String sText="";
		protected boolean IsDirty=false;
		protected transient SimpleFuture FutureText;
		protected boolean showOnlyInfocus=false;
		public String DropBoxImageTitle="Insert image URL";
		public String DropBoxImageButtonText="Insert";
		public String DropBoxLinkTitle="Insert URL";
		public String DropBoxLinkButtonText="Insert";
		public String DropBoxFontSizeTitle="Font size";
		public String DropBoxFontSizeButtonText="Change";
		protected boolean mEnabled=true;
		protected boolean HasToolbar=true;
		public boolean ForceMinimalInitialHeight=false;
		public String FixedWidth="";
		public String FixedMinHeight="";
		protected String DefaultFont="";
		protected String DefaultFontSize="";
		public int Zoom=100;
		
		protected boolean mAllowTabs=false;
		protected boolean mAllowPasteImages=false;
		
		/**
		 * Get/Set is the tab key in the editor inserts a blockquote (looks like a tab).  
		 * 
		 * Set the editor theme.BlockQuoteColor = ABMaterial.COLOR_TRANSPARENT if you do not want to see the border of the blockquote.
		 */
		public boolean getAllowTabs() {
			return mAllowTabs;
		}
		
		public void setAllowTabs(boolean b) {
			mAllowTabs = b;
		}
		
		/**
		 * Gets/Sets if the user is allowed to paste images into the editor (e.g. from the Windows 10 Snippet tool)
		 * 
		 * When true, you will need to
		 * 
		 * 1. Change the code so it includes the pageid
		 * Dim ABMPageId As String = req.GetHeader("pageid")
	     *
		 * Dim callback As Object = req.GetSession.GetAttribute("abmcallback" & ABMPageId)
		 * Dim downloadfolder As String = req.GetSession.GetAttribute("abmdownloadfolder" & ABMPageId)
		 * Dim MaxSize As Int
		 * Try
		 * 		MaxSize = req.GetSession.GetAttribute("abmmaxsize" & ABMPageId)
		 * Catch
		 * 		resp.SendError(500, LastException)		
		 *		Return
		 * End Try
		 * 
		 * 2. add a block in ABMUploadHandler
		 * <code>
		 * ...
		 *      ' start block
		 *      Else
		 *			Dim filePart As Part = data.Get("editor")
		 *			If filePart.IsInitialized Then
		 *				fileName = ABMShared.GetGUID & filePart.SubmittedFilename
		 *				tmpFileName = filePart.TempFile
		 *				If ABM.HandleUpload(downloadfolder, tmpFileName, fileName) Then
		 *					If SubExists(callback, "Page_FileUploaded") Then
		 *						CallSubDelayed3(callback, "Page_FileUploaded", fileName, True)
		 *					End If
		 *				Else
		 *					If SubExists(callback, "Page_FileUploaded") Then
		 *						CallSubDelayed3(callback, "Page_FileUploaded", fileName, False)
		 *					End If
		 *				End If
		 *				resp.Write($"../EditorImages/${fileName}"$)
		 *			End If	
		 *		End If
		 *		' end block
		 *	End If		
		 * End If		
		 * Catch		
		 * </code>
		 * 
		 * 3. In the page that uses the ABMEditor control:
		 * <code>
		 * ' in global
		 * Public DownloadFolder As String
	     * Public DownloadMaxSize As Long = 1024*1024*5
	     * 
	     * ' at the end of Websocket_Connected
	     * DownloadFolder = File.Combine(File.DirApp, "\www\" & AppName & "\EditorImages\")
	     * If File.Exists("",DownloadFolder) = False Then
		 *    File.MakeDir("", DownloadFolder)
		 * End If
		 * Dim WindowName As String = ABMPageId.Replace(Name, "")
	     * ws.Session.SetAttribute("abmcallback" & WindowName, Me)
	     * ws.Session.SetAttribute("abmdownloadfolder" & WindowName, DownloadFolder)
	     * ws.Session.SetAttribute("abmmaxsize" & WindowName, DownloadMaxSize)
	     * 
	     * ' in Websocket_Disconnected
	     * Try
		 * 	Dim WindowName As String = ABMPageId.Replace(Name, "")
		 * 	ws.Session.RemoveAttribute("abmcallback" & WindowName)
		 * 	ws.Session.RemoveAttribute("abmdownloadfolder" & WindowName)
		 * 	ws.Session.RemoveAttribute("abmmaxsize" & WindowName)
		 * Catch
		 *	Log(LastException.Message)
		 * End Try
		 * </code>  
		 */
		public boolean getAllowPasteImages() {
			return mAllowPasteImages;
		}
		
		public void setAllowPasteImages(boolean b) {
			mAllowPasteImages = b;
		}
					
		public void Initialize(ABMPage page, String id, boolean isEnabled, boolean hasToolbar, String themeName) {
			this.ID = id;			
			this.Page = page;
			this.Type = ABMaterial.UITYPE_EDITOR;				
			this.HasToolbar = hasToolbar;
			this.mEnabled = isEnabled;
			if (!themeName.equals("")) {
				if (Page.CompleteTheme.Editors.containsKey(themeName.toLowerCase())) {
					Theme = Page.CompleteTheme.Editors.get(themeName.toLowerCase()).Clone();				
				}
			}	
			IsInitialized=true;
		}
		
		public void SetDefaultFont(String fontFamily, String fontSize) {
			DefaultFont=fontFamily;
			DefaultFontSize = fontSize;
		}
		
		@Override
		protected void ResetTheme() {
			UseTheme(Theme.ThemeName);
		}
		
		public void UseTheme(String themeName) {
			if (!themeName.equals("")) {
				if (Page.CompleteTheme.Editors.containsKey(themeName.toLowerCase())) {
					Theme = Page.CompleteTheme.Editors.get(themeName.toLowerCase()).Clone();				
				}
			}
		}	
		
		@Override
		protected String RootID() {
			return ArrayName.toLowerCase() + ID.toLowerCase() + "-parent";
		}
		
		public void HideToolbar() {
			HasToolbar=false;
		}
		
		public void ShowToolbar() {
			HasToolbar=true;
		}
		
		public void SetFocus() {
			Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-iframe')[0].contentWindow.document.body.focus();", null);
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Editor SetFocus: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
		
		public void SetHTML(String text) {
			this.sText = text;	
			this.IsDirty = true;
			
			StringBuilder s = new StringBuilder();
			s.append("var iframe=$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-iframe').first()[0];");
			s.append("var cwindow=iframe.contentWindow;");
			s.append("var editor=cwindow.editor;");
			s.append("if (editor) {");
			s.append("editor.ABMIsDirty=false;");
			s.append("}");
			Page.ws.Eval(s.toString(), null);
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Editor SetHTML: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
		
		public String GetHTML() {		
			if (!(FutureText==null)) {
				try {				
					this.sText = (String) FutureText.getValue();					
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
			
			return sText;
		}
		
		public boolean getEnabled() {		
			return mEnabled;
		}
		
		public void setEnabled(boolean enabled) {
			IsEnabledDirty=true;
			GotLastEnabled=true;
			mEnabled=enabled;
		}
			
		@Override
		protected void AddArrayName(String arrayName) {	
			this.ArrayName += arrayName;
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
			Page.ws.Eval(BuildJavaScript(), null);
			// ALAIN 16/11/03
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Editor FirstRun : " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
			IsDirty=true;
			Refresh();
			// END ALAIN 16/11/13
			
			super.FirstRun();
		}
		
		protected String BuildJavaScript() {
			StringBuilder s = new StringBuilder();	
			ThemeEditor t = Theme;
			
			s.append("var doItEditor=false;");
			s.append("if ($('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-iframe').first()[0]==undefined) {");
				s.append("doItEditor=true;");
			s.append("} else {");
				s.append("if ($('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-iframe').first()[0].contentWindow==undefined){");
					s.append("doItEditor=true;");
				s.append("}");
			s.append("}\n");
			
			s.append("if (doItEditor) {");
			s.append("new SquireUI({");
			s.append("replace: 'div#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "',\n");
			
			boolean UseCDN = (ABMaterial.CDN);
			if (UseCDN) {
				s.append("buildPath: '" + ABMaterial.mCDNUrl + "js/', ");
			} else {
				s.append("buildPath: '../../js/', ");
			}
			s.append("id: '" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "',\n ");
			s.append("showonlyinfocus: " + showOnlyInfocus + ",");
			s.append("activeBorderColor: '" + ABMaterial.GetColorStrMap(t.ActiveBorderColor,t.ActiveBorderColorIntensity) + "',");
			s.append("inactiveBorderColor: '" + ABMaterial.GetColorStrMap(t.InactiveBorderColor,t.InactiveBorderColorIntensity) + "',");
			s.append("dropboxTitleColor: '" + ABMaterial.GetColorStrMap(t.DropboxTitleColor, t.DropboxTitleColorIntensity) + "',");
			s.append("dropboxLineColor: '" + ABMaterial.GetColorStrMap(t.DropboxTitleColor, t.DropboxTitleColorIntensity) + "',");
			s.append("dropboxTextColor: '" + ABMaterial.GetColorStrMap(t.DropboxTextColor, t.DropboxTextColorIntensity) + "',");
			s.append("dropboxButtonBackColor: '" + ABMaterial.GetColorStrMap(t.DropboxButtonBackColor, t.DropboxButtonBackColorIntensity) + "',");
			s.append("dropboxButtonForeColor: '" + ABMaterial.GetColorStrMap(t.DropboxButtonForeColor, t.DropboxButtonForeColorIntensity) + "',");
			s.append("blockQuoteColor: '" + ABMaterial.GetColorStrMap(t.BlockQuoteColor, t.BlockQuoteColorIntensity) + "', ");
			s.append("textColor: '" + ABMaterial.GetColorStrMap(t.TextColor, t.TextColorIntensity) + "', ");
			s.append("backColor: '" + ABMaterial.GetColorStrMap(t.BackColor, t.BackColorIntensity) + "', ");
			s.append("activeMenuColor: '" + ABMaterial.GetColorStrMap(t.ActiveMenuColor, t.ActiveMenuColorIntensity)  + "', ");
			s.append("theme: '" + t.ThemeName.toLowerCase() + "', ");
			s.append("dropboxImageTitle: '" + DropBoxImageTitle + "', ");
			s.append("dropboxImageButtonText: '" + DropBoxImageButtonText + "', ");
			s.append("dropboxLinkTitle: '" + DropBoxLinkTitle + "', ");
			s.append("dropboxLinkButtonText: '" + DropBoxLinkButtonText + "', ");
			s.append("dropboxFontSizeTitle: '" + DropBoxFontSizeTitle + "', ");
			s.append("dropboxFontSizeButtonText: '" + DropBoxFontSizeButtonText + "', ");	
			s.append("isEnabled: " + mEnabled + ", ");
			s.append("AllowTabs: " + mAllowTabs + ", ");
			s.append("AllowPasteImages: " + mAllowPasteImages + ", ");
			s.append("hasToolbar: " + HasToolbar + ", ");
			s.append("arrayName: '" + this.ArrayName.toLowerCase() + "', ");
			if (ForceMinimalInitialHeight) {
				s.append("abmheight: 'initial', ");
			} else {
				s.append("abmheight: '100%', ");
			}
			s.append("realId: '" + ArrayName.toLowerCase() + ID.toLowerCase() + "' ");
			s.append("});\n");
			s.append("};");
			s.append("editorenable('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "','" + mEnabled + "', '" + HasToolbar + "');\n");
			s.append("editorresize('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "');\n");
			
			String zoomPct = "";
			if (Zoom!=100) {
				zoomPct = "zoom: " + Zoom + "%;";
			}
			
			if (!DefaultFont.equals("")) {
				String rtl="";
				if (getRightToLeft()) {
					rtl = "direction: rtl;";
				}
				s.append("var head = jQuery('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-iframe').contents().find(\"head\");\n");				
				s.append("var css = '<style type=\"text/css\">body{" + zoomPct + rtl + "font-family: " + DefaultFont + " !important;font-size: " + DefaultFontSize + " !important}</style>';\n");
				s.append("jQuery(head).append(css);\n");
			} else {
				if (getRightToLeft()) {
					s.append("var head = jQuery('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-iframe').contents().find(\"head\");\n");				
					s.append("var css = '<style type=\"text/css\">body{" + zoomPct + " direction: rtl}</style>';\n");
					s.append("jQuery(head).append(css);\n");
				} else {
					s.append("var head = jQuery('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-iframe').contents().find(\"head\");\n");				
					s.append("var css = '<style type=\"text/css\">body{" + zoomPct + "}</style>';\n");
					s.append("jQuery(head).append(css);\n");
				}
			}
			if (!FixedMinHeight.equals("")) {
				s.append("jQuery('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-iframe').css('min-height', '" + FixedMinHeight + "');");
			}
			s.append("var iframe=$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-iframe').first()[0];");
			s.append("var cwindow=iframe.contentWindow;");
			s.append("var editor=cwindow.editor;");
			s.append("if (editor) {");
			s.append("editor.ABMIsDirty=false;");
			s.append("editor.addEventListener('input', function() {editor.ABMIsDirty=true;});");
			s.append("}");
			
			return s.toString();
		}
		
		public boolean GetIsDirty() {
			StringBuilder s = new StringBuilder();
			s.append("var iframe=$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-iframe').first()[0];");
			s.append("var cwindow=iframe.contentWindow;");
			s.append("var editor=cwindow.editor;");
			s.append("if (editor) {");
			s.append("return editor.ABMIsDirty;");
			s.append("} else {");
			s.append("return true;");
			s.append("}");
			Boolean res=false;
			
			if (Page.ws.getOpen()) {
				SimpleFuture ret = Page.ws.EvalWithResult(s.toString(), null);
				if (ret!=null) {
					try {
						res = (Boolean) ret.getValue();			
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
						
			return res;
		}
		
		public void SetIsDirty(boolean bool) {
			StringBuilder s = new StringBuilder();
			s.append("var iframe=$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-iframe').first()[0];");
			s.append("var cwindow=iframe.contentWindow;");
			s.append("var editor=cwindow.editor;");
			s.append("if (editor) {");
			if (bool) {
				s.append("editor.ABMIsDirty=true;");
			} else {
				s.append("editor.ABMIsDirty=false;");
			}
			s.append("}");
			Page.ws.Eval(s.toString(), null);
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Editor Refresh 1 : " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
		
		@Override
		public void Refresh() {	
			RefreshInternal(true);
		}
			
		@Override
		protected void RefreshInternal(boolean DoFlush) {			
			// TODO ExtraClasses not working
			super.Refresh();
			ABMaterial.ChangeVisibility(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent", mVisibility);
			if (mIsPrintableClass.equals("")) {
				ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent", "no-print");
			} else {
				ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent", "no-print");
			}
			if (mIsOnlyForPrintClass.equals("")) {
				ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent", "only-print");
			} else {
				ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent", "only-print");
			}
			
			if (IsDirty) {
				
				sText = sText.replace("\"","\\\"");
				sText = sText.replace("\n","<br>"); 
				sText = sText.replace("\r","");
				StringBuilder s = new StringBuilder();
				s.append(BuildJavaScript());
				s.append("var tmpHtml=\"" + sText + "\";");
				
				s.append("var iframe=$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-iframe').first()[0];");
				
				s.append("var cwindow=iframe.contentWindow;");
				
				s.append("var editor=cwindow.editor;");
				
				s.append("if (editor) {");
				s.append("editor.setHTML(tmpHtml);");
				
				s.append("editorresize('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "');\n");
				s.append("}");
				if (!FixedMinHeight.equals("")) {
					s.append("jQuery('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-iframe').css('min-height', '" + FixedMinHeight + "');");
				}
				
				Page.ws.Eval(s.toString(), null);
				
				if (DoFlush) {
					try {
						if (Page.ws.getOpen()) {
							if (Page.ShowDebugFlush) {BA.Log("Editor Refresh 1 : " + ID);}
							Page.ws.Flush();Page.RunFlushed();
						}
					} catch (IOException e) {			
						//e.printStackTrace();
					}
				}
			}
			
			if (DoFlush) {
				try {
					if (Page.ws.getOpen()) {
						if (Page.ShowDebugFlush) {BA.Log("Editor Refresh 2: " + ID);}
						Page.ws.Flush();Page.RunFlushed();
					}
				} catch (IOException e) {			
					e.printStackTrace();
				}
			}
			IsDirty=false;
			
		}
		
		@Override
		protected String Build() {
			if (Theme.ThemeName.equals("default")) {
				Theme.Colorize(Page.CompleteTheme.MainColor);
			}
			StringBuilder s = new StringBuilder();	
			if (FixedWidth.equals("")) {
				s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent\" class=\"" + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() + "\">");
			} else {
				s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent\" class=\"" + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() + "\" style=\"width: " + FixedWidth + "\">");
			}
			
			s.append(BuildBody());
			s.append("</div>");
			IsBuild=true;
			return s.toString();
		}
		
		@Hide
		protected String BuildClass() {			
			StringBuilder s = new StringBuilder();			
			return s.toString(); 
		}
		
		@Hide
		protected String BuildBody() {
			StringBuilder s = new StringBuilder();
			s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" ></div>");
			return s.toString();
		}
		
		@Override
		protected ABMComponent Clone() {
			ABMEditor c = new ABMEditor();
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

