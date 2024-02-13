package pasa.cbentley.framework.gui.src4.creator;

import pasa.cbentley.core.src4.utils.interfaces.IColors;
import pasa.cbentley.core.src4.utils.interfaces.IColorsBase;
import pasa.cbentley.core.src4.utils.interfaces.IColorsHTML;

/**
 * An array of Ints with Pointers.
 * <br>
 * {@link MyColors} can be described with Strings to the user.
 * <br>
 * Each color is configurable.
 * <br>
 * Each style class can have its colors,
 * <br>
 * Hierarchy in colors
 * @author Charles Bentley
 *
 */
public class MyColors implements IColors, IColorsBase, IColorsHTML, IColorsKey {

   public MyColors(int[] colors) {
      this.colors = colors;
   }

   /**
    * 
    * @param colorData
    */
   public MyColors(byte[] colorData) {
      //array starts with root. each class has an ID. StyleManager can put a name on the ID
      //for each style class, read num colors
      //each class use its own colors or use parent or finally root
   }

   private int[] colors;

   public int    fontColor         = FULLY_OPAQUE_BEIGE;

   public int    scrollBarFigure   = FULLY_OPAQUE_GREEN;

   public int    scrollBar3rdColor = FULLY_OPAQUE_ORANGE;

   public int    scrollBar2ndColor = FULLY_OPAQUE_PURPLE;

   public int    border            = FULLY_OPAQUE_YELLOW;

   public int    border2ndColor    = FULLY_OPAQUE_PINK;

   public int    border3rdColor    = FULLY_OPAQUE_SKY_BLUE;

   public int    bg                = FULLY_OPAQUE_RED;

   public int getColor(int key) {
      return colors[key];
   }



}
