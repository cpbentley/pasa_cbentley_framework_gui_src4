package pasa.cbentley.framework.gui.src4.ctx;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.logging.LogParameters;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;

public class ObjectGC implements IStringable {

   protected final CmdCtx cc;

   //#debug
   private String         debugName;

   protected final GuiCtx gc;

   public ObjectGC(GuiCtx gc) {
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
      dc.root(this, ObjectGC.class, 38);
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ObjectGC.class);
      toStringPrivate(dc);
   }

   public LogParameters toStringGetLine(Class cl, String method, int value) {
      return toStringGetUCtx().toStringGetLine(cl, method, value);
   }

   public String toStringGetLine(int value) {
      return toStringGetUCtx().toStringGetLine(value);
   }

   //#mdebug
   public String toStringGetName() {
      return debugName;
   }

   public UCtx toStringGetUCtx() {
      return gc.getUC();
   }

   public String toStringName() {
      return debugName;
   }

   private void toStringPrivate(Dctx dc) {
      if (debugName != null) {
         dc.appendBracketedWithSpace(debugName);
      }
   }

   public void toStringSetName(String name) {
      debugName = name;
   }

   //#enddebug

}
