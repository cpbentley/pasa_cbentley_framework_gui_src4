package pasa.cbentley.framework.gui.src4.ctx;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.ABOCtx;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IConfigBO;
import pasa.cbentley.byteobjects.src4.ctx.IStaticIDsBO;
import pasa.cbentley.core.src4.ctx.CtxManager;
import pasa.cbentley.core.src4.ctx.ICtx;
import pasa.cbentley.core.src4.ctx.IStaticIDs;
import pasa.cbentley.core.src4.event.EventBusArray;
import pasa.cbentley.core.src4.event.IEventBus;
import pasa.cbentley.core.src4.i8n.IStringProducer;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.stator.IStatorFactory;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.cmd.src4.ctx.IStaticIDsCmd;
import pasa.cbentley.framework.core.src4.app.IAppli;
import pasa.cbentley.framework.core.src4.ctx.CoreFrameworkCtx;
import pasa.cbentley.framework.core.src4.interfaces.ITechHostCore;
import pasa.cbentley.framework.coredata.src4.ctx.CoreDataCtx;
import pasa.cbentley.framework.coredraw.src4.ctx.CoreDrawCtx;
import pasa.cbentley.framework.coreui.src4.ctx.CoreUiCtx;
import pasa.cbentley.framework.datamodel.src4.ctx.DataModelCtx;
import pasa.cbentley.framework.drawx.src4.ctx.DrwCtx;
import pasa.cbentley.framework.gui.src4.anim.AnimFactory;
import pasa.cbentley.framework.gui.src4.anim.AnimOperator;
import pasa.cbentley.framework.gui.src4.canvas.CanvasAppliInputGui;
import pasa.cbentley.framework.gui.src4.canvas.CanvasDrawControl;
import pasa.cbentley.framework.gui.src4.canvas.FocusCtrl;
import pasa.cbentley.framework.gui.src4.canvas.IBOCanvasAppliGui;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.canvas.StatorFactoryDrawable;
import pasa.cbentley.framework.gui.src4.canvas.TopLevelCtrl;
import pasa.cbentley.framework.gui.src4.canvas.ViewContext;
import pasa.cbentley.framework.gui.src4.cmd.CmdMapperGui;
import pasa.cbentley.framework.gui.src4.cmd.CommanderGui;
import pasa.cbentley.framework.gui.src4.cmd.ViewCommandListener;
import pasa.cbentley.framework.gui.src4.core.DefaultStyles;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.DrawableRepo;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.core.StyleManager;
import pasa.cbentley.framework.gui.src4.core.UserInteraction;
import pasa.cbentley.framework.gui.src4.core.UserInteractionCtrl;
import pasa.cbentley.framework.gui.src4.ctx.config.IConfigAppGui;
import pasa.cbentley.framework.gui.src4.factories.CellPolicyFactory;
import pasa.cbentley.framework.gui.src4.factories.DrawableFactory;
import pasa.cbentley.framework.gui.src4.factories.DrawableOperator;
import pasa.cbentley.framework.gui.src4.factories.DrawableStringOperator;
import pasa.cbentley.framework.gui.src4.factories.StringDrawableFactory;
import pasa.cbentley.framework.gui.src4.factories.TableGeneticsFactory;
import pasa.cbentley.framework.gui.src4.factories.TablePolicyFactory;
import pasa.cbentley.framework.gui.src4.forms.FormFactory;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsView;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IUIView;
import pasa.cbentley.framework.gui.src4.mui.AppliGui;
import pasa.cbentley.framework.gui.src4.mui.MLogViewer;
import pasa.cbentley.framework.gui.src4.mui.StyleAdapter;
import pasa.cbentley.framework.gui.src4.nav.Navigator;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;
import pasa.cbentley.framework.gui.src4.string.StringEditControl;
import pasa.cbentley.framework.gui.src4.table.CellPolicy;
import pasa.cbentley.framework.gui.src4.table.TableOperator;
import pasa.cbentley.framework.gui.src4.utils.StatusData;
import pasa.cbentley.framework.input.src4.ctx.IBOCtxSettingsInput;
import pasa.cbentley.framework.input.src4.ctx.IBOTypesInput;
import pasa.cbentley.framework.input.src4.ctx.InputCtx;
import pasa.cbentley.framework.input.src4.interfaces.IBOCanvasAppli;
import pasa.cbentley.layouter.src4.ctx.LayouterCtx;

/**
 * 
 * Drawable toolkit module for building GUI applications
 * 
 * Merge {@link DrwCtx}, {@link InputCtx} and {@link CmdCtx} to create a GUI Tookit to build
 * rich, visual, responsive applications
 * 
 * <p>
 * <li> {@link Drawable}
 * <li> {@link StyleClass} defines how to draw the Drawable object
 * <li> {@link StringDrawable}
 * </p>
 * Is there one {@link GuiCtx} for every canvas? If not, we need a CanvasCtx
 * 
 * 
 * Appli -> Une surface 
 * Appli can have several surfaces/canvas
 * 
 * A {@link IDrawable} belongs to a surface
 * By default, parent is the 1 first surface of {@link GuiCtx}
 * 
 * A {@link GuiCtx} knows about the surfaces (frames provided by the host, if able to do it)
 * 
 * isMultiSurface
 * 
 * @author Charles Bentley
 *
 */
public class GuiCtx extends ABOCtx implements ITechCtxSettingsAppGui {

   public static final int          MODULE_ID = 13;

   private AnimFactory              animFactory;

   private AnimOperator             animOp;

   private BOModuleGui              boModule;

   private int                      canvasCount;

   private CanvasGuiCtx             canvasCtxRoot;

   private CanvasGuiCtx[]           canvasCtxs;


   protected final CmdCtx           cc;

   private CellPolicy               cellPolicy;

   private CellPolicyFactory        cellPolicyFactory;

   protected final CoreFrameworkCtx cfc;

   private StyleClass[]             classes;

   private CmdMapperGui             cmdMapperGui;

   private CommanderGui             commander;

   protected final IConfigAppGui    config;

   private MLogViewer               ctxLogger;

   protected final DrwCtx           dc;

   private StyleClass               defaults;

   protected final DataModelCtx     dmc;

   private DrawableFactory          drawableCoreFactory;

   private DrawableOperator         drawableOperator;

   private DrawableStringOperator   drawableStrOp;

   private IEventBus                eventBus;

   private FocusCtrl                focusCtrl;

   private FormFactory              formFactory;

   protected final InputCtx         ic;

   /**
    * There is one per {@link GuiCtx}
    * 
    * but not one per canvasctx
    */
   private StringEditControl        me;

   private Navigator                navigator;

   protected final DrawableRepo     repo;

   /**
    * Supposedly only one status
    */
   private StatusData               status;

   private StringDrawableFactory    stringFactory;

   protected final IStringProducer  strings;

   private StyleAdapter             styleAdapterDef;

   private DefaultStyles            styleFactory;

   private StyleManager             styleManager;

   private TableGeneticsFactory     tableGenes;

   private TableOperator            tableOperator;

   private TablePolicyFactory       tablePolicyFactory;

   private TopLevelCtrl             topLevelCtrl;

   private UserInteractionCtrl      uiaCtrl;

   private ViewCommandListener      vcl;

   private ViewCommandListener      viewCommandListener;

   private SettingsWrapperGui       wrapper;

   private StatorFactoryDrawable    statorFactory;

   public GuiCtx(IConfigAppGui config, CoreFrameworkCtx cfc, InputCtx ic, CmdCtx cc, BOCtx boc, DrwCtx dc, DataModelCtx dmc, IStringProducer strings) {
      super(config, boc);
      this.config = config;
      toStringSetToStringFlag(config.getFlagsGui());
      this.cfc = cfc;
      this.ic = ic;
      this.cc = cc;
      this.dc = dc;
      this.dmc = dmc;
      this.strings = strings;
      boModule = new BOModuleGui(this);
      repo = new DrawableRepo(this);
      styleFactory = new DefaultStyles(this);

      statorFactory = new StatorFactoryDrawable(this);
      animFactory = new AnimFactory(this);

      CtxManager c = uc.getCtxManager();

      c.registerStaticRange(this, IStaticIDsBO.SID_BYTEOBJECT_TYPES, IBOTypesGui.SID_VIEWTYPE_A, IBOTypesGui.SID_VIEWTYPE_Z);

      c.registerStaticRange(this, IStaticIDs.SID_DIDS, IToStringsDIDGui.A_DID_OFFSET_A_GUI, IToStringsDIDGui.A_DID_OFFSET_Z_GUI);
      c.registerStaticRange(this, IStaticIDsCmd.SID_COMMANDS, ICmdsView.A_SID_VCMD_A, ICmdsView.A_SID_VCMD_Z);
      c.registerStaticRange(this, IStaticIDs.SID_STRINGS, IStringsGui.AZ_STR_VIEW_A, IStringsGui.AZ_STR_VIEW_Z);

      strings.loads(this, IStringsGui.STR_VIEW_FILE);

      int[] eventsTolopogy = new int[IEventsGui.PID_NUM];
      eventsTolopogy[IEventsGui.CHAR_EVENT_PROD_ID] = IEventsGui.CHAR_EVENT_NUM_MAX;
      eventBus = new EventBusArray(uc, this, eventsTolopogy);

      canvasCtxs = new CanvasGuiCtx[2];
      focusCtrl = new FocusCtrl(this);
      uiaCtrl = new UserInteractionCtrl(this);

      if (this.getClass() == GuiCtx.class) {
         a_Init();
      }
   }

   public IStatorFactory getStatorFactory() {
      return statorFactory;
   }

   public void a_Init() {
      super.a_Init();
   }

   public void addCanvas(CanvasGuiCtx canvas) {
      if (canvasCtxRoot == null) {
         canvasCtxRoot = canvas;
      }
      canvasCtxs = ensureCapacity(canvasCtxs, canvasCount + 1);
      this.canvasCtxs[canvasCount] = canvas;
      canvasCount++;
   }

   protected void applySettings(ByteObject settingsNew, ByteObject settingsOld) {
      //#debug
      toDLog().pFlow("", null, GuiCtx.class, "applySettings", LVL_04_FINER, true);

   }

   /**
    * {@link IBOCanvasAppliGui}
    * @return
    */
   public ByteObject createTechCanvasAppliDrawableDefault() {
      int type = IBOCanvasAppli.CANVAS_APP_BASE_TYPE;
      int size = IBOCanvasAppliGui.CANVAS_APP_DRW_BASIC_SIZE;
      ByteObject tech = cfc.getBOC().getByteObjectFactory().createByteObject(type, size);

      int subType = IBOCanvasAppliGui.CANVAS_APP_TYPE_SUB_GUI;
      tech.set1(IBOCanvasAppli.CANVAS_APP_OFFSET_02_TYPE_SUB1, subType);

      ic.setTechCanvasAppliDefault(tech);
      //maps from config defaults

      setTechCanvasAppliDrawableDefault(tech);

      return tech;
   }

   public CanvasGuiCtx[] ensureCapacity(CanvasGuiCtx[] ar, int size) {
      if (size >= ar.length) {
         CanvasGuiCtx[] nar = new CanvasGuiCtx[size + 1];
         for (int i = 0; i < ar.length; i++) {
            nar[i] = ar[i];
         }
         ar = nar;
      }
      return ar;
   }

   public void exitInputContext() {
      canvasCtxRoot.exitInputContext();
   }

   public AnimFactory getAnimFactory() {
      return animFactory;
   }

   public AnimOperator getAnimOperator() {
      if (animOp == null) {
         animOp = new AnimOperator(this);
      }
      return animOp;
   }

   /**
    * {@link AppliGui} of type {@link IAppli}.. 
    * @return
    */
   public AppliGui getAppli() {
      return (AppliGui) cfc.getCoordinator().getAppli();
   }

   public int getBOCtxSettingSize() {
      return ITechCtxSettingsAppGui.CTX_GUI_BASIC_SIZE;
   }

   public BOModuleGui getBOMod() {
      return boModule;
   }

   /**
    * The first canvas created or set as root
    * @return
    */
   public CanvasGuiCtx getCanvasGCRoot() {
      return canvasCtxRoot;
   }

   public CanvasAppliInputGui getCanvasRoot() {
      if (canvasCtxRoot == null) {
         return null;
      }
      return canvasCtxRoot.getCanvas();
   }

   public CmdCtx getCC() {
      return cc;
   }

   public CoreDrawCtx getCDC() {
      return ic.getCUC().getCDC();
   }

   public CellPolicy getCellPolicy() {
      if (cellPolicy == null) {
         cellPolicy = new CellPolicy(this);
      }
      return cellPolicy;
   }

   public CellPolicyFactory getCellPolicyC() {
      if (cellPolicyFactory == null) {
         cellPolicyFactory = new CellPolicyFactory(this);
      }
      return cellPolicyFactory;
   }

   public CoreFrameworkCtx getCFC() {
      return ic.getCFC();
   }

   /**
    * Returns the ID {@link StyleClass}.
    * <br>
    * <br>
    * This is the preferred way for code to get their style class for application level items.
    * <br>
    * <br>
    * The Theming constructs the {@link StyleClass} array.
    * <br>
    * <br>
    * When ID is too big for the classes, method returns the root style class.
    * <br>
    * @param id
    * @return never null.
    * @throws 
    */
   public StyleClass getClass(int i) {
      if (i >= classes.length) {
         //#debug
         toDLog().pInit1("Class Style ID not valid " + i, null, GuiCtx.class, "getClass");
         //#debug
         if (uc.getConfigU().isForceExceptions()) {
            throw new IllegalStateException("Style classes not initialized to support id " + i);
         }
         return classes[0];
      }
      return classes[i];
   }

   public StyleClass[] getClasses() {
      return classes;
   }

   public CmdMapperGui getCmdMapperGui() {
      if (cmdMapperGui == null) {
         cmdMapperGui = new CmdMapperGui(this);
      }
      return cmdMapperGui;
   }

   public CommanderGui getCommanderGui() {
      if (commander == null) {
         commander = new CommanderGui(this);
      }
      return commander;
   }

   public CoreDataCtx getCoreDataCtx() {
      return dmc.getCoreDataCtx();
   }

   public int getCtxID() {
      return MODULE_ID;
   }

   public MLogViewer getCtxLogViewer() {
      return ctxLogger;
   }

   public ICtx[] getCtxSub() {
      return new ICtx[] { ic, cc, cfc };
   }

   public CoreUiCtx getCUC() {
      return ic.getCUC();
   }

   public DrwCtx getDC() {
      return dc;
   }

   public StyleClass getDefaultSC() {
      return defaults;
   }

   public DefaultStyles getDefaultStyles() {
      return styleFactory;
   }

   public DataModelCtx getDMC() {
      return dmc;
   }

   public DrawableFactory getDrawableCoreFactory() {
      if (drawableCoreFactory == null) {
         drawableCoreFactory = new DrawableFactory(this);
      }
      return drawableCoreFactory;
   }

   public DrawableOperator getDrawableOperator() {
      if (drawableOperator == null) {
         drawableOperator = new DrawableOperator(this);
      }
      return drawableOperator;
   }

   public StringDrawableFactory getDrawableStringFactory() {
      if (stringFactory == null) {
         stringFactory = new StringDrawableFactory(this);
      }
      return stringFactory;
   }

   public DrawableStringOperator getDrawableStringOperator() {
      if (drawableStrOp == null) {
         drawableStrOp = new DrawableStringOperator(this);
      }
      return drawableStrOp;
   }

   public StyleClass getEmptySC() {
      return getClass(IUIView.SC_6_EMPTY);

   }

   public IEventBus getEventsBusGui() {
      return eventBus;
   }

   public FocusCtrl getFocusCtrl() {
      return focusCtrl;
   }

   public int getFontScreenRatio() {
      return getSettingsBO().get1(CTX_GUI_OFFSET_10_FONT_SCREEN_RATIO1);
   }

   public FormFactory getFormFactory() {
      if (formFactory == null) {
         formFactory = new FormFactory(this);
      }
      return formFactory;
   }

   public InputCtx getIC() {
      return ic;
   }

   public LayouterCtx getLAC() {
      return dc.getLAC();
   }

   public Navigator getNavigator() {
      if (navigator == null) {
         navigator = new Navigator(this);
      }
      return navigator;
   }

   public int getPaintMode() {
      return getSettingsBO().get1(CTX_GUI_OFFSET_09_PAINT_MODE1);
   }

   public DrawableRepo getRepo() {
      return repo;
   }

   public SettingsWrapperGui getSettingsWrapper() {
      if (wrapper == null) {
         wrapper = new SettingsWrapperGui(this);
      }
      return wrapper;
   }

   public StatusData getStatus() {
      if (status == null) {
         status = new StatusData(this);
      }
      return status;
   }

   /**
    * Non null init {@link StringEditControl} layouted.
    * <br>
    * <br>
    * When null, initialize the control with the default style class loaded on public field {@link StringEditControl#defaultSC}
    * <br>
    * <br>
    * 
    * @return
    */
   public StringEditControl getStrEditCtrl() {
      if (me == null) {
         StyleClass sc = getClass(IUIView.SC_0_BASE_TABLE).getStyleClass(IBOTypesGui.LINK_90_STRING_EDIT_CONTROL);
         me = new StringEditControl(this, sc);
         me.init(0, -1);
      }
      return me;
   }

   public IStringProducer getStrings() {
      return strings;
   }

   public StyleAdapter getStyleAdapterDefault() {
      if (styleAdapterDef == null) {
         styleAdapterDef = new StyleAdapter(this);
      }
      return styleAdapterDef;
   }

   public StyleManager getStyleManager() {
      if (styleManager == null) {
         styleManager = new StyleManager(this);
      }
      return styleManager;
   }

   public TableGeneticsFactory getTableGeneticsC() {
      if (tableGenes == null) {
         tableGenes = new TableGeneticsFactory(this);
      }
      return tableGenes;
   }

   public TableOperator getTableOperator() {
      if (tableOperator == null) {
         tableOperator = new TableOperator(this);
      }
      return tableOperator;
   }

   public TablePolicyFactory getTablePolicyC() {
      if (tablePolicyFactory == null) {
         tablePolicyFactory = new TablePolicyFactory(this);
      }
      return tablePolicyFactory;
   }

   /**
    * Create 
    * @return
    */
   public TopLevelCtrl getTopLvl() {
      //#mdebug
      if (topLevelCtrl == null) {
         throw new NullPointerException();
      }
      //#enddebug
      return topLevelCtrl;
   }

   /**
    * 
    * @param uiaid
    * @return
    */
   public UserInteraction getUIA(int uiaid) {
      return uiaCtrl.getUIA(uiaid);
   }

   public UserInteractionCtrl getUIACtrl() {
      return uiaCtrl;
   }

   private ViewContext vcGhost;

   /**
    * Allows the creation of Drawable without a real Canvas but a Ghost canvas
    * 
    * to set a canvas, {@link Drawable#setViewContext(ViewContext)}
    * 
    * @return
    */
   public ViewContext getVCGhost() {
      return vcGhost;
   }

   public ViewCommandListener getViewCommandListener() {
      if (vcl == null) {
         vcl = new ViewCommandListener(this);
      }
      return vcl;
   }

   public boolean isOneThumb() {
      return getSettingsBO().hasFlag(CTX_GUI_OFFSET_01_FLAG1, CTX_GUI_FLAG_3_ONE_THUMB);
   }

   protected void matchConfig(IConfigBO config, ByteObject settings) {
      IConfigAppGui configG = (IConfigAppGui) config;
      settings.setFlag(CTX_GUI_OFFSET_01_FLAG1, CTX_GUI_FLAG_3_ONE_THUMB, configG.isOneThumb());
      settings.setFlag(CTX_GUI_OFFSET_01_FLAG1, CTX_GUI_FLAG_2_USER_MENU_BAR, configG.isUsingMenuBar());

      settings.set1(CTX_GUI_OFFSET_02_VIEW_THEME_ID2, configG.getThemeID());

      settings.set1(CTX_GUI_OFFSET_04_MENU_BAR_POSITION1, configG.getMenuBarPosition());
      settings.set1(CTX_GUI_OFFSET_05_DEBUG_BAR_POSITION1, configG.getDebugBarPosition());
      settings.set1(CTX_GUI_OFFSET_07_DEBUG_MODE1, configG.getDebugMode());

   }

   /**
    * Entry point for {@link GuiCtx} {@link Runnable}
    * @param gotorunnable
    */
   public void runAsWorker(Runnable runnable) {
      uc.getWorkerThread().addToQueue(runnable);
   }

   /**
    * Inverse of {@link GuiCtx#getCanvasGCRoot()}
    * @param canvasGC
    */
   public void setCanvasGCRoot(CanvasGuiCtx canvasGC) {
      this.canvasCtxRoot = canvasGC;
   }

   /**
    * This method must be called before
    * 
    * In the exceptional case that 2 canvas want different themes, they will need different {@link GuiCtx}
    * @param roots
    */
   public void setClasses(StyleClass[] roots) {
      classes = roots;
      defaults = roots[0];
   }

   public void setCtxLogViewer(MLogViewer ctxLogger) {
      this.ctxLogger = ctxLogger;
   }

   public void setStyleManager(StyleManager styleManager) {
      this.styleManager = styleManager;
   }

   public void setTechCanvasAppliDrawableDefault(ByteObject bo) {

      ByteObject settingsBO = getSettingsBO();
      int debugBarPosition = settingsBO.get1(ITechCtxSettingsAppGui.CTX_GUI_OFFSET_05_DEBUG_BAR_POSITION1);
      bo.set1(IBOCanvasAppliGui.CANVAS_APP_DRW_OFFSET_05_DEBUG_BAR_POSITION1, debugBarPosition);

      int debugMode = settingsBO.get1(ITechCtxSettingsAppGui.CTX_GUI_OFFSET_07_DEBUG_MODE1);
      bo.set1(IBOCanvasAppliGui.CANVAS_APP_DRW_OFFSET_07_DEBUG_MODE1, debugMode);

      int menuBarPosition = settingsBO.get1(ITechCtxSettingsAppGui.CTX_GUI_OFFSET_04_MENU_BAR_POSITION1);
      bo.set1(IBOCanvasAppliGui.CANVAS_APP_DRW_OFFSET_04_MENU_BAR_POSITION1, menuBarPosition);

      int menuBarType = settingsBO.get1(ITechCtxSettingsAppGui.CTX_GUI_OFFSET_06_MENU_BAR_TYPE1);
      bo.set1(IBOCanvasAppliGui.CANVAS_APP_DRW_OFFSET_06_MENU_BAR_TYPE1, menuBarType);

      boolean isOneThumb = settingsBO.hasFlag(ITechCtxSettingsAppGui.CTX_GUI_OFFSET_01_FLAG1, ITechCtxSettingsAppGui.CTX_GUI_FLAG_3_ONE_THUMB);
      bo.setFlag(IBOCanvasAppliGui.CANVAS_APP_DRW_OFFSET_01_FLAG1, IBOCanvasAppliGui.CANVAS_APP_DRW_FLAG_3_ONE_THUMB, isOneThumb);

      boolean isUseMenuBar = settingsBO.hasFlag(ITechCtxSettingsAppGui.CTX_GUI_OFFSET_01_FLAG1, ITechCtxSettingsAppGui.CTX_GUI_FLAG_2_USER_MENU_BAR);
      bo.setFlag(IBOCanvasAppliGui.CANVAS_APP_DRW_OFFSET_01_FLAG1, IBOCanvasAppliGui.CANVAS_APP_DRW_FLAG_4_USE_MENU_BAR, isUseMenuBar);

   }

   public void showIndependant(InputConfig ic, IDrawable drawable) {
      if (getCFC().getHostCore().hasFeatureSupport(ITechHostCore.SUP_ID_24_MULTIPLE_WINDOWS)) {
         ByteObject techCanvasHost = getCUC().createBOCanvasHostDefault();
         ByteObject techCanvasAppli = createTechCanvasAppliDrawableDefault();

         CanvasAppliInputGui canvas = new CanvasAppliInputGui(this, techCanvasAppli, techCanvasHost);
         //link to this canvas view 
         drawable.setViewContext(canvas.getVCAppli());

         //show it on a screen area not already covered by a Canvas?
         canvas.getCanvasHost().setDefaultStartPosition();
         canvas.getCanvasHost().canvasShow();
      }

      //show it over
      ViewContext viewContext = drawable.getViewContext();
      CanvasDrawControl drawCtrlAppli = viewContext.getDrawCtrlAppli();
      drawCtrlAppli.shShowDrawableOver(ic, drawable);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, GuiCtx.class, 666);
      toStringPrivate(dc);
      dc.nlAppend("DID Range");
      dc.append(IToStringsDIDGui.A_DID_OFFSET_A_GUI);
      dc.append(" ");
      dc.append(IToStringsDIDGui.A_DID_OFFSET_Z_GUI);
      //
      dc.nlAppend("Type Range");
      dc.append(IBOTypesGui.SID_VIEWTYPE_A);
      dc.append(" ");
      dc.append(IBOTypesGui.SID_VIEWTYPE_Z);
      super.toString(dc.sup());
      dc.nlLvl(config, IConfigAppGui.class);
      dc.nlLvlCtx(cc, CmdCtx.class);
      dc.nlLvlCtx(ic, InputCtx.class);
      dc.nlLvlCtx(dmc, DataModelCtx.class);
      dc.nlLvlCtx(this.dc, DrwCtx.class);
      dc.nlLvlCtx(cfc, CoreFrameworkCtx.class);

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "GuiCtx");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   public void toStringCtxSettings(Dctx dc, ByteObject bo) {
      dc.root(bo, "CtxSettingsGuiCtx");
      dc.appendVarWithSpace("viewthemeid", bo.get2(ITechCtxSettingsAppGui.CTX_GUI_OFFSET_02_VIEW_THEME_ID2));
      dc.appendVarWithSpace("DebugMode", ToStringStaticGui.toStringDebugMode(bo.get1(ITechCtxSettingsAppGui.CTX_GUI_OFFSET_07_DEBUG_MODE1)));

      super.toStringCtxSettings(dc.sup(), bo);
   }

   public void toStringGuiCtxTech(Dctx dc, ByteObject bo) {
      int id = bo.get3(CTX_OFFSET_03_CTXID_3);
      if (id == GuiCtx.MODULE_ID) {
         dc.nlVar("ThemeID", bo.get2(CTX_GUI_OFFSET_02_VIEW_THEME_ID2));
         dc.nlVar("MenuBarPos", bo.get1(CTX_GUI_OFFSET_04_MENU_BAR_POSITION1));
      }
   }

   public void toStringGuiCtxTech1Line(Dctx dc, ByteObject bo) {
      int id = bo.get3(CTX_OFFSET_03_CTXID_3);
      if (id == GuiCtx.MODULE_ID) {
         dc.nlVar("ThemeID", bo.get2(CTX_GUI_OFFSET_02_VIEW_THEME_ID2));
         dc.nlVar("MenuBarPos", bo.get1(CTX_GUI_OFFSET_04_MENU_BAR_POSITION1));
      }
   }

   private void toStringPrivate(Dctx dc) {

   }

   //#enddebug

}
