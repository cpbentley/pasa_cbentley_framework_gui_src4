package pasa.cbentley.framework.gui.src4.factories.interfaces;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.framework.gui.src4.core.ViewDrawable;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.string.StringEditModule;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;
import pasa.cbentley.framework.gui.src4.string.Symbs;
import pasa.cbentley.framework.gui.src4.tech.ITechStringDrawable;

/**
 * Defines
 * 
 * <li> {@link IBOTypesGui#TYPE_124_STRING_DATA} data related options
 * <li> {@link IBOTypesGui#TYPE_125_STRING_EDIT_TECH} visual/technical options for edition
 * 
 * 
 * When a {@link StringDrawable} goes in edition mode, edition parameters are loaded by the {@link StringEditModule}.
 * 
 * @see IBOStringEdit
 * @author Charles-Philip Bentley
 *
 */
public interface IBOStringData extends IByteObject {

   /**
    * 10 bytes
    */
   public static final int SDATA_BASIC_SIZE                  = A_OBJECT_BASIC_SIZE + 10;

   /**
    * Flag string as password.
    */
   public static final int SDATA_FLAG_1_PASSWORD             = 1 << 0;

   /**
    * Data must not be saved.
    */
   public static final int SDATA_FLAG_2_SENSITIVE_DATA       = 1 << 1;

   /**
    * Show capital letters only
    */
   public static final int SDATA_FLAG_3_MAJ                  = 1 << 2;

   /**
    * Info defines a specific {@link IBOTypesGui#TYPE_125_STRING_EDIT_TECH}
    */
   public static final int SDATA_FLAG_4_EDIT_SPECS           = 1 << 3;

   /**
    * When set, {@link StringDrawable} switch to the edit mode when it recieves {@link ITechDrawable#STYLE_06_FOCUSED_KEY}
    */
   public static final int SDATA_FLAG_5_AUTO_EDIT_MODE       = 1 << 4;

   /**
    * Special root
    */
   public static final int SDATA_FLAG_6_KEEP_ARRAY           = 1 << 5;

   /**
    * All words start with a capital letter
    */
   public static final int SDATA_FLAG_7_AUTO_CAP_WORD        = 1 << 6;

   /**
    * All sentence start with a capital letter automatically
    */
   public static final int SDATA_FLAG_8_AUTO_CAP_SENTENCE    = 1 << 7;

   public static final int SDATA_OFFSET_01_FLAG              = A_OBJECT_BASIC_SIZE;

   /**
    * Arrangement of characters in the string relative to a rectangular area and the {@link ISizer} constraints.
    * Override string type from String figure.
    * 
    * <li>{@link ITechStringDrawable#PRESET_CONFIG_0_NONE}
    * <li>{@link ITechStringDrawable#PRESET_CONFIG_1_TITLE}
    * <li>{@link ITechStringDrawable#PRESET_CONFIG_2_SCROLL_H}
    * <li>{@link ITechStringDrawable#PRESET_CONFIG_3_SCROLL_V}
    * <li>{@link ITechStringDrawable#PRESET_CONFIG_4_NATURAL_NO_WRAP}
    * 
    * <p>
    * 
    * A String of Type {@link ITechStringDrawable#PRESET_CONFIG_3_SCROLL_V} will take the cue width
    * and break the String on it. If Cue String of width sizer is {@link ISizer#ET_TYPE_0_PREFERRED_SIZE}
    * The break will occur on a new line.
    * </p>
    * 
    * <p>
    * A String Title will take the String and trim it at the cue width.
    * A logic sizer height of 2 will trim at the second line.
    * </p>
    */
   public static final int SDATA_OFFSET_02_PRESET_CONFIG1      = A_OBJECT_BASIC_SIZE + 1;

   /**
    * Default Language Character set for the edition of the String.
    * ID of charset
    * <li> {@link Symbs#CHAR_SET_0_DEFAULT}
    * <li> {@link Symbs#CHAR_SET_3_EN}
    * <li> {@link Symbs#CHAR_SET_1_Fr}
    * <li> {@link Symbs#CHAR_SET_2_Ru}
    * <br>
    * <br>
    * What about localization? When 0, the default is used and it is not forced
    */
   public static final int SDATA_OFFSET_03_CHARSET_ID1       = A_OBJECT_BASIC_SIZE + 2;

   /**
    * Constraint on data input
    * <li> {@link ITechStringDrawable#S_DATA_0_ANY}
    * <li> {@link ITechStringDrawable#S_DATA_1_EMAIL}
    * <li> {@link ITechStringDrawable#S_DATA_2_NUMERIC}
    * <li> {@link ITechStringDrawable#S_DATA_3_DECIMAL}
    * 
    */
   public static final int SDATA_OFFSET_04_DATA_TYPE1        = A_OBJECT_BASIC_SIZE + 3;

   /**
    * 1-255 size of string. 0 for infinity.
    * <br>
    * Checked during initialization and by {@link StringEditModule} in edition mode.
    */
   public static final int SDATA_OFFSET_05_MAX_SIZE1         = A_OBJECT_BASIC_SIZE + 4;

   /**
    * Defines whether the {@link StringDrawable} can be selected or edited.
    * 
    * This is a hardcoded genetic trait. that can be changed on a cloned {@link ByteObject} so as to not
    * changed the trait for other StringDrawable using the byteobject definition.
    * <li> {@link ITechStringDrawable#S_ACTION_MODE_0_READ}
    * <li> {@link ITechStringDrawable#S_ACTION_MODE_1_SELECT}
    * <li> {@link ITechStringDrawable#S_ACTION_MODE_2_EDIT}
    * 
    */
   public static final int SDATA_OFFSET_06_ACTION_MODE1      = A_OBJECT_BASIC_SIZE + 5;

   /**
    * Styles flags such as {@link ITechDrawable#STYLE_05_SELECTED} to be used when computing {@link StringDrawable} preferred size.
    * 
    * Discussion: What should be done when a style modifies the structure?
    * <li>Do nothing
    * <li>Recompute preferred dimension, possibly the whole canvas.
    */
   public static final int SDATA_OFFSET_07_INIT_STYLE_FLAGS4 = A_OBJECT_BASIC_SIZE + 6;

   public static final int SDATA_OFFSET_08_                  = A_OBJECT_BASIC_SIZE + 10;

}
