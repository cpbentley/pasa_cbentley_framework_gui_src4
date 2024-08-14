package pasa.cbentley.framework.gui.src4.cmd;

import pasa.cbentley.framework.cmd.src4.engine.CmdFactoryCore;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsGui;

public class CmdMapperGui extends CmdFactoryCore implements ICmdsGui {

   private MCmd                        cCue;

   protected MCmd                      cGoTo;

   private MCmd                        cHide;

   protected MCmd                      cInverse;

   private MCmd                        cNavUp;

   private MCmd                        cPageNext;

   private MCmd                        cPagePrev;

   protected MCmd                      cShowFilters;

   private MCmd                        cUIAToggle;

   protected final CmdProcessorGui vc;

   protected final GuiCtx              gc;

   public CmdMapperGui(GuiCtx gc) {
      super(gc.getCC());
      this.gc = gc;
      vc = gc.getCmdProcessorGui();
   }

   public MCmd get(String str, int id) {
      MCmd c = new MCmd(cc, str);
      c.setCmdId(id);
      return c;
   }

   public MCmd getCmd(int id) {
      MCmd cmd = super.getCmd(id);
      if (cmd != null) {
         return cmd;
      }
      switch (id) {
         case VCMD_11_INVERSE:
            cmd = getCmdInverse();
            break;
         case VCMD_12_GO_TO:
            cmd = getCmdGoto();
            break;
         case VCMD_10_SHOW_FILTERS:
            cmd = getCmdFilters();
            break;
         case VCMD_13_HIDE:
            cmd = getCmdHide();
            break;
         case VCMD_02_UIA_TOGGLE:
            cmd = getCmdUIAToggle();
            break;
         case VCMD_08_PAGE_NEXT:
            cmd = getCmdPageNext();
            break;
         case VCMD_09_PAGE_PREVIOUS:
            cmd = getCmdPagePrevious();
            break;
         case VCMD_16_CUE_PARAM_NUMBER:
            cmd = getCmdCue();
            break;
         default:
            break;
      }
      if (cmd == null) {
         cmd = getCmdC(id);
      }
      if (cmd == null) {
         cmd = getNavCmd(id);
      }
      return cmd;
   }

   public MCmd getCmdC(int id) {
      MCmd cmd = null;
      switch (id) {
         case CMD_11_NAV_UP:

            break;

         default:
            break;
      }
      return cmd;
   }

   private MCmd getCmdCue() {
      if (cCue == null) {
         cCue = get("Cue Pointer", ICmdsGui.VCMD_16_CUE_PARAM_NUMBER);
      }
      return cCue;
   }

   private MCmd getCmdFilters() {
      if (cShowFilters == null) {
         cShowFilters = get("Show Filters", ICmdsGui.VCMD_08_PAGE_NEXT);
      }
      return cShowFilters;
   }

   public MCmd getCmdGoto() {
      if (cGoTo == null) {
         cGoTo = get("Go To", ICmdsGui.VCMD_12_GO_TO);
      }
      return cGoTo;
   }

   private MCmd getCmdHide() {
      if (cHide == null) {
         cHide = get("Hide", ICmdsGui.VCMD_13_HIDE);
      }
      return cHide;
   }

   public MCmd getCmdInverse() {
      if (cInverse == null) {
         cInverse = get("Sort ASC", ICmdsGui.VCMD_11_INVERSE);
      }
      return cInverse;
   }

   public MCmd getCmdNavUp() {
      if (cNavUp == null) {
         cNavUp = get("NavUp", ICmdsGui.CMD_11_NAV_UP);
      }
      return cNavUp;
   }

   private MCmd getCmdPageNext() {
      if (cPageNext == null) {
         cPageNext = get("Page Next", ICmdsGui.VCMD_08_PAGE_NEXT);
      }
      return cPageNext;
   }

   private MCmd getCmdPagePrevious() {
      if (cPagePrev == null) {
         cPagePrev = get("Page Previous", ICmdsGui.VCMD_09_PAGE_PREVIOUS);
      }
      return cPagePrev;
   }

   private MCmd getCmdUIAToggle() {
      if (cUIAToggle == null) {
         cUIAToggle = get("UIA Toggle", ICmdsGui.VCMD_02_UIA_TOGGLE);
      }
      return cUIAToggle;
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
