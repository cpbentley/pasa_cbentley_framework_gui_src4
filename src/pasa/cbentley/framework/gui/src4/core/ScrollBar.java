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
import pasa.cbentley.framework.drawx.src4.tech.ITechAnchor;
import pasa.cbentley.framework.drawx.src4.utils.AnchorUtils;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.canvas.PointerGestureDrawable;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceDrawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.ctx.IToStringFlagsDraw;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOScrollBar;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOViewPane;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawableListener;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechViewDrawable;
import pasa.cbentley.framework.gui.src4.tech.ITechLinks;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;
import pasa.cbentley.framework.gui.src4.utils.DrawableUtilz;
import pasa.cbentley.framework.input.src4.gesture.GestureDetector;
import pasa.cbentley.layouter.src4.engine.LayoutOperator;

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
 * In Wrapper style, the scrollbar is entirely defined by {@link ITechLinks#LINK_53_STYLE_SCROLLBAR_BOT_RIGHT_WRAPPER}
 * and {@link ITechLinks#LINK_52_STYLE_SCROLLBAR_TOP_LEFT_WRAPPER}
 * <br>
 * <br>
 * <b>Directional Figures</b> : <br> 
 * Stores model of Figure as a TechParam. using Fig_tech_Id
 * Create {@link FigDrawable} with Direction.
 * A model of Directional figure is given to the scrollbar. It automatically parametrize the creation of specific figures
 * for wrappers and block arrows.
 * {@link ByteObject#FIG_FLAGZ_8_DIRECTION}.
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

   /**
    * 
    */
   protected ByteObject   boScrollbar;

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
    * X coordinate of the boundary box. 
    */
   private int            boundaryBoxX;

   /**
    * Y coordinate of the boundary box. 
    */
   private int            boundaryBoxY;

   /**
    * Drawable that paint the scrolling configuration as text
    * In the case of scrolling over an unknown number
    */
   private IDrawable      dConfigInfo;

   /**
    * Number of pixels for drawing the moving bar and its background artifact.
    * <br>
    * <br>
    * It is computed as dh - HConsumed - ButtonsSize
    */
   private int            numPixelsForBlock;

   private ViewPane       pane;

   /**
    * Controls the state of the Scrollbar.
    * 
    * Never null
    */
   protected ScrollConfig scrollConfig;

   /**
    * Content width is computed from the root style of the scrollbar.
    * <br>
    * {@link Drawable} with stylekey from scrollbar
    */
   private Drawable       topLeftFigure;

   /**
    * Info drawable drawn next to the block figure that displays information about the state of the scrolling.
    * <br>
    * For example, when scrolling characters, a callback updates IDrawable as a StringDrawable with the top most
    * characters being displayed.
    * This drawable is displayed in a N+1 layer as the {@link ScrollBar}.
    */
   protected IDrawable    trailer;

   public ScrollBar(GuiCtx gc, StyleClass sc, boolean isHoriz) {
      this(gc, sc, isHoriz, null);
   }

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
      ByteObject tech = null;
      int techid = ITechLinks.LINK_68_BO_V_SCROLLBAR;
      if (horiz) {
         techid = ITechLinks.LINK_69_BO_H_SCROLLBAR;
      }
      //different Tech for horizontal and vertical
      tech = styleClass.getByteObjectNotNull(techid);
      a_InitScrollbar(tech);

   }

   /**
    * {@link StyleClass} with following links
    * <li> {@link ITechLinks#LINK_49_FIG_SCROLLBAR_WRAPPER}
    * <li> {@link ITechLinks#LINK_50_STYLE_SCROLLBAR_BLOCK_BG}
    * <li> {@link ITechLinks#LINK_51_STYLE_SCROLLBAR_BLOCK_FIG}
    * <li> {@link ITechLinks#LINK_52_STYLE_SCROLLBAR_TOP_LEFT_WRAPPER}
    * <li> {@link ITechLinks#LINK_53_STYLE_SCROLLBAR_BOT_RIGHT_WRAPPER}
    * 
    * <li> {@link ITechLinks#LINK_68_BO_V_SCROLLBAR}
    * <li> {@link ITechLinks#LINK_69_BO_H_SCROLLBAR}
    * @param gc
    * @param sc
    * @param tech
    */
   public ScrollBar(GuiCtx gc, StyleClass sc, ByteObject tech) {
      super(gc, sc);
      a_InitScrollbar(tech);
   }

   private void a_InitScrollbar(ByteObject boScrollbar) {
      boScrollbar.checkType(IBOTypesGui.TYPE_GUI_01_SCROLLBAR);
      setBOScrollbar(boScrollbar); //method checks for null.

      if (hasTechFlag(SB_FLAG_1_VERT)) {
         setBehaviorFlag(ITechDrawable.BEHAVIOR_26_NAV_VERTICAL, true);
      } else {
         setBehaviorFlag(ITechDrawable.BEHAVIOR_27_NAV_HORIZONTAL, true);
      }
      scrollConfig = new ScrollConfig(gc);
      scrollConfig.scrollMoveType = boScrollbar.get1(SB_OFFSET_07_SCROLL_TYPES1);
      styleValidate();
      if (style == null) {
         throw new NullPointerException("Null Style");
      }
      //debug
      toStringSetName("Scrollbar " + (hasTechFlag(SB_FLAG_1_VERT) ? "Vertical" : "Horizontal"));

      //#debug
      toDLog().pInit(toStringGetName() + " ", this, ScrollBar.class, "Created@268", LVL_05_FINE, true);
   }

   protected void actionUnselectTopBot(InputConfig ic) {
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
         int mx = x + (width - getWrapperTopBotWidthStyled()) / 2;
         setMyXY(mx, y);
      } else {
         int my = y + (height - getWrapperLeftRightHeightStyle()) / 2;
         setMyXY(x, my);
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
      StyleClass sc = styleClass.getStyleClass(ITechLinks.LINK_52_STYLE_SCROLLBAR_TOP_LEFT_WRAPPER);
      if (sc != null) {
         createWrapperTwo();
      } else {
         createWrapperDefSingle();
      }
   }

   /**
    * One style classes. different byte object figures.
    */
   private void createWrapperDefSingle() {
      DrwCtx dc = gc.getDC();
      //gets figure definition and take its direction type
      ByteObject fig = styleClass.getByteObject(ITechLinks.LINK_49_FIG_SCROLLBAR_WRAPPER);
      FigureFactory figFac = dc.getFigureFactory();
      GradientFactory gradFac = dc.getGradientFactory();
      if (fig == null) {
         int direction = 0;
         ByteObject wrapperFigGrad = gradFac.getGradient(ColorUtils.getRGBInt(128, 16, 16), 0, ITechGradient.GRADIENT_TYPE_TRIG_02_TOP_JESUS);
         fig = figFac.getFigTriangleAngle(IColors.FULLY_OPAQUE_BLACK, direction, 0, wrapperFigGrad);
      }
      FigureOperator figOp = dc.getFigureOperator();

      int dirTopLeft = (hasTechFlag(SB_FLAG_1_VERT) ? C.DIR_0_TOP : C.DIR_2_LEFT);
      ByteObject topLeftFig = figOp.cloneFigDirectionanl(fig, dirTopLeft);
      StyleClass scTopLeft = styleClass.getSCNotNull(ITechLinks.LINK_52_STYLE_SCROLLBAR_TOP_LEFT_WRAPPER);
      topLeftFigure = new FigDrawable(gc, scTopLeft, topLeftFig);
      topLeftFigure.setParent(this);
      topLeftFigure.setBehaviorFlag(ITechDrawable.BEHAVIOR_23_PARENT_EVENT_CONTROL, true);

      int dirBotRight = (hasTechFlag(SB_FLAG_1_VERT) ? C.DIR_1_BOTTOM : C.DIR_3_RIGHT);
      ByteObject botRightFig = figOp.cloneFigDirectionanl(fig, dirBotRight);

      StyleClass scBotRight = styleClass.getSCNotNull(ITechLinks.LINK_53_STYLE_SCROLLBAR_BOT_RIGHT_WRAPPER);
      botRightFigure = new FigDrawable(gc, scBotRight, botRightFig);
      botRightFigure.setParent(this);
      botRightFigure.setBehaviorFlag(ITechDrawable.BEHAVIOR_23_PARENT_EVENT_CONTROL, true);

      //debug
      topLeftFigure.toStringSetName("Fig" + (hasTechFlag(SB_FLAG_1_VERT) ? "Top" : "Left"));
      botRightFigure.toStringSetName("Fig" + (hasTechFlag(SB_FLAG_1_VERT) ? "Bottom" : "Right"));
   }

   private void createWrappers() {
      DrwCtx dc = gc.getDC();
      //gets figure definition and take its direction type
      StyleClass src = getStyleClassForChildren();
      ByteObject fig = src.getByteObject(ITechLinks.LINK_49_FIG_SCROLLBAR_WRAPPER);
      FigureFactory figFac = dc.getFigureFactory();
      GradientFactory gradFac = dc.getGradientFactory();
      ByteObject topLeftFig = null;
      ByteObject botRightFig = null;
      int dirTopLeft = (hasTechFlag(SB_FLAG_1_VERT) ? C.TYPE_00_TOP : C.TYPE_02_LEFT);
      int dirBotRight = (hasTechFlag(SB_FLAG_1_VERT) ? C.TYPE_01_BOTTOM : C.TYPE_03_RIGHT);
      if (fig == null) {
         ByteObject wrapperFigGrad = gradFac.getGradient(ColorUtils.getRGBInt(128, 16, 16), 0, ITechGradient.GRADIENT_TYPE_TRIG_02_TOP_JESUS);
         topLeftFig = figFac.getFigTriangleType(IColors.FULLY_OPAQUE_GREEN, dirTopLeft, wrapperFigGrad);
         botRightFig = figFac.getFigTriangleType(IColors.FULLY_OPAQUE_GREEN, dirBotRight, wrapperFigGrad);
      } else {
         FigureOperator figOp = dc.getFigureOperator();
         topLeftFig = figOp.cloneFigDirectionanl(fig, dirTopLeft);
         botRightFig = figOp.cloneFigDirectionanl(fig, dirBotRight);
      }

      StyleClass scLeft = src.getStyleClass(ITechLinks.LINK_52_STYLE_SCROLLBAR_TOP_LEFT_WRAPPER);
      topLeftFigure = new FigDrawable(gc, scLeft, topLeftFig);
      topLeftFigure.setParent(this);
      topLeftFigure.setBehaviorFlag(ITechDrawable.BEHAVIOR_23_PARENT_EVENT_CONTROL, true);

      StyleClass scRight = src.getStyleClass(ITechLinks.LINK_53_STYLE_SCROLLBAR_BOT_RIGHT_WRAPPER);
      botRightFigure = new FigDrawable(gc, scRight, botRightFig);
      botRightFigure.setParent(this);
      botRightFigure.setBehaviorFlag(ITechDrawable.BEHAVIOR_23_PARENT_EVENT_CONTROL, true);

      //#debug
      topLeftFigure.toStringSetName("Fig" + (hasTechFlag(SB_FLAG_1_VERT) ? "Top" : "Left"));
      //#debug
      botRightFigure.toStringSetName("Fig" + (hasTechFlag(SB_FLAG_1_VERT) ? "Bottom" : "Right"));
   }

   /**
    * One style class for each type of wrapper.
    */
   private void createWrapperTwo() {
      StyleClass scLeft = styleClass.getStyleClass(ITechLinks.LINK_52_STYLE_SCROLLBAR_TOP_LEFT_WRAPPER);
      topLeftFigure = new Drawable(gc, scLeft);
      topLeftFigure.setParent(this);
      topLeftFigure.setBehaviorFlag(ITechDrawable.BEHAVIOR_23_PARENT_EVENT_CONTROL, true);
      //debug
      topLeftFigure.toStringSetName("Fig" + (hasTechFlag(SB_FLAG_1_VERT) ? "Top" : "Left"));

      StyleClass scRight = styleClass.getStyleClass(ITechLinks.LINK_53_STYLE_SCROLLBAR_BOT_RIGHT_WRAPPER);
      botRightFigure = new Drawable(gc, scRight);
      botRightFigure.setParent(this);
      botRightFigure.setBehaviorFlag(ITechDrawable.BEHAVIOR_23_PARENT_EVENT_CONTROL, true);

      //debug
      botRightFigure.toStringSetName("Fig" + (hasTechFlag(SB_FLAG_1_VERT) ? "Bottom" : "Right"));
   }

   private void doUpdateBlockXYWH() {
      //we work on number of pixels used by background figure
      if (hasTechFlag(SB_FLAG_1_VERT)) {
         doUpdateBlockXYWHVert();
      } else {
         doUpdateBlockXYWHHoriz();
      }
   }

   private void doUpdateBlockXYWHHoriz() {
      int ww = numPixelsForBlock;
      //horizontal
      float onecol = (float) ww / (float) scrollConfig.getSITotal();
      int startX = (int) ((float) scrollConfig.getStartFirstFullyVisible() * onecol);
      if (hasTechFlag(SB_FLAG_5_ARROW_ON_BLOC)) {
         startX += getWrapperLeftRightWidth();
      }
      startX += getContentX() + blockBgFigure.getStyleWLeftConsumed();
      int endx = startX + (int) ((float) scrollConfig.getSIVisibleFully() * onecol);
      if (scrollConfig.isEnd()) {
         endx = blockBgFigure.getX() + ww - blockBgFigure.getStyleWRightConsumed();
      }
      barSize = endx - startX;
      //set minimum size
      int blockMinPixels = gc.getScrollbarBlockMinPixel();
      if (barSize < blockMinPixels) {
         barSize = blockMinPixels;
      }
      int startY = getContentY();
      int height = getBOSize1st();
      int width = barSize;
      initBlock(startX, startY, width, height);
   }

   /**
    * Start y coordinate is:
    * <li> {@link ScrollConfig#getStartFirstFullyVisible()} * numberOfPixel / {@link ScrollConfig#getSITotal()}
    * <li>Top wrapper height is added
    * <li>Style Top consume of block bg figure is added
    * <li>Finally add {@link ScrollBar#getContentY()} to reach absolute postion
    */
   private void doUpdateBlockXYWHVert() {
      int wh = numPixelsForBlock;
      //he is the size available
      float onerow = (float) wh / (float) scrollConfig.getSITotal();
      int starty = (int) ((float) scrollConfig.getStartFirstFullyVisible() * onerow);
      if (hasTechFlag(SB_FLAG_5_ARROW_ON_BLOC)) {
         starty += getWrapperTopBotHeight();
      }
      starty += getContentY() + blockBgFigure.getStyleHTopConsumed();

      int endy = starty + (int) ((float) scrollConfig.getSIVisibleFully() * onerow);
      //statement so that the scrollbar "touches" bottom
      if (scrollConfig.isEnd()) {
         endy = blockBgFigure.getY() + wh - blockBgFigure.getStyleHBotConsumed();
      }
      barSize = endy - starty;
      int blockMinPixels = gc.getScrollbarBlockMinPixel();
      //set minimum size
      if (barSize < blockMinPixels) {
         barSize = blockMinPixels;
      }
      initBlock(getContentX(), starty, getBOSize1st(), barSize);
   }

   /**
    * Update {@link ScrollConfig} types and behavior from {@link ViewPane}'s tech.
    * @param tech
    */
   public void doUpdateBOViewPane(ByteObject tech) {
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

   /**
    * Method modifies Scrollbar artifact positions and sizes based on the {@link ScrollConfig}.
    * 
    * Method called when ScrollingConfig changes its structure. <br>
    * If there is no scroll configuration, the method returns. <br>
    * It will change the block drawing params
    */
   public void doUpdateStructure() {

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

      doUpdateBlockXYWH();
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
      //#mdebug
      if (gc.toStringHasFlagDraw(IToStringFlagsDraw.FLAG_DRAW_01_SCROLL_BAR_BOUNDARY)) {
         g.setColor(255, 20, 20);
         toStringDrawBoundaryBox(g);
      }
      //#enddebug

      if (hasTechFlag(SB_FLAG_3_WRAPPER))
         drawWrapper(g);
      else {
         drawBlockBar(g);
      }
   }

   /**
    * This method draws the {@link ScrollBar} styled background before the 2 figures.
    * Draw the two directional figures. 
    * @param g
    */
   private void drawWrapper(GraphicsX g) {
      int secondSize = getBOSize2nd();
      int x = getX();
      int y = getY();
      int dw = getDw();
      int dh = getDh();
      if (hasTechFlag(SB_FLAG_1_VERT)) {
         int styleH = this.getStyleHConsumed();
         int width = dw;
         int height = secondSize + styleH;
         int dy = y + dh - secondSize - styleH;
         super.drawDrawableBg(g, x, y, width, height);
         super.drawDrawableBg(g, x, dy, width, height);
      } else {
         int styleW = this.getStyleWConsumed();
         int width = secondSize + styleW;
         int height = dh;
         int dx = x + dw - secondSize - styleW;
         super.drawDrawableBg(g, x, y, width, height);
         super.drawDrawableBg(g, dx, y, width, height);
      }
      topLeftFigure.draw(g);
      botRightFigure.draw(g);
   }

   /**
    * Returns {@link IBOScrollBar#SB_OFFSET_03_PRIMARY_SIZE4} decoded.
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
   public int getBOSize1st() {
      int size = boScrollbar.getValue(SB_OFFSET_03_PRIMARY_SIZE4, 4);
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
   public int getBOSize2nd() {
      int size = boScrollbar.get4(SB_OFFSET_04_SECOND_SIZE4);
      size = implicitDecode(size);
      return size;
   }

   /**
    * 3rd size is the size of the background artifact laying below the block
    * <br>
    * Cannot be bigger than 1st size, because the block must be sliding over it
    * @return
    */
   public int getBOSize3rd() {
      int size = boScrollbar.get4(SB_OFFSET_05_THIRD_SIZE4);
      size = implicitDecode(size);
      return size;
   }

   public ScrollConfig getConfig() {
      //#mdebug
      if (scrollConfig == null) {
         throw new NullPointerException();
      }
      //#enddebug
      return scrollConfig;
   }

   /**
    * Returns the preferred size : 
    * <li> width for a vertical scrollbar
    * <li> height for an horizontal scrollbar
    * 
    * <p>
    * It includes the style pad,border,margin of the {@link ScrollBar} styleclass.
    * </p>
    * 
    * @return
    */
   public int getMainSize() {
      int size = getBOSize1st();
      if (hasTechFlag(SB_FLAG_1_VERT)) {
         size += getStyleWConsumed();
      } else {
         size += getStyleHConsumed();
      }
      return size;
   }

   public int getSbSpaceConsumedHorizLeft() {
      if (hasBehavior(ITechDrawable.BEHAVIOR_16_IMMATERIAL)) {
         return 0;
      }
      if (hasTechFlag(SB_FLAG_3_WRAPPER)) {
         if (isHorizontal()) {
            return (getBOSize2nd() + this.getStyleWConsumed());
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
    * 
    * Returns the minimum sum of pixels on any line consumed horizontally by the scrollbar components.
    * In a {@link ViewPane} context, this value computes the viewport's width
    * 
    * <li> 0 when {@link ITechDrawable#BEHAVIOR_16_IMMATERIAL}
    * <li> 0 when there is a line
    * 
    * @return
    */
   public int getSbSpaceConsumedHorizMin() {
      if (hasBehavior(ITechDrawable.BEHAVIOR_16_IMMATERIAL)) {
         return 0;
      }
      if (hasTechFlag(SB_FLAG_3_WRAPPER)) {
         if (isHorizontal()) {
            return 2 * (getBOSize2nd() + this.getStyleWConsumed());
         }
      } else {
         //block type of scrollbar. then vertical scrollbar will consume
         if (hasTechFlag(SB_FLAG_1_VERT)) {
            return getMainSize();
         }
      }
      return 0;
   }

   public int getSbSpaceConsumedVertMax() {
      if (hasBehavior(ITechDrawable.BEHAVIOR_16_IMMATERIAL)) {
         return 0;
      }
      if (hasTechFlag(SB_FLAG_3_WRAPPER)) {
         if (hasTechFlag(SB_FLAG_1_VERT)) {
            return 2 * (getBOSize2nd() + this.getStyleHConsumed());
         }
      } else {
         //block type of scrollbar. then vertical scrollbar will consume
         if (!hasTechFlag(SB_FLAG_1_VERT)) {
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
   public int getSbSpaceConsumedVertMin() {
      if (hasBehavior(ITechDrawable.BEHAVIOR_16_IMMATERIAL)) {
         return 0;
      }
      if (hasTechFlag(SB_FLAG_3_WRAPPER)) {
         if (hasTechFlag(SB_FLAG_1_VERT)) {
            return 2 * (getBOSize2nd() + this.getStyleHConsumed());
         }
      } else {
         //block type of scrollbar. then vertical scrollbar will consume
         if (!hasTechFlag(SB_FLAG_1_VERT)) {
            return getMainSize();
         }
      }
      return 0;
   }

   public int getSbSpaceConsumedVertTop() {
      if (hasBehavior(ITechDrawable.BEHAVIOR_16_IMMATERIAL)) {
         return 0;
      }
      if (hasTechFlag(SB_FLAG_3_WRAPPER)) {
         if (hasTechFlag(SB_FLAG_1_VERT)) {
            return (getBOSize2nd() + this.getStyleHConsumed());
         }
      } else {
         //block type of scrollbar. then vertical scrollbar will consume
         if (!hasTechFlag(SB_FLAG_1_VERT)) {
            return getMainSize();
         }
      }
      return 0;
   }

   /**
    * Width pixels consumed on the left of the box.
    * 
    * <li>For wrappers, its the width of the left wrapper
    * <li>For block scrollbars, its the main size when vertical and reversed (i.e. drawn at the top)
    * 
    * @return
    */
   public int getShiftXLeft() {
      if (hasTechFlag(SB_FLAG_3_WRAPPER)) {
         if (!hasTechFlag(SB_FLAG_1_VERT)) {
            return getWrapperLeftRightWidth() + this.getStyleWConsumed();
         }
      } else {
         //block
         if (hasTechFlag(SB_FLAG_1_VERT) && hasTechFlag(SB_FLAG_6_BLOCK_INVERSE)) {
            return getMainSize();
         }
      }
      return 0;
   }

   public int getShiftXRight() {
      if (hasTechFlag(SB_FLAG_3_WRAPPER)) {
         if (!hasTechFlag(SB_FLAG_1_VERT)) {
            return getWrapperLeftRightWidth() + this.getStyleWConsumed();
         }
      } else {
         //block
         if (hasTechFlag(SB_FLAG_1_VERT) && !hasTechFlag(SB_FLAG_6_BLOCK_INVERSE)) {
            return getMainSize();
         }
      }
      return 0;
   }

   /**
    * Height pixels consumed at the bottom of the box.
    * 
    * <li>For wrappers, its the size of the top wrapper
    * <li>For block scrollbars, its the main size when horizontal and reversed (i.e. drawn at the top)
    * 
    * @return
    */
   public int getShiftYBot() {
      if (hasTechFlag(SB_FLAG_3_WRAPPER)) {
         if (hasTechFlag(SB_FLAG_1_VERT)) {
            return getWrapperTopBotHeight() + this.getStyleHConsumed();
         }
      } else {
         //block
         if (!hasTechFlag(SB_FLAG_1_VERT) && !hasTechFlag(SB_FLAG_6_BLOCK_INVERSE)) {
            return getMainSize();
         }
      }
      return 0;
   }

   /**
    * Height pixels consumed at the top of the box.
    * 
    * <li>For wrappers, its the size of the top wrapper
    * <li>For block scrollbars, its the main size when horizontal and reversed (i.e. drawn at the top)
    * 
    * @return
    */
   public int getShiftYTop() {
      if (hasTechFlag(SB_FLAG_3_WRAPPER)) {
         if (hasTechFlag(SB_FLAG_1_VERT)) {
            return getWrapperTopBotHeight() + this.getStyleHConsumed();
         }
      } else {
         //block
         if (!hasTechFlag(SB_FLAG_1_VERT) && hasTechFlag(SB_FLAG_6_BLOCK_INVERSE)) {
            return getMainSize();
         }
      }
      return 0;
   }

   public StyleClass getStyleClassForChildren() {
      return this.styleClass;
   }

   /**
    * Left/Right wrapper height. <br>
    * 
    * @return
    */
   public int getWrapperLeftRightHeight() {
      if (hasTechFlag(SB_FLAG_3_WRAPPER) && hasTechFlag(SB_FLAG_4_WRAPPER_FILL)) {
         //filling the whole space
         return boundaryBoxH - this.getStyleHConsumed();
      } else {
         return getBOSize1st();
      }
   }

   /**
    * Styled
    * @return
    */
   public int getWrapperLeftRightHeightStyle() {
      return getWrapperLeftRightHeight() + getStyleHConsumed();
   }

   /**
    * Left and Right wrapper width is defined by the Tech param {@link IBOScrollBar#SB_OFFSET_04_SECOND_SIZE4}.
    * @return
    */
   public int getWrapperLeftRightWidth() {
      return getBOSize2nd();
   }

   /**
    * Total width of content + style of Left or Right wrapper
    * @return
    */
   public int getWrapperLeftRightWidthStyle() {
      return getWrapperLeftRightWidth() + getStyleWConsumed();
   }

   /**
    * Containes border
    * @return
    */
   public int getWrapperTopBotHeight() {
      return getBOSize2nd();
   }

   public int getWrapperTopBotHeightStyled() {
      return getWrapperTopBotHeight() + getStyleHConsumed();
   }

   /**
    * The pixel width of the wrapper scrollbar
    * Overlay border pixel size is not computed in
    * Reads the ByteObject for data if non null
    * @return
    */
   public int getWrapperTopBotWidth() {
      if (hasTechFlag(SB_FLAG_3_WRAPPER) && hasTechFlag(SB_FLAG_4_WRAPPER_FILL))
         return boundaryBoxW - this.getStyleWConsumed();
      else
         return getBOSize1st();
   }

   public int getWrapperTopBotWidthStyled() {
      return getWrapperTopBotWidth() + getStyleWConsumed();
   }

   public boolean hasTechFlag(int flag) {
      return boScrollbar.hasFlag(SB_OFFSET_01_FLAG, flag);
   }

   public boolean hasTechFlagX(int flag) {
      return boScrollbar.hasFlag(SB_OFFSET_02_FLAGX, flag);
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

   public void initSize() {
      //potentially called when used as a a stand alone drawable.
      super.initSize();
   }

   /**
    * Initialize the {@link ScrollBar} dimension using the boundary box system.  
    * 
    * 
    * After calling this method, those methods returns a correct value
    * <li> {@link ScrollBar#getSbSpaceConsumedHorizMin()}
    * <li> {@link ScrollBar#getSbSpaceConsumedVertMin()}
    * 
    * Dw and Dh are set for animation purpose. 
    * 
    * Fit the smallest rectangle possible to include all Scrollbar items.
    * 
    * In wrapper mode {@link IBOScrollBar#SB_FLAG_3_WRAPPER}, {@link IDrawable#getHoles()} return the middle hole
    * not used by the {@link ScrollBar}.
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
      //sizerCtx.w = width;
      //sizerCtx.h = height;
      this.layEngine.setManualOverrideH(true);
      this.layEngine.setManualOverrideW(true);
      //compute smallest rectangle size based on boundary box size and style/tech data.
      //step used to resize the ViewPort in a ViewPane context
      if (hasTechFlag(SB_FLAG_3_WRAPPER)) {
         initSizeSBWrapper(width, height);
      } else {
         initSizeSBBlock(width, height);

      }
      
      super.initStyleCache();
      
      setStateFlag(ITechDrawable.STATE_05_LAYOUTED, true);
      
      
   }

   private void initBlock(int x, int y, int w, int h) {
      blockFigure.setSizePixels(w, h);
      blockFigure.initSize();
      blockFigure.setXY(x, y);
   }

   public void initPosition() {
      super.initPosition();
      if (hasTechFlag(SB_FLAG_3_WRAPPER)) {
         initPositionWrapper();
      } else {
         initPositionBlock();
      }
      if (blockBgFigure != null) {
         doUpdateBlockXYWH();
      }
   }

   /**
    * Based on boundary box x,y
    */
   private void initPositionBlock() {
      //#debug
      toDLog().pInit(toStringBoundaryBox(), this, ScrollBar.class, "initPositionBlock", LVL_05_FINE, true);
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
      int cx = this.getX() + this.getStyleWLeftConsumed();
      int cy = this.getY() + this.getStyleHTopConsumed();
      //block bar
      if (hasTechFlag(SB_FLAG_5_ARROW_ON_BLOC)) {
         topLeftFigure.setXY(cx, cy);
         if (hasTechFlag(SB_FLAG_1_VERT)) {
            cy += topLeftFigure.getDrawnHeight();
         } else {
            cx += topLeftFigure.getDrawnWidth();
         }
      }

      //init the block bg figure
      if (blockBgFigure != null) {
         int size1 = getBOSize1st();
         int size3 = getBOSize3rd();
         int dx = cx;
         int dy = cy;
         //if background is bigger, it main size
         if (size3 < size1) {
            ByteObject blockFigureStyle = blockBgFigure.getStyle();
            ByteObject anchor = getStyleOp().getStyleAnchor(blockFigureStyle);

            if (hasTechFlag(SB_FLAG_1_VERT)) {
               if (anchor == null) {
                  //def to center
                  dx = AnchorUtils.getXAlign(ITechAnchor.ALIGN_6_CENTER, dx, size1, size3);
               } else {
                  dx = AnchorUtils.getXAlign(anchor, dx, size1, size3);
               }
            } else {
               if (anchor == null) {
                  //def to center
                  dy = AnchorUtils.getYAlign(ITechAnchor.ALIGN_6_CENTER, dy, size1, size3);
               } else {
                  dy = AnchorUtils.getYAlign(anchor, dy, size1, size3);
               }
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
      int x = getX();
      int y = getY();
      int cx = x + getStyleWLeftConsumed();
      int cy = y + getStyleHTopConsumed();
      topLeftFigure.setXY(cx, cy);

      if (hasTechFlag(SB_FLAG_1_VERT)) {
         cy = y + boundaryBoxH - getWrapperTopBotHeight() - this.getStyleHBotConsumed();
      } else {
         cx = x + boundaryBoxW - getWrapperLeftRightWidth() - this.getStyleWRightConsumed();
      }
      botRightFigure.setXY(cx, cy);
   }

   /**
    * Positions wrappers and arrows on block.
    * @param x
    * @param y
    * @param w
    * @param h
    */
   private void initPositionWrapper(int x, int y) {
      int cx = x + getStyleWLeftConsumed();
      int cy = y + getStyleHTopConsumed();
      topLeftFigure.setXY(cx, cy);
      if (hasTechFlag(SB_FLAG_1_VERT)) {
         cy += boundaryBoxH - getWrapperTopBotHeight();
      } else {
         cx += boundaryBoxW - getWrapperLeftRightWidth();
      }
      botRightFigure.setXY(cx, cy);
   }

   private void initSizeSBBlock(int width, int height) {
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

      if (hasTechFlag(SB_FLAG_1_VERT)) {
         numPixelsForBlock = boundaryBoxH - this.getStyleHConsumed();
      } else {
         numPixelsForBlock = boundaryBoxW - this.getStyleWConsumed();
      }
      if (hasTechFlag(SB_FLAG_5_ARROW_ON_BLOC)) {
         //here we init the 
         initSizeWrapper();

         if (hasTechFlag(SB_FLAG_1_VERT)) {
            int tbH = getWrapperTopBotHeight();
            numPixelsForBlock -= (2 * tbH);
         } else {
            int lrW = getWrapperLeftRightWidth();
            numPixelsForBlock -= (2 * lrW);
         }
      }

      //init the block bg figure
      if (blockBgFigure != null) {
         int size3 = getBOSize3rd();
         if (hasTechFlag(SB_FLAG_1_VERT)) {
            blockBgFigure.setSizePixels(size3, numPixelsForBlock);
         } else {
            blockBgFigure.setSizePixels(numPixelsForBlock, size3);
         }
         blockBgFigure.initSize();
      }
      //the block figure
   }

   private void initSizeSBWrapper(int width, int height) {
      if (hasTechFlag(SB_FLAG_4_WRAPPER_FILL)) {
         setDw(width);
         setDh(height);
      } else {
         int s2ndSize = getBOSize2nd();
         int s1stSize = getBOSize1st();
         if (hasTechFlag(SB_FLAG_1_VERT)) {
            this.setDw(s1stSize + this.getStyleWConsumed());
            this.setDh(height);
         } else {
            this.setDw(width);
            this.setDh(s1stSize + this.getStyleHConsumed());
         }

         ///TODO hole management
         setStateFlag(ITechDrawable.STATE_24_HOLED, true);
         holes = new int[4];
         //only with overlay.. let ViewPane query this and decide what to do
         if (hasTechFlag(SB_FLAG_1_VERT)) {
            holes[ITechDrawable.HOLE_0_X] = getX();
            holes[ITechDrawable.HOLE_1_Y] = getY() + s2ndSize;
            holes[ITechDrawable.HOLE_2_W] = getWrapperTopBotWidth();
            holes[ITechDrawable.HOLE_3_H] = boundaryBoxH - (2 * s2ndSize);
         } else {
            holes[ITechDrawable.HOLE_0_X] = getX() + s2ndSize;
            holes[ITechDrawable.HOLE_1_Y] = getY();
            holes[ITechDrawable.HOLE_2_W] = boundaryBoxW - (2 * s2ndSize);
            holes[ITechDrawable.HOLE_3_H] = getWrapperLeftRightHeight();
         }
      }

      initSizeWrapper();
   }

   /**
    * Init the size of wrapper elements
    * <br>
    * <br>
    * The hole definition is finalized here
    */
   private void initSizeWrapper() {
      if (hasTechFlag(SB_FLAG_1_VERT)) {
         int tbCH = getWrapperTopBotHeight();
         int tbCW = getWrapperTopBotWidth();
         topLeftFigure.setSizePixels(tbCW, tbCH);
         botRightFigure.setSizePixels(tbCW, tbCH);
      } else {
         int lrCW = getWrapperLeftRightWidth();
         int lrCH = getWrapperLeftRightHeight();
         topLeftFigure.setSizePixels(lrCW, lrCH);
         botRightFigure.setSizePixels(lrCW, lrCH);
      }
      
      topLeftFigure.initSize();
      botRightFigure.initSize();
   }

   public boolean isHorizontal() {
      return !hasTechFlag(SB_FLAG_1_VERT);
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
         doUpdateStructure();
      }
      ic.srActionDoneRepaint();
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

         doUpdateStructure();

         repaintScrollbarAndContent(ic);
         animCall(ic, upleft);
      }
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
         doUpdateStructure();
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

   public void notifyEvent(IDrawable d, int event, Object o) {
      if (event == ITechDrawable.EVENT_14_POINTER_EVENT) {
         managePointerEvent(d, (InputConfig) o);
      }
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
         doUpdateStructure();
      }
      //repaint ViewDrawable content and this scrollbar
      repaintScrollbarAndContent(ic);

      //generates call drag method on View
      pane.getViewDrawable().manageDragCallBack(this);
      if (trailer != null) {
         if (trailer.hasState(ITechDrawable.STATE_03_HIDDEN)) {
            trailer.init(0, 0);
            vc.getDrawCtrlAppli().shShowDrawable(ic, trailer, ITechCanvasDrawable.SHOW_TYPE_1_OVER_TOP);
         }
         trailer.setXY(blockFigure.getX() - trailer.getDrawnWidth(), blockFigure.getY());
      }
   }

   /**
    * Called when a release pointer event is generated after a dragged of block scrollbar.
    * @param ic
    */
   private void pointerReleasedDragged(InputConfig ic) {
      //System.out.println("#Scrollbar pointerReleasedDragged after Dragging");
      //deselects all drawables that may have been selected
      actionUnselectTopBot(ic);
      if (blockFigure != null) {
         blockFigure.setStateStyle(ITechDrawable.STYLE_05_SELECTED, false);
      }
      if (trailer != null) {
         vc.getDrawCtrlAppli().removeDrawable(null, trailer, null);
      }
      ic.srActionDoneRepaint(this);
   }

   public void repaintScrollbarAndContent(InputConfig ic) {
      //scroll action done on ScrollBar. we want to repaint the scrollbar and ViewDrawable's content.
      ic.srActionDoneRepaint(this);
      if (pane != null) {
         ViewDrawable vd = pane.getViewDrawable();
         //ask to repaint only the content of the viewpane
         vd.setFlagView(ITechViewDrawable.FLAG_VSTATE_02_REPAINTING_CONTENT, true);
         ic.srActionDoneRepaint(vd);
      }
   }

   /**
    * Create the {@link Drawable} of the {@link ScrollBar}.
    * <br>
    * <br>
    * 
    * @param boSb
    */
   public void setBOScrollbar(ByteObject boSb) {
      if (boSb == null)
         throw new NullPointerException("No Tech Param defined for Scrollbar");
      this.boScrollbar = boSb;
      //based on the tech design, fetch the style of the items of the scrollbar
      if (boScrollbar.hasFlag(SB_OFFSET_01_FLAG, SB_FLAG_3_WRAPPER)) {
         //linked parameter of Tech has the StyleDef
         createWrappers();
      } else {
         StyleClass bkey = styleClass.getStyleClass(ITechLinks.LINK_51_STYLE_SCROLLBAR_BLOCK_FIG);
         if (bkey != null) {
            //there is at least a block
            blockFigure = new Drawable(gc, bkey);
            blockFigure.setParent(this);
            blockFigure.setBehaviorFlag(ITechDrawable.BEHAVIOR_23_PARENT_EVENT_CONTROL, true);
            blockFigure.toStringSetName("BlockFigure");
            //check if there is a special background
            StyleClass key = styleClass.getStyleClass(ITechLinks.LINK_50_STYLE_SCROLLBAR_BLOCK_BG);
            blockBgFigure = new Drawable(gc, key);
            blockBgFigure.setParent(this);
            blockBgFigure.setBehaviorFlag(ITechDrawable.BEHAVIOR_23_PARENT_EVENT_CONTROL, true);
            blockBgFigure.setStateFlag(ITechDrawable.STATE_27_OVERLAYED, true);
            blockBgFigure.toStringSetName("BlockBg");
         } else {
            //draw none. code breaks if a background and no block
         }

         if (boScrollbar.hasFlag(SB_OFFSET_01_FLAG, SB_FLAG_5_ARROW_ON_BLOC)) {
            createWrappers();
         }
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
      //#debug
      toDLog().pFlow(x + "," + y, this, ScrollBar.class, "setMyXY", LVL_05_FINE, true);
      this.setX(x);
      this.setY(y);
   }

   public void setScrollConfig(ScrollConfig scrollConfig) {
      if (scrollConfig == null) {
         throw new NullPointerException();
      }
      this.scrollConfig = scrollConfig;
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

   //#mdebug

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

   public void setTrailer(IDrawable trailer) {
      this.trailer = trailer;
   }

   /**
    * Sets the x,y coordinate of the boundary box. <br>
    * <br>
    * Computes positions of ScrollBar artifacts.
    */
   public void setXY(int x, int y) {
      super.setXY(x, y);
      boundaryBoxX = x;
      boundaryBoxY = y;
   }

   public void shiftXY(int x, int y) {
      //#debug
      toDLog().pFlow(x + "," + y, this, ScrollBar.class, "shiftXY", LVL_05_FINE, true);

      boundaryBoxX += x;
      boundaryBoxY += y;
      this.setX(this.getX() + x);
      this.setY(this.getY() + y);
      //ViewPane.shiftXY(this, x, y);
      DrawableUtilz.shiftXY(topLeftFigure, x, y);
      DrawableUtilz.shiftXY(botRightFigure, x, y);
      DrawableUtilz.shiftXY(blockFigure, x, y);
      DrawableUtilz.shiftXY(blockBgFigure, x, y);
      DrawableUtilz.shiftXY(dConfigInfo, x, y);
      //shift the holes if any
      if (holes != null) {
         holes[ITechDrawable.HOLE_0_X] += x;
         holes[ITechDrawable.HOLE_1_Y] += y;
      }
      //when shifting we must update all x,y dependant figures
      doUpdateStructure();
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, ScrollBar.class, 1700);
      toStringPrivate(dc);
      super.toString(dc.sup());

      dc.nl();
      dc.append("HorizSpaceConsumed=" + getSbSpaceConsumedHorizMin() + " VertSpaceConsumed=" + getSbSpaceConsumedVertMin());
      dc.append(" xShift=" + getShiftXLeft());
      dc.append(" yShift=" + getShiftYTop());

      dc.nl();
      dc.append("TBWrapperW=" + getWrapperTopBotWidth());
      dc.append(" TBWrapperH=" + getWrapperTopBotHeight());
      dc.append(" LRWrapperW=" + getWrapperLeftRightWidth());
      dc.append(" LRWrapperH=" + getWrapperLeftRightHeight());

      dc.nlLvlIgnoreNull("TopLeft Drawable", topLeftFigure);
      dc.nlLvlIgnoreNull("BotRight Drawable", botRightFigure);
      dc.nlLvlIgnoreNull("Block Bg Drawable", blockBgFigure);
      dc.nlLvlIgnoreNull("Block Drawable", blockFigure);

      dc.nlLvl(scrollConfig);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ScrollBar.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private String toStringBoundaryBox() {
      return "bbox=[" + boundaryBoxX + "," + boundaryBoxY + " - " + boundaryBoxW + "," + boundaryBoxH + "]";
   }

   private void toStringDrawBoundaryBox(GraphicsX g) {
      g.drawRect(boundaryBoxX - 1, boundaryBoxY - 1, boundaryBoxW + 1, boundaryBoxH + 1);
   }

   private void toStringPrivate(Dctx dc) {
      if (hasTechFlag(SB_FLAG_1_VERT)) {
         dc.appendWithSpace("Vertical");
      } else {
         dc.appendWithSpace("Horizontal");
      }
      dc.append(" Boundary [" + boundaryBoxX + "," + boundaryBoxY + " " + boundaryBoxW + "," + boundaryBoxH + "]");
   }

   //#enddebug

}
