package pasa.cbentley.framework.gui.src4.factories;

import pasa.cbentley.byteobjects.src4.core.BOAbstractFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.objects.pointer.IBOMergeMask;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStringData;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStringEdit;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;
import pasa.cbentley.framework.gui.src4.string.Symbs;
import pasa.cbentley.framework.gui.src4.tech.ITechStringDrawable;

public class DrawableStringFactory extends BOAbstractFactory implements IBOStringData, IBOStringEdit {

   protected final GuiCtx gc;

   public DrawableStringFactory(GuiCtx gc) {
      super(gc.getBOC());
      this.gc = gc;
   }

   /**
    * Create a Transparent definition of a String tech input param just defining the charset
    * <br>
    * <br>
    * @param ctype0English
    * @return
    */
   public ByteObject getStringTechTCharSet(int charset) {
      ByteObject bo = new ByteObject(boc, IBOTypesGui.TYPE_124_STRING_TECH, SDATA_BASIC_SIZE);
      bo.set1(IBOStringData.SDATA_OFFSET_03_CHARSET_ID1, charset);
      
      gc.getDC().getMergeMaskFactory().setMergeMask(bo, IBOMergeMask.MERGE_MASK_OFFSET_5VALUES1, IBOMergeMask.MERGE_MASK_FLAG5_2);
      return bo;
   }

   public ByteObject getBOEditDefault() {
      ByteObject tech = getBOEditEmpty();

      tech.setFlag(SEDIT_OFFSET_01_FLAG, SEDIT_FLAG_1_CARET_BG, false);

      tech.setFlag(SEDIT_OFFSET_01_FLAG, SEDIT_FLAG_2_CARET_BLINK, false);

      tech.setFlag(SEDIT_OFFSET_01_FLAG, SEDIT_FLAG_4_KB_PREDICTIVE, false);

      tech.setFlag(SEDIT_OFFSET_01_FLAG, SEDIT_FLAG_5_CAPS_ON, false);

      tech.setFlag(SEDIT_OFFSET_01_FLAG, SEDIT_FLAG_6_SAME_THREAD, false);

      tech.set1(SEDIT_OFFSET_10_CARET_BLINK_SPEED_ON1, 80);
      tech.set1(SEDIT_OFFSET_11_CARET_BLINK_SPEED_OFF1, 60);

      tech.set2(SEDIT_OFFSET_04_CARET_POSITION2, 0);

      tech.set1(SEDIT_OFFSET_06_SPEED_NUMPAD1, 10);

      tech.set1(SEDIT_OFFSET_05_CONTROL_POSITION1, ITechStringDrawable.SEDIT_CONTROL_1_TOP);
      tech.set1(SEDIT_OFFSET_07_PRED_NUM_VISIBLE1, 4);
      tech.set1(SEDIT_OFFSET_08_PRED_REAL_TIME1, 0);
      tech.set1(SEDIT_OFFSET_09_SYMBOL_TABLE1, 0);
      return tech;
   }

   /**
    * 
    * @return
    */
   public ByteObject getStringTech() {
      ByteObject bo = new ByteObject(boc, IBOTypesGui.TYPE_124_STRING_TECH, SDATA_BASIC_SIZE);
      return bo;
   }

   /**
    * <li>{@link ITechStringDrawable#PRESET_CONFIG_0_NONE}
    * <li>{@link ITechStringDrawable#PRESET_CONFIG_1_TITLE}
    * <li>{@link ITechStringDrawable#PRESET_CONFIG_2_SCROLL_H}
    * 
    * @param stringType
    * @return
    */
   public ByteObject getStringTech(int stringType) {
      ByteObject bo = new ByteObject(boc, IBOTypesGui.TYPE_124_STRING_TECH, SDATA_BASIC_SIZE);
      bo.set1(IBOStringData.SDATA_OFFSET_02_PRESET_CONFIG1, stringType);
      return bo;
   }

   /**
    * 
    * @param stringType
    * @param mode {@link IBOStringData#SDATA_OFFSET_06_ACTION_MODE1}
    * @return
    */
   public ByteObject getStringTech(int stringType, int mode) {
      ByteObject bo = new ByteObject(boc, IBOTypesGui.TYPE_124_STRING_TECH, SDATA_BASIC_SIZE);
      bo.set1(IBOStringData.SDATA_OFFSET_02_PRESET_CONFIG1, stringType);
      bo.set1(IBOStringData.SDATA_OFFSET_06_ACTION_MODE1, mode);
      return bo;
   }

   /**
    * Uses Symbs.CHAR_SET_0_DEFAULT for the char set/
    * 
    * <p>
    * <li>{@link ITechStringDrawable#S_ACTION_MODE_0_READ}
    * <li>{@link ITechStringDrawable#S_ACTION_MODE_1_SELECT}
    * <li>{@link ITechStringDrawable#S_ACTION_MODE_2_EDIT}
    * </p>
    * 
    * @param stringType {@link IBOStringData#SDATA_OFFSET_02_PRESET_CONFIG1}
    * @param mode {@link IBOStringData#SDATA_OFFSET_06_ACTION_MODE1}
    * @param maxSize {@link IBOStringData#SDATA_OFFSET_05_MAX_SIZE1}
    * @param inputType {@link IBOStringData#SDATA_OFFSET_04_DATA_TYPE1}
    * <br>
    * <br>
    * @return
    */
   public ByteObject getStringTech(int stringType, int mode, int maxSize, int inputType) {
      return getStringTech(stringType, mode, maxSize, inputType, Symbs.CHAR_SET_0_DEFAULT);
   }

   public ByteObject getStringTechEdit(int stringType, int maxSize, int inputType) {
      return getStringTech(stringType, ITechStringDrawable.S_ACTION_MODE_2_EDIT, maxSize, inputType, Symbs.CHAR_SET_0_DEFAULT);
   }

   /**
    * 
    * @param stringType {@link IBOStringData#SDATA_OFFSET_02_PRESET_CONFIG1}
    * @param mode 
    * <li>{@link ITechStringDrawable#S_ACTION_MODE_0_READ}
    * <li>{@link ITechStringDrawable#S_ACTION_MODE_1_SELECT}
    * <li>{@link ITechStringDrawable#S_ACTION_MODE_2_EDIT}
    * @param maxSize {@link IBOStringData#SDATA_OFFSET_05_MAX_SIZE1}
    * @param inputType {@link IBOStringData#SDATA_OFFSET_04_DATA_TYPE1}
    * @param charSetID {@link IBOStringData#SDATA_OFFSET_03_CHARSET_ID1}
    * @return
    */
   public ByteObject getStringTech(int stringType, int mode, int maxSize, int inputType, int charSetID) {
      ByteObject bo = new ByteObject(boc, IBOTypesGui.TYPE_124_STRING_TECH, SDATA_BASIC_SIZE);
      bo.set1(SDATA_OFFSET_02_PRESET_CONFIG1, stringType);
      bo.set1(SDATA_OFFSET_03_CHARSET_ID1, charSetID);
      bo.set1(SDATA_OFFSET_04_DATA_TYPE1, inputType);
      bo.set1(SDATA_OFFSET_05_MAX_SIZE1, maxSize);
      bo.set1(SDATA_OFFSET_06_ACTION_MODE1, mode);
      return bo;
   }

   /**
    * Returns {@link ByteObject} defining {@link StringDrawable} editing options
    * <br>
    * @return
    */
   public ByteObject getBOEditEmpty() {
      ByteObject bo = new ByteObject(boc, IBOTypesGui.TYPE_125_STRING_EDIT_TECH, SEDIT_BASIC_SIZE);
      return bo;
   }

   /**
    * The default tech maybe modified externally without other people knowing about it!
    * @return
    */
   public ByteObject getDefaultStringTech() {
      return getStringTech();
   }

   /**
    * 
    * @return
    */
   public ByteObject getBOEditNormal() {
      ByteObject tech = getBOEditEmpty();

      boolean isCaretBg = true;
      tech.setFlag(SEDIT_OFFSET_01_FLAG, SEDIT_FLAG_1_CARET_BG, isCaretBg);

      boolean isCaretBlink = false;
      tech.setFlag(SEDIT_OFFSET_01_FLAG, SEDIT_FLAG_2_CARET_BLINK, isCaretBlink);

      boolean isPredOn = false;
      tech.setFlag(SEDIT_OFFSET_01_FLAG, SEDIT_FLAG_4_KB_PREDICTIVE, isPredOn);

      boolean isCapsOn = false;
      tech.setFlag(SEDIT_OFFSET_01_FLAG, SEDIT_FLAG_5_CAPS_ON, isCapsOn);

      tech.set1(SEDIT_OFFSET_10_CARET_BLINK_SPEED_ON1, 80);
      tech.set1(SEDIT_OFFSET_11_CARET_BLINK_SPEED_OFF1, 60);

      tech.set2(SEDIT_OFFSET_04_CARET_POSITION2, 0);
      tech.set1(SEDIT_OFFSET_06_SPEED_NUMPAD1, 80);

      tech.set1(SEDIT_OFFSET_05_CONTROL_POSITION1, ITechStringDrawable.SEDIT_CONTROL_1_TOP);
      tech.set1(SEDIT_OFFSET_07_PRED_NUM_VISIBLE1, 4);
      tech.set1(SEDIT_OFFSET_08_PRED_REAL_TIME1, 0);
      tech.set1(SEDIT_OFFSET_09_SYMBOL_TABLE1, 0);
      return tech;
   }

   /**
    * Return the String tech for editing a small title
    * @param ck
    * @return
    */
   public ByteObject getStringTechTable() {
      ByteObject is = getStringTech();

      is.setFlag(SDATA_OFFSET_01_FLAG, SDATA_FLAG_1_PASSWORD, false);
      is.setFlag(SDATA_OFFSET_01_FLAG, SDATA_FLAG_3_MAJ, false);

      is.set1(SDATA_OFFSET_02_PRESET_CONFIG1, ITechStringDrawable.PRESET_CONFIG_1_TITLE);

      is.set1(SDATA_OFFSET_03_CHARSET_ID1, Symbs.CHAR_SET_0_DEFAULT);

      is.set1(SDATA_OFFSET_04_DATA_TYPE1, ITechStringDrawable.S_DATA_0_ANY);

      is.set1(SDATA_OFFSET_05_MAX_SIZE1, 0);

      is.set1(SDATA_OFFSET_06_ACTION_MODE1, ITechStringDrawable.S_ACTION_MODE_2_EDIT);

      int styleInit = ITechDrawable.STYLE_05_SELECTED | ITechDrawable.STYLE_03_MARKED;
      is.set4(SDATA_OFFSET_07_INIT_STYLE_FLAGS4, styleInit);

      return is;
   }

}
