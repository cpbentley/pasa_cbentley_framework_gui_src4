package pasa.cbentley.framework.gui.src4.table;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesDrw;
import pasa.cbentley.byteobjects.src4.objects.litteral.LitteralStringOperator;
import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.core.src4.event.IEventConsumer;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.stator.StatorReader;
import pasa.cbentley.core.src4.stator.StatorWriter;
import pasa.cbentley.core.src4.utils.BitUtils;
import pasa.cbentley.core.src4.utils.IntUtils;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdListener;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdsCmd;
import pasa.cbentley.framework.cmd.src4.interfaces.ICommandable;
import pasa.cbentley.framework.cmd.src4.interfaces.INavTech;
import pasa.cbentley.framework.coreui.src4.ctx.ToStringStaticCoreUi;
import pasa.cbentley.framework.coreui.src4.exec.ExecutionContext;
import pasa.cbentley.framework.coreui.src4.tech.IBCodes;
import pasa.cbentley.framework.coreui.src4.utils.ViewState;
import pasa.cbentley.framework.datamodel.src4.table.ITableModel;
import pasa.cbentley.framework.datamodel.src4.table.ModelAux;
import pasa.cbentley.framework.datamodel.src4.table.ObjectTableModel;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.factories.FigureOperator;
import pasa.cbentley.framework.gui.src4.canvas.ExecutionCtxDraw;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceDrawable;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.DrawableInjected;
import pasa.cbentley.framework.gui.src4.core.LayEngineDrawable;
import pasa.cbentley.framework.gui.src4.core.ScrollBar;
import pasa.cbentley.framework.gui.src4.core.ScrollConfig;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.core.ViewDrawable;
import pasa.cbentley.framework.gui.src4.core.ViewPane;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.ctx.IFlagsToStringGui;
import pasa.cbentley.framework.gui.src4.ctx.ToStringStaticGui;
import pasa.cbentley.framework.gui.src4.factories.TablePolicyFactory;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOViewPane;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawListener;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawableListener;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechViewDrawable;
import pasa.cbentley.framework.gui.src4.menu.MenuBar;
import pasa.cbentley.framework.gui.src4.mui.MLogViewer;
import pasa.cbentley.framework.gui.src4.string.InputRequestStr;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOCellPolicy;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOGenetics;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOTablePolicy;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOTableView;
import pasa.cbentley.framework.gui.src4.tech.ITechStringDrawable;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;
import pasa.cbentley.framework.gui.src4.utils.DrawableMapper;
import pasa.cbentley.framework.gui.src4.utils.DrawableUtilz;
import pasa.cbentley.framework.input.src4.InputState;
import pasa.cbentley.framework.input.src4.gesture.GestureDetector;

/**
 * Structure for displaying drawables in a table grid fashion. 
 * <br>
 * <br>
 * Services:
 * <li>sizing
 * <li>positioning
 * <li>styling
 * <li>parenting
 * <br>
 * <br>
 * Data elements are provided by a {@link ITableModel}, whose purpose is to wrap data {@link ITableModel} elements in {@link IDrawable}s.
 * Parenting using method {@link IDrawable#setParent(IDrawable)} is done 
 * <br>
 * <br>
 * A policy (a {@link ByteObject}) define the structure of the table, which structures the data elements of the model.
 * Missing information is completed by the {@link IDrwTypes#TYPE_TABLE_POLICY} DrwParam. Because of the business side of things, the model simply defines
 * the number of columns and their titles. 
 * <br>
 * <br>
 * <br>
 * <b>Invisible Rows or Columns</b> :<br>
 * <li>May be set dynamically by user. Command Hide Row, Hide Column => #x +
 * Column Title Impact of Invisible on table Policy? <br>
 * <b>Moving a Column or a Row</b>? 
 * <br>
 * If the {@link ITableModel} supports it. {@link ObjectTableModel} does. Acts
 * on Model or is it just cosmetic? 
 * <br>
 * <br>
 * <b>Cell Edition</b> <br>
 * Decided at String Model?<br>
 * When Edition is made, event is made
 * {@link IBOTableView#EVENT_ID_03_SELECTION_EDITION_START}. <br>
 * <br>
 * <b> How to use it ?</b> 2 relationships are possible for the User <br>
 * <li><b>isA</b> TableView. A sub class is created to override TableView's
 * methods. When client code has only a Table structure to draw.
 * <li><b>usesA</b> TableView. An instance of TableView is created and stored in
 * a class field. If user is a Drawable, it will be forward InputConfig. <br>
 * <br>
 * <b>Event Handling</b> <br>
 * {@link TableView} is often used in the same purpose as {@link InputRequestStr}.
 * <br>
 * <li>a {@link MCmd} requires input. Model create a {@link TableView} with its data model to choose data from.
 * <li>Registers as an {@link ICmdListener} to the {@link ICmdsCmd#CMD_ID_SELECT} {@link MCmd}
 * <li>Table is show, User selects, User fire the Selcet Command of the Table.
 * <li>Select cmd has a listener, so fire it. Original caller gets the
 * TableRefence, DataModel, and eventually Data. <br>
 * Default Event Hook ups for TableView users? Navigation within the table is
 * automatic Selection <br>
 * <br>
 * The <b>Table</b> is initalized with the {@link Drawable#init(int, int)
 * Drawable#init} method.
 * <li><b>Positive</b>. Forces the use of scrollbars.
 * <li><b>0</b> values are ignored at this level. {@link ViewDrawable} deals
 * with it.
 * <li><b>Negative</b>. Drawn dimensions are the area of a number of cells.
 * -1,-1 values would mean 1 visible column and 1 visible row as drawn width and
 * height Value is set as in {@link ScrollConfig} visible increment <br>
 * The {@link IDrwTypes#TYPE_TABLE_POLICY} stored in
 * {@link TableView#policyTable} which defines the structure does not define
 * visible cells. <br>
 * <br>
 * <b>Shrinking Behavior and Visual Left Over</b>: 
 * <br>
 * {@link IBOViewPane#VP_OFFSET_06_VISUAL_LEFT_OVER1} parameters. <br>
 * {@link TableView} is not scrolled. <br>
 * <br>
 * <b>Constraints on Table cells</b>.<br>
 * Constraining table policies force cells into a pixel area. Other table
 * policies ask cells to layout with method {@link Drawable#init(int, int)} 0,0
 * and take the drawn dimension into account. <br>
 * Some drawables like {@link StringDrawable} use pre-defined constraint types (
 * {@link ITechStringDrawable#TYPE_2_SCROLL_H}). Those types are defined at the
 * Wrapper/Model level. The TableView is unaware of those. Pre-define types
 * usually override the init() method semantics. <br>
 * <br>
 * Here is a list from the most constrained case to the least.
 * <ol>
 * <li>explicit - viewport capped : most practical cases
 * <li>explicit - no cap
 * <li>logical implicit - etalon - capped - 1 pre-init needed
 * <li>logical implicit - all - pre-init needed
 * <li>implicit - etalon viewport capped = 1 prenint needed
 * <li>implicit all viewport capped - all pre-init
 * <li>free implicit - all pre-init. the cell takes whatever it wants. In this
 * mode, TableView acts like a ViewPane.
 * </ol>
 * An etalon is defined at the row index for columns with
 * {@link IBOCellPolicy#OFFSET_08_ETALON1} <br>
 * <br>
 * Setup sizes ({@link TableView#setupColSizes}) are computed each time a
 * reshape is called. <br>
 * 
 * TODO Should the Table disable ViewPane on cells? It may indeed do so when it
 * provides a way to further display. It cannot set false to scrolling
 * behavior. It must use its own special flag.
 * The {@link IBOTypesGui#LINK_66_TECH_VIEWPANE} and {@link IBOTypesGui#LINK_65_STYLE_VIEWPANE} must be
 * 
 * The ViewPort cap is decided by flag {@link IBOCellPolicy#FLAG_7_OVERSIZE}.
 * When this flag is set and at least one cell is bigger than viewport,
 * {@link TableView#capOverSized} boolean field is set true. <br>
 * Shrink Flags of Drawable inside the cell. <br>
 * Shrink behavior should be disabled <br>
 * The cells are given their xywh area just before being drawn. 
 * Thus the
 * ModelWrapper method {@link DrawableMapper#getDrawable(Object, int)} may return a
 * Drawable not initialized with its area. Which means
 * {@link ITechDrawable#STATE_05_LAYOUTED} is not set <br>
 * <br>
 * <b>Structural Implicit</b> :<br>
 * Happens with {@link IBOCellPolicy#TYPE_0_GENERIC} policy when columns have
 * policy {@link IBOCellPolicy#CELL_5_FILL_WEAK}, {@link IBOCellPolicy#CELL_4_FILL_AVERAGE} {@link IBOCellPolicy#CELL_3_FILL_STRONG}
 * Strucutral Implicit policy implies pixel value of column is computed last. 
 * It may also changes when selection changes and a non visible cell becomes visible. 
 * <br>
 * <br>
 * The Table controls the {@link ScrollConfig} using CurrentConfig. It decides
 * the nature of increments. By default Increment unit is a cell. When a Cell
 * has a FREE dimension constraint, it may be drawn on an area bigger than the
 * Table's ViewPort. <br>
 * In those cases, Cell Increments are not practical. <br>
 * <br>
 * The Table is {@link IDrawable}. Cells are {@link IDrawable}s and those
 * drawables may be themselves {@link IDrawable}. When a Cell is selected, that cell become the active {@link IDrawable} of the  {@link Controller}
 * when it recieve the focus.
 * Its releases the NavFocus {@link ITechDrawable#EVENT_03_KEY_FOCUS_GAIN EVENT_KEY_FOCUS_GAIN} when focusing out using navigation keys or when an command like fire <br>
 * <br>
 * <br>
 * Sometimes the column is a pure visual artifact with no foundation in the
 * model. That is why the generic interface method
 * {@link DrawableMapper#getDrawable(Object, int)} uses an one dimension index.
 * Model may not have any column semantics. 
 * <br>
 * <br>
 * <b>Content Updates</b> : <br>
 * When a Drawable's content is updated, TableView first check if cell is
 * <li>implicit a reshape of the table is done if there is indeed a change
 * <li>visible a repaint of the cell is done When implicit, the drawable is
 * re-initialized and if a change in setupsize is deemed necessary, a full
 * reshape is done. TableView registers itself with
 * {@link DrawableMapper} to get updates through
 * {@link TableView#consumeModelUpdateEvent(Object)}.
 * 
 * <br>
 * <br>
 * <b> Optimizations:</b> <br>
 * <li>Optimized drawing and deselected/selected cells. If both drawables are
 * opaque, just repaint them otherwise if single color, paint area or use cached
 * table background
 * <br>
 * <br>
 * <b>Column/Row Titles</b>. <br>
 * Titles are located in {@link ViewPane} headers and are always visible. <br>
 * Usually String, but a TableView with the same row Policy/ colPolicy is. The
 * model for title data. <br>
 * <br>
 * Uses the {@link IDrawListener} trick. Put in the header of the ViewPane
 * Scrolling with the horizontal scrollbar if there is one. They are injected
 * drawn.
 * <br>
 * <br>
 * <b>Focus Traversal :</b> <br>
 * Is focus different from Selection? Yes. They are separate. Table primarly manage focus between cells. Selection happens to always follow focus.
 * <br>
 * <br>
 * <b>Navigation Managament</b> <br>
 * When selection may change inside the {@link TableView} {@link IBOTableView#T_FLAGX_1_NO_SELECTION}.
 * The helper flags are set when effective navigation is possible 
 * <li> {@link IBOTableView#HELPER_FLAG_10_OWN_NAV_W} horizontal
 * <li> {@link IBOTableView#HELPER_FLAG_11_OWN_NAV_H} vertical
 * <br>
 * Then the {@link ViewPane} cannot navigate the TableView by itself with up/down/left/right key events. 
 * <br>
 * The {@link ViewPane} only manage scrolling pointer events. 
 * <br>
 * See tech flags {@link IBOTableView#T_FLAGY_3_POINTER_SCROLLS_SELECTION}. 
 * <br>
 * <br>
 * <b>Selectability</b> :
 * <br>
 * By default, no selection is possible.
 * <br>
 * When flag {@link IBOTableView#T_FLAGX_1_NO_SELECTION} is set, cells recieve the {@link ITechDrawable#STYLE_05_SELECTED} state.
 * <br>
 * {@link IBOTableView#T_FLAGM_2_MULTIPLE_SELECTION}, only pointer events makes it possible to do that, or the use of
 * CTRL key or MAJ key for key events.
 * <br>
 * <b>Relation with {@link Choice} and {@link List}</b> : <br>
 * <li> {@link IBOTableView#T_FLAGM_2_MULTIPLE_SELECTION} matches {@link Choice#MULTIPLE}
 * <li> {@link IBOTableView#T_FLAGX_1_NO_SELECTION} matches {@link Choice#EXCLUSIVE}
 * <li> {@link IBOTableView#T_FLAGM_2_MULTIPLE_SELECTION} matches {@link Choice#IMPLICIT}
 * <br>
 * <br>
 * Cell selectability may be constrained by a model. That policy removes selectability to columns rows or specific cell indexes.
 * <li>Model based selectability
 * <li>View based selectability.
 * <b>Implementation Note</b> : 
 * <br>
 * <br>
 * <b>Examples of use</b>: <br>
 * <li> {@link MLogViewer} class <b>isA</b> TableView. It implements the
 * {@link MLogViewer#log(String, int)} method to update the model? Why use or
 * usesA TableView. IsA TableView when class needs to override a Method (Usually
 * {@link TableView#manageKeyInput(InputConfig)} ) or use a protected field <br>
 * <li>Table is only used to select in a model. A wizard displaying a linked
 * list of Choices. At each step, each model is fed to the TableView. When users
 * select one item, TableView mark model index as selected and next Model Choice
 * is given to the TableView
 * <li>Using it like Swing layouts? <br>
 * <li>{@link MenuBar} <b>usesA</b> TableView as a structure service to display
 * menus and another TableView to display menu choices. When MenuBar drawable is
 * pressed, MenuBar delegates but after it asks if Menu is Pressed. If so,
 * MenuBar fires the Menu routine Since the MenuBar is only 3 cells, it acts as
 * the Model. 
 * <br>
 * <br>
 * @author Charles-Philip Bentley
 * 
 * @see ViewDrawable
 * @see IBOCellPolicy
 */
public class TableView extends ViewDrawable implements IDrawableListener, IEventConsumer, IDrawListener, IBOTypesDrw, IBOCellPolicy, IBOTableView, ICommandable {

   private static final int SETUP_WEAK              = Integer.MIN_VALUE;

   private static final int SPAN_PADDING            = 1;

   public static int        TRANSITION_HISTORY_SIZE = 5;

   /**
    * Gets the integer value are index i.
    * <br>
    * <br>
    * only exception is if array is of length 1. it returns value at index 0
    * When index is bigger than array, return last value
    * <br>
    * <br>
    * @param sizes
    * @param i
    * @return integer at i or 0
    * @throws IndexOutOfBoundsException
    *             if i is too big
    */
   public static int getArrayFirstValOrIndex(int[] sizes, int i) {
      if (sizes.length == 0) {
         throw new IllegalArgumentException("No Sizes for i=" + i);
      }
      if (i >= sizes.length) {
         return sizes[sizes.length - 1];
      }
      return sizes[i];
   }

   private int[]            colPolicies;

   /**
    * Created during a reshape when Tech Flag.
    * 
    * {@link IBOTableView#T_FLAGX_8_SELECT_EMPTY_CELLS} is set. <br>
    */
   private Drawable         emptyBluePrint;

   /**
    * Horizontal separator
    */
   private ByteObject       figureHLines;

   private ByteObject       figureOverlay;

   /**
    * Figure for drawing the vertical lines
    */
   private ByteObject       figureVLines;

   /**
    * Helper flags
    */
   private int              helpers;

   private int[]            indexRepaints;

   private int              innerState;

   /**
    * Maps model objects to {@link IDrawable}.
    * <br>
    * <br>
    * May be null in which case {@link TableView} is subclassed and that class provides the mapping or Drawable directly.
    * <br>
    * <br>
    * Contains a cache of {@link IDrawable} instances.
    */
   protected DrawableMapper mapperObjectDrawable;

   /**
    * {@link ITableModel} provides Data fed to {@link Drawable} mapper.
    * <br>
    * <br>
    * <li>Gives model genetics
    * <li>cell selectability
    * <li>Master Table policy {@link ByteObject}.
    * <li>Column and Rows titles data (as Object mapped to {@link Drawable} by mapper)
    * <li>Generates events for the {@link TableView} to consume.
    * <br>
    * May be null but the getSize method must be overriden to return something other than 0.
    * <br>
    * <br>
    * View registers for model events.
    * Model may have its own {@link TableSelectionModel}
    * .
    */
   protected ITableModel    model;

   /**
    * Can be null? NO!
    */
   private CellModel        modelCol;

   /**
    * Provide some info about the modeled data. Some subclass only provide that, instead of a full fledged {@link ITableModel}.
    */
   protected ByteObject     modelGenetics;

   /**
    * Model Mask Length
    */
   private int              modelLen     = 0;

   /**
    * TODO Model Mask is there because one View may decide to display a model's data in different ways.
    * Some index may be displayed on the Menubar.
    * <br>
    * Modifying the model would not work. It is really at the View level this must be done.
    * <br>
    * Not to be confused with making a record invisible, in which case, one can work at the model level.
    */
   private int              modelOffset  = 0;

   /**
    * {@link CellModel} is initialized 
    */
   private CellModel        modelRow;

   private int[]            pointed      = new int[2];

   /**
    * Could be initialized to null. 
    */
   protected ByteObject     policyTable;

   /**
    * Producer ID for {@link EventChannel} and {@link BusEvent}.
    * <br>
    * <br>
    * Its value is decided during TableView initialization using {@link EventChannel#getNextProducerID(int)}
    */
   private int              producerID   = -1;

   /**
    * Tells which cells are root span or slaved span or none <br>
    * <br>
    * For the purpose of computing setup sizes: 
    * <li>Slaved cell are considered empty. 
    * <li>Master cell is considered alone. 
    * <br>
    * <br>
    * Spanning will occur after init or just before drawing. <br>
    * Given by TablePolicy <br>
    * Value of 
    * <li>0 means no spanning
    * <li>Negative value + SPAN PAD is a root the TopLeft grid
    * <li>Postive value + SPAN PAD is a slave spanned cell
    */
   private int[]            span;

   /**
    * Count the number of grid cells slaved to a span root.
    * <br>
    * This value is added to the model size count in order to compute
    * consumed number of cells.
    */
   protected int            spannedCount = 0;

   /**
    * Root {@link StyleClass} to apply to all drawables.
    * <br>
    * <br>
    * Depending on the Tech configuration
    * [ flag ], the Table Policy may override some style parameters to a column
    * <br>
    * <br>
    * In order to give different X alignments for different columns, use {@link TableView#colStyles}.
    * 
    */
   private StyleClass       tableCellStyleClass;

   /**
    * Type of Table.
    * <br>
    * <br>
    * <li>strict
    * <li>generic column
    * <li>generic row
    * <li>flow column
    * <li>flow row
    */
   private int              tableType;

   /**
    * Technical parameters for the {@link TableView}.
    */
   protected ByteObject     techTable;

   /**
    * Maps model index to grid index.<br>
    * Only needed for exotic fill orders or when spanning occurs.
    * <br>
    * Spanned slaves have the negative SPAN value index model of the span root.
    */
   private int[]            visualIndex;

   /**
    * Default Row and Column policy. <br>
    * 
    * @param styleKey
    */
   protected TableView(GuiCtx gc, StyleClass styleKey) {
      this(gc, styleKey, null, null);
   }

   /**
    * Policy Init
    * <br>
    * <br>
    * @param sc can be null and default will be used
    * @param policyTable
    */
   public TableView(GuiCtx gc, StyleClass sc, ByteObject policyTable) {
      this(gc, sc, policyTable, null);
   }

   /**
    * 
    * @param sc StyleClass on which {@link IBOTypesGui#TYPE_103_TABLE_TECH} will be fetched
    * @param policyTable TablePolicy
    * @param model when null initialize without a model. that means no data
    */
   public TableView(GuiCtx gc, StyleClass sc, ByteObject policyTable, ITableModel model) {
      super(gc, sc);
      modelCol = new CellModel(gc);
      modelRow = new CellModel(gc);
      if (policyTable != null) {
         initPolicy(policyTable);
      }
      aHelpersInitialization();
      setDataModel(model);
      //check nullities and set default values if absent. (
      if (policyTable == null) {
         policyTable = gc.getTablePolicyC().getMenuSubPolicy();
      }
   }

   /**
    * Model Init
    * <br>
    * <br>
    * @param sc
    * @param model
    */
   public TableView(GuiCtx gc, StyleClass sc, ITableModel model) {
      this(gc, sc, null, model);
   }

   /**
    * When {@link TableView#span} is not null, modifies {@link CellModel} of columns
    * and rows to include spanning definitions
    */
   private void addSpanningToConfig() {
      int addW = 0;
      int addH = 0;
      int gridIndex = 0;
      int gridV = modelCol.sepSize;
      int gridH = modelRow.sepSize;
      int numTotalRows = modelRow.numCells;
      int numTotalCols = modelCol.numCells;
      if (span != null) {
         for (int rowAbs = 0; rowAbs < numTotalRows; rowAbs++) {
            for (int colAbs = 0; colAbs < numTotalCols; colAbs++) {
               gridIndex++;
               //System.out.println("TableView#Drawing  gridIndex=" + gridIndex + " cell [" + colAbs + "," + rowAbs + "] spanID=" + span[gridIndex]);
               if (span[gridIndex] > 0) {
                  return;
               } else if (span[gridIndex] < 0) {
                  for (int j = colAbs + 1; j < numTotalCols; j++) {
                     int ngridIndex = getGridIndexFromAbs(j, rowAbs);
                     if (span[ngridIndex] == gridIndex + SPAN_PADDING) {
                        addW += modelCol.workCellSizes[j];
                        addW += gridV;
                     } else {
                        break;
                     }
                  }
                  for (int j = rowAbs + 1; j < numTotalRows; j++) {
                     int ngridIndex = getGridIndexFromAbs(colAbs, j);
                     if (span[ngridIndex] == gridIndex + SPAN_PADDING) {
                        addH += modelRow.workCellSizes[j];
                        addH += gridH;
                     } else {
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   private Object eventParamO;

   public void addEventListener(IEventConsumer listener, int eid) {
      if (producerID == -1) {
         producerID = gc.getEventsBusGui().createNewProducerID(EVENT_ID_MAX);
      }
      gc.getEventsBusGui().addConsumer(listener, producerID, eid);
   }

   /**
    * From the {@link StyleClass} of the Table, extract Tech, Cell Style Class.
    */
   private void aHelpersInitialization() {
      mapperObjectDrawable = new DrawableMapper(gc, 5);
      emptyBluePrint = new Drawable(gc, gc.getEmptySC(), this);
      //#debug
      emptyBluePrint.setDebugName("Empty Table Cell");
      updateStyleClass();

   }

   /**
    * Call this method when model recieve a structural change.
    * <br>
    * <br>
    * 
    * Loads {@link IBOGenetics} {@link ModelAux#getGenetics()}
    * <br>
    * <br>
    * Create an event relationship with {@link ITableModel#EVENT_0_FLUSH}
    * <br>
    * <br>
    * 
    */
   protected void aInitModel() {
      if (model != null) {
         //when model is not null during initialization
         ModelAux modelAux = model.getModelAux();

         //first find out the tech to use.
         ByteObject modelTech = modelAux.getTableTech();
         if (modelTech != null && !hasTechF(T_FLAGF_5_IGNORE_MODEL_TECH)) {
            if (hasTechF(T_FLAGF_6_MERGE_MODEL_TECH) || modelTech.hasFlag(T_OFFSET_04_FLAGF, T_FLAGF_6_MERGE_MODEL_TECH)) {
               //merge the policies
               this.techTable = gc.getBOC().getBOModuleManager().mergeByteObject(techTable, modelTech);
            } else {
               this.techTable = modelTech;
            }
         }

         //find out which table policy to use
         ByteObject modelPolicy = modelAux.getTablePolicy();
         boolean isAuxPolicyAPlayer = (modelPolicy != null && !hasTechF(T_FLAGF_7_IGNORE_MODEL_POLICY));
         //different behavior override, merge, ignore
         if (isAuxPolicyAPlayer || policyTable == null) {
            if (hasTechF(T_FLAGF_8_MERGE_MODEL_POLICY)) {
               //merge the policies
               this.policyTable = gc.getBOC().getBOModuleManager().mergeByteObject(policyTable, modelPolicy);
            } else {
               this.policyTable = modelPolicy;
            }
            if (policyTable == null) {
               //when still null, it defaults to one column.
               policyTable = gc.getTablePolicyC().getMenuSubPolicy();
            }
            initPolicy(policyTable);
         }
         //assert policyTable is not null 

         modelGenetics = modelAux.getModelGenetics();
         if (modelGenetics != null) {
            setHelperFlag(HELPER_FLAG_21_MODEL_STYLE, modelGenetics.hasFlag(IBOGenetics.GENE_OFFSET_02_FLAGX, IBOGenetics.GENE_FLAGX_7_MODEL_STYLE));
            if (modelGenetics.hasFlag(IBOGenetics.GENE_OFFSET_01_FLAG, IBOGenetics.GENE_FLAG_5_MAPPERINT_TYPE)) {
               //modifies String Tech
               //mapperObjectDrawable.setType(modelGenetics.get2(IGenetics.GENE_OFFSET_5INT_MAPPER_TYPE2));
            }
            if (modelGenetics.hasFlag(IBOGenetics.GENE_OFFSET_01_FLAG, IBOGenetics.GENE_FLAG_6_SELECTABILITY_DRAWABLE)) {
               setHelperFlag(HELPER_FLAG_29_UNSELECTABLE_STUFF, true);
            }
            if (modelGenetics.hasFlag(IBOGenetics.GENE_OFFSET_02_FLAGX, IBOGenetics.GENE_FLAGX_8_DRAWABLES)) {
               //NO MAPPER IS NEEDED
               mapperObjectDrawable = null;
            }
            //TODO maps the tech parameters
            if (mapperObjectDrawable != null) {
               mapperObjectDrawable.techSpecifics = modelAux.getTechSpecifics();
            }

            boolean hasCtypes = modelGenetics.hasFlag(IBOGenetics.GENE_OFFSET_02_FLAGX, IBOGenetics.GENE_FLAGX_6_MODEL_CTYPES);
            setHelperFlag(HELPER_FLAG_30_DO_CTYPES, hasCtypes);
         }
         //check for policy titles,styles etc.

         //create an event relationship check if model will generates flushes
         model.addEventListener(this, ITableModel.EVENT_0_FLUSH);
         model.addEventListener(this, ITableModel.EVENT_1_REFRESH);
      }
   }

   /**
    * Apply Minimum and Maximum sizes to value
    * 
    * @param index
    * @param value
    * @param minSizes
    * @param maxSizes
    * @return
    */
   private int applyMinMax(int index, int value, int[] minSizes, int[] maxSizes) {
      if (minSizes != null) {
         int minColSize = getArrayFirstValOrIndex(minSizes, index);
         if (minColSize != 0 && value < minColSize) {
            value = minColSize;
         }
      }
      if (maxSizes != null) {
         int maxColSize = getArrayFirstValOrIndex(maxSizes, index);
         if (maxColSize != 0 && value > maxColSize) {
            value = maxColSize;
         }
      }
      return value;
   }

   /**
    * Computes
    * <li>  {@link TableView#visualIndex}
    * <br> 
    * To be built when exotic fill model is used.
    * <br>
    * <br>
    * When {@link IBOTableView#T_OFFSET_09_MODEL_FILL_TYPE1} is 0. method is not called.
    */
   private void buildGridIndexModel() {
      int numTotalCols = modelCol.numCells;
      int numTotalRows = modelRow.numCells;
      if (techTable.get1(T_OFFSET_09_MODEL_FILL_TYPE1) != 0) {
         visualIndex = new int[numTotalCols * numTotalRows];
         int val = techTable.get2Bits1(T_OFFSET_09_MODEL_FILL_TYPE1);
         boolean isColFil = techTable.hasFlag(T_OFFSET_09_MODEL_FILL_TYPE1, T_OFFSET_09_FLAG_3FILL_COL);
         if (span == null) {
            buildNoSpan(val, isColFil);
         } else {
            buildSpan(val, isColFil);
         }
      } else if (span != null) {
         visualIndex = new int[numTotalCols * numTotalRows];
         buildSpan(C.DIAG_DIR_0_TOP_LEFT, false);
      } else {
         visualIndex = null;
         spannedCount = 0;
      }
   }

   private void buildNoSpan(int val, boolean isColFil) {
      int numTotalCols = modelCol.numCells;
      int numTotalRows = modelRow.numCells;
      int index = 0;
      switch (val) {
         case C.DIAG_DIR_0_TOP_LEFT:
            //top left
            for (int i = 0; i < numTotalRows; i++) {
               for (int j = 0; j < numTotalCols; j++) {
                  visualIndex[index] = isColFil ? (numTotalRows * j) + i : (numTotalCols * i) + j;
                  index++;
               }
            }
            break;
         case C.DIAG_DIR_1_TOP_RIGHT:
            //top right
            for (int i = 0; i < numTotalRows; i++) {
               for (int j = numTotalCols - 1; j >= 0; j--) {
                  visualIndex[index] = isColFil ? (numTotalRows * j) + i : (numTotalCols * i) + j;
                  index++;
               }
            }
            break;
         case C.DIAG_DIR_2_BOT_LEFT:
            for (int i = numTotalRows - 1; i >= 0; i--) {
               for (int j = 0; j < numTotalCols; j++) {
                  visualIndex[index] = isColFil ? (numTotalRows * j) + i : (numTotalCols * i) + j;
                  index++;
               }
            }
            break;
         case C.DIAG_DIR_3_BOT_RIGHT:
            //bottom right
            for (int i = numTotalRows - 1; i >= 0; i--) {
               for (int j = numTotalCols - 1; j >= 0; j--) {
                  visualIndex[index] = isColFil ? (numTotalRows * j) + i : (numTotalCols * i) + j;
                  index++;
               }
            }
            break;
         default:
            throw new IllegalArgumentException();
      }
   }

   /**
    * Called when span array is not null.
    * <br>
    * 
    * @param val
    * @param isColFil
    */
   private void buildSpan(int val, boolean isColFil) {
      int numTotalCols = modelCol.numCells;
      int numTotalRows = modelRow.numCells;
      int spanMod = 0;
      int index = 0;
      switch (val) {
         case C.DIAG_DIR_0_TOP_LEFT:
            //top left
            if (isColFil) {
               //
            }

            for (int i = 0; i < numTotalRows; i++) {
               for (int j = 0; j < numTotalCols; j++) {
                  if (span[index] > 0) {
                     spanMod++;
                     visualIndex[index] = span[index] - SPAN_PADDING;
                  } else {
                     visualIndex[index] = isColFil ? (numTotalRows * j) + i - spanMod : (numTotalCols * i) + j - spanMod;
                  }
                  index++;
               }
            }
            break;
         case C.DIAG_DIR_1_TOP_RIGHT:
            //top right
            for (int i = 0; i < numTotalRows; i++) {
               for (int j = numTotalCols - 1; j >= 0; j--) {
                  if (span[index] < 0) {
                     spanMod++;
                     visualIndex[index] = convertTopRight(span[index] - SPAN_PADDING);
                  } else {
                     visualIndex[index] = isColFil ? (numTotalRows * j) + i - spanMod : (numTotalCols * i) + j - spanMod;
                  }
                  index++;
               }
            }
            break;
         case 2:
            for (int i = numTotalRows - 1; i >= 0; i--) {
               for (int j = 0; j < numTotalCols; j++) {
                  if (span != null && span[index] < 0) {
                     spanMod++;
                     visualIndex[index] = convertBotLeft(span[index] - SPAN_PADDING);
                  } else {
                     visualIndex[index] = (numTotalRows * j) + i - spanMod;
                  }
                  index++;
               }
            }
            break;
         case 3:
            //bottom right
            for (int i = numTotalRows - 1; i >= 0; i--) {
               for (int j = numTotalCols - 1; j >= 0; j--) {
                  if (span != null && span[index] < 0) {
                     spanMod++;
                     visualIndex[index] = convertBotRight(span[index] - SPAN_PADDING);
                  } else {
                     visualIndex[index] = (numTotalRows * j) + i - spanMod;
                  }
                  index++;
               }
            }
            break;

         default:
            break;
      }
      spannedCount = spanMod;
   }

   /**
    * Computes
    * <li> {@link TableView#span} array
    * <li> {@link TableView#spannedCount}
    * 
    */
   private void buildSpanning() {
      if (policyTable != null && policyTable.hasFlag(IBOTablePolicy.TP_OFFSET_1FLAG, IBOTablePolicy.TP_FLAG_3SPANNING)) {
         int numTotalCols = modelCol.numCells;
         int numTotalRows = modelRow.numCells;
         span = new int[numTotalCols * numTotalRows];
         ByteObject[] p = policyTable.getSubs();
         for (int i = 2; i < p.length; i++) {
            ByteObject spanDrw = p[i];
            int colAbs = spanDrw.get2(IBOTablePolicy.SPAN_OFFSET_2COL2);
            int rowAbs = spanDrw.get2(IBOTablePolicy.SPAN_OFFSET_4ROW2);
            int colV = spanDrw.get2(IBOTablePolicy.SPAN_OFFSET_3COL_VALUE2);
            int rowV = spanDrw.get2(IBOTablePolicy.SPAN_OFFSET_5ROW_VALUE2);
            int indexGridRoot = getGridIndexFromAbs(colAbs, rowAbs);
            int indexGrid = 0;
            int endCol = 1;
            if (colV != 0) {
               endCol = numTotalCols - colAbs;
               //default case of 1.
               if (colV != 1) {
                  endCol = Math.min(endCol, colV);
               }
            }
            int endRow = 1;
            if (rowV != 0) {
               endRow = numTotalRows - rowAbs;
               //default case of 1.
               if (rowV != 1) {
                  endRow = Math.min(endRow, rowV);
               }
            }
            for (int j = 0; j < endRow; j++) {
               for (int s = 0; s < endCol; s++) {
                  indexGrid = indexGridRoot + s + (j * numTotalCols);
                  if (span[indexGrid] != 0) {
                     cleanSpan(indexGrid);
                  }
                  span[indexGrid] = indexGridRoot + SPAN_PADDING;
               }
            }
            span[indexGridRoot] = -(indexGridRoot + SPAN_PADDING); //master
         }
         int count = 0;
         for (int i = 0; i < span.length; i++) {
            if (span[i] > 0)
               count++;
         }
         spannedCount = count;

      } else {
         spannedCount = 0;
         span = null;
      }
   }

   private void checkNullities() {
   }

   private void cleanSpan(int index) {
      //remove the old span
      int key = span[index];
      for (int i = 0; i < span.length; i++) {
         if (span[i] == key || span[i] == -key) {
            span[i] = 0;
         }
      }
   }

   /**
    * 
    */
   public void consumeEvent(BusEvent e) {
      if (e.getProducer() == model) {
         if (e.getEventID() == ITableModel.EVENT_0_FLUSH) {
            //
            invalidateLayout();
            e.setFlag(BusEvent.FLAG_1_ACTED, true);
            //check if current thread is event thread with stored thread reference
            repaintDrawable();
         } else if (e.getEventID() == ITableModel.EVENT_1_REFRESH) {
            invalidateLayout();
            if (e.getParam1() != -1) {
               //specific refresh
               int index = getGridIndexFromAbs(e.getParam2(), e.getParam1());
               if (e.hasFlag(ITableModel.EVENT_FLAG_1_INDEX_PARAM)) {
                  index = e.getParam1();
               }
               mapperObjectDrawable.doUpdateDrawable(model.getObject(index), index);
            } else {
               //update all by clearing the cache. drawables will created again during next repaint
               mapperObjectDrawable.clearCache();
            }
            e.setFlag(BusEvent.FLAG_1_ACTED, true);
            if (!e.hasFlag(BusEvent.FLAG_3_USER_EVENT)) {
               repaintDrawable();
            }
         }

      }
   }

   private int convertBotLeft(int topLeftIndex) {
      int numTotalCols = modelCol.numCells;
      int numTotalRows = modelRow.numCells;
      int colAbs = getAbsColFromIndex(topLeftIndex);
      int rowAbs = getAbsRowFromIndex(topLeftIndex);
      rowAbs = numTotalRows - rowAbs;
      return rowAbs * numTotalCols + colAbs;
   }

   private int convertBotRight(int topLeftIndex) {
      int numTotalCols = modelCol.numCells;
      int numTotalRows = modelRow.numCells;
      int colAbs = getAbsColFromIndex(topLeftIndex);
      int rowAbs = getAbsRowFromIndex(topLeftIndex);
      colAbs = numTotalCols - colAbs;
      rowAbs = numTotalRows - rowAbs;
      return rowAbs * numTotalCols + colAbs;
   }

   private int convertTopRight(int topLeftIndex) {
      int numTotalCols = modelCol.numCells;
      int colAbs = getAbsColFromIndex(topLeftIndex);
      int rowAbs = getAbsRowFromIndex(topLeftIndex);
      colAbs = numTotalCols - colAbs;
      return rowAbs * numTotalCols + colAbs;
   }

   /**
    * Draw the IDrawable cell Apply the X,Y,W,H values computed.
    * <br>
    * <br>
    * @param g
    * @param x coordinate of content
    * @param y coordinate of content
    * @param colAbs
    * @param rowAbs
    * @param style
    */
   private void drawCell(GraphicsX g, int x, int y, int colAbs, int rowAbs, CellModel modelCol, CellModel modelRow) {
      int cellX = x + modelCol.getOffsetCellAbs(colAbs);
      int cellY = y + modelRow.getOffsetCellAbs(rowAbs);
      int[] r = new int[2];
      getCellsDim(r, colAbs, rowAbs);
      //System.out.println("TableView#Drawing Cell [" + colAbs + "," + rowAbs + "] cellX=" + cellX + " cellY=" + cellY + " width=" + r[0] + " height=" + r[1]);
      // the draw drawn in this cell
      drawModelDrawable(g, cellX, cellY, r[0], r[1], colAbs, rowAbs);
   }

   /**
    * Called when title Drawable put in ViewPane's header get their draw method called.
    * <br>
    * <br>
    * Since they are mere {@link Drawable} and don't know specifics of their draw, they delegate with {@link IDrawListener}.
    * <br>
    * 
    */
   public void drawContentListen(GraphicsX g, int x, int y, int w, int h, Drawable d) {
      if (d == null) {
         return;
      }
      if (d == modelCol.titlesView) {
         drawTitlesColInject(g, d);
      }
      if (d == modelRow.titlesView) {
         drawTitlesRowInject(g, d);
      }
   }

   /**
    * Use the Empty BluePrint to draw. Set selected style automatically
    * 
    * @param g
    * @param cellX
    * @param cellY
    * @param cellWidth
    * @param cellHeight
    * @param colAbs
    * @param rowAbs
    */
   private void drawEmptyCell(GraphicsX g, int cellX, int cellY, int cellWidth, int cellHeight, int colAbs, int rowAbs) {
      if (emptyBluePrint != null) {
         Drawable d = emptyBluePrint;
         // set style anyways because it is only a blueprint.
         setStructuralStyleKey(d, colAbs, rowAbs);
         if (colAbs == modelCol.selectedCellAbs && rowAbs == modelRow.selectedCellAbs) {
            d.setStateStyle(ITechDrawable.STYLE_05_SELECTED, true);
         } else {
            d.setStateStyle(ITechDrawable.STYLE_05_SELECTED, false);
         }
         d.setXY(cellX, cellY);
         d.init(cellWidth, cellHeight);
         d.draw(g);
      }
   }

   /**
    * Override this method to draw what you like.
    * <br>
    * Calls the {@link IDrawable#draw(GraphicsX)} via the {@link IDrawable#show(GraphicsX)}.
    * <br>
    * Style and Layout the Drawable.
    * <br>
    * When 
    * <li> First time drawn, animation entry is fired.
    * <li> redrawn, only the {@link IDrawable#draw(GraphicsX)} method is called.
    * <br>
    * When redrawn, optimization can be made. No more styling, no more layouting.
    * <br>
    * <br>
    * @param g
    * @param x
    * @param y
    * @param w
    * @param h
    * @param c
    * @param colAbs
    * @param rowAbs
    */
   protected void drawModelDrawable(GraphicsX g, int x, int y, int w, int h, int colAbs, int rowAbs) {
      //no recycler view like in Android or TableView like in Swift?
      //what if we have a million items in the model?
      IDrawable d = getModelDrawable(colAbs, rowAbs);
      if (d == null) {
         drawEmptyCell(g, x, y, w, h, colAbs, rowAbs);
      } else {
         //style the cell with TableView structural style and cell style.
         //check if override drawable root style key
         if (!hasHelperFlag(HELPER_FLAG_21_MODEL_STYLE)) {
            // System.out.println(Presentation.debugStyleKey(drawableStyleKey));
            d.setStyleClass(tableCellStyleClass);
         }
         //         if (hasHelperFlag(HELPER_FLAG_25_STRUCT_STYLES)) {
         //            setStructuralStyleKey(d, colAbs, rowAbs);
         //         }
         //TODO do a selection model

         d.setXY(x, y);
         d.init(w, h);
         //d.setParent(this);
         //2 cases. redrawn
         //generates the animations calls
         DrawableUtilz.aboutToShow(d);
         // selection state has already been set
         d.draw(g);
      }
   }

   /**
    * Draws special index based cells.
    */
   protected void drawSpecial(GraphicsX g) {
      if (indexRepaints != null) {
         initDataDraw(g);
         int x = getContentX();
         int y = getContentY();
         for (int i = 0; i < indexRepaints.length; i++) {
            int index = indexRepaints[i];
            int colAbs = getAbsColFromIndex(index);
            int rowAbs = getAbsRowFromIndex(index);
            drawCell(g, x, y, colAbs, rowAbs, modelCol, modelRow);
         }
      }
      indexRepaints = null;
   }

   protected void drawTitlesColInject(GraphicsX g, Drawable d) {
      // draw each row number using the style
      int firstColAbs = modelCol.firstCellAbs;
      int lastColAbs = modelCol.lastCellAbs;
      int dx = getContentX(); //force it to be there
      int dy = d.getY();
      if (viewPane != null) {
         if (viewPane.getTech().get1(IBOViewPane.VP_OFFSET_14_STYLE_VIEWPORT_MODE1) == ITechViewPane.DRW_STYLE_0_VIEWDRAWABLE) {
            dx += getStyleWLeftConsumed();
         }
      }
      StyleClass titleColStyleClass = styleClass.getStyleClass(IBOTypesGui.LINK_82_STYLE_TABLE_COL_TITLE);
      StringDrawable[] cols = modelCol.titleStringDrawables;
      if (cols == null) {
         return;
      }
      if (lastColAbs >= cols.length || firstColAbs >= cols.length) {
         //#debug
         toDLog().pDraw("modelCol", modelCol, TableView.class, "drawTitlesColInject", LVL_05_FINE, true);
         return;
      }
      for (int j = firstColAbs; j <= lastColAbs; j++) {
         if (cols[j] == null) {
            String title = modelCol.titles[j];
            cols[j] = new StringDrawable(gc, titleColStyleClass, title, ITechStringDrawable.TYPE_1_TITLE);
            cols[j].setParent(this);
            cols[j].setBehaviorFlag(ITechDrawable.BEHAVIOR_23_PARENT_EVENT_CONTROL, true);
         }
         cols[j].init(modelCol.workCellSizes[j], -1);
         cols[j].setXY(dx, dy);
         DrawableUtilz.aboutToShow(cols[j]);
         cols[j].draw(g);
         dx += cols[j].getDrawnWidth();
         dx += modelCol.sepSize;
      }
   }

   protected void drawTitlesRowInject(GraphicsX g, Drawable d) {
      // draw each row number using the style
      int firstRowAbs = modelRow.firstCellAbs;
      int lastRowAbs = modelRow.lastCellAbs;
      //TODO add flag. needed only with partial rows
      g.clipSet(d.getX(), d.getY(), d.getDrawnWidth(), d.getDrawnHeight());

      int dx = d.getX();
      //content of this TableView
      int dy = getContentY() - modelRow.undrawnTopLeft;
      if (viewPane != null) {
         if (viewPane.getTech().get1(IBOViewPane.VP_OFFSET_14_STYLE_VIEWPORT_MODE1) == ITechViewPane.DRW_STYLE_0_VIEWDRAWABLE) {
            dy += getStyleHTopConsumed();
         }
      }
      int width = d.getDrawnWidth();
      StyleClass titleRowStyleClass = styleClass.getStyleClass(IBOTypesGui.LINK_83_STYLE_TABLE_ROW_TITLE);
      StringDrawable[] rows = modelRow.titleStringDrawables;
      if (rows == null) {
         return;
      }
      if (lastRowAbs >= rows.length || firstRowAbs >= rows.length) {
         //#debug
         toDLog().pDraw("modelCol", modelCol, TableView.class, "drawTitlesRowInject", LVL_05_FINE, true);
         return;
      }
      for (int j = firstRowAbs; j <= lastRowAbs; j++) {
         if (rows[j] == null) {
            String title = modelRow.titles[j];
            rows[j] = new StringDrawable(gc, titleRowStyleClass, title, ITechStringDrawable.TYPE_1_TITLE);
            rows[j].setParent(this);
            //we don't want the default shrinking title behavior
            rows[j].setViewFlag(VIEW_GENE_30_SHRINKABLE_H, false);
            rows[j].setBehaviorFlag(ITechDrawable.BEHAVIOR_23_PARENT_EVENT_CONTROL, true);
         }
         rows[j].init(width, modelRow.workCellSizes[j]);
         rows[j].setXY(dx, dy);
         DrawableUtilz.aboutToShow(rows[j]);
         rows[j].draw(g);
         dy += rows[j].getDrawnHeight();
         dy += modelRow.sepSize;
      }
      g.clipReset();
   }

   /**
    * Called by ViewDrawable between bg and fg layers. <br>
    * 
    * <br>
    * A {@link TableView} uses <br>
    * <li> {@link ITechViewPane#VISUAL_0_LEAVE} 
    * <li>{@link ITechViewPane#VISUAL_1_PARTIAL} 
    * <li>{@link ITechViewPane#VISUAL_2_SHRINK} 
    * <li>{@link ITechViewPane#VISUAL_3_FILL} 
    * <br>
    * <br>
    * For {@link ITechViewPane#SCROLL_TYPE_0_PIXEL_UNIT}, convert situation described by both {@link ScrollConfig} and apply them to {@link Config}.
    * <br>
    * <br>
    * If x start is after 1 column, set {@link Config#firstColAbs) otherwise set {@link Config#undrawnWLeft} accordingly.
    * <br>
    * <br>
    * <b>Clip</b> when Config has partial cells to be drawn. 
    * <br>
    * <br>
    * <b>Animation</b>:
    * For variable size cells, animation is problematic. <br>
    * <br>
    * How does Pixel scrolling occurs for STRONG/STRONG? or STRONG,SET,AVERAGE ?
    * <br>
    * Consider Method must be able to draw them together. So STRONG and SET,AVERAGE. 
    * <br>
    * <br>
    * In cases of variable column sizes, we have to check if the number of
    * visible increment is compatible with policy. If it is not, 2 Config are
    * created and drawn. 
    * <br>
    * <br>
    * 2 Cases <br> 
    * <li>2 configs transition are comptatible SET,WEAK,SET,AVERAGE for example. 
    * <li>2 configs clash SET,WEAK,SET|SET => SET|WEAK,SET,SET here WEAK is shown with a different size.
    * <br>
    * <br>
    * One first old first col/root and the other with the new root col
    * <br>
    * <br>
    * The current {@link Config} is the reference.
    * <br>
    * <br>
    */
   public void drawViewDrawableContent(GraphicsX g, int x, int y, ScrollConfig scX, ScrollConfig scY) {
      initDataDraw(g);
      drawViewDrawableContentFixed(g, x, y, scX, scY);
   }

   /**
    * Match a temporary {@link Config} to {@link ScrollConfig} X and Y. Then draw that config at x,y on {@link GraphicsX}. 
    * <br>
    * <br>
    * Case when cell sizes are fixed and don't change depending selection state. <br>
    * So there is no problems with recomputing {@link Config}.
    * <br>
    * <br>
    * <li>Find the {@link Config} applicable. 
    * <li>Populate Config with xoffset,yoffset, first col, last col,
    * <li>Compute Cell Root Coordinates based on that {@link Config}. 
    * <br>
    * <br>
    * For {@link ITechViewPane#SCROLL_TYPE_0_PIXEL_UNIT}, convert situation described by both {@link ScrollConfig} and apply them to {@link Config}.
    * <br>
    * <br>
    * If x start is after 1 column, set {@link Config#firstColAbs) otherwise set {@link Config#undrawnWLeft} accordingly.
    * <br>
    * <br>
    * For {@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT}, read th {@link ScrollConfig} and apply them to {@link Config}. 
    * <br>
    * <br>
    * Sub method of {@link TableView#drawViewDrawableContent(GraphicsX, int, int, ScrollConfig, ScrollConfig)}
    * <br>
    * <br>
    * @param g
    * @param x
    * @param y
    * @param scX
    * @param scY
    */
   void drawViewDrawableContentFixed(GraphicsX g, int x, int y, ScrollConfig scX, ScrollConfig scY) {

      CellModel modelCol = this.modelCol;
      CellModel modelRow = this.modelRow;

      //may be called in a future model state
      if (!g.hasPaintCtx(ITechCanvasDrawable.REPAINT_14_SYSTEM_REPAINT)) {

         //#debug
         toDLog().pFlow("Non System Repaint", this, TableView.class, "drawViewDrawableContentFixed", LVL_05_FINE, true);

         modelCol = (CellModel) modelCol.clone();
         modelRow = (CellModel) modelRow.clone();

      }

      matchModel(modelCol, scX);
      matchModel(modelRow, scY);

      modelCol.computeCoordinates();
      modelRow.computeCoordinates();

      //System.out.println("modelRow =" + modelRow.toString());
      // matches the configurations to the Table config
      //System.out.println("TableView.drawViewDrawableContentFixed " + x + "," + y);
      drawViewDrawableContentProcessed(g, x, y, modelCol, modelRow);

   }

   /**
    * Override this method to draw your own Drawables
    * <br>
    * 
    * @param g
    * @param c
    * @param x
    * @param y
    */
   protected void drawViewDrawableContentProcessed(GraphicsX g, int x, int y, CellModel modelCol, CellModel modelRow) {
      if (hasViewFlag(VIEWSTATE_01_CLIP)) {
         g.clipSet(x, y, getContentW(), getContentH());
      }

      // first data to be printed in the 0:0 cells
      int firstRowAbs = modelRow.firstCellAbs;
      int lastRowAbs = modelRow.lastCellAbs;
      int firstColAbs = modelCol.firstCellAbs;
      int lastColAbs = modelCol.lastCellAbs;

      FigureOperator figop = gc.getDC().getFigureOperator();
      // draw H lines and V lines for the sizes of workCol offseted + len
      figop.drawFigLines(g, x, y, figureVLines, modelCol.workCellSizes, firstColAbs, lastColAbs, figureHLines, modelRow.workCellSizes, firstRowAbs, lastRowAbs);

      for (int j = firstRowAbs; j <= lastRowAbs; j++) {
         for (int i = firstColAbs; i <= lastColAbs; i++) {
            drawCell(g, x, y, i, j, modelCol, modelRow);
         }
      }
      // draw an additional overlay over the whole selected row
      if (hasTechX(T_FLAGX_3_ROW_SELECTION) && !hasTechX(T_FLAGX_5_ROW_SELECTION_NO_OVERLAY) && !hasTechX(T_FLAGX_1_NO_SELECTION)) {
         int dx = x + modelCol.getOffsetCellAbs(modelCol.firstCellAbs);
         int dy = y + modelRow.getOffsetCellAbs(modelRow.selectedCellAbs);
         int w = getContentW();
         int h = modelRow.workCellSizes[modelRow.selectedCellAbs];
         // draw an overlay of selected row
         figop.paintFigure(g, dx, dy, w, h, figureOverlay);
      }
      if (hasViewFlag(VIEWSTATE_01_CLIP)) {
         g.clipReset();
      }
   }

   /**
    * Forwards the method call {@link Drawable#managePointerInput(InputConfig)} to the Drawable.
    * <br>
    * Contrary to {@link TableView#forwardPointerSelectEvent(InputConfig)} which generates a {@link BusEvent} selection event.
    * without calling the 
    * <br>
    * Method used for press/released events only.
    * <br>
    * 
    * Finds the cell {@link Drawable} at the coordinate.
    * <br>
    * <br>
    * The {@link Drawable#managePointerInput(InputConfig)} method is called.
    * <br>
    * <br>
    * It will set the in {@link PointerGesture} the pressed Drawable.
    * @param ic
    */
   protected void forwardPointerEvent(InputConfig ic) {
      int[] pointed = getPointerDrawableCoordinates(ic);
      if (pointed != null) {
         //cell coordinates for which the pointer is over
         int i = pointed[0];
         int j = pointed[1];
         IDrawable d = getModelDrawable(i, j);
         if (d != null) {
            //when cell is not selected
            if (!isSelectedCell(i, j)) {
               boolean isSelectable = isCellDrawableSelectable(d, i, j);
               d.managePointerInput(ic);
               if (isSelectable) {
                  selectionMove(ic, i, j);
               }
               gc.getFocusCtrl().newFocusKey(ic, d);
            } else {
               d.managePointerInput(ic);
            }
         }
      }
   }

   /**
    * Forwards the selection to the drawable over which the pointer InputConfig is.
    * <br>
    * calls {@link TableView#forwardPointerSelectEventSub(InputConfig, IDrawable, int, int)}
    * <br>
    * @param ic
    */
   private void forwardPointerSelectEvent(InputConfig ic) {
      int[] pointed = getPointerDrawableCoordinates(ic);
      if (pointed != null) {
         //cell coordinates for which the pointer is over
         int i = pointed[0];
         int j = pointed[1];
         IDrawable d = getModelDrawable(i, j);
         boolean isSelectable = isCellDrawableSelectable(d, i, j);
         if (isSelectable) {
            forwardPointerSelectEventSub(ic, d, i, j);
         }
      }
   }

   /**
    * In All cases generates a {@link IBOTableView#EVENT_ID_00_SELECT} .
    * <br>
    * May also generate a {@link IBOTableView#EVENT_ID_01_SELECTION_CHANGE} before it if it was not already so
    * <br>
    * @param ic
    * @param d
    * @param i
    * @param j
    */
   private void forwardPointerSelectEventSub(InputConfig ic, IDrawable d, int i, int j) {
      if (hasTechX(T_FLAGX_3_ROW_SELECTION)) {
         modelCol.setNewSelection(i);
         modelRow.setNewSelection(j);
         selectionSelectEvent(ic);
      } else if (isSelectedCell(i, i)) {
         selectionSelectEvent(ic);
      } else {
         selectionMove(ic, i, j);
         selectionSelectEvent(ic);
      }
   }

   protected void forwardPointerSelectPress(InputConfig ic) {
      int[] pointed = getPointerDrawableCoordinates(ic);
      if (pointed != null) {
         //cell coordinates for which the pointer is over
         int i = pointed[0];
         int j = pointed[1];
         //check if the element at this index is 'selected'. In case of drawable less
         if (hasHelperFlag(HELPER_FLAG_20_SPECIAL_REPAINT)) {
            if (hasPointState(getGridIndexFromAbs(i, j), ITechDrawable.STYLE_05_SELECTED)) {
               selectionSelectEvent(ic);
            } else {
               selectionMove(ic, i, j);
            }
         } else {
            IDrawable d = getModelDrawable(i, j);
            if (d != null && d.hasStateStyle(ITechDrawable.STYLE_05_SELECTED)) {
               selectionSelectEvent(ic);
            } else {
               boolean isSelectable = isCellDrawableSelectable(d, i, j);
               if (isSelectable) {
                  selectionMove(ic, i, j);
               }
            }
         }
      }
   }

   /**
    * From model index, locate the column. <br>
    * <br>
    * Takes colspan and rowspan into account. <br>
    * A colspan cell adds its colspan value in filling order.
    * 
    * @param index
    * @return
    */
   protected int getAbsColFromIndex(int index) {
      return index % modelCol.numCells;
   }

   /**
    * From model index, locate the row. <br>
    * <br>
    * Take colspan and rowspan into account
    * 
    * @param index
    * @return
    */
   protected int getAbsRowFromIndex(int index) {
      return (index - getAbsColFromIndex(index)) / modelCol.numCells;
   }

   /**
    * Loads cell size (normal + spanning) into the int array. 
    * <br>
    * <br>
    * @param r place holder.
    * @param colAbs
    * @param rowAbs
    * @param c
    */
   public void getCellsDim(int[] r, int colAbs, int rowAbs) {
      int addW = 0;
      int addH = 0;
      if (span != null) {
         int numTotalCols = modelCol.numCells;
         int numTotalRows = modelRow.numCells;
         int gridIndex = getGridIndexFromAbs(colAbs, rowAbs);
         //System.out.println("TableView#Drawing  gridIndex=" + gridIndex + " cell [" + colAbs + "," + rowAbs + "] spanID=" + span[gridIndex]);
         if (span[gridIndex] > 0) {
            return;
         } else if (span[gridIndex] < 0) {
            for (int j = colAbs + 1; j < numTotalCols; j++) {
               int ngridIndex = getGridIndexFromAbs(j, rowAbs);
               if (span[ngridIndex] == gridIndex + SPAN_PADDING) {
                  addW += modelCol.workCellSizes[j];
                  addW += getSizeGridV();
               } else {
                  break;
               }
            }
            for (int j = rowAbs + 1; j < numTotalRows; j++) {
               int ngridIndex = getGridIndexFromAbs(colAbs, j);
               if (span[ngridIndex] == gridIndex + SPAN_PADDING) {
                  addH += modelRow.workCellSizes[j];
                  addH += getSizeGridH();
               } else {
                  break;
               }
            }
         }
      }
      r[0] = modelCol.workCellSizes[colAbs] + addW;
      r[1] = modelRow.workCellSizes[rowAbs] + addH;
   }

   /**
    * Get the best etalon {@link IDrawable} for that policy.
    * <br>
    * <br>
    * Initialized with setup sizes.
    * @param policy
    * @return
    */
   private IDrawable getEtalon(ByteObject policy) {
      // get etalon cell. initialize
      int etalonIndex = policy.get1(OFFSET_08_ETALON1);
      IDrawable d = getFirstNonNull(etalonIndex);
      int colIndex = getAbsColFromIndex(etalonIndex);
      int rowIndex = getAbsRowFromIndex(etalonIndex);
      d.init(modelCol.setupSizes[colIndex], modelRow.setupSizes[rowIndex]);
      return d;
   }

   /**
    * Get the first non null, non empty Drawable
    * <br>
    * <br>
    * @param index
    * @return null if no model or no non null values in the model.
    */
   public IDrawable getFirstNonNull(int index) {
      int size = getSize();
      IDrawable d = getModelDrawable(index);
      if (d == null) {
         for (int i = 0; i < size; i++) {
            d = getModelDrawable(i);
            if (d != null) {
               break;
            }
         }
      }
      return d;
   }

   private int getGeneticSize(boolean isRow) {
      int index = (!isRow) ? IBOGenetics.GENE_OFFSET_03_WIDTH2 : IBOGenetics.GENE_OFFSET_04_HEIGHT2;
      int size = modelGenetics.get2(index);
      return size;
   }

   /**
    * Match a temporary {@link Config} to {@link ScrollConfig} X and Y. Then
    * draw that config at x,y on {@link GraphicsX}. <br>
    * <br>
    * Case when cell sizes are variable, depending on start increment and
    * number of visible increments in the {@link Config}<br>
    * <br>
    * If the {@link ScrollConfig} are the root ones used by ViewPane, draw
    * using the current {@link Config}.
    * 
    * @param g
    * @param x
    * @param y
    * @param scX
    * @param scY
    */
   //   void drawViewDrawableContentVar(GraphicsX g, int x, int y, ScrollConfig scX, ScrollConfig scY) {
   //      Config scroll = null;
   //      if (scX != null) {
   //         // convert situation to the Config using the
   //         switch (scX.getTypeUnit()) {
   //            case IViewPane.SCROLL_TYPE_0_PIXEL_UNIT:
   //               //
   //               x -= scX.getSIStart();
   //               break;
   //            case IViewPane.SCROLL_TYPE_1_LOGIC_UNIT:
   //               break;
   //            case IViewPane.SCROLL_TYPE_2_PAGE_UNIT:
   //               break;
   //            default:
   //               throw new IllegalArgumentException();
   //         }
   //      }
   //      if (scY != null) {
   //         switch (scY.getTypeUnit()) {
   //            case IViewPane.SCROLL_TYPE_0_PIXEL_UNIT:
   //               y -= scY.getSIStart();
   //               break;
   //            case IViewPane.SCROLL_TYPE_1_LOGIC_UNIT:
   //               break;
   //            case IViewPane.SCROLL_TYPE_2_PAGE_UNIT:
   //               break;
   //            default:
   //               throw new IllegalArgumentException();
   //         }
   //      }
   //      if (scroll != null) {
   //         def = scroll;
   //      }
   //      computeCoordinatesColumns(def);
   //      computeCoordinatesRows(def);
   //      // matches the configurations to the Table config
   //      // System.out.println("TableView.drawViewDrawableContent " + x + "," +
   //      // y);
   //      drawViewDrawableContent(g, def, x, y);
   //   }

   /**
    * Grid visual index from cell coordinate (colAbs,rowAbs).
    * <br>
    * <br>
    * Unrelated to model. 
    * <br>
    * Used to identify spanning.
    * <br>
    * @param colAbs
    * @param rowAbs
    * @return
    */
   private int getGridIndexFromAbs(int colAbs, int rowAbs) {
      return modelCol.numCells * (rowAbs) + colAbs;
   }

   /**
    * Policy based initialization method. <br>
    * Computes pixels needed to draw x number of cells from that position. <br>
    * <br>
    * Policies with cell sizes function of first col/row have to call this
    * method everytime that duo of values changes. <br>
    * 
    * @param size number of cells from first col/row position index position
    * @param row
    * @return
    */
   protected int getLogicalHeight(int h) {
      int[] sizes = modelRow.workCellSizes;
      int total = 0;
      for (int i = 0; i < modelRow.numCells; i++) {
         if (i == h)
            break;
         total += getArrayFirstValOrIndex(sizes, i);
      }
      return total;
   }

   /**
    * What happens when Table has different cell sizes? {@link TableView} drawable size changes.
    * What happens if modelCol not computed?
    */
   protected int getLogicalWidth(int w) {
      int[] sizes = modelCol.workCellSizes;
      int total = 0;
      for (int i = 0; i < modelCol.numCells; i++) {
         if (i == w)
            break;
         total += getArrayFirstValOrIndex(sizes, i);
      }
      return total;
   }

   public CellModel getModelCol() {
      return modelCol;
   }

   /**
    * Get object from {@link ITableModel} and wraps it inside a {@link Drawable} using {@link DrawableMapper}.
    * <br>
    * <br>
    * CType may already be set if Mapper has ctype info.
    * <br>
    * <br>
    * Injection overrides this method.
    * <br>
    * Does nothing in a ModelLess mode. 
    * <br>
    * <br>
    * @param index 
    * @return null if model does not have an object at index
    * @throws NullPointerException when {@link DrawableMapper} is null
    */
   public IDrawable getModelDrawable(int index) {
      if (model != null) {
         if (hasHelperFlag(HELPER_FLAG_06_MODEL_MASK)) {
            index += modelOffset;
         }
         Object o = model.getObject(index);
         IDrawable d = null;
         if (mapperObjectDrawable != null) {
            d = mapperObjectDrawable.getDrawable(o, index);
         } else {
            d = (IDrawable) o;
         }
         if (d != null) {
            d.setParent(this);
            d.setBehaviorFlag(ITechDrawable.BEHAVIOR_23_PARENT_EVENT_CONTROL, true);
            //System.out.println("#TableView setParentEventControl");
         }
         return d;
      }
      return null;
   }

   /**
    * Retrieve the {@link IDrawable} drawn at the cell coordinate.
    * <br>
    * <br>
    * @param colAbs
    * @param rowAbs
    * @return may be null
    */
   public IDrawable getModelDrawable(int colAbs, int rowAbs) {
      int visualIndex = getVisualIndex(colAbs, rowAbs);
      return getModelDrawable(visualIndex);
   }

   /**
    * Iterate over all model's drawables and return the maximum DrawnWidth/DrawnHeight
    * <br>
    * Ask Model genetics before iterating over all drawables.
    * @return
    */
   private int getModelDrawablesMaxSizeFlow(boolean isHeight) {
      if (modelGenetics != null) {
         //no need to test them all if genetics gives it to you.
         int flag = (isHeight) ? IBOGenetics.GENE_FLAG_4_SAME_SIZE_H : IBOGenetics.GENE_FLAG_3_SAME_SIZE_W;
         if (modelGenetics.hasFlag(IBOGenetics.GENE_OFFSET_01_FLAG, flag)) {
            int index = (isHeight) ? IBOGenetics.GENE_OFFSET_04_HEIGHT2 : IBOGenetics.GENE_OFFSET_03_WIDTH2;
            return modelGenetics.get2(index);
         }
      }
      int size = getSize();
      int max = 0;
      for (int i = 0; i < size; i++) {
         IDrawable d = getModelDrawable(i);
         if (d != null) {
            int value = (isHeight) ? d.getDrawnHeight() : d.getDrawnWidth();
            if (value > max)
               max = value;
         }
      }
      return max;
   }

   /**
    * Drawable with structural style.
    * <br>
    * <br>
    * Post:
    * <li> Struct style is set
    * <li> Ctype if any is set
    * <li> {@link StyleClass} for cell is set
    * <br>
    * <br>
    * @param index
    * @return null if no model or model mapper returns null
    */
   public IDrawable getModelDrawableStyled(int index) {
      IDrawable d = getModelDrawable(index);
      if (d != null) {
         if (!hasHelperFlag(HELPER_FLAG_21_MODEL_STYLE)) {
            // System.out.println(Presentation.debugStyleKey(drawableStyleKey));
            d.setStyleClass(tableCellStyleClass);
         }
         if (hasCTypes()) {
            //get ctype property from model and give it to IDrawable.
            int ctype = model.getPropertyInt(ITableModel.PROPERTY_0_CTYPE, index);
            d.setCType(ctype);
         }
         if (hasHelperFlag(HELPER_FLAG_25_STRUCT_STYLES)) {
            setStructuralStyleKey(d, index);
         }
         //
         d.getLayEngine().setManualOverride(true); //the whole layout is decided by the TableView
      }
      return d;
   }

   /**
    * Return Model Drawable styled with Ctype,structural and {@link StyleClass}.
    * <br>
    * @param colAbs
    * @param rowAbs
    * @return
    */
   public IDrawable getModelDrawableStyled(int colAbs, int rowAbs) {
      int visualIndex = getVisualIndex(colAbs, rowAbs);
      return getModelDrawableStyled(visualIndex);
   }

   public CellModel getModelRow() {
      return modelRow;
   }

   private void checkInit() {
      if (modelRow == null) {
         throw new RuntimeException("No init");
      }
   }

   /**
    * Throws an exception if call is made while TableView is not initialized
    * 
    */
   public IDrawable getDrawableViewPort(int x, int y, ExecutionCtxDraw ex) {
      checkInit();
      int firstRowAbs = modelRow.firstCellAbs;
      int lastRowAbs = modelRow.lastCellAbs;
      int firstColAbs = modelCol.firstCellAbs;
      int lastColAbs = modelCol.lastCellAbs;
      int dx = getContentX();
      int dy = getContentY();
      //toLog().printState("#TableView#getPointerDrawableCoordinates  [" + dx + "," + dy + "]" + " firstRow=" + firstRowAbs + "," + lastRowAbs + " col=" + firstColAbs + "," + lastColAbs);
      int[] r = new int[2];
      for (int j = firstRowAbs; j <= lastRowAbs; j++) {
         for (int i = firstColAbs; i <= lastColAbs; i++) {
            int cellX = dx + modelCol.positions[i];
            int cellY = dy + modelRow.positions[j];
            //System.out.println("[" + i + "," + j + "] = (" + cellX + ", " + cellY + ")");
            getCellsDim(r, i, j);
            if (DrawableUtilz.isInside(x, y, cellX, cellY, r[0], r[1])) {
               //toLog().printEvent("#TableView#getPointerDrawableCoordinates ----[" + i + "," + j + "] = Point(" + ic.x + ", " + ic.y + ") isInside [" + cellX + "," + cellY + " " + (cellX + r[0])
               //      + ", " + (cellY + r[1]));
               IDrawable d = getModelDrawable(i, j);
               ex.addressX = i;
               ex.addressY = j;
               return d;
            }
         }
      }
      return null;
   }

   /**
    * Iterates over the index starting at currently selected index and returns the next index selected.
    * <br>
    * Does not care about 
    * @return -1 if non is selectable
    */
   private int getNextSelectable() {
      int selIndex = getSelectedIndex();
      int numTotalCols = modelCol.numCells;
      int numTotalRows = modelRow.numCells;
      if (hasHelperFlag(HELPER_FLAG_29_UNSELECTABLE_STUFF)) {
         for (int i = selIndex + 1; i < model.getSizeModel(); i++) {
            int col = getAbsColFromIndex(i);
            int row = getAbsRowFromIndex(i);
            if (isCellSelectable(col, row)) {
               return i;
            }
         }
         for (int i = 0; i <= selIndex; i++) {
            int col = getAbsColFromIndex(i);
            int row = getAbsRowFromIndex(i);
            if (isCellSelectable(col, row)) {
               return i;
            }
         }
         return -1;
      } else {
         return selIndex + 1;
      }
   }

   /**
    * Apply selectability and Navigation options about Around the Clock.
    * <br>
    * <br>
    * @return
    */
   public int getNextSelectableDown() {
      int row = modelRow.selectedCellAbs;
      int numTotalRows = modelRow.numCells;
      if (hasHelperFlag(HELPER_FLAG_29_UNSELECTABLE_STUFF)) {
         int count = 1;
         //is it defined on column or cell wise?
         int col = modelCol.selectedCellAbs;
         if (hasBehavior(ITechDrawable.BEHAVIOR_29_NAV_CLOCK_VERTICAL)) {
            while (count < numTotalRows) {
               int nextRow = (row + count) % numTotalRows;
               if (isCellSelectable(col, nextRow)) {
                  return nextRow;
               } else {
                  count++;
               }
            }
            return row;
         } else {
            for (int i = row + 1; i < numTotalRows; i++) {
               if (isCellSelectable(col, i)) {
                  return i;
               }
            }
            return row;
         }
      } else {
         int count = 1;
         if (hasBehavior(ITechDrawable.BEHAVIOR_29_NAV_CLOCK_VERTICAL)) {
            return (row + count) % numTotalRows;
         } else {
            return (row + count) < numTotalRows ? row + count : row;
         }
      }

   }

   /**
    * Looks for the first cells index doing horizontal look up
    * 
    * When {@link ITechDrawable#BEHAVIOR_30_NAV_CLOCK_HORIZONTAL} is set
    * 
    * @return
    * @see TableView#getNextSelectableUp()
    */
   private int getNextSelectableLeft() {
      int col = modelCol.selectedCellAbs;
      int numTotalCols = modelCol.numCells;
      int numTotalRows = modelRow.numCells;
      if (hasHelperFlag(HELPER_FLAG_29_UNSELECTABLE_STUFF)) {
         int count = 1;
         //is it defined on column or cell wise?
         int row = modelRow.selectedCellAbs;
         if (hasBehavior(ITechDrawable.BEHAVIOR_30_NAV_CLOCK_HORIZONTAL)) {
            int nextCol = 0;
            while (count < numTotalRows) {
               if (hasTechY(T_FLAGM_5_CLOCK_FULL_H_LOOP)) {
                  nextCol = col - count;
                  if (nextCol < 0) {
                     nextCol = numTotalCols - 1;
                     row = (row - 1 < 0) ? numTotalRows - 1 : row - 1;
                  }
               } else {
                  nextCol = (col - count < 0) ? numTotalCols + (col - count) : (col - count);
               }
               if (isCellSelectable(nextCol, row)) {
                  return nextCol;
               } else {
                  count++;
               }
            }
            return col;
         } else {
            for (int i = col - 1; i >= 0; i--) {
               if (isCellSelectable(i, row)) {
                  return i;
               }
            }
            return col;
         }
      } else {
         if (hasBehavior(ITechDrawable.BEHAVIOR_30_NAV_CLOCK_HORIZONTAL)) {
            return (col == 0) ? numTotalRows - 1 : (col - 1);
         } else {
            return (col == 0) ? col : col - 1;
         }
      }
   }

   private int getNextSelectableRight() {
      int col = modelCol.selectedCellAbs;
      int numTotalCols = modelCol.numCells;
      int numTotalRows = modelRow.numCells;

      if (hasHelperFlag(HELPER_FLAG_29_UNSELECTABLE_STUFF)) {
         int count = 1;
         //is it defined on column or cell wise?
         int row = modelRow.selectedCellAbs;

         if (hasBehavior(ITechDrawable.BEHAVIOR_30_NAV_CLOCK_HORIZONTAL)) {
            int nextCol = 0;
            while (count < numTotalCols) {
               if (hasTechY(T_FLAGM_5_CLOCK_FULL_H_LOOP)) {
                  nextCol = (col + count);
                  if (nextCol >= numTotalCols) {
                     nextCol = nextCol % numTotalCols;
                     row = (row + 1) % numTotalRows;
                  }
               } else {
                  nextCol = (col + count) % numTotalCols;
               }
               if (isCellSelectable(nextCol, row)) {
                  return nextCol;
               } else {
                  count++;
               }
            }
            return col;
         } else {
            for (int i = col + 1; i < numTotalCols; i++) {
               if (isCellSelectable(i, row)) {
                  return i;
               }
            }
            return col;
         }
      } else {
         if (hasBehavior(ITechDrawable.BEHAVIOR_30_NAV_CLOCK_HORIZONTAL)) {
            return (col + 1) % numTotalCols;
         } else {
            return (col + 1 < numTotalCols) ? col + 1 : col;
         }
      }
   }

   /**
    * Must decide from Navigation option if it does an around the clock
    * @return
    */
   private int getNextSelectableUp() {
      int row = modelRow.selectedCellAbs;
      int numTotalCols = modelCol.numCells;
      int numTotalRows = modelRow.numCells;
      if (hasHelperFlag(HELPER_FLAG_29_UNSELECTABLE_STUFF)) {
         int count = 1;
         //is it defined on column or cell wise?
         int col = modelCol.selectedCellAbs;
         if (hasBehavior(ITechDrawable.BEHAVIOR_29_NAV_CLOCK_VERTICAL)) {
            while (count < numTotalRows) {
               int nextRow = (row - count < 0) ? numTotalRows + (row - count) : (row - count);
               if (isCellSelectable(col, nextRow)) {
                  return nextRow;
               } else {
                  count++;
               }
            }
            return row;
         } else {
            for (int i = row - 1; i >= 0; i--) {
               if (isCellSelectable(col, i)) {
                  return i;
               }
            }
            return row;
         }
      } else {
         if (hasBehavior(ITechDrawable.BEHAVIOR_29_NAV_CLOCK_VERTICAL)) {
            return (row == 0) ? numTotalRows - 1 : (row - 1);
         } else {
            return (row == 0) ? row : row - 1;
         }
      }
   }

   public Drawable getNullCellPrint() {
      return emptyBluePrint;
   }

   public int getNumCols() {
      return modelCol.numCells;
   }

   public int getNumRows() {
      return modelRow.numCells;
   }

   /**
    * Only to be called in the serialized Event Thread.
    * <br>
    * <br>
    * Called by {@link TableView#forwardPointerEvent(InputConfig)}
    * Called by {@link TableView#forwardPointerEvent(InputConfig)}
    * <br>
    * <br>
    * 
    * @param ic
    * @return
    */
   private int[] getPointerDrawableCoordinates(InputConfig ic) {
      int firstRowAbs = modelRow.firstCellAbs;
      int lastRowAbs = modelRow.lastCellAbs;
      int firstColAbs = modelCol.firstCellAbs;
      int lastColAbs = modelCol.lastCellAbs;
      int dx = getContentX();
      int dy = getContentY();
      //toLog().printState("#TableView#getPointerDrawableCoordinates  [" + dx + "," + dy + "]" + " firstRow=" + firstRowAbs + "," + lastRowAbs + " col=" + firstColAbs + "," + lastColAbs);
      int[] r = new int[2];
      for (int j = firstRowAbs; j <= lastRowAbs; j++) {
         for (int i = firstColAbs; i <= lastColAbs; i++) {
            int cellX = dx + modelCol.positions[i];
            int cellY = dy + modelRow.positions[j];
            //System.out.println("[" + i + "," + j + "] = (" + cellX + ", " + cellY + ")");
            getCellsDim(r, i, j);
            if (DrawableUtilz.isInside(ic, cellX, cellY, r[0], r[1])) {
               //toLog().printEvent("#TableView#getPointerDrawableCoordinates ----[" + i + "," + j + "] = Point(" + ic.x + ", " + ic.y + ") isInside [" + cellX + "," + cellY + " " + (cellX + r[0])
               //      + ", " + (cellY + r[1]));
               pointed[0] = i;
               pointed[1] = j;
               return pointed;
            }
         }
      }
      return null;
   }

   /**
    * Only to be called in the serialized Event Thread.
    * <br>
    * <br>
    * Called by {@link TableView#forwardPointerEvent(InputConfig)}
    * Called by {@link TableView#forwardPointerEvent(InputConfig)}
    * <br>
    * <br>
    * 
    * @param ic
    * @return
    */
   private int[] getPointerDrawableCoordinates(InputState ic) {
      int firstRowAbs = modelRow.firstCellAbs;
      int lastRowAbs = modelRow.lastCellAbs;
      int firstColAbs = modelCol.firstCellAbs;
      int lastColAbs = modelCol.lastCellAbs;
      int dx = getContentX();
      int dy = getContentY();
      //toLog().printState("#TableView#getPointerDrawableCoordinates  [" + dx + "," + dy + "]" + " firstRow=" + firstRowAbs + "," + lastRowAbs + " col=" + firstColAbs + "," + lastColAbs);
      int[] r = new int[2];
      for (int j = firstRowAbs; j <= lastRowAbs; j++) {
         for (int i = firstColAbs; i <= lastColAbs; i++) {
            int cellX = dx + modelCol.positions[i];
            int cellY = dy + modelRow.positions[j];
            //System.out.println("[" + i + "," + j + "] = (" + cellX + ", " + cellY + ")");
            getCellsDim(r, i, j);
            if (DrawableUtilz.isInside(ic, cellX, cellY, r[0], r[1])) {
               //toLog().printEvent("#TableView#getPointerDrawableCoordinates ----[" + i + "," + j + "] = Point(" + ic.x + ", " + ic.y + ") isInside [" + cellX + "," + cellY + " " + (cellX + r[0])
               //      + ", " + (cellY + r[1]));
               pointed[0] = i;
               pointed[1] = j;
               return pointed;
            }
         }
      }
      return null;
   }

   public int getProducerID() {
      return producerID;
   }

   /**
    * Return absolute model index of currently column of selected cell.
    * 
    * @return
    */
   public int getSelectedCol() {
      return modelCol.selectedCellAbs;
   }

   /**
    * May return null if selected index has not drawable
    * @return
    */
   public IDrawable getSelectedDrawable() {
      return getModelDrawable(getSelectedIndex());
   }

   /**
    * @param l
    * @return -1 if none selected
    */
   public static int getFirstSelected(TableView l) {
      boolean[] bs = new boolean[l.getSize()];
      l.getSelectedFlags(bs);
      for (int i = 0; i < bs.length; i++) {
         if (bs[i])
            return i;
      }
      return -1;
   }

   /**
    * 
    * @param l the List
    * @return first selected index or -1 if none
    */
   public static int getSelectedIndex(TableView l) {
      boolean[] b = new boolean[l.getSize()];
      l.getSelectedFlags(b);
      for (int i = 0; i < b.length; i++) {
         if (b[i] == true) {
            return i;
         }
      }
      return -1;
   }

   /**
    * 
    * @param l the List
    * @return first selected index or -1 if none
    */
   public static int[] getSelectedIndexes(TableView l) {
      boolean[] b = new boolean[l.getSize()];
      int si = l.getSelectedFlags(b);
      int[] ints = new int[si];
      int count = 0;
      for (int i = 0; i < b.length; i++) {
         if (b[i] == true) {
            ints[count] = i;
            count++;
         }
      }
      return ints;
   }

   public static String getSelectedString(TableView list) {
      return list.getString(getSelectedIndex(list));
   }

   public static String[] getSelectedStrings(TableView list) {
      boolean[] selectedIDs = new boolean[list.getSize()];
      int numSelected = list.getSelectedFlags(selectedIDs);
      String[] langs = new String[numSelected];
      int count = 0;
      for (int i = 0; i < selectedIDs.length; i++) {
         if (selectedIDs[i]) {
            langs[count] = list.getString(i);
         }
      }
      return langs;
   }

   /**
    * indexing starts at 0
    * @param l
    * @param index visualIndex - 1
    */
   public static void setSelectedIndex(TableView l, int index) {
      if (l.getSize() > index) {
         boolean[] bols = new boolean[l.getSize()];
         for (int i = 0; i < bols.length; i++) {
            if (i == index) {
               bols[i] = true;
            } else
               bols[i] = false;
         }
         l.setSelectedFlags(bols);
      }
   }

   /**
    * Attempts to select str in list. Deselects all others
    * @param list
    * @param str
    */
   public static void setSelectedString(TableView list, String str) {
      int size = list.getSize();
      for (int i = 0; i < size; i++) {
         if (list.getString(i).equals(str)) {
            setSelectedIndex(list, i);
            return;
         }
      }
   }

   /**
    * Reads the cells and set to true/false if they are selected
    * @param b
    * @return the number of trues
    */
   public int getSelectedFlags(boolean[] b) {
      return 0;
   }

   /**
    * Last selected visual index. 
    * <br>
    * <br>
    * For complex selections, using {@link TableSelectionModel}.
    * <br>
    * 
    * @return
    */
   public int getSelectedIndex() {
      return getGridIndexFromAbs(modelCol.selectedCellAbs, modelRow.selectedCellAbs);
   }

   /**
    * Return previously selected (head selection) index.
    * <br>
    * TODO  
    * @return
    */
   public int getSelectedIndexPrevious() {
      return getGridIndexFromAbs(modelCol.oldSelectedCellAbs, modelRow.oldSelectedCellAbs);

   }

   /**
    * Return absolute model index of the first row with the a selected cell.
    * 
    * @return
    */
   public int getSelectedRow() {
      return modelRow.selectedCellAbs;
   }

   /**
    * Model method that return the number of displayable elements in this {@link TableView}
    * <br>
    * It reads {@link ITableModel#getSizeModel()} ...
    * This method is thread safe.
    * @return
    */
   public int getSize() {
      int val = 0;
      if (model != null) {
         val = model.getSizeModel();
      }
      if (hasHelperFlag(HELPER_FLAG_06_MODEL_MASK) && modelLen != 0) {
         val = Math.min(val, modelLen);
      }
      return val;
   }

   /**
    * Size of horizontal grid lines.
    * @return
    */
   private int getSizeGridH() {
      if (hasTech(T_FLAG_7_DRAW_GRID)) {
         return techTable.getValue(T_OFFSET_05_HGRID_SIZE1, 1);
      }
      return 0;
   }

   /**
    * Size of vertical grid lines.
    * @return
    */
   private int getSizeGridV() {
      if (hasTech(T_FLAG_7_DRAW_GRID)) {
         return techTable.getValue(T_OFFSET_06_VGRID_SIZE1, 1);
      }
      return 0;
   }

   /**
    * Shortcut method
    * @param i
    * @return
    */
   public String getString(int i) {
      return getTableModel().getObject(i).toString();
   }

   /**
    * Read only mode of the model.
    * <br>
    * Because the TableView is only a displayer.
    * <br>
    * The user of the {@link TableView} controls the model
    * <br>
    * Set a Model using {@link TableView#setDataModel(ITableModel)}
    * <br>
    * <br>
    * @return
    */
   public ITableModel getTableModel() {
      return model;
   }

   /**
    * Does the ViewState include
    * <li> Selection state
    * <li> ViewState of all visible and invisible cells
    * <br>
    * <br>
    * Automatic Header viewstate saving
    */
   public ViewState getViewState() {
      ViewState vs = super.getViewState();
      //we create a ViewState according to current model size.
      //
      for (int i = 0; i < getSize(); i++) {
         IDrawable d = getModelDrawable(i);
         //only save view state when not default
         vs.addViewState(d);
      }
      return vs;
   }

   /**
    * Return the visual index associated with grid cell. Take into account the spanning and mask offseting.
    * <br>
    * {@link TableView#getGridIndexFromAbs(int, int)}
    * <br>
    * @param colAbs
    * @param rowAbs
    * @return integer visual index
    * 
    */
   public int getVisualIndex(int colAbs, int rowAbs) {
      int gridIndex = getGridIndexFromAbs(colAbs, rowAbs);
      if (visualIndex == null) {
         //case when not used
         return gridIndex;
      } else {
         if (gridIndex >= visualIndex.length) {
            //#debug
            toDLog().pFlow(gridIndex + " >=" + visualIndex.length, this, TableView.class, "getVisualIndex", LVL_05_FINE, true);
         }
         return visualIndex[gridIndex];
      }
   }

   /**
    * True when model not null and has {@link ITableModel#PROPERTY_0_CTYPE}
    * <br>
    * @return
    */
   private boolean hasCTypes() {
      return hasHelperFlag(HELPER_FLAG_30_DO_CTYPES);
   }

   public boolean hasHelperFlag(int flag) {
      return BitUtils.hasFlag(helpers, flag);
   }

   /**
    * 
    * @param index
    * @param state
    * @return
    */
   public boolean hasPointState(int index, int state) {
      return false;
   }

   /**
    * Simple check on {@link IBOTableView#T_FLAGX_1_NO_SELECTION}
    * <br>
    * <br>
    * 
    * @return
    */
   private boolean hasSelectionBehavior() {
      return !hasTech(T_FLAGX_1_NO_SELECTION);
   }

   protected boolean hasTech(int flag) {
      return techTable.hasFlag(T_OFFSET_01_FLAG, flag);
   }

   protected boolean hasTechF(int flag) {
      return techTable.hasFlag(T_OFFSET_04_FLAGF, flag);
   }

   protected boolean hasTechM(int flag) {
      return techTable.hasFlag(T_OFFSET_11_FLAGM, flag);
   }

   protected boolean hasTechX(int flag) {
      return techTable.hasFlag(T_OFFSET_02_FLAGX, flag);
   }

   protected boolean hasTechY(int flag) {
      return techTable.hasFlag(T_OFFSET_03_FLAGY, flag);
   }

   private void initConfigWorkSizes() {
      modelCol.initWorkSizes();
      modelRow.initWorkSizes();
   }

   /**
    * Init to be done before the painting is done.
    * <br>
    * <br>
    * Implemented by subclasses.
    * <br>
    * @param g
    */
   public void initDataDraw(GraphicsX g) {

   }

   /**
   * 
   */
   public boolean initInject(int w, int h, Drawable d) {

      //#debug
      toDLog().pInit("w=" + w + " h=" + h + " " + d.toString1Line(), this, TableView.class, "initInject", LVL_05_FINE, true);

      if (d == modelCol.titlesView) {
         //System.out.println("#TableView#initInject Column Titles addW=" + addW);
         StyleClass titleColStyleClass = styleClass.getSCNotNull(IBOTypesGui.LINK_82_STYLE_TABLE_COL_TITLE);
         ByteObject style = titleColStyleClass.getRootStyle();
         int colH = getStyleOp().getStyleFont(style).getHeight() + getStyleHConsumed();
         modelCol.titlesView.setDrawableSize(w, colH, true);
         return true;
      } else if (d == modelRow.titlesView) {
         //System.out.println("#TableView#initInject Row Titles addH=" + addH);
         StyleClass titleRowStyleClass = styleClass.getSCNotNull(IBOTypesGui.LINK_83_STYLE_TABLE_ROW_TITLE);
         ByteObject style = titleRowStyleClass.getRootStyle();
         int rowW = (getStyleOp().getStyleFont(style).charWidth('9') * 2) + getStyleHConsumed();
         modelRow.titlesView.setDrawableSize(rowW, h, true);
         return true;
      }
      return false;
   }

   /**
    * Called by {@link DrawableInjected}.
    * <br>
    * Width of Column Title container is the 
    * 
    * Use the {@link Drawable#setDrawableSize(int, int, boolean)} to set the effective
    * pixel size.
    * 
    * @param sizerW sizer with the width given to the {@link DrawableInjected}
    * @param sizerH
    */
   public boolean initListen(ByteObject sizerW, ByteObject sizerH, Drawable d) {
      //#debug
      toLog().pInit("w=" + sizerW.toString1Line() + " h=" + sizerH.toString1Line() + " " + d.toString1Line(), this, TableView.class, "initInject", LVL_05_FINE, true);
      //init column titles drawable. Height is style dependent. Width is decided by?
      //TODO Depending on Table settings. Columns may influence preferred size of cells?
      //Yes
      if (d == modelCol.titlesView) {
         //#debug
         toLog().pInit("Column Titles Width", sizerW, TableView.class, "initInject");
         //init all title drawable. take the biggest size
         return true;
      } else if (d == modelRow.titlesView) {
         //#debug
         toLog().pInit("Row Titles Height", sizerH, TableView.class, "initInject");
         modelCol.titlesView.initH(sizerH);
         return true;
      }
      return false;
   }

   /**
    * Sibling of {@link TableView#initModelDrawableRow(int, int, int[])}
    * Iterate over all cells row in a column. <br>
    * <br>
    * <br>
    * 
    * @param colAbs column absolute index
    * @param width pixel width given to that column.
    * @param rowSizes pre computed row sizes.
    * @return
    */
   private int initModelDrawableCol(int colAbs, int width, int[] rowSizes) {
      int max = 0;
      int rowAbs = 0;
      //TODO what if number of cells has not been computed yet?
      for (int j = 0; j < modelRow.numCells; j++) {
         // take each row
         IDrawable d = getModelDrawableStyled(colAbs, rowAbs);
         if (d != null) {
            // they might not have been layout before
            int height = getArrayFirstValOrIndex(rowSizes, j);
            d.init(width, height);
            // maximum height is available room
            if (d.getDrawnWidth() > max) {
               max = d.getDrawnWidth();
            }
         }
         rowAbs++;
      }

      //
      if (!modelCol.policy.hasFlag(IBOCellPolicy.OFFSET_02_FLAG, IBOCellPolicy.FLAG_7_OVERSIZE)) {
         //oversized cells are not allowed. check against ViewPort's width.
         if (!layEngine.isContextualW()) {
            //implicit init width gives 0 or negative contentWidth.
            int c = getContentW();
            if (max > c) {
               setHelperFlag(HELPER_FLAG_03_CAP_OVERSIZE, true);
               max = c;
            }
         }
      }
      return max;
   }

   //   public void initScrollingConfig2(ScrollConfig scX, ScrollConfig scY) {
   //
   //      if (scX != null) {
   //         if (scX.getTypeUnit() == IViewPane.SCROLL_TYPE_1_LOGIC_UNIT) {
   //            int sizeTotal = getContentW();
   //            int siTotal = modelCol.getScrollTotal();
   //            if (hasHelperFlag(HELPER_FLAG_08_ALL_COLS_SAME_SIZE)) {
   //
   //               scX.initConfigLogic(modelCol.workCellSizes[0], sizeTotal, siTotal, getSizeGridV());
   //            } else {
   //               scX.initConfigLogic(modelCol.workCellSizes, sizeTotal, siTotal, getSizeGridV());
   //            }
   //         } else {
   //            DrawableUtilz.initConfigX(this, scX);
   //         }
   //      }
   //      if (scY != null) {
   //         if (scY.getTypeUnit() == IViewPane.SCROLL_TYPE_1_LOGIC_UNIT) {
   //            int siTotal = modelRow.getScrollTotal();
   //            if (hasHelperFlag(HELPER_FLAG_09_ALL_ROWS_SAME_SIZE)) {
   //               scY.initConfigLogic(modelRow.workCellSizes[0], getContentH(), siTotal, getSizeGridH());
   //            } else {
   //               scY.initConfigLogic(modelRow.workCellSizes, getContentH(), siTotal, getSizeGridH());
   //            }
   //         } else {
   //            DrawableUtilz.initConfigY(this, scY);
   //         }
   //      }
   //      //DrawableUtilz.debugScrollConfigs("TableView#initScrollingConfig", scX, scY);
   //   }

   /**
    * Iterate over columns inside a row and initialize drawables with height and setupColSizes that have been pre-computed. 
    * <br>
    * <br>
    * <b>Why</b>? : what is the row height? needed when row size is implicit. code has to init all cells in that row
    * and get the maximum height and apply it to all.
    * <br>
    * <br>
    * Returned value may be bigger than ViewPort's height. <br>
    * Caller applies {@link IBOCellPolicy#FLAG_7_OVERSIZE} policy. <br>
    * 
    * Calling this method is not necessary when model genetics says all cells have the same height
    * <br>
    * <br>
    * @param rowAbs
    * @param height variable height such as implicit(0) or logical (<0)
    * @param colSizes setup column sizes. Must not bel null.
    * @return the maximum height value computed.
    */
   private int initModelDrawableRow(int rowAbs, int height, int[] colSizes) {
      int max = 0;
      int colAbs = 0;
      for (int j = 0; j < modelCol.numCells; j++) {
         //take each row
         IDrawable d = getModelDrawableStyled(colAbs, rowAbs);
         if (d != null) {
            int width = getArrayFirstValOrIndex(colSizes, j);
            d.init(width, height);
            // maximum height is available room
            if (d.getDrawnHeight() > max) {
               max = d.getDrawnHeight();
            }
         }
         colAbs++;
      }
      if (!modelRow.policy.hasFlag(IBOCellPolicy.OFFSET_02_FLAG, IBOCellPolicy.FLAG_7_OVERSIZE)) {
         if (!layEngine.isContextualH()) {
            int ch = getContentH();
            if (max > ch) {
               setHelperFlag(HELPER_FLAG_03_CAP_OVERSIZE, true);
               max = ch;
            }
         }
      }
      return max;
   }

   /**
    * Compute as many rows possible to fit inside totalSize.
    * <br>
    * <br>
    * Return the array of those computed heights.
    * <b>Why</b>?: Flow must know the number of rows
    * <br>
    * <br>
    * @param w
    * @param h
    * @param total
    * @param sepSize
    * @return
    */
   private int[] initModelDrawablesDimension(int w, int h, int total, int sepSize, boolean isRow) {
      int size = getSize();
      int count = 0;
      int currTotal = 0;
      int mw = 0;
      for (int i = 0; i < size; i++) {
         IDrawable d = getModelDrawableStyled(i);
         if (d != null) {
            // the number of columns must be known before that method is called.
            d.init(w, h);
            mw = (isRow) ? d.getDrawnHeight() : d.getDrawnWidth();
            if (mw + currTotal < total) {
               count++;
               currTotal += mw;
               currTotal += sepSize;
               //System.out.println("initModelDrawablesDimension :  count=" + count + " currTotal=" + currTotal + " mw=" + mw + " totalSize=" + total);
            } else {
               break;
            }
         }
      }
      //TODO what if count is zero when all drawables are null?
      int[] c = new int[count];
      for (int i = 0; i < count; i++) {
         IDrawable d = getModelDrawable(i);
         if (d != null) {
            c[i] = (isRow) ? d.getDrawnHeight() : d.getDrawnWidth();
         }
      }
      return c;
   }

   /**
    * <b>Why</b>:Flow implicit needs to initialize all drawables in the model and take the biggest values.
    * <br>
    * <br>
    * This method sets the style and call the {@link IDrawable#init(int, int)} on all drawables. <br>
    * <br>
    * 
    * @param w
    * @param h
    */
   private void initModelDrawablesDimensionAll(int w, int h) {
      int size = getSize();
      for (int i = 0; i < size; i++) {
         IDrawable d = getModelDrawableStyled(i);
         if (d != null) {
            // the number of columns must be known before that method is called.
            d.init(w, h);
         }
      }
   }

   /**
    * Init the table policy.
    * <br>
    * <br>
    * When null, defaults to defaults
    * <br>
    * <br>
    * 
    * @param policyTable
    */
   protected void initPolicy(ByteObject policyTable) {
      if (policyTable == null) {
         throw new NullPointerException("Null Table Policy. You must link at least one!");
      }
      this.policyTable = policyTable;
      ByteObject[] param = policyTable.getSubs();
      ByteObject policyCol = param[0];
      if (policyCol == null) {
         throw new NullPointerException("policy Col");
      }
      LitteralStringOperator lsf = gc.getBOC().getLitteralStringOperator();
      if (policyCol.hasFlag(IBOCellPolicy.OFFSET_03_FLAGP, IBOCellPolicy.FLAGP_7_TITLE_STRING_DEF)) {
         modelCol.setTitles(lsf.getLitteralArrayString(policyCol.getSubFirst(TYPE_008_LIT_ARRAY_STRING)));
      }
      modelCol.policy = policyCol;
      ByteObject policyRow = param[1];

      if (policyRow == null) {
         throw new NullPointerException("policy Row");
      }

      if (policyRow.hasFlag(IBOCellPolicy.OFFSET_03_FLAGP, IBOCellPolicy.FLAGP_7_TITLE_STRING_DEF)) {
         modelRow.setTitles(lsf.getLitteralArrayString(policyRow.getSubFirst(TYPE_008_LIT_ARRAY_STRING)));
      }
      modelRow.policy = policyRow;

   }

   /**
    * Variable cell sizes depend on value
    */
   private void initPreferredSizeFromCellSizes(int width, int height) {
      try {
         //first compute number of cells
         setupNumColRows();

         // once number of cells is known
         //layout the space used by row numbers and column titles
         setupTitlesRow();
         setupTitlesCol();

         // setupSizes();
         setupPolicyType2();
         // post: symbolized cell sizes are know.

         // Config work size are taken from setupsize
         //initConfigWorkSizes(u);
         initConfigWorkSizes();

         buildSpanning();
         buildGridIndexModel();

         // compute one time for variable size cells. symbolized/variable sizes compute
         modelCol.computeSetupVariableCellSizes(width);
         modelRow.computeSetupVariableCellSizes(height);
         // once sizes are known
         // computeVisibleRowsInit(u, getContentH());
         // computeVisibleColsInit(u, getContentW());

         // computeCellCoordinates(u);
         //compute span
         addSpanningToConfig();
         updateNavFlag();

         if (hasTechF(T_FLAGF_1_SELECT_KEPT_WITHOUT_FOCUS_KEY)) {
            setSelectionState();
         }
      } catch (Exception e) {
         //#debug
         e.printStackTrace();
         //#debug
         throw new RuntimeException("In the TableView Compute Config method. Debug Stop");
      }

      // computes the preferred sizes based on the configuration
      pw = IntUtils.sum(modelCol.workCellSizes);
      pw += getSizeGridV() * (modelCol.numCells - 1);

      //fix pw

      ph = IntUtils.sum(modelRow.workCellSizes);
      ph += getSizeGridH() * (modelRow.numCells - 1);
   }

   /**
    * <br>
    * <br>
    * What mechanism does not break the UI?
    * <br>
    * <br>
    * {@link TableView#getContentH()} is computed because {@link ViewDrawable#initViewDrawable(int, int)} has been called
    * to know if a ViewPane was needed.
    * <br>
    * <br>
    * In some instances for logical units, the number of frames is used instead of the number of columns.
    * <br>
    * SET,AVERAGE,STRONG gives 3 columns but 2 frames. A Frame is an aggregate of columns that form an single scrolling increment.
    * Frames are logical scrolling increments instead of cells. Case of Fixed Frame.
    * 
    * Variable Frame give SET,AVERAGE - AVERAGE - STRONG
    * <br>
    * Variable size frames for example with {@link IBOCellPolicy#CELL_4_FILL_AVERAGE}
    * <br>
    * <br>
    * Fixed Frames is for all pratical purposes equal to {@link ITechViewPane#SCROLL_TYPE_2_PAGE_UNIT}
    */
   public void initScrollingConfig(ScrollConfig scX, ScrollConfig scY) {
      if (scX != null) {
         if (modelCol.frames != null) {
            scX.setScrollUnit(ITechViewPane.SCROLL_TYPE_2_PAGE_UNIT);
            int sizeTotal = getContentW();
            int siTotal = modelCol.getScrollTotal();
            siTotal = modelCol.frames.length / 2;
            scX.initConfigLogic(sizeTotal, sizeTotal, siTotal, modelCol.sepSize);
         } else if (scX.getTypeUnit() == ITechViewPane.SCROLL_TYPE_1_LOGIC_UNIT) {
            int sizeTotal = getContentW();
            int siTotal = modelCol.getScrollTotal();
            int sepSize = modelCol.sepSize;
            if (modelCol.hasHelperFlag(CellModel.HELPER_FLAG_08_ALL_CELLS_SAME_SIZE)) {
               scX.initConfigLogic(modelCol.workCellSizes[0], sizeTotal, siTotal, sepSize);
            } else {
               scX.initConfigLogic(modelCol.workCellSizes, sizeTotal, siTotal, sepSize);
            }
         } else {
            DrawableUtilz.initConfigX(this, scX);
         }
      }
      if (scY != null) {
         if (modelRow.frames != null) {
            scY.setScrollUnit(ITechViewPane.SCROLL_TYPE_2_PAGE_UNIT);
            int sizeTotal = getContentH();
            int siTotal = modelRow.getScrollTotal();
            siTotal = modelRow.frames.length / 2;
            scY.initConfigLogic(sizeTotal, sizeTotal, siTotal, modelRow.sepSize);
         } else if (scY.getTypeUnit() == ITechViewPane.SCROLL_TYPE_1_LOGIC_UNIT) {
            int sizeTotal = getContentH();
            int siTotal = modelRow.getScrollTotal();
            int sepSize = modelRow.sepSize;
            if (modelRow.hasHelperFlag(CellModel.HELPER_FLAG_08_ALL_CELLS_SAME_SIZE)) {
               scY.initConfigLogic(modelRow.workCellSizes[0], sizeTotal, siTotal, sepSize);
            } else {
               scY.initConfigLogic(modelRow.workCellSizes, sizeTotal, siTotal, sepSize);
            }
         } else {
            DrawableUtilz.initConfigY(this, scY);
         }
      }
      //DrawableUtilz.debugScrollConfigs("TableView#initScrollingConfig", scX, scY);
   }

   protected void initViewDrawable(LayEngineDrawable ds) {
      //System.out.println("$TableView#initViewDrawable w=" + width + " h=" + height);
      checkNullities();
      setHelperFlags();

      int width = ds.getW();
      int height = ds.getH();
      initPreferredSizeFromCellSizes(width, height);

      //depending on the sizer, dw and dh

      //now compute the style size. how when sizing of padding/border/margin is contextual?
      //contextual on what? proportional to draw size or preferred size?

      if (ph == 0 || pw == 0) {
         // at this stage if pw or ph is zero we have an empty table.
         // the ViewDrawable will deal with it according to the tech params
      }
   }

   /**
    * Called when a ViewPort is created or changed
    */
   protected void initViewPortSub(int width, int height) {
      initPreferredSizeFromCellSizes(width, height);
   }

   /**
    * Method computes the current cell configuration {@link Config} and thus the preferred width and height.
    * <br>
    * <br>
    * Called by {@link ViewDrawable#init(int, int)} to initialize the Table<br>
    * <br>
    * <br>
    * When width is negative, the absolute value is the number of visible
    * columns. Drawable size is computed based on that single constraint. It
    * should only be called when all columns have the same width. <br>
    * Same with negative height values. <br>
    * <b>Examples</b> : <br>
    * <li>values of -1,-1 shows only 1 cell surronded with scrollbars 
    * <li>-2,100 shows 2 cells wide and a drawable height of 100 pixels
    * 
    * <br>
    * Implicit height or width imply {@link IViewPane#PLANET_MODE_0_EAT} is overriden to {@link IViewPane#PLANET_MODE_1_EXPAND}
    * 
    * <br>
    * When called by the {@link ViewPane}, 
    * <br>
    * <br>
    * What is the meaning of Flow Column and Init width of 0 or negative?
    * 
    * @param width
    * @param height
    */
   //   public void initViewDrawable(int width, int height) {
   //      //System.out.println("$TableView#initViewDrawable w=" + width + " h=" + height);
   //      checkNullities();
   //      setHelperFlags();
   //
   //      try {
   //         //first compute number of cells
   //         setupNumColRows();
   //
   //         // once number of cells is known
   //         //layout the space used by row numbers and column titles
   //         setupTitlesRow();
   //         setupTitlesCol();
   //
   //         // setupSizes();
   //         setupPolicyType2();
   //         // post: symbolized cell sizes are know.
   //
   //         // Config work size are taken from setupsize
   //         //initConfigWorkSizes(u);
   //         initConfigWorkSizes();
   //
   //         buildSpanning();
   //         buildGridIndexModel();
   //
   //         // compute one time for variable size cells. symbolized/variable sizes compute
   //         modelCol.computeSetupVariableCellSizes(getContentW());
   //         modelRow.computeSetupVariableCellSizes(getContentH());
   //         // once sizes are known
   //         // computeVisibleRowsInit(u, getContentH());
   //         // computeVisibleColsInit(u, getContentW());
   //
   //         // computeCellCoordinates(u);
   //         //compute span
   //         addSpanningToConfig();
   //         updateNavFlag();
   //
   //         if (hasTechF(T_FLAGF_1_SELECT_KEPT_WITHOUT_FOCUS_KEY)) {
   //            setSelectionState();
   //         }
   //      } catch (Exception e) {
   //         //#debug
   //         e.printStackTrace();
   //         //#debug
   //         throw new RuntimeException("In the TableView Compute Config method. Debug Stop");
   //      }
   //
   //      // computes the preferred sizes based on the configuration
   //      pw = IntUtils.sum(modelCol.workCellSizes) + MStyle.getStyleWConsumed(style);
   //      pw += getSizeGridV() * (modelCol.numCells - 1);
   //      ph = IntUtils.sum(modelRow.workCellSizes) + MStyle.getStyleHConsumed(style);
   //      ph += getSizeGridH() * (modelRow.numCells - 1);
   //      // at this stage if pw or ph is zero we have an empty table.
   //      // the ViewDrawable will deal with it according to the tech params
   //
   //      //TODO expanded is done with FILL policy. do we really need this?
   //      //      if (hasViewFlag(VIEW_GENE_31_EXPANDABLE_W)) {
   //      //         if (pw < width) {
   //      //            //redistribute width pixel to columns
   //      //            pw = width;
   //      //            setViewFlag(VIEWSTATE_18_EXPANDED_W, true);
   //      //         }
   //      //      }
   //      //setDwDh(width, height);
   //   }

   /**
    * Within this {@link Drawable}, what are the drawable that intersect with given area
    * <br>
    * Those {@link Drawables} will be repainted.
    * @param x
    * @param y
    * @param w
    * @param h
    * @return
    */
   public IDrawable[] intersection(int x, int y, int w, int h) {
      return null;
   }

   /**
    * Check {@link ITechDrawable#BEHAVIOR_01_NOT_SELECTABLE} then using policy selectability and model selectability
    * 
    * Equivalent to SelectionModel in Swing
    * @param d
    * @param colAbs
    * @param rowAbs
    * @return
    */
   public boolean isCellDrawableSelectable(IDrawable d, int colAbs, int rowAbs) {
      if (hasHelperFlag(HELPER_FLAG_29_UNSELECTABLE_STUFF)) {
         if (d != null) {
            if (d.hasBehavior(ITechDrawable.BEHAVIOR_01_NOT_SELECTABLE)) {
               return false;
            }
         }
         if (hasHelperFlag(HELPER_FLAG_26_UNSELECTABLE_CELLS)) {
            return model.getPropertyInt(ITableModel.PROPERTY_1_SELECTABLE, getGridIndexFromAbs(colAbs, rowAbs)) == 1;
         }
         if (modelCol.hasHelperFlag(CellModel.HELPER_FLAG_27_UNSELECTABLE)) {
            //column model
            return modelCol.flags[colAbs] == 1;
         }
         if (modelRow.hasHelperFlag(CellModel.HELPER_FLAG_27_UNSELECTABLE)) {
            return modelRow.flags[rowAbs] == 1;
         }
      }
      return true;
   }

   /**
    * Checks the model based selectability of a cell.
    * <br>
    * <br>
    * Reads {@link ITableModel#getPropertyInt(int, int)} with {@link ITableModel#PROPERTY_1_SELECTABLE}.
    * <br>
    * <br>
    * @param colAbs
    * @param rowAbs
    * @return
    */
   public boolean isCellSelectable(int colAbs, int rowAbs) {
      IDrawable d = getModelDrawable(colAbs, rowAbs);
      return isCellDrawableSelectable(d, colAbs, rowAbs);
   }

   protected boolean isFullInsideTransition() {
      return modelCol.transitionType == Transition.TRANSITION_0INSIDE_VIEWPORT && modelRow.transitionType == Transition.TRANSITION_0INSIDE_VIEWPORT;
   }

   public boolean isSelectedCell(int colAbs, int rowAbs) {
      return (getSelectedCol() == colAbs && getSelectedRow() == rowAbs);
   }

   private boolean isSelectStateRemovedWithKeyFocus() {
      return !hasTechF(T_FLAGF_1_SELECT_KEPT_WITHOUT_FOCUS_KEY);
   }

   /**
    * Manages Navigational keys itself via the {@link Controller}.
    * <br>
    * <br>
    * <br>
    */
   public void manageKeyInput(InputConfig ic) {
      super.manageKeyInput(ic);
   }

   public void managePointerEvent(IDrawable slave, InputConfig ic) {
      if (slave == modelCol.titlesView) {
         //we have an event on the column titles.
         //all events going to a title, first go through container. (top-down)
         //ask the CellModel to forward to pointer input to the right title
         if (modelCol.managePointerInputForward(ic)) {
            return;
         }
         //TODO if u want to sort.. get the column index
      } else if (slave == modelRow.titlesView) {
         if (modelRow.managePointerInputForward(ic)) {
            return;
         }
      } else if (modelRow.titlesView != null && DrawableUtilz.isInside(ic, modelRow.titlesView)) {
         //press event first goes viewpane/title container/tableview slave/title/tableview now here
         //#debug
         toDLog().pEvent("Row Title Slaved Event '" + ic.toStringMod() + "' " + slave.toString1Line(), this, TableView.class, "managePointerEvent", LVL_05_FINE, true);
      } else if (modelCol.titlesView != null && DrawableUtilz.isInside(ic, modelCol.titlesView)) {
         //row title event
         //#debug
         toDLog().pEvent("Column Title Slaved Event", this, TableView.class, "managePointerEvent", LVL_05_FINE, true);
         ic.srActionDoneRepaint(modelCol.titlesView);
         //forward it
      } else {
         //
         managePointerInputSlaveCell(ic, slave);
         //return from others
         //            if (ic.isPressed()) {
         //               movePointerPressed(ic);
         //            } else if (ic.isMoved() && hasTechX(T_FLAGX_3_ROW_SELECTION)) {
         //               setRowStyle(modelRow.pointedIndexAbs, STYLE_07_FOCUSED_POINTER, true);
         //            } else if (ic.isReleased()) {
         //               if (hasTechX(T_FLAGX_3_ROW_SELECTION)) {
         //                  setRowStyle(modelRow.pointedIndexAbs, STYLE_07_FOCUSED_POINTER, false);
         //               }
         //            }
      }
   }

   /**
    * Override this and then call super so that the slave gets a chance to update its state styles
    * <br>
    * <br>
    * managePointerInputSlaveCell
    * @param ic
    * @param slave
    * 
    * @see Drawable
    */
   protected void managePointerInputSlaveCell(InputConfig ic, IDrawable slave) {
      //toLog().println("#TableView#managePointerInputSlave Event sent to Slave = " + slave.toStringOneLine());
      //we don't do anything? but maybe sub class will do something on it
      slave.managePointerStateStyle(ic);
      // 2 march 2014. we add this so
      //      if (ic.isMoved()) {
      //         //move inside viewport. first pass.
      //         managePointerMoved(ic);
      //      } else {
      //         managePointerInputSelection(ic);
      //      }
      //      
   }

   public void manageNavigate(CmdInstanceDrawable cd, int navEvent) {
      if (navEvent == INavTech.NAV_1_UP) {
         navigateUp(cd.getIC());
      } else if (navEvent == INavTech.NAV_5_SELECT) {

      }
   }

   /**
    * Called by {@link ViewPane} when pointer event is inside the ViewPort area.
    * <br>
    * <br>
    * 2 pass method. 
    * <li>1st time slave drawable is null.
    * <li>2nd time, if the slave is not null, calls {@link TableView#managePointerInputSlave(InputConfig)}
    */
   public void managePointerInputViewPort(InputConfig ic) {
      if (techTable.hasFlag(IBOTableView.T_OFFSET_02_FLAGX, IBOTableView.T_FLAGX_7_VIEW_PORT_BLIND)) {
         return;
      }
      //#debug
      //toLog().printFlow("#TableView#managePointerInputViewPort " + ((ic.getSlaveDrawable() != null) ? ic.getSlaveDrawable().toStringOneLine() : " Slave=Null"));
      //call back by titles
      if (ic.isWheeled()) {

         //#debug
         toDLog().pFlow("Wheeled " + ToStringStaticCoreUi.getStringMouseButton(ic.getIdKeyBut()), this, TableView.class, "managePointerInputViewPort", LVL_05_FINE, true);
         if (ic.getIdKeyBut() == IBCodes.PBUTTON_3_WHEEL_UP) {
            if (modelRow.numCells == 1) {
               navigateLeft(ic);
            } else {
               navigateUp(ic);
            }
         } else {
            if (modelRow.numCells == 1) {
               navigateRight(ic);
            } else {
               navigateDown(ic);
            }
         }
         return;
      }

      //deletage to ViewDrawable to redirect
      if (ic.isMoved()) {
         //move inside viewport. first pass.
         managePointerViewMoved(ic);
      } else {
         if (ic.isPressed()) {
            managePointerViewPressed(ic);
         } else if (ic.isReleased()) {
            managePointerViewReleased(ic);
         } else if (ic.isDraggedPointer0Button0()) {
            managePointerViewDragged(ic);
         }
      }
      //separates pointer gesture from child event
      //first manage the scrolling gesture
      super.managePointerGesture(ic);

   }

   private void managePointerViewDragged(InputConfig ic) {
      // TODO Auto-generated method stub

   }

   /**
    * Manage the state style {@link ITechDrawable#STYLE_07_FOCUSED_POINTER}
    * <br>
    * <br>
    * Redirect from {@link IDrawable#managePointerInput(InputConfig)}.
    * <br>
    * <br>
    * @param ic
    */
   private void managePointerViewMoved(InputConfig ic) {
      int[] pointed = getPointerDrawableCoordinates(ic);
      if (pointed != null) {
         int i = pointed[0];
         int j = pointed[1];
         if (hasTechX(T_FLAGX_3_ROW_SELECTION)) {
            if (modelRow.pointedIndexAbs != j) {
               setRowStyle(modelRow.pointedIndexAbs, ITechDrawable.STYLE_07_FOCUSED_POINTER, false);
            }
         }

         modelCol.pointedIndexAbs = i;
         modelRow.pointedIndexAbs = j;
         IDrawable od = getModelDrawable(i, j);
         //check if index is movable? how?

         //if not selectable, still remove focus from old guy and give it to table

         if (hasTechX(T_FLAGX_3_ROW_SELECTION)) {
            //set move style to the whole row
            setRowStyle(modelRow.pointedIndexAbs, ITechDrawable.STYLE_07_FOCUSED_POINTER, true);
         } else {
            if (od != null) {
               //#debug
               toDLog().pFlow("Pointer to " + od.toString1Line(), this, TableView.class, "managePointerViewMoved", LVL_05_FINE, true);

               //the drawable will set the states by itself
               od.managePointerInput(ic);
            }
         }
      } else {
         //when pointer is not over a drawable, give focus back to TableView
         gc.getFocusCtrl().newFocusPointerPress(ic, this);
      }
   }

   /**
    * Selection routine to decide to forward {@link IBOTableView#EVENT_ID_00_SELECT} event.
    * <br>
    * <br>
    * In all cases, we need the Pressed Drawable in order to forward the state updates.
    * <br>
    * <br>
    * Detect which is inside or outside.
    * <br>
    * @param ic
    */
   protected void managePointerViewPressed(InputConfig ic) {
      //#debug
      toDLog().pFlow("", techTable, TableView.class, "managePointerViewPressed", LVL_05_FINE, true);

      int type = techTable.get1(T_OFFSET_12_PSELECTION_MODE1);
      if (type == PSELECT_0_NONE) {
         forwardPointerEvent(ic);
      } else if (type == PSELECT_1_PRESS) {
         forwardPointerSelectEvent(ic);
      } else if (type == PSELECT_2_PRESS_RELEASE) {
         forwardPointerEvent(ic);
      } else if (type == PSELECT_3_SELECTED_PRESS) {
         forwardPointerSelectPress(ic);
      } else if (type == PSELECT_4_PRESS_DOUBLE) {
         if (ic.isPressedDouble()) {
            forwardPointerSelectEvent(ic);
         } else {
            forwardPointerEvent(ic);
         }
      } else if (type == PSELECT_5_PRESS_RELEASE_DOUBLE) {
         if (ic.isPressedReleasedDouble()) {
            forwardPointerSelectEvent(ic);
         } else {
            forwardPointerEvent(ic);
         }
      }
   }

   public void managePointerViewReleased(InputConfig ic) {

      int type = techTable.get1(T_OFFSET_12_PSELECTION_MODE1);
      if (type == PSELECT_0_NONE) {
         forwardPointerEvent(ic);
      } else if (type == PSELECT_1_PRESS) {
         //dealt with in the other method
      } else if (type == PSELECT_2_PRESS_RELEASE) {
         //finds which drawable is located under the release event
         int[] pointed = getPointerDrawableCoordinates(ic);
         if (pointed != null) {
            int i = pointed[0];
            int j = pointed[1];
            IDrawable d = getModelDrawable(i, j);
            //TODO... check this. for press releases.. maybe use more 
            //check if release is done on the same drawable as the press event
            //however if the release is done after a drag, no selection
            GestureDetector gd = ic.is.getOrCreateGesture(d);
            if (!ic.isDraggedDragged()) {
               boolean isSelectable = isCellDrawableSelectable(d, i, j);
               if (isSelectable) {
                  forwardPointerSelectEventSub(ic, d, i, j);
               }
            }
            // pointer event as slave. manage the internal drawable states.
            d.managePointerInput(ic);
         }
      } else if (type == PSELECT_3_SELECTED_PRESS) {
      } else if (type == PSELECT_4_PRESS_DOUBLE) {
      } else if (type == PSELECT_5_PRESS_RELEASE_DOUBLE) {
         if (ic.isPressedReleasedDouble()) {
            forwardPointerSelectEvent(ic);
         }
      }
   }

   /**
    * Matches the {@link CellModel} and the {@link ScrollConfig} together.
    * <br>
    * The match is done just before drawing by {@link TableView#drawViewDrawableContentFixed(GraphicsX, int, int, ScrollConfig, ScrollConfig)}
    * @param cm
    * @param sc
    */
   private void matchModel(CellModel cm, ScrollConfig sc) {
      if (sc == null) {
         cm.callMatchNull();
      } else {
         cm.callMatching(sc);
      }
      //toLog().printState("#TableView#matchModel after the match " + cm.toStringOneLine() + ((sc == null) ? "null " : sc.toString1Line()));
   }

   /**
    * In most configurations, the {@link TableView} navigates key Navs on its own.
    * <br>
    * <br>
    * This is decided by {@link CellModel#HELPER_FLAG_10_OWN_NAVIGATION}.
    * <br>
    * Apply selectability
    * <br> 
    * <li> Logic scrolling
    * Otherwise, delegate to the {@link ViewPane} if any.
    * <br>
    * When 
    */
   public void navigateDown(InputConfig ic) {
      if (ic.isPressed() || ic.isWheeled()) {
         // table has its own navigation
         if (modelRow.hasHelperFlag(CellModel.HELPER_FLAG_10_OWN_NAVIGATION)) {
            int nextRow = getNextSelectableDown();
            if (nextRow != modelRow.selectedCellAbs) {
               selectionMove(ic, modelCol.selectedCellAbs, nextRow);
            }
         }
      }
   }

   /**
    * Tries to navigate. it may fail because
    * <li> No navi
    * <li> reached the end of table and no circle flag
    * 
    * @param cd
    */
   public void navigateDown(CmdInstanceDrawable cd) {
      // table has its own navigation
      if (modelRow.hasHelperFlag(CellModel.HELPER_FLAG_10_OWN_NAVIGATION)) {
         int nextRow = getNextSelectableDown();
         if (nextRow != modelRow.selectedCellAbs) {
            selectionMove(cd.getIC(), modelCol.selectedCellAbs, nextRow);
         }
      }
   }

   /**
    * 
    * When for example, there is only one column selectable, delegates to parent of TableView
    */
   public void navigateLeft(InputConfig ic) {
      if (ic.isPressed()) {
         if (modelCol.hasHelperFlag(CellModel.HELPER_FLAG_10_OWN_NAVIGATION)) {
            int nextCol = getNextSelectableLeft();
            if (nextCol != modelCol.selectedCellAbs) {
               selectionMove(ic, nextCol, modelRow.selectedCellAbs);
            }
         }
      }
   }

   public void navigateLeft(CmdInstanceDrawable cd) {
      if (modelCol.hasHelperFlag(CellModel.HELPER_FLAG_10_OWN_NAVIGATION)) {
         int nextCol = getNextSelectableLeft();
         if (nextCol != modelCol.selectedCellAbs) {
            selectionMove(cd.getIC(), nextCol, modelRow.selectedCellAbs);
         }
      }
   }

   public void navigateRight(CmdInstanceDrawable cd) {
      if (modelCol.hasHelperFlag(CellModel.HELPER_FLAG_10_OWN_NAVIGATION)) {
         int nextCol = getNextSelectableRight();
         if (nextCol != modelCol.selectedCellAbs) {
            selectionMove(cd.getIC(), nextCol, modelRow.selectedCellAbs);
         }
      }
   }

   /**
    * 
    */
   public void navigateRight(InputConfig ic) {
      if (ic.isPressed()) {
         if (modelCol.hasHelperFlag(CellModel.HELPER_FLAG_10_OWN_NAVIGATION)) {
            int nextCol = getNextSelectableRight();
            if (nextCol != modelCol.selectedCellAbs) {
               selectionMove(ic, nextCol, modelRow.selectedCellAbs);
            }
         }
      }
   }

   /**
    * Select Command is generated. 
    * <br>
    * <br>
    * Depending on state
    * <li> If Cell is already selected, selection event is fired.
    * <li> When auto select setting, key/pointer focus is given to.
    * <br>
    * Depends on the type of navigation.
    *  
    * @param ic {@link InputConfig} might be in pressed mode or release or continuous
    */
   public void navigateSelect(InputConfig ic) {
      if (ic.isPressed()) {
         // notify on input config?
         selectionSelectEvent(ic);
      }
   }

   /**
    * Navigates on step up if possible.
    * <br>
    * ViewPane navigation will be automatic
    */
   public void navigateUp(InputConfig ic) {
      if (ic.isPressed() || ic.isWheeled()) {
         if (modelRow.hasHelperFlag(CellModel.HELPER_FLAG_10_OWN_NAVIGATION)) {
            int nextRow = getNextSelectableUp();
            if (nextRow != modelRow.selectedCellAbs) {
               selectionMove(ic, modelCol.selectedCellAbs, nextRow);
            }
         }
      }
   }

   public void navigateUp(CmdInstanceDrawable cd) {
      if (modelRow.hasHelperFlag(CellModel.HELPER_FLAG_10_OWN_NAVIGATION)) {
         int nextRow = getNextSelectableUp();
         if (nextRow != modelRow.selectedCellAbs) {
            InputConfig ic = cd.getIC();
            selectionMove(ic, modelCol.selectedCellAbs, nextRow);
         }
      }
   }

   public void notifyEvent(IDrawable d, int event, Object o) {
      if (event == ITechDrawable.EVENT_14_POINTER_EVENT) {
         managePointerEvent(d, (InputConfig) o);
      }
   }

   /**
    * When Selected {@link ITechDrawable#STYLE_05_SELECTED} depends on Focus,
    * <br>
    * <br>
    * Always makes sure the {@link ITechDrawable#STYLE_05_SELECTED} flag is set on the Drawable.
    * When it is the first time a {@link TableView} gets the focus, if selection behavior, it sets the
    * selected state.
    */
   public void notifyEventKeyFocusGain(BusEvent e) {
      super.notifyEventKeyFocusGain(e);
      if (hasSelectionBehavior()) {
         if (!hasTechF(T_FLAGF_1_SELECT_KEPT_WITHOUT_FOCUS_KEY)) {
            IDrawable d = getSelectedDrawable();
            if (d != null) {
               d.setStateStyle(ITechDrawable.STYLE_05_SELECTED, true);
            }
         }
      }
   }

   /**
    * If Tech Requires it, removes selected state from selected cell {@link Drawable}.
    * <br>
    * <br>
    * test 4 tables focus change mechanism relative to focus of table cells.
    */
   public void notifyEventKeyFocusLost(BusEvent e) {
      super.notifyEventKeyFocusLost(e);
      if (hasSelectionBehavior()) {
         boolean isNewChildren = DrawableUtilz.isUpInFamily((IDrawable) e.getParamO1(), this);
         if (!isNewChildren) {
            //only deselect when focus goes to another branch.
            if (isSelectStateRemovedWithKeyFocus()) {
               IDrawable d = getSelectedDrawable();
               if (d != null) {
                  d.setStateStyle(ITechDrawable.STYLE_05_SELECTED, false);
               }
            }
         }
      }
   }

   /**
    * Add the cell to the SelectionModel
    * @param ic
    * @param colAbs
    * @param rowAbs
    */
   public void selectionAdd(InputConfig ic, int colAbs, int rowAbs) {

   }

   /**
    * Moves the {@link ITechDrawable#STYLE_05_SELECTED} to the new drawable.
    * <br>
    * <br>
    * 
    * @param ic
    * @param od
    * @param nd can be null.
    */
   protected void selectionChangeSelectedState(InputConfig ic, IDrawable od, IDrawable nd, boolean giveFocus) {
      if (hasTechX(T_FLAGX_3_ROW_SELECTION)) {
         if (hasTechX(T_FLAGX_4_ROW_SELECTION_STYLE_STATES)) {
            setRowStyle(modelRow.oldSelectedCellAbs, ITechDrawable.STYLE_05_SELECTED, false);
         }
      } else {
         od.setStateStyle(ITechDrawable.STYLE_05_SELECTED, false);
      }

      if (nd == null) {
         if (hasTechX(T_FLAGX_8_SELECT_EMPTY_CELLS)) {
            nd = emptyBluePrint;
         } else {
            //no selection. get to next in line cell
         }
      }
      if (hasTechX(T_FLAGX_3_ROW_SELECTION)) {
         if (hasTechX(T_FLAGX_4_ROW_SELECTION_STYLE_STATES)) {
            setRowStyle(modelRow.selectedCellAbs, ITechDrawable.STYLE_05_SELECTED, true);
         }
      } else {
         if (nd != null) {
            nd.setStateStyle(ITechDrawable.STYLE_05_SELECTED, true);
            if (giveFocus) {
               gc.getFocusCtrl().newFocusKey(ic, nd);
            }
         }
      }
   }

   /**
    * Moves some states to new cell defined by colAbs and rowAbs. Nothing happens if cell is not selectable.
    * <br>
    * <br>
    * Does not deal with Navigation options.
    * Usually moves:
    * <li>{@link ITechDrawable#STYLE_05_SELECTED}
    * <li>{@link ITechDrawable#STYLE_06_FOCUSED_KEY}
    * <br>
    * <br>
    * It is dependant on the navigationOut process for navigation flags AroundTheClock
    * <br>
    * <br>
    * The method modifies the {@link Config}, makes it current, thus influencing {@link ScrollConfig}, {@link ScrollBar} and {@link ViewPane}.
    * <br>
    * <br>
    * By default, Spanned cells behave like a single cell. Selection move relative to span root.
    * <br>
    * <br>
    * TODO When key move changes the {@link ScrollConfig}, animates the block of the scrollbar smoothly when the pixel amplitude is big.
    * <br>
    * <br>
    * When move is made inside the ViewPort, new and old cells are repainted.
    * <br>
    * <br>
    * TODO when inside selection moves outside visible cells, calls {@link ViewPane#moveX(int)} move methods. 
    * for InputConfig
    * <br>
    * <br>
    * When moving the selection programmatically, one may not want to give the focus
    * <br>
    * <br>
    * @param ic {@link InputConfig}. Cannot be null even when it is programmatically moved. 
    * @param colAbs caller is responsible to select a selectable cell
    * @param rowAbs are cells pre-checked?
    * 
    */
   public void selectionMove(InputConfig ic, int colAbs, int rowAbs) {
      boolean giveFocus = !hasTechF(T_FLAGF_2_SELECT_MOVE_NOT_GIVES_FOCUS);
      selectionMove(ic, colAbs, rowAbs, giveFocus);
   }

   /**
    * Does it make sense to move to a null Drawable? Don't we want
    * the selection to go to the next non null Drawable.
    * <br>
    * returns silently when col and rows are out of bounds
    * <br>
    * @param ic
    * @param colAbs
    * @param rowAbs
    * @param giveFocus
    */
   public void selectionMove(InputConfig ic, int colAbs, int rowAbs, boolean giveFocus) {
      //System.out.println("#TableView moveSelection to col=" + colAbs + " row=" + rowAbs);
      if (ic == null) {
         throw new NullPointerException("Null InputConfig");
      }
      // only do the move if the destination cell is valid
      if (rowAbs < 0 || rowAbs > modelRow.numCells) {
         return;
      }
      if (colAbs < 0 || colAbs > modelCol.numCells) {
         return;
      }

      //check if cell is selectable. if not return false

      modelCol.setNewSelection(colAbs);
      modelRow.setNewSelection(rowAbs);

      //#debug
      toDLog().pState("Transition " + TableSelectionModel.debugType(modelCol.transitionType) + "-" + TableSelectionModel.debugType(modelRow.transitionType), this, TableView.class, "selectionMove", LVL_05_FINE, true);

      // make it current

      //tabledrawindex set full repaint, but not stupid. 
      //helper flag to do a index based repaint.
      if (hasHelperFlag(HELPER_FLAG_20_SPECIAL_REPAINT)) {
         if (isFullInsideTransition()) {
            int visualIndex1 = getVisualIndex(modelCol.oldSelectedCellAbs, modelRow.oldSelectedCellAbs);
            int visualIndex2 = getVisualIndex(modelCol.selectedCellAbs, modelRow.selectedCellAbs);
            indexRepaints = new int[] { visualIndex1, visualIndex2 };
            //#debug
            toDLog().pFlow("Special Repaint for Indexes: " + visualIndex1 + " and " + visualIndex2, this, TableView.class, "selectionMove", LVL_05_FINE, true);
            ic.srActionDoneRepaintSpecial(this);
         }
      } else {
         //TODO ask Index if it can be repainted. For Drawables it is easy.
         //for TableDrawIndex makes a special repaint on Index, not Drawables
         //modeless table will return null, or when model has a null value, it must return print
         IDrawable od = getModelDrawable(modelCol.oldSelectedCellAbs, modelRow.oldSelectedCellAbs);
         IDrawable nd = getModelDrawable(modelCol.selectedCellAbs, modelRow.selectedCellAbs);

         // change drawable states. if conditions applies, only repaint those two drawables
         if (od == null) {
            od = emptyBluePrint;
         }
         if (nd == null) {
            nd = emptyBluePrint;
         }
         //unselect, style falls back to previous
         //         if (hasTechF(T_FLAGF_2SELECT_MOVE_GIVES_FOCUS)) {
         //            getMe().focusLost(od);
         //         }

         selectionChangeSelectedState(ic, od, nd, giveFocus);

         if (isFullInsideTransition()) {
            //ask InputConfig to repaint 
            if (od.hasState(ITechDrawable.STATE_04_DRAWN_OUTSIDE) || nd.hasState(ITechDrawable.STATE_04_DRAWN_OUTSIDE)) {
               // repaint the whole thing or just the inside?
               //TODO must be able to compute extra size of style layers because you may have to repaint a scrollbar/headers as well.
               setViewFlag(ITechViewDrawable.VIEWSTATE_02_REPAINTING_CONTENT, true);
               ic.srActionDoneRepaint(this);
            } else {
               // if it is not opaque. it will have to repaint bg layers and fg layers of parent drawable.
               ic.srActionDoneRepaint(od);
               ic.srActionDoneRepaint(nd);
            }
         } else {
            setViewFlag(ITechViewDrawable.VIEWSTATE_02_REPAINTING_CONTENT, true);
            ic.srActionDoneRepaint(this);
         }
      }

      //in all cases, when not inside fully inside the ViewPort,
      //propagate the change to the ViewPane and its ScrollingConfig. that may generate an animation
      if (!isFullInsideTransition() && viewPane != null) {
         //Here we think in terms of moves. but why can't be just set the scrollconfig increments? and master variable?
         //what happens if the model is modified while doing a repaint?
         //variable cells
         int changeX = 0;
         int changeY = 0;
         if (modelCol.transitionType != CellModel.TRANSITION_0INSIDE_VIEWPORT) {
            changeX = modelCol.doUpdate(viewPane.getSChorizontal(), getContentW());
            if (changeX != 0) {
               //move the scroll config vx increments
               viewPane.moveX(ic, changeX);
            }
         }
         if (modelRow.transitionType != CellModel.TRANSITION_0INSIDE_VIEWPORT) {
            changeY = modelRow.doUpdate(viewPane.getSCvertical(), getContentH());
            if (changeY != 0) {
               //move the scroll config vx increments
               viewPane.moveY(ic, changeY);
            }
         }
         ic.srActionDoneRepaint(this);
         //#debug
         toDLog().pState("NotFullyInsideViewPort Transition Change=" + changeX + "," + changeY, this, TableView.class, "selectionMove", LVL_05_FINE, true);
      }

      //TODO animate transition of the FG layer
      if (techTable.hasFlag(IBOTableView.T_OFFSET_01_FLAG, IBOTableView.T_FLAG_8_STYLE_ANIMATION)) {

      }
      selectionMoveEvent(ic);
   }

   protected void selectionMoveEvent(InputConfig ic) {
      gc.getEventsBusGui().sendNewEvent(producerID, EVENT_ID_01_SELECTION_CHANGE, this);
   }

   /**
    * Method calledd when selected cell is "selected" by means of the keyboard or the pointer. 
    * <br>
    * <br>
    * Depending on key assignments and tech pointer behavior. Keyboard usually uses the ENTER key.
    * <br>
    * <br>
    * @param ic
    */
   protected void selectionSelectEvent(InputConfig ic) {
      //#debug
      toDLog().pEvent("SelectedCell=[" + getSelectedCol() + "," + getSelectedRow() + "] =" + toStringSelectedDrawable(), this, TableView.class, "selectionSelectEvent");
      BusEvent be = gc.getEventsBusGui().createEvent(producerID, EVENT_ID_00_SELECT, this);
      be.setParamO1(eventParamO);
      be.putOnBus();
   }

   /**
    * Setting the model nullifies
    * @param model
    */
   public void setDataModel(ITableModel model) {
      this.model = model;
      if (mapperObjectDrawable != null) {
         mapperObjectDrawable.clearCache();
      }
      aInitModel();
   }

   private void setFlowSingleSize(CellModel cm, int totalSize, int sepSize, int size) {
      int divisor = size + sepSize;
      // when divisor is small, the number of column may be too much
      int numTotalCells = IntUtils.divideFloor(totalSize, divisor);
      if (numTotalCells == 0) {
         numTotalCells = 1;
      }
      cm.numCells = numTotalCells;
      cm.setupSizes = new int[] { size };
      cm.policies = new int[] { CELL_1_EXPLICIT_SET };
   }

   public void setHelperFlag(int flag, boolean v) {
      helpers = BitUtils.setFlag(helpers, flag, v);
   }

   /**
   * 
   */
   public void setHelperFlags() {
      //      if (viewPane != null) {
      //         if (viewPane.getSChorizontal() != null) {
      //            if (policyTable.get1(TP_OFFSET_05WDRAW1) == TP_DRAW_3FILL || policyTable.hasFlag(TP_OFFSET_03FLAGP1, TP_FLAGP_5VARIABLE_COLSIZES)) {
      //               setHelperFlag(HELPER_FLAG_01VARIABLE_COLSIZES, true);
      //            }
      //         }
      //         if (viewPane.getSCvertical() != null) {
      //            if (policyTable.get1(TP_OFFSET_06HDRAW1) == TP_DRAW_3FILL || policyTable.hasFlag(TP_OFFSET_03FLAGP1, TP_FLAGP_6VARIABLE_ROWSIZES)) {
      //               setHelperFlag(HELPER_FLAG_02VARIABLE_ROWSIZES, true);
      //            }
      //         }
      //      }
      if (hasTechX(T_FLAGX_1_NO_SELECTION)) {
         modelRow.setHelperFlag(CellModel.HELPER_FLAG_10_OWN_NAVIGATION, false);
         modelCol.setHelperFlag(CellModel.HELPER_FLAG_10_OWN_NAVIGATION, false);
      } else {
         modelRow.setHelperFlag(CellModel.HELPER_FLAG_10_OWN_NAVIGATION, true);
         boolean isRowSelection = hasTechX(IBOTableView.T_FLAGX_3_ROW_SELECTION);
         modelCol.setHelperFlag(CellModel.HELPER_FLAG_10_OWN_NAVIGATION, !isRowSelection);
      }
      // this.setViewFlag(ViewDrawable.VIEWSTATE_1_CONTENT_WIDTH_FOLLOWS_VIEWPORT,
      // hasHelperFlag(HELPER_FLAG_01VARIABLE_COLSIZES));
      // this.setViewFlag(ViewDrawable.VIEWSTATE_2_CONTENT_HEIGHT_FOLLOWS_VIEWPORT,
      // hasHelperFlag(HELPER_FLAG_02VARIABLE_ROWSIZES));

   }

   public void setMapper(DrawableMapper mapper) {
      if (mapper != null) {
         this.mapperObjectDrawable = mapper;
      }
   }

   public void setModelLength(int modelLength) {
      modelLen = modelLength;
      if (modelLen != 0 || modelOffset != 0) {
         setHelperFlag(HELPER_FLAG_06_MODEL_MASK, true);
      } else {
         setHelperFlag(HELPER_FLAG_06_MODEL_MASK, false);
      }

   }

   /**
    * Offset at which TableView starts reading.
    * <br>
    * More generally, mask the first ith element in the model. Visually they are ignored.
    * @param i
    */
   public void setModelOffset(int i) {
      modelOffset = i;
      if (modelLen != 0 || modelOffset != 0) {
         setHelperFlag(HELPER_FLAG_06_MODEL_MASK, true);
      } else {
         setHelperFlag(HELPER_FLAG_06_MODEL_MASK, false);
      }
   }

   protected void setParentLink(IDrawable item) {
      item.setParent(this);
      item.setBehaviorFlag(ITechDrawable.BEHAVIOR_23_PARENT_EVENT_CONTROL, true);
   }

   public void setRowStyle(int rowAbs, int flag, boolean v) {
      for (int i = 0; i < modelCol.numCells; i++) {
         IDrawable d = getModelDrawable(i, rowAbs);
         if (d != null) {
            d.setStateStyle(flag, v);
         }
      }
   }

   public void setSelectedFlags(boolean[] bols) {

   }

   /**
    * Change the selection index without changing or sending further events.
    * <br>
    * <br>
    * 
    * @param sel
    */
   public void setSelectedIndex(int index) {
      int colAbs = getAbsColFromIndex(index);
      int rowAbs = getAbsRowFromIndex(index);
      modelCol.selectedCellAbs = colAbs;
      modelRow.selectedCellAbs = rowAbs;
   }

   /**
    * Programmatically generates a {@link TableView#selectionMove(InputConfig, int, int)} as if the user
    * did it. Thus generates a selection event. 
    * <br>
    * If index is invalid, nothing happens
    * <br>
    * Gets a write lock
    * <br>
    * @param index
    * @param ic
    * @param giveFocus
    */
   public void setSelectedIndex(int index, InputConfig ic, boolean giveFocus) {
      //acquire write lock
      int colAbs = getAbsColFromIndex(index);
      int rowAbs = getAbsRowFromIndex(index);
      selectionMove(ic, colAbs, rowAbs, giveFocus);
      //release lock
   }

   /**
    * Sets without moving animations.
    * Gives the category focus?
    * @param colAbs
    * @param rowAbs
    */
   public void setSelectedIndex(int colAbs, int rowAbs, ExecutionContext ex) {
      //acquire write lock
      //selectionMove(ic, colAbs, rowAbs, giveFocus);
      //release lock
      throw new RuntimeException();
   }

   /**
    * Sets the {@link ITechDrawable#STYLE_05_SELECTED} flags on {@link IDrawable}s that are model selected.
    * <li> {@link CellModel#selectedCellAbs}
    */
   public void setSelectionState() {
      if (hasSelectionBehavior()) {
         IDrawable d = getSelectedDrawable();
         if (d != null) {
            d.setStateStyle(ITechDrawable.STYLE_05_SELECTED, true);
         }
      }
   }

   /**
    * Sets the type of selection. IMPLICIT or MULTIPLE.
    * <br>
    * <br>
    * that's the View business. is it? Model is also apply it to the model
    * <br>
    * <br>
    * The old MIDP choice was EXPLICIT, MULTIPLE, IMPLICIT
    * <br>
    * <li>{@link ITableModel#SELECTION_0_IMPLICIT}
    * <li>{@link ITableModel#SELECTION_1_EXCLUSIVE}
    * <li>{@link ITableModel#SELECTION_2_MULTIPLE}
    * 
    * @param selType
    */
   public void setSelectionType(int selType) {
      // TODO Auto-generated method stub

   }

   /**
    * Propagates flag change to the {@link ViewPane} and its {@link ViewPane#setStateFlag(int, boolean)} method.
    * <li> {@link ITechDrawable#STATE_03_HIDDEN}.
    * <li> {@link ITechDrawable#STATE_02_DRAWN}. 
    */
   public void setStateFlag(int flag, boolean value) {
      if (flag == ITechDrawable.STATE_03_HIDDEN || flag == ITechDrawable.STATE_02_DRAWN) {
         int firstRowAbs = modelRow.firstCellAbs;
         int lastRowAbs = modelRow.lastCellAbs;
         int firstColAbs = modelCol.firstCellAbs;
         int lastColAbs = modelCol.lastCellAbs;
         for (int j = firstRowAbs; j <= lastRowAbs; j++) {
            for (int i = firstColAbs; i <= lastColAbs; i++) {
               IDrawable d = getModelDrawable(i, j);
               if (d != null) {
                  d.setStateFlag(flag, value);
               }
            }
         }
      }
      super.setStateFlag(flag, value);
   }

   /**
    * Gives the correct style key to the Drawable. <br>
    * PRE: numTotalCols has been computed > 1
    * 
    * @param d
    * @param index
    */
   protected void setStructuralStyleKey(IDrawable d, int index) {
      // System.out.println(index);
      int col = getAbsColFromIndex(index);
      int row = getAbsRowFromIndex(index);
      setStructuralStyleKey(d, col, row);
   }

   /**
    * Sets Table Structural Style to the {@link IDrawListener}.
    * 
    * <br>
    * <br>
    * Rows and Columns may have a specific styles.
    * <br>
    * <br>
    * This feature is used to implement cyclical row colors.
    * <br>
    * <br>
    * @param d
    * @param colAbs used to identify the column style if any.
    * @param rowAbs used to identify the row style, if any.
    */
   protected void setStructuralStyleKey(IDrawable d, int colAbs, int rowAbs) {
      if (d == null)
         return;
      ByteObject structStyle = null;
      if (modelCol.styles != null) {
         structStyle = modelCol.styles[colAbs % modelCol.styles.length];
      }
      if (modelRow.styles != null) {
         if (structStyle == null) {
            structStyle = modelRow.styles[rowAbs % modelRow.styles.length];
         } else {
            // merge but with no StyleID space => linked to the Static StyleID (0)
            structStyle = modelCol.styles[colAbs].mergeByteObject(modelRow.styles[rowAbs]);
         }
      }
      d.setStructStyle(structStyle);
   }

   public void setStyleClass(StyleClass sc) {
      super.setStyleClass(sc);
      updateStyleClass();
      updateTitleStyles();
      //
   }

   /**
    * Init a column or a row with 0 or negative setup value. <br>
    * <br>
    * Need the opposite cosize setup values. Those value <br>
    * 
    * @param policy cell policy
    * @param value
    * @param rowAbs
    * @return
    */
   private int setupImplicitCell(boolean isRow, ByteObject policy, int index, int[] coSetupSize, int value) {
      if (modelGenetics != null) {
         int flag = (!isRow) ? IBOGenetics.GENE_FLAG_3_SAME_SIZE_W : IBOGenetics.GENE_FLAG_4_SAME_SIZE_H;
         if (modelGenetics.hasFlag(IBOGenetics.GENE_OFFSET_01_FLAG, flag)) {
            int indexv = (!isRow) ? IBOGenetics.GENE_OFFSET_03_WIDTH2 : IBOGenetics.GENE_OFFSET_04_HEIGHT2;
            int size = modelGenetics.get2(indexv);
            return size;
         }
      }
      // cell value is negative or 0
      if (policy.hasFlag(OFFSET_02_FLAG, FLAG_2_ETALON)) {
         // use the etalon for computing the implicit size
         if (isRow) {
            return getEtalon(policy).getDrawnHeight();
         } else {
            return getEtalon(policy).getDrawnWidth();
         }
      } else {
         // TODO take the max init the whole column/row
         int val = 0;
         if (isRow) {
            val = initModelDrawableRow(index, value, coSetupSize);
         } else {
            val = initModelDrawableCol(index, value, coSetupSize);
         }
         return val;
      }
   }

   /**
    * Transform 0 and negative setupsize to their explicit values. 
    * <br>
    * <br>
    * Happens to
    * <li> {@link IBOCellPolicy#CELL_0_IMPLICIT_SET}
    * <br>
    * <br>
    * 
    * @param policy
    * @param setupSizes
    */
   private void setupImplicitCells(boolean isRow, ByteObject policy, int[] setupSizes, int[] coSetupSize) {
      for (int i = 0; i < setupSizes.length; i++) {
         int value = setupSizes[i];
         if (value <= 0) {
            setupSizes[i] = setupImplicitCell(isRow, policy, i, coSetupSize, value);
         }
      }
   }

   private void setupImplicitFlow(boolean isRow, CellModel cm, int totalSize, int sepSize, int[] coSizeSetup) {
      //check implicit for modelGenetics that may speed up
      if (modelGenetics != null) {
         int flag = (!isRow) ? IBOGenetics.GENE_FLAG_3_SAME_SIZE_W : IBOGenetics.GENE_FLAG_4_SAME_SIZE_H;
         if (modelGenetics.hasFlag(IBOGenetics.GENE_OFFSET_01_FLAG, flag)) {
            int index = (!isRow) ? IBOGenetics.GENE_OFFSET_03_WIDTH2 : IBOGenetics.GENE_OFFSET_04_HEIGHT2;
            int size = modelGenetics.get2(index);
            setFlowSingleSize(cm, totalSize, sepSize, size);
            return;
         }
      }
      int size = cm.policy.get4(OFFSET_09_SIZE4);
      // implicit case, it needs the other cosize values to be computed
      int wsize = 0;
      int hsize = 0;
      if (!isRow) {
         wsize = size;
         // we cannot know which row since we don't know yet the # of cols.
         hsize = coSizeSetup[0];
      } else {
         hsize = size;
         wsize = coSizeSetup[0];
      }
      // implicit size. we must init the etalon with that opposite cosize value
      if (cm.policy.hasFlag(OFFSET_02_FLAG, FLAG_2_ETALON)) {
         if (!isRow) {
            size = getEtalon(cm.policy).getDrawnWidth();
         } else {
            size = getEtalon(cm.policy).getDrawnHeight();
         }
         setFlowSingleSize(cm, totalSize, sepSize, size);
      } else if (cm.policy.hasFlag(IBOCellPolicy.OFFSET_02_FLAG, IBOCellPolicy.FLAG_3_MAX)) {
         //get the maximum size on the whole set: init all the drawables and take the maximum value
         initModelDrawablesDimensionAll(wsize, hsize);
         size = getModelDrawablesMaxSizeFlow(isRow);
         setFlowSingleSize(cm, totalSize, sepSize, size);
      } else {
         // cells size implicit on first row/column. apply those values for all rows/columns
         cm.setupSizes = initModelDrawablesDimension(wsize, hsize, totalSize, sepSize, isRow);
         cm.numCells = cm.setupSizes.length;
      }
      //
   }

   /**
    * Apply minimum and maximum bounds on cell sizes. <br>
    * <br>
    * Called once explicit setup on both rows and columns have been called. <br>
    * <br>
    * Called at the end of 
    * <li>Setup Fixed
    * <li>Setup Varaible
    * <br>
    * <br> 
    * @param policy
    * @param setupSizes
    * @param totalSize
    * @param coSetupSize
    */
   private void setupMinMaxCells(ByteObject policy, int[] setupSizes, int totalSize, int[] coSetupSize) {
      int max = policy.get2(IBOCellPolicy.OFFSET_11_SIZE_MAX2);
      int min = policy.get2(IBOCellPolicy.OFFSET_10_SIZE_MIN2);
      int[] rowMinSizes = policy.getValuesFlag(CELLP_BASIC_SIZE, OFFSET_04_FLAGZ, FLAGZ_3_MIN_SIZES);
      int[] rowMaxSizes = policy.getValuesFlag(CELLP_BASIC_SIZE, OFFSET_04_FLAGZ, FLAGZ_4_MAX_SIZES);
      boolean isMinMax = rowMinSizes != null || rowMaxSizes != null;
      if (isMinMax) {
         for (int i = 0; i < setupSizes.length; i++) {
            setupSizes[i] = applyMinMax(i, setupSizes[i], rowMinSizes, rowMaxSizes);
         }
      }
   }

   private void setupNumColRows() {
      int numTotalCols = modelCol.policy.get2(OFFSET_05_CELL_NUM2);
      int numTotalRows = modelRow.policy.get2(OFFSET_05_CELL_NUM2);

      if (numTotalCols == 0 && numTotalRows == 0) {
         // check policyCol to know which is the strongest.
         if (modelRow.policy.hasFlag(OFFSET_02_FLAG, FLAG_5_STRONG_FLOW)) {
            tableType = IBOTablePolicy.TABLE_TYPE_4FLOW_ROW; //strong rows
            setHelperFlag(HELPER_FLAG_01_STRONG_ROWS, true);
         } else {
            tableType = IBOTablePolicy.TABLE_TYPE_3FLOW_COL; //strong columns
         }
         //those values will be updated by setup when cell sizes are computed
         numTotalCols = 1;
         numTotalRows = 1;
      } else if (numTotalCols != 0 && numTotalRows == 0) {
         tableType = IBOTablePolicy.TABLE_TYPE_1GENERIC_COL; // strong columns
         numTotalRows = updateTotalColRows(numTotalCols);
      } else if (numTotalRows != 0 && numTotalCols == 0) {
         setHelperFlag(HELPER_FLAG_01_STRONG_ROWS, true);
         tableType = IBOTablePolicy.TABLE_TYPE_2GENERIC_ROW; // strong row
         numTotalCols = updateTotalColRows(numTotalRows);
      } else {
         // type
         tableType = IBOTablePolicy.TABLE_TYPE_0STRICT;
      }
      modelCol.setNumCells(numTotalCols);
      modelRow.setNumCells(numTotalRows);

   }

   private void setupPolicyType(boolean isRow, CellModel cm, int sepSize, int totalSize) {
      // check the cell type
      int typer = cm.policy.get1(IBOCellPolicy.OFFSET_01_TYPE1);
      switch (typer) {
         case TYPE_0_GENERIC:
            setupTypeGeneric(isRow, cm, totalSize, sepSize);
            break;
         case TYPE_2_RATIO:
            setupTypeRatio(cm, totalSize, sepSize);
            break;
         default:
         case TYPE_1_FLOW:
            setupTypeFlow(isRow, cm, totalSize, sepSize);
            break;
      }
   }

   private void setupPolicyType2() {
      // compute from model number of rows
      //future 
      int gridV = getSizeGridV();
      int gridH = getSizeGridH();
      int ch = getContentH();
      int cw = getContentW();
      modelCol.sepSize = getSizeGridV();
      modelRow.sepSize = getSizeGridH();

      //must first compute an
      switch (tableType) {
         case IBOTablePolicy.TABLE_TYPE_0STRICT:
         case IBOTablePolicy.TABLE_TYPE_1GENERIC_COL:
         case IBOTablePolicy.TABLE_TYPE_3FLOW_COL:
            //first the column
            setupPolicyType(false, modelCol, gridV, cw);
            setupPolicyType(true, modelRow, gridH, ch);
            // break the implicit
            setupPolicyTypeImplicit(false, modelCol, modelRow, gridV, cw);
            setupPolicyTypeImplicit(true, modelRow, modelCol, gridH, ch);
            break;
         case IBOTablePolicy.TABLE_TYPE_2GENERIC_ROW:
         case IBOTablePolicy.TABLE_TYPE_4FLOW_ROW:
            // first compute setup heights. must be strict/ratio/
            setupPolicyType(true, modelRow, gridH, ch);
            // number of columns is unknown. will be 1 or more. depends on
            // policy size
            setupPolicyType(false, modelCol, gridV, cw);

            //implicit weak columns first
            setupPolicyTypeImplicit(false, modelCol, modelRow, gridV, cw);
            //implicit strong rows last.
            setupPolicyTypeImplicit(true, modelRow, modelCol, gridH, ch);

            break;
         default:
            break;
      }

      //end switch
      switch (tableType) {
         case IBOTablePolicy.TABLE_TYPE_3FLOW_COL:
            //re compute row number based on col number
            modelRow.setNumCells(updateTotalColRows(modelCol.numCells));
            break;
         case IBOTablePolicy.TABLE_TYPE_4FLOW_ROW:
            //re compute the number of weak columns
            modelCol.setNumCells(updateTotalColRows(modelRow.numCells));
            break;
         default:
            break;
      }
   }

   public void stateReadFrom(StatorReader state) {
      super.stateReadFrom(state);
   }

   public void stateWriteTo(StatorWriter state) {
      super.stateWriteTo(state);
   }
   /**
    * Finalize setup by computing implicit setup size to implict. <br>
    * <br>
    * setup size arrays and policies are created.
    * 
    * @param cellPolicies
    * @param numTotalCells
    * @param data
    * @param totalSize
    * @param sepSize
    */
   private void setupPolicyTypeImplicit(boolean isRow, CellModel cm, CellModel coModel, int sepSize, int totalSize) {
      // check the cell type
      int typer = cm.policy.get1(IBOCellPolicy.OFFSET_01_TYPE1);
      switch (typer) {
         case TYPE_0_GENERIC:
            setupImplicitCells(isRow, cm.policy, cm.setupSizes, coModel.setupSizes);
            break;
         case TYPE_2_RATIO:
            break;
         default:
         case TYPE_1_FLOW:
            int size = cm.policy.get4(OFFSET_09_SIZE4);
            if (size <= 0) {
               setupImplicitFlow(isRow, cm, totalSize, sepSize, coModel.setupSizes);
               // we must update the number of columns and row
            }
            break;
      }
   }

   /**
    * <br>
    * We use the {@link IDrawListener} to draw columns.
    * <br>
    * It will be located in the {@link ViewPane} top close or top bottom area.
    * <br>
    * So the column titles will always be visible.
    *  Else a simple
    * drawable in which column number are drawn.
    * 
    * <br>
    * <br>
    * Called by {@link TableView#computeConfig(Config)}
    */
   private void setupTitlesCol() {
      if (hasTech(T_FLAG_4_SHOW_COL_TITLE)) {
         //titles are hardcoded or localized
         if (model != null && model.getModelAux().getTitlesColumns() != null) {
            modelCol.setTitles(model.getModelAux().getTitlesColumns());
         }
         modelCol.setupTitles();
         if (modelCol.titlesView == null) {
            StyleClass titleColStyleClass = styleClass.getStyleClass(IBOTypesGui.LINK_82_STYLE_TABLE_COL_TITLE);
            //Place Holder to hold Columns..
            //ViewPane will automatically set as parent this one
            DrawableInjected titlesView = new DrawableInjected(gc, titleColStyleClass, this, this);
            //debug
            titlesView.setDebugName("TitleColsContainer");
            titlesView.setBehaviorFlag(ITechDrawable.BEHAVIOR_23_PARENT_EVENT_CONTROL, true);
            setHeaderClose(titlesView, C.POS_0_TOP);
            modelCol.titlesView = titlesView;
         }
      }
   }

   /**
    * TODO manage when rows are added or deleted.
    */
   private void setupTitlesRow() {
      if (hasTech(T_FLAG_3_SHOW_ROW_TITLE)) {
         modelRow.setupTitles();
         if (modelRow.titlesView == null) {
            StyleClass titleRowStyleClass = styleClass.getStyleClass(IBOTypesGui.LINK_83_STYLE_TABLE_ROW_TITLE);
            DrawableInjected titlesView = new DrawableInjected(gc, titleRowStyleClass, this, this);
            titlesView.setDebugName("TitleRowsContainer");
            titlesView.setBehaviorFlag(ITechDrawable.BEHAVIOR_23_PARENT_EVENT_CONTROL, true);
            setHeader(titlesView, C.POS_2_LEFT, ITechViewPane.PLANET_MODE_0_EAT);
            modelRow.titlesView = titlesView;
         }
      }
   }

   private void setupTypeFlow(boolean isRow, CellModel cm, int totalSize, int sepSize) {
      // when size is zero or negative, take each, min, maximum or etalon. no
      // other choice
      // when size is zero, take maximum
      int size = cm.policy.get4(OFFSET_09_SIZE4);
      if (size > 0) {
         int divisor = size + sepSize;
         // when divisor is small, the number of column may be too much
         int numPossibleTotalCells = IntUtils.divideFloor(totalSize, divisor);
         if (numPossibleTotalCells == 0) {
            numPossibleTotalCells = 1;
         }
         cm.setNumCells(numPossibleTotalCells);
         cm.setupSizes = new int[] { size };
         cm.policies = new int[] { CELL_1_EXPLICIT_SET };
      } else {
         //will have to setup later but yet put data
         cm.setupSizes = new int[] { size };
         cm.policies = new int[] { CELL_1_EXPLICIT_SET };
      }
      cm.setHelperFlag(CellModel.HELPER_FLAG_08_ALL_CELLS_SAME_SIZE, true);
      if (isRow) {
         setViewFlag(ITechViewDrawable.VIEWSTATE_11_CONTENT_PH_VIEWPORT_DH, true);
         setViewFlag(ITechViewDrawable.VIEWSTATE_13_CONTENT_H_DEPENDS_VIEWPORT, true);
      } else {
         setViewFlag(ITechViewDrawable.VIEWSTATE_10_CONTENT_PW_VIEWPORT_DW, true);
         setViewFlag(ITechViewDrawable.VIEWSTATE_12_CONTENT_W_DEPENDS_VIEWPORT, true);
      }
   }

   private void setupTypeGeneric(boolean isRow, CellModel cm, int totalSize, int sepSize) {
      ByteObject policy = cm.policy;
      int[] setupSizes = new int[cm.numCells];
      cm.setupSizes = setupSizes;
      // extract policy values
      int[] policies = null;
      if (policy.hasFlag(OFFSET_04_FLAGZ, FLAGZ_1_POLICIES)) {
         policies = policy.getValuesFlag(CELLP_BASIC_SIZE, OFFSET_04_FLAGZ, FLAGZ_1_POLICIES);
      } else {
         // no special policies thus either defined built them based on
         // setupColSizes
         policies = new int[] { policy.get1(OFFSET_07_POLICY1) };
      }
      cm.policies = policies;
      if (policies.length == 1) {
         cm.setHelperFlag(CellModel.HELPER_FLAG_08_ALL_CELLS_SAME_SIZE, true);
      }
      // extract size values
      int[] sizes = null;
      if (policy.hasFlag(OFFSET_04_FLAGZ, FLAGZ_2_SIZES)) {
         sizes = policy.getValuesFlag(CELLP_BASIC_SIZE, OFFSET_04_FLAGZ, FLAGZ_2_SIZES);
      } else {
         int size = policy.get4(OFFSET_09_SIZE4);
         if (modelGenetics != null) {
            size = getGeneticSize(isRow);
         }
         sizes = new int[] { size };
      }
      int[] frames = null;
      if (policy.hasFlag(OFFSET_04_FLAGZ, FLAGZ_5_FRAMES)) {
         frames = policy.getValuesFlag(CELLP_BASIC_SIZE, OFFSET_04_FLAGZ, FLAGZ_5_FRAMES);
      }
      cm.frames = frames;

      for (int i = 0; i < setupSizes.length; i++) {
         int val = getArrayFirstValOrIndex(policies, i);
         int size = getArrayFirstValOrIndex(sizes, i);
         switch (val) {
            case CELL_3_FILL_STRONG:
               // that might change
               size = totalSize;
               setViewPortFlag(isRow);
               break;
            case CELL_0_IMPLICIT_SET:
               size = 0;
               break;
            case CELL_4_FILL_AVERAGE:
            case CELL_5_FILL_WEAK:
               //will be computed in variable
               size = SETUP_WEAK;
               if (isRow) {
                  setViewFlag(ITechViewDrawable.VIEWSTATE_13_CONTENT_H_DEPENDS_VIEWPORT, true);
                  cm.setHelperFlag(CellModel.HELPER_FLAG_14_VARIABLE_SETUP_SIZE, true);
               } else {
                  setViewFlag(ITechViewDrawable.VIEWSTATE_12_CONTENT_W_DEPENDS_VIEWPORT, true);
                  cm.setHelperFlag(CellModel.HELPER_FLAG_14_VARIABLE_SETUP_SIZE, true);
               }
               //
               break;
            case CELL_2_RATIO:
               // variable
               setViewPortFlag(isRow);
               break;
            default:
               // explicit case. when set to 0, that means implicit
               break;
         }
         setupSizes[i] = size;
      }
   }

   //#mdebug

   private void setupTypeRatio(CellModel cm, int totalSize, int sepSize) {
      int[] setupSizes = new int[cm.numCells];
      int[] values = new int[cm.numCells];
      if (cm.policy.hasFlag(OFFSET_02_FLAG, FLAG_4_RATIO_EVEN)) {
         IntUtils.fill(values, 10);
         cm.setHelperFlag(CellModel.HELPER_FLAG_08_ALL_CELLS_SAME_SIZE, true);
      } else {
         values = cm.policy.getValuesFlag(CELLP_BASIC_SIZE, OFFSET_04_FLAGZ, FLAGZ_2_SIZES);
      }
      int hroom = totalSize - ((cm.numCells - 1) * sepSize);
      double total = IntUtils.sum(values);
      double w = hroom;
      for (int i = 0; i < values.length; i++) {
         setupSizes[i] = (int) (w * ((double) values[i]) / total);
      }
      // redistribute left over pixel according to fill policy
      int leftOver = hroom - IntUtils.sum(setupSizes);
      if (leftOver >= setupSizes.length)
         throw new IllegalStateException("leftOver");
      for (int i = 0; i < leftOver; i++) {
         setupSizes[i]++;
      }
      cm.setupSizes = setupSizes;
      cm.policies = new int[] { CELL_1_EXPLICIT_SET };
   }

   protected void setViewPortFlag(boolean isRow) {
      if (isRow) {
         setViewFlag(ITechViewDrawable.VIEWSTATE_13_CONTENT_H_DEPENDS_VIEWPORT, true);
      } else {
         setViewFlag(ITechViewDrawable.VIEWSTATE_12_CONTENT_W_DEPENDS_VIEWPORT, true);
      }
   }

   /**
    * How do you dispatch the ViewState?
    * <br>
    * <br>
    * If ViewSate model size does not match actual model size, State is discarded.
    * <br>
    * <br>
    * What if the model size depends on ViewState reads by a lower class? Then
    */
   public void setViewState(ViewState vs) {
      super.setViewState(vs);
      if (vs != null) {
         ViewState ns = vs.getMyState(3);
         if (ns != null) {
            int curmodelsize = getSize();
         }
      }

   }

   /**
    * Change the X,Y coordinate of Table and of all its cells/planetaries. 
    * <br>
    * <br>
    * Updates the current {@link Config} X and Y coordinates. 
    * 
    * TODO Several behaviors. <br>
    * Mainly used in animations moving the whole table. <br>
    * But in some case, developer wants a soft set(XY) where nothing is done until the draw method is called. 
    * Important to remember that {@link Config} are offsets from content (x,y).
    * Final Drawable coordinates are given just before being drawn.
    * This is an issue when animating Cell Drawables, 
    * This is called {@link ITechDrawable#BEHAVIOR_12_ZERO_COORDINATE} behaviour.
    * <br>
    * <br>
    * @param x
    * @param y
    */
   public void setXY(int x, int y) {
      //      int diffX = x - getX();
      //      int diffY = y - getY();
      //      //
      //      int size = getSize();
      //      for (int i = 0; i < size; i++) {
      //         IDrawable d = mapper.getDrawable(i);
      //         if (d != null) {
      //            d.shiftXY(diffX, diffY);
      //         }
      //      }
      super.setXY(x, y);
      // we must do an update

   }

   /**
    * Shift X and Y coordinates. Shift Configuration actually.
    * 
    * @param x
    * @param y
    */
   public void shiftXY(int dx, int dy) {
      if (dx != 0) {
         setX(getX() + dx);
      }
      if (dy != 0) {
         setY(getY() + dy);
      }
      //currentConfig.shiftXY(dx, dy);
   }

   public void toString(Dctx sb) {
      sb.root(this, "TableView");
      sb.append(ToStringStaticGui.toStringTableType(tableType) + " modelSize=" + getSize() + " spanCount=" + spannedCount);
      super.toString(sb.sup());

      //helper flags
      sb.nl();
      toStringHelperFlags(sb);
      sb.nl();
      sb.append("ColFlags:");
      modelCol.toStringHelperFlags(sb);
      sb.nl();
      sb.append("RowFlags:");
      modelRow.toStringHelperFlags(sb);

      int numTotalCols = modelCol.numCells;
      int numTotalRows = modelRow.numCells;
      sb.nlFrontTitle(modelCol.setupSizes, "Setup Col Sizes");
      sb.nlFrontTitle(modelRow.setupSizes, "Setup Row Sizes");
      sb.append("numTotal (" + numTotalCols + "," + numTotalRows + ")");

      toStringModelColRow(sb.newLevel());
      TablePolicyFactory tablePol = gc.getTablePolicyC();
      if (policyTable != null) {
         gc.getTableOperator().toStringTablePolicy(policyTable, sb.newLevel());
      } else {
         sb.nl();
         sb.append("Table Policy is null");
      }

      CellPolicy cellPol = gc.getCellPolicy();
      sb.nl();
      sb.append(cellPol.toString(modelCol.policy, sb.newLevel(), "col"));
      sb.nl();
      sb.append(cellPol.toString(modelRow.policy, sb.newLevel(), "row"));

      if (span != null) {
         sb.append("#Spans");
         sb.nl();
         int index = 0;
         for (int i = 0; i < numTotalRows; i++) {
            for (int j = 0; j < numTotalCols; j++) {
               sb.append(span[index]);
               sb.append('\t');
               index++;
            }
            sb.nl();
         }
      }
      if (visualIndex != null) {
         sb.append("#GridIndexModel");
         sb.nl();
         int index = 0;
         for (int i = 0; i < numTotalRows; i++) {
            for (int j = 0; j < numTotalCols; j++) {
               sb.append(visualIndex[index]);
               sb.append('\t');
               index++;
            }
            sb.nl();
         }
         sb.nl();
      }

      sb.nl();
      gc.getTableOperator().toStringTableTech(techTable, sb.newLevel());

      sb.nlLvlIgnoreNull("#Titles COLUMN", modelCol.titlesView);

      sb.nlLvlIgnoreNull("#Titles ROW", modelRow.titlesView);

      if (sb.hasFlagData(gc, IFlagsToStringGui.D_FLAG_01_STYLE)) {
         sb.nlLvl("Cell StyleKey", tableCellStyleClass);
      }

      if (sb.hasFlagData(gc, IFlagsToStringGui.D_FLAG_10_TABLE_SELECTED_DRAWABLE)) {
         if (getSize() > 0) {
            int selectedIndex = getGridIndexFromAbs(modelCol.selectedCellAbs, modelRow.selectedCellAbs);
            IDrawable d = getModelDrawable(selectedIndex);
            sb.nlLvl("#Selected Drawable", d);
         }
      }
      if (sb.hasFlagData(gc, IFlagsToStringGui.D_FLAG_06_TABLE_MODEL)) {
         sb.nlLvl("TableModel", model);
      }
      if (sb.hasFlagData(gc, IFlagsToStringGui.D_FLAG_07_TABLE_MAPPER)) {
         sb.nlLvl("Mapper", mapperObjectDrawable);
      }
   }

   //#enddebug

   public void toString1Line(Dctx dc) {
      dc.root(this, "TableView");
      dc.appendWithSpace(" " + getDebugName());
      dc.append(" [" + modelCol.numCells + ":" + modelRow.numCells + "]");
      if (styleStates != 0) {
         ToStringStaticGui.toStringBehavior(this, dc);
      }
   }

   public void toStringHelperFlag(Dctx sb, int flag, String str) {
      if (hasHelperFlag(flag)) {
         sb.append(' ');
         sb.append(str);
      }
   }

   public void toStringHelperFlags(Dctx sb) {
      sb.append("HelperFlags =");
      toStringHelperFlag(sb, HELPER_FLAG_01_STRONG_ROWS, "StrongRows");
      toStringHelperFlag(sb, HELPER_FLAG_02_SINGLE_CONFIG, "SingleConfig");
      toStringHelperFlag(sb, HELPER_FLAG_03_CAP_OVERSIZE, "CapOverSize");
      toStringHelperFlag(sb, HELPER_FLAG_04_TOTAL_SET, "TotalSet");
      toStringHelperFlag(sb, HELPER_FLAG_05_YCOORDS_ONLY, "YCoordOnly");
      toStringHelperFlag(sb, HELPER_FLAG_16_SPANNING, "Spanning");
      toStringHelperFlag(sb, HELPER_FLAG_17_OVERSIZE_WIDTH, "OversizeW");
      toStringHelperFlag(sb, HELPER_FLAG_18_OVERSIZE_HEIGHT, "OversizeH");
      toStringHelperFlag(sb, HELPER_FLAG_19_SPECIAL_FILL, "SpecialFill");

   }

   /**
    * To be removed from production code
    * 
    * @return
    */
   public void toStringModelColRow(Dctx sb) {
      sb.append("#Config  ");
      String lmaster = "LeftMaster";
      if (!modelCol.useMaster) {
         lmaster = "RightMaster";
      }
      String tmaster = "TopMaster";
      if (!modelRow.useMaster) {
         tmaster = "BottomMaster";
      }
      sb.append(lmaster + ":" + tmaster);
      sb.nl();
      sb.append("Root (" + modelCol.rootCellAbs + "," + modelRow.rootCellAbs + ") ");
      sb.append("Selected (" + modelCol.selectedCellAbs + "," + modelRow.selectedCellAbs + ")");
      sb.nl();
      sb.append("First (" + modelCol.firstCellAbs + "," + modelRow.firstCellAbs + ") ");
      sb.append("Last (" + modelCol.lastCellAbs + "," + modelRow.lastCellAbs + ")");
      sb.nl();
      sb.append("NumVisible (" + modelCol.numVisibleCells + "," + modelRow.numVisibleCells + ") ");
      sb.append("NumFullyVisible (" + modelCol.numFullVisibleCells + "," + modelRow.numFullVisibleCells + ") ");
      sb.append("IndexPartiallyVisibles (" + modelCol.partiallyVisibleCellAbs + "," + modelRow.partiallyVisibleCellAbs + ")" + ",(" + modelCol.partiallyVisibleCellAbs2 + "," + modelRow.partiallyVisibleCellAbs2 + ")");
      sb.nlFrontTitle(modelCol.workCellSizes, "Work Col Sizes");
      sb.nlFrontTitle(modelRow.workCellSizes, "Work Row Sizes");
      sb.append(" Pixel Consumed=" + modelCol.consumedPixels + "," + modelRow.consumedPixels);
      sb.nl();
      sb.append("undrawnWLeft=" + modelCol.undrawnTopLeft + " undrawnHTop=" + modelRow.undrawnTopLeft + " undrawnWRight=" + modelCol.undrawnBotRight + " undrawnHBottom=" + modelRow.undrawnBotRight);
      sb.nlFrontTitle(modelCol.positions, "X Coordinates");
      sb.nlFrontTitle(modelRow.positions, "Y Coordinates");
   }

   private String toStringSelectedDrawable() {
      IDrawable d = getSelectedDrawable();
      if (d != null) {
         return d.toString1Line();
      } else {
         return "[Null Selected Drawable]";
      }
   }

   /**
    * Compute the {@link IDrawable} flag based on the table structure.
    * <br>
    * <br>
    * Called at every init
    * <br>
    * Navigation may be disabled with
    * <li> {@link IBOTableView#T_FLAGX_1_NO_SELECTION}
    * 
    */
   private void updateNavFlag() {
      boolean navH = false;
      boolean navV = false;
      boolean selectAble = false;
      if (!hasTechX(T_FLAGX_1_NO_SELECTION)) {
         selectAble = true;
         if (modelCol.numCells > 1) {
            navH = true;
         }
         if (modelRow.numCells > 1) {
            navV = true;
         }
      }
      setBehaviorFlag(ITechDrawable.BEHAVIOR_27_NAV_HORIZONTAL, navH);
      setBehaviorFlag(ITechDrawable.BEHAVIOR_26_NAV_VERTICAL, navV);
      setBehaviorFlag(ITechDrawable.BEHAVIOR_28_NAV_SELECTABLE, selectAble);

   }

   /**
    * Called during construction or when {@link StyleClass} is changed
    */
   protected void updateStyleClass() {
      //TODO ATTENTION. Model provides a TECH. in which case the tech from the styleClass is never
      // used
      techTable = styleClass.getByteObject(IBOTypesGui.LINK_80_TECH_TABLE);
      // take default Tech
      if (techTable == null) {
         techTable = gc.getTablePolicyC().getTableTechDef();
      }
      if (hasTechM(T_FLAGM_7_CLOCK_VERTICAL)) {
         setBehaviorFlag(ITechDrawable.BEHAVIOR_29_NAV_CLOCK_VERTICAL, true);
      }
      if (hasTechM(T_FLAGM_6_CLOCK_HORIZ)) {
         setBehaviorFlag(ITechDrawable.BEHAVIOR_30_NAV_CLOCK_HORIZONTAL, true);
      }
      tableCellStyleClass = styleClass.getStyleClass(IBOTypesGui.LINK_81_STYLE_CLASS_TABLE_CELL);
      //style class for mapper
      mapperObjectDrawable.setStyleClass(tableCellStyleClass);

      figureOverlay = styleClass.getByteObject(IBOTypesGui.LINK_84_STYLE_TABLE_OVERLAY_FIGURE);
      emptyBluePrint.setStyleClass(styleClass);
   }

   /**
    * We don't invalidate our children?
    * 
    */
   public void layoutInvalidate(boolean topDown) {
      super.layoutInvalidate(topDown);
   }
   
   protected void updateTablePolicy(ByteObject policyTable) {
      invalidateLayout();
      initPolicy(policyTable);
   }

   private void updateTitleStyles() {
      if (modelRow.titlesView != null) {
         StyleClass titleRowStyleClass = styleClass.getStyleClass(IBOTypesGui.LINK_83_STYLE_TABLE_ROW_TITLE);
         modelRow.titlesView.setStyleClass(titleRowStyleClass);
      }
      if (modelCol.titlesView != null) {
         StyleClass titleRowStyleClass = styleClass.getStyleClass(IBOTypesGui.LINK_82_STYLE_TABLE_COL_TITLE);
         modelCol.titlesView.setStyleClass(titleRowStyleClass);
      }
   }

   /**
    * Based on model size and number of columns, initialized number of rows
    * Call this method when the number of column has been computed
    */
   private int updateTotalColRows(int numOpposite) {
      //when no data in the model?
      if (numOpposite == 0)
         return 0;
      int modulo = getSize() % numOpposite;
      int numTotal = (getSize() / numOpposite);
      if (modulo != 0) {
         numTotal++;
      }
      return numTotal;
   }

}
