package pasa.cbentley.framework.gui.src4.anim.definitions;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.gui.src4.anim.base.DrawableAnim;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawableListener;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;

/**
 * Animation that changes the value of a target {@link ByteObject} using a function.
 * <br>
 * <br.
 * An {@link IDrawable} is given most of the time. When no {@link IDrawable} is given animation is not able to know where
 * are the {@link IDrawable} using the target {@link ByteObject}. Therefore a full repaint is done.
 * 
 * When the Animation Page has finished
 * 
 * <br>
 * The Target may be obtained from the {@link IDrawable}
 * <li> {@link IDrawable#getStyle()}
 * <br>
 * <br>
 * 
 * Action : <br>
 * <li>start {@link IAction#ACTION_TIME_4_ANIM_START}
 * <li>end {@link IAction#ACTION_TIME_4_ANIM_START}
 * <li>reset {@link IAction#ACTION_TIME_3_RESET}
 * <li>step {@link IAction#ACTION_TIME_2_STEP}
 * <br>
 * <br>
 * <br>
 * <br>
 * <br>
 * <br>
 * <br>
 * 
 * @author Charles-Philip Bentley
 *
 */
public class ListenerAnim extends DrawableAnim {

   private IDrawableListener l;

   /**
    * 
    * @param d
    * @param animDef
    */
   public ListenerAnim(GuiCtx gc, IDrawable d, ByteObject animDef, IDrawableListener l) {
      super(gc, d, animDef);
      this.l = l;
   }

   public void lifeEnd() {
      super.lifeEnd();
      l.notifyEvent(d, ITechDrawable.EVENT_05_ANIM_FINISHED, this);
   }

   /**
    * We may have to initialized the Animation function.
    * <br>
    * Some gradient function take into input the drawable color and/or dimensions.
    * <br>
    * <br>
    * 
    */
   public void lifeStart() {
      super.lifeStart();
      l.notifyEvent(d, ITechDrawable.EVENT_06_ANIM_STARTED, this);
   }

   /**
    * why {@link Function#fx(int)} ?
    * That depends on the pointer characteristics.
    * <br>
    * Here, this class deals with one value.
    * <li>
    * 
    */
   public void nextTurnSub() {
      l.notifyEvent(d, ITechDrawable.EVENT_17_ANIM_NEXT_TURN, this);
   }

   public void reset() {
      super.reset();
   }

   //#mdebug

   public void toString(Dctx dc) {
      dc.root(this, "ListenerAnim");
      super.toString(dc.sup());
      dc.nlLvl(l, "Listener");
   }

   
   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "ListenerAnim");
   }

   //#enddebug

}
