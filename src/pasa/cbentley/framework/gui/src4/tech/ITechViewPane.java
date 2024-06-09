package pasa.cbentley.framework.gui.src4.tech;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.ImageDrawable;
import pasa.cbentley.framework.gui.src4.core.ScrollBar;
import pasa.cbentley.framework.gui.src4.core.ScrollConfig;
import pasa.cbentley.framework.gui.src4.core.ViewDrawable;
import pasa.cbentley.framework.gui.src4.core.ViewPane;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOViewPane;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;
import pasa.cbentley.framework.gui.src4.table.TableView;

/**
 * {@link ViewPane} tech flags for {@link IBOViewPane}
 * 
 * @author Charles-Philip Bentley
 *
 */
public interface ITechViewPane extends IByteObject {
   /**
    * In Neutral Mode, each Headers take the ViewPort dimension.
    * <br>
    * <br>
    * This creates 4 holes.
    */
   public static final int COMPET_HEADER_0_NEUTRAL       = 0;

   /**
    * Top/Bot horizontal headers take the most area.
    * <br>
    * Therefore they are initialized after Left/Right
    */
   public static final int COMPET_HEADER_1_HORIZ         = 1;

   /**
    * Left/Right vertical headers take the most area.
    * <br>
    * Therefore they are initialized after Top/Bot
    */
   public static final int COMPET_HEADER_2_VERTICAL      = 2;

   /**
    * 
    * <li>top takes all minus right
    * <li>right takes all minus bot
    * <li>bot takes all mines left
    * <li>left takes all minus top
    */
   public static final int COMPET_HEADER_3_WHEEL         = 3;

   /**
    * <li>top takes all minus left
    * <li>left takes all minus bot
    * <li>bot takes all mines right
    * <li>right takes all minus top
    */
   public static final int COMPET_HEADER_4_WHEEL_CCW     = 4;

   /**
    * Default behavior that creates a sb hole
    */
   public static final int COMPET_SB_0_NEUTRAL           = 0;

   /**
    * Horiztonal sb takes area
    */
   public static final int COMPET_SB_1_HORIZONTAL        = 1;

   /**
    * Vertical sb takes area
    */
   public static final int COMPET_SB_2_VERTICAL          = 2;

   /**
    * Last active is on top
    */
   public static final int COMPET_SB_3_OVERLAY           = 3;

   /**
    * Drawable style is drawn at ViewPort.
    * <br>
    * <br>
    * Scrolling occurs only on the content of the {@link Drawable}.
    * <br>
    * Computation of {@link ScrollConfig} is done the preferred content size.
    * <br>
    * Contextual Etalon is VP, ViewPort
    * <br>
    * <li>{@link IBOViewPane#VP_FLAGX_1_STYLE_VIEWPANE} applies normally and decides {@link ViewDrawable} opaqueness
    * 
    * <p>
    * 
    * Stored at one of theses offsets:
    * 
    * <li>{@link IBOViewPane#VP_OFFSET_13_STYLE_VIEWPANE_MODE1}
    * <li>{@link IBOViewPane#VP_OFFSET_14_STYLE_VIEWPORT_MODE1}
    * 
    * </p>
    */
   public static final int DRW_STYLE_0_VIEWDRAWABLE      = 0;

   /**
    * Apply style of {@link ViewDrawable} to the {@link ViewPane} dimensions.
    * <br>
    * Only if the flag The ViewPane's style is never used.
    * Flag {@link IBOViewPane#VP_FLAGX_1_STYLE_VIEWPANE} is ignored.
    */
   public static final int DRW_STYLE_1_VIEWPANE          = 1;

   /**
    * Special style defined for the ViewPort.
    * <br>
    * If not defined, will use ViewPane's style but log a Dev Warning.
    * 
    * Uses the {@link ITechLinks#LINK_64_STYLE_VIEWPORT} for styling.
    */
   public static final int DRW_STYLE_2_VIEWPORT          = 2;

   /**
    * Override the flag and ignore the style
    */
   public static final int DRW_STYLE_3_IGNORED           = 3;

   public static final int DRW_STYLE_CK_MAX              = 3;

   /**
    * Visible increment in {@link ScrollConfig} depends on direction.
    * <br>
    * <br>
    * Relevant in {@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT} with {@link ITechViewPane#VISUAL_1_PARTIAL}.
    */
   public static final int PARTIAL_TYPE_0_BOTH           = 0;

   /**
    * First visible increment (top) is always fully visible. Last visible increment is always partial.
    * <br>
    * <br>
    * A blank space is drawn when last increment is reached and no partially visible unit is shown. 
    * <br>
    * <br>
    * {@link ScrollConfig}'s rootSI is always startSI.
    * <br>
    * <br>
    * Relevant in {@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT} with {@link ITechViewPane#VISUAL_1_PARTIAL}.
    * <br>
    * <br>
    * Opposite of {@link ITechViewPane#PARTIAL_TYPE_2_BOTTOM}.
    */
   public static final int PARTIAL_TYPE_1_TOP            = 1;

   /**
    * Last visible increment is always fully visible. First visible increment is always partial.
    * <br>
    * Opposite of {@link ITechViewPane#PARTIAL_TYPE_1_TOP}.
    */
   public static final int PARTIAL_TYPE_2_BOTTOM         = 2;

   /**
    * Header reduces the ViewPort dimension. The Drawable dimension is not changed.
    * Default mode, used for Drawables initialized with virtual canvas dimension. (cannot grow).
    * <br>
    * {@link ScrollBar} pixels on the axis (width or height) eats into the ViewPort.
    */
   public static final int PLANET_MODE_0_EAT             = 0;

   /**
    * Header will expand the Drawable dimension. ViewPort is unchanged.
    * <br>
    * Using this setting may make the Drawable not entirely visible.
    * <br>
    * <br>
    * Expansion is up to {@link ViewDrawable} maximum size.
    */
   public static final int PLANET_MODE_1_EXPAND          = 1;

   /**
    * Planet will be drawn over the viewport and slaved planetaries. 
    * <br>
    * An overlay planet does not change ViewPort dimension.
    * <br>
    * TODO how to make them invisible when pointer or nav key is not using scrollbar?
    * A Twist with overlay is
    * <li>{@link IBOViewPane#VP_FLAG_4_MASTER_OVERLAY}
    * <li>{@link IBOViewPane#VP_FLAG_6_COMPET_OVERLAY_HEADER}
    * <li>{@link IBOViewPane#VP_FLAG_7_COMPET_OVERLAY_SB}
    */
   public static final int PLANET_MODE_2_OVERLAY         = 2;

   /**
    * Sattelite is a ghost and never drawn and not taken into account for layouting.
    * <br>
    * May still provide functionality. i.e. {@link ScrollBar}.
    * <br>
    * This goes well 
    * In Practive, set {@link ITechDrawable#BEHAVIOR_16_IMMATERIAL} to Satellite using EAT mode.
    * <br>
    * Set on a given {@link ScrollBar}. not on the width consumed.
    * <br>
    * Pixels on that axis are invisible. 
    * <br>
    * What if a {@link ScrollBar} is using both vertical and horizontal axis? Deal with it.
    * <br>
    */
   public static final int PLANET_MODE_3_IMMATERIAL      = 3;

   public static final int SAT_00_V_SB                   = 0;

   public static final int SAT_01_H_SB                   = 1;

   public static final int SAT_02_TOP                    = 2;

   public static final int SAT_03_TOP_CLOSE              = 3;

   public static final int SAT_04_BOT                    = 4;

   public static final int SAT_05_BOT_CLOSE              = 5;

   public static final int SAT_06_LEFT                   = 6;

   public static final int SAT_07_LEFT_CLOSE             = 7;

   public static final int SAT_08_RIGHT                  = 8;

   public static final int SAT_09_RIGHT_CLOSE            = 9;

   public static final int SAT_11_SB_HOLE_TOP_LEFT       = 11;

   public static final int SAT_12_SB_HOLE_TOP_RIGHT      = 12;

   public static final int SAT_13_SB_HOLE_BOT_LEFT       = 13;

   public static final int SAT_14_SB_HOLE_BOT_RIGHT      = 14;

   public static final int SAT_15_H_HOLE_TOP_LEFT        = 15;

   public static final int SAT_16_H_HOLE_TOP_RIGHT       = 16;

   public static final int SAT_17_H_HOLE_BOT_LEFT        = 17;

   public static final int SAT_18_H_HOLE_BOT_RIGHT       = 18;

   public static final int SAT_FLAG_00_V_SB              = 1 << 0;

   public static final int SAT_FLAG_01_H_SB              = 1 << 1;

   public static final int SAT_FLAG_02_TOP               = 1 << 2;

   public static final int SAT_FLAG_03_TOP_CLOSE         = 1 << 3;

   public static final int SAT_FLAG_04_BOT               = 1 << 4;

   public static final int SAT_FLAG_05_BOT_CLOSE         = 1 << 5;

   public static final int SAT_FLAG_06_LEFT              = 1 << 6;

   public static final int SAT_FLAG_07_LEFT_CLOSE        = 1 << 7;

   public static final int SAT_FLAG_08_RIGHT             = 1 << 8;

   public static final int SAT_FLAG_09_RIGHT_CLOSE       = 1 << 9;

   public static final int SAT_FLAG_11_SB_HOLE_TOP_LEFT  = 1 << 11;

   public static final int SAT_FLAG_12_SB_HOLE_TOP_RIGHT = 1 << 12;

   public static final int SAT_FLAG_13_SB_HOLE_BOT_LEFT  = 1 << 13;

   public static final int SAT_FLAG_14_SB_HOLE_BOT_RIGHT = 1 << 14;

   public static final int SAT_FLAG_15_H_HOLE_TOP_LEFT   = 1 << 15;

   public static final int SAT_FLAG_16_H_HOLE_TOP_RIGHT  = 1 << 16;

   public static final int SAT_FLAG_17_H_HOLE_BOT_LEFT   = 1 << 17;

   public static final int SAT_FLAG_18_H_HOLE_BOT_RIGHT  = 1 << 18;

   public static final int SAT_MAX_NUM                   = 18;

   public static final int SAT_FLAG_XX_NONE              = 0;

   /**
    * Start increment stops at zero and upper boundary
    */
   int                     SB_MOVE_TYPE_0_FIXED          = 0;

   /**
    * 
    */
   int                     SB_MOVE_TYPE_1_CLOCK          = 1;

   /**
    * Start increment shows
    */
   int                     SB_MOVE_TYPE_2_CIRCULAR       = 2;

   /**
    * Unit of scrolling is the pixel
    */
   public static final int SCROLL_TYPE_0_PIXEL_UNIT      = 0;

   /**
    * Unit of scrolling is a depends of {@link ViewDrawable}'s content. For {@link StringDrawable}, line size vertically
    * and a character horizontally.
    * <br>
    * This mode takes into accoun Visual option.
    * Only one increment is partially visible at any given time.
    */
   public static final int SCROLL_TYPE_1_LOGIC_UNIT      = 1;

   /**
    * Consideres the viewport width/height as an increment unit. <br>
    * Visible unit is always 1. <br>
    * Force scrolling to always be page scrolling<br>
    * <br>Unaware of Visual Types.
    */
   public static final int SCROLL_TYPE_2_PAGE_UNIT       = 2;

   /**
    * How to manage visual left over in scrolling type {@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT}.
    * <br>
    * Only relevant when ViewPane is not null.
    * Drawable get value from {@link ScrollConfig}.
    */
   public static final int VISUAL_0                      = 0;

   /**
    * Default behavior. Don't draw partial units.
    * <br>
    * <br>
    * Meaning for scrolling mode {@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT}
    * <br>
    * Set in {@link IBOViewPane#VP_OFFSET_06_VISUAL_LEFT_OVER1} 3 and 4
    */
   public static final int VISUAL_0_LEAVE                = 0;

   /**
    * Shows partial logical scrolling unit, i.e. only relevant in mode {@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT}. 
    * <br>
    * <br>
    * The number of increment visible is kept +1. 
    * <br>
    * <br>
    * Set in {@link IBOViewPane#VP_OFFSET_06_VISUAL_LEFT_OVER1} 3(5,6) and 4(7,8) bits
    */
   public static final int VISUAL_1_PARTIAL              = 1;

   /**
    * ViewPort size is reduced so the only visible increments fit content dimension.
    * <br>
    * Meaning for scrolling mode {@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT} <br>
    * <br>
    * Reduces drawable dimension of component.
    * Shrinks only if more than  1 logical increment
    * Only happens when {@link ViewDrawable} 
    */
   public static final int VISUAL_2_SHRINK               = 2;

   /**
    * Last partially visible increment is not drawn, partial space is given to the previous increment. <br>
    * <br>
    * {@link ViewDrawable} implementation does the fill.
    * {@link TableView} gives it to the last cell. 
    * {@link StringDrawable} and {@link ImageDrawable} ignores it.
    * <br>
    * <br>
    * In a Table with a single column that is given too much space. 
    * Usually we want that column to take all the space in order to use the unused pixel spaced.
    */
   public static final int VISUAL_3_FILL                 = 3;

   /**
    * Size HeaderTop independently of the ViewPort dimension.
    * <br>
    * When smaller, the Drawable will be drawn inside according to the anchor style
    * defined in 
    * When bigger, It will increase the {@link ViewDrawable} size similarly to Expand.
    * <br>
    * Sizing however is maxed to its own and to ViewDrawable max size.
    * <br>
    * When slave to Left/Right/Scrollbar, max size 
    */
   public static final int VP_SIZER_1_TOP                = 1 << 0;

   public static final int VP_SIZER_2_BOT                = 1 << 1;

   public static final int VP_SIZER_3_LEFT               = 1 << 2;

   public static final int VP_SIZER_4_RIGHT              = 1 << 3;

   public static final int VP_SIZER_5_TOP_CLOSE          = 1 << 4;

   public static final int VP_SIZER_6_BOT_CLOSE          = 1 << 5;

   public static final int VP_SIZER_7_LEFT_CLOSE         = 1 << 6;

   public static final int VP_SIZER_8_RIGHT_CLOSE        = 1 << 7;

}
