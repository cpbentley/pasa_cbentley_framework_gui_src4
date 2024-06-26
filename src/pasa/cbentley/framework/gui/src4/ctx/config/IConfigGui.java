package pasa.cbentley.framework.gui.src4.ctx.config;

import pasa.cbentley.byteobjects.src4.ctx.IConfigBO;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IToStringFlagsGui;

/**
 * Configuration for {@link GuiCtx} module.
 * 
 * @author Charles Bentley
 *
 */
public interface IConfigGui extends IConfigBO {

   
   /**
    * {@link IToStringFlagsGui}
    * 
    * @return
    */
   public int toStringGetFlagsGui();
}
