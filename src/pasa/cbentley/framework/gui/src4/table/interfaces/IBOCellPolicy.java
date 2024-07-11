package pasa.cbentley.framework.gui.src4.table.interfaces;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.framework.datamodel.src4.table.ITableModel;
import pasa.cbentley.framework.drawx.src4.style.ITechStyleCache;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.layouter.src4.tech.IBOSizer;

/**
 * {@link IBOCellPolicy} defines the structure of a column to enable the efficient display of large tables on a small screen. 
 * 
 * <p>
 * Others
 * <li> A {@link ByteObject} of type {@link IBOTypesGui#TYPE_GUI_04_TABLE_POLICY} acts like a modeling power on {@link ITableModel}.
 * <li> {@link IBOGenetics}
 * <li> {@link IBOTablePolicy}
 * <li> {@link IBOTableView}
 * </p>
 * 
 * This class acts like. Indeed.
 * A small screen is not able to display more than 2-3 columns. 
 * Table design modifies the visible columns depending on available pixel space. <br>
 * <br>
 * <b>Frame management</b> <br>
 * <li>none. case of identic policy for all colums/rows 
 * <li>Mix
 * <li>Implicit
 * <li>None explicit. Ratio Ratio | Ratio Ratio 2 frames. We want in all cases 2 by 2 ratios.
 * When the ViewPort dimension is so big that the preferred size of one columns is 30% of the width
 * available? Frames management can try sizing the table with only
 * {@link ITechCell#CELL_0_IMPLICIT_SET} and if frames count is smaller, we have increased user
 * visibility.
 * In defining Cell policies, one can provide several and the Table will take the cleanest solution
 * A clean solution is when
 * <li> cells are not cut by viewport
 * <li> minimal amount of frames
 * <li> Let users move columns visually.. the model is unchanged. a mapping does the 
 * <br>
 * <br>
 * <b>Implicit</b> : <br>
 * <b>Example</b>: {@link ITechCell#CELL_4_FILL_AVERAGE} -  {@link ITechCell#CELL_1_EXPLICIT_SET} -  {@link ITechCell#CELL_4_FILL_AVERAGE} 
 * <br>
 * <br>
 * <ol>
 * <li>In start configuration, the user sees 1st (Fill Average) and and 2nd (Explicit Set) columns.
 * <li>When moving selection to 2nd column nothing happens.
 * <li>When moving selection to 3rd column, the users sees 2nd column and 3rd columns (Fill Average)
 * </ol>
 * <br>
 * When true {@link IBOCellPolicy#CELLP_FLAG_8_SLIDE_TO_IMPLICIT} and when ViewPort is big enough to support preferred columns sizes, policy may become 
 * On a big screen, when preferred column sizes are honored, or maximum size, we want policy to become IMPLICIT, SET, IMPLICIT
 * AVERAGE is actually a weak IMPLICIT sucking all unused space. Weak in the sense if there is very little room, it will take it.
 * <br>
 * <br>
 * <b>None Explicit</b> : <br>
 * <li>Ratio Ratio | Ratio Ratio. 2 column as the scrolling increment. Visible = 2 show either
 * <li>
 * <br>
 * @author Charles-Philip Bentley
 *
 */
public interface IBOCellPolicy extends IByteObject {
   /**
    * Size of table policy <br>
    * 1 byte for type [1] {@link IBOCellPolicy#CELLP_OFFSET_01_TYPE1} <br>
    * 1 byte for flag [2]<br> 
    * 1 byte for flagp [3]<br> 
    * 1 byte for flagx [4]<br>
    * 2 bytes for num [9]<br>
    * 1 bytes for frame [11]<br>
    * 1 bytes for policy [15]<br>
    * 1 byte for etalon [21] <br>
    * 4 bytes of size [19] <br>
    * 2 bytes for min size [20]<br>
    * 2 bytes for man size [20]<br>
    * 1 byte for frame
    * 
    */
   public static final int CELLP_BASIC_SIZE               = A_OBJECT_BASIC_SIZE + 18;

   /**
    * When set this CellSize policy is for cosize height.
    * <li>cosize width false
    * <li>cosize height true
    */
   public static final int CELLP_FLAG_1_HEIGHT_TRUE       = 1 << 0;

   /**
    * Set when use etalon for implicit cosize
    */
   public static final int CELLP_FLAG_2_ETALON            = 1 << 1;

   /**
    * Use maximum value for implicit cosize.
    */
   public static final int CELLP_FLAG_3_MAX               = 1 << 2;

   /**
    * Helper flag of the {@link IBOCellPolicy#TYPE_4RATIO} for say all columns share evenly the pixels
    * available.
    */
   public static final int CELLP_FLAG_4_RATIO_EVEN        = 1 << 3;

   /**
    * When set, {@link TableView} have its number of rows set in stone, while number of columns is infinite and depend on model size.
    * <br>
    * <br>
    * When not set, columns number is computed and fixed, while number of rows depends on model
    */
   public static final int CELLP_FLAG_5_STRONG_FLOW       = 1 << 4;

   /**
    * Policies/Strict defined gives a template to reproduce. <br>
    * "Repeated" Strict.
    */
   public static final int CELLP_FLAG_6_CYCLICAL          = 1 << 5;

   /**
    * Allows oversized for that cosize. That means a cell is not capped and maybe bigger than the ViewPort.
    */
   public static final int CELLP_FLAG_7_OVERSIZE          = 1 << 6;

   /**
    * TableView computes preferred sizes, if all fit into the screen
    * policy becomes {@link ITechCell#CELL_0_IMPLICIT_SET}.
    * <br>
    * Which policies are not overriden?
    * <li> {@link ITechCell#CELL_1_EXPLICIT_SET} will never change
    */
   public static final int CELLP_FLAG_8_SLIDE_TO_IMPLICIT = 1 << 7;

   /**
    * Data model fill in reverse in the cell
    */
   public static final int CELLP_FLAGP_1_REVERSE          = 1 << 0;

   /**
    * Defines explicit frames
    * Set when horizontal scrolling is structurally necessary. <br>
    * <b>Explicit Examples</b> <br>
    * <li>SET,AVERAGE,STRONG,STRONG. 3 Frames [0,2,3]
    * 
    * <b>Implicit Examples</b> <br>
    * <li>STRONG,STRONG
    * 
    */
   public static final int CELLP_FLAGP_2_FRAMES_EXPLICIT  = 1 << 1;

   /**
    * TODO
    * Frame management as Variable which generates variable column sizes. Fixed by Default.
    * <br>
    * Take policies 
    * <br>
    * AVERAGE,SET,AVERAGE
    * At first user sees AVERAGE and SET columns.
    * User scrolls to 3 rd column, what should he see?
    * Depending on Fixed Frames or Not.
    * Average alone as Strong or 2nd SET column and 3rd AVERAGE together?
    * <li>SET,AVERAGE,STRONG
    * <li>AVERAGE,SET, SET,AVERAGE is either Fixed AVERAGE,SET | SET,AVERAGE or Variable AVERAGE,SET | SET,SET | SET,AVERAGE
    * <br>
    * <br>
    * 
    */
   public static final int CELLP_FLAGP_3_FRAMES_VARIABLE  = 1 << 2;

   /**
    * Set when code detects cell size computation is implicit.
    * <br>
    * <b>Examples</b> <br>
    * <li> {@link ITechCell#CELL_0_IMPLICIT_SET}
    * <li> When {@link IBOCellPolicy#CELLP_OFFSET_09_SIZE4} is zero or negative
    */
   public static final int CELLP_FLAGP_4_IMPLICIT         = 8;

   /**
    * Set when at least one column has a variable size that will change
    * during a selection move.
    */
   public static final int CELLP_FLAGP_5_VARIABLE_SIZES   = 16;

   /**
    */
   public static final int CELLP_FLAGP_6_FIT              = 1 << 5;

   /**
    * Defines a sub litteral of String[] for cell titles
    */
   public static final int CELLP_FLAGP_7_TITLE_STRING_DEF = 1 << 6;

   /**
    * Defines a sub litteral of int[] for ided cell titles
    */
   public static final int CELLP_FLAGP_8_TITLE_INT_DEF    = 1 << 7;

   /**
    * By default, framework applies the value of {@link IBOCellPolicy#CELLP_OFFSET_07_POLICY1} to all columns.
    * 
    * Set this flag to true to use an array of values defining the different policies.
    * 
    */
   public static final int CELLP_FLAGZ_1_POLICIES         = 1 << 0;

   /**
    * Defines column sizes in Strict Definitions
    * Specific column sizes for each cell. 
    */
   public static final int CELLP_FLAGZ_2_SIZES            = 1 << 1;

   /**
    * Defines minimum cell cosizes
    */
   public static final int CELLP_FLAGZ_3_MIN_SIZES        = 1 << 2;

   /**
    * Defines maximum cell cosizes
    */
   public static final int CELLP_FLAGZ_4_MAX_SIZES        = 1 << 3;

   /**
    * Struct styles fetched with {@link StyleClass#getByteObject(int)}
    */
   public static final int CELLP_FLAGZ_5_FRAMES           = 1 << 4;

   /**
    * 
    */
   public static final int CELLP_FLAGZ_6                  = 1 << 5;

   /**
    * 
    */
   public static final int CELLP_FLAGZ_7                  = 1 << 6;

   /**
    * There is one {@link IBOSizer}. {@link IBOCellPolicy#CELLP_OFFSET_09_SIZE4} is ignored.
    * 
    */
   public static final int CELLP_FLAGZ_8_SIZER            = 1 << 7;

   /**
    * The Major Type of the Cell CoSize Policy.
    * <li> {@link ITechCell#TYPE_0_GENERIC}
    * <li> {@link ITechCell#TYPE_1_FLOW}
    * <li> {@link ITechCell#TYPE_2_RATIO}
    */
   public static final int CELLP_OFFSET_01_TYPE1          = A_OBJECT_BASIC_SIZE;

   /**
    * Miscellanous flags
    */
   public static final int CELLP_OFFSET_02_FLAG           = A_OBJECT_BASIC_SIZE + 1;

   /**
    * Perf flags
    */
   public static final int CELLP_OFFSET_03_FLAGP          = A_OBJECT_BASIC_SIZE + 2;

   /**
    * Extra flags
    */
   public static final int CELLP_OFFSET_04_FLAGZ          = A_OBJECT_BASIC_SIZE + 3;

   /**
    * Tells the number of cells in that cosize. (Strict and Ratio modes) 
    * <br>
    * <br>
    * Value of <b>0</b> means undefined, that means a weak cosize.
    * <br>
    * <br>
    * This value must match the array defined by flags
    * <li>{@link IBOCellPolicy#FLAGZ_1COL_POLICIES}
    * <li>{@link IBOCellPolicy#CELLP_FLAGZ_2_SIZES}
    * <li>{@link IBOCellPolicy#CELLP_FLAGZ_3_MIN_SIZES}
    * <li>{@link IBOCellPolicy#CELLP_FLAGZ_4_MAX_SIZES}
    * <br>
    * If mismatch, either ignore or use the last value for undefined columns. <br>
    * <br>
    */
   public static final int CELLP_OFFSET_05_CELL_NUM2      = A_OBJECT_BASIC_SIZE + 4;

   /**
    * Number of visible cells at a time in a frame. Value of 0 is undefined.
    * <br>
    * <br>
    * This field influence policies who number of columns depend on available size.
    * <li>{@link ITechCell#CELL_2_RATIO}
    * <li>and FILL policies only.
    * <li>Flow also allows, this value then tells about the number of times
    * <br>
    * <br>
    * <b>Examples 1</b>:
    * <li>RATIO,RATIO,SET,SET,SET (1) force the packing of all cells in a single frame.
    * <br>
    * <br>
    * <b>Examples 2</b>:
    * <li>RATIO,RATIO,RATIO,RATIO (2) give 3 frames
    * <li>AVERAGE,SET,SET (2) #1 AVERAGE,SET ; 2 SET,SET
    * <br>
    * 
    * <b>No effects on</b> <br>
    * <li>Strict policies like SET,SET,SET
    * <br>
    * For this, uses negative size definition in init method of TableView
    * That's because those policies are only based on cell data size.
    */
   public static final int CELLP_OFFSET_06_NUM_VISIBLE1   = A_OBJECT_BASIC_SIZE + 6;

   /**
    * Single policy value for all cells.
    * 
    * <p>
    * <li> {@link ITechCell#CELL_0_IMPLICIT_SET} -> Default to value
    * <li> {@link ITechCell#CELL_1_EXPLICIT_SET}
    * <li> {@link ITechCell#CELL_2_RATIO}
    * <li> {@link ITechCell#CELL_3_FILL_STRONG}
    * <li> {@link ITechCell#CELL_4_FILL_AVERAGE}
    * <li> {@link ITechCell#CELL_5_FILL_WEAK}
    * <li> {@link ITechCell#CELL_6_FRAME_SEPARATOR}
    * <li> {@link ITechCell#CELL_7_OTHER_COLUMN}
    * </p>
    * 
    */
   public static final int CELLP_OFFSET_07_POLICY1        = A_OBJECT_BASIC_SIZE + 7;

   /**
    * Decides which cell is used for computing all implicit cell's dimension.
    * <br>
    * <br>
    * Undefined when {@link IBOCellPolicy#CELLP_FLAG_2_ETALON} is not set.
    * <br>
    * <br>
    * <b>Examples</b> : <br>
    * <li> Implicit -1,-1 Shrink W or H will use Etalon cell to compute drawable size
    * <br>
    * <br>
    * 
    */
   public static final int CELLP_OFFSET_08_ETALON1        = A_OBJECT_BASIC_SIZE + 8;

   /**
    * {@link ISizer} coded size to be used for all cells
    *  <br>
    * 
    * Not used when {@link IBOCellPolicy#CELLP_FLAGZ_2_SIZES} is defined.
    * 
    */
   public static final int CELLP_OFFSET_09_SIZE4          = A_OBJECT_BASIC_SIZE + 9;

   /**
    * <li> {@link ITechStyleCache#RELATIVE_TYPE_0_MARGIN}
    * <li> {@link ITechStyleCache#RELATIVE_TYPE_1_BORDER}
    * <li> {@link ITechStyleCache#RELATIVE_TYPE_2_PADDING}
    * <li> {@link ITechStyleCache#RELATIVE_TYPE_3_CONTENT}
    */
   public static final int CELLP_OFFSET_10_RELATIVE1      = A_OBJECT_BASIC_SIZE + 9;

   /**
    * 
    */
   public static final int CELLP_OFFSET_10_SIZE_MIN2      = A_OBJECT_BASIC_SIZE + 13;

   /**
    * A maximum size is defined
    */
   public static final int CELLP_OFFSET_11_SIZE_MAX2      = A_OBJECT_BASIC_SIZE + 15;

   /**
    * 
    */
   public static final int CELLP_OFFSET_12_FRAMES1        = A_OBJECT_BASIC_SIZE + 17;

}
