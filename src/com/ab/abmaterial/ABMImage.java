package com.ab.abmaterial;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;
import anywheresoftware.b4j.object.WebSocket.SimpleFuture;

@Author("Alain Bailleul")  
@ShortName("ABMImage")
@Events(values={"Clicked(Target As String)","ImagePicked(urlData as String)"})
public class ABMImage extends ABMComponent {
	
	private static final long serialVersionUID = 1549526145559062987L;
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	public boolean IsResponsive=false;
	public boolean IsCircular=false;
	public String Source="";
	public String SourceToggle="";
	public boolean IsMaterialBoxed=false;
	public String Caption="";
	public double Opacity=1;
	protected boolean mToggled=false;
	protected transient SimpleFuture FutureState=null;
	protected boolean IsDirty=false;
	protected int Width=0;
	protected int Height=0;
	public String Alt="";
	public boolean IsClickable=true;
	public String ResponsiveMaxWidth="";
	
	public boolean IsTextSelectable=true;
	
	public String ZDepth="";
	
	protected String mExtraStyle="";
	
	public boolean FilePicker=false;
		
	public void Initialize(ABMPage page, String id, String source, double opacity) {
		this.ID = id;			
		this.Type = ABMaterial.UITYPE_IMAGE;
		this.Source = source;
		this.Page = page;
		this.Opacity = opacity;
		IsInitialized=true;
	}	
	
	public void InitializeAsToggle(ABMPage page, String id, String source1, String source2, boolean initialShowFirst, double opacity) {
		this.ID = id;			
		this.Type = ABMaterial.UITYPE_IMAGE;
		this.Source = source1;
		this.SourceToggle = source2;
		this.Page = page;
		this.Opacity = opacity;
		this.mToggled = !initialShowFirst;
		IsInitialized=true;
	}	
	
	public void SetExtraStyle(String extraStyle) {
		if (extraStyle.length()>0) {
			if (!extraStyle.endsWith(";")) {
				extraStyle = extraStyle + ";";
			}
		}
		mExtraStyle = extraStyle;
	}
	
	@Override
	protected void ResetTheme() {
		//UseTheme(Theme.ThemeName);
	}
	
	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase();
	}
	
	public void SetFixedSize(int widthPx, int heightPx) {
		this.Width = widthPx;
		this.Height = heightPx;
	}
	
	public void setToggled(boolean isToggled) {
		mToggled=isToggled;
		IsDirty=true;
	}
		
	public boolean getToggled() {
		if (!(FutureState==null)) {
			try {
				if (FutureState.getValue().equals("1")) {
					mToggled=true;
				} else {
					mToggled=false;
				}					
			} catch (InterruptedException e) {					
				e.printStackTrace();
			} catch (TimeoutException e) {					
				e.printStackTrace();
			} catch (ExecutionException e) {					
				e.printStackTrace();
			} catch (IOException e) {					
				e.printStackTrace();
			}
			
		} else {
			BA.Log("FutureState = null");
			mToggled=false;
		}
		
		return mToggled;
	}
	
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}
	
	@Override
	protected void RemoveMe() {
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
	}
	
	@Override
	protected void FirstRun() {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());		
		Page.ws.RunFunction("inittooltipped", Params);
		if (IsMaterialBoxed) {
			anywheresoftware.b4a.objects.collections.List Params2 = new anywheresoftware.b4a.objects.collections.List();
			Params2.Initialize();
			Params2.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());		
			Page.ws.RunFunction("reinitmaterialbox", Params2);	
		}
		if (FilePicker) {
			StringBuilder s = new StringBuilder();
			String evName = ParentString.toLowerCase() + ArrayName.toLowerCase() + ID.toLowerCase();
			s.append("");
			s.append("var input = document.getElementById('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-input');");
			s.append("input.addEventListener('change', function (e) {");
			s.append("var files = e.target.files;");
			s.append("var done = function (url) {");
			s.append("input.value = '';");
			s.append("b4j_raiseEvent('page_parseevent', {'eventname': '");
			s.append(evName);
			s.append("_imagepicked','eventparams': 'urldata','urldata': url});");
			s.append("};");
			s.append("var reader;");
			s.append("var file;");
			s.append("var url;");
			s.append("if (files && files.length > 0) {");
			s.append("file = files[0];");
			s.append("if (URL) {");
			s.append("done(URL.createObjectURL(file));");
			s.append("} else if (FileReader) {");
			//s.append("if (FileReader) {");
			s.append("reader = new FileReader();");
			s.append("reader.onload = function (e) {");
			s.append("done(reader.result);");
			s.append("};");
			s.append("reader.readAsDataURL(file);");
			s.append("}");
			s.append("}");
			s.append("});");
			Page.ws.Eval(s.toString(), null);
			try {
				Page.ws.Flush();
			} catch (IOException e) {				
			}
		}
		super.FirstRun();	
	}
	
	public void SetTooltip(String text, String position, int delay) {
		this.ToolTipText = text;
		this.ToolTipPosition = position;
		this.ToolTipDelay = delay;			
	}
	
	@Override	
	protected String Build() {
		StringBuilder s = new StringBuilder();	
		String toolTip="";
		if (!ToolTipText.equals("")) {
			toolTip=" data-position=\"" + ToolTipPosition + "\" data-delay=\"" + ToolTipDelay + "\" data-tooltip=\"" + ToolTipText + "\" "; 
		}		
		String cursor="";
		if (!SourceToggle.equals("")) {
			cursor=" ;cursor: pointer";
		}
		String WidthHeight="";
		if (Width!=0 || Height!=0) {
			WidthHeight = " width=\"" + Width + "px\" height=\"" + Height + "px\" ";
		} else {
			if (IsResponsive) { // && SourceToggle.equals("")) {
				WidthHeight = " width=\"100%\" height=\"auto\" ";
			}
		}
		String selectable="";
		if (!IsTextSelectable) {
			selectable = " notselectable ";
		}
		String RespMaxWidth="";
		if (!ResponsiveMaxWidth.equals("")) {
			RespMaxWidth=" ;max-width: " +ResponsiveMaxWidth;
		}
		
		if (FilePicker) {
	    	s.append("<label class=\"label\">");
	    }
		
		if (SourceToggle.equals("")) {
			String onclick="";
			String cursor2="";
			if (!IsMaterialBoxed && IsClickable) {
				onclick=" onclick=\"imageclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "')\" ";
				cursor2 = " ;cursor: pointer";
			}
			if (Caption.equals("")) {
				s.append("<img alt=\"" + Alt + "\" " + WidthHeight + toolTip + onclick + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" src=\"" + Source + "\" class=\"" + BuildClass() + selectable + "\" data-caption=\" \" style=\"opacity: " + Opacity + cursor2 + RespMaxWidth + ";" + mExtraStyle + ";\" >")	;
			} else {
				s.append("<img alt=\"" + Alt + "\" " + WidthHeight + toolTip + onclick + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" src=\"" + Source + "\" class=\"" + BuildClass() + selectable + "\" data-caption=\"" + ABMaterial.HTMLConv().htmlEscape(Caption, Page.PageCharset) + "\"  style=\"opacity: " + Opacity + cursor2 + RespMaxWidth + ";" + mExtraStyle + ";\">")	;
			}
		} else {
			if (!mToggled) {
				s.append("<img alt=\"" + Alt + "\" abmtoggle=\"0\" " + WidthHeight + toolTip + " onclick=\"imagetogglearray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "','1','" + SourceToggle + "','" + Source + "')\" id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" src=\"" + Source + "\" class=\"" + BuildClass() + selectable + "\" data-caption=\"" + ABMaterial.HTMLConv().htmlEscape(Caption, Page.PageCharset) + "\" style=\"opacity: " + Opacity  + cursor + RespMaxWidth + ";-webkit-tap-highlight-color: rgba(0,0,0,0);" + mExtraStyle + ";\" >");
			} else {
				s.append("<img alt=\"" + Alt + "\" abmtoggle=\"1\" " + WidthHeight + toolTip + " onclick=\"imagetogglearray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "','0','" + Source + "','" + SourceToggle + "')\" id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" src=\"" + SourceToggle + "\" class=\"" + BuildClass() + selectable + "\" data-caption=\"" + ABMaterial.HTMLConv().htmlEscape(Caption, Page.PageCharset) + "\"  style=\"opacity: " + Opacity  + cursor  + RespMaxWidth + ";-webkit-tap-highlight-color: rgba(0,0,0,0);" + mExtraStyle + ";\">");
			}
		}		
		if (FilePicker) {
			s.append("<input type=\"file\" class=\"sr-only\" id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-input\" name=\"image\" accept=\"image/*\"></label>");
		}
		IsBuild=true;
		return s.toString();
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		super.Refresh();
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		
		String selectable="";
		if (!IsTextSelectable) {
			selectable = " notselectable ";
		}
		j.SetProp("class", BuildClass() + selectable);
		
		String cursor="";
		if (!SourceToggle.equals("") || IsClickable) {
			cursor=" ;cursor: pointer";
		}
		
		String RespMaxWidth="";
		if (!ResponsiveMaxWidth.equals("")) {
			RespMaxWidth=" ;max-width: " +ResponsiveMaxWidth;
		}
		
		j.SetProp("style",  "opacity: " + Opacity + cursor + RespMaxWidth + ";" + mExtraStyle);
		if (Caption.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-caption", " ");
		} else {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-caption", ABMaterial.HTMLConv().htmlEscape(Caption, Page.PageCharset));
		}
		if (!ToolTipText.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-position", ToolTipPosition);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-delay", "" + ToolTipDelay);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-tooltip", ToolTipText);
		}
		if (Width!=0 || Height!=0) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "width", Width + "px");
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "height", Height + "px");
		} else {
			if (IsResponsive) {
				ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "width", "100%");
				ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "height", "auto");
			} else {
				ABMaterial.RemoveProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "width");
				ABMaterial.RemoveProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "height");
			}
		}
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "alt", Alt);
		if (SourceToggle.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "src", Source);
		} else {
			if (IsDirty) {
				if (!mToggled) {
					j.SetProp("src", Source);	
					ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "abmtoggle", "0");
					j.SetProp("onclick", "imagetogglearray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "','1','" + SourceToggle + "','" + Source + "'))");
				} else {
					j.SetProp("src", SourceToggle);
					ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "abmtoggle", "1");
					j.SetProp("onclick", "imagetogglearray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "','0','" + Source + "','" + SourceToggle + "'))");
				}
			}
		}
		
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());			
		Page.ws.RunFunction("initboxed", Params);
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Image Refresh: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
		
	}
	
	@Hide
	protected String BuildClass() {			
		StringBuilder s = new StringBuilder();
		s.append(super.BuildExtraClasses());
		if (!ToolTipText.equals("")) {
			s.append("tooltipped ");
		}
		s.append(mVisibility + " ");
		
		if (IsCircular) {
			s.append("circle ");
		}
		if (IsMaterialBoxed) {
			s.append("materialboxed ");
		}
		s.append(ZDepth + " ");
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
		ABMImage c = new ABMImage();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;
		c.Type = Type;
		c.mVisibility = mVisibility;	
		c.ToolTipDelay = ToolTipDelay;
		c.ToolTipPosition = ToolTipPosition;
		c.ToolTipText = ToolTipText;
		c.Caption = Caption;
		c.FutureState = FutureState;
		c.IsCircular = IsCircular;
		c.IsDirty = false;
		c.IsMaterialBoxed = IsMaterialBoxed;
		c.IsResponsive = IsResponsive;
		c.mToggled = mToggled;
		c.Opacity = Opacity;		
		c.Source = Source;
		c.SourceToggle = SourceToggle;
		return c;
	}
}
