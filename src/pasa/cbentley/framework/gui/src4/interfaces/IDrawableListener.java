package pasa.cbentley.framework.gui.src4.interfaces;

import pasa.cbentley.core.src4.logging.IStringable;

/**
 * 
 * @author Charles Bentley
 *
 */
public interface IDrawableListener extends IStringable {

   /**
    * Generic Event Notificator
    * <br>
    * <li> {@link ITechDrawable#EVENT_10_STYLE_STATE_CHANGE}
    * <li> {@link ITechDrawable#EVENT_11_REFRESH_CLEAN}
    * <li> {@link ITechDrawable#EVENT_12_SIZE_CHANGED}
    * <li> {@link ITechDrawable#EVENT_13_KEY_EVENT}
    * <li> {@link ITechDrawable#EVENT_14_POINTER_EVENT}
    * <li> {@link ITechDrawable#EVENT_15_INIT_SIZE_W}
    * <li> {@link ITechDrawable#EVENT_16_INIT_SIZE_H}
    * <li> {@link ITechDrawable#EVENT_17_ANIM_NEXT_TURN}
    * 
    * @param d
    * @param event
    * @param o
    */
   public void notifyEvent(IDrawable d, int event, Object o);
}
