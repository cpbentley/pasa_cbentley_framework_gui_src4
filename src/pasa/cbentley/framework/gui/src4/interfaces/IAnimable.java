package pasa.cbentley.framework.gui.src4.interfaces;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.core.ui.src4.interfaces.IUserInteraction;
import pasa.cbentley.framework.core.ui.src4.user.UserInteraction;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.engine.RgbImage;
import pasa.cbentley.framework.gui.src4.anim.ITechAnim;
import pasa.cbentley.framework.gui.src4.anim.base.DrawableAnim;
import pasa.cbentley.framework.gui.src4.anim.base.ImgAnimable;
import pasa.cbentley.framework.gui.src4.anim.definitions.AlphaChangeRgb;
import pasa.cbentley.framework.gui.src4.anim.move.Move;
import pasa.cbentley.framework.gui.src4.canvas.CanvasAppliInputGui;
import pasa.cbentley.framework.gui.src4.canvas.ViewContext;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.RgbDrawable;

/**
 * Animates an {@link IDrawable}. For animation an {@link RgbImage}, use the class {@link RgbDrawable}.
 * <br>
 * <br>
 * Like the {@link MCmd}, it isA {@link UserInteraction}. 
 * <br>
 * <br>
 * <b>State Machine</b>:
 * <br>
 * <li>When added, animation start its life in state 1.
 * <li>Once {@link IAnimable#lifeStart()} has been called 
 * <li> {@link IAnimable#ANIM_11_STATE_RUNNING} when start method has returned
 * <li> {@link IAnimable#ANIM_13_STATE_FINISHED} when {@link IAnimable#lifeEnd()} has been called
 * <br>
 * <br>
 * Controls the time stepping of each animation frame, setting repaint of Drawable as true.
 * <br>
 * <br>
 * {@link IAnimable#lifeStart()} Animation hides {@link IDrawable} when state {@link IAnimable#ANIM_24_OVERRIDE_DRAW} is set.
 * <br>
 * <br>
 * <b>Loop</b>
 * <br>
 * The basic scheme follows this pattern <br>
 * {@link IAnimable#lifeStart()} <br>
 * 
 * until {@link IAnimable#isFinished()} <br>
 * doAnimationStep [function,sleep] <br>	
 * 
 * {@link IAnimable#lifeEnd()} <br>
 * So there might be no step at all if isFinished return true after the initialization
 * 
 * {@link IAnimable#paint(GraphicsX)} is the entry point for drawing the current step.<br>
 * <br>
 * Animation are classified in computation power so the most demanding are switched off/stepped down on a weak CPU<br>
 * <br>
 * <br>
 * <b>Animation Turns </b><br>
 * Drawable Animation turns are controlled by a function. 
 * Most of the time f(x).
 *  <br>
 * <br>
 * <br>
 * <b>Links between {@link IAnimable} and {@link IDrawable} </b><br>
 * <li> {@link ITechDrawable#STATE_11_ANIMATING}
 * <li> {@link ITechDrawable#EVENT_05_ANIM_FINISHED}
 * <li> {@link ITechDrawable#EVENT_06_ANIM_STARTED}
 * <li> 
 * <br>
 * <br>
 * When the Animation sets the {@link IAnimable#ANIM_25_OVERRIDE_DRAW_CONTENT}, the {@link IAnimable#paint(GraphicsX)} is called  
 * instead of the content drawing method.
 * <br>
 * <br>
 * A drawable may be directly modified with a {@link ByteObjectMod} or indirectly with an {@link ImgAnimable}. 
 * <br>
 * <br>
 * <br>
 * Animation uses {@link Drawable#draw(GraphicsX)} method and its control flow to draw each of its step.
 * or it draws by itself (case of {@link ImgAnimable}.
 * {@link AlphaChangeRgb} and {@link Move} are examples of {@link ImgAnimable}. They work on Drawable's pixel data.
 * The move class may be direct if the drawing time of the drawable is fast and area is big. For small Drawable though, the move
 * will always be indirect.
 * <br>
 * <br>
 * <b>Screen transitions</b>.
 * <br>
 * Many frameworks use the concept of screen transitions. In our framework, that means the main drawable of {@link MasterCanvas} being
 * replaced by another.  
 * <br>
 * A screen transition is
 * <li>An exit animation for the visible drawable
 * <li>an entry animation of the invisible drawable
 * <br>
 * Animations are run concurrently.
 * Layer animations may run.
 * <br>
 * {@link ByteObject}.
 * What happens to the Entry/Exit animation of sub drawables? They may be turned off. It depends if the parent drawable forward 
 * hide event.
 * <br>
 * The figure is deemed animated.
 *  Managed by {@link ByteObjectFig#setFigSubFigures(ByteObject, ByteObject[])} 
 * <br>
 * <br>
 * <b>Animation Sound?</b>
 * <br>
 * How does an animation plays a sound each time it starts? Using an {@link IAction} and a Sound integer ID.
 * <li>Sound ID
 * <li>Hard coded MIDI notes.
 * <li>Wav ID.
 * <br>
 * <br>
 * Use Framework API that enable quick porting.Base is the  {@link IMIDIControl}.
 * {@link Player} from interface javax.microedition.media.Player sucks and does not allow mixing.
 * <br>
 * Sound FX / Music may be totally disabled in options. 
 * <br>
 * <br>
 * <b>Boundary / Bounds</b> <br>
 * Imagine a one big non cached expensive drawable. An frontmost Drawable animates a figure as an overlay of that.
 * <br>
 * Best performance is achieve by caching it :)
 * <br>
 * On the other hand, if goes over tabled many expensive drawables, we want to repaint only Drawable that are overlayed in the turn.
 * <br>
 * We need to know the boundary of the current turn of the animation.
 * <br>
 * We don't want to repaint all Drawables. That's the use of {@link IAnimable#getBounds()}, this gives the Drawable in need to repaint
 * when the partial repaint call is made.
 * <br>
 * ZIndex is actually managed by Drawable layouting. Animation don't decide.
 * <br>
 * <br>
 * @author Charles-Philip Bentley
 * @see DrawableAnim
 * @see ImgAnimable
 *
 */
public interface IAnimable extends IStringable, ITechAnimableDrawable {


   /**
    * When a moving {@link IDrawable} is repainted through the animation {@link ScreenResult},
    * the previous position must be erased in optimized repaint. Therefore, it gets the boundaries
    * of the previous paint. When a moving animation {@link IAnimable#paint(GraphicsX)} is called. it locks
    * the last boundary used.
    * <br>
    * <br>
    * This is not needed when a {@link ITechCanvasDrawable#REPAINT_01_FULL} is made.
    * The int array returned contains 5 values. The current bound rectangle x,y,w,h,color
    * If alpha of color is fully transparent, no clearing occurs
    * 
    * @return
    */
   public int[] getBounds();

   /**
    * Called in the Rendering Thread before adding to the animation queue.
    * <br>
    * <br>
    * In Passive Mode, the Rendering Thread is the UI thread.
    * In Active mode, the Rendering Thread is separate
    */
   public void lifeStartUIThread();

   /**
    * The Drawable animated by this {@link IAnimable}.
    * <br>
    * <br>
    * THe {@link IDrawable} provides position, drawing area, zindex through the topology.
    * <br>
    * {@link IDrawable} delegates drawing to the Animation.
    * <br>
    * Also provides the {@link ViewContext} Canvas context if any. That is used for requesting
    * a repaint on the {@link CanvasAppliInputGui}
    * <br>
    * @return
    */
   public IDrawable getDrawable();

   /**
    * Genetics flag + state
    * + realisator states.
    * @return
    */
   public boolean hasAnimFlag(int flag);

   /**
    * True if the animation has finished all its turns.
    * <br>
    * @return
    */
   public boolean isFinished();

   /**
    * Called when the animation is just about to be removed from the animation cycle
    * <br>
    * <br>
    * Free up resources.
    * <br>
    * Should return very quickly. Use another thread for long running operations.
    */
   public void lifeEnd();

   /**
    * Called just before the animation is queued by the animaiton thread. 
    * <br>
    * <br>
    * Use this method to initialize variables that weren't in the constructor
    * <br>
    * <br>
    * i.e. Get the {@link RgbImage} lock on the drawable cache.
    * <br>
    * Generates event {@link ITechDrawable#EVENT_06_ANIM_STARTED} to listeners
    * <br>
    * Is this the first frame?
    */
   public void lifeStart();

   /**
    * Readies the animable for next turn.
    * <br>
    * <br>
    * This is done in which thread?
    * <br>
    * It might take more time than confortable. which will slow down faster concurrent animations quite significantly.
    * <br>
    * @return the next sleep time or -1 if finished
    */
   public int nextTurn();

   /**
    * Called ?
    */
   public void loadHeavyResources();

   /**
    * Paints the current frame of the animation in the Rendering thread.
    * <br>
    * How to avoid synchronization issue
    * <br>
    * Called by {@link Drawable#draw(GraphicsX)} control flow when needed.
    * 
    * <b>Several possibilities</b> :
    * <li> Target {@link IDrawable} draws itself using the {@link IDrawable#draw(GraphicsX)}.
    * <li> Target {@link IDrawable} draws itself using the {@link IDrawable#drawDrawable(GraphicsX)}.
    * <li>Animation class does its own painting each turn
    * <br>
    * Included in anim genetics
    * <br>
    * @param g {@link GraphicsX}.
    */
   public void paint(GraphicsX g);

   /**
    * Finish the animation as soon as possible.
    * <br>
    * Puts it in its final skipping all remaining frames.
    * <br>
    * Called in the Rendering thread just before the repaint of the Update call
    * that asked for the race (because of a user action)
    * <br>
    * An animation is a {@link IUserInteraction} and several races will shut it down.
    * <br>
    * Race are done on {@link ITechAnim#ANIM_TIME_1_ENTRY}
    * 
    * You cannot race a main animation. You can stop it though if stoppable.
    * because animation is just pretty sugar and is not functional.
    * <br>
    */
   public void race();

   /**
    * Resets the function and other variable so the animation can start again.
    * <br>
    * <li>It is used by entry/exit animation to reuse the animation parameters several times in a row. <br> 
    * <li>It is used by animation with a repeat count or infinite loop. 
    */
   public void reset();

   /**
    * Target of Drawable.
    * <li> {@link ITechAnim#ANIM_TARGET_0_FULL}
    * <li> {@link ITechAnim#ANIM_TARGET_1_DLAYER1}
    * <li> {@link ITechAnim#ANIM_TARGET_2_DLAYER1}
    * <li> {@link ITechAnim#ANIM_TARGET_9_CONTENT}
    * 
    * @return
    */
   public int getTarget();

   /**
    * Draw type
    * <li>{@link ITechAnim#ANIM_DRAW_0_OVERRIDE}
    * <li>{@link ITechAnim#ANIM_DRAW_1_CACHE}
    * <li>{@link ITechAnim#ANIM_DRAW_2_DRAWABLE}
    * 
    * @return
    */
   public int getType();

   /**
    * 
    * <li>{@link ITechAnim#ANIM_TIME_0_MAIN}
    * <li>{@link ITechAnim#ANIM_TIME_1_ENTRY}
    * <li>{@link ITechAnim#ANIM_TIME_2_EXIT}
    * <br>
    * <br>
    * 
    * @return
    */
   public int getTiming();

   public ByteObject getDefinition();

   /**
    * Sets flag
    * <li> {@link IAnimable#ANIM_01_BUSINESS}
    * <li> {@link IAnimable#ANIM_02_REVERSABLE}
    * 
    * @param state
    * @param v
    */
   public void setAnimFlag(int state, boolean v);

   /**
    * Sets the repeat value for the animation.
    * <br>
    * Ignored when step function is looping {@link ByteObject#FUN_FLAG_3_LOOPING}.
    * <br>
    * @param repeat {@link IAnimable#REPEAT_INFINITE}
    */
   public void setRepeat(int repeat);

   /**
    * Change the speed in milliseconds
    * @param speed
    */
   public void setSleep(int speed);

}
