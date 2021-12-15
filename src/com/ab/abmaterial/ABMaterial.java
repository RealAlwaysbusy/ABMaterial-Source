package com.ab.abmaterial;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.zip.GZIPOutputStream;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.net.ssl.HttpsURLConnection;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.Cookie;
import com.ab.abmaterial.ABMChronologyList.ChronoSlide;
import com.ab.abmaterial.ABMCombo.ABMComboItem;
import com.ab.abmaterial.ABMList.ABMItem;
import com.ab.abmaterial.ABMTable.TableValue;
import com.ab.abmaterial.ABMTableInfinite.TableValueInfinite;
import com.ab.abmaterial.ABMTableMutable.TableValueMutable;
import com.ab.abmaterial.ThemePage.ExtraColor;
import com.ab.b4js.B4JS;
import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import com.googlecode.pngtastic.core.PngImage;
import com.googlecode.pngtastic.core.PngOptimizer;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.DependsOn;
import anywheresoftware.b4a.BA.DesignerName;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.RaisesSynchronousEvents;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.BA.Version;
import anywheresoftware.b4a.keywords.Regex;
import anywheresoftware.b4j.object.HttpSessionWrapper;
import anywheresoftware.b4j.object.JServlet.ServletRequestWrapper;
import anywheresoftware.b4j.object.JServlet.ServletResponseWrapper;
import anywheresoftware.b4j.object.WebSocket;
import anywheresoftware.b4j.object.WebSocket.SimpleFuture;
import anywheresoftware.b4a.BA.CustomClasses;
import anywheresoftware.b4a.BA.CustomClass;

@CustomClasses(values = {
	       @CustomClass(name = "ABM Web Page", fileNameWithoutExtension = "abm_page"),
	       @CustomClass(name = "ABM Custom Component", fileNameWithoutExtension = "abm_customcomponent"),
	       @CustomClass(name = "ABM B4JS", fileNameWithoutExtension = "abm_b4js")
})

@DesignerName("Build 20211215")                                    
@Version(4.95F)                                
@Author("Alain Bailleul")      
@ShortName("ABMaterial")    
@DependsOn(values={"jServer", "pngtastic-1.1", "microsoft-translator-java-api-0.6.2-jar-with-dependencies", "prettytime-4.0.1.Final", "commons-lang3-3.4", "thumbnailator-0.4.8", "httpcore-4.4.8", "httpclient-4.5.1", "cloudinary-core-1.19.0", "cloudinary-http44-1.19.0", "httpmime-4.5.6", "commons-logging-1.2", "jsoup-1.11.3"}) //, "yuicompressor-2.4.8", "rhino-1.7.8"}) //, "hazelcast-all-3.8.4"})
public class ABMaterial {
	public final static String VersionName="Dragonfly";
	public final static String Version="4.95";
	public final static String Version495="4.95";
	
	protected final static String CSSMaterialize=".4.35";
	protected final static String CSSTimeline=".3.00";
	protected final static String CSSChronology=".3.75";
	protected final static String CSSSquire=".3.75";
	protected final static String CSSFileManager=".4.31";
	protected final static String JSCore=".4.39";
	protected final static String JSAbmPivotExport=".3.02";
	protected final static String JSAbmPivot=".4.62";
	protected final static String JSAbmCalendar=".3.00";
	protected final static String JSAbmTreeTable=".3.00";
	protected final static String JSAbmPlanner=".3.00";
	protected final static String JSSVGSurface=".4.38";
	protected final static String JSAbmSquire=".4.02";
	protected final static String JSAbmUpload=".3.00";
	protected final static String JSSmartWizard=".3.00";
	protected final static String JSAPlayer=".3.00";
	protected final static String JSAbmCanvas=".4.25";
	protected final static String JSChartist=".4.00";
	protected final static String JSABScrollFire=".3.00";
	protected final static String JSDateTimePicker=".4.00";
	protected final static String JSPatternLock=".3.00";
	protected final static String JSMobiScroll=".4.00";
	protected final static String JSJSSocials=".4.26";
	protected final static String JSInputMask=".3.01";
	protected final static String JSFlexImages=".3.00";
	protected final static String JSTimeline=".4.00";
	protected final static String JSSortTable=".4.32";
	protected final static String JSMoment=".3.00";
	protected final static String JSNoUISlider=".3.00";
	protected final static String JSABMTableScroll=".4.00";
	protected final static String JSHello=".3.00";
	protected final static String JSSignaturePad=".3.00";
	protected final static String JSGmaps=".4.70";
	protected final static String JSLangAll=".3.00";
	protected final static String JSConzole=".3.20";
	protected final static String JSFileManager=".4.31";
	
	protected static String CacheSystem="1.0";	
	protected static Cloudinary cloudinary=null;	
	protected static boolean TryToActivate3DAcceleration=false;	
	protected static boolean mUseWebWorker=false;
	protected static Map<String,String> PageMappings = new HashMap<String,String>();
	
	public static int ThresholdPxConsiderdSmall=600;
	public static int ThresholdPxConsiderdMedium=992;
	public static int ThresholdPxConsiderdLarge=1200;
	
	protected static Map<String,String> ContainerThresholds = new LinkedHashMap<String,String>();
	
	public static long SiteExpires=0;
	
	protected static int FontPercentSmall=100;
	protected static int FontPercentMedium=100;
	protected static int FontPercentLarge=100;
	protected static int FontPercentExtraLarge=100;
	
	private boolean IsApp=false;
	public static boolean PreloadAllJavascriptAndCSSFiles=false;
	protected static boolean GZip=false;
	protected static int minGZipSize=0;
	
	protected static boolean CDN=false;
	protected static boolean CloudCDN=false;
	protected static String CloudAppName="";
	protected static boolean CloudUploadGeneratedJSCSS=false;
	protected static boolean CloudDebug=false;
	
	protected static String UseBrowserAutocomplete="";
	
	protected static String CloudCoreCSSUrl="";
	protected static anywheresoftware.b4a.objects.collections.Map CloudinaryMap = new anywheresoftware.b4a.objects.collections.Map();
	protected static Map<String,String> CloudinaryToDeleteMap = new LinkedHashMap<String,String>();
	protected static boolean CW=false;
	protected static String mCDNUrl="";
		
	protected static boolean PNGGamma = false;
	protected static int PNGLevel=0;
	protected static List<String>PNGFolders = new ArrayList<String>();
	protected static boolean PNGOpt=false;
	protected static boolean PNGKeepOriginals=true;
	protected static boolean PNGExtendedLog=false;
	
	protected static String mAppVersion="";
	public static String AppPublishedStartURL="";
	protected static List<Entity> SiteMaps=new ArrayList<Entity>();
	protected static Map<String,Boolean> AllNeeds = new LinkedHashMap<String, Boolean>();
	protected static Map<String,String> SavedNeeds = new LinkedHashMap<String, String>();
	protected static Map<String,String> SavedB4JSClasses = new LinkedHashMap<String, String>();
		
	protected static Map<String,Boolean> NeededIcons = new LinkedHashMap<String, Boolean>();
	protected static String BuildedIconsCSS="";
	protected static boolean NeedsAwesomeFont=false;

	protected int B4JSTrackFromLineNumber=-1;
	protected int B4JSTrackToLineNumber=-1;
	protected String B4JSTrackModule="";
	
	protected static ScriptEngineManager factory=null;
	protected static ScriptEngine jsEngine=null;
	
	protected static Map<String,String> iconmap = new LinkedHashMap<String,String>();
	
	public static boolean UseMaterialFontsFromWebLink=false;
	
	@Hide
	public static boolean IsSinglePageApp=false;
	
	protected enum SCOPE {
		PRIVATE, PUBLIC, GLOBAL;
	}
	
	public NativeApp Native = new NativeApp();
	
	public String WriteAppRedirect="";
	
	protected static boolean HasSourceFolder=false;
	protected static Map<String,String> B4JSClassGenerated = new LinkedHashMap<String,String>();
	
	private static final char[] hp = "bWzPlaxItmDV1Cw9".toCharArray();
    private static final byte[] hs = { (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12, (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12, };
	
	public static boolean LoadJavascriptsAfterDOM=false;
	public static boolean LoadCSSAfterDOM=false;
	
	public boolean DebugSkipCheckSourceFolder=false;
	
	protected boolean DoNativeAppGeneration=false;
	protected boolean DoNativeAppGenerationOverwrite=false;
	
	public static boolean UseDebugScriptsAndCSS=false;
	public static boolean AnalyserDisableAll=false;
	
	public static String SessionCreatorClassV2="abmsessioncreator";
	public static String SessionCacheControlV3="abmcachecontrol";
	
	protected static final double PI=3.141592653589793238463;
	
	protected static String heart="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><path d=\"m61.1 18.2c-6.4-17-27.2-9.4-29.1-.9-2.6-9-22.9-15.7-29.1.9-6.9 18.5 26.7 35.1 29.1 37.8 2.4-2.2 36-19.6 29.1-37.8\" fill=\"#ff5a79\"/></svg>";
	protected static String brokenheart="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><path d=\"m61.1 19.1c-4.5-12.1-16.2-11.6-23.2-7.4l-5.3 11.3 10-.9-10.3 13.2 8.4-2.2-5.3 20.9-1.6-14.1-11.9 2.5 9.1-14.4-7.8-1.1 5.4-12.4c-6.4-6.4-23-7.6-26.2 7.8-3.9 18.3 27.3 29.6 32.9 32.7v.1c0 0 0 0 .1 0 0 0 .1 0 .1 0v-.1c8.3-5.5 31.3-20.5 25.6-35.9\" fill=\"#ff5a79\"/></svg>";
	protected static String joy="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><path d=\"m49.7 34.4c-.4-.5-1.1-.4-1.9-.4-15.8 0-15.8 0-31.6 0-.8 0-1.5-.1-1.9.4-3.9 5 .7 19.6 17.7 19.6 17 0 21.6-14.6 17.7-19.6\" fill=\"#664e27\"/><path d=\"m33.8 41.7c-.6 0-1.5.5-1.1 2 .2.7 1.2 1.6 1.2 2.8 0 2.4-3.8 2.4-3.8 0 0-1.2 1-2 1.2-2.8.3-1.4-.6-2-1.1-2-1.6 0-4.1 1.7-4.1 4.6 0 3.2 2.7 5.8 6 5.8 3.3 0 6-2.6 6-5.8-.1-2.8-2.7-4.5-4.3-4.6\" fill=\"#4c3526\"/><path d=\"m24.3 50.7c2.2 1 4.8 1.5 7.7 1.5 2.9 0 5.5-.6 7.7-1.5-2.1-1.1-4.7-1.7-7.7-1.7s-5.6.6-7.7 1.7\" fill=\"#ff717f\"/><path d=\"m47 36c-15 0-15 0-29.9 0-2.1 0-2.1 4-.1 4 10.4 0 19.6 0 30 0 2 0 2-4 0-4\" fill=\"#fff\"/><g fill=\"#65b1ef\"><path d=\"m59.4 36.9c7.3 7.7-2.6 18.1-9.9 10.4-5.3-5.6-5.6-16.3-5.6-16.3s10.2.3 15.5 5.9\"/><path d=\"m14.5 47.3c-7.3 7.7-17.2-2.7-9.9-10.4 5.3-5.6 15.5-5.9 15.5-5.9s-.3 10.7-5.6 16.3\"/></g><g fill=\"#664e27\"><path d=\"m28.5 28.7c-1.9-5.1-4.7-7.7-7.5-7.7s-5.6 2.6-7.5 7.7c-.2.5.8 1.4 1.3.9 1.8-1.9 4-2.7 6.2-2.7 2.2 0 4.4.8 6.2 2.7.6.5 1.5-.4 1.3-.9\"/><path d=\"m50.4 28.7c-1.9-5.1-4.7-7.7-7.5-7.7s-5.6 2.6-7.5 7.7c-.2.5.8 1.4 1.3.9 1.8-1.9 4-2.7 6.2-2.7s4.4.8 6.2 2.7c.5.5 1.5-.4 1.3-.9\"/></g></svg>";
	protected static String smiley="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><g fill=\"#664e27\"><circle cx=\"20.5\" cy=\"26.6\" r=\"5\"/><circle cx=\"43.5\" cy=\"26.6\" r=\"5\"/><path d=\"m44.6 40.3c-8.1 5.7-17.1 5.6-25.2 0-1-.7-1.8.5-1.2 1.6 2.5 4 7.4 7.7 13.8 7.7s11.3-3.6 13.8-7.7c.6-1.1-.2-2.3-1.2-1.6\"/></g></svg>";
	protected static String slightsmile="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><g fill=\"#664e27\"><circle cx=\"20.5\" cy=\"24.5\" r=\"5\"/><circle cx=\"43.5\" cy=\"24.5\" r=\"5\"/><path d=\"m49 38c0-.8-.5-1.8-1.8-2.1-3.5-.7-8.6-1.3-15.2-1.3-6.6 0-11.7.7-15.2 1.3-1.3.3-1.8 1.3-1.8 2.1 0 7.3 5.6 14.6 17 14.6 11.4 0 17-7.3 17-14.6\"/></g><path d=\"m44.7 38.3c-2.2-.4-6.8-1-12.7-1-5.9 0-10.5.6-12.7 1-1.3.2-1.4.7-1.3 1.5.1.4.1 1 .3 1.6.1.6.3.9 1.3.8 1.9-.2 23-.2 24.9 0 1 .1 1.1-.2 1.3-.8.1-.6.2-1.1.3-1.6 0-.8-.1-1.3-1.4-1.5\" fill=\"#fff\"/></svg>";
	protected static String sweatsmile="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><g fill=\"#664e27\"><path d=\"m25.5 26.9c-1.9-5.1-4.7-7.7-7.5-7.7s-5.6 2.6-7.5 7.7c-.2.5.8 1.4 1.3.9 1.8-1.9 4-2.7 6.2-2.7s4.4.8 6.2 2.7c.6.5 1.5-.4 1.3-.9\"/><path d=\"m47.4 26.9c-1.9-5.1-4.7-7.7-7.5-7.7s-5.6 2.6-7.5 7.7c-.2.5.8 1.4 1.3.9 1.8-1.9 4-2.7 6.2-2.7s4.4.8 6.2 2.7c.5.5 1.5-.4 1.3-.9\"/><path d=\"m46 38c0-.8-.5-1.8-1.8-2.1-3.5-.7-8.6-1.3-15.2-1.3-6.6 0-11.7.7-15.2 1.3-1.3.3-1.8 1.3-1.8 2.1 0 7.3 5.6 14.6 17 14.6 11.4 0 17-7.3 17-14.6\"/></g><path d=\"m41.7 38.3c-2.2-.4-6.8-1-12.7-1-5.9 0-10.5.6-12.7 1-1.3.2-1.4.7-1.3 1.5.1.4.1 1 .3 1.6.1.6.3.9 1.3.8 1.9-.2 23-.2 24.9 0 1 .1 1.1-.2 1.3-.8.1-.6.2-1.1.3-1.6 0-.8-.1-1.3-1.4-1.5\" fill=\"#fff\"/><path d=\"M60,30.2c0,7.2-9.7,7.2-9.7,0c0-5.2,4.9-10.4,4.9-10.4S60,25,60,30.2z\" fill=\"#65b1ef\"/></svg>";
	protected static String laughing="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><g fill=\"#664e27\"><path d=\"m51.7 19.4c.6.3.3 1-.2 1.1-2.7.4-5.5.9-8.3 2.4 4 .7 7.2 2.7 9 4.8.4.5-.1 1.1-.5 1-4.8-1.7-9.7-2.7-15.8-2-.5 0-.9-.2-.8-.7 1.6-7.3 10.9-10 16.6-6.6\"/><path d=\"m12.3 19.4c-.6.3-.3 1 .2 1.1 2.7.4 5.5.9 8.3 2.4-4 .7-7.2 2.7-9 4.8-.4.5.1 1.1.5 1 4.8-1.7 9.7-2.7 15.8-2 .5 0 .9-.2.8-.7-1.6-7.3-10.9-10-16.6-6.6\"/><path d=\"m49.7 34.4c-.4-.5-1.1-.4-1.9-.4-15.8 0-15.8 0-31.6 0-.8 0-1.5-.1-1.9.4-3.9 5 .7 19.6 17.7 19.6 17 0 21.6-14.6 17.7-19.6\"/></g><path d=\"m33.8 41.7c-.6 0-1.5.5-1.1 2 .2.7 1.2 1.6 1.2 2.8 0 2.4-3.8 2.4-3.8 0 0-1.2 1-2 1.2-2.8.3-1.4-.6-2-1.1-2-1.6 0-4.1 1.7-4.1 4.6 0 3.2 2.7 5.8 6 5.8s6-2.6 6-5.8c-.1-2.8-2.7-4.5-4.3-4.6\" fill=\"#4c3526\"/><path d=\"m24.3 50.7c2.2 1 4.8 1.5 7.7 1.5s5.5-.6 7.7-1.5c-2.1-1.1-4.7-1.7-7.7-1.7s-5.6.6-7.7 1.7\" fill=\"#ff717f\"/><path d=\"m47 36c-15 0-15 0-29.9 0-2.1 0-2.1 4-.1 4 10.4 0 19.6 0 30 0 2 0 2-4 0-4\" fill=\"#fff\"/></svg>";
	protected static String wink="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><circle cx=\"22.3\" cy=\"31.6\" r=\"5\" fill=\"#664e27\"/><g fill=\"#917524\"><path d=\"m51.2 27.5c-3.2-2.7-7.5-3.9-11.7-3.1-.6.1-1.1-2-.4-2.2 4.8-.9 9.8.5 13.5 3.6.6.5-1 2.1-1.4 1.7\"/><path d=\"m24.5 18.8c-4.2-.7-8.5.4-11.7 3.1-.4.4-2-1.2-1.4-1.7 3.7-3.2 8.7-4.5 13.5-3.6.7.2.2 2.3-.4 2.2\"/></g><g fill=\"#664e27\"><path d=\"m50.2 34.3c-1.7-3.5-4.4-5.3-7-5.3s-5.2 1.8-7 5.3c-.2.4.7 1 1.2.6 1.7-1.3 3.7-1.8 5.8-1.8s4.1.5 5.8 1.8c.4.3 1.3-.3 1.2-.6\"/><path d=\"m44.1 42.2c-6.9 3.6-16.4 2.9-19.1 2.9-.7 0-1.2.3-1 .9 2 7 17 7 21.1-2.7.5-1.1-.3-1.4-1-1.1\"/></g></svg>";
	protected static String sweat="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><path d=\"M2,32c0,16.6,13.4,30,30,30s30-13.4,30-30S48.6,2,32,2S2,15.4,2,32z\" fill=\"#ffdd67\"/><path d=\"M48.5,16.3c0,9.9,13.5,9.9,13.5,0C62,9.1,55.3,2,55.3,2S48.5,9.1,48.5,16.3z\" fill=\"#65b1ef\"/><g fill=\"#664e27\"><circle cx=\"43.5\" cy=\"36\" r=\"5\"/><circle cx=\"20.5\" cy=\"36\" r=\"5\"/></g><g fill=\"#917524\"><path d=\"m25.6 21.9c-3.2 2.7-7.5 3.9-11.7 3.1-.6-.1-1.1 2-.4 2.2 4.8.9 9.8-.5 13.5-3.6.5-.5-1-2.1-1.4-1.7\"/><path d=\"m50.1 24.9c-4.2.7-8.5-.4-11.7-3.1-.4-.4-2 1.2-1.4 1.7 3.7 3.2 8.7 4.5 13.5 3.6.7-.2.2-2.3-.4-2.2\"/></g><path d=\"m40 52h-16c-1.5 0-1.5-4 0-4h16c1.5 0 1.5 4 0 4\" fill=\"#664e27\"/></svg>";
	protected static String kissingheart="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><path fill=\"#f46767\" d=\"m50.9 58h-.1z\"/><ellipse cx=\"22\" cy=\"27\" rx=\"5\" ry=\"6\" fill=\"#664e27\"/><path d=\"m61.4 42.6c-.9-1.8-2.7-3-5-2.8-2.3.2-4.1 1.4-5.6 3.5-1.5-2-3.3-3.3-5.6-3.4-2.3-.1-4.1 1.1-5 2.9-.9 1.8-.9 4.2.9 7 1.8 2.7 9.5 8.2 9.7 8.4.2-.2 7.9-5.8 9.7-8.4 1.8-3 1.8-5.4.9-7.2\" fill=\"#f46767\"/><g fill=\"#664e27\"><path d=\"m51.9 30.5c-1.9-4.1-4.7-6.1-7.5-6.1s-5.6 2-7.5 6.1c-.2.4.8 1.2 1.3.8 1.8-1.5 4-2.1 6.2-2.1s4.4.6 6.2 2.1c.5.4 1.4-.3 1.3-.8\"/><path d=\"m39.5 50.4c1.6-1.6-1.8-3-1.8-5.9s3.4-4.2 1.8-5.9c-1.9-2-6-.5-8.6-3.1 0 2.2 1.8 4.5 5.2 4.5 0 0-2.3.9-2.3 4.5s2.3 4.5 2.3 4.5c-3.4 0-5.2 2.3-5.2 4.5 2.6-2.7 6.7-1.2 8.6-3.1\"/></g></svg>";
	protected static String stuckouttonguewink="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><path d=\"m31.2 24.6c0 5.5-4.5 10-10 10-5.5 0-10-4.5-10-10 0-5.5 4.5-10 10-10 5.6 0 10 4.5 10 10\" fill=\"#fff\"/><g fill=\"#664e27\"><circle cx=\"21.2\" cy=\"24.6\" r=\"4.5\"/><path d=\"m51 29.1c-1.9-4.1-4.7-6.1-7.5-6.1s-5.6 2-7.5 6.1c-.2.4.8 1.2 1.3.8 1.8-1.5 4-2.1 6.2-2.1s4.4.6 6.2 2.1c.5.4 1.5-.4 1.3-.8\"/><path d=\"m47.9 38c-3.3 0-9.7 0-15.9 0s-12.6 0-15.9 0c-.7 0-1.1.5-1.1 1 0 7.3 6 15 17 15s17-7.7 17-15c0-.5-.4-1-1.1-1\"/></g><path d=\"m41.2 44c-2.3 0-9.2 0-9.2 0s-6.9 0-9.2 0c-.7 0-.8.3-.8.8 0 .9 0 2.4 0 4 0 8.8 4.5 13.2 10 13.2 5.5 0 10-4.4 10-13.2 0-1.6 0-3.1 0-4 0-.5-.1-.8-.8-.8\" fill=\"#ff717f\"/><path fill=\"#e2596c\" d=\"M33.5 44 32 57.8 30.5 44z\"/></svg>";
	protected static String dissappointed="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><g fill=\"#664e27\"><path d=\"m25.5 28.4c1.4 2.9-.4 6.6-3.9 8.3-3.5 1.6-7.5.6-8.9-2.3-.8-1.9 12-7.9 12.8-6\"/><path d=\"m38.5 28.4c-1.4 2.9.4 6.6 3.9 8.3 3.5 1.6 7.5.6 8.9-2.3.8-1.9-12-7.9-12.8-6\"/></g><g fill=\"#917524\"><path d=\"m22.7 19.8c-2.7 3.3-9.2 6.3-13.5 6.3-.6 0-.7 2.2 0 2.2 4.9 0 12-3.3 15.2-7.1.5-.5-1.3-1.8-1.7-1.4\"/><path d=\"m41.3 19.8c2.7 3.3 9.2 6.3 13.5 6.3.6 0 .7 2.2 0 2.2-4.9 0-12-3.3-15.2-7.1-.5-.5 1.3-1.8 1.7-1.4\"/></g><path d=\"m40.6 46.4c-5.4-2.5-11.8-2.5-17.2 0-1.3.6.3 4.2 1.7 3.5 3.6-1.7 8.9-2.3 13.9 0 1.3.6 3-2.8 1.6-3.5\" fill=\"#664e27\"/></svg>";
	protected static String angry="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><path d=\"m41 49.7c-5.8-4.8-12.2-4.8-18 0-.7.6-1.3-.4-.8-1.3 1.8-3.4 5.3-6.5 9.8-6.5s8.1 3.1 9.8 6.5c.5.8-.1 1.8-.8 1.3\" fill=\"#664e27\"/><path d=\"m10.2 24.9c-1.5 4.7.6 10 5.3 12.1 4.6 2.2 10 .5 12.7-3.7l-6.9-7.7-11.1-.7\" fill=\"#fff\"/><g fill=\"#664e27\"><path d=\"m14.2 25.8c-1.4 2.9-.1 6.4 2.8 7.7 2.9 1.4 6.4.1 7.7-2.8 1-1.9-9.6-6.8-10.5-4.9\"/><path d=\"m10.2 24.9c1.6-1 3.5-1.5 5.4-1.5 1.9 0 3.8.5 5.6 1.3 1.7.8 3.3 2 4.6 3.4 1.2 1.5 2.2 3.2 2.4 5.1-1.3-1.3-2.6-2.4-4-3.4-1.4-1-2.8-1.8-4.2-2.4-1.5-.7-3-1.2-4.6-1.7-1.8-.3-3.4-.6-5.2-.8\"/></g><path d=\"m53.8 24.9c1.5 4.7-.6 10-5.3 12.1-4.6 2.2-10 .5-12.7-3.7l6.9-7.7 11.1-.7\" fill=\"#fff\"/><g fill=\"#664e27\"><path d=\"m49.8 25.8c1.4 2.9.1 6.4-2.8 7.7-2.9 1.4-6.4.1-7.7-2.8-1-1.9 9.6-6.8 10.5-4.9\"/><path d=\"m53.8 24.9c-1.6-1-3.5-1.5-5.4-1.5-1.9 0-3.8.5-5.6 1.3-1.7.8-3.3 2-4.6 3.4-1.2 1.5-2.2 3.2-2.4 5.1 1.3-1.3 2.6-2.4 4-3.4 1.4-1 2.8-1.8 4.2-2.4 1.5-.7 3-1.2 4.6-1.7 1.8-.3 3.4-.6 5.2-.8\"/></g></svg>";
	protected static String cry="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><path d=\"m40.6 46.4c-5.4-2.5-11.8-2.5-17.2 0-1.3.6.3 4.2 1.7 3.5 3.6-1.7 8.9-2.3 13.9 0 1.3.6 3-2.8 1.6-3.5\" fill=\"#664e27\"/><path d=\"m54 31c0 5-4 9-9 9-5 0-9-4-9-9 0-5 4-9 9-9 5 0 9 4 9 9\" fill=\"#fff\"/><circle cx=\"45\" cy=\"31\" r=\"6\" fill=\"#664e27\"/><g fill=\"#fff\"><ellipse cx=\"46.6\" cy=\"35.5\" rx=\"2.8\" ry=\"3.2\"/><ellipse cx=\"42.8\" cy=\"31\" rx=\"1.6\" ry=\"1.9\"/><path d=\"m28 31c0 5-4 9-9 9-5 0-9-4-9-9 0-5 4-9 9-9 5 0 9 4 9 9\"/></g><circle cx=\"19\" cy=\"31\" r=\"6\" fill=\"#664e27\"/><g fill=\"#fff\"><ellipse cx=\"20.6\" cy=\"35.5\" rx=\"2.8\" ry=\"3.2\"/><ellipse cx=\"16.8\" cy=\"31\" rx=\"1.6\" ry=\"1.9\"/></g><path d=\"m47 36c-5.1 6.8-8 13-8 18.1 0 4.4 3.6 7.9 8 7.9 4.4 0 8-3.5 8-7.9 0-5.1-3-11.4-8-18.1\" fill=\"#65b1ef\"/><g fill=\"#917524\"><path d=\"m53.2 20.7c-3.2-2.7-7.5-3.9-11.7-3.1-.6.1-1.1-2-.4-2.2 4.8-.9 9.8.5 13.5 3.6.6.5-1 2.1-1.4 1.7\"/><path d=\"m22.5 17.4c-4.2-.7-8.5.4-11.7 3.1-.4.4-2-1.2-1.4-1.7 3.7-3.2 8.7-4.5 13.5-3.6.7.2.2 2.3-.4 2.2\"/></g></svg>";
	protected static String persevere="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><g fill=\"#ff717f\"><ellipse transform=\"matrix(.4226-.9063.9063.4226 1.9347 65.6611)\" cx=\"52.5\" cy=\"31.3\" rx=\"6.5\" ry=\"9\" opacity=\".8\"/><ellipse transform=\"matrix(.9063-.4226.4226.9063-12.1546 7.7934)\" cx=\"11.5\" cy=\"31.3\" rx=\"9\" ry=\"6.5\" opacity=\".8\"/></g><g fill=\"#664e27\"><path d=\"m19.4 42.2c8.1-5.7 17.1-5.6 25.2 0 1 .7 1.8-.5 1.2-1.6-2.5-4-7.4-7.7-13.8-7.7s-11.3 3.6-13.8 7.7c-.6 1.1.2 2.3 1.2 1.6\"/><path d=\"m51.7 15.1c.6.3.3 1-.2 1.1-2.7.4-5.5.9-8.3 2.4 4 .7 7.2 2.7 9 4.8.4.5-.1 1.1-.5 1-4.8-1.7-9.7-2.7-15.8-2-.5 0-.9-.2-.8-.7 1.6-7.3 10.9-10 16.6-6.6\"/><path d=\"m12.3 15.1c-.6.3-.3 1 .2 1.1 2.7.4 5.5.9 8.3 2.4-4 .7-7.2 2.7-9 4.8-.4.5.1 1.1.5 1 4.8-1.7 9.7-2.7 15.8-2 .5 0 .9-.2.8-.7-1.6-7.3-10.9-10-16.6-6.6\"/></g></svg>";
	protected static String fearful="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><circle cx=\"19.5\" cy=\"24.8\" r=\"10\" fill=\"#fff\"/><circle cx=\"19.5\" cy=\"24.8\" r=\"3.5\" fill=\"#664e27\"/><circle cx=\"44.5\" cy=\"24.8\" r=\"10\" fill=\"#fff\"/><g fill=\"#664e27\"><circle cx=\"44.5\" cy=\"24.8\" r=\"3.5\"/><path d=\"m47.7 44c-1.7-3.6-5.9-6-15.7-6-9.8 0-14 2.4-15.7 6-.9 1.9.4 5 .4 5 1.6 3.9 1.4 5 15.3 5 13.9 0 13.6-1.1 15.3-5 0 0 1.3-3.1.4-5\"/></g><path d=\"m43.4 43c.1-.3 0-.6-.2-.8 0 0-2.5-2.2-11.1-2.2s-11.1 2.2-11.1 2.2c-.2.2-.3.5-.2.8l.2.6c.1.3.4.5.7.5h21c.3 0 .6-.2.7-.5v-.6\" fill=\"#fff\"/><g fill=\"#917524\"><path d=\"m50.2 13.5c-3.2-2.7-7.5-3.9-11.7-3.1-.6.1-1.1-2-.4-2.2 4.8-.9 9.8.5 13.5 3.6.6.4-1 2-1.4 1.7\"/><path d=\"m25.5 10.2c-4.2-.7-8.5.4-11.7 3.1-.4.4-2-1.2-1.4-1.7 3.7-3.2 8.7-4.5 13.5-3.6.7.1.2 2.3-.4 2.2\"/></g></svg>";
	protected static String flushed="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><g fill=\"#ff717f\"><circle cx=\"52.8\" cy=\"37.1\" r=\"8\"/><circle cx=\"11.2\" cy=\"37.1\" r=\"8\"/></g><g fill=\"#917524\"><path d=\"m54.6 20.6c-2.7-3.3-6.7-5.1-11-5.1-.6 0-.7-2.2 0-2.2 4.9 0 9.5 2.1 12.7 5.9.5.6-1.3 1.9-1.7 1.4\"/><path d=\"m20.3 15.4c-4.2 0-8.3 1.9-11 5.1-.4.5-2.1-.8-1.7-1.4 3.1-3.8 7.8-5.9 12.7-5.9.7 0 .6 2.2 0 2.2\"/></g><circle cx=\"32\" cy=\"49.5\" r=\"4.5\" fill=\"#664e27\"/><path d=\"m29.5 29.1c0 5-4 9-9 9-5 0-9-4-9-9 0-5 4-9 9-9 5 0 9 4.1 9 9\" fill=\"#fff\"/><circle cx=\"20.5\" cy=\"29.1\" r=\"4.5\" fill=\"#664e27\"/><path d=\"m52.5 29.1c0 5-4 9-9 9-5 0-9-4-9-9 0-5 4-9 9-9 5 0 9 4.1 9 9\" fill=\"#fff\"/><circle cx=\"43.5\" cy=\"29.1\" r=\"4.5\" fill=\"#664e27\"/></svg>";
	protected static String dizzyface="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><g fill=\"#664e27\"><path d=\"m28.6 19.8l-12.1 12.2c-.8.8-3.6-2-2.8-2.8l12.2-12.2c.7-.7 3.5 2.1 2.7 2.8\"/><path d=\"m25.8 32l-12.1-12.2c-.8-.8 2-3.6 2.8-2.8l12.2 12.2c.7.8-2.1 3.6-2.9 2.8\"/><path d=\"m50.3 19.8l-12.1 12.2c-.8.8-3.6-2-2.8-2.8l12.2-12.2c.7-.7 3.5 2.1 2.7 2.8\"/><path d=\"m47.5 32l-12.1-12.2c-.8-.8 2-3.6 2.8-2.8l12.2 12.2c.7.8-2.1 3.6-2.9 2.8\"/><circle cx=\"32\" cy=\"47\" r=\"9\"/></g><path d=\"m26 44c1.2-2.4 3.4-4 6-4 2.6 0 4.8 1.6 6 4h-12\" fill=\"#fff\"/></svg>";
	protected static String okwoman="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><path d=\"M32,2C17.3,2,9.3,11.2,9.3,23.3v26.9h45.3V23.3c0-18-12.3-17.7-12.3-17.7S41.5,2,32,2z\" fill=\"#ffb300\"/><g fill=\"#ffdd67\"><path d=\"m31.2 3.2c0-.7-5-.6-6.5-.6-2.6 0-9.8 3.8-9.8 3.8s3.2 4 4.8 4c2.2 0 4.4-1.5 4.4-3.6 0-1.6 7.1-1.5 7.1-3.6\"/><path d=\"m32.8 3.2c0-.7 5-.6 6.5-.6 2.6 0 9.8 3.8 9.8 3.8s-3.2 4-4.8 4c-2.2 0-4.4-1.5-4.4-3.6 0-1.6-7.1-1.5-7.1-3.6\"/></g><path d=\"m61.7 25.8c-1.3-5.1-7.2-12.1-12.5-20.4l-5.9 5.7c0 0 5.7 5.1 9.9 18-2.4 5.9-5.4 14.9-9.6 17.6-2.4-1.2-6.1-1.2-11.6-1.2-5.5 0-9.2 0-11.6 1.2-4.2-2.7-7.2-11.7-9.6-17.6 4.2-12.9 9.9-18 9.9-18l-5.9-5.7c-5.4 8.3-11.2 15.3-12.5 20.4-2.4 9.6 11.1 29.3 10.9 36.2h37.6c-.2-6.9 13.3-26.6 10.9-36.2\" fill=\"#c28fef\"/><path d=\"m15.3 33.5c0 0-3.4-.9-3.4-5.8 0-3.9 2.5-4.6 2.5-4.6 9.1 0 25.9-11.7 25.9-11.7s3.5 10.3 8.9 11.7c0 0 2.6.5 2.6 4.6 0 4.9-3.5 5.8-3.5 5.8 0 7.4-10.3 16.4-16.5 16.4-6 0-16.5-8.9-16.5-16.4\" fill=\"#ffdd67\"/><path d=\"m32 38.4c-3.2 0-4.8-2.3-3.2-2.3s4.8 0 6.4 0c1.6 0 0 2.3-3.2 2.3\" fill=\"#eba352\"/><path d=\"m36.8 31.6c-1.1 0-1-1.4-1-1.4 1.7-8.8 11.5-4.1 11.5-4.1.8 1.5-1.8 5.3-2.5 5.5-3.1.8-8 0-8 0\" fill=\"#f5f5f5\"/><circle cx=\"41.2\" cy=\"28\" r=\"3.5\" fill=\"#664e27\"/><circle cx=\"41.2\" cy=\"28\" r=\"1.2\" fill=\"#2b2925\"/><path d=\"m35.8 30.2c1.7-10.5 10.4-3.5 13.8-5-3.4 3.2-10.3-3.8-13.8 5\" fill=\"#3b3226\"/><path d=\"m27.2 31.6c1.1 0 1-1.4 1-1.4-1.7-8.8-11.5-4.1-11.5-4.1-.8 1.5 1.8 5.3 2.5 5.5 3.1.8 8 0 8 0\" fill=\"#f5f5f5\"/><circle cx=\"22.8\" cy=\"28\" r=\"3.5\" fill=\"#664e27\"/><path d=\"m28.1 30.2c-1.7-10.5-10.4-3.5-13.8-5 3.5 3.2 10.4-3.8 13.8 5\" fill=\"#3b3226\"/><path d=\"m32 42.2l-7.7-1.2c4.6 6.6 10.8 6.6 15.4 0l-7.7 1.2\" fill=\"#f09985\"/><path d=\"m35 40.8c-2.3-.9-3 .5-3 .5s-.8-1.4-3-.5c-1.8.7-4.7.3-4.7.3 3.1.7 3.5 1.9 7.7 1.9 4.2 0 4.6-1.3 7.7-1.9 0 0-2.9.4-4.7-.3\" fill=\"#d47f6c\"/><circle cx=\"22.8\" cy=\"28\" r=\"1.2\" fill=\"#2b2925\"/></svg>";
	protected static String innocent="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><path d=\"M61,33c0,16-13,29-29,29C16,62,3,49,3,33C3,17,16,4,32,4C48,4,61,17,61,33z\" fill=\"#ffdd67\"/><g fill=\"#664e27\"><path d=\"m28.6 34.4c-1.8-4.9-4.5-7.4-7.2-7.4-2.7 0-5.4 2.5-7.2 7.4-.2.5.7 1.4 1.2.9 1.7-1.8 3.8-2.6 6-2.6 2.2 0 4.3.7 6 2.6.5.5 1.4-.4 1.2-.9\"/><path d=\"m49.8 34.4c-1.8-4.9-4.5-7.4-7.2-7.4-2.7 0-5.4 2.5-7.2 7.4-.2.5.7 1.4 1.2.9 1.7-1.8 3.8-2.6 6-2.6 2.2 0 4.3.7 6 2.6.4.5 1.4-.4 1.2-.9\"/><path d=\"m44.2 42.2c-7.8 5.5-16.5 5.4-24.3 0-.9-.7-1.8.5-1.1 1.5 2.4 3.9 7.2 7.4 13.3 7.4 6.1 0 10.9-3.5 13.3-7.4.5-1-.3-2.1-1.2-1.5\"/></g><path d=\"m54.3 7.2c-.5-4.2-8.4-6.4-25.8-4.6-16.3 1.8-24.6 5.7-24.2 9.8.7 6.4 12.2 8.6 26 7.1 13.8-1.4 24.6-5.9 24-12.3m-24.7 5.4c-9.2 1-16.9.4-17-1.2-.1-.6.9-1.2 2.5-1.9 4.7-3.4 10.6-5.5 16.9-5.5 4.5 0 8.8 1 12.6 2.9.9.3 1.4.6 1.4 1 .2 1.5-7.2 3.7-16.4 4.7\" fill=\"#4aa9ff\"/></svg>";
	protected static String sunglasses="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><path d=\"M32,2c16.6,0,30,13.4,30,30S48.6,62,32,62C15.4,62,2,48.6,2,32S15.4,2,32,2\" fill=\"#ffdd67\"/><path d=\"m35.8 20.5c-2.2 1.1-5.5 1.1-7.7 0-2.3-1.2-5.2-2-8.7-2.3-3.4-.3-10.5-.3-14 1-.4.1-.8.3-1.2.5-.1.1-.2.2-.2.6v.5c0 1-.1.6.6 1 1.4.8 2.2 2.9 2.6 5.8.6 4.2 2.7 6.9 6 8.1 3.1 1.2 6.6 1.1 9.7-.1 1.7-.7 3.2-1.7 4.4-3.5 2.1-3 1.4-4.9 2.5-7.5.9-2.3 3.5-2.3 4.5 0 1.1 2.6.4 4.5 2.5 7.5 1.2 1.7 2.7 2.8 4.4 3.5 3.1 1.2 6.6 1.3 9.7.1 3.4-1.3 5.4-3.9 6-8.1.4-2.9 1.2-5 2.6-5.8.7-.4.6 0 .6-1v-.5c0-.4 0-.5-.3-.6-.4-.2-.8-.4-1.2-.5-3.6-1.3-10.7-1.3-14-1-3.5.3-6.4 1.1-8.8 2.3\" fill=\"#494949\"/><path d=\"m44.6 42.3c-8.1 5.7-17.1 5.6-25.2 0-1-.7-1.8.5-1.2 1.6 2.5 4 7.4 7.7 13.8 7.7s11.3-3.6 13.8-7.7c.6-1.1-.2-2.3-1.2-1.6\" fill=\"#664e27\"/></svg>";
	protected static String expressionless="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><g fill=\"#664e27\"><path d=\"m40 48h-16c-1.5 0-1.5-4 0-4h16c1.5 0 1.5 4 0 4\"/><path d=\"m27.1 32h-16c-1.5 0-1.5-4 0-4h16c1.5 0 1.5 4 0 4\"/><path d=\"m52.9 32h-16c-1.5 0-1.5-4 0-4h16c1.5 0 1.5 4 0 4\"/></g></svg>";
	protected static String confused="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><path d=\"m2.5 37.2c2.9 16.3 18.4 27.2 34.8 24.3 16.3-2.9 27.2-18.4 24.3-34.8-2.9-16.2-18.5-27.1-34.8-24.2-16.3 2.8-27.2 18.4-24.3 34.7\" fill=\"#ffdd67\"/><g fill=\"#664e27\"><circle cx=\"42.4\" cy=\"24.7\" r=\"5\"/><circle cx=\"19.7\" cy=\"28.7\" r=\"5\"/><path d=\"m43.3 41.8c-5.8-1.5-12-.4-16.9 3-1.2.9 1.1 4 2.3 3.2 3.2-2.3 8.4-3.8 13.7-2.4 1.3.3 2.4-3.3.9-3.8\"/></g></svg>";
	protected static String stuckouttongue="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><path d=\"m47.9 38c-3.3 0-9.7 0-15.9 0s-12.6 0-15.9 0c-.7 0-1.1.5-1.1 1 0 7.3 6 15 17 15s17-7.7 17-15c0-.5-.4-1-1.1-1\" fill=\"#664e27\"/><path d=\"m41.2 44c-2.3 0-9.2 0-9.2 0s-6.9 0-9.2 0c-.7 0-.8.3-.8.8 0 .9 0 2.4 0 4 0 8.8 4.5 13.2 10 13.2 5.5 0 10-4.4 10-13.2 0-1.6 0-3.1 0-4 0-.5-.1-.8-.8-.8\" fill=\"#ff717f\"/><path fill=\"#e2596c\" d=\"M33.5 44 32 57.8 30.5 44z\"/><g fill=\"#664e27\"><circle cx=\"20.5\" cy=\"24.5\" r=\"5\"/><circle cx=\"43.5\" cy=\"24.5\" r=\"5\"/></g></svg>";
	protected static String openmouth="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><g fill=\"#664e27\"><circle cx=\"32\" cy=\"45.1\" r=\"7\"/><circle cx=\"20.2\" cy=\"25\" r=\"4.5\"/><circle cx=\"42.7\" cy=\"25\" r=\"4.5\"/></g></svg>";
	protected static String nomouth="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" style=\"width: 18px;height: 18px;vertical-align: middle;\"><circle cx=\"32\" cy=\"32\" r=\"30\" fill=\"#ffdd67\"/><g fill=\"#664e27\"><circle cx=\"20.5\" cy=\"28.5\" r=\"5\"/><circle cx=\"43.5\" cy=\"28.5\" r=\"5\"/></g></svg>";
	
	public static final String PRINT_SIZE_A3="A3";
	public static final String PRINT_SIZE_A4="A4";
	public static final String PRINT_SIZE_A5="A5";
	public static final String PRINT_SIZE_LETTER="Letter";
	
	protected static Map<String,B4JS> B4JSClasses = new LinkedHashMap<String,B4JS>();
	
	public static final String B4JS_PARAM_INPUTKEY="B4JS#!#KEY";
	public static final String B4JS_PARAM_INPUTKEYCODE="B4JS#!#KCODE";
	public static final String B4JS_PARAM_ACTIONMAINBUTTON="B4JS#!#AB";
	public static final String B4JS_PARAM_ACTIONSUBBUTTON="B4JS#!#ASB";
	public static final String B4JS_PARAM_RANGESTART="B4JS#!#RSTART";
	public static final String B4JS_PARAM_RANGESTOP="B4JS#!#RSTOP";
	public static final String B4JS_PARAM_SLIDERVALUE="B4JS#!#SVALUE";
	public static final String B4JS_PARAM_TABSRETURNNAME="B4JS#!#TABSRN";
	
	public static boolean RightToLeft=false;
		
	@Hide
	public static  Map<String,String> GlobalVars = new LinkedHashMap<String,String>();
	
	public static String BROADCAST_JOINED = "abmbroadcastjoined";
	public static String BROADCAST_LEFT = "abmbroadcastleft";
	
	public static boolean InputMaskNewVersion=false; 
		
	public static final String PRINT_PAGEBREAK_AFTER_AUTO = " pba-auto ";
	public static final String PRINT_PAGEBREAK_AFTER_ALWAYS = " pba-always ";
	public static final String PRINT_PAGEBREAK_AFTER_AVOID = " pba-avoid ";
	public static final String PRINT_PAGEBREAK_AFTER_LEFT = " pba-left ";
	public static final String PRINT_PAGEBREAK_AFTER_RIGHT = " pba-right ";
	public static final String PRINT_PAGEBREAK_AFTER_INITIAL = " pba-initial ";
	public static final String PRINT_PAGEBREAK_AFTER_INHERIT = " pba-inherit ";
	
	public static final String PRINT_PAGEBREAK_BEFORE_AUTO = " pbb-auto ";
	public static final String PRINT_PAGEBREAK_BEFORE_ALWAYS = " pbb-always ";
	public static final String PRINT_PAGEBREAK_BEFORE_AVOID = " pbb-avoid ";
	public static final String PRINT_PAGEBREAK_BEFORE_LEFT = " pbb-left ";
	public static final String PRINT_PAGEBREAK_BEFORE_RIGHT = " pbb-right ";
	public static final String PRINT_PAGEBREAK_BEFORE_INITIAL = " pbb-initial ";
	public static final String PRINT_PAGEBREAK_BEFORE_INHERIT = " pbb-inherit ";
	
	public static final String PRINT_PAGEBREAK_INSIDE_AUTO = " pbi-auto ";
	public static final String PRINT_PAGEBREAK_INSIDE_AVOID = " pbi-avoid ";
	public static final String PRINT_PAGEBREAK_INSIDE_INITIAL = " pbi-initial ";
	public static final String PRINT_PAGEBREAK_INSIDE_INHERIT = " pbi-inherit ";
	
	public static final String SECTION_BUTTONTYPE_UP="up";
	public static final String SECTION_BUTTONTYPE_DOWN="down";
	
	public static final String CONTAINERIMAGE_REPEAT_REPEAT="repeat";
	public static final String CONTAINERIMAGE_REPEAT_REPEAT_X="repeat-x";
	public static final String CONTAINERIMAGE_REPEAT_REPEAT_Y="repeat-y";
	public static final String CONTAINERIMAGE_REPEAT_NOREPEAT="no-repeat";
	
	public static final String CONTAINERIMAGE_POSITION_LEFTTOP="left top";
	public static final String CONTAINERIMAGE_POSITION_LEFTCENTER="left center";
	public static final String CONTAINERIMAGE_POSITION_LEFTBOTTOM="left bottom";
	public static final String CONTAINERIMAGE_POSITION_RIGHTTOP="right top";
	public static final String CONTAINERIMAGE_POSITION_RIGHTCENTER="right center";
	public static final String CONTAINERIMAGE_POSITION_RIGHTBOTTOM="right bottom";
	public static final String CONTAINERIMAGE_POSITION_CENTERTOP="center top";
	public static final String CONTAINERIMAGE_POSITION_CENTERCENTER="center center";
	public static final String CONTAINERIMAGE_POSITION_CENTERBOTTOM="center bottom";
	public static final String CONTAINERIMAGE_POSITION_COVER="cover";
	
	public static final String HALFBUTTON_LEFT="left";
	public static final String HALFBUTTON_RIGHT="right";
	public static final String HALFBUTTON_CENTER="center";
		
	public static final String PIVOT_TABLETYPE_TABLE="Table";
	public static final String PIVOT_TABLETYPE_TABLEBARCHART="Table Barchart";
	public static final String PIVOT_TABLETYPE_HEATMAP="Heatmap";
	public static final String PIVOT_TABLETYPE_ROWHEATMAP="Row Heatmap";
	public static final String PIVOT_TABLETYPE_COLHEATMAP="Col Heatmap";
	protected static final String PIVOT_TABLETYPE_TABLEST="Table With Subtotal";
	protected static final String PIVOT_TABLETYPE_TABLEBARCHARTST= "Table With Subtotal Bar Chart";
	protected static final String PIVOT_TABLETYPE_HEATMAPST="Table With Subtotal Heatmap";
	protected static final String PIVOT_TABLETYPE_ROWHEATMAPST="Table With Subtotal Row Heatmap";
	protected static final String PIVOT_TABLETYPE_COLHEATMAPST= "Table With Subtotal Col Heatmap";
	public static final String PIVOT_TABLETYPE_GOOGLE_LINECHART="Line Chart";
	public static final String PIVOT_TABLETYPE_GOOGLE_BARCHART= "Bar Chart";
	public static final String PIVOT_TABLETYPE_GOOGLE_STACKEDBARCHART="Stacked Bar Chart";
	public static final String PIVOT_TABLETYPE_GOOGLE_AREACHART="Area Chart";
	public static final String PIVOT_TABLETYPE_GOOGLE_SCATTERCHART="Scatter Chart";		
	
	public static final String INDICATOR_RIGHT="indright";
	public static final String INDICATOR_LEFT="indleft";
	public static final String INDICATOR_LEFT_ON_DESKTOP_RIGHT_ON_BELOW="indleftright";
	
	public static final String PIVOT_AGGRTYPE_COUNT="Count";
	public static final String PIVOT_AGGRTYPE_COUNTUNIQUEVALUES="Count Unique Values";
	public static final String PIVOT_AGGRTYPE_LISTUNIQUEVALUES="List Unique Values";
	public static final String PIVOT_AGGRTYPE_SUM="Sum";
	public static final String PIVOT_AGGRTYPE_INTEGERSIM="Integer Sum";
	public static final String PIVOT_AGGRTYPE_AVERAGE="Average";
	public static final String PIVOT_AGGRTYPE_MINIMUM="Minimum";
	public static final String PIVOT_AGGRTYPE_MAXIMUM= "Maximum";
	public static final String PIVOT_AGGRTYPE_FIRST="First";
	public static final String PIVOT_AGGRTYPE_LAST="Last";
	public static final String PIVOT_AGGRTYPE_SUMOVERSUM="Sum over Sum";
	public static final String PIVOT_AGGRTYPE_80UPPERBOUND="80% Upper Bound";
	public static final String PIVOT_AGGRTYPE_80LOWERBOUND="80% Lower Bound";
	public static final String PIVOT_AGGRTYPE_SUMASFRACTIONTOTAL="Sum as Fraction of Total";
	public static final String PIVOT_AGGRTYPE_SUMASFRACTIONROWS="Sum as Fraction of Rows";
	public static final String PIVOT_AGGRTYPE_SUMASFRACTIONCOLS="Sum as Fraction of Columns";
	public static final String PIVOT_AGGRTYPE_COUNTASFRACTIONTOTAL="Count as Fraction of Total";
	public static final String PIVOT_AGGRTYPE_COUNTASFRACTIONROWS="Count as Fraction of Rows";
	public static final String PIVOT_AGGRTYPE_COUNTASFRACTIONCOLS="Count as Fraction of Columns";
	
	public static final String ACTIONBUTTON_DIRECTION_UP="up";
	public static final String ACTIONBUTTON_DIRECTION_DOWN="down";
	public static final String ACTIONBUTTON_DIRECTION_LEFT="left";
	public static final String ACTIONBUTTON_DIRECTION_RIGHT="right";
	
	public static final String ACTIONBUTTON_POSITION_TOPLEFT="topleft";
	public static final String ACTIONBUTTON_POSITION_TOPRIGHT="topright";
	public static final String ACTIONBUTTON_POSITION_BOTTOMLEFT="bottomleft";
	public static final String ACTIONBUTTON_POSITION_BOTTOMRIGHT="bottomright";
	
	public static final String NATIVE_PLATFORM_B4J="b4j";
	public static final String NATIVE_PLATFORM_B4A="b4a";
	public static final String NATIVE_PLATFORM_B4i="b4i";
	public static final String NATIVE_PLATFORM_NONE="";
	
	public static final String ICONBADGE_POSITION_TOPLEFT="iosb-top-left";
	public static final String ICONBADGE_POSITION_TOPRIGHT="iosb-top-right";
	public static final String ICONBADGE_POSITION_BOTTOMLEFT="iosb-bottom-left";
	public static final String ICONBADGE_POSITION_BOTTOMRIGHT="iosb-bottom-right";
		
	protected static final String VALIDSTRING="VALID_TRUE:VALID_FALSE:VALID_UNDETERMINED";
	public static final String VALID_TRUE="valid";
	public static final String VALID_FALSE="invalid";
	public static final String VALID_UNDETERMINED="";
	
	public static final long NOW=Long.MAX_VALUE;
	public static final String NOWISO="9223372036854775807";
	
	protected static final String LOADERSTRING="LOADER_TYPE_AUTO:LOADER_TYPE_MANUAL";
	public static final int LOADER_TYPE_AUTO=0;
	public static final int LOADER_TYPE_MANUAL=1;
	
	protected static boolean IsFirstRunning=true;
	
	public static final String PAGE_INLINE_USEAPPSETTING="APPSETTING";
	public static final String PAGE_INLINE_FALSE="FALSE";
	public static final String PAGE_INLINE_TRUE="TRUE";
	
	public static final String FLOATING_FROMTOP="top";
	public static final String FLOATING_FROMBOTTOM="bottom";
	
	protected static final String AUTOCOMPLETESTRING = "AUTOCOMPLETE_CONTAINS:AUTOCOMPLETE_STARTS:AUTOCOMPLETE_GOOGLE";
	public static final String AUTOCOMPLETE_CONTAINS="contains";
	public static final String AUTOCOMPLETE_STARTS="starts";
	public static final String AUTOCOMPLETE_GOOGLE="google";
	
	protected static final String CURSORSTRING="CURSOR_DEFAULT:CURSOR_POINTING:CURSOR_WAITING:CURSOR_LOADING:CURSOR_MOVING:CURSOR_H_RESIZING:CURSOR_V_RESIZING:CURSOR_TYPING:CURSOR_ZOOM_IN:CURSOR_ZOOM_OUT:CURSOR_CELL:CURSOR_COL_RESIZING:CURSOR_NONE:CURSOR_DENY";
	public static final String CURSOR_DEFAULT="cursor-default";
	public static final String CURSOR_POINTING="cursor-pointing";
	public static final String CURSOR_WAITING="cursor-waiting";
	public static final String CURSOR_LOADING="cursor-loading";
	public static final String CURSOR_MOVING="cursor-moving";
	public static final String CURSOR_H_RESIZING="cursor-h-resizing";
	public static final String CURSOR_V_RESIZING="cursor-v-resizing";
	public static final String CURSOR_TYPING="cursor-typing";
	public static final String CURSOR_ZOOM_IN="cursor-zoom-in";
	public static final String CURSOR_ZOOM_OUT="cursor-zoom-out";
	public static final String CURSOR_CELL="cursor-cell";
	public static final String CURSOR_COL_RESIZING="cursor-col-resizing";
	public static final String CURSOR_NONE="cursor-none";
	public static final String CURSOR_DENY="cursor-deny";	
	
	public static final String SMARTWIZARD_STATE_ACTIVE="active";
	public static final String SMARTWIZARD_STATE_HIDDEN="hidden hide";
	public static final String SMARTWIZARD_STATE_DISABLED="disabled";
	public static final String SMARTWIZARD_STATE_ERROR="error";
	public static final String SMARTWIZARD_STATE_DONE="done";
	
	public static final String SMARTWIZARD_RESPONSIVE_OVERUNDER="overunder";
	public static final String SMARTWIZARD_RESPONSIVE_USEALTICON="alticon";
	
	public static final int SVGELEMENT_UNDEFINED=0;
	public static final int SVGELEMENT_CIRCLE=1;
	public static final int SVGELEMENT_GROUP=2;
	public static final int SVGELEMENT_ELLIPSE=3;
	public static final int SVGELEMENT_IMAGE=4;
	public static final int SVGELEMENT_LINE=5;
	public static final int SVGELEMENT_PATH=6;
	public static final int SVGELEMENT_POLYLINE=7;
	public static final int SVGELEMENT_POLYGON=8;
	public static final int SVGELEMENT_RECT=9;
	public static final int SVGELEMENT_TEXT=10;
	public static final int SVGELEMENT_TEXTARRAY=11;
	public static final int SVGELEMENT_ARC=12;
	
	protected static final String CONTAINERBORDERSTRING="CONTAINERBORDER_DEFAULT:CONTAINERBORDER_DASHED:CONTAINERBORDER_DOTTED:CONTAINERBORDER_DOUBLE:CONTAINERBORDER_GROOVE:CONTAINERBORDER_HIDDEN:CONTAINERBORDER_INSET:CONTAINERBORDER_NONE:CONTAINERBORDER_OUTSET:CONTAINERBORDER_RIDGE:CONTAINERBORDER_SOLID:";
	public static final String CONTAINERBORDER_DEFAULT="";
	public static final String CONTAINERBORDER_DASHED="dashed";
	public static final String CONTAINERBORDER_DOTTED="dotted";
	public static final String CONTAINERBORDER_DOUBLE="double";
	public static final String CONTAINERBORDER_GROOVE="groove";
	public static final String CONTAINERBORDER_HIDDEN="hidden";
	public static final String CONTAINERBORDER_INSET="inset";
	public static final String CONTAINERBORDER_NONE="none";
	public static final String CONTAINERBORDER_OUTSET="outset";
	public static final String CONTAINERBORDER_RIDGE="ridge";
	public static final String CONTAINERBORDER_SOLID="solid";
	
	public static final String BORDER_DEFAULT="";
	public static final String BORDER_DASHED="dashed";
	public static final String BORDER_DOTTED="dotted";
	public static final String BORDER_DOUBLE="double";
	public static final String BORDER_GROOVE="groove";
	public static final String BORDER_HIDDEN="hidden";
	public static final String BORDER_INSET="inset";
	public static final String BORDER_NONE="none";
	public static final String BORDER_OUTSET="outset";
	public static final String BORDER_RIDGE="ridge";
	public static final String BORDER_SOLID="solid";
	
	public static final String PAGE_OPENGRAPH_TITLE="og:title"; 
	public static final String PAGE_OPENGRAPH_URL="og:url";
	public static final String PAGE_OPENGRAPH_IMAGE="og:image";
	public static final String PAGE_OPENGRAPH_IMAGEWIDTH="og:image:width";
	public static final String PAGE_OPENGRAPH_IMAGEHEIGHT="og:image:height";
	public static final String PAGE_OPENGRAPH_DESCRIPTION="og:description";
	public static final String PAGE_OPENGRAPH_SITENAME="og:site_name";
	public static final String PAGE_OPENGRAPH_TYPE="og:type";
	public static final String PAGE_OPENGRAPH_LOCALE="og:locale";
	public static final String PAGE_OPENGRAPH_EMAIL="og:email";
	public static final String PAGE_OPENGRAPH_PHONENUMBER="og:phone_number";
	public static final String PAGE_OPENGRAPH_FAXNUMBER="og:fax_number";
	public static final String PAGE_OPENGRAPH_LATITUDE="og:latitude";
	public static final String PAGE_OPENGRAPH_LONGITUDE="og:longitude";
	public static final String PAGE_OPENGRAPH_STREETADDRESS="og:street_address";
	public static final String PAGE_OPENGRAPH_LOCALITY="og:locality";
	public static final String PAGE_OPENGRAPH_REGION="og:region";
	public static final String PAGE_OPENGRAPH_POSTALCODE="og:postal_code";
	public static final String PAGE_OPENGRAPH_COUNTRYNAME="og:country_name";
	
	public static final String PAGE_TWITTER_CARD="twitter:card"; // summary, photo, player
	public static final String PAGE_TWITTER_SITE="twitter:site";
	public static final String PAGE_TWITTER_SITEID="twitter:site:id";
	public static final String PAGE_TWITTER_CREATOR="twitter:creator";
	public static final String PAGE_TWITTER_CREATORID="twitter:creator:id";
	public static final String PAGE_TWITTER_URL="twitter:url";
	public static final String PAGE_TWITTER_DESCRIPTION="twitter:description";
	public static final String PAGE_TWITTER_TITLE="twitter:title";
	public static final String PAGE_TWITTER_IMAGE="twitter:image";
	public static final String PAGE_TWITTER_IMAGEWIDTH="twitter:image:width";
	public static final String PAGE_TWITTER_IMAGEHEIGHT="twitter:image:height";
	public static final String PAGE_TWITTER_PLAYER="twitter:player";
	public static final String PAGE_TWITTER_PLAYERWIDTH="twitter:player:width";
	public static final String PAGE_TWITTER_PLAYERHEIGHT="twitter:player:height";
	public static final String PAGE_TWITTER_STREAM="twitter:player:stream";
	
	public static boolean AppDefaultPageCSSInline=false;
	public static boolean AppDefaultPageJSInline=false;
	
	public static final String TWEEN_LINEAR="linear";
	public static final String TWEEN_SWING="swing";
	public static final String TWEEN_SPRING="spring";
	public static final String TWEEN_EASE="ease";
	public static final String TWEEN_EASEIN="ease-in";
	public static final String TWEEN_EASEOUT="ease-out";
	public static final String TWEEN_EASEINOUT="ease-in-out";
	public static final String TWEEN_EASEINSINE="easeInSine";
	public static final String TWEEN_EASEOUTSINE="easeOutSine";
	public static final String TWEEN_EASEINOUTSINE="easeInOutSine";
	public static final String TWEEN_EASEINQUAD="easeInQuad";
	public static final String TWEEN_EASEOUTQUAD="easeOutQuad";
	public static final String TWEEN_EASEINOUTQUAD="easeInOutQuad";
	public static final String TWEEN_EASEINCUBIC="easeInCubic";
	public static final String TWEEN_EASEOUTCUBIC="easeOutCubic";
	public static final String TWEEN_EASEINOUTCUBIC="easeInOutCubic";
	public static final String TWEEN_EASEINQUART="easeInQuart";
	public static final String TWEEN_EASEOUTQUART="easeOutQuart";
	public static final String TWEEN_EASEINOUTQUART="easeInOutQuart";
	public static final String TWEEN_EASEINQUINT="easeInQuint";
	public static final String TWEEN_EASEOUTQUINT="easeOutQuint";
	public static final String TWEEN_EASEINOUTQUINT="easeInOutQuint";
	public static final String TWEEN_EASEINEXPO="easeInExpo";
	public static final String TWEEN_EASEOUTEXPO="easeOutExpo";
	public static final String TWEEN_EASEINOUTEXPO="easeInOutExpo";
	public static final String TWEEN_EASEINCIRC="easeInCirc";
	public static final String TWEEN_EASEOUTCIRC="easeOutCirc";
	public static final String TWEEN_EASEINOUTCIRC="easeInOutCirc";
	
	public static final String SIDEBAR_MANUAL_ALWAYSHIDE="alwayshide";
	public static final String SIDEBAR_MANUAL_HIDEMEDIUMSMALL="mediumsmallhide";
	public static final String SIDEBAR_AUTO="auto";
	
	public static final String PDF_READDIRECTION_LTR="ltr";
	public static final String PDF_READDIRECTION_RTL="rtl";
		
	public static final int GEN_NONE=-1;
	public static final int GEN_TEXT=0;
	public static final int GEN_DOUBLE=1;
	public static final int GEN_TEXTAREA=2;
	public static final int GEN_COMBOSQL=3;
	public static final int GEN_COMBOLIST=4;
	public static final int GEN_CHECKBOX=5;
	public static final int GEN_SWITCH=6;
	public static final int GEN_DATE_SCROLL=7;
	public static final int GEN_TIME_SCROLL=8;
	public static final int GEN_DATETIME_SCROLL=9;
	public static final int GEN_DATE_PICK=10;
	public static final int GEN_TIME_PICK=11;
	public static final int GEN_DATETIME_PICK=12;
	public static final int GEN_INTEGER=13;
	
	public static final String BUTTONSIZE_SMALL="small";
	public static final String BUTTONSIZE_NORMAL="normal";
	public static final String BUTTONSIZE_LARGE="large";
	
	public static final String SITEMAP_FREQ_NONE="none";
	public static final String SITEMAP_FREQ_ALWAYS="always";
	public static final String SITEMAP_FREQ_HOURLY="hourly";
	public static final String SITEMAP_FREQ_DAILY="daily";
	public static final String SITEMAP_FREQ_WEEKLY="weekly";
	public static final String SITEMAP_FREQ_MONTHLY="monthly";
	public static final String SITEMAP_FREQ_YEARLY="yearly";
	public static final String SITEMAP_FREQ_NEVER="never";	
	
	public static final int PLANNER_STATUS_FREE_AVAILABLE=0;
	public static final int PLANNER_STATUS_FREE_NOTAVAILABLE=1;
	public static final int PLANNER_STATUS_USED_AVAILABLE=2;
	public static final int PLANNER_STATUS_USED_NOTAVAILABLE=3;
	
	public static final String PLANNER_MENUTYPE_DAY="day";
	public static final String PLANNER_MENUTYPE_HOUR="hour";
	public static final String PLANNER_MENUTYPE_MIN="min";
	
	public static final int PLANNER_MENU_SETFREE=1;
	public static final int PLANNER_MENU_SETNOTAVAILABLE=2;
	public static final int PLANNER_MENU_CUT=6;
	public static final int PLANNER_MENU_COPY=5;
	public static final int PLANNER_MENU_PASTE=4;
	public static final int PLANNER_MENU_DELETE=3;
	
	protected static final String COLORSTRING="COLOR_AMBER:COLOR_BLACK:COLOR_BLUE:COLOR_BLUEGREY:COLOR_BROWN:COLOR_CYAN:COLOR_DEEPORANGE:COLOR_DEEPPURPLE:COLOR_GREEN:COLOR_GREY:COLOR_INDIGO:COLOR_LIGHTBLUE:COLOR_LIGHTGREEN:COLOR_LIME:COLOR_ORANGE:COLOR_PINK:COLOR_PURPLE:COLOR_RED:COLOR_TEAL:COLOR_TRANSPARENT:COLOR_WHITE:COLOR_YELLOW";
	public static final String COLOR_AMBER= "amber";
	public static final String COLOR_BLACK="black";
	public static final String COLOR_BLUE="blue";
	public static final String COLOR_BLUEGREY="blue-grey";
	public static final String COLOR_BROWN="brown";
	public static final String COLOR_CYAN="cyan";
	public static final String COLOR_DEEPORANGE="deep-orange";
	public static final String COLOR_DEEPPURPLE="deep-purple";
	public static final String COLOR_GREEN="green";
	public static final String COLOR_GREY="grey";
	public static final String COLOR_INDIGO="indigo";
	public static final String COLOR_LIGHTBLUE="light-blue";
	public static final String COLOR_LIGHTGREEN="light-green";
	public static final String COLOR_LIME="lime";
	public static final String COLOR_ORANGE="orange";
	public static final String COLOR_PINK="pink";
	public static final String COLOR_PURPLE="purple";
	public static final String COLOR_RED="red";
	public static final String COLOR_TEAL="teal";
	public static final String COLOR_TRANSPARENT="transparent";
	public static final String COLOR_WHITE="white";
	public static final String COLOR_YELLOW="yellow";
	
	protected static final String INTENSITYSTRING="INTENSITY_NORMAL:INTENSITY_LIGHTEN5:INTENSITY_LIGHTEN4:INTENSITY_LIGHTEN3:INTENSITY_LIGHTEN2:INTENSITY_LIGHTEN1:INTENSITY_DARKEN1:INTENSITY_DARKEN2:INTENSITY_DARKEN3:INTENSITY_DARKEN4:INTENSITY_ACCENT1:INTENSITY_ACCENT2:INTENSITY_ACCENT3:INTENSITY_ACCENT4";
	public static final String INTENSITY_NORMAL="";
	public static final String INTENSITY_LIGHTEN5="lighten-5";
	public static final String INTENSITY_LIGHTEN4="lighten-4";
	public static final String INTENSITY_LIGHTEN3="lighten-3";
	public static final String INTENSITY_LIGHTEN2="lighten-2";
	public static final String INTENSITY_LIGHTEN1="lighten-1";
	public static final String INTENSITY_DARKEN1="darken-1";
	public static final String INTENSITY_DARKEN2="darken-2";
	public static final String INTENSITY_DARKEN3="darken-3";
	public static final String INTENSITY_DARKEN4="darken-4";
	public static final String INTENSITY_ACCENT1="accent-1";
	public static final String INTENSITY_ACCENT2="accent-2";
	public static final String INTENSITY_ACCENT3="accent-3";
	public static final String INTENSITY_ACCENT4="accent-4";
	
	public static final String GOOGLEMAPTYPE_HYBRID="hybrid";
	public static final String GOOGLEMAPTYPE_ROADMAP="roadmap";
	public static final String GOOGLEMAPTYPE_SATELLITE="satellite";
	public static final String GOOGLEMAPTYPE_TERRAIN="terrain";

	public static final String GOOGLEMAP_TRAVELMODE_DRIVING="driving";
	public static final String GOOGLEMAP_TRAVELMODE_WALKING="walking";
	public static final String GOOGLEMAP_TRAVELMODE_TRANSIT="transit";
	public static final String GOOGLEMAP_TRAVELMODE_BICYCLING="bicycling";	
	
	public static final String GOOGLE_AUTOCOMLETE_RESULTTYPE_STREETNUMBER="street_number";
	public static final String GOOGLE_AUTOCOMLETE_RESULTTYPE_STREETNAME="route";
	public static final String GOOGLE_AUTOCOMLETE_RESULTTYPE_CITY="locality";	
	public static final String GOOGLE_AUTOCOMLETE_RESULTTYPE_STATE="administrative_area_level_1";
	public static final String GOOGLE_AUTOCOMLETE_RESULTTYPE_COUNTRY="country";
	public static final String GOOGLE_AUTOCOMLETE_RESULTTYPE_POSTALCODE="postal_code";
	public static final String GOOGLE_AUTOCOMLETE_RESULTTYPE_LOCATION="location";
		 
	public static final String VISIBILITY_ALL="";
	public static final String VISIBILITY_HIDE_ALL="hide";
	public static final String VISIBILITY_HIDE_ON_SMALL_ONLY="hide-on-small-only";
	public static final String VISIBILITY_HIDE_ON_MEDIUM_ONLY="hide-on-med-only";
	public static final String VISIBILITY_HIDE_ON_LARGE_ONLY="hide-on-large-only";
	public static final String VISIBILITY_HIDE_ON_EXTRALARGE_ONLY="hide-on-extralarge-only";//
	
	public static final String VISIBILITY_HIDE_ON_MEDIUM_AND_BELOW="hide-on-med-and-down";
	public static final String VISIBILITY_HIDE_ON_MEDIUM_AND_ABOVE="hide-on-med-and-up";	
	public static final String VISIBILITY_HIDE_ON_LARGE_AND_BELOW="hide-on-large-and-down"; //
	public static final String VISIBILITY_HIDE_ON_LARGE_AND_ABOVE="hide-on-large-and-up"; //
		
	public static final String VISIBILITY_SHOW_ON_SMALL_ONLY="show-on-small";
	public static final String VISIBILITY_SHOW_ON_MEDIUM_ONLY="show-on-medium";
	public static final String VISIBILITY_SHOW_ON_LARGE_ONLY="show-on-large";
	public static final String VISIBILITY_SHOW_ON_EXTRALARGE_ONLY="show-on-extralarge"; //
		
	public static final String VISIBILITY_SHOW_ON_MEDIUM_AND_ABOVE="show-on-medium-and-up";
	public static final String VISIBILITY_SHOW_ON_MEDIUM_AND_BELOW="show-on-medium-and-down";	
	public static final String VISIBILITY_SHOW_ON_LARGE_AND_ABOVE="show-on-large-and-up"; //
	public static final String VISIBILITY_SHOW_ON_LARGE_AND_BELOW="show-on-large-and-down"; //
	
	public static final String WAVESEFFECT_NONE="";
	public static final String WAVESEFFECT_DEFAULT="waves-effect";
	public static final String WAVESEFFECT_BLOCK="waves-effect waves-block";
	public static final String WAVESEFFECT_LIGHT="waves-effect waves-light";
	public static final String WAVESEFFECT_RED="waves-effect waves-red";
	public static final String WAVESEFFECT_YELLOW="waves-effect waves-yellow";
	public static final String WAVESEFFECT_ORANGE="waves-effect waves-orange";
	public static final String WAVESEFFECT_PURPLE="waves-effect waves-purple";
	public static final String WAVESEFFECT_GREEN="waves-effect waves-green";
	public static final String WAVESEFFECT_TEAL="waves-effect waves-teal";
	public static final String WAVESEFFECT_CUSTOM="waves-effect waves-";
	
	public static final String ITEMLIST_TYPE_DISC="disc";
	public static final String ITEMLIST_TYPE_CIRCLE="circle";
	public static final String ITEMLIST_TYPE_SQUARE="square";
	public static final String ITEMLIST_TYPE_NONE="";
	public static final String ITEMLIST_TYPE_NUMBERS="1";
	public static final String ITEMLIST_TYPE_ALPHABET_LOWERCASE="a";
	public static final String ITEMLIST_TYPE_ALPHABET_UPPERCASE="A";
	public static final String ITEMLIST_TYPE_ROMAN_LOWERCASE="i";
	public static final String ITEMLIST_TYPE_ROMAN_UPPERCASE="I";
	
	protected static final String SIZESTRING = "SIZE_H6:SIZE_H5:SIZE_H4:SIZE_H3:SIZE_H2:SIZE_H1:SIZE_PARAGRAPH:SIZE_SPAN:SIZE_A";
	
	public static final String SIZE_H6="h6";
	public static final String SIZE_H5="h5";
	public static final String SIZE_H4="h4";
	public static final String SIZE_H3="h3";
	public static final String SIZE_H2="h2";
	public static final String SIZE_H1="h1";
	public static final String SIZE_PARAGRAPH="p";
	public static final String SIZE_SPAN="span";
	public static final String SIZE_A="a";
	public static final String SIZE_SMALL="small";
	public static final String SIZE_CUSTOM="div";
		
	protected static final String ZDEPTHSTRING="ZDEPTH_DEFAULT:ZDEPTH_REMOVE:ZDEPTH_1:ZDEPTH_2:ZDEPTH_3:ZDEPTH_4:ZDEPTH_5";
	
	public static final String ZDEPTH_DEFAULT="";
	public static final String ZDEPTH_REMOVE="z-depth-0";
	public static final String ZDEPTH_1="z-depth-1";
	public static final String ZDEPTH_2="z-depth-2";
	public static final String ZDEPTH_3="z-depth-3";
	public static final String ZDEPTH_4="z-depth-4";
	public static final String ZDEPTH_5="z-depth-5";
	
	protected static final String INPUTSTRING="INPUT_TEXT:INPUT_PASSWORD:INPUT_EMAIL:INPUT_TEL:INPUT_URL:INPUT_DATE:INPUT_TIME:INPUT_DATETIMELOCAL:INPUT_SEARCH:INPUT_NUMBER";
	
	public static final String INPUT_TEXT="text";
	public static final String INPUT_PASSWORD="password";
	public static final String INPUT_EMAIL="email";
	public static final String INPUT_TEL="tel";
	public static final String INPUT_URL="url";
	public static final String INPUT_DATE="date";
	public static final String INPUT_TIME="time";
	public static final String INPUT_DATETIMELOCAL="datetime-local";
	public static final String INPUT_SEARCH="search";
	public static final String INPUT_NUMBER="number";
	protected static final String INPUT_TEXTAREA="text";
	
	public static final String CARD_SMALL="small";
	public static final String CARD_MEDIUM="medium";
	public static final String CARD_LARGE="large";
	public static final String CARD_NOTSPECIFIED="";
	
	protected static final String COLLAPSESTRING="COLLAPSE_NONE:COLLAPSE_ACCORDION:COLLAPSE_EXPANDABLE";
	
	public static final String COLLAPSE_NONE="";
	public static final String COLLAPSE_ACCORDION="accordion";
	public static final String COLLAPSE_EXPANDABLE="expandable";
	
	protected static final String TOOLTIPSTRING="TOOLTIP_TOP:TOOLTIP_BOTTOM:TOOLTIP_LEFT:TOOLTIP_RIGHT";
	
	public static final String TOOLTIP_TOP="top";
	public static final String TOOLTIP_BOTTOM="bottom";
	public static final String TOOLTIP_LEFT="left";
	public static final String TOOLTIP_RIGHT="right";
	
	public static final int UITYPE_UNDEFINED=0;
	public static final int UITYPE_BUTTON=1;	
	public static final int UITYPE_LABEL=2;
	public static final int UITYPE_INPUTFIELD=3;
	public static final int UITYPE_DIVIDER=4;
	public static final int UITYPE_IMAGE=5;
	public static final int UITYPE_VIDEO=6;
	public static final int UITYPE_CARD=7;
	public static final int UITYPE_CHIP=8;
	public static final int UITYPE_COMBO=9;
	public static final int UITYPE_SWITCH=10;
	public static final int UITYPE_CHECKBOX=11;
	public static final int UITYPE_CODELABEL=12;
	public static final int UITYPE_TABEL=13;
	public static final int UITYPE_ABMCONTAINER=14;
	public static final int UITYPE_MODALSHEET=15;
	public static final int UITYPE_PARALLAX=16;
	public static final int UITYPE_TABS=17;
	public static final int UITYPE_ACTIONBUTTON=18;
	public static final int UITYPE_LIST=19;
	public static final int UITYPE_IMAGESLIDER=20;
	public static final int UITYPE_RADIOGROUP=21;
	public static final int UITYPE_BADGE=22;
	public static final int UITYPE_GOOGLEMAP=23;
	public static final int UITYPE_CANVAS=24;
	public static final int UITYPE_FILEINPUT=25;
	public static final int UITYPE_UPLOAD=26;
	public static final int UITYPE_SIGNATUREPAD=27;
	public static final int UITYPE_CHART=28;
	public static final int UITYPE_SOCIALOAUTH=29;
	public static final int UITYPE_CALENDAR=30;
	public static final int UITYPE_TREETABLE=31;
	public static final int UITYPE_PAGINATION=32;
	public static final int UITYPE_CUSTOMCOMPONENT=33;
	public static final int UITYPE_SLIDER=34;
	public static final int UITYPE_RANGE=35;
	public static final int UITYPE_DTSCROLLER=36;
	public static final int UITYPE_DTPICKER=37;
	public static final int UITYPE_EDITOR=38;
	public static final int UITYPE_SOCIALSHARE=41;
	public static final int UITYPE_PDFVIEWER=43;
	public static final int UITYPE_PIVOTTABLE=44;
	public static final int UITYPE_AUDIOPLAYER=45;
	public static final int UITYPE_TIMELINE=46;
	public static final int UITYPE_FLEXWALL=47;
	public static final int UITYPE_SVGSURFACE=48;
	public static final int UITYPE_TABELMUTABLE=49;
	public static final int UITYPE_PATTERNLOCK=50;
	public static final int UITYPE_CHRONOLIST=51;
	protected static final int UITYPE_XPLAY=52;
	public static final int UITYPE_CUSTOMCARD=53;
	public static final int UITYPE_CHAT=54;
	public static final int UITYPE_PLANNER=55;
	public static final int UITYPE_PERCENTSLIDER=56;
	public static final int UITYPE_SMARTWIZARD=57;
	public static final int UITYPE_ITEMLIST=58;
	public static final int UITYPE_REPORTCOMPONENT=59;
	public static final int UITYPE_COMPOSER=62;
	public static final int UITYPE_FILEMANAGER=63;
	public static final int UITYPE_TABELINFINITE=64;
	
	public static final int YOUTUBESTATE_UNSTARTED=-1;
	public static final int YOUTUBESTATE_ENDED=0;
	public static final int YOUTUBESTATE_PLAYING=1;
	public static final int YOUTUBESTATE_PAUSED=2;
	public static final int YOUTUBESTATE_BUFFERING=3;
	public static final int YOUTUBESTATE_VIDEOCUED=5;
	
	protected static final String ICONALIGNSTRING="ICONALIGN_LEFT:ICONALIGN_RIGHT";
	
	public static final String ICONALIGN_LEFT="left";
	public static final String ICONALIGN_RIGHT="right";
	public static final String ICONALIGN_CENTER="center";
	
	protected static final String ICONSIZESTRING="ICONSIZE_TINY:ICONSIZE_SMALL:ICONSIZE_MEDIUM:ICONSIZE_LARGE";
	
	public static final String ICONSIZE_TINY="tiny";
	public static final String ICONSIZE_SMALL="small";
	public static final String ICONSIZE_MEDIUM="medium";
	public static final String ICONSIZE_LARGE="large";
	
	protected static final String TEXTALIGNSTRING="TEXTALIGN_LEFT:TEXTALIGN_CENTER:TEXTALIGN_RIGHT:TEXTALIGN_JUSTIFY";	
			
	public static final String TEXTALIGN_LEFT="left";
	public static final String TEXTALIGN_CENTER="center";
	public static final String TEXTALIGN_RIGHT="right";	
	public static final String TEXTALIGN_JUSTIFY="justify";	
	
	public static final String IMAGESLIDER_LEFT="left-align";
	public static final String IMAGESLIDER_RIGHT="right-align";
	public static final String IMAGESLIDER_CENTER="center-align";
	
	public static final String SLIDER_CONNECT_NONE="false";
	public static final String SLIDER_CONNECT_LOWER="lower";
	public static final String SLIDER_CONNECT_UPPER="upper";
	
	protected static final String SLIDER_DIRECTION_LEFTTORIGHT="ltr";
	protected static final String SLIDER_DIRECTION_RIGHTTOLEFT="rtl";
	
	public static final String RANGE_CONNECT_FALSE="false";
	public static final String RANGE_CONNECT_TRUE="true";
		
	public static final String RANGE_DIRECTION_LEFTTORIGHT="ltr";
	public static final String RANGE_DIRECTION_RIGHTTOLEFT="rtl";
	
	public static final String CELL_ALIGN_LEFT=""; // left-align";
	public static final String CELL_ALIGN_RIGHT="hralign-wrapper"; // "right-align";
	public static final String CELL_ALIGN_CENTER="hcalign-wrapper"; //"center-align";
	public static final String CELL_ALIGN_JUSTIFY="hjalign-wrapper";
	
	public static final String TABLECELL_HORIZONTALALIGN_LEFT="left-align";
	public static final String TABLECELL_HORIZONTALALIGN_CENTER="center-align";
	public static final String TABLECELL_HORIZONTALALIGN_RIGHT="right-align";
	
	public static final String TABLECELL_VERTICALALIGN_TOP="top";
	public static final String TABLECELL_VERTICALALIGN_MIDDLE="middle";
	public static final String TABLECELL_VERTICALALIGN_BOTTOM="bottom";	
	
	public static final String POSITION_TOPLEFT="topleft";
	public static final String POSITION_TOPRIGHT="topright";
	public static final String POSITION_BOTTOMLEFT="bottomleft";
	public static final String POSITION_BOTTOMRIGHT="bottomright";
	
	protected static boolean MobGen=false;
	
	protected static final int UITYPE_ROW=1000;
	protected static final int UITYPE_CELL=1001;
	protected static final int UITYPE_LAYOUT=1002;
	protected static final int UITYPE_NAVBAR=1003;
	
	private static Map<String,String> colorMap = new HashMap<String,String>();
	private static ABMHtmlUtils innerHTMLConv=null;
	
	protected List<String> AppleTouchIcons=new ArrayList<String>();
	protected List<String> MSTileIcons=new ArrayList<String>();
	protected List<String> FavorityIcons=new ArrayList<String>();
	protected List<String> AppleTouchIconSizes=new ArrayList<String>();
	protected List<String> MSTileIconSizes=new ArrayList<String>();
	protected List<String> FavorityIconSizes=new ArrayList<String>();
	public static String Manifest="";
	public static String MaskIcon="";
	public static String MaskIconColor=ABMaterial.COLOR_WHITE;
	public static String MaskIconColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public static String MSTileColor=ABMaterial.COLOR_WHITE;
	public static String MSTileColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public static String AndroidChromeThemeColor=ABMaterial.COLOR_LIGHTBLUE;
	public static String AndroidChromeThemeColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public static String AppleTitleBarThemeColor=ABMaterial.COLOR_BLACK;
	public static String AppleTitleBarThemeColorIntensity=ABMaterial.INTENSITY_NORMAL;
	public static String BrowserConfig="none";
	
	public static final String COMBO_DATA_BELOWINPUT="true";
	public static final String COMBO_DATA_FLOATING="false";
	
	public static final String CANVAS_LINECAP_BUTT="butt";
	public static final String CANVAS_LINECAP_ROUND="round";
	public static final String CANVAS_LINECAP_SQUARE="square";
	
	public static final String CANVAS_LINEJOIN_BEVEL="bevel";
	public static final String CANVAS_LINEJOIN_ROUND="round";
	public static final String CANVAS_LINEJOIN_MITER="miter";
	
	public static final String CANVAS_TEXTALIGN_START="start";
	public static final String CANVAS_TEXTALIGN_END="end";
	public static final String CANVAS_TEXTALIGN_CENTER="center";
	public static final String CANVAS_TEXTALIGN_LEFT="left";
	public static final String CANVAS_TEXTALIGN_RIGHT="right";
	
	public static final String INPUT_TEXTALIGN_START="start";
	public static final String INPUT_TEXTALIGN_END="end";
	public static final String INPUT_TEXTALIGN_CENTER="center";
	public static final String INPUT_TEXTALIGN_LEFT="left";
	public static final String INPUT_TEXTALIGN_RIGHT="right";
	
	public static final String CANVAS_TEXTBASELINE_ALPHABETIC="alphabetic";
	public static final String CANVAS_TEXTBASELINE_TOP="top";
	public static final String CANVAS_TEXTBASELINE_HANGING="hanging";
	public static final String CANVAS_TEXTBASELINE_MIDDLE="middle";
	public static final String CANVAS_TEXTBASELINE_IDEOGRAPHIC="ideographic";
	public static final String CANVAS_TEXTBASELINE_BOTTOM="bottom";
	
	public static final String CANVAS_OPERATION_SOURCEOVER="source-over";
	public static final String CANVAS_OPERATION_SOURCEATOP="source-atop";
	public static final String CANVAS_OPERATION_SOURCEIN="source-in";
	public static final String CANVAS_OPERATION_SOURCEOUT="source-out";
	public static final String CANVAS_OPERATION_DESTINATIONOVER="destination-over";
	public static final String CANVAS_OPERATION_DESTINATIONATOP="destination-atop";
	public static final String CANVAS_OPERATION_DESTINATIONIN="destination-in";
	public static final String CANVAS_OPERATION_DESTINATIONOUT="destination-out";
	public static final String CANVAS_OPERATION_LIGHTER="lighter";
	public static final String CANVAS_OPERATION_COPY="copy";
	public static final String CANVAS_OPERATION_XOR="xor";
	
	public static final String CANVAS_TEXTSTYLE_NORMAL="normal";
	public static final String CANVAS_TEXTSTYLE_ITALIC="italic";
	public static final String CANVAS_TEXTSTYLE_OBLIQUE="oblique";
	
	public static final String CANVAS_TEXTVARIANT_NORMAL="normal";
	public static final String CANVAS_TEXTVARIANT_SMALLCAPS="small-caps";
	
	public static final String CANVAS_TEXTWEIGHT_NORMAL="normal";
	public static final String CANVAS_TEXTWEIGHT_BOLD="bold";
	public static final String CANVAS_TEXTWEIGHT_BOLDER="bolder";
	public static final String CANVAS_TEXTWEIGHT_LIGHTER="lighter";
	public static final String CANVAS_TEXTWEIGHT_100="100";
	public static final String CANVAS_TEXTWEIGHT_200="200";
	public static final String CANVAS_TEXTWEIGHT_300="300";
	public static final String CANVAS_TEXTWEIGHT_400="400";
	public static final String CANVAS_TEXTWEIGHT_500="500";
	public static final String CANVAS_TEXTWEIGHT_600="600";
	public static final String CANVAS_TEXTWEIGHT_700="700";
	public static final String CANVAS_TEXTWEIGHT_800="800";
	public static final String CANVAS_TEXTWEIGHT_900="900";
	
	public static final String CHART_RATIO_SQUARE="ct-square";
	public static final String CHART_RATIO_MINORSECOND="ct-minor-second";
	public static final String CHART_RATIO_MAJORSECOND="ct-major-second";
	public static final String CHART_RATIO_MINORTHIRD="ct-minor-third";
	public static final String CHART_RATIO_MAJORTHIRD="ct-major-third";
	public static final String CHART_RATIO_PERFECTFORTH="ct-perfect-fourth";
	public static final String CHART_RATIO_PERFECTFIFTH="ct-perfect-fifth";
	public static final String CHART_RATIO_MINORSIXTH="ct-minor-sixth";
	public static final String CHART_RATIO_GOLDENSECTION="ct-golden-section";
	public static final String CHART_RATIO_MAJORSIXTH="ct-major-sixth";
	public static final String CHART_RATIO_MINORSEVENTH="ct-minor-seventh";
	public static final String CHART_RATIO_MAJORSEVENTH="ct-major-seventh";
	public static final String CHART_RATIO_OCTAVE="ct-octave";
	public static final String CHART_RATIO_MAJORTENTH="ct-major-tenth";
	public static final String CHART_RATIO_MAJORELEVENTH="ct-major-eleventh";
	public static final String CHART_RATIO_MAJORTWELFTH="ct-major-twelfth";
	public static final String CHART_RATIO_DOUBLEOCTAVE="ct-double-octave";
	
	public static final String CHART_TYPELINE="ct-line";
	public static final String CHART_TYPEBAR="ct-bar";
	public static final String CHART_TYPEPIE="ct-pie";
	
	public static final String CHART_SERIEINDEX_A="a";
	public static final String CHART_SERIEINDEX_B="b";
	public static final String CHART_SERIEINDEX_C="c";
	public static final String CHART_SERIEINDEX_D="d";
	public static final String CHART_SERIEINDEX_E="e";
	public static final String CHART_SERIEINDEX_F="f";
	public static final String CHART_SERIEINDEX_G="g";
	public static final String CHART_SERIEINDEX_H="h";
	public static final String CHART_SERIEINDEX_I="i";
	public static final String CHART_SERIEINDEX_J="j";
	public static final String CHART_SERIEINDEX_K="k";
	public static final String CHART_SERIEINDEX_L="l";
	public static final String CHART_SERIEINDEX_M="m";
	public static final String CHART_SERIEINDEX_N="n";
	public static final String CHART_SERIEINDEX_O="o";
	protected static final String CHART_SERIEINDEX_Z="z";
	
	public static final String CHART_LINESMOOTH_NONE="Chartist.Interpolation.none()";
	public static final String CHART_LINESMOOTH_SIMPLE="Chartist.Interpolation.simple()";
	public static final String CHART_LINESMOOTH_STEP="Chartist.Interpolation.step()";
	public static final String CHART_LINESMOOTH_CARDINAL="Chartist.Interpolation.cardinal()";
	
	public static final String CHART_POSITION_START="start";
	public static final String CHART_POSITION_END="end";
	
	public static final String CHART_AXISTYPE_UNDEFINED="";
	public static final String CHART_AXISTYPE_STEPAXIS="Chartist.StepAxis";
	public static final String CHART_AXISTYPE_FIXEDSCALEAXIS="Chartist.FixedScaleAxis";
	public static final String CHART_AXISTYPE_AUTOSCALEAXIS="Chartist.AutoScaleAxis";
	
	public static final String CHART_LABELPOSITION_INSIDE="inside";
	public static final String CHART_LABELPOSITION_OUTSIDE="outside";
	public static final String CHART_LABELPOSITION_CENTER="center";
	
	public static final String CHART_LABELDIRECTION_NEUTRAL="neutral";
	public static final String CHART_LABELDIRECTION_EXPLODE="explode";
	public static final String CHART_LABELDIRECTION_IMPLODE="implode";
	
	public static final int CHART_NULLVALUE=Integer.MAX_VALUE;
	
	public static final String VIEWER_MOBILE="mobile";
	public static final String VIEWER_TABLET="tablet";
	public static final String VIEWER_LAPTOP="laptop";
	public static final String VIEWER_DESKTOP="desktop";

	public static final String SOCIALOAUTH_FACEBOOK="facebook";
	public static final String SOCIALOAUTH_FOURSQUARE="foursquare";
	public static final String SOCIALOAUTH_GOOGLEPLUS="google";
	public static final String SOCIALOAUTH_INSTAGRAM="instagram";
	public static final String SOCIALOAUTH_LINKEDIN="linkedin";
	public static final String SOCIALOAUTH_SOUNDCLOUD="soundcloud";
	public static final String SOCIALOAUTH_TWITTER="twitter";
	public static final String SOCIALOAUTH_WINDOWSLIVE="windows";
	public static final String SOCIALOAUTH_YAHOO="yahoo";
	
	public static final String SOCIALSHARE_EMAIL="email";
	public static final String SOCIALSHARE_TWITTER="twitter";
	public static final String SOCIALSHARE_FACEBOOK="facebook";
	public static final String SOCIALSHARE_FACEBOOKEXTENDED="facebookextended";
	public static final String SOCIALSHARE_FACEBOOKLIKE="facebooklike";
	public static final String SOCIALSHARE_GOOGLEPLUS="googleplus";
	public static final String SOCIALSHARE_LINKEDIN="linkedin";
	public static final String SOCIALSHARE_PINTREST="pintrest";
	public static final String SOCIALSHARE_WHATSAPP="whatsapp";
	
	public static final String SOCIALSHAREBUTTONTYPE_BASIC="Basic";
	public static final String SOCIALSHAREBUTTONTYPE_ICONSONLY="Icons";
	public static final String SOCIALSHAREBUTTONTYPE_ICONSCOUNTINSIDE="IconsCountInside";
	public static final String SOCIALSHAREBUTTONTYPE_BUTTONLABEL="ButtonLabel";
	public static final String SOCIALSHAREBUTTONTYPE_ICONSCOUNT="IconsCount";
	public static final String SOCIALSHAREBUTTONTYPE_BUTTONLABELCOUNT="ButtonLabelCount";
	
	public static final String SOCIALSHARTHEMETYPE_FLAT="flat";
	public static final String SOCIALSHARTHEMETYPE_CLASSIC="classic";
	public static final String SOCIALSHARTHEMETYPE_MINIMA="minima";
	public static final String SOCIALSHARTHEMETYPE_PLAIN="plain";
	
	
	public static final int FIRSTDAYOFWEEK_MONDAY=1;
	public static final int FIRSTDAYOFWEEK_TUESDAY=2;
	public static final int FIRSTDAYOFWEEK_WEDNESDAY=3;
	public static final int FIRSTDAYOFWEEK_THURSDAY=4;
	public static final int FIRSTDAYOFWEEK_FRIDAY=5;
	public static final int FIRSTDAYOFWEEK_SATURDAY=6;
	public static final int FIRSTDAYOFWEEK_SUNDAY=0;
	
	public static final String CALENDAR_DEFAULTVIEW_WEEK="agendaWeek";
	public static final String CALENDAR_DEFAULTVIEW_DAY="agendaDay";
	public static final String CALENDAR_DEFAULTVIEW_MONTH="month";
	
	public static final int MODALSHEET_SIZE_NORMAL=0;
	public static final int MODALSHEET_SIZE_LARGE=1;
	public static final int MODALSHEET_SIZE_FULL=2;
	
	public static final String MODALSHEET_TYPE_BOTTOM="bottom";
	public static final String MODALSHEET_TYPE_TOP="top";
	public static final String MODALSHEET_TYPE_NORMAL="normal";
	
	public static final String DATETIMESCROLLER_MODE_SCROLLER="scroller";
	public static final String DATETIMESCROLLER_MODE_CLICKPICK="clickpick";
	public static final String DATETIMESCROLLER_MODE_MIXED="mixed";
	
	public static final String DATETIMESCROLLER_TYPE_DATE="date";
	public static final String DATETIMESCROLLER_TYPE_TIME="time";
	public static final String DATETIMESCROLLER_TYPE_DATETIME="datetime";
	
	public static final String DATETIMEPICKER_TYPE_DATE="date";
	public static final String DATETIMEPICKER_TYPE_WEEK="week";
	public static final String DATETIMEPICKER_TYPE_TIME="time";
	public static final String DATETIMEPICKER_TYPE_DATETIME="datetime";
	
	protected static final String FIREBASE_PROVIDER_GOOGLE="google";
	protected static final String FIREBASE_PROVIDER_FACEBOOK="facebook";
	protected static final String FIREBASE_PROVIDER_TWITTER="twitter";
	protected static final String FIREBASE_PROVIDER_GITHUB="github";
	
	protected static ExtractResult res=null;

	protected static Map<String,String> useNeeds = new LinkedHashMap<String,String>();
	
	protected static Map<String,Map<String,String>> Trans = new LinkedHashMap<String,Map<String,String>>();
	protected static Map<String,Map<String,String>> Defs = new LinkedHashMap<String, Map<String,String>>();
	
	public static final String TOTP_ENCRYPT_SHA1 = "HmacSHA1";
	public static final String TOTP_ENCRYPT_SHA256 = "HmacSHA256";
	public static final String TOTP_ENCRYPT_SHA512 = "HmacSHA512";
	
	public static final String MSGBOX_TYPE_SUCCESS="success";
	public static final String MSGBOX_TYPE_ERROR="error";
	public static final String MSGBOX_TYPE_WARNING="warning";
	public static final String MSGBOX_TYPE_INFO="info";
	public static final String MSGBOX_TYPE_QUESTION="question";
	public static final String MSGBOX_TYPE_NOICON="";
	
	public static final String MSGBOX_RESULT_OVERLAY="abmoverlay";
	public static final String MSGBOX_RESULT_CANCEL="abmcancel";
	public static final String MSGBOX_RESULT_CLOSE="abmclose";
	public static final String MSGBOX_RESULT_ESC="abmesc";
	public static final String MSGBOX_RESULT_OK="abmok";
		
	public static final String INPUTBOX_TYPE_SUCCESS="success";
	public static final String INPUTBOX_TYPE_ERROR="error";
	public static final String INPUTBOX_TYPE_WARNING="warning";
	public static final String INPUTBOX_TYPE_INFO="info";
	public static final String INPUTBOX_TYPE_QUESTION="question";
			
	public static final String INPUTBOX_QUESTIONTYPE_TEXT="text";
	public static final String INPUTBOX_QUESTIONTYPE_EMAIL="email";
	public static final String INPUTBOX_QUESTIONTYPE_PASSWORD="password";
	public static final String INPUTBOX_QUESTIONTYPE_TEXTAREA="textarea";
	public static final String INPUTBOX_QUESTIONTYPE_RADIO="radio";
	public static final String INPUTBOX_QUESTIONTYPE_CHECKBOX="checkbox";
	
	public static final String INPUTBOX_RESULT_OVERLAY="abmoverlay";
	public static final String INPUTBOX_RESULT_CANCEL="abmcancel";
	public static final String INPUTBOX_RESULT_CLOSE="abmclose";
	public static final String INPUTBOX_RESULT_ESC="abmesc";
	public static final String INPUTBOX_RESULT_INVALID="abminvalid";
	
	public static final String MSGBOX_POS_TOP_LEFT="swal2pos-tl";
	public static final String MSGBOX_POS_TOP_CENTER="swal2pos-tc";
	public static final String MSGBOX_POS_TOP_RIGHT="swal2pos-tr";
	public static final String MSGBOX_POS_CENTER_LEFT="swal2pos-cl";
	public static final String MSGBOX_POS_CENTER_CENTER="swal2pos-cc";
	public static final String MSGBOX_POS_CENTER_RIGHT="swal2pos-cr";
	public static final String MSGBOX_POS_BOTTOM_LEFT="swal2pos-bl";
	public static final String MSGBOX_POS_BOTTOM_CENTER="swal2pos-bc";
	public static final String MSGBOX_POS_BOTTOM_RIGHT="swal2pos-br";
	
	public static boolean AllowZoom=false;
	
	public Utilities Util = new Utilities();
	
	protected static Map<String,ExtraColor> ExtraColors = new HashMap<String,ExtraColor>();
	
	/*
	Letter  Date or Time Component  Presentation        Examples
	------  ----------------------  ------------------  -------------------------------------
	G       Era designator          Text                AD
	y       Year                    Year                1996; 96
	Y       Week year               Year                2009; 09
	M/L     Month in year           Month               July; Jul; 07
	w       Week in year            Number              27
	W       Week in month           Number              2
	D       Day in year             Number              189
	d       Day in month            Number              10
	F       Day of week in month    Number              2
	E       Day in week             Text                Tuesday; Tue
	u       Day number of week      Number              1
	a       Am/pm marker            Text                PM
	H       Hour in day (0-23)      Number              0
	k       Hour in day (1-24)      Number              24
	K       Hour in am/pm (0-11)    Number              0
	h       Hour in am/pm (1-12)    Number              12
	m       Minute in hour          Number              30
	s       Second in minute        Number              55
	S       Millisecond             Number              978
	z       Time zone               General time zone   Pacific Standard Time; PST; GMT-08:00
	Z       Time zone               RFC 822 time zone   -0800
	X       Time zone               ISO 8601 time zone  -08; -0800; -08:00
	'abc'   Fixed String            Text				'T'
	*/
		
	/**
	 * Replaced the pre 2.11 GetUUID() method	
	 */
	public static String GetPageID(ABMPage page, String pageName, WebSocket ws) {
		if (ws==null) {
			return "";
		}
		if (!ws.getOpen()) {
			return "";
		}
		page.SetWebsocket(ws);
		
		String myUUID="";
		SimpleFuture ret = ws.RunFunctionWithResult("getGuid", null);
		if (ret!=null) {
			try {
				myUUID = (String) ret.getValue();			
			} catch (InterruptedException e) {
				//e.printStackTrace();
			} catch (TimeoutException e) {
				//e.printStackTrace();
			} catch (ExecutionException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}		
		page.UUID=myUUID;
		
		return pageName + myUUID;
	}
	
	public void AddPageMapping(String pageName, String pageClass) {
		pageClass = pageClass.toLowerCase();
		int i = pageClass.lastIndexOf(".");
		if (i>-1) {
			pageClass = pageClass.substring(i+1);
		}
		PageMappings.put(pageClass, pageName);		
	}
		
	public static void ScavengeCache(final BA ba, anywheresoftware.b4a.objects.collections.Map cachedPages) {
		List<String> ToRemoves = new ArrayList<String>();
		List<Boolean> ToRemovesClasses = new ArrayList<Boolean>();
		final anywheresoftware.b4a.BA.IterableList keys = cachedPages.Keys();
		final int keyLen = keys.getSize();		
		BA.Log("Scavenger running... (" + (keyLen/2) + " page(s) cached)");
		for (int i = 0;i < keyLen ;i++){
			String key = (String) keys.Get(i);
			if (key.contains("_ABMWSField")) {
				String wsFieldName = (String) cachedPages.GetDefault(key, ""); 
				boolean mayClose=true;
				key = key.replaceAll("_ABMWSField", "");
				
				B4AClass cachedClass = null;
				if (!wsFieldName.equals("")) {
					cachedClass = (B4AClass) cachedPages.Get(key);
					if (cachedClass!=null) {
						try {
							Field wsField = cachedClass.getClass().getDeclaredField(wsFieldName);
							WebSocket ws = (WebSocket) wsField.get(cachedClass);
							if (ws!=null) {
								if (ws.getSession()!=null) {
									String state = (String) ws.getSession().GetAttribute2("_ABMActive", "0");
									if (state.equals("1")) {
										mayClose=false;
										
									} 
								}
							}
						} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {		
							//e.printStackTrace();
						} catch (IllegalStateException e2) {
							//e2.printStackTrace();
						}
					}
				}
				if (mayClose) { 
					ToRemoves.add(key);
					if (cachedClass == null) {
						ToRemovesClasses.add(true);
					} else {
						ToRemovesClasses.add(false);
						try {
							anywheresoftware.b4a.keywords.Common.CallSubNew(ba,cachedClass,"Page_Expired");
						} catch (Exception e) {
							//e.printStackTrace();
						}
						
					}					
				} else {
					if (cachedClass != null) {
						try {
							anywheresoftware.b4a.keywords.Common.CallSubNew(ba,cachedClass,"Page_StillCached");
						} catch (Exception e) {
							//e.printStackTrace();
						}
					}
				}
			}
		}	
		
		for (int i=0;i<ToRemoves.size();i++) {
			String key = ToRemoves.get(i);
			if (ToRemovesClasses.get(i)) {
				BA.Log("Expired " + key + " B4AClass is null: True");
			} else {
				BA.Log("Expired " + key + " B4AClass is null: False");
			}
			cachedPages.Remove(key);
			cachedPages.Remove(key + "_ABMWSField");			
			
		}
		ToRemovesClasses.clear();
		ToRemovesClasses = null;
	}
	
	public static void setAppVersion(String appVersion) {
		mAppVersion = appVersion;
	}
	
	public static String getAppVersion() {
		return mAppVersion;
	}
	
	public void EnableBrowserAutocomplete(boolean enable) {
		if (enable) {
			UseBrowserAutocomplete="";
		} else {
			UseBrowserAutocomplete=" autocomplete=\"off\" ";
		}
	}
	
	public void B4JSLoadOnServer(String jsFolder, anywheresoftware.b4a.objects.collections.List ScriptsToLoad) {
		try {
			if (!jsFolder.endsWith("/") && !jsFolder.endsWith("\\")) {
				jsFolder = jsFolder + "/";
			}
			boolean HasScript=false;
			String scriptName=anywheresoftware.b4a.objects.streams.File.Combine(anywheresoftware.b4a.objects.streams.File.getDirApp(), "copymewithjar.js.needs") ;
	        FileReader script=null;
	        if (anywheresoftware.b4a.objects.streams.File.Exists(anywheresoftware.b4a.objects.streams.File.getDirApp(), "copymewithjar.js.needs")) {
	        	HasScript=true;
	        	script = new FileReader(scriptName);
	        }
			
	        factory = new ScriptEngineManager();
	        jsEngine = factory.getEngineByName("nashorn");
	        
	        StringBuilder s = new StringBuilder();
	        s.append("var _b4jsclasses = {};");
	        jsEngine.eval(s.toString());
	        
	        s = new StringBuilder();
	        s.append("var global=this,window=this,process={env:{}},console={};console.debug=print,console.warn=print,console.log=print,console.error=print,console.trace=print,Object.assign=function(o){for(var n,r=1,e=arguments.length;r<e;r++){n=arguments[r];for(var t in n)Object.prototype.hasOwnProperty.call(n,t)&&(o[t]=n[t])}return o};");
	        jsEngine.eval(s.toString());
	        
	        // blob polyfill
	        s = new StringBuilder();
	        s.append("!function(t){\"use strict\";if(t.URL=t.URL||t.webkitURL,t.Blob&&t.URL)try{return void new Blob}catch(t){}var e=t.BlobBuilder||t.WebKitBlobBuilder||t.MozBlobBuilder||function(t){var e=function(t){return Object.prototype.toString.call(t).match(/^\\[object\\s(.*)\\]$/)[1]},n=function(){this.data=[]},o=function(t,e,n){this.data=t,this.size=t.length,this.type=e,this.encoding=n},i=n.prototype,a=o.prototype,r=t.FileReaderSync,c=function(t){this.code=this[this.name=t]},l=\"NOT_FOUND_ERR SECURITY_ERR ABORT_ERR NOT_READABLE_ERR ENCODING_ERR NO_MODIFICATION_ALLOWED_ERR INVALID_STATE_ERR SYNTAX_ERR\".split(\" \"),s=l.length,d=t.URL||t.webkitURL||t,u=d.createObjectURL,f=d.revokeObjectURL,R=d,p=t.btoa,h=t.atob,b=t.ArrayBuffer,g=t.Uint8Array,w=/^[\\w-]+:\\/*\\[?[\\w\\.:-]+\\]?(?::[0-9]+)?/;for(o.fake=a.fake=!0;s--;)c.prototype[l[s]]=s+1;return d.createObjectURL||(R=t.URL=function(t){var e,n=document.createElementNS(\"http://www.w3.org/1999/xhtml\",\"a\");return n.href=t,\"origin\"in n||(\"data:\"===n.protocol.toLowerCase()?n.origin=null:(e=t.match(w),n.origin=e&&e[1])),n}),R.createObjectURL=function(t){var e,n=t.type;return null===n&&(n=\"application/octet-stream\"),t instanceof o?(e=\"data:\"+n,\"base64\"===t.encoding?e+\";base64,\"+t.data:\"URI\"===t.encoding?e+\",\"+decodeURIComponent(t.data):p?e+\";base64,\"+p(t.data):e+\",\"+encodeURIComponent(t.data)):u?u.call(d,t):void 0},R.revokeObjectURL=function(t){\"data:\"!==t.substring(0,5)&&f&&f.call(d,t)},i.append=function(t){var n=this.data;if(g&&(t instanceof b||t instanceof g)){for(var i=\"\",a=new g(t),l=0,s=a.length;l<s;l++)i+=String.fromCharCode(a[l]);n.push(i)}else if(\"Blob\"===e(t)||\"File\"===e(t)){if(!r)throw new c(\"NOT_READABLE_ERR\");var d=new r;n.push(d.readAsBinaryString(t))}else t instanceof o?\"base64\"===t.encoding&&h?n.push(h(t.data)):\"URI\"===t.encoding?n.push(decodeURIComponent(t.data)):\"raw\"===t.encoding&&n.push(t.data):(\"string\"!=typeof t&&(t+=\"\"),n.push(unescape(encodeURIComponent(t))))},i.getBlob=function(t){return arguments.length||(t=null),new o(this.data.join(\"\"),t,\"raw\")},i.toString=function(){return\"[object BlobBuilder]\"},a.slice=function(t,e,n){var i=arguments.length;return i<3&&(n=null),new o(this.data.slice(t,i>1?e:this.data.length),n,this.encoding)},a.toString=function(){return\"[object Blob]\"},a.close=function(){this.size=0,delete this.data},n}(t);t.Blob=function(t,n){var o=n&&n.type||\"\",i=new e;if(t)for(var a=0,r=t.length;a<r;a++)Uint8Array&&t[a]instanceof Uint8Array?i.append(t[a].buffer):i.append(t[a]);var c=i.getBlob(o);return!c.slice&&c.webkitSlice&&(c.slice=c.webkitSlice),c};var n=Object.getPrototypeOf||function(t){return t.__proto__};t.Blob.prototype=n(new t.Blob)}(\"undefined\"!=typeof self&&self||\"undefined\"!=typeof window&&window||this.content||this);");
	        jsEngine.eval(s.toString());
	        
	        // timer polyfill
	        s = new StringBuilder();
	        s.append("(function nashornEventLoopMain(context) {");
	        s.append("  'use strict';");
	        s.append("  var Thread = Java.type('java.lang.Thread');");
	        s.append("  var Phaser = Java.type('java.util.concurrent.Phaser');");
	        s.append("  var ArrayDeque = Java.type('java.util.ArrayDeque');");
	        s.append("  var HashMap = Java.type('java.util.HashMap');");
	        s.append("  var TimeUnit = Java.type(\"java.util.concurrent.TimeUnit\");");
	        s.append("  var Runnable = Java.type('java.lang.Runnable');");
	        s.append("  var globalTimerId;");
	        s.append("  var timerMap;");
	        s.append("  var eventLoop;");
	        s.append("  var phaser = new Phaser();");
	        s.append("  var scheduler = context.__NASHORN_POLYFILL_TIMER__;");
	        s.append("  resetEventLoop();");
	        s.append("  function resetEventLoop() {");
	        s.append("    globalTimerId = 1;");
	        s.append("    if (timerMap) {");
	        s.append("      timerMap.forEach(function (key, value) {");
	        s.append("        value.cancel(true);");
	        s.append("      })");
	        s.append("    }");
	        s.append("    timerMap = new HashMap();");
	        s.append("    eventLoop = new ArrayDeque();");
	        s.append("  };");
	        s.append("  function waitForMessages() {");
	        s.append("    phaser.register();");
	        s.append("    var wait = !(eventLoop.size() === 0);");
	        s.append("    phaser.arriveAndDeregister();");
	        s.append("");
	        s.append("    return wait;");
	        s.append("  };");
	        s.append("  function processNextMessages() {");
	        s.append("    var remaining = 1;");
	        s.append("    while (remaining) {");
	        s.append("      phaser.register();");
	        s.append("      var message = eventLoop.removeFirst();");
	        s.append("      remaining = eventLoop.size();");
	        s.append("      phaser.arriveAndDeregister();");
	        s.append("");
	        s.append("      var fn = message.fn;");
	        s.append("      var args = message.args;");
	        s.append("");
	        s.append("      try {");
	        s.append("        fn.apply(context, args);");
	        s.append("      } catch (e) {");
	        s.append("        console.trace(e);");
	        s.append("        console.trace(fn);");
	        s.append("        console.trace(args);");
	        s.append("      }");
	        s.append("    }");
	        s.append("  };");
	        s.append("  context.nashornEventLoop = {");
	        s.append("    process: function () {");
	        s.append("      while (waitForMessages()) {");
	        s.append("        processNextMessages()");
	        s.append("      }");
	        s.append("    },");
	        s.append("    reset: resetEventLoop");
	        s.append("  };");
	        s.append("  function createRunnable(fn, timerId, args, repeated) {");
	        s.append("    return new Runnable {");
	        s.append("      run: function () {");
	        s.append("        try {");
	        s.append("          var phase = phaser.register();");
	        s.append("          eventLoop.addLast({");
	        s.append("            fn: fn,");
	        s.append("            args: args");
	        s.append("          });");
	        s.append("        } catch (e) {");
	        s.append("          console.trace(e);");
	        s.append("        } finally {");
	        s.append("          if (!repeated) timerMap.remove(timerId);");
	        s.append("          phaser.arriveAndDeregister();");
	        s.append("        }");
	        s.append("      }");
	        s.append("    }");
	        s.append("  };");
	        s.append("  var setTimeout = function (fn, millis /* [, args...] */) {");
	        s.append("    var args = [].slice.call(arguments, 2, arguments.length);");
	        s.append("");
	        s.append("    var timerId = globalTimerId++;");
	        s.append("    var runnable = createRunnable(fn, timerId, args, false);");
	        s.append("");
	        s.append("    var task = scheduler.schedule(runnable, millis, TimeUnit.MILLISECONDS);");
	        s.append("    timerMap.put(timerId, task);");
	        s.append("");
	        s.append("    return timerId;");
	        s.append("  };");
	        s.append("  var setImmediate = function (fn /* [, args...] */) {");
	        s.append("    var args = [].slice.call(arguments, 1, arguments.length);");
	        s.append("    return setTimeout(fn, 0, args);");
	        s.append("  };");
	        s.append("  var clearImmediate = function (timerId) {");
	        s.append("    clearTimeout(timerId);");
	        s.append("  };");
	        s.append("  var clearTimeout = function (timerId) {");
	        s.append("    var task = timerMap.get(timerId);");
	        s.append("    if (task) {");
	        s.append("      task.cancel(true);");
	        s.append("      timerMap.remove(timerId);");
	        s.append("    }");
	        s.append("  };");
	        s.append("  var setInterval = function (fn, delay /* [, args...] */) {");
	        s.append("    var args = [].slice.call(arguments, 2, arguments.length);");
	        s.append("");
	        s.append("    var timerId = globalTimerId++;");
	        s.append("    var runnable = createRunnable(fn, timerId, args, true);");
	        s.append("    var task = scheduler.scheduleWithFixedDelay(runnable, delay, delay, TimeUnit.MILLISECONDS);");
	        s.append("    timerMap.put(timerId, task);");
	        s.append("");
	        s.append("    return timerId;");
	        s.append("  };");
	        s.append("  var clearInterval = function (timerId) {");
	        s.append("    clearTimeout(timerId);");
	        s.append("  };");
	        s.append("  context.setTimeout = setTimeout;");
	        s.append("  context.clearTimeout = clearTimeout;");
	        s.append("  context.setImmediate = setImmediate;");
	        s.append("  context.clearImmediate = clearImmediate;");
	        s.append("  context.setInterval = setInterval;");
	        s.append("  context.clearInterval = clearInterval;");
	        s.append("})(typeof global !== \"undefined\" && global || typeof self !== \"undefined\" && self || this);");
	        
	        jsEngine.eval(s.toString());
	        
	        // xml-http-request polyfill
	        s = new StringBuilder();
	        s.append("!function(t){\"use strict\";Java.type(\"java.lang.System\");var e=Packages.org.apache.http.client.methods.RequestBuilder,a=Packages.org.apache.http.concurrent.FutureCallback,n=Packages.org.apache.http.impl.nio.client.HttpAsyncClientBuilder,s=Packages.org.apache.http.message.BasicHeader,i=Java.type(\"java.util.ArrayList\"),o=(Packages.org.apache.http.entity.ByteArrayEntity,Packages.org.apache.http.entity.StringEntity);Packages.org.apache.http.client.entity.EntityBuilder,Packages.org.apache.http.entity.ContentType;t.XMLHttpRequest=function(){var r={};this.onreadystatechange=function(){},this.onload=function(){},this.onerror=function(){},this.readyState=0,this.response=null,this.responseText=null,this.responseType=\"\",this.status=null,this.statusText=null,this.timeout=0,this.ontimeout=function(){},this.withCredentials=!1;var u=null;this.abort=function(){},this.getAllResponseHeaders=function(){},this.getResponseHeader=function(t){},this.setRequestHeader=function(t,e){r[t]=e},this.open=function(a,n,s,i,o){this.readyState=1,(u=e.create(a)).setUri(n),t.setTimeout(this.onreadystatechange,0)},this.send=function(e){var l=this,c=n.create(),h=new i(r.length);for(var p in r)h.add(new s(p,r[p]));if(window.__HTTP_SERVLET_REQUEST__)for(var d=[\"Cookie\",\"Authorization\"],g=0;g<d.length;g++)h.add(new s(d[g],window.__HTTP_SERVLET_REQUEST__.getHeader(d[g])));if(c.setDefaultHeaders(h),null==e)u.setEntity(null);else{if(\"string\"!=typeof e)throw new Error(\"unsupported body data type\");u.setEntity(new o(e))}var y=c.build();y.start();var f=new a({completed:function(e){l.readyState=4;var a=org.apache.http.util.EntityUtils.toString(e.getEntity(),\"UTF-8\");l.responseText=l.response=a;var n=null;if(\"json\"===l.responseType)try{l.response=JSON.parse(l.response)}catch(t){n=t}if(!n){var s=e.getStatusLine();l.status=s.getStatusCode(),l.statusText=s.getReasonPhrase(),t.setTimeout(l.onreadystatechange,0),t.setTimeout(l.onload,0),y.close()}},cancelled:function(){t.System.err.println(\"Cancelled\"),y.close()},failed:function(e){l.readyState=4,l.status=0,l.statusText=e.getMessage(),t.setTimeout(l.onreadystatechange,0),t.setTimeout(l.onerror,0),y.close()}});y.execute(u.build(),null,f)}}}(\"undefined\"!=typeof global&&global||\"undefined\"!=typeof self&&self||this);");
	        jsEngine.eval(s.toString());
	        
	        // Maquette
	        s.append("!function(e,r){\"object\"==typeof exports&&\"undefined\"!=typeof module?r(exports):\"function\"==typeof define&&define.amd?define([\"exports\"],r):r(e.maquette={})}(this,function(e){\"use strict\";var r,t=\"http://www.w3.org/\",n=t+\"2000/svg\",o=t+\"1999/xlink\",i=[],a=function(e,r){var t={};return Object.keys(e).forEach(function(r){t[r]=e[r]}),r&&Object.keys(r).forEach(function(e){t[e]=r[e]}),t},d=function(e,r){return e.vnodeSelector===r.vnodeSelector&&(e.properties&&r.properties?e.properties.key===r.properties.key&&e.properties.bind===r.properties.bind:!e.properties&&!r.properties)},p=function(e){if(\"string\"!=typeof e)throw new Error(\"Style values must be strings\")},s=function(e,r,t){if(\"\"!==r.vnodeSelector)for(var n=t;n<e.length;n++)if(d(e[n],r))return n;return-1},c=function(e,r,t,n){var o=e[r];if(\"\"!==o.vnodeSelector){var i=o.properties;if(!(i?void 0===i.key?i.bind:i.key:void 0))for(var a=0;a<e.length;a++)if(a!==r){var p=e[a];if(d(p,o))throw new Error(t.vnodeSelector+\" had a \"+o.vnodeSelector+\" child \"+(\"added\"===n?n:\"removed\")+\", but there is now more than one. You must add unique key properties to make them distinguishable.\")}}},f=function(e){if(e.properties){var r=e.properties.enterAnimation;r&&r(e.domNode,e.properties)}},u=[],l=!1,v=function(e){(e.children||[]).forEach(v),e.properties&&e.properties.afterRemoved&&e.properties.afterRemoved.apply(e.properties.bind||e.properties,[e.domNode])},h=function(){l=!1,u.forEach(v),u.length=0},m=function(e){u.push(e),l||(l=!0,\"undefined\"!=typeof window&&\"requestIdleCallback\"in window?window.requestIdleCallback(h,{timeout:16}):setTimeout(h,16))},g=function(e){var r=e.domNode;if(e.properties){var t=e.properties.exitAnimation;if(t){r.style.pointerEvents=\"none\";return void t(r,function(){r.parentNode&&(r.parentNode.removeChild(r),m(e))},e.properties)}}r.parentNode&&(r.parentNode.removeChild(r),m(e))},y=function(e,r,t){!function(e,r,t){if(r)for(var n=0,o=r;n<o.length;n++){var i=o[n];N(i,e,void 0,t)}}(e,r.children,t),r.text&&(e.textContent=r.text),function(e,r,t){if(r)for(var i=t.eventHandlerInterceptor,a=Object.keys(r),d=a.length,s=function(d){var s,c=a[d],f=r[c];if(\"className\"===c)throw new Error('Property \"className\" is not supported, use \"class\".');if(\"class\"===c)f.split(/\\s+/).forEach(function(r){return e.classList.add(r)});else if(\"classes\"===c)for(var u=Object.keys(f),l=u.length,v=0;v<l;v++){var h=u[v];f[h]&&e.classList.add(h)}else if(\"styles\"===c){var m=Object.keys(f),g=m.length;for(v=0;v<g;v++){var y=m[v],N=f[y];N&&(p(N),t.styleApplyer(e,y,N))}}else if(\"key\"!==c&&null!=f){var b=typeof f;\"function\"===b?0===c.lastIndexOf(\"on\",0)&&(i&&(f=i(c,f,e,r)),\"oninput\"===c&&(s=f,f=function(e){s.apply(this,[e]),e.target[\"oninput-value\"]=e.target.value}),e[c]=f):\"string\"===b&&\"value\"!==c&&\"innerHTML\"!==c?t.namespace===n&&\"href\"===c?e.setAttributeNS(o,c,f):e.setAttribute(c,f):e[c]=f}},c=0;c<d;c++)s(c)}(e,r.properties,t),r.properties&&r.properties.afterCreate&&r.properties.afterCreate.apply(r.properties.bind||r.properties,[e,t,r.vnodeSelector,r.properties,r.children])},N=function(e,r,t,o){var i,d=0,p=e.vnodeSelector,s=r.ownerDocument;if(\"\"===p)i=e.domNode=s.createTextNode(e.text),void 0!==t?r.insertBefore(i,t):r.appendChild(i);else{for(var c=0;c<=p.length;++c){var f=p.charAt(c);if(c===p.length||\".\"===f||\"#\"===f){var u=p.charAt(d-1),l=p.slice(d,c);\".\"===u?i.classList.add(l):\"#\"===u?i.id=l:(\"svg\"===l&&(o=a(o,{namespace:n})),void 0!==o.namespace?i=e.domNode=s.createElementNS(o.namespace,l):(i=e.domNode=e.domNode||s.createElement(l),\"input\"===l&&e.properties&&void 0!==e.properties.type&&i.setAttribute(\"type\",e.properties.type)),void 0!==t?r.insertBefore(i,t):i.parentNode!==r&&r.appendChild(i)),d=c+1}}y(i,e,o)}},b=function(e,r,t){r&&r.split(\" \").forEach(function(r){return e.classList.toggle(r,t)})};r=function(e,t,u){var l=e.domNode,v=!1;if(e===t)return!1;var h=!1;if(\"\"===t.vnodeSelector){if(t.text!==e.text){var m=l.ownerDocument.createTextNode(t.text);return l.parentNode.replaceChild(m,l),t.domNode=m,v=!0}t.domNode=l}else 0===t.vnodeSelector.lastIndexOf(\"svg\",0)&&(u=a(u,{namespace:n})),e.text!==t.text&&(h=!0,void 0===t.text?l.removeChild(l.firstChild):l.textContent=t.text),t.domNode=l,h=function(e,t,n,o,a){if(n===o)return!1;o=o||i;for(var p,u=(n=n||i).length,l=o.length,v=0,h=0,m=!1;h<l;){var y=v<u?n[v]:void 0,b=o[h];if(void 0!==y&&d(y,b))m=r(y,b,a)||m,v++;else{var w=s(n,b,v+1);if(w>=0){for(p=v;p<w;p++)g(n[p]),c(n,p,e,\"removed\");m=r(n[w],b,a)||m,v=w+1}else N(b,t,v<u?n[v].domNode:void 0,a),f(b),c(o,h,e,\"added\")}h++}if(u>v)for(p=v;p<u;p++)g(n[p]),c(n,p,e,\"removed\");return m}(t,l,e.children,t.children,u)||h,h=function(e,r,t,i){if(t){for(var a=!1,d=Object.keys(t),s=d.length,c=0;c<s;c++){var f=d[c],u=t[f],l=r[f];if(\"class\"===f)l!==u&&(b(e,l,!1),b(e,u,!0));else if(\"classes\"===f)for(var v=e.classList,h=Object.keys(u),m=h.length,g=0;g<m;g++){var y=h[g],N=!!u[y];N!==!!l[y]&&(a=!0,N?v.add(y):v.remove(y))}else if(\"styles\"===f){var w=Object.keys(u),x=w.length;for(g=0;g<x;g++){var A=w[g],S=u[A];S!==l[A]&&(a=!0,S?(p(S),i.styleApplyer(e,A,S)):i.styleApplyer(e,A,\"\"))}}else if(u||\"string\"!=typeof l||(u=\"\"),\"value\"===f){var k=e[f];k!==u&&(e[\"oninput-value\"]?k===e[\"oninput-value\"]:u!==l)&&(e[f]=u,e[\"oninput-value\"]=void 0),u!==l&&(a=!0)}else if(u!==l){var E=typeof u;\"function\"===E&&i.eventHandlerInterceptor||(\"string\"===E&&\"innerHTML\"!==f?i.namespace===n&&\"href\"===f?e.setAttributeNS(o,f,u):\"role\"===f&&\"\"===u?e.removeAttribute(f):e.setAttribute(f,u):e[f]!==u&&(e[f]=u),a=!0)}}return a}}(l,e.properties,t.properties,u)||h,t.properties&&t.properties.afterUpdate&&t.properties.afterUpdate.apply(t.properties.bind||t.properties,[l,u,t.vnodeSelector,t.properties,t.children]);return h&&t.properties&&t.properties.updateAnimation&&t.properties.updateAnimation(l,t.properties,e.properties),v};var w=function(e,t){return{getLastRender:function(){return e},update:function(n){if(e.vnodeSelector!==n.vnodeSelector)throw new Error(\"The selector for the root VNode may not be changed. (consider using dom.merge and add one extra level to the virtual DOM)\");var o=e;e=n,r(o,n,t)},domNode:e.domNode}},x={namespace:void 0,performanceLogger:function(){},eventHandlerInterceptor:void 0,styleApplyer:function(e,r,t){e.style[r]=t}},A=function(e){return a(x,e)},S={create:function(e,r){return r=A(r),N(e,document.createElement(\"div\"),void 0,r),w(e,r)},append:function(e,r,t){return t=A(t),N(r,e,void 0,t),w(r,t)},insertBefore:function(e,r,t){return t=A(t),N(r,e.parentNode,e,t),w(r,t)},merge:function(e,r,t){return t=A(t),r.domNode=e,y(e,r,t),w(r,t)},replace:function(e,r,t){return t=A(t),N(r,e.parentNode,e,t),e.parentNode.removeChild(e),w(r,t)}},k=function(e,r,t){for(var n=0,o=r.length;n<o;n++){var i=r[n];Array.isArray(i)?k(e,i,t):null!=i&&(\"string\"==typeof i&&(i={vnodeSelector:\"\",properties:void 0,children:void 0,text:i.toString(),domNode:null}),t.push(i))}};var E;E=Array.prototype.find?function(e,r){return e.find(r)}:function(e,r){return e.filter(r)[0]};var C=function(e,r,t){var n=function(n){t(\"domEvent\",n);var o=r(),i=function(e,r){for(var t=[];e!==r;)t.push(e),e=e.parentNode;return t}(n.currentTarget,o.domNode);i.reverse();var a,d,p,s=(a=o.getLastRender(),d=a,i.forEach(function(e){d=d&&d.children?E(d.children,function(r){return r.domNode===e}):void 0}),d);return e.scheduleRender(),s&&(p=s.properties[\"on\"+n.type].apply(s.properties.bind||this,arguments)),t(\"domEventProcessed\",n),p};return function(e,r,t,o){return n}};e.dom=S,e.h=function(e,r,t){if(Array.isArray(r))t=r,r=void 0;else if(r&&(\"string\"==typeof r||r.hasOwnProperty(\"vnodeSelector\"))||t&&(\"string\"==typeof t||t.hasOwnProperty(\"vnodeSelector\")))throw new Error(\"h called with invalid arguments\");var n,o;return void 0!==t&&1===t.length&&\"string\"==typeof t[0]?n=t[0]:t&&(k(e,t,o=[]),0===o.length&&(o=void 0)),{vnodeSelector:e,properties:r,children:o,text:\"\"===n?void 0:n,domNode:null}},e.createProjector=function(e){var r,t,n=A(e),o=n.performanceLogger,i=!0,a=!1,d=[],p=[],s=function(e,t,i){var a;n.eventHandlerInterceptor=C(r,function(){return a},o),a=e(t,i(),n),d.push(a),p.push(i)},c=function(){if(t=void 0,i){i=!1,o(\"renderStart\",void 0);for(var e=0;e<d.length;e++){var r=p[e]();o(\"rendered\",void 0),d[e].update(r),o(\"patched\",void 0)}o(\"renderDone\",void 0),i=!0}};return r={renderNow:c,scheduleRender:function(){t||a||(t=requestAnimationFrame(c))},stop:function(){t&&(cancelAnimationFrame(t),t=void 0),a=!0},resume:function(){a=!1,i=!0,r.scheduleRender()},append:function(e,r){s(S.append,e,r)},insertBefore:function(e,r){s(S.insertBefore,e,r)},merge:function(e,r){s(S.merge,e,r)},replace:function(e,r){s(S.replace,e,r)},detach:function(e){for(var r=0;r<p.length;r++)if(p[r]===e)return p.splice(r,1),d.splice(r,1)[0];throw new Error(\"renderFunction was not found\")}}},e.createCache=function(){var e,r;return{invalidate:function(){r=void 0,e=void 0},result:function(t,n){if(e)for(var o=0;o<t.length;o++)e[o]!==t[o]&&(r=void 0);return r||(r=n(),e=t),r}}},e.createMapping=function(e,r,t){var n=[],o=[];return{results:o,map:function(i){for(var a=i.map(e),d=o.slice(),p=0,s=0;s<i.length;s++){var c=i[s],f=a[s];if(f===n[p])o[s]=d[p],t(c,d[p],s),p++;else{for(var u=!1,l=1;l<n.length+1;l++){var v=(p+l)%n.length;if(n[v]===f){o[s]=d[v],t(i[s],d[v],s),p=v+1,u=!0;break}}u||(o[s]=r(c,s))}}o.length=i.length,n=a}}},Object.defineProperty(e,\"__esModule\",{value:!0})});");
	        jsEngine.eval(s.toString());
	        
	        jsEngine.eval(s.toString());
	        
	        if(ScriptsToLoad.IsInitialized()) {
	            for (int i=0;i<ScriptsToLoad.getSize();i++) {
	            	String dependency = jsFolder + (String) ScriptsToLoad.Get(i);
	                FileReader dependencyFile = new FileReader(dependency);
	                jsEngine.eval(dependencyFile);
	            }
	        }
	        if (HasScript) {	
	        	jsEngine.eval(script);
	        	
	        	s = new StringBuilder();
	 	        for (Entry<String,String> entry: ABMaterial.SavedB4JSClasses.entrySet()) {
	 	        	if (!entry.getKey().equals("b4js")) {
	 	        		String className = entry.getKey();
	 	        		s.append("if (_b4jsclasses[\"b4jspagekey" + className.toLowerCase() + "\"] === undefined) {");
	 	        		s.append("_b4jsclasses[\"b4jspagekey" + className.toLowerCase() + "\"] = new b4js_" + className.toLowerCase() + "();");
	 	        		s.append("_b4jsclasses[\"b4jspagekey" + className.toLowerCase() + "\"].initializeb4js();");
	 	        		s.append("}");
	 	        	}
	 	        }
	 	        jsEngine.eval(s.toString());
	        }
		} catch (ScriptException | IOException e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * Pass me = null if not an ABM page class
	 * 
	 * Returns the message id 
	 */
	public String Broadcast(anywheresoftware.b4a.objects.collections.Map cachedPages, B4AClass me, String toPageClassName, ABMBroadcastMessage mymessage, boolean AlsoToMe, BA ba) throws Exception {
		mymessage.mID = UUID.randomUUID().toString();
		
		toPageClassName = toPageClassName.toLowerCase();
		final anywheresoftware.b4a.BA.IterableList keys = cachedPages.Keys();
		final int keyLen = keys.getSize();		
		for (int i = 0;i < keyLen ;i++){
			String key = (String) keys.Get(i);
			if (!key.contains("_ABMWSField")) {
				B4AClass cachedClass = (B4AClass) cachedPages.Get(key);
				if (cachedClass.getClass().getSimpleName().equals(toPageClassName)) {
					if (me!=null) {
						if (cachedClass==me) {
							if (AlsoToMe) {
								anywheresoftware.b4a.keywords.Common.CallSubDelayed2(ba,cachedClass,"Page_IncomingBroadcast",(Object)(mymessage));
							}
						} else {
							anywheresoftware.b4a.keywords.Common.CallSubDelayed2(ba,cachedClass,"Page_IncomingBroadcast",(Object)(mymessage));
						}
					} else {
						anywheresoftware.b4a.keywords.Common.CallSubDelayed2(ba,cachedClass,"Page_IncomingBroadcast",(Object)(mymessage));
					}
				}
			}
		}
		return mymessage.getID();
	}
	
	public String MakeRESTApiCall(String method, String restUrl, String jsonBody, anywheresoftware.b4a.objects.collections.Map headers) {
			HttpsURLConnection urlConnection = null;
			try {
			  URL url = new URL(restUrl);
			  urlConnection = (HttpsURLConnection) url.openConnection();

			  urlConnection.setConnectTimeout(20000);
			  urlConnection.setReadTimeout(20000);
			  urlConnection.setDoOutput(true);
			  urlConnection.setRequestMethod(method);
			  
			  final anywheresoftware.b4a.BA.IterableList keys = headers.Keys();
			  final int keyLen = keys.getSize();		
			  for (int i = 0;i < keyLen ;i++){
					String key = (String) keys.Get(i);
					String value = (String) headers.Get(key);
					urlConnection.setRequestProperty(key, value);
			  }
			  urlConnection.connect();

			  byte[] outputBytes = jsonBody.getBytes("UTF-8");
			  OutputStream out = urlConnection.getOutputStream();
			  out.write(outputBytes);
			  out.flush();
			  out.close();

			  int statusCode = urlConnection.getResponseCode();
			  String tmpCode="";
			  if (statusCode != HttpURLConnection.HTTP_OK) {
			    BA.Log("MakeRESTApiCall: connection failed: statusCode: " + statusCode);
			    tmpCode = "connection failed -> statusCode: " + statusCode + "\n";
			  }

			  InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			  String responseText = convertInputStreamToString(in);
			  return tmpCode + responseText;

			} catch (IOException e) {
			  e.printStackTrace();
			} finally {
			  if (urlConnection != null) {
			    urlConnection.disconnect();
			  }
			}
			return "";
	}
	
	private static String convertInputStreamToString(InputStream is) {
        
        BufferedReader br = null;
        StringBuilder sbContent = new StringBuilder();
        
        try{
            
            /*
             * Create BufferedReader from InputStreamReader 
             */
            br = new BufferedReader(new InputStreamReader(is));
            
            /*
             * read line by line and append content to
             * StringBuilder
             */
            String strLine = null;
            boolean isFirstLine = true;
            
            while( (strLine = br.readLine()) != null){
                if(isFirstLine)
                    sbContent.append(strLine);
                else
                    sbContent.append("\n").append(strLine);
                
                /*
                 * Flag to make sure we don't append new line
                 * before the first line. 
                 */
                isFirstLine = false;
            }
            
        }catch(IOException ioe){
            ioe.printStackTrace();
        }finally{            
            try{
                if(br  != null)
                    br.close();
            }catch(Exception e){ e.printStackTrace();  }
        }
        
        //convert StringBuilder to String and return
        return sbContent.toString();
    }
	
	public HttpSessionWrapper GetSession(WebSocket ws, int SessionMaxInactiveIntervalSeconds) {
		HttpSessionWrapper session =  ws.getUpgradeRequest().GetSession();	
		session.setMaxInactiveInterval(SessionMaxInactiveIntervalSeconds);
		return session;
	}
		
	public static void RemoveMeFromCache(anywheresoftware.b4a.objects.collections.Map cachedPages, String pageID) {
		if (pageID.length()>0) {
			if (cachedPages.IsInitialized()) {
				cachedPages.Remove(pageID + "_ABMWSField");
				cachedPages.Remove(pageID);
			}
		}
	}	
	
	public static boolean IsUrlWellFormatted(String url ) {
		try {
	         URL obj = new URL(url);
	         obj.toURI();
	         return true;
	      } catch (MalformedURLException e) {
	         return false;
	      } catch (URISyntaxException e) {
	         return false;
	      }
	}
			
	public static void UpdateFromCache(B4AClass me, anywheresoftware.b4a.objects.collections.Map cachedPages, String pageID, WebSocket ws, BA ba) {
		UpdateFromCacheDebug(me, cachedPages, pageID, ws, ba, false);
	}
	
	public static void UpdateFromCacheDebug(B4AClass me, anywheresoftware.b4a.objects.collections.Map cachedPages, String pageID, WebSocket ws, BA ba, boolean Debug) {		
		ABMPage page=null;
		String wsFieldName="";
		ServletRequestWrapper req=null;
		try {
			if (ws!=null) {
				if (ws.getSession()!=null) {
					ws.getSession().SetAttribute("_ABMActive", "1");
				}
				req = ws.getUpgradeRequest();					
			}
		} catch (SecurityException | IllegalArgumentException | IllegalStateException e) {		
			//e.printStackTrace();
		}
		
		BA.Log("UpdateFromCache: " + pageID);
		if (cachedPages.ContainsKey(pageID)) {
			BA.Log("Reading from cache");
			B4AClass cachedClass = (B4AClass) cachedPages.Get(pageID);
			Field[] fieldsA = cachedClass.getClass().getDeclaredFields();
			for (Field classFieldA : fieldsA) {
				if (classFieldA!=null) {
					Field classFieldB;
					try {
						classFieldB = me.getClass().getDeclaredField(classFieldA.getName());
					} catch (NoSuchFieldException e3) {	
						if (Debug) {
							e3.printStackTrace();
						}
						return;
					} catch (SecurityException e3) {
						if (Debug) {
							e3.printStackTrace();
						}
						return;
					}
					if (Debug) {
						BA.Log("Checking: " + classFieldA.getName() + " " + Modifier.toString(classFieldA.getModifiers()) + " " + classFieldA.getType().getName());
					}
					switch (classFieldA.getType().getSimpleName().toLowerCase()) {
					case "websocket":
						if (Debug) {
							BA.Log("Found websocket");
						}
						try {
							classFieldA.setAccessible(true);
							classFieldB.setAccessible(true);
							classFieldB.set(me, ws);
							wsFieldName=classFieldA.getName();
							if (Debug) {
								BA.Log("Setting: " + classFieldA.getName() + " " + ws);
							}
						} catch (IllegalArgumentException | IllegalAccessException e2) {
							BA.Log("Failed Setting: " + classFieldA.getName() + " " + Modifier.toString(classFieldA.getModifiers()) + " " + classFieldA.getType().getSimpleName());
							//e2.printStackTrace();
						}
						
						break;
					case "abmpage":
						if (Debug) {
							BA.Log("Found abmpage");
						}
						try {
							classFieldA.setAccessible(true);
							if (classFieldA.get(cachedClass)!=null) {
								classFieldB.setAccessible(true);
								page = (ABMPage) classFieldA.get(cachedClass);
								page.ComesFromPageCache = true;
								if (Debug) {
									BA.Log("Setting old abmpage back");
								}
								page.SetWebsocket(ws);	
								page.mPageID = pageID;
								page.caller = me;
								page.CheckDevice(req);
								classFieldB.set(me, page);
								if (Debug) {
									BA.Log("Restoring: " + classFieldA.getName() + " " + classFieldA.get(cachedClass));
								}
							} else {
								if (Debug) {
									BA.Log("problem setting old abmpage back");
								}
							}
						} catch (IllegalArgumentException | IllegalAccessException e2) {
							BA.Log("Failed Restoring: " + classFieldA.getName() + " " + Modifier.toString(classFieldA.getModifiers()) + " " + classFieldA.getType().getSimpleName());
							//e2.printStackTrace();
						}
						break;
					case "abmaterial":
						break;
					case "common":					
						break;
					default:
						String packageName = BA.packageName.toLowerCase();
						switch (classFieldA.getName().toLowerCase()) {
						case "htsubs":
							break;						
						default:
							if (classFieldA.getType().getName().toLowerCase().startsWith(packageName)) {
								try {
									@SuppressWarnings("rawtypes")
									Class c = Class.forName(classFieldA.getType().getName());
									boolean HasHtSubs=false;
									try {
										c.getDeclaredField("htSubs");
										HasHtSubs=true;
									} catch (NoSuchFieldException nsfe) {
									       // intentionally ignored								    	
									}
									if (classFieldA.getType().getName().contains("$_")) {
										HasHtSubs=true;
									}
									if (HasHtSubs) {
										try {
											classFieldA.setAccessible(true);
											if (classFieldA.get(cachedClass)!=null) {
												classFieldB.setAccessible(true);										
												classFieldB.set(me, classFieldA.get(cachedClass));
												if (Debug) {
													BA.Log("Restoring: " + classFieldA.getName() + " " + classFieldA.get(cachedClass));
												}
											} else {
												if (Debug) {
													BA.Log("Problem setting back: " + classFieldA.getName() + " " + classFieldA.get(cachedClass));
												}
											}
											
										} catch (IllegalArgumentException e) {
											BA.Log("Failed Restoring: " + classFieldA.getName() + " " + Modifier.toString(classFieldA.getModifiers()) + " " + classFieldA.getType().getSimpleName());
											//e.printStackTrace();
										} catch (IllegalAccessException e) {
											BA.Log("Failed Restoring: " + classFieldA.getName() + " " + Modifier.toString(classFieldA.getModifiers()) + " " + classFieldA.getType().getSimpleName());
											//e.printStackTrace();
										}
									}
								} catch (ClassNotFoundException e1) {
									e1.printStackTrace();
								}
								
							} else {
								try {
									classFieldA.setAccessible(true);									
									if (classFieldA.get(cachedClass)!=null) {
										classFieldB.setAccessible(true);
										classFieldB.set(me, classFieldA.get(cachedClass));
										if (Debug) {
											BA.Log("Restoring: " + classFieldA.getName() + " " + classFieldA.get(cachedClass));
										}
									} else {
										if (Debug) {
											BA.Log("Problem setting back: " + classFieldA.getName() + " " + classFieldA.get(cachedClass));
										}
									}
									
								} catch (IllegalArgumentException e) {
									BA.Log("Failed Restoring: " + classFieldA.getName() + " " + Modifier.toString(classFieldA.getModifiers()) + " " + classFieldA.getType().getSimpleName());
									//e.printStackTrace();
								} catch (IllegalAccessException e) {
									BA.Log("Failed Restoring: " + classFieldA.getName() + " " + Modifier.toString(classFieldA.getModifiers()) + " " + classFieldA.getType().getSimpleName());
									//e.printStackTrace();
								}
							}
						}
						
					}
				}
				
			}	
			
			
			
		} else {			
			BA.Log("Saving the first instance");
			Field[] fieldsA = me.getClass().getDeclaredFields();			
			for (Field classFieldA : fieldsA) {
				if (classFieldA!=null) {
					switch (classFieldA.getType().getSimpleName().toLowerCase()) {
					case "websocket":
						if (Debug) {
							BA.Log("Found websocket first instance");
						}
						try {
							classFieldA.setAccessible(true);
							classFieldA.set(me, ws);
							wsFieldName=classFieldA.getName();
						} catch (IllegalArgumentException | IllegalAccessException e2) {
							BA.Log("Failed Setting: " + classFieldA.getName() + " " + Modifier.toString(classFieldA.getModifiers()) + " " + classFieldA.getType().getSimpleName());
							//e2.printStackTrace();
						}
						
						break;
					case "abmpage":
						if (Debug) {
							BA.Log("Found abmpage first instance");
						}
						try {
							classFieldA.setAccessible(true);
							page = (ABMPage) classFieldA.get(me);
							page.ComesFromPageCache = false;
							if (Debug) {
								BA.Log("Setting old abmpage back first instance");
							}
							page.SetWebsocket(ws);		
							page.mPageID = pageID;
							page.caller = me;		
							page.CheckDevice(req);
							classFieldA.set(me, page);
						} catch (IllegalArgumentException | IllegalAccessException e2) {
							BA.Log("Failed setting: " + classFieldA.getName() + ".Websocket " + Modifier.toString(classFieldA.getModifiers()) + " " + classFieldA.getType().getSimpleName());
							//e2.printStackTrace();
						}
						break;
					}
				}
				
			}			
			
		}
		cachedPages.Put(pageID, me);
		cachedPages.Put(pageID + "_ABMWSField", wsFieldName);
	}
	
	/**
	 * NOTE: There are reported issues with OSX (this is an OSX bug/limitation, NOT an ABMaterial one!)
	 * This is useful for ABMPivot before using the Export methods.
	 */
	public boolean IsBrowserFileSaveSupported(ABMPage page) {
		if (page.ws!=null) {
			StringBuilder s = new StringBuilder();
			s.append(")try {");
			s.append("var isFileSaverSupported = !!new Blob;");
			s.append("return true;");
			s.append("} catch (e) {");
			s.append("return false;");
			s.append("}");
			SimpleFuture j = page.ws.RunFunctionWithResult(s.toString(), null);			
			if (j!=null) {
				try {
					return (boolean) j.getValue();				
				} catch (InterruptedException e) {
					//e.printStackTrace();
				} catch (TimeoutException e) {
					//e.printStackTrace();
				} catch (ExecutionException e) {
					//e.printStackTrace();
				} catch (IOException e) {
					//e.printStackTrace();
				}				
			}
		}
		return false;
	}
	
	/**
	 * Tries to force a global scaling of the font size depending on the screen of the device  
	 */
	public void SetFontSizePercentages(int small, int medium, int large, int extraLarge) {
		FontPercentSmall=small;
		FontPercentMedium=medium;
		FontPercentLarge=large;
		FontPercentExtraLarge=extraLarge;
	}
		
	/**
	 * path: e.g. http://localhost:51042/demo/
	 * zoomLevel (between 100 and 500, in 20 increments) 
	 */
	public void ViewerOpenAllDevices(String path, int zoomLevel) {
		zoomLevel = (zoomLevel + 19) / 20 * 20;
		if (zoomLevel<100 || zoomLevel>500) {
			zoomLevel = 100;
		}
		try {
			if (anywheresoftware.b4a.objects.streams.File.Exists(anywheresoftware.b4a.objects.streams.File.getDirApp(), "viewer.html")) {
				anywheresoftware.b4a.objects.streams.File.Delete(anywheresoftware.b4a.objects.streams.File.getDirApp(), "viewer.html");
			}
			try {
				ExportResource("resources/viewer/", "viewer.html", anywheresoftware.b4a.objects.streams.File.getDirApp());
			} catch (Exception e) {
				e.printStackTrace();
			}
			Path p = Paths.get(anywheresoftware.b4a.objects.streams.File.Combine(anywheresoftware.b4a.objects.streams.File.getDirApp(), "viewer.html"));
			Charset charset = StandardCharsets.UTF_8;

			String content = new String(Files.readAllBytes(p), charset);
			content = content.replaceAll("==URL==", path);
			content = content.replaceAll("==ZOOM==", "" + zoomLevel);
			content = content.replaceAll("==DEVICE==", "all");
			Files.write(p, content.getBytes(charset));
		} catch (IOException e) {
			BA.LogError("Unable to write the viewer.html file!");
		}
		OpenInBrowser( anywheresoftware.b4a.objects.streams.File.getDirApp() + "/viewer.html");
	}
	
	/**
	 * path: e.g. http://localhost:51042/demo/
	 * zoomLevel (between 100 and 500, in 20 increments
	 * device: Use the ABM.VIEWER_ constants
	 */
	public void ViewerOpenDevice(String path, int zoomLevel, String device) {	
		zoomLevel = (zoomLevel + 19) / 20 * 20;
		if (zoomLevel<100 || zoomLevel>500) {
			zoomLevel = 100;
		}
				
		try {
			if (anywheresoftware.b4a.objects.streams.File.Exists(anywheresoftware.b4a.objects.streams.File.getDirApp(), "viewer.html")) {
				anywheresoftware.b4a.objects.streams.File.Delete(anywheresoftware.b4a.objects.streams.File.getDirApp(), "viewer.html");
			}
			try {
				ExportResource("resources/viewer/", "viewer.html", anywheresoftware.b4a.objects.streams.File.getDirApp());
			} catch (Exception e) {
				e.printStackTrace();
			}
			Path p = Paths.get(anywheresoftware.b4a.objects.streams.File.Combine(anywheresoftware.b4a.objects.streams.File.getDirApp(), "viewer.html"));
			Charset charset = StandardCharsets.UTF_8;

			String content = new String(Files.readAllBytes(p), charset);
			content = content.replaceAll("==URL==", path);
			content = content.replaceAll("==ZOOM==", "" + zoomLevel);
			content = content.replaceAll("==DEVICE==", device);
			Files.write(p, content.getBytes(charset));
		} catch (IOException e) {
			BA.LogError("Unable to write the viewer.html file!");
		}
		OpenInBrowser(anywheresoftware.b4a.objects.streams.File.getDirApp() + "/viewer.html");
	}
	
	@Hide
	protected void ExportResource(String path, String resourceName, String pageDir) throws Exception {
        InputStream stream = null;
        java.io.OutputStream resStreamOut = null;
        try {
            stream = this.getClass().getClassLoader().getResourceAsStream(path + resourceName);//note that each / is a directory down in the "jar tree" been the jar the root of the tree
            if(stream == null) {
            	BA.LogError("Cannot get resource \"" + path + resourceName + "\" from ABMaterial library file.");
            	return;	                
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            resStreamOut = new FileOutputStream(pageDir + File.separator + resourceName);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (Exception ex) {
        	ex.printStackTrace();
        	return;
        } finally {
            stream.close();
            resStreamOut.close();
        }	
    }
		
	protected static void DeleteFilesForPathByPrefix(final String searchPath, final String prefix) {
		Path path = Paths.get(searchPath);
	    try (DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(path, prefix + "*")) {
	        for (final Path newDirectoryStreamItem : newDirectoryStream) {
	            Files.delete(newDirectoryStreamItem);
	        }
	    } catch (final Exception e) { // empty
	    }
	}
	
	protected static void CopyFileUsingStream(File source, File dest) throws IOException {
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
	}
	
	protected static String encrypt(String property) throws GeneralSecurityException, UnsupportedEncodingException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(hp));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(hs, 20));
        return base64Encode(pbeCipher.doFinal(property.getBytes("UTF-8")));
    }

    protected static String base64Encode(byte[] bytes) {
        // NB: This class is internal, and you probably should use another impl
        return Base64.getEncoder().encodeToString(bytes);
    }

    @SuppressWarnings("unused")
	private static String decrypt(String property) throws GeneralSecurityException, IOException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(hp));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(hs, 20));
        return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
    }

    private static byte[] base64Decode(String property) throws IOException {
        // NB: This class is internal, and you probably should use another impl
        return Base64.getDecoder().decode(property);
    }
	
	/**
	 * path: e.g. http://localhost:51042/demo/ 
	 */
	public void ViewerOpen(String path) {
		OpenInBrowser(path);
	}
	
	@Hide
	protected void OpenInBrowser(String url) {
		String os = System.getProperty("os.name").toLowerCase();
        Runtime rt = Runtime.getRuntime();

	try{
		BA.Log("Viewer running on os: " + os);
		BA.Log("Trying to open: " + url);
	    if (os.indexOf( "win" ) >= 0) {

	        // this doesn't support showing urls in the form of "page.html#nameLink"
	        rt.exec( "rundll32 url.dll,FileProtocolHandler " + url);

	    } else if (os.indexOf( "mac" ) >= 0) {

	        rt.exec( "open " + url);

            } else if (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0) {

	        // Do a best guess on unix until we get a platform independent way
	        // Build a list of browsers to try, in this order.
	        String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
	       			             "netscape","opera","links","lynx"};

	        // Build a command string which looks like "browser1 "url" || browser2 "url" ||..."
	        StringBuffer cmd = new StringBuffer();
	        for (int i=0; i<browsers.length; i++)
	            cmd.append( (i==0  ? "" : " || " ) + browsers[i] +" \"" + url + "\" ");

	        rt.exec(new String[] { "sh", "-c", cmd.toString() });

           } else {
                return;
           }
       }catch (Exception e){
    	   BA.LogError("Unable to open viewer page. ");
    	   e.getStackTrace();
	    return;
       }
      return;
	}
		
	public static ZAbstractDesignerDefinition ZABDEFCreateRow() {		
        ABMRow r = new ABMRow();
		r.ZABInit();
		return r.ZABDEF;
	}

	public static ZAbstractDesignerDefinition ZABDEFCreateCell() {
        ABMCell c = new ABMCell();
		c.ZABInit();
		return c.ZABDEF;
	}
	
	protected static String GetConst(String name) throws NoSuchFieldException, SecurityException {
		Field f = ABMaterial.class.getField(name.toUpperCase());
		if (f!=null) {
			Class<?> t = f.getType();
			try {
				if(t == int.class){
					return String.valueOf(f.getInt(null));
				}else if(t == String.class){
					return (String) (f.get(null));
				}
			} catch (IllegalArgumentException e) {
				//e.printStackTrace();
			} catch (IllegalAccessException e) {
				//e.printStackTrace();
			}		
		}
		return "";
	}
	
	public static String B4JSGetConst(String name) throws NoSuchFieldException, SecurityException {
		Field f = ABMaterial.class.getField(name.toUpperCase());
		if (f!=null) {
			Class<?> t = f.getType();
			try {
				if(t == int.class){
					return String.valueOf(f.getInt(null));
				}else if(t == String.class){
					return "\"" + (String) (f.get(null)) + "\"";
				}
			} catch (IllegalArgumentException e) {
				//e.printStackTrace();
			} catch (IllegalAccessException e) {
				//e.printStackTrace();
			}		
		}
		return "";
	}
	
	protected static int GetConstInt(String name) throws NoSuchFieldException, SecurityException {
		Field f = ABMaterial.class.getField(name.toUpperCase());
		if (f!=null) {
			Class<?> t = f.getType();
			try {
				if(t == int.class){
					return f.getInt(null);
				}
			} catch (IllegalArgumentException e) {
				//e.printStackTrace();
			} catch (IllegalAccessException e) {
				//e.printStackTrace();
			}		
		}
		return 0;
	}
	
	public String CreateAbsolutePath(String root, String relativePath) {
		File a = new File(root);
		File parentFolder = new File(a.getParent());
		File b = new File(parentFolder, relativePath);
		String absolute="";
		try {
			absolute = b.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return absolute;
	}
	
	public static B4AClass CreateClassForName(String className) {
		try {
			Class<?> clazz = Class.forName(fixClassName(className));
			Method m = getInitializeMethod(clazz);
			return createInstance(clazz, m);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Method getInitializeMethod(Class<?> c) throws NoSuchMethodException, SecurityException {
		Method m = null;
		try {
			m = c.getDeclaredMethod("_initialize", BA.class);
		} catch (NoSuchMethodException e) {
			m = c.getDeclaredMethod("innerInitializeHelper", BA.class);
		}
		return m;
	}
	
	private static B4AClass createInstance(Class<?> handlerClass, Method initializeMethod) throws Exception {
		B4AClass handler = (B4AClass)handlerClass.getDeclaredConstructor().newInstance();		
		initializeMethod.invoke(handler, (Object)null);
		BA ba = handler.getBA();
		if (BA.isShellModeRuntimeCheck(ba)) {
			ba.raiseEvent(null, "initialize", (Object)null);
		}
		return handler;
	}
	
	@Hide
	@RaisesSynchronousEvents
	public void SinglePageAppCreatePage(ABMPage preHandlerPage, String appName, String pageName, WebSocket ws) {		
		String pageClassName="";
		if (pageName.toLowerCase().endsWith(".htm") || pageName.toLowerCase().endsWith(".html") || pageName.toLowerCase().endsWith("/")) {
			int i = pageName.toLowerCase().lastIndexOf("/");
			pageClassName = pageName.substring(0, i);
			i = pageClassName.toLowerCase().lastIndexOf("/");
			if (i>0) {
				pageClassName = pageClassName.substring(i+1);
			}
		} else {
			pageClassName = pageName;
			int i = pageClassName.toLowerCase().lastIndexOf("/");
			if (i>0) {
				pageClassName = pageClassName.substring(i+1);
			}
		}
		
		if (pageClassName.equals(".") || pageClassName.equals("..")) {
			pageClassName = "";
		}
		
		BA.Log("Loading page: " + pageName + " " + pageClassName);
		if (pageClassName.equals("")) {
			pageClassName = appName;
			if (ws!=null) {
				StringBuilder s = new StringBuilder();
				
				s.append("$('main').empty();");
				s.append("document.getElementById(\"pagestyle\").setAttribute(\"href\", \"./" + pageClassName + "." + ABMaterial.getAppVersion() + ".css\");");				
				s.append("var fileref=document.createElement('script');");
				s.append("fileref.setAttribute('type','text/javascript');");
				s.append("fileref.setAttribute('src', \"./" + pageClassName + "." + ABMaterial.getAppVersion() + ".js\");");
				s.append("fileref.setAttribute('id', 'pagescript');");
				s.append("document.getElementById(\"pagescript\").parentNode.replaceChild(fileref,document.getElementById(\"pagescript\"));");				
				
				ws.Eval(s.toString(), null);
				try {
					if (ws.getOpen()) {
						ws.Flush();				
					}
				} catch (IOException e) {			
			
				}
			}		
			if (preHandlerPage.PreHandlerPage==null) {
				preHandlerPage.mCurrentContentPageClass=null;	
				preHandlerPage.mCurrentContentPageClassPageID="";
			} else {
				preHandlerPage.PreHandlerPage.mCurrentContentPageClass=null;
				preHandlerPage.PreHandlerPage.mCurrentContentPageClassPageID="";
			}
			return;
		} else {		
			if (ws!=null) {
				StringBuilder s = new StringBuilder();
				
				s.append("$('main').empty();");
				s.append("document.getElementById(\"pagestyle\").setAttribute(\"href\", \"./" + pageClassName + "/" + pageClassName + "." + ABMaterial.getAppVersion() + ".css\");");				
				s.append("var fileref=document.createElement('script');");
				s.append("fileref.setAttribute('type','text/javascript');");
				s.append("fileref.setAttribute('src', \"./" + pageClassName + "/" + pageClassName + "." + ABMaterial.getAppVersion() + ".js\");");
				s.append("fileref.setAttribute('id', 'pagescript');");
				s.append("document.getElementById(\"pagescript\").parentNode.replaceChild(fileref,document.getElementById(\"pagescript\"));");
								
				ws.Eval(s.toString(), null);
				try {
					if (ws.getOpen()) {
						ws.Flush();				
					}
				} catch (IOException e) {			
			
				}
			}
		}
		B4AClass cls = CreateClassForNameSP(pageClassName,ws);	
		
		ABMPage newPage = FindPageObject(cls);
		if (newPage!=null) {
			if (preHandlerPage.PreHandlerPage==null) {
				preHandlerPage.mCurrentContentPageClass=cls;
				preHandlerPage.mCurrentContentPageClassPageID=newPage.GetPageID();
				newPage.PreHandlerPage = preHandlerPage;
			} else {
				preHandlerPage.PreHandlerPage.mCurrentContentPageClass=cls;
				preHandlerPage.PreHandlerPage.mCurrentContentPageClassPageID=newPage.GetPageID();
				newPage.PreHandlerPage = preHandlerPage.PreHandlerPage;
			}
		}		
	}
		
	protected ABMPage FindPageObject(B4AClass clazz) {
		Field[] fieldsA = clazz.getClass().getDeclaredFields();
		for (Field classFieldA : fieldsA) {
			if (classFieldA!=null) {
				if (classFieldA.getType().getSimpleName().toLowerCase().equals("abmpage")) {
					classFieldA.setAccessible(true);
					try {
						return (ABMPage) classFieldA.get(clazz);						
					} catch (IllegalArgumentException | IllegalAccessException e2) {						
					}
				}
			}
		}
		return null;
	}
	
	private Method getInitializeMethodSP(Class<?> c) throws NoSuchMethodException, SecurityException {
		Method m = null;
		try {
			m = c.getDeclaredMethod("_initialize", BA.class);
		} catch (NoSuchMethodException e) {
			m = c.getDeclaredMethod("innerInitializeHelper", BA.class);
		}
		return m;
	}
	
	private B4AClass CreateClassForNameSP(String className, WebSocket ws) {
		try {
			Class<?> clazz = Class.forName(fixClassName(className));
			Method m = getInitializeMethodSP(clazz);
			return createInstanceSP(clazz, m, ws);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	private B4AClass createInstanceSP(Class<?> handlerClass, Method initializeMethod, WebSocket ws) throws Exception {
		B4AClass handler = (B4AClass)handlerClass.getDeclaredConstructor().newInstance();		
		initializeMethod.invoke(handler, (Object)null);
		

		BA ba = handler.getBA();
		if (BA.isShellModeRuntimeCheck(ba)) {
			ba.raiseEvent(null, "initialize", (Object)null);
		}
		anywheresoftware.b4a.keywords.Common.CallSubNew2(ba, handler, "websocket_connected", ws);
		return handler;
	}

	
	
	private static String fixClassName(String clazz) {
		String className = clazz.toLowerCase(BA.cul);
		if (className.contains(".") == false)
			className = BA.packageName + "." + className;
		return className;
	}
				
	protected static void SetSavedNeeds(ABMPage page) {		
		String currNeeds = SavedNeeds.getOrDefault(page.Name, "");
		String[] parts = currNeeds.split(";");
		for (String p: parts) {
			switch (p) {
			case "NeedsCodeLabel":
				page.NeedsCodeLabel=true;
    			break;
    		case "NeedsYouTube":
    			page.NeedsYouTube=true;
    			break;
    		case "NeedsGoogleMap":
    			page.NeedsGoogleMap=true;
    			break;
    		case "NeedsCanvas":
    			page.NeedsCanvas=true;
    			break;
    		case "NeedsInput":
    			page.NeedsInput=true;
    			break;
    		case "NeedsFileInput":
    			page.NeedsFileInput=true;
    			break;
    		case "NeedsRadio":
    			page.NeedsRadio=true;
    			break;
    		case "NeedsTabs":
    			page.NeedsTabs=true;
    			break;
    		case "NeedsTable":
    			page.NeedsTable=true;
    			break;
    		case "NeedsJQueryUI":
    			page.NeedsJQueryUI=true;
    			break;    	
    		case "NeedsSortingTable":
    			page.NeedsSortingTable=true;
    			break;
    		case "NeedsBadge":
    			page.NeedsBadge=true;
    			break;
    		case "NeedsCheckbox":
    			page.NeedsCheckbox=true;
    			break;
    		case "NeedsCombo":
    			page.NeedsCombo=true;
    			break;
    		case "NeedsImageSlider":
    			page.NeedsImageSlider=true;
    			break;
    		case "NeedsTextArea":
    			page.NeedsTextArea=true;
    			break;
    		case "NeedsSwitch":
    			page.NeedsSwitch=true;
    			break;
    		case "NeedsUpload":
    			page.NeedsUpload=true;
    			break;
    		case "NeedsHTML5Video":
    			page.NeedsHTML5Video=true;
    			break;
    		case "NeedsChips":
    			page.NeedsChips=true;
    			break;
    		case "NeedsActionButton":
    			page.NeedsActionButton=true;
    			break;
    		case "NeedsLists":
    			page.NeedsLists=true;
    			break;
    		case "NeedsCards":
    			page.NeedsCards=true;
    			break;
    		case "NeedsParallax":
    			page.NeedsParallax=true;
    			break;
    		case "NeedsSignature":
    			page.NeedsSignature=true;
    			break;
    		case "NeedsChart":
    			page.NeedsChart=true;
    			break;
    		case "NeedsOAuth":
    			page.NeedsOAuth=true;
    			break;
    		case "NeedsCalendar":
    			page.NeedsCalendar=true;
    			break;
    		case "NeedsMask":
    			page.NeedsMask=true;
    			break;
    		case "NeedsTreeTable":
    			page.NeedsTreeTable=true;
    			break;
    		case "NeedsPagination":
    			page.NeedsPagination=true;
    			break;
    		case "NeedsSlider":
    			page.NeedsSlider=true;
    			break;
    		case "NeedsRange":
    			page.NeedsRange=true;
    			break;
    		case "NeedsDateTimeScroller":
    			page.NeedsDateTimeScroller=true;
    			break;
    		case "NeedsDateTimePicker":
    			page.NeedsDateTimePicker=true;
    			break;
    		case "NeedsEditor":
    			page.NeedsEditor=true;
    			break;    	
    		case "NeedsSocialShare":
    			page.NeedsSocialShare=true;
    			break;
    		case "NeedsPDFViewer":
    			page.NeedsPDFViewer=true;
    			break;
    		case "NeedsPlatform":
    			page.NeedsPlatform=true;
    			break;
    		case "NeedsAudioPlayer":
    			page.NeedsAudioPlayer=true;
    			break;
    		case "NeedsTimeline":
    			page.NeedsTimeline=true;
    			break;
    		case "NeedsFlexWall":
    			page.NeedsFlexWall=true;
    			break;
    		case "NeedsSVGSurface":
    			page.NeedsSVGSurface = true;
    			break;
    		case "NeedsPatternLock":
    			page.NeedsPatternLock = true;
    			break;
    		case "NeedsChronologyList":
    			page.NeedsChronologyList = true;
    			break;
    		case "NeedsXPlay":
    			page.NeedsXPlay = true;
    			break;
    		case "NeedsCustomCards":
    			page.NeedsCustomCards=true;
    			break;
    		case "NeedsChat":
    			page.NeedsChat=true;
    			break;
    		case "NeedsPlanner":
    			page.NeedsPlanner=true;
    			break;
    		case "NeedsPivot":
    			page.NeedsPivot=true;
    			break;
    		case "NeedsPercentSlider":
    			page.NeedsPercentSlider=true;
    			break;
    		case "NeedsSmartWizard":
    			page.NeedsSmartWizard=true;
    			break;
    		case "NeedsSimpleBar":
    			page.NeedsSimpleBar=true;
    			break;
    		case "NeedsComposer":
    			page.NeedsComposer=true;
    			break; 
    		case "NeedsFileManager":
    			page.NeedsFileManager=true;
    			break; 
    		case "NeedsResponsiveContainer":
    			page.NeedsResponsiveContainer=true;
    			break;
    		case "NeedsB4JS":
    			page.NeedsB4JS=true;
    			break;
			}
			
		}
	}
	
	
	
	public boolean IsCookiesEnabled(WebSocket ws) {
		boolean isEnabled=false;
		if (ws!=null) {
			SimpleFuture j = ws.EvalWithResult("return Cookies.enabled;", null);			
			if (j!=null) {
				try {
					isEnabled = (boolean) j.getValue();				
				} catch (InterruptedException e) {
					//e.printStackTrace();
				} catch (TimeoutException e) {
					//e.printStackTrace();
				} catch (ExecutionException e) {
					//e.printStackTrace();
				} catch (IOException e) {
					//e.printStackTrace();
				}	
			}
		}
		return isEnabled;
	}
	
	
	public void CookieSet(ServletResponseWrapper response, String key, String value, int maxAge, boolean setHttpOnly) {
		Cookie cookie = new Cookie(key,value);
	    cookie.setPath("/");
	    cookie.setMaxAge(maxAge);	    
	    cookie.setHttpOnly(setHttpOnly);	  
	    response.getObject().addCookie(cookie);	 
	}
		
	public String CookieGet(ServletRequestWrapper request, String key) {
		 Cookie[] cookies = request.getObject().getCookies();
		 if (cookies != null) {
		   for (Cookie cookie : cookies) {
		      if (key.equals(cookie.getName())) {
		         return cookie.getValue();
		      }
		   }
		}
		return null;
	}
	
	public void CookieRemove(ServletResponseWrapper response, String key) {
	    CookieSet(response, key, null, 0, true);
	}
	
	public void LoadTranslations(String dirToLanguageFiles) {
		File folder = new File(dirToLanguageFiles);
		if (!folder.exists()) {
			try {
				Files.createDirectories(Paths.get(dirToLanguageFiles));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File[] files = folder.listFiles(new FileFilter() {
    	    @Override
    	    public boolean accept(File pathname) {
    	        String name = pathname.getName().toLowerCase();
    	        return name.endsWith(".lng") && pathname.isFile();
    	    }
    	});
		Arrays.sort(files);
		for (File f: files) {
			String fn = f.getName();

			String langName = Left(fn,fn.length()-4);
			if (Trans.containsKey(langName.toLowerCase())) {
				continue;
			}
			Map<String, String> tmpTrans = new LinkedHashMap<String, String>();
			if (langName.indexOf("_")>-1) {
				String tmpLangName = langName.substring(0, langName.indexOf("_"));
				tmpTrans = Trans.getOrDefault(tmpLangName.toLowerCase(),new LinkedHashMap<String, String>());
			}	
			BufferedReader br=null;
			try {
				String sCurrentLine;
				br = new BufferedReader(new FileReader(f));
				while ((sCurrentLine = br.readLine()) != null) {
					String[] parts = sCurrentLine.split(";");
					switch (parts.length) {
					case 0:
						break;
					case 1: 
						tmpTrans.put(parts[0], "");
						break;
					case 2: 
						tmpTrans.put(parts[0], parts[1]);
						break;
					default:
						String[] tmpArray = Arrays.copyOfRange(parts, 1, parts.length);
						String joined = String.join(";", tmpArray);
						tmpTrans.put(parts[0], joined);
						break;
					}
				}
			} catch (IOException e) {
	        	e.printStackTrace();
	        } finally {
	        	try {
	        		if (br != null)br.close();
	        	} catch (IOException ex) {
	        		ex.printStackTrace();
	        	}
	        }
			Trans.put(langName.toLowerCase(), tmpTrans);
		}
	}
	
	public void LoadDefaults(String dirToDefaultFiles) {
		File folder = new File(dirToDefaultFiles);
		if (!folder.exists()) {
			try {
				Files.createDirectories(Paths.get(dirToDefaultFiles));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File[] files = folder.listFiles(new FileFilter() {
    	    @Override
    	    public boolean accept(File pathname) {
    	        String name = pathname.getName().toLowerCase();
    	        return name.endsWith(".def") && pathname.isFile();
    	    }
    	});
		Arrays.sort(files);
		for (File f: files) {
			String fn = f.getName();
			String defName = Left(fn,fn.length()-4);
			if (Defs.containsKey(defName.toLowerCase())) {
				continue;
			}
			Map<String, String> tmpDefs = new LinkedHashMap<String, String>();
			if (defName.indexOf("_")>-1) {
				String tmpDefName = defName.substring(0, defName.indexOf("_"));
				tmpDefs = Defs.getOrDefault(tmpDefName.toLowerCase(),new LinkedHashMap<String, String>());
			}	
			BufferedReader br=null;
			try {
				String sCurrentLine;
				br = new BufferedReader(new FileReader(f));
				while ((sCurrentLine = br.readLine()) != null) {
					String[] parts = sCurrentLine.split(";");
					switch (parts.length) {
					case 0:
						break;
					case 1: 
						tmpDefs.put(parts[0], "");
						break;
					case 2: 
						tmpDefs.put(parts[0], parts[1]);
						break;
					default:
						String[] tmpArray = Arrays.copyOfRange(parts, 1, parts.length);
						String joined = String.join(";", tmpArray);
						tmpDefs.put(parts[0], joined);
						break;
					}
				}
			} catch (IOException e) {
	        	e.printStackTrace();
	        } finally {
	        	try {
	        		if (br != null)br.close();
	        	} catch (IOException ex) {
	        		ex.printStackTrace();
	        	}
	        }
			Defs.put(defName.toLowerCase(), tmpDefs);
		}
	}
	
	@Hide
	protected Map<String,String> GetUsedModules(File fn) {
		Map<String,String> files = new LinkedHashMap<String, String>();
		BufferedReader br=null;
		try {
			String s;
			br = new BufferedReader(new FileReader(fn));
			StringBuilder allS = new StringBuilder(); 
			while ((s = br.readLine()) != null) {
				String sEx = s.toLowerCase();
				if (sEx.contains("@endofdesigntext@")) {
					br.close();
					return files;
				}
				if (sEx.startsWith("module")) {
					int i = sEx.indexOf("=");
					sEx = s.substring(i+1);
					files.put(sEx, "extract");
				}
				//s = s.replace("\"\"", "\\\"");
				allS.append(s + " \n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}	
		return files;
	}
	
	@Hide
	public void CheckApp() {
		useNeeds.put("abmcodelabel", "NeedsCodeLabel");
		useNeeds.put("abmvideo", "NeedsYouTube;NeedsHTML5Video");
		useNeeds.put("abmgooglemap", "NeedsGoogleMap");
		useNeeds.put("abmcanvas", "NeedsCanvas");
		useNeeds.put("abminput", "NeedsInput;NeedsTextArea");
		useNeeds.put("inputmask", "NeedsMask");
		useNeeds.put("abmradiogroup", "NeedsRadio");
		useNeeds.put("abmtabs", "NeedsTabs");
		useNeeds.put("abmtable", "NeedsTable;NeedsSortingTable");
		useNeeds.put("abmtablemutable", "NeedsTable;NeedsSortingTable");
		useNeeds.put("abmtableinfinite", "NeedsTable;NeedsSortingTable;NeedsJQueryUI");
		useNeeds.put("abmbadge", "NeedsBadge");
		useNeeds.put("abmcheckbox", "NeedsCheckbox");
		useNeeds.put("abmcombo", "NeedsCombo");
		useNeeds.put("abmimageslider", "NeedsImageSlider");
		useNeeds.put("abmswitch", "NeedsSwitch");
		useNeeds.put("abmupload", "NeedsUpload");
		useNeeds.put("abmchip", "NeedsChips");    				
		useNeeds.put("abmactionbutton", "NeedsActionButton");
		useNeeds.put("abmlist", "NeedsLists");
		useNeeds.put("abmcard", "NeedsCards");
		useNeeds.put("abmparallax", "NeedsParallax");
		useNeeds.put("abmsignaturepad", "NeedsSignature");
		useNeeds.put("abmchart", "NeedsChart");
		useNeeds.put("abmsocialoauth", "NeedsOAuth");
		useNeeds.put("abmsocialshare", "NeedsSocialShare");
		useNeeds.put("abmcalendar", "NeedsCalendar");
		useNeeds.put("abmtreetable", "NeedsTreeTable");
		useNeeds.put("abmpagination", "NeedsPagination");
		useNeeds.put("abmslider", "NeedsSlider");
		useNeeds.put("abmrange", "NeedsRange");
		useNeeds.put("abmdatetimepicker", "NeedsDateTimePicker");
		useNeeds.put("abmdatetimescroller", "NeedsDateTimeScroller");
		useNeeds.put("abmeditor", "NeedsEditor");    				
		useNeeds.put("abmpdfviewer", "NeedsPDFViewer");
		useNeeds.put("abmplatform", "NeedsPlatform");
		useNeeds.put("abmaudioplayer", "NeedsAudioPlayer");
		useNeeds.put("abmtimeline", "NeedsTimeline");
		useNeeds.put("abmflexwall", "NeedsFlexWall");
		useNeeds.put("abmsvgsurface", "NeedsSVGSurface");
		useNeeds.put("abmfileinput", "NeedsInput;NeedsUpload;NeedsFileInput");
		useNeeds.put("abmpatternlock", "NeedsPatternLock");
		useNeeds.put("abmchronologylist", "NeedsChronologyList");
		useNeeds.put("abmxplay", "NeedsXPlay");
		useNeeds.put("abmcustomcard", "NeedsCustomCards");
		useNeeds.put("abmchat", "NeedsChat");
		useNeeds.put("abmplanner", "NeedsPlanner");
		useNeeds.put("abmpivottable", "NeedsPivot");
		useNeeds.put("abmpercentslider", "NeedsPercentSlider");
		useNeeds.put("abmsmartwizard", "NeedsSmartWizard");
		useNeeds.put("abmmmenu", "NeedsMMenu");
		useNeeds.put("abmmhead", "NeedsMHead");
		useNeeds.put("abmcomposer", "NeedsComposer");
		useNeeds.put("abmfilemanager", "NeedsFileManager");
		useNeeds.put("usealternativescrollbar", "NeedsSimpleBar");
		useNeeds.put("isresponsivecontainer", "NeedsResponsiveContainer");
		useNeeds.put("b4jsuniquekey", "NeedsB4JS");
		
		res = new ExtractResult();
		
		String dirApp = anywheresoftware.b4a.objects.streams.File.getDirApp();
		File JarFolder = new File(dirApp);  // Objects
		File SourceFolder=null;
		SourceFolder = new File(dirApp);
		SourceFolder = new File(SourceFolder.getParent()); // .bas files
    	String dir = JarFolder.getAbsolutePath();
    	File buildFile = new File(dir, "BaseTranslations" + ".lng");
    	if (buildFile.exists()) {
    		buildFile.delete();
    	}
    	buildFile = new File(dir, "BaseTranslations" + ".optimizable");
    	if (buildFile.exists()) {
    		buildFile.delete();
    	}
    	buildFile = new File(dir, "BaseDefaults" + ".def");
    	if (buildFile.exists()) {
    		buildFile.delete();
    	}
    	buildFile = new File(dir, "BaseDefaults" + ".optimizable");
    	if (buildFile.exists()) {
    		buildFile.delete();
    	}
    	buildFile = new File(dir, "B4JAnalyse" + ".log");
    	if (buildFile.exists()) {
    		buildFile.delete();
    	}
    			
		if (SourceFolder.exists() && !DebugSkipCheckSourceFolder && BA.debugMode) {
			HasSourceFolder = true;
			Map<String,File> OrderedFiles = new LinkedHashMap<String, File>();
			Map<String,String> OrderedFilesAllowedNew = new LinkedHashMap<String, String>(); 
			
			File[] files = SourceFolder.listFiles(new FileFilter() {
	    	    @Override
	    	    public boolean accept(File pathname) {
	    	        String name = pathname.getName().toLowerCase();
	    	        return (name.endsWith(".b4j")) && pathname.isFile();
	    	    }
	    	});
	    	if (files!=null) {
	    		if (files.length==0) {
	    			return;
	    		}
	    		File B4JFile=null;
	    		for (File f: files) {
	    			String fn = f.getName();
	    			if (fn.endsWith(".b4j")) {
	    				OrderedFilesAllowedNew = GetUsedModules(f);
	    				B4JFile = f;
	    				break;
	    			}	    				
	    		} 
	    		
	    		boolean MustUpgrade=true;
	    		for (Entry<String,String> entry: OrderedFilesAllowedNew.entrySet()) {	    			
	    			String fName = entry.getKey() + ".bas";
	    			File f=null;
	    			if (fName.startsWith("|relative|")) {
	    				fName = fName.substring(10);
	    				File b = new File(SourceFolder, fName);
	    				try {
							String absolute = b.getCanonicalPath();
							f = new File(absolute);
						} catch (IOException e) {
							e.printStackTrace();
						} // may throw IOException
	    			} else {
	    				if (fName.startsWith("|absolute|")) {
	    					fName = fName.substring(10);
	    					f = new File(fName);
	    				} else {
	    					// in the SourceFolder
	    					f = new File(SourceFolder.getAbsoluteFile().getPath() + "/" + fName);
	    				}
	    			}
	    			String fn = f.getName();
	    			String ModuleName = Left(fn,fn.length()-4);
	    			if (ModuleName.equalsIgnoreCase(SessionCreatorClassV2)) {
	    				CacheSystem="2.0";
	    			}
    				if (ModuleName.equalsIgnoreCase(SessionCacheControlV3)) {
	    				CacheSystem="3.0";
	    				MustUpgrade=false;
	    			}
    				if (TypeFile(f, "StaticCode", "")) {
    					ExtractResultModule esm = new ExtractResultModule("StaticCode", ModuleName);
    					res.Modules.put(ModuleName.toLowerCase(), esm);
    					OrderedFiles.put(ModuleName.toLowerCase(), f);
    				}
	    		}
	    		if (MustUpgrade) {
	    			BA.LogError("----------------------------------------------------------");
	    			BA.LogError("ABM v" + ABMaterial.Version + " requires Cache System 3.0 (yours: " + CacheSystem + ")");
	    			BA.LogError("Upgrade instructions:");
	    			BA.LogError("see the pdf and readme files in the zip.");
	    			BA.LogError("----------------------------------------------------------");
	    		}
	    		for (Entry<String,String> entry: OrderedFilesAllowedNew.entrySet()) {
	    			String fName = entry.getKey() + ".bas";
	    			File f=null;
	    			if (fName.startsWith("|relative|")) {
	    				fName = fName.substring(10);
	    				File b = new File(SourceFolder, fName);
	    				try {
							String absolute = b.getCanonicalPath();
							f = new File(absolute);
						} catch (IOException e) {
							e.printStackTrace();
						} // may throw IOException
	    			} else {
	    				if (fName.startsWith("|absolute|")) {
	    					fName = fName.substring(10);
	    					f = new File(fName);
	    				} else {
	    					// in the SourceFolder
	    					f = new File(SourceFolder.getAbsoluteFile().getPath() + "/" + fName);
	    				}
	    			}
	    			String fn = f.getName();
	    			String ModuleName = Left(fn,fn.length()-4);
	    			
	    			if (TypeFile(f, "Class", "page_parseevent")) {
	    				ExtractResultModule esm = new ExtractResultModule("Class", ModuleName);
	    				res.Modules.put(ModuleName.toLowerCase(), esm);
	    				OrderedFiles.put(ModuleName.toLowerCase(), f);
	    			}	    			
	    		}
	    		for (Entry<String,String> entry: OrderedFilesAllowedNew.entrySet()) {
	    			String fName = entry.getKey() + ".bas";
	    			File f=null;
	    			if (fName.startsWith("|relative|")) {
	    				fName = fName.substring(10);

	    				File b = new File(SourceFolder, fName);
	    				try {
							String absolute = b.getCanonicalPath();
							f = new File(absolute);
						} catch (IOException e) {
							e.printStackTrace();
						} // may throw IOException
	    			} else {
	    				if (fName.startsWith("|absolute|")) {
	    					fName = fName.substring(10);
	    					f = new File(fName);
	    				} else {
	    					// in the SourceFolder
	    					f = new File(SourceFolder.getAbsoluteFile().getPath() + "/" + fName);
	    				}
	    			}
	    			String fn = f.getName();
	    			String ModuleName = Left(fn,fn.length()-4);
	    			
	    			if (TypeFile(f, "Class", "")) {
	    				ExtractResultModule esm = new ExtractResultModule("Class", ModuleName);
	    				res.Modules.put(ModuleName.toLowerCase(), esm);
	    				OrderedFiles.put(ModuleName.toLowerCase(), f);
	    			}	    			
	    		}
	    		ExtractResultModule esm = new ExtractResultModule("Main", "Main");
				res.Modules.put("main", esm);
				OrderedFiles.put("main", B4JFile);
	    		
	    			    		
	    		BA.Log("Start B4J Analyse!");
	    		BA.Log("When an error occurs, check the B4JAnalyse.log file in the Objects folder to see the last B4J line it was working on.");
	    		BA.Log("Collecting data from B4J source files... (1/2)");
	    		
	    		try {
	    			File analyseLogFile = new File(dir, "B4JAnalyse" + ".log");
	    			res.analyseLog = new BufferedWriter(new FileWriter(analyseLogFile, false));
	    		     	
	    		
	    			for (Entry<String,File> entry: OrderedFiles.entrySet()) {	    				
	    				res.Modules.get(entry.getKey()).PrepareModule(entry.getValue(), dir, OrderedFiles, res);
	    				if (res.Modules.get(entry.getKey()).MarkedToDocument.size()>0) {
	    					BufferedWriter writerDoc = null;
	    					try {
	    						buildFile = new File(dir, "DOC_" + entry.getValue().getName().substring(0, entry.getValue().getName().length()-4) + ".txt");
	    						if (buildFile.exists()) {
	    							buildFile.delete();
	    						}
	    						writerDoc = new BufferedWriter(new FileWriter(buildFile, false));
	    						for (int j=0;j<res.Modules.get(entry.getKey()).MarkedToDocument.size();j++) {
	    							writerDoc.write(res.Modules.get(entry.getKey()).MarkedToDocument.get(j) + "\n");
	    						}
				       	
	    					} catch (Exception e) {
	    						e.printStackTrace();
	    					} finally {
	    						try {
	    							writerDoc.close();
	    						} catch (Exception e) {
	    						}
	    					}
	    				}
	    			}
	    		
	    			if (res.oTranslations.size()>0 || res.wrongTranslations.size()>0) {
	    				BufferedWriter writerTrans = null;
	    				try {
	    					buildFile = new File(dir, "BaseTranslations" + ".optimizable");
				       	
	    					writerTrans = new BufferedWriter(new FileWriter(buildFile, true));
	    					writerTrans.write("[Suggestions where you can re-use existing translations]\n");
	    					for (int j=0;j<res.oTranslations.size();j++) {
	    						writerTrans.write(res.oTranslations.get(j));
	    					}
	    					writerTrans.write("[Duplicate use of the same translation code withing a B4J file]\n");
	    					for (Entry<String,List<String>> entry: res.wrongTranslations.entrySet()) {
	    						for (int j=0;j<entry.getValue().size();j++) {
	    							writerTrans.write(entry.getValue().get(j) + "\n");
	    						}
	    					}
	    				} catch (Exception e) {
	    					e.printStackTrace();
	    				} finally {
	    					try {
	    						writerTrans.close();
	    					} catch (Exception e) {
	    					}
	    				} 
	    			}
	    		
	    			if (res.oDefaults.size()>0 || res.wrongDefs.size()>0) {
	    				BufferedWriter writerDefaults = null;
	    				try {
	    					buildFile = new File(dir, "BaseDefaults" + ".optimizable");
				       	
	    					writerDefaults = new BufferedWriter(new FileWriter(buildFile, true));
	    					writerDefaults.write("[Suggestions where you can re-use existing defaults]\n");
	    					for (int j=0;j<res.oDefaults.size();j++) {
	    						writerDefaults.write(res.oDefaults.get(j));
	    					}
	    					writerDefaults.write("[Duplicate use of the same default code withing a B4J file]\n");
	    					for (Entry<String,List<String>> entry: res.wrongTranslations.entrySet()) {
	    						for (int j=0;j<entry.getValue().size();j++) {
	    							writerDefaults.write(entry.getValue().get(j) + "\n");
	    						}
	    					}
	    				} catch (Exception e) {
	    					e.printStackTrace();
	    				} finally {
	    					try {
	    						writerDefaults.close();
	    					} catch (Exception e) {
	    					}
	    				} 
	    			}
	    		
	    			BA.Log("Analysing data from B4J source files... (2/2)");
	    		
	    			for (Entry<String,File> entry: OrderedFiles.entrySet()) {	    				
	    				res.Modules.get(entry.getKey()).CollectNeeds(res, useNeeds, OrderedFiles);
	    			}
	    			for (Entry<String,File> entry: OrderedFiles.entrySet()) {
	    				ExtractResultModule m = res.Modules.get(entry.getKey());
	    				if (m.Needs.size()>0) {
	    					Map<String,String> ExtraNeeds = new LinkedHashMap<String,String>();
	    					for (Entry<String,String> v: m.Needs.entrySet()) {
	    						if (v.getValue().equals("$TOCOLLECT$")) {
	    							ExtractResultModule m2 = res.Modules.get(v.getKey());
	    							String ExtraNeedsStr="";
	    							for (Entry<String,String> v2: m2.Needs.entrySet()) {
	    								ExtraNeedsStr+=";" + v2.getValue();
	    							}
	    							if (!ExtraNeedsStr.equals("")) {
	    								ExtraNeedsStr=ExtraNeedsStr.substring(1);
	    								ExtraNeeds.put(v.getKey().toLowerCase(), ExtraNeedsStr);
	    							}
	    						}		    				
	    					}
	    					if (ExtraNeeds.size()>0) {
	    						for (Entry<String,String> v2: ExtraNeeds.entrySet()) {
	    							m.Needs.put(v2.getKey(), v2.getValue());
	    						}
	    					}
	    				}
	    				if (!entry.getKey().equals("main")) {
	    					for (Entry<String,Boolean> ex: m.NeedsFromClass.entrySet()) {
	    						ExtractResultModule m2 = res.Modules.get(ex.getKey());
	    						for (Entry<String,String> v2: m2.Needs.entrySet()) {
	    							m.Needs.put(v2.getKey(), v2.getValue());
	    						}
	    					}
	    				}
	    			}
		    	
	    			// write the needs out to disk
	    			BufferedWriter writerNeeds = null;
	    			try {
	    				
	    				buildFile = new File(dir, "copymewithjar.needs");	    					
	    					    				
	    				writerNeeds = new BufferedWriter(new FileWriter(buildFile));
	    				writerNeeds.write("ABMCacheSystem:" + CacheSystem + "\n");
	    				writerNeeds.write("ABMAppVersion:" + getAppVersion() + "\n");
	    				for (Entry<String,File> entry: OrderedFiles.entrySet()) {
	    					ExtractResultModule m = res.Modules.get(entry.getKey());	    					
	    					if (m.Needs.size()>0) {
	    						String str="";
	    						for (Entry<String,String> v: m.Needs.entrySet()) {
	    							str+=";" + v.getValue();
	    						}
	    						str=str.substring(1);
	    						if (PageMappings.size()>0) {
	    							writerNeeds.write(PageMappings.get(m.Name.toLowerCase()) + ":" + str + "\n");
	    						} else {
	    							writerNeeds.write(m.Name + ":" + str + "\n");
	    						}
	    					}
	    				}
	    				String B4JSClassesStr="";
	    				for(Entry<String,B4JS>entry: B4JSClasses.entrySet()) {
	    					if (!B4JSClassesStr.equals("")) {
	    						B4JSClassesStr=B4JSClassesStr+";";
	    					}
	    					B4JSClassesStr=B4JSClassesStr + entry.getKey();
	    				}
	    				writerNeeds.write("B4JSCLASSES:" + B4JSClassesStr + "\n");
	    				writerNeeds.write("NEEDEDICONS:");
	    				ABMaterial.BuildedIconsCSS=BuildIconsCSS();
	    				if (ABMaterial.BuildedIconsCSS.indexOf(".fa-")>-1) {
							ABMaterial.NeedsAwesomeFont=true;											
						} else {
							ABMaterial.NeedsAwesomeFont=false;
						}
	    				writerNeeds.write(ABMaterial.BuildedIconsCSS.replace(":", ";") + "\n");
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			} finally {
	    				try {
	    					writerNeeds.close();
	    				} catch (Exception e) {
	    				}
	    			}
	    		} catch (Exception e) {
    		        e.printStackTrace();
    		    } finally {
    		        try {
    		        	res.analyseLog.close();
    		        } catch (Exception e) {
    		        }
    		    }
	    	} else {
	    		return;
	    	}
		}
	}
	
	@Hide
	protected String Left(String Text, int Length) {
	   if (Length>Text.length()) {
		   Length=Text.length();
	   }
	   return Text.substring(0, Length);
	}
	
	@Hide
	protected boolean TypeFile(File f, String Type, String NotExtra) {
		BufferedReader br=null;
		boolean IsFound=false;
		try {
			String s;
			br = new BufferedReader(new FileReader(f));
			while( (s = br.readLine()) != null) {
				if (s.contains("Type=" + Type)) {
					IsFound=true;
				}
				if (s.contains("@EndOfDesignText@")) {
					break;
				}
			}
			if (IsFound && !NotExtra.equals("")) {
				while( (s = br.readLine()) != null) {
					if (s.toLowerCase().contains(NotExtra)) {
						IsFound=false;
						break;
					}
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return IsFound;
	}
			
	public void AddAppleTouchIcon(String image, String size) {
		AppleTouchIcons.add(image);
		AppleTouchIconSizes.add(size);
	}	
	
	public void AddMSTileIcon(String image, String size) {
		MSTileIcons.add(image);
		MSTileIconSizes.add(size);
	}
	
	public void AddFavorityIcon(String image, String size) {
		FavorityIcons.add(image);
		FavorityIconSizes.add(size);
	}
	
			
	/**
	 * Returns if the page needs a handler for uploads
	 */
	@RaisesSynchronousEvents
	public boolean WritePageToDisk(ABMPage page, String dir, String fileName, boolean NeedsAutorization) {
		page.BASFileName = page.Name.toLowerCase();
		if (res==null || IsFirstRunning) {
			String dirApp = anywheresoftware.b4a.objects.streams.File.getDirApp();
			IsFirstRunning = false;
			res = new ExtractResult();
			if (!AnalyserDisableAll) {
				CheckApp();
			} else {
				BA.LogError("Skipping B4J Analyse...");
				BA.LogError("Make sure you have set the Page.Needs... manually!");
				File JarFolder = new File(dirApp);  // Objects
				String dirFiles = JarFolder.getAbsolutePath();
		    	
		    	File buildFile = new File(dirFiles, "BaseTranslations" + ".lng");
		    	if (buildFile.exists()) {
		    		buildFile.delete();
		    	}
		    	buildFile = new File(dirFiles, "BaseTranslations" + ".optimizable");
		    	if (buildFile.exists()) {
		    		buildFile.delete();
		    	}
		    	buildFile = new File(dirFiles, "BaseDefaults" + ".def");
		    	if (buildFile.exists()) {
		    		buildFile.delete();
		    	}
		    	buildFile = new File(dirFiles, "BaseDefaults" + ".optimizable");
		    	if (buildFile.exists()) {
		    		buildFile.delete();
		    	}
		    	buildFile = new File(dirFiles, "B4JAnalyse" + ".log");
		    	if (buildFile.exists()) {
		    		buildFile.delete();
		    	}
			}
			
			ABMaterial.SavedNeeds = new LinkedHashMap<String,String>();
			if (ABMaterial.SavedNeeds.size()==0) {
				File readFile=null;
				BA.Log("loading " + dirApp + ": copymewithjar.needs...");
				readFile = new File(dirApp,"copymewithjar.needs");
			
				if (readFile.exists()) {
					BufferedReader br=null;
					try {

						String sCurrentLine;

						br = new BufferedReader(new FileReader(readFile));
						
						while ((sCurrentLine = br.readLine()) != null) {
							String p[] = Regex.Split(":", sCurrentLine);
							if (p.length==2) {
								if (p[0].equals("ABMAppVersion")) {
									BA.Log("Current App version: " + p[1]);
									ABMaterial.mAppVersion = p[1];
								} else {
									if (p[0].equals("ABMCacheSystem")) {
										BA.Log("Using cache system: " + p[1]);
										ABMaterial.CacheSystem = p[1];
									} else {
										if (p[0].equals("NEEDEDICONS")) {
											BA.Log("Needs material/awesome icons");
											ABMaterial.BuildedIconsCSS = p[1].replace(";", ":");
											if (ABMaterial.BuildedIconsCSS.indexOf(".fa-")>-1) {
												ABMaterial.NeedsAwesomeFont=true;											
											} else {
												ABMaterial.NeedsAwesomeFont=false;
											}
										} else {
											if (p[0].equals("B4JSCLASSES")) {
												ABMaterial.BuildedIconsCSS = p[1].replace(";", ":");
												String B4JSClasses[] = Regex.Split(";", p[1]);
												for (int mm=0;mm<B4JSClasses.length;mm++) {
													ABMaterial.SavedB4JSClasses.put(B4JSClasses[mm], B4JSClasses[mm]);
												}
											} else {
												ABMaterial.SavedNeeds.put(p[0], p[1]);
											}
										}
									}
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if (br != null)br.close();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				} else {
					BA.LogError("MISSING FILE NEXT TO THE JAR: copymewithjar.needs");
				}
			} else {
				BA.Log("Current App version: " + ABMaterial.mAppVersion);				
			}
			
		}
		
		SetSavedNeeds(page);
		
		for (int index=0;index<AppleTouchIcons.size();index++) {
			page.AddAppleTouchIcon(AppleTouchIcons.get(index), AppleTouchIconSizes.get(index));
		}
		for (int index=0;index<MSTileIcons.size();index++) {
			page.AddMSTileIcon(MSTileIcons.get(index), MSTileIconSizes.get(index));
		}
		for (int index=0;index<FavorityIcons.size();index++) {
			page.AddFavorityIcon(FavorityIcons.get(index), FavorityIconSizes.get(index));
		}
		
		if (!IsApp) {
			page.UpdateAllNeeds();
		} else {
			page.GetAllNeeds();			
		}

		 
		page.WritePageToDisk(dir, fileName, NeedsAutorization, IsApp);
		if (page.NeedsUpload || page.NeedsSignature || page.NeedsTreeTable || page.NeedsCanvas || page.NeedsFileManager) {
			return true;
		}
		
		return false;
	}
	
	public boolean HandleUpload(String dir, String tempFileName, String fileName) {
	    File tmpFile = new File(tempFileName);
	    File newFile = new File(dir, fileName);
	    
	    if (tmpFile.exists()) {
	    	return tmpFile.renameTo(newFile) ;  
	    } else {
	    	BA.Log(tempFileName + " does not exist!");
	    	return false;
	    }
		
	}
		
	public anywheresoftware.b4a.objects.collections.List ProcessTablesFromTargetName(String target) {
		anywheresoftware.b4a.objects.collections.List lst = new anywheresoftware.b4a.objects.collections.List();
		lst.Initialize();
		String[] tbl = target.split("__");
		for (String s: tbl) {
			String tblcell[] = s.split("_");
			ABMTableCell c = new ABMTableCell();
			if (tblcell.length==2) {
				// header or footer
				c.TableName = tblcell[0];
				c.Row=0;
				c.RowUniqueId="";
				c.Column = Integer.parseInt(tblcell[1]);
			} else {
				c.TableName = tblcell[0];
				c.RowUniqueId=tblcell[1];
				if (isNumericInt(c.RowUniqueId)) {
					c.Row = Integer.parseInt(tblcell[1]);
					c.RowUniqueId="";
				}
				c.Column = Integer.parseInt(tblcell[2]);
			}
			lst.Add(c);
		}
		return lst;
	}
	
	@Hide
	public static boolean isNumericInt(String str) {  
		try {  
			@SuppressWarnings("unused")
			int d = Integer.parseInt(str);  
		} catch(NumberFormatException nfe) {  
			return false;  
		}  
		return true;  
	}
	
	/**
	 * Cast a generic ABMComponent. You can use the ABMComponent.Type to find out what sort of component it is.
	 * You can use the ABM.UITYPE_ constants to find out what it is.
	 * 
	 * Dim comp as ABMComponent = page.component("mycomp")
	 * Dim lbl as ABMLabel = ABM.CastABMComponent(comp)
	 * 
	 *  Now you can use all methods or properties of the ABMLabel
	 */
	public static ABMComponent CastABMComponent(ABMComponent component) {
		switch (component.Type) {
		case ABMaterial.UITYPE_CALENDAR:
			return (ABMCalendar) component;
		case ABMaterial.UITYPE_SOCIALOAUTH:
			return (ABMSocialOAuth) component;
		case ABMaterial.UITYPE_BUTTON:
			return (ABMButton) component;
		case ABMaterial.UITYPE_LABEL:
			return (ABMLabel) component;
		case ABMaterial.UITYPE_INPUTFIELD:
			return (ABMInput) component;			
		case ABMaterial.UITYPE_FILEINPUT:
			return (ABMFileInput) component;	
		case ABMaterial.UITYPE_DIVIDER:
			return (ABMDivider) component;
		case ABMaterial.UITYPE_IMAGE:
			return (ABMImage) component;			
		case ABMaterial.UITYPE_VIDEO:
			return (ABMVideo) component;
		case ABMaterial.UITYPE_CARD:
			return (ABMCard) component;
		case ABMaterial.UITYPE_CHIP:
			return (ABMChip) component;
		case ABMaterial.UITYPE_BADGE:
			return (ABMBadge) component;
		case ABMaterial.UITYPE_COMBO:
			return (ABMCombo) component;
		case ABMaterial.UITYPE_SWITCH:
			return (ABMSwitch) component;			
		case ABMaterial.UITYPE_CHECKBOX:
			return (ABMCheckbox) component;			
		case ABMaterial.UITYPE_CODELABEL:
			return (ABMCodeLabel) component;
		case ABMaterial.UITYPE_TABEL:
			return (ABMTable) component;
		case ABMaterial.UITYPE_TABELMUTABLE:
			return (ABMTableMutable) component;
		case ABMaterial.UITYPE_TABELINFINITE:
			return (ABMTableInfinite) component;
		case ABMaterial.UITYPE_TREETABLE:
			return (ABMTreeTable) component;
		case ABMaterial.UITYPE_ABMCONTAINER:
			return (ABMContainer) component;	
		case ABMaterial.UITYPE_PARALLAX:
			return (ABMParallax) component;
		case ABMaterial.UITYPE_TABS:
			return (ABMTabs) component;
		case ABMaterial.UITYPE_ACTIONBUTTON:
			return (ABMActionButton) component;
		case ABMaterial.UITYPE_LIST:
			return (ABMList) component;
		case ABMaterial.UITYPE_IMAGESLIDER:
			return (ABMImageSlider) component;
		case ABMaterial.UITYPE_RADIOGROUP:
			return (ABMRadioGroup) component;
		case ABMaterial.UITYPE_GOOGLEMAP:
			return (ABMGoogleMap) component;
		case ABMaterial.UITYPE_CANVAS:
			return (ABMCanvas) component;
		case ABMaterial.UITYPE_PAGINATION:
			return (ABMPagination) component;
		case ABMaterial.UITYPE_UPLOAD:
			return (ABMUpload) component;
		case ABMaterial.UITYPE_SIGNATUREPAD:
			return (ABMSignaturePad) component;
		case ABMaterial.UITYPE_CHART:
			return (ABMChart) component;
		case ABMaterial.UITYPE_CUSTOMCOMPONENT:
			return (ABMCustomComponent) component;
		case ABMaterial.UITYPE_SLIDER:
			return (ABMSlider) component;
		case ABMaterial.UITYPE_RANGE:
			return (ABMRange) component;
		case ABMaterial.UITYPE_DTSCROLLER:
			return (ABMDateTimeScroller) component;			
		case ABMaterial.UITYPE_DTPICKER:
			return (ABMDateTimePicker) component;				
		case ABMaterial.UITYPE_EDITOR:
			return (ABMEditor) component;			
		case ABMaterial.UITYPE_SOCIALSHARE:
			return (ABMSocialShare) component;
		case ABMaterial.UITYPE_PDFVIEWER:
			return (ABMPDFViewer) component;
		case ABMaterial.UITYPE_PIVOTTABLE:
			return (ABMPivotTable) component;
		case ABMaterial.UITYPE_AUDIOPLAYER:
			return (ABMAudioPlayer) component;
		case ABMaterial.UITYPE_TIMELINE:
			return (ABMTimeLine) component;
		case ABMaterial.UITYPE_FLEXWALL:
			return (ABMFlexWall) component;
		case ABMaterial.UITYPE_SVGSURFACE:
			return (ABMSVGSurface) component;
		case ABMaterial.UITYPE_PATTERNLOCK:
			return (ABMPatternLock) component;
		case ABMaterial.UITYPE_CHRONOLIST:
			return (ABMChronologyList) component;		
		case ABMaterial.UITYPE_CUSTOMCARD:
			return (ABMCustomCard) component;
		case ABMaterial.UITYPE_CHAT:
			return (ABMChat) component;
		case ABMaterial.UITYPE_PLANNER:
			return (ABMPlanner) component;
		case ABMaterial.UITYPE_PERCENTSLIDER:
			return (ABMPercentSlider) component;
		case ABMaterial.UITYPE_SMARTWIZARD:
			return (ABMSmartWizard) component;
		case ABMaterial.UITYPE_ITEMLIST:
			return (ABMItemList) component;
		case ABMaterial.UITYPE_REPORTCOMPONENT:
			return (ABMReport) component;		
		case ABMaterial.UITYPE_COMPOSER:
			return (ABMComposer) component;
		case ABMaterial.UITYPE_FILEMANAGER:
			return (ABMFileManager) component;
		}
		BA.Log("Object has an undefined type!");
		return null;

	}
	
	public void SaveThemeToDisk(ABMTheme theme, String path) {
		String s = Serialize(theme);
		BufferedWriter writer = null;
        try {
        	Files.createDirectories(Paths.get(path));        	
        	File buildFile = new File(path, theme.Name + ".ABMTheme");
        	
            writer = new BufferedWriter(new FileWriter(buildFile));
            writer.write(s);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
	}
	
	
	public ABMTheme LoadThemeFromDisk(String path, String fileName) {
		String s;
		try {
			s = new String(Files.readAllBytes(Paths.get(combine(path, fileName))), StandardCharsets.UTF_8);
			return (ABMTheme) Deserialize(s);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;				
	}
	
	protected String combine(String path1, String path2)
	{
	    File file1 = new File(path1);
	    File file2 = new File(file1, path2);
	    return file2.getPath();
	}
	
	
	protected String Serialize(Object obj) {			
		if (!((obj instanceof Serializable) || (obj instanceof Externalizable))) {
			BA.LogError("There was a problem Serializing!");
			return "";
		}
		String redisString="";
		try {
             ByteArrayOutputStream bo = new ByteArrayOutputStream();
             ObjectOutputStream so = new ObjectOutputStream(bo);
             so.writeObject(obj);
             redisString = new String(Base64.getEncoder().encode(bo.toByteArray()));          
		} catch (Exception e) {
             e.printStackTrace();        
        }
		return redisString;
	}
	
	protected Object Deserialize(String value) {
		Object obj = null;
		try {
			byte b[] = Base64.getDecoder().decode(value.getBytes("UTF-8")); 
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            obj = si.readObject();                     
		} catch (Exception e) {
            e.printStackTrace();        
		}
		return obj;
	}
	
	protected static void AbstactDesignerComponent(ABMPage page, String[] vals, String[] values, boolean isBuild, String newID, ABMContainer parent) {
		switch (Integer.parseInt(vals[2])) {
		case ABMaterial.UITYPE_CALENDAR:
			ABMCalendar cal = (ABMCalendar) page.Deserialize(values[1]);			
			AbstractDesignerAdd(page, cal, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_SOCIALOAUTH:
			ABMSocialOAuth soa = (ABMSocialOAuth) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, soa, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_BUTTON:
			ABMButton but = (ABMButton) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, but, vals[0], vals[1], isBuild, newID, parent);
			break;
		case ABMaterial.UITYPE_LABEL:
			ABMLabel lab = (ABMLabel) page.Deserialize(values[1]);			
			AbstractDesignerAdd(page, lab, vals[0], vals[1], isBuild, newID, parent);
			break;
		case ABMaterial.UITYPE_INPUTFIELD:
			ABMInput inp = (ABMInput) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, inp, vals[0], vals[1], isBuild, newID, parent);
			break;			
		case ABMaterial.UITYPE_FILEINPUT:
			ABMFileInput finp = (ABMFileInput) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, finp, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_DIVIDER:
			ABMDivider div = (ABMDivider) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, div, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_IMAGE:
			ABMImage img = (ABMImage) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, img, vals[0], vals[1], isBuild, newID, parent);
			break;				
		case ABMaterial.UITYPE_VIDEO:
			ABMVideo vid = (ABMVideo) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, vid, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_CARD:
			ABMCard crd = (ABMCard) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, crd, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_CHIP:
			ABMChip chp = (ABMChip) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, chp, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_BADGE:
			ABMBadge bdg = (ABMBadge) page.Deserialize(values[1]);			
			AbstractDesignerAdd(page, bdg, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_COMBO:
			ABMCombo cmb = (ABMCombo) page.Deserialize(values[1]);
			cmb.Items = new LinkedHashMap<String, ABMComboItem>();
			AbstractDesignerAdd(page, cmb, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_SWITCH:
			ABMSwitch swt = (ABMSwitch) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, swt, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_CHECKBOX:
			ABMCheckbox chk = (ABMCheckbox) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, chk, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_CODELABEL:
			ABMCodeLabel cod = (ABMCodeLabel) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, cod, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_TABEL:
			ABMTable tbl = (ABMTable) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, tbl, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_TABELMUTABLE:
			ABMTableMutable tbm = (ABMTableMutable) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, tbm, vals[0], vals[1], isBuild, newID, parent);
			break;
		case ABMaterial.UITYPE_TABELINFINITE:
			ABMTableInfinite tbi = (ABMTableInfinite) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, tbi, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_TREETABLE:
			ABMTreeTable tt = (ABMTreeTable) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, tt, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_ABMCONTAINER:			
			break;	
		case ABMaterial.UITYPE_PARALLAX:
			ABMParallax par = (ABMParallax) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, par, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_TABS:
			ABMTabs tab = (ABMTabs) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, tab, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_ACTIONBUTTON:
			ABMActionButton acb = (ABMActionButton) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, acb, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_LIST:
			ABMList lst = (ABMList) page.Deserialize(values[1]);
			lst.Items = new LinkedHashMap<String, String>();
			lst.Parents = new LinkedHashMap<String,ABMItem>();
			AbstractDesignerAdd(page, lst, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_IMAGESLIDER:
			ABMImageSlider ims = (ABMImageSlider) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, ims, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_RADIOGROUP:
			ABMRadioGroup rgr = (ABMRadioGroup) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, rgr, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_GOOGLEMAP:
			ABMGoogleMap goo = (ABMGoogleMap) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, goo, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_CANVAS:
			ABMCanvas can = (ABMCanvas) page.Deserialize(values[1]);
			can.Objects = new LinkedHashMap<String, ABMCanvasObject>();
			can.CanvasCommands = new LinkedHashMap<String, String>();
			AbstractDesignerAdd(page, can, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_PAGINATION:
			ABMPagination pag = (ABMPagination) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, pag, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_UPLOAD:
			ABMUpload upl = (ABMUpload) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, upl, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_SIGNATUREPAD:
			ABMSignaturePad sig = (ABMSignaturePad) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, sig, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_CHART:
			ABMChart chr = (ABMChart) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, chr, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_CUSTOMCOMPONENT:
			ABMCustomComponent cus = (ABMCustomComponent) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, cus, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_SLIDER:
			ABMSlider sld = (ABMSlider) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, sld, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_RANGE:
			ABMRange rng = (ABMRange) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, rng, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_DTSCROLLER:
			ABMDateTimeScroller dsc = (ABMDateTimeScroller) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, dsc, vals[0], vals[1], isBuild, newID, parent);
			break;				
		case ABMaterial.UITYPE_DTPICKER:
			ABMDateTimePicker dpc = (ABMDateTimePicker) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, dpc, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_EDITOR:
			ABMEditor edi = (ABMEditor) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, edi, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_SOCIALSHARE:
			ABMSocialShare ssh = (ABMSocialShare) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, ssh, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_PDFVIEWER:
			ABMPDFViewer pdf = (ABMPDFViewer) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, pdf, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_PIVOTTABLE:
			ABMPivotTable piv = (ABMPivotTable) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, piv, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_AUDIOPLAYER:
			ABMAudioPlayer aud = (ABMAudioPlayer) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, aud, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_TIMELINE:
			ABMTimeLine tln = (ABMTimeLine) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, tln, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_FLEXWALL:
			ABMFlexWall flw = (ABMFlexWall) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, flw, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_SVGSURFACE:
			ABMSVGSurface svg = (ABMSVGSurface) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, svg, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_PATTERNLOCK:
			ABMPatternLock plc = (ABMPatternLock) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, plc, vals[0], vals[1], isBuild, newID, parent);
			break;	
		case ABMaterial.UITYPE_CHRONOLIST:
			ABMChronologyList cls = (ABMChronologyList) page.Deserialize(values[1]);
			cls.slides = new LinkedHashMap<String,ChronoSlide>();
			AbstractDesignerAdd(page, cls, vals[0], vals[1], isBuild, newID, parent);
			break;			
		case ABMaterial.UITYPE_CUSTOMCARD:
			ABMCustomCard ccrd = (ABMCustomCard) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, ccrd, vals[0], vals[1], isBuild, newID, parent);
			break;
		case ABMaterial.UITYPE_CHAT:
			ABMChat conv = (ABMChat) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, conv, vals[0], vals[1], isBuild, newID, parent);
			break;
		case ABMaterial.UITYPE_PLANNER:
			ABMPlanner plan = (ABMPlanner) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, plan, vals[0], vals[1], isBuild, newID, parent);
			break;
		case ABMaterial.UITYPE_PERCENTSLIDER:
			ABMPercentSlider pslider = (ABMPercentSlider) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, pslider, vals[0], vals[1], isBuild, newID, parent);
			break;
		case ABMaterial.UITYPE_SMARTWIZARD:
			ABMSmartWizard wiz = (ABMSmartWizard) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, wiz, vals[0], vals[1], isBuild, newID, parent);
			break;
		case ABMaterial.UITYPE_ITEMLIST:
			ABMItemList lbll = (ABMItemList) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, lbll, vals[0], vals[1], isBuild, newID, parent);
			break;
		case ABMaterial.UITYPE_REPORTCOMPONENT:
			ABMReport rep = (ABMReport) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, rep, vals[0], vals[1], isBuild, newID, parent);
			break;		
		case ABMaterial.UITYPE_COMPOSER:
			ABMComposer comp = (ABMComposer) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, comp, vals[0], vals[1], isBuild, newID, parent);
			break;
		case ABMaterial.UITYPE_FILEMANAGER:
			ABMFileManager fman = (ABMFileManager) page.Deserialize(values[1]);
			AbstractDesignerAdd(page, fman, vals[0], vals[1], isBuild, newID, parent);
			break;
		default:
			BA.LogError(vals[4] + " is of an unknown ABM type " + vals[2]);
			break;								
		}
	}
	
	protected static String GetComponentType(String val) {
		switch (Integer.parseInt(val)) {
		case ABMaterial.UITYPE_CALENDAR:			
			return "abmcalendar";
		case ABMaterial.UITYPE_SOCIALOAUTH:			
			return "abmsocialoauth";
		case ABMaterial.UITYPE_BUTTON:			
			return "abmbutton";
		case ABMaterial.UITYPE_LABEL:			
			return "abmlabel";
		case ABMaterial.UITYPE_INPUTFIELD:
			return "abminput";
		case ABMaterial.UITYPE_FILEINPUT:
			return "abmfileinput";
		case ABMaterial.UITYPE_DIVIDER:
			return "abmdivider";
		case ABMaterial.UITYPE_IMAGE:
			return "abmimage";
		case ABMaterial.UITYPE_VIDEO:
			return "abmvideo";
		case ABMaterial.UITYPE_CARD:
			return "abmcard";
		case ABMaterial.UITYPE_CHIP:
			return "abmchip";
		case ABMaterial.UITYPE_BADGE:
			return "abmbadge";
		case ABMaterial.UITYPE_COMBO:
			return "abmcombo";
		case ABMaterial.UITYPE_SWITCH:
			return "abmswitch";
		case ABMaterial.UITYPE_CHECKBOX:
			return "abmcheckbox";
		case ABMaterial.UITYPE_CODELABEL:
			return "abmcodelabel";
		case ABMaterial.UITYPE_TABEL:
			return "abmtable";
		case ABMaterial.UITYPE_TABELMUTABLE:
			return "abmtablemutable";
		case ABMaterial.UITYPE_TABELINFINITE:
			return "abmtableinfinite";
		case ABMaterial.UITYPE_TREETABLE:
			return "abmtreetable";
		case ABMaterial.UITYPE_ABMCONTAINER:
			return "abmcontainer";
		case ABMaterial.UITYPE_PARALLAX:
			return "abmparallax";
		case ABMaterial.UITYPE_TABS:
			return "abmtabs";
		case ABMaterial.UITYPE_ACTIONBUTTON:
			return "abmactionbutton";
		case ABMaterial.UITYPE_LIST:
			return "abmlist";
		case ABMaterial.UITYPE_IMAGESLIDER:
			return "abmimageslider";
		case ABMaterial.UITYPE_RADIOGROUP:
			return "abmradiogroup";
		case ABMaterial.UITYPE_GOOGLEMAP:
			return "abmgooglemap";
		case ABMaterial.UITYPE_CANVAS:
			return "abmcanvas";
		case ABMaterial.UITYPE_PAGINATION:
			return "abmpagination";
		case ABMaterial.UITYPE_UPLOAD:
			return "abmupload";
		case ABMaterial.UITYPE_SIGNATUREPAD:
			return "abmsignaturepad";
		case ABMaterial.UITYPE_CHART:
			return "abmchart";
		case ABMaterial.UITYPE_CUSTOMCOMPONENT:
			return "abmcustomcomponent";
		case ABMaterial.UITYPE_SLIDER:
			return "abmslider";
		case ABMaterial.UITYPE_RANGE:
			return "abmrange";
		case ABMaterial.UITYPE_DTSCROLLER:
			return "abmdatetimescroller";
		case ABMaterial.UITYPE_DTPICKER:
			return "abmdatetimepicker";
		case ABMaterial.UITYPE_EDITOR:
			return "abmeditor";
		case ABMaterial.UITYPE_SOCIALSHARE:
			return "abmsocialshare";
		case ABMaterial.UITYPE_PDFVIEWER:
			return "abmpdfviewer";
		case ABMaterial.UITYPE_PIVOTTABLE:
			return "abmpivottable";
		case ABMaterial.UITYPE_AUDIOPLAYER:
			return "abmaudioplayer";
		case ABMaterial.UITYPE_TIMELINE:
			return "abmtimeline";
		case ABMaterial.UITYPE_FLEXWALL:
			return "abmflexwall";
		case ABMaterial.UITYPE_SVGSURFACE:
			return "abmsvgsurface";
		case ABMaterial.UITYPE_PATTERNLOCK:
			return "abmpatternlock";
		case ABMaterial.UITYPE_CHRONOLIST:
			return "abmchronologylist";
		case ABMaterial.UITYPE_XPLAY:
			return "abmxplay";
		case ABMaterial.UITYPE_CUSTOMCARD:
			return "abmcustomcard";
		case ABMaterial.UITYPE_CHAT:
			return "abmchat";
		case ABMaterial.UITYPE_PLANNER:
			return "abmplanner";
		case ABMaterial.UITYPE_PERCENTSLIDER:
			return "abmpercentslider";
		case ABMaterial.UITYPE_SMARTWIZARD:
			return "abmsmartwizard";
		case ABMaterial.UITYPE_ITEMLIST:
			return "abmlabellist";
		case ABMaterial.UITYPE_REPORTCOMPONENT:
			return "abmreport";
		case ABMaterial.UITYPE_COMPOSER:
			return "abmcomposer";
		case ABMaterial.UITYPE_FILEMANAGER:
			return "abmfilemanager";
		default:
			BA.LogError("Unknown ABM type " + val);
			return "";								
		}
	}
	
	protected static void AbstractDesignerAdd(ABMPage page, ABMComponent comp, String r, String c, boolean isBuild, String newID, ABMContainer parent) {
		comp.LoadAtConnect = isBuild;
		comp.IsBuild = isBuild;
		comp.IsInitialized = isBuild;
		comp.ParentString = newID;
		
		comp.SetPage(page);
		ABMCell ccell=null;
		if (parent==null) {
			ccell = page.CellInternal(r,c);
		} else {
			ccell= parent.CellInternal(r, c);			
		}
		
		if (ccell==null) {
			BA.LogError("ERROR: No cell found for " + r + " "  + c);
			return;
		}
		if (!comp.ArrayName.equals("")) {
			ccell.AddArrayComponent(comp, "");
		} else {
			ccell.AddComponent(comp);
		}
	}
	
	@Hide
	public static ABMComponent GetComponent(ABMPage page, ABMComponent obj, String componentId, String parString) {		
		if (!obj.mB4JSUniqueKey.equals("")) {
			obj.GotLastVisibility = false;
		} else {
			obj.GotLastVisibility = true;
		}
		switch (obj.Type) {
		case ABMaterial.UITYPE_CALENDAR:
			return (ABMCalendar) obj;
		case ABMaterial.UITYPE_SOCIALOAUTH:
			return (ABMSocialOAuth) obj;
		case ABMaterial.UITYPE_BUTTON:
			ABMButton btn = (ABMButton) obj;
			btn.GotLastText = false;
			btn.GotLastEnabled = false;
			btn.JQ  = page.ws.GetElementById(parString + componentId.toLowerCase());	
		
			
			if (btn.JQ!=null) {
				btn.FutureText = btn.JQ.GetHtml();				
			} else {
				btn.FutureText=null;				
			}
				
			return btn;
		case ABMaterial.UITYPE_LABEL:
			ABMLabel lbl = (ABMLabel) obj;	
			lbl.GotLastText = false;
			lbl.JQ  = page.ws.GetElementById(parString + componentId.toLowerCase());	
		
			
			if (lbl.JQ!=null) {
				lbl.FutureText = lbl.JQ.GetHtml();				
			} else {
				lbl.FutureText=null;				
			}
				
			return lbl;
		case ABMaterial.UITYPE_INPUTFIELD:
			ABMInput in = (ABMInput) obj;
			in.GotLastText=false;
			in.GotLastEnabled = false;

			in.JQ  = page.ws.GetElementById(parString + componentId.toLowerCase());	
			if (in.JQ!=null) {
				if (!in.IsTextArea) {
					in.FutureText = in.JQ.GetVal();
				} else {
					in.FutureText = in.JQ.GetVal();
					in.FutureTextValue = in.JQ.GetText();
				}
			} else {
				in.FutureText=null;
				in.FutureTextValue=null;
			}
					
			return in;
		case ABMaterial.UITYPE_FILEINPUT:
			ABMFileInput fin = (ABMFileInput) obj;			
			return fin;
		case ABMaterial.UITYPE_DIVIDER:
			return (ABMDivider) obj;
		case ABMaterial.UITYPE_IMAGE:
			ABMImage im = (ABMImage) obj;
			if (im.SourceToggle.equals("")) {
				return im;
			} else {
				im.JQ  = page.ws.GetElementById(parString + componentId.toLowerCase());
				if (im.JQ!=null) {
					im.FutureState = im.JQ.GetProp("alt");
				} else {
					im.FutureState= null;
				}
				return im;
			}
		case ABMaterial.UITYPE_VIDEO:
			return (ABMVideo) obj;
		case ABMaterial.UITYPE_CARD:
			return (ABMCard) obj;
		case ABMaterial.UITYPE_CHIP:
			return (ABMChip) obj;
		case ABMaterial.UITYPE_BADGE:
			return (ABMBadge) obj;
		case ABMaterial.UITYPE_COMBO:
			ABMCombo cmb = (ABMCombo) obj;
			cmb.FutureText = GetProperty(page, parString + componentId.toLowerCase(), "returnid");
			return cmb;
		case ABMaterial.UITYPE_SWITCH:
			ABMSwitch sw = (ABMSwitch) obj;
			sw.GotLastState=false;
			sw.GotLastEnabled = false;
			sw.JQ  = page.ws.GetElementById(parString + componentId.toLowerCase() + "input");
			
			if (sw.JQ!=null) {
				sw.FutureChecked = sw.JQ.GetProp("checked");
			} else {
				sw.FutureChecked = null;
			}
			
			return sw;
		case ABMaterial.UITYPE_CHECKBOX:
			ABMCheckbox ch = (ABMCheckbox) obj;
			ch.GotLastState=false;
			ch.GotLastEnabled = false;
			
			ch.JQ  = page.ws.GetElementById(parString + componentId.toLowerCase() + "input");
			if (ch.JQ!=null) {
				ch.FutureChecked = ch.JQ.GetProp("checked");
			} else {
				ch.FutureChecked = null;
			}
			
			return ch;
		case ABMaterial.UITYPE_CODELABEL:
			return (ABMCodeLabel) obj;
		case ABMaterial.UITYPE_TABEL:
			return (ABMTable) obj;
		case ABMaterial.UITYPE_TABELMUTABLE:
			return (ABMTableMutable) obj;
		case ABMaterial.UITYPE_TABELINFINITE:
			return (ABMTableInfinite) obj;
		case ABMaterial.UITYPE_TREETABLE:
			return (ABMTreeTable) obj;
		case ABMaterial.UITYPE_ABMCONTAINER:
			ABMContainer cont =  (ABMContainer) obj;
			cont.GotLastVisibility = false;
			cont.JQ = page.ws.GetElementById(parString + componentId.toLowerCase());
			return cont;
		case ABMaterial.UITYPE_PARALLAX:
			return (ABMParallax) obj;
		case ABMaterial.UITYPE_TABS:
			ABMTabs ta = (ABMTabs) obj;
			return ta;
		case ABMaterial.UITYPE_ACTIONBUTTON:
			return (ABMActionButton) obj;
		case ABMaterial.UITYPE_LIST:
			return (ABMList) obj;
		case ABMaterial.UITYPE_IMAGESLIDER:
			return (ABMImageSlider) obj;
		case ABMaterial.UITYPE_RADIOGROUP:
			ABMRadioGroup radio = (ABMRadioGroup) obj;
			radio.GotLastChecked = false;			
			return radio;
		case ABMaterial.UITYPE_GOOGLEMAP:
			return (ABMGoogleMap) obj;
		case ABMaterial.UITYPE_CANVAS:
			return (ABMCanvas) obj;
		case ABMaterial.UITYPE_PAGINATION:
			return (ABMPagination) obj;
		case ABMaterial.UITYPE_UPLOAD:
			return (ABMUpload) obj;
		case ABMaterial.UITYPE_SIGNATUREPAD:
			return (ABMSignaturePad) obj;
		case ABMaterial.UITYPE_CHART:
			return (ABMChart) obj;
		case ABMaterial.UITYPE_CUSTOMCOMPONENT:
			return (ABMCustomComponent) obj;
		case ABMaterial.UITYPE_SLIDER:
			ABMSlider sl = (ABMSlider) obj;
			sl.GotLastValue = false;
			return sl;
		case ABMaterial.UITYPE_RANGE:
			ABMRange ra = (ABMRange) obj;
			ra.GotLastStart = false;
			ra.GotLastStop = false;
			return ra;
		case ABMaterial.UITYPE_DTSCROLLER:
			ABMDateTimeScroller src = (ABMDateTimeScroller) obj;
			src.FutureText = GetProperty(page, parString + componentId.toLowerCase(), "returnvalue");
			src.FutureTextISO = GetProperty(page, parString + componentId.toLowerCase(), "returnvalueISO");
			src.FutureTextISOZ = GetProperty(page, parString + componentId.toLowerCase(), "returnvalueISOZ");
			return src;
		case ABMaterial.UITYPE_DTPICKER:
			ABMDateTimePicker pick = (ABMDateTimePicker) obj;
			pick.FutureText = GetProperty(page, parString + componentId.toLowerCase(), "returnvalue");
			pick.FutureTextISO = GetProperty(page, parString + componentId.toLowerCase(), "returnvalueISO");
			pick.FutureTextISOZ = GetProperty(page, parString + componentId.toLowerCase(), "returnvalueISOZ");
			return pick;	
		case ABMaterial.UITYPE_EDITOR:
			ABMEditor ed = (ABMEditor) obj;
			anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
			Params.Initialize();
			Params.Add(parString + componentId.toLowerCase());
			ed.FutureText = page.ws.RunFunctionWithResult("geteditorhtml", Params);		
			return ed;
		case ABMaterial.UITYPE_SOCIALSHARE:
			return (ABMSocialShare) obj;
		case ABMaterial.UITYPE_PDFVIEWER:
			return (ABMPDFViewer) obj;
		case ABMaterial.UITYPE_PIVOTTABLE:
			return (ABMPivotTable) obj;
		case ABMaterial.UITYPE_AUDIOPLAYER:
			return (ABMAudioPlayer) obj;
		case ABMaterial.UITYPE_TIMELINE:
			return (ABMTimeLine) obj;
		case ABMaterial.UITYPE_FLEXWALL:
			return (ABMFlexWall) obj;
		case ABMaterial.UITYPE_SVGSURFACE:
			return (ABMSVGSurface) obj;
		case ABMaterial.UITYPE_PATTERNLOCK:
			return (ABMPatternLock) obj;
		case ABMaterial.UITYPE_CHRONOLIST:
			return (ABMChronologyList) obj;
		case ABMaterial.UITYPE_CUSTOMCARD:
			return (ABMCustomCard) obj;	
		case ABMaterial.UITYPE_CHAT:
			return (ABMChat) obj;
		case ABMaterial.UITYPE_PLANNER:
			return (ABMPlanner) obj;
		case ABMaterial.UITYPE_PERCENTSLIDER:
			return (ABMPercentSlider) obj;
		case ABMaterial.UITYPE_SMARTWIZARD:
			return (ABMSmartWizard) obj;
		case ABMaterial.UITYPE_ITEMLIST:
			return (ABMItemList) obj;
		case ABMaterial.UITYPE_REPORTCOMPONENT:
			return (ABMReport) obj;
		case ABMaterial.UITYPE_COMPOSER:
			return (ABMComposer) obj;
		case ABMaterial.UITYPE_FILEMANAGER:
			return (ABMFileManager) obj;
		}
		BA.Log("Object with id=" + componentId + " has an undefined type!");
		return null;
	}
	
	@Hide
	public static String RemoveComponent(ABMPage Page, ABMComponent component) {

		component.Page = Page;
		String Upload="";
		String tmpId = component.ArrayName.toLowerCase() + component.ID.toLowerCase();
		switch (component.Type) {
			case ABMaterial.UITYPE_CALENDAR:
				Page.Calendars.remove(tmpId);
				break;
			case ABMaterial.UITYPE_ABMCONTAINER:
				Page.Containers.remove(tmpId);
				break;
			case ABMaterial.UITYPE_SOCIALOAUTH:
				Page.Networks.remove(tmpId);
				break;
			case ABMaterial.UITYPE_CHART:
				Page.Charts.remove(tmpId);
				break;
			case ABMaterial.UITYPE_CANVAS:
				Page.Canvases.remove(tmpId);
				break;
			case ABMaterial.UITYPE_GOOGLEMAP:
				Page.gmaps.remove(tmpId);
				break;
			case ABMaterial.UITYPE_TABEL:
				ABMTable t = (ABMTable) component;
				if (t.IsScrollable) {
					Page.Tables.remove(tmpId);
				}	
				break;
			case ABMaterial.UITYPE_TABELMUTABLE:
				
				break;
			case ABMaterial.UITYPE_INPUTFIELD:					
				Page.Inputs.remove(tmpId);
				break;
			case ABMaterial.UITYPE_FILEINPUT:
				break;
			case ABMaterial.UITYPE_TREETABLE:
				ABMTreeTable tt = (ABMTreeTable) component;
				for (Entry<String, ABMTreeTableRow> erow: tt.Rows.entrySet()) {
					ABMTreeTableRow row = erow.getValue();
					for (int i=0;i<row.cells.size();i++) {
						ABMTreeTableCell cell = (ABMTreeTableCell) row.cells.get(i);
						if (cell.value!=null) {
							RemoveComponent(Page, cell.value);
						}
					}
				}
				break;
			case ABMaterial.UITYPE_LIST:
				Page.Lists.remove(tmpId);
				break;
			case ABMaterial.UITYPE_UPLOAD:
				Upload="upload";
				Page.uploads.remove(tmpId);
				break;
			case ABMaterial.UITYPE_SIGNATUREPAD:
				Page.signatures.remove(tmpId);
				break;
			case ABMaterial.UITYPE_CUSTOMCOMPONENT:
				Page.customcomps.remove(tmpId);
				break;
			case ABMaterial.UITYPE_SLIDER:
				Page.sliders.remove(tmpId);
				break;
			case ABMaterial.UITYPE_RANGE:
				Page.ranges.remove(tmpId);
				break;
			case ABMaterial.UITYPE_DTSCROLLER:
				Page.dateTimeScrollers.remove(tmpId);
				break;
			case ABMaterial.UITYPE_DTPICKER:
				Page.dateTimePickers.remove(tmpId);
				break;
			case ABMaterial.UITYPE_EDITOR:
				Page.Editors.remove(tmpId);
				break;
			case ABMaterial.UITYPE_SOCIALSHARE:
				Page.SocialShares.remove(tmpId);
				break;
			case ABMaterial.UITYPE_AUDIOPLAYER:
				break;
			case ABMaterial.UITYPE_TIMELINE:
				Page.TimeLines.remove(tmpId);
				break;
			case ABMaterial.UITYPE_FLEXWALL:
				Page.FlexWalls.remove(tmpId);
				break;
			case ABMaterial.UITYPE_SVGSURFACE:
				Page.SVGSurfaces.remove(tmpId);
				break;
			case ABMaterial.UITYPE_PATTERNLOCK:
				Page.PatternLocks.remove(tmpId);
				break;
			case ABMaterial.UITYPE_CHRONOLIST:
				Page.ChronoLists.remove(tmpId);
				break;
			case ABMaterial.UITYPE_CHAT:
				break;
			case ABMaterial.UITYPE_PLANNER:
				break;
			case ABMaterial.UITYPE_PERCENTSLIDER:
				break;	
			case ABMaterial.UITYPE_SMARTWIZARD:
				break;
			case ABMaterial.UITYPE_ITEMLIST:
				break;			
			case ABMaterial.UITYPE_REPORTCOMPONENT:
				break;			
			case ABMaterial.UITYPE_COMPOSER:
				break;
			case ABMaterial.UITYPE_FILEMANAGER:
				break;
		}
		return Upload;
	}
	
	@Hide
	public static String AddComponent(ABMPage Page, ABMComponent component) {

		component.Page = Page;
		String Upload="";
		String tmpId = component.ArrayName.toLowerCase() + component.ID.toLowerCase();
		switch (component.Type) {
			case ABMaterial.UITYPE_PARALLAX:
				Page.NeedsParallax=true;
				break;
			case ABMaterial.UITYPE_CALENDAR:
				Page.NeedsCalendar=true;
				Page.Calendars.put(tmpId, (ABMCalendar) component);
				break;
			case ABMaterial.UITYPE_ABMCONTAINER:
				Page.Containers.put(tmpId, (ABMContainer) component);
				break;
			case ABMaterial.UITYPE_SOCIALOAUTH:
				Page.NeedsOAuth=true;
				Page.Networks.put(tmpId, (ABMSocialOAuth) component);
				break;
			case ABMaterial.UITYPE_CHART:
				Page.NeedsChart=true;
				Page.Charts.put(tmpId, (ABMChart) component);
				break;
			case ABMaterial.UITYPE_CHIP:
				Page.NeedsChips=true;
				break;
			case ABMaterial.UITYPE_ACTIONBUTTON:
				Page.NeedsActionButton=true;
				break;
			case ABMaterial.UITYPE_CARD:
				Page.NeedsCards=true;
				break;
			case ABMaterial.UITYPE_LIST:
				Page.Lists.put(tmpId, (ABMList) component);
				Page.NeedsLists=true;
				break;
			case ABMaterial.UITYPE_CODELABEL:
				Page.NeedsCodeLabel=true;
				break;
			case ABMaterial.UITYPE_CANVAS:
				Page.NeedsCanvas=true;
				Page.Canvases.put(tmpId, (ABMCanvas)component);
				break;
			case ABMaterial.UITYPE_GOOGLEMAP:
				Page.NeedsGoogleMap=true;
				Page.gmaps.put(tmpId, (ABMGoogleMap)component);
				break;
			case ABMaterial.UITYPE_VIDEO:
				ABMVideo v = (ABMVideo) component;
				switch (v.videoType) {
				case "HTML5":
					Page.NeedsHTML5Video=true;
					break;
				case "youtube":
					Page.NeedsYouTube=true;
					break;
				case "vimeo":
					
				}
				
				break;
			case ABMaterial.UITYPE_INPUTFIELD:
				ABMInput in = (ABMInput) component;
				Page.NeedsInput=true;
				if (in.mAutoComplete.size()>0 || in.AutoCompleteType.equals(AUTOCOMPLETE_GOOGLE)) {
					Page.Inputs.put(tmpId, (ABMInput) component);
				}
				if (in.IsTextArea) {
					Page.NeedsTextArea=true;
				}
				if (!in.mInputMask.equals("")) {
					Page.NeedsMask=true;
				}
				if (in.AutoCompleteType.equals(AUTOCOMPLETE_GOOGLE)) {
					Page.NeedsGoogleMap=true;
				}
				break;
			case ABMaterial.UITYPE_FILEINPUT:
				Page.NeedsUpload=true;
				Page.NeedsInput=true;
				Page.NeedsFileInput=true;
				break;
			case ABMaterial.UITYPE_BADGE:
				Page.NeedsBadge=true;
				break;
			case ABMaterial.UITYPE_CHECKBOX:
				Page.NeedsCheckbox=true;
				break;
			case ABMaterial.UITYPE_COMBO:
				Page.NeedsCombo=true;
				break;
			case ABMaterial.UITYPE_RADIOGROUP:
				Page.NeedsRadio=true;
				break;
			case ABMaterial.UITYPE_TABS:
				Page.NeedsTabs=true;
				break;
			case ABMaterial.UITYPE_SWITCH:
				Page.NeedsSwitch=true;
				break;
			case ABMaterial.UITYPE_IMAGESLIDER:
				Page.NeedsImageSlider=true;
				break;	
			case ABMaterial.UITYPE_TABEL:
				
				ABMTable t = (ABMTable) component;
				Page.NeedsTable=true;
				if (t.IsScrollable) {	
					Page.Tables.put(tmpId, t);
				}	
				if (t.IsSortable) {
					Page.NeedsSortingTable=t.IsSortable;
				}
				break;
			case ABMaterial.UITYPE_TABELMUTABLE:
				
				ABMTableMutable tm = (ABMTableMutable) component;
				Page.NeedsTable=true;
				
				if (tm.IsSortable) {
					Page.NeedsSortingTable=tm.IsSortable;
				}
				break;
			case ABMaterial.UITYPE_TABELINFINITE:				
				ABMTableInfinite ti = (ABMTableInfinite) component;
				Page.NeedsTable=true;
				Page.NeedsJQueryUI=true;
				
				if (ti.IsSortable) {
					Page.NeedsSortingTable=ti.IsSortable;
				}
				break;
			case ABMaterial.UITYPE_TREETABLE:
				ABMTreeTable tt = (ABMTreeTable) component;
				Page.NeedsTreeTable=true;
				for (Entry<String, ABMTreeTableRow> erow: tt.Rows.entrySet()) {
					ABMTreeTableRow row = erow.getValue();
					for (int i=0;i<row.cells.size();i++) {
						ABMTreeTableCell cell = (ABMTreeTableCell) row.cells.get(i);
						if (cell.value!=null) {
							AddComponent(Page, cell.value);
						}
					}
				}
				break;
			case ABMaterial.UITYPE_PAGINATION:
				Page.NeedsPagination=true;
				break;
			case ABMaterial.UITYPE_UPLOAD:
				Upload="upload";
				component.extra = "upload";
				Page.NeedsUpload=true;
				Page.uploads.put(tmpId, (ABMUpload) component);
				break;
			case ABMaterial.UITYPE_SIGNATUREPAD:
				Page.NeedsSignature=true;
				Page.signatures.put(tmpId, (ABMSignaturePad) component);
				break;
			case ABMaterial.UITYPE_CUSTOMCOMPONENT:
				Page.customcomps.put(tmpId, (ABMCustomComponent) component);
				break;
			case ABMaterial.UITYPE_SLIDER:
				Page.sliders.put(tmpId, (ABMSlider) component);
				Page.NeedsSlider=true;
				break;	
			case ABMaterial.UITYPE_RANGE:
				Page.ranges.put(tmpId, (ABMRange) component);
				Page.NeedsRange=true;
				break;	
			case ABMaterial.UITYPE_DTSCROLLER:
				Page.dateTimeScrollers.put(tmpId, (ABMDateTimeScroller) component);
				Page.NeedsDateTimeScroller=true;
				break;
			case ABMaterial.UITYPE_DTPICKER:
				Page.dateTimePickers.put(tmpId, (ABMDateTimePicker) component);
				Page.NeedsDateTimePicker=true;
				break;	
			case ABMaterial.UITYPE_EDITOR:
				Page.Editors.put(tmpId, (ABMEditor) component);
				Page.NeedsEditor=true;
				break;
			case ABMaterial.UITYPE_SOCIALSHARE:
				Page.SocialShares.put(tmpId, (ABMSocialShare) component);
				Page.NeedsSocialShare=true;
			case ABMaterial.UITYPE_PDFVIEWER:
				Page.NeedsPDFViewer=true;
				break;
			case ABMaterial.UITYPE_PIVOTTABLE:				
				Page.NeedsPivot=true;
				break;
			case ABMaterial.UITYPE_AUDIOPLAYER:
				Page.NeedsAudioPlayer=true;
				break;
			case ABMaterial.UITYPE_TIMELINE:
				Page.TimeLines.put(tmpId, (ABMTimeLine) component);
				Page.NeedsTimeline=true;
				break;
			case ABMaterial.UITYPE_FLEXWALL:
				Page.FlexWalls.put(tmpId, (ABMFlexWall) component);
				Page.NeedsFlexWall=true;
				break;
			case ABMaterial.UITYPE_SVGSURFACE:
				Page.SVGSurfaces.put(tmpId, (ABMSVGSurface) component);
				Page.NeedsSVGSurface=true;
				break;
			case ABMaterial.UITYPE_PATTERNLOCK:
				Page.PatternLocks.put(tmpId, (ABMPatternLock) component);
				Page.NeedsPatternLock=true;
				break;
			case ABMaterial.UITYPE_CHRONOLIST:
				Page.ChronoLists.put(tmpId, (ABMChronologyList) component);
				Page.NeedsChronologyList=true;
				break;			
			case ABMaterial.UITYPE_CUSTOMCARD:
				Page.NeedsCustomCards=true;
				break;
			case ABMaterial.UITYPE_CHAT:
				Page.NeedsChat=true;
				break;
			case ABMaterial.UITYPE_PLANNER:
				Page.NeedsPlanner=true;
				break;	
			case ABMaterial.UITYPE_PERCENTSLIDER:
				Page.NeedsPercentSlider=true;
				break;
			case ABMaterial.UITYPE_SMARTWIZARD:
				Page.NeedsSmartWizard=true;
				break;
			case ABMaterial.UITYPE_ITEMLIST:
				
				break;
			case ABMaterial.UITYPE_REPORTCOMPONENT:
				break;	
			case ABMaterial.UITYPE_COMPOSER:
				Page.NeedsComposer=true;
				break;
			case ABMaterial.UITYPE_FILEMANAGER:
				Page.NeedsFileManager=true;
				break;				
		}	
		return Upload;
	}
		
	protected static ABMHtmlUtils HTMLConv() {
		if (innerHTMLConv==null) {
			innerHTMLConv = new ABMHtmlUtils();
			innerHTMLConv.Initialize();
		}
		return innerHTMLConv;
	}
	
	/**
	 * You can use extra to make specific files (e.g. for a specific client)
	 * e.g.  using XTR("en", "myclient", "0001", "Name") will do the following:
	 * 
	 * 1. using en.lng
	 * 2. using en_myclient.lng (overwiting all entries "en.lng" had.
	 * 
	 * ---> en.lng
	 * GlobalTranslations_0001;Name
	 * GlobalTranslations_0002;Address
	 * GlobalTranslations_0003;Persons
	 * GlobalTranslations_0004;Articles
	 * 
	 * ---> en_myclient.lng
	 * GlobalTranslations_0003;Employees
	 * 
	 */
	public String XTR(String Language, String Extra, String Code, String Text) {
		String ActiveSubLanguage = (Language + "_" + Extra).toLowerCase();
		String ActiveLanguage = (Language).toLowerCase();
		String prefix = "GlobalTranslations";
		if (Trans.containsKey(ActiveSubLanguage)) {
			return Trans.get(ActiveSubLanguage).getOrDefault(prefix + "_" + Code, Text);
		}
		if (Trans.containsKey(ActiveLanguage)) {
			return Trans.get(ActiveLanguage).getOrDefault(prefix + "_" + Code, Text);
		}
		return Text;
	}
	
	/**
	 * You can use extra to make specific files (e.g. for a specific client)
	 * e.g.  using ABM.XDF("alldefaults", "myclient", "0001", "km") will do the following:
	 * 
	 * 1. using alldefaults.def
	 * 2. using alldefaults_myclient.def (overwiting all entries "alldefaults.def" had.
	 * 
	 * ---> alldefaults.def
	 * GlobalDefaults_0001;litre
	 * GlobalDefaults_0002;km
	 * GlobalDefaults_0003;cm
	 * GlobalDefaults_0004;metre
	 * 
	 * ---> alldefaults_myclient.def
	 * GlobalDefaults_0002;miles
	 * 
	 */
	public String XDF(String DefaultsCode, String Extra, String Code, String Default) {
		String ActiveDef = (DefaultsCode + "_" + Extra).toLowerCase();
		String prefix = "GlobalDefaults";
		if (Defs.containsKey(ActiveDef)) {
			return Defs.get(ActiveDef).getOrDefault(prefix + "_" + Code, Default);
		}
		return Default;
	}
	
		
	@Hide
	public static String GetColorStr(String col, String in, String extra) {
		String s=col;
		if (!extra.equals("")) {
			s += "-"+extra;
		}
		
		if (!in.equals("")) {
			if (!extra.equals("")) {
				s += " " + extra + "-" + in;
			} else {
				s += " " + in;
			}
		}
		return s;
	}
	
	@Hide
	public static String GetColorStrMapRGBA(String col, String in, double opacity) {
		if (col.equals(COLOR_BLACK) || col.equals(COLOR_WHITE) || col.equals(COLOR_TRANSPARENT)) {
			in = "";
		}
		String s = (col + " " + in).trim();
		ExtraColor c = ExtraColors.getOrDefault(s,null);
		if (c!=null) {
			return hex2Rgba(c.HexValue, c.Opacity);
		}
		s = GetColorStrMap(col, in);
		return hex2Rgba(s, opacity);
	}
	
	@Hide
	public static String hex2Rgba(String colorStr, double opacity) {
		String s = "rgba(" + Integer.valueOf( colorStr.substring( 1, 3 ), 16 ) + "," + Integer.valueOf( colorStr.substring( 3, 5 ), 16 ) + "," + Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) + "," + opacity + ")";
	    return s;
	}
	
	public static String ABMColorToHex(String color, String intensity) {
		if (color.startsWith("#")) {
			return color;
		}
		return GetColorStrMap(color, intensity);
	}
	
	@Hide
	public static String GetColorStrMap(String col, String in) {
		if (col.startsWith("#")) {
			return col;
		}
		if (col.equals(COLOR_BLACK) || col.equals(COLOR_WHITE) || col.equals(COLOR_TRANSPARENT)) {
			in = "";
		}
		String s = (col + " " + in).trim();
		ExtraColor c = ExtraColors.getOrDefault(s,null);
		if (c!=null) {
			return c.HexValue;
		}
		if (colorMap.isEmpty()) {
			BuildColorMap();
		}
		return colorMap.getOrDefault(s, "#000000");
	}
	
	@Hide
	public static String GetWavesEffect(String effect, boolean asCircle) {
		if (effect.equalsIgnoreCase(WAVESEFFECT_DEFAULT)) {
			return WAVESEFFECT_DEFAULT;
		}
		String s = effect;
		if (asCircle) {
			s = s + " waves-circle";			
		}
		return s;
	}
	
	public static int GetColorInt(String col, String intensity) {
		if (col.startsWith("#")) {
			return Integer.decode(col);
		}
		if (col.equals(COLOR_BLACK) || col.equals(COLOR_WHITE) || col.equals(COLOR_TRANSPARENT)) {
			intensity = "";
		}
		String s = (col + " " + intensity).trim();
		ExtraColor c = ExtraColors.getOrDefault(s,null);
		if (c!=null) {
			return Integer.decode(c.HexValue);
		}
		if (colorMap.isEmpty()) {
			BuildColorMap();
		}
		s = colorMap.getOrDefault(s, "#000000");
		return Integer.decode(s);
	}
	
	public static void RemoveHTML(ABMPage page, String ID) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ID.toLowerCase());		
		page.ws.RunFunction("RemoveMe", Params);		
	}
	
	public static void RemoveBubbles(ABMPage page, String ID) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ID.toLowerCase());		
		page.ws.RunFunction("RemoveBubbles", Params);		
	}
	
	public static void EmptyHTML(ABMPage page, String ID) {
		if (page!=null) {
			if (page.ws!=null) {
				anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
				Params.Initialize();
				Params.Add(ID.toLowerCase());		
				page.ws.RunFunction("EmptyMe", Params);
			}
		}
	}	
	
	@Hide
	public static void RemoveHTMLParent(ABMPage page, String ID) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ID.toLowerCase());		
		page.ws.RunFunction("RemoveMeParent", Params);		
	}
	
	@Hide
	public static void AddHTMLOrReplaceMe(ABMPage page, String parentID, String myID, String HTML) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();		
		Params.Add(parentID.toLowerCase());
		Params.Add(myID);
		Params.Add(HTML);
		page.ws.RunFunction("AddHTMLOrReplaceMe", Params);
	}
	
	public static void AddHTMLPageComponent(ABMPage page, String HTML) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();		
		Params.Add(HTML);
		page.ws.RunFunction("AddHTMLPageComponent", Params);		
	}
	
	public static void AddHTML(ABMPage page, String parentID, String HTML) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(parentID.toLowerCase());
		Params.Add(HTML);
		page.ws.RunFunction("AddHTML", Params);		
	}
		
	public static void PrependHTML(ABMPage page, String parentID, String HTML) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(parentID.toLowerCase());
		Params.Add(HTML);
		page.ws.RunFunction("PrependHTML", Params);		
	}
		
	public static void AddHTMLToMain(ABMPage page, String HTML) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(HTML);
		page.ws.RunFunction("AddHTMLToMain", Params);		
	}
		
	public static void InsertHTMLAfter(ABMPage page, String parentID, String HTML) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(parentID.toLowerCase());
		Params.Add(HTML);
		page.ws.RunFunction("InsertHTMLAfter", Params);		
	}
	
	protected static void InsertHTMLAfterParent(ABMPage page, String parentID, String HTML) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(parentID.toLowerCase());
		Params.Add(HTML);
		page.ws.RunFunction("InsertHTMLAfterParent", Params);		
	}
	
	public static void InsertHTMLBefore(ABMPage page, String parentID, String HTML) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(parentID.toLowerCase());
		Params.Add(HTML);
		page.ws.RunFunction("InsertHTMLBefore", Params);		
	}
	
	protected static void InsertHTMLBeforeParent(ABMPage page, String parentID, String HTML) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(parentID.toLowerCase());
		Params.Add(HTML);
		page.ws.RunFunction("InsertHTMLBeforeParent", Params);		
	}
	
	public static void ReplaceMyHTML(ABMPage page, String ID, String HTML) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ID.toLowerCase());
		Params.Add(HTML);
		page.ws.RunFunction("replacemewith", Params);
	}
	
	public static void ReplaceMyInnerHTML(ABMPage page, String ID, String HTML) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ID.toLowerCase());
		Params.Add(HTML);
		page.ws.RunFunction("replacemeinnerwith", Params);
	}
		
	public static void AddClass(ABMPage page, String ID, String Class) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ID.toLowerCase());
		Params.Add(Class);
		page.ws.RunFunction("AddClass", Params);		
	}
		
	public static void RemoveClass(ABMPage page, String ID, String Class) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ID.toLowerCase());
		Params.Add(Class);
		page.ws.RunFunction("RemoveClass", Params);		
	}
	
	protected static void ChangeVisibility(ABMPage page, String ID, String Class) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ID.toLowerCase());
		Params.Add(Class);
		page.ws.RunFunction("ChangeVisibility", Params);		
	}
	
	protected static void ChangeVisibilityNoLowercase(ABMPage page, String ID, String Class) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ID);
		Params.Add(Class);
		page.ws.RunFunction("ChangeVisibility", Params);		
	}
	
	protected static void ChangeCursor(ABMPage page, String ID, String Class) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ID.toLowerCase());
		Params.Add(Class);
		page.ws.RunFunction("ChangeCursor", Params);		
	}
	
	public static void SetStyleProperty(ABMPage page, String ID, String propertyName, String value) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ID.toLowerCase());
		Params.Add(propertyName);
		Params.Add(value);
		page.ws.RunFunction("SetCSS", Params);		
	}
	
	/**
	 * the difference with SetStyleProperty is that the target can be any jquery selector
	 */
	public static void SetStyleProperty2(ABMPage page, String target, String propertyName, String value) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(target);
		Params.Add(propertyName);
		Params.Add(value);
		page.ws.RunFunction("SetCSS2", Params);		
	}
	
	public static String GetStyleProperty(ABMPage page, String ID, String propertyName) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ID.toLowerCase());
		Params.Add(propertyName);		
		SimpleFuture j = page.ws.RunFunctionWithResult("GetCSS", Params);	
		if (j!=null) {
			try {
				return (String) j.getValue();
			} catch (InterruptedException e) {
				//e.printStackTrace();
			} catch (TimeoutException e) {
				//e.printStackTrace();
			} catch (ExecutionException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		return "";
	}	
	
	public static void SetProperty(ABMPage page, String ID, String name, String value) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ID.toLowerCase());
		Params.Add(name);
		Params.Add(value);
		page.ws.RunFunction("SetProperty", Params);
		
	}
		
	public static void RemoveProperty(ABMPage page, String ID, String name) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ID.toLowerCase());
		Params.Add(name);
		page.ws.RunFunction("RemoveProperty", Params);
		
	}
		
	public static SimpleFuture GetProperty(ABMPage page, String ID, String name) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ID.toLowerCase());
		Params.Add(name);		
		SimpleFuture j = page.ws.RunFunctionWithResult("GetProperty", Params);
		return j;
	}	
	
	@Hide
	public static void SignOffSocialNetwork(ABMPage page, String network, String registrationId, String returnExtra) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(network);
		Params.Add(registrationId);
		Params.Add(returnExtra);
		page.ws.RunFunction("signout", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("SignOffSocialNetwork");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
		
	public static String HasClass(ABMPage page, String ID, String cl) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ID.toLowerCase());
		Params.Add(cl);		
		SimpleFuture j = page.ws.RunFunctionWithResult("HasClass", Params);
		if (j!=null) {
			try {
				return (String) j.getValue();
			} catch (InterruptedException e) {
				//e.printStackTrace();
			} catch (TimeoutException e) {
				//e.printStackTrace();
			} catch (ExecutionException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		return "";
	}
	
	
	
	@Hide
	public static Map<String,String> GetActiveHeaders(ABMPage page, String ID) {
		Map<String,String> ss = new HashMap<String,String>();
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ID.toLowerCase());				
		SimpleFuture j = page.ws.RunFunctionWithResult("getactiveheaders", Params);
		if (j!=null) {
			try {
				String s = (String) j.getValue();
				String[] split = s.split(";");
				for (String v: split) {
					ss.put(v.toLowerCase(), v);
				}
			} catch (InterruptedException e) {
				//e.printStackTrace();
			} catch (TimeoutException e) {
				//e.printStackTrace();
			} catch (ExecutionException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		return ss;
	}
	
		
	@Hide
	public static void SetSortColumn(ABMPage page, String id, String dataField, String order) {
		if (page.ws.getOpen()) {
			anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
			Params.Initialize();
			Params.Add(id.toLowerCase());
			Params.Add(dataField);
			Params.Add(order);
			page.ws.RunFunction("setsortcolumn", Params);
			try {
				if (page.ws.getOpen()) {
					if (page.ShowDebugFlush) {BA.Log("SetSortColumn");}
					page.ws.Flush();page.RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
	}
	
	@Hide
	public static void TreeTableCollapseAll(ABMPage page, String id) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());		
		page.ws.RunFunction("ttcollapseall", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("TreeTableCollapseAll");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void TreeTableExpandAll(ABMPage page, String id) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());		
		page.ws.RunFunction("ttexpandall", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("TreeTableExpandAll");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void TreeTableCollapse(ABMPage page, String id, String rowid) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		Params.Add(rowid);	
		page.ws.RunFunction("ttcollapse", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("TreeTableCollapse");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void TreeTableExpand(ABMPage page, String id, String rowid) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		Params.Add(rowid);
		page.ws.RunFunction("ttexpand", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("TreeTableExpand");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	
	@Hide
	public static void GetTableStringValue(ABMPage page, String ID, String cellId, TableValue value ) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ID.toLowerCase());
		Params.Add(cellId.toLowerCase());
		
		value.j = page.ws.RunFunctionWithResult("gettablestringvalue", Params);
		
	}
	
	@Hide
	public static void GetTableMutableStringValue(ABMPage page, String ID, String cellId, TableValueMutable value ) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ID.toLowerCase());
		Params.Add(cellId.toLowerCase());
		
		value.j = page.ws.RunFunctionWithResult("gettablestringvalue", Params);
		
	}
	
	@Hide
	public static void GetTableInfiniteStringValue(ABMPage page, String ID, String cellId, TableValueInfinite value ) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ID.toLowerCase());
		Params.Add(cellId.toLowerCase());
		
		value.j = page.ws.RunFunctionWithResult("gettablestringvalue", Params);
		
	}
	
	@Hide
	public static void SetActiveTableRow(ABMPage page, String id, String uniqueId) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		Params.Add(uniqueId);
		page.ws.RunFunction("setactivetablerow", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("SetActiveTableRow");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	
	@Hide
	public static void ShowModal(ABMPage page, String id, boolean isDismissible, int size) {
		ABMModalSheet modal = page.ModalSheet(id);
		if (modal!=null) {
			if (modal.IsOpen && modal.IsDismissible==false) {
				anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
				Params.Initialize();
				Params.Add(id.toLowerCase());
				page.ws.RunFunction("ReShowModal", Params);
				try {
					if (page.ws.getOpen()) {
						if (page.ShowDebugFlush) {BA.Log("ReShowModal 1");}
						page.ws.Flush();page.RunFlushed();
					}
				} catch (IOException e) {			
					//e.printStackTrace();
				}
				return;
			}
			modal.IsOpen=true;
		}
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		Params.Add(isDismissible);
		if (size==MODALSHEET_SIZE_FULL) {
			Params.Add("0%");
		} else {
			Params.Add("10%");
		}
		page.ws.RunFunction("ShowModal", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("ShowModal");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void ShowModalRelativeCell(ABMPage page, String id, boolean isDismissible, String parentCellId, int cellWidth, String fixedWidth) {
		ABMModalSheet modal = page.ModalSheet(id);
		if (modal!=null) {
			if (modal.IsOpen && modal.IsDismissible==false) {
				anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
				Params.Initialize();
				Params.Add(id.toLowerCase());
				page.ws.RunFunction("ReShowModal", Params);
				try {
					if (page.ws.getOpen()) {
						if (page.ShowDebugFlush) {BA.Log("ReShowModal 2");}
						page.ws.Flush();page.RunFlushed();
					}
				} catch (IOException e) {			
					//e.printStackTrace();
				}
				return;
			}
			modal.IsOpen=true;
		}
		
		if (!fixedWidth.equals("")) {
			ShowModalRelativeComponent(page, id, isDismissible, parentCellId, fixedWidth);
		} else {
			anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
			Params.Initialize();
			Params.Add(id.toLowerCase());
			Params.Add(isDismissible);
			Params.Add(parentCellId);
			Params.Add(cellWidth);
			page.ws.RunFunction("ShowModalRelativeCell", Params);
		}
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("ShowModalRelativeCell");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	@Hide
	public static void ShowModalRelativeComponent(ABMPage page, String id, boolean isDismissible, String parentComponentId, String fixedWidth) {
		ABMModalSheet modal = page.ModalSheet(id);
		if (modal!=null) {
			if (modal.IsOpen && modal.IsDismissible==false) {
				anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
				Params.Initialize();
				Params.Add(id.toLowerCase());
				page.ws.RunFunction("ReShowModal", Params);
				try {
					if (page.ws.getOpen()) {
						if (page.ShowDebugFlush) {BA.Log("ReShowModal 3");}
						page.ws.Flush();page.RunFlushed();
					}
				} catch (IOException e) {			
					//e.printStackTrace();
				}
				return;
			}
			modal.IsOpen=true;
		}
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		Params.Add(isDismissible);
		Params.Add(parentComponentId);
		Params.Add(fixedWidth);
		page.ws.RunFunction("ShowModalRelativeComponent", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("ShowModalRelativeComponent");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void ShowModalAbsolute(ABMPage page, String id, boolean isDismissible, String left, String top, String width, String height) {
		ABMModalSheet modal = page.ModalSheet(id);
		if (modal!=null) {
			if (modal.IsOpen && modal.IsDismissible==false) {
				anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
				Params.Initialize();
				Params.Add(id.toLowerCase());
				page.ws.RunFunction("ReShowModal", Params);
				try {
					if (page.ws.getOpen()) {
						if (page.ShowDebugFlush) {BA.Log("ReShowModal 4");}
						page.ws.Flush();page.RunFlushed();
					}
				} catch (IOException e) {			
					//e.printStackTrace();
				}
				return;
			}
			modal.IsOpen=true;
		}
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		Params.Add(isDismissible);
		Params.Add(left);
		Params.Add(top);
		Params.Add(width);
		Params.Add(height);
		page.ws.RunFunction("ShowModalAbsolute", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("ShowModalAbsolute");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void CloseModal(ABMPage page, String id) {
		ABMModalSheet modal = page.ModalSheet(id);
		if (modal!=null) {
			modal.IsOpen=false;
		}
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		page.ws.RunFunction("CloseModal", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("CloseModal");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void PlayYouTube(ABMPage page, String id) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		page.ws.RunFunction("playyoutube", Params);
		
	}
	
	@Hide
	public static void StopYouTube(ABMPage page, String id) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		page.ws.RunFunction("stopyoutube", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("StopYouTube");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void PauseYouTube(ABMPage page, String id) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		page.ws.RunFunction("pauseyoutube", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("PauseYouTube");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void SetVolumeYouTube(ABMPage page, String id, double volume) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		Params.Add(volume);
		page.ws.RunFunction("setvolumeyoutube", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("SetVolumeYouTube");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void MuteYouTube(ABMPage page, String id) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		page.ws.RunFunction("muteyoutube", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("MuteYouTube");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void UnMuteYouTube(ABMPage page, String id) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		page.ws.RunFunction("unmuteyoutube", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("UnMuteYouTube");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void InitEditEnterFields(ABMPage page) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		page.ws.RunFunction("InitEditEnterFields", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("InitEditEnterFields");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void PlayHTML5(ABMPage page, String id) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		page.ws.RunFunction("playhtml5", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("PlayHTML5");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void StopHTML5(ABMPage page, String id) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		page.ws.RunFunction("stophtml5", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("StopHTML5");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void PauseHTML5(ABMPage page, String id) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		page.ws.RunFunction("pausehtml5", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("PauseHTML5");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void SetVolumeHTML5(ABMPage page, String id, double volume) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		Params.Add(volume);
		page.ws.RunFunction("setvolumehtml5", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("SetVolumeHTML5");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void MuteHTML5(ABMPage page, String id) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		page.ws.RunFunction("mutehtml5", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("MuteHTML5");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void UnMuteHTML5(ABMPage page, String id) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		page.ws.RunFunction("unmutehtml5", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("UnMuteHTML5");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
		
	@Hide
	public static void CueYouTube(ABMPage page, String id, String videoId) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		Params.Add(videoId);
		page.ws.RunFunction("cueyoutube", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("CueYouTube");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void ChartUpdate(ABMPage page, String id, String data, String options) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		Params.Add(data);
		Params.Add(options);
		page.ws.RunFunction("chartupdate", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("ChartUpdate");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void CuePlayNextYouTube(ABMPage page, String id) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		page.ws.RunFunction("cueplaynextyoutube", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("CuePlayNextYouTube");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	
	@Hide
	public static void GMRefresh(ABMPage page, String id, int height, int width) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		Params.Add(height + "px");
		if (width==0) {
			Params.Add("100%");
		} else {
			Params.Add(width + "px");
		}
		
		page.ws.RunFunction("gmrefresh", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("GMRefresh");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	

	@Hide
	public static void GMRemoveMarkers(ABMPage page, String id) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		page.ws.RunFunction("gmremovemarkers", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("GMRemoveMarkers");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
		
	
    @Hide
	public static void GMRemoveMarker(ABMPage page, String id, String markerId) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		Params.Add(markerId.toLowerCase());
		page.ws.RunFunction("gmremovemarkerman", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("GMRemoveMarker");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}	
	
	@Hide
	public static void ShowPage(ABMPage page) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();		
		page.ws.RunFunction("ShowPage", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("ShowPage");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
		
	}
	
	@Hide
	public static void GMAddMarker(ABMPage page, String id, String markerId, double Latitude, double Longitude, String MarkerColor, String Title, String infoWindow, boolean Draggable, boolean showCrossOnDrag, boolean ShowDeleteInInfoWindow, anywheresoftware.b4a.objects.collections.List polygonIds) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		Params.Add(markerId.toLowerCase());
		Params.Add(Latitude);
		Params.Add(Longitude);
		Params.Add(MarkerColor);
		Params.Add(Title);
		Params.Add(infoWindow);
		if (Draggable) {
			Params.Add(showCrossOnDrag);
			Params.Add(ShowDeleteInInfoWindow);
			if (polygonIds!=null) {
				String pIds="[";
				for (int i=0;i<polygonIds.getSize();i++) {
					if (i>0) {
						pIds=pIds+",";
					}
					String s = (String) polygonIds.Get(i);
					pIds=pIds+"'" + s + "'";
				}
				pIds=pIds+"]";
				Params.Add(pIds);
			} else {
				Params.Add("[]");
			}
			page.ws.RunFunction("gmaddmarkerdraggable", Params);
		} else {
			Params.Add(ShowDeleteInInfoWindow);
			if (polygonIds!=null) {
				String pIds="[";
				for (int i=0;i<polygonIds.getSize();i++) {
					if (i>0) {
						pIds=pIds+",";
					}
					String s = (String) polygonIds.Get(i);
					pIds=pIds+"'" + s + "'";
				}
				pIds=pIds+"]";
				Params.Add(pIds);
			} else {
				Params.Add("[]");
			}
			page.ws.RunFunction("gmaddmarker", Params);
		}
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("GMAddMarker");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void GMAddMarkerEx(ABMPage page, String id, String markerId, double Latitude, double Longitude, String Title, String infoWindow, String iconPNGUrl, boolean permanent, boolean Draggable, boolean showCrossOnDrag, boolean ShowDeleteInInfoWindow, anywheresoftware.b4a.objects.collections.List polygonIds) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		Params.Add(markerId.toLowerCase());
		Params.Add(Latitude);
		Params.Add(Longitude);
		Params.Add(Title);
		Params.Add(infoWindow);
		Params.Add(iconPNGUrl);
		Params.Add(permanent);
		if (Draggable) {
			Params.Add(showCrossOnDrag);
			Params.Add(ShowDeleteInInfoWindow);
			if (polygonIds!=null) {
				String pIds="[";
				for (int i=0;i<polygonIds.getSize();i++) {
					if (i>0) {
						pIds=pIds+",";
					}
					String s = (String) polygonIds.Get(i);
					pIds=pIds+"'" + s + "'";
				}
				pIds=pIds+"]";
				Params.Add(pIds);
			} else {
				Params.Add("[]");
			}
			page.ws.RunFunction("gmaddmarkerexdraggable", Params);
		} else {
			Params.Add(ShowDeleteInInfoWindow);
			if (polygonIds!=null) {
				String pIds="[";
				for (int i=0;i<polygonIds.getSize();i++) {
					if (i>0) {
						pIds=pIds+",";
					}
					String s = (String) polygonIds.Get(i);
					pIds=pIds+"'" + s + "'";
				}
				pIds=pIds+"]";
				Params.Add(pIds);
			} else {
				Params.Add("[]");
			}
			page.ws.RunFunction("gmaddmarkerex", Params);
		}
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("GMAddMarkerEx");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void GMAddMarkerEx2(ABMPage page, String id, String markerId, double Latitude, double Longitude, String Title, String infoWindow, String iconPNGUrl, boolean permanent, boolean Draggable, double anchorPointX, double anchorPointY, boolean showCrossOnDrag, boolean ShowDeleteInInfoWindow, anywheresoftware.b4a.objects.collections.List polygonIds) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		Params.Add(markerId.toLowerCase());
		Params.Add(Latitude);
		Params.Add(Longitude);
		Params.Add(Title);
		Params.Add(infoWindow);
		Params.Add(iconPNGUrl);
		Params.Add(permanent);
		Params.Add(anchorPointX);
		Params.Add(anchorPointY);
		if (Draggable) {
			Params.Add(showCrossOnDrag);
			Params.Add(ShowDeleteInInfoWindow);
			if (polygonIds!=null) {
				String pIds="[";
				for (int i=0;i<polygonIds.getSize();i++) {
					if (i>0) {
						pIds=pIds+",";
					}
					String s = (String) polygonIds.Get(i);
					pIds=pIds+"'" + s + "'";
				}
				pIds=pIds+"]";
				Params.Add(pIds);
			} else {
				Params.Add("[]");
			}
			page.ws.RunFunction("gmaddmarkerex2draggable", Params);
		} else {
			Params.Add(ShowDeleteInInfoWindow);
			if (polygonIds!=null) {
				String pIds="[";
				for (int i=0;i<polygonIds.getSize();i++) {
					if (i>0) {
						pIds=pIds+",";
					}
					String s = (String) polygonIds.Get(i);
					pIds=pIds+"'" + s + "'";
				}
				pIds=pIds+"]";
				Params.Add(pIds);
			} else {
				Params.Add("[]");
			}
			page.ws.RunFunction("gmaddmarkerex2", Params);
		}		
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("GMAddMarkerEx2");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void GMFitZoom(ABMPage page, String id) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		page.ws.RunFunction("gmfitzoom", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("GMFitZoom");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static int GMGetZoom(ABMPage page, String id) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		SimpleFuture j = page.ws.RunFunctionWithResult("gmgetzoom", Params);
		if (j!=null) {
			try {
				return (Integer) j.getValue();			
			} catch (InterruptedException e) {
				//e.printStackTrace();
			} catch (TimeoutException e) {
				//e.printStackTrace();
			} catch (ExecutionException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		return -99999; 
	}
	
	public static void GMSetZoom(ABMPage page, String id, int zoom) {		
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		Params.Add(zoom);
		page.ws.RunFunction("gmsetzoom", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("GMFitZoom");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void GMGeoLocate(ABMPage page, String id) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		page.ws.RunFunction("gmgeolocate", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("GMGeoLocate");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void GMGeoCode(ABMPage page, String id, String address) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		Params.Add(address);
		page.ws.RunFunction("gmgeocode", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("GMGeoCode");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static String GMReverseGeoCode(ABMPage page, String id, double lat, double lng) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		Params.Add(lat);
		Params.Add(lng);
		SimpleFuture j = page.ws.RunFunctionWithResult("gmreversegeocode", Params);
		if (j!=null) {
			try {
				return (String) j.getValue();			
			} catch (InterruptedException e) {
				//e.printStackTrace();
			} catch (TimeoutException e) {
				//e.printStackTrace();
			} catch (ExecutionException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		return "";		
	}
	
	@Hide
	public static void GMSetLocation(ABMPage page, String id, double Latitude, double Longitude) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		Params.Add(Latitude);
		Params.Add(Longitude);
		page.ws.RunFunction("gmsetlocation", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("GMSetLocation");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void GMRemovePolyLines(ABMPage page, String id) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		page.ws.RunFunction("gmremovepolylines", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("GMRemovePolyLines");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void GMRemovePolyLine(ABMPage page, String id, String PolylineId) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		Params.Add(PolylineId.toLowerCase());
		page.ws.RunFunction("gmremovepolyline", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("GMRemovePolyLines");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void GMAddPolyLine(ABMPage page, String id, anywheresoftware.b4a.objects.collections.List path, String StrokeColor, String StrokeColorIntensity, double StrokeOpacity, int StrokeWeight, String PolylineId) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		StringBuilder p = new StringBuilder();
		p.append("[");
		for (int index=0;index<path.getSize();index+=2) {
			double d1 = (double) path.Get(index);
			double d2 = (double) path.Get(index+1);
			p.append("[" + d1 + "," + d2 + "]");
			if (index<path.getSize()-2) {
				p.append(",");
			}
		}
		p.append("]");
		Params.Add(p);
		Params.Add(GetColorStrMap(StrokeColor, StrokeColorIntensity));
		Params.Add(StrokeOpacity);;
		Params.Add(StrokeWeight);
		Params.Add(PolylineId.toLowerCase());
		page.ws.RunFunction("gmaddpolyline", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("GMAddPolyLine");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void GMAddPolygon(ABMPage page, String id, anywheresoftware.b4a.objects.collections.List path, String StrokeColor, String StrokeColorIntensity, double StrokeOpacity, int StrokeWeight, String FillColor, String FillColorIntensity, double FillOpacity, String PolygonId) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		StringBuilder p = new StringBuilder();
		p.append("[");
		for (int index=0;index<path.getSize();index+=2) {
			double d1 = (double) path.Get(index);
			double d2 = (double) path.Get(index+1);
			p.append("[" + d1 + "," + d2 + "]");
			if (index<path.getSize()-2) {
				p.append(",");
			}
		}
		p.append("]");
		Params.Add(p);
		Params.Add(GetColorStrMap(StrokeColor, StrokeColorIntensity));
		Params.Add(StrokeOpacity);;
		Params.Add(StrokeWeight);
		Params.Add(GetColorStrMap(FillColor, FillColorIntensity));
		Params.Add(FillOpacity);
		Params.Add(PolygonId.toLowerCase());
		page.ws.RunFunction("gmaddpolygon", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("GMAddPolygon");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void GMRemovePolygons(ABMPage page, String id) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		page.ws.RunFunction("gmremovepolygons", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("GMRemovePolygons");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void GMRemovePolygon(ABMPage page, String id, String PolygonId) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		Params.Add(PolygonId.toLowerCase());
		page.ws.RunFunction("gmremovepolygon", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("GMRemovePolygons");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void GMAddRoute(ABMPage page, String id, double OriginLatitude, double OriginLongitude, double DestinationLatitude, double DestinationLongitude, String travelMode, String StrokeColor, String StrokeColorIntensity, double StrokeOpacity, int StrokeWeight) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		Params.Add("[" + OriginLatitude + "," + OriginLongitude + "]");
		Params.Add("[" + DestinationLatitude + "," + DestinationLongitude + "]");		
		Params.Add(travelMode);
		Params.Add(GetColorStrMap(StrokeColor, StrokeColorIntensity));
		Params.Add(StrokeOpacity);
		Params.Add(StrokeWeight);		
		page.ws.RunFunction("gmaddroute", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("GMAddRoute");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static void GMRemoveRoutes(ABMPage page, String id) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(id.toLowerCase());
		page.ws.RunFunction("gmremoveroutes", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("GMRemoveRoutes");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide
	public static String GetActiveTab(ABMPage page, String tabsId) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(tabsId.toLowerCase());
		SimpleFuture j = page.ws.RunFunctionWithResult("getactivetab", Params);
		if (j!=null) {
			try {
				return (String) j.getValue();
			} catch (InterruptedException e) {
				//e.printStackTrace();
			} catch (TimeoutException e) {
				//e.printStackTrace();
			} catch (ExecutionException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		return "";
	}
	
	@Hide 
	public static String GetActiveRadioButton(ABMPage page, String groupId) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(groupId.toLowerCase());
		SimpleFuture j = page.ws.RunFunctionWithResult("getactiveradiobutton", Params);		
		if (j!=null) {
			try {
				return (String) j.getValue();
			} catch (InterruptedException e) {
				//e.printStackTrace();
			} catch (TimeoutException e) {
				//e.printStackTrace();
			} catch (ExecutionException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		return "";
	}
	
	@Hide
	public static String GetDrawingURI(ABMPage page, String Id, String fileName, String handler) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(Id.toLowerCase());
		Params.Add(fileName);
		Params.Add(handler);
		String ext = fileName;
		int last = ext.lastIndexOf(".");
		if (last>-1) {
			ext = ext.substring(last+1).toLowerCase();
			if (ext.equals("jpg")) {
				ext = "jpeg";
			}
		} else {
			ext = "jpeg";
			fileName=fileName + ".jpeg";
		}
		Params.Add(ext);
		SimpleFuture j = page.ws.RunFunctionWithResult("getDataURLSignaturePad", Params);
		if (j!=null) {
			try {
				return (String) j.getValue();			
			} catch (InterruptedException e) {
				//e.printStackTrace();
			} catch (TimeoutException e) {
				//e.printStackTrace();
			} catch (ExecutionException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		return "";
	}	
	
	@Hide
	public static String GetDrawingURICanvas(ABMPage page, String Id, String fileName, String handler) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(Id.toLowerCase());
		Params.Add(fileName);
		Params.Add(handler);
		String ext = fileName;
		int last = ext.lastIndexOf(".");
		if (last>-1) {
			ext = ext.substring(last+1).toLowerCase();
			if (ext.equals("jpg")) {
				ext = "jpeg";
			}
		} else {
			ext = "jpeg";
			fileName=fileName + ".jpeg";
		}
		Params.Add(ext);
		BA.Log("Running getDataURLCanvas");
		SimpleFuture j = page.ws.RunFunctionWithResult("getDataURLCanvas", Params);
		if (j!=null) {
			try {
				return (String) j.getValue();			
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
			
	@Hide 
	public static void ClearSignature(ABMPage page, String Id) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(Id.toLowerCase());
		page.ws.RunFunction("clearSignaturePad", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("ClearSignature");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	public static void SaveInLocalStorage(ABMPage page, String key, String value) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();		
		Params.Add(key);
		Params.Add(value);
		page.ws.RunFunction("customsaveinlocalstorage", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("SaveInLocalStorage");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	public static String LoadFromLocalStorage(ABMPage page, String key) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();		
		Params.Add(key);
		SimpleFuture j = page.ws.RunFunctionWithResult("customloadfromlocalstorage", Params);
		if (j!=null) {
			try {
				return (String) j.getValue();			
			} catch (InterruptedException e) {
				//e.printStackTrace();
			} catch (TimeoutException e) {
				//e.printStackTrace();
			} catch (ExecutionException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		return "";
	}	
	
	public static void DeleteFromLocalStorage(ABMPage page, String key) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(key);
		page.ws.RunFunction("customdeletefromlocalstorage", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("DeleteFromLocalStorage");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	public static void SaveLogin(ABMPage page, String appName, String login, String pwd) {
		if (page.ws!=null) {
			if (page.ws.getOpen()) {
				anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
				Params.Initialize();
				Params.Add(appName);
				Params.Add(login);
				Params.Add(pwd);
				page.ws.RunFunction("saveinlocalstorage", Params);
				try {
					if (page.ws.getOpen()) {
						if (page.ShowDebugFlush) {BA.Log("SaveLogin");}
						page.ws.Flush();page.RunFlushed();
					}
				} catch (IOException e) {			
					//e.printStackTrace();
				}
			}
		}
	}
	
	public static String LoadLogin(ABMPage page, String appName) {
		if (page.ws!=null) {
			if (page.ws.getOpen()) {
				anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
				Params.Initialize();
				Params.Add(appName);
				SimpleFuture j = page.ws.RunFunctionWithResult("loadfromlocalstorage", Params);
				if (j!=null) {
					try {
						return (String) j.getValue();			
					} catch (InterruptedException e) {
						//e.printStackTrace();
					} catch (TimeoutException e) {
						//e.printStackTrace();
					} catch (ExecutionException e) {
						//e.printStackTrace();
					} catch (IOException e) {
						//e.printStackTrace();
					}
				}
			}
		}		
		return "";
	}
	
	public static void DeleteLogin(ABMPage page, String appName) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(appName);
		page.ws.RunFunction("clearlocalstorage", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("DeleteLogin");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	public static void SaveLogin2(ABMPage page, String appName, String loginPwd) {
		if (page.ws!=null) {
			if (page.ws.getOpen()) {
				anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
				Params.Initialize();
				Params.Add(appName);
				Params.Add(loginPwd);
				page.ws.RunFunction("saveinlocalstorage2", Params);
				try {
					if (page.ws.getOpen()) {
						if (page.ShowDebugFlush) {BA.Log("SaveLogin2");}
						page.ws.Flush();page.RunFlushed();
					}
				} catch (IOException e) {			
					//e.printStackTrace();
				}
			}
		}
	}
	
	public static String LoadLogin2(ABMPage page, String appName) {
		if (page.ws!=null) {
			if (page.ws.getOpen()) {
				anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
				Params.Initialize();
				Params.Add(appName);
				SimpleFuture j = page.ws.RunFunctionWithResult("loadfromlocalstorage2", Params);
				if (j!=null) {
					try {
						return (String) j.getValue();			
					} catch (InterruptedException e) {
						//e.printStackTrace();
					} catch (TimeoutException e) {
						//e.printStackTrace();
					} catch (ExecutionException e) {
						//e.printStackTrace();
					} catch (IOException e) {
						//e.printStackTrace();
					}
				}
			}
		}		
		return "";
	}
	
	public static void DeleteLogin2(ABMPage page, String appName) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(appName);
		page.ws.RunFunction("clearlocalstorage2", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("DeleteLogin2");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	public static String GetBrowserWidthHeight(ABMPage page) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		SimpleFuture j = page.ws.RunFunctionWithResult("getbrowserwidthheight", Params);
		if (j!=null) {
			try {
				return (String) j.getValue();			
			} catch (InterruptedException e) {
				//e.printStackTrace();
			} catch (TimeoutException e) {
				//e.printStackTrace();
			} catch (ExecutionException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		return "";
	}
	
	protected static String CookieGet(ABMPage page, String key) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(key);
		SimpleFuture j = page.ws.RunFunctionWithResult("cookieGet", Params);
		if (j!=null) {
			try {
				return (String) j.getValue();			
			} catch (InterruptedException e) {
				//e.printStackTrace();
			} catch (TimeoutException e) {
				//e.printStackTrace();
			} catch (ExecutionException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		return "";
	}
			
	@Hide 
	public static void CALSetEvents(ABMPage page, String Id, String events) {		
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(Id.toLowerCase());
		Params.Add(events);
		page.ws.RunFunction("calsetevents", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("CALSetEvents");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide 
	public static void CALAddEvent(ABMPage page, String Id, String event) {		
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(Id.toLowerCase());
		Params.Add(event);
		page.ws.RunFunction("caladdevent", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("CALAddEvent");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide 
	public static void CALUpdateEvent(ABMPage page, String Id, String event) {		
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(Id.toLowerCase());
		Params.Add(event);
		page.ws.RunFunction("calupdateevent", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("CALUpdateEvent");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide 
	public static void CALRemoveEvent(ABMPage page, String Id, String eventId) {		
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(Id.toLowerCase());
		Params.Add(eventId);
		page.ws.RunFunction("calremoveevent", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("CALRemoveEvent");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide 
	public static void CALRefresh(ABMPage page, String Id) {	
		BA.Log("Refreshing calendar");
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(Id.toLowerCase());
		page.ws.RunFunction("calrefresh", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("CALRefresh");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide 
	public static void CALChangeView(ABMPage page, String Id, String newView) {	
		
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(Id.toLowerCase());
		Params.Add(newView);
		page.ws.RunFunction("calchangeview", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("CALChangeView");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide 
	public static void CALChangeDefaultView(ABMPage page, String Id, String newView) {	
		
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(Id.toLowerCase());
		Params.Add(newView);
		page.ws.RunFunction("calchangedefaultview", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("CALChangeDefaultView");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide 
	public static void CALChangeEventsEditable(ABMPage page, String Id, boolean editable) {	
		
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(Id.toLowerCase());
		Params.Add(editable);
		page.ws.RunFunction("calchangeeventseditable", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("CALChangeEventsEditable");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	@Hide 
	public static void CALRefetchData(ABMPage page, String Id) {	
		
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(Id.toLowerCase());
		page.ws.RunFunction("calrefetchdata", Params);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("CALRefetchData");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	
	@Hide 
	public static String CALGetTitle(ABMPage page, String Id) {		
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(Id.toLowerCase());
		SimpleFuture j = page.ws.RunFunctionWithResult("calgettitle", Params);
		if (j!=null) {
			try {
				return (String) j.getValue();			
			} catch (InterruptedException e) {
				//e.printStackTrace();
			} catch (TimeoutException e) {
				//e.printStackTrace();
			} catch (ExecutionException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		return "";
	}
	
	@Hide
	public static void CanvasRefresh(ABMPage page, ABMCanvas c, boolean doDraw) {
		anywheresoftware.b4a.objects.collections.List TopAllParams = new anywheresoftware.b4a.objects.collections.List();
		TopAllParams.Initialize();
		
		anywheresoftware.b4a.objects.collections.List AllParams = new anywheresoftware.b4a.objects.collections.List();
		AllParams.Initialize();
		AllParams.Add(c.ParentString + c.ArrayName.toLowerCase() + c.ID.toLowerCase());
		for (Entry<String, String> comp : c.CanvasCommands.entrySet()) {
			anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
			Params.Initialize();
			anywheresoftware.b4a.objects.collections.List Commands = new anywheresoftware.b4a.objects.collections.List();
			Commands.Initialize();
			ABMCanvasObject obj = c.GetObject(comp.getKey());
			switch (comp.getValue()) {
			case "0": // add
				Params.Clear();
				Params.Add("0");
				Params.Add(obj.ID.toLowerCase());
				Params.Add(obj.Type);
				Params.Add(true); // changed x
				Params.Add(true); // changed y
				Params.Add(obj.X);
				Params.Add(obj.Y);
				Params.Add(obj.Width);
				Params.Add(obj.Height);
				Params.Add(obj.Radius);
				Params.Add(obj.Draggable);
				Params.Add(obj.DraggableLeft);
				Params.Add(obj.DraggableTop);
				Params.Add(obj.DraggableWidth);
				Params.Add(obj.DraggableHeight);
				Params.Add(obj.mClickable);
				obj.XChanged=false;
				obj.YChanged=false;
				for (ABMCanvasCommand ss: obj.Commands) {
					Commands.Add(ss.GetParams());
				}
				Params.Add(Commands);
				Params.Add(c.ArrayName.toLowerCase() + c.ID.toLowerCase());
				AllParams.Add(Params);
				break;
			case "1": // update
				Params.Clear();
				Params.Add("1");
				Params.Add(obj.ID.toLowerCase());
				Params.Add(obj.Type);
				Params.Add(obj.XChanged); 
				Params.Add(obj.YChanged); 
				Params.Add(obj.X);
				Params.Add(obj.Y);
				Params.Add(obj.Width);
				Params.Add(obj.Height);
				Params.Add(obj.Radius);
				Params.Add(obj.Draggable);
				Params.Add(obj.DraggableLeft);
				Params.Add(obj.DraggableTop);
				Params.Add(obj.DraggableWidth);
				Params.Add(obj.DraggableHeight);
				Params.Add(obj.mClickable);
				obj.XChanged=false;
				obj.YChanged=false;
				
				for (ABMCanvasCommand ss: obj.Commands) {
					Commands.Add(ss.GetParams());
				}
				Params.Add(Commands);
				Params.Add(c.ArrayName.toLowerCase() + c.ID.toLowerCase());
				AllParams.Add(Params);
				break;
			case "2": // remove
				Params.Clear();
				Params.Add("2");				
				Params.Add(obj.ID.toLowerCase());
				c.Objects.remove(comp.getKey());
				AllParams.Add(Params);
				break;
			}
		}

		if (page.ws!=null) {
			TopAllParams.Add(AllParams);
			if (doDraw) {
				page.ws.RunFunction("updateinnercanvas", TopAllParams);
			} else {
				page.ws.RunFunction("initinnercanvas", TopAllParams);
			}
			try {
				if (page.ws.getOpen()) {
					if (page.ShowDebugFlush) {BA.Log("CanvasRefresh");}
					page.ws.Flush();page.RunFlushed();
				}
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}
	}
	
	@Hide 
	public static ABMPoint CanvasGetPosition(ABMPage page, String canvasId, String objectId) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(canvasId.toLowerCase());
		Params.Add(objectId.toLowerCase());
		SimpleFuture j = page.ws.RunFunctionWithResult("canvasposition", Params);
		if (j!=null) {
			try {
				String s = (String) j.getValue();
				String[] parts = s.split(";");
				ABMPoint p = new ABMPoint();
				p.x = Double.parseDouble(parts[0]);
				p.y = Double.parseDouble(parts[1]);
				return p;
			} catch (InterruptedException e) {
				//e.printStackTrace();
			} catch (TimeoutException e) {
				//e.printStackTrace();
			} catch (ExecutionException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		return null;
	}
		
	private static void BuildColorMap() {
		colorMap.put("red lighten-5","#ffebee");
		colorMap.put("red lighten-4","#ffcdd2");
		colorMap.put("red lighten-3","#ef9a9a");
		colorMap.put("red lighten-2","#e57373");
		colorMap.put("red lighten-1","#ef5350");
		colorMap.put("red","#f44336");
		colorMap.put("red darken-1","#e53935");
		colorMap.put("red darken-2","#d32f2f");
		colorMap.put("red darken-3","#c62828");
		colorMap.put("red darken-4","#b71c1c");
		colorMap.put("red accent-1","#ff8a80");
		colorMap.put("red accent-2","#ff5252");
		colorMap.put("red accent-3","#ff1744");
		colorMap.put("red accent-4","#d50000");
		colorMap.put("pink lighten-5","#fce4ec");
		colorMap.put("pink lighten-4","#f8bbd0");
		colorMap.put("pink lighten-3","#f48fb1");
		colorMap.put("pink lighten-2","#f06292");
		colorMap.put("pink lighten-1","#ec407a");
		colorMap.put("pink","#e91e63");
		colorMap.put("pink darken-1","#d81b60");
		colorMap.put("pink darken-2","#c2185b");
		colorMap.put("pink darken-3","#ad1457");
		colorMap.put("pink darken-4","#880e4f");
		colorMap.put("pink accent-1","#ff80ab");
		colorMap.put("pink accent-2","#ff4081");
		colorMap.put("pink accent-3","#f50057");
		colorMap.put("pink accent-4","#c51162");
		colorMap.put("purple lighten-5","#f3e5f5");
		colorMap.put("purple lighten-4","#e1bee7");
		colorMap.put("purple lighten-3","#ce93d8");
		colorMap.put("purple lighten-2","#ba68c8");
		colorMap.put("purple lighten-1","#ab47bc");
		colorMap.put("purple","#9c27b0");
		colorMap.put("purple darken-1","#8e24aa");
		colorMap.put("purple darken-2","#7b1fa2");
		colorMap.put("purple darken-3","#6a1b9a");
		colorMap.put("purple darken-4","#4a148c");
		colorMap.put("purple accent-1","#ea80fc");
		colorMap.put("purple accent-2","#e040fb");
		colorMap.put("purple accent-3","#d500f9");
		colorMap.put("purple accent-4","#aa00ff");
		colorMap.put("deep-purple lighten-5","#ede7f6");
		colorMap.put("deep-purple lighten-4","#d1c4e9");
		colorMap.put("deep-purple lighten-3","#b39ddb");
		colorMap.put("deep-purple lighten-2","#9575cd");
		colorMap.put("deep-purple lighten-1","#7e57c2");
		colorMap.put("deep-purple","#673ab7");
		colorMap.put("deep-purple darken-1","#5e35b1");
		colorMap.put("deep-purple darken-2","#512da8");
		colorMap.put("deep-purple darken-3","#4527a0");
		colorMap.put("deep-purple darken-4","#311b92");
		colorMap.put("deep-purple accent-1","#b388ff");
		colorMap.put("deep-purple accent-2","#7c4dff");
		colorMap.put("deep-purple accent-3","#651fff");
		colorMap.put("deep-purple accent-4","#6200ea");
		colorMap.put("indigo lighten-5","#e8eaf6");
		colorMap.put("indigo lighten-4","#c5cae9");
		colorMap.put("indigo lighten-3","#9fa8da");
		colorMap.put("indigo lighten-2","#7986cb");
		colorMap.put("indigo lighten-1","#5c6bc0");
		colorMap.put("indigo","#3f51b5");
		colorMap.put("indigo darken-1","#3949ab");
		colorMap.put("indigo darken-2","#303f9f");
		colorMap.put("indigo darken-3","#283593");
		colorMap.put("indigo darken-4","#1a237e");
		colorMap.put("indigo accent-1","#8c9eff");
		colorMap.put("indigo accent-2","#536dfe");
		colorMap.put("indigo accent-3","#3d5afe");
		colorMap.put("indigo accent-4","#304ffe");
		colorMap.put("blue lighten-5","#e3f2fd");
		colorMap.put("blue lighten-4","#bbdefb");
		colorMap.put("blue lighten-3","#90caf9");
		colorMap.put("blue lighten-2","#64b5f6");
		colorMap.put("blue lighten-1","#42a5f5");
		colorMap.put("blue","#2196f3");
		colorMap.put("blue darken-1","#1e88e5");
		colorMap.put("blue darken-2","#1976d2");
		colorMap.put("blue darken-3","#1565c0");
		colorMap.put("blue darken-4","#0d47a1");
		colorMap.put("blue accent-1","#82b1ff");
		colorMap.put("blue accent-2","#448aff");
		colorMap.put("blue accent-3","#2979ff");
		colorMap.put("blue accent-4","#2962ff");
		colorMap.put("light-blue lighten-5","#e1f5fe");
		colorMap.put("light-blue lighten-4","#b3e5fc");
		colorMap.put("light-blue lighten-3","#81d4fa");
		colorMap.put("light-blue lighten-2","#4fc3f7");
		colorMap.put("light-blue lighten-1","#29b6f6");
		colorMap.put("light-blue","#03a9f4");
		colorMap.put("light-blue darken-1","#039be5");
		colorMap.put("light-blue darken-2","#0288d1");
		colorMap.put("light-blue darken-3","#0277bd");
		colorMap.put("light-blue darken-4","#01579b");
		colorMap.put("light-blue accent-1","#80d8ff");
		colorMap.put("light-blue accent-2","#40c4ff");
		colorMap.put("light-blue accent-3","#00b0ff");
		colorMap.put("light-blue accent-4","#0091ea");
		colorMap.put("cyan lighten-5","#e0f7fa");
		colorMap.put("cyan lighten-4","#b2ebf2");
		colorMap.put("cyan lighten-3","#80deea");
		colorMap.put("cyan lighten-2","#4dd0e1");
		colorMap.put("cyan lighten-1","#26c6da");
		colorMap.put("cyan","#00bcd4");
		colorMap.put("cyan darken-1","#00acc1");
		colorMap.put("cyan darken-2","#0097a7");
		colorMap.put("cyan darken-3","#00838f");
		colorMap.put("cyan darken-4","#006064");
		colorMap.put("cyan accent-1","#84ffff");
		colorMap.put("cyan accent-2","#18ffff");
		colorMap.put("cyan accent-3","#00e5ff");
		colorMap.put("cyan accent-4","#00b8d4");
		colorMap.put("teal lighten-5","#e0f2f1");
		colorMap.put("teal lighten-4","#b2dfdb");
		colorMap.put("teal lighten-3","#80cbc4");
		colorMap.put("teal lighten-2","#4db6ac");
		colorMap.put("teal lighten-1","#26a69a");
		colorMap.put("teal","#009688");
		colorMap.put("teal darken-1","#00897b");
		colorMap.put("teal darken-2","#00796b");
		colorMap.put("teal darken-3","#00695c");
		colorMap.put("teal darken-4","#004d40");
		colorMap.put("teal accent-1","#a7ffeb");
		colorMap.put("teal accent-2","#64ffda");
		colorMap.put("teal accent-3","#1de9b6");
		colorMap.put("teal accent-4","#00bfa5");
		colorMap.put("green lighten-5","#e8f5e9");
		colorMap.put("green lighten-4","#c8e6c9");
		colorMap.put("green lighten-3","#a5d6a7");
		colorMap.put("green lighten-2","#81c784");
		colorMap.put("green lighten-1","#66bb6a");
		colorMap.put("green","#4caf50");
		colorMap.put("green darken-1","#43a047");
		colorMap.put("green darken-2","#388e3c");
		colorMap.put("green darken-3","#2e7d32");
		colorMap.put("green darken-4","#1b5e20");
		colorMap.put("green accent-1","#b9f6ca");
		colorMap.put("green accent-2","#69f0ae");
		colorMap.put("green accent-3","#00e676");
		colorMap.put("green accent-4","#00c853");
		colorMap.put("light-green lighten-5","#f1f8e9");
		colorMap.put("light-green lighten-4","#dcedc8");
		colorMap.put("light-green lighten-3","#c5e1a5");
		colorMap.put("light-green lighten-2","#aed581");
		colorMap.put("light-green lighten-1","#9ccc65");
		colorMap.put("light-green","#8bc34a");
		colorMap.put("light-green darken-1","#7cb342");
		colorMap.put("light-green darken-2","#689f38");
		colorMap.put("light-green darken-3","#558b2f");
		colorMap.put("light-green darken-4","#33691e");
		colorMap.put("light-green accent-1","#ccff90");
		colorMap.put("light-green accent-2","#b2ff59");
		colorMap.put("light-green accent-3","#76ff03");
		colorMap.put("light-green accent-4","#64dd17");
		colorMap.put("lime lighten-5","#f9fbe7");
		colorMap.put("lime lighten-4","#f0f4c3");
		colorMap.put("lime lighten-3","#e6ee9c");
		colorMap.put("lime lighten-2","#dce775");
		colorMap.put("lime lighten-1","#d4e157");
		colorMap.put("lime","#cddc39");
		colorMap.put("lime darken-1","#c0ca33");
		colorMap.put("lime darken-2","#afb42b");
		colorMap.put("lime darken-3","#9e9d24");
		colorMap.put("lime darken-4","#827717");
		colorMap.put("lime accent-1","#f4ff81");
		colorMap.put("lime accent-2","#eeff41");
		colorMap.put("lime accent-3","#c6ff00");
		colorMap.put("lime accent-4","#aeea00");
		colorMap.put("yellow lighten-5","#fffde7");
		colorMap.put("yellow lighten-4","#fff9c4");
		colorMap.put("yellow lighten-3","#fff59d");
		colorMap.put("yellow lighten-2","#fff176");
		colorMap.put("yellow lighten-1","#ffee58");
		colorMap.put("yellow","#ffeb3b");
		colorMap.put("yellow darken-1","#fdd835");
		colorMap.put("yellow darken-2","#fbc02d");
		colorMap.put("yellow darken-3","#f9a825");
		colorMap.put("yellow darken-4","#f57f17");
		colorMap.put("yellow accent-1","#ffff8d");
		colorMap.put("yellow accent-2","#ffff00");
		colorMap.put("yellow accent-3","#ffea00");
		colorMap.put("yellow accent-4","#ffd600");
		colorMap.put("amber lighten-5","#fff8e1");
		colorMap.put("amber lighten-4","#ffecb3");
		colorMap.put("amber lighten-3","#ffe082");
		colorMap.put("amber lighten-2","#ffd54f");
		colorMap.put("amber lighten-1","#ffca28");
		colorMap.put("amber","#ffc107");
		colorMap.put("amber darken-1","#ffb300");
		colorMap.put("amber darken-2","#ffa000");
		colorMap.put("amber darken-3","#ff8f00");
		colorMap.put("amber darken-4","#ff6f00");
		colorMap.put("amber accent-1","#ffe57f");
		colorMap.put("amber accent-2","#ffd740");
		colorMap.put("amber accent-3","#ffc400");
		colorMap.put("amber accent-4","#ffab00");
		colorMap.put("orange lighten-5","#fff3e0");
		colorMap.put("orange lighten-4","#ffe0b2");
		colorMap.put("orange lighten-3","#ffcc80");
		colorMap.put("orange lighten-2","#ffb74d");
		colorMap.put("orange lighten-1","#ffa726");
		colorMap.put("orange","#ff9800");
		colorMap.put("orange darken-1","#fb8c00");
		colorMap.put("orange darken-2","#f57c00");
		colorMap.put("orange darken-3","#ef6c00");
		colorMap.put("orange darken-4","#e65100");
		colorMap.put("orange accent-1","#ffd180");
		colorMap.put("orange accent-2","#ffab40");
		colorMap.put("orange accent-3","#ff9100");
		colorMap.put("orange accent-4","#ff6d00");
		colorMap.put("deep-orange lighten-5","#fbe9e7");
		colorMap.put("deep-orange lighten-4","#ffccbc");
		colorMap.put("deep-orange lighten-3","#ffab91");
		colorMap.put("deep-orange lighten-2","#ff8a65");
		colorMap.put("deep-orange lighten-1","#ff7043");
		colorMap.put("deep-orange","#ff5722");
		colorMap.put("deep-orange darken-1","#f4511e");
		colorMap.put("deep-orange darken-2","#e64a19");
		colorMap.put("deep-orange darken-3","#d84315");
		colorMap.put("deep-orange darken-4","#bf360c");
		colorMap.put("deep-orange accent-1","#ff9e80");
		colorMap.put("deep-orange accent-2","#ff6e40");
		colorMap.put("deep-orange accent-3","#ff3d00");
		colorMap.put("deep-orange accent-4","#dd2c00");
		colorMap.put("brown lighten-5","#efebe9");
		colorMap.put("brown lighten-4","#d7ccc8");
		colorMap.put("brown lighten-3","#bcaaa4");
		colorMap.put("brown lighten-2","#a1887f");
		colorMap.put("brown lighten-1","#8d6e63");
		colorMap.put("brown","#795548");
		colorMap.put("brown darken-1","#6d4c41");
		colorMap.put("brown darken-2","#5d4037");
		colorMap.put("brown darken-3","#4e342e");
		colorMap.put("brown darken-4","#3e2723");
		colorMap.put("grey lighten-5","#fafafa");
		colorMap.put("grey lighten-4","#f5f5f5");
		colorMap.put("grey lighten-3","#eeeeee");
		colorMap.put("grey lighten-2","#e0e0e0");
		colorMap.put("grey lighten-1","#bdbdbd");
		colorMap.put("grey","#9e9e9e");
		colorMap.put("grey darken-1","#757575");
		colorMap.put("grey darken-2","#616161");
		colorMap.put("grey darken-3","#424242");
		colorMap.put("grey darken-4","#212121");
		colorMap.put("blue-grey lighten-5","#eceff1");
		colorMap.put("blue-grey lighten-4","#cfd8dc");
		colorMap.put("blue-grey lighten-3","#b0bec5");
		colorMap.put("blue-grey lighten-2","#90a4ae");
		colorMap.put("blue-grey lighten-1","#78909c");
		colorMap.put("blue-grey","#607d8b");
		colorMap.put("blue-grey darken-1","#546e7a");
		colorMap.put("blue-grey darken-2","#455a64");
		colorMap.put("blue-grey darken-3","#37474f");
		colorMap.put("blue-grey darken-4","#263238");
		colorMap.put("black","#000000");
		colorMap.put("white","#ffffff");
		colorMap.put("transparent","transparent");
	}
		
		
	private void OptimizePNG(String input, String output, boolean removeGamma, int compressionLevel) throws IOException {
		PngOptimizer optimizer = new PngOptimizer();
		PngImage image = new PngImage(input, "NONE");
		optimizer.optimize(image, output, removeGamma, compressionLevel);
		
	}
	
	
	public void ActivateGZip(String DummyDonatorKey, int minSizeKB) {
		@SuppressWarnings("unused")
		float float1 = -1.0f;
        try {
			if (anywheresoftware.b4a.objects.streams.File.Exists("", "abmversion")) {
			    InputStreamReader inputStreamReader = null;
				try {
					inputStreamReader = new InputStreamReader((InputStream)anywheresoftware.b4a.objects.streams.File.OpenInput("", "abmversion").getObject());
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
					try {
						final String line;
						if ((line = bufferedReader.readLine()) != null) {
							float1 = Float.parseFloat(line);
						}
					}
					finally {
						bufferedReader.close();
						inputStreamReader.close();
					}
					bufferedReader.close();
					inputStreamReader.close();
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		GZip=true;
		minGZipSize = minSizeKB;
	}
	
	public void ActivateUseCDN(String DummyDonatorKey, String CDNUri) {
		@SuppressWarnings("unused")
		float float1 = -1.0f;
        try {
			if (anywheresoftware.b4a.objects.streams.File.Exists("", "abmversion")) {
			    InputStreamReader inputStreamReader = null;
				try {
					inputStreamReader = new InputStreamReader((InputStream)anywheresoftware.b4a.objects.streams.File.OpenInput("", "abmversion").getObject());
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
					try {
						final String line;
						if ((line = bufferedReader.readLine()) != null) {
							float1 = Float.parseFloat(line);
						}
					}
					finally {
						bufferedReader.close();
						inputStreamReader.close();
					}
					bufferedReader.close();
					inputStreamReader.close();
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		CDN=true;
		if (!CDNUri.equals("")) {
			if (!CDNUri.endsWith("/")) {
				CDNUri=CDNUri+"/";
			}
			mCDNUrl=CDNUri;
		}		
		
	}
	
	/**
	 * set your cloudinary account info:
	 * 
	 * You can find this info when you created an (free of paying) account with Cloudinary here:
	 * 
	 * https://cloudinary.com/console
	 * 
	 * UploadGeneratedJSCSS: when true, ABM will automatically upload and use all generated css/js files per page to Cloudinary
	 */
	public void ActivateCloudinary(String DummyDonatorKey, String cloudName, String apiKey, String apiSecretKey, boolean UploadGeneratedJSCSS, String appName, boolean debug, anywheresoftware.b4a.objects.collections.List BlacklistUploadIPS) {
		CloudCDN=true;
		CloudAppName = appName;
		CloudDebug=debug;
		CloudCoreCSSUrl="";
		
		String currHostIP="";
		try {
			currHostIP = GetMyIP();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		BA.Log("Cloudinary running on: " + currHostIP);
		if (BlacklistUploadIPS.IsInitialized()) {
			for (int i=0;i<BlacklistUploadIPS.getSize();i++) {
				String s = (String) BlacklistUploadIPS.Get(i);
				if (s.equals(currHostIP)) {
					CloudUploadGeneratedJSCSS = false;
					CloudCDN=false;
					CloudAppName="";
					CloudDebug=false;
					CloudCoreCSSUrl="";
					BA.LogError("Cloudinary DISABLED BECAUSE RUNNING FROM " + currHostIP);
					break;
				}
			}
		}
		
		CloudinaryMap.Initialize();
		if (CloudCDN) {			
			CloudUploadGeneratedJSCSS = UploadGeneratedJSCSS;
			cloudinary = new Cloudinary(ObjectUtils.asMap(
					"cloud_name", cloudName,
					"api_key", apiKey,
					"api_secret", apiSecretKey));	
			
			if (CloudUploadGeneratedJSCSS) {
				CloudinaryLoadToDelete(appName + "/css/", true);
				CloudinaryLoadToDelete(appName + "/js/", false);
			}
			
		}
	}
	
	public String GetMyIP() throws SocketException {
		String lh = "127.0.0.1";
		Inet6Address ip6 = null;
		Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
		if (en != null) {
			while (en.hasMoreElements()) {
				NetworkInterface ni = en.nextElement();
				Enumeration<InetAddress> en2 = ni.getInetAddresses();
				while (en2.hasMoreElements()) {
					InetAddress ia = en2.nextElement();
					if (!ia.isLoopbackAddress()) {
						if (ia instanceof Inet6Address) {
							if (ip6 == null)
								ip6 = (Inet6Address) ia;
						}
						else
							return ia.getHostAddress();
					}
				}
			}
		}
		if (ip6 != null)
			return ip6.getHostAddress();
		return lh;

}
	
	/**
	 * Must be activated with ActivateCloudinary() first.
	 * 
	 * You can 'organize' resources by using a prefix in the publicId: e.g. "multiple/folders/sample" 
	 * 
	 * example: Log(ABM.CloudinaryUploadRawFile("css/abmcomposer.min.css", File.Combine(File.DirApp & "/www/css/", "abmcomposer.min.css")))
	 */
	public static String CloudinaryUploadRawFile(String publicId, String fileNamePath) {
		String ret="";
		if (CloudCDN) {
			File toUpload = new File(fileNamePath);
			try {
				@SuppressWarnings("rawtypes")
				Map uploadResult = cloudinary.uploader().upload(toUpload, ObjectUtils.asMap("public_id", publicId, "resource_type" , "raw"));
				ret = (String) uploadResult.get("secure_url");
				if (CloudDebug) {
					BA.Log(ret);					
				}
				CloudinaryMap.Put(publicId, ret);				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
	
	
	protected static String CloudinaryUploadRawFileInternal(String publicId, String fileNamePath) {
		String ret="";
		if (CloudCDN) {
			File toUpload = new File(fileNamePath);
			
			try {
				ret = (String) CloudinaryToDeleteMap.getOrDefault(publicId, "");
				if (!ret.equals("")) {
					if (CloudDebug) {
						BA.Log("No need to upload: " + publicId + " url: " + ret);					
					}
					CloudinaryToDeleteMap.remove(publicId);
					return ret;
				}
				
			} catch (Exception e1) {
				
			}
			
			try {
				@SuppressWarnings("rawtypes")
				Map uploadResult = cloudinary.uploader().upload(toUpload, ObjectUtils.asMap("public_id", publicId, "resource_type" , "raw"));
				ret = (String) uploadResult.get("secure_url");
				CloudinaryMap.Put(publicId, ret);
				if (CloudDebug) {
					BA.Log("Uploaded: " + publicId);					
				}
				
			} catch (IOException e) {				
				e.printStackTrace();
			}
		}
		return ret;
	}
	
	protected static void CloudinaryDeleteAllOldFiles() {
		for (Entry<String,String> entry: CloudinaryToDeleteMap.entrySet()) {
			CloudinaryDeleteRawFile(entry.getKey());
		}
	}
	
	/**
	 * Must be activated with ActivateCloudinary() first.
	 * 
	 * You can 'organize' resources by using a prefix in the publicId: e.g. "multiple/folders/sample" 
	 * 
	 * example: Log(ABM.CloudinaryUploadAudioVideo("video/video.mp4", File.Combine(File.DirApp & "/www/video", "video.mp4")))
	 */
	public static String CloudinaryUploadAudioVideo(String publicId, String fileNamePath) {
		String ret="";
		if (CloudCDN) {
			File toUpload = new File(fileNamePath);
			try {
				@SuppressWarnings("rawtypes")
				Map uploadResult = cloudinary.uploader().upload(toUpload, ObjectUtils.asMap("public_id", publicId, "resource_type" , "video"));
				ret = (String) uploadResult.get("secure_url");
				if (CloudDebug) {
					BA.Log(ret);					
				}
				CloudinaryMap.Put(publicId, ret);				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
	
	/**
	 * Must be activated with ActivateCloudinary() first.
	 * 
	 * You can 'organize' resources by using a prefix in the publicId: e.g. "multiple/folders/sample" 
	 * 
	 * example: Log(ABM.CloudinaryUploadImage("images/image.png", File.Combine(File.DirApp & "/www/images/", "image.png")))
	 */
	public static String CloudinaryUploadImage(String publicId, String fileNamePath) {
		String ret="";
		if (CloudCDN) {
			File toUpload = new File(fileNamePath);
			try {
				@SuppressWarnings("rawtypes")
				Map uploadResult = cloudinary.uploader().upload(toUpload, ObjectUtils.asMap("public_id", publicId));
				ret = (String) uploadResult.get("secure_url");
				if (CloudDebug) {
					BA.Log(ret);
				}
				CloudinaryMap.Put(publicId, ret);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
	
	/**
	 * loads all the raw file urls for the given prefix
	 */
	public static void CloudinaryLoadRawFiles(String prefix, boolean clear) {
		if (clear) {
			CloudinaryMap.Clear();
		}
		if (CloudCDN) {
			ApiResponse result = null;
			String nextCursor = null;
			do {
			    try {
			        result = cloudinary.search().expression("public_id:" +  prefix + "* AND resource_type:raw").maxResults(500).nextCursor(nextCursor).execute();
			        nextCursor = result.containsKey("next_cursor") ? result.get("next_cursor").toString() : null;

			        if(result.containsKey("resources")) {
			            @SuppressWarnings("unchecked")
						List<Map<String,Object>> resources = (ArrayList<Map<String,Object>>) result.get("resources");
			            for (Map<String,Object> resource : resources) {
			                if(resource.containsKey("public_id")) {
			                    String publicId = resource.get("public_id").toString();
			                    CloudinaryMap.Put(publicId, resource.get("secure_url").toString());
			                    if (CloudDebug) {
			    					BA.Log(publicId + ": " + resource.get("secure_url").toString());					
			    				}
			                }
			            }
			        }
			    }
			    catch (Exception e) {
			        nextCursor = null;
			        BA.Log(e.getMessage());
			    }
			} while (nextCursor != null);			
		}		
	}
	
	/**
	 * loads all the raw file urls for the given prefix
	 */
	protected static void CloudinaryLoadToDelete(String prefix, boolean clear) {
		if (clear) {
			CloudinaryToDeleteMap.clear();;
		}
		if (CloudCDN) {
			ApiResponse result = null;
			String nextCursor = null;
			do {
			    try {
			        result = cloudinary.search().expression("public_id:" +  prefix + "* AND resource_type:raw").maxResults(500).nextCursor(nextCursor).execute();
			        nextCursor = result.containsKey("next_cursor") ? result.get("next_cursor").toString() : null;

			        if(result.containsKey("resources")) {
			            @SuppressWarnings("unchecked")
						List<Map<String,Object>> resources = (ArrayList<Map<String,Object>>) result.get("resources");
			            for (Map<String,Object> resource : resources) {
			                if(resource.containsKey("public_id")) {
			                    String publicId = resource.get("public_id").toString();
			                    CloudinaryToDeleteMap.put(publicId, resource.get("secure_url").toString());			        
			                }
			            }
			        }
			    }
			    catch (Exception e) {
			        nextCursor = null;
			        BA.Log(e.getMessage());
			    }
			} while (nextCursor != null);			
		}		
	}
	
	/**
	 * loads all the video urls for the given prefix
	 */
	public static void CloudinaryLoadAudioVideos(String prefix, boolean clear) {
		if (clear) {
			CloudinaryMap.Clear();
		}
		if (CloudCDN) {
			ApiResponse result = null;
			String nextCursor = null;
			do {
			    try {
			    	result = cloudinary.search().expression("public_id:" +  prefix + "* AND resource_type:video").maxResults(500).nextCursor(nextCursor).execute();
			        nextCursor = result.containsKey("next_cursor") ? result.get("next_cursor").toString() : null;

			        if(result.containsKey("resources")) {
			            @SuppressWarnings("unchecked")
						List<Map<String,Object>> resources = (ArrayList<Map<String,Object>>) result.get("resources");
			            for (Map<String,Object> resource : resources) {
			                if(resource.containsKey("public_id")) {
			                    String publicId = resource.get("public_id").toString();
			                    CloudinaryMap.Put(publicId, resource.get("secure_url").toString());
			                    if (CloudDebug) {
			    					BA.Log(publicId + ": " + resource.get("secure_url").toString());					
			    				}
			                }
			            }
			        }
			    }
			    catch (Exception e) {
			        nextCursor = null;
			    }
			} while (nextCursor != null);			
		}		
	}
	
	/**
	 * loads all the image urls for the given prefix
	 */
	public static void CloudinaryLoadImages(String prefix, boolean clear) {
		if (clear) {
			CloudinaryMap.Clear();
		}
		if (CloudCDN) {
			ApiResponse result = null;
			String nextCursor = null;
			do {
			    try {
			    	result = cloudinary.search().expression("public_id:" +  prefix + "*").maxResults(500).nextCursor(nextCursor).execute();
			        nextCursor = result.containsKey("next_cursor") ? result.get("next_cursor").toString() : null;
			        			        
			        if(result.containsKey("resources")) {
			            @SuppressWarnings("unchecked")
						List<Map<String,Object>> resources = (ArrayList<Map<String,Object>>) result.get("resources");
			            for (Map<String,Object> resource : resources) {
			                if(resource.containsKey("public_id")) {
			                    String publicId = resource.get("public_id").toString();
			                    CloudinaryMap.Put(publicId, resource.get("secure_url").toString());
			                    if (CloudDebug) {
			    					BA.Log(publicId + ": " + resource.get("secure_url").toString());					
			    				}
			                }
			            }
			        }
			    }
			    catch (Exception e) {
			        nextCursor = null;
			    }
			} while (nextCursor != null);			
		}		
	}
	
	/**
	 * get the url for a certain publicid (can also be one loaded with CloudinaryLoadImages() or CloudinaryLoadRawFiles())  
	 */
	public static String CloudinaryGet(String publicId) {
		return (String) CloudinaryMap.GetDefault(publicId, "");
	}
	
	/**
	 * Must be activated with ActivateCloudinary() first.
	 * 
	 */
	public static String CloudinaryDeleteRawFile(String publicId) {
		String ret = "";
		try {
			@SuppressWarnings("rawtypes")
			Map destroyResult = cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type" , "raw"));
			ret = (String) destroyResult.get("result");
			if (CloudDebug) {
				BA.Log("Deleting: " + publicId + ": " + ret);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Must be activated with ActivateCloudinary() first.
	 * 
	 */
	public static String CloudinaryDeleteAudioVideo(String publicId) {
		String ret = "";
		try {
			@SuppressWarnings("rawtypes")
			Map destroyResult = cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type" , "video"));
			ret = (String) destroyResult.get("result");
			if (CloudDebug) {
				BA.Log("Deleting: " + publicId + ": " + ret);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Must be activated with ActivateCloudinary() first.
	 * 
	 */
	public static String CloudinaryDeleteImage(String publicId) {
		String ret = "";
		try {
			@SuppressWarnings("rawtypes")
			Map destroyResult = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());	
			ret = (String) destroyResult.get("result");
			if (CloudDebug) {
				BA.Log("Deleting: " + publicId + ": " + ret);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Must be activated with ActivateCloudinary() first.
	 * 
	 * if you used a prefix in your publicId, you can delete in bulk: e.g. "multiple/" ("/" is important)
	 * 
	 */
	public static String CloudinaryDeleteAllRawFilesWithPrefix(String prefix) {
		String ret = "";
		try {
			@SuppressWarnings("rawtypes")
			Map destroyResult = cloudinary.api().deleteResourcesByPrefix(prefix, ObjectUtils.asMap("resource_type" , "raw"));
			ret = destroyResult.toString();
			if (CloudDebug) {
				BA.Log(ret);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Must be activated with ActivateCloudinary() first.
	 * 
	 * if you used a prefix in your publicId, you can delete in bulk: e.g. "multiple/" ("/" is important)
	 * 
	 */
	public static String CloudinaryDeleteAllAudioVideosWithPrefix(String prefix) {
		String ret = "";
		try {
			@SuppressWarnings("rawtypes")
			Map destroyResult = cloudinary.api().deleteResourcesByPrefix(prefix, ObjectUtils.asMap("resource_type" , "video"));			
			ret = destroyResult.toString();
			if (CloudDebug) {
				BA.Log(ret);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Must be activated with ActivateCloudinary() first.
	 * 
	 * if you used a prefix in your publicId, you can delete in bulk: e.g. "multiple/" ("/" is important)
	 * 
	 */
	public static String CloudinaryDeleteAllImagesWithPrefix(String prefix) {
		String ret = "";
		try {
			@SuppressWarnings("rawtypes")
			Map destroyResult = cloudinary.api().deleteResourcesByPrefix(prefix, ObjectUtils.emptyMap());
			ret = (String) destroyResult.get("result");		
			if (CloudDebug) {
				BA.Log(ret);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public void ActivateObfuscateCreatedWith(String DummyDonatorKey) {
		CW=true;
	}
	
	public String getCDNUrl() {
		return mCDNUrl;
	}
	
	public void NativeGenerateApps(boolean overwrite) {
		DoNativeAppGeneration=true;
		DoNativeAppGenerationOverwrite=overwrite;
	}
		
	public void ActivatePNGOptimize(String DummyDonatorKey, anywheresoftware.b4a.objects.collections.List folders, boolean removeGamma, int compressionLevel, boolean keepOriginals, boolean extendedLog  ) {
		
        PNGExtendedLog=extendedLog;
		PNGGamma = removeGamma;
		PNGLevel=compressionLevel;
		if (PNGLevel>9) {
			return;
		}
		PNGFolders = new ArrayList<String>();
		for (int i=0;i<folders.getSize();i++) {
			String s = (String) folders.Get(i);
			if (!s.endsWith("/")) {
				s+="/";
			}
			PNGFolders.add(s);
		}
		PNGOpt=true;
		PNGKeepOriginals=keepOriginals;
	}
	
	protected void GzipIt(String source) {
		 byte[] buffer = new byte[1024];
		 
		 try{
		 
		    	GZIPOutputStream gzos = 
		    		new GZIPOutputStream(new FileOutputStream(source + ".gz"));
		 
		        FileInputStream in = 
		            new FileInputStream(source);
		 
		        int len;
		        while ((len = in.read(buffer)) > 0) {
		        	gzos.write(buffer, 0, len);
		        }
		 
		        in.close();
		    	
		    	gzos.finish();
		    	gzos.close();
		    	
		    }catch(IOException ex){
		       ex.printStackTrace();   
		    }		 
	}
	
	private void purgeDirectoryButKeepSubDirectories(File dir) {
	    for (File file: dir.listFiles()) {
	        if (!file.isDirectory()) file.delete();
	    }
	}
			
	public void WriteAppLauchPageToDisk(ABMPage page, String appDir, String fileName, boolean NeedsAutorization) {
		if (GZip) {
			BA.Log("GZipping HTML, JavaScript and CSS files...");
			File staticFilesFolder = new File(appDir);
	    	
			if (page.mStaticFilesFolder.equals("")) {
				while (!staticFilesFolder.getPath().endsWith("www")) {
					staticFilesFolder = new File(staticFilesFolder.getParent());
				}				
			} else {
				staticFilesFolder = new File(page.mStaticFilesFolder);
				appDir = staticFilesFolder.getAbsolutePath() + File.separator + page.mAppName; 
			}
			
	    	File testDirectory = new File(staticFilesFolder.getAbsolutePath() + "/css/");
	    	File[] files = testDirectory.listFiles(new FileFilter() {
	    	    @Override
	    	    public boolean accept(File pathname) {
	    	        String name = pathname.getName().toLowerCase();
	    	        return name.endsWith(".css") && pathname.isFile();
	    	    }
	    	});
	    	if (files!=null) {
	    		for (File f: files) {
	    			File buildFile = new File(staticFilesFolder.getAbsolutePath() + "/css/", f.getName() + ".gz");
	    			buildFile.delete();
	    			if (f.length()>minGZipSize) {
	    				buildFile = new File(staticFilesFolder.getAbsolutePath() + "/css/", f.getName());
	    				GzipIt(buildFile.getAbsolutePath());
	    			}
	    		}
	    	}
	    	
	    	testDirectory = new File(staticFilesFolder.getAbsolutePath() + "/js/");
	    	files = testDirectory.listFiles(new FileFilter() {
	    	    @Override
	    	    public boolean accept(File pathname) {
	    	        String name = pathname.getName().toLowerCase();
	    	        return name.endsWith(".js") && pathname.isFile();
	    	    }
	    	});
	    	if (files!=null) {
	    		for (File f: files) {
	    			File buildFile = new File(staticFilesFolder.getAbsolutePath() + "/js/", f.getName() + ".gz");
	    			buildFile.delete();
	    			if (f.length()>minGZipSize) {
	    				buildFile = new File(staticFilesFolder.getAbsolutePath() + "/js/", f.getName());
	    				GzipIt(buildFile.getAbsolutePath());
	    			}
	    		}
	    	}
	    	
	    	testDirectory = new File(staticFilesFolder.getAbsolutePath() + "/js/web/");
	    	files = testDirectory.listFiles(new FileFilter() {
	    	    @Override
	    	    public boolean accept(File pathname) {
	    	        String name = pathname.getName().toLowerCase();
	    	        return (name.endsWith(".js") || name.endsWith(".css")) && pathname.isFile();
	    	    }
	    	});
	    	if (files!=null) {
	    		for (File f: files) {
	    			File buildFile = new File(staticFilesFolder.getAbsolutePath() + "/js/web/", f.getName() + ".gz");
	    			buildFile.delete();
	    			if (f.length()>minGZipSize) {
	    				buildFile = new File(staticFilesFolder.getAbsolutePath() + "/js/web/", f.getName());
	    				GzipIt(buildFile.getAbsolutePath());
	    			}
	    		}
	    	}
	    	
	    	testDirectory = new File(staticFilesFolder.getAbsolutePath() + "/js/build/");
	    	files = testDirectory.listFiles(new FileFilter() {
	    	    @Override
	    	    public boolean accept(File pathname) {
	    	        String name = pathname.getName().toLowerCase();
	    	        return name.endsWith(".js") && pathname.isFile();
	    	    }
	    	});
	    	if (files!=null) {
	    		for (File f: files) {
	    			File buildFile = new File(staticFilesFolder.getAbsolutePath() + "/js/build/", f.getName() + ".gz");
	    			buildFile.delete();
	    			if (f.length()>minGZipSize) {
	    				buildFile = new File(staticFilesFolder.getAbsolutePath() + "/js/build/", f.getName());
	    				GzipIt(buildFile.getAbsolutePath());
	    			}
	    		}
	    	}
		}
		if (PNGOpt) {
			
			if (PNGLevel<10) {
				BA.Log("Optimizing new and updated PNG files...");
				for (int i=1;i<PNGFolders.size();i++) {
					File testDirectory = new File(PNGFolders.get(i));
					File[] files = testDirectory.listFiles(new FileFilter() {
			    	    @Override
			    	    public boolean accept(File pathname) {
			    	        String name = pathname.getName().toLowerCase();
			    	        return name.endsWith(".png") && !name.endsWith("_abmorig.png") && pathname.isFile();
			    	    }
			    	});
			        String line = null;
			        Map<String,String> origs = new HashMap<String,String>();
			        try {
			            FileReader fileReader = new FileReader(PNGFolders.get(i) + "abmoptimizer.donotdelete");
			            BufferedReader bufferedReader = new BufferedReader(fileReader);
			            while((line = bufferedReader.readLine()) != null) {
			            	String p[] = Regex.Split(";", line);
			            	origs.put(p[0].toLowerCase(), p[1]);			                
			            }   
			            bufferedReader.close();         
			        }
			        catch(FileNotFoundException ex) {
			                       
			        }
			        catch(IOException ex) {
			           
			        }
					if (files!=null) {
						for (File f: files) {
							String origName = f.getName();
							String s = origs.getOrDefault(f.getName().toLowerCase(), "");
							String s2 = f.lastModified() + ":" + f.length();
							if (!s.equals(s2)) {
								double before=f.length();
								File f2 = new File(PNGFolders.get(i), f.getName().substring(0, f.getName().length()-4) + "_abmorig.png");
								if (f2.exists()) {
									f2.delete();
								}
								f.renameTo(f2);
			    			
								try {
									if (PNGExtendedLog) {
										BA.Log("Optimizing " + origName);
									}
									OptimizePNG(PNGFolders.get(i) + f2.getName(), PNGFolders.get(i) + origName, PNGGamma, PNGLevel);
									if (!PNGKeepOriginals) {
										f2.delete();
									}
								} catch (IOException e) {
								}			    		
								double after=f.length();
								
								BA.Log(origName + ": optimized (" + (Math.round((1.0-(after/before))*10000.0)/100.0)  + "%)");
								s2 = f.lastModified() + ":" + f.length();
								origs.put(f.getName().toLowerCase(), s2);
							}
						}			        
			        
			    		BufferedWriter writer = null;
			    		try {
			    			File buildFile = new File(PNGFolders.get(i), "abmoptimizer.donotdelete");
		            	
			    			writer = new BufferedWriter(new FileWriter(buildFile));
			    			for (Entry<String,String> s: origs.entrySet()) {
			    				writer.write(s.getKey() + ";" + s.getValue() + "\n");
			    			}
			    		} catch (Exception e) {
			    			e.printStackTrace();
			    		} finally {
			    			try {
			    				writer.close();
			    			} catch (Exception e) {
			    			}
			    		}
			    		writer = null;
					}
				}
			}
		}
				
		String SiteMap="";
		if (!AppPublishedStartURL.equals("")) {
			if (!AppPublishedStartURL.endsWith("/")) {
				AppPublishedStartURL+="/";
			}
			StringBuilder s = new StringBuilder();
			
			s.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			s.append("<urlset\n");
			s.append("      xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\"\n");
			s.append("      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
			s.append("      xsi:schemaLocation=\"http://www.sitemaps.org/schemas/sitemap/0.9\n");
			s.append("            http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd\">\n");
			s.append("<!-- created with ABMaterial and B4J -->\n");
			
			s.append("<url>\n");
			s.append("<loc>" + AppPublishedStartURL + page.PageHTMLName + "</loc>\n");
			s.append("<lastmod>" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+'00:00").format(new Date()) + "</lastmod>\n");
			if (!page.PageSiteMapFrequency.equals(SITEMAP_FREQ_NONE)) {
				s.append("<changefreq>" + page.PageSiteMapFrequency + "</changefreq>\n");			
			}
			if (!page.PageSiteMapPriority.equals("")) {
				s.append("<priority>" + page.PageSiteMapPriority + "</priority>\n");
			}
			s.append("</url>\n");
			Collections.sort(SiteMaps);
			for (Entity entity : SiteMaps) {
				s.append(entity.name);
			}			
			s.append("</urlset>\n");
			SiteMap = s.toString();
		}
		try {
			
			Files.createDirectories(Paths.get(appDir));
			purgeDirectoryButKeepSubDirectories(new File(appDir));
			Files.createDirectories(Paths.get(appDir + File.separator + "images"));
	    	Files.createDirectories(Paths.get(appDir + File.separator + "uploads"));
	    	
	    	File b4jsFolder = new File(appDir);
			String b4jsAppDir = appDir;
	    	
			if (page.mStaticFilesFolder.equals("")) {
				while (!b4jsFolder.getPath().endsWith("www")) {
					b4jsFolder = new File(b4jsFolder.getParent());
				}				
			} else {
				b4jsFolder = new File(page.mStaticFilesFolder);
				b4jsAppDir = b4jsFolder.getAbsolutePath() + File.separator + page.mAppName; 
			}
			String b4jsDir = anywheresoftware.b4a.objects.streams.File.getDirApp();
			File b4jsFile = new File(b4jsDir, "copymewithjar.js.needs");
			File b4jsFileNew = new File(b4jsAppDir, "b4js." + getAppVersion() + ".js");
			
			if (HasSourceFolder) {
				BA.Log("Building " + b4jsFile.getAbsolutePath());
				if (b4jsFile.exists()) {
					b4jsFile.delete();
				}
				boolean B4JSFirst=true;
				boolean B4JSLast=false;
				int B4JSCounter = 0;
				for(Entry<String,B4JS>entry: B4JSClasses.entrySet()) {
					B4JSCounter++;
					if (B4JSCounter==B4JSClasses.values().size()) {
						B4JSLast=true;
					}
            		entry.getValue().Build(B4JSFirst, B4JSLast, ABMaterial.B4JSClassGenerated, BA.debugMode);
            		B4JSFirst=false;
            		SavedB4JSClasses.put(entry.getKey(), entry.getKey());
            	}
				
				if (!B4JSClassGenerated.isEmpty()) {
					BufferedWriter b4jsWriter = null;
		    		try {
		    			b4jsWriter = new BufferedWriter(new FileWriter(b4jsFile));
		    			String b4jsCore = B4JSClassGenerated.getOrDefault("b4js", "");
						if (!b4jsCore.equals("")) {
							b4jsWriter.write(b4jsCore);
						}						
						for(Entry<String,String>entry: B4JSClassGenerated.entrySet()) {
							if (!entry.getKey().equals("b4js")) {
								if (!entry.getValue().equals("")) {
									b4jsWriter.write(entry.getValue());
								}
							}
						}
		    		} catch (Exception e) {
		    			e.printStackTrace();
		    		} finally {
		    			try {
		    				b4jsWriter.close();
		    			} catch (Exception e) {
		    			}
		    		}
		    		b4jsWriter = null;		    		
				} 
			}
			
			if (b4jsFile.exists() && b4jsFileNew.exists()==false) {
				BA.Log("Cloning to " + b4jsFileNew.getAbsolutePath());
				DeleteFilesForPathByPrefix(b4jsAppDir, "b4js.");				
				try {
					CopyFileUsingStream(b4jsFile, b4jsFileNew);
				} catch (IOException e) {
				    BA.LogError("Problem cloning the copymewithjar.js.needs file!");
				}
				if (ABMaterial.GZip) {
					File buildB4jsFile = new File(b4jsAppDir, "b4js." + getAppVersion() + ".js.gz");
					buildB4jsFile.delete();
					
					buildB4jsFile = new File(b4jsAppDir, "b4js." + getAppVersion() + ".js");
					if (buildB4jsFile.length()>minGZipSize) {
						GzipIt(buildB4jsFile.getAbsolutePath());
					}
				}
			}
	    	
	    	IsApp=true;
	    	WritePageToDisk(page, appDir, fileName,NeedsAutorization);
	    	IsApp=false;
	    	if (!SiteMap.equals("")) {	 		
	        	
	        	BufferedWriter writer = null;
	            try {
	            	Files.createDirectories(Paths.get(appDir));        	
	            	File buildFile = new File(appDir, "sitemap.xml");
	            	
	                writer = new BufferedWriter(new FileWriter(buildFile));
	                writer.write(SiteMap);
	            } catch (Exception e) {
	                e.printStackTrace();
	            } finally {
	                try {
	                    writer.close();
	                } catch (Exception e) {
	                }
	            }
	            writer = null;
	            try {
	            	File folder = null;
		    		if (page.mStaticFilesFolder.equals("")) {
		    			folder = new File(appDir);
		    			while (!folder.getPath().endsWith("www")) {
		    				folder = new File(folder.getParent());	            		
		    			}
		    			folder = new File(folder.getParent());
		    		} else {
		    			folder = new File(page.mStaticFilesFolder);		    			
		    		}
	            	
	            	String dir2 = folder.getAbsolutePath(); 
	            	File buildFile = new File(dir2, "robots.txt");
	            	
	                writer = new BufferedWriter(new FileWriter(buildFile));
	                writer.write("User-agent: *\n");
	                writer.write("Disallow:\n\n");
	                writer.write("Sitemap: " + AppPublishedStartURL + "sitemap.xml\n");
	            } catch (Exception e) {
	                e.printStackTrace();
	            } finally {
	                try {
	                    writer.close();
	                } catch (Exception e) {
	                }
	            }
	            
	            
	    	}
	    	if (!WriteAppRedirect.equals("")) {
	    		BufferedWriter writer = null;
	    		File folder = null;
	    		if (page.mStaticFilesFolder.equals("")) {
	    			folder = new File(appDir);
	    			while (!folder.getPath().endsWith("www")) {
	    				folder = new File(folder.getParent());	            		
	    			}
	    			folder = new File(folder.getParent());
	    		} else {
	    			folder = new File(page.mStaticFilesFolder);
	    			folder = new File(folder.getParent());
	    		}
	            try {
	            	String relPathApp=this.WriteAppRedirect;
	            	if (!relPathApp.endsWith("/")) {
	            		relPathApp+="/";
	            	}
	            	
	            	File buildFile = new File(folder.getAbsolutePath(), "index.html");
	            	
	                writer = new BufferedWriter(new FileWriter(buildFile));
	                writer.write("<!DOCTYPE html  PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\" \"http://www.w3.org/TR/html4/frameset.dtd\">\n");
	                if (!page.PageLanguage.equals("")) {
	                	writer.write("<html lang=\"" + page.PageLanguage + "\">\n<head>\n");
	                } else {
	                	writer.write("<html>\n<head>\n");
	                }
	                if (CW) {
                    	String z = encrypt("This WebApp/WebSite was generated using ABMaterial v" + ABMaterial.Version + ", a B4X library written by Alain Bailleul 2015-2020. See http://alwaysbusycorner.com/abmaterial");
                    	writer.write("<!-- " + z + " -->\n");                    	
                    } else {
                    	writer.write("<!-- This WebApp/WebSite was generated using ABMaterial v" + ABMaterial.Version + ", a B4X library written by Alain Bailleul 2015-2020. See http://alwaysbusycorner.com/abmaterial -->\n");
                    }
                	writer.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + page.PageCharset + "\">\n");            	
	                if (!page.PageKeywords.equals("")) {
	                	 writer.write("<meta name=\"keywords\" content=\"" + page.PageKeywords + "\">\n");
	                }
	                
	                
	                writer.write("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.01\">\n");
	                
	                Date expdate= new Date();
	                if (page.PageExpires==0) {            	
	                	LocalDate futureDate = LocalDate.now().plusYears(1);
	                	expdate = Date.from(futureDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	                }else {
	                	expdate.setTime(page.PageExpires);
	                }
	                DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
	                df.setTimeZone(TimeZone.getTimeZone("GMT"));
	                writer.write("<meta http-equiv=\"expires\" content=\"" + df.format(expdate)+ "\">\n");
	                writer.write("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n");
	                writer.write("<meta name=\"msapplication-tap-highlight\" content=\"no\">\n");
	                writer.write("<meta name=\"description\" content=\"" + ABMaterial.HTMLConv().htmlEscape(page.PageDescription, page.PageCharset) + "\">\n");
	                writer.write("<meta name=\"apple-mobile-web-app-capable\" content=\"yes\">\n");
	                writer.write("<meta name=\"apple-mobile-web-app-title\" content=\"" + ABMaterial.HTMLConv().htmlEscape(page.PageTitle, page.PageCharset) + "\" />\n");
	                writer.write("<title>" + ABMaterial.HTMLConv().htmlEscape(page.PageTitle, page.PageCharset) + "</title>\n");
	                
	                for (int index=0;index<AppleTouchIcons.size();index++) {
	                	writer.write("<link rel=\"apple-touch-icon\" href=\"" + relPathApp + "images/" + AppleTouchIcons.get(index) + "\" sizes=\"" + AppleTouchIconSizes.get(index) + "\">\n");
	                }
	                
	                for (int index=0;index<FavorityIcons.size();index++) {
	                	if (FavorityIcons.get(index).startsWith("http")) {
	                		writer.write("<link rel=\"icon\" href=\"" + FavorityIcons.get(index) + "\" sizes=\"" + FavorityIconSizes.get(index) + "\">\n");
	                		writer.write("<link rel=\"shortcut icon\" href=\"" + FavorityIcons.get(index) + "\" sizes=\"" + FavorityIconSizes.get(index) + "\">\n");
	                	} else {
	                		writer.write("<link rel=\"icon\" href=\"" + relPathApp + "images/" + FavorityIcons.get(index) + "\" sizes=\"" + FavorityIconSizes.get(index) + "\">\n");
	                		writer.write("<link rel=\"shortcut icon\" href=\"" + relPathApp + "images/" + FavorityIcons.get(index) + "\" sizes=\"" + FavorityIconSizes.get(index) + "\">\n");
	                	}
	                }
	                
	                if (!ABMaterial.Manifest.equals("")) {
	                	writer.write("<link rel=\"manifest\" href=\"" + relPathApp + "images/" + ABMaterial.Manifest + "\">\n");
	                }
	                
	                if (!ABMaterial.MaskIcon.equals("")) {
	                	String IconColor=ABMaterial.GetColorStrMap(ABMaterial.MaskIconColor, ABMaterial.MaskIconColorIntensity);
	                	writer.write("<link rel=\"mask-icon\" href=\"" + relPathApp + "images/" + ABMaterial.MaskIcon + "\" color=\"" + IconColor + "\">\n");
	                }
	                
	                String TileColor = ABMaterial.GetColorStrMap(ABMaterial.MSTileColor, ABMaterial.MSTileColorIntensity);
	                writer.write("<meta name=\"msapplication-TileColor\" content=\"#" + TileColor + "\">\n");
	                for (int index=0;index<MSTileIcons.size();index++) {
	                	writer.write("<meta name=\"msapplication-TileImage\" content=\"" + relPathApp + "images/" + MSTileIcons.get(index) + "\" sizes=\"" + MSTileIconSizes.get(index)+ "\">\n");
	                }
	                
	               
	                writer.write("<meta name=\"theme-color\" content=\"" + ABMaterial.GetColorStrMap(ABMaterial.AndroidChromeThemeColor,ABMaterial.AndroidChromeThemeColorIntensity) + "\">\n");
	                
	                if (!ABMaterial.BrowserConfig.equals("")) {
	                	writer.write("<meta name=\"msapplication-config\" content=\"" + ABMaterial.BrowserConfig + "\" />\n");
	                }
	                
	                for (Entry<String,String> entry: page.svgIconmapContent.entrySet()) {
	                	writer.write(entry.getValue());
	                }
	                
	                writer.write("</head>\n");
	                writer.write("<script language=\"JavaScript\">\n");
	                writer.write("window.location.replace(\"" + this.WriteAppRedirect + "\");\n");	               
	                writer.write("</script>\n");	               
	                writer.write("</html>\n");
	            } catch (Exception e) {
	                e.printStackTrace();
	            } finally {
	                try {
	                    writer.close();
	                } catch (Exception e) {
	                }
	            }
	            writer = null;
	            if (GZip) {
	            	File buildFile = new File(folder.getAbsolutePath(), "index.html" + ".gz");
	    			buildFile.delete();
	    			//if (f.length()>minGZipSize) {
	    				buildFile = new File(folder.getAbsolutePath(), "index.html");
	    				GzipIt(buildFile.getAbsolutePath());
	    			//}
	            	
	            }
	            
	    	}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		if (DoNativeAppGeneration) {
			Native.Generate(page, DoNativeAppGenerationOverwrite);		
			
		}
		res = null;
	}
	
	protected static String GetIconCSS(String iconName) {
		String s = iconmap.getOrDefault(iconName, "");
		if (!s.equals("")) {
			s = "." + iconName + s;
		}
		return s;
	}
	
	protected static String BuildIconsCSS() {
		NeededIcons.put("mdi-navigation-menu", true);
		NeededIcons.put("mdi-navigation-arrow-drop-down", true);
		NeededIcons.put("mdi-action-assignment-turned-in", true);
		NeededIcons.put("mdi-action-search", true);
		NeededIcons.put("mdi-content-add", true);
		NeededIcons.put("mdi-action-visibility", true);
		NeededIcons.put("mdi-action-delete", true);
		NeededIcons.put("mdi-action-settings-overscan", true);
		NeededIcons.put("mdi-av-skip-previous", true);
		NeededIcons.put("mdi-av-skip-next", true);
		NeededIcons.put("mdi-hardware-keyboard-arrow-left", true);
		NeededIcons.put("mdi-hardware-keyboard-arrow-right", true);
		NeededIcons.put("mdi-navigation-cancel", true);
		NeededIcons.put("close", true);	
		NeededIcons.put("fa-bars", true);
		NeededIcons.put("fa-print", true);
		NeededIcons.put("fa-plus-circle", true);
		NeededIcons.put("fa-minus-circle", true);
		
		NeededIcons.put("fa-play", true);
		NeededIcons.put("fa-pause", true);
		NeededIcons.put("fa-scissors", true);
		NeededIcons.put("fa-files-o", true);
		NeededIcons.put("fa-clipboard", true);
		NeededIcons.put("fa-trash-o", true);
	
		NeededIcons.put("fa-whatsapp", true);
		NeededIcons.put("fa-at", true);
		NeededIcons.put("fa-twitter", true);
		NeededIcons.put("fa-facebook", true);
		NeededIcons.put("fa-google", true);
		NeededIcons.put("fa-linkedin", true);
		NeededIcons.put("fa-pinterest", true);			
	
		NeededIcons.put("view_module", true);
		NeededIcons.put("view_list", true);
		NeededIcons.put("cloud_upload", true);
		NeededIcons.put("file_download", true);
		NeededIcons.put("delete_forever", true);
		NeededIcons.put("content_paste", true);
		NeededIcons.put("content_copy", true);
		NeededIcons.put("content_cut", true);
		NeededIcons.put("create", true);
		
		NeededIcons.put("fa-file-image-o", true);			
		NeededIcons.put("fa-file-pdf-o", true);
		NeededIcons.put("fa-file-word-o", true);	
		NeededIcons.put("fa-file-powerpoint-o", true);			
		NeededIcons.put("fa-file-excel-o", true);
		NeededIcons.put("fa-file-audio-o", true);	
		NeededIcons.put("fa-file-video-o", true);				
		NeededIcons.put("fa-file-zip-o", true);	
		NeededIcons.put("fa-file-code-o", true);			
		NeededIcons.put("fa-file-text-o", true);
		NeededIcons.put("fa-file-o", true);
		NeededIcons.put("folder", true);
		NeededIcons.put("folder_open", true);
		NeededIcons.put("keyboard_arrow_down", true);
		NeededIcons.put("keyboard_arrow_right", true);
		NeededIcons.put("fa-check-circle", true);			
			
		LoadIcons();
		
		BA.Log("Number of icons needed: " + NeededIcons.size());
		
		StringBuilder s = new StringBuilder();
		for (Entry<String, Boolean> entry: NeededIcons.entrySet()) {
			s.append(GetIconCSS(entry.getKey()));
		}
		return s.toString();
	}
	
	// font-awesome v4.7.0
	// https://fontawesome.com/v4.7.0/icons
	protected static void LoadIcons() {
		if (iconmap.size()==0) {
			iconmap.put("mdi-action-3d-rotation",":before{content:\"\\e600\"}");
			iconmap.put("mdi-action-accessibility",":before{content:\"\\e601\"}");
			iconmap.put("mdi-action-account-balance-wallet",":before{content:\"\\e602\"}");
			iconmap.put("mdi-action-account-balance",":before{content:\"\\e603\"}");
			iconmap.put("mdi-action-account-box",":before{content:\"\\e604\"}");
			iconmap.put("mdi-action-account-child",":before{content:\"\\e605\"}");
			iconmap.put("mdi-action-account-circle",":before{content:\"\\e606\"}");
			iconmap.put("mdi-action-add-shopping-cart",":before{content:\"\\e607\"}");
			iconmap.put("mdi-action-alarm-add",":before{content:\"\\e608\"}");
			iconmap.put("mdi-action-alarm-off",":before{content:\"\\e609\"}");
			iconmap.put("mdi-action-alarm-on",":before{content:\"\\e60a\"}");
			iconmap.put("mdi-action-alarm",":before{content:\"\\e60b\"}");
			iconmap.put("mdi-action-android",":before{content:\"\\e60c\"}");
			iconmap.put("mdi-action-announcement",":before{content:\"\\e60d\"}");
			iconmap.put("mdi-action-aspect-ratio",":before{content:\"\\e60e\"}");
			iconmap.put("mdi-action-assessment",":before{content:\"\\e60f\"}");
			iconmap.put("mdi-action-assignment-ind",":before{content:\"\\e610\"}");
			iconmap.put("mdi-action-assignment-late",":before{content:\"\\e611\"}");
			iconmap.put("mdi-action-assignment-return",":before{content:\"\\e612\"}");
			iconmap.put("mdi-action-assignment-returned",":before{content:\"\\e613\"}");
			iconmap.put("mdi-action-assignment-turned-in",":before{content:\"\\e614\"}");
			iconmap.put("mdi-action-assignment",":before{content:\"\\e615\"}");
			iconmap.put("mdi-action-autorenew",":before{content:\"\\e616\"}");
			iconmap.put("mdi-action-backup",":before{content:\"\\e617\"}");
			iconmap.put("mdi-action-book",":before{content:\"\\e618\"}");
			iconmap.put("mdi-action-bookmark-outline",":before{content:\"\\e619\"}");
			iconmap.put("mdi-action-bookmark",":before{content:\"\\e61a\"}");
			iconmap.put("mdi-action-bug-report",":before{content:\"\\e61b\"}");
			iconmap.put("mdi-action-cached",":before{content:\"\\e61c\"}");
			iconmap.put("mdi-action-check-circle",":before{content:\"\\e61d\"}");
			iconmap.put("mdi-action-class",":before{content:\"\\e61e\"}");
			iconmap.put("mdi-action-credit-card",":before{content:\"\\e61f\"}");
			iconmap.put("mdi-action-dashboard",":before{content:\"\\e620\"}");
			iconmap.put("mdi-action-delete",":before{content:\"\\e621\"}");
			iconmap.put("mdi-action-description",":before{content:\"\\e622\"}");
			iconmap.put("mdi-action-dns",":before{content:\"\\e623\"}");
			iconmap.put("mdi-action-done-all",":before{content:\"\\e624\"}");
			iconmap.put("mdi-action-done",":before{content:\"\\e625\"}");
			iconmap.put("mdi-action-event",":before{content:\"\\e626\"}");
			iconmap.put("mdi-action-exit-to-app",":before{content:\"\\e627\"}");
			iconmap.put("mdi-action-explore",":before{content:\"\\e628\"}");
			iconmap.put("mdi-action-extension",":before{content:\"\\e629\"}");
			iconmap.put("mdi-action-face-unlock",":before{content:\"\\e62a\"}");
			iconmap.put("mdi-action-favorite-outline",":before{content:\"\\e62b\"}");
			iconmap.put("mdi-action-favorite",":before{content:\"\\e62c\"}");
			iconmap.put("mdi-action-find-in-page",":before{content:\"\\e62d\"}");
			iconmap.put("mdi-action-find-replace",":before{content:\"\\e62e\"}");
			iconmap.put("mdi-action-flip-to-back",":before{content:\"\\e62f\"}");
			iconmap.put("mdi-action-flip-to-front",":before{content:\"\\e630\"}");
			iconmap.put("mdi-action-get-app",":before{content:\"\\e631\"}");
			iconmap.put("mdi-action-grade",":before{content:\"\\e632\"}");
			iconmap.put("mdi-action-group-work",":before{content:\"\\e633\"}");
			iconmap.put("mdi-action-help",":before{content:\"\\e634\"}");
			iconmap.put("mdi-action-highlight-remove",":before{content:\"\\e635\"}");
			iconmap.put("mdi-action-history",":before{content:\"\\e636\"}");
			iconmap.put("mdi-action-home",":before{content:\"\\e637\"}");
			iconmap.put("mdi-action-https",":before{content:\"\\e638\"}");
			iconmap.put("mdi-action-info-outline",":before{content:\"\\e639\"}");
			iconmap.put("mdi-action-info",":before{content:\"\\e63a\"}");
			iconmap.put("mdi-action-input",":before{content:\"\\e63b\"}");
			iconmap.put("mdi-action-invert-colors",":before{content:\"\\e63c\"}");
			iconmap.put("mdi-action-label-outline",":before{content:\"\\e63d\"}");
			iconmap.put("mdi-action-label",":before{content:\"\\e63e\"}");
			iconmap.put("mdi-action-language",":before{content:\"\\e63f\"}");
			iconmap.put("mdi-action-launch",":before{content:\"\\e640\"}");
			iconmap.put("mdi-action-list",":before{content:\"\\e641\"}");
			iconmap.put("mdi-action-lock-open",":before{content:\"\\e642\"}");
			iconmap.put("mdi-action-lock-outline",":before{content:\"\\e643\"}");
			iconmap.put("mdi-action-lock",":before{content:\"\\e644\"}");
			iconmap.put("mdi-action-loyalty",":before{content:\"\\e645\"}");
			iconmap.put("mdi-action-markunread-mailbox",":before{content:\"\\e646\"}");
			iconmap.put("mdi-action-note-add",":before{content:\"\\e647\"}");
			iconmap.put("mdi-action-open-in-browser",":before{content:\"\\e648\"}");
			iconmap.put("mdi-action-open-in-new",":before{content:\"\\e649\"}");
			iconmap.put("mdi-action-open-with",":before{content:\"\\e64a\"}");
			iconmap.put("mdi-action-pageview",":before{content:\"\\e64b\"}");
			iconmap.put("mdi-action-payment",":before{content:\"\\e64c\"}");
			iconmap.put("mdi-action-perm-camera-mic",":before{content:\"\\e64d\"}");
			iconmap.put("mdi-action-perm-contact-cal",":before{content:\"\\e64e\"}");
			iconmap.put("mdi-action-perm-data-setting",":before{content:\"\\e64f\"}");
			iconmap.put("mdi-action-perm-device-info",":before{content:\"\\e650\"}");
			iconmap.put("mdi-action-perm-identity",":before{content:\"\\e651\"}");
			iconmap.put("mdi-action-perm-media",":before{content:\"\\e652\"}");
			iconmap.put("mdi-action-perm-phone-msg",":before{content:\"\\e653\"}");
			iconmap.put("mdi-action-perm-scan-wifi",":before{content:\"\\e654\"}");
			iconmap.put("mdi-action-picture-in-picture",":before{content:\"\\e655\"}");
			iconmap.put("mdi-action-polymer",":before{content:\"\\e656\"}");
			iconmap.put("mdi-action-print",":before{content:\"\\e657\"}");
			iconmap.put("mdi-action-query-builder",":before{content:\"\\e658\"}");
			iconmap.put("mdi-action-question-answer",":before{content:\"\\e659\"}");
			iconmap.put("mdi-action-receipt",":before{content:\"\\e65a\"}");
			iconmap.put("mdi-action-redeem",":before{content:\"\\e65b\"}");
			iconmap.put("mdi-action-reorder",":before{content:\"\\e65c\"}");
			iconmap.put("mdi-action-report-problem",":before{content:\"\\e65d\"}");
			iconmap.put("mdi-action-restore",":before{content:\"\\e65e\"}");
			iconmap.put("mdi-action-room",":before{content:\"\\e65f\"}");
			iconmap.put("mdi-action-schedule",":before{content:\"\\e660\"}");
			iconmap.put("mdi-action-search",":before{content:\"\\e661\"}");
			iconmap.put("mdi-action-settings-applications",":before{content:\"\\e662\"}");
			iconmap.put("mdi-action-settings-backup-restore",":before{content:\"\\e663\"}");
			iconmap.put("mdi-action-settings-bluetooth",":before{content:\"\\e664\"}");
			iconmap.put("mdi-action-settings-cell",":before{content:\"\\e665\"}");
			iconmap.put("mdi-action-settings-display",":before{content:\"\\e666\"}");
			iconmap.put("mdi-action-settings-ethernet",":before{content:\"\\e667\"}");
			iconmap.put("mdi-action-settings-input-antenna",":before{content:\"\\e668\"}");
			iconmap.put("mdi-action-settings-input-component",":before{content:\"\\e669\"}");
			iconmap.put("mdi-action-settings-input-composite",":before{content:\"\\e66a\"}");
			iconmap.put("mdi-action-settings-input-hdmi",":before{content:\"\\e66b\"}");
			iconmap.put("mdi-action-settings-input-svideo",":before{content:\"\\e66c\"}");
			iconmap.put("mdi-action-settings-overscan",":before{content:\"\\e66d\"}");
			iconmap.put("mdi-action-settings-phone",":before{content:\"\\e66e\"}");
			iconmap.put("mdi-action-settings-power",":before{content:\"\\e66f\"}");
			iconmap.put("mdi-action-settings-remote",":before{content:\"\\e670\"}");
			iconmap.put("mdi-action-settings-voice",":before{content:\"\\e671\"}");
			iconmap.put("mdi-action-settings",":before{content:\"\\e672\"}");
			iconmap.put("mdi-action-shop-two",":before{content:\"\\e673\"}");
			iconmap.put("mdi-action-shop",":before{content:\"\\e674\"}");
			iconmap.put("mdi-action-shopping-basket",":before{content:\"\\e675\"}");
			iconmap.put("mdi-action-shopping-cart",":before{content:\"\\e676\"}");
			iconmap.put("mdi-action-speaker-notes",":before{content:\"\\e677\"}");
			iconmap.put("mdi-action-spellcheck",":before{content:\"\\e678\"}");
			iconmap.put("mdi-action-star-rate",":before{content:\"\\e679\"}");
			iconmap.put("mdi-action-stars",":before{content:\"\\e67a\"}");
			iconmap.put("mdi-action-store",":before{content:\"\\e67b\"}");
			iconmap.put("mdi-action-subject",":before{content:\"\\e67c\"}");
			iconmap.put("mdi-action-supervisor-account",":before{content:\"\\e67d\"}");
			iconmap.put("mdi-action-swap-horiz",":before{content:\"\\e67e\"}");
			iconmap.put("mdi-action-swap-vert-circle",":before{content:\"\\e67f\"}");
			iconmap.put("mdi-action-swap-vert",":before{content:\"\\e680\"}");
			iconmap.put("mdi-action-system-update-tv",":before{content:\"\\e681\"}");
			iconmap.put("mdi-action-tab-unselected",":before{content:\"\\e682\"}");
			iconmap.put("mdi-action-tab",":before{content:\"\\e683\"}");
			iconmap.put("mdi-action-theaters",":before{content:\"\\e684\"}");
			iconmap.put("mdi-action-thumb-down",":before{content:\"\\e685\"}");
			iconmap.put("mdi-action-thumb-up",":before{content:\"\\e686\"}");
			iconmap.put("mdi-action-thumbs-up-down",":before{content:\"\\e687\"}");
			iconmap.put("mdi-action-toc",":before{content:\"\\e688\"}");
			iconmap.put("mdi-action-today",":before{content:\"\\e689\"}");
			iconmap.put("mdi-action-track-changes",":before{content:\"\\e68a\"}");
			iconmap.put("mdi-action-translate",":before{content:\"\\e68b\"}");
			iconmap.put("mdi-action-trending-down",":before{content:\"\\e68c\"}");
			iconmap.put("mdi-action-trending-neutral",":before{content:\"\\e68d\"}");
			iconmap.put("mdi-action-trending-up",":before{content:\"\\e68e\"}");
			iconmap.put("mdi-action-turned-in-not",":before{content:\"\\e68f\"}");
			iconmap.put("mdi-action-turned-in",":before{content:\"\\e690\"}");
			iconmap.put("mdi-action-verified-user",":before{content:\"\\e691\"}");
			iconmap.put("mdi-action-view-agenda",":before{content:\"\\e692\"}");
			iconmap.put("mdi-action-view-array",":before{content:\"\\e693\"}");
			iconmap.put("mdi-action-view-carousel",":before{content:\"\\e694\"}");
			iconmap.put("mdi-action-view-column",":before{content:\"\\e695\"}");
			iconmap.put("mdi-action-view-day",":before{content:\"\\e696\"}");
			iconmap.put("mdi-action-view-headline",":before{content:\"\\e697\"}");
			iconmap.put("mdi-action-view-list",":before{content:\"\\e698\"}");
			iconmap.put("mdi-action-view-module",":before{content:\"\\e699\"}");
			iconmap.put("mdi-action-view-quilt",":before{content:\"\\e69a\"}");
			iconmap.put("mdi-action-view-stream",":before{content:\"\\e69b\"}");
			iconmap.put("mdi-action-view-week",":before{content:\"\\e69c\"}");
			iconmap.put("mdi-action-visibility-off",":before{content:\"\\e69d\"}");
			iconmap.put("mdi-action-visibility",":before{content:\"\\e69e\"}");
			iconmap.put("mdi-action-wallet-giftcard",":before{content:\"\\e69f\"}");
			iconmap.put("mdi-action-wallet-membership",":before{content:\"\\e6a0\"}");
			iconmap.put("mdi-action-wallet-travel",":before{content:\"\\e6a1\"}");
			iconmap.put("mdi-action-work",":before{content:\"\\e6a2\"}");
			iconmap.put("mdi-alert-error",":before{content:\"\\e6a3\"}");
			iconmap.put("mdi-alert-warning",":before{content:\"\\e6a4\"}");
			iconmap.put("mdi-av-album",":before{content:\"\\e6a5\"}");
			iconmap.put("mdi-av-closed-caption",":before{content:\"\\e6a6\"}");
			iconmap.put("mdi-av-equalizer",":before{content:\"\\e6a7\"}");
			iconmap.put("mdi-av-explicit",":before{content:\"\\e6a8\"}");
			iconmap.put("mdi-av-fast-forward",":before{content:\"\\e6a9\"}");
			iconmap.put("mdi-av-fast-rewind",":before{content:\"\\e6aa\"}");
			iconmap.put("mdi-av-games",":before{content:\"\\e6ab\"}");
			iconmap.put("mdi-av-hearing",":before{content:\"\\e6ac\"}");
			iconmap.put("mdi-av-high-quality",":before{content:\"\\e6ad\"}");
			iconmap.put("mdi-av-loop",":before{content:\"\\e6ae\"}");
			iconmap.put("mdi-av-mic-none",":before{content:\"\\e6af\"}");
			iconmap.put("mdi-av-mic-off",":before{content:\"\\e6b0\"}");
			iconmap.put("mdi-av-mic",":before{content:\"\\e6b1\"}");
			iconmap.put("mdi-av-movie",":before{content:\"\\e6b2\"}");
			iconmap.put("mdi-av-my-library-add",":before{content:\"\\e6b3\"}");
			iconmap.put("mdi-av-my-library-books",":before{content:\"\\e6b4\"}");
			iconmap.put("mdi-av-my-library-music",":before{content:\"\\e6b5\"}");
			iconmap.put("mdi-av-new-releases",":before{content:\"\\e6b6\"}");
			iconmap.put("mdi-av-not-interested",":before{content:\"\\e6b7\"}");
			iconmap.put("mdi-av-pause-circle-fill",":before{content:\"\\e6b8\"}");
			iconmap.put("mdi-av-pause-circle-outline",":before{content:\"\\e6b9\"}");
			iconmap.put("mdi-av-pause",":before{content:\"\\e6ba\"}");
			iconmap.put("mdi-av-play-arrow",":before{content:\"\\e6bb\"}");
			iconmap.put("mdi-av-play-circle-fill",":before{content:\"\\e6bc\"}");
			iconmap.put("mdi-av-play-circle-outline",":before{content:\"\\e6bd\"}");
			iconmap.put("mdi-av-play-shopping-bag",":before{content:\"\\e6be\"}");
			iconmap.put("mdi-av-playlist-add",":before{content:\"\\e6bf\"}");
			iconmap.put("mdi-av-queue-music",":before{content:\"\\e6c0\"}");
			iconmap.put("mdi-av-queue",":before{content:\"\\e6c1\"}");
			iconmap.put("mdi-av-radio",":before{content:\"\\e6c2\"}");
			iconmap.put("mdi-av-recent-actors",":before{content:\"\\e6c3\"}");
			iconmap.put("mdi-av-repeat-one",":before{content:\"\\e6c4\"}");
			iconmap.put("mdi-av-repeat",":before{content:\"\\e6c5\"}");
			iconmap.put("mdi-av-replay",":before{content:\"\\e6c6\"}");
			iconmap.put("mdi-av-shuffle",":before{content:\"\\e6c7\"}");
			iconmap.put("mdi-av-skip-next",":before{content:\"\\e6c8\"}");
			iconmap.put("mdi-av-skip-previous",":before{content:\"\\e6c9\"}");
			iconmap.put("mdi-av-snooze",":before{content:\"\\e6ca\"}");
			iconmap.put("mdi-av-stop",":before{content:\"\\e6cb\"}");
			iconmap.put("mdi-av-subtitles",":before{content:\"\\e6cc\"}");
			iconmap.put("mdi-av-surround-sound",":before{content:\"\\e6cd\"}");
			iconmap.put("mdi-av-timer",":before{content:\"\\e6ce\"}");
			iconmap.put("mdi-av-video-collection",":before{content:\"\\e6cf\"}");
			iconmap.put("mdi-av-videocam-off",":before{content:\"\\e6d0\"}");
			iconmap.put("mdi-av-videocam",":before{content:\"\\e6d1\"}");
			iconmap.put("mdi-av-volume-down",":before{content:\"\\e6d2\"}");
			iconmap.put("mdi-av-volume-mute",":before{content:\"\\e6d3\"}");
			iconmap.put("mdi-av-volume-off",":before{content:\"\\e6d4\"}");
			iconmap.put("mdi-av-volume-up",":before{content:\"\\e6d5\"}");
			iconmap.put("mdi-av-web",":before{content:\"\\e6d6\"}");
			iconmap.put("mdi-communication-business",":before{content:\"\\e6d7\"}");
			iconmap.put("mdi-communication-call-end",":before{content:\"\\e6d8\"}");
			iconmap.put("mdi-communication-call-made",":before{content:\"\\e6d9\"}");
			iconmap.put("mdi-communication-call-merge",":before{content:\"\\e6da\"}");
			iconmap.put("mdi-communication-call-missed",":before{content:\"\\e6db\"}");
			iconmap.put("mdi-communication-call-received",":before{content:\"\\e6dc\"}");
			iconmap.put("mdi-communication-call-split",":before{content:\"\\e6dd\"}");
			iconmap.put("mdi-communication-call",":before{content:\"\\e6de\"}");
			iconmap.put("mdi-communication-chat",":before{content:\"\\e6df\"}");
			iconmap.put("mdi-communication-clear-all",":before{content:\"\\e6e0\"}");
			iconmap.put("mdi-communication-comment",":before{content:\"\\e6e1\"}");
			iconmap.put("mdi-communication-contacts",":before{content:\"\\e6e2\"}");
			iconmap.put("mdi-communication-dialer-sip",":before{content:\"\\e6e3\"}");
			iconmap.put("mdi-communication-dialpad",":before{content:\"\\e6e4\"}");
			iconmap.put("mdi-communication-dnd-on",":before{content:\"\\e6e5\"}");
			iconmap.put("mdi-communication-email",":before{content:\"\\e6e6\"}");
			iconmap.put("mdi-communication-forum",":before{content:\"\\e6e7\"}");
			iconmap.put("mdi-communication-import-export",":before{content:\"\\e6e8\"}");
			iconmap.put("mdi-communication-invert-colors-off",":before{content:\"\\e6e9\"}");
			iconmap.put("mdi-communication-invert-colors-on",":before{content:\"\\e6ea\"}");
			iconmap.put("mdi-communication-live-help",":before{content:\"\\e6eb\"}");
			iconmap.put("mdi-communication-location-off",":before{content:\"\\e6ec\"}");
			iconmap.put("mdi-communication-location-on",":before{content:\"\\e6ed\"}");
			iconmap.put("mdi-communication-message",":before{content:\"\\e6ee\"}");
			iconmap.put("mdi-communication-messenger",":before{content:\"\\e6ef\"}");
			iconmap.put("mdi-communication-no-sim",":before{content:\"\\e6f0\"}");
			iconmap.put("mdi-communication-phone",":before{content:\"\\e6f1\"}");
			iconmap.put("mdi-communication-portable-wifi-off",":before{content:\"\\e6f2\"}");
			iconmap.put("mdi-communication-quick-contacts-dialer",":before{content:\"\\e6f3\"}");
			iconmap.put("mdi-communication-quick-contacts-mail",":before{content:\"\\e6f4\"}");
			iconmap.put("mdi-communication-ring-volume",":before{content:\"\\e6f5\"}");
			iconmap.put("mdi-communication-stay-current-landscape",":before{content:\"\\e6f6\"}");
			iconmap.put("mdi-communication-stay-current-portrait",":before{content:\"\\e6f7\"}");
			iconmap.put("mdi-communication-stay-primary-landscape",":before{content:\"\\e6f8\"}");
			iconmap.put("mdi-communication-stay-primary-portrait",":before{content:\"\\e6f9\"}");
			iconmap.put("mdi-communication-swap-calls",":before{content:\"\\e6fa\"}");
			iconmap.put("mdi-communication-textsms",":before{content:\"\\e6fb\"}");
			iconmap.put("mdi-communication-voicemail",":before{content:\"\\e6fc\"}");
			iconmap.put("mdi-communication-vpn-key",":before{content:\"\\e6fd\"}");
			iconmap.put("mdi-content-add-box",":before{content:\"\\e6fe\"}");
			iconmap.put("mdi-content-add-circle-outline",":before{content:\"\\e6ff\"}");
			iconmap.put("mdi-content-add-circle",":before{content:\"\\e700\"}");
			iconmap.put("mdi-content-add",":before{content:\"\\e701\"}");
			iconmap.put("mdi-content-archive",":before{content:\"\\e702\"}");
			iconmap.put("mdi-content-backspace",":before{content:\"\\e703\"}");
			iconmap.put("mdi-content-block",":before{content:\"\\e704\"}");
			iconmap.put("mdi-content-clear",":before{content:\"\\e705\"}");
			iconmap.put("mdi-content-content-copy",":before{content:\"\\e706\"}");
			iconmap.put("mdi-content-content-cut",":before{content:\"\\e707\"}");
			iconmap.put("mdi-content-content-paste",":before{content:\"\\e708\"}");
			iconmap.put("mdi-content-create",":before{content:\"\\e709\"}");
			iconmap.put("mdi-content-drafts",":before{content:\"\\e70a\"}");
			iconmap.put("mdi-content-filter-list",":before{content:\"\\e70b\"}");
			iconmap.put("mdi-content-flag",":before{content:\"\\e70c\"}");
			iconmap.put("mdi-content-forward",":before{content:\"\\e70d\"}");
			iconmap.put("mdi-content-gesture",":before{content:\"\\e70e\"}");
			iconmap.put("mdi-content-inbox",":before{content:\"\\e70f\"}");
			iconmap.put("mdi-content-link",":before{content:\"\\e710\"}");
			iconmap.put("mdi-content-mail",":before{content:\"\\e711\"}");
			iconmap.put("mdi-content-markunread",":before{content:\"\\e712\"}");
			iconmap.put("mdi-content-redo",":before{content:\"\\e713\"}");
			iconmap.put("mdi-content-remove-circle-outline",":before{content:\"\\e714\"}");
			iconmap.put("mdi-content-remove-circle",":before{content:\"\\e715\"}");
			iconmap.put("mdi-content-remove",":before{content:\"\\e716\"}");
			iconmap.put("mdi-content-reply-all",":before{content:\"\\e717\"}");
			iconmap.put("mdi-content-reply",":before{content:\"\\e718\"}");
			iconmap.put("mdi-content-report",":before{content:\"\\e719\"}");
			iconmap.put("mdi-content-save",":before{content:\"\\e71a\"}");
			iconmap.put("mdi-content-select-all",":before{content:\"\\e71b\"}");
			iconmap.put("mdi-content-send",":before{content:\"\\e71c\"}");
			iconmap.put("mdi-content-sort",":before{content:\"\\e71d\"}");
			iconmap.put("mdi-content-text-format",":before{content:\"\\e71e\"}");
			iconmap.put("mdi-content-undo",":before{content:\"\\e71f\"}");
			iconmap.put("mdi-editor-attach-file",":before{content:\"\\e776\"}");
			iconmap.put("mdi-editor-attach-money",":before{content:\"\\e777\"}");
			iconmap.put("mdi-editor-border-all",":before{content:\"\\e778\"}");
			iconmap.put("mdi-editor-border-bottom",":before{content:\"\\e779\"}");
			iconmap.put("mdi-editor-border-clear",":before{content:\"\\e77a\"}");
			iconmap.put("mdi-editor-border-color",":before{content:\"\\e77b\"}");
			iconmap.put("mdi-editor-border-horizontal",":before{content:\"\\e77c\"}");
			iconmap.put("mdi-editor-border-inner",":before{content:\"\\e77d\"}");
			iconmap.put("mdi-editor-border-left",":before{content:\"\\e77e\"}");
			iconmap.put("mdi-editor-border-outer",":before{content:\"\\e77f\"}");
			iconmap.put("mdi-editor-border-right",":before{content:\"\\e780\"}");
			iconmap.put("mdi-editor-border-style",":before{content:\"\\e781\"}");
			iconmap.put("mdi-editor-border-top",":before{content:\"\\e782\"}");
			iconmap.put("mdi-editor-border-vertical",":before{content:\"\\e783\"}");
			iconmap.put("mdi-editor-format-align-center",":before{content:\"\\e784\"}");
			iconmap.put("mdi-editor-format-align-justify",":before{content:\"\\e785\"}");
			iconmap.put("mdi-editor-format-align-left",":before{content:\"\\e786\"}");
			iconmap.put("mdi-editor-format-align-right",":before{content:\"\\e787\"}");
			iconmap.put("mdi-editor-format-bold",":before{content:\"\\e788\"}");
			iconmap.put("mdi-editor-format-clear",":before{content:\"\\e789\"}");
			iconmap.put("mdi-editor-format-color-fill",":before{content:\"\\e78a\"}");
			iconmap.put("mdi-editor-format-color-reset",":before{content:\"\\e78b\"}");
			iconmap.put("mdi-editor-format-color-text",":before{content:\"\\e78c\"}");
			iconmap.put("mdi-editor-format-indent-decrease",":before{content:\"\\e78d\"}");
			iconmap.put("mdi-editor-format-indent-increase",":before{content:\"\\e78e\"}");
			iconmap.put("mdi-editor-format-italic",":before{content:\"\\e78f\"}");
			iconmap.put("mdi-editor-format-line-spacing",":before{content:\"\\e790\"}");
			iconmap.put("mdi-editor-format-list-bulleted",":before{content:\"\\e791\"}");
			iconmap.put("mdi-editor-format-list-numbered",":before{content:\"\\e792\"}");
			iconmap.put("mdi-editor-format-paint",":before{content:\"\\e793\"}");
			iconmap.put("mdi-editor-format-quote",":before{content:\"\\e794\"}");
			iconmap.put("mdi-editor-format-size",":before{content:\"\\e795\"}");
			iconmap.put("mdi-editor-format-strikethrough",":before{content:\"\\e796\"}");
			iconmap.put("mdi-editor-format-textdirection-l-to-r",":before{content:\"\\e797\"}");
			iconmap.put("mdi-editor-format-textdirection-r-to-l",":before{content:\"\\e798\"}");
			iconmap.put("mdi-editor-format-underline",":before{content:\"\\e799\"}");
			iconmap.put("mdi-editor-functions",":before{content:\"\\e79a\"}");
			iconmap.put("mdi-editor-insert-chart",":before{content:\"\\e79b\"}");
			iconmap.put("mdi-editor-insert-comment",":before{content:\"\\e79c\"}");
			iconmap.put("mdi-editor-insert-drive-file",":before{content:\"\\e79d\"}");
			iconmap.put("mdi-editor-insert-emoticon",":before{content:\"\\e79e\"}");
			iconmap.put("mdi-editor-insert-invitation",":before{content:\"\\e79f\"}");
			iconmap.put("mdi-editor-insert-link",":before{content:\"\\e7a0\"}");
			iconmap.put("mdi-editor-insert-photo",":before{content:\"\\e7a1\"}");
			iconmap.put("mdi-editor-merge-type",":before{content:\"\\e7a2\"}");
			iconmap.put("mdi-editor-mode-comment",":before{content:\"\\e7a3\"}");
			iconmap.put("mdi-editor-mode-edit",":before{content:\"\\e7a4\"}");
			iconmap.put("mdi-editor-publish",":before{content:\"\\e7a5\"}");
			iconmap.put("mdi-editor-vertical-align-bottom",":before{content:\"\\e7a6\"}");
			iconmap.put("mdi-editor-vertical-align-center",":before{content:\"\\e7a7\"}");
			iconmap.put("mdi-editor-vertical-align-top",":before{content:\"\\e7a8\"}");
			iconmap.put("mdi-editor-wrap-text",":before{content:\"\\e7a9\"}");
			iconmap.put("mdi-file-attachment",":before{content:\"\\e7aa\"}");
			iconmap.put("mdi-file-cloud-circle",":before{content:\"\\e7ab\"}");
			iconmap.put("mdi-file-cloud-done",":before{content:\"\\e7ac\"}");
			iconmap.put("mdi-file-cloud-download",":before{content:\"\\e7ad\"}");
			iconmap.put("mdi-file-cloud-off",":before{content:\"\\e7ae\"}");
			iconmap.put("mdi-file-cloud-queue",":before{content:\"\\e7af\"}");
			iconmap.put("mdi-file-cloud-upload",":before{content:\"\\e7b0\"}");
			iconmap.put("mdi-file-cloud",":before{content:\"\\e7b1\"}");
			iconmap.put("mdi-file-file-download",":before{content:\"\\e7b2\"}");
			iconmap.put("mdi-file-file-upload",":before{content:\"\\e7b3\"}");
			iconmap.put("mdi-file-folder-open",":before{content:\"\\e7b4\"}");
			iconmap.put("mdi-file-folder-shared",":before{content:\"\\e7b5\"}");
			iconmap.put("mdi-file-folder",":before{content:\"\\e7b6\"}");
			iconmap.put("mdi-device-access-alarm",":before{content:\"\\e720\"}");
			iconmap.put("mdi-device-access-alarms",":before{content:\"\\e721\"}");
			iconmap.put("mdi-device-access-time",":before{content:\"\\e722\"}");
			iconmap.put("mdi-device-add-alarm",":before{content:\"\\e723\"}");
			iconmap.put("mdi-device-airplanemode-off",":before{content:\"\\e724\"}");
			iconmap.put("mdi-device-airplanemode-on",":before{content:\"\\e725\"}");
			iconmap.put("mdi-device-battery-20",":before{content:\"\\e726\"}");
			iconmap.put("mdi-device-battery-30",":before{content:\"\\e727\"}");
			iconmap.put("mdi-device-battery-50",":before{content:\"\\e728\"}");
			iconmap.put("mdi-device-battery-60",":before{content:\"\\e729\"}");
			iconmap.put("mdi-device-battery-80",":before{content:\"\\e72a\"}");
			iconmap.put("mdi-device-battery-90",":before{content:\"\\e72b\"}");
			iconmap.put("mdi-device-battery-alert",":before{content:\"\\e72c\"}");
			iconmap.put("mdi-device-battery-charging-20",":before{content:\"\\e72d\"}");
			iconmap.put("mdi-device-battery-charging-30",":before{content:\"\\e72e\"}");
			iconmap.put("mdi-device-battery-charging-50",":before{content:\"\\e72f\"}");
			iconmap.put("mdi-device-battery-charging-60",":before{content:\"\\e730\"}");
			iconmap.put("mdi-device-battery-charging-80",":before{content:\"\\e731\"}");
			iconmap.put("mdi-device-battery-charging-90",":before{content:\"\\e732\"}");
			iconmap.put("mdi-device-battery-charging-full",":before{content:\"\\e733\"}");
			iconmap.put("mdi-device-battery-full",":before{content:\"\\e734\"}");
			iconmap.put("mdi-device-battery-std",":before{content:\"\\e735\"}");
			iconmap.put("mdi-device-battery-unknown",":before{content:\"\\e736\"}");
			iconmap.put("mdi-device-bluetooth-connected",":before{content:\"\\e737\"}");
			iconmap.put("mdi-device-bluetooth-disabled",":before{content:\"\\e738\"}");
			iconmap.put("mdi-device-bluetooth-searching",":before{content:\"\\e739\"}");
			iconmap.put("mdi-device-bluetooth",":before{content:\"\\e73a\"}");
			iconmap.put("mdi-device-brightness-auto",":before{content:\"\\e73b\"}");
			iconmap.put("mdi-device-brightness-high",":before{content:\"\\e73c\"}");
			iconmap.put("mdi-device-brightness-low",":before{content:\"\\e73d\"}");
			iconmap.put("mdi-device-brightness-medium",":before{content:\"\\e73e\"}");
			iconmap.put("mdi-device-data-usage",":before{content:\"\\e73f\"}");
			iconmap.put("mdi-device-developer-mode",":before{content:\"\\e740\"}");
			iconmap.put("mdi-device-devices",":before{content:\"\\e741\"}");
			iconmap.put("mdi-device-dvr",":before{content:\"\\e742\"}");
			iconmap.put("mdi-device-gps-fixed",":before{content:\"\\e743\"}");
			iconmap.put("mdi-device-gps-not-fixed",":before{content:\"\\e744\"}");
			iconmap.put("mdi-device-gps-off",":before{content:\"\\e745\"}");
			iconmap.put("mdi-device-location-disabled",":before{content:\"\\e746\"}");
			iconmap.put("mdi-device-location-searching",":before{content:\"\\e747\"}");
			iconmap.put("mdi-device-multitrack-audio",":before{content:\"\\e748\"}");
			iconmap.put("mdi-device-network-cell",":before{content:\"\\e749\"}");
			iconmap.put("mdi-device-network-wifi",":before{content:\"\\e74a\"}");
			iconmap.put("mdi-device-nfc",":before{content:\"\\e74b\"}");
			iconmap.put("mdi-device-now-wallpaper",":before{content:\"\\e74c\"}");
			iconmap.put("mdi-device-now-widgets",":before{content:\"\\e74d\"}");
			iconmap.put("mdi-device-screen-lock-landscape",":before{content:\"\\e74e\"}");
			iconmap.put("mdi-device-screen-lock-portrait",":before{content:\"\\e74f\"}");
			iconmap.put("mdi-device-screen-lock-rotation",":before{content:\"\\e750\"}");
			iconmap.put("mdi-device-screen-rotation",":before{content:\"\\e751\"}");
			iconmap.put("mdi-device-sd-storage",":before{content:\"\\e752\"}");
			iconmap.put("mdi-device-settings-system-daydream",":before{content:\"\\e753\"}");
			iconmap.put("mdi-device-signal-cellular-0-bar",":before{content:\"\\e754\"}");
			iconmap.put("mdi-device-signal-cellular-1-bar",":before{content:\"\\e755\"}");
			iconmap.put("mdi-device-signal-cellular-2-bar",":before{content:\"\\e756\"}");
			iconmap.put("mdi-device-signal-cellular-3-bar",":before{content:\"\\e757\"}");
			iconmap.put("mdi-device-signal-cellular-4-bar",":before{content:\"\\e758\"}");
			iconmap.put("mdi-signal-wifi-statusbar-connected-no-internet-after",":before{content:\"\\e8f6\"}");
			iconmap.put("mdi-device-signal-cellular-connected-no-internet-0-bar",":before{content:\"\\e759\"}");
			iconmap.put("mdi-device-signal-cellular-connected-no-internet-1-bar",":before{content:\"\\e75a\"}");
			iconmap.put("mdi-device-signal-cellular-connected-no-internet-2-bar",":before{content:\"\\e75b\"}");
			iconmap.put("mdi-device-signal-cellular-connected-no-internet-3-bar",":before{content:\"\\e75c\"}");
			iconmap.put("mdi-device-signal-cellular-connected-no-internet-4-bar",":before{content:\"\\e75d\"}");
			iconmap.put("mdi-device-signal-cellular-no-sim",":before{content:\"\\e75e\"}");
			iconmap.put("mdi-device-signal-cellular-null",":before{content:\"\\e75f\"}");
			iconmap.put("mdi-device-signal-cellular-off",":before{content:\"\\e760\"}");
			iconmap.put("mdi-device-signal-wifi-0-bar",":before{content:\"\\e761\"}");
			iconmap.put("mdi-device-signal-wifi-1-bar",":before{content:\"\\e762\"}");
			iconmap.put("mdi-device-signal-wifi-2-bar",":before{content:\"\\e763\"}");
			iconmap.put("mdi-device-signal-wifi-3-bar",":before{content:\"\\e764\"}");
			iconmap.put("mdi-device-signal-wifi-4-bar",":before{content:\"\\e765\"}");
			iconmap.put("mdi-device-signal-wifi-off",":before{content:\"\\e766\"}");
			iconmap.put("mdi-device-signal-wifi-statusbar-1-bar",":before{content:\"\\e767\"}");
			iconmap.put("mdi-device-signal-wifi-statusbar-2-bar",":before{content:\"\\e768\"}");
			iconmap.put("mdi-device-signal-wifi-statusbar-3-bar",":before{content:\"\\e769\"}");
			iconmap.put("mdi-device-signal-wifi-statusbar-4-bar",":before{content:\"\\e76a\"}");
			iconmap.put("mdi-device-signal-wifi-statusbar-connected-no-internet-",":before{content:\"\\e76b\"}");
			iconmap.put("mdi-device-signal-wifi-statusbar-connected-no-internet",":before{content:\"\\e76f\"}");
			iconmap.put("mdi-device-signal-wifi-statusbar-connected-no-internet-2",":before{content:\"\\e76c\"}");
			iconmap.put("mdi-device-signal-wifi-statusbar-connected-no-internet-3",":before{content:\"\\e76d\"}");
			iconmap.put("mdi-device-signal-wifi-statusbar-connected-no-internet-4",":before{content:\"\\e76e\"}");
			iconmap.put("mdi-signal-wifi-statusbar-not-connected-after",":before{content:\"\\e8f7\"}");
			iconmap.put("mdi-device-signal-wifi-statusbar-not-connected",":before{content:\"\\e770\"}");
			iconmap.put("mdi-device-signal-wifi-statusbar-null",":before{content:\"\\e771\"}");
			iconmap.put("mdi-device-storage",":before{content:\"\\e772\"}");
			iconmap.put("mdi-device-usb",":before{content:\"\\e773\"}");
			iconmap.put("mdi-device-wifi-lock",":before{content:\"\\e774\"}");
			iconmap.put("mdi-device-wifi-tethering",":before{content:\"\\e775\"}");
			iconmap.put("mdi-hardware-cast-connected",":before{content:\"\\e7b7\"}");
			iconmap.put("mdi-hardware-cast",":before{content:\"\\e7b8\"}");
			iconmap.put("mdi-hardware-computer",":before{content:\"\\e7b9\"}");
			iconmap.put("mdi-hardware-desktop-mac",":before{content:\"\\e7ba\"}");
			iconmap.put("mdi-hardware-desktop-windows",":before{content:\"\\e7bb\"}");
			iconmap.put("mdi-hardware-dock",":before{content:\"\\e7bc\"}");
			iconmap.put("mdi-hardware-gamepad",":before{content:\"\\e7bd\"}");
			iconmap.put("mdi-hardware-headset-mic",":before{content:\"\\e7be\"}");
			iconmap.put("mdi-hardware-headset",":before{content:\"\\e7bf\"}");
			iconmap.put("mdi-hardware-keyboard-alt",":before{content:\"\\e7c0\"}");
			iconmap.put("mdi-hardware-keyboard-arrow-down",":before{content:\"\\e7c1\"}");
			iconmap.put("mdi-hardware-keyboard-arrow-left",":before{content:\"\\e7c2\"}");
			iconmap.put("mdi-hardware-keyboard-arrow-right",":before{content:\"\\e7c3\"}");
			iconmap.put("mdi-hardware-keyboard-arrow-up",":before{content:\"\\e7c4\"}");
			iconmap.put("mdi-hardware-keyboard-backspace",":before{content:\"\\e7c5\"}");
			iconmap.put("mdi-hardware-keyboard-capslock",":before{content:\"\\e7c6\"}");
			iconmap.put("mdi-hardware-keyboard-control",":before{content:\"\\e7c7\"}");
			iconmap.put("mdi-hardware-keyboard-hide",":before{content:\"\\e7c8\"}");
			iconmap.put("mdi-hardware-keyboard-return",":before{content:\"\\e7c9\"}");
			iconmap.put("mdi-hardware-keyboard-tab",":before{content:\"\\e7ca\"}");
			iconmap.put("mdi-hardware-keyboard-voice",":before{content:\"\\e7cb\"}");
			iconmap.put("mdi-hardware-keyboard",":before{content:\"\\e7cc\"}");
			iconmap.put("mdi-hardware-laptop-chromebook",":before{content:\"\\e7cd\"}");
			iconmap.put("mdi-hardware-laptop-mac",":before{content:\"\\e7ce\"}");
			iconmap.put("mdi-hardware-laptop-windows",":before{content:\"\\e7cf\"}");
			iconmap.put("mdi-hardware-laptop",":before{content:\"\\e7d0\"}");
			iconmap.put("mdi-hardware-memory",":before{content:\"\\e7d1\"}");
			iconmap.put("mdi-hardware-mouse",":before{content:\"\\e7d2\"}");
			iconmap.put("mdi-hardware-phone-android",":before{content:\"\\e7d3\"}");
			iconmap.put("mdi-hardware-phone-iphone",":before{content:\"\\e7d4\"}");
			iconmap.put("mdi-hardware-phonelink-off",":before{content:\"\\e7d5\"}");
			iconmap.put("mdi-hardware-phonelink",":before{content:\"\\e7d6\"}");
			iconmap.put("mdi-hardware-security",":before{content:\"\\e7d7\"}");
			iconmap.put("mdi-hardware-sim-card",":before{content:\"\\e7d8\"}");
			iconmap.put("mdi-hardware-smartphone",":before{content:\"\\e7d9\"}");
			iconmap.put("mdi-hardware-speaker",":before{content:\"\\e7da\"}");
			iconmap.put("mdi-hardware-tablet-android",":before{content:\"\\e7db\"}");
			iconmap.put("mdi-hardware-tablet-mac",":before{content:\"\\e7dc\"}");
			iconmap.put("mdi-hardware-tablet",":before{content:\"\\e7dd\"}");
			iconmap.put("mdi-hardware-tv",":before{content:\"\\e7de\"}");
			iconmap.put("mdi-hardware-watch",":before{content:\"\\e7df\"}");
			iconmap.put("mdi-image-add-to-photos",":before{content:\"\\e7e0\"}");
			iconmap.put("mdi-image-adjust",":before{content:\"\\e7e1\"}");
			iconmap.put("mdi-image-assistant-photo",":before{content:\"\\e7e2\"}");
			iconmap.put("mdi-image-audiotrack",":before{content:\"\\e7e3\"}");
			iconmap.put("mdi-image-blur-circular",":before{content:\"\\e7e4\"}");
			iconmap.put("mdi-image-blur-linear",":before{content:\"\\e7e5\"}");
			iconmap.put("mdi-image-blur-off",":before{content:\"\\e7e6\"}");
			iconmap.put("mdi-image-blur-on",":before{content:\"\\e7e7\"}");
			iconmap.put("mdi-image-brightness-1",":before{content:\"\\e7e8\"}");
			iconmap.put("mdi-image-brightness-2",":before{content:\"\\e7e9\"}");
			iconmap.put("mdi-image-brightness-3",":before{content:\"\\e7ea\"}");
			iconmap.put("mdi-image-brightness-4",":before{content:\"\\e7eb\"}");
			iconmap.put("mdi-image-brightness-5",":before{content:\"\\e7ec\"}");
			iconmap.put("mdi-image-brightness-6",":before{content:\"\\e7ed\"}");
			iconmap.put("mdi-image-brightness-7",":before{content:\"\\e7ee\"}");
			iconmap.put("mdi-image-brush",":before{content:\"\\e7ef\"}");
			iconmap.put("mdi-image-camera-alt",":before{content:\"\\e7f0\"}");
			iconmap.put("mdi-image-camera-front",":before{content:\"\\e7f1\"}");
			iconmap.put("mdi-image-camera-rear",":before{content:\"\\e7f2\"}");
			iconmap.put("mdi-image-camera-roll",":before{content:\"\\e7f3\"}");
			iconmap.put("mdi-image-camera",":before{content:\"\\e7f4\"}");
			iconmap.put("mdi-image-center-focus-strong",":before{content:\"\\e7f5\"}");
			iconmap.put("mdi-image-center-focus-weak",":before{content:\"\\e7f6\"}");
			iconmap.put("mdi-image-collections",":before{content:\"\\e7f7\"}");
			iconmap.put("mdi-image-color-lens",":before{content:\"\\e7f8\"}");
			iconmap.put("mdi-image-colorize",":before{content:\"\\e7f9\"}");
			iconmap.put("mdi-image-compare",":before{content:\"\\e7fa\"}");
			iconmap.put("mdi-image-control-point-duplicate",":before{content:\"\\e7fb\"}");
			iconmap.put("mdi-image-control-point",":before{content:\"\\e7fc\"}");
			iconmap.put("mdi-image-crop-3-2",":before{content:\"\\e7fd\"}");
			iconmap.put("mdi-image-crop-5-4",":before{content:\"\\e7fe\"}");
			iconmap.put("mdi-image-crop-7-5",":before{content:\"\\e7ff\"}");
			iconmap.put("mdi-image-crop-16-9",":before{content:\"\\e800\"}");
			iconmap.put("mdi-image-crop-din",":before{content:\"\\e801\"}");
			iconmap.put("mdi-image-crop-free",":before{content:\"\\e802\"}");
			iconmap.put("mdi-image-crop-landscape",":before{content:\"\\e803\"}");
			iconmap.put("mdi-image-crop-original",":before{content:\"\\e804\"}");
			iconmap.put("mdi-image-crop-portrait",":before{content:\"\\e805\"}");
			iconmap.put("mdi-image-crop-square",":before{content:\"\\e806\"}");
			iconmap.put("mdi-image-crop",":before{content:\"\\e807\"}");
			iconmap.put("mdi-image-dehaze",":before{content:\"\\e808\"}");
			iconmap.put("mdi-image-details",":before{content:\"\\e809\"}");
			iconmap.put("mdi-image-edit",":before{content:\"\\e80a\"}");
			iconmap.put("mdi-image-exposure-minus-1",":before{content:\"\\e80b\"}");
			iconmap.put("mdi-image-exposure-minus-2",":before{content:\"\\e80c\"}");
			iconmap.put("mdi-image-exposure-plus-1",":before{content:\"\\e80d\"}");
			iconmap.put("mdi-image-exposure-plus-2",":before{content:\"\\e80e\"}");
			iconmap.put("mdi-image-exposure-zero",":before{content:\"\\e80f\"}");
			iconmap.put("mdi-image-exposure",":before{content:\"\\e810\"}");
			iconmap.put("mdi-image-filter-1",":before{content:\"\\e811\"}");
			iconmap.put("mdi-image-filter-2",":before{content:\"\\e812\"}");
			iconmap.put("mdi-image-filter-3",":before{content:\"\\e813\"}");
			iconmap.put("mdi-image-filter-4",":before{content:\"\\e814\"}");
			iconmap.put("mdi-image-filter-5",":before{content:\"\\e815\"}");
			iconmap.put("mdi-image-filter-6",":before{content:\"\\e816\"}");
			iconmap.put("mdi-image-filter-7",":before{content:\"\\e817\"}");
			iconmap.put("mdi-image-filter-8",":before{content:\"\\e818\"}");
			iconmap.put("mdi-image-filter-9-plus",":before{content:\"\\e819\"}");
			iconmap.put("mdi-image-filter-9",":before{content:\"\\e81a\"}");
			iconmap.put("mdi-image-filter-b-and-w",":before{content:\"\\e81b\"}");
			iconmap.put("mdi-image-filter-center-focus",":before{content:\"\\e81c\"}");
			iconmap.put("mdi-image-filter-drama",":before{content:\"\\e81d\"}");
			iconmap.put("mdi-image-filter-frames",":before{content:\"\\e81e\"}");
			iconmap.put("mdi-image-filter-hdr",":before{content:\"\\e81f\"}");
			iconmap.put("mdi-image-filter-none",":before{content:\"\\e820\"}");
			iconmap.put("mdi-image-filter-tilt-shift",":before{content:\"\\e821\"}");
			iconmap.put("mdi-image-filter-vintage",":before{content:\"\\e822\"}");
			iconmap.put("mdi-image-filter",":before{content:\"\\e823\"}");
			iconmap.put("mdi-image-flare",":before{content:\"\\e824\"}");
			iconmap.put("mdi-image-flash-auto",":before{content:\"\\e825\"}");
			iconmap.put("mdi-image-flash-off",":before{content:\"\\e826\"}");
			iconmap.put("mdi-image-flash-on",":before{content:\"\\e827\"}");
			iconmap.put("mdi-image-flip",":before{content:\"\\e828\"}");
			iconmap.put("mdi-image-gradient",":before{content:\"\\e829\"}");
			iconmap.put("mdi-image-grain",":before{content:\"\\e82a\"}");
			iconmap.put("mdi-image-grid-off",":before{content:\"\\e82b\"}");
			iconmap.put("mdi-image-grid-on",":before{content:\"\\e82c\"}");
			iconmap.put("mdi-image-hdr-off",":before{content:\"\\e82d\"}");
			iconmap.put("mdi-image-hdr-on",":before{content:\"\\e82e\"}");
			iconmap.put("mdi-image-hdr-strong",":before{content:\"\\e82f\"}");
			iconmap.put("mdi-image-hdr-weak",":before{content:\"\\e830\"}");
			iconmap.put("mdi-image-healing",":before{content:\"\\e831\"}");
			iconmap.put("mdi-image-image-aspect-ratio",":before{content:\"\\e832\"}");
			iconmap.put("mdi-image-image",":before{content:\"\\e833\"}");
			iconmap.put("mdi-image-iso",":before{content:\"\\e834\"}");
			iconmap.put("mdi-image-landscape",":before{content:\"\\e835\"}");
			iconmap.put("mdi-image-leak-add",":before{content:\"\\e836\"}");
			iconmap.put("mdi-image-leak-remove",":before{content:\"\\e837\"}");
			iconmap.put("mdi-image-lens",":before{content:\"\\e838\"}");
			iconmap.put("mdi-image-looks-3",":before{content:\"\\e839\"}");
			iconmap.put("mdi-image-looks-4",":before{content:\"\\e83a\"}");
			iconmap.put("mdi-image-looks-5",":before{content:\"\\e83b\"}");
			iconmap.put("mdi-image-looks-6",":before{content:\"\\e83c\"}");
			iconmap.put("mdi-image-looks-one",":before{content:\"\\e83d\"}");
			iconmap.put("mdi-image-looks-two",":before{content:\"\\e83e\"}");
			iconmap.put("mdi-image-looks",":before{content:\"\\e83f\"}");
			iconmap.put("mdi-image-loupe",":before{content:\"\\e840\"}");
			iconmap.put("mdi-image-movie-creation",":before{content:\"\\e841\"}");
			iconmap.put("mdi-image-nature-people",":before{content:\"\\e842\"}");
			iconmap.put("mdi-image-nature",":before{content:\"\\e843\"}");
			iconmap.put("mdi-image-navigate-before",":before{content:\"\\e844\"}");
			iconmap.put("mdi-image-navigate-next",":before{content:\"\\e845\"}");
			iconmap.put("mdi-image-palette",":before{content:\"\\e846\"}");
			iconmap.put("mdi-image-panorama-fisheye",":before{content:\"\\e847\"}");
			iconmap.put("mdi-image-panorama-horizontal",":before{content:\"\\e848\"}");
			iconmap.put("mdi-image-panorama-vertical",":before{content:\"\\e849\"}");
			iconmap.put("mdi-image-panorama-wide-angle",":before{content:\"\\e84a\"}");
			iconmap.put("mdi-image-panorama",":before{content:\"\\e84b\"}");
			iconmap.put("mdi-image-photo-album",":before{content:\"\\e84c\"}");
			iconmap.put("mdi-image-photo-camera",":before{content:\"\\e84d\"}");
			iconmap.put("mdi-image-photo-library",":before{content:\"\\e84e\"}");
			iconmap.put("mdi-image-photo",":before{content:\"\\e84f\"}");
			iconmap.put("mdi-image-portrait",":before{content:\"\\e850\"}");
			iconmap.put("mdi-image-remove-red-eye",":before{content:\"\\e851\"}");
			iconmap.put("mdi-image-rotate-left",":before{content:\"\\e852\"}");
			iconmap.put("mdi-image-rotate-right",":before{content:\"\\e853\"}");
			iconmap.put("mdi-image-slideshow",":before{content:\"\\e854\"}");
			iconmap.put("mdi-image-straighten",":before{content:\"\\e855\"}");
			iconmap.put("mdi-image-style",":before{content:\"\\e856\"}");
			iconmap.put("mdi-image-switch-camera",":before{content:\"\\e857\"}");
			iconmap.put("mdi-image-switch-video",":before{content:\"\\e858\"}");
			iconmap.put("mdi-image-tag-faces",":before{content:\"\\e859\"}");
			iconmap.put("mdi-image-texture",":before{content:\"\\e85a\"}");
			iconmap.put("mdi-image-timelapse",":before{content:\"\\e85b\"}");
			iconmap.put("mdi-image-timer-3",":before{content:\"\\e85c\"}");
			iconmap.put("mdi-image-timer-10",":before{content:\"\\e85d\"}");
			iconmap.put("mdi-image-timer-auto",":before{content:\"\\e85e\"}");
			iconmap.put("mdi-image-timer-off",":before{content:\"\\e85f\"}");
			iconmap.put("mdi-image-timer",":before{content:\"\\e860\"}");
			iconmap.put("mdi-image-tonality",":before{content:\"\\e861\"}");
			iconmap.put("mdi-image-transform",":before{content:\"\\e862\"}");
			iconmap.put("mdi-image-tune",":before{content:\"\\e863\"}");
			iconmap.put("mdi-image-wb-auto",":before{content:\"\\e864\"}");
			iconmap.put("mdi-image-wb-cloudy",":before{content:\"\\e865\"}");
			iconmap.put("mdi-image-wb-incandescent",":before{content:\"\\e866\"}");
			iconmap.put("mdi-image-wb-irradescent",":before{content:\"\\e867\"}");
			iconmap.put("mdi-image-wb-sunny",":before{content:\"\\e868\"}");
			iconmap.put("mdi-maps-beenhere",":before{content:\"\\e869\"}");
			iconmap.put("mdi-maps-directions-bike",":before{content:\"\\e86a\"}");
			iconmap.put("mdi-maps-directions-bus",":before{content:\"\\e86b\"}");
			iconmap.put("mdi-maps-directions-car",":before{content:\"\\e86c\"}");
			iconmap.put("mdi-maps-directions-ferry",":before{content:\"\\e86d\"}");
			iconmap.put("mdi-maps-directions-subway",":before{content:\"\\e86e\"}");
			iconmap.put("mdi-maps-directions-train",":before{content:\"\\e86f\"}");
			iconmap.put("mdi-maps-directions-transit",":before{content:\"\\e870\"}");
			iconmap.put("mdi-maps-directions-walk",":before{content:\"\\e871\"}");
			iconmap.put("mdi-maps-directions",":before{content:\"\\e872\"}");
			iconmap.put("mdi-maps-flight",":before{content:\"\\e873\"}");
			iconmap.put("mdi-maps-hotel",":before{content:\"\\e874\"}");
			iconmap.put("mdi-maps-layers-clear",":before{content:\"\\e875\"}");
			iconmap.put("mdi-maps-layers",":before{content:\"\\e876\"}");
			iconmap.put("mdi-maps-local-airport",":before{content:\"\\e877\"}");
			iconmap.put("mdi-maps-local-atm",":before{content:\"\\e878\"}");
			iconmap.put("mdi-maps-local-attraction",":before{content:\"\\e879\"}");
			iconmap.put("mdi-maps-local-bar",":before{content:\"\\e87a\"}");
			iconmap.put("mdi-maps-local-cafe",":before{content:\"\\e87b\"}");
			iconmap.put("mdi-maps-local-car-wash",":before{content:\"\\e87c\"}");
			iconmap.put("mdi-maps-local-convenience-store",":before{content:\"\\e87d\"}");
			iconmap.put("mdi-maps-local-drink",":before{content:\"\\e87e\"}");
			iconmap.put("mdi-maps-local-florist",":before{content:\"\\e87f\"}");
			iconmap.put("mdi-maps-local-gas-station",":before{content:\"\\e880\"}");
			iconmap.put("mdi-maps-local-grocery-store",":before{content:\"\\e881\"}");
			iconmap.put("mdi-maps-local-hospital",":before{content:\"\\e882\"}");
			iconmap.put("mdi-maps-local-hotel",":before{content:\"\\e883\"}");
			iconmap.put("mdi-maps-local-laundry-service",":before{content:\"\\e884\"}");
			iconmap.put("mdi-maps-local-library",":before{content:\"\\e885\"}");
			iconmap.put("mdi-maps-local-mall",":before{content:\"\\e886\"}");
			iconmap.put("mdi-maps-local-movies",":before{content:\"\\e887\"}");
			iconmap.put("mdi-maps-local-offer",":before{content:\"\\e888\"}");
			iconmap.put("mdi-maps-local-parking",":before{content:\"\\e889\"}");
			iconmap.put("mdi-maps-local-pharmacy",":before{content:\"\\e88a\"}");
			iconmap.put("mdi-maps-local-phone",":before{content:\"\\e88b\"}");
			iconmap.put("mdi-maps-local-pizza",":before{content:\"\\e88c\"}");
			iconmap.put("mdi-maps-local-play",":before{content:\"\\e88d\"}");
			iconmap.put("mdi-maps-local-post-office",":before{content:\"\\e88e\"}");
			iconmap.put("mdi-maps-local-print-shop",":before{content:\"\\e88f\"}");
			iconmap.put("mdi-maps-local-restaurant",":before{content:\"\\e890\"}");
			iconmap.put("mdi-maps-local-see",":before{content:\"\\e891\"}");
			iconmap.put("mdi-maps-local-shipping",":before{content:\"\\e892\"}");
			iconmap.put("mdi-maps-local-taxi",":before{content:\"\\e893\"}");
			iconmap.put("mdi-maps-location-history",":before{content:\"\\e894\"}");
			iconmap.put("mdi-maps-map",":before{content:\"\\e895\"}");
			iconmap.put("mdi-maps-my-location",":before{content:\"\\e896\"}");
			iconmap.put("mdi-maps-navigation",":before{content:\"\\e897\"}");
			iconmap.put("mdi-maps-pin-drop",":before{content:\"\\e898\"}");
			iconmap.put("mdi-maps-place",":before{content:\"\\e899\"}");
			iconmap.put("mdi-maps-rate-review",":before{content:\"\\e89a\"}");
			iconmap.put("mdi-maps-restaurant-menu",":before{content:\"\\e89b\"}");
			iconmap.put("mdi-maps-satellite",":before{content:\"\\e89c\"}");
			iconmap.put("mdi-maps-store-mall-directory",":before{content:\"\\e89d\"}");
			iconmap.put("mdi-maps-terrain",":before{content:\"\\e89e\"}");
			iconmap.put("mdi-maps-traffic",":before{content:\"\\e89f\"}");
			iconmap.put("mdi-navigation-apps",":before{content:\"\\e8a0\"}");
			iconmap.put("mdi-navigation-arrow-back",":before{content:\"\\e8a1\"}");
			iconmap.put("mdi-navigation-arrow-drop-down-circle",":before{content:\"\\e8a2\"}");
			iconmap.put("mdi-navigation-arrow-drop-down",":before{content:\"\\e8a3\"}");
			iconmap.put("mdi-navigation-arrow-drop-up",":before{content:\"\\e8a4\"}");
			iconmap.put("mdi-navigation-arrow-forward",":before{content:\"\\e8a5\"}");
			iconmap.put("mdi-navigation-cancel",":before{content:\"\\e8a6\"}");
			iconmap.put("mdi-navigation-check",":before{content:\"\\e8a7\"}");
			iconmap.put("mdi-navigation-chevron-left",":before{content:\"\\e8a8\"}");
			iconmap.put("mdi-navigation-chevron-right",":before{content:\"\\e8a9\"}");
			iconmap.put("mdi-navigation-close",":before{content:\"\\e8aa\"}");
			iconmap.put("mdi-navigation-expand-less",":before{content:\"\\e8ab\"}");
			iconmap.put("mdi-navigation-expand-more",":before{content:\"\\e8ac\"}");
			iconmap.put("mdi-navigation-fullscreen-exit",":before{content:\"\\e8ad\"}");
			iconmap.put("mdi-navigation-fullscreen",":before{content:\"\\e8ae\"}");
			iconmap.put("mdi-navigation-menu",":before{content:\"\\e8af\"}");
			iconmap.put("mdi-navigation-more-horiz",":before{content:\"\\e8b0\"}");
			iconmap.put("mdi-navigation-more-vert",":before{content:\"\\e8b1\"}");
			iconmap.put("mdi-navigation-refresh",":before{content:\"\\e8b2\"}");
			iconmap.put("mdi-navigation-unfold-less",":before{content:\"\\e8b3\"}");
			iconmap.put("mdi-navigation-unfold-more",":before{content:\"\\e8b4\"}");
			iconmap.put("mdi-notification-adb",":before{content:\"\\e8b5\"}");
			iconmap.put("mdi-notification-bluetooth-audio",":before{content:\"\\e8b6\"}");
			iconmap.put("mdi-notification-disc-full",":before{content:\"\\e8b7\"}");
			iconmap.put("mdi-notification-dnd-forwardslash",":before{content:\"\\e8b8\"}");
			iconmap.put("mdi-notification-do-not-disturb",":before{content:\"\\e8b9\"}");
			iconmap.put("mdi-notification-drive-eta",":before{content:\"\\e8ba\"}");
			iconmap.put("mdi-notification-event-available",":before{content:\"\\e8bb\"}");
			iconmap.put("mdi-notification-event-busy",":before{content:\"\\e8bc\"}");
			iconmap.put("mdi-notification-event-note",":before{content:\"\\e8bd\"}");
			iconmap.put("mdi-notification-folder-special",":before{content:\"\\e8be\"}");
			iconmap.put("mdi-notification-mms",":before{content:\"\\e8bf\"}");
			iconmap.put("mdi-notification-more",":before{content:\"\\e8c0\"}");
			iconmap.put("mdi-notification-network-locked",":before{content:\"\\e8c1\"}");
			iconmap.put("mdi-notification-phone-bluetooth-speaker",":before{content:\"\\e8c2\"}");
			iconmap.put("mdi-notification-phone-forwarded",":before{content:\"\\e8c3\"}");
			iconmap.put("mdi-notification-phone-in-talk",":before{content:\"\\e8c4\"}");
			iconmap.put("mdi-notification-phone-locked",":before{content:\"\\e8c5\"}");
			iconmap.put("mdi-notification-phone-missed",":before{content:\"\\e8c6\"}");
			iconmap.put("mdi-notification-phone-paused",":before{content:\"\\e8c7\"}");
			iconmap.put("mdi-notification-play-download",":before{content:\"\\e8c8\"}");
			iconmap.put("mdi-notification-play-install",":before{content:\"\\e8c9\"}");
			iconmap.put("mdi-notification-sd-card",":before{content:\"\\e8ca\"}");
			iconmap.put("mdi-notification-sim-card-alert",":before{content:\"\\e8cb\"}");
			iconmap.put("mdi-notification-sms-failed",":before{content:\"\\e8cc\"}");
			iconmap.put("mdi-notification-sms",":before{content:\"\\e8cd\"}");
			iconmap.put("mdi-notification-sync-disabled",":before{content:\"\\e8ce\"}");
			iconmap.put("mdi-notification-sync-problem",":before{content:\"\\e8cf\"}");
			iconmap.put("mdi-notification-sync",":before{content:\"\\e8d0\"}");
			iconmap.put("mdi-notification-system-update",":before{content:\"\\e8d1\"}");
			iconmap.put("mdi-notification-tap-and-play",":before{content:\"\\e8d2\"}");
			iconmap.put("mdi-notification-time-to-leave",":before{content:\"\\e8d3\"}");
			iconmap.put("mdi-notification-vibration",":before{content:\"\\e8d4\"}");
			iconmap.put("mdi-notification-voice-chat",":before{content:\"\\e8d5\"}");
			iconmap.put("mdi-notification-vpn-lock",":before{content:\"\\e8d6\"}");
			iconmap.put("mdi-social-cake",":before{content:\"\\e8d7\"}");
			iconmap.put("mdi-social-domain",":before{content:\"\\e8d8\"}");
			iconmap.put("mdi-social-group-add",":before{content:\"\\e8d9\"}");
			iconmap.put("mdi-social-group",":before{content:\"\\e8da\"}");
			iconmap.put("mdi-social-location-city",":before{content:\"\\e8db\"}");
			iconmap.put("mdi-social-mood",":before{content:\"\\e8dc\"}");
			iconmap.put("mdi-social-notifications-none",":before{content:\"\\e8dd\"}");
			iconmap.put("mdi-social-notifications-off",":before{content:\"\\e8de\"}");
			iconmap.put("mdi-social-notifications-on",":before{content:\"\\e8df\"}");
			iconmap.put("mdi-social-notifications-paused",":before{content:\"\\e8e0\"}");
			iconmap.put("mdi-social-notifications",":before{content:\"\\e8e1\"}");
			iconmap.put("mdi-social-pages",":before{content:\"\\e8e2\"}");
			iconmap.put("mdi-social-party-mode",":before{content:\"\\e8e3\"}");
			iconmap.put("mdi-social-people-outline",":before{content:\"\\e8e4\"}");
			iconmap.put("mdi-social-people",":before{content:\"\\e8e5\"}");
			iconmap.put("mdi-social-person-add",":before{content:\"\\e8e6\"}");
			iconmap.put("mdi-social-person-outline",":before{content:\"\\e8e7\"}");
			iconmap.put("mdi-social-person",":before{content:\"\\e8e8\"}");
			iconmap.put("mdi-social-plus-one",":before{content:\"\\e8e9\"}");
			iconmap.put("mdi-social-poll",":before{content:\"\\e8ea\"}");
			iconmap.put("mdi-social-public",":before{content:\"\\e8eb\"}");
			iconmap.put("mdi-social-school",":before{content:\"\\e8ec\"}");
			iconmap.put("mdi-social-share",":before{content:\"\\e8ed\"}");
			iconmap.put("mdi-social-whatshot",":before{content:\"\\e8ee\"}");
			iconmap.put("mdi-toggle-check-box-outline-blank",":before{content:\"\\e8ef\"}");
			iconmap.put("mdi-toggle-check-box",":before{content:\"\\e8f0\"}");
			iconmap.put("mdi-toggle-radio-button-off",":before{content:\"\\e8f1\"}");
			iconmap.put("mdi-toggle-radio-button-on",":before{content:\"\\e8f2\"}");
			iconmap.put("mdi-toggle-star-half",":before{content:\"\\e8f3\"}");
			iconmap.put("mdi-toggle-star-outline",":before{content:\"\\e8f4\"}");
			iconmap.put("mdi-toggle-star",":before{content:\"\\e8f5\"}");
			iconmap.put("fa-glass",":before{content:\"\\f000\"}");
			iconmap.put("fa-music",":before{content:\"\\f001\"}");
			iconmap.put("fa-search",":before{content:\"\\f002\"}");
			iconmap.put("fa-envelope-o",":before{content:\"\\f003\"}");
			iconmap.put("fa-heart",":before{content:\"\\f004\"}");
			iconmap.put("fa-star",":before{content:\"\\f005\"}");
			iconmap.put("fa-star-o",":before{content:\"\\f006\"}");
			iconmap.put("fa-user",":before{content:\"\\f007\"}");
			iconmap.put("fa-film",":before{content:\"\\f008\"}");
			iconmap.put("fa-th-large",":before{content:\"\\f009\"}");
			iconmap.put("fa-th",":before{content:\"\\f00a\"}");
			iconmap.put("fa-th-list",":before{content:\"\\f00b\"}");
			iconmap.put("fa-check",":before{content:\"\\f00c\"}");
			iconmap.put("fa-remove",":before{content:\"\\f00d\"}");
			iconmap.put("fa-close",":before{content:\"\\f00d\"}");
			iconmap.put("fa-times",":before{content:\"\\f00d\"}");
			iconmap.put("fa-search-plus",":before{content:\"\\f00e\"}");
			iconmap.put("fa-search-minus",":before{content:\"\\f010\"}");
			iconmap.put("fa-power-off",":before{content:\"\\f011\"}");
			iconmap.put("fa-signal",":before{content:\"\\f012\"}");
			iconmap.put("fa-gear",":before{content:\"\\f013\"}");
			iconmap.put("fa-cog",":before{content:\"\\f013\"}");
			iconmap.put("fa-trash-o",":before{content:\"\\f014\"}");
			iconmap.put("fa-home",":before{content:\"\\f015\"}");
			iconmap.put("fa-file-o",":before{content:\"\\f016\"}");
			iconmap.put("fa-clock-o",":before{content:\"\\f017\"}");
			iconmap.put("fa-road",":before{content:\"\\f018\"}");
			iconmap.put("fa-download",":before{content:\"\\f019\"}");
			iconmap.put("fa-arrow-circle-o-down",":before{content:\"\\f01a\"}");
			iconmap.put("fa-arrow-circle-o-up",":before{content:\"\\f01b\"}");
			iconmap.put("fa-inbox",":before{content:\"\\f01c\"}");
			iconmap.put("fa-play-circle-o",":before{content:\"\\f01d\"}");
			iconmap.put("fa-rotate-right",":before{content:\"\\f01e\"}");
			iconmap.put("fa-repeat",":before{content:\"\\f01e\"}");
			iconmap.put("fa-refresh",":before{content:\"\\f021\"}");
			iconmap.put("fa-list-alt",":before{content:\"\\f022\"}");
			iconmap.put("fa-lock",":before{content:\"\\f023\"}");
			iconmap.put("fa-flag",":before{content:\"\\f024\"}");
			iconmap.put("fa-headphones",":before{content:\"\\f025\"}");
			iconmap.put("fa-volume-off",":before{content:\"\\f026\"}");
			iconmap.put("fa-volume-down",":before{content:\"\\f027\"}");
			iconmap.put("fa-volume-up",":before{content:\"\\f028\"}");
			iconmap.put("fa-qrcode",":before{content:\"\\f029\"}");
			iconmap.put("fa-barcode",":before{content:\"\\f02a\"}");
			iconmap.put("fa-tag",":before{content:\"\\f02b\"}");
			iconmap.put("fa-tags",":before{content:\"\\f02c\"}");
			iconmap.put("fa-book",":before{content:\"\\f02d\"}");
			iconmap.put("fa-bookmark",":before{content:\"\\f02e\"}");
			iconmap.put("fa-print",":before{content:\"\\f02f\"}");
			iconmap.put("fa-camera",":before{content:\"\\f030\"}");
			iconmap.put("fa-font",":before{content:\"\\f031\"}");
			iconmap.put("fa-bold",":before{content:\"\\f032\"}");
			iconmap.put("fa-italic",":before{content:\"\\f033\"}");
			iconmap.put("fa-text-height",":before{content:\"\\f034\"}");
			iconmap.put("fa-text-width",":before{content:\"\\f035\"}");
			iconmap.put("fa-align-left",":before{content:\"\\f036\"}");
			iconmap.put("fa-align-center",":before{content:\"\\f037\"}");
			iconmap.put("fa-align-right",":before{content:\"\\f038\"}");
			iconmap.put("fa-align-justify",":before{content:\"\\f039\"}");
			iconmap.put("fa-list",":before{content:\"\\f03a\"}");
			iconmap.put("fa-dedent",":before{content:\"\\f03b\"}");
			iconmap.put("fa-outdent",":before{content:\"\\f03b\"}");
			iconmap.put("fa-indent",":before{content:\"\\f03c\"}");
			iconmap.put("fa-video-camera",":before{content:\"\\f03d\"}");
			iconmap.put("fa-photo",":before{content:\"\\f03e\"}");
			iconmap.put("fa-image",":before{content:\"\\f03e\"}");
			iconmap.put("fa-picture-o",":before{content:\"\\f03e\"}");
			iconmap.put("fa-pencil",":before{content:\"\\f040\"}");
			iconmap.put("fa-map-marker",":before{content:\"\\f041\"}");
			iconmap.put("fa-adjust",":before{content:\"\\f042\"}");
			iconmap.put("fa-tint",":before{content:\"\\f043\"}");
			iconmap.put("fa-edit",":before{content:\"\\f044\"}");
			iconmap.put("fa-pencil-square-o",":before{content:\"\\f044\"}");
			iconmap.put("fa-share-square-o",":before{content:\"\\f045\"}");
			iconmap.put("fa-check-square-o",":before{content:\"\\f046\"}");
			iconmap.put("fa-arrows",":before{content:\"\\f047\"}");
			iconmap.put("fa-step-backward",":before{content:\"\\f048\"}");
			iconmap.put("fa-fast-backward",":before{content:\"\\f049\"}");
			iconmap.put("fa-backward",":before{content:\"\\f04a\"}");
			iconmap.put("fa-play",":before{content:\"\\f04b\"}");
			iconmap.put("fa-pause",":before{content:\"\\f04c\"}");
			iconmap.put("fa-stop",":before{content:\"\\f04d\"}");
			iconmap.put("fa-forward",":before{content:\"\\f04e\"}");
			iconmap.put("fa-fast-forward",":before{content:\"\\f050\"}");
			iconmap.put("fa-step-forward",":before{content:\"\\f051\"}");
			iconmap.put("fa-eject",":before{content:\"\\f052\"}");
			iconmap.put("fa-chevron-left",":before{content:\"\\f053\"}");
			iconmap.put("fa-chevron-right",":before{content:\"\\f054\"}");
			iconmap.put("fa-plus-circle",":before{content:\"\\f055\"}");
			iconmap.put("fa-minus-circle",":before{content:\"\\f056\"}");
			iconmap.put("fa-times-circle",":before{content:\"\\f057\"}");
			iconmap.put("fa-check-circle",":before{content:\"\\f058\"}");
			iconmap.put("fa-question-circle",":before{content:\"\\f059\"}");
			iconmap.put("fa-info-circle",":before{content:\"\\f05a\"}");
			iconmap.put("fa-crosshairs",":before{content:\"\\f05b\"}");
			iconmap.put("fa-times-circle-o",":before{content:\"\\f05c\"}");
			iconmap.put("fa-check-circle-o",":before{content:\"\\f05d\"}");
			iconmap.put("fa-ban",":before{content:\"\\f05e\"}");
			iconmap.put("fa-arrow-left",":before{content:\"\\f060\"}");
			iconmap.put("fa-arrow-right",":before{content:\"\\f061\"}");
			iconmap.put("fa-arrow-up",":before{content:\"\\f062\"}");
			iconmap.put("fa-arrow-down",":before{content:\"\\f063\"}");
			iconmap.put("fa-mail-forward",":before{content:\"\\f064\"}");
			iconmap.put("fa-share",":before{content:\"\\f064\"}");
			iconmap.put("fa-expand",":before{content:\"\\f065\"}");
			iconmap.put("fa-compress",":before{content:\"\\f066\"}");
			iconmap.put("fa-plus",":before{content:\"\\f067\"}");
			iconmap.put("fa-minus",":before{content:\"\\f068\"}");
			iconmap.put("fa-asterisk",":before{content:\"\\f069\"}");
			iconmap.put("fa-exclamation-circle",":before{content:\"\\f06a\"}");
			iconmap.put("fa-gift",":before{content:\"\\f06b\"}");
			iconmap.put("fa-leaf",":before{content:\"\\f06c\"}");
			iconmap.put("fa-fire",":before{content:\"\\f06d\"}");
			iconmap.put("fa-eye",":before{content:\"\\f06e\"}");
			iconmap.put("fa-eye-slash",":before{content:\"\\f070\"}");
			iconmap.put("fa-warning",":before{content:\"\\f071\"}");
			iconmap.put("fa-exclamation-triangle",":before{content:\"\\f071\"}");
			iconmap.put("fa-plane",":before{content:\"\\f072\"}");
			iconmap.put("fa-calendar",":before{content:\"\\f073\"}");
			iconmap.put("fa-random",":before{content:\"\\f074\"}");
			iconmap.put("fa-comment",":before{content:\"\\f075\"}");
			iconmap.put("fa-magnet",":before{content:\"\\f076\"}");
			iconmap.put("fa-chevron-up",":before{content:\"\\f077\"}");
			iconmap.put("fa-chevron-down",":before{content:\"\\f078\"}");
			iconmap.put("fa-retweet",":before{content:\"\\f079\"}");
			iconmap.put("fa-shopping-cart",":before{content:\"\\f07a\"}");
			iconmap.put("fa-folder",":before{content:\"\\f07b\"}");
			iconmap.put("fa-folder-open",":before{content:\"\\f07c\"}");
			iconmap.put("fa-arrows-v",":before{content:\"\\f07d\"}");
			iconmap.put("fa-arrows-h",":before{content:\"\\f07e\"}");
			iconmap.put("fa-bar-chart-o",":before{content:\"\\f080\"}");
			iconmap.put("fa-bar-chart",":before{content:\"\\f080\"}");
			iconmap.put("fa-twitter-square",":before{content:\"\\f081\"}");
			iconmap.put("fa-facebook-square",":before{content:\"\\f082\"}");
			iconmap.put("fa-camera-retro",":before{content:\"\\f083\"}");
			iconmap.put("fa-key",":before{content:\"\\f084\"}");
			iconmap.put("fa-gears",":before{content:\"\\f085\"}");
			iconmap.put("fa-cogs",":before{content:\"\\f085\"}");
			iconmap.put("fa-comments",":before{content:\"\\f086\"}");
			iconmap.put("fa-thumbs-o-up",":before{content:\"\\f087\"}");
			iconmap.put("fa-thumbs-o-down",":before{content:\"\\f088\"}");
			iconmap.put("fa-star-half",":before{content:\"\\f089\"}");
			iconmap.put("fa-heart-o",":before{content:\"\\f08a\"}");
			iconmap.put("fa-sign-out",":before{content:\"\\f08b\"}");
			iconmap.put("fa-linkedin-square",":before{content:\"\\f08c\"}");
			iconmap.put("fa-thumb-tack",":before{content:\"\\f08d\"}");
			iconmap.put("fa-external-link",":before{content:\"\\f08e\"}");
			iconmap.put("fa-sign-in",":before{content:\"\\f090\"}");
			iconmap.put("fa-trophy",":before{content:\"\\f091\"}");
			iconmap.put("fa-github-square",":before{content:\"\\f092\"}");
			iconmap.put("fa-upload",":before{content:\"\\f093\"}");
			iconmap.put("fa-lemon-o",":before{content:\"\\f094\"}");
			iconmap.put("fa-phone",":before{content:\"\\f095\"}");
			iconmap.put("fa-square-o",":before{content:\"\\f096\"}");
			iconmap.put("fa-bookmark-o",":before{content:\"\\f097\"}");
			iconmap.put("fa-phone-square",":before{content:\"\\f098\"}");
			iconmap.put("fa-twitter",":before{content:\"\\f099\"}");
			iconmap.put("fa-facebook-f",":before{content:\"\\f09a\"}");
			iconmap.put("fa-facebook",":before{content:\"\\f09a\"}");
			iconmap.put("fa-github",":before{content:\"\\f09b\"}");
			iconmap.put("fa-unlock",":before{content:\"\\f09c\"}");
			iconmap.put("fa-credit-card",":before{content:\"\\f09d\"}");
			iconmap.put("fa-feed",":before{content:\"\\f09e\"}");
			iconmap.put("fa-rss",":before{content:\"\\f09e\"}");
			iconmap.put("fa-hdd-o",":before{content:\"\\f0a0\"}");
			iconmap.put("fa-bullhorn",":before{content:\"\\f0a1\"}");
			iconmap.put("fa-bell",":before{content:\"\\f0f3\"}");
			iconmap.put("fa-certificate",":before{content:\"\\f0a3\"}");
			iconmap.put("fa-hand-o-right",":before{content:\"\\f0a4\"}");
			iconmap.put("fa-hand-o-left",":before{content:\"\\f0a5\"}");
			iconmap.put("fa-hand-o-up",":before{content:\"\\f0a6\"}");
			iconmap.put("fa-hand-o-down",":before{content:\"\\f0a7\"}");
			iconmap.put("fa-arrow-circle-left",":before{content:\"\\f0a8\"}");
			iconmap.put("fa-arrow-circle-right",":before{content:\"\\f0a9\"}");
			iconmap.put("fa-arrow-circle-up",":before{content:\"\\f0aa\"}");
			iconmap.put("fa-arrow-circle-down",":before{content:\"\\f0ab\"}");
			iconmap.put("fa-globe",":before{content:\"\\f0ac\"}");
			iconmap.put("fa-wrench",":before{content:\"\\f0ad\"}");
			iconmap.put("fa-tasks",":before{content:\"\\f0ae\"}");
			iconmap.put("fa-filter",":before{content:\"\\f0b0\"}");
			iconmap.put("fa-briefcase",":before{content:\"\\f0b1\"}");
			iconmap.put("fa-arrows-alt",":before{content:\"\\f0b2\"}");
			iconmap.put("fa-group",":before{content:\"\\f0c0\"}");
			iconmap.put("fa-users",":before{content:\"\\f0c0\"}");
			iconmap.put("fa-chain",":before{content:\"\\f0c1\"}");
			iconmap.put("fa-link",":before{content:\"\\f0c1\"}");
			iconmap.put("fa-cloud",":before{content:\"\\f0c2\"}");
			iconmap.put("fa-flask",":before{content:\"\\f0c3\"}");
			iconmap.put("fa-cut",":before{content:\"\\f0c4\"}");
			iconmap.put("fa-scissors",":before{content:\"\\f0c4\"}");
			iconmap.put("fa-copy",":before{content:\"\\f0c5\"}");
			iconmap.put("fa-files-o",":before{content:\"\\f0c5\"}");
			iconmap.put("fa-paperclip",":before{content:\"\\f0c6\"}");
			iconmap.put("fa-save",":before{content:\"\\f0c7\"}");
			iconmap.put("fa-floppy-o",":before{content:\"\\f0c7\"}");
			iconmap.put("fa-square",":before{content:\"\\f0c8\"}");
			iconmap.put("fa-navicon",":before{content:\"\\f0c9\"}");
			iconmap.put("fa-reorder",":before{content:\"\\f0c9\"}");
			iconmap.put("fa-bars",":before{content:\"\\f0c9\"}");
			iconmap.put("fa-list-ul",":before{content:\"\\f0ca\"}");
			iconmap.put("fa-list-ol",":before{content:\"\\f0cb\"}");
			iconmap.put("fa-strikethrough",":before{content:\"\\f0cc\"}");
			iconmap.put("fa-underline",":before{content:\"\\f0cd\"}");
			iconmap.put("fa-table",":before{content:\"\\f0ce\"}");
			iconmap.put("fa-magic",":before{content:\"\\f0d0\"}");
			iconmap.put("fa-truck",":before{content:\"\\f0d1\"}");
			iconmap.put("fa-pinterest",":before{content:\"\\f0d2\"}");
			iconmap.put("fa-pinterest-square",":before{content:\"\\f0d3\"}");
			iconmap.put("fa-google-plus-square",":before{content:\"\\f0d4\"}");
			iconmap.put("fa-google-plus",":before{content:\"\\f0d5\"}");
			iconmap.put("fa-money",":before{content:\"\\f0d6\"}");
			iconmap.put("fa-caret-down",":before{content:\"\\f0d7\"}");
			iconmap.put("fa-caret-up",":before{content:\"\\f0d8\"}");
			iconmap.put("fa-caret-left",":before{content:\"\\f0d9\"}");
			iconmap.put("fa-caret-right",":before{content:\"\\f0da\"}");
			iconmap.put("fa-columns",":before{content:\"\\f0db\"}");
			iconmap.put("fa-unsorted",":before{content:\"\\f0dc\"}");
			iconmap.put("fa-sort",":before{content:\"\\f0dc\"}");
			iconmap.put("fa-sort-down",":before{content:\"\\f0dd\"}");
			iconmap.put("fa-sort-desc",":before{content:\"\\f0dd\"}");
			iconmap.put("fa-sort-up",":before{content:\"\\f0de\"}");
			iconmap.put("fa-sort-asc",":before{content:\"\\f0de\"}");
			iconmap.put("fa-envelope",":before{content:\"\\f0e0\"}");
			iconmap.put("fa-linkedin",":before{content:\"\\f0e1\"}");
			iconmap.put("fa-rotate-left",":before{content:\"\\f0e2\"}");
			iconmap.put("fa-undo",":before{content:\"\\f0e2\"}");
			iconmap.put("fa-legal",":before{content:\"\\f0e3\"}");
			iconmap.put("fa-gavel",":before{content:\"\\f0e3\"}");
			iconmap.put("fa-dashboard",":before{content:\"\\f0e4\"}");
			iconmap.put("fa-tachometer",":before{content:\"\\f0e4\"}");
			iconmap.put("fa-comment-o",":before{content:\"\\f0e5\"}");
			iconmap.put("fa-comments-o",":before{content:\"\\f0e6\"}");
			iconmap.put("fa-flash",":before{content:\"\\f0e7\"}");
			iconmap.put("fa-bolt",":before{content:\"\\f0e7\"}");
			iconmap.put("fa-sitemap",":before{content:\"\\f0e8\"}");
			iconmap.put("fa-umbrella",":before{content:\"\\f0e9\"}");
			iconmap.put("fa-paste",":before{content:\"\\f0ea\"}");
			iconmap.put("fa-clipboard",":before{content:\"\\f0ea\"}");
			iconmap.put("fa-lightbulb-o",":before{content:\"\\f0eb\"}");
			iconmap.put("fa-exchange",":before{content:\"\\f0ec\"}");
			iconmap.put("fa-cloud-download",":before{content:\"\\f0ed\"}");
			iconmap.put("fa-cloud-upload",":before{content:\"\\f0ee\"}");
			iconmap.put("fa-user-md",":before{content:\"\\f0f0\"}");
			iconmap.put("fa-stethoscope",":before{content:\"\\f0f1\"}");
			iconmap.put("fa-suitcase",":before{content:\"\\f0f2\"}");
			iconmap.put("fa-bell-o",":before{content:\"\\f0a2\"}");
			iconmap.put("fa-coffee",":before{content:\"\\f0f4\"}");
			iconmap.put("fa-cutlery",":before{content:\"\\f0f5\"}");
			iconmap.put("fa-file-text-o",":before{content:\"\\f0f6\"}");
			iconmap.put("fa-building-o",":before{content:\"\\f0f7\"}");
			iconmap.put("fa-hospital-o",":before{content:\"\\f0f8\"}");
			iconmap.put("fa-ambulance",":before{content:\"\\f0f9\"}");
			iconmap.put("fa-medkit",":before{content:\"\\f0fa\"}");
			iconmap.put("fa-fighter-jet",":before{content:\"\\f0fb\"}");
			iconmap.put("fa-beer",":before{content:\"\\f0fc\"}");
			iconmap.put("fa-h-square",":before{content:\"\\f0fd\"}");
			iconmap.put("fa-plus-square",":before{content:\"\\f0fe\"}");
			iconmap.put("fa-angle-double-left",":before{content:\"\\f100\"}");
			iconmap.put("fa-angle-double-right",":before{content:\"\\f101\"}");
			iconmap.put("fa-angle-double-up",":before{content:\"\\f102\"}");
			iconmap.put("fa-angle-double-down",":before{content:\"\\f103\"}");
			iconmap.put("fa-angle-left",":before{content:\"\\f104\"}");
			iconmap.put("fa-angle-right",":before{content:\"\\f105\"}");
			iconmap.put("fa-angle-up",":before{content:\"\\f106\"}");
			iconmap.put("fa-angle-down",":before{content:\"\\f107\"}");
			iconmap.put("fa-desktop",":before{content:\"\\f108\"}");
			iconmap.put("fa-laptop",":before{content:\"\\f109\"}");
			iconmap.put("fa-tablet",":before{content:\"\\f10a\"}");
			iconmap.put("fa-mobile-phone",":before{content:\"\\f10b\"}");
			iconmap.put("fa-mobile",":before{content:\"\\f10b\"}");
			iconmap.put("fa-circle-o",":before{content:\"\\f10c\"}");
			iconmap.put("fa-quote-left",":before{content:\"\\f10d\"}");
			iconmap.put("fa-quote-right",":before{content:\"\\f10e\"}");
			iconmap.put("fa-spinner",":before{content:\"\\f110\"}");
			iconmap.put("fa-circle",":before{content:\"\\f111\"}");
			iconmap.put("fa-mail-reply",":before{content:\"\\f112\"}");
			iconmap.put("fa-reply",":before{content:\"\\f112\"}");
			iconmap.put("fa-github-alt",":before{content:\"\\f113\"}");
			iconmap.put("fa-folder-o",":before{content:\"\\f114\"}");
			iconmap.put("fa-folder-open-o",":before{content:\"\\f115\"}");
			iconmap.put("fa-smile-o",":before{content:\"\\f118\"}");
			iconmap.put("fa-frown-o",":before{content:\"\\f119\"}");
			iconmap.put("fa-meh-o",":before{content:\"\\f11a\"}");
			iconmap.put("fa-gamepad",":before{content:\"\\f11b\"}");
			iconmap.put("fa-keyboard-o",":before{content:\"\\f11c\"}");
			iconmap.put("fa-flag-o",":before{content:\"\\f11d\"}");
			iconmap.put("fa-flag-checkered",":before{content:\"\\f11e\"}");
			iconmap.put("fa-terminal",":before{content:\"\\f120\"}");
			iconmap.put("fa-code",":before{content:\"\\f121\"}");
			iconmap.put("fa-mail-reply-all",":before{content:\"\\f122\"}");
			iconmap.put("fa-reply-all",":before{content:\"\\f122\"}");
			iconmap.put("fa-star-half-empty",":before{content:\"\\f123\"}");
			iconmap.put("fa-star-half-full",":before{content:\"\\f123\"}");
			iconmap.put("fa-star-half-o",":before{content:\"\\f123\"}");
			iconmap.put("fa-location-arrow",":before{content:\"\\f124\"}");
			iconmap.put("fa-crop",":before{content:\"\\f125\"}");
			iconmap.put("fa-code-fork",":before{content:\"\\f126\"}");
			iconmap.put("fa-unlink",":before{content:\"\\f127\"}");
			iconmap.put("fa-chain-broken",":before{content:\"\\f127\"}");
			iconmap.put("fa-question",":before{content:\"\\f128\"}");
			iconmap.put("fa-info",":before{content:\"\\f129\"}");
			iconmap.put("fa-exclamation",":before{content:\"\\f12a\"}");
			iconmap.put("fa-superscript",":before{content:\"\\f12b\"}");
			iconmap.put("fa-subscript",":before{content:\"\\f12c\"}");
			iconmap.put("fa-eraser",":before{content:\"\\f12d\"}");
			iconmap.put("fa-puzzle-piece",":before{content:\"\\f12e\"}");
			iconmap.put("fa-microphone",":before{content:\"\\f130\"}");
			iconmap.put("fa-microphone-slash",":before{content:\"\\f131\"}");
			iconmap.put("fa-shield",":before{content:\"\\f132\"}");
			iconmap.put("fa-calendar-o",":before{content:\"\\f133\"}");
			iconmap.put("fa-fire-extinguisher",":before{content:\"\\f134\"}");
			iconmap.put("fa-rocket",":before{content:\"\\f135\"}");
			iconmap.put("fa-maxcdn",":before{content:\"\\f136\"}");
			iconmap.put("fa-chevron-circle-left",":before{content:\"\\f137\"}");
			iconmap.put("fa-chevron-circle-right",":before{content:\"\\f138\"}");
			iconmap.put("fa-chevron-circle-up",":before{content:\"\\f139\"}");
			iconmap.put("fa-chevron-circle-down",":before{content:\"\\f13a\"}");
			iconmap.put("fa-html5",":before{content:\"\\f13b\"}");
			iconmap.put("fa-css3",":before{content:\"\\f13c\"}");
			iconmap.put("fa-anchor",":before{content:\"\\f13d\"}");
			iconmap.put("fa-unlock-alt",":before{content:\"\\f13e\"}");
			iconmap.put("fa-bullseye",":before{content:\"\\f140\"}");
			iconmap.put("fa-ellipsis-h",":before{content:\"\\f141\"}");
			iconmap.put("fa-ellipsis-v",":before{content:\"\\f142\"}");
			iconmap.put("fa-rss-square",":before{content:\"\\f143\"}");
			iconmap.put("fa-play-circle",":before{content:\"\\f144\"}");
			iconmap.put("fa-ticket",":before{content:\"\\f145\"}");
			iconmap.put("fa-minus-square",":before{content:\"\\f146\"}");
			iconmap.put("fa-minus-square-o",":before{content:\"\\f147\"}");
			iconmap.put("fa-level-up",":before{content:\"\\f148\"}");
			iconmap.put("fa-level-down",":before{content:\"\\f149\"}");
			iconmap.put("fa-check-square",":before{content:\"\\f14a\"}");
			iconmap.put("fa-pencil-square",":before{content:\"\\f14b\"}");
			iconmap.put("fa-external-link-square",":before{content:\"\\f14c\"}");
			iconmap.put("fa-share-square",":before{content:\"\\f14d\"}");
			iconmap.put("fa-compass",":before{content:\"\\f14e\"}");
			iconmap.put("fa-toggle-down",":before{content:\"\\f150\"}");
			iconmap.put("fa-caret-square-o-down",":before{content:\"\\f150\"}");
			iconmap.put("fa-toggle-up",":before{content:\"\\f151\"}");
			iconmap.put("fa-caret-square-o-up",":before{content:\"\\f151\"}");
			iconmap.put("fa-toggle-right",":before{content:\"\\f152\"}");
			iconmap.put("fa-caret-square-o-right",":before{content:\"\\f152\"}");
			iconmap.put("fa-euro",":before{content:\"\\f153\"}");
			iconmap.put("fa-eur",":before{content:\"\\f153\"}");
			iconmap.put("fa-gbp",":before{content:\"\\f154\"}");
			iconmap.put("fa-dollar",":before{content:\"\\f155\"}");
			iconmap.put("fa-usd",":before{content:\"\\f155\"}");
			iconmap.put("fa-rupee",":before{content:\"\\f156\"}");
			iconmap.put("fa-inr",":before{content:\"\\f156\"}");
			iconmap.put("fa-cny",":before{content:\"\\f157\"}");
			iconmap.put("fa-rmb",":before{content:\"\\f157\"}");
			iconmap.put("fa-yen",":before{content:\"\\f157\"}");
			iconmap.put("fa-jpy",":before{content:\"\\f157\"}");
			iconmap.put("fa-ruble",":before{content:\"\\f158\"}");
			iconmap.put("fa-rouble",":before{content:\"\\f158\"}");
			iconmap.put("fa-rub",":before{content:\"\\f158\"}");
			iconmap.put("fa-won",":before{content:\"\\f159\"}");
			iconmap.put("fa-krw",":before{content:\"\\f159\"}");
			iconmap.put("fa-bitcoin",":before{content:\"\\f15a\"}");
			iconmap.put("fa-btc",":before{content:\"\\f15a\"}");
			iconmap.put("fa-file",":before{content:\"\\f15b\"}");
			iconmap.put("fa-file-text",":before{content:\"\\f15c\"}");
			iconmap.put("fa-sort-alpha-asc",":before{content:\"\\f15d\"}");
			iconmap.put("fa-sort-alpha-desc",":before{content:\"\\f15e\"}");
			iconmap.put("fa-sort-amount-asc",":before{content:\"\\f160\"}");
			iconmap.put("fa-sort-amount-desc",":before{content:\"\\f161\"}");
			iconmap.put("fa-sort-numeric-asc",":before{content:\"\\f162\"}");
			iconmap.put("fa-sort-numeric-desc",":before{content:\"\\f163\"}");
			iconmap.put("fa-thumbs-up",":before{content:\"\\f164\"}");
			iconmap.put("fa-thumbs-down",":before{content:\"\\f165\"}");
			iconmap.put("fa-youtube-square",":before{content:\"\\f166\"}");
			iconmap.put("fa-youtube",":before{content:\"\\f167\"}");
			iconmap.put("fa-xing",":before{content:\"\\f168\"}");
			iconmap.put("fa-xing-square",":before{content:\"\\f169\"}");
			iconmap.put("fa-youtube-play",":before{content:\"\\f16a\"}");
			iconmap.put("fa-dropbox",":before{content:\"\\f16b\"}");
			iconmap.put("fa-stack-overflow",":before{content:\"\\f16c\"}");
			iconmap.put("fa-instagram",":before{content:\"\\f16d\"}");
			iconmap.put("fa-flickr",":before{content:\"\\f16e\"}");
			iconmap.put("fa-adn",":before{content:\"\\f170\"}");
			iconmap.put("fa-bitbucket",":before{content:\"\\f171\"}");
			iconmap.put("fa-bitbucket-square",":before{content:\"\\f172\"}");
			iconmap.put("fa-tumblr",":before{content:\"\\f173\"}");
			iconmap.put("fa-tumblr-square",":before{content:\"\\f174\"}");
			iconmap.put("fa-long-arrow-down",":before{content:\"\\f175\"}");
			iconmap.put("fa-long-arrow-up",":before{content:\"\\f176\"}");
			iconmap.put("fa-long-arrow-left",":before{content:\"\\f177\"}");
			iconmap.put("fa-long-arrow-right",":before{content:\"\\f178\"}");
			iconmap.put("fa-apple",":before{content:\"\\f179\"}");
			iconmap.put("fa-windows",":before{content:\"\\f17a\"}");
			iconmap.put("fa-android",":before{content:\"\\f17b\"}");
			iconmap.put("fa-linux",":before{content:\"\\f17c\"}");
			iconmap.put("fa-dribbble",":before{content:\"\\f17d\"}");
			iconmap.put("fa-skype",":before{content:\"\\f17e\"}");
			iconmap.put("fa-foursquare",":before{content:\"\\f180\"}");
			iconmap.put("fa-trello",":before{content:\"\\f181\"}");
			iconmap.put("fa-female",":before{content:\"\\f182\"}");
			iconmap.put("fa-male",":before{content:\"\\f183\"}");
			iconmap.put("fa-gittip",":before{content:\"\\f184\"}");
			iconmap.put("fa-gratipay",":before{content:\"\\f184\"}");
			iconmap.put("fa-sun-o",":before{content:\"\\f185\"}");
			iconmap.put("fa-moon-o",":before{content:\"\\f186\"}");
			iconmap.put("fa-archive",":before{content:\"\\f187\"}");
			iconmap.put("fa-bug",":before{content:\"\\f188\"}");
			iconmap.put("fa-vk",":before{content:\"\\f189\"}");
			iconmap.put("fa-weibo",":before{content:\"\\f18a\"}");
			iconmap.put("fa-renren",":before{content:\"\\f18b\"}");
			iconmap.put("fa-pagelines",":before{content:\"\\f18c\"}");
			iconmap.put("fa-stack-exchange",":before{content:\"\\f18d\"}");
			iconmap.put("fa-arrow-circle-o-right",":before{content:\"\\f18e\"}");
			iconmap.put("fa-arrow-circle-o-left",":before{content:\"\\f190\"}");
			iconmap.put("fa-toggle-left",":before{content:\"\\f191\"}");
			iconmap.put("fa-caret-square-o-left",":before{content:\"\\f191\"}");
			iconmap.put("fa-dot-circle-o",":before{content:\"\\f192\"}");
			iconmap.put("fa-wheelchair",":before{content:\"\\f193\"}");
			iconmap.put("fa-vimeo-square",":before{content:\"\\f194\"}");
			iconmap.put("fa-turkish-lira",":before{content:\"\\f195\"}");
			iconmap.put("fa-try",":before{content:\"\\f195\"}");
			iconmap.put("fa-plus-square-o",":before{content:\"\\f196\"}");
			iconmap.put("fa-space-shuttle",":before{content:\"\\f197\"}");
			iconmap.put("fa-slack",":before{content:\"\\f198\"}");
			iconmap.put("fa-envelope-square",":before{content:\"\\f199\"}");
			iconmap.put("fa-wordpress",":before{content:\"\\f19a\"}");
			iconmap.put("fa-openid",":before{content:\"\\f19b\"}");
			iconmap.put("fa-institution",":before{content:\"\\f19c\"}");
			iconmap.put("fa-bank",":before{content:\"\\f19c\"}");
			iconmap.put("fa-university",":before{content:\"\\f19c\"}");
			iconmap.put("fa-mortar-board",":before{content:\"\\f19d\"}");
			iconmap.put("fa-graduation-cap",":before{content:\"\\f19d\"}");
			iconmap.put("fa-yahoo",":before{content:\"\\f19e\"}");
			iconmap.put("fa-google",":before{content:\"\\f1a0\"}");
			iconmap.put("fa-reddit",":before{content:\"\\f1a1\"}");
			iconmap.put("fa-reddit-square",":before{content:\"\\f1a2\"}");
			iconmap.put("fa-stumbleupon-circle",":before{content:\"\\f1a3\"}");
			iconmap.put("fa-stumbleupon",":before{content:\"\\f1a4\"}");
			iconmap.put("fa-delicious",":before{content:\"\\f1a5\"}");
			iconmap.put("fa-digg",":before{content:\"\\f1a6\"}");
			iconmap.put("fa-pied-piper-pp",":before{content:\"\\f1a7\"}");
			iconmap.put("fa-pied-piper-alt",":before{content:\"\\f1a8\"}");
			iconmap.put("fa-drupal",":before{content:\"\\f1a9\"}");
			iconmap.put("fa-joomla",":before{content:\"\\f1aa\"}");
			iconmap.put("fa-language",":before{content:\"\\f1ab\"}");
			iconmap.put("fa-fax",":before{content:\"\\f1ac\"}");
			iconmap.put("fa-building",":before{content:\"\\f1ad\"}");
			iconmap.put("fa-child",":before{content:\"\\f1ae\"}");
			iconmap.put("fa-paw",":before{content:\"\\f1b0\"}");
			iconmap.put("fa-spoon",":before{content:\"\\f1b1\"}");
			iconmap.put("fa-cube",":before{content:\"\\f1b2\"}");
			iconmap.put("fa-cubes",":before{content:\"\\f1b3\"}");
			iconmap.put("fa-behance",":before{content:\"\\f1b4\"}");
			iconmap.put("fa-behance-square",":before{content:\"\\f1b5\"}");
			iconmap.put("fa-steam",":before{content:\"\\f1b6\"}");
			iconmap.put("fa-steam-square",":before{content:\"\\f1b7\"}");
			iconmap.put("fa-recycle",":before{content:\"\\f1b8\"}");
			iconmap.put("fa-automobile",":before{content:\"\\f1b9\"}");
			iconmap.put("fa-car",":before{content:\"\\f1b9\"}");
			iconmap.put("fa-cab",":before{content:\"\\f1ba\"}");
			iconmap.put("fa-taxi",":before{content:\"\\f1ba\"}");
			iconmap.put("fa-tree",":before{content:\"\\f1bb\"}");
			iconmap.put("fa-spotify",":before{content:\"\\f1bc\"}");
			iconmap.put("fa-deviantart",":before{content:\"\\f1bd\"}");
			iconmap.put("fa-soundcloud",":before{content:\"\\f1be\"}");
			iconmap.put("fa-database",":before{content:\"\\f1c0\"}");
			iconmap.put("fa-file-pdf-o",":before{content:\"\\f1c1\"}");
			iconmap.put("fa-file-word-o",":before{content:\"\\f1c2\"}");
			iconmap.put("fa-file-excel-o",":before{content:\"\\f1c3\"}");
			iconmap.put("fa-file-powerpoint-o",":before{content:\"\\f1c4\"}");
			iconmap.put("fa-file-photo-o",":before{content:\"\\f1c5\"}");
			iconmap.put("fa-file-picture-o",":before{content:\"\\f1c5\"}");
			iconmap.put("fa-file-image-o",":before{content:\"\\f1c5\"}");
			iconmap.put("fa-file-zip-o",":before{content:\"\\f1c6\"}");
			iconmap.put("fa-file-archive-o",":before{content:\"\\f1c6\"}");
			iconmap.put("fa-file-sound-o",":before{content:\"\\f1c7\"}");
			iconmap.put("fa-file-audio-o",":before{content:\"\\f1c7\"}");
			iconmap.put("fa-file-movie-o",":before{content:\"\\f1c8\"}");
			iconmap.put("fa-file-video-o",":before{content:\"\\f1c8\"}");
			iconmap.put("fa-file-code-o",":before{content:\"\\f1c9\"}");
			iconmap.put("fa-vine",":before{content:\"\\f1ca\"}");
			iconmap.put("fa-codepen",":before{content:\"\\f1cb\"}");
			iconmap.put("fa-jsfiddle",":before{content:\"\\f1cc\"}");
			iconmap.put("fa-life-bouy",":before{content:\"\\f1cd\"}");
			iconmap.put("fa-life-buoy",":before{content:\"\\f1cd\"}");
			iconmap.put("fa-life-saver",":before{content:\"\\f1cd\"}");
			iconmap.put("fa-support",":before{content:\"\\f1cd\"}");
			iconmap.put("fa-life-ring",":before{content:\"\\f1cd\"}");
			iconmap.put("fa-circle-o-notch",":before{content:\"\\f1ce\"}");
			iconmap.put("fa-ra",":before{content:\"\\f1d0\"}");
			iconmap.put("fa-resistance",":before{content:\"\\f1d0\"}");
			iconmap.put("fa-rebel",":before{content:\"\\f1d0\"}");
			iconmap.put("fa-ge",":before{content:\"\\f1d1\"}");
			iconmap.put("fa-empire",":before{content:\"\\f1d1\"}");
			iconmap.put("fa-git-square",":before{content:\"\\f1d2\"}");
			iconmap.put("fa-git",":before{content:\"\\f1d3\"}");
			iconmap.put("fa-y-combinator-square",":before{content:\"\\f1d4\"}");
			iconmap.put("fa-yc-square",":before{content:\"\\f1d4\"}");
			iconmap.put("fa-hacker-news",":before{content:\"\\f1d4\"}");
			iconmap.put("fa-tencent-weibo",":before{content:\"\\f1d5\"}");
			iconmap.put("fa-qq",":before{content:\"\\f1d6\"}");
			iconmap.put("fa-wechat",":before{content:\"\\f1d7\"}");
			iconmap.put("fa-weixin",":before{content:\"\\f1d7\"}");
			iconmap.put("fa-send",":before{content:\"\\f1d8\"}");
			iconmap.put("fa-paper-plane",":before{content:\"\\f1d8\"}");
			iconmap.put("fa-send-o",":before{content:\"\\f1d9\"}");
			iconmap.put("fa-paper-plane-o",":before{content:\"\\f1d9\"}");
			iconmap.put("fa-history",":before{content:\"\\f1da\"}");
			iconmap.put("fa-circle-thin",":before{content:\"\\f1db\"}");
			iconmap.put("fa-header",":before{content:\"\\f1dc\"}");
			iconmap.put("fa-paragraph",":before{content:\"\\f1dd\"}");
			iconmap.put("fa-sliders",":before{content:\"\\f1de\"}");
			iconmap.put("fa-share-alt",":before{content:\"\\f1e0\"}");
			iconmap.put("fa-share-alt-square",":before{content:\"\\f1e1\"}");
			iconmap.put("fa-bomb",":before{content:\"\\f1e2\"}");
			iconmap.put("fa-soccer-ball-o",":before{content:\"\\f1e3\"}");
			iconmap.put("fa-futbol-o",":before{content:\"\\f1e3\"}");
			iconmap.put("fa-tty",":before{content:\"\\f1e4\"}");
			iconmap.put("fa-binoculars",":before{content:\"\\f1e5\"}");
			iconmap.put("fa-plug",":before{content:\"\\f1e6\"}");
			iconmap.put("fa-slideshare",":before{content:\"\\f1e7\"}");
			iconmap.put("fa-twitch",":before{content:\"\\f1e8\"}");
			iconmap.put("fa-yelp",":before{content:\"\\f1e9\"}");
			iconmap.put("fa-newspaper-o",":before{content:\"\\f1ea\"}");
			iconmap.put("fa-wifi",":before{content:\"\\f1eb\"}");
			iconmap.put("fa-calculator",":before{content:\"\\f1ec\"}");
			iconmap.put("fa-paypal",":before{content:\"\\f1ed\"}");
			iconmap.put("fa-google-wallet",":before{content:\"\\f1ee\"}");
			iconmap.put("fa-cc-visa",":before{content:\"\\f1f0\"}");
			iconmap.put("fa-cc-mastercard",":before{content:\"\\f1f1\"}");
			iconmap.put("fa-cc-discover",":before{content:\"\\f1f2\"}");
			iconmap.put("fa-cc-amex",":before{content:\"\\f1f3\"}");
			iconmap.put("fa-cc-paypal",":before{content:\"\\f1f4\"}");
			iconmap.put("fa-cc-stripe",":before{content:\"\\f1f5\"}");
			iconmap.put("fa-bell-slash",":before{content:\"\\f1f6\"}");
			iconmap.put("fa-bell-slash-o",":before{content:\"\\f1f7\"}");
			iconmap.put("fa-trash",":before{content:\"\\f1f8\"}");
			iconmap.put("fa-copyright",":before{content:\"\\f1f9\"}");
			iconmap.put("fa-at",":before{content:\"\\f1fa\"}");
			iconmap.put("fa-eyedropper",":before{content:\"\\f1fb\"}");
			iconmap.put("fa-paint-brush",":before{content:\"\\f1fc\"}");
			iconmap.put("fa-birthday-cake",":before{content:\"\\f1fd\"}");
			iconmap.put("fa-area-chart",":before{content:\"\\f1fe\"}");
			iconmap.put("fa-pie-chart",":before{content:\"\\f200\"}");
			iconmap.put("fa-line-chart",":before{content:\"\\f201\"}");
			iconmap.put("fa-lastfm",":before{content:\"\\f202\"}");
			iconmap.put("fa-lastfm-square",":before{content:\"\\f203\"}");
			iconmap.put("fa-toggle-off",":before{content:\"\\f204\"}");
			iconmap.put("fa-toggle-on",":before{content:\"\\f205\"}");
			iconmap.put("fa-bicycle",":before{content:\"\\f206\"}");
			iconmap.put("fa-bus",":before{content:\"\\f207\"}");
			iconmap.put("fa-ioxhost",":before{content:\"\\f208\"}");
			iconmap.put("fa-angellist",":before{content:\"\\f209\"}");
			iconmap.put("fa-cc",":before{content:\"\\f20a\"}");
			iconmap.put("fa-shekel",":before{content:\"\\f20b\"}");
			iconmap.put("fa-sheqel",":before{content:\"\\f20b\"}");
			iconmap.put("fa-ils",":before{content:\"\\f20b\"}");
			iconmap.put("fa-meanpath",":before{content:\"\\f20c\"}");
			iconmap.put("fa-buysellads",":before{content:\"\\f20d\"}");
			iconmap.put("fa-connectdevelop",":before{content:\"\\f20e\"}");
			iconmap.put("fa-dashcube",":before{content:\"\\f210\"}");
			iconmap.put("fa-forumbee",":before{content:\"\\f211\"}");
			iconmap.put("fa-leanpub",":before{content:\"\\f212\"}");
			iconmap.put("fa-sellsy",":before{content:\"\\f213\"}");
			iconmap.put("fa-shirtsinbulk",":before{content:\"\\f214\"}");
			iconmap.put("fa-simplybuilt",":before{content:\"\\f215\"}");
			iconmap.put("fa-skyatlas",":before{content:\"\\f216\"}");
			iconmap.put("fa-cart-plus",":before{content:\"\\f217\"}");
			iconmap.put("fa-cart-arrow-down",":before{content:\"\\f218\"}");
			iconmap.put("fa-diamond",":before{content:\"\\f219\"}");
			iconmap.put("fa-ship",":before{content:\"\\f21a\"}");
			iconmap.put("fa-user-secret",":before{content:\"\\f21b\"}");
			iconmap.put("fa-motorcycle",":before{content:\"\\f21c\"}");
			iconmap.put("fa-street-view",":before{content:\"\\f21d\"}");
			iconmap.put("fa-heartbeat",":before{content:\"\\f21e\"}");
			iconmap.put("fa-venus",":before{content:\"\\f221\"}");
			iconmap.put("fa-mars",":before{content:\"\\f222\"}");
			iconmap.put("fa-mercury",":before{content:\"\\f223\"}");
			iconmap.put("fa-intersex",":before{content:\"\\f224\"}");
			iconmap.put("fa-transgender",":before{content:\"\\f224\"}");
			iconmap.put("fa-transgender-alt",":before{content:\"\\f225\"}");
			iconmap.put("fa-venus-double",":before{content:\"\\f226\"}");
			iconmap.put("fa-mars-double",":before{content:\"\\f227\"}");
			iconmap.put("fa-venus-mars",":before{content:\"\\f228\"}");
			iconmap.put("fa-mars-stroke",":before{content:\"\\f229\"}");
			iconmap.put("fa-mars-stroke-v",":before{content:\"\\f22a\"}");
			iconmap.put("fa-mars-stroke-h",":before{content:\"\\f22b\"}");
			iconmap.put("fa-neuter",":before{content:\"\\f22c\"}");
			iconmap.put("fa-genderless",":before{content:\"\\f22d\"}");
			iconmap.put("fa-facebook-official",":before{content:\"\\f230\"}");
			iconmap.put("fa-pinterest-p",":before{content:\"\\f231\"}");
			iconmap.put("fa-whatsapp",":before{content:\"\\f232\"}");
			iconmap.put("fa-server",":before{content:\"\\f233\"}");
			iconmap.put("fa-user-plus",":before{content:\"\\f234\"}");
			iconmap.put("fa-user-times",":before{content:\"\\f235\"}");
			iconmap.put("fa-hotel",":before{content:\"\\f236\"}");
			iconmap.put("fa-bed",":before{content:\"\\f236\"}");
			iconmap.put("fa-viacoin",":before{content:\"\\f237\"}");
			iconmap.put("fa-train",":before{content:\"\\f238\"}");
			iconmap.put("fa-subway",":before{content:\"\\f239\"}");
			iconmap.put("fa-medium",":before{content:\"\\f23a\"}");
			iconmap.put("fa-yc",":before{content:\"\\f23b\"}");
			iconmap.put("fa-y-combinator",":before{content:\"\\f23b\"}");
			iconmap.put("fa-optin-monster",":before{content:\"\\f23c\"}");
			iconmap.put("fa-opencart",":before{content:\"\\f23d\"}");
			iconmap.put("fa-expeditedssl",":before{content:\"\\f23e\"}");
			iconmap.put("fa-battery-4",":before{content:\"\\f240\"}");
			iconmap.put("fa-battery-full",":before{content:\"\\f240\"}");
			iconmap.put("fa-battery-3",":before{content:\"\\f241\"}");
			iconmap.put("fa-battery-three-quarters",":before{content:\"\\f241\"}");
			iconmap.put("fa-battery-2",":before{content:\"\\f242\"}");
			iconmap.put("fa-battery-half",":before{content:\"\\f242\"}");
			iconmap.put("fa-battery-1",":before{content:\"\\f243\"}");
			iconmap.put("fa-battery-quarter",":before{content:\"\\f243\"}");
			iconmap.put("fa-battery-0",":before{content:\"\\f244\"}");
			iconmap.put("fa-battery-empty",":before{content:\"\\f244\"}");
			iconmap.put("fa-mouse-pointer",":before{content:\"\\f245\"}");
			iconmap.put("fa-i-cursor",":before{content:\"\\f246\"}");
			iconmap.put("fa-object-group",":before{content:\"\\f247\"}");
			iconmap.put("fa-object-ungroup",":before{content:\"\\f248\"}");
			iconmap.put("fa-sticky-note",":before{content:\"\\f249\"}");
			iconmap.put("fa-sticky-note-o",":before{content:\"\\f24a\"}");
			iconmap.put("fa-cc-jcb",":before{content:\"\\f24b\"}");
			iconmap.put("fa-cc-diners-club",":before{content:\"\\f24c\"}");
			iconmap.put("fa-clone",":before{content:\"\\f24d\"}");
			iconmap.put("fa-balance-scale",":before{content:\"\\f24e\"}");
			iconmap.put("fa-hourglass-o",":before{content:\"\\f250\"}");
			iconmap.put("fa-hourglass-1",":before{content:\"\\f251\"}");
			iconmap.put("fa-hourglass-start",":before{content:\"\\f251\"}");
			iconmap.put("fa-hourglass-2",":before{content:\"\\f252\"}");
			iconmap.put("fa-hourglass-half",":before{content:\"\\f252\"}");
			iconmap.put("fa-hourglass-3",":before{content:\"\\f253\"}");
			iconmap.put("fa-hourglass-end",":before{content:\"\\f253\"}");
			iconmap.put("fa-hourglass",":before{content:\"\\f254\"}");
			iconmap.put("fa-hand-grab-o",":before{content:\"\\f255\"}");
			iconmap.put("fa-hand-rock-o",":before{content:\"\\f255\"}");
			iconmap.put("fa-hand-stop-o",":before{content:\"\\f256\"}");
			iconmap.put("fa-hand-paper-o",":before{content:\"\\f256\"}");
			iconmap.put("fa-hand-scissors-o",":before{content:\"\\f257\"}");
			iconmap.put("fa-hand-lizard-o",":before{content:\"\\f258\"}");
			iconmap.put("fa-hand-spock-o",":before{content:\"\\f259\"}");
			iconmap.put("fa-hand-pointer-o",":before{content:\"\\f25a\"}");
			iconmap.put("fa-hand-peace-o",":before{content:\"\\f25b\"}");
			iconmap.put("fa-trademark",":before{content:\"\\f25c\"}");
			iconmap.put("fa-registered",":before{content:\"\\f25d\"}");
			iconmap.put("fa-creative-commons",":before{content:\"\\f25e\"}");
			iconmap.put("fa-gg",":before{content:\"\\f260\"}");
			iconmap.put("fa-gg-circle",":before{content:\"\\f261\"}");
			iconmap.put("fa-tripadvisor",":before{content:\"\\f262\"}");
			iconmap.put("fa-odnoklassniki",":before{content:\"\\f263\"}");
			iconmap.put("fa-odnoklassniki-square",":before{content:\"\\f264\"}");
			iconmap.put("fa-get-pocket",":before{content:\"\\f265\"}");
			iconmap.put("fa-wikipedia-w",":before{content:\"\\f266\"}");
			iconmap.put("fa-safari",":before{content:\"\\f267\"}");
			iconmap.put("fa-chrome",":before{content:\"\\f268\"}");
			iconmap.put("fa-firefox",":before{content:\"\\f269\"}");
			iconmap.put("fa-opera",":before{content:\"\\f26a\"}");
			iconmap.put("fa-internet-explorer",":before{content:\"\\f26b\"}");
			iconmap.put("fa-tv",":before{content:\"\\f26c\"}");
			iconmap.put("fa-television",":before{content:\"\\f26c\"}");
			iconmap.put("fa-contao",":before{content:\"\\f26d\"}");
			iconmap.put("fa-500px",":before{content:\"\\f26e\"}");
			iconmap.put("fa-amazon",":before{content:\"\\f270\"}");
			iconmap.put("fa-calendar-plus-o",":before{content:\"\\f271\"}");
			iconmap.put("fa-calendar-minus-o",":before{content:\"\\f272\"}");
			iconmap.put("fa-calendar-times-o",":before{content:\"\\f273\"}");
			iconmap.put("fa-calendar-check-o",":before{content:\"\\f274\"}");
			iconmap.put("fa-industry",":before{content:\"\\f275\"}");
			iconmap.put("fa-map-pin",":before{content:\"\\f276\"}");
			iconmap.put("fa-map-signs",":before{content:\"\\f277\"}");
			iconmap.put("fa-map-o",":before{content:\"\\f278\"}");
			iconmap.put("fa-map",":before{content:\"\\f279\"}");
			iconmap.put("fa-commenting",":before{content:\"\\f27a\"}");
			iconmap.put("fa-commenting-o",":before{content:\"\\f27b\"}");
			iconmap.put("fa-houzz",":before{content:\"\\f27c\"}");
			iconmap.put("fa-vimeo",":before{content:\"\\f27d\"}");
			iconmap.put("fa-black-tie",":before{content:\"\\f27e\"}");
			iconmap.put("fa-fonticons",":before{content:\"\\f280\"}");
			iconmap.put("fa-reddit-alien",":before{content:\"\\f281\"}");
			iconmap.put("fa-edge",":before{content:\"\\f282\"}");
			iconmap.put("fa-credit-card-alt",":before{content:\"\\f283\"}");
			iconmap.put("fa-codiepie",":before{content:\"\\f284\"}");
			iconmap.put("fa-modx",":before{content:\"\\f285\"}");
			iconmap.put("fa-fort-awesome",":before{content:\"\\f286\"}");
			iconmap.put("fa-usb",":before{content:\"\\f287\"}");
			iconmap.put("fa-product-hunt",":before{content:\"\\f288\"}");
			iconmap.put("fa-mixcloud",":before{content:\"\\f289\"}");
			iconmap.put("fa-scribd",":before{content:\"\\f28a\"}");
			iconmap.put("fa-pause-circle",":before{content:\"\\f28b\"}");
			iconmap.put("fa-pause-circle-o",":before{content:\"\\f28c\"}");
			iconmap.put("fa-stop-circle",":before{content:\"\\f28d\"}");
			iconmap.put("fa-stop-circle-o",":before{content:\"\\f28e\"}");
			iconmap.put("fa-shopping-bag",":before{content:\"\\f290\"}");
			iconmap.put("fa-shopping-basket",":before{content:\"\\f291\"}");
			iconmap.put("fa-hashtag",":before{content:\"\\f292\"}");
			iconmap.put("fa-bluetooth",":before{content:\"\\f293\"}");
			iconmap.put("fa-bluetooth-b",":before{content:\"\\f294\"}");
			iconmap.put("fa-percent",":before{content:\"\\f295\"}");
			iconmap.put("fa-gitlab",":before{content:\"\\f296\"}");
			iconmap.put("fa-wpbeginner",":before{content:\"\\f297\"}");
			iconmap.put("fa-wpforms",":before{content:\"\\f298\"}");
			iconmap.put("fa-envira",":before{content:\"\\f299\"}");
			iconmap.put("fa-universal-access",":before{content:\"\\f29a\"}");
			iconmap.put("fa-wheelchair-alt",":before{content:\"\\f29b\"}");
			iconmap.put("fa-question-circle-o",":before{content:\"\\f29c\"}");
			iconmap.put("fa-blind",":before{content:\"\\f29d\"}");
			iconmap.put("fa-audio-description",":before{content:\"\\f29e\"}");
			iconmap.put("fa-volume-control-phone",":before{content:\"\\f2a0\"}");
			iconmap.put("fa-braille",":before{content:\"\\f2a1\"}");
			iconmap.put("fa-assistive-listening-systems",":before{content:\"\\f2a2\"}");
			iconmap.put("fa-asl-interpreting",":before{content:\"\\f2a3\"}");
			iconmap.put("fa-american-sign-language-interpreting",":before{content:\"\\f2a3\"}");
			iconmap.put("fa-deafness",":before{content:\"\\f2a4\"}");
			iconmap.put("fa-hard-of-hearing",":before{content:\"\\f2a4\"}");
			iconmap.put("fa-deaf",":before{content:\"\\f2a4\"}");
			iconmap.put("fa-glide",":before{content:\"\\f2a5\"}");
			iconmap.put("fa-glide-g",":before{content:\"\\f2a6\"}");
			iconmap.put("fa-signing",":before{content:\"\\f2a7\"}");
			iconmap.put("fa-sign-language",":before{content:\"\\f2a7\"}");
			iconmap.put("fa-low-vision",":before{content:\"\\f2a8\"}");
			iconmap.put("fa-viadeo",":before{content:\"\\f2a9\"}");
			iconmap.put("fa-viadeo-square",":before{content:\"\\f2aa\"}");
			iconmap.put("fa-snapchat",":before{content:\"\\f2ab\"}");
			iconmap.put("fa-snapchat-ghost",":before{content:\"\\f2ac\"}");
			iconmap.put("fa-snapchat-square",":before{content:\"\\f2ad\"}");
			iconmap.put("fa-pied-piper",":before{content:\"\\f2ae\"}");
			iconmap.put("fa-first-order",":before{content:\"\\f2b0\"}");
			iconmap.put("fa-yoast",":before{content:\"\\f2b1\"}");
			iconmap.put("fa-themeisle",":before{content:\"\\f2b2\"}");
			iconmap.put("fa-google-plus-circle",":before{content:\"\\f2b3\"}");
			iconmap.put("fa-google-plus-official",":before{content:\"\\f2b3\"}");
			iconmap.put("fa-fa",":before{content:\"\\f2b4\"}");
			iconmap.put("fa-font-awesome",":before{content:\"\\f2b4\"}");
			iconmap.put("fa-handshake-o",":before{content:\"\\f2b5\"}");
			iconmap.put("fa-envelope-open",":before{content:\"\\f2b6\"}");
			iconmap.put("fa-envelope-open-o",":before{content:\"\\f2b7\"}");
			iconmap.put("fa-linode",":before{content:\"\\f2b8\"}");
			iconmap.put("fa-address-book",":before{content:\"\\f2b9\"}");
			iconmap.put("fa-address-book-o",":before{content:\"\\f2ba\"}");
			iconmap.put("fa-vcard",":before{content:\"\\f2bb\"}");
			iconmap.put("fa-address-card",":before{content:\"\\f2bb\"}");
			iconmap.put("fa-vcard-o",":before{content:\"\\f2bc\"}");
			iconmap.put("fa-address-card-o",":before{content:\"\\f2bc\"}");
			iconmap.put("fa-user-circle",":before{content:\"\\f2bd\"}");
			iconmap.put("fa-user-circle-o",":before{content:\"\\f2be\"}");
			iconmap.put("fa-user-o",":before{content:\"\\f2c0\"}");
			iconmap.put("fa-id-badge",":before{content:\"\\f2c1\"}");
			iconmap.put("fa-drivers-license",":before{content:\"\\f2c2\"}");
			iconmap.put("fa-id-card",":before{content:\"\\f2c2\"}");
			iconmap.put("fa-drivers-license-o",":before{content:\"\\f2c3\"}");
			iconmap.put("fa-id-card-o",":before{content:\"\\f2c3\"}");
			iconmap.put("fa-quora",":before{content:\"\\f2c4\"}");
			iconmap.put("fa-free-code-camp",":before{content:\"\\f2c5\"}");
			iconmap.put("fa-telegram",":before{content:\"\\f2c6\"}");
			iconmap.put("fa-thermometer-4",":before{content:\"\\f2c7\"}");
			iconmap.put("fa-thermometer",":before{content:\"\\f2c7\"}");
			iconmap.put("fa-thermometer-full",":before{content:\"\\f2c7\"}");
			iconmap.put("fa-thermometer-3",":before{content:\"\\f2c8\"}");
			iconmap.put("fa-thermometer-three-quarters",":before{content:\"\\f2c8\"}");
			iconmap.put("fa-thermometer-2",":before{content:\"\\f2c9\"}");
			iconmap.put("fa-thermometer-half",":before{content:\"\\f2c9\"}");
			iconmap.put("fa-thermometer-1",":before{content:\"\\f2ca\"}");
			iconmap.put("fa-thermometer-quarter",":before{content:\"\\f2ca\"}");
			iconmap.put("fa-thermometer-0",":before{content:\"\\f2cb\"}");
			iconmap.put("fa-thermometer-empty",":before{content:\"\\f2cb\"}");
			iconmap.put("fa-shower",":before{content:\"\\f2cc\"}");
			iconmap.put("fa-bathtub",":before{content:\"\\f2cd\"}");
			iconmap.put("fa-s15",":before{content:\"\\f2cd\"}");
			iconmap.put("fa-bath",":before{content:\"\\f2cd\"}");
			iconmap.put("fa-podcast",":before{content:\"\\f2ce\"}");
			iconmap.put("fa-window-maximize",":before{content:\"\\f2d0\"}");
			iconmap.put("fa-window-minimize",":before{content:\"\\f2d1\"}");
			iconmap.put("fa-window-restore",":before{content:\"\\f2d2\"}");
			iconmap.put("fa-times-rectangle",":before{content:\"\\f2d3\"}");
			iconmap.put("fa-window-close",":before{content:\"\\f2d3\"}");
			iconmap.put("fa-times-rectangle-o",":before{content:\"\\f2d4\"}");
			iconmap.put("fa-window-close-o",":before{content:\"\\f2d4\"}");
			iconmap.put("fa-bandcamp",":before{content:\"\\f2d5\"}");
			iconmap.put("fa-grav",":before{content:\"\\f2d6\"}");
			iconmap.put("fa-etsy",":before{content:\"\\f2d7\"}");
			iconmap.put("fa-imdb",":before{content:\"\\f2d8\"}");
			iconmap.put("fa-ravelry",":before{content:\"\\f2d9\"}");
			iconmap.put("fa-eercast",":before{content:\"\\f2da\"}");
			iconmap.put("fa-microchip",":before{content:\"\\f2db\"}");
			iconmap.put("fa-snowflake-o",":before{content:\"\\f2dc\"}");
			iconmap.put("fa-superpowers",":before{content:\"\\f2dd\"}");
			iconmap.put("fa-wpexplorer",":before{content:\"\\f2de\"}");
			iconmap.put("fa-meetup",":before{content:\"\\f2e0\"}");
		}
	}	
		
}
