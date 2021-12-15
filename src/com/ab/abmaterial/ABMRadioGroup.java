package com.ab.abmaterial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;

@Author("Alain Bailleul")  
@ShortName("ABMRadioGroup")
@Events(values={"Clicked(Target As String)"})
public class ABMRadioGroup extends ABMComponent{
	
	private static final long serialVersionUID = -9075232225094331017L;
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	protected ThemeRadioGroup Theme=new ThemeRadioGroup();
	protected List<RadioButton> Buttons = new ArrayList<RadioButton>();
	protected int Active=0;
	
	protected boolean mEnabled=true;
	
	public String Title="";
	
	protected boolean IsDirty=false;	
	protected boolean GotLastChecked=true;
	
	public void Initialize(ABMPage page, String id, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_RADIOGROUP;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.RadioGroups.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.RadioGroups.get(themeName.toLowerCase()).Clone();				
			}
		}	
		IsInitialized=true;
		IsDirty=false;
		IsVisibilityDirty=false;
	}
	
	@Override
	protected void ResetTheme() {
		UseTheme(Theme.ThemeName);
	}
	
	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase();
	}
	
	public void AddRadioButton(String caption, boolean enabled) {
		RadioButton b = new RadioButton();
		b.Caption = caption;
		b.Enabled = enabled;
		b.ShowHorizontal=false;
		Buttons.add(b);
	}
	
	/**
	 * Will show the NEXT radio button without a line break IF the available width makes it possible. 
	 */
	public void AddRadioButtonNoLineBreak(String caption, boolean enabled) {
		RadioButton b = new RadioButton();
		b.Caption = caption;
		b.Enabled = enabled;
		b.ShowHorizontal=true;
		Buttons.add(b);
	}
	
	public boolean getEnabled() {	
		return mEnabled;
	}	
	
	public void setEnabled(boolean enabled) {
		IsEnabledDirty=true;
		GotLastEnabled=true;
		mEnabled=enabled;
	}
	
	public void SetActive(int index) {
		Active=index;
		IsDirty=true;
	}
	
	public int GetActive() {
		if (GotLastChecked) {
			return Active;
		}
		GotLastChecked=true;
		if (!IsDirty) {
			String ret = ABMaterial.GetActiveRadioButton(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() );
			if (!ret.equals("")) {
				Active=Integer.parseInt(ret);
			}
		}
		return Active;
	}
	
	@Override
	public String getVisibility() {
		if (GotLastVisibility) {
			return mVisibility;
		}
		GotLastVisibility=true;
		if (!mB4JSUniqueKey.equals("") && !IsVisibilityDirty) {
			if (JQ!=null) {
				anywheresoftware.b4a.objects.collections.List par = new anywheresoftware.b4a.objects.collections.List();
				par.Initialize();
				par.Add(ParentString + RootID());
				FutureVisibility = JQ.RunMethodWithResult("GetVisibility", par);				
			} else {
				FutureVisibility=null;				
			}
			if (!(FutureVisibility==null)) {
				try {
					//BA.Log(ID);
					this.mVisibility = ((String)FutureVisibility.getValue());					
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
				//BA.Log("FutureText = null");
				
			}	
			return mVisibility;
		}
		
		return mVisibility;		
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public void B4JSSetActive(int index) {
		
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public int B4JSGetActive() {		
		return 0;
	}
	
	public void ChangeRadioButton(int index, String caption, boolean enabled) {
		Buttons.get(index).Enabled = enabled;
		Buttons.get(index).Caption = caption;
	}
	
	public void RemoveRadioButton(int index) {		
		Buttons.remove(index);
	}
	
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}
	
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.RadioGroups.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.RadioGroups.get(themeName.toLowerCase()).Clone();				
			}
		}
	}

	public void SetTooltip(String text, String position, int delay) {
		this.ToolTipText = text;
		this.ToolTipPosition = position;
		this.ToolTipDelay = delay;			
	}
	
	/**
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
	public void B4JSOnClick(String B4JSClassName, String B4JSMethod, anywheresoftware.b4a.objects.collections.List Params) {
		PrepareEvent("B4JSOnClick", B4JSClassName, B4JSMethod, Params);				
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
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());		
		Page.ws.RunFunction("inittooltipped", Params);
		
		super.FirstRun();
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		super.RefreshInternal(DoFlush);
		
		GetActive();
		getVisibility();
				
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		
		ThemeRadioGroup sw = Theme;
		
		if (!IsDirty) {
			String ret = ABMaterial.GetActiveRadioButton(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() );
			if (!ret.equals("")) {
				Active=Integer.parseInt(ret);
			}
		}
				
		if (!ToolTipText.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "input", "data-position", ToolTipPosition);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "input", "data-delay", "" + ToolTipDelay);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "input", "data-tooltip", ToolTipText);
		}
		String toolTip2="";
		if (!ToolTipText.equals("")) {
			toolTip2=" tooltipped ";
		}
		
		j.SetProp("class", "abmrg " + toolTip2 + " " + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses());
		
		StringBuilder s = new StringBuilder();
		String MainDisabled="";
		if (!mEnabled) {
			MainDisabled= "disabled ";
		}
		if (!Title.equals("")) {
			s.append("<label id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "title\" class=\"radiotitle" + sw.ThemeName.toLowerCase() + MainDisabled + "\">" + BuildBody(Title) + "</label>\n");
		} else {
			s.append("<label id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "title\" class=\"hidden" + MainDisabled + "\">" + BuildBody(Title) + "</label>\n");
		}
		
		int Counter=0;
		s.append("<p>\n");
		for (RadioButton b: Buttons) {			
			String disabled="";
			if (!b.Enabled || !mEnabled) {
				disabled= " disabled ";
			}
			String checked="";
			if (Counter==Active) {
				checked= " checked ";
			}
			String WithGab="";
		
			s.append("<input rbnr=\"" + Counter + "\" class=\"radio" + sw.ThemeName.toLowerCase() + WithGab + "\"" + disabled + checked  + " name=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "input\" type=\"radio\" id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "input" + Counter + "\" onClick=\"radioclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "')\"/>\n"); 
			s.append("<label id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label" + Counter + "\" for=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "input" + Counter + "\" class=\"" + ABMaterial.GetColorStr(sw.LabelColor, sw.LabelColorIntensity, "text") + "\">");
			s.append(ABMaterial.HTMLConv().htmlEscape(b.Caption, Page.PageCharset));
			s.append("</label>\n");
			if (!b.ShowHorizontal && Counter<Buttons.size()-1) {
				s.append("</p><p>\n");
			}
			
			Counter++;
		}
		s.append("</p>\n");
		ABMaterial.ReplaceMyInnerHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() , s.toString());
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("RadioGroup Refresh 2: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {
				//e.printStackTrace();
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
		ThemeRadioGroup sw=Theme;
		
		String toolTip="";
		String toolTip2="";
		if (!ToolTipText.equals("")) {
			toolTip=" data-position=\"" + ToolTipPosition + "\" data-delay=\"" + ToolTipDelay + "\" data-tooltip=\"" + ToolTipText + "\" "; 
			toolTip2=" tooltipped ";
		}
		
		String B4JSData="";
		if (!mB4JSUniqueKey.equals("")) {
			B4JSData = " data-b4js=\"" + mB4JSUniqueKey + "\" data-b4jsextra=\"\" ";
		} else {
			B4JSData = " data-b4js=\"\" data-b4jsextra=\"\" ";
		}
		s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" " + B4JSData + " class=\"abmrg " + toolTip2 + " " + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() + "\" " + toolTip + " style=\"padding-left: 10px\">\n");
		String MainDisabled="";
		if (!mEnabled) {
			MainDisabled= "disabled ";
		}
		if (!Title.equals("")) {
			s.append("<label id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "title\" class=\"radiotitle" + sw.ThemeName.toLowerCase() + MainDisabled + "\">" + BuildBody(Title) + "</label>\n");
		} else {
			s.append("<label id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "title\" class=\"hidden " + MainDisabled + "\">" + BuildBody(Title) + "</label>\n");
		}
		int Counter=0;
		s.append("<p>\n");
		for (RadioButton b: Buttons) {			
			String disabled="";
			if (!b.Enabled || !mEnabled) {
				disabled= " disabled ";
			}
			String checked="";
			if (Counter==Active) {
				checked= " checked ";
			}
			String WithGab="";
			
			s.append("<input rbnr=\"" + Counter + "\" class=\"radio" + sw.ThemeName.toLowerCase() + WithGab + "\"" + disabled + checked + " name=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "input\" type=\"radio\" id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "input" + Counter + "\" onClick=\"radioclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "')\"/>\n"); 
			s.append("<label id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label" + Counter + "\" for=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "input" + Counter + "\" class=\"" + ABMaterial.GetColorStr(sw.LabelColor, sw.LabelColorIntensity, "text") + "\">");
			
			s.append(BuildBody(b.Caption));
			s.append("</label>\n");
			if (!b.ShowHorizontal && Counter<Buttons.size()-1) {
				s.append("</p><p>\n");
			}
			
			Counter++;
		}
		s.append("</p>\n");
		s.append("</div>\n");
		IsBuild=true;
		IsDirty=false;
		return s.toString();
	}
	
	@Hide
	protected String BuildClass() {			
		StringBuilder s = new StringBuilder();	
				
		return s.toString(); 
	}
		
	@Hide
	protected String BuildBody(String Text) {
		StringBuilder s = new StringBuilder();	
		
		String v = ABMaterial.HTMLConv().htmlEscape(Text, Page.PageCharset);
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
		ABMRadioGroup c = new ABMRadioGroup();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;
		c.Theme = Theme.Clone();
		c.Type = Type;
		c.mVisibility = mVisibility;	
		c.ToolTipDelay = ToolTipDelay;
		c.ToolTipPosition = ToolTipPosition;
		c.ToolTipText = ToolTipText;
		c.Title = Title;		
		for (RadioButton b: Buttons) {
			c.Buttons.add(b.Clone());
		}
		return c;
	}
	
	protected class RadioButton {
		protected String Caption="";
		protected boolean Enabled=true;
		protected boolean ShowHorizontal=false;
		
		protected RadioButton Clone() {
			RadioButton b = new RadioButton();
			b.Caption = Caption;
			b.Enabled = Enabled;
			b.ShowHorizontal=ShowHorizontal;
			return b;
		}
		
	}

}
