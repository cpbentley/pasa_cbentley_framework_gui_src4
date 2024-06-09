package pasa.cbentley.framework.gui.src4.factories.interfaces;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.framework.drawx.src4.string.interfaces.IBOStrAux;
import pasa.cbentley.framework.drawx.src4.string.interfaces.IBOStrAuxFx;
import pasa.cbentley.framework.drawx.src4.string.interfaces.IBOStrAuxFxApplicator;
import pasa.cbentley.framework.drawx.src4.string.interfaces.IBOStrAuxSpecialCharDirective;
import pasa.cbentley.framework.drawx.src4.string.interfaces.IBOStrAuxFormat;

/**
 * {@link IBOStrAuxPrediction} in the family of {@link IBOStrAux}.
 * 
 * <p>
 * 
 * </p>
 * <li> {@link IBOStrAuxEdit}
 * <li> {@link IBOStrAuxData}
 * <li> {@link IBOStrAuxPrediction}
 * 
 * <p>
 * And from 
 * <li> {@link IBOStrAuxFx}
 * <li> {@link IBOStrAuxFxApplicator}
 * <li> {@link IBOStrAuxSpecialCharDirective}
 * <li> {@link IBOStrAuxFormat}
 * 
 * </p>
 * 
 * @author Charles Bentley
 *
 */
public interface IBOStrAuxPrediction extends IBOStrAux {
   /**
    * 
    */
   public static final int PREDICTION_BASIC_SIZE                  = STR_AUX_SIZE + 12;

   /**
    * Ask to save unknown words.
    */
   public static final int PREDICTION_FLAG_1_SAVE_PERSONAL        = 1;

   /**
    * Number of visibles predictives
    */
   public static final int PREDICTION_OFFSET_01_PRED_NUM_VISIBLE1 = STR_AUX_SIZE;

   /**
    * Number of finds before a GUI update is called.
    */
   public static final int PREDICTION_OFFSET_02_PRED_REAL_TIME1   = STR_AUX_SIZE + 1;
}
