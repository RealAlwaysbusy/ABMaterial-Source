package com.ab.abmaterial;

import java.util.ArrayList;
import java.util.List;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")  
@ShortName("ABMAudioPlayerSong")
public class ABMAudioPlayerSong implements java.io.Serializable {
	private static final long serialVersionUID = -9069324760493022121L;
	public String Title="";
	public String Author="";
	public String AudioUrl="";
	public String ImageUrl="";
	protected List<String> Lyrics = new ArrayList<String>();
	public boolean IsInitialized=false;
	
	public void Initialize(String audioUrl) {
		this.AudioUrl = audioUrl;
		IsInitialized=true;
	}
	
	public void AddLyric(String value) {
		Lyrics.add(value);
	}
	
	public void ClearLyrics() {
		Lyrics = new ArrayList<String>();
	}
	
	public void AddLyrics(anywheresoftware.b4a.objects.collections.List lyrics) {
		for (int i=0;i<lyrics.getSize();i++) {
			Lyrics.add((String) lyrics.Get(i));
		}
	}
	
	
}
