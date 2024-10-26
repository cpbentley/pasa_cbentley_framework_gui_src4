package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdListener;
import pasa.cbentley.framework.core.ui.src4.user.IBOUserInterAction;
import pasa.cbentley.framework.core.ui.src4.user.UserInteraction;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ObjectGC;

public class UserInteractionGuiControl extends ObjectGC implements ICmdListener {

   public UserInteractionGuiControl(GuiCtx gc) {
      super(gc);
      gc.getCC().addCmdListener(this);
   }

   public void cmdExecutedWas(CmdInstance ci) {
      //user interaction is used to help user modify things he doesn't like about
      //what just happened.. flag user interaction as slow for example.
      //it is a feedback tool
      //if something unexpected happens the user want to know exactly which commands were executed
      //TODO do we need this stuff then?
      //can be switched off

      //when we have a device event
      //inside this user interaction, hierarchy of user interaction
      UserInteraction ui = new UserInteraction(cc.getCUC(), IBOUserInterAction.TYPE_0_COMMAND);
      gc.getUIACtrl().log(ui);

   }

   public void cmdExecutedWill(CmdInstance ci) {
      // TODO Auto-generated method stub
      
   }

}
