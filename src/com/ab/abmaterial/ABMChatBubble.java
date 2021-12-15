package com.ab.abmaterial;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.ShortName;

@ShortName("ABMChatBubble")
public class ABMChatBubble {
	protected String mText;
	public String IsFrom="";
	public String ExtraInfo="";
	public String ForMeThemeName="default";
	public String ForThemThemeName="default";
	protected String mRawHTML="";
	public String ExtraStyle="";
	protected boolean IsLoaded=false;
	protected boolean Replacing=false;
	public String MessageId="";
	protected String ID="";
	public Object Tag=null;
	protected boolean mHasText=true;
	protected boolean mHasRawHTML=false;
	
	public void Initialize(String isFrom, String text, String extraInfo, String forMeThemeName, String forThemThemeName) {
		BA.LogError("ABMChatBubble.Initialize may be depreciated in the future: use InitializeWithMessageid instead!");
		this.mText = text;
		this.mRawHTML="";
		this.mHasText=true;
		this.mHasRawHTML=false;
		this.IsFrom = isFrom;
		this.ExtraInfo = extraInfo;
		this.ForMeThemeName = forMeThemeName.toLowerCase();
		this.ForThemThemeName = forThemThemeName.toLowerCase();
	}
	
	public void InitializeWithMessageid(String isFrom, String forMeThemeName, String forThemThemeName, String messageId) {
		//this.Text = text;
		this.IsFrom = isFrom;
		this.mHasText=false;
		this.mHasRawHTML=false;
		//this.ExtraInfo = extraInfo;
		this.ForMeThemeName = forMeThemeName.toLowerCase();
		this.ForThemThemeName = forThemThemeName.toLowerCase();
		this.MessageId = messageId;
	}
	
	protected ABMChatBubble Clone() {
		ABMChatBubble bubble = new ABMChatBubble();
		bubble.mText = mText;
		bubble.IsFrom = IsFrom;
		bubble.ExtraInfo = ExtraInfo;
		bubble.ForMeThemeName = ForMeThemeName;
		bubble.ForThemThemeName = ForThemThemeName;
		bubble.mRawHTML=mRawHTML;
		bubble.ExtraStyle=ExtraStyle;
		bubble.MessageId=MessageId;
		bubble.Replacing=Replacing;
		bubble.ID=ID;
		bubble.Tag=Tag;
		bubble.mHasText=mHasText;
		bubble.mHasRawHTML=mHasRawHTML;
		return bubble;
	}
	
	public boolean HasText() {
		return mHasText;
	}
	
	public boolean HasRawHTML() {
		return mHasRawHTML;
	}
	
	public void setText(String text) {
		mText=text;
		mRawHTML="";
		this.mHasText=true;
		this.mHasRawHTML=false;
	}
	
	public void setRawHTML(String html) {
		mText="";
		mRawHTML=html;
		this.mHasText=false;
		this.mHasRawHTML=true;
	}
	
	public String getText() {
		return mText;
	}
	
	public String getRawHTML() {
		return mRawHTML;
	}
}
