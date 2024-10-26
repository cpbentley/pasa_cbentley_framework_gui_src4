package pasa.cbentley.framework.gui.src4.cmd.mcmd;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.ctx.IEventsCore;
import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.core.src4.event.IEventConsumer;
import pasa.cbentley.core.src4.i8n.IStringProducer;
import pasa.cbentley.core.src4.i8n.LocaleID;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.framework.cmd.src4.cmd.MCmdAbstract;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdsCmd;
import pasa.cbentley.framework.datamodel.src4.table.ObjectTableModel;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceGui;
import pasa.cbentley.framework.gui.src4.cmd.CmdProcessorGui;
import pasa.cbentley.framework.gui.src4.cmd.CommanderGui;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.factories.TableCellPolicyFactory;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsGui;
import pasa.cbentley.framework.gui.src4.interfaces.IUIView;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.table.interfaces.ITechTable;
import pasa.cbentley.framework.gui.src4.tech.ITechLinks;

/**
 * One instance per langTable
 * @author Charles Bentley
 *
 */
public class MCmdGuiChangeLanguage extends MCmdAbstractGui implements IEventConsumer {

   public MCmdGuiChangeLanguage(GuiCtx gc) {
      super(gc, ICmdsCmd.CMD_40_LANGUAGE_CHANGE);
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
         toDLog().pEvent("Suffix=" + suffix + " Selected Row=" + row + " out of " + otm.getNumRows(), this, CmdProcessorGui.class, "consumeEvent", LVL_05_FINE, true);

         cc.executeInstance(activeCmdLang);
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
   public void cmdExecuteFinalGui(CmdInstanceGui cmd) {
      ExecutionContextCanvasGui ec = cmd.getExecutionContextGui();
      if (cmd.isParamO(LocaleID.class)) {
         LocaleID suffix = (LocaleID) cmd.getParamO();
         IStringProducer strLoader = gc.getStrings();
         LocaleID old = strLoader.getLocaleID();
         strLoader.setLocalID(suffix);
         //string user feedback in command log
         cmd.setUndo(old);
         //update all StringDrawable using localized string
         int responseType = FLAG_04_RENEW_LAYOUT | FLAG_02_FULL_REPAINT | FLAG_06_DATA_REFRESH;
         //invalidate all layouts generated full refresh event, cleaning of non visible elements.
         if (langTable != null) {
            langTable.removeDrawable(ec, null);
            langTable = null;
         }
         //generates an event
         gc.getUC().getEventBusRoot().sendNewEvent(IEventsCore.PID_01_FRAMEWORK, IEventsCore.PID_01_FRAMEWORK_2_LANGUAGE_CHANGED, cmd);
         //set the action feedback on the command. flag it  as processed
         cmd.actionDone(null, responseType);
      } else {
         //choice
         IStringProducer strLoader = gc.getStrings();
         //show the table
         if (langTable == null) {
            TableCellPolicyFactory cellFac = gc.getTableCellPolicyFactory();
            LocaleID[] data = strLoader.getLocaleIDs();
            ByteObject colPol = cellFac.getGeneric(2, 0);
            ByteObject rowPol = cellFac.getGeneric(0, 0);
            ByteObject policyTable = gc.getTablePolicyFactory().getTablePolicy(colPol, rowPol);
            StyleClass scMenu = gc.getStyleClass(IUIView.SC_1_MENU);
            StyleClass scMenuItems = scMenu.getSCNotNull(ITechLinks.LINK_74_STYLE_CLASS_MENU);

            TableView tv = new TableView(gc, scMenuItems, policyTable);
            //position it bottom left or center center logical position relative to parent/screen
            ObjectTableModel otm = new ObjectTableModel(gc.getDMC(), data);
            tv.setDataModel(otm);
            tv.setXYLogic(C.LOGIC_3_BOTTOM_RIGHT, C.LOGIC_3_BOTTOM_RIGHT);
            //set the selection to the language
            //init once the model has been set.
            tv.setSizer_WH_Pref();
            
            tv.addEventListener(this, ITechTable.EVENT_ID_00_SELECT);
            //show the form as child of current front drawable
            langTable = tv;
            activeCmdLang = cmd;
         }
         langTable.shShowDrawableOver(ec);
         LocaleID lang = strLoader.getLocaleID();
         int index = ((ObjectTableModel) langTable.getTableModel()).findIndexObjectFirst(lang);
         if (index == -1) {
            index = 0;
         }
         //give focus to selected
         langTable.setSelectedIndex(index, ec, true);

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

   public void cmdExecuteParamGui(CmdInstanceGui ci) {
      
   }


   public MCmdAbstract createInstance(CmdInstance ci) {
      return new MCmdGuiChangeLanguage(gc);
   }

}
