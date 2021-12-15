package com.ab.abmaterial;

import java.io.IOException;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;

@Author("Alain Bailleul")  
@ShortName("ABMPagination")
@Events(values={"PageChanged(OldPage as int, NewPage as int)"})
public class ABMPagination extends ABMComponent{
	
	private static final long serialVersionUID = -4054939734545791766L;
	protected ThemePagination Theme=new ThemePagination();
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	protected int ActivePage=1;
	protected boolean HasFirstLast=true;
	protected boolean HasPreviousNext=true;
	protected int MaxNumberOfPages=0;
	protected int TotalNumberOfPages=0;
	protected ABMButton first=null;
	protected ABMButton last=null;
	protected ABMButton next=null;
	protected ABMButton previous=null;
	protected List<ABMButton> buttons = new ArrayList<ABMButton>();
	protected int buttonCounter=1;
	
	public void Initialize(ABMPage page, String id, int maxNumberOfPages, boolean hasFirstLast, boolean hasPreviousNext, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.MaxNumberOfPages = maxNumberOfPages;
		this.HasFirstLast = hasFirstLast;
		this.HasPreviousNext = hasPreviousNext;
		this.Type = ABMaterial.UITYPE_PAGINATION;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Paginations.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Paginations.get(themeName.toLowerCase()).Clone();				
			}
		} else {
			themeName = Theme.ThemeName;
		}
		if (Theme.ThemeName.equals("default")) {
			Theme.Colorize(Page.CompleteTheme.MainColor);
		}
		if (hasFirstLast || hasPreviousNext) {
			Page.CompleteTheme.AddButtonTheme(themeName + "nav");
			Page.CompleteTheme.Button(themeName + "nav").BackColor = Theme.NavigationButtonsBackColor;
			Page.CompleteTheme.Button(themeName + "nav").BackColorIntensity = Theme.NavigationButtonsBackColorIntensity;
			Page.CompleteTheme.Button(themeName + "nav").ForeColor = Theme.NavigationButtonsForeColor;
			Page.CompleteTheme.Button(themeName + "nav").ForeColorIntensity = Theme.NavigationButtonsForeColorIntensity;
		}
		Page.CompleteTheme.AddButtonTheme(themeName + "number");
		Page.CompleteTheme.Button(themeName + "number").BackColor = Theme.InActivePageBackColor;
		Page.CompleteTheme.Button(themeName + "number").BackColorIntensity = Theme.InActivePageBackColorIntensity;
		Page.CompleteTheme.Button(themeName + "number").ForeColor = Theme.InActivePageForeColor;
		Page.CompleteTheme.Button(themeName + "number").ForeColorIntensity = Theme.InActivePageForeColorIntensity;
		Page.CompleteTheme.AddButtonTheme(themeName + "numberactive");
		Page.CompleteTheme.Button(themeName + "numberactive").BackColor = Theme.ActivePageBackColor;
		Page.CompleteTheme.Button(themeName + "numberactive").BackColorIntensity = Theme.ActivePageBackColorIntensity;
		Page.CompleteTheme.Button(themeName + "numberactive").ForeColor = Theme.ActivePageForeColor;
		Page.CompleteTheme.Button(themeName + "numberactive").ForeColorIntensity = Theme.ActivePageForeColorIntensity;
		
		if (hasFirstLast) {
			first = new ABMButton();
			first.InitializeFloating(Page, ParentString + ArrayName.toLowerCase() + id.toLowerCase() + "-first", "mdi-av-skip-previous", Theme.ThemeName + "nav");
			first.IDPagination = ArrayName.toLowerCase() + id.toLowerCase();
			first.TypePagination = "first";			
			last = new ABMButton();
			last.InitializeFloating(Page, ParentString + ArrayName.toLowerCase() + id.toLowerCase() + "-last", "mdi-av-skip-next", Theme.ThemeName + "nav");
			last.IDPagination = ArrayName.toLowerCase() + id.toLowerCase();
			last.TypePagination = "last";
		}
		if (hasPreviousNext) {
			previous = new ABMButton();
			previous.InitializeFloating(Page, ParentString + ArrayName.toLowerCase() + id.toLowerCase() + "-prev", "mdi-hardware-keyboard-arrow-left", Theme.ThemeName + "nav");
			previous.IDPagination = ArrayName.toLowerCase() + id.toLowerCase();
			previous.TypePagination = "prev";
			next = new ABMButton();
			next.InitializeFloating(Page, ParentString + ArrayName.toLowerCase() + id.toLowerCase() + "-next", "mdi-hardware-keyboard-arrow-right", Theme.ThemeName + "nav");
			next.IDPagination = ArrayName.toLowerCase() + id.toLowerCase();
			next.TypePagination = "next";
		}
		IsInitialized=true;
	}	
	
	@Override
	protected void ResetTheme() {
		UseTheme(Theme.ThemeName);		
	}
	
	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase();
	}
	
	public void SetTotalNumberOfPages(int totalNumberOfPages) {
		for (ABMButton button: buttons) {
			button.CleanUp();
		}
		buttons.clear();
		buttons = new ArrayList<ABMButton>();
		if (HasFirstLast) {
			first.TotalNumberPagesPagination=totalNumberOfPages;
			first.ParentString = ParentString;
			last.TotalNumberPagesPagination=totalNumberOfPages;
			last.ParentString = ParentString;
		}
		if (HasPreviousNext) {
			previous.TotalNumberPagesPagination=totalNumberOfPages;
			previous.ParentString = ParentString;
			next.TotalNumberPagesPagination=totalNumberOfPages;
			next.ParentString = ParentString;
		}
		int NumberOfPages = totalNumberOfPages;
		if (NumberOfPages > MaxNumberOfPages) {
			NumberOfPages = MaxNumberOfPages;
		}
		for (int i=0;i<NumberOfPages;i++) {
			ABMButton button = new ABMButton();
			button.InitializeFloating(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-number" + i, "", Theme.ThemeName + "number");
			button.mText = "" +(i+1);
			button.IDPagination = ArrayName.toLowerCase() + ID.toLowerCase();
			button.TypePagination = "page";
			button.NumberPagination = i + 1;
			button.TotalNumberPagesPagination=totalNumberOfPages;
			buttons.add(button);
		}
		TotalNumberOfPages = totalNumberOfPages;
	}
	
	public int GetTotalNumberOfPages() {
		return TotalNumberOfPages;
	}
	
	public void SetActivePage(int activePage) {
		this.ActivePage = activePage;
	}
	
	public int GetActivePage() {
		return this.ActivePage;
	}
	
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}	
	
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Paginations.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Paginations.get(themeName.toLowerCase()).Clone();				
			}
		}
		themeName = Theme.ThemeName;
		if (HasFirstLast || HasPreviousNext) {
			Page.CompleteTheme.Button(themeName + "nav").BackColor = Theme.NavigationButtonsBackColor;
			Page.CompleteTheme.Button(themeName + "nav").BackColorIntensity = Theme.NavigationButtonsBackColorIntensity;
			Page.CompleteTheme.Button(themeName + "nav").ForeColor = Theme.NavigationButtonsForeColor;
			Page.CompleteTheme.Button(themeName + "nav").ForeColorIntensity = Theme.NavigationButtonsForeColorIntensity;
		}
		Page.CompleteTheme.Button(themeName + "number").BackColor = Theme.InActivePageBackColor;
		Page.CompleteTheme.Button(themeName + "number").BackColorIntensity = Theme.InActivePageBackColorIntensity;
		Page.CompleteTheme.Button(themeName + "number").ForeColor = Theme.InActivePageForeColor;
		Page.CompleteTheme.Button(themeName + "number").ForeColorIntensity = Theme.InActivePageForeColorIntensity;		
		Page.CompleteTheme.Button(themeName + "numberactive").BackColor = Theme.ActivePageBackColor;
		Page.CompleteTheme.Button(themeName + "numberactive").BackColorIntensity = Theme.ActivePageBackColorIntensity;
		Page.CompleteTheme.Button(themeName + "numberactive").ForeColor = Theme.ActivePageForeColor;
		Page.CompleteTheme.Button(themeName + "numberactive").ForeColorIntensity = Theme.ActivePageForeColorIntensity;
		
	}
	
	public void SetTooltip(String text, String position, int delay) {
		this.ToolTipText = text;
		this.ToolTipPosition = position;
		this.ToolTipDelay = delay;			
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
		super.Refresh();
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		j.SetProp("class", BuildClass());
		
		if (!ToolTipText.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-position", ToolTipPosition);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-delay", ""+ToolTipDelay);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-tooltip", ToolTipText);
		}
		j.SetHtml(BuildBody());		
		
		int numbuttons = buttons.size();
		if (this.HasPreviousNext) {
			numbuttons+=2;
		}
		if (this.HasFirstLast) {
			numbuttons+=2;
		}
		numbuttons*=44; // 37px + showdow 7 px'
		ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "style", "margin: auto;max-width: " + numbuttons + "px;float: none");
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Pagination Refresh: " + ID);}
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
		
		String toolTip="";
		if (!ToolTipText.equals("")) {
			toolTip=" data-position=\"" + ToolTipPosition + "\" data-delay=\"" + ToolTipDelay + "\" data-tooltip=\"" + ToolTipText + "\" "; 
		}
		s.append("<div " + toolTip + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"");
		s.append(BuildClass());	
		int numbuttons = buttons.size();
		if (this.HasPreviousNext) {
			numbuttons+=2;
		}
		if (this.HasFirstLast) {
			numbuttons+=2;
		}
		numbuttons*=44; // 37px + showdow 7 px'
		s.append("\" style=\"margin: auto;max-width: " + numbuttons + "px;float: none\">");
		s.append(BuildBody());
		s.append("</div>\n");
	
		IsBuild=true;
		return s.toString();
	}
	
	@Hide
	protected String BuildClass() {			
		StringBuilder s = new StringBuilder();
		s.append(super.BuildExtraClasses());
		ThemePagination l=Theme;
		
		if (!ToolTipText.equals("")) {
			s.append("tooltipped ");
		}		
		s.append(mVisibility + " ");	
		s.append(l.ZDepth + " ");
		s.append("notselectable ");
		s.append(mIsPrintableClass);
		s.append(mIsOnlyForPrintClass);
		
		return s.toString(); 
	}
	
	@Hide
	protected String BuildBody() {
		StringBuilder s = new StringBuilder();		
		if (ActivePage<buttonCounter) {
			buttonCounter=ActivePage;
		}
		if (ActivePage>=buttonCounter+MaxNumberOfPages) {
			buttonCounter=ActivePage-MaxNumberOfPages+1;
		}
		if (HasFirstLast) {			
			first.mEnabled = true;
			first.IsEnabledDirty=true;
			first.GotLastEnabled=true;
			last.mEnabled = true;
			last.IsEnabledDirty=true;
			last.GotLastEnabled=true;
		}
		if (HasPreviousNext) {
			previous.mEnabled = true;
			previous.IsEnabledDirty=true;
			previous.GotLastEnabled=true;
			next.mEnabled = true;
			next.IsEnabledDirty=true;
			next.GotLastEnabled=true;
		}
		if (ActivePage==1) {
			if (HasFirstLast) {
				first.mEnabled = false;
				first.IsEnabledDirty=true;
				first.GotLastEnabled=true;
			}
			if (HasPreviousNext) {
				previous.mEnabled = false;
				previous.IsEnabledDirty=true;
				previous.GotLastEnabled=true;
			}
		}
		if (ActivePage==TotalNumberOfPages) {
			if (HasFirstLast) {
				last.mEnabled = false;
				last.IsEnabledDirty=true;
				last.GotLastEnabled=true;
			}
			if (HasPreviousNext) {
				next.mEnabled = false;
				next.IsEnabledDirty=true;
				next.GotLastEnabled=true;
			}
		}
		if (HasFirstLast) {			
			s.append(first.Build());
		}
		if (HasPreviousNext) {
			s.append(previous.Build());
		}
		int buttonCounterNow=buttonCounter;
		for (ABMButton button: buttons) {
			button.mText = "" + buttonCounterNow;
			button.NumberPagination = buttonCounterNow; 
			if (buttonCounterNow==ActivePage) {
				button.UseTheme(Theme.ThemeName + "numberactive");
				button.ActivePagination = "selected";
			} else {
				button.UseTheme(Theme.ThemeName + "number");
				button.ActivePagination = "";
			}
			s.append(button.Build());
			buttonCounterNow++;
		}		
		if (HasPreviousNext) {
			s.append(next.Build());
		}
		if (HasFirstLast) {
			s.append(last.Build());
		}
		return s.toString();
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMPagination c = new ABMPagination();
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
		return c;
	}

}
