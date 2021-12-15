package com.ab.abmaterial;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.SimpleFuture;
import anywheresoftware.b4j.objects.collections.JSONParser;

@Author("Alain Bailleul")  
@ShortName("ABMFileManager")
@Events(values={"FolderChange(folder As String)", "RequestForDownload(jsonList as String)", "Downloaded(jobcode as String)", "RequestForDelete(jsonList as String)", "Uploaded()", "UploadStarted()", "UploadFailed(status As String, message as String)", "RequestForCopyPaste(fromFolder as String, jsonList as String)", "RequestForCutPaste(fromFolder as String, jsonList as String)", "RequestForRename(oldFileName as String)", "RequestForCreateFolder()"})
public class ABMFileManager extends ABMComponent {	
	private static final long serialVersionUID = 5534599861016826806L;
	protected ThemeFileManager Theme=new ThemeFileManager();
	public int VerticalHeightPx=200;
	
	public String PaddingLeft=""; //"0.75rem";
	public String PaddingRight=""; //"0.75rem";
	public String PaddingTop=""; //"0rem";
	public String PaddingBottom=""; //"0rem";
	public String MarginTop=""; //"0px";
	public String MarginBottom=""; //"0px";
	public String MarginLeft=""; //"0px";
	public String MarginRight=""; //"0px";
	
	public String TitleName="Name";
	public String TitleSize="Size";
	public String TitleDate="Date";
	protected String TitleSelected="Selected";
	public boolean ShowBigIcons=true;
	
	public String ToastMaximumFileSize="The maximum file size is";
	public String ToastFileTypeNotAllowed="This file type is not allowed";
	public String ToastFileExtensionNotAllowed="This file extention is not allowed";
	
	public boolean CanUpload=true;
	public boolean CanDownload=true;
	public boolean CanCut=true;
	public boolean CanCopy=true;
	public boolean CanPaste=true;
	public boolean CanDelete=true;
	public boolean CanRename=true;
	public boolean CanCreateFolder=true;
	
	public String ToolTipUpload="Upload";
	public String ToolTipDownload="Download";
	public String ToolTipCut="Cut";
	public String ToolTipCopy="Copy";
	public String ToolTipPaste="Paste";
	public String ToolTipRename="Rename";
	public String ToolTipDelete="Delete";
	public String ToolTipCreateFolder="New";
	
	public String RootName="File Manager";
	
	public String DateFormat="yyyy/MM/dd HH:mm:ss";
	
	protected Map<String,String> Icons = new LinkedHashMap<String,String>();
	
	protected String Root="/";
	protected String OrigRoot="";
	
	private static final String[] SI_UNITS = { "B", "kB", "MB", "GB", "TB", "PB", "EB" };
	private static final String[] BINARY_UNITS = { "B", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB" };
	
	protected String CurrentPath="";
	protected String CurrentFullPath="";
	protected anywheresoftware.b4a.objects.collections.List Exts = new anywheresoftware.b4a.objects.collections.List();
	protected long MaxSize=1024*1024;
	protected String URL="";
			
	public void Initialize(ABMPage page, String id, String root, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_FILEMANAGER;		
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.FileManagers.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.FileManagers.get(themeName.toLowerCase()).Clone();				
			}
		}	
		IsInitialized=true;
		Exts.Initialize();
		
		Icons.put("gif", "fa fa-file-image-o");
		Icons.put("jpeg", "fa fa-file-image-o");
		Icons.put("jpg", "fa fa-file-image-o");
		Icons.put("png", "fa fa-file-image-o");
		Icons.put("bmp", "fa fa-file-image-o");

		Icons.put("pdf", "fa fa-file-pdf-o");

		Icons.put("doc", "fa fa-file-word-o");
		Icons.put("docx", "fa fa-file-word-o");

		Icons.put("ppt", "fa fa-file-powerpoint-o");
		Icons.put("pptx", "fa fa-file-powerpoint-o");

		Icons.put("xls", "fa fa-file-excel-o");
		Icons.put("xlsx", "fa fa-file-excel-o");

		Icons.put("aac", "fa fa-file-audio-o");
		Icons.put("mp3", "fa fa-file-audio-o");
		Icons.put("ogg", "fa fa-file-audio-o");
		Icons.put("wav", "fa fa-file-audio-o");

		Icons.put("avi", "fa fa-file-video-o");
		Icons.put("flv", "fa fa-file-video-o");
		Icons.put("mkv", "fa fa-file-video-o");
		Icons.put("mp4", "fa fa-file-video-o");
		Icons.put("mpg", "fa fa-file-video-o");		
		Icons.put("mpeg", "fa fa-file-video-o");
	    			
		Icons.put("gz", "fa fa-file-zip-o");
		Icons.put("zip", "fa fa-file-zip-o");
		Icons.put("rar", "fa fa-file-zip-o");		

		Icons.put("css", "fa fa-file-code-o");
		Icons.put("html", "fa fa-file-code-o");
		Icons.put("js", "fa fa-file-code-o");
		Icons.put("c", "fa fa-file-code-o");
		Icons.put("h", "fa fa-file-code-o");
		Icons.put("bas", "fa fa-file-code-o");

		Icons.put("txt", "fa fa-file-text-o");

		Root = root;
		OrigRoot=root;

	}
	
	public void SetUploadOptions(String url, long maxSize, anywheresoftware.b4a.objects.collections.List supportedExtensions) {
		Exts.Clear();
		if (supportedExtensions!=null && supportedExtensions.IsInitialized()) {
			Exts.AddAll(supportedExtensions);
		}
		MaxSize=maxSize;
		URL=url;
	}
	
	@Override
	protected void ResetTheme() {
		UseTheme(Theme.ThemeName);
	}
	
	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase();
	}
		
		
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}	
		
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.FileManagers.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.FileManagers.get(themeName.toLowerCase()).Clone();				
			}
		}
	}		
		
	@Override
	protected void CleanUp() {
		super.CleanUp();
	}
	
	@Override
	protected void RemoveMe() {
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
	}
	
	@Override
	protected void FirstRun() {
		Page.ws.Eval(BuildJavaScript(), null);
		
		super.FirstRun();
	}
	
	public String GetSelectedFiles() {
		String ret = "";
		if (Page.ws!=null) {
			String tmpVar = ParentString + this.ArrayName.toLowerCase() + this.ID.toLowerCase();
			tmpVar = tmpVar.replace("-", "_");
			SimpleFuture fut = Page.ws.EvalWithResult("return fileMans['" + tmpVar + "'].getSelected();", null);
			
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
	
	public void StartDownload(String url, String zipName, String jobCode) {
		String tmpVar = ParentString + this.ArrayName.toLowerCase() + this.ID.toLowerCase();
		tmpVar = tmpVar.replace("-", "_");
		if (Page.ws!=null) {
			Page.ws.Eval("fileMans['" + tmpVar + "'].StartDownload('" + url + "', '" + zipName + "', '" + jobCode + "');", null);
			try {
				if (Page.ws.getOpen())
				Page.ws.Flush();Page.RunFlushed();
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
	}
	
	public String getCurrentPath() {
		return CurrentPath;
	}
	
	public String getCurrentFullPath() {
		if (CurrentFullPath.endsWith("/")) {
			CurrentFullPath = CurrentFullPath.substring(0, CurrentFullPath.length()-1);
		}
		return CurrentFullPath;
	}
	
	public String getRootFolder() {
		return OrigRoot;
	}
	
	public void setRootFolder(String root) {
		this.Root = root;
		this.OrigRoot = root;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public anywheresoftware.b4a.objects.collections.List ExtractFileNamesFromJson(JSONParser json) throws JSONException {
		anywheresoftware.b4a.objects.collections.List ret = new anywheresoftware.b4a.objects.collections.List();
		ret.Initialize();
		anywheresoftware.b4a.objects.collections.Map mFiles = json.NextObject();
		ArrayList files = (ArrayList) mFiles.Get("files");
		for (int i=0;i<files.size();i++) {
			Map m = (Map) files.get(i);
			String s = (String) m.getOrDefault("name", "");
			if (!s.equals("")) {
				ret.Add(s);
			}
		}
		return ret;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public anywheresoftware.b4a.objects.collections.List ExtractFullPathFileNamesFromJson(JSONParser json, String path) throws JSONException {
		if (!path.endsWith("/")) {
			path = path + "/";
		}
		anywheresoftware.b4a.objects.collections.List ret = new anywheresoftware.b4a.objects.collections.List();
		ret.Initialize();
		anywheresoftware.b4a.objects.collections.Map mFiles = json.NextObject();
		ArrayList files = (ArrayList) mFiles.Get("files");
		for (int i=0;i<files.size();i++) {
			Map m = (Map) files.get(i);
			String s = (String) m.getOrDefault("name", "");
			if (!s.equals("")) {
				ret.Add(path + "/" + s);
			}
		}
		return ret;
	}
	
	/**
	 * The thumbnail folder must be in your www folder! 
	 * 
	 */
	public void LoadFolder(String folder, String thumbnailFolder, boolean buildImageThumbnailIfNotExists) {
		CurrentPath = folder;
		if (!Root.endsWith("/")) {
			Root = Root + "/";		
		}
		File rootFile = new File(Root);
		folder = Root + folder;
		CurrentFullPath = folder;
		
		File dir = new File(folder);
		String thumb = "";
		String name = "";
		String size = "";
		String date = "";
		String icon="";
		
		StringBuilder s = new StringBuilder();
		File dir2 = new File(thumbnailFolder);
		String thumbFolder=dir2.getName();
		String relPath="";		
		
		while (!dir2.getPath().endsWith("www")) {
			relPath+="../";
			dir2 = new File(dir2.getParent());
		}
		relPath=relPath.substring(0, relPath.length()-3);
		
		String lang = Page.DetectLanguage(Page.ws.getUpgradeRequest().GetHeader("Accept-Language"));
		Locale locale = localeFromString(lang);
		
		List<File> Folders= new ArrayList<File>();
		List<File> Files = new ArrayList<File>();
		
		for (File file: dir.listFiles()) {
			if (file.isDirectory()) {
				Folders.add(file);
			} else {
				Files.add(file);
			}
		}
		
		File[] foldersSort = Folders.toArray(new File[Folders.size()]);
		Arrays.sort(foldersSort); 
		File[] filesSort = Files.toArray(new File[Files.size()]);
		Arrays.sort(filesSort); 
		
		boolean isFirst=true;
		
		s.append("'{" + BuildBreadCrumbs(folder, rootFile) + ",\"folders\": [");
		
		BuildDirectoryTree(rootFile, dir, s,0);
		if (s.toString().endsWith(",")) {
			s.deleteCharAt(s.length()-1);	
		}
		
		s.append("], \"files\": [");
				
		for (File file: foldersSort) {
			if (!isFirst) {
				s.append(",");				
			}
			isFirst=false;
			icon = "folder";
			date = "";
			size = "";
			thumb = "";
			name = file.getName();
			s.append("{\"icon\": \"folder\",\"name\": \"" + name.replace("'", "\\\'") + "\",\"folder\": \"true\", \"size\": \"\", \"date\": \"\", \"image\": \"\"}");
		}
		
		for (File file: filesSort) {
			if (!isFirst) {
				s.append(",");				
			}
			isFirst=false;
	        name = file.getName();
	        thumb="";
	        String fileName = name.toLowerCase();
	        if (buildImageThumbnailIfNotExists) {
	        	if (fileName.endsWith("jpg") || fileName.endsWith("jpeg") || fileName.endsWith("png") || fileName.endsWith("bmp")) {
	        		try {
	        			MessageDigest digest = MessageDigest.getInstance("SHA-256");
				        byte[] hash = digest.digest(fileName.getBytes("UTF-8"));
				        StringBuffer hexString = new StringBuffer();

				        for (int i = 0; i < hash.length; i++) {
				            String hex = Integer.toHexString(0xff & hash[i]);
				            if(hex.length() == 1) hexString.append('0');
				            hexString.append(hex);
				        }
				        String encryptedString=hexString.toString(); 
		        		
		        		String extension = "";

		        		int i = fileName.lastIndexOf('.');
		        		if (i > 0) {
		        		    extension = fileName.substring(i+1);
		        		}
		        		
		        		encryptedString = encryptedString + "." + extension;
		        				        		
		        		File chkFile = new File(thumbnailFolder + File.separator + encryptedString);	        		
		        		if (!chkFile.exists()) {
		        			Utilities.CreateThumbnail(folder + File.separator + name, thumbnailFolder + File.separator + encryptedString, 64, 64);
		        		}
		        		thumb = relPath + thumbFolder + "/" + encryptedString;
					} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
						e.printStackTrace();
					}

	        	}        		 
	        	
	        }
	        int i = fileName.lastIndexOf(".");
		    if (i>-1) {
		      	icon = Icons.getOrDefault(fileName.substring(i+1), "fa fa-file-o");
		    } else {
		       	icon = "fa fa-file-o";
		    }
		    SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
		    date = sdf.format(file.lastModified());
	    	size = humanReadableByteCount(file.length(), true, locale);
	    	s.append("{\"icon\": \"" + icon + "\",\"name\": \"" + name.replace("'", "\\\'") + "\",\"folder\": \"false\", \"size\": \"" + size + "\", \"date\": \"" + date + "\", \"image\": \"" + thumb.replace("'", "\\\'") + "\"}");
		}
		s.append("]}';");
		
		String tmpVar = ParentString + this.ArrayName.toLowerCase() + this.ID.toLowerCase();
		tmpVar = tmpVar.replace("-", "_");
		
		StringBuilder sSend = new StringBuilder();
		
		sSend.append("fileMans['" + tmpVar + "'].clearFolder();");
		sSend.append("var " + tmpVar + "_json = " + s.toString());
		sSend.append("fileMans['" + tmpVar + "'].loadFolder(" + tmpVar + "_json);");
		
		Page.ws.Eval(sSend.toString(), null);
		
		try {
			if (Page.ws.getOpen())
			Page.ws.Flush();Page.RunFlushed();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	private static void BuildDirectoryTree(File folder, File targetFolder, StringBuilder sb, int level) {
		if (!folder.isDirectory()) {
	        return;
	    }
		boolean isActive=false;
	    boolean isOpen=false;
	    if (targetFolder.getAbsolutePath().startsWith(folder.getAbsolutePath())) {
	    	isOpen=true;
	    }
	    File[] foldersSort = folder.listFiles();
	    
	    if (targetFolder.getAbsolutePath().equals(folder.getAbsolutePath())) {
	    	isActive=true;
	    	boolean HasFolders=false;
	    	for (File file : foldersSort) {
	    		if (file.isDirectory()) {
	    			HasFolders=true;
	    		}
	    	}
	    	if (HasFolders) {
	    		isOpen=true;
	    	}
	    }
	    
	    sb.append("{\"name\": \"" + folder.getName().replace("'", "\\\'") + "\", \"open\": \"" + isOpen + "\", \"active\": \"" + isActive + "\", \"level\": \"" + level + "\"},");
	    if (!isOpen && !isActive) {
	    	return;
	    }
	    	    
		Arrays.sort(foldersSort);
	    
	    for (File file : foldersSort) {
	        if (file.isDirectory()) {
	            BuildDirectoryTree(file, targetFolder, sb, level + 1);
	        } 
	    }
	}

	
	protected static Locale localeFromString(String locale) {
	    String parts[] = locale.split("_", -1);
	    if (parts.length == 1) return new Locale(parts[0]);
	    else if (parts.length == 2
	            || (parts.length == 3 && parts[2].startsWith("#")))
	        return new Locale(parts[0], parts[1]);
	    else return new Locale(parts[0], parts[1], parts[2]);
	}
		
	protected static String humanReadableByteCount(final long bytes, final boolean useSIUnits, final Locale locale) 	{
	    final String[] units = useSIUnits ? SI_UNITS : BINARY_UNITS;
	    final int base = useSIUnits ? 1000 : 1024;

	    // When using the smallest unit no decimal point is needed, because it's the exact number.
	    if (bytes < base) {
	        return bytes + " " + units[0];
	    }

	    final int exponent = (int) (Math.log(bytes) / Math.log(base));
	    final String unit = units[exponent];
	    return String.format(locale, "%.1f %s", bytes / Math.pow(base, exponent), unit);
	}
		
	protected String BuildJavaScript() {
		File rootFile = new File(Root);
		StringBuilder s = new StringBuilder();	
		String tmpVar = ParentString + this.ArrayName.toLowerCase() + this.ID.toLowerCase();
		tmpVar = tmpVar.replace("-", "_");
		s.append("var " + tmpVar + "_Options = " + BuildFileOptions(rootFile) + ";");
		s.append("fileMans['" + tmpVar + "'] = new ABMFileManager('" +  ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "', " + tmpVar + "_Options);");
		
		return s.toString();
	}
	
	protected String BuildFileOptions(File RootFolder) {
		String thePath = "";
		File file = RootFolder;
		
		while (file!=null) {
			thePath = file.getName() + "/" + thePath;
			file = file.getParentFile();
		}
		if (thePath.startsWith("/")) {
			thePath = thePath.substring(1);
		}
		if (thePath.endsWith("/")) {
			thePath = thePath.substring(0, thePath.length()-1);
		}
		thePath = thePath + "/";
		String exts="null";
		if (Exts.getSize()>0) {
			exts = "[";
			for (int i=0; i< Exts.getSize();i++) {
				if (i > 0) {
					exts = exts + ",";
				}
				exts = exts + "\"" + Exts.Get(i) + "\""; 
			}
			exts = exts + "]";
		}
		if (URL.equals("")) {
			BA.Log("Didn't you forget to set the Upload Options?");
		}
		return "{\"Name\": \"" + TitleName.replace("'", "\\\'") + "\", \"Size\": \"" + TitleSize.replace("'", "\\\'") + "\", \"Date\": \"" + TitleDate.replace("'", "\\\'") + "\", \"isBig\": " + ShowBigIcons + ", \"Selected\": \"" + TitleSelected + "\", \"canUpload\": " + CanUpload + ", \"canDownload\": " + CanDownload + ", \"canCut\": "+ CanCut + ", \"canCopy\": " + CanCopy + ", \"canPaste\": " + CanPaste + ", \"canDelete\": " + CanDelete + ", \"canRename\": " + CanRename + ", \"canCreate\": " + CanCreateFolder + ", \"ttupload\": \"" + ToolTipUpload.replace("'", "\\\'") + "\", \"ttdwnload\": \"" + ToolTipDownload.replace("'", "\\\'") + "\", \"ttcut\": \""+ ToolTipCut.replace("'", "\\\'") + "\", \"ttcopy\": \"" + ToolTipCopy.replace("'", "\\\'") + "\", \"ttpaste\": \"" + ToolTipPaste.replace("'", "\\\'") + "\", \"ttdelete\": \"" + ToolTipDelete.replace("'", "\\\'") + "\", \"ttrename\": \"" + ToolTipRename.replace("'", "\\\'") + "\", \"ttcreate\": \"" + ToolTipCreateFolder.replace("'", "\\\'") + "\", \"root\": \"" + thePath.replace("'", "\\\'") + "\", \"maxSize\": " + MaxSize + ", \"Exts\": " + exts + ", \"url\": \"" + URL + "\", \"toastMax\": \"" + this.ToastMaximumFileSize + "\", \"toastType\": \"" + this.ToastFileTypeNotAllowed + "\", \"toastExt\": \"" + this.ToastFileExtensionNotAllowed + "\", \"theme\": \"" + Theme.ThemeName.toLowerCase() + "\", \"rtl\":" + getRightToLeft() + " }";
	}
	
	protected String BuildBreadCrumbs(String folder, File RootFolder) {
		StringBuilder s = new StringBuilder();
		s.append("\"crumbs\": [");
		s.append("{\"name\": \"" + RootName.replace("'", "\\\'") + "\"},");
		File file = new File(folder);
		String fileName = file.getName();
		String thePath = "";
		if (!file.getAbsolutePath().equals(RootFolder.getAbsolutePath())) {
			file = file.getParentFile();
			while (file!=null && !file.getAbsolutePath().equals(RootFolder.getAbsolutePath())) {
				thePath = file.getName() + "/" + thePath;
				file = file.getParentFile();
			}
			if (thePath.startsWith("/")) {
				thePath = thePath.substring(1);
			}
			if (thePath.endsWith("/")) {
				thePath = thePath.substring(0, thePath.length()-1);
			}
		}
	
		s.append("{\"name\": \"" + fileName.replace("'", "\\\'") + "\", \"fullPath\": \"" + thePath.replace("'", "\\\'") + "\"}");		
		s.append("]");	
		
		return s.toString();
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		// TODO ExtraClasses not working
		ABMaterial.ChangeVisibility(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), mVisibility);
		if (mIsPrintableClass.equals("")) {
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "no-print");
		} else {
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "no-print");
		}
		if (mIsOnlyForPrintClass.equals("")) {
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "only-print");
		} else {
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "only-print");
		}
		File rootFile = new File(Root);
		StringBuilder s = new StringBuilder();	
		String tmpVar = ParentString + this.ArrayName.toLowerCase() + this.ID.toLowerCase();
		tmpVar = tmpVar.replace("-", "_");
		s.append("var " + tmpVar + "_Options = " + BuildFileOptions(rootFile) + ";");
		if (Page.ws!=null) {
			Page.ws.Eval("fileMans['" + tmpVar + "'].RefreshOptions(" + tmpVar + "_Options);", null);
			try {
				if (Page.ws.getOpen())
				Page.ws.Flush();Page.RunFlushed();
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
		super.Refresh();		
	}
	
	@Override
	protected String Build() {
		if (Theme.ThemeName.equals("default")) {
			Theme.Colorize(Page.CompleteTheme.MainColor);
		}
		StringBuilder s = new StringBuilder();	
		
		s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"" + BuildStyle() + "\" class=\"abmfilemanager abmfilemanager" + Theme.ThemeName.toLowerCase() + " " + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() + " notselectable " + Theme.ZDepth  + "\">");
				
		s.append("</div>");
		IsBuild=true;
		return s.toString();
	}
	
	@Hide
	protected String BuildStyle() {			
		StringBuilder s = new StringBuilder();
		
		if (MarginTop!="") s.append(" margin-top: " + MarginTop + ";");
		if (MarginBottom!="") s.append(" margin-bottom: " + MarginBottom + ";");
		if (MarginLeft!="") s.append(" margin-left: " + MarginLeft + ";");
		if (MarginRight!="") s.append(" margin-right: " + MarginRight + ";");
		if (PaddingTop!="") s.append(" padding-top: " + PaddingTop + ";");
		if (PaddingBottom!="") s.append(" padding-bottom: " + PaddingBottom + ";");
		if (PaddingLeft!="") s.append(" padding-left: " + PaddingLeft + ";");
		if (PaddingRight!="") s.append(" padding-right: " + PaddingRight + ";");
		
		s.append("direction: ltr;");
		
		s.append(" width: 100%");
		
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
		return s.toString();
	}
	
	protected String BuildText(String text) {
		StringBuilder s = new StringBuilder();	
		
		String v = ABMaterial.HTMLConv().htmlEscape(text, Page.PageCharset);
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
				repl = "<i>" + Page.svgIconmap.getOrDefault(IconName.toLowerCase(), "") + "</i>"; 
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
	
	@Override
	protected ABMComponent Clone() {
		ABMFileManager c = new ABMFileManager();
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
