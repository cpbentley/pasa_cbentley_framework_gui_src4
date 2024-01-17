package pasa.cbentley.framework.gui.src4.ctx;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;

public class ObjectGui implements IStringable {

   protected final GuiCtx gc;

   protected final CmdCtx cc;

   public ObjectGui(GuiCtx gc) {
      this.gc = gc;
      this.cc = gc.getCC();
   }

   public GuiCtx getGC() {
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
      dc.root(this, ObjectGui.class, "@line5");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ObjectGui.class);
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return gc.getUCtx();
   }

   //#enddebug

}
