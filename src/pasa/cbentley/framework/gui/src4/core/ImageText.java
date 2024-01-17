package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.framework.coredraw.src4.interfaces.IImage;
import pasa.cbentley.framework.coredraw.src4.interfaces.IMFont;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.string.StringDrawUtils;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;

/**
 * Draws Image and Text.
 * Image | text . several lines if Image height is big enough.
 * Text around image is draw at the bottom
 * 
 * Q:what happens when the image is too big for the width given?
 * A:width and height in excess are ignored. The image will have a 1 line value
 * for scrolling purposes
 * 
 * Q:What happens when the height of the image is not an increment factor?
 * A: Lines of text control the scrolling behavior
 * The image is associated with 1 or more lines. The image disappears with the lines
 * it is associated with
 * 
 * @author Mordan
 *
 */
public class ImageText extends ViewDrawable {

   private int[][][] breakText;

   private int[][][] subBreaks;

   /**
    * Pixels left on the side of the image
    */
   private int[]     imageLeftWidth;

   /**
    * The number of lines of text in that group
    */
   private int[]     numLines;

   public Object[]   os;

   public ImageText(GuiCtx gc, StyleClass styleKey, Object[] os) {
      super(gc, styleKey);
      this.os = os;
      breakText = new int[os.length][][];
      imageLeftWidth = new int[os.length];
      numLines = new int[os.length];
   }

   /**
    * Draw the chunks
    * Each image has a line value = the number of lines 
    * will always
    */
   public void drawViewDrawable(GraphicsX g) {
      int vx = getX();
      int vy = getY();
      int widthForData = getContentW();
      int anchor = GraphicsX.ANCHOR;
      IMFont f = null;
      int countlines = 0;
      ScrollConfig scY = null;
      //starts at increment
      int skip = scY.getSIStart();
      for (int i = 0; i < os.length; i++) {
         int maxLines = scY.getSIVisible();
         if (os[i] == null)
            continue;
         else if (os[i] instanceof String) {
            String text = (String) os[i];
            int[][] sets = breakText[i];
            int end = Math.min(maxLines, sets.length);
            int start = skip;
            int oldselx = vx;
            int oldsely = vy;

            for (int k = start; k < end; k++) {
               //oldselx += MStyle.getXAlign(style, dw, f.substringWidth(text, sets[i][0], sets[i][1]));
               g.drawSubstring(text, sets[i][0], sets[i][1], oldselx, oldsely, anchor);
               oldsely += f.getHeight();
               countlines++;
               skip--;
            }

         } else if (os[i] instanceof IImage) {
            if (skip != 0) {
               //the image must be drawn incompletely 
               int lines = subBreaks[i].length;
               if (lines <= skip) {
                  skip = skip - lines;
                  continue;
               }
               //else draw some of it
            }
            IImage my = (IImage) os[i];
            g.drawImage(my, vx, vy, anchor);
            if (subBreaks[i] != null) {
               int subx = vx + my.getWidth();
               int oldsely = vy;
               int oldselx = vx;
               int[][] sets = subBreaks[i];
               String text = (String) os[i + 1];
               for (int j = 0; j < sets.length; j++) {
                  g.drawSubstring(text, sets[j][0], sets[j][1], oldselx, oldsely, anchor);
                  oldsely += f.getHeight();
               }
            }
         }
      }
   }

   /**
    * Effectively initialize the class
    * @param style
    * @param width
    * @return
    */
   public int getPreferredHeightGivenWidth(int height, int width) {
      int h = height;
      int widthForData = width;
      Object previous = null;
      IMFont dataFont = null;
      for (int i = 0; i < os.length; i++) {
         if (os[i] == null)
            continue;
         else if (os[i] instanceof String) {
            String text = (String) os[i];
            if (previous instanceof IImage && imageLeftWidth[i - 1] > 0) {
               int widthLeft = imageLeftWidth[i - 1];
               //break the text in two.
               //1st part is draw in the region left by the image
               int[][] subBreak = StringDrawUtils.breakString(text.toCharArray(), 0, widthLeft, dataFont, numLines[i - 1]);
               int subBreakLen = 0;
               for (int j = 0; j < subBreak.length; j++) {
                  subBreakLen += subBreak[i][1];
               }
               subBreaks[i - 1] = subBreak;
               breakText[i] = StringDrawUtils.breakString(text.toCharArray(), subBreakLen, widthForData, dataFont, Integer.MAX_VALUE);
            } else {
               breakText[i] = StringDrawUtils.breakString(text, widthForData, dataFont);
               h += breakText[i].length * dataFont.getHeight();
               h += (breakText[i].length - 1);
            }
            numLines[i] = breakText[i].length;

         } else if (os[i] instanceof IImage) {
            IImage my = (IImage) os[i];
            int ih = my.getHeight();
            h += ih;
            //imageNumLines[i] = MUtils.div
            imageLeftWidth[i] = width - my.getWidth();
            numLines[i] = 1;
         }
         previous = os[i];
      }
      return h;
   }

}
