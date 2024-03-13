package pasa.cbentley.framework.gui.src4.scenario;

import pasa.cbentley.core.src4.ctx.ICtx;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.stator.IStatorable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;

public abstract class ScenarioItemAbstract implements IStatorable {
   protected final GuiCtx gc;

   public ScenarioItemAbstract(GuiCtx gc) {
      this.gc = gc;
   }
   
   public ICtx getCtxOwner() {
      return gc;
   }

   //#mdebug
   public IDLog toDLog() {
      return toStringGetUCtx().toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "ScenarioItemAbstract");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "ScenarioItemAbstract");
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return gc.getUC();
   }

   //#enddebug
   

}
