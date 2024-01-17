package pasa.cbentley.framework.gui.src4.forms;

import pasa.cbentley.framework.cmd.src4.engine.CmdNode;

/**
 * Constuctor of View has a link to the Model Browsee
 * interface for forms that display results of a Browsed Item
 * This enable the form to execute NEXT and PREVIOUS functions directly
 * without going back to the Browser screen
 * add commands with the CommandListener
 * add the commandlistener at the end for cmd processing
 * @author cbentley
 *
 */
public interface IBrowseeView {

   /**
    * remove the browsing commands and set the CommandListener to null (making
    * the browser eligible for gc)
    *
    */
   public void browserClosed();

   /**
    * Returns the 
    * @return
    */
   public Browsee getBrowsee();

   /**
    * 
    * @param rid
    */
   public void displayBrowsee(int rid);

   /**
    * The {@link CmdNode} of the View on which to set the NEXT and PREVIOUS commands
    * @return
    */
   public CmdNode getCmdNode();
}
