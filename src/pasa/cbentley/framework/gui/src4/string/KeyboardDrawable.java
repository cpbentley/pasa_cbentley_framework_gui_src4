package pasa.cbentley.framework.gui.src4.string;

import pasa.cbentley.core.src4.utils.IntUtils;
import pasa.cbentley.framework.coredraw.src4.interfaces.IGraphics;
import pasa.cbentley.framework.coredraw.src4.interfaces.IMFont;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.FigDrawable;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.table.TableView;

/**
 * String input Helper class.
 * <br>
 * <br>
 * 
 * Draws a first line of numbers
 * 1 2 3 4 5 6 7 8 9 0 <-
 * q w e r t y u i o p enter
 * ca a s d f g h j k l enter
 * MAJ z x c v b n m . MAJ
 * abc 123 SPACE , ? @
 * <br>
 * <br>
 * 
 * @author Mordan
 *
 */
public class KeyboardDrawable extends Drawable {

   private int           widthUnit;

   private int           heightUnit;

   private FigDrawable[] row1;

   private int[]         relativeXs;

   private int[]         relativeYs;

   private TableView     rowNums;

   private TableView     rowQwerty;

   private TableView     rowAsdfgh;

   private TableView     rowZxcvbn;

   public KeyboardDrawable(GuiCtx gc, StyleClass sc) {
      super(gc, sc);
   }

   /**
    * 1 or 2 pixels line separator with empty draw at cross sections
    * <br>
    * <br>
    * @param charSet
    */
   public void init(char[] charSet) {
      setDw(getVC().getWidth());
      setDh(getVC().getHeight());
      int workW = getDw() - getStyleWConsumed();
      widthUnit = IntUtils.divideFloor(workW, 11);

      //choose font according to width and character set
      IMFont f = gc.getCDC().getFontFactory().getDefaultFont();
      boolean ok = true;
      int padding = 0;
      for (int i = 0; i < charSet.length; i++) {
         if (f.charWidth(charSet[i]) + padding > widthUnit) {
            ok = false;
            break;
         }
      }
      //if too big a font, try a smaller one
      //in the end take the smallest if none works

      heightUnit = f.getHeight();

      int[][] widths = new int[5][11];
      char leftChar = (char) ((33 << 8) + 144 << 0);

      char[] row1 = new char[] { 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', leftChar };
   }

   /**
    * Strategy to draw
    * <br>
    * Table with all set values
    * @param g
    */
   public void paint(IGraphics g) {

   }

   /**
    * Call Back for DownCmd
    *
    */
   public void navigateDown(ExecutionContextCanvasGui ec) {

   }

   /**
    * Called only if {@link ITechDrawable#BEHAVIOR_27_NAV_HORIZONTAL} is true.
    * <br>
    * First call in cycle with {@link InputConfig#isFirstNavCycle()}
    * <br>
    * Second call, implements any second cycle stuff like cycles or other stuff.
    * @param ic
    */
   public void navigateLeft(ExecutionContextCanvasGui ec) {

   }

   /**
    * Implement to the right horizontal traversal
    * @param ic
    */
   public void navigateRight(ExecutionContextCanvasGui ec) {

   }

   /**
    * 
    * @param ic
    */
   public void navigateSelect(ExecutionContextCanvasGui ec) {

   }

}
