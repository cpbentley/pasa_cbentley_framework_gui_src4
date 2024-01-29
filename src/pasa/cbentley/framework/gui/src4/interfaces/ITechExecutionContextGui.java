package pasa.cbentley.framework.gui.src4.interfaces;

import pasa.cbentley.framework.coreui.src4.exec.ITechExecutionContext;
import pasa.cbentley.framework.gui.src4.canvas.ExecutionContextGui;

/**
 * Defines constants for the {@link ExecutionContextGui}
 * <br>
 * <br>
 * 
 * @author Charles Bentley
 *
 */
public interface ITechExecutionContextGui extends ITechExecutionContext {

   public static final int EXEC_TYPE_2_DRAW      = 2;

   public static final int EXEC_TYPE_3_PAGE      = 3;

   public static final int ACTION_0_SHOW_OVER    = 0;

   public static final int ACTION_1_SHOW_REPLACE = 1;

   public static final int ACTION_2_HIDE         = 2;

   public static final int ACTION_3_INVALIDATE   = 3;

}
