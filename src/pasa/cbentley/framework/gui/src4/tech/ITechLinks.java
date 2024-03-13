package pasa.cbentley.framework.gui.src4.tech;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.interfaces.ITech;
import pasa.cbentley.framework.drawx.src4.string.interfaces.IBOFigString;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.core.ViewPane;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStringData;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOViewPane;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;
import pasa.cbentley.framework.gui.src4.string.StringEditControl;
import pasa.cbentley.framework.gui.src4.string.StringEditModule;
import pasa.cbentley.framework.gui.src4.table.TableView;

public interface ITechLinks extends ITech {

   public static final int  LINK_10_BASE_TECH                         = 10;

   /**
    * The {@link IBOFigString} 
    */
   public static final int  LINK_40_BO_STRING_FIGURE                  = 40;

   /**
    * {@link IBOStringData}
    * <li> {@link IBOStringData#SDATA_OFFSET_02_PRESET_CONFIG1}
    */
   public static final int  LINK_41_BO_STRING_DATA                    = 41;

   /**
    * Linked to StyleClass of {@link StringDrawable}.
    * <br>
    * Reads by {@link StringDrawable} edit module {@link StringEditModule}.
    */
   public static final int  LINK_42_BO_STRING_EDIT                    = 42;

   public static final int  LINK_43_CARET_FIGURE                      = 43;

   /**
    * Link between a {@link StringDrawable} and the style of its auxilliary {@link TableView}.
    */
   public static final int  LINK_45_STRING_LIST_VIEW                  = 45;

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

   /**
    * Defines the style of attached {@link StringEditControl}.
    * <br>
    * Overrides the default one loaded initially by Framework
    */
   public static final char LINK_55_STYLE_STRING_EDIT_CONTROL         = 55;

   /**
    * Link to {@link ViewPane}'s {@link StyleClass} for holes.
    * <br>
    * <br>
    * Style is used when a specific Drawable (Special Button) is put into the hole.
    */
   public static final int  LINK_58_STYLE_VIEWPANE_HOLE_HEADER        = 58;

   public static final int  LINK_59_STYLE_VIEWPANE_HOLE_SB            = 59;

   public static final int  LINK_64_STYLE_VIEWPORT                    = 64;

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

   /**
    * ID linked to {@link StyleClass#linkByteObject(ByteObject, int)}
    * 
    * {@link IBOViewPane}
    */
   public static final int  LINK_66_BO_VIEWPANE                       = 66;

   public static final int  LINK_68_BO_V_SCROLLBAR                    = 68;

   public static final int  LINK_69_BO_H_SCROLLBAR                    = 69;

   public static final int  LINK_71_STYLE_VIEWPANE_H_SCROLLBAR        = 71;

   public static final int  LINK_72_STYLE_VIEWPANE_V_SCROLLBAR        = 72;

   /**
    * Style Class to be used for the menu showing commands
    */
   public static final int  LINK_74_STYLE_CLASS_MENU                  = 74;

   /**
    * Technical options of MenuBar
    */
   public static final int  LINK_75_MENU_BAR_TECH                     = 75;

   /**
    * Id to be used with stylekey with {@link StyleClass#linkTech(int, mordan.memory.ByteObject, int)}
    * <br>
    * 
    */
   public static final int  LINK_80_BO_TABLEVIEW                      = 80;

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

}
