package pasa.cbentley.framework.gui.src4.creator;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;

public abstract class CreatorAbstractTable extends CreatorAbstract {

   public CreatorAbstractTable(CreateContext gc) {
      super(gc);
   }

   public abstract ByteObject getTableTech();
  
}
