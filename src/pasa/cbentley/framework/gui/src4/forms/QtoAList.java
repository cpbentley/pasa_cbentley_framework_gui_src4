package pasa.cbentley.framework.gui.src4.forms;

import pasa.cbentley.framework.datamodel.src4.table.ITableModel;
import pasa.cbentley.framework.drawx.src4.engine.RgbImage;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.table.TableLayoutView;

/**
 * Title is the question
 * lists answers
 * @author cbentley
 *
 */
public class QtoAList extends TableLayoutView {

   public static final int    TYPE_YES_NO = 0;

   public static final int    TYPE_METHOD = 1;

   public static final String STRING_BT   = "Bluetooth";

   public static final String STRING_LF   = "Local File";

   public static final String STRING_I    = "Internet";

   public QtoAList(GuiCtx gc, StyleClass sc) {
      super(gc, sc, 1, ITableModel.SELECTION_0_IMPLICIT);
   }

   public void setType(String question, int type) {
      formDeleteAll();
      this.setFormTitle(question);
      switch (type) {
         case TYPE_YES_NO:
            //#style libEntry
            this.formAppend("Yes", null);
            //#style libEntry
            this.formAppend("No", null);
            break;
         case TYPE_METHOD:
            this.formAppend(STRING_LF, null);
            this.formAppend(STRING_BT, null);
            this.formAppend(STRING_I, null);
            break;
         default:
            this.formAppend("Continue...", null);
      }
   }

   public void set(String question, String[] answers) {
      this.setFormTitle(question);
      for (int i = 0; i < answers.length; i++) {
         this.formAppend(answers[i], null);
      }
   }

   public void set(String question, String[] answers, RgbImage[] images) {
      this.setFormTitle(question);
      for (int i = 0; i < answers.length; i++) {
         this.formAppend(answers[i], images[i]);
      }
   }
}
