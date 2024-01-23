package pasa.cbentley.framework.gui.src4.mui;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.stator.StatorReader;
import pasa.cbentley.core.src4.stator.StatorWriter;
import pasa.cbentley.core.src4.structs.IntToObjects;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.core.src4.app.AppCtx;
import pasa.cbentley.framework.core.src4.app.AppliAbstract;
import pasa.cbentley.framework.core.src4.app.IBOCtxSettingsAppli;
import pasa.cbentley.framework.coreui.src4.interfaces.ICanvasAppli;
import pasa.cbentley.framework.coreui.src4.tech.IBOCanvasHost;
import pasa.cbentley.framework.coreui.src4.utils.ViewState;
import pasa.cbentley.framework.gui.src4.canvas.CanvasAppliDrawable;
import pasa.cbentley.framework.gui.src4.canvas.CanvasResultDrawable;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.canvas.InputStateDrawable;
import pasa.cbentley.framework.gui.src4.canvas.TopLevelCtrl;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceDrawable;
import pasa.cbentley.framework.gui.src4.cmd.MCmdGuiNewStart;
import pasa.cbentley.framework.gui.src4.cmd.ViewCommandListener;
import pasa.cbentley.framework.gui.src4.core.IDToDrawable;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.CanvasGuiContext;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.config.IConfigAppGui;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsView;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IUIView;

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

   protected final GuiCtx      gc;

   private boolean             isInit = false;

   private CanvasAppliDrawable root0;

   private CanvasAppliDrawable root1;

   /**
    * Sanity checks that sub classes have called every method
    */
   private int                 verifFlag;

   /**
    * Create an {@link AppliGui} givens the {@link IFrameworkCtx} framework context.
    * <br>
    * <br>
    * Creates the {@link GuiContext} that will control  the module
    * @param dd
    */
   public AppliGui(GuiCtx gc, AppCtx apc) {
      super(apc);
      this.gc = gc;
   }

   /**
    * Chance for sub class to do some stuff after {@link CanvasAppliDrawable} was created.
    * @param cv
    */
   protected void canvasCreated(CanvasAppliDrawable cv) {

   }

   public void cmdStart(CmdInstanceDrawable cd) {

   }

   public IDToDrawable createIDMapper() {
      return new IDToDrawable(gc);
   }

   /**
    * Recreates Canvas based on the serialized state
    * @param id
    * @param tech
    * @return
    */
   public ICanvasAppli getCanvas(int id, ByteObject tech) {
      if (tech == null) {
         return getCanvasDefault();
      } else {
         if (id <= 0) {
            if (root0 == null) {
               //there can only be one root?
               root0 = new CanvasAppliDrawable(gc, tech);
               return root0;
            } else {
               //throw new IllegalStateException("Root0 is already created");
               return null;
            }
         } else if (id == 1) {
            if (root1 == null) {
               //there can only be one root?
               root1 = new CanvasAppliDrawable(gc, tech);
               return root1;
            } else {
               return null;
               //throw new IllegalStateException("Root1 is already created");
            }
         } else if (id == 2) {
            //this canvas... how do distinguish from several?
            CanvasAppliDrawable ide = new CanvasAppliDrawable(gc, tech);
            if (root1 == null) {
               throw new IllegalStateException();
            }
            canvasExtras.add(ide);
         }
         return null;
      }
   }

   /**
    * 
    */
   private ICanvasAppli getCanvasDefault() {
      if (root0 == null) {
         //do we create it here? we load it from save state. the AppManager will look up the Canvas ID in the current screen config
         ByteObject techCanvas = gc.getCUC().createBOCanvasHostDefault();
         techCanvas.set2(IBOCanvasHost.TCANVAS_OFFSET_03_ID2, IBOCanvasHost.TCANVAS_ID_1_ROOT);
         //title and icon are taken from the LaunchValues no.
         //they are localized
         techCanvas.set2(IBOCanvasHost.TCANVAS_OFFSET_07_ICON_ID2, 0);
         techCanvas.set2(IBOCanvasHost.TCANVAS_OFFSET_08_TITLE_ID_ID2, 0);
         //TODO problem when root. root MUST always be shown on start up ?
         root0 = (CanvasAppliDrawable) getCanvas(1, techCanvas);
      }
      return root0;
   }

   /**
    * Populate the {@link StyleClass} with {@link StyleClass#setClasses(StyleClass[])}.
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
    * <li> {@link ViewCommandListener} state
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

      vs.add(gc.getViewCommandListener().getViewState());
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
      InputConfig ic = null;
      IDrawable firstD = getFirstDrawable();
      if (firstD != null) {
         firstD.shShowDrawableOver(ic);
      }
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

   protected void subAppVersionChange() {
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
    * Not started in headless mode, console only.
    * <br>
    * <br>
    * {@link GuiCtx} code is using {@link CmdCtx} paradigm, so everything action is a command.
    * {@link MCmdGuiNewStart}
    * 
    * {@link AppliAbstract#amsAppStart} has been called already.
    * 
    * Decides what kind of MasterCanvas to use. Basic or with Menu or something else.
    */
   protected void subAppStarted() {

      CanvasAppliDrawable canvasView = gc.getCanvasRoot();
      InputStateDrawable isd = (InputStateDrawable) canvasView.getEvCtrl().getState();
      CanvasResultDrawable srd = canvasView.getRepaintCtrlDraw().getSD();
      InputConfig ic = new InputConfig(gc, canvasView, isd, srd);

      IDrawable draw = getFirstDrawable();

      draw.shShowDrawableOver(ic);

      //set GUI State from saved view state. send CMD App Start
      //gc.getViewCommandListener().processCmd(ICmdsView.VCMD_00_LAST_LOGIN);

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
