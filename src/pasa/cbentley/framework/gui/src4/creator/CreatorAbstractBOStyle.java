package pasa.cbentley.framework.gui.src4.creator;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;

/**
 * Creates 
 * @author Charles Bentley
 *
 */
public abstract class CreatorAbstractBOStyle extends CreatorAbstract {

   public CreatorAbstractBOStyle(CreateContext gc) {
      super(gc);
   }

   public abstract ByteObject getStyleHole();

   public abstract ByteObject getStyleGradientBgHorizBorder();

   public abstract ByteObject getStyleGradientBgVertBorder();

   public abstract ByteObject getStyleDrawableOnlyBorder();

   public abstract ByteObject getStyleBorderPad();

   public abstract ByteObject getStyleBorderSmallOnly();

   public abstract ByteObject getStyleDrawableBig();

   public abstract ByteObject getStyleGradient();

   public abstract ByteObject getStyleFigure();

   public abstract ByteObject getStyleRoot();

   public abstract ByteObject getStyleScrollbarV();

   public abstract ByteObject getStyleScrollbarH();

   public abstract ByteObject getStyleScrollbarWrapper();

   public abstract ByteObject getStyleScrollbarBlockFig();

   public abstract ByteObject getStyleScrollbarBlockBg();

   public abstract ByteObject getStyle111();
}
