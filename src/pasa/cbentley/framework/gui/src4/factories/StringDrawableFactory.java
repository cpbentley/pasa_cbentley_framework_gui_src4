package pasa.cbentley.framework.gui.src4.factories;

import pasa.cbentley.byteobjects.src4.core.BOAbstractFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.objects.pointer.IBOMergeMask;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStringDrawable;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStringEdit;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;
import pasa.cbentley.framework.gui.src4.string.Symbs;
import pasa.cbentley.framework.gui.src4.tech.ITechStringDrawable;

public class StringDrawableFactory extends BOAbstractFactory implements IBOStringDrawable {

   protected final GuiCtx gc;

   public StringDrawableFactory(GuiCtx gc) {
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
      ByteObject bo = new ByteObject(boc, IBOTypesGui.TYPE_124_STRING_TECH, INPUT_BASIC_SIZE);
      bo.setValue(IBOStringDrawable.INPUT_OFFSET_03_CHARSET_ID1, charset, 1);
      
      gc.getDC().getMergeMaskFactory().setMergeMask(bo, IBOMergeMask.MERGE_MASK_OFFSET_5VALUES1, IBOMergeMask.MERGE_MASK_FLAG5_2);
      return bo;
   }

   public ByteObject getStringEditTechDef() {
      ByteObject tech = getStringEditTech();

      tech.setFlag(IBOStringEdit.SEDIT_OFFSET_01_FLAG, IBOStringEdit.SEDIT_FLAG_1_CARET_BG, false);

      tech.setFlag(IBOStringEdit.SEDIT_OFFSET_01_FLAG, IBOStringEdit.SEDIT_FLAG_2_CARET_BLINK, false);

      tech.setFlag(IBOStringEdit.SEDIT_OFFSET_01_FLAG, IBOStringEdit.SEDIT_FLAG_4_KB_PREDICTIVE, false);

      tech.setFlag(IBOStringEdit.SEDIT_OFFSET_01_FLAG, IBOStringEdit.SEDIT_FLAG_5_CAPS_ON, false);

      tech.setFlag(IBOStringEdit.SEDIT_OFFSET_01_FLAG, IBOStringEdit.SEDIT_FLAG_6_SAME_THREAD, false);

      tech.setValue(IBOStringEdit.SEDIT_OFFSET_10_CARET_BLINK_SPEED_ON1, 80, 1);
      tech.setValue(IBOStringEdit.SEDIT_OFFSET_11_CARET_BLINK_SPEED_OFF1, 60, 1);

      tech.setValue(IBOStringEdit.SEDIT_OFFSET_04_CARET_POSITION2, 0, 2);

      tech.setValue(IBOStringEdit.SEDIT_OFFSET_06_SPEED_NUMPAD1, 10, 1);

      tech.setValue(IBOStringEdit.SEDIT_OFFSET_05_CONTROL_POSITION1, 1, ITechStringDrawable.SEDIT_CONTROL_1_TOP);
      tech.setValue(IBOStringEdit.SEDIT_OFFSET_07_PRED_NUM_VISIBLE1, 4, 1);
      tech.setValue(IBOStringEdit.SEDIT_OFFSET_08_PRED_REAL_TIME1, 0, 1);
      tech.setValue(IBOStringEdit.SEDIT_OFFSET_09_SYMBOL_TABLE1, 0, 1);
      return tech;
   }

   /**
    * 
    * @return
    */
   public ByteObject getStringTech() {
      ByteObject bo = new ByteObject(boc, IBOTypesGui.TYPE_124_STRING_TECH, INPUT_BASIC_SIZE);
      return bo;
   }

   /**
    * <li>{@link ITechStringDrawable#TYPE_0_NONE}
    * <li>{@link ITechStringDrawable#TYPE_1_TITLE}
    * <li>{@link ITechStringDrawable#TYPE_2_SCROLL_H}
    * 
    * @param stringType
    * @return
    */
   public ByteObject getStringTech(int stringType) {
      ByteObject bo = new ByteObject(boc, IBOTypesGui.TYPE_124_STRING_TECH, INPUT_BASIC_SIZE);
      bo.setValue(IBOStringDrawable.INPUT_OFFSET_02_STRING_TYPE1, stringType, 1);
      return bo;
   }

   /**
    * 
    * @param stringType
    * @param mode {@link IBOStringDrawable#INPUT_OFFSET_06_MODE1}
    * @return
    */
   public ByteObject getStringTech(int stringType, int mode) {
      ByteObject bo = new ByteObject(boc, IBOTypesGui.TYPE_124_STRING_TECH, INPUT_BASIC_SIZE);
      bo.setValue(IBOStringDrawable.INPUT_OFFSET_02_STRING_TYPE1, stringType, 1);
      bo.setValue(IBOStringDrawable.INPUT_OFFSET_06_MODE1, mode, 1);
      return bo;
   }

   /**
    * Uses Symbs.CHAR_SET_0_DEFAULT for the char set/
    * 
    * @param stringType {@link IBOStringDrawable#INPUT_OFFSET_02_STRING_TYPE1}
    * @param mode 
    * <li>{@link ITechStringDrawable#MODE_0_READ}
    * <li>{@link ITechStringDrawable#MODE_1_SELECT}
    * <li>{@link ITechStringDrawable#MODE_2_EDIT}
    * @param maxSize {@link IBOStringDrawable#INPUT_OFFSET_05_MAX_SIZE1}
    * @param inputType {@link IBOStringDrawable#INPUT_OFFSET_04_INPUT_TYPE1}
    * <br>
    * <br>
    * @return
    */
   public ByteObject getStringTech(int stringType, int mode, int maxSize, int inputType) {
      return getStringTech(stringType, mode, maxSize, inputType, Symbs.CHAR_SET_0_DEFAULT);
   }

   public ByteObject getStringTechEdit(int stringType, int maxSize, int inputType) {
      return getStringTech(stringType, ITechStringDrawable.MODE_2_EDIT, maxSize, inputType, Symbs.CHAR_SET_0_DEFAULT);
   }

   /**
    * 
    * @param stringType {@link IBOStringDrawable#INPUT_OFFSET_02_STRING_TYPE1}
    * @param mode 
    * <li>{@link ITechStringDrawable#MODE_0_READ}
    * <li>{@link ITechStringDrawable#MODE_1_SELECT}
    * <li>{@link ITechStringDrawable#MODE_2_EDIT}
    * @param maxSize {@link IBOStringDrawable#INPUT_OFFSET_05_MAX_SIZE1}
    * @param inputType {@link IBOStringDrawable#INPUT_OFFSET_04_INPUT_TYPE1}
    * @param charSetID {@link IBOStringDrawable#INPUT_OFFSET_03_CHARSET_ID1}
    * @return
    */
   public ByteObject getStringTech(int stringType, int mode, int maxSize, int inputType, int charSetID) {
      ByteObject bo = new ByteObject(boc, IBOTypesGui.TYPE_124_STRING_TECH, INPUT_BASIC_SIZE);
      bo.setValue(INPUT_OFFSET_02_STRING_TYPE1, stringType, 1);
      bo.setValue(INPUT_OFFSET_03_CHARSET_ID1, charSetID, 1);
      bo.setValue(INPUT_OFFSET_04_INPUT_TYPE1, inputType, 1);
      bo.setValue(INPUT_OFFSET_05_MAX_SIZE1, maxSize, 1);
      bo.setValue(INPUT_OFFSET_06_MODE1, mode, 1);
      return bo;
   }

   /**
    * Returns {@link ByteObject} defining {@link StringDrawable} editing options
    * <br>
    * @return
    */
   public ByteObject getStringEditTech() {
      ByteObject bo = new ByteObject(boc, IBOTypesGui.TYPE_125_STRING_EDIT_TECH, IBOStringEdit.SEDIT_BASIC_SIZE);
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
   public ByteObject getDefaultStringEditTech() {
      ByteObject tech = getStringEditTech();

      boolean isCaretBg = true;
      tech.setFlag(IBOStringEdit.SEDIT_OFFSET_01_FLAG, IBOStringEdit.SEDIT_FLAG_1_CARET_BG, isCaretBg);

      boolean isCaretBlink = false;
      tech.setFlag(IBOStringEdit.SEDIT_OFFSET_01_FLAG, IBOStringEdit.SEDIT_FLAG_2_CARET_BLINK, isCaretBlink);

      boolean isPredOn = false;
      tech.setFlag(IBOStringEdit.SEDIT_OFFSET_01_FLAG, IBOStringEdit.SEDIT_FLAG_4_KB_PREDICTIVE, isPredOn);

      boolean isCapsOn = false;
      tech.setFlag(IBOStringEdit.SEDIT_OFFSET_01_FLAG, IBOStringEdit.SEDIT_FLAG_5_CAPS_ON, isCapsOn);

      tech.setValue(IBOStringEdit.SEDIT_OFFSET_10_CARET_BLINK_SPEED_ON1, 80, 1);
      tech.setValue(IBOStringEdit.SEDIT_OFFSET_11_CARET_BLINK_SPEED_OFF1, 60, 1);

      tech.setValue(IBOStringEdit.SEDIT_OFFSET_04_CARET_POSITION2, 0, 1);
      tech.setValue(IBOStringEdit.SEDIT_OFFSET_06_SPEED_NUMPAD1, 80, 1);

      tech.setValue(IBOStringEdit.SEDIT_OFFSET_05_CONTROL_POSITION1, 1, ITechStringDrawable.SEDIT_CONTROL_1_TOP);
      tech.setValue(IBOStringEdit.SEDIT_OFFSET_07_PRED_NUM_VISIBLE1, 4, 1);
      tech.setValue(IBOStringEdit.SEDIT_OFFSET_08_PRED_REAL_TIME1, 0, 1);
      tech.setValue(IBOStringEdit.SEDIT_OFFSET_09_SYMBOL_TABLE1, 0, 1);
      return tech;
   }

   /**
    * Return the String tech for editing a small title
    * @param ck
    * @return
    */
   public ByteObject getStringTechTable() {
      ByteObject is = getStringTech();

      is.setFlag(INPUT_OFFSET_01_FLAG, INPUT_FLAG_1_PASSWORD, false);
      is.setFlag(INPUT_OFFSET_01_FLAG, INPUT_FLAG_3_MAJ, false);

      is.setValue(INPUT_OFFSET_02_STRING_TYPE1, ITechStringDrawable.TYPE_1_TITLE, 1);

      is.setValue(INPUT_OFFSET_03_CHARSET_ID1, Symbs.CHAR_SET_0_DEFAULT, 1);

      is.setValue(INPUT_OFFSET_04_INPUT_TYPE1, ITechStringDrawable.INPUT_TYPE_0_ANY, 1);

      is.setValue(INPUT_OFFSET_05_MAX_SIZE1, 0, 1);

      is.setValue(INPUT_OFFSET_06_MODE1, ITechStringDrawable.MODE_2_EDIT, 1);

      int styleInit = ITechDrawable.STYLE_05_SELECTED | ITechDrawable.STYLE_03_MARKED;
      is.setValue(INPUT_OFFSET_07_INIT_STYLE_FLAGS4, styleInit, 4);

      return is;
   }

}
