package pasa.cbentley.framework.gui.src4.anim.definitions;

import java.util.Random;

import pasa.cbentley.byteobjects.src4.objects.function.Function;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.drawx.src4.engine.RgbImage;
import pasa.cbentley.framework.gui.src4.anim.base.ImgAnimable;
import pasa.cbentley.framework.gui.src4.canvas.GraphicsXD;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

/**
 * 
 * @author Charles-Philip Bentley
 *
 */
public class PixelFalling extends ImgAnimable {

   int[]          add;

   private int    bh;

   int[]          buffer;

   private int    bw;

   private int    countFinish;

   int[]          fil;

   int[]          imgData;

   /**
    * Modified by the height of the image.
    * 
    */
   int            maxAdd = 5;

   /**
    * current move pixel for that columns
    */
   int[]          pix;

   int[]          pos;

   private Random r;

   public PixelFalling(GuiCtx gc, IDrawable d, Function f) {
      super(gc, d, f);
   }

   /**
    * Animates a LightWeight Drawable
    * @param img
    * @param x
    * @param y
    * @param f
    */
   public PixelFalling(GuiCtx gc,StyleClass sc, RgbImage img, int x, int y, Function f) {
      super(gc,sc, img, x, y, f);
   }

   public void paint(GraphicsXD g) {
      int x = d.getX();
      int y = d.getY();
      int[] data = img.getRgbData();
      if (data == null) {
         stepFunction.finish();
         return;
      }
      stepFallingPixels();
      g.drawRGB(buffer, 0, bw, x, y, bw, bh, true);
      step++;
      if (countFinish == bw) {
         stepFunction.finish();
      }
   }

   public void reset() {
      super.reset();
   }

   public void lifeStart() {
      r = gc.getUC().getRandom();
      bw = img.getWidth();
      bh = img.getHeight();
      buffer = new int[bw * bh];
      img.acquireLock(RgbImage.FLAG_07_READ_LOCK);
      imgData = img.getRgbData();
      pos = new int[bw]; //current position of falling pixel
      fil = new int[bw]; //number of pixels in the column
      pix = new int[bw];
      for (int i = 0; i < pix.length; i++) {
         pix[i] = imgData[((bh - 1) * bw) + i];
      }
      add = new int[bw];
      for (int i = 0; i < add.length; i++) {
         add[i] = r.nextInt(maxAdd) + min;
      }
   }

   private int min = 1;

   private void stepFallingPixels() {

      //for each line bring next step
      for (int i = 0; i < bw; i++) {
         //remove old pixel
         if (fil[i] != bh) {
            int pixelMove = add[i];
            buffer[i + (pos[i] * bw)] = 0;
            //we still have pixels
            if (pos[i] + pixelMove >= bh - fil[i]) {
               int lineoffset = (bh - fil[i] - 1) * bw; //line offset, 0 when fil[]
               int pixeloffset = lineoffset + i;
               //			if (i == 0) {
               //			   System.out.println(pixeloffset + " = " + buffer.length);
               //			}
               buffer[pixeloffset] = pix[i];
               pos[i] = 0;
               add[i] = r.nextInt(maxAdd) + min; //change the move amplitude
               fil[i]++; //if after increment we reach the top
               if (fil[i] == bh) {
                  countFinish++;
                  //acceleration of all
                  min++;
               } else {
                  //get next pixel
                  //			   if (i == 0) {
                  //				 System.out.println(i + (bh - fil[i] - 1) * bw);
                  //			   }
                  pix[i] = imgData[i + (bh - fil[i] - 1) * bw]; //next pixel

               }
            } else {
               int lineoffset = (pos[i] + pixelMove) * bw;
               //System.out.println((lineoffset + i) + " " + buffer.length);
               buffer[i + lineoffset] = pix[i];
               pos[i] += pixelMove;
            }
         }
      }

   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "PixelFalling");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {
      
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "PixelFalling");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug
   

}
