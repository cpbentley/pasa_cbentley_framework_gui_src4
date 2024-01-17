package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.framework.coreui.src4.tech.ITechInputFeedback;
import pasa.cbentley.framework.gui.src4.anim.Realisator;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.input.src4.CanvasAppliInput;
import pasa.cbentley.framework.input.src4.RepaintCtrl;
import pasa.cbentley.framework.input.src4.CanvasResult;
import pasa.cbentley.framework.input.src4.ctx.IFlagsToStringInput;
import pasa.cbentley.framework.input.src4.interfaces.ITechInput;

public class RepaintCtrlDrawable extends RepaintCtrl {

   CanvasAppliDrawable                   canvasD;

   /**
    * Base {@link ScreenResult}.
    * <br>
    * When active, Controller creates a new one.
    */
   private CanvasResultDrawable screenResultEvent;

   protected final GuiCtx gc;

   public RepaintCtrlDrawable(GuiCtx gc, CanvasAppliInput canvas) {
      super(gc.getIC(), canvas);
      this.gc = gc;
      canvasD = (CanvasAppliDrawable) canvas;
      if(this.getClass() == RepaintCtrlDrawable.class) {
         constructHelpers();
      }
   }

   public CanvasResult create(int id) {
      return new CanvasResultDrawable(gc,canvas, id);
   }
   
   public void constructHelpers() {
      super.constructHelpers();
   }

   /**
    * Appends the {@link CanvasResultDrawable} with other pending results in the queue and
    * blocks until that results has been rendered.
    * <br>
    * <b>Active Rendering Mode</b>: Wake up and accelerates Rendering. Returns.
    * <br>
    * <br>
    * <b>Passive Rendering Mode</b>: Method called by {@link Realisator} in the animation thread for repainting the animations.
    * <br>
    * The method blocks until 
    * <br>
    * 
    * No other threads should call this method.
    * <br>
    * If {@link ScreenResult} is merged with another full repaint, {@link Realisator} still me be called.
    * <br>
    * <br>
    * <b>Use Case : Animated object flying over the screen:</b>
    * <br>
    * <li> Several animations are running and will send many frames all over the place over 10-15 seconds without user interaction
    * except for canceling the animation.
    * <li> A strategy is to take a screenshot of the background layers and draw the animation layers over the background.
    * <li> This work is coordinated with the {@link TopologyDLayer}. It will cache the layers. knows that a plane near the camera is has the highest ZIndex and is drawn the last.
    * <li>Some moving animations will simply request a full repaint for simplicity and ask {@link MasterCanvas} to
    * cache static DLayers.
    * <br>
    * <br>
    * Calling Thread <b>must not</b> hold any lock used by the Painting thread.
    * <br>
    * <br>
    * @param screenResultAnimation {@link ScreenResult} specyfying which {@link Drawable} have to be repainted
    * @throws InterruptedException 
    */
   public void repaintAnimationLock(CanvasResultDrawable screenResultAnimation) throws InterruptedException {

      screenResultAnimation.setRepaintFlag(ITechCanvasDrawable.REPAINT_04_ANIMATION, true);

      //we want the method to lock
      screenResultAnimation.setFlag(ITechInputFeedback.FLAG_15_SCREEN_LOCK, true);

      //call for a repaint. in passive mode sends an event what if repaint call goes faster?
      //add the request to the queue

      queueRepaintBlock(screenResultAnimation);

      //      canvas.repaint(screenResultAnimation);
      //      //Forces the screen result to be repainted before continuing on.
      //      try {
      //      canvas.addPainterWait();
      //      } catch(InterruptedException e) {
      //         //do nothing
      //      }
      //      //wait for a repaint uses a lock notified when painting method finishes
      //      //acquire lock monitor
      //      synchronized (lock) {
      //         try {
      //            //this lock must be checked and notified every
      //            if (isWaitAnimNeeded) {
      //               //don't wait twice
      //               if (!isLockingAnimation) {
      //                  //System.out.println("#Controller repaintAnimation Wait!");
      //                  isLockingAnimation = true;
      //                  animLock = lock;
      //                  //wait method synchro claims on lock object. 
      //                  animLock.wait();
      //                  isLockingAnimation = false;
      //               }
      //            }
      //            isWaitAnimNeeded = false;
      //         } catch (Exception e) {
      //            e.printStackTrace();
      //         }
      //      }
   }

   /**
    * Appends the {@link CanvasResultDrawable} with other pending results in the queue and return.
    * <br>
    * Method called inside an Animation Thread that allows frame slipping.
    * <br>
    * When rendering thread is too slow, the Animation Thread goes several turn for each
    * Render Turn. Some Animations Frames will be skipped.
    */
   public void repaintAnimationFast(CanvasResultDrawable screenResultAnimation) {
      //call for a repaint. in passive mode sends an event what if repaint call goes faster?
      repaint(screenResultAnimation);

   }

   /**
    * The {@link Runnable} is the code that updates the render state.
    * <br>
    * It will be run in the {@link ITechInput#THREAD_1_UPDATE} that writes to 
    * the render state.
    * <br>
    * It is added to the queue
    * <br>
    * This method is thread safe
    * @param d
    * @param run in sync with the Update Thread.
    */
   public synchronized void repaintDrawableCycleBusiness(IDrawable d, Runnable run) {

   }

   /**
    * Called from a thread not know what it is and requesting a repaint of the drawable.
    * <br>
    * If current thread is the event thread (passive mode), updates
    * ScreenResult.
    * 
    * Called from another thread than the UserEvent thread. Cycle type is {@link Controller#CYCLE_1_BUSINESS_EVENT}
    * <br>
    * <br>
    * i.e. the caret blinking thread.
    * <br>
    * Uses flags to know that business has called a repaint.
    * <br>
    * <br>
    * Uses a free {@link ScreenResult} of type {@link Controller#CYCLE_1_BUSINESS_EVENT}.
    * <br>
    * <br>
    * 
    * @param d when null, it will be a full repaint
    * 
    * @throws IllegalStateException this thread requires that the caller knows the
    * the calling thread is NOT the rendering thread of the current mode.
    */
   public void repaintDrawableCycleBusiness(IDrawable d) {
      //check current thread
      if (Thread.currentThread() == eventThread) {
         //we are in the GUI Event Thread
         screenResultEvent.setActionDoneRepaint(d);
         return;
      } else {
         CanvasResultDrawable businessSR = getWorkSD();
         //we are not in the GUI Event Thread. update the 
         businessSR.setActionDoneRepaint(d);

         //force repaints
         if (canvasD.hasDebugFlag(IFlagsToStringInput.Debug_8_ForceFullRepaints)) {
            businessSR.setActionDoneRepaint();
         }
         businessSR.isRepaint();
         //sends the result to be painted. before that create a new
         super.rotateBusinessResult();
         repaint(businessSR);
         //TODO when do we have to call this exactly?
         businessSR.resetAll();
      }

   }

   public void repaintDrawableCycleBusiness() {
      repaintDrawableCycleBusiness(null);
   }

   public CanvasResultDrawable getWorkSD() {
      return (CanvasResultDrawable) canvasResultBusiness;
   }

   public CanvasResultDrawable getSD() {
      return (CanvasResultDrawable) getScreenResult();
   }
}
