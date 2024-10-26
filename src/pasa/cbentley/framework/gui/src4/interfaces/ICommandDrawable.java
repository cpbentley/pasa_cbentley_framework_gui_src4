package pasa.cbentley.framework.gui.src4.interfaces;

import pasa.cbentley.framework.cmd.src4.cmd.MCmdAbstract;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdExecutor;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceGui;

/**
 * A {@link IDrawable} that is commandable.
 * <br>
 * 
 * Register
 * @author Charles Bentley
 *
 */
public interface ICommandDrawable extends ICmdExecutor {

   /**
    * {@link ICmdExecutor#commandAction(mordan.controller.CmdInstance)}
    * sends commands from the default
    * <br>
    * This method provides for all commands actions.
    * The state of the {@link CmdInstanceGui} tells us what should be done.
    * 
    * Unlike {@link MCmdAbstract} where state is templated to methods.
    * 
    * @param cd
    */
   public void commandAction(CmdInstanceGui cd);

}
