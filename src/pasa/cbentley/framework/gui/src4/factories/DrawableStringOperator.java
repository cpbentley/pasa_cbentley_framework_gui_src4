package pasa.cbentley.framework.gui.src4.factories;

import pasa.cbentley.byteobjects.src4.core.BOAbstractOperator;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStringDrawable;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStringEdit;

public class DrawableStringOperator extends BOAbstractOperator implements IBOStringDrawable {

   protected final GuiCtx gc;

   public DrawableStringOperator(GuiCtx gc) {
      super(gc.getBOC());
      this.gc = gc;
   }

   public void linkStringTechs(StyleClass sc) {

      ByteObject strTech = gc.getDrawableStringFactory().getStringTech();
      sc.linkByteObject(strTech, IBOTypesGui.LINK_41_TECH_STRING);

      ByteObject strTechEdit = gc.getDrawableStringFactory().getStringEditTech();
      sc.linkByteObject(strTechEdit, IBOTypesGui.LINK_40_TECH_STRING_EDIT);

   }

   public void toStringTechEdit(ByteObject tech, Dctx sb) {
      sb.append("#TechStringEdit");
      sb.nl();
      sb.append(" CaretBlinkSpeed:" + tech.get1(IBOStringEdit.SEDIT_OFFSET_10_CARET_BLINK_SPEED_ON1));
      sb.nl();
      sb.append(" CaretPos:" + tech.get2(IBOStringEdit.SEDIT_OFFSET_04_CARET_POSITION2));
      sb.nl();
      sb.append(" CaretControlPos:" + tech.get1(IBOStringEdit.SEDIT_OFFSET_05_CONTROL_POSITION1));
      sb.nl();
      sb.append(" SpeedNumPad:" + tech.get1(IBOStringEdit.SEDIT_OFFSET_06_SPEED_NUMPAD1));
      sb.nl();
      sb.append(" PredictionNumVisible:" + tech.get1(IBOStringEdit.SEDIT_OFFSET_07_PRED_NUM_VISIBLE1));
      sb.nl();
      sb.append(" PredictionRealTime:" + tech.get1(IBOStringEdit.SEDIT_OFFSET_08_PRED_REAL_TIME1));
      sb.nl();
      sb.append(" SymbolTable:" + tech.get1(IBOStringEdit.SEDIT_OFFSET_09_SYMBOL_TABLE1));
   }

   public void toStringTech(ByteObject tech, Dctx sb) {
      sb.append("#StringTech ");
      int states = tech.get1(INPUT_OFFSET_01_FLAG);
      if (states != 0) {
         sb.append("States");
         if (tech.hasFlag(INPUT_OFFSET_01_FLAG, INPUT_FLAG_1_PASSWORD)) {
            sb.append("Password");
         }
      }
      sb.append(" StringType:" + tech.get1(INPUT_OFFSET_02_STRING_TYPE1));
      sb.append(" CharSetID:" + tech.get1(INPUT_OFFSET_03_CHARSET_ID1));
      sb.append(" InputType:" + tech.get1(INPUT_OFFSET_04_INPUT_TYPE1));
      sb.append(" MaxSize:" + tech.get1(INPUT_OFFSET_05_MAX_SIZE1));
      sb.append(" Mode:" + tech.get1(INPUT_OFFSET_06_MODE1));
      sb.append(" InitStyle:" + tech.get4(INPUT_OFFSET_07_INIT_STYLE_FLAGS4));

   }

}
