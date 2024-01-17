package pasa.cbentley.framework.gui.src4.table;

import pasa.cbentley.byteobjects.src4.core.BOAbstractOperator;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.ctx.ToStringStaticUc;
import pasa.cbentley.core.src4.helpers.StringBBuilder;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ToStringStaticGui;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOGenetics;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOTablePolicy;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOTableView;

public class TableOperator extends BOAbstractOperator implements IBOTablePolicy, IBOTableView {

   protected final GuiCtx gc;

   public TableOperator(GuiCtx gc) {
      super(gc.getBOC());
      this.gc = gc;
   }



   public void toStringTableTech(ByteObject tech, Dctx sb) {
      sb.root(tech, "TableTech");
      sb.nl();
      sb.append(" ShowGrid=" + tech.hasFlag(T_OFFSET_01_FLAG, T_FLAG_7_DRAW_GRID));
      sb.append(" HGrid size = " + tech.get1(T_OFFSET_05_HGRID_SIZE1));
      sb.append(" VGrid size = " + tech.get1(T_OFFSET_06_VGRID_SIZE1));
      sb.append(" ScrollEarly = " + tech.get1(T_OFFSET_08_SELECTION_SCROLL1));
      sb.nl();

      sb.append(" RowTitle=" + tech.hasFlag(T_OFFSET_01_FLAG, T_FLAG_3_SHOW_ROW_TITLE));
      sb.append(" ColTitle=" + tech.hasFlag(T_OFFSET_01_FLAG, T_FLAG_4_SHOW_COL_TITLE));

      sb.append(" RowSel=" + tech.hasFlag(T_OFFSET_02_FLAGX, T_FLAGX_3_ROW_SELECTION));
      sb.append(" SelectEmptyCells=" + tech.hasFlag(T_OFFSET_02_FLAGX, T_FLAGX_8_SELECT_EMPTY_CELLS));
      sb.append(" MultipleSelections=" + tech.hasFlag(T_OFFSET_02_FLAGX, T_FLAGM_2_MULTIPLE_SELECTION));

      sb.nl();
      sb.append(" Hclock=" + tech.hasFlag(T_OFFSET_02_FLAGX, T_FLAGM_6_CLOCK_HORIZ));
      sb.append(" Vclock=" + tech.hasFlag(T_OFFSET_02_FLAGX, T_FLAGM_7_CLOCK_VERTICAL));
      sb.append(" isColFill=" + tech.hasFlag(T_OFFSET_09_MODEL_FILL_TYPE1, T_OFFSET_09_FLAG_3FILL_COL));
      sb.append(" StartCount=" + ToStringStaticUc.toStringDiagDir(tech.get2Bits1(T_OFFSET_09_MODEL_FILL_TYPE1)));

      sb.nl();

      sb.append(" SelectionMode=" + ToStringStaticGui.toStringPSelectMode(tech.get1(T_OFFSET_12_PSELECTION_MODE1)));
      sb.append(" SelectionTime=" + tech.get1(T_OFFSET_13_PSELECTION_TIME1));
      sb.append(" ModelPolicy=" + tech.get1(T_OFFSET_14_MODEL_POLICY1));
      sb.append(" ModelTech=" + tech.get1(T_OFFSET_15_MODEL_TECH1));

   }

   /**
    * @param modelGenetics
    * @param nl
    * @return
    * @deprecated
    */
   public String toStringModelGenetics(ByteObject modelGenetics, String nl) {
      StringBBuilder sb = new StringBBuilder(gc.getUCtx());
      String nnl = nl + "\t";
      sb.append("#ModelGenetics");
      sb.append(nl);
      sb.append("Size :" + modelGenetics.get2(IBOGenetics.GENE_OFFSET_03_WIDTH2) + "," + modelGenetics.get2(IBOGenetics.GENE_OFFSET_04_HEIGHT2));
      sb.append(nl);
      sb.append("Flags");
      sb.append(" SameSizeW=" + modelGenetics.hasFlag(IBOGenetics.GENE_OFFSET_01_FLAG, IBOGenetics.GENE_FLAG_3_SAME_SIZE_W));
      sb.append(" SameSizeH=" + modelGenetics.hasFlag(IBOGenetics.GENE_OFFSET_01_FLAG, IBOGenetics.GENE_FLAG_4_SAME_SIZE_H));
      sb.append(" IntMapper=" + modelGenetics.hasFlag(IBOGenetics.GENE_OFFSET_01_FLAG, IBOGenetics.GENE_FLAG_5_MAPPERINT_TYPE));
      return sb.toString();
   }

   public void toStringSpanning(ByteObject span, Dctx sb) {
      sb.root(span, "Span");
      sb.append(" root = ");
      sb.append(span.get2(SPAN_OFFSET_2COL2));
      sb.append(',');
      sb.append(span.get2(SPAN_OFFSET_4ROW2));
      sb.append(" - ");
      sb.append(span.get2(SPAN_OFFSET_3COL_VALUE2));
      sb.append(',');
      sb.append(span.get2(SPAN_OFFSET_5ROW_VALUE2));
   }

   public void toStringTablePolicy(ByteObject policy, Dctx sb) {
      sb.root(policy, "#TablePolicy");
      ByteObject[] subs = policy.getSubs();
      if (subs == null) {
         sb.append("No Column/Row policies");
      } else {
         CellPolicy cellPolicy = gc.getCellPolicy();
         cellPolicy.toString(subs[0], sb.newLevel(), "col");
         cellPolicy.toString(subs[1], sb.newLevel(), "row");
         for (int i = 2; i < subs.length; i++) {
            toStringSpanning(subs[i], sb.newLevel());
         }
      }
   }
}
