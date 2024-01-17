package pasa.cbentley.framework.gui.src4.interfaces;

import pasa.cbentley.core.src4.interfaces.ITech;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.core.ScrollBar;
import pasa.cbentley.framework.gui.src4.core.ScrollConfig;
import pasa.cbentley.framework.gui.src4.core.ViewDrawable;
import pasa.cbentley.framework.gui.src4.core.ViewPane;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOViewPane;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOCellPolicy;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;

public interface ITechViewDrawable extends ITech {

   /**
    * When set, Header Top preferred size is taken into account to a maximum
    * <br>
    * Often maximum pw is ViewPort's width
    */
   public static final int VIEW_GENE_27_HEADER_TOP_PW              = 1 << 26;

   /**
    * Specifically prevents scrolling.
    */
   public static final int VIEW_GENE_28_NOT_SCROLLABLE             = 1 << 27;

   /**
    * Shrink Drawable Width to preferred width when former is bigger.
    * <br>
    * <br>
    * When both EXpandable and Shrinkable. Nothing is done.
    * <br>
    * Shrink flags are configured in the subclass Tech Param.
    */
   public static final int VIEW_GENE_29_SHRINKABLE_W               = 1 << 28;

   /**
    * Shrink Drawable height to preferred height when former is bigger.
    * <br>
    * <br>
    * When both EXpandable and Shrinkable. Nothing is done.
    */
   public static final int VIEW_GENE_30_SHRINKABLE_H               = 1 << 29;

   /**
    * When preferred size is smaller than drawable size, drawable expands its preferred size to drawable size.
    * <br>
    * State Expanded is then set.
    * <br>
    * Case of a single column table whose preferred width is smaller than the ViewPort.
    * Case when Preferred Width is smaller than Drawable width and ViewDrawable has the Expandable genetic. An Image is not.
    * <br>
    * Fill the gap left by COLL_FILL policy or ratio. Why not asking Column policy?
    * 
    */
   public static final int VIEW_GENE_31_EXPANDABLE_W               = 1 << 30;

   /**
    * 
    */
   public static final int VIEW_GENE_32_EXPANDABLE_H               = 1 << 31;

   /**
    * When set, draw method clips on content area.
    * <br>
    * <br>
    * Performance when {@link ViewDrawable} is sure by construction that no clip is needed.
    */
   public static final int VIEWSTATE_01_CLIP                       = 1 << 0;

   /**
    * Force the {@link ViewPane#drawDrawable(GraphicsX)} to only paint the content. No Scrollbars, no Headers.
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
   public static final int VIEWSTATE_02_REPAINTING_CONTENT         = 1 << 1;

   /**
    * Set when at least on {@link ViewPane} sattelite (header or {@link ScrollBar}) are displayed in Overay mode.
    * <br>
    * {@link ITechViewPane#PLANET_MODE_2_OVERLAY}
    * <br>
    * This means bottom up pointer events
    */
   public static final int VIEWSTATE_03_VIEWPANE_OVERLAY           = 1 << 2;

   /**
    * Set when content of viewdrawable drawn with {@link ViewDrawable#drawViewDrawable(GraphicsX, int, int, ScrollConfig, ScrollConfig)}
    * <br>
    * does not change state.
    * <br>
    * Animation may cache individual increments.
    */
   public static final int VIEWSTATE_04_NO_CONTENT_STATE           = 1 << 3;

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
   public static final int VIEWSTATE_06_NO_EAT_W_MUST_EXPAND       = 1 << 5;

   /**
    * Headers and Scrollbars' height may not be {@link ITechViewPane#PLANET_MODE_0_EAT}. 
    * <br>
    * <br>
    * Flag set by subclass, overriding any style decision.
    * <br>
    * <b>Example</b>: <br>
    * Set Liners init with height=-1 will set this flag.
    */
   public static final int VIEWSTATE_07_NO_EAT_H_MUST_EXPAND       = 1 << 6;

   /**
    * 
    */
   public static final int VIEWSTATE_08_PREF_SIZE_COMPUTED         = 1 << 7;

   /**
    * Set to true when content pw follows the ViewPort's width at all times.
    * <br>
    * <br>
    * Specific case of {@link ITechViewDrawable#VIEWSTATE_12_CONTENT_W_DEPENDS_VIEWPORT}, where not only it depends, but it is matched exactly.
    * <br>
    * Horizontal scrolling is thus impossible since pw being equal to dw. 
    * <br>
    * <br>
    * Makes {@link ViewDrawable} malleable with {@link ViewDrawable#isMalleable()}.
    * <br>
    * <br>
    * Case of a {@link TableView} with {@link IBOCellPolicy#TYPE_1_FLOW}.
    * <br>
    */
   public static final int VIEWSTATE_10_CONTENT_PW_VIEWPORT_DW     = 1 << 9;

   /**
    * Same as {@link ITechViewDrawable#VIEWSTATE_10_CONTENT_PW_VIEWPORT_DW}
    * Both states set is not useful.<br>
    * Prevents the creation of a scrollbar
    * <br>
    * Make {@link ViewDrawable} malleable with {@link ViewDrawable#isMalleable()}.
    */
   public static final int VIEWSTATE_11_CONTENT_PH_VIEWPORT_DH     = 1 << 10;

   /**
    * View content needs to be initialized anytime ViewPane's width change, i.e. a scrollbar is added to the ViewPort. 
    * <br>
    * <br>
    * {@link ScrollBar} for horizontal scrolling is possible, while with {@link ITechViewDrawable#VIEWSTATE_10_CONTENT_PW_VIEWPORT_DW} which prevents horizontal scrolling.
    * <br>
    * <br>
    * Case of a {@link TableView} policy:
    * <li> {@link IBOCellPolicy#CELL_2_RATIO}
    * <li> {@link IBOCellPolicy#CELL_3_FILL_STRONG}
    * <li> {@link IBOCellPolicy#CELL_4_FILL_AVERAGE}
    * <li> {@link IBOCellPolicy#CELL_5_FILL_WEAK}
    * <br>
    * <br>
    * Make {@link ViewDrawable} malleable with {@link ViewDrawable#isMalleable()}.
    * 
    */
   public static final int VIEWSTATE_12_CONTENT_W_DEPENDS_VIEWPORT = 1 << 11;

   /**
    * Content needs to be initialized anytime ViewPane's width change.
    * <br>
    * Case of a {@link TableView} policy with Fill Strong/Average/Weak.
    * <br>
    * It will may need scrollbars. Unlike other flag {@link ITechViewDrawable#VIEWSTATE_11_CONTENT_PH_VIEWPORT_DH} which
    * prevents vertical scrolling.
    */
   public static final int VIEWSTATE_13_CONTENT_H_DEPENDS_VIEWPORT = 1 << 12;

   /**
    * Drawable width is logical. ViewPane width is shrunk to match the number of content logical units. 
    * <br>
    * <br>
    * Force {@link ViewPane} horitonal axis planetaries from {@link ITechViewPane#PLANET_MODE_0_EAT} to {@link ITechViewPane#PLANET_MODE_1_EXPAND} mode. <br>
    * <br>
    * That means Left/Right headers + horizontal pixle consuming scrollbars.
    */
   public static final int VIEWSTATE_14_LOGICAL_WIDTH              = 1 << 13;

   /**
    * Drawable has been initialized with -1. That forces all ViewPane's headers 
    * and scrollbar to be {@link ITechViewPane#PLANET_MODE_1_EXPAND}
    */
   public static final int VIEWSTATE_15_LOGICAL_HEIGHT             = 1 << 14;

   /**
    * When Gene {@link ITechViewDrawable#VIEW_GENE_29_SHRINKABLE_W} is activated.
    * <br>
    * <br>
    * 
    */
   public static final int VIEWSTATE_16_SHRANK_W                   = 1 << 15;

   public static final int VIEWSTATE_17_SHRANK_H                   = 1 << 16;

   /**
    * Set when gene {@link ITechViewDrawable#VIEW_GENE_31_EXPANDABLE_W} is actived.
    */
   public static final int VIEWSTATE_18_EXPANDED_W                 = 1 << 17;

   public static final int VIEWSTATE_19_EXPANDED_H                 = 1 << 18;

   /**
    * Set when {@link IBOViewPane#VP_FLAGX_1_STYLE_VIEWPANE} is set and ViewPane is not null.
    */
   public static final int VIEWSTATE_21_VP_STYLE_DRAWN             = 1 << 19;

   public static final int VIEWSTATE_22_SCROLLED                   = 1 << 21;

   public static final int VIEWSTATE_23_SCROLLED_H                 = 1 << 22;

   public static final int VIEWSTATE_24_SCROLLED_V                 = 1 << 23;

   /**
    * Has at least one header
    */
   public static final int VIEWSTATE_25_HEADERED                   = 1 << 24;

}
