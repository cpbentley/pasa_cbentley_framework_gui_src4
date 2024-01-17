package pasa.cbentley.framework.gui.src4.anim;

import pasa.cbentley.byteobjects.src4.core.BOAbstractOperator;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ToStringStaticGui;

public class AnimOperator extends BOAbstractOperator implements IBOAnim {

   protected final GuiCtx gc;

   public AnimOperator(GuiCtx gc) {
      super(gc.getBOC());
      this.gc = gc;
   }
   
   public  void toStringAnim(ByteObject bo, Dctx sb) {
      sb.append("#Anim");
      sb.append(ToStringStaticGui.debugAnimType(bo.get1(ANIM_OFFSET_01_TYPE1)));
   }
   public  void toStringAnim1Line(ByteObject bo, Dctx sb) {
      sb.append("#Anim");
      sb.append(ToStringStaticGui.debugAnimType(bo.get1(ANIM_OFFSET_01_TYPE1)));
   }
}
