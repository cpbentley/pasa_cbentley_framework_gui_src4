package pasa.cbentley.framework.gui.src4.ctx;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.core.ViewPane;
import pasa.cbentley.framework.gui.src4.string.EditModule;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;
import pasa.cbentley.framework.gui.src4.string.StringEditControl;
import pasa.cbentley.framework.gui.src4.table.TableView;

/**
 * Framework sub types belonging to the Drawable framework
 * <br>
 * <br>
 * 
 * Range allocation is [50-200]
 * <br>
 * <br>
 * For new types, extension type 
 * <br>
 * <br>
 * @author Charles-Philip Bentley
 *
 */
public interface IBOTypesGui extends IBOTypesBOC {

   public static final int  LINK_10_BASE_TECH                         = 10;

   public static final int  LINK_49_FIG_SCROLLBAR_WRAPPER             = 49;

   public static final int  LINK_50_STYLE_SCROLLBAR_BLOCK_BG          = 50;

   public static final int  LINK_51_STYLE_SCROLLBAR_BLOCK_FIG         = 51;

   /**
    * StyleClass for top/left wrappers and arrows and blocks.
    */
   public static final int  LINK_52_STYLE_SCROLLBAR_TOP_LEFT_WRAPPER  = 52;

   /**
    * StyleClass for bottom/right wrappers and arrows and blocks.
    */
   public static final int  LINK_53_STYLE_SCROLLBAR_BOT_RIGHT_WRAPPER = 53;

   public static final int  LINK_54_STYLE_CLASS_WRAPPER               = 54;

   /**
    * Style creator links Drawable and ViewPane styleKeys with this ID. 
    * <br>
    * <br>
    * {@link StyleClass#linkStyleClass(StyleClass, int)} : link between a Drawable style and the style of its ViewPane. 
    * <br>
    * <br>
    * If the drawable style has not been linked, the default ViewPane style is returned.
    */
   public static final int  LINK_65_STYLE_VIEWPANE                    = 65;

   public static final int  LINK_64_STYLE_VIEWPORT                    = 64;

   /**
    * ID linked to {@link StyleClass#linkByteObject(ByteObject, int)}
    */
   public static final int  LINK_66_TECH_VIEWPANE                     = 66;

   /**
    * Link to {@link ViewPane}'s {@link StyleClass} for holes.
    * <br>
    * <br>
    * Style is used when a specific Drawable (Special Button) is put into the hole.
    */
   public static final int  LINK_67_STYLE_VIEWPANE_HOLE               = 67;

   public static final int  LINK_68_TECH_V_SCROLLBAR                  = 68;

   public static final int  LINK_69_TECH_H_SCROLLBAR                  = 69;

   public static final int  LINK_71_STYLE_VIEWPANE_H_SCROLLBAR        = 71;

   public static final int  LINK_72_STYLE_VIEWPANE_V_SCROLLBAR        = 72;

   /**
    * Technical options of MenuBar
    */
   public static final int  LINK_75_MENU_BAR_TECH                     = 75;

   /**
    * Style Class to be used for the menu showing commands
    */
   public static final int  LINK_74_STYLE_CLASS_MENU                  = 74;

   /**
    * Id to be used with stylekey with {@link StyleClass#linkTech(int, mordan.memory.ByteObject, int)}
    * <br>
    * 
    */
   public static final int  LINK_80_TECH_TABLE                        = 80;

   /**
    * {@link StyleClass} that will be used by cells Drawable inside a {@link TableView} 
    */
   public static final int  LINK_81_STYLE_CLASS_TABLE_CELL            = 81;

   /**
    *  {@link StyleClass}
    */
   public static final int  LINK_82_STYLE_TABLE_COL_TITLE             = 82;

   /**
    * {@link StyleClass}
    */
   public static final int  LINK_83_STYLE_TABLE_ROW_TITLE             = 83;

   /**
    * {@link ByteObject}
    */
   public static final int  LINK_84_STYLE_TABLE_OVERLAY_FIGURE        = 84;

   public static final int  LINK_85_STYLE_TABLE_TITLE_TOP             = 85;

   public static final int  LINK_86_STYLE_TABLE_TITLE_BOT             = 86;

   /**
    * Usually this style must be set on the root {@link StyleClass}
    */
   public static final int  LINK_90_STRING_EDIT_CONTROL               = 90;

   /**
    * Linked to StyleClass of {@link StringDrawable}.
    * <br>
    * Reads by {@link StringDrawable} edit module {@link EditModule}.
    */
   public static final int  LINK_40_TECH_STRING_EDIT                  = 40;

   /**
    * Linked to StyleClass.
    * <br>
    * Reads by {@link StringDrawable}.
    */
   public static final int  LINK_41_TECH_STRING                       = 41;

   public static final int  LINK_42_CARET_FIGURE                      = 42;

   /**
    * Link between a {@link StringDrawable} and the style of its auxilliary {@link TableView}.
    */
   public static final int  LINK_45_STRING_LIST_VIEW                  = 45;

   public static final int  TYPE_101_VIEWPANE_TECH                    = 101;

   public static final int  TYPE_102_SCROLLBAR_TECH                   = 102;

   public static final int  TYPE_103_TABLE_TECH                       = 103;

   public static final int  TYPE_119_GENETICS                         = 119;

   public static final int  TYPE_120_TABLE_POLICY                     = 120;

   public static final int  TYPE_121_SPANNING                         = 121;

   /**
    * ByteObject type.
    */
   public static final int  TYPE_122_CELL_POLICY                      = 122;

   /**
    * Technical options for displaying string of characters.
    */
   public static final int  TYPE_124_STRING_TECH                      = 124;

   /**
    * Technical options for editing a string of characters.
    */
   public static final int  TYPE_125_STRING_EDIT_TECH                 = 125;

   /**
    * Global options that are user specific and apply to all string edition items
    */
   public static final int  TYPE_126_STRING_GLOBAL                    = 126;

   /**
    * Parameters for prediction types.
    * <li>All
    * <li>Names
    * <li>Countries
    * <br>
    * <br>
    * Prediction Engine to use
    */
   public static final int  TYPE_127_STRING_PREDITION                 = 127;

   /**
    * Defines animation parameters.
    * For example a fade in. When a style has such a figure
    * it will call the paintFigure method. If FIG_FLAG_ANIMATED set
    * the method looks up the ByteObject animation type
    * Entry, Life, Exit
    * Basic example, animation that changes the alpha value
    * will create a Animation class AlphaValue
    */
   public static final int  TYPE_130_ANIMATION                        = 130;

   public static final int  TYPE_141_USER_INTERACTION                 = 141;

   public static final int  TYPE_199_ANIM_TECH                        = 199;

   public static final int  TYPE_200_EXTENSION                        = 200;

   /**
    * Defines the style of attached {@link StringEditControl}.
    * <br>
    * Overrides the default one loaded initially by Framework
    */
   public static final char LINK_55_STYLE_STRING_EDIT_CONTROL         = 55;

   public static final int  SID_VIEWTYPE_A                            = 100;

   public static final int  SID_VIEWTYPE_Z                            = 199;
}
