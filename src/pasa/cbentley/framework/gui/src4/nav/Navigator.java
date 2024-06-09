package pasa.cbentley.framework.gui.src4.nav;

import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.cmd.src4.engine.CmdNode;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.cmd.src4.trigger.CmdTrigger;
import pasa.cbentley.framework.cmd.src4.trigger.FacTrig;
import pasa.cbentley.framework.coreui.src4.tech.ITechCodes;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.cmd.ViewCommandListener;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ObjectGC;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsView;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

public class Navigator extends ObjectGC implements ICmdsView {

   protected CmdNode navCtx;

   public Navigator(GuiCtx gc) {
      super(gc);
      initDefNav();
   }

   public MCmd getNavCmd(int cmdid) {
      return new NavigationCmd(gc, 0, cmdid);
   }

   public void cmdPopActive(InputConfig ic) {

   }

   /**
    * Position {@link IDrawable} with Pozers so that it is drawn
    * on top aligned left
    * @param ic the execution context
    * @param src
    * @param dir
    * @param insert
    */
   public void navInsert(InputConfig ic, IDrawable src, int dir, IDrawable insert) {
      
   }

   /**
    * Remove Drawable and reattach links
    * @param src
    * @param remove
    */
   public void navRemove(InputConfig ic, IDrawable src, IDrawable remove) {
      
   }

   public CmdNode getCtxNav() {
      return navCtx;
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
      navCtx = new CmdNode(cc, "nav", 0, null);

      //navigate command that requires a parameter
      NavigationCmd navCmd = new NavigationCmd(gc, 0);

      CmdTrigger ctT = triggerFactory.createKeyP(ITechCodes.KEY_DOWN);
      navCtx.addCmdLinkRepeated(ctT, navCmd);
      
      //#debug
      toDLog().pCmd("Before", ctT, Navigator.class, "initDefNav@76");

      CmdTrigger ctB = triggerFactory.createKeyP(ITechCodes.KEY_UP);
      navCtx.addCmdLinkRepeated(ctB, navCmd);
      CmdTrigger ctL = triggerFactory.createKeyP(ITechCodes.KEY_LEFT);
      navCtx.addCmdLinkRepeated(ctL, navCmd);
      CmdTrigger ctR = triggerFactory.createKeyP(ITechCodes.KEY_RIGHT);
      navCtx.addCmdLinkRepeated(ctR, navCmd);

      CmdTrigger ctF = triggerFactory.createKeyP(ITechCodes.KEY_FIRE);
      navCtx.addCmdLink(ctF, navCmd);
      CmdTrigger ctES = triggerFactory.createKeyP(ITechCodes.KEY_ESCAPE);
      navCtx.addCmdLink(ctES, navCmd);

      CmdTrigger ctWheelUp = triggerFactory.createPointerPressed(ITechCodes.PBUTTON_3_WHEEL_UP);
      navCtx.addCmdLink(ctWheelUp, navCmd);
      CmdTrigger ctWheelDown = triggerFactory.createPointerPressed(ITechCodes.PBUTTON_4_WHEEL_DOWN);
      navCtx.addCmdLink(ctWheelDown, navCmd);
      //first press is a CUE_SELECT
      CmdTrigger ctPointer1 = triggerFactory.createPointerPressed(ITechCodes.PBUTTON_0_DEFAULT);
      navCtx.addCmdLink(ctPointer1, CMD_18_NAV_PRE_SELECT);

      CmdTrigger ctPointerDrag = triggerFactory.create1stPointerDrag(ITechCodes.PBUTTON_0_DEFAULT);
      navCtx.addCmdLink(ctPointerDrag, CMD_18_NAV_PRE_SELECT);

      //first press is a CUE_SELECT. some nav context will want a pointer press selection
      //while others want 
      CmdTrigger ctPointerType = triggerFactory.createPointer(ITechCodes.PBUTTON_0_DEFAULT);
      navCtx.addCmdLink(ctPointerType, navCmd);

      CmdTrigger ctEx = triggerFactory.createTypedRepeat(ITechCodes.KEY_ESCAPE, 5, ITechCodes.TIMING_3_FAST);
      cc.getNodeRoot().addCmdLink(ctEx, CMD_03_EXIT);
      CmdTrigger ctF1 = triggerFactory.createKeyP(ITechCodes.KEY_F1);
      cc.getNodeRoot().addCmdLink(ctF1, CMD_19_HELP);

      //#debug
      toDLog().pCmd("After", navCtx, Navigator.class, "initDefNav");
   }

   public void setNavCtx(CmdNode ct) {
      navCtx = ct;
   }

   /**
    * Make sure the Drawable close {@link CmdCtx} has the Nav Branch attached.
    * @param d
    * @param b
    */
   public void setNavCtx(Drawable d, boolean b) {
      if (b) {
         d.getCmdNode().addBranch(navCtx);
      } else {
         d.getCmdNode().removeBranch(navCtx);
      }
   }
}
