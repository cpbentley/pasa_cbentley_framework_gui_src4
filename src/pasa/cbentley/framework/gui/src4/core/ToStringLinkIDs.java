package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.core.src4.logging.IStringableInt;
import pasa.cbentley.framework.gui.src4.ctx.ToStringStaticGui;

public class ToStringLinkIDs implements IStringableInt {

   public String toString(int value) {
      return ToStringStaticGui.debugStateStyle(value);
   }

}
