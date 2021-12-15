package com.ab.abmaterial;

import java.util.ArrayList;
import java.util.List;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.ShortName;

@Author("Alain Bailleul") 
@ShortName("ABMCanvasObject")
public class ABMCanvasObject implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5272269921919062195L;
	public String ID="";
	protected int Type=0;
	protected double X=0;
	protected double Y=0;
	protected double Width=0;
	protected double Height=0;
	protected double Radius=0;
	protected transient List<ABMCanvasCommand> Commands = new ArrayList<ABMCanvasCommand>();
	protected transient ABMCanvas MyCanvas=null;
	protected boolean Draggable=false;
	protected boolean XChanged=false;
	protected boolean YChanged=false;
	protected transient ABMPage Page;
	protected double DraggableLeft=0;
	protected double DraggableTop=0;
	protected double DraggableWidth=0;
	protected double DraggableHeight=0;
	public boolean IsInitialized=false;
	protected boolean mClickable=false;
	
	public void InitializeAsRectangle(ABMPage page, String id, int x, int y, int width, int height, boolean raiseMouseDownUp) {
		ID=id;
		this.X=x;
		this.Y=y;
		this.Width = width;
		this.Height = height;
		this.Type=0;
		this.Draggable=raiseMouseDownUp;
		this.Page=page;
		if (raiseMouseDownUp) {
			SetDragZone(x,y,0.001,0.001);
		}		
		IsInitialized=true;
	}
	
	public void InitializeAsCircle(ABMPage page, String id, int centerX, int centerY, int radius, boolean raiseMouseDownUp) {
		ID=id;
		this.X=centerX;
		this.Y=centerY;
		this.Radius = radius;
		this.Type=1;
		this.Draggable=raiseMouseDownUp;
		if (raiseMouseDownUp) {
			SetDragZone(centerX,centerY,0.001,0.001);
		}
		this.Page=page;
		IsInitialized=true;
	}
	
	public void SetDragZone(double left, double top, double width, double height) {
		if (!Draggable) {
			BA.Log(ID + " is not draggable. Unable to set dragzone.");
			return;
		}
		this.DraggableLeft=left;
		this.DraggableTop=top;
		this.DraggableWidth=width;
		this.DraggableHeight=height;
	}
	
	public void setClickable(boolean bool) {
		if (Draggable && bool) {
			BA.Log("This object is already draggable, you will need to use the _ObjectDown(); and ObjectUp() events. Setting clickable cancelled");
			mClickable = false;
			return;
		}
		mClickable=bool;
	}
	
	public boolean getClickable() {
		return mClickable;
	}
	
	public void Refresh() {
		if (!MyCanvas.CanvasCommands.containsKey(ID.toLowerCase())) {
			MyCanvas.CanvasCommands.put(ID.toLowerCase(), "1");
		}
	}
	
	public void Clear() {
		Commands.clear();
	}
	
	public double getWidth() {
		return this.Width;
	}
	
	public double getHeight() {
		return this.Height;
	}
	
	public void SetPosition(double x, double y) {
		if (x!=X) {
			XChanged=true;
		}
		if (y!=Y) {
			YChanged=true;
		}
		this.X = x;
		this.Y = y;
	}
	
	public void SetSize(double width, double height) {
		this.Width=width;
		this.Height=height;
	}
	
	public ABMPoint GetPosition() {
		ABMPoint p = ABMaterial.CanvasGetPosition(Page,  MyCanvas.ParentString.toLowerCase() + MyCanvas.ArrayName.toLowerCase() + MyCanvas.ID.toLowerCase(), ID.toLowerCase());
		if (p!=null) {
			this.X = p.x;
			this.Y = p.y;
		}
		return p;
	}
	
	protected void CleanUp() {
		MyCanvas=null;
		Page=null;
	}
	
	public void fillStyleColor(String color) {
		ABMCanvasCommand com = new ABMCanvasCommand(1);
		com.Params.add(color);
		Commands.add(com);
	}
	
	public void fillStyleLinearGradient(double x0, double y0, double x1, double y1, anywheresoftware.b4a.objects.collections.List Stops, anywheresoftware.b4a.objects.collections.List Colors) {
		ABMCanvasCommand com = new ABMCanvasCommand(2);
		com.Params.add(x0);
		com.Params.add(y0);
		com.Params.add(x1);
		com.Params.add(y1);
		com.Params.add(Stops);
		com.Params.add(Colors);
		Commands.add(com);
	}
	
	public void fillStyleRadialGradient(double x0, double y0, double r0, double x1, double y1, double r1, anywheresoftware.b4a.objects.collections.List Stops, anywheresoftware.b4a.objects.collections.List Colors) {
		ABMCanvasCommand com = new ABMCanvasCommand(3);
		com.Params.add(x0);
		com.Params.add(y0);
		com.Params.add(r0);
		com.Params.add(x1);
		com.Params.add(y1);
		com.Params.add(r1);
		com.Params.add(Stops);
		com.Params.add(Colors);
		Commands.add(com);
	}
	
	public void strokeStyleColor(String Color) {
		ABMCanvasCommand com = new ABMCanvasCommand(4);
		com.Params.add(Color);
		Commands.add(com);
	}
	
	public void strokeStyleLinearGradient(double x0, double y0, double x1, double y1, anywheresoftware.b4a.objects.collections.List Stops, anywheresoftware.b4a.objects.collections.List Colors) {
		ABMCanvasCommand com = new ABMCanvasCommand(5);
		com.Params.add(x0);
		com.Params.add(y0);
		com.Params.add(x1);
		com.Params.add(y1);
		com.Params.add(Stops);
		com.Params.add(Colors);
		Commands.add(com);
	}
	
	public void strokeStyleRadialGradient(double x0, double y0, double r0, double x1, double y1, double r1, anywheresoftware.b4a.objects.collections.List Stops, anywheresoftware.b4a.objects.collections.List Colors) {
		ABMCanvasCommand com = new ABMCanvasCommand(6);
		com.Params.add(x0);
		com.Params.add(y0);
		com.Params.add(r0);
		com.Params.add(x1);
		com.Params.add(y1);
		com.Params.add(r1);
		com.Params.add(Stops);
		com.Params.add(Colors);
		Commands.add(com);
	}
	
	public void shadowBlur(double blur) {
		ABMCanvasCommand com = new ABMCanvasCommand(7);
		com.Params.add(blur);
		Commands.add(com);
	}
	
	public void shadowColor(String color) {
		ABMCanvasCommand com = new ABMCanvasCommand(8);
		com.Params.add(color);
		Commands.add(com);
	}
	
	public void shadowOffsetX(double x) {
		ABMCanvasCommand com = new ABMCanvasCommand(9);
		com.Params.add(x);
		Commands.add(com);
	}
	
	public void shadowOffsetY(double y) {
		ABMCanvasCommand com = new ABMCanvasCommand(10);
		com.Params.add(y);
		Commands.add(com);
	}
	
	public void lineCap(String linecap) {
		ABMCanvasCommand com = new ABMCanvasCommand(11);
		com.Params.add(linecap);
		Commands.add(com);
	}
	
	public void lineJoin(String linejoin) {
		ABMCanvasCommand com = new ABMCanvasCommand(12);
		com.Params.add(linejoin);
		Commands.add(com);
	}
	
	public void lineWidth(double width) {
		ABMCanvasCommand com = new ABMCanvasCommand(13);
		com.Params.add(width);
		Commands.add(com);
	}
	
	public void miterLimit(int limit) {
		ABMCanvasCommand com = new ABMCanvasCommand(14);
		com.Params.add(limit);
		Commands.add(com);
	}
	
	public void rect(double x, double y, double width, double height) {
		ABMCanvasCommand com = new ABMCanvasCommand(15);
		com.Params.add(x);
		com.Params.add(y);
		com.Params.add(width);
		com.Params.add(height);
		Commands.add(com);
	}
	
	public void fillRect(double x, double y, double width, double height) {
		ABMCanvasCommand com = new ABMCanvasCommand(16);
		com.Params.add(x);
		com.Params.add(y);
		com.Params.add(width);
		com.Params.add(height);
		Commands.add(com);
	}
	
	public void strokeRect(double x, double y, double width, double height) {
		ABMCanvasCommand com = new ABMCanvasCommand(17);
		com.Params.add(x);
		com.Params.add(y);
		com.Params.add(width);
		com.Params.add(height);
		Commands.add(com);
	}
	
	public void clearRect(double x, double y, double width, double height) {
		ABMCanvasCommand com = new ABMCanvasCommand(18);
		com.Params.add(x);
		com.Params.add(y);
		com.Params.add(width);
		com.Params.add(height);
		Commands.add(com);
	}
	
	public void fill() {
		ABMCanvasCommand com = new ABMCanvasCommand(19);
		Commands.add(com);
	}
	
	public void stroke() {
		ABMCanvasCommand com = new ABMCanvasCommand(20);
		Commands.add(com);
	}
	
	public void beginPath() {
		ABMCanvasCommand com = new ABMCanvasCommand(21);
		Commands.add(com);
	}
	
	public void moveTo(double x, double y) {
		ABMCanvasCommand com = new ABMCanvasCommand(22);
		com.Params.add(x);
		com.Params.add(y);
		Commands.add(com);
	}
	
	public void closePath() {
		ABMCanvasCommand com = new ABMCanvasCommand(23);
		Commands.add(com);
	}
	
	public void lineTo(double x, double y) {
		ABMCanvasCommand com = new ABMCanvasCommand(24);
		com.Params.add(x);
		com.Params.add(y);
		Commands.add(com);
	}
	
	public void clip() {
		ABMCanvasCommand com = new ABMCanvasCommand(25);
		Commands.add(com);
	}
	
	public void quadraticCurveTo(double cpx, double cpy, double x, double y) {
		ABMCanvasCommand com = new ABMCanvasCommand(26);
		com.Params.add(cpx);
		com.Params.add(cpy);
		com.Params.add(x);
		com.Params.add(y);
		Commands.add(com);
	}
	
	public void bezierTo(double cp1x, double cp1y, double cp2x, double cp2y, double x, double y) {
		ABMCanvasCommand com = new ABMCanvasCommand(27);
		com.Params.add(cp1x);
		com.Params.add(cp1y);
		com.Params.add(cp2x);
		com.Params.add(cp2y);
		com.Params.add(x);
		com.Params.add(y);
		Commands.add(com);
	}
		
	public void arc(double x, double y, double r, double sAngle, double eAngle) {
		ABMCanvasCommand com = new ABMCanvasCommand(28);
		com.Params.add(x);
		com.Params.add(y);
		com.Params.add(r);
		com.Params.add(sAngle);
		com.Params.add(eAngle);
		Commands.add(com);
	}
	
	public void arc2(double x, double y, double r, double sAngle, double eAngle, boolean counterClockwise) {
		ABMCanvasCommand com = new ABMCanvasCommand(47);
		com.Params.add(x);
		com.Params.add(y);
		com.Params.add(r);
		com.Params.add(sAngle);
		com.Params.add(eAngle);
		com.Params.add(counterClockwise);
		Commands.add(com);
	}
	
	public void arcTo(double x1, double y1, double x2, double y2, double r) {
		ABMCanvasCommand com = new ABMCanvasCommand(29);
		com.Params.add(x1);
		com.Params.add(y1);
		com.Params.add(x2);
		com.Params.add(y2);
		com.Params.add(r);
		Commands.add(com);
	}
	
	public void scale(double scaleWidth, double scaleHeight) {
		ABMCanvasCommand com = new ABMCanvasCommand(30);
		com.Params.add(scaleWidth);
		com.Params.add(scaleHeight);
		Commands.add(com);
	}
	
	public void rotate(double angle) {
		ABMCanvasCommand com = new ABMCanvasCommand(31);
		com.Params.add(angle);
		Commands.add(com);
	}
	
	public void translate(double x, double y) {
		ABMCanvasCommand com = new ABMCanvasCommand(32);
		com.Params.add(x);
		com.Params.add(y);
		Commands.add(com);
	}
	
	public void transform(double a, double b, double c, double d, double e, double f) {
		ABMCanvasCommand com = new ABMCanvasCommand(33);
		com.Params.add(a);
		com.Params.add(b);
		com.Params.add(c);
		com.Params.add(d);
		com.Params.add(e);
		com.Params.add(f);
		Commands.add(com);
	}
	
	public void SetTransform(double a, double b, double c, double d, double e, double f) {
		ABMCanvasCommand com = new ABMCanvasCommand(34);
		com.Params.add(a);
		com.Params.add(b);
		com.Params.add(c);
		com.Params.add(d);
		com.Params.add(e);
		com.Params.add(f);
		Commands.add(com);
	}
	
	public void font(String fontName, int sizePx) {
		ABMCanvasCommand com = new ABMCanvasCommand(35);
		com.Params.add(sizePx + "px " + fontName);		
		Commands.add(com);
	}
	
	public void font2(String fontName, String style, String variant, String weight, int sizePx) {
		ABMCanvasCommand com = new ABMCanvasCommand(35);
		com.Params.add(style + " " + variant + " " + weight + " " + sizePx + "px " + fontName);		
		Commands.add(com);
	}
	
	public void textAlign(String align) {
		ABMCanvasCommand com = new ABMCanvasCommand(36);
		com.Params.add(align);
		Commands.add(com);
	}
	
	public void textBaseline(String baseLine) {
		ABMCanvasCommand com = new ABMCanvasCommand(37);
		com.Params.add(baseLine);
		Commands.add(com);
	}
	
	public void fillText(String text, double x, double y) {
		ABMCanvasCommand com = new ABMCanvasCommand(38);
		com.Params.add(text);
		com.Params.add(x);
		com.Params.add(y);
		com.Params.add(9999999);
		Commands.add(com);
	}
	
	public void fillText2(String text, double x, double y, double maxWidth) {
		ABMCanvasCommand com = new ABMCanvasCommand(38);
		com.Params.add(text);
		com.Params.add(x);
		com.Params.add(y);
		com.Params.add(maxWidth);
		Commands.add(com);
	}
	
	public void strokeText(String text, double x, double y, double maxWidth) {
		ABMCanvasCommand com = new ABMCanvasCommand(39);
		com.Params.add(text);
		com.Params.add(x);
		com.Params.add(y);
		com.Params.add(maxWidth);
		Commands.add(com);
	}
	
	public void drawImage(String imageId, double x, double y) {
		ABMCanvasCommand com = new ABMCanvasCommand(40);
		com.Params.add(imageId);
		com.Params.add(x);
		com.Params.add(y);
		Commands.add(com);
	}
	
	public void drawImage2(String imageId, double x, double y, double width, double height) {
		ABMCanvasCommand com = new ABMCanvasCommand(41);
		com.Params.add(imageId);
		com.Params.add(x);
		com.Params.add(y);
		com.Params.add(width);
		com.Params.add(height);
		Commands.add(com);
	}
	
	public void drawImage3(String imageId, double sx, double sy, double swidth, double sheight, double x, double y, double width, double height) {
		ABMCanvasCommand com = new ABMCanvasCommand(42);
		com.Params.add(imageId);
		com.Params.add(sx);
		com.Params.add(sy);
		com.Params.add(swidth);
		com.Params.add(sheight);
		com.Params.add(x);
		com.Params.add(y);
		com.Params.add(width);
		com.Params.add(height);
		Commands.add(com);
	}
	
	public void globalAlpha(double alpha) {
		ABMCanvasCommand com = new ABMCanvasCommand(43);
		com.Params.add(alpha);
		Commands.add(com);
	}
	
	public void globalCompositeOperation(String operation) {
		ABMCanvasCommand com = new ABMCanvasCommand(44);
		com.Params.add(operation);
		Commands.add(com);
	}
	
	public void save() {
		ABMCanvasCommand com = new ABMCanvasCommand(45);
		Commands.add(com);
	}
	
	public void restore() {
		ABMCanvasCommand com = new ABMCanvasCommand(46);
		Commands.add(com);
	}

}



