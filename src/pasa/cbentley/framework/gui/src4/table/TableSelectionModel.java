package pasa.cbentley.framework.gui.src4.table;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;

/**
 * Tracks "selected" cells in a {@link TableView}.
 * <br>
 * <br>
 * @author Charles-Philip Bentley
 *
 */
public class TableSelectionModel implements IStringable {

   /**
    * Focus to a fully visible cell.
    */
   public static final int TRANSITION_0INSIDE_VIEWPORT   = 0;

   /**
    * Focus goes to a partially shown cell
    */
   public static final int TRANSITION_1INSIDE_TO_PARTIAL = 1;

   /**
    * Focus goes to an invisible cell.
    */
   public static final int TRANSITION_2OUTSIDE           = 2;

   public static String debugType(int type) {
      switch (type) {
         case TRANSITION_0INSIDE_VIEWPORT:
            return "INSIDE";
         case TRANSITION_1INSIDE_TO_PARTIAL:
            return "ToPartial";
         case TRANSITION_2OUTSIDE:
            return "OUTSIDE";
         default:
            return "UNKNOWN";
      }
   }

   public int[] cols;

   public int   oldSelectedColAbs;

   public int   oldSelectedRowAbs;

   public int[] rows;

   public int   selectedColAbs;

   public int   selectedRowAbs;

   public int   type;

   /**
    * if vx is positive, we have a move right
    */
   public int   vx;

   /**
    * if vy is positive, we have a move down
    */
   public int   vy;

   protected final GuiCtx gc;

   
   public TableSelectionModel(GuiCtx gc) {
      this.gc = gc;
      
   }
   
   public void newSingleSelection(int colAbs, int rowAbs, int type) {
      oldSelectedColAbs = selectedColAbs;
      oldSelectedRowAbs = selectedRowAbs;
      selectedColAbs = colAbs;
      selectedRowAbs = rowAbs;
      // if vx is positive, we have a move right
      vx = selectedColAbs - oldSelectedColAbs;
      // if vy is positive, we have a move down
      vy = selectedRowAbs - oldSelectedRowAbs;
      this.type = type;
   }

   
   //#mdebug
   public IDLog toDLog() {
      return toStringGetUCtx().toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "TableSelectionModel");
      toStringPrivate(dc);
      dc.append(" Type=" + debugType(type));
      dc.append(" Vector (" + vx);
      dc.append("," + vy + ")");
      dc.append(" ");
      dc.append(" newSelected (" + selectedColAbs);
      dc.append("," + selectedRowAbs);
      dc.append(')');
      dc.append(" oldSelected (" + oldSelectedColAbs);
      dc.append("," + oldSelectedRowAbs);
      dc.append(")");
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "TableSelectionModel");
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return gc.getUC();
   }

   //#enddebug
   

}
