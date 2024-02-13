package pasa.cbentley.framework.gui.src4.factories.interfaces;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.framework.gui.src4.core.ScrollBar;
import pasa.cbentley.framework.gui.src4.core.ViewDrawable;
import pasa.cbentley.framework.gui.src4.core.ViewPane;
import pasa.cbentley.framework.gui.src4.interfaces.ITechViewDrawable;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;

public interface IBOViewPane extends IByteObject {

   /**
    * <li>1 byte for 	
    * 
    */
   public static final int VP_BASIC_SIZE                         = A_OBJECT_BASIC_SIZE + 14;

   /**
    * When set, the style class of scrollbars and holes are read from the viewdrawable
    * style class.
    */
   public static final int VP_FLAG_1_DATA_FROM_VIEW              = 1 << 0;

   /**
    * Scrollbars are always visible, even when scrolling is not needed. of course 
    * they are not actionnable.
    * <li> {@link IBOViewPane#VP_FLAGY_5_SB_INVISIBLE_HORIZ} will hide it though.
    * <li> {@link IBOViewPane#VP_FLAGY_6_SB_INVISIBLE_VERT} will hide it though.
    * 
    */
   public static final int VP_FLAG_2_SCROLLBAR_ALWAYS            = 1 << 1;

   /**
    * When true, Scrollbars are masters over Headers. 
    * <br>
    * When in {@link PLANET_STRUCT_2OVERLAY} mode,
    * they are drawn over the headers and ViewPort. <br>
    * By default the scrollbars are slaved to headers dimension wise. <br>
    * In all cases, competing Scrollbars are neutral
    */
   public static final int VP_FLAG_3_SCROLLBAR_MASTER            = 1 << 2;

   /**
    * Competing overlay header and overlay scrollbar by default do a coexistence. 
    * <br>
    * When this flag is set, the master is drawn over the slave. 
    * <br>
    * Both are drawn over the ViewPort
    * <br>
    */
   public static final int VP_FLAG_4_MASTER_OVERLAY              = 1 << 3;

   public static final int VP_FLAG_5_                            = 1 << 4;

   /**
    * <b>Competion Overlay</b> is done for headers, master is defined by.
    * <br>
    * <li> {@link ITechViewPane#COMPET_HEADER_1_HORIZ}
    * <li> {@link ITechViewPane#COMPET_HEADER_1_HORIZ}
    * <li> {@link ITechViewPane#COMPET_HEADER_2_VERTICAL}
    * <li> {@link ITechViewPane#COMPET_HEADER_4_OVERLAY_VERT}
    * <li> {@link ITechViewPane#COMPET_HEADER_3_WHEEL}
    * <li> {@link ITechViewPane#COMPET_HEADER_4_WHEEL_CCW}
    * 
    */
   public static final int VP_FLAG_6_COMPET_OVERLAY_HEADER       = 1 << 5;

   /**
    * Undefined in neutral
    */
   public static final int VP_FLAG_7_COMPET_OVERLAY_SB           = 1 << 6;

   /**
    * ViewPane and its scrollbars are made totally invisible when not activated.
    * <br>
    * <br>
    * Activation:
    * <li>Key scrolling
    * <li>Mouse Over
    * <li>Mouse Press when no mouse movement is available (touch screens)
    */
   public static final int VP_FLAG_8_OVERLAY_INVISIBLE           = 1 << 7;

   /**
    *  Switch for applying a style at the {@link ViewPane} x,y,w,h.
    *  that is Margin,Padding,Border applied around Headers and scrollbars.
    *  
    * <p>
    * The style used is defined by {@link IBOViewPane#VP_OFFSET_13_STYLE_VIEWPANE_MODE1}
    * </p>
    */
   public static final int VP_FLAGX_1_STYLE_VIEWPANE             = 1 << 0;

   /**
    * Switch for applying a style on the Viewport area, i.e the area between content and headers/scrollbars
    * <p>
    * The style used is defined by {@link IBOViewPane#VP_OFFSET_14_STYLE_VIEWPORT_MODE1}
    * </p>
    */
   public static final int VP_FLAGX_2_STYLE_VIEWPORT             = 1 << 1;

   /**
    * Flag for drawing the style of the {@link ViewDrawable} as content inside the ViewPane
    * 
    * <p>
    * The style used is always the ViewDrawable's style.
    * </p>
    * 
    * There are no style selectors as for ViewPane and ViewPort.
    * 
    * <p>
    * TODO there might be an issue with logic scrolling. Investigate.
    * tyle is shown on first logic, then disappears.
    * style is shown on last logic if possible 
    * 
    * </p>
    */
   public static final int VP_FLAGX_3_STYLE_CONTENT              = 1 << 2;

   public static final int VP_FLAGX_4_                           = 1 << 3;

   /**
    * Both vertical and horizontal take the hole area. 
    * <br>
    * Two block bar will overlay each other.
    * The one set by flag {@link ITechViewPane#VP_FLAGX_4VERTICAL_WINS} is drawn last.
    */
   public static final int VP_FLAGX_5_                           = 1 << 4;

   /**
    */
   public static final int VP_FLAGX_6_                           = 1 << 5;

   /**
    * Moves several pixel increments smoothly.
    * Animated scrolling content for 
    * <br>
    * <b>Logic</b> : {@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT} and {@link ITechViewPane#SCROLL_TYPE_2_PAGE_UNIT}
    * <br>
    * The animation occurs in all cases. Both on the {@link ScrollBar} and with inside navigation.
    * <br>
    * When a number of increments are moved
    * <br>
    * <b>Pixel</b> {@link ITechViewPane#SCROLL_TYPE_0_PIXEL_UNIT} 
    * <br>
    * <br>
    */
   public static final int VP_FLAGX_7_ANIMATED                   = 1 << 6;

   /**
    * Works when a {@link ViewDrawable} has a {@link ViewPane}
    * When this flag is set, the 
    * <li>{@link ViewDrawable#getX()}
    * <li>{@link ViewDrawable#getY()}
    * <li>{@link ViewDrawable#setXY(int, int)}
    * <li>{@link ViewDrawable#getDrawnWidth()}
    * <li>{@link ViewDrawable#getDrawnHeight()}
    * <br>
    * apply on the ViewDrawable. 
    * <br>
    * For animations don't forget to remove the flag once the animation is finished.
    */
   public static final int VP_FLAGX_8_COUPLED_CONTENT            = 1 << 7;

   /**
    * When left or right header is in overlay mode and Horizontal Master mode, 
    * this flag will diminish top and botton headers's width
    */
   public static final int VP_FLAGY_1_SHORTEN_HEADER_WIDTH       = 1 << 0;

   /**
    * When Bottom or Top header is in overlay mode and Horizontal Master mode, 
    * this flag will diminish Left and Right headers's height
    */
   public static final int VP_FLAGY_2_SHORTEN_HEADER_HEIGHT      = 1 << 1;

   public static final int VP_FLAGY_3_SHORTEN_SB_WIDTH           = 1 << 2;

   /**
    * When bottom and top scrollabr is in overlay mode, this flag will diminish height consuming scrollbar
    */
   public static final int VP_FLAGY_4_SHORTEN_SB_HEIGHT          = 1 << 3;

   /**
    * Horizontal scrollbar is fully invisible
    */
   public static final int VP_FLAGY_5_SB_INVISIBLE_HORIZ         = 1 << 4;

   /**
    * Vertical scrollbar is fully invisible.
    * <br>
    * Subtle difference with {@link ITechViewPane#PLANET_MODE_3_IMMATERIAL}.
    * <br>
    */
   public static final int VP_FLAGY_6_SB_INVISIBLE_VERT          = 1 << 5;

   /**
    * Scrollbars are always visible, even when scrolling is not needed. of course 
    * they are not actionnable.
    * <li> {@link IBOViewPane#VP_FLAGY_5_SB_INVISIBLE_HORIZ} will hide it though.
    * <li> {@link IBOViewPane#VP_FLAGY_6_SB_INVISIBLE_VERT} will hide it though.
    * 
    */
   public static final int VP_FLAGY_7_SB_ALWAYS_HORIZ            = 1 << 6;

   /**
    * Scrollbars are always visible, even when scrolling is not needed. of course 
    * they are not actionnable.
    * <li> {@link IBOViewPane#VP_FLAGY_6_SB_INVISIBLE_VERT} will hide it though.
    * 
    */
   public static final int VP_FLAGY_8_SB_ALWAYS_VERT             = 1 << 7;

   /**
    * 
    */
   public static final int VP_OFFSET_01_FLAG                     = A_OBJECT_BASIC_SIZE;

   /**
    * 
    */
   public static final int VP_OFFSET_02_FLAGX                    = A_OBJECT_BASIC_SIZE + 1;

   /**
    * 
    */
   public static final int VP_OFFSET_03_FLAGY                    = A_OBJECT_BASIC_SIZE + 2;

   /**
    * Planet Mode 
    * <li>{@link ITechViewPane#PLANET_MODE_0_EAT}
    * <li>{@link ITechViewPane#PLANET_MODE_1_EXPAND}
    * <li>{@link ITechViewPane#PLANET_MODE_2_OVERLAY}
    * <li>{@link ITechViewPane#PLANET_MODE_3_IMMATERIAL}
    * <br>
    * <b>2 bits 1</b> for Top mode <br>
    * <b>2 bits 2</b> for Bot mode <br>
    * <b>2 bits 3</b> for Left mode <br>
    * <b>2 bits 4</b> for Right mode <br>
    * 
    */
   public static final int VP_OFFSET_04_HEADER_PLANET_MODE1      = A_OBJECT_BASIC_SIZE + 3;

   /**
    * Planetary Mode and Scroll Type data field.
    * 
    * <p>
    * <li><b>2 bits 1</b> decide for scrollbars' horizontal pixels mode X (eat/expand/overlay) 
    * <li><b>2 bits 2</b> decide for scrollbars' vertical pixels mode Y (eat/expand/overlay) 
    * </p>
    * 
    * <p>
    * 
    * ViewPane cannot know if horizontal scrollbar consumes pixels vertically or horizontally.
    * Thus mode applies axis of pixels
    * </p>
    * 
    * <li>{@link ITechViewPane#PLANET_MODE_0_EAT}
    * <li>{@link ITechViewPane#PLANET_MODE_1_EXPAND}
    * <li>{@link ITechViewPane#PLANET_MODE_2_OVERLAY}
    * <li>{@link ITechViewPane#PLANET_MODE_3_IMMATERIAL}
    * 

    * 
    * <p>
    * 
    * Scrolling Type = pixel/unit/page  
    * <li><b>2 bits 3</b> for h scrolling type 
    * <li><b>2 bits 4</b> for v scrolling type 
    *  
    * </p>
    * <li>{@link ITechViewPane#SCROLL_TYPE_0_PIXEL_UNIT}
    * <li>{@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT}
    * <li>{@link ITechViewPane#SCROLL_TYPE_2_PAGE_UNIT}
    * 
    */
   public static final int VP_OFFSET_05_SCROLLBAR_MODE1          = A_OBJECT_BASIC_SIZE + 4;

   /**
    * How to manage visual left over in scrolling type {@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT}.
    * <br>
    * <br>
    * <b>4 bits 1</b> for width<br>
    * <b>4 bits 2</b> for height<br>
    * <br>
    * Choice between:
    * <li>{@link ITechViewPane#VISUAL_0_LEAVE}
    * <li>{@link ITechViewPane#VISUAL_1_PARTIAL}
    * <li>{@link ITechViewPane#VISUAL_2_SHRINK}
    * <li>{@link ITechViewPane#VISUAL_3_FILL}
    * <br>
    * <br>
    * This should not be confused with Shrinking flags {@link ITechViewDrawable#FLAG_GENE_29_SHRINKABLE_W}.
    * <br>
    * This {@link IBOViewPane#VP_OFFSET_06_VISUAL_LEFT_OVER1} works when the ViewPane is scrolling the {@link ViewDrawable}
    * and some scrolling increments currenly shown do not need the whole ViewPort area.
    * <br>
    * As stated above, this only happens with {@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT}.
    */
   public static final int VP_OFFSET_06_VISUAL_LEFT_OVER1        = A_OBJECT_BASIC_SIZE + 5;

   public static final int VP_OFFSET_07_1                        = A_OBJECT_BASIC_SIZE + 6;

   /**
    * Decides how to move scrolling content.
    * <br>
    * <br>
    * <b>Move type</b>:
    * <li>{@link ITechViewPane#SB_MOVE_TYPE_0_FIXED}
    * <li>{@link ITechViewPane#SB_MOVE_TYPE_1_CLOCK}
    * <li>{@link ITechViewPane#SB_MOVE_TYPE_2_CIRCULAR}
    * <br>
    * <br>
    * <b>Partial type</b>:
    * <li> {@link ITechViewPane#PARTIAL_TYPE_0_BOTH}
    * <li> {@link ITechViewPane#PARTIAL_TYPE_1_TOP}
    * <li> {@link ITechViewPane#PARTIAL_TYPE_2_BOTTOM}
    * <br>
    * <br>
    * <li>2 bits : horizontal move type 
    * <li>2 bits : horizontal partial type 
    * <li>2 bits : vertical move type 
    * <li>2 bits : vertical partial type 
    */
   public static final int VP_OFFSET_08_MOVE_TYPE1               = A_OBJECT_BASIC_SIZE + 7;

   /**
    * Define competition type between headers and scrollbars.
    * <li> {@link ITechViewPane#COMPET_SB_0_NEUTRAL} 
    * <li>Horizontal wins
    * <li>Vertical Wins
    * <li>Equal Merge with a blending of common area. Expensive.
    */
   public static final int VP_OFFSET_09_COMPETITION_TYPE1        = A_OBJECT_BASIC_SIZE + 8;

   /**
    *  <li> {@link ITechViewPane#COMPET_SB_0_NEUTRAL} 
    *  <li> {@link ITechViewPane#COMPET_SB_1_HORIZONTAL} 
    *  <li> {@link ITechViewPane#COMPET_SB_2_VERTICAL} 
    *  <li> {@link ITechViewPane#COMPET_SB_3_OVERLAY} 
    */
   public static final int VP_OFFSET_10_COMPETITION_SB_TYPE1     = A_OBJECT_BASIC_SIZE + 9;

   /**
    * How are the Headers layouted
    * <li> {@link ITechViewPane#COMPET_HEADER_0_NEUTRAL}
    * <li> {@link ITechViewPane#COMPET_HEADER_1_HORIZ}
    * <li> {@link ITechViewPane#COMPET_HEADER_2_VERTICAL}
    * <li> {@link ITechViewPane#COMPET_HEADER_3_WHEEL}
    * <li> {@link ITechViewPane#COMPET_HEADER_4_WHEEL_CCW}
    */
   public static final int VP_OFFSET_11_COMPETITION_HEADER_TYPE1 = A_OBJECT_BASIC_SIZE + 10;

   /**
    * One flag for each element, telling us if it is present.
    * 
    * <li> {@link ITechViewPane#VP_SIZER_1_TOP}
    * <li> {@link ITechViewPane#VP_SIZER_2_BOT}
    * <li> {@link ITechViewPane#VP_SIZER_3_LEFT}
    * <li> {@link ITechViewPane#VP_SIZER_4_RIGHT}
    * <li> {@link ITechViewPane#VP_SIZER_5_TOP_CLOSE}
    * <li> {@link ITechViewPane#VP_SIZER_6_BOT_CLOSE}
    * <li> {@link ITechViewPane#VP_SIZER_7_LEFT_CLOSE}
    * <li> {@link ITechViewPane#VP_SIZER_8_RIGHT_CLOSE}
    */
   public static final int VP_OFFSET_12_INTERNAL_SIZING1         = A_OBJECT_BASIC_SIZE + 11;

   /**
    * Decides which style to use for the {@link ViewPane} when flag {@link IBOViewPane#VP_FLAGX_1_STYLE_VIEWPANE} is true.
    * 
    * <li>{@link ITechViewPane#DRW_STYLE_0_VIEWDRAWABLE}
    * <li>{@link ITechViewPane#DRW_STYLE_1_VIEWPANE}
    * <li>{@link ITechViewPane#DRW_STYLE_2_VIEWPORT}
    */
   public static final int VP_OFFSET_13_STYLE_VIEWPANE_MODE1     = A_OBJECT_BASIC_SIZE + 12;

   /**
    * Decides which style to use for the {@link ViewPane} when flag is true {@link IBOViewPane#VP_FLAGX_2_STYLE_VIEWPORT}.
    * <br>
    * <li>{@link ITechViewPane#DRW_STYLE_0_VIEWDRAWABLE}
    * <li>{@link ITechViewPane#DRW_STYLE_1_VIEWPANE}
    * <li>{@link ITechViewPane#DRW_STYLE_2_VIEWPORT}
    */
   public static final int VP_OFFSET_14_STYLE_VIEWPORT_MODE1     = A_OBJECT_BASIC_SIZE + 13;

}
