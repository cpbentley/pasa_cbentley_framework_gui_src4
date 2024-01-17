package pasa.cbentley.framework.gui.src4.string;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.engine.CmdNode;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdListener;
import pasa.cbentley.framework.cmd.src4.interfaces.ICommandable;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.core.ViewDrawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.forms.ListRoot;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IValidable;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;

/**
 * Used to show a dialog asking the user for a string. This is always part of a {@link CmdInstance} that is not yet
 * executed. The {@link InputRequestStr} job is to get the string parameter for the command.
 * <br>
 * Or just gets a confirmation or cancel
 * <br>
 * <br>
 * May also show a selection choice such as {@link ListRoot}.
 * <br>
 * When the user commands 
 * <li>{@link CmdController#CMD_04_OK}
 * <li>{@link CmdController#CMD_06_APPLY}
 * <li>{@link CmdController#CMD_05_CANCEL}
 * <br>
 * <br>
 * The {@link InputRequestStr} creates a command with the String in parameter
 * This is the mechanism used for getting the feedback to the object requesting
 * <br>
 * <br>
 * 
 * @author Charles-Philip
 *
 */
public class InputRequestStr implements ICommandable {

   CmdCtx                 cc;

   CmdInstance            ci;

   CmdNode            ctxEditStr;

   protected final GuiCtx gc;

   ICommandable           icon;

   private StringDrawable sd;

   IValidable             validator;

   public InputRequestStr(GuiCtx gc, StyleClass scStr, String title) {

      this.gc = gc;
      cc = gc.getCC();
      ctxEditStr = cc.createCmdNode(null, "EditStr");
      ctxEditStr.addMenuCmd(cc.CMD_04_OK);
      ctxEditStr.addMenuCmd(cc.CMD_05_CANCEL);

      ctxEditStr.setListener(this);

      sd = new StringDrawable(gc, scStr, "");

      sd.setCmdCtx(ctxEditStr);

      sd.getEditModule();

      StringDrawable sdE = new StringDrawable(gc, scStr, title);

      sd.setHeader(sdE, C.POS_0_TOP, ITechViewPane.PLANET_MODE_1_EXPAND);
      //must be called before the init!
      sd.setXYLogic(C.LOGIC_2_CENTER, C.LOGIC_3_BOTTOM_RIGHT);
      sd.init(300, -1);
   }

   /**
    * TODO string validation?
    */
   public void commandAction(CmdInstance cmd) {
      if (cmd.cmdID == cc.CMD_04_OK || cmd.cmdID == cc.CMD_04_OK) {
         ci.paramO = sd.getString();
         icon.commandAction(ci);
         sd.removeEditControl();
         //ShowHelper.hide(sd);
      } else {
         //hide
         sd.removeEditControl();
         //ShowHelper.hide(sd);
      }
   }

   public MCmd getCmd(int vcmdid) {
      // TODO Auto-generated method stub
      return null;
   }

   public CmdNode getCmdNode() {
      return ctxEditStr;
   }

   public ViewDrawable getView() {
      return sd;
   }

   public int sendEvent(int evType, Object param) {
      return ICmdListener.PRO_STATE_0;
   }

   public void show(ICommandable icon, CmdInstance ci) {
      this.icon = icon;
      this.ci = ci;
      //  Controller.getMe().loadDrawableCmds(d);
      // Controller.getMe().newFocusKey(ic, d);
      sd.shShowDrawable((InputConfig) ci.getFeedback(), ITechCanvasDrawable.SHOW_TYPE_1_OVER);
   }

   
   //#mdebug
   public IDLog toDLog() {
      return toStringGetUCtx().toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "InputRequestStr");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "InputRequestStr");
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return gc.getUCtx();
   }

   //#enddebug
   



}
