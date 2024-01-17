package pasa.cbentley.framework.gui.src4.factories.interfaces;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;

public interface IBOStringPrediction extends IByteObject {
   public static final int PREDICTION_BASIC_SIZE                  = A_OBJECT_BASIC_SIZE + 12;

   /**
    * Ask to save unknown words.
    */
   public static final int PREDICTION_FLAG_1_SAVE_PERSONAL        = 1;

   /**
    * Number of visibles predictives
    */
   public static final int PREDICTION_OFFSET_01_PRED_NUM_VISIBLE1 = A_OBJECT_BASIC_SIZE;

   /**
    * Number of finds before a GUI update is called.
    */
   public static final int PREDICTION_OFFSET_02_PRED_REAL_TIME1   = A_OBJECT_BASIC_SIZE + 1;
}
