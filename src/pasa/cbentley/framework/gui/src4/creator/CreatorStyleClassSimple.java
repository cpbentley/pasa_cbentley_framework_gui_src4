package pasa.cbentley.framework.gui.src4.creator;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.tech.ITechLinks;

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

   protected CreatorAbstractView createCreatorView() {
      return new CreatorViewSimple(crc);
   }

   public StyleClass getStyleClassFigure() {
      ByteObject style = creatorStyle.getStyleFigure();
      StyleClass sc = new StyleClass(gc, style);
      return sc;
   }

   public StyleClass getStyleClassGradient() {
      ByteObject rootStyle = creatorStyle.getStyleRoot();
      StyleClass sc = new StyleClass(gc, rootStyle);
      populateStyleClassAll(sc);
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
      sc.linkByteObject(bo, ITechLinks.LINK_75_MENU_BAR_TECH);

      return sc;
   }

   public StyleClass getStyleClassRoot() {
      ByteObject rootStyle = creatorStyle.getStyleRoot();
      StyleClass sc = new StyleClass(gc, rootStyle);
      populateStyleClassAll(sc);
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

   public StyleClass getStyleClassSelected() {
      ByteObject style = creatorStyle.getStyleRoot();
      StyleClass sc = new StyleClass(gc, style);

      ByteObject styleSelected = creatorStyle.getStyleSelected();
      sc.linkStateStyle(styleSelected, ITechDrawable.STYLE_05_SELECTED);

      StyleClass scVp = getStyleClassViewPane();
      sc.linkStyleClass(scVp, ITechLinks.LINK_65_STYLE_VIEWPANE);
      populateStyleClassViewPane(scVp);

      return sc;
   }

   public StyleClass getStyleClassTable() {
      ByteObject style = creatorStyle.getStyleRoot();
      StyleClass sc = new StyleClass(gc, style);

      populateStyleClassString(sc);

      ByteObject tableTech = creatorTable.getBOTableViewNoTitles();
      sc.linkByteObject(tableTech, ITechLinks.LINK_80_BO_TABLEVIEW);

      StyleClass scVp = getStyleClassViewPane();
      sc.linkStyleClass(scVp, ITechLinks.LINK_65_STYLE_VIEWPANE);
      populateStyleClassViewPane(scVp);

      return sc;
   }

   public StyleClass getStyleClassTextNoPreset() {
      ByteObject style = creatorStyle.getStyleTextReaderH();
      StyleClass sc = new StyleClass(gc, style);

      ByteObject styleSelected = creatorStyle.getStyleSelected();
      sc.linkStateStyle(styleSelected, ITechDrawable.STYLE_05_SELECTED);

      ByteObject styleMarked = creatorStyle.getStyleMarked();
      sc.linkStateStyle(styleMarked, ITechDrawable.STYLE_03_MARKED);
      
      ByteObject styleGreyed = creatorStyle.getStyleGreyed();
      sc.linkStateStyle(styleGreyed, ITechDrawable.STYLE_04_GREYED);
      
      
      populateStyleClassStringNoPreset(sc);

      StyleClass scVp = getStyleClassViewPane();
      sc.linkStyleClass(scVp, ITechLinks.LINK_65_STYLE_VIEWPANE);
      populateStyleClassViewPane(scVp);
      return sc;
   }

   public StyleClass getStyleClassTextReader() {
      ByteObject style = creatorStyle.getStyleTextReaderH();
      StyleClass sc = new StyleClass(gc, style);

      populateStyleClassStringReader(sc);

      StyleClass scVp = getStyleClassViewPane();
      sc.linkStyleClass(scVp, ITechLinks.LINK_65_STYLE_VIEWPANE);
      populateStyleClassViewPane(scVp);
      return sc;
   }

   public StyleClass getStyleClassTextNaturalNoWrap() {
      ByteObject style = creatorStyle.getStyleTextReaderH();
      StyleClass sc = new StyleClass(gc, style);

      populateStyleClassStringReaderNaturalNoWrap(sc);

      StyleClass scVp = getStyleClassViewPane();
      sc.linkStyleClass(scVp, ITechLinks.LINK_65_STYLE_VIEWPANE);
      populateStyleClassViewPane(scVp);
      return sc;
   }

   public StyleClass getStyleClassTextHorizontal() {
      ByteObject style = creatorStyle.getStyleTextReaderH();
      StyleClass sc = new StyleClass(gc, style);

      populateStyleClassStringReaderH(sc);

      StyleClass scVp = getStyleClassViewPane();
      sc.linkStyleClass(scVp, ITechLinks.LINK_65_STYLE_VIEWPANE);
      populateStyleClassViewPane(scVp);
      return sc;
   }

   public StyleClass getStyleClassTitle() {
      ByteObject style = creatorStyle.getStyleTitle();
      StyleClass sc = new StyleClass(gc, style);

      ByteObject stringTech = creatorString.getBOStringDataTitle();
      sc.linkByteObject(stringTech, ITechLinks.LINK_41_BO_STRING_DATA);

      ByteObject stringEditTech = creatorString.getBOStringEdit();
      sc.linkByteObject(stringEditTech, ITechLinks.LINK_42_BO_STRING_EDIT);

      return sc;
   }

   public StyleClass getStyleClassFigureLosange() {
      ByteObject style = creatorStyle.getStyleFigureLosange();
      StyleClass sc = new StyleClass(gc, style);

      ByteObject stringTech = creatorString.getBOStringDataTitle();
      sc.linkByteObject(stringTech, ITechLinks.LINK_41_BO_STRING_DATA);

      ByteObject stringEditTech = creatorString.getBOStringEdit();
      sc.linkByteObject(stringEditTech, ITechLinks.LINK_42_BO_STRING_EDIT);

      return sc;
   }

   public StyleClass getStyleClassViewPane() {
      ByteObject boStyle = creatorView.getBOStyleViewPane();
      StyleClass sc = new StyleClass(gc, boStyle);

      ByteObject bo = creatorView.getBOViewPane();
      sc.linkByteObject(bo, ITechLinks.LINK_66_BO_VIEWPANE);

      populateStyleClassViewPane(sc);

      return sc;
   }

   private StyleClass getStyleClassViewPort() {
      ByteObject boStyle = creatorView.getBOStyleViewPort();
      StyleClass sc = new StyleClass(gc, boStyle);
      sc.setName("ViewPort");
      return sc;
   }

   protected void populateStyleClassAll(StyleClass sc) {
      populateStyleClassString(sc);
      populateStyleClassTables(sc);

      StyleClass scVp = getStyleClassViewPane();
      sc.linkStyleClass(scVp, ITechLinks.LINK_65_STYLE_VIEWPANE);

      populateStyleClassViewPane(scVp);

   }

   protected void populateStyleClassString(StyleClass sc) {
      ByteObject stringData = creatorString.getBOStringDataTitle();
      sc.linkByteObject(stringData, ITechLinks.LINK_41_BO_STRING_DATA);

      ByteObject stringEdit = creatorString.getBOStringEdit();
      sc.linkByteObject(stringEdit, ITechLinks.LINK_42_BO_STRING_EDIT);

   }

   protected void populateStyleClassStringReaderH(StyleClass sc) {
      ByteObject stringTech = creatorString.getBOStringDataReaderH();
      sc.linkByteObject(stringTech, ITechLinks.LINK_41_BO_STRING_DATA);

      ByteObject stringEditTech = creatorString.getBOStringEdit();
      sc.linkByteObject(stringEditTech, ITechLinks.LINK_42_BO_STRING_EDIT);

      ByteObject stringFigure = creatorString.getBOStringFigurePreset();
      sc.linkByteObject(stringFigure, ITechLinks.LINK_40_BO_STRING_FIGURE);
   }

   protected void populateStyleClassStringReaderNaturalNoWrap(StyleClass sc) {
      ByteObject stringTech = creatorString.getBOStringDataReaderNaturalNoWrap();
      sc.linkByteObject(stringTech, ITechLinks.LINK_41_BO_STRING_DATA);

      ByteObject stringEditTech = creatorString.getBOStringEdit();
      sc.linkByteObject(stringEditTech, ITechLinks.LINK_42_BO_STRING_EDIT);

      ByteObject stringFigure = creatorString.getBOStringFigurePreset();
      sc.linkByteObject(stringFigure, ITechLinks.LINK_40_BO_STRING_FIGURE);
   }

   protected void populateStyleClassStringReader(StyleClass sc) {
      ByteObject stringTech = creatorString.getBOStringDataReader();
      sc.linkByteObject(stringTech, ITechLinks.LINK_41_BO_STRING_DATA);
      ByteObject stringEditTech = creatorString.getBOStringEdit();
      sc.linkByteObject(stringEditTech, ITechLinks.LINK_42_BO_STRING_EDIT);

      ByteObject stringFigure = creatorString.getBOStringFigureScrollReader();
      sc.linkByteObject(stringFigure, ITechLinks.LINK_40_BO_STRING_FIGURE);
   }

   protected void populateStyleClassStringNoPreset(StyleClass sc) {
      ByteObject stringTech = creatorString.getBOStringDataNoPreset();
      sc.linkByteObject(stringTech, ITechLinks.LINK_41_BO_STRING_DATA);
      ByteObject stringEditTech = creatorString.getBOStringEdit();
      sc.linkByteObject(stringEditTech, ITechLinks.LINK_42_BO_STRING_EDIT);

      ByteObject stringFigure = creatorString.getBOStringFigure();
      sc.linkByteObject(stringFigure, ITechLinks.LINK_40_BO_STRING_FIGURE);
   }

   private void populateStyleClassTables(StyleClass sc) {
      ByteObject tableTech = creatorTable.getTableTech();
      sc.linkByteObject(tableTech, ITechLinks.LINK_80_BO_TABLEVIEW);

      ByteObject menuTech = creatorView.getBOMenu();
      sc.linkByteObject(menuTech, ITechLinks.LINK_75_MENU_BAR_TECH);
   }

   protected void populateStyleClassViewPane(StyleClass sc) {

      StyleClass scViewPort = getStyleClassViewPort();
      sc.linkStyleClass(scViewPort, ITechLinks.LINK_64_STYLE_VIEWPORT);

      StyleClass scScrollbarH = getStyleClassScrollbarH();
      sc.linkStyleClass(scScrollbarH, ITechLinks.LINK_71_STYLE_VIEWPANE_H_SCROLLBAR);

      StyleClass scScrollbarV = getStyleClassScrollbarV();
      sc.linkStyleClass(scScrollbarV, ITechLinks.LINK_72_STYLE_VIEWPANE_V_SCROLLBAR);

      StyleClass scHoleHeader = getStyleClassHoleHeader();
      sc.linkStyleClass(scHoleHeader, ITechLinks.LINK_58_STYLE_VIEWPANE_HOLE_HEADER);

      StyleClass scHoleSB = getStyleClassHoleScrollbar();
      sc.linkStyleClass(scHoleSB, ITechLinks.LINK_59_STYLE_VIEWPANE_HOLE_SB);

   }

}
