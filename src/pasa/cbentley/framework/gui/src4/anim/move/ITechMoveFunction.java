package pasa.cbentley.framework.gui.src4.anim.move;

import pasa.cbentley.core.src4.interfaces.ITech;

public interface ITechMoveFunction extends ITech {

   int MOVE_FLAG_1_USE_CLIP   = 1;
   /**
    * In the case of Drawable, will it inherit the destination coordinate?
    * once the animation is finished or cut short
    */
   int MOVE_FLAG_2_EFFECTIVE  = 1 << 1;
   int MOVE_FLAG_3_DEBUG_TIME = 1 << 2;
   int TRAIL_0_UP             = 0;
   int TRAIL_1_DOWN           = 1;
   int TRAIL_2_LEFT           = 2;
   int TRAIL_3_RIGHT          = 3;
   /**
    * Use the parameter. Default is 0
    */
   int INCREMENT_0_PARAM              = 0;
   /**
    * Defines the increment change.
    * For Bresenham line, compute the biggest x,y distance.
    * Points on the bresenham values are taken
    * 1 + 1 + 2 
    */
   int INCREMENT_1_FIB                = 1;
   int INCREMENT_2_FIB_FAST           = 2;
   /**
    * [1,2,3,4,5,6,7,8,9]
    */
   int INCREMENT_3_LINEAR_INCREASE    = 3;
   int INCREMENT_MAX                  = 2;
   /**
    * Function for defining the Move line
    */
   int TYPE_MOVE_0_ASAP               = 0;
   /**
    * 
    */
   int TYPE_MOVE_1_BRESENHAM          = 1;
   int TYPE_MOVE_CK_MAX               = 1;

}
