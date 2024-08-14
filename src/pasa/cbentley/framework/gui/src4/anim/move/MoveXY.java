package pasa.cbentley.framework.gui.src4.anim.move;

import pasa.cbentley.byteobjects.src4.objects.move.FunctionMove;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.BitUtils;
import pasa.cbentley.framework.gui.src4.anim.base.DrawableAnim;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

/**
 * Moves a {@link IDrawable} modifying X and Y with {@link IDrawable#setXY(int, int)}.
 * <br>
 * <br>
 * A {@link FunctionMove} decides the path.
 * <br>
 * 
 * @author Charles-Philip Bentley
 *
 */
public class MoveXY extends DrawableAnim {

   private int[]           boundsRepaint          = new int[5];

   private int             flagsMove;

   private int             olddiff;

   private long            time;

   /**
    * Draws those images next to each other starting from the main image
    * TrailDir defines the direction of the trail.
    * All images in the trail
    */
   private IDrawable[]     trail;

   /**
    * Move of a Drawable 
    * @param d
    * @param f
    */
   public MoveXY(GuiCtx gc, IDrawable d, FunctionMove f) {
      super(gc, d, f);
   }

   public int[] getBounds() {
      return boundsRepaint;
   }

   public boolean hasFlagMove(int flag) {
      return BitUtils.hasFlag(flagsMove, flag);
   }

   /**
    * If move is effective, automatically sets the {@link IDrawable} coordinates.
    */
   public void lifeEnd() {
      FunctionMove mf = (FunctionMove) stepFunction;
      mf.finish();
      d.setXY(mf.getDestX(), mf.getDestY());
      super.lifeEnd();
   }

   public void lifeStart() {
      setAnimFlag(ANIM_02_REVERSABLE, true);
      setInitialize();
   }

   public int nextTurn() {
      int val = super.nextTurn();
      boundsRepaint[0] = d.getX();
      boundsRepaint[1] = d.getY();
      boundsRepaint[2] = d.getDrawnWidth();
      boundsRepaint[3] = d.getDrawnWidth();
      FunctionMove mf = ((FunctionMove) stepFunction);
      mf.fx();
      int x = mf.getX();
      int y = mf.getY();
      d.setXY(x, y);
      return val;
   }

   public void setFlagMove(int flag, boolean v) {
      flagsMove = BitUtils.setFlag(flagsMove, flag, v);
   }

   /**
    * Some classes may instanciate a Move animation and run it. They will keep a reference and reuse it to dynamically set
    * a new destination.
    * <br>
    * <br>
    * Method asks the {@link FunctionMove} to move x,y in distance
    * 2 cases. 
    * <br>
    * <li>the call is made when the move is ongoing => A move is compute from the current position to the new offset destination.<br>
    * <li>the call is made when the move is finished => #2 New origin, new destination. 
    * <br>
    * <br>
    * @param xoffset
    * @param yoffset
    * 
    */
   public void setNewDestOffset(int xoffset, int yoffset) {
      FunctionMove mf = (FunctionMove) stepFunction;
      int dx = xoffset - mf.getDestX();
      int dy = yoffset - mf.getDestY();
      mf.shiftDest(dx, dy);
   }

   public void setShiftDestOffset(int dx, int dy) {
      FunctionMove mf = (FunctionMove) stepFunction;
      mf.shiftDest(dx, dy);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "MoveXY");
      FunctionMove mf = (FunctionMove) stepFunction;
      dc.nlLvl(mf);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "MoveXY");
   }

   //#enddebug

}
