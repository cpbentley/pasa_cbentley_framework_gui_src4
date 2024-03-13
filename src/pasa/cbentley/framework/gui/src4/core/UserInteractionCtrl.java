package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.memory.IMemFreeable;
import pasa.cbentley.core.src4.structs.IntToObjects;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IUiACreator;

/**
 * The resources behind a {@link UserInteraction} is not managed here.
 * <br>
 * This class tracks the {@link UserInteraction} happening
 * <br>
 * Class must be used in the update thread.
 * 
 * @author Charles Bentley
 *
 */
public class UserInteractionCtrl implements IMemFreeable {

   IUiACreator  c;

   IntToObjects itos;

   protected final GuiCtx gc;

   public UserInteractionCtrl(GuiCtx gc) {
      this.gc = gc;
      UCtx uc = gc.getUC();
      itos = new IntToObjects(uc);

      //how can you unregister a module?
      uc.getMem().addMemFreeable(this);

   }

   public void freeMemory() {
      itos.clear();
   }

   public UserInteraction getUIA(int uiaid) {
      //creates a new entry of a user interactions
      UserInteraction ui = c.createUIA(uiaid);
      itos.add(ui);
      //check if
      return ui;
   }

   /**
    * Must be accessed in the update thread
    * @param ui
    */
   public void log(UserInteraction ui) {
      itos.add(ui);
   }


   //#mdebug
   public IDLog toDLog() {
      return toStringGetUCtx().toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "UserInteractionCtrl");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "UserInteractionCtrl");
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return gc.getUC();
   }

   //#enddebug
   


}
