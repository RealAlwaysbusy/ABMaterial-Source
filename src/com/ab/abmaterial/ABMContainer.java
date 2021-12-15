package com.ab.abmaterial;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.keywords.Regex;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;

import com.ab.abmaterial.ABMGridDefinition.CellDef;
import com.ab.abmaterial.ABMGridDefinition.RowDef;

@Author("Alain Bailleul")  
@ShortName("ABMContainer")
@Events(values={"AnimationFinished(Target as String, lastAnimation As String)","NextContent(TriggerComponent As String)"}) 
public class ABMContainer extends ABMComponent{
	private static final long serialVersionUID = 7102439138694142300L;
	protected transient Map<String,ABMRowCell> AllComponents = new LinkedHashMap<String, ABMRowCell>();
	protected transient Map<String, ABMRow> Rows = new LinkedHashMap<String,ABMRow>();
	protected ThemeContainer Theme=new ThemeContainer();
	protected ThemeContainer PrevTheme=null;
	protected ABMGridDefinition Grid=new ABMGridDefinition();
	protected int CurrentRow=1;
	protected boolean mIsAnimatable=false;
	protected String mInitialAnimationName="";
	protected String mLastAnimationRunned="";
	protected String BorderStyle=ABMaterial.CONTAINERBORDER_DEFAULT;
	protected String ExtraStyle="";
	protected String FixedWidth="";
	protected String FixedHeight="";
	
	protected String FixedPosition="";
	
	protected boolean mIsCollapsable=false;
	protected transient ABMContainer mCollapseHeading=null;
	protected transient ABMContainer mCollapseBody=null;
	protected boolean mBodyCollapsed=false;
	
	protected int ContainerTop=Integer.MIN_VALUE;
	protected int ContainerTopSmall=Integer.MIN_VALUE;
	protected int ContainerTopMedium=Integer.MIN_VALUE;
	protected int ContainerTopLarge=Integer.MIN_VALUE;
		
	public String PaddingLeft=""; //"0.75rem";
	public String PaddingRight=""; //"0.75rem";
	public String PaddingTop=""; //"0rem";
	public String PaddingBottom=""; //"0rem";
	public String MarginTop=""; //"0px";
	public String MarginBottom=""; //"0px";
	public String MarginLeft=""; //"0px";
	public String MarginRight=""; //"0px";
	
	protected String LayoutName="";
	
	protected String ModalID="";
	
	protected String FromTopOrBottom="";
	protected String TopOrBottomValue="";
	protected double OnScrollStartOpacity=Double.MIN_VALUE;
	protected double OnScrollStopOpacity=Double.MIN_VALUE;
	
	public boolean IsTextSelectable=true;
	protected boolean GridIsCleared=false;
	
	protected String BackgroundImage="";
	
	protected boolean ContentOpen=true;
	
	protected Map<String, String> mDraggableTo = new LinkedHashMap<String,String>();
	protected Map<String,String> DragDropGroupNames= new LinkedHashMap<String,String>();
	
	public String Title="";
	
	protected String mPrintPageBreak="";
	protected String mIsOnlyForPrintClass=" only-print ";
	
	private boolean ZABMode=false;
	private boolean mIsResponsiveContainer=false;
	private String mThresholdName="";
	
	protected String mHasToggleIcon="";
	protected boolean mIsAllToggleContainer=false;
	protected String mGroupName="";
	protected String mGroupNamePrint="";
	protected String mGroupStartState="";
	
	public String B4JSExtra="";
	
	public boolean IsForPrint=false;
	
	/**
	 * if not in BuildPage(), the components needs a Refresh or a RefreshNoContents! 	
	 * 
	 * Note that a container is not draggable by default, so you have to explicitly set it with this method.
	 * Use the cellDragDropName defined in page.CreateDragDropZone()
	 * 
	 * This allows maximum flexibility in the behaviour.
	 */
	public void EnableDragDropZone(String groupName, String zoneDragDropName, boolean enable) {
		ABMDragDropGroup grp = Page.dragDropGroups.getOrDefault(groupName.toLowerCase(), null);
		if (grp==null) {
			BA.LogError("No drag drop group '" + groupName + "' found!");
			return;
		}
		DragDropGroupNames.put(groupName.toLowerCase(), groupName);
		if (enable) {
			mDraggableTo.put(zoneDragDropName.toLowerCase(),zoneDragDropName.toLowerCase());
		} else {
			mDraggableTo.remove(zoneDragDropName.toLowerCase());
		}
		if (IsBuild) {
			String sDragDrop = "";
			for (Entry<String,String> entry: mDraggableTo.entrySet()) {
				sDragDrop += entry.getKey() + ":";
			}
			if (!sDragDrop.equals("")) {
				sDragDrop = ":" + sDragDrop;
			}
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-dropto", sDragDrop);			
		}
	}
	
	/**
	 * if not in first Build, the components needs a Refresh or a RefreshNoContents! 	
	 * 
	 * Note that a container is not draggable by default, so you have to explicitly set it with this method.
	 * Use the cellDragDropName defined in page.CreateDragDropZone()
	 * 
	 * This allows maximum flexibility in the behaviour.
	 * 
	 * NOTE: if you add a container later, you will have to re-enable all previous containers so the newly added 
	 * is alo included a a drop zone!  You can use Page.GetAllDragDropComponentsFromGroupZone(groupName) or 
	 * Page.GetAllDragDropComponentsFromGroup(groupName, zoneDragDropName) to get them.
	 */
	public void EnableDragDropAllZonesFromGroup(String groupName, boolean enable) {
		ABMDragDropGroup grp = Page.dragDropGroups.getOrDefault(groupName.toLowerCase(), null);
		if (grp==null) {
			BA.LogError("No drag drop group '" + groupName + "' found!");
			return;
		}
		if (enable) {
			DragDropGroupNames.put(groupName.toLowerCase(), groupName);
		} else {
			DragDropGroupNames.remove(groupName.toLowerCase());
		}
		for (Entry<String, ABMCell> entry: grp.Cells.entrySet()) {
			String zoneDragDropName = entry.getKey();
			if (enable) {
				mDraggableTo.put(zoneDragDropName.toLowerCase(),zoneDragDropName.toLowerCase());
			} else {
				mDraggableTo.remove(zoneDragDropName.toLowerCase());
			}
		}
		if (IsBuild) {
			String sDragDrop = "";
			for (Entry<String,String> entry: mDraggableTo.entrySet()) {
				sDragDrop += entry.getKey() + ":";
			}
			if (!sDragDrop.equals("")) {
				sDragDrop = ":" + sDragDrop;
			}
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-dropto", sDragDrop);			
		}
	}
	
	/**
	 * Set a minimum height each cell in the group must be (so empty drop zones are visible too)	
	 * If there is already a group with this name, this call is ignored.
	 */
	public void DragDropCreateGroup(String groupName, int minHeight) {
		if (!Page.dragDropGroups.containsKey(groupName.toLowerCase())) {
			ABMDragDropGroup ddg=new ABMDragDropGroup();
			ddg.minHeight = minHeight;
			Page.dragDropGroups.put(groupName.toLowerCase(), ddg);
		}
	}
	
	/**
	 * Enable/Disable this container from being printed 
	 */
	public void setIsPrintable(Boolean printme) {
		if (printme) {
			mIsPrintableClass="";
			mIsOnlyForPrintClass=" only-print ";
		} else {
			mIsPrintableClass=" no-print ";
			mIsOnlyForPrintClass="";
		}
	}
	
	public void HasToggleIcon(String icon, String groupName) {
		mHasToggleIcon = icon;
		mGroupName = "ABM" + groupName.toLowerCase();
		mGroupNamePrint = " ABMrap ";
	}
	
	public void IsAllToggleContainer(boolean b, boolean startState) {
		this.mIsAllToggleContainer=true;
		if (startState) {
			mGroupStartState=" prntglall ";
		} else {
			mGroupStartState="";
		}
	}
	
	/**
	 * pageBreak: use the ABMaterial.PRINT_PAGEBREAK_ constants 
	 */
	public void setPrintPageBreak(String pageBreak) {
		mPrintPageBreak=pageBreak;
	}
	
	public String getPrintPageBreak() {
		return mPrintPageBreak;
	}
	
	/**
	 * set headerComponent = null if you do not want to use it.
	 */
	public void DragDropAddZone(String groupName, String zoneDragDropName, ABMCell cell, ABMComponent headerComponent) {
		ABMDragDropGroup ddg=null;
		if (Page.dragDropGroups.containsKey(groupName.toLowerCase())) {
			if (headerComponent!=null) {
				headerComponent.DragDropIsHeader=true;
				cell.AddComponentDD(headerComponent);
			}
			ddg = Page.dragDropGroups.get(groupName.toLowerCase());
			cell.DragDropName = zoneDragDropName.toLowerCase();
			cell.DragDropGroupName = groupName.toLowerCase();
			cell.DragDropMinHeight = ddg.minHeight;
			ddg.Cells.put(zoneDragDropName.toLowerCase(), cell);
		} else {
			BA.Log("No group '" + groupName + "' found. Use DragDropCreateGroup() first.");
			return;
		}
		Page.dragDropGroups.put(groupName.toLowerCase(), ddg);
	}
	
	public void Initialize(ABMPage page, String id, String themeName) {
		this.ID = id;			
		this.Type = ABMaterial.UITYPE_ABMCONTAINER;
		this.Page = page;
		UseTheme(themeName);
		IsInitialized=true;	
		IsVisibilityDirty=false;
	}	
	
	public void InitializeAnimated(ABMPage page, String id, String initialAnimationName, String themeName) {
		this.ID = id;			
		this.Type = ABMaterial.UITYPE_ABMCONTAINER;
		this.Page = page;
		mIsAnimatable=true;
		mInitialAnimationName = initialAnimationName;
		UseTheme(themeName);
		IsInitialized=true;
		IsVisibilityDirty=false;
	}
	
	public void InitializeCollapsable(ABMPage page, String id, String themeName, String headingThemeName, String bodyThemeName, boolean collapsed) {
		this.ID = id;			
		this.Type = ABMaterial.UITYPE_ABMCONTAINER;
		this.Page = page;
		this.mIsCollapsable=true;
		this.mCollapseHeading = new ABMContainer();
		this.mCollapseHeading.Initialize(page, "_heading_" + id, headingThemeName);
		this.mBodyCollapsed=collapsed; 
		this.mCollapseBody = new ABMContainer();		
		this.mCollapseBody.Initialize(page, "_body_" + id, bodyThemeName);
		UseTheme(themeName);
		IsInitialized=true;
		IsVisibilityDirty=false;
	}
		
	public void SetContentIsOpen(boolean open) {
		ContentOpen=open;
	}
	
	/**
	 * CAN NOT BE CALLED IN Build()! Use ContentIsOpen(boolean) instead to set the initial state.
	 */
	public void OpenContent() {		
		if (Page!=null) {
			if (Page.ws!=null) {
				Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').slideDown('fast');", null);
				try {
					if (Page.ws.getOpen()) {
						if (Page.ShowDebugFlush) {BA.Log("Container OpenContent: " + ID);}
						Page.ws.Flush();Page.RunFlushed();
						ContentOpen=true;
					}
				} catch (IOException e) {			
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * CAN NOT BE CALLED IN Build()! Use ContentIsOpen(boolean) instead to set the initial state.
	 */
	public void CloseContent() {		
		if (Page!=null) {
			if (Page.ws!=null) {
				Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').slideUp('fast');", null);
				try {
					if (Page.ws.getOpen()) {
						if (Page.ShowDebugFlush) {BA.Log("Container CloseContent: " + ID);}
						Page.ws.Flush();Page.RunFlushed();
						ContentOpen=false;
					}
				} catch (IOException e) {			
					e.printStackTrace();
				}
			}
		}
	}
	
	protected String Serialize(Object obj, String rowcell, int Type) {		
		if (!((obj instanceof Serializable) || (obj instanceof Externalizable))) {
			BA.LogError("There was a problem Serializing " + rowcell);
			return "";
		}		
		String redisString="";
		try {
             ByteArrayOutputStream bo = new ByteArrayOutputStream();
             ObjectOutputStream so = new ObjectOutputStream(bo);
             so.writeObject(obj);
             if (Page.ws.getOpen())
             so.flush();
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
	
	@Override
	public String getVisibility() {
		if (GotLastVisibility) {
			return mVisibility;
		}
		GotLastVisibility=true;
		if (!mB4JSUniqueKey.equals("") && !IsVisibilityDirty) {
			if (JQ!=null) {
				anywheresoftware.b4a.objects.collections.List par = new anywheresoftware.b4a.objects.collections.List();
				par.Initialize();
				par.Add(ParentString + RootID());
				FutureVisibility = JQ.RunMethodWithResult("GetVisibility", par);				
			} else {
				FutureVisibility=null;				
			}
			if (!(FutureVisibility==null)) {
				try {
					//BA.Log(ID);
					this.mVisibility = ((String)FutureVisibility.getValue());					
				} catch (InterruptedException e) {					
					e.printStackTrace();
				} catch (TimeoutException e) {					
					e.printStackTrace();
				} catch (ExecutionException e) {					
					e.printStackTrace();
				} catch (IOException e) {					
					e.printStackTrace();
				}
			} else {
				//BA.Log("FutureText = null");
				
			}	
			return mVisibility;
		}
		
		return mVisibility;		
	}
	
	/*
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public void setB4JSVisibility(String visibility) {
		
	}
	
	/*
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public String getB4JSVisibility() {
		return "B4JS Dummy";
	}	
	
	/** 
	 * returns a list of ABMComponents.  
	 * You will need to check its type and Cast it to the correct ABMComponent to access its properties and methods.
	 * 
	 * Note: for a ModalSheet use myModal.Content.GetAllComponents 
	 *  
	 * e.g. 
	 * Dim comps as List = myContainer.GetAllComponents
	 * for i = 0 to comps.size - 1
	 *      Dim comp As ABMComponent = comps.Get(i)
	 * 		Select case comp.Type
	 * 			Case ABM.UITYPE_LABEL
	 *              Dim lbl as ABMLabel = ABM.CastABMComponent(comp)
	 *              ...
	 *          Case ABM.UITYPE_CARD
	 *              Dim card as ABMCard = ABM.CastABMComponent(card)
	 *              ...
	 *          Case ...
	 *          
	 *      End Select
	 * next
	 */
	public anywheresoftware.b4a.objects.collections.List GetAllComponents() {
		anywheresoftware.b4a.objects.collections.List ret = new anywheresoftware.b4a.objects.collections.List();
		ret.Initialize();
		for (Entry<String,ABMRowCell>entry: AllComponents.entrySet()) {
			ABMRowCell rc = entry.getValue();			
			ABMCell ccell = CellInternal(rc.rowId, rc.cellId);
			if (ccell!=null) {
				for (Entry<String,ABMComponent> comps: ccell.Components.entrySet()) {
					String componentId = comps.getValue().extra + comps.getValue().ArrayName.toLowerCase() + comps.getValue().ID.toLowerCase();
					ret.Add(ccell.Component(componentId));
					//ret.Add(comps.getValue());
				}
			}
		}
		return ret;
	}
	
	/**
	 * for type use the ABM.UITYPE_ constants
	 * 
	 * returns a list of components of that type
	 * You will need to check its type and Cast it to the correct ABMComponent to access its properties and methods.
	 * 
	 * Note: for a ModalSheet use myModal.Content.GetAllComponentsOfType(ABM.UITYPE_LABEL)
	 *  
	 * e.g.
	 * Dim Labels as List = myContainer.GetAllComponentsOfType(ABM.UITYPE_LABEL)
	 * for i = 0 to Labels.size - 1
	 *      Dim lbl As ABMLabel = ABM.CastABMComponent(Labels.Get(i))
	 *      ...
	 * next
	 */
	public anywheresoftware.b4a.objects.collections.List GetAllComponentsOfType(int type) {
		anywheresoftware.b4a.objects.collections.List ret = new anywheresoftware.b4a.objects.collections.List();
		ret.Initialize();
		for (Entry<String,ABMRowCell>entry: AllComponents.entrySet()) {
			ABMRowCell rc = entry.getValue();			
			ABMCell ccell = CellInternal(rc.rowId, rc.cellId);
			if (ccell!=null) {
				for (Entry<String,ABMComponent> comps: ccell.Components.entrySet()) {
					if (comps.getValue().Type==type) {
						String componentId = comps.getValue().extra + comps.getValue().ArrayName.toLowerCase() + comps.getValue().ID.toLowerCase();
						ret.Add(ccell.Component(componentId));
						//ret.Add(comps.getValue());
					}
				}
			}
		}
		return ret;
	}
	
	public void setIsResponsiveContainer(boolean bool) {
		mIsResponsiveContainer = bool;
		mThresholdName="";
		Page.NeedsResponsiveContainer=true;
	}
	
	public void RaiseNextContentOnComponent(ABMComponent component, int row, int cell, int offsetBottom) {
		if (Page.ws==null) {
			return;
		}
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(component.ParentString);
		Params.Add(component.ArrayName.toLowerCase());
		Params.Add(component.ID.toLowerCase());
		Params.Add(ID.toLowerCase());
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-r" + row + "c" + cell);
		Params.Add(offsetBottom);
		Page.ws.RunFunction("nextcontentcontainer", Params);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("Container RaiseNextContentOnComponent");}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public void ScrollToRow(int row, int speed) {
		ABMRow r = Row(row);
		if (r==null) {
			BA.Log("Row " + row + " not found!");
			return;
		}
		String scrollID = "";
		if (r.CenterInPage) {
			scrollID = r.ParentString + r.ArrayName.toLowerCase() + r.ID.toLowerCase() + "_cont";
		} else {
			scrollID = r.ParentString + r.ArrayName.toLowerCase() + r.ID.toLowerCase();
		}
				
		if (Page.ws!=null) {				
			Page.ws.Eval("$('#" + scrollID + "').animate({scrollTop: ($('#" + scrollID +"').offset().top)}," + speed + ");", null);				
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("ScrollToRow");}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
		
	}
	
	public void ScrollToComponent(ABMComponent component, int speed) {
		String scrollID = component.ParentString + component.RootID();
		if (Page.ws!=null) {				
			Page.ws.Eval("$('#" + scrollID + "').animate({scrollTop: ($('#" + scrollID +"').offset().top)}," + speed + ");", null);				
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("ScrollToComponent");}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * repeat: use the ABM.CONTAINERIMAGE_REPEAT_ constants
	 * position: use the ABM.CONTAINERIMAGE_POSITION_ constants or a valid CSS position string
	 */
	public void SetBackgroundImage(String color, String colorIntensity, String source, String repeat, String position) {
		String extra="";
		if (position.equals("cover")) {
			repeat = "no-repeat";
			position = "center center fixed";
			extra = ",\"-webkit-background-size\": \"cover\", \"-moz-background-size\": \"cover\", \"-o-background-size\": \"cover\", \"background-size\": \"cover\"";
		}
		BackgroundImage = "{\"background\": \"" + ABMaterial.GetColorStrMap(color, colorIntensity) + " url('" + source + "') " + repeat + " " + position + "\"" + extra + "}";
	}
	
	/**
	 * NIET VERGETEN OOK IN EXTRACTRESULTMODULE mocht er item zodal [TYPE] bijkomen!!!!! 
	 */
	protected void internalLoadABMLayout(BA ba, ABMPage page, String layoutName, String newID, boolean doProperties) {
		//BA.Log("Loading layout " + layoutName);
		SetPage(page);
		LayoutDef def = Page.Layouts.getOrDefault(layoutName.toLowerCase(), null);
		if (def!=null) {
			boolean isGrid=false;
			if (this.ModalID.equals("")) {
				this.ID = newID.toLowerCase();
			} else {
				this.ID = this.ModalID.toLowerCase();
			}
			String[] values; 		
			if (Rows!=null) {
				for (Entry<String,ABMRow> entry: Rows.entrySet()) {
					entry.getValue().RemoveAllComponents();
				}
			}
			Rows = new LinkedHashMap<String,ABMRow>();
			for (int l=0;l<def.Lines.size();l++) {
				String sCurrentLine = def.Lines.get(l);
				switch (sCurrentLine) {
				case "[GRID]":
					isGrid=true;
					break;
				case "[/GRID]":
					isGrid=false;
					break;
				default:
					if (sCurrentLine.startsWith("[TYPE]")) {
						if (!sCurrentLine.substring(6).equals("CONTAINER")) {
							BA.LogError("ERROR:  The " + layoutName + " is not a Container Layout!");
							return;
						}						
					} else {
						if (sCurrentLine.startsWith("[PROPERTIES]")) {
							ABMContainer mycon = (ABMContainer) page.Deserialize(sCurrentLine.substring(12));
							Copy(mycon);
						} else {
							if (isGrid) {
								ABMRow row = new ABMRow();
								values = Regex.Split(":", sCurrentLine);
								try {
									row.Deserialize(values[1], page);
								} catch (UnsupportedEncodingException e) {							
									e.printStackTrace();
								}
								Rows.put(values[0], row);
							} else {					
								values = Regex.Split(":", sCurrentLine);
								String[] vals = Regex.Split(",", values[0]);
							
								if (Integer.parseInt(vals[2])==ABMaterial.UITYPE_ABMCONTAINER) {
									ABMContainer con = (ABMContainer) page.Deserialize(values[1]);
									con.AllComponents = new LinkedHashMap<String, ABMRowCell>();
									con.Rows = new LinkedHashMap<String,ABMRow>();
									con.internalLoadABMLayout(ba, page, con.LayoutName, vals[4], false);
									ABMaterial.AbstractDesignerAdd(page, con, vals[0], vals[1], false, this.getID(), this);
								
								} else {							
									ABMaterial.AbstactDesignerComponent(page, vals, values, false, this.getID(), this);
								}
							}
						}
					}
					break;
				}
			}
		} else {
			File f = new File(anywheresoftware.b4a.objects.streams.File.getDirApp() + "/abmlayouts/", layoutName.toLowerCase() + ".abm");
			BufferedReader br=null;
			boolean isGrid=false;
			this.ID = newID.toLowerCase();
			String[] values; 		
			if (Rows!=null) {
				for (Entry<String,ABMRow> entry: Rows.entrySet()) {
					entry.getValue().RemoveAllComponents();
				}
			}
			Rows = new LinkedHashMap<String,ABMRow>();		
			def = new LayoutDef();
			try {
				String sCurrentLine;
				br = new BufferedReader(new FileReader(f));
				while ((sCurrentLine = br.readLine()) != null) {	
					def.Lines.add(sCurrentLine);
					switch (sCurrentLine) {
					case "[GRID]":
						isGrid=true;
						break;
					case "[/GRID]":
						isGrid=false;
						break;
					default:
						if (sCurrentLine.startsWith("[TYPE]")) {
							if (!sCurrentLine.substring(6).equals("CONTAINER")) {
								BA.LogError("ERROR:  The " + layoutName + " is not a Container Layout!");
								try {
					        		if (br != null)br.close();
					        	} catch (IOException ex) {
					        		ex.printStackTrace();
					        	}
								return;
							}							
						} else {
							if (sCurrentLine.startsWith("[PROPERTIES]")) {
								ABMContainer mycon = (ABMContainer) page.Deserialize(sCurrentLine.substring(12));
								Copy(mycon);
							} else {							
								if (isGrid) {
									ABMRow row = new ABMRow();
									values = Regex.Split(":", sCurrentLine);
									row.Deserialize(values[1], page);
									Rows.put(values[0], row);
								} else {					
									values = Regex.Split(":", sCurrentLine);
									String[] vals = Regex.Split(",", values[0]);
								
									if (Integer.parseInt(vals[2])==ABMaterial.UITYPE_ABMCONTAINER) {
										ABMContainer con = (ABMContainer) page.Deserialize(values[1]);
										con.AllComponents = new LinkedHashMap<String, ABMRowCell>();
										con.Rows = new LinkedHashMap<String,ABMRow>();
										con.internalLoadABMLayout(ba, page, con.LayoutName, vals[4], false);
										ABMaterial.AbstractDesignerAdd(page, con, vals[0], vals[1], false, this.getID(), this);										
									} else {								
										ABMaterial.AbstactDesignerComponent(page, vals, values, false, this.getID(), this);
									}
								}
							}
						}
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
			Page.Layouts.put(layoutName.toLowerCase(), def);
		}
		if (Page.ws!=null) {
			Refresh();
		}
	}
	
	/**
	 * Get if the container is collapsable.  
	 */
	public boolean IsCollapsable() {
		return this.mIsCollapsable;
	}
	
	public ABMContainer CollapsableHeading() {
		if (!this.mIsCollapsable) {
			BA.Log("This is not a collapsable container!");
			return null;
		}
		return this.mCollapseHeading;
	}
	
	public ABMContainer CollapsableBody() {
		if (!this.mIsCollapsable) {
			BA.Log("This is not a collapsable container!");
			return null;
		}
		return this.mCollapseBody;
	}
	
	public void SetBorder(String color, String intensity, int widthPx, String borderStyle) {
		if (borderStyle==ABMaterial.CONTAINERBORDER_NONE) {
			BorderStyle="";
			return;
		}
		BorderStyle = "border: " + ABMaterial.GetColorStrMap(color, intensity) + ";" + "border-width:" + widthPx + "px;border-style:" + borderStyle;
	}
	
	public void SetBorderEx(String color, String intensity, int widthPx, String borderStyle, String radius) {
		if (borderStyle==ABMaterial.CONTAINERBORDER_NONE) {
			BorderStyle="";
			return;
		}
		BorderStyle = "border: " + ABMaterial.GetColorStrMap(color, intensity) + ";" + "border-width:" + widthPx + "px;border-style:" + borderStyle + ";border-radius:" + radius ;
	}
	
	public void SetExtraStyle(String extraStyle) {
		if (extraStyle.length()>0) {
			if (!extraStyle.endsWith(";")) {
				extraStyle = extraStyle + ";";
			}
		}
		ExtraStyle = extraStyle;
	}
	
	public void SetFixedWidth(String width) {
		this.FixedWidth = width;
	}
	
	public void SetFixedHeight(String height) {
		this.FixedHeight = height;
	}
	
	/**
	 * Set a parameter to empty string when you don not want to set it. 
	 */
	public void SetFixedPosition(String left, String right, String top, String bottom) {
		this.FixedPosition = "position: fixed;";
		if (!left.equals("")) {
			this.FixedPosition = this.FixedPosition + "left: " + left + ";";
		}
		if (!right.equals("")) {
			this.FixedPosition = this.FixedPosition + "right: " + right + ";";
		}
		if (!top.equals("")) {
			this.FixedPosition = this.FixedPosition + "top: " + top + ";";
		}
		if (!bottom.equals("")) {
			this.FixedPosition = this.FixedPosition + "bottom: " + bottom + ";";
		}
	}
	
	public boolean IsAnimatable() {
		return mIsAnimatable;
	}
	
	public String LastAnimationRunned() {
		return mLastAnimationRunned;
	}
	
	protected void ResetTheme() {
		UseTheme(Theme.ThemeName);
		for (Entry<String, ABMRow> row : Rows.entrySet()) {
			row.getValue().ResetTheme();							
        }
	}
	
	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase();
	}
	
	public String GetDebugRowCellIDs() {
		StringBuilder s = new StringBuilder();
		for (Entry<String, ABMRow> row : Rows.entrySet()) {
			ABMRow r = row.getValue();
			s.append(r.GetDebugID() + "\n");
			for (Entry<String, ABMCell> cell : r.Cells.entrySet()) {
				ABMCell c = cell.getValue();
				s.append("     " + c.GetDebugID() + "\n");
			}
        }
		return s.toString();
	}
	
	public void RunAnimation(String animationName) {
		if (!mIsAnimatable) {
			BA.Log("This container is not animatable!");
			return;
		}
		ABMAnimation anim = Page.Animations.getOrDefault(animationName.toLowerCase(), null);
		if (anim==null) {
			BA.Log("Animation " + animationName + " not found!");
			return;
		}
		StringBuilder s = new StringBuilder();
		
		s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').velocity(");
		String ev = "";
		if (ArrayName.equals("")) {
			ev = "b4j_raiseEvent('page_parseevent', {'eventname': '" + ID.toLowerCase() + "_animationfinished','eventparams': 'target,lastanimation', 'target': '" + ID.toLowerCase() + "', 'lastanimation': '" + animationName + "'});";
		} else {
			ev = "b4j_raiseEvent('page_parseevent', {'eventname': '" + ArrayName.toLowerCase() + "_animationfinished','eventparams': 'target,lastanimation', 'target': '" + ArrayName.toLowerCase() + ID.toLowerCase() + "', 'lastanimation': '" + animationName + "'});";
		}
		s.append(anim.Build(ev));
		s.append(");");
		Page.ws.Eval(s.toString(), null);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("Container RunAnimation: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
			mLastAnimationRunned = animationName;
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}		
	
	protected String RunInitialAnimation() {
		if (mIsAnimatable) {
			ABMAnimation initAnim = Page.Animations.getOrDefault(mInitialAnimationName.toLowerCase(), null);
			if (initAnim==null) {
				return "";
			}
			String s = initAnim.BuildInitialAfterCreation();
			if (!s.equals("")) {
				return "$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').velocity(" + s + ");";
			} else {
				return "";
			}
		} else {
			return "";
		}
		
	}
	
	@Override
	protected void AddArrayName(String arrayName) {
		this.ArrayName += arrayName;
		Map<String, ABMRow> newRows = new LinkedHashMap<String,ABMRow>();
		for (Map.Entry<String, ABMRow> entry : Rows.entrySet()) {
			ABMRow row = (ABMRow) entry.getValue();
			row.SetParentStringAndClick(this.ParentString + arrayName.toLowerCase() + ID.toLowerCase());
			
			for (Map.Entry<String,ABMCell> cell: row.Cells.entrySet()) {
				cell.getValue().CellInfo = arrayName + cell.getValue().CellInfo;
				
			}
			newRows.put(entry.getKey(), row);	
		}
		Rows.clear();
		Rows.putAll(newRows);
	}
	
	public ABMComponent ComponentRowCell(int row, int cell, String componentId) {
		if (mIsCollapsable) {
			BA.Log("This container is collapsable.  Use CollapsableHeader/CollapsableBody!");
			return null;
		}
		ABMCell ccell = Cell(row, cell);
		if (ccell==null) {
			return null;
		}
		
		return ccell.Component(componentId);		
	}
	
	public ABMComponent ComponentRowCellWriteOnly(int row, int cell, String componentId) {
		if (mIsCollapsable) {
			BA.Log("This container is collapsable.  Use CollapsableHeader/CollapsableBody!");
			return null;
		}
		ABMCell ccell = Cell(row, cell);
		if (ccell==null) {
			return null;
		}
		
		return ccell.ComponentWriteOnly(componentId);
	}
	
	public ABMComponent Component(String componentId) {
		if (mIsCollapsable) {
			BA.Log("This container is collapsable.  Use CollapsableHeader/CollapsableBody!");
			return null;
		}
		ABMRowCell rc = AllComponents.getOrDefault(componentId.toLowerCase(), null);
		if (rc==null) {
			BA.Log("Container No component found with id " + componentId );
			return null;
		}	
		ABMCell ccell = CellInternal(rc.rowId, rc.cellId);
		if (ccell==null) {
			return null;
		}

		return ccell.Component(componentId);		
	}
	
	public ABMComponent ComponentWriteOnly(String componentId) {
		if (mIsCollapsable) {
			BA.Log("This container is collapsable.  Use CollapsableHeader/CollapsableBody!");
			return null;
		}
		ABMRowCell rc = AllComponents.getOrDefault(componentId.toLowerCase(), null);
		if (rc==null) {
			BA.Log("Container No component found with id " + componentId );
			return null;
		}	
		ABMCell ccell = CellInternal(rc.rowId, rc.cellId);
		if (ccell==null) {
			return null;
		}

		return ccell.ComponentWriteOnly(componentId);
	}
	
	protected ABMCell CellInternal(String rowId, String cellId) {
		if (mIsCollapsable) {
			BA.Log("This container is collapsable.  Use CollapsableHeader/CollapsableBody!");
			return null;
		}
		ABMRow rrow = RowInternal(rowId);
		if (rrow==null) {
			return null;
		}
		ABMCell c = rrow.CellInternal(cellId);	
		c.Parent=this;
		c.ClickableTarget = ArrayName.toLowerCase() + ID.toLowerCase();
		c.ParentString=ArrayName.toLowerCase() + ID.toLowerCase() + "-";
		return c;
	}
	
	/**
	 * Use default 0 margin-top, 20 margin-bottom 
	 */
	public RowDef AddRows(int numberOfRows, boolean centerInPage, String themeName) {
		
		if (mIsCollapsable) {
			BA.Log("This container is collapsable.  Use CollapsableHeader/CollapsableBody!");
			return null;
		}
		return Grid.AddRows(numberOfRows, centerInPage, themeName);
	}
	
	public RowDef AddRowsM(int numberOfRows, boolean centerInPage, int marginTopPx, int marginBottomPx, String themeName) {
		
		if (mIsCollapsable) {
			BA.Log("This container is collapsable.  Use CollapsableHeader/CollapsableBody!");
			return null;
		}
		return Grid.AddRowsM(numberOfRows, centerInPage, marginTopPx, marginBottomPx, themeName);
	}
	
	public RowDef AddRowsM2(int numberOfRows, boolean centerInPage, int marginTopPx, int marginBottomPx, int marginLeftPx, int marginRightPx, String themeName) {
		
		if (mIsCollapsable) {
			BA.Log("This container is collapsable.  Use CollapsableHeader/CollapsableBody!");
			return null;
		}
		return Grid.AddRowsM2(numberOfRows, centerInPage, marginTopPx, marginBottomPx, marginLeftPx, marginRightPx, themeName);
	}
	
	/**
	 * creates one row with the number of cells specified, all equal in size 
	 * Possible values for numberOfCells are: 1, 2, 3, 4, 6, 12  
	 */
	public RowDef AddRow(int numberOfCells, boolean centerInPage, String rowThemeName, String cellThemeName) {
		
		if (mIsCollapsable) {
			BA.Log("This container is collapsable.  Use CollapsableHeader/CollapsableBody!");
			return null;
		}
		return Grid.AddRow(numberOfCells, centerInPage, rowThemeName, cellThemeName);
	}
	
	/**
	 * Use default 0 margin-top, 20 margin-bottom 
	 */
	public RowDef AddRowsV(int numberOfRows, boolean centerInPage, String visibility, String themeName) {
		
		if (mIsCollapsable) {
			BA.Log("This container is collapsable.  Use CollapsableHeader/CollapsableBody!");
			return null;
		}
		return Grid.AddRowsV(numberOfRows, centerInPage, visibility, themeName);
	}
	
	public RowDef AddRowsMV(int numberOfRows, boolean centerInPage, int marginTopPx, int marginBottomPx, String visibility, String themeName) {
		
		if (mIsCollapsable) {
			BA.Log("This container is collapsable.  Use CollapsableHeader/CollapsableBody!");
			return null;
		}
		return Grid.AddRowsMV(numberOfRows, centerInPage, marginTopPx, marginBottomPx, visibility, themeName);
	}
	
	public RowDef AddRowsMV2(int numberOfRows, boolean centerInPage, int marginTopPx, int marginBottomPx, int marginLeftPx, int marginRightPx, String visibility, String themeName) {
		
		if (mIsCollapsable) {
			BA.Log("This container is collapsable.  Use CollapsableHeader/CollapsableBody!");
			return null;
		}
		return Grid.AddRowsMV2(numberOfRows, centerInPage, marginTopPx, marginBottomPx, marginLeftPx, marginRightPx, visibility, themeName);
	}
	
	/**
	 * creates one row with the number of cells specified, all equal in size 
	 * Possible values for numberOfCells are: 1, 2, 3, 4, 6, 12  
	 */
	public RowDef AddRowV(int numberOfCells, boolean centerInPage, String rowThemeName, String visibility, String cellThemeName) {
		
		if (mIsCollapsable) {
			BA.Log("This container is collapsable.  Use CollapsableHeader/CollapsableBody!");
			return null;
		}
		return Grid.AddRowV(numberOfCells, centerInPage, rowThemeName, visibility, cellThemeName);
	}
		
	public void BuildGrid() {
		
		if (mIsCollapsable) {
			BA.Log("This container is collapsable.  Use CollapsableHeader/CollapsableBody!");
			return;
		}
		AddGridDefinition(Grid);
	}	
	
	public void DebugPrintGrid() {
		BA.Log(Grid.DebugPrintGrid(ID));
	}
	
	/**
	 * VERY experimental! Should allow you to remove everything (rows, cols, components) from the component and allow you
	 * to use the AddRow() methods again, followed by a BuildGrid.
	 * 
	 * You can then add components again.
	 * 
	 * Returns the orignal container ID so you can re-use when adding new components
	 */
	public String ClearGrid() {
		for (Entry<String,ABMRow> row: Rows.entrySet()) {
			row.getValue().RemoveAllComponents();			
		}
		AllComponents = new LinkedHashMap<String, ABMRowCell>();
		Rows = new LinkedHashMap<String,ABMRow>();
		Grid=new ABMGridDefinition();
		CurrentRow=1;
		
		ABMaterial.EmptyHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		
		this.HadFirstRun=false;
		GridIsCleared=true;
		
		return ID;
	}
	
	@Hide
	protected void AddGridDefinition(ABMGridDefinition gridDef) {
		int rCounter=1;
		for (RowDef row: gridDef.Rows) {
			if (!row.IsBuild) {
				for (int i=0;i<row.NumberOfrows;i++) {
					AddRow(ArrayName.toLowerCase() + gridDef.GridBaseId + rCounter, row.CenterInPage, row.MarginTopPx, row.MarginBottomPx, row.MarginLeftPx, row.MarginRightPx, row.Visibility, row.ThemeName);
					CellDef cell = row.cellDef;
					int cCounter=1;
					while (cell!=null) {
						for (int j=0;j<cell.NumberOfCells;j++) {
							AddCell(ArrayName.toLowerCase() + gridDef.GridBaseId + rCounter, ArrayName.toLowerCase()+ gridDef.GridBaseId + rCounter + "C" + cCounter, cell.OffsetSmall, cell.OffsetMedium, cell.OffsetLarge, cell.SizeSmall, cell.SizeMedium, cell.SizeLarge, cell.MarginTopPx, cell.MarginBottomPx, cell.PaddingLeftPx, cell.PaddingRightPx, cell.Visibility, cell.ThemeName, "" + rCounter + "," + cCounter);							
							cCounter++;
						}
						cell = cell.cellDef;
					}
					rCounter++;					
				}
				row.IsBuild=true;
			} else {
				rCounter+=row.NumberOfrows;
			}
		}
		
	}
				
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Containers.containsKey(themeName.toLowerCase())) {
				if (Theme!=null) {
					PrevTheme = Theme.Clone();
				}
				Theme = Page.CompleteTheme.Containers.get(themeName.toLowerCase()).Clone();				
			}
		}
	}	
	
	@Hide
	public void AddRow(String rowId, boolean centerInPage, int marginTopPx, int marginBottomPx, int marginLeftPx, int marginRightPx, String visibility,String themeName) {
		AddRowToCell("", rowId, centerInPage, marginTopPx, marginBottomPx, marginLeftPx, marginRightPx, visibility, themeName);
	}
	
	@Hide
	public void AddRowToCell(String cellId, String rowId, boolean centerInPage, int marginTopPx, int marginBottomPx, int marginLeftPx, int marginRightPx, String visibility, String themeName) {
		ABMRow row = new ABMRow();
		row.ID = rowId;	
		row.CenterInPage = centerInPage;
		row.MarginTop = marginTopPx + "px";
		row.MarginBottom = marginBottomPx + "px";
		if (marginLeftPx!=-9999) {
			row.MarginLeft = marginLeftPx + "px";
		}
		if (marginRightPx!=-9999) {
			row.MarginRight = marginRightPx + "px";
		}
		row.Page= Page;
		row.mVisibility = visibility;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Rows.containsKey(themeName.toLowerCase())) {
				row.Theme = Page.CompleteTheme.Rows.get(themeName.toLowerCase()).Clone();				
			}
		}
		row.ParentString=ArrayName.toLowerCase() + ID.toLowerCase() + "-";
		row.ClickableTarget = ArrayName.toLowerCase() + ID.toLowerCase();
		row.SetZAB(ZABMode);
		Rows.put(rowId.toLowerCase(),row);		
	}
	
	public ABMRow Row(int row) {
		if (row==-1) return null;
		if (mIsCollapsable) {
			BA.Log("This container is collapsable.  Use CollapsableHeader/CollapsableBody!");
			return null;
		}
		String rowId=ArrayName.toLowerCase() + "R" + row;
		ABMRow r =  RowInternal(rowId);
		if (r==null) {
			rowId="R" + row;
			r =  RowInternal(rowId);
			if (r==null) {
				BA.Log("Container No row found with rowId=" + rowId + "!");
			}
		}
		return r;
	}	
	
	protected ABMRow RowInternal(String rowId) {
		ABMRow rrow = Rows.getOrDefault(rowId.toLowerCase(), null);
		if (rrow==null) {
			return null;
		}
		rrow.ParentString=ArrayName.toLowerCase() + ID.toLowerCase() + "-";
		rrow.ClickableTarget = ArrayName.toLowerCase() + ID.toLowerCase();
		return rrow;
	}
	
	@Hide
	public void AddCell(String rowId, String cellId, int offsetSmall, int offsetMedium, int offsetLarge, int sizeSmall, int sizeMedium, int sizeLarge, int marginTopPx, int marginBottomPx, int paddingLeftPx, int paddingRightPx, String visibility, String themeName, String cellInfo) {		
		ABMRow row = RowInternal(rowId);
		if (row==null) {
			BA.Log("Row is null: " + rowId );
			return;
		}
		ABMCell cell = new ABMCell();
		cell.CellInfo = ID + ".cell(" + cellInfo + ")";
		cell.CellInfo=cell.CellInfo.replace("-footer", ".footer");
		cell.CellInfo=cell.CellInfo.replace("-content", ".content");
		cell.ID = cellId;
		cell.PaddingLeft=paddingLeftPx + "px";
		cell.PaddingRight=paddingRightPx + "px";
		cell.MarginTop=marginTopPx + "px";
		cell.MarginBottom=marginBottomPx + "px";
		cell.OffsetSmall = offsetSmall;
		cell.OffsetMedium = offsetMedium;
		cell.OffsetLarge = offsetLarge;
		cell.SizeSmall = sizeSmall;
		cell.SizeMedium = sizeMedium;
		cell.SizeLarge = sizeLarge;			
		cell.Page = Page;
		cell.RowId = rowId;
		cell.Parent=this;
		cell.ParentString=ArrayName.toLowerCase() + ID.toLowerCase() + "-";
		cell.ClickableTarget = ArrayName.toLowerCase() + ID.toLowerCase();
		cell.mVisibility = visibility;
		cell.SetZAB(ZABMode);
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Cells.containsKey(themeName.toLowerCase())) {
				cell.Theme = Page.CompleteTheme.Cells.get(themeName.toLowerCase()).Clone();				
			}
		}
				
		row.Cells.put(cellId.toLowerCase(), cell);
	}
	
	public ABMCell Cell(int row, int cell) {
		if (mIsCollapsable) {
			BA.Log("This container is collapsable.  Use CollapsableHeader/CollapsableBody!");
			return null;
		}
		CurrentRow=row;
		ABMRow rrow = RowInternal(ArrayName.toLowerCase() + "R" + row);
		if (rrow==null) {
			rrow = RowInternal("R" + row);
			if (rrow==null) {
				BA.Log("Container No row found with rowId=" + ArrayName.toLowerCase() + "R" + row + "!");
				return null;
			}
		}
		ABMCell c = rrow.Cell(cell);
		if (c!=null) {
			c.Parent=this;
			c.ParentString=ArrayName.toLowerCase() + ID.toLowerCase() + "-";
			c.ClickableTarget = ArrayName.toLowerCase() + ID.toLowerCase();
		}
		return c;		
	}	
	
	public ABMCell CellR(int moveNumberOfRows, int cell) {
		if (mIsCollapsable) {
			BA.Log("This container is collapsable.  Use CollapsableHeader/CollapsableBody!");
			return null;
		}
		CurrentRow+=moveNumberOfRows;
		ABMRow rrow = RowInternal(ArrayName.toLowerCase() + "R" + CurrentRow);
		if (rrow==null) {
			rrow = RowInternal("R" + CurrentRow);
			if (rrow==null) {
				return null;
			}
		}
		ABMCell c = rrow.Cell(cell);	
		c.Parent=this;
		c.ParentString=ArrayName.toLowerCase() + ID.toLowerCase() + "-";
		c.ClickableTarget = ArrayName.toLowerCase() + ID.toLowerCase();
		return c;				
	}
	
	
	protected void CheckIfInBrowser(StringBuilder ToCheckSB) {
		if (!mIsCollapsable) {
			for (Entry<String, ABMRow> row : Rows.entrySet()) {	
				row.getValue().CheckIfInBrowser(ToCheckSB);
			}
		} else {
			this.mCollapseHeading.CheckIfInBrowser(ToCheckSB);
			this.mCollapseBody.CheckIfInBrowser(ToCheckSB);
		}
	}
	
	protected boolean ResetNotInBrowser(String ToCheck, String OKCheck) {
		boolean ret = true;
		if (!mIsCollapsable) {
			for (Entry<String, ABMRow> row : Rows.entrySet()) {	
				if (!row.getValue().ResetNotInBrowser(ToCheck, OKCheck)) {
					ret = false;
				}
			}
		} else {
			if (!this.mCollapseHeading.ResetNotInBrowser(ToCheck, OKCheck)) {
				ret = false;
			}
			if (!this.mCollapseBody.ResetNotInBrowser(ToCheck, OKCheck)) {
				ret = false;
			}
		}
		return ret;
	}
	
	protected void ResetSVGs() {
		BA.Log("Resetting all SVG...");
		if (!mIsCollapsable) {
			for (Entry<String, ABMRow> row : Rows.entrySet()) {	
				row.getValue().ResetSVGs();				
			}
		} else {
			mCollapseHeading.ResetSVGs();
			mCollapseBody.ResetSVGs();			
		}
	}
	
	
	@Hide
	@Override
	protected String Build() {
		
		if (Theme.ThemeName.equals("default")) {
			Theme.Colorize(Page.CompleteTheme.MainColor);
		}
		
		AddGridDefinition(Grid);
		StringBuilder s = new StringBuilder();
		
		String StyleStr=BuildStyle();
		if (StyleStr!="") {
			StyleStr = " style=\"" + StyleStr + "\" ";
		}
		String Collapse="";
		if (this.mIsCollapsable) {
			Collapse=" collapse-card ";			
			if (!this.mBodyCollapsed) {
				Collapse+=" active ";
			}
			
		}
		String cursor = "";
		if (!mCursor.equals("")) {
			cursor = " " + mCursor + " ";
		}
		String ContainerTop="";
		if (this.ContainerTopSmall!=Integer.MIN_VALUE) {
			ContainerTop = " container" + ID.toLowerCase() + " ";
		}
		String pointerEvents="";

		pointerEvents = " floatingpointer ";

		String selectable="";
		if (!IsTextSelectable) {
			selectable = " notselectable ";
		}

		String sDragDrop = "";
		for (Entry<String,String> entry: mDraggableTo.entrySet()) {
			sDragDrop += entry.getKey() + ":";
		}
		if (!sDragDrop.equals("")) {
			sDragDrop = ":" + sDragDrop;
		}
		String ZAB="";
		if (ZABMode) {
			ZAB = " zab ";
		}
		if (mIsResponsiveContainer) {
			ZAB = ZAB + " responsive-cont" + mThresholdName + " ";
		}
		String B4JSData="";
		if (!mB4JSUniqueKey.equals("")) {
			B4JSData = " data-b4js=\"" + mB4JSUniqueKey + "\" data-b4jsextra=\"" + B4JSExtra + "\" "; 
		} else {
			B4JSData = " data-b4js=\"\" data-b4jsextra=\"" + B4JSExtra + "\" "; 
		}
		if (!GridIsCleared) {
			if (this.mIsAllToggleContainer) {
				if (!sDragDrop.equals("")) {
					s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\"" + B4JSData + " data-dropto=\"" + sDragDrop + "\" class=\"" + super.BuildExtraClasses() + cursor + ContainerTop + ABMaterial.GetColorStr(Theme.BackColor, Theme.BackColorIntensity, "") + " " + Theme.ZDepth + Collapse + " " + mVisibility + " " + pointerEvents + selectable + mIsPrintableClass + mIsOnlyForPrintClass + mPrintPageBreak + ZAB + "\"" + StyleStr + ">\n");
				} else {
					s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\"" + B4JSData + " class=\"" + super.BuildExtraClasses() + cursor + ContainerTop + ABMaterial.GetColorStr(Theme.BackColor, Theme.BackColorIntensity, "") + " " + Theme.ZDepth + Collapse + " " + mVisibility + " " + pointerEvents + selectable + mIsPrintableClass + mIsOnlyForPrintClass + mPrintPageBreak + ZAB + "\"" + StyleStr + ">\n");
				}
			} else {
				if (!sDragDrop.equals("")) {
					s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\"" + B4JSData + " data-dropto=\"" + sDragDrop + "\" class=\"" + mGroupNamePrint + mGroupName + " " + super.BuildExtraClasses() + cursor + ContainerTop + ABMaterial.GetColorStr(Theme.BackColor, Theme.BackColorIntensity, "") + " " + Theme.ZDepth + Collapse + " " + mVisibility + " " + pointerEvents + selectable + mIsPrintableClass + mIsOnlyForPrintClass + mPrintPageBreak + ZAB + "\"" + StyleStr + ">\n");
				} else {
					s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\"" + B4JSData + " class=\"" + mGroupNamePrint + mGroupName + " " + super.BuildExtraClasses() + cursor + ContainerTop + ABMaterial.GetColorStr(Theme.BackColor, Theme.BackColorIntensity, "") + " " + Theme.ZDepth + Collapse + " " + mVisibility + " " + pointerEvents + selectable + mIsPrintableClass + mIsOnlyForPrintClass + mPrintPageBreak + ZAB + "\"" + StyleStr + ">\n");
				}
			}
			if (!Title.equals("")) {
				String rtl="";
				if (this.getRightToLeft()) {
					rtl = " abmrtl right";
				}
				s.append("<label id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "title\" class=\"containertitle" + Theme.ThemeName.toLowerCase() + rtl + "\">" + BuildText(Title) + "</label>\n");
				if (!this.mHasToggleIcon.equals("")) {
					if (this.mIsAllToggleContainer) {
						if (mGroupStartState.equals("")) {
							s.append("<h6 id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-prtgl\" class=\"transparent no-print" + mGroupStartState + "\" style=\"margin-bottom: 0px;width: 16px;margin-left: -16px;position: absolute; cursor: pointer\"><i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-tglicon\" style=\"color: #bdbdbd;margin-left: -10px;margin-top: -10px;margin-bottom: 10px;margin-right: 10px;opacity: " + Theme.NoPrintOpacity + "\" onclick=\"prntglall('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "','" + mGroupName + "'," + Theme.NoPrintOpacity + ")\" class=\"" + mHasToggleIcon + "\"></i>&nbsp;</h6>");						
						} else {
							s.append("<h6 id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-prtgl\" class=\"transparent no-print" + mGroupStartState + "\" style=\"margin-bottom: 0px;width: 16px;margin-left: -16px;position: absolute; cursor: pointer\"><i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-tglicon\" style=\"color: #bdbdbd;margin-left: -10px;margin-top: -10px;margin-bottom: 10px;margin-right: 10px;\" onclick=\"prntglall('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "','" + mGroupName + "'," + Theme.NoPrintOpacity + ")\" class=\"" + mHasToggleIcon + "\"></i>&nbsp;</h6>");
						}
					} else {
						s.append("<h6 id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-prtgl\" class=\"transparent no-print\" style=\"margin-bottom: 0px;width: 16px;margin-left: -16px;position: absolute; cursor: pointer\"><i style=\"color: #bdbdbd;margin-left: -10px;margin-top: -10px;margin-bottom: 10px;margin-right: 10px;\" onclick=\"prntgl('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "'," + Theme.NoPrintOpacity + ")\" class=\"" + mHasToggleIcon + "\"></i>&nbsp;</h6>");
					}
				}
			} else {
				if (!IsForPrint) {
					s.append("<label id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "title\" class=\"hidden\">" + BuildText(Title) + "</label>\n");
				}
				if (!this.mHasToggleIcon.equals("")) {					  
					if (this.mIsAllToggleContainer) {
						if (mGroupStartState.equals("")) {
							s.append("<h6 id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-prtgl\" class=\"transparent no-print" + mGroupStartState + "\" style=\"margin-bottom: 0px;width: 16px;margin-left: -16px;position: absolute; cursor: pointer\"><i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-tglicon\" style=\"color: #bdbdbd;margin-left: -10px;margin-top: -10px;margin-bottom: 10px;margin-right: 10px;opacity: " + Theme.NoPrintOpacity + "\" onclick=\"prntglall('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "','" + mGroupName + "'," + Theme.NoPrintOpacity + ")\" class=\"" + mHasToggleIcon + "\"></i>&nbsp;</h6>");
						} else {
							s.append("<h6 id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-prtgl\" class=\"transparent no-print" + mGroupStartState + "\" style=\"margin-bottom: 0px;width: 16px;margin-left: -16px;position: absolute; cursor: pointer\"><i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-tglicon\" style=\"color: #bdbdbd;margin-left: -10px;margin-top: -10px;margin-bottom: 10px;margin-right: 10px;\" onclick=\"prntglall('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "','" + mGroupName + "'," + Theme.NoPrintOpacity + ")\" class=\"" + mHasToggleIcon + "\"></i>&nbsp;</h6>");
						}
					} else {
						s.append("<h6 id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-prtgl\" class=\"transparent no-print\" style=\"margin-bottom: 0px;width: 16px;margin-left: -16px;position: absolute;cursor: pointer\"><i style=\"color: #bdbdbd;margin-left: -10px;margin-top: -10px;margin-bottom: 10px;margin-right: 10px;\" onclick=\"prntgl('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "'," + Theme.NoPrintOpacity + ")\" class=\"" + mHasToggleIcon + "\"></i>&nbsp;</h6>");
					}
				}
			}
		}

		if (!this.mIsCollapsable) {
			if (!Rows.isEmpty()) {			
				s.append(BuildBody());			
			}
		} else {
			s.append(BuildBody());
		}
		if (!GridIsCleared) {
			s.append("</div>\n");
		}
		GridIsCleared=false;
		
		IsBuild=true;

		return s.toString();
	}
	
	@Override
	protected void SetEventHandler() {
		if (EventHandler!=null) {
			if (!ArrayName.equals("")) {
				Page.EventHandlers.put(ArrayName.toLowerCase(), EventHandler);
			} else {
				Page.EventHandlers.put(ID.toLowerCase(), EventHandler);
			}
				
			if (!mIsCollapsable) {
				for (Entry<String, ABMRow> row : Rows.entrySet()) {	
					row.getValue().SetEventHandlerParent(EventHandler);
				}
			} else {
				this.mCollapseHeading.SetEventHandlerParent(EventHandler);
				this.mCollapseBody.SetEventHandlerParent(EventHandler);
			}
		}
	}
	
	protected void SetEventHandlerExtracontentSidebar() {
		if (EventHandler!=null) {
			if (!ArrayName.equals("")) {
				Page.EventHandlers.put(extra + ArrayName.toLowerCase(), EventHandler);
			} else {
				Page.EventHandlers.put(ID.toLowerCase(), EventHandler);
			}
				
			if (!mIsCollapsable) {
				for (Entry<String, ABMRow> row : Rows.entrySet()) {	
					row.getValue().SetEventHandlerParent(EventHandler);
				}
			} else {
				this.mCollapseHeading.SetEventHandlerParent(EventHandler);
				this.mCollapseBody.SetEventHandlerParent(EventHandler);
			}
		}
	}
	
	@Override
	protected void SetEventHandlerParent(Object parentEventHandler) {
		if (EventHandler==null) {
			EventHandler = parentEventHandler;
		}
		SetEventHandler();
	}
	
	protected String BuildStyle() {
		StringBuilder s = new StringBuilder();
		if (BorderStyle!="") {
			s.append(BorderStyle + "; ");
		}
		if (ExtraStyle!="") {
			s.append(ExtraStyle);
		}
		if (FixedWidth!="") {
			s.append("width: " + FixedWidth + "; ");
		}
		if (FixedHeight!="") {
			s.append("height: " + FixedHeight + "; ");
		}
		if (ModalID.equals("")) {
			if (MarginTop!="") s.append(" margin-top: " + MarginTop + ";");
			if (MarginBottom!="") s.append(" margin-bottom: " + MarginBottom + ";");
			if (MarginLeft!="") s.append(" margin-left: " + MarginLeft + ";");
			if (MarginRight!="") s.append(" margin-right: " + MarginRight + ";");
			if (PaddingTop!="") s.append(" padding-top: " + PaddingTop + ";");
			if (PaddingBottom!="") s.append(" padding-bottom: " + PaddingBottom + ";");
			if (PaddingLeft!="") s.append(" padding-left: " + PaddingLeft + ";");
			if (PaddingRight!="") s.append(" padding-right: " + PaddingRight + ";");
		}
		if (ContainerTop!=Integer.MIN_VALUE) {
			s.append(" position: relative; top: " + ContainerTop + "px;");
		}
		if (!s.toString().contains("display:")) {
			if (ContentOpen) {
				s.append( "display: block;");			
			} else {
				s.append( "display: none;");
			}
		}
		
		if (!FixedPosition.equals("") && ContainerTop==Integer.MIN_VALUE) {
			s.append(FixedPosition);
		}
		
		return s.toString();
	}
	
	@Hide
	protected String BuildBody() {
		StringBuilder s = new StringBuilder();
		if (!mIsCollapsable) {			
			for (Entry<String, ABMRow> row : Rows.entrySet()) {
				row.getValue().SetZAB(ZABMode);
				s.append(row.getValue().Build());							
	        }
			return s.toString();
		} else {
			s.append("<div class=\"collapse-card__heading\">");
			s.append(this.mCollapseHeading.Build());
			s.append("</div>");
			String StyleStr="";
			if (this.mIsCollapsable) {
				if (!this.mBodyCollapsed) {
					StyleStr = " style=\"display: block\" ";
				}
			}
			s.append("<div class=\"collapse-card__body\" " + StyleStr + ">");
			s.append(this.mCollapseBody.Build());
			s.append("</div>");
			return s.toString();
		}
		
	}
		
	@Override
	public void Refresh() {
		RefreshInternalExtra(false,true, true);
	}
	
	/**
	 * This is a limited version of Refresh() which will only refresh the container properties + each row properties + each cell properties, NOT its contents.		 
	 */
	public void RefreshNoContents(boolean DoFlush) {
		// TODO extraClasses not working
		super.RefreshInternal(DoFlush);
		
		getVisibility();
		
		AddGridDefinition(Grid); // new 160629
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		ABMaterial.ChangeVisibility(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), mVisibility);
		ABMaterial.ChangeCursor(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), mCursor);
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
		if (!ZABMode) {
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "zab");
		} else {
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "zab");
		}
		if (!mIsResponsiveContainer) {
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "responsive-cont" + mThresholdName);
		} else {
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "responsive-cont" + mThresholdName);
		}
		if (PrevTheme!=null) {
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), PrevTheme.BackColor);
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), PrevTheme.BackColorIntensity);
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), Theme.BackColor);
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), Theme.BackColorIntensity);
			PrevTheme=null;
		}
		
		j.SetProp("style", BuildStyle());	
		
		String rtl="";
		if (this.getRightToLeft()) {
			rtl = " abmrtl right";
		}
		j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "title");
		if (!Title.equals("")) {
			j.SetProp("class", "containertitle" + Theme.ThemeName.toLowerCase() + rtl);
			j.SetHtml(BuildText(Title));
		} else {
			j.SetProp("class", "hidden");
			j.SetHtml(BuildText(Title));
		}
		
		String sDragDrop = "";
		for (Entry<String,String> entry: mDraggableTo.entrySet()) {
			sDragDrop += entry.getKey() + ":";
		}
		if (!sDragDrop.equals("")) {
			sDragDrop = ":" + sDragDrop;
		}
		if (!sDragDrop.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-dropto", sDragDrop);
		} else {
			ABMaterial.RemoveProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-dropto");
		}
		
		UpdateDragDropGroups();
			
		if (!mIsCollapsable) {
			for (Entry<String, ABMRow> row : Rows.entrySet()) {	
				row.getValue().SetZAB(ZABMode);
				row.getValue().RefreshNoContents(false);
			}
		} else {
			this.mCollapseHeading.RefreshNoContents(false);
			this.mCollapseBody.RefreshNoContents(false);
		}
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("RefreshNoContents: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
		
	}
	
	@Hide
	protected String BuildText(String Text) {
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
	
	protected void UpdateDragDropGroups() {
		StringBuilder ss = new StringBuilder();
		if (Page.dragDropGroups.size()>0) {
			ss.append("window.removeEventListener( 'touchmove', function() {});");
			ss.append("window.addEventListener( 'touchmove', function() {});");
			ss.append("if (drake) {drake.destroy();}\n");
			ss.append("drake=dragula([");
			int cellTell=0;
			String headerIDs="";
			String footerIDs="";
			for (Entry<String,ABMDragDropGroup> entry: Page.dragDropGroups.entrySet()) {
				ABMDragDropGroup ddg = entry.getValue();
				for (Entry<String,ABMCell> entryCell: ddg.Cells.entrySet()) {
					Page.DragDropCells.put(entryCell.getValue().ParentString + entryCell.getValue().RootID(), entryCell.getValue());
					if (cellTell>0) {
						ss.append(",document.querySelector('#" + entryCell.getValue().ParentString + entryCell.getValue().RootID() + "')");
					} else {
						ss.append("document.querySelector('#" + entryCell.getValue().ParentString + entryCell.getValue().RootID() + "')");
					}
					for (Entry<String,ABMComponent> entryComp: entryCell.getValue().Components.entrySet()) {
						ABMComponent comp = entryComp.getValue();
						if (comp.DragDropIsHeader) {
							if (headerIDs.equals("")) {
								headerIDs = ":" + comp.getID() + ":";
							} else {
								headerIDs = headerIDs + comp.getID() + ":";
							}
						}
					}
					cellTell++;
				}
			}
			ss.append("]\n, {");
			ss.append("isContainer: function (el) {");
			ss.append("return false;");
			ss.append("},");
			ss.append("moves: function (el, source, handle, sibling) {");
			ss.append("return $(el).attr('data-dropto');");
			ss.append("},");
			ss.append("accepts: function (el, target, source, sibling) {");
			ss.append("if ($(el).attr('data-dropto')) { ");
			ss.append("return ($(el).attr('data-dropto').indexOf($(target).attr('data-dgn')) !== -1);");
			ss.append("}\n");
			ss.append("return false;");
			ss.append("},");
			ss.append("invalid: function (el, handle) {");
			ss.append("if ($(el).hasClass('gm-style')) {return true;}");
			ss.append("return false;");
			ss.append("},");				
			ss.append("direction: 'vertical',");             
			ss.append("copy: false,");          
			ss.append("copySortSource: false,");             
			ss.append("revertOnSpill: true,");              
			ss.append("removeOnSpill: false,");              
			ss.append("mirrorContainer: document.body,");    
			ss.append("ignoreInputTextSelection: true,");
			ss.append("headerIDs: '" + headerIDs + "',");
			ss.append("footerIDs: '" + footerIDs + "',");
			ss.append("}).on('drop', function(el, target, source, sibling) {");
			for (Entry<String,ABMDragDropGroup> entry: Page.dragDropGroups.entrySet()) {
				ABMDragDropGroup ddg = entry.getValue();
				ss.append("$(\"[data-ddgn='" + entry.getKey() + "']\").matchHeight({minHeight: " + ddg.minHeight + "});");
			}
			ss.append("if (sibling) {");
			ss.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_dropped','eventparams':'component,source,target,before','component':$(el).attr('id'), 'source':$(source).attr('id'), 'target': $(target).attr('id'), 'before': $(sibling).attr('id')});");
			ss.append("} else {");
			ss.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_dropped','eventparams':'component,source,target,before','component':$(el).attr('id'), 'source':$(source).attr('id'), 'target': $(target).attr('id'), 'before': ''});");
			ss.append("}");

			ss.append("}).on('drag', function(el,source) {;\n");
			ss.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_dragstart','eventparams':'component,source','component':$(el).attr('id'), 'source':$(source).attr('id')});");		

			ss.append("}).on('cancel', function(el,container,source) {;\n");
			ss.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_dragcancelled','eventparams':'component,source','component':$(el).attr('id'), 'source':$(source).attr('id')});");		
			ss.append("});\n");
			for (Entry<String,ABMDragDropGroup> entry: Page.dragDropGroups.entrySet()) {
				ABMDragDropGroup ddg = entry.getValue();
				ss.append("$(\"[data-ddgn='" + entry.getKey() + "']\").matchHeight({minHeight: " + ddg.minHeight + "});");
			}		
			
		}
		if (!ss.toString().equals("")) {
			if (Page.ws!=null) {		
				if (Page.ws.getOpen()) {
					Page.ws.Eval(ss.toString(), null);
				}
			}
		}
	}
		
	protected void RefreshInternalExtra(boolean onlyNew, boolean DoTheChecks, boolean DoFlush) {
		super.RefreshInternal(DoFlush);
		
		getVisibility();
		
		AddGridDefinition(Grid); // new 160629
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		ABMaterial.ChangeVisibility(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), mVisibility);
		ABMaterial.ChangeCursor(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), mCursor);
		if (this.mHasToggleIcon.equals("")) {
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
		}
		if (!ZABMode) {
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "zab");
		} else {
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "zab");
		}
		if (!mIsResponsiveContainer) {
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "responsive-cont" + mThresholdName + " ");
		} else {
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "responsive-cont" + mThresholdName + " ");
		}
		if (PrevTheme!=null) {
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), PrevTheme.BackColor);
			ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), PrevTheme.BackColorIntensity);
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), Theme.BackColor);
			ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), Theme.BackColorIntensity);
			PrevTheme=null;
		}
		
		j.SetProp("style", BuildStyle());
		String rtl="";
		if (this.getRightToLeft()) {
			rtl = " abmrtl right";
		}
		j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "title");
		if (!Title.equals("")) {
			j.SetProp("class", "containertitle" + Theme.ThemeName.toLowerCase() + rtl);
			j.SetHtml(BuildText(Title));
		} else {
			j.SetProp("class", "hidden");
			j.SetHtml(BuildText(Title));
		}
		
		String sDragDrop = "";
		for (Entry<String,String> entry: mDraggableTo.entrySet()) {
			sDragDrop += entry.getKey() + ":";
		}
		if (!sDragDrop.equals("")) {
			sDragDrop = ":" + sDragDrop;
		}
		if (sDragDrop.equals("")) {
			ABMaterial.RemoveProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-dropto");
		} else {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-dropto", sDragDrop);
		}
		if (!BackgroundImage.equals("")) {
			Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').css(" + BackgroundImage + ");", null);
		}
		
		UpdateDragDropGroups();
		
		int Counter=0;
		String prevID="";
		boolean isContainer=false;
		if (!mIsCollapsable) {
			for (Entry<String, ABMRow> row : Rows.entrySet()) {	
				row.getValue().SetZAB(ZABMode);
				
				if (row.getValue().IsBuild) {
					row.getValue().RefreshInternal(onlyNew, false, false);	
					prevID = row.getValue().RootID();
					isContainer = row.getValue().CenterInPage;
				} else {
					GridIsCleared = true;
					if (Counter==0) {						
						ABMaterial.AddHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), row.getValue().Build());
						prevID = row.getValue().RootID();
						isContainer = row.getValue().CenterInPage;
					} else {
						if (isContainer) {
							ABMaterial.InsertHTMLAfter(Page, row.getValue().ParentString + prevID + "_cont", row.getValue().Build());
						} else {
							ABMaterial.InsertHTMLAfter(Page, row.getValue().ParentString + prevID, row.getValue().Build());
						}
						
						prevID = row.getValue().RootID();			
						isContainer = row.getValue().CenterInPage;
					}						
				}				
				Counter++;
			}
		} else {
			this.mCollapseHeading.RefreshInternalExtra(onlyNew, DoTheChecks, false);
			this.mCollapseBody.RefreshInternalExtra(onlyNew, DoTheChecks,false);
		}
		if (GridIsCleared) {
			Page.ws.Eval("initplugins();", null);
			RunAllFirstRunsInternal(false);
		} else {
			Page.ws.RunFunction("initrowcellclicks", null);
		}
		GridIsCleared=false;
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Container RefreshInternal: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
		
		
	}
	
	@Override
	public void Prepare() {
		IsBuild=true;
		
		for (Entry<String, ABMRow> row : Rows.entrySet()) {
			row.getValue().Prepare();							
        }
	}
	
	@Override
	protected void RunAllFirstRuns() {
		RunAllFirstRunsInternal(true);
	}
	
	protected void RunAllFirstRunsInternal(boolean DoFlush) {
		if (!mIsCollapsable) {
			for (Entry<String, ABMRow> row : Rows.entrySet()) {
				row.getValue().RunAllFirstRunsInternal(false);							
			}
		} else {
			this.mCollapseHeading.RunAllFirstRunsInternal(false);
			this.mCollapseBody.RunAllFirstRunsInternal(false);
		}
		FirstRun();
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Container RunAllFirstRunsInternal: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	protected void CleanUp() {
		for (Map.Entry<String, ABMRow> entry : this.Rows.entrySet()) {			
			entry.getValue().CleanUp();
		}
		this.Rows.clear();
		this.Page=null;
	}	
	
	@Override
	protected void RemoveMe() {
		for (Entry<String, ABMRow> row : Rows.entrySet()) {
			row.getValue().RemoveAllComponents();							
		}
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
	}
	
	protected void RemoveMeHTMLOnly() {
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
	}
	
	
	public void RemoveAllComponents() {
		for (Entry<String, ABMRow> row : Rows.entrySet()) {
			row.getValue().RemoveAllComponents();							
		}
	}
	
	@Override
	protected void FirstRun() {
		StringBuilder s = new StringBuilder();
		if (this.mIsCollapsable) {
			s.append("$('.collapse-card').paperCollapse();");
		}
		
		int SML600=ABMaterial.ThresholdPxConsiderdSmall;
    	int SML992=ABMaterial.ThresholdPxConsiderdMedium;
    	int SML993=SML992+1;
    	
		if (this.ContainerTopSmall!=Integer.MIN_VALUE) {
			s.append("$('body').append(\"<style type='text/css'>");
			s.append("@media only screen and (min-width : " + SML993 + "px) {.container" + ID.toLowerCase() + " {position: relative;top: " + ContainerTopLarge + "px !important;}}");
			s.append("@media only screen and (min-width: " + SML600 + "px) and (max-width: 992px) {.container" + ID.toLowerCase() + " {position: relative;top: " + ContainerTopMedium + "px !important;}}");
			s.append("@media only screen and (max-width : " + SML600 + "px) {.container" + ID.toLowerCase() + " {position: relative;top: " + ContainerTopSmall + "px !important;}}");
			s.append("</style>\");");			
		}
		if (!s.toString().equals("")) {
			Page.ws.Eval(s.toString(), null);
		}
		if (!BackgroundImage.equals("")) {
			Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').css(" + BackgroundImage + ");", null);
		}
		Page.ws.RunFunction("initrowcellclicks", null);
		super.FirstRun();
	}
	
	protected ABMContainer Clone() {
		ABMContainer c = new ABMContainer();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;
		c.Theme = Theme.Clone();
		c.Type = Type;
		c.mVisibility = mVisibility;	
		c.ParentString = ParentString;
		for (Map.Entry<String, ABMRow> entry : Rows.entrySet()) {
			Rows.put(entry.getKey(), (ABMRow) entry.getValue().Clone());
		}
		return c;
		
	}
	
	protected void Copy(ABMContainer c) {
		this.AllComponents = new LinkedHashMap<String, ABMRowCell>();
		this.Rows = new LinkedHashMap<String,ABMRow>();
		this.Theme=c.Theme.Clone();
		this.Grid = c.Grid.Clone();
		this.CurrentRow=c.CurrentRow;
		this.mIsAnimatable=c.mIsAnimatable;
		this.mInitialAnimationName=c.mInitialAnimationName;
		this.mLastAnimationRunned=c.mLastAnimationRunned;
		this.BorderStyle=c.BorderStyle;
		this.FixedWidth=c.FixedWidth;		
		this.FixedHeight=c.FixedHeight;
		this.FixedPosition=c.FixedPosition;
		this.mIsCollapsable=c.mIsCollapsable;
		this.mBodyCollapsed=c.mBodyCollapsed;
		this.ContainerTop=c.ContainerTop;
		this.ContainerTopSmall=c.ContainerTopSmall;
		this.ContainerTopMedium=c.ContainerTopMedium;
		this.ContainerTopLarge=c.ContainerTopLarge;
		this.PaddingLeft=c.PaddingLeft; //"0.75rem";
		this.PaddingRight=c.PaddingRight; //"0.75rem";
		this.PaddingTop=c.PaddingTop; //"0rem";
		this.PaddingBottom=c.PaddingBottom; //"0rem";
		this.MarginTop=c.MarginTop; //"0px";
		this.MarginBottom=c.MarginBottom; //"0px";
		this.MarginLeft=c.MarginLeft; //"0px";
		this.MarginRight=c.MarginRight; //"0px";
		this.LayoutName=c.LayoutName;
		this.ModalID=c.ModalID;
		this.ID=c.ID;
		this.ArrayName=c.ArrayName;
		this.CellId=c.CellId;	
		this.RowId=c.RowId;
		this.IsBuild=c.IsBuild;
		this.mVisibility=c.mVisibility;
		this.ParentString=c.ParentString;
		this.CellAlign=c.CellAlign;
		this.TreeNeedsFirstRun=c.TreeNeedsFirstRun;
		this.mRootID=c.mRootID;
		this.FlexWallExtra=c.FlexWallExtra;
		this.beforeID=c.beforeID;
		this.IsInitialized=c.IsInitialized;
		this.Type=c.Type;	
		this.LoadAtConnect=c.LoadAtConnect;
		this.ZABMode = c.ZABMode;
	}
		
	
}
