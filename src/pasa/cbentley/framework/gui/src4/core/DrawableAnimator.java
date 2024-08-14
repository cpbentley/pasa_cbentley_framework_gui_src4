package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.utils.BitUtils;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.anim.AnimManager;
import pasa.cbentley.framework.gui.src4.anim.IBOAnim;
import pasa.cbentley.framework.gui.src4.anim.ITechAnim;
import pasa.cbentley.framework.gui.src4.anim.base.AnimAggregate;
import pasa.cbentley.framework.gui.src4.anim.definitions.SizeMod;
import pasa.cbentley.framework.gui.src4.anim.move.Move;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ToStringStaticGui;
import pasa.cbentley.framework.gui.src4.interfaces.IAnimable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechAnimableDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;

/**
 * Aggregate and Manage the animations of a {@link Drawable}.
 * <br>
 * <br>
 * In the painting control flow, paints when {@link ITechCanvasDrawable#REPAINT_14_SYSTEM_REPAINT} context is set
 * 
 * <br>
 * <br>
 * <b>Animated DLayers</b>
 * Tags which layers are animated.
 * <br>
 * <br>
 * When used with global animations.
 * In a cycle, {@link DrawableAnimator} updates the cache from layer animations.
 * <br>
 * <br>
 * 
 * RGB cache using animations ask the Drawable to draw the cache. Each Turn the directly modifies the cache.
 * Animation tracks if cache has been content updated. when so, it updates the cache using its current state.
 * <br>
 * <br>
 * That cache are merged into one for drawing purposes.
 * <br>
 * Animations that modifies parameters like size {@link SizeMod} or position {@link Move} are not drawing anything.
 * <br>
 * <br>
 * An Alpha
 * Tag if they need a RGB cache.
 * <li>mandatory by nature
 * <li>of if timing is faster with a cache.
 * <br>
 * <br>
 * 
 * For the content method, draw
 * <br>
 * 
 * For a Drawable animation that modifies the Width and Height of a Drawable?
 * <li>Tricky! Start method gives a starting size, stores original size
 * <li> End method should give back original size
 * <br>
 * <br>
 * So what about a mix of 0 to size including a Move?<br>
 * 
 * How does the {@link Move} works with the {@link SizeMod} animation class?
 * {@link AnimAggregate} working on the same {@link IDrawable}.<br>
 * 
 * Syncs the turns. Animation Move turn modifies {@link Drawable} coordinate
 * Animation SizeMode modifies size with {@link IDrawable#init(int, int)}.
 * Aggregate follows an order.<br>
 * <br>
 * <br>
 * For the paint method, they call {@link Drawable#draw(GraphicsX)}.
 * <br>
 * <br>
 * @author Charles-Philip Bentley
 *
 */
public class DrawableAnimator implements IStringable, ITechAnimableDrawable {

   /**
    * Set when at least 1 main anim
    */
   public static final int CTRL_FLAG_1_MAIN  = 1;

   /**
    * Set when there is at least 1 entry animation
    */
   public static final int CTRL_FLAG_2_ENTRY = 2;

   /**
    * Set when there is at least 1 entry animation
    */
   public static final int CTRL_FLAG_3_EXIT  = 4;

   public static final int INDEX_ENTRY       = 0;

   public static final int INDEX_EXIT        = 1;

   public static final int INDEX_MAIN        = 2;

   public static final int INNER_5_BG        = 16;

   public static final int INNER_6_CONTENT   = 32;

   public static final int INNER_7_FG        = 64;

   public static final int INNER_ANIM        = 8;

   public static final int LAYER_0_BG1       = 0;

   /**
    * 9 space
    * {@link FigDrawable} encapsulates the animated Drawable layers.
    * <br>
    * {@link FigDrawable} is initialized with the right style dimension area and x,y coordinate.
    * <br>
    * <br>
    * After that animation plays with everything it likes in the {@link IAnimable#paint(GraphicsX)}, nothing happens.
    * <br>
    * <br>
    * Animations is controlled by {@link AnimAggregate}.
    * <br>
    * <br>
    * 
    */
   public FigDrawable[]    animated          = new FigDrawable[9];

   /**
    * All the animations of the Drawable.
    * <br>
    * <br>
    * Sorted by Type? No. Animator iterates to find the ones he is looking for.
    */
   private IAnimable[]     animations        = new IAnimable[3];

   /**
    * Next empty value for {@link DrawableAnimator#animations}
    */
   private int             animNextEmpty;

   /**
    * <li> {@link DrawableAnimator#CTRL_FLAG_1_MAIN}
    * <li> {@link DrawableAnimator#CTRL_FLAG_2_ENTRY}
    * <li> {@link DrawableAnimator#CTRL_FLAG_3_EXIT}
    * 
    */
   private int             ctrlFlags;

   /**
    * Animated Drawable
    */
   Drawable                d;

   /**
    * Set for animated layer 
    */
   public int              layerFlags;

   protected final GuiCtx  gc;

   /**
    * Constructor requires a non null {@link Drawable}.
    * <br>
    * <br>
    * @param dr
    */
   public DrawableAnimator(GuiCtx gc, Drawable dr) {
      this.gc = gc;
      d = dr;
   }

   /**
    * Drawable Anim overrides style layer animations, unless they are compatible.
    * <br>
    * <br>
    * How is the compatibility modeled?
    * 
    * When Main is set and drawable is already shown, fire animation
    * <br>
    * If state is not appearing or disappearing, main animatioin is played.
    * <br>
    * <br>
    * 
    * Stops and Replace any existing animation in the time period slot.
    * <br>
    * <br>
    * @param anim
    */
   public void addDrawableAnim(IAnimable anim) {
      if (anim == null) {
         return;
      }
      if (anim.getTarget() != 0) {
         //dividing
      }
      int index = -1;
      for (int i = 0; i < animNextEmpty; i++) {
         if (animations[i] == null) {
            index = i;
         }
      }
      if (index == -1) {
         index = animNextEmpty;
         animNextEmpty++;
         if (index >= animations.length) {
            IAnimable[] nanimations = new IAnimable[animations.length + 3];
            for (int i = 0; i < animations.length; i++) {
               nanimations[i] = animations[i];
            }
            animations = nanimations;
         }
      }
      animations[index] = anim;
      //      if (type == INDEX_MAIN) {
      //         if (d.hasState(IDrawable.STATE_02_DRAWN)) {
      //            showMain();
      //         }
      //      }
   }

   /**
    * For 8 style layers, that's a {@link FigDrawable} encapsulating the {@link ByteObject} figure.
    * <br>
    * <br>
    * For the content, a clone of {@link Drawable} with behavior {@link ITechDrawable#BEHAVIOR_04_CONTENT_ONLY}.
    * <br>
    * <br>
    * @param anim
    */
   public void addLayerAnim(IAnimable anim) {
      if (anim.getDrawable() == d) {
         addDrawableAnim(anim);
      } else {
         addDrawableAnim(anim);
         animated[0] = (FigDrawable) anim.getDrawable();
      }
   }

   /**
    * Check if this {@link IAnimable} is the last animating animation and then set {@link ITechDrawable#STATE_11_ANIMATING} to false.
    * <br>
    * <br>
    * Callback from Drawable when event {@link ITechDrawable#EVENT_05_ANIM_FINISHED} is recieved.
    * <br>
    * <br>
    * 
    * @param anim
    */
   public void animFinished(IAnimable anim) {
      //#debug
      toDLog().pAnim(anim.toString1Line() + " for " + d.toString1Line() + " " + Thread.currentThread(), this, DrawableAnimator.class, "animFinished", LVL_05_FINE, true);

      //bef
      anim.setAnimFlag(ANIM_14_CONTROLLED, false);
      boolean isLast = isLastofKind(anim);
      if (isLast) {
         d.setStateFlag(ITechDrawable.STATE_11_ANIMATING, false);
         if (anim.hasAnimFlag(ANIM_24_OVERRIDE_DRAW)) {
            d.setStateFlag(ITechDrawable.STATE_20_ANIMATED_FULL_HIDDEN, false);
         }
         int timing = anim.getTiming();
         if (timing == ITechAnim.ANIM_TIME_1_ENTRY) {
            //Drawable is officially no more hidden and is elligible to be drawn by the control flow of the draw method
            d.setStateFlag(ITechDrawable.STATE_03_HIDDEN, false);
            d.setStateFlag(ITechDrawable.STATE_20_ANIMATED_FULL_HIDDEN, false);
            d.setStateFlag(ITechDrawable.STATE_12_APPEARING, false);
         } else if (timing == ITechAnim.ANIM_TIME_0_MAIN) {
            d.setStateFlag(ITechDrawable.STATE_03_HIDDEN, false);
            d.setStateFlag(ITechDrawable.STATE_02_DRAWN, false);
         } else if (timing == ITechAnim.ANIM_TIME_2_EXIT) {
            //that was the exit animation finishing.
            //make sure the flags are set
            d.setStateFlag(ITechDrawable.STATE_03_HIDDEN, true);
            d.setStateFlag(ITechDrawable.STATE_02_DRAWN, false);
            d.setStateFlag(ITechDrawable.STATE_13_DISAPPEARING, false);
            //
            d.getVC().getTopo().removeDrawableNoAnimHooks(d);
         }
      }
   }

   public void animStartedThreadAnim(IAnimable anim) {

   }

   /**
    * Called by {@link Drawable} when notified of event {@link ITechDrawable#EVENT_06_ANIM_STARTED}
    * <br>
    * <br>
    * Sets the appropriate flags and aggregate with any running animation.
    * <br>
    * <br>
    * Especially relating to {@link ITechAnim#ANIM_TIME_1_ENTRY} flags.
    * <br>
    * Called in the UI Thread
    * @param anim
    */
   public void animStartedThreadUI(IAnimable anim) {
      //#debug
      toDLog().pAnim(anim.toString1Line() + " for " + d.toString1Line() + " " + Thread.currentThread(), this, DrawableAnimator.class, "animStartedThreadUI", LVL_05_FINE, true);

      d.setStateFlag(ITechDrawable.STATE_11_ANIMATING, true);

      if (anim.hasAnimFlag(ANIM_24_OVERRIDE_DRAW)) {
         d.setStateFlag(ITechDrawable.STATE_20_ANIMATED_FULL_HIDDEN, true);
      }
      if (anim.hasAnimFlag(ANIM_25_OVERRIDE_DRAW_CONTENT)) {
         d.setStateFlag(ITechDrawable.STATE_22_ANIMATED_CONTENT_HIDDEN, true);
      }
      int timing = anim.getTiming();
      if (timing == ITechAnim.ANIM_TIME_1_ENTRY) {
         //ENTRY
         d.setStateFlag(ITechDrawable.STATE_12_APPEARING, true);
      } else if (timing == ITechAnim.ANIM_TIME_0_MAIN) {
         //MAIN

      } else if (timing == ITechAnim.ANIM_TIME_2_EXIT) {
         //EXIT
         d.setStateFlag(ITechDrawable.STATE_13_DISAPPEARING, true);
      }
   }

   /**
    * Called when {@link IDrawable#draw(GraphicsX)} is called and {@link ITechDrawable#STATE_20_ANIMATED_FULL_HIDDEN} is set.
    * <br>
    * <br>
    * That flag was set by an animation
    * Depending on the state, draws entry/main/exit animations
    * <br><br>
    * <br>
    * Only relates to {@link IAnimable#ANIM_24_OVERRIDE_DRAW}. When the animation {@link IAnimable#paint(GraphicsX)} method is to be called.
    * <br>
    * <br>
    * Both the drawable draw method and animation draw can live together. For instance Animation draws a fire effect over the regular Drawable.
    * <br>
    * <br>
    * 
    * @param g
    */
   public void drawAnimations(GraphicsX g) {
      //SystemLog.printAnim("#DrawableAnimator#drawAnimations animNextEmpty=" + animNextEmpty);
      for (int i = 0; i < animNextEmpty; i++) {
         IAnimable anim = animations[i];
         boolean isAnimPaint = anim.hasAnimFlag(ANIM_24_OVERRIDE_DRAW) && anim.hasAnimFlag(ANIM_14_CONTROLLED);
         if (isAnimPaint) {
            anim.paint(g);
         }
      }
   }

   public FigDrawable getFg(int i) {
      return animated[i];
   }

   /**
    * <li> {@link DrawableAnimator#CTRL_FLAG_1_MAIN}
    * <li> {@link DrawableAnimator#CTRL_FLAG_2_ENTRY}
    * <li> {@link DrawableAnimator#CTRL_FLAG_3_EXIT}
    * @param flag
    * @return
    */
   public boolean hasFlag(int flag) {
      return BitUtils.hasFlag(ctrlFlags, flag);
   }

   /**
    * Checks if there is another animation of the same timing ( {@link IBOAnim#ANIM_OFFSET_05_TIME1} currently managed by the animatior
    * <br>
    * <br>
    * 
    * @param a
    * @return
    */
   private boolean isLastofKind(IAnimable a) {
      int timing = a.getDefinition().get1(IBOAnim.ANIM_OFFSET_05_TIME1);
      for (int i = 0; i < animNextEmpty; i++) {
         IAnimable ani = animations[i];
         if (ani != a && ani.getDefinition().get1(IBOAnim.ANIM_OFFSET_05_TIME1) == timing) {
            if (ani.hasAnimFlag(ANIM_14_CONTROLLED)) {
               return false;
            }
         }
      }
      return true;
   }

   /**
    * Kick start Entry or Main animation.
    */
   public void notifyShow() {
      showEntry();
      if (!d.hasState(ITechDrawable.STATE_12_APPEARING)) {
         showMain();
      }
   }

   public void removeAnimation(int timing) {
      for (int i = 0; i < animNextEmpty; i++) {
         if (animations[i] != null && animations[i].getTiming() == timing) {
            animations[i] = null;
         }
      }
   }

   public AnimManager getAnimCreator() {
      return d.getAnimCreator();
   }

   /**
    * Stops and removes all {@link IAnimable}.
    */
   public void removeAnimations() {
      for (int i = 0; i < animNextEmpty; i++) {
         if (animations[i] != null) {
            if (!animations[i].hasAnimFlag(ANIM_13_STATE_FINISHED)) {
               getAnimCreator().animStop(animations[i]);
            }
            animations[i] = null;
         }
      }
   }

   /**
    * <li> {@link DrawableAnimator#CTRL_FLAG_1_MAIN}
    * <li> {@link DrawableAnimator#CTRL_FLAG_2_ENTRY}
    * <li> {@link DrawableAnimator#CTRL_FLAG_3_EXIT}
    * 
    * @param flag
    * @param v
    */
   public void setFlag(int flag, boolean v) {
      ctrlFlags = BitUtils.setFlag(ctrlFlags, flag, v);
   }

   /**
    * How do we make sure the entry animations are started at the same frame cycle? 
    * <br>
    * <br>
    * 
    * We don't want first anim to load and execute its first turn, second loads.
    * Well. animation must require synchro together. that is, generically speaking
    * for an animation to start its first turn, it must check if its starting conditions are all good.
    * So if one condition requires another IAnimable to be {@link IAnimable#ANIM_11_STATE_RUNNING} it will have to wait for
    * that animation {@link IAnimable#lifeStart()} to finish its init work.
    * <br>
    * <br>
    * 
    */
   public void showEntry() {
      showGeneric(CTRL_FLAG_2_ENTRY, ITechAnim.ANIM_TIME_1_ENTRY);
   }

   /**
    * 
    */
   public void showExit() {
      //we must stop previous animation
      stopAnimation(ITechAnim.ANIM_TIME_0_MAIN);
      stopAnimation(ITechAnim.ANIM_TIME_1_ENTRY);
      showGeneric(CTRL_FLAG_3_EXIT, ITechAnim.ANIM_TIME_2_EXIT);

   }

   /**
    * we have to set the 
    * {@link DrawableAnimator#animStartedThreadUI(IAnimable)}
    * <br>
    * Only when drawable is not visible at the start
    * <br>
    * Called in the UI Thread.
    * @param flagCtrl
    * @param timing
    */
   private void showGeneric(int flagCtrl, int timing) {
      //#debug
      toDLog().pAnim("Timing=" + ToStringStaticGui.debugTiming(timing), this, DrawableAnimator.class, "showGeneric", LVL_05_FINE, true);

      for (int i = 0; i < animNextEmpty; i++) {
         if (animations[i] != null) {
            IAnimable anim = animations[i];
            if (anim.getTiming() == timing) {
               anim.reset();
               //flag anim as drawable controlled.
               anim.setAnimFlag(ANIM_14_CONTROLLED, true);
               //TODO here we have to set the flag
               //animStartedThreadUI(anim);
               getAnimCreator().animPlay(anim);
            }
         }
      }
      //then 
   }

   public void showMain() {
      showGeneric(CTRL_FLAG_1_MAIN, ITechAnim.ANIM_TIME_0_MAIN);
   }

   public void stopAnimation(int timing) {
      for (int i = 0; i < animNextEmpty; i++) {
         if (animations[i] != null && animations[i].getTiming() == timing) {
            IAnimable ia = (IAnimable) animations[i];
            if (!ia.hasAnimFlag(ANIM_13_STATE_FINISHED)) {
               getAnimCreator().animStop(ia);
            }
         }
      }
   }

   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public IDLog toDLog() {
      return gc.toDLog();
   }

   public void toString(Dctx dc) {
      dc.root(this, "DrawableAnimator ");
      dc.nlLvlArray("#Animations", animations);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root(this, "DrawableAnimator");
   }

   public UCtx toStringGetUCtx() {
      return gc.getUC();
   }
   //#enddebug
}
