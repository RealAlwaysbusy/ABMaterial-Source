package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Hide;

@Hide
public class NiceScrollBar {
	public boolean EnableCustom=false;
	public String CursorColor = ABMaterial.COLOR_GREY; //: "#424242", // change cursor color in hex
	public String CursorColorIntensity = ABMaterial.INTENSITY_NORMAL;
	public double CursorOpacityMin = 0; // change opacity when cursor is inactive (scrollabar "hidden" state), range from 1 to 0
    public double CursorOpacityMax = 1; // change opacity when cursor is active (scrollabar "visible" state), range from 1 to 0
    public int CursorWidthPx = 8; // cursor width in pixel (you can also write "5px")
    public int CursorBorderWidthPx = 1; //px solid #fff", // css definition for cursor border
    public String CursorBorderColor = ABMaterial.COLOR_WHITE;
    public String CursorBorderColorIntensity = ABMaterial.INTENSITY_NORMAL;
    public int CursorBorderRadiusPx = 8; // border radius in pixel for cursor
    
    public int ScrollSpeed = 60;  // scrolling speed
    public int MouseScrollStep = 40; // scrolling speed with mouse wheel (pixel)
    public boolean TouchBehavior = false; // enable cursor-drag scrolling like touch devices in desktop computer
    public boolean HwAcceleration = true; // use hardware accelerated scroll when supported
    
    public String AutoHideMode = "true"; // how hide the scrollbar works, possible values: 
      //true | // hide when no scrolling
      //"cursor" | // only cursor hidden
      //false | // do not hide,
      //"leave" | // hide only if pointer leaves content
      //"hidden" | // hide always
      //"scroll", // show only on scroll          
    public String BackgroundCSS = ""; // change css for rail background
    public boolean IFrameAutoResize = true; // autoresize iframe on load event
    public int CursorMinHeightPx = 32; // set the minimum cursor height (pixel)
    public boolean PreservenativesSrolling = true; // you can scroll native scrollable areas with mouse, bubbling mouse wheel event
    public boolean RailOffset = false; // you can add offset top/left for rail position
    public boolean BounceScroll = false; // (only hw accell) enable scroll bouncing at the end of content as mobile-like 
    public boolean SpacebarEnabled = true; // enable page down scrolling when space bar has pressed
    public int RailPaddingTopPx = 0; //: { top: 0, right: 0, left: 0, bottom: 0 }, // set padding for rail bar
    public int RailPaddingRightPx = 0;
    public int RailPaddingLeftPx = 0;
    public int RailPaddingBottomPx = 0;
    public boolean DisableOutline = true; // for chrome browser, disable outline (orange highlight) when selecting a div with nicescroll
    public boolean HorizRailEnabled = true; // nicescroll can manage horizontal scroll
    public String RailAlign = "right"; // alignment of vertical rail
    public String RailVerticalAlign = "bottom"; // alignment of horizontal rail
    public boolean EnableTranslate3d = true; // nicescroll can use css translate to scroll content
    public boolean EnableMousewheel = true; // nicescroll can manage mouse wheel events
    public boolean EnableKeyboard = true; // nicescroll can manage keyboard events
    public boolean SmoothScroll = true; // scroll with ease movement
    public boolean SensitiveRail = true; // click on rail make a scroll
    public boolean EnableMouselockApi = true; // can use mouse caption lock API (same issue on object dragging)
    public boolean CursorFixedHeight = false; // set fixed height for cursor in pixel
    public int HideCursorDelay = 400; // set the delay in microseconds to fading out scrollbars
    public int DirectionLockDeadzone = 6; // dead zone in pixels for direction lock activation
    public boolean NativeParentScrolling = true; // detect bottom of content and let parent to scroll, as native scroll does
    public boolean EnableScrollOnSelection = true; // enable auto-scrolling of content when selection text
    public double CursorDragspeed = 0.3; // speed of selection when dragged with cursor
    public String RtlMode = "auto"; // horizontal div scrolling starts at left side
    public boolean CursorDragOnTouch = false; // drag cursor in touch / touchbehavior mode also
    public String OneAxisMousemode = "auto"; // it permits horizontal scrolling with mousewheel on horizontal only content, if false (vertical-only) mousewheel don't scroll horizontally, if value is auto detects two-axis mouse
    
    public boolean PreventMultiTouchScrolling = true; // prevent scrolling on multitouch events
    public boolean DisableMutationObserver = false; // force MutationObserver disabled
    
    protected String GetInitString() {
    	if (!EnableCustom) {
    		return "";
    	}
    	StringBuilder s = new StringBuilder();
    	s.append("$(\"html\").niceScroll({");
    	s.append("cursorcolor: \"" + ABMaterial.GetColorStrMap(CursorColor, CursorColorIntensity) + "\","); // change cursor color in hex
    	s.append("cursoropacitymin: " + this.CursorOpacityMin + ","); // change opacity when cursor is inactive (scrollabar "hidden" state), range from 1 to 0
    	s.append("cursoropacitymax: " + this.CursorOpacityMax + ","); // change opacity when cursor is active (scrollabar "visible" state), range from 1 to 0
    	s.append("cursorwidth: \"" + this.CursorWidthPx + "px\","); // cursor width in pixel (you can also write "5px")
    	s.append("cursorborder: \"" + this.CursorBorderWidthPx + "px solid " + ABMaterial.GetColorStrMap(CursorBorderColor, CursorBorderColorIntensity) + "\","); // css definition for cursor border
    	s.append("cursorborderradius: \"" + this.CursorBorderRadiusPx + "px\","); // border radius in pixel for cursor
        
    	s.append("scrollspeed: " + this.ScrollSpeed + ","); // scrolling speed
    	s.append("mousescrollstep: " + this.MouseScrollStep + ","); // scrolling speed with mouse wheel (pixel)
    	s.append("touchbehavior: " + this.TouchBehavior + ","); // enable cursor-drag scrolling like touch devices in desktop computer
    	s.append("hwacceleration: " + this.HwAcceleration + ","); // use hardware accelerated scroll when supported
        
    	s.append("grabcursorenabled: false,"); // (only when touchbehavior=true) display "grab" icon
    	if (this.AutoHideMode.equalsIgnoreCase("true") || this.AutoHideMode.equalsIgnoreCase("false")) {
    		s.append("autohidemode: " + this.AutoHideMode + ","); // how hide the scrollbar works, possible values:
    	} else {
    		s.append("autohidemode: \"" + this.AutoHideMode + "\","); // how hide the scrollbar works, possible values:
    	}
    	s.append("background: \"" + this.BackgroundCSS + "\","); // change css for rail background
    	s.append("iframeautoresize: " + this.IFrameAutoResize + ","); // autoresize iframe on load event
    	s.append("cursorminheight: " + this.CursorMinHeightPx + ","); // set the minimum cursor height (pixel)
    	s.append("preservenativescrolling: " + this.PreservenativesSrolling + ","); // you can scroll native scrollable areas with mouse, bubbling mouse wheel event
    	s.append("railoffset: " + this.RailOffset + ","); // you can add offset top/left for rail position
    	s.append("bouncescroll: " + this.BounceScroll + ","); // (only hw accell) enable scroll bouncing at the end of content as mobile-like 
    	s.append("spacebarenabled: " + this.SpacebarEnabled + ","); // enable page down scrolling when space bar has pressed
    	s.append("railpadding: { top: " + this.RailPaddingTopPx + ", right: " + this.RailPaddingRightPx + ", left: " + this.RailPaddingLeftPx + ", bottom: " + this.RailPaddingBottomPx + " },"); // set padding for rail bar
    	s.append("disableoutline: " + this.DisableOutline + ","); // for chrome browser, disable outline (orange highlight) when selecting a div with nicescroll
    	s.append("horizrailenabled: " + this.HorizRailEnabled + ","); // nicescroll can manage horizontal scroll
    	s.append("railalign: \"" + this.RailAlign + "\","); // alignment of vertical rail
    	s.append("railvalign: \"" + this.RailVerticalAlign + "\","); // alignment of horizontal rail
    	s.append("enabletranslate3d: " + this.EnableTranslate3d + ","); // nicescroll can use css translate to scroll content
    	s.append("enablemousewheel: " + this.EnableMousewheel + ","); // nicescroll can manage mouse wheel events
    	s.append("enablekeyboard: " + this.EnableKeyboard + ","); // nicescroll can manage keyboard events
    	s.append("smoothscroll: " + this.SmoothScroll + ","); // scroll with ease movement
    	s.append("sensitiverail: " + this.SensitiveRail + ","); // click on rail make a scroll
    	s.append("enablemouselockapi: " + this.EnableMouselockApi + ","); // can use mouse caption lock API (same issue on object dragging)
    	s.append("cursorfixedheight: " + this.CursorFixedHeight + ","); // set fixed height for cursor in pixel
    	s.append("hidecursordelay: " + this.HideCursorDelay + ","); // set the delay in microseconds to fading out scrollbars
    	s.append("directionlockdeadzone: " + this.DirectionLockDeadzone + ","); // dead zone in pixels for direction lock activation
    	s.append("nativeparentscrolling: " + this.NativeParentScrolling + ","); // detect bottom of content and let parent to scroll, as native scroll does
    	s.append("enablescrollonselection: " + this.EnableScrollOnSelection + ","); // enable auto-scrolling of content when selection text
    	s.append("cursordragspeed: " + this.CursorDragspeed + ","); // speed of selection when dragged with cursor
    	s.append("rtlmode: \"auto\","); // horizontal div scrolling starts at left side
    	s.append("cursordragontouch: false,"); // drag cursor in touch / touchbehavior mode also
    	if (this.OneAxisMousemode.equalsIgnoreCase("true") || this.OneAxisMousemode.equalsIgnoreCase("false")) {
    		s.append("oneaxismousemode: " + this.OneAxisMousemode + ","); // it permits horizontal scrolling with mousewheel on horizontal only content, if false (vertical-only) mousewheel don't scroll horizontally, if value is auto detects two-axis mouse
    	} else {
    		s.append("oneaxismousemode: \"" + this.OneAxisMousemode + "\","); // it permits horizontal scrolling with mousewheel on horizontal only content, if false (vertical-only) mousewheel don't scroll horizontally, if value is auto detects two-axis mouse
    	}
        
    	s.append("preventmultitouchscrolling: " + this.PreventMultiTouchScrolling + ","); // prevent scrolling on multitouch events
    	s.append("disablemutationobserver: " + this.DisableMutationObserver + ""); // force MutationObserver disabled
    	s.append("});");
    	return s.toString();
    }
}
