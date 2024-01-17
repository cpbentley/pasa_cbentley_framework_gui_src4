package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.interfaces.IAnimable;
import pasa.cbentley.framework.gui.src4.interfaces.IBOUserInterAction;
import pasa.cbentley.framework.input.src4.gesture.GestureXY;

/**
 * Anything that interacts with the user, passively or actively annoying the user.
 * <br>
 * user interaction is used to help the user modify things he doesn't like about
 * what just happened.. flag user interaction as slow for example.
 * <br>
 * Speed up an animation for example.
 * When an animation is running? how does code know if user wants to kill animation
 * or just interact? Well intro anims are killed as as soon as an event is registered
 * on its area
 * 
 * it is a feedback tool
 * <br>
 * Interactions are:
 * <li>Animation: {@link IAnimable} is a passive user interaction. 
 * <li>Sounds : Passive
 * <li>Commands: {@link MCmd} is an active user interaction.
 * <li>Key/Pointer events
 * <li>Gestures: {@link GestureXY}
 * <br>
 * <br>
 * 
 * Continuous Passive Interactions
 * <li>Specific ubiquitous animation like Caret Blinking
 * <li>Music Playing
 * <br>
 * a repaint is not a user interaction.
 * <br>
 * The user may query the last few user interactions. IDying the animation, the user may decide to
 * <li>mute it if it plays sounds.
 * <li>Remove animation altogether
 * <li>Increase/Decrease its speed
 * <br>
 * <br>
 * <b>Animations</b>:
 * <br>
 * Change the types of animations for that situation. Actually a choice is made randomly in the list for that situation.
 * When an Animation is playing, a user may shorten it by pressing any key or pointer. This will race the animation to its end
 * and remember to increase the speed for next instance of that situation. 
 * Giving the millisecond maximum recorded at the user event.
 * <br>
 * Input/Output animations are the most likely to be accelerated. There are separated from other animations.
 * In Android, you can speed up animations as a system wide setting. However it speeds up passive background animations
 * as well.
 * <br>
 * <b>Commands</b>:
 * <br>
 * A {@link MCmd} is also a user interaction (active). The user decides to change its triggers.
 * <br>
 * <br>
 * <b>Scope</b>
 * <br>
 * <br>
 * A {@link UserInteraction} is scoped to the module that created it. It cannot be used outside the module.
 * <br>
 * The userinteraction sound when you click a button will always be for that button.
 * @author Charles-Philip Bentley
 *
 */
public class UserInteraction implements IStringable {

   private ByteObject     me;

   private String         path;

   private String         pathAux;

   protected final GuiCtx gc;

   public UserInteraction(GuiCtx gc, int type) {
      this.gc = gc;
      me = new ByteObject(gc.getBOC(), IBOTypesGui.TYPE_141_USER_INTERACTION, IBOUserInterAction.UIA_BASIC_SIZE);
      me.set1(IBOUserInterAction.UIA_OFFSET_02_TYPE1, type);
   }

   public String getAux() {
      return pathAux;
   }

   public String getString() {
      return path;
   }

   public ByteObject getTech() {
      return me;
   }

   public boolean isAccepted() {
      return !me.hasFlag(IBOUserInterAction.UIA_OFFSET_01_FLAG, IBOUserInterAction.UIA_FLAG_1_REJECTED);
   }

   public void setAux(String type) {
      pathAux = type;
   }

   public void setString(String string) {
      path = string;
   }


   //#mdebug
   public IDLog toDLog() {
      return toStringGetUCtx().toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "UserInteraction");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "UserInteraction");
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return gc.getUCtx();
   }

   //#enddebug
   

}
