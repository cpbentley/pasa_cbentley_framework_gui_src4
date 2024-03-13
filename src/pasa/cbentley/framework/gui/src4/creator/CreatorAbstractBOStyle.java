package pasa.cbentley.framework.gui.src4.creator;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.objects.pointer.IBOMergeMask;
import pasa.cbentley.framework.drawx.src4.string.interfaces.IBOFigString;

/**
 * Creates 
 * @author Charles Bentley
 *
 */
public abstract class CreatorAbstractBOStyle extends CreatorAbstract implements IBOMergeMask, IBOFigString {

   public CreatorAbstractBOStyle(CreateContext gc) {
      super(gc);
   }

   public abstract ByteObject getStyle111();

   public abstract ByteObject getStyleBorderPad();

   public abstract ByteObject getStyleBorderSmallOnly();

   public abstract ByteObject getStyleDrawableBig();

   public abstract ByteObject getStyleDrawableOnlyBorder();

   public abstract ByteObject getStyleFigure();

   public abstract ByteObject getStyleGradient();

   public abstract ByteObject getStyleGradientBgHorizBorder();

   public abstract ByteObject getStyleGradientBgVertBorder();

   public abstract ByteObject getStyleHole();

   public abstract ByteObject getStyleRoot();

   public abstract ByteObject getStyleScrollbarBlockBg();

   public abstract ByteObject getStyleScrollbarBlockFig();

   public abstract ByteObject getStyleScrollbarH();

   public abstract ByteObject getStyleScrollbarV();

   public abstract ByteObject getStyleScrollbarWrapper();

   public abstract ByteObject getStyleSelected();

   public abstract ByteObject getStyleTextReader();

   public abstract ByteObject getStyleTitle();
   
   public abstract ByteObject getStyleFigureLosange();
}
