package com.ab.abmaterial;

import java.io.IOException;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.SimpleFuture;

@Author("Alain Bailleul")  
@ShortName("ABMFileInput")
@Events(values={"Changed(value As String)"})
public class ABMFileInput extends ABMComponent {	
	private static final long serialVersionUID = -6395967395506908004L;
	public ABMInput Path = new ABMInput();
	public ABMButton Button = new ABMButton();
	protected int SizeSmall=12;
	protected int SizeLarge=12;
	protected int SizeMedium=12;	
	protected int OffsetSmall=0;
	protected int OffsetMedium=0;
	protected int OffsetLarge=0;
	protected String PathPlaceHolderText = "";
	protected String PathThemeName = "";
	protected String ButtonText = "";	
	protected String ButtonThemeName = "";
	protected boolean DoValidation = true;
	public String UploadHandler="abmuploadhandler";
	
	public String InputFieldVisibility="";
	
	public String Accepts="";
			
	public void SetFocus() {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());		
		Page.ws.RunFunction("inputsetfocus", Params);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("FileInput SetFocus : " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
			
	public void Initialize(ABMPage page, String id, String pathPlaceholderText, String buttonText, boolean doValidation, String pathThemeName, String buttonThemeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_FILEINPUT;
		this.Path.Initialize(page, id+"path", "text", "", false, pathThemeName);
		this.Path.PlaceHolderText = pathPlaceholderText;
		this.Path.RaiseChangedEvent = true;
		this.Path.setValidate(doValidation);
		this.Button.InitializeRaised(page, id+"button", "", "", buttonText, buttonThemeName);
		this.Button.Size = ABMaterial.BUTTONSIZE_LARGE;
		this.Button.IsFileButton=true;
		this.Button.IDFileButton=id;
		
		this.Path.IsFileInput=true;
		this.Path.IDFileInput=ID;
		IsInitialized=true;
	}
	
	public void InitializeWithSize(ABMPage page, String id, String pathPlaceholderText, String buttonText, boolean doValidation, int offsetSmall, int offsetMedium, int offsetLarge, int sizeSmall, int sizeMedium, int sizeLarge, String pathThemeName, String buttonThemeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_FILEINPUT;
		this.Path.InitializeWithSize(page, id+"path", "text", "", offsetSmall, offsetMedium, offsetLarge, sizeSmall, sizeMedium, sizeLarge , false, pathThemeName);
		this.Path.PlaceHolderText = pathPlaceholderText;
		this.Path.RaiseChangedEvent = true;
		this.Path.setValidate(doValidation);
		this.Button.InitializeRaised(page, id+"button", "", "", buttonText, buttonThemeName);
		this.Button.Size = ABMaterial.BUTTONSIZE_LARGE;
		this.Button.IsFileButton=true;
		this.Button.IDFileButton=id;
		
		this.Path.IsFileInput=true;
		this.Path.IDFileInput=ID;
		IsInitialized=true;
	}
	
	public void setValid(String valid) {
		this.Path.IsValid = valid;		
		this.Path.mValidate=false;
	}
	
	public String getValid() {
		return this.Path.IsValid;
	}
	
	public void setValidate(boolean valid) {
		this.Path.mValidate=valid;
		this.Path.IsValid="";
	}
	
	public boolean getValidate() {
		return this.Path.mValidate;
	}
	
	@Override
	protected void ResetTheme() {
		UseThemePath(Path.Theme.ThemeName);
		UseThemeButton(Button.Theme.ThemeName);
	}
	
	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase();
	}
	
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}	
	
	public void UseThemePath(String themeName) {
		this.Path.UseTheme(themeName);
	}
	
	public void UseThemeButton(String themeName) {
		this.Button.UseTheme(themeName);
	}
	
	public void SetTooltip(String text, String position, int delay) {
		this.Path.SetTooltip(text, position, delay);		
	}
	
	public String GetFileName() {
		StringBuilder s = new StringBuilder();
		s.append("var inp = document.getElementById('" + ArrayName.toLowerCase() + ID.toLowerCase() + "buttoninput');");
		s.append("if (inp.files[0]) {return inp.files[0].name} else {return ''}");
		
		SimpleFuture ret = Page.ws.EvalWithResult(s.toString(), null);
		try {
			String retvalue = (String) ret.getValue();
			return retvalue;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public void Clear() {
		this.Path.setText("");
		this.Path.Refresh();
		StringBuilder s = new StringBuilder();
		s.append("var inp = document.getElementById('" + ArrayName.toLowerCase() + ID.toLowerCase() + "buttoninput');");
		s.append("inp.value=null;");
		Page.ws.Eval(s.toString(), null);
		try {
			if (Page.ws.getOpen())
			Page.ws.Flush();Page.RunFlushed();
		} catch (IOException e) {			
			e.printStackTrace();
		}		
	}
	
	public void UploadToServer() {
		StringBuilder s = new StringBuilder();
		
		s.append("var inp = document.getElementById('" + ArrayName.toLowerCase() + ID.toLowerCase() + "buttoninput');");
		
		s.append("var files = inp.files;");
		s.append("var formData = new FormData();");
		s.append("var file = files[0];");
		s.append("formData.append('upl', file, file.name);");
		s.append("var xhr = new XMLHttpRequest();");
		s.append("xhr.open('POST', '" + UploadHandler.toLowerCase() + "', true);");
		s.append("xhr.onload = function () {");
		s.append("if (xhr.status === 200) {");			
						
		s.append("} else {");
		
		s.append("}");
		s.append("};");
		s.append("xhr.send(formData);");
		
		Page.ws.Eval(s.toString(), null);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("FileInput UploadToServer : " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
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
		
		anywheresoftware.b4a.objects.collections.List Params2 = new anywheresoftware.b4a.objects.collections.List();
		Params2.Initialize();		
		Page.ws.RunFunction("reinitinputfields", Params2);
		super.FirstRun();
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		// TODO ExtraClasses not working
		super.Refresh();
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
		this.Button.RefreshInternal(DoFlush);
		Path.Accepts = Accepts;
		this.Path.RefreshInternal(DoFlush);	
		FirstRun(); // ALAIN 161203
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("FileInput Refresh : " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
	}
	
	@Override
	protected String Build() {
		
		StringBuilder s = new StringBuilder();		
		String rtl="";
		String padRight="margin-left: 10px;";
		if (this.getRightToLeft()) {
			rtl = " abmrtl ";
			padRight="margin-right: 10px;padding-right: 10px;padding-left: 0px";
		}
		s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"file-field input-field input-field" + Path.Theme.ThemeName.toLowerCase() + " " + mVisibility + " " + rtl + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() + "\">");		
		s.append(Button.Build());
		s.append("<div class=\"file-path-wrapper " + InputFieldVisibility + "\" style=\"" + padRight + "\">");
		Path.IsFileInputDirectory = false; //SelectDirectory;
		Path.Accepts = Accepts;
		s.append(Path.Build());
		s.append("</div>\n");
		s.append("</div>\n");
	
		IsBuild=true;
		return s.toString();
	}
		
	@Hide
	protected String BuildClass() {			
		return "";
	}
	
	@Hide
	protected String BuildBody() {
		return "";
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMFileInput c = new ABMFileInput();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;		
		c.Type = Type;
		c.mVisibility = mVisibility;	
		
		return c;
	}

}
