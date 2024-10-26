package pasa.cbentley.framework.gui.src4.cmd;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.core.ui.src4.tech.ITechInputFeedback;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

/**
 * Specialisation of {@link CmdInstance} for the {@link GuiCtx} and its {@link IDrawable} ui kit.
 * 
 * 
 * @see ExecutionContextCanvasGui
 * 
 * @author Charles Bentley
 *
 */
public class CmdInstanceGui extends CmdInstance {

   private String           actionString;

   private CmdInstanceGui   cmdDrawableChild;

   private IDrawable        d;

   protected final GuiCtx   gc;

   protected CmdInstanceGui parentGui;

   public CmdInstanceGui(GuiCtx gc, MCmd c) {
      super(gc.getCC(), c);
      this.gc = gc;
   }

   /**
    * Called after move....Do we compute the layout? Yes we will have to compute the whole layout
    * just before the repaint and compute the repaint area based
    * 
    * @param d
    */
   public void actionDoneAfter(IDrawable d) {

   }

   /**
    * Called before a move or change of dimension.
    * <br>
    * The sizing is correct
    * @param d
    */
   public void actionDoneBefore(IDrawable d) {
      this.actionDone(null, ITechInputFeedback.FLAG_02_FULL_REPAINT);
   }

   public CmdInstanceGui getChildCmdDrawable() {
      return cmdDrawableChild;
   }

   public IDrawable getDrawable() {
      return d;
   }

   public ExecutionContextCanvasGui getExecutionContextGui() {
      ExecutionContextCanvasGui ec = (ExecutionContextCanvasGui) getExecutionContext();
      ec.setCmdInstanceGui(this);
      return ec;
   }

   public InputConfig getInputConfig() {
      return getExecutionContextGui().getInputConfig();
   }

   /**
    * Is there is child command.
    * <br>
    * Child commands are commands like OK/CANCEL generated while executing a command.
    * <br>
    * What about cue parameter commands?
    * @return
    */
   public boolean hasChild() {
      return cmdDrawableChild != null;
   }

   /**
    * Returns true when Trigger contains the info
    * to compute the parameters.
    * <br>
    * In a Move command using a Drag pointer, the parameters is in the drag
    * vector.
    * @return
    */
   public boolean isTriggerParam() {

      return false;
   }

   public void setActionString(String string) {
      actionString = string;
   }

   public void setDrawable(IDrawable d) {
      this.d = d;
   }

   public void setParentGui(CmdInstanceGui cmdGui) {
      this.setParent(cmdGui);
      this.parentGui = cmdGui;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, CmdInstanceGui.class, 141);
      toStringPrivate(dc);
      super.toString(dc.sup());

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, CmdInstanceGui.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }
   //#enddebug

}
