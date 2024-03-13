package pasa.cbentley.framework.gui.src4.tech;

import pasa.cbentley.core.src4.interfaces.ITech;
import pasa.cbentley.framework.drawx.src4.string.interfaces.IBOFigString;
import pasa.cbentley.framework.gui.src4.core.ViewDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechViewDrawable;
import pasa.cbentley.framework.gui.src4.string.StringEditModule;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;

public interface ITechStringDrawable extends ITech {

   public static final char DEFAULT_CARET_CHAR              = '>';

   /**
    * Delay for seperating strokes when a key is continuously pressed
    * This is normally controlled by InputConfig, but this component may override
    */
   public static final int  DEFAULT_DELAY_MULTIPLIER        = 5;

   /**
    * Any types of characters is valid.
    */
   public static final int  S_DATA_0_ANY                    = 0;

   /**
    * alpha numeric + @ and . and _
    */
   public static final int  S_DATA_1_EMAIL                  = 1;

   /**
    * only integer
    */
   public static final int  S_DATA_2_NUMERIC                = 2;

   /**
    * integer with . for decimals
    */
   public static final int  S_DATA_3_DECIMAL                = 3;

   /**
    * alpha numeric + may start with http[s]://
    */
   public static final int  S_DATA_4_URL                    = 4;

   /**
    * Date format
    */
   public static final int  S_DATA_5_DATE                   = 5;

   public static final int  S_DATA_MAX_CK                   = 5;

   public static final int  S_DATA_MAX_MODULO               = 6;

   /**
    * {@link StringDrawable}'s content cannot be edited or selected
    */
   public static final int  S_ACTION_MODE_0_READ            = 0;

   /**
    * {@link StringDrawable}'s {@link StringEditModule} only allows selection.
    */
   public static final int  S_ACTION_MODE_1_SELECT          = 1;

   /**
    * {@link StringDrawable}'s {@link StringEditModule} only allows both selection and edition.
    */
   public static final int  S_ACTION_MODE_2_EDIT            = 2;

   public static final int  SEDIT_CONTROL_0_CANVAS          = 0;

   /**
    * 
    */
   public static final int  SEDIT_CONTROL_1_TOP             = 1;

   public static final int  SEDIT_CONTROL_2_NONE            = 2;

   /**
    * Custom state set when Edit mode is true.
    * <br>
    * Controls the edit mode style.
    * <br>
    * 
    */
   public static final int  SEDIT_STATE_AS1_EDIT            = ITechDrawable.STYLE_01_CUSTOM;

   /**
    * 
    */
   public static final int  STRING_FLAG_1_IS_EDITABLE       = 1;

   /**
    * Switch to EDIT mode when editable and focus.
    */
   public static final int  STRING_FLAG_2_IMPLICIT_EDITABLE = 2;

   /**
    * Sizer type for width and height.
    * 
    * Sizing of {@link StringDrawable} will depend on sizers 
    * <li> {@link StringDrawable#setSizersPreset_1LineLong()}
    * <li> {@link StringDrawable#setSizersPreset_Free()}
    * <li> {@link StringDrawable#setSizersPreset_1LineAtWidth(int)}
    * <li> {@link StringDrawable#setSizersPreset_1CharPerLines(int)}
    * <li> {@link StringDrawable#setSizersPreset_TrimOnWidth(int)}
    * <li> {@link StringDrawable#setSizersPreset_FreeWidthYLines(int)}
    * and on the {@link IBOFigString} parameters
    * <li> {@link IBOFigString#FIG_STRING_OFFSET_07_WRAP_WIDTH1}
    * <li> {@link IBOFigString#FIG_STRING_OFFSET_08_WRAP_HEIGHT1}
    * <p>
    * This mode does not 
    * <li>No preset Shrinking flags set.
    * </p>
    */
   public static final int  PRESET_CONFIG_0_NONE            = 0;

   /**
    * Preset for titles.  one line of text trimmed at sizer width.
    * 
    * <li>No Scrolling {@link ITechViewDrawable#FLAG_GENE_28_NOT_SCROLLABLE}.
    * <li>When 0 width, shows the whole title
    * <li>In all cases, only show 1 line, thus ignores new lines
    * <li>Preset shrinks both w and H.
    * <br>
    * <br>
    * In EditMode draws letters while keeping caret visible with a small scrolling.
    */
   public static final int  PRESET_CONFIG_1_TITLE           = 1;

   /**
    * Force the arrangement of characters on a single line, this ignoring new lines characters.
    * <br>
    * <br>
    * External modules may implement scrolling.
    * <br>
    * <br>
    * Equivalent to
    * <li>Preset for <b>init(x,-1)</b>.
    * <li>Scrolling {@link ITechViewDrawable#FLAG_GENE_28_NOT_SCROLLABLE} 
    * <li>Shrinking vertically {@link ITechViewDrawable#FLAG_GENE_30_SHRINKABLE_H}
    * <li>Ignores new lines.
    * <li>top,bottom and vertical scrollbar pixels must be in expand mode
    */
   public static final int  PRESET_CONFIG_2_SCROLL_H        = 2;

   /**
    * String type that
    * Default is maximum of fully displayed lines given by the height parameter.
    * <br>
    * <br>
    * If the height parameter is negative, use that number to compute drawable area
    * <br>
    * <li>Shrink to fit. 
    * <li>No Partial. 
    * <li>Scrolling vertically is enabled.
    * <li>Shrinking vertically {@link ITechViewDrawable#FLAG_VSTATE_10_CONTENT_PW_VIEWPORT_DW}
    * <br>
    * <br>
    * In this type of String, the Width Sizer is overriden with {@link ISizer#ETALON_0_SIZEE_CTX}
    * <br>
    * If Cue width is {@link ISizer#ET_TYPE_0_PREFERRED_SIZE}, ScrollV takes parent drawn size or
    * ViewContext Drawn Size as preferred size.
    * Final backup size is ViewPort's width. That's the outer bound.
    */
   public static final int  PRESET_CONFIG_3_SCROLL_V        = 3;

   /**
    * Draws text like an Image. Scrolling Increments are pixels
    * Free Dimension constraint when width and height are set to 0. H and V Scrolling
    * Implementation:
    * When drawing, clipping is done at padding.
    * No Trim.
    */
   public static final int  PRESET_CONFIG_4_NATURAL_NO_WRAP = 4;

   /**
    * Fast char drawing. no messing with breaking code or anything else.
    * <br>
    * <br>
    * Init method just ask for Font Height and usual preferred width in the Text Style.
    * <br>
    * When Implicit 0,0 or negative
    * <li>Height of Drawable is decided by Font.
    * <li>Width is decided an etalon letter?
    */
   public static final int  PRESET_CONFIG_5_CHAR            = 5;

   public static final int  PRESET_CONFIG_MAX_CK            = 5;

   public static final int  PRESET_CONFIG_MAX_MODULO        = 6;

}
