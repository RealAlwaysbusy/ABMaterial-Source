package com.ab.abmaterial;

import java.util.HashMap;
import java.util.Map;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
                               
@Author("Alain Bailleul")  
@ShortName("ABMHtmlUtils")
public class ABMHtmlUtils {
	private static final String DEFAULT_CHARACTER_ENCODING = "ISO-8859-1";
	private HtmlCharacterEntityReferences characterEntityReferences;
	public boolean IsInitialized=false;

	public void Initialize() {
		characterEntityReferences = new HtmlCharacterEntityReferences();
		IsInitialized=true;
	}
	
	/**
	 * Turn special characters into HTML character references.
	 * Handles complete character set defined in HTML 4.01 recommendation.
	 * <p>Escapes all special characters to their corresponding
	 * entity reference (e.g. {@code &lt;}).
	 * <p>Reference:
	 * <a href="http://www.w3.org/TR/html4/sgml/entities.html">
	 * http://www.w3.org/TR/html4/sgml/entities.html
	 * </a>
	 * @param input the (unescaped) input string
	 * @return the escaped string
	 */
	public String htmlEscapeDefault(String input) {
		return htmlEscape(input, DEFAULT_CHARACTER_ENCODING);
	}

	/**
	 * Turn special characters into HTML character references.
	 * Handles complete character set defined in HTML 4.01 recommendation.
	 * <p>Escapes all special characters to their corresponding
	 * entity reference (e.g. {@code &lt;}) at least as required by the
	 * specified encoding. In other words, if a special character does
	 * not have to be escaped for the given encoding, it may not be.
	 * <p>Reference:
	 * <a href="http://www.w3.org/TR/html4/sgml/entities.html">
	 * http://www.w3.org/TR/html4/sgml/entities.html
	 * </a>
	 * @param input the (unescaped) input string
	 * @param encoding The name of a supported {@link java.nio.charset.Charset charset}
	 * @return the escaped string
	 * @since 4.1.2
	 */
	public String htmlEscape(String input, String encoding) {
		//Assert.notNull(encoding, "Encoding is required");
		if (input == null) {
			return null;
		}
		StringBuilder escaped = new StringBuilder(input.length() * 2);
		for (int i = 0; i < input.length(); i++) {
			char character = input.charAt(i);
			String reference = characterEntityReferences.convertToReference(character, encoding);
			if (reference != null) {
				escaped.append(reference);
			}
			else {
				escaped.append(character);
			}
		}
		return escaped.toString();
	}

	/**
	 * Turn special characters into HTML character references.
	 * Handles complete character set defined in HTML 4.01 recommendation.
	 * <p>Escapes all special characters to their corresponding numeric
	 * reference in decimal format (&#<i>Decimal</i>;).
	 * <p>Reference:
	 * <a href="http://www.w3.org/TR/html4/sgml/entities.html">
	 * http://www.w3.org/TR/html4/sgml/entities.html
	 * </a>
	 * @param input the (unescaped) input string
	 * @return the escaped string
	 */
	public String htmlEscapeDecimal(String input) {
		return htmlEscapeDecimal(input, DEFAULT_CHARACTER_ENCODING);
	}

	/**
	 * Turn special characters into HTML character references.
	 * Handles complete character set defined in HTML 4.01 recommendation.
	 * <p>Escapes all special characters to their corresponding numeric
	 * reference in decimal format (&#<i>Decimal</i>;) at least as required by the
	 * specified encoding. In other words, if a special character does
	 * not have to be escaped for the given encoding, it may not be.
	 * <p>Reference:
	 * <a href="http://www.w3.org/TR/html4/sgml/entities.html">
	 * http://www.w3.org/TR/html4/sgml/entities.html
	 * </a>
	 * @param input the (unescaped) input string
	 * @param encoding The name of a supported {@link java.nio.charset.Charset charset}
	 * @return the escaped string
	 * @since 4.1.2
	 */
	public String htmlEscapeDecimal(String input, String encoding) {
		//Assert.notNull(encoding, "Encoding is required");
		if (input == null) {
			return null;
		}
		StringBuilder escaped = new StringBuilder(input.length() * 2);
		for (int i = 0; i < input.length(); i++) {
			char character = input.charAt(i);
			if (characterEntityReferences.isMappedToReference(character, encoding)) {
				escaped.append(HtmlCharacterEntityReferences.DECIMAL_REFERENCE_START);
				escaped.append((int) character);
				escaped.append(HtmlCharacterEntityReferences.REFERENCE_END);
			}
			else {
				escaped.append(character);
			}
		}
		return escaped.toString();
	}

	/**
	 * Turn special characters into HTML character references.
	 * Handles complete character set defined in HTML 4.01 recommendation.
	 * <p>Escapes all special characters to their corresponding numeric
	 * reference in hex format (&#x<i>Hex</i>;).
	 * <p>Reference:
	 * <a href="http://www.w3.org/TR/html4/sgml/entities.html">
	 * http://www.w3.org/TR/html4/sgml/entities.html
	 * </a>
	 * @param input the (unescaped) input string
	 * @return the escaped string
	 */
	public String htmlEscapeHex(String input) {
		return htmlEscapeHex(input, DEFAULT_CHARACTER_ENCODING);
	}

	/**
	 * Turn special characters into HTML character references.
	 * Handles complete character set defined in HTML 4.01 recommendation.
	 * <p>Escapes all special characters to their corresponding numeric
	 * reference in hex format (&#x<i>Hex</i>;) at least as required by the
	 * specified encoding. In other words, if a special character does
	 * not have to be escaped for the given encoding, it may not be.
	 * <p>Reference:
	 * <a href="http://www.w3.org/TR/html4/sgml/entities.html">
	 * http://www.w3.org/TR/html4/sgml/entities.html
	 * </a>
	 * @param input the (unescaped) input string
	 * @param encoding The name of a supported {@link java.nio.charset.Charset charset}
	 * @return the escaped string
	 * @since 4.1.2
	 */
	public String htmlEscapeHex(String input, String encoding) {
		//Assert.notNull(encoding, "Encoding is required");
		if (input == null) {
			return null;
		}
		StringBuilder escaped = new StringBuilder(input.length() * 2);
		for (int i = 0; i < input.length(); i++) {
			char character = input.charAt(i);
			if (characterEntityReferences.isMappedToReference(character, encoding)) {
				escaped.append(HtmlCharacterEntityReferences.HEX_REFERENCE_START);
				escaped.append(Integer.toString(character, 16));
				escaped.append(HtmlCharacterEntityReferences.REFERENCE_END);
			}
			else {
				escaped.append(character);
			}
		}
		return escaped.toString();
	}

	/**
	 * Turn HTML character references into their plain text UNICODE equivalent.
	 * <p>Handles complete character set defined in HTML 4.01 recommendation
	 * and all reference types (decimal, hex, and entity).
	 * <p>Correctly converts the following formats:
	 * <blockquote>
	 * &amp;#<i>Entity</i>; - <i>(Example: &amp;amp;) case sensitive</i>
	 * &amp;#<i>Decimal</i>; - <i>(Example: &amp;#68;)</i><br>
	 * &amp;#x<i>Hex</i>; - <i>(Example: &amp;#xE5;) case insensitive</i><br>
	 * </blockquote>
	 * Gracefully handles malformed character references by copying original
	 * characters as is when encountered.<p>
	 * <p>Reference:
	 * <a href="http://www.w3.org/TR/html4/sgml/entities.html">
	 * http://www.w3.org/TR/html4/sgml/entities.html
	 * </a>
	 * @param input the (escaped) input string
	 * @return the unescaped string
	 */
	public String htmlUnescape(String input) {
		if (input == null) {
			return null;
		}
		return new HtmlCharacterEntityDecoder(characterEntityReferences, input).decode();
	}
	
	@Hide
	public class HtmlCharacterEntityDecoder {

		private static final int MAX_REFERENCE_SIZE = 10;


		private final HtmlCharacterEntityReferences characterEntityReferences;

		private final String originalMessage;

		private final StringBuilder decodedMessage;

		private int currentPosition = 0;

		private int nextPotentialReferencePosition = -1;

		private int nextSemicolonPosition = -2;


		public HtmlCharacterEntityDecoder(HtmlCharacterEntityReferences characterEntityReferences, String original) {
			this.characterEntityReferences = characterEntityReferences;
			this.originalMessage = original;
			this.decodedMessage = new StringBuilder(originalMessage.length());
		}

		public String decode() {
			while (currentPosition < originalMessage.length()) {
				findNextPotentialReference(currentPosition);
				copyCharactersTillPotentialReference();
				processPossibleReference();
			}
			return decodedMessage.toString();
		}

		private void findNextPotentialReference(int startPosition) {
			nextPotentialReferencePosition = Math.max(startPosition, nextSemicolonPosition - MAX_REFERENCE_SIZE);

			do {
				nextPotentialReferencePosition =
						originalMessage.indexOf('&', nextPotentialReferencePosition);

				if (nextSemicolonPosition != -1 &&
						nextSemicolonPosition < nextPotentialReferencePosition)
					nextSemicolonPosition = originalMessage.indexOf(';', nextPotentialReferencePosition + 1);

				boolean isPotentialReference =
						nextPotentialReferencePosition != -1
						&& nextSemicolonPosition != -1
						&& nextPotentialReferencePosition - nextSemicolonPosition < MAX_REFERENCE_SIZE;

				if (isPotentialReference) {
					break;
				}
				if (nextPotentialReferencePosition == -1) {
					break;
				}
				if (nextSemicolonPosition == -1) {
					nextPotentialReferencePosition = -1;
					break;
				}

				nextPotentialReferencePosition = nextPotentialReferencePosition + 1;
			}
			while (nextPotentialReferencePosition != -1);
		}


		private void copyCharactersTillPotentialReference() {
			if (nextPotentialReferencePosition != currentPosition) {
				int skipUntilIndex = nextPotentialReferencePosition != -1 ?
						nextPotentialReferencePosition : originalMessage.length();
				if (skipUntilIndex - currentPosition > 3) {
					decodedMessage.append(originalMessage.substring(currentPosition, skipUntilIndex));
					currentPosition = skipUntilIndex;
				}
				else {
					while (currentPosition < skipUntilIndex)
						decodedMessage.append(originalMessage.charAt(currentPosition++));
				}
			}
		}

		private void processPossibleReference() {
			if (nextPotentialReferencePosition != -1) {
				boolean isNumberedReference = originalMessage.charAt(currentPosition + 1) == '#';
				boolean wasProcessable = isNumberedReference ? processNumberedReference() : processNamedReference();
				if (wasProcessable) {
					currentPosition = nextSemicolonPosition + 1;
				}
				else {
					char currentChar = originalMessage.charAt(currentPosition);
					decodedMessage.append(currentChar);
					currentPosition++;
				}
			}
		}

		private boolean processNumberedReference() {
			boolean isHexNumberedReference =
					originalMessage.charAt(nextPotentialReferencePosition + 2) == 'x' ||
					originalMessage.charAt(nextPotentialReferencePosition + 2) == 'X';
			try {
				int value = (!isHexNumberedReference) ?
						Integer.parseInt(getReferenceSubstring(2)) :
						Integer.parseInt(getReferenceSubstring(3), 16);
				decodedMessage.append((char) value);
				return true;
			}
			catch (NumberFormatException ex) {
				return false;
			}
		}

		private boolean processNamedReference() {
			String referenceName = getReferenceSubstring(1);
			char mappedCharacter = characterEntityReferences.convertToCharacter(referenceName);
			if (mappedCharacter != HtmlCharacterEntityReferences.CHAR_NULL) {
				decodedMessage.append(mappedCharacter);
				return true;
			}
			return false;
		}

		private String getReferenceSubstring(int referenceOffset) {
			return originalMessage.substring(nextPotentialReferencePosition + referenceOffset, nextSemicolonPosition);
		}

	}

	@Hide
	public class HtmlCharacterEntityReferences {
		private static final String DEFAULT_CHARACTER_ENCODING = "ISO-8859-1";
		
		//private static final String PROPERTIES_FILE = "HtmlCharacterEntityReferences.properties";

		static final char REFERENCE_START = '&';

		static final String DECIMAL_REFERENCE_START = "&#";

		static final String HEX_REFERENCE_START = "&#x";

		static final char REFERENCE_END = ';';

		static final char CHAR_NULL = (char) -1;


		private final String[] characterToEntityReferenceMap = new String[3000];

		private final Map<String, Character> entityReferenceToCharacterMap = new HashMap<String, Character>(252);


		/**
		 * Returns a new set of character entity references reflecting the HTML 4.0 character set.
		 */
		public HtmlCharacterEntityReferences() {
			AddEntity(160, "nbsp");
			AddEntity(161, "iexcl");
			AddEntity(162, "cent");
			AddEntity(163, "pound");
			AddEntity(164, "curren");
			AddEntity(165, "yen");
			AddEntity(166, "brvbar");
			AddEntity(167, "sect");
			AddEntity(168, "uml");
			AddEntity(169, "copy");
			AddEntity(170, "ordf");
			AddEntity(171, "laquo");
			AddEntity(172, "not");
			AddEntity(173, "shy");
			AddEntity(174, "reg");
			AddEntity(175, "macr");
			AddEntity(176, "deg");
			AddEntity(177, "plusmn");
			AddEntity(178, "sup2");
			AddEntity(179, "sup3");
			AddEntity(180, "acute");
			AddEntity(181, "micro");
			AddEntity(182, "para");
			AddEntity(183, "middot");
			AddEntity(184, "cedil");
			AddEntity(185, "sup1");
			AddEntity(186, "ordm");
			AddEntity(187, "raquo");
			AddEntity(188, "frac14");
			AddEntity(189, "frac12");
			AddEntity(190, "frac34");
			AddEntity(191, "iquest");
			AddEntity(192, "Agrave");
			AddEntity(193, "Aacute");
			AddEntity(194, "Acirc");
			AddEntity(195, "Atilde");
			AddEntity(196, "Auml");
			AddEntity(197, "Aring");
			AddEntity(198, "AElig");
			AddEntity(199, "Ccedil");
			AddEntity(200, "Egrave");
			AddEntity(201, "Eacute");
			AddEntity(202, "Ecirc");
			AddEntity(203, "Euml");
			AddEntity(204, "Igrave");
			AddEntity(205, "Iacute");
			AddEntity(206, "Icirc");
			AddEntity(207, "Iuml");
			AddEntity(208, "ETH");
			AddEntity(209, "Ntilde");
			AddEntity(210, "Ograve");
			AddEntity(211, "Oacute");
			AddEntity(212, "Ocirc");
			AddEntity(213, "Otilde");
			AddEntity(214, "Ouml");
			AddEntity(215, "times");
			AddEntity(216, "Oslash");
			AddEntity(217, "Ugrave");
			AddEntity(218, "Uacute");
			AddEntity(219, "Ucirc");
			AddEntity(220, "Uuml");
			AddEntity(221, "Yacute");
			AddEntity(222, "THORN");
			AddEntity(223, "szlig");
			AddEntity(224, "agrave");
			AddEntity(225, "aacute");
			AddEntity(226, "acirc");
			AddEntity(227, "atilde");
			AddEntity(228, "auml");
			AddEntity(229, "aring");
			AddEntity(230, "aelig");
			AddEntity(231, "ccedil");
			AddEntity(232, "egrave");
			AddEntity(233, "eacute");
			AddEntity(234, "ecirc");
			AddEntity(235, "euml");
			AddEntity(236, "igrave");
			AddEntity(237, "iacute");
			AddEntity(238, "icirc");
			AddEntity(239, "iuml");
			AddEntity(240, "eth");
			AddEntity(241, "ntilde");
			AddEntity(242, "ograve");
			AddEntity(243, "oacute");
			AddEntity(244, "ocirc");
			AddEntity(245, "otilde");
			AddEntity(246, "ouml");
			AddEntity(247, "divide");
			AddEntity(248, "oslash");
			AddEntity(249, "ugrave");
			AddEntity(250, "uacute");
			AddEntity(251, "ucirc");
			AddEntity(252, "uuml");
			AddEntity(253, "yacute");
			AddEntity(254, "thorn");
			AddEntity(255, "yuml");
			AddEntity(402, "fnof");
			AddEntity(913, "Alpha");
			AddEntity(914, "Beta");
			AddEntity(915, "Gamma");
			AddEntity(916, "Delta");
			AddEntity(917, "Epsilon");
			AddEntity(918, "Zeta");
			AddEntity(919, "Eta");
			AddEntity(920, "Theta");
			AddEntity(921, "Iota");
			AddEntity(922, "Kappa");
			AddEntity(923, "Lambda");
			AddEntity(924, "Mu");
			AddEntity(925, "Nu");
			AddEntity(926, "Xi");
			AddEntity(927, "Omicron");
			AddEntity(928, "Pi");
			AddEntity(929, "Rho");
			AddEntity(931, "Sigma");
			AddEntity(932, "Tau");
			AddEntity(933, "Upsilon");
			AddEntity(934, "Phi");
			AddEntity(935, "Chi");
			AddEntity(936, "Psi");
			AddEntity(937, "Omega");
			AddEntity(945, "alpha");
			AddEntity(946, "beta");
			AddEntity(947, "gamma");
			AddEntity(948, "delta");
			AddEntity(949, "epsilon");
			AddEntity(950, "zeta");
			AddEntity(951, "eta");
			AddEntity(952, "theta");
			AddEntity(953, "iota");
			AddEntity(954, "kappa");
			AddEntity(955, "lambda");
			AddEntity(956, "mu");
			AddEntity(957, "nu");
			AddEntity(958, "xi");
			AddEntity(959, "omicron");
			AddEntity(960, "pi");
			AddEntity(961, "rho");
			AddEntity(962, "sigmaf");
			AddEntity(963, "sigma");
			AddEntity(964, "tau");
			AddEntity(965, "upsilon");
			AddEntity(966, "phi");
			AddEntity(967, "chi");
			AddEntity(968, "psi");
			AddEntity(969, "omega");
			AddEntity(977, "thetasym");
			AddEntity(978, "upsih");
			AddEntity(982, "piv");
			AddEntity(8226, "bull");
			AddEntity(8230, "hellip");
			AddEntity(8242, "prime");
			AddEntity(8243, "Prime");
			AddEntity(8254, "oline");
			AddEntity(8260, "frasl");
			AddEntity(8472, "weierp");
			AddEntity(8465, "image");
			AddEntity(8476, "real");
			AddEntity(8482, "trade");
			AddEntity(8501, "alefsym");
			AddEntity(8592, "larr");
			AddEntity(8593, "uarr");
			AddEntity(8594, "rarr");
			AddEntity(8595, "darr");
			AddEntity(8596, "harr");
			AddEntity(8629, "crarr");
			AddEntity(8656, "lArr");
			AddEntity(8657, "uArr");
			AddEntity(8658, "rArr");
			AddEntity(8659, "dArr");
			AddEntity(8660, "hArr");
			AddEntity(8704, "forall");
			AddEntity(8706, "part");
			AddEntity(8707, "exist");
			AddEntity(8709, "empty");
			AddEntity(8711, "nabla");
			AddEntity(8712, "isin");
			AddEntity(8713, "notin");
			AddEntity(8715, "ni");
			AddEntity(8719, "prod");
			AddEntity(8721, "sum");
			AddEntity(8722, "minus");
			AddEntity(8727, "lowast");
			AddEntity(8730, "radic");
			AddEntity(8733, "prop");
			AddEntity(8734, "infin");
			AddEntity(8736, "ang");
			AddEntity(8743, "and");
			AddEntity(8744, "or");
			AddEntity(8745, "cap");
			AddEntity(8746, "cup");
			AddEntity(8747, "int");
			AddEntity(8756, "there4");
			AddEntity(8764, "sim");
			AddEntity(8773, "cong");
			AddEntity(8776, "asymp");
			AddEntity(8800, "ne");
			AddEntity(8801, "equiv");
			AddEntity(8804, "le");
			AddEntity(8805, "ge");
			AddEntity(8834, "sub");
			AddEntity(8835, "sup");
			AddEntity(8836, "nsub");
			AddEntity(8838, "sube");
			AddEntity(8839, "supe");
			AddEntity(8853, "oplus");
			AddEntity(8855, "otimes");
			AddEntity(8869, "perp");
			AddEntity(8901, "sdot");
			AddEntity(8968, "lceil");
			AddEntity(8969, "rceil");
			AddEntity(8970, "lfloor");
			AddEntity(8971, "rfloor");
			AddEntity(9001, "lang");
			AddEntity(9002, "rang");
			AddEntity(9674, "loz");
			AddEntity(9824, "spades");
			AddEntity(9827, "clubs");
			AddEntity(9829, "hearts");
			AddEntity(9830, "diams");
			AddEntity(34, "quot");
			AddEntity(38, "amp");
			AddEntity(39, "#39");
			AddEntity(60, "lt");
			AddEntity(62, "gt");
			AddEntity(338, "OElig");
			AddEntity(339, "oelig");
			AddEntity(352, "Scaron");
			AddEntity(353, "scaron");
			AddEntity(376, "Yuml");
			AddEntity(710, "circ");
			AddEntity(732, "tilde");
			AddEntity(8194, "ensp");
			AddEntity(8195, "emsp");
			AddEntity(8201, "thinsp");
			AddEntity(8204, "zwnj");
			AddEntity(8205, "zwj");
			AddEntity(8206, "lrm");
			AddEntity(8207, "rlm");
			AddEntity(8211, "ndash");
			AddEntity(8212, "mdash");
			AddEntity(8216, "lsquo");
			AddEntity(8217, "rsquo");
			AddEntity(8218, "sbquo");
			AddEntity(8220, "ldquo");
			AddEntity(8221, "rdquo");
			AddEntity(8222, "bdquo");
			AddEntity(8224, "dagger");
			AddEntity(8225, "Dagger");
			AddEntity(8240, "permil");
			AddEntity(8249, "lsaquo");
			AddEntity(8250, "rsaquo");
			AddEntity(8364, "euro");
		}

		private void AddEntity(int referredChar, String reference ) {
			int index = (referredChar < 1000 ? referredChar : referredChar - 7000);
			this.characterToEntityReferenceMap[index] = REFERENCE_START + reference + REFERENCE_END;
			this.entityReferenceToCharacterMap.put(reference, (char) referredChar);
		}
		

		/**
		 * Return the number of supported entity references.
		 */
		public int getSupportedReferenceCount() {
			return this.entityReferenceToCharacterMap.size();
		}

		/**
		 * Return true if the given character is mapped to a supported entity reference.
		 */
		public boolean isMappedToReference(char character) {
			return isMappedToReference(character, DEFAULT_CHARACTER_ENCODING);
		}

		/**
		 * Return true if the given character is mapped to a supported entity reference.
		 */
		public boolean isMappedToReference(char character, String encoding) {
			return (convertToReference(character, encoding) != null);
		}

		/**
		 * Return the reference mapped to the given character or {@code null}.
		 */
		public String convertToReference(char character) {
		   return convertToReference(character, DEFAULT_CHARACTER_ENCODING);
		}

		/**
		 * Return the reference mapped to the given character or {@code null}.
		 * @since 4.1.2
		 */
		public String convertToReference(char character, String encoding) {
			if (encoding.startsWith("UTF-")){
				switch (character){
					case '<':
						return "&lt;";
					case '>':
						return "&gt;";
					case '"':
						return "&quot;";
					case '&':
						return "&amp;";
					case '\'':
						return "&#39;";
				}
			}
			else if (character < 1000 || (character >= 8000 && character < 10000)) {
				int index = (character < 1000 ? character : character - 7000);
				String entityReference = this.characterToEntityReferenceMap[index];
				if (entityReference != null) {
					return entityReference;
				}
			}
			return null;
		}

		/**
		 * Return the char mapped to the given entityReference or -1.
		 */
		public char convertToCharacter(String entityReference) {
			Character referredCharacter = this.entityReferenceToCharacterMap.get(entityReference);
			if (referredCharacter != null) {
				return referredCharacter;
			}
			return CHAR_NULL;
		}

	}

}

