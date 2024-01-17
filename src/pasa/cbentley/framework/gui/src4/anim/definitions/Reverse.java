package pasa.cbentley.framework.gui.src4.anim.definitions;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.framework.gui.src4.anim.IBOAnim;
import pasa.cbentley.framework.gui.src4.anim.ITechAnim;
import pasa.cbentley.framework.gui.src4.anim.base.DrawableAnim;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

public class Reverse extends DrawableAnim {

   public Reverse(GuiCtx gc, IDrawable d, ByteObject def) {
      super(gc, d, def);
      //check if 
      int time = definition.get1(IBOAnim.ANIM_OFFSET_05_TIME1);
      if (time == ITechAnim.ANIM_TIME_1_ENTRY) {
         //fetech exit if any and reverse it.
      } else if (time == ITechAnim.ANIM_TIME_2_EXIT) {

      }
   }

}
