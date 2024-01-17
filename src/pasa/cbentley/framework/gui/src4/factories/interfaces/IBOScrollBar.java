package pasa.cbentley.framework.gui.src4.factories.interfaces;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.framework.gui.src4.core.ViewPane;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;

public interface IBOScrollBar extends IByteObject {

   public static final int MINIMUM_BLOCK_PIXEL_SIZE         = 5;

   /**
    * if set, this scrollbar is vertical
    */
   public static final int SB_FLAG_1_VERT                 = 1;

   /**
    * In wrapper mode, the x,y of Basicdrawable is the top/left item
    * Also centers scrollbar
    * If flag is not set, this is a block scrollbar bar on the side
    */
   public static final int SB_FLAG_3_WRAPPER              = 4;

   /**
    * If wrapper, takes all the room possible to draw.
    * possibly skewing the shape
    */
   public static final int SB_FLAG_4_WRAPPER_FILL         = 8;

   /**
    * Flag only relevant for block type. It will draw a directional figure at the
    * extremities of the block. <br>
    * Sequence is Draw Top Figure, Draw Block, Draw Bar, Draw Bottom Figure.
    */
   public static final int SB_FLAG_5_ARROW_ON_BLOC        = 16;

   /**
    * Flag so the vertical block scrollbar is shown on the left of viewport
    * instead of the right. top for an horizontal
    */
   public static final int SB_FLAG_6_BLOCK_INVERSE        = 32;

   /**
    * Allows the scrollbar to show 
    * <br>
    * When used in a {@link ViewPane}, this flag is slaved to ViewPane Tech.<br>
    * This flag is used when a scrollbar is used in stand alone slider.
    * Will set the {@link IDrawable} behavior 
    */
   public static final int SB_FLAG_7_AROUND_THE_CLOCK     = 64;

   /**
    * Single wrapper Style Class
    */
   public static final int SB_FLAG_8_SINGLE_STYLECLASS    = 128;

   /**
    * When set, show the number on the scrollbar
    */
   public static final int SB_FLAGX_1_SHOW_CONFIG         = 1;

   /**
    * Set when showing block bg
    */
   public static final int SB_FLAGX_3_SHOW_BLOCK_BG       = 4;

   public static final int SB_FLAGX_4_BLOCK_BG_SEPARATION = 8;

   /**
    * Minimum size is applied at all times.
    */
   public static final int SB_FLAGX_6_BLOCK_FIXED_SIZE    = 32;

   /**
    * When Block figure is a stand alone figure whose size is computed
    * from its anchor. usually as a percentage of 1st size. <br>
    * A triangle whose direction changes according to the scrolling config vector
    */
   public static final int SB_FLAGX_7_SHRINK_BLOCK        = 64;

   /**
    * 
    */
   public static final int SB_FLAGX_8_STAND_ALONE_INIT    = 128;

   /**
    * 4 bytes for second size
    * 4 bytes for third size
    * 4 bytes for min size
    */
   public static final int SB_BASIC_SIZE               = A_OBJECT_BASIC_SIZE + 19;

   public static final int SB_OFFSET_01_FLAG           = A_OBJECT_BASIC_SIZE;

   /**
    * 1 byte. 
    */
   public static final int SB_OFFSET_02_FLAGX          = A_OBJECT_BASIC_SIZE + 1;

   /**
    * <b>Block</b>
    * Primary size of a block bar
    * <li> width for vertical bar
    * <li> height for horizontal bar
    * <br>
    * <br>
    * <b>Wrapper</b> Value is ignored if Fill Wrapper:
    * <li>width for vertical wrapper bar. 
    * <li>height for horiztonal wrapper bar.
    */
   public static final int SB_OFFSET_03_PRIMARY_SIZE4  = A_OBJECT_BASIC_SIZE + 2;

   /**
    * <b>Block</b>: only used when {@link IBOScrollBar#SB_FLAG_5_ARROW_ON_BLOC}. Button.
    * <li> height of arrows for a vertical bar
    * <li> width of arrows for horizontal bar
    * <br>
    * <br>
    * <b>Wrapper</b> 
    * <li>height of each wrappers a vertical wrapper. 
    * <li>width of each wrapper for an horiztonal wrapper.
    */
   public static final int SB_OFFSET_04_SECOND_SIZE4   = A_OBJECT_BASIC_SIZE + 6;

   /**
    * Width of block bg
    */
   public static final int SB_OFFSET_05_THIRD_SIZE4    = A_OBJECT_BASIC_SIZE + 10;

   /**
    * Minimum size of block.
    * <br>
    * Or when 
    */
   public static final int SB_OFFSET_06_MIN_SIZE4      = A_OBJECT_BASIC_SIZE + 14;

   /**
    * 2 bits for scroll move type
    * 
    * <li> {@link ITechViewPane#SB_MOVE_TYPE_0_FIXED}
    */
   public static final int SB_OFFSET_07_SCROLL_TYPES1  = A_OBJECT_BASIC_SIZE + 18;

}
