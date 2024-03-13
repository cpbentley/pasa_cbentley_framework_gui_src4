package pasa.cbentley.framework.gui.src4.string;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.interfaces.IColors;
import pasa.cbentley.framework.cmd.src4.engine.CmdNode;
import pasa.cbentley.framework.coredraw.src4.interfaces.IMFont;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.factories.interfaces.IBOBox;
import pasa.cbentley.framework.drawx.src4.tech.ITechAnchor;
import pasa.cbentley.framework.drawx.src4.utils.AnchorUtils;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.ScrollBar;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.factories.TablePolicyFactory;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.table.interfaces.ITechTable;
import pasa.cbentley.framework.gui.src4.tech.ITechLinks;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;

/**
 * Handler for showing char
 * 
 * TODO should use the same font used by the {@link StringDrawable} requesting the character.
 * <br>
 * <br>
 * @author Charles-Philip Bentley
 *
 */
public class SymbolTable extends TableView {

   public static final int LINK_HEADER_STYLE  = 223;

   public static final int TECH_ID_CHAR_TABLE = 120;

   //#mdebug
   public static String[] toString(char[] myChars) {
      String[] ar = new String[myChars.length];
      for (int i = 0; i < ar.length; i++) {
         ar[i] = String.valueOf(myChars.length);
      }
      return ar;
   }

   //#enddebug

   private Drawable       base;

   private int            charWidth;

   public int             currentPlane = 0;

   private int            currentPlaneIndex;

   private IMFont         f;

   private int            fColor;

   private StringDrawable headerBotTitle;

   private StringDrawable headerTopTitle;

   private boolean        isDrawSelected;

   private boolean        isFiltering  = true;

   private boolean        isMonoSpaced;

   char[]                 myChars;

   private int            selectedIndex;

   private int            selectedIndexPrevious;

   private ByteObject     techSymbol;

   StringDrawable         trailer;

   private int            xMod;

   private int            yMod;

   /**
    * Policy depends on the style
    * @param sc
    */
   public SymbolTable(GuiCtx gc, StyleClass sc) {
      super(gc, sc, gc.getTablePolicyFactory().createTableFlowHorizPrefContent());
      techSymbol = styleClass.getByteObject(TECH_ID_CHAR_TABLE);

      CmdNode ctx = gc.getCC().createCmdNode("Symbols");
      setCmdNote(ctx);

      //create model
      IMFont f = getStyleOp().getStyleFont(style);

      StyleClass scCell = sc.getStyleClass(ITechLinks.LINK_81_STYLE_CLASS_TABLE_CELL);

      base = new Drawable(gc, scCell);
      //init with font sizer ctx
      ByteObject sizer = gc.getLAC().getSizerFactory().getSizerFontCtx();

      base.setSizers(sizer, sizer);
      base.initSize();
      //init before getting x,y
      //used to compute preferred content sizes
      modelGenetics = gc.getTableGeneticsC().getGenetics(base.getDrawnWidth(), base.getDrawnHeight());
      xMod = base.getContentX();
      yMod = base.getContentY();

      StyleClass scHeader = styleClass.getStyleClass(LINK_HEADER_STYLE);

      doUpdateCharArray();
      trailer = new StringDrawable(gc, scHeader, "");

      headerTopTitle = new StringDrawable(gc, scHeader, "Table of Symbols");
      setHeader(headerTopTitle, C.POS_0_TOP, ITechViewPane.PLANET_MODE_0_EAT);
      headerBotTitle = new StringDrawable(gc, scHeader, "");
      setHeader(headerBotTitle, C.POS_1_BOT, ITechViewPane.PLANET_MODE_0_EAT);

      setHelperFlag(ITechTable.HELPER_FLAG_20_SPECIAL_REPAINT, true);
   }

   protected void cmdNextPlane(InputConfig ic) {
      //find next valid plane
      currentPlaneIndex--;
      if (currentPlaneIndex < 0) {
         currentPlaneIndex = SymbolUtils.planes.length - 1;
      }
      currentPlane = SymbolUtils.planes[currentPlaneIndex];
      doUpdateCharArray();
      ic.srActionDoneRepaint(this);
   }

   protected void cmdPreviousPlane(InputConfig ic) {
      //find next valid plane
      currentPlaneIndex++;
      if (currentPlaneIndex >= SymbolUtils.planes.length) {
         currentPlaneIndex = 0;
      }
      currentPlane = SymbolUtils.planes[currentPlaneIndex];
      doUpdateCharArray();
      ic.srActionDoneRepaint(this);
   }

   protected void cmdToggleFiltering(InputConfig ic) {
      isFiltering = !isFiltering;
      doUpdateCharArray();
      ic.srActionDoneRepaint(this);
   }

   /**
    * Depending on plane, special + custom chars
    * <br>
    * <br>
    * 
    */
   public void doUpdateCharArray() {
      myChars = SymbolUtils.getPlaneChars(currentPlane, isFiltering);
      if (selectedIndex >= myChars.length) {
         selectedIndex = myChars.length - 1;
      }
      doUpdateHeader();
   }

   /**
    * Request {@link ScreenResult} to repaint header {@link Drawable}.
    * <br>
    * <br>
    * @param sr
    */
   public void doUpdateHeader() {
      char c = getSelectedChar();
      String update = c + " p:" + ((c >> 8) & 0xFF) + " :" + (c & 0xFF);
      if (headerBotTitle != null) {
         headerBotTitle.setStringNoUpdate(update);
         headerBotTitle.layoutInvalidate(true);
      }
   }

   /**
    * Overrides {@link TableView#drawModelDrawable(GraphicsX, int, int, int, int, int, int)}
    */
   protected void drawModelDrawable(GraphicsX g, int x, int y, int w, int h, int colAbs, int rowAbs) {
      int index = getVisualIndex(colAbs, rowAbs);
      drawModelIndex(g, x, y, w, h, index);
   }

   /**
    * Draw index and bg color for that cell.
    */
   private void drawModelIndex(GraphicsX g, int x, int y, int w, int h, int index) {
      if (index >= myChars.length) {
         return;
      }
      x += xMod;
      y += yMod;
      //System.out.println("#SymbolTable#drawModelIndex =\t" + index + " [" + x + "," + y + "]");
      //if selectable
      if (index == selectedIndex) {
         //selected index
         g.setColor(IColors.FULLY_OPAQUE_WHITE);
         g.fillRect(x, y, w, h);
         g.setColor(IColors.FULLY_OPAQUE_RED);
      } else {
         //if painting repaint with color
         if (index == selectedIndexPrevious) {
            //#debug
            toDLog().pDraw("erasing old IndexPrevious = " + selectedIndexPrevious, this, SymbolTable.class, "drawModelIndex", LVL_05_FINE, true);

            g.clipSet(x, y, w, h);
            super.drawDrawableBg(g);
            g.clipReset();
         }
         g.setColor(fColor);
      }
      int charWidth = f.charWidth(myChars[index]);
      int dx = AnchorUtils.getXAlign(ITechAnchor.ALIGN_6_CENTER, x, w, charWidth);
      int dy = AnchorUtils.getYAlign(ITechAnchor.ALIGN_6_CENTER, y, h, f.getHeight());
      g.drawChar(myChars[index], dx, dy, IBOBox.ANCHOR);
      if (index == selectedIndex) {
         //#debug
         toDLog().pDraw(x + " " + y + " w=" + w + " charWidth=" + charWidth, this, SymbolTable.class, "drawModelIndex", LVL_05_FINE, true);
      }
   }

   public char getSelectedChar() {
      return myChars[selectedIndex];
   }

   public int getSize() {
      return myChars.length;
   }

   /**
    * <li>Height = one logical unit
    * <li>Width = maximum of all preferred widths
    * <br>
    * <br>
    * How does work 0
    */
   public ByteObject getSymbolPolicy(TablePolicyFactory tableFac) {
      return tableFac.getFlowHoriz(0, 15);
   }

   public boolean hasPointState(int index, int state) {
      if (state == ITechDrawable.STYLE_05_SELECTED) {
         return index == selectedIndex;
      }
      return false;
   }

   /**
    * Called by mother class before starting to draw. 
    * <br>
    * This method prevents some calls to be repetitively called.
    * <br>
    * <br>
    * Set {@link GraphicsX} attributes global to all data cells.
    * @param g
    */
   public void initDataDraw(GraphicsX g) {
      f = gc.getDC().getCoreDrawCtx().getFontFactory().getFont(IMFont.FACE_MONOSPACE, IMFont.STYLE_BOLD, IMFont.SIZE_3_MEDIUM);
      //MStyle.getStyleFont(style);
      isMonoSpaced = true;
      charWidth = f.charWidth('m');
      fColor = getStyleOp().getStyleFontColor(style);
      g.setFont(f);
      selectedIndex = getSelectedIndex();
   }

   public void manageDragCallBack(ScrollBar scrollBar) {
      trailer.setStringNoUpdate("" + scrollBar.getConfig().getSIStart());
      trailer.init(0, 0);
      scrollBar.setTrailer(trailer);
   }

   /**
    * Uses the selection mechanism provided by {@link TableView}.
    * <br>
    */
   public void manageKeyInput(InputConfig ic) {
      if (ic.isStarP() && ic.is.getNumKeysPressed() == 1) {
         cmdPreviousPlane(ic);
      }
      if (ic.isPoundP() && ic.is.getNumKeysPressed() == 1) {
         cmdNextPlane(ic);
      }
      if (ic.isCancelP()) {
         cmdToggleFiltering(ic);
      }
      super.manageKeyInput(ic);
   }

   /**
    * Since we don't have cell {@link Drawable}, the {@link TableView} automatic press management will not work.
    */
   public void managePointerInput(InputConfig ic) {
      super.managePointerInput(ic);
   }

   /**
    * 
    * @param w
    * @param h
    * @param index
    */
   public void modelInit(int w, int h, int index) {
      //nothing to init
   }

   /**
    * Ask a repaint in the current {@link ScreenResult}
    * <br>
    * <br>
    * {@link TableView#getSelectedIndex()} returns the new.
    * <br>
    * <br>
    * 
    */
   protected void selectionMoveEvent(InputConfig ic) {
      int selectedIndex = super.getSelectedIndex();
      if (selectedIndex < myChars.length) {
         this.selectedIndexPrevious = getSelectedIndexPrevious();
         this.selectedIndex = selectedIndex;
      }
      doUpdateCharArray();
      doUpdateHeader();

      //#debug
      toDLog().pEvent("selectedIndex=" + selectedIndex, this, SymbolTable.class, "selectionMoveEvent", LVL_05_FINE, true);

      super.selectionMoveEvent(ic);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, SymbolTable.class, 320);
      toStringPrivate(dc);
      super.toString(dc.sup());
      dc.nlLvl(modelGenetics, "modelGenetics");
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, SymbolTable.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {
      dc.appendVarWithSpace("selectedIndex", selectedIndex);
      dc.appendVarWithSpace("size", getSize());
   }
   //#enddebug
}
