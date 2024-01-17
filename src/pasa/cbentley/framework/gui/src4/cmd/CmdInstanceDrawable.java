package pasa.cbentley.framework.gui.src4.cmd;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.engine.CmdNode;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.cmd.src4.trigger.CmdTrigger;
import pasa.cbentley.framework.coreui.src4.tech.ITechInputFeedback;
import pasa.cbentley.framework.gui.src4.canvas.ExecutionCtxDraw;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

/**
 * The execution context of a command 
 * @author Charles Bentley
 *
 */
public class CmdInstanceDrawable extends CmdInstance {

   private String         actionString;

   private CmdInstanceDrawable    childCmd;

   private IDrawable      d;

   protected CmdInstanceDrawable  parentCmd;

   protected final GuiCtx gc;

   public CmdInstanceDrawable(GuiCtx gc, int vcmdID) {
      super(gc.getCC(), vcmdID);
      this.gc = gc;
   }

   public CmdInstanceDrawable(GuiCtx gc, MCmd c, CmdNode ctx, CmdTrigger ct) {
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

   public CmdInstanceDrawable getChildCmdDrawable() {
      return childCmd;
   }

   public ExecutionCtxDraw getDExCtx() {
      return (ExecutionCtxDraw) exeCtx;
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
      return cmd;
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
      dc.root(this, "CmdDrawable");
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx sb) {
      sb.root1Line(this, "CmdDrawable");
      sb.append(" ");
      super.toString1LineHelper(sb);
   }
   //#enddebug
}
