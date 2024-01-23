package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.utils.interfaces.IColors;
import pasa.cbentley.framework.coredraw.src4.interfaces.IMFont;
import pasa.cbentley.framework.drawx.src4.ctx.DrwCtx;
import pasa.cbentley.framework.drawx.src4.style.IBOStyle;
import pasa.cbentley.framework.drawx.src4.tech.ITechMergeMaskFigure;
import pasa.cbentley.framework.drawx.src4.tech.ITechMergeMaskFigureString;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ObjectGui;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;

/**
 * Style manager understand the {@link StyleClass}.
 * <br>
 * It serializes and deserialize StyleClass arrays.
 * 
 * <br>
 * Sub class and override serialize method to provide custom behavior.
 * <br>
 * @author Charles Bentley
 *
 */
public class StyleManager extends ObjectGui implements IBOStyle {

   private ByteObject     defContentStyle = null;


   ByteObject             styleDragged;

   private ByteObject     styleDraggedOver;

   ByteObject             styleKeyFocus;

   ByteObject             stylePointerFocus;

   ByteObject             stylePressed;

   public StyleManager(GuiCtx gc) {
      super(gc);
   }

   public ByteObject getDefaultCaretFigure() {
      ByteObject bo = gc.getDC().getFigureFactory().getFigRect(IColors.FULLY_OPAQUE_BLACK);
      return bo;
   }

   /**
    * Create style mask for font string and color
    * <br>
    * Create a String figure.
    * <br>
    * Uses {@link IMergeMask}
    * <br>
    * <br>
    * Every font has a fontID to identify it. When Font cannot be found,
    * it falls back on font {@link IMFont#FACE_SYSTEM}
    * <br>
    * <br>
    * 
    * @param string
    * @param fontColor
    * @return
    */
   public ByteObject createStyleContentMask(String string, int fontColor) {

      DrwCtx dc = gc.getDC();
      int fontID = gc.getCDC().getFontFactory().getFontFaceID(string);
      //in the case of figString, we need a 

      ByteObject text = dc.getFigureFactory().getFigString(fontID, 0, 0, fontColor);
      
      ByteObject mergeMaskFigure = dc.getFigureFactory().getMergeMaskFigure();
      
      mergeMaskFigure.setFlag(ITechMergeMaskFigureString.FIG_STR_MM_02_FACE_INDEX, ITechMergeMaskFigureString.FIG_STR_MM_02_FACE_FLAG, true);
      mergeMaskFigure.setFlag(ITechMergeMaskFigure.MM_INDEX_VALUES5_FLAG_2_COLOR, ITechMergeMaskFigure.MM_VALUES5_FLAG_2_COLOR, true);
      dc.getMergeMaskFactory().setMergeMask(text, mergeMaskFigure);

      ByteObject content = text;
      return dc.getStyleFactory().getStyle(content, STYLE_OFFSET_1_FLAGA, STYLE_FLAGA_1_CONTENT);
   }

   public ByteObject getDefaultContentStyle() {
      if (defContentStyle == null) {
         int fontColor = IColors.FULLY_OPAQUE_GREY;
         IMFont f = gc.getDC().getFontFactory().getDefaultFont();
         defContentStyle = gc.getDC().getFigureFactory().getFigString(null, f.getFace(), f.getStyle(), f.getSize(), fontColor);
      }
      return defContentStyle;
   }

   public ByteObject getDraggedOverStyle() {
      return styleDraggedOver;
   }


   /**
    * Adds the Given Focus Styles to the {@link StyleClass}
    * <li> {@link ITechDrawable#STYLE_06_FOCUSED_KEY}
    * <li> {@link ITechDrawable#STYLE_07_FOCUSED_POINTER}
    * <li> {@link ITechDrawable#STYLE_08_PRESSED}
    * <li> {@link ITechDrawable#STYLE_09_DRAGGED}
    * 
    * @param sc
    */
   public void linkFocusStyles(StyleClass sc) {
      if (styleKeyFocus != null) {
         sc.linkStateStyle(styleKeyFocus, ITechDrawable.STYLE_06_FOCUSED_KEY);
      }
      if (stylePointerFocus != null) {
         sc.linkStateStyle(stylePointerFocus, ITechDrawable.STYLE_07_FOCUSED_POINTER);
      }
      if (stylePressed != null) {
         sc.linkStateStyle(stylePressed, ITechDrawable.STYLE_08_PRESSED);
      }
      if (styleDragged != null) {
         sc.linkStateStyle(styleDragged, ITechDrawable.STYLE_09_DRAGGED);
      }
   }

   
   public void setDraggedOverStyle(ByteObject pressed) {
      styleDraggedOver = pressed;
   }

   public void setDraggedStyle(ByteObject dragged) {
      styleDragged = dragged;
   }

   public void setKeyFocusStyle(ByteObject style) {
      styleKeyFocus = style;
   }

   /**
    * Sets the focus style that will be use when method {@link StyleManager#linkFocusStyles(StyleClass)} is called
    * on a {@link StyleClass}.
    * <br>
    * <br>
    * @param style style layer for items with pointer focus state
    */
   public void setPointerFocusStyle(ByteObject style) {
      stylePointerFocus = style;
   }

   public void setPressedStyle(ByteObject pressed) {
      stylePressed = pressed;
   }

   

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, StyleManager.class, "@line5");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {
      
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, StyleManager.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug
   

   

}
