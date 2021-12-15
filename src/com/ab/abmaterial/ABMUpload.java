package com.ab.abmaterial;

import java.io.IOException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;

@Author("Alain Bailleul")  
@ShortName("ABMUpload")
public class ABMUpload extends ABMComponent{
	
	private static final long serialVersionUID = -3726377615751215282L;
	protected ThemeUpload Theme=new ThemeUpload();
	public String Message="";
	protected String ButtonText="";
	protected boolean UploadAfterDrop=false;
	public String UploadHandler="abmuploadhandler";
	
	public void Initialize(ABMPage page, String id, String message, String buttonText, String themeName) {
		this.ID = id;
		this.Page = page;
		this.Type = ABMaterial.UITYPE_UPLOAD;
		this.Message=message;
		this.ButtonText=buttonText;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Uploads.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Uploads.get(themeName.toLowerCase()).Clone();				
			}
		}
		IsInitialized=true;
	}
	
	@Override
	protected void ResetTheme() {
		UseTheme(Theme.ThemeName);		
	}
	
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Uploads.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Uploads.get(themeName.toLowerCase()).Clone();				
			}
		}
	}
	
	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase() + Theme.ThemeName.toLowerCase();
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
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + Theme.ThemeName.toLowerCase());
	}
	
	@Override
	protected void FirstRun() {
		Page.ws.Eval(BuildJavaScript(), null);
		
		super.FirstRun();
	}

	protected String BuildJavaScript() {
		StringBuilder s = new StringBuilder();
		String id = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		String relPath="../../"; // FIXED! But how can it be calculated?
				
		s.append("$.stylesheet('#upload" + id.toLowerCase() + Theme.ThemeName.toLowerCase() + "', {'font-family':'\"PT Sans Narrow\", sans-serif','background-color':'" + ABMaterial.GetColorStrMap(Theme.BackColor,Theme.BackColorIntensity) + "','width':'300px','padding':'30px','border-radius':'3px','margin':'0px auto 0px'});");
		if (ABMaterial.CDN) {
			s.append("$.stylesheet('#drop" + id.toLowerCase() + Theme.ThemeName.toLowerCase() + "', {'background-color': '" + ABMaterial.GetColorStrMap(Theme.DropZoneBackColor,Theme.DropZoneBackColorIntensity) + "','padding': '40px 50px','margin-bottom': '30px','border': '20px solid rgba(0,0,0,0)','border-radius': '3px','border-image': 'url(\"" + ABMaterial.mCDNUrl + "js/border-image.png\") 25 repeat','text-align': 'center','text-transform': 'uppercase','font-size':'16px','font-weight':'bold','color':'" + ABMaterial.GetColorStrMap(Theme.ForeColor,Theme.ForeColorIntensity) + "'});");
		} else {
			s.append("$.stylesheet('#drop" + id.toLowerCase() + Theme.ThemeName.toLowerCase() + "', {'background-color': '" + ABMaterial.GetColorStrMap(Theme.DropZoneBackColor,Theme.DropZoneBackColorIntensity) + "','padding': '40px 50px','margin-bottom': '30px','border': '20px solid rgba(0,0,0,0)','border-radius': '3px','border-image': 'url(\"" + relPath + "js/border-image.png\") 25 repeat','text-align': 'center','text-transform': 'uppercase','font-size':'16px','font-weight':'bold','color':'" + ABMaterial.GetColorStrMap(Theme.ForeColor,Theme.ForeColorIntensity) + "'});");
		}
		s.append("$.stylesheet('#drop" + id.toLowerCase() + Theme.ThemeName.toLowerCase() + " a', {'background-color':'" + ABMaterial.GetColorStrMap(Theme.ButtonBackColor,Theme.ButtonBackColorIntensity) + "','padding':'12px 26px','color':'" + ABMaterial.GetColorStrMap(Theme.ButtonForeColor,Theme.ButtonForeColorIntensity) + "','font-size':'14px','border-radius':'2px','cursor':'pointer','display':'inline-block','margin-top':'12px','line-height':'1','margin-left': '-20px','margin-right': '-20px'});");
		s.append("$.stylesheet('#drop" + id.toLowerCase() + Theme.ThemeName.toLowerCase() + " a:hover', {'background-color':'" + ABMaterial.GetColorStrMap(Theme.ButtonHoverBackColor,Theme.ButtonHoverBackColorIntensity) + "'});");
		s.append("$.stylesheet('#drop" + id.toLowerCase() + Theme.ThemeName.toLowerCase() + " input', {'display':'none'});");
		s.append("$.stylesheet('#upload" + id.toLowerCase() + Theme.ThemeName.toLowerCase() + "  ul', {'list-style':'none','margin':'0 -30px','border-top':'1px solid " + ABMaterial.GetColorStrMap(Theme.UploadBorderColor,Theme.UploadBorderColorIntensity) + "','border-bottom':'1px solid " + ABMaterial.GetColorStrMap(Theme.UploadBorderColor,Theme.UploadBorderColorIntensity) + "'});");
		s.append("$.stylesheet('#upload" + id.toLowerCase() + Theme.ThemeName.toLowerCase() + "  ul li', {'background-color':'" + ABMaterial.GetColorStrMap(Theme.UploadBackColor,Theme.UploadBackColorIntensity) + "','border-top':'1px solid " + ABMaterial.GetColorStrMap(Theme.UploadBorderColor,Theme.UploadBorderColorIntensity) + "','border-bottom':'1px solid " + ABMaterial.GetColorStrMap(Theme.UploadBorderColor,Theme.UploadBorderColorIntensity) + "','padding':'15px','height': '78px','position': 'relative'});");
		s.append("$.stylesheet('#upload" + id.toLowerCase() + Theme.ThemeName.toLowerCase() + "  ul li input', {'display': 'none'});");
		s.append("$.stylesheet('#upload" + id.toLowerCase() + Theme.ThemeName.toLowerCase() + "  ul li p', {'width': '166px','overflow': 'hidden','white-space': 'nowrap','color':'" + ABMaterial.GetColorStrMap(Theme.UploadFileColor,Theme.UploadFileColorIntensity) + "','font-size': '14px','font-weight': 'bold','position': 'absolute','top': '0px','left': '78px'});");
		s.append("$.stylesheet('#upload" + id.toLowerCase() + Theme.ThemeName.toLowerCase() + "  ul li i', {'font-weight': 'normal','font-style':'normal','color':'" + ABMaterial.GetColorStrMap(Theme.UploadFileSizeColor,Theme.UploadFileSizeColorIntensity) + "','display':'block'});");
		
		s.append("$.stylesheet('#upload" + id.toLowerCase() + Theme.ThemeName.toLowerCase() + "  ul li canvas', {'top': '15px','left': '15px','position': 'absolute'});");
		if (ABMaterial.CDN) {
			s.append("$.stylesheet('#upload" + id.toLowerCase() + Theme.ThemeName.toLowerCase() + "  ul li span', {'width': '15px','height': '12px','background': 'url(\"" + ABMaterial.mCDNUrl + "js/icons.png\") no-repeat','position':'absolute','top': '34px','right': '33px','cursor':'pointer'});");
		} else {
			s.append("$.stylesheet('#upload" + id.toLowerCase() + Theme.ThemeName.toLowerCase() + "  ul li span', {'width': '15px','height': '12px','background': 'url(\"" + relPath + "js/icons.png\") no-repeat','position':'absolute','top': '34px','right': '33px','cursor':'pointer'});");
		}
		s.append("$.stylesheet('#upload" + id.toLowerCase() + Theme.ThemeName.toLowerCase() + "  ul li.working span', {'height': '16px','background-position': '0 -12px'});");
		s.append("$.stylesheet('#upload" + id.toLowerCase() + Theme.ThemeName.toLowerCase() + "  ul li.error p', {'color':'" + ABMaterial.GetColorStrMap(Theme.ErrorColor,Theme.ErrorColorIntensity) + "'});");
		
		s.append("var ul = $('#upload" + id.toLowerCase() + Theme.ThemeName.toLowerCase() + " ul');");
		s.append("$('#drop" + id.toLowerCase() + Theme.ThemeName.toLowerCase() + " a').off('click').click(function() {");
		s.append("$(this).parent().find('input').click();");
		s.append("});");
		s.append("$('#upload" + id.toLowerCase() + Theme.ThemeName.toLowerCase() + "').fileupload({");
		s.append("dropZone: $('#drop" + id.toLowerCase() + Theme.ThemeName.toLowerCase() + "'),");
		s.append("add: function (e, data) {");
		s.append("var tpl = $('<li class=\"working\"><input type=\"text\" value=\"0\" data-width=\"48\" data-height=\"48\"'+' data-fgColor=\"" + ABMaterial.GetColorStrMap(Theme.UploadProgressColor,Theme.UploadProgressColorIntensity) + "\" data-readOnly=\"1\" data-bgColor=\"" + ABMaterial.GetColorStrMap(Theme.UploadBackColor,Theme.UploadBackColorIntensity) + "\" /><p></p><span></span></li>');");
		s.append("tpl.find('p').text(data.files[0].name).append('<br><i>' + formatFileSize(data.files[0].size) + '</i>');");
		s.append("data.context = tpl.appendTo(ul);");
		s.append("tpl.find('input').knob();");
		s.append("tpl.find('span').click(function(){");
		s.append("if(tpl.hasClass('working')){");
		s.append("jqXHR.abort();");
		s.append("}");
		s.append("tpl.fadeOut(function(){");
		s.append("tpl.remove();");
		s.append("});");
		s.append("});");
		s.append("var jqXHR = data.submit();");
		s.append("},");
		s.append("progress: function(e, data){");
		s.append("var progress = parseInt(data.loaded / data.total * 100, 10);");
		s.append("data.context.find('input').val(progress).change();");
		s.append("if(progress == 100){");
		s.append("data.context.removeClass('working');");
		s.append("}");
		s.append("},");
		s.append("fail:function(e, data){");            
		s.append("data.context.addClass('error');");
		s.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_fileuploaded','eventparams': 'filename,success', 'filename': data.files[0].name, 'success': false});");
		s.append("}");
		s.append("});");    
		s.append("$(document).on('drop dragover', function (e) {");
		s.append("e.preventDefault();");
		s.append("});");    
		s.append("function formatFileSize(bytes) {");
		s.append("if (typeof bytes !== 'number') {");
		s.append("return '';");
		s.append("}");
		s.append("if (bytes >= 1000000000) {");
		s.append("return (bytes / 1000000000).toFixed(2) + ' GB';");
		s.append("}");
		s.append("if (bytes >= 1000000) {");
		s.append("return (bytes / 1000000).toFixed(2) + ' MB';");
		s.append("}");
		s.append("return (bytes / 1000).toFixed(2) + ' KB';");
		s.append("}");
		
		return s.toString();
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		// TODO ExtraClasses not working
		super.Refresh();
		ABMaterial.ChangeVisibility(Page, "upload" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + Theme.ThemeName.toLowerCase(), mVisibility);
		if (mIsPrintableClass.equals("")) {
			ABMaterial.RemoveClass(Page, "upload" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + Theme.ThemeName.toLowerCase(), "no-print");
		} else {
			ABMaterial.AddClass(Page, "upload" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + Theme.ThemeName.toLowerCase(), "no-print");
		}
		if (mIsOnlyForPrintClass.equals("")) {
			ABMaterial.RemoveClass(Page, "upload" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + Theme.ThemeName.toLowerCase(), "only-print");
		} else {
			ABMaterial.AddClass(Page, "upload" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + Theme.ThemeName.toLowerCase(), "only-print");
		}
		
		JQueryElement j = Page.ws.GetElementById("drop" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		j.SetText(ABMaterial.HTMLConv().htmlEscape(Message, Page.PageCharset));
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Upload Refresh: " + ID);}
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
		s.append("<form class=\"" + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() + "\" id=\"upload" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + Theme.ThemeName.toLowerCase() + "\" method=\"post\" action=\"" + UploadHandler.toLowerCase() + "\" enctype=\"multipart/form-data\">\n");
		s.append("<div id=\"drop" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + Theme.ThemeName.toLowerCase() + "\">" + ABMaterial.HTMLConv().htmlEscape(Message, Page.PageCharset) + "<a>" + ABMaterial.HTMLConv().htmlEscape(ButtonText, Page.PageCharset) + "</a><input type=\"file\" name=\"upl\" multiple />\n");
		s.append("</div>\n");
		s.append("<ul>\n");
		s.append("<!-- The file uploads will be shown here -->\n");
		s.append("</ul>\n");
		s.append("</form>\n");
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
		ABMUpload c = new ABMUpload();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;
		c.Theme = Theme.Clone();
		c.Type = Type;
		c.mVisibility = mVisibility;	
		c.ButtonText = ButtonText;
		c.Message=Message;
		c.UploadAfterDrop=UploadAfterDrop;
		return c;
	}
}
