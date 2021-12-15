package com.ab.abmaterial;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.ocpsoft.prettytime.Duration;
import org.ocpsoft.prettytime.PrettyTime;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.keywords.Regex;
import anywheresoftware.b4j.object.WebSocket.SimpleFuture;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 * 
 * Some general methods for creating random texts, calculating and converting dates, (un)escaping unicode chars, BING translations and GEO location methods
 */
public class Utilities {
	private boolean cID=false;
	private final static double MIN_LAT = Math.toRadians(-90d);  // -PI/2
	private final static double MAX_LAT = Math.toRadians(90d);   //  PI/2
	private final static double MIN_LON = Math.toRadians(-180d); // -PI
	private final static double MAX_LON = Math.toRadians(180d);  //  PI.
	protected LoremIpsum LoremIpsumGenerator=new LoremIpsum();
	
	public final static int WHITELIST_NONE=0;
	public final static int WHITELIST_SIMPLETEXT=1;
	public final static int WHITELIST_BASIC=2;	
	public final static int WHITELIST_BASICWITHIMAGES=3;
	public final static int WHITELIST_RELAXED=4;
	
	/**
	 * Generates a random double
	 */
	public double Rnd(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}
	
	/**
	 * Get a random word
	 */
	public String randomWord() {
		return LoremIpsumGenerator.randomWord();
	}

	/**
	 * Get a random punctuation mark
	 */
	public String randomPunctuation() {
		return LoremIpsumGenerator.randomPunctuation();
	}

	/**
	 * Get a string of words
	 * 
	 * count: the number of words to fetch
	 */
	public String words(int count) {
		return LoremIpsumGenerator.words(count);
	}

	/**
	 * Get a sentence fragment
	 */
	public String sentenceFragment() {
		return LoremIpsumGenerator.sentenceFragment();
	}

	/**
	 * Get a sentence
	 */
	public String sentence() {
		return LoremIpsumGenerator.sentence();
	}

	/**
	 * Get multiple sentences
	 * 
	 * count: the number of sentences
	 */
	public String sentences(int count) {
		return LoremIpsumGenerator.sentences(count);
	}

	/**
	 * Get a paragraph
	 * 
	 * useStandard: begin with the standard Lorem Ipsum paragraph?
	 */
	public String paragraph(boolean useStandard) {
		return LoremIpsumGenerator.paragraph(useStandard);
	}	

	/**
	 * Get multiple paragraphs
	 * 
	 * count: the number of paragraphs
	 * useStandard: begin with the standard Lorem Ipsum paragraph?
	 */
	public String paragraphs(int count, boolean useStandard) {
		return LoremIpsumGenerator.paragraphs(count, useStandard);
	}	
	
	/**
	 * Convert a B4J long to a date (String) 
	 */
	public String ConvertToDateTimeString(long value, String format) {
		Date value2 = new Date();
		value2.setTime(value);
		DateFormat df = new SimpleDateFormat(format);
		return df.format(value2);
	}
	
	/**
	 * Convert a date (string format) to a B4J long 
	 */
	public long ConvertFromDateTimeString(String value, String format) {
		DateFormat df = new SimpleDateFormat(format);
		try {
			Date value2 = df.parse(value);
			return value2.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Initialize the ABMaterial BING translator 
	 */
	public void BINGActivateTranslator(ABMPage page, String donatorKey, String ClientID, String ClientSecret) {
        
        cID=true;
        MicrosoftTranslate.setClientSecret(ClientSecret);
        MicrosoftTranslate.setClientId(ClientID);        
	}
	
	public boolean MoveFile(String srcDir, String srcFileName, String tgtDir, String tgtFileName) {
	    File tmpFile = new File(srcDir, srcFileName);
	    File newFile = new File(tgtDir, tgtFileName);
	    
	    if (tmpFile.exists()) {
	    	return tmpFile.renameTo(newFile) ;  
	    } else {
	    	BA.Log(srcFileName + " does not exist!");
	    	return false;
	    }
		
	}
	
	public boolean RenameFile(String dir, String oldFileName, String newFileName) {
	    File tmpFile = new File(dir, oldFileName);
	    File newFile = new File(dir, newFileName);
	    
	    if (tmpFile.exists()) {
	    	return tmpFile.renameTo(newFile) ;  
	    } else {
	    	BA.Log(oldFileName + " does not exist!");
	    	return false;
	    }
		
	}
	
	/**
	 * Use BING to translate a string 
	 */
	public String BINGTranslate(String text, String srcLanguage, String tgtLanguage) throws Exception {
		if (!cID) {
			BA.LogError("BINGTranslate: Use BINGActivateTranslator before running this method!");
			return "";
		}
		return MicrosoftTranslate.execute(text, srcLanguage, tgtLanguage);		 
	}
	
	/**
	 * Use BING to translate a List of strings 
	 */
	public anywheresoftware.b4a.objects.collections.List BINGTranslateMultipleTexts(anywheresoftware.b4a.objects.collections.List texts, String srcLanguage, String tgtLanguage) throws Exception {
		if (!cID) {
			BA.LogError("BINGTranslateMultipleTexts: Use BINGActivateTranslator before running this method!");
			return null;
		}
		String[] textsArray = new String[texts.getSize()];
		for (int i=0;i<texts.getSize();i++) {
			textsArray[i] = (String)texts.Get(i);
		}
		final String[] results = MicrosoftTranslate.execute(textsArray, srcLanguage, tgtLanguage);
		anywheresoftware.b4a.objects.collections.List res = new anywheresoftware.b4a.objects.collections.List();
		res.Initialize();
		for (String s: results) {
			res.Add(s);
		}
		return res;
	}	
	
	/**
	 * Beware! This is a helper method. Do NOT run this method in RELEASE mode or every time your app starts! You'll abuse you're BING account!
	 */
	public void BINGCreateTranslationFiles(String dirToLanguageFiles, String srcLanguage, anywheresoftware.b4a.objects.collections.List tgtLanguages) {
		if (!cID) {
			BA.LogError("BINGCreateTranslationFiles: Use BINGActivateTranslator before running this method!");
			return;
		}
		BA.LogInfo("RUNNING BINGCreateTranslationFiles. This is a helper method. Do NOT run this method in RELEASE mode or every time your app starts! You'll abuse you're BING account!");
		BA.LogInfo("Note: 'extra' .lgn files like 'en_myclient.lng' will not be deleted or recreated.  You have to make them manually.");
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
		for (File f: files) {
			if (!f.getName().contains("_")) {
				f.delete();
			}
		}
		
		String dirApp = anywheresoftware.b4a.objects.streams.File.getDirApp();
		File readFile = new File(dirApp, "BaseTranslations.lng");
		Map<String,String> ToTranslate=new LinkedHashMap<String,String>();
		if (readFile.exists()) {
			if (readFile.exists()) {
				BufferedReader br=null;
				try {

					String sCurrentLine;

					br = new BufferedReader(new FileReader(readFile));

					while ((sCurrentLine = br.readLine()) != null) {
						String p[] = Regex.Split(";", sCurrentLine);
						switch (p.length) {
						case 0:
						case 1:							
							break;
						case 2:
							ToTranslate.put(p[0], p[1]);
							break;
						default:
							String[] tmpArray = Arrays.copyOfRange(p, 1, p.length);
							String joined = String.join(";", tmpArray);
							ToTranslate.put(p[0], joined);
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
			}
		}
		
		String[] textsArray = new String[ToTranslate.size()];
		String[] keysArray = new String[ToTranslate.size()];
		List<Integer> transParts = new ArrayList<Integer>();
		int currentSize=0;
		
		int j = 0;
		BufferedWriter writerTransOrig = null;
	    try {
	    	File buildFile = new File(dirToLanguageFiles, srcLanguage + ".lng");
	   
	    	writerTransOrig = new BufferedWriter(new FileWriter(buildFile));
	    	for (Entry<String,String> entry: ToTranslate.entrySet()) {
	    		keysArray[j] = entry.getKey();
	    		textsArray[j] = entry.getValue();
	    		currentSize+=textsArray[j].length();
	    		if (currentSize>10000) {
	    			transParts.add(j);
	    			currentSize=0;
	    		}
			
	    		writerTransOrig.write(keysArray[j] + ";" + textsArray[j] + "\n");
	    		j++;	    		
	    	}
	    } catch (Exception e) {
	           e.printStackTrace();
	    } finally {
	           try {
	           		writerTransOrig.close();
	           } catch (Exception e) {
	           }
	    } 
		transParts.add(ToTranslate.size());		
		
		
		for (int i=0;i<tgtLanguages.getSize();i++) {
			BufferedWriter writerTrans = null;
		    try {
		    	String tgtLanguage=(String)tgtLanguages.Get(i);
		       	File buildFile = new File(dirToLanguageFiles, tgtLanguage + ".lng");
		       	
		       	int prev=0;
		       	writerTrans = new BufferedWriter(new FileWriter(buildFile));
		       	for (int w=0;w<transParts.size();w++) {
		       		String[] currTrans = Arrays.copyOfRange(textsArray, prev, transParts.get(w));
		       		String[] currKeys = Arrays.copyOfRange(keysArray, prev, transParts.get(w));
		       		final String[] results = MicrosoftTranslate.execute(currTrans, srcLanguage, tgtLanguage);
		       		for (int k=0;k<results.length;k++) {
		       			//BA.Log("Translation: " + results[k]);
		       			writerTrans.write(currKeys[k] + ";" + results[k] + "\n");
		       		}
		       		prev = transParts.get(w);
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
		BA.LogInfo("Translation done! Do not forget to deactivate this method!");
	}
	
	/**
	 * Splits the string on the delimiter, but preserves empty trailing ones
	 */
	public String[] Split3(String delimiter, String text) {
		return text.split(delimiter, -1);
	}
	
	/**
	 * Returns a human readable string compared to Now (e.g. A moment ago, 3 hours ago, 2 months from now...
	 * Supports a couple of languages. Language codes, see: http://www.ocpsoft.org/prettytime/ 
	 */
	public String GetDatePrettyPrint(long testDate, String locale, boolean calcPrecise) {
		Locale loc = new Locale(locale);
		String s="";
		if (testDate < new Date().getTime()) {
			PrettyTime t = new PrettyTime(new Date());
			t.setLocale(loc);
			if (calcPrecise) {
				List<Duration> durations = t.calculatePreciseDuration(new Date(testDate));
				s = t.format(durations);
			} else {
				s = t.format(new Date(testDate));
			}
		} else {			
			PrettyTime t = new PrettyTime();
			t.setLocale(loc);
			if (calcPrecise) {
				List<Duration> durations = t.calculatePreciseDuration(new Date(testDate));
				s = t.format(durations);
			} else {
				s = t.format(new Date(testDate));
			}
		}
		return s;
	}
	
	/**
	 * Shows a human readable form of a duration 
	 * Supports a couple of languages. Language codes, see: http://www.ocpsoft.org/prettytime/ 
	 */
	public String GetDatePrettyPrintDuration(long testDate, String locale, boolean calcPrecise) {
		Locale loc = new Locale(locale);
		String s="";
		
		PrettyTime t = new PrettyTime();
		t.setLocale(loc);
		if (!calcPrecise) {
			Duration durations = t.approximateDuration(new Date(System.currentTimeMillis()-testDate)); // calculatePreciseDuration
			s = t.formatDuration(durations);
		} else {
			s = t.formatDuration(new Date(System.currentTimeMillis()-testDate));
		}
		
		return s;
	}
	
	
	public String GetDatePrettyPrintDurationFromTime(String testTime, String locale, boolean calcPrecise) {
		Locale loc = new Locale(locale);
		String s="";

		
		PrettyTime t = new PrettyTime(new Date(0));
		t.setLocale(loc);
		
		long Time=0;
		String p[] = Regex.Split(":", testTime);
		switch (p.length) {
		case 0:
		case 1:							
			Time = Long.parseLong(p[0]) * 60 * 60 * 1000;			
			break;
		case 2:
			Time = Long.parseLong(p[0]) * 60 * 60 * 1000 + Long.parseLong(p[1]) * 60 * 1000;
			break;
		default:
			Time = Long.parseLong(p[0]) * 60 * 60 * 1000 + Long.parseLong(p[1]) * 60 * 1000 + Long.parseLong(p[2]) * 1000;
			break;
		}
		
		
		if (!calcPrecise) {
			Duration durations = t.approximateDuration(new Date(Time)); // calculatePreciseDuration
			s = t.formatDuration(durations);
		} else {
			List<Duration> duration = t.calculatePreciseDuration(new Date(Time));
			s = t.formatDuration(duration);
		}
		return s;
	}	
	
	/**
	 * will return a string in the format WidthxHeight.  Use split to know the width and height 
	 */
	public String GetImageDimensions(String dir, String fileName) {
		File file = new File(dir, fileName);
		SimpleImageInfo info;
		try {
			info = new SimpleImageInfo(file);
			String s = info.getWidth() + "x" + info.getHeight();
			return s;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		
	}
	
	/**
	 * Returns the video ID from a youtube URL 
	 */
	public String GetYoutubeIDFromUrl(String youtubeUrl) {
		String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

	    Pattern compiledPattern = Pattern.compile(pattern);
	    Matcher matcher = compiledPattern.matcher(youtubeUrl);

	    if(matcher.find()){
	        return matcher.group();
	    } else {
	    	return "";
	    }
	}
	
	/**
	 * Generates a Time-based One-time Password. Encryption must be a TOTP_ENCRYPT_ constant.
	 */
	public String GetTOTP(String key, String encryption, long time, int validTimeSeconds, int returnDigits) {
		String k2 = stringToHex(key);
		long T = (time)/(validTimeSeconds*1000);
        String steps = Long.toHexString(T).toUpperCase();
        while(steps.length() < 16) steps = "0" + steps;
		return TOTP.generateTOTP(k2, steps, Integer.toString(returnDigits), encryption);
    }
	
	/**
	 * It uses the Hex key, not the base32 key!
	 * Uses a validTime of 30 seconds and needs a 6 digit code 
	 */
	public boolean CheckTOTP(String hexKey, String check, int windowSec) {		
		long time = System.currentTimeMillis();
		 for (int i = -windowSec; i <= windowSec; ++i) {
			 String hexTime = Long.toHexString(((time/1000)+i)/30);
			 String found = TOTP.generateTOTP(hexKey, hexTime, "6");
			 if (found.equals(check)) {
				 return true;
			 }
		 }		 
		 return false;
    }
	
	public double GetCurrentTimeZoneHours() {
		TimeZone tz = TimeZone.getDefault();
		return tz.getOffset(new Date().getTime()) / 1000 / 60 / 60;
    }
	
	/**
	 * Converts a String to Hex 
	 */
	protected String stringToHex(String base) {
	     StringBuffer buffer = new StringBuffer();
	     int intValue;
	     for(int x = 0; x < base.length(); x++)
	         {
	         int cursor = 0;
	         intValue = base.charAt(x);
	         String binaryChar = new String(Integer.toBinaryString(base.charAt(x)));
	         for(int i = 0; i < binaryChar.length(); i++)
	             {
	             if(binaryChar.charAt(i) == '1')
	                 {
	                 cursor += 1;
	             }
	         }
	         if((cursor % 2) > 0)
	             {
	             intValue += 128;
	         }
	         buffer.append(Integer.toHexString(intValue));
	     }
	     return buffer.toString();
	}
		
	/**
	 * Converts an ISO date from a Timezone to another Timezone 
	 */
	public String GetDateConvertTimeZone(String dateISO, String fromTimeZone, String toTimeZone ) throws ParseException {
		if (dateISO==null || dateISO=="") {
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone(fromTimeZone));
		Date date = formatter.parse(dateISO);		
		
		SimpleDateFormat formatterOut = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		String tmpDate = formatterOut.format(date);
				
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		formatter2.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date2 = formatter2.parse(tmpDate);
		
		SimpleDateFormat formatterOut2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		formatterOut2.setTimeZone(TimeZone.getTimeZone(toTimeZone));
		String tmpDate2 = formatterOut2.format(date2);
				
		return tmpDate2;
	}
	
	public String GetDateConvertUTCToTimeZoneFormatted(String dateUTC, String toTimeZone, String outFormat ) throws ParseException {
		if (dateUTC==null || dateUTC=="") {
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = formatter.parse(dateUTC);		
		
		SimpleDateFormat formatterOut = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		String tmpDate = formatterOut.format(date);
				
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		formatter2.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date2 = formatter2.parse(tmpDate);
		
		SimpleDateFormat formatterOut2 = new SimpleDateFormat(outFormat);
		formatterOut2.setTimeZone(TimeZone.getTimeZone(toTimeZone));
		String tmpDate2 = formatterOut2.format(date2);
				
		return tmpDate2;
	}
	
	/**
	 * Formats a B4J date (long)
	 */
	public String FormatDate(Long date, String timeZone, String format, String locale) {
		//"EEEE dd MMM yyyy"
		Locale loc = new Locale(locale);
		SimpleDateFormat dateFormat = new SimpleDateFormat(format, loc);
		if (!timeZone.equals("")) {
			dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
		}
		String formatted = dateFormat.format(new Date(date));
		return formatted;
		
	}
	
	/**
	 * Formats an ISO date 
	 */
	public String FormatDateISO(String dateISO, String timeZone, String format, String locale) throws ParseException {
		//"EEEE dd MMM yyyy"
		Locale loc = new Locale(locale);
		SimpleDateFormat dateFormat = new SimpleDateFormat(format, loc);
		if (!timeZone.equals("")) {
			dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
		Date date = formatter.parse(dateISO);	
		String formatted = dateFormat.format(date);
		return formatted;
		
	}
	
	public String FormatDateTimeISO(String dateISO, String timeZone, String format, String locale) throws ParseException {
		//"EEEE dd MMM yyyy"
		Locale loc = new Locale(locale);
		SimpleDateFormat dateFormat = new SimpleDateFormat(format, loc);
		if (!timeZone.equals("")) {
			dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
		Date date = formatter.parse(dateISO);	
		String formatted = dateFormat.format(date);
		return formatted;
		
	}
	
	/**
	 * Converts a ISO date to UTC
	 */
	public String GetDateConvertToUTC(String dateISO, String fromTimeZone) throws ParseException {
		if (dateISO==null || dateISO=="") {
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone(fromTimeZone));
		Date date = formatter.parse(dateISO);
		
		SimpleDateFormat formatterOut = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		String tmpDate = formatterOut.format(date);
		
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		formatter2.setTimeZone(TimeZone.getDefault());
		Date date2 = formatter2.parse(tmpDate);
		
		SimpleDateFormat formatterOut2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		formatterOut2.setTimeZone(TimeZone.getTimeZone("UTC"));
		String tmpDate2 = formatterOut2.format(date2);
		
		return tmpDate2;
	}
		
	/**
	 * Gets the number of seconds between two IOS dates 
	 */
	public long GetSecondsBetween(String fromDateISO, String fromTimeZone, String toDateISO, String toTimeZone) {
		LocalDateTime fromDT = LocalDateTime.parse(fromDateISO);
		ZoneId fromZoneId = ZoneId.of(fromTimeZone);
		ZonedDateTime from = ZonedDateTime.of(fromDT, fromZoneId);
		
		LocalDateTime toDT = LocalDateTime.parse(toDateISO);
		ZoneId toZoneId = ZoneId.of(toTimeZone);
		ZonedDateTime to = ZonedDateTime.of(toDT, toZoneId);
		
		long seconds = java.time.Duration.between(from, to).getSeconds();
		return seconds;
	}
	
	/**
	 * Gets the number of seconds between two IOS dates 
	 */
	public long GetDaysBetween(String fromDateISO, String fromTimeZone, String toDateISO, String toTimeZone) {
		LocalDateTime fromDT = LocalDateTime.parse(fromDateISO);
		ZoneId fromZoneId = ZoneId.of(fromTimeZone);
		ZonedDateTime from = ZonedDateTime.of(fromDT, fromZoneId);
		
		LocalDateTime toDT = LocalDateTime.parse(toDateISO);
		ZoneId toZoneId = ZoneId.of(toTimeZone);
		ZonedDateTime to = ZonedDateTime.of(toDT, toZoneId);
		
		long days = java.time.Duration.between(from, to).getSeconds() / 86400;
		return days;
	}
	
	/**
	 * Add a period to a date in ISO format 
	 */
	public String DateISOAddPeriod(String dateISO, String timeZone, int years, int months, int weeks, int days, int hours, int minutes, int seconds) {
		LocalDateTime fromDT = LocalDateTime.parse(dateISO);
		ZoneId zoneId = ZoneId.of(timeZone);
		ZonedDateTime from = ZonedDateTime.of(fromDT, zoneId);
		if (years > 0) {
			from = from.plusYears(years);			
		}
		if (years < 0) {
			from = from.minusYears(years*-1);
		}
		if (months > 0) {
			from = from.plusMonths(months);
		}
		if (months < 0) {
			from = from.minusMonths(months*-1);
		}
		if (weeks > 0) {
			from = from.plusWeeks(weeks);
		}
		if (weeks < 0) {
			from = from.minusWeeks(weeks*-1);
		}
		if (days > 0) {
			from = from.plusDays(days);
		}
		if (days < 0) {
			from = from.minusDays(days*-1);
		}
		if (hours > 0) {
			from = from.plusHours(hours);
		}
		if (hours < 0) {
			from = from.minusHours(hours*-1);
		}
		if (minutes > 0) {
			from = from.plusMinutes(minutes);
		}
		if (minutes < 0) {
			from = from.minusMinutes(minutes*-1);
		}
		if (seconds > 0) {
			from = from.plusSeconds(seconds);
		}
		if (seconds < 0) {
			from = from.minusSeconds(seconds*-1);
		}
		return from.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}
	
	public String GetCurrentDateTimeAsUTCString() {
	    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	    final String utcTime = sdf.format(new Date());

	    return utcTime;
	}
	
	public int GetWeekNum(String input, String locale) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");   // Define formatting pattern to match your input string.
	    LocalDate date = LocalDate.parse(input, formatter);                     // Parse string into a `LocalDate` object.

	    Locale loc = Locale.forLanguageTag(locale);
	    WeekFields wf = WeekFields.of(loc);    // Use week fields appropriate to your locale. People in different places define a week and week-number differently, such as starting on a Monday or a Sunday, and so on.
	    BA.Log(loc.toString());
	    BA.Log(wf.getFirstDayOfWeek().name());
	    BA.Log("MinDaysInWeek: " + wf.getMinimalDaysInFirstWeek());
	    TemporalField weekNum = wf.weekOfWeekBasedYear();                       // Represent the idea of this locale’s definition of week number as a `TemporalField`. 
	    int week = Integer.parseInt(String.format("%02d",date.get(weekNum)));   // Using that locale’s definition of week number, determine the week-number for this particular `LocalDate` value.

	    return week;
	}
	
	public String ListOfLocales() {
		StringBuilder s = new StringBuilder();
		Locale list[] = DateFormat.getAvailableLocales();
        for (Locale aLocale : list) {           
        	s.append(aLocale.toString() + ";" + aLocale.getDisplayName() + ";");
        	WeekFields wf = WeekFields.of(aLocale);    // Use week fields appropriate to your locale. People in different places define a week and week-number differently, such as starting on a Monday or a Sunday, and so on.
    	    s.append(wf.getFirstDayOfWeek().name() + ";");
    	    s.append(wf.getFirstDayOfWeek().getValue() + ";");
    	    s.append(wf.getMinimalDaysInFirstWeek());
    	    s.append("\n");
        }
        return s.toString();
	}
	
	/**
	 * Gets the current timezone id from the users browser 
	 */
	public String GetBrowserTimeZone(ABMPage page) {
		if (page==null) {
			return "";
		}
		SimpleFuture j = page.ws.RunFunctionWithResult("GetBrowserTimeZone", null);		
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
		return "";
	}
	
	/**
	 * Calcualtes the distance in meters between two GEO points 
	 */
	public static double GEOGetDistanceVincentyMeters(double Latitude1, double Longitude1, double Latitude2, double Longitude2) {
	    double a = 6378137, b = 6356752.314245, f = 1 / 298.257223563; // WGS-84 ellipsoid params
	    double L = Math.toRadians(Longitude2 - Longitude1);
	    double U1 = Math.atan((1 - f) * Math.tan(Math.toRadians(Latitude1)));
	    double U2 = Math.atan((1 - f) * Math.tan(Math.toRadians(Latitude2)));
	    double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
	    double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);

	    double sinLambda, cosLambda, sinSigma, cosSigma, sigma, sinAlpha, cosSqAlpha, cos2SigmaM;
	    double lambda = L, lambdaP, iterLimit = 100;
	    do {
	        sinLambda = Math.sin(lambda);
	        cosLambda = Math.cos(lambda);
	        sinSigma = Math.sqrt((cosU2 * sinLambda) * (cosU2 * sinLambda)
	                + (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda) * (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
	        if (sinSigma == 0)
	            return 0; // co-incident points
	        cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
	        sigma = Math.atan2(sinSigma, cosSigma);
	        sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
	        cosSqAlpha = 1 - sinAlpha * sinAlpha;
	        cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha;
	        if (Double.isNaN(cos2SigmaM))
	            cos2SigmaM = 0; // equatorial line: cosSqAlpha=0 (§6)
	        double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
	        lambdaP = lambda;
	        lambda = L + (1 - C) * f * sinAlpha
	                * (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));
	    } while (Math.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0);

	    if (iterLimit == 0)
	        return Double.NaN; // formula failed to converge

	    double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
	    double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
	    double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
	    double deltaSigma = B
	            * sinSigma
	            * (cos2SigmaM + B
	                    / 4
	                    * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) - B / 6 * cos2SigmaM
	                            * (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));
	    double dist = b * A * (sigma - deltaSigma);

	    return dist;
	}
	
	/**
	 * <p>Computes the bounding coordinates of all points on the surface
	 * of a sphere that have a great circle distance to the point represented
	 * by this GeoLocation instance that is less or equal to the distance
	 * argument. Latitude and Longitude must be in radians!</p>
	 * <p>For more information about the formulae used in this method visit
	 * <a href="http://JanMatuschek.de/LatitudeLongitudeBoundingCoordinates">
	 * http://JanMatuschek.de/LatitudeLongitudeBoundingCoordinates</a>.</p>
	 * distance the distance from the point represented by this
	 * GeoLocation instance. Must me measured in the same unit as the radius
	 * argument.
	 * radius the radius of the sphere, e.g. the average radius for a
	 * spherical approximation of the figure of the Earth is approximately
	 * 6371.01 kilometers.
	 * return san array of four doubles (radians) such that:<ul>
	 * <li>The latitude of any point within the specified distance is greater
	 * or equal to the latitude of the first array element and smaller or
	 * equal to the latitude of the third array element.</li>
	 * <li>If the longitude of the first array element is smaller or equal to
	 * the longitude of the third element, then
	 * the longitude of any point within the specified distance is greater
	 * or equal to the longitude of the second array element and smaller or
	 * equal to the longitude of the forth array element.</li>
	 * <li>If the longitude of the second array element is greater than the
	 * longitude of the forth element (this is the case if the 180th
	 * meridian is within the distance), then
	 * the longitude of any point within the specified distance is greater
	 * or equal to the longitude of the second array element
	 * <strong>or</strong> smaller or equal to the longitude of the forth
	 * array element.</li>
	 * </ul>
	 */
	public static anywheresoftware.b4a.objects.collections.List GEOGetBoundingBox(double LatitudeRad, double LongitudeRad, double distance, double radius) {
		anywheresoftware.b4a.objects.collections.List ret = new anywheresoftware.b4a.objects.collections.List();
		ret.Initialize();
		
		if (radius < 0d || distance < 0d)
			throw new IllegalArgumentException();

		// angular distance in radians on a great circle
		double radDist = distance / radius;
		
		double minLat = LatitudeRad - radDist;
		double maxLat = LatitudeRad + radDist;
		
		double minLon, maxLon;
		if (minLat > MIN_LAT && maxLat < MAX_LAT) {
			double deltaLon = Math.asin(Math.sin(radDist) / Math.cos(LatitudeRad));
			minLon = LongitudeRad - deltaLon;
			if (minLon < MIN_LON) minLon += 2d * Math.PI;
			maxLon = LongitudeRad + deltaLon;
			if (maxLon > MAX_LON) maxLon -= 2d * Math.PI;
		} else {
			// a pole is within the distance
			minLat = Math.max(minLat, MIN_LAT);
			maxLat = Math.min(maxLat, MAX_LAT);
			minLon = MIN_LON;
			maxLon = MAX_LON;
		}

		ret.Add(minLat);
		ret.Add(minLon);
		ret.Add(maxLat);
		ret.Add(maxLon);
		
		return ret;
	}
	
	public static double GEOToRadians(double degrees) {
		return Math.toRadians(degrees);
	}
	
	public static double GEOToDegrees(double radians) {
		return Math.toDegrees(radians);
	}
	
	/**
	 * Helper method to create a WHERE clause for an SQL query 
	 */
	public static String GEOPrepareSQLWHEREClause(String LatFieldName, String LonFieldName, double LatitudeRad, double LongitudeRad, double distance, double radius) {
		anywheresoftware.b4a.objects.collections.List ret = GEOGetBoundingBox(LatitudeRad, LongitudeRad, distance, radius);
		double lat0 = (double) ret.Get(0);
		double lat1 = (double) ret.Get(2);
		double lon0 = (double) ret.Get(1);
		double lon1 = (double) ret.Get(3);
		StringBuilder s = new StringBuilder();	
		s.append(" (" + LatFieldName + ">=" + lat0 + " AND " + LatFieldName + "<=" + lat1 + ") ");
		if (lon0 > lon1) {
			s.append("AND (" + LonFieldName + ">=" + lon0 + " OR " + LonFieldName + "<=" + lon1 + ") ");
		} else {
			s.append("AND (" + LonFieldName + ">=" + lon0 + " AND " + LonFieldName + "<=" + lon1 + ") ");
		}
		s.append("AND acos(sin(" + LatitudeRad +") * sin(" + LatFieldName + ") + cos(" + LatitudeRad + ") * cos(" + LatFieldName + ") * cos(" + LonFieldName + " - " + LongitudeRad + ")) <= " + (distance/radius));
		return s.toString();
	}
	
	/**
	 * Helper method to check if a location is in radius
	 */
	public static boolean GEOCheckLocationInRadius(double CenterLatitudeRad, double CenterLongitudeRad, double CheckLatitudeRad, double CheckLongitudeRad, double distance, double radius) {
		anywheresoftware.b4a.objects.collections.List ret = GEOGetBoundingBox(CenterLatitudeRad, CenterLongitudeRad, distance, radius);
		double lat0 = (double) ret.Get(0);
		double lat1 = (double) ret.Get(2);
		double lon0 = (double) ret.Get(1);
		double lon1 = (double) ret.Get(3);
		
		if (lon0 > lon1) {
			return ((CheckLatitudeRad >= lat0 && CheckLatitudeRad <= lat1) && (CheckLongitudeRad >= lon0 || CheckLongitudeRad <= lon1) && 
					Math.acos(Math.sin(CenterLatitudeRad) * Math.sin(CheckLatitudeRad) + Math.cos(CenterLatitudeRad) * Math.cos(CheckLatitudeRad) * Math.cos(CheckLongitudeRad - CenterLongitudeRad)) <= (distance/radius));							
		} else {
			return ((CheckLatitudeRad >= lat0 && CheckLatitudeRad <= lat1) && (CheckLongitudeRad >= lon0 && CheckLongitudeRad <= lon1) && 
					Math.acos(Math.sin(CenterLatitudeRad) * Math.sin(CheckLatitudeRad) + Math.cos(CenterLatitudeRad) * Math.cos(CheckLatitudeRad) * Math.cos(CheckLongitudeRad - CenterLongitudeRad)) <= (distance/radius));
		}		
	}

	/**
	 * Replaces all occurences of a string to another string 
	 */
	public String ReplaceAll(String input, String search, String replace) {
		if (input==null) {
			return "";
		}
		return input.replace(search, replace);
	}
	
	/**
	 * Converts \\uxxxx to the real unicode character 
	 */
	public String UnescapeUnicode(String str) {
		return org.apache.commons.lang3.StringEscapeUtils.unescapeJava(str);
	}
	
	/**
	 * Converts a Unicode character to its \\uxxxx equivalent 
	 */
	public String EscapeUnicode(String str) {
		return org.apache.commons.lang3.StringEscapeUtils.escapeJava(str);
	}
	
	
	public static void CreateThumbnail(String inputImageFullPath, String outputImageFullPath, int width, int height) {
		try {			
			Thumbnails.of(inputImageFullPath)
				.size(width, height)
				.crop(Positions.CENTER)
				.toFile(new File(outputImageFullPath));			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void Zip(anywheresoftware.b4a.objects.collections.List filesToZip, String outputFolder, String outputFileName) throws IOException {
		FileOutputStream fos = new FileOutputStream(anywheresoftware.b4a.objects.streams.File.Combine(outputFolder, outputFileName));
		ZipOutputStream zipOut = new ZipOutputStream(fos);
		for (int i=0;i<filesToZip.getSize();i++) {
			String srcFile = (String) filesToZip.Get(i);
          File fileToZip = new File(srcFile);
          zipFile(fileToZip, fileToZip.getName(), zipOut);
      }
		
      zipOut.close();
      fos.close();
	}
	
	public static void Unzip(String zipFolder,String zipFileName, String outputFolder) {		
		try {
			String fileZip = anywheresoftware.b4a.objects.streams.File.Combine(zipFolder, zipFileName);
			byte[] buffer = new byte[1024];
			ZipInputStream zis;
		
			zis = new ZipInputStream(new FileInputStream(fileZip));
			
			ZipEntry zipEntry = zis.getNextEntry();
			while(zipEntry != null){
				File file = new File(outputFolder, zipEntry.getName());
	            if (zipEntry.isDirectory() && !file.exists()) {
	            	file.mkdirs();
				} else {
					String fileName = zipEntry.getName();
					File newFile = new File(outputFolder + "/" + fileName);
					
					if (!newFile.getParentFile().exists()) {
	                    newFile.getParentFile().mkdirs();
	                }
					
					FileOutputStream fos = new FileOutputStream(newFile);
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					fos.close();
					zipEntry = zis.getNextEntry();
				}
			}
			zis.closeEntry();
			zis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
	
	protected static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) {
		try {
			if (fileToZip.isHidden()) {
				return;
			}
			if (fileToZip.isDirectory()) {
				File[] children = fileToZip.listFiles();
				for (File childFile : children) {
					zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
				}
				return;
			}
			FileInputStream fis = new FileInputStream(fileToZip);
			ZipEntry zipEntry = new ZipEntry(fileName);
			zipOut.putNextEntry(zipEntry);
			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zipOut.write(bytes, 0, length);
			}
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
	
  public String URLEncode(String text, String Charset)	 {
	  return ABMaterial.HTMLConv().htmlEscape(text, Charset);
  }
  
  /**
   * Type, Use one of the ABM.Util.WHITELIST_ constants 
   */
  public String CleanupHTML(String html, int whitelistType) {
	  switch (whitelistType) {
	  	case WHITELIST_NONE:
	  		return Jsoup.clean(html, Whitelist.none());
	  	case WHITELIST_SIMPLETEXT:
	  		return Jsoup.clean(html, Whitelist.simpleText());
	  	case WHITELIST_BASIC:
	  		return Jsoup.clean(html, Whitelist.basic());
	  	case WHITELIST_BASICWITHIMAGES:
	  		return Jsoup.clean(html, Whitelist.basicWithImages());
	  	case WHITELIST_RELAXED:
	  		return Jsoup.clean(html, Whitelist.relaxed());
	  }		
	  return html;
	  
  }  
	
}
