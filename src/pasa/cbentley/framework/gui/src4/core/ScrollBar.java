package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.objects.color.GradientFactory;
import pasa.cbentley.byteobjects.src4.objects.color.ITechGradient;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.ColorUtils;
import pasa.cbentley.core.src4.utils.interfaces.IColors;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdsCmd;
import pasa.cbentley.framework.coreui.src4.tech.ITechCodes;
import pasa.cbentley.framework.drawx.src4.ctx.DrwCtx;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.factories.FigureFactory;
import pasa.cbentley.framework.drawx.src4.factories.FigureOperator;
import pasa.cbentley.framework.drawx.src4.utils.AnchorUtils;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.canvas.PointerGestureDrawable;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceDrawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOScrollBar;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOViewPane;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawableListener;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechViewDrawable;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;
import pasa.cbentley.framework.gui.src4.utils.DrawableUtilz;
import pasa.cbentley.framework.input.src4.gesture.GestureDetector;
import pasa.cbentley.layouter.src4.engine.LayoutOperator;
import pasa.cbentley.layouter.src4.engine.SizerCtx;

/**
 * Draws and manage an artifact for scrolling a {@link ViewDrawable} in a {@link ViewPane}.
 * <br>
 * Like in the {@link SliderDrawable}, a scrollbar may also be used in a standalone as a slider for selected a value. <br>
 * <br>
 * Otherwise, it is closely tied to the {@link ViewPane} which uses a mode and options to draw the bar.
 * <br>
 * In all cases, the scrollbar communicates with a {@link ScrollConfig}. This class use the AroundTheClock option to tell
 * the scrollbar to draw greyed artifacts or not..
 * <br>
 * <br>
 * Wrapper draw at the top and the bottom for vertical mode and rightmost and leftmost for horiz mode
 * <br>
 * <br>
 * <b>Initialization </b> : <br> 
 * In a stand alone situation, the scrollbar is given a box. <br>
 * In a {@link ViewPane} context, the ViewPane computes a boundary box within which the scrollbar draws itself. <br>
 * <li>A horizontal wrapper scrollbar will take left and right extremities of the box boundary
 * <li>A vertical wrapper scrollbar will take top and bottom extremities of the box boundary
 * <li>a vertical box will take right extremity or left extremity
 * <br>
 * The {@link ScrollBar#init(int, int)} is given the area over which it may be drawn. The scrollbar computes
 * its drawable width and height.
 * The boundary box may be updated if another scrollbar reduce its area. <br>
 * In case of
 * The {@link Drawable#dw} and {@link Drawable#dh} values for wrappers are computed based on the smallest rectangle that is possible to compute.
 * that includes both wrapper.<br>
 * This rectangle decides x,y,w,h of the {@link Drawable}.
 *  <br>
 * <br>
 * 
 * <b>Overlay</b> option draws the scrollbar over the ViewPort<br>
 * {@link ITechViewPane#PLANET_MODE_2_OVERLAY} is set to {@link IBOViewPane#VP_OFFSET_05_SCROLLBAR_MODE1}.
 * At the level of the ViewPane, overlaying is decided at the axis level.
 * <br>
 * <br>
 * <b>Abilities</b> <br>
 * <li>Horizonatal/Vertical
 * <li>Wrapper figures drawn around content/scrollbar block
 * <li> a text scrollbar using 33rd UTF plane<br>
 * <li>a block line, <br>
 * <li> an Image that is rotated to draw Top, Right and Bottom<br>
 * <li> It may draws the current state of the scrolling configuration.<br>
 * <br>
 * <br>
 * In Wrapper style, the scrollbar is entirely defined by {@link IBOTypesGui#LINK_53_STYLE_SCROLLBAR_BOT_RIGHT_WRAPPER}
 * and {@link IBOTypesGui#LINK_52_STYLE_SCROLLBAR_TOP_LEFT_WRAPPER}
 * <br>
 * <br>
 * <b>Directional Figures</b> : <br> 
 * Stores model of Figure as a TechParam. using Fig_tech_Id
 * Create {@link FigDrawable} with Direction.
 * A model of Directional figure is given to the scrollbar. It automatically parametrize the creation of specific figures
 * for wrappers and block arrows.
 * {@link ByteObject#FIG_FLAG_8_TBLR_DIR}.
 * A single definition is given to the Scrollbar. It is used parametrically on Wrappers and DirOnBlock 
 * <br>
 * <br>
 * <b>Implementation Note</b>: <br>
 * <li>style of scrollbar = background
 * <li>2 styles for each wrapper. So wrapper have their own style class
 * <li>1 style for the moving block
 * <br>
 * The last 3 style have <b>greyed</b> {@link ITechDrawable#STYLE_04_GREYED} and <b>selected</b> {@link ITechDrawable#STYLE_05_SELECTED} sub styles.
 * <br>
 * <br>
 * <b>Standalone</b>: <br>
 * Used a slider. Block figure draws the content of the start increment in {@link ScrollConfig}.
 * Visibile increment is 1. {@link SliderDrawable}.
 * <br>
 * <br>
 * TODO {@link ImageDrawable} for scrollbar buttons flipped/rotated
 * <br>
 * {@link CmdCtx} for a {@link Scrollbar}.
 * <br>
 * Any special commands are executed here
 * <br>
 * @author Charles-Philip Bentley
 *
 */
public class ScrollBar extends Drawable implements IBOScrollBar, IDrawableListener {

   private int            barSize;

   /**
    * May not be drawn
    */
   private Drawable       blockBgFigure;

   /**
    * May be a FigDrawable with a string figure that is updated along the Scrolling config
    */
   private Drawable       blockFigure;

   public int             blockMinPixels = 3;

   /**
    * Created by {@link ScrollBar#createWrapperDef}
    */
   private Drawable       botRightFigure;

   /**
    * Height of the Area over which the scrollbar is initialized. <br>
    * Serves as the basis for percentage sizes
    */
   private int            boundaryBoxH;

   /**
    * Width of the Area over which the scrollbar is initialized. <br>
    * Serves as the basis for percentage sizes
    */
   private int            boundaryBoxW;

   /**
    * X coordinate of the boundary box. Absolute to the origin of {@link MasterCanvas}.
    */
   private int            boundaryBoxX;

   /**
    * Y coordinate of the boundary box. Absolute to the origin of {@link MasterCanvas}.
    */
   private int            boundaryBoxY;

   /**
    * Drawable that paint the scrolling configuration as text
    * In the case of scrolling over an unknown number
    */
   private IDrawable      dConfigInfo;

   public int             flags          = 0;

   /**
    * Number of pixels for drawing the moving bar and its background artifact.
    * <br>
    * <br>
    * It is computed as dh - HConsumed - ButtonsSize
    */
   private int            numPixelsForBlock;

   /**
    * Number of pixels consumed by one
    */
   private int            oneUnit;

   public ViewPane        pane;

   /**
    * Controls the state of the Scrollbar.
    */
   protected ScrollConfig scrollConfig;

   /**
    * 
    */
   protected ByteObject   techParam;

   /**
    * Info drawable drawn next to the block figure that displays information about the state of the scrolling.
    * <br>
    * For example, when scrolling characters, a callback updates IDrawable as a StringDrawable with the top most
    * characters being displayed.
    * This drawable is displayed in a N+1 layer as the {@link ScrollBar}.
    */
   protected IDrawable    trailer;

   protected SizerCtx     sizerCtx;

   /**
    * Content width is computed from the root style of the scrollbar.
    * <br>
    * {@link Drawable} with stylekey from scrollbar
    */
   private Drawable       topLeftFigure;

   /**
    * Constructor called from inside a ViewPane. Creates a defautl ScrollConfig
    * <br>
    * <br>
    * @param style
    * @param horiz decides which TechParam to load
    * @param vp The hosting view pane. Cannot be null with this constructor
    */
   public ScrollBar(GuiCtx gc, StyleClass scl, boolean horiz, ViewPane vp) {
      super(gc, scl);
      if (vp == null)
         throw new NullPointerException("Null ViewPane");
      pane = vp;
      sizerCtx = new SizerCtx(0, 0);
      ByteObject tech = null;
      int techid = IBOTypesGui.LINK_68_TECH_V_SCROLLBAR;
      if (horiz) {
         techid = IBOTypesGui.LINK_69_TECH_H_SCROLLBAR;
      }
      //different Tech for horizontal and vertical
      tech = styleClass.getByteObjectNotNull(techid);
      initScrollbar(tech);

   }

   public ScrollBar(GuiCtx gc, StyleClass scl, ByteObject tech) {
      super(gc, scl);
      initScrollbar(tech);
   }

   /**
    * In case of Unit scrolling, we want a smooth end animation move content smoothly .
    * <br>
    * <br>
    * Animates the Block bar of the {@link ScrollBar} when moving several pixels.
    * <br>
    * <br>
    * @param upleft
    */
   private void animCall(InputConfig ic, boolean upleft) {
      if (scrollConfig.getTypeUnit() == ITechViewPane.SCROLL_TYPE_0_PIXEL_UNIT) {
         return;
      }
      if (pane != null && pane.hasTechX(IBOViewPane.VP_FLAGX_7_ANIMATED)) {
         boolean isVert = hasTechFlag(SB_FLAG_1_VERT);
         if (isVert) {
            pane.manageAnimVert(ic, scrollConfig, upleft);
         } else {
            pane.manageAnimHoriz(ic, scrollConfig, upleft);
         }
      }
   }

   /**
    * Center wrapper scrollbar x,y coordinates
    * @param x
    * @param y
    * @param width
    * @param height
    */
   private void centerWrapper(int x, int y, int width, int height) {
      if (hasTechFlag(SB_FLAG_1_VERT)) {
         int mx = x + (width - getTBWrapperW()) / 2;
         setMyXY(mx, y);
      } else {
         int my = y + (height - getLRWrapperH()) / 2;
         setMyXY(x, my);
      }
   }

   /**
    * Wrapper means drawing 2 backgrounds and 2 borders
    * <br>
    * <br>
    * Called when {@link IBOScrollBar#SB_FLAG_5_ARROW_ON_BLOC} or {@link IBOScrollBar#SB_FLAG_3_WRAPPER} is set.
    * <br>
    * <br>
    * First looks for a specific Top/Left. then goes for generic
    * <br>
    * <br>
    * @param wrapperW
    * @param wrapperH
    */
   private void createWrapperDef() {
      StyleClass sc = styleClass.getStyleClass(IBOTypesGui.LINK_52_STYLE_SCROLLBAR_TOP_LEFT_WRAPPER);
      if (sc != null) {
         createWrapperTwo();
      } else {
         createWrapperDefSingle();
      }
   }

   /**
    * One style class for each type of wrapper.
    */
   private void createWrapperTwo() {
      StyleClass leftSC = styleClass.getStyleClass(IBOTypesGui.LINK_52_STYLE_SCROLLBAR_TOP_LEFT_WRAPPER);
      topLeftFigure = new Drawable(gc, leftSC, this);
      
      topLeftFigure.setBehaviorFlag(ITechDrawable.BEHAVIOR_23_PARENT_EVENT_CONTROL, true);
      //debug
      topLeftFigure.setDebugName("Fig" + (hasTechFlag(SB_FLAG_1_VERT) ? "Top" : "Left"));
      StyleClass rightStyleKey = styleClass.getStyleClass(IBOTypesGui.LINK_53_STYLE_SCROLLBAR_BOT_RIGHT_WRAPPER);
      botRightFigure = new Drawable(gc, rightStyleKey, this);
      botRightFigure.setBehaviorFlag(ITechDrawable.BEHAVIOR_23_PARENT_EVENT_CONTROL, true);
      //debug
      botRightFigure.setDebugName("Fig" + (hasTechFlag(SB_FLAG_1_VERT) ? "Bottom" : "Right"));
   }

   /**
    * One style classes. different byte object figures.
    */
   private void createWrapperDefSingle() {
      StyleClass sc = styleClass.getSCNotNull(IBOTypesGui.LINK_54_STYLE_CLASS_WRAPPER);
      DrwCtx dc = gc.getDC();
      //gets figure definition and take its direction type
      ByteObject fig = sc.getByteObject(IBOTypesGui.LINK_49_FIG_SCROLLBAR_WRAPPER);
      FigureFactory figFac = dc.getFigureFactory();
      GradientFactory gradFac = dc.getGradientFactory();
      if (fig == null) {
         int direction = 0;
         ByteObject wrapperFigGrad = gradFac.getGradient(ColorUtils.getRGBInt(128, 16, 16), 0, ITechGradient.GRADIENT_TYPE_TRIG_02_TOP_JESUS);
         fig = figFac.getFigTriangle(IColors.FULLY_OPAQUE_BLACK, direction, 0, wrapperFigGrad);
      }
      FigureOperator figOp = dc.getFigureOperator();

      int dirTopLeft = (hasTechFlag(SB_FLAG_1_VERT) ? C.DIR_0_TOP : C.DIR_2_LEFT);
      ByteObject topLeftFig = figOp.cloneFigDirectionanl(fig, dirTopLeft);
      topLeftFigure = new FigDrawable(gc, sc, topLeftFig);
      topLeftFigure.setParent(this);
      topLeftFigure.setBehaviorFlag(ITechDrawable.BEHAVIOR_23_PARENT_EVENT_CONTROL, true);

      int dirBotRight = (hasTechFlag(SB_FLAG_1_VERT) ? C.DIR_1_BOTTOM : C.DIR_3_RIGHT);
      ByteObject botRightFig = figOp.cloneFigDirectionanl(fig, dirBotRight);

      botRightFigure = new FigDrawable(gc, sc, botRightFig);
      botRightFigure.setParent(this);
      botRightFigure.setBehaviorFlag(ITechDrawable.BEHAVIOR_23_PARENT_EVENT_CONTROL, true);

      //debug
      topLeftFigure.setDebugName("Fig" + (hasTechFlag(SB_FLAG_1_VERT) ? "Top" : "Left"));
      botRightFigure.setDebugName("Fig" + (hasTechFlag(SB_FLAG_1_VERT) ? "Bottom" : "Right"));
   }

   public void debugBB(GraphicsX g) {
      g.drawRect(boundaryBoxX, boundaryBoxY, boundaryBoxW, boundaryBoxH);
   }

   /**
    * Apply Style Filters.
    * Most used is the {@link #STYLE_FLAGC_5_FILTER_ALL} that applies
    * to overlay scrollbars
    * @param g
    */
   private void drawBlockBar(GraphicsX g) {
      //since x,y have been set before just lay it down
      super.drawDrawableBg(g);
      if (hasTechFlag(SB_FLAG_5_ARROW_ON_BLOC)) {
         //first 
         topLeftFigure.draw(g);
         botRightFigure.draw(g);
      }
      if (blockBgFigure != null)
         blockBgFigure.draw(g);
      if (blockFigure != null) {
         blockFigure.draw(g);
      }
      super.drawDrawableFg(g);
   }

   /**
    * Paint scroll bar at origine x,y
    * The type is the master desiciveness
    * @param g
    * @param x
    * @param y
    */
   public void drawDrawable(GraphicsX g) {
      if (hasTechFlag(SB_FLAG_3_WRAPPER))
         drawWrapper(g);
      else {
         drawBlockBar(g);
      }
   }

   /**
    * Only draw background below
    * Draw the two directional figures. 
    * @param g
    */
   private void drawWrapper(GraphicsX g) {
      int secondSize = get2ndSize();
      if (hasTechFlag(SB_FLAG_1_VERT)) {
         super.drawDrawableBg(g, getX(), getY(), getDw(), secondSize);
         super.drawDrawableBg(g, getX(), getY() + getDh() - secondSize, getDw(), secondSize);
      } else {
         super.drawDrawableBg(g, getX(), getY(), secondSize, getDh());
         super.drawDrawableBg(g, getX() + getDw() - secondSize, getY(), secondSize, getDh());
      }
      topLeftFigure.draw(g);
      botRightFigure.draw(g);
   }

   /**
    * Returns {@link IBOScrollBar#SB_OFFSET_04_SECOND_SIZE4} decoded.
    * <br>
    * <br>
    * <b>Block</b>
    * Primary size of a block bar
    * <li> width for vertical bar
    * <li> height for horizontal bar
    * <br>
    * <br>
    * <b>Wrapper</b> Value is ignored if Fill Wrapper:
    * <li>width for vertical wrapper bar. 
    * <li>height for horiztonal wrapper bar.
    * <br>
    * @return
    */
   public int get1stSize() {
      int size = techParam.getValue(SB_OFFSET_03_PRIMARY_SIZE4, 4);
      size = implicitDecode(size);
      return size;
   }

   /**
    * Returns {@link IBOScrollBar#SB_OFFSET_04_SECOND_SIZE4} decoded.
    * <br>
    * <br>
    * <b>Block</b>: only used when {@link IBOScrollBar#SB_FLAG_5_ARROW_ON_BLOC}. Button.
    * <li> height of arrows for a vertical bar
    * <li> width of arrows for horizontal bar
    * <br>
    * <br>
    * <b>Wrapper</b> 
    * <li>height of each wrappers a vertical wrapper. 
    * <li>width of each wrapper for an horiztonal wrapper.
    * <br>
    * @return
    */
   public int get2ndSize() {
      int size = techParam.getValue(SB_OFFSET_04_SECOND_SIZE4, 4);
      LayoutOperator layoutOperator = gc.getDC().getLAC().getLayoutOperator();
      if (hasTechFlag(SB_FLAG_1_VERT)) {
         size = layoutOperator.codedSizeDecodeH(size, this);
      } else {
         size = layoutOperator.codedSizeDecodeW(size, this);
      }
      return size;
   }

   /**
    * 3rd size is the size of the background artifact.
    * <br>
    * Cannot be bigger than 1st size.
    * @return
    */
   public int get3rdSize() {
      int size = techParam.getValue(SB_OFFSET_05_THIRD_SIZE4, 4);
      size = implicitDecode(size);
      return size;
   }

   public ScrollConfig getConfig() {
      return scrollConfig;
   }

   /**
    * Styled
    * @return
    */
   public int getLRWrapperCH() {
      return getLRWrapperH() - getStyleHConsumed();
   }

   /**
    * Content width of Left and Right wrapper
    * @return
    */
   public int getLRWrapperCW() {
      return getLRWrapperW() - getStyleWConsumed();
   }

   /**
    * Left/Right wrapper height. <br>
    * 
    * @return
    */
   public int getLRWrapperH() {
      if (hasTechFlag(SB_FLAG_3_WRAPPER) && hasTechFlag(SB_FLAG_4_WRAPPER_FILL)) {
         //filling the whole space
         return boundaryBoxH;
      } else {
         return getMainSize();
      }
   }

   /**
    * Left and Right wrapper width is defined by the Tech param {@link IBOScrollBar#SB_OFFSET_04_SECOND_SIZE4}.
    * @return
    */
   public int getLRWrapperW() {
      return get2ndSize();
   }

   /**
    * Preferred size in the dimension that has a meaning of size.
    * Eating/Expand size <br>
    * Either first or 3rd size
    * For Block Bar, the main size is number of pixels consumed by figures + scrollbar style <br>
    * For Wrapper, heigth of triangle <br>
    * 
    * @return
    */
   public int getMainSize() {
      int size1 = get1stSize();
      int size3 = get3rdSize();
      int size = Math.max(size1, size3);
      if (hasTechFlag(SB_FLAG_1_VERT)) {
         size += getStyleWConsumed();
      } else {
         size += getStyleHConsumed();
      }
      return size;
   }

   /**
    * Pixels starting from YShift that are consumed by the scrollbar
    * going horizontally. <br>
    * In a {@link ViewPane} context, this value computes the viewport's width
    * @return
    */
   public int getSbHorizSpaceConsumed() {
      if (hasBehavior(ITechDrawable.BEHAVIOR_16_IMMATERIAL)) {
         return 0;
      }
      if (hasTechFlag(SB_FLAG_3_WRAPPER)) {
         if (!hasTechFlag(SB_FLAG_1_VERT)) {
            return 2 * getLRWrapperW();
         }
      } else {
         //block type of scrollbar. then vertical scrollbar will consume
         if (hasTechFlag(SB_FLAG_1_VERT)) {
            return getMainSize();
         }
      }
      return 0;
   }

   /**
    * This value will compute the viewport's height. 
    * <br>
    * Space on the Y axis consumed on the ViewPort.
    * For Bar, this returns the height of the horizontal scrollbar.
    * @return
    */
   public int getSbVertSpaceConsumed() {
      if (hasBehavior(ITechDrawable.BEHAVIOR_16_IMMATERIAL)) {
         return 0;
      }
      if (hasTechFlag(SB_FLAG_3_WRAPPER)) {
         if (hasTechFlag(SB_FLAG_1_VERT)) {
            return 2 * get2ndSize();
         }
      } else {
         //block type of scrollbar. then vertical scrollbar will consume
         if (!hasTechFlag(SB_FLAG_1_VERT)) {
            return getMainSize();
         }
      }
      return 0;
   }

   public int getTBWrapperCH() {
      return getTBWrapperH() - getStyleHConsumed();
   }

   public int getTBWrapperCW() {
      return getTBWrapperW() - getStyleWConsumed();
   }

   /**
    * Containes border
    * @return
    */
   public int getTBWrapperH() {
      return get2ndSize();
   }

   /**
    * The pixel width of the wrapper scrollbar
    * Overlay border pixel size is not computed in
    * Reads the ByteObject for data if non null
    * @return
    */
   public int getTBWrapperW() {
      if (hasTechFlag(SB_FLAG_3_WRAPPER) && hasTechFlag(SB_FLAG_4_WRAPPER_FILL))
         return boundaryBoxW;
      else
         return getMainSize();
   }

   /**
    * Width consumed at the left of the box by the scrollbar. <br>
    * For inverse vertical, method will return the width of the scrollbar
    * For horizontal wrapper scrollbar, it will return the thickness of the wrapper
    * @return
    */
   public int getXShiftLeft() {
      if (hasTechFlag(SB_FLAG_3_WRAPPER)) {
         if (!hasTechFlag(SB_FLAG_1_VERT)) {
            return getLRWrapperW();
         }
      } else {
         //block
         if (hasTechFlag(SB_FLAG_1_VERT) && hasTechFlag(SB_FLAG_6_BLOCK_INVERSE)) {
            return getMainSize();
         }
      }
      return 0;
   }

   public int getXShiftRight() {
      if (hasTechFlag(SB_FLAG_3_WRAPPER)) {
         if (!hasTechFlag(SB_FLAG_1_VERT)) {
            return getLRWrapperW();
         }
      } else {
         //block
         if (hasTechFlag(SB_FLAG_1_VERT)) {
            return getMainSize();
         }
      }
      return 0;
   }

   public int getYShiftBot() {
      if (hasTechFlag(SB_FLAG_3_WRAPPER)) {
         if (hasTechFlag(SB_FLAG_1_VERT)) {
            return getTBWrapperH();
         }
      } else {
         //block
         if (!hasTechFlag(SB_FLAG_1_VERT)) {
            return getMainSize();
         }
      }
      return 0;
   }

   /**
    * Height pixels consumed at the top of the box.
    * <br>
    * Usually vertical wrappers will return a value > 0
    * <br>
    * @return
    */
   public int getYShiftTop() {
      if (hasTechFlag(SB_FLAG_3_WRAPPER)) {
         if (hasTechFlag(SB_FLAG_1_VERT)) {
            return getTBWrapperH();
         }
      } else {
         //block
         if (!hasTechFlag(SB_FLAG_1_VERT) && hasTechFlag(SB_FLAG_6_BLOCK_INVERSE)) {
            return getMainSize();
         }
      }
      return 0;
   }

   public boolean hasTechFlag(int flag) {
      return techParam.hasFlag(SB_OFFSET_01_FLAG, flag);
   }

   public boolean hasTechFlagX(int flag) {
      return techParam.hasFlag(SB_OFFSET_02_FLAGX, flag);
   }

   /**
    * Uses pane to compute percentage size.
    * @param size
    * @return
    */
   private int implicitDecode(int size) {
      LayoutOperator layoutOperator = gc.getDC().getLAC().getLayoutOperator();
      if (hasTechFlag(SB_FLAG_1_VERT)) {
         size = layoutOperator.codedSizeDecodeW(size, this);
      } else {
         size = layoutOperator.codedSizeDecodeH(size, this);
      }
      return size;
   }

   /**
    * Initialize the {@link ScrollBar} dimension using the boundary box system.  
    * <br>
    * <br>
    * After calling this method, those methods returns a correct value
    * <li> {@link ScrollBar#getSbHorizSpaceConsumed()}
    * <li> {@link ScrollBar#getSbVertSpaceConsumed()}
    * <br>
    * <br>
    * Dw and Dh are set for animation purpose. 
    * 
    * Fit the smallest rectangle possible to include all Scrollbar items.
    * 
    * <br>
    * In wrapper mode {@link IBOScrollBar#SB_FLAG_3_WRAPPER}, {@link IDrawable#getHoles()} return the middle hole
    * not used by the {@link ScrollBar}.
    * <br>
    * <br>
    * 
    * @param width boundary box width. Must be positive.
    * @param height boundary box height. Must be positive.
    */
   public void init(int width, int height) {

      if (width <= 0 || height <= 0) {
         //throw new IllegalArgumentException("width=" + width + " height=" + height);
         //simply don't create scrollbars
         return;
      }
      if (!hasState(ITechDrawable.STATE_06_STYLED)) {
         styleValidate();
      }
      boundaryBoxW = width;
      boundaryBoxH = height;
      sizerCtx.w = width;
      sizerCtx.h = height;
      //compute smallest rectangle size based on boundary box size and style/tech data.
      //step used to resize the ViewPort in a ViewPane context
      if (hasTechFlag(SB_FLAG_3_WRAPPER)) {
         setStateFlag(ITechDrawable.STATE_24_HOLED, true);
         holes = new int[4];
         if (hasTechFlag(SB_FLAG_4_WRAPPER_FILL)) {
            setDw(width);
            setDh(height);
         } else {
            if (hasTechFlag(SB_FLAG_1_VERT)) {
               this.setDw(get2ndSize());
               this.setDh(height);
            } else {
               this.setDw(width);
               this.setDh(get2ndSize());
            }
         }

      } else {
         //
         int size = getMainSize();
         //the usual scrollbar
         if (hasTechFlag(SB_FLAG_1_VERT)) {
            this.setDh(height);
            this.setDw(size);
         } else {
            this.setDw(width);
            this.setDh(size);
         }
      }
      //now init dimension of scrollbar artifacts
      if (hasTechFlag(SB_FLAG_3_WRAPPER)) {
         initSizesWrapper();
         int s2ndSize = get2ndSize();
         if (hasTechFlag(SB_FLAG_1_VERT)) {
            holes[ITechDrawable.HOLE_0_X] = getX();
            holes[ITechDrawable.HOLE_1_Y] = getY() + s2ndSize;
            holes[ITechDrawable.HOLE_2_W] = getTBWrapperW();
            holes[ITechDrawable.HOLE_3_H] = boundaryBoxH - (2 * s2ndSize);
         } else {
            holes[ITechDrawable.HOLE_0_X] = getX() + s2ndSize;
            holes[ITechDrawable.HOLE_1_Y] = getY();
            holes[ITechDrawable.HOLE_2_W] = boundaryBoxW - (2 * s2ndSize);
            holes[ITechDrawable.HOLE_3_H] = getLRWrapperH();
         }

      } else {
         if (hasTechFlag(SB_FLAG_1_VERT)) {
            numPixelsForBlock = boundaryBoxH - super.getStyleHConsumed();
         } else {
            numPixelsForBlock = boundaryBoxW - super.getStyleWConsumed();
         }
         initSizesBlock();
      }
      setStateFlag(ITechDrawable.STATE_05_LAYOUTED, true);
   }

   private void initBlock(int x, int y, int w, int h) {
      blockFigure.init(w, h);
      blockFigure.setXY(x, y);
   }

   /**
    * Based on boundary box x,y
    */
   private void initPositionBlock() {
      //System.out.println("Scrollbar#initPositionBlock bbox=" + boundaryBoxX + "," + boundaryBoxY + " - " + boundaryBoxW + "," + boundaryBoxH);
      int mx = 0;
      int my = 0;
      if (hasTechFlag(SB_FLAG_6_BLOCK_INVERSE)) {
         mx = boundaryBoxX;
         my = boundaryBoxY;
      } else {
         if (hasTechFlag(SB_FLAG_1_VERT)) {
            mx = boundaryBoxX + boundaryBoxW - getMainSize();
            my = boundaryBoxY;
         } else {
            mx = boundaryBoxX;
            my = boundaryBoxY + boundaryBoxH - getMainSize();
         }
      }
      setMyXY(mx, my);
      int cx = this.getX() + getStyleWLeftConsumed();
      int cy = this.getY() + getStyleHTopConsumed();
      //block bar
      if (hasTechFlag(SB_FLAG_5_ARROW_ON_BLOC)) {
         topLeftFigure.setXY(cx, cy);
         if (hasTechFlag(SB_FLAG_1_VERT)) {
            cy += getTBWrapperCH();
         } else {
            cx += getLRWrapperCW();
         }
      }

      //init the block bg figure
      if (blockBgFigure != null) {
         int msize = get1stSize();
         int size3 = get3rdSize();
         int dx = cx;
         int dy = cy;
         //if background is bigger, it main size
         if (size3 < msize) {
            ByteObject blockFigureStyle = blockBgFigure.getStyle();
            ByteObject anchor = getStyleOp().getStyleAnchor(blockFigureStyle);
            if (hasTechFlag(SB_FLAG_1_VERT)) {
               dx = AnchorUtils.getXAlign(anchor, dx, msize, size3);
            } else {
               dy = AnchorUtils.getYAlign(anchor, dy, msize, size3);
            }
         }
         blockBgFigure.setXY(dx, dy);

      }
      if (hasTechFlag(SB_FLAG_5_ARROW_ON_BLOC)) {
         if (hasTechFlag(SB_FLAG_1_VERT)) {
            cy += numPixelsForBlock;
         } else {
            cx += numPixelsForBlock;
         }
         botRightFigure.setXY(cx, cy);
      }
      //positioning of the figureBar
      updateStructure();
   }

   /**
    * Based on boundary box x,y
    */
   private void initPositionWrapper() {
      if (hasTechFlag(SB_FLAG_4_WRAPPER_FILL)) {
         setMyXY(boundaryBoxX, boundaryBoxY);
      } else {
         //default behavior is center anchorage
         centerWrapper(boundaryBoxX, boundaryBoxY, boundaryBoxW, boundaryBoxH);
      }
      int cx = getX() + getStyleWLeftConsumed();
      int cy = getY() + getStyleHTopConsumed();
      topLeftFigure.setXY(cx, cy);
      if (hasTechFlag(SB_FLAG_1_VERT)) {
         cy = getY() + boundaryBoxH - getTBWrapperH();
      } else {
         cx = getX() + boundaryBoxW - getLRWrapperW();
      }
      botRightFigure.setXY(cx, cy);
   }

   private void initScrollbar(ByteObject tech) {

      tech.checkType(IBOTypesGui.TYPE_102_SCROLLBAR_TECH);
      
      setTech(tech); //method checks for null.
      if (hasTechFlag(SB_FLAG_1_VERT)) {
         setBehaviorFlag(ITechDrawable.BEHAVIOR_26_NAV_VERTICAL, true);
      } else {
         setBehaviorFlag(ITechDrawable.BEHAVIOR_27_NAV_HORIZONTAL, true);
      }
      scrollConfig = new ScrollConfig(gc);
      techUpdateScrollBar(tech);
      styleValidate();
      if (style == null) {
         throw new NullPointerException("Null Style");
      }
      //debug
      setDebugName("Scrollbar " + (hasTechFlag(SB_FLAG_1_VERT) ? "Vertical" : "Horizontal"));
   }

   /**
    * 
    */
   private void initSizesBlock() {
      if (hasTechFlag(SB_FLAG_5_ARROW_ON_BLOC)) {
         initSizesWrapper();
         if (hasTechFlag(SB_FLAG_1_VERT)) {
            int tbH = getTBWrapperCH();
            numPixelsForBlock -= (2 * tbH);
         } else {
            int lrW = getLRWrapperCW();
            numPixelsForBlock -= (2 * lrW);
         }
      }

      //init the block bg figure
      if (blockBgFigure != null) {
         int size3 = get3rdSize();
         if (hasTechFlag(SB_FLAG_1_VERT)) {
            //size -= MStyle.getStyleWConsumed(styleKey);
            //init the background drawable
            blockBgFigure.init(size3, numPixelsForBlock);
            //position bg figure relative to content width and content height
         } else {
            //size -= MStyle.getStyleHConsumed(styleKey);
            blockBgFigure.init(numPixelsForBlock, size3);
         }
      }
   }

   /**
    * Init the size of wrapper elements
    * <br>
    * <br>
    * The hole definition is finalized here
    */
   private void initSizesWrapper() {
      if (hasTechFlag(SB_FLAG_1_VERT)) {
         int tbCH = getTBWrapperCH();
         int tbCW = getTBWrapperCW();
         topLeftFigure.init(tbCW, tbCH);
         botRightFigure.init(tbCW, tbCH);
      } else {
         int lrCW = getLRWrapperCW();
         int lrCH = getLRWrapperCH();
         topLeftFigure.init(lrCW, lrCH);
         botRightFigure.init(lrCW, lrCH);
      }

      //TODO can you cache them?
   }

   /**
    * Called by {@link Controller} in stand alone mode.
    * <br>
    * Within a {@link ViewPane} context, called by {@link ViewPane#manageKeyInput(InputConfig)} 
    */
   public void manageKeyInput(InputConfig ic) {
      //System.out.println("ScrollBar Key");
      if (hasTechFlag(SB_FLAG_1_VERT) && ic.isUpActive()) {
         navigateUp(ic);
      }
      if ((!hasTechFlag(SB_FLAG_1_VERT) && ic.isLeftActive())) {
         navigateLeft(ic);
      }
      if (hasTechFlag(SB_FLAG_1_VERT) && ic.isDownActive()) {
         navigateDown(ic);
      }
      if (!hasTechFlag(SB_FLAG_1_VERT) && ic.isRightActive()) {
         navigateRight(ic);
      }
      //
      if (ic.isUp() && ic.isReleased()) {
         navigateUp(ic);
      }
      if (ic.isDown() && ic.isReleased()) {
         navigateDown(ic);
      }
   }

   public void commandAction(CmdInstanceDrawable cd) {
      if (cd.getCmdID() == ICmdsCmd.CMD_11_NAV_UP) {
         navigateUp(cd.getIC());
      } else {
         super.commandAction(cd);
      }
   }

   /**
    * Scrollbar gets hooked from {@link ViewPane}. Upon returning, if action is done,
    * it will repaint the
    * <li>When Pressed on a Scrollbar area, ViewPane lock
    * <li>When dragged, the viewpane updates the scrolling config accordingly
    * <li>When released, ViewPane unlock
    * <br>
    * On some drawables, this behavior only works when first Pressed on Scrollbar.<br>
    * <br>
    * Mainly because pressing inside the viewport will select sub items. This is checked
    * with instance of Nav or if Behavior has been set explicitely.
    * <br>
    * <li>Inside Top Figure
    * <li>Inside Bottom Figure
    * <li>Inside Block Figure
    * <li>Outside Block Figure - 
    * 	<li>North/West of Block
    * 	<li>South/East of Block
    */
   public void managePointerInput(InputConfig ic) {
      if (ic.isWheeled()) {
         if (ic.getIdKeyBut() == ITechCodes.PBUTTON_3_WHEEL_UP) {
            navigateGeneric(ic, 0, true, true);
         } else {
            navigateGeneric(ic, 0, false, true);
         }
         return;
      }

      //top down dispatch: arrives here when no slave and when there is no pointer moves
      //straight event because dispatch to Drawable
      if (DrawableUtilz.isInside(ic, topLeftFigure)) {
         topLeftFigure.managePointerInput(ic);
      } else if (DrawableUtilz.isInside(ic, botRightFigure)) {
         botRightFigure.managePointerInput(ic);
      } else if (DrawableUtilz.isInside(ic, blockFigure)) {
         blockFigure.managePointerInput(ic);
      } else if (DrawableUtilz.isInside(ic, blockBgFigure)) {
         blockBgFigure.managePointerInput(ic);
      } else {
         //inside scrollbar per se... nothing to do
         gc.getFocusCtrl().newFocusPointerPress(ic, this);
         //we need to inform ViewPane that this is it.
         ic.srActionDoneRepaint(this);
      }

   }

   protected void manageBlockBgPointerEvent(InputConfig ic) {
      if (ic.isPressed()) {
         boolean upleft = false;
         if (hasTechFlag(SB_FLAG_1_VERT)) {
            if (ic.is.getY() < blockFigure.getY()) {
               upleft = true;
            }
            navigateGeneric(ic, scrollConfig.getSIVisible(), upleft, true);
         } else {
            if (ic.is.getX() < blockFigure.getX()) {
               upleft = true;
            }
            navigateGeneric(ic, scrollConfig.getSIVisible(), upleft, true);
         }
      } else if (ic.isReleased()) {
         pointerReleasedDragged(ic);
      }
   }

   /**
    * Updates the state of scrollbar buttons for a default navigation down
    * Smooth moves with the scrollbars. Handles dragging and move animation
    * Move animation
    */
   public void navigateDown(InputConfig ic) {
      if (!hasTechFlag(SB_FLAG_1_VERT)) {
         return;
      }
      navigateGeneric(ic, 0, false, false);

   }

   /**
    * For releases events, repaint components whose state is changed.
    * <br>
    * <br>
    * Method called 
    * @param ic
    * @param vchange change for start increment. when 0
    * @param upleft
    */
   public void navigateGeneric(InputConfig ic, int vchange, boolean upleft, boolean isPointer) {
      if (ic.isPressed()) {
         navigateGenericPressed(ic, vchange, upleft, isPointer);
      } else if (ic.isDraggedPointer0Button0()) {
         //
         navigateGenericDragged(ic, vchange, upleft);
      } else if (ic.isReleased()) {
         setSelectedStateGeneric(upleft, false);
         ic.srActionDoneRepaint(this);
      } else if (ic.isWheeled()) {
         navigateGenericWheeled(ic, vchange, upleft);
      }
   }

   public void navigateGenericDragged(InputConfig ic, int vchange, boolean upleft) {
      if (scrollConfig.getSIStart() != vchange) {
         setSelectedStateGeneric(upleft, true);
         scrollConfig.setSIStartNoEx(vchange);
         updateStructure();
      }
      ic.srActionDoneRepaint();
   }

   public void navigateGenericWheeled(InputConfig ic, int vchange, boolean upleft) {
      if (vchange == 0) {
         vchange = 1 + 0;
      }
      if (upleft) {
         scrollConfig.moveDecrease(vchange);
      } else {
         scrollConfig.moveIncrease(vchange);
      }
      if (scrollConfig.getSILastChange() != 0) {
         updateStructure();
         repaintScrollbarAndContent(ic);
         animCall(ic, upleft);
      }
   }

   /**
    * 
    * @param ic
    * @param vchange
    * @param upleft
    * @param isPointer
    */
   public void navigateGenericPressed(InputConfig ic, int vchange, boolean upleft, boolean isPointer) {
      //we want pointer/ and if key to repeat sending event until there is a release
      ic.requestRepetition();

      if (vchange == 0) {
         //at least one in change
         vchange = 1 + 0;
         //with a maximum of ?
         if (vchange > scrollConfig.getMaximumAmplitude()) {
            vchange = scrollConfig.getMaximumAmplitude();
         }
      }
      if (upleft) {
         scrollConfig.moveDecrease(vchange);
      } else {
         scrollConfig.moveIncrease(vchange);
      }
      if (scrollConfig.getSILastChange() != 0) {
         setSelectedStateGeneric(upleft, true);

         updateStructure();

         repaintScrollbarAndContent(ic);
         animCall(ic, upleft);
      }
   }

   /**
    * The amount of Left Navigation
    */
   public void navigateLeft(InputConfig ic) {
      if (hasTechFlag(SB_FLAG_1_VERT)) {
         return;
      }
      navigateGeneric(ic, 0, true, false);
   }

   public void navigateRight(InputConfig ic) {
      if (hasTechFlag(SB_FLAG_1_VERT)) {
         return;
      }
      navigateGeneric(ic, 0, false, false);
   }

   public void navigateSelect(InputConfig ic) {
      //nothing to select here
   }

   /**
    * 
    */
   public void navigateUp(InputConfig ic) {
      if (!hasTechFlag(SB_FLAG_1_VERT)) {
         return;
      }
      navigateGeneric(ic, 0, true, false);
   }

   /**
    * Since the Drag was first registered. 
    * <br>
    * <br>
    * Compute natural position of startIncrement based on Pointer Position.
    * <br>
    * <br>
    * We want the x/y coordinate of scfg to move in the same amplitude as the vector.
    * <br>
    * For each start increment above 0, dy is = incrSize * start
    * So if we drag incrSize, we move one increment
    * <br>
    * <br>
    *  Translates the dragging amplitude in pixel, to a dragging amplitude in start scroll config increments.
    * <br>
    * For that we need to know the value of 1 pixel in terms of config increment. Usually 1 pixel will be 2 or more increments.
    * <br>
    * <br>
    * 
    * @param ic
    */
   private void pointerDraggingBlock(InputConfig ic) {
      //pixel different since the last pressed event
      int pixelMove = 0;
      if (hasTechFlag(SB_FLAG_1_VERT)) {
         pixelMove = ic.getDraggedVectorY();
      } else {
         pixelMove = ic.getDraggedVectorX();
      }

      int startIncr = ic.is.getOrCreateGesture(blockFigure).getPressed(PointerGestureDrawable.ID_0_X);
      //translate the pixel into increment units.
      //int startIncr = Controller.getMe().getDraggedPressedX();

      //we compute the pixel move, thus the ratio and we set the scrolling config start increment

      //normalize all pixel values to increment pixel values
      //the more this value is big, the more pixelMove need to be big to make a move
      //get the si

      //the variable value depending on the mouse position.

      int incrementMove = 0;
      if (scrollConfig.getTypeUnit() == ITechViewPane.SCROLL_TYPE_1_LOGIC_UNIT) {
         //TODO should relate to the pointer positon on bgBlock so it makes sense visually
         //in case of 2 increments, as soon as pointer goes out of figure, it scrolls
         float onecol = (float) numPixelsForBlock / (float) scrollConfig.getSITotal();
         // pixels * number of increment / number of pixel
         if (pixelMove < 0) {
            pixelMove--;
         } else {
            pixelMove++;
         }
         int incrementMove2 = (int) (((float) (pixelMove)) / onecol);
         incrementMove = incrementMove2;
      } else {
         int X = scrollConfig.getSITotal();
         //we have X increments for Y visible pixels =  1 pixel for X/Y increments
         double ratio = (double) X / (double) numPixelsForBlock;
         incrementMove = (int) (((double) pixelMove) * ratio);
      }

      int newStart = startIncr + incrementMove;

      boolean topLeft = true;
      if (pixelMove > 0) {
         topLeft = false;
      }
      //compute the number 

      //#debug
      String msg = "StartIncr=" + startIncr + " vdiff=" + pixelMove + " StartVal=" + startIncr + " NewStartSi=" + newStart + " incrementMove=" + incrementMove;
      //#debug
      toDLog().pState(msg, this, ScrollBar.class, "pointerDraggingBlock", LVL_05_FINE, true);
      
      if (incrementMove != 0) {
         scrollConfig.setSIStartNoEx(newStart);
         setSelectedStateGeneric(topLeft, true);
         updateStructure();
      }
      //repaint ViewDrawable content and this scrollbar
      repaintScrollbarAndContent(ic);

      //generates call drag method on View
      pane.getViewed().manageDragCallBack(this);
      if (trailer != null) {
         if (trailer.hasState(ITechDrawable.STATE_03_HIDDEN)) {
            trailer.init(0, 0);
            vc.getDrawCtrlAppli().shShowDrawable(ic, trailer, ITechCanvasDrawable.SHOW_TYPE_1_OVER);
         }
         trailer.setXY(blockFigure.getX() - trailer.getDrawnWidth(), blockFigure.getY());
      }
   }

   public void setTrailer(IDrawable trailer) {
      this.trailer = trailer;
   }

   /**
    * Called when a release pointer event is generated after a dragged of block scrollbar.
    * @param ic
    */
   private void pointerReleasedDragged(InputConfig ic) {
      //System.out.println("#Scrollbar pointerReleasedDragged after Dragging");
      //deselects all drawables that may have been selected
      unselectTopBot(ic);
      if (blockFigure != null) {
         blockFigure.setStateStyle(ITechDrawable.STYLE_05_SELECTED, false);
      }
      if (trailer != null) {
         vc.getDrawCtrlAppli().removeDrawable(null, trailer, null);
      }
      ic.srActionDoneRepaint(this);
   }

   protected void unselectTopBot(InputConfig ic) {
      if (topLeftFigure != null && topLeftFigure.hasStateStyle(ITechDrawable.STYLE_05_SELECTED)) {
         topLeftFigure.setStateStyle(ITechDrawable.STYLE_05_SELECTED, false);
         ic.srActionDoneRepaint(topLeftFigure);
      }
      if (botRightFigure != null && botRightFigure.hasStateStyle(ITechDrawable.STYLE_05_SELECTED)) {
         botRightFigure.setStateStyle(ITechDrawable.STYLE_05_SELECTED, false);
         ic.srActionDoneRepaint(botRightFigure);
      }
   }

   /**
    * Positions wrappers and arrows on block.
    * @param x
    * @param y
    * @param w
    * @param h
    */
   private void positionWrapper(int x, int y) {
      int cx = x + getStyleWLeftConsumed();
      int cy = y + getStyleHTopConsumed();
      topLeftFigure.setXY(cx, cy);
      if (hasTechFlag(SB_FLAG_1_VERT)) {
         cy += boundaryBoxH - getTBWrapperH();
      } else {
         cx += boundaryBoxW - getLRWrapperW();
      }
      botRightFigure.setXY(cx, cy);
   }

   public void repaintScrollbarAndContent(InputConfig ic) {
      //scroll action done on ScrollBar. we want to repaint the scrollbar and ViewDrawable's content.
      ic.srActionDoneRepaint(this);
      if (pane != null) {
         ViewDrawable vd = pane.getViewed();
         //ask to repaint only the content of the viewpane
         vd.setViewFlag(ITechViewDrawable.VIEWSTATE_02_REPAINTING_CONTENT, true);
         ic.srActionDoneRepaint(vd);
      }
   }

   public void setBotRightSelected(boolean v) {
      if (botRightFigure != null)
         botRightFigure.setStateStyle(ITechDrawable.STYLE_05_SELECTED, v);
      if (blockFigure != null) {
         blockFigure.setStateStyle(ITechDrawable.STYLE_05_SELECTED, v);
      }
   }

   /**
    * Sets the x,y coordinate of the {@link ScrollBar}.
    * @param x
    * @param y
    */
   private void setMyXY(int x, int y) {
      //System.out.println("Scrollbar#setMyXY " + x + "," + y);
      this.setX(x);
      this.setY(y);
   }

   /**
    * 
    * @param upleft
    * @param v
    */
   private void setSelectedStateGeneric(boolean upleft, boolean v) {
      if (upleft) {
         setTopLeftSelected(v);
      } else {
         setBotRightSelected(v);
      }
   }

   /**
    * 
    */
   public void setStateFlag(int flag, boolean value) {
      if (flag == ITechDrawable.STATE_03_HIDDEN || flag == ITechDrawable.STATE_02_DRAWN) {
         //System.out.println("Scrollbar " + Drawable.debugState(flag) + " = " + value);
         if (blockBgFigure != null) {
            blockBgFigure.setStateFlag(flag, value);
         }
         if (blockFigure != null) {
            blockFigure.setStateFlag(flag, value);
         }
         if (topLeftFigure != null) {
            topLeftFigure.setStateFlag(flag, value);
         }
         if (botRightFigure != null) {
            botRightFigure.setStateFlag(flag, value);
         }
      }
      super.setStateFlag(flag, value);
   }

   /**
    * Create the {@link Drawable} of the {@link ScrollBar}.
    * <br>
    * <br>
    * 
    * @param tech
    */
   public void setTech(ByteObject tech) {
      if (tech == null)
         throw new NullPointerException("No Tech Param defined for Scrollbar");
      techParam = tech;
      //based on the tech design, fetch the style of the items of the scrollbar
      if (techParam.hasFlag(SB_OFFSET_01_FLAG, SB_FLAG_3_WRAPPER)) {
         //linked parameter of Tech has the StyleDef
         createWrapperDef();
      } else {
         StyleClass bkey = styleClass.getStyleClass(IBOTypesGui.LINK_51_STYLE_SCROLLBAR_BLOCK_FIG);
         if (bkey != null) {
            //there is at least a block
            blockFigure = new Drawable(gc, bkey, this);
            blockFigure.setBehaviorFlag(ITechDrawable.BEHAVIOR_23_PARENT_EVENT_CONTROL, true);
            blockFigure.setDebugName("BlockFigure");
            //check if there is a special background
            StyleClass key = styleClass.getStyleClass(IBOTypesGui.LINK_50_STYLE_SCROLLBAR_BLOCK_BG);
            blockBgFigure = new Drawable(gc, key, this);
            blockBgFigure.setBehaviorFlag(ITechDrawable.BEHAVIOR_23_PARENT_EVENT_CONTROL, true);
            blockBgFigure.setStateFlag(ITechDrawable.STATE_27_OVERLAYED, true);
            blockBgFigure.setDebugName("BlockBg");
         } else {
            //draw none. code breaks if a background and no block
         }

         if (techParam.hasFlag(SB_OFFSET_01_FLAG, SB_FLAG_5_ARROW_ON_BLOC)) {
            createWrapperDef();
         }
      }

   }

   /**
    * Changes the selected state for 
    * <li>topLeft button/wrapper figure 
    * <li>block figure
    * <br>
    * <br>
    * @param f true/false for {@link ITechDrawable#STYLE_05_SELECTED}
    */
   public void setTopLeftSelected(boolean f) {
      if (topLeftFigure != null) {
         topLeftFigure.setStateStyle(ITechDrawable.STYLE_05_SELECTED, f);
      }
      if (blockFigure != null) {
         blockFigure.setStateStyle(ITechDrawable.STYLE_05_SELECTED, f);
      }
   }

   /**
    * Sets the x,y coordinate of the boundary box. <br>
    * <br>
    * Computes positions of ScrollBar artifacts.
    */
   public void setXY(int x, int y) {
      boundaryBoxX = x;
      boundaryBoxY = y;
      if (hasTechFlag(SB_FLAG_3_WRAPPER)) {
         initPositionWrapper();
      } else {
         initPositionBlock();
      }
   }

   public void shiftXY(int x, int y) {
      //System.out.println("Scrollbar#shiftXY " + x + "," + y);
      boundaryBoxX += x;
      boundaryBoxY += y;
      this.setX(this.getX() + x);
      this.setY(this.getY() + y);
      //ViewPane.shiftXY(this, x, y);
      ViewPane.shiftXY(topLeftFigure, x, y);
      ViewPane.shiftXY(botRightFigure, x, y);
      ViewPane.shiftXY(blockFigure, x, y);
      ViewPane.shiftXY(blockBgFigure, x, y);
      ViewPane.shiftXY(dConfigInfo, x, y);
      //shift the holes if any
      if (holes != null) {
         holes[ITechDrawable.HOLE_0_X] += x;
         holes[ITechDrawable.HOLE_1_Y] += y;
      }
      //when shifting we must update all x,y dependant figures
      updateStructure();
   }

   /**
    * No unit or visual types for a standalone {@link ScrollBar}.
    * @param tech
    */
   public void techUpdateScrollBar(ByteObject tech) {
      scrollConfig.scrollMoveType = tech.get1(SB_OFFSET_07_SCROLL_TYPES1);
   }

   /**
    * Update {@link ScrollConfig} types and behavior from {@link ViewPane}'s tech.
    * @param tech
    */
   public void techUpdateViewPane(ByteObject tech) {
      if (hasTechFlag(SB_FLAG_1_VERT)) {
         //vertical
         scrollConfig.scrollUnitType = tech.get2Bits4(IBOViewPane.VP_OFFSET_05_SCROLLBAR_MODE1);
         scrollConfig.scrollVisualType = tech.get4Bits2(IBOViewPane.VP_OFFSET_06_VISUAL_LEFT_OVER1);
         scrollConfig.scrollMoveType = tech.get2Bits3(IBOViewPane.VP_OFFSET_08_MOVE_TYPE1);
         scrollConfig.scrollPartialType = tech.get2Bits4(IBOViewPane.VP_OFFSET_08_MOVE_TYPE1);
      } else {
         //horizontal
         scrollConfig.scrollUnitType = tech.get2Bits3(IBOViewPane.VP_OFFSET_05_SCROLLBAR_MODE1);
         scrollConfig.scrollVisualType = tech.get4Bits1(IBOViewPane.VP_OFFSET_06_VISUAL_LEFT_OVER1);
         scrollConfig.scrollMoveType = tech.get2Bits1(IBOViewPane.VP_OFFSET_08_MOVE_TYPE1);
         scrollConfig.scrollPartialType = tech.get2Bits2(IBOViewPane.VP_OFFSET_08_MOVE_TYPE1);
      }
   }

   //#mdebug

   public void toString(Dctx sb) {
      String h = "Horizontal";
      if (hasTechFlag(SB_FLAG_1_VERT)) {
         h = "Vertical";
      }
      sb.root(this, "Scrollbar " + h);
      sb.append(" Boundary [" + boundaryBoxX + "," + boundaryBoxY + " " + boundaryBoxW + "," + boundaryBoxH + "]");
      super.toString(sb.sup());
      sb.nl();
      sb.append("HorizSpaceConsumed=" + getSbHorizSpaceConsumed() + " VertSpaceConsumed=" + getSbVertSpaceConsumed());
      sb.append(" xShift=" + getXShiftLeft());
      sb.append(" yShift=" + getYShiftTop());
      sb.nl();
      sb.append("TBWrapperW=" + getTBWrapperW());
      sb.append(" TBWrapperH=" + getTBWrapperH());
      sb.append(" LRWrapperW=" + getLRWrapperW());
      sb.append(" LRWrapperH=" + getLRWrapperH());

      sb.nlLvlIgnoreNull("TopLeft Drawable", topLeftFigure);
      sb.nlLvlIgnoreNull("BotRight Drawable", botRightFigure);
      sb.nlLvlIgnoreNull("Block Bg Drawable", blockBgFigure);
      sb.nlLvlIgnoreNull("Block Drawable", blockFigure);

      sb.nlLvl(scrollConfig);
   }

   public void toString1Line(Dctx dc) {
      String h = "Horizontal";
      if (hasTechFlag(SB_FLAG_1_VERT)) {
         h = "Vertical";
      }
      dc.root1Line(this, "Scrollbar " + h);
   }
   //#enddebug

   private void updateBlockXYWHHoriz() {
      int ww = numPixelsForBlock;
      //horizontal
      float onecol = (float) ww / (float) scrollConfig.getSITotal();
      int startx = (int) ((float) scrollConfig.getStartFirstFullyVisible() * onecol);
      if (hasTechFlag(SB_FLAG_5_ARROW_ON_BLOC)) {
         startx += getLRWrapperCW();
      }
      startx += getContentX() + blockBgFigure.getStyleWLeftConsumed();
      int endx = startx + (int) ((float) scrollConfig.getSIVisibleFully() * onecol);
      if (scrollConfig.isEnd()) {
         endx = blockBgFigure.getX() + ww - blockBgFigure.getStyleWRightConsumed();
      }
      barSize = endx - startx;
      //set minimum size
      if (barSize < blockMinPixels) {
         barSize = blockMinPixels;
      }
      initBlock(startx, getContentY(), barSize, get1stSize());
   }

   /**
    * Start y coordinate is:
    * <li> {@link ScrollConfig#getStartFirstFullyVisible()} * numberOfPixel / {@link ScrollConfig#getSITotal()}
    * <li>Top wrapper height is added
    * <li>Style Top consume of block bg figure is added
    * <li>Finally add {@link ScrollBar#getContentY()} to reach absolute postion
    */
   private void updateBLockXYWHVert() {
      int wh = numPixelsForBlock;
      //he is the size available
      float onerow = (float) wh / (float) scrollConfig.getSITotal();
      int starty = (int) ((float) scrollConfig.getStartFirstFullyVisible() * onerow);
      if (hasTechFlag(SB_FLAG_5_ARROW_ON_BLOC)) {
         starty += getTBWrapperCH();
      }
      starty += getContentY() + blockBgFigure.getStyleHTopConsumed();

      int endy = starty + (int) ((float) scrollConfig.getSIVisibleFully() * onerow);
      //statement so that the scrollbar "touches" bottom
      if (scrollConfig.isEnd()) {
         endy = blockBgFigure.getY() + wh - blockBgFigure.getStyleHBotConsumed();
      }
      barSize = endy - starty;
      //set minimum size
      if (barSize < blockMinPixels) {
         barSize = blockMinPixels;
      }
      initBlock(getContentX(), starty, get1stSize(), barSize);
   }

   /**
    * Method modifies Scrollbar artifact positions and sizes based on the {@link ScrollConfig}.
    * Method called when ScrollingConfig changes its structure. <br>
    * If there is no scroll configuration, the method returns. <br>
    * It will change the block drawing params
    */
   public void updateStructure() {

      if (scrollConfig == null) {
         //not initialized yet
         return;
      }
      scrollConfig.checkStart();

      if (topLeftFigure != null) {
         if (scrollConfig.getStartFirstFullyVisible() == 0 && scrollConfig.getTypeMove() == ITechViewPane.SB_MOVE_TYPE_0_FIXED) {
            topLeftFigure.setStateStyle(ITechDrawable.STYLE_04_GREYED, true);
         } else {
            topLeftFigure.setStateStyle(ITechDrawable.STYLE_04_GREYED, false);
         }
      }
      if (botRightFigure != null) {
         if (scrollConfig.isEnd() && scrollConfig.getTypeMove() == ITechViewPane.SB_MOVE_TYPE_0_FIXED) {
            botRightFigure.setStateStyle(ITechDrawable.STYLE_04_GREYED, true);
         } else {
            botRightFigure.setStateStyle(ITechDrawable.STYLE_04_GREYED, false);
         }
      }

      if (hasTechFlag(SB_FLAG_3_WRAPPER)) {
         //only change state for config who reached an end
         return;
      }

      if (blockFigure == null)
         return;

      if (blockBgFigure == null) {
         return;
      }

      //we work on number of pixels used by background figure
      if (hasTechFlag(SB_FLAG_1_VERT)) {
         updateBLockXYWHVert();
      } else {
         updateBlockXYWHHoriz();
      }
   }

   public void notifyEvent(IDrawable d, int event, Object o) {
      if (event == ITechDrawable.EVENT_14_POINTER_EVENT) {
         managePointerEvent(d, (InputConfig) o);
      }
   }

   public void managePointerEvent(IDrawable slave, InputConfig ic) {
      //either call back from slave
      //System.out.println("#Scrollbar#managePointerInput Slave " + slave.toStringOneLine());
      if (ic.isMoved()) {
         //when moving from and to bgfigure and block figure, we must do the swap here
         if (slave == blockBgFigure && DrawableUtilz.isInside(ic, blockFigure)) {
            blockFigure.managePointerStateStyle(ic);
         } else {
            blockBgFigure.managePointerStateStyle(ic);
         }
         //we have to set action done, otherwise ViewPane thinks nothing was done
         return;
      }
      //registered for presses (scrolling + state) and releases (state change)
      if (slave == topLeftFigure) {
         navigateGeneric(ic, 0, true, true);
      } else if (slave == botRightFigure) {
         navigateGeneric(ic, 0, false, true);
         botRightFigure.managePointerStateStyle(ic);
      } else if (slave == blockBgFigure) {
         manageBlockBgPointerEvent(ic);
         blockBgFigure.managePointerStateStyle(ic);
      } else if (slave == blockFigure) {
         //update the drag starter values
         if (ic.isPressed()) {
            //update the x
            GestureDetector pg = ic.is.getOrCreateGesture(blockFigure);
            pg.setReleasePointer(true, false);
            //starts a simple gesture with no values
            pg.simplePress(scrollConfig.getSIStart(), ic.is);
         } else if (ic.isReleased()) {
            GestureDetector pg = ic.is.getOrCreateGesture(blockFigure);
            //starts a simple gesture with no values
            //gesture depends on the ViewDrawable
            pg.simpleRelease(ic.is);
            pointerReleasedDragged(ic);
         } else if (ic.isDraggedPointer0Button0()) {
            GestureDetector pg = ic.is.getOrCreateGesture(blockFigure);
            //starts a simple gesture with no values
            pg.simpleDrag(ic.is);
            pointerDraggingBlock(ic);
         }
         blockFigure.managePointerStateStyle(ic);
      }
   }
}
