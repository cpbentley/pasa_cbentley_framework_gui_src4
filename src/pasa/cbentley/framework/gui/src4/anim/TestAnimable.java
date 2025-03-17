package pasa.cbentley.framework.gui.src4.anim;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.BitUtils;
import pasa.cbentley.core.src4.utils.interfaces.IColors;
import pasa.cbentley.framework.coredraw.src4.interfaces.IGraphics;
import pasa.cbentley.framework.coredraw.src4.interfaces.ITechGraphics;
import pasa.cbentley.framework.gui.src4.canvas.GraphicsXD;
import pasa.cbentley.framework.gui.src4.interfaces.IAnimable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

/**
 * Animable to graphically display its state for debuggin animation framework
 * @author Charles-Philip Bentley
 *
 */
public class TestAnimable implements IAnimable {
   int[]         bounds = new int[5];

   private int   counter;

   private int   speed;

   protected int stateFlags;

   int           turn   = 0;

   int           type;

   int           x;

   int           y;

   int           zIndex = 0;

   public TestAnimable(int x, int y, int counter, int speed) {
      this.x = x;
      this.y = y;
      this.counter = counter;
      this.speed = speed;
      bounds = new int[] { x, y, 30, 30, IColors.FULLY_TRANSPARENT_WHITE };
   }

   public int[] getBounds() {
      return bounds;
   }

   public ByteObject getDefinition() {
      // TODO Auto-generated method stub
      return null;
   }

   public IDrawable getDrawable() {
      // TODO Auto-generated method stub
      return null;
   }

   public int getTarget() {
      // TODO Auto-generated method stub
      return 0;
   }

   public int getTiming() {
      // TODO Auto-generated method stub
      return 0;
   }

   public int getType() {
      return type;
   }

   public int getZIndex() {
      return zIndex;
   }

   public boolean hasAnimFlag(int flag) {
      return BitUtils.hasFlag(stateFlags, flag);
   }

   public boolean hasFlag() {
      // TODO Auto-generated method stub
      return false;
   }

   /**
    * Finished when 20 turns have been drawn
    */
   public boolean isFinished() {
      return turn == counter;
   }

   public void lifeEnd() {
      // TODO Auto-generated method stub

   }

   public void lifeStart() {
      // TODO Auto-generated method stub

   }

   public void lifeStartUIThread() {
      // TODO Auto-generated method stub

   }

   public void loadHeavyResources() {
      // TODO Auto-generated method stub

   }

   /**
    * First turn prepares animation for the first animation frame
    * If there is no next turn, method returns -1 and the paint method will not be called again
    * during next repaint
    */
   public int nextTurn() {
      turn++;
      if (turn == counter)
         return -1;
      return speed;
   }

   public void paint(GraphicsXD g) {
      // TODO Auto-generated method stub

   }

   public void paint(IGraphics g) {
      g.drawString("Turn #" + turn + " speed=" + speed, x, y, ITechGraphics.TOP | ITechGraphics.LEFT);
   }

   public void race() {
      // TODO Auto-generated method stub

   }

   public void reset() {
      // TODO Auto-generated method stub

   }

   public void setAnimFlag(int state, boolean v) {
      // TODO Auto-generated method stub

   }

   public void setRepeat(int repeat) {
      // TODO Auto-generated method stub

   }

   public void setSleep(int speed) {
      // TODO Auto-generated method stub

   }

   public void toString(Dctx dc) {
      // TODO Auto-generated method stub

   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   
   public void toString1Line(Dctx dc) {
      // TODO Auto-generated method stub

   }

   
   public UCtx toStringGetUCtx() {
      // TODO Auto-generated method stub
      return null;
   }

   public String toStringOneLine() {
      return "TestAnimable";
   }

}
