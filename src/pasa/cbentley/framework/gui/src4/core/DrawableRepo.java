package pasa.cbentley.framework.gui.src4.core;

import java.util.Enumeration;
import java.util.Hashtable;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.interfaces.ITechNav;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

public class DrawableRepo implements ITechNav, IStringable {
   private Hashtable      ht = new Hashtable();

   protected final GuiCtx gc;

   public DrawableRepo(GuiCtx gc) {
      this.gc = gc;
   }

   private int uuid;

   public void linkNav(IDrawable d, int navEvent, IDrawable dest) {
      NavKey nk = new NavKey(d.getUIID(), navEvent);
      ht.put(nk, dest);
   }

   /**
    * Returns a unique ID for the {@link GuiContext}.
    * <li> For nav key
    * <li> For Drawable reference in {@link ISizer}
    * 
    * @return
    */
   public int nextUIID() {
      return uuid++;
   }

   /**
    * 
    * @param uiid
    * @return
    * @throws IllegalArgumentException when {@link GuiContext} failed to track Drawable
    */
   public IDrawable getDrawable(int uiid) {
      if (uiid < arrayAll.length) {
         return arrayAll[uiid];
      }
      throw new IllegalArgumentException(uiid + " " + arrayAll.length + " ");
   }

   private IDrawable[] arrayAll = new IDrawable[50];

   public class NavKey {
      public NavKey(int id, int navEvent) {
         uiid = id;
         nav = navEvent;
      }

      int uiid;

      int nav;
   }

   /**
    * remove all links with drawable
    * @param d
    */
   public void removeNavLinksAll(IDrawable d) {
      //removes all possible link to drawable 
      Enumeration en = ht.keys();
      while (en.hasMoreElements()) {
         NavKey key = (NavKey) en.nextElement();
         Object val = ht.get(key);
         if (val == d || key.uiid == d.getUIID()) {
            ht.remove(key);
         }
      }
   }

   public static int navInverse(int navEvent) {
      switch (navEvent) {
         case NAV_0_REFRESH:
            return NAV_0_REFRESH;
         case NAV_1_UP:
            return NAV_2_DOWN;
         case NAV_2_DOWN:
            return NAV_1_UP;
         case NAV_3_LEFT:
            return NAV_4_RIGHT;
         case NAV_4_RIGHT:
            return NAV_3_LEFT;
         case NAV_5_SELECT:
            return NAV_6_UNSELECT;
         case NAV_6_UNSELECT:
            return NAV_5_SELECT;
         default:
            return NAV_0_REFRESH;
      }
   }

   /**
    * Removes with the navEvent
    * @param d
    * @param navEvent
    */
   public void removeNavLinks(IDrawable d, int navEvent) {
      //removes all possible link to drawable 
      Enumeration en = ht.keys();
      while (en.hasMoreElements()) {
         NavKey key = (NavKey) en.nextElement();
         Object val = ht.get(key);
         if (key.nav == navEvent && key.uiid == d.getUIID()) {
            ht.remove(key);
         }
      }
   }

   public IDrawable getLink(IDrawable d, int navEvent) {
      NavKey nk = new NavKey(d.getUIID(), navEvent);
      return (IDrawable) ht.get(nk);
   }
   
   //#mdebug
   public IDLog toDLog() {
      return toStringGetUCtx().toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "DrawableRepo");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "DrawableRepo");
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return gc.getUC();
   }

   //#enddebug
   

}
