package pasa.cbentley.framework.gui.src4.ctx;

import pasa.cbentley.byteobjects.src4.core.BOModuleAbstract;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.ToStringStaticBO;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDebugStringable;
import pasa.cbentley.core.src4.logging.ToStringStaticC;
import pasa.cbentley.framework.drawx.src4.ctx.IBOTypesDrawX;
import pasa.cbentley.framework.gui.src4.canvas.IBOCanvasAppliGui;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOCellPolicy;
import pasa.cbentley.framework.input.src4.interfaces.IBOCanvasAppli;

/**
 * When initialized, its statically loads an print to the {@link ByteObject}
 * <br>
 * <br>
 * The creation of those classes are only there for debugging purposes
 * <br>
 * <br>
 * 
 * @author Charles-Philip Bentley
 *
 */
public class BOModuleGui extends BOModuleAbstract implements IBOTypesGui, IBOCanvasAppliGui, IDebugStringable, IToStringsDIDGui, ITechCanvasDrawable, ITechCtxSettingsAppGui {

   public static final int BIP_CLASS_SC  = 3;

   public static final int BIP_MODULE_ID = 2;

   protected final GuiCtx  gc;

   public BOModuleGui(GuiCtx gc) {
      super(gc.getBOC());
      this.gc = gc;
      //#debug
      toDLog().pInit("", null, BOModuleGui.class, "Created@36", LVL_04_FINER, true);
   }

   public ByteObject getFlagOrderedBO(ByteObject bo, int offset, int flag) {
      // TODO Auto-generated method stub
      return null;
   }

   public ByteObject merge(ByteObject root, ByteObject merge) {
      int type = merge.getType();
      switch (type) {
         case TYPE_GUI_05_CELL_SPANNING:

            break;
         case TYPE_GUI_04_TABLE_POLICY:

            break;
      }
      return null;
   }

   /**
    * Displays a name of the offset field. Reflection on the field.
    * <br>
    * @param type
    * @return
    */
   public String subToStringOffset(ByteObject o, int offset) {
      int type = o.getType();
      switch (type) {
         case TYPE_GUI_05_CELL_SPANNING:
            switch (offset) {
               default:
                  return null;
            }
         default:
            return null;
      }
   }

   public String subToStringType(int type) {
      switch (type) {
         case TYPE_GUI_05_CELL_SPANNING:
            return "SPAN";
         case TYPE_GUI_04_TABLE_POLICY:
            return "TablePolicy";
         case TYPE_GUI_06_CELL_POLICY:
            return "CellPolicy";
         default:
            return null;
      }
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, BOModuleGui.class, 130);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public boolean toString(Dctx dc, ByteObject bo) {
      int type = bo.getType();
      switch (type) {
         case TYPE_012_CTX_SETTINGS:
            //check if our sub type else drop it
            gc.toStringGuiCtxTech(dc, bo);
            break;
         case TYPE_GUI_00_VIEWPANE:
            gc.getDrawableCoreFactory().toStringViewPaneTech(bo, dc);
            break;
         case TYPE_GUI_05_CELL_SPANNING:
            gc.getTableOperator().toStringSpanning(bo, dc);
            break;
         case TYPE_GUI_03_TABLE_GENETICS:
            gc.getTableOperator().toStringModelGenetics(bo, dc);
            break;
         case TYPE_GUI_04_TABLE_POLICY:
            gc.getTableOperator().toStringTablePolicy(bo, dc);
            break;
         case TYPE_GUI_02_TABLE:
            gc.getTableOperator().toStringTableTech(bo, dc);
            break;
         case TYPE_GUI_06_CELL_POLICY:
            gc.getCellPolicy().toString(bo, dc, "Cell");
            break;
         case TYPE_GUI_01_SCROLLBAR:
            gc.getDrawableOperator().toStringTech(bo, dc);
            break;
         case TYPE_GUI_11_ANIMATION:
            gc.getAnimOperator().toStringAnim(bo, dc);
            break;
         case IBOTypesDrawX.TYPE_DRWX_07_STRING_AUX:
            return gc.getDrawableStringFactory().toStringStrAux(bo, dc);
         default:
            return false;
      }
      return true;
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, BOModuleGui.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

   public boolean toString1Line(Dctx dc, ByteObject bo) {
      int type = bo.getType();
      switch (type) {
         case TYPE_012_CTX_SETTINGS:
            gc.toStringGuiCtxTech1Line(dc, bo);
            break;
         case TYPE_GUI_00_VIEWPANE:
            gc.getDrawableCoreFactory().toStringViewPaneTech(bo, dc);
            break;
         case TYPE_GUI_05_CELL_SPANNING:
            gc.getTableOperator().toStringSpanning(bo, dc);
            break;
         case TYPE_GUI_04_TABLE_POLICY:
            gc.getTableOperator().toStringTablePolicy(bo, dc);
            break;
         case TYPE_GUI_02_TABLE:
            gc.getTableOperator().toStringTableTech(bo, dc);
            break;
         case TYPE_GUI_06_CELL_POLICY:
            gc.getCellPolicy().toString(bo, dc, "Cell");
            break;
         case TYPE_GUI_01_SCROLLBAR:
            gc.getDrawableOperator().toStringTech(bo, dc);
            break;
         case TYPE_GUI_11_ANIMATION:
            gc.getAnimOperator().toStringAnim1Line(bo, dc);
            break;
         case IBOTypesDrawX.TYPE_DRWX_07_STRING_AUX:
            return gc.getDrawableStringFactory().toStringStrAux1Line(bo, dc);
         default:
            return false;
      }
      return true;
   }

   public String toStringGetDIDString(int did, int value) {
      switch (did) {
         case DID_01_VIEWPANE_MOVE_TYPE:
            return ToStringStaticGui.toStringScrollMove(value);
         case DID_02_VIEWPANE_PARTIAL_TYPE:
            return ToStringStaticGui.toStringScrollPartial(value);
         case DID_04_VIEWPANE_VISUAL_TYPE:
            return ToStringStaticGui.toStringScrollVisual(value);
         case DID_03_VIEWPANE_SCROLL_TYPE:
            return ToStringStaticGui.toStringScrollbarMode(value);
         case DID_11_VIEWPANE_STYLE_TYPE:
            return ToStringStaticGui.toStringViewPaneStyleType(value);
         case DID_05_STRING_DRAWABLE_TYPE:
            return ToStringStaticGui.toStringStringPreset(value);
         case DID_09_CELL_POLICIES:
            return ToStringStaticGui.toStringPolicyType(value);
         case DID_12_DRAWABLE_CACHE_TYPE:
            return ToStringStaticGui.debugCacheType(value);
         case DID_13_VIEWPANE_SCROLL_COMPET_TYPE:
            return ToStringStaticGui.toStringCompetitionType(value);
         case DID_14_VIEWPANE_HEADER_COMPET_TYPE:
            return ToStringStaticGui.toStringCompetitionType(value);
         case DID_15_TABLEVIEW_PSELECT_MODE:
            return ToStringStaticGui.toStringPSelectMode(value);
         case DID_45_ANIM_MOVE_TYPE:
            return ToStringStaticBO.toStringMoveType(value);
         case DID_46_ANIM_INCREMENT_TYPE:
            return ToStringStaticBO.toStringMoveIncrementType(value);
         default:
            break;
      }
      return null;
   }

   public String toStringLink(int link) {
      return ToStringStaticGui.toStringLinkStatic(link);
   }

   public String toStringOffset(ByteObject bo, int offset) {
      int type = bo.getType();
      switch (type) {
         case TYPE_GUI_06_CELL_POLICY:
            if (offset == IBOCellPolicy.CELLP_OFFSET_01_TYPE1) {
               return "CellType";
            }
            break;
      }

      return null;
   }

   private void toStringPrivate(Dctx dc) {

   }

   public boolean toStringSubType(Dctx dc, ByteObject bo, int subType) {
      int type = bo.getType();
      if (type == IBOCanvasAppli.CANVAS_APP_BASE_TYPE) {
         if (subType == CANVAS_APP_TYPE_SUB_GUI) {
            //deal with wrong size
            dc.rootN(bo, "IBOCanvasAppliGui");
            dc.appendVarWithSpace("subType", subType);
            if (bo.getLength() < CANVAS_APP_DRW_BASIC_SIZE) {
               dc.append("Error: Size of ByteObject ");
               dc.append(bo.getLength());
               dc.append(" < ");
               dc.append(CANVAS_APP_DRW_BASIC_SIZE);
            } else {

               boolean isThumb = bo.hasFlag(CANVAS_APP_DRW_OFFSET_01_FLAG1, CANVAS_APP_DRW_FLAG_3_ONE_THUMB);
               boolean isUseMenuBar = bo.hasFlag(CANVAS_APP_DRW_OFFSET_01_FLAG1, CANVAS_APP_DRW_FLAG_4_USE_MENU_BAR);
               dc.appendVarWithNewLine("isThumb", isThumb);
               dc.appendVarWithSpace("isUseMenuBar", isUseMenuBar);

               int themeID = bo.get2(CANVAS_APP_DRW_OFFSET_02_VIEW_THEME_ID2);
               dc.appendVarWithNewLine("themeID", themeID);

               int debugMode = bo.get1(CANVAS_APP_DRW_OFFSET_07_DEBUG_MODE1);
               dc.appendVarWithSpace("debugMode", ToStringStaticGui.toStringDebugMode(debugMode));
               int debugBarPosition = bo.get1(CANVAS_APP_DRW_OFFSET_05_DEBUG_BAR_POSITION1);
               dc.appendVarWithSpace("debugBarPosition", ToStringStaticC.toStringPos(debugBarPosition));

               int menuBarPosition = bo.get1(CANVAS_APP_DRW_OFFSET_04_MENU_BAR_POSITION1);
               dc.appendVarWithNewLine("menuBarPosition", ToStringStaticC.toStringPos(menuBarPosition));

               return true;
            }
         }
      }
      return false;
   }

   public String toStringType(int type) {
      return ToStringStaticGui.toStringTypeBO(type);
   }
}
