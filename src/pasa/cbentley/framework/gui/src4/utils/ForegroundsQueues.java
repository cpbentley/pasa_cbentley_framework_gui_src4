package pasa.cbentley.framework.gui.src4.utils;

import java.util.Timer;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.canvas.CanvasAppliDrawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

public class ForegroundsQueues implements IStringable {
   /**
    * In effect frees up the first spot
    * @param ds
    */
   public static void shiftUp(RepaintTask[] ds) {
      for (int i = ds.length - 1; i > 0; i--) {
         ds[i] = ds[i - 1];
      }
      ds[0] = null;
   }

   private CanvasAppliDrawable canvas;

   private Timer      foregroundsTimer = new Timer();

   /**
    * Create a LinkedList.
    * There is a Repaint Task for every 
    */
   RepaintTask[][]    foress           = new RepaintTask[9][5];

   private GuiCtx     gc;

   public ForegroundsQueues(GuiCtx gc, CanvasAppliDrawable canvas) {
      this.gc = gc;
      this.canvas = canvas;
   }

   /**
    * Adds by default to {@link C#ANC_4_CENTER_CENTER}
    * @param foreDrw
    * @param millis
    */
   public synchronized void addForeground(IDrawable foreDrw, int millis) {
      addForeground(foreDrw, millis, C.ANC_4_CENTER_CENTER);
   }

   /**
    * Add a foreground drawable to be painted for xxx ms
    * @param foreDrw
    * @param millis
    * @param anchor {@link C#ANC_0_TOP_LEFT} ...
    */
   public synchronized void addForeground(IDrawable foreDrw, int millis, int anchor) {
      switch (anchor) {
         case C.ANC_0_TOP_LEFT:
            foreDrw.setXYLogic(C.LOGIC_1_TOP_LEFT, C.LOGIC_1_TOP_LEFT);
            break;
         case C.ANC_1_TOP_CENTER:
            foreDrw.setXYLogic(C.LOGIC_2_CENTER, C.LOGIC_1_TOP_LEFT);
            break;
         case C.ANC_2_TOP_RIGHT:
            foreDrw.setXYLogic(C.LOGIC_3_BOTTOM_RIGHT, C.LOGIC_1_TOP_LEFT);
            break;
         case C.ANC_3_CENTER_LEFT:
            foreDrw.setXYLogic(C.LOGIC_1_TOP_LEFT, C.LOGIC_2_CENTER);
            break;
         case C.ANC_5_CENTER_RIGHT:
            foreDrw.setXYLogic(C.LOGIC_3_BOTTOM_RIGHT, C.LOGIC_2_CENTER);
            break;
         case C.ANC_6_BOT_LEFT:
            foreDrw.setXYLogic(C.LOGIC_1_TOP_LEFT, C.LOGIC_3_BOTTOM_RIGHT);
            break;
         case C.ANC_7_BOT_CENTER:
            foreDrw.setXYLogic(C.LOGIC_2_CENTER, C.LOGIC_3_BOTTOM_RIGHT);
            break;
         case C.ANC_8_BOT_RIGHT:
            foreDrw.setXYLogic(C.LOGIC_3_BOTTOM_RIGHT, C.LOGIC_3_BOTTOM_RIGHT);
            break;
         default:
            foreDrw.setXYLogic(C.LOGIC_2_CENTER, C.LOGIC_2_CENTER);
            break;
      }
      foreDrw.init(0, 0);
      addForeToArray(foreDrw, millis, anchor);
   }

   public synchronized void addForeToArray(IDrawable foreDrw, int millis, int anchor) {
      RepaintTask[] fores = foress[anchor];
      synchronized (this) {
         if (fores[0] != null) {
            //shift all
            shiftUp(fores);
         }
         fores[0] = new RepaintTask(gc, foreDrw, millis, fores);
         try {
            foregroundsTimer.schedule(fores[0], millis);
         } catch (Exception e) {
            for (int i = 0; i < fores.length; i++) {
               fores[i] = null;
            }
            foregroundsTimer = new Timer();
            fores[0] = new RepaintTask(gc, foreDrw, millis, fores);
            foregroundsTimer.schedule(fores[0], millis);
         }
      }
   }

   /**
    * Generic Foreground behavior:
    * <li>Paint Once Until Next Repaint / TimeOut or/and NextEvent
    * Paint all weak Single/Timer Paints Drawable or Message
    * Foregrounds are not cacheable
    * <br>
    * <br>
    * Foreground are drawn top to left in the center
    * @param g
    */
   public void paintForegrounds(GraphicsX g) {

      paintRepaintTasksTop(g, foress[C.ANC_0_TOP_LEFT]);
      paintRepaintTasksTop(g, foress[C.ANC_1_TOP_CENTER]);
      paintRepaintTasksTop(g, foress[C.ANC_2_TOP_RIGHT]);

      paintRepaintTasksCenter(g, foress[C.ANC_3_CENTER_LEFT]);
      paintRepaintTasksCenter(g, foress[C.ANC_4_CENTER_CENTER]);
      paintRepaintTasksCenter(g, foress[C.ANC_5_CENTER_RIGHT]);

      paintRepaintTasksBottom(g, foress[C.ANC_6_BOT_LEFT]);
      paintRepaintTasksBottom(g, foress[C.ANC_7_BOT_CENTER]);
      paintRepaintTasksBottom(g, foress[C.ANC_8_BOT_RIGHT]);

   }

   private void paintRepaintTasksBottom(GraphicsX g, RepaintTask[] rts) {
      int y = canvas.getHeight();
      for (int i = 0; i < rts.length; i++) {
         if (rts[i] != null) {
            IDrawable d = rts[i].d;
            y -= d.getDrawnHeight();
            d.setY(y);
            d.drawDrawable(g);
         }
      }
   }

   private void paintRepaintTasksCenter(GraphicsX g, RepaintTask[] rts) {
      int y = canvas.getHeight() / 2;
      for (int i = 0; i < rts.length; i++) {
         if (rts[i] != null) {
            IDrawable d = rts[i].d;
            if (i != 0) {
               y -= d.getDrawnHeight();
            }
            d.setY(y);
            d.drawDrawable(g);
         }
      }
   }

   private void paintRepaintTasksTop(GraphicsX g, RepaintTask[] rts) {
      int y = 0;
      for (int i = 0; i < rts.length; i++) {
         if (rts[i] != null) {
            IDrawable d = rts[i].d;
            if (i != 0) {
               y += d.getDrawnHeight();
            }
            d.setY(y);
            d.drawDrawable(g);
         }

      }
   }

   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "ForegroundsQueues");
      for (int i = 0; i < foress.length; i++) {
         if (foress[i] != null) {
            for (int j = 0; j < foress[i].length; j++) {
               RepaintTask rt = foress[i][j];
               dc.nlLvl(i + ":" + j, rt);
            }
         }
      }
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root(this, "ForegroundsQueues");
   }

   public UCtx toStringGetUCtx() {
      return gc.getUCtx();
   }

   //#enddebug
}
