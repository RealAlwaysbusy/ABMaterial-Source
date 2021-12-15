package com.ab.abmaterial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import anywheresoftware.b4a.BA;
//import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;
import anywheresoftware.b4j.object.WebSocket.SimpleFuture;

@Author("Alain Bailleul")  
@ShortName("ABMInput")
@Events(values={"EnterPressed(value As String)","LostFocus()","GotFocus()","Changed(value As String)","ChangedArray(target As String, value As String)", "TabPressed(target As String, value As String)", "AutoCompleteClicked(uniqueId as String)","ResetClicked(Target As String)", "Clicked(Target As String)"})
public class ABMInput extends ABMComponent {	
	private static final long serialVersionUID = 6495650748155900055L;
	protected ThemeInput Theme=new ThemeInput();
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	protected String sText="";
	public String Title="";		
	protected String mInputType=ABMaterial.INPUT_TEXT;
	protected boolean mValidate=true;
	protected boolean mEnabled=true;
	public String IconName="";
	public String IconAwesomeExtra= "";
	public String SuccessMessage="";
	public String WrongMessage="";
	public String PlaceHolderText="";
	protected int SizeSmall=12;
	protected int SizeLarge=12;
	protected int SizeMedium=12;	
	protected int OffsetSmall=0;
	protected int OffsetMedium=0;
	protected int OffsetLarge=0;
	protected transient SimpleFuture FutureText;
	protected transient SimpleFuture FutureTextValue;
	protected boolean IsTextArea=false;
	
	protected String mInputMask="";
	public boolean ReadOnly=false;
	public boolean AutoCompleteShowArrow=false;
	
	protected boolean IsDirty=false;
	protected boolean GotLastText=true;
	
	protected String PositionType="";
	protected int px=0;
	protected int py=0;
	protected boolean IsFileInput=false;
	protected boolean IsFileInputDirectory=false;
	protected String IDFileInput="";
	public String Align=ABMaterial.INPUT_TEXTALIGN_START;
	protected transient List<String> mAutoComplete = new ArrayList<String>();
	protected transient List<String> mAutoCompleteIDs = new ArrayList<String>();
	public String AutoCompleteType=ABMaterial.AUTOCOMPLETE_CONTAINS;
	public boolean AutoCompleteOpenOnFocus=false;
	public boolean DidGoogleAuto=false;
	public boolean Narrow=false;
	private transient ABMList ForwardList=null;
	public boolean RaiseChangedEvent=false;
	public boolean RaiseFocusEvent=true;
	public double NumberStep=1;
	
	public boolean PreventClickPropagation=false;
	
	protected String UseBrowserAutocomplete="";
	
	protected String Accepts=""; // does not work
	
	protected String IsValid="";
		
	protected Map<String,ABMInput> GoogleAutoCompleteFields = new LinkedHashMap<String,ABMInput>();
	
	protected DateOptions mDate = new DateOptions();
	
	public int CharacterCounter=0;
	public int MaxCharacters=0;
	
	public boolean AllowTabInTextArea=false;
	public boolean RemoveBottomLine=false;
	public boolean DisableSpellcheck=false;
	public boolean ShowRequiredMark=false;
	public boolean ShowDropdownIcon=false;
	public boolean ShowSidebarIcon=false;
	
	public boolean RaiseClickedEvent=false;
	protected boolean IsDirtyInputMask=true;
		
	public void Initialize(ABMPage page, String id, String inputType, String title, boolean isTextArea, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_INPUTFIELD;
		if (isTextArea) {
			this.mInputType = ABMaterial.INPUT_TEXTAREA;
		} else {
			this.mInputType = inputType;
		}		
		this.Title = title;
		this.SizeSmall = 12;
		this.SizeMedium = 12;
		this.SizeLarge = 12;
		this.OffsetSmall=0;
		this.OffsetMedium=0;
		this.OffsetLarge=0;
		this.IsTextArea = isTextArea;
		UseBrowserAutocomplete = ABMaterial.UseBrowserAutocomplete;
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Inputs.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Inputs.get(themeName.toLowerCase()).Clone();				
			}
		}			
		IsInitialized=true;
		IsDirty=false;
		IsVisibilityDirty=false;
		IsEnabledDirty=false;
	}
	
	public String getInputMask() {
		return mInputMask;
	}
	
	public void setInputMask(String s) {
		if (!s.equals(mInputMask)) {
			IsDirtyInputMask=true;
			mInputMask=s;
		}
	}
	
	public void setValid(String valid) {
		IsValid = valid;		
		mValidate=false;
	}
	
	public String getValid() {
		return IsValid;
	}
	
	public void setValidate(boolean valid) {
		this.mValidate=valid;
		IsValid="";
	}

	public void EnableBrowserAutocomplete(boolean enable) {
		if (enable) {
			this.UseBrowserAutocomplete="";
		} else {
			this.UseBrowserAutocomplete=" autocomplete=\"off\" ";
		}
	}
				
	/**
	 * 
	 * uniqueId: this id will be raised if the user clicks an item from the list 
	 */
	public void AddAutoComplete(String uniqueId, String value) {
		mAutoCompleteIDs.add(uniqueId);
		mAutoComplete.add(value);
	}	
	
	public void ClearAutoComplete() {
		mAutoComplete = new ArrayList<String>();
		mAutoCompleteIDs = new ArrayList<String>();
	}
	
	public boolean getEnabled() {	
		if (GotLastEnabled) {
			return mEnabled;
		}
		GotLastEnabled=true;
		if (!mB4JSUniqueKey.equals("") && !IsEnabledDirty) {
			if (JQ!=null) {
				anywheresoftware.b4a.objects.collections.List par = new anywheresoftware.b4a.objects.collections.List();
				par.Initialize();
				par.Add(ParentString + RootID());
				FutureEnabled = JQ.RunMethodWithResult("GetEnabled", par);				
			} else {
				FutureEnabled=null;				
			}
			if (!(FutureEnabled==null)) {
				try {
					this.mEnabled = ((Boolean)FutureEnabled.getValue());					
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
			return mEnabled;
		}
		return mEnabled;
	}
	
	public void setEnabled(boolean enabled) {
		IsEnabledDirty=true;
		GotLastEnabled=true;
		mEnabled=enabled;
	}
	
	public boolean getValidate() {
		return mValidate;
	}
	
	public void SetGoogleAutocompleteResultInputComponent(ABMInput component, String GoogleAutocompleteResultType) {
		GoogleAutoCompleteFields.put(GoogleAutocompleteResultType, component);
	}
		
	@Override
	protected void ResetTheme() {
		UseTheme(Theme.ThemeName);
	}
	
	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase() + "-parent";
	}
	
	/**
	 * every method you want to call with a B4JSOn... call MUST return a boolean
	 * returning true will consume the event in the browser and not call the B4J event (if any)
	 * 
	 * Note: If the RaiseFocusEvent = false, this will still work, just not raise to the server
	 *
	 * e.g. myButton.B4JSOnClicked("MyJavascript", "AddToLabel", Array As Object(myCounter))
	 * if AddToLabel return true, then myButton_Clicked() will not be raised.
	 * if AddToLabel returns false, then myButton_Clicked() will be raised AFTER the B4JS method is done.
	 * 
	 * public Sub AddToLabel(MyCounter As Int) As Boolean
	 *      if myCounter mod 2 = 0 then	       
	 *		   Return True
	 *      else
	 *         Return False
	 *      end if
	 * End Sub
	 */
	public void B4JSOnGotFocus(String B4JSClassName, String B4JSMethod, anywheresoftware.b4a.objects.collections.List Params) {
		PrepareEvent("B4JSGotFocus", B4JSClassName, B4JSMethod, Params);	
	}
	
	/**
	 * every method you want to call with a B4JSOn... call MUST return a boolean
	 * returning true will consume the event in the browser and not call the B4J event (if any)
	 * 
	 * Note: If the RaiseFocusEvent = false, this will still work, just not raise to the server
	 *
	 * e.g. myButton.B4JSOnClicked("MyJavascript", "AddToLabel", Array As Object(myCounter))
	 * if AddToLabel return true, then myButton_Clicked() will not be raised.
	 * if AddToLabel returns false, then myButton_Clicked() will be raised AFTER the B4JS method is done.
	 * 
	 * public Sub AddToLabel(MyCounter As Int) As Boolean
	 *      if myCounter mod 2 = 0 then	       
	 *		   Return True
	 *      else
	 *         Return False
	 *      end if
	 * End Sub
	 */
	public void B4JSOnLostFocus(String B4JSClassName, String B4JSMethod, anywheresoftware.b4a.objects.collections.List Params) {
		PrepareEvent("B4JSLostFocus", B4JSClassName, B4JSMethod, Params);	
	}
	
	/**
	 * Needs at least the param ABM.B4JS_PARAM_KEY or ABM.B4JS_PARAM_KEYCODE
	 * 
	 * every method you want to call with a B4JSOn... call MUST return a boolean
	 * returning true will consume the event in the browser and not call the B4J event (if any)
	 *
	 * e.g. myButton.B4JSOnClicked("MyJavascript", "AddToLabel", Array As Object(myCounter))
	 * if AddToLabel return true, then myButton_Clicked() will not be raised.
	 * if AddToLabel returns false, then myButton_Clicked() will be raised AFTER the B4JS method is done.
	 * 
	 * public Sub AddToLabel(MyCounter As Int) As Boolean
	 *      if myCounter mod 2 = 0 then	       
	 *		   Return True
	 *      else
	 *         Return False
	 *      end if
	 * End Sub
	 */
	public void B4JSOnKeyUp(String B4JSClassName, String B4JSMethod, anywheresoftware.b4a.objects.collections.List Params) {
		PrepareEvent("B4JSOnKeyUp", B4JSClassName, B4JSMethod, Params);				
	}
	
	/**
	 * Needs at least the param ABM.B4JS_PARAM_INPUTKEY or ABM.B4JS_PARAM_INPUTKEYCODE
	 * 
	 * every method you want to call with a B4JSOn... call MUST return a boolean
	 * returning true will consume the event in the browser and not call the B4J event (if any)
	 *
	 * e.g. myButton.B4JSOnClicked("MyJavascript", "AddToLabel", Array As Object(myCounter))
	 * if AddToLabel return true, then myButton_Clicked() will not be raised.
	 * if AddToLabel returns false, then myButton_Clicked() will be raised AFTER the B4JS method is done.
	 * 
	 * public Sub AddToLabel(MyCounter As Int) As Boolean
	 *      if myCounter mod 2 = 0 then	       
	 *		   Return True
	 *      else
	 *         Return False
	 *      end if
	 * End Sub
	 */
	public void B4JSOnKeyDown(String B4JSClassName, String B4JSMethod, anywheresoftware.b4a.objects.collections.List Params) {
		PrepareEvent("B4JSOnKeyDown", B4JSClassName, B4JSMethod, Params);
	}
		
	public void SetFocus() {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());		
		Page.ws.RunFunction("inputsetfocus", Params);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("Input SetFocus: " + ID);}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public void B4JSSetFocus() {
		
	}
	
	public void InitializeWithSize(ABMPage page, String id, String inputType, String title, int offsetSmall, int offsetMedium, int offsetLarge, int sizeSmall, int sizeMedium, int sizeLarge, boolean isTextArea, String themeName) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_INPUTFIELD;
		if (isTextArea) {
			this.mInputType = ABMaterial.INPUT_TEXTAREA;
		} else {
			this.mInputType = inputType;
		}
		this.Title = title;
		this.SizeSmall = sizeSmall;
		this.SizeMedium = sizeMedium;
		this.SizeLarge = sizeLarge;
		this.OffsetSmall=offsetSmall;
		this.OffsetMedium=offsetMedium;
		this.OffsetLarge=offsetLarge;
		this.IsTextArea = isTextArea;
		Theme = new ThemeInput(inputType);
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Inputs.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Inputs.get(themeName.toLowerCase()).Clone();				
			}
		}			
		IsDirty=false;
		IsVisibilityDirty=false;
		IsEnabledDirty=false;
	}	
	
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}
	
	public void UseTheme(String themeName) {
		if (!themeName.equals("")) {
			if (Page.CompleteTheme.Inputs.containsKey(themeName.toLowerCase())) {
				Theme = Page.CompleteTheme.Inputs.get(themeName.toLowerCase()).Clone();				
			}
		}
	}
	
	public void SetRelativePosition(String positionType, int x, int y) {
		this.PositionType = positionType;
		this.px = x;
		this.py = y;
	}
	
	public void setText(String text) {
		this.sText = text;	
		this.IsDirty = true;
	}
	
	public String getText() {			
		if (GotLastText) {
			return sText;
		}
		GotLastText=true;
		if (!IsDirty) {			
			if (!(FutureText==null)) {
				try {
					this.sText = (String) FutureText.getValue();					
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
			if (sText.equals("")) {			
				if (!(FutureTextValue==null)) {
					try {
						this.sText = (String) FutureTextValue.getValue();					
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
					//BA.Log("FutureTextValue = null");
					
				}
			}
		}
		return sText;
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
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public void setB4JSText(String text) {
		
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public String getB4JSText() {
		return "B4JS Dummy";
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public void setB4JSVisibility(String visibility) {
		
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public String getB4JSVisibility() {
		return "B4JS Dummy";
	}
	
	public void SetTooltip(String text, String position, int delay) {
		this.ToolTipText = text;
		this.ToolTipPosition = position;
		this.ToolTipDelay = delay;			
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public boolean getB4JSEnabled() {		
		return false;
	}
	
	/**
	 * ONLY TO USE IN A B4JS CLASS!
	 */
	public void setB4JSEnabled(boolean enabled) {
		
	}
	
	@Override
	protected void CleanUp() {
		super.CleanUp();
	}
	
	@Override
	protected void RemoveMe() {
		ABMaterial.RemoveHTML(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent");
	}
	
	@Override
	protected void FirstRun() {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());		
		Page.ws.RunFunction("inittooltipped", Params);
		Page.ws.Eval(BuildJavaScript(), null);
		anywheresoftware.b4a.objects.collections.List Params2 = new anywheresoftware.b4a.objects.collections.List();
		Params2.Initialize();		
		Page.ws.RunFunction("reinitinputfields", Params2);
		
		if (IsDirtyInputMask) {
			if (!mInputMask.equals("")) {
				if (ABMaterial.InputMaskNewVersion==false) {
					Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').inputmask();", null);
				} else {
					Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').inputmask('remove');$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').inputmask(" + mInputMask + ");", null);
				}
			}
		}
		if (CharacterCounter>0) {
			Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').parent().find('span[class=\"character-counter\"]').remove();", null);
			Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').characterCounter();", null);
		}
		super.FirstRun();

	}
	
	protected String BuildJavaScriptForward() {
		StringBuilder s = new StringBuilder();
		if (this.ForwardList!=null) {
			s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').keydown(function(e) {");
			s.append("if (e.keyCode == 13) {");					
			s.append("sendlistclickarray('enter', '" + ForwardList.ParentString + "','" + ForwardList.ArrayName.toLowerCase() + "','" + ForwardList.ArrayName.toLowerCase() + ForwardList.ID.toLowerCase() + "');");		        
			s.append("}");
			s.append("if (e.keyCode == 38) {");
			s.append("sendlistclickarray('up', '" + ForwardList.ParentString + "','" + ForwardList.ArrayName.toLowerCase() + "','" + ForwardList.ArrayName.toLowerCase() + ForwardList.ID.toLowerCase() + "');");					
			s.append("}");
			s.append("if (e.keyCode == 40) {");
			s.append("sendlistclickarray('down', '" + ForwardList.ParentString + "','" + ForwardList.ArrayName.toLowerCase() + "','" + ForwardList.ArrayName.toLowerCase() + ForwardList.ID.toLowerCase() + "');");
			s.append("}");
			s.append("});");			
		}
		return s.toString();
	}
	
	protected String BuildJavaScript() {
		StringBuilder s = new StringBuilder();
		if (PreventClickPropagation) {
			s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').click(function(ev) {");
			s.append("ev.stopPropagation();");
			s.append("});");
		}
		if (mAutoComplete.size()==0) {
			if (this.AutoCompleteType.equals(ABMaterial.AUTOCOMPLETE_GOOGLE) && !DidGoogleAuto) {
				if (Page.GoogleMapsAPIExtras.equals("")) {
					BA.Log("Warning: No Google Key is set!");
				}
				s.append("function GetPlaceValue(place, ptype) {");
				s.append("for (var i = 0; i < place.address_components.length; i++) {");
				s.append("var addressType = place.address_components[i].types[0];");
				s.append("if (addressType==ptype) {");
				s.append("var val = place.address_components[i].long_name;");
				s.append("return val;");
				s.append("}");
				s.append("}");
				s.append("return \"\";");
				s.append("}\n");
				
				String tmpVar = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
				tmpVar = tmpVar.replace("-", "_");
				
				s.append("function fillInAddress" + tmpVar + "() {\n");
				s.append("var searchbox = googleac['" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "'];\n");
				s.append("var places = searchbox.getPlaces();\n");
				s.append("if (places == undefined) {\n");
				s.append("return;\n");
				s.append("}\n");
				s.append("var j=0;\n");
				s.append("var place=places[j];\n");
				s.append("while ((j+1)<places.length && place.address_components==undefined) {\n");
				s.append("j++;\n");
				s.append("place=places[j];\n");
				s.append("}\n");
				s.append("if (place.address_components==undefined) {return;}");
				s.append("console.log(place.address_components);\n");
				if (GoogleAutoCompleteFields.size()>0) {
					for (Entry<String,ABMInput> entry: GoogleAutoCompleteFields.entrySet()) {
						String myID=entry.getValue().ParentString + entry.getValue().ArrayName.toLowerCase() + entry.getValue().ID.toLowerCase();
						s.append("document.getElementById('" + myID + "').value = '';\n");
						s.append("document.getElementById('" + myID + "').disabled = false;\n");
						if (entry.getKey().equals(ABMaterial.GOOGLE_AUTOCOMLETE_RESULTTYPE_LOCATION)) {
							s.append("if (place.geometry) {\n");
							s.append("var pvalue=place.geometry.location.lat() + ';' + place.geometry.location.lng();");
							s.append("} else {\n");
							s.append("var pvalue='';\n");
							s.append("}\n");
						} else {
							s.append("var pvalue = GetPlaceValue(place, '" + entry.getKey() + "');\n");
						}
						s.append("document.getElementById('" + myID + "').value = pvalue;\n");  
					}
				}  					  
				s.append("Materialize.updateTextFields();\n");	
				s.append("}\n");
				
				s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').focus(function() {\n");
				s.append("$('.pac-container').remove();\n");
				s.append("googleac['" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "']= new google.maps.places.SearchBox((document.getElementById('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "')));\n");
				s.append("googleac['" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "'].addListener('places_changed', fillInAddress" + tmpVar + ");\n");
				s.append("});\n");
				 
				DidGoogleAuto=true;
				return s.toString();
			} else {
				if (IsDirtyInputMask) {
					if (!mInputMask.equals("")) {
						if (ABMaterial.InputMaskNewVersion==false) {
							s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').inputmask();");
						} else {
							s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').inputmask('remove');$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').inputmask(" + mInputMask + ");");
						}
					}
				}
				if (CharacterCounter>0) {
					Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').parent().find('span[class=\"character-counter\"]').remove();", null);
					Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').characterCounter();", null);
				}
				return s.toString();
			}		
		}
		String ss="";
		
		String tmpVar = ParentString + ArrayName.toLowerCase() + ID.toLowerCase();
		tmpVar = tmpVar.replace("-", "_");
		s.append("var " + tmpVar + "=[");
		for (int i=0;i<mAutoComplete.size();i++) {
			if (i>0) {
				s.append(",");
			}
			ss = (String) mAutoComplete.get(i);
			s.append("{value: \"" + ss + "\", id: \"" + (String)mAutoCompleteIDs.get(i) + "\"}");
		}
		s.append("];");
		s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').data('array', " + tmpVar + ");");
		
		s.append("runautocomplete('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "', '" + Theme.ThemeName + " " + Theme.AutoCompleteZDepth + "', '" + AutoCompleteType + "');");
		if (IsDirtyInputMask) {
			if (!mInputMask.equals("")) {
				if (ABMaterial.InputMaskNewVersion==false) {
					s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').inputmask();");
				} else {
					s.append("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').inputmask('remove');$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').inputmask(" + mInputMask + ");");
				}
			}
		}
		if (CharacterCounter>0) {
			Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').parent().find('span[class=\"character-counter\"]').remove();", null);
			Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').characterCounter();", null);
		}

        
		return s.toString();
	}
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		super.RefreshInternal(DoFlush);
		getText();
		getVisibility();
		getEnabled();
		
		Page.ws.Eval("allobjectids['" + ID.toLowerCase() + "']='#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "';", null);
		JQueryElement j=null;
		ThemeInput in = Theme;
		if (!this.IsFileInput) {
			j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent");			
			
			j.SetProp("class", BuildClassParent(in));
			String position="";
			switch (PositionType) {
			case ABMaterial.POSITION_TOPLEFT:
				position = "position: absolute; top: " + py + "px; left: " + px + "px";
				break;
			case ABMaterial.POSITION_TOPRIGHT:
				position = "position: absolute; top: " + py + "px; right: " + px + "px";
				break;
			case ABMaterial.POSITION_BOTTOMLEFT:
				position = "position: absolute; bottom: " + py + "px; left: " + px + "px";
				break;
			case ABMaterial.POSITION_BOTTOMRIGHT:
				position = "position: absolute; bottom: " + py + "px; right: " + px + "px";
				break;
			}
			if (Narrow) {
				position += "; margin-top: 0";
			}
		
			j.SetProp("style", position);
			if (!ToolTipText.equals("")) {
				ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-position", ToolTipPosition);
				ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-delay", "" + ToolTipDelay);
				ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-tooltip", ToolTipText);
			}
			 
			
		}
		j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		
		String extraArea="";
		if (IsTextArea) {
			extraArea = "materialize-textarea" + in.ThemeName.toLowerCase() + " materialize-textarea";
			if (IsDirty) {
				j.SetVal(sText);			
			}
		} else {
			if (IsDirty) {
				j.SetVal(sText);
			}
		}
		String toolTip2="";
		if (!ToolTipText.equals("")) {
			toolTip2 = " tooltipped ";
		}
		String input="";
		if (IsFileInput) {
			input = " file-path ";
		}
		String auto="";
		if (mAutoComplete.size()>0) {
			auto = " autocomplete inputFields ";
			if (AutoCompleteOpenOnFocus) {
				auto = auto + " autocompopen ";
			}
		}
		String RaiseChanged="";
		if (RaiseChangedEvent && IsFileInput) {
			RaiseChanged=" raisechangedcheck "; 
		} else {
			if (RaiseChangedEvent) {
				RaiseChanged=" raisechanged "; 
			}
		}
		String NotRaiseFocus="";
		if (!RaiseFocusEvent) {
			NotRaiseFocus=" notraisefocus ";			
		}
		String IEReset="";
		if (Theme.HasReset) {
			IEReset = " abmcloseinp ";
		}
		String cursor="";
		if (RaiseClickedEvent && mCursor.equals("")) {
			cursor = " cursor-pointing ";
		}
		if (!mCursor.equals("")) {
			cursor = " " + mCursor + " ";
		}
		String onClick="";
		if (RaiseClickedEvent) {
			onClick="labelclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "')";
		}
		if (mValidate) {
			String FIextra="";
			if (IsFileInput) {	
				FIextra = "abm";
			}
			j.SetProp("class", auto + extraArea + input + toolTip2 + RaiseChanged + NotRaiseFocus + IEReset + cursor + " validate" + FIextra + " " + ABMaterial.GetColorStr(in.InputColor, in.InputColorIntensity, "text") + " " + BuildClass());
		} else {
			j.SetProp("class", auto + extraArea + input + toolTip2 + RaiseChanged + NotRaiseFocus + IEReset + cursor + " " + IsValid + " " + ABMaterial.GetColorStr(in.InputColor, in.InputColorIntensity, "text") + " " + BuildClass());
		}
		if (IsFileInput) {	
			if (!Accepts.equals("")) {
				ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "accept",Accepts);
			} else {
				ABMaterial.RemoveProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "accept");
			}
		}
		if (DisableSpellcheck) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "spellcheck","false");
		} else {
			ABMaterial.RemoveProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "spellcheck");
		}
		if (!PlaceHolderText.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "placeholder",BuildBodyInfo(PlaceHolderText, true, false));
		} else {
			ABMaterial.RemoveProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "placeholder");
		}

		String ForceLeft="";
		if (!mInputMask.equals("") && (Align.equals(ABMaterial.INPUT_TEXTALIGN_LEFT) || Align.equals(ABMaterial.INPUT_TEXTALIGN_START))) {
			ForceLeft = ",'rightAlign': false";
		}
		if (!IsTextArea && !mInputMask.equals("")) {
			if (ABMaterial.InputMaskNewVersion==false) {
				ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-inputmask", mInputMask + ", 'showMaskOnHover':false" + ForceLeft); 
			}
		}
		String BottomLine="";
		if (RemoveBottomLine) {
			BottomLine=";border-bottom: unset;box-shadow: unset";
		}
		
		String ExtraStyle=BottomLine;
		if (in.Bold) {
			ExtraStyle = ";font-weight: bold";
		}
		if (in.Italic) {
			ExtraStyle += ";font-style: italic";
		}
		if (in.Underlined) {
			ExtraStyle+=";text-decoration: underline";
		}
		if (Narrow) {
			j.SetProp("style", "text-align:" + Align + ";margin-bottom: 0" + ExtraStyle);
		} else {
			j.SetProp("style", "text-align:" + Align + ExtraStyle);
		}
		
		if (CharacterCounter>0) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "length", "" + CharacterCounter);			
		}
		if (MaxCharacters>0) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "maxlength", "" + MaxCharacters);
		}
		
		if (mInputType==ABMaterial.INPUT_NUMBER) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "step", ""+NumberStep);			
		}
		
		if (Theme.HasReset) {
			j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-reset");
			if (sText.replace("{NBSP}", "").equals("") || !mEnabled) {
				ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-reset", "hide");
			} else {
				ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-reset", "hide");
			}
		}
		
		ABMaterial.RemoveProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "onclick");
		if (!onClick.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "onclick", onClick);
		}
		onClick="";
		
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		Params.Add(!mEnabled);
		Page.ws.RunFunction("SetDisabled", Params);
		
		if (!this.IsFileInput) {
			j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label");
			if (!sText.equals("") || !PlaceHolderText.equals("")) {
				ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label", "active");			
			} else {
				ABMaterial.RemoveClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label", "active");			
			}
			if (!cursor.equals("")) {
				ABMaterial.AddClass(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label", cursor);
			}
			j.SetHtml(BuildBodyInfo(Title, false, true));
		
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label", "data-error", ABMaterial.HTMLConv().htmlEscape(WrongMessage, Page.PageCharset));
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label", "data-success", ABMaterial.HTMLConv().htmlEscape(SuccessMessage, Page.PageCharset));
			 
		}
		
		if (IsTextArea) {
			anywheresoftware.b4a.objects.collections.List ParamsResize = new anywheresoftware.b4a.objects.collections.List();
			ParamsResize.Initialize();
			ParamsResize.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());							
			Page.ws.RunFunction("TextAreaResize", ParamsResize);
		}
		
		Page.ws.Eval(BuildJavaScript(), null);
		
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("Input Refresh: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		anywheresoftware.b4a.objects.collections.List Params2 = new anywheresoftware.b4a.objects.collections.List();
		Params2.Initialize();		
		Page.ws.RunFunction("reinitinputfields", Params2);
		Page.ws.Eval("Materialize.updateTextFields();", null);
		if (IsDirtyInputMask) {
			if (!mInputMask.equals("")) {
				if (ABMaterial.InputMaskNewVersion==false) {
					Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').inputmask();", null);
				} else {
					Page.ws.Eval("$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').inputmask('remove');$('#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "').inputmask(" + mInputMask + ");", null);
				}
			} 
		}
		if (ABMaterial.InputMaskNewVersion) {
			IsDirtyInputMask=false;
		} else {
			IsDirtyInputMask=true;
		}
		IsDirty=false;
	}
	
	@Override
	protected String Build() {	
		String B4JSData="";
		if (!mB4JSUniqueKey.equals("")) {
			if (IsTextArea) {
				B4JSData = " data-b4js=\"" + mB4JSUniqueKey + "\" data-b4jsextra=\"textarea\" ";
			} else {
				B4JSData = " data-b4js=\"" + mB4JSUniqueKey + "\" data-b4jsextra=\"\" ";
			}
		} else {
			if (IsTextArea) {
				B4JSData = " data-b4js=\"\" data-b4jsextra=\"textarea\" ";
			} else {
				B4JSData = " data-b4js=\"\" data-b4jsextra=\"\" ";
			}
		}
		
		if (Theme.ThemeName.equals("default")) {
			Theme.Colorize(Page.CompleteTheme.MainColor);
		}
		StringBuilder s = new StringBuilder();		
		String toolTip="";
		String toolTip2="";
		if (!ToolTipText.equals("")) {
			toolTip=" data-position=\"" + ToolTipPosition + "\" data-delay=\"" + ToolTipDelay + "\" data-tooltip=\"" + ToolTipText + "\" "; 
			toolTip2=" tooltipped ";
		}
		ThemeInput in = Theme;
		String position="";
		String NarrowStr="";
		if (Narrow) {
			NarrowStr = ";margin-top: 0";
		}
		String charCounter="";
		if (CharacterCounter>0) {
			charCounter = " length=" + CharacterCounter + " ";
		}
		if (MaxCharacters>0) {
			charCounter = charCounter + " maxlength=" + MaxCharacters + " ";
		}
		
		String cursor="";
		String cursorStyle="";
		if (RaiseClickedEvent && mCursor.equals("")) {
			cursor = " cursor-pointing ";
			cursorStyle="cursor: pointer;";
		}
		if (!mCursor.equals("")) {
			cursor = " " + mCursor + " ";
		}
		String onClick="";
		if (RaiseClickedEvent) {
			onClick=" onclick=\"labelclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "')\" ";
		}
		
		switch (PositionType) {
		case ABMaterial.POSITION_TOPLEFT:
			position = " style=\"position: absolute; top: " + py + "px; left: " + px + "px" + NarrowStr +"\" ";
			break;
		case ABMaterial.POSITION_TOPRIGHT:
			position = " style=\"position: absolute; top: " + py + "px; right: " + px + "px" + NarrowStr +"\" ";
			break;
		case ABMaterial.POSITION_BOTTOMLEFT:
			position = " style=\"position: absolute; bottom: " + py + "px; left: " + px + "px" + NarrowStr +"\" ";
			break;
		case ABMaterial.POSITION_BOTTOMRIGHT:
			position = " style=\"position: absolute; bottom: " + py + "px; right: " + px + "px" + NarrowStr +"\" ";
			break;
		default:
			if (Narrow || this.IsFileInput) {
				position = " style=\"margin-top: 0\"";
			}
		}
		if (!this.IsFileInput) {
			s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-parent" + "\" class=\"" + BuildClassParent(in) + "\" " + position+ ">\n");
		}
		if (!IconName.equals("")) {
			switch (IconName.substring(0, 3).toLowerCase()) {
			case "mdi":
				s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "icon\" icontype=\"mdi\" class=\"" + IconName + " prefix\" style=\"" + in.ExtraStyleIcon + "\"></i>\n");
				break;
			case "fa ":
			case "fa-":
				s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "icon\" icontype=\"fa\" class=\"" + IconAwesomeExtra + " " + IconName + " prefix\" style=\"" + in.ExtraStyleIcon + "\"></i>\n");
				break;
			default:
				s.append("<i id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "icon\"  icontype=\"mat\" class=\"material-icons prefix\" style=\"" + in.ExtraStyleIcon + "\">" + ABMaterial.HTMLConv().htmlEscape(IconName, Page.PageCharset) + "</i>\n");				
			}			
		}
		String BottomLine="";
		if (RemoveBottomLine) {
			BottomLine=";border-bottom: unset;box-shadow: unset";
		}
		String spellCheck="";
		if (DisableSpellcheck) {
			spellCheck=" spellcheck=\"false\" ";
		}
		String extraArea="";
		if (IsTextArea) {
			extraArea = "materialize-textarea" + in.ThemeName.toLowerCase() + " materialize-textarea";
			if (ArrayName.equals("")) {
				s.append("<textarea " + toolTip + charCounter + B4JSData + spellCheck + onClick +" id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" type=\"" + mInputType + "\" allowtab=\"" + this.AllowTabInTextArea + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" ");				
			} else {
				s.append("<textarea " + toolTip + charCounter + B4JSData + spellCheck + onClick +" id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" type=\"" + mInputType + "\" allowtab=\"" + this.AllowTabInTextArea + "\" evname=\"" + ArrayName.toLowerCase() + "\" ");
			}
		} else {
			if (IsFileInput) {
				String FIextraDir="";
				if (this.IsFileInputDirectory) {
					FIextraDir = " webkitdirectory mozdirectory odirectory msdirectory directory multiple ";
				}
				if (this.Accepts.equals("")) {
					if (ArrayName.equals("")) {
						s.append("<input " + toolTip + charCounter + B4JSData + spellCheck + onClick + " " + UseBrowserAutocomplete + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" type=\"" + mInputType + "\" evname=\"" + ArrayName.toLowerCase() + IDFileInput.toLowerCase() + "\"" + FIextraDir + " ");
					} else {
						s.append("<input " + toolTip + charCounter + B4JSData + spellCheck + onClick + " " + UseBrowserAutocomplete + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" type=\"" + mInputType + "\" evname=\"" + ArrayName.toLowerCase() + "\"" + FIextraDir + " ");
					}
				} else {
					if (ArrayName.equals("")) {
						s.append("<input " + toolTip + charCounter + B4JSData + spellCheck + onClick + " " + UseBrowserAutocomplete + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" type=\"" + mInputType + "\" evname=\"" + ArrayName.toLowerCase() + IDFileInput.toLowerCase() + "\"" + FIextraDir + " accept=\"" + Accepts + "\" ");
					} else {
						s.append("<input " + toolTip + charCounter + B4JSData + spellCheck + onClick + " " + UseBrowserAutocomplete + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" type=\"" + mInputType + "\" evname=\"" + ArrayName.toLowerCase() + "\"" + FIextraDir + " accept=\"" + Accepts + "\" ");
					}
				}
			} else {
				if (ArrayName.equals("")) {
					s.append("<input " + toolTip + charCounter + B4JSData + spellCheck + onClick + " " + UseBrowserAutocomplete + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" type=\"" + mInputType + "\" evname=\"" + ArrayName.toLowerCase() + ID.toLowerCase() + "\" ");
				} else {
					s.append("<input " + toolTip + charCounter + B4JSData + spellCheck + onClick + " " + UseBrowserAutocomplete + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" type=\"" + mInputType + "\" evname=\"" + ArrayName.toLowerCase() + "\" idname=\"" + ID.toLowerCase() + "\" ");
				}
			}
		}
		if (IsFileInput || ReadOnly) {
			s.append("readonly=\"true\" ");
		}
		if (mInputType==ABMaterial.INPUT_NUMBER) {
			s.append("step=\"" + NumberStep + "\" ");
		}
		if (!mEnabled) {
			s.append("disabled ");
		}
		if (!PlaceHolderText.equals("")){
			s.append("placeholder=\"" + BuildBodyInfo(PlaceHolderText, true, false) + "\" ");
		}
		String input="";
		if (IsFileInput) {
			input = " file-path ";
		}
		String auto="";
		if (mAutoComplete.size()>0) {
			auto = " autocomplete inputFields ";
			if (AutoCompleteOpenOnFocus) {
				auto = auto + " autocompopen ";
			}
		}
		
		String RaiseChanged="";
		if (RaiseChangedEvent && IsFileInput) {
			RaiseChanged=" raisechangedcheck "; 
		} else {
			if (RaiseChangedEvent) {
				RaiseChanged=" raisechanged "; 
			}
		}
		String NotRaiseFocus="";
		if (!RaiseFocusEvent) {
			NotRaiseFocus=" notraisefocus ";			
		}
		String IEReset="";
		if (Theme.HasReset) {
			IEReset = " abmcloseinp ";
		}
		if (mValidate) {
			String FIextra="";
			if (IsFileInput) {	
				FIextra = "abm";
			}
			s.append("class=\"" + auto + extraArea + input + toolTip2 + RaiseChanged + NotRaiseFocus + IEReset + cursor + " validate" + FIextra + " " + ABMaterial.GetColorStr(in.InputColor, in.InputColorIntensity, "text") + " " + BuildClass() + "\" ");
		} else {
			s.append("class=\"" + auto + extraArea + input + toolTip2 + RaiseChanged + NotRaiseFocus + IEReset + cursor + " " + IsValid + " " + ABMaterial.GetColorStr(in.InputColor, in.InputColorIntensity, "text") + " " + BuildClass() + "\" ");
		}
		if (!IsTextArea) {				
			s.append("value=\"" + ABMaterial.HTMLConv().htmlEscape(sText, Page.PageCharset) + "\" ");				
		}
		String ForceLeft="";
		if (!mInputMask.equals("") && (Align.equals(ABMaterial.INPUT_TEXTALIGN_LEFT) || Align.equals(ABMaterial.INPUT_TEXTALIGN_START))) {
			ForceLeft = ",'rightAlign': false";
		}
		if (!IsTextArea && !mInputMask.equals("")) {
			if (ABMaterial.InputMaskNewVersion==false) {
				s.append("data-inputmask=\"" + mInputMask + ", 'showMaskOnHover':false " + ForceLeft + "\"");
			}
		}
		String ExtraStyle=BottomLine;
		if (in.Bold) {
			ExtraStyle = ";font-weight: bold";
		}
		if (in.Italic) {
			ExtraStyle += ";font-style: italic";
		}
		if (in.Underlined) {
			ExtraStyle+=";text-decoration: underline";
		}
		if (Narrow) {
			s.append(" style=\"text-align:" + Align + ";margin-bottom: 0" + ExtraStyle + "\"");			
		} else {
			s.append(" style=\"text-align:" + Align + ExtraStyle + "\"");
		}
		s.append(">");
		
		if (Theme.HasReset) {
			if (sText.replace("{NBSP}", "").equals("") || !mEnabled) {
				s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-reset\" class=\"abmcloseinp-icon abmcloseinp-icon" + Theme.ThemeName.toLowerCase() + " hide\" onclick=\"inputresetclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "')\"></div>");
			} else {
				s.append("<div id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "-reset\" class=\"abmcloseinp-icon abmcloseinp-icon" + Theme.ThemeName.toLowerCase() + "\" onclick=\"inputresetclickarray(event, '" + ParentString + "','" + ArrayName.toLowerCase() + "','" + ArrayName.toLowerCase() + ID.toLowerCase() + "')\"></div>");
			}			
		}
		
		if (IsTextArea) {
			s.append(ABMaterial.HTMLConv().htmlEscape(sText, Page.PageCharset) + "</textarea>\n");
		}
		String showArrow="";
		if (AutoCompleteShowArrow) {
			showArrow = "<span class=\"caret\">&nbsp;&#9660;</span>";
		}
		
		if (!this.IsFileInput) {
			if (!sText.equals("") || !PlaceHolderText.equals("")) {
				s.append("<label id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label" + "\" for=\""+ ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\"" + BuildErrorMessages() + " class=\"active\" style=\"" + cursorStyle + "\">" + BuildBodyInfo(Title, false, true) + showArrow + "</label>\n");		
			} else {
				s.append("<label id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "label" + "\" for=\""+ ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\"" + BuildErrorMessages() + "  style=\"" + cursorStyle + "\">" + BuildBodyInfo(Title, false, true) + showArrow  + "</label>\n");
			}
			s.append("</div>\n");
		}
		
		IsBuild=true;
		IsDirty=false;		
		return s.toString();
	}
	
	@Hide
	protected String BuildClass() {			
		StringBuilder s = new StringBuilder();
		s.append(super.BuildExtraClasses());
		ThemeInput l=Theme;
		
		s.append(ABMaterial.GetColorStr(l.BackColor, l.BackColorIntensity, "") + " " + ABMaterial.GetColorStr(l.ForeColor, l.ForeColorIntensity, "text"));
		return s.toString(); 
	}	
	
	@Hide	
	protected String BuildClassParent(ThemeInput in) {			
		StringBuilder s = new StringBuilder();
		String rtl="";
		if (getRightToLeft()) {
			rtl = " abmrtl ";
		}
		s.append("input-field input-field" + in.ThemeName.toLowerCase() + " " + "col s" + SizeSmall + " m" + SizeMedium + " l" + SizeLarge + " offset-s" + OffsetSmall + " offset-m" + OffsetMedium + " offset-l" + OffsetLarge + " " + mVisibility + rtl + " " + in.ZDepth + " " + ABMaterial.GetColorStr(in.BackColor, in.BackColorIntensity, "") + " " + ABMaterial.GetColorStr(in.ForeColor, in.ForeColorIntensity, "text"));
		
		s.append(mIsPrintableClass);
		s.append(mIsOnlyForPrintClass);
		return s.toString(); 
	}
	
	@Hide	
	protected String BuildErrorMessages() {			
		StringBuilder s = new StringBuilder();
		if (!WrongMessage.equals("")) {
			s.append(" data-error=\"" + ABMaterial.HTMLConv().htmlEscape(WrongMessage, Page.PageCharset) + "\"");
		}
		if (!SuccessMessage.equals("")) {
			s.append(" data-success=\"" + ABMaterial.HTMLConv().htmlEscape(SuccessMessage, Page.PageCharset) + "\"");
		}
					
		return s.toString(); 
	}
	
	protected String BuildBodyInfo(String text, boolean OnlyNBSP, boolean DoRequiredMark) {
		StringBuilder s = new StringBuilder();	
		
		String v = ABMaterial.HTMLConv().htmlEscape(text, Page.PageCharset);
		if (OnlyNBSP) {
			v=v.replace("{NBSP}", " ");
		} else {
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
				default:
					repl = "<i style=\"color: " + IconColor + "\" class=\"material-icons\">" + IconName + "</i>";
				}
				v=v.replace(vv,repl );
				start = v.indexOf("{IC:");
			}
		}
		s.append(v);
		
		if (DoRequiredMark) {
			if (this.ShowRequiredMark) {
				if (sText.replace("{NBSP}", "").equals("")) {
					s.append("<span id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "req" + "\" class=\"required\">&nbsp;*</span>");
				} else {
					s.append("<span id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "req" + "\" class=\"required hide\">&nbsp;*</span>");
				}
			}			
		}
		
		if (this.ShowDropdownIcon) {
			s.append("<span class=\"caret\" style=\"float: unset\">&nbsp;&#9660;</span>");			
		}
		if (this.ShowSidebarIcon) {
			s.append("<span class=\"caret\" style=\"float: unset\">&nbsp;<i style=\"color: " + ABMaterial.GetColorStrMap(Theme.ForeColor, Theme.ForeColorIntensity) + "\" class=\"fa fa-bars\"></i></span>");			
		}
		
		return s.toString();
	}
	
	public String getInputType() {
		return mInputType;
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMInput c = new ABMInput();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;
		c.Theme = Theme.Clone();
		c.Type = Type;
		c.mVisibility = mVisibility;	
		c.ToolTipDelay = ToolTipDelay;
		c.ToolTipPosition = ToolTipPosition;
		c.ToolTipText = ToolTipText;
		c.mEnabled= mEnabled;
		c.FutureText = FutureText;		
		c.IconName = IconName;
		c.IconAwesomeExtra = IconAwesomeExtra;
		c.mInputType = mInputType;
		c.IsDirty = false;
		c.IsTextArea = IsTextArea;
		c.OffsetLarge = OffsetLarge;
		c.OffsetMedium = OffsetMedium;
		c.OffsetSmall = OffsetSmall;
		c.PlaceHolderText = PlaceHolderText;
		c.SizeLarge = SizeLarge;
		c.SizeMedium = SizeMedium;
		c.SizeSmall = SizeSmall;
		c.sText = sText;
		c.SuccessMessage = SuccessMessage;
		c.Title = Title;
		c.mValidate = mValidate;
		c.IsValid=IsValid;
		c.PositionType = PositionType;
		c.px = px;
		c.py = py;
		c.WrongMessage = WrongMessage;
		return c;
	}
	
	public class DateOptions implements java.io.Serializable {
		
		private static final long serialVersionUID = -8654018399908327185L;
		public int FirstDay=0;
		public String Months = "'January','February','March','April','May','June','July','August','September','October','November','December'";
		public String WeekDays = "'Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'";
		public String WeekDaysShort = "'Sun','Mon','Tue','Wed','Thu','Fri','Sat'";
		public String Format = "MM-DD-YYYY";		
	}
}
