package pasa.cbentley.framework.gui.src4.table.interfaces;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;

public interface IBOTableSpan extends IByteObject {

   public static final int BASIC_SIZE_SPANNING     = A_OBJECT_BASIC_SIZE + 9;

   public static final int SPAN_OFFSET_1_FLAG       = A_OBJECT_BASIC_SIZE;

   public static final int SPAN_OFFSET_2_COL2       = A_OBJECT_BASIC_SIZE + 1;

   /**
    * <li>value of 0 means no spanning
    * <li>value of 1 means the rest
    * <li>value of 2 or more is the actual
    */
   public static final int SPAN_OFFSET_3_COL_VALUE2 = A_OBJECT_BASIC_SIZE + 3;

   public static final int SPAN_OFFSET_4_ROW2       = A_OBJECT_BASIC_SIZE + 5;

   public static final int SPAN_OFFSET_5_ROW_VALUE2 = A_OBJECT_BASIC_SIZE + 7;

}
