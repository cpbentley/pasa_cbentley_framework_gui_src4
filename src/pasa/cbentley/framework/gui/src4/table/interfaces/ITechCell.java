package pasa.cbentley.framework.gui.src4.table.interfaces;

import pasa.cbentley.framework.gui.src4.table.CellModel;
import pasa.cbentley.framework.gui.src4.table.TableView;

public interface ITechCell {
   public static final int CELL_FLAG_1_UNSELECTABLE               = 1;

   public static final int FRAME_INSIDE_LENGTH_2                  = 2;

   /**
    *  When {@link CellModel#firstCellAbs}  and {@link CellModel#lastCellAbs}  will never change.
    *  <br>
    */
   public static final int HELPER_FLAG_04_TOTAL_SET               = 1 << 3;

   /**
    * Set when at least one column has a variable size that will change during a selection move. 
    * <br>
    * <br>
    * <b>Happens when </b><br>
    * <li>horizontal scrolling <br>
    * <b>AND</b> <br> 
    * <li>{@link IBOCellPolicy#CELL_4_FILL_AVERAGE} 
    * <li>{@link IBOCellPolicy#CELL_5_FILL_WEAK}
    * <br>
    * AND
    * No Frames.
    * <br>
    * When set, the method {@link TableView#computeVariableColSizes(TableView.Config)} must be called everytime there is a out of bound non partial transition.
    * <br>
    * <br>
    * TODO set it somewhere
    */
   public static final int HELPER_FLAG_06_VARIABLE_MOVE_SIZE      = 1 << 5;

   /**
    * Set when all cells have the same size. <br>
    * <li>Flow Etalon 
    * <li>Explicit Set
    */
   public static final int HELPER_FLAG_08_ALL_CELLS_SAME_SIZE     = 1 << 7;

   public static final int HELPER_FLAG_10_OWN_NAVIGATION          = 1 << 9;

   /**
    * When flag {@link T_FLAGY_5VARIABLE_WIDTH} is set and policy gives situations in which {@link TableView}'s drawable width is
    * going to change. 
    */
   public static final int HELPER_FLAG_12_EFFECTIVE_VARIABLE_SIZE = 1 << 11;

   /**
    * Setup sizes have at least one cell policy such as 
    * <li> {@link IBOCellPolicy#CELL_4_FILL_AVERAGE}
    * <li> {@link IBOCellPolicy#CELL_5_FILL_WEAK}
    * <br>
    * <br>
    * When the number of frames is inside the policy, the Fill cell size is computed during setup
    * <br>
    */
   public static final int HELPER_FLAG_14_VARIABLE_SETUP_SIZE     = 1 << 13;

   public static final int HELPER_FLAG_17_OVERSIZE                = 1 << 16;

   /**
    * When at least a cell has flag {@link CellModel#CELL_FLAG_1_UNSELECTABLE}
    */
   public static final int HELPER_FLAG_27_UNSELECTABLE            = 1 << 26;

   /**
    * Transition to a fully visible cell.
    */
   public static final int TRANSITION_0INSIDE_VIEWPORT            = 0;

   /**
    * Focus goes to a partially shown cell
    */
   public static final int TRANSITION_1INSIDE_TO_PARTIAL          = 1;

   /**
    * Focus goes to an invisible cell.
    */
   public static final int TRANSITION_2OUTSIDE                    = 2;
}
