package com.ab.abmaterial;

import java.io.IOException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;

@ShortName("ABMVideo")
@Events(values={"YouTubeStateChanged(State As Int)"})
public class ABMVideo extends ABMComponent {
	
	private static final long serialVersionUID = 2569589270906030430L;
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	protected String videoType="";
	protected int Width=0;
	protected int Height=0;
	protected boolean HasFrameBorder=false;
	protected boolean AllowFullScreen=false;
	protected boolean HasControls=true;
	protected String HTML5VideoType="video/mp4";	
	protected boolean YouTubeLoop=false;
	protected boolean YouTubeAutoPlay=false;
	
	protected String VimeoPlayerId="";
	protected boolean YouTubeShowInfo=false;
	protected boolean YouTubeShowRelatedVideos=false;
	protected boolean YouTubePlaysInline=false;
	protected int YouTubeStart=0;
	protected int YouTubeEnd=0;
	protected String Source="";
	protected int Volume=100;
	protected String YouTubeOrigin="";
	protected boolean VideoIDLoaded=false;
	
	/**
	 * Initially load the embed http link, later use LoadVideoYouTube() to load other videos with their videoId
	 */
	public void InitializeYouTube(ABMPage page, String id, String source, String origin, boolean hasControls, boolean hasFrameBorder, boolean allowFullScreen, int volume) {
		this.ID = id;			
		this.Type = ABMaterial.UITYPE_VIDEO;
		this.Source = source;
		this.videoType="youtube";
		this.AllowFullScreen = allowFullScreen;
		this.HasControls = hasControls;		
		this.Page=page;
		this.HasFrameBorder=hasFrameBorder;	
		this.Volume = volume;		
		this.YouTubeOrigin=origin;
		IsInitialized=true;
	}	
	
	public void InitializeHTML5(ABMPage page, String id, String source, boolean hasControls, String videoType) {
		this.ID = id;			
		this.Type = ABMaterial.UITYPE_VIDEO;
		this.Source = source;
		this.videoType="HTML5";
		this.HTML5VideoType = videoType;
		this.Page=page;
		this.HasControls=hasControls;
		IsInitialized=true;
	}
	
	public void InitializeVimeo(ABMPage page, String id, String source, boolean hasFrameBorder, boolean allowFullScreen) {
		this.ID = id;			
		this.Type = ABMaterial.UITYPE_VIDEO;
		this.Source = source;
		this.videoType="vimeo";
		this.AllowFullScreen = allowFullScreen;			
		this.Page=page;
		this.HasFrameBorder=hasFrameBorder;	
		IsInitialized=true;
	}	
	
	@Override
	protected void ResetTheme() {
		// not used		
	}
	
	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase() + "-parent";
	}
	
	public void SetFixedSize(int widthPx, int heightPx) {
		this.Width = widthPx;
		this.Height = heightPx;
	}
	
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}	
	
	public void SetTooltip(String text, String position, int delay) {
		this.ToolTipText = text;
		this.ToolTipPosition = position;
		this.ToolTipDelay = delay;			
	}
	
	/**
	 *  Once the YouTube control is loaded, you can load another video with its videoID
	 */
	public void LoadVideoYouTube(String videoId) {
		Source=videoId;
		VideoIDLoaded=true;
		ABMaterial.CueYouTube(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), videoId);
	}
	
	public void LoadVideoHTML5(String source, String videoType) {
		this.Source = source;
		this.HTML5VideoType=videoType;		
	}
	
	public void LoadVideoVimeo(String source) {
		this.Source = source;			
	}
	
	public void Play() {
		switch (videoType) {
		case "HTML5":
			SetVolume(Volume);
			ABMaterial.PlayHTML5(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
			break;
		case "youtube":
			SetVolume(Volume);
			ABMaterial.PlayYouTube(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
			break;
		case "vimeo":	
			BA.Log("Play is not supported in the Vimeo API");
			break;
		}
		
	}
	
	public void Stop() {
		switch (videoType) {
		case "HTML5":			
			ABMaterial.StopHTML5(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
			break;
		case "youtube":
			ABMaterial.StopYouTube(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
			break;
		case "vimeo":	
			BA.Log("Stop is not supported in the Vimeo API");
			break;
		}		
	}
	
	public void Pause() {
		switch (videoType) {
		case "HTML5":			
			ABMaterial.PauseHTML5(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
			break;
		case "youtube":
			ABMaterial.PauseYouTube(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
			break;
		case "vimeo":	
			BA.Log("Pause is not supported in the Vimeo API");
			break;
		}		
	}
	
	public void SetVolume(int volume) {
		Volume = volume;
		switch (videoType) {
		case "HTML5":			
			ABMaterial.SetVolumeHTML5(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), volume);
			break;
		case "youtube":
			ABMaterial.SetVolumeYouTube(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), volume);
			break;
		case "vimeo":	
			BA.Log("SetVolume is not supported in the Vimeo API");
			break;
		}		
	}
	
	public void Mute() {
		switch (videoType) {
		case "HTML5":
			ABMaterial.MuteHTML5(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
			break;
		case "youtube":
			ABMaterial.MuteYouTube(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
			break;
		case "vimeo":	
			BA.Log("Mute is not supported in the Vimeo API");
			break;
		}		
	}
	
	public void UnMute() {
		switch (videoType) {
		case "HTML5":
			ABMaterial.UnMuteHTML5(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
			break;
		case "youtube":
			ABMaterial.UnMuteYouTube(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
			break;
		case "vimeo":	
			BA.Log("UnMute is not supported in the Vimeo API");
			break;
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
	protected String Build() {
		StringBuilder s = new StringBuilder();
		String WidthHeight="";
		if (Width!=0 || Height!=0) {
			WidthHeight = " width=\"" + Width + "\" height=\"" + Height + "\" ";			
		} else {
			if (videoType.equals("HTML5")) {
				WidthHeight = " width=\"100%\" ";
			}
		}
		
		switch (videoType) {
		case "HTML5":
			String controls = "";
			if (HasControls) {
				controls = " controls ";
			}
			s.append("<video id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent" + "\" " + WidthHeight + " class=\"" + BuildClassParent() + "\" " + controls + ">\n");
			s.append("<source id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" src=\"" + Source + "\" type=\"" + HTML5VideoType + "\">\n")	;
			s.append("</video>\n");	
			break;
		case "youtube":
			s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent" + "\" class=\"" + BuildClassParent() + "\">\n");
			String allowFullScreenStr = "";
			if (AllowFullScreen) {
				allowFullScreenStr = " webkitallowfullscreen mozallowfullscreen allowfullscreen ";
			}
			String FrameBorder="";
			if (!HasFrameBorder) {
				FrameBorder=" frameborder=\"" + FrameBorder + "\" ";
			}
			
			String extra="?enablejsapi=1";
			if (YouTubeShowRelatedVideos) {
				extra+="&rel=1";
			} else {
				extra+="&rel=0";
			}
			if (YouTubeShowInfo) {
				extra+="&showinfo=0";
			}
			if (!HasControls) {
				extra+="&controls=0";
			}
			if (YouTubeLoop) {
				extra+="&loop=1";
			}
			if (YouTubeAutoPlay) {
				extra+="&autoplay=1";
			}
			if (YouTubePlaysInline) {
				extra+="&playsinline=1";
			}
			if (YouTubeStart>0) {
				extra+="&start="+YouTubeStart;
			}
			if (YouTubeEnd>0) {
				extra+="&end="+YouTubeEnd;
			}	
			if (!YouTubeOrigin.equals("")) {
				extra+="&origin=" + YouTubeOrigin;
			}
			s.append("<iframe id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" " + WidthHeight + " src=\"" + Source + extra + "\"" + FrameBorder + allowFullScreenStr + "></iframe>\n")	;
			s.append("</div>\n");	
			break;
		case "vimeo":
			s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent" + "\" class=\"" + BuildClassParent() + "\">\n");
			String allowFullScreenStr2 = "";
			if (AllowFullScreen) {
				allowFullScreenStr2 = " webkitallowfullscreen mozallowfullscreen allowfullscreen ";
			}
			String FrameBorder2="";
			if (!HasFrameBorder) {
				FrameBorder2=" frameborder=\"" + FrameBorder2 + "\" ";
			}
			
			String extra2=""; //?api=1&player_id=" + VimeoPlayerId;
			
			s.append("<iframe id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" " + WidthHeight + " src=\"" + Source + extra2 + "\"" + FrameBorder2 + allowFullScreenStr2 + "></iframe>\n")	;
			s.append("</div>\n");	
			break;
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
		// TODO ExtraClasses not working
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
		
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		switch (videoType) {
		case "HTML5":
			j.SetProp("type", HTML5VideoType);
			j.SetProp("src", Source);	
			if (DoFlush) {
				try {
					if (Page.ws.getOpen()) {
						if (Page.ShowDebugFlush) {BA.Log("Video Refresh 1: " + ID);}
						Page.ws.Flush();Page.RunFlushed();
					}
					ABMaterial.StopHTML5(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase()); // reload
				} catch (IOException e) {
					//e.printStackTrace();
				}
			}
			break;
		case "youtube":
			String extra="?enablejsapi=1";
			if (YouTubeShowRelatedVideos) {
				extra+="&rel=1";
			} else {
				extra+="&rel=0";
			}
			if (YouTubeShowInfo) {
				extra+="&showinfo=0";
			}
			if (!HasControls) {
				extra+="&controls=0";
			}
			if (YouTubeLoop) {
				extra+="&loop=1";
			}
			if (YouTubeAutoPlay) {
				extra+="&autoplay=1";
			}
			if (YouTubePlaysInline) {
				extra+="&playsinline=1";
			}
			if (YouTubeStart>0) {
				extra+="&start="+YouTubeStart;
			}
			if (YouTubeEnd>0) {
				extra+="&end="+YouTubeEnd;
			}	
			if (!YouTubeOrigin.equals("")) {
				extra+="&origin=" + YouTubeOrigin;
			}
			j.SetProp("src", Source+extra);	
			break;
		case "vimeo":
			String extra2="";
			j.SetProp("src", Source+extra2);	
			break;			
		}		
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Video Refresh 2: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
	}
	
	@Hide
	protected String BuildClassParent() {			
		StringBuilder s = new StringBuilder();	
		s.append(super.BuildExtraClasses());
		if (videoType.equals("HTML5")) {
			s.append("responsive-video ");
		} else {
			s.append("video-container ");			
		}
		s.append(" " + mVisibility + " ");
		s.append(mIsPrintableClass);
		s.append(mIsOnlyForPrintClass);
		return s.toString(); 
	}		
	
	@Hide
	protected String BuildClass() {			
		StringBuilder s = new StringBuilder();		
			
		return s.toString(); 
	}
	
	@Hide
	protected String BuildBody() {
		return "";
	}
	
	
}
