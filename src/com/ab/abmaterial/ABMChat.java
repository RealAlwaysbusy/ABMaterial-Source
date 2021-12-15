package com.ab.abmaterial;

import java.io.IOException;

import com.ab.abmaterial.ThemeChat.ThemeChatBubble;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.objects.collections.List;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;

@Author("Alain Bailleul")  
@ShortName("ABMChat")
@Events(values={"ScrolledToTop(Target As String)"})
public class ABMChat extends ABMComponent {

	private static final long serialVersionUID = -3404864098427107143L;
	protected ThemeChat Theme=new ThemeChat();
	protected int MaxWidthPx=450;
	protected int MaxBubbleWidthPx=255;
	protected int HeightPx=400;
	protected List ChatBubbles = new List();
	protected ABMChatBubble typingBubble=null;
	protected boolean TypingShown=false; 
	protected boolean CancelTypingShown=false;
	protected String MyFrom="";
		
	public boolean SupportEmoji=true;
	
	protected long msgCounter=0;
	
	/*
	protected String heart="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><path d=\"m61.1 18.2c-6.4-17-27.2-9.4-29.1-.9-2.6-9-22.9-15.7-29.1.9-6.9 18.5 26.7 35.1 29.1 37.8 2.4-2.2 36-19.6 29.1-37.8\" fill=\"#ff5a79\"/></svg>";
	protected String brokenheart="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><path d=\"m61.1 19.1c-4.5-12.1-16.2-11.6-23.2-7.4l-5.3 11.3 10-.9-10.3 13.2 8.4-2.2-5.3 20.9-1.6-14.1-11.9 2.5 9.1-14.4-7.8-1.1 5.4-12.4c-6.4-6.4-23-7.6-26.2 7.8-3.9 18.3 27.3 29.6 32.9 32.7v.1c0 0 0 0 .1 0 0 0 .1 0 .1 0v-.1c8.3-5.5 31.3-20.5 25.6-35.9\" fill=\"#ff5a79\"/></svg>";
	protected String joy="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><path d=\"m49.7 34.4c-.4-.5-1.1-.4-1.9-.4-15.8 0-15.8 0-31.6 0-.8 0-1.5-.1-1.9.4-3.9 5 .7 19.6 17.7 19.6 17 0 21.6-14.6 17.7-19.6\" fill=\"#664e27\"/><path d=\"m33.8 41.7c-.6 0-1.5.5-1.1 2 .2.7 1.2 1.6 1.2 2.8 0 2.4-3.8 2.4-3.8 0 0-1.2 1-2 1.2-2.8.3-1.4-.6-2-1.1-2-1.6 0-4.1 1.7-4.1 4.6 0 3.2 2.7 5.8 6 5.8 3.3 0 6-2.6 6-5.8-.1-2.8-2.7-4.5-4.3-4.6\" fill=\"#4c3526\"/><path d=\"m24.3 50.7c2.2 1 4.8 1.5 7.7 1.5 2.9 0 5.5-.6 7.7-1.5-2.1-1.1-4.7-1.7-7.7-1.7s-5.6.6-7.7 1.7\" fill=\"#ff717f\"/><path d=\"m47 36c-15 0-15 0-29.9 0-2.1 0-2.1 4-.1 4 10.4 0 19.6 0 30 0 2 0 2-4 0-4\" fill=\"#fff\"/><g fill=\"#65b1ef\"><path d=\"m59.4 36.9c7.3 7.7-2.6 18.1-9.9 10.4-5.3-5.6-5.6-16.3-5.6-16.3s10.2.3 15.5 5.9\"/><path d=\"m14.5 47.3c-7.3 7.7-17.2-2.7-9.9-10.4 5.3-5.6 15.5-5.9 15.5-5.9s-.3 10.7-5.6 16.3\"/></g><g fill=\"#664e27\"><path d=\"m28.5 28.7c-1.9-5.1-4.7-7.7-7.5-7.7s-5.6 2.6-7.5 7.7c-.2.5.8 1.4 1.3.9 1.8-1.9 4-2.7 6.2-2.7 2.2 0 4.4.8 6.2 2.7.6.5 1.5-.4 1.3-.9\"/><path d=\"m50.4 28.7c-1.9-5.1-4.7-7.7-7.5-7.7s-5.6 2.6-7.5 7.7c-.2.5.8 1.4 1.3.9 1.8-1.9 4-2.7 6.2-2.7s4.4.8 6.2 2.7c.5.5 1.5-.4 1.3-.9\"/></g></svg>";
	protected String smiley="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><g fill=\"#664e27\"><circle cx=\"20.5\" cy=\"26.6\" r=\"5\"/><circle cx=\"43.5\" cy=\"26.6\" r=\"5\"/><path d=\"m44.6 40.3c-8.1 5.7-17.1 5.6-25.2 0-1-.7-1.8.5-1.2 1.6 2.5 4 7.4 7.7 13.8 7.7s11.3-3.6 13.8-7.7c.6-1.1-.2-2.3-1.2-1.6\"/></g></svg>";
	protected String slightsmile="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><g fill=\"#664e27\"><circle cx=\"20.5\" cy=\"24.5\" r=\"5\"/><circle cx=\"43.5\" cy=\"24.5\" r=\"5\"/><path d=\"m49 38c0-.8-.5-1.8-1.8-2.1-3.5-.7-8.6-1.3-15.2-1.3-6.6 0-11.7.7-15.2 1.3-1.3.3-1.8 1.3-1.8 2.1 0 7.3 5.6 14.6 17 14.6 11.4 0 17-7.3 17-14.6\"/></g><path d=\"m44.7 38.3c-2.2-.4-6.8-1-12.7-1-5.9 0-10.5.6-12.7 1-1.3.2-1.4.7-1.3 1.5.1.4.1 1 .3 1.6.1.6.3.9 1.3.8 1.9-.2 23-.2 24.9 0 1 .1 1.1-.2 1.3-.8.1-.6.2-1.1.3-1.6 0-.8-.1-1.3-1.4-1.5\" fill=\"#fff\"/></svg>";
	protected String sweatsmile="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><g fill=\"#664e27\"><path d=\"m25.5 26.9c-1.9-5.1-4.7-7.7-7.5-7.7s-5.6 2.6-7.5 7.7c-.2.5.8 1.4 1.3.9 1.8-1.9 4-2.7 6.2-2.7s4.4.8 6.2 2.7c.6.5 1.5-.4 1.3-.9\"/><path d=\"m47.4 26.9c-1.9-5.1-4.7-7.7-7.5-7.7s-5.6 2.6-7.5 7.7c-.2.5.8 1.4 1.3.9 1.8-1.9 4-2.7 6.2-2.7s4.4.8 6.2 2.7c.5.5 1.5-.4 1.3-.9\"/><path d=\"m46 38c0-.8-.5-1.8-1.8-2.1-3.5-.7-8.6-1.3-15.2-1.3-6.6 0-11.7.7-15.2 1.3-1.3.3-1.8 1.3-1.8 2.1 0 7.3 5.6 14.6 17 14.6 11.4 0 17-7.3 17-14.6\"/></g><path d=\"m41.7 38.3c-2.2-.4-6.8-1-12.7-1-5.9 0-10.5.6-12.7 1-1.3.2-1.4.7-1.3 1.5.1.4.1 1 .3 1.6.1.6.3.9 1.3.8 1.9-.2 23-.2 24.9 0 1 .1 1.1-.2 1.3-.8.1-.6.2-1.1.3-1.6 0-.8-.1-1.3-1.4-1.5\" fill=\"#fff\"/><path d=\"M60,30.2c0,7.2-9.7,7.2-9.7,0c0-5.2,4.9-10.4,4.9-10.4S60,25,60,30.2z\" fill=\"#65b1ef\"/></svg>";
	protected String laughing="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><g fill=\"#664e27\"><path d=\"m51.7 19.4c.6.3.3 1-.2 1.1-2.7.4-5.5.9-8.3 2.4 4 .7 7.2 2.7 9 4.8.4.5-.1 1.1-.5 1-4.8-1.7-9.7-2.7-15.8-2-.5 0-.9-.2-.8-.7 1.6-7.3 10.9-10 16.6-6.6\"/><path d=\"m12.3 19.4c-.6.3-.3 1 .2 1.1 2.7.4 5.5.9 8.3 2.4-4 .7-7.2 2.7-9 4.8-.4.5.1 1.1.5 1 4.8-1.7 9.7-2.7 15.8-2 .5 0 .9-.2.8-.7-1.6-7.3-10.9-10-16.6-6.6\"/><path d=\"m49.7 34.4c-.4-.5-1.1-.4-1.9-.4-15.8 0-15.8 0-31.6 0-.8 0-1.5-.1-1.9.4-3.9 5 .7 19.6 17.7 19.6 17 0 21.6-14.6 17.7-19.6\"/></g><path d=\"m33.8 41.7c-.6 0-1.5.5-1.1 2 .2.7 1.2 1.6 1.2 2.8 0 2.4-3.8 2.4-3.8 0 0-1.2 1-2 1.2-2.8.3-1.4-.6-2-1.1-2-1.6 0-4.1 1.7-4.1 4.6 0 3.2 2.7 5.8 6 5.8s6-2.6 6-5.8c-.1-2.8-2.7-4.5-4.3-4.6\" fill=\"#4c3526\"/><path d=\"m24.3 50.7c2.2 1 4.8 1.5 7.7 1.5s5.5-.6 7.7-1.5c-2.1-1.1-4.7-1.7-7.7-1.7s-5.6.6-7.7 1.7\" fill=\"#ff717f\"/><path d=\"m47 36c-15 0-15 0-29.9 0-2.1 0-2.1 4-.1 4 10.4 0 19.6 0 30 0 2 0 2-4 0-4\" fill=\"#fff\"/></svg>";
	protected String wink="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><circle cx=\"22.3\" cy=\"31.6\" r=\"5\" fill=\"#664e27\"/><g fill=\"#917524\"><path d=\"m51.2 27.5c-3.2-2.7-7.5-3.9-11.7-3.1-.6.1-1.1-2-.4-2.2 4.8-.9 9.8.5 13.5 3.6.6.5-1 2.1-1.4 1.7\"/><path d=\"m24.5 18.8c-4.2-.7-8.5.4-11.7 3.1-.4.4-2-1.2-1.4-1.7 3.7-3.2 8.7-4.5 13.5-3.6.7.2.2 2.3-.4 2.2\"/></g><g fill=\"#664e27\"><path d=\"m50.2 34.3c-1.7-3.5-4.4-5.3-7-5.3s-5.2 1.8-7 5.3c-.2.4.7 1 1.2.6 1.7-1.3 3.7-1.8 5.8-1.8s4.1.5 5.8 1.8c.4.3 1.3-.3 1.2-.6\"/><path d=\"m44.1 42.2c-6.9 3.6-16.4 2.9-19.1 2.9-.7 0-1.2.3-1 .9 2 7 17 7 21.1-2.7.5-1.1-.3-1.4-1-1.1\"/></g></svg>";
	protected String sweat="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><path d=\"M2,32c0,16.6,13.4,30,30,30s30-13.4,30-30S48.6,2,32,2S2,15.4,2,32z\" fill=\"#ffdd67\"/><path d=\"M48.5,16.3c0,9.9,13.5,9.9,13.5,0C62,9.1,55.3,2,55.3,2S48.5,9.1,48.5,16.3z\" fill=\"#65b1ef\"/><g fill=\"#664e27\"><circle cx=\"43.5\" cy=\"36\" r=\"5\"/><circle cx=\"20.5\" cy=\"36\" r=\"5\"/></g><g fill=\"#917524\"><path d=\"m25.6 21.9c-3.2 2.7-7.5 3.9-11.7 3.1-.6-.1-1.1 2-.4 2.2 4.8.9 9.8-.5 13.5-3.6.5-.5-1-2.1-1.4-1.7\"/><path d=\"m50.1 24.9c-4.2.7-8.5-.4-11.7-3.1-.4-.4-2 1.2-1.4 1.7 3.7 3.2 8.7 4.5 13.5 3.6.7-.2.2-2.3-.4-2.2\"/></g><path d=\"m40 52h-16c-1.5 0-1.5-4 0-4h16c1.5 0 1.5 4 0 4\" fill=\"#664e27\"/></svg>";
	protected String kissingheart="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><path fill=\"#f46767\" d=\"m50.9 58h-.1z\"/><ellipse cx=\"22\" cy=\"27\" rx=\"5\" ry=\"6\" fill=\"#664e27\"/><path d=\"m61.4 42.6c-.9-1.8-2.7-3-5-2.8-2.3.2-4.1 1.4-5.6 3.5-1.5-2-3.3-3.3-5.6-3.4-2.3-.1-4.1 1.1-5 2.9-.9 1.8-.9 4.2.9 7 1.8 2.7 9.5 8.2 9.7 8.4.2-.2 7.9-5.8 9.7-8.4 1.8-3 1.8-5.4.9-7.2\" fill=\"#f46767\"/><g fill=\"#664e27\"><path d=\"m51.9 30.5c-1.9-4.1-4.7-6.1-7.5-6.1s-5.6 2-7.5 6.1c-.2.4.8 1.2 1.3.8 1.8-1.5 4-2.1 6.2-2.1s4.4.6 6.2 2.1c.5.4 1.4-.3 1.3-.8\"/><path d=\"m39.5 50.4c1.6-1.6-1.8-3-1.8-5.9s3.4-4.2 1.8-5.9c-1.9-2-6-.5-8.6-3.1 0 2.2 1.8 4.5 5.2 4.5 0 0-2.3.9-2.3 4.5s2.3 4.5 2.3 4.5c-3.4 0-5.2 2.3-5.2 4.5 2.6-2.7 6.7-1.2 8.6-3.1\"/></g></svg>";
	protected String stuckouttonguewink="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><path d=\"m31.2 24.6c0 5.5-4.5 10-10 10-5.5 0-10-4.5-10-10 0-5.5 4.5-10 10-10 5.6 0 10 4.5 10 10\" fill=\"#fff\"/><g fill=\"#664e27\"><circle cx=\"21.2\" cy=\"24.6\" r=\"4.5\"/><path d=\"m51 29.1c-1.9-4.1-4.7-6.1-7.5-6.1s-5.6 2-7.5 6.1c-.2.4.8 1.2 1.3.8 1.8-1.5 4-2.1 6.2-2.1s4.4.6 6.2 2.1c.5.4 1.5-.4 1.3-.8\"/><path d=\"m47.9 38c-3.3 0-9.7 0-15.9 0s-12.6 0-15.9 0c-.7 0-1.1.5-1.1 1 0 7.3 6 15 17 15s17-7.7 17-15c0-.5-.4-1-1.1-1\"/></g><path d=\"m41.2 44c-2.3 0-9.2 0-9.2 0s-6.9 0-9.2 0c-.7 0-.8.3-.8.8 0 .9 0 2.4 0 4 0 8.8 4.5 13.2 10 13.2 5.5 0 10-4.4 10-13.2 0-1.6 0-3.1 0-4 0-.5-.1-.8-.8-.8\" fill=\"#ff717f\"/><path fill=\"#e2596c\" d=\"M33.5 44 32 57.8 30.5 44z\"/></svg>";
	protected String dissappointed="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><g fill=\"#664e27\"><path d=\"m25.5 28.4c1.4 2.9-.4 6.6-3.9 8.3-3.5 1.6-7.5.6-8.9-2.3-.8-1.9 12-7.9 12.8-6\"/><path d=\"m38.5 28.4c-1.4 2.9.4 6.6 3.9 8.3 3.5 1.6 7.5.6 8.9-2.3.8-1.9-12-7.9-12.8-6\"/></g><g fill=\"#917524\"><path d=\"m22.7 19.8c-2.7 3.3-9.2 6.3-13.5 6.3-.6 0-.7 2.2 0 2.2 4.9 0 12-3.3 15.2-7.1.5-.5-1.3-1.8-1.7-1.4\"/><path d=\"m41.3 19.8c2.7 3.3 9.2 6.3 13.5 6.3.6 0 .7 2.2 0 2.2-4.9 0-12-3.3-15.2-7.1-.5-.5 1.3-1.8 1.7-1.4\"/></g><path d=\"m40.6 46.4c-5.4-2.5-11.8-2.5-17.2 0-1.3.6.3 4.2 1.7 3.5 3.6-1.7 8.9-2.3 13.9 0 1.3.6 3-2.8 1.6-3.5\" fill=\"#664e27\"/></svg>";
	protected String angry="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><path d=\"m41 49.7c-5.8-4.8-12.2-4.8-18 0-.7.6-1.3-.4-.8-1.3 1.8-3.4 5.3-6.5 9.8-6.5s8.1 3.1 9.8 6.5c.5.8-.1 1.8-.8 1.3\" fill=\"#664e27\"/><path d=\"m10.2 24.9c-1.5 4.7.6 10 5.3 12.1 4.6 2.2 10 .5 12.7-3.7l-6.9-7.7-11.1-.7\" fill=\"#fff\"/><g fill=\"#664e27\"><path d=\"m14.2 25.8c-1.4 2.9-.1 6.4 2.8 7.7 2.9 1.4 6.4.1 7.7-2.8 1-1.9-9.6-6.8-10.5-4.9\"/><path d=\"m10.2 24.9c1.6-1 3.5-1.5 5.4-1.5 1.9 0 3.8.5 5.6 1.3 1.7.8 3.3 2 4.6 3.4 1.2 1.5 2.2 3.2 2.4 5.1-1.3-1.3-2.6-2.4-4-3.4-1.4-1-2.8-1.8-4.2-2.4-1.5-.7-3-1.2-4.6-1.7-1.8-.3-3.4-.6-5.2-.8\"/></g><path d=\"m53.8 24.9c1.5 4.7-.6 10-5.3 12.1-4.6 2.2-10 .5-12.7-3.7l6.9-7.7 11.1-.7\" fill=\"#fff\"/><g fill=\"#664e27\"><path d=\"m49.8 25.8c1.4 2.9.1 6.4-2.8 7.7-2.9 1.4-6.4.1-7.7-2.8-1-1.9 9.6-6.8 10.5-4.9\"/><path d=\"m53.8 24.9c-1.6-1-3.5-1.5-5.4-1.5-1.9 0-3.8.5-5.6 1.3-1.7.8-3.3 2-4.6 3.4-1.2 1.5-2.2 3.2-2.4 5.1 1.3-1.3 2.6-2.4 4-3.4 1.4-1 2.8-1.8 4.2-2.4 1.5-.7 3-1.2 4.6-1.7 1.8-.3 3.4-.6 5.2-.8\"/></g></svg>";
	protected String cry="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><path d=\"m40.6 46.4c-5.4-2.5-11.8-2.5-17.2 0-1.3.6.3 4.2 1.7 3.5 3.6-1.7 8.9-2.3 13.9 0 1.3.6 3-2.8 1.6-3.5\" fill=\"#664e27\"/><path d=\"m54 31c0 5-4 9-9 9-5 0-9-4-9-9 0-5 4-9 9-9 5 0 9 4 9 9\" fill=\"#fff\"/><circle cx=\"45\" cy=\"31\" r=\"6\" fill=\"#664e27\"/><g fill=\"#fff\"><ellipse cx=\"46.6\" cy=\"35.5\" rx=\"2.8\" ry=\"3.2\"/><ellipse cx=\"42.8\" cy=\"31\" rx=\"1.6\" ry=\"1.9\"/><path d=\"m28 31c0 5-4 9-9 9-5 0-9-4-9-9 0-5 4-9 9-9 5 0 9 4 9 9\"/></g><circle cx=\"19\" cy=\"31\" r=\"6\" fill=\"#664e27\"/><g fill=\"#fff\"><ellipse cx=\"20.6\" cy=\"35.5\" rx=\"2.8\" ry=\"3.2\"/><ellipse cx=\"16.8\" cy=\"31\" rx=\"1.6\" ry=\"1.9\"/></g><path d=\"m47 36c-5.1 6.8-8 13-8 18.1 0 4.4 3.6 7.9 8 7.9 4.4 0 8-3.5 8-7.9 0-5.1-3-11.4-8-18.1\" fill=\"#65b1ef\"/><g fill=\"#917524\"><path d=\"m53.2 20.7c-3.2-2.7-7.5-3.9-11.7-3.1-.6.1-1.1-2-.4-2.2 4.8-.9 9.8.5 13.5 3.6.6.5-1 2.1-1.4 1.7\"/><path d=\"m22.5 17.4c-4.2-.7-8.5.4-11.7 3.1-.4.4-2-1.2-1.4-1.7 3.7-3.2 8.7-4.5 13.5-3.6.7.2.2 2.3-.4 2.2\"/></g></svg>";
	protected String persevere="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><g fill=\"#ff717f\"><ellipse transform=\"matrix(.4226-.9063.9063.4226 1.9347 65.6611)\" cx=\"52.5\" cy=\"31.3\" rx=\"6.5\" ry=\"9\" opacity=\".8\"/><ellipse transform=\"matrix(.9063-.4226.4226.9063-12.1546 7.7934)\" cx=\"11.5\" cy=\"31.3\" rx=\"9\" ry=\"6.5\" opacity=\".8\"/></g><g fill=\"#664e27\"><path d=\"m19.4 42.2c8.1-5.7 17.1-5.6 25.2 0 1 .7 1.8-.5 1.2-1.6-2.5-4-7.4-7.7-13.8-7.7s-11.3 3.6-13.8 7.7c-.6 1.1.2 2.3 1.2 1.6\"/><path d=\"m51.7 15.1c.6.3.3 1-.2 1.1-2.7.4-5.5.9-8.3 2.4 4 .7 7.2 2.7 9 4.8.4.5-.1 1.1-.5 1-4.8-1.7-9.7-2.7-15.8-2-.5 0-.9-.2-.8-.7 1.6-7.3 10.9-10 16.6-6.6\"/><path d=\"m12.3 15.1c-.6.3-.3 1 .2 1.1 2.7.4 5.5.9 8.3 2.4-4 .7-7.2 2.7-9 4.8-.4.5.1 1.1.5 1 4.8-1.7 9.7-2.7 15.8-2 .5 0 .9-.2.8-.7-1.6-7.3-10.9-10-16.6-6.6\"/></g></svg>";
	protected String fearful="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><circle cx=\"19.5\" cy=\"24.8\" r=\"10\" fill=\"#fff\"/><circle cx=\"19.5\" cy=\"24.8\" r=\"3.5\" fill=\"#664e27\"/><circle cx=\"44.5\" cy=\"24.8\" r=\"10\" fill=\"#fff\"/><g fill=\"#664e27\"><circle cx=\"44.5\" cy=\"24.8\" r=\"3.5\"/><path d=\"m47.7 44c-1.7-3.6-5.9-6-15.7-6-9.8 0-14 2.4-15.7 6-.9 1.9.4 5 .4 5 1.6 3.9 1.4 5 15.3 5 13.9 0 13.6-1.1 15.3-5 0 0 1.3-3.1.4-5\"/></g><path d=\"m43.4 43c.1-.3 0-.6-.2-.8 0 0-2.5-2.2-11.1-2.2s-11.1 2.2-11.1 2.2c-.2.2-.3.5-.2.8l.2.6c.1.3.4.5.7.5h21c.3 0 .6-.2.7-.5v-.6\" fill=\"#fff\"/><g fill=\"#917524\"><path d=\"m50.2 13.5c-3.2-2.7-7.5-3.9-11.7-3.1-.6.1-1.1-2-.4-2.2 4.8-.9 9.8.5 13.5 3.6.6.4-1 2-1.4 1.7\"/><path d=\"m25.5 10.2c-4.2-.7-8.5.4-11.7 3.1-.4.4-2-1.2-1.4-1.7 3.7-3.2 8.7-4.5 13.5-3.6.7.1.2 2.3-.4 2.2\"/></g></svg>";
	protected String flushed="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><g fill=\"#ff717f\"><circle cx=\"52.8\" cy=\"37.1\" r=\"8\"/><circle cx=\"11.2\" cy=\"37.1\" r=\"8\"/></g><g fill=\"#917524\"><path d=\"m54.6 20.6c-2.7-3.3-6.7-5.1-11-5.1-.6 0-.7-2.2 0-2.2 4.9 0 9.5 2.1 12.7 5.9.5.6-1.3 1.9-1.7 1.4\"/><path d=\"m20.3 15.4c-4.2 0-8.3 1.9-11 5.1-.4.5-2.1-.8-1.7-1.4 3.1-3.8 7.8-5.9 12.7-5.9.7 0 .6 2.2 0 2.2\"/></g><circle cx=\"32\" cy=\"49.5\" r=\"4.5\" fill=\"#664e27\"/><path d=\"m29.5 29.1c0 5-4 9-9 9-5 0-9-4-9-9 0-5 4-9 9-9 5 0 9 4.1 9 9\" fill=\"#fff\"/><circle cx=\"20.5\" cy=\"29.1\" r=\"4.5\" fill=\"#664e27\"/><path d=\"m52.5 29.1c0 5-4 9-9 9-5 0-9-4-9-9 0-5 4-9 9-9 5 0 9 4.1 9 9\" fill=\"#fff\"/><circle cx=\"43.5\" cy=\"29.1\" r=\"4.5\" fill=\"#664e27\"/></svg>";
	protected String dizzyface="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><g fill=\"#664e27\"><path d=\"m28.6 19.8l-12.1 12.2c-.8.8-3.6-2-2.8-2.8l12.2-12.2c.7-.7 3.5 2.1 2.7 2.8\"/><path d=\"m25.8 32l-12.1-12.2c-.8-.8 2-3.6 2.8-2.8l12.2 12.2c.7.8-2.1 3.6-2.9 2.8\"/><path d=\"m50.3 19.8l-12.1 12.2c-.8.8-3.6-2-2.8-2.8l12.2-12.2c.7-.7 3.5 2.1 2.7 2.8\"/><path d=\"m47.5 32l-12.1-12.2c-.8-.8 2-3.6 2.8-2.8l12.2 12.2c.7.8-2.1 3.6-2.9 2.8\"/><circle cx=\"32\" cy=\"47\" r=\"9\"/></g><path d=\"m26 44c1.2-2.4 3.4-4 6-4 2.6 0 4.8 1.6 6 4h-12\" fill=\"#fff\"/></svg>";
	protected String okwoman="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><path d=\"M32,2C17.3,2,9.3,11.2,9.3,23.3v26.9h45.3V23.3c0-18-12.3-17.7-12.3-17.7S41.5,2,32,2z\" fill=\"#ffb300\"/><g fill=\"#ffdd67\"><path d=\"m31.2 3.2c0-.7-5-.6-6.5-.6-2.6 0-9.8 3.8-9.8 3.8s3.2 4 4.8 4c2.2 0 4.4-1.5 4.4-3.6 0-1.6 7.1-1.5 7.1-3.6\"/><path d=\"m32.8 3.2c0-.7 5-.6 6.5-.6 2.6 0 9.8 3.8 9.8 3.8s-3.2 4-4.8 4c-2.2 0-4.4-1.5-4.4-3.6 0-1.6-7.1-1.5-7.1-3.6\"/></g><path d=\"m61.7 25.8c-1.3-5.1-7.2-12.1-12.5-20.4l-5.9 5.7c0 0 5.7 5.1 9.9 18-2.4 5.9-5.4 14.9-9.6 17.6-2.4-1.2-6.1-1.2-11.6-1.2-5.5 0-9.2 0-11.6 1.2-4.2-2.7-7.2-11.7-9.6-17.6 4.2-12.9 9.9-18 9.9-18l-5.9-5.7c-5.4 8.3-11.2 15.3-12.5 20.4-2.4 9.6 11.1 29.3 10.9 36.2h37.6c-.2-6.9 13.3-26.6 10.9-36.2\" fill=\"#c28fef\"/><path d=\"m15.3 33.5c0 0-3.4-.9-3.4-5.8 0-3.9 2.5-4.6 2.5-4.6 9.1 0 25.9-11.7 25.9-11.7s3.5 10.3 8.9 11.7c0 0 2.6.5 2.6 4.6 0 4.9-3.5 5.8-3.5 5.8 0 7.4-10.3 16.4-16.5 16.4-6 0-16.5-8.9-16.5-16.4\" fill=\"#ffdd67\"/><path d=\"m32 38.4c-3.2 0-4.8-2.3-3.2-2.3s4.8 0 6.4 0c1.6 0 0 2.3-3.2 2.3\" fill=\"#eba352\"/><path d=\"m36.8 31.6c-1.1 0-1-1.4-1-1.4 1.7-8.8 11.5-4.1 11.5-4.1.8 1.5-1.8 5.3-2.5 5.5-3.1.8-8 0-8 0\" fill=\"#f5f5f5\"/><circle cx=\"41.2\" cy=\"28\" r=\"3.5\" fill=\"#664e27\"/><circle cx=\"41.2\" cy=\"28\" r=\"1.2\" fill=\"#2b2925\"/><path d=\"m35.8 30.2c1.7-10.5 10.4-3.5 13.8-5-3.4 3.2-10.3-3.8-13.8 5\" fill=\"#3b3226\"/><path d=\"m27.2 31.6c1.1 0 1-1.4 1-1.4-1.7-8.8-11.5-4.1-11.5-4.1-.8 1.5 1.8 5.3 2.5 5.5 3.1.8 8 0 8 0\" fill=\"#f5f5f5\"/><circle cx=\"22.8\" cy=\"28\" r=\"3.5\" fill=\"#664e27\"/><path d=\"m28.1 30.2c-1.7-10.5-10.4-3.5-13.8-5 3.5 3.2 10.4-3.8 13.8 5\" fill=\"#3b3226\"/><path d=\"m32 42.2l-7.7-1.2c4.6 6.6 10.8 6.6 15.4 0l-7.7 1.2\" fill=\"#f09985\"/><path d=\"m35 40.8c-2.3-.9-3 .5-3 .5s-.8-1.4-3-.5c-1.8.7-4.7.3-4.7.3 3.1.7 3.5 1.9 7.7 1.9 4.2 0 4.6-1.3 7.7-1.9 0 0-2.9.4-4.7-.3\" fill=\"#d47f6c\"/><circle cx=\"22.8\" cy=\"28\" r=\"1.2\" fill=\"#2b2925\"/></svg>";
	protected String innocent="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><path d=\"M61,33c0,16-13,29-29,29C16,62,3,49,3,33C3,17,16,4,32,4C48,4,61,17,61,33z\" fill=\"#ffdd67\"/><g fill=\"#664e27\"><path d=\"m28.6 34.4c-1.8-4.9-4.5-7.4-7.2-7.4-2.7 0-5.4 2.5-7.2 7.4-.2.5.7 1.4 1.2.9 1.7-1.8 3.8-2.6 6-2.6 2.2 0 4.3.7 6 2.6.5.5 1.4-.4 1.2-.9\"/><path d=\"m49.8 34.4c-1.8-4.9-4.5-7.4-7.2-7.4-2.7 0-5.4 2.5-7.2 7.4-.2.5.7 1.4 1.2.9 1.7-1.8 3.8-2.6 6-2.6 2.2 0 4.3.7 6 2.6.4.5 1.4-.4 1.2-.9\"/><path d=\"m44.2 42.2c-7.8 5.5-16.5 5.4-24.3 0-.9-.7-1.8.5-1.1 1.5 2.4 3.9 7.2 7.4 13.3 7.4 6.1 0 10.9-3.5 13.3-7.4.5-1-.3-2.1-1.2-1.5\"/></g><path d=\"m54.3 7.2c-.5-4.2-8.4-6.4-25.8-4.6-16.3 1.8-24.6 5.7-24.2 9.8.7 6.4 12.2 8.6 26 7.1 13.8-1.4 24.6-5.9 24-12.3m-24.7 5.4c-9.2 1-16.9.4-17-1.2-.1-.6.9-1.2 2.5-1.9 4.7-3.4 10.6-5.5 16.9-5.5 4.5 0 8.8 1 12.6 2.9.9.3 1.4.6 1.4 1 .2 1.5-7.2 3.7-16.4 4.7\" fill=\"#4aa9ff\"/></svg>";
	protected String sunglasses="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><path d=\"M32,2c16.6,0,30,13.4,30,30S48.6,62,32,62C15.4,62,2,48.6,2,32S15.4,2,32,2\" fill=\"#ffdd67\"/><path d=\"m35.8 20.5c-2.2 1.1-5.5 1.1-7.7 0-2.3-1.2-5.2-2-8.7-2.3-3.4-.3-10.5-.3-14 1-.4.1-.8.3-1.2.5-.1.1-.2.2-.2.6v.5c0 1-.1.6.6 1 1.4.8 2.2 2.9 2.6 5.8.6 4.2 2.7 6.9 6 8.1 3.1 1.2 6.6 1.1 9.7-.1 1.7-.7 3.2-1.7 4.4-3.5 2.1-3 1.4-4.9 2.5-7.5.9-2.3 3.5-2.3 4.5 0 1.1 2.6.4 4.5 2.5 7.5 1.2 1.7 2.7 2.8 4.4 3.5 3.1 1.2 6.6 1.3 9.7.1 3.4-1.3 5.4-3.9 6-8.1.4-2.9 1.2-5 2.6-5.8.7-.4.6 0 .6-1v-.5c0-.4 0-.5-.3-.6-.4-.2-.8-.4-1.2-.5-3.6-1.3-10.7-1.3-14-1-3.5.3-6.4 1.1-8.8 2.3\" fill=\"#494949\"/><path d=\"m44.6 42.3c-8.1 5.7-17.1 5.6-25.2 0-1-.7-1.8.5-1.2 1.6 2.5 4 7.4 7.7 13.8 7.7s11.3-3.6 13.8-7.7c.6-1.1-.2-2.3-1.2-1.6\" fill=\"#664e27\"/></svg>";
	protected String expressionless="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><g fill=\"#664e27\"><path d=\"m40 48h-16c-1.5 0-1.5-4 0-4h16c1.5 0 1.5 4 0 4\"/><path d=\"m27.1 32h-16c-1.5 0-1.5-4 0-4h16c1.5 0 1.5 4 0 4\"/><path d=\"m52.9 32h-16c-1.5 0-1.5-4 0-4h16c1.5 0 1.5 4 0 4\"/></g></svg>";
	protected String confused="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><path d=\"m2.5 37.2c2.9 16.3 18.4 27.2 34.8 24.3 16.3-2.9 27.2-18.4 24.3-34.8-2.9-16.2-18.5-27.1-34.8-24.2-16.3 2.8-27.2 18.4-24.3 34.7\" fill=\"#ffdd67\"/><g fill=\"#664e27\"><circle cx=\"42.4\" cy=\"24.7\" r=\"5\"/><circle cx=\"19.7\" cy=\"28.7\" r=\"5\"/><path d=\"m43.3 41.8c-5.8-1.5-12-.4-16.9 3-1.2.9 1.1 4 2.3 3.2 3.2-2.3 8.4-3.8 13.7-2.4 1.3.3 2.4-3.3.9-3.8\"/></g></svg>";
	protected String stuckouttongue="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><path d=\"m47.9 38c-3.3 0-9.7 0-15.9 0s-12.6 0-15.9 0c-.7 0-1.1.5-1.1 1 0 7.3 6 15 17 15s17-7.7 17-15c0-.5-.4-1-1.1-1\" fill=\"#664e27\"/><path d=\"m41.2 44c-2.3 0-9.2 0-9.2 0s-6.9 0-9.2 0c-.7 0-.8.3-.8.8 0 .9 0 2.4 0 4 0 8.8 4.5 13.2 10 13.2 5.5 0 10-4.4 10-13.2 0-1.6 0-3.1 0-4 0-.5-.1-.8-.8-.8\" fill=\"#ff717f\"/><path fill=\"#e2596c\" d=\"M33.5 44 32 57.8 30.5 44z\"/><g fill=\"#664e27\"><circle cx=\"20.5\" cy=\"24.5\" r=\"5\"/><circle cx=\"43.5\" cy=\"24.5\" r=\"5\"/></g></svg>";
	protected String openmouth="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><g fill=\"#664e27\"><circle cx=\"32\" cy=\"45.1\" r=\"7\"/><circle cx=\"20.2\" cy=\"25\" r=\"4.5\"/><circle cx=\"42.7\" cy=\"25\" r=\"4.5\"/></g></svg>";
	protected String nomouth="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><g fill=\"#664e27\"><circle cx=\"20.5\" cy=\"28.5\" r=\"5\"/><circle cx=\"43.5\" cy=\"28.5\" r=\"5\"/></g></svg>";
	*/
	
	public boolean IsTextSelectable=true;
	
	/**
	 * 
	 * maxWidthPx: default = 450
	 * maxBubbleWidthPx: default = 255
	 */
	public void Initialize(ABMPage page, String id, int heightPx, int maxWidthPx, int maxBubbleWidthPx, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.MaxWidthPx=maxWidthPx;
		this.MaxBubbleWidthPx=maxBubbleWidthPx;
		this.HeightPx = heightPx;
		this.Type = ABMaterial.UITYPE_CHAT;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Chats.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Chats.get(themeName.toLowerCase()).Clone();				
			}
		}	
		ChatBubbles.Initialize();
		IsInitialized=true;
	}
	
	/**
	 * You can only use this method if the cell has a fixed height or a fixed height from bottom! 
	 */
	public void Initialize2(ABMPage page, String id, int maxWidthPx, int maxBubbleWidthPx, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.MaxWidthPx=maxWidthPx;
		this.MaxBubbleWidthPx=maxBubbleWidthPx;
		this.HeightPx = Integer.MIN_VALUE;
		this.Type = ABMaterial.UITYPE_CHAT;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Chats.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Chats.get(themeName.toLowerCase()).Clone();				
			}
		}	
		ChatBubbles.Initialize();
		IsInitialized=true;
	}
	
	public void SetMyFrom(String myFrom) {
		this.MyFrom = myFrom.toLowerCase();
	}
	
	public ABMChatBubble AddBubble(String isFrom, String text, String extraInfo, String forMeThemeName, String forThemThemeName) {
		BA.LogError("AddBubble may be depreciated in the future: use AppendBubble, InsertBubbleAt instead!");
		if (forMeThemeName.equals("")) {
			forMeThemeName = "default";
		}
		if (forThemThemeName.equals("")) {
			forThemThemeName = "default";
		}
		ABMChatBubble bubble = new ABMChatBubble();
		bubble.Initialize(isFrom,  text,  extraInfo, forMeThemeName.toLowerCase(), forThemThemeName.toLowerCase());
		msgCounter++;
		bubble.ID = "abm_msg" + msgCounter;
		ChatBubbles.Add(bubble);	
		return bubble;
	}
	
	public ABMChatBubble AddBubble2(ABMChatBubble bubble) {
		BA.LogError("AddBubble2 may be depreciated in the future: use AppendBubble, InsertBubbleAt instead!");
		msgCounter++;
		bubble.ID = "abm_msg" + msgCounter;
		ABMChatBubble NewBubble = bubble.Clone();
		NewBubble.ForMeThemeName = bubble.ForMeThemeName.toLowerCase();
		NewBubble.ForThemThemeName = bubble.ForThemThemeName.toLowerCase();
		NewBubble.IsLoaded=false;		
		ChatBubbles.Add(NewBubble);
		return bubble;		
	}
	
	public ABMChatBubble InsertBubbleAt(int index, ABMChatBubble bubble) {
		msgCounter++;
		bubble.ID = "abm_msg" + msgCounter;
		ABMChatBubble NewBubble = bubble.Clone();
		NewBubble.ForMeThemeName = bubble.ForMeThemeName.toLowerCase();
		NewBubble.ForThemThemeName = bubble.ForThemThemeName.toLowerCase();
		NewBubble.IsLoaded=false;
		ChatBubbles.InsertAt(index, NewBubble);
		return bubble;	
	}
			
	public ABMChatBubble AppendBubble(ABMChatBubble bubble) {
		msgCounter++;
		bubble.ID = "abm_msg" + msgCounter;
		ABMChatBubble NewBubble = bubble.Clone();
		NewBubble.ForMeThemeName = bubble.ForMeThemeName.toLowerCase();
		NewBubble.ForThemThemeName = bubble.ForThemThemeName.toLowerCase();
		NewBubble.IsLoaded=false;
		ChatBubbles.Add(NewBubble);
		return bubble;	
	}
		
	public ABMChatBubble RemoveBubbleByMessageId(String messageId) {
		for (int i=0;i<ChatBubbles.getSize();i++) {
			ABMChatBubble bubble = (ABMChatBubble) ChatBubbles.Get(i);
			if (bubble.MessageId.equals(messageId)) {
				ABMaterial.RemoveHTML(Page, bubble.ID);
				ChatBubbles.RemoveAt(i);
				return bubble;
			}
		}
		BA.Log("No message found with id: " + messageId);
		return null;
	}
	
	public ABMChatBubble RemoveBubbleByIndex(int index) {
		ABMChatBubble bubble = (ABMChatBubble) ChatBubbles.Get(index);
		if (bubble!=null) {
			ABMaterial.RemoveHTML(Page, bubble.ID);
			ChatBubbles.RemoveAt(index);
		}
		return bubble;
	}
	
	public ABMChatBubble UpdateBubble(ABMChatBubble bubble) {
		if (bubble.ID.equals("")) {
			BA.LogError("Unable to update - Bubble with " + bubble.MessageId + " does not exist yet!");
			return null;
		}
		for (int i=0;i<ChatBubbles.getSize();i++) {
			ABMChatBubble tmpBubble = (ABMChatBubble) ChatBubbles.Get(i);
			if (tmpBubble.MessageId.equals(bubble.MessageId)) {
				ABMChatBubble NewBubble = bubble.Clone();
				NewBubble.ForMeThemeName = bubble.ForMeThemeName.toLowerCase();
				NewBubble.ForThemThemeName = bubble.ForThemThemeName.toLowerCase();
				NewBubble.IsLoaded=false;
				NewBubble.Replacing=true;
				ChatBubbles.Set(i, NewBubble);		
				return NewBubble;
			}
		}		
		return null;
	}	
	
	public ABMChatBubble GetBubbleByIndex(int index) {
		if (index<ChatBubbles.getSize()) {
			return (ABMChatBubble) ChatBubbles.Get(index);
		} else {
			BA.Log("index > size of bubbles list");
			return null;
		}
	}
	
	public ABMChatBubble GetBubbleByMessageId(String messageId) {
		for (int i=0;i<ChatBubbles.getSize();i++) {
			ABMChatBubble bubble = (ABMChatBubble) ChatBubbles.Get(i);
			if (bubble.MessageId.equals(messageId)) {
				return bubble;
			}
		}
		BA.Log("No message found with id: " + messageId);
		return null;
	}
		
	public void ScrollToBottom() {
		if (Page.ws!=null) {
			Page.ws.Eval("var d = $('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "');d.animate({scrollTop: d.prop('scrollHeight')},300);", null);
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("ScrollToBottom: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}			
		}
	}
	
	public void ScrollToBubble(String messageId) {		
		if (Page.ws!=null) {
			for (int i=0;i<ChatBubbles.getSize();i++) {
				ABMChatBubble bubble = (ABMChatBubble) ChatBubbles.Get(i);
				if (bubble.MessageId.equals(messageId)) {
					Page.ws.Eval("var d = $('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "');d.animate({scrollTop: $('#" + bubble.ID + "').offset().top},1);", null);
					try {
						if (Page.ws.getOpen()) {
							if (Page.ShowDebugFlush) {BA.Log("ScrollToBubble " + ID);}
							Page.ws.Flush();Page.RunFlushed();
						}
					} catch (IOException e) {
						//e.printStackTrace();
					}
					return;
				}
			}
			BA.Log("No message found with id: " + messageId);
		}
	}
	
	public void ThemAreTyping(boolean bool, String forThemThemeName) {		
		if (bool) {
			typingBubble = new ABMChatBubble();
			typingBubble.Initialize("",  "",  "", "",  forThemThemeName.toLowerCase());			
			CancelTypingShown=false;
		} else {
			typingBubble=null;		
			CancelTypingShown=true;
		}
	}
			
	public List GetConversation() {
		List ret = new List();
		ret.Initialize();
		for (int i=0;i<ChatBubbles.getSize();i++) {
			ret.Add(ChatBubbles.Get(i));
		}
		return ret;
	}
	
	public void ClearConversation() {
		ChatBubbles.Initialize();
		ChatBubbles.Clear();
		msgCounter=0;
		StringBuilder s = new StringBuilder();
		s.append("<section id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "section\" class=\"bubbles\" style=\"max-width: " + MaxWidthPx + "px;margin:50px auto;width: 98%;overflow-x: hidden\">");
		s.append(BuildBody());
		s.append("</section>");
		
		String ss = "$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').html('" + s.toString() + "');";
		if (Page.ws!=null) {
			Page.ws.Eval(ss, null);
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("ClearConversation: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}			
		}
	}
	
	public void SetConversation(List conversation) {
		for (int i=0;i<conversation.getSize();i++) {
			ABMChatBubble bubble = (ABMChatBubble) conversation.Get(i);
			ABMChatBubble NewBubble = bubble.Clone();
			NewBubble.ForMeThemeName = bubble.ForMeThemeName.toLowerCase();			
			NewBubble.ForThemThemeName = bubble.ForThemThemeName.toLowerCase();
			if (NewBubble.ID.equals("")) {
				msgCounter++;
				NewBubble.ID = "abm_msg" + msgCounter;
			}
			ChatBubbles.Add(NewBubble);
		}
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
	}	
		
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Chats.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Chats.get(themeName.toLowerCase()).Clone();				
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
	
	protected String BuildJavaScript() {
		StringBuilder s = new StringBuilder();	
		s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').off('scroll').on('scroll', function(){");
		s.append("if($(this).scrollTop() === 0){");
		s.append("b4j_raiseEvent('page_parseevent', {'eventname': '" + ID.toLowerCase() + "_scrolledtotop','eventparams': 'target', 'target': '" + ID + "'});");
		s.append("}");
		s.append("});");
		return s.toString();
	}
	
	
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		super.Refresh();
		ThemeChat l = Theme;
		int prev=0;
		
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		j.SetProp("class", BuildClass());
		
		for (int i=0;i<ChatBubbles.getSize();i++) {
			ABMChatBubble bubble = (ABMChatBubble) ChatBubbles.Get(i);
			if (!bubble.IsLoaded) {
				ThemeChatBubble lbMe;
				ThemeChatBubble lbThem;
				if (l.Bubbles.containsKey(bubble.ForMeThemeName.toLowerCase())) {
					lbMe = l.Bubbles.get(bubble.ForMeThemeName.toLowerCase());
				} else {
					lbMe = l.Bubbles.get("default");
				}
				if (l.Bubbles.containsKey(bubble.ForThemThemeName.toLowerCase())) {
					lbThem = l.Bubbles.get(bubble.ForThemThemeName.toLowerCase());
				} else {
					lbThem = l.Bubbles.get("default");
				}
				String extra = "";
				String text="";
				String style="";
				if (!bubble.ExtraStyle.contains("margin-bottom")) {
					style=" style=\"margin-bottom: 0px;" + bubble.ExtraStyle + "\"";
				} else {
					style=" style=\"" + bubble.ExtraStyle + "\"";
				}
				String extraFirst="";
				
				extraFirst = " style=\"padding-bottom:1px;margin-top:10px\" ";
				
				if (i>0) {									
					if (bubble.IsFrom.toLowerCase().equals(MyFrom)) {
						if (!bubble.ExtraInfo.equals("")) {
							extra = "<p class=\"bubbles-from-meex" + l.ThemeName.toLowerCase() + "-" + lbMe.ThemeName.toLowerCase() + "\">" + BuildBodyTextNoEmoji(bubble.ExtraInfo) + "</p>";
						}
						if (bubble.mRawHTML.equals("")) {
							text = BuildBodyText(bubble.mText);
						} else {
							text = bubble.mRawHTML;
						}
						if (bubble.Replacing) {
							ABMaterial.ReplaceMyInnerHTML(Page, bubble.ID, "<div class=\"bubbles-clear\"" + extraFirst + ">" + extra + "</div><div class=\"bubbles-from-me bubbles-from-me" + l.ThemeName.toLowerCase() + "-" + lbMe.ThemeName.toLowerCase() + "\" style=\"max-width:" + MaxBubbleWidthPx + "px\"><div" + style + ">" + text + "</div></div>");							
						} else {
							ABMChatBubble prevBubble = (ABMChatBubble) ChatBubbles.Get(i-1);
							ABMaterial.InsertHTMLAfter(Page, prevBubble.ID, "<div id=\"" + bubble.ID + "\"><div class=\"bubbles-clear\">" + extra + "</div><div class=\"bubbles-from-me bubbles-from-me" + l.ThemeName.toLowerCase() + "-" + lbMe.ThemeName.toLowerCase() + "\" style=\"max-width:" + MaxBubbleWidthPx + "px\"><div" + style + ">" + text + "</div></div></div>");
						}						
					} else {
						if (!bubble.ExtraInfo.equals("")) {
							extra = "<p class=\"bubbles-from-themex" + l.ThemeName.toLowerCase() + "-" + lbMe.ThemeName.toLowerCase() + "\">" + BuildBodyTextNoEmoji(bubble.ExtraInfo) + "</p>";
						}
						if (bubble.mRawHTML.equals("")) {
							text = BuildBodyText(bubble.mText);
						} else {
							text = bubble.mRawHTML;
						}
						if (bubble.Replacing) {
							ABMaterial.ReplaceMyInnerHTML(Page, bubble.ID, "<div class=\"bubbles-clear\"" + extraFirst + ">" + extra + "</div><div class=\"bubbles-from-them bubbles-from-them" + l.ThemeName.toLowerCase() + "-" + lbThem.ThemeName.toLowerCase() + "\" style=\"max-width:" + MaxBubbleWidthPx + "px\"><div" + style + ">" + text + "</div></div>");
						} else {
							ABMChatBubble prevBubble = (ABMChatBubble) ChatBubbles.Get(i-1);
							ABMaterial.InsertHTMLAfter(Page, prevBubble.ID, "<div id=\"" + bubble.ID + "\"><div class=\"bubbles-clear\">" + extra + "</div><div class=\"bubbles-from-them bubbles-from-them" + l.ThemeName.toLowerCase() + "-" + lbThem.ThemeName.toLowerCase() + "\" style=\"max-width:" + MaxBubbleWidthPx + "px\"><div" + style + ">" + text + "</div></div></div>");
						}
					}
				} else {
					if (bubble.IsFrom.toLowerCase().equals(MyFrom)) {
						if (!bubble.ExtraInfo.equals("")) {
							extra = "<p class=\"bubbles-from-meex" + l.ThemeName.toLowerCase() + "-" + lbMe.ThemeName.toLowerCase() + "\">" + BuildBodyTextNoEmoji(bubble.ExtraInfo) + "</p>";
						}
						if (bubble.mRawHTML.equals("")) {
							text = BuildBodyText(bubble.mText);
						} else {
							text = bubble.mRawHTML;
						}
						if (bubble.Replacing) {
							ABMaterial.ReplaceMyInnerHTML(Page, bubble.ID, "<div class=\"bubbles-clear\"" + extraFirst + ">" + extra + "</div><div class=\"bubbles-from-me bubbles-from-me" + l.ThemeName.toLowerCase() + "-" + lbMe.ThemeName.toLowerCase() + "\" style=\"max-width:" + MaxBubbleWidthPx + "px\"><div" + style + ">" + text + "</div></div>");
						} else {
							ABMaterial.PrependHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "section", "<div id=\"" + bubble.ID + "\"><div class=\"bubbles-clear\"" + extraFirst + ">" + extra + "</div><div class=\"bubbles-from-me bubbles-from-me" + l.ThemeName.toLowerCase() + "-" + lbMe.ThemeName.toLowerCase() + "\" style=\"max-width:" + MaxBubbleWidthPx + "px\"><div" + style + ">" + text + "</div></div></div>");
						}
					} else {
						if (!bubble.ExtraInfo.equals("")) {
							extra = "<p class=\"bubbles-from-themex" + l.ThemeName.toLowerCase() + "-" + lbThem.ThemeName.toLowerCase() + "\">" + BuildBodyTextNoEmoji(bubble.ExtraInfo) + "</p>";
						}
						if (bubble.mRawHTML.equals("")) {
							text = BuildBodyText(bubble.mText);
						} else {
							text = bubble.mRawHTML;
						}
						if (bubble.Replacing) {
							ABMaterial.ReplaceMyInnerHTML(Page, bubble.ID, "<div class=\"bubbles-clear\"" + extraFirst + ">" + extra + "</div><div class=\"bubbles-from-them bubbles-from-them" + l.ThemeName.toLowerCase() + "-" + lbThem.ThemeName.toLowerCase() + "\" style=\"max-width:" + MaxBubbleWidthPx + "px\"><div" + style + ">" + text + "</div></div>");
						} else {
							ABMaterial.PrependHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "section", "<div id=\"" + bubble.ID + "\"><div class=\"bubbles-clear\"" + extraFirst + ">" + extra + "</div><div class=\"bubbles-from-them bubbles-from-them" + l.ThemeName.toLowerCase() + "-" + lbThem.ThemeName.toLowerCase() + "\" style=\"max-width:" + MaxBubbleWidthPx + "px\"><div" + style + ">" + text + "</div></div></div>");
						}
					}
				}
				bubble.IsLoaded=true;
				ChatBubbles.Set(i, bubble);
			}	
			prev++;
		}
		if (typingBubble!=null && TypingShown==false) {
			TypingShown=true;
			ThemeChatBubble lbThem;
			if (l.Bubbles.containsKey(typingBubble.ForThemThemeName.toLowerCase())) {
				lbThem = l.Bubbles.get(typingBubble.ForThemThemeName.toLowerCase());
			} else {
				lbThem = l.Bubbles.get("default");
			}
			if (ChatBubbles.getSize()>0) {
				ABMChatBubble prevBubble = (ABMChatBubble) ChatBubbles.Get(prev-1);
				ABMaterial.InsertHTMLAfter(Page, prevBubble.ID, "<div class=\"bubbles-clear bubble-typing" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\"></div><div class=\"bubbles-from-them bubbles-from-them" + l.ThemeName.toLowerCase() + "-" + lbThem.ThemeName.toLowerCase() + " bubble-typing" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"max-width:" + MaxBubbleWidthPx + "px\"><span>.</span><span>.</span><span>.</span></div>");	
			} else {
				String extraFirst = " style=\"padding-bottom:1px;\" ";
				ABMaterial.AddHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "<div class=\"bubbles-clear bubble-typing" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\"" + extraFirst + "></div><div class=\"bubbles-from-them bubbles-from-them" + l.ThemeName.toLowerCase() + "-" + lbThem.ThemeName.toLowerCase() + " bubble-typing" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"max-width:" + MaxBubbleWidthPx + "px\"><span>.</span><span>.</span><span>.</span></div>");	
			}
		} else {
			if (CancelTypingShown) {
				TypingShown=false;
				CancelTypingShown=false;
				typingBubble=null;
				ABMaterial.RemoveBubbles(Page,ParentString + ArrayName.toLowerCase() + ID.toLowerCase());				
			}
		}
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Chat Refresh: " + ID);}
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
		ThemeChat l = Theme;
		
		if (HeightPx==Integer.MIN_VALUE) {
			s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"height: 100%;overflow-y: scroll;background: " + ABMaterial.GetColorStrMap(l.BackgroundColor, l.BackgroundColorIntensity) + "\" class=\"" + BuildClass() + "\">");
		} else {			
			s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" style=\"height: " + HeightPx + "px;overflow-y: scroll;background: " + ABMaterial.GetColorStrMap(l.BackgroundColor, l.BackgroundColorIntensity) + "\" class=\"" + BuildClass() + "\">");
		}
		s.append("<section id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "section\" class=\"bubbles\" style=\"max-width: " + MaxWidthPx + "px;margin:50px auto;width: 98%;overflow-x: hidden\">");
		s.append(BuildBody());
		s.append("</section>");
		s.append("</div>");
		IsBuild=true;
		return s.toString();
	}
	
	@Hide
	protected String BuildClass() {			
		StringBuilder s = new StringBuilder();
		s.append(super.BuildExtraClasses());
		if (!IsTextSelectable) {
			s.append(" notselectable ");
		}
		s.append(mVisibility + " ");
		s.append(mIsPrintableClass);
		s.append(mIsOnlyForPrintClass);
		return s.toString(); 
	}
	
	@Hide
	protected String BuildBody() {
		StringBuilder s = new StringBuilder();
		ThemeChat l = Theme;
		for (int i=0;i<ChatBubbles.getSize();i++) {
			ABMChatBubble bubble = (ABMChatBubble) ChatBubbles.Get(i);
			ThemeChatBubble lbMe;
			ThemeChatBubble lbThem;
			if (l.Bubbles.containsKey(bubble.ForMeThemeName.toLowerCase())) {
				lbMe = l.Bubbles.get(bubble.ForMeThemeName.toLowerCase());
			} else {
				lbMe = l.Bubbles.get("default");
			}
			if (l.Bubbles.containsKey(bubble.ForThemThemeName.toLowerCase())) {
				lbThem = l.Bubbles.get(bubble.ForThemThemeName.toLowerCase());
			} else {
				lbThem = l.Bubbles.get("default");
			}
			String extra="";
			String extraFirst="";
			
			extraFirst = " style=\"padding-bottom:1px;margin-top:10px\" ";
			
			String text="";
			String style="";
			if (!bubble.ExtraStyle.contains("margin-bottom")) {
				style=" style=\"margin-bottom: 0px;" + bubble.ExtraStyle + "\"";
			} else {
				style=" style=\"" + bubble.ExtraStyle + "\"";
			}
			if (bubble.IsFrom.toLowerCase().equals(MyFrom)) {
				s.append("<div id=\"" + bubble.ID + "\">");
				if (!bubble.ExtraInfo.equals("")) {
					extra = "<p class=\"bubbles-from-meex" + l.ThemeName.toLowerCase() + "-" + lbMe.ThemeName.toLowerCase() + "\">" + BuildBodyTextNoEmoji(bubble.ExtraInfo) + "</p>";					
				}
				s.append("<div class=\"bubbles-clear\"" + extraFirst + ">" + extra + "</div>");
				if (bubble.mRawHTML.equals("")) {
					text = BuildBodyText(bubble.mText);
				} else {
					text = bubble.mRawHTML;
				}
				s.append("<div class=\"bubbles-from-me bubbles-from-me" + l.ThemeName.toLowerCase() + "-" + lbMe.ThemeName.toLowerCase() + "\" style=\"max-width:" + MaxBubbleWidthPx + "px\"><div" + style + ">" + text + "</div></div>");
				s.append("</div>");
			} else {
				s.append("<div id=\"" + bubble.ID + "\">");
				if (!bubble.ExtraInfo.equals("")) {
					extra = "<p class=\"bubbles-from-themex" + l.ThemeName.toLowerCase() + "-" + lbThem.ThemeName.toLowerCase() + "\">" + BuildBodyTextNoEmoji(bubble.ExtraInfo) + "</p>";
				}
				s.append("<div class=\"bubbles-clear\"" + extraFirst + ">" + extra + "</div>");
				if (bubble.mRawHTML.equals("")) {
					text = BuildBodyText(bubble.mText);
				} else {
					text = bubble.mRawHTML;
				}
				s.append("<div class=\"bubbles-from-them bubbles-from-them" + l.ThemeName.toLowerCase() + "-" + lbThem.ThemeName.toLowerCase() + "\" style=\"max-width:" + MaxBubbleWidthPx + "px\"><div" + style + ">" + text + "</div></div>");
				s.append("</div>");
			}
			bubble.IsLoaded = true;
			ChatBubbles.Set(i,  bubble);
		}
		return s.toString();
	}
	
	protected String BuildBodyText(String text) {
		StringBuilder s = new StringBuilder();	
		String v = PrepareEmoji(text);
		v = ABMaterial.HTMLConv().htmlEscape(v, Page.PageCharset);
		v = ConvertEmoji(v);
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
	
	protected String BuildBodyTextNoEmoji(String text) {
		StringBuilder s = new StringBuilder();	
		String v = text;
		v = ABMaterial.HTMLConv().htmlEscape(v, Page.PageCharset);
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
				repl = "<i class=\"" + IconName + " " + IconColor + "\"></i>";
				break;
			case "fa ":
			case "fa-":
				repl = "<i class=\"" + IconName + " " + IconColor + "\"></i>";
				break;
			case "abm":
				repl = "<i>" + Page.svgIconmap.getOrDefault(IconName.toLowerCase(), "") + "</i>"; 
				break;
			default:
				repl = "<i class=\"material-icons " + IconColor + "\">" + IconName + "</i>";
			}
			v=v.replace(vv,repl );
			start = v.indexOf("{IC:");
		}
		
		s.append(v);
		
		return s.toString();
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMChat c = new ABMChat();
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
	
	protected String PrepareEmoji(String text) {
		if (!SupportEmoji) return text;
		String v = text;
		
		// 5
		v = v.replace("*\\0/*", "y:yok_womany:y");
		v = v.replace("*\\O/*", "y:yok_womany:y");
		v = v.replace("*\\o/*", "y:yok_womany:y");
		v = v.replace("-___-", "y:yexpressionlessy:y");
		
		// 4
		v = v.replace(":'-)", "y:yjoyy:y");
		v = v.replace("':-)", "y:ysweat_smiley:y");
		v = v.replace("':-D", "y:ysweat_smiley:y");
		v = v.replace("':-d", "y:ysweat_smiley:y");
		v = v.replace(">:-)", "y:ylaughingy:y");
		v = v.replace("':-(", "y:ysweaty:y");
		v = v.replace(">:-(", "y:yangryy:y");
		v = v.replace(":'-(", "y:ycryy:y");
		v = v.replace("O:-)", "y:yinnocenty:y");
		v = v.replace("o:-)", "y:yinnocenty:y");
		v = v.replace("0:-3", "y:yinnocenty:y");
		v = v.replace("0:-)", "y:yinnocenty:y");
		v = v.replace("0;^)", "y:yinnocenty:y");
		v = v.replace("O:-)", "y:yinnocenty:y");
		v = v.replace("o:-)", "y:yinnocenty:y");
		v = v.replace("O;-)", "y:yinnocenty:y");
		v = v.replace("o;-)", "y:yinnocenty:y");
		v = v.replace("0;-)", "y:yinnocenty:y");
		v = v.replace("O:-3", "y:yinnocenty:y");
		v = v.replace("o:-3", "y:yinnocenty:y");
		v = v.replace("-__-", "y:yexpressionlessy:y");
		
		
		//3
		v = v.replace("</3", "y:ybroken_hearty:y");
		v = v.replace(":')", "y:yjoyy:y");
		v = v.replace(":-D", "y:ysmileyy:y");
		v = v.replace(":-d", "y:ysmileyy:y");
		v = v.replace(":-)", "y:yslight_smiley:y");
		v = v.replace("':)", "y:ysweat_smiley:y");		
		v = v.replace("'=)", "y:ysweat_smiley:y");
		v = v.replace("':D", "y:ysweat_smiley:y");
		v = v.replace("'=D", "y:ysweat_smiley:y");
		v = v.replace("':d", "y:ysweat_smiley:y");
		v = v.replace("'=d", "y:ysweat_smiley:y");
		v = v.replace(">:)", "y:ylaughingy:y");
		v = v.replace(">;)", "y:ylaughingy:y");
		v = v.replace(">=)", "y:ylaughingy:y");
		v = v.replace(";-)", "y:ywinky:y");
		v = v.replace("*-)", "y:ywinky:y");
		v = v.replace(";-]", "y:ywinky:y");		
		v = v.replace(";^)", "y:ywinky:y");
		v = v.replace("':(", "y:ysweaty:y");
		v = v.replace("'=(", "y:ysweaty:y");
		v = v.replace(":-*", "y:ykissing_hearty:y");		
		v = v.replace(":^*", "y:ykissing_hearty:y");
		v = v.replace(">:P", "y:ystuck_out_tongue_winking_eyey:y");
		v = v.replace(">:p", "y:ystuck_out_tongue_winking_eyey:y");
		v = v.replace("X-P", "y:ystuck_out_tongue_winking_eyey:y");
		v = v.replace("x-p", "y:ystuck_out_tongue_winking_eyey:y");
		v = v.replace(">:[", "y:ydisappointedy:y");
		v = v.replace(":-(", "y:ydisappointedy:y");		
		v = v.replace(":-[", "y:ydisappointedy:y");
		v = v.replace(">:(", "y:yangryy:y");
		v = v.replace(":'(", "y:ycryy:y");
		v = v.replace(";-(", "y:ycryy:y");
		v = v.replace(">.<", "y:yperseverey:y");
		v = v.replace("#-)", "y:ydizzy_facey:y");
		v = v.replace("%-)", "y:ydizzy_facey:y");
		v = v.replace("X-)", "y:ydizzy_facey:y");
		v = v.replace("x-)", "y:ydizzy_facey:y");
		v = v.replace("\\0/", "y:yok_womany:y");
		v = v.replace("\\O/", "y:yok_womany:y");
		v = v.replace("\\o/", "y:yok_womany:y");
		v = v.replace("0:3", "y:yinnocenty:y");
		v = v.replace("0:)", "y:yinnocenty:y");
		v = v.replace("O:)", "y:yinnocenty:y");
		v = v.replace("o:)", "y:yinnocenty:y");
		v = v.replace("O=)", "y:yinnocenty:y");
		v = v.replace("o=)", "y:yinnocenty:y");
		v = v.replace("O:3", "y:yinnocenty:y");
		v = v.replace("o:3", "y:yinnocenty:y");
		v = v.replace("B-)", "y:ysunglassesy:y");
		v = v.replace("b-)", "y:ysunglassesy:y");
		v = v.replace("8-)", "y:ysunglassesy:y");
		v = v.replace("B-D", "y:ysunglassesy:y");
		v = v.replace("8-D", "y:ysunglassesy:y");
		v = v.replace("8-d", "y:ysunglassesy:y");
		v = v.replace("-_-", "y:yexpressionlessy:y");
		v = v.replace(">:\\", "y:yconfusedy:y");
		v = v.replace(">:/", "y:yconfusedy:y");
		v = v.replace(":-/", "y:yconfusedy:y");
		v = v.replace(":-.", "y:yconfusedy:y");
		v = v.replace(":-P", "y:ystuck_out_tonguey:y");
		v = v.replace(":-p", "y:ystuck_out_tonguey:y");
		v = v.replace(":-Þ", "y:ystuck_out_tonguey:y");
		v = v.replace(":-b", "y:ystuck_out_tonguey:y");
		v = v.replace(":-O", "y:yopen_mouthy:y");
		v = v.replace(":-o", "y:yopen_mouthy:y");
		v = v.replace("O_O", "y:yopen_mouthy:y");
		v = v.replace(">:O", "y:yopen_mouthy:y");
		v = v.replace(":-X", "y:yno_mouthy:y");
		v = v.replace(":-#", "y:yno_mouthy:y");
		v = v.replace(":-x", "y:yno_mouthy:y");
				
		//2		
		v = v.replace("<3", "y:yhearty:y");
		v = v.replace(":D", "y:ysmileyy:y");		
		v = v.replace("=D", "y:ysmileyy:y");		
		v = v.replace(":d", "y:ysmileyy:y");
		v = v.replace("=d", "y:ysmileyy:y");
		
		v = v.replace(":)", "y:yslight_smiley:y");
		v = v.replace("=]", "y:yslight_smiley:y");
		v = v.replace("=)", "y:yslight_smiley:y");
		v = v.replace(":]", "y:yslight_smiley:y");
		
		v = v.replace(";)", "y:ywinky:y");
		v = v.replace("*)", "y:ywinky:y");
		v = v.replace(";]", "y:ywinky:y");
		v = v.replace(";D", "y:ywinky:y");
		v = v.replace(";d", "y:ywinky:y");
		
		v = v.replace(":*", "y:ykissing_hearty:y");
		v = v.replace("=*", "y:ykissing_hearty:y");
		
		v = v.replace(":(", "y:ydisappointedy:y");
		v = v.replace(":[", "y:ydisappointedy:y");
		v = v.replace("=(", "y:ydisappointedy:y");
		
		v = v.replace(":@", "y:yangryy:y");
		
		v = v.replace(";(", "y:ycryy:y");
		
		v = v.replace("D:", "y:yfearfuly:y");
				
		v = v.replace(":$", "y:yflushedy:y");
		v = v.replace("=$", "y:yflushedy:y");		
		
		v = v.replace("#)", "y:ydizzy_facey:y");
		v = v.replace("%)", "y:ydizzy_facey:y");
		v = v.replace("X)", "y:ydizzy_facey:y");
		v = v.replace("x)", "y:ydizzy_facey:y");
				
		v = v.replace("B)", "y:ysunglassesy:y");
		v = v.replace("b)", "y:ysunglassesy:y");
		v = v.replace("8)", "y:ysunglassesy:y");
				
		v = v.replace(":/", "y:yconfusedy:y");
		v = v.replace(":\\", "y:yconfusedy:y");
		v = v.replace("=/", "y:yconfusedy:y");
		v = v.replace("=\\", "y:yconfusedy:y");
		v = v.replace(":L", "y:yconfusedy:y");
		v = v.replace("=L", "y:yconfusedy:y");
		
		v = v.replace(":P", "y:ystuck_out_tonguey:y");
		v = v.replace("=P", "y:ystuck_out_tonguey:y");
		v = v.replace(":p", "y:ystuck_out_tonguey:y");
		v = v.replace("=p", "y:ystuck_out_tonguey:y");
		v = v.replace(":Þ", "y:ystuck_out_tonguey:y");
		v = v.replace(":þ", "y:ystuck_out_tonguey:y");
		v = v.replace("-þ", "y:ystuck_out_tonguey:y");
		v = v.replace(":b", "y:ystuck_out_tonguey:y");
		v = v.replace(":B", "y:ystuck_out_tonguey:y");
		v = v.replace("d:", "y:ystuck_out_tonguey:y");
		
		v = v.replace(":O", "y:yopen_mouthy:y");
		v = v.replace(":o", "y:yopen_mouthy:y");
		
		
		v = v.replace(":X", "y:yno_mouthy:y");
		v = v.replace(":#", "y:yno_mouthy:y");
		v = v.replace("=X", "y:yno_mouthy:y");
		v = v.replace("=x", "y:yno_mouthy:y");
		v = v.replace(":x", "y:yno_mouthy:y");
		v = v.replace("=#", "y:yno_mouthy:y");
		return v;
	}
	
	protected String ConvertEmoji(String text) {
		if (!SupportEmoji) return text;
		String v = text;
		v = v.replace("y:yhearty:y", ABMaterial.heart);		
		v = v.replace("y:ybroken_hearty:y", ABMaterial.brokenheart);		
		v = v.replace("y:yjoyy:y", ABMaterial.joy);				
		v = v.replace("y:ysmileyy:y", ABMaterial.smiley);		
		v = v.replace("y:yslight_smiley:y", ABMaterial.slightsmile);
		v = v.replace("y:ysweat_smiley:y", ABMaterial.sweatsmile);
		v = v.replace("y:ylaughingy:y", ABMaterial.laughing);
		v = v.replace("y:ywinky:y", ABMaterial.wink);
		v = v.replace("y:ysweaty:y", ABMaterial.sweat);
		v = v.replace("y:ykissing_hearty:y", ABMaterial.kissingheart);
		v = v.replace("y:ystuck_out_tongue_winking_eyey:y", ABMaterial.stuckouttonguewink);
		v = v.replace("y:ydisappointedy:y", ABMaterial.dissappointed);
		v = v.replace("y:yangryy:y", ABMaterial.angry);
		v = v.replace("y:ycryy:y", ABMaterial.cry);
		v = v.replace("y:yperseverey:y", ABMaterial.persevere);
		v = v.replace("y:yfearfuly:y", ABMaterial.fearful);
		v = v.replace("y:yflushedy:y", ABMaterial.flushed);
		v = v.replace("y:ydizzy_facey:y", ABMaterial.dizzyface);
		v = v.replace("y:yok_womany:y", ABMaterial.okwoman);
		v = v.replace("y:yinnocenty:y", ABMaterial.innocent);
		v = v.replace("y:ysunglassesy:y", ABMaterial.sunglasses);
		v = v.replace("y:yexpressionlessy:y", ABMaterial.expressionless);
		v = v.replace("y:yconfusedy:y", ABMaterial.confused);
		v = v.replace("y:ystuck_out_tonguey:y", ABMaterial.stuckouttongue);
		v = v.replace("y:yopen_mouthy:y", ABMaterial.openmouth);
		v = v.replace("y:yno_mouthy:y", ABMaterial.nomouth);
		
		return v;
	}
		

}

