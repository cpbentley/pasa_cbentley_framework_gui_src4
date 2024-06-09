package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.stator.StatorReader;
import pasa.cbentley.core.src4.stator.StatorWriter;
import pasa.cbentley.core.src4.utils.BitUtils;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.coreui.src4.tech.ITechGestures;
import pasa.cbentley.framework.coreui.src4.utils.ViewState;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.engine.RgbImage;
import pasa.cbentley.framework.drawx.src4.string.StringMetrics;
import pasa.cbentley.framework.gui.src4.anim.move.Move;
import pasa.cbentley.framework.gui.src4.canvas.ExecutionContextGui;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.canvas.PointerGestureDrawable;
import pasa.cbentley.framework.gui.src4.canvas.ViewContext;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IToStringFlagsGui;
import pasa.cbentley.framework.gui.src4.ctx.ToStringStaticGui;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOViewPane;
import pasa.cbentley.framework.gui.src4.interfaces.IBODrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechViewDrawable;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.tech.ITechLinks;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;
import pasa.cbentley.framework.gui.src4.utils.DrawableUtilz;
import pasa.cbentley.framework.input.src4.gesture.GestureDetector;
import pasa.cbentley.layouter.src4.interfaces.ILayoutable;
import pasa.cbentley.layouter.src4.tech.IBOSizer;
import pasa.cbentley.layouter.src4.tech.ITechLayout;

/**
 * {@link Drawable} with a {@link ViewPane} that implements scrolling over Drawable's content bounded by its preferred size.
 * <br>
 * <br>
 * <b>Philosophy</b> <br>
 * We want the scrolling behavior to be genetically included inside the drawable.
 * <br>
 * We don't want the developer to wrap a "Scroll Pane" around Drawable that might need scrollling.
 * Our solution is elegant in that the ViewPane is created only when needed and the outside world will always see a single object reference.
 * <br>
 * The {@link ViewPane} is a layout component that brings Headers (Top,Bottom,Left and Right) and automatic scrolling support to the {@link Drawable} class. <br>
 * <br>
 * It also has the notion of <b>preferred</b> size which the Drawable does not implement (for simplicity Drawable preferred size 
 * equals DrawableSize. Because not all drawables need to express a preferred Size<br>
 * <br>
 * Preferred size is the size that the item would take without any constraints.
 * <li>actual content size
 * <li>decoration size from {@link Presentation#getStyleWConsumed(int)} and {@link Presentation#getStyleHConsumed(int)} .
 * <li>ViewPane's headers' sizes.
 * <br>
 * For the {@link ViewDrawable}, drawable size from {@link Drawable#getDrawnWidth()} include 
 * <li>ViewPane header
 * <li>ViewPane scrollbar.
 * <br>
 * <br>
 * <b>Shrink Semantics</b> : <br>
 * Specialize the meaning of {@link ITechViewDrawable#FLAG_GENE_29_SHRINKABLE_W} and {@link ITechViewDrawable#FLAG_GENE_30_SHRINKABLE_H}. <br>
 * {@link ViewDrawable} shrinks to preferred size<br>.
 * The purpose of Shrink behaviors is to reduce drawable dimension (dw and dh) to preferred dimension +
 * the planetaries of the {@link ViewPane} <br>
 * They have the meaning of {@link ITechViewPane#VISUAL_2_SHRINK} but are less specific and apply only to the case
 * where {@link ViewDrawable#getViewPortDecoW()} > pw. 
 * <br>
 * <br>
 * <b>Drawable's style layers when scrolled</b> : <br>
 * <li> Match the ViewPort Dimension. {@link ViewDrawable#drawDrawableStyledViewPort(GraphicsX, int, int, ScrollConfig, ScrollConfig)}. 
 * <li> Match the preferred dimension. {@link ViewDrawable#drawDrawableStyledContent(GraphicsX, int, int, ScrollConfig, ScrollConfig)}. See {@link ViewPane.TECH_VP_FLAG_6STYLE_TO_CONTENT}.
 * <br>
 * <br>
 * Adds support automatic dimension resize<br>
 * <br>
 * X,Y coordinates always controlled by Drawable. When ViewPane expands Drawable, it expands on dw and dh<br>
 * <br>
 * Drawable caching services apply to the ViewPane,Scrollbars and ViewDrawable. Those services are used
 * by Image Animations working the whole component.<br>
 * 
 * <br>
 * Scrolling behaviour and acceleration is managed by the {@link ViewPane} and the {@link ScrollConfig}.
 * <br>
 * Most Drawable will subclass this class and implement pseudo abstract method 
 * <li>{@link ViewDrawable#initViewDrawable(LayouterEngineDrawable)} 
 * <li>{@link ViewDrawable#initViewPortSub(int, int)} 
 * 
 * 
 * The {@link ViewDrawable} may time the drawContent method, in order to know whether to cache content or not for animations.

 * 
 * @author Charles-Philip Bentley
 *
 */
public class ViewDrawable extends Drawable implements ITechViewDrawable, ITechViewPane {

   public static final int VER_VIEW_DRAW_01 = 01;

   /**
    * ViewPane cannot be null
    * @param ic
    * @return
    */
   public static boolean isInsideViewPort(ViewPane viewPane, InputConfig ic) {
      return DrawableUtilz.isInside(ic, viewPane.getViewPortXAbs(), viewPane.getViewPortYAbs(), viewPane.getViewPortWidth(), viewPane.getViewPortHeight());
   }

   /**
    * Specific state for the {@link ViewDrawable} class, related to {@link ViewPane}.
    */
   protected int      flagsView;

   protected int      flagsGene;

   /**
    * The implicit view pane. It will manage drawable headers and scrollbars.
    * <br>
    * <br>
    * If not null, it will hijack some methods of the {@link Drawable} and route back the flow to this class after applying the {@link ViewPane} business logic
    * <br>
    * <br>
    * The {@link Drawable} may change the scrolling configuration using this reference. 
    * <br>
    * The {@link ViewPane} gets the following style states:
    * <li> {@link ITechDrawable#STYLE_07_FOCUSED_POINTER}
    * <br>
    * <br>
    * The {@link ViewPane} gets the following states linked from {@link ViewDrawable} states:
    * <br>
    * <br>
    * <li> {@link ITechDrawable#STATE_02_DRAWN}
    * <li> {@link ITechDrawable#STATE_03_HIDDEN}
    * <li> {@link ITechDrawable#STATE_02_DRAWN}
    * 
    */
   protected ViewPane viewPane;

   /**
    * Creates a blank ViewDrawable. No content
    * That figure will use preferred with and height
    * Subclass use this super constructor as well
    * <br>
    * <br>
    * @param style
    * @param fig
    */
   public ViewDrawable(GuiCtx gc, StyleClass sc) {
      super(gc, sc);
   }

   public ViewDrawable(GuiCtx gc, StyleClass sc, ViewContext vc) {
      super(gc, sc, vc);
   }

   /**
    * The sole purpose of shrink flag is to reduce drawable dimension (dw and dh) to preferred dimension the planetaries of the Viewpane .
    * <br>
    * Method updates the dw and dh
    * <br>
    * TODO Take into account the scrollbar
    * <br>
    * @return true if flags have been put to work, forcing the ViewPane to rework
    * to redimensioin the ViewPort and the Scrollbars
    */
   protected void activateShrinkExpandFlags() {
      int pw = layEngine.getPw();
      int dw = getDw();
      if (pw < dw) {
         if (layEngine.hasFlagSizerW(IBOSizer.SIZER_FLAG_2_ALLOW_SHRINK)) {
            setDw(pw);
         }
         if (hasFlagGene(FLAG_GENE_29_SHRINKABLE_W)) {
            setDw(pw);
         }
      } else {
         if(this.hasFlagView(FLAG_VSTATE_06_NO_EAT_W_MUST_EXPAND)) {
            setDw(pw);
         }
      }
      int ph = layEngine.getPh();
      int dh = getDh();
      if (ph < dh) {
         if (layEngine.hasFlagSizerH(IBOSizer.SIZER_FLAG_2_ALLOW_SHRINK)) {
            setDh(ph);
         }
         if (hasFlagGene(FLAG_GENE_30_SHRINKABLE_H)) {
            setDh(ph);
         }
      } else {
         if(this.hasFlagView(FLAG_VSTATE_07_NO_EAT_H_MUST_EXPAND)) {
            setDh(ph);
         }
      }
   }

   /**
    * Method called by Stringer?
    * <br>
    * The preferred size includes style.
    * <br>
    * When style dimension is relative preferred size ?
    */
   public void addStyleToPrefSize() {
      int styleHConsumed = getStyleHConsumed();
      int styleWConsumed = getStyleWConsumed();
      layEngine.incrPh(styleHConsumed);
      layEngine.incrPw(styleWConsumed);
   }

   public void addStyleToSizeDrawn() {
      int styleHConsumed = getStyleHConsumed();
      int styleWConsumed = getStyleWConsumed();
      layEngine.incrDh(styleHConsumed);
      layEngine.incrDw(styleWConsumed);
   }

   public void commandAction(CmdInstance cmd) {

   }

   /**
    * Create a ViewPane from scratch or from the Factory
    */
   private void createViewPaneIfNull() {
      if (viewPane == null) {
         StyleClass scViewPane = styleClass.getStyleClass(ITechLinks.LINK_65_STYLE_VIEWPANE);
         viewPane = new ViewPane(gc, scViewPane, this);
         //viewPane use the same context
         viewPane.setViewContext(getVC());
         //override sizer and pozers since viewpane is layout controlled
         viewPane.getLayEngine().setManualOverride(true);
         boolean isHidden = this.hasState(ITechDrawable.STATE_03_HIDDEN);
         viewPane.setStateFlag(ITechDrawable.STATE_03_HIDDEN, isHidden);
      }
   }

   public void doUndraw() {
      if (viewPane != null) {
         //viewpane overrides to propagate Drawn state
         viewPane.setStateFlag(ITechDrawable.STATE_02_DRAWN, false);
      }
      setStateFlag(ITechDrawable.STATE_02_DRAWN, false);
   }

   /**
    * Called by {@link Drawable#draw(GraphicsX)}. 
    * <br>
    * <br>
    * If component has a ViewPane, it delegates drawing flow control. Otherwise, it calls {@link ViewDrawable#drawViewDrawableContent(GraphicsX, int, int, ScrollConfig, ScrollConfig)}
    * with null Scrolling configurations
    * <br>
    * <br>
    */
   public final void drawDrawable(GraphicsX g) {
      if (viewPane != null) {
         //draw the view pane that will call the drawDrawable method 
         //no flow control for the ViewPane
         viewPane.draw(g);
      } else {
         //no viewpane then  it is not scrollable and does not have headers
         drawDrawableBg(g);
         drawViewDrawableContentCtrl(g, getContentX(), getContentY(), null, null);
         drawDrawableFg(g);
      }
   }

   /**
    * Overrides for updating only the content.
    * <br>
    * <br>
    * Scrollbars are considered decoration for the purpose of caching.
    * <br>
    * <br>
    * For the {@link ViewDrawable}, content is ViewPane and everything. Only Style layers are not content
    * <br>
    */
   public void drawDrawableContent(GraphicsX g) {
      drawDrawable(g);
      //drawViewDrawableContent(g, getContentX(), getContentY(), getSChorizontal(), getSCvertical());
   }

   /**
    * Entry Point for drawing a {@link ViewDrawable}.
    * <br>
    * <br>
    * Draws content data at x,y  on the scrolling configuration X and Y.
    * <br>
    * <br>
    * Called by {@link ViewPane} before scrollbars/headers are drawn. It is the responsability of this method to draw both decoration and content
    * <br>
    * <br>
    * When both scrolling configurations are null
    * <br>
    * <br>
    * @param g
    * @param x position x coordinate
    * @param y position y coordinate
    * @param scx may be null
    * @param scy may be null
    */
   public void drawViewDrawable(GraphicsX g, int x, int y, ScrollConfig scx, ScrollConfig scy) {

   }

   /**
    * Draws the content according to the horizontal and vertical {@link ScrollConfig}. 
    * <br> 
    * The number of increments drawns are decided by horizontal and vertical {@link ScrollConfig}s.
    * <br> 
    * Method is <b>implemented</b> by the specific {@link ViewDrawable}. such as {@link StringDrawable}, {@link ImageDrawable}, {@link TableView}.<br>
    * Independantly of content and ViewPort.
    * Clipping on Content Area is decided externally. <br>
    * <br>
    * ViewPane ensures that {@link ScrollConfig}:
    * <li>Match its ScrollType
    * <li>Type increment match {@link ViewDrawable} state using the method {@link ViewDrawable#initScrollingConfig(ScrollConfig, ScrollConfig)}
    * 
    * <br>
    * 
    * <br>
    * Usually subclass will call this before to draw it below specific content <br>
    * <br>
    * <br>
    * When both {@link ScrollConfig} are null, the {@link ViewDrawable} is not scrolled.
    * Draw content 
    * positioning of the content. Uses start,visible and direction.
    * When a {@link ScrollConfig} is null, draw all the content on that axis. <br>
    * Method should clip on the content area when necessary. <br>
    * <br>
    * <b>Time of Call:</b> <br>
    * Called after background layers have been drawn and before foreground layers are drawn.<br>
    * <br>
    * Animated Move : <br>
    * <li>Append : Draws new increment on an Image GraphicsX object.
    * <li>Whole : draw is moved
    * <br>
    * @param g {@link GraphicsX} object has been clipped already if necessary.
    * @param x x content coordinate
    * @param y y content coordinate
    * @param scx {@link ScrollConfig} for horizontal scrolling
    * @param scy {@link ScrollConfig} for vertical scrolling
    */
   public void drawViewDrawableContent(GraphicsX g, int x, int y, ScrollConfig scx, ScrollConfig scy) {
      //to be subclassed
   }

   public void drawViewDrawableContentCtrl(GraphicsX g, int x, int y, ScrollConfig scx, ScrollConfig scy) {
      //System.out.println("#ViewDrawable#drawViewDrawableContentCtrl " + toStringOneLine());
      if (hasState(ITechDrawable.STATE_22_ANIMATED_CONTENT_HIDDEN) && da != null) {
         //System.out.println("#ViewDrawable#drawViewDrawableContentCtrl Anim Content Hidden " + Thread.currentThread().toString());
         da.drawAnimations(g);
      } else {
         drawViewDrawableContent(g, x, y, scx, scy);
      }
   }

   /**
    * Draws style layers on ViewPort area using style of {@link Drawable}. When {@link ViewPane} is null, draws normally.
    * <br>
    * <br>
    * Scrolling animation will draw final state on a {@link RgbImage}. It moves original image down and moves destination image down
    * Therefore {@link ViewPane} must be able to create an image of any of scrolling state (dimension defined by visible increments).
    * <br>
    * <br>
    * @param g
    * @param x
    * @param y
    * @param scx
    * @param scy
    */
   //   protected void drawDrawableStyledViewPort(GraphicsX g, int x, int y, ScrollConfig scx, ScrollConfig scy) {
   //      //System.out.println("drawDrawableStyledViewPort x=" + x + " y=" + y);
   //      int contentX = x + MStyle.getStyleLeftWConsumed(style);
   //      int contentY = y + MStyle.getStyleTopHConsumed(style);
   //      int decoX = x;
   //      int decoY = y;
   //      int decoW = getViewPortDecoW();
   //      int decoH = getViewPortDecoH();
   //      drawDrawableBg(g, decoX, decoY, decoW, decoH);
   //      //MasterCanvas.getMasterCanvas().setClip(g, clipx, clipy, clipw, cliph);
   //      drawViewDrawableContent(g, contentX, contentY, scx, scy);
   //      //MasterCanvas.getMasterCanvas().resetClip(g);
   //      drawDrawableFg(g, decoX, decoY, decoW, decoH);
   //   }

   /**
    * Draw the Content layer using the preferred sizes. <br> 
    * Called after background layers have been drawn and before foreground layers are drawn.
    * Usually subclass will call this before to draw it below specific content
    * @param g {@link GraphicsX} object has been clipped already if necessary.
    * @param x shifted content root x coordinate
    * @param y shifted content root y coordinate
    */
   //   public void drawViewDrawableContent(GraphicsX g, int x, int y) {
   //      drawViewDrawableContent(g, x, y, null, null);
   //   }

   /**
    * Only draws the style elements of décoration
    * @param g
    * @param scX
    * @param scY
    */
   public void drawViewDrawableDeco(GraphicsX g, int x, int y, ScrollConfig scX, ScrollConfig scY) {

   }

   /**
    * 
    * @param g
    * @param x
    * @param y
    * @param scx
    * @param scy
    */
   //   public void drawViewDrawableStyle(GraphicsX g, int x, int y, ScrollConfig scx, ScrollConfig scy) {
   //      int styleValue = viewPane.getTech().get1(IViewPane.VP_OFFSET_7STYLE_MODE1);
   //      if (viewPane != null) {
   //         switch (styleValue) {
   //            case IViewPane.DRW_STYLE_0_TO_VIEWPORT:
   //               drawDrawableStyledViewPort(g, x, y, scx, scy);
   //               break;
   //            case IViewPane.DRW_STYLE_1_TO_VIEWPANE:
   //               drawDrawableStyledViewPane(g, x, y, scx, scy);
   //               break;
   //            case IViewPane.DRW_STYLE_2_IGNORED:
   //               drawDrawableStyledIgnore(g, x, y, scx, scy);
   //               break;
   //            default:
   //               break;
   //         }
   //         //drawDrawableStyledContent(g, x, y, scx, scy);
   //      } else {
   //         //when viewpane is null, just drawn normally
   //
   //         //in this case content and decoration cannot be cached together
   //         drawDrawableStyledViewPort(g, x, y, scx, scy);
   //      }
   //   }

   /**
    * When state no content is set, how to size drawable?
    * Minimum sizer by sizer ?
    * By Parent Sizer?
    */
   public void emptyW() {
      boStringDrawable.get1(IBODrawable.DTECH_OFFSET_02_FLAGX);
   }

   /**
    * 
    */
   public void eraseDrawableContent(GraphicsX g) {
      if (viewPane != null) {
         g.fillRect(getContentX(), getContentY(), getContentW(), getContentH());
      } else {
         g.fillRect(getX(), getY(), getDw(), getDh());
      }
   }

   /**
    * Return the actual {@link Drawable#dh} value.<br>
    * When there is a ViewPane with expanding planteries, this value is not to be used.
    * Therefore only the {@link ViewPane} should call this method.
    * @return
    */
   public int getActualDrawableH() {
      return getDh();
   }

   /**
    * Return the actual {@link Drawable#dw}
    * @return
    */
   public int getActualDrawableW() {
      return getDw();
   }

   /**
    * Keeps the semantics of {@link Drawable#getContentH()} and give the actual {@link ViewDrawable}'s content visual height size. <br>
    * When there is a {@link ViewPane} and has been {@link ITechDrawable#STATE_05_LAYOUTED}.
    * the method returns the ViewPort Height minus ViewPort's decoration H pixels. <br>
    * 
    */
   public int getContentH() {
      if (viewPane != null) {
         if (!viewPane.hasState(ITechDrawable.STATE_05_LAYOUTED)) {
            return super.getContentW();
         }
         return viewPane.getViewPortHeight();
      } else {
         return super.getContentH();
      }
   }

   /**
    * Returns the number of Pixels available for content of this {@link ViewDrawable} 
    * Removes pixel width of 
    * <li>viewpane style
    * <li>scrollbar
    * <li>borders and padding in case of {@link ITechViewPane#DRW_STYLE_0_VIEWDRAWABLE}.
    * <br>
    * <br>
    * The call ignores the ViewPane if it hasn't been initialized with the init method yet
    * <br>
    * <br>
    * TODO When {@link Drawable#init(int, int)} is made with -1 or 0 values, this method return something inconsistent.
    * <br>
    * @return
    */
   public int getContentW() {
      if (viewPane != null) {
         if (!viewPane.hasState(ITechDrawable.STATE_05_LAYOUTED)) {
            return super.getContentW();
         }
         return viewPane.getViewPortWidth();
      } else {
         return super.getContentW();
      }
   }

   /**
    * Keeps the semantics of {@link Drawable#getContentX()} and give the actual {@link ViewDrawable}'s content x coordinate, taking into
    * account ViewPane's planetaries.
    */
   public int getContentX() {
      if (viewPane != null) {
         return getX() + viewPane.getViewPortXOffset();
      } else {
         return super.getContentX();
      }
   }

   /**
    * Keeps the semantics of {@link Drawable#getContentY()} and give the actual {@link ViewDrawable}'s content y coordinate, taking into
    * account ViewPane's planetaries.
    */
   public int getContentY() {
      if (viewPane != null) {
         return getY() + viewPane.getViewPortYOffset();
      } else {
         return super.getContentY();
      }
   }

   public IDrawable getDrawable(int x, int y, ExecutionContextGui ex) {
      if (viewPane != null) {
         return viewPane.getDrawable(x, y, null);
      } else {
         return getDrawableViewPort(x, y, ex);
      }
   }

   public IDrawable getDrawableViewPort(int x, int y, ExecutionContextGui ex) {
      return this;
   }

   /**
    * The number of pixels consumed vertically by the component. <br>
    * It will include pixels of the {@link ViewPane}'s expansion pixels of scrollbars and headers. 
    * <br> For example an expanding Top planetary header {@link ITechViewPane#PLANET_MODE_1_EXPAND}.
    * 
    */
   public int getDrawnHeight() {
      if (hasBehavior(BEHAVIOR_16_IMMATERIAL)) {
         return 0;
      }
      if (viewPane != null) {
         //we compute the dh and add expansion pixel and minus 
         return getDh() + viewPane.getExpansionPixelsH();
      }
      return getDh();
   }

   /**
    * Returns masked data telling us if 
    * @param d
    * @return 0 if d is not a sattelite of this {@link ViewDrawable}
    */
   public int getSatteliteFlag(IDrawable d) {
      if(viewPane == null) {
         return 0;
      } else {
         return viewPane.getSatteliteFlag(d);
      }
   }
   /**
    * The number of pixels consumed horizontally by the component. 
    * <br>
    * It will include pixels of the {@link ViewPane} expansion pixels.
    */
   public int getDrawnWidth() {
      if (hasBehavior(BEHAVIOR_16_IMMATERIAL)) {
         return 0;
      }
      if (viewPane != null) {
         return getDw() + viewPane.getExpansionPixelsW();
      }
      return getDw();
   }

   public int getEtalonLogicSizeW(int value, boolean withStyle) {
      int ev = getLogicalWidth(value);
      if (withStyle) {
         ev += getStyleWConsumed();
      }
      return ev;
   }

   /**
    * Based on ViewPort
    * @return the number of increment currently visible
    * Default method works on pixels.
    * Subclass may choose another metric (Strings will use Lines/Characters/Words)
    */
   public int getHorizVisible() {
      if (viewPane != null) {
         return viewPane.getViewPortWidth();
      }
      return getDw();
   }

   /**
    * The number of height pixels needed to draw this ViewDrawable in that vertical configuration
    * @param scy
    * @return
    * Default behavior returns incrVisible as pixel value
    * Other class may interpret increment differently
    */
   public int getHPixelConsumed(ScrollConfig scy) {
      return scy.getSIVisible();
   }

   /**
    * The ViewDrawable returns
    */
   public ILayoutable getLayoutableViewPort() {
      //TODO object with for the viewport
      return this;
   }

   /**
    * Override this method to provide the logic metrics
    * @param h
    * @return
    */
   protected int getLogicalHeight(int h) {
      return 0;
   }

   /**
    * Returns the preferred Width of x units.
    * <br>
    * Override this method to provide the logic metrics.
    * x is 2, the width of 2 table columns
    * @param x
    * @return 0 by default, 
    * 
    * -1 if width could not be computed because ViewDrawable was not initialized
    */
   protected int getLogicalWidth(int x) {
      return 0;
   }

   /**
    * The preferred height without the decoration. 
    * Must be called after the initViewDrawable method returns. (i.e. when ph has been fully computed)
    * @return
    */
   public int getPreferredContentHeight() {
      if (viewPane == null) {
         return getSizePreferredHeight() - getStyleHConsumed();
      } else {
         return getSizePreferredHeight();
      }
   }

   /**
    * The preferred width without the decoration.
    * @return
    */
   public int getPreferredContentWidth() {
      if (viewPane == null) {
         return getSizePreferredWidth() - getStyleWConsumed();
      } else {
         return getSizePreferredWidth();
      }

   }

   /**
    * Returns the preferred height. Entirely content related. Totally unaware of {@link ViewPane}
    * in its computation.
    */
   public int getPreferredHeight() {
      return getSizePreferredHeight();
   }

   /**
    * The number of pixels this ViewDrawable's content needs horizontally
    * pw = dw <=> ViewPane == null.
    * <br>
    * The preferred width is computed based on ViewDrawable's content
    * and {@link LayouterEngineDrawable} definitions.
    * <br>
    * Value does not style pixels which are computed relative to preferred size.
    * <br>
    * The style is drawn 
    * <li> {@link ITechViewPane#DRW_STYLE_0_VIEWDRAWABLE}
    * <li> {@link ITechViewPane#DRW_STYLE_1_VIEWPANE}
    * 
    * Value is initialized by {@link StringMetrics}.
    * When {@link ITechViewPane#DRW_STYLE_3_INSIDE_CONTENT} is true, the style is content
    * and that case style is part of the preferred size.
    * Often {@link ImageDrawable} will use style as content.
    * <br>
    * When a ViewPane is created, the preferred size might increase due to a header
    * <br>
    * Preferred size does not decrease.
    * Preferred width. Entirely content related. Totally unaware of {@link ViewPane}
    * in its computation.
    */
   public int getPreferredWidth() {
      return getSizePreferredWidth();
   }

   public ScrollConfig getSChorizontal() {
      if (viewPane != null) {
         return viewPane.getScrollConfigHorizontal();
      }
      return null;
   }

   public ScrollConfig getSCvertical() {
      if (viewPane != null) {
         return viewPane.getScrollConfigVertical();
      }
      return null;
   }

   public int getSizeContentWidth() {
      return layEngine.getContentW();
   }

   public int getSizePreferredWidth() {
      return layEngine.getPw();
   }

   public int getSizePreferredHeight() {
      return layEngine.getPh();
   }

   public int getSizePropertyValueH(int sizeType) {
      if (sizeType == ITechLayout.SIZER_PROP_05_CONTENT) {
         return getViewPortContentH();
      } else if (sizeType == ITechLayout.SIZER_PROP_01_PREFERRED) {
         return getPreferredHeight();
      } else {
         return super.getSizePropertyValueH(sizeType);
      }
   }

   public int getSizePropertyValueW(int sizeType) {
      if (sizeType == ITechLayout.SIZER_PROP_05_CONTENT) {
         return getViewPortContentW();
      } else if (sizeType == ITechLayout.SIZER_PROP_01_PREFERRED) {
         return getPreferredWidth();
      } else {
         return super.getSizePropertyValueW(sizeType);
      }
   }

   /**
    * Content's drawable height. i.e.
    * Drawable height or when ViewPane, the height of the viewPane
    * @return
    */
   public int getVerticalVisible() {
      if (viewPane != null) {
         return viewPane.getViewPortHeight();
      }
      return getDh();
   }

   /**
    * 
    * Method used by the {@link ViewPane} for scrolling animations. RgbImage is fed to the {@link Move} animation. 
    * <br>
    * <br>
    * {@link ViewDrawable} has a cache layer for this function when Drawable
    * <br>
    * <br>
    * Gets a screenshot of the content
    * @param scx
    * @param scy
    * @return
    */
   public RgbImage getViewDrawableContent(ScrollConfig scx, ScrollConfig scy) {
      int iw = 0;
      int ih = 0;
      if (scx == null) {
         iw = getContentW();
      } else {
         iw = scx.getPixelVisibleSize();
      }
      if (scy == null) {
         ih = getContentH();
      } else {
         ih = scy.getPixelVisibleSize();
      }
      RgbImage ri = gc.getDC().getCache().create(iw, ih);
      GraphicsX g = ri.getGraphicsX();
      //we needed not erase of special stuff. full
      g.setPaintCtxFlag(ITechCanvasDrawable.REPAINT_01_FULL, true);
      boolean hasClip = hasFlagView(FLAG_VSTATE_01_CLIP);
      setFlagView(FLAG_VSTATE_01_CLIP, false);
      //
      drawViewDrawableContent(g, 0, 0, scx, scy);
      setFlagView(FLAG_VSTATE_01_CLIP, hasClip);
      return ri;
   }

   public ViewPane getViewPane() {
      return viewPane;
   }

   /**
    * Returns the content height inside the ViewPort. 
    * <br>
    * <br>
    * Like all viewport dimensions, values factors in ViewPane's artifacts and shrink values.
    * <br>
    * @return
    */
   public int getViewPortContentH() {
      return getViewPortDecoH() - getStyleHConsumed();
   }

   /**
    * Returns the content width inside the ViewPort. 
    * <br>
    * With no {@link ViewPane}, this returns the same as {@link ViewDrawable#getContentW()}
    * @return
    */
   public int getViewPortContentW() {
      return getViewPortDecoW() - getStyleWConsumed();
   }

   /**
    * Pixels consumed by content inside the View pane.
    * <br>
    * <br>
    * When {@link ViewPane} is null, returns dh
    * <br>
    * <br>
    * @return
    */
   public int getViewPortDecoH() {
      if (viewPane != null) {
         return viewPane.getViewPortHeight();
      }
      return getDh();
   }

   /**
    * Pixels consumed by content inside the {@link ViewPane}.
    * <br>
    * <br>
    * Includes content pixels and decoration pixels in case of {@link ITechViewPane#DRW_STYLE_0_VIEWDRAWABLE} or {@link ITechViewPane#STYLE_1_TO_CONTENT}
    * <br>
    * <br>
    * When {@link ViewPane} is null, returns dw
    * <br>
    * <br>
    * @return
    */
   public int getViewPortDecoW() {
      if (viewPane != null) {
         return viewPane.getViewPortWidth();
      }
      return getDw();
   }

   /**
    * <li>
    * @param flag
    * @return
    */
   public boolean hasFlagView(int flag) {
      return (flagsView & flag) == flag;
   }

   public boolean hasFlagGene(int flag) {
      return (flagsGene & flag) == flag;
   }

   /**
    * Sizing Process:
    * <li>Parent call init with sizers.
    * <li>When sizer is absolute and not contextual, the draw size is known
    * before computing preferred size
    * <li>When sizer is contextual with {@link ISizer#ET_TYPE_0_PREFERRED_SIZE}
    * the draw size is computed after the preferred size.
    * <li>Style sizes are computed last because they depends on the context.
    * <br>
    * <br>
    * The Draw size may increase when
    * <li> {@link ViewPane}'s planetaries are in expand mode
    * The Draw size may decrease when
    * <li> {@link ViewPane} has shrink flag {@link ITechViewPane#VISUAL_2_SHRINK} 
    * <li> {@link ViewDrawable} sizers are absolute but with shrink flag
    * <br>
    * <br>
    * 
    * A {@link ViewDrawable} has a preferred size.
    * <br>
    * <br>
    * This method initializes the <b>preferred</b> width and height of Drawable from the
    * {@link ISizer}s definitions.
    * <br>
    * <br>
    * PRE: Called by {@link Drawable#initSize()}
    * <br>
    */
   protected void initDrawableSub() {
      if (hasState(ITechDrawable.STATE_08_NO_RELAYOUTING)) {
         //we check again because we don't know what happened in the super method
         return;
      }
      //if we have a ViewPane with satellite, they might eat into the available space

      //first do init with raw values given. compute as if no viewpane
      //based on cue sizers, subclass computes its preferred size.
      //sizers might be contextual, init without any planetaries.
      initViewDrawable(layEngine);

      if (layEngine.isContextualW()) {
         //when width is contextual, headers cannot eat into our viewport area
         this.setFlagView(FLAG_VSTATE_06_NO_EAT_W_MUST_EXPAND, true);
         layEngine.layoutUpdateSizeWCheck();
      }
      if (layEngine.isContextualH()) {
         this.setFlagView(FLAG_VSTATE_07_NO_EAT_H_MUST_EXPAND, true);
         layEngine.layoutUpdateSizeHCheck();
      }

      //FILL or shrink? o

      //after the first call. dw and dh are positive if correctly initialized. set the dh,dw so the ViewPane can init itself
      //System.out.println("#ViewDrawable#init = #1 dw=" + dw + " dh=" + dh + " pw=" + pw + " ph=" + ph + " "+ this.toStringOneLine());
      //once dw and dh are computed from the context
      setPreferredTunedTDrawn();
      setFlagView(FLAG_VSTATE_08_PREF_SIZE_COMPUTED, true);

      if (this.toStringGetName() == "matcher") {
         toDLog().pInit("After First doInit", this, ViewDrawable.class, "initDrawable");
      }

      ////////////////  VIEWPANE \\\\\\\\\\\\\\\

      //ViewPane scrollbar creation
      boolean isScrolling = isScrollingNeeded();
      boolean isViewPaneNeeded = isScrolling || hasFlagView(ITechViewDrawable.FLAG_VSTATE_25_HEADERED);

      if (isViewPaneNeeded) {
         createViewPaneIfNull();

         //the viewpane starts with all the values except viewdrawable style if applied
         int viewPaneInitWidth = getDw();
         int viewPaneInitHeigh = getDh(); //or increased by Expand Planets
         viewPane.initViewPane(viewPaneInitWidth, viewPaneInitHeigh);
      } else {
         viewPane = null;
      }

      //at this stage if viewpane is null we can try shrinking ViewDrawable
      if (viewPane == null) {
         //viewdrawable first compute their preferred size without the style in case there is a viewpane
         //that will change the style dynamics.
         //when viewpane is null, preferred size is equal to drawn size
         addStyleToPrefSize();
         //Note: ViewPane deals with Shrinking by itself (How?). if not there, shrink.
         activateShrinkExpandFlags();
      }
      //when 
      //      if (viewPane != null && isMalleable()) {
      //         //init again with viewport dimensions at least headers planetaries.
      //         initViewRecursive();
      //      }

      ////////////////////////////////////////////////////////////
      //unload init style if any
   }

   /**
    * Initialize <b>preferred</b> width and height of Drawable with {@link ITechDrawable#DIMENSION_API} semantics.
    * <br>
    * <br>
    * Compute drawable width and height when :
    * <li>width/height is 0 or negative.
    * <br>
    * <br>
    * Based on those 4 values, a ViewPane may be created. <br>
    * <br>
    * Unless {@link IViewDrawable#VIEW_GENE_28_NOT_SCROLLABLE} is set, it creates a scrolling view when preferred sizes are bigger than
    * drawable sizes.
    * <br>
    * <br>
    * On the other hand if preferred sizes are smaller than drawable sizes, <b>shrink flags</b> will take effect
    * and reduce drawable sizes to preferred sizes.
    * <br>
    * <br>
    * Width and Height have each their own shrink flag {@link IViewDrawable#VIEW_GENE_29_SHRINKABLE_W} and {@link IViewDrawable#VIEW_GENE_30_SHRINKABLE_H}
    * <br>
    * <br>
    * Return early when state {@link ITechDrawable#STATE_05_LAYOUTED} is set and dimension is unchanged.
    * <br>
    * <br>
    * Once the drawn height and width are known, the super method {@link Drawable#init(int, int)} is called.
    * <br>
    * <br>
    * Calls {@link ViewDrawable#initViewDrawable(int, int)} at the end.
    * <br>
    * <br>
    */
   //   public void initDrawable(int width, int height) {
   //      if (true)
   //         throw new RuntimeException();
   //      if (hasState(ITechDrawable.STATE_08_NO_RELAYOUTING)) {
   //         //we check again because we don't know what happened in the super method
   //         return;
   //      }
   //      //first subclass initialization. dw and dh may be <= 0. init without any planetaries.
   //      initViewDrawable(dw, dh);
   //      //after the first call. dw and dh are positive if correctly initialized. set the dh,dw so the ViewPane can init itself
   //      //System.out.println("#ViewDrawable#init = #1 dw=" + dw + " dh=" + dh + " pw=" + pw + " ph=" + ph + " "+ this.toStringOneLine());
   //      setPreferredTunedTDrawn();
   //
   //      //first check if viewpane is needed.
   //
   //      //ViewPane scrollbar creation
   //      if (viewPane != null) {
   //         boolean isViewPaneNeeded = isScrollingNeeded() || hasState(ITechDrawable.STATE_30_HEADERED);
   //         if (isViewPaneNeeded) {
   //            //viewPane is not null because headers planetaries have been set
   //            viewPane.initViewPane(dw, dh);
   //         } else {
   //            viewPane = null;
   //         }
   //      } else if (!hasViewFlag(VIEW_GENE_28_NOT_SCROLLABLE) && isScrollingNeeded()) {
   //         //#mdebug
   //         toLog().printInit("ViewDrawable#initDrawable Creating View Pane because isScrollingNeeded=" + isScrollingNeeded() + " && " + !hasViewFlag(VIEW_GENE_28_NOT_SCROLLABLE) + " dw=" + dw + " dh="
   //               + dh + " pw=" + pw + " ph=" + ph + " " + this.toString1Line());
   //         //#enddebug
   //         if (viewPane == null) {
   //            createViewPane();
   //         }
   //         viewPane.initViewPane(dw, dh);
   //         viewPane.setStateFlag(ITechDrawable.STATE_03_HIDDEN, hasState(ITechDrawable.STATE_03_HIDDEN));
   //      }
   //      if (viewPane == null) {
   //         //Note: ViewPane deals with Shrinking by itself (How?). if not there, shrink.
   //         activateShrinkExpandFlags();
   //      }
   //      if (viewPane != null && isMalleable()) {
   //         //init again with viewport dimensions at least headers planetaries.
   //         initViewRecursive();
   //      }
   //      //unload init style if any
   //   }

   /**
    * Populate {@link ScrollConfig}s for a normal configuration.
    * <br>
    * <br>
    * Method flags {@link ScrollConfig} with {@link ScrollConfig#FLAG_1_UPDATED}. 
    * <br>
    * <br>
    * Called by {@link ViewPane#init(int, int)} when {@link ViewDrawable#init(int, int)} is called. After the first call to
    * <br>
    * <br> 
    * Based on content and startIncrement/direction, {@link ScrollConfig} values are set : 
    * <li>IncrementPixel<b>Sizes</b>
    * <li><b>Visible</b> ScrollingIncrements
    * <li><b>Total</b> ScrollingIncrements
    * <br>
    * <br>
    * <b>Partial Increments</b> : <br>
    * For {@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT} and {@link ITechViewPane#SCROLL_TYPE_2_PAGE_UNIT},
    * <br>
    * <br>
    * <b>Variable Increment Sizes</b> : <br>
    * <li> PartiallyVisibleIncrement is set.
    * 
    * <br>
    * <br>
    * <br>
    * <b>Explanation</b> : <br>
    * When scrollbars are created, they need to know the configuration of the {@link ViewDrawable} given a startIncrement and Direction. <br>
    * ViewPane uses this method for that purpose.
    * When first created, a {@link ScrollConfig} has a start increment of 0, Top by default.
    * <br>
    * <br>
    * <b>Default implementation</b> within {@link ViewDrawable#initScrollingConfig(ScrollConfig, ScrollConfig)} sets 
    * <li> visible increments = content width/height 
    * <li> total increments = preferred width/height
    * <br>
    * <br>
    * Called by {@link ViewPane} during initialization. {@link ViewPane#init} calls {@link ViewPane#updateScrollingConfig}.
    * <br>
    * <br>
    * @param scX {@link ScrollConfig} for the horizontal scrollbar. Null if no horizontal scrolling is possible.
    * @param scY {@link ScrollConfig} for the vertical scrollbar. Null if no vertical scrolling is possible.
    */
   public void initScrollingConfig(ScrollConfig scX, ScrollConfig scY) {

   }

   /**
    * Initialize the preferred size of the content to {@link LayouterEngineDrawable}.
    * That's all this method does. compute pw and ph from tentative dw and dh from
    * <li>  {@link LayouterEngineDrawable#getW()} the tentative size. 0 if none
    * <li>  {@link LayouterEngineDrawable#getH()} the tentative height. 0 if none.
    * <p>
    * This computation of the preferred size CANNOT include the style.
    * <b>Why?</b> Because when style is contextual to drawn size, :
    * Example. a 2 lines drawn String has a border that is 5 percent of min(dw,dh)
    * </p>
    * 
    * <p>
    * If required by lack of space, when a {@link ViewPane} is created, the method {@link ViewDrawable#initViewPortSub(int, int)}
    * will be called with the actual available space.
    * </p>
    * 
    * <p>
    * The {@link ViewDrawable} engine will account for it at a later stage. In the case of {@link IBOViewPane#VP_FLAGX_3_STYLE_CONTENT}, the {@link ViewDrawable} who calls
    * this method will add the style values.
    * </p>
    * 
    * <p>
    * May the size of headers influence the preffered size? For example, the MLogViewer title bigger than Shrank Width.
    * 
    * Yes when the header has a sizer and is added with {@link ViewPane#setHeaderSized(IDrawable, int, int)}.
    * </p>
    * 
    * 
    * @param ds {@link LayouterEngineDrawable#getW()}
    */
   protected void initViewDrawable(LayouterEngineDrawable ds) {

   }

   /**
    * The first init method computes the base size.
    * 
    * <li>Afterwards {@link ViewPane} call this update.
    * <li>Sub class overrides {@link ViewDrawable#initViewPortSub(int, int)}.
    * Where all it has to do is recalibrate the preferred size
    * <li>This method is called by the {@link ViewPane} when it has computed a new ViewPort dimension.
    * 
    * 
    * @param width positive ViewPort width size in pixels
    * @param height positive ViewPort height in pixels
    */
   public void initViewPort(int width, int height) {
      if (width <= 0) {
         //#debug
         toDLog().pInit("width=" + width + " height=" + height, this, ViewDrawable.class, "initViewPort");
         throw new IllegalArgumentException("" + width);
      }
      if (height <= 0) {
         if (gc.getUC().getConfigU().isForceExceptions()) {
            throw new IllegalArgumentException("" + height);
         } else {
            //#debug
            toDLog().pNull("height <= 0 width=" + width + " height=" + height, this, ViewDrawable.class, "initViewPort", LVL_05_FINE, false);
         }
      }

      //updates the preferred size of
      initViewPortSub(width, height);

   }

   /**
    * Called when a ViewPort is created or its size changed.
    * Sub class must
    * <li>Recompute the preferred size of the content.
    * <li>Recompute child elements with new ViewPort size.
    * <li>Ignore style border,padding and margin.
    * 
    * <p>
    * {@link ViewDrawable} handles styling on its own.
    * </p>
    * 
    * @param width Pixel size of the Viewport. Style is already taken out.
    * @param height Pixel size of the Viewport. Style is already taken out.
    */
   protected void initViewPortSub(int width, int height) {

   }

   public void initPosition() {
      super.initPosition();
      if (viewPane != null) {
         viewPane.initPosition();
      }
   }


   public void invalidateLayout() {
      super.invalidateLayout();
      setFlagView(FLAG_VSTATE_08_PREF_SIZE_COMPUTED, false);
   }

   /**
    * The ViewDrawable set Behavior to caching if content, viewpane has that behavior
    * ViewPane will have it if one headers or scrollbars have it.
    * @return
    */
   public boolean isCachingViewDrawable() {
      boolean b = false;
      if (viewPane != null) {
         b = viewPane.hasBehavior(ITechDrawable.BEHAVIOR_10_FORCE_CACHING);
      }
      return b || hasBehavior(ITechDrawable.BEHAVIOR_10_FORCE_CACHING);
   }

   /**
    * Should there be a gesture for that variable
    * <br>
    * @param sc
    * @param flag
    * @param pg
    */
   private void isDoGestureBoundaries(ScrollConfig sc, GestureDetector pg, int id) {
      boolean isDoX = false;
      if (sc != null) {
         int rangeXStart = 0;
         int rangeXEnd = sc.getSIStartRangeEnd();
         pg.setBoundaries(rangeXStart, rangeXEnd, id);
         pg.setCurrent(id, sc.getSIStart());
         isDoX = true;
      }
      pg.setFlags(PointerGestureDrawable.FLAG_14_VAL_DO_IT, isDoX, id);
   }

   /**
    * Returns <b>true</b> when content closely espouse the {@link ViewDrawable} size on either dimension.
    * <br>
    * <br>
    * That happens when either of those flags are set:
    * 
    * <li> {@link ITechViewDrawable#FLAG_VSTATE_10_CONTENT_PW_VIEWPORT_DW}
    * <li> {@link ITechViewDrawable#FLAG_VSTATE_11_CONTENT_PH_VIEWPORT_DH}
    * <li> {@link ITechViewDrawable#FLAG_VSTATE_12_CONTENT_W_DEPENDS_VIEWPORT}
    * <li> {@link ITechViewDrawable#FLAG_VSTATE_13_CONTENT_H_DEPENDS_VIEWPORT}
   
    * <br>
    * <br>
    * Returning true makes the {@link ViewDrawable#init(int, int)} method initialize twice when a ViewPane is there.
    * <br>And 3 times when scrollbars are created.
    * <br>
    * @return
    */
   public boolean isMalleable() {
      if (hasFlagView(FLAG_VSTATE_13_CONTENT_H_DEPENDS_VIEWPORT) || hasFlagView(FLAG_VSTATE_12_CONTENT_W_DEPENDS_VIEWPORT)) {
         return true;
      }
      return hasFlagView(FLAG_VSTATE_10_CONTENT_PW_VIEWPORT_DW) || hasFlagView(FLAG_VSTATE_11_CONTENT_PH_VIEWPORT_DH);
   }

   /**
    * A {@link ViewDrawable} with a {@link ViewPane} is opaque when {@link ViewPane} has {@link IBOViewPane#VP_FLAGX_1_STYLE_VIEWPANE}
    * and Vp style is opaque {@link DrawableUtilz#isOpaqueBgLayers(IDrawable)}.
    */
   public boolean isOpaque() {
      if (viewPane != null) {
         //check opaqueness of ViewPane style if shown, otherwise return no.
         boolean isViewPaneOpaque = viewPane.isOpaque();
         if (isViewPaneOpaque) {
            setStateFlag(ITechDrawable.STATE_17_OPAQUE, isViewPaneOpaque);
            setStateFlag(ITechDrawable.STATE_18_OPAQUE_COMPUTED, true);
         }
         return isViewPaneOpaque;
      } else {
         return super.isOpaque();
      }
   }

   /**
    * Returns true when preferred width or height is bigger than drawable width or height.
    * <br>
    * <br>
    * False otherwise or if genetically prevented from scrolling.
    * <br>
    * Scrolling is needed when preferred width is bigger than drawable width <br>
    * or <br>
    * when preferred height is smaller than drawable height 
    * @return
    */
   public boolean isScrollingNeeded() {
      if (hasFlagGene(FLAG_GENE_28_NOT_SCROLLABLE)) {
         return false;
      }
      return (layEngine.isPwBiggerThanDw() || layEngine.isPhBiggerThanDh());
   }

   /**
    * Recomputes the drawable/preferred sizes of the {@link ViewDrawable}.
    * <br>
    * <br>
    * Recall of initMethod with initial parameters.
    * <br>
    * Call this when <br>
    * <li>the content size has changed 
    * <li>when canvas size has changed
    * <li>Style key has changed
    * <br>
    * <br>
    * If drawable dimension changes. the parent drawable's layout is updated as well.
    * Changes to the scrollbar or viewpane headers
    * <br>
    * <br>
    * @param topDown
    */
   public void layoutInvalidate(boolean topDown) {
      super.layoutInvalidate(topDown);
      setFlagView(FLAG_VSTATE_08_PREF_SIZE_COMPUTED, false);
      if (viewPane != null) {
         viewPane.layoutInvalidate(false);
      }
   }

   /**
    * Override this method to set a trailer object based on scrollbar state!
    * <br>
    * @param scrollBar
    */
   public void manageDragCallBack(ScrollBar scrollBar) {
      // TODO Auto-generated method stub

   }

   /**
    * Sub class should call this when they want the ViewPane to manage Up-Down-Left-Right scrollbar control. <br>
    */
   public void manageKeyInput(InputConfig ic) {
      if (viewPane != null) {
         viewPane.manageKeyInput(ic);
      }
   }

   /**
    * Called by a {@link ViewDrawable} with a {@link ViewPane}  that wants scrolling to occur when some gestures
    * are made the user inside the viewport.
    * <br>
    * <br>
    * 
    * @param ic
    */
   public void managePointerGesture(InputConfig ic) {
      //System.out.println("#ViewDrawable#managePointerGesture " + pg.toStringOneLine());
      GestureDetector pg = ic.is.getOrCreateGesture(this);
      //how to stop the gesture? when a new press event occurs
      if (ic.isGestured()) {
         //#debug
         toDLog().pFlow("isGestured", this, ViewDrawable.class, "managePointerGesture", LVL_05_FINE, true);
         //do an update on the vector.
         if (pg.hasValueFlag(PointerGestureDrawable.ID_0_X, ITechGestures.FLAG_10_IS_GESTURING)) {
            int pos = pg.getPosition(PointerGestureDrawable.ID_0_X);
            viewPane.moveSetX(ic, pos);
         }
         if (pg.hasValueFlag(PointerGestureDrawable.ID_1_Y, ITechGestures.FLAG_10_IS_GESTURING)) {
            int pos = pg.getPosition(PointerGestureDrawable.ID_1_Y);
            //SystemLog.printFlow("#ViewDrawable#managePointerGesture setting Y start Increment to " + pos);
            viewPane.moveSetY(ic, pos);
         }
         //#debug
         String msg = "[" + pg.getPosition(PointerGestureDrawable.ID_0_X) + "," + pg.getPosition(PointerGestureDrawable.ID_1_Y) + "]";
         //#debug
         toDLog().pFlow(msg, this, ViewDrawable.class, "managePointerGesture", LVL_05_FINE, true);
         ic.srActionDoneRepaint(this);
      }
      if (viewPane != null) {
         if (ic.isPressed()) {
            //Gesture is auto started
            int pressedX = (getSChorizontal() != null) ? getSChorizontal().getSIStart() : -1;
            int pressedY = (getSCvertical() != null) ? getSCvertical().getSIStart() : -1;
            pg.simplePress(pressedX, pressedY, ic.is);
         }
         //the release is automatically 
         if (ic.isReleased()) {

            //we set the parameters for the release

            //sets the boundaries for both X and Y.
            isDoGestureBoundaries(getSChorizontal(), pg, PointerGestureDrawable.ID_0_X);
            isDoGestureBoundaries(getSCvertical(), pg, PointerGestureDrawable.ID_1_Y);

            //ask the gesture to generate a timer. what is the call back? ManagePointerInputViewPOrt method with a special time event?
            //out of the GUI thread? or inside? called Serially i guess. same idea as a Repeater
            int relX = (getSChorizontal() != null) ? getSChorizontal().getSIStart() : -1;
            int relY = (getSCvertical() != null) ? getSCvertical().getSIStart() : -1;

            //sets the hooks for the release call in the Drawable.

            pg.simpleReleaseGesture(ic.is, relX, relY, ITechGestures.GESTURE_1_BOUNDARY, null);
            //compute the gesture amplitude and generate a timer for modyfing scroll config start increment 
            //Controller.getMe().draggedRemover(this, ic);
         }
         if (ic.isDraggedPointer0Button0()) {
            //drag normally by modifying the scrolling start increment
            //translate pixels move into SI increment delta
            //pg.simpleDrag(this, ic);
            int pressedX = pg.getPressed(PointerGestureDrawable.ID_0_X);
            int pressedY = pg.getPressed(PointerGestureDrawable.ID_1_Y);
            if (pressedX != -1) {
               //because here an increment is equal to a pixel. 
               //otherwise translate dragged Vector to increment pixels.
               ScrollConfig sc = viewPane.getScrollConfigHorizontal();
               if (sc != null) {
                  int mod = sc.getIncrementSize(Math.abs(ic.getDraggedDiffX()), true);
                  if (ic.getDraggedDiffX() < 0) {
                     mod = -mod;
                  }
                  viewPane.moveSetX(ic, pressedX - mod);
               }
            }
            if (pressedY != -1) {
               ScrollConfig sc = viewPane.getScrollConfigVertical();
               if (sc != null) {
                  int mod = sc.getIncrementSize(Math.abs(ic.getDraggedDiffY()), true);
                  if (ic.getDraggedDiffY() < 0) {
                     mod = -mod;
                  }
                  viewPane.moveSetY(ic, pressedY - mod);
               }
            }
            ic.srActionDoneRepaint(this);
         }
      }

   }

   /**
    * Method flow from the {@link Controller}. The {@link ViewDrawable} delegates to the {@link ViewPane}.
    * <br>
    * <br>
    * Subclass overrides this method for managing content input. Super calling when subclass delegates.
    * <br>
    * <br>
    * Sub class must be able to call {@link Drawable#managePointerInput(InputConfig)} without going through {@link ViewDrawable} logic
    * <br>
    * <br>
    * It is dangerous for a sub class to override this method.
    * Most business must be handled by {@link ViewDrawable#managePointerInputViewPort(InputConfig)}.
    * <br>
    * <br>
    * When TBLR Drawables are slaves which event code is done by subclass, The ViewPane will 
    * call the method.
    * <br>
    * <br>
    * 
    * It is easy to get lost in the different calls
    * 
    * @see ViewPane#managePointerInput(InputConfig)
    * @see Drawable#managePointerInput(InputConfig)
    */
   public void managePointerInput(InputConfig ic) {
      //don't we want first Drawable top down management? not if viewpane exists
      if (viewPane != null) {
         //this method will call managePointerInputViewPort eventually if pointer is indeed inside
         viewPane.managePointerInput(ic);
      } else {
         //what to call before? viewport first
         //pointer is inside the ViewPort since there is none :)
         managePointerInputViewPort(ic);
         //TODO is that right?
         //when the ViewPort does not process the pointer event, asks the Drawable to set the states.

         //         if (!ic.isActionDone()) {
         //            super.managePointerInput(ic);
         //         }
      }
      //dangerous.. if user forget to set action done flag? in the viewport?
      if (!ic.isActionDone()) {
         //SystemLog.printFlow("#ViewDrawable#managePointerInput No Action in ViewPort/ViewPane => Calling Drawable#managePointerInput " + this.toStringOneLine());
         super.managePointerInput(ic);
      } else {
         //case when no viewpane satellites have been acted upon. So if no drawable was set any new states
         //the only remaining case is the content of the viewdrawable. so go up to Drawable states.
         if (!ic.sr.isDrawableStatesSet()) {
            super.managePointerStateStyle(ic);
         }
      }
   }

   /**
    * Called in the Top-Down approach.
    * <br>
    * {@link ViewDrawable} can still hi-jack by overriding {@link ViewDrawable#managePointerInput(InputConfig)}.
    * <br>
    * <br>
    * But the preferred way is to override this method and implement stuff here.
    * <br>
    * This method will be called even if the ViewPane is not there
    * @param ic
    */
   public void managePointerInputViewPort(InputConfig ic) {

   }

   /**
    * Are we sure to forward this style to the {@link ViewPane}?
    */
   public void notifyEventKeyFocusGain(BusEvent e) {
      super.notifyEventKeyFocusGain(e);
      if (viewPane != null) {
         viewPane.setStateStyle(ITechDrawable.STYLE_06_FOCUSED_KEY, true);
      }
   }

   /**
    * 
    */
   public void notifyEventKeyFocusLost(BusEvent e) {
      super.notifyEventKeyFocusLost(e);
      if (viewPane != null) {
         viewPane.setStateStyle(ITechDrawable.STYLE_06_FOCUSED_KEY, false);
      }
   }

   /**
    * Search for {@link IDrawable} in sattelite and remove it structurally.
    * <br>
    * May start an animation updating the size of the inner components
    * @param debug
    */
   public boolean removeDrawable(IDrawable d) {
      if (viewPane != null) {
         return viewPane.removeDrawable(d);
      }
      return false;
   }

   /**
    * Programmatically scroll {@link ViewDrawable}
    * @param startX
    * @param startY
    */
   public void scroll(int startX, int startY) {
      if (viewPane != null) {
         ScrollBar hSB = viewPane.getScrollBarH();
         if (hSB != null) {
            ScrollConfig config = hSB.getConfig();
            config.setSIStart(startX);
            hSB.doUpdateStructure();
         }
         ScrollBar vSB = viewPane.getScrollBarV();
         if (vSB != null) {
            ScrollConfig config = vSB.getConfig();
            config.setSIStart(startY);
            vSB.doUpdateStructure();
         }
         viewPane.initScrollingConfigs();
      }
   }

   /**
    * Sets the header {@link IDrawable} in the {@link ViewPane}.
    * 
    * <li>Calling this method invalidates the layout? Yes.
    * <li>TODO? May the size of headers influence the preffered size? MLogViewer title bigger than Shrank Width
    * Yes. Parameter
    * <li>Planetary have their settings include sizer. 
    * In case of a Title set as a top header.
    * <br>
    * 
    * <p>
    * 
    * The {@link ViewPane} gives a width constraint for Top and Bot headers.
    * This value must be respected unless. The sizer in parameters will try to influence
    * the ViewPane sizers, which were influenced by ViewDrawable sizers
    * <br>
    * Preferred size of a header will be decided by Cue Size given by ViewPane.
    * <br>
    * ViewPane header can have max sizer constraint, telling max 1/3 parent, min 1 logic for title string
    * with minimum having advantage. 
    * Is Eat mode is sizing? No. This mode is independant.
    * </p>
    * 
    * <p>
    * 
    * What if Sizer has a shrink flag? It will shrink
    * Parent of Headers is the ViewPane.
    * </p>
    * 
    * POST: Invalidated layout
    * 
    * @param d
    * @param pos {@link C#POS_0_TOP} or {@link C#POS_1_BOT} or {@link C#POS_2_LEFT} or {@link C#POS_3_RIGHT}
    * @param mode planetary mode {@link ITechViewPane#PLANET_MODE_0_EAT} or {@link ITechViewPane#PLANET_MODE_1_EXPAND}
    */
   public void setHeader(IDrawable d, int pos, int mode) {
      createViewPaneIfNull();
      viewPane.setHeader(d, pos, mode);
      setFlagView(ITechViewDrawable.FLAG_VSTATE_25_HEADERED, true);
      invalidateLayout();
   }

   /**
    * {@link ViewDrawable#setHeader(IDrawable, int, int)} with 
    * <li> {@link C#POS_0_TOP};
    * <li> {@link ITechViewPane#PLANET_MODE_0_EAT}
    * @param d
    */
   public void setHeaderEatTop(IDrawable d) {
      this.setHeader(d, C.POS_0_TOP, ITechViewPane.PLANET_MODE_0_EAT);
   }

   public void setHeaderEatCloseTop(IDrawable d) {
      this.setHeaderClose(d, C.POS_0_TOP, ITechViewPane.PLANET_MODE_0_EAT);
   }

   public void setHeaderEatCloseBot(IDrawable d) {
      this.setHeaderClose(d, C.POS_1_BOT, ITechViewPane.PLANET_MODE_0_EAT);
   }

   public void setHeaderEatCloseLeft(IDrawable d) {
      this.setHeaderClose(d, C.POS_2_LEFT, ITechViewPane.PLANET_MODE_0_EAT);
   }

   public void setHeaderEatCloseRight(IDrawable d) {
      this.setHeaderClose(d, C.POS_3_RIGHT, ITechViewPane.PLANET_MODE_0_EAT);
   }

   public void setHeadersNull() {
      if (viewPane != null) {
         viewPane.setHeadersNull();
      }
   }

   public void setHeaderEatBot(IDrawable d) {
      this.setHeader(d, C.POS_1_BOT, ITechViewPane.PLANET_MODE_0_EAT);
   }

   public void setHeaderExpandTop(IDrawable d) {
      this.setHeader(d, C.POS_0_TOP, ITechViewPane.PLANET_MODE_1_EXPAND);
   }

   public void setHeaderExpandBot(IDrawable d) {
      this.setHeader(d, C.POS_1_BOT, ITechViewPane.PLANET_MODE_1_EXPAND);
   }

   public void setHeaderEatLeft(IDrawable d) {
      this.setHeader(d, C.POS_2_LEFT, ITechViewPane.PLANET_MODE_0_EAT);
   }

   public void setHeaderEatRight(IDrawable d) {
      this.setHeader(d, C.POS_3_RIGHT, ITechViewPane.PLANET_MODE_0_EAT);
   }

   public void setHeaderExpandLeft(IDrawable d) {
      this.setHeader(d, C.POS_2_LEFT, ITechViewPane.PLANET_MODE_1_EXPAND);
   }

   public void setHeaderExpandRight(IDrawable d) {
      this.setHeader(d, C.POS_3_RIGHT, ITechViewPane.PLANET_MODE_1_EXPAND);
   }

   /**
    * 
    * @param d
    * @param pos
    */
   public void setHeaderClose(IDrawable d, int pos) {
      createViewPaneIfNull();
      viewPane.setHeaderClose(d, pos);
      setFlagView(ITechViewDrawable.FLAG_VSTATE_25_HEADERED, true);
      invalidateLayout();
   }

   public void setHeaderClose(IDrawable d, int pos, int type) {
      createViewPaneIfNull();
      viewPane.setHeaderClose(d, pos, type);
      setFlagView(ITechViewDrawable.FLAG_VSTATE_25_HEADERED, true);
      invalidateLayout();
   }

   public void setHeaderCloseTop(IDrawable d) {
      this.setHeaderClose(d, C.POS_0_TOP);
   }

   public void setHeaderCloseTopEx(IDrawable d) {
      this.setHeaderClose(d, C.POS_0_TOP, ITechViewPane.PLANET_MODE_1_EXPAND);
   }

   public void setHeaderCloseBotEx(IDrawable d) {
      this.setHeaderClose(d, C.POS_1_BOT, ITechViewPane.PLANET_MODE_1_EXPAND);
   }

   public void setHeaderCloseLeftEx(IDrawable d) {
      this.setHeaderClose(d, C.POS_2_LEFT, ITechViewPane.PLANET_MODE_1_EXPAND);
   }

   public void setHeaderCloseRightEx(IDrawable d) {
      this.setHeaderClose(d, C.POS_3_RIGHT, ITechViewPane.PLANET_MODE_1_EXPAND);
   }

   public void setHeaderCloseBot(IDrawable d) {
      this.setHeaderClose(d, C.POS_1_BOT);
   }

   public void setHeaderCloseLeft(IDrawable d) {
      this.setHeaderClose(d, C.POS_2_LEFT);
   }

   public void setHeaderCloseRight(IDrawable d) {
      this.setHeaderClose(d, C.POS_3_RIGHT);
   }

   /**
    * The {@link Drawable} header will size the whole {@link ViewDrawable} ?
    * 
    * 
    * @param d
    * @param pos
    * @param mode
    */
   public void setHeaderSized(IDrawable d, int pos, int mode) {
      createViewPaneIfNull();
      viewPane.setHeaderSized(d, pos, mode);
      setFlagView(ITechViewDrawable.FLAG_VSTATE_25_HEADERED, true);

   }

   public void stateReadFrom(StatorReader state) {
      super.stateReadFrom(state);
   }

   public void stateWriteTo(StatorWriter state) {
      super.stateWriteTo(state);
   }

   /**
    * Use this method for figure scrolling
    * @param w
    * @param h
    */
   public void setPreferredSize(int w, int h) {
      layEngine.setPh(h);
      layEngine.setPw(w);
   }

   public void rebuild() {
      if(viewPane != null) {
         viewPane.rebuild();
      }
      super.rebuild();
   }

   /**
    * if ph or pw are not computed, it takes the drawable.
    * Say the StringDrawable is empty. Ph and pw are 0. it was given
    * it may happen that ph or pw is not computed in a contextual sizer
    * <li>{@link ISizer#ET_TYPE_0_PREFERRED_SIZE}
    * <li>{@link ISizer#ET_TYPE_1_LOGIC_SIZE}
    * In case of logic, even if preferred size if 0, the size is computed
    * <br>
    * then what happens is the following
    * <li>drawable is made hidden cuz empty
    * <li>a default size if given : provided by sizer min size, parent min children sizes, viewContext min children sizes
    * <li>drawable is drawn with just its style as content. but doesn't PW includes style?
    * 
    * But what is drawable was given 0, ? TODO create a invisible drawable by size?
    * Depends on minimum size
    */
   protected void setPreferredTunedTDrawn() {
      int pw = getPreferredWidth();
      int ph = getPreferredHeight();
      if (getDw() <= 0) {
         setDw(pw);
      }
      if (getDh() <= 0) {
         setDh(ph);
      }
      if (ph == 0) {
         //preferred height was not computed by ViewDrawable
         if (getDh() > 0) {
            //takes the drawn. There won't be any scrolling since ph = dh.
            int newPh = getDh();
            layEngine.setPh(newPh);
         } else {
            //how can dh be 0 with style? style must be blank as well.
            //when drawable h 
            setBehaviorFlag(ITechDrawable.BEHAVIOR_02_EMPTY, true);
            throw new IllegalStateException("ph=" + ph + " dh =" + getDh());
         }
      }
      if (pw == 0) {
         if (getDw() > 0) {
            int newPw = getDw();
            layEngine.setPw(newPw);
         } else {
            setBehaviorFlag(ITechDrawable.BEHAVIOR_02_EMPTY, true);
            throw new IllegalStateException("pw=" + pw + " dw =" + getDw());
         }
      }
   }

   /**
    * Sets behaviour flags for the policy towards using extra unused pixel space that is when preferred size is smaller
    * than given size.
    * <li> {@link ITechViewDrawable#FLAG_GENE_29_SHRINKABLE_W}
    * <li> {@link ITechViewDrawable#VIEW_GENE_29_SHRINKABLE_H}
    * <br>
    * <br>
    * @param w
    * @param h
    */
   public void setShrink(boolean w, boolean h) {
      setFlagGene(FLAG_GENE_29_SHRINKABLE_W, w);
      setFlagGene(FLAG_GENE_30_SHRINKABLE_H, h);
   }

   /**
    * Propagates flag change to the {@link ViewPane} and its {@link ViewPane#setStateFlag(int, boolean)}
    * method.
    * <li> {@link ITechDrawable#STATE_03_HIDDEN}.
    * <li> {@link ITechDrawable#STATE_02_DRAWN}. 
    */
   public void setStateFlag(int flag, boolean value) {
      if (flag == ITechDrawable.STATE_03_HIDDEN || flag == ITechDrawable.STATE_02_DRAWN) {
         if (viewPane != null) {
            viewPane.setStateFlag(flag, value);
         }
      }
      super.setStateFlag(flag, value);
   }

   public void setStyleClass(StyleClass sc) {
      super.setStyleClass(sc);
      if (viewPane != null) {
         StyleClass scViewPane = styleClass.getStyleClass(ITechLinks.LINK_65_STYLE_VIEWPANE);
         viewPane.setStyleClass(scViewPane);
      }
   }

   public void setFlagView(int flag, boolean value) {
      if (flag == FLAG_VSTATE_02_REPAINTING_CONTENT) {
         //throw new NullPointerException();
      }
      flagsView = BitUtils.setFlag(flagsView, flag, value);
   }

   public void setFlagGene(int flag, boolean value) {
      if (flag == FLAG_VSTATE_02_REPAINTING_CONTENT) {
         //throw new NullPointerException();
      }
      flagsGene = BitUtils.setFlag(flagsGene, flag, value);
   }

   /**
    * Sets both x and y coordinate
    * The Drawable will draw its content and viewpane at this coordinate
    */
   public void setXY(int x, int y) {
      if (viewPane != null) {
         viewPane.setXY(x, y);
      }
      super.setXY(x, y);
   }

   //#mdebug

   public void toString(Dctx dc) {
      dc.root(this, ViewDrawable.class, 1890);
      toStringPrivate(dc);
      super.toString(dc.sup());
      dc.appendVarWithSpace("pw", layEngine.getPw());
      dc.appendVarWithSpace("ph", layEngine.getPh());

      dc.append(' ');
      dc.append("ContentXYWH=" + getContentX());
      dc.append(',');
      dc.append(getContentY());
      dc.append(' ');
      dc.append(getContentW());
      dc.append(',');
      dc.append(getContentH());

      ///
      dc.append(" ");
      if (viewPane != null) {
         dc.append("Actual=(" + getActualDrawableW() + "," + getActualDrawableH());
         dc.append(") ");
         dc.append(" ViewPortContent=(" + getViewPortContentW() + "," + getViewPortContentH());
         dc.append(")");
         dc.append(" ViewPortDeco=(" + getViewPortDecoW() + "," + getViewPortDecoH());
         dc.append(")");
      } else {
         dc.append("ViewPane is null");
      }

      dc.nl();
      ToStringStaticGui.toStringViewStates(this, dc);
      //sb.append(nl);
      dc.append("  ");
      ToStringStaticGui.toStringViewGenes(this, dc);
      //When not
      dc.append(" Malleable=" + isMalleable());
      dc.append(" ScrollingNeeded=" + isScrollingNeeded());

      if (dc.hasFlagData(gc, IToStringFlagsGui.D_FLAG_17_VIEW_PANE)) {
         //Debug the ViewPane
         dc.nlLvl(viewPane, "ViewPane");
      } else {
         dc.nlLvlNullTitleList("Viewpane", viewPane);
      }
   }

   private void toStringPrivate(Dctx dc) {
      if(viewPane == null) {
         dc.appendWithSpace("No ViewPane");
      } else {
         dc.appendWithSpace("With ViewPane");
      }
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ViewDrawable.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

}
