package pasa.cbentley.framework.gui.src4.cmd;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.engine.CmdNode;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.cmd.src4.trigger.CmdTrigger;
import pasa.cbentley.framework.core.ui.src4.tech.ITechInputFeedback;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

/**
 * The execution context of a command 
 * @author Charles Bentley
 *
 */
public class CmdInstanceGui extends CmdInstance {

   private String           actionString;

   private CmdInstanceGui   childCmd;

   private IDrawable        d;

   protected CmdInstanceGui parentGui;

   protected final GuiCtx   gc;

   public CmdInstanceGui(GuiCtx gc, int vcmdID) {
      super(gc.getCC(), vcmdID);
      this.gc = gc;
   }

   public CmdInstanceGui(GuiCtx gc, int vcmdID, ExecutionContextCanvasGui ec) {
      super(gc.getCC(), vcmdID);
      this.gc = gc;
      this.exeCtx = ec;
   }

   public CmdInstanceGui(GuiCtx gc, MCmd c) {
      super(gc.getCC(), c);
      this.gc = gc;
   }

   public void setParentGui(CmdInstanceGui cmdGui) {
      this.setParent(cmdGui);
      this.parentGui = cmdGui;
   }

   public CmdInstanceGui(GuiCtx gc, MCmd c, CmdNode ctx, CmdTrigger ct) {
      super(gc.getCC(), c, ctx, ct);
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
      return childCmd;
   }

   public ExecutionContextCanvasGui getExecutionCtxGui() {
      return (ExecutionContextCanvasGui) getExecutionContext();
   }

   public InputConfig getIC() {
      return (InputConfig) getFeedback();
   }

   public IDrawable getDrawable() {
      return d;
   }

   public int getParamInt(int id) {
      return paramsDo.getParamInt(id);
   }

   public int getParamUndoInt(int id) {
      return paramsUndo.getParamInt(id);
   }

   public MCmd getRoot() {
      return getMCmd();
   }

   //#enddebug
   /**
    * Is there is child command.
    * <br>
    * Child commands are commands like OK/CANCEL generated while executing a command.
    * <br>
    * What about cue parameter commands?
    * @return
    */
   public boolean hasChild() {
      return childCmd != null;
   }

   public boolean isRootMode() {
      return true;
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

   public void setParamInt(int id, int param) {
      paramsDo.setParamInt(id, param);
   }

   public void setParamUndoInt(int value) {
      paramsUndo.setParamInt(0, value);
   }

   public void setParamUndoInt(int id, int value) {
      paramsUndo.setParamInt(id, value);
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
