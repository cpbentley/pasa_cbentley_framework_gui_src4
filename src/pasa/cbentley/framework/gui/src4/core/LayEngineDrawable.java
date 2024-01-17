package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.coredraw.src4.interfaces.IMFont;
import pasa.cbentley.framework.drawx.src4.style.ITechStyle;
import pasa.cbentley.framework.drawx.src4.style.StyleCache;
import pasa.cbentley.framework.gui.src4.canvas.ViewContext;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.layouter.src4.engine.LayEngineRead;
import pasa.cbentley.layouter.src4.engine.LayoutOperator;
import pasa.cbentley.layouter.src4.engine.Zer2DRect;
import pasa.cbentley.layouter.src4.tech.ITechLayout;
import pasa.cbentley.layouter.src4.tech.ITechSizer;

/**
 * A 2D draw sizer defines the size of a rectangle relative to another one.
 * 
 * A value must be kept in a given ratio
 * <br>
 * 
 * Uses a {@link ISizer} to compute width and height pixel values.
 * <br>
 * 
 * This class will size Border, Margins and Padding of the style defintion.
 * <br>
 * 
 * Uses {@link Zer2DRect} was its positions and actual size
 * 
 * Use {@link StyleCache} for style cached values
 * 
 * @author Charles-Philip Bentley
 *
 */
public class LayEngineDrawable extends LayEngineRead implements ITechLayout, IStringable, ITechStyle, IByteObject, ITechDrawable {

   private Drawable       drawable;

   protected final GuiCtx gc;

   protected int          minusH;

   /**
    * Number of pixels that are substracted from dw.
    */
   protected int          minusW;


   /**
    * Never null. However, caching can be disabled
    */
   private StyleCache     styleCache;

   /**
    * Value computed from
    */
   protected int          styleH;

   protected int          styleW;

   public LayEngineDrawable(GuiCtx gc, Drawable drawable) {
      super(gc.getDC().getLAC(), drawable);
      this.gc = gc;
      this.drawable = drawable;

   }

   public void addStyleToHeight(ByteObject style) {
      // TODO Auto-generated method stub

   }

   /**
    * Compute the style width pixel consumption
    * and add it to the.
    * 
    * @param style
    */
   public void addStyleToWidth(ByteObject style) {
      // TODO Auto-generated method stub

   }

   /**
    * Drawable height minus style decoration height pixels.
    * <br>
    * <br>
    * @return
    */
   public int getContentH() {
      return getH() - drawable.getStyleHConsumed();
   }

   /**
    * Drawable width minus decoration width pixels.
    * @return
    */
   public int getContentW() {
      return getW() - drawable.getStyleWConsumed();
   }

   /**
    * Content x coordinate. Take into account Left Margin/Padding/Border.
    * @return
    */
   public int getContentX() {
      return getX() + drawable.getStyleWLeftConsumed();
   }

   /**
    * Content y coordinate. Take into account Top Margin/Padding/Border.
    * @return
    */
   public int getContentY() {
      return getY() + drawable.getStyleHTopConsumed();
   }

   int getEtalonParent(boolean isWidth) {
      int ev;
      IDrawable parent = drawable.getParent();
      if (parent != null && parent.hasState(STATE_05_LAYOUTED)) {
         if (isWidth) {
            ev = parent.getDrawnWidth();
         } else {
            ev = parent.getDrawnHeight();
         }
      } else {
         if (isWidth) {
            ev = drawable.getVC().getWidth();
         } else {
            ev = drawable.getVC().getHeight();
         }
      }
      return ev;
   }

   int getEtalonRatioFont(IMFont font, ViewContext vc, boolean isWidth) {
      int mv = font.getHeight();
      int val = 0;
      if (isWidth) {
         val = vc.getWidth();
      } else {
         val = vc.getHeight();
      }
      return val / mv;
   }

   public int getH() {
      return rect.getH();
   }

   private LayoutOperator getLayOp() {
      return lac.getLayoutOperator();
   }

   /**
    * Never returns null. When null return 
    * @param type
    * @param param
    * @return
    */
   public IDrawable getLink(IDrawable d, int type, int param) {
      IDrawable drw = null;
      if (type == LINK_1_NAV) {
         drw = d.getNavigate(param);
      } else if (type == LINK_2_UIID) {
         drw = gc.getRepo().getDrawable(param);
      }
      if (type == LINK_0_PARENT || drw == null) {
         drw = d.getParentNotNull();
      }
      return drw;
   }

   public int getPH() {
      return rect.getPh();
   }

   public int getPW() {
      return rect.getPw();
   }


   public int getSizePotentialH() {
      return getSizeWComputed();
   }

   /**
    * Compute the max size the drawable could use.
    * 
    * Computed size may include shrank value
    * <br>
    * @return -1 if no maximum size is defined
    */
   public int getSizePotentialW() {
      return getSizeWComputed();
   }

   public StyleCache getStyleCache() {
      return styleCache;
   }

   /**
    * Current drawable width.
    * <br>
    * @return
    */
   public int getW() {
      return rect.getW();
   }


   public int getX() {
      return rect.getX();
   }

   public int getY() {
      return rect.getY();
   }

   private boolean hasFlagSizer(ByteObject sizer, int flag) {
      if (sizer != null) {
         return sizer.hasFlag(ITechSizer.SIZER_OFFSET_01_FLAG, ITechSizer.SIZER_FLAG_2_ALLOW_SHRINK);
      }
      return false;
   }

   public boolean hasFlagSizerH(int flag) {
      ByteObject sizer = getArea().getSizerH();
      return hasFlagSizer(sizer, flag);
   }

   public boolean hasFlagSizerW(int flag) {
      ByteObject sizer = getArea().getSizerW();
      return hasFlagSizer(sizer, flag);
   }



   public void setX(int x) {
      rect.setX(x);
      setManualOverrideX(true);
   }

   public void setXInvalidate(int x) {
      setX(x);
      layoutInvalidatePosition();
   }

   /**
    * <Li> {@link C#LOGIC_1_TOP_LEFT}
    * <Li> {@link C#LOGIC_2_CENTER}
    * <Li> {@link C#LOGIC_3_BOTTOM_RIGHT}
    * @param xLogic
    */
   public void setXLogic(int xLogic) {
      switch (xLogic) {
         case C.LOGIC_1_TOP_LEFT:
            getLay().layPoz_StartToStart_OfParent();
            break;
         case C.LOGIC_2_CENTER:
            getLay().layPoz_MidXToMid_OfParent();
            break;
         case C.LOGIC_3_BOTTOM_RIGHT:
            getLay().layPoz_EndToEnd_Parent();
            break;
         default:
            throw new IllegalArgumentException();
      }
   }

   public void setXYInvalidate(int x, int y) {
      setX(x);
      setY(y);
      layoutInvalidatePosition();
   }

   public void setY(int y) {
      rect.setY(y);
      setManualOverrideY(true);
   }

   public void setYInvalidate(int y) {
      setY(y);
      layoutInvalidatePosition();
   }

   /**
    * <Li> {@link C#LOGIC_1_TOP_LEFT}
    * <Li> {@link C#LOGIC_2_CENTER}
    * <Li> {@link C#LOGIC_3_BOTTOM_RIGHT}
    * @param yLogic
    */
   public void setYLogic(int yLogic) {
      switch (yLogic) {
         case C.LOGIC_1_TOP_LEFT:
            getLay().layPoz_TopToTop_OfParent();
            break;
         case C.LOGIC_2_CENTER:
            getLay().layPoz_MidYToMid_OfParent();
            break;
         case C.LOGIC_3_BOTTOM_RIGHT:
            getLay().layPoz_BotToBot_OfParent();
            break;
         default:
            throw new IllegalArgumentException();
      }
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, LayEngineDrawable.class);
      toStringPrivate(dc);
      dc.appendVarWithSpace("isContextualW", isContextualW());
      dc.appendVarWithSpace("isContextualH", isContextualH());
      dc.append(")");
      dc.append(" Max=(" + getSizeMaxW() + "," + getSizeMaxH());
      dc.append(")");
      dc.append(" Min=(" + getSizeMinW() + "," + getSizeMinH());
      dc.append(")");

      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, LayEngineDrawable.class);
      toStringPrivate(dc);

      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }
   //#enddebug

}
