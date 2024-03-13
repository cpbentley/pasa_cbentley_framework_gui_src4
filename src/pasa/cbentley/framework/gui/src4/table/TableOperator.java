package pasa.cbentley.framework.gui.src4.table;

import pasa.cbentley.byteobjects.src4.core.BOAbstractOperator;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.ctx.ToStringStaticUc;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ToStringStaticGui;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOGenetics;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOTablePolicy;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOTableSpan;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOTableView;

public class TableOperator extends BOAbstractOperator implements IBOTablePolicy, IBOTableView {

   protected final GuiCtx gc;

   public TableOperator(GuiCtx gc) {
      super(gc.getBOC());
      this.gc = gc;
   }

   public void toStringModelGenetics(ByteObject boGenetics, Dctx dc) {
      dc.rootN(boGenetics, "ModelGenetics", TableOperator.class, 24);
      dc.nl();
      dc.appendVarWithSpace("width", boGenetics.get2(IBOGenetics.GENE_OFFSET_03_WIDTH2));
      dc.appendVarWithSpace("height", boGenetics.get2(IBOGenetics.GENE_OFFSET_04_HEIGHT2));
      dc.nl();
      dc.append("Flags");
      dc.append(" SameSizeW=" + boGenetics.hasFlag(IBOGenetics.GENE_OFFSET_01_FLAG, IBOGenetics.GENE_FLAG_3_SAME_SIZE_W));
      dc.append(" SameSizeH=" + boGenetics.hasFlag(IBOGenetics.GENE_OFFSET_01_FLAG, IBOGenetics.GENE_FLAG_4_SAME_SIZE_H));
      dc.append(" IntMapper=" + boGenetics.hasFlag(IBOGenetics.GENE_OFFSET_01_FLAG, IBOGenetics.GENE_FLAG_5_MAPPERINT_TYPE));
   }

   public void toStringSpanning(ByteObject span, Dctx dc) {
      dc.rootN(span, "Span", TableOperator.class, 36);
      dc.append(" root = ");
      dc.append(span.get2(IBOTableSpan.SPAN_OFFSET_2_COL2));
      dc.append(',');
      dc.append(span.get2(IBOTableSpan.SPAN_OFFSET_4_ROW2));
      dc.append(" - ");
      dc.append(span.get2(IBOTableSpan.SPAN_OFFSET_3_COL_VALUE2));
      dc.append(',');
      dc.append(span.get2(IBOTableSpan.SPAN_OFFSET_5_ROW_VALUE2));
   }

   public void toStringTablePolicy(ByteObject policy, Dctx dc) {
      dc.rootN(policy, "TablePolicy", TableOperator.class, 83);
      ByteObject[] subs = policy.getSubs();
      if (subs == null) {
         dc.append("No Column/Row policies");
      } else {
         CellPolicy cellPolicy = gc.getCellPolicy();
         cellPolicy.toString(subs[0], dc.newLevel(), "col");
         cellPolicy.toString(subs[1], dc.newLevel(), "row");
         for (int i = 2; i < subs.length; i++) {
            toStringSpanning(subs[i], dc.newLevel());
         }
      }
   }

   /**
    * {@link IBOTableView}
    * 
    * @param boTableView
    * @param dc
    */
   public void toStringTableTech(ByteObject boTableView, Dctx dc) {
      dc.rootN(boTableView, "BOTableView", TableOperator.class, 30);
      dc.nl();
      dc.append(" ShowGrid=" + boTableView.hasFlag(T_OFFSET_01_FLAG, T_FLAG_7_DRAW_GRID));
      dc.append(" HGrid size = " + boTableView.get1(T_OFFSET_05_HGRID_SIZE1));
      dc.append(" VGrid size = " + boTableView.get1(T_OFFSET_06_VGRID_SIZE1));
      dc.append(" ScrollEarly = " + boTableView.get1(T_OFFSET_08_SELECTION_SCROLL1));
      dc.nl();

      dc.append(" RowTitle=" + boTableView.hasFlag(T_OFFSET_01_FLAG, T_FLAG_3_SHOW_ROW_TITLE));
      dc.append(" ColTitle=" + boTableView.hasFlag(T_OFFSET_01_FLAG, T_FLAG_4_SHOW_COL_TITLE));

      dc.append(" RowSel=" + boTableView.hasFlag(T_OFFSET_02_FLAGX, T_FLAGX_3_ROW_SELECTION));
      dc.append(" SelectEmptyCells=" + boTableView.hasFlag(T_OFFSET_02_FLAGX, T_FLAGX_8_SELECT_EMPTY_CELLS));
      dc.append(" MultipleSelections=" + boTableView.hasFlag(T_OFFSET_02_FLAGX, T_FLAGM_2_MULTIPLE_SELECTION));

      dc.nl();
      dc.append(" Hclock=" + boTableView.hasFlag(T_OFFSET_02_FLAGX, T_FLAGM_6_CLOCK_HORIZ));
      dc.append(" Vclock=" + boTableView.hasFlag(T_OFFSET_02_FLAGX, T_FLAGM_7_CLOCK_VERTICAL));
      dc.append(" isColFill=" + boTableView.hasFlag(T_OFFSET_09_MODEL_FILL_TYPE1, T_OFFSET_09_FLAG_3_FILL_COL));
      dc.append(" StartCount=" + ToStringStaticUc.toStringDiagDir(boTableView.get2Bits1(T_OFFSET_09_MODEL_FILL_TYPE1)));

      dc.nl();

      dc.append(" SelectionMode=" + ToStringStaticGui.toStringPSelectMode(boTableView.get1(T_OFFSET_12_PSELECTION_MODE1)));
      dc.append(" SelectionTime=" + boTableView.get1(T_OFFSET_13_PSELECTION_TIME1));
      dc.append(" ModelPolicy=" + boTableView.get1(T_OFFSET_14_MODEL_POLICY1));
      dc.append(" ModelTech=" + boTableView.get1(T_OFFSET_15_MODEL_TECH1));

   }
}
