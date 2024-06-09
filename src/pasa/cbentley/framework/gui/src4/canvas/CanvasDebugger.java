package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.core.src4.helpers.StringBBuilder;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.coredraw.src4.interfaces.IMFont;
import pasa.cbentley.framework.coreui.src4.ctx.ToStringStaticCoreUi;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.string.CharOpt;
import pasa.cbentley.framework.gui.src4.anim.AnimManager;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.LayouterEngineDrawable;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ITechCtxSettingsAppGui;
import pasa.cbentley.framework.gui.src4.ctx.IToStringFlagsGui;
import pasa.cbentley.framework.gui.src4.ctx.ToStringStaticGui;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;
import pasa.cbentley.framework.gui.src4.utils.RenderMetrics;

/**
 * Drawable to paint debug info.
 * <br>
 * <br>
 * Depending on the orientation, 
 * draws stuff horizontally
 * draws stuff on many short lines
 * 
 * @author Charles Bentley
 *
 */
public class CanvasDebugger extends Drawable {

   protected int          countAnimRepaints;

   protected int          countDrawableRepaints;

   protected int          countFullRepaints;

   private int            debugMode      = 0;

   private boolean        isDebugPainting;

   private int            maxDiff;

   private int            minDiff;

   private int            numPaintCalls;

   private int            oldConsumed;

   private int            orientation;

   private StringBBuilder sb;

   protected int          totalPaintTime = 0;

   public CanvasDebugger(GuiCtx gc, StyleClass sc, ViewContext vc) {
      super(gc, sc, vc);
      sb = new StringBBuilder(gc.getUC());
      toStringSetName("CanvasDebugger");
   }

   /**
    * Prints painting info into a debug
    * @param g
    */
   private void debugPaintStart(GraphicsX g) {
      sb.append("#CanvasDebug Painting");
      sb.append("(" + Thread.currentThread().getName() + ")");
   }

   public void debugRender1End(GraphicsX g, RenderMetrics renderMetrics) {

      paintDebugLight(g, renderMetrics.getLastDuration());

      //#mdebug
      if (gc.toStringHasToStringFlag(IToStringFlagsGui.D_FLAG_28_MASTER_CANVAS)) {
      }
      //#enddebug      
   }

   public void debugRender1Start(GraphicsX g) {
      numPaintCalls++;
      //#mdebug
      if (isDebugPainting) {
         debugPaintStart(g);
      }
      //#enddebug

      if (g.hasPaintCtx(ITechCanvasDrawable.REPAINT_01_FULL))
         countFullRepaints++;
      if (g.hasPaintCtx(ITechCanvasDrawable.REPAINT_04_ANIMATION))
         countAnimRepaints++;
      if (g.hasPaintCtx(ITechCanvasDrawable.REPAINT_06_DRAWABLE)) {
         countDrawableRepaints++;
      }

   }

   /**
    * Creates a {@link StringBuilder} with debug data, send that buffer to {@link StringDrawable}.
    * set Tech flag to use No Memory while drawing.
    * 
    * @param g
    * @param time
    */
   private void drawDebugDirect(GraphicsX g, long time) {
      int duration = (int) (System.currentTimeMillis() - time);
      //System.out.println(duration);
      IMFont f = gc.getCDC().getFontFactory().getFontDebug();
      int primitiveTally = g.getPrimitivetTally();
      int rgbTally = g.getRgbCount();
      int numLayers = gc.getCanvasRoot().getVCAppli().getTopo().getNumLayers();
      g.setColor(255, 255, 255);
      g.setFont(f);
      g.fillRect(0, 0, getDrawnWidth(), f.getHeight() * 2);
      g.setColor(0);
      totalPaintTime += duration;
      int fps = 1000;
      if (duration > 0) {
         fps = 1000 / duration;
      }
      int avg = 0;
      int avgPersec = 0;
      if (countFullRepaints != 0) {
         avg = totalPaintTime / countFullRepaints;
         if (avg != 0)
            avgPersec = 1000 / avg;
      }
      //int consumed = DrwParamRepository.getMemConsumer();
      //int diffDrw = oldDrwParamConsumed - consumed;
      int freemem = (int) Runtime.getRuntime().freeMemory();
      int totalmem = (int) Runtime.getRuntime().totalMemory();
      int diff = totalmem - freemem - oldConsumed;
      oldConsumed = totalmem - freemem;
      if (diff < minDiff) {
         minDiff = diff;
      }
      if (diff > maxDiff) {
         maxDiff = diff;
      }
      //in kilo bytes
      int con = (totalmem - freemem) / 1000;
      int total = totalmem / 1000;
      int percent = (con * 100) / total;
      int persec = 0;
      if (duration != 0)
         persec = 1000 / duration;
      int dx = 0;
      int dy = 0;

      dx += CharOpt.draw(g, duration, dx, dy, f);
      dx += CharOpt.draw(g, " ms (", dx, dy, f);
      dx += CharOpt.draw(g, fps, dx, dy, f);
      dx += CharOpt.draw(g, " fps)  ", dx, dy, f);

      dx += CharOpt.draw(g, numPaintCalls, dx, dy, f);
      dx += CharOpt.draw(g, ":", dx, dy, f);
      dx += CharOpt.draw(g, countFullRepaints, dx, dy, f);
      dx += CharOpt.draw(g, ":", dx, dy, f);
      dx += CharOpt.draw(g, countAnimRepaints, dx, dy, f);
      dx += CharOpt.draw(g, ":", dx, dy, f);
      dx += CharOpt.draw(g, countDrawableRepaints, dx, dy, f);
      dx += CharOpt.draw(g, " ", dx, dy, f);
      dx += CharOpt.draw(g, primitiveTally, dx, dy, f);
      dx += CharOpt.draw(g, ",", dx, dy, f);
      dx += CharOpt.draw(g, rgbTally, dx, dy, f);
      dx += CharOpt.draw(g, ":", dx, dy, f);
      dx += CharOpt.draw(g, numLayers, dx, dy, f);
      dx += CharOpt.draw(g, " Ev:", dx, dy, f);
      //returns input state that triggered this repaint
      if (g instanceof GraphicsXD) {
         InputStateDrawable isd = ((GraphicsXD) g).isd;
         int x = isd.getX();
         dx += CharOpt.draw(g, x, dx, dy, f);
         dx += CharOpt.draw(g, ",", dx, dy, f);
         int y = isd.getY();
         dx += CharOpt.draw(g, y, dx, dy, f);
         dx += CharOpt.draw(g, " ", dx, dy, f);
         int mod = isd.getMode();
         dx += CharOpt.draw(g, ToStringStaticCoreUi.toStringMod(mod), dx, dy, f);
         int key = isd.getKeyCode();
         dx += CharOpt.draw(g, " ", dx, dy, f);
         dx += CharOpt.draw(g, key, dx, dy, f);
      }
      AnimManager ac = this.getAnimCreator();
      if (ac.getRealisator() != null) {
         int rs = ac.getRealisator().getSize();
         dx += CharOpt.draw(g, " ", dx, dy, f);
         dx += CharOpt.draw(g, rs, dx, dy, f);
         dx += CharOpt.draw(g, ":", dx, dy, f);
         dx += CharOpt.draw(g, (int) ac.getRealisator().getTimeTurns(), dx, dy, f);
         dx += CharOpt.draw(g, "ms", dx, dy, f);
      }
      dy += f.getHeight();
      dx = 0;

      String conUnit = "kb (";
      if (con > 10000) {
         con = con / 1000;
         total = total / 1000;
         conUnit = "mb (";
      }

      dx += CharOpt.draw(g, con, dx, dy, f);
      dx += CharOpt.draw(g, "/", dx, dy, f);
      dx += CharOpt.draw(g, total, dx, dy, f);
      dx += CharOpt.draw(g, conUnit, dx, dy, f);
      dx += CharOpt.draw(g, percent, dx, dy, f);
      dx += CharOpt.draw(g, "%) ", dx, dy, f);

      dx += CharOpt.draw(g, "MemDiff:", dx, dy, f);

      //compute the memory difference
      if (diff > -1100 && diff < 1100) {
         dx += CharOpt.draw(g, diff, dx, dy, f);
         dx += CharOpt.draw(g, "bytes", dx, dy, f);
      } else {
         diff /= 1000;
         dx += CharOpt.draw(g, diff, dx, dy, f);
         dx += CharOpt.draw(g, "kb", dx, dy, f);
      }
      int maxV = maxDiff;
      String maxUnit = "bytes";
      if (maxDiff > 2000) {
         maxV = maxDiff / 1000;
         maxUnit = "kb";
      } else if (maxDiff > 1000000) {
         maxV = maxDiff / 1000000;
         maxUnit = "mb";
      }
      int minV = minDiff;
      String minUnit = "bytes";
      if (minDiff < -2000) {
         minV = minDiff / 1000;
         minUnit = "kb";
      } else if (minDiff < -1000000) {
         minV = minDiff / 1000000;
         minUnit = "mb";
      }
      dx += CharOpt.draw(g, " Min:-", dx, dy, f);
      dx += CharOpt.draw(g, minV, dx, dy, f);
      dx += CharOpt.draw(g, minUnit, dx, dy, f);
      dx += CharOpt.draw(g, " Max:", dx, dy, f);
      dx += CharOpt.draw(g, maxV, dx, dy, f);
      dx += CharOpt.draw(g, maxUnit, dx, dy, f);

   }

   public void initDrawable(LayouterEngineDrawable ds) {
      //we don't touch width. we set our height
      IMFont f = gc.getCDC().getFontFactory().getFontDebug();
      setDh(f.getHeight() * 2);

   }

   /**
    * This is drawn at the very end of the draw method in {@link CanvasAppliInputGui}.
    * 
    * 
    * Draws the TopHeader displaying Debug Information about Processor Use during the Painting Cycle
    * and memory changes
    * TODO: make the debug stuff into a class similary to the Menu. overhaul the whole system.
    * MasterCanvas becomes MasterDrawable.
    * To be consistent. there is Full Area, and Business Area.
    * Full Area is over EVERYTHING even debug and menu
    * @param g
    * @param time
    */
   protected void paintDebugComplete(GraphicsXD g, long time) {
      //#debug
      gc.toDLog().pDraw("x=" + getX() + " y=" + getY(), null, CanvasDebugger.class, "paintDebugComplete@272");

      g.setTranslationShift(getX(), getY());

      if (debugMode == ITechCanvasDrawable.DEBUG_3_COMPLETE_2LINES) {
         //the main paint translated method
         //drawDebugAsBuffer(g, time);
         drawDebugDirect(g, time);
         //drawDebugAsString(g, time);
         g.clipReset();
      } else if (debugMode == ITechCanvasDrawable.DEBUG_2_COMPLETE_1LINE) {
         //the main paint translated method
         //drawDebugAsBuffer(g, time);
         drawDebugDirect(g, time);
         //drawDebugAsString(g, time);
         g.clipReset();
      }
      g.setTranslationShift(-getX(), -getY());
   }

   public int getSizePreferredHeight() {
      if (debugMode == ITechCanvasDrawable.DEBUG_2_COMPLETE_1LINE || debugMode == ITechCanvasDrawable.DEBUG_3_COMPLETE_2LINES) {
         return gc.getCDC().getFontFactory().getFontDebug().getHeight() * 2;
      } else {
         return gc.getCDC().getFontFactory().getFontDebug().getHeight();
      }
   }

   /**
    * Draw the small overlay millisecond in the top right corner
    * <br>
    * <br>
    * @param g
    * @param time
    */
   private void paintDebugLight(GraphicsX g, long duration) {
      if (debugMode == ITechCanvasDrawable.DEBUG_1_LIGHT) {
         IMFont f = gc.getCDC().getFontFactory().getFontDebug();
         String s = Long.toString(duration);
         int sw = f.stringWidth(s);
         g.setColor(0);
         g.fillRect(this.getDrawnWidth() - sw, 0, sw, f.getHeight());
         g.setFont(f);
         g.setColor(255, 255, 255);
         g.drawString(s, this.getDrawnHeight() - sw, 0, ITechCanvasDrawable.ANCHOR);
      }
   }

   public void reset() {
      countAnimRepaints = 0;
      totalPaintTime = 0;
      numPaintCalls = 0;
   }

   /**
    * <li>{@link ITechCanvasDrawable#DEBUG_0_NONE}
    * {@link ITechCtxSettingsAppGui#CTX_GUI_OFFSET_07_DEBUG_MODE1}
    * @param mode
    */
   public void setDebugMode(int mode) {
      debugMode = mode;
   }

   public void setOrientation(int pos) {
      orientation = pos;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, CanvasDebugger.class, 350);
      toStringPrivate(dc);
      super.toString(dc.sup());

      dc.appendVarWithNewLine("isDebugPainting", isDebugPainting);
      dc.appendVarWithNewLine("countAnimRepaints", countAnimRepaints);
      dc.appendVarWithNewLine("countDrawableRepaints", countDrawableRepaints);
      dc.appendVarWithNewLine("countFullRepaints", countFullRepaints);
      dc.appendVarWithNewLine("totalPaintTime", totalPaintTime);

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, CanvasDebugger.class, 350);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());

   }

   private void toStringPrivate(Dctx dc) {
      dc.appendVarWithSpace("DebugMode", ToStringStaticGui.toStringDebugMode(debugMode));
      dc.appendVarWithSpace("numPaintCalls", numPaintCalls);
   }
   //#enddebug

}
