package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Hide;

@Author("Alain Bailleul")  
@Hide
public class ABMSVGAnimation implements java.io.Serializable {
	
	private static final long serialVersionUID = -262105061271709093L;
	protected String INAnim="";
	protected int INDuration=0;
	protected String INEasing="";
	protected String OUTAnim="";
	protected int OUTDuration=0;
	protected String OUTEasing="";
	protected int AnimType=0;	
	public boolean IsInitialized=false;
	
	/**
	 * easing: "mina.linear","mina.easeout","mina.easein","mina.easeinout","mina.backin","mina.backout","mina.elastic","mina.bounce" 
	 */
	protected void InitializeSingleRun(String anim, int durationMs, String easing) {
		AnimType=1;
		INAnim = anim;
		INDuration=durationMs;
		INEasing=easing;
		IsInitialized=true;
	}
	
	/**
	 * easing: "mina.linear","mina.easeout","mina.easein","mina.easeinout","mina.backin","mina.backout","mina.elastic","mina.bounce"
	 */
	protected void InitializeContinuousRun(String anim, int durationMs, String easing, String resetAnim, int resetDurationMs, String resetEasing) {
		AnimType=2;
		INAnim = anim;
		INDuration=durationMs;
		INEasing=easing;
		OUTAnim = resetAnim;
		OUTDuration=resetDurationMs;
		OUTEasing=resetEasing;
		IsInitialized=true;
	}
	
	protected String GetINAsString() {
		if (INAnim.equals("")) return "";
		if (!INEasing.equals("")) {
			return INAnim + "," + INDuration + "," + INEasing;
		} else {
			return INAnim + "," + INDuration + ",mina.linear"; 
		}
	}
	
	protected String GetOUTAsString() {
		if (OUTAnim.equals("")) return "";
		if (!OUTEasing.equals("")) {
			return OUTAnim + "," + OUTDuration + "," + OUTEasing;
		} else {
			return OUTAnim + "," + OUTDuration + ",mina.linear"; 
		}
	}
	
	protected ABMSVGAnimation Clone() {
		ABMSVGAnimation c = new ABMSVGAnimation();
		c.INAnim = INAnim;
		c.INDuration = INDuration;
		c.INEasing = INEasing;
		c.OUTAnim = OUTAnim;
		c.OUTDuration = OUTDuration;
		c.OUTEasing = OUTEasing;
		c.AnimType = AnimType;
		c.IsInitialized = true;
		return c;
	}
}
