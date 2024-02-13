package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesDrw;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.engine.RgbImage;
import pasa.cbentley.framework.drawx.src4.factories.interfaces.IBOFigure;
import pasa.cbentley.framework.drawx.src4.tech.ITechFigure;
import pasa.cbentley.framework.gui.src4.anim.IBOAnim;
import pasa.cbentley.framework.gui.src4.anim.ITechAnim;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.ctx.IToStringFlagsDraw;
import pasa.cbentley.framework.gui.src4.interfaces.IAnimable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.utils.SourceCache;

/**
 * {@link Drawable} whose content is a ByteObject Figure that espouses the sizers.
 * <br>
 * Its preferred size and logic sizes are parent, then ViewContext. Default implementation
 * of {@link Drawable}.
 * 
 * When the figure as an internal sizers and anchors, the sizers don't take into account the Drawable sizers
 * <br>
 * <br>
 * Alernatively, one may use an {@link RgbImage} sourced with a {@link ByteObject} figure in an {@link ImageDrawable}.
 * <br>
 * <br>
 * <b>Services provided by the framework</b> : <br>
 * <li>cache service
 *  <li>animation hooks
 *  <br>
 *  <br>
 * <b>Anchor</b> <br>
 * The figure fills the {@link Drawable} content area when no anchor is defined. 
 * <br>
 * It asks an implicit compute without an
 * anchor, the FigDrawable falls back the static default size
 * <br>
 * <br>
 * <b>Direction Parameter</b> : <br>
 * Add directional bias to the style figures.<br>
 * Any figure with {@link IDrw#FIG_FLAGZ_8_DIRECTION} Dir support in the style will be drawn using the parameter of this class.
 * <br>
 * <br>
 * The {@link Drawable} class already support automatic figure drawing via the style G layers. 
 * <br>
 * <b> When to use {@link FigDrawable}? </b><br>
 * <li> Encapsulate a direction parameter
 * <li>
 * <li> Figure FigDrawable Anchor impacts the dimension. ByteObject anchor also does it.
 * <li> It also offers fine grained animation control specific to a ByteObject figure. Style layers offer the same
 * <br>
 * <br>
 * For Scrolling a {@link ByteObject} figure, see {@link ImageDrawable} and definition of an {@link RgbImage} with a {@link ByteObject}
 * <br>
 * Figure cache source.
 * <br>
 * <br>
 * @author Charles-Philip Bentley
 * @see Drawable
 */
public class FigDrawable extends Drawable implements ITechFigure {

   /**
    * Directional parameters for the figure.. TODO
    * {@link C#DIR_0_TOP}
    * <br>
    * <br>
    * 
    */
   protected int         dir;

   /**
    * Specific figure. Maybe Null.
    * <br>
    * <br>
    * Animations of this figure are managed by {@link Drawable}.
    * <br>
    * <br>
    * 
    */
   protected ByteObject  figure;

   private int           lastDrawVersion;

   protected SourceCache sc;

   /**
    * Root figure for that styleKey
    * @param fig
    * @param styleKey
    */
   public FigDrawable(GuiCtx gc, StyleClass sc, ByteObject fig) {
      super(gc, sc);
      setFigure(fig);
   }

   public void drawAt(GraphicsX g, int x, int y) {
      setXY(x, y);
      draw(g);
   }

   /**
    * Figure is a DrwParam.
    */
   public void drawDrawable(GraphicsX g) {
      //#mdebug
      if (gc.toStringHasFlagDraw(IToStringFlagsDraw.FLAG_DRAW_02_FIG_DRAWABLE_BOUNDARY)) {
         g.setColor(255, 20, 20);
         g.drawRect(getX(), getY(), getDrawnWidth(), getDrawnHeight());
      }
      //#enddebug
      if (getDw() >= 0 && getDh() >= 0) {
         super.drawDrawableBg(g);
         //fetch the figure for the style
         //Paint figure with a Random object will always draw a different figure
         //When should the seed be changed? The seed is stored as X bytes value in the Figure DrwParam
         //This value is computed from the master seed
         drawDrawableContent(g, getContentX(), getContentY(), getContentW(), getContentH());
         super.drawDrawableFg(g);
      }
   }

   public void drawDrawableContent(GraphicsX g, int x, int y, int w, int h) {
      ByteObject figure = getFigure();
      g.drawFigure(figure, x, y, w, h);
   }

   public ByteObject getFigure() {
      return figure;
   }

   /**
    * Return true as long as the parameters of the ByteObject haven't changed
    */
   public boolean hasDrawableContentChanged() {
      ByteObject fig = getFigure();
      //same figure
      if (lastDrawVersion == fig.getVersion()) {
         return false;
      } else {
         lastDrawVersion = fig.getVersion();
         return true;
      }
   }

   /**
    * Nothing to init.
    * 
    * Why?
    * Because {@link Drawable#initSize()} did all the work already.
    * <br>
    * A Figure in a {@link FigDrawable} does not modify the dw and dh. Purely decided by sizers.
    * <br>
    * When a Figure has internal sizers and anchors, it will draw relative to dh and dw.
    */
   public void initDrawable(LayouterEngineDrawable ds) {
   }

   public void managePointerInput(InputConfig ic) {
      //#debug
      toDLog().pEvent1("x=" + ic.is.getX() + " y=" + ic.is.getY(), null, FigDrawable.class, "managePointerInput");
   }

   /**
    * Modifies the figure {@link IDrwTypes#TYPE_050_FIGURE}  of this {@link FigDrawable}
    * <br>
    * <br>
    * Invalidates cache.
    * 
    * Updates the  animation cache data because drawing state has changed.
    * <br>
    * <br>
    * @param p
    */
   public void setFigure(ByteObject p) {
      p.checkType(IBOTypesDrw.TYPE_050_FIGURE);
      if (this.figure != p) {
         this.figure = p;
      }
      //based on Figure Flag, enables caching
      if (figure.hasFlag(IBOFigure.FIG__OFFSET_03_FLAGP, IBOFigure.FIG_FLAGP_1_RGB) || figure.hasFlag(IBOFigure.FIG__OFFSET_02_FLAG, IBOFigure.FIG_FLAG_5_FILTER)) {
         //the figure is using rgb pixel manipulation, so it is wise to cache content. tip the mother class.
         setBehaviorFlag(ITechDrawable.BEHAVIOR_22_TIP_HEAVY_CONTENT, true);
      } else {
         this.setBehaviorFlag(ITechDrawable.BEHAVIOR_22_TIP_HEAVY_CONTENT, false);
      }
      //deals with animations on the figure
      if (figure.hasFlag(IBOFigure.FIG__OFFSET_02_FLAG, IBOFigure.FIG_FLAG_6_ANIMATED)) {
         ByteObject[] anims = figure.getSubs(IBOTypesGui.TYPE_130_ANIMATION);
         for (int i = 0; i < anims.length; i++) {
            ByteObject anim = anims[i];
            anim.setValue(IBOAnim.ANIM_OFFSET_06_TARGET1, ITechAnim.ANIM_TARGET_9_CONTENT, 1);
            IAnimable fanim = gc.getAnimFactory().createDrawableAnimable(anim, this);
            addFullAnimation(fanim);
         }
      }
      lastDrawVersion = 0;
   }

   public void setSourceCache(SourceCache sc, int dir) {
      this.sc = sc;
      this.dir = dir;
   }

   //#mdebug

   public void toString(Dctx dc) {
      dc.root(this, FigDrawable.class, 210);
      toStringPrivate(dc);
      super.toString(dc.sup());
      dc.nlLvlNullTitle("Figure", figure);
   }

   private void toStringPrivate(Dctx dc) {
      dc.appendVarWithSpace("lastDrawVersion", lastDrawVersion);
      dc.append(toStringDimension());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, FigDrawable.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());

      figure.toString1Line(dc);
   }

   //#enddebug

}
