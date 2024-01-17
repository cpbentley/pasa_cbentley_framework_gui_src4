package pasa.cbentley.framework.gui.src4.tech;

import pasa.cbentley.core.src4.interfaces.ITech;
import pasa.cbentley.framework.gui.src4.core.ViewDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechViewDrawable;
import pasa.cbentley.framework.gui.src4.string.EditModule;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;

public interface ITechStringDrawable extends ITech {

   public static final char DEFAULT_CARET_CHAR              = '>';

   /**
    * Delay for seperating strokes when a key is continuously pressed
    * This is normally controlled by InputConfig, but this component may override
    */
   public static final int  DEFAULT_DELAY_MULTIPLIER        = 5;

   /**
    * Any character
    */
   public static final int  INPUT_TYPE_0_ANY                = 0;

   /**
    * alpha numeric + @ and . and _
    */
   public static final int  INPUT_TYPE_1_EMAIL              = 1;

   /**
    * only integer
    */
   public static final int  INPUT_TYPE_2_NUMERIC            = 2;

   /**
    * integer with . for decimals
    */
   public static final int  INPUT_TYPE_3_DECIMAL            = 3;

   /**
    * alpha numeric + may start with http[s]://
    */
   public static final int  INPUT_TYPE_4_URL                = 4;

   /**
    * Date format
    */
   public static final int  INPUT_TYPE_5_DATE               = 5;

   public static final int  INPUT_TYPE_CK_MAX               = 5;

   /**
    * {@link StringDrawable}'s content cannot be edited or selected
    */
   public static final int  MODE_0_READ                     = 0;

   /**
    * {@link StringDrawable}'s {@link EditModule} only allows selection.
    */
   public static final int  MODE_1_SELECT                   = 1;

   /**
    * {@link StringDrawable}'s {@link EditModule} only allows both selection and edition.
    */
   public static final int  MODE_2_EDIT                     = 2;

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
    * No explicit type, recipient decides how to handle the String. width/height.
    * <br>
    * <br>
    * No overrides. Reads {@link ViewDrawable#initViewDrawable(int, int)} semantics and apply dimension constraint.
    * <li> <b>init(x,-1)</b> One line trimmed at width
    * <li> <b>init(-1,x)</b> X lines with only 1 characters
    * <li> <b>init(0,0)</b> Free
    * <li> <b>init(0,x)</b> Supports new lines
    * <li> <b>init(x,0)</b> Trim on width, show as many lines. not scrollable
    * <li> <b>init(0,-1)</b> One line as long as it needs to be
    * <br>
    * <li>No preset Shrinking flags set.
    */
   public static final int  TYPE_0_NONE                     = 0;

   /**
    * Title string: one line of text trimmed at width.
    * <br>
    * <li>No Scrolling {@link ITechViewDrawable#VIEW_GENE_28_NOT_SCROLLABLE}.
    * <li>When negative width, only shows X letters. Ignores the rest.
    * <li>When 0 width, shows the whole title
    * <li>In all cases, only show 1 line, thus ignores new lines
    * <li>Preset shrink
    * <br>
    * <br>
    * In EditMode draws letters while keeping caret visible with a small scrolling.
    */
   public static final int  TYPE_1_TITLE                    = 1;

   /**
    * Force the arrangement of characters on a single line, this ignoring new lines characters.
    * <br>
    * <br>
    * External modules may implement scrolling.
    * <br>
    * <br>
    * Equivalent to
    * <li>Preset for <b>init(x,-1)</b>.
    * <li>Scrolling {@link ITechViewDrawable#VIEW_GENE_28_NOT_SCROLLABLE} 
    * <li>Shrinking vertically {@link ITechViewDrawable#VIEW_GENE_30_SHRINKABLE_H}
    * <li>Ignores new lines.
    * <li>top,bottom and vertical scrollbar pixels must be in expand mode
    */
   public static final int  TYPE_2_SCROLL_H                 = 2;

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
    * <li>Shrinking vertically {@link ITechViewDrawable#VIEWSTATE_10_CONTENT_PW_VIEWPORT_DW}
    * <br>
    * <br>
    * In this type of String, the Width Sizer is overriden with {@link ISizer#ETALON_0_SIZEE_CTX}
    * <br>
    * If Cue width is {@link ISizer#ET_TYPE_0_PREFERRED_SIZE}, ScrollV takes parent drawn size or
    * ViewContext Drawn Size as preferred size.
    * Final backup size is ViewPort's width. That's the outer bound.
    */
   public static final int  TYPE_3_SCROLL_V                 = 3;

   /**
    * Draws text like an Image. Scrolling Increments are pixels
    * Free Dimension constraint when width and height are set to 0. H and V Scrolling
    * Implementation:
    * When drawing, clipping is done at padding.
    * No Trim.
    */
   public static final int  TYPE_4_NATURAL_NO_WRAP          = 4;

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
   public static final int  TYPE_5_CHAR                     = 5;

   public static final int  TYPE_CK_MAX                     = 5;

   public static final int  TYPE_MODULO_MAX                 = 6;

}
