package pasa.cbentley.framework.gui.src4.anim;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.interfaces.ITech;
import pasa.cbentley.framework.drawx.src4.style.ITechStyle;
import pasa.cbentley.framework.gui.src4.anim.move.FunctionMove;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.interfaces.IAnimable;

public interface ITechAnim extends ITech {

   /**
    * Animation overrides the complete draw process of the {@link Drawable}
    */
   public static final int ANIM_DRAW_0_OVERRIDE     = 0;

   /**
    * When animations works on the Drawable cache.
    */
   public static final int ANIM_DRAW_1_CACHE        = 1;

   /**
    * Animation modifies Drawable properties and Drawable automatically takes them into account in the next
    * painting cycle.
    */
   public static final int ANIM_DRAW_2_DRAWABLE     = 2;

   /**
    * Which part of the Drawable does the animation targets?
    */
   public static final int ANIM_TARGET_0            = 0;

   /**
    * The whole {@link Drawable} as a whole. Default target.
    */
   public static final int ANIM_TARGET_0_FULL       = 0;

   /**
    * {@link ITechStyle#STYLE_FLAGB_1_BG}
    */
   public static final int ANIM_TARGET_1_DLAYER1    = 1;

   /**
    * {@link ITechStyle#STYLE_FLAGB_2_BG}
    */
   public static final int ANIM_TARGET_2_DLAYER1    = 2;

   /**
    * {@link ITechStyle#STYLE_FLAGB_3_BG}
    */
   public static final int ANIM_TARGET_3_DLAYER1    = 3;

   /**
    * {@link ITechStyle#STYLE_FLAGB_4_BG}
    */
   public static final int ANIM_TARGET_4_DLAYER1    = 4;

   /**
    * {@link ITechStyle#STYLE_FLAGB_5_FG}
    */
   public static final int ANIM_TARGET_5_DLAYER1    = 5;

   /**
    * {@link ITechStyle#STYLE_FLAGB_6_FG}
    */
   public static final int ANIM_TARGET_6_DLAYER1    = 6;

   /**
    * {@link ITechStyle#STYLE_FLAGB_7_FG}
    */
   public static final int ANIM_TARGET_7_DLAYER1    = 7;

   /**
    * {@link ITechStyle#STYLE_FLAGB_8_FG}
    */
   public static final int ANIM_TARGET_8_DLAYER1    = 8;

   /**
    * Content only.
    */
   public static final int ANIM_TARGET_9_CONTENT    = 9;

   /**
    * Life Cycle
    * The animation to run while the Drawable is on screen
    */
   public static final int ANIM_TIME_0_MAIN         = 0;

   /**
    * When all Entry Type {@link IAnimable} are stopped.
    * Entry animations may be accelerated when to the final state using {@link IAnimable#race}
    * 
    */
   public static final int ANIM_TIME_1_ENTRY        = 1;

   /**
    * 
    */
   public static final int ANIM_TIME_2_EXIT         = 2;

   /**
    * Class identifier for {@link ByteObjectMod}.
    * <br>
    * <br>
    */
   public static final int ANIM_TYPE_00______       = 0;

   /**
    * Each animation frames changes the value of a target {@link ByteObject} using a pointer {@link ITypesCore#TYPE_010_POINTER}
    */
   public static final int ANIM_TYPE_01_VALUE       = 1;

   /**
    * Animation that takes in input another animation and apply it in reverse.
    * <br>
    * Animates in reverse only if that makes sense at the level of the animation genetics
    * <li> {@link ByteObject#ANIM_TIME_1_ENTRY} will look up an animation in {@link ByteObject#ANIM_TIME_2_EXIT}
    * <li> {@link ByteObject#ANIM_TIME_2_EXIT}
    * <br>
    * <br>
    * This allows one to define one animation for Drawable entry and simply set a reverse on exit for all animations.
    */
   public static final int ANIM_TYPE_02_REVERSE     = 2;

   /**
    * Move a drawable around the screen. Source and destination coordinates may be defined 
    * <li>explicitly
    * <li>anchored
    * <li>ratio
    * <br>
    * <br>
    * A {@link FunctionMove} defines the intermediary coordinates between the origin and the destination
    * <br>
    * 
    * 
    */
   public static final int ANIM_TYPE_03_MOVE        = 3;

   public static final int ANIM_TYPE_04_PIXELATE    = 4;

   public static final int ANIM_TYPE_05_LINE_SHIFT  = 5;

   /**
    * Each animation frame change the Alpha values of the Drawable
    */
   public static final int ANIM_TYPE_06_ALPHA       = 6;

   /**
    * Mix of Move and Alpha. Move Drawable leaving diminishing alpha trail
    */
   public static final int ANIM_TYPE_07_ALPHA_TRAIL = 7;

   /**
    * Special function values that requires a special initialization.
    * <br>
    * <br>
    * 
    */
   public static final int ANIM_TYPE_08_GRADIENT    = 8;

}
