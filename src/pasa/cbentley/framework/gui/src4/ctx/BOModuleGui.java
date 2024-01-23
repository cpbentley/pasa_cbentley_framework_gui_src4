package pasa.cbentley.framework.gui.src4.ctx;

import pasa.cbentley.byteobjects.src4.core.BOModuleAbstract;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDebugStringable;
import pasa.cbentley.core.src4.logging.ToStringStaticC;
import pasa.cbentley.framework.gui.src4.canvas.ITechCanvasAppliDrawable;
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
public class BOModuleGui extends BOModuleAbstract implements IBOTypesGui, ITechCanvasAppliDrawable, IDebugStringable, IToStringsDIDGui, ITechCanvasDrawable, ITechCtxSettingsAppGui {

   public static final int BIP_CLASS_SC  = 3;

   public static final int BIP_MODULE_ID = 2;

   protected final GuiCtx  gc;

   public BOModuleGui(GuiCtx gc) {
      super(gc.getBOC());
      this.gc = gc;
      //#debug
      toDLog().pInit("", null, BOModuleGui.class, "Constructor", LVL_04_FINER, true);
   }

   public boolean toStringSubType(Dctx dc, ByteObject bo, int subType) {
      int type = bo.getType();
      if (type == IBOCanvasAppli.CANVAS_APP_TYPE) {
         if (subType == CANVAS_APP_DRW_1_TYPE_DRAWABLE) {
            //deal with wrong size
            dc.rootN(bo, "ITechCanvasAppliDrawable");
            dc.appendVarWithSpace("subType", subType);
            if (bo.getLength() < CANVAS_APP_DRW_BASIC_SIZE) {
               dc.append("Error: Size of ByteObject ");
               dc.append(bo.getLength());
               dc.append(" < ");
               dc.append(CANVAS_APP_DRW_BASIC_SIZE);
            } else {
               
               boolean isThumb = bo.hasFlag(CANVAS_APP_DRW_OFFSET_01_FLAG1, CANVAS_APP_DRW_FLAG_3_ONE_THUMB);
               boolean isUseMenuBar = bo.hasFlag(CANVAS_APP_DRW_OFFSET_01_FLAG1, CANVAS_APP_DRW_FLAG_4_USE_MENU_BAR);
               dc.appendVarWithSpace("isThumb", isThumb);
               dc.appendVarWithSpace("isUseMenuBar", isUseMenuBar);

               int themeID = bo.get2(CANVAS_APP_DRW_OFFSET_02_VIEW_THEME_ID2);
               dc.appendVarWithSpace("themeID", themeID);

               int debugMode = bo.get1(CANVAS_APP_DRW_OFFSET_07_DEBUG_MODE1);
               dc.appendVarWithSpace("debugMode", ToStringStaticGui.toStringDebugMode(debugMode));
               int debugBarPosition = bo.get1(CANVAS_APP_DRW_OFFSET_05_DEBUG_BAR_POSITION1);
               dc.appendVarWithSpace("debugBarPosition", ToStringStaticC.toStringPos(debugBarPosition));
               
               int menuBarPosition = bo.get1(CANVAS_APP_DRW_OFFSET_04_MENU_BAR_POSITION1);
               dc.appendVarWithSpace("menuBarPosition", ToStringStaticC.toStringPos(menuBarPosition));
               
               
               return true;
            }
         }
      }
      return false;
   }

   public String toStringGetDIDString(int did, int value) {
      switch (did) {
         case DID_01_VIEWPANE_MOVE_TYPE:
            return ToStringStaticGui.debugScrollMove(value);
         case DID_02_VIEWPANE_PARTIAL_TYPE:
            return ToStringStaticGui.debugScrollPartial(value);
         case DID_04_VIEWPANE_VISUAL_TYPE:
            return ToStringStaticGui.debugScrollVisual(value);
         case DID_03_VIEWPANE_SCROLL_TYPE:
            return ToStringStaticGui.debugScrollType(value);
         case DID_11_VIEWPANE_STYLE_TYPE:
            return ToStringStaticGui.toStringViewPaneStyleType(value);
         case DID_05_STRING_DRAWABLE_TYPE:
            return ToStringStaticGui.debugStringType(value);
         case DID_09_CELL_POLICIES:
            return ToStringStaticGui.toStringPolicyType(value);
         case DID_12_DRAWABLE_CACHE_TYPE:
            return ToStringStaticGui.debugCacheType(value);
         case DID_13_VIEWPANE_SCROLL_COMPET_TYPE:
            return ToStringStaticGui.debugCompetType(value);
         case DID_14_VIEWPANE_HEADER_COMPET_TYPE:
            return ToStringStaticGui.debugCompetType(value);
         case DID_15_TABLEVIEW_PSELECT_MODE:
            return ToStringStaticGui.toStringPSelectMode(value);
         case DID_45_ANIM_MOVE_TYPE:
            return ToStringStaticGui.toStringMoveType(value);
         case DID_46_ANIM_INCREMENT_TYPE:
            return ToStringStaticGui.toStringMoveIncrementType(value);
         default:
            break;
      }
      return null;
   }

   public ByteObject merge(ByteObject root, ByteObject merge) {
      int type = merge.getType();
      switch (type) {
         case TYPE_121_SPANNING:

            break;
         case TYPE_120_TABLE_POLICY:

            break;
      }
      return null;
   }

   public String toStringLink(int link) {
      return ToStringStaticGui.toStringLinkStatic(link);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, BOModuleGui.class, "@line5");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, BOModuleGui.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

   /**
    * Displays a name of the offset field. Reflection on the field.
    * <br>
    * @param type
    * @return
    */
   public String subToStringOffset(ByteObject o, int offset) {
      int type = o.getType();
      switch (type) {
         case TYPE_121_SPANNING:
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
         case TYPE_121_SPANNING:
            return "SPAN";
         case TYPE_120_TABLE_POLICY:
            return "TablePolicy";
         case TYPE_122_CELL_POLICY:
            return "CellPolicy";
         default:
            return null;
      }
   }

   public ByteObject getFlagOrderedBO(ByteObject bo, int offset, int flag) {
      // TODO Auto-generated method stub
      return null;
   }

   public boolean toString(Dctx dc, ByteObject bo) {
      int type = bo.getType();
      switch (type) {
         case TYPE_012_CTX_SETTINGS:
            //check if our sub type else drop it
            gc.toStringGuiCtxTech(dc, bo);
            break;
         case TYPE_101_VIEWPANE_TECH:
            gc.getDrawableCoreFactory().toStringViewPaneTech(bo, dc);
            break;
         case TYPE_121_SPANNING:
            gc.getTableOperator().toStringSpanning(bo, dc);
            break;
         case TYPE_120_TABLE_POLICY:
            gc.getTableOperator().toStringTablePolicy(bo, dc);
            break;
         case TYPE_103_TABLE_TECH:
            gc.getTableOperator().toStringTableTech(bo, dc);
            break;
         case TYPE_122_CELL_POLICY:
            gc.getCellPolicy().toString(bo, dc, "Cell");
            break;
         case TYPE_102_SCROLLBAR_TECH:
            gc.getDrawableOperator().toStringTech(bo, dc);
            break;
         case TYPE_130_ANIMATION:
            gc.getAnimOperator().toStringAnim(bo, dc);
            break;
         case TYPE_124_STRING_TECH:
            gc.getDrawableStringOperator().toStringTech(bo, dc);
            break;
         case TYPE_125_STRING_EDIT_TECH:
            gc.getDrawableStringOperator().toStringTechEdit(bo, dc);
            break;
         default:
            return false;
      }
      return true;
   }

   public boolean toString1Line(Dctx dc, ByteObject bo) {
      int type = bo.getType();
      switch (type) {
         case TYPE_012_CTX_SETTINGS:
            gc.toStringGuiCtxTech1Line(dc, bo);
            break;
         case TYPE_101_VIEWPANE_TECH:
            gc.getDrawableCoreFactory().toStringViewPaneTech(bo, dc);
            break;
         case TYPE_121_SPANNING:
            gc.getTableOperator().toStringSpanning(bo, dc);
            break;
         case TYPE_120_TABLE_POLICY:
            gc.getTableOperator().toStringTablePolicy(bo, dc);
            break;
         case TYPE_103_TABLE_TECH:
            gc.getTableOperator().toStringTableTech(bo, dc);
            break;
         case TYPE_122_CELL_POLICY:
            gc.getCellPolicy().toString(bo, dc, "Cell");
            break;
         case TYPE_102_SCROLLBAR_TECH:
            gc.getDrawableOperator().toStringTech(bo, dc);
            break;
         case TYPE_130_ANIMATION:
            gc.getAnimOperator().toStringAnim1Line(bo, dc);
            break;
         case TYPE_124_STRING_TECH:
            gc.getDrawableStringOperator().toStringTech(bo, dc);
            break;
         case TYPE_125_STRING_EDIT_TECH:
            gc.getDrawableStringOperator().toStringTechEdit(bo, dc);
            break;
         default:
            return false;
      }
      return true;
   }

   public String toStringOffset(ByteObject bo, int offset) {
      int type = bo.getType();
      switch (type) {
         case TYPE_122_CELL_POLICY:
            if (offset == IBOCellPolicy.OFFSET_01_TYPE1) {
               return "CellType";
            }
            break;
      }

      return null;
   }

   public String toStringType(int type) {
      return ToStringStaticGui.toStringTypeBO(type);
   }
}
