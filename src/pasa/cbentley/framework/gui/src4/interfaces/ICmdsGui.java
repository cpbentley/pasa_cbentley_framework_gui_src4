package pasa.cbentley.framework.gui.src4.interfaces;

import pasa.cbentley.framework.cmd.src4.interfaces.ICmds;

/**
 * Commands specific to this view drawable module.
 * <br>
 * @author Charles Bentley
 *
 */
public interface ICmdsGui extends ICmds {

   public static final int A_SID_VCMD_A                    = 1000;

   public static final int A_SID_VCMD_Z                    = 2000;

   /**
    * Cmd to fall back on the last saved state.
    * If app is already running, first send a cmd for stopping the app but not exiting
    * 
    */
   public static final int CMD_GUI_00_LAST_LOGIN              = A_SID_VCMD_A;

   /**
    * 
    */
   public static final int CMD_GUI_01_RESUME_FROM_STATE       = A_SID_VCMD_A + 1;

   /**
    * Start
    */
   public static final int CMD_GUI_01_START_FRESH             = A_SID_VCMD_A;

   /**
    * Display list of last UIAs and user may toggle them
    */
   public static final int CMD_GUI_02_UIA_TOGGLE              = A_SID_VCMD_A + 2;

   public static final int CMD_GUI_03_EXIT_WITH_STATE         = A_SID_VCMD_A + 1;

   /**
    * Exit without writing state
    */
   public static final int CMD_GUI_04_EXIT_NO_STATE           = A_SID_VCMD_A + 1;

   /**
    * Combo of
    * <li> {@link ICmdsGui#CMD_GUI_04_EXIT_NO_STATE}
    * <li> {@link ICmdsGui#CMD_GUI_01_START_FRESH}
    */
   public static final int CMD_GUI_05_RESTART_EXIT_WITH_STATE = A_SID_VCMD_A + 1;

   public static final int CMD_GUI_08_PAGE_NEXT               = A_SID_VCMD_A + 8;

   public static final int CMD_GUI_09_PAGE_PREVIOUS           = A_SID_VCMD_A + 9;

   public static final int CMD_GUI_10_SHOW_FILTERS            = A_SID_VCMD_A + 10;

   public static final int CMD_GUI_11_SORT_INVERSE                 = A_SID_VCMD_A + 11;

   public static final int CMD_GUI_12_GO_TO                   = A_SID_VCMD_A + 12;

   public static final int CMD_GUI_13_HIDE                    = A_SID_VCMD_A + 13;

   /**
    * When moving a drawable
    */
   public static final int CMD_GUI_14_MOVE_DRAG               = A_SID_VCMD_A + 14;

   public static final int CMD_GUI_15_CANVAS_DEBUGGER         = A_SID_VCMD_A + 15;

   /**
    * Generic command for reading a number from keyboard
    */
   public static final int CMD_GUI_16_CUE_PARAM_NUMBER        = A_SID_VCMD_A + 16;

   public static final int CMD_GUI_17_MENU_BAR                = A_SID_VCMD_A + 17;

   public static final int CMD_GUI_19_SHOW_LIST               = A_SID_VCMD_A + 19;

   public static final int CMD_GUI_20_EDIT_MODE               = A_SID_VCMD_A + 20;
}
