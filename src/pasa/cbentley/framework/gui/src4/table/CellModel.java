package pasa.cbentley.framework.gui.src4.table;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.utils.BitUtils;
import pasa.cbentley.core.src4.utils.IntUtils;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.ScrollConfig;
import pasa.cbentley.framework.gui.src4.core.ViewDrawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ObjectGC;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOCellPolicy;
import pasa.cbentley.framework.gui.src4.table.interfaces.ITableUtils;
import pasa.cbentley.framework.gui.src4.table.interfaces.ITechCell;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;
import pasa.cbentley.framework.gui.src4.utils.DrawableUtilz;

/**
 * ViewModel + some business controller stuff like selectability, titles
 * @author Charles-Philip Bentley
 *
 */
public class CellModel extends ObjectGC implements IStringable, ITechCell, IBOCellPolicy, ITableUtils {

   int              consumedPixels;

   int              firstCellAbs;

   /**
    * Cell flags. 
    * <li> Selectability : {@link CellModel#CELL_FLAG_1_UNSELECTABLE} 
    */
   int[]            flags;

   /**
    * SET,AVERAGE,STRONG = [0,2,2,1]
    * SET,AVERAGE,STRONG,STRONG = [0,2,2,1]
    * SET,AVERAGE,STRONG,SET,WEAK,SET = [0,2,2,1,3,3]
    * 
    * index of first cell in frame
    */
   int[]            frames;

   /**
    * Coalesce some information about the cell model
    */
   int              helperFlags;

   /**
    * The number of increments difference between old selection and new selection
    * <br>
    */
   public int       incrementChange          = 0;

   /**
    * 
    */
   int              lastCellAbs;

   int              numCells;

   int              numFrames;

   int              numFullVisibleCells;

   int              numVisibleCells;

   public int       oldSelectedCellAbs;

   /**
    * Always top left
    */
   int              partiallyVisibleCellAbs  = -1;

   /**
    * Always bot right
    */
   int              partiallyVisibleCellAbs2 = -1;

   /**
    * Index pointed by pointer
    */
   public int       pointedIndexAbs;

   /**
    * Inner Table Row Policies that govern behavior during updates. 
    * <br>
    * <br>
    * Array cannot be null.? if null only explicit? TODO <br>
    * <br>
    * Useful for variable sized rows. Valid values are : <br>
    * <li>{@link ITechCell#CELL_1_EXPLICIT_SET}. <br>
    * This is the usual value telling row heights are explicitely defined in
    * {@link TableView#setupRowSizes} Concerning policies, Row heights do not
    * depend on ScrollConfig start increment. <br>
    * When <br>
    * <br>
    * In the case of variable cell sizes, we have <li>
    * {@link ITechCell#CELL_5_FILL_WEAK} <li>
    * {@link ITechCell#CELL_4_FILL_AVERAGE} <li>
    * {@link ITechCell#CELL_2_RATIO} when ratio pixel size could not be set in
    * stone during the setup
    * 
    * <br>
    * <br>
    * <br>
    * {@link ITechCell#CELL_0_IMPLICIT_SET} has been converted to
    * {@link ITechCell#CELL_1_EXPLICIT_SET} with value computed. <br>
    * {@link ITechCell#CELL_3_FILL_STRONG} has been converted to ViewPort's
    * pixel height
    */
   int[]            policies;

   ByteObject       policy;

   int[]            positions;

   int              rootCellAbs;

   int              selectedCellAbs;

   /**
    * The vector change in Cell Units.
    * <br>
    * <li>When positive, we have a move to the right/bottom
    * <li>When negative, we have a move to the left/top
    */
   public int       selectionCellVectorChange;

   int              sepSize;

   ByteObject[]     setupSizers;

   /**
    * Setupsizes are computed by the {@link TableView#computeConfig(Config)}
    * and setup methods rooted in {@link TableView#setupSizes()}. <br>
    * <br>
    * That means -1 and 0 values are accepted when the setup does not know the size<br>
    * Cannot be null But setup does compute those values. So when {@link TableView#computeConfig(Config)} method returns, all values are positive.
    * <br>
    * <br>
    * Setup sizes are computed once for a {@link Config}.
    * <br>
    * Then when selection changes, {@link TableView#setupColSizes} are used as a basis to compute {@link Config#workColSizes}.
    * <br>
    * <br>
    * Selection changes possibly modifies a column's width when <li>not all
    * columns are visible <li>one or more columns have a policy like
    * {@link ITechCell#CELL_4_FILL_AVERAGE} or
    * {@link ITechCell#CELL_5_FILL_WEAK} <br>
    * 
    * <br>
    * For {@link ITechCell#TYPE_0_GENERIC} policies, {@link TableView#setupColSizes} have the values of {@link IBOCellPolicy#CELLP_FLAGZ_2_SIZES}.
    * <br>
    * For a column with policy {@link ITechCell#CELL_4_FILL_AVERAGE}, the setupSize is computed according to the root selected position. If not
    * visible, it is kept to 0
    */
   int[]            setupSizes;

   /**
    * Structural style for the row cosizes cells. 
    * <br>
    * Allows the creation of cyclical colored backgrounds. 
    * Example: 3 styles and 9 rows, 
    * <br>
    * Read from ?
    */
   ByteObject[]     styles                   = null;

   String[]         titles;

   int[]            titlesInt;

   /**
   * 
   */
   StringDrawable[] titleStringDrawables;

   /**
    * Container for the whole column/row of cell titles 
    */
   Drawable         titlesView;

   public int       transitionType;

   int              undrawnBotRight;

   /**
    * Pixels undrawn for the first logical increment. Not counting hidden increments and separators.
    */
   int              undrawnTopLeft;

   /**
    * Semantics with a {@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT} .
    * <br>
    * <br>
    * <li>true = left/top master => left/top most cell is always fully visible
    * <li>false = right/bot master => right/bottom most cell is always fully visible
    * <br>
    * <br>
    * When the selection is heading to the left => the root column is on the left.
    * <br>
    * When the selection is heading to the right => the root column will be the one on the right
    * <br>
    * <br>
    * Much needed when {@link ITechViewPane#VISUAL_1_PARTIAL} is set.
    * <br>
    * <br>
    * Used only when {@link CellModel#H}
    */
   public boolean   useMaster                = true;

   /**
    * Can be null?
    */
   int[]            workCellSizes;

   public CellModel(GuiCtx gc) {
      super(gc);
   }

   /**
    * Synchronizes CellModel with the ScrollConfig.
    * <br>
    * <br>
    * Called when {@link TableView} content is being drawn. The {@link ScrollConfig} was initialized by
    * {@link ViewDrawable#initScrollingConfig(ScrollConfig, ScrollConfig)}.
    * <br>
    * Therefore any partially increments are known.
    * <br>
    * <br>
    * @param sc
    */
   public void callMatching(ScrollConfig sc) {
      if (sc != null) {
         // convert situation to the Config using the
         switch (sc.getTypeUnit()) {
            case ITechViewPane.SCROLL_TYPE_0_PIXEL_UNIT:
               matchPixel(sc);
               break;
            case ITechViewPane.SCROLL_TYPE_1_LOGIC_UNIT:
               matchLogic(sc);
               break;
            case ITechViewPane.SCROLL_TYPE_2_PAGE_UNIT:
               matchPage(sc);
               break;
            default:
               throw new IllegalArgumentException();
         }
      }
   }

   /**
    * Synchronizes {@link ScrollConfig} with selection state.
    * <br>
    * <br>
    * Compute the increment change the must occurs
    * <br>
    * Called when inside navigation occurs outside the fully visible cells.
    * <br>
    * @param sc
    * @return the number of increments to be moved
    */
   public void callMatchingReverse(ScrollConfig sc) {
      if (sc != null) {
         int type = sc.getTypeUnit();
         if (type == ITechViewPane.SCROLL_TYPE_0_PIXEL_UNIT) {
            if (partiallyVisibleCellAbs == selectedCellAbs) {
               incrementChange = -undrawnTopLeft;
            } else if (partiallyVisibleCellAbs2 == selectedCellAbs) {
               incrementChange = undrawnBotRight;
            } else if (transitionType == TRANSITION_2_OUTSIDE) {
               //assume difference of one
               incrementChange = workCellSizes[selectedCellAbs];
               incrementChange += sepSize;
               if (selectionCellVectorChange < 0) {
                  incrementChange = -incrementChange;
               }
            }

         } else if (type == ITechViewPane.SCROLL_TYPE_1_LOGIC_UNIT) {
            if (partiallyVisibleCellAbs == selectedCellAbs) {
               if (selectionCellVectorChange > 0) {
                  // now partial cells are computed bottom up
                  useMaster = false;
                  partiallyVisibleCellAbs = firstCellAbs;
               } else {
                  useMaster = true;
                  partiallyVisibleCellAbs = lastCellAbs;
               }
            } else {
               incrementChange = selectionCellVectorChange;
            }
         }
      }
   }

   public void callMatchNull() {
      firstCellAbs = 0;
      lastCellAbs = numCells - 1;
      numVisibleCells = numCells;
      numFullVisibleCells = numCells;
      undrawnTopLeft = 0;
   }

   public Object clone() {
      CellModel cm = new CellModel(gc);
      cm.consumedPixels = this.consumedPixels;
      cm.firstCellAbs = this.firstCellAbs;
      cm.flags = this.flags;
      cm.frames = this.frames;
      cm.lastCellAbs = this.lastCellAbs;
      cm.numCells = this.numCells;
      cm.numVisibleCells = this.numVisibleCells;
      cm.partiallyVisibleCellAbs = this.partiallyVisibleCellAbs;
      cm.partiallyVisibleCellAbs2 = this.partiallyVisibleCellAbs2;
      cm.positions = new int[numCells];
      cm.undrawnBotRight = this.undrawnBotRight;
      cm.undrawnTopLeft = this.undrawnTopLeft;
      cm.useMaster = this.useMaster;
      cm.rootCellAbs = this.rootCellAbs;
      cm.sepSize = this.sepSize;

      cm.transitionType = transitionType;
      cm.undrawnBotRight = undrawnBotRight;
      cm.undrawnTopLeft = undrawnTopLeft;
      cm.useMaster = useMaster;
      cm.workCellSizes = this.workCellSizes;
      return cm;
   }

   public void computeCoordinates() {
      // System.out.println(u);
      int counter = -undrawnTopLeft;
      for (int i = firstCellAbs; i <= lastCellAbs; i++) {
         if (i < positions.length) {
            positions[i] = counter;
            counter += workCellSizes[i] + sepSize;
         }
      }
   }

   /**
    * 
    * @param root
    * @param master true, increase index count. else decrease.
    * @param setupCellSizes
    * @param totalSize
    * @param sepSize
    * @param policies
    * @param workSizes
    */
   public void computeFillCellSize(int root, boolean master, int[] workSizes, int[] setupCellSizes, int[] policies, int num, int totalSize, int sepSize) {
      // maximum number of cells that can be displayed
      int maxNumCells = computeMaxSemanticsCellsNum(root, master, policies, num);
      int[] ts = new int[] { 0, 0, 0, -1 };
      // inspect the root 0 for separator size because separator is n-1
      inspectFill(ts, root, totalSize, policies, setupCellSizes, sepSize);
      // System.out.println("#computeFromSetupFromRoot root=" + root +
      // " finalSizes.length=" + finalSizes.length);
      workSizes[root] = TableViewUtils.getArrayFirstValOrIndex(setupCellSizes, root);
      int count = 1;
      while (ts[TRACK_1END] == 0 && count < maxNumCells) {
         int indexAbs = (master) ? root + count : root - count;
         inspectFill(ts, indexAbs, totalSize, policies, setupCellSizes, sepSize);
         if (ts[TRACK_1END] == 0) {
            workSizes[indexAbs] = TableViewUtils.getArrayFirstValOrIndex(setupCellSizes, indexAbs);
         }
         count++;
      }
      // what to do with the space left?
      int ndiff = totalSize - ts[TRACK_2SIZE_USED];
      // give it to the fill index for Draw Filler WEAK/AVERAGE
      int cellFillIndex = ts[TRACK_3FILL_INDEX];
      if (cellFillIndex != -1) {
         // give the left over pixels to the FILL column
         workSizes[cellFillIndex] = ndiff;
      } else {
         // should not arrive it
         throw new IllegalStateException("Only Policies with FILL may call this method");
      }
   }

   /**
    * Counts the number of frames semantically. Not Counting pixels.
    * <br>
    * <br>
    * @param policies
    * @return
    */
   public int computeFramesNumber(int[] policies) {
      return 1;
   }

   /**
    * 
    * 
    * Iterate over policies from index, going up or down depending on sign.
    * <br>
    * <br>
    * In the absence of frame definition
    * Return the maximum number of cols/rows that can be displayed based on
    * policy semantics. <br>
    * <br>
    * The method doesn't look at pixel size, but only at the <b>semantics</b>. 
    * <br>
    * <br>
    * That means <br>
    * <li> {@link ITechCell#CELL_3_FILL_STRONG} will allow only 1 cell maximum.
    * <li> {@link ITechCell#CELL_4_FILL_AVERAGE} will allow only 2 cells maximum. 
    * <li> {@link ITechCell#CELL_5_FILL_WEAK} will allow only 3 cells maximum.
    * <br>
    * <br>
    * For policy configuration with no FILL type, return the number of columns/rows. 
    * <br>
    * <br>
    * Max number of rows from start row in argument going up or down. 
    * <br>
    * <br>
    * Computes the number of visible cells horizontally (<code>row</code> = false) and verically (<code>row</code> = true).
    * <br>
    * <br>
    * 
    * @param policies not null. the policies of each columns or each rows.
    * @param numTotal
    *            total number of columns or rows
    * @param row
    *            defines the axis. true if working for row, false for columns
    * @return the maximum number of cells displayable at that config
    */
   public int computeMaxSemanticsCellsNum(int rootAbs, boolean drawMaster, int[] policies, int numTotal) {
      // first check root which must be displayed
      int[] cp = new int[] { 0, 0, 0 };
      //      inspect(cp, rootAbs, policies);
      //      if (cp[TRACK_1END] == 1) {
      //         return cp[TRACK_0COUNT];
      //      } else {
      //         int finishValue = 0;
      //         int counterValue = 0;
      //         int add = 0;
      //         if (drawMaster) {
      //            counterValue = rootAbs + 1;
      //            finishValue = policies.length;
      //            add = 1;
      //         } else {
      //            counterValue = rootAbs - 1;
      //            finishValue = -1;
      //            add = -1;
      //         }
      //         while (counterValue != finishValue) {
      //            inspect(cp, counterValue, policies);
      //            if (cp[TRACK_1END] == 1) {
      //               return cp[TRACK_0COUNT];
      //            }
      //
      //            counterValue += add;
      //         }
      //         return cp[TRACK_0COUNT];
      //      }

      if (drawMaster) {
         for (int i = rootAbs; i < policies.length; i++) {
            inspect(cp, i, policies);
            if (cp[TRACK_1END] == 1) {
               return cp[TRACK_0COUNT];
            }
         }

      } else {
         for (int i = rootAbs; i >= 0; i--) {
            inspect(cp, i, policies);
            if (cp[TRACK_1END] == 1) {
               return cp[TRACK_0COUNT];
            }
         }
      }
      return cp[TRACK_0COUNT];
   }

   /**
    * In Given CellModel, how many cell can be semantically displayed.
    * <br>
    * <br>
    * Takes in account
    * <li> root absolute position 
    * <li>drawMaster direction (true = from left to right),
    * <li>policies
    * <li>total number of cells
    * <li>frames definitions
    * <br>
    * <br>
    * @return
    */
   public int computeMaxSemanticsCellsNumCheck() {
      if (policies.length == 1 || IntUtils.isOnly(policies, ITechCell.CELL_1_EXPLICIT_SET)) {
         if (useMaster) {
            // the maximum number of columns
            return numCells - rootCellAbs;
         } else {
            return rootCellAbs + 1;
         }
      } else {
         if (frames == null) {
            return computeMaxSemanticsCellsNum(rootCellAbs, useMaster, policies, numCells);
         } else {

            for (int i = 0; i < frames.length; i += 2) {
               int startFrame = frames[i];
               int lenFrane = frames[i + 1];
               if (rootCellAbs >= startFrame && rootCellAbs < startFrame + lenFrane) {
                  return lenFrane;
               }
            }
            throw new IllegalArgumentException("Bad Frame defintion");
         }
      }
   }

   /**
    * From root absolute position and drawMaster direction (true = from left to right),
    * the policies
    * @param rootAbs
    * @param drawMaster
    * @param policies
    * @param numTotal
    * @return
    */
   public int computeMaxSemanticsCellsNumCheck(int rootAbs, boolean drawMaster, int[] policies, int numTotal) {

      if (policies.length == 1 || IntUtils.containsOnly(policies, ITechCell.CELL_1_EXPLICIT_SET)) {
         if (drawMaster) {
            // the maximum number of columns
            return numTotal - rootAbs;
         } else {
            return rootAbs + 1;
         }
      } else {
         return computeMaxSemanticsCellsNum(rootAbs, drawMaster, policies, numTotal);
      }

      // first check root which must be displayed
      //      int[] cp = new int[] { 0, 0, 0 };
      //      inspect(cp, rootAbs, policies);
      //      if (cp[TRACK_1END] == 1) {
      //         return cp[TRACK_0COUNT];
      //      } else {
      //         int finishValue = 0;
      //         int counterValue = 0;
      //         int add = 0;
      //         if (drawMaster) {
      //            counterValue = rootAbs + 1;
      //            finishValue = policies.length;
      //            add = 1;
      //         } else {
      //            counterValue = rootAbs - 1;
      //            finishValue = -1;
      //            add = -1;
      //         }
      //         while (counterValue != finishValue) {
      //            inspect(cp, counterValue, policies);
      //            if (cp[TRACK_1END] == 1) {
      //               return cp[TRACK_0COUNT];
      //            }
      //   
      //            counterValue += add;
      //         }
      //         return cp[TRACK_0COUNT];
      //      }
   }

   /**
    * 
    * Iterate over policies array starting from {@link Config#rootColAbs} index, going up or down depending on sign.
    * <br>
    * <br>
    * Return the maximum number of cols/rows that can be displayed based on policy semantics. Does not care about pixels.
    * <br>
    * <br>
    * That means <br>
    * <li> {@link ITechCell#CELL_3_FILL_STRONG} will allow only 1 cell maximum.
    * <li> {@link ITechCell#CELL_4_FILL_AVERAGE} will allow only 2 cells maximum. 
    * <li> {@link ITechCell#CELL_5_FILL_WEAK} will allow only 3 cells maximum. 
    * <br>
    * <br>
    * For policy configuration without types mentioned above, returns the number of columns/rows. 
    * <br>
    * <br>
    * Implements the special cases, see the specifications of {@link ITechCell#CELL_4_FILL_AVERAGE},{@link ITechCell#CELL_5_FILL_WEAK}
    * <br>
    * <br>
    * @param u {@link TableView.Config}
    * @param policies the policies of each columns/rows.
    * @param numTotal total number of columns/rows
    * @param row defines the axis. true if working for row, false for columns
    * @return the maximum number of cells displayable at that config
    */
   public int computeMaxSemanticsCellsNumRoot(Config u, int[] policies, int numTotal, boolean row) {
      if (policies == null) {
         throw new NullPointerException("No Policies");
      }
      if (u == null) {
         throw new NullPointerException("No Config");
      }
      int rootAbs = (row) ? u.rootRowAbs : u.rootColAbs;
      boolean drawMaster = (row) ? u.useTopMaster : u.useLeftMaster;
      return computeMaxSemanticsCellsNumCheck(rootAbs, drawMaster, policies, numTotal);
   }

   /**
    * Computes the size of all visible variable size cells.
    * <br>
    * <br>
    * Called every time a selection changes.
    * <br>
    * Some Table Policies will return immediately because cell sizes never change. 
    * <br>
    * <br>
    * @param c The new Configuration on which to update work sizes
    */
   public void computeMoveVariableCellSizes(int totalSize) {
      if (hasHelperFlag(CELL_H_FLAG_06_VARIABLE_MOVE_SIZE)) {
         //computeVariableColSizes(c);
         computeVariableCellSizes(totalSize);
      }
   }

   /**
    * Match reverse.
    * When selecting a partially visible cell,
    * <br>
    * Updates the root cell index.
    * @param selectedAbs
    */
   public void computeNotFullyVisible(int selectedAbs) {
      oldSelectedCellAbs = selectedCellAbs;
      selectedCellAbs = selectedAbs;
      // if vx is positive, we have a move right
      selectionCellVectorChange = selectedCellAbs - oldSelectedCellAbs;

      //depending on logic or pixel matching.
      if (partiallyVisibleCellAbs == selectedCellAbs) {
         if (selectionCellVectorChange > 0) {
            // now partial cells are computed bottom up
            useMaster = false;
            partiallyVisibleCellAbs = firstCellAbs;
         } else {
            useMaster = true;
            partiallyVisibleCellAbs = lastCellAbs;
         }
      }
      if (partiallyVisibleCellAbs2 == selectedCellAbs) {

      }
      if (selectionCellVectorChange != 0) {
         rootCellAbs = selectedCellAbs;
         if (selectionCellVectorChange > 0) {
            useMaster = false;
         } else {
            useMaster = true;
         }
      }
   }

   public void computeSetupFillCells(int total) {
      if (hasHelperFlag(CELL_H_FLAG_14_VARIABLE_SETUP_SIZE)) {
         //computeVariableColSizes(c);
         computeVariableCellSizes(total);
      }
   }

   /**
    * Computes the size of all visible variable size cells.
    * <br>
    * <br>
    * Called during the set up/reshape
    * @param totalSize
    */
   public void computeSetupVariableCellSizes(int totalSize) {
      if (hasHelperFlag(CELL_H_FLAG_14_VARIABLE_SETUP_SIZE)) {
         //computeVariableColSizes(c);
         computeVariableCellSizes(totalSize);
      }
   }

   /**
    * Method deals when cell column sizes change depending on {@link ScrollConfig}.
    * <br>
    * <br>
    * <li> {@link ITechCell#CELL_4_FILL_AVERAGE}
    * <li> {@link ITechCell#CELL_5_FILL_WEAK}
    * <br>
    * <br>
    * 
    * <br>
    * <br>
    * Take into account the frames.
    * <br>
    * {@link Config} first column is used as a basis. <br>
    * <br>
    * <b>POST</b> 
    * <li>WorkSize for fill column is computed
    * 
    * <br>
    * <br>
    * @param cellModel {@link Config}
    */
   public void computeVariableCellSizes(int totalSize) {
      int root = this.rootCellAbs;
      int[] setupSizes = this.setupSizes;
      int[] workSizes = this.workCellSizes;
      int separatorSize = this.sepSize;
      int[] policies = this.policies;
      boolean master = this.useMaster;
      // maximum number of cells that can be displayed semantically
      int maxNumCells = this.computeMaxSemanticsCellsNumCheck();
      int[] ts = new int[] { 0, 0, 0, -1 };
      // inspect the root 0 for separator size because separator is n-1
      inspectFill(ts, root, totalSize, policies, setupSizes, separatorSize);
      // System.out.println("#computeFromSetupFromRoot root=" + root + " finalSizes.length=" + finalSizes.length);
      workSizes[root] = getArrayFirstValOrIndex(setupSizes, root);
      int count = 1;
      //TODO problem when totalsize is small, TRACK_END finish before reaching FILL Column
      while (ts[TRACK_1END] == 0 && count < maxNumCells) {
         int indexAbs = (master) ? root + count : root - count;
         inspectFill(ts, indexAbs, totalSize, policies, setupSizes, separatorSize);
         if (ts[TRACK_1END] == 0) {
            workSizes[indexAbs] = getArrayFirstValOrIndex(setupSizes, indexAbs);
         }
         count++;
      }
      // what to do with the space left?
      int ndiff = totalSize - ts[TRACK_2SIZE_USED];
      // give it to the fill index for Draw Filler WEAK/AVERAGE
      int cellFillIndex = ts[TRACK_3FILL_INDEX];
      if (cellFillIndex != -1) {
         // give the left over pixels to the FILL column
         workSizes[cellFillIndex] = ndiff;
      } else {
         //for example in SET FILL, TableView is too small to even display SET.
         // should not arrive it.. TODO but when well it could 
         //do nothing right now
         //throw new IllegalStateException("Only Policies with FILL may call this method");
      }
   }

   public void debugHelperFlag(Dctx sb, int flag, String str) {
      if (hasHelperFlag(flag)) {
         sb.append(' ');
         sb.append(str);
      }
   }

   /**
    * Return the Number of increments.
    * <br>
    * Computes the number of increment change and their pixel values, including separator?
    * @param sc
    * @param size
    * @return
    */
   public int doUpdate(ScrollConfig sc, int size) {
      callMatchingReverse(sc);
      computeMoveVariableCellSizes(size); //only happens if there is a ViewPane
      if (incrementChange != 0 && transitionType != TRANSITION_0_INSIDE_VIEWPORT) {
         //move the scroll config vx increments
         return incrementChange;
      }
      return 0;
   }

   /**
    * Gets the integer value are index i.
    * <br>
    * <br>
    * only exception is if array is of length 1. it returns value at index 0
    * When index is bigger than array, return last value
    * <br>
    * <br>
    * @param sizes
    * @param i
    * @return integer at i or 0
    * @throws IndexOutOfBoundsException
    *             if i is too big
    */
   public int getArrayFirstValOrIndex(int[] sizes, int i) {
      if (sizes.length == 0) {
         throw new IllegalArgumentException("No Sizes for i=" + i);
      }
      if (i >= sizes.length) {
         return sizes[sizes.length - 1];
      }
      return sizes[i];
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
   public int getNumCellsSize(int numCells) {
      int total = 0;
      int count = 0;
      for (int i = firstCellAbs; i < workCellSizes.length; i++) {
         total += workCellSizes[i];
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
    * @param cellAbs visible column absolute index.
    * @return
    */
   public int getOffsetCellAbs(int cellAbs) {
      if (cellAbs >= positions.length || cellAbs < 0) {
         throw new IllegalArgumentException("Array Input Value " + cellAbs + " not valid for array length " + positions.length);
      }
      return positions[cellAbs];
   }

   public ByteObject getPolicy() {
      return policy;
   }
   public int getScrollTotal() {
      if (frames != null) {
         return frames.length / 2;
      }
      return numCells;
   }

   public int getSize() {
      return numCells;
   }

   /**
    * No cyclical?
    * @param index
    * @return
    */
   public ByteObject getStyle(int index) {
      if (styles != null) {
         index = index & styles.length;
         return styles[index];
      }
      return null;
   }

   /**
    * Cell Helper Flags
    * @param flag
    * @return
    */
   public boolean hasHelperFlag(int flag) {
      return BitUtils.hasFlag(helperFlags, flag);
   }

   public void initWorkSizes() {
      if (positions == null || positions.length != numCells) {
         positions = new int[numCells];
         workCellSizes = new int[numCells];
      }
      for (int i = 0; i < numCells; i++) {
         workCellSizes[i] = TableViewUtils.getArrayFirstValOrIndex(setupSizes, i);
      }

   }

   /**
    * Inspect column policies to deal policies : 
    * <li>{@link ITechCell#CELL_4_FILL_AVERAGE}, 
    * <li>{@link ITechCell#CELL_5_FILL_WEAK}
    * <li>{@link ITechCell#CELL_3_FILL_STRONG} 
    * <br>
    * Only one fill is accepted in a slate. 
    * <br>
    * <br>
    */
   public void inspect(int[] cp, int rowAbs, int[] policies) {
      int pol = policies[rowAbs];
      if (pol == ITechCell.CELL_1_EXPLICIT_SET || pol == ITechCell.CELL_0_IMPLICIT_SET) {
         //semantically, they can always be added unless
         cp[TRACK_0COUNT]++;
      } else if (pol == ITechCell.CELL_3_FILL_STRONG) {
         //fill strongs are always 1 frame
         if (cp[TRACK_0COUNT] == 0) {
            cp[TRACK_0COUNT] = 1;
         }
         cp[TRACK_1END] = 1;
      } else if (pol == ITechCell.CELL_4_FILL_AVERAGE) {
         if (cp[TRACK_2FILL_ALREADY] == 1) {
            cp[TRACK_1END] = 1;
         } else {
            if (cp[TRACK_0COUNT] != 0) {
               // we already have one Set, and only accept one adjacent
               cp[TRACK_1END] = 1;
            }
            cp[TRACK_2FILL_ALREADY] = 1;
            cp[TRACK_0COUNT]++;
         }

      } else if (pol == ITechCell.CELL_5_FILL_WEAK) {
         // continue until no more
         if (cp[TRACK_2FILL_ALREADY] == 1) {
            cp[TRACK_1END] = 1;
         } else {
            cp[TRACK_2FILL_ALREADY] = 1;
            cp[TRACK_0COUNT]++;
            // continue until we meet another fill
         }
      } else if (pol == ITechCell.CELL_2_RATIO) {
         cp[TRACK_0COUNT]++;
      } else {
         throw new RuntimeException();
      }

   }

   /**
    * Update integer array with information about the cell.
    * 
    * @param ts
    * @param index
    * @param policies
    * @param sizes
    * @param sepSize
    * @param totalSize
    * @param partial
    */
   public void inspectFill(int[] ts, int index, int totalSize, int[] policies, int[] sizes, int sepSize) {
      int pol = TableViewUtils.getArrayFirstValOrIndex(policies, index);
      int mySize = TableViewUtils.getArrayFirstValOrIndex(sizes, index);
      if (pol == ITechCell.CELL_3_FILL_STRONG) {
         ts[TRACK_1END] = 1;
         if (ts[TRACK_0COUNT] == 0) {
            ts[TRACK_3FILL_INDEX] = index;
            ts[TRACK_0COUNT]++;
         }
      } else if (pol == ITechCell.CELL_5_FILL_WEAK || pol == ITechCell.CELL_4_FILL_AVERAGE) {
         ts[TRACK_3FILL_INDEX] = index;
         // final size is still unknown but already had size of separator
         ts[TRACK_2SIZE_USED] += sepSize;
         ts[TRACK_0COUNT]++;
      } else {
         // not a FILL cell
         int newSize = ts[TRACK_2SIZE_USED];
         int spaceNeeded = mySize + sepSize;
         if (newSize + spaceNeeded > totalSize) {
            ts[TRACK_1END] = 1;
         } else {
            ts[TRACK_2SIZE_USED] += spaceNeeded;
            ts[TRACK_0COUNT]++;
         }
      }
   }

   /**
    * Just a forward. Tile String will basically update their styles
    * @param ic
    * @return
    */
   public boolean managePointerInputForward(ExecutionContextCanvasGui ec) {
      for (int j = firstCellAbs; j <= lastCellAbs; j++) {
         if (DrawableUtilz.isInside(ec, titleStringDrawables[j])) {
            titleStringDrawables[j].managePointerInput(ec);
            return true;
         }
      }
      return false;
   }

   /**
    * Matches a {@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT} {@link ScrollConfig} to {@link TableView} drawing state.
    * <br>
    * <br>
    * TODO frame matching
    * @param scX
    * @param scroll
    */
   private void matchLogic(ScrollConfig scX) {
      if (frames != null) {
         throw new IllegalArgumentException("Frames should be set to PAGE INCREMENT");
      } else {
         firstCellAbs = scX.getSIStart();
         numVisibleCells = scX.getSIVisible();
         numFullVisibleCells = scX.getSIVisibleFully();
         // compute visible and not fully visible
         lastCellAbs = firstCellAbs + numVisibleCells - 1;
         if (scX.isPartial()) {
            int undrawn = scX.getPartialOffset();
            int type = scX.getPartialType();
            if (type == ITechViewPane.PARTIAL_TYPE_0_BOTH) {
               if (useMaster) {
                  partiallyVisibleCellAbs2 = lastCellAbs;
                  undrawnBotRight = undrawn;
               } else {
                  partiallyVisibleCellAbs = firstCellAbs;
                  undrawnTopLeft = undrawn;
               }
            } else if (type == ITechViewPane.PARTIAL_TYPE_1_TOP) {
               partiallyVisibleCellAbs2 = lastCellAbs;
               undrawnBotRight = undrawn;
            } else {
               partiallyVisibleCellAbs = firstCellAbs;
               undrawnTopLeft = undrawn;
            }
         }
      }
   }

   /**
    * Match Page
    * @param sc
    */
   private void matchPage(ScrollConfig sc) {
      if (frames != null) {
         int numVisibles = sc.getSIVisible();
         int frameNum = sc.getSIStart();
         int index = frameNum * 2;
         firstCellAbs = frames[index];
         int numVisibleCells = frames[index + 1];
         //check the number of visible cells for all visible frames/pages
         for (int i = 1; i < numVisibles; i++) {
            numVisibleCells += frames[index + 1 + FRAME_INSIDE_LENGTH_2];
         }
         lastCellAbs = firstCellAbs + numVisibleCells - 1;
      } else {
         //TODO
      }
   }

   /**
    * Match the {@link ScrollConfig} to cell {@link Config} so it can be drawn.
    * <br>
    * <br>
    * Inverse function of what is done in {@link TableView#initScrollingConfig(ScrollConfig, ScrollConfig)} with {@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT}.
    * Computes the first.
    * <br>
    * How inner selection moves impact the pixel.
    * <br>
    * In effect computes number of visible cells.
    * TODO when user do pixel scrolling, and then navigation selection, tableview centers back selection
    * <br>
    * <br>
    * @param sc checked as being NOT null
    * @param scroll
    */
   private void matchPixel(ScrollConfig sc) {
      int startPixel = sc.getSIStart();
      int totalNumPixels = sc.getSIVisible();
      int lastPossiblePixel = startPixel + sc.getSIVisible();
      int pixelCount = 0;
      boolean firstCellNotFound = true; //flag for telling if first cell has been found
      int cellCount = 0;
      undrawnBotRight = 0;
      undrawnTopLeft = 0;
      partiallyVisibleCellAbs = -1;
      partiallyVisibleCellAbs2 = -1;

      int pixelsConsumed = 0;
      //iterate over the cells. read cell size. find the first cell to be displayed
      //from that point, count the number of cells that's possible to show
      for (int i = 0; i < numCells; i++) {
         int cellSize = workCellSizes[i];
         pixelCount += cellSize;
         if (pixelCount >= startPixel) {
            cellCount++;
            if (firstCellNotFound) {
               //first cell to be shown
               int drawn = pixelCount - startPixel;
               //first visible row
               firstCellAbs = i;
               undrawnTopLeft = cellSize - drawn;
               if (drawn != cellSize) {
                  partiallyVisibleCellAbs = i;
               }
               pixelsConsumed = drawn + sepSize;
               firstCellNotFound = false;
            } else {
               //see if the current cell fits in the available pixels left
               //when <= we have found how last cell.
               int numPixelsLeft = totalNumPixels - pixelsConsumed - cellSize;
               if (numPixelsLeft > 0) {
                  //continue the for loop. increment counter with pixel cell size and here also add separator size
                  pixelsConsumed += (cellSize + sepSize);
               } else if (numPixelsLeft < 0) {
                  undrawnBotRight = 0 - numPixelsLeft + sepSize;
                  partiallyVisibleCellAbs2 = i;
                  break;
               } else if (numPixelsLeft == 0) {
                  break;
               }
            }
         }
         pixelCount += sepSize;
      }
      numVisibleCells = cellCount;
      lastCellAbs = firstCellAbs + numVisibleCells - 1;

      //System.out.println("#CellModel matchingPixel " + this.toString());
   }

   public void setHelperFlag(int flag, boolean v) {
      helperFlags = BitUtils.setFlag(helperFlags, flag, v);
   }

   /**
    * Tells
    * @param selectedAbs
    */
   public void setNewSelection(int selectedAbs) {
      oldSelectedCellAbs = selectedCellAbs;
      selectedCellAbs = selectedAbs;

      // if vx is positive, we have a move right
      selectionCellVectorChange = selectedCellAbs - oldSelectedCellAbs;
      int transType = TRANSITION_0_INSIDE_VIEWPORT;
      if (selectedAbs < firstCellAbs || selectedAbs > lastCellAbs) {
         transType = TRANSITION_2_OUTSIDE;
      } else if (partiallyVisibleCellAbs == selectedAbs || partiallyVisibleCellAbs2 == selectedAbs) {
         transType = TRANSITION_1_INSIDE_TO_PARTIAL;
      }
      transitionType = transType;
   }

   public void setNumCells(int num) {
      if (num < 0)
         throw new IllegalArgumentException("num cells = " + num);
      numCells = num;
   }

   public void setSelectable(int index, boolean v) {
      flags[index] = BitUtils.setFlag(flags[index], CELL_FLAG_1_UNSELECTABLE, v);
      setHelperFlag(CELL_H_FLAG_27_UNSELECTABLE, true);
   }

   public void setStyles(ByteObject[] styles) {
      this.styles = styles;
   }

   public void setTitles(String[] titles) {
      this.titles = titles;
   }

   //#mdebug

   public void setupTitles() {
      //titles are hardcoded or localized

      if (titles == null) {
         titles = new String[numCells];
         for (int i = 0; i < numCells; i++) {
            titles[i] = String.valueOf(i + 1);
         }
      }
      if (titleStringDrawables == null) {
         titleStringDrawables = new StringDrawable[numCells];

      }
      // initialize column titles.
   }

   public void shift(int val) {
      if (val != 0) {
         for (int i = 0; i < positions.length; i++) {
            positions[i] += val;
         }
      }
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, CellModel.class, 1192);
      toStringPrivate(dc);
      super.toString(dc.sup());

      dc.nl();
      dc.appendVarWithSpace("sepSize", sepSize);
      dc.appendVarWithSpace("Pixels Consumed", consumedPixels);
      dc.nl();
      dc.append("First=" + firstCellAbs + " last=" + lastCellAbs + " root=" + rootCellAbs + " master=" + useMaster);
      dc.nl();
      dc.append("selected=" + selectedCellAbs);
      dc.append(" oldselected=" + oldSelectedCellAbs);
      dc.nl();
      dc.append("partialIndex [" + partiallyVisibleCellAbs + "," + partiallyVisibleCellAbs2 + "]");
      dc.nl();
      dc.append("undrawn=[" + undrawnTopLeft + "<->" + undrawnBotRight + "]");
      dc.nlFrontTitle(positions, "Coordinates");
      dc.nlFrontTitle(workCellSizes, "Work Cell Sizes");
      dc.nlFrontTitle(setupSizes, "Setup Cell Sizes");
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, CellModel.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   public void toStringHelperFlags(Dctx sb) {
      debugHelperFlag(sb, CELL_H_FLAG_04_TOTAL_SET, "TotalSet");
      debugHelperFlag(sb, CELL_H_FLAG_06_VARIABLE_MOVE_SIZE, "VariableMoveSize");
      debugHelperFlag(sb, CELL_H_FLAG_08_ALL_CELLS_SAME_SIZE, "CellsSameSize");
      debugHelperFlag(sb, CELL_H_FLAG_10_OWN_NAVIGATION, "OwnNav");
      debugHelperFlag(sb, CELL_H_FLAG_12_EFFECTIVE_VARIABLE_SIZE, "EffectVariable");
      debugHelperFlag(sb, CELL_H_FLAG_14_VARIABLE_SETUP_SIZE, "VariableSetup");
      debugHelperFlag(sb, CELL_H_FLAG_17_OVERSIZE, "Oversize");

   }

   private void toStringPrivate(Dctx dc) {
      dc.appendVarWithSpace("numCells", numCells);
      dc.appendVarWithSpace("numVisibleCells", numVisibleCells);
      dc.appendVarWithSpace("numFullVisibleCells", numFullVisibleCells);
   }

   //#enddebug
}
