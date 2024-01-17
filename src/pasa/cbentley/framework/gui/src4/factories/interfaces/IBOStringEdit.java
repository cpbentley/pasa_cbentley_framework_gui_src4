package pasa.cbentley.framework.gui.src4.factories.interfaces;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;
import pasa.cbentley.framework.gui.src4.string.StringEditControl;
import pasa.cbentley.framework.gui.src4.tech.ITechStringDrawable;

public interface IBOStringEdit extends IByteObject {

   public static final int SEDIT_BASIC_SIZE                       = A_OBJECT_BASIC_SIZE + 12;

   /**
    * By default, the caret is drawn as a FG figure.
    */
   public static final int SEDIT_FLAG_1_CARET_BG                  = 1;

   /**
    * Set to make caret blink
    */
   public static final int SEDIT_FLAG_2_CARET_BLINK               = 2;

   public static final int SEDIT_FLAG_4_KB_PREDICTIVE             = 8;

   /**
    * When
    */
   public static final int SEDIT_FLAG_5_CAPS_ON                   = 16;

   public static final int SEDIT_FLAG_6_SAME_THREAD               = 32;

   /**
    * Enables T9 dic search when NUMPAD is set.
    */
   public static final int SEDIT_FLAG_7_NUMPAD_T9                 = 64;

   /**
    * When Editing, use Keyboard or virtual keyboard when Device requires it.
    */
   public static final int SEDIT_FLAG_8_KEYBOARD                  = 128;

   /**
    * Save unknown Trie Dic into personnal dictionnary
    */
   public static final int SEDIT_FLAGX_1_SAVE_PERSONAL            = 1;

   public static final int SEDIT_OFFSET_01_FLAG                   = A_OBJECT_BASIC_SIZE;

   public static final int SEDIT_OFFSET_02_FLAGX                  = A_OBJECT_BASIC_SIZE + 1;

   public static final int SEDIT_OFFSET_03_EMPTY1                 = A_OBJECT_BASIC_SIZE + 2;

   /**
    * Default caret position when going into editing mode.
    * <br>
    * <br>
    * Maximum means at the end of the String.
    * <br>
    * <br>
    * This is used to save state for a {@link StringDrawable} .
    */
   public static final int SEDIT_OFFSET_04_CARET_POSITION2        = A_OBJECT_BASIC_SIZE + 3;

   /**
    * Controls how the {@link StringEditControl} class is positioned
    * <li> {@link ITechStringDrawable#SEDIT_CONTROL_0_CANVAS} in master canvas default position
    * <li> {@link ITechStringDrawable#SEDIT_CONTROL_1_TOP} just above as an overlay (so as to not change the structure hosting the {@link StringDrawable}
    * <li> {@link ITechStringDrawable#SEDIT_CONTROL_2_NONE}
    */
   public static final int SEDIT_OFFSET_05_CONTROL_POSITION1      = A_OBJECT_BASIC_SIZE + 5;

   /**
    * Number of deci second to wait before going on to the next char
    * <br>
    * Usually should be 50 (500 millis)
    */
   public static final int SEDIT_OFFSET_06_SPEED_NUMPAD1          = A_OBJECT_BASIC_SIZE + 6;

   /**
    * Number of visibles predictives
    */
   public static final int SEDIT_OFFSET_07_PRED_NUM_VISIBLE1      = A_OBJECT_BASIC_SIZE + 7;

   /**
    * Number of finds before a GUI update is called.
    * <br>
    * <br>
    * 
    */
   public static final int SEDIT_OFFSET_08_PRED_REAL_TIME1        = A_OBJECT_BASIC_SIZE + 8;

   /**
    * First table of symbols to be shown. This is part of Memory State
    */
   public static final int SEDIT_OFFSET_09_SYMBOL_TABLE1          = A_OBJECT_BASIC_SIZE + 9;

   /**
    * centi seconds for the flashing caret
    * 54
    * Can be defined as global in the global sedit object. which merge unto
    * different {@link ByteObject} unless BY is protected
    */
   public static final int SEDIT_OFFSET_10_CARET_BLINK_SPEED_ON1  = A_OBJECT_BASIC_SIZE + 10;

   public static final int SEDIT_OFFSET_11_CARET_BLINK_SPEED_OFF1 = A_OBJECT_BASIC_SIZE + 11;

}
