package pasa.cbentley.framework.gui.src4.string;

import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.engine.CmdNode;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdListener;
import pasa.cbentley.framework.cmd.src4.interfaces.ICommandable;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.core.ViewDrawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ObjectGC;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.forms.ListRoot;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IValidable;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;

/**
 * Used to show a dialog asking the user for a string. This is always part of a {@link CmdInstance} that is not yet
 * executed. The {@link RequestStringInput} job is to get the string parameter for the command.
 * 
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
 * The {@link RequestStringInput} creates a command with the String in parameter
 * This is the mechanism used for getting the feedback to the object requesting
 * <br>
 * <br>
 * 
 * @author Charles-Philip
 *
 */
public class RequestStringInput extends ObjectGC implements ICommandable {

   protected CmdInstance  ci;

   protected CmdNode      cmdNodeEditStr;

   protected ICommandable icon;

   private StringDrawable sd;

   protected IValidable   validator;

   public RequestStringInput(GuiCtx gc, StyleClass scStr, String title) {
      super(gc);

      cmdNodeEditStr = cc.createCmdNode(null, "EditStr");
      cmdNodeEditStr.addMenuCmd(cc.CMD_04_OK);
      cmdNodeEditStr.addMenuCmd(cc.CMD_05_CANCEL);

      cmdNodeEditStr.setListener(this);

      sd = new StringDrawable(gc, scStr, "");

      sd.setCmdNote(cmdNodeEditStr);

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
      if (cmd.getCmdID() == cc.CMD_04_OK || cmd.getCmdID() == cc.CMD_04_OK) {
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
      return cmdNodeEditStr;
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
      ExecutionContextCanvasGui ec = (ExecutionContextCanvasGui) ci.getExecutionContext();
      sd.shShowDrawable(ec, ITechCanvasDrawable.SHOW_TYPE_1_OVER_TOP);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, RequestStringInput.class, 130);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, RequestStringInput.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

}
