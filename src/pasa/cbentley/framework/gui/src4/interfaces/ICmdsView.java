package pasa.cbentley.framework.gui.src4.interfaces;

import pasa.cbentley.framework.cmd.src4.interfaces.ICmdsCmd;
import pasa.cbentley.framework.gui.src4.canvas.FocusCtrl;

/**
 * Commands specific to this view drawable module.
 * <br>
 * @author Charles Bentley
 *
 */
public interface ICmdsView extends ICmdsCmd {

   /**
    * Context ID to be used when calling
    * {@link CmdController#setActiveCommandable(mordan.controller.interfaces.ICommandable, int)}
    * <br>
    * <br>
    * When the key context changes.
    * 
    * <br>
    * When a key is pressed, it is associated with the current active
    * Key context.
    * In the Drawable framework, each context category has a focused Drawable.
    * 
    * {@link FocusCtrl#getFocus(int)
    * <br>
    * 
    */
   public static final int CTX_CAT_0_KEY              = 0;

   /**
    * A category for each pointer ID?
    */
   public static final int CTX_CAT_1_POINTER_PRESSED  = 1;

   public static final int CTX_CAT_2_POINTER_OVER     = 2;

   /**
    * When Device has several pointers.
    * Base support until 5 pointers
    * <br>
    * This will depend on the Host.
    * FEATURE_TOUCHSCREEN_MULTITOUCH_JAZZHAND
    * <br>
    * Framework supports any number of pointers.
    * <br>
    * But the IDs are then dynamically created
    */
   public static final int CTX_CAT_3_POINTER2_PRESSED = 3;

   public static final int CTX_CAT_4_POINTER2_OVER    = 4;

   public static final int CTX_CAT_5_POINTER3_PRESSED = 5;

   public static final int CTX_CAT_6_POINTER3_OVER    = 6;

   public static final int A_SID_VCMD_A               = 2000;

   public static final int A_SID_VCMD_Z               = 4999;

   /**
    * Cmd to fall back on the last saved state
    */
   public static final int VCMD_00_LAST_LOGIN         = A_SID_VCMD_A;

   /**
    * 
    */
   public static final int VCMD_01_RESUME_FROM_STATE  = A_SID_VCMD_A + 1;

   /**
    * Display list of last UIAs and user may toggle them
    */
   public static final int VCMD_02_UIA_TOGGLE         = A_SID_VCMD_A + 2;

   public static final int VCMD_08_PAGE_NEXT          = A_SID_VCMD_A + 8;

   public static final int VCMD_09_PAGE_PREVIOUS      = A_SID_VCMD_A + 9;

   public static final int VCMD_10_SHOW_FILTERS       = A_SID_VCMD_A + 10;

   public static final int VCMD_11_INVERSE            = A_SID_VCMD_A + 11;

   public static final int VCMD_12_GO_TO              = A_SID_VCMD_A + 12;

   public static final int VCMD_13_HIDE               = A_SID_VCMD_A + 13;

   /**
    * When moving a drawable
    */
   public static final int VCMD_14_MOVE_DRAG          = A_SID_VCMD_A + 14;

   public static final int VCMD_15_CANVAS_DEBUGGER    = A_SID_VCMD_A + 15;

   public static final int VCMD_17_MENU_BAR           = A_SID_VCMD_A + 17;

   /**
    * Generic command for reading a number from keyboard
    */
   public static final int VCMD_16_CUE_PARAM_NUMBER   = A_SID_VCMD_A + 16;
}
