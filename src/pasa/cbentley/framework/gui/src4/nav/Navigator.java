package pasa.cbentley.framework.gui.src4.nav;

import pasa.cbentley.core.src4.interfaces.ITechNav;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.cmd.src4.engine.CmdFactoryCore;
import pasa.cbentley.framework.cmd.src4.engine.CmdNode;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdsCmd;
import pasa.cbentley.framework.cmd.src4.trigger.CmdTrigger;
import pasa.cbentley.framework.cmd.src4.trigger.FacTrig;
import pasa.cbentley.framework.core.ui.src4.tech.ITechCodes;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ObjectGC;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsGui;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

public class Navigator extends ObjectGC implements ICmdsCmd, ICmdsGui {

   protected CmdNode cmdNodeNav;
   private CmdNode cmdNodeTblrSelect;

   public Navigator(GuiCtx gc) {
      super(gc);
      initDefNav();
   }

   public void cmdPopActive(ExecutionContextCanvasGui ic) {

   }

   public CmdNode getCmdNodeNav() {
      return cmdNodeNav;
   }

   /**
    * The {@link CmdNode} with up,down,left,right and select commands
    * @return
    */
   public CmdNode getCmdNodeNavTBLRSelect() {
      if(cmdNodeTblrSelect == null) {
         cmdNodeTblrSelect = cc.createCmdNode("navtblrselect");
         this.addTBLRMoves(cmdNodeTblrSelect);
         this.addFireEscape(cmdNodeTblrSelect);
      }
      return cmdNodeTblrSelect;
   }
   /**      
    * not all navigation node will have
    * 
    * @param cmdNode
    */
   private void addScrollUpDownToNode(CmdNode cmdNode) {
      FacTrig triggerFactory = cc.getFacTrig();
      CmdFactoryCore cmdFactoryCore = cc.getCmdFactoryCore();

      //scroll up cmd
      CmdTrigger ctWheelUp = triggerFactory.createPointerPressed(ITechCodes.PBUTTON_3_WHEEL_UP);
      cmdNode.addCmdLink(ctWheelUp, cmdFactoryCore.getCmdNavScrollUp());
      CmdTrigger ctWheelDown = triggerFactory.createPointerPressed(ITechCodes.PBUTTON_4_WHEEL_DOWN);
      cmdNode.addCmdLink(ctWheelDown, cmdFactoryCore.getCmdNavScrollDown());
      //first press is a CUE_SELECT
   }

   public void addFireEscape(CmdNode cmdNode) {
      CmdFactoryCore cmdFactoryCore = cc.getCmdFactoryCore();
      cmdNodeNav.addCmdLink(ITechCodes.KEY_FIRE, cmdFactoryCore.getCmdNavSelect());
      cmdNodeNav.addCmdLink(ITechCodes.KEY_ESCAPE, cmdFactoryCore.getCmdNavUnSelect());

   }

   public void addTBLRMoves(CmdNode cmdNode) {
      CmdFactoryCore cmdFactoryCore = cc.getCmdFactoryCore();
      cmdNode.addCmdLinkRepeated(ITechCodes.KEY_UP, cmdFactoryCore.getCmdNavUp());
      cmdNode.addCmdLinkRepeated(ITechCodes.KEY_DOWN, cmdFactoryCore.getCmdNavDown());
      cmdNode.addCmdLinkRepeated(ITechCodes.KEY_LEFT, cmdFactoryCore.getCmdNavLeft());
      cmdNode.addCmdLinkRepeated(ITechCodes.KEY_RIGHT, cmdFactoryCore.getCmdNavRight());
   }

   /**
    * There can be several kind of nav context.
    * <br>
    * Menu navigation will use single click for select
    * <br>
    * Attach the navigation context needed to the Drawable context
    */
   public void initDefNav() {
      FacTrig triggerFactory = cc.getFacTrig();
      cmdNodeNav = new CmdNode(cc, "nav", 0, null);

      //#debug
      toDLog().pCmd("Before", cmdNodeNav, Navigator.class, "initDefNav@76");

      CmdFactoryCore cmdFactoryCore = cc.getCmdFactoryCore();
      cmdNodeNav.addCmdLinkRepeated(ITechCodes.KEY_UP, cmdFactoryCore.getCmdNavUp());
      cmdNodeNav.addCmdLinkRepeated(ITechCodes.KEY_DOWN, cmdFactoryCore.getCmdNavDown());
      cmdNodeNav.addCmdLinkRepeated(ITechCodes.KEY_LEFT, cmdFactoryCore.getCmdNavLeft());
      cmdNodeNav.addCmdLinkRepeated(ITechCodes.KEY_RIGHT, cmdFactoryCore.getCmdNavRight());

      cmdNodeNav.addCmdLink(ITechCodes.KEY_FIRE, cmdFactoryCore.getCmdNavSelect());
      cmdNodeNav.addCmdLink(ITechCodes.KEY_ESCAPE, cmdFactoryCore.getCmdNavUnSelect());

      CmdTrigger ctPointer1 = triggerFactory.createPointerPressed(ITechCodes.PBUTTON_0_DEFAULT);
      cmdNodeNav.addCmdLink(ctPointer1, CMD_18_NAV_PRE_SELECT);

      CmdTrigger ctPointerDrag = triggerFactory.create1stPointerDrag(ITechCodes.PBUTTON_0_DEFAULT);
      cmdNodeNav.addCmdLink(ctPointerDrag, CMD_18_NAV_PRE_SELECT);

      //first press is a CUE_SELECT. some nav context will want a pointer press selection
      //while others want 
      CmdTrigger ctPointerType = triggerFactory.createPointer(ITechCodes.PBUTTON_0_DEFAULT);
      cmdNodeNav.addCmdLink(ctPointerType, CMD_18_NAV_PRE_SELECT);

      //5 typed fast for exiting with a confirm dialog
      CmdTrigger ctEx = triggerFactory.createTypedRepeat(ITechCodes.KEY_ESCAPE, 5, ITechCodes.TIMING_3_FAST);
      cc.getCmdNodeRoot().addCmdLink(ctEx, CMD_03_EXIT);
      CmdTrigger ctF1 = triggerFactory.createKeyP(ITechCodes.KEY_F1);
      cc.getCmdNodeRoot().addCmdLink(ctF1, CMD_39_HELP);

      //#debug
      toDLog().pCmd("After", cmdNodeNav, Navigator.class, "initDefNav");
   }

   /**
    * Position {@link IDrawable} with Pozers so that it is drawn
    * on top aligned left
    * @param ic the execution context
    * @param src
    * @param dir
    * @param insert
    */
   public void navInsert(ExecutionContextCanvasGui ic, IDrawable src, int dir, IDrawable insert) {

   }

   /**
    * Remove Drawable and reattach links
    * @param src
    * @param remove
    */
   public void navRemove(ExecutionContextCanvasGui ic, IDrawable src, IDrawable remove) {

   }

   public void setNavCtx(CmdNode cmdNode) {
      cmdNodeNav = cmdNode;
   }

   /**
    * Make sure the Drawable close {@link CmdCtx} has the Nav Branch attached.
    * @param d
    * @param b
    */
   public void setNavCtx(Drawable d, boolean b) {
      if (b) {
         d.getCmdNode().addBranch(cmdNodeNav);
      } else {
         d.getCmdNode().removeBranch(cmdNodeNav);
      }
   }
}
