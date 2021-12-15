package com.ab.abmaterial;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.objects.collections.List;


@Author("Alain Bailleul")  
@ShortName("ABMReportBlock")
public class ABMReportBlock {
	protected ABMReport mGroup;
	protected String mQuery="";
	protected List mTexts = new List();
	protected List mWidths = new List();
	protected List mClassesOpen = new List();
	protected List mClassesClosed = new List();
	protected List mRaws = new List();
	protected String mRowClassesOpen="";
	protected String mRowClassesClosed="";
	protected ABMContainer mContainer;
	protected BA mBA=null;
	protected String mEventName="";
	protected Object mCaller=null;

	public String Visibility=ABMaterial.VISIBILITY_ALL;
	protected String mIsPrintableClass="";
	protected String mPageBreak="";
	
	protected int BlockType=0;
	protected boolean IsType2=false;
	protected boolean IsHeader=false;
	protected boolean IsFooter=false;
	protected String mExtraStyleRow="";
		
	protected String mDataValue1="";
	protected String mDataValue2="";
	protected String mDataValue3="";
	protected String mDataValue4="";
	protected String mDataValue5="";
	
	protected String mCollapseState="";
	
	protected int dataLen=0;
	
	protected List mVisibleIfOpen = new List();
	protected List mVisibleIfClosed = new List();
	
	protected int mLineType=0;
	protected boolean IsPageBreak=false;
	
	protected String mExtraClasses="";
		
	/**
	 * Enable/Disable this component from being printed 
	 */
	public void setIsPrintable(Boolean printme) {
		if (printme) {
			mIsPrintableClass="";
		} else {
			mIsPrintableClass=" no-print ";
		}
	}
	
	public boolean getIsPrintable() {
		return mIsPrintableClass.equals("");
	}
	
	/**
	 * pageBreak: use the ABMaterial.PRINT_PAGEBREAK_ constants, empty string is default
	 */
	public void InitializeAsTexts(List texts, List widthPercentages, List cssClasses, String pageBreak, String ExtraStyleRow) {
		int sum=0;
		for (int i=0;i<widthPercentages.getSize();i++) {
			int val = (Integer) widthPercentages.Get(i);
			sum+=val;
		}
		if (sum!=100) {
			BA.Log("Warning: widthPercentages do not add up to 100!");
		}
		if (texts.getSize()!=widthPercentages.getSize()) {
			BA.Log("Error: widthPercentages not the same size as texts!");
			return;
		}
		if (texts.getSize()!=cssClasses.getSize()) {
			BA.Log("Error: cssClasses not the same size as texts!");
			return;
		}
		mTexts.Initialize2(texts);
		mRaws.Initialize();
		for (int i=0;i<mTexts.getSize();i++) {
			mRaws.Add(false);
		}
		mWidths.Initialize2(widthPercentages);
		mClassesOpen.Initialize2(cssClasses);
		mClassesClosed.Initialize2(cssClasses);
		mVisibleIfOpen.Initialize();
		mVisibleIfClosed.Initialize();
		mDataValue1 = "";
		mDataValue2 = "";
		mDataValue3 = "";
		mDataValue4 = "";
		mDataValue5 = "";
		mExtraStyleRow = ExtraStyleRow;
		BlockType = 1;
		mPageBreak = pageBreak;
	}
	
	public void SetColumnRaw(int index, String HTMLRaw) {
		mTexts.Set(index, HTMLRaw);
		mRaws.Set(index,true);
	}
	
	/**
	 * pageBreak: use the ABMaterial.PRINT_PAGEBREAK_ constants, empty string is default
	 * Has DataValues so you can collapse this specific line (max 5!).
	 * They will be in the html as data-r1, data-r2, data-r3, data-r4 and data-r5
	 */
	public void InitializeAsTextsCollapsable(List texts, List widthPercentages, String pageBreak, String ExtraStyleRow, String RowCSSClassesOpen, String RowCSSClassesClosed, List RowDataValues, List visibleIfOpen, List visibleIfClosed, List cssClassesOpen, List cssClassesClosed, String collapseState, int LineType) {
		int sum=0;
		for (int i=0;i<widthPercentages.getSize();i++) {
			int val = (Integer) widthPercentages.Get(i);
			sum+=val;
		}
		if (sum!=100) {
			BA.Log("Warning: widthPercentages do not add up to 100!");
		}
		if (texts.getSize()!=widthPercentages.getSize()) {
			BA.Log("Error: widthPercentages not the same size as texts!");
			return;
		}
		if (texts.getSize()!=cssClassesOpen.getSize()) {
			BA.Log("Error: cssClasses not the same size as texts!");
			return;
		}
		if (texts.getSize()!=cssClassesClosed.getSize()) {
			BA.Log("Error: cssClasses not the same size as texts!");
			return;
		}
		mTexts.Initialize2(texts);
		mRaws.Initialize();
		for (int i=0;i<mTexts.getSize();i++) {
			mRaws.Add(false);
		}
		mWidths.Initialize2(widthPercentages);
		mRowClassesOpen = RowCSSClassesOpen;
		mRowClassesClosed = RowCSSClassesClosed;
		mClassesOpen.Initialize2(cssClassesOpen);
		mClassesClosed.Initialize2(cssClassesClosed);
		mVisibleIfOpen.Initialize2(visibleIfOpen);
		mVisibleIfClosed.Initialize2(visibleIfClosed);
		mCollapseState = collapseState;
		mLineType=LineType;
		dataLen = RowDataValues.getSize();
		mPageBreak = pageBreak;
		if (dataLen>0) mDataValue1 = (String)RowDataValues.Get(0);
		if (dataLen>1) mDataValue2 = (String)RowDataValues.Get(1);
		if (dataLen>2) mDataValue3 = (String)RowDataValues.Get(2);
		if (dataLen>3) mDataValue4 = (String)RowDataValues.Get(3);
		if (dataLen>4) mDataValue5 = (String)RowDataValues.Get(4);
		mExtraStyleRow = ExtraStyleRow;		
		BlockType = 1;
	}
	
	/**
	 * pageBreak: use the ABMaterial.PRINT_PAGEBREAK_ constants, empty string is default
	 */
	public ABMContainer InitializeAsContainer(ABMPage page, String id, String themeName, String pageBreak, String extraClasses) { //, boolean visibleIfOpen, boolean visibleIfClosed, String cssClassesOpen, String cssClassesClosed, String collapseState) {
		mContainer = new ABMContainer();
		mContainer.Initialize(page, id, themeName);
		mContainer.mPrintPageBreak = pageBreak;
		mExtraClasses = extraClasses;
		mVisibleIfOpen.Initialize();
		mVisibleIfClosed.Initialize();
		mClassesOpen.Initialize();
		mClassesClosed.Initialize();
		mRaws.Initialize();
		/*
		mVisibleIfOpen.Initialize();
		mVisibleIfOpen.Add(visibleIfOpen);
		mVisibleIfClosed.Initialize();
		mVisibleIfClosed.Add(visibleIfClosed);
		mClassesOpen.Initialize();
		mClassesOpen.Add(cssClassesOpen);
		mClassesClosed.Initialize();
		mClassesClosed.Add(cssClassesClosed);
		mCollapseState = collapseState;
		*/
		BlockType = 3;
		return mContainer;
	}	
	
	protected void setSubReport(ABMReport group) {
		BlockType = 4;
		mGroup=group;
	}
	
	
	protected String GetDataValue() {
		switch (dataLen) {
		case 0:
			return "";			
		case 1:
			return " data-r1=\"" + mDataValue1 + "\"";			
		case 2:
			return " data-r1=\"" + mDataValue1 + "\" data-r2=\"" + mDataValue2 + "\"";			
		case 3:
			return " data-r1=\"" + mDataValue1 + "\" data-r2=\"" + mDataValue2 + "\" data-r3=\"" + mDataValue3 + "\"";			
		case 4:
			return " data-r1=\"" + mDataValue1 + "\" data-r2=\"" + mDataValue2 + "\" data-r3=\"" + mDataValue3 + "\" data-r4=\"" + mDataValue4 + "\"";
		case 5:
			return " data-r1=\"" + mDataValue1 + "\" data-r2=\"" + mDataValue2 + "\" data-r3=\"" + mDataValue3 + "\" data-r4=\"" + mDataValue4 + "\" data-r5=\"" + mDataValue5 + "\"";	
		}
		return "";
	}
	
	protected String Build(ABMPage page) {
		StringBuilder s = new StringBuilder();
		
		switch (BlockType) {
		case 1:
			if (IsHeader && IsType2) {
				s.append("<div class='report-fixedtop'>");
			}
			if (IsFooter && IsType2) {
				s.append("<div class='report-fixedbottom'>");
			}
			
			s.append("<div style='display:table;width:100%;" + mExtraStyleRow + "' class=\"" + mPageBreak + "\">");
				
			String isCollapsable="";
			if (!this.mCollapseState.equals("")) {
				isCollapsable = "repcol ";
			}
			String mRowClasses="";
			switch (this.mCollapseState) {
			case "":
				mRowClasses = mRowClassesOpen;
				break;
			case "open":
				mRowClasses = mRowClassesOpen;
				break;
			case "closed":
				mRowClasses = mRowClassesClosed;
				break;	
			}	
			switch (dataLen) {
			case 0:
				s.append("<div class=\"reportrow " + isCollapsable + mIsPrintableClass + " " + mRowClasses + " " + Visibility + "\">");
				break;
			case 1:
				s.append("<div data-reptype=\"" + mLineType + "\" class=\"reportrow " + isCollapsable + mIsPrintableClass + " " + mRowClasses + " " + Visibility + "\" data-r1=\"" + mDataValue1 + "\">");
				break;
			case 2:
				s.append("<div data-reptype=\"" + mLineType + "\" class=\"reportrow " + isCollapsable + mIsPrintableClass + " " + mRowClasses + " " + Visibility + "\" data-r1=\"" + mDataValue1 + "\" data-r2=\"" + mDataValue2 + "\">");
				break;
			case 3:
				s.append("<div data-reptype=\"" + mLineType + "\" class=\"reportrow " + isCollapsable + mIsPrintableClass + " " + mRowClasses + " " + Visibility + "\" data-r1=\"" + mDataValue1 + "\" data-r2=\"" + mDataValue2 + "\" data-r3=\"" + mDataValue3 + "\">");
				break;
			case 4:
				s.append("<div data-reptype=\"" + mLineType + "\" class=\"reportrow " + isCollapsable + mIsPrintableClass + " " + mRowClasses + " " + Visibility + "\" data-r1=\"" + mDataValue1 + "\" data-r2=\"" + mDataValue2 + "\" data-r3=\"" + mDataValue3 + "\" data-r4=\"" + mDataValue4 + "\">");
				break;
			case 5:
				s.append("<div data-reptype=\"" + mLineType + "\" class=\"reportrow " + isCollapsable + mIsPrintableClass + " " + mRowClasses + " " + Visibility + "\" data-r1=\"" + mDataValue1 + "\" data-r2=\"" + mDataValue2 + "\" data-r3=\"" + mDataValue3 + "\" data-r4=\"" + mDataValue4 + "\" data-r5=\"" + mDataValue5 + "\">");
				break;	
			}
			switch (this.mCollapseState) {
			case "":
				for (int j=0;j<mTexts.getSize();j++) {
					boolean isRaw = (boolean)mRaws.Get(j);
					if (isRaw) {
						s.append("<div class=\"reportcol " + mClassesOpen.Get(j) + "\" style=\"width:" + (Integer)mWidths.Get(j) + "%;\">" + (String) mTexts.Get(j) + "</div>");
					} else {
						s.append("<div class=\"reportcol " + mClassesOpen.Get(j) + "\" style=\"width:" + (Integer)mWidths.Get(j) + "%;\">" + BuildBody((String) mTexts.Get(j), page) + "</div>");
					}
				}
				break;
			case "open":
				if (dataLen>0) {
					s.append("<h6 class=\"transparent no-print repicon\"><i data-r0=\"1\" onclick=\"togglerep(this)\" class=\"fa fa-minus-circle repicon-i\"></i>&nbsp;</h6>");								
				}								
				for (int j=0;j<mTexts.getSize();j++) {
					String collapseVis="";
					if (!(Boolean) mVisibleIfOpen.Get(j)) {
						collapseVis=" hide no-print";
					}
					boolean isRaw = (boolean)mRaws.Get(j);
					if (isRaw) {
						s.append("<div class=\"reportcol " + mClassesOpen.Get(j) + collapseVis + "\" style=\"width:" + (Integer)mWidths.Get(j) + "%;\">" + (String) mTexts.Get(j) + "</div>");
					} else {
						s.append("<div class=\"reportcol " + mClassesOpen.Get(j) + collapseVis + "\" style=\"width:" + (Integer)mWidths.Get(j) + "%;\">" + BuildBody((String) mTexts.Get(j), page) + "</div>");
					}
				}
				break;
			case "closed":
				if (dataLen>0) {
					s.append("<h6 class=\"transparent no-print repicon\"><i data-r0=\"1\" onclick=\"togglerep(this)\" class=\"fa fa-plus-circle repicon-i\"></i>&nbsp;</h6>");							
				}
				
				for (int j=0;j<mTexts.getSize();j++) {
					String collapseVis="";
					if (!(Boolean) mVisibleIfClosed.Get(j)) {
						collapseVis=" hide no-print";
					}
					boolean isRaw = (boolean)mRaws.Get(j);
					if (isRaw) {
						s.append("<div class=\"reportcol " + mClassesClosed.Get(j) + collapseVis + "\" style=\"width:" + (Integer)mWidths.Get(j) + "%;\">" + (String) mTexts.Get(j) + "</div>");
					} else {
						s.append("<div class=\"reportcol " + mClassesClosed.Get(j) + collapseVis + "\" style=\"width:" + (Integer)mWidths.Get(j) + "%;\">" + BuildBody((String) mTexts.Get(j), page) + "</div>");
					}
				}
				break;	
			}
			
			s.append("</div>");
			s.append("</div>");
			if (IsType2) {
				s.append("</div>");
			}
			break;
		case 2:			
			@SuppressWarnings("unused") boolean ret = (Boolean) mBA.raiseEvent(mCaller, mEventName + "_runquery", new Object[] {mQuery});						
			break;			
		case 3:
			if (IsHeader && IsType2) {
				s.append("<div class='report-fixedtop'>");
			}
			if (IsFooter && IsType2) {
				s.append("<div class='report-fixedbottom'>");
			}
			if (!mIsPrintableClass.equals("")) {
				mContainer.setIsPrintable(false);
			} else {
				mContainer.setIsPrintable(true);
			}
			if (mContainer.mVisibility.equals("")) {
				mContainer.mVisibility = Visibility;
			}
			s.append(mContainer.Build());
			if (IsType2) {
				s.append("</div>");
			}
			
			break;
		case 4:
			s.append("<div class=\"" + mIsPrintableClass + " " + Visibility + "\" style=\"" + mGroup.mPageBreak + ";" + mGroup.mExtraStyle + "\"><div style=\"display: table;width: 100%\">" + mGroup.RunInternal(page) + "</div></div>");
			break;
		}		
		
		return s.toString();
	}
	
	protected void FirstRun() {
		switch (BlockType) {
		case 1:
			
			break;
		case 2:
			
			break;
		case 3:			
			mContainer.RunAllFirstRunsInternal(false);
			break;
		case 4:
			mGroup.FirstRun();
			break;
		}		
	}
	
	public String BuildFromFormattedString(String text, ABMPage page) {
		return BuildBody(text, page);
	}
	
	protected String BuildBody(String text, ABMPage page) {
		StringBuilder s = new StringBuilder();	
		
		String v = ABMaterial.HTMLConv().htmlEscape(text, page.PageCharset);
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
				repl = "<i>" + page.svgIconmap.getOrDefault(IconName.toLowerCase(), "") + "</i>"; 
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
	
}
