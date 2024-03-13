package pasa.cbentley.framework.gui.src4.string;

import pasa.cbentley.core.src4.structs.IntToStrings;
import pasa.cbentley.core.src4.thread.AbstractBRunnable;
import pasa.cbentley.core.src4.thread.IBRunnable;
import pasa.cbentley.framework.datamodel.src4.table.ObjectTableModel;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.powerdata.spec.src4.power.IPowerCharCollector;

/**
 * {@link IMRunnable} to search for stuff in a {@link IPowerCharCollector}
 * <br>
 * Can be run in a waiting queue inside a Thread
 * @author Charles Bentley
 *
 */
public class PredictionRunner extends AbstractBRunnable implements IBRunnable {

   /**
    * 
    */
   private IPowerCharCollector activeTrie;

   /**
    * Position of caret when prediction is asked
    */
   int                         caretInit;

   /**
    * Computed position of starting word
    */
   int                         caretWordStart;

   private InputConfig         ic;

   /**
    * Model. This is read from one column model.
    * <br>
    * This object belongs to the Trie Business Thread.
    */
   private ObjectTableModel    predictions = null;

   String                      prefix;

   int                         px;

   int                         py;

   StringEditControl           sec;

   protected final GuiCtx gc;

   /**
    * Constructor in a thread
    * @param s
    */
   public PredictionRunner(GuiCtx gc, StringEditControl s) {
      super(gc.getUC());
      this.gc = gc;
      sec = s;
      predictions = new ObjectTableModel(gc.getDMC(), 1);
   }

   public ObjectTableModel getModel() {
      synchronized (predictions) {
         return predictions;
      }
   }

   /**
    * Called from any thread
    * @param selectedIndex
    * @return
    */
   public String getPred(int selectedIndex) {
      synchronized (predictions) {
         String str = (String) predictions.getObject(selectedIndex);
         return str;
      }
   }

   /**
    * A run is made in the context of an {@link InputConfig}.
    * <br>
    * Either a user UI action, Command or System command generated the state change.
    */
   public void runAbstract() {
      //this is run in worker thread
      final IntToStrings its = activeTrie.searchPrefix(5).searchWait(prefix);

      //update the model. lock it to prevent other threads from reading from it
      synchronized (predictions) {
         predictions.clean();
         for (int i = 0; i < its.nextempty; i++) {
            predictions.appendObject(its.strings[i]);
         }
      }
      //create a runnable to be run in the screen result before painting. in a Passive Mode
      //active mode, rendering thread
      //how are the views updated if they run in other threads?
      //TODO this code must run in the GUI thread.
      predictions.eventRefresh();
      sec.searchPrefix(ic, prefix);
   }

   public void setInputConfig(InputConfig ic) {
      this.ic = ic;
   }
}
