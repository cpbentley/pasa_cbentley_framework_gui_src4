package pasa.cbentley.framework.gui.src4.ctx.app;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.objects.color.ITechGradient;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.structs.IntToObjects;
import pasa.cbentley.core.src4.utils.interfaces.IColorsBase;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdControlled;
import pasa.cbentley.framework.core.framework.src4.app.AppCtx;
import pasa.cbentley.framework.core.framework.src4.app.AppliAbstract;
import pasa.cbentley.framework.core.ui.src4.interfaces.ICanvasAppli;
import pasa.cbentley.framework.core.ui.src4.tech.IBOCanvasHost;
import pasa.cbentley.framework.core.ui.src4.tech.ITechWrapper;
import pasa.cbentley.framework.core.ui.src4.utils.ViewState;
import pasa.cbentley.framework.gui.src4.canvas.CanvasAppliInputGui;
import pasa.cbentley.framework.gui.src4.canvas.ICanvasDrawable;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.canvas.TopLevelCtrl;
import pasa.cbentley.framework.gui.src4.cmd.CmdControlledGui;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceGui;
import pasa.cbentley.framework.gui.src4.cmd.CmdProcessorGui;
import pasa.cbentley.framework.gui.src4.cmd.mcmd.MCmdGuiNewStart;
import pasa.cbentley.framework.gui.src4.core.FigDrawable;
import pasa.cbentley.framework.gui.src4.core.IDToDrawable;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.exec.InputStateCanvasGui;
import pasa.cbentley.framework.gui.src4.exec.OutputStateCanvasGui;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsGui;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IUIView;
import pasa.cbentley.framework.gui.src4.mui.MLogViewer;

/**
 * Implemented by Modules using the MUI graphical framework to provide business
 * value using ?
 * <br>
 * Ties together a {@link GuiCtx} and an {@link AppCtx}
 * 
 * @author Charles-Philip Bentley
 *
 */
public abstract class AppliGui extends AppliAbstract {

   private IntToObjects        canvasExtras;

   private IDrawable           firstDrawableCreated;

   protected final GuiCtx      gc;

   private boolean             isInit = false;

   private CanvasAppliInputGui root0;

   private CanvasAppliInputGui root1;

   /**
    * Sanity checks that sub classes have called every method
    */
   private int                 verifFlag;

   /**
    * Create an {@link AppliGui} as an {@link ICmdControlled}.
    * 
    * @param gc {@link GuiCtx}
    * @param apc {@link AppCtx}
    */
   public AppliGui(GuiCtx gc, AppCtx apc) {
      super(apc);
      this.gc = gc;
      
      ICmdControlled cced = createCmdControlled();
      
      gc.getCC().setCmdControlled(cced);
      
      //#debug
      toDLog().pCreate("", this, AppliGui.class, "Created@75", LVL_04_FINER, true);

   }
   
   /**
    * Override this for a more specific {@link ICmdControlled}
    * 
    * @return {@link ICmdControlled}
    * 
    * @see CmdControlledGui
    */
   protected ICmdControlled createCmdControlled() {
      return new CmdControlledGui(gc);
   }

   /**
    * Chance for sub class to do some stuff after {@link CanvasAppliInputGui} was created.
    * @param cv
    */
   protected void canvasCreated(CanvasAppliInputGui cv) {

   }

   public void cmdStart(CmdInstanceGui cd) {

   }

   public ICanvasAppli createCanvas(int id, ByteObject techCanvasHost, Object params) {
      if (techCanvasHost == null) {
         return getCanvasDefault();
      } else {
         if (id <= 0) {
            if (root0 == null) {
               //there can only be one root?
               root0 = new CanvasAppliInputGui(gc, techCanvasHost);
               return root0;
            } else {
               //throw new IllegalStateException("Root0 is already created");
               return null;
            }
         } else if (id == 1) {
            if (root1 == null) {
               //there can only be one root?
               root1 = new CanvasAppliInputGui(gc, techCanvasHost);
               return root1;
            } else {
               return null;
               //throw new IllegalStateException("Root1 is already created");
            }
         } else if (id == 2) {
            //this canvas... how do distinguish from several?
            CanvasAppliInputGui ide = new CanvasAppliInputGui(gc, techCanvasHost);
            if (root1 == null) {
               throw new IllegalStateException();
            }
            canvasExtras.add(ide);
         }
         return null;
      }
   }

   public IDToDrawable createIDMapper() {
      return new IDToDrawable(gc);
   }

   /**
    * 
    */
   private ICanvasAppli getCanvasDefault() {
      if (root0 == null) {
         //do we create it here? we load it from save state. the AppManager will look up the Canvas ID in the current screen config
         ByteObject techCanvasHost = gc.getCUC().createBOCanvasHostDefault();
         techCanvasHost.set2(IBOCanvasHost.CANVAS_HOST_OFFSET_03_ID2, ITechWrapper.TCANVAS_ID_1_ROOT);
         //title and icon are taken from the LaunchValues no.
         //they are localized
         techCanvasHost.set2(IBOCanvasHost.CANVAS_HOST_OFFSET_07_ICON_ID2, 0);
         techCanvasHost.set2(IBOCanvasHost.CANVAS_HOST_OFFSET_08_TITLE_ID_ID2, 0);
         //TODO problem when root. root MUST always be shown on start up ?
         root0 = (CanvasAppliInputGui) createCanvas(1, techCanvasHost, null);
      }
      return root0;
   }

   /**
    * Creates the style classes used by the application.
    * 
    *  the {@link StyleClass} with {@link StyleClass#setClasses(StyleClass[])}.
    * <br>
    * <br>
    * This must be done for
    * <li>{@link IUIView#SC_0_BASE_TABLE}
    * <li>{@link IUIView#SC_1_MENU}
    * <li>{@link IUIView#SC_2_TITLE}
    * <li>{@link IUIView#SC_3_LOG}
    * 
    * @param dc
    * @return
    */
   public abstract StyleClass[] getClasses();

   public IDrawable getCreated() {
      return firstDrawableCreated;
   }

   /**
    * Return the Home Drawable of this Appli. Method will be called by the HOME command
    * Creates if necessary. The first drawable is unaware of top level logic.
    * that will be loaded with
    * <br>
    * <br>
    * @return
    */
   public abstract IDrawable getFirstDrawable();

   /**
    * Get Top Level
    * Those units have an app business meaning
    * Like Activity/Fragments/IMyGui/DrawablePageReference
    * Application provides some top level 
    * @return
    */
   public IDrawable getTopLevelDrawable(int id) {
      return getFirstDrawable();
   }

   /**
    * <li> {@link CmdProcessorGui} state
    * <li> {@link TopLevelCtrl} list of History.
    * <li> Current Drawables state? how
    * 
    * <li> Asks the {@link MLogViewer} to write down the logs.
    * The ViewState only keeps the last logs that are not saved to disk
    * Logs are regularly saved to a daily file.
    * <br>
    * 
    * @return
    */
   public ViewState getViewState() {
      ViewState vs = new ViewState();
      return vs;
   }

   /**
    * Method might be called before Controller setup.
    */
   protected void pauseApp() {
      super.amsAppPause();
      //exits the input context of the Appli
      gc.exitInputContext();

   }

   /**
    * Either set the default drawable, or set the last state drawables
    */
   protected void setFirstDrawable() {
      //which input config to use here?
      ExecutionContextCanvasGui ec = null;
      IDrawable firstD = getFirstDrawable();
      if (firstD != null) {
         firstD.shShowDrawableOver(ec);
      }
   }

   /**
    */
   protected void showCanvasDefault() {
      super.showCanvasDefault();
   }

   /**
    * Save the ViewState
    */
   protected void subAppExit() {
      //same as pausing
   }

   protected void subAppFirstLaunch() {
      //called when the app is launched first 
   }

   /**
    * Loads {@link StyleClass}.
    * 
    * subclass may also decide  to?
    * We don't know if its the first load at this stage
    * <li>
    */
   protected void subAppLoad() {

   }

   protected void subAppResumed() {
      
   }

   protected void subAppLoadPostCtxSettings() {
      //theme needs device to compute device specific
      //what is theme needs size of canvas?. theme is invaliadated and reloaded 
      StyleClass[] roots = this.getClasses();
      if (roots == null) {
         throw new NullPointerException("Implementation must define a set of Styles");
      }
      gc.setClasses(roots);
   }

   /**
    * For android see
    * https://developer.android.com/guide/components/activities/activity-lifecycle.html
    * https://developer.android.com/reference/android/app/Activity.html#SavingPersistentState
    * http://stackoverflow.com/questions/5123407/losing-data-when-rotate-screen
    * 
    */
   protected void subAppPause() {
      //TODO when pausing we save the viewstate and all settings to disk

   }

   /**
    * 
    */
   protected void subAppStarted() {

      //TODO check if started from scratch or with saved state.

      IDrawable draw = null;
      try {
         draw = getFirstDrawable();

      } catch (Exception e) {
         //
         //#debug
         toDLog().pEx("Subclass failed to create FirstDrawable", this, AppliGui.class, "subAppStarted@289", e);

         toDLog().pEx("Generating Error Drawable", null, AppliGui.class, "subAppStarted@289", e);

         int gradColor = IColorsBase.BASE_10_GREEN_DARK;
         ByteObject grad = gc.getDC().getGradientFactory().getGradient(gradColor, 50, ITechGradient.GRADIENT_TYPE_ELLIPSE_04_BOT_FLAMME);
         int color = IColorsBase.BASE_20_CORAL_LIGHT;
         ByteObject rect = gc.getDC().getFigureFactory().getFigEllipse(color, grad);

         ByteObject style = gc.getDefaultStyles().getStyle2Pad5Border1M();
         StyleClass sc = new StyleClass(gc, style);
         FigDrawable fd = new FigDrawable(gc, sc, rect);
         fd.setSizePixels(50, 70);
         fd.setXY(5, 5);
         draw = fd;

      }
      firstDrawableCreated = draw;

      ICanvasDrawable canvasRoot = gc.getCanvasRoot();

      //#debug
      toDLog().pInit("CanvasView after FirstDrawable is created", canvasRoot, AppliGui.class, "subAppStarted@289", LVL_05_FINE, false);

      //set GUI State from saved view state. send CMD App Start

      //we are still in the gui thread here
      //generate a cmd event and put it on the queue for this canvas.

      ExecutionContextCanvasGui ec = canvasRoot.createExecutionContextCmdGui();
      gc.getCC().executeInstance(MCmdGuiNewStart.CMD_ID, ec);

   }

   protected void subAppVersionChange() {
      //called when the app is launched first 
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, AppliGui.class);
      dc.rootCtx(gc, GuiCtx.class);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, AppliGui.class);
   }
   //#enddebug
}
