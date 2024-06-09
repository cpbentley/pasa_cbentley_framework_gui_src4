package pasa.cbentley.framework.gui.src4.cmd;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.core.src4.event.IEventConsumer;
import pasa.cbentley.core.src4.i8n.IStringProducer;
import pasa.cbentley.core.src4.i8n.LocaleID;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.core.src4.structs.BufferObject;
import pasa.cbentley.core.src4.structs.IntToObjects;
import pasa.cbentley.core.src4.structs.IntToStrings;
import pasa.cbentley.framework.cmd.src4.interfaces.ITechCmd;
import pasa.cbentley.framework.datamodel.src4.table.ObjectTableModel;
import pasa.cbentley.framework.gui.src4.canvas.CanvasDebugger;
import pasa.cbentley.framework.gui.src4.canvas.ExecutionContextGui;
import pasa.cbentley.framework.gui.src4.canvas.IBOCanvasAppliGui;
import pasa.cbentley.framework.gui.src4.canvas.ICanvasDrawable;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.canvas.ViewContext;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.core.TopoViewDrawable;
import pasa.cbentley.framework.gui.src4.ctx.CanvasGuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.factories.TableCellPolicyFactory;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsView;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IUIView;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.table.interfaces.ITechTable;
import pasa.cbentley.framework.gui.src4.tech.ITechLinks;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;
import pasa.cbentley.layouter.src4.engine.SizerFactory;
import pasa.cbentley.layouter.src4.tech.ITechLayout;

/**
 * Parameters are implicit, or fetch with a GUI.
 * 
 * Manage the application of 
 * 
 * <li> {@link IBOCanvasAppliGui#CANVAS_APP_DRW_OFFSET_07_DEBUG_MODE1}
 * <li> {@link IBOCanvasAppliGui#CANVAS_APP_DRW_OFFSET_05_DEBUG_BAR_POSITION1}
 * 
 * 
 * <p>
 * It knows how to read and write parameters on the {@link CmdInstanceDrawable}
 * </p>
 * 
 * 
 * @author Charles Bentley
 *
 */
public class MCmdGuiChangeCanvasDebugger extends MCmdGui implements IEventConsumer {

   /**
    * 
    */
   private int mode;

   private int position;

   public MCmdGuiChangeCanvasDebugger(GuiCtx gc) {
      super(gc, ICmdsView.VCMD_15_CANVAS_DEBUGGER);
   }

   public void consumeEvent(BusEvent e) {
      // TODO Auto-generated method stub

   }

   private void execute(InputConfig ic, CmdInstanceDrawable cd, ICanvasDrawable canvas, int modeParam, int positionParam) {
      
      ExecutionContextGui dExCtx = cd.getDExCtx();
      TopoViewDrawable topoViewDrawable = canvas.getTopoViewDrawable();

      CanvasDebugger canvasDebug = canvas.getExtras().getDebugCanvas(); //null if not already created

      //get current values if the same.. check if those values are correct physically
      int positionCurrent = canvas.getCanvasBOHelper().getDebugBarPosition();
      int modeCurrent = canvas.getCanvasBOHelper().getDebugMode();

      int positionReal = topoViewDrawable.getSatteliteFlag(canvasDebug);

      if (positionReal == ITechViewPane.SAT_FLAG_XX_NONE) {

      }
      
      if (modeParam == ITechCanvasDrawable.DEBUG_0_NONE) {
         if (canvasDebug == null) {
            cd.setActionString("Debug Mode No Changes. CanvasDebug was already Null");
            //remove it from
         } else {
            topoViewDrawable.removeDrawable(canvasDebug);
            dExCtx.addDrawnHide(canvasDebug);
            dExCtx.addDrawnInvalidated(topoViewDrawable);
            cd.setActionString("Debug Mode removed. CanvasDebug set to Null");
         }
      } else {
         if (canvasDebug == null) {
            StyleClass sc = gc.getStyleClass(IUIView.SC_3_LOG);
            ViewContext vcRoot = topoViewDrawable.getVC();
            canvasDebug = new CanvasDebugger(gc, sc, vcRoot);
         }
         SizerFactory sf = gc.getLAC().getFactorySizer();
         ByteObject sizerH = null;
         ByteObject sizerW = sf.getSizerFitParentLazy();
         
         if (modeParam == ITechCanvasDrawable.DEBUG_1_LIGHT) {
        
             sizerH = sf.getSizerFontHeightRatio(100, ITechLayout.ET_FONT_2_DEBUG);
    
            //sets the header with no specific sizer. ViewPane sets its own.
            topoViewDrawable.setHeader(canvasDebug, C.POS_0_TOP, ITechViewPane.PLANET_MODE_0_EAT);

            dExCtx.addDrawnShow(canvasDebug);
            dExCtx.addDrawnInvalidated(topoViewDrawable);
         } else if (modeParam == ITechCanvasDrawable.DEBUG_2_COMPLETE_1LINE) {
             sizerH = sf.getSizerFontHeightTimes(1, ITechLayout.ET_FONT_2_DEBUG);
             //TODO  or use ViewDrawable without a viewpane
             canvasDebug.setSizerHContent(sizerH);
         } else if (modeParam == ITechCanvasDrawable.DEBUG_3_COMPLETE_2LINES) {
            sizerH = sf.getSizerFontHeightTimes(2, ITechLayout.ET_FONT_2_DEBUG);
         } else {
            cd.setActionString("Bad Parameter " + modeParam);
         }
         
         canvasDebug.setDebugMode(modeParam);
         canvasDebug.setOrientation(positionParam);
      }

      int type = FLAG_02_FULL_REPAINT | FLAG_04_RENEW_LAYOUT;
      cd.actionDone(null, type);
   }

   public void execute(InputConfig ic) {

      ic.checkCmdInstanceNotNull();

      //the cmdinstance knows on which canvas to change

      CmdInstanceDrawable cd = ic.getCmdInstance();

      if (!cd.isParamed()) {
         //fetch parameters from gui but we need a canvas.. table selection of canvas.. if only one canvas
         //skip the step
         ic.sr.actionDone();

      }
      ICanvasDrawable canvas = (ICanvasDrawable) cd.paramO;
      int mode = cd.param;
      int position = cd.param2;

      execute(ic, cd, canvas, mode, position);
   }

   /**
    * Works on the active Canvas.. {@link CanvasGuiCtx}.
    * 
    * @param ic
    * @param cd
    */
   private void getGuiParam(InputConfig ic, CmdInstanceDrawable cd) {
      IStringProducer strLoader = gc.getStrings();
      //show the table
      if (tableParamSelection == null) {
         
         StyleClass scTitle = gc.getStyleClass(IUIView.SC_2_TITLE);
         StringDrawable sd = new StringDrawable(gc, scTitle, "Debug Mode");
         tableParamSelection.setHeaderExpandTop(sd);
         
         TableCellPolicyFactory cellFac = gc.getTableCellPolicyFactory();
        
         ByteObject colPol = cellFac.getGeneric(2, 0);
         ByteObject rowPol = cellFac.getGeneric(0, 0);
         ByteObject policyTable = gc.getTablePolicyFactory().getTablePolicy(colPol, rowPol);
         StyleClass scMenu = gc.getStyleClass(IUIView.SC_1_MENU);
         StyleClass scMenuItems = scMenu.getSCNotNull(ITechLinks.LINK_74_STYLE_CLASS_MENU);

         TableView tv = new TableView(gc, scMenuItems, policyTable);
         //position it bottom left or center center logical position relative to parent/screen
       
         tv.setXYLogic(C.LOGIC_3_BOTTOM_RIGHT, C.LOGIC_3_BOTTOM_RIGHT);
         //set the selection to the language
         //init once the model has been set.
         tv.setSizer_WH_Pref();

         tv.addEventListener(this, ITechTable.EVENT_ID_00_SELECT);
         //show the form as child of current front drawable
         
         tableParamSelection = tv;
      }
      
      IntToObjects its = new IntToObjects(gc.getUC(), 10);
      its.add(ITechCanvasDrawable.DEBUG_0_NONE, "None");
      its.add(ITechCanvasDrawable.DEBUG_1_LIGHT, "Light");
      its.add(ITechCanvasDrawable.DEBUG_2_COMPLETE_1LINE, "Full 1 line");
      its.add(ITechCanvasDrawable.DEBUG_3_COMPLETE_2LINES, "Full on 2 Lines");
      ObjectTableModel otm = new ObjectTableModel(gc.getDMC(), its);
      tableParamSelection.setDataModel(otm);
      

      ICanvasDrawable canvas = null;

      //get current values if the same.. check if those values are correct physically
      int positionCurrent = canvas.getCanvasBOHelper().getDebugBarPosition();
      int modeCurrent = canvas.getCanvasBOHelper().getDebugMode();

      
      int index = ((ObjectTableModel) tableParamSelection.getTableModel()).findIndexIntFirst(modeCurrent);
      if (index == -1) {
         index = 0;
      }
      tableParamSelection.shShowDrawableOver(ic);
      
      
      //give focus to selected.. uses a separate command to host the animation
      tableParamSelection.setSelectedIndex(index, ic, true);

      
      //create an OK/CANCEL interaction cmd ctx. modal?
      //TODO actually menu listen to active cmd ctx and update itself automatically
      //based on it
      //set menu to model mode with OK / CANCEL
      String menuLabel = "Select";
      tableParamSelection.getCanvas().getMenuBar().setCmdModelOKCAncel(menuLabel);
      
      cd.actionDone(tableParamSelection, 0);

   }

   public void setParamsToggle(CmdInstanceDrawable cd, ICanvasDrawable canvas) {
      this.setParamsDirective(cd, canvas, ITechCmd.DIRECTIVE_1_TOGGLE);
   }

   public void setParamsDirective(CmdInstanceDrawable cd, ICanvasDrawable canvas, int directive) {
      cd.paramO = canvas;
      //directive
      cd.setDirective(directive);
      cd.setParamed();
   }

   public void setParams(CmdInstanceDrawable cd, ICanvasDrawable canvas, int mode, int pos) {
      cd.paramO = canvas;
      cd.param = mode;
      cd.param2 = pos;
      cd.setParamed();
   }

}
