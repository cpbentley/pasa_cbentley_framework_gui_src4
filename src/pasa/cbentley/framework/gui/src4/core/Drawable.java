/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.ctx.ICtx;
import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.core.src4.helpers.StringBBuilder;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.core.src4.interfaces.ITechNav;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.stator.StatorReader;
import pasa.cbentley.core.src4.stator.StatorWriter;
import pasa.cbentley.core.src4.utils.BitUtils;
import pasa.cbentley.core.src4.utils.interfaces.IColors;
import pasa.cbentley.framework.cmd.src4.cmd.MCmdNav;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.engine.CmdNode;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.cmd.src4.interfaces.ITechCmd;
import pasa.cbentley.framework.core.ui.src4.input.InputState;
import pasa.cbentley.framework.core.ui.src4.utils.ViewState;
import pasa.cbentley.framework.coredraw.src4.interfaces.IMFont;
import pasa.cbentley.framework.datamodel.src4.table.ObjectTableModel;
import pasa.cbentley.framework.drawx.src4.ctx.IBOTypesDrawX;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.engine.RgbCache;
import pasa.cbentley.framework.drawx.src4.engine.RgbImage;
import pasa.cbentley.framework.drawx.src4.factories.interfaces.IBOFigure;
import pasa.cbentley.framework.drawx.src4.string.StringAuxOperator;
import pasa.cbentley.framework.drawx.src4.style.IBOStyle;
import pasa.cbentley.framework.drawx.src4.style.ITechStyleCache;
import pasa.cbentley.framework.drawx.src4.style.StyleCache;
import pasa.cbentley.framework.drawx.src4.style.StyleOperator;
import pasa.cbentley.framework.gui.src4.anim.AnimManager;
import pasa.cbentley.framework.gui.src4.anim.IBOAnim;
import pasa.cbentley.framework.gui.src4.anim.ITechAnim;
import pasa.cbentley.framework.gui.src4.anim.base.DrawableAnim;
import pasa.cbentley.framework.gui.src4.anim.base.ImgAnimable;
import pasa.cbentley.framework.gui.src4.anim.definitions.AlphaChangeRgb;
import pasa.cbentley.framework.gui.src4.anim.move.Move;
import pasa.cbentley.framework.gui.src4.canvas.CanvasAppliInputGui;
import pasa.cbentley.framework.gui.src4.canvas.CanvasDrawControl;
import pasa.cbentley.framework.gui.src4.canvas.FocusCtrl;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.canvas.RepaintHelperGui;
import pasa.cbentley.framework.gui.src4.canvas.TopologyDLayer;
import pasa.cbentley.framework.gui.src4.canvas.ViewContext;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceGui;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.ctx.IToStringFlagsDraw;
import pasa.cbentley.framework.gui.src4.ctx.IToStringFlagsGui;
import pasa.cbentley.framework.gui.src4.ctx.ObjectGC;
import pasa.cbentley.framework.gui.src4.ctx.ToStringStaticGui;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStrAuxData;
import pasa.cbentley.framework.gui.src4.interfaces.IAnimable;
import pasa.cbentley.framework.gui.src4.interfaces.ICommandDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawListener;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawableListener;
import pasa.cbentley.framework.gui.src4.interfaces.INavigational;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.tech.ITechLinks;
import pasa.cbentley.framework.gui.src4.utils.DrawableUtilz;
import pasa.cbentley.framework.gui.src4.utils.SeveralActor;
import pasa.cbentley.layouter.src4.ctx.LayouterCtx;
import pasa.cbentley.layouter.src4.engine.Area2DConfigurator;
import pasa.cbentley.layouter.src4.engine.SizerFactory;
import pasa.cbentley.layouter.src4.engine.Zer2DArea;
import pasa.cbentley.layouter.src4.interfaces.ILayoutDelegate;
import pasa.cbentley.layouter.src4.interfaces.ILayoutable;
import pasa.cbentley.layouter.src4.tech.ITechLayout;

/**
 * Root class implementing {@link IDrawable} interface. <br>
 * <br>
 * The content of a Drawable in {@link Drawable#drawDrawableContent(GraphicsX, int, int, int, int)} may be drawn by injection using the interface {@link IDrawListener} <br>
 * For this to work the parent {@link Drawable} must implements the {@link IDrawListener} interface.
 * <br>
 * <br>
 * <b>Internal Life cycle</b>:<br>
 * A drawable's purpose in life is to be drawn on the screen. <br>
 * <li>Created {@link ITechDrawable#STATE_03_HIDDEN}
 * <li>Entry Animated {@link ITechDrawable#BEHAVIOR_06_ANIMATING_ENTRY} => {@link ITechDrawable#STATE_11_ANIMATING}, {@link ITechDrawable#STATE_20_ANIMATED_FULL_HIDDEN}
 * <li>init {@link ITechDrawable#STATE_05_LAYOUTED}
 * <li> position in structure {@link IDrawable#STATE_S_04HEADED},  
 * <li>Drawn {@link ITechDrawable#STATE_02_DRAWN}
 * <li> Acted upon with user. All AS system falgs like {@link ITechDrawable#STYLE_05_SELECTED}, {@link ITechDrawable#STYLE_07_FOCUSED_POINTER}
 * <li> Animated life {@link ITechDrawable#BEHAVIOR_05_ANIMATING}
 * <li> Cached {@link ITechDrawable#STATE_14_CACHED}
 * <li> hiding {@link ITechDrawable#BEHAVIOR_07_ANIMATING_EXIT}
 * <br>
 * <br>
 * Use {@link Drawable#show(GraphicsX)} to kick start to life.
 * <br>
 * <br>
 * <b>Animations</b> : <br>
 * Animations are fired relative to events in the lifecycle
 * <br>
 * <li>Coordinated animations defined at the style level using {@link ByteObject#STYLE_FLAG_C_6_ANIM_ENTRY}.
 * <br>
 * <li>Individual animations may also be defined at the figure of style layers with {@link IDrw#FIG_FLAG_6_ANIMATED}
 * <br>
 * If bg1 and bg2 have an entry animation defined. The whole Drawable is put on animated state. It will be repainted
 * on each step of the animation. If content is heavy, content is cached.
 * bg1 moves in, it uses {@link Move}
 * <br>
 * <br>
 * <b>Cache</b> : <br>
 * Disposable {@link RgbImage}
 * <br>
 * <br>
 * 
 * <b>Case of Scrollbar drawable</b>. <br>
 * When a directional key is fired, an invisible Scrollbar is set to active state.
 * This state fires an animation whose target is the whole drawable or just a style layer.
 * <br>
 * Animation has a post action script to fire a timer.
 * When timer is not reset by a further key event, it fires an exit animation which set
 * the active state to false.
 * <br>
 * <li>An entry animation {@link ITechDrawable#BEHAVIOR_06_ANIMATING_ENTRY}
 * <li>A main animation {@link ITechDrawable#BEHAVIOR_05_ANIMATING}
 * <li>An exit animation {@link ITechDrawable#BEHAVIOR_07_ANIMATING_EXIT}
 * <br>
 * 
 * <br>
 * Drawable looks up for animated style layers.
 * If one Bg layer has an Entry animation, all Bg layers are lumped together for entry animation.
 * the ByteObject animation definition, for example RGB fade in on BG layer 1.
 * Animation works on BG layer 1 area (drawn at margin,border,padding or content).
 * Layer draws itself on RgbImage. This image is used by the {@link ImgAnimable}.
 * Repaint cycle works on Drawable. At each repaint steps, drawable is painted
 * 
 * <br>
 * <br>
 * For {@link ByteObjectMod}, the whole Drawable is drawn at each step.
 * <br>
 * <br>
 *<b> Key Event Changing States</b> <br>
 *
 * <br>
 * <br>
 *  In MIDP the closest thing to a Drawable is a Layer. In Swing, JComponent <br>
 * Style layers will be drawn if the sub class calls the draw method<br>
 * If subclass does not override drawDrawable, Drawable will draw the style completely<br>
 * <br>
 * A UI developer creates a Drawable and its content, initialize area and coordinate and call the uiShow method.
 * The show method manages system flags and flow control of the draw method. The show method handles the animation hooks<br>
 * 
 * When the drawable has been shown and must be hidden away, the UI developer calls the uiHide method<br>
 * Equivalent of a JButton? Just a StringDrawable with a pointer pressed listener cmd.
 * Trigger pointer pressed to ctx and a give cmd. Cmd execution listener may change the state of Drawable.(StringDrawable)
 * <br>
 * <br>
 * When external user wants to animate drawable, it does it outside the framework.
 * <br>
 * <br>
 * TODO 2019: With IOS experience.. how can you leverage framework for image animations such
 * as rotations? Or fast scaling for entry/exit animations
 * High Level implementation
 * or implementation with Host as a UIView with a transform.
 * There is an easy mapping between Drawable and UIView.
 * We will animate an UIImage behind a IImage? Pure Bentley scaling will be done in software.
 * The host may provides scaling and rotating functions
 * 
 * @author Charles-Philip Bentley
 *
 */
public class Drawable extends ObjectGC implements IDrawable, IBOStyle, IStringable, ILayoutable, ICommandDrawable {

   public static final int          VER_DRAWABLE_01 = 1;

   /**
    * Hosts the behavior flags of this Drawable
    */
   protected int                    behaviors;

   /**
    * {@link RgbCache} pixel level cache.
    * <br>
    * <br>
    * 
    * A cache is used for time consuming elements
    * <br>
    * <br>
    * This object is used by static caching animations like {@link AlphaChangeRgb}.
    * <li>What is Animation wants Full, and another only Content?
    * <li>When a DLayer is animating in static mode, the full cache is invalidated at each of its turn
    * <br>
    * <br>
    * {@link MemController} may flush the cache content.
    */
   protected RgbImage               cache;

   /**
    * Cache type is decided by this class when Caching behavior is set.
    * <br>
    * <br>
    * <li>{@link ITechDrawable#CACHE_0_NONE} generic all
    * <li>{@link ITechDrawable#CACHE_1_CONTENT}
    * <li>{@link ITechDrawable#CACHE_2_FULL}
    * <li>{@link ITechDrawable#CACHE_3_BG_DECO}
    * <br>
    * <br>
    * It looks at the figures of the style and ask subclass if content should be cached. 
    * <br>
    * <br>
    * This computation is an ongoing process. The value may change.
    */
   protected int                    cacheType;

   /**
    * TODO A Drawable must have a Canvas ID
    * When repainted. must know on which canvas. (similar to x and y)
    */
   protected int                    canvasID;

   /**
    * Null by construction.
    * 
    * <li>{@link Drawable#getCmdNode()}
    * <li>{@link Drawable#setCmdNode(CmdNode)}
    */
   protected CmdNode                cmdNode;

   private int                      ctrl;

   /**
    * Custom user Type, to which a specific translucent style may be linked.
    * When > 0, {@link Presentation#getCTypeStyleKey(int, int)} to look up a ctype style layer.
    * 
    * The bit numberal state 16 bits by default
    * A Table may sort Drawables according to their CType
    * 
    * It is of the style Type differentiation such as MSG_INFO, MSG_ERROR and MSG_WARNING
    * It often has a business meaning in direct relation to {@link ObjectTableModel#getCellCType(int)}
    * 
    * <p>
    * <li> {@link Drawable#getCType()}
    * <li> {@link Drawable#setCType(int)}
    * </p>
    */
   protected int                    ctype           = 0;

   /**
    * Constructed when an animation is added with
    * <br>
    * <li>{@link Drawable#addAnimationLayer(IAnimable)}
    * <li>{@link IDrawable#addFullAnimation(IAnimable)}
    */
   protected DrawableAnimator       da;

   /**
    * Read only by external world.
    * Set inside by {@link Drawable} or subclass.
    */
   protected int                    genes;

   /**
    * Defining holes in the rectangular area x,y,dw,dh
    */
   protected int[]                  holes;

   /**
    * One instance for every drawable. it contains its position and size
    */
   protected LayouterEngineDrawable layEngine;

   /**
    * Listener to events generated on this {@link Drawable}
    */
   protected IDrawableListener      pactor;

   /**
    * {@link IDrawListener} or {@link Drawable} controlling the positioning.
    * 
    * Never null. Will be {@link ViewContext}
    */
   protected IDrawable              parent;

   /**
    * Count the number of times the method draw is called.
    * The number of initialization
    */
   protected int                    statCount;

   /**
    * When created a Drawable is Hidden
    */
   protected int                    states          = STATE_03_HIDDEN;

   /**
    * 
    */
   private int                      statesNav;

   /**
    * The Draw method can only write to this field
    */
   protected volatile int           statesVolatile  = STATE_03_HIDDEN;

   /**
    * <p>
    * Incomplete style definition that will merge with root style before 
    * applying state styles.
    * </p>
    * 
    * <p>
    * Set externally by positioning layout. Must be semi-opaque classless style.
    * A TableView might set a column based style layer.
    * </p>
    * 
    * <p>
    * It is used of the style differentiation in table columns.
    * </p>
    */
   protected ByteObject             styleStruct;

   /**
    * Style active used for drawing and computing {@link Drawable}'s dimension.
    * <br>
    * <br>
    * Cannot be null
    * TODO remove. use cache
    */
   protected ByteObject             style;

   protected StyleCache             styleCache;

   /**
    * Style class can be shared between drawable, so the current style ByteObject is retrieved using a pointer handle.
    * <br>
    * This pointer handle is only valid for the given {@link StyleClass}.
    * <br>
    * Cannot be null
    *
    */
   protected StyleClass             styleClass;

   /**
    * Style History
    * null if style was never changed (set or merge)
    * Why?
    */
   protected int[]                  styleHistory;

   /**
    * States that modify the styling
    * 
    * <li>{@link ITechDrawable#STYLE_05_SELECTED}
    * <li>{@link ITechDrawable#STYLE_06_FOCUSED_KEY}
    * <li>{@link ITechDrawable#STYLE_07_FOCUSED_POINTER}
    * 
    */
   protected int                    styleStates;

   private int                      uiid;

   /**
    * Cannot be null. By default the Appli Context
    */
   protected ViewContext            vc;

   /**
    * Used for cache updates
    */
   protected int                    versioning;

   /**
    * Extra
    */
   private int                      zindex          = -1;

   /**
    * Creates a drawable that belongs to the application {@link ViewContext} of the root Canvas.
    * 
    * @param gc
    * @param sc cannot be null
    */
   public Drawable(GuiCtx gc, StyleClass sc) {
      this(gc, sc, gc.getCanvasRoot().getVCAppli());
   }

   /**
    * 
    * @param gc
    * @param sc
    * @param vc
    */
   public Drawable(GuiCtx gc, StyleClass sc, ViewContext vc) {
      super(gc);
      if (sc == null) {
         throw new NullPointerException("StyleClass cannot be null for a Drawable");
      }
      if (vc == null) {
         vc = gc.getVCGhost();
      }
      this.styleClass = sc;
      uiid = gc.getRepo().nextUIID();
      //a drawable is sized by default? Full ViewContext
      layEngine = new LayouterEngineDrawable(gc, this);
      this.vc = vc;
      //init style to root
      style = styleClass.getRootStyle();
      styleCache = new StyleCache(gc.getDC(), this, style);
   }

   /**
    * Add a style layer animation
    * @param anim
    */
   public void addAnimationLayer(IAnimable anim) {
      if (da == null) {
         da = new DrawableAnimator(gc, this);
      }
      da.addLayerAnim(anim);
   }

   public void addCtrlFlags(int flags) {
      ctrl |= flags;
   }

   /**
    * Allows an external object to control the code for
    * <li> {@link IDrawable#manageKeyInput(ExecutionContextCanvasGui)}
    * <li> {@link IDrawable#managePointerInput(ExecutionContextCanvasGui)}
    * <br>
    * this is done through the {@link IDrawableListener} interface.
    * <br>
    * To keep object reference low, only 1 Listener is allowed in addition to the parent.
    * <br>
    * For more, register a special {@link IDrawableListener} like {@link SeveralActor} that
    * allows for more. 
    * @param actor
    */
   public void addDrawableListener(IDrawableListener actor) {
      if (pactor != null && pactor != actor) {
         SeveralActor sa = null;
         if (pactor instanceof SeveralActor) {
            sa = (SeveralActor) pactor;
         } else {
            sa = new SeveralActor(gc);
            pactor = sa;
         }
         sa.add(actor);
      } else {
         pactor = actor;
      }
   }

   /**
    * Add the {@link IAnimable} to animate the {@link Drawable}.
    * <br>
    * <br>
    * Creates a {@link DrawableAnimator}.
    * <br>
    * <br>
    * @param anim null to remove animation for that time period.
    */
   public void addFullAnimation(IAnimable anim) {
      if (da == null) {
         da = new DrawableAnimator(gc, this);
      }
      da.addDrawableAnim(anim);
   }

   public void byteObjectEvent(ByteObject bo, int type) {

   }

   /**
    * Check for cache {@link RgbImage} and drawable dimension mismatch. Fix it.
    * @param rgb
    * @return true if cache dimension has been updated.
    */
   public boolean cacheDimensionUpdate(RgbImage rgb) {
      switch (cacheType) {
         case CACHE_1_CONTENT:
            if (!isCacheDimensionOK(rgb, getContentW(), getContentH())) {
               rgb.changeDimension(getContentW(), getContentH());
               return true;
            }
            break;
         default:
            if (!isCacheDimensionOK(rgb, getDrawnWidth(), getDrawnHeight())) {
               rgb.changeDimension(getDrawnWidth(), getDrawnHeight());
               return true;
            }
            break;
      }
      return false;
   }

   /**
    * Initialize the {@link RgbImage} that is used for the caching.
    */
   private void cacheInit() {
      int initColor = getCacheBgColor();
      RgbCache rcache = gc.getDC().getCache();
      switch (cacheType) {
         case CACHE_0_NONE:
            cache = null;
            setStateFlag(STATE_14_CACHED, false);
            break;
         case CACHE_1_CONTENT:
            cache = rcache.create(getContentW(), getContentH(), initColor);
            setStateFlag(STATE_14_CACHED, true);
            break;
         case CACHE_2_FULL:
         case CACHE_3_BG_DECO:
            cache = rcache.create(getDrawnWidth(), getDrawnHeight(), initColor);
            setStateFlag(STATE_14_CACHED, true);
            break;
         default:
            break;
      }
   }

   /**
    * Repaint the Drawable cache {@link RgbImage} by on the cache type. 
    * <br>
    * <li> {@link ITechDrawable#CACHE_2_FULL}
    * <li> {@link ITechDrawable#CACHE_1_CONTENT}
    * <li> {@link ITechDrawable#CACHE_3_BG_DECO}
    * <br>
    * <br>
    * 
    * If Drawable wants direct Rgb drawing, 
    * <br>
    * <br>
    * @param rgb
    */
   public void cacheUpdate(RgbImage rgb) {
      //coordinate context is 0,0
      //should the Image be cleared of previous version? yes.

      //GraphicsX on the cache object. The cache could well be in Primitive mode for performance reasons
      //who decides that? TODO depends on opaqueness. when if Full Drawable is opaque, can use primitive mode
      rgb.fill(getCacheBgColor());
      GraphicsX cacheGraphics = rgb.getGraphicsX();
      cacheGraphics.toStringSetName("Drawable Cache");
      cacheGraphics.setPaintCtxFlag(ITechCanvasDrawable.REPAINT_13_CACHE_UPDATE, true);
      cacheGraphics.setPaintCtxFlag(ITechCanvasDrawable.REPAINT_11_NO_FLOW, true);

      //System.out.println("#Drawable.updateRgbCache for " + this.toString1Line() + " \n\t" + rgb.toString("\n\t\t"));
      cacheGraphics.tickStart();
      switch (cacheType) {
         case CACHE_1_CONTENT:
            //what is content for ViewDrawable? it is what is inside the ViewPort
            cacheGraphics.setTranslationShift(-getContentX(), -getContentY());
            drawDrawableContent(cacheGraphics);
            break;
         case CACHE_2_FULL:
            //translation is needed because drawable coordinate is at 0,0
            cacheGraphics.setTranslationShift(-getX(), -getY());
            //scrollbar may have the hidden flag but paint ctx flag deals with it.
            draw(cacheGraphics);
            break;
         case CACHE_3_BG_DECO:
            //what is decoration for ViewDrawable? Headers and Scrollbars?
            cacheGraphics.setTranslationShift(-getX(), -getY());
            drawDrawableBg(cacheGraphics);
            break;
         default:
            break;
      }
      setStateFlag(STATE_07_CACHE_INVALIDATED, true);
      //when cache is automatic
      //#debug
      String msg = "Time=" + cacheGraphics.tickTime() + "ms" + " PrimitiveCount=" + cacheGraphics.getPrimitiveCount() + " " + Thread.currentThread();
      //#debug
      toDLog().pAnim(msg, this, Drawable.class, "cacheUpdate", LVL_05_FINE, true);

      if (cacheGraphics.tickTime() < 20) {
      }
   }

   /**
    * <li> check if the dimension of drawable match those of the cache
    * <li> check if flag modified was set
    * <li> check if Drawable content has changed since last cache update
    * <li> check if Cache RgbImage has been modified by an animation
    */
   public void cacheValidate() {
      boolean isValid = true;
      switch (cacheType) {
         case CACHE_0_NONE:
            break;
         case CACHE_1_CONTENT:
            break;
         case CACHE_2_FULL:
            if (getDrawnWidth() != cache.getWidth() || getDrawnHeight() != cache.getHeight()) {
               isValid = false;
            }
            break;
         case CACHE_3_BG_DECO:
            break;

         default:
            break;
      }
      setStateFlag(STATE_07_CACHE_INVALIDATED, isValid);
   }

   /**
    * Default checks based on Behavior flags.
    * <br>
    * @param navEvent
    * @return
    */
   public boolean canNavigate(int navEvent) {
      if (navEvent == ITechNav.NAV_1_UP || navEvent == ITechNav.NAV_2_DOWN) {
         return hasBehavior(BEHAVIOR_26_NAV_VERTICAL);
      } else if (navEvent == ITechNav.NAV_3_LEFT || navEvent == ITechNav.NAV_4_RIGHT) {
         return hasBehavior(BEHAVIOR_27_NAV_HORIZONTAL);
      } else if (navEvent == ITechNav.NAV_5_SELECT) {
         return hasBehavior(BEHAVIOR_28_NAV_SELECTABLE);
      }
      return false;
   }

   /**
    * Called when the Drawable is being destroyed.
    * <br>
    * Even in Garbaged collected Host, sometimes a Drawable will register on the
    * eventChannel.
    * <br>
    * This method gives the opportuinity to clean up.
    * <br>
    * Called after a Page is serialized
    */
   public void clean() {
      if (isNavigatable()) {
         gc.getRepo().removeNavLinksAll(this);
      }
   }

   /**
    * Does nothing
    */
   public void commandAction(CmdInstance cmd) {

   }

   /**
    * Does nothing
    */
   public void commandAction(CmdInstanceGui cd) {

   }

   /**
    * Draws {@link Drawable} with flow control.
    * <br>
    * <br>
    * Flow Control depends : <br>
    * <li> {@link ITechDrawable#STATE_03_HIDDEN}
    * <li> {@link ITechDrawable#STATE_05_LAYOUTED}
    * <li> {@link ITechDrawable#STATE_06_STYLED}
    * <li> {@link ITechDrawable#STATE_20_ANIMATED_FULL_HIDDEN}
    * 
    * <li> Animation. 
    * <li>Cache.
    * <br>
    * <br>
    * Some animations hide the drawable because they draw an Image modification of it
    * But they will ask the Drawable to draw on its cache and get a Read Lock
    * Combinaison of ImgAnim and DrwAnim works as the cache updates from the DrwParam
    * Abstract
    * In the case of a partial repaint, draws again parent bg
    * <br>
    * <br>
    * Repainting a drawable in the context of a drawable repaint, procedure depends on opaqueness of Drawable.
    * Opaqueness state is set during the layout of the drawable. It is computed based on style layers.
    * A Style is opaque when At least one layer is opaque and meets with the Margin.
    * <br>
    * A Drawable may be opaque in a given style combination but not in another.
    * <br>
    * <br>
    * 
    * <br>
    * @param g 
    * @see {@link IDrawable#draw(GraphicsX)}.
    */
   public void draw(GraphicsX g) {
      int x = getX();
      //#debug
      toDLog().pDraw(ToStringStaticGui.toStringStates(this), this, Drawable.class, "draw@699", LVL_05_FINE, true);

      //#mdebug
      if (g == null) {
         throw new NullPointerException("Null GraphicsX");
      }
      //#enddebug

      //#mdebug
      if (gc.toStringHasFlagDraw(IToStringFlagsDraw.FLAG_DRAW_25_IGNORE_CLIP)) {
         g.setIgnoreClip(true);
      }
      //#enddebug

      //start the drawing by locking the Drawable with flag
      try {
         setStateFlag(STATE_01_DRAWING, true);
         if (g.hasPaintCtx(ITechCanvasDrawable.REPAINT_13_CACHE_UPDATE)) {
            drawDrawable(g);
         } else if (g.hasPaintCtx(ITechCanvasDrawable.REPAINT_11_NO_FLOW)) {
            drawDrawableIfCacheUseIt(g);
         } else {
            if (hasState(STATE_03_HIDDEN)) {
               return;
            }
            if (!hasState(STATE_06_STYLED)) {
               //#debug
               toDLog().pDraw("Drawable" + toStringGetName() + " not styled", this, Drawable.class, "draw");
               initSize();

               //#debug
               //throw new RuntimeException("#Drawable Drawn attempt without being Styled " + this.toString1Line());
            }
            //what if invalidated during the draw process? by another thread?
            if (!hasState(STATE_05_LAYOUTED)) {
               //#debug
               toDLog().pDraw("Drawable" + toStringGetName() + " not layouted", this, Drawable.class, "draw");
               initSize();

               //#debug
               //throw new RuntimeException("#Drawable Drawn attempt without being Layouted " + this.toString1Line());

            }
            if (!hasState(STATE_26_POSITIONED)) {
               initPosition();
            }
            if (hasState(STATE_20_ANIMATED_FULL_HIDDEN)) {
               //ask the animation director to delegate to active IAnimable
               if (da != null) {
                  da.drawAnimations(g);
               }
               return;
            }
            if (da != null) {
               if (hasState(STATE_21_ANIMATED_LAYERS)) {
                  drawDrawableAnimated(g);
               }
            }
            //Question: what if method is called to draw it on a RgbImage and not on the Screen?
            //Answer: Wrong way to do it. You must then use drawDrawable(Img).
            if (g.hasPaintCtx(ITechCanvasDrawable.REPAINT_10_SPECIAL)) {
               drawSpecial(g);
               return;
            }
            if (!hasBehavior(BEHAVIOR_17_CLIP_CONTROLLED)) {
               int cx = g.getClipX();
               boolean isClipped = true;
               if (cx < x) {
                  int cy = g.getClipY();
                  if (cy < getY()) {
                     int cw = g.getClipWidth();
                     if (cx + cw > x + getDrawnWidth()) {
                        int ch = g.getClipHeight();
                        if (cy + ch > getY() + getDrawnHeight()) {
                           isClipped = false;
                        }
                     }
                  }
               }
               setStateFlag(STATE_29_CLIPPED, isClipped);
            }
            if (!g.hasPaintCtx(ITechCanvasDrawable.REPAINT_01_FULL)) {
               eraseDrawNotFull(g);
            }
            drawDrawableIfCacheUseIt(g);
            if (!hasState(STATE_02_DRAWN)) {
               setStateFlag(STATE_02_DRAWN, true);
            }
         }
      } finally {
         //release the lock
         setStateFlag(STATE_01_DRAWING, false);
      }
   }

   /**
    * Draws the Cache version of the {@link Drawable}
    * <li>{@link ITechDrawable#CACHE_0_NONE} generic all
    * <li>{@link ITechDrawable#CACHE_1_CONTENT} generic for bg and fg. RgbImage for content
    * <li>{@link ITechDrawable#CACHE_2_FULL} RgbImage for all.
    * <li>{@link ITechDrawable#CACHE_3_BG_DECO}
    * <br>
    * <br>
    * @param g
    */
   private void drawCache(GraphicsX g) {
      switch (cacheType) {
         case CACHE_0_NONE:
            drawDrawable(g);
            break;
         case CACHE_1_CONTENT:
            drawDrawableBg(g);
            cache.draw(g, getContentX(), getContentY());
            drawDrawableFg(g);
            break;
         case CACHE_2_FULL:
            cache.draw(g, getX(), getY());
            break;
         case CACHE_3_BG_DECO:
            cache.draw(g, getX(), getY());
            drawDrawableContent(g);
            drawDrawableFg(g);
         default:
            break;
      }
   }

   /**
    * Draws the Drawable using the cached {@link RgbImage}.
    * <br>
    * <br>
    * <br>
    * 
    * @param g
    */
   private void drawCachedDrawable(GraphicsX g) {
      //check cache status
      if (cache == null) {
         cacheInit();
         if (cache != null) {
            cache.setFlag(RgbImage.FLAG_09_USED, true);
            cacheUpdate(cache);
            setStateFlag(STATE_07_CACHE_INVALIDATED, false);
         }
      } else {
         cache.setFlag(RgbImage.FLAG_09_USED, true);
         //1# check if the dimension of drawable match those of the cache
         //2# check if flag modified was set
         //2# check if Drawable content has changed since last cache update
         //3# check if Cache RgbImage has been modified by an animation
         cacheValidate();
         if (hasState(STATE_07_CACHE_INVALIDATED)) {
            cacheUpdate(cache);
            setStateFlag(STATE_07_CACHE_INVALIDATED, false);
            //notify img listeners, that image content has changed
            cache.setFlag(RgbImage.FLAG_14_MODIFIED, false);
         }
      }
      drawCache(g);
   }

   /**
    * Snapshot Draw with no flow control.
    * When {@link DrawableAnimator} is not null
    * <br>
    * <li>First draw BgDecoration
    * <li>Then Drawable content in area for content
    * <li>Last draw Foreground decoration
    * <br>
    * <br>
    * Subclasses such as {@link ViewDrawable} will subclass this to implement {@link ViewPane} wrapping
    * <br>
    * <br>
    * @param g
    * 
    */
   public void drawDrawable(GraphicsX g) {
      if (da != null && da.hasFlag(DrawableAnimator.INNER_ANIM)) {
         drawDrawableAnimLayers(g);
      } else {
         //#mdebug
         if (gc.toStringHasFlagDraw(IToStringFlagsDraw.FLAG_DRAW_03_DRAWABLE_BOUNDARY)) {
            g.setColor(255, 0, 100);
            g.drawRect(getX(), getY(), getDrawnWidth(), getDrawnHeight());
         }
         //#enddebug
         drawDrawableBg(g);
         drawDrawableContent(g, getContentX(), getContentY(), getContentW(), getContentH());
         drawDrawableFg(g);

         //#mdebug
         if (gc.toStringHasFlagDraw(IToStringFlagsDraw.FLAG_DRAW_03_DRAWABLE_BOUNDARY)) {
            g.setColor(255, 0, 100);
            g.drawRect(getX(), getY(), getDrawnWidth(), getDrawnHeight());
         }
         //#enddebug
      }
   }

   public void drawDrawable(RgbImage rgb) {

   }

   /**
    * This method is needed when at least one {@link FigDrawable} is used to impersonate a DLayer.
    * <br>
    * <br>
    * Needed for {@link ITechAnim#ANIM_DRAW_1_CACHE} and {@link ITechAnim#ANIM_DRAW_1_CACHE} animations.
    * <br>
    * <br>
    * <br>
    * 
    * Use the {@link DrawableAnimator}
    * {@link DrawableAnimator} is not null
    * @param g
    */
   void drawDrawableAnimated(GraphicsX g) {
      //draw bg
      int x = this.getPozeX();
      int y = this.getPozeY();
      int[] styleAreas = this.getStyleAreas();
      ByteObject style = this.styleCache.getStyle();
      StyleOperator styleOp = getStyleOp();
      styleOp.drawStyleBg(style, g, styleAreas, x, y);

      if (da.animated[0] != null) {
         da.animated[0].drawDrawable(g);
      } else {
         //otherwise draw it normally.
         styleOp.drawStyleFigure(style, g, STYLE_OFFSET_2_FLAG_B, STYLE_FLAG_B_1_BG, styleAreas, 1);
      }
      if (da.animated[9] != null) {
         da.animated[9].drawDrawable(g);
      } else {
         drawDrawableContent(g, getContentX(), getContentY(), getContentW(), getContentH());
      }
      drawDrawableFg(g);
   }

   private void drawDrawableAnimLayer(GraphicsX g, int w, int h, int[] areas, int index, int flagOffset, int flag) {
      FigDrawable fg1 = da.getFg(index);
      if (fg1 == null) {
         getStyleOp().drawStyleFigure(style, g, STYLE_OFFSET_2_FLAG_B, STYLE_FLAG_B_1_BG, areas, 1);
      } else {
         fg1.draw(g);
      }
   }

   /**
    * 
    * @param g
    */
   public void drawDrawableAnimLayers(GraphicsX g) {
      //we have to draw
      int w = getDw();
      int h = getDh();
      int[] areas = getStyleOp().computeNewStyleAreas(getX(), getY(), w, h, style);
      if (da.hasFlag(DrawableAnimator.INNER_5_BG)) {
         //
         drawDrawableAnimLayer(g, w, h, areas, 0, STYLE_OFFSET_2_FLAG_B, STYLE_FLAG_B_1_BG);
         drawDrawableAnimLayer(g, w, h, areas, 1, STYLE_OFFSET_2_FLAG_B, STYLE_FLAG_B_2_BG);
         drawDrawableAnimLayer(g, w, h, areas, 2, STYLE_OFFSET_2_FLAG_B, STYLE_FLAG_B_3_BG);
         drawDrawableAnimLayer(g, w, h, areas, 3, STYLE_OFFSET_2_FLAG_B, STYLE_FLAG_B_4_BG);

      } else {
         drawDrawableBg(g);
      }
      if (da.hasFlag(DrawableAnimator.INNER_6_CONTENT)) {
         FigDrawable fg1 = da.getFg(8);
         fg1.draw(g);
      } else {
         drawDrawableContent(g, getContentX(), getContentY(), getContentW(), getContentH());
      }
      if (da.hasFlag(DrawableAnimator.INNER_7_FG)) {
         drawDrawableAnimLayer(g, w, h, areas, 4, STYLE_OFFSET_2_FLAG_B, STYLE_FLAG_B_5_FG);
         drawDrawableAnimLayer(g, w, h, areas, 5, STYLE_OFFSET_2_FLAG_B, STYLE_FLAG_B_6_FG);
         drawDrawableAnimLayer(g, w, h, areas, 6, STYLE_OFFSET_2_FLAG_B, STYLE_FLAG_B_7_FG);
         drawDrawableAnimLayer(g, w, h, areas, 7, STYLE_OFFSET_2_FLAG_B, STYLE_FLAG_B_8_FG);
      } else {
         drawDrawableFg(g);
      }
   }

   /**
    * Draw layers below content (Background and Border) using the drawable area x,y dw,dh
    * <br>
    * <br>
    * 
    * @param g
    */
   public void drawDrawableBg(GraphicsX g) {
      int x = this.getPozeX();
      int y = this.getPozeY();
      this.drawDrawableBg(g, x, y);
   }

   public void drawDrawableBg(GraphicsX g, int x, int y) {
      int[] styleAreas = this.getStyleAreas();
      ByteObject style = this.styleCache.getStyle();
      StyleOperator styleOp = getStyleOp();
      styleOp.drawStyleBg(style, g, styleAreas, x, y);
   }

   public void drawDrawableFg(GraphicsX g, int x, int y) {
      int[] styleAreas = this.getStyleAreas();
      ByteObject style = this.styleCache.getStyle();
      StyleOperator styleOp = getStyleOp();
      styleOp.drawStyleFg(style, g, styleAreas, x, y);
   }

   /**
    * Draw background style layers using give x,y,w,h.. 
    * 
    * This computes area values for this draw without using cache version,
    * @param g
    * @param x
    * @param y
    * @param w
    * @param h
    */
   public void drawDrawableBg(GraphicsX g, int x, int y, int w, int h) {
      StyleOperator styleOp = getStyleOp();
      int[] styleAreas = styleOp.getStyleAreasFull(x, y, w, h, style, this);
      styleOp.drawStyleBg(style, g, styleAreas);
   }

   /**
    * Draw content. Coordinates must be computed by the method
    * <br>
    * <br>
    * How does {@link ViewDrawable} implement it for content caching?
    * <br>
    * 
    * @param g 
    */
   public void drawDrawableContent(GraphicsX g) {
      drawDrawableContent(g, getContentX(), getContentY(), getContentW(), getContentH());
   }

   /**
    * To be implemented by subclass.
    * <br>
    * <br>
    * Only draws raw content at x,y. no background, no border, no foreground.
    * <br>
    * <br>
    * Special case for bare {@link Drawable} class when Parent is of type {@link IDrawListener}.
    * <br>
    * Parent draws content of drawable by check {@link Drawable} reference.
    * <br>
    * <br>
    * @param g
    * @param x caller has already computed x as content X
    * @param y caller has already computed y as content Y
    * @param w content width
    * @param h content height
    */
   protected void drawDrawableContent(GraphicsX g, int x, int y, int w, int h) {
   }

   /**
    * @param g
    */
   public void drawDrawableFg(GraphicsX g) {
      int x = this.getPozeX();
      int y = this.getPozeY();
      this.drawDrawableFg(g, x, y);
   }

   public void drawDrawableFg(GraphicsX g, int x, int y, int w, int h) {
      StyleOperator styleOp = getStyleOp();
      int[] styleAreas = styleOp.getStyleAreasFull(x, y, w, h, style, this);
      getStyleOp().drawStyleFg(style, g, styleAreas);
   }

   /**
    * Draws if cache is available, otherwise simple draw.
    * <br>
    * <br>
    * @param g
    */
   private void drawDrawableIfCacheUseIt(GraphicsX g) {
      //cache management
      if (hasBehavior(BEHAVIOR_10_FORCE_CACHING) && !hasBehavior(BEHAVIOR_11_DISABLE_CACHING)) {

         drawCachedDrawable(g);
      } else {
         //draw on the Screen GraphicsX Object
         drawDrawable(g);
      }
      if (cache != null) {
         cache.setFlag(RgbImage.FLAG_09_USED, false);
      }
   }

   protected void drawSpecial(GraphicsX g) {

   }

   /**
    * To be implemented by subclass when it has a special topology.
    * <br>
    * <br>
    * See {@link ViewDrawable#eraseDrawableContent(GraphicsX)}
    * @param g
    * 
    */
   public void eraseDrawableContent(GraphicsX g) {
      g.fillRect(getX(), getY(), getDw(), getDh());
   }

   /**
    * Called when partial repaint and Canvas area has not been erased. Therefore {@link Drawable} is responsible for erasing its area.
    * <br>
    * <br>
    * A repaint directive is computed by {@link ScreenResult} when {@link Drawable} is repainted individually.
    * <br>
    * <br>
    * When {@link Drawable} is opaque, there is no need to erase. The repainting process will erase by the {@link Drawable} area
    * with its opaque style layers. 
    * <br>
    * <br>
    * When not opaque, the {@link Drawable} must repaint what is supposed to be below him until it finds an opaque layer. 
    * <li>container background
    * <li>overlayed {@link Drawable}. Container knows that.
    * <br>
    * <br>
    * @param g
    */
   protected void eraseDrawNotFull(GraphicsX g) {

      if (isOpaque()) {
         return;
      } else {
         //#debug
         String msg = "";
         //#debug
         toDLog().pDraw(msg, this, Drawable.class, "eraseDrawNotFull", LVL_05_FINE, true);

         if (hasState(STATE_24_HOLED)) {
            //TODO why Scrollbar does not override this method?
            int[] holes = getHoles();
            //erase the area minus the holes
            int[] ag = gc.getUC().getGeo2dUtils().getHolesInverse(getX(), getY(), getDrawnWidth(), getDrawnHeight(), holes);
            for (int i = 0; i < ag.length; i += 4) {
               //repaint area
               int ax = ag[i];
               int ay = ag[i + 1];
               int aw = ag[i + 2];
               int ah = ag[i + 3];
               g.setColor(0);
               g.fillRect(ax, ay, aw, ah);
            }
            return;
         }
         //repaint directive computed by Sc
         //when not direct parent or mastercanvas, do a full repaint, this must be checked in ScreenResult
         //we must repaint parent until Mastercanvas which is always opaque. too complicated do 
         //find an opaque parent. or a layer down the ZIndex

         IDrawable parent = getParent();
         g.clipSet(getX(), getY(), getDw(), getDh());
         if (parent != null) {
            if (!parent.hasState(STATE_01_DRAWING)) {
               if (parent.isOpaque()) {
                  //if parent is not opaque? draw layers below until find master canvas
                  if (parent instanceof Drawable) {
                     ((Drawable) parent).drawDrawableBg(g);
                  } else {
                     //bad
                     throw new RuntimeException("Not Implemented Yet");
                  }

               } else {
                  //not opaque?
               }
            }
         } else {
            //we must also check that child is contained in parent, otherwise just do a full repaint.
            //what if drawable in on a lyer?
            //get the background color of g and clean our area with it
            g.clearWithBgColor();
         }
         g.clipReset();

         //reset erase repaint directive flags which have been acted upon
         setBehaviorFlag(BEHAVIOR_13_REPAINT_PARENT_STYLE, false);
         setBehaviorFlag(BEHAVIOR_14_REPAINT_MASTER_CANVAS, false);

      }
   }

   public AnimManager getAnimCreator() {
      return vc.getCanvasDrawable().getAnimCreator();
   }

   public Zer2DArea getArea() {
      return layEngine.getArea();
   }

   private int getCacheBgColor() {
      if (getStyleOp().isOpaqueBgLayersStyle(style)) {
         return IColors.FULLY_OPAQUE_BLACK;
      } else {
         return 0;
      }
   }

   public CanvasAppliInputGui getCanvas() {
      return vc.getCanvasDrawable();
   }

   /**
    * To be overriden by Drawables with children
    */
   public IDrawable[] getChildren() {
      return null;
   }

   public MCmd getCmd(int vcmdid) {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * Returns
    * <li> own {@link CmdCtx} if one
    * <li> Parent drawable if one
    * <li> root
    */
   public CmdNode getCmdNode() {
      if (cmdNode != null) {
         return cmdNode;
      } else if (parent != null) {
         return parent.getCmdNode();
      } else {
         return gc.getCC().getCmdNodeRoot();
      }
   }

   /**
    * Drawable height minus style decoration height pixels.
    * <br>
    * <br>
    * @return
    */
   public int getContentH() {
      return layEngine.getContentH();
   }

   /**
    * Drawable width minus decoration width pixels.
    * @return
    */
   public int getContentW() {
      return layEngine.getContentW();
   }

   /**
    * Content x coordinate. Take into account Left Margin/Padding/Border.
    * @return
    */
   public int getContentX() {
      //but has X been computed?
      return getX() + styleCache.getStyleWLeftAll();
   }

   /**
    * Content y coordinate. Take into account Top Margin/Padding/Border.
    * @return
    */
   public int getContentY() {
      return layEngine.getContentY();
   }

   public ICtx getCtxOwner() {
      return gc;
   }

   /**
    *  Custom Type -> see {@link Drawable#ctype} for discussion on the nature of this field.
    * @return
    */
   public int getCType() {
      return ctype;
   }

   public ILayoutable[] getDependencies() {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * drawn height straight from the {@link LayouterEngineDrawable}
    * @return
    */
   protected int getDh() {
      return layEngine.getSizeDrawnHeight();
   }

   /**
    * Construct a list of Drawables that are located at coordinate.
    * <br>
    * Subclass overrides this method when it has children.
    * <br>
    * When a child is outside the parent?, the parent will not be.
    * <br>
    * This method constructs the visible. 
    * {@link TopologyDLayer#getDrawable(int, int, ExecutionContextCanvasGui)} gets to 
    * direct.
    * <br>
    * When a child is outside a parent, it must be drawn as a layer.
    * <br>
    * By default return this. Parent is responsible to check for boundary.
    * <br>
    */
   public IDrawable getDrawable(int x, int y, ExecutionContextCanvasGui ex) {
      return this;
   }

   /**
    * Gets the {@link DrawableAnimator}  for the {@link Drawable}.
    * <br>
    * <br>
    * Create one if none
    * @return null if none
    */
   public DrawableAnimator getDrawableAnimator() {
      if (da == null) {
         da = new DrawableAnimator(gc, this);
      }
      return da;
   }

   /**
    * 0 when Immaterial
    */
   public int getDrawnHeight() {
      if (hasBehavior(BEHAVIOR_16_IMMATERIAL)) {
         return 0;
      }
      return getDh();
   }

   /**
    * 0 when Immaterial
    */
   public int getDrawnWidth() {
      if (hasBehavior(BEHAVIOR_16_IMMATERIAL)) {
         return 0;
      }
      return getDw();
   }

   /**
    * Not 0 when Immaterial
    * 
    */
   protected int getDw() {
      return layEngine.getSizeDrawnWidth();
   }

   public FocusCtrl getFocusCtrl() {
      return gc.getFocusCtrl();
   }

   public IMFont getFontStyle() {
      IMFont font = getStyleOp().getStyleFont(style);
      return font;
   }

   public int getHCPosInside() {
      int vx = vc.getX();
      int vw = vc.getHeight();
      int mid = getX() + (getDrawnWidth() / 2);
      if (mid < vx + (vw / 2)) {
         return C.POS_2_LEFT;
      }
      return C.POS_3_RIGHT;
   }

   public int[] getHoles() {
      return holes;
   }

   /**
    * Used to set sizers and pozers for this {@link Drawable}
    * @return
    */
   public Area2DConfigurator getLay() {
      return layEngine.getLay();
   }

   public LayouterEngineDrawable getLayEngine() {
      return layEngine;
   }

   public ILayoutable getLayoutableDelegate(ILayoutable source) {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * Returns rarely supported etalon 
    */
   public ILayoutable getLayoutableEtalon(int etalonType) {
      // TODO Auto-generated method stub
      return null;
   }

   public ILayoutable getLayoutableID(int id) {
      return gc.getRepo().getDrawable(id);
   }

   public ILayoutable getLayoutableNav(int dir) {
      return gc.getRepo().getLink(this, dir);
   }

   public ILayoutable getLayoutableParent() {
      return parent;
   }

   public ILayoutable getLayoutableViewContext() {
      return vc;
   }

   /**
    * Return this
    */
   public ILayoutable getLayoutableViewPort() {
      return this;
   }

   public ILayoutDelegate getLayoutDelegate() {
      // TODO Auto-generated method stub
      return null;
   }

   public int getLayoutID() {
      // TODO Auto-generated method stub
      return 0;
   }

   public String getName() {
      return "" + hashCode();
   }

   /**
    * {@link Drawable} that is located at the given location
    * <br>
    * If any
    * <br>
    * Event is
    * <li> {@link INavigational#NAV_0_REFRESH}
    * <li> {@link INavigational#NAV_1_UP}
    * <li> {@link INavigational#NAV_2_DOWN}
    * <li> {@link INavigational#NAV_3_LEFT}
    * <li> {@link INavigational#NAV_4_RIGHT}
    * <li> {@link INavigational#NAV_5_SELECT}
    * <li> {@link INavigational#NAV_6_UNSELECT}
    * 
    * @param navEvent
    * @return
    */
   public IDrawable getNavigate(int navEvent) {
      if (isNavigatable()) {
         return gc.getRepo().getLink(this, navEvent);
      }
      return null;
   }

   protected StringAuxOperator getOpStringFx() {
      return gc.getDC().getStrAuxOperator();
   }

   /**
    * Will be null at the root drawable who is neve
    */
   public IDrawable getParent() {
      return parent;
   }

   /**
    * TODO
    * When null, create a wrapper around the {@link ViewContext}
    * @return
    */
   public IDrawable getParentNotNull() {
      if (parent == null) {
         throw new NullPointerException("Parent Must Not Be NUll");
      }
      return parent;
   }

   public int getPozeX() {
      return layEngine.getX();
   }

   public int getPozeXComputed() {
      layEngine.layoutUpdatePositionXCheck();
      return layEngine.getX();
   }

   public int getPozeY() {
      return layEngine.getY();
   }

   public int getPozeYComputed() {
      layEngine.layoutUpdatePositionYCheck();
      return layEngine.getY();
   }

   public RepaintHelperGui getRepaintCtrlDraw() {
      ViewContext viewContext = vc;
      if (viewContext == null) {
         viewContext = gc.getCanvasGCRoot().getCanvas().getVCRoot();
      }
      return viewContext.getRepaintCtrlDraw();
   }

   /**
    * Returns a reference to the cache. If none, creates one of the given type.
    * <br>
    * Mostly used by animations and can be shared between animations.
    * <br>
    * <br>
    * @param type
    * @return {@link RgbImage}
    */
   public RgbImage getRgbCache(int type) {

      //update the cache if it does not match type requested
      //&& !hasState(STATE_07_CACHE_INVALIDATED)
      if (false && cache != null && cacheType == type && !hasState(STATE_07_CACHE_INVALIDATED)) {
         return cache;
      } else {
         cache = null;
         cacheType = type;
         if (cache == null) {
            cacheInit();
         }
         cacheUpdate(cache);
         return cache;
      }
   }

   /**
    * Calls {@link Drawable#getRgbImage(int, int)} with opaque black or fully transparent if {@link ITechDrawable#STATE_17_OPAQUE} is not set.
    */
   public RgbImage getRgbImage(int type) {
      if (hasState(STATE_17_OPAQUE)) {
         return getRgbImage(type, IColors.FULLY_OPAQUE_BLACK);
      }
      return getRgbImage(type, 0);
   }

   /**
    * Returns image of {@link IDrawable#getDrawnWidth()}, {@link IDrawable#getDrawnHeight()}.
    * <br>
    * <br>
    * May it uses the cache for drawing if one is available? Yes
    * <br>
    * Totally unrelated image to the {@link Drawable}
    * 
    * <li> {@link ITechDrawable#IMAGE_0_ALL}
    * <li> {@link ITechDrawable#IMAGE_1_CONTENT}
    * <li> {@link ITechDrawable#IMAGE_2_BG}
    * <li> {@link ITechDrawable#IMAGE_3_FG}
    * <li> {@link ITechDrawable#IMAGE_4_CONTENT_BG}
    * <li> {@link ITechDrawable#IMAGE_5_CONTENT_FG}
    * <br>
    * <br>
    * In all cases, image has dw and dh
    * <br>
    * <br>
    * @param type {@link ITechDrawable#IMAGE_1_CONTENT} etc
    * @param bgColor background color of the image. if 0 (fully transparent black)
    */
   public RgbImage getRgbImage(int type, int bgColor) {
      RgbImage ri = gc.getDC().getRgbCache().create(getDrawnWidth(), getDrawnHeight(), bgColor);
      GraphicsX g = ri.getGraphicsX();
      g.setTranslationShift(-getX(), -getY());
      switch (type) {
         case IMAGE_0_ALL:
            drawDrawableIfCacheUseIt(g);
            break;
         case IMAGE_1_CONTENT:
            drawDrawableContent(g);
            break;
         case IMAGE_2_BG:
            drawDrawableBg(g);
            break;
         case IMAGE_3_FG:
            drawDrawableFg(g);
            break;
         case IMAGE_4_CONTENT_BG:
            drawDrawableBg(g);
            drawDrawableContent(g);
            break;
         case IMAGE_5_CONTENT_FG:
            drawDrawableContent(g);
            drawDrawableFg(g);
            break;
         default:
            break;
      }
      return ri;
   }

   /**
    * Position on the Screen. This adds all {@link ViewContext} 
    * in the Hierarchy until the root {@link ViewContext}.
    * <br>
    * @return
    */
   public int getScreenX() {
      return getX() + vc.getScreenX();
   }

   public int getScreenY() {
      return getY() + vc.getScreenY();
   }

   /**
    * Height Size of the actual content without the margin, border and padding
    * @return
    */
   public int getSizeContentHeight() {
      return layEngine.getContentH();
   }

   /**
    * Width size of the actual content without the margin, border and padding.
    * @return
    */
   public int getSizeContentWidth() {
      return layEngine.getContentW();
   }

   public int getSizeDrawnHeight() {
      return layEngine.getH();
   }

   public int getSizeDrawnWidth() {
      return layEngine.getW();
   }

   public int getSizeFontHeight() {
      return getFontStyle().getHeight();
   }

   public int getSizeFontWidth() {
      return getFontStyle().getWidthWeigh();
   }

   public int getSizeH(int sizeType) {
      return getSizePropertyValueH(sizeType);
   }

   /**
    * The preferred height of this Drawable.
    * That is preferred content size with style values.
    */
   public int getSizePreferredHeight() {
      //we don't have ph here at this lvl
      return layEngine.getH();
   }

   public int getSizePreferredWidth() {
      return layEngine.getW();
   }

   public int getSizePropertyValueH(int property) {
      switch (property) {
         case ITechLayout.SIZER_PROP_00_DRAWN:
            return getSizeDrawnHeight();
         case ITechLayout.SIZER_PROP_01_PREFERRED:
            return getSizePreferredHeight();
         case ITechLayout.SIZER_PROP_02_UNIT_LOGIC:
            return getSizeUnitHeight();
         case ITechLayout.SIZER_PROP_03_FONT:
            return getSizeFontHeight();
         case ITechLayout.SIZER_PROP_05_CONTENT:
            return getSizeContentHeight();
         case ITechLayout.SIZER_PROP_06_CONTENT_PAD:
            return getSizeContentHeight() + styleCache.getStyleWPadding();
         case ITechLayout.SIZER_PROP_07_CONTENT_PAD_BORDER:
            return getSizeContentHeight() + styleCache.getStyleHPaddingBorder();
         case ITechLayout.SIZER_PROP_10_PAD:
            return styleCache.getStyleHPadding();
         case ITechLayout.SIZER_PROP_11_PAD_BORDER:
            return styleCache.getStyleHPaddingBorder();
         case ITechLayout.SIZER_PROP_12_PAD_BORDER_MARGIN:
            return styleCache.getStyleHPaddingBorderMargin();
         case ITechLayout.SIZER_PROP_13_BORDER:
            return styleCache.getStyleHBorder();
         case ITechLayout.SIZER_PROP_14_BORDER_MARGIN:
            return styleCache.getStyleHBorderMargin();
         case ITechLayout.SIZER_PROP_15_MARGIN:
            return styleCache.getStyleHMargin();
         default:
            break;
      }
      throw new IllegalArgumentException("property=" + property);
   }

   public int getSizePropertyValueW(int property) {
      switch (property) {
         case ITechLayout.SIZER_PROP_00_DRAWN:
            return getSizeDrawnWidth();
         case ITechLayout.SIZER_PROP_01_PREFERRED:
            return getSizePreferredWidth();
         case ITechLayout.SIZER_PROP_02_UNIT_LOGIC:
            return getSizeUnitWidth();
         case ITechLayout.SIZER_PROP_03_FONT:
            return getSizeFontWidth();
         case ITechLayout.SIZER_PROP_05_CONTENT:
            return getSizeContentWidth();
         case ITechLayout.SIZER_PROP_06_CONTENT_PAD:
            return getSizeContentWidth() + styleCache.getStyleWPadding();
         case ITechLayout.SIZER_PROP_07_CONTENT_PAD_BORDER:
            return getSizeContentWidth() + styleCache.getStyleWPaddingBorder();
         case ITechLayout.SIZER_PROP_10_PAD:
            return styleCache.getStyleWPadding();
         case ITechLayout.SIZER_PROP_11_PAD_BORDER:
            return styleCache.getStyleWPaddingBorder();
         case ITechLayout.SIZER_PROP_12_PAD_BORDER_MARGIN:
            return styleCache.getStyleWPaddingBorderMargin();
         case ITechLayout.SIZER_PROP_13_BORDER:
            return styleCache.getStyleWBorder();
         case ITechLayout.SIZER_PROP_14_BORDER_MARGIN:
            return styleCache.getStyleWBorderMargin();
         case ITechLayout.SIZER_PROP_15_MARGIN:
            return styleCache.getStyleWMargin();
         default:
            break;
      }
      throw new IllegalArgumentException("property=" + property);
   }

   /**
    * Subclass overrides to provide unit accounting
    * @return
    */
   public int getSizeUnitHeight() {
      //no concept of unit here.. return plain H
      return layEngine.getH();
   }

   public int getSizeUnitWidth() {
      //no concept of unit here.. return plain W
      return layEngine.getW();
   }

   public int getSizeW(int sizeType) {
      return getSizePropertyValueW(sizeType);
   }

   public int getSizeX(int sizeType) {
      int x = getPozeXComputed();
      switch (sizeType) {
         case ITechLayout.SIZER_PROP_05_CONTENT:
            return x + getStyleWLeftConsumed();
         case ITechLayout.SIZER_PROP_06_CONTENT_PAD:
            return x + getStyleWLeftConsumed() - styleCache.getStyleWPaddingLeft();
         default:
            break;
      }
      return x;
   }

   public int getSizeY(int sizeType) {
      if (sizeType == ITechLayout.SIZER_PROP_05_CONTENT) {
         return getY() + getStyleHTopConsumed();
      }
      return getY();
   }

   public int getStatorableClassID() {
      throw new RuntimeException("Must be implemented by subclass");
   }

   /**
    * See {@link Drawable#styleStruct} for a discussion of the nature of this field
    * 
    * @return mostly null unless set externally by {@link Drawable#setStyleStruct(ByteObject)}
    */
   public ByteObject getStructStyle() {
      return styleStruct;
   }

   /**
    * Current {@link ByteObject} style.
    */
   public ByteObject getStyle() {
      return style;
   }

   /**
    * Shortcut to {@link StyleCache#getStyleAreas()}
    * @return
    */
   public int[] getStyleAreas() {
      if (!hasState(STATE_05_LAYOUTED)) {
         this.initSize();
      }
      return styleCache.getStyleAreas();
   }

   /**
    * Returns a newly computed copy of the style areas with current x,y position and computed drawn width/height.
    * 
    * <p>
    * Structure of int[] array specified by {@link StyleCache#getStyleAreas()}
    * </p>
    * Does not use any cached values.
    * 
    * @return
    */
   public int[] getStyleAreasFor(int x, int y, int w, int h) {
      int relX = ITechStyleCache.RELATIVE_TYPE_0_MARGIN;
      int relY = ITechStyleCache.RELATIVE_TYPE_0_MARGIN;
      int relW = ITechStyleCache.RELATIVE_TYPE_0_MARGIN;
      int relH = ITechStyleCache.RELATIVE_TYPE_0_MARGIN;
      return gc.getDC().getStyleOperator().getStyleAreas(x, y, w, h, style, this, null, relW, relH, relX, relY);
   }

   public StyleCache getStyleCache() {
      return styleCache;
   }

   /**
    * {@link StyleClass} of this {@link Drawable}.
    */
   public StyleClass getStyleClass() {
      return styleClass;
   }

   public int getStyleHBotConsumed() {
      return styleCache.getStyleHBotAll();
   }

   /**
    * Number of pixels consumed by the top and bottom styles layers, padding, border and margin
    * 
    * @return
    */
   public int getStyleHConsumed() {
      return styleCache.getStyleHAll();
   }

   public int getStyleHTopConsumed() {
      return styleCache.getStyleHTopAll();
   }

   protected StyleOperator getStyleOp() {
      return gc.getDC().getStyleOperator();
   }

   public ByteObject getStyleValidated() {
      styleValidate();
      return style;
   }

   public int getStyleWConsumed() {
      return styleCache.getStyleWAll();
   }

   public int getStyleWLeftConsumed() {
      return styleCache.getStyleWLeftAll();
   }

   public int getStyleWRightConsumed() {
      return styleCache.getStyleWRightAll();
   }

   /**
    * Used for key in serializing and other services
    */
   public int getUIID() {
      return uiid;
   }

   /**
    * Returns the {@link ViewContext}
    */
   public ViewContext getVC() {
      return vc;
   }

   /**
    * Is the Drawable in the lower or the upper part of the {@link ViewContext}
    * <li> {@link C#POS_0_TOP}
    * <li> {@link C#POS_1_BOT}
    * 
    */
   public int getVCPosInside() {
      int vy = vc.getY();
      int vh = vc.getHeight();
      int mid = getY() + (getDrawnHeight() / 2);
      if (mid < vy + (vh / 2)) {
         return C.POS_0_TOP;
      }
      return C.POS_2_LEFT;
   }

   /**
    * 
    * @return
    */
   public ViewContext getViewContext() {
      return vc;
   }

   /**
    * Creates a new {@link ViewState} to save the state of the drawable.
    * <br>
    * 
    */
   public ViewState getViewState() {
      ViewState vs = new ViewState();
      vs.int1States = states;
      vs.int2Behaviors = behaviors;

      //TODO how do you save Listeners????
      return vs;
   }

   public int getWidthDrawn() {
      return layEngine.getW();
   }

   public int getWidthUnit() {
      return getSizeUnitWidth();
   }

   /**
    * current X pixel position relative to the {@link ViewContext}
    */
   public int getX() {
      return layEngine.getX();
   }

   /**
    * Currently computed Y pixel position relative to the {@link ViewContext}
    * If layout is not computed, value is undefined.
    */
   public int getY() {
      return layEngine.getY();
   }

   /**
    * returns the Z index in the current ViewContext 's topology.
    * <br>
    * The state is managed by. when index is -1, drawable is not the root
    * of a layer.
    * 
    */
   public int getZIndex() {
      return zindex;
   }

   public boolean hasBehavior(int flag) {
      return (behaviors & flag) == flag;
   }

   public boolean hasCtrlFlag(int flag) {
      return BitUtils.hasFlag(ctrl, flag);
   }

   /**
    * If two calls return false at two points in time, drawing the drawable
    * at those two point in time with xywh will yield identical visual results
    * So the subclass must implemenent a mean to know if an image has changed in content
    * For caching purposes, the Dw and Dh are handled automatically by the Drawable class
    * @return false by default (force update of the Cache)
    */
   public boolean hasDrawableContentChanged() {
      return false;
   }

   public boolean hasGene(int gene) {
      return BitUtils.hasFlag(genes, gene);
   }

   /**
    * {@link IDrawable} states. Thread safe because volatile field.
    * 
    * <li>{@link ITechDrawable#STATE_01_DRAWING}
    * <li>{@link ITechDrawable#STATE_02_DRAWN}
    * <li>{@link ITechDrawable#STATE_03_HIDDEN}
    * <li>{@link ITechDrawable#STATE_04_DRAWN_OUTSIDE}
    * <li>{@link ITechDrawable#STATE_05_LAYOUTED}
    * <li>{@link ITechDrawable#STATE_06_STYLED}
    * <li>{@link ITechDrawable#STATE_07_CACHE_INVALIDATED}
    * <li>{@link ITechDrawable#STATE_08_NO_RELAYOUTING}
    * <li>{@link ITechDrawable#STATE_09_TRIMMED}
    * <li>{@link ITechDrawable#STATE_10_CLIPPED}
    * <li>{@link ITechDrawable#STATE_11_ANIMATING}
    * <li>{@link ITechDrawable#STATE_12_APPEARING}
    * <li>{@link ITechDrawable#STATE_13_DISAPPEARING}
    * <li>{@link ITechDrawable#STATE_14_CACHED}
    * <li>{@link ITechDrawable#STATE_15_EMPTY}
    * <li>{@link ITechDrawable#STATE_16_TRANSLUCENT}
    * <li>{@link ITechDrawable#STATE_17_OPAQUE}
    */
   public boolean hasState(int flag) {
      return (states & flag) == flag;
   }

   public boolean hasFlagStateStyled() {
      return hasState(STATE_06_STYLED);
   }

   public boolean hasFlagStateLayouted() {
      return hasState(STATE_05_LAYOUTED);
   }

   public boolean hasStateNav(int flag) {
      return (statesNav & flag) == flag;
   }

   /**
    * States that modify the styling
    * 
    * <li>{@link ITechDrawable#STYLE_01_CUSTOM}
    * <li>{@link ITechDrawable#STYLE_02_CUSTOM}
    * <li>{@link ITechDrawable#STYLE_03_MARKED}
    * <li>{@link ITechDrawable#STYLE_04_GREYED}
    * <li>{@link ITechDrawable#STYLE_05_SELECTED}
    * <li>{@link ITechDrawable#STYLE_06_FOCUSED_KEY}
    * <li>{@link ITechDrawable#STYLE_07_FOCUSED_POINTER}
    * <li>{@link ITechDrawable#STYLE_08_PRESSED}
    * <li>{@link ITechDrawable#STYLE_09_DRAGGED}
    * <li>{@link ITechDrawable#STYLE_10_DRAGGED_OVER}
    * <li>{@link ITechDrawable#STYLE_11_CUSTOM}
    */
   public boolean hasStateStyle(int flag) {
      return (styleStates & flag) == flag;
   }

   /**
    * Reads the states that are volatile.
    * <br>
    * Volatile reads are pretty fast.
    * @param flag
    * @return
    */
   public boolean hasStateVolatile(int flag) {
      return (statesVolatile & flag) == flag;
   }

   /**
    * Request to hide this drawable inside the execution of {@link CmdInstance}
    * @param cmd
    */
   public void hideMe(ExecutionContextCanvasGui ec) {
      vc.getDrawCtrlAppli().removeDrawable(ec, this, null);
   }

   /**
    * Initialize the drawable width and height of the {@link Drawable} using {@link ITechDrawable#DIMENSION_API} semantics.
    * <br>
    * <br>
    * A default drawable initialized with 0,0 cannot computed anything since it has no content. It will take
    * the size of its parent/viewcontext
    * <br>
    * <br>
    * Method stores widht and height parameters:
    * <li>{@link Drawable#getInitWidth()}
    * <li>{@link Drawable#getInitHeight()}
    * 
    * <br>
    * <br>
    * 
    * When {@link Drawable} dimension must fit the {@link MasterCanvas}, {@link IDrawable#BEHAVIOR_14FULL_CANVAS_SIZE}.
    * <br>
    * Parent effective size ? Ratio of that size
    * <br>
    * When flag {@link ITechDrawable#STATE_06_STYLED} not set, drawable is re-initialized
    * <br>
    * <br>
    * <b>POST</b>: 
    * <li>State {@link ITechDrawable#STATE_06_STYLED} is true
    * <li>State {@link ITechDrawable#STATE_05_LAYOUTED} is true
    * <br>
    * <br>
    * It also initializes logical positions.
    * <br>
    * <br>
    * Inits positions that depends on Width and Height.
    * Otherwise, this is the job of {@link IDrawable#setXY(int, int)}
    * <br>
    * @param width
    * @param height
    * @deprecated
    */
   public void init(int width, int height) {
      //#debug
      toDLog().pFlow("width=" + width + " height=" + height, this, Drawable.class, "init", LVL_05_FINE, true);

      LayouterCtx lac = gc.getLAC();
      SizerFactory sizerFactory = lac.getSizerFactory();
      if (width > 0) {
         layEngine.setOverrideW(width);
      } else {
         if (width == 0) {
            layEngine.setManualOverrideW(false);
            layEngine.getArea().setSizerW(sizerFactory.getSizerPrefLazy());
         } else {
            //negative
            layEngine.setManualOverrideW(false);
            layEngine.getArea().setSizerW(sizerFactory.getSizerLogicUnit(-width));
         }
      }
      if (height > 0) {
         layEngine.setOverrideH(height);
      } else {
         if (height == 0) {
            layEngine.setManualOverrideH(false);
            layEngine.getArea().setSizerH(sizerFactory.getSizerPrefLazy());
         } else {
            //negative
            layEngine.setManualOverrideH(false);
            layEngine.getArea().setSizerH(sizerFactory.getSizerLogicUnit(-height));
         }
      }
      initSize();

   }

   /**
    * When {@link LayouterEngineDrawable} request context, sub class must update it.
    * To be overriden by subclass
    * Sub class uses the {@link LayouterEngineDrawable} to compute its size.
    * <br>
    * Some drawable like the {@link ImageDrawable} don't need cue sizers to compute
    * their size. 
    * <br>
    * 
    * @param width
    * @param height
    */
   protected void initDrawableSub() {

   }

   /**
    * Compute functional positions if any.
    * Set the positions if needed.
    * Let's say we have a {@link Drawable},
    * that is logically position in parent.
    * {@link Drawable} B position and dimension are both relative to
    * Drawable A. 
    * Drawable A moves in his Parent Rectangle. Drawable B managed by Parent
    * 
    * Everytime Drawable A position is invalidated, Parent requests a init on all
    * children. 
    * 
    * {@link ITechDrawable#STATE_26_POSITIONED}
    */
   public void initPosition() {
      layoutUpdatePositionCheck();
      setStateFlag(STATE_26_POSITIONED, true);
   }

   /**
    * Init with previous Sizing values.
    * Sets {@link ITechDrawable#STATE_30_LAYOUTING}
    * Returns true if the rectangle has changed.
    * Does not compute position of self. this is the job of the caller who knows where to position.
    * <br>
    * Computes its dimension and then the positions of its children {@link Drawable}s.
    * <br>
    * Flag Behavior is set when Drawable dimension depends on Children. In that case,
    * children sizers {@link ISizer#ET_LINK_0_PARENT} will default on ViewContext 
    * <br>
    * <br>
    * Reminder. Logical Positions are computed on demand. usually just before the draw
    * Setting a raw x
    */
   public void initSize() {
      setStateFlag(STATE_30_LAYOUTING, true);
      //compute pixel size based
      //might not have changed but context has changed!
      int oldDw = getDw();
      int oldDh = getDh();

      if (!hasState(STATE_06_STYLED)) {
         styleValidate();
      }

      initStyleLoad(); //load init style

      setStateFlagPrivate(STATE_08_NO_RELAYOUTING, false);

      //dw and dh are computing
      layEngine.layoutUpdateSizeCheck();
      //dw and dh are computed

      initDrawableSub();

      initStyleUnload(); // ?

      initStyleCache();

      //set flag just before sending event
      setStateFlag(STATE_05_LAYOUTED, true);
      setStateFlag(STATE_30_LAYOUTING, false);

      if (pactor != null && (getDw() != oldDw || getDh() != oldDh)) {
         pactor.notifyEvent(this, EVENT_12_SIZE_CHANGED, null);
      }
   }

   protected void initStyleCache() {
      //now that style has been initialized with cstyle if required
      //lets compute style areas
      int dw = getDrawnWidth();
      int dh = getDrawnHeight();
      int typeRelativeW = layEngine.getTypeSizerW();
      int typeRelativeH = layEngine.getTypeSizerH();
      int typeRelativeX = layEngine.getTypePozerX();
      int typeRelativeY = layEngine.getTypePozerY();

      styleCache.computeStyleDimensions(dw, dh, typeRelativeW, typeRelativeH, typeRelativeX, typeRelativeY);

      //if modified
      if (typeRelativeW == ITechStyleCache.RELATIVE_TYPE_0_MARGIN) {
         //no change to dw
      } else if (typeRelativeW == ITechStyleCache.RELATIVE_TYPE_1_BORDER) {
         this.setDw(styleCache.getStyleWMargin() + dw);
      } else if (typeRelativeW == ITechStyleCache.RELATIVE_TYPE_2_PADDING) {
         this.setDw(styleCache.getStyleWBorderMargin() + dw);
      } else if (typeRelativeW == ITechStyleCache.RELATIVE_TYPE_3_CONTENT) {
         this.setDw(styleCache.getStyleWAll() + dw);
      } else {
         throw new IllegalArgumentException();
      }

      if (typeRelativeH == ITechStyleCache.RELATIVE_TYPE_0_MARGIN) {
         //no change to dh
      } else if (typeRelativeH == ITechStyleCache.RELATIVE_TYPE_1_BORDER) {
         this.setDh(styleCache.getStyleHMargin() + dh);
      } else if (typeRelativeH == ITechStyleCache.RELATIVE_TYPE_2_PADDING) {
         this.setDh(styleCache.getStyleHBorderMargin() + dh);
      } else if (typeRelativeH == ITechStyleCache.RELATIVE_TYPE_3_CONTENT) {
         this.setDh(styleCache.getStyleHAll() + dh);
      } else {
         throw new IllegalArgumentException();
      }
   }

   public void initSizeWithPixels(int w, int h) {
      this.setSizePixels(w, h);
      this.initSize();
   }

   /**
    * Load init style for the duration of the {@link IDrawable#init(int, int)} method.
    * <br>
    * <br>
    * This method allows a Drawable to initialize with the biggest style.
    * <br>
    * For example, designer uses another Font with Bold for selected.
    * We want that style to be used for computing the init
    * 
    * TODO what effect on cache? it invalidates it
    */
   public void initStyleLoad() {
      int styleFlags = styleClass.getInitStyle();
      if (styleFlags != 0) {
         style = styleClass.getStyle(styleFlags, ctype, styleStruct);
      }

   }

   /**
    * 
    */
   public void initStyleUnload() {
      int styleFlags = styleClass.getInitStyle();
      if (styleFlags != 0) {
         styleValidate();
      }
   }

   /**
    * Invalidate the layout by setting false to {@link ITechDrawable#STATE_05_LAYOUTED} so that
    * the layout is computed again just before next repaint.
    * <br>
    * What if a component new layout influence the layout of another component that was not invalidated?
    * 
    * <br>
    * The {@link IDrawable#draw(GraphicsX)} will call {@link IDrawable#initSize()}
    * <br>
    * 
    * The goal is to avoid multiple relayouts in the interval of 2 repaints.
    * <br>
    * <br>
    * 
    */
   public void invalidateLayout() {
      //#debug
      toDLog().pInit("", this, Drawable.class, "invalidateLayout@2239", LVL_03_FINEST, true);

      setStateFlag(STATE_05_LAYOUTED, false);
      setStateFlag(STATE_26_POSITIONED, false);
   }

   /***
    * Called when the {@link StyleClass} repository has been modified.
    * <br>
    * Each Drawable updates its {@link StyleClass} from the repository. Using the ID.
    * <br>
    * If {@link StyleClass} does not have an ID. the StyleClass is not changed.
    * <br>
    * Sub classes must implement and call
    */
   public void invalidateStyleClass() {
      //how to uniquely identify a class? by root id then link IDs.
      //but a single class can be used at several positions? for example use a ViewPane style class for 
      //different table view style class.
      int id = styleClass.getID();
      if (id != -1) {
         styleClass = gc.getStyleClass(id);
      }
      invalidateLayout();
      setStateFlag(STATE_06_STYLED, false);
   }

   /**
    * Do the drawable dimensions match those of the rgb cache?
    * @param rgb
    * @return
    */
   private boolean isCacheDimensionOK(RgbImage rgb, int w, int h) {
      if (rgb.getWidth() != w)
         return false;
      if (rgb.getHeight() != h)
         return false;
      return true;
   }

   private boolean isNavigatable() {
      return hasBehavior(BEHAVIOR_31_NAVIGATABLE);
   }

   /**
    * Returns {@link ITechDrawable#STATE_17_OPAQUE}
    * 
    * If it was not computed by {@link Drawable#computeOpaqueFlag()}
    * it assumes it is not opaque.
    * @return
    */
   public boolean isOpaque() {
      return this.hasState(STATE_17_OPAQUE);
   }

   /**
    * Sets {@link ITechDrawable#STATE_18_OPAQUE_COMPUTED}.
    * @return
    */
   public boolean computeOpaqueFlag() {
      if (this.hasState(STATE_18_OPAQUE_COMPUTED)) {
         return this.hasState(STATE_17_OPAQUE);
      } else {
         int[] styleAreas = getStyleAreas();
         boolean b = getStyleOp().isOpaqueBgLayersStyle(style, styleAreas);
         this.setStateFlag(STATE_17_OPAQUE, b);
         this.setStateFlag(STATE_18_OPAQUE_COMPUTED, true);
         return b;
      }
   }

   public void layoutInvalidate() {
      layEngine.layoutInvalidate();
   }

   /**
    * Invalidates the Drawable position and Size by setting the state {@link ITechDrawable#STATE_05_LAYOUTED} flag to false. 
    * This force init call in the next drawing cycle.
    * <br>
    * Drawable MUST be reinitialized because being draw again.
    * <br>
    * Animation threads modifies {@link ISizer}. It will invalidates the Layout.
    * 
    * Will be called when canvasSize is changed. Subclass using Canvas size override this method to update their structure. 
    * <br>
    * <br>
    * <br>
    * Should the {@link Drawable#init(int, int)} method be called? No. The caller can still do it.
    * <br>
    * <br>
    * true when called in a top down approach which means, the method should invalide only its children
    * false, called in bottom up, ask the parent to invalidate itself if required.
    * 
    * @param noParentInvalidate when true, no check to updateParent structure. false when updating layout from a children
    * and we want the parents to update
    */
   public void layoutInvalidate(boolean noParentInvalidate) {
      if (noParentInvalidate) {
         invalidateLayout();
         //init(dwOriginal, dhOriginal);
      } else {
         //bottom up. all parents hosting the drawable will update by settings the la
         //
         int oldDH = getDrawnHeight();
         int oldDW = getDrawnWidth();
         invalidateLayout();
         //we comment this out because, we don't want 2 updateLayout calls 
         //init(dwOriginal, dhOriginal);
         //the call the init, will call the update on the ViewPane if necessary
         if (getDrawnWidth() != oldDW || getDrawnHeight() != oldDH) {
            if (parent != null) {
               parent.layoutInvalidate(false);
            }
         }
      }
      layEngine.layoutInvalidate();
   }

   public void layoutInvalidatePosition() {
      layEngine.layoutInvalidatePosition();
   }

   public void layoutInvalidateSize() {
      layEngine.layoutInvalidateSize();
   }

   public boolean layoutIsValidPosition() {
      return layEngine.layoutIsValidPosition();
   }

   public boolean layoutIsValidSize() {
      return layEngine.layoutIsValidSize();
   }

   /**
    * Force the resizing of everything drawable in the chain.
    * <br>
    * The layout flags are first set to false, then reinit of with
    * {@link Drawable#initSize()}
    * @param child not null when cause of update was a change in 
    * Drawable will decide if it needs to update its dimension.
    */
   public void layoutUpdate(IDrawable child) {
      int x = getX();
      int y = getY();
      int w = getDrawnWidth();
      int h = getDrawnHeight();

      initSize();
      if (parent != null) {
         if (w != getDrawnWidth() || h != getDrawnHeight() || x != getX() || y != getY()) {
            parent.layoutUpdate(child);
         }
      }
   }

   public void layoutUpdateDependencies(int type) {
      layEngine.layoutUpdateDependencies(type);
   }

   public void layoutUpdatePositionCheck() {
      layEngine.layoutUpdatePositionCheck();
   }

   public void layoutUpdatePositionXCheck() {
      layEngine.layoutUpdatePositionXCheck();
   }

   public void layoutUpdatePositionYCheck() {
      layEngine.layoutUpdatePositionYCheck();
   }

   public void layoutUpdateSizeCheck() {
      layEngine.layoutUpdateSizeCheck();
   }

   public void layoutUpdateSizeHCheck() {
      layEngine.layoutUpdateSizeHCheck();
   }

   public void layoutUpdateSizeWCheck() {
      layEngine.layoutUpdateSizeWCheck();
   }

   /**
    * 
    * @param ec TODO
    */
   public void manageGestureInput(ExecutionContextCanvasGui ec) {

   }

   public void manageInput(ExecutionContextCanvasGui ec) {
      InputState is = ec.getInputState();
      if (is.isTypeDeviceKeyboard()) {
         //procehisss the event in the main topology
         this.manageKeyInput(ec);
      } else {
         this.managePointerInput(ec);
      }
   }

   /**
    * Overrides this method
    */
   public void manageKeyInput(ExecutionContextCanvasGui ec) {
      InputConfig ic = ec.getInputConfig();
      if (parent != null && hasBehavior(BEHAVIOR_23_PARENT_EVENT_CONTROL)) {
         if (parent instanceof IDrawableListener) {
            ((IDrawableListener) parent).notifyEvent(this, EVENT_13_KEY_EVENT, ic);
         } else {
            throw new ClassCastException("Parent Drawable must implement IPointerActor");
         }
      }
      if (pactor != null) {
         pactor.notifyEvent(this, EVENT_13_KEY_EVENT, ic);
      }

   }

   /**
    * {@link Drawable} checks its state for a valid drawable destination.
    * <br>
    * It sets the destination drawable with
    * {@link InputConfig#s}.
    * Event is
    * <li> {@link INavigational#NAV_0_REFRESH}
    * <li> {@link INavigational#NAV_1_UP}
    * <li> {@link INavigational#NAV_2_DOWN}
    * <li> {@link INavigational#NAV_3_LEFT}
    * <li> {@link INavigational#NAV_4_RIGHT}
    * <li> {@link INavigational#NAV_5_SELECT}
    * <li> {@link INavigational#NAV_6_UNSELECT}
    * @param cd
    * @param navEvent
    */
   public void manageNavigate(CmdInstanceGui cd, int navEvent) {

   }

   /**
    * All other events kinds are sent here.
    */
   public void manageOtherInput(ExecutionContextCanvasGui ec) {

   }

   /**
    * Manages Pointer events for the {@link Drawable}.
    * <br>
    * Forwards the event to a child {@link Drawable} in a Top-Down manner.
    * <br>
    * Once the event reaches the final drawable, the event is acted upon with a command and state styles are
    * activated. The {@link Drawable} may delegate the event command.
    * <br>
    * That's how Bentley Script work. Event are generated. Control Drawable ID is recorded on the activated Drawable.
    * Command name is recorded. The Script must work with a different window size. Therefore absolute x.y pointer
    * events are not reliable.
    * <br>
    * By default manages state styles with {@link Drawable#managePointerStateStyle(ExecutionContextCanvasGui)}.
    * <br>
    * It also sets 
    * <br>
    * <b>Special Case:</b>
    * When Parent Drawable controls its child with {@link ITechDrawable#BEHAVIOR_23_PARENT_EVENT_CONTROL}. For example
    * a Table controlling the column titles.
    * Event is first forwarded to the parent {@link Drawable}. The parent knows it must manages the event for that Drawable because
    * {@link InputConfig#getSlaveDrawable()} returns it.
    * In the event, event pointer management is bottom up, {@link Drawable} manages style states and delegates to its parent
    * <li> {@link ITechDrawable#BEHAVIOR_23_PARENT_EVENT_CONTROL}
    * <li> {@link InputConfig#setSlaveDrawable(IDrawable)}
    * <br>
    * <br>
    * <li>Moved pointer focus automatically. though you may want to do it externally for efficiency.
    * <li> {@link ITechDrawable#STYLE_08_PRESSED}
    * <li> {@link ITechDrawable#STYLE_09_DRAGGED}
    * <li> Releases
    * <li> {@link InputConfig#setSlaveDrawable(IDrawable)}
    * 
    */
   public void managePointerInput(ExecutionContextCanvasGui ec) {
      InputConfig ic = ec.getInputConfig();
      //how do you detect several calls here?
      //#debug
      ic.toCheckPointer(this);

      //#debug
      toDLog().pFlow("'" + ic.toStringMod() + "'", this, Drawable.class, "managePointerInput", LVL_05_FINE, true);

      //use a callback
      //First pass. check
      if (parent != null && hasBehavior(BEHAVIOR_23_PARENT_EVENT_CONTROL)) {
         if (parent instanceof IDrawableListener) {
            ((IDrawableListener) parent).notifyEvent(this, EVENT_14_POINTER_EVENT, ic);
         } else {
            throw new ClassCastException("Parent Drawable must implement IPointerActor");
         }
      }
      if (pactor != null) {
         pactor.notifyEvent(this, EVENT_14_POINTER_EVENT, ic);
      }
      if (!ic.getCanvasResultDrawable().isDrawableStatesSet()) {
         managePointerStateStyle(ec);
      }
   }

   /**
    * This is the key method that must always be reached when a pointer event is generated
    * on a {@link Drawable}.
    * <br>
    * <br>
    * It finalize the states.
    * <br>
    * <br>
    * It is reached by {@link Drawable#managePointerInput(ExecutionContextCanvasGui)}. In the first pass
    * when parent is not controlling the Drawable. If controlled by parent, it is the responsibility
    * of the parent to call the {@link Drawable#managePointerInput(ExecutionContextCanvasGui)} once the slave pointer
    * event has been processed.
    * <br>
    * <br>
    * It also tracks the Gesture/Dragged state.
    */
   public void managePointerStateStyle(ExecutionContextCanvasGui ec) {
      //#debug
      toDLog().pState("", this, Drawable.class, "managePointerStateStyle", LVL_05_FINE, true);
      InputConfig ic = ec.getInputConfig();
      ic.getInputStateDrawable().setPointedDrawable(this);
      if (ic.isMoved()) {
         //TODO update the mouse over focused drawable
         //sometimes we don't want it to be processed.
         gc.getFocusCtrl().newFocusPointerPress(ec, this);
      } else if (ic.isPressed()) {

         setStateStyle(STYLE_08_PRESSED, true);
         //only do the gesture ctrl flow if no gesture has already been registered.
         //ie. if the drawable is not already in the gesture
         gc.getFocusCtrl().newFocusPointerPress(ec, this);
         ic.srActionDoneRepaint(this);
      } else if (ic.isReleased()) {
         setStateStyle(STYLE_08_PRESSED, false);
         setStateStyle(STYLE_09_DRAGGED, false);
         //the release is done outside the drawable but it got called because 
         //it was the draggable. so it gets a chance to set state
         if (!DrawableUtilz.isInside(ec, this)) {
            ic.getCanvasResultDrawable().setOutsideDrawable(true);
         }
         ic.srActionDoneRepaint(this);
         //Controller.getMe().draggedRemover(this, ic);

      } else if (ic.isDraggedPointer0Button0()) {
         setStateStyle(STYLE_09_DRAGGED, true);
         ic.srActionDoneRepaint(this);
      } else if (ic.isWheeled()) {

      }
      ic.getCanvasResultDrawable().setDrawableStates();
   }

   public void manageRepeatInput(ExecutionContextCanvasGui ec) {

   }

   /**
    * Called by {@link MCmdNav} on the active Drawable with {@link CmdInstanceGui}.
    * 
    * <p>
    * Default implementation inside {@link Drawable} assumes {@link Drawable} is not {@link INavigational}.
    * </p>
    *
    */
   public void navigateDown(CmdInstanceGui ci) {

   }

   /**
    * Called only if {@link ITechDrawable#BEHAVIOR_27_NAV_HORIZONTAL} is true.
    * <br>
    * First call in cycle with {@link InputConfig#isFirstNavCycle()}
    * <br>
    * Second call, implements any second cycle stuff like cycles or other stuff.
    * @param ic
    */
   public void navigateLeft(ExecutionContextCanvasGui ec) {

   }

   /**
    * Implement to the right horizontal traversal
    * @param ic
    */
   public void navigateRight(ExecutionContextCanvasGui ec) {

   }

   /**
    * 
    * @param ic
    */
   public void navigateSelect(ExecutionContextCanvasGui ec) {

   }

   /**
    * Call Back for Up Command
    *
    */
   public void navigateUp(ExecutionContextCanvasGui ec) {

   }

   public void notifyEvent(int event) {
      notifyEvent(event, null);
   }

   /**
    * Notify the {@link Drawable} for different life cycle events.
    * <br>
    * <br>
    * Override with care because it hijacks all dispatching event calls.
    * <br>
    * <br>
    * Events are notified after just after the state change.
    * <br>
    * <br>
    * <b>Animation Events</b> : <br>
    * Register for event with {@link DrawableAnim#register(IDrawable)}.
    * <br>
    * <li> {@link ITechDrawable#EVENT_06_ANIM_STARTED} when Drawable is target and {@link IAnimable#lifeStart()} is called
    * <li> {@link ITechDrawable#EVENT_05_ANIM_FINISHED} when {@link Drawable} root {@link IAnimable} is finished.
    * <br>
    * {@link DrawableAnim}
    * Used for hooking animation starts and stop and setting correct states.
    * When Finished, check if this is the last of its kind. Animation 
    * <br>
    * See {@link IDrawable} main documentation for life cycle details. <br>
    * <br>
    */
   public void notifyEvent(final int event, Object o) {
      //#debug
      toDLog().pEvent1(ToStringStaticGui.toStringEvent(event) + " : " + ToStringStaticGui.toStringStates(this), this, Drawable.class, "notifyEvent");

      switch (event) {
         case EVENT_01_NOTIFY_SHOW:
            notifyEventShow();
            break;
         case EVENT_02_NOTIFY_HIDE:
            notifyEventHide();
            break;
         case EVENT_03_KEY_FOCUS_GAIN:
            notifyEventKeyFocusGain((BusEvent) o);
            break;
         case EVENT_04_KEY_FOCUS_LOSS:
            notifyEventKeyFocusLost((BusEvent) o);
            break;
         case EVENT_05_ANIM_FINISHED:
            notifyEventAnimFinished(o);
            break;
         case EVENT_06_ANIM_STARTED:
            notifyEventAnimStarted(o);
            break;
         case EVENT_07_POINTER_FOCUS_LOSS:
            setStateStyle(STYLE_07_FOCUSED_POINTER, false);
            break;
         case EVENT_08_POINTER_FOCUS_GAIN:
            setStateStyle(STYLE_07_FOCUSED_POINTER, true);
            break;
         case EVENT_10_STYLE_STATE_CHANGE:
            //cast Object to event state change container.
            break;
         case EVENT_11_REFRESH_CLEAN:
            //cast Object to event state change container.
            notifyEventRefresh();
            break;
         default:
            break;
      }
      if (pactor != null) {
         pactor.notifyEvent(this, event, o);
      }
   }

   /**
    * Depending on {@link IAnimable} genetics,update the state flag of the {@link Drawable}.
    * <br>
    * Animation is flagged as a style layer animation
    * @param o finishing 
    */
   public void notifyEventAnimFinished(Object o) {
      if (da != null) {
         da.animFinished((IAnimable) o);
      }
      //do a repaint??
      vc.getRepaintCtrlDraw().repaintDrawableCycleBusiness(this);
   }

   /**
    * Notifies that the animation has effectively started. in some cases, it may take some time before the call hook
    * and the first animation frame is displayed.
    * <br>
    * This method is called in the anim Thread!
    * <br>
    * @param o
    */
   public void notifyEventAnimStarted(Object o) {
      if (da != null) {
         da.animStartedThreadAnim((IAnimable) o);
      }
   }

   /**
    * 
    */
   public void notifyEventHide() {
      if (da != null) {
         da.showExit();
      }
      if (!hasState(STATE_13_DISAPPEARING)) {
         setStateFlag(STATE_03_HIDDEN, true);
      }
   }

   public void notifyEventKeyFocusGain(BusEvent e) {
      setStateStyle(STYLE_06_FOCUSED_KEY, true);
   }

   /**
    * Called by {@link Drawable#notifyEvent(int, Object)} for event {@link ITechDrawable#EVENT_04_KEY_FOCUS_LOSS} generated by {@link Controller}.
    * <br>
    * Sets the {@link ITechDrawable#STYLE_06_FOCUSED_KEY} to false.
    * <br>
    * 
    */
   public void notifyEventKeyFocusLost(BusEvent e) {
      setStateStyle(STYLE_06_FOCUSED_KEY, false);
   }

   public void notifyEventRefresh() {

   }

   /**
    * {@link ITechDrawable#EVENT_01_NOTIFY_SHOW} Called when Drawable is soon to be drawn by the framework using {@link IDrawable#draw(GraphicsX)}.
    * <br>
    * <br>
    * Change the state {@link ITechDrawable#STATE_03_HIDDEN}. 
    * <br>
    * <br>
    * <br>
    * But super method must be called for flags to be set correctly.
    * <br>
    * <b>Animations</b>:
    * <br>
    * Look up {@link ITechAnim#ANIM_TIME_1_ENTRY} animation and hook it up setting the state {@link ITechDrawable#STATE_12_APPEARING}.
    * <br>
    * If none look for {@link ITechAnim#ANIM_TIME_0_MAIN}
    * <br>
    */
   public void notifyEventShow() {
      if (da != null) {
         da.notifyShow();
      }
      setStateFlag(STATE_03_HIDDEN, false);
   }

   /**
    * Drawable based animation.
    * <br>
    * Different from style layer based animation
    * @param type
    * @param flag {@link IBOStyle#STYLE_FLAG_C_6_ANIM_ENTRY} or {@link IBOStyle#STYLE_FLAG_C_7_ANIM_MAIN}
    * @param style
    */
   protected void processAnim(int type, int flag, ByteObject style) {
      if (style.hasFlag(STYLE_OFFSET_3_FLAG_C, flag)) {
         ByteObject anim = getStyleOp().getStyleDrw(style, STYLE_OFFSET_3_FLAG_C, flag);
         IAnimable ianim = gc.getAnimFactory().createDrawableAnimable(anim, this);
         addFullAnimation(ianim);
      }
   }

   /**
    * Look up any Style layer with an {@link IBOAnim} {@link IBOTypesGui#TYPE_GUI_11_ANIMATION} defined and load them in the {@link DrawableAnimator}.
    * <br>
    * <br>
    * Read all animations of the style figure layers.
    * <br>
    * <br>
    * 
    * Create a {@link FigDrawable} to track the animation of a DLayer.
    * <br>
    * <br>
    * Called when style is validated. Method must clean 
    * <br>
    * <br>
    * For instance, a background rectangle animating its color is a layer animation. Coupled with a Move drawable animation.
    * 
    * @param style
    */
   protected void processAnimFigure(ByteObject style) {

      ByteObject[] styleElements = style.getSubs();
      if (styleElements == null) {
         return;
      }

      //may define an animation on the content only.
      ByteObject contentFig = getStyleOp().getStyleDrw(style, IBOStyle.STYLE_OFFSET_1_FLAG_A, IBOStyle.STYLE_FLAG_A_1_CONTENT);
      //go over each style elements and inspect animated figures.
      for (int i = 0; i < styleElements.length; i++) {
         ByteObject styleSub = styleElements[i];
         if (styleSub != null && styleSub.getType() == IBOTypesDrawX.TYPE_DRWX_00_FIGURE) {
            if (styleSub.hasFlag(IBOFigure.FIG__OFFSET_02_FLAG, IBOFigure.FIG_FLAG_6_ANIMATED)) {
               if (styleSub == contentFig) {
                  //animate only the content? How?

               }
               //look up the layer at which it is

               int flag = getStyleOp().getStyleDLayerPosition(style, styleSub);
               //the animations of this style figure
               ByteObject[] animsDrw = styleSub.getSubs(IBOTypesGui.TYPE_GUI_11_ANIMATION);
               for (int j = 0; j < animsDrw.length; j++) {
                  if (animsDrw[j] != null) {
                     animsDrw[j].setValue(IBOAnim.ANIM_OFFSET_06_TARGET1, flag, 1);
                     FigDrawable fg = new FigDrawable(gc, gc.getDefaultStyles().getEmptyStyleClass(), styleSub);
                     fg.setBehaviorFlag(BEHAVIOR_04_CONTENT_ONLY, true);
                     //create an intermediary 
                     IAnimable ianim = gc.getAnimFactory().createDrawableAnimable(animsDrw[j], fg);
                     addAnimationLayer(ianim);
                  }

               }
            }
         }
      }

   }

   /**
    * Calls in order
    * <li> {@link Drawable#layoutInvalidate()}
    * <li> {@link Drawable#invalidateStyleClass()}
    * <li> {@link Drawable#initSize()}
    * <li> {@link Drawable#initPosition()}
    */
   public void rebuild() {
      this.layoutInvalidate();
      this.invalidateStyleClass();
      //if 
      this.layEngine.rebuild();
      this.initSize();
      this.initPosition();
   }

   public void removeDrawable(ExecutionContextCanvasGui ec, IDrawable newFocus) {
      vc.getDrawCtrlAppli().removeDrawable(ec, this, newFocus);
   }

   public void repaintDrawable() {
      vc.getRepaintCtrlDraw().repaintDrawableCycleBusiness(this);
   }

   /**
    * Called from a non ui thread, requesting 
    */
   public void repaintFromWorker() {
      this.getCanvas().getRepaintCtrlDraw().repaintDrawableCycleBusiness(this);
   }

   public void repaintLayoutable() {
      vc.getRepaintCtrlDraw().repaintDrawableCycleBusiness(this);
   }

   public int commandEvent(int evType, Object param) {
      return ITechCmd.PRO_STATE_0_CONTINUE;
   }

   /**
    * Manual override of sizers and pozers to a raw pixel value.
    * <br>
    * @param x
    * @param y
    * @param w
    * @param h
    */
   public void setArea(int x, int y, int w, int h) {
      layEngine.setManualOverride(true);
      layEngine.setX(x);
      layEngine.setY(y);
      layEngine.setW(w);
      layEngine.setH(h);
   }

   public void setArea(Zer2DArea area) {
      getLay().setArea(area);
   }

   public void setBehaviorFlag(int flag, boolean value) {
      behaviors = BitUtils.setFlag(behaviors, flag, value);
   }

   /**
    * Simply set the cache type property.
    * 
    */
   public void setCacheType(int type) {
      cacheType = type;
   }

   /**
    * The {@link CmdCtx} hierarchy is independant from the Drawable hierarchy.
    * <br>
    * But we want sometimes.... special set commands? or create a context
    * from Drawable parent cmd ctx?
    * 
    * When to initialize a {@link CmdCtx} in {@link Drawable} constructor?
    * 
    * @param cmdNode
    */
   public void setCmdNode(CmdNode cmdNode) {
      this.cmdNode = cmdNode;
   }

   public void setCtrlFlag(int flag, boolean value) {
      ctrl = BitUtils.setFlag(ctrl, flag, value);
   }

   /**
    * Custom Type -> see {@link Drawable#ctype} for discussion on the nature of this field.
    * 
    * <p>
    * Sets the CType. This state is not supposed to change.
    * </p>
    * 
    * <p>
    * Invalidates the style
    * </p>
    * @return
    */
   public void setCType(int ctype) {
      this.ctype = ctype;
      styleInvalidate();
   }

   public void setDependency(ILayoutable lay, int flags) {
      layEngine.setDependency(lay, flags);
   }

   /**
    * Sets h without any layout flag change
    * @param dh
    */
   protected void setDh(int dh) {
      this.layEngine.setH(dh);
   }

   protected void decrementDw(int incr) {
      this.layEngine.incrDh(-incr);
   }

   protected void decrementDh(int incr) {
      this.layEngine.incrDh(-incr);
   }

   protected void incrementDw(int incr) {
      this.layEngine.incrDh(incr);
   }

   protected void incrementDh(int incr) {
      this.layEngine.incrDh(incr);
   }

   /**
    * Sets dw and dh. Anyone can do that.
    * Those value will be changed during next init.
    * <br>
    * Should only be called by {@link IDrawListener} 
    * <br>
    * Also called in ViewPane to set new size when viewport is shrank
    * TODO This also updates sizers
    * <br>
    * Why?: {@link IDrawListener} must init the sizes.
    * Used by {@link TableView} to initialize Column Sizes.
    * <br>
    * <br>
    * 
    * @param dw
    * @param dh
    * @param styleIncluded
    * 
    * @see TableView#initListen(ByteObject, ByteObject, Drawable)
    */
   public void setDrawableSize(int dw, int dh, boolean styleIncluded) {
      this.setDw(dw);
      this.setDh(dh);
      if (!styleIncluded) {
         //we code this after because
         //when style needs DW as etalon to compute itself
         this.setDw(this.getDw() + getStyleWConsumed());
         this.setDh(this.getDh() + getStyleHConsumed());
      }

   }

   /**
    * Simple set dw as Width in engine. No other changes.
    * @param dw
    */
   protected void setDw(int dw) {
      this.layEngine.setW(dw);
   }

   /**
    * Link {@link Drawable} with the given navigational relationship.
    * <br>
    * Caller is on Top/Left/Right/Bot of
    * <br>
    * Erase previous relation.
    * <br>
    * Event is
    * <li> {@link INavigational#NAV_0_REFRESH}
    * <li> {@link INavigational#NAV_1_UP}
    * <li> {@link INavigational#NAV_2_DOWN}
    * <li> {@link INavigational#NAV_3_LEFT}
    * <li> {@link INavigational#NAV_4_RIGHT}
    * <li> {@link INavigational#NAV_5_SELECT}
    * <li> {@link INavigational#NAV_6_UNSELECT}
    * 
    * <br>
    * When true, link the inverse relationship
    * @param navEvent
    * @param d
    */
   public void setNavigate(int navEvent, IDrawable d, boolean doInverse) {
      this.setBehaviorFlag(BEHAVIOR_31_NAVIGATABLE, true);
      gc.getRepo().linkNav(this, navEvent, d);
      if (doInverse) {
         int navInverse = DrawableRepo.navInverse(navEvent);
         gc.getRepo().linkNav(d, navInverse, this);
      }
   }

   public void setParent(IDrawable d) {
      if (d == null) {
         throw new NullPointerException();
      }
      parent = d;
   }

   public void setPixelsH(int h) {
      layEngine.setOverrideH(h);
   }

   public void setPixelsW(int w) {
      layEngine.setOverrideW(w);
   }

   /**
    * <li> {@link ITechDrawable#SIZER_TYPE_0_MARGIN_FULL}
    * <li> {@link ITechDrawable#SIZER_TYPE_1_BORDER}
    * <li> {@link ITechDrawable#SIZER_TYPE_2_PADDING}
    * <li> {@link ITechDrawable#SIZER_TYPE_3_CONTENT}
    * @param typeW
    * @param typeH
    */
   public void setPozerTypes(int typeX, int typeY) {
      layEngine.setRelativePozerX(typeX);
      layEngine.setRelativePozerY(typeY);
   }

   public void setSizePixels(int w, int h) {
      layEngine.setOverrideW(w);
      layEngine.setOverrideH(h);
   }

   public void setSizePixelsAsSizers(int w, int h) {
      SizerFactory sizerFactory = gc.getLAC().getSizerFactory();
      ByteObject sizerPixelW = sizerFactory.getSizerPixel(w);
      this.setSizerW(sizerPixelW);
      ByteObject sizerPixelH = sizerFactory.getSizerPixel(h);
      this.setSizerH(sizerPixelH);
   }

   public void setSizePixelsInitSize(int w, int h) {
      layEngine.setOverrideW(w);
      layEngine.setOverrideH(h);

      this.initSize();
   }

   public void setSizer_WH_Pref() {
      setSizerW_Pref();
      setSizerH_Pref();
   }

   /**
    * Sets reference. Does not compute anything
    * @param h
    */
   public void setSizerH(ByteObject h) {
      layEngine.setSizerH(h);
   }

   public void setSizerH_Pref() {
      SizerFactory sizerFactory = gc.getLAC().getSizerFactory();
      ByteObject sizerPrefLazy = sizerFactory.getSizerPrefLazy();
      this.setSizerH(sizerPrefLazy);
   }

   public void setSizerHContent(ByteObject sizerH) {
      layEngine.setSizerH(sizerH);
      layEngine.setRelativeSizerH(ITechStyleCache.RELATIVE_TYPE_3_CONTENT);
   }

   /**
    * Sets references. Does not compute anything
    * @param w
    * @param h
    */
   public void setSizers(ByteObject w, ByteObject h) {
      setSizerW(w);
      setSizerH(h);
   }

   /**
    * <li> {@link ITechDrawable#SIZER_TYPE_0_MARGIN_FULL}
    * <li> {@link ITechDrawable#SIZER_TYPE_1_BORDER}
    * <li> {@link ITechDrawable#SIZER_TYPE_2_PADDING}
    * <li> {@link ITechDrawable#SIZER_TYPE_3_CONTENT}
    * @param typeW
    * @param typeH
    */
   public void setSizerTypes(int typeW, int typeH) {
      layEngine.setRelativeSizerW(typeW);
      layEngine.setRelativeSizerH(typeH);
   }

   /**
    * Sets reference. Does not compute anything
    * @param w
    */
   public void setSizerW(ByteObject w) {
      layEngine.setSizerW(w);
   }

   public void setSizerW_Pref() {
      this.setSizerW(gc.getLAC().getSizerFactory().getSizerPrefLazy());
   }

   public void setRelativePozerX(int typePozerX) {
      layEngine.setRelativePozerX(typePozerX);
   }

   public void setRelativePozerY(int typePozerY) {
      layEngine.setRelativePozerY(typePozerY);
   }

   /**
    * <li> {@link ITechStyleCache#RELATIVE_TYPE_0_MARGIN}
    * <li> {@link ITechStyleCache#RELATIVE_TYPE_1_BORDER}
    * <li> {@link ITechStyleCache#RELATIVE_TYPE_2_PADDING}
    * <li> {@link ITechStyleCache#RELATIVE_TYPE_3_CONTENT}
    * @param typeSizerH
    */
   public void setRelativeSizerH(int typeSizerH) {
      layEngine.setRelativeSizerH(typeSizerH);
   }

   /**
    * <li> {@link ITechStyleCache#RELATIVE_TYPE_0_MARGIN}
    * <li> {@link ITechStyleCache#RELATIVE_TYPE_1_BORDER}
    * <li> {@link ITechStyleCache#RELATIVE_TYPE_2_PADDING}
    * <li> {@link ITechStyleCache#RELATIVE_TYPE_3_CONTENT}
    * @param typeSizerH
    */
   public void setRelativeSizerW(int typeSizerW) {
      layEngine.setRelativeSizerW(typeSizerW);
   }

   /**
    * Changes the value for the flag.
    * <br>
   
    */
   public void setStateFlag(int flag, boolean value) {
      states = BitUtils.setFlag(states, flag, value);
   }

   private void setStateFlagPrivate(int flag, boolean value) {
      states = BitUtils.setFlag(states, flag, value);
   }

   /**
    * <li>Read the version of the class state.
    * <li>Branch based on version or throw an {@link IllegalArgumentException}
    * <li>Set state
    * @param vs
    */
   public void setStateFrom(ViewState vs) {
      int ver = vs.readVersion();
      if (ver == VER_DRAWABLE_01) {
         states = vs.readInt();
         behaviors = vs.readInt();
      } else {
         vs.wrongVersion(ver);
      }
   }

   public void setStateNav(int flag, boolean value) {
      statesNav = BitUtils.setFlag(statesNav, flag, value);
   }

   /**
    * Updates the style state. Look up {@link StyleClass} for a style change from the current and updates the active style.
    * 
    * <p>
    * The final style layer will be merged when the call to {@link Drawable#styleValidate()}.
    * 
    * </p>
    * 
    * <li> {@link ITechDrawable#STYLE_01_CUSTOM}
    * <li> {@link ITechDrawable#STYLE_02_CUSTOM}
    * <li> {@link ITechDrawable#STYLE_03_MARKED}
    * <li> {@link ITechDrawable#STYLE_04_GREYED}
    * <li> {@link ITechDrawable#STYLE_05_SELECTED}
    * <li> {@link ITechDrawable#STYLE_06_FOCUSED_KEY}
    * <li> {@link ITechDrawable#STYLE_07_FOCUSED_POINTER}
    * <li> {@link ITechDrawable#STYLE_08_PRESSED}
    * <li> {@link ITechDrawable#STYLE_09_DRAGGED}
    * <li> {@link ITechDrawable#STYLE_10_DRAGGED_OVER}
    * 
    * <br>
    * 
    * <p>
    * Generates specific animation only when state {@link ITechDrawable#STATE_06_STYLED} is set.
    * <b>Animation Concerns</b>:
    * {@link ITechDrawable#STATE_02_DRAWN}.
    *  No point animating a Drawable invisible.
    * 
    * </p>
    * 
    * 
    * 
    * StyleClass tracks whether new style implies an animation.
    * <br>
    * It is like an entry event for the state style.<br>
    * When set to false, and was true, check if style has an exit animations.
    * Check whether the new state implies a style change 
    * {@link StyleClass} tracks state style layers.
    * <br>
    * Case: no animation new style animate
    * <br>
    * @param styleFlag
    * @param value
    */
   public void setStateStyle(int styleFlag, boolean value) {

      //#debug
      String msgBase = "'" + ToStringStaticGui.debugStateStyle(styleFlag) + "' set to " + value;

      if (hasStateStyle(styleFlag) && !value || (value && !hasStateStyle(styleFlag))) {
         styleStates = BitUtils.setFlag(styleStates, styleFlag, value);
         //updates the style given by this style flag
         ByteObject stateStyle = styleClass.getStateStyle(styleFlag);
         if (stateStyle == null) {
            //no style updates
            toDLog().pState("NONE \t" + msgBase, this, Drawable.class, "setStateStyle", LVL_05_FINE, true);

         } else {

            //#debug
            toDLog().pState("OK \t" + msgBase, this, Drawable.class, "setStateStyle", LVL_05_FINE, true);

            //extract the IAnimable, ask the Da to animate
            //individual style may have anim
            if (stateStyle.hasFlag(STYLE_OFFSET_6_FLAG_PERF, STYLE_FLAG_PERF_4_ANIMS)) {
               processAnim(ITechAnim.ANIM_TIME_1_ENTRY, STYLE_FLAG_C_6_ANIM_ENTRY, stateStyle);
               processAnimFigure(stateStyle);
            }
            styleValidate();
            //TODO do we invalidate Layout?
            //in most cases, layout will not change.. how can we know if a style update changes the tblr values ?
            //we do have to invalidate opaqueness
            setStateFlag(STATE_18_OPAQUE_COMPUTED, false);
         }

      } else {
         //#debug
         toDLog().pState("IG \t" + msgBase, this, Drawable.class, "setStateStyle", LVL_05_FINE, true);
      }
   }

   public void setStateTo(ViewState vs) {
      vs.setVersion(VER_DRAWABLE_01);
      vs.writeInt(states);
      vs.writeInt(behaviors);
   }

   /**
    * 
    * See {@link Drawable#styleStruct} for a discussion of the nature of this field
    * 
    * <p>
    * This state is not supposed to change once set 
    * </p>
    * 
    * POST: Style is invalidated
    * 
    * @param style Must be incomplete
    */
   public void setStyleStruct(ByteObject style) {
      //#debug
      gc.getDC().getStyleOperator().checkStyleIncomplete(style);

      this.styleStruct = style;

      styleInvalidate();
   }

   /**
    * Sets new {@link StyleClass} only if not null.
    * <br>
    * <br>
    * Subclass must correctly update their style class by subclassing and calling this method
    */
   public void setStyleClass(StyleClass sc) {
      if (sc != null) {
         this.styleClass = sc;
         styleInvalidate();
      }
   }

   /**
    * Used internally, for state flags.
    * @param flag
    * @param value
    */
   protected void setSysFlag(int flag, boolean value) {
      states = BitUtils.setFlag(states, flag, value);
   }

   /**
    * 
    */
   public void setViewContext(ViewContext vc) {
      this.vc = vc;
   }

   /**
    * When overriden, the code should called the parent method first.
    * <br>
    * <br>
    * 
    */
   public void setViewState(ViewState vs) {
      if (vs != null) {
         //check we have our ClassID
         states = vs.int1States;
         behaviors = vs.int2Behaviors;
      }
   }

   /**
    * Invalidates X
    */
   public void setX(int x) {
      layEngine.setXInvalidate(x);
   }

   /**
    * Sets X and Y position as a Function.
    * <br>
    * The Boundary is always defined by the {@link ViewContext}.
    * <br>
    * 
    * Create a sizer with
    * {@link IPozer} anchoring relative to?
    * 
    * @param pozerX
    * @param pozerY
    */
   public void setXY(ByteObject pozerX, ByteObject pozerY) {
      layEngine.getLay().setPozerXStart(pozerX);
      layEngine.getLay().setPozerYTop(pozerY);
   }

   /**
    * When a Drawable is created, it is located at 0,0 relative to its {@link ViewContext}.
    * <br>
    * 
    * Convenience method towards 
    * <br>
    * This method is be overriden by container Drawables {@link ITechDrawable#BEHAVIOR_25_CONTAINER}
    * who layout drawable.
    * <br>
    * Containers must update shift all children when this method is called.
    * <br>
    * When position is a function inside a 2D rectangle, the position method
    * sets the X,Y.
    * <br>
    */
   public void setXY(int x, int y) {
      layEngine.setXY(x, y);
   }

   /**
    * Sets x and y and call {@link Drawable#initPosition()}
    * @param x
    * @param y
    */
   public void setXYInitPosition(int x, int y) {
      this.setXY(x, y);
      this.initPosition();
   }

   /**
    * Same as {@link IDrawable#setXY(int, int)} but with layout refresh.
    * @param x
    * @param y
    */
   public void setXYLay(int x, int y) {
      this.setX(x);
      this.setY(y);
      layoutInvalidatePosition();
   }

   /**
    * Activate Functional Positioning relative to the {@link ViewContext} or the Parent.
    * <br>
    * Anchors the Drawable
    * Set Drawable position logically to parent {@link Drawable} positioning or {@link MasterCanvas} if flag
    * {@link ITechDrawable#BEHAVIOR_15_CANVAS_POSITIONING} is set to true or the parent is null.
    * 
    * <li> {@link C#LOGIC_1_LEFT}
    * <li> {@link C#LOGIC_1_TOP_LEFT}
    * <li> {@link C#LOGIC_2_CENTER}
    * <li> {@link C#LOGIC_3_BOTTOM_RIGHT}
    * <li> {@link C#LOGIC_3_RIGHT}
    * 
    * This method is convenience around the definition of the Function definition.
    */
   public void setXYLogic(int xLogic, int yLogic) {
      layEngine.setXLogic(xLogic);
      layEngine.setYLogic(yLogic);
   }

   /**
    * Invalidates layout dependencies?
    */
   public void setY(int y) {
      layEngine.setY(y);
   }

   public void setZIndex(int zindex) {
      this.zindex = zindex;
   }

   public void shHideDrawable(ExecutionContextCanvasGui ec) {
      vc.getDrawCtrlAppli().shHideDrawable(ec, this);
   }

   /**
    * A class with several sub drawable must override this method to call the shift on its components.
    * 
    * The method invalidates all {@link ILayoutable} x and y dependencies.
    * 
    * Override
    */
   public void shiftXY(int dx, int dy) {
      int x = getX() + dx;
      this.setX(x);

      int y = getY() + dy;
      this.setY(y);
   }

   /**
    * Draws Drawable on {@link GraphicsX} with event control flow: animation, focus. 
    * <br>
    * <br>
    * Any {@link ByteObject#ANIM_TIME_1_ENTRY} animation is fired up. 
    * <br>
    * If animation requires it, Drawable stays hidden until the animation finishes.<br>
    * Then a final repaint is called and {@link Drawable#draw(GraphicsX)} do its thing. 
    * <br>
    * <br>
    * <b>Implementation Note</b> : <br>
    * <li>Sets {@link ITechDrawable#STATE_03_HIDDEN} to false. <br>
    * <li>Notify events {@link ITechDrawable#EVENT_01_NOTIFY_SHOW} and {@link ITechDrawable#EVENT_08_POINTER_FOCUS_GAIN}. <br>
    * <li>Finally, calls {@link IDrawable#draw(GraphicsX)}.
    * <br>
    * <br>
    * @param g
    */
   public void show(GraphicsX g) {
      this.notifyEvent(EVENT_01_NOTIFY_SHOW);
      draw(g);
   }

   public void shShowDrawable(ExecutionContextCanvasGui ec, int showType1Over) {
      vc.getDrawCtrlAppli().shShowDrawable(ec, this, showType1Over);
   }

   public void shShowDrawableOver(ExecutionContextCanvasGui ec) {
      CanvasDrawControl drawCtrlAppli = vc.getDrawCtrlAppli();
      drawCtrlAppli.shShowDrawableOver(ec, this);
   }

   public void stateReadFrom(StatorReader state) {
   }

   public void stateWriteTo(StatorWriter state) {
   }

   public void stateWriteToParamSub(StatorWriter state) {
   }

   public void stringUpdate() {
      //nothing to update here
   }
   //   protected void managePointerStateStyle(ExecutionContextCanvasGui ec) {
   //      if (ic.getSlaveDrawable() == this) {
   //         return;
   //      }
   //      ic.pointedDrawable = this;
   //      if (ic.isMoved()) {
   //         //sometimes we don't want it to be process.
   //         Controller.getMe().newFocusPointer(ic, this);
   //         if (parent != null && hasBehavior(BEHAVIOR_23_PARENT_EVENT_CONTROL)) {
   //            ic.setSlaveDrawable(this);
   //            parent.managePointerInput(ic);
   //         }
   //      } else if (ic.isPressed()) {
   //         setStateStyle(STYLE_08_PRESSED, true);
   //         PointerGesture pg = ic.getOrCreateGesture();
   //         //starts a simple gesture with no values
   //         pg.simplePress(this, ic);
   //         //Controller.getMe().draggedStarter(this, 0, 0, ic);
   //         ic.srActionDoneRepaint(this);
   //         if (parent != null && hasBehavior(BEHAVIOR_23_PARENT_EVENT_CONTROL)) {
   //            ic.setSlaveDrawable(this);
   //            parent.managePointerInput(ic);
   //         }
   //      } else if (ic.isReleased()) {
   //         setStateStyle(STYLE_08_PRESSED, false);
   //         setStateStyle(STYLE_09_DRAGGED, false);
   //         ic.srActionDoneRepaint(this);
   //         if (!DrawableUtilz.isInside(ic, this)) {
   //            ic.isOutside = true;
   //         }
   //         if (parent != null && hasBehavior(BEHAVIOR_23_PARENT_EVENT_CONTROL)) {
   //            ic.setSlaveDrawable(this);
   //            parent.managePointerInput(ic);
   //         }
   //         PointerGesture pg = ic.getOrCreateGesture();
   //         pg.simpleRelease(this, ic);
   //
   //         //Controller.getMe().draggedRemover(this, ic);
   //
   //      } else if (ic.isDragged()) {
   //         setStateStyle(STYLE_09_DRAGGED, true);
   //         ic.srActionDoneRepaint(this);
   //         if (parent != null && hasBehavior(BEHAVIOR_23_PARENT_EVENT_CONTROL)) {
   //            ic.setSlaveDrawable(this);
   //            parent.managePointerInput(ic);
   //         }
   //      } else if (ic.isWheeled()) {
   //         if (parent != null && hasBehavior(BEHAVIOR_23_PARENT_EVENT_CONTROL)) {
   //            ic.setSlaveDrawable(this);
   //            parent.managePointerInput(ic);
   //         }
   //      }
   //   }

   /**
    * 
    */
   protected void styleInvalidate() {
      setStateFlag(STATE_06_STYLED, false);
      setStateFlag(STATE_18_OPAQUE_COMPUTED, false);
      setStateFlag(STATE_07_CACHE_INVALIDATED, true);
      styleCache.invalidateValues();
   }

   /**
    * Changes the active style and therefore the {@link StyleClass}. 
    * 
    * <p>
    * Animation:
    * Construct {@link IAnimable} from style definition.
    * The state styles of the old root style are copied to the new merged style.
    * This method is called when the flag {@link ITechDrawable#STATE_06_STYLED} is set to true
    * When animation entry appears in a style update, it has no effect.
    * </p>
    * 
    * 
    */
   public void styleValidate() {

      style = styleClass.getStyle(styleStates, ctype, styleStruct);
      styleCache.setNewStyle(style);

      //check for global animations
      ByteObject currentStyle = getStyle();
      //remove animation in the array
      if (currentStyle.hasFlag(STYLE_OFFSET_6_FLAG_PERF, STYLE_FLAG_PERF_4_ANIMS)) {
         processAnim(ITechAnim.ANIM_TIME_1_ENTRY, STYLE_FLAG_C_6_ANIM_ENTRY, currentStyle);
         processAnim(ITechAnim.ANIM_TIME_0_MAIN, STYLE_FLAG_C_7_ANIM_MAIN, currentStyle);
         processAnim(ITechAnim.ANIM_TIME_2_EXIT, STYLE_FLAG_C_8_ANIM_EXIT, currentStyle);
      }
      processAnimFigure(currentStyle);

      //state style previously made are set.
      setStateFlag(STATE_06_STYLED, true);

      if (style.hasFlag(IBOStyle.STYLE_OFFSET_5_FLAG_X, IBOStyle.STYLE_FLAG_X_3_MERGED_STRUCT)) {
         this.invalidateLayout();
      }
   }

   public void toString(Dctx dc) {
      dc.root(this, Drawable.class, 3630);
      toStringPrivate(dc);
      super.toString(dc.sup());

      if (holes != null) {
         dc.append("Holes=");
         for (int i = 0; i < holes.length; i = +4) {
            dc.append('[');
            dc.append(holes[0]);
            dc.append(',');
            dc.append(holes[1]);
            dc.append(' ');
            dc.append(holes[2]);
            dc.append(',');
            dc.append(holes[3]);
            dc.append(']');
            dc.append(' ');

         }
      }
      dc.nlLvl1Line(vc);

      dc.nlLvl("", layEngine);

      dc.nlVarOneLineListNull("Parent", parent);
      dc.nlLvlList("StructStyle", styleStruct);

      if (ctype != 0) {
         dc.nlAppend("CType=" + ctype);
         //get the ctype style if it exists are stored in the StyleID space
         if (dc.hasFlagToString(gc, IToStringFlagsGui.D_FLAG_01_STYLE)) {
            ByteObject ctypeStyle = styleClass.getCTypeStyle(ctype);
            if (ctypeStyle != null) {
               //the style here does not have a static id yet
               dc.nlLvl("CTypeStyle", ctypeStyle);
            }
         }
      }
      //-----------------------------
      if (dc.hasFlagToString(gc, IToStringFlagsGui.D_FLAG_27_CACHE)) {
         dc.nlLvl("Cache " + ToStringStaticGui.debugCacheType(cacheType), cache);
      }

      //----------------
      if (states != 0 || behaviors != 0) {
         dc.nl();
         ToStringStaticGui.toStringStates(this, dc);
         dc.append(" ");
         if (styleStates != 0) {
            ToStringStaticGui.debugStateStyles(this, dc);
         }
         dc.append(" ");
         if (behaviors != 0) {
            ToStringStaticGui.toStringBehavior(this, dc);
         }

         dc.appendVarWithSpace("Opaque", isOpaque());

      }

      dc.nlLvl(styleCache, "styleCache");

      if (dc.hasFlagToString(gc, IToStringFlagsGui.D_FLAG_02_STYLE_CLASS)) {
         //cannot be null
         dc.nlLvl(styleClass);
      } else {
         dc.nlLvl1Line(styleClass);
      }

      //-----------------
      //style cannot be null
      if (dc.hasFlagToString(gc, IToStringFlagsGui.D_FLAG_01_STYLE)) {
         dc.nlLvl("Style", style);
      } else {
         dc.nl();
         dc.append("Style " + style.getMyHashCode());
      }
      if (dc.hasFlagToString(gc, IToStringFlagsGui.D_FLAG_04_ANIMATIONS)) {
         dc.nlLvl("DrawableAnimator", da);
      }

      dc.nlLvlList("Listener", pactor);
      dc.nlLvl(cmdNode, "CmdCtx");

      dc.listNulls();
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, Drawable.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
      if (parent != null && parent instanceof IDrawListener) {
         dc.append("Injected by '" + parent.toString1Line() + "'");
      }
      dc.append(toStringDimension());
   }

   public String toStringDimension() {
      return "[" + getX() + "," + getY() + " " + getDrawnWidth() + "," + getDrawnHeight() + "]";
   }

   public String toStringOneLineStates() {
      StringBBuilder sb = new StringBBuilder(gc.getUC());
      sb.append("");
      sb.append(ToStringStaticGui.toStringStates(this));
      return sb.toString();
   }

   private void toStringPrivate(Dctx dc) {
      dc.append(" x=" + getX() + " y=" + getY());
      dc.append(" dw=" + getDw());
      if (getDw() != getDrawnWidth()) {
         dc.append("[" + getDrawnWidth() + "]");
      }
      dc.append(" dh=" + getDh());
      if (getDh() != getDrawnHeight()) {
         dc.append("[" + getDrawnHeight() + "]");
      }
   }
   //#enddebug
}
