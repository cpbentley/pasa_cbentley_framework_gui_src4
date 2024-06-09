package pasa.cbentley.framework.gui.src4.cmd;

import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.coreui.src4.tech.ITechInputFeedback;
import pasa.cbentley.framework.datamodel.src4.table.ObjectTableModel;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.table.TableView;

/**
 * Extension of {@link MCmd} for the {@link GuiCtx}.
 * 
 * @author Charles Bentley
 *
 */
public abstract class MCmdGui extends MCmd implements ITechInputFeedback {

   protected final GuiCtx gc;

   protected TableView    tableParamSelection;

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

   public void consumeEvent(BusEvent e) {
      Object producer = e.getProducer();
      if (producer == tableParamSelection) {
         int row = tableParamSelection.getSelectedRow();
         // The active instance command used when fetching command parameters in a GUI
         CmdInstance activeCmdLang = (CmdInstance) e.getParamO1();
         ObjectTableModel otm = (ObjectTableModel) tableParamSelection.getTableModel();
         String suffix = (String) otm.getObject(row, 0);
         //now that we have the suffix, take the command instance, set the parameter and execute it.
         activeCmdLang.setParamO(suffix);

         //#debug
         toDLog().pEvent("Suffix=" + suffix + " Selected Row=" + row + " out of " + otm.getNumRows(), this, ViewCommandListener.class, "consumeEvent", LVL_05_FINE, true);

         cc.commandAction(activeCmdLang);
         //hide table view
      }

      //TODO if cancel
   }

   public void tableSelection(InputConfig ic) {

   }

}
