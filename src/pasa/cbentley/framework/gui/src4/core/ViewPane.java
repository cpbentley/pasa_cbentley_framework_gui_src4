package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.structs.IntBuffer;
import pasa.cbentley.framework.coreui.src4.utils.ViewState;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.engine.RgbImage;
import pasa.cbentley.framework.drawx.src4.style.StyleCache;
import pasa.cbentley.framework.gui.src4.anim.move.Move;
import pasa.cbentley.framework.gui.src4.anim.move.FunctionMove;
import pasa.cbentley.framework.gui.src4.anim.move.ITechMoveFunction;
import pasa.cbentley.framework.gui.src4.anim.move.MoveGhost;
import pasa.cbentley.framework.gui.src4.canvas.ExecutionContextGui;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.canvas.PointerGestureDrawable;
import pasa.cbentley.framework.gui.src4.canvas.ViewContext;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IFlagsToStringGui;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOScrollBar;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOViewPane;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.interfaces.IAnimable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechViewDrawable;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;
import pasa.cbentley.framework.gui.src4.utils.DrawableUtilz;
import pasa.cbentley.framework.input.src4.gesture.GestureDetector;
import pasa.cbentley.layouter.src4.ctx.LayouterCtx;
import pasa.cbentley.layouter.src4.engine.Zer2DArea;

/**
 * The {@link ViewPane} controls the layout of a {@link ViewDrawable}. 
 * 
 * It implements scrolling and mimic the Swing BorderLayout with TBLR headers.<br>
 * References to a {@link ViewPane} are only used by a {@link ViewDrawable}, i.e. an external class see a {@link ViewDrawable} and is unaware of the ViewPane.
 * <br>
 * The {@link ViewPane} is a {@link Drawable} and implements {@link IDrawable}.
 * <br>
 * <br>
 * {@link ViewPane#getDrawnWidth()} returns the width of the whole area and is the same as {@link ViewDrawable#getDrawnWidth()}. 
 * <br>
 * {@link ViewPane#getDrawnHeight()} returns the width of the whole area and is the same as {@link ViewDrawable#getDrawnHeight()}. 
 * <br>
 * {@link ViewPane} tricks the outside world like a virus inside the {@link ViewDrawable}.
 * {@link ViewDrawable} will be a container.
 * <br>
 * <br>
 * <b>Planetaries</b>
 * <br>
 * Around the {@link ViewDrawable}, the ViewPane control up to 8 <b>planetary</b> elements
 * <ol>
 * <li> Horizontal {@link ScrollBar}
 * <li> Vertical {@link ScrollBar}
 * <li> Top Header {@link IDrawable}
 * <li> Bottom Header {@link IDrawable}
 * <li> Left Header {@link IDrawable}
 * <li> Right Header {@link IDrawable}
 * <li> Top Close Header {@link IDrawable}, between the Top Header and Content.
 * <li> Bot Close Header {@link IDrawable}, between the Bottom Header and Content.
 * <li> Up to 5 holes. {@link Drawable}
 * </ol>
 * 
 * <b>How to position planetaries</b> : <br>
 * <li>{@link ITechViewPane#PLANET_MODE_0_EAT} : eat into the viewport.
 * <li>{@link ITechViewPane#PLANET_MODE_1_EXPAND} : expand the drawable width/height
 * <li>{@link ITechViewPane#PLANET_MODE_2_OVERLAY} : Drawn over the viewport.
 * <br>
 * To make a planetary invisible, set {@link ITechViewPane#PLANET_MODE_2_OVERLAY} and {@link ITechDrawable#STATE_03_HIDDEN}.
 * 
 * The selection can follow the viewport<br>
 * <br>
 * 
 * 
 * <b>Initialization</b> : <br>
 * 
 * The {@link ViewDrawable} initializes its preferred sizes when {@link ViewDrawable#init(int, int)} is called.
 * <br> Drawable sizes are computed and updated when: 
 * <li> width or height are negative, the drawable size is computed.
 * <li> they are positiv
 * <br>
 * shrink flags are applied. {@link ITechViewDrawable#FLAG_GENE_29_SHRINKABLE_W}
 * When shrinking gain is computed and given to the ViewPane. This value is the slack for non expandable headers and scrollbars.
 * <br>
 * <b>Visual Left Over</b> : <br>
 * <li>{@link ITechViewPane#VISUAL_2_SHRINK} Shrink. Impact on ViewPort 
 * <li>{@link ITechViewPane#VISUAL_1_PARTIAL} Draw partial. ViewPort unchanged but clip management + scroll config.
 * <li>{@link ITechViewPane#VISUAL_0_LEAVE} Leave space empty. Not very handsome and a waste of pixel area.
 * <li>{@link ITechViewPane#VISUAL_3_FILL} Fill. Some {@link ViewDrawable} like the {@link TableView} are able to give the left over room to the last <i>unit</i>.
 * Otherwise behaves like {@link ITechViewPane#VISUAL_0_LEAVE}. 
 * <br>
 * This is a way to reach the most fitting visual logical constraint based on a given pixel value. <br>
 * <br>
 * If the {@link ViewDrawable} hasn't enough room, a ViewPane is created and its init method {@link ViewPane#init(int, int)} is called. <br>
 * Scrollbars are created automatically on horizontally and/or vertically.
 * The ViewPane thus computes the ViewPort dimension and call the {@link ViewDrawable#initViewPort(int, int)} method.
 * The ViewPort size is the original size reduced by scrollbars, headers and ViewPane's decoration. <br>
 * <br>
 * 
 * 
 * The {@link ViewDrawable} x,y coordinates are decided by the ViewPane and the scrollbar design<br>
 * The ViewPane's (x,y) position is determined by {@link ViewDrawable#getX()} and {@link ViewDrawable#getY()}<br>
 * <br>
 * <b>Scrolling Configuration</b> : <br>
 * A scrolling configuration {@link ScrollConfig} links the scrollbar and the ViewDrawable so they draw the state of the scrolling
 * in sync.
 * <br>
 * <br>
 * <b>Types of Scrolling</b> : <br>
 * <li>{@link ITechViewPane#SCROLL_TYPE_0_PIXEL_UNIT} Pixel scrolling: ViewPane sees the content as an image.
 * <li>{@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT} Logical unit scrolling. There is a Master Direction because the ViewDrawable draws Units
 * <li>{@link ITechViewPane#SCROLL_TYPE_2_PAGE_UNIT} Page scrolling. Whole screen changes.
 * <br>
 * Around the clock scrolling is decided here and propagated to the {@link ScrollBar} tech {@link IBOScrollBar#SB_FLAG_7_AROUND_THE_CLOCK}.
 * Using {@link ViewDrawable#initScrollingConfig(ScrollConfig, ScrollConfig)}, the ViewDrawable. <br>
 * Scrolling acceleration is decided ???. That depends on the number of seconds the trigger is actioned. It is
 * the business of the Controller. <br>
 * <br>
 * <b>Holes</b> : <br>
 * The ViewPane scrollbars and headers may create empty areas. An external class may set an {@link IDrawable} into those holes. <br>
 * By default, the ViewPane draws a hole style given by child {@link IBOTypesGui#LINK_67_STYLE_VIEWPANE_HOLE} inside those areas.
 * <br>
 * <br>
 * <b>Competitive modes</b>:<br>
 * <li>No winners. Only case which creates holes
 * <li>Vertical wins
 * <li>Horizontal takes all
 * <li>Both take area. they overlap. this is not very good for pointer button overlapping.
 * <br>
 * <br> 
 * <b>Double Call</b>:
 * At first only a Vertical scrollbar is needed. However that scrollbar reduces the width and now preferred width
 * is bigger than ViewPort's width.
 * If the call to {@link ViewDrawable#initViewPort(int, int)} is smaller modifies the preferred size
 * <br>
 * <br>
 * <b>Navigation</b> : <br>
 * The method {@link ViewPane#navigateDown(InputConfig)}
 * <br>
 * <br>
 * <b>Content Animations</b> : <br>
 * Since a reference to {@link IDrawable#getDrawnWidth()} returns the width of the {@link ViewPane} and a
 * call to  {@link IDrawable#getX()} returns the coordinate of the ViewPane,
 * It allows to move a Drawable with its scrollbars and its headers being totally unaware of them. 
 * That's nice but how do we move <i>just</i> the {@link ViewDrawable}'s content 
 * using the {@link IDrawable#setXY(int, int)} method.
 * Use the flag Tech of the ViewPane. TECH_V
 * <br>
 * <b>Scrolling Animation</b> : <br>
 * <li>{@link Move} down animation is generated. {@link ViewPane#navigateDown(InputConfig)} is called.
 * <li>vertical {@link ScrollConfig} created with visible increment + 1 than the vertical scrollbar config.
 * <li>Image is created.
 * <li>
 * <br>
 * Moving a header is no feat, as the animation works directly on it. <br>
 * <br>
 * <b>Examples of use</b> : <br>
 * <li>Very long text in a popup
 * <li>Buttons in a control panel too tigh to display all buttons at the same time
 * <li>A full table
 * <br>
 * <b>Constraints on Headers</b> : <br>
 * TOP and BOTTOM Headers are set to WIDTH_CONSTRAINT<br>
 * LEFT and RIGHT Headers are set to HEIGHT_CONSTRAINT<br>
 * <br>
 * <b>Events</b> <br>
 * With pointer enabled devices, scrolling may occur when pointer is pressed and then dragged inside the ViewPort. 
 * Provides the same scrolling speed inside the IPhone.
 * Gesture detection.
 * <br>
 * <br>
 * <b>Optimizations</b> <br>
 * <li>Detect directional figures in Top/Bottom. Times the drawing of first. On an opaque {@link Image}
 * if {@link Drawable} has not transparent pixels.
 * <li>Manage single figure cache for scrollbar wrappers of equal size. 3 images for Top button. Normal/Greyed/Selected.
 * Image transform applied for other buttons.
 * <br>
 * @author Charles-Philip Bentley
 *
 * @see ScrollBar
 * @see ScrollConfig
 */
public class ViewPane extends Drawable implements ITechViewPane, ITechDrawable, ITechViewDrawable, IBOViewPane {

   private static final int HOLE_0_TOP_LEFT  = 0;

   private static final int HOLE_1_TOP_RIGHT = 1;

   private static final int HOLE_2_BOT_RIGHT = 2;

   private static final int HOLE_3_BOT_LEFT  = 3;

   public static int getAdd(Object param) {
      int add = 1;
      if (param != null && param instanceof Integer) {
         Integer i = (Integer) param;
         add = add * i.intValue();
      }
      return add;
   }

   static void shiftXY(IDrawable dr, int xd, int yd) {
      if (dr != null) {
         int nx = dr.getX() + xd;
         int ny = dr.getY() + yd;
         dr.setXY(nx, ny);
      }
   }

   private int          expandPixelsH      = 0;

   /**
    * Tracks the expansion pixels used by
    * <li> Sized Headers
    * <li> {@link ITechViewPane#PLANET_MODE_1_EXPAND} Headers
    * <li> {@link ITechViewPane#PLANET_MODE_1_EXPAND} Scrollbars
    */
   private int          expandPixelsW      = 0;

   public IDrawable     headerBottom;

   public IDrawable     headerBottomClose;

   /**
    * 0 topleft
    * 1 top right
    * 2 bot right
    * 3 bot left
    */
   private IDrawable[]  headerHoles;

   private IDrawable    headerLeft;

   private IDrawable    headerLeftClose;

   private IDrawable    headerRight;

   private IDrawable    headerRightClose;

   private IDrawable    headerTop;

   /**
    * Serves as secondary header to be used for column titles.
    */
   private IDrawable    headerTopClose;

   private ScrollBar    hScrollBar;

   public boolean       isAnimated         = false;

   private MoveGhost    move;

   /**
    * Object tracking the move animation horizontally
    * <br>
    * Once created. it is never set to null. It is reused or augmented.
    */
   private MoveGhost    moveHoriz;

   /**
    * {@link MoveGhost} animation tracking the 
    */
   private MoveGhost    moveVert;

   private int          originalPortHeight = -1;

   private int          originalPortWidth  = -1;

   public boolean       recomputing        = false;

   /**
    * With 2 block scrollbar there is 1 hole
    * With 2 wrapper scrollbar, there are 4 holes.
    */
   private IDrawable    scrollBarHole;

   /**
    * When 4 scrollbars, we have 4 holes.
    * 4 wrapping holes
    * <li>With 2 block scrollbar there is 1 hole
    * <li>With 2 wrapper scrollbar, there are 4 holes.
    */
   private IDrawable[]  scrollBarHoles;

   /**
    * Height component of ScrollBarPane Area. <br>
    * Computed in {@link ViewPane#layDimensionScrollbars()}. <br>
    * Computes the scrollbar Drawable Height dh. <br>
    * When Scrollbars are slaved to Headers, this value is equal to viewport's height.
    */
   int                  scrollBarHPaneH;

   /**
    * Width component of ScrollBarPane Area. <br>
    * Computed in {@link ViewPane#layDimensionScrollbars()}. <br>
    * Computes the scrollbar Drawable Width dw. <br>
    * When Scrollbars are slaved to Headers, this value is equal to viewport's width
    */
   int                  scrollBarHPaneW;

   int                  scrollBarVPaneH;

   int                  scrollBarVPaneW;

   /**
    * The amount of pixel shrank vertically because preferred height is smaller than ViewPort's height
    */
   private int          shrinkViewPortH    = 0;

   /**
    * The amount of pixel shrank vertically because preferred height is smaller than ViewPort's height.
    * <br>
    * Only happens if Drawable is malleable.
    */
   private int          shrinkViewPortW    = 0;

   /**
    * the amount of pixel shrank vertically because of the scroll visual {@link ITechViewPane#VISUAL_2_SHRINK}
    */
   private int          shrinkVisualH      = 0;

   /**
    * the amount of pixel shrank horizontally because of the scroll visual {@link ITechViewPane#VISUAL_2_SHRINK}
    */
   private int          shrinkVisualW      = 0;

   /**
    * When null, no style is applied at the ViewPane dimension.
    * <br>
    */
   protected ByteObject styleViewPane;

   /**
    * When null, no special styling is applied to ViewPort
    * <br>
    */
   protected ByteObject styleViewPort;

   /**
    * {@link IBOViewPane}
    */
   private ByteObject   boViewPane;

   /**
    * Tracks startInc and number of increment doubles currently managed by MoveGhost
    */
   private IntBuffer    trailScope;

   /**
    * Controlled component.
    */
   ViewDrawable         viewedDrawable;

   /**
    * Base value from which the actual Height is computed
    * Internal cache value. must reset to -1 at every update
    */
   private int          viewPortHeight     = -1;

   private StyleClass   viewPortSC;

   private StyleCache   viewPortStyleCache;

   /**
    * Internal cache value set to {@link ViewDrawable#getDrawnWidth()} in constructor. must reset to -1 at every update
    * 
    */
   private int          viewPortWidth      = -1;

   /**
    * Must not be null for up/down scrolling.
    * Scrollbar has the reference to the ScrollConfig
    */
   public ScrollBar     vScrollBar;

   public ViewPane(GuiCtx gc, StyleClass sc, ViewDrawable c) {
      super(gc, sc);
      satellites = new IDrawable[SAT_MAX_NUM];
      this.viewedDrawable = c;
      doUpdateStyleClass();
      trailScope = new IntBuffer(gc.getUCtx());

      //#debug
      this.toStringSetName("ViewPane");

   }

   protected void addAnimImage(RgbImage ri, int trail, MoveGhost move) {
      RgbDrawable rd = new RgbDrawable(gc, gc.getDefaultSC(), ri, 0, 0);
      move.addTrail(rd);
      move.setTrail(trail);
      //we are already clipped
      move.setSleep(200);

   }

   /**
    * Compute drawable pixels in excess visually when settings require it ie 
    * <br>
    * <br>
    * <li> {@link ViewPane#shrinkViewPortW}
    * <li> {@link ViewPane#shrinkViewPortH}
    * <br>
    * <br>
    * 
    * <li> {@link ITechViewDrawable#FLAG_GENE_29_SHRINKABLE_W}
    * <li> {@link ITechViewDrawable#FLAG_GENE_30_SHRINKABLE_H}
    * <br>
    * <br>
    * Shrinking only applies when {@link ViewDrawable} is shrinkable
    * <br>
    * This method is called after the planetaries have been sized.
    * <br>
    */
   private boolean applyBehaviorShrinking() {
      boolean isShrinkVP = false;
      if (boViewPane.get4Bits1(VP_OFFSET_06_VISUAL_LEFT_OVER1) == VISUAL_2_SHRINK) {
         //check the ph of the current visible increments.
         viewedDrawable.getViewPortDecoW();
      }
      //
      if (viewedDrawable.hasFlagView(FLAG_GENE_29_SHRINKABLE_W)) {
         int dw = viewedDrawable.getDrawnWidth();
         int decoW = getViewPaneHorizSpaceConsumed();
         int pw = viewedDrawable.getPreferredWidth();
         int shrink = dw - pw - decoW;
         //#debug
         toDLog().pInit("decoW=" + decoW + " pw=" + pw + " dw=" + dw + " shrink=" + shrink, this, ViewPane.class, "applyBehaviorShrinking", LVL_05_FINE, true);
         if (shrink > 0) {
            shrinkViewPortW = shrink;
            isShrinkVP = true;
         } else {
            shrinkViewPortW = 0;
         }
      }
      if (viewedDrawable.hasFlagView(FLAG_GENE_30_SHRINKABLE_H)) {
         int dh = viewedDrawable.getDrawnHeight();
         int decoH = getViewPaneVerticalSpaceConsumed();
         int ph = viewedDrawable.getPreferredHeight();
         int shrink = dh - ph - decoH;
         //#debug
         toDLog().pInit("decoH=" + decoH + " ph=" + ph + " dh=" + dh + " shrink=" + shrink, this, ViewPane.class, "applyBehaviorShrinking", LVL_05_FINE, true);
         if (shrink > 0) {
            isShrinkVP = true;
            shrinkViewPortH = shrink;
         } else {
            shrinkViewPortH = 0;
         }
      }
      return isShrinkVP;
   }

   private int botHeadersMod(int y) {
      if (boViewPane.get2Bits1(VP_OFFSET_04_HEADER_PLANET_MODE1) != PLANET_MODE_2_OVERLAY) {
         if (headerBottom != null) {
            y += headerTop.getDrawnHeight();
         }
         if (headerBottomClose != null) {
            y += headerBottomClose.getDrawnHeight();
         }
      }
      return y;
   }

   private void createSBHolesArray() {
      if (scrollBarHoles == null) {
         scrollBarHoles = new IDrawable[4];
      }
   }

   private void doUpdateDLayout(IDrawable d, boolean topDown) {
      if (d != null) {
         d.layoutInvalidate(topDown);
      }
   }

   public void doUpdateScrollbarStructures() {
      if (vScrollBar != null) {
         vScrollBar.updateStructure();
      }
      if (hScrollBar != null) {
         hScrollBar.updateStructure();
      }
   }

   /**
    * TODO make sure no bug when style set with {@link ITechViewPane#DRW_STYLE_1_VIEWPANE}
    */
   protected void doUpdateStyleClass() {
      boViewPane = styleClass.getByteObjectNotNull(IBOTypesGui.LINK_66_TECH_VIEWPANE);
      //how do you hijack the style?
      styleValidateViewPort();
      styleValidateViewPane();
   }

   public void doUpdateTechScrollbar() {
      if (hScrollBar != null) {
         hScrollBar.techUpdateViewPane(boViewPane);
      }
      if (vScrollBar != null) {
         vScrollBar.techUpdateViewPane(boViewPane);
      }
   }

   /**
    * Dragging a ViewDrawable with a press on the scrollbar hole.
    * <br>
    * @param ic
    */
   protected void draggingViewPane(InputConfig ic) {
      if (ic.isDraggedPointer0Button0()) {
         GestureDetector pg = ic.is.getOrCreateGesture(this);
         int nx = pg.getPressed(PointerGestureDrawable.ID_0_X) + ic.getDraggedVectorX();
         int ny = pg.getPressed(PointerGestureDrawable.ID_1_Y) + ic.getDraggedVectorY();
         viewedDrawable.setXY(nx, ny);
         ic.srActionDoneRepaint(viewedDrawable);
      } else if (ic.isReleased()) {
         GestureDetector pg = ic.is.getOrCreateGesture(this);
         pg.simpleRelease(ic.is);
      }
   }

   /**
    * ViewPane calls this method.
    * Decoration is either scrolled or fixed (that is transfer of decoration to viewport)
    * <br>
    * Calls {@link ViewDrawable#drawViewDrawableContent(GraphicsX, int, int, ScrollConfig, ScrollConfig)} for item to draw itself
    * <br>
    * <br>
    * @param g
    */
   public void drawContent(GraphicsX g, int cx, int cy, int cw, int ch) {

      if (isViewPortStyleApplied()) {
         //viewport style is applied to viewDrawable
         ByteObject styleViewPort = getStyleViewPort();
         int decoX = cx - viewPortStyleCache.getStyleWLeftAll();
         int decoY = cy - viewPortStyleCache.getStyleHTopAll();
         int decoW = cw;
         int decoH = ch;
         decoW += viewPortStyleCache.getStyleWAll();
         decoH += viewPortStyleCache.getStyleHAll();
         int[] styleAreas = viewPortStyleCache.getStyleAreas(decoX, decoY, decoW, decoH);
         getStyleOp().drawStyleBg(styleViewPort, g, decoX, decoY, decoW, decoH, styleAreas);
         drawContentInside(g, cx, cy, cw, ch);
         getStyleOp().drawStyleFg(styleViewPort, g, decoX, decoY, decoW, decoH, styleAreas);
      } else {
         drawContentInside(g, cx, cy, cw, ch);
      }
   }

   private void drawContentInside(GraphicsX g, int cx, int cy, int cw, int ch) {
      g.clipSet(cx, cy, cw, ch);
      ScrollConfig scx = getSChorizontal();
      ScrollConfig scy = getSCvertical();
      drawViewedDrawableContent(g, scx, scy, cx, cy);
      g.clipReset();
   }

   /**
    * Hijacker of the draw method.
    * <br>
    * Same principle with {@link Drawable}. If {@link ViewPane} is not opaque, redraw first opaque parent bglayers.
    * <br>
    * <br>
    * All Above intersecting DLayers
    * <br>
    * <br>
    */
   public void drawDrawable(GraphicsX g) {

      int cx = getViewPortXAbs();
      int cy = getViewPortYAbs();
      int cw = getViewPortWidth();
      int ch = getViewPortHeight();

      //check if we only have to repaint the content inside.
      //in that case repaint style bg and fg
      if (!g.hasPaintCtx(ITechCanvasDrawable.REPAINT_01_FULL) && viewedDrawable.hasFlagView(FLAG_VSTATE_02_REPAINTING_CONTENT)) {
         //we also have to draw ViewPane background or clear the area.
         g.clipSet(cx, cy, cw, ch);

         if (isViewPaneStyleApplied()) {
            drawDrawableBg(g);
         } else {
            g.setColor(g.getBgColor());
            g.fillRect(cx, cy, cw, ch);
         }
         drawContent(g, cx, cy, cw, ch);

         if (isViewPaneStyleApplied()) {
            drawDrawableFg(g);
         }
         g.clipReset();
         return;
      }

      //which sizer to use to draw the style viewport? we don't have
      //draw the style of the viewpane
      if (isViewPaneStyleApplied()) {
         drawDrawableBg(g);
      }

      // we must draw the content before planetaries who may be drawn in overlay

      drawContent(g, cx, cy, cw, ch);

      if (hasTech(VP_FLAG_3_SCROLLBAR_MASTER)) {
         drawHeaders(g);
         drawScrollBars(g);
      } else {
         drawScrollBars(g);
         drawHeaders(g);
      }

      //finally draws FG layers drawn at ViewPane dimensions.
      if (isViewPaneStyleApplied()) {
         drawDrawableFg(g);
      }

      //	 if (vScrollBar != null) {
      //	    g.setColor(0xFF0000);
      //	    vScrollBar.debugBB(g);
      //	 }
      //	 if (hScrollBar != null) {
      //	    g.setColor(0x00FF00);
      //	    hScrollBar.debugBB(g);
      //	 }
   }

   private void drawHeaders(GraphicsX g) {
      //draw the headers
      if (headerTop != null) {
         headerTop.draw(g);
      }
      if (headerTopClose != null) {
         headerTopClose.draw(g);
      }
      if (headerBottom != null) {
         headerBottom.draw(g);
      }
      if (headerLeft != null) {
         headerLeft.draw(g);
      }
      if (headerRight != null) {
         headerRight.draw(g);
      }
      drawHoles(g, headerHoles);

   }

   private void drawHoles(GraphicsX g, IDrawable[] ds) {
      if (ds != null) {
         for (int i = 0; i < ds.length; i++) {
            if (ds[i] != null) {
               ds[i].draw(g);
            }
         }
      }

   }

   private void drawScrollBars(GraphicsX g) {
      //now the scrollbars
      if (vScrollBar != null && !isScrollBarVertImmaterial()) {
         vScrollBar.draw(g);
      }
      if (hScrollBar != null && !isScrollBarHorizImmaterial()) {
         hScrollBar.draw(g);
      }
      //draw holes
      if (scrollBarHole != null) {
         scrollBarHole.draw(g);
      }
      drawHoles(g, scrollBarHoles);
   }

   protected void drawViewedDrawableContent(GraphicsX g, ScrollConfig scx, ScrollConfig scy, int cx, int cy) {
      viewedDrawable.drawViewDrawableContentCtrl(g, cx, cy, scx, scy);
   }

   /**
    * ViewPort height minus space consumed by Scrollbars when slaved.
    * @return
    */
   private int getBaseHeadersHeight() {
      int val = getViewPortHeightWithoutStyle();
      if (isViewPaneStyleApplied()) {
         val -= getStyleHConsumed();
      }
      //
      if (isHeaderMaster() && isScrollBarVertNotOverlay()) {
         val += getSbVertSpaceConsumed();
      }
      return val;
   }

   /**
    * Returns the width available to Top/Bottom headers <br>
    * Takes into account <br>
    * <li>ViewPort
    * <li>ScrollBar Space
    * <li>Header Expansion Size
    * <br>
    * @return
    */
   private int getBaseHeadersWidth() {
      int val = getViewPortWidthWithoutStyle();
      if (isViewPaneStyleApplied()) {
         val -= getStyleWConsumed();
      }
      if (isHeaderMaster() && isScrollBarHorizNotOverlay()) {
         val += getSbHorizSpaceConsumed();
      }
      return val;
   }

   public int getCompetTypeHeader() {
      return boViewPane.get1(VP_OFFSET_11_COMPETITION_HEADER_TYPE1);
   }

   public int getCompetTypeSb() {
      return boViewPane.get1(VP_OFFSET_10_COMPETITION_SB_TYPE1);
   }

   /**
    * x coordinate for drawing Left Most content of the {@link ViewPane}.
    * <br>
    * <br>
    * {@link ViewPane#getViewPortXAbs()}
    */
   public int getContentX() {
      int x = viewedDrawable.getX();
      if (isViewPaneStyleApplied()) {
         x += getStyleWLeftConsumed();
      }
      return x;

   }

   public int getContentY() {
      int y = viewedDrawable.getY();
      if (isViewPaneStyleApplied()) {
         y += getStyleHTopConsumed();
      }
      return y;
   }

   /**
    * The number of pixels for the vertical expansion
    * @return
    */
   int getExpansionHPixels() {
      int expansionHPixels = 0;
      if (isScrollBarVertExpand()) {
         if (vScrollBar != null) {
            expansionHPixels += vScrollBar.getSbVertSpaceConsumed();
         }
         if (hScrollBar != null) {
            expansionHPixels += hScrollBar.getSbVertSpaceConsumed();
         }
      }
      if (isTopHExpand()) {
         expansionHPixels += headerTop.getDrawnHeight();
      }
      if (isBotHExpand()) {
         expansionHPixels += headerBottom.getDrawnHeight();
      }
      return expansionHPixels;
   }

   /**
    * Width pixels used by scrollbars and headers in expansion mode. <br>
    * Pixels that are added to the Drawable width of {@link ViewDrawable#getDrawnWidth()}.
    * 
    * @return
    */
   int getExpansionWPixels() {
      int expansionWPixels = 0;
      if (isScrollBarHorizExpand()) {
         if (vScrollBar != null) {
            expansionWPixels += vScrollBar.getSbHorizSpaceConsumed();
         }
         if (hScrollBar != null) {
            expansionWPixels += hScrollBar.getSbHorizSpaceConsumed();
         }
      }
      if (isLeftHExpand()) {
         expansionWPixels += headerLeft.getDrawnWidth();
      }
      if (isRightHExpand()) {
         expansionWPixels += headerRight.getDrawnWidth();
      }
      return expansionWPixels;
   }

   private int getHeaderBotY() {
      int val = viewedDrawable.getY() + viewedDrawable.getDrawnHeight();
      if (isViewPaneStyleApplied()) {
         val -= getStyleHBotConsumed();
      }
      if (hasTech(VP_FLAG_3_SCROLLBAR_MASTER)) {
         if (vScrollBar != null) {
            val -= vScrollBar.getYShiftTop();
         }
         if (hScrollBar != null) {
            val -= hScrollBar.getYShiftBot();
         }
      }
      val -= headerBottom.getDrawnHeight();
      return val;
   }

   int getHeaderHConsumedBot() {
      int space = 0;
      if (headerTop != null) {
         space += headerBottom.getDrawnHeight();
      }
      if (headerTopClose != null) {
         space += headerBottomClose.getDrawnHeight();
      }
      return space;
   }

   int getHeaderHConsumedTop() {
      int space = 0;
      if (headerTop != null) {
         space += headerTop.getDrawnHeight();
      }
      if (headerTopClose != null) {
         space += headerTopClose.getDrawnHeight();
      }
      return space;
   }

   /**
    * The Y coordinate of the Bottom header.
    * @return
    */
   private int getHeaderOffsetBottomY(int yinput) {
      //root y coordinate
      int y = viewedDrawable.getY() + getViewPortHeight();
      //      if (hasStyleViewPane()) {
      //         y += MStyle.getStyleTopHConsumed(style);
      //      }
      y = topHeadersMod(y);
      if (!hasTech(VP_FLAG_3_SCROLLBAR_MASTER) && isScrollBarVertNotOverlay()) {
         y += getSbVertSpaceConsumed();
      }
      if (isBotHOverlay()) {
         y -= headerBottom.getDrawnHeight();
      }
      return y;
   }

   /**
    * X coordinate of the Right Header
    * 
    * @return
    */
   private int getHeaderOffsetRightX() {
      int x = viewedDrawable.getX() + getViewPortWidth();
      //      if (hasStyleViewPane()) {
      //         x += getStyleWLeftConsumed();
      //      }
      x = leftHeadersMod(x);
      if (!hasTech(VP_FLAG_3_SCROLLBAR_MASTER) && isScrollBarHorizNotOverlay()) {
         x += getSbHorizSpaceConsumed();
      }
      if (isRightHOverlay()) {
         x -= headerRight.getDrawnWidth();
      }
      return x;
   }

   /**
    * Pixel size consumed by non overlay Top/Bot headers
    * <br>
    * @return
    */
   public int getHeadersHConsumed() {
      int space = 0;
      space += getHeaderHConsumedTop();
      space += getHeaderHConsumedBot();
      return space;
   }

   public int getHeadersWConsumed() {
      int space = 0;
      space += getHeaderWConsumedLeft();
      space += getHeaderWConsumedRight();
      return space;
   }

   /**
    * Y coordinate of the "Header ViewPort"
    * @return
    */
   private int getHeaderTopY() {
      int val = viewedDrawable.getY();
      if (isViewPaneStyleApplied()) {
         val += getStyleHTopConsumed();
      }
      if (hasTech(VP_FLAG_3_SCROLLBAR_MASTER)) {
         if (vScrollBar != null) {
            val += vScrollBar.getYShiftTop();
         }
         if (hScrollBar != null) {
            val += hScrollBar.getYShiftTop();
         }
      }
      return val;
   }

   int getHeaderWConsumedLeft() {
      int space = 0;
      if (headerLeft != null) {
         space += headerLeft.getDrawnWidth();
      }
      if (headerLeftClose != null) {
         space += headerLeftClose.getDrawnWidth();
      }
      return space;
   }

   int getHeaderWConsumedRight() {
      int space = 0;
      if (headerRight != null) {
         space += headerRight.getDrawnWidth();
      }
      if (headerRightClose != null) {
         space += headerRightClose.getDrawnWidth();
      }
      return space;
   }

   /**
    * X coordinate of the "Header ViewPort"
    * @return
    */
   private int getHeaderX() {
      int val = viewedDrawable.getX();
      if (isViewPaneStyleApplied()) {
         val += getStyleWLeftConsumed();
      }
      if (hasTech(VP_FLAG_3_SCROLLBAR_MASTER)) {
         //when scrollbar is overlay. we don't have a shift.
         if (vScrollBar != null) {
            val += vScrollBar.getXShiftLeft();
         }
         if (hScrollBar != null) {
            val += hScrollBar.getXShiftLeft();
         }
      }
      return val;
   }

   private int getHeaderXRight() {
      int val = viewedDrawable.getX() + viewedDrawable.getDrawnWidth();
      if (isViewPaneStyleApplied()) {
         val -= getStyleWRightConsumed();
      }
      if (hasTech(VP_FLAG_3_SCROLLBAR_MASTER)) {
         //when scrollbar is overlay. we don't have a shift.
         if (vScrollBar != null) {
            val -= vScrollBar.getXShiftRight();
         }
         if (hScrollBar != null) {
            val -= hScrollBar.getXShiftRight();
         }
      }
      val -= headerRight.getDrawnWidth();
      return val;
   }

   public IDrawable getDrawable(int x, int y, ExecutionContextGui ex) {
      if (isInsideViewPort(x, y)) {
         return viewedDrawable.getDrawableViewPort(x, y, ex);
      } else {
         //else try to see if any sattelite process the event 
         return getDrawableFromSattelites(x, y, ex);
      }
   }

   /**
    * Flags for checking if holes, sb etc.
    */
   public int          satFlags;

   private IDrawable[] satellites = new IDrawable[SAT_MAX_NUM];

   public IDrawable getDrawableFromSattelites(int x, int y, ExecutionContextGui ex) {
      IDrawable d = null;
      for (int i = 0; i < satellites.length; i++) {
         //to avoid array access check flag
         if (isNotNull(i)) {
            d = managePointerInputHelper(x, y, satellites[i], ex);
            if (d != null) {
               return d;
            }
         }
      }
      return d;
   }

   private boolean isNotNull(int index) {
      int flag = 1 << index;
      return (satFlags & flag) == flag;
   }

   /**
    * The pixel horizontal lines consumed by the whole viewpane and its slaved items (headers and scrollbars)
    * @return
    */
   public int getHorizSpaceConsumed() {
      return getDw();
   }

   public ScrollBar getHScrollBar() {
      return hScrollBar;
   }

   /**
    * Returns the maximum width size of Headers Top/Bot.
    * <br>
    * Reads {@link ViewDrawable} maxsize and removes Scrollbar and Headers Left/Top
    * and style.
    * <br>
    * We need Headers to be initialized before calling this method
    * @return -1 when no max size
    */
   public int getMaxHorizHeaderSize() {
      int maxWidth = getViewDrawable().getLayEngine().getSizePotentialW();
      if (maxWidth != -1) {
         int headerCompet = getCompetTypeHeader();
         boolean isSBMaster = isHeaderMaster();
         int sbW = getSbHorizSpaceConsumed();
         if (isSBMaster) {
            maxWidth -= sbW;
         }
         if (headerCompet == COMPET_HEADER_0_NEUTRAL || headerCompet == COMPET_HEADER_2_VERTICAL) {
            maxWidth -= getHeadersWConsumed();
         } else if (headerCompet == COMPET_HEADER_3_WHEEL) {
            maxWidth -= getHeaderWConsumedRight();
         } else if (headerCompet == COMPET_HEADER_4_WHEEL_CCW) {
            maxWidth -= getHeaderWConsumedLeft();
         }
         return maxWidth - getViewPaneHorizSpaceConsumed();
      }
      return -1;
   }

   /**
    * 
    * @param dir
    * @param screenResultCause
    * @return
    */
   private FunctionMove getMoveFunction(int dir, int moveSize) {
      int xo = viewedDrawable.getContentX();
      int yo = viewedDrawable.getContentY();
      int xd = xo;
      int yd = yo;
      switch (dir) {
         case C.DIR_1_BOTTOM:
            yd = yo - moveSize;
            break;
         case C.DIR_3_RIGHT:
            xd = xo - moveSize;
            break;
         case C.DIR_0_TOP:
            yd = viewedDrawable.getContentY();
            yo = yd - moveSize;
            break;
         case C.DIR_2_LEFT:
            xd = viewedDrawable.getContentX();
            xo = xd - moveSize;
            break;
         default:
            break;
      }

      FunctionMove mf = new FunctionMove(gc, ITechMoveFunction.TYPE_MOVE_0_ASAP, xo, yo, xd, yd);
      mf.setIncrementType(ITechMoveFunction.INCREMENT_1_FIB);
      return mf;
   }

   /**
    * 
    * @param ic
    * @param ri {@link RgbImage} to move
    * @param mf
    * @param trail
    * @return
    */
   private MoveGhost getMoveGhostHoriz(InputConfig ic, int moveSize, int moveSizeIncr, FunctionMove mf, int trail, ScrollConfig base) {
      ScrollConfig opposite = getSCvertical();
      if (moveHoriz != null) {
         if (moveHoriz.hasAnimFlag(IAnimable.ANIM_13_STATE_FINISHED)) {
            //reuse and check if image is already covering some area needed. if so, add a trail if needed.
            moveHoriz.setMoveFunction(mf);
         } else {
            //augment it
            int start = base.getSIStart();
            ScrollConfig scClone = base.cloneModVisibleSet(start, moveSizeIncr);

            RgbImage ri = viewedDrawable.getViewDrawableContent(opposite, opposite);
            RgbDrawable rd = new RgbDrawable(gc, gc.getDefaultSC(), ri);
            synchronized (moveHoriz) {
               moveHoriz.setShiftDestOffset(moveSize, 0);
               moveHoriz.addTrail(rd, trail);
            }
         }
      } else {
         moveHoriz = new MoveGhost(gc, viewedDrawable, mf);
         moveHoriz.register(this);
      }
      return move;
   }

   /**
    * Returns the space currently horizontally consumed by the scrollbars <br>
    * 
    * Method is used to compute viewport <b>width</b>
    * @return
    */
   public int getSbHorizSpaceConsumed() {
      int space = 0;
      if (vScrollBar != null) {
         space += vScrollBar.getSbHorizSpaceConsumed();
      }
      if (hScrollBar != null) {
         space += hScrollBar.getSbHorizSpaceConsumed();
      }
      return space;
   }

   /**
    * Returns the space currently vertically (Y axis) consumed by the scrollbars <br>
    * 
    * Method is used to compute viewport <b>height</b>
    * @return
    */
   public int getSbVertSpaceConsumed() {
      int space = 0;
      if (vScrollBar != null) {
         space += vScrollBar.getSbVertSpaceConsumed();
      }
      if (hScrollBar != null) {
         space += hScrollBar.getSbVertSpaceConsumed();
      }
      return space;
   }

   public int getSbXShift() {
      int xs = 0;
      if (vScrollBar != null) {
         xs += vScrollBar.getXShiftLeft();
      }
      if (hScrollBar != null) {
         xs += hScrollBar.getXShiftLeft();
      }
      return xs;
   }

   public int getSbYShift() {
      int ys = 0;
      if (vScrollBar != null) {
         ys += vScrollBar.getYShiftTop();
      }
      if (hScrollBar != null) {
         ys += hScrollBar.getYShiftTop();
      }
      return ys;
   }

   public ScrollConfig getSChorizontal() {
      if (hScrollBar != null)
         return hScrollBar.getConfig();
      return null;
   }

   public ScrollConfig getSCvertical() {
      if (vScrollBar != null)
         return vScrollBar.getConfig();
      return null;
   }

   private ByteObject getStyleViewPane() {
      return styleViewPane;
   }

   private ByteObject getStyleViewPort() {
      return styleViewPort;
   }

   public ByteObject getTech() {
      return boViewPane;
   }

   private int getTopHoleH() {
      int val = 0;
      if (headerTop != null) {
         val += headerTop.getDrawnHeight();
      }
      if (headerTopClose != null) {
         val += headerTopClose.getDrawnHeight();
      }
      return val;
   }

   public ViewDrawable getViewDrawable() {
      return viewedDrawable;
   }

   /**
    * Pixel space consumed horizontally axis by {@link ViewPane} by
    * <li> {@link ViewPane}'s style decoration.
    * <li> ViewPort's style decoration. 
    * <li> {@link ScrollBar}s.
    * <li> Left and Right headers width when not in {@link ITechViewPane#PLANET_MODE_2_OVERLAY}
    * <br>
    * 
    * Method ignores Headers mode.
    *   Does not take into account eat strategy. Overlay is ignored tough.
    * 
    * @return
    */
   public int getViewPaneHorizSpaceConsumed() {
      int val = 0;
      if (isViewPaneStyleApplied()) {
         val += getStyleWConsumed();
      }
      if (isViewPortStyleApplied()) {
         val += getStyleOp().getStyleWConsumed(styleViewPort);
      }
      val = leftHeadersMod(val);
      val = rightHeadersMod(val);
      val += getSbHorizSpaceConsumed();
      return val;
   }

   public ByteObject getViewPaneStyle() {
      return style;
   }

   /**
    * Pixel space consumed on the Y axis by {@link ViewPane} decoration, headers and scrollbars
    * <li> {@link ViewPane} own style
    * <li> ViewPort's style
    * <li> Top and Bot headers
    * <li> Scrollbar consumption
    * <br>
    * <br>
    * 
    * @return
    */
   public int getViewPaneVerticalSpaceConsumed() {
      int val = 0;
      if (isViewPaneStyleApplied()) {
         val += getStyleHConsumed();
      }
      if (isViewPortStyleApplied()) {
         val += getStyleOp().getStyleHConsumed(styleViewPort);
      }
      val = topHeadersMod(val);
      val = botHeadersMod(val);
      return val + getSbVertSpaceConsumed();
   }

   /**
    * Root height minus
    * <li>Eating headers
    * <li>Eating scrollbars
    * <li>shrink values
    * <br>
    * <br>
    * When headers eat completely or more into the ViewPort dimension, return the value 0.
    * @return 0 or a positive value
    */
   public int getViewPortHeight() {
      int val = getViewPortHeightWithoutStyle();
      val = minusStyleValuesH(val, VP_FLAGX_1_STYLE_VIEWPANE, VP_OFFSET_13_STYLE_VIEWPANE_MODE1);
      val = minusStyleValuesH(val, VP_FLAGX_2_STYLE_VIEWPORT, VP_OFFSET_14_STYLE_VIEWPORT_MODE1);
      if (val < 0) {
         val = 0;
      }
      return val;
   }

   /**
    * Style is not removed from ViewPort.
    * <br>
    * Same specifications as {@link ViewPane#getViewPortWidthWithoutStyle()}
    * @return
    */
   private int getViewPortHeightWithoutStyle() {
      int val = viewPortHeight;
      if (isBotEat()) {
         val -= headerBottom.getDrawnHeight();
      }
      if (isTopHEat()) {
         val -= headerTop.getDrawnHeight();
      }
      if (isTopCloseHEat()) {
         val -= headerTopClose.getDrawnHeight();
      }
      if (isScrollBarVertEat()) {
         if (vScrollBar != null) {
            val -= vScrollBar.getSbVertSpaceConsumed();
         }
         if (hScrollBar != null) {
            val -= hScrollBar.getSbVertSpaceConsumed();
         }
      }
      val -= shrinkViewPortH;
      val -= shrinkVisualH;
      return val;
   }

   /**
    * Returns the pixels used for the width of the ViewPort.
    * <br>
    * 
    * By default, the same size as Drawable width.
    * <br>
    * 
    * @return
    */
   public int getViewPortWidth() {
      int val = getViewPortWidthWithoutStyle();
      val = minusStyleValuesW(val, VP_FLAGX_1_STYLE_VIEWPANE, VP_OFFSET_13_STYLE_VIEWPANE_MODE1);
      val = minusStyleValuesW(val, VP_FLAGX_2_STYLE_VIEWPORT, VP_OFFSET_14_STYLE_VIEWPORT_MODE1);
      if (val < 0) {
         val = 0;
      }
      return val;
   }

   /**
    * Style is not removed from ViewPort's computation
    * <br>
    * Computes it from the root value minus
    * <li> Shrink
    * <li> Eating scrollbars and headers {@link ITechViewPane#PLANET_MODE_0_EAT}
    * <br>
    * <br>
    * {@link ViewPane#getViewPortWidth()}
    * @return
    */
   private int getViewPortWidthWithoutStyle() {
      int val = viewPortWidth;
      if (isLeftHEat()) {
         val -= headerLeft.getDrawnWidth();
      }
      if (isRightHEat()) {
         val -= headerRight.getDrawnWidth();
      }
      if (isScrollBarHorizEat()) {
         //viewport width is dimished by the scrollbars unless expand or overlay
         if (vScrollBar != null) {
            val -= vScrollBar.getSbHorizSpaceConsumed();
         }
         if (hScrollBar != null) {
            val -= hScrollBar.getSbHorizSpaceConsumed();
         }
      }
      val -= shrinkViewPortW;
      val -= shrinkVisualW;
      return val;
   }

   /**
    * Returns the ViewPort x absolute coordinate.
    * <br>
    * Its the {@link ViewDrawable#getX()} and {@link ViewPane#getViewPortXOffset()}
    * @return the x coordinate where to start drawing the viewpane's view
    */
   public int getViewPortXAbs() {
      return viewedDrawable.getX() + getViewPortXOffset();
   }

   /**
    * Compute the X coordinate offset of the ViewPort from {@link Drawable#getX()}.
    * <br>
    * <br>
    * That is 
    * <li>HeaderLeft, 
    * <li>H & V scrollbar, 
    * <li>ViewPane style
    * <li>ViewPort style
    * <br>
    * <br>
    * @return
    */
   public int getViewPortXOffset() {
      int viewPortXOffset = 0;
      viewPortXOffset = leftHeadersMod(viewPortXOffset);
      if (boViewPane.get2Bits1(VP_OFFSET_05_SCROLLBAR_MODE1) != PLANET_MODE_2_OVERLAY) {
         viewPortXOffset += getSbXShift();
      }
      if (isViewPaneStyleApplied()) {
         viewPortXOffset += getStyleWLeftConsumed();
      }
      if (isViewPortStyleApplied()) {
         viewPortXOffset += getStyleOp().getStyleLeftWConsumed(getStyleViewPort());
      }
      return viewPortXOffset;
   }

   /**
    * Its the {@link ViewDrawable#getY()} and {@link ViewPane#getViewPortYOffset()}
    * @return the y coordinate where to start drawing the viewpane's view
    */
   public int getViewPortYAbs() {
      return viewedDrawable.getY() + getViewPortYOffset();
   }

   /**
    * Deportation of the ViewPort from original y coordinate.
    * <br>
    * The number of top height pixels consumed on content area by <br>
    * <li>ViewPane top styling
    * <li>Top Header
    * <li>Top horizontal Scrollbar
    * @return
    */
   public int getViewPortYOffset() {
      int viewPortYOffset = 0;
      viewPortYOffset = topHeadersMod(viewPortYOffset);
      if (boViewPane.get2Bits2(VP_OFFSET_05_SCROLLBAR_MODE1) != PLANET_MODE_2_OVERLAY) {
         viewPortYOffset += getSbYShift();
      }
      if (isViewPaneStyleApplied()) {
         viewPortYOffset += getStyleHTopConsumed();
      }
      if (isViewPortStyleApplied()) {
         viewPortYOffset += getStyleOp().getStyleTopHConsumed(getStyleViewPort());
      }
      return viewPortYOffset;
   }

   public ScrollBar getVScrollBar() {
      return vScrollBar;
   }

   private boolean hasAtLeastOneSizedDrawable() {
      return boViewPane.get1(VP_OFFSET_12_INTERNAL_SIZING1) != 0;
   }

   public boolean hasTech(int flag) {
      return boViewPane.hasFlag(VP_OFFSET_01_FLAG, flag);
   }

   public boolean hasTechX(int flag) {
      return boViewPane.hasFlag(VP_OFFSET_02_FLAGX, flag);
   }

   public boolean hasTechY(int flag) {
      return boViewPane.hasFlag(VP_OFFSET_03_FLAGY, flag);
   }

   /**
    * Content style
    * {@link IBOViewPane#VP_FLAGX_3_STYLE_CONTENT}
    * @return
    */
   public boolean hasViewDrwStyle() {
      return hasTechX(VP_FLAGX_3_STYLE_CONTENT);
   }

   public boolean hasVisualPartialH() {
      return boViewPane.get4Bits2(VP_OFFSET_06_VISUAL_LEFT_OVER1) == VISUAL_1_PARTIAL;
   }

   public boolean hasVisualPartialW() {
      return boViewPane.get4Bits1(VP_OFFSET_06_VISUAL_LEFT_OVER1) == VISUAL_1_PARTIAL;
   }

   /**
    * ViewPane does not follow Drawable x,y,w,h
    * <li>{@link ViewPane#getViewPortXAbs}
    * <li>{@link ViewPane#getViewPortYAbs()}
    */
   public void init(int w, int hf) {
      //throw new RuntimeException();
   }

   /**
    * Immateriality is not around a specific axis.
    * <br>
    * Wrapper mode horizontal bar consumes pixels horizontally
    * Block mode horizontal bar consumes pixels vertically
    * <br>
    * What is immateraility then?
    * <br>
    * Use the full Invisible Scrollbar flag
    */
   private void initFullHBar() {
      initHBar();
      if (boViewPane.get2Bits1(VP_OFFSET_05_SCROLLBAR_MODE1) == PLANET_MODE_3_IMMATERIAL) {
         hScrollBar.setBehaviorFlag(ITechDrawable.BEHAVIOR_16_IMMATERIAL, true);
      }
      if (boViewPane.hasFlag(VP_OFFSET_03_FLAGY, VP_FLAGY_5_SB_INVISIBLE_HORIZ)) {
         hScrollBar.setBehaviorFlag(ITechDrawable.BEHAVIOR_16_IMMATERIAL, true);
      }
      if (isScrollBarHorizExpand()) {
         scrollBarHPaneW += hScrollBar.getSbHorizSpaceConsumed();
      }
      if (isScrollBarVertExpand()) {
         scrollBarHPaneH += hScrollBar.getSbVertSpaceConsumed();
      }
      hScrollBar.init(scrollBarHPaneW, scrollBarHPaneH);
      if (isScrollBarHorizEat()) {
         //eat into
         scrollBarVPaneW -= hScrollBar.getSbHorizSpaceConsumed();
      }
      if (isScrollBarVertEat()) {
         scrollBarVPaneH -= hScrollBar.getSbVertSpaceConsumed();
      }
   }

   /**
    * Reads Scrollbar style and modifies the scrollbar values
    * 
    */
   private void initFullVBar() {
      initVBar();
      if (boViewPane.get2Bits2(VP_OFFSET_05_SCROLLBAR_MODE1) == PLANET_MODE_3_IMMATERIAL) {
         vScrollBar.setBehaviorFlag(ITechDrawable.BEHAVIOR_16_IMMATERIAL, true);
      }
      if (boViewPane.hasFlag(VP_OFFSET_03_FLAGY, VP_FLAGY_6_SB_INVISIBLE_VERT)) {
         vScrollBar.setBehaviorFlag(ITechDrawable.BEHAVIOR_16_IMMATERIAL, true);
      }
      //over which area is the vertical scrollbar be dimensioned?
      //over the viewport do a first init
      //for expand policy, we must increase the height 
      if (isScrollBarHorizExpand()) {
         scrollBarVPaneW += vScrollBar.getSbHorizSpaceConsumed();
      }
      if (isScrollBarVertExpand()) {
         scrollBarVPaneH += vScrollBar.getSbVertSpaceConsumed();
      }
      vScrollBar.init(scrollBarVPaneW, scrollBarVPaneH);
      if (isScrollBarHorizEat()) {
         scrollBarHPaneW -= vScrollBar.getSbHorizSpaceConsumed();
      }
      if (isScrollBarVertEat()) {
         scrollBarHPaneH -= vScrollBar.getSbVertSpaceConsumed();
      }
   }

   private void initHBar() {
      if (hScrollBar == null) {
         //from styleKey of ViewPane
         StyleClass sc = styleClass.getSCNotNull(IBOTypesGui.LINK_71_STYLE_VIEWPANE_H_SCROLLBAR);
         //TODO implement figure sharing..that is one figure drawn using flipping for scrollbars
         //how would do that? scrollbars have the same style, same sizing
         //wrappers are just drawable with style layers
         //you need a cache for each style state
         // GraphicsXD has a cache engine that can be explicitely asked
         // a flag in scrollbar style can give a clue if wrappers are likely to be
         // cachable. When cacheable, when asked to be drawn, Drawable ask GraphicXD engine for a match
         // match parameters are width/height,styleType,flipType. The style behave.
         // so Bgstyle can be drawn using that cache. Ask StyleEngine? Probably
         hScrollBar = new ScrollBar(gc, sc, true, this);
         //override around the clock behavior
         hScrollBar.getLayEngine().setManualOverride(true);
         hScrollBar.techUpdateViewPane(boViewPane);

      }
   }

   private void initMoveGhost() {
      if (move == null) {
         move = new MoveGhost(gc, viewedDrawable, null);
         move.register(this);
         move.setRepeat(0);
         move.setSleep(200);
      }
   }

   /**
    * Update scrolling configuration of existing horiztonal or vertical {@link ScrollBar}
    * For it to be valid, size must have been computed
    * <br>
    * <br>
    * Called by {@link ViewPane#init(int, int)}
    */
   public void initScrollingConfigs() {
      ScrollConfig scx = null;
      ScrollConfig scy = null;
      if (vScrollBar != null) {
         scy = vScrollBar.getConfig();
      }
      if (hScrollBar != null) {
         scx = hScrollBar.getConfig();
      }
      //#debug
      toDLog().pFlow("ScrollConfig X", scx, ViewPane.class, "initScrollingConfigs", LVL_05_FINE, false);
      //#debug
      toDLog().pFlow("ScrollConfig Y", scy, ViewPane.class, "initScrollingConfigs", LVL_05_FINE, false);
      viewedDrawable.initScrollingConfig(scx, scy);
      
      doUpdateScrollbarStructures();
   }

   /**
    * Sized Drawable may increase the ViewPort and the whole Drawable.
    * <br>
    * Called at first to increase the initial pixel size.
    * 
    * Increase is capped to {@link ViewDrawable} maximum size.
    * 
    * @param width
    * @param height
    */
   private void initSizedDrawables(int width, int height) {
      if (hasAtLeastOneSizedDrawable()) {
         //compute max width of ViewDrawable
         ViewDrawable vd = getViewDrawable();
         LayEngineDrawable layEngineViewDrawable = vd.getLayEngine();
         
         int maxWidth = layEngineViewDrawable.getSizeMaxW();
         //at least one Header has a size. update viewPortWidth
         maxViewPortW(headerTop, maxWidth, VP_SIZER_1_TOP);
         maxViewPortW(headerBottom, maxWidth, VP_SIZER_2_BOT);
         maxViewPortW(headerTopClose, maxWidth, VP_SIZER_5_TOP_CLOSE);
         maxViewPortW(headerBottomClose, maxWidth, VP_SIZER_6_BOT_CLOSE);

         int maxHeight = layEngineViewDrawable.getSizeMaxH();

         maxViewPortH(headerLeft, maxHeight, VP_SIZER_3_LEFT);
         maxViewPortH(headerRight, maxHeight, VP_SIZER_4_RIGHT);
         maxViewPortH(headerLeftClose, maxHeight, VP_SIZER_7_LEFT_CLOSE);
         maxViewPortH(headerRightClose, maxHeight, VP_SIZER_8_RIGHT_CLOSE);

      }
   }

   /**
    * TODO {@link MyDrawables}
    */
   public void initStyleSlavery() {
      if (headerTop != null) {
         if (headerTopClose != null) {
            if (headerTop.getStyle() == headerTopClose.getStyle()) {
               MyDrawables md = new MyDrawables(gc, headerTop.getStyleClass());
               //create a duo that will draw link headerClose style to top
               md.addDrawable(headerTop);
               md.addDrawable(headerTopClose);

               //draw bg for both.
               //disable headerTopClose regular draw.

            }
         }
      }
   }

   /**
    * Create Vertical Scrollbar.
    * Using the Style ID of content, fetch the Scrollbar style and create a new scrollbar
    */
   private void initVBar() {
      if (vScrollBar == null) {
         //get scrollbar parameters style from the view pane style
         StyleClass sc = styleClass.getStyleClass(IBOTypesGui.LINK_72_STYLE_VIEWPANE_V_SCROLLBAR);
         vScrollBar = new ScrollBar(gc, sc, false, this);
         vScrollBar.getLayEngine().setManualOverride(true); //everything is decided
         vScrollBar.techUpdateViewPane(boViewPane);
      }
   }

   /**
    * Initialize the ViewPane with the {@link ViewDrawable} dimension  <code>width</code> and <code>height</code>. 
    * <br>
    * <br>
    * This original dimension is the given by the {@link ViewDrawable} first size computation.
    * <br>
    * This original dimension can be modified by the {@link ViewPane} under one or more of the following
    * <li> A Header has a sizer with {@link ISizer#ET_TYPE_0_PREFERRED_SIZE} and was added with
    * {@link ViewPane#setHeaderSized(IDrawable, int, int)}
    * <li> A Header was added with {@link ITechViewPane#PLANET_MODE_1_EXPAND} mode
    * <li> Shrink {@link ITechViewPane#VISUAL_2_SHRINK} will decrease original dimension
    * <li> 
    * <br>
    * This dimension will not change unless Drawable is malleable.
    * <br>
    * A {@link Drawable} set with a {@link ViewContext} size, is not malleable anymore.
    * <br>
    * 
    * The method calls {@link ViewDrawable#initViewPort(int, int)} with ViewPort's width and height which
    * will force the {@link ViewDrawable} to adjust its preferred size.
    * If a change was made to preferred size, return true.
    * <br>
    * <br>
    * A {@link ViewDrawable#isMalleable()} with a {@link ViewPane} is initialized twice by {@link ViewDrawable#init(int, int)} method.
    * <br>
    * <br>
    * <b>Implementation Note</b>: 
    * <li>If Headers are master first layout headers.<br>
    * <li>Check Drawable Init<br>
    * <li>Layout Scrollbars<br>
    * <li>Check Drawable Init<br>
    * <li>Layout All<br>
    * <li>The ViewPane must honour the Axis by expanding.
    * <li>Unless Expansion flags are explicit, Expansion is limited by shrank size.
    * <br>
    * <br> 
    * Complete override of root {@link Drawable#init(int, int)} method.
    * <br>
    * <br>
    * @param width the drawable width of the ViewDrawable as computed by computeDrawableSize
    * @param height the drawable height of the ViewDrawable as computed by computeDrawableSize
    */
   protected void initViewPane(int width, int height) {
      //TODO update viewpane sizers that will be used to compute the style
      //LayouterCtx lac = gc.getLAC();
      //ByteObject sizerW = lac.getSizerFactory().getSizerFromOldValue(width);
      //ByteObject sizerH = lac.getSizerFactory().getSizerFromOldValue(height);

      //layEngine = new LayEngineDrawable(gc, this);

      initStyleSlavery();

      originalPortWidth = width;
      originalPortHeight = height;
      //viewpane is fully constrained by default but follow its own dimension rules for expansion
      //so by default viewport dimension is the same but remove viewpane style
      viewPortWidth = width;
      viewPortHeight = height;

      //case.. top sized extra.. but then we have a header Left in Expand Mode....
      //Viewpane tracks the expansion pixels
      initSizedDrawables(viewPortWidth, viewPortHeight);

      //reset nav flags because they will be set to true when scrollbars are validated
      setBehaviorFlag(ITechDrawable.BEHAVIOR_26_NAV_VERTICAL, false);
      setBehaviorFlag(ITechDrawable.BEHAVIOR_27_NAV_HORIZONTAL, false);

      //      if (hasStyleViewPane()) {
      //         viewPortWidth -= MStyle.getStyleWConsumed(style);
      //         viewPortHeight -= MStyle.getStyleHConsumed(style);
      //      }

      if (!hasState(ITechDrawable.STATE_06_STYLED)) {
         styleValidate();
      }
      setStateFlag(ITechDrawable.STATE_05_LAYOUTED, true);

      initViewPaneSattelites();

      mapViewDrawableToViewPane();

      //do the viewport of the ViewDrawable and update ViewPane if changes in preferred size.
      initViewPort();

      //once we have a final results.. see if we can shrink anything. which do the process again
      boolean hasShrink = initViewPaneShrink();
      if (hasShrink) {
         //the following code adjust drawable width if the ViewPane shrank the ViewPort areas.
         if (shrinkViewPortW != 0) {
            setDw(getDw() - shrinkViewPortW);
            setDw(getDw() - shrinkVisualW);
         }
         if (shrinkViewPortH != 0) {
            setDh(getDh() - shrinkViewPortH);
            setDh(getDh() - shrinkVisualH);
         }
         setDh(getDh() + getExpansionHPixels());
         setDw(getDw() + getExpansionWPixels());

         //a shrink influence every sattelites but won't change scrollbar existence
         //initViewPaneSattelites();
      }

      //update the viewedDrawable visible drawable dimension. this does not generate a style compute update
      //a)because expanding headers and scrollabr will increase the drawable dimension
      //b)because viewport might have been shrunk.
      viewedDrawable.setDrawableSize(getDw(), getDh(), true);

      //after the up
      if (vScrollBar != null) {
         viewedDrawable.setFlagView(FLAG_VSTATE_24_SCROLLED_V, true);
         viewedDrawable.setFlagView(FLAG_VSTATE_22_SCROLLED, true);
         setBehaviorFlag(ITechDrawable.BEHAVIOR_26_NAV_VERTICAL, true);
      }
      if (hScrollBar != null) {
         viewedDrawable.setFlagView(FLAG_VSTATE_23_SCROLLED_H, true);
         viewedDrawable.setFlagView(FLAG_VSTATE_22_SCROLLED, true);
         setBehaviorFlag(ITechDrawable.BEHAVIOR_27_NAV_HORIZONTAL, true);
      }

      gc.getNavigator().setNavCtx(viewedDrawable, viewedDrawable.hasFlagView(FLAG_VSTATE_22_SCROLLED));
   }

   /**
    * 
    */
   private void initViewPaneSattelites() {
      layDimensionArtifacts();
      //once dimensions are known shrink any
      initScrollingConfigs();

      //position scrollbars and headers
      if (hasTech(VP_FLAG_3_SCROLLBAR_MASTER)) {
         layPositionHeaders();
         layPositionScrollBars();
      } else {
         layPositionScrollBars();
         layPositionHeaders();
      }

      layDimensionHeaderHoles();
      layDimensionSBHoles();

      initScrollingConfigs();
   }

   /**
    * shrinking is done once the final is done.
    * <br>
    * <br>
    * if sizers allow it: i.e sattelites sizers are not in preferred mode
    * <br>
    * or preferred size allows shrinking. only then shrink
    */
   private boolean initViewPaneShrink() {
      //
      boolean shrink = initVisualShrink();
      //after sizing 
      boolean shrinkB = applyBehaviorShrinking();

      return shrink || shrinkB;
   }

   private void initViewPort() {
      viewPortStyleCache = new StyleCache(gc.getDC(), viewedDrawable, styleViewPort);

      LayEngineDrawable layEngineDrawable = viewedDrawable.getLayEngine();
      //keep a copy to see if ViewPort changes modified the preferred size
      int lastPw = layEngineDrawable.getPw();
      int lastPh = layEngineDrawable.getPh();
      int lastViewPortW = getViewPortWidth();
      int lastViewPortH = getViewPortHeight();

      //give a chance to drawable to update its preferred size due to a new size in viewport
      viewedDrawable.initViewPort(lastViewPortW, lastViewPortH);

      //
      if (viewedDrawable.isMalleable()) {
         //only do that if malleable. otherwise preferred size does not change relative to 
         if (lastPw != layEngineDrawable.getPw() || lastPh != layEngineDrawable.getPh()) {
            //preferred size was changed therefore we need to update the ViewPane
            //changes in preferred sizes can create a new scrollbar which modifies the configuration
            //the change in preferred size influence the scrollbars which in turn may change headers.
            initViewPaneSattelites();

            //after
            int nextViewPortW = getViewPortWidth();
            int nextViewPortH = getViewPortHeight();
            //a new scrollbar diminished the ViewPort
            if (lastViewPortW != nextViewPortW || lastViewPortH != nextViewPortH) {
               //viewport dimension changed, probably because of a new scrollbar.
               //we have to update the ViewPort again.
               lastPw = layEngineDrawable.getPw();
               lastPh = layEngineDrawable.getPh();
               lastViewPortW = nextViewPortW;
               lastViewPortH = nextViewPortH;

               //we have to init again with a new viewport
               viewedDrawable.initViewPort(nextViewPortW, nextViewPortH);

               //check if the ViewPort changed the preferred size

               if (lastPw != layEngineDrawable.getPw() || lastPh != layEngineDrawable.getPh()) {
                  //preferred size were changed again we need to update the ViewPane
                  initViewPaneSattelites();

                  //do it again in the rare cases when a second scrollbar appears because of the resizing
                  nextViewPortW = getViewPortWidth();
                  nextViewPortH = getViewPortHeight();

                  //a new scrollbar diminished the ViewPort
                  if (lastViewPortW != nextViewPortW || lastViewPortH != nextViewPortH) {
                     //we have to init again with a new viewport
                     viewedDrawable.initViewPort(nextViewPortW, nextViewPortH);

                  }
               }
            }
         }
      }
   }

   /**
    * Compute visualShrink values. 
    * <br>
    * <li> {@link ViewPane#shrinkVisualH}
    * <li> {@link ViewPane#shrinkVisualW}
    * Must be called after the scrolling configuration have been set.
    * <br>
    * <br>
    */
   private boolean initVisualShrink() {
      boolean isVisualShrink = false;
      //only shrink when visual bar
      if (vScrollBar != null && isVisualShrinkH()) {
         //ask ViewDrawable pixels needed for the current
         int ch = viewedDrawable.getViewPortContentH();
         int pn = vScrollBar.getConfig().getPixelSizeFullVisible();
         shrinkVisualH = ch - pn;
         isVisualShrink = true;
      } else {
         shrinkVisualH = 0;
      }
      //only shrink when visual bar
      if (hScrollBar != null && isVisualShrinkW()) {
         //ask ViewDrawable pixels needed for the current
         int cw = viewedDrawable.getViewPortContentW();
         int pw = hScrollBar.getConfig().getPixelSizeFullVisible();
         shrinkVisualW = cw - pw;
         isVisualShrink = true;
      } else {
         shrinkVisualW = 0;
      }
      return isVisualShrink;
   }

   private boolean isBotEat() {
      return headerBottom != null && (boViewPane.get2Bits2(VP_OFFSET_04_HEADER_PLANET_MODE1) == PLANET_MODE_0_EAT && !viewedDrawable.hasFlagView(FLAG_VSTATE_07_NO_EAT_H_MUST_EXPAND));
   }

   /**
    * True even when PLANET_STRUCT_0EAT but {@link ViewDrawable} has state {@link ITechViewDrawable#FLAG_VSTATE_07_NO_EAT_H_MUST_EXPAND}.
    * @return
    */
   private boolean isBotHExpand() {
      int val = boViewPane.get2Bits2(VP_OFFSET_04_HEADER_PLANET_MODE1);
      return headerBottom != null && (val == PLANET_MODE_1_EXPAND || (val == PLANET_MODE_0_EAT && viewedDrawable.hasFlagView(FLAG_VSTATE_07_NO_EAT_H_MUST_EXPAND)));
   }

   private boolean isBotHNotOverlay() {
      return headerBottom != null && boViewPane.get2Bits2(VP_OFFSET_04_HEADER_PLANET_MODE1) != PLANET_MODE_2_OVERLAY;
   }

   private boolean isBotHOverlay() {
      return headerBottom != null && boViewPane.get2Bits2(VP_OFFSET_04_HEADER_PLANET_MODE1) == PLANET_MODE_2_OVERLAY;
   }

   /**
    * Should {@link ITechViewDrawable#FLAG_VSTATE_01_CLIP} be set to true.
    * <br>
    * <br>
    * @return
    */
   public boolean isContentClipped() {
      return hasVisualPartialH() || hasVisualPartialW();
   }

   private boolean isHeaderMaster() {
      return !hasTech(VP_FLAG_3_SCROLLBAR_MASTER);
   }

   public boolean isInsideViewPort(InputConfig ic) {
      return DrawableUtilz.isInside(ic, getViewPortXAbs(), getViewPortYAbs(), getViewPortWidth(), getViewPortHeight());
   }

   public boolean isInsideViewPort(int x, int y) {
      return DrawableUtilz.isInside(x, y, getViewPortXAbs(), getViewPortYAbs(), getViewPortWidth(), getViewPortHeight());
   }

   private boolean isLeftHEat() {
      return headerLeft != null && boViewPane.get2Bits3(VP_OFFSET_04_HEADER_PLANET_MODE1) == PLANET_MODE_0_EAT && !viewedDrawable.hasFlagView(FLAG_VSTATE_06_NO_EAT_W_MUST_EXPAND);
   }

   private boolean isLeftHExpand() {
      int val = boViewPane.get2Bits3(VP_OFFSET_04_HEADER_PLANET_MODE1);
      return headerLeft != null && (val == PLANET_MODE_1_EXPAND || (val == PLANET_MODE_0_EAT && viewedDrawable.hasFlagView(FLAG_VSTATE_06_NO_EAT_W_MUST_EXPAND)));
   }

   private boolean isLeftHNotOverlay() {
      return headerLeft != null && boViewPane.get2Bits3(VP_OFFSET_04_HEADER_PLANET_MODE1) != PLANET_MODE_2_OVERLAY;
   }

   private boolean isLeftHOverlay() {
      return headerLeft != null && boViewPane.get2Bits3(VP_OFFSET_04_HEADER_PLANET_MODE1) == PLANET_MODE_2_OVERLAY;
   }

   /**
    * A {@link ViewPane} is opaque when .
    * <br>
    * For performance purpose, it is good thing to use a bg layer opaque
    */
   public boolean isOpaque() {
      if (hasState(ITechDrawable.STATE_18_OPAQUE_COMPUTED)) {
         return hasState(ITechDrawable.STATE_17_OPAQUE);
      } else {
         boolean isOpaque = false;
         if (isViewPaneStyleApplied()) {
            isOpaque = super.isOpaque();
         } else {
            //no viewpane style.
            //TODO check if all planetaries are opaque
            if (vScrollBar != null) {
               isOpaque = isOpaque && vScrollBar.isOpaque();
            }
            if (hScrollBar != null) {
               isOpaque = isOpaque && hScrollBar.isOpaque();
            }
         }

         //check if content and style is opaque as well
         setStateFlag(ITechDrawable.STATE_17_OPAQUE, isOpaque);
         setStateFlag(ITechDrawable.STATE_18_OPAQUE_COMPUTED, true);
         return isOpaque;
      }
   }

   /**
    * 
    * @return
    */
   private boolean isRightHEat() {
      return headerRight != null && boViewPane.get2Bits4(VP_OFFSET_04_HEADER_PLANET_MODE1) == PLANET_MODE_0_EAT && !viewedDrawable.hasFlagView(FLAG_VSTATE_06_NO_EAT_W_MUST_EXPAND);
   }

   /**
    * 
    * @return
    */
   private boolean isRightHExpand() {
      int val = boViewPane.get2Bits4(VP_OFFSET_04_HEADER_PLANET_MODE1);
      return headerRight != null && (val == PLANET_MODE_1_EXPAND || (val == PLANET_MODE_0_EAT && viewedDrawable.hasFlagView(FLAG_VSTATE_06_NO_EAT_W_MUST_EXPAND)));
   }

   private boolean isRightHNotOverlay() {
      return headerRight != null && boViewPane.get2Bits4(VP_OFFSET_04_HEADER_PLANET_MODE1) != PLANET_MODE_2_OVERLAY;
   }

   private boolean isRightHOverlay() {
      return headerRight != null && boViewPane.get2Bits4(VP_OFFSET_04_HEADER_PLANET_MODE1) == PLANET_MODE_2_OVERLAY;
   }

   private boolean isScrollBarHorizEat() {
      return boViewPane.get2Bits1(VP_OFFSET_05_SCROLLBAR_MODE1) == PLANET_MODE_0_EAT && !viewedDrawable.hasFlagView(FLAG_VSTATE_06_NO_EAT_W_MUST_EXPAND);
   }

   /**
    * 
    * @return
    */
   private boolean isScrollBarHorizExpand() {
      int val = boViewPane.get2Bits1(VP_OFFSET_05_SCROLLBAR_MODE1);
      return (val == PLANET_MODE_1_EXPAND || (val == PLANET_MODE_0_EAT && viewedDrawable.hasFlagView(FLAG_VSTATE_06_NO_EAT_W_MUST_EXPAND)));
   }

   private boolean isScrollBarHorizImmaterial() {
      return boViewPane.get2Bits1(VP_OFFSET_05_SCROLLBAR_MODE1) == PLANET_MODE_3_IMMATERIAL;
   }

   private boolean isScrollBarHorizNotOverlay() {
      return boViewPane.get2Bits1(VP_OFFSET_05_SCROLLBAR_MODE1) != PLANET_MODE_2_OVERLAY;
   }

   private boolean isScrollBarHorizOverlay() {
      return boViewPane.get2Bits1(VP_OFFSET_05_SCROLLBAR_MODE1) == PLANET_MODE_2_OVERLAY;
   }

   private boolean isScrollBarVertEat() {
      return boViewPane.get2Bits2(VP_OFFSET_05_SCROLLBAR_MODE1) == PLANET_MODE_0_EAT && !viewedDrawable.hasFlagView(FLAG_VSTATE_07_NO_EAT_H_MUST_EXPAND);

   }

   /**
    * 
    * @return
    */
   private boolean isScrollBarVertExpand() {
      int val = boViewPane.get2Bits2(VP_OFFSET_05_SCROLLBAR_MODE1);
      return (val == PLANET_MODE_1_EXPAND || (val == PLANET_MODE_0_EAT && viewedDrawable.hasFlagView(FLAG_VSTATE_07_NO_EAT_H_MUST_EXPAND)));
   }

   private boolean isScrollBarVertImmaterial() {
      int val = boViewPane.get2Bits2(VP_OFFSET_05_SCROLLBAR_MODE1);
      return (val == PLANET_MODE_3_IMMATERIAL);
   }

   /**
    * Check if scrollbar does consumed pixel
    * @return
    */
   private boolean isScrollBarVertNotOverlay() {
      return boViewPane.get2Bits2(VP_OFFSET_05_SCROLLBAR_MODE1) != PLANET_MODE_2_OVERLAY;
   }

   private boolean isScrollBarVertOverlay() {
      return boViewPane.get2Bits2(VP_OFFSET_05_SCROLLBAR_MODE1) == PLANET_MODE_2_OVERLAY;
   }

   /**
    * ViewPort's width is smaller than the preferred height
    * @return
    */
   private boolean isScrollHNeeded() {
      if (viewedDrawable.hasFlagView(FLAG_VSTATE_10_CONTENT_PW_VIEWPORT_DW)) {
         return false;
      }
      //System.out.println("#ViewPane#isScrollHNeeded viewportWidth="+ getViewPortWidth() + " < "+ viewedDrawable.getPreferredContentWidth());
      return getViewPortWidth() < viewedDrawable.getPreferredContentWidth();
   }

   public boolean isScrollHPixels() {
      return boViewPane.get2Bits3(VP_OFFSET_05_SCROLLBAR_MODE1) == SCROLL_TYPE_0_PIXEL_UNIT;
   }

   /**
    * ViewPort's height is smaller than the preferred height of content!
    * @return
    */
   private boolean isScrollVNeeded() {
      if (viewedDrawable.hasFlagView(FLAG_VSTATE_11_CONTENT_PH_VIEWPORT_DH)) {
         return false;
      }
      //System.out.println("#ViewPane#isScrollNeededV viewportHeigh="+ getViewPortHeight() + " < "+ viewedDrawable.getPreferredContentHeight());
      return getViewPortHeight() < viewedDrawable.getPreferredContentHeight();
   }

   public boolean isScrollVPixels() {
      return boViewPane.get2Bits4(VP_OFFSET_05_SCROLLBAR_MODE1) == SCROLL_TYPE_0_PIXEL_UNIT;
   }

   private boolean isTopCloseHEat() {
      return headerTopClose != null && boViewPane.get2Bits1(VP_OFFSET_04_HEADER_PLANET_MODE1) == PLANET_MODE_0_EAT;
   }

   private boolean isTopCloseHExpand() {
      int val = boViewPane.get2Bits1(VP_OFFSET_04_HEADER_PLANET_MODE1);
      return headerTopClose != null && (val == PLANET_MODE_1_EXPAND || (val == PLANET_MODE_0_EAT && viewedDrawable.hasFlagView(FLAG_VSTATE_07_NO_EAT_H_MUST_EXPAND)));
   }

   private boolean isTopCloseHOverlay() {
      return headerTopClose != null && boViewPane.get2Bits1(VP_OFFSET_04_HEADER_PLANET_MODE1) == PLANET_MODE_2_OVERLAY;
   }

   private boolean isTopHEat() {
      return headerTop != null && (boViewPane.get2Bits1(VP_OFFSET_04_HEADER_PLANET_MODE1) == PLANET_MODE_0_EAT && !viewedDrawable.hasFlagView(FLAG_VSTATE_07_NO_EAT_H_MUST_EXPAND));
   }

   /**
    * 
    * @return
    */
   private boolean isTopHExpand() {
      int val = boViewPane.get2Bits1(VP_OFFSET_04_HEADER_PLANET_MODE1);
      return headerTop != null && (val == PLANET_MODE_1_EXPAND || (val == PLANET_MODE_0_EAT && viewedDrawable.hasFlagView(FLAG_VSTATE_07_NO_EAT_H_MUST_EXPAND)));
   }

   private boolean isTopHNotOverlay() {
      return headerTop != null && boViewPane.get2Bits1(VP_OFFSET_04_HEADER_PLANET_MODE1) != PLANET_MODE_2_OVERLAY;
   }

   private boolean isTopHOverlay() {
      return headerTop != null && boViewPane.get2Bits1(VP_OFFSET_04_HEADER_PLANET_MODE1) == PLANET_MODE_2_OVERLAY;
   }

   /**
    * Returns true when a style is drawn around the ViewPane area.
    * <br>
    * That style is defined by {@link IBOViewPane#VP_OFFSET_13_STYLE_VIEWPANE_MODE1}
    * <br>
    * To get the style, use method {@link ViewPane#getViewPaneStyle()}
    * @return
    */
   public boolean isViewPaneStyleApplied() {
      return hasTechX(VP_FLAGX_1_STYLE_VIEWPANE);
   }

   public boolean isViewPortStyleApplied() {
      return hasTechX(VP_FLAGX_2_STYLE_VIEWPORT);
   }

   private boolean isVisualShrinkH() {
      return boViewPane.get4Bits2(VP_OFFSET_06_VISUAL_LEFT_OVER1) == VISUAL_2_SHRINK;
   }

   private boolean isVisualShrinkW() {
      return boViewPane.get4Bits1(VP_OFFSET_06_VISUAL_LEFT_OVER1) == VISUAL_2_SHRINK;
   }

   /**
    * Computes the dimension of headers and scrollbars.
    * <br>
    * This method does not change {@link ViewPane} state, except
    * <li> setting {@link ITechViewDrawable#FLAG_VSTATE_03_VIEWPANE_OVERLAY} to {@link ViewDrawable}
    * <li> settings scrollbar to null or creating them
    * <li> {@link ViewPane#scrollBarHPaneH}
    * <li> {@link ViewPane#scrollBarHPaneW}
    * <li> {@link ViewPane#scrollBarVPaneH}
    * <li> {@link ViewPane#scrollBarVPaneW}
    * 
    */
   private void layDimensionArtifacts() {
      if (hasTech(VP_FLAG_3_SCROLLBAR_MASTER)) {
         layDimensionHeaders();
         layDimensionScrollbars();
         //boolean hs = hScrollBar != null && tech.get2Bits1(TECH_VP_OFFSET_4SCROLLBAR_MODE1) == PLANET_STRUCT_1EXPAND;
         //boolean vs = vScrollBar != null && tech.get2Bits2(TECH_VP_OFFSET_4SCROLLBAR_MODE1) == PLANET_STRUCT_1EXPAND;
         boolean hs = hScrollBar != null;
         boolean vs = vScrollBar != null;
         if (hs || vs) {
            //lay again because expansion scrollbars increase the headers dimension
            layDimensionHeaders();
         }
      } else {
         layDimensionHeaders();
         boolean b = layDimensionScrollbars();
         //lay again because expansion headers increase the ViewPort area.
         if (b) {
            layDimensionHeaders();
         }
      }
      setupOverlayState();
   }

   private void layDimensionHeaderHoles() {
      if (getCompetTypeHeader() != COMPET_HEADER_0_NEUTRAL) {
         headerHoles = null;
         return;
      }
      //create holes
      if (headerHoles == null)
         headerHoles = new Drawable[4];
      if (headerTop != null || headerTopClose != null) {
         int holeH = getTopHoleH();
         int holeY = 0;
         if (headerTop == null) {
            holeY = headerTopClose.getY();
         } else {
            holeY = headerTop.getY();
         }
         if (headerLeft != null) {
            headerHoles[HOLE_0_TOP_LEFT] = new Drawable(gc, styleClass.getStyleClass(IBOTypesGui.LINK_67_STYLE_VIEWPANE_HOLE));
            IDrawable h = headerHoles[HOLE_0_TOP_LEFT];
            h.init(headerLeft.getDrawnWidth(), holeH);
            h.setXY(headerLeft.getX(), holeY);
         }
         if (headerRight != null) {
            headerHoles[HOLE_1_TOP_RIGHT] = new Drawable(gc, styleClass.getStyleClass(IBOTypesGui.LINK_67_STYLE_VIEWPANE_HOLE));
            IDrawable h = headerHoles[HOLE_1_TOP_RIGHT];
            h.init(headerRight.getDrawnWidth(), holeH);
            h.setXY(headerRight.getX(), holeY);
         }
      }
      if (headerBottom != null) {
         if (headerLeft != null) {
            headerHoles[HOLE_3_BOT_LEFT] = new Drawable(gc, styleClass.getStyleClass(IBOTypesGui.LINK_67_STYLE_VIEWPANE_HOLE));
            IDrawable h = headerHoles[HOLE_3_BOT_LEFT];
            h.init(headerLeft.getDrawnWidth(), headerBottom.getDrawnHeight());
            h.setXY(headerLeft.getX(), headerBottom.getY());
            //System.out.println("x=" + headerLeft.getX() + " y=" + headerBottom.getY());
         }
         if (headerRight != null) {
            headerHoles[HOLE_2_BOT_RIGHT] = new Drawable(gc, styleClass.getStyleClass(IBOTypesGui.LINK_67_STYLE_VIEWPANE_HOLE));
            IDrawable h = headerHoles[HOLE_2_BOT_RIGHT];
            h.init(headerRight.getDrawnWidth(), headerBottom.getDrawnHeight());
            h.setXY(headerRight.getX(), headerBottom.getY());
         }
      }
   }

   /**
    * Important setting is {@link IBOViewPane#VP_OFFSET_11_COMPETITION_HEADER_TYPE1}
    * <br>
    */
   private void layDimensionHeaders() {
      //System.out.println("For Header Setup we have a width=" + width + " height=" + height);
      //set up sizes. We must ensure
      setImmaterialHeaders();
      int height = getBaseHeadersHeight(); //
      int width = getBaseHeadersWidth();
      int nw = width;
      int nh = height;
      int type = getCompetTypeHeader(); //
      if (type == COMPET_HEADER_0_NEUTRAL) {
         //horizontal headers have a set width and whatever height they need
         layDimensionHeadersNeutral(height, width, nw, nh);
      } else if (type == COMPET_HEADER_2_VERTICAL) {
         //vertical master: first set up the weak ones. then the strong ones, then again the weak ones
         layDimensionHeadersVerticalMaster(height, width, nw);
      } else if (type == COMPET_HEADER_1_HORIZ) {
         //horizontal master:first set the
         layDimensionHeadersHorizMaster(height, width, nh);
      }
   }

   /**
    * Top and Bot takes the space of holes for themselves
    * @param height
    * @param width
    * @param nh
    */
   protected void layDimensionHeadersHorizMaster(int height, int width, int nh) {
      setUpHeaderSize(headerLeft, 0, height);
      setUpHeaderSize(headerRight, 0, height);

      if (isRightHNotOverlay()) {
         width += headerRight.getDrawnWidth();
      }
      if (isLeftHNotOverlay()) {
         width += headerLeft.getDrawnWidth();
      }
      if (headerTop != null) {
         if (boViewPane.hasFlag(VP_OFFSET_12_INTERNAL_SIZING1, VP_SIZER_1_TOP)) {
            //our header top wants a word in the init width 
            headerTop.initSize();
            int wTop = headerTop.getDrawnWidth();
            if (wTop > width) {

            }
            //does the wid
            // height 
         } else {
            setUpHeaderSize(headerTop, width, 0);
         }
      }
      setUpHeaderSize(headerTop, width, 0);
      setUpHeaderSize(headerTopClose, width, 0);
      setUpHeaderSize(headerBottom, width, 0);

      //overlay Top or Bottom take the place if the shorten overlay flag is set.
      if (hasTechY(VP_FLAGY_2_SHORTEN_HEADER_HEIGHT)) {
         if (isTopHOverlay()) {
            height -= headerTop.getDrawnHeight();
         }
         if (isTopCloseHOverlay()) {
            height -= headerTopClose.getDrawnHeight();
         }
         if (isBotHOverlay()) {
            height -= headerBottom.getDrawnHeight();
         }
         if (height != nh) {
            setUpHeaderSize(headerLeft, 0, height);
            setUpHeaderSize(headerRight, 0, height);
         }
      }
   }

   /**
    * 
    * When {@link ViewDrawable#setHeaderSized(IDrawable, int, int)},
    * The Drawable is sized externally and its size will influence
    * the ViewPane's viewport size by increasing it if needed.
    * <br>
    * <br>
    * How?
    * 
    * @param height
    * @param width
    * @param nw
    * @param nh
    */
   protected void layDimensionHeadersNeutral(int height, int width, int nw, int nh) {

      //constraint the height unless ViewDrawable allows headers to expand
      //and use their preferred maxed to ViewContext or Parent
      //TODO it is the creator when
      setUpHeaderSize(headerLeft, 0, height);
      setUpHeaderSize(headerRight, 0, height);
      //expanding increase this width value... so in that case, those drawable 
      //should be initialized first... That means a priority in sizers.
      //how does the ViewPane knows that it needs to init headerTop first?
      //because sizer is preferred so it must compute its preferred size
      //then compare it to other preferred sizes. take the biggest
      //and reinit everyone with that size
      //when pref size and slave to scrollbar?? capped to ViewDrawable max size
      setUpHeaderSize(headerTop, width, 0);
      setUpHeaderSize(headerTopClose, width, 0);
      setUpHeaderSize(headerBottom, width, 0);
      setUpHeaderSize(headerBottomClose, width, 0);

      //in neutral, Expand have no impact
      if (isLeftHOverlay()) {
         width -= headerLeft.getDrawnWidth();
      }
      if (isRightHOverlay()) {
         width -= headerRight.getDrawnWidth();
      }
      if (isTopHOverlay()) {
         height -= headerTop.getDrawnHeight();
      }
      if (isTopCloseHOverlay()) {
         height -= headerTopClose.getDrawnHeight();
      }
      if (isBotHOverlay()) {
         height -= headerBottom.getDrawnHeight();
      }
      if (nw != width) {
         setUpHeaderSize(headerTop, width, 0);
         setUpHeaderSize(headerTopClose, width, 0);
         setUpHeaderSize(headerBottom, width, 0);
      }
      if (nh != height) {
         setUpHeaderSize(headerLeft, 0, height);
         setUpHeaderSize(headerRight, 0, height);
      }
   }

   /**
    * Set up Headers when Top/Bot are initialized first.
    * <br>
    * Left and Right take all the Height
    * @param height
    * @param width
    * @param nw
    */
   protected void layDimensionHeadersVerticalMaster(int height, int width, int nw) {
      setUpHeaderSize(headerTop, width, 0);
      setUpHeaderSize(headerTopClose, width, 0);
      setUpHeaderSize(headerBottom, width, 0);

      height = topHeadersMod(height);
      if (isBotHNotOverlay()) {
         height += headerBottom.getDrawnHeight();
      }
      setUpHeaderSize(headerLeft, 0, height);
      setUpHeaderSize(headerRight, 0, height);

      if (hasTechY(VP_FLAGY_1_SHORTEN_HEADER_WIDTH)) {
         if (isLeftHOverlay()) {
            width -= headerLeft.getDrawnWidth();
         }
         if (isRightHOverlay()) {
            width -= headerRight.getDrawnWidth();
         }
         if (width != nw) {
            setUpHeaderSize(headerLeft, width, 0);
            setUpHeaderSize(headerRight, width, 0);
         }
      }
   }

   /**
    * 1 hole for 2 block bars
    * <br>
    * Only when both {@link ScrollBar} are in Eat and/or Expand modes.
    * <br>
    * Who decides the states of a scrollbar hole? The subclass of the {@link ViewDrawable}
    */
   private void layDimensionSBHoles() {
      if (hScrollBar != null && vScrollBar != null) {
         if ((isScrollBarHorizEat() || isScrollBarHorizExpand()) && (isScrollBarVertEat() || isScrollBarVertExpand())) {
            if (vScrollBar.hasTechFlag(IBOScrollBar.SB_FLAG_3_WRAPPER)) {
               //TODO init 4 holes. only in non overlay mode
               createSBHolesArray();
               int hx = hScrollBar.getX();
               int hy = vScrollBar.getY();
               for (int i = 0; i < scrollBarHoles.length; i++) {
                  if (scrollBarHoles[i] == null) {
                     scrollBarHoles[i] = new Drawable(gc, styleClass.getStyleClass(IBOTypesGui.LINK_67_STYLE_VIEWPANE_HOLE));
                  }
                  scrollBarHoles[i].setParent(this);
                  scrollBarHoles[i].init(hScrollBar.get2ndSize(), vScrollBar.get2ndSize());
               }
               scrollBarHoles[0].setXY(hx, hy);
               int aw = hScrollBar.get2ndSize() + getViewPortWidthWithoutStyle();
               int ah = vScrollBar.get2ndSize() + getViewPortHeightWithoutStyle();

               scrollBarHoles[1].setXY(hx + aw, hy);
               scrollBarHoles[2].setXY(hx, hy + ah);
               scrollBarHoles[3].setXY(hx + aw, hy + ah);

            } else {
               //setup the scrollbar holes
               scrollBarHole = new Drawable(gc, styleClass.getStyleClass(IBOTypesGui.LINK_67_STYLE_VIEWPANE_HOLE));
               scrollBarHole.setParent(this);
               scrollBarHole.setBehaviorFlag(ITechDrawable.BEHAVIOR_23_PARENT_EVENT_CONTROL, true);

               int vBarDrawnWidth = vScrollBar.getDrawnWidth();
               int hBarDrawnHeight = hScrollBar.getDrawnHeight();
               int hx = vScrollBar.getX();
               int hy = hScrollBar.getY();

               scrollBarHole.setArea(hx, hy, vBarDrawnWidth, hBarDrawnHeight);
            }
            return;
         }

      }
      scrollBarHole = null;
      scrollBarHoles = null;
   }

   /**
    * Create and Lay scrollbar when needed :
    * @return true if at least one scrollbar was created
    */
   private boolean layDimensionScrollbars() {
      boolean isVNeeded = isScrollVNeeded();
      boolean isHNeeded = isScrollHNeeded();
      if (!isVNeeded && !isHNeeded) {
         vScrollBar = null;
         hScrollBar = null;
         return false;
      }

      //the basic drawable create a vertical scrolling config when needed
      int scrollBarPaneW = viewPortWidth;
      int scrollBarPaneH = viewPortHeight;

      //diminish styling for the scrollbar
      if (isViewPaneStyleApplied()) {
         ByteObject style = getStyleViewPane();
         //TODO only style engine know how to compute with sizers
         scrollBarPaneW -= getStyleWConsumed();
         scrollBarPaneH -= getStyleHConsumed();
      }
      if (hasTech(VP_FLAG_3_SCROLLBAR_MASTER)) {
         //take space for expanding headers
         if (isTopHExpand()) {
            scrollBarPaneH += headerTop.getDrawnHeight();
         }
         if (isTopCloseHExpand()) {
            scrollBarPaneH += headerTopClose.getDrawnHeight();
         }
         if (isBotHExpand()) {
            scrollBarPaneH += headerBottom.getDrawnHeight();
         }
         if (isLeftHExpand()) {
            scrollBarPaneW += headerLeft.getDrawnWidth();
         }
         if (isRightHExpand()) {
            scrollBarPaneW += headerRight.getDrawnWidth();
         }
      } else {
         //scrollbar space is reduced
         if (isTopHEat()) {
            scrollBarPaneH -= headerTop.getDrawnHeight();
         }
         if (isTopCloseHEat()) {
            scrollBarPaneH -= headerTopClose.getDrawnHeight();
         }
         if (isBotEat()) {
            scrollBarPaneH -= headerBottom.getDrawnHeight();
         }
         if (isLeftHEat()) {
            scrollBarPaneW -= headerLeft.getDrawnWidth();
         }
         if (isRightHEat()) {
            scrollBarPaneW -= headerRight.getDrawnWidth();
         }
      }
      scrollBarPaneW -= shrinkViewPortW;
      scrollBarPaneH -= shrinkViewPortH;
      scrollBarPaneW -= shrinkVisualW;
      scrollBarPaneH -= shrinkVisualH;

      //System.out.println("ViewPane#layDimScrollbar visualShrinkW=" + visualShrinkW + " visualShrinkH=" + visualShrinkH);

      scrollBarVPaneW = scrollBarPaneW;
      scrollBarVPaneH = scrollBarPaneH;
      scrollBarHPaneW = scrollBarPaneW;
      scrollBarHPaneH = scrollBarPaneH;
      //first check for vertical scrollbar
      if (isVNeeded) {
         initFullVBar();
      } else {
         vScrollBar = null;
      }
      if (isHNeeded) {
         initFullHBar();
      } else {
         hScrollBar = null;
      }
      if (hScrollBar != null && vScrollBar == null) {
         if (isScrollVNeeded()) {
            initFullVBar();
         }
      }
      if (hScrollBar != null && vScrollBar != null) {
         //update it again to take into account horizontal pixel consumption
         vScrollBar.init(scrollBarVPaneW, scrollBarVPaneH);
         hScrollBar.init(scrollBarHPaneW, scrollBarHPaneH);
      }
      return true;
   }

   /**
    * Lay the headers and the scrollbars according to the policy
    * The scrollable provides the Policy. Should header eat or expand the dimension?
    * If constraint is Unit visible, that will be the case
    * The scrollbar may not hide the single line.
    */
   public void layoutInvalidate(boolean topDown) {
      super.layoutInvalidate(topDown);
      doUpdateDLayout(hScrollBar, topDown);
      doUpdateDLayout(vScrollBar, topDown);
      doUpdateDLayout(scrollBarHole, topDown);
      doUpdateDLayout(headerBottom, topDown);
      doUpdateDLayout(headerLeft, topDown);
      doUpdateDLayout(headerLeftClose, topDown);
      doUpdateDLayout(headerRight, topDown);
      doUpdateDLayout(headerRightClose, topDown);
      doUpdateDLayout(headerTop, topDown);
      doUpdateDLayout(headerTopClose, topDown);

      //TODO resize may have created new drawables, thus HIDDEN state must be taken off those
      setStateFlag(ITechDrawable.STATE_03_HIDDEN, false);
      initScrollingConfigs();

   }

   /**
    * Attention for {@link GraphicsX#setTranslationShift(int, int)}: we lay headers relative to 0,0. When a X,Y will be set at the {@link ViewPane} level,
    * each component will be shifted
    */
   private void layPositionHeaders() {
      //sets x and y base. take into account scrollbar layout with xOffset and yOffset
      int xHeader = getHeaderX();
      int yHeader = getHeaderTopY();
      int leftX = xHeader;
      int leftY = yHeader;
      int topY = yHeader;
      int topX = xHeader;
      int type = getCompetTypeHeader();
      if (headerLeft != null) {
         if (type == COMPET_HEADER_0_NEUTRAL || type == COMPET_HEADER_2_VERTICAL) {
            //shift top X with the left header width
            topX += headerLeft.getDrawnWidth();
         }
      }
      if (headerTop != null) {
         if (type == COMPET_HEADER_0_NEUTRAL || type == COMPET_HEADER_1_HORIZ) {
            leftY += headerTop.getDrawnHeight();
         }
      }
      if (headerTopClose != null) {
         if (type == COMPET_HEADER_0_NEUTRAL || type == COMPET_HEADER_1_HORIZ) {
            leftY += headerTopClose.getDrawnHeight();
         }
      }
      //TOP X,Y
      int verticalY = topY;
      if (headerTop != null) {
         headerTop.setXY(topX, verticalY);
      }
      if (headerTopClose != null) {
         if (headerTop != null) {
            verticalY += headerTop.getDrawnHeight();
         }
         headerTopClose.setXY(topX, verticalY);
      }
      //BOTTOM X,Y
      if (headerBottom != null) {
         int botX = topX;
         int botY = getHeaderBotY();
         headerBottom.setXY(botX, botY);
      }
      //LEFT X,Y
      if (headerLeft != null) {
         headerLeft.setXY(leftX, leftY);
      }
      //RIGHT X,Y
      if (headerRight != null) {
         int rightX = getHeaderXRight();
         int rightY = leftY;
         headerRight.setXY(rightX, rightY);
      }

   }

   /**
    * 
    * Position the boundary box of each scrollbars.
    * <br>
    * Boundary box is computed and then given .
    * <br>
    * Both scrollbars in the same {@link ViewPane} must use the same type:
    * <li>Block
    * <li>Wrapper
    * <br>
    * Position bounbady boxes are rooted at the viewPort
    */
   private void layPositionScrollBars() {
      if (vScrollBar != null) {
         //the scrollbar box includes ViewPort style
         int xSCBox = viewedDrawable.getX();
         int ySCBox = viewedDrawable.getY();
         if (isViewPaneStyleApplied()) {
            xSCBox += getStyleWLeftConsumed();
            ySCBox += getStyleHTopConsumed();
         }
         if (!hasTech(VP_FLAG_3_SCROLLBAR_MASTER)) {
            ySCBox = topHeadersMod(ySCBox);
            if (isLeftHNotOverlay()) {
               xSCBox += headerLeft.getDrawnWidth();
            }
         }
         if (hScrollBar != null && isScrollBarVertNotOverlay()) {
            ySCBox += hScrollBar.getYShiftTop();
         }
         if (hScrollBar != null && isScrollBarHorizNotOverlay()) {
            xSCBox += hScrollBar.getXShiftLeft();
         }
         vScrollBar.setXY(xSCBox, ySCBox);
      }
      if (hScrollBar != null) {
         int xSCBox = viewedDrawable.getX();
         int ySCBox = viewedDrawable.getY();
         if (isViewPaneStyleApplied()) {
            xSCBox += getStyleWLeftConsumed();
            ySCBox += getStyleHTopConsumed();
         }
         if (!hasTech(VP_FLAG_3_SCROLLBAR_MASTER)) {
            ySCBox = topHeadersMod(ySCBox);
            if (isLeftHNotOverlay()) {
               xSCBox += headerLeft.getDrawnWidth();
            }
         }
         if (vScrollBar != null && isScrollBarVertNotOverlay()) {
            ySCBox += vScrollBar.getYShiftTop();
         }
         if (vScrollBar != null && isScrollBarHorizNotOverlay()) {
            xSCBox += vScrollBar.getXShiftLeft();
         }
         hScrollBar.setXY(xSCBox, ySCBox);
      }
   }

   private int leftHeadersMod(int x) {
      if (isLeftHNotOverlay()) {
         x += headerLeft.getDrawnWidth();
      }
      return x;
   }

   protected void manageAnimHoriz(InputConfig ic, ScrollConfig scX, boolean isLeft) {
      int moveSizePixel = scX.getPixelSizeLastMove();
      int moveSizeIncr = scX.getLastEffectiveChange();
      if (isLeft) {
         moveAnimZLeft(ic, moveSizePixel, moveSizeIncr);
      } else {
         moveAnimZRight(ic, moveSizePixel, moveSizeIncr);
      }
   }

   protected void manageAnimVert(InputConfig ic, ScrollConfig scY, boolean isUp) {
      int moveSizePixel = scY.getPixelSizeLastMove();
      int moveSizeIncr = scY.getLastEffectiveChange();
      if (isUp) {
         moveAnimZUp(ic, moveSizePixel, moveSizeIncr);
      } else {
         moveAnimZDown(ic, moveSizePixel, moveSizeIncr);
      }
   }

   /**
    * Called by {@link ViewDrawable#manageKeyInput(InputConfig)}. 
    * <br>
    * <br>Thus implementation may decide to manage everything themselves. 
    * <br>
    * This method delegates to {@link ScrollBar#manageKeyInput(InputConfig)}. 
    * <br>
    * <br>
    * {@link ViewPane#navigateDown(InputConfig)} is not called in this line of code.
    */
   public void manageKeyInput(InputConfig ic) {
      if (vScrollBar != null) {
         vScrollBar.manageKeyInput(ic);
      }
      if (hScrollBar != null) {
         hScrollBar.manageKeyInput(ic);
      }
   }

   /**
    * {@link ViewDrawable} gets to handle it first by calling isInputInsideViewPort. 
    * <br>
    * <br>
    * <b>Behavior</b> <br>
    * <li>When Pressed on a Scrollbar area, ViewPane locks. <br>
    * <li>When dragged, the viewpane updates the scrolling config accordingly
    * <li>When released, ViewPane unlock
    * <br>
    * <br>
    * On some drawables, this behavior only works when first Pressed on Scrollbar.<br>
    * Mainly because pressing inside the viewport will select sub items. <br>
    * <br>
    * This is checked with instance of Nav or if Behavior has been set explicitely.
    * <br>
    * If inside ViewPort and virgin state. do nothing.
    * <br>
    * <br>
    */
   public void managePointerInput(InputConfig ic) {
      //System.out.println("#ViewPane#managePointerInput " + ic.x + "," + ic.y);

      //the following if checks if previous event is on a scrollbar and redirects it . is it still needed?
      if (ic.isDragged(viewedDrawable)) {
         viewedDrawable.managePointerInputViewPort(ic);
         return;
      } else if (ic.isDragged(this)) {
         draggingViewPane(ic);
      }
      //check the overlayed headers and scollbars
      if (viewedDrawable.hasFlagView(ViewDrawable.FLAG_VSTATE_03_VIEWPANE_OVERLAY)) {
         managePointerSattelite(ic);
         if (ic.isActionDone()) {
            //cancel what was done in slaved?
            return;
         }
      }
      //then forward event to ViewDrawable
      if (isInsideViewPort(ic)) {
         viewedDrawable.managePointerInputViewPort(ic);
         return;
      } else {
         //else try to see if any sattelite process the event 
         managePointerSattelite(ic);
         if (!ic.isActionDone()) {
            //ask Drawable ViewPane to do it
            //System.out.println("#ViewPane calls Drawable#managePointerInput");
            //super.managePointerInput(ic);
         }

      }
   }

   /**
    * 
    * @param ic
    * @param d
    */
   private void managePointerInputHelper(InputConfig ic, IDrawable d) {
      if (d != null && DrawableUtilz.isInside(ic, d)) {
         d.managePointerInput(ic);
      }
   }

   private IDrawable managePointerInputHelper(int x, int y, IDrawable d, ExecutionContextGui ex) {
      if (d != null && DrawableUtilz.isInside(x, y, d)) {
         return d.getDrawable(x, y, ex);
      }
      return null;
   }

   /**
    * A Sattelite maybe slaved to ViewDrawable in which case, all events go straight to ViewDrawable.
    * <br>
    * <br>
    * The base {@link IDrawable#managePointerInput(InputConfig)} is not called.
    * @param ic
    */
   protected void managePointerSattelite(InputConfig ic) {
      managePointerInputHelper(ic, vScrollBar);
      managePointerInputHelper(ic, hScrollBar);
      managePointerInputHelper(ic, headerTop);
      managePointerInputHelper(ic, headerTopClose);
      managePointerInputHelper(ic, headerBottom);
      managePointerInputHelper(ic, headerLeft);
      managePointerInputHelper(ic, headerRight);
      managePointerInputHelper(ic, scrollBarHole);

      //if attached to a dragger adapter
      //      if (DrawableUtilz.isInside(ic, scrollBarHole)) {
      //         //start the dragging process
      //         super.managePointerInput(ic);
      //         if (ic.isPressed()) {
      //            PointerGesture pg = ic.getOrCreateGesture();
      //            pg.dragStarter(this, viewedDrawable.getX(), viewedDrawable.getY(), ic);
      //         }
      //      }
      if (headerHoles != null) {
         for (int i = 0; i < headerHoles.length; i++) {
            managePointerInputHelper(ic, headerHoles[i]);
         }
      }
      if (scrollBarHoles != null) {
         for (int i = 0; i < scrollBarHoles.length; i++) {
            managePointerInputHelper(ic, scrollBarHoles[i]);
         }
      }
   }

   private void mapViewDrawableToViewPane() {
      //set the ViewPane positoin and dimension 
      setX(viewedDrawable.getX());
      setY(viewedDrawable.getY());
      //since ViewPane is a Drawable. Dimension of ViewPane includes everything!
      setDw(viewedDrawable.getDrawnWidth());
      setDh(viewedDrawable.getDrawnHeight());
   }

   private void maxViewPortH(IDrawable d, int maxHeight, int flag) {
      if (d != null) {
         if (boViewPane.hasFlag(VP_OFFSET_12_INTERNAL_SIZING1, flag)) {
            //our header top wants a word in the init width 
            d.initSize();
            int h = d.getDrawnHeight();
            if (h > viewPortHeight) {
               //we force the maximum size
               viewPortWidth = getNewValue(h, maxHeight);
            }
         }
      }
   }

   private int getNewValue(int drwa, int max) {
      if (max == -1) {
         //no max take it
         return drwa;
      } else if (drwa < max) {
         return drwa;
      } else {
         return max;
      }
   }

   /**
    * when max-1
    * @param d
    * @param maxWidth
    * @param flag
    */
   private void maxViewPortW(IDrawable d, int maxWidth, int flag) {
      if (d != null) {
         if (boViewPane.hasFlag(VP_OFFSET_12_INTERNAL_SIZING1, flag)) {
            //our header top wants a word in the init width 
            d.initSize();
            int wTop = d.getDrawnWidth();
            if (wTop > viewPortWidth) {
               viewPortWidth = getNewValue(wTop, maxWidth);
               //we force the maximum size
            }
         }
      }
   }

   private int minusStyleValuesH(int val, int xflag, int tech) {
      if (hasTechX(xflag)) {
         int styleType = getTech().get1(tech);
         switch (styleType) {
            case DRW_STYLE_0_VIEWDRAWABLE:
               val -= viewedDrawable.getStyleHConsumed();
               break;
            case DRW_STYLE_1_VIEWPANE:
               val -= getStyleHConsumed();
               break;
            case DRW_STYLE_2_VIEWPORT:
               val -= viewPortStyleCache.getStyleHAll();
               break;
            default:
               break;
         }
      }
      return val;
   }

   private int minusStyleValuesW(int val, int xflag, int tech) {
      if (hasTechX(xflag)) {
         int styleType = getTech().get1(tech);
         switch (styleType) {
            case DRW_STYLE_0_VIEWDRAWABLE:
               val -= viewedDrawable.getStyleWConsumed();
               break;
            case DRW_STYLE_1_VIEWPANE:
               val -= getStyleWConsumed();
               break;
            case DRW_STYLE_2_VIEWPORT:
               val -= viewPortStyleCache.getStyleWAll();
               break;
            default:
               break;
         }
      }
      return val;
   }

   void moveAnim(InputConfig ic, int moveSize, int trail, int moveAngle, int siStartMod, boolean horiz) {
      ScrollConfig sc = (horiz) ? getSCvertical() : getSChorizontal();
      ScrollConfig scClone = sc.cloneModVisible(siStartMod, Math.abs(siStartMod));
      ScrollConfig opposite = (horiz) ? getSChorizontal() : getSCvertical();
      //the image depends if append is needed
      //somehow, MoveGhost must remember the cached image increments
      FunctionMove mf = getMoveFunction(moveAngle, moveSize);
      MoveGhost move = null;
      getAnimCreator().animPlay(move);
   }

   /**
    * Called when a Scrolling Down is effective after a pressed,not during a repeater, after repeat/release
    * <br>
    * If there is still a reason to animate the scrolling.
    * Animation optimization
    * Depending on the situation, if an animation is already rolling, modifies the Move path
    * and append Images to the {@link Move}. <br>
    * Repaint only content drawable. Talk to ViewPane with {@link GraphicsX} object.
    * ViewPane only repaints the
    * <br>
    * {@link RgbDrawable} is used to host the moving and {@link ITechDrawable#STATE_20_ANIMATED_FULL_HIDDEN} is set in {@link ViewDrawable}.
    * <br>
    * In Case of full repaint what happens?
    */
   void moveAnimDown() {
   }

   /**
    * We need the siStart and siVisible and siPixelSize for the move.
    * @param ic
    * @param moveSiStart increment of the
    * @param moveSiVisible absolute increment of number of visible increment of the move
    * @param moveSiPixelSize pixel amplitude of the move
    */
   private void moveAnimGhostHorizLeft(InputConfig ic, int moveSiStart, int moveSiVisible, int moveSiPixelSize) {
      ScrollConfig opposite = getSCvertical();
      initMoveGhost();
      //already modified
      ScrollConfig base = getSChorizontal();

      if (move.hasAnimFlag(IAnimable.ANIM_12_STATE_WORKING)) {
         //still in the animator loop. no need to rush it
         //augment it. in case of reverse, check MoveGhost current increment coverage

         //
         int[] d = trailScope.getIntsRef();
         int len = d[0];
         boolean isAddTrail = true;
         for (int i = 1; i < len; i += 2) {
            if (d[i] == moveSiStart) {
               isAddTrail = false;
            }
         }

         if (isAddTrail) {
            int start = base.getSIStart();
            ScrollConfig scClone = base.cloneModVisibleSet(start, moveSiVisible);
            RgbImage ri = viewedDrawable.getViewDrawableContent(scClone, opposite);
            trailScope.addInt(start);
            trailScope.addInt(moveSiVisible);
            RgbDrawable rd = new RgbDrawable(gc, gc.getDefaultSC(), ri);
            move.addTrail(rd, ITechMoveFunction.TRAIL_3_RIGHT);
         }

         synchronized (move) {
            move.setShiftDestOffset(moveSiPixelSize, 0);
         }

         //check again if still in the loop

      } else {
         //reuse and check if image is already covering some area needed. if so, add a trail if needed.

         //for a reverse, look at the trailScope. when inside. don't recreate image? what if a state has changed?
         //trailScope is useless when selected state changes at each time a move occurs.
         //how do we check visual state change? we can't. we are talking increments here. not aware of Drawables.
         // so ViewDrawable must externally invalidate a given increment in the trailScope.

         if (viewedDrawable.hasFlagView(FLAG_VSTATE_04_NO_CONTENT_STATE)) {

         } else {
            move.resetTrail();

         }

         ScrollConfig scClone = base.cloneModVisible(moveSiStart, moveSiVisible);
         RgbImage ri = viewedDrawable.getViewDrawableContent(scClone, opposite);
         trailScope.addInt(moveSiStart);
         trailScope.addInt(moveSiVisible);
         RgbDrawable rd = new RgbDrawable(gc, gc.getDefaultSC(), ri);
         move.addTrail(rd);
         //do the move function
         FunctionMove mf = getMoveFunction(C.DIR_2_LEFT, moveSiPixelSize);
         move.setMoveFunction(mf);
      }

      if (!move.hasAnimFlag(IAnimable.ANIM_12_STATE_WORKING)) {
         getAnimCreator().animPlay(move);
      }
   }

   /**
    * In case of 
    * @param ic
    * @param moveSiStartIncr
    * @param moveSiVisible
    * @param moveSiPixelSize
    */
   private void moveAnimGhostVertDown(InputConfig ic, int moveSiStartIncr, int moveSiVisible, int moveSiPixelSize) {
      ScrollConfig opposite = getSChorizontal();
      initMoveGhost();
      //already modified
      ScrollConfig base = getSCvertical();

      if (move.hasAnimFlag(IAnimable.ANIM_12_STATE_WORKING)) {
         //still in the animator loop. no need to rush it
         //augment it. in case of reverse, check MoveGhost current increment coverage

         //
         int[] d = trailScope.getIntsRef();
         int len = d[0];
         boolean isAddTrail = true;
         for (int i = 1; i < len; i += 2) {
            if (d[i] == moveSiStartIncr) {
               isAddTrail = false;
            }
         }

         if (isAddTrail) {
            int start = base.getSIStart();
            ScrollConfig scClone = base.cloneModVisibleSet(start, moveSiVisible);
            RgbImage ri = viewedDrawable.getViewDrawableContent(scClone, opposite);
            trailScope.addInt(start);
            trailScope.addInt(moveSiVisible);
            RgbDrawable rd = new RgbDrawable(gc, gc.getDefaultSC(), ri);
            move.addTrail(rd, ITechMoveFunction.TRAIL_3_RIGHT);
         }

         synchronized (move) {
            move.setShiftDestOffset(moveSiPixelSize, 0);
         }

         //check again if still in the loop

      } else {
         //reuse and check if image is already covering some area needed. if so, add a trail if needed.

         //for a reverse, look at the trailScope. when inside. don't recreate image? what if a state has changed?
         //trailScope is useless when selected state changes at each time a move occurs.
         //how do we check visual state change? we can't. we are talking increments here. not aware of Drawables.
         // so ViewDrawable must externally invalidate a given increment in the trailScope.

         if (viewedDrawable.hasFlagView(FLAG_VSTATE_04_NO_CONTENT_STATE)) {

         } else {
            move.resetTrail();

         }

         ScrollConfig scClone = base.cloneModVisible(moveSiStartIncr, moveSiVisible);
         //#debug
         toDLog().pAnim("", scClone, ViewPane.class, "moveAnimGhostVertDown", LVL_05_FINE, true);

         RgbImage ri = viewedDrawable.getViewDrawableContent(opposite, scClone);
         trailScope.addInt(moveSiStartIncr);
         trailScope.addInt(moveSiVisible);
         RgbDrawable rd = new RgbDrawable(gc, gc.getDefaultSC(), ri);
         move.addTrail(rd);
         //do the move function
         FunctionMove mf = getMoveFunction(C.DIR_1_BOTTOM, moveSiPixelSize);
         //#debug
         toDLog().pAnim("MoveFunction", mf, ViewPane.class, "moveAnimGhostVertDown", LVL_05_FINE, true);
         move.setMoveFunction(mf);
         move.reset();
         //starting stuff must notify for drawable state changes
         viewedDrawable.addFullAnimation(move);
      }

      if (!move.hasAnimFlag(IAnimable.ANIM_12_STATE_WORKING)) {

         //MasterCanvas.getMasterCanvas().animPlay(move);
      }
   }

   /**
    * In case of 
    * @param ic
    * @param moveSiStartIncr
    * @param moveSiVisible
    * @param moveSiPixelSize
    */
   private void moveAnimGhostVertUp(InputConfig ic, int moveSiStartIncr, int moveSiVisible, int moveSiPixelSize) {
      ScrollConfig opposite = getSChorizontal();
      initMoveGhost();
      //already modified
      ScrollConfig base = getSCvertical();
      if (move.hasAnimFlag(IAnimable.ANIM_12_STATE_WORKING)) {

      } else {
         move.resetTrail();
         ScrollConfig scClone = base.cloneModVisible(moveSiStartIncr, moveSiVisible);

         RgbImage ri = viewedDrawable.getViewDrawableContent(opposite, scClone);
         trailScope.addInt(moveSiStartIncr);
         trailScope.addInt(moveSiVisible);
         RgbDrawable rd = new RgbDrawable(gc, gc.getDefaultSC(), ri);
         move.addTrail(rd);
         FunctionMove mf = getMoveFunction(C.DIR_0_TOP, moveSiPixelSize);
         move.setMoveFunction(mf);
         viewedDrawable.addFullAnimation(move);
      }
   }

   /**
    * The
    * @param ic
    * @param moveSize
    * @param moveSizeIncr
    */
   void moveAnimZDown(InputConfig ic, int moveSize, int moveSizeIncr) {
      //the base Scrolling config has already been updated. so -1
      moveAnimGhostVertDown(ic, -1, moveSizeIncr, moveSize);
      //moveAnim(ic, moveSize, MoveGhost.TRAIL_0_UP, C.ANGLE_RIGHT_0, moveSizeIncr, false);
   }

   /**
    * How do we know the change.
    * Pixel/Logic/Page must be matched increments?
    * @param moveSize the number of pixels to the left have to move
    */
   void moveAnimZLeft(InputConfig ic, int moveSize, int moveSizeIncr) {
      moveAnimGhostHorizLeft(ic, -moveSizeIncr, moveSizeIncr, moveSize);
      //moveAnim(ic, moveSize, MoveGhost.TRAIL_3_RIGHT, C.ANGLE_LEFT_180, -moveSizeIncr, true);
   }

   void moveAnimZRight(InputConfig ic, int moveSize, int moveSizeIncr) {
      moveAnim(ic, moveSize, ITechMoveFunction.TRAIL_2_LEFT, C.ANGLE_RIGHT_0, moveSizeIncr, true);
   }

   void moveAnimZUp(InputConfig ic, int moveSizePixel, int moveSizeIncr) {
      ScrollConfig base = getSCvertical();
      moveAnimGhostVertUp(ic, 0, moveSizeIncr, moveSizePixel);
      // moveAnim(ic, moveSize, MoveGhost.TRAIL_1_DOWN, C.ANGLE_RIGHT_0, -moveSizeIncr, false);
   }

   /**
    * Moves the horizontal part and updates any {@link ScrollBar}.
    * <br>
    * <br>
    * Modifies the horizontal {@link ScrollConfig} by setting the starting increment.
    * <br>
    * @param newStart the new start increment
    */
   public void moveSetX(InputConfig ic, int newStart) {
      ScrollConfig scX = getSChorizontal();
      if (scX != null) {
         scX.setSIStartNoEx(newStart);
         hScrollBar.updateStructure();
      }
   }

   /**
    * Updates visual hooks on scrollbar
    * @param vy
    */
   public void moveSetY(InputConfig ic, int vy) {
      ScrollConfig scY = getSCvertical();
      if (scY != null) {
         //SystemLog.printFlow("#ViewPane#moveSetY " + vy);
         scY.setSIStartNoEx(vy);
         vScrollBar.updateStructure();
      }
   }

   /**
    * Takes the {@link ScrollConfig} move vs and update horizontal {@link ScrollBar}'s structure
    * <br>
    * <br>
    * @param vx the number of increments to move the X axis. Semantics depends on {@link ScrollConfig}.
    */
   public void moveX(InputConfig ic, int vx) {
      ScrollConfig scX = getSChorizontal();
      if (scX != null && vx != 0) {
         scX.move(vx);
         hScrollBar.updateStructure();
         boolean isLeft = vx < 0;
         //need pixel
         if (hasTechX(VP_FLAGX_7_ANIMATED)) {
            //we must compute the vx to pixel new config
            manageAnimHoriz(ic, scX, isLeft);
         }
         //we need the pixels in absolute including the extra separators
      }
   }

   /**
    * Moves the vertical part.
    * <br>
    * <br>
    * Modifies the vertical {@link ScrollConfig} by setting the starting increment.
    * @param vy
    */
   public void moveY(InputConfig ic, int vy) {
      ScrollConfig scY = getSCvertical();
      if (scY != null) {
         scY.move(vy);
         vScrollBar.updateStructure();
         boolean isUp = vy < 0;
         if (hasTechX(VP_FLAGX_7_ANIMATED)) {
            //we must compute the vx to pixel new config
            manageAnimVert(ic, scY, isUp);
         }
      }
   }

   /**
    * If the {@link ViewDrawable} want to control his navigation because it is itself an {@link IDrawable},
    * <br>
    * <br>
    * it will call {@link ViewPane#navigateDown(InputConfig)} otherwise.
    * <br>
    * <br>
    */
   public void navigateDown(InputConfig ic) {
      if (vScrollBar != null) {
         vScrollBar.navigateDown(ic);
      }
   }

   public void navigateLeft(InputConfig ic) {
      if (hScrollBar != null) {
         hScrollBar.navigateLeft(ic);
      }
   }

   public void navigateRight(InputConfig ic) {
      if (hScrollBar != null) {
         hScrollBar.navigateRight(ic);
      }
   }

   public void navigateSelect(InputConfig ic) {
      //nothing to select here
   }

   public void navigateUp(InputConfig ic) {
      if (vScrollBar != null) {
         vScrollBar.navigateUp(ic);
      }
   }

   /**
    * Must check that both animations have stopped.
    * <br>
    * 
    */
   public void notifyEventAnimFinished(Object o) {
      //SystemLog
      if (moveHoriz != null && moveVert != null) {
         if (moveHoriz.hasAnimFlag(IAnimable.ANIM_13_STATE_FINISHED) && moveVert.hasAnimFlag(IAnimable.ANIM_13_STATE_FINISHED)) {
            viewedDrawable.setStateFlag(ITechDrawable.STATE_22_ANIMATED_CONTENT_HIDDEN, false);
         }
      } else {
         viewedDrawable.setStateFlag(ITechDrawable.STATE_22_ANIMATED_CONTENT_HIDDEN, false);
      }

   }

   public void notifyEventAnimStarted(Object o) {
      //SystemLog
      viewedDrawable.setStateFlag(ITechDrawable.STATE_22_ANIMATED_CONTENT_HIDDEN, true);
   }

   public void pointerPressed(int x, int y) {

   }

   public void pointerReleased(int x, int y) {

   }

   public boolean removeDrawable(IDrawable d) {
      boolean isFound = false;
      if(d==headerLeft) {
         isFound = true;
      }
      if(isFound) {
         d.setParent(null);
      }
      return isFound;
   }

   private int rightHeadersMod(int x) {
      if (isRightHNotOverlay()) {
         x += headerRight.getDrawnWidth();
      }
      return x;
   }

   /**
    * 
    * 
    * 
    * @param d can be null to remove a header
    * @param pos {@link C#POS_1_BOT}
    * @param posType  {@link ITechViewPane#PLANET_MODE_0_EAT}
    */
   public void setHeaderSized(IDrawable d, int pos, int posType) {
      //#debug
      if (!d.getLayEngine().getArea().isValidSize()) {
         throw new IllegalArgumentException("This method only accepts sized drawables");
      }

      //For TOP and BOTTOM we must ensure width shrinking flag is not set
      boolean val = false;
      if (d != null) {
         val = true;
      }
      //equivalent to old zero.
      //we want pref height as in the drawable can take what it wants, fixed sizeW
      switch (pos) {
         case C.POS_0_TOP:
            boViewPane.setFlag(VP_OFFSET_12_INTERNAL_SIZING1, VP_SIZER_1_TOP, val);
            break;
         case C.POS_1_BOT:
            boViewPane.setFlag(VP_OFFSET_12_INTERNAL_SIZING1, VP_SIZER_2_BOT, val);
            break;
         case C.POS_2_LEFT:
            boViewPane.setFlag(VP_OFFSET_12_INTERNAL_SIZING1, VP_SIZER_3_LEFT, val);
            break;
         case C.POS_3_RIGHT:
            boViewPane.setFlag(VP_OFFSET_12_INTERNAL_SIZING1, VP_SIZER_4_RIGHT, val);
            break;
         default:
            throw new IllegalArgumentException("Unknown Position" + pos);
      }
      //flag drawable as internally sized?
      setHeaderPrivate(d, pos, posType);
   }

   /**
    * Header close follow the EAT/EXPAND policy of outer header
    * <br>
    * {@link ITechViewPane#PLANET_MODE_0_EAT}
    * {@link ITechViewPane#PLANET_MODE_1_EXPAND}
    * <br>
    * So by default its EAT
    * @param d
    * @param pos
    * @param eat
    */
   public void setHeaderClose(IDrawable d, int pos) {
      switch (pos) {
         case C.POS_0_TOP:
            headerTopClose = d;
            break;
         case C.POS_1_BOT:
            headerBottomClose = d;
            break;
         case C.POS_2_LEFT:
            headerLeftClose = d;
            break;
         case C.POS_3_RIGHT:
            headerRight = d;
            break;
         default:
            throw new IllegalArgumentException("Unknown Position" + pos);
      }
      if (d != null) {
         d.setParent(viewedDrawable);
      }
      setHeaderOverride(d, pos);
   }

   /**
    * 
    * 4 possible positions defined by
    * <li> {@link C#POSDIAG_0_TOP_LEFT}
    * <li> {@link C#POSDIAG_1_TOP_RIGHT}
    * <li> {@link C#POSDIAG_2_BOT_RIGHT}
    * <li> {@link C#POSDIAG_3_BOT_LEFT}
    * <br>
    * <br>
    * Not shown when ViewPane settings don't create holes.
    * 
    * TODO holes influencing sizing.
    * @param d
    * @param pos 
    */
   public void setHeaderHole(IDrawable d, int pos) {
      if (headerHoles == null) {
         headerHoles = new IDrawable[4];
      }
      headerHoles[pos] = d;
   }

   /**
    * Header has been set its sizers with {@link IDrawable#setSizers(ByteObject, ByteObject)}.
    * <br>
    * <br>
    * Usually we don't want shrinking sizers.
    * <br>
    * <br>
    * In all cases, ViewPane sets absolute sizers for Width in case
    * <br>
    * Cue Sizers are decided by the caller. However the {@link ViewPane} ...
    * 
    * @param d when null, removes any header at the specified position
    * @param pos {@link C#POS_0_TOP}
    * @param posType
    */
   public void setHeader(IDrawable d, int pos, int posType) {
      setHeaderPrivate(d, pos, posType);
      setHeaderOverride(d, posType);
   }

   private void setHeaderPrivate(IDrawable d, int pos, int posType) {
      //For TOP and BOTTOM we must ensure width shrinking flag is not set
      //We must also overrides
      switch (pos) {
         case C.POS_0_TOP:
            headerTop = d;
            boViewPane.setValue2Bits1(VP_OFFSET_04_HEADER_PLANET_MODE1, posType);
            break;
         case C.POS_1_BOT:
            headerBottom = d;
            boViewPane.setValue2Bits2(VP_OFFSET_04_HEADER_PLANET_MODE1, posType);
            break;
         case C.POS_2_LEFT:
            headerLeft = d;
            boViewPane.setValue2Bits3(VP_OFFSET_04_HEADER_PLANET_MODE1, posType);
            break;
         case C.POS_3_RIGHT:
            headerRight = d;
            boViewPane.setValue2Bits4(VP_OFFSET_04_HEADER_PLANET_MODE1, posType);
            break;
         default:
            throw new IllegalArgumentException("Unknown Position" + pos);
      }
      if (d != null) {
         d.setParent(viewedDrawable);
      }
   }

   private void setHeaderOverride(IDrawable d, int pos) {
      LayEngineDrawable engine = d.getLayEngine();
      switch (pos) {
         case C.POS_0_TOP:
            engine.setOverrideXYWH(true, true, true, false);
            break;
         case C.POS_1_BOT:
            engine.setOverrideXYWH(true, true, true, false);
            break;
         case C.POS_2_LEFT:
            engine.setOverrideXYWH(true, true, false, true);
            break;
         case C.POS_3_RIGHT:
            engine.setOverrideXYWH(true, true, false, true);
            break;
         default:
            throw new IllegalArgumentException("Unknown Position" + pos);
      }
   }

   private void setImmaterialHeaders() {
      if (headerRight != null) {
         if (boViewPane.get2Bits4(VP_OFFSET_04_HEADER_PLANET_MODE1) == PLANET_MODE_3_IMMATERIAL) {
            headerRight.setBehaviorFlag(ITechDrawable.BEHAVIOR_16_IMMATERIAL, true);
         }
      }
      if (headerLeft != null) {
         if (boViewPane.get2Bits3(VP_OFFSET_04_HEADER_PLANET_MODE1) == PLANET_MODE_3_IMMATERIAL) {
            headerRight.setBehaviorFlag(ITechDrawable.BEHAVIOR_16_IMMATERIAL, true);
         }
      }
      if (headerTop != null) {
         if (boViewPane.get2Bits1(VP_OFFSET_04_HEADER_PLANET_MODE1) == PLANET_MODE_3_IMMATERIAL) {
            headerRight.setBehaviorFlag(ITechDrawable.BEHAVIOR_16_IMMATERIAL, true);
         }
      }
      if (headerBottom != null) {
         if (boViewPane.get2Bits2(VP_OFFSET_04_HEADER_PLANET_MODE1) == PLANET_MODE_3_IMMATERIAL) {
            headerRight.setBehaviorFlag(ITechDrawable.BEHAVIOR_16_IMMATERIAL, true);
         }
      }
   }

   public void setSBHoles(IDrawable d) {
      scrollBarHole = d;
   }

   public void setSBHoles(IDrawable d, int pos) {
      createSBHolesArray();
      scrollBarHoles[pos] = d;
   }

   public void setStateFlag(int flag, boolean value) {
      if (flag == ITechDrawable.STATE_03_HIDDEN || flag == ITechDrawable.STATE_02_DRAWN) {
         //System.out.println("ViewPane " + Drawable.debugState(flag) + " = " + value);
         if (headerTop != null) {
            headerTop.setStateFlag(flag, value);
         }
         if (headerTopClose != null) {
            headerTopClose.setStateFlag(flag, value);
         }
         if (headerBottom != null) {
            headerBottom.setStateFlag(flag, value);
         }
         if (headerRight != null) {
            headerRight.setStateFlag(flag, value);
         }
         if (headerLeft != null) {
            headerLeft.setStateFlag(flag, value);
         }
         if (vScrollBar != null) {
            vScrollBar.setStateFlag(flag, value);
         }
         if (hScrollBar != null) {
            hScrollBar.setStateFlag(flag, value);
         }
         if (scrollBarHole != null) {
            scrollBarHole.setStateFlag(flag, value);
         }
         if (headerHoles != null) {
            for (int i = 0; i < headerHoles.length; i++) {
               if (headerHoles[i] != null) {
                  headerHoles[i].setStateFlag(flag, value);
               }
            }
         }
         if (scrollBarHoles != null) {
            for (int i = 0; i < scrollBarHoles.length; i++) {
               if (scrollBarHoles[i] != null) {
                  scrollBarHoles[i].setStateFlag(flag, value);
               }
            }
         }
      }
      super.setStateFlag(flag, value);
   }

   /**
    * The style class of the ViewDrawable must be completed first
    */
   public void setStyleClass(StyleClass sc) {
      super.setStyleClass(sc);
      doUpdateStyleClass();
   }

   public void setTechFlagX(int flag, boolean v) {
      boViewPane.setFlag(VP_OFFSET_02_FLAGX, flag, v);
   }

   /**
    * Changes the Mode of a TBLR header.
    * Pos is
    * <li>{@link ITechViewPane#PLANET_MODE_0_EAT}
    * <li>{@link ITechViewPane#PLANET_MODE_1_EXPAND}
    * <li>{@link ITechViewPane#PLANET_MODE_2_OVERLAY}
    * <br>
    * <br>
    * @param tblr
    * @param pos ,  or 
    */
   public void setTechPos(int tblr, int pos) {

   }

   /**
    * Init the header.
    * <br>
    * Depending on the position, it will be 0 on width or h.
    * <br>
    * That means
    * @param d
    * @param w
    * @param h
    */
   private void setUpHeaderSize(IDrawable d, int w, int h) {
      if (d != null) {
         LayEngineDrawable engine = d.getLayEngine();
         Zer2DArea area = engine.getLay().getArea();
         ByteObject singletonSizerPref = gc.getLAC().getSizerFactory().getSingletonSizerPref();
         if (w == 0) {
            //if it does not have a sizer
            if (area.getSizerW() == null) {
               area.setSizerW(singletonSizerPref);
            }
         } else {
            engine.setOverrideW(w);
         }
         if (h == 0) {
            //if it does not have a sizer
            if (area.getSizerH() == null) {
               area.setSizerH(singletonSizerPref);
            }
         } else {
            engine.setOverrideH(h);
         }
         engine.layoutUpdateSizeCheck();
      }
   }

   /**
    * Sets {@link ITechViewDrawable#FLAG_VSTATE_03_VIEWPANE_OVERLAY} to indicate at least one viewpane satellite is drawn in overlay
    * <br>
    * Called by {@link ViewPane#layDimensionArtifacts()}
    */
   private void setupOverlayState() {
      boolean isOverlayed = false;
      if (isScrollBarHorizOverlay() || isScrollBarVertOverlay()) {
         isOverlayed = true;
      }
      viewedDrawable.setFlagView(FLAG_VSTATE_03_VIEWPANE_OVERLAY, isOverlayed);
   }

   public void setViewState(ViewState vs) {
      super.setViewState(vs);

   }

   /**
    * {@link ViewPane} can thought as having its own coordinate space for scrollbars,header and viewport.
    * <br>
    * <br>
    */
   public void setXY(int x, int y) {
      //check the difference and moves all headers and scrollbars
      //accordingly
      int xd = x - this.getX();
      int yd = y - this.getY();
      shiftXY(headerTop, xd, yd);
      shiftXY(headerTopClose, xd, yd);
      shiftXY(headerBottom, xd, yd);
      shiftXY(headerLeft, xd, yd);
      shiftXY(headerRight, xd, yd);
      if (hScrollBar != null) {
         hScrollBar.shiftXY(xd, yd);
      }
      if (vScrollBar != null) {
         vScrollBar.shiftXY(xd, yd);
      }
      shiftXY(scrollBarHole, xd, yd);
      if (headerHoles != null) {
         for (int i = 0; i < headerHoles.length; i++) {
            shiftXY(headerHoles[i], xd, yd);
         }
      }
      if (scrollBarHoles != null) {
         for (int i = 0; i < scrollBarHoles.length; i++) {
            shiftXY(scrollBarHoles[i], xd, yd);
         }
      }
      super.setXY(x, y);
   }

   //#enddebug

   protected void styleValidateViewPane() {
      int styleType = getTech().get1(VP_OFFSET_13_STYLE_VIEWPANE_MODE1);
      if (styleType == DRW_STYLE_0_VIEWDRAWABLE) {
         styleViewPane = viewedDrawable.style;
      } else if (styleType == DRW_STYLE_1_VIEWPANE) {
         styleViewPane = styleClass.getRootStyle();
      } else if (styleType == DRW_STYLE_2_VIEWPORT) {
         viewPortSC = viewedDrawable.getStyleClass().getStyleClass(IBOTypesGui.LINK_64_STYLE_VIEWPORT);
         styleViewPane = viewPortSC.getRootStyle();
      }
   }

   //#mdebug

   /**
    * Set our styles
    */
   protected void styleValidateViewPort() {
      int styleType = getTech().get1(VP_OFFSET_14_STYLE_VIEWPORT_MODE1);
      if (styleType == DRW_STYLE_0_VIEWDRAWABLE) {
         styleViewPort = viewedDrawable.style;
      } else if (styleType == DRW_STYLE_1_VIEWPANE) {
         styleViewPort = style;
      } else if (styleType == DRW_STYLE_2_VIEWPORT) {
         viewPortSC = viewedDrawable.getStyleClass().getStyleClass(IBOTypesGui.LINK_64_STYLE_VIEWPORT);
         styleViewPort = viewPortSC.getRootStyle();
      }
   }

   /**
    * Updates y value because the Headers Top 
    * Unless in overlay mode
    * @param y
    * @return
    */
   private int topHeadersMod(int y) {
      if (boViewPane.get2Bits1(VP_OFFSET_04_HEADER_PLANET_MODE1) != PLANET_MODE_2_OVERLAY) {
         if (headerTop != null) {
            y += headerTop.getDrawnHeight();
         }
         if (headerTopClose != null) {
            y += headerTopClose.getDrawnHeight();
         }
      }
      return y;
   }

   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public static void append(Dctx dc, int index, ByteObject bo, int flag, String str) {
      if (bo.hasFlag(index, flag)) {
         dc.appendWithSpace(str);
      }
   }

   public void toString(Dctx dc) {
      dc.root(this, "ViewPane");
      dc.appendWithSpace("viewport XYWH=(" + getViewPortXOffset() + "," + getViewPortYOffset() + " " + getViewPortWidth() + "," + getViewPortHeight() + ")");
      dc.nl();
      dc.append("viewPortDIM=[" + viewPortWidth + "," + viewPortHeight + "]");
      dc.append("viewPortShrink=(" + shrinkViewPortW + "," + shrinkViewPortH + ")");
      dc.append("visualShrink=(" + shrinkVisualW + "," + shrinkVisualH + ")");
      dc.append(" scrollbarConsume=(" + getSbHorizSpaceConsumed() + "," + getSbVertSpaceConsumed() + ")");
      dc.append(" planetsConsume=(" + getViewPaneHorizSpaceConsumed() + "," + getViewPaneVerticalSpaceConsumed() + ")");
      dc.append(" expansion=(" + getExpansionWPixels() + "," + getExpansionHPixels() + ")");

      dc.append("hasAtLeastOneSizedDrawable=" + hasAtLeastOneSizedDrawable());
      if (hasAtLeastOneSizedDrawable()) {
         append(dc, VP_OFFSET_12_INTERNAL_SIZING1, boViewPane, VP_SIZER_1_TOP, "Top");
         append(dc, VP_OFFSET_12_INTERNAL_SIZING1, boViewPane, VP_SIZER_2_BOT, "Bot");
         append(dc, VP_OFFSET_12_INTERNAL_SIZING1, boViewPane, VP_SIZER_3_LEFT, "Left");
         append(dc, VP_OFFSET_12_INTERNAL_SIZING1, boViewPane, VP_SIZER_4_RIGHT, "Right");
         append(dc, VP_OFFSET_12_INTERNAL_SIZING1, boViewPane, VP_SIZER_5_TOP_CLOSE, "TopClose");
         append(dc, VP_OFFSET_12_INTERNAL_SIZING1, boViewPane, VP_SIZER_6_BOT_CLOSE, "BotClose");
         append(dc, VP_OFFSET_12_INTERNAL_SIZING1, boViewPane, VP_SIZER_7_LEFT_CLOSE, "LeftClose");
         append(dc, VP_OFFSET_12_INTERNAL_SIZING1, boViewPane, VP_SIZER_8_RIGHT_CLOSE, "RightClose");
      }
      if (boViewPane != null) {
         gc.getDrawableCoreFactory().toStringViewPaneTech(boViewPane, dc.newLevel());
      }
      super.toString(dc.sup());

      if (dc.hasFlagData(gc, IFlagsToStringGui.D_FLAG_15_VP_SCROLL_CONFIGS)) {
         ScrollConfig scV = getSCvertical();
         if (scV != null) {
            dc.nlLvl("#VerticalSC", scV);
         }
         ScrollConfig scH = getSChorizontal();
         if (scH != null) {
            dc.nlLvl("#HorizontalSC", scH);
         }
      }
      if (dc.hasFlagData(gc, IFlagsToStringGui.D_FLAG_11_VP_HEADERS)) {
         toStringHeaders(dc.newLevel());
      }
      if (dc.hasFlagData(gc, IFlagsToStringGui.D_FLAG_12_VP_SCROLLBARS)) {
         toStringScrollbars(dc.newLevel());
      }
   }

   public void toString1Line(Dctx dc) {
      dc.root(this, "ViewPane");
   }

   private void toStringHeaders(Dctx sb) {
      sb.nlLvlList("#Header Top", headerTop);
      sb.nlLvlList("#Header TopClose", headerTop);
      sb.nlLvlList("#Header Bottom", headerBottom);
      sb.nlLvlList("#Header BottomClose", headerBottomClose);
      sb.nlLvlList("#Header Right", headerRight);
      sb.nlLvlList("#Header RightClose", headerRightClose);
      sb.nlLvlList("#Header Left", headerLeft);
      sb.nlLvlList("#Header LeftClose", headerLeftClose);
      if (headerHoles != null) {
         sb = sb.newLevel();
         sb.append("#Header Holes");
         sb.nlLvl("TopLeft", headerHoles[HOLE_0_TOP_LEFT]);
         sb.nlLvl("TopRight", headerHoles[HOLE_1_TOP_RIGHT]);
         sb.nlLvl("BotRight", headerHoles[HOLE_2_BOT_RIGHT]);
         sb.nlLvl("BotLeft", headerHoles[HOLE_3_BOT_LEFT]);
      }
   }

   private void toStringScrollbars(Dctx sb) {
      sb.nlLvlList("#H Scrollbar", hScrollBar);
      sb.nlLvlList("#V Scrollbar", vScrollBar);
      sb.nlLvlList("#BlockScrollBar Hole", scrollBarHole);
   }
   //#enddebug
}