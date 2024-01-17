package pasa.cbentley.framework.gui.src4.string;

import pasa.cbentley.framework.coredraw.src4.interfaces.IGraphics;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.table.TableView;

/**
 * Draws on area a serie of numbers.
 * 
 * <b>Implementation Note</b> <br>
 * A {@link StringDrawable} with a single char, scaled to fit.
 * With a vertical scrollbar in overlay wrapper fill. No figure when not pressed, it has a figure
 * when selected.
 * For several number changes, use a {@link TableView}. <br>
 * 1 3 4
 * for the user to change
 * This serie represents 134.
 * 
 * 2 styles used
 * One for the component
 * One for each number box in the comp
 * Pointer event modifies a number
 * @author Mordan
 *
 */
public class NumChangeDrawable extends Drawable {

   public int     value;

   private int    nums = 0;

   private byte[] data = new byte[4];

   private int    currentIndex;

   public NumChangeDrawable(GuiCtx gc, StyleClass styleKey) {
      super(gc, styleKey);
   }

   public int getNumber() {
      String s = "";
      for (int i = 0; i < nums; i++) {
         s += data[i] & 0xFF;
      }
      return Integer.valueOf(s).intValue();
   }

   public void setNumber(int num) {

   }

   /**
    * Will scale content to match area
    */
   public void drawDrawableContent(IGraphics g, int x, int y, int w, int h) {
      if (nums > 0) {
         int wc = w / nums;

         for (int i = 0; i < nums; i++) {

         }
      }
   }

   public void navigateDown(InputConfig ic) {
      int c = (data[currentIndex] & 0xFF);
      if (c > 0)
         data[currentIndex] = (byte) (c - 1);
      else {
         data[currentIndex] = 9;
      }

   }

   public void navigateLeft(InputConfig ic) {
      currentIndex--;
      if (currentIndex < 0)
         currentIndex = 0;
   }

   public void navigateRight(InputConfig ic) {
      currentIndex++;
      if (currentIndex >= nums)
         currentIndex = nums - 1;
   }

   public void navigateSelect(InputConfig ic) {
      //notifies parent of this drawable that the number selection is finished
      //gets out of keyfocus
   }

   public void navigateUp(InputConfig ic) {
      data[currentIndex] = (byte) (((data[currentIndex] & 0xFF) + 1) % 10);
   }
}
