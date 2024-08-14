package pasa.cbentley.framework.gui.src4.factories;

import pasa.cbentley.byteobjects.src4.core.BOAbstractFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.objects.pointer.IBOMerge;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.drawx.src4.ctx.IBOTypesDrawX;
import pasa.cbentley.framework.drawx.src4.string.interfaces.IBOStrAux;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStrAuxData;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStrAuxEdit;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStrAuxGlobal;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStrAuxPrediction;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;
import pasa.cbentley.framework.gui.src4.string.Symbs;
import pasa.cbentley.framework.gui.src4.tech.ITechStringDrawable;

public class DrawableStringFactory extends BOAbstractFactory implements IBOStrAuxData, IBOStrAuxEdit {

   protected final GuiCtx gc;

   public DrawableStringFactory(GuiCtx gc) {
      super(gc.getBOC());
      this.gc = gc;
   }

   public ByteObject createStrAuxData() {
      ByteObject p = getBOFactory().createByteObject(IBOTypesDrawX.TYPE_DRWX_07_STRING_AUX, SDATA_BASIC_SIZE);
      p.set1(STR_AUX_OFFSET_1_EXT_TYPE1, IBOTypesGui.TYPE_DRWX_07_STRING_AUX_0_DATA);
      return p;
   }

   /**
    * {@link IBOStrAuxEdit}
    * 
    * @return
    */
   public ByteObject createStrAuxEdit() {
      ByteObject p = getBOFactory().createByteObject(IBOTypesDrawX.TYPE_DRWX_07_STRING_AUX, IBOStrAuxEdit.SEDIT_BASIC_SIZE);
      p.set1(IBOStrAux.STR_AUX_OFFSET_1_EXT_TYPE1, IBOTypesGui.TYPE_DRWX_07_STRING_AUX_1_EDIT);
      return p;
   }

   public ByteObject createStrAuxGlobal() {
      ByteObject p = getBOFactory().createByteObject(IBOTypesDrawX.TYPE_DRWX_07_STRING_AUX, IBOStrAuxGlobal.GLOABAL_BASIC_SIZE);
      p.set1(STR_AUX_OFFSET_1_EXT_TYPE1, IBOTypesGui.TYPE_DRWX_07_STRING_AUX_3_GLOBAL);
      return p;
   }

   public ByteObject createStrAuxPrediction() {
      ByteObject p = getBOFactory().createByteObject(IBOTypesDrawX.TYPE_DRWX_07_STRING_AUX, IBOStrAuxPrediction.PREDICTION_BASIC_SIZE);
      p.set1(STR_AUX_OFFSET_1_EXT_TYPE1, IBOTypesGui.TYPE_DRWX_07_STRING_AUX_2_PREDICTION);
      return p;
   }

   /**
    * Creates an {@link IBOStrAuxData} with provided preset config.
    * 
    * <li>{@link ITechStringDrawable#PRESET_CONFIG_0_NONE}
    * <li>{@link ITechStringDrawable#PRESET_CONFIG_1_TITLE}
    * <li>{@link ITechStringDrawable#PRESET_CONFIG_2_SCROLL_H}
    * 
    * @param preset
    * @return
    */
   public ByteObject getBOStringData(int preset) {
      ByteObject bo = createStrAuxData();
      bo.set1(IBOStrAuxData.SDATA_OFFSET_02_PRESET_CONFIG1, preset);
      return bo;
   }

   /**
    * 
    * <li> {@link ITechStringDrawable#S_ACTION_MODE_0_READ}
    * <li> {@link ITechStringDrawable#S_ACTION_MODE_1_SELECT}
    * <li> {@link ITechStringDrawable#S_ACTION_MODE_2_EDIT}
    * @param stringType
    * @param mode {@link IBOStrAuxData#SDATA_OFFSET_06_ACTION_MODE1}
    * @return
    */
   public ByteObject getBOStringData(int stringType, int mode) {
      ByteObject bo = createStrAuxData();
      bo.set1(IBOStrAuxData.SDATA_OFFSET_02_PRESET_CONFIG1, stringType);
      bo.set1(IBOStrAuxData.SDATA_OFFSET_06_ACTION_MODE1, mode);
      return bo;
   }

   /**
    * 
    * @param stringType
    * @param maxSize
    * @param inputType
    * @return
    */
   public ByteObject getBOStringData(int stringType, int maxSize, int inputType) {
      return getBOStringData(stringType, ITechStringDrawable.S_ACTION_MODE_2_EDIT, maxSize, inputType, Symbs.CHAR_SET_0_DEFAULT);
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
    * @param presetConfig {@link IBOStrAuxData#SDATA_OFFSET_02_PRESET_CONFIG1}
    * @param mode {@link IBOStrAuxData#SDATA_OFFSET_06_ACTION_MODE1}
    * @param maxSize {@link IBOStrAuxData#SDATA_OFFSET_05_MAX_SIZE1}
    * @param inputType {@link IBOStrAuxData#SDATA_OFFSET_04_DATA_TYPE1}
    * <br>
    * <br>
    * @return
    */
   public ByteObject getBOStringData(int presetConfig, int mode, int maxSize, int inputType) {
      return getBOStringData(presetConfig, mode, maxSize, inputType, Symbs.CHAR_SET_0_DEFAULT);
   }

   /**
    * 
    * @param stringType {@link IBOStrAuxData#SDATA_OFFSET_02_PRESET_CONFIG1}
    * @param mode 
    * <li>{@link ITechStringDrawable#S_ACTION_MODE_0_READ}
    * <li>{@link ITechStringDrawable#S_ACTION_MODE_1_SELECT}
    * <li>{@link ITechStringDrawable#S_ACTION_MODE_2_EDIT}
    * @param maxSize {@link IBOStrAuxData#SDATA_OFFSET_05_MAX_SIZE1}
    * @param inputType {@link IBOStrAuxData#SDATA_OFFSET_04_DATA_TYPE1}
    * @param charSetID {@link IBOStrAuxData#SDATA_OFFSET_03_CHARSET_ID1}
    * @return
    */
   public ByteObject getBOStringData(int stringType, int mode, int maxSize, int inputType, int charSetID) {
      ByteObject bo = createStrAuxData();
      bo.set1(SDATA_OFFSET_02_PRESET_CONFIG1, stringType);
      bo.set1(SDATA_OFFSET_03_CHARSET_ID1, charSetID);
      bo.set1(SDATA_OFFSET_04_DATA_TYPE1, inputType);
      bo.set1(SDATA_OFFSET_05_MAX_SIZE1, maxSize);
      bo.set1(SDATA_OFFSET_06_ACTION_MODE1, mode);
      return bo;
   }

   /**
    * The default tech maybe modified externally without other people knowing about it!
    * @return
    */
   public ByteObject getBOStringDataDefault() {
      return getBOStringDataEmpty();
   }

   /**
    * 
    * @return
    */
   public ByteObject getBOStringDataEmpty() {
      return createStrAuxData();
   }

   /**
    * Return the String tech for editing a small title
    * @param ck
    * @return
    */
   public ByteObject getBOStringDataSmallTitle() {
      ByteObject is = getBOStringDataEmpty();

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

   /**
    * Create a Transparent definition of a String tech input param just defining the charset.
    * 
    * @param charset value for {@link IBOStrAuxData#SDATA_OFFSET_03_CHARSET_ID1}
    * @return
    */
   public ByteObject getBOStringDataTransparentCharSet(int charset) {
      ByteObject bo = createStrAuxData();
      bo.set1(IBOStrAuxData.SDATA_OFFSET_03_CHARSET_ID1, charset);

      gc.getDC().getMergeMaskFactory().setMergeMask(bo, IBOMerge.MERGE_MASK_OFFSET_05_VALUES1, IBOMerge.MERGE_MASK_FLAG5_2);
      return bo;
   }

   public ByteObject getBOStringEditDefault() {
      ByteObject tech = getBOStringEditEmpty();

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
    * Returns {@link ByteObject} defining {@link StringDrawable} editing options
    * <br>
    * @return
    */
   public ByteObject getBOStringEditEmpty() {
      return createStrAuxEdit();
   }

   /**
    * 
    * @return
    */
   public ByteObject getBOStringEditNormal() {
      ByteObject tech = getBOStringEditEmpty();

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

   public boolean toStringStrAux(ByteObject bo, Dctx dc) {
      final int subType = bo.get1(IBOStrAux.STR_AUX_OFFSET_1_EXT_TYPE1);

      switch (subType) {
         case IBOTypesGui.TYPE_DRWX_07_STRING_AUX_0_DATA:
            toStringTech(bo, dc);
            break;
         case IBOTypesGui.TYPE_DRWX_07_STRING_AUX_1_EDIT:
            toStringTechEdit(bo, dc);
            break;
         case IBOTypesGui.TYPE_DRWX_07_STRING_AUX_2_PREDICTION:
            toStringStrAuxPrediction(bo, dc);
            break;
         case IBOTypesGui.TYPE_DRWX_07_STRING_AUX_3_GLOBAL:
            toStringStrAuxGlobal(bo, dc);
            break;
         default:
            //subtype is not known by this module
            return false;
      }
      return true;
   }

   public boolean toStringStrAux1Line(ByteObject bo, Dctx dc) {
      final int subType = bo.get1(IBOStrAux.STR_AUX_OFFSET_1_EXT_TYPE1);

      switch (subType) {
         case IBOTypesGui.TYPE_DRWX_07_STRING_AUX_0_DATA:
            toStringTech(bo, dc);
            break;
         case IBOTypesGui.TYPE_DRWX_07_STRING_AUX_1_EDIT:
            toStringTechEdit(bo, dc);
            break;
         case IBOTypesGui.TYPE_DRWX_07_STRING_AUX_2_PREDICTION:
            toStringStrAuxPrediction(bo, dc);
            break;
         case IBOTypesGui.TYPE_DRWX_07_STRING_AUX_3_GLOBAL:
            toStringStrAuxGlobal(bo, dc);
            break;
         default:
            //subtype is not known by this module
            return false;
      }
      return true;
   }

   /**
    * {@link IBOStrAuxGlobal} 
    * @param bo
    * @param dc
    */
   private void toStringStrAuxGlobal(ByteObject bo, Dctx dc) {
      dc.rootN(bo, "IBOStrAuxGlobal", DrawableStringFactory.class, 317);
      int numvisible = bo.get1(IBOStrAuxGlobal.GLOBAL_OFFSET_01_);
      dc.appendVarWithNewLine("numvisible", numvisible);
   }

   /**
    * {@link IBOStrAuxPrediction} 
    * @param bo
    * @param dc
    */
   private void toStringStrAuxPrediction(ByteObject bo, Dctx dc) {
      dc.rootN(bo, "IBOStrAuxPrediction", DrawableStringFactory.class, 317);
      int numvisible = bo.get1(IBOStrAuxPrediction.PREDICTION_OFFSET_01_PRED_NUM_VISIBLE1);
      dc.appendVarWithNewLine("numvisible", numvisible);
   }

   /**
    * {@link IBOStrAuxData} 
    * @param bo
    * @param dc
    */
   public void toStringTech(ByteObject bo, Dctx dc) {
      dc.rootN(bo, "IBOStrAuxData", DrawableStringFactory.class, 317);

      int states = bo.get1(SDATA_OFFSET_01_FLAG);
      if (states != 0) {
         dc.append("States");
         if (bo.hasFlag(SDATA_OFFSET_01_FLAG, SDATA_FLAG_1_PASSWORD)) {
            dc.append("Password");
         }
      }
      dc.append(" StringType:" + bo.get1(SDATA_OFFSET_02_PRESET_CONFIG1));
      dc.append(" CharSetID:" + bo.get1(SDATA_OFFSET_03_CHARSET_ID1));
      dc.append(" InputType:" + bo.get1(SDATA_OFFSET_04_DATA_TYPE1));
      dc.append(" MaxSize:" + bo.get1(SDATA_OFFSET_05_MAX_SIZE1));
      dc.append(" Mode:" + bo.get1(SDATA_OFFSET_06_ACTION_MODE1));
      dc.append(" InitStyle:" + bo.get4(SDATA_OFFSET_07_INIT_STYLE_FLAGS4));

   }

   /**
    * {@link IBOStrAuxEdit} 
    * @param bo
    * @param dc
    */
   public void toStringTechEdit(ByteObject bo, Dctx dc) {
      dc.rootN(bo, "IBOStrAuxEdit", DrawableStringFactory.class, 317);

      int caretBlinkSpeed = bo.get1(IBOStrAuxEdit.SEDIT_OFFSET_10_CARET_BLINK_SPEED_ON1);
      dc.appendVarWithNewLine("caretBlinkSpeed", caretBlinkSpeed);

      dc.append(" CaretPos:" + bo.get2(IBOStrAuxEdit.SEDIT_OFFSET_04_CARET_POSITION2));
      dc.nl();
      dc.append(" CaretControlPos:" + bo.get1(IBOStrAuxEdit.SEDIT_OFFSET_05_CONTROL_POSITION1));
      dc.nl();
      dc.append(" SpeedNumPad:" + bo.get1(IBOStrAuxEdit.SEDIT_OFFSET_06_SPEED_NUMPAD1));
      dc.nl();
      dc.append(" PredictionNumVisible:" + bo.get1(IBOStrAuxEdit.SEDIT_OFFSET_07_PRED_NUM_VISIBLE1));
      dc.nl();
      dc.append(" PredictionRealTime:" + bo.get1(IBOStrAuxEdit.SEDIT_OFFSET_08_PRED_REAL_TIME1));
      dc.nl();
      dc.append(" SymbolTable:" + bo.get1(IBOStrAuxEdit.SEDIT_OFFSET_09_SYMBOL_TABLE1));
   }
}
