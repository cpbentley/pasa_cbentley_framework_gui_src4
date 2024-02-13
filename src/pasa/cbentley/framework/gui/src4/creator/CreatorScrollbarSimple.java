package pasa.cbentley.framework.gui.src4.creator;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOScrollBar;

/**
 * Creators of anything related Scroll
 * @author Charles Bentley
 *
 */
public class CreatorScrollbarSimple extends CreatorAbstractScrollbar {

   public CreatorScrollbarSimple(CreateContext gc) {
      super(gc);
   }

   protected CreatorAbstractBOStyle createCreatorStyle() {
      return new CreatorBOStyleSimple(crc);
   }

   /**
    * Create the triangle figure object for the figure
    * @return
    */
   public ByteObject getBOFigWrapper() {
      int color = getColorSetActive().getContent1();
      ByteObject figGrad = getGradientTrigSB();
      ByteObject trig = figureFactory.getFigTriangleType(color, C.DIR_0_TOP, figGrad);
      return trig;
   }

   protected ByteObject getBOScrollbar(boolean vertical) {
      ByteObject boSB = new ByteObject(boc, IBOTypesGui.TYPE_102_SCROLLBAR_TECH, SB_BASIC_SIZE);
      int scrollbar1Size = 16;
      int scrollbar2Size = 15;
      int scrollbar3Size = 10;
      boSB.setValue(SB_OFFSET_03_PRIMARY_SIZE4, scrollbar1Size, 4);
      boSB.setValue(SB_OFFSET_04_SECOND_SIZE4, scrollbar2Size, 4);
      boSB.setValue(SB_OFFSET_05_THIRD_SIZE4, scrollbar3Size, 4);
      boSB.setValue(SB_OFFSET_06_MIN_SIZE4, 10, 4);
      boSB.setFlag(SB_OFFSET_01_FLAG, SB_FLAG_1_VERT, vertical);
      boSB.setFlag(SB_OFFSET_01_FLAG, SB_FLAG_3_WRAPPER, isWrapper);
      boSB.setFlag(SB_OFFSET_01_FLAG, SB_FLAG_4_WRAPPER_FILL, isWrapperFill);
      boSB.setFlag(SB_OFFSET_01_FLAG, SB_FLAG_5_ARROW_ON_BLOC, true);
      boSB.setFlag(SB_OFFSET_01_FLAG, SB_FLAG_6_BLOCK_INVERSE, false);
      boSB.setFlag(SB_OFFSET_01_FLAG, SB_FLAG_7_AROUND_THE_CLOCK, false);
      boSB.setFlag(SB_OFFSET_01_FLAG, SB_FLAG_8_SINGLE_STYLECLASS, false);

      return boSB;
   }

   protected ByteObject getBOScrollbarH() {
      return getBOScrollbar(false);
   }

   public ByteObject getBOScrollbarHorizontal() {
      return getBOScrollbar(true);
   }

   public ByteObject getBOScrollbarVertical() {
      return getBOScrollbar(true);
   }

   public ByteObject getBOStyleBlockBg() {
      ByteObject style = creatorStyle.getStyleGradientBgHorizBorder();
      return style;
   }

   public ByteObject getBOStyleBlockFigure() {
      ByteObject style = creatorStyle.getStyleBorderPad();
      return style;
   }

   public ByteObject getBOStyleScrollbarV() {
      ByteObject style = creatorStyle.getStyleScrollbarV();
      return style;
   }

   public ByteObject getBOStyleScrollbarH() {
      ByteObject style = creatorStyle.getStyleScrollbarH();
      return style;
   }

   public ByteObject getBOStyleWrapperBotRight() {
      ByteObject style = creatorStyle.getStyleBorderPad();
      return style;
   }

   public ByteObject getBOStyleWrapperTopLeft() {
      ByteObject style = creatorStyle.getStyleBorderPad();
      return style;
   }

   /**
    * Returns a gradient object is when a gradient has been defined
    * @return
    */
   public ByteObject getGradientTrigSB() {
      ByteObject gradTrig = null;

      int scolor = getColorSetActive().getContent2();
      int sec = 0;
      int step = 2; //TODO must be a function of available size
      int type = GRADIENT_TYPE_TRIG_01_TENT_JESUS;

      boolean has3rdColor = true;
      ByteObject tcolor = null;
      if (has3rdColor) {
         tcolor = litteralFactory.getLitteralInt(getColorSetActive().getContent3());
      }
      gradTrig = gradFactory.getGradient(scolor, sec, type, step, tcolor);
      return gradTrig;
   }

   public StyleClass getStyleClassBlockBgHoriz() {
      ByteObject boStyle = creatorStyle.getStyleGradientBgHorizBorder();
      StyleClass sc = new StyleClass(gc, boStyle);
      return sc;
   }

   public StyleClass getStyleClassBlockBgVert() {
      ByteObject boStyle = creatorStyle.getStyleGradientBgVertBorder();
      StyleClass sc = new StyleClass(gc, boStyle);
      return sc;
   }

   public StyleClass getStyleClassBlockFigure() {
      ByteObject boStyle = getBOStyleBlockFigure();
      StyleClass sc = new StyleClass(gc, boStyle);
      return sc;
   }

   public StyleClass getStyleClassScrollbarHoriz() {
      ByteObject boStyle = getBOStyleScrollbarH();
      StyleClass sc = new StyleClass(gc, boStyle);

      ByteObject bo = getBOScrollbarH();
      sc.linkByteObject(bo, IBOTypesGui.LINK_69_BO_H_SCROLLBAR);

      ByteObject boFigure = getBOFigWrapper();
      sc.linkByteObject(boFigure, IBOTypesGui.LINK_49_FIG_SCROLLBAR_WRAPPER);

      StyleClass sbBlockBg = getStyleClassBlockBgHoriz();
      sc.linkStyleClass(sbBlockBg, IBOTypesGui.LINK_50_STYLE_SCROLLBAR_BLOCK_BG);

      StyleClass sbBlockFig = getStyleClassBlockBgHoriz();
      sc.linkStyleClass(sbBlockFig, IBOTypesGui.LINK_51_STYLE_SCROLLBAR_BLOCK_FIG);

      StyleClass sbWrapperTopLeft = getStyleClassWrapperTopLeft();
      sc.linkStyleClass(sbWrapperTopLeft, IBOTypesGui.LINK_52_STYLE_SCROLLBAR_TOP_LEFT_WRAPPER);

      StyleClass sbWrapperBotRight = getStyleClassWrapperBotRight();
      sc.linkStyleClass(sbWrapperBotRight, IBOTypesGui.LINK_53_STYLE_SCROLLBAR_BOT_RIGHT_WRAPPER);

      return sc;
   }

   public StyleClass getStyleClassScrollbarVert() {
      ByteObject boStyle = getBOStyleScrollbarV();
      StyleClass sc = new StyleClass(gc, boStyle);

      ByteObject bo = getBOScrollbarVertical();
      sc.linkByteObject(bo, IBOTypesGui.LINK_68_BO_V_SCROLLBAR);

      ByteObject boFigure = getBOFigWrapper();
      sc.linkByteObject(boFigure, IBOTypesGui.LINK_49_FIG_SCROLLBAR_WRAPPER);

      StyleClass sbBlockBg = getStyleClassBlockBgVert();
      sc.linkStyleClass(sbBlockBg, IBOTypesGui.LINK_50_STYLE_SCROLLBAR_BLOCK_BG);

      StyleClass sbBlockFig = getStyleClassBlockBgVert();
      sc.linkStyleClass(sbBlockFig, IBOTypesGui.LINK_51_STYLE_SCROLLBAR_BLOCK_FIG);

      StyleClass sbWrapperTopLeft = getStyleClassWrapperTopLeft();
      sc.linkStyleClass(sbWrapperTopLeft, IBOTypesGui.LINK_52_STYLE_SCROLLBAR_TOP_LEFT_WRAPPER);

      StyleClass sbWrapperBotRight = getStyleClassWrapperBotRight();
      sc.linkStyleClass(sbWrapperBotRight, IBOTypesGui.LINK_53_STYLE_SCROLLBAR_BOT_RIGHT_WRAPPER);

      return sc;
   }

   public StyleClass getStyleClassWrapperBotRight() {
      ByteObject boStyle = getBOStyleWrapperBotRight();
      StyleClass sc = new StyleClass(gc, boStyle);
      return sc;
   }

   public StyleClass getStyleClassWrapperTopLeft() {
      ByteObject boStyle = getBOStyleWrapperTopLeft();
      StyleClass sc = new StyleClass(gc, boStyle);
      return sc;
   }

}
