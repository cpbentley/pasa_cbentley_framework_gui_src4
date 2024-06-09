package pasa.cbentley.framework.gui.src4.factories.interfaces;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.framework.drawx.src4.string.interfaces.IBOStrAux;
import pasa.cbentley.framework.drawx.src4.string.interfaces.IBOStrAuxFx;
import pasa.cbentley.framework.drawx.src4.string.interfaces.IBOStrAuxFxApplicator;
import pasa.cbentley.framework.drawx.src4.string.interfaces.IBOStrAuxSpecialCharDirective;
import pasa.cbentley.framework.drawx.src4.string.interfaces.IBOStrAuxFormat;

/**
 * {@link IBOStrAuxGlobal} in the family of {@link IBOStrAux}.
 * 
 * <p>
 * 
 * </p>
 * <li> {@link IBOStrAuxEdit}
 * <li> {@link IBOStrAuxData}
 * <li> {@link IBOStrAuxGlobal}
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
public interface IBOStrAuxGlobal extends IBOStrAux {
   /**
    * 
    */
   public static final int GLOABAL_BASIC_SIZE                  = STR_AUX_SIZE + 2;


   /**
    * Number of visibles predictives
    */
   public static final int GLOBAL_OFFSET_01_ = STR_AUX_SIZE;

}
