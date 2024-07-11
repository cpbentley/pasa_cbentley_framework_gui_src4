package pasa.cbentley.framework.gui.src4.factories;

import pasa.cbentley.byteobjects.src4.core.BOAbstractFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOCellPolicy;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOTablePolicy;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOTableSpan;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOTableView;
import pasa.cbentley.framework.gui.src4.table.interfaces.ITechCell;
import pasa.cbentley.layouter.src4.engine.SizerFactory;

public class TablePolicyFactory extends BOAbstractFactory implements IBOTablePolicy, IBOTableView {

   protected final GuiCtx gc;

   public TablePolicyFactory(GuiCtx gc) {
      super(gc.getBOC());
      this.gc = gc;
   }

   /**
    * Adds a spanning definition
    * @param tablePol
    * @param span
    */
   public void addSpanning(ByteObject tablePol, ByteObject span) {

   }

   public TableCellPolicyFactory getCellPolicyFactory() {
      return gc.getTableCellPolicyFactory();
   }

   /**
    * 
    * @param isVert
    * @param w
    * @param h
    * @return
    */
   public ByteObject getButtonLine(boolean isVert, int w, int h) {
      int numCol = 0;
      int numRow = 1;
      if (isVert) {
         numRow = 0;
         numCol = 1;
      }
      ByteObject policyCol = getCellPolicyFactory().getGeneric(numCol, w);
      ByteObject policyRow = getCellPolicyFactory().getGeneric(numRow, h);
      ByteObject policyTable = getTablePolicy(policyCol, policyRow);
      return policyTable;
   }

   /**
    * A flow of cells that start top left towards top right.
    * All cells have the w,h pixel size.
    * @param w -1-
    * @param h
    * @return {@link IBOTablePolicy}
    */
   public ByteObject createTableFlowHoriz(int w, int h) {
      //flow column
      ByteObject policyCol = getCellPolicyFactory().getFlow(w, true);
      policyCol.setFlag(IBOCellPolicy.CELLP_OFFSET_02_FLAG, IBOCellPolicy.CELLP_FLAG_7_OVERSIZE, false);
      //as many rows are wanted, one logical size
      ByteObject policyRow = getCellPolicyFactory().getGeneric(0, h);
      ByteObject policyTable = getTablePolicy(policyCol, policyRow);
      return policyTable;
   }

   /**
    * A flow of cells that start top left towards top right.
    * All cells have the w,h pixel size.
    * @param w -1-
    * @param h
    * @return {@link IBOTablePolicy}
    */
   public ByteObject getFlowHoriz(ByteObject sizerW, ByteObject sizerH) {
      //flow column
      ByteObject policyCol = getCellPolicyFactory().getFlow(sizerW, true);
      policyCol.setFlag(IBOCellPolicy.CELLP_OFFSET_02_FLAG, IBOCellPolicy.CELLP_FLAG_7_OVERSIZE, false);
      //as many rows are wanted, one logical size
      ByteObject policyRow = getCellPolicyFactory().getGeneric(0, sizerH);
      ByteObject policyTable = getTablePolicy(policyCol, policyRow);
      return policyTable;
   }

   /**
    * Implict,Weak,Implicit
    * With Etalon on the first cell for -1;
    * <br>
    * <br>
    * @return
    */
   public ByteObject getMenuPolicy() {
      int[] colPolicies = new int[] { ITechCell.CELL_0_IMPLICIT_SET, ITechCell.CELL_5_FILL_WEAK, ITechCell.CELL_0_IMPLICIT_SET };
      ByteObject policyCol = getCellPolicyFactory().getGeneric(colPolicies, null);
      ByteObject policyRow = getCellPolicyFactory().getGeneric(1, -1);
      ByteObject policyTable = getTablePolicy(policyCol, policyRow);
      return policyTable;
   }

   /**
    * Table policy for menus. [1 0 = 0 -1]
    * <br>
    * <br>
    * One column. Width of column is implicit but maxed to ViewPort
    * <br>
    * As many rows as needed but logical size of 1
    * <br>
    * @return
    */
   public ByteObject getMenuSubPolicy() {
      //one column
      ByteObject policyCol = getCellPolicyFactory().getGeneric(1, 0);
      policyCol.setFlag(IBOCellPolicy.CELLP_OFFSET_02_FLAG, IBOCellPolicy.CELLP_FLAG_7_OVERSIZE, false);
      //as many rows are wanted, one logical size
      ByteObject policyRow = getCellPolicyFactory().getGeneric(0, -1);
      ByteObject policyTable = getTablePolicy(policyCol, policyRow);
      return policyTable;
   }

   public ByteObject getMenuSubPolicy(int width, int heigh) {
      //one column
      ByteObject policyCol = getCellPolicyFactory().getGeneric(1, 0, 0, 0, width);
      //as many rows are wanted, one logical size
      ByteObject policyRow = getCellPolicyFactory().getGeneric(0, -1);
      ByteObject policyTable = getTablePolicy(policyCol, policyRow);
      return policyTable;
   }

   public ByteObject createTableXColsManyRows(int[] policies, int[] sizesCol, ByteObject sizerRow) {
      TableCellPolicyFactory cellPolicyFactory = getCellPolicyFactory();
      ByteObject policyCol = cellPolicyFactory.getGeneric(policies, sizesCol);
      policyCol.setFlag(IBOCellPolicy.CELLP_OFFSET_02_FLAG, IBOCellPolicy.CELLP_FLAG_7_OVERSIZE, false);
      //as many rows are wanted, one logical size
      ByteObject policyRow = cellPolicyFactory.getGeneric(0, sizerRow);
      ByteObject policyTable = getTablePolicy(policyCol, policyRow);
      return policyTable;
   }

   public ByteObject getSimpleXColsManyRows(int[] policies, int[] sizesCol) {
      TableCellPolicyFactory cellPolicyFactory = getCellPolicyFactory();
      ByteObject policyCol = cellPolicyFactory.getGeneric(policies, sizesCol);
      policyCol.setFlag(IBOCellPolicy.CELLP_OFFSET_02_FLAG, IBOCellPolicy.CELLP_FLAG_7_OVERSIZE, false);
      //as many rows are wanted, one logical size
      ByteObject policyRow = cellPolicyFactory.getGeneric(0, -1);
      ByteObject policyTable = getTablePolicy(policyCol, policyRow);
      return policyTable;
   }

   public ByteObject getSimple1RowManyCols() {
      ByteObject policyRow = getCellPolicyFactory().getGeneric(1, -1);
      policyRow.setFlag(IBOCellPolicy.CELLP_OFFSET_02_FLAG, IBOCellPolicy.CELLP_FLAG_7_OVERSIZE, false);
      //as many rows are wanted, one logical size
      ByteObject policyCol = getCellPolicyFactory().getGeneric(0, -1);
      ByteObject policyTable = getTablePolicy(policyCol, policyRow);
      return policyTable;
   }

   /**
    * 
    * @param x
    * @param sizeCol
    * @return
    */
   public ByteObject getSimpleXColsManyRows(int x, int sizeCol) {
      ByteObject policyCol = getCellPolicyFactory().getGeneric(x, sizeCol);
      policyCol.setFlag(IBOCellPolicy.CELLP_OFFSET_02_FLAG, IBOCellPolicy.CELLP_FLAG_7_OVERSIZE, false);
      //as many rows are wanted, one logical size
      ByteObject policyRow = getCellPolicyFactory().getGeneric(0, -1);
      ByteObject policyTable = getTablePolicy(policyCol, policyRow);
      return policyTable;
   }

   public ByteObject createTableXColsManyRows(int x, int sizeCol, int sizeRows) {
      ByteObject policyCol = getCellPolicyFactory().getGeneric(x, sizeCol);
      policyCol.setFlag(IBOCellPolicy.CELLP_OFFSET_02_FLAG, IBOCellPolicy.CELLP_FLAG_7_OVERSIZE, false);
      //as many rows are wanted, one logical size
      ByteObject policyRow = getCellPolicyFactory().getGeneric(0, sizeRows);
      ByteObject policyTable = getTablePolicy(policyCol, policyRow);
      return policyTable;
   }

   public ByteObject getSimpleXColsManyRows(int x) {
      return getSimpleXColsManyRows(x, 0);
   }

   public ByteObject getSimple2ColsManyRows() {
      return getSimpleXColsManyRows(2);
   }

   /**
    * TODO as getMenuSub. remove
    * @return
    */
   public ByteObject getSimple1ColPolicy() {
      return getSimpleXColsManyRows(1);
   }

   /**
    * Create a spanning object {@link IBOTableSpan}.
    * <p>
    * Col and Row values follow this rule
    * <li>value of 0 means no spanning
    * <li>value of 1 means the rest
    * <li>value of 2 or more is the actual
    * </p>
    * 
    * @param colAbs column coordinate for the spanning root
    * @param colValue number of columns spanned. see {@link IBOTableSpan#SPAN_OFFSET_3_COL_VALUE2}.
    * @param rowAbs row coordinate for the spanning root
    * @param rowValue number of rows spanned.
    * @return
    */
   public ByteObject getSpanning(int colAbs, int colValue, int rowAbs, int rowValue) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_05_CELL_SPANNING, IBOTableSpan.BASIC_SIZE_SPANNING);
      p.setValue(IBOTableSpan.SPAN_OFFSET_2_COL2, colAbs, 2);
      p.setValue(IBOTableSpan.SPAN_OFFSET_3_COL_VALUE2, colValue, 2);
      p.setValue(IBOTableSpan.SPAN_OFFSET_4_ROW2, rowAbs, 2);
      p.setValue(IBOTableSpan.SPAN_OFFSET_5_ROW_VALUE2, rowValue, 2);
      return p;
   }

   /**
    * No span specification
    * @param colPol
    * @param rowPol
    * @return {@link IBOTypesGui#TYPE_GUI_04_TABLE_POLICY}
    */
   public ByteObject getTablePolicy(ByteObject colPol, ByteObject rowPol) {
      return getTablePolicy(colPol, rowPol, null);
   }

   /**
    * 
    * @param colPol
    * @param rowPol
    * @param spans
    * @return {@link IBOTypesGui#TYPE_GUI_04_TABLE_POLICY}
    */
   public ByteObject getTablePolicy(ByteObject colPol, ByteObject rowPol, ByteObject[] spans) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_04_TABLE_POLICY, BASIC_SIZE_TABLE_POLICY);
      ByteObject[] ar = null;
      if (spans != null) {
         p.setFlag(TP_OFFSET_1FLAG, TP_FLAG_3_SPANNING, true);
         int num = spans.length;
         ar = new ByteObject[num + 2];
         ar[0] = colPol;
         ar[1] = rowPol;
         for (int i = 0; i < spans.length; i++) {
            ar[i + 2] = spans[i];
         }
      } else {
         ar = new ByteObject[] { colPol, rowPol };
      }
      p.setByteObjects(ar);
      return p;
   }

   /**
    * 
    * @return
    */
   public ByteObject createTableFlowHorizPrefContent() {
      TableCellPolicyFactory cellPolicyFac = getCellPolicyFactory();
      boolean isStrongRow = false;
      ByteObject sizerW = gc.getLAC().getSizerFactory().getSizerPref();
      ByteObject policyCol = cellPolicyFac.getFlow(sizerW, isStrongRow);
      policyCol.setFlag(IBOCellPolicy.CELLP_OFFSET_02_FLAG, IBOCellPolicy.CELLP_FLAG_7_OVERSIZE, false);
      //as many rows are wanted, one logical size
      ByteObject sizerH = gc.getLAC().getSizerFactory().getSizerPref();
      //TODO. sizerH is for content
      ByteObject policyRow = cellPolicyFac.getGeneric(0, sizerH);

      //first cell decides the size of all other cell.

      //option eahc cell can have its own
      ByteObject policyTable = getTablePolicy(policyCol, policyRow);
      return policyTable;
   }

   public ByteObject createTableFlowHorizLogic1() {
      TableCellPolicyFactory cellPolicyFac = getCellPolicyFactory();
      boolean isStrongRow = false;
      ByteObject sizerW = gc.getLAC().getSizerFactory().getSizerLogicUnit(1);
      ByteObject policyCol = cellPolicyFac.getFlow(sizerW, isStrongRow);
      policyCol.setFlag(IBOCellPolicy.CELLP_OFFSET_02_FLAG, IBOCellPolicy.CELLP_FLAG_7_OVERSIZE, false);
      //as many rows are wanted, one logical size
      ByteObject sizerH = gc.getLAC().getSizerFactory().getSizerLogicUnit(1);
      //TODO. sizerH is for content
      ByteObject policyRow = cellPolicyFac.getGeneric(0, sizerH);

      ByteObject policyTable = getTablePolicy(policyCol, policyRow);
      return policyTable;
   }

   /**
    * A Table whose cells are aligned one by one on a row. rows are created when necessary.
    * @param sizerW
    * @param sizerH
    * @return
    */
   public ByteObject createTableFlowHoriz(ByteObject sizerW, ByteObject sizerH) {
      TableCellPolicyFactory cellPolicyFac = getCellPolicyFactory();
      boolean isStrongRow = false;
      ByteObject policyCol = cellPolicyFac.getFlow(sizerW, isStrongRow);
      policyCol.setFlag(IBOCellPolicy.CELLP_OFFSET_02_FLAG, IBOCellPolicy.CELLP_FLAG_7_OVERSIZE, false);
      ByteObject policyRow = cellPolicyFac.getGeneric(0, sizerH);

      ByteObject policyTable = getTablePolicy(policyCol, policyRow);
      return policyTable;
   }

   /**
    * 
    * @return
    */
   public ByteObject createTableFlowVertical() {
      SizerFactory sizerFactory = gc.getLAC().getSizerFactory();
      ByteObject sizerW = sizerFactory.getSizerPref();
      ByteObject sizerH = sizerFactory.getSizerPref();

      return createTableFlowVertical(sizerW, sizerH);
   }

   public ByteObject createTableFlowVertical(int w, int h) {
      TableCellPolicyFactory cellPolicyFactory = getCellPolicyFactory();
      ByteObject policyCol = cellPolicyFactory.getGeneric(0, w);
      policyCol.setFlag(IBOCellPolicy.CELLP_OFFSET_02_FLAG, IBOCellPolicy.CELLP_FLAG_7_OVERSIZE, false);
      //as many rows are wanted, one logical size
      ByteObject policyRow = cellPolicyFactory.getFlow(h, true);
      ByteObject policyTable = getTablePolicy(policyCol, policyRow);
      return policyTable;
   }

   /**
    * A policy where cells are aligned on each column.. columns are created when necessary.
    * @param sizerW
    * @param sizerH
    * @return
    */
   public ByteObject createTableFlowVertical(ByteObject sizerW, ByteObject sizerH) {
      TableCellPolicyFactory cellPolicyFac = getCellPolicyFactory();
      boolean isStrongRow = false;
      ByteObject policyCol = cellPolicyFac.getFlow(sizerW, isStrongRow);
      policyCol.setFlag(IBOCellPolicy.CELLP_OFFSET_02_FLAG, IBOCellPolicy.CELLP_FLAG_7_OVERSIZE, false);
      ByteObject policyRow = cellPolicyFac.getGeneric(0, sizerH);

      ByteObject policyTable = getTablePolicy(policyCol, policyRow);
      return policyTable;
   }

   public ByteObject createTableWeakStrong(ByteObject rowSizer) {
      int[] policies = new int[] { ITechCell.CELL_5_FILL_WEAK, ITechCell.CELL_3_FILL_STRONG };
      int[] sizes = new int[] { 0, 60 };
      return createTableXColsManyRows(policies, sizes, rowSizer);
   }

   public ByteObject createTableSizeWeakSize(ByteObject colSizerLeft, ByteObject colSizerRight, ByteObject rowSizer) {
      int[] policies = new int[] { ITechCell.CELL_1_EXPLICIT_SET, ITechCell.CELL_5_FILL_WEAK, ITechCell.CELL_1_EXPLICIT_SET };
      int[] sizes = new int[] { 0, 60 };
      return createTableXColsManyRows(policies, sizes, rowSizer);
   }

   public ByteObject createTableSetWeakSet(int sizeLeft, int sizeRight, ByteObject rowSizer) {
      int[] policies = new int[] { ITechCell.CELL_1_EXPLICIT_SET, ITechCell.CELL_5_FILL_WEAK, ITechCell.CELL_1_EXPLICIT_SET };
      int[] sizes = new int[] { sizeLeft, 0, sizeRight };
      return createTableXColsManyRows(policies, sizes, rowSizer);
   }

   public ByteObject getTwoColumns() {
      int[] policies = new int[] { ITechCell.CELL_0_IMPLICIT_SET, ITechCell.CELL_1_EXPLICIT_SET };
      int[] sizes = new int[] { 0, 60 };
      return getSimpleXColsManyRows(policies, sizes);
   }

   public ByteObject getBOTable(boolean rowSlection, boolean showColTitles, boolean showRowTitles) {
      ByteObject tech = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_02_TABLE, T_BASIC_SIZE);
      tech.setFlag(T_OFFSET_01_FLAG, T_FLAG_3_SHOW_ROW_TITLE, showRowTitles);
      tech.setFlag(T_OFFSET_01_FLAG, T_FLAG_4_SHOW_COL_TITLE, showColTitles);
      tech.setFlag(T_OFFSET_01_FLAG, T_FLAGX_3_ROW_SELECTION, rowSlection);
      return tech;
   }

   /**
    * 
    * @return
    */
   public ByteObject getBOTableDefault() {
      return getBOTable(false, false, false);
   }

   /**
    * 
    * @param mod
    * @param x
    * @return
    */
   public ByteObject getXColumns(int x) {
      int[] policies = new int[x];
      int[] sizes = new int[x];
      return getSimpleXColsManyRows(policies, sizes);
   }

}
