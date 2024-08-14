package pasa.cbentley.framework.gui.src4.interfaces;

import pasa.cbentley.framework.cmd.src4.interfaces.ICommandable;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceGui;

/**
 * A {@link IDrawable} that is commandable.
 * <br>
 * 
 * Register
 * @author Charles Bentley
 *
 */
public interface ICommandDrawable extends ICommandable {

   /**
    * {@link ICommandable#commandAction(mordan.controller.CmdInstance)}
    * sends commands from the default
    * <br>
    * This method provides for all commands
    * @param cd
    */
   public void commandAction(CmdInstanceGui cd);

}
