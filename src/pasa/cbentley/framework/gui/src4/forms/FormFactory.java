package pasa.cbentley.framework.gui.src4.forms;

import pasa.cbentley.core.src4.structs.IntToStrings;
import pasa.cbentley.framework.core.data.src4.db.IByteCache;
import pasa.cbentley.framework.coredraw.src4.interfaces.IImage;
import pasa.cbentley.framework.datamodel.src4.interfaces.IObjectStore;
import pasa.cbentley.framework.datamodel.src4.table.ITableModel;
import pasa.cbentley.framework.datamodel.src4.table.ObjectTableModel;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.factories.TablePolicyFactory;

public class FormFactory {

   protected final GuiCtx gc;

   public FormFactory(GuiCtx gc) {
      this.gc = gc;

   }

   /**
    * 
    * @param sc
    * @param title
    * @param selType
    * <li>{@link ITableModel#SELECTION_0_IMPLICIT}
    * <li>{@link ITableModel#SELECTION_1_EXCLUSIVE}
    * <li>{@link ITableModel#SELECTION_2_MULTIPLE}
    * @return
    * 
    */
   public ListUIRecords create(StyleClass sc, String title, int selType) {
      ITableModel model = new ObjectTableModel(gc.getDMC(), new String[0]);
      TablePolicyFactory tableFac = gc.getTablePolicyFactory();
      ListUIRecords lu = new ListUIRecords(gc, sc, tableFac.getSimple1ColPolicy(), model);
      lu.setSelectionType(selType);
      return lu;
   }

   public ListUIRecords create(StyleClass sc, String title, int selType, IntToStrings its, IImage[] imgs) {
      ITableModel model = new ObjectTableModel(gc.getDMC(), new String[0]);
      TablePolicyFactory tableFac = gc.getTablePolicyFactory();
      ListUIRecords lu = new ListUIRecords(gc, sc, tableFac.getSimple1ColPolicy(), model);
      lu.setSelectionType(selType);
      return lu;
   }

   public ListUIRecords create(StyleClass sc, String title, IObjectStore mo) {
      ITableModel model = new ObjectTableModel(gc.getDMC(), new String[0]);
      TablePolicyFactory tableFac = gc.getTablePolicyFactory();
      ListUIRecords lu = new ListUIRecords(gc, sc, tableFac.getSimple1ColPolicy(), model);
      return lu;
   }

   public ListUIRecords create(StyleClass sc, String title, int type, IByteCache bc, ViewOptions vo) {
      ITableModel model = new ObjectTableModel(gc.getDMC(), new String[0]);
      TablePolicyFactory tableFac = gc.getTablePolicyFactory();
      ListUIRecords lu = new ListUIRecords(gc, sc, tableFac.getSimple1ColPolicy(), model);
      if (bc == null)
         throw new NullPointerException();
      lu.appendAll();

      //get the last used index
      lu.setSelectedIndex(lu, vo.selected);
      return lu;
   }

}
