package pasa.cbentley.framework.gui.src4.creator;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;

public class CreatorStyleClassSimple extends CreatorAbstractStyleClass {

   public CreatorStyleClassSimple(CreateContext gc) {
      super(gc);
   }

   protected CreatorAbstractScrollbar createCreatorScrollbar() {
      return new CreatorScrollbarSimple(crc);
   }

   protected CreatorAbstractString createCreatorString() {
      return new CreatorStringSimple(crc);
   }

   protected CreatorAbstractBOStyle createCreatorStyle() {
      return new CreatorBOStyleSimple(crc);
   }

   protected CreatorAbstractTable createCreatorTable() {
      return new CreatorTableSimple(crc);
   }
   public StyleClass getStyleClassFigure() {
      ByteObject style = creatorStyle.getStyleFigure();
      StyleClass sc = new StyleClass(gc, style);
      return sc;
   }
   protected CreatorAbstractView createCreatorView() {
      return new CreatorViewSimple(crc);
   }

   public StyleClass getStyleClassGradient() {
      ByteObject rootStyle = creatorStyle.getStyleRoot();
      StyleClass sc = new StyleClass(gc, rootStyle);
      populateStyleClass(sc);
      return sc;
   }

   public StyleClass getStyleClassHoleHeader() {
      return creatorView.getStyleClassViewPaneHoleHeader();
   }

   public StyleClass getStyleClassHoleScrollbar() {
      return creatorView.getStyleClassViewPaneHoleSB();
   }

   public StyleClass getStyleClassMenu() {
      ByteObject boStyle = creatorStyle.getStyleDrawableOnlyBorder();
      StyleClass sc = new StyleClass(gc, boStyle);

      ByteObject bo = creatorView.getBOMenu();
      sc.linkByteObject(bo, IBOTypesGui.LINK_75_MENU_BAR_TECH);

      return sc;
   }

   public StyleClass getStyleClassRoot() {
      ByteObject rootStyle = creatorStyle.getStyleRoot();
      StyleClass sc = new StyleClass(gc, rootStyle);
      populateStyleClass(sc);
      return sc;
   }

   public StyleClass getStyleClassScrollbarBlockBg() {
      return creatorScrollbar.getStyleClassBlockBgHoriz();
   }

   public StyleClass getStyleClassScrollbarBlockFigure() {
      return creatorScrollbar.getStyleClassBlockFigure();
   }

   public StyleClass getStyleClassScrollbarH() {
      return creatorScrollbar.getStyleClassScrollbarHoriz();
   }

   public StyleClass getStyleClassScrollbarV() {
      return creatorScrollbar.getStyleClassScrollbarVert();
   }

   public StyleClass getStyleClassScrollbarWrapperBotRight() {
      return creatorScrollbar.getStyleClassWrapperBotRight();
   }

   public StyleClass getStyleClassScrollbarWrapperTopLeft() {
      return creatorScrollbar.getStyleClassWrapperTopLeft();
   }

   public StyleClass getStyleClassTable() {
      return getStyleClassRoot();
   }

   public StyleClass getStyleClassViewPane() {
      ByteObject boStyle = creatorView.getBOStyleViewPane();
      StyleClass sc = new StyleClass(gc, boStyle);

      ByteObject bo = creatorView.getBOViewPane();
      sc.linkByteObject(bo, IBOTypesGui.LINK_66_BO_VIEWPANE);

      populateStyleClassViewPane(sc);

      return sc;
   }

   private StyleClass getStyleClassViewPort() {
      ByteObject boStyle = creatorView.getBOStyleViewPort();
      StyleClass sc = new StyleClass(gc, boStyle);
      sc.setName("ViewPort");
      return sc;
   }

   protected void populateStyleClass(StyleClass sc) {

      StyleClass scVp = getStyleClassViewPane();
      sc.linkStyleClass(scVp, IBOTypesGui.LINK_65_STYLE_VIEWPANE);

      populateStyleClassViewPane(sc);

      ByteObject tableTech = creatorTable.getTableTech();
      sc.linkByteObject(tableTech, IBOTypesGui.LINK_80_TECH_TABLE);

      ByteObject stringTech = creatorString.getTechString();
      sc.linkByteObject(stringTech, IBOTypesGui.LINK_41_BO_STRING_DRAWABLE);

      ByteObject stringEditTech = creatorString.getTechStringEdit();
      sc.linkByteObject(stringEditTech, IBOTypesGui.LINK_40_TECH_STRING_EDIT);

      ByteObject menuTech = creatorView.getBOMenu();
      sc.linkByteObject(menuTech, IBOTypesGui.LINK_75_MENU_BAR_TECH);

      StyleClass scHoleHeader = getStyleClassHoleHeader();
      sc.linkStyleClass(scHoleHeader, IBOTypesGui.LINK_58_STYLE_VIEWPANE_HOLE_HEADER);

      StyleClass scHoleSB = getStyleClassHoleScrollbar();
      sc.linkStyleClass(scHoleSB, IBOTypesGui.LINK_59_STYLE_VIEWPANE_HOLE_SB);

   }

   protected void populateStyleClassViewPane(StyleClass sc) {

      StyleClass scViewPort = getStyleClassViewPort();
      sc.linkStyleClass(scViewPort, IBOTypesGui.LINK_64_STYLE_VIEWPORT);

      StyleClass scScrollbarH = getStyleClassScrollbarH();
      sc.linkStyleClass(scScrollbarH, IBOTypesGui.LINK_71_STYLE_VIEWPANE_H_SCROLLBAR);

      StyleClass scScrollbarV = getStyleClassScrollbarV();
      sc.linkStyleClass(scScrollbarV, IBOTypesGui.LINK_72_STYLE_VIEWPANE_V_SCROLLBAR);

      StyleClass scHoleHeader = getStyleClassHoleHeader();
      sc.linkStyleClass(scHoleHeader, IBOTypesGui.LINK_58_STYLE_VIEWPANE_HOLE_HEADER);

      StyleClass scHoleSB = getStyleClassHoleScrollbar();
      sc.linkStyleClass(scHoleSB, IBOTypesGui.LINK_59_STYLE_VIEWPANE_HOLE_SB);

   }

}
