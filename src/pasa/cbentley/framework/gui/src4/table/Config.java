package pasa.cbentley.framework.gui.src4.table;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.coreui.src4.utils.ViewState;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.core.ScrollConfig;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOTableView;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;

/**
   * Internal helper that defines the visual situation of the table.<br>
   * <br>
   * Converts to {@link ScrollConfig} when Scrolling event occurs. FOr
   * example, when selected cell is going down out of viewport, the
   * ScrollingConfig if ViewPane is fed to
   * {@link Config#setScrollConfigY(ScrollConfig)}
   * 
   * <br>
   * All variables that may change when user navigates in the Table<br>
   * 
   * <li>Primary = selection<br> <li>Secondary = rows/cols from master to
   * selection<br> <li>Results = Boundaries and Sizes. computed from above and
   * numTotalRows/Cols<br>
   * <br>
   * <br>
   * Style of table applies around<br>
   * <br>
   * Part of the {@link ViewState} <br>
   * <br>
   * 
   * @author Charles-Philip Bentley
   * 
   */
public class Config implements IStringable {

   /**
    * Pixels actually consumed by the visible rows. <br>
    * It may be smaller/greater than the height available.
    */
   int                    consumedHeight;

   /**
    * Pixels actually consumed by the visible columns. <br>
    * It may be smaller/greater than the width available.
    */
   int                    consumedWidth;

   /**
    * Absolute index of first partial or fully visible column.
    */
   int                    firstColAbs;

   /**
    * Absolute index of first visible row.
    */
   int                    firstRowAbs;

   /**
    * When Config is computed, this flag is set when ViewPort clip should
    * be set.
    */
   boolean                isClipNeeded            = false;

   /**
    * Absolute index of last partial or fully visible column.
    */
   int                    lastColAbs;

   /**
    * Absolute index of last partial or fully visible row.
    */
   int                    lastRowAbs;

   /**
    * Number of fully visible columns. 
    * <br>
    * <br>
    * Might be zero (in case of {@link IBOTableView#HELPER_FLAG_03_CAP_OVERSIZE} ,1 or more. 
    * <br>
    * <br>
    * <li>With {@link ITechViewPane#SCROLL_TYPE_0_PIXEL_UNIT}, 1 if {@link ITechViewPane#VISUAL_1_PARTIAL} horizontally. 
    * <li>With {@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT}, 0, 1 or 2. 
    * <li>With {@link ITechViewPane#SCROLL_TYPE_2_PAGE_UNIT} not supported. 
    * <br>
    * <br>
    * When partial cell on the x axis is enabled, this value differs from total number of visible columns
    */
   int                    numFullyVisibleCols;

   /**
    * Number of fully visible rows. Computed based on {@link ScrollConfig}
    */
   int                    numFullyVisibleRows;

   /**
    * Number of visible (fully and partially) columns in this configuration
    */
   int                    numVisibleCols;

   /**
    * Number of visible (fully and partially) rows in this configuration
    */
   int                    numVisibleRows;

   /**
    * absolute index of the incomplete column. 
    */
   int                    partiallyVisibleColAbs  = -1;

   /**
    * Possible when pixel scrolling
    */
   int                    partiallyVisibleColAbs2 = -1;

   /**
    * absolute index of the incomplete row.
    * <br>
    * <br>
    * What when 2 incomplete with
    * <br>
    * Used to know if selection goes into a partially drawn cell.
    */
   int                    partiallyVisibleRowAbs  = -1;

   /**
    * Possible when pixle scrolling
    */
   int                    partiallyVisibleRowAbs2 = -1;

   /**
    * Root focus. Column to be displayed first semantics depends on the master v. 
    * <br>
    * When LeftMaster, {@link Config#rootColAbs} = {@link Config#firstColAbs} Slightly different meaning. Used when
    * dealing with variable FILL policies. 
    * <br>
    * Example: a config such as SET,WEAK,SET,AVERAGE either show SET,WEAK,SET or SET,AVERAGE. 
    * <br>
    * When selection goes from SET to AVERAGE, {@link Config#firstColAbs} hops 2 cells.
    */
   int                    rootColAbs;

   /**
    * Master cell for key up-down events. <br>
    * <br>
    * Used in conjunection with {@link Config#useTopMaster} to define the
    * direction. <br>
    * Usually the root is the selected row when
    * {@link IBOTableView#T_OFFSET_08_SELECTION_SCROLL1} is 0. Else it differs.
    * displayed first or second. <br>
    * Another policy would be to always keep it in the middle. See <br>
    * <br>
    * See {@link IBOTableView#T_FLAGY_4_ROOT_FOLLOWS_SCROLLING}. If root
    * does not follow Pixel scrolling, does not For logical scrolling, that
    * does not change, though TECH
    */
   int                    rootRowAbs;

   /**
    * column index of the cell who has the key focus.
    * <br>
    * //TODO Tracks the "selected" i.e. focused Cell whose state style {@link ITechDrawable#STYLE_05_SELECTED} is set
    * However when Table is not in the focus hierarchy, the style may be removed depending on Tech option
    */
   int                    selectedColAbs;

   /**
    * Primary Input input variable for row selection
    */

   int                    selectedRowAbs;

   /**
    * Transition to reach this configuration
    */
   public Transition      tr;

   /**
    * Number of pixels partially drawn for <b>last visible row</b>. <br>
    */
   int                    uleftOverHBottom        = 0;

   /**
    * Number of pixels partially drawn for <b>first</b> visible row. <br>
    */
   int                    uleftOverHTop           = 0;

   /**
    * Number of pixels partially drawn for <b>first visible col</b>. <br>
    */
   int                    uleftOverWLeft          = 0;

   /**
    * Number of pixels partially drawn for <b>last visible col</b>. <br>
    */
   int                    uleftOverWRight         = 0;

   /**
    * Number of undrawn pixels for last visible row. <br>
    * Zero if last row is fully visible
    */
   int                    undrawnHBottom          = 0;

   /**
    * Number of undrawn pixels for the first visible row. 
    * <br>
    * <br>
    * Value is used when : <br>
    * <li>{@link ITechViewPane#SCROLL_TYPE_0_PIXEL_UNIT} is set horizontally =>computed in {@link TableView#matchPixelConfigY} 
    * <li>{@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT} and {@link ITechViewPane#VISUAL_1_PARTIAL} is set for columns and first column is partially visible.
    * <br>
    * <br>
    * In first case, value is ;
    */
   int                    undrawnHTop;

   /**
    * Number of undrawn pixels for the first visible column. 
    * <br>
    * <br>
    * Offset for all cells' X coordinates. 
    * <br>
    * <br>
    * Value is used when : <br>
    * <li>{@link ITechViewPane#VISUAL_1_PARTIAL} is set for columns and first column is partially visible. 
    * <li>Pixel scrolling {@link ITechViewPane#SCROLL_TYPE_0_PIXEL_UNIT} is set horizontally. 
    * <br>
    * In other cases, it will be zero.
    */
   int                    undrawnWLeft;

   /**
    * Number of undrawn pixels for last visible column. 
    * <br>
    * <br>
    * Zero if last row is fully visible. 
    * <br>
    * See {@link Config#undrawnWLeft}
    */
   int                    undrawnWRight           = 0;

   /**
    * Semantics in {@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT} horizontally.
    * <br>
    * <br>
    * true = left master => leftmost column is always fully visible <br>
    * false = right master => right most column is always fully visible<br>
    * 
    * When the selection is heading to the left => the root column is on
    * the left<br>
    * When the selection is heading to the right => the root column will be
    * the one on the right<br>
    * 
    */
   boolean                useLeftMaster           = true;

   /**
    * Semantics in {@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT}
    * vertically. <br>
    * true = top master => topmost row is always fully visible <br>
    * false = bottom master => bottom most row is always fully visible<br>
    */
   boolean                useTopMaster            = true;

   /**
    * Secondary Input if left master, this is the number of visible columns
    * left of selected col visibleColsFromMaster , visibleColsFromMaster ,
    * selecteColAbs, colNotFullyVisible
    * 
    * if right master, this is the number of visible columns right of
    * selected col colNotFullyVisible, selecteColAbs, visibleColsFromMaster
    * , visibleColsFromMaster
    * 
    * 
    * if newColAbs is 0 and left master, then this value can only be 0
    * |newColAbs, ..., ... if newColAbs is 0 and right master, then this
    * value [0 numTotalCol[
    * 
    * update method try to populate that number of columns with the
    * available size
    */
   int                    visibleColsFromMaster;

   /**
    * number of rows drawn before drawing the selected row If not enough
    * space, it will do its best and draw as many rows as possible
    */
   int                    visibleRowsFromMaster;

   /**
    * Sizes to use for drawing the visible columns.
    * <br>
    * <br>
    * Most of the time, those will be setupSizes except when with Variable Size Cells
    */
   int[]                  workColSizes;

   /**
    * Sizes to use for drawing the visible rows
    */
   int[]                  workRowSizes;

   /**
    * x coordinates of cells relative to 0,0 of that configuration. 
    * <br>
    * <br>
    * Computes only for visible columns of that config.
    * <br>
    * <br>
    * Values will be fed to the model IDrawable just before being drawn.
    * <br>
    * {@link TableView} uses {@link Config#getOffsetXColAbs(int)}.
    */
   int[]                  xs;

   /**
    * y coordinate of cells relative to 0,0 . xoffsets have been
    * computed in. <br>
    * Will be fed to the IDrawable just before being drawn.
    */
   int[]                  ys;

   protected final GuiCtx gc;

   /**
    * Init {@link Transition}
    */
   public Config(GuiCtx gc) {
      this.gc = gc;
      tr = new Transition(gc);
   }

   /**
    * 
    * @param c
    * @param tr
    */
   private void computeNotFullyVisible(Config c, Transition tr) {
      partiallyVisibleRowAbs = -1;
      partiallyVisibleColAbs = -1;
      if (c.partiallyVisibleRowAbs == selectedRowAbs) {
         if (tr.vy > 0) {
            // now partial cells are computed bottom up
            useTopMaster = false;
            partiallyVisibleRowAbs = firstRowAbs;
         } else {
            useTopMaster = true;
            partiallyVisibleRowAbs = lastRowAbs;
         }
      }
      if (c.partiallyVisibleColAbs == selectedColAbs) {
         if (tr.vx > 0) {
            useLeftMaster = false;
            partiallyVisibleColAbs = firstColAbs;
         } else {
            useLeftMaster = true;
            partiallyVisibleColAbs = lastColAbs;
         }
      }
   }

   void doTransitionInside(Config oldConfig) {
      undrawnWLeft = oldConfig.undrawnWLeft;
      undrawnHTop = oldConfig.undrawnHTop;
      numVisibleCols = oldConfig.numVisibleCols;
      numVisibleRows = oldConfig.numVisibleRows;
      rootRowAbs = oldConfig.rootRowAbs;
      rootColAbs = oldConfig.rootColAbs;
      firstColAbs = oldConfig.firstColAbs;
      lastColAbs = oldConfig.lastColAbs;
      firstRowAbs = oldConfig.firstRowAbs;
      lastRowAbs = oldConfig.lastRowAbs;
      useLeftMaster = oldConfig.useLeftMaster;
      useTopMaster = oldConfig.useTopMaster;
   }

   void doTransOutside(Config oldConfig, Transition tr) {
      if (tr.vx == 0 && tr.vy == 0) {
         throw new IllegalArgumentException("Transition vx and vy are both 0");
      }
      if (tr.vx != 0 && tr.vy != 0) {
         rootColAbs = tr.newSelectedColAbs;
         rootRowAbs = tr.newSelectedRowAbs;
      } else if (tr.vy == 0) {
         // keep rows
         rootRowAbs = oldConfig.rootRowAbs;
         rootColAbs = tr.newSelectedColAbs;
         useTopMaster = oldConfig.useTopMaster;
         if (tr.vx > 0) {
            useLeftMaster = false;
         } else {
            useLeftMaster = true;
         }
      } else if (tr.vx == 0) {
         // keep col
         rootColAbs = oldConfig.rootColAbs;
         rootRowAbs = tr.newSelectedRowAbs;
         useLeftMaster = oldConfig.useLeftMaster;
         if (tr.vy > 0) {
            useTopMaster = false;
         } else {
            useTopMaster = true;
         }
      }
   }

   /**
    * Called when {@link TableView#selectionMove(InputConfig, int, int)} moves to a new {@link Config} using {@link Transition}.
    * <br>
    * <br>
    * Method create that new {@link Config}.
    * <br>
    * <br>
    * @param oldConfig
    * @param tr {@link Transition}
    */
   public void doUpdateFromTransition(Config oldConfig, Transition tr) {
      this.tr = tr;
      selectedColAbs = tr.newSelectedColAbs;
      selectedRowAbs = tr.newSelectedRowAbs;
      xs = oldConfig.xs;
      ys = oldConfig.ys;
      workColSizes = oldConfig.workColSizes;
      workRowSizes = oldConfig.workRowSizes;

      switch (tr.type) {
         case Transition.TRANSITION_0INSIDE_VIEWPORT:
            doTransitionInside(oldConfig);
            break;
         case Transition.TRANSITION_1INSIDE_TO_PARTIAL:
            break;
         case Transition.TRANSITION_2OUTSIDE:
            doTransOutside(oldConfig, tr);
            break;
         default:
            break;
      }
      computeNotFullyVisible(oldConfig, tr);

   }

   /**
    * Returns the column size of the grid.
    * <br>
    * @param colAbs
    * @return
    */
   private int getDrawnColAbsSize(int colAbs) {
      return workColSizes[colAbs];
   }

   /**
    * 
    * @param rowAbs
    * @return
    */
   private int getDrawnRowAbsSize(int rowAbs) {
      return workRowSizes[rowAbs];
   }

   /**
    * Policy based initialization method. 
    * <br>
    * <br>
    * Computes pixels needed to draw x number of cells from that position. <br>
    * <br>
    * Policies with cell sizes function of first col/row have to call this
    * method everytime that duo of values changes. <br>
    * 
    * @param size
    *            number of cells from first col/row position index position
    * @param row
    * @return
    */
   public int getNumCellsSize(int numCells, boolean row) {
      int[] sizes = (row) ? workRowSizes : workColSizes;
      int start = (row) ? firstRowAbs : firstColAbs;
      int total = 0;
      int count = 0;
      for (int i = start; i < sizes.length; i++) {
         total += sizes[i];
         count++;
         if (count == numCells)
            break;
      }
      return total;
   }

   /**
    * Might be slightly negative to take into account.
    * <br>
    * <br>
    * Called by {@link TableView#drawCell(GraphicsX, int, int)}.
    * <br>
    * <br>
    * @param colAbs visible column absolute index.
    * @return
    */
   public int getOffsetXColAbs(int colAbs) {
      if (colAbs >= xs.length || colAbs < 0) {
         throw new IllegalArgumentException("Array Input Value " + colAbs + " not valid for array length " + xs.length);
      }
      return xs[colAbs];
   }

   public int getOffsetYRowAbs(int rowAbs) {
      if (rowAbs >= ys.length || rowAbs < 0) {
         throw new IllegalArgumentException("Array Input Value " + rowAbs + " not valid for array length " + ys.length);
      }
      return ys[rowAbs];
   }

   /**
    * 
    * @param numTotalCols number of columns
    * @param gridSizeV size of vertical grid lines
    * @param startPixelAbs first pixel visible in the coordinate system of cells
    * @param lastPixelAbs last pixel visible in the coordinate system of cells
    */
   public void matchX(int numTotalCols, int gridSizeV, int startPixelAbs, int lastPixelAbs) {
      int visibleCount = 0;
      int pixelCount = 0;
      boolean notFoundFirst = false; //flag for telling if first cell has been found
      int rowCount = 0;
      //iterate over columns size
      for (int i = 0; i < numTotalCols; i++) {
         pixelCount += workColSizes[i];
         if (pixelCount > startPixelAbs) {
            rowCount++;
            if (notFoundFirst) {
               //first visible row
               firstColAbs = i;
               partiallyVisibleColAbs = i;
               undrawnWLeft = pixelCount;
            } else {
               visibleCount++;
               int diff = pixelCount + gridSizeV + workColSizes[i + 1] - lastPixelAbs;
               if (diff > 0) {
                  //last visible row
                  numVisibleCols = rowCount + 1;
                  partiallyVisibleColAbs2 = i + 1;
                  lastColAbs = i + 1;
                  undrawnWRight = diff;
                  break;
               } else if (diff == 0) {
                  numVisibleCols = rowCount;
                  lastColAbs = i;
                  break;
               }
            }
         }
         pixelCount += gridSizeV;
      }
      numVisibleCols = visibleCount;
      lastColAbs = firstColAbs + numVisibleCols - 1;
   }

   public void matchY(int numTotalRows, int gridSizeH, int startPixel, int endPixel) {
      int visibleCount = 0;
      int pixelCount = 0;
      boolean notFoundFirst = false;
      int rowCount = 0;
      for (int i = 0; i < numTotalRows; i++) {
         pixelCount += workRowSizes[i];
         if (pixelCount > startPixel) {
            rowCount++;
            if (notFoundFirst) {
               //first visible row
               firstRowAbs = i;
               partiallyVisibleRowAbs = i;
               undrawnHTop = pixelCount;
            } else {
               visibleCount++;
               int diff = pixelCount + gridSizeH + workRowSizes[i + 1] - endPixel;
               if (diff > 0) {
                  //last visible row
                  numVisibleRows = rowCount + 1;
                  partiallyVisibleRowAbs2 = i + 1;
                  lastRowAbs = i + 1;
                  undrawnHBottom = diff;
                  break;
               } else if (diff == 0) {
                  numVisibleRows = rowCount;
                  lastRowAbs = i;
                  break;
               }
            }
         }
         pixelCount += gridSizeH;
      }
      numVisibleRows = visibleCount;
      lastRowAbs = firstRowAbs + numVisibleRows - 1;
   }

   public void shiftXY(int dx, int dy) {
      if (dx != 0) {
         for (int i = 0; i < xs.length; i++) {
            xs[i] += dx;
         }
      }
      if (dy != 0) {
         for (int i = 0; i < ys.length; i++) {
            ys[i] += dy;
         }
      }
   }

   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx sb) {
      sb.root(this, "Config");
      String lmaster = "LeftMaster";
      if (!useLeftMaster) {
         lmaster = "RightMaster";
      }
      String tmaster = "TopMaster";
      if (!useTopMaster) {
         tmaster = "BottomMaster";
      }
      sb.append(lmaster + ":" + tmaster);
      sb.nl();
      sb.append("Root (" + rootColAbs + "," + rootRowAbs + ") ");
      sb.append("Selected (" + selectedColAbs + "," + selectedRowAbs + ")");
      sb.nl();
      sb.append("First (" + firstColAbs + "," + firstRowAbs + ") ");
      sb.append("Last (" + lastColAbs + "," + lastRowAbs + ")");
      sb.nl();
      sb.append("NumVisible (" + numVisibleCols + "," + numVisibleRows + ") ");
      sb.append("NumFullyVisible (" + numFullyVisibleCols + "," + numFullyVisibleRows + ") ");
      sb.append("IndexPartiallyVisibles (" + partiallyVisibleColAbs + "," + partiallyVisibleRowAbs + ")" + ",(" + partiallyVisibleColAbs2 + "," + partiallyVisibleRowAbs2 + ")");
      sb.nl();
      sb.nlFrontTitle(workColSizes, "Work Col Sizes");
      sb.nlFrontTitle(workRowSizes, "Work Row Sizes");
      sb.append(" Pixel Consumed=" + consumedWidth + "," + consumedHeight);
      sb.nl();
      sb.append("undrawnWLeft=" + undrawnWLeft + " undrawnHTop=" + undrawnHTop + " undrawnWRight=" + undrawnWRight + " undrawnHBottom=" + undrawnHBottom);
      sb.nlFrontTitle(xs, "X Coordinates");
      sb.nlFrontTitle(ys, "Y Coordinates");

   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root(this, "DrawableSizer");
   }

   public UCtx toStringGetUCtx() {
      return gc.getUCtx();
   }
   //#enddebug
}