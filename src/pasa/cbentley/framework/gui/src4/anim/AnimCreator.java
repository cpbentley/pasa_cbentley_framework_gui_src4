package pasa.cbentley.framework.gui.src4.anim;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.framework.gui.src4.canvas.CanvasAppliInputGui;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ObjectGC;
import pasa.cbentley.framework.gui.src4.interfaces.IAnimable;

/**
 * Singleton Class hiding the realisator.
 * 
 * Declarative programming breaks the link between code and class. Therefore, the code shrinker is enable to know which
 * case statement is not used by the code.
 * <br>
 * <br>
 * 
 * If it is bad OOP, it is necessary to shrink the size of an application that uses only one animation among many.
 * This application will subclass Creator with only 1 case statement for the animation
 * <br>
 * <br>
 * @author Charles-Philip Bentley
 *
 */
public class AnimCreator extends ObjectGC implements IBOAnim {


   private Realisator         realisator;

   protected final CanvasAppliInputGui canvas;

   public AnimCreator(GuiCtx gc, CanvasAppliInputGui canvas) {
      super(gc);
      this.canvas = canvas;
   }

   public void animPause(IAnimable anim) {
      if (realisator != null) {
         realisator.pause(anim);
      }
   }

   /**
    * Sends the animation to the realisator.
    * 
    * @param ani
    */
   public void animPlay(IAnimable anim) {
      checkAndCreateRealisator();
      //System.out.println("=== Animating === ");
      //System.out.println(anim.toString());
      //System.out.println("===  -------  === ");
      realisator.playASAP(anim);
   }

   /**
    * Stops all animation by calling {@link Realisator#removeNowAll()}.
    * 
    */
   public void animStop() {
      if (realisator != null) {
         realisator.removeNowAll();
      }
   }

   /**
    * Stops the animation only if that animatioin is currently running in the {@link Realisator}.
    * <br>
    * <br>
    * Stopping an animation calls the {@link IAnimable#lifeEnd()} and the {@link Realisator} removes it from the running
    * list.
    * <br>
    * <br>
    * 
    * @param ia
    */
   public void animStop(IAnimable anim) {
      checkAndCreateRealisator();
      realisator.stop(anim);
   }

   public void animWakeUp() {
      if (realisator != null) {
         synchronized (realisator) {
            realisator.notifyAll();
         }
      }
   }

   protected void checkAndCreateRealisator() {
      if (realisator == null) {
         realisator = new Realisator1Thread(gc, canvas);
         realisator.start();
      }
   }

   public Realisator getRealisator() {
      checkAndCreateRealisator();
      return realisator;
   }

   public boolean isAnimating(IAnimable anim) {
      if (realisator != null) {
         return realisator.isPlaying(anim);
      }
      return false;
   }

   public void race() {
      if (realisator != null) {
         realisator.race();
      }
   }


   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, AnimCreator.class, "@line5");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {
      
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, AnimCreator.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug
   


}
