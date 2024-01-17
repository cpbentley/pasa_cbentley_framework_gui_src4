package pasa.cbentley.framework.gui.src4.anim.definitions;

import java.util.Vector;

import pasa.cbentley.byteobjects.src4.objects.function.Function;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.engine.RgbImage;
import pasa.cbentley.framework.gui.src4.anim.Realisator;
import pasa.cbentley.framework.gui.src4.anim.base.DrawableAnim;
import pasa.cbentley.framework.gui.src4.anim.move.Move;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IAnimable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

/**
 * Draws an alpha trail to a moving {@link Drawable}
 * <br>
 * <br>
 * Composite animation of {@link Move} and {@link AlphaChangeRgb}.
 * <br>
 * <br>
 * At each Move frames, a new {@link AlphaChangeRgb} animation is created.
 * <br>
 * <br>
 * {@link AlphaTrail} precomputes the alpha frames and all {@link AlphaChangeRgb} simply draws<br>
 * If not enough memory is available, {@link AlphaChangeRgb} will compute on its own image copy<br>
 * <br>
 * <br>
 * <b>Implementation</b>:
 * <br>
 * <li>Host Drawable draws normally its full state
 * <li>creates a new DLayer with a Drawable hosting each fading image.
 * <br>
 * {@link AlphaTrail} controls the removal from {@link MasterCanvas}
 * <br>
 * @author Charles-Philip Bentley
 *
 */
public class AlphaTrail extends DrawableAnim {

   private Vector   af = new Vector();

   private Function alphaFunction;

   private RgbImage img;

   private Move     move;

   public AlphaTrail(GuiCtx gc, IDrawable d, Function f) {
      super(gc, d, f);
   }

   public void addXY(int x, int y) {

   }

   public int nextTurn() {
      move.nextTurn();
      //when data is shared, no need to use the AlphaChangeRgb class
      //TODO just keep everything in this class 
      //give shared move so anim Draws an index in the image cache
      //AlphaChangeRgb af = new AlphaChangeRgb(img, move.x, move.y, alphaFunction);
      //this.af.addElement(af);
      for (int i = 0; i < this.af.size(); i++) {
         AlphaChangeRgb afa = (AlphaChangeRgb) this.af.elementAt(i);
         afa.nextTurn();
      }
      for (int i = this.af.size() - 1; i >= 0; i--) {
         AlphaChangeRgb afa = (AlphaChangeRgb) this.af.elementAt(i);
         if (afa.hasAnimFlag(IAnimable.ANIM_13_STATE_FINISHED)) {
            this.af.removeElement(afa);
         }
      }
      if (move.isFinished() && this.af.size() == 0) {
         return -1;
      }
      return Realisator.DEF_SLEEP;
   }

   public void paint(GraphicsX g) {
      move.paint(g);
      for (int i = 0; i < this.af.size(); i++) {
         AlphaChangeRgb afa = (AlphaChangeRgb) this.af.elementAt(i);
         afa.paint(g);
      }
   }
   
   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "AlphaTrail");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {
      
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "AlphaTrail");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug
   


}
