package pasa.cbentley.framework.gui.src4.creator;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.table.interfaces.ITechTable;

public abstract class CreatorAbstractTable extends CreatorAbstract {

   public CreatorAbstractTable(CreateContext gc) {
      super(gc);
   }

   public ByteObject getBOTableViewNoTitles() {

      ByteObject tech = createByteObject(IBOTypesGui.TYPE_GUI_02_TABLE, T_BASIC_SIZE);

      int gridSizeH = 2;
      int gridSizeV = 2;

      tech.set1(T_OFFSET_05_HGRID_SIZE1, gridSizeH);
      tech.set1(T_OFFSET_06_VGRID_SIZE1, gridSizeV);

      boolean isClockHoriz = false;
      boolean isClockV = false;

      boolean isShowGrid = true;
      boolean isSelectionAnimation = false;

      tech.setFlag(T_OFFSET_11_FLAGM, T_FLAGM_6_CLOCK_HORIZ, isClockHoriz);
      tech.setFlag(T_OFFSET_11_FLAGM, T_FLAGM_7_CLOCK_VERTICAL, isClockV);

      tech.setFlag(T_OFFSET_01_FLAG, T_FLAG_7_DRAW_GRID, isShowGrid);
      tech.setFlag(T_OFFSET_01_FLAG, T_FLAG_8_STYLE_ANIMATION, isSelectionAnimation);

      int fillType = C.DIAG_DIR_0_TOP_LEFT; //how to fill model
      tech.set1(T_OFFSET_09_MODEL_FILL_TYPE1, fillType);

      boolean showColTitles = false;
      boolean showRowTitles = false;

      tech.setFlag(T_OFFSET_01_FLAG, T_FLAG_3_SHOW_ROW_TITLE, showRowTitles);
      tech.setFlag(T_OFFSET_01_FLAG, T_FLAG_4_SHOW_COL_TITLE, showColTitles);

      int pSelectionMode = ITechTable.PSELECT_1_PRESS;
      int pSelectionTime = 0;
      tech.set1(T_OFFSET_12_PSELECTION_MODE1, pSelectionMode);
      tech.set1(T_OFFSET_13_PSELECTION_TIME1, pSelectionTime);

      boolean isNoSelection = false;
      boolean isSelectEmptyCells = false;
      tech.setFlag(T_OFFSET_02_FLAGX, T_FLAGX_1_NO_SELECTION, isNoSelection);
      tech.setFlag(T_OFFSET_02_FLAGX, T_FLAGX_8_SELECT_EMPTY_CELLS, isSelectEmptyCells);

      boolean isRowSelection = false;
      boolean isRowSelectionStates = false;
      boolean isRowSelectionOverlay = false;
      tech.setFlag(T_OFFSET_02_FLAGX, T_FLAGX_3_ROW_SELECTION, isRowSelection);
      tech.setFlag(T_OFFSET_02_FLAGX, T_FLAGX_4_ROW_SELECTION_STYLE_STATES, isRowSelectionStates);
      tech.setFlag(T_OFFSET_02_FLAGX, T_FLAGX_5_ROW_SELECTION_NO_OVERLAY, isRowSelectionOverlay);

      boolean isListUpDown = false;
      boolean isClockFullHLook = false;
      tech.setFlag(T_OFFSET_03_FLAGY, T_FLAGY_2_LIST_UP_DOWN, isListUpDown);
      tech.setFlag(T_OFFSET_03_FLAGY, T_FLAGM_5_CLOCK_FULL_H_LOOP, isClockFullHLook);

      boolean selectKeptNoFocus = true;
      boolean selectMoveNotGivesFocus = true;
      boolean noStyle = true;
      tech.setFlag(T_OFFSET_04_FLAGF, T_FLAGF_1_SELECT_KEPT_WITHOUT_FOCUS_KEY, selectKeptNoFocus);
      tech.setFlag(T_OFFSET_04_FLAGF, T_FLAGF_2_SELECT_MOVE_NOT_GIVES_FOCUS, selectMoveNotGivesFocus);
      tech.setFlag(T_OFFSET_04_FLAGF, T_FLAGF_4_NOSTYLE, noStyle);

      return tech;

   }

   public abstract ByteObject getTableTech();

}
