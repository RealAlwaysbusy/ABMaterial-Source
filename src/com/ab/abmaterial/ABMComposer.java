package com.ab.abmaterial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;

@ShortName("ABMComposer")
@Events(values={"Edit(blockID as String)", "Remove(blockID as String)", "Add(blockID as String)"})
public class ABMComposer extends ABMComponent {
	private static final long serialVersionUID = 5539983971509918870L;
	protected ThemeComposer Theme=new ThemeComposer();
	
	protected List<ABMComposerBlock> Blocks = new ArrayList<ABMComposerBlock>();
	
	protected String MarginLeft="8px";
					
	/**
	 * marginLeft: default = "8px". If you are planning to add a label to each block, you will need to adjust this size. 
	 */
	public void Initialize(ABMPage page, String id, String marginLeft, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_COMPOSER;	
		if (!marginLeft.equals("")) {
			this.MarginLeft = marginLeft;
		}
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Composers.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Composers.get(themeName.toLowerCase()).Clone();				
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
		
	/*
	 * Not applicable for this component.  Set theme in initialize.
	 */
	public void UseTheme(String themeName) {
		
	}	
	
	public void AddBlock(ABMComposerBlock block) {
		block.IsDirty=true;
		Blocks.add(block);
	}
	
	public void RemoveBlock(int index) {
		if (index>=Blocks.size()) {
			BA.LogError("index is greater than size!");
			return;
		}
		Blocks.get(index).Header.Clear();
		Blocks.get(index).Body.Clear();
		Blocks.get(index).Footer.Clear();
		Blocks.get(index).CleanUp();
		Blocks.remove(index);
	}
	
	public ABMComposerBlock GetBlock(int index) {
		if (index>=Blocks.size()) {
			BA.LogError("index is greater than size!");
			return null;
		}
		return Blocks.get(index);
	}
	
	public int Size() {
		return Blocks.size();
	}
		
	public void InsertBlockBefore(int index, ABMComposerBlock block) {
		if (index>=Blocks.size()) {
			BA.LogError("index is greater than size!");
			return;
		}
		block.IsDirty=true;
		Blocks.add(index, block);
	}
	
	public void Clear() {
		for (int i=0;i<Blocks.size();i++) {
			ABMComposerBlock block = Blocks.get(i);
			block.Header.Clear();
			block.Body.Clear();
			block.Footer.Clear();
			block.CleanUp();
		}
		Blocks.clear();
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-body");
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("Composer Clear: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
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
		String tmpVar = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		tmpVar = tmpVar.replace("-", "_");
		
		
		s.append("$('.abmcpr-arrw').each(function() {");
		s.append("var arrid=$(this).attr('id');");
		
		
		if (Page.IsTablet() || Page.IsPhone()) {
			s.append("$(this).off('touchend').on('touchend', {el: arrid}, abmcprOnUpEvent);");
		} else {
			s.append("$(this).off('mouseup').on('mouseup', {el: arrid}, abmcprOnUpEvent);");
		}
	    s.append("});");
		
		return s.toString();
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		ABMaterial.ChangeVisibility(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), mVisibility);
		if (mIsPrintableClass.equals("")) {
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "no-print");
		} else {
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "no-print");
		}
		if (mIsOnlyForPrintClass.equals("")) {
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "only-print");
		} else {
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "only-print");
		}
		super.Refresh();
		String prevID="";
		for (int i=0;i<Blocks.size();i++) {
			Blocks.get(i).SetParams(ParentString, ArrayName, MarginLeft, ID);
			if (Blocks.get(i).IsBuild) {
				Blocks.get(i).Refresh();	
				prevID = Blocks.get(i).ID;
			} else {
				String extra="";
				if (i<Blocks.size()-1) {
					extra = "";
				} else {
					extra = "-last";
				}
				if (i==0) {
					ABMaterial.AddHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-body", Blocks.get(i).Build(extra));
					prevID = Blocks.get(i).ID;
				} else {
					ABMaterial.InsertHTMLAfter(Page, prevID, Blocks.get(i).Build(extra));
					prevID = Blocks.get(i).ID;
				}
			}
		}
		FirstRun();
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Composer Refresh: " + ID);}
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
		s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"abmcpr abmcpr-narrow abmcpr" + Theme.ThemeName.toLowerCase() + " " + mVisibility + " " + mIsPrintableClass + mIsOnlyForPrintClass + super.BuildExtraClasses() + "\" >");
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
		s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-body\" class=\"abmcpr-banchor\">");
		for (int i=0;i<Blocks.size();i++) {
			Blocks.get(i).SetParams(ParentString, ArrayName, MarginLeft, ID);
			if (i<Blocks.size()-1) {
				s.append(Blocks.get(i).Build(""));
			} else {
				s.append(Blocks.get(i).Build("-last"));
			}			
		}
		s.append("</div>");
		return s.toString();
	}
		
	protected String BuildBodyText(String Text) {
		StringBuilder s = new StringBuilder();		
	
		
		String v = ABMaterial.HTMLConv().htmlEscape(Text, Page.PageCharset);
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
		ABMComposer c = new ABMComposer();
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
		

}
