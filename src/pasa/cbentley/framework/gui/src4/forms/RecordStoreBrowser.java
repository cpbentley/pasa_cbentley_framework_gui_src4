package pasa.cbentley.framework.gui.src4.forms;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.engine.CmdNode;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.coreui.src4.interfaces.IActionFeedback;
import pasa.cbentley.framework.datamodel.src4.engine.MByteObjectTableModel;
import pasa.cbentley.framework.datamodel.src4.filter.MBoFilterSet;
import pasa.cbentley.framework.datamodel.src4.interfaces.IBOEnum;
import pasa.cbentley.framework.datamodel.src4.mbo.MBOByteObject;
import pasa.cbentley.framework.drawx.src4.engine.RgbImage;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsView;
import pasa.cbentley.framework.gui.src4.string.InputRequestStr;
import pasa.cbentley.framework.gui.src4.table.TableLayoutView;
import pasa.cbentley.framework.gui.src4.tech.ITechStringDrawable;

/**
 * This class implements the View and the Controller of the MVC
 * 
 * The Browsee defines the Model and updates the View through the IAppendable interface
 * @author cbentley
 *
 */
public class RecordStoreBrowser extends TableLayoutView implements IAppendable, ICmdsView {

   /**
    * Class that views individual items.
    */
   private IBrowseeView            browseeView;

   protected MByteObjectTableModel browseSrc;

   private CmdNode             ctxMain;

   /**
    * 
    */
   //private int             _ignoreNum      = 0;
   /**
    * indicates the max num of elements possible
    * will be shown after "of maxSize
    * -1 means max size is unknown and should be ignored
    */
   //private int             _maxSize        = -1;
   private boolean                 deleteMode;

   private FilterForm              filterForm;

   private MBoFilterSet            filters;

   private boolean                 gotomode;

   private Runnable                gotorunnable;

   private boolean                 isReload;

   private int                     messageindex = -1;

   /**
    * This number of
    */
   private int                     numPerPage;

   /**
    * the currently selected element
    */
   private int                     selectedVisualIndex;

   /**
    * The {@link RecordStoreBrowser} is not populated with records.
    * <br>
    * <br>
    * It needs to be done.
    * <br>
    * <br>
    * 
    * @param sc
    * @param bv
    */
   public RecordStoreBrowser(GuiCtx gc, StyleClass sc, IBrowseeView bv) {
      super(gc, sc);

      CmdCtx cc = gc.getCC();
      ctxMain = cc.createCmdNode("MainCtx");

      ctxMain.addMenuCmd(CMD_30_NEXT);
      ctxMain.addMenuCmd(CMD_31_PREVIOUS);
      ctxMain.addMenuCmd(VCMD_12_GO_TO);
      ctxMain.addMenuCmd(CMD_23_DELETE);
      ctxMain.addMenuCmd(VCMD_11_INVERSE);
      ctxMain.addMenuCmd(VCMD_10_SHOW_FILTERS);

      this.setBrowseeView(bv);

   }

   /**
    * Adds an item to the
    * @see mordan.universal.IAppendable#addItem(java.lang.String, java.lang.String, int, int)
    */
   public void addItem(String displayString, String img, int visualIndex, int recordId) {
      //manage view image
      RgbImage imgg = null;
      if (img != null) {
         imgg = gc.getDC().getRgbCache().getImage(img, true);
      }
      if (messageindex != -1) {
         this.insert(messageindex, displayString, imgg);
         messageindex++;

         if (numPerPage == visualIndex + 1) {
            //we reached the end, remove message
            this.formDelete(messageindex);
            messageindex = -1;
         }
      } else {
         this.formAppend(displayString, imgg);
      }
   }

   /**
    * 
    */
   public void clean() {
      browseSrc.clean();
      messageindex = -1;
   }

   /**
    * @param cmd 
    * 
    */
   private void cmdDelete(CmdInstance cmd) {
      if (cmd.param == 0) {
         //ask co
         int i = this.getSelectedIndex();
         String msg = "Delete" + this.getBrowsedString(i) + "?";
         InputRequestStr ir = new InputRequestStr(gc, getStyleClass(), msg);
         ir.show(this, cmd);
      } else {
         if (cmd.param == 1) {
            browseeView.getBrowsee().delete(getSelectedRID());
            populate();
            cmd.actionDone(this, IActionFeedback.FLAG_07_OBJECT_REPAINT);
         }
      }
   }

   /**
    * It might run in its own thread
    */
   private void cmdGoto(CmdInstance cmd) {
      if (cmd.param == 0) {
         InputRequestStr sd;
         //
         String msg = "Go To ID";
         ByteObject strTech = gc.getDrawableStringFactory().getStringTechEdit(ITechStringDrawable.TYPE_1_TITLE, 5, ITechStringDrawable.INPUT_TYPE_2_NUMERIC);
      } else {
         if (cmd.param == 1) {
            String s = (String) cmd.paramO;
            if (s != null && s.length() != 0) {
               int go = Integer.parseInt(s);
               browseSrc.getEnum().setStart(go);
               browseSrc.enumClearReload();
            }
         }
      }

   }

   private void cmdInverse(CmdInstance ci) {
      int sort = browseSrc.getEnum().getTech().get1(IBOEnum.BO_ENUM_OFFSET_03_SORT_TYPE1);
      MCmd m = gc.getCmdMapperGui().getCmdInverse();
      if (sort == IBOEnum.SORT_1_ASC) {
         m.doUpdateLabel(ci.getExCtx(), "Sort DESC");
         browseSrc.getEnum().getTech().set1(IBOEnum.BO_ENUM_OFFSET_03_SORT_TYPE1, IBOEnum.SORT_2_DEC);
      } else {
         m.doUpdateLabel(ci.getExCtx(), "Sort ASC");
         browseSrc.getEnum().getTech().set1(IBOEnum.BO_ENUM_OFFSET_03_SORT_TYPE1, IBOEnum.SORT_1_ASC);
      }
      browseSrc.enumClearReload();
   }

   public void cmdNext() {
      browseSrc.enumClearContinue();
   }

   private void cmdPrevious() {
      browseSrc.enumClearContinue();
   }

   /**
    * 
    */
   private void cmdSelect() {
      int rid = getSelectedRID();
      if (rid == -1) {
         //#debug
         toDLog().pNull("Bad Visual ID " + this.getSelectedIndex(), this, RecordStoreBrowser.class, "cmdSelect", LVL_05_FINE, true);
         return;
      }
      browseeView.displayBrowsee(rid);
   }

   /**
    * Manages the NEXT and PREVIOUS commands for the {@link IBrowseeView}
    */
   public void commandAction(CmdInstance cmd) {
      InputConfig ic = (InputConfig) cmd.getFeedback();
      int c = cmd.cmdID;
      CmdCtx cc = gc.getCC();
      if (cmd.ctx == browseeView.getCmdNode()) {
         //individually go to the next
         if (c == CMD_30_NEXT) {
            int maxRID = browseSrc.getLink().getNextBID();
            if (selectedVisualIndex + 1 < maxRID) {
               selectedVisualIndex++;
               browseeView.displayBrowsee(browseSrc.getBIDFromIndex(selectedVisualIndex));
            } else {
            }
         }
         if (c == CMD_31_PREVIOUS) {
            if (selectedVisualIndex - 1 >= 0) {
               selectedVisualIndex--;
               int rid = getSelectedRID();
               browseeView.displayBrowsee(rid);
            } else {
            }
         }
         //to prevent the Next command to propagate down
         return;
      }
      if (c == CMD_24_SELECT) {
         cmdSelect();
      }
      if (c == CMD_23_DELETE) {
         cmdDelete(cmd);
      }
      if (c == VCMD_11_INVERSE) {
         cmdInverse(cmd);
      }
      if (CMD_31_PREVIOUS == c) {
         cmdPrevious();
      }

      if (CMD_30_NEXT == c) {
         cmdNext();
      }

      if (VCMD_12_GO_TO == c) {
         gc.runAsWorker(gotorunnable);
      }
      if (c == VCMD_10_SHOW_FILTERS) {
         if (filterForm != null) {
            filterForm.shShowDrawableOver(ic);
         }
      }

      super.commandAction(cmd);

   }

   public String getBrowsedString(int vid) {
      int i = this.getSelectedRow();
      selectedVisualIndex = i;
      //View Action Calls the Model for Data
      MBOByteObject o = browseSrc.getByteObjectFromIndex(i);
      return o.getDisplayString();
   }

   public Browsee getBrowsee() {
      return browseeView.getBrowsee();
   }

   public FilterForm getFilterForm() {
      return filterForm;
   }

   /**
    * Map visual selected index to the RID in the 
    * @return
    */
   public int getSelectedRID() {
      int i = this.getSelectedRow();
      selectedVisualIndex = i;
      //View Action Calls the Model for Data
      MBOByteObject o = browseSrc.getByteObjectFromIndex(i);
      int rid = o.getRID();
      return rid;
   }

   /**
    * Tells the view that next time it shows, it has to refresh its data
    *
    */
   public void modelUpdated() {
      isReload = true;
   }

   /**
    * Called when the {@link RecordStoreBrowser} is going visible.
    * <br>
    * <br>
    * <li> when the filter set has changed
    * <li> when mode delete was set.
    */
   public void notifyEventShow() {
      //check if the filter set has changed
      if (browseSrc.getEnum().getFilterSet() != filters) {
         isReload = true;
      }
      if (isReload) {
         isReload = false;
         populate();
         ListUIRecords.setSelectedIndex(this, selectedVisualIndex);
      } else {
         //#debug
         System.out.println("No Reload For Browser after GoingVisible");
      }
      super.notifyEventShow();
   }

   /**
    * Call the session for building entries
    * Activate a session if none is active
    */
   public void populate() {
      //create or use the current session
      browseSrc.enumClearReload();
   }

   public void setBrowseeView(IBrowseeView bv) {
      browseeView = bv;
      bv.getCmdNode().addMenuCmd(gc.getViewCommandListener().CMD_30_NEXT);
      bv.getCmdNode().addMenuCmd(gc.getViewCommandListener().CMD_31_PREVIOUS);
   }

   public void setFilterForm(FilterForm filterForm) {
      if (filterForm != null) {
         this.filterForm = filterForm;
         ctxMain.addMenuCmd(filterForm.CMD_ShowFilters);
      }
   }

   /**
    * 
    * @see mordan.universal.IAppendable#setMessage(java.lang.String)
    */
   public void setMessage(String string) {
      messageindex = this.getModelRow().getSize();
      this.formAppend(string, null);
   }

   public void setSession(MByteObjectTableModel session) {
      browseSrc = session;
   }

   public void setTitle(String title) {
      setFormTitle(title);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "RecordStoreBrowser");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "RecordStoreBrowser");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

}
