package com.ab.abmaterial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;

@Author("Alain Bailleul")  
@ShortName("ABMAudioPlayer")
@Events(values={"Done()"})
public class ABMAudioPlayer extends ABMComponent {
	private static final long serialVersionUID = 3661978829659563603L;
	protected ThemeAudioPlayer Theme=new ThemeAudioPlayer();
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	
	protected boolean Narrow=false;
	protected boolean AutoPlay=false;
	protected boolean ShowLyrics=false;
	
	protected transient ABMAudioPlayerSong Song=null;
	protected transient List<ABMAudioPlayerSong> playList = new ArrayList<ABMAudioPlayerSong>();
	protected boolean IsDirty=false;
	
	/**
	 * On mobile browsers, autoPlay is ignored!	
	 * If narrow = true, loading a playlist is not supported
	 */
	public void Initialize(ABMPage page, String id, boolean narrow, boolean autoPlay, boolean showLyrics, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_AUDIOPLAYER;
		this.Narrow=narrow;
		this.AutoPlay=autoPlay;
		this.ShowLyrics=showLyrics;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.AudioPlayers.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.AudioPlayers.get(themeName.toLowerCase()).Clone();				
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
		return ArrayName.toLowerCase() + ID.toLowerCase();
	}
	
	public void LoadSingleSong(ABMAudioPlayerSong song) {
		this.playList.clear();
		this.Song = song;
		IsDirty=true;
	}
	
	public void LoadPlaylist(anywheresoftware.b4a.objects.collections.List playList) {
		this.playList.clear();
		this.Song=null;
		for (int i=0;i<playList.getSize();i++) {
			this.playList.add((ABMAudioPlayerSong)playList.Get(i));
		}
		IsDirty=true;
	}	
		
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}	
		
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.AudioPlayers.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.AudioPlayers.get(themeName.toLowerCase()).Clone();				
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
	
	public void SetTooltip(String text, String position, int delay) {
		this.ToolTipText = text;
		this.ToolTipPosition = position;
		this.ToolTipDelay = delay;			
	}	
	
	protected String BuildJavaScript() {
		StringBuilder s = new StringBuilder();	
		if (Song==null) {
			s.append("var " + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + " = new APlayer({");
			s.append("element: document.getElementById('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "'),");
			s.append("narrow: " + Narrow + ",");
			s.append("autoplay: " + AutoPlay + ",");
			s.append("showlrc: " + ShowLyrics + ",");
			s.append("theme: '" + ABMaterial.GetColorStrMap(Theme.ForeColor, ABMaterial.INTENSITY_NORMAL) + "',");
			s.append("themename: '" + Theme.ThemeName + "',");
			s.append("music: [");
			for (int j=0;j<playList.size();j++) {
				ABMAudioPlayerSong sg = playList.get(j);
			
				s.append("{");
				s.append("title: '" + sg.Title + "',");
				s.append("author: '" + sg.Author + "',");
				s.append("url: '" + sg.AudioUrl + "',");
				s.append("pic: '" + sg.ImageUrl + "'");
				s.append("}");
				if (j<playList.size()) {
					s.append(",");
				}
			}
			s.append("]");
			s.append("});");
			s.append("" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + ".init();");
		} else {
			s.append("var " + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + " = new APlayer({");
			s.append("element: document.getElementById('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "'),");
			s.append("narrow: " + Narrow + ",");
			s.append("autoplay: " + AutoPlay + ",");
			s.append("showlrc: " + ShowLyrics + ",");
			s.append("theme: '" + ABMaterial.GetColorStrMap(Theme.ForeColor, ABMaterial.INTENSITY_NORMAL) + "',");
			s.append("themename: '" + Theme.ThemeName + "',");
			s.append("music: {");
			s.append("title: '" + Song.Title + "',");
			s.append("author: '" + Song.Author + "',");
			s.append("url: '" + Song.AudioUrl + "',");
			s.append("pic: '" + Song.ImageUrl + "'");
			s.append("}");
			s.append("});");
			s.append("" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + ".init();");
		}
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
		j.SetProp("class", BuildClass());	
		
		if (!ToolTipText.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-position", ToolTipPosition);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-delay", "" + ToolTipDelay);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-tooltip", ToolTipText);
		}	
		ABMaterial.ReplaceMyInnerHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), BuildBody());
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("AudioPlayer Refresh: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		if (IsDirty) {
			Page.ws.Eval(BuildJavaScript(), null);
		}
		IsDirty=false;
		
		
		
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
		s.append(" aplayer ");
		if (!ToolTipText.equals("")) {
			s.append("tooltipped ");
		}		
		s.append(mVisibility + " ");	
		s.append(Theme.ZDepth + " ");	
		s.append(mIsPrintableClass);
		s.append(mIsOnlyForPrintClass);
		return s.toString(); 
	}
	
	@Hide
	protected String BuildBody() {
		StringBuilder s = new StringBuilder();
		if (Song==null) {
			for (int j=0;j<playList.size();j++) {
				ABMAudioPlayerSong sg = playList.get(j);
				if (sg.Lyrics.size()>0) {
					s.append("<pre class=\"aplayer-lrc-content\">");
					for (int i=0;i<sg.Lyrics.size();i++) {
						s.append(sg.Lyrics.get(i) + "\n");
					}
					s.append("</pre>");
				}
			}
		} else {
			if (Song.Lyrics.size()>0) {
				s.append("<pre class=\"aplayer-lrc-content\">");
				for (int i=0;i<Song.Lyrics.size();i++) {
					s.append(Song.Lyrics.get(i) + "\n");
				}
				s.append("</pre>");
			}
		}
		return s.toString();
	}
	
	@Hide
	@Override
	protected void Prepare() {	
		this.IsBuild = true;
		this.IsDirty=true;
		Refresh();
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMAudioPlayer c = new ABMAudioPlayer();
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
