package pasa.cbentley.framework.gui.src4.anim.base;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.framework.gui.src4.anim.definitions.AlphaChangeRgb;
import pasa.cbentley.framework.gui.src4.anim.definitions.AlphaTrail;
import pasa.cbentley.framework.gui.src4.anim.definitions.SizeMod;
import pasa.cbentley.framework.gui.src4.anim.move.Move;
import pasa.cbentley.framework.gui.src4.canvas.GraphicsXD;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IAnimable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

/**
 * Aggregate several animations.
 * <br>
 * Used by {@link Drawable} to merge together several Entry/Main/Exit animations.
 * 
 * <br>
 * <br>
 * Case of 4 style layers that move from outside the main canvas.
 * <br>
 * We want the 4 animations to be synchronized, same speed, same number of steps.
 * <br>
 * Clocks many animations using 
 * <br>
 * Probotector main page = A background image with many animated sprites.
 * <br>
 * <br>
 * For example
 * {@link AlphaTrail} is specific because it draws a trail.
 * <br>
 * Move is compatible with {@link AlphaChangeRgb}, {@link SizeMod}.
 * {@link AnimAggregate} manage the compatibility, the sharing of cache if possible(not with {@link SizeMod} but with {@link AlphaChangeRgb}
 * cache is shareable.
 * <br>
 * <br>
 * {@link Move} animation that moves endlessly within an area. Rebounds are computed by ?
 * @author Charles-Philip Bentley
 *
 */
public class AnimAggregate extends DrawableAnim {

   public IAnimable[] animations;

   public AnimAggregate(GuiCtx gc, IDrawable d, ByteObject tech) {
      super(gc, d, tech);
   }

   public int nextTurn() {
      for (int i = 0; i < animations.length; i++) {
         animations[i].nextTurn();
      }
      return getReturn();
   }

   public void paint(GraphicsXD g) {
      for (int i = 0; i < animations.length; i++) {
         animations[i].paint(g);
      }
   }

}
