package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.objects.color.IBOFilter;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.IntUtils;
import pasa.cbentley.framework.coredraw.src4.interfaces.IImage;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.engine.RgbImage;
import pasa.cbentley.framework.drawx.src4.utils.AnchorUtils;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IToStringFlagsDraw;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechViewDrawable;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;
import pasa.cbentley.framework.gui.src4.utils.DrawableUtilz;

/**
 * 
 * ImageDrawable uses DimConstraint
 * 
 * Style borders and foreground are applied
 * Background is applied only if image has non opaque pixels
 * Text effect has the purpose of Image effects => color filters, scale filter are applied
 * Like the FigDrawable may override its style figure, The Image can override Filters by setting specific
 * color and scale filters
 * How do we know if a RgbImage has been processed by the filters and style decoration?
 * 
 * <b>Logical Constraint</b> <br>
 * Images don't have an inner logic like String with lines. Therefore they must use an external source for mapping
 * logical units to pixels.
 * 
 * @author Charles-Philip Bentley
 *
 */
public class ImageDrawable extends ViewDrawable {

   /**
    * {@link IBOFilter}
    */
   private ByteObject filter;

   /**
    * Provides content cache automatically :)
    * Decoration may be applied to cache
    * For instance, alpha foreground will be applied, but not borders
    * A background with an image with translucent pixels will also be
    * applied.
    * <br>
    * Cannot be null
    */
   private RgbImage   img;

   /**
    * Define the resize of the image
    * Zoom in/out + Dimension + Algorithm
    */
   private ByteObject scale;

   private int        xincr = 20;

   private int        yincr = 20;

   public ImageDrawable(GuiCtx gc, RgbImage img) {
      this(gc, null, img, false);
   }

   public ImageDrawable(GuiCtx gc, RgbImage img, ByteObject filter) {
      this(gc, null, img, false);
      this.filter = filter;
      //applying a filter, we must remove the srcLocator TODO
      //or tag the image as modified
      gc.getDC().getRgbImageOperator().applyColorFilter(filter, img);
   }

   public ImageDrawable(GuiCtx gc, StyleClass sc, RgbImage img) {
      this(gc, sc, img, false);
   }

   public ImageDrawable(GuiCtx gc, StyleClass sc, RgbImage img, boolean trim) {
      super(gc, sc);
      if (img == null)
         throw new NullPointerException();
      this.img = img;
      setStateFlag(ITechDrawable.STATE_14_CACHED, true);
      setFlagGene(ITechViewDrawable.FLAG_GENE_28_NOT_SCROLLABLE, trim);
   }

   /**
    * To be called only if cache
    * @param g
    * @param m
    * @param n
    * @param w
    * @param h
    * @param x coordinate for drawing image region
    * @param y coordinate for drawing image region
    */
   private void drawImageRegion(GraphicsX g, int m, int n, int w, int h, int x, int y) {
      g.drawRgbImage(img, m, n, w, h, IImage.TRANSFORM_0_NONE, x, y);
   }

   /**
    * Called by {@link ViewDrawable} between bg and fg layers. <br>
    * 
    * An image ignores
    * <br>
    * <li> {@link ITechViewPane#VISUAL_0_LEAVE}
    * <li> {@link ITechViewPane#VISUAL_1_PARTIAL}
    * <li> {@link ITechViewPane#VISUAL_3_FILL}
    * <br>
    * 
    */
   public void drawViewDrawableContent(GraphicsX g, int x, int y, ScrollConfig scx, ScrollConfig scy) {
      int w = getContentW();
      int h = getContentH();

      //#mdebug
      if (gc.toStringHasFlagDraw(IToStringFlagsDraw.FLAG_DRAW_05_IMAGE_DRAWABLE_BOUNDARY)) {
         g.setColor(255, 120, 20);
         g.drawRect(getX()-1, getY()-1, getDrawnWidth()+1, getDrawnHeight()+1);
         g.setColor(255, 200, 200);
         g.drawRect(x, y, w, h);
      }
      //#enddebug

      //#debug
      toDLog().pDraw("x,y=" + y + "," + y + " contentWH=" + w + "," + h, this, ImageDrawable.class, "drawViewDrawableContent@118", LVL_05_FINE, true);

      //what happens if there is a super clip that wants to be inforced? Like for the PinBoard?
      //we must take the intersection or not?
      g.clipSet(x, y, w, h, GraphicsX.CLIP_DIRECTIVE_0_INTERSECTION);
      x = drawVScrollMod(x, scx, xincr, getContentW(), img.getWidth() - getContentW());
      y = drawVScrollMod(y, scy, yincr, getContentH(), img.getHeight() - getContentH());

      //apply Anchor if any
      ByteObject anchor = getStyleOp().getStyleAnchor(style);
      if (anchor != null) {
         if (scx == null) {
            x += AnchorUtils.getXAlign(anchor, 0, getContentW(), img.getWidth());
         }
         if (scy == null) {
            y += AnchorUtils.getYAlign(anchor, 0, getContentH(), img.getHeight());
         }
      }
      g.drawRgbImage(img, x, y);
      //#mdebug
      if (gc.toStringHasFlagDraw(IToStringFlagsDraw.FLAG_DRAW_07_IMAGE_BOUNDARY)) {
         g.setColor(255, 120, 20);
         g.drawRect(x-1, y-1, img.getWidth()+1, img.getHeight()+1);
         g.setColor(255, 200, 200);
         g.drawRect(x, y, w, h);
      }
      //#enddebug
      g.clipReset();
   }

   /**
    * 
    * @param v
    * @param sc
    * @param logicIncr
    * @param pageIncr
    * @param max
    * @return
    */
   private int drawVScrollMod(int v, ScrollConfig sc, int logicIncr, int pageIncr, int max) {
      if (sc != null) {
         int c = sc.getSIStart();//multiply this
         int val = 0;
         switch (sc.getTypeUnit()) {
            case ITechViewPane.SCROLL_TYPE_0_PIXEL_UNIT:
               //draw helper will automatically starts/stops when characters are over the content area.
               v -= c;
               //compute within which logic unit it falls.
               break;
            case ITechViewPane.SCROLL_TYPE_1_LOGIC_UNIT:
               val = Math.min(c * logicIncr, max);
               v -= val;
               break;
            case ITechViewPane.SCROLL_TYPE_2_PAGE_UNIT:
               val = Math.min(c * pageIncr, max);
               v -= val;
               break;
            default:
               throw new IllegalArgumentException();
         }
      }
      return v;
   }

   public RgbImage getImage() {
      return img;
   }

   /**
    * 
    */
   public void initScrollingConfig(ScrollConfig scX, ScrollConfig scY) {
      if (scX != null) {
         if (scX.getTypeUnit() == ITechViewPane.SCROLL_TYPE_1_LOGIC_UNIT) {
            int totalScrollingIncrement = IntUtils.divideCeil(img.getWidth(), xincr);
            scX.initConfigLogic(xincr, getContentW(), totalScrollingIncrement);
         } else {
            DrawableUtilz.initConfigX(this, scX);
         }
      }
      if (scY != null) {
         if (scY.getTypeUnit() == ITechViewPane.SCROLL_TYPE_1_LOGIC_UNIT) {
            int totalScrollingIncrement = IntUtils.divideCeil(img.getHeight(), yincr);
            scY.initConfigLogic(yincr, getContentH(), totalScrollingIncrement);
         } else {
            DrawableUtilz.initConfigY(this, scY);
         }
      }
   }

   protected void initViewPortSub(int width, int height) {
      //#debug
      toDLog().pFlow("", this, ImageDrawable.class, "initViewPortSub@200", LVL_05_FINE, true);
   }

   public void initViewDrawable(LayouterEngineDrawable engine) {
      //#debug
      toDLog().pFlow("", this, ImageDrawable.class, "initViewDrawable@220", LVL_05_FINE, true);
      
      //image drawable just draws itself
      int ph = img.getHeight();
      int pw = img.getWidth();
      layEngine.setPh(ph);
      layEngine.setPw(pw);
   }

   public void setImage(RgbImage img) {
      this.img = img;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, ImageDrawable.class, 220);
      toStringPrivate(dc);
      super.toString(dc.sup());

      dc.nlLvl(img);
      dc.nlLvlIgnoreNull("Filter", filter);
      dc.nlLvl(scale, "scale");
   }

   private void toStringPrivate(Dctx dc) {
      dc.appendVarWithSpace("xincr", xincr);
      dc.appendVarWithSpace("yincr", yincr);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ImageDrawable.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

  

   //#enddebug

}
