package pasa.cbentley.framework.gui.src4.table.interfaces;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;

/**
 * 
 * @author Charles-Philip Bentley
 *
 */
public interface IBOGenetics extends IByteObject {

   public static final int GENE_BASIC_SIZE                    = A_OBJECT_BASIC_SIZE + 8;

   /**
    * Set to true when Data is fixed upon constructing model.
    * <br>
    * 
    */
   public static final int GENE_FLAG_2_STATIC                 = 2;

   public static final int GENE_FLAG_3_SAME_SIZE_W            = 4;

   /**
    * TODO May helps when init the -1 implicit. No need to try all Drawables.
    */
   public static final int GENE_FLAG_4_SAME_SIZE_H            = 8;

   public static final int GENE_FLAG_5_MAPPERINT_TYPE         = 16;

   /**
    * Set to True when at least one {@link IDrawable} has a flag {@link ITechDrawable#BEHAVIOR_01_NOT_SELECTABLE}.
    */
   public static final int GENE_FLAG_6_SELECTABILITY_DRAWABLE = 32;

   /**
    * Selectability defined inside the model
    */
   public static final int GENE_FLAG_7_SELECTABILITY_MODEL    = 64;

   /**
    * 
    */
   public static final int GENE_FLAG_8_TECH_PARAM_MODEL       = 128;

   public static final int GENE_FLAGX_1_TITLE_STRING_DEF      = 1;

   public static final int GENE_FLAGX_2_TITLE_INT_DEF         = 2;

   public static final int GENE_FLAGX_3_SPECIFIC_TECH_STRING  = 4;

   public static final int GENE_FLAGX_6_MODEL_CTYPES           = 1 << 5;

   public static final int GENE_FLAGX_7_MODEL_STYLE           = 64;

   public static final int GENE_FLAGX_8_DRAWABLES             = 128;

   public static final int GENE_OFFSET_01_FLAG                = A_OBJECT_BASIC_SIZE;

   public static final int GENE_OFFSET_02_FLAGX               = A_OBJECT_BASIC_SIZE + 1;

   /**
    * Value given when asked for implicit.
    * <br>
    * Only read when flag {@link IBOGenetics#GENE_FLAG_3_SAME_SIZE_W} is set.
    */
   public static final int GENE_OFFSET_03_WIDTH2              = A_OBJECT_BASIC_SIZE + 2;

   /**
    * <br>
    * Only read when flag {@link IBOGenetics#GENE_FLAG_4_SAME_SIZE_H} is set.
    */
   public static final int GENE_OFFSET_04_HEIGHT2             = A_OBJECT_BASIC_SIZE + 4;

   /**
    * Should table use a DrawableMapper or not?
    */
   public static final int GENE_OFFSET_05_INT_MAPPER_TYPE2    = A_OBJECT_BASIC_SIZE + 6;

}
