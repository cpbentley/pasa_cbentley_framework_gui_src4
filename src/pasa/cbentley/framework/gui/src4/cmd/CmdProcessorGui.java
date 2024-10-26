package pasa.cbentley.framework.gui.src4.cmd;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.engine.CmdNode;
import pasa.cbentley.framework.cmd.src4.engine.CmdProcessor;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdsCmd;
import pasa.cbentley.framework.cmd.src4.trigger.CmdTrigger;
import pasa.cbentley.framework.core.ui.src4.tech.ITechCodes;
import pasa.cbentley.framework.core.ui.src4.tech.ITechInputFeedback;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.exec.InputStateCanvasGui;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsGui;
import pasa.cbentley.framework.gui.src4.menu.CmdMenuBar;

/**
 * Implements the Root {@link CmdCtx} commands that require the use of {@link Drawable}s.
 * <br>
 * <br>
 * <li> {@link CmdController#CMD_HELP}
 * <li> {@link CmdController#CMD_CMDS_HISTORY}
 * <li> {@link CmdController#CMD_40_LANGUAGE_CHANGE}
 * <li> {@link CmdController#CMD_HELP}
 * <br>
 * <br>
 * Linked to {@link CmdController#commandAction(CmdInstance)}
 * <br>
 * Basically any Show Menu Something commands will be dealt here.
 * <br>
 * <br>
 * 
 * @author Charles-Philip Bentley
 *
 */
public class CmdProcessorGui extends CmdProcessor implements ICmdsGui, ITechInputFeedback {

   protected final GuiCtx gc;

   private CmdNode        modalCtx;

   public CmdProcessorGui(GuiCtx gc) {
      super(gc.getCC());
      this.gc = gc;
   }

   public CmdMenuBar getCmdMenuBar() {
      return getCmdMenuBar();
   }

   /**
    * Returns the root model context used to draw exclusive
    * OK/Cancel Input requests
    * @return
    */
   public CmdNode getModalCmdNode() {
      if (modalCtx == null) {
         modalCtx = cc.getCmdNodeRoot().createChildCtx("Modal");
         CmdTrigger ctFire = cc.getFacTrig().createKeyP(ITechCodes.KEY_FIRE);
         modalCtx.addCmdLink(ctFire, ICmdsCmd.CMD_04_OK);
         CmdTrigger ctEscape = cc.getFacTrig().createKeyP(ITechCodes.KEY_ESCAPE);
         modalCtx.addCmdLink(ctEscape, ICmdsCmd.CMD_05_CANCEL);
      }
      return modalCtx;
   }

   /**
    * Maps current event to a Trigger or a Command
    * <br>
    * <br>
    * If no matching, let the event go to the application method for processing.
    * <br>
    * Non trigger events such as Trail events are Gesture Events, they are forwarded to the command but there are not a trigger per se.
    * <br>
   
    * @param is
    * @param sr
    * @return processing state
    */
   public int processGUIEvent(ExecutionContextCanvasGui ec, InputStateCanvasGui is, int ctxcat) {
      return 0;
   }

   /**
    * Make this command position the most important.
    * <br>
    * It will be shown first in a drop down menu of relevant commands.
    * <br>
    * @param cmd
    */
   public void setTopPriorityCommand(MCmd cmd) {
      getCmdMenuBar().setTopPriorityCommand(cmd);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, CmdProcessorGui.class, 473);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, CmdProcessorGui.class, 479);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }
   //#enddebug

}
