package pasa.cbentley.framework.gui.src4.anim.base;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.objects.function.Function;
import pasa.cbentley.byteobjects.src4.objects.function.FunctionFactory;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.utils.BitUtils;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.anim.AnimCreator;
import pasa.cbentley.framework.gui.src4.anim.IBOAnim;
import pasa.cbentley.framework.gui.src4.anim.ITechAnim;
import pasa.cbentley.framework.gui.src4.anim.Realisator;
import pasa.cbentley.framework.gui.src4.anim.definitions.AlphaChangeRgb;
import pasa.cbentley.framework.gui.src4.anim.definitions.ByteObjectAnim;
import pasa.cbentley.framework.gui.src4.anim.move.Move;
import pasa.cbentley.framework.gui.src4.anim.move.FunctionMove;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.ctx.ToStringStaticGui;
import pasa.cbentley.framework.gui.src4.interfaces.IAnimable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;

/**
 * Animates an {@link IDrawable}. Animation turns are controlled by a {@link Function} or by the paint method that
 * <br>
 * <br>
 *  <b>Examples of {@link Function}</b> <br>
 * <li>time f(x). 
 * <li>For the {@link ByteObjectMod}, the function controls everything.
 * <li>For the {@link Move} animation, {@link FunctionMove} decides how to move the object.
 * <br>
 * <br>
 * The basic scheme follows this pattern
 * <p>
 * {@link IAnimable#lifeStart()} <br>
 * 
 * until {@link IAnimable#isFinished()} <br>
 * doAnimationStep [function,sleep] <br>	
 * 
 * {@link IAnimable#lifeEnd()} <br>
 * </p>
 * So there might no step at all if isFinished return true after the initialization
 * 
 * A drawable may be directly modified with a {@link ByteObjectMod} or indirectly with an {@link ImgAnimable}. <br>
 * 
 * 
 * {@link AlphaChangeRgb} and {@link Move} are examples of {@link ImgAnimable}. They work on Drawable's pixel data.
 * The move class may be direct if the drawing time of the drawable is fast and area is big. For small Drawable though, the move
 * will always be indirect.
 * <br>
 * <br>
 * <b>Genetics</b><br>
 * An animation may be of type In,Out,Other.
 * Increasing alpha value is In. Pixelating out is out.
 * <br>
 * <br>
 * <b>Screen transitions</b>. <br>
 * Many frameworks uses the concept of screen transitions. In our world, that means the main drawable is
 * replaced by another. A screen transition overrides the entry and exit animation of those drawables
 * So a Transition is 2 Animations working in parallel on the same frequency
 * What happens to the Entry/Exit animation of sub drawables? The may be turned off. It depends if the parent drawable forward 
 * hide event.
 * <br>
 * <br>
 * <b>Animations Genetics</b>:
 * How can you speed up an animation? Decrease sleep time. But that does not help once you hit the 0 wall. Maximum proc speed.
 * After that, animation speed genetics must be tweaked.
 * Most of the time, increase the step function so that fewer animation turns are needed to complete it. 
 * <br>
 * Iterating over alpha value from 0 to 255 may take 255 steps or 3 steps 32, 128, 192 and before the final draw of the image.
 * <br>
 * An animation must thus be able to know how many steps will be needs. Not always possible with randomized functions.
 * <br>
 * <br>
 * <b>Time constrained animations</b>:
 * <br>
 * An animation has a time roof of 500 ms seconds.
 * If first frame runs at 50 ms, maximum of 10 frames. Function must reevalute or frames must be skipped.
 * <br>
 * Sleep may be ajusted as well. 
 * <br>
 * <br>
 * <b>Racing:</b> {@link DrawableAnim#race()}
 * <br>
 * When user activates a key, animation is raced to finish frame, and key is sent to {@link IDrawable} cmd management.
 * Sometimes, the animation is able to control some commands.
 * For example, a {@link Move} of a table is able to handle selection changes, provides cache is updated. 
 * THis animation must be raced to at least a given milestone. Selection area must be visible on the Canvas.
 * Milestone is easier to {@link AlphaChangeRgb}, a value of 192 is OK. Each animation decides where the Drawable keeps
 * its usability.
 * <br>
 * <br>
 * <b>Z Index</b> <br>
 * Controlled by {@link AnimAggregate}. Otherwise last painted is on top.
 * <br>
 * <br>
 * <br>
 * <br>
 * @author Charles-Philip Bentley
 * @see AnimCreator
 * @see ByteObjectAnim
 * @see ImgAnimable
 * @see IAnimable
 *
 */
public abstract class DrawableAnim implements IAnimable {

   private int[]        bounds  = new int[5];

   /**
    * Target Drawable of this animation. May be not be null
    * <br>
    * Animation must use an {@link IDrawable} wrapper.
    */
   protected IDrawable  d;

   /**
    * {@link IBOAnim} definition.
    * Could be null.
    * Type is {@link IBOTypesGui#TYPE_130_ANIMATION}
    */
   protected ByteObject definition;

   /**
    * Drawable to be notified of animations events
    */
   protected IDrawable  notified;

   /**
    * -1 = infinite loop
    */
   protected int        repeats = 0;

   /**
    * Default speed applied to each animation step
    */
   protected int        sleep   = Realisator.DEF_SLEEP;

   /**
    * Each step has its own speed. Sleep Function.
    * Can be null.
    */
   protected Function   sleepFunc;

   protected int        stateFlags;

   protected int        step;

   /**
    * Function of the animation that controls whether the animation has finished.
    * <br>
    * <br>
    * When {@link IAnimable#nextTurn()} is called, it may declare that the animation is now finished.
    * <br>
    * <br>
    * Once last call to {@link IAnimable#paint(GraphicsX)} ?
    * <br>
    * <br>
    * Calling {@link IAnimable#reset()} resets this function.
    * <br>
    * Function can be created during the {@link DrawableAnim#lifeStart()} method
    * <br>
    * Reset counter and isFinished
    * Takes values and apply them
    * DrwParamMod f(pointer), Drawable Move f(x,y)
    * <br>
    * Since most {@link IAnimable} have a function, we have a field here that controls {@link DrawableAnim#nextTurn()}.
    * <br>
    * Those animations just implement {@link DrawableAnim#paint(GraphicsX)}.
    * <br>
    */
   protected Function   stepFunction;

   protected int        target;

   /**
    * Specific Tech objects of the Animation
    */
   protected ByteObject tech;

   /**
    * Starts at 0
    */
   protected int        turn    = 0;

   protected final GuiCtx gc;


   /**
    * 
    * @param d
    * @param techTab
    */
   public DrawableAnim(GuiCtx gc, IDrawable d, ByteObject def) {
      this.gc = gc;
      if (d == null)
         throw new NullPointerException("Null BaseAnimable Drawable");
      if (def == null)
         throw new NullPointerException("Null Animation Defintion");
      this.d = d;
      this.definition = def;
      this.tech = def.getSubFirst(IBOTypesGui.TYPE_199_ANIM_TECH);
      initGlobalDef();
   }

   /**
    * 
    * @param d
    * @param f might be null if subclass handles function and turns itself.
    */
   public DrawableAnim(GuiCtx gc,IDrawable d, Function f) {
      this.gc = gc;
      if (d == null)
         throw new NullPointerException("Null BaseAnimable Drawable");
      this.d = d;
      this.stepFunction = f;
   }

   /**
    * Boundary is mainly used to defined the repaint clip area
    */
   public int[] getBounds() {
      bounds[0] = d.getX();
      bounds[1] = d.getY();
      bounds[2] = d.getDrawnWidth();
      bounds[3] = d.getDrawnHeight();
      return bounds;
   }

   public ByteObject getDefinition() {
      return definition;
   }

   public IDrawable getDrawable() {
      return d;
   }

   /**
    * Get the sleep return value at the end of the step. 
    * <br>
    * <br>
    * Method handles finished condition and repeat feature
    * <br>
    * <br>
    * May call the {@link IAnimable#reset()} when a repeat is required.
    * <br>
    * <br>
    * @return
    */
   public int getReturn() {
      if (isFinished()) {
         if (repeats == 0) {
            return -1;
         } else {
            if (repeats != -1) {
               repeats--;
            }
            reset();
            return sleep;
         }
      } else {
         return sleep;
      }
   }

   public int getTarget() {
      return definition.get1(IBOAnim.ANIM_OFFSET_06_TARGET1);
   }

   public int getTiming() {
      return definition.get1(IBOAnim.ANIM_OFFSET_05_TIME1);
   }

   /**
    * {@link IBOAnim#ANIM_OFFSET_04_DRAW_TYPE1}
    */
   public int getType() {
      return definition.get1(IBOAnim.ANIM_OFFSET_04_DRAW_TYPE1);
   }

   public boolean hasAnimFlag(int flag) {
      return BitUtils.hasFlag(stateFlags, flag);
   }

   /**
    * Partial repaint will need to repaint background if animation has transparent pixels
    * @return
    */
   public boolean hasTrans() {
      if (d != null) {
         //delegate
         return d.hasState(ITechDrawable.STATE_16_TRANSLUCENT);
      }
      return false;
   }

   private void initGlobalDef() {
      //may not be defined here
      ByteObject function = definition.getSubFirst(IBOTypesBOC.TYPE_021_FUNCTION);
      FunctionFactory functionFactory = gc.getBOC().getFunctionFactory();
      stepFunction = functionFactory.createFunction(function);
      //can be null at this stage. function will be created by the subclass
      if (stepFunction == null) {
         //#debug
         //throw new NullPointerException("Null Step function for " + this.toStringOneLine());
      }
      int sleep = 50;
      sleep = definition.get2(IBOAnim.ANIM_OFFSET_08_SLEEP2);
      if (definition.hasFlag(IBOAnim.ANIM_OFFSET_02_FLAG, IBOAnim.ANIM_FLAG_2_SLEEP_CUSTOM)) {
      }
      this.sleep = sleep;

      if (definition.hasFlag(IBOAnim.ANIM_OFFSET_02_FLAG, IBOAnim.ANIM_FLAG_4_SLEEP_FUNCTION)) {
         ByteObject sleF = definition.getSubOrder(IBOTypesBOC.TYPE_021_FUNCTION, 1);
         sleepFunc = functionFactory.createFunction(sleF);
      }
      int rep = 0;
      if (definition.hasFlag(IBOAnim.ANIM_OFFSET_02_FLAG, IBOAnim.ANIM_FLAG_3_LOOP)) {
         rep = -1;
      } else {
         rep = definition.get1(IBOAnim.ANIM_OFFSET_07_REPEAT1);
      }
   }

   /**
    * Simply checks for IAnimable.ANIM_13_STATE_FINISHED
    */
   public boolean isFinished() {
      return hasAnimFlag(IAnimable.ANIM_13_STATE_FINISHED);
   }

   /**
    * Called when the Animation if finished.
    * <br>
    * Send the event {@link ITechDrawable#EVENT_05_ANIM_FINISHED} to registered Drawable.
    */
   public void lifeEnd() {
      d.notifyEvent(ITechDrawable.EVENT_05_ANIM_FINISHED, this);
      if (notified != null) {
         notified.notifyEvent(ITechDrawable.EVENT_05_ANIM_FINISHED, this);
      }
   }

   /**
    * Called in the UI Thread
    */
   public void lifeEndUIThread() {

   }

   /**
    * Sublclass should call this method if it overrides.
    * <br>
    * <br>
    * Sets {@link ITechDrawable#STATE_11_ANIMATING} to true
    * <br>
    * Implementation calls {@link DrawableAnim#setInitialize()} at one time.
    */
   public void lifeStart() {
      d.notifyEvent(ITechDrawable.EVENT_06_ANIM_STARTED, this);
      if (notified != null) {
         notified.notifyEvent(ITechDrawable.EVENT_06_ANIM_STARTED, this);
      }
      loadHeavyResourcesSub();
   }

   /**
    * Must be called by overriding
    */
   public void lifeStartUIThread() {
      d.setStateFlag(ITechDrawable.STATE_11_ANIMATING, true);
   }

   /**
    * Must be called a little prior to when it starts.. so that when it actually starts
    * the data is ready to be used
    * Loads data from disk, or images, 
    */
   public void loadHeavyResources() {
      loadHeavyResourcesSub();
      
      //animator thread might sleeping. notify realisator by waking up for start the animation
      getAnimCreator().animWakeUp();
   }

   protected AnimCreator getAnimCreator() {
      return d.getCanvas().getAnimCreator();
   }

   public void loadHeavyResourcesSub() {

   }

   /**
    * When overriding, code must call {@link DrawableAnim#getReturn()} and must increment turn value.
    * super.nextTurn();
    * <br>
    * <br>
    * When a step {@link Function} is used. the {@link IAnimable#ANIM_13_STATE_FINISHED} flag is set automatically.
    * <br>
    * <br>
    * 
    */
   public int nextTurn() {
      if (sleepFunc != null) {
         sleep = sleepFunc.fx(step);
      }
      turn++;
      if (!hasAnimFlag(ANIM_13_STATE_FINISHED)) {
         if (stepFunction != null) {
            setAnimFlag(ANIM_13_STATE_FINISHED, stepFunction.isFinished());
         }
      }
      if (!hasAnimFlag(ANIM_13_STATE_FINISHED)) {
         nextTurnSub();
      }
      if (!hasAnimFlag(ANIM_13_STATE_FINISHED)) {
         if (stepFunction != null) {
            setAnimFlag(ANIM_13_STATE_FINISHED, stepFunction.isFinished());
         }
      }
      int r = getReturn();
      //SystemLog.printAnim("#DrawableAnim#nextTurn Sleeping=" + r + " ms");
      return r;
   }

   /**
    * Implemented by the subclass if it is has specific turn activities.
    * <br>
    * <br>
    * <b>PRE</b>: turn 1 = means 1st frame
    * <br>
    * <br>
    * <b>POST</b>: If used, Step function counter has increased. At one time, {@link IAnimable#ANIM_13_STATE_FINISHED} must be set to true.
    * <br>
    * <br>
    * 
    */
   public void nextTurnSub() {

   }

   /**
    * Implemented by specific animation. <br>
    * They will draw according to the animation step.
    * <br>
    * Method may compute turn based information, though {@link DrawableAnim#nextTurn()} stays the controlling method.
    * <br>
    * Default behavior simply call {@link IDrawable#draw(GraphicsX)}
    */
   public void paint(GraphicsX g) {
      d.draw(g);
   }

   public void race() {

   }

   /**
    * Register the {@link IDrawable} to be notified of Animation events.
    * <br>
    * <br>
    * Some animations works on their own {@link IDrawable} but are actually animating a drawable.
    * <br>
    * <br>
    * @param d
    */
   public void register(IDrawable d) {
      notified = d;
   }

   /**
    * Reset the function
    */
   public void reset() {
      if (stepFunction != null) {
         stepFunction.resetCounter();
      }
      turn = 0;
      setAnimFlag(ANIM_13_STATE_FINISHED, false);
   }

   /**
    * Do the job for displaying the next frame.
    */
   public void run() {
      long timeTick = System.currentTimeMillis();
      nextTurnSub();
   }

   public void setAnimFlag(int flag, boolean v) {
      stateFlags = BitUtils.setFlag(stateFlags, flag, v);
   }

   public void setEntryMainStates() {
      IAnimable anim = this;
      
      
      //#debug
      String msg = anim.toString1Line() + " for " + d.toString1Line() + " " + Thread.currentThread();
      //#debug
      toDLog().pAnim(msg, this, DrawableAnim.class, "setEntryMainStates", LVL_05_FINE, false);

      d.setStateFlag(ITechDrawable.STATE_11_ANIMATING, true);
      if (anim.hasAnimFlag(IAnimable.ANIM_24_OVERRIDE_DRAW)) {
         d.setStateFlag(ITechDrawable.STATE_20_ANIMATED_FULL_HIDDEN, true);
      }
      if (anim.hasAnimFlag(IAnimable.ANIM_25_OVERRIDE_DRAW_CONTENT)) {
         d.setStateFlag(ITechDrawable.STATE_22_ANIMATED_CONTENT_HIDDEN, true);
      }
      if (anim.getTiming() == ITechAnim.ANIM_TIME_1_ENTRY) {
         //ENTRY
         d.setStateFlag(ITechDrawable.STATE_12_APPEARING, true);
      } else if (anim.getTiming() == ITechAnim.ANIM_TIME_0_MAIN) {
         //MAIN

      } else if (anim.getTiming() == ITechAnim.ANIM_TIME_2_EXIT) {
         //EXIT
         d.setStateFlag(ITechDrawable.STATE_13_DISAPPEARING, true);
      }
   }

   public void setInitialize() {

   }

   public void setRepeat(int repeat) {
      this.repeats = repeat;
      definition.setValue(IBOAnim.ANIM_OFFSET_07_REPEAT1, repeat, 1);
   }

   /**
    * Change the core speed. No effect if sleep function is working
    */
   public void setSleep(int sleep) {
      this.sleep = sleep;
      definition.setValue(IBOAnim.ANIM_OFFSET_08_SLEEP2, sleep, 1);
   }

   public void setSleepFunction(Function sf) {
      sleepFunc = sf;
   }

   public void setTarget(int tar) {
      target = tar;
   }

   /**
    * Setup the animation number of frames so that it completes before the number of millisec using the current speed.
    * Most of the time, a transition may not take more than 200ms. If user press a key this value is reduced to 180
    * then 160, then 140.
    * @param millisec if too small, the animation will have only 1 frame
    */
   public void setTimeRoof(int millisec) {

   }

   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public IDLog toDLog() {
      return gc.toDLog();
   }

   public void toString(Dctx dc) {
      dc.root(this, "DrawableAnim ");

      dc.append(" Speed=" + sleep);
      dc.append(" Timing=" + ToStringStaticGui.debugTiming(getTiming()));
      dc.append(" DrawType=" + ToStringStaticGui.debugAnimType(getType()));
      if (repeats == -1) {
         dc.append(" Infinite Loop");
      } else {
         dc.append(" repeats=" + repeats);
      }
      dc.nlLvl1Line(d, "Drawable");

      dc.nlLvl("StepFunction", stepFunction);
   }

   
   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "DrawableAnim");
   }
   
   public UCtx toStringGetUCtx() {
      return gc.getUCtx();
   }

   //#enddebug
}
