package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Hide;

@Hide
public class GoogleTranslateAPIException extends Exception {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1904924954995479356L;
	
	public GoogleTranslateAPIException(final String message) {
		super(message);
	}

	public GoogleTranslateAPIException(final Exception e) {
		super(e);
	}

}
