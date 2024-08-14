package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.core.src4.event.IEventBus;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.cmd.src4.input.FocusDeviceUser;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdsCmd;
import pasa.cbentley.framework.cmd.src4.interfaces.ICommandable;
import pasa.cbentley.framework.cmd.src4.interfaces.ITechCmd;
import pasa.cbentley.framework.core.ui.src4.input.InputState;
import pasa.cbentley.framework.core.ui.src4.tech.IInput;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceGui;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IEventsGui;
import pasa.cbentley.framework.gui.src4.ctx.ObjectGC;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.exec.OutputStateCanvasGui;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.nav.TopologyTBLRNav;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.utils.DrawableArrays;
import pasa.cbentley.framework.gui.src4.utils.DrawableUtilz;

/**
 * Manages the focus inside a {@link CanvasAppliInputGui}
 * 
 * Focus owners:
 * <li> User1 Mouse Focus
 * <li> User2 Mouse Focus
 * <li> User1 Keyboard1 Focus
 * <li> User1 Keyboard2 Focus
 * 
 * <p>
 *  There are 2 main input methods. 
 *  <li>Pointer/Touch 
 *  <li>Keyboard.
 *  For each method, there is a focused {@link IDrawable} associated.
 * </p>
 *  
 *  Supporting of directional navigation in your application is also important in ensuring that your application is accessible to users who do not navigate using visual cues. 
 *  <br>
 *  <br>
 *  <br>
 *  <br>
 *  Like in Android, you can define the navigational topology.
 *  <br> This create TBLR relationships between IDrawables
 *  <br>
 *  <br>
 *  
 *  Android does it like this
 *  
 *  Users can also navigate your app using the arrow keys on a keyboard (the behavior is the same as when navigating with a D-pad or trackball). The system provides a best-guess as to which view should be given focus in a given direction based on the layout of the views on screen. Sometimes, however, the system might guess wrong.

If the system does not pass focus to the appropriate view when navigating in a given direction, specify which view should receive focus with the following attributes:

    android:nextFocusUp
    android:nextFocusDown
    android:nextFocusLeft
    android:nextFocusRight

Each attribute designates the next view to receive focus when the user navigates in that direction, as specified by the view ID. For example:


 * @author Charles Bentley
 *
 */
public class FocusCtrl extends ObjectGC implements IStringable {

   private BusEvent      focusChange;

   /**
    * {@link Drawable} who first respond to key events and whose {@link CmdCtx} is active.
    * <br>
    * <br>
    * After {@link IDrawable}, it is the first to recieve the {@link IDrawable#manageKeyInput(ExecutionContextCanvasGui)} method call.
    * <br> 
    * <br>
    * TODO when a Drawable gets the focus, it may send its Help ID to display.
    * <br>
    * <br>
    * Can be null.
    * <br>
    * <br>
    * 
    * T9 is currently off. Press Enter to turn it on. 
    * Help commands takes into account the current focus hierarchy.
    * Press # key and then * to learn more about T9.
    * T9 is linked to a StringDrawable. The user may want to know more about this specific instance. Max letter for instance.
    * <br>
    * <br>
    * Specific IDs may be user learned
    */
   private IDrawable     itemInKeyFocus;

   /**
    * Items to first recieve Menu command activation events.
    * TODO menu focus is relative to the caller..
    * on which keyboard focus does it act? It acts on the keyboard
    * focus that the pointer would activate with a press
    */
   private IDrawable     itemInMenuFocus;

   /**
    * {@link IDrawable} on which the pointer is currently on.
    * <br>
    * <br>
    * Hierarchy can be known using {@link IDrawable#getParent()}
    * <br>
    * Bottom up approach. 
    * <br>
    * <br>
    * Get presses, released first.
    * If null, Top Down from {@link MasterCanvas}.
    * <br>
    * <br>
    * Whenever a topology is added, this field is updated. TODO
    * <br>
    * <br>
    * 
    */
   private IDrawable     itemInPointerPressedFocus = null;

   /**
    * With devices with moving pointers like computer.
    * 
    * This will include Dragged over on 
    * <br>
    * One focus for each pointer ID
    * <br>
    * Such pointers can use their buttons to generate a command
    * without change the PointerPressFocus.
    * <br>
    * User Pointer's middle click modifies something on this focus and teleport
    * pointer to the middle of the pointer Press Focus.
    * <br> 
    */
   private IDrawable[]   itemInPointerOverFocus    = new IDrawable[1];

   /**
    * Stores Drawables in focus for the categories
    * <li> {@link ITechCmd#CTX_CAT_0_KEY}
    * <li> {@link ITechCmd#CTX_CAT_1_POINTER_PRESSED}
    * <li> {@link ITechCmd#CTX_CAT_0_KEY}
    * 
    */
   private IDrawable[]   itemInFocus               = new IDrawable[1];

   private IDrawable[]   devicesFocus              = new IDrawable[1];

   private IDrawable[][] devicesExtraFocus         = new IDrawable[1][];

   private int           ITERATE_FOCUS_KEY;

   private int           keyFocusChangeCount;

   public IDrawable getFocus(int cat) {
      return itemInFocus[cat];
   }

   /**
    * Manages the Keyfocus movement
    */
   private TopologyTBLRNav topologyNav;

   public FocusCtrl(GuiCtx gc) {
      super(gc);
      topologyNav = new TopologyTBLRNav(gc);
      
      IEventBus eventsBusGui = gc.getEventsBusGui();
      focusChange = new BusEvent(gc.getUC(), eventsBusGui, IEventsGui.PID_01_DRAW, IEventsGui.PID_01_DRAW_01_FOCUS_CHANGE);
   }

   private void focusGainPointer(IDrawable d) {
      d.notifyEvent(ITechDrawable.EVENT_08_POINTER_FOCUS_GAIN);
      itemInPointerPressedFocus = d;
   }

   private void focusLostPointer(IDrawable d) {
      d.notifyEvent(ITechDrawable.EVENT_07_POINTER_FOCUS_LOSS);
      itemInPointerPressedFocus = null;
   }

   /**
    * {@link IDrawable} calls this method when an inside Navigation Event allows for focus out.
    * <br>
    * <br>
    * Controller looks which {@link IDrawable} recieve the focus.
    * <br>
    * For Example in a TabbedPane<br>
    * when the focus is on the Top Horizontal TableView, a Down Key generates a Focus Out and give the focus<br>
    * to selected Drawable. <br>
    * When Drawable is not INav, Top Horizontal TableView keeps the INav focus.<br>
    * Otherwise In turn when Drawable recieves an Up Key, check if reach top and then focusOut to the Top Horizontal TableView<br>
    * When a new cell is selected, old pane Drawable is removed from Nav hierarchy and new one is added.
    * <br>
    * For 2 dimensions {@link TableView}, when the * is pressed, Nav commands first go to the next up in the INav hierarchy.
    * <br>
    * <br>
    * It always look in the Nav hierarchy.
    * @param sd
    * @param posTop
    */
   public void focusOut(IDrawable d, int posTop) {
      // TODO Auto-generated method stub

   }

   public void focusRelease() {
      // TODO Auto-generated method stub

   }

   /**
    * The {@link IDrawable} under focus by the owner
    * @param focus
    * @return
    */
   public IDrawable getItemInFocus(FocusDeviceUser focus) {

      return null;
   }

   public IDrawable getItemInKeyFocus() {
      return itemInKeyFocus;
   }

   public IDrawable getItemInPointerFocus() {
      return itemInPointerPressedFocus;
   }

   /**
    * ID of the pointer.
    * 
    * @param id
    * @return
    */
   public IDrawable getItemInPointerFocus(int id) {
      return itemInPointerPressedFocus;
   }

   public int getKeyFocusCount() {
      return keyFocusChangeCount;
   }

   public TopologyTBLRNav getTopologyNav() {
      return topologyNav;
   }

   /**
    * Call back when a Nav method of the leaf {@link IDrawable} has nothing to do and decide to delegate
    * the navigation to the topology. thus knowing it may lose the focus.
    * <br>
    * <br>
    * <li>First check the screen topology
    * If navigation is succesfull, and {@link IDrawable} is a {@link IDrawable}, it will be notified with a {@link ITechDrawable#EVENT_04_KEY_FOCUS_LOSS}.
    * <br>
    * <br>
    * Depending on options, it may only be done on a 
    * <li>fast typed
    * <li>typed, 
    * <li>press no repeat
    * <li>Press and on repeated presses.
    * <br>
    * <br>
    * @param nav
    * @param posLeft
    */
   public void navigateOut(ExecutionContextCanvasGui ec, IDrawable nav, int posLeft) {
      if (posLeft == C.POS_2_LEFT) {
      }
   }

   /**
    * Removes the focus state from the current keyboard focused {@link Drawable} and give it to new one.
    * <br>
    * <br>
    * 
    * <b>Focus transfer</b>:
    * <li> Old Drawable gets the {@link IDrawable#notifyEvent(int)} {@link ITechDrawable#EVENT_04_KEY_FOCUS_LOSS}
    * <li> New Drawable gets the {@link IDrawable#notifyEvent(int)} {@link ITechDrawable#EVENT_03_KEY_FOCUS_GAIN}
    * <li> Register new {@link ICommandable} as the context for key commands.
    * <br>
    * <br>
    * 
    * Is New OffSpring/Sister/Ancestor?
    * <br>
    * <br>
    * Key focus becomes null when parameter is null.
    * <br>
    * <br>
    * @param ec TODO
    * @param newKeyFocusDrawable the {@link Drawable} to give focus {@link ITechDrawable#STYLE_06_FOCUSED_KEY} state to.
    */
   public void newFocusKey(ExecutionContextCanvasGui ec, IDrawable newKeyFocusDrawable) {
      OutputStateCanvasGui os = ec.getCanvasResultDrawable();
      focusChange.clearForReUse();
      focusChange.setParamO1(newKeyFocusDrawable);
      focusChange.setParamO2(itemInKeyFocus);
      keyFocusChangeCount++;
      if (newKeyFocusDrawable == null) {
         if (itemInKeyFocus != null) {
            os.setActionDoneRepaint(itemInKeyFocus);
            //#debug
            os.debugSetActionDoneRepaint("Removed Old Drawable KeyFocus");
            itemInKeyFocus.notifyEvent(ITechDrawable.EVENT_04_KEY_FOCUS_LOSS, focusChange);
            itemInKeyFocus = null;
         }
      } else {
         if (itemInKeyFocus != null && newKeyFocusDrawable != itemInKeyFocus) {
            os.setActionDoneRepaint(itemInKeyFocus);
            itemInKeyFocus.notifyEvent(ITechDrawable.EVENT_04_KEY_FOCUS_LOSS, focusChange);
            itemInKeyFocus = null;
         }
         if (newKeyFocusDrawable != itemInKeyFocus) {
            os.setActionDoneRepaint(newKeyFocusDrawable);
            //#debug
            os.debugSetActionDoneRepaint("#Controller New Drawable KeyFocus");
            newKeyFocusDrawable.notifyEvent(ITechDrawable.EVENT_03_KEY_FOCUS_GAIN, focusChange);
            itemInKeyFocus = newKeyFocusDrawable;
         }
      }
      gc.getCC().setActiveCommandable(itemInKeyFocus, ITechCmd.CTX_CAT_0_KEY);
   }

   /**
    * Drawable is focus by the pointer in {@link InputState}.
    * <br>
    * @param ec TODO
    * @param drawable
    */
   public void newFocusPointerOver(ExecutionContextCanvasGui ec, IDrawable drawable) {

      gc.getCC().setActiveCommandable(drawable, ITechCmd.CTX_CAT_2_POINTER_OVER);
   }

   /**
    * Hosts that supports Pointer move events, 
    * <br>
    * Bentley framework provides simulated "Move Over" while receiving Dragged events from Host.
    * <br>
    * 
    * <br>
    * 
    * In the process of the {@link ICmdsCmd#CMD_24_SELECT}, the Drawable
    * hierarchy selects a new PointerOver Drawable.
    * <br>
    * Manage {@link ITechDrawable#STYLE_07_FOCUSED_POINTER} flag between old {@link Drawable} and new {@link Drawable}
    * <br>
    * <br>
    * Sets the action done in all cases
    * <br>
    * <br>
    * @param ec TODO
    * @param drawable when null removes the focus to all focused {@link IDrawable}.
    */
   public void newFocusPointerPress(ExecutionContextCanvasGui ec, IDrawable drawable) {
      InputConfig ic = ec.getInputConfig();
      if (drawable == null) {
         if (itemInPointerPressedFocus != null) {
            ic.srActionDoneRepaint(itemInPointerPressedFocus);

            //#debug
            toDLog().pFlow("Removed [Null] PointerFocus", itemInPointerPressedFocus, FocusCtrl.class, "newFocusPointerPress", LVL_05_FINE, true);

            focusLostPointer(itemInPointerPressedFocus);
         }
      } else {
         //#debug
         toDLog().pFlow("", drawable, FocusCtrl.class, "newFocusPointerPress", LVL_05_FINE, true);

         if (drawable != itemInPointerPressedFocus && DrawableUtilz.isInside(ec, drawable)) {
            if (itemInPointerPressedFocus != null && drawable != itemInPointerPressedFocus) {
               ic.srActionDoneRepaint(itemInPointerPressedFocus);

               //#debug
               toDLog().pFlow("Removed PointerFocus", itemInPointerPressedFocus, FocusCtrl.class, "newFocusPointerPress", LVL_05_FINE, true);

               focusLostPointer(itemInPointerPressedFocus);
            }
            if (drawable != itemInPointerPressedFocus) {
               ic.srActionDoneRepaint(drawable);

               //#debug
               toDLog().pFlow("Set PointerFocus for", drawable, FocusCtrl.class, "newFocusPointerPress", LVL_05_FINE, true);

               focusGainPointer(drawable);
            }
         }
      }
      gc.getCC().setActiveCommandable(itemInPointerPressedFocus, ITechCmd.CTX_CAT_1_POINTER_PRESSED);
      //action flag is set. this must be done
      ic.srActionDone();
   }

   /**
    * Settings the states call the setState Method.
    * The drawable sets the state.
    * But if the state impacts other things??? Pressing
    * on a Drawable in a TableView will also set the state on another.
    * <br>
    * @param ec TODO
    * @param d
    */
   public void setPointerStates(ExecutionContextCanvasGui ec, IDrawable d) {
      InputConfig ic = ec.getInputConfig();
      if (ic.isMoved()) {
      } else if (ic.isPressed()) {
         d.setStateStyle(ITechDrawable.STYLE_08_PRESSED, true);
      } else if (ic.isReleased()) {
         d.setStateStyle(ITechDrawable.STYLE_08_PRESSED, false);
         d.setStateStyle(ITechDrawable.STYLE_09_DRAGGED, false);
         //the release is done outside the drawable but it got called because 
         //it was the draggable. so it gets a chance to set state
         if (!DrawableUtilz.isInside(ec, d)) {
            ic.getCanvasResultDrawable().setOutsideDrawable(true);
         }
      } else if (ic.isDraggedPointer0Button0()) {
         d.setStateStyle(ITechDrawable.STYLE_09_DRAGGED, true);
      } else if (ic.isWheeled()) {

      }
      ic.getCanvasResultDrawable().setDrawableStates();

   }

   /**
    * Focus for what and who?
    * 
    * <li> {@link ITechCmd#CTX_CAT_0_KEY}
    * <li> {@link ITechCmd#CTX_CAT_1_POINTER_PRESSED}
    * <li> {@link ITechCmd#CTX_CAT_2_POINTER_OVER}
    * <li> {@link ITechCmd#CTX_CAT_3_POINTER2_PRESSED}
    * <li> {@link ITechCmd#CTX_CAT_4_POINTER2_OVER}
    * <li> {@link ITechCmd#CTX_CAT_5_POINTER3_PRESSED}
    * 
    * @param d
    * @param ctxCategory
    */
   public void setNewFocus(IDrawable d, int ctxCategory) {
      if (ctxCategory >= itemInFocus.length) {
         itemInFocus = DrawableArrays.ensureCapacity(itemInFocus, ctxCategory);
      }

      itemInFocus[ctxCategory] = d;
      gc.getCC().setActiveCommandable(d, ctxCategory);
   }

   public void setNewFocus(InputState is, IDrawable d, FocusDeviceUser focusType) {
      int deviceID = focusType.getDeviceID();
      int deviceType = focusType.getDeviceType();
      //for the most used context we check them. most common case if deviceID 0 for pointer and keyboard
      IDrawable old = null;
      if (deviceID == 0) {
         if (deviceType == IInput.DEVICE_0_KEYBOARD) {
            old = itemInKeyFocus;
            itemInKeyFocus = d;
         } else if (deviceType == IInput.DEVICE_1_MOUSE) {
            old = itemInPointerPressedFocus;
            itemInPointerPressedFocus = d;
         } else {
            old = devicesFocus[deviceType];
            devicesFocus[deviceType] = d;
         }
      } else {
         old = devicesExtraFocus[deviceType][deviceID];
         devicesExtraFocus[deviceType][deviceID] = d;
      }
      EventCmdNodeDrawable ced = new EventCmdNodeDrawable(gc, d, old, focusType);
      //queue an event Ctx Change 
      is.queuePost(ced);

   }

   /**
    * Called just before the paint
    * In the context of the command, hide the drawable.
    * Gives the key focus to newFocus or the parent.
    * <br>
    * What about the focus of other devices?
    * Do we update it? Pointer Focus are updatable based on X,Y positions.
    * <br>
    * Gamepad focus<br>
    * If Gamepad focus was on the Drawable that was hidden?
    * <br>
    * The focus is given to the new Drawable.
    * <br>
    * <li> When closing a Drawable and its {@link CmdCtx}, other Devices focus on that hierarchy
    * are updated
    * <li> When opening a Drawable, it may hides other Drawables. Devices focus are updated
    * Device may lose its current focus, saved in history
    * <br>
    * <br>
    * When a drawable is hidden
    * <li> Key Focus is given to the Drawable below.
    * <br>
    * The Focus must go back to where it was previously before it was gone.
    * <br>
    * @param cd
    * @param nestedMenus
    * @param returns the new focus key. must be painted
    */
   public IDrawable drawableHide(CmdInstanceGui cd, IDrawable d, IDrawable newKeyFocus) {
      //first remove drawable from the topology
      TopologyDLayer topo = d.getVC().getTopo();
      topo.removeDrawable(d);

      //TODO save view state? of the removed drawable?

      IDrawable next = newKeyFocus;
      if (next == null) {
         next = d.getParent();
      }
      if (next == null) {
         //take highest in top
         next = topo.getTop();
      }

      if (newKeyFocus == null) {
         newKeyFocus = d.getParent();
         if (newKeyFocus == null) {
            newKeyFocus = topo.getTop();
            if (newKeyFocus == null) {
               newKeyFocus = gc.getCanvasGCRoot().getRootDrawable();
            }
         }
      }

      setNewFocus(newKeyFocus, ITechCmd.CTX_CAT_0_KEY);
      return newKeyFocus;
   }

   /**
    * Shows Drawable
    * @param drawExCtx
    * @param d
    */
   public void drawableShow(ExecutionContextCanvasGui drawExCtx, IDrawable d) {
      setNewFocus(d, ITechCmd.CTX_CAT_0_KEY);
      d.getVC().getTopo().addDLayer(d, ITechCanvasDrawable.SHOW_TYPE_1_OVER_TOP);
   }

   //   /**
   //    * Event goes up to the top and down again.
   //    * <br>
   //    * So a Left may be ignored on its way up, but on its way down, uses for an Around the Clock Move.
   //    * <br>
   //    * <br>
   //    * Asks current {@link IDrawable} in key focus to process navigation event.
   //    * If not processed in first swipe, ask parent container. if not processed by parent,
   //    * parent of parent (tab pane containing tables and cells with)
   //    * 
   //    * {@link TopologyTBLRNav}
   //    * <br>
   //    * <li> {@link IDrawable#BE}
   //    */
   //   private void processNav(ExecutionContextCanvasGui ec) {
   //      if (itemInKeyFocus == null) {
   //         return;
   //      }
   //      //since most of the time we will have a navigation, first check if InputConfig is doing a navigation event.
   //      processNav(ic, itemInKeyFocus);
   //      if (!ic.isActionDone()) {
   //         if (ic.isOrderPressed(TrigCodes.KEY_POUND, TrigCodes.KEY_FIRE)) {
   //            //gives key focus to parent
   //            if (itemInKeyFocus.getParent() != null) {
   //               newFocusKey(ic, itemInKeyFocus.getParent());
   //               //#debug
   //               ic.debugSrActionDoneRepaint("Focus Up");
   //            }
   //         } else if (ic.isFireP()) {
   //            //go down if possible only valid actual navigation
   //            itemInKeyFocus.navigateSelect(ic);
   //            if (ic.isActionDone()) {
   //               //#debug
   //               ic.debugSrActionDoneRepaint("Focus Down");
   //            }
   //            return;
   //         }
   //      }
   //   }
   //
   //   /**
   //    * Recursively work bottom up the chain of {@link IDrawable} until action is taken or the chain is ended. 
   //    * <br>
   //    * <br>
   //    * The chain is ended when {@link IDrawable#getParent()} returns null.
   //    * <br>
   //    * Create a Nav command
   //    * <br>
   //    * @param ic
   //    * @param d
   //    */
   //   private void processNav(ExecutionContextCanvasGui ec, IDrawable d) {
   //      processNavigationEvent(ic, d);
   //      if (!ic.isActionDone() && d.getParent() != null) {
   //         ic.is.setNavProcessParent(d);
   //         processNav(ic, d.getParent());
   //      }
   //   }
   //
   //   /**
   //    * Conditions for Navigational commands to fire:
   //    * <br>
   //    * <li>Navigation flag {@link IDrawable#getNavFlag()}
   //    * <li>Navigation keys in {@link InputConfig}. Pressed keys are only navigational.
   //    * <br>
   //    * <br>
   //    * Calls
   //    * <li>{@link IDrawable#navigateDown(InputConfig)}
   //    * <li>{@link IDrawable#navigateUp(InputConfig)}
   //    * <li>{@link IDrawable#navigateLeft(InputConfig)}
   //    * <li>{@link IDrawable#navigateRight(InputConfig)}
   //    * <li>{@link IDrawable#navigateSelect(InputConfig)}
   //    * <br>
   //    * <br>
   //    * TODO how do you deal with repeating navigation on a per component basis?
   //    * INav tech. that provides all those options
   //    * <br>
   //    * <br>
   //    * @param ic
   //    * @param nav {@link IDrawable} against which to check {@link InputConfig} for navigation commands.
   //    */
   //   private void processNavigationEvent(ExecutionContextCanvasGui ec, IDrawable nav) {
   //      if (nav == null) {
   //         return;
   //      }
   //      if (ic.isWheeled()) {
   //         //check if special key to change the default axis of the wheel movement
   //         if (ic.getIdKeyBut() == TrigCodes.POINTER_10_WHEEL_UP) {
   //            nav.navigateUp(ic);
   //         } else {
   //            nav.navigateDown(ic);
   //         }
   //         if (!ic.isActionDone()) {
   //            if (ic.getIdKeyBut() == TrigCodes.POINTER_10_WHEEL_UP) {
   //               nav.navigateLeft(ic);
   //            } else {
   //               nav.navigateRight(ic);
   //            }
   //         }
   //         return;
   //      }
   //      //parametrize this as TechParam
   //      int key = ic.is.idKeyBut;
   //      if (ic.is.pressedKeyCounter == 1) {
   //         if (key == TrigCodes.KEY_UP && nav.hasBehavior(ITechDrawable.BEHAVIOR_26_NAV_VERTICAL)) {
   //            if (nav.hasStateNav(ITechDrawable.NAV_05_OVERRIDE_UP)) {
   //               topologyNav.navigateOutLeft(ic, nav);
   //            } else {
   //               nav.navigateUp(ic);
   //            }
   //         } else if (key == TrigCodes.KEY_DOWN && nav.hasBehavior(ITechDrawable.BEHAVIOR_26_NAV_VERTICAL)) {
   //            nav.navigateDown(ic);
   //         } else if (key == TrigCodes.KEY_LEFT && nav.hasBehavior(ITechDrawable.BEHAVIOR_27_NAV_HORIZONTAL)) {
   //            nav.navigateLeft(ic);
   //         } else if (key == TrigCodes.KEY_RIGHT && nav.hasBehavior(ITechDrawable.BEHAVIOR_27_NAV_HORIZONTAL)) {
   //            nav.navigateRight(ic);
   //         } else if (key == TrigCodes.KEY_FIRE && nav.hasBehavior(ITechDrawable.BEHAVIOR_28_NAV_SELECTABLE)) {
   //            nav.navigateSelect(ic);
   //         }
   //      } else {
   //         //case when Up/Down + Left/Right are pressed together.
   //         //we want both to register
   //         //invalidate if a pressed key is not
   //         if (!ic.isOnlyDirectionalPadPressed())
   //            return;
   //         if (nav.hasBehavior(ITechDrawable.BEHAVIOR_26_NAV_VERTICAL)) {
   //            if (ic.isPressed(TrigCodes.KEY_UP)) {
   //               nav.navigateUp(ic);
   //            }
   //            if (ic.isPressed(TrigCodes.KEY_DOWN)) {
   //               nav.navigateDown(ic);
   //            }
   //         }
   //         if (nav.hasBehavior(ITechDrawable.BEHAVIOR_27_NAV_HORIZONTAL)) {
   //            if (ic.isPressed(TrigCodes.KEY_LEFT)) {
   //               nav.navigateLeft(ic);
   //            }
   //            if (ic.isPressed(TrigCodes.KEY_RIGHT)) {
   //               nav.navigateRight(ic);
   //            }
   //         }
   //         if (nav.hasBehavior(ITechDrawable.BEHAVIOR_28_NAV_SELECTABLE)) {
   //            if (key == TrigCodes.KEY_FIRE) {
   //               nav.navigateSelect(ic);
   //            }
   //         }
   //      }
   //      if (ic.isActionDone()) {
   //         ic.is.getInputReg().setContinuous(true, 400, 100);
   //      }
   //   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, FocusCtrl.class, 700);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, FocusCtrl.class, 700);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }
   //#enddebug

}
