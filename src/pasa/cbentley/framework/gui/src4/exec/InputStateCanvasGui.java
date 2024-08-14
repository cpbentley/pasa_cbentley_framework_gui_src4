package pasa.cbentley.framework.gui.src4.exec;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.cmd.src4.trigger.CmdTrigger;
import pasa.cbentley.framework.core.ui.src4.input.InputState;
import pasa.cbentley.framework.gui.src4.canvas.CanvasAppliInputGui;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.utils.DrawableArrays;
import pasa.cbentley.framework.gui.src4.utils.DrawableUtilz;
import pasa.cbentley.framework.input.src4.engine.CanvasAppliInput;
import pasa.cbentley.framework.input.src4.engine.InputStateCanvas;
import pasa.cbentley.framework.input.src4.gesture.GestureDetector;

/**
 * Created by the {@link CanvasAppliInputGui} is a {@link CanvasAppliInput} is a {@link DisplayableAbstract}.
 * <br>
 * Extends {@link InputState} to handle input on {@link IDrawable}s.
 * <br>
 * <li> tracks dragged {@link IDrawable}
 * <li> drawable under a pointer
 * 
 * @author Charles Bentley
 *
 */
public class InputStateCanvasGui extends InputStateCanvas {

   protected final GuiCtx gc;
   
   protected CanvasAppliInputGui canvasGui;

   public InputStateCanvasGui(GuiCtx gc, CanvasAppliInputGui mc) {
      super(gc.getIC(), mc);
      this.gc = gc;
   }

   private IDrawable[] gesturesD = new IDrawable[3];

   /**
    * The first {@link IDrawable} directly below  pointed drawable
    */
   private IDrawable   pointedDrawable;

   /**
    * Keeps running gestures
    * Removes all gestures and stop running tasks.
    * <br>
    * TODO keep gestures that survive a Pointer PRESS event. Pointer press slow them without killing them
    */
   public void cleanGestures() {
      for (int i = 0; i < gesturesD.length; i++) {
         if (gestures[i] != null) {
            gestures[i].endGesture();
         }
         gesturesD[i] = null;
         gestures[i] = null;
      }
   }

   public boolean isInside(IDrawable d) {
      return DrawableUtilz.isInside(this, d);
   }

   /**
    * Check whether the drawable is dragged in a gesture
    * @param viewedDrawable
    * @return
    */
   public boolean isDragged(IDrawable viewedDrawable) {
      for (int i = 0; i < gesturesD.length; i++) {
         if (gesturesD[i] == viewedDrawable) {
            return true;
         }
      }
      return false;
   }

   /**
    * Must return the same associated to the drawable.
    * <br>
    * What about concurrent gestures?
    * <br>
    * <br>
    * @return
    */
   public GestureDetector getOrCreateGesture(IDrawable d) {
      int index = -1;
      for (int i = 0; i < gesturesD.length; i++) {
         if (gesturesD[i] == d) {
            return gestures[i];
         }
         if (gesturesD[i] == null) {
            index = i;
         }
      }
      if (index == -1) {
         gesturesD = DrawableArrays.ensureCapacity(gesturesD, gesturesD.length);
         GestureDetector[] ds = new GestureDetector[gestures.length + 1];
         for (int i = 0; i < gestures.length; i++) {
            ds[i] = gestures[i];
         }
         gestures = ds;
         index = gestures.length - 1;
      }
      gestures[index] = new GestureDetector(gc.getIC(), (CanvasAppliInputGui)eventCanvas);
      gesturesD[index] = d;
      return gestures[index];
   }

   public void setPointedDrawable(IDrawable d) {
      pointedDrawable = d;
   }

   /**
    * Upon recieving events it creates a CmdTrigger.
    * When to clear it?
    * @return
    */
   public CmdTrigger getCmdTrigger() {
      // TODO Auto-generated method stub
      return null;
   }


   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, InputStateCanvasGui.class);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {
      
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, InputStateCanvasGui.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug
   


}
