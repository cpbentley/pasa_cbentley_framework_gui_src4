package pasa.cbentley.framework.gui.src4.cmd;

import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.coreui.src4.tech.ITechInputFeedback;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;

public abstract class MCmdGui extends MCmd implements ITechInputFeedback {

   protected final GuiCtx gc;

   public MCmdGui(GuiCtx gc, int id) {
      super(gc.getCC(), "");
      this.gc = gc;
   }

   /**
    * {@link InputConfig} provides everything the cmd needs for execution and providing
    * results.
    * It runs
    * @param ic
    */
   public abstract void execute(InputConfig ic);
}
