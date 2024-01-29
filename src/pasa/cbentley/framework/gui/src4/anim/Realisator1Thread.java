package pasa.cbentley.framework.gui.src4.anim;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.interfaces.ITimeCtrl;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.gui.src4.anim.base.DrawableAnim;
import pasa.cbentley.framework.gui.src4.anim.move.Move;
import pasa.cbentley.framework.gui.src4.canvas.CanvasResultDrawable;
import pasa.cbentley.framework.gui.src4.canvas.CanvasAppliInputGui;
import pasa.cbentley.framework.gui.src4.canvas.RepaintCtrlGui;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IAnimable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechAnimable;

/**
 * Control the animation repaint flow in one animation thread.
 * <br>
 * <br>
 * <b>Initialization</b>:<br>
 * The Anim Thread starts and wait for an animation to come in the Queue
 * <br>
 * <br>
 * <b>Work</b> : <br>
 * <li>An animation is added. 
 * <li>The anim thread is woken up
 * <li>The anim thread calls nextTurn. Clock is schedule to sleep anim frame sleep time
 * <li>Anim thread wait.
 * <br>
 * <br>
 * The clock thread wakes up from sleep and call the {@link Object#notify()}  method. 
 * <br>It goes to sleep straight back with the alreay computed next sleep time.
 * starts and wait
 * <br>
 * <br>
 * @author Charles-Philip Bentley
 */
public class Realisator1Thread extends Realisator implements ITechAnimable {

   public static final int      MAX_NUM               = 10;

   public static int            UNIT_PERIOD           = 10;

   private IAnimable[]          animationsArray       = new IAnimable[MAX_NUM];

   private IAnimable[]          animationsNeedRepaint = new IAnimable[MAX_NUM];

   private Thread               animThread;

   /**
    * The last index at which there is an {@link IAnimable} in the animation array.
    */
   private int                  lastUsedAnimIndex     = -1;

   /**
    * the number of active animations.
    */
   private int                  numAnimations         = 0;

   /**
    * May contain special {@link IDrawable} to repaint? This will be the case of {@link DrawableAnim} where
    * animation simply modifies a {@link ByteObject} of the {@link IDrawable}, or one of its properties like
    * {@link Move}.
    * <br>
    * <br>
    * But most of the time,
    * <br>
    * 
    */
   private CanvasResultDrawable screenResultAnimation = null;

   private int                  sleep;

   /**
    * time left until the next frame.
    */
   private int[]                timeCounter           = new int[MAX_NUM];

   private long                 timeTurns;

   protected final CanvasAppliInputGui   canvas;

   public Realisator1Thread(GuiCtx gc, CanvasAppliInputGui canvas) {
      super(gc);
      this.canvas = canvas;
      //asks the Canvas to create an animation screen result object
      screenResultAnimation = canvas.createScreenResultAnimation();
   }

   /**
    * Called by another thread that puts {@link IAnimable} in the queue.
    * <br>
    * 
    * @param anim
    */
   private void addToArray(IAnimable anim) {
      int animIndex = getNextSlot();
      animationsArray[animIndex] = anim;
      numAnimations++;
   }

   /**
    * Find a free index in animation array.
    * @return
    */
   private int getNextSlot() {
      for (int i = 0; i < animationsArray.length; i++) {
         if (animationsArray[i] == null) {
            if (lastUsedAnimIndex < i)
               lastUsedAnimIndex = i;
            return i;
         }
      }
      //we must increase capacity
      return 0;
   }

   public int getSize() {
      int count = 0;
      for (int i = 0; i < lastUsedAnimIndex; i++) {
         if (animationsArray[i] != null) {
            count++;
         }
      }
      return count;
   }

   public long getTimeTurns() {
      return timeTurns;
   }

   public void initiateFreeUp() {
      animThread = null;
   }

   public boolean isPlaying(IAnimable anim) {
      for (int i = 0; i < animationsArray.length; i++) {
         if (anim == animationsArray[i]) {
            return true;
         }
      }
      return false;
   }

   /**
    * Look up for new animations, start them all together
    */
   public synchronized void notifyNew() {
      //wake up animation thread if it is waiting
      //#debug
      toDLog().pAnim("Notified for a New Animation", this, Realisator1Thread.class, "notifyNew", LVL_05_FINE, true);

      this.notifyAll();
   }

   public void pause(IAnimable anim) {
      //first checks if animation is currently runnng
      anim.setAnimFlag(ANIM_15_PAUSED, true);
      //      if (anim.hasAnimFlag(ANIM_15_PAUSED)) {
      //         anim.setAnimFlag(ANIM_15_PAUSED, false);
      //      } else {
      //         anim.setAnimFlag(ANIM_15_PAUSED, true);
      //      }
   }

   /**
    * Entry point of main event thread.
    * <br>
    * Wakes up the animation thread
    * <br>
    * The animation.
    * <br>
    * {@link IAnimable} may define an initial wait time.
    * <br>
    * <br>
    * @param anim initialized to start frame ready to be drawn
    */
   public synchronized void playASAP(IAnimable anim) {
      anim.lifeStartUIThread();
      if (anim.hasAnimFlag(ANIM_15_PAUSED)) {
         anim.setAnimFlag(ANIM_15_PAUSED, false);
      } else {
         if (anim.hasAnimFlag(ANIM_09_STATE_LOADED)) {
            //this should not happen
            //#debug
            toDLog().pAnim("Warning Playing an Animation already loaded " + anim.toString(), this, Realisator1Thread.class, "notifyNew", LVL_05_FINE, true);

         } else {
            addToArray(anim);
            //start the animation 
            anim.setAnimFlag(ANIM_09_STATE_LOADED, true);
            //wake up the thread
         }
      }
      notifyNew();
   }

   public void race() {
      for (int i = 0; i < animationsArray.length; i++) {
         if (animationsArray[i] != null) {
            animationsArray[i].race();
         }
      }
   }

   private void removeAnimation(int i) {
      if (animationsArray[i] != null) {
         animationsArray[i].setAnimFlag(ANIM_09_STATE_LOADED, false);
         animationsArray[i] = null;
         numAnimations--;
      }
   }

   public void removeNowAll() {
      numAnimations = 0;
      lastUsedAnimIndex = -1;
      for (int i = 0; i < animationsArray.length; i++) {
         animationsArray[i] = null;
      }
   }

   /**
    * Runs in the Animation Thread. <br>
    * Does nothing until woken up by {@link Realisator1Thread#playASAP(IAnimable)}.
    * <br>
    * <li>Iterates of each animations whose timer is up,
    * <li> each of which compute their next frame.
    * <li>Repaint the screen.
    * <li>wait
    * 
    * <br>
    * Each animation has it own policy toward lag.
    * <li>Play as fast as possible. Nothing to do. Play as it is.
    * <li>Skip frames. Running time is decided by {@link IAnimable#nextTurn()} timing. If for some reason, timing
    * cannot be honoured, engine skips frame
    */
   public void run() {
      try {
         final ITimeCtrl time = gc.getCFC().getTimeCtrl();
         final RepaintCtrlGui rcd = canvas.getRepaintCtrlDraw();
         //all this is in the animation thread
         while (true) {
            if (numAnimations == 0) {
               synchronized (this) {
                  //#debug
                  toDLog().pAnim("0 Animations. Waiting For a New One", this, Realisator1Thread.class, "notifyNew", LVL_05_FINE, true);

                  this.wait();

                  //#debug
                  toDLog().pAnim(" Woken up by new animation", this, Realisator1Thread.class, "notifyNew", LVL_05_FINE, true);

               }
            }
            //all animations whose timer is up have their next turn called
            //time consuming operations like pixel blending is done in the loop
            long tick1 = time.getTickNano();
            for (int i = 0; i <= lastUsedAnimIndex; i++) {
               runForLoop(i);
            }
            long tick2 = time.getTickNano();
            timeTurns = tick2 - tick1;
            if (screenResultAnimation.isActionDone()) {
               //TODO
               //we clock the painting to compensate the painting time. 
               //should we repaint the screen? yes if animation frames were changed on screen
               //This must be polled on the animations that were modified
               //Partially? depends if the background of the animation is fixed.
               //return animation Boundary for partial repaint stops the animation thread 
               //do a service repaint to force the paint before going to the next frame. 
               //System.out.println("#Realisator Requesting Animation Repaint Waiting");
               rcd.repaintAnimationLock(screenResultAnimation);
               //System.out.println("#Realisator Requesting Animation Repaint Waiting Notified Repaint Done");

               //take duration of animation repaint and add
               screenResultAnimation.resetAll();
            }
            //find the smallest time interval between now and the next animation frame.
            int smallest = DEF_SLEEP;
            for (int i = 0; i <= lastUsedAnimIndex; i++) {
               //iterate over to learn which time to wait
               if (animationsArray[i] != null && timeCounter[i] >= 0 && timeCounter[i] < smallest) {
                  smallest = timeCounter[i];
               }
            }

            //animations whose timerCounter is zero or minus will have their next frame executed.
            synchronized (this) {
               try {
                  if (smallest <= 0) {
                     //debug("#Realisator Not Waiting. He is late!! ", CTRL_FLAG_1_DEBUG);
                  } else {
                     //debug("#Realisator Waiting Smallest " + smallest + " ms", CTRL_FLAG_1_DEBUG);
                     this.wait(smallest);
                  }
                  //System.out.println("#Realisator Waiting Smallest " + smallest + "ms NOTIFIED");
               } catch (Exception e) {
                  e.printStackTrace();
               }
            }
            int effectiveTimeWait = (int) (time.getTickNano() - tick1);
            //remove the amout of time waited. 
            for (int i = 0; i <= lastUsedAnimIndex; i++) {
               if (animationsArray[i] != null && timeCounter[i] > 0) {
                  timeCounter[i] -= effectiveTimeWait;
               }
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   /**
    * 
    * @param i
    */
   protected void runForLoop(int i) {
      if (timeCounter[i] <= 0) {
         //the clock computed it was time for the animation 
         final IAnimable anim = animationsArray[i];
         if (anim != null) {
            if (anim.hasAnimFlag(ANIM_15_PAUSED)) {
               return;
            }
            if (anim.hasAnimFlag(ANIM_11_STATE_RUNNING)) {
               runForLoopNextTurn(i, anim);
            } else {
               anim.lifeStart();
               anim.setAnimFlag(ANIM_11_STATE_RUNNING, true);

               //#debug
               toDLog().pAnim("Animation started with calling Anim.lifeStart() for " + anim.toString1Line(), this, Realisator1Thread.class, "runForLoop", LVL_05_FINE, true);

               runForLoopNextTurn(i, anim);
            }
         }
      }
   }

   /**
    * Frame Skipping:
    * Say an animation is supposed to diplay a total of 6 frames every 50 ms.
    * <br>
    * @param i
    * @param anim
    */
   protected void runForLoopNextTurn(int i, final IAnimable anim) {
      //
      //skip frames when accepted because time since last turn is bigger
      //say animation is supposed to diplay a totla of 6 frames every 50 ms
      //
      //get ready for next turn
      //System.out.println("Next Turn for " + i);
      int r = anim.nextTurn();
      //debug("#Realisator nextTurn returns " + r + " ms", CTRL_FLAG_1_DEBUG);

      if (r == -1) {
         //finish the animation by setting state. and terminating its life.
         anim.setAnimFlag(ANIM_12_STATE_WORKING, false);

         //no more turns. animation has reached the end
         anim.setAnimFlag(ANIM_13_STATE_FINISHED, true);
         anim.setAnimFlag(ANIM_11_STATE_RUNNING, false);

         removeAnimation(i);

         //#debug
         String msg = "Removing Animation #" + i + " " + anim.toString1Line() + " for " + anim.getDrawable().toStringOneLineStates();
         //#debug
         toDLog().pAnim(msg, this, Realisator1Thread.class, "runForLoopNextTurn", LVL_05_FINE, true);

         //this notification will remove IDrawable animation states. this next repaint will be normal
         anim.lifeEnd();
         //last frame drawn was the last

         //one final repaint after the animation is finished ? but it won't work cuz the anim finished was called.
         //still a repaint maybe needed to bring back drawable to normal screen state
         //#debug
         toDLog().pAnim("Last Finishing Frame " + anim.toString1Line(), this, Realisator1Thread.class, "runForLoopNextTurn", LVL_05_FINE, true);

         if (anim.hasAnimFlag(ANIM_07_PAINT_FINISHING_FRAME)) {
            animationsNeedRepaint[i] = anim;
            screenResultAnimation.setActionDoneRepaint(anim.getDrawable());
            //#debug
            screenResultAnimation.debugSetActionDoneRepaint(anim.toString1Line());
         }
      } else {
         //make sure it is there
         animationsNeedRepaint[i] = anim;
         timeCounter[i] = r;
         //register animable drawable to be repainted
         screenResultAnimation.setActionDoneRepaint(anim.getDrawable());
         //#debug
         screenResultAnimation.debugSetActionDoneRepaint(anim.toString1Line());
      }
   }

   public void speedUp() {
      sleep = sleep / 2;
   }

   public void start() {
      //animation thread
      animThread = new Thread(this, "animThread");
      animThread.start();
   }

   public void stop(IAnimable anim) {
      for (int i = 0; i < animationsArray.length; i++) {
         if (animationsArray[i] == anim) {
            anim.lifeEnd();
            removeAnimation(i);
         }
      }
   }

   private void doUpdateLastSlow() {
      for (int i = animationsArray.length - 1; i >= 0; i--) {
         if (animationsArray[i] != null) {
            lastUsedAnimIndex = i;
            return;
         }
      }
      lastUsedAnimIndex = -1;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "Realisator1Thread");
      toStringPrivate(dc);
      dc.append("numAnimations=" + numAnimations);
      dc.append(" lastUsedSlow=" + lastUsedAnimIndex);
      dc.nl();
      for (int i = 0; i <= lastUsedAnimIndex; i++) {
         if (animationsArray[i] == null) {
            dc.append("#" + i + " null");
         } else {
            dc.append("#" + i + " " + animationsArray[i].toString());
         }
      }
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "Realisator1Thread");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

   //   public void runC() {
   //	 while (true) {
   //	    int next = nextEmpty;
   //	    for (int i = 0; i < next; i++) {
   //		  if (toWork[i]) {
   //			toWork[i] = false;
   //			IAnimable anim = v[i];
   //			if (anim != null) {
   //			   int r = anim.nextTurn();
   //			   if (r == -1)
   //				 v[i] = null;
   //			   else {
   //				 times[i] = r;
   //			   }
   //			}
   //		  }
   //	    }
   //	    //should we repaint the screen? yes if animation frames were changed on screen
   //	    //This must be polled on the animations that were modified
   //	    //Partially? depends if the background of the animation is fixed.
   //	    //return animation Boundary for partial repaint
   //	    MasterCanvas.getMasterCanvas().animationRepaint();
   //	    synchronized (clock) {
   //		  try {
   //
   //			//wait for the clock to wake up the animation thread
   //			//This will occur when clock has finished sleeping or when a new animation is added
   //			clock.wait();
   //		  } catch (InterruptedException e) {
   //			e.printStackTrace();
   //		  }
   //	    }
   //	 }
   //  }

}
