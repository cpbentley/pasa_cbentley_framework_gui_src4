package pasa.cbentley.framework.gui.src4.interfaces;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;

public interface IBODrawable extends IByteObject {

   public static final int DTECH_BASIC_SIZE                      = A_OBJECT_BASIC_SIZE + 10;

   public static final int DTECH_FLAGX_01_CACHE_STYLE            = 1 << 0;

   public static final int DTECH_OFFSET_01_FLAG                  = A_OBJECT_BASIC_SIZE + 1;

   public static final int DTECH_OFFSET_02_FLAGX                 = A_OBJECT_BASIC_SIZE + 1;

   public static final int DTECH_OFFSET_03_FLAGY                 = A_OBJECT_BASIC_SIZE + 1;

   public static final int DTECH_OFFSET_04_STATE_FLAG_A          = A_OBJECT_BASIC_SIZE + 1;

   public static final int DTECH_OFFSET_05_STATE_FLAG_B          = A_OBJECT_BASIC_SIZE + 1;

   public static final int DTECH_OFFSET_10_BEHAVIOR_FLAG_A       = A_OBJECT_BASIC_SIZE + 1;

   public static final int DTECH_OFFSET_11_BEHAVIOR_FLAG_B       = A_OBJECT_BASIC_SIZE + 1;
}
