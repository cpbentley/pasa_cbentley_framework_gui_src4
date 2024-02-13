package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.core.src4.structs.IntToObjects;
import pasa.cbentley.framework.coreui.src4.exec.ExecEntry;
import pasa.cbentley.framework.coreui.src4.exec.ExecutionContext;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceDrawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechExecutionContextGui;

/**
 * Execution context of a GUI event in the Drawable Bentley framework.
 * It collects {@link IDrawable} and pages page IDs.
 * A Page is like a Screen in Android or a form in J2ME. It replaces everything that 
 * was drawn previously.
 * 
 * Events {@link IEventsKernel} that modify UI state, add modifications on this context.
 * 
 * 
 * As described by {@link ExecutionContext}.
 * 
 * @author Charles Bentley
 *
 */
public class ExecutionContextGui extends ExecutionContext implements ITechExecutionContextGui {

   public int                 addressX;

   /**
    * Pointed Drawable Y address.
    */
   public int                 addressY;

   /**
    * Set when a command is found
    */
   public CmdInstanceDrawable cd;

   public volatile boolean    finished;

   protected final GuiCtx     gc;

   private IntToObjects       renders;

   private InputConfig        icc;

   public ExecutionContextGui(GuiCtx gc) {
      super(gc.getCUC());
      this.gc = gc;
      renders = new IntToObjects(gc.getUCtx());
   }

   public InputConfig getInputConfig() {
      return icc;
   }

   /**
    * Queue the {@link IDrawable} to be
    * @param draw
    * @param action
    */
   public void addDrawn(IDrawable draw, int action) {
      types.add(EXEC_TYPE_2_DRAW, null);
      data.add(action, draw);
   }

   /**
    * All Draw must be shown in the GUi thread
    * @param draw
    */
   public void addDrawn(Object draw) {
      this.addDrawn(draw, ACTION_0_SHOW_OVER);
   }

   public void addDrawn(Object draw, int action) {
      types.add(EXEC_TYPE_2_DRAW, null);
      data.add(action, draw);
   }

   public void addDrawnHide(Object draw) {
      this.addDrawn(draw, ACTION_2_HIDE);
   }

   public void addInvalide(IDrawable d) {
      types.add(EXEC_TYPE_3_PAGE, null);
      data.add(0, d);
   }

   public void addPage(int pageID) {
      types.add(EXEC_TYPE_3_PAGE, null);
      data.add(pageID, null);
   }

   public void addRender(ExecEntry ee) {
      renders.add(ee);
   }

   /**
    * Reset pointers
    */
   public void clear() {
   }

   public void renderStart() {
      for (int i = 0; i < renders.nextempty; i++) {
         ExecEntry ee = (ExecEntry) renders.objects[i];
         int type = ee.type;
         if (type == EXEC_TYPE_1_RUN) {
            execTypeRun(ee);
         } else if (type == EXEC_TYPE_2_DRAW) {
            execTypeDraw(ee);
         } else if (type == EXEC_TYPE_3_PAGE) {
            execTypePage(ee);
         } else if (type == EXEC_TYPE_0_EVENT) {
            throw new IllegalArgumentException();
         } else {
            throw new IllegalArgumentException();
         }
      }
   }

   private void execTypeRun(ExecEntry ee) {
      Runnable d = (Runnable) ee.o;
      d.run();
   }

   private void execTypePage(ExecEntry ee) {
      int newPageID = ee.action;
      boolean addToHistory = true;
      ExecutionContextGui ex = this;
      gc.getTopLvl().showPage(ex, newPageID, addToHistory);
   }

   private void execTypeDraw(ExecEntry ee) {
      IDrawable d = (IDrawable) ee.o;
      int action = ee.action;
      if (action == ACTION_0_SHOW_OVER) {
         //showing? what inputconfig? we are modifying state in the GUI thread
         gc.getFocusCtrl().drawableShow(this, d);
      } else if (action == ACTION_2_HIDE) {
         gc.getFocusCtrl().drawableHide(cd, d, null);
      } else if (action == ACTION_1_SHOW_REPLACE) {
         gc.getFocusCtrl().drawableShow(this, d);
      } else if (action == ACTION_3_INVALIDATE) {
         d.invalidateLayout();
      }
   }

   public void setInputConfig(InputConfig icc) {
      this.icc = icc;
   }
}
