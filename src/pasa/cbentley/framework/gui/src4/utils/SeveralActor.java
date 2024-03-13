package pasa.cbentley.framework.gui.src4.utils;

import java.util.Enumeration;
import java.util.Vector;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawableListener;

public class SeveralActor implements IDrawableListener {

   private Vector         v;

   protected final GuiCtx gc;

   public SeveralActor(GuiCtx gc) {
      this.gc = gc;
      v = new Vector();
   }

   public void add(IDrawableListener pa) {
      v.addElement(pa);
   }

   public void remove(IDrawableListener pa) {
      v.removeElement(pa);
   }

   public void notifyEvent(IDrawable d, int event, Object o) {
      Enumeration en = v.elements();
      while (en.hasMoreElements()) {
         IDrawableListener pa = (IDrawableListener) en.nextElement();
         pa.notifyEvent(d, event, o);
      }
   }

   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "SeveralActor");
      dc.appendVarWithSpace("size", v.size());
      Enumeration en = v.elements();
      while (en.hasMoreElements()) {
         IDrawableListener pa = (IDrawableListener) en.nextElement();
         dc.nlLvl1Line(pa);
      }
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "SeveralActor");
   }

   public UCtx toStringGetUCtx() {
      return gc.getUC();
   }
   //#enddebug
}
