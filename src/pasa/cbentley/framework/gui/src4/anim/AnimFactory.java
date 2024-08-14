package pasa.cbentley.framework.gui.src4.anim;

import pasa.cbentley.byteobjects.src4.core.BOAbstractFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.objects.function.Function;
import pasa.cbentley.byteobjects.src4.objects.function.IBOFunction;
import pasa.cbentley.byteobjects.src4.objects.function.ITechFunction;
import pasa.cbentley.byteobjects.src4.objects.move.FunctionMove;
import pasa.cbentley.byteobjects.src4.objects.move.IBOMoveFunction;
import pasa.cbentley.byteobjects.src4.objects.move.ITechMoveFunction;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.framework.drawx.src4.ctx.IBOTypesDrawX;
import pasa.cbentley.framework.drawx.src4.factories.interfaces.IBOFigure;
import pasa.cbentley.framework.gui.src4.anim.definitions.AlphaChangeRgb;
import pasa.cbentley.framework.gui.src4.anim.definitions.ByteObjectAnim;
import pasa.cbentley.framework.gui.src4.anim.definitions.ITechShiftLine;
import pasa.cbentley.framework.gui.src4.anim.definitions.Pixeler;
import pasa.cbentley.framework.gui.src4.anim.definitions.Reverse;
import pasa.cbentley.framework.gui.src4.anim.definitions.ShiftLines;
import pasa.cbentley.framework.gui.src4.anim.definitions.SizeMod;
import pasa.cbentley.framework.gui.src4.anim.move.Move;
import pasa.cbentley.framework.gui.src4.canvas.ViewContext;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.interfaces.IAnimable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

public class AnimFactory extends BOAbstractFactory implements IBOAnim, ITechShiftLine, ITechAnim {

   protected final GuiCtx gc;

   public AnimFactory(GuiCtx gc) {
      super(gc.getBOC());
      this.gc = gc;
   }

   /**
    * Maps a descriptive definition of an animation, to its concrete class and the figure it animates.
    * <br>
    * <br>
    * Called when an animated Figure is set (content)
    * or 
    * when a style layer is animated (1-8 id)
    * <br>
    * Animated style layers use this method.
    * <br>
    * <br>
    * @param anim description of the animation
    * @param d Drawable to be repainted at each animation step
    * @return
    */
   public IAnimable createDrawableAnimable(ByteObject anim, IDrawable d) {
      int animType = anim.get1(ANIM_OFFSET_01_TYPE1);
      ByteObject function = anim.getSubFirst(IBOTypesBOC.TYPE_021_FUNCTION);
      IAnimable ianim = null;
      switch (animType) {
         case ANIM_TYPE_01_VALUE:
         case ANIM_TYPE_08_GRADIENT:
            //create a step function depending on the type of animation and based on gradient definition.
            return new ByteObjectAnim(gc, d, anim);
         case ANIM_TYPE_02_REVERSE:
            return new Reverse(gc, d, anim);
         case ANIM_TYPE_03_MOVE:
            //
            int position = function.get1(IBOMoveFunction.MOVE_OFFSET_1TYPE1);
            boolean origin = anim.get1(ANIM_OFFSET_05_TIME1) == ANIM_TIME_1_ENTRY;
            int moveType = function.get1(IBOMoveFunction.MOVE_OFFSET_1TYPE1);
            boolean isHidden = function.hasFlag(IBOFunction.FUN_OFFSET_02_FLAG, IBOFunction.FUN_FLAG_7_CUSTOM);
            FunctionMove f = getMoveFunction(d, d.getVC(), origin, position, isHidden, moveType);
            ianim = new Move(gc, d, f);
            break;
         case ANIM_TYPE_04_PIXELATE:
            ianim = new Pixeler(gc, d, anim, null);
            break;
         case ANIM_TYPE_05_LINE_SHIFT:
            ianim = new ShiftLines(gc, d, anim);
            break;
         case ANIM_TYPE_06_ALPHA:
            ianim = new AlphaChangeRgb(gc, d, anim);
            break;
         default:
            break;
      }
      return ianim;
   }

   /**
    * Default set of appearing animations.
    * <li>Move
    * <li>Alpha
    * <li>Shift
    * <li>
    * @param type
    * @return
    */
   public ByteObject getAnimAppearing(int type) {

      return null;
   }

   /**
    * Default Alpha Function
    * @param type
    * @param up
    * @param repeat
    * @return
    */
   public ByteObject getAnimationAlpha(int type, boolean up, int repeat) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_11_ANIMATION, ANIM_BASIC_SIZE);
      p.setValue(ANIM_OFFSET_01_TYPE1, ANIM_TYPE_06_ALPHA, 1);
      p.setValue(ANIM_OFFSET_05_TIME1, type, 1);
      p.setFlag(ANIM_OFFSET_02_FLAG, ANIM_FLAG_8_CUSTOM, up);
      setAnimationRepeat(p, repeat);
      return p;
   }

   public ByteObject getAnimationAlpha(int type, ByteObject function, ByteObject sleepFunct, int repeat) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_11_ANIMATION, ANIM_BASIC_SIZE);
      p.setValue(ANIM_OFFSET_01_TYPE1, ANIM_TYPE_06_ALPHA, 1);
      p.setValue(ANIM_OFFSET_05_TIME1, type, 1);
      if (repeat == -1) {
         p.setFlag(ANIM_OFFSET_02_FLAG, ANIM_FLAG_3_LOOP, true);
      } else {
         p.setValue(ANIM_OFFSET_07_REPEAT1, repeat, 1);
      }
      p.setByteObjects(new ByteObject[] { function });
      return p;
   }

   public ByteObject getAnimationAlpha(int type, int repeat, int[] values, int sleep) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_11_ANIMATION, ANIM_BASIC_SIZE);
      p.setValue(ANIM_OFFSET_01_TYPE1, ANIM_TYPE_06_ALPHA, 1);
      p.setValue(ANIM_OFFSET_05_TIME1, type, 1);
      p.setValue(ANIM_OFFSET_08_SLEEP2, sleep, 2);
      setAnimationRepeat(p, repeat);
      ByteObject func = boc.getFunctionFactory().getFunctionValues(values);
      p.addByteObject(func);
      return p;
   }

   public ByteObject getAnimationValue(ByteObject pointer, ByteObject function, int time, int target, int sleep, int repeat, ByteObject sleepFun) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_11_ANIMATION, ANIM_BASIC_SIZE);
      p.set1(ANIM_OFFSET_01_TYPE1, ANIM_TYPE_01_VALUE);
      p.set1(ANIM_OFFSET_04_DRAW_TYPE1, ANIM_DRAW_2_DRAWABLE);
      p.set1(ANIM_OFFSET_05_TIME1, time);
      p.set1(ANIM_OFFSET_06_TARGET1, target);
      if (repeat == -1) {
         p.setFlag(ANIM_OFFSET_02_FLAG, ANIM_FLAG_3_LOOP, true);
         p.set1(ANIM_OFFSET_07_REPEAT1, 0);
      } else {
         p.set1(ANIM_OFFSET_07_REPEAT1, repeat);
      }
      p.setByteObjects(new ByteObject[] { function, pointer });
      if (sleepFun == null) {
         p.set2(ANIM_OFFSET_08_SLEEP2, sleep);
      } else {
         p.addByteObject(sleepFun);
      }

      return p;
   }

   /**
    * Animates a value defined by pointer with function.
    * 
    * Default sleep value
    * @param timing Decide when the animation is to be played
    * @param pointer
    * @param function y=f(x) where x is the old value and y is the new value
    * @return
    */
   public ByteObject getAnimationValue(int timing, ByteObject pointer, ByteObject function) {
      return getAnimationValue(pointer, function, timing, 0, 50, -1, null);
   }

   public ByteObject getAnimationValue(int timing, ByteObject pointer, ByteObject function, ByteObject sleep) {
      return getAnimationValue(timing, pointer, function, sleep, -1);
   }

   /**
    * 
    * @param timing
    * @param pointer
    * @param function Maybe a gradient in which case flag is set
    * @param sleep Will be stored as a sleep function
    * @param repeat
    * @return
    */
   public ByteObject getAnimationValue(int timing, ByteObject pointer, ByteObject function, ByteObject sleep, int repeat) {
      return getAnimationValue(pointer, function, timing, 0, 0, repeat, sleep);
   }

   public ByteObject getAnimationValue(int type, ByteObject pointer, ByteObject function, int sleep) {
      return getAnimationValue(pointer, function, type, 0, sleep, 0, null);
   }

   public ByteObject getAnimBase(int timing) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_11_ANIMATION, ANIM_BASIC_SIZE);
      p.setValue(ANIM_OFFSET_05_TIME1, timing, 1);
      return p;
   }

   /**
    * Move chunks of an area to predefined screen point.
    * <br>
    * Cuts the area in  2 or 4, 5 chunks and each moves in opposite direction
    * <br>
    * Moves go either 
    * <li>TopLeft/TopRight/BotLeft/BotRight
    * <li>Top/Bot/Left/Right
    * <li>Top/Bot
    * <li>Left/Right
    * 
    * @param fct
    * @return
    */
   public ByteObject getAnimChunkMove(int type) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_11_ANIMATION, ANIM_BASIC_SIZE);
      p.setValue(ANIM_OFFSET_01_TYPE1, ANIM_TYPE_03_MOVE, 1);
      return p;
   }

   public ByteObject getAnimDisappearing(int type) {

      return null;
   }

   /**
    * Animation that reads the color of a {@link ITechFigureTypes#TYPE_DRWX_00_FIGURE} and computes a gradient.
    * <br>
    * <br>
    * {@link ITechFigureTypes#TYPE_038_GRADIENT} hosts the final color with {@link IGradient#GRADIENT_OFFSET_04_COLOR4}.
    * <br>
    * {@link IGradient#GRADIENT_OFFSET_07_STEP1} contains the number of gradient steps.
    * <br>
    * <br>
    * 
    * Function to generate the values is initialized based on input figure base color.
    * <br>
    * <br>
    * @param pointer pointer to the value to be gradient changed. could possibly be null for style layers gradient.
    * @param gradient defines the gradient function to apply
    * @param timing
    * @return
    */
   public ByteObject getAnimGradient(ByteObject pointer, ByteObject gradient, int timing, int target, int size) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_11_ANIMATION, ANIM_BASIC_SIZE);
      p.setValue(ANIM_OFFSET_01_TYPE1, ANIM_TYPE_08_GRADIENT, 1);
      p.setValue(ANIM_OFFSET_05_TIME1, timing, 1);
      p.setValue(ANIM_OFFSET_06_TARGET1, target, 1);
      p.set2(ANIM_OFFSET_09_NUM_STEPS2, size);
      p.addByteObject(pointer);
      p.addByteObject(gradient);
      return p;
   }

   /**
    * Function that decides how is moved the figure/drawable.
    * @param fct
    * @return
    */
   public ByteObject getAnimMove(ByteObject fct) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_11_ANIMATION, ANIM_BASIC_SIZE);
      p.setValue(ANIM_OFFSET_01_TYPE1, ANIM_TYPE_03_MOVE, 1);
      return p;
   }

   /**
    * Values are generated by other module
    * @param type
    * @param origine
    * @param anchor2
    * @return
    */
   public ByteObject getAnimMove(int type, boolean origine, ByteObject anchor2) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_11_ANIMATION, ANIM_BASIC_SIZE);
      p.setValue(ANIM_OFFSET_01_TYPE1, ANIM_TYPE_03_MOVE, 1);
      return p;
   }

   public ByteObject getAnimMove(int time, ByteObject fct) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_11_ANIMATION, ANIM_BASIC_SIZE);
      p.setValue(ANIM_OFFSET_01_TYPE1, ANIM_TYPE_03_MOVE, 1);
      p.setValue(ANIM_OFFSET_05_TIME1, time, 1);
      return p;
   }

   /**
    * Pixelize the area out or in
    * @param type according to type, pixelate in or out?
    * @param b
    * @param object
    * @return
    */
   public ByteObject getAnimPixel(int type, boolean isOut) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_11_ANIMATION, ANIM_BASIC_SIZE);
      p.setValue(ANIM_OFFSET_01_TYPE1, ANIM_TYPE_04_PIXELATE, 1);
      p.setValue(ANIM_OFFSET_05_TIME1, type, 1);
      p.setFlag(ANIM_OFFSET_02_FLAG, ANIM_FLAG_8_CUSTOM, isOut);
      return p;
   }

   /**
    * Animation that look up the opposite animation and animates in reverse, if that makes sense at the level of the animation genetics
    * <li> {@link ByteObject#ANIM_TIME_1_ENTRY}
    * <li> {@link ByteObject#ANIM_TIME_2_EXIT}
    * 
    * @param timing
    * @return
    */
   public ByteObject getAnimReverse(int timing) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_11_ANIMATION, ANIM_BASIC_SIZE);
      p.setValue(ANIM_OFFSET_01_TYPE1, ANIM_TYPE_02_REVERSE, 1);
      p.setValue(ANIM_OFFSET_05_TIME1, timing, 1);
      return p;
   }

   /**
    * 
    * @param in
    * @param leftToRight
    * @param clip
    * @return
    */
   public ByteObject getAnimShiftLine(boolean in, boolean leftToRight, boolean clip, int shiftBaseSize) {
      ByteObject t = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_13_ANIM, TECH_BASIC_SIZE);
      t.setFlag(TECH_OFFSET_1_FLAG, TECH_FLAG_5CLIP, in);
      t.setFlag(TECH_OFFSET_1_FLAG, TECH_FLAG_2REVERSE, leftToRight);
      t.setFlag(TECH_OFFSET_1_FLAG, TECH_FLAG_4MULTIPLY, true);
      t.setValue(TECH_OFFSET_3_SHIFT_SIZE2, shiftBaseSize, 2);
      return t;
   }

   public ByteObject getAnimShiftLines(int timing) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_11_ANIMATION, ANIM_BASIC_SIZE);
      p.setValue(ANIM_OFFSET_01_TYPE1, ANIM_TYPE_05_LINE_SHIFT, 1);
      p.setValue(ANIM_OFFSET_05_TIME1, timing, 1);
      return p;
   }

   public ByteObject getFunctionMoveFrom(int originType) {
      return getFunctionMoveFrom(originType, ITechMoveFunction.TYPE_MOVE_0_ASAP, true);
   }

   /**
    * 
    * @param originType {@link C#DIR_0_TOP}
    * @param moveType  {@link ITechMoveFunction#TYPE_MOVE_0_ASAP} ...
    * @param isHidden
    * @return
    */
   public ByteObject getFunctionMoveFrom(int originType, int moveType, boolean isHidden) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesBOC.TYPE_021_FUNCTION, IBOMoveFunction.FUN_BASIC_SIZE_MOVE);
      p.setValue(IBOMoveFunction.FUN_OFFSET_08_POST_OPERATOR1, originType, 1);
      p.setValue(IBOMoveFunction.FUN_OFFSET_07_AUX_OPERATOR1, moveType, 1);
      p.setFlag(IBOMoveFunction.FUN_OFFSET_02_FLAG, IBOMoveFunction.FUN_FLAG_7_CUSTOM, isHidden);

      return p;
   }

   public Move getMoveDrawableTo(IDrawable d, int xd, int yd) {
      Move m = new Move(gc, d, getMoveFunction(ITechMoveFunction.TYPE_MOVE_0_ASAP, d.getX(), d.getY(), xd, yd));
      return m;
   }

   public FunctionMove getMoveFunction(IDrawable d, ViewContext vc, boolean origin, int position) {
      int x = d.getX();
      int y = d.getY();
      int w = d.getDrawnWidth();
      int h = d.getDrawnHeight();
      int rx = 0;
      int ry = 0;
      int rw = vc.getWidth();
      int rh = vc.getHeight();
      return getMoveFunction(x, y, w, h, origin, position, rx, ry, rw, rh);
   }

   /**
    * Moves Drawable to/from 
    * <li>{@link C#DIR_0_TOP}
    * <li>{@link C#DIR_1_BOTTOM}
    * <li>{@link C#DIR_2_LEFT}
    * <li>{@link C#DIR_3_RIGHT}
    * <li>{@link C#DIR_4_TopLeft}
    * <li>{@link C#DIR_5_TopRight}
    * <li>{@link C#DIR_6_BotLeft}
    * <li>{@link C#DIR_7_BotRight}
    * <br>
    * of {@link MasterCanvas}.
    * <br>
    * <br>
    * @param d {@link Drawable} with its initialized x,y coordinate
    * @param origin true = > to (Exit) , false from (Entry)
    * @param position {@link C#DIR_0_TOP}, {@link C#DIR_4_TopLeft}
    * @param hidden decides if amplitude hides completely or Drawable stays completely visible
    * X. Y of drawable being the TopLeft coordinate.
    * @return
    */
   public FunctionMove getMoveFunction(IDrawable d, ViewContext vc, boolean origin, int position, boolean isHidden, int moveType) {
      int x = d.getX();
      int y = d.getY();
      int w = d.getDrawnWidth();
      int h = d.getDrawnHeight();
      int rx = 0;
      int ry = 0;
      int rw = vc.getWidth();
      int rh = vc.getHeight();
      return getMoveFunction(x, y, w, h, moveType, position, origin, isHidden, rx, ry, rw, rh);

   }

   public FunctionMove getMoveFunction(int x, int y, int w, int h, boolean origin, int position, int rx, int ry, int rw, int rh) {
      return getMoveFunction(x, y, w, h, ITechMoveFunction.TYPE_MOVE_0_ASAP, position, origin, true, rx, ry, rw, rh);
   }

   /**
    * 
    * Move of type
    * 
    * @param type
    * @param xo
    * @param yo
    * @param xd
    * @param yd
    * @return
    * 
    */
   public FunctionMove getMoveFunction(int type, int xo, int yo, int xd, int yd) {
      FunctionMove mf = new FunctionMove(gc.getBOC(), type, xo, yo, xd, yd);
      return mf;
   }

   /**
    * Generic move function of two rectangular areas relative to each other.
    * <br>
    * [x,y w,h] is the origin object
    * @param dx Drawable x
    * @param dy Drawable y coordinate
    * @param dw {@link Drawable#getDrawnWidth()}
    * @param dh {@link Drawable#getDrawnHeight()}
    * @param moveType {@link ITechMoveFunction#TYPE_MOVE_0_ASAP} or   {@link ITechMoveFunction#TYPE_MOVE_1_BRESENHAM}
    * @param position {@link C#DIR_0_TOP} = move rectangle to the top, {@link C#DIR_7_BotRight} moves to the bottom right of destination rectangle.
    * @param isTo <code>true</code> if x to r, <code>false</code> r to x
    * @param isHidden when true, the moving rectangle moves its coordinate so intersection is null.
    * @param rx x coordinate of destination rectangle
    * @param ry
    * @param rw
    * @param rh
    * @return
    */
   public FunctionMove getMoveFunction(int dx, int dy, int dw, int dh, int moveType, int position, boolean isTo, boolean isHidden, int rx, int ry, int rw, int rh) {
      int xOrigin = 0;
      int yOrigin = 0;
      int xDest = 0;
      int yDest = 0;
      int mx = 0;
      int my = 0;
      if (isHidden) {
         switch (position) {
            case C.DIR_0_TOP:
               mx = dx;
               my = ry - dh;
               break;
            case C.DIR_1_BOTTOM:
               mx = dx;
               my = ry + rh;
               break;
            case C.DIR_2_LEFT:
               mx = rx - dw;
               my = dy;
               break;
            case C.DIR_3_RIGHT:
               mx = rx + rw;
               my = dy;
               break;
            case C.DIR_4_TopLeft:
               mx = rx - dw;
               my = ry - dh;
               break;
            case C.DIR_5_TopRight:
               mx = rw;
               my = ry - dh;
               break;
            case C.DIR_6_BotLeft:
               mx = rx - dw;
               my = ry + rh;
               break;
            case C.DIR_7_BotRight:
               mx = rw;
               my = ry + rh;
               break;
            default:
               break;
         }
      } else {
         switch (position) {
            case C.DIR_0_TOP:
               mx = dx;
               my = ry;
               break;
            case C.DIR_1_BOTTOM:
               mx = dx;
               my = ry + rh - dh;
               break;
            case C.DIR_2_LEFT:
               mx = rx;
               my = dy;
               break;
            case C.DIR_3_RIGHT:
               mx = rx + rw - dw;
               my = dy;
               break;
            case C.DIR_4_TopLeft:
               mx = rx;
               my = ry;
               break;
            case C.DIR_5_TopRight:
               mx = rx + rw - dw;
               my = ry;
               break;
            case C.DIR_6_BotLeft:
               mx = rx;
               my = ry + rh - dh;
               break;
            case C.DIR_7_BotRight:
               mx = rx + rw - dw;
               my = ry + rh - dh;
               break;
            default:
               break;
         }
      }
      if (isTo) {
         xOrigin = dx;
         yOrigin = dy;
         xDest = mx;
         yDest = my;
      } else {
         xOrigin = mx;
         yOrigin = my;
         xDest = dx;
         yDest = dy;
      }
      FunctionMove mf = new FunctionMove(gc.getBOC(), moveType, xOrigin, yOrigin, xDest, yDest);
      return mf;
   }

   public FunctionMove getMoveFunction(int x, int y, int w, int h, int position, int rx, int ry, int rw, int rh) {
      return getMoveFunction(x, y, w, h, ITechMoveFunction.TYPE_MOVE_0_ASAP, position, false, true, rx, ry, rw, rh);
   }

   /**
    * Function for Moving a {@link IDrawable} from a TBLR postion to his current x,y.
    * <br>
    * <br>
    * The referential is the Root Topology ( {@link MasterCanvas} virtual container ).
    * <br>
    * <br>
    * Position
    * <li> {@link C#DIR_0_TOP}
    * <li> {@link C#DIR_1_BOTTOM}
    * <li> {@link C#DIR_2_LEFT}
    * <li> {@link C#DIR_3_RIGHT}
    * <li> {@link C#DIR_4_TopLeft}
    * <li> {@link C#DIR_5_TopRight}
    * <li>...
    * <br>
    * <br>
    * @param d
    * @param position
    * @return
    */
   public FunctionMove getMoveFunctionFrom(IDrawable d, ViewContext vc, int position) {
      return getMoveFunction(d, vc, false, position);
   }

   public FunctionMove getMoveFunctionTo(IDrawable d, int xd, int yd) {
      FunctionMove mf = new FunctionMove(gc.getBOC(), d.getX(), d.getY(), xd, yd);
      return mf;
   }

   /**
    * Same as {@link MoveFunctionC#getMoveFunctionFrom(IDrawable, int)} but starts at current
    * @param d
    * @param position
    * @return
    */
   public FunctionMove getMoveFunctionTo(IDrawable d, ViewContext vc, int position) {
      return getMoveFunction(d, vc, true, position);
   }

   /**
    * 
    * @param dd
    * @return
    */
   public SizeMod getSizeModEx(IDrawable dd) {
      ByteObject fctW = boc.getFunctionFactory().getFunctionValues(new int[] { -1, -3, -5, -7, -9, -7, -5, -3, -1 });
      Function wf = boc.getFunctionFactory().createFunction(fctW);
      ByteObject fctH = boc.getFunctionFactory().getFunctionValues(new int[] { 1, 2, 3, 4, 5, 4, 3, 2, 1 });
      Function hf = boc.getFunctionFactory().createFunction(fctH);
      Function fTick = boc.getFunctionFactory().createFunTick(9, ITechFunction.FUN_COUNTER_OP_0_ASC);

      SizeMod sm = new SizeMod(gc, dd, fTick, wf, hf);
      sm.setAnchor(C.ANC_4_CENTER_CENTER);
      return sm;
   }

   void setAnimationRepeat(ByteObject p, int repeat) {
      if (repeat == -1) {
         p.setFlag(ANIM_OFFSET_02_FLAG, ANIM_FLAG_3_LOOP, true);
      } else {
         p.setValue(ANIM_OFFSET_07_REPEAT1, repeat, 1);
      }
   }

   /**
    * Adds an {@link ByteObject} animation to the Figure.
    * <br>
    * <br>
    * @param fig
    * @param anim Animation must be implicit
    */
   public void setFigAnimation(ByteObject fig, ByteObject anim) {
      fig.checkType(IBOTypesDrawX.TYPE_DRWX_00_FIGURE);
      anim.checkType(IBOTypesGui.TYPE_GUI_11_ANIMATION);
      if (fig.hasFlag(IBOFigure.FIG__OFFSET_02_FLAG, IBOFigure.FIG_FLAG_6_ANIMATED)) {
         //check the animations in the array remove
      } else {
         fig.setFlag(IBOFigure.FIG__OFFSET_02_FLAG, IBOFigure.FIG_FLAG_6_ANIMATED, true);
      }
      fig.addByteObject(anim);
   }

}
