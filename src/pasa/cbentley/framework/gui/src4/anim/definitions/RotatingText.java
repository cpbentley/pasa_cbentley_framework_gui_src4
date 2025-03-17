package pasa.cbentley.framework.gui.src4.anim.definitions;

import pasa.cbentley.byteobjects.src4.objects.function.Function;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.gui.src4.anim.base.DrawableAnim;
import pasa.cbentley.framework.gui.src4.canvas.GraphicsXD;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;

/**
 * To be implemented with DrwParamMod on Text Effects.
 * 
 * <br>
 * How does the {@link Drawable} computes its preferred size?
 * @author Charles-Philip Bentley
 *
 */
public class RotatingText extends DrawableAnim {

   int[]          bounds   = new int[4];

   int            maxX;

   int            maxY;

   int            position = 3;

   int            rootX;

   int            rootY;

   StringDrawable sd;

   int            speed    = 50;

   int            turn     = 0;

   int            xshift   = 0;

   boolean        xUp;

   int            yshift   = 0;

   boolean        yUp;

   public RotatingText(GuiCtx gc, StringDrawable sf) {
      super(gc, sf, (Function) null);
      sd = sf;
      maxX = maxY;
      position = 3;
      rootX = sf.getX();
      rootY = sf.getY();
   }

   public int[] getBounds() {
      bounds[0] = sd.getX();
      bounds[1] = sd.getY();
      bounds[2] = sd.getDrawnWidth();
      bounds[3] = sd.getDrawnHeight();
      return bounds;
   }

   public void lifeEnd() {

   }

   public void lifeStart() {

   }

   public int nextTurn() {
      if (position == 1) {
         if (Math.abs(yshift) == maxY) {
            xshift++;
         } else {
            yshift--;
         }
         if (xshift == 0) {
            position = 2;
         }
      } else if (position == 2) {
         if (Math.abs(xshift) == maxX) {
            yshift++;
         } else {
            xshift++;
         }
         if (yshift == 0)
            position = 3;
      } else if (position == 3) {
         if (Math.abs(yshift) == maxY) {
            xshift--;
         } else {
            yshift++;
         }
         if (xshift == 0) {
            position = 4;
         }
      } else if (position == 4) {
         if (Math.abs(xshift) == maxX) {
            yshift--;
         } else {
            xshift--;
         }
         if (yshift == 0)
            position = 1;
      }
      return speed;
   }

   public void paint(GraphicsXD g) {
      sd.draw(g);
   }


   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "RotatingText");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "RotatingText");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {
      
   }

   //#enddebug
   


}
