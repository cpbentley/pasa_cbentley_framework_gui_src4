package pasa.cbentley.framework.gui.src4.creator;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStringData;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStringEdit;
import pasa.cbentley.framework.gui.src4.tech.ITechStringDrawable;

public abstract class CreatorAbstractString extends CreatorAbstract {

   public CreatorAbstractString(CreateContext gc) {
      super(gc);
   }

   
   public ByteObject getTechString() {
      ByteObject tech = new ByteObject(boc, new byte[IBOStringData.SDATA_BASIC_SIZE], 0);
      int stringType = ITechStringDrawable.PRESET_CONFIG_1_TITLE;
      int stringMode = ITechStringDrawable.S_ACTION_MODE_0_READ;
      tech.set1(SDATA_OFFSET_02_PRESET_CONFIG1, stringType);
      tech.set1(SDATA_OFFSET_06_ACTION_MODE1, stringMode);

      return tech;
   }

   public ByteObject getTechStringEdit() {
      ByteObject tech = new ByteObject(boc, new byte[IBOStringEdit.SEDIT_BASIC_SIZE], 0);

      int symbolTable = 1;
      tech.set1(IBOStringEdit.SEDIT_OFFSET_09_SYMBOL_TABLE1, symbolTable);
      return tech;
   }
}
