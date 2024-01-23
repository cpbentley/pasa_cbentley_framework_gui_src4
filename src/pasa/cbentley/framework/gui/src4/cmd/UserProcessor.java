package pasa.cbentley.framework.gui.src4.cmd;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.structs.IntToObjects;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.engine.CmdNode;
import pasa.cbentley.framework.cmd.src4.engine.CmdSearch;
import pasa.cbentley.framework.cmd.src4.engine.FocusType;
import pasa.cbentley.framework.cmd.src4.input.FocusTypeInput;
import pasa.cbentley.framework.cmd.src4.input.NodeDevice;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdListener;
import pasa.cbentley.framework.cmd.src4.interfaces.IEventCmds;
import pasa.cbentley.framework.cmd.src4.interfaces.ITriggerUnit;
import pasa.cbentley.framework.cmd.src4.interfaces.ITriggers;
import pasa.cbentley.framework.cmd.src4.trigger.CmdTrigger;
import pasa.cbentley.framework.cmd.src4.trigger.CmdTriggerLive;
import pasa.cbentley.framework.cmd.src4.trigger.EventCmdNode;
import pasa.cbentley.framework.cmd.src4.trigger.TrigUnitInteger;
import pasa.cbentley.framework.coreui.src4.event.BEvent;
import pasa.cbentley.framework.coreui.src4.event.CtxChangeEvent;
import pasa.cbentley.framework.coreui.src4.event.DeviceEvent;
import pasa.cbentley.framework.coreui.src4.event.DeviceEventGroup;
import pasa.cbentley.framework.coreui.src4.event.EventKey;
import pasa.cbentley.framework.coreui.src4.event.GestureEvent;
import pasa.cbentley.framework.coreui.src4.event.GesturePointer;
import pasa.cbentley.framework.coreui.src4.event.RepeatEvent;
import pasa.cbentley.framework.coreui.src4.event.SenseEvent;
import pasa.cbentley.framework.coreui.src4.exec.ExecutionContext;
import pasa.cbentley.framework.coreui.src4.interfaces.ITechSenses;
import pasa.cbentley.framework.coreui.src4.tech.IInput;
import pasa.cbentley.framework.coreui.src4.tech.ITechGestures;
import pasa.cbentley.framework.input.src4.InputState;
import pasa.cbentley.framework.input.src4.KeyEventListed;

/**
 * 
 * @author Charles Bentley
 *
 */
public class UserProcessor implements ITriggers, IStringable {

   private IntToObjects   allContexts;

   private CmdCtx         cc;

   private Object[]       keys;

   private int[]          mapIDs;

   private int[]          mapKeyboardIDs;

   /**
    * Live triggers with press/release pattern currently pressed down.
    * <br>
    * They are used as a base for the current event matching on the {@link FocusTypeInput}.
    * <br>
    * When the mouse is moving over a new {@link CmdNode}, this generates a {@link EventCmdNode}.
    * This event alone is matched after a possible pattern match.
    * pattern + node change
    * <br>
    * <br>
    */
   CmdTriggerLive         patternTrigger;

   /**
    * Id of the person of the {@link UserProcessor}.
    */
   private int            personID = 0;

   private FocusTypeInput primaryFocusGamepad;

   private FocusTypeInput primaryFocusTouchScreen;

   private FocusTypeInput primaryKeyboardContext;

   private FocusTypeInput primaryMouseContext;

   public UserProcessor(CmdCtx cc) {
      this.cc = cc;
      allContexts = new IntToObjects(cc.getUCtx());
      patternTrigger = new CmdTriggerLive(cc);
   }

   /**
    * A command was found in a {@link CmdSearch#searchPattern(CmdTriggerLive)}
    * @param ci
    * @param ft
    * @param exc
    */
   private void foundPerfectCommand(CmdInstance ci, FocusTypeInput ft, ExecutionContext exc) {

   }

   /**
    * Each input device has one {@link CmdSearch}.
    * <br>
    * TODO map deviceID to devicerUserID.
    * <br>
    * Red keyboard
    * <br> 1st Finger
    * <br> 2nd Finger
    * @param userDeviceId
    * @param deviceType
    * @return
    */
   private FocusTypeInput getActiveSearchFocus(DeviceEvent de) {
      int userDeviceId = de.getDeviceID();
      int deviceType = de.getDeviceType();
      if (deviceType == IInput.DEVICE_0_KEYBOARD) {
         if (userDeviceId == 0) {
            if (primaryKeyboardContext == null) {
               primaryKeyboardContext = new FocusTypeInput(IInput.DEVICE_0_KEYBOARD, 0, personID, cc.createNextFocusID());
               allContexts.add(primaryKeyboardContext);
            }
            return primaryKeyboardContext;
         } else {
            //TODO context for secondary keyboard
         }
      } else if (deviceType == IInput.DEVICE_1_MOUSE) {
         if (userDeviceId == 0) {
            if (primaryMouseContext == null) {
               primaryMouseContext = new FocusTypeInput(IInput.DEVICE_1_MOUSE, 0, personID, cc.createNextFocusID());
               allContexts.add(primaryMouseContext);
            }
            return primaryMouseContext;
         }
      } else if (deviceType == IInput.DEVICE_4_SCREEN) {
         //at the level of the user.. we don't have deviceID but deviceName (1st finger, 2nd finger)
         //should the focus context of other fingers matter? you canno
         if (primaryFocusTouchScreen == null) {
            primaryFocusTouchScreen = new FocusTypeInput(IInput.DEVICE_4_SCREEN, 0, personID, cc.createNextFocusID());
         }
         return primaryFocusTouchScreen;
      } else if (deviceType == IInput.DEVICE_2_GAMEPAD) {
         //how are gamepads used by a single player? as one focus. a 2 second gamepad simply provides more keys
         // another user will have its own UserProcessor
         if (primaryFocusGamepad == null) {
            int gamepadID = de.getDeviceID();
            primaryFocusGamepad = new FocusTypeInput(IInput.DEVICE_2_GAMEPAD, gamepadID, personID, cc.createNextFocusID());
         }
         return primaryFocusGamepad;
      }
      throw new IllegalArgumentException();
   }

   /**
    * Converts the {@link BEvent} to a {@link CmdTrigger} unit.
    * <br>
    * Return a raw trigger. Time constraints are not computed here.
    * <li> {@link DeviceEvent} press, release and turned (wheels)
    * <li> Simultaenous events are processed by {@link DeviceEventGroup} which are registered
    * when potential
    * <li> Timing triggers are generated on potential. Example PressReleaseKey Wait2secs
    * <li> {@link NodeDevice} events for {@link CmdNode} changes.
    * <li> {@link GestureEvent} trigger. Atomic fling/AllerRetour/Shake. 
    * <li> For processing continuous events move, pinch
    * <br>
    * This method only converts. It does not make assumptions.
    * <br>
    * Some events are not triggers but continuous feedback.
    * <br>
    * A move after a move does not change the trigger.
    * <br>
    * The {@link CmdNode} search will 
    * @param ic
    * @param is
    * @return
    */
   private CmdTriggerLive getTriggerFromEvent(InputState is, BEvent ev) {
      //what about commands when entering a context?
      //changing context generates an event in the Update Thread
      //this in turn may start context Out context In commands.
      //depending on the active trigger when the context is changed
      //CTRL modifier may change the command.

      CmdTriggerLive ct = new CmdTriggerLive(cc);
      if (is.isTypeDevicePointer()) {
         if (is.isModMoved()) {
            ct.addDevice(0, 0, IInput.MOD_3_MOVED, is.getLastDeviceType());
            return ct;
         }
         int key2 = is.getDeviceID();
         int key1 = is.getKeyCode();
         ct.addDevice(key1, key2, is.getMode(), is.getLastDeviceType());
      } else if (is.isTypeDeviceKeyboard()) {
         int key1 = is.getKeyCode();
         int key2 = is.getDeviceID();
         ct.addDevice(key1, key2, is.getMode(), is.getLastDeviceType());
      } else if (is.isTypeRepeat()) {
         //create a command trigger for a repeat
         RepeatEvent re = is.getRepeatEvent();
         //but first identify the type of repeat
         int type = re.getType();
         if (type == IInput.REPEAT_2_LONG) {
            if (re.isPinging()) {
               //TODO continuous command for visualfeedback?
            } else {
               int num = re.getCount();
               ct.addFunctionLongPress(num);
            }
         }
      } else if (is.isGestured()) {
         //TODO a gesture context uses the pressedContext, not the release context how do we keep it?
         //if it is a call back.. forward to command
         GestureEvent ge = is.getLastGesture();
         int type = ge.getType();
         if (type == ITechGestures.GESTURE_TYPE_2_SWIPE) {
            //in the case of the continuous drag swipe?
            ct.add(0, ITechGestures.GESTURE_TYPE_2_SWIPE, IInput.TYPE_2_GESTURE);
         } else if (type == ITechGestures.GESTURE_TYPE_3_FLING) {
            ct.add(0, ITechGestures.GESTURE_TYPE_3_FLING, IInput.TYPE_2_GESTURE);
         }
      } else if (is.isCtxChange()) {
         CtxChangeEvent cce = (CtxChangeEvent) is.getEventCurrent();
         //translate the event to the Trigger language
         CmdNode d = (CmdNode) cce.getNewCtx();
         if (d != null) {
            int id = d.getNodeID();
            ct.add(id, 0, IInput.TYPE_5_CTX_CHANGE);
         }
         //since we have a live trigger link the event to the trigger
         ct.setLiveEvent(cce);
      } else if (is.isTypeDeviceSensor()) {
         SenseEvent se = is.getSensor();
         int type = se.getSensorType();
         if (type == ITechSenses.GESTURE_TYPE_05_SHAKE) {
            ct.addDevice(0, ITechSenses.GESTURE_TYPE_05_SHAKE, IInput.DEVICE_7_SENSOR);
         }
      }
      //create a link trigger.. TODO i.e what is the relation between this one
      //and the previous trigger? timestamp
      return ct;
   }

   /**
    * Null if event cannot be mapped to a trigger
    * <br>
    * <br>
    * <br>
    * @param is
    * @param ev
    * @return
    */
   private CmdTriggerLive getTriggerUnitFromEvent(InputState is, BEvent ev) {
      if (ev instanceof DeviceEvent) {
         DeviceEvent de = (DeviceEvent) ev;
         return getTriggerFromEvent(is, ev);
      }
      return null;
   }

   /**
    * Add Event if at least one potential command is detected.
    * @param dex
    * @param is
    * @param cs
    */
   private void managePotentials(ExecutionContext dex, InputState is, CmdSearch cs) {
      IntToObjects potentials = cs.getPotentials();
      if (potentials.nextempty != 0) {
         //list potential cmds by sending an event? GUI component may
         BEvent me = new BEvent(cc.getCUC());
         me.setParamO1(cs);
         dex.addEvent(IEventCmds.EVENT_5_TRIGGER_SEARCH, this); //will be fired at the end
         //for each potential trigger. check if there is a time constraint
         CmdTriggerLive ctLive = cs.getLiveTrigger();
         //with a timing we must have a DeviceEvent TODO here otherwise Trigger is badly formed
         BEvent be = is.getEventCurrent();

         //iterate over match triggers
         for (int i = 0; i < potentials.nextempty; i++) {
            CmdTrigger ctPot = (CmdTrigger) potentials.getObjectAtIndex(i);
            //we need to lookup the next ... move this code in the CmdSearch just after adding the potential 
            TrigUnitInteger tuNext = ctPot.getUnitAfter(ctLive);
            findNextListeners(is, ctLive, be, tuNext);

         }
         //#debug
         toDLog().pCmd("Potential Commands", cs, UserProcessor.class, "process");
      }
   }

   /**
    * <li> Main keyboard
    * <li> Black keyboard
    * @param deviceNameID
    * @return
    */
   private int mapKeyboardName(int deviceNameID) {
      return mapKeyboardIDs[deviceNameID];
   }

   /**
    * <li>Black Mouse to pointer 1
    * <li>Finger 1 on screen A to pointerID
    * <br>
    * @param pointerNameID
    * @return
    */
   private int mapPointerName(int pointerNameID) {
      return mapIDs[pointerNameID];
   }

   /**
    * Context Focus Category id.
    * 
    * a pointer updates the keyboard context with a nav command linked on the pointer focus. All pointer events are relative to that context.
    * click/gesturing
    * <br>
    * <br>
    * <li>every device focus of a single user provides potential trigger links.
    * <li> every device has its own focus and context. registered with {@link CmdCtx#createNextFocusID()}
    * <li> for gaming speed or other reasons, gamepads devices can go into exclusive mode to their context
    * and not for as modifiers
    * <li>
    * 
    * <br>
    * <br>
    * TODO when undoing a command, does it undo scrolling commands / move commands? Yes. There
    * are several granularity. Document Edit commands will have undo Edit
    * @param dex
    * @param is
    * @param ctxcat
    * @return
    */
   public int processEvent(ExecutionContext dex, InputState is) {
      //when we have a keyboard event.. target the keyboard focus trigger history
      BEvent lastEvent = is.getEventCurrent();
      //creates a trigger unit
      //CmdTrigger ct = getTriggerFromEvent(is, lastEvent); //easy job is to create a CmdTrigger unit

      if (lastEvent instanceof NodeDevice) {
         NodeDevice cd = (NodeDevice) lastEvent;
         //which focus got changed? when the navigation command moves the focus
         int focusID = cd.getFocus().getFocusID();
         CmdSearch cs = cc.getFocusedSearch(focusID);
         cs.newCtx(cd.getNewCtx());
         //process the "trigger" of new event
      } else if (is.isTypeDevice()) {
         DeviceEvent de = (DeviceEvent) lastEvent;
         if (is.isTypeDeviceKeyboard()) {
            //#debug
            toDLog().pEvent("", de, UserProcessor.class, "processEvent");
         }
         //find the focus type and cmdsearch
         FocusTypeInput fType = getActiveSearchFocus(de);
         CmdNode ccKeyboard = null;
         //fetch the active search focus for this keyboard
         CmdSearch search = cc.getFocusedSearch(fType.getFocusID());

         processEvent(is, dex, fType, search, de);

         return finalCmd(is, dex, search);

      }

      //TODO code for continuous, we want do we? set flag, command processor will request a continous update

      //find a match to trigger context

      //case of click double click.
      //1st: click.. search context. return perfect click and potential double click
      //2nd: another click. search potential. click. yes. next trigger is time constraint. fast enough for a double click? yes double click command
      //no? look up perfect? is this a match? yes. use that command. no search keep the potential list
      // what if click click CTRL+B+C is a command?
      //3rd. ATL pressed. potentail fail. perfect fail. do a search
      //3rd bis. CTRL pressed. potentail potential but no match. do a search for click+ctrl and ctrl alone.
      //merge those with potential and perfects

      //for the triple click or nuple click? double click command read parameters from inputstate.
      //the potential triggers are used in the next phase. search first for the
      //then the perfect is matched, no need for another search.
      return ICmdListener.PRO_STATE_0;
   }

   private int finalCmd(InputState is, ExecutionContext dex, CmdSearch cs) {
      IntToObjects perfects = cs.getPerfect();
      if (perfects.nextempty == 0) {
         //no perfect match..
         managePotentials(dex, is, cs);
         return ICmdListener.PRO_STATE_0;
      } else {
         //#debug
         toDLog().pCmd("Getting First Perfect Match", cs, UserProcessor.class, "process");
         //command definition or CmdInstance
         CmdInstance ci = (CmdInstance) perfects.getObjectAtIndex(0);
         ci.setCtx(dex);
         //send the cmd for processing
         cc.processCmd(ci);
         return ICmdListener.PRO_STATE_1;
      }
   }

   /**
    * We know the event belongs to the user.
    * <li>compute the trigger unit
    * <li>Find the context associated. a keyboard event will execute on the keyboard context
    * however we have to look for potential triggers in other contexts because the keyboard event might 
    * be used as a modifier. Ctrl+Pointer for example.
    * <br>
    * Pattern trigger works with release? How do you represent Pattern + KeyTyped?
    * <br>
    * A pattern is a kind of sub context. modifying context of current event. CTRL_B.
    * Pattern is
    * @param is
    * @param ec
    * @param event
    * @return
    */
   public int processEvent(InputState is, ExecutionContext ec, FocusType ft, CmdSearch cs, BEvent event) {
      //this is live
      CmdTriggerLive cmdUnit = getTriggerUnitFromEvent(is, event); //this is already a unit
      if (cmdUnit != null) {
         //create the pattern trigger..
         //
         //no need to create another unit
         boolean act = updatePatternPressed(cmdUnit.getTriggerUnitHead());
         if (act) {
            //if pattern returns true.. use pattern trigger for a search
            cs.searchPattern(patternTrigger);
            //search potential in other context.. i.e. we want CTRL_A_ to show potential for CTRL_A_MOUSE in mouse context
            for (int i = 0; i < allContexts.nextempty; i++) {
               Object b = allContexts.getObjectAtIndex(i);
               if (b != cs) {
                  ((CmdSearch) b).searchPatternPotential(patternTrigger);
               }
            }
         }
         //after pattern trigger... search for live triggers
         //take current pattern and search
         cs.addTriggerToChain(cmdUnit);
      }
      return ICmdListener.PRO_STATE_0;
   }

   /**
    * A potential trigger has a command trigger unit process it.
    * <li> Repeat
    * @param is
    * @param ctLive
    * @param be
    * @param tuMatchNext {@link ITriggerUnit#isFunction()} is true
    */
   private void inspectPotentialFunctions(InputState is, CmdTrigger ctLive, BEvent be, ITriggerUnit tuMatchNext) {
      if (!(be instanceof DeviceEvent)) {
         //malformed trigger definition
         throw new IllegalArgumentException();
      }
      DeviceEvent de = (DeviceEvent) is.getEventCurrent();
      //TODO there can only be one repeat definition.. throw a user warning
      //timing 
      //register input for long press/repeats... trigger format of a long press is a press followed by a time constraint
      int key = tuMatchNext.getCmdValueA();
      int key2 = tuMatchNext.getCmdValueB();
      int functionType = tuMatchNext.getTriggerFunction();
      if (functionType == FUN_0_NONE) {
      } else if (functionType == FUN_2_REPEAT) {
         ITriggerUnit head = ctLive.getTriggerUnitHead();
         //how many device events to repeat?
         RepeatEvent eventRepeat = new RepeatEvent(cc.getCUC()); //the repeat event will generate a live trigger event
         //that will be matched against first potential trigger
         int numDevice = key; //number of keys repeated. stop event as soon as one key is released
         int numRepeats = key2;
         //use system. if there is another time trigger unit, it defines custom start/period millis\
         ITriggerUnit next = head.getTUNext();
         if (next != null && next.isFunction()) {
            int nextMode = next.getMode();
            if (nextMode == 0) {
               //define specific millis in next integer. otherwise use default value
               int periodMillis = next.getCmdValueA();
               int startMillis = next.getCmdValueB();
               eventRepeat.setPeriodMillis(periodMillis);
               eventRepeat.setStartMillis(startMillis);

            } else if (nextMode == 1) {
               //long press.. next define specific values for preset.. otherwise use def values
               //TODO set pointer move constraints?
            }

         }
         is.getInputRequestRoot().requestRepetition(eventRepeat);
      } else if (functionType == FUN_3_SIMULTANEOUS) {
         inspectFunSimul(is, tuMatchNext);
      }
   }

   private void inspectFunSimul(InputState is, ITriggerUnit tuMatchNext) {
      //
      int simulType = tuMatchNext.getCmdValueA();
      int simulSize = tuMatchNext.getCmdValueB(); //number of keys together
      if (simulType == FUN_SIMULTANEOUS_0_TIMER) {
      } else if (simulType == FUN_SIMULTANEOUS_1_UNDO) {

      } else if (simulType == FUN_SIMULTANEOUS_2_ADD) {

      }
      //every press, we have to check if keys are simultaneously pressed 

      //we check the simultaneous if current context has potential simul
      //.. at least one of the simul keys is pressed. how do we model this in match triggers?

      //if so.. create event and post it
      DeviceEventGroup deg = is.getSimultaneousPressed();
      if (deg != null) {
         is.getInputRequestRoot().requestSimulJob(deg);
      }
   }

   /**
    * 
    * @param is
    * @param ctLive
    * @param be
    * @param tuNext the next trigger unit in the potential
    */
   private void findNextListeners(InputState is, CmdTriggerLive ctLive, BEvent be, TrigUnitInteger tuNext) {
      //e.g check next unit for a time constraint
      if (tuNext.isFunction()) {
         inspectPotentialFunctions(is, ctLive, be, tuNext);
      } else if (tuNext.isGesture()) {
         //some gestures such
         //how do you detect the need for a Gesture ?
         //a gesture linked to 2 keys pressed.
         //match the pointer in the command trigger def to a pointerID
         EventKey ke = null; //track this trigger.. when pattern is gone. unregisters
         int pointerNameID = tuNext.getKey2();
         int pointerID = mapPointerName(pointerNameID);

         GesturePointer gp = is.gesturePointerAdd(pointerID);
         //is it a fire once or continuous gesture with a cancel key
         gp.addKey(ke);
         //there is a gesture unit... pointer press move
         //pointer press, move, released, gesture unit

         //
      } else if (tuNext.isFunction()) {
         //it might register several gestures for different pointers
         int mode = tuNext.getMode();
         if (mode == IInput.MOD_3_MOVED) {
            int pointerID = tuNext.getKey1();
            KeyEventListed ke = is.getLastKeyEvent();
            //is.gesturePointerAddKey(pointerID, ke);
            //the remove ? whatever happens we need to unregister when key is relesead.
            //in case there is a mistake
            //key is flagged as having gestures. so when released inside InputState
            //gesture is analyzed
         }
      }
   }

   /**
    * When a new incoming event arrives updates the pressed pattern.
    * <br>
    * Last event with pattern will be matched.
    * The cmdUnit updates the pattern or not.
    * When a unit is a press event, if keys are already pressed, it will be used
    * as a pattern trigger. Otherwise it returns false and the single pressed key
    * follows the normal flow.
    * When a unit is a release event, updates the pattern return false. release
    * goes the normal flow for fasttype of other things.
    * <br>
    * CTRL+FastTyped Key? What about the pattern? 
    */
   private boolean updatePatternPressed(ITriggerUnit cmdUnit) {
      if (cmdUnit.isDevice()) {
         if (cmdUnit.isReleased()) {
            patternTrigger = patternTrigger.unitMinus(cmdUnit);
         } else if (cmdUnit.isPressed()) {
            patternTrigger = patternTrigger.unitPlus(cmdUnit);
            return true;
         }
      }
      return false;
   }

   //#mdebug
   public IDLog toDLog() {
      return toStringGetUCtx().toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "UserProcessor");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "UserProcessor");
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return cc.getUCtx();
   }

   //#enddebug

}
