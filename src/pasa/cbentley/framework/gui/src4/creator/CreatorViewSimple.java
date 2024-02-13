package pasa.cbentley.framework.gui.src4.creator;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOViewPane;

public class CreatorViewSimple extends CreatorAbstractView  {

   public CreatorViewSimple(CreateContext gc) {
      super(gc);
   }

   public ByteObject getBOViewPane() {
      ByteObject boViewPane = dFac.getDefViewPaneTech();
      boViewPane.set1(VP_OFFSET_11_COMPETITION_HEADER_TYPE1, COMPET_HEADER_0_NEUTRAL);
      boViewPane.setFlag(VP_OFFSET_01_FLAG, VP_FLAG_1_DATA_FROM_VIEW, false);
      boViewPane.setFlag(VP_OFFSET_01_FLAG, VP_FLAG_2_SCROLLBAR_ALWAYS, false);
      boViewPane.setFlag(VP_OFFSET_01_FLAG, VP_FLAG_3_SCROLLBAR_MASTER, false);
      
      boViewPane.setFlag(VP_OFFSET_02_FLAGX, VP_FLAGX_1_STYLE_VIEWPANE, true);
      boViewPane.setFlag(VP_OFFSET_02_FLAGX, VP_FLAGX_2_STYLE_VIEWPORT, false);
      boViewPane.setFlag(VP_OFFSET_02_FLAGX, VP_FLAGX_3_STYLE_CONTENT, false);
      
      //#debug
      boViewPane.toStringSetName("CreatorViewSimple_DefViewPaneTech");
      return boViewPane;
   }

   public StyleClass getStyleClassMenu() {
      ByteObject boStyle = creatorStyle.getStyleDrawableOnlyBorder();
      StyleClass sc = new StyleClass(gc, boStyle);

      ByteObject bo = getBOMenu();
      sc.linkByteObject(bo, IBOTypesGui.LINK_75_MENU_BAR_TECH);

      return sc;
   }

   protected CreatorAbstractBOStyle createCreatorStyle() {
      return new CreatorBOStyleSimple(crc);
   }

   public ByteObject getBOMenu() {
      ByteObject tech = new ByteObject(boc, new byte[MENUS_BASIC_SIZE], 0);
      int menuPosition = C.DIR_0_TOP;
      int showMode = SHOW_MODE_0_ALWAYS_ON;
      tech.set1(MENUS_OFFSET_02_POSITION1, menuPosition);
      tech.set1(MENUS_OFFSET_03_SHOW_MODE1, showMode);
      return tech;
   }

   public StyleClass getStyleClassViewPaneHoleHeader() {
      ByteObject boStyle = creatorStyle.getStyleDrawableOnlyBorder();
      StyleClass sc = new StyleClass(gc, boStyle);
      return sc;
   }

   public StyleClass getStyleClassViewPaneHoleSB() {
      ByteObject boStyle = creatorStyle.getStyleHole();
      StyleClass sc = new StyleClass(gc, boStyle);
      return sc;
   }

   public ByteObject getBOStyleViewPane() {
      return creatorStyle.getStyleBorderSmallOnly();
   }

   public ByteObject getBOStyleViewPort() {
      return creatorStyle.getStyle111();
   }
}
