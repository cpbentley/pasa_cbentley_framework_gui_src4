package pasa.cbentley.framework.gui.src4.cmd;

import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.engine.CmdNode;
import pasa.cbentley.framework.cmd.src4.engine.CmdProcessor;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.cmd.src4.input.Commander;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdControlled;
import pasa.cbentley.framework.core.ui.src4.engine.CanvasHostAbstract;
import pasa.cbentley.framework.core.ui.src4.interfaces.ICanvasAppli;
import pasa.cbentley.framework.gui.src4.canvas.CanvasAppliInputGui;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ObjectGC;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

public class CmdControlledGui extends ObjectGC implements ICmdControlled {

   public CmdControlledGui(GuiCtx gc) {
      super(gc);
   }

   public CmdInstance createCmdInstance(MCmd cmd) {
      return new CmdInstanceGui(gc, cmd);
   }

   public Commander createCommander(int id) {
      return new CommanderGui(gc, id);
   }

   public CmdProcessor createProcessor() {
      return new CmdProcessorGui(gc);
   }

   public CmdNode getCmdNode(int x, int y) {
      // TODO Auto-generated method stub
      //fetch the node on the canvas located at x y
      CanvasHostAbstract[] canvasAt = gc.getCUC().getCanvasAt(x, y);

      if (canvasAt != null) {

         for (int i = 0; i < canvasAt.length; i++) {
            ICanvasAppli canvasAppli = canvasAt[i].getCanvasAppli();
            if (canvasAppli instanceof CanvasAppliInputGui) {
               CanvasAppliInputGui canvas = (CanvasAppliInputGui) canvasAppli;
               ExecutionContextCanvasGui ec = null;
               IDrawable d = canvas.getTopologyRoot().getDrawable(x, y, ec);
               return d.getCmdNode();
            }
         }
      }

      return null;
   }

}
