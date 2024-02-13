package pasa.cbentley.framework.gui.src4.forms;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.i8n.I8nString;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.thread.IBProgessable;
import pasa.cbentley.core.src4.thread.IBRunnable;
import pasa.cbentley.core.src4.thread.ITechRunnable;
import pasa.cbentley.core.src4.utils.BitUtils;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.cmd.src4.engine.CmdNode;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsView;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;
import pasa.cbentley.framework.gui.src4.table.TableLayoutView;
import pasa.cbentley.framework.gui.src4.tech.ITechStringDrawable;

/**
 * A Drawable that displays a Gauge
 * <br>
 * <br>
 * A gauge may have a Hide and Cancel commands.
 * Hide is only applicable if the Gauge works for a WorkerThread
 * Because the GaugeScreen may be used by a background thread, the callSerially method is used for all
 * GUI updates
 * @author cbentley
 *
 */
public class GaugeScreenS extends TableLayoutView implements IBProgessable {

   private Runnable       cancel;

   private Gauge          gauge;

   private StringDrawable info;

   private int            opened = 0;

   public GaugeScreenS(GuiCtx gc, StyleClass sc, String title) {
      super(gc,sc);

      setFormTitle(title);

      //#style libEntry
      gauge = new Gauge(gc,sc, "", true, 1, 0);
      //#style libEntryOne
      info = new StringDrawable(gc,sc, "info:", ITechStringDrawable.PRESET_CONFIG_1_TITLE);

      this.formAppend(info);
      this.formAppend(gauge);

      CmdCtx cc = gc.getCC();
      CmdNode ctx = cc.createCmdNode("gauge");
      ctx.addMenuCmd(ICmdsView.CMD_05_CANCEL);
      ctx.addMenuCmd(ICmdsView.VCMD_13_HIDE);

      setCmdNote(ctx);
   }

   /**
    * 
    * @see mordan.kernel.interfaces.IMProgessable#close()
    */
   public void close(InputConfig ic) {
      // the method might have been called accidently while no gaugescreen is visible.
      //makes sure the current displayable is the gaugescreen
      if (opened > 0) {
         opened--;
         setTitle("Work Finished");
         gauge.setLabel("");
         info.setStringNoUpdate("");
         //hide it
         this.shHideDrawable(ic);
      }
   }

   public Runnable getCancelRunnable() {
      return cancel;
   }

   public IBProgessable getChild() {
      //#style libEntry
      final Gauge childgauge = new Gauge(gc,getStyleClass(), "", true, 1, 0);
      //#style libEntryOne
      final StringDrawable childinfo = new StringDrawable(gc,getStyleClass(), "info:", ITechStringDrawable.PRESET_CONFIG_1_TITLE);
      IBProgessable child = new IBProgessable() {

         private Runnable childCancelRunnable;

         public void close() {
            //delete the last two
            int size = formSize();
            formDelete(size - 1);
            formDelete(size - 2);
         }

         public Runnable getCancelRunnable() {
            return this.childCancelRunnable;
         }

         public IBProgessable getChild() {
            return this;
         }

         public int getOptions() {
            return 0;
         }

         public String getTitle() {
            return "";
         }

         public void increment() {
            int nv = childgauge.getValue() + 1;
            //#debug
            System.out.println("Child Increment " + nv);
            childgauge.setValue(nv);
         }

         public void open() {
            formAppend(childinfo);
            formAppend(childgauge);
         }

         /**
          * No options for child
          * @see mordan.kernel.interfaces.IMProgessable#open(int)
          */
         public void open(int options) {
            this.open();
         }

         public void setCancelRunnable(Runnable run) {
            this.childCancelRunnable = run;
         }

         public void setInfo(String info) {
            childinfo.setStringNoUpdate(info);
         }

         public void setLabel(String s) {
            childgauge.setLabel(s);
         }

         public void setMaxValue(int mv) {
            //#debug
            System.out.println("Child Progress Setting Max Value To " + mv);
            childgauge.setMaxValue(mv);
         }

         public void setTitle(String title) {

         }

         public void setValue(int value) {
            childgauge.setValue(value);
         }

         public IBProgessable getChild(IBRunnable runnable) {
            // TODO Auto-generated method stub
            return null;
         }

         public boolean isCanceled() {
            // TODO Auto-generated method stub
            return false;
         }

         public IBProgessable openNew(IBRunnable runnable) {
            // TODO Auto-generated method stub
            return null;
         }

         public void set(String title, String info, String label, int maxValue, int level) {
            // TODO Auto-generated method stub

         }

         public int getLvl() {
            // TODO Auto-generated method stub
            return 0;
         }


         public void set(I8nString title, I8nString info, I8nString label, int maxValue, int level) {
            // TODO Auto-generated method stub

         }

         public void setLabel(I8nString s) {
            // TODO Auto-generated method stub

         }

         public String toString1Line() {
            // TODO Auto-generated method stub
            return null;
         }

         public void toString(Dctx dc) {
            // TODO Auto-generated method stub
            
         }

         public void toString1Line(Dctx dc) {
            // TODO Auto-generated method stub
            
         }

         public UCtx toStringGetUCtx() {
            // TODO Auto-generated method stub
            return null;
         }

         public void setLvl(int lvl) {
            // TODO Auto-generated method stub
            
         }

         public void setTimeLeft(long value) {
            // TODO Auto-generated method stub
            
         }

         public void error(String msg) {
            // TODO Auto-generated method stub
            
         }

         public void increment(int value) {
            // TODO Auto-generated method stub
            
         }

         public void close(String msg) {
            // TODO Auto-generated method stub
            
         }

      };

      return child;
   }

   public Gauge getGauge() {
      return gauge;
   }

   public int getOptions() {
      // TODO Auto-generated method stub
      return 0;
   }

   public void increment() {
      gauge.setValue(gauge.getValue() + 1);
   }

   /**
    * Open it
    * @see mordan.kernel.interfaces.IMProgessable#open()
    */
   public void open(InputConfig ic) {
      opened++;
      if (opened == 1) {
         //only do it, if not already opened
         this.shShowDrawableOver(ic);
      }
   }

   public void open(InputConfig ic, int options) {
      CmdCtx cc = gc.getCC();
      CmdNode ctx = getCmdNode();
      if (BitUtils.isSet(options, ITechRunnable.FLAG_04_CANCELABLE)) {
         ctx.addMenuCmd(ICmdsView.CMD_05_CANCEL);
      } else {
         ctx.removeMenuCmd(ICmdsView.CMD_05_CANCEL);
      }
      if (BitUtils.isSet(options, ITechRunnable.FLAG_05_UI_HIDABLE)) {
         ctx.addMenuCmd(ICmdsView.VCMD_13_HIDE);
      } else {
         ctx.removeMenuCmd(ICmdsView.VCMD_13_HIDE);
      }
      this.open(ic);
   }

   public void setCancelRunnable(Runnable run) {
      cancel = run;
   }

   public void setInfo(String info) {
      this.info.setStringNoUpdate(info);
   }

   public void setLabel(String s) {
      gauge.setLabel(s);
   }

   public void setMaxValue(int mv) {
      if (mv < 1)
         mv = 1; //otherwise we get ArithmeticException for / by zero
      gauge.setMaxValue(mv);
   }

   public void setValue(int value) {
      gauge.setValue(value);
   }

   public String toString() {
      return "gaugescreen";
   }

   public String getTitle() {
      // TODO Auto-generated method stub
      return null;
   }

   public void setTitle(String title) {
      // TODO Auto-generated method stub

   }

   public IBProgessable getChild(IBRunnable runnable) {
      // TODO Auto-generated method stub
      return null;
   }

   public boolean isCanceled() {
      // TODO Auto-generated method stub
      return false;
   }

   public IBProgessable openNew(IBRunnable runnable) {
      // TODO Auto-generated method stub
      return null;
   }

   public void set(String title, String info, String label, int maxValue, int level) {
      // TODO Auto-generated method stub

   }

   public int getLvl() {
      // TODO Auto-generated method stub
      return 0;
   }

   public void setLvl(int lvl) {
      // TODO Auto-generated method stub
   }

   public void set(I8nString title, I8nString info, I8nString label, int maxValue, int level) {
      // TODO Auto-generated method stub

   }

   public void setLabel(I8nString s) {
      // TODO Auto-generated method stub

   }

   
   public void close() {
      // TODO Auto-generated method stub

   }

   public void setTimeLeft(long value) {
      // TODO Auto-generated method stub
      
   }

   public void error(String msg) {
      // TODO Auto-generated method stub
      
   }

   public void increment(int value) {
      // TODO Auto-generated method stub
      
   }

   public void close(String msg) {
      // TODO Auto-generated method stub
      
   }

}
