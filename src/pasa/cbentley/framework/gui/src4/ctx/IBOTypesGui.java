package pasa.cbentley.framework.gui.src4.ctx;

import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesExtendedBOC;
import pasa.cbentley.framework.core.data.src4.ctx.IBOTypesCoreData;
import pasa.cbentley.framework.core.framework.src4.ctx.IBOTypesCoreFramework;
import pasa.cbentley.framework.core.ui.src4.ctx.IBOTypesCoreUi;
import pasa.cbentley.framework.coredraw.src4.ctx.IBOTypesCoreDraw;
import pasa.cbentley.framework.datamodel.src4.ctx.IBOTypesDataModel;
import pasa.cbentley.framework.drawx.src4.ctx.IBOTypesDrawX;
import pasa.cbentley.framework.input.src4.ctx.IBOTypesInput;
import pasa.cbentley.layouter.src4.ctx.IBOTypesLayout;

/**
 * Framework sub types belonging to the Drawable framework
 * 
 * <li> {@link IBOTypesBOC}
 * <li> {@link IBOTypesExtendedBOC}
 * <li> {@link IBOTypesDrawX}
 * <li> {@link IBOTypesLayout}
 * <li> {@link IBOTypesInput}
 * <li> {@link IBOTypesDataModel}
 * 
 * <li> {@link IBOTypesCoreDraw}
 * <li> {@link IBOTypesCoreUi}
 * <li> {@link IBOTypesCoreFramework}
 * <li> {@link IBOTypesCoreData}
 * <li>
 * <li>
 * @author Charles-Philip Bentley
 *
 */
public interface IBOTypesGui extends IBOTypesBOC {

   public static final int SID_VIEWTYPE_A                       = 151;

   public static final int SID_VIEWTYPE_Z                       = 190;

   public static final int TYPE_GUI_00_VIEWPANE                 = SID_VIEWTYPE_A + 0;

   public static final int TYPE_GUI_01_SCROLLBAR                = SID_VIEWTYPE_A + 1;

   public static final int TYPE_GUI_02_TABLE                    = SID_VIEWTYPE_A + 2;

   public static final int TYPE_GUI_03_TABLE_GENETICS           = SID_VIEWTYPE_A + 3;

   public static final int TYPE_GUI_04_TABLE_POLICY             = SID_VIEWTYPE_A + 4;

   public static final int TYPE_GUI_05_CELL_SPANNING            = SID_VIEWTYPE_A + 5;

   /**
    * ByteObject type.
    */
   public static final int TYPE_GUI_06_CELL_POLICY              = SID_VIEWTYPE_A + 6;



   /**
    * Technical options for displaying string of characters.
    */
   public static final int TYPE_DRWX_07_STRING_AUX_0_DATA       = IBOTypesDrawX.TYPE_DRWX_07_STRING_AUX_XXX + 0;
   /**
    * Technical options for editing a string of characters.
    */
   public static final int TYPE_DRWX_07_STRING_AUX_1_EDIT       = IBOTypesDrawX.TYPE_DRWX_07_STRING_AUX_XXX + 1;

   /**
    * Parameters for prediction types.
    * <li>All
    * <li>Names
    * <li>Countries
    * <br>
    * <br>
    * Prediction Engine to use
    */
   public static final int TYPE_DRWX_07_STRING_AUX_2_PREDICTION = IBOTypesDrawX.TYPE_DRWX_07_STRING_AUX_XXX + 2;
   /**
    * Global options that are user specific and apply to all string edition items
    */
   public static final int TYPE_DRWX_07_STRING_AUX_3_GLOBAL     = IBOTypesDrawX.TYPE_DRWX_07_STRING_AUX_XXX + 3;


   /**
    * Defines animation parameters.
    * For example a fade in. When a style has such a figure
    * it will call the paintFigure method. If FIG_FLAG_ANIMATED set
    * the method looks up the ByteObject animation type
    * Entry, Life, Exit
    * Basic example, animation that changes the alpha value
    * will create a Animation class AlphaValue
    */
   public static final int TYPE_GUI_11_ANIMATION                = SID_VIEWTYPE_A + 11;

   public static final int TYPE_GUI_12_USER_INTERACTION         = SID_VIEWTYPE_A + 12;

   public static final int TYPE_GUI_13_ANIM                     = SID_VIEWTYPE_A + 13;

   public static final int TYPE_GUI_15_EXTENSION                = SID_VIEWTYPE_A + 14;
}
