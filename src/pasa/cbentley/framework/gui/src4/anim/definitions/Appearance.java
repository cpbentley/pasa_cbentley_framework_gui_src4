package pasa.cbentley.framework.gui.src4.anim.definitions;

import pasa.cbentley.byteobjects.src4.objects.function.Function;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.drawx.src4.engine.RgbImage;
import pasa.cbentley.framework.gui.src4.anim.base.ImgAnimable;
import pasa.cbentley.framework.gui.src4.canvas.GraphicsXD;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

public class Appearance extends ImgAnimable {

   private int   bh;

   private int[] buffer;

   private int   bw;

   private int   step;

   /**
    * Increase when image is big
    */
   private int   stepPerTurn = 2;

   private int   type;

   /**
    * FUnction is automatically defined
    * @param d
    */
   public Appearance(GuiCtx gc, IDrawable d, Function f) {
      super(gc, d, f);
   }

   /**
    * Animates a LightWeight Drawable
    * @param img
    * @param x
    * @param y
    * @param f
    */
   public Appearance(GuiCtx gc, StyleClass sc, RgbImage img, int x, int y, Function f) {
      super(gc, sc, img, x, y, f);
   }

   private void doStep(int[] data) {
      switch (type) {
         case 0:
            if (step < bh) {
               System.arraycopy(data, step * img.getWidth(), buffer, step * img.getWidth(), img.getWidth());
            } else {
               stepFunction.finish();
            }
            break;
         case 1:
            if (step < bw) {
               int index = step;
               for (int i = 0; i < bh; i++) {
                  buffer[index] = data[index];
                  index += bw;
               }
            } else {
               stepFunction.finish();
            }
            break;
         case 2:
            if (step < bw) {
               int index = bw - step - 1;
               for (int i = 0; i < bh; i++) {
                  buffer[index] = data[index];
                  index += bw;
               }
            } else {
               stepFunction.finish();
            }
            break;
         case 3:
            if (step < bh) {
               System.arraycopy(data, (bh - step - 1) * img.getWidth(), buffer, (bh - step - 1) * img.getWidth(), img.getWidth());
            } else {
               stepFunction.finish();
            }
            break;
         case 4:
            if (step < bh) {
               System.arraycopy(data, step * img.getWidth(), buffer, step * img.getWidth(), img.getWidth());
            }
            if (step < bw) {
               int index = step;
               for (int i = 0; i < bh; i++) {
                  buffer[index] = data[index];
                  index += bw;
               }
            }
            if (step >= bh && step >= bw) {
               stepFunction.finish();
            }
            break;
         case 5:
            if (step < bw) {
               int index = step;
               for (int i = 0; i < step; i++) {
                  buffer[index] = data[index];
                  index += bw;
                  index--;
               }
               index = buffer.length - step;
               for (int i = 0; i < step; i++) {
                  buffer[index] = data[index];
                  index -= bw;
                  index++;
               }
            }
            break;
         case 6:
         default:
            break;
      }
   }

   /**
    * 
    */
   public void lifeStart() {

      bw = img.getWidth();
      bh = img.getHeight();

      buffer = new int[bw * bh];

      super.lifeStart();
   }

   public void paint(GraphicsXD g) {
      int[] data = img.getRgbData();
      if (data == null) {
         stepFunction.finish();
         return;
      }
      int x = d.getX();
      int y = d.getY();
      for (int i = 0; i < stepPerTurn; i++) {
         doStep(data);
      }

      g.drawRGB(buffer, 0, bw, x, y, bw, bh, true);
      step++;
   }

   public void setType(int type) {
      this.type = type;
   }


   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "Appearance");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {
      
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "Appearance");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug
   

}
