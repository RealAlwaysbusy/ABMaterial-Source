package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Hide;

import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

@Hide
public final class GoogleTranslate extends GoogleTranslateAPI {
	/**
	 * Constants.
	 */
	private static final String URL = "https://www.googleapis.com/language/translate/v2";
	private static final String PAR_TEMPLATE = "key=%s&q=%s&target=%s";

	public String execute(final String text, final String from, final String to) throws GoogleTranslateAPIException {
		try {
			validateReferrer();

			if (key == null) {
				throw new IllegalStateException("You MUST have a Google API Key to use the V2 APIs. See http://code.google.com/apis/language/translate/v2/getting_started.html");
			}

			final String parameters = String.format(PAR_TEMPLATE, key, URLEncoder.encode(text, ENCODING), to.toString())
					+ (from.equals("") ? "" : String.format("&source=%s", from));

			final URL url = new URL(URL);

			final JSONObject json = retrieveJSON(url,parameters);

			return getJSONResponse(json);
		} catch (final Exception e) {
			System.out.println("Error: " +e.getMessage());

			throw new GoogleTranslateAPIException(e);
		}
	}

	public String[] execute(final String[] text, final String from, final String to) throws GoogleTranslateAPIException {
		try {
			validateReferrer();
			
			final String[] fromArgs = new String[text.length];
			final String[] toArgs = new String[text.length];
			
			for (int i = 0; i<text.length; i++) {
				fromArgs[i] = from;
				toArgs[i] = to;
			}
			
			return execute(text, fromArgs, toArgs);
		} catch (final Exception e) {
			throw new GoogleTranslateAPIException(e);
		}
	}

	public String[] execute(final String text, final String from, final String[] to) throws GoogleTranslateAPIException {
		try {
			validateReferrer();
			
			final String[] textArgs = new String[to.length];
			final String[] fromArgs = new String[to.length];
			
			for (int i = 0; i<to.length; i++) {
				textArgs[i] = text;
				fromArgs[i] = from;
			}
			
			return execute(textArgs, fromArgs, to);
		} catch (final Exception e) {
			throw new GoogleTranslateAPIException(e);
		}
	}
	
	public String[] execute(final String[] text, final String from[], final String[] to) throws GoogleTranslateAPIException {
		try {
			validateReferrer();
			
			if (text.length != from.length || from.length != to.length) {
				throw new Exception(
						"[google-api-translate-java] The same number of texts, from and to languages must be supplied.");
			}
			
			if (text.length == 1) {
				return new String[] { execute(text[0], from[0], to[0]) };
			}
			
			final String[] responses = new String[text.length];
			for (int i = 0; i < responses.length; i++) {
				responses[i] = execute(text[i], from[i], to[i]);
			}
			
			return responses;
		} catch (final Exception e) {
			throw new GoogleTranslateAPIException(e);
		}
	}
	
	/**
	 * Returns the JSON response data as a String. Throws an exception if the status is not a 200 OK.
	 * 
	 * @param json The JSON object to retrieve the response data from.
	 * @return The responseData from the JSONObject.
	 * @throws Exception If the responseStatus is not 200 OK.
	 */
	private static String getJSONResponse(final JSONObject json) throws Exception {
		final JSONObject data = json.getJSONObject("data");
		final JSONArray translations = data.getJSONArray("translations");
		final JSONObject translation = translations.getJSONObject(0);
		final String translatedText = translation.getString("translatedText");
		
		//return HTMLEntities.unhtmlentities(translatedText);
		return ABMaterial.HTMLConv().htmlUnescape(translatedText);
	}

}
