package com.ab.abmaterial;

import java.net.URL;
import java.net.URLEncoder;
import anywheresoftware.b4a.BA.Hide;

@Hide
public final class MicrosoftDetect extends MicrosoftTranslatorAPI {
    private static final String SERVICE_URL = "http://api.microsofttranslator.com/V2/Ajax.svc/Detect?";
    private static final String ARRAY_SERVICE_URL = "http://api.microsofttranslator.com/V2/Ajax.svc/DetectArray?";
    
    // prevent instantiation
    private MicrosoftDetect(){};
    /**
	 * Detects the language of a supplied String.
	 * 
	 * @param text The String to detect the language of.
	 * @return A String containing the language
	 * @throws Exception on error.
	 */
	public static String execute(final String text) throws Exception {
        //Run the basic service validations first
        validateServiceState(text); 
		final URL url = new URL(SERVICE_URL 
                        +(apiKey != null ? PARAM_APP_ID + URLEncoder.encode(apiKey,ENCODING) : "") 
                        +PARAM_TEXT_SINGLE+URLEncoder.encode(text, ENCODING));
                     
		final String response = retrieveString(url);
        return response;
	}
        
     /**
	 * Detects the language of all supplied Strings in array.
	 * 
	 * @param text The Strings to detect the language of.
	 * @return A String array containing the detected languages
	 * @throws Exception on error.
	 */
	public static String[] execute(final String[] texts) throws Exception {
        //Run the basic service validations first
        validateServiceState(texts); 
        final String textArr = buildStringArrayParam(texts);
		final URL url = new URL(ARRAY_SERVICE_URL 
                        +(apiKey != null ? PARAM_APP_ID + URLEncoder.encode(apiKey,ENCODING) : "") 
                        +PARAM_TEXT_ARRAY+URLEncoder.encode(textArr, ENCODING));
		final String[] response = retrieveStringArr(url);
                return response;
	}
        
    private static void validateServiceState(final String text) throws Exception {
    	final int byteLength = text.getBytes(ENCODING).length;
        if(byteLength>10240) {
            throw new RuntimeException("TEXT_TOO_LARGE - Microsoft Translator (Detect) can handle up to 10,240 bytes per request");
        }
        validateServiceState();
    }
        
    private static void validateServiceState(final String[] texts) throws Exception {
    	int length = 0;
        for(String text : texts) {
            length+=text.getBytes(ENCODING).length;
        }
        if(length>10240) {
            throw new RuntimeException("TEXT_TOO_LARGE - Microsoft Translator (Detect) can handle up to 10,240 bytes per request");
        }
        validateServiceState();
    }

}

