package pasa.cbentley.framework.gui.src4.cmd;

import pasa.cbentley.framework.cmd.src4.engine.CmdFactoryAbstract;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdsCmd;
import pasa.cbentley.framework.gui.src4.cmd.mcmd.MCmdGuiChangeCanvasDebugger;
import pasa.cbentley.framework.gui.src4.cmd.mcmd.MCmdGuiChangeMenuLocation;
import pasa.cbentley.framework.gui.src4.cmd.mcmd.MCmdGuiNewStart;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsGui;

public class CmdFactoryGui extends CmdFactoryAbstract implements ICmdsCmd, ICmdsGui {

   private MCmd                        cCue;

   protected MCmd                      cGoTo;

   private MCmd                        cHide;

   protected MCmd                      cInverse;

   private MCmdGuiChangeCanvasDebugger cmdGuiChangeCanvasDebugger;

   private MCmdGuiChangeMenuLocation   cmdGuiChangeMenuLocation;

   private MCmdGuiNewStart             cmdGuiNewStart;

   private MCmd                        cNavUp;

   private MCmd                        cPageNext;

   private MCmd                        cPagePrev;

   protected MCmd                      cShowFilters;

   private MCmd                        cUIAToggle;

   protected final GuiCtx              gc;


   public CmdFactoryGui(GuiCtx gc) {
      super(gc.getCC());
      this.gc = gc;
   }

   public MCmd get(String str, int id) {
      MCmd c = createCmdIDLabel(id, str);
      return c;
   }

   public MCmd getCmd(int id) {
      MCmd cmd = null;
      switch (id) {
         case CMD_GUI_00_LAST_LOGIN:
            cmd = getCmdLastLogin();
            break;
         case CMD_GUI_11_SORT_INVERSE:
            cmd = getCmdSortInverse();
            break;
         case CMD_GUI_12_GO_TO:
            cmd = getCmdGoto();
            break;
         case CMD_GUI_10_SHOW_FILTERS:
            cmd = getCmdFilters();
            break;
         case CMD_GUI_13_HIDE:
            cmd = getCmdHide();
            break;
         case CMD_GUI_02_UIA_TOGGLE:
            cmd = getCmdUIAToggle();
            break;
         case CMD_GUI_08_PAGE_NEXT:
            cmd = getCmdPageNext();
            break;
         case CMD_GUI_09_PAGE_PREVIOUS:
            cmd = getCmdPagePrevious();
            break;
         case CMD_GUI_16_CUE_PARAM_NUMBER:
            cmd = getCmdCue();
            break;
         default:
            break;
      }

      if (cmd == null) {
         cmd = getNavCmd(id);
      }
      return cmd;
   }

   private MCmd getCmdCue() {
      if (cCue == null) {
         cCue = get("Cue Pointer", CMD_GUI_16_CUE_PARAM_NUMBER);
      }
      return cCue;
   }

   private MCmd getCmdFilters() {
      if (cShowFilters == null) {
         cShowFilters = get("Show Filters", CMD_GUI_08_PAGE_NEXT);
      }
      return cShowFilters;
   }

   public MCmd getCmdGoto() {
      if (cGoTo == null) {
         cGoTo = get("Go To", CMD_GUI_12_GO_TO);
      }
      return cGoTo;
   }

   private MCmd getCmdHide() {
      if (cHide == null) {
         cHide = get("Hide", CMD_GUI_13_HIDE);
      }
      return cHide;
   }

   public MCmd getCmdSortInverse() {
      if (cInverse == null) {
         cInverse = get("Sort ASC", CMD_GUI_11_SORT_INVERSE);
      }
      return cInverse;
   }

   private MCmd getCmdLastLogin() {
      if (cmdGuiNewStart == null) {
         cmdGuiNewStart = new MCmdGuiNewStart(gc);
      }
      return cmdGuiNewStart;
   }

   public MCmd getCmdNavUp() {
      if (cNavUp == null) {
         cNavUp = get("NavUp", CMD_11_NAV_UP);
      }
      return cNavUp;
   }

   private MCmd getCmdPageNext() {
      if (cPageNext == null) {
         cPageNext = get("Page Next", CMD_GUI_08_PAGE_NEXT);
      }
      return cPageNext;
   }

   private MCmd getCmdPagePrevious() {
      if (cPagePrev == null) {
         cPagePrev = get("Page Previous", CMD_GUI_09_PAGE_PREVIOUS);
      }
      return cPagePrev;
   }

   private MCmd getCmdUIAToggle() {
      if (cUIAToggle == null) {
         cUIAToggle = get("UIA Toggle", CMD_GUI_02_UIA_TOGGLE);
      }
      return cUIAToggle;
   }

   public MCmdGuiChangeCanvasDebugger getMCmdGuiChangeCanvasDebugger() {
      if (cmdGuiChangeCanvasDebugger == null) {
         cmdGuiChangeCanvasDebugger = new MCmdGuiChangeCanvasDebugger(gc);
      }
      return cmdGuiChangeCanvasDebugger;
   }

   public MCmdGuiChangeMenuLocation getMCmdGuiChangeMenuLocation() {
      if (cmdGuiChangeMenuLocation == null) {
         cmdGuiChangeMenuLocation = new MCmdGuiChangeMenuLocation(gc);
      }
      return cmdGuiChangeMenuLocation;
   }

   private MCmd getNavCmd(int id) {
      MCmd cmd = null;
      switch (id) {
         case CMD_11_NAV_UP:
         case CMD_12_NAV_DOWN:
         case CMD_13_NAV_LEFT:
         case CMD_14_NAV_RIGHT:
         case CMD_15_NAV_SELECT:
         case CMD_27_NAV_SCROLL_UP:
         case CMD_25_NAV_UP_SELECT:
         case CMD_18_NAV_PRE_SELECT:
            cmd = gc.getNavigator().getNavCmd(CMD_18_NAV_PRE_SELECT);
      }
      return cmd;
   }
}
