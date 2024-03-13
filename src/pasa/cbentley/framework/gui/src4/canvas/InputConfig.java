package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.coreui.src4.ctx.ToStringStaticCoreUi;
import pasa.cbentley.framework.coreui.src4.event.RepeatEvent;
import pasa.cbentley.framework.coreui.src4.exec.ExecutionContext;
import pasa.cbentley.framework.coreui.src4.interfaces.IActionFeedback;
import pasa.cbentley.framework.coreui.src4.tech.ITechCodes;
import pasa.cbentley.framework.coreui.src4.tech.IInput;
import pasa.cbentley.framework.coreui.src4.tech.ITechInputFeedback;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceDrawable;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.ViewDrawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.utils.DrawableUtilz;
import pasa.cbentley.framework.input.src4.CanvasResult;
import pasa.cbentley.framework.input.src4.InputState;

/**
 * 
 * Aggregates for the View module
 * <li> {@link InputStateDrawable} is a {@link InputState}
 * <li> {@link CmdInstance}
 * <li> {@link CanvasResultDrawable} is a {@link CanvasResult}
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
public class InputConfig implements IStringable, IInput, IActionFeedback {

   private CmdInstanceDrawable ci;

   //#debug
   private IDrawable           d;

   private ExecutionContext        ex;

   private GuiCtx              gc;

   public InputStateDrawable   is;

   public CanvasResultDrawable sr;

   protected final CanvasAppliInputGui  canvas;

   public InputConfig(GuiCtx gc, CanvasAppliInputGui canvas, InputStateDrawable isd, CanvasResultDrawable srd) {
      this.canvas = canvas;
      is = isd;
      sr = srd;
      this.gc = gc;
   }
   
   public InputStateDrawable getInputState() {
      return is;
   }

   public CmdInstanceDrawable createCmd(int cmdid) {
      CmdInstanceDrawable cd = new CmdInstanceDrawable(gc, cmdid);
      cd.setFeedback(this);
      return cd;
   }

   public GuiCtx getGC() {
      return gc;
   }

   public CanvasAppliInputGui getCanvas() {
      return canvas;
   }

   public void actionDone() {
      actionDone(null, ITechInputFeedback.FLAG_01_ACTION_DONE);
   }

   public void actionDone(Object o, int type) {
      sr.actionDone(o, type);
   }

   public void cmdActionOnDrawable(IDrawable d) {
      if (ci != null) {
         ci.actionDone();
      }
      sr.setActionDoneRepaint(d);
   }

   public CmdInstanceDrawable getCmdInstance() {
      return ci;
   }

   public int getDraggedDiffX() {
      return is.getDraggedDiffX();
   }

   public int getDraggedDiffY() {
      return is.getDraggedDiffY();
   }

   /**
    * The amount of X pixels dragged since the last press event.
    * <br>
    * <br>
    * @return value is positive or negative depending on direction
    */
   public int getDraggedVectorX() {
      return is.getDraggedVectorX();
   }

   /**
    * The amount of Y pixels dragged since the last press event.
    * <br> value is positive or negative depending on direction
    * @return
    */
   public int getDraggedVectorY() {
      return is.getDraggedVectorY();
   }

   public ExecutionContext getEx() {
      return ex;
   }

   public int getIdKeyBut() {
      return is.getKeyCode();
   }

   /**
    * 
    * @param last
    * @return
    */
   public int getKeyModSequence(int last) {
      return is.getKeyModSequence(last);
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
      return is.getKeyNum();
   }

   /**
    * Number of currently pressed keys.
    * @return
    */
   public int getPressedKeyCounter() {
      return is.getPressedKeyNum();
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

   public void debugSrActionDoneRepaint(String actionStr) {
      sr.debugSetActionDoneRepaint(actionStr);
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
      return sr.isActionDone();
   }

   public boolean isActive(int key) {
      return is.isActive(key);
   }

   public boolean isCancel() {
      return is.isCancel();
   }

   public boolean isCancelP() {
      return is.isCancelP();
   }

   public boolean isDown() {
      return is.isDown();
   }

   public boolean isDownActive() {
      return is.isDownActive();
   }

   public boolean isDownP() {
      return is.isDownP();
   }

   /**
    * Check whether the drawable is dragged in a gesture
    * @param viewedDrawable
    * @return
    */
   public boolean isDragged(IDrawable viewedDrawable) {
      return is.isDragged(viewedDrawable);
   }

   public boolean isDraggedDragged() {
      return is.isDraggedDragged();
   }

   /**
    * TODO generic marge when to release the event.
    * See
    * <li>pixel distance
    * <li>out of the drawable
    * @return
    */
   public boolean isDraggedPointer0Button0() {
      return is.isDragged();
   }

   /**
    * Is the key fast typed.
    * <br>
    * Problem is about the long repaints. the fast is not registered.
    * @param key
    * @return
    */
   public boolean isFastTyped(int key) {
      return is.isLastKeyFastTyped(key);
   }

   public boolean isFire() {
      return is.isFire();
   }

   public boolean isFireP() {
      return is.isFireP();
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
      return is.isGestured();
   }

   public boolean isInside(IDrawable d) {
      return DrawableUtilz.isInside(this, d);
   }

   /**
    * True if current state of inputconfig has a KeyType event. KeyType is actually a case of
    * the release event
    * @return
    */
   public boolean isKeyTyped() {
      return is.isKeyTyped();
   }

   public boolean isKeyTyped(int key) {
      return is.isKeyTyped(key);
   }

   public boolean isKeyTypedAlone(int key) {
      return is.isKeyTypedAlone(key);
   }

   public boolean isLeft() {
      return is.isLeft();
   }

   public boolean isLeftActive() {
      return is.isLeftActive();
   }

   public boolean isLeftP() {
      return is.isLeftP();
   }

   public boolean isMinusP() {
      return is.isMinusP();
   }

   public boolean isMoved() {
      return is.isModMoved();
   }

   public boolean isNavKey() {
      return is.isNavKey();
   }

   public boolean isPhotoP() {
      return is.isPhotoP();
   }

   public boolean isPinched() {
      return is.isPinched();
   }

   public boolean isPlusP() {
      return is.isPlusP();
   }

   public boolean isPointerEventAccepted(IDrawable d) {
      if (!this.isActionDone() && d != null && DrawableUtilz.isInside(this, d)) {
         return true;
      }
      return false;
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
      return is.isPoundP();
   }

   /**
    * Is Pressed the last Modifier in the chain of events. For keys and pointer events.
    * @return
    */
   public boolean isPressed() {
      return is.isModPressed();
   }

   /**
    * A double press
    * @return
    */
   public boolean isPressedDouble() {
      return is.isPressedDouble();
   }

   /**
    * Is the key pressed, among all the keys currently pressed
    * <br>
    * <br>
    * @param key value from {@link ITechCodes#KEY_BACK}, {@link ITechCodes#KEY_NUM3}
    * @return
    */
   public boolean isPressedKeyboard0(int key) {
      return is.isPressedKeyboard0(key);
   }

   /**
    * A double click
    * @return
    */
   public boolean isPressedReleasedDouble() {
      return is.isPressedReleasedDouble();
   }

   /**
    * Check if the release event is not occuring further than the given distance for both x and y
    * @param value
    * @return
    */
   public boolean isReleaseAround(int value) {
      return is.isReleaseAround(value);
   }

   /**
    * Does not check if the release is inside
    * <br>
    * 
    * @return
    */
   public boolean isReleased() {
      return is.isModReleased();
   }

   public boolean isRight() {
      return is.isRight();
   }

   public boolean isRightActive() {
      return is.isRightActive();
   }

   public boolean isRightP() {
      return isPressedKeyboard0(ITechCodes.KEY_RIGHT);
   }

   public boolean isSequencedPressed(int key1, int key2) {
      return is.isSequencedPressedKeyboard0(key1, key2);
   }

   public boolean isSequencedTyped(int key1, int key2) {
      return is.isSequencedTypedKeyboard0(key1, key2);
   }

   /**
    * Is Soft key pressed 
    * @return
    */
   public boolean isSoftLeftP() {
      return is.isSoftLeftP();
   }

   public boolean isSoftRightP() {
      return is.isSoftRightP();
   }

   /**
    * Is the key * currently pressed
    * @return
    */
   public boolean isStarP() {
      return is.isStarP();
   }

   /**
    * True when * is press along side with that key
    * @param key
    * @return
    */
   public boolean isTriggerStar(int key) {
      return is.isTriggerStar(key);
   }

   public boolean isUp() {
      return is.isUp();
   }

   public boolean isUpActive() {
      return is.isUpActive();
   }

   public boolean isUpP() {
      return is.isUpP();
   }

   public boolean isWheeled() {
      return is.isWheeled();
   }

   //#enddebug

   public boolean isZeroP() {
      return is.isZeroP();
   }

   public void requestRepetition() {
      RepeatEvent er = is.createRepeatEventLastDevicePress();
      is.getInputRequestRoot().requestRepetition(er);
   }

   public void setActionDoneRepaintMessage(IDrawable msg, int timeout, int anchor) {
      sr.setActionDoneRepaintMessage(msg, timeout, anchor);
   }

   public void setActionDoneRepaintMessage(String string, int timeout, int anchor) {
      sr.setActionDoneRepaintMessage(string, timeout, anchor);
   }

   public void setActionString(String string) {
      sr.setActionString(string);
   }

   public void srActionDone() {
      sr.setActionDone();
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
      sr.setActionDoneRepaint();
   }

   public void srActionDoneRepaint(boolean done, boolean repaint) {
      sr.setActionDoneRepaint(done, repaint);
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
      sr.setActionDoneRepaint(drawable);
   }

   /**
    * Special repaint that simply display a message on screen
    * 
    * @param string
    */
   public void srActionDoneRepaintMessage(String string) {
      sr.setActionDoneRepaintMessage(string);
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
      sr.setActionDoneRepaintMessage(string, timeout);
   }

   /**
    * Special repaint on a {@link IDrawable} with an array of integer as parameter.
    * If no other repaint are made that will invalidate this one, when repainted,
    * {@link IDrawable} will check special flag and fetch integer array it stored
    * @param is
    */
   public void srActionDoneRepaintSpecial(IDrawable d) {
      sr.setActionDoneRepaintSpecial(d);
   }

   public void srActionRepaint(IDrawable drawable) {
      sr.setActionDoneRepaint(drawable);
   }

   public void srMenuRepaint() {
      sr.srMenuRepaint();
   }

   /**
    * Force a full flush of all cache
    */
   public void srRenewLayout() {
      sr.srRenewLayout();
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
      return ToStringStaticCoreUi.toStringMod(is.getMode());
   }
   //#enddebug

   //#enddebug

   private void toStringPrivate(Dctx dc) {

   }

}
