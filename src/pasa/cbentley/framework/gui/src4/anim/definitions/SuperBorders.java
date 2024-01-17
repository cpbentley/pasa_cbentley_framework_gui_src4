package pasa.cbentley.framework.gui.src4.anim.definitions;

import pasa.cbentley.byteobjects.src4.objects.function.Function;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.anim.base.DrawableAnim;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

/**
 * Implements different types of Border animation on a IDrawable
 * The IDrawable content is not repainted.
 * @author Charles-Philip Bentley
 *
 */
public class SuperBorders extends DrawableAnim {
   public static final int COLOR_EVOLVE        = 1;

   public static final int CREEPING_EFFECT     = 1;

   public static final int FLASHING            = 1;

   public static final int GLOWING             = 1;

   public static final int GLOWING_BALLS_COINS = 1;

   public static final int SERPENTINE          = 1;

   /**
    * Animate the border of the Drawable using the animation type
    * Some effects can be combined
    * @param d
    * @param type
    */
   public SuperBorders(GuiCtx gc, IDrawable d, int type) {
      super(gc, d, (Function) null);
   }

   public void lifeEnd() {

   }

   public void lifeStart() {
   }

   public void nextTurnSub() {

   }

   public void paint(GraphicsX g) {

   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "SuperBorders");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "SuperBorders");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

}
