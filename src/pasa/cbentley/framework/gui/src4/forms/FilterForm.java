package pasa.cbentley.framework.gui.src4.forms;

import java.util.Vector;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.objects.function.ITechAcceptor;
import pasa.cbentley.core.src4.i8n.IStringProducer;
import pasa.cbentley.core.src4.structs.IntToStrings;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.engine.CmdNode;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdsCmd;
import pasa.cbentley.framework.cmd.src4.interfaces.ICommandable;
import pasa.cbentley.framework.coredata.src4.db.IByteStore;
import pasa.cbentley.framework.datamodel.src4.filter.FilterCondition;
import pasa.cbentley.framework.datamodel.src4.filter.IFilterable;
import pasa.cbentley.framework.datamodel.src4.filter.MFilterSet;
import pasa.cbentley.framework.datamodel.src4.table.ITableModel;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.factories.StringDrawableFactory;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStringDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;
import pasa.cbentley.framework.gui.src4.table.TableLayoutView;
import pasa.cbentley.framework.gui.src4.tech.ITechStringDrawable;

/**
 * This class is the view of the TestSettings model
 * 
 * @author cbentley
 * 
 */
public class FilterForm extends TableLayoutView implements ICommandable, IBOStringDrawable, ITechAcceptor {

   private static final String MAIN_LIST_TITLE = "Filters";

   private ListRoot            _fieldList;

   private Vector              dfields         = new Vector(2);

   private StringDrawable      _fieldValue;

   private IFilterable         filterable;

   private IntToStrings        filters;

   private int                 insertIndex;

   private ListRoot            _opList;

   private int                 previous;

   private ListRoot            filterRoot;

   private StringDrawable      settingName;

   public final MCmd           CMD_ShowFilters;

   private CmdCtx              cc;


   private MCmd                CMD_SHOW_LIST;

   private IByteStore          bs;

   private IStringProducer     sl;

   private Browsee             br;

   public FilterForm(GuiCtx gc, StyleClass sc, IFilterable fil, ListUIRecords db, Browsee br) {
      super(gc, sc);
      this.br = br;
      bs = gc.getCoreDataCtx().getByteStore();
      sl = gc.getStrings();

      cc = gc.getCC();
      CMD_ShowFilters = new MCmd(cc, "Show Filters");

      CMD_SHOW_LIST = new MCmd(cc, "Show List");

      filterable = fil;
      filterRoot = new ListRoot(gc, sc, MAIN_LIST_TITLE, db);
      filterRoot.getCmdNode().addMenuCmd(CMD_SHOW_LIST);

      ByteObject tech = gc.getDrawableStringFactory().getStringTech(ITechStringDrawable.TYPE_1_TITLE, ITechStringDrawable.MODE_0_READ, 15, ITechStringDrawable.INPUT_TYPE_0_ANY);
      settingName = new StringDrawable(gc, sc, "Name", tech);
      _fieldValue = new StringDrawable(gc, sc, "Name", tech);

      CmdNode ctx = cc.createCmdNode("filterform");
      ctx.addMenuCmd(ICmdsCmd.CMD_06_APPLY);
      ctx.addMenuCmd(ICmdsCmd.CMD_23_DELETE);
      ctx.addMenuCmd(ICmdsCmd.CMD_26_SAVE);
      setCmdCtx(ctx);

      this.formAppend(filterRoot);

   }

   public void addFilter(IFilterable fil) {
      // if first filter does not add OP
      this.formAppend(getNewOperator());
      ListRoot lr = getFields(fil);
      dfields.addElement(lr);
      this.formAppend(lr);
      insertIndex++;
   }

   private MFilterSet buildFilterSet() {
      // TODO Auto-generated method stub
      return null;
   }

   public void commandAction(CmdInstance cmd) {
      MCmd c = cmd.cmd;
      CmdCtx cc = cmd.getCC();
      if (c.getCmdId() == ICmdsCmd.CMD_23_DELETE) {
         int m_setting_id = filters.ints[filterRoot.getList().getSelectedIndex()];
         bs.deleteRecord(getRecordStore(), m_setting_id);
         this.updateFilterInternalList();
      }
      if (c.getCmdId() == ICmdsCmd.CMD_06_APPLY) {
         //apply filter set to current browsee
         MFilterSet fs = buildFilterSet();
         //globally set this filterset for the filterable
         br.setFilterSet(filterable, fs);
         //save it
         bs.addBytes(getRecordStore(), fs.toByteArray());
         this.updateFilterInternalList();
      }

   }

   public ListRoot getFields(IFilterable fil) {
      ListUIRecords valueLis = gc.getFormFactory().create(getStyleClass(), "Fields", 0);
      IntToStrings its = fil.getTypes();
      for (int i = 0; i < its.strings.length; i++) {
         valueLis.formAppend(its.strings[i], null);
      }
      valueLis.addCmds(this, cmdCtx, null);

      ListRoot fieldList = new ListRoot(gc, getStyleClass(), "", valueLis);
      CmdNode ctx = fieldList.getCmdNode();
      ctx.addMenuCmd(CMD_SHOW_LIST);
      ctx.setListener(this);

      return fieldList;
   }

   public IFilterable getFilterable() {
      return filterable;
   }

   public ListRoot getNewOperator() {
      //LEVEL 2 OPERATORS
      ListUIRecords opLis = gc.getFormFactory().create(getStyleClass(), "Operators", ITableModel.SELECTION_0_IMPLICIT);
      opLis.formAppend(sl.getString("op_0"), null);
      opLis.formAppend(sl.getString("op_1"), null);

      opLis.addCmds(this, cmdCtx, null);

      //#style filterEntry
      ListRoot opList = new ListRoot(gc, getStyleClass(), "Op:", opLis);
      CmdNode ctx = opList.getCmdNode();
      ctx.addMenuCmd(CMD_SHOW_LIST);
      ctx.setListener(this);
      return opList;
   }

   public String getRecordStore() {
      //return a unique name idependant of obfuscation
      return "filter" + filterable.getFilterTypeName();
   }

   public ListRoot getSmallOp(int conditionType) {
      //LEVEL 1 OPERATORS
      //#style filterForm
      ListUIRecords opLis = gc.getFormFactory().create(getStyleClass(), "Condition Op", ITableModel.SELECTION_0_IMPLICIT);

      switch (conditionType) {
         case ACC_TYPE_4_INT_DATE:
            listAddDateOperators(opLis);
            break;
         case ACC_TYPE_0_INT:
            listAddIntOperators(opLis);
            break;
         case ACC_TYPE_1_ARRAY:
            listAddStringOperators(opLis);
            break;
         case ACC_TYPE_3_STRING:
            listAddStringOperators(opLis);
            break;

         default:
            break;
      }
      opLis.addCmds(this, cmdCtx, null);

      //#style filterEntry
      ListRoot opList = new ListRoot(gc, getStyleClass(), "Op:", opLis);
      CmdNode ctx = opList.getCmdNode();
      ctx.addMenuCmd(CMD_SHOW_LIST);
      ctx.setListener(this);
      return opList;
   }

   public void goingVisible() {
      //we only want to update when the filter list has been changed
      updateFilter();
   }

   /**
    * State changed as occured in the context
    * @param i
    */
   public void itemStateChanged(IDrawable i) {
      //#debug
      System.out.println("State Changed on " + i.toString());
      if (i == filterRoot) {
         updateFilter();
      }
      for (int j = 0; j < dfields.size(); j++) {
         IDrawable it = (IDrawable) dfields.elementAt(j);
         if (i == it) {
            ListRoot lr = (ListRoot) it;
            int visualIndex = ListUIRecords.getSelectedIndex(lr.getList());
            int filterId = filterable.getTypes().ints[visualIndex];
            String selString = filterable.getTypes().strings[visualIndex];
            int condition = filterable.getFilterType(filterId);
            StringDrawableFactory drawableStringFactory = gc.getDrawableStringFactory();
            if (j == dfields.size() - 1) {
               //last filter, we have to add small op and field value
               //_insertIndex points to the next
               StyleClass sc = getStyleClass();
               switch (condition) {
                  case ITechAcceptor.ACC_TYPE_4_INT_DATE:
                     this.insert(insertIndex, getSmallOp(ACC_TYPE_4_INT_DATE));
                     insertIndex++;
                     ByteObject techDate = drawableStringFactory.getStringTech(ITechStringDrawable.TYPE_1_TITLE, ITechStringDrawable.MODE_2_EDIT, 30, ITechStringDrawable.INPUT_TYPE_5_DATE);
                     StringDrawable sdDate = new StringDrawable(gc, sc, "", techDate);
                     this.insert(insertIndex, sdDate);
                     break;
                  case ACC_TYPE_0_INT:
                     this.insert(insertIndex, getSmallOp(ACC_TYPE_0_INT));
                     insertIndex++;
                     ByteObject tech = drawableStringFactory.getStringTech(ITechStringDrawable.TYPE_1_TITLE, ITechStringDrawable.MODE_2_EDIT, 30, ITechStringDrawable.INPUT_TYPE_3_DECIMAL);
                     StringDrawable sd = new StringDrawable(gc, sc, "", tech);
                     this.insert(insertIndex, sd);
                     break;
                  case ACC_TYPE_3_STRING:
                     this.insert(insertIndex, getSmallOp(ACC_TYPE_3_STRING));
                     insertIndex++;
                     ByteObject tech2 = drawableStringFactory.getStringTech(ITechStringDrawable.TYPE_1_TITLE, ITechStringDrawable.MODE_2_EDIT, 30, ITechStringDrawable.INPUT_TYPE_0_ANY);
                     StringDrawable sd2 = new StringDrawable(gc, sc, "", tech2);
                     this.insert(insertIndex, sd2);
                     break;
                  case ACC_TYPE_1_ARRAY:
                     this.insert(insertIndex, getSmallOp(ACC_TYPE_1_ARRAY));
                     insertIndex++;
                     IntToStrings its = filterable.getStrings(filterId);
                     ListUIRecords lu = gc.getFormFactory().create(sc, selString, ITableModel.SELECTION_2_MULTIPLE, its, null);
                     this.insert(insertIndex, new ListRoot(gc, sc, selString, lu));
                     break;

                  default:
                     break;
               }

            } else {
               insertIndex = insertIndex + 3;

            }
         }
      }

   }

   public void listAddDateOperators(ListUIRecords l) {
      l.formAppend(sl.getString("op_date_last"), null);
      l.formAppend(sl.getString("op_date_datemonth"), null);
      l.formAppend(sl.getString("op_date_dateweek"), null);
      l.formAppend(sl.getString("op_date_dateyear"), null);
   }

   public void listAddIntOperators(ListUIRecords l) {
      l.formAppend(sl.getString("op_equal"), null);
      l.formAppend(sl.getString("op_higher"), null);
      l.formAppend(sl.getString("op_lower"), null);
   }

   public void listAddStringOperators(ListUIRecords l) {
      l.formAppend(sl.getString("op_string_contains"), null);
      l.formAppend(sl.getString("op_string_equals"), null);
   }

   public void setFilterable(IFilterable fil) {
      filterable = fil;
   }

   /**
    * Visually apply the selected settings to the form
    * and set the filter as the MIDlet filter
    * @param fs
    */
   public void showFilter(MFilterSet fs) {
      settingName.setStringNoUpdate(fs.getName());

   }

   public String toString() {
      return "filterform";
   }

   /**
    * Call this method when the filter list has been changed
    *
    */
   public void updateFilter() {
      int seli = ListUIRecords.getSelectedIndex(filterRoot.getList());
      //performance check
      if (previous == seli)
         return;
      else
         previous = seli;
      if (seli < 0) {
         //no filters in the list
         return;
      }
      if (seli < filters.ints.length) {
         int m_setting_id = filters.ints[seli];
         MFilterSet fs = new MFilterSet(gc.getDMC(), bs.getBytes(getRecordStore(), m_setting_id), 0);
         //visuall show the filter
         this.showFilter(fs);
         //save current filter
         br.setFilterSet(filterable, fs);
      } else {
         //#debug
         System.out.println("Selected Index=" + seli + " >= " + filters.ints.length);
      }
   }

   /**
    * This method has the refresh the List
    *
    */
   private void updateFilterInternalList() {
   }

}
