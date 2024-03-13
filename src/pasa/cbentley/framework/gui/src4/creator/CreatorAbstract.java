package pasa.cbentley.framework.gui.src4.creator;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.objects.color.ColorRepo;
import pasa.cbentley.byteobjects.src4.objects.color.ColorSet;
import pasa.cbentley.byteobjects.src4.objects.color.GradientFactory;
import pasa.cbentley.byteobjects.src4.objects.color.ITechGradient;
import pasa.cbentley.byteobjects.src4.objects.litteral.LitteralIntFactory;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.interfaces.IColors;
import pasa.cbentley.core.src4.utils.interfaces.IColorsBase;
import pasa.cbentley.core.src4.utils.interfaces.IColorsWeb;
import pasa.cbentley.framework.coredraw.src4.interfaces.ITechFont;
import pasa.cbentley.framework.drawx.src4.ctx.DrwCtx;
import pasa.cbentley.framework.drawx.src4.ctx.IBOTypesDrawX;
import pasa.cbentley.framework.drawx.src4.factories.AnchorFactory;
import pasa.cbentley.framework.drawx.src4.factories.BoxFactory;
import pasa.cbentley.framework.drawx.src4.factories.FigureFactory;
import pasa.cbentley.framework.drawx.src4.style.IBOStyle;
import pasa.cbentley.framework.drawx.src4.style.StyleFactory;
import pasa.cbentley.framework.drawx.src4.style.StyleOperator;
import pasa.cbentley.framework.drawx.src4.tech.ITechFigure;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ObjectGC;
import pasa.cbentley.framework.gui.src4.factories.DrawableFactory;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOScrollBar;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStringData;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOViewPane;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechViewDrawable;
import pasa.cbentley.framework.gui.src4.menu.IMenus;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOTableView;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;
import pasa.cbentley.layouter.src4.engine.LayoutOperator;
import pasa.cbentley.layouter.src4.engine.SizerFactory;
import pasa.cbentley.layouter.src4.engine.TblrFactory;
import pasa.cbentley.layouter.src4.tech.IBOSizer;
import pasa.cbentley.layouter.src4.tech.ITechLayout;

public abstract class CreatorAbstract
      extends ObjectGC implements ITechViewPane, IBOSizer, ITechGradient, ITechFigure, ITechLayout, IBOTableView, IBOStyle, IBOScrollBar, ITechViewDrawable, ITechDrawable, IBOViewPane, IBOStringData, IMenus, IColorsBase, ITechFont, IColors, IColorsWeb, IBOTypesDrawX {

   protected final AnchorFactory      anchorFac;

   protected final AnchorFactory      anchorFactory;

   protected final BOCtx              boc;

   protected final BoxFactory         boxEng;

   protected final DrwCtx             dc;

   protected final DrawableFactory    dFac;

   protected final FigureFactory      facFigure;

   protected final GradientFactory    facGradient;

   protected final StyleFactory       facStyle;

   protected final FigureFactory      figureFac;

   protected final FigureFactory      figureFactory;

   protected final GradientFactory    gradFac;

   protected final GradientFactory    gradFactory;

   protected final LitteralIntFactory litteralFactory;

   protected final SizerFactory       sizerFactory;

   protected final StyleFactory       styleFac;

   protected final StyleFactory       styleFactory;

   protected final StyleOperator      styleOp;

   protected final TblrFactory        tblrFac;

   protected final TblrFactory        tblrFactory;

   protected long                     seed;

   protected final CreateContext      crc;

   public CreatorAbstract(CreateContext crc) {
      super(crc.getGC());
      this.crc = crc;
      boc = gc.getBOC();
      dc = gc.getDC();
      styleOp = dc.getStyleOperator();
      dFac = gc.getDrawableCoreFactory();
      boxEng = dc.getBoxFactory();
      gradFactory = dc.getGradientFactory();
      facGradient = gradFactory;
      gradFac = gradFactory;
      figureFactory = dc.getFigureFactory();
      facFigure = figureFactory;
      figureFac = figureFactory;
      styleFactory = dc.getStyleFactory();
      styleFac = styleFactory;
      facStyle = styleFactory;
      tblrFactory = dc.getTblrFactory();
      tblrFac = tblrFactory;
      anchorFactory = dc.getAnchorFactory();
      anchorFac = anchorFactory;
      litteralFactory = boc.getLitteralIntFactory();
      sizerFactory = dc.getLAC().getFactorySizer();

   }

   public CreateContext getCRC() {
      return crc;
   }
   public ColorRepo getRepo() {
      return crc.getRepo();
   }

   protected ByteObject createByteObject(int type, int size) {
      return new ByteObject(gc.getBOC(), type, size);
   }

   /**
    * Relative to font
    * @return
    */
   public int encodeSize(int mode, int val, int eta, int etype, int efun) {
      LayoutOperator si = gc.getLAC().getLayoutOperator();
      return si.codedSizeEncode(mode, val, eta, etype, efun);

   }

   public ByteObject getCenter() {
      return boxEng.getBoxCenter();
   }

   public ColorSet getColorSetActive() {
      return getRepo().getActive();
   }

   public ByteObject getLeftCenter() {
      return boxEng.getBoxLeftCenter();
   }

   public ByteObject getRightCenter() {
      return boxEng.getBoxRightCenter();
   }

   /**
    * Bring back previous color set
    */
   public void reverseColors() {

   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, CreatorAbstract.class, "@line5");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, CreatorAbstract.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }

   //#enddebug

}
