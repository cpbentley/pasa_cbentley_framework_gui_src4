package pasa.cbentley.framework.gui.src4.table;

import pasa.cbentley.framework.cmd.src4.cmd.MCmdAbstract;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceGui;
import pasa.cbentley.framework.gui.src4.cmd.mcmd.MCmdAbstractGui;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsGui;

public class MCmdGuiSelectCell extends MCmdAbstractGui {

   public MCmdGuiSelectCell(GuiCtx gc) {
      super(gc, ICmdsGui.CMD_GUI_21_SELECT_CELL);
      // TODO Auto-generated constructor stub
   }

   public void cmdExecuteFinalGui(CmdInstanceGui ci) {
      // TODO Auto-generated method stub

   }

   public MCmdAbstract createInstance(CmdInstance ci) {
      // TODO Auto-generated method stub
      return null;
   }

}
