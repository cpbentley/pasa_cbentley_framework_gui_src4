package pasa.cbentley.framework.gui.src4.anim.move;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.BitUtils;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.string.CharOpt;
import pasa.cbentley.framework.gui.src4.anim.base.DrawableAnim;
import pasa.cbentley.framework.gui.src4.anim.base.ImgAnimable;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.ViewPane;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.utils.DrawableArrays;

/**
 * Move animation on a 2D plane. {@link FunctionMove} decides the path.
 * <br>
 * <br>
 * Function could use a retrace like a ball rebounding
 * <br>
 * <br>
 * If the whole screen is repainted, no micro optimizations are made on bounds.
 * <br>
 * <br>
 * <b>Implementation</b>:
 * <br>
 * <br>
 * <br>
 * <br>
 * <b>Use Cases </b>
 * <ol>
 * <li>Move a small object across the screen on the top DLayer of {@link MasterCanvas}.
 * <li>Appearance or Transition: Move main {@link Drawable} to his defined position from a screen corner
 * <li>Moves content of a {@link ViewPane} when the user activate the scrollbar <br>
 * <ol>
 * <li>asks to draw ViewPort content on main Image<br>
 * <li>ask to draw ViewPort addition<br>
 * <li>adds a trail<br>
 * </ol>
 * </ol>
 * 
 * Tracks trail increments in memory to deal with further moves<br>
 * <br>
 * <br>
 * @author Charles-Philip Bentley
 *
 */
public class Move extends ImgAnimable {

   /**
    * if 25 pixels separate origin from dest
    * 8 + 5 + 3 + 2 + 1 + 1= 20
    * 25 - 8 = 17;
    * 17 - 5 = 12
    * 12 - 5 = 7
    * 7 - 3 = 4;
    * 4 - 2 = 2;
    * 1 - 1 =1;
    * 1 - 1 = 0;
    */
   public static int[]     fib                    = new int[] { 1, 1, 2, 3, 5, 8, 13, 21 };

   public static final int MOVE_FLAG_1_USE_CLIP   = 1;

   /**
    * In the case of Drawable, will it inherit the destination coordinate?
    * once the animation is finished or cut short
    */
   public static final int MOVE_FLAG_2_EFFECTIVE  = 1 << 1;

   public static final int MOVE_FLAG_3_DEBUG_TIME = 1 << 2;

   public static final int TRAIL_0_UP             = 0;

   public static final int TRAIL_1_DOWN           = 1;

   public static final int TRAIL_2_LEFT           = 2;

   public static final int TRAIL_3_RIGHT          = 3;

   private int[]           boundsRepaint          = new int[5];

   private int             cliph;

   private int             clipw;

   private int             clipx;

   private int             clipy;

   private int             flagsMove;

   private int             olddiff;

   /**
    * <li> {@link Move#TRAIL_0_UP} Default
    * <li> {@link Move#TRAIL_1_DOWN}
    * <li> {@link Move#TRAIL_2_LEFT}
    * <li> {@link Move#TRAIL_3_RIGHT}
    */
   private int             tblrTrail;

   public void setTrail(int trail) {
      tblrTrail = trail;
   }

   private long        time;

   /**
    * Draws those images next to each other starting from the main image
    * TrailDir defines the direction of the trail.
    * All images in the trail
    */
   private IDrawable[] trail;

   /**
    * Move of a Drawable 
    * @param d
    * @param f
    */
   public Move(GuiCtx gc, IDrawable d, FunctionMove f) {
      super(gc, d, f);
   }

   public Move(GuiCtx gc, IDrawable d, ByteObject def) {
      super(gc, d, def);
   }

   public void addTrail(IDrawable trailDrawable) {
      if (trail == null) {
         trail = new IDrawable[1];
         trail[0] = trailDrawable;
      } else {
         int index = trail.length;
         trail = DrawableArrays.ensureCapacity(trail, trail.length);
         trail[index] = trailDrawable;
      }
   }

   protected void doDebugTime(GraphicsX g, int x, int y) {
      if (hasFlagMove(MOVE_FLAG_3_DEBUG_TIME)) {
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
      d.setXY(mf.getDestX(), mf.getDestY());
      super.lifeEnd();
   }

   public void lifeStart() {
      setAnimFlag(ANIM_24_OVERRIDE_DRAW, true);
      setAnimFlag(ANIM_02_REVERSABLE, true);
      setInitialize();
   }

   public int nextTurn() {

      int val = super.nextTurn();
      boundsRepaint[0] = d.getX();
      boundsRepaint[1] = d.getY();
      boundsRepaint[2] = d.getDrawnWidth();
      boundsRepaint[3] = d.getDrawnWidth();

      //      MoveFunction mf = ((MoveFunction) stepFunction);
      //      mf.fx();
      //      int x = mf.getX();
      //      int y = mf.getY();
      //      d.setXY(x, y);
      return val;
   }

   /**
    * Paint the Img or the Drawable
    * 
    * @param g {@link GraphicsX}
    */
   public void paint(GraphicsX g) {
      FunctionMove mf = ((FunctionMove) stepFunction);
      mf.fx();
      int x = mf.getX();
      int y = mf.getY();
      //System.out.println("Drawing Move at " + x + ":" + y + " " + mf.toStringDebug());
      if (hasFlagMove(MOVE_FLAG_1_USE_CLIP)) {
         g.clipSet(clipx, clipy, clipw, cliph);
      }
      d.setXY(x, y);
      d.drawDrawable(g);
      paintTrail(g, x, y, d);
      if (hasFlagMove(MOVE_FLAG_1_USE_CLIP)) {
         g.clipReset();
      }
      doDebugTime(g, x, y);
   }

   protected void paintTrail(GraphicsX g, int x, int y, IDrawable d) {
      int dx = x;
      int dy = y;
      if (trail != null) {
         IDrawable prev = d;
         for (int i = 0; i < trail.length; i++) {
            switch (tblrTrail) {
               case TRAIL_0_UP:
                  dy -= prev.getDrawnHeight();
                  break;
               case TRAIL_1_DOWN:
                  dy += prev.getDrawnHeight();
                  break;
               case TRAIL_2_LEFT:
                  dx -= prev.getDrawnWidth();
                  break;
               case TRAIL_3_RIGHT:
                  dx += prev.getDrawnWidth();
                  break;
            }
            trail[i].setXY(dx, dy);
            trail[i].drawDrawable(g);
            prev = trail[i];
         }
      }
   }

   //   /** 
   //    * @param bg is the actual image on which the method draws
   //    * To erase a frame rectangle, you draw that image regio
   //    * @param g the actualy pixels on the screen. reference from the repaint method. 
   //    */
   //   public void paintErase(GraphicsX g, RgbImage bg) {
   //	 //MUtils.debug(boundsOldTurn);
   //	 //MUtils.debug(boundsTurn);
   //	 //if null no repaint is needed
   //	 if (isErasingOld) {
   //	    if (isEraseFullBounds) {
   //		  erase(g, bg, boundsRepaint);
   //	    } else {
   //		  //erase old turn and then next turn
   //		  erase(g, bg, boundsOldTurn);
   //		  //will draw the current turn. will not erase old turn
   //		  if (boundsTurn[4] != 0) {
   //			erase(g, bg, boundsTurn);
   //		  }
   //	    }
   //	 }
   //	 //get the boundary of the repaint for the new frame
   //
   //	 //now draws the drawable/image
   //	 paint(g);
   //   }

   /**
    * the Base class {@link DrawableAnim} will call resetCounter on the MoveFunction
    * {@link FunctionMove#resetCounter()}
    */
   public void reset() {
      super.reset();
   }

   /**
    * Sets a specific clip when drawing the move animation
    * @param x
    * @param y
    * @param w
    * @param h
    */
   public void setClip(int x, int y, int w, int h) {
      setFlagMove(MOVE_FLAG_1_USE_CLIP, true);
      clipx = x;
      clipy = y;
      clipw = w;
      cliph = h;
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
      dc.root(this, "Move");
      FunctionMove mf = (FunctionMove) stepFunction;
      dc.nlLvl(mf);
      boolean isUseClip = (hasFlagMove(MOVE_FLAG_1_USE_CLIP));
      dc.append("isClipping=" + isUseClip + " clip=" + clipx + "," + clipy + "-" + clipw + "," + cliph);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "Move");
   }

   //#enddebug

}
