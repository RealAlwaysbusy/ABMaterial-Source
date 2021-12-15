package com.ab.abmaterial;

import java.util.ArrayList;
import java.util.List;

import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul")  
@ShortName("ABMAnimation")
public class ABMAnimation implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7314083531192353767L;
	protected String Name="";
	protected int DurationMs=0;
	protected String Tween=ABMaterial.TWEEN_LINEAR;
	protected List<String> builds = new ArrayList<String>();
	protected List<String> initBuildsAfterCreation = new ArrayList<String>();
	public boolean IsInitialized=false;
	
	public void Initialize(String name, int durationMs, String tween) {
		builds.add("perspective: '600px'");
		initBuildsAfterCreation.add("perspective: '600px'");
		this.Name = name;
		this.DurationMs=durationMs;
		this.Tween = tween;		
		IsInitialized=true;
	}
	
	/**
	 * Applies a blur effect to the animation. A larger value will create more blur.  
	 */
	public void Blur(int value) {
		builds.add("blur: " + value);
		initBuildsAfterCreation.add("blur: " + (value));
	}
	
	/**
	 * Sets the opacity level for the animation. The opacity-level describes the transparency-level, where:
	 * 0 is completely transparent.
	 * 1 is default and represents the original animation (no transparency).
	 * Note: Negative values are not allowed.
	 */	
	public void Opacity(double opacity) {
		builds.add("opacity: " + opacity);
		initBuildsAfterCreation.add("opacity: " + opacity);
	}
	
	public void SlideLeft() {
		builds.add("translateX: '-100vw'");
		initBuildsAfterCreation.add("translateX: '-100vw'");
	}
	
	public void SlideRight() {
		builds.add("translateX: '100vw'");
		initBuildsAfterCreation.add("translateX: '100vw'");
	}
	
	public void SlideTop() {
		builds.add("translateY: '-100vh'");
		initBuildsAfterCreation.add("translateY: '-100vh'");
	}
	
	public void SlideBottom() {
		builds.add("translateY: '100vh'");
		initBuildsAfterCreation.add("translateY: '100vh'");
	}
	
	public void SlideInPositionX() {
		builds.add("translateX: 0");
		initBuildsAfterCreation.add("translateX: 0");
	}
	
	public void SlideInPositionY() {
		builds.add("translateY: 0");
		initBuildsAfterCreation.add("translateY: 0");
	}
	
	public void RotateX(int degrees) {
		builds.add("rotateX: '" + degrees + "deg'");
		initBuildsAfterCreation.add("rotateX : '" + degrees + "deg'");
	}
	
	public void RotateY(int degrees) {
		builds.add("rotateY: '" + degrees + "deg'");
		initBuildsAfterCreation.add("rotateY : '" + degrees + "deg'");
	}
	
	public void RotateZ(int degrees) {
		builds.add("rotateYZ: '" + degrees + "deg'");
		initBuildsAfterCreation.add("rotateZ : '" + degrees + "deg'");
	}
	
	public void ScaleX(double scale) {		
		builds.add("scaleX: " + scale);		
		initBuildsAfterCreation.add("scaleX: " + scale);
	}
	
	public void ScaleY(double scale) {		
		builds.add("scaleY: " + scale);		
		initBuildsAfterCreation.add("scaleY: " + scale);
	}
	
	public void ScaleZ(double scale) {		
		builds.add("scaleZ: " + scale);		
		initBuildsAfterCreation.add("scaleZ: " + scale);
	}
	
	protected String Build(String complete) {
		StringBuilder s = new StringBuilder();
		s.append("{");		
		s.append("p: {");
		for (int i=0;i<builds.size();i++) {
			if (i>0) {
				s.append(",");
			}
			s.append(builds.get(i));
		}	    		  
		s.append("},");
		s.append("o: {");
		s.append("duration: " + DurationMs + ",");
		s.append("complete: function() {" + complete + "},");		
		s.append("queue: false,");
		s.append("easing: '" + Tween + "'");
		s.append("}");
		s.append("}");
		return s.toString();
	}
	
	protected String BuildInitialAfterCreation() {
		if (initBuildsAfterCreation.size()==0) {
			return "";
		}
		StringBuilder s = new StringBuilder();
		s.append("{");		
		s.append("p: {");
		for (int i=0;i<initBuildsAfterCreation.size();i++) {
			if (i>0) {
				s.append(",");
			}
			s.append(initBuildsAfterCreation.get(i));
		}	    		  
		s.append("},");
		s.append("o: {");
		s.append("duration: 0,");			
		s.append("queue: false,");
		s.append("easing: '" + Tween + "'");
		s.append("}");
		s.append("}");
		return s.toString();		
	}
	
	
	
}
