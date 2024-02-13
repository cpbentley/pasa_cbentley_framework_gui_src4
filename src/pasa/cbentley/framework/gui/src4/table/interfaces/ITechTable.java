package pasa.cbentley.framework.gui.src4.table.interfaces;

import pasa.cbentley.core.src4.interfaces.ITech;
import pasa.cbentley.framework.datamodel.src4.table.ITableModel;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.ScrollConfig;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.core.ViewPane;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.string.SymbolTable;
import pasa.cbentley.framework.gui.src4.table.CellModel;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;
import pasa.cbentley.framework.gui.src4.utils.DrawableMapper;

public interface ITechTable extends ITech {

   /**
   * Allows a listener to react to a cell being selected 
   * Kind of ActionListener.
   * <br>
   */
   public static final int EVENT_ID_00_SELECT                       = 0;

   /**
    * When selection focus goes over.
    */
   public static final int EVENT_ID_01_SELECTION_CHANGE             = 1;

   /**
    * Long selection press event.
    */
   public static final int EVENT_ID_02_SELECT_LONG                  = 2;

   /**
    * Sent before Selection Change
    */
   public static final int EVENT_ID_03_SELECTION_EDITION_END        = 3;

   public static final int EVENT_ID_03_SELECTION_EDITION_START      = 2;

   public static final int EVENT_ID_MAX                             = 2;

   /**
    * Case when only
    */
   public static final int HELPER_FLAG_01_STRONG_ROWS               = 1 << 0;

   /**
    * Flag set when config is never changed. 
    * <br>
    * No history of {@link TableView.Config}.
    */
   public static final int HELPER_FLAG_02_SINGLE_CONFIG             = 1 << 1;

   /**
    * Helper flag set by code when both conditions are met : <br>
    * <li>cells widths or heights are allowed to be bigger than ViewPort by cell policy flag {@link IBOCellPolicy#CELLP_FLAG_7_OVERSIZE}. 
    * <li>at least one such a cell has been computed in the model. 
    * <br>
    * <br>
    * It will happen when cells are initialized with
    * <li> {@link ITechCell#CELL_0_IMPLICIT_SET} or
    * <li> {@link ITechCell#CELL_1_EXPLICIT_SET}.
    * <br>
    * <br>
    * 
    * Logical scrolling {@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT} is possible. <br>
    * {@link TableView#initScrollingConfig(ScrollConfig, ScrollConfig)} compute a logical unit(s) for the hidden cell part(s).
    */
   public static final int HELPER_FLAG_03_CAP_OVERSIZE              = 1 << 2;

   /**
    * When only the y cell coordinates change
    */
   public static final int HELPER_FLAG_05_YCOORDS_ONLY              = 1 << 4;

   public static final int HELPER_FLAG_06_MODEL_MASK                = 1 << 5;

   /**
    * Set when the TableView has processed the {@link ITechDrawable#STYLE_05_SELECTED}
    */
   public static final int HELPER_FLAG_13_SELECTION_STATE_PROCESSED = 1 << 12;

   /**
    * Set when {@link ViewPane} has an overlay satellite, in which case pointer events must first redirect to {@link ViewPane}.
    * <br>
    * Bottom up does not work in that case.
    */
   public static final int HELPER_FLAG_14_VIEWPANE_OVERLAY          = 1 << 13;

   /**
    * Set when there IS there colspan or rowspan?
    */
   public static final int HELPER_FLAG_16_SPANNING                  = 1 << 15;

   public static final int HELPER_FLAG_17_OVERSIZE_WIDTH            = 1 << 16;

   public static final int HELPER_FLAG_18_OVERSIZE_HEIGHT           = 1 << 17;

   /**
    * When {@link T_OFFSET_9FILL_MODEL1} is different than 0
    */
   public static final int HELPER_FLAG_19_SPECIAL_FILL              = 1 << 18;

   /**
    * Set when {@link TableView} repaints based on indexes.
    * <br>
    * Table with hundreds of visible cells usually will not use {@link IDrawable}.
    * <br>
    */
   public static final int HELPER_FLAG_20_SPECIAL_REPAINT           = 1 << 19;

   /**
    * Set when {@link TableView} root cell {@link StyleClass} is not set to all {@link Drawable}.
    * <br>
    * <br>
    * Subclass of {@link TableView} is responsible to initialize model {@link Drawable} correctly.
    * <br>
    * <br>
    * @see IBOTableView#T_FLAGF_4_NOSTYLE
    */
   public static final int HELPER_FLAG_21_MODEL_STYLE               = 1 << 20;

   public static final int HELPER_FLAG_22_MODEL_GENETICS            = 1 << 21;

   /**
    * Set where there is no need for a {@link DrawableMapper}.
    * <br>
    * Case of subclass feeding in {@link IDrawable}s
    */
   public static final int HELPER_FLAG_23_NO_MAPPER                 = 1 << 22;

   /**
    * Sublcass override event method and no events should be sent on the {@link EventChannel}.
    */
   public static final int HELPER_FLAG_24_NO_EVENTS                 = 1 << 23;

   /**
    * Set when Table defines 1 or more structural styles.
    */
   public static final int HELPER_FLAG_25_STRUCT_STYLES             = 1 << 24;

   /**
    * When {@link ITableModel#getPropertyInt(int, int)} may be used.
    */
   public static final int HELPER_FLAG_26_UNSELECTABLE_CELLS        = 1 << 25;

   /**
    * When selectability needs to be checked using one of the following methods check in that order
    * <li> {@link ITechDrawable#BEHAVIOR_01_NOT_SELECTABLE}
    * <li> {@link ITableModel#getPropertyInt(int, int)}
    * <li> {@link CellModel#CELL_H_FLAG_27_UNSELECTABLE}
    */
   public static final int HELPER_FLAG_29_UNSELECTABLE_STUFF        = 1 << 28;

   public static final int HELPER_FLAG_30_DO_CTYPES                 = 1 << 29;

   public static final int ID_0_SELECTABLE                          = 0;

   public static final int ID_1_NOT_SELECTABLE                      = 1;

   public static final int ISTATE_FLAG_01_                          = 1 << 0;

   public static final int ISTATE_FLAG_02_                          = 1 << 1;

   public static final int ISTATE_FLAG_03_                          = 1 << 2;

   public static final int ISTATE_FLAG_04_                          = 1 << 3;

   /**
    * Uses the given style
    */
   public static final int LINK_0_TABLE_DEFAULT                     = 0;

   public static final int LINK_150_TABLE_EDITABLE_STRINGS          = 150;

   /**
    * 
    */
   public static final int MODEL_0_OVERIDE                          = 0;

   /**
    * 
    */
   public static final int MODEL_1_MERGE                            = 1;

   /**
    * 
    */
   public static final int MODEL_2_IGNORE                           = 2;

   /**
    * {@link ITechTable#EVENT_ID_00_SELECT} event is not possible with pointer.
    * <br>
    * <br>
    * The pointer event is always forwarded to the Drawable cell.
    */
   public static final int PSELECT_0_NONE                           = 0;

   /**
    * As soon as pointer press. sets the {@link ITechDrawable#STYLE_05_SELECTED} is the pressed cell.
    * <br>
    * 
    * The Android does not behave like this.
    */
   public static final int PSELECT_1_PRESS                          = 1;

   /**
    * Pointer must be pressed and released in the cell to generate select event
    * Timer between press and release must be met.
    * <br>
    * Android works like this with a small delay before the press event, waiting for a drag
    * event.
    */
   public static final int PSELECT_2_PRESS_RELEASE                  = 2;

   /**
    * Cell must be pressed while selected {@link ITechDrawable#STYLE_05_SELECTED} is on cell.
    * <br>
    * Used by the {@link SymbolTable} for example
    */
   public static final int PSELECT_3_SELECTED_PRESS                 = 3;

   /**
    * A Double press with timer
    */
   public static final int PSELECT_4_PRESS_DOUBLE                   = 4;

   /**
    * Event generated on the release after of a double press
    */
   public static final int PSELECT_5_PRESS_RELEASE_DOUBLE           = 5;

   public static final int PSELECT_CK_MAX                           = 5;

   /**
    * 
    */
   public static final int SELECTION_MOVE_0_NONE                    = 0;

   public static final int SELECTION_MOVE_1_CLOCK_FLAGS             = 1;

   /**
    * Navigation option: when reaching the end of a line, going right goes to the first column of next line.
    */
   public static final int SELECTION_MOVE_2_GRID_COUNT              = 2;

}
