package pasa.cbentley.framework.gui.src4.interfaces;

import pasa.cbentley.framework.gui.src4.mui.MLogViewer;

/**
 * The required style class
 * @author Charles Bentley
 *
 */
public interface IUIView {

   /**
    * StyleClass that includes all that is needed for a full table with String modifications
    * <br>
    * <br>
    * Row and Col Title.
    */
   public static final int SC_0_BASE_TABLE = 0;

   /**
    * StyleClass to be used by the Menubar
    */
   public static final int SC_1_MENU       = 1;

   /**
    * Style for Titles
    */
   public static final int SC_2_TITLE      = 2;

   /**
    * Style for the {@link MLogViewer}
    */
   public static final int SC_3_LOG        = 3;

   /**
    * 
    */
   public static final int SC_4_APP        = 4;

   /**
    * An application can define.
    */
   public static final int SC_5_STRING     = 5;

   /**
    * No border,padding,margin. no background. black fonts
    */
   public static final int SC_6_EMPTY      = 6;

   /**
    * 
    */
   public static final int SC_7_ROOT       = 7;

   /**
    * Any appli must set all those style class
    */
   public static final int SC_BASE_MAX     = 8;

}
