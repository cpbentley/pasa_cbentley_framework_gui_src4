package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.byteobjects.src4.core.BOAbstractFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesDrw;
import pasa.cbentley.core.src4.utils.ColorUtils;
import pasa.cbentley.core.src4.utils.interfaces.IColors;
import pasa.cbentley.framework.coredraw.src4.interfaces.IMFont;
import pasa.cbentley.framework.drawx.src4.ctx.DrwCtx;
import pasa.cbentley.framework.drawx.src4.factories.AnchorFactory;
import pasa.cbentley.framework.drawx.src4.factories.BoxFactory;
import pasa.cbentley.framework.drawx.src4.factories.FigureFactory;
import pasa.cbentley.framework.drawx.src4.factories.interfaces.IBOFigure;
import pasa.cbentley.framework.drawx.src4.style.IBOStyle;
import pasa.cbentley.framework.drawx.src4.style.StyleFactory;
import pasa.cbentley.framework.drawx.src4.style.StyleOperator;
import pasa.cbentley.framework.drawx.src4.tech.ITechFigure;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.layouter.src4.ctx.IBOTypesLayout;
import pasa.cbentley.layouter.src4.engine.TblrFactory;

public class DefaultStyles extends BOAbstractFactory implements IBOStyle, ITechFigure, IBOTypesDrw {

   protected final GuiCtx gc;

   FigureFactory          figureFactory;

   StyleFactory           styleFactory;

   StyleOperator          styleOp;

   TblrFactory            tblrFactory;

   AnchorFactory          anchorFac;

   protected final DrwCtx dc;

   private BoxFactory     boxFac;

   public DefaultStyles(GuiCtx gc) {
      super(gc.getBOC());
      this.gc = gc;
      dc = gc.getDC();
      anchorFac = dc.getAnchorFactory();
      figureFactory = dc.getFigureFactory();
      styleOp = dc.getStyleOperator();
      styleFactory = dc.getStyleFactory();
      boxFac = dc.getBoxFactory();
      tblrFactory = dc.getTblrFactory();
   }

   /**
    * White bg
    * Black fg.
    * 
    * Empty. No border. no margin no padding
    * @return
    */
   public StyleClass getDefaultStyleClass() {

      ByteObject figBg = figureFactory.getFigRectOpaque(IColors.FULLY_OPAQUE_WHITE);
      ByteObject text = figureFactory.getFigString(IMFont.FACE_SYSTEM, IMFont.STYLE_PLAIN, IMFont.SIZE_3_MEDIUM, IColors.FULLY_OPAQUE_BLACK);
      ByteObject anchor = boxFac.getBoxCenter();
      ByteObject style = styleFactory.getStyle(figBg, text, anchor);
      StyleClass defStyle = new StyleClass(gc, style);
      defStyle.setName("Default");
      return defStyle;
   }

   public StyleClass getEmptyStyleClass() {
      return getDefaultStyleClass();
   }

   /**
    * Not cached
    * @param borderColor
    * @param master true if style has its StyleID space
    * @return
    */
   public ByteObject getStyle2Pad2Border(int borderColor) {
      int pcolor = ColorUtils.getRGBInt(255, 0, 0);
      int scolor = ColorUtils.getRGBInt(255, 0, 200);
      boolean horiz = true;

      ByteObject text = figureFactory.getFigString(IMFont.FACE_SYSTEM, IMFont.STYLE_PLAIN, IMFont.SIZE_3_MEDIUM, 0);
      ByteObject anchor = anchorFac.getCenterCenter();
      //simple 2 border
      ByteObject border = tblrFactory.getTBLRCoded(2);
      ByteObject figBorder = figureFactory.getFigBorder(border, borderColor);
      //simple background gradient
      ByteObject bg = figureFactory.getFigRectGrad(horiz, pcolor, scolor, 0);
      ByteObject pad = tblrFactory.getTBLRCoded(2);
      ByteObject[] bgs = new ByteObject[] { bg, figBorder };
      ByteObject style = styleFactory.getStyle(bgs, text, anchor, pad, border);
      return style;
   }

   public ByteObject getStyle2Pad2Border() {

      int pcolor = ColorUtils.getRGBInt(255, 0, 0);
      int scolor = ColorUtils.getRGBInt(255, 0, 200);
      boolean horiz = true;

      ByteObject text = figureFactory.getFigString(IMFont.FACE_SYSTEM, IMFont.STYLE_PLAIN, IMFont.SIZE_3_MEDIUM, 0);
      ByteObject anchor = anchorFac.getCenterCenter();
      //simple 2 border
      ByteObject border = tblrFactory.getTBLRCoded(2);
      ByteObject figBorder = figureFactory.getFigBorder(border, ColorUtils.FULLY_OPAQUE_GREY);
      //simple background gradient
      ByteObject bg = figureFactory.getFigRectGrad(horiz, pcolor, scolor, 0);
      ByteObject pad = tblrFactory.getTBLRCoded(2);
      ByteObject[] bgs = new ByteObject[] { bg, figBorder };
      ByteObject style = styleFactory.getStyle(bgs, text, anchor, pad, border);
      return style;
   }

   public ByteObject getStyle2Pad5Border1M() {

      int pcolor = ColorUtils.getRGBInt(255, 100, 0);
      int scolor = ColorUtils.getRGBInt(255, 100, 200);
      int pBrColor = ColorUtils.getRGBInt(255, 100, 200);
      int sBrColor = ColorUtils.getRGBInt(40, 100, 200);

      boolean horiz = true;

      ByteObject text = figureFactory.getFigString(IMFont.FACE_SYSTEM, IMFont.STYLE_PLAIN, IMFont.SIZE_3_MEDIUM, 0);
      ByteObject anchor = anchorFac.getCenterCenter();
      //simple 2 border
      //simple background gradient
      ByteObject bg = figureFactory.getFigRectGrad(horiz, pcolor, scolor, 0);
      ByteObject pad = tblrFactory.getTBLRCoded(2);
      ByteObject margin = tblrFactory.getTBLRCoded(1);
      ByteObject figBorder = figureFactory.getFigBorder(5, pBrColor, sBrColor, 100);
      ByteObject border = figBorder.getSubFirst(IBOTypesLayout.FTYPE_2_TBLR);

      ByteObject[] bgs = new ByteObject[] { bg, figBorder };

      ByteObject style = styleFactory.getStyle(bgs, text, anchor, pad, border, margin);
      return style;
   }

   /**
    * A Style that greys all colors
    * White becomes 192 192
    *  Black becomes 32,32,32
    *  All colors in between are matched to the closest
    * @return
    */
   public ByteObject getStyleGreyGeneric() {
      return null;
   }

   public ByteObject getStyleKeyFocusGeneric() {
      return null;
   }

   /**
    * Draws an alpha FG around the whole screen except at the Drawable area.
    * Alpha is Strongest in closely related drawables
    * FG is most opaque in remotely related drawables
    * How does the framework implement this?
    * 
    * @return
    */
   public ByteObject getStyleKeyFocusStrong() {
      return null;
   }

   public ByteObject getStyleOnly2Pad5BorderWhiteBg() {
      int pcolor = ColorUtils.getRGBInt(255, 100, 255);
      int scolor = ColorUtils.getRGBInt(255, 0, 200);

      ByteObject bg = figureFactory.getFigRect(ColorUtils.FULLY_OPAQUE_WHITE);
      ByteObject figBorder = figureFactory.getFigBorder(5, 0, 0, pcolor, scolor, 100);
      ByteObject pad = tblrFactory.getTBLRCoded(2);
      ByteObject style = styleFactory.getStyle(new ByteObject[] { bg, figBorder }, null, null, pad, figBorder.getSubFirst(IBOTypesLayout.FTYPE_2_TBLR));
      return style;
   }

   public ByteObject getStyleSelectedBgRed() {
      ByteObject bg = figureFactory.getFigRect(ColorUtils.getRGBInt(255, 255, 255, 255), true);
      ByteObject fg2 = figureFactory.getFigBorder(2, ColorUtils.getRGBInt(255, 255, 0, 0), true);
      fg2.setFlag(IBOFigure.FIG__OFFSET_03_FLAGP, IBOFigure.FIG_FLAGP_8_POSTPONE, true);
      ByteObject style = styleFactory.getStyle(bg, STYLE_OFFSET_2_FLAGB, STYLE_FLAGB_4_BG);
      styleOp.setGAnchors(style, STYLE_FLAGB_4_BG, STYLE_ANC_3_PADDING);

      styleOp.styleSet(style, fg2, STYLE_OFFSET_2_FLAGB, STYLE_FLAGB_5_FG);
      styleOp.setGAnchors(style, STYLE_FLAGB_5_FG, STYLE_ANC_0_BORDER);

      return style;
   }

   public ByteObject getStyleSelectedBgSimple() {
      ByteObject fg = figureFactory.getFigRect(ColorUtils.getRGBInt(128, 0, 0, 0), true);
      //DrwParam borderValue = DrwParam.getTBLR(2);
      //DrwParam borderFigure = DrwParam.getFigBorder(borderValue, ColorUtils.getRGBInt(210, 10, 10));
      //DrwParam style = DrwParam.getStyle(new ByteObject[] { null, borderFigure }, null, null, null, borderValue, null, fg);
      ByteObject style = styleFactory.getStyle(fg, STYLE_OFFSET_2_FLAGB, STYLE_FLAGB_4_BG);
      styleOp.setGAnchors(style, STYLE_FLAGB_4_BG, STYLE_ANC_3_PADDING);
      ByteObject content = figureFactory.getFigStringTColor(ColorUtils.getRGBInt(255, 255, 255, 255));
      //STYLESet(style, content, STYLE_OFFSET_1FLAGV, STYLE_FLAGV_1CONTENT);
      return style;
   }

   /**
    * Black Bg with WhiteForeground
    * @return
    */
   public ByteObject getStyleSelectedBlackBg() {
      //null for letting it know to use parent definition when merging
      ByteObject txt = figureFactory.getFigString(IMFont.FACE_MONOSPACE, IMFont.STYLE_BOLD, IMFont.SIZE_2_SMALL, ColorUtils.getRGBInt(200, 200, 255));
      ByteObject bg = figureFactory.getFigRectOpaque(ColorUtils.FULLY_OPAQUE_BLACK);
      ByteObject style = styleFactory.getStyle(bg, txt, null);
      return style;
   }

   /**
    * Simple Style for selection overriding all layers except Box Model members
    * Flag alpha Foreground. No need to manage anything
    * @return
    */
   public ByteObject getStyleSelectedSimple() {
      ByteObject fg = figureFactory.getFigRect(ColorUtils.getRGBInt(128, 0, 0, 0), true);
      //DrwParam borderValue = DrwParam.getTBLR(2);
      //DrwParam borderFigure = DrwParam.getFigBorder(borderValue, ColorUtils.getRGBInt(210, 10, 10));
      //DrwParam style = DrwParam.getStyle(new ByteObject[] { null, borderFigure }, null, null, null, borderValue, null, fg);
      ByteObject style = styleFactory.getStyle(fg, STYLE_OFFSET_2_FLAGB, STYLE_FLAGB_5_FG);
      styleOp.setGAnchors(style, STYLE_FLAGB_5_FG, STYLE_ANC_3_PADDING);
      ByteObject content = figureFactory.getFigStringTColor(ColorUtils.getRGBInt(255, 255, 255, 255));
      styleOp.styleSet(style, content, STYLE_OFFSET_1_FLAGA, STYLE_FLAGA_1_CONTENT);
      return style;
   }

}
