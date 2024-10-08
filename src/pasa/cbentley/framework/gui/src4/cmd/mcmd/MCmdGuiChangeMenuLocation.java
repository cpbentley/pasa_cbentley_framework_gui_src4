package pasa.cbentley.framework.gui.src4.cmd.mcmd;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.core.src4.event.IEventConsumer;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.datamodel.src4.table.ObjectTableModel;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.factories.TableCellPolicyFactory;
import pasa.cbentley.framework.gui.src4.factories.TablePolicyFactory;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsGui;
import pasa.cbentley.framework.gui.src4.interfaces.IUIView;
import pasa.cbentley.framework.gui.src4.menu.CmdMenuBar;
import pasa.cbentley.framework.gui.src4.menu.IMenus;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.table.interfaces.ITechTable;
import pasa.cbentley.framework.gui.src4.tech.ITechLinks;

/**
 * 
 * @author Charles Bentley
 *
 */
public class MCmdGuiChangeMenuLocation extends MCmdGui implements IEventConsumer {

   private CmdInstance activeCmdMenuPos;

   private TableView   menuPosTable;

   public MCmdGuiChangeMenuLocation(GuiCtx gc) {
      super(gc, ICmdsGui.CMD_75_CHANGE_MENU_POSITION);
   }

   public void consumeEvent(BusEvent e) {
      if (e.getProducer() == menuPosTable) {
         int row = menuPosTable.getSelectedRow();
         //hide table view
         ObjectTableModel otm = (ObjectTableModel) menuPosTable.getTableModel();
         String menuPositionStr = (String) otm.getObject(row, 0);
         activeCmdMenuPos.setParamO(menuPositionStr);
         cc.commandAction(activeCmdMenuPos);
      }
   }

   public void execute(ExecutionContextCanvasGui ec) {
      CmdInstance cmd = ec.getCmdInstance();
      //default menu bar is the one of the root canvas
      CmdMenuBar cm = gc.getCanvasGCRoot().getMenuBar();
      ByteObject boMenuBar = cm.getMenuBarTech();
      int pos = boMenuBar.get1(IMenus.MENUS_OFFSET_02_POSITION1);
      
      //iterate version of command
      if (cmd.hasFlag(CmdInstance.STATE_FLAG_2_CANCELED))
         if (pos == C.POS_0_TOP) {
            pos = C.POS_2_LEFT;
         } else if (pos == C.POS_2_LEFT) {
            pos = C.POS_1_BOT;
         } else if (pos == C.POS_1_BOT) {
            pos = C.POS_3_RIGHT;
         } else if (pos == C.POS_3_RIGHT) {
            pos = C.POS_0_TOP;
         }
      if (menuPosTable == null) {
         String[] data = new String[] { "Top", "Bottom", "Left", "Right" };
         TableCellPolicyFactory cellFac = gc.getTableCellPolicyFactory();
         TablePolicyFactory tabFac = gc.getTablePolicyFactory();
         ByteObject colPol = cellFac.getGeneric(2, 0);
         ByteObject rowPol = cellFac.getGeneric(0, 0);
         ByteObject policyTable = tabFac.getTablePolicy(colPol, rowPol);
         StyleClass scMenu = gc.getStyleClass(IUIView.SC_1_MENU);
         StyleClass scMenuItems = scMenu.getSCNotNull(ITechLinks.LINK_74_STYLE_CLASS_MENU);
         TableView tv = new TableView(gc, scMenuItems, policyTable);
         ObjectTableModel otm = new ObjectTableModel(gc.getDMC(), data, 1);
         tv.setDataModel(otm);

         tv.getLay().layPoz_BotToBot_OfParent();
         tv.getLay().layPoz_EndToEnd_Parent();

         tv.addEventListener(this, ITechTable.EVENT_ID_00_SELECT);
         //show the form as child of current front drawable
         menuPosTable = tv;
         activeCmdMenuPos = cmd;
      }
      //write feedback. user String Top,Bottom,Left,Right
      boMenuBar.set1(IMenus.MENUS_OFFSET_02_POSITION1, pos);
      int responseType = FLAG_04_RENEW_LAYOUT | FLAG_02_FULL_REPAINT | FLAG_06_DATA_REFRESH;
      
      cmd.actionDone(null, responseType);
   }

}
