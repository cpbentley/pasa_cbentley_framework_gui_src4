package pasa.cbentley.framework.gui.src4.ctx;

import pasa.cbentley.core.src4.i8n.IStringsKernel;

public interface IStringsGui extends IStringsKernel {
   /**
    * Used as a base to compute index of String in text file
    */
   public static final int    AZ_STR_VIEW_A         = 20000;

   public static final int    AZ_STR_VIEW_Z         = 30000;

   public static final String STR_VIEW_FILE         = "strings_view";

   /**
    * Display list of last UIAs and user may toggle them
    */
   public static final int    VSTR_01_UIA_TOGGLE    = AZ_STR_VIEW_A + 1;

   public static final int    VSTR_08_PAGE_NEXT     = AZ_STR_VIEW_A + 8;

   public static final int    VSTR_09_PAGE_PREVIOUS = AZ_STR_VIEW_A + 9;

   public static final int    VSTR_10_SHOW_FILTERS  = AZ_STR_VIEW_A + 10;

   public static final int    VSTR_11_INVERSE       = AZ_STR_VIEW_A + 11;

   public static final int    VSTR_12_GO_TO         = AZ_STR_VIEW_A + 12;

   public static final int    VSTR_13_HIDE          = AZ_STR_VIEW_A + 13;

   /**
    * Cmd to fall back on the last saved state
    */
   public static final int    VSTR_14_LAST_LOGIN    = AZ_STR_VIEW_A + 14;

}
