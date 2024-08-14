package pasa.cbentley.framework.gui.src4.interfaces;

import pasa.cbentley.framework.cmd.src4.interfaces.ICmdsCmd;

/**
 * Commands specific to this view drawable module.
 * <br>
 * @author Charles Bentley
 *
 */
public interface ICmdsGui extends ICmdsCmd {

   public static final int A_SID_VCMD_A                    = 2000;

   public static final int A_SID_VCMD_Z                    = 4999;

   /**
    * Cmd to fall back on the last saved state.
    * If app is already running, first send a cmd for stopping the app but not exiting
    * 
    */
   public static final int VCMD_00_LAST_LOGIN              = A_SID_VCMD_A;

   /**
    * Start
    */
   public static final int VCMD_01_START_FRESH             = A_SID_VCMD_A;

   /**
    * 
    */
   public static final int VCMD_01_RESUME_FROM_STATE       = A_SID_VCMD_A + 1;

   public static final int VCMD_03_EXIT_WITH_STATE         = A_SID_VCMD_A + 1;

   /**
    * Exit without writing state
    */
   public static final int VCMD_04_EXIT_NO_STATE           = A_SID_VCMD_A + 1;

   /**
    * Combo of
    * <li> {@link ICmdsGui#VCMD_04_EXIT_NO_STATE}
    * <li> {@link ICmdsGui#VCMD_01_START_FRESH}
    */
   public static final int VCMD_05_RESTART_EXIT_WITH_STATE = A_SID_VCMD_A + 1;

   /**
    * Display list of last UIAs and user may toggle them
    */
   public static final int VCMD_02_UIA_TOGGLE              = A_SID_VCMD_A + 2;

   public static final int VCMD_08_PAGE_NEXT               = A_SID_VCMD_A + 8;

   public static final int VCMD_09_PAGE_PREVIOUS           = A_SID_VCMD_A + 9;

   public static final int VCMD_10_SHOW_FILTERS            = A_SID_VCMD_A + 10;

   public static final int VCMD_11_INVERSE                 = A_SID_VCMD_A + 11;

   public static final int VCMD_12_GO_TO                   = A_SID_VCMD_A + 12;

   public static final int VCMD_13_HIDE                    = A_SID_VCMD_A + 13;

   /**
    * When moving a drawable
    */
   public static final int VCMD_14_MOVE_DRAG               = A_SID_VCMD_A + 14;

   public static final int VCMD_15_CANVAS_DEBUGGER         = A_SID_VCMD_A + 15;

   public static final int VCMD_17_MENU_BAR                = A_SID_VCMD_A + 17;

   /**
    * Generic command for reading a number from keyboard
    */
   public static final int VCMD_16_CUE_PARAM_NUMBER        = A_SID_VCMD_A + 16;
}
