package pasa.cbentley.framework.gui.src4.cmd.mcmd;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.core.src4.event.IEventConsumer;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.structs.IntToObjects;
import pasa.cbentley.framework.cmd.src4.cmd.MCmdAbstract;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.engine.CmdNode;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdListener;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdsCmd;
import pasa.cbentley.framework.cmd.src4.interfaces.ITechCmd;
import pasa.cbentley.framework.core.ui.src4.tech.ITechInputFeedback;
import pasa.cbentley.framework.datamodel.src4.table.ObjectTableModel;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.cmd.CmdFactoryGui;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceGui;
import pasa.cbentley.framework.gui.src4.cmd.CmdProcessorGui;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.exec.InputStateCanvasGui;
import pasa.cbentley.framework.gui.src4.exec.OutputStateCanvasGui;
import pasa.cbentley.framework.gui.src4.factories.TableCellPolicyFactory;
import pasa.cbentley.framework.gui.src4.factories.TablePolicyFactory;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsGui;
import pasa.cbentley.framework.gui.src4.interfaces.IUIView;
import pasa.cbentley.framework.gui.src4.string.RequestStringInput;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.table.interfaces.ITechTable;
import pasa.cbentley.framework.gui.src4.tech.ITechLinks;

/**
 * Extension of {@link MCmdAbstract} for the {@link GuiCtx}.
 * 
 * <p>
 * They are created by {@link CmdFactoryGui} and kept there as singletons
 * </p>
 * 
 * <p>
 * Provide services to commands that want to fetch parameters using a GUI.
 * Shows a {@link TableView} and handles some events
 * 
 * Only the command implementation knows the layout of the form.
 * It might be a simple list or a complex set of check boxes within a wizard with Previous-Next-Cancel
 * </p>
 * 
 * @author Charles Bentley
 *
 */
public abstract class MCmdAbstractGui extends MCmdAbstract implements ITechInputFeedback, ICmdsGui, IEventConsumer {

   private CmdInstanceGui activeCmdMenuPos;

   protected final GuiCtx gc;

   protected TableView    tableParamSelection;

   public MCmdAbstractGui(GuiCtx gc, int cmdId) {
      super(gc.getCC(), cmdId);
      this.gc = gc;

      //#debug
      toDLog().pCreate("", this, MCmdAbstractGui.class, "Created@43", LVL_04_FINER, true);

   }

   public final void cmdExecuteFinal(CmdInstance cd) {
      CmdInstanceGui ci = (CmdInstanceGui) cd;
      this.cmdExecuteFinalGui(ci);
   }

   /**
    * {@link InputConfig} provides everything the cmd needs for execution and providing results.
    * <li>    {@link InputConfig#getCRD()} {@link OutputStateCanvasGui}
    * <li>    {@link InputConfig#getISD()} {@link InputStateCanvasGui}
    * @param ic
    */
   public abstract void cmdExecuteFinalGui(CmdInstanceGui ci);

   /**
    * By default the cmd is paramed. invalidate if parameters could not be computed
    */
   public final void cmdExecuteParam(CmdInstance cd) {
      CmdInstanceGui ci = (CmdInstanceGui) cd;
      if (ci.isCanceled()) {
         //close the table drawable if active
         this.cmdCanceledGui(ci);
      } else {
         ci.setParamed();
         this.cmdExecuteParamGui(ci);
      }
   }

   public void cmdCanceledGui(CmdInstanceGui ci) {

   }

   /**
    * Invalite state {@link ITechCmd#STATE_FLAG_09_PARAMED} if parameters are not done when method returns
    * 
    * <li> {@link CmdInstance#setParamed()}
    * 
    * <p>
    * 
    * Manage the {@link CmdInstance#getDirective()}
    * <li> {@link ITechCmd#DIRECTIVE_5_SET} In this caes, this method is not called.
    * <li> {@link ITechCmd#DIRECTIVE_2_ITERATE_UP} for example.
    * 
    * </p>
    * 
    * @param ci {@link CmdInstanceGui} for which to compute the parameters.
    */
   public void cmdExecuteParamGui(CmdInstanceGui ci) {
      //by default no parameters
   }

   /**
    * <b>Event Handling</b> <br>
    * {@link TableView} is often used in the same purpose as {@link RequestStringInput}.
    * <br>
    * <li>a {@link MCmd} requires input. Model create a {@link TableView} with its data model to choose data from.
    * <li>Registers as an {@link ICmdListener} to the {@link ICmdsCmd#CMD_ID_SELECT} {@link MCmd}
    * <li>Table is show, User selects, User fire the Selcet Command of the Table.
    * <li>Select cmd has a listener, so fire it. Original caller gets the TableRefence, DataModel, and eventually Data. 
    * <br>
    * Default Event Hook ups for TableView users? Navigation within the table is
    * automatic Selection 
    * 
    * This event is the result of an OK cmd
    * 
    */
   public void consumeEvent(BusEvent e) {
      Object producer = e.getProducer();
      if (producer == tableParamSelection) {
         int row = tableParamSelection.getSelectedIndex();
         // The active instance command used when fetching command parameters in a GUI
         CmdInstanceGui activeCmdLang = (CmdInstanceGui) e.getParamO1();
         ObjectTableModel model = (ObjectTableModel) tableParamSelection.getTableModel();

         processSelection(activeCmdLang, row, model);

         //#debug
         toDLog().pEvent(" Selected Index=" + row + " out of " + model.getNumElements(), this, CmdProcessorGui.class, "consumeEvent", LVL_05_FINE, true);

         cc.executeInstance(activeCmdLang);
         //hide table view
      }

      //TODO if cancel
   }

   /**
    * Override when parameter selection is not one of supported scenarios.
    * 
    * @param ci
    * @param index
    * @param model
    */
   protected void processSelection(CmdInstanceGui ci, int index, ObjectTableModel model) {

   }

   public void showListParameters(CmdInstanceGui ci, IntToObjects data) {
      TableCellPolicyFactory cellFac = gc.getTableCellPolicyFactory();
      TablePolicyFactory tabFac = gc.getTablePolicyFactory();
      ByteObject colPol = cellFac.getGeneric(2, 0);
      ByteObject rowPol = cellFac.getGeneric(0, 0);
      ByteObject policyTable = tabFac.getTablePolicy(colPol, rowPol);
      StyleClass scMenu = gc.getStyleClass(IUIView.SC_1_MENU);
      StyleClass scMenuItems = scMenu.getSCNotNull(ITechLinks.LINK_74_STYLE_CLASS_MENU);
      TableView tv = new TableView(gc, scMenuItems, policyTable);
      //the 
      CmdNode node = cc.createCmdNode(cc.getCmdNodeRoot(), "paramFetcher");

      node.setCmdPending(ci);
      tv.setCmdNode(node);

      //associates OK and Cancel commands to some events. Esc key to Cancel / Enter to Ok

      ObjectTableModel otm = new ObjectTableModel(gc.getDMC(), data, 1);
      tv.setDataModel(otm);

      tv.getLay().layPoz_BotToBot_OfParent();
      tv.getLay().layPoz_EndToEnd_Parent();

      tv.addEventListener(this, ITechTable.EVENT_ID_00_SELECT);

      //add cancel/ok buttons
      this.tableParamSelection = tv;

      activeCmdMenuPos = ci;
   }

   /**
    * 
    * @param ec
    */
   public void tableSelection(ExecutionContextCanvasGui ec) {

   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, MCmdAbstractGui.class, toStringGetLine(200));
      toStringPrivate(dc);
      super.toString(dc.sup());
      
      dc.nlLvl(tableParamSelection, "tableParamSelection");

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, MCmdAbstractGui.class, toStringGetLine(200));
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {
      
   }
   //#enddebug
   


}
