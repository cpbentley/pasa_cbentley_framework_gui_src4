package pasa.cbentley.framework.gui.src4.string;

import pasa.cbentley.core.src4.structs.IntToStrings;
import pasa.cbentley.framework.cmd.src4.interfaces.ITechNavCmd;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.table.TableView;

/**
 * The search is done.
 * 
 * This runnable inside the GUI thread
 * @author Charles Bentley
 *
 */
public class SearchPrefixRunnable implements Runnable {

   private StringDrawable controlledSD;

   private TableView      predictionTable;

   PredictionRunner       pr;

   private GuiCtx         gc;

   private ExecutionContextCanvasGui ic;

   private IntToStrings results;

   public SearchPrefixRunnable(GuiCtx gc, ExecutionContextCanvasGui ic, IntToStrings results) {
      this.gc = gc;
      this.ic = ic;
      this.results = results;

   }

   public void run() {
      if (results.nextempty != 0) {
         predictionTable.init(0, 0);

         int dy = pr.py + controlledSD.getContentY() + controlledSD.getStringer().getMetrics().getLineHeight();

         predictionTable.setXY(pr.px + controlledSD.getContentX(), dy);
      } else {
         //hide table ?

      }

      //request a repaint
      //make sure the table is shown. set the topology
      gc.getNavigator().navInsert(ic,controlledSD, ITechNavCmd.NAV_02_DOWN, predictionTable);
      //TODO flag so Down command from caret context goes to the prediction table
      controlledSD.setStateNav(ITechDrawable.NAV_06_OVERRIDE_DOWN, true);
      predictionTable.shShowDrawable(ic, ITechCanvasDrawable.SHOW_TYPE_1_OVER_TOP);
   }
}
