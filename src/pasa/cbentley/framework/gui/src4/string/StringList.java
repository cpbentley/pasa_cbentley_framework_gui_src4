package pasa.cbentley.framework.gui.src4.string;

import pasa.cbentley.framework.datamodel.src4.table.ObjectTableModel;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.forms.ListRoot;

/**
 * 
 * @see ListRoot
 * 
 * Displays String from a list of Strings. Array or {@link ObjectTableModel}.
 * <br> 
 * <br>
 * Provides
 * <li>Event management
 * <li>
 * Used be to called {@link ListRoot}.
 * <br>
 * <br>
 * <li>Use T9 on phones
 * <li>Use Predictive on other devices
 * <br>
 * @author Charles-Philip Bentley
 *
 */
public class StringList extends StringDrawable {

   /**
    * Loads only one string.
    * <br>
    * Will load model on demand.
    * <br>
    * @param sc
    * @param str
    */
   public StringList(GuiCtx gc, StyleClass sc, String str, int type) {
      super(gc, sc, str, type);
   }

   public StringList(GuiCtx gc, StyleClass sc, String[] strs) {
      super(gc, sc, strs[0]);
   }

}
