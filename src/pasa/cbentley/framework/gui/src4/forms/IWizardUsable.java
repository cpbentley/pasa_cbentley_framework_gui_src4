package pasa.cbentley.framework.gui.src4.forms;

import pasa.cbentley.framework.gui.src4.core.Drawable;

public interface IWizardUsable {

   /**
    * 
    * @param i
    * @return null if current Screen i is valid, else return error message in String
    */
   public String validate(int i);

   /**
    * 
    * @param i
    * @return
    */
   public Drawable get(int i);

   /**
    * 
    */
   public void end();

   /**
    * 
    * @return
    */
   public int getSize();

   /**
    * 
    * @return
    */
   public int[] getScreens();
}
