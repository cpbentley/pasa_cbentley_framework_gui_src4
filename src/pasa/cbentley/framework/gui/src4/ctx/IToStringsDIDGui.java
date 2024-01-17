package pasa.cbentley.framework.gui.src4.ctx;

import pasa.cbentley.byteobjects.src4.ctx.IToStringsDIDsBoc;
import pasa.cbentley.core.src4.ctx.CtxManager;
import pasa.cbentley.core.src4.ctx.ICtx;

/**
 * Debug IDs
 * <br>
 * <br>
 * Allows the {@link BOModuleGui#toStringGetDIDString(int, int)} to get a name on the key/value pair
 * <br>
 * <br>
 * 
 * @author Charles-Philip
 *
 */
public interface IToStringsDIDGui extends IToStringsDIDsBoc {

   /**
    * Static range offset for {@link CtxManager#registerStaticID(ICtx, int)}
    */
   public static final int A_DID_OFFSET_A_GUI                   = 4000;

   /**
    * Static range length for {@link CtxManager#registerStaticID(ICtx, int)}
    */
   public static final int A_DID_OFFSET_Z_GUI                   = 4500;

   public static final int DID_01_VIEWPANE_MOVE_TYPE            = A_DID_OFFSET_A_GUI + 1;

   public static final int DID_02_VIEWPANE_PARTIAL_TYPE         = A_DID_OFFSET_A_GUI + 2;

   public static final int DID_03_VIEWPANE_SCROLL_TYPE          = A_DID_OFFSET_A_GUI + 3;

   public static final int DID_04_VIEWPANE_VISUAL_TYPE          = A_DID_OFFSET_A_GUI + 4;

   public static final int DID_05_STRING_DRAWABLE_TYPE          = A_DID_OFFSET_A_GUI + 5;

   public static final int DID_06_DRAWABLE_SPECIAL_IMPLICIT_W_H = A_DID_OFFSET_A_GUI + 6;

   public static final int DID_07_TABLE_POLICY                  = A_DID_OFFSET_A_GUI + 7;

   public static final int DID_08_ANIMATIONS                    = A_DID_OFFSET_A_GUI + 8;

   public static final int DID_09_CELL_POLICIES                 = A_DID_OFFSET_A_GUI + 9;

   public static final int DID_11_VIEWPANE_STYLE_TYPE           = A_DID_OFFSET_A_GUI + 11;

   public static final int DID_12_DRAWABLE_CACHE_TYPE           = A_DID_OFFSET_A_GUI + 12;

   public static final int DID_13_VIEWPANE_SCROLL_COMPET_TYPE   = A_DID_OFFSET_A_GUI + 13;

   public static final int DID_14_VIEWPANE_HEADER_COMPET_TYPE   = A_DID_OFFSET_A_GUI + 14;

   public static final int DID_15_TABLEVIEW_PSELECT_MODE        = A_DID_OFFSET_A_GUI + 15;

   public static final int DID_45_ANIM_MOVE_TYPE                = A_DID_OFFSET_A_GUI + 45;

   public static final int DID_46_ANIM_INCREMENT_TYPE           = A_DID_OFFSET_A_GUI + 46;

   public static final int DID_MUI_NUMBER                       = A_DID_OFFSET_A_GUI + 46;
}
