package pasa.cbentley.framework.gui.src4.scenario;

import pasa.cbentley.core.src4.ctx.ICtx;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.stator.IStatorable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ObjectGC;

public abstract class ScenarioItemAbstract extends ObjectGC implements IStatorable {

   public ScenarioItemAbstract(GuiCtx gc) {
      super(gc);
   }

   public ICtx getCtxOwner() {
      return gc;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, ScenarioItemAbstract.class, 25);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ScenarioItemAbstract.class, 25);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }
   //#enddebug

}
