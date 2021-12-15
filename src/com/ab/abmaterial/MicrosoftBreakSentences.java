package com.ab.abmaterial;

import java.net.URL;
import java.net.URLEncoder;
import anywheresoftware.b4a.BA.Hide;

@Hide
public final class MicrosoftBreakSentences extends MicrosoftTranslatorAPI {

	private static final String SERVICE_URL = "http://api.microsofttranslator.com/V2/Ajax.svc/BreakSentences?";

	// prevent instantiation
	private MicrosoftBreakSentences(){};
	/**
	 * Reports the number of sentences detected and the length of those sentences
	 * 
	 * @param text The String to break into sentences
	 * @param fromLang The Language of origin
	 * @return an array of integers representing the size of each detected sentence
	 * @throws Exception on error.
	 */
	public static Integer[] execute(final String text, final String fromLang) throws Exception {
        //Run the basic service validations first
        validateServiceState(text,fromLang); 
		final URL url = new URL(SERVICE_URL 
                        +(apiKey != null ? PARAM_APP_ID + URLEncoder.encode(apiKey,ENCODING) : "") 
                        +PARAM_SENTENCES_LANGUAGE+URLEncoder.encode(fromLang.toString(), ENCODING)
                        +PARAM_TEXT_SINGLE+URLEncoder.encode(text, ENCODING));
                     
		final Integer[] response = retrieveIntArray(url);
		return response;
	}
	
	private static void validateServiceState(final String text, final String fromLang) throws Exception {
		final int byteLength = text.getBytes(ENCODING).length;
        if(byteLength>10240) {
            throw new RuntimeException("TEXT_TOO_LARGE - Microsoft Translator (BreakSentences) can handle up to 10,240 bytes per request");
        }
        if(fromLang.equals("")) {
        	throw new RuntimeException("BreakSentences does not support AUTO_DETECT Langauge. Please specify the origin language");
        }
        validateServiceState();
    }
}
