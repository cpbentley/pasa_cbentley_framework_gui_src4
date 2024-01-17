package pasa.cbentley.framework.gui.src4.anim.definitions;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.anim.base.DrawableAnim;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

/**
 * Animation that move several layers in a loop within a rectangular area.
 * <br>
 * <br>
 * using different speed for each layer
 * The x shift for each layer is a function of the previous layer.
 * The layer in the background is the layer moving the slower
 * <br>
 * Each sub layer has a different base y.
 * <br>
 * <br>
 * @author Charles-Philip Bentley
 *
 */
public class ParallaxScrolling extends DrawableAnim {
   public ParallaxScrolling(GuiCtx gc, IDrawable d, ByteObject tech) {
      super(gc, d, tech);
   }

   public void lifeEnd() {

   }

   public String toStringOneLine() {
      return "#ParallaxScrolling";
   }

   public void nextTurnSub() {
   }

   public void paint(GraphicsX g) {

   }

   public void lifeStart() {

   }


   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "ParallaxScrolling");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {
      
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "ParallaxScrolling");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug
   


}
