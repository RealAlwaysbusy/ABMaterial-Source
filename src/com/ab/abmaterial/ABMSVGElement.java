package com.ab.abmaterial;

//import anywheresoftware.b4a.BA;
import java.util.ArrayList;
import java.util.List;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")  
@ShortName("ABMSVGElement")
public class ABMSVGElement implements java.io.Serializable {
	
	private static final long serialVersionUID = -3316594002119153866L;
	protected int mSVGElementType=ABMaterial.SVGELEMENT_UNDEFINED;
	protected String mElementID="";
	protected boolean IsDirty=true;
	protected String initString="";
	protected String mAttributes="";
	protected boolean IsNew=true;
	protected String mMask="";
	
	public boolean HasEvents=false;
	protected boolean HasHover=false;
	protected boolean HasAnim=false;
	protected String mDirection="";
	protected String mVis="";
	public transient Object Tag=null;
	public boolean IsInitialized=false;
	
	protected List<ABMSVGAnimation> anims = new ArrayList<ABMSVGAnimation>();
	protected List<ABMSVGAnimation> hoverins = new ArrayList<ABMSVGAnimation>();
	protected List<ABMSVGAnimation> hoverouts = new ArrayList<ABMSVGAnimation>();
	
	protected ABMSVGElement myClone=null;
			
	public ABMSVGElement(boolean withClone) {
		if (withClone) {
			myClone = new ABMSVGElement(false);
		}
	}
	
	protected void BackToClone() {
		if (myClone!=null) {
			IsDirty = true;
			mAttributes = myClone.mAttributes;
			IsNew = true;
			mMask = myClone.mMask;
			HasEvents = myClone.HasEvents;
			HasHover = myClone.HasHover;
			HasAnim = myClone.HasAnim;
			mDirection = myClone.mDirection;
			mVis = myClone.mVis;
			Tag = myClone.Tag;
			anims = new ArrayList<ABMSVGAnimation>();
			hoverins = new ArrayList<ABMSVGAnimation>();
			hoverouts = new ArrayList<ABMSVGAnimation>();
			for (ABMSVGAnimation anim: myClone.anims) {
				anims.add(anim.Clone());
			}
			for (ABMSVGAnimation hover: myClone.hoverins) {
				hoverins.add(hover.Clone());
			}
			for (ABMSVGAnimation hover: myClone.hoverouts) {
				hoverouts.add(hover.Clone());
			}	
		}
	}
		
	public void Initialize(String elementID, int SVGElementType) {
		this.mElementID=elementID;
		this.mSVGElementType = SVGElementType;
		IsInitialized=true;
		if (myClone!=null) {
			myClone.Initialize(elementID, SVGElementType);
		}
	}
	
	public int getSVGElementType() {
		return this.mSVGElementType;
	}
	
	public String getElementID() {
		return this.mElementID;
	}
	
	public void SetAttributes(String attr) {
		mAttributes = attr;
		IsDirty=true;
		if (myClone!=null) {
			myClone.SetAttributes(attr);
		}
	}
	
	/**
	 * easing: "mina.linear","mina.easeout","mina.easein","mina.easeinout","mina.backin","mina.backout","mina.elastic","mina.bounce"
	 */
	public void AddAnimationSingleRun(String anim, int durationMs, String easing) {
		ABMSVGAnimation an = new ABMSVGAnimation();
		an.InitializeSingleRun(anim, durationMs, easing);
		anims.add(an);
		IsDirty=true;
		HasAnim=true;
		if (myClone!=null) {
			myClone.AddAnimationSingleRun(anim, durationMs, easing);
		}
	}
	
	/**
	 * easing: "mina.linear","mina.easeout","mina.easein","mina.easeinout","mina.backin","mina.backout","mina.elastic","mina.bounce"
	 */
	public void AddAnimationContinuousRun(String anim, int durationMs, String easing, String resetAnim, int resetDurationMs, String resetEasing) {
		ABMSVGAnimation an = new ABMSVGAnimation();
		an.InitializeContinuousRun(anim, durationMs, easing, resetAnim, resetDurationMs, resetEasing);
		anims.add(an);
		IsDirty=true;
		HasAnim=true;
		if (myClone!=null) {
			myClone.AddAnimationContinuousRun(anim, durationMs, easing, resetAnim, resetDurationMs, resetEasing);
		}
	}
	
	protected String GetAnimations() {
		StringBuilder s = new StringBuilder();
		int counter=0;
		for (ABMSVGAnimation an: anims) {
			switch (an.AnimType) {
			case 1:
				s.append("$EL$.stop(true,true).animate(" + an.GetINAsString() + ");");
				break;
			case 2:
				counter++;
				s.append(AnimateLoopAnimateReset(an.INAnim, an.INDuration, an.INEasing, an.OUTAnim, an.OUTDuration, an.OUTEasing, counter));
				break;
			}
		}
		return s.toString();
	}
	
	protected String GetHovers() {
		StringBuilder s = new StringBuilder();
		
		s.append("$EL$.hover( function(e){");
		for (ABMSVGAnimation an: hoverins) {
			if (!an.GetINAsString().equals("")) {
				s.append("$EL$.stop(true,true).animate(" + an.GetINAsString() + ");");
			}
		}	
		s.append("}, function(e) {");
		for (ABMSVGAnimation an: hoverouts) {
			if (!an.GetINAsString().equals("")) { //must be IN also as they are SingleRuns
				s.append("$EL$.stop(true,true).animate(" + an.GetINAsString() + ");");
			}			
		}
		s.append("});");
		return s.toString();
	}
	
	public void AddHoverINAnimation(String anim, int durationMs, String easing) {
		ABMSVGAnimation an = new ABMSVGAnimation();
		an.InitializeSingleRun(anim, durationMs, easing);
		hoverins.add(an);
		IsDirty=true;
		HasHover=true;
		if (myClone!=null) {
			myClone.AddHoverINAnimation(anim, durationMs, easing);
		}
	}
	
	public void AddHoverOUTAnimation(String anim, int durationMs, String easing) {
		ABMSVGAnimation an = new ABMSVGAnimation();
		an.InitializeSingleRun(anim, durationMs, easing);
		hoverouts.add(an);
		IsDirty=true;
		HasHover=true;
		if (myClone!=null) {
			myClone.AddHoverOUTAnimation(anim, durationMs, easing);
		}
	}
	
	protected String AnimateLoopAnimateReset(String anim, int durationMs, String easing, String resetAnim, int resetDurationMs, String resetEasing, int counter) {
		StringBuilder s = new StringBuilder();
		s.append("function " + mElementID.replace("-",  "_") + "loop_" + counter + "(el) {");
		
		s.append("if (svgstop[$EL2$]==='1') {svgstop[$EL2$]='0';return};");
		if (!easing.equals("")) {
			s.append("$EL$.stop(true,true).animate(" + anim + "," + durationMs + "," + easing + ",");
		} else {
			s.append("$EL$.stop(true,true).animate(" + anim + "," + durationMs + ",mina.linear,");
		}
		s.append("function() {");		
		s.append(mElementID.replace("-",  "_") + "loopback_" + counter + "($EL$);");
		s.append("});");
		s.append("}\n");
		s.append("function " + mElementID.replace("-",  "_") + "loopback_" + counter + "(el) {");
		
		s.append("if (svgstop[$EL2$]==='1') {svgstop[$EL2$]='0';return};");
		if (!resetEasing.equals("")) {
			s.append("$EL$.stop(true,true).animate(" + resetAnim + "," + resetDurationMs + "," + resetEasing + ",");
		} else {
			s.append("$EL$.stop(true,true).animate(" + resetAnim + "," + resetDurationMs + ",mina.linear,");
		}
		s.append("function() {");		
		s.append(mElementID.replace("-",  "_") + "loop_" + counter + "($EL$);");
		s.append("});");
		
		s.append("}\n");
		s.append(mElementID.replace("-",  "_") + "loop_" + counter + "($EL$);");
		return s.toString();
	}
	
	public void StopAnimation() {
		IsDirty=true;
	}
	
	@Hide
	protected void SetMask(String mask) {
		mMask+=mask;
		IsDirty=true;
		if (myClone!=null) {
			myClone.SetMask(mask);
		}
	}
	
	public void ToFront() {
		mDirection=".toFront();";
		IsDirty=true;
		if (myClone!=null) {
			myClone.ToFront();
		}
	}
	
	public void ToBack() {
		mDirection=".toBack();";
		IsDirty=true;
		if (myClone!=null) {
			myClone.ToBack();
		}
	}
	
	public void Backward() {
		mDirection=".backward();";
		IsDirty=true;
		if (myClone!=null) {
			myClone.Backward();
		}
	}
	
	public void Forward() {
		mDirection=".forward();";
		IsDirty=true;
		if (myClone!=null) {
			myClone.Forward();
		}
	}
	
	public void Hide() {
		mVis=".hide();";
		IsDirty=true;
		if (myClone!=null) {
			myClone.Hide();
		}
	}
	
	public void Show() {
		mVis=".show();";
		IsDirty=true;
		if (myClone!=null) {
			myClone.Show();
		}
	}
	
	public String ToString() {
		StringBuilder s = new StringBuilder();
		s.append("IsDirty=" + IsDirty + "|");
		s.append("initString=" + initString + "|");
		s.append("mAttributes=" + mAttributes + "|");
		s.append("IsNew=" + IsNew + "|");
		s.append("mMask=" + mMask + "|");
		s.append("HasEvents=" + HasEvents + "|");
		s.append("HasHover=" + HasHover + "|");
		s.append("HasAnim=" + HasAnim + "|");
		s.append("mDirection=" + mDirection + "|");
		s.append("mVis=" + mVis + "|");
		return s.toString();
	}
	
	
}
