package com.ab.abmaterial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4j.object.WebSocket.JQueryElement;
import anywheresoftware.b4j.object.WebSocket.SimpleFuture;

@Author("Alain Bailleul")  
@ShortName("ABMGoogleMap")
@Events(values={"MarkerClicked(MarkerId as String)","CurrentLocation(Latitude as Double, Longitude as Double)","Error(ErrorMessage as String)", "OutsideGeofence(MarkerId as String, polygonId as String)", "InsideGeofence(MarkerId as String, polygonId as String)","GeoCodeResult(Latitude as Double, Longitude as Double)","ReverseGeoCodeResult(Address as String)", "Clicked(Latitude as Double, Longitude as Double)", "Ready()", "MarkerDragStart(MarkerId as String)", "MarkerDragEnd(Latitude as Double, Longitude as Double)","MarkerRemoved(markerId as String)"})
public class ABMGoogleMap extends ABMComponent {
	private static final long serialVersionUID = -2194129929710777424L;
	protected String ToolTipPosition=ABMaterial.TOOLTIP_TOP;
	protected String ToolTipText="";
	protected int ToolTipDelay=50;
	
	protected String MapType="roadmap";
	
	protected int Zoom=15;
	public boolean HasZoomControl=true;
	public boolean HasStreetViewControl=false;
	public boolean HasMapTypeControl=false;
	public transient List<String> mapTypes=new ArrayList<String>();
	protected int Width=0;
	protected int Height=400;
	
	public String ZDepth="z-depth-1";
	protected double Latitude =0.0;
	protected double Longitude =0.0;
	public boolean Draggable=true;
	protected int Heading=0;
	protected int Pitch=0;
	protected int ClusterSize=0;
	
	public boolean ShowDeleteInInfoWindow=false;
		
	/**
	 * 
	 * clusterSize = 0 is do not use clusters
	 */
	public void Initialize(ABMPage page, String id, double Latitude, double Longitude, int zoom, int heightPx, String mapType, int clusterSize) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_GOOGLEMAP;			
		this.MapType = mapType;
		this.Latitude = Latitude;
		this.Longitude = Longitude;
		this.Height = heightPx;
		this.Zoom=zoom;
		this.ClusterSize=clusterSize;
		IsInitialized=true;
	}	
	
	public void InitializeAsPanorama(ABMPage page, String id, double Latitude, double Longitude, int zoom, int heightPx, int headingDegrees, int pitchDegrees) {
		this.ID = id;			
		this.Page = page;
		this.Type = ABMaterial.UITYPE_GOOGLEMAP;			
		this.MapType = "panorama";
		this.Latitude = Latitude;
		this.Longitude = Longitude;	
		this.Heading = headingDegrees;
		this.Pitch = pitchDegrees;
		this.Zoom=zoom;
		this.Height = heightPx;
		IsInitialized=true;
	}	
	
	@Override
	protected void ResetTheme() {
		// Dummy
	}
	
	@Override
	protected String RootID() {
		return ArrayName.toLowerCase() + ID.toLowerCase();
	}
	
	public void SetFixedSize(int widthPx, int heightPx) {
		this.Width = widthPx;
		this.Height = heightPx;
	}
	
	@Override
	protected void AddArrayName(String arrayName) {	
		this.ArrayName += arrayName;
	}	
	
	public void SetTooltip(String text, String position, int delay) {
		this.ToolTipText = text;
		this.ToolTipPosition = position;
		this.ToolTipDelay = delay;			
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
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());		
		Page.ws.RunFunction("inittooltipped", Params);
		
		StringBuilder s = new StringBuilder();
		if (MapType.equals("panorama")) {
 			s.append("gms['" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "']=new GMaps.createPanorama({");
 			s.append("el:'#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "',");
 			s.append("lat:" + Latitude + ",");
 		    s.append("lng:" + Longitude + ","); 
 		    s.append("pov:{zoom:" + Zoom + ",heading:" + Heading + ",pitch:" + Pitch + "},");       	 		   
 		    s.append("idle: function(e){gmready('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "');},");
 		    s.append("});");
 	    } else {       	 			
 			s.append("gms['" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "']=new GMaps({");
 			s.append("el:'#" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "',");
 			s.append("lat:" + Latitude + ",");
 		    s.append("lng:" + Longitude + ",");
 		    s.append("mapTypeId:'" + MapType + "',");
 		    s.append("zoom:" + Zoom + ",");
 		    if (ClusterSize>0) {
 		    	s.append("markerClusterer: function(map) {");
 		    	s.append("options = {");
 		    	s.append("gridSize: " + ClusterSize + ",");
 		    	s.append("imagePath: 'https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m'");
 		    	s.append("}\n"); 		 
 		    	s.append("return new MarkerClusterer(map, [], options);");
 		    	s.append("},");
 		    }
 		    s.append("click:function(e){gmclickarray('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "', e.latLng);  },");
 		    s.append("idle:function(e){gmready('" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "');  },");
 		    if (!Draggable) {
 		    	s.append("draggable:false,");
 		    }
 		    if (HasMapTypeControl) {
 		    	s.append("mapTypeControlOptions: {");
 		        s.append("mapTypeIds:[");
 		    	for (int index=0;index<mapTypes.size();index++) {
 		    		if (index<mapTypes.size()-1) {
 		    			s.append("'" + mapTypes.get(index) + "',");
 		    		} else {
 		    		s.append("'" + mapTypes.get(index) + "'");
 		    		}
 		    	}
	 			s.append("]");
	 			s.append("},");
	 			
 		    }
 		    if (HasZoomControl) {
 		    	s.append("zoomControl:true,");
 		    	s.append("zoomControlOpt:{");
 		        s.append("style:'SMALL',");
 		        s.append("position:'TOP_LEFT'");
 		        s.append("},");
 		    } else {
 		    	s.append("zoomControl:false,");
 		    }
 		   
 			if (HasStreetViewControl) {
 				s.append("streetViewControl:true,");
 			} else {
 				s.append("streetViewControl:false,");
 			}
 			if (HasMapTypeControl) {
 				s.append("mapTypeControl:true,");
 			} else {
 				s.append("mapTypeControl:false,");
 			}
 			
 			s.append("});");
 	    }
		Page.ws.Eval(s.toString(), null);
		
		super.FirstRun();
	}
	
	public void SetOverlay(double north, double south, double east, double west, String url) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		Params.Add(north);
		Params.Add(south);
		Params.Add(east);
		Params.Add(west);
		Params.Add(url);
		Page.ws.RunFunction("gmsetoverlay", Params);
		try {
			if (Page.ws.getOpen()) {
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}		
	}
	
	public void RemoveOverlay() {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		Page.ws.RunFunction("gmremoveoverlay", Params);
		try {
			if (Page.ws.getOpen()) {
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}	
	}
	
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
		
		v=v.replace("{TCX}", " <tcxspan tcxhref=\"");
		v=v.replace("{TCTB}", "\" title=\"");
		v=v.replace("{TCTE}", "\">");
		v=v.replace("{/TCX}", "</tcxspan>");
		
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
	
	public void AddMapType(String mapType) {
		mapTypes.add(mapType);
	}
	
	public void AddMarker(String markerId, double Latitude, double Longitude, String MarkerColor, String Title, String infoWindow, anywheresoftware.b4a.objects.collections.List polygonIds) {
		ABMaterial.GMAddMarker(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), markerId, Latitude, Longitude, MarkerColor, BuildText(Title), BuildText(infoWindow), false, false, ShowDeleteInInfoWindow, polygonIds);
	}
	
	public void AddMarkerDraggable(String markerId, double Latitude, double Longitude, String MarkerColor, String Title, String infoWindow, anywheresoftware.b4a.objects.collections.List polygonIds) {
		ABMaterial.GMAddMarker(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), markerId, Latitude, Longitude, MarkerColor, BuildText(Title), BuildText(infoWindow), true, false, ShowDeleteInInfoWindow, polygonIds);
	}
	
	public void AddMarkerEx(String markerId, double Latitude, double Longitude, String Title, String infoWindow, String iconPNGUrl, boolean infoWindowOpen, anywheresoftware.b4a.objects.collections.List polygonIds) {
		ABMaterial.GMAddMarkerEx(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), markerId, Latitude, Longitude, BuildText(Title), BuildText(infoWindow), iconPNGUrl, infoWindowOpen, false, false, ShowDeleteInInfoWindow, polygonIds);
	}
	
	public void AddMarkerExDraggable(String markerId, double Latitude, double Longitude, String Title, String infoWindow, String iconPNGUrl, boolean infoWindowOpen, anywheresoftware.b4a.objects.collections.List polygonIds) {
		ABMaterial.GMAddMarkerEx(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), markerId, Latitude, Longitude, BuildText(Title), BuildText(infoWindow), iconPNGUrl, infoWindowOpen, true, false, ShowDeleteInInfoWindow, polygonIds);
	}
	
	public void AddMarkerEx2(String markerId, double Latitude, double Longitude, String Title, String infoWindow, String iconPNGUrl, boolean infoWindowOpen, double anchorPointX, double anchorPointY, anywheresoftware.b4a.objects.collections.List polygonIds) {
		ABMaterial.GMAddMarkerEx2(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), markerId, Latitude, Longitude, BuildText(Title), BuildText(infoWindow), iconPNGUrl, infoWindowOpen, false, anchorPointX, anchorPointY, false, ShowDeleteInInfoWindow,polygonIds);
	}
	
	public void AddMarkerEx2Draggable(String markerId, double Latitude, double Longitude, String Title, String infoWindow, String iconPNGUrl, boolean infoWindowOpen, double anchorPointX, double anchorPointY, anywheresoftware.b4a.objects.collections.List polygonIds) {
		ABMaterial.GMAddMarkerEx2(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), markerId, Latitude, Longitude, BuildText(Title), BuildText(infoWindow), iconPNGUrl, infoWindowOpen, true, anchorPointX, anchorPointY, false,ShowDeleteInInfoWindow,polygonIds);
	}
	
	public void RemoveMarkers() {
		ABMaterial.GMRemoveMarkers(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());		
	}
		
	public void RemoveMarker(String markerId) {
		ABMaterial.GMRemoveMarker(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), markerId);
	}
	
	/**
	 * Check if a coordinate is inside or outside of a geofence. 
	 */
	public boolean CheckGeofence(double Latitude, double Longitude, String polygonId) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		Params.Add(Latitude);
		Params.Add(Longitude);
		Params.Add(polygonId);
		SimpleFuture j = Page.ws.RunFunctionWithResult("gmcheckgeofence", Params);
		if (j!=null) {
			try {
				return (Boolean) j.getValue();			
			} catch (InterruptedException e) {
				//e.printStackTrace();
			} catch (TimeoutException e) {
				//e.printStackTrace();
			} catch (ExecutionException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		BA.Log("CheckGeofence no result");
		return false;		
	}
	
	/**
	 * Triggers an event when a marker is outside its geofences. 
	 */
	public void CheckMarkerGeofence(String markerId) {
		anywheresoftware.b4a.objects.collections.List Params = new anywheresoftware.b4a.objects.collections.List();
		Params.Initialize();
		Params.Add(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		Params.Add(markerId);
		Page.ws.RunFunction("gmcheckmarkergeofence", Params);
		try {
			if (Page.ws.getOpen()) {
				if (Page.ShowDebugFlush) {BA.Log("");}
				Page.ws.Flush();Page.RunFlushed();
			}
		} catch (IOException e) {			
			//e.printStackTrace();
		}
	}
	
	public void FitZoom() {
		ABMaterial.GMFitZoom(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
	}
	
	public int getZoom() {
		return ABMaterial.GMGetZoom(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
	}
	
	public void setZoom(int zoom) {
		this.Zoom = zoom;
		ABMaterial.GMSetZoom(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), zoom);
	}
	
	public void GeoLocate() {
		ABMaterial.GMGeoLocate(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
	}
	
	public void SetLocation(double Latitude, double Longitude) {
		this.Latitude = Latitude;
		this.Longitude = Longitude;
		ABMaterial.GMSetLocation(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), Latitude, Longitude);
	}
	
	public void GeoCode(String address) {
		ABMaterial.GMGeoCode(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), address);
	}
	
	public String ReverseGeoCode(double lat, double lng) {
		return ABMaterial.GMReverseGeoCode(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), lat, lng);
	}	
	
	public void AddPolyLine(String PolylineId, anywheresoftware.b4a.objects.collections.List path, String StrokeColor, String StrokeColorIntensity, double StrokeOpacity, int StrokeWeight ) {
		ABMaterial.GMAddPolyLine(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), path, StrokeColor, StrokeColorIntensity, StrokeOpacity, StrokeWeight, PolylineId);
	}
	
	public void RemovePolyLines() {
		ABMaterial.GMRemovePolyLines(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
	}	
	
	public void RemovePolyLine(String PolylineId) {
		ABMaterial.GMRemovePolyLine(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), PolylineId);
	}
	
	public void AddPolygon(String PolygonId, anywheresoftware.b4a.objects.collections.List path, String StrokeColor, String StrokeColorIntensity, double StrokeOpacity, int StrokeWeight, String FillColor, String FillColorIntensity, double FillColorOpacity) {
		ABMaterial.GMAddPolygon(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), path, StrokeColor, StrokeColorIntensity, StrokeOpacity, StrokeWeight,FillColor, FillColorIntensity, FillColorOpacity, PolygonId);
	}
	
	public void RemovePolygons() {
		ABMaterial.GMRemovePolygons(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
	}
	
	public void RemovePolygon(String PolygonId) {
		ABMaterial.GMRemovePolygon(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), PolygonId);
	}
	
	public void AddRoute(double OriginLatitude, double OriginLongitude, double DestinationLatitude, double DestinationLongitude, String travelMode, String StrokeColor, String StrokeColorIntensity, double StrokeOpacity, int StrokeWeight) {
		ABMaterial.GMAddRoute(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), OriginLatitude, OriginLongitude, DestinationLatitude, DestinationLongitude, travelMode, StrokeColor, StrokeColorIntensity, StrokeOpacity, StrokeWeight );
	}
	
	public void RemoveRoutes() {
		ABMaterial.GMRemoveRoutes(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
	}	
	
	@Override
	public void Refresh() {	
		RefreshInternal(true);
	}
		
	@Override
	protected void RefreshInternal(boolean DoFlush) {
		super.Refresh();
		JQueryElement j = Page.ws.GetElementById(ParentString + ArrayName.toLowerCase() + ID.toLowerCase());
		j.SetProp("class", BuildClass());	
		
		if (!ToolTipText.equals("")) {
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-position", ToolTipPosition);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-delay", ""+ToolTipDelay);
			ABMaterial.SetProperty(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), "data-tooltip", ToolTipText);
		}
		ABMaterial.GMRefresh(Page, ParentString + ArrayName.toLowerCase() + ID.toLowerCase(), Height, Width);
		if (!MapType.equals("panorama")) {
			StringBuilder s = new StringBuilder();
			s.append("mapOptions = {");
			 	if (!Draggable) {
				 	s.append("draggable:false"); // ,");
	 		    } else {
	 		    	s.append("draggable:true"); //,");
	 		    }
	 		s.append("};");
	 		s.append("gms['" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "'].setOptions(mapOptions);");
	 		Page.ws.Eval(s.toString(), null);
		}
		if (DoFlush) {
			try {
				if (Page.ws.getOpen()) {
					if (Page.ShowDebugFlush) {BA.Log("GoogleMap Refresh: " + ID);}
					Page.ws.Flush();Page.RunFlushed();
				}
			} catch (IOException e) {
					//e.printStackTrace();
			}
		}
	}
	
	@Override
	protected String Build() {		
		StringBuilder s = new StringBuilder();	
		
		String toolTip="";
		if (!ToolTipText.equals("")) {
			toolTip=" data-position=\"" + ToolTipPosition + "\" data-delay=\"" + ToolTipDelay + "\" data-tooltip=\"" + ToolTipText + "\" "; 
		}
		String WidthHeight="height: " + Height + "px";
		if (Width>0) {
			WidthHeight = "width: " + Width + "px;" + WidthHeight; 
		}
		
		s.append("<div " + toolTip + " id=\"" + ParentString + ArrayName.toLowerCase() + ID.toLowerCase() + "\" class=\"");
		s.append(BuildClass());	
		s.append("\" style=\"" + WidthHeight + "\">");
		
		s.append(BuildBody());			
		s.append("</div>\n");
	
		IsBuild=true;
		return s.toString();
	}
	
	@Hide
	protected String BuildClass() {			
		StringBuilder s = new StringBuilder();
		s.append(super.BuildExtraClasses());
		if (!ToolTipText.equals("")) {
			s.append("tooltipped ");
		}		
		s.append(mVisibility + " ");	
		s.append(ZDepth + " ");		
		s.append(mIsPrintableClass);
		s.append(mIsOnlyForPrintClass);
		return s.toString(); 
	}
	
	@Hide
	protected String BuildBody() {
		StringBuilder s = new StringBuilder();			
		return s.toString();
	}
	
	@Override
	protected ABMComponent Clone() {
		ABMGoogleMap c = new ABMGoogleMap();
		c.ArrayName = ArrayName;
		c.CellId = CellId;
		c.ID = ID;
		c.Page = Page;
		c.RowId= RowId;		
		c.Type = Type;
		c.mVisibility = mVisibility;	
		c.ToolTipDelay = ToolTipDelay;
		c.ToolTipPosition = ToolTipPosition;
		c.ToolTipText = ToolTipText;		
		c.Zoom=Zoom;
		c.HasZoomControl=HasZoomControl;
		c.HasStreetViewControl=HasStreetViewControl;
		c.HasMapTypeControl=HasMapTypeControl;
		c.Width=Width;
		c.Height=Height;
		c.MapType=MapType;
		c.ZDepth=ZDepth;
		c.Draggable=Draggable;
		c.Heading=Heading;
		c.Pitch=Pitch;
		return c;
	}
	
	
	
	
}
