package pasa.cbentley.framework.gui.src4.anim.move;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.BitUtils;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.string.CharOpt;
import pasa.cbentley.framework.gui.src4.anim.base.DrawableAnim;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.utils.DrawableArrays;

/**
 * Does not move the underlying IDrawable.
 * <br>
 * <br>
 * Instead Draws moving ghost images according to the {@link FunctionMove}.
 * @author Charles-Philip Bentley
 *
 */
public class MoveGhost extends DrawableAnim {

   private int[]           boundsRepaint          = new int[5];

   private int             cliph;

   private int             clipw;

   private int             clipx;

   private int             clipy;

   private int             flagsMove;

   /**
    * Draws those Ghosts {@link IDrawable} next to each other starting from the main image
    * <br>
    * TrailDir defines the direction of the trail.
    * All images in the trail
    */
   private IDrawable[]     ghosts;

   /**
    * Relative to the previous ghost
    */
   private int[]           ghostsTrails;

   private int             olddiff;

   /**
    * <li> {@link ITechMoveFunction#TRAIL_0_UP} Default
    * <li> {@link ITechMoveFunction#TRAIL_1_DOWN}
    * <li> {@link ITechMoveFunction#TRAIL_2_LEFT}
    * <li> {@link ITechMoveFunction#TRAIL_3_RIGHT}
    */
   private int             tblrTrail;

   private long            time;

   private int             trailNextEmpty;

   /**
    * Move of a Drawable 
    * @param d
    * @param f
    */
   public MoveGhost(GuiCtx gc, IDrawable d, FunctionMove f) {
      super(gc, d, f);
   }

   /**
    * 
    * @param trailDrawable
    */
   public void addTrail(IDrawable trailDrawable) {
      addTrail(trailDrawable, ITechMoveFunction.TRAIL_0_UP);
   }

   /**
    * Trail is relative to the previous {@link IDrawable} added with this method.
    * <br>
    * <br>
    * Thus the first trail is meaningless.
    * <br>
    * <br>
    * The {@link IDrawable}s added with this method will be drawn instead of the {@link IDrawable} hosted by this animation.
    * <br>
    * <br>
    * @param trailDrawable
    * @param trail
    */
   public void addTrail(IDrawable trailDrawable, int trail) {
      if (ghosts == null) {
         ghosts = new IDrawable[] { trailDrawable };
         ghostsTrails = new int[] { trail };
      } else {
         int index = trailNextEmpty;
         ghosts = DrawableArrays.ensureCapacity(ghosts, index);
         ghostsTrails = gc.getUC().getMem().ensureCapacity(ghostsTrails, index);
         ghosts[index] = trailDrawable;
         ghostsTrails[index] = trail;
         trailNextEmpty++;
      }

   }

   protected void doDebugTime(GraphicsX g, int x, int y) {
      if (hasFlagMove(ITechMoveFunction.MOVE_FLAG_3_DEBUG_TIME)) {
         long cur = System.currentTimeMillis();
         int diff = (int) (cur - time);
         if (Math.abs(olddiff - diff) <= 1) {
            diff = olddiff;
         }
         olddiff = diff;
         CharOpt.draw(g, diff, x, y, g.getRefFont(), 0, -1);
         time = cur;
      }
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
      super.lifeEnd();
   }

   public void lifeStart() {
      setAnimFlag(ANIM_25_OVERRIDE_DRAW_CONTENT, true);
      setInitialize();
   }

   public void nextTurnSub() {
      FunctionMove mf = ((FunctionMove) stepFunction);
      mf.fx();

      boundsRepaint[0] = d.getX();
      boundsRepaint[1] = d.getY();
      boundsRepaint[2] = d.getDrawnWidth();
      boundsRepaint[3] = d.getDrawnWidth();

   }

   /**
    * Paint the Img or the Drawable
    * 
    * @param g {@link GraphicsX}
    */
   public void paint(GraphicsX g) {
      FunctionMove mf = ((FunctionMove) stepFunction);
      int x = mf.getX();
      int y = mf.getY();
      //System.out.println("Drawing Move at " + x + ":" + y + " " + mf.toStringDebug());
      if (hasFlagMove(ITechMoveFunction.MOVE_FLAG_1_USE_CLIP)) {
         g.clipSet(clipx, clipy, clipw, cliph);
      }
      paintTrail(g, x, y);
      if (hasFlagMove(ITechMoveFunction.MOVE_FLAG_1_USE_CLIP)) {
         g.clipReset();
      }
      doDebugTime(g, x, y);
   }

   protected void paintTrail(GraphicsX g, int x, int y) {
      int dx = x;
      int dy = y;
      if (ghosts != null) {
         IDrawable prev = d;
         for (int i = 0; i < ghosts.length; i++) {
            ghosts[i].setXY(dx, dy);
            ghosts[i].drawDrawable(g);
            prev = ghosts[i];
            g.clipSet(0, 0, 1000, 1000, GraphicsX.CLIP_DIRECTIVE_1_OVERRIDE);
            ghosts[i].setXY(dx + 200, dy + 200);
            ghosts[i].drawDrawable(g);
            g.clipReset();
            switch (ghostsTrails[i]) {
               case ITechMoveFunction.TRAIL_0_UP:
                  dy -= prev.getDrawnHeight();
                  break;
               case ITechMoveFunction.TRAIL_1_DOWN:
                  dy += prev.getDrawnHeight();
                  break;
               case ITechMoveFunction.TRAIL_2_LEFT:
                  dx -= prev.getDrawnWidth();
                  break;
               case ITechMoveFunction.TRAIL_3_RIGHT:
                  dx += prev.getDrawnWidth();
                  break;
            }
         }
      }
   }

   /**
    * the Base class {@link DrawableAnim} will call resetCounter on the MoveFunction
    * {@link FunctionMove#resetCounter()}
    */
   public void reset() {
      super.reset();
   }

   public void resetTrail() {
      trailNextEmpty = 0;
   }

   /**
    * Sets a specific clip when drawing the move animation
    * @param x
    * @param y
    * @param w
    * @param h
    */
   public void setClip(int x, int y, int w, int h) {
      setFlagMove(ITechMoveFunction.MOVE_FLAG_1_USE_CLIP, true);
      clipx = x;
      clipy = y;
      clipw = w;
      cliph = h;
   }

   public void setFlagMove(int flag, boolean v) {
      flagsMove = BitUtils.setFlag(flagsMove, flag, v);
   }

   public void setMoveFunction(FunctionMove mf) {
      stepFunction = mf;
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

   public void setTrail(int trail) {
      tblrTrail = trail;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "MoveGhost");
      FunctionMove mf = (FunctionMove) stepFunction;
      dc.nlLvl(mf);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "MoveGhost");
   }

   //#enddebug

}
