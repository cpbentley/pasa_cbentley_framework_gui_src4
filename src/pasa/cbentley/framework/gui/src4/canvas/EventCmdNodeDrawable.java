package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.framework.cmd.src4.input.EventCmdNodeFocusDeviceChange;
import pasa.cbentley.framework.cmd.src4.input.FocusDeviceCommander;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

/**
 * Change of Focus from one Drawable to another
 * @author Charles Bentley
 *
 */
public class EventCmdNodeDrawable extends EventCmdNodeFocusDeviceChange {

   private IDrawable newDrw;

   private IDrawable oldDrw;

   public EventCmdNodeDrawable(GuiCtx gc, IDrawable newD, IDrawable oldD, FocusDeviceCommander focus) {
      super(gc.getCC(), newD != null ? newD.getCmdNode() : null, oldD != null ? oldD.getCmdNode() : null, focus);
      this.newDrw = newD;
      this.oldDrw = oldD;
   }

}
