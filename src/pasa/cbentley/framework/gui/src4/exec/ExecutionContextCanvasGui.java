package pasa.cbentley.framework.gui.src4.exec;

import pasa.cbentley.core.src4.structs.IntToObjects;
import pasa.cbentley.framework.core.ui.src4.exec.ExecEntry;
import pasa.cbentley.framework.core.ui.src4.exec.ExecutionContext;
import pasa.cbentley.framework.gui.src4.canvas.CanvasAppliInputDrawable;
import pasa.cbentley.framework.gui.src4.canvas.CanvasAppliInputGui;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.canvas.TopLevelCtrl;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceGui;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechExecutionContextGui;
import pasa.cbentley.framework.gui.src4.string.StringListInput;
import pasa.cbentley.framework.input.src4.engine.ExecutionContextCanvas;

/**
 * {@link ExecutionContext} inside an {@link CanvasAppliInputGui}.
 * 
 * <p>
 * <li> It collects {@link IDrawable} and pages page IDs from {@link TopLevelCtrl}
 * </p>
 * 
 * It is created by  {@link CanvasAppliInputGui#createExecutionContextCanvasGui()}
 * 
 * @see ExecutionContextCanvas
 * @see ExecutionContext
 * 
 * @see CanvasAppliInputDrawable
 * @author Charles Bentley
 *
 */
public class ExecutionContextCanvasGui extends ExecutionContextCanvas implements ITechExecutionContextGui {

   public int              addressX;

   /**
    * Pointed Drawable Y address.
    */
   public int              addressY;

   /**
    * Set when a command is found
    */
   public CmdInstanceGui   cd;

   public volatile boolean finished;

   protected final GuiCtx  gc;

   private InputConfig     icc;

   private IntToObjects    renders;

   public ExecutionContextCanvasGui(GuiCtx gc) {
      super(gc.getIC());
      this.gc = gc;
      renders = new IntToObjects(gc.getUC());
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

   public void addDrawnHide(IDrawable draw) {
      this.addDrawn(draw, ACTION_2_HIDE);
   }

   public void addDrawnHide(Object draw) {
      this.addDrawn(draw, ACTION_2_HIDE);
   }

   public void addDrawnInvalidated(IDrawable draw) {
      this.addDrawn(draw, ACTION_3_INVALIDATE);
   }

   public void addDrawnShow(IDrawable draw) {
      this.addDrawn(draw, ACTION_0_SHOW_OVER);
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

   private void execTypePage(ExecEntry ee) {
      int newPageID = ee.action;
      boolean addToHistory = true;
      ExecutionContextCanvasGui ex = this;
      gc.getTopLvl().showPage(ex, newPageID, addToHistory);
   }

   private void execTypeRun(ExecEntry ee) {
      Runnable d = (Runnable) ee.o;
      d.run();
   }

   public OutputStateCanvasGui getCanvasResultDrawable() {
      return (OutputStateCanvasGui) os;
   }

   public CmdInstanceGui createCmd(int cmdid) {
      CmdInstanceGui cd = new CmdInstanceGui(gc, cmdid);
      cd.setFeedback(os);
      return cd;
   }

   public CmdInstanceGui getCmdInstanceGui() {
      return ci;
   }

   public CmdInstanceGui getCmdInstance() {
      return ci;
   }

   public void checkCmdInstanceNotNull() {
      if (ci == null) {
         throw new NullPointerException();
      }
   }

   public InputConfig getInputConfig() {
      return icc;
   }

   public InputStateCanvasGui getInputStateDrawable() {
      return (InputStateCanvasGui) is;
   }

   public OutputStateCanvasGui getOutputStateCanvasGui() {
      return (OutputStateCanvasGui) os;
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

   private CmdInstanceGui ci;

   public void cmdActionOnDrawable(IDrawable d) {
      if (ci != null) {
         ci.actionDone();
      }
      getCanvasResultDrawable().setActionDoneRepaint(d);
   }

   public void setInputConfig(InputConfig icc) {
      this.icc = icc;
   }

   public void srActionDoneRepaint() {
      icc.srActionDoneRepaint();
   }

   public void srActionDoneRepaint(IDrawable d) {
      icc.srActionDoneRepaint(d);
   }
}
