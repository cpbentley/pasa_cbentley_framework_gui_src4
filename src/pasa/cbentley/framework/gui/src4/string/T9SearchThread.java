package pasa.cbentley.framework.gui.src4.string;

import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.powerdata.spec.src4.guicontrols.T9PrefixTrieSearch;
import pasa.cbentley.powerdata.spec.src4.spec.IT9View;

/**
 * Manage the results of a T9 search in a Thread.
 * <br>
 * <br>
 * Sets the result all at once.
 * <br>
 * <br>
 * @author Charles-Philip Bentley
 *
 */
public class T9SearchThread {

   private IT9View            view;

   private T9PrefixTrieSearch search;

   private GuiCtx         gc;

   public T9SearchThread(GuiCtx gc, IT9View view, T9PrefixTrieSearch search) {
      this.gc = gc;
      this.view = view;
      this.search = search;
   }

   /**
    * Called when only a single char is added.
    */
   public void addChars(final char c) {

   }

   /**
    * Add the given chars possibilities to the search and set results 
    * @param chars
    */
   public void addChars(final char[] chars) {
      gc.getCUC().getExecutor().executeWorker(new Runnable() {
         public void run() {
            search.add(chars);
            updateUI();
         }
      });
   }

   private void updateUI() {
      gc.getCUC().getExecutor().executeMainLater(new Runnable() {
         public void run() {
            //call this serially in the ui.
            view.setT9Result(search.getResults());
         }
      });
   }

   public void back() {
      gc.getCUC().getExecutor().executeWorker(new Runnable() {
         public void run() {
            search.stepBack();
            updateUI();
         }
      });
   }

}
