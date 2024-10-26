package pasa.cbentley.framework.gui.src4.cmd.mcmd;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.event.IEventConsumer;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.structs.IntToObjects;
import pasa.cbentley.framework.cmd.src4.cmd.MCmdAbstract;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdsCmd;
import pasa.cbentley.framework.datamodel.src4.table.ObjectTableModel;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceGui;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.menu.CmdMenuBar;
import pasa.cbentley.framework.gui.src4.menu.IMenus;

/**
 * 
 * @author Charles Bentley
 *
 */
public class MCmdGuiChangeMenuLocation extends MCmdAbstractGui implements IEventConsumer {

   public MCmdGuiChangeMenuLocation(GuiCtx gc) {
      super(gc, ICmdsCmd.CMD_75_CHANGE_MENU_POSITION);
   }

   public void cmdExecuteFinalGui(CmdInstanceGui ci) {
      CmdMenuBar cm = gc.getCanvasGCRoot().getMenuBar();
      if (cm == null) {
         ci.actionDone();
      } else {
         ByteObject boMenuBar = cm.getMenuBarTech();
         int currentValue = boMenuBar.get1(IMenus.MENUS_OFFSET_02_POSITION1);
         ci.setParamUndoInt(currentValue);
         int valueNew = ci.getParamInt1();
         boMenuBar.set1(IMenus.MENUS_OFFSET_02_POSITION1, valueNew);
         int responseType = FLAG_04_RENEW_LAYOUT | FLAG_02_FULL_REPAINT | FLAG_06_DATA_REFRESH;
         ci.actionDone(null, responseType);
      }
   }

   public void cmdExecuteParamGui(CmdInstanceGui ci) {
      //default menu bar is the one of the root canvas
      int directive = ci.getDirective();
      if (directive == DIRECTIVE_4_ASK_GUI) {
         String[] data = new String[] { "Top", "Bottom", "Left", "Right" };
         int[] values = new int[] { C.POS_0_TOP, C.POS_1_BOT, C.POS_2_LEFT, C.POS_3_RIGHT };
         IntToObjects its = new IntToObjects(cc.getUC(), data, values);
         super.showListParameters(ci, its);
         ci.setParametersRequested();
      } else if (directive == DIRECTIVE_3_ITERATE_DOWN) {
         directiveIterate(directive);
      } else if (directive == DIRECTIVE_2_ITERATE_UP) {
         directiveIterate(directive);
      }

   }

   private void directiveIterate(int directive) {
      CmdMenuBar cm = gc.getCanvasGCRoot().getMenuBar();
      ByteObject boMenuBar = cm.getMenuBarTech();
      int pos = boMenuBar.get1(IMenus.MENUS_OFFSET_02_POSITION1);
      if (directive == DIRECTIVE_3_ITERATE_DOWN) {
         pos = (pos + 1) % 4;
      } else if (directive == DIRECTIVE_2_ITERATE_UP) {
         pos = (pos - 1);
         if (pos < 0) {
            pos = C.POS_3_RIGHT;
         }
      }
      ci.setParamInt1(pos);
   }

   /**
    * 
    */
   public void cmdUndo(CmdInstance ci) {
      CmdMenuBar cm = gc.getCanvasGCRoot().getMenuBar();
      ByteObject boMenuBar = cm.getMenuBarTech();

      //tell success otherwise assumed failed
      int old = ci.getParamUndoInt();
      //save other for any redo

      boMenuBar.set1(IMenus.MENUS_OFFSET_02_POSITION1, old);
      int responseType = FLAG_04_RENEW_LAYOUT | FLAG_02_FULL_REPAINT | FLAG_06_DATA_REFRESH;
      ci.actionDone(null, responseType);

      ci.setFlagStateUndoneSuccess();
   }

   public MCmdAbstract createInstance(CmdInstance ci) {
      return new MCmdGuiChangeMenuLocation(gc);
   }

   protected void processSelection(CmdInstanceGui ci, int index, ObjectTableModel model) {
      int data = model.getIntValue(index);
      ci.setParamInt1(data);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, MCmdGuiChangeMenuLocation.class, 98);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, MCmdGuiChangeMenuLocation.class, 98);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }
   //#enddebug

}
