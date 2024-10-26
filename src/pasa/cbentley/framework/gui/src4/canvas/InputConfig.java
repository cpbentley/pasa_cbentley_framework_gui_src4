package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.core.ui.src4.ctx.ToStringStaticCoreUi;
import pasa.cbentley.framework.core.ui.src4.event.RepeatEvent;
import pasa.cbentley.framework.core.ui.src4.exec.ExecutionContext;
import pasa.cbentley.framework.core.ui.src4.input.InputState;
import pasa.cbentley.framework.core.ui.src4.tech.IInput;
import pasa.cbentley.framework.core.ui.src4.tech.ITechCodes;
import pasa.cbentley.framework.core.ui.src4.tech.ITechInputFeedback;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.ViewDrawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ObjectGC;
import pasa.cbentley.framework.gui.src4.exec.InputStateCanvasGui;
import pasa.cbentley.framework.gui.src4.exec.OutputStateCanvasGui;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.input.src4.engine.OutputStateCanvas;

/**
 * 
 * Aggregates for the View module
 * <li> {@link InputStateCanvasGui} is a {@link InputState}
 * <li> {@link CmdInstance}
 * <li> {@link OutputStateCanvasGui} is a {@link OutputStateCanvas}
 * <li> {@link ExecutionContext}
 * <br>
 * 
 * A method that requires an {@link InputConfig} as parameter means it needs to run
 * inside the execution context (input state, update state, request render)
 * It is initiated by a user event, or by a background thread.
 * That thread takes the root canvas or the canvas to which the drawable it works on belongs to.
 * 
 * @author Charles-Philip Bentley
 *
 */
public class InputConfig extends ObjectGC implements IStringable, IInput {

   protected final ICanvasDrawable canvas;

   //#debug
   private IDrawable               d;

   private ExecutionContext        ex;

   private InputStateCanvasGui     is;

   private OutputStateCanvasGui    sr;

   public InputConfig(GuiCtx gc, ICanvasDrawable canvas, InputStateCanvasGui isd, OutputStateCanvasGui srd) {
      super(gc);
      this.canvas = canvas;
      setInputStateDrawable(isd);
      setCanvasResultDrawable(srd);
   }

   public void actionDone() {
      actionDone(null, ITechInputFeedback.FLAG_01_ACTION_DONE);
   }

   public void actionDone(Object o, int type) {
      getCanvasResultDrawable().actionDone(o, type);
   }

   public void debugSrActionDoneRepaint(String actionStr) {
      getCanvasResultDrawable().debugSetActionDoneRepaint(actionStr);
   }

   public ICanvasDrawable getCanvas() {
      return canvas;
   }

   public OutputStateCanvasGui getCanvasResultDrawable() {
      return sr;
   }

   public OutputStateCanvasGui getCRD() {
      return getCanvasResultDrawable();
   }

   public int getDraggedDiffX() {
      return getInputStateDrawable().getDraggedDiffX();
   }

   public int getDraggedDiffY() {
      return getInputStateDrawable().getDraggedDiffY();
   }

   /**
    * The amount of X pixels dragged since the last press event.
    * <br>
    * <br>
    * @return value is positive or negative depending on direction
    */
   public int getDraggedVectorX() {
      return getInputStateDrawable().getDraggedVectorX();
   }

   /**
    * The amount of Y pixels dragged since the last press event.
    * <br> value is positive or negative depending on direction
    * @return
    */
   public int getDraggedVectorY() {
      return getInputStateDrawable().getDraggedVectorY();
   }

   public ExecutionContext getEx() {
      return ex;
   }

   public GuiCtx getGC() {
      return gc;
   }

   public int getIdKeyBut() {
      return getInputStateDrawable().getKeyCode();
   }

   public InputStateCanvasGui getInputState() {
      return getInputStateDrawable();
   }

   public InputStateCanvasGui getInputStateDrawable() {
      return is;
   }

   public InputStateCanvasGui getISD() {
      return getInputStateDrawable();
   }

   /**
    * 
    * @param last
    * @return
    */
   public int getKeyModSequence(int last) {
      return getInputStateDrawable().getKeyModSequence(last);
   }

   /**
    * -1 if no number is involved in the current event.
    * <br>
    * <br>
    * Returned values:
    * <li> 0 for 0 key
    * <li> 1 for 1 key
    * 
    * @return
    */
   public int getKeyNum() {
      return getInputStateDrawable().getKeyNum();
   }

   /**
    * Number of currently pressed keys.
    * @return
    */
   public int getPressedKeyCounter() {
      return getInputStateDrawable().getPressedKeyNum();
   }

   public boolean is0() {
      return getIdKeyBut() == ITechCodes.KEY_NUM0;
   }

   public boolean is0P() {
      return isPressedKeyboard0(ITechCodes.KEY_NUM0);
   }

   public boolean is1() {
      return getIdKeyBut() == ITechCodes.KEY_NUM1;
   }

   public boolean is1P() {
      return isPressedKeyboard0(ITechCodes.KEY_NUM1);
   }

   public boolean is2() {
      return getIdKeyBut() == ITechCodes.KEY_NUM2;
   }

   public boolean is2P() {
      return isPressedKeyboard0(ITechCodes.KEY_NUM2);
   }

   public boolean is3() {
      return getIdKeyBut() == ITechCodes.KEY_NUM3;
   }

   public boolean is3P() {
      return isPressedKeyboard0(ITechCodes.KEY_NUM3);
   }

   public boolean is4() {
      return getIdKeyBut() == ITechCodes.KEY_NUM4;
   }

   public boolean is4P() {
      return isPressedKeyboard0(ITechCodes.KEY_NUM4);
   }

   public boolean is5() {
      return getIdKeyBut() == ITechCodes.KEY_NUM5;
   }

   public boolean is5P() {
      return isPressedKeyboard0(ITechCodes.KEY_NUM5);
   }

   public boolean is6() {
      return getIdKeyBut() == ITechCodes.KEY_NUM6;
   }

   public boolean is6P() {
      return isPressedKeyboard0(ITechCodes.KEY_NUM6);
   }

   public boolean is7() {
      return getIdKeyBut() == ITechCodes.KEY_NUM7;
   }

   public boolean is7P() {
      return isPressedKeyboard0(ITechCodes.KEY_NUM7);
   }

   public boolean is8() {
      return getIdKeyBut() == ITechCodes.KEY_NUM8;
   }

   public boolean is8P() {
      return isPressedKeyboard0(ITechCodes.KEY_NUM8);
   }

   public boolean is9() {
      return getIdKeyBut() == ITechCodes.KEY_NUM9;
   }

   public boolean is9P() {
      return isPressedKeyboard0(ITechCodes.KEY_NUM9);
   }

   public boolean isActionDone() {
      return getCanvasResultDrawable().isActionDone();
   }

   public boolean isActive(int key) {
      return getInputStateDrawable().isActive(key);
   }

   public boolean isCancel() {
      return getInputStateDrawable().isCancel();
   }

   public boolean isCancelP() {
      return getInputStateDrawable().isCancelP();
   }

   public boolean isDown() {
      return getInputStateDrawable().isDown();
   }

   public boolean isDownActive() {
      return getInputStateDrawable().isDownActive();
   }

   public boolean isDownP() {
      return getInputStateDrawable().isDownP();
   }

   /**
    * Check whether the drawable is dragged in a gesture
    * @param viewedDrawable
    * @return
    */
   public boolean isDragged(IDrawable viewedDrawable) {
      return getInputStateDrawable().isDragged(viewedDrawable);
   }

   public boolean isDraggedDragged() {
      return getInputStateDrawable().isDraggedDragged();
   }

   /**
    * TODO generic marge when to release the event.
    * See
    * <li>pixel distance
    * <li>out of the drawable
    * @return
    */
   public boolean isDraggedPointer0Button0() {
      return getInputStateDrawable().isDragged();
   }

   /**
    * Is the key fast typed.
    * <br>
    * Problem is about the long repaints. the fast is not registered.
    * @param key
    * @return
    */
   public boolean isFastTyped(int key) {
      return getInputStateDrawable().isLastKeyFastTyped(key);
   }

   public boolean isFire() {
      return getInputStateDrawable().isFire();
   }

   public boolean isFireP() {
      return getInputStateDrawable().isFireP();
   }

   /**
    * True if mod is {@link IInput#MOD_6_GESTURED}
    * <br>
    * This mod is exclusive with {@link IInput#MOD_0_PRESSED}, 
    * <br>
    * This mode will occur when a worker thread generates Gesture event in the UI thread. Those gesture events will
    * trickle down the event consumer hierarchy like other events until some code decides to act upon the event.
    * <br>
    * @return
    */
   public boolean isGestured() {
      return getInputStateDrawable().isGestured();
   }

   /**
    * True if current state of inputconfig has a KeyType event. KeyType is actually a case of
    * the release event
    * @return
    */
   public boolean isKeyTyped() {
      return getInputStateDrawable().isKeyTyped();
   }

   public boolean isKeyTyped(int key) {
      return getInputStateDrawable().isKeyTyped(key);
   }

   public boolean isKeyTypedAlone(int key) {
      return getInputStateDrawable().isKeyTypedAlone(key);
   }

   public boolean isLeft() {
      return getInputStateDrawable().isLeft();
   }

   public boolean isLeftActive() {
      return getInputStateDrawable().isLeftActive();
   }

   public boolean isLeftP() {
      return getInputStateDrawable().isLeftP();
   }

   public boolean isMinusP() {
      return getInputStateDrawable().isMinusP();
   }

   public boolean isMoved() {
      return getInputStateDrawable().isModMoved();
   }

   public boolean isNavKey() {
      return getInputStateDrawable().isNavKey();
   }

   public boolean isPhotoP() {
      return getInputStateDrawable().isPhotoP();
   }

   public boolean isPinched() {
      return getInputStateDrawable().isPinched();
   }

   public boolean isPlusP() {
      return getInputStateDrawable().isPlusP();
   }

   /**
    * Returns true if the release event is done on the same Drawable on which the press was made.
    * Use {@link PointerGesture}.
    * @return
    */
   public boolean isPointerReleasedInsidePressed() {
      if (isReleased()) {

         return true;
      }
      return false;
   }

   /**
    * Is the # key pressed
    * @return
    */
   public boolean isPoundP() {
      return getInputStateDrawable().isPoundP();
   }

   /**
    * Is Pressed the last Modifier in the chain of events. For keys and pointer events.
    * @return
    */
   public boolean isPressed() {
      return getInputStateDrawable().isModPressed();
   }

   /**
    * A double press
    * @return
    */
   public boolean isPressedDouble() {
      return getInputStateDrawable().isPressedDouble();
   }

   /**
    * Is the key pressed, among all the keys currently pressed
    * <br>
    * <br>
    * @param key value from {@link ITechCodes#KEY_BACK}, {@link ITechCodes#KEY_NUM3}
    * @return
    */
   public boolean isPressedKeyboard0(int key) {
      return getInputStateDrawable().isPressedKeyboard0(key);
   }

   /**
    * A double click
    * @return
    */
   public boolean isPressedReleasedDouble() {
      return getInputStateDrawable().isPressedReleasedDouble();
   }

   /**
    * Check if the release event is not occuring further than the given distance for both x and y
    * @param value
    * @return
    */
   public boolean isReleaseAround(int value) {
      return getInputStateDrawable().isReleaseAround(value);
   }

   /**
    * Does not check if the release is inside
    * <br>
    * 
    * @return
    */
   public boolean isReleased() {
      return getInputStateDrawable().isModReleased();
   }

   public boolean isRight() {
      return getInputStateDrawable().isRight();
   }

   public boolean isRightActive() {
      return getInputStateDrawable().isRightActive();
   }

   public boolean isRightP() {
      return isPressedKeyboard0(ITechCodes.KEY_RIGHT);
   }

   public boolean isSequencedPressed(int key1, int key2) {
      return getInputStateDrawable().isSequencedPressedKeyboard0(key1, key2);
   }

   public boolean isSequencedTyped(int key1, int key2) {
      return getInputStateDrawable().isSequencedTypedKeyboard0(key1, key2);
   }

   /**
    * Is Soft key pressed 
    * @return
    */
   public boolean isSoftLeftP() {
      return getInputStateDrawable().isSoftLeftP();
   }

   public boolean isSoftRightP() {
      return getInputStateDrawable().isSoftRightP();
   }

   /**
    * Is the key * currently pressed
    * @return
    */
   public boolean isStarP() {
      return getInputStateDrawable().isStarP();
   }

   /**
    * True when * is press along side with that key
    * @param key
    * @return
    */
   public boolean isTriggerStar(int key) {
      return getInputStateDrawable().isTriggerStar(key);
   }

   public boolean isUp() {
      return getInputStateDrawable().isUp();
   }

   public boolean isUpActive() {
      return getInputStateDrawable().isUpActive();
   }

   //#enddebug

   public boolean isUpP() {
      return getInputStateDrawable().isUpP();
   }

   public boolean isWheeled() {
      return getInputStateDrawable().isWheeled();
   }

   public boolean isZeroP() {
      return getInputStateDrawable().isZeroP();
   }

   public void requestRepetition() {
      RepeatEvent er = getInputStateDrawable().createRepeatEventLastDevicePress();
      getInputStateDrawable().getInputRequestRoot().requestRepeat(er);
   }

   public void setActionDoneRepaintMessage(IDrawable msg, int timeout, int anchor) {
      getCanvasResultDrawable().setActionDoneRepaintMessage(msg, timeout, anchor);
   }

   public void setActionDoneRepaintMessage(String string, int timeout, int anchor) {
      getCanvasResultDrawable().setActionDoneRepaintMessage(string, timeout, anchor);
   }

   public void setActionString(String string) {
      getCanvasResultDrawable().setActionString(string);
   }

   public void setCanvasResultDrawable(OutputStateCanvasGui sr) {
      this.sr = sr;
   }

   public void setInputStateDrawable(InputStateCanvasGui is) {
      this.is = is;
   }

   public void srActionDone() {
      getCanvasResultDrawable().setActionDone();
   }

   /**
    * Flags config for full repaint and action done.
    * <br>
    * <br>
    * Normally, only one action may be generated by event.
    * <br>
    * However when UP and DOWN are pressed together, they may generated 2 action done on the same loop
    */
   public void srActionDoneRepaint() {
      getCanvasResultDrawable().setActionDoneRepaint();
   }

   public void srActionDoneRepaint(boolean done, boolean repaint) {
      getCanvasResultDrawable().setActionDoneRepaint(done, repaint);
   }

   /**
    * InputConfig result requires the drawable to be repainted in the next paint cycle.
    * <br>
    * <br>
    * When the {@link ViewDrawable} is set, Controller flag tells wether to repaint content or the whole thing.
    * <br>
    * Impact the {@link Controller#screenResult()} method.
    * <br>
    * <br>
    * When {@link IDrawable} does not have the flag {@link ITechDrawable#STATE_17_OPAQUE}, we force {@link IDrawable#draw(GraphicsX)} to draw
    * <li>itself fully
    * <li>only style layers. 
    * <li>nothing
    * <br>
    * The caller is responsible to know. A TableView will know if it has style layers
    * <br>
    * <br>
    * Therefore for efficiency, having an opaque bg layer is a good thing. Note:Swing assume that by default.
    * <br>
    * <br>
    * @param drawable
    */
   public void srActionDoneRepaint(IDrawable drawable) {
      getCanvasResultDrawable().setActionDoneRepaint(drawable);
   }

   /**
    * Special repaint that simply display a message on screen
    * 
    * @param string
    */
   public void srActionDoneRepaintMessage(String string) {
      getCanvasResultDrawable().setActionDoneRepaintMessage(string);
   }

   /**
    * Draws a string message over the virtual canvas. 
    * <br>
    * <br>
    * without repainting component during the next painting cycle.
    * Message will not be repainted at next repaint 
    * Optimally, it should be called when processing a user key event
    * Method must be called from the User Event Thread. 
    *     * A non zero time out will display the message for x milliseconds
    * A Message repaint will be generated and only the message will erased 
    * and background is repainted.
    * Performance will depend on the Paint Mode
    * @param str
    */
   public void srActionDoneRepaintMessage(String string, int timeout) {
      getCanvasResultDrawable().setActionDoneRepaintMessage(string, timeout);
   }

   /**
    * Special repaint on a {@link IDrawable} with an array of integer as parameter.
    * If no other repaint are made that will invalidate this one, when repainted,
    * {@link IDrawable} will check special flag and fetch integer array it stored
    * @param is
    */
   public void srActionDoneRepaintSpecial(IDrawable d) {
      getCanvasResultDrawable().setActionDoneRepaintSpecial(d);
   }

   public void srActionRepaint(IDrawable drawable) {
      getCanvasResultDrawable().setActionDoneRepaint(drawable);
   }

   public void srMenuRepaint() {
      getCanvasResultDrawable().srMenuRepaint();
   }

   /**
    * Force a full flush of all cache
    */
   public void srRenewLayout() {
      getCanvasResultDrawable().srRenewLayout();
   }

   public void toCheckPointer(Drawable drawable) {
      if (d != null) {
         throw new IllegalStateException("Calling several times");
      }
      d = drawable;
   }

   //#mdebug
   public IDLog toDLog() {
      return toStringGetUCtx().toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "InputConfig");
      toStringPrivate(dc);
   }

   //#enddebug

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "InputConfig");
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return gc.getUC();
   }

   public String toStringMod() {
      return ToStringStaticCoreUi.toStringMod(getInputStateDrawable().getMode());
   }
   //#enddebug

   private void toStringPrivate(Dctx dc) {

   }

}
