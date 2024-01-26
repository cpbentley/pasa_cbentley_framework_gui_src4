package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.core.src4.interfaces.IAInitable;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.stator.StatorReader;
import pasa.cbentley.core.src4.stator.StatorWriter;
import pasa.cbentley.core.src4.utils.ColorUtils;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdListener;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdMenu;
import pasa.cbentley.framework.coredraw.src4.interfaces.IGraphics;
import pasa.cbentley.framework.coredraw.src4.interfaces.IImage;
import pasa.cbentley.framework.coreui.src4.event.CanvasHostEvent;
import pasa.cbentley.framework.coreui.src4.exec.ExecEntry;
import pasa.cbentley.framework.coreui.src4.exec.ExecutionContext;
import pasa.cbentley.framework.coreui.src4.interfaces.ICanvasHost;
import pasa.cbentley.framework.coreui.src4.interfaces.ITechEventHost;
import pasa.cbentley.framework.coreui.src4.tech.ITechCodes;
import pasa.cbentley.framework.coreui.src4.tech.ITechHostUI;
import pasa.cbentley.framework.coreui.src4.tech.IInput;
import pasa.cbentley.framework.coreui.src4.tech.IBOCanvasHost;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.engine.RgbImage;
import pasa.cbentley.framework.gui.src4.anim.AnimCreator;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceDrawable;
import pasa.cbentley.framework.gui.src4.core.FigDrawable;
import pasa.cbentley.framework.gui.src4.core.TopoViewDrawable;
import pasa.cbentley.framework.gui.src4.ctx.CanvasGuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ITechCtxSettingsAppGui;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsView;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawableExCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawableListener;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IUIView;
import pasa.cbentley.framework.gui.src4.menu.CmdMenuBar;
import pasa.cbentley.framework.gui.src4.mui.StyleAdapter;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;
import pasa.cbentley.framework.gui.src4.utils.ForegroundsQueues;
import pasa.cbentley.framework.gui.src4.utils.RenderMetrics;
import pasa.cbentley.framework.input.src4.CanvasAppliInput;
import pasa.cbentley.framework.input.src4.CanvasResult;
import pasa.cbentley.framework.input.src4.InputState;
import pasa.cbentley.framework.input.src4.RepaintCtrl;
import pasa.cbentley.framework.input.src4.ScreenOrientationCtrl;
import pasa.cbentley.framework.input.src4.interfaces.ITechPaintThread;
import pasa.cbentley.framework.input.src4.interfaces.ITechInputCycle;
import pasa.cbentley.layouter.src4.engine.LayouterDebugBreaker;
import pasa.cbentley.layouter.src4.engine.PozerFactory;
import pasa.cbentley.layouter.src4.engine.SizerFactory;

/**
 * A Canvas that provides a ViewDrawable layout
 * <br>
 * Treats everything as a {@link IDrawable},
 * even the DebugField.
 * <br>
 * The {@link ICmdMenu} is also just a regular {@link IDrawable}.
 * <br>
 * Uses the {@link GraphicsX} framework
 * 
 * Canvas supporting 
 * 
 * @author Charles Bentley
 *
 */
public class CanvasAppliDrawable extends CanvasAppliInput implements IDrawableListener, IAInitable {

   public void stateReadFrom(StatorReader state) {
      super.stateReadFrom(state);
   }

   public void stateWriteTo(StatorWriter state) {
      super.stateWriteTo(state);
   }

   private AnimCreator               animCreator;

   /**
    * Clearing color at the start of each painting cycle. The clearing can be disabled
    */
   private int                       backgroundColor;

   private CanvasDebugger            canvasDebug;

   private CmdCtx                    cc;

   int                               cmdProcessingMode;

   protected GraphicsXD              destGraphicsX;

   private CanvasDrawControl         drwControl;

   private CanvasAppliDrawableExtras extras;

   private ForegroundsQueues         foregroundQueues;

   private GuiCtx                    gc;

   private boolean                   isPainting;

   ExecutionCtxDraw                         previous;

   private RenderMetrics             renderMetrics;

   /**
    * The rotation state.
    * <li> {@link ITechCanvasDrawable#SCREEN_0_TOP_NORMAL}
    * <li> {@link ITechCanvasDrawable#SCREEN_1_BOT_UPSIDEDOWN}
    * <li> {@link ITechCanvasDrawable#SCREEN_2_LEFT_ROTATED}
    * <li> {@link ITechCanvasDrawable#SCREEN_3_RIGHT_ROTATED}
    * 
    */
   protected int                     screenConfig = ITechHostUI.SCREEN_0_TOP_NORMAL;

   private CanvasTechHelper          settingsHelper;

   private StyleAdapter              styleAdapter;

   /**
    * Tracks the current visible stack of {@link IDrawable}.
    * In this topology,
    * 0,0 is the Screen.
    * <br>
    * This reference will never change.
    * <br>
    * Can be read by both threads.
    */
   private TopologyDLayer            topologyRoot;

   /**
    * The root element of this View. There can only be one.
    * It will be drawn in background.
    * <br>
    * <br>
    * Fixed size.
    */
   protected TopoViewDrawable        topoViewDrawableRoot;

   private ViewContext               vcContent;

   private ViewContext               vcRoot;

   private CanvasGuiCtx canvasGC;

   public CanvasAppliDrawable(GuiCtx gc, ByteObject canvasTechHost) {
      this(gc, gc.createTechCanvasAppliDrawableDefault(), canvasTechHost);
   }

   /**
    * Creates a {@link CanvasAppliDrawable} with its {@link ICanvasHost}
    * At constructor time, the Canvas size is known.
    * <br>
    * 
    * Automatically registers {@link CanvasAppliDrawable} with {@link GuiCtx}
    * 
    * @param gc
    * @param techCanvasAppli {@link ITechCanvasAppliDrawable}
    * @param techCanvasHost {@link IBOCanvasHost}
    */
   public CanvasAppliDrawable(GuiCtx gc, ByteObject techCanvasAppli, ByteObject techCanvasHost) {
      super(gc.getIC(), techCanvasAppli, techCanvasHost);
      this.gc = gc;
      this.cc = gc.getCC();
      this.canvasGC = new CanvasGuiCtx(gc, this);
      gc.addCanvas(this);

      //only call this if we are leaf
      if (this.getClass() == CanvasAppliDrawable.class) {
         a_Init();
      }

      //#debug
      toDLog().pInit("Size inside constructor w=" + getWidth() + " h=" + getHeight(), null, CanvasAppliDrawable.class, "CanvasAppliDrawable", LVL_05_FINE, true);
   }

   public CanvasGuiCtx getCanvasGC() {
      return canvasGC;
   }
   /**
    * Because those helpers are designed to be extended
    */
   public void a_Init() {
      super.a_Init();
      settingsHelper = new CanvasTechHelper(this);
      extras = new CanvasAppliDrawableExtras(gc, this);
      drwControl = new CanvasDrawControl(gc, this);
      renderMetrics = new RenderMetrics(gc);

      constructHelpersDrawable(gc);

      applySettingsCanvas();
   }

   private CmdMenuBar cmdMenuBar;

   /**
    * <li> Creates Menu Bar if settings require it. 
    * {@link ITechCtxSettingsAppGui#CTX_GUI_FLAG_2_USER_MENU_BAR}
    * Sets the menubar position.
    * What about the style of the Menu?
    * @param mm
    */
   public void applySettingsCanvas() {

      boolean useMenuBar = settingsHelper.isUsingMenuBar();
      ByteObject settingsGuiCtx = gc.getSettingsBO();
      //define which menu bar to use
      //if only root canvas
      if (useMenuBar) {
         if (cmdMenuBar == null) {
            //TODO the style class will depend on the menu type
            //how can this be static
            cmdMenuBar = new CmdMenuBar(gc, gc.getClass(IUIView.SC_1_MENU));
            cmdMenuBar.getLay().laySiz_Preferred();
         }
         int pos = settingsGuiCtx.get1(ITechCtxSettingsAppGui.CTX_GUI_OFFSET_04_MENU_BAR_POSITION1);
         topoViewDrawableRoot.setHeader(cmdMenuBar, pos, ITechViewPane.PLANET_MODE_0_EAT);
      } else {
         if (cmdMenuBar != null) {
            topoViewDrawableRoot.removeDrawable(cmdMenuBar);
         }
      }
      //update with the menu bar
      extras.setMenuBar(cmdMenuBar);

      int debugMode = settingsHelper.getDebugMode();
      setDebugMode(debugMode);

   }

   public void checkRenderThread() {
      if (!isThreadRender()) {
         throw new IllegalThreadStateException("Must be the Render Thread");
      }
   }

   public void clearCanvasWithBgColor(GraphicsX g) {
      int dx = 0;
      int dy = 0;
      int dw = this.getWidth();
      int dh = this.getHeight();
      g.clear(this.backgroundColor, dx, dy, dw, dh);
   }

   /**
    * Paint background color on the whole physical screen. 
    * <br>
    * <br>
    * Does not use {@link MasterCanvas#getHeightVirtual()} but {@link MasterCanvas#getHeight()}
    * <br>
    * Erase all DLayers and set state {@link ITechDrawable#STATE_02_DRAWN} to false.
    * <br>
    * <br>
    * @param g
    */
   private void clearPhysicalCanvas(GraphicsXD g) {
      clearCanvasWithBgColor(g);
      //sets the Drawn flag to false 
      topologyRoot.setStateFlags(ITechDrawable.STATE_02_DRAWN, false);
   }

   private void cmdPro2() {
      // TODO Auto-generated method stub

   }

   /**
    * Sends {@link ITechCanvasDrawable#CMD_EVENT_4_UIEVENT} to the current {@link CmdCtx} hierarchy
    * for processing
    * @param cc
    * @param icc
    */
   private int cmdProCmdCtx(int state, InputConfig icc) {
      if (state == ICmdListener.PRO_STATE_0) {
         return cc.getActiveCtx().sendEvent(ITechCanvasDrawable.CMD_EVENT_4_UIEVENT, icc); //send event down ctx hierarch
      }
      return state;
   }

   /**
    * 
    * @param cc
    * @param icc
    */
   private int cmdProCmdRepo(int state, ExecutionCtxDraw icc, int ctxcat) {
      if (state == ICmdListener.PRO_STATE_0) {
         state = gc.getViewCommandListener().processGUIEvent(icc, ctxcat);
         return state;
      }
      return state;
   }

   /**
    * Delegates the input management to the {@link TopologyDLayer} root.
    * @param state
    * @param icc
    * @return
    */
   private int cmdProDrawables(int state, InputConfig icc) {
      //send event down drawable hierarchy why third? why not first? TODO config
      if (state == ICmdListener.PRO_STATE_0) {
         //a cmd was not processed
         //TODO aqcuire lock on topology. business threads might update it
         boolean isDone = topologyRoot.intercepts(icc);
         if (!isDone) {
            //send the event down to the Drawables
            topologyRoot.manageInput(icc);
         }
         //         isDone = appliTopo.intercepts(icc);
         //         if (!isDone) {
         //            appliTopo.manageInput(icc);
         //         }
      }
      return state;
   }

   /**
    * Navigational Pointer Event Select
    * @param ic
    */
   public void cmdSelectPointer(InputConfig ic) {
      boolean isDone = topologyRoot.intercepts(ic);
      if (!isDone) {
         //send the event down to the Drawablesd
         topologyRoot.manageInput(ic);
      }
   }

   private void constructHelpersDrawable(GuiCtx gc) {
      //we separate root, to display things outside the application. such as a debug banner
      vcRoot = new ViewContext(gc, this);
      vcRoot.toStringSetDebugName("Root");
      topologyRoot = vcRoot.getTopo();
      //create the first ViewContext
      vcRoot.setWidth(getWidth());
      vcRoot.setHeight(getHeight());

      //#debug
      toDLog().pInit("ViewContext Root with first W and H ", vcRoot, CanvasAppliDrawable.class, "constructHelpersDrawable", LVL_05_FINE, true);

      vcRoot.setTopo(topologyRoot);

      vcContent = new ViewContext(gc, this);
      vcContent.toStringSetDebugName("Appli");
      vcContent.setParent(vcRoot);

      //set the no style style
      topoViewDrawableRoot = new TopoViewDrawable(gc, gc.getClass(IUIView.SC_6_EMPTY), vcRoot, vcContent);

      //#debug
      topoViewDrawableRoot.setDebugName("RootDrawable");

      topoViewDrawableRoot.getLay().layFullViewContext();

      //#debug
      gc.getLAC().toStringSetDebugBreaks(new LayouterDebugBreaker(gc.getLAC(), topoViewDrawableRoot));

      //place it
      vcRoot.getTopo().addDLayer(topoViewDrawableRoot, ITechCanvasDrawable.SHOW_TYPE_0_REPLACE);

      //#debug
      //toLog().ptInit("RootDrawable", root, Canvas.class, "Constructor");

      //it will be modified everytime the ViewPort area changes... how to do that?
   }

   /**
    * Called when a new
    */
   public InputState createInputState() {
      return new InputStateDrawable(gc, this);
   }

   protected RepaintCtrl createRepaintCtrl() {
      return new RepaintCtrlDrawable(gc, this);

   }

   public CanvasResultDrawable createScreenResultAnimation() {
      return (CanvasResultDrawable) repaintControl.create(ITechInputCycle.CYCLE_2_ANIMATION_EVENT);
   }

   protected void ctrlAppEvent(InputState is, CanvasResult sr, CanvasHostEvent che) {
      int actionType = che.getActionType();
      //#debug
      toDLog().pEvent1("" + actionType, is, CanvasAppliDrawable.class, "ctrlAppEvent");

      if (actionType == ITechEventHost.ACTION_1_CLOSE) {
      } else if (actionType == ITechEventHost.ACTION_2_MOVED) {
      } else if (actionType == ITechEventHost.ACTION_4_FOCUS_GAIN) {
      } else if (actionType == ITechEventHost.ACTION_3_RESIZED) {
         ctrlAppEventResized(is, sr, che);
      } else if (actionType == ITechEventHost.ACTION_5_FOCUS_LOSS) {
      } else if (actionType == ITechEventHost.ACTION_6_NOTIFY_SHOW) {
      }

   }

   protected void ctrlAppEventResized(InputState is, CanvasResult sr, CanvasHostEvent e) {
      sizeChanged(e.getW(), e.getH());
   }

   /**
    * 
    * An event 
    * <li>wait for previous execution context to finish
    * <li>queries the state of the GUI with focus state changes , Lock on {@link ITechPaintThread#THREAD_2_RENDER} 
    * <li>identify a command,
    * <li>execute the command and sub commands, 
    * <li>push the changes to the GUI. Lock on {@link ITechPaintThread#THREAD_2_RENDER}
    * <br>
    * <br>
    * An execution context can be parceled in several paint jobs. 
    * <ol>
    * <li>A pointer click is made
    * <li>Drawable Focus states change.. one repaint job can be queued for that drawable
    * <li>Update thread search for a command. We have a select on a Menu Item
    * <li>Select command decides a {@link MCmd}
    * <li>
    * </ol>
    * An event cannot reason while the previous execution context is still rendering
    * 
    * In a single thread, all repaint jobs are queued and executed once the update process
    * is done.
    * With an independent rendering thread, as soon as the rendering thread finishes,
    * it can load jobs, modify the rendering state and paint the new state.
    * <br>
    * 
    * 
    * If the command is not found, {@link CanvasAppliInput} will forward
    * the event to the 
    * {@link CanvasAppliInput#ctrlKeyPressed}
    * <br>
    * Called in the update thread {@link ITechPaintThread#THREAD_1_UPDATE}
    * <br>
    * <br>
    * At the begining of the Appli life cycle, the CMD start is processed.
    * <br>
    * <br>
    * <b>A Pointer Move</b> Implies
    * <li> the drawable hierarchy checks for a new PointerMoveFocus Drawable 
    * <li> In which case, the trigger NewContext on the current context
    * <br>
    * <br>
    * <b>A Pointer Press</b>
    * <li> the drawable hierarchy checks for a new PointerPressFocus Drawable 
    * <li> Associated context is set.
    */
   protected void ctrlUIEvent(InputState ic, CanvasResult sr) {
      //#debug
      toDLog().pFlow("", this, CanvasAppliDrawable.class, "ctrlUIEvent@line416", LVL_03_FINEST, true);

      if (ic.getEventCurrent() instanceof CanvasHostEvent) {
         ctrlAppEvent(ic, sr, (CanvasHostEvent) ic.getEventCurrent());
         return;
      }

      if (ic.isKeyTyped(ITechCodes.KEY_F2)) {
         toDLog().pAlways("F2 Debug CanvasView", this, CanvasAppliDrawable.class, "ctrlUIEvent");
         return;
      }
      if (ic.isKeyPressed(ITechCodes.KEY_F3)) {
         toDLog().pAlways("F3 Debug InputState", ic, CanvasAppliDrawable.class, "ctrlUIEvent");
      }
      if (ic.isKeyPressed(ITechCodes.KEY_F4)) {
         toDLog().pAlways("F4 Debug Coordinator", gc.getCFC().getCoordinator(), CanvasAppliDrawable.class, "ctrlUIEvent");
      }
      InputStateDrawable id = (InputStateDrawable) ic;
      CanvasResultDrawable sd = (CanvasResultDrawable) sr;

      InputConfig icc = new InputConfig(gc, this, id, sd);

      //wait until previous context has finished rendering. why?
      //because we will getDrawable at x,y and we need the latest state
      if (getThreadingMode() == ITechPaintThread.THREADING_3_THREE_SEPARATE) {
         //in single thread.. that's automatic
         if (previous != null) {
            if (!previous.finished) {
               //acquire lock
               synchronized (previous) {
                  try {
                     previous.wait();
                  } catch (InterruptedException e) {
                     e.printStackTrace();
                     return;
                  }
               }
            }
         }
      }
      ExecutionCtxDraw exd = new ExecutionCtxDraw(gc);
      exd.setInputConfig(icc);
      //when only pointer pressed 
      //TODO. we must get pointer events first so they pointer presses change the focus and thus
      //active pointer ctx
      //commands are activated on pointer context for pointer events. CTRL+pointer acts
      //on the pointer cmd context. same for drags
      //programmer can use color pointers in pointer only, different pointer modes act like modifiers
      //CTRL ALT. Pressing CTRL sets pointer in blue
      //in windows, pressing ALT gives the keyboard focus to the menu and sets the pointer over context
      // as null. it does not work anymore. disabled
      // left and right
      //TODO context category is device type and id dependant.
      int ctxCategory = ICmdsView.CTX_CAT_0_KEY;
      if (ic.isTypeDevicePointer()) {
         //when pointer event, update the pointer context
         if (ic.getMode() == IInput.MOD_0_PRESSED || ic.getMode() == IInput.MOD_1_RELEASED) {
            ctxCategory = ICmdsView.CTX_CAT_1_POINTER_PRESSED;
         } else if (ic.getMode() == IInput.MOD_3_MOVED) {
            ctxCategory = ICmdsView.CTX_CAT_2_POINTER_OVER;
         }
         //TODO drag has a context?
         ctxCategory = ctxCategory + (ic.getDeviceID() * 2);

         //we need a read lock on graphical state.
         lockAcquireRender();
         //we must sync on the GUI thread to access. TODO wait for 
         //last execution context to be finished? i.e. shown on 
         IDrawable d = topologyRoot.getDrawable(icc.is.getX(), icc.is.getY(), exd);
         //it is the drawable to be focused in its category? always??
         if (d != null) {
            //set the states here? mouse animation hooks?
            gc.getFocusCtrl().setPointerStates(icc, d);
            //            FocusType ft = new FocusType();
            //            ft.deviceID = ic.getDeviceID();
            //            ft.deviceType = IInput.DEVICE_1_MOUSE;
            //            ft.personID = 0; //
            //            gc.getFocusCtrl().setNewFocus(ic, d, ft);
         }
         lockReleaseRender();
      }
      //down open menu. if no left, left will go to next menu, the left is delegated up
      int state = 0;
      if (cmdProcessingMode == ITechCanvasDrawable.CMD_PRO_0) {
         state = cmdProCmdRepo(state, exd, ctxCategory);
         state = cmdProCmdCtx(state, icc);
         state = cmdProDrawables(state, icc);
      } else if (cmdProcessingMode == ITechCanvasDrawable.CMD_PRO_1) {
         state = cmdProDrawables(state, icc);
         state = cmdProCmdCtx(state, icc);
         state = cmdProCmdRepo(state, exd, ctxCategory);
      } else if (cmdProcessingMode == ITechCanvasDrawable.CMD_PRO_2) {
         state = cmdProCmdRepo(state, exd, ctxCategory);
         state = cmdProDrawables(state, icc);
         state = cmdProCmdCtx(state, icc);
      } else {
         //no processing lol
      }

      //jobs from execution map execution. GUI thread stuff is packed into a run 
      ExecEntry ee = null;
      while ((ee = exd.getNext()) != null) {
         int type = ee.type;
         if (type == IDrawableExCtx.TYPE_0_EVENT) {
            BusEvent be = (BusEvent) ee.o;
            be.putOnBus();
         } else if (type == IDrawableExCtx.TYPE_1_RUN) {
            Runnable d = (Runnable) ee.o;
            exd.addRender(ee);
         } else if (type == IDrawableExCtx.TYPE_2_DRAW) {
            IDrawable d = (IDrawable) ee.o;
            int action = ee.action;
            //issue is that it is not sized already.. so pointless?
            //no because drawables flags here will be painted
            //
            sd.setActionDoneRepaint(d);
            //lets entry sit to be processed by render thread
            exd.addRender(ee);

         }
      }
      //we are in the update thread.. what to repaint?
      //wehen debug mode.. set full repaint

      sr.setExCtx(exd);

      if (canvasDebug != null) {
         sd.setActionDoneRepaint(canvasDebug);
      }

   }

   /**
    * Draws a figure at the mouse position
    * @param cd
    */
   public void doCuePress(CmdInstanceDrawable cd) {
      int cx = cd.getIC().is.getX();
      int cy = cd.getIC().is.getY();
      int cueColor = ColorUtils.getRGBInt(128, 128, 128, 128);
      ByteObject fig = gc.getDC().getFigureFactory().getEllipse(cueColor);
      IDrawable cue = new FigDrawable(gc, gc.getClass(IUIView.SC_6_EMPTY), fig);

      SizerFactory siz = gc.getLAC().getSizerFactory();
      ByteObject sizerF = siz.getSizerFontH();
      cue.setSizers(sizerF, sizerF);

      //The cue will active for draggs? It registers to disappear
      //after a release of the pointer
      //TODO anchor at center of x, y
      //mutable pozers. update
      PozerFactory poz = gc.getLAC().getPozerFactory();
      ByteObject pozerX = poz.getPozerAtPixel(C.LOGIC_2_CENTER, cx);
      ByteObject pozerY = poz.getPozerAtPixel(C.LOGIC_2_CENTER, cy);

      cue.setXY(pozerX, pozerY);

      //set flag to ignore it when fetching pointers

      //cue.setBehaviorFlag(ITechDrawable.BEHAVIOR_16_IMMATERIAL, true);
      //cue must be shown in a foreground. pure visual
      cd.getDExCtx().addDrawn(cue);
   }

   public void doUpdateAppliVC() {

      //      //#mdebug
      //      String msg = "x=" + root.getContentX() + " y=" + root.getContentY() + " w=" + root.getViewPortContentW() + " h=" + root.getViewPortContentH() + " ";
      //      toLog().ptInit1(msg, root, CanvasView.class, "doUpdateAppliVC");
      //      //#enddebug
      //      
      //      ViewContext vcAppli = gc.getVCAppli();
      //      vcAppli.setWidth(root.getViewPortContentW());
      //      vcAppli.setHeight(root.getViewPortContentH());
      //      vcAppli.setX(root.getContentX());
      //      vcAppli.setY(root.getContentY());

   }

   /**
    * Reset presses on the InputState
    */
   public void exitInputContext() {
      super.exitInputContext();
   }

   public AnimCreator getAnimCreator() {
      if (animCreator == null) {
         animCreator = new AnimCreator(gc, this);
      }
      return animCreator;
   }

   public CanvasDebugger getDebugCanvas() {
      return canvasDebug;
   }

   public CanvasDrawControl getDrwCtrl() {
      return drwControl;
   }

   public CanvasAppliDrawableExtras getExtras() {
      return extras;
   }

   public ForegroundsQueues getForegrounds() {
      if (foregroundQueues == null) {
         foregroundQueues = new ForegroundsQueues(gc, this);
      }
      return foregroundQueues;
   }

   /**
    * System menubar of the {@link CanvasAppliDrawable}
    * Collects the cmd of the current command node.
    * @return
    */
   public CmdMenuBar getMenuBar() {
      return cmdMenuBar;
   }

   public RenderMetrics getRenderMetrics() {
      return renderMetrics;
   }

   /**
    * Returns singleton until a Canvas Structural Change event occurs.
    */
   public RepaintCtrlDrawable getRepaintCtrlDraw() {
      //#mdebug
      if (repaintControl == null) {
         throw new NullPointerException();
      }
      //#enddebug
      return (RepaintCtrlDrawable) repaintControl;
   }

   /**
    * The {@link IDrawable} first drawn for the Appli
    * @return
    */
   public IDrawable getRoot() {
      return topoViewDrawableRoot;
   }

   /**
    * Excludes any drawable related to the Menubar
    * @param bgColor
    * @return
    */
   public RgbImage getSSVirtualCanvasAsImage(int bgColor) {
      int w = gc.getVCAppli().getWidth();
      int h = gc.getVCAppli().getHeight();
      RgbImage img = gc.getDC().getRgbCache().create(w, h, bgColor);
      GraphicsX gx = img.getGraphicsX(GraphicsX.MODE_2_RGB_IMAGE);
      gx.setPaintCtxFlag(ITechCanvasDrawable.REPAINT_01_FULL, true);
      gx.setPaintCtxFlag(ITechCanvasDrawable.REPAINT_15_SCREEN_SHOT, true);
      this.topoViewDrawableRoot.draw(gx);
      return img;
   }

   public StyleAdapter getStyleAdapter() {
      if (styleAdapter == null) {
         styleAdapter = gc.getStyleAdapterDefault();
      }
      return styleAdapter;
   }

   /**
    * The {@link ViewContext} excluding sattelites like debug and menus
    * @return
    */
   public ViewContext getVCAppli() {
      return vcContent;
   }

   /**
    * The Whole view including Debug and Menus
    * @return
    */
   public ViewContext getVCRoot() {
      return vcRoot;
   }
   
   public GuiCtx getGC() {
      return gc;
   }

   public void layoutInvalidate() {
      topologyRoot.invalidateLayout();
   }

   public void notifyEvent(IDrawable d, int event, Object o) {
      //#debug
      //toLog().ptEvent1("Event=" + Drawable.toStringEvent(event) + " ", d, CanvasView.class, "notifyEvent");

      if (event == ITechDrawable.EVENT_12_SIZE_CHANGED) {
         if (d == topoViewDrawableRoot) {
            doUpdateAppliVC();
         }
      }
   }

   /**
    * Paint on G the class configuration of Full/Animation/Realisator
    * <br>
    * <br>
    * When doing a repaint Full, concurrently with a Animation thread,
    * paint ordering must be respected and it not based on animation order (specific to realisator implementation)
    * 
    * @param g
    */
   private void paintCanvasContent(GraphicsXD g) {
      //debug business 
      long time = System.currentTimeMillis();
      //we must paint the background of the canvas before the translation to erase the top header if any

      //only repaint selected drawables
      if (g.hasPaintCtx(ITechCanvasDrawable.REPAINT_06_DRAWABLE)) {
         //must be painted in Order of DLayer index

         g.screenResultCause.paintDrawables(g);
      } else {
         //do a full clear
         clearPhysicalCanvas(g);

         //draw the center first
         //appliTopo.drawLayers(g);

         //draw the root topology on top
         topologyRoot.drawLayers(g);

         //and finally draw the foregrounds
         if (foregroundQueues != null) {
            //single paint are painted on the top of the animations
            foregroundQueues.paintForegrounds(g);

            //draw the postponed
            g.drawPostponed();
         }

      }

      //after the clip reset if any, paint the menu bar

      //draw top debug header information
      if (canvasDebug != null) {
         canvasDebug.paintDebugComplete(g, time);
      }

   }

   /**
    * Last Step when sending the data to the Screen Graphics
    * Modifies it
    * A full repaint must update the background image
    * @param g
    */
   private void paintFinalToScreen(IGraphics g, GraphicsX gx) {
      //there is only work if not in screen. In Screen Mode everything is already drawn
      //to the screen Graphics
      int transformation = IImage.TRANSFORM_0_NONE;
      switch (screenConfig) {
         case ITechCanvasDrawable.SCREEN_2_LEFT_ROTATED:
            transformation = IImage.TRANSFORM_5_ROT_90;
            break;
         case ITechCanvasDrawable.SCREEN_3_RIGHT_ROTATED:
            transformation = IImage.TRANSFORM_6_ROT_270;
            break;
         case ITechCanvasDrawable.SCREEN_1_BOT_UPSIDEDOWN:
            transformation = IImage.TRANSFORM_3_ROT_180;
            break;
         default:
            break;
      }
      gx.paintToScreen(g, transformation);
   }

   /**
    * A rotation modifies the virtual canvas size.
    * <br>
    * <br>
    * Compute the new dimension
    * 
    * Called by {@link ScreenOrientationCtrl#rotate(int)}
    */
   public void postRotation() {
      //size for the image layer
      int w = 0;
      int h = 0;
      if (screenConfig == ITechHostUI.SCREEN_1_BOT_UPSIDEDOWN || screenConfig == ITechHostUI.SCREEN_0_TOP_NORMAL) {
         w = getWidth();
         h = getHeight();
      } else {
         w = getHeight();
         h = getWidth();
      }
      if (destGraphicsX.getPaintMode() == GraphicsX.MODE_0_SCREEN) {
         //we must give the size of the Virtual canvas for the image layer
         destGraphicsX.setPaintMode(GraphicsX.MODE_1_IMAGE, w, h);
      }
      sizeChanged(w, h);
   }

   /**
    * Main Render method to draw {@link CanvasAppliDrawable} content. 
    * 
    * <b>Threading Mode</b>
    * <li>Called in the render thread {@link ITechPaintThread#THREAD_2_RENDER}
    * in Active Rendering mode 
    * <li> the GUI Event Thread in Passive Mode.
    * <li> It can also be called in a Business Thread when painting ? Not a good idea.
    * <br>
    * 
    * {@link RepaintCtrlDrawable} tracks the context;
    * 
    * <br>
    * <br>
    * During the paint, {@link Controller} {@link InputConfig} and {@link ScreenResult} give the state that generates the painting
    * <br>
    * Once paint concludes and {@link CanvasResult#paintEnd()} is called, this is no more the case. A new cycle has begun.
    * Different cycles exist:
    * <li>UserEventAction-Repaint {@link Controller#CYCLE_0_USER_EVENT}
    * <li>BusinessCodeThread-Repaint {@link Controller#CYCLE_1_BUSINESS_EVENT}
    * <li>AnimatonTimer-Repaint {@link Controller#CYCLE_2_ANIMATION_EVENT}
    * <br>
    * <br>
    * <b>Implementation Note</b> : <br>
    * Reminder from {@link Canvas} : Applications must not assume that they know the underlying source of the paint()  call and 
    * use this assumption to paint only a subset of the pixels within the clip region. 
    * The reason is that this <b>particular paint()</b> call may have resulted from <b>multiple repaint()</b> requests, some of which may have been generated from outside the application. 
    * <br>
    * An application that paints only what it thinks is necessary to be painted may display incorrectly if the screen contents had been invalidated by, for example, an incoming telephone call. 
    * Failing to paint every pixel may result in a portion of the previous screen image remaining visible. 
    * <br>
    * <br>
    * <b>Solution</b>: <br>
    * How can this framework identifies one its calls with a call from the host? Should all drawable flagged repaints be dubious?
    * <br>
    * One way of knowing is if the paint clip our framework coalesce is different from which given, in which case, screen result goes to
    * fullRepaint and flag {@link ITechCanvasDrawable#REPAINT_09_EXTERN} is set.
    * <br>
    * When only an external call is made, framework identifies it because the {@link ScreenResult} does not have any pending repaint calls.
    * <br>
    * When several external calls are made, a full caching of the Canvas enable fast repaints.
    * <br>
    * Still the unlikely event the external call and internal are made exactly together will mess up. 
    * <br>
    * <br>
    * This method may never be called anywhere in the framework or application code.
    * <br>
    * <br>
    */
   protected void render(IGraphics g, InputState is, CanvasResult sr) {

      //in drawing, first validate layout, invalidate caches. 
      //second pass, draw.
      //TODO we have to compute what is changed 
      //as long as a drawable is x,y,w,h is the same, repaint 

      //draw strategy
      //1 execute runnables from the execution context in the screenres before
      ExecutionContext ex = sr.getExecCtx();
      if (ex != null) {
         ExecutionCtxDraw dec = (ExecutionCtxDraw) ex;
         dec.renderStart();

      }
      //2 validate the layout and positions of the tree, invalidates caching.

      //3 draw the tree

      //#debug
      String msg = "Clip " + "[" + g.getClipX() + "," + g.getClipY() + " " + g.getClipWidth() + "," + g.getClipHeight() + "]";
      //#debug
      toDLog().pDraw(msg, null, CanvasAppliDrawable.class, "render");

      CanvasResultDrawable renderCause = (CanvasResultDrawable) sr;
      //clean clip sequence since this is a new paint job
      super.paintStart();
      //create new GraphicsXDrawable context
      if (destGraphicsX == null) {
         //init the 
         destGraphicsX = new GraphicsXD(gc);
         int paintMode = settingsHelper.getPaintMode();
         destGraphicsX.setPaintMode(paintMode, getWidth(), getHeight());
         destGraphicsX.toStringSetName("RootGraphics");
         //destGraphicsX = new GraphicsX(GraphicsX.MODE_IMAGE, getWidth(), getHeight());
      }
      if (destGraphicsX.getPaintMode() == GraphicsX.MODE_0_SCREEN) {
         //when paint mode is not screen, we have to draw
         destGraphicsX.reset(g);
      }
      destGraphicsX.screenResultCause = renderCause;
      destGraphicsX.isd = (InputStateDrawable) is;

      ///////////////////////
      //#debug
      //System.out.println(screenResultActive.toStringRepaintFlags() + " " + screenResultActive.debugClip(g));
      //#mdebug
      if (isPainting) {
         isPainting = false;
         System.err.println("There was a uncatched exception in the last painting cycle");
         throw new RuntimeException("Paint is True in CanvasView");
      }
      //#enddebug
      ///////////////
      isPainting = true;
      //initialize the GraphicsX according to the Painting Mode
      //System.out.println("========== DEBUG MASTERCANVAS JUST BEFORE PAINT METHOD ============");
      //System.out.println(MasterCanvas.getMasterCanvas().toString());
      //draw on the destination GraphicsX object
      int repaintFlags = renderCause.getRepaintFlag();
      if (repaintFlags == 0) {
         //#debug
         toDLog().pDraw("Repaint Flag is Zero. Sync Error or something", this, CanvasAppliDrawable.class, "render", LVL_05_FINE, true);
         repaintFlags = ITechCanvasDrawable.REPAINT_01_FULL;
      }
      repaintFlags |= ITechCanvasDrawable.REPAINT_14_SYSTEM_REPAINT;
      //TODO move this method GraphicsXD
      destGraphicsX.setPaintCtx(repaintFlags);

      //
      render1(destGraphicsX);

      //make sure everything is sent to the Screen Graphics
      //TODO when not screen mode, we have to print it
      paintFinalToScreen(g, destGraphicsX);

      super.paintEnd();

      //#debug
      toDLog().pDraw("End of Paint", this, CanvasAppliDrawable.class, "render", LVL_03_FINEST, true);

      //reset repaint flags
      isPainting = false;
      renderCause.paintEnd();
      //TODO check if during the paint method, some business process piles up some ScreenResult
      //in which case, call a repaint on those.
   }

   /**
    * In All cases, draw everything on the RgbImage of the GraphicsX 
    * @param g
    */
   public void render1(GraphicsXD g) {
      if (canvasDebug != null) {
         canvasDebug.debugRender1Start(g);
      }
      //ask the device to time
      long time = gc.getCFC().getTimeCtrl().getTickNano();

      paintCanvasContent(g);

      long after = gc.getCFC().getTimeCtrl().getTickNano();
      long duration = (after - time);
      //add the paint stat to compute the last paintings and their timing
      //average time painting, average event time processing.
      renderMetrics.addFrame(time, duration);

      if (canvasDebug != null) {
         canvasDebug.debugRender1End(g, this.getRenderMetrics());
      }
   }

   /**
    * Renders the whole render state.
    * Use a special screen results.
    * Must be called in a BusinessThread.
    * <br>
    * Reads the RenderState
    * @param g
    */
   protected void renderImage(GraphicsXD g) {
      render1(g);
   }

   /**
    * 
    * @param mode
    */
   public void setDebugMode(int mode) {
      if (mode != ITechCanvasDrawable.DEBUG_0_NONE) {
         if (canvasDebug == null) {
            canvasDebug = new CanvasDebugger(gc);
            canvasDebug.setViewContext(vcRoot);
         }
         canvasDebug.setDebugMode(mode);
         int pos = settingsHelper.getDebugBarPosition();
         canvasDebug.setOrientation(pos);
         //sets the header with no specific sizer. ViewPane sets its own.
         topoViewDrawableRoot.setHeader(canvasDebug, C.POS_0_TOP, ITechViewPane.PLANET_MODE_0_EAT);
      } else {
         if (canvasDebug != null) {
            //remove it from
            topoViewDrawableRoot.removeDrawable(canvasDebug);
         }
         canvasDebug = null;
      }
   }

   /**
    * User can manually switch the paint mode. This commands will be 
    * executed just before the repaint of the command
    * <br>
    * This method MUST be called in the rendering thread.
    * is scheduled to run in the Rendering thread.
    * TODO what thread ?
    * @param mode
    */
   public void setPaintMode(int mode) {
      //#debug
      checkRenderThread();

      int w = vcRoot.getWidth();
      int h = vcRoot.getHeight();
      destGraphicsX.setPaintMode(mode, w, h);
   }

   public void setStyleAdapter(StyleAdapter styleAdapter) {
      this.styleAdapter = styleAdapter;
   }

   /**
    * Updates the dimension of the root {@link ViewContext} and invalidate all the layouts,
    * forcing a new layout next draw.
    * 
    * Adapt the call if screen is rotated
    * Adapt font ratios
    */
   public void sizeChanged(int w, int h) {
      //#debug
      toDLog().pEvent("w=" + w + " h=" + h + " getWidth=" + getWidth() + " getHeight=" + getHeight(), null, CanvasAppliDrawable.class, "sizeChanged");

      vcRoot.setHeight(h);
      vcRoot.setWidth(w);
      //no need to invalidate,, since we are in manual override
      //vcRoot.layoutInvalidateSize();

      //
      int canvasW = getWidth();
      int canvasH = getHeight();
      //deal with the rotation state
      if (screenConfig == ITechCanvasDrawable.SCREEN_2_LEFT_ROTATED || screenConfig == ITechCanvasDrawable.SCREEN_3_RIGHT_ROTATED) {
         canvasW = getHeight();
         canvasH = getWidth();
      }
      //TODO update the size of fonts when fonts are linked to ratio.. Root VC 10 fonts.

      //the appli defined a font screen ratio?
      int fontScreenRatio = settingsHelper.getFontScreenRatio();
      if (fontScreenRatio != 0) {
         gc.getCDC().getFontFactory().setFontRatio(fontScreenRatio, canvasH);
      }

      //invalidate all the layout
      layoutInvalidate();

      //#debug
      toDLog().pInit("End: w=" + getWidth() + " h=" + getHeight(), null, CanvasAppliDrawable.class, "initCanvasSize@line721");

      this.repaint();
   }

   public void doThreadGUI() {
      if (!isThreadRender()) {
         //TODO debug current thread
         //#debug
         toDLog().pInit("Not Gui Thread " + Thread.currentThread(), null, CanvasAppliDrawable.class, "threadGui");
         throw new RuntimeException();
      }
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, CanvasAppliDrawable.class);
      dc.appendVarWithSpace("IsPainting", isPainting);
      dc.appendColorWithSpace("EraseColor", backgroundColor);
      dc.appendVarWithSpace("cmdProcessingMode", cmdProcessingMode);
      super.toString(dc.sup());
      dc.nlLvl(vcRoot, "viewContextRoot");
      dc.nlLvl(vcContent, "viewContextAppli");
      dc.nlLvl(canvasDebug, "canvasDebug");
      dc.nlLvl(cmdMenuBar, "cmdMenuBar");
      dc.nlLvl(topoViewDrawableRoot, "topoViewDrawableRoot");
      dc.nlLvl("ForegroundQueues", foregroundQueues);
      dc.nlLvl(destGraphicsX, "destGraphicsX");
      dc.nlLvl(renderMetrics, "renderMetrics");
      dc.nlLvl(styleAdapter, StyleAdapter.class);
   }

   /**
    * Called when  {@link Dctx} see the same object for another time
    * @param dc
    */
   public void toString1Line(Dctx dc) {
      dc.root1Line(this, CanvasAppliDrawable.class);
   }
   //#enddebug

}
