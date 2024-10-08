package pasa.cbentley.framework.gui.src4.anim;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.utils.BitUtils;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ObjectGC;
import pasa.cbentley.framework.gui.src4.interfaces.IAnimable;

/**
 * 
 * @author Charles-Philip Bentley
 *
 *(1)
 *Init:
 *The Anim Thread starts and wait for an animation to come
 *
 *(2)
 *An animation is added. The anim thread is woken up
 *The anim thread calls nextTurn. Clock is schedule to sleep anim frame sleep time
 *Anim thread wait
 *
 *The clock thread wakes up from sleep and call the notify method. it goes to sleep straight back with the alreayd
 *computed next sleep time.
 *starts and wait
 */
public abstract class Realisator extends ObjectGC implements Runnable, IStringable {

   public static final int CTRL_FLAG_1_DEBUG = 1;

   public static final int DEF_SLEEP         = 1000;

   private int             ctrlFlags;


   public Realisator(GuiCtx gc) {
      super(gc);
   }

   /**
    * Number of playing animations
    * <br>
    * <br>
    * @return
    */
   public int getSize() {
      return 0;
   }

   public long getTimeTurns() {
      return 0;
   }

   /**
    * @param flag
    * @return
    */
   public boolean hasCtrlFlag(int flag) {
      return BitUtils.hasFlag(ctrlFlags, flag);
   }

   public void initiateFreeUp() {
      // TODO Auto-generated method stub

   }

   public abstract boolean isPlaying(IAnimable anim);

   public void pause(IAnimable anim) {

   }

   /**
    * 
    */
   public void pauseAll() {
      // TODO Auto-generated method stub

   }

   /**
    * Start playing this animation as soon as possible
    * Method returns
    * @param anim
    */
   public abstract void playASAP(IAnimable anim);

   public void race() {

   }

   public void removeNowAll() {

   }

   /**
    * 
    * @param flag
    * @param v
    */
   public void setCtrlFlag(int flag, boolean v) {
      ctrlFlags = BitUtils.setFlag(ctrlFlags, flag, v);
   }

   public void start() {

   }

   public void stop(IAnimable anim) {

   }


   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, Realisator.class, 118);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, Realisator.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {
      
   }
   //#enddebug
   


}