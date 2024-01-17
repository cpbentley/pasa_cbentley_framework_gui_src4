package pasa.cbentley.framework.gui.src4.utils;

import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;

/**
 * Statu Data will be displayed by different status views. Its a container
 * <br>
 * Each {@link GuiContext} 
 * @author Charles Bentley
 *
 */
public class StatusData {

   private GuiCtx gc;

   String             str;

   int                type;

   public StatusData(GuiCtx gc) {
      this.gc = gc;
   }

   public void setStatusStr(String str) {
      this.str = str;
   }

   /**
    * Sets the def status str
    * @param str
    * @param type ctype {@link StyleClass} or color
    */
   public void setStatusStr(String str, int type) {
      this.str = str;
      this.type = type;
   }

   /**
    * 
    * @param id
    * @param str
    * @param type
    */
   public void setStatusStr(int id, String str, int type) {
      this.str = str;
      this.type = type;
   }
}
