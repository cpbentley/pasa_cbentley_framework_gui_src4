package pasa.cbentley.framework.gui.src4.ctx;

import pasa.cbentley.core.src4.interfaces.IEvents;

public interface IEventsGui extends IEvents {

   public static final int A_SID_GUI_EVENT_A            = 26;

   public static final int A_SID_GUI_EVENT_Z            = 30;

   public static final int GUI_NUM_EVENTS               = 2;

   public static final int PID_00                       = 0;

   public static final int PID_00_ANY                   = A_SID_GUI_EVENT_A + PID_00;

   public static final int PID_00_XX                    = 1;

   public static final int PID_01                       = 1;

   public static final int PID_01_DRAW                  = A_SID_GUI_EVENT_A + PID_01;

   public static final int PID_01_DRAW_00_ANY           = 0;

   /**
    * Event that Request the current Virtual Keyboard to open.
    * <br>
    * Might be Host
    */
   public static final int PID_01_DRAW_01_FOCUS_CHANGE  = 1;

   public static final int PID_01_DRAW_02_              = 2;

   public static final int PID_01_XX                    = 3;

   public static final int PID_02                       = 1;

   public static final int PID_02_CHAR                  = A_SID_GUI_EVENT_A + PID_02;

   public static final int PID_02_CHAR_00_ANY           = 0;

   public static final int PID_02_CHAR_01_ADDED         = 1;

   public static final int PID_02_LIFE_02_CHOSEN        = 2;

   public static final int PID_02_XX                    = 3;


}
