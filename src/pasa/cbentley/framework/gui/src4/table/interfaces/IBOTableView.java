package pasa.cbentley.framework.gui.src4.table.interfaces;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.framework.datamodel.src4.table.ITableModel;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.MyDrawables;
import pasa.cbentley.framework.gui.src4.core.ScrollConfig;
import pasa.cbentley.framework.gui.src4.core.ViewPane;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOViewPane;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;

/**
 * {@link IBOTableView} defines common display/usage parameters of tables.
 * 
 * <p>
 * 
 * <b>Technical parameters</b> : <br>
 * <li>Hide/Show columns title <-> Hide/Show row numbers
 * <li>Allow around the clock selection. Overrides parameters of the {@link ViewPane}. Eventually goes to {@link ScrollConfig}.
 * <li> <b>Selection parameters</b>
 * <ol>
 * <li> Row selection. The whole row is selected. Horizontal move is impossible.
 * <li> Overlay
 * <li> Pointer types of selection
 * </ol>
 * <li>Reverse order.  {@link IBOTableView}
 * <li>Draw a grid separation between cells. <br>
 * </p>
 * <br>
 * <p>
 * <b>Fill order</b> : <br>
 * Depends on level 
 * <li>Col : one grid row at a time
 * <li>Row : one grid col at a time
 * </p>
 * 
 * <p>
 * 
 * Then 
 * <li>Reverse H
 * <li>Reverse W
 * <br>
 * That gives 8 possibilities.
 * </p>
 * @author Charles-Philip Bentley
 *
 */
public interface IBOTableView extends IByteObject {

   public static final int T_BASE_OFFSET                             = A_OBJECT_BASIC_SIZE;

   /**
    * Tech size for Table
    * <br>
    * 9 bytes
    * <li> 4 bytes for 4 flags
    * <li> 2 bytes of h and w size
    * <li> 3 bytes for modes
    */
   public static final int T_BASIC_SIZE                              = A_OBJECT_BASIC_SIZE + 15;

   public static final int T_FLAG_1_                                 = 1 << 0;

   public static final int T_FLAG_2_                                 = 1 << 1;

   /**
    * Switch for showing Row Titles/Numbers
    */
   public static final int T_FLAG_3_SHOW_ROW_TITLE                   = 1 << 2;

   /**
    * Switch for showing Column Titles/Numbers
    */
   public static final int T_FLAG_4_SHOW_COL_TITLE                   = 1 << 3;

   /**
    * Show the real titles instead of column number, if they exist.
    * <br>
    * They are provided by {@link ITableModel}
    */
   public static final int T_FLAG_5_REAL_TITLE                       = 1 << 4;

   /**
    * Appends the real title to the column/row number.
    */
   public static final int T_FLAG_6_APPEND_REAL_TITLE                = 1 << 5;

   /**
    * if set, separator of size given in the tech is applied
    */
   public static final int T_FLAG_7_DRAW_GRID                        = 1 << 6;

   /**
    * Animated the transition of style from selected cells when transition is inside
    * <br>
    * <br>
    * 
    * Very tough job.
    * <br>
    * Style
    * <br>
    * Must also synchronize with any {@link ViewPane} animations {@link IBOViewPane#VP_FLAGX_7_ANIMATED}
    */
   public static final int T_FLAG_8_STYLE_ANIMATION                  = 1 << 7;

   /**
    * When this flag is set, Cells keeps their {@link ITechDrawable#STYLE_05_SELECTED} state style when {@link TableView} 
    * is removed from the main focus chain. i.e. 
    * {@link TableView#notifyEventKeyFocusLost(mordan.universal.event.MEvent)}
    * 
    * <br>
    * <br>
    * When not set, the state is set to false and thus removes the visuals
    * <br>
    * However the internal selection states is kept and when focus is given back to the TableView, Selected state
    * is not automatically given
    * <br>
    * <br>
    */
   public static final int T_FLAGF_1_SELECT_KEPT_WITHOUT_FOCUS_KEY   = 1;

   /**
    * When selection moves, does not move the selected focus {@link Controller#newFocusKey(mordan.controller.InputConfig, IDrawable)} to the new selected cell.
    * <br>
    * In the default {@link TableView#selectionMove(mordan.controller.InputConfig, int, int)}.
    * <br>
    * the key Focus may still be given when Fire/Enter command is generated.
    */
   public static final int T_FLAGF_2_SELECT_MOVE_NOT_GIVES_FOCUS     = 1 << 1;

   /**
    * When this flat is set, the focus given to the {@link TableView} does not automatically gives the selected state
    * <br>
    * <br>
    * When this flag is not set, the focus is also given to the if {@link IBOTableView#T_FLAGF_2_SELECT_MOVE_NOT_GIVES_FOCUS} is not set.
    */
   public static final int T_FLAGF_3_FOCUS_DOESNOT_GIVES_CELL_SELECT = 1 << 2;

   /**
    * Cell styles are not managed by {@link TableView} but by Model or sub class implementing the model.
    * <br>
    * <br>
    * Usually when model data is injected. TableUser provides everything. 
    * <br>
    * Sets the Helper flag {@link ITechTable#HELPER_FLAG_21_MODEL_STYLE}
    * <br>
    * <br>
    * Most of the time, subclass of {@link TableView} will set {@link ITechTable#HELPER_FLAG_21_MODEL_STYLE} in constructor.
    * <br>
    * <br>
    */
   public static final int T_FLAGF_4_NOSTYLE                         = 1 << 3;

   public static final int T_FLAGF_5_IGNORE_MODEL_TECH               = 1 << 4;

   public static final int T_FLAGF_6_MERGE_MODEL_TECH                = 1 << 5;

   /**
    * When set ignores
    */
   public static final int T_FLAGF_7_IGNORE_MODEL_POLICY             = 1 << 6;

   /**
    * When set, the table policy given by the model is merged with the policy
    * from the Table constructor.
    */
   public static final int T_FLAGF_8_MERGE_MODEL_POLICY              = 1 << 7;

   /**
    * Allows selection of multiple cells.
    * <br>
    * <br>
    * Built-check box first column appears or use a modifier key.
    * <br>
    * <br>
    * Selecting toggles check box select status.
    */
   public static final int T_FLAGM_2_MULTIPLE_SELECTION              = 1 << 1;

   public static final int T_FLAGM_3_MULTIPLE_SELECTION_CHECKBOX     = 1 << 2;

   public static final int T_FLAGM_1_                                = 1 << 0;

   public static final int T_FLAGM_4_                                = 1 << 3;

   /**
    * When going around the clock horizontally, next cell is on next line Once
    * the end is reached. next cell is the first cell.
    * <br>
    * For the flag to take effect, {@link ITechDrawable#BEHAVIOR_30_NAV_CLOCK_HORIZONTAL} must be set
    */
   public static final int T_FLAGM_5_CLOCK_FULL_H_LOOP               = 1 << 4;

   /**
    * Selection boundary move policy.
    * Horizontally it may go around the clock.
    * Set {@link ITechDrawable#BEHAVIOR_30_NAV_CLOCK_HORIZONTAL}
    */
   public static final int T_FLAGM_6_CLOCK_HORIZ                     = 1 << 5;

   /**
    * Navigation option.
    * Selection boundary move policy.
    * Vertically it may go around the clock
    */
   public static final int T_FLAGM_7_CLOCK_VERTICAL                  = 1 << 6;

   public static final int T_FLAGM_8_                                = 1 << 7;

   /**
    * Set to false for selection mechanism to work.
    * <br>
    * Set flag to true to remove selection semantics and mechanism.
    * <br>
    * <br>
    * Table will not implement selection style. 
    * <br>
    * Getters on selection return -1 since selection has no meaning. 
    * <br>
    * <br>
    * Scrolling still occurs.
    */
   public static final int T_FLAGX_1_NO_SELECTION                    = 1 << 0;

   public static final int T_FLAGX_2_                                = 1 << 1;

   /**
    * Switch for selection to behave as if all cells on a row are selected.
    * <br>
    * <br>
    * <b>Row Selection</b> <br>
    * The Tech Flag {@link IBOTableView#T_FLAGX_3_ROW_SELECTION} visual impact :
    * <li>Horizontal scrolling is still feasible but no other visual cues other than the scrollbar. 
    * <li>Scrolling unit is {@link ITechViewPane#SCROLL_TYPE_2_PAGE_UNIT} . 
    * <li>At the model level, no impact. Model user is free to read all columns once the selected row index is known. 
    * <br>
    * <br>
    * <b>State Management</b> 
    * <br>
    * Style states are all managed at the level of the {@link TableView}.
    * <br>
    * States are set to all cells on a row when flag is set {@link IBOTableView#T_FLAGX_4_ROW_SELECTION_STYLE_STATES}
    * <br>
    * <br>
    * Uses a {@link MyDrawables}
    */
   public static final int T_FLAGX_3_ROW_SELECTION                   = 1 << 2;

   /**
    * Set styles states  to all cells on a given line.
    * <li>{@link ITechDrawable#STYLE_05_SELECTED}
    * <li>{@link ITechDrawable#STYLE_06_FOCUSED_KEY}
    * <li>{@link ITechDrawable#STYLE_07_FOCUSED_POINTER}
    * <li>...
    */
   public static final int T_FLAGX_4_ROW_SELECTION_STYLE_STATES      = 1 << 3;

   /**
    * Draws an overlay figure over the row area.
    * <br>
    * Style to be drawn over the area as if row was a single Drawable.
    */
   public static final int T_FLAGX_5_ROW_SELECTION_NO_OVERLAY        = 1 << 4;

   /**
    * 
    */
   public static final int T_FLAGX_6_SELECTION_ASAP                  = 1 << 5;

   /**
    * Makes empty {@link Drawable} cells selectable. 
    * <br>
    * <br>
    * Model getter return null when empty cell is selected. 
    * <br>
    * <br>
    * Otherwise selection goes to next possible non empty cell.
    * <br>
    * <br>
    * 
    */
   public static final int T_FLAGX_8_SELECT_EMPTY_CELLS              = 1 << 7;

   /**
    * Gives the key focus on pointer select
    */
   public static final int T_FLAGY_1_GIVE_KEY_FOCUS                  = 1 << 0;

   /**
    * Selection moves along the model index count even if there are 2 visual dimensions.
    * <br>
    * Up/Left decrease count.
    * Down/Right increase count.
    * <br>
    * <br>
    * 
    * For some device We want the jog Up and Down to first do the columns in a row and then go
    * to the next row. 
    * 
    * {@link TableView} behaves like all cells are in a 1 dimension list for the purpose of navigation
    */
   public static final int T_FLAGY_2_LIST_UP_DOWN                    = 2;

   /**
    * Controls what happens to the selected row when scrolling occurs with a pointer on a scrollbar.
    * <br>
    * <br>
    * For logical scrolling and page scrolling:
    * <br>
    * <li>the selected row follows its current position relative to the visible cells range.
    * <br> 
    * <br>
    * <br>
    * With pixel scrolling:
    * <li>It follows when cell is entirely hidden.
    * <li>Unless {@link IBOTableView#T_FLAGX_6_SELECTION_ASAP} flag is set.
    * <br>
    * <br>
    * 
    */
   public static final int T_FLAGY_3_POINTER_SCROLLS_SELECTION       = 1 << 2;

   /**
    * When set, selected column and row follows its policy position,
    * even when table is pixel scrolled or logic scrolling using a scrollbar
    */
   public static final int T_FLAGY_4_ROOT_FOLLOWS_SCROLLING          = 1 << 3;

   /**
    * When TableView is allowed to modify its drawable size during a
    * configuration change <br>
    * This would force a relayout of parent drawable
    */
   public static final int T_FLAGY_5_VARIABLE_WIDTH                  = 1 << 4;

   /**
    * When TableView is allowed to modify its drawable size during a configuration change 
    * <br>
    * This would force a relayout of parent drawable and a full repaint of
    * {@link TableView}.
    */
   public static final int T_FLAGY_6_VARIABLE_HEIGHT                 = 1 << 5;

   public static final int T_FLAGY_7_                                = 1 << 6;

   /**
    * When pointer is pressed on a cell, it directly sends a selection event.
    * <br>
    * Otherwise, it needs another press on a selected cell.
    */
   public static final int T_FLAGY_8_POINTER_PRESS_SELECTION         = 1 << 7;

   /**
    */
   public static final int T_OFFSET_01_FLAG                          = T_BASE_OFFSET;

   /**
    * 
    */
   public static final int T_OFFSET_02_FLAGX                         = T_BASE_OFFSET + 1;

   public static final int T_OFFSET_03_FLAGY                         = T_BASE_OFFSET + 2;

   public static final int T_OFFSET_04_FLAGF                         = T_BASE_OFFSET + 3;

   /**
    * Number of pixels separating two rows of cells.
    * <br>
    * Expressed using the {@link ISizer}
    */
   public static final int T_OFFSET_05_HGRID_SIZE1                   = T_BASE_OFFSET + 4;

   /**
    * Number of pixels separating two columns of cells
    */
   public static final int T_OFFSET_06_VGRID_SIZE1                   = T_BASE_OFFSET + 5;

   /**
    * Model element to be selected by default at construction time.
    * <br>
    */
   public static final int T_OFFSET_07_SELECTION_DEFAULT1            = T_BASE_OFFSET + 6;

   /**
    * 
    * Defines the way selected cell is positioned to root scrolling increment. 
    * <br>
    * <li>0 means root is equal to selected 
    * <li>1 means selected cell is one cell away from root For example, 
    * <li>2 means 2 selected cells or middle if there are more 
    * <li>3 means 3 selected cells or middle 
    * <li>255 means middle.
    * <br>
    * Needed when pointer scrolling modifies selected element flag {@link IBOTableView#T_FLAGY_3_POINTER_SCROLLS_SELECTION}.
    * When not set, model scrolls below unless reached the end, or Scrolling is in circular mode {@link ITechViewPane#SB_MOVE_TYPE_2_CIRCULAR}.
    * <br>
    * <br>
    * See also {@link IBOTableView#T_FLAGY_4_ROOT_FOLLOWS_SCROLLING}
    */
   public static final int T_OFFSET_08_SELECTION_SCROLL1             = T_BASE_OFFSET + 7;

   /**
    * This flag works only on byte {@link IBOTableView#T_OFFSET_09_MODEL_FILL_TYPE1}.
    * <br> 
    * When not set (false), filling follows the normal way. It puts first model elements on the first row, the second row etc.
    * <br>
    * When it is set (true), model objects are filled column by columns. 
    * <br>
    */
   public static final int T_OFFSET_09_FLAG_3_FILL_COL                = 4;

   /**
    * Model filling mode : how does the model index relate to the visible index.
    * <br>
    * <br>
    * For example, the set in a 3 col 2 row grid. 
    * <br>
    * <br>
    * Row fill : fill one row at a time. 
    * <pre>
    * <li> a b c | d e  : TopLeft Start.
    * <li> c b a |   e d: TopRight start.
    * <li> d e   | a b c: BottomLeft
    * <li>   e d | c b a: BottomRight
    * </pre>
    * <p>
    * 
    * Col fill
    * <pre>
    * <li> a c e | b d  : TopLeft Start
    * <li> e c a |   d b: TopRight Start
    * <li> b d   | a c e: BotLeft Start
    * <li>   d b | e c a: BotRight Start
    * </pre>
    * </p>
    * <br>
    * <p>
    * 1 bit for col/row fill.   
    * <br>
    * 2 bits for start 
    * <li>{@link C#DIAG_DIR_0_TOP_LEFT}
    * <li>{@link C#DIAG_DIR_1_TOP_RIGHT}
    * <li>{@link C#DIAG_DIR_2_BOT_LEFT}
    * <li>{@link C#DIAG_DIR_3_BOT_RIGHT}
    * </p>
    */
   public static final int T_OFFSET_09_MODEL_FILL_TYPE1              = T_BASE_OFFSET + 8;

   /**
    * In Row Selection Mode, the column to recieve the {@link ITechDrawable#STYLE_06_FOCUSED_KEY}
    */
   public static final int T_OFFSET_10_SELECTION_COLUMN1             = T_BASE_OFFSET + 9;

   public static final int T_OFFSET_11_FLAGM                         = T_BASE_OFFSET + 10;

   /**
    * What pointer event generates a selection event {@link ITechTable#EVENT_ID_00_SELECT}
    * <br>
    * <li> {@link ITechTable#PSELECT_0_NONE}
    * <li> {@link ITechTable#PSELECT_1_PRESS}
    * <li> {@link ITechTable#PSELECT_2_PRESS_RELEASE}
    * 
    */
   public static final int T_OFFSET_12_PSELECTION_MODE1              = T_BASE_OFFSET + 11;

   /**
    * TODO what's this?
    */
   public static final int T_OFFSET_13_PSELECTION_TIME1              = T_BASE_OFFSET + 12;

   /**
    * What happens with model policy if not null?
    * <li> {@link ITechTable#MODEL_0_OVERIDE}
    * <li> {@link ITechTable#MODEL_1_MERGE}
    * <li> {@link ITechTable#MODEL_2_IGNORE}
    * 
    */
   public static final int T_OFFSET_14_MODEL_POLICY1                 = T_BASE_OFFSET + 13;

   /**
    * 
    */
   public static final int T_OFFSET_15_MODEL_TECH1                   = T_BASE_OFFSET + 14;

   /**
    * The Table will ignore all pointer event going through the ViewPort as long as this flag is set.
    * <br>
    */
   public static final int T_FLAGX_7_VIEW_PORT_BLIND                 = 1 << 6;
}
