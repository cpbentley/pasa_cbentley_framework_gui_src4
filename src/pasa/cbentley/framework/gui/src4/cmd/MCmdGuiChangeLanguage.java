package pasa.cbentley.framework.gui.src4.cmd;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.ctx.IEventsCore;
import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.core.src4.event.IEventConsumer;
import pasa.cbentley.core.src4.i8n.IStringProducer;
import pasa.cbentley.core.src4.i8n.LocaleID;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.datamodel.src4.table.ObjectTableModel;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.factories.CellPolicyFactory;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsView;
import pasa.cbentley.framework.gui.src4.interfaces.IUIView;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOTableView;

/**
 * One instance per langTable
 * @author Charles Bentley
 *
 */
public class MCmdGuiChangeLanguage extends MCmdGui implements IEventConsumer {

   public MCmdGuiChangeLanguage(GuiCtx gc) {
      super(gc, ICmdsView.CMD_40_LANGUAGE_CHANGE);
   }

   protected CmdInstance activeCmdLang;

   private TableView     langTable;

   public void consumeEvent(BusEvent e) {
      Object producer = e.getProducer();
      if (producer == langTable) {
         int row = langTable.getSelectedRow();
         // The active instance command used when fetching command parameters in a GUI
         CmdInstance activeCmdLang = (CmdInstance) e.getParamO1();
         ObjectTableModel otm = (ObjectTableModel) langTable.getTableModel();
         String suffix = (String) otm.getObject(row, 0);
         //now that we have the suffix, take the command instance, set the parameter and execute it.
         activeCmdLang.setParamO(suffix);

         //#debug
         toDLog().pEvent("Suffix=" + suffix + " Selected Row=" + row + " out of " + otm.getNumRows(), this, ViewCommandListener.class, "consumeEvent", LVL_05_FINE, true);

         cc.commandAction(activeCmdLang);
         //hide table view
      }
   }

   /**
    * 3 possibilites
    * <li>Cmd is loaded with parameter
    * <li>Cmd asks user for parameter
    * <li>Cmd undo mode.
    * <br>
    * <br>
    * 
    * @param cmd
    */
   public void execute(InputConfig ic) {
      CmdInstance cmd = ic.getCmdInstance();
      if (cmd.paramO != null) {
         LocaleID suffix = (LocaleID) cmd.paramO;
         IStringProducer strLoader = gc.getStrings();
         LocaleID old = strLoader.getLocaleID();
         strLoader.setLocalID(suffix);
         //string user feedback in command log
         cmd.setUndo(old);
         //update all StringDrawable using localized string
         int responseType = FLAG_04_RENEW_LAYOUT | FLAG_02_FULL_REPAINT | FLAG_06_DATA_REFRESH;
         //invalidate all layouts generated full refresh event, cleaning of non visible elements.
         if (langTable != null) {
            langTable.removeDrawable((InputConfig) cmd.getFeedback(), null);
            langTable = null;
         }
         //generates an event
         gc.getUCtx().getEventBusRoot().sendNewEvent(IEventsCore.PID_1_FRAMEWORK, IEventsCore.PID_1_FRAMEWORK_2_LANGUAGE_CHANGED, cmd);
         //set the action feedback on the command. flag it  as processed
         cmd.actionDone(null, responseType);
      } else {
         //choice
         IStringProducer strLoader = gc.getStrings();
         //show the table
         if (langTable == null) {
            CellPolicyFactory cellFac = gc.getCellPolicyC();
            LocaleID[] data = strLoader.getLocaleIDs();
            ByteObject colPol = cellFac.getGeneric(2, 0);
            ByteObject rowPol = cellFac.getGeneric(0, 0);
            ByteObject policyTable = gc.getTablePolicyC().getTablePolicy(colPol, rowPol);
            StyleClass scMenu = gc.getClass(IUIView.SC_1_MENU);
            StyleClass scMenuItems = scMenu.getSCNotNull(IBOTypesGui.LINK_74_STYLE_CLASS_MENU);

            TableView tv = new TableView(gc, scMenuItems, policyTable);
            //position it bottom left or center center logical position relative to parent/screen
            ObjectTableModel otm = new ObjectTableModel(gc.getDMC(), data);
            tv.setDataModel(otm);
            tv.setXYLogic(C.LOGIC_3_BOTTOM_RIGHT, C.LOGIC_3_BOTTOM_RIGHT);
            //set the selection to the language
            //init once the model has been set.
            tv.init(0, 0);
            tv.addEventListener(this, IBOTableView.EVENT_ID_00_SELECT);
            //show the form as child of current front drawable
            langTable = tv;
            activeCmdLang = cmd;
         }
         langTable.shShowDrawableOver((InputConfig) cmd.getFeedback());
         LocaleID lang = strLoader.getLocaleID();
         int index = ((ObjectTableModel) langTable.getTableModel()).findObjectRef(lang);
         if (index == -1) {
            index = 0;
         }
         //give focus to selected
         langTable.setSelectedIndex(index, (InputConfig) cmd.getFeedback(), true);

         //create an OK/CANCEL interaction cmd ctx. modal?
         //TODO actually menu listen to active cmd ctx and update itself automatically
         //based on it
         //set menu to model mode with OK / CANCEL
         String menuLabel = "Select";
         langTable.getCanvas().getMenuBar().setCmdModelOKCAncel(menuLabel);
         cmd.actionDone(langTable, 0);

         //#debug
         toDLog().pFlow("", langTable, CommanderGui.class, "cmdChangeLanguage", LVL_05_FINE, false);
      }
   }

}
