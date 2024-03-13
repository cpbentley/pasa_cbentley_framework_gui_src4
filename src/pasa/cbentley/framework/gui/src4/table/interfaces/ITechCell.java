package pasa.cbentley.framework.gui.src4.table.interfaces;

import pasa.cbentley.core.src4.interfaces.ITech;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.table.CellModel;
import pasa.cbentley.framework.gui.src4.table.TableView;

public interface ITechCell extends ITech {

   /**
    * Maximum Preferred size of the Drawable cells on the Column/Row.
    * <br>
    * The sizers for both width and height will be
    * <li> Max value of TableViewPort
    * 
    * <br>
    * TODO This value can be set a maximum.
    * <br>
    * 
    * The drawable width or height value in method {@link IDrawable#init(int, int)} will be <b>0</b>.
    * <br>
    * <br>
    * 
    * For columns : <br>
    * Width of cells in the column is the width of the widest row-cell
    * <br>
    * Equivalent to 0 column size.
    * Override any size meaning
    * <br>
    * <br>
    * For rows: <br>
    * Height of cells in the row is the height of the tallest cell in that row <br>
    * <br>
    * Equivalent to 0 row size.
    * <br> <br>
    * When an implicit computation gives zero, falls back to size given in array, then falls back to single size.
    */
   public static final int CELL_0_IMPLICIT_SET                    = 0;

   /**
    * Needs an explicit pixel value for column's widht or row's height.
    * 
    * <p>
    * <li>{@link IBOCellPolicy#CELLP_OFFSET_09_SIZE4}
    * <li>{@link IBOCellPolicy#CELLP_FLAGZ_2_SIZES} 
    * <li>{@link IBOCellPolicy#CELLP_FLAGZ_8_SIZER} 
    * </p>
    * 
    * <p>
    * That means if value is {@link IBOCellPolicy#CELLP_OFFSET_09_SIZE4}, 
    * this policy will have the meaning of {@link ITechCell#CELL_0_IMPLICIT_SET} meaning.
    * </p>
    * 
    */
   public static final int CELL_1_EXPLICIT_SET                    = 1;

   /**
    * All columns with this policy will divide the space left between themselves within the current frame. <br>
    * according to 
    * {@link IBOCellPolicy#FLAGZ_2COL_SIZES}. If none is defined, leftover pixel room is divided evenly among
    * the column who share that Ratio policy. If no leftover space, a new window ViewPort area is given.
    * <br>
    * <br>
    * Not compatible with:
    * <li>{@link ITechCell#CELL_4_FILL_AVERAGE}
    * <li>{@link ITechCell#CELL_5_FILL_WEAK} <br>
    * <br>
    * By construction at least two ratio cells must be 
    * <br>
    * [{@link ITechCell#CELL_3_FILL_STRONG} - {@link ITechCell#CELL_2_RATIO} - {@link ITechCell#CELL_2_RATIO}] is possible.
    * The ratio rule applies to the ViewPort.
    * <br>
    * <br>
    * <b>Examples </b>: <br>
    * <li>RATIO,SET,RATIO : SET takes, other pixels are distributed using ratios. 1 Frame.
    * <li>RATIO,SET is identical to AVERAGE,SET
    * <li>SET,RATIO,SET is identical to SET,WEAK,SET.
    * <li>SET,RATIO,RATIO,SET is to SET,LeftOverSpaceRatioed,SET. if num of visible columns is not explicit.
    * <li>SET,RATIO|RATIO,SET (2) visible columns at a time. So we have SET,RATIO - RATIO,RATIO - RATIO,SET 
    * Variable size with Logical Scrolling.
    * <li>STRONG|RATIO becomes STRONG|STRONG
    * <li>RATIO,RATIO,RATIO shares all visible space. No scrolling
    * <li>RATIO,RATIO|RATIO,RATIO (2). 2 visible columns at a time. Space is shared. May give a variable size scenario.
    * <br>
    * Conversions happens inside the {@link TableView}.
    * When possible, it converts to {@link ITechCell#CELL_1_EXPLICIT_SET} otherwise set variable flag.
    */
   public static final int CELL_2_RATIO                           = 2;

   /**
    * Accepts 0 neighbouring cell within a frame <b>FILL0</b>. 
    * <br>
    * <br>
    * Even Ratio with 1 visible column/row.
    * <br>
    * Each cell on that axis takes every ViewPort pixels. <br>
    * <br>
    * <br>
    * <b>Examples yielding 1 frame</b>  <br>
    * <li>STRONG = 1 column
    * <br>
    * <br>
    * <b>Examples yielding 2 frames</b>  <br>
    * <li>STRONG | STRONG
    * <li>RATIO,RATIO | STRONG
    * <li>SET | STRONG
    * <li>SET,AVERAGE | STRONG
    * <li>SET,WEAK,SET | STRONG
    * <br>
    */
   public static final int CELL_3_FILL_STRONG                     = 3;

   /**
    * <b>FILL1</b> : Accepts 1 neighbouring cell within a frame <br>
    * <br>
    * <b>Examples yielding 1 frame</b>  <br>
    * <li>AVERAGE alone acts like STRONG
    * <li>SET,AVERAGE
    * <br>
    * <br>
    * <b>Examples yielding 2 frames</b>  <br>
    * <li>AVERAGE,SET | SET
    * <li>AVERAGE,SET,AVERAGE = AVERAGE,SET | AVERAGE or AVERAGE,SET | SET,AVERAGE
    * 
    * Frame enabling
    * 
    */
   public static final int CELL_4_FILL_AVERAGE                    = 4;

   /**
    * <b>FILL2</b> : Accepts 2 neighbouring cells within a frame <br>
    * <br>
    * Takes pixel room left once neighbouring {@link ITechCell#CELL_1_EXPLICIT_SET} or {@link ITechCell#CELL_0_IMPLICIT_SET}
    * column sizes have been computed. 
    * <br>
    * <br>
    * <b>Examples</b>: <br>
    * <li>SET,WEAK,SET = 1 frame.
    * <li>SET,WEAK,STRONG -> as SET,AVERAGE,STRONG.
    * <li>RATIO,WEAK,RATIO is fine.
    * <li>WEAK,SET -> Upgraded to {@link ITechCell#CELL_4_FILL_AVERAGE}
    * <li>WEAK -> Upgraded to {@link ITechCell#CELL_3_FILL_STRONG}
    * <li>WEAK,WEAK acts as STRONG,STRONG
    */
   public static final int CELL_5_FILL_WEAK                       = 5;

   /**
    * Explicitely
    */
   public static final int CELL_6_FRAME_SEPARATOR                 = 6;

   /**
    * Takes the size of another column
    * <br>
    * What column get the extras?
    */
   public static final int CELL_7_OTHER_COLUMN                    = 7;

   public static final int CELL_FLAG_1_UNSELECTABLE               = 1;

   /**
    *  When {@link CellModel#firstCellAbs}  and {@link CellModel#lastCellAbs}  will never change.
    *  <br>
    */
   public static final int CELL_H_FLAG_04_TOTAL_SET               = 1 << 3;

   /**
    * Set when at least one column has a variable size that will change during a selection move. 
    * <br>
    * <br>
    * <b>Happens when </b><br>
    * <li>horizontal scrolling <br>
    * <b>AND</b> <br> 
    * <li>{@link ITechCell#CELL_4_FILL_AVERAGE} 
    * <li>{@link ITechCell#CELL_5_FILL_WEAK}
    * <br>
    * AND
    * No Frames.
    * <br>
    * When set, the method {@link TableView#computeVariableColSizes(TableView.Config)} must be called everytime there is a out of bound non partial transition.
    * <br>
    * <br>
    * TODO set it somewhere
    */
   public static final int CELL_H_FLAG_06_VARIABLE_MOVE_SIZE      = 1 << 5;

   /**
    * Set when all cells have the same size. <br>
    * <li>Flow Etalon 
    * <li>Explicit Set
    */
   public static final int CELL_H_FLAG_08_ALL_CELLS_SAME_SIZE     = 1 << 7;

   public static final int CELL_H_FLAG_10_OWN_NAVIGATION          = 1 << 9;

   /**
    * When flag {@link T_FLAGY_5VARIABLE_WIDTH} is set and policy gives situations in which {@link TableView}'s drawable width is
    * going to change. 
    */
   public static final int CELL_H_FLAG_12_EFFECTIVE_VARIABLE_SIZE = 1 << 11;

   /**
    * Setup sizes have at least one cell policy such as 
    * <li> {@link ITechCell#CELL_4_FILL_AVERAGE}
    * <li> {@link ITechCell#CELL_5_FILL_WEAK}
    * <br>
    * <br>
    * When the number of frames is inside the policy, the Fill cell size is computed during setup
    * <br>
    */
   public static final int CELL_H_FLAG_14_VARIABLE_SETUP_SIZE     = 1 << 13;

   public static final int CELL_H_FLAG_17_OVERSIZE                = 1 << 16;

   /**
    * When at least a cell has flag {@link CellModel#CELL_FLAG_1_UNSELECTABLE}
    */
   public static final int CELL_H_FLAG_27_UNSELECTABLE            = 1 << 26;

   public static final int FRAME_INSIDE_LENGTH_2                  = 2;

   /**
    * Transition to a fully visible cell.
    */
   public static final int TRANSITION_0_INSIDE_VIEWPORT           = 0;

   /**
    * Focus goes to a partially shown cell
    */
   public static final int TRANSITION_1_INSIDE_TO_PARTIAL         = 1;

   /**
    * Focus goes to an invisible cell.
    */
   public static final int TRANSITION_2_OUTSIDE                   = 2;

   /**
    * Mix of all policies.
    * <br>
    * Cell number is optional<br>
    * Frame number is optional<br>
    */
   public static final int TYPE_0_GENERIC                         = 0;

   /**
    * Number of cells is unknown and depends on available content pixel size.
    * <br>
    * <br>
    * Etalon or init cells with given {@link IBOCellPolicy#CELLP_OFFSET_09_SIZE4} and opposite cosize values.
    * <br>
    * <br>
    */
   public static final int TYPE_1_FLOW                            = 1;

   /**
    * All cells have the ratio policy {@link CELL_2_RATIO}. 
    * <br>
    * <br>
    * Cell number is optional
    * <br>
    * Visible Cells number is optional
    * <br>
    * <br>
    */
   public static final int TYPE_2_RATIO                           = 2;
}
