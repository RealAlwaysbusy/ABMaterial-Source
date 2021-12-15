package com.ab.abmaterial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.SimpleFuture;

@Author("Alain Bailleul")  
@ShortName("ABMPercentSlider")
public class ABMPercentSlider extends ABMComponent {
	
	private static final long serialVersionUID = 6164558509064296L;
	protected ThemePercentSlider Theme=new ThemePercentSlider();
	protected List<Block> Blocks = new ArrayList<Block>();
	protected int mActive=0;
		
	public void Initialize(ABMPage page, String id, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_PERCENTSLIDER;	
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.PercentSliders.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.PercentSliders.get(themeName.toLowerCase()).Clone();				
			}
		}	
		IsInitialized=true;
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
			if (Page.CompleteTheme.PercentSliders.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.PercentSliders.get(themeName.toLowerCase()).Clone();				
			}
		}
	}	
	
	/**
	 * perc has to be in intervals of 5, ranging 5 to 100! 
	 * They all start from the left, so ten blocks could be:
	 * 
	 * AddBlock("lbl1", "indlbl1", 5)
	 * AddBlock("lbl2", "indlbl2", 15)
	 * AddBlock("lbl3", "indlbl3", 30)
	 * AddBlock("lbl4", "indlbl4", 40)
	 * AddBlock("lbl5", "indlbl5", 50)
	 * AddBlock("lbl6", "indlbl6", 65)
	 * AddBlock("lbl7", "indlbl7", 70)
	 * AddBlock("lbl8", "indlbl8", 80)
	 * AddBlock("lbl9", "indlbl9", 95)
	 * AddBlock("lbl10", "indlbl10", 100)
	 */
	public void AddBlock(String labelText, String indicatorText, int perc) {		
		Block bl = new Block();
		bl.Text = labelText;
		bl.IndText = indicatorText;
		bl.Perc = perc;
		Blocks.add(bl);
	}
	
	public void ClearBlocks() {
		Blocks = new ArrayList<Block>();
	}
	
	/**
	 *  activeblock can be 0 to 19 (or the number of blocks you added)
	 */
	public void SetActiveUntilBlock(int activeBlock) {
		mActive=activeBlock;
	}
	
	public int GetActiveUntilBlock() {
		StringBuilder s = new StringBuilder();
		s.append("var ret='';");
		s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').children('input').each(function () {");
		s.append("if ($(this).is(':checked')) {");
		s.append("ret=$(this).attr('value');");
		s.append("}");
		s.append("});");
		s.append("return ret;");
		
		int ret = -1;
		if (Page.ws!=null) {
			SimpleFuture fut = Page.ws.EvalWithResult(s.toString(), null);
			
			try {				
				ret = Integer.parseInt((String) fut.getValue());
				mActive=ret;
			} catch (InterruptedException e) {				
			} catch (TimeoutException e) {						
			} catch (ExecutionException e) {						
			} catch (IOException e) {						
			}
		}		
		return ret;
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
		String tmpVar = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		tmpVar = tmpVar.replace("-", "_");
			
		return s.toString();
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		// TODO extraClasses not working
		super.Refresh();
		ThemePercentSlider l = Theme;
		ABMaterial.ReplaceMyInnerHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), BuildBody());
		Page.ws.Eval("$('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').attr('class', 'dgradio-sb dgradio-sb" + l.ThemeName.toLowerCase()  + " " + mVisibility + " " + l.ZDepth + mIsPrintableClass + mIsOnlyForPrintClass + "');", null);
		
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("PercentSlider Refresh: " + ID);}
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
		ThemePercentSlider l = Theme;
		StringBuilder s = new StringBuilder();	
		s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"dgradio-sb dgradio-sb" + l.ThemeName.toLowerCase() + " " + mVisibility + " " + l.ZDepth + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() + "\">");
		s.append(BuildBody());
		s.append("</div>");
		IsBuild=true;
		return s.toString();
	}
	
	@Hide
	protected String BuildClass() {			
		StringBuilder s = new StringBuilder();			
		return s.toString(); 
	}
	
	@Hide
	protected String BuildBody() {
		StringBuilder s = new StringBuilder();
		for (int i=0;i<Blocks.size();i++) {
			String Active="";
			if (i==mActive) {
				Active=" checked=\"checked\" ";
			}
			s.append("<input type=\"radio\" class=\"dg-item\" name=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "Name\" id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + i + "\" value=\"" + i + "\" " + Active + "/>");
			s.append("<label for=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + i + "\" class=\"dg-label dg-" + Blocks.get(i).Perc + "\" data-caption=\"" + Blocks.get(i).IndText + "\">" + BuildText(Blocks.get(i).Text) + "<span class=\"dg-bg\"></span></label>");
		}
		return s.toString();
	}
	
	protected String BuildText(String text) {
		StringBuilder s = new StringBuilder();	
				
		String v = ABMaterial.HTMLConv().htmlEscape(text, Page.PageCharset);
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
	
	@Override
	protected ABMComponent Clone() {
		ABMPercentSlider c = new ABMPercentSlider();
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
	
	protected class Block {
		protected String Text="";
		protected String IndText="";
		protected int Perc=0;
	}

}

