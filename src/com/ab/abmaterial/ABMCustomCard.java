package com.ab.abmaterial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;

@Author("Alain Bailleul")
@ShortName("ABMCustomCard")
public class ABMCustomCard extends ABMComponent {
	private static final long serialVersionUID = -1899561664539136717L;
	protected ThemeCustomCard Theme=new ThemeCustomCard();
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	protected transient ABMComponent mFrontTopComponent=null;
	protected transient ABMComponent mFrontTitleComponent=null;
	protected transient ABMComponent mBackComponent=null;
	protected transient List<ABMComponent> Actions = new ArrayList<ABMComponent>();
	
	public void Initialize(ABMPage page, String id, String themeName) {
		this.ID = id;
		this.Type = ABMaterial.UITYPE_CUSTOMCARD;
		this.Page = page;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.CustomCards.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.CustomCards.get(themeName.toLowerCase()).Clone();				
			}
		}	
		IsInitialized=true;
	}
	
	public void SetFrontTopComponent(ABMComponent component) {		
		mFrontTopComponent = component;		
	}
	
	public ABMComponent GetFrontTopComponent() {
		return mFrontTopComponent;
	}
	
	public void SetFrontTitleComponent(ABMComponent component) {
		mFrontTitleComponent = component;
	}
	
	public ABMComponent GetFrontTitleComponent() {
		return mFrontTitleComponent;
	}
	
	public void SetBackComponent(ABMComponent component) {
		mBackComponent = component;
	}
	
	public ABMComponent GetBackComponent() {
		return mBackComponent;
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
		
		mFrontTopComponent.AddArrayName(mFrontTopComponent.ID.toLowerCase());
		mFrontTopComponent.ID = ID.toLowerCase();
		mFrontTitleComponent.AddArrayName(mFrontTitleComponent.ID.toLowerCase());
		mFrontTitleComponent.ID = ID.toLowerCase();
		mBackComponent.AddArrayName(mBackComponent.ID.toLowerCase());
		mBackComponent.ID = ID.toLowerCase();
		for (int i=0; i < Actions.size(); i++) {
			Actions.get(i).AddArrayName(Actions.get(i).ID.toLowerCase());
			Actions.get(i).ID = ID.toLowerCase();
		}
				
	}
	
	public void AddAction(ABMComponent action) {
		Actions.add(action);
	}
	
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.CustomCards.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.CustomCards.get(themeName.toLowerCase()).Clone();				
			}
		}
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
		if (mFrontTopComponent!=null) {
			mFrontTopComponent.FirstRun();
		}
		if (mFrontTitleComponent!=null) {
			mFrontTitleComponent.FirstRun();
		}
		if (mBackComponent!=null) {
			mBackComponent.FirstRun();
		}
		for(int i=0;i<Actions.size();i++) {
			Actions.get(i).FirstRun();
		}
		
		super.FirstRun();
	}
		
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		j.SetProp("class", BuildClass());
		if (!ToolTipText.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-position", ToolTipPosition);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-delay", ""+ToolTipDelay);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-tooltip", ToolTipText);
		}
		if (mFrontTopComponent!=null) {
			mFrontTopComponent.RefreshInternal(false);
		}
		if (mFrontTitleComponent!=null) {
			mFrontTitleComponent.RefreshInternal(false);
		}
		if (mBackComponent!=null) {
			mBackComponent.RefreshInternal(false);
		}
		for(int i=0;i<Actions.size();i++) {
			Actions.get(i).RefreshInternal(false);
		}
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("CustomCard Refresh: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {
				//e.printStackTrace();
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
		String toolTip="";
		if (!ToolTipText.equals("")) {
			toolTip=" data-position=\"" + ToolTipPosition + "\" data-delay=\"" + ToolTipDelay + "\" data-tooltip=\"" + ToolTipText + "\" "; 
		}
		s.append("<div " + toolTip + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"");
		s.append(BuildClass());
		s.append("\">");
		s.append(BuildBody());					
		s.append("</div>\n");	
		IsBuild=true;
		return s.toString();
	}
	
	@Hide
	protected String BuildClass() {			
		StringBuilder s = new StringBuilder();	
		s.append(super.BuildExtraClasses());
		ThemeCustomCard card = Theme; 
		String toolTip2="";
		String Sticky="";
		
		if (!ToolTipText.equals("")) {
			toolTip2 = " tooltipped ";
		}
		String rtl = "";
		if (this.getRightToLeft()) {
			rtl = " abmrtl ";
		}
		
		s.append("card " + Sticky + " " + toolTip2 + ABMaterial.GetColorStr(card.BackColor, card.BackColorIntensity, "") + " " + mVisibility + " " + card.ZDepth + rtl + " ");
		s.append(mIsPrintableClass);
		s.append(mIsOnlyForPrintClass);
		return s.toString(); 
	}
	
	@Hide
	protected String BuildBody() {
		StringBuilder s = new StringBuilder();
		
		ThemeCustomCard card = Theme; 
		
		s.append("<div class=\"card-image " + ABMaterial.GetWavesEffect(card.WavesEffect, card.WavesCircle) + "\" style=\"width: 100%\">\n");
		if (mFrontTopComponent!=null) {
			s.append(mFrontTopComponent.Build());
		}
		s.append("</div>\n");
		s.append("<div class=\"card-content \">\n");
		String reveal="";
		if (mBackComponent!=null) {
			if (this.getRightToLeft()) {
				reveal = "<i class=\"material-icons left " + ABMaterial.GetColorStr(card.RevealColor, card.RevealColorIntensity, "text") + "\" style=\"margin-top: -40px;cursor: pointer\">more_vert</i>";
			} else {
				reveal = "<i class=\"material-icons right " + ABMaterial.GetColorStr(card.RevealColor, card.RevealColorIntensity, "text") + "\" style=\"margin-top: -40px;cursor: pointer\">more_vert</i>";
			}
		}
		if (mFrontTitleComponent!=null) {
			s.append("<div class=\"card-title activator\" style=\"cursor: default\">" + mFrontTitleComponent.Build() + reveal + "</div>\n");
		} else {
			s.append("<span class=\"card-title activator\" style=\"cursor: default\">&nbsp;" + reveal + "</span>\n");
		}
		if (Actions.size()>0) {
			s.append("<div class=\"card-action\">\n");
			for(int i=0;i<Actions.size();i++) {
				s.append(Actions.get(i).Build() + "\n");
		    }
			s.append("</div>\n");
		}
		s.append("</div>\n");
		s.append("<div class=\"card-reveal \">\n");

		if (mFrontTitleComponent!=null) {
			if (this.getRightToLeft()) {
				s.append("<div class=\"card-title\" style=\"cursor: default\">" + mFrontTitleComponent.Build() + "<i class=\"material-icons left " + ABMaterial.GetColorStr(card.RevealColor, card.RevealColorIntensity, "text") + "\" style=\"margin-top: -40px;cursor: pointer\">close</i></div>\n");
			} else {
				s.append("<div class=\"card-title\" style=\"cursor: default\">" + mFrontTitleComponent.Build() + "<i class=\"material-icons right " + ABMaterial.GetColorStr(card.RevealColor, card.RevealColorIntensity, "text") + "\" style=\"margin-top: -40px;cursor: pointer\">close</i></div>\n");
			}
		} else {
			if (this.getRightToLeft()) {
				s.append("<span class=\"card-title\" style=\"cursor: default\">&nbsp;<i class=\"material-icons left " + ABMaterial.GetColorStr(card.RevealColor, card.RevealColorIntensity, "text") + "\" style=\"margin-top: -40px;cursor: pointer\">close</i></span>\n");
			} else {
				s.append("<span class=\"card-title\" style=\"cursor: default\">&nbsp;<i class=\"material-icons right " + ABMaterial.GetColorStr(card.RevealColor, card.RevealColorIntensity, "text") + "\" style=\"margin-top: -40px;cursor: pointer\">close</i></span>\n");
			}
		}
		
		if (mBackComponent!=null) {
			s.append("<div>" + mBackComponent.Build() + "</div>\n");
		} else {
			s.append("<p>&nbsp;</p>\n");
		}
		s.append("</div>\n");
				
		return s.toString();
	}
	
	protected String BuildCustomBody() {
		StringBuilder s = new StringBuilder();
		
		return s.toString();
	}
		
	@Override
	protected ABMComponent Clone() {
		ABMCustomCard c = new ABMCustomCard();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;
		c.Theme = Theme.Clone();
		c.Type = Type;
		c.mVisibility = mVisibility;			
		if (mFrontTopComponent!=null) {
			c.mFrontTopComponent=mFrontTopComponent.Clone();
		}
		if (mFrontTitleComponent!=null) {
			c.mFrontTitleComponent=mFrontTopComponent.Clone();
		}
		if (mBackComponent!=null) {
			c.mBackComponent=mBackComponent.Clone();
		}
		for(int i=0;i<Actions.size();i++) {
			Actions.add(Actions.get(i));
		}
		//c.Size = Size;
		
		c.ToolTipDelay = ToolTipDelay;
		c.ToolTipPosition = ToolTipPosition;
		c.ToolTipText = ToolTipText;
		return c;
		
	}
	
}

