package pasa.cbentley.framework.gui.src4.table.interfaces;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;

/**
 * 
 * {@link IBOTablePolicy} defines flags for to specify colspan and rowspan within a grid defined by 2 cell policies.
 * <p>
 * Others
 * <li> {@link IBOGenetics}
 * <li> {@link IBOTableView}
 * <li> {@link IBOCellPolicy}
 * </p>
 * <br>
 * <p>
 * colspan values are
 * <li>number of cells in the direction. [2->[
 * <li>1, spans the whole 
 * <li>0 undefined
 * </p>
 * <br>
 * <p>
 * Defined as (colAbs,rowAbs) = value
 * Spanning is always counted from Grid starting Top Left
 * <br>
 * in Grid
 * <pre>
 *   0 1 2 3
 * 0  
 * 1   
 * 2
 * 3
 * </pre>
 * </p>
 * 
 * <p>
 * 
 * (0,0) colspan 2, rowspan 3
 * Once a cell has been spanned, any spanning definition using that cell is ignored
 * </p>
 * 
 * 
 * @author Charles-Philip Bentley
 *
 */
public interface IBOTablePolicy extends IByteObject {

   public static final int BASIC_SIZE_TABLE_POLICY = A_OBJECT_BASIC_SIZE + 3;

   public static final int TP_FLAG_1COL_POLICY     = 1;

   public static final int TP_FLAG_2ROW_POLICY     = 2;

   public static final int TP_FLAG_3SPANNING       = 4;

   public static final int TP_OFFSET_1FLAG         = A_OBJECT_BASIC_SIZE;

   public static final int TP_OFFSET_2FLAGX        = A_OBJECT_BASIC_SIZE + 1;

   /**
    * Both col and row numbers are set by policy.
    * <br>
    * <br>
    * Model size does not have an influence.
    */
   public static final int TABLE_TYPE_0STRICT      = 0;

   /**
    * Master Col.
    * <li>Policy decides number of columns.
    * <li>Model size decides number of row.
    */
   public static final int TABLE_TYPE_1GENERIC_COL = 1;

   /**
    * Master Row.
    * <li>Policy decides number of rows.
    * <li>Model size decides number of columns.
    */
   public static final int TABLE_TYPE_2GENERIC_ROW = 2;

   /**
    * Master Column.
    * <br>
    * <br>
    * 
    * <li>Content width decides number of columns, which is computed first
    * <li>number of rows decided by policy or model.
    */
   public static final int TABLE_TYPE_3FLOW_COL    = 3;

   /**
    * Master Row. 
    * <br>
    * <br>
    * <li>Content height decides number of rows.
    * <li>number of columns decided by policy or model.
    */
   public static final int TABLE_TYPE_4FLOW_ROW    = 4;

}
