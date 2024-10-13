package pasa.cbentley.framework.gui.src4.creator;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.objects.color.ColorRepo;
import pasa.cbentley.byteobjects.src4.objects.color.ColorSet;
import pasa.cbentley.byteobjects.src4.objects.color.ITechGradient;
import pasa.cbentley.byteobjects.src4.objects.pointer.MergeFactory;
import pasa.cbentley.core.src4.utils.ColorUtils;
import pasa.cbentley.framework.coredraw.src4.interfaces.ITechFont;
import pasa.cbentley.framework.drawx.src4.string.interfaces.IBOFigString;
import pasa.cbentley.framework.drawx.src4.style.IBOStyle;
import pasa.cbentley.framework.drawx.src4.tech.ITechStyle;
import pasa.cbentley.layouter.src4.ctx.IBOTypesLayout;
import pasa.cbentley.layouter.src4.tech.ITechLayout;

public class CreatorBOStyleSimple extends CreatorAbstractBOStyle {

   public CreatorBOStyleSimple(CreateContext gc) {
      super(gc);
   }

   public ByteObject getStyle111() {
      ColorSet cs = getColorSetActive();
      int colorBg1 = cs.getBg1();
      int colorBgGrad = cs.getBg2();
      int colorBorder1 = cs.getBorder1();
      int colorFont = cs.getFont1();

      ByteObject bg1Rect = figureFac.getFigArlequin(colorBg1, colorBgGrad);

      ByteObject marginTBLR = tblrFac.getTBLRPixel(1);
      ByteObject borderTBLR = marginTBLR;
      ByteObject paddinTBLR = marginTBLR;

      ByteObject bg2Border = figureFac.getFigBorder(borderTBLR, colorBorder1);
      ByteObject anchor = anchorFac.getCenterCenter();
      ByteObject content = figureFac.getFigString(FACE_01_MONOSPACE, STYLE_0_PLAIN, SIZE_4_LARGE, colorFont);
      ByteObject style = styleFac.getStyle(new ByteObject[] { bg1Rect, bg2Border }, content, anchor, paddinTBLR, borderTBLR, marginTBLR);
      style.toStringSetName("Style111");
      return style;
   }

   public ByteObject getStyleBorderBgXCenterYCenter() {

      boolean horiz = true;

      ByteObject text = figureFactory.getFigString(FACE_00_SYSTEM, STYLE_0_PLAIN, ITechFont.SIZE_3_MEDIUM, 0);
      ByteObject anchor = anchorFac.getCenterCenter();
      ColorRepo colorRepo = getRepo();
      int pcolor = colorRepo.getBg1();
      int scolor = colorRepo.getBg2();
      ByteObject bg = figureFactory.getFigRectGrad(horiz, pcolor, scolor, 0);
      ByteObject pad = tblrFactory.getTBLRCoded(2);
      ByteObject margin = tblrFactory.getTBLRCoded(1);

      int borderColor1 = colorRepo.getBorder1();
      int borderColor2 = colorRepo.getBorder2();
      ByteObject figBorder = figureFactory.getFigBorder(5, borderColor1, borderColor2, 100);
      ByteObject border = figBorder.getSubFirst(IBOTypesLayout.FTYPE_2_TBLR);

      ByteObject[] bgs = new ByteObject[] { bg, figBorder };

      ByteObject style = styleFactory.getStyle(bgs, text, anchor, pad, border, margin);
      return style;
   }

   public ByteObject getStyleBorderPad() {
      ColorSet cs = getColorSetActive();
      int colorBg1 = cs.getBg1();
      int colorBgGrad = cs.getBg2();
      int colorBorder1 = cs.getBorder1();
      int colorFont = cs.getFont1();

      ByteObject bgGradient = gradFac.getGradient(colorBgGrad, 100, ITechGradient.GRADIENT_TYPE_RECT_00_SQUARE);
      ByteObject bg1Rect = figureFac.getFigRect(colorBg1, bgGradient);

      ByteObject sizerBorder = sizerFactory.getSizerXPixelForYPixel(1, 400, ITechLayout.ET_FUN_04_MAX);
      ByteObject sizerPaddin = sizerFactory.getSizerXPixelForYPixel(1, 300, ITechLayout.ET_FUN_04_MAX);

      ByteObject marginTBLR = null;
      ByteObject borderTBLR = tblrFac.getTBLRSizer(sizerBorder);
      ByteObject paddinTBLR = tblrFac.getTBLRSizer(sizerPaddin);

      ByteObject bg2Border = figureFac.getFigBorder(sizerBorder, colorBorder1);
      ByteObject anchor = anchorFac.getCenterCenter();
      ByteObject content = figureFac.getFigString(FACE_01_MONOSPACE, STYLE_0_PLAIN, SIZE_4_LARGE, colorFont);
      ByteObject style = styleFac.getStyle(new ByteObject[] { bg1Rect, bg2Border }, content, anchor, paddinTBLR, borderTBLR, marginTBLR);
      style.toStringSetName("StyleBorderPad");
      return style;
   }

   public ByteObject getStyleBorderSmallOnly() {
      ColorSet cs = getColorSetActive();
      int colorBg1 = cs.getBg1();
      int colorBgGrad = cs.getBg2();
      int colorBg3 = cs.getBg3();
      int colorBorder1 = cs.getBorder1();
      int colorFont = cs.getFont1();

      int step = 3;
      int type = ITechGradient.GRADIENT_TYPE_RECT_01_HORIZ;

      ByteObject bgGradient = gradFac.getGradient(colorBgGrad, 50, type, step, colorBg3);
      ByteObject bg1Rect = figureFac.getFigRect(colorBg1, bgGradient);

      ByteObject sizerBorder = sizerFactory.getSizerXPixelForYPixel(1, 200, ITechLayout.ET_FUN_04_MAX);

      ByteObject marginTBLR = null;
      ByteObject borderTBLR = tblrFac.getTBLRSizer(sizerBorder);
      ByteObject paddinTBLR = null;

      ByteObject bg2Border = figureFac.getFigBorder(sizerBorder, colorBorder1);
      ByteObject anchor = anchorFac.getCenterCenter();
      ByteObject content = figureFac.getFigString(FACE_01_MONOSPACE, STYLE_0_PLAIN, SIZE_4_LARGE, colorFont);
      ByteObject style = styleFac.getStyle(new ByteObject[] { bg1Rect, bg2Border }, content, anchor, paddinTBLR, borderTBLR, marginTBLR);
      style.toStringSetName("StyleBorderSmallOnly");
      return style;
   }

   public ByteObject getStyleDrawableBig() {
      ColorSet cs = getColorSetActive();
      int colorBg1 = cs.getBg1();
      int colorBgGrad = cs.getBg2();
      int colorBorder1 = cs.getBorder1();
      int colorFont = cs.getFont1();

      ByteObject bgGradient = gradFac.getGradient(colorBgGrad, 100, ITechGradient.GRADIENT_TYPE_RECT_00_SQUARE);
      ByteObject bg1Rect = figureFac.getFigRect(colorBg1, bgGradient);

      ByteObject sizerMargin = sizerFactory.getSizerXPixelForYPixel(1, 30, ITechLayout.ET_FUN_04_MAX);
      ByteObject sizerBorder = sizerFactory.getSizerXPixelForYPixel(1, 40, ITechLayout.ET_FUN_03_MIN);
      ByteObject sizerPaddin = sizerFactory.getSizerXPixelForYPixel(1, 30, ITechLayout.ET_FUN_04_MAX);

      ByteObject marginTBLR = tblrFac.getTBLRSizer(sizerMargin);
      ByteObject borderTBLR = tblrFac.getTBLRSizer(sizerBorder);
      ByteObject paddinTBLR = tblrFac.getTBLRSizer(sizerPaddin);

      ByteObject bg2Border = figureFac.getFigBorder(sizerBorder, colorBorder1);
      ByteObject anchor = anchorFac.getCenterCenter();
      ByteObject content = figureFac.getFigString(FACE_01_MONOSPACE, STYLE_0_PLAIN, SIZE_4_LARGE, colorFont);
      ByteObject style = styleFac.getStyle(new ByteObject[] { bg1Rect, bg2Border }, content, anchor, paddinTBLR, borderTBLR, marginTBLR);
      style.toStringSetName("StyleDrawableBig");
      return style;
   }

   public ByteObject getStyleDrawableOnlyBorder() {
      ColorSet cs = getColorSetActive();

      int colorBgGrad = cs.getBg2();
      int colorBorder1 = cs.getBorder1();
      int colorFont = cs.getFont1();

      ByteObject bgGradient = gradFac.getGradient(colorBgGrad, 100, ITechGradient.GRADIENT_TYPE_RECT_00_SQUARE);

      ByteObject sizerBorder = sizerFactory.getSizerXPixelForYPixel(1, 20, ITechLayout.ET_FUN_03_MIN);

      ByteObject marginTBLR = null;
      ByteObject borderTBLR = tblrFac.getTBLRSizer(sizerBorder);
      ByteObject paddinTBLR = null;

      //arcw is size of tblr
      ByteObject bg2Border = figureFac.getFigBorder(borderTBLR, 10, 10, colorBorder1, bgGradient);
      bg2Border.toStringSetName("border2StyleOnlyBorder");
      ByteObject bg1Rect = figureFac.getFigRect(cs.getBg1(), bgGradient);

      ByteObject anchor = anchorFac.getCenterCenter();
      ByteObject content = figureFac.getFigString(FACE_01_MONOSPACE, STYLE_0_PLAIN, SIZE_4_LARGE, colorFont);
      ByteObject style = styleFac.getStyle(new ByteObject[] { bg1Rect, bg2Border }, content, anchor, paddinTBLR, borderTBLR, marginTBLR);
      style.toStringSetName("StyleDrawableOnlyBorder");
      return style;
   }

   public ByteObject getStyleFigure() {
      ColorSet cs = getColorSetActive();
      int colorBg1 = cs.getBg1();
      int colorBgGrad = cs.getBg2();
      int colorBorder1 = cs.getBorder1();
      int colorFont = cs.getFont1();

      ByteObject bg1Rect = figureFac.getFigRect(colorBg1);

      ByteObject marginTBLR = null;
      ByteObject borderTBLR = tblrFac.getTBLRPixel(2);
      ByteObject paddinTBLR = tblrFac.getTBLRPixel(1);

      ByteObject bg2Border = figureFac.getFigBorder(borderTBLR, colorBorder1);
      ByteObject anchor = anchorFac.getCenterCenter();
      ByteObject content = figureFac.getFigString(FACE_01_MONOSPACE, STYLE_0_PLAIN, SIZE_4_LARGE, colorFont);
      ByteObject style = styleFac.getStyle(new ByteObject[] { bg1Rect, bg2Border }, content, anchor, paddinTBLR, borderTBLR, marginTBLR);
      style.toStringSetName("StyleFigure");
      return style;
   }

   public ByteObject getStyleGradient() {
      ColorSet cs = getColorSetActive();
      int colorBg1 = cs.getBg1();
      int colorBgGrad = cs.getBg2();
      int colorBorder1 = cs.getBorder1();
      int colorFont = cs.getFont1();

      ByteObject bgGradient = gradFac.getGradient(colorBgGrad, 100, ITechGradient.GRADIENT_TYPE_RECT_00_SQUARE);
      ByteObject bg1Rect = figureFac.getFigRect(colorBg1, bgGradient);

      ByteObject sizerMargin = sizerFactory.getSizerXPixelForYPixel(1, 30, ITechLayout.ET_FUN_04_MAX);
      ByteObject sizerBorder = sizerFactory.getSizerXPixelForYPixel(1, 40, ITechLayout.ET_FUN_04_MAX);
      ByteObject sizerPaddin = sizerFactory.getSizerXPixelForYPixel(1, 30, ITechLayout.ET_FUN_04_MAX);

      ByteObject marginTBLR = tblrFac.getTBLRSizer(sizerMargin);
      ByteObject borderTBLR = tblrFac.getTBLRSizer(sizerBorder);
      ByteObject paddinTBLR = tblrFac.getTBLRSizer(sizerPaddin);

      ByteObject bg2Border = figureFac.getFigBorder(sizerBorder, colorBorder1);
      ByteObject anchor = anchorFac.getCenterCenter();
      ByteObject content = figureFac.getFigString(FACE_01_MONOSPACE, STYLE_0_PLAIN, SIZE_4_LARGE, colorFont);
      ByteObject style = styleFac.getStyle(new ByteObject[] { bg1Rect, bg2Border }, content, anchor, paddinTBLR, borderTBLR, marginTBLR);
      style.toStringSetName("StyleGradient");
      return style;
   }

   public ByteObject getStyleGradientBgHorizBorder() {
      int arcBg = 0;
      int colorBorderBg = FULLY_OPAQUE_GREY;
      ByteObject tblrBlock = tblrFac.getTBLRPixel(1);
      ByteObject gradBlock = gradFac.getGradient(FULLY_OPAQUE_WHITE, 50, GRADIENT_TYPE_RECT_01_HORIZ);
      ByteObject bgBlock = figureFac.getFigRect(ColorUtils.getRGBInt(192, 16, 16), gradBlock);
      ByteObject borderBlock = figureFac.getFigBorder(tblrBlock, arcBg, arcBg, colorBorderBg, null);
      ByteObject style = styleFac.getStyle(new ByteObject[] { bgBlock, borderBlock }, null, null, null, tblrBlock);

      return style;
   }

   public ByteObject getStyleGradientBgVertBorder() {
      int arcBg = 0;
      int colorBorderBg = FULLY_OPAQUE_GREY;
      ByteObject tblrBlock = tblrFac.getTBLRPixel(1);
      ByteObject gradBlock2 = gradFac.getGradient(FULLY_OPAQUE_WHITE, 50, GRADIENT_TYPE_RECT_02_VERT);
      ByteObject bgBlock2 = figureFac.getFigRect(ColorUtils.getRGBInt(192, 16, 16), gradBlock2);
      ByteObject borderBlock2 = figureFac.getFigBorder(tblrBlock, arcBg, arcBg, colorBorderBg, null);
      ByteObject style = styleFac.getStyle(new ByteObject[] { bgBlock2, borderBlock2 }, null, null, null, tblrBlock);
      return style;
   }

   public ByteObject getStyleGreyed() {
      int arcBg = 0;
      int colorBorderBg = FULLY_OPAQUE_GREY;
      //BG Vert
      ByteObject tblrBlock = tblrFac.getTBLRPixel(1);
      ByteObject gradBlock = gradFac.getGradient(FULLY_OPAQUE_WHITE, 50, GRADIENT_TYPE_RECT_01_HORIZ);
      ByteObject bgBlock = figureFac.getFigRect(ColorUtils.getRGBInt(192, 16, 16), gradBlock);
      ByteObject borderBlock = figureFac.getFigBorder(tblrBlock, arcBg, arcBg, colorBorderBg, null);

      ByteObject sbBlockStyle = styleFac.getStyle(new ByteObject[] { bgBlock, borderBlock }, null, null, null, tblrBlock);
      return sbBlockStyle;
   }

   public ByteObject getStyleHole() {
      ColorSet cs = getColorSetActive();

      int colorBgGrad = cs.getBg2();
      int colorBorder1 = cs.getBorder1();
      int colorFont = cs.getFont1();

      ByteObject bgGradient = gradFac.getGradient(colorBgGrad, 100, ITechGradient.GRADIENT_TYPE_RECT_00_SQUARE);

      ByteObject sizerBorder = sizerFactory.getSizerXPixelForYPixel(1, 20, ITechLayout.ET_FUN_03_MIN);

      ByteObject marginTBLR = null;
      ByteObject borderTBLR = tblrFac.getTBLRSizer(sizerBorder);
      ByteObject paddinTBLR = null;

      //arcw is size of tblr
      ByteObject bg1Rect = figureFac.getFigRect(cs.getBg1(), bgGradient);
      ByteObject bg2Border = figureFac.getFigRectFill(cs.getContent2(), 2);
      ByteObject bg3Border = figureFac.getFigBorder(borderTBLR, 10, 10, colorBorder1, bgGradient);

      ByteObject anchor = anchorFac.getCenterCenter();
      ByteObject content = figureFac.getFigString(FACE_01_MONOSPACE, STYLE_0_PLAIN, SIZE_4_LARGE, colorFont);
      ByteObject style = styleFac.getStyle(new ByteObject[] { bg1Rect, bg2Border, bg3Border }, content, anchor, paddinTBLR, borderTBLR, marginTBLR);
      style.toStringSetName("StyleHole");
      return style;
   }

   public ByteObject getStyleRoot() {
      ColorSet cs = getColorSetActive();
      int colorBg1 = cs.getBg1();
      int colorBgGrad = cs.getBg2();
      int colorBorder1 = cs.getBorder1();
      int colorFont = cs.getFont1();

      ByteObject bgGradient = gradFac.getGradient(colorBgGrad, 100, ITechGradient.GRADIENT_TYPE_RECT_00_SQUARE);
      ByteObject bg1Rect = figureFac.getFigRect(colorBg1, bgGradient);

      ByteObject sizerMargin = sizerFactory.getSizerXPixelForYPixel(1, 30, ITechLayout.ET_FUN_03_MIN);
      ByteObject sizerBorder = sizerFactory.getSizerXPixelForYPixel(1, 30, ITechLayout.ET_FUN_03_MIN);
      ByteObject sizerPaddin = sizerFactory.getSizerXPixelForYPixel(1, 30, ITechLayout.ET_FUN_03_MIN);

      ByteObject marginTBLR = null;
      ByteObject borderTBLR = tblrFac.getTBLRSizer(sizerBorder);
      ByteObject paddinTBLR = tblrFac.getTBLRSizer(sizerPaddin);

      ByteObject bg2Border = figureFac.getFigBorder(sizerBorder, colorBorder1);
      bg2Border.toStringSetName("border2StyleRoot");
      ByteObject anchor = anchorFac.getCenterCenter();
      ByteObject content = figureFac.getFigString(FACE_01_MONOSPACE, STYLE_0_PLAIN, SIZE_4_LARGE, colorFont);
      ByteObject style = styleFac.getStyle(new ByteObject[] { bg1Rect, bg2Border }, content, anchor, paddinTBLR, borderTBLR, marginTBLR);
      return style;
   }

   public ByteObject getStyleScrollbarBlockBg() {
      return getStyleDrawableOnlyBorder();
   }

   public ByteObject getStyleScrollbarBlockFig() {
      return getStyleDrawableBig();
   }

   public ByteObject getStyleScrollbarH() {

      ColorSet cs = getColorSetActive();
      int colorBg1 = cs.getBg1();
      int colorBgGrad = cs.getBg2();
      int colorBorder1 = cs.getBorder1();
      int colorFont = cs.getFont1();

      ByteObject bgGradient = gradFac.getGradient(colorBgGrad, 100, ITechGradient.GRADIENT_TYPE_RECT_00_SQUARE);
      ByteObject bg1Rect = figureFac.getFigRect(colorBg1, bgGradient);

      ByteObject sizerMargin = sizerFactory.getSizerXPixelForYPixel(1, 30, ITechLayout.ET_FUN_04_MAX);
      //ByteObject sizerBorder = sizerFactory.getSizerXPixelForYPixel(1, 40, ITechLayout.ET_FUN_3_MIN);
      //ByteObject sizerPaddin = sizerFactory.getSizerXPixelForYPixel(1, 30, ITechLayout.ET_FUN_4_MAX);
      ByteObject sizerBorder = sizerFactory.getSizerPixel(4);
      ByteObject sizerPaddin = sizerFactory.getSizerPixel(1);

      ByteObject marginTBLR = null;
      ByteObject borderTBLR = tblrFac.getTBLRSizer(sizerBorder);
      ByteObject paddinTBLR = tblrFac.getTBLRSizer(sizerPaddin);

      ByteObject bg2Border = figureFac.getFigBorder(sizerBorder, colorBorder1);
      ByteObject anchor = anchorFac.getCenterCenter();
      ByteObject content = figureFac.getFigString(FACE_01_MONOSPACE, STYLE_0_PLAIN, SIZE_4_LARGE, colorFont);
      ByteObject style = styleFac.getStyle(new ByteObject[] { bg1Rect, bg2Border }, content, anchor, paddinTBLR, borderTBLR, marginTBLR);
      return style;
   }

   public ByteObject getStyleScrollbarV() {
      ColorSet cs = getColorSetActive();
      int colorBg1 = cs.getBg1();
      int colorBgGrad = cs.getBg2();
      int colorBorder1 = cs.getBorder1();
      int colorFont = cs.getFont1();

      ByteObject bgGradient = gradFac.getGradient(colorBgGrad, 100, ITechGradient.GRADIENT_TYPE_RECT_00_SQUARE);
      ByteObject bg1Rect = figureFac.getFigRect(colorBg1, bgGradient);

      ByteObject sizerMargin = sizerFactory.getSizerXPixelForYPixel(1, 30, ITechLayout.ET_FUN_04_MAX);
      //ByteObject sizerBorder = sizerFactory.getSizerXPixelForYPixel(1, 40, ITechLayout.ET_FUN_3_MIN);
      ///ByteObject sizerPaddin = sizerFactory.getSizerXPixelForYPixel(1, 30, ITechLayout.ET_FUN_4_MAX);
      ByteObject sizerBorder = sizerFactory.getSizerPixel(4);
      ByteObject sizerPaddin = sizerFactory.getSizerPixel(1);

      ByteObject marginTBLR = null;
      ByteObject borderTBLR = tblrFac.getTBLRSizer(sizerBorder);
      ByteObject paddinTBLR = tblrFac.getTBLRSizer(sizerPaddin);

      ByteObject bg2Border = figureFac.getFigBorder(sizerBorder, colorBorder1);
      ByteObject anchor = anchorFac.getCenterCenter();
      ByteObject content = figureFac.getFigString(FACE_01_MONOSPACE, STYLE_0_PLAIN, SIZE_4_LARGE, colorFont);
      ByteObject style = styleFac.getStyle(new ByteObject[] { bg1Rect, bg2Border }, content, anchor, paddinTBLR, borderTBLR, marginTBLR);
      return style;
   }

   public ByteObject getStyleScrollbarWrapper() {
      ColorSet cs = getColorSetActive();
      int colorBg1 = cs.getBg1();
      int colorBgGrad = cs.getBg2();
      ByteObject gradBg = gradFac.getGradient(colorBgGrad, 100, ITechGradient.GRADIENT_TYPE_RECT_00_SQUARE);
      ByteObject bgWrapper = figureFac.getFigRect(colorBg1, gradBg);

      int colorBorder1 = cs.getBorder1();
      ByteObject borderSize = sizerFactory.getSizerXPixelForYPixel(1, 40, ITechLayout.ET_FUN_03_MIN);
      ByteObject borderWrapper = figureFac.getFigBorder(colorBg1, colorBorder1);
      ByteObject sizer = null;
      ByteObject pad = tblrFac.getTBLRSizer(sizer);
      ByteObject margin = tblrFac.getTBLRSizer(sizer);
      ByteObject anchor = null;
      ByteObject sbBlockStyle = styleFac.getStyle(new ByteObject[] { bgWrapper, borderWrapper }, anchor, pad, borderSize, margin);
      return sbBlockStyle;
   }

   public ByteObject getStyleSelected() {

      ColorRepo colorRepo = getRepo();
      int pcolor = colorRepo.getBg2();
      int ccolor = colorRepo.getContent2();

      ByteObject content = figureFactory.getFigStringT_StyleColor(ccolor, STYLE_1_BOLD);

      ByteObject anchor = null;
      ByteObject margin = null;
      ByteObject pad = null;
      ByteObject border = null;

      ByteObject bgMargin = figureFactory.getFigRect(pcolor);
      ByteObject[] bgs = new ByteObject[] { bgMargin };

      ByteObject style = styleFactory.getStyle(bgs, content, anchor, pad, border, margin);

      styleOp.setGAnchorsBG1_Margin(style);
      
      style.setFlag(IBOStyle.STYLE_OFFSET_5_FLAG_X, IBOStyle.STYLE_FLAG_X_1_INCOMPLETE, true);

      //#debug
      style.toStringSetName("CreatorBOStyleSimple#getStyleSelected");

      return style;
   }
   
   
   public ByteObject getStyleMarked() {
      ByteObject content = null;
      ByteObject anchor = null;
      ByteObject margin = null;
      ByteObject pad = null;
      ByteObject border = null;

      int colorGrey = ColorUtils.getRGBInt(128, 250, 128, 128);
      ByteObject fg1 = figureFactory.getFigRect(colorGrey);
      fg1.setFlag(FIG__OFFSET_02_FLAG, FIG_FLAGP_5_IGNORE_ALPHA, false);
      fg1.setFlag(FIG__OFFSET_03_FLAGP, FIG_FLAGP_3_OPAQUE, false);

      ByteObject[] bgs = null;
      ByteObject[] fgs = new ByteObject[] { fg1 };

      ByteObject style = styleFactory.getStyle(bgs, content, anchor, pad, border, margin, fgs);

      styleOp.setGAnchorsFG1_Content(style);
      
      style.setFlag(IBOStyle.STYLE_OFFSET_5_FLAG_X, IBOStyle.STYLE_FLAG_X_1_INCOMPLETE, true);

      //#debug
      style.toStringSetName("MarkedIncomplete");

      return style;
   }
   

   public ByteObject getStyleTextReader() {
      ColorRepo colorRepo = getRepo();
      int pcolor = colorRepo.getBg1();
      int ccolor = colorRepo.getContent1();

      ByteObject content = figureFactory.getFigString(FACE_02_PROPORTIONAL, STYLE_0_PLAIN, ITechFont.SIZE_3_MEDIUM, ccolor);
      ByteObject anchor = anchorFac.getLeftTop();

      ByteObject margin = null;
      ByteObject pad = tblrFactory.getTBLRPixel(2);

      ByteObject bg = figureFactory.getFigRect(pcolor);

      int borderColor1 = colorRepo.getBorder1();
      ByteObject figBorder = figureFactory.getFigBorder(3, borderColor1);
      ByteObject border = figBorder.getSubFirst(IBOTypesLayout.FTYPE_2_TBLR);

      ByteObject[] bgs = new ByteObject[] { bg, figBorder };

      ByteObject style = styleFactory.getStyle(bgs, content, anchor, pad, border, margin);

      //#debug
      style.toStringSetName("StyleReader");

      return style;
   }

   public ByteObject getStyleTextReaderH() {
      ColorRepo colorRepo = getRepo();

      int pcolor = colorRepo.getBg1();
      int bg2 = colorRepo.getBg2();
      int bg3 = colorRepo.getBg3();

      int ccolor = colorRepo.getContent1();

      int br1 = colorRepo.getBorder1();
      int br2 = colorRepo.getBorder2();
      int br3 = colorRepo.getBorder3();

      ByteObject content = figureFactory.getFigString(FACE_02_PROPORTIONAL, STYLE_0_PLAIN, ITechFont.SIZE_3_MEDIUM, ccolor);
      ByteObject anchor = anchorFac.getLeftTop();

      ByteObject margin = tblrFactory.getTBLRPixel(5);
      ByteObject border = tblrFactory.getTBLRPixel(4);
      ByteObject pad = tblrFactory.getTBLRPixel(3);

      ByteObject bg = figureFactory.getFigRect(pcolor);

      ByteObject gradientMargin = gradFac.getGradient(br3, 50, ITechGradient.GRADIENT_TYPE_RECT_02_VERT);
      ByteObject bgMargin = figureFactory.getFigRect(pcolor, gradientMargin);

      int borderColor1 = br1;
      int borderColor2 = br3;
      int arcSize = 4;
      ByteObject gradientBorder = gradFac.getGradient(borderColor2, 100, ITechGradient.GRADIENT_TYPE_RECT_00_SQUARE);
      ByteObject figBorder = figureFactory.getFigBorder(border, arcSize, arcSize, borderColor1, gradientBorder);

      ByteObject[] bgs = new ByteObject[] { bgMargin, bg, figBorder };

      ByteObject style = styleFactory.getStyle(bgs, content, anchor, pad, border, margin);
      styleOp.setGAnchors(style, IBOStyle.STYLE_FLAG_B_1_BG, ITechStyle.STYLE_ANC_1_MARGIN);
      styleOp.setGAnchors(style, IBOStyle.STYLE_FLAG_B_2_BG, ITechStyle.STYLE_ANC_2_CONTENT);
      styleOp.setGAnchors(style, IBOStyle.STYLE_FLAG_B_3_BG, ITechStyle.STYLE_ANC_0_BORDER);

      //#debug
      style.toStringSetName("StyleReaderH");

      return style;
   }

   public ByteObject getStyleTitle() {
      ColorRepo colorRepo = getRepo();
      int pcolor = colorRepo.getBg1();
      int scolor = colorRepo.getBg2();

      int ccolor = colorRepo.getContent1();

      ByteObject text = figureFactory.getFigString(FACE_01_MONOSPACE, STYLE_1_BOLD, SIZE_4_LARGE, ccolor);
      ByteObject anchor = anchorFac.getCenterCenter();

      ByteObject margin = null;
      ByteObject pad = tblrFactory.getTBLRPixel(2);

      ByteObject bg = figureFactory.getFigRect(pcolor);

      int borderColor1 = colorRepo.getBorder1();
      int borderColor2 = colorRepo.getBorder2();
      ByteObject figBorder = figureFactory.getFigBorder(7, borderColor1, borderColor2, 100);
      ByteObject border = figBorder.getSubFirst(IBOTypesLayout.FTYPE_2_TBLR);

      ByteObject[] bgs = new ByteObject[] { bg, figBorder };

      ByteObject style = styleFactory.getStyle(bgs, text, anchor, pad, border, margin);
      return style;
   }

   public ByteObject getStyleFigureLosange() {
      ColorRepo colorRepo = getRepo();
      int pcolor = colorRepo.getBg3();
      int scolor = colorRepo.getBg2();

      int ccolor = colorRepo.getContent1();

      ByteObject text = figureFactory.getFigString(FACE_01_MONOSPACE, STYLE_1_BOLD, SIZE_4_LARGE, ccolor);
      ByteObject anchor = anchorFac.getCenterCenter();

      ByteObject margin = null;
      ByteObject pad = tblrFactory.getTBLRPixel(1);

      ByteObject bgRect = figureFactory.getFigRect(pcolor);

      int borderColor1 = colorRepo.getBorder1();
      int borderColor2 = colorRepo.getBorder2();
      ByteObject bgBorder = figureFactory.getFigBorder(2, borderColor1);
      ByteObject border = bgBorder.getSubFirst(IBOTypesLayout.FTYPE_2_TBLR);

      ByteObject[] bgs = new ByteObject[] { bgRect, bgBorder };

      ByteObject style = styleFactory.getStyle(bgs, text, anchor, pad, border, margin);
      return style;
   }

   public ByteObject getStyleBackgroundIncomplete() {
      int colorMargin = FULLY_OPAQUE_YELLOW;
      int colorBorder = FULLY_OPAQUE_GREEN;
      int colorPadding = FULLY_OPAQUE_BLUE;
      int colorContent = FULLY_OPAQUE_RED;

      int cg = FULLY_OPAQUE_GREY;
      boolean isReverse = true;
      
      ByteObject content = null;
      ByteObject anchor = null;

      ByteObject margin = null; 
      ByteObject border = null;
      ByteObject pad = null;

      Integer arcSize = new Integer(0);

      ByteObject gradientMargin = gradFac.getGradient(cg, 50, ITechGradient.GRADIENT_TYPE_RECT_00_SQUARE, isReverse);
      ByteObject bgMargin = figureFactory.getFigBorderT(margin, arcSize, arcSize, colorMargin, gradientMargin);

      ByteObject gradientBorder = gradFac.getGradient(cg, 50, ITechGradient.GRADIENT_TYPE_RECT_00_SQUARE, isReverse);
      ByteObject bgBorder = figureFactory.getFigBorderT(border, arcSize, arcSize, colorBorder, gradientBorder);

      ByteObject gradientPadding = gradFac.getGradient(cg, 50, ITechGradient.GRADIENT_TYPE_RECT_00_SQUARE, isReverse);
      ByteObject bgPadding = figureFactory.getFigBorderT(pad, arcSize, arcSize, colorPadding, gradientPadding);
       
      ByteObject gradientContent = gradFac.getGradient(cg, 0, ITechGradient.GRADIENT_TYPE_RECT_00_SQUARE);
      ByteObject bgContent = figureFactory.getFigRect(colorContent, gradientContent);

      ByteObject[] bgs = new ByteObject[] { bgMargin, bgBorder, bgPadding, bgContent };

      ByteObject style = styleFactory.getStyle(bgs, content, anchor, pad, border, margin);

      styleOp.setGAnchors(style, IBOStyle.STYLE_FLAG_B_1_BG, ITechStyle.STYLE_ANC_1_MARGIN);
      styleOp.setGAnchors(style, IBOStyle.STYLE_FLAG_B_2_BG, ITechStyle.STYLE_ANC_0_BORDER);
      styleOp.setGAnchors(style, IBOStyle.STYLE_FLAG_B_3_BG, ITechStyle.STYLE_ANC_3_PADDING);
      styleOp.setGAnchors(style, IBOStyle.STYLE_FLAG_B_4_BG, ITechStyle.STYLE_ANC_2_CONTENT);
      
      styleOp.setStyleIncomplete(style);
      return style;
   }

}
