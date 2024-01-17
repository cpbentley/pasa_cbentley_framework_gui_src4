package pasa.cbentley.framework.gui.src4.mui;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.memory.IMemFreeable;
import pasa.cbentley.core.src4.structs.IntBuffer;
import pasa.cbentley.core.src4.structs.IntToObjects;
import pasa.cbentley.framework.datamodel.src4.table.ObjectTableModel;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.ITechViewDrawable;
import pasa.cbentley.framework.gui.src4.table.TableView;

/**
 * 
 * Draws the log using a Table of 1 column. Model is String only.
 * <br>
 * <br>
 * When created, register this class as a {@link IMLog} to {@link UiLink#setLogger(IMLog)}.
 * <br>
 * <br>
 * User may delete message when selected using the {@link CmdController#CMD_DELETE}
 * <br>
 * <br>
 * By default, it simply displays log Strings
 * The LogViewer may be parametrized to show timestamps.
 * <li> absolute 
 * <li> relative to now, this require data to be processed upon the time of the model fetch.
 * <br>
 * <br>
 * StyleKey in the constructor:
 * To this root key are linked custom type style layers
 * A TechParam linked to this style decides how the options of the MLogViewer
 * Those options are configurable and saved (e.g. show timestamp)
 * <br>
 * 
 * @author Charles-Philip Bentley
 * @see {@link TableView}
 * @see {@link IMLog}
 * 
 */
public class MLogViewer extends TableView implements IMLog, IMemFreeable {

   /**
    * Show full message in cell or only 1 trimmed line
    * THis flag will impact Table Policy
    */
   public static int TECH_FLAG1_FULL_STRING   = 1;

   /**
    * Show timestamps
    */
   public static int TECH_FLAG2_TIMESTAMPS    = 2;

   /**
    * Show relative timestamps
    */
   public static int TECH_FLAG4_RELATIVE      = 4;

   boolean           modifiedSinceLastReshape = false;

   ObjectTableModel  stm;

   /**
    * timestamps when messages were received.<br>
    * That a second column.
    */
   public IntBuffer  timestamps;

   /**
    * 
    * @param styleKey The key. 
    * @param w
    * @param h
    */
   public MLogViewer(GuiCtx gc, StyleClass sc) {
      this(gc, sc, gc.getTablePolicyC().getSimple1ColPolicy(), null);
   }

   /**
    * Sets the {@link ITechViewDrawable#VIEW_GENE_31_EXPANDABLE_W}
    * @param styleKey
    * @param its
    * @param policy
    */
   public MLogViewer(GuiCtx gc, StyleClass sc, ByteObject policy, IntToObjects its) {
      super(gc, sc, policy);
      UCtx uc = gc.getUCtx();
      setViewFlag(ITechViewDrawable.VIEW_GENE_31_EXPANDABLE_W, true);
      if(its == null) {
         its = new IntToObjects(uc);
      }
      //the ViewUpdate event the column names are defined here. linked 
      stm = new ObjectTableModel(gc.getDMC(), its);
      setDataModel(stm);
      timestamps = new IntBuffer(uc);
   }

   /**
    * Action handler of command {@link CmdController#CMD_DELETE}
    * @param id
    */
   public void deleteMsg(int id) {

   }

   public void navigateSelect(InputConfig ic) {
      if (ic.isPressed()) {
         ic.srActionDoneRepaint(true, false);
      }
   }

   /**
    * Removes all logs but the last 5 from the model and clear the wrapper + 
    * call a repaint on the Table
    */
   public void freeMemory() {

   }

   public String getName() {
      return "LogViewer";
   }

   public void log(String msg) {
      log(msg, IMLog.TYPE_0_MSG);
   }

   /**
    * Logs the message with its type. <br>
    * If option is on, timestamp the message.
    * <br>
    * <li> {@link IMLog#TYPE_0_MSG}
    * <li> {@link IMLog#TYPE_1_ERROR}
    * <li> {@link IMLog#TYPE_2_WARNING}
    * <li> {@link IMLog#TYPE_3_CRITICAL}
    * 
    * <br>
    * @param msg the message as a String
    * @param type {@link IMLog#TYPE_1_ERROR} for typing message as an error
    */
   public void log(String msg, int type) {
      //notifies model and call for a ViewUpdate
      stm.appendObject(msg, type);
   }

   public void log(int bip, String msg, int type) {
      // TODO Auto-generated method stub

   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "MLogViewer");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "MLogViewer");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

}
