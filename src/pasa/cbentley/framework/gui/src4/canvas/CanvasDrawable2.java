package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.engine.CmdNode;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdListener;
import pasa.cbentley.framework.coredraw.src4.interfaces.IGraphics;
import pasa.cbentley.framework.coreui.src4.event.BEvent;
import pasa.cbentley.framework.coreui.src4.utils.ViewState;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.engine.RgbImage;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceDrawable;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.CanvasGuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ObjectGC;
import pasa.cbentley.framework.gui.src4.interfaces.IAnimable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.input.src4.CanvasAppliInput;
import pasa.cbentley.framework.input.src4.InputState;
import pasa.cbentley.framework.input.src4.CanvasResult;

/**
 * Drawable to be used inside another {@link CanvasAppliInputGui}.
 * <br>
 * <br>
 * When Parent has an event, it.
 * 
 * <br>
 * When a Single
 * @author Charles Bentley
 *
 */
public abstract class CanvasDrawable2 extends ObjectGC implements IDrawable {

   protected final CanvasGuiCtx  cgc;

   private IDrawable             parent;

   private Drawable              root;

   private int                   uiid;

   protected CanvasAppliInputGui canvas;


   /**
    * 
    * @param cgc the canvas on which its being drawn.
    * @param canvas
    */
   public CanvasDrawable2(CanvasGuiCtx cgc, CanvasAppliInputGui canvas, StyleClass sc) {
      super(cgc.getGC());
      this.cgc = cgc;
      this.canvas = canvas;
      root = new Drawable(gc, sc);
      this.root.setParent(this);
      uiid = gc.getRepo().nextUIID();
   }

   public CanvasGuiCtx getCGC() {
      return cgc;
   }

   public void addFullAnimation(IAnimable anim) {
      // TODO Auto-generated method stub

   }

   public void commandAction(CmdInstanceDrawable cd) {
      // TODO Auto-generated method stub

   }

   public void commandAction(CmdInstance cmd) {
      // TODO Auto-generated method stub

   }

   /**
    * Sub class 
    */
   public void draw(GraphicsX g) {
      //#debug
      toDLog().pDraw("Clip " + "[" + g.getClipX() + "," + g.getClipY() + " " + g.getClipWidth() + "," + g.getClipHeight() + "]", null, CanvasAppliInput.class, "render");

      root.drawDrawable(g);
      canvas.paint(g.getGraphics());
   }

   public void drawDrawable(GraphicsX g) {
      root.drawDrawable(g);
      canvas.paint(g.getGraphics());
   }

   public IDrawable[] getChildren() {
      // TODO Auto-generated method stub
      return null;
   }

   public MCmd getCmd(int vcmdid) {
      // TODO Auto-generated method stub
      return null;
   }

   public CmdNode getCmdNode() {
      return root.getCmdNode();
   }

   public int getCType() {
      return root.getCType();
   }

   public int getDrawnHeight() {
      return root.getDrawnHeight();
   }

   public int getDrawnWidth() {
      return root.getDrawnWidth();
   }

   public int[] getHoles() {
      return root.getHoles();
   }

   public String getName() {
      return "" + hashCode();
   }

   public IDrawable getNavigate(int navEvent) {
      return null;
   }

   public IDrawable getParent() {
      return parent;
   }

   public RgbImage getRgbCache(int type) {
      // TODO Auto-generated method stub
      return null;
   }

   public RgbImage getRgbImage(int type) {
      // TODO Auto-generated method stub
      return null;
   }

   public int getSizeH(int sizeType) {
      // TODO Auto-generated method stub
      return 0;
   }

   public int getSizeW(int sizeType) {
      // TODO Auto-generated method stub
      return 0;
   }

   public ByteObject getStructStyle() {
      // TODO Auto-generated method stub
      return null;
   }

   public ByteObject getStyle() {
      // TODO Auto-generated method stub
      return null;
   }

   public StyleClass getStyleClass() {
      // TODO Auto-generated method stub
      return null;
   }

   public int getUIID() {
      return uiid;
   }

   public ViewContext getVC() {
      // TODO Auto-generated method stub
      return null;
   }

   public ViewState getViewState() {
      // TODO Auto-generated method stub
      return null;
   }

   public int getX() {
      // TODO Auto-generated method stub
      return 0;
   }

   public int getY() {
      // TODO Auto-generated method stub
      return 0;
   }

   public int getZIndex() {
      // TODO Auto-generated method stub
      return 0;
   }

   public boolean hasBehavior(int flag) {
      // TODO Auto-generated method stub
      return false;
   }

   public boolean hasState(int flag) {
      return root.hasState(flag);
   }

   public boolean hasStateNav(int flag) {
      return root.hasStateNav(flag);
   }

   public boolean hasStateStyle(int flag) {
      return root.hasStateStyle(flag);
   }

   public void initSize() {
      // TODO Auto-generated method stub

   }

   public void init(int width, int height) {
      //we are init on the  
   }

   public boolean isOpaque() {
      // TODO Auto-generated method stub
      return false;
   }

   public void layoutInvalidate(boolean topDown) {
      // TODO Auto-generated method stub

   }

   public void layoutUpdate(IDrawable child) {
      // TODO Auto-generated method stub

   }

   public void manageInput(InputConfig ic) {
      generateEvent(ic);
   }
   
   protected void generateEvent(InputConfig ic) {
      BEvent eventCurrent = ic.getInputState().getEventCurrent();
      canvas.event(eventCurrent);
   }

   public void manageKeyInput(InputConfig ic) {
      generateEvent(ic);

   }

   public void managePointerInput(InputConfig ic) {
      generateEvent(ic);
   }

   public void managePointerStateStyle(InputConfig ic) {
      // TODO Auto-generated method stub

   }

   public void notifyEvent(int event) {
      this.notifyEvent(event, null);
   }

   public void notifyEvent(int event, Object o) {
      switch (event) {
         case ITechDrawable.EVENT_01_NOTIFY_SHOW:
            break;
         case ITechDrawable.EVENT_02_NOTIFY_HIDE:
            break;
         case ITechDrawable.EVENT_07_POINTER_FOCUS_LOSS:
            break;

         default:
            break;
      }
   }


   public int sendEvent(int evType, Object param) {
      return ICmdListener.PRO_STATE_0;
   }

   public void setBehaviorFlag(int flag, boolean value) {
      // TODO Auto-generated method stub

   }

   public void setCacheType(int type) {
      // TODO Auto-generated method stub

   }

   public void setCType(int num) {
      // TODO Auto-generated method stub

   }

   public void setParent(IDrawable d) {
      parent = d;
   }

   public void setSizers(ByteObject w, ByteObject h) {
      // TODO Auto-generated method stub

   }

   public void setStateFlag(int flag, boolean value) {
      // TODO Auto-generated method stub

   }

   public void setStateNav(int flag, boolean value) {
      // TODO Auto-generated method stub

   }

   public void setStateStyle(int flag, boolean value) {
      // TODO Auto-generated method stub

   }

   public void setStructStyle(ByteObject style) {
      // TODO Auto-generated method stub

   }

   public void setStyleClass(StyleClass sc) {
      // TODO Auto-generated method stub

   }

   public void setViewState(ViewState vs) {
      // TODO Auto-generated method stub

   }

   public void setX(int x) {
      // TODO Auto-generated method stub

   }

   public void setXY(int x, int y) {
      // TODO Auto-generated method stub

   }

   public void setXYLogic(int xLogic, int yLogic) {
      // TODO Auto-generated method stub

   }

   public void setY(int y) {
      // TODO Auto-generated method stub

   }

   public void setZIndex(int zindex) {
      // TODO Auto-generated method stub

   }

   public void shiftXY(int dx, int dy) {
      // TODO Auto-generated method stub

   }

   public void show(GraphicsX g) {
      // TODO Auto-generated method stub

   }

   public String toStringOneLineStates() {
      // TODO Auto-generated method stub
      return null;
   }

}
