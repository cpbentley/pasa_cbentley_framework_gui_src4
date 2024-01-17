package pasa.cbentley.framework.gui.src4.table.interfaces;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.framework.datamodel.src4.table.ITableModel;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.table.TableView;

/**
 * Defines the structure to enable the efficient display of large tables on a small screen. 
 * <br>
 * <br>
 * A {@link ByteObject} of type {@link IBOTypesGui#TYPE_120_TABLE_POLICY} acts like a modeling power on {@link ITableModel}.
 * <br>
 * <br>
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
 * {@link IBOCellPolicy#CELL_0_IMPLICIT_SET} and if frames count is smaller, we have increased user
 * visibility.
 * In defining Cell policies, one can provide several and the Table will take the cleanest solution
 * A clean solution is when
 * <li> cells are not cut by viewport
 * <li> minimal amount of frames
 * <li> Let users move columns visually.. the model is unchanged. a mapping does the 
 * <br>
 * <br>
 * <b>Implicit</b> : <br>
 * <b>Example</b>: {@link IBOCellPolicy#CELL_4_FILL_AVERAGE} -  {@link IBOCellPolicy#CELL_1_EXPLICIT_SET} -  {@link IBOCellPolicy#CELL_4_FILL_AVERAGE} 
 * <br>
 * <br>
 * <ol>
 * <li>In start configuration, the user sees 1st (Fill Average) and and 2nd (Explicit Set) columns.
 * <li>When moving selection to 2nd column nothing happens.
 * <li>When moving selection to 3rd column, the users sees 2nd column and 3rd columns (Fill Average)
 * </ol>
 * <br>
 * When true {@link IBOCellPolicy#FLAG_8_SLIDE_TO_IMPLICIT} and when ViewPort is big enough to support preferred columns sizes, policy may become 
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
    * Maximum Preferred size of the Drawable cells on the Column/Row.
    * <br>
    * The sizers for both width and height will be
    * <li> {@link ISizer#ET_TYPE_0_PREFERRED_SIZE}
    * <li> {@link ISizer#MODE_2_RATIO} with 100%
    * <li> Max value of TableViewPort
    * 
    * <br>
    * TODO This value can be set a maximum.
    * <br>
    * 
    * The drawable width or height value in method {@link IDrawable#init(int, int)} will be <b>0</b>.
    * <br>
    * <br>
    * 
    * For columns : <br>
    * Width of cells in the column is the width of the widest row-cell
    * <br>
    * Equivalent to 0 column size.
    * Override any size meaning
    * <br>
    * <br>
    * For rows: <br>
    * Height of cells in the row is the height of the tallest cell in that row <br>
    * <br>
    * Equivalent to 0 row size.
    * <br> <br>
    * When an implicit computation gives zero, falls back to size given in array, then falls back to single size.
    */
   public static final int CELL_0_IMPLICIT_SET      = 0;

   /**
    * Needs an explicit pixel value for column's widht or row's height.
    * <br>
    * 
    * <br>
    * This value is given by either 
    * <li>{@link IBOCellPolicy#OFFSET_09WIDTH4}
    * <li>{@link IBOCellPolicy#FLAGZ_2COL_SIZES} 
    * <li>{@link ISizer}
    * <br>
    * That means if value is {@link IBOCellPolicy#OFFSET_09WIDTH4}, this policy will have the meaning of {@link IBOCellPolicy#CELL_0_IMPLICIT_SET} meaning.
    * 
    */
   public static final int CELL_1_EXPLICIT_SET      = 1;

   /**
    * All columns with this policy will divide the space left between themselves within the current frame. <br>
    * according to 
    * {@link IBOCellPolicy#FLAGZ_2COL_SIZES}. If none is defined, leftover pixel room is divided evenly among
    * the column who share that Ratio policy. If no leftover space, a new window ViewPort area is given.
    * <br>
    * <br>
    * Not compatible with:
    * <li>{@link IBOCellPolicy#CELL_4_FILL_AVERAGE}
    * <li>{@link IBOCellPolicy#CELL_5_FILL_WEAK} <br>
    * <br>
    * By construction at least two ratio cells must be 
    * <br>
    * [{@link IBOCellPolicy#CELL_3_FILL_STRONG} - {@link IBOCellPolicy#CELL_2_RATIO} - {@link IBOCellPolicy#CELL_2_RATIO}] is possible.
    * The ratio rule applies to the ViewPort.
    * <br>
    * <br>
    * <b>Examples </b>: <br>
    * <li>RATIO,SET,RATIO : SET takes, other pixels are distributed using ratios. 1 Frame.
    * <li>RATIO,SET is identical to AVERAGE,SET
    * <li>SET,RATIO,SET is identical to SET,WEAK,SET.
    * <li>SET,RATIO,RATIO,SET is to SET,LeftOverSpaceRatioed,SET. if num of visible columns is not explicit.
    * <li>SET,RATIO|RATIO,SET (2) visible columns at a time. So we have SET,RATIO - RATIO,RATIO - RATIO,SET 
    * Variable size with Logical Scrolling.
    * <li>STRONG|RATIO becomes STRONG|STRONG
    * <li>RATIO,RATIO,RATIO shares all visible space. No scrolling
    * <li>RATIO,RATIO|RATIO,RATIO (2). 2 visible columns at a time. Space is shared. May give a variable size scenario.
    * <br>
    * Conversions happens inside the {@link TableView}.
    * When possible, it converts to {@link IBOCellPolicy#CELL_1_EXPLICIT_SET} otherwise set variable flag.
    */
   public static final int CELL_2_RATIO             = 2;

   /**
    * Accepts 0 neighbouring cell within a frame <b>FILL0</b>. 
    * <br>
    * <br>
    * Even Ratio with 1 visible column/row.
    * <br>
    * Each cell on that axis takes every ViewPort pixels. <br>
    * <br>
    * <br>
    * <b>Examples yielding 1 frame</b>  <br>
    * <li>STRONG = 1 column
    * <br>
    * <br>
    * <b>Examples yielding 2 frames</b>  <br>
    * <li>STRONG | STRONG
    * <li>RATIO,RATIO | STRONG
    * <li>SET | STRONG
    * <li>SET,AVERAGE | STRONG
    * <li>SET,WEAK,SET | STRONG
    * <br>
    */
   public static final int CELL_3_FILL_STRONG       = 3;

   /**
    * <b>FILL1</b> : Accepts 1 neighbouring cell within a frame <br>
    * <br>
    * <b>Examples yielding 1 frame</b>  <br>
    * <li>AVERAGE alone acts like STRONG
    * <li>SET,AVERAGE
    * <br>
    * <br>
    * <b>Examples yielding 2 frames</b>  <br>
    * <li>AVERAGE,SET | SET
    * <li>AVERAGE,SET,AVERAGE = AVERAGE,SET | AVERAGE or AVERAGE,SET | SET,AVERAGE
    * 
    * Frame enabling
    * 
    */
   public static final int CELL_4_FILL_AVERAGE      = 4;

   /**
    * <b>FILL2</b> : Accepts 2 neighbouring cells within a frame <br>
    * <br>
    * Takes pixel room left once neighbouring {@link IBOCellPolicy#CELL_1_EXPLICIT_SET} or {@link IBOCellPolicy#CELL_0_IMPLICIT_SET}
    * column sizes have been computed. 
    * <br>
    * <br>
    * <b>Examples</b>: <br>
    * <li>SET,WEAK,SET = 1 frame.
    * <li>SET,WEAK,STRONG -> as SET,AVERAGE,STRONG.
    * <li>RATIO,WEAK,RATIO is fine.
    * <li>WEAK,SET -> Upgraded to {@link IBOCellPolicy#CELL_4_FILL_AVERAGE}
    * <li>WEAK -> Upgraded to {@link IBOCellPolicy#CELL_3_FILL_STRONG}
    * <li>WEAK,WEAK acts as STRONG,STRONG
    */
   public static final int CELL_5_FILL_WEAK         = 5;

   /**
    * Explicitely
    */
   public static final int CELL_6_FRAME_SEPARATOR   = 6;

   /**
    * Takes the size of another column
    * <br>
    * What column get the extras?
    */
   public static final int CELL_7_OTHER_COLUMN      = 7;

   /**
    * Size of table policy <br>
    * 1 byte for type [1] {@link IBOCellPolicy#OFFSET_01_TYPE1} <br>
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
   public static final int CELLP_BASIC_SIZE         = A_OBJECT_BASIC_SIZE + 18;

   /**
    * When set this CellSize policy is for cosize height.
    * <li>cosize width false
    * <li>cosize height true
    */
   public static final int FLAG_1_HEIGHT_TRUE       = 1 << 0;

   /**
    * Set when use etalon for implicit cosize
    */
   public static final int FLAG_2_ETALON            = 1 << 1;

   /**
    * Use maximum value for implicit cosize.
    */
   public static final int FLAG_3_MAX               = 1 << 2;

   /**
    * Helper flag of the {@link IBOCellPolicy#TYPE_4RATIO} for say all columns share evenly the pixels
    * available.
    */
   public static final int FLAG_4_RATIO_EVEN        = 1 << 3;

   /**
    * When set, {@link TableView} have its number of rows set in stone, while number of columns is infinite and depend on model size.
    * <br>
    * <br>
    * When not set, columns number is computed and fixed, while number of rows depends on model
    */
   public static final int FLAG_5_STRONG_FLOW       = 1 << 4;

   /**
    * Policies/Strict defined gives a template to reproduce. <br>
    * "Repeated" Strict.
    */
   public static final int FLAG_6_CYCLICAL          = 1 << 5;

   /**
    * Allows oversized for that cosize. That means a cell is not capped and maybe bigger than the ViewPort.
    */
   public static final int FLAG_7_OVERSIZE          = 1 << 6;

   /**
    * TableView computes preferred sizes, if all fit into the screen
    * policy becomes {@link IBOCellPolicy#CELL_0_IMPLICIT_SET}.
    * <br>
    * Which policies are not overriden?
    * <li> {@link IBOCellPolicy#CELL_1_EXPLICIT_SET} will never change
    */
   public static final int FLAG_8_SLIDE_TO_IMPLICIT = 1 << 7;

   /**
    * Data model fill in reverse in the cell
    */
   public static final int FLAGP_1_REVERSE          = 1 << 0;

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
   public static final int FLAGP_2_FRAMES_EXPLICIT  = 1 << 1;

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
   public static final int FLAGP_3_FRAMES_VARIABLE  = 1 << 2;

   /**
    * Set when code detects cell size computation is implicit.
    * <br>
    * <b>Examples</b> <br>
    * <li> {@link IBOCellPolicy#CELL_0_IMPLICIT_SET}
    * <li> When {@link IBOCellPolicy#OFFSET_09_SIZE4} is zero or negative
    */
   public static final int FLAGP_4_IMPLICIT         = 8;

   /**
    * Set when at least one column has a variable size that will change
    * during a selection move.
    */
   public static final int FLAGP_5_VARIABLE_SIZES   = 16;

   /**
    */
   public static final int FLAGP_6_FIT              = 1 << 5;

   /**
    * Defines a sub litteral of String[] for cell titles
    */
   public static final int FLAGP_7_TITLE_STRING_DEF = 1 << 6;

   /**
    * Defines a sub litteral of int[] for ided cell titles
    */
   public static final int FLAGP_8_TITLE_INT_DEF    = 1 << 7;

   /**
    * Defined when columns have different policies. <br>
    * When this field is not defined, framework uses value of {@link IBOCellPolicy#OFFSET_13COL_POLICY1} which will apply
    * to all columns.<br>
    * 
    */
   public static final int FLAGZ_1_POLICIES         = 1 << 0;

   /**
    * Defines column sizes in Strict Definitions
    * Specific cosizes for each cell. 
    */
   public static final int FLAGZ_2_SIZES            = 1 << 1;

   /**
    * Defines minimum cell cosizes
    */
   public static final int FLAGZ_3_MIN_SIZES        = 1 << 2;

   /**
    * Defines maximum cell cosizes
    */
   public static final int FLAGZ_4_MAX_SIZES        = 1 << 3;

   /**
    * Struct styles fetched with {@link StyleClass#getByteObject(int)}
    */
   public static final int FLAGZ_5_FRAMES           = 1 << 4;

   /**
    * Defines array of structural styles
    */
   public static final int FLAGZ_8                  = 1 << 7;

   public static final int INTRA_REF_SELECTABILITY  = 5;

   /**
    * The Major Type of the Cell CoSize Policy.
    * <br>
    * <br>
    * <li>Ratio, Generic
    * <li>Flow
    */
   public static final int OFFSET_01_TYPE1          = A_OBJECT_BASIC_SIZE;

   /**
    * Miscellanous flags
    */
   public static final int OFFSET_02_FLAG           = A_OBJECT_BASIC_SIZE + 1;

   /**
    * Perf flags
    */
   public static final int OFFSET_03_FLAGP          = A_OBJECT_BASIC_SIZE + 2;

   /**
    * Extra flags
    */
   public static final int OFFSET_04_FLAGZ          = A_OBJECT_BASIC_SIZE + 3;

   /**
    * Tells the number of cells in that cosize. (Strict and Ratio modes) 
    * <br>
    * <br>
    * Value of <b>0</b> means undefined, that means a weak cosize.
    * <br>
    * <br>
    * This value must match the array defined by flags
    * <li>{@link IBOCellPolicy#FLAGZ_1COL_POLICIES}
    * <li>{@link IBOCellPolicy#FLAGZ_2_SIZES}
    * <li>{@link IBOCellPolicy#FLAGZ_3_MIN_SIZES}
    * <li>{@link IBOCellPolicy#FLAGZ_4_MAX_SIZES}
    * <br>
    * If mismatch, either ignore or use the last value for undefined columns. <br>
    * <br>
    */
   public static final int OFFSET_05_CELL_NUM2      = A_OBJECT_BASIC_SIZE + 4;

   /**
    * Number of visible cells at a time in a frame. Value of 0 is undefined.
    * <br>
    * <br>
    * This field influence policies who number of columns depend on available size.
    * <li>{@link IBOCellPolicy#CELL_2_RATIO}
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
   public static final int OFFSET_06_NUM_VISIBLE1   = A_OBJECT_BASIC_SIZE + 6;

   /**
    * Single policy value for all cells.
    * <br>
    * 
    * <br>Default to value <b>0</b> which is {@link IBOCellPolicy#CELL_0_IMPLICIT_SET}
    * <br>
    * <br>
    * For example when {@link IBOCellPolicy#OFFSET_13COL_POLICY1} is set to {@link IBOCellPolicy#CELL_1_EXPLICIT_SET},
    *  the framework will use either {@link IBOCellPolicy#OFFSET_09WIDTH4} or {@link IBOCellPolicy#FLAGZ_2COL_SIZES} when defined.
    */
   public static final int OFFSET_07_POLICY1        = A_OBJECT_BASIC_SIZE + 7;

   /**
    * Decides which cell is used for computing all implicit cell's dimension.
    * <br>
    * <br>
    * Undefined when {@link IBOCellPolicy#FLAG_2_ETALON} is not set.
    * <br>
    * <br>
    * <b>Examples</b> : <br>
    * <li> Implicit -1,-1 Shrink W or H will use Etalon cell to compute drawable size
    * <br>
    * <br>
    * 
    */
   public static final int OFFSET_08_ETALON1        = A_OBJECT_BASIC_SIZE + 8;

   /**
    * {@link ISizer} coded size to be used for all cells
    *  <br>
    * 
    * Not used when {@link IBOCellPolicy#FLAGZ_2COL_SIZES FLAGZ_2COL_SIZES} is defined.
    * 
    */
   public static final int OFFSET_09_SIZE4          = A_OBJECT_BASIC_SIZE + 9;

   /**
    * 
    */
   public static final int OFFSET_10_SIZE_MIN2      = A_OBJECT_BASIC_SIZE + 13;

   /**
    * A maximum size is defined
    */
   public static final int OFFSET_11_SIZE_MAX2      = A_OBJECT_BASIC_SIZE + 15;

   /**
    * 
    */
   public static final int OFFSET_12_FRAMES1        = A_OBJECT_BASIC_SIZE + 17;

   /**
    * Mix of all policies.
    * <br>
    * Cell number is optional<br>
    * Frame number is optional<br>
    */
   public static final int TYPE_0_GENERIC           = 0;

   /**
    * Number of cells is unknown and depends on available content pixel size.
    * <br>
    * <br>
    * Etalon or init cells with given {@link IBOCellPolicy#OFFSET_09_SIZE4} and opposite cosize values.
    * <br>
    * <br>
    */
   public static final int TYPE_1_FLOW              = 1;

   /**
    * All cells have the ratio policy {@link IBOCellPolicy#CELL_2_RATIO}. 
    * <br>
    * <br>
    * Cell number is optional
    * <br>
    * Visible Cells number is optional
    * <br>
    * <br>
    */
   public static final int TYPE_2_RATIO             = 2;

}
