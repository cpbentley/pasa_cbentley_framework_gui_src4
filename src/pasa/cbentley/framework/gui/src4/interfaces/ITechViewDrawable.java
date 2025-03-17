package pasa.cbentley.framework.gui.src4.interfaces;

import pasa.cbentley.core.src4.interfaces.ITech;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.canvas.GraphicsXD;
import pasa.cbentley.framework.gui.src4.core.ScrollBar;
import pasa.cbentley.framework.gui.src4.core.ScrollConfig;
import pasa.cbentley.framework.gui.src4.core.ViewDrawable;
import pasa.cbentley.framework.gui.src4.core.ViewPane;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOViewPane;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.table.interfaces.ITechCell;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;

/**
 * VSTATE flags are interal flags that should not be set by the devs outside of {@link ViewDrawable} business.
 * 
 * GENE flags are flags settable by 
 * @author Charles Bentley
 *
 */
public interface ITechViewDrawable extends ITech {

   /**
    * When set, draw method clips on content area.
    * <br>
    * <br>
    * Performance when {@link ViewDrawable} is sure by construction that no clip is needed.
    */
   public static final int FLAG_VSTATE_01_CLIP                       = 1 << 0;

   /**
    * Force the {@link ViewPane#drawDrawable(GraphicsXD)} to only paint the content. No Scrollbars, no Headers.
    * <br>
    * <br>
    * Only valid if {@link GraphicsX} paint flag FULL repaint is not set.
    * <br>
    * <br>
    * Also set when {@link ScrollBar} modifies the {@link ScrollConfig}. Scrollbar registers itself a repaintDrawable as well and
    * any Drawable registered as painting the {@link ScrollConfig}.
    * <br>
    * <br>
    * Used when for example a {@link ViewDrawable} like a {@link TableView} wants a repaint of itself but only the content, not the satellites.
    * <br>
    * <br>
    * The aim is to prevent the repainting of costly headers.
    */
   public static final int FLAG_VSTATE_02_REPAINTING_CONTENT         = 1 << 1;

   /**
    * Set when at least on {@link ViewPane} sattelite (header or {@link ScrollBar}) are displayed in Overay mode.
    * <br>
    * {@link ITechViewPane#PLANET_MODE_2_OVERLAY}
    * <br>
    * This means bottom up pointer events
    */
   public static final int FLAG_VSTATE_03_VIEWPANE_OVERLAY           = 1 << 2;

   /**
    * Set when content of viewdrawable drawn with {@link ViewDrawable#drawViewDrawable(GraphicsX, int, int, ScrollConfig, ScrollConfig)}
    * <br>
    * does not change state.
    * <br>
    * Animation may cache individual increments.
    */
   public static final int FLAG_VSTATE_04_NO_CONTENT_STATE           = 1 << 3;

   public static final int FLAG_VSTATE_05_                           = 1 << 4;

   /**
    * Headers and Scrollbars' width will follow the {@link ITechViewPane#PLANET_MODE_1_EXPAND}
    * 
    * {@link ITechViewPane#PLANET_MODE_0_EAT} becomes {@link ITechViewPane#PLANET_MODE_1_EXPAND} . 
    * <br>
    * <br>
    * Flag set by subclass, overriding {@link ViewPane}'s Tech planetary decision. <br>
    * Moreover Overlay should not be bigger than Dw.
    * <br>
    * <br>
    * This happens when content area. i.e. one line strings cannot be eaten.
    * <br>
    * Overlays {@link ITechViewPane#PLANET_MODE_2_OVERLAY} are still possible.
    */
   public static final int FLAG_VSTATE_06_NO_EAT_W_MUST_EXPAND       = 1 << 5;

   /**
    * Headers and Scrollbars' height may not be {@link ITechViewPane#PLANET_MODE_0_EAT}. 
    * <br>
    * <br>
    * Flag set by subclass, overriding any style decision.
    * <br>
    * <b>Example</b>: <br>
    * Set Liners init with height=-1 will set this flag.
    */
   public static final int FLAG_VSTATE_07_NO_EAT_H_MUST_EXPAND       = 1 << 6;

   /**
    * 
    */
   public static final int FLAG_VSTATE_08_PREF_SIZE_COMPUTED         = 1 << 7;

   public static final int FLAG_VSTATE_09_                           = 1 << 8;

   /**
    * Set to true when content's preferred width is forced set to the ViewPort's width at all times.
    * <p>
    * Specific case of {@link ITechViewDrawable#FLAG_VSTATE_12_CONTENT_W_DEPENDS_VIEWPORT}, where not only it depends, but it is matched exactly.
    * <br>
    * Horizontal scrolling is thus impossible since pw is equal to dw. So when true, this flag prevents the creation of a horizontal scrollbar
    * </p>
    * 
    * <p>
    * Makes {@link ViewDrawable} malleable with {@link ViewDrawable#isMalleable()}.
    * </p>
    * 
    * <p>
    * Case of a {@link TableView} with {@link ITechCell#TYPE_1_FLOW}.
    * </p>
    * 
    * <p>
    * Setting both flags to true prevents the creation of any scrollbars.
    * <li> {@link ITechViewDrawable#FLAG_VSTATE_10_CONTENT_PW_VIEWPORT_DW}
    * <li> {@link ITechViewDrawable#FLAG_VSTATE_11_CONTENT_PH_VIEWPORT_DH}
    * </p>
    */
   public static final int FLAG_VSTATE_10_CONTENT_PW_VIEWPORT_DW     = 1 << 9;

   /**
    * Set to true when content's preferred height is forced set to the ViewPort's height at all times.
    * 
    * <p>
    * Make {@link ViewDrawable} malleable with {@link ViewDrawable#isMalleable()}.
    * </p>
    * 
    * <p>
    * Setting both flags to true prevents the creation of any scrollbars.
    * <li> {@link ITechViewDrawable#FLAG_VSTATE_10_CONTENT_PW_VIEWPORT_DW}
    * <li> {@link ITechViewDrawable#FLAG_VSTATE_11_CONTENT_PH_VIEWPORT_DH}
    * </p>
    */
   public static final int FLAG_VSTATE_11_CONTENT_PH_VIEWPORT_DH     = 1 << 10;

   /**
    * View content needs to be initialized anytime ViewPane's width change, i.e. a scrollbar is added to the ViewPort. 
    * <br>
    * <br>
    * {@link ScrollBar} for horizontal scrolling is possible, while with {@link ITechViewDrawable#FLAG_VSTATE_10_CONTENT_PW_VIEWPORT_DW} which prevents horizontal scrolling.
    * <br>
    * <br>
    * Case of a {@link TableView} policy:
    * <li> {@link ITechCell#CELL_2_RATIO}
    * <li> {@link ITechCell#CELL_3_FILL_STRONG}
    * <li> {@link ITechCell#CELL_4_FILL_AVERAGE}
    * <li> {@link ITechCell#CELL_5_FILL_WEAK}
    * 
    * <p>
    * This flags makes {@link ViewDrawable} malleable with {@link ViewDrawable#isMalleable()}.
    * </p>
    * 
    */
   public static final int FLAG_VSTATE_12_CONTENT_W_DEPENDS_VIEWPORT = 1 << 11;

   /**
    * Content needs to be initialized anytime ViewPane's width change.
    * <br>
    * Case of a {@link TableView} policy with Fill Strong/Average/Weak.
    * <br>
    * It will may need scrollbars. Unlike other flag {@link ITechViewDrawable#FLAG_VSTATE_11_CONTENT_PH_VIEWPORT_DH} which
    * prevents vertical scrolling.
    */
   public static final int FLAG_VSTATE_13_CONTENT_H_DEPENDS_VIEWPORT = 1 << 12;

   /**
    * Drawable width is logical. ViewPane width is shrunk to match the number of content logical units. 
    * <br>
    * <br>
    * Force {@link ViewPane} horitonal axis planetaries from {@link ITechViewPane#PLANET_MODE_0_EAT} to {@link ITechViewPane#PLANET_MODE_1_EXPAND} mode. <br>
    * <br>
    * That means Left/Right headers + horizontal pixle consuming scrollbars.
    */
   public static final int FLAG_VSTATE_14_LOGICAL_WIDTH              = 1 << 13;

   /**
    * Drawable has been initialized with -1. That forces all ViewPane's headers 
    * and scrollbar to be {@link ITechViewPane#PLANET_MODE_1_EXPAND}
    */
   public static final int FLAG_VSTATE_15_LOGICAL_HEIGHT             = 1 << 14;

   /**
    * When Gene {@link ITechViewDrawable#FLAG_GENE_29_SHRINKABLE_W} is activated.
    * <br>
    * <br>
    * 
    */
   public static final int FLAG_VSTATE_16_SHRANK_W                   = 1 << 15;

   public static final int FLAG_VSTATE_17_SHRANK_H                   = 1 << 16;

   /**
    * Set when gene {@link ITechViewDrawable#FLAG_GENE_31_EXPANDABLE_W} is actived.
    */
   public static final int FLAG_VSTATE_18_EXPANDED_W                 = 1 << 17;

   public static final int FLAG_VSTATE_19_EXPANDED_H                 = 1 << 18;

   public static final int FLAG_VSTATE_20_                           = 1 << 19;

   /**
    * Set when {@link IBOViewPane#VP_FLAGX_1_STYLE_VIEWPANE} is set and ViewPane is not null.
    */
   public static final int FLAG_VSTATE_21_VP_STYLE_DRAWN             = 1 << 20;

   public static final int FLAG_VSTATE_22_SCROLLED                   = 1 << 21;

   public static final int FLAG_VSTATE_23_SCROLLED_H                 = 1 << 22;

   public static final int FLAG_VSTATE_24_SCROLLED_V                 = 1 << 23;

   /**
    * Has at least one header
    */
   public static final int FLAG_VSTATE_25_HEADERED                   = 1 << 24;

   public static final int FLAG_VSTATE_26_                           = 1 << 25;

   /**
    * When set, Header Top preferred size is taken into account to a maximum
    * <br>
    * Often maximum pw is ViewPort's width
    */
   public static final int FLAG_VSTATE_27_HEADER_TOP_PW              = 1 << 26;

   /**
    * Specifically prevents scrolling.
    */
   public static final int FLAG_GENE_28_NOT_SCROLLABLE               = 1 << 27;

   /**
    * Shrink Drawable Width to preferred width when former is bigger.
    * <br>
    * <br>
    * When both EXpandable and Shrinkable. Nothing is done.
    * <br>
    * Shrink flags are configured in the subclass Tech Param.
    */
   public static final int FLAG_GENE_29_SHRINKABLE_W                 = 1 << 28;

   /**
    * Shrink Drawable height to preferred height when former is bigger.
    * <br>
    * <br>
    * When both EXpandable and Shrinkable. Nothing is done.
    */
   public static final int FLAG_GENE_30_SHRINKABLE_H                 = 1 << 29;

   /**
    * When preferred width is smaller than drawable width, drawable expands its preferred size to drawable size.
    * 
    * 
    * State {@link ITechViewDrawable#FLAG_VSTATE_18_EXPANDED_W} is then set.
    * 
    * <p>
    * 
    * <li>Case of a single column table whose preferred width is smaller than the ViewPort.
    * <li>Case when Preferred Width is smaller than Drawable width and ViewDrawable has the Expandable genetic. An Image is not.
    * 
    * </p>
    * Fill the gap left by COLL_FILL policy or ratio. Why not asking Column policy?
    * 
    * <p>
    * Similar to {@link ITechViewDrawable#FLAG_GENE_32_EXPANDABLE_H} but for the width.
    * </p>
    */
   public static final int FLAG_GENE_31_EXPANDABLE_W                 = 1 << 30;

   /**
    * State {@link ITechViewDrawable#FLAG_VSTATE_18_EXPANDED_W} is then set.
    * 
    * Same as {@link ITechViewDrawable#FLAG_GENE_31_EXPANDABLE_W} but for the height.
    */
   public static final int FLAG_GENE_32_EXPANDABLE_H                 = 1 << 31;

}
