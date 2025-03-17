package pasa.cbentley.framework.gui.src4.table;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.interfaces.IColors;
import pasa.cbentley.framework.coredraw.src4.interfaces.IMFont;
import pasa.cbentley.framework.datamodel.src4.table.ITableModel;
import pasa.cbentley.framework.datamodel.src4.table.ObjectTableModel;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.factories.interfaces.IBOBox;
import pasa.cbentley.framework.drawx.src4.tech.ITechAnchor;
import pasa.cbentley.framework.drawx.src4.utils.AnchorUtils;
import pasa.cbentley.framework.gui.src4.canvas.GraphicsXD;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOTableView;
import pasa.cbentley.framework.gui.src4.table.interfaces.ITechTable;
import pasa.cbentley.framework.gui.src4.utils.DrawableMapper;

/**
 * {@link TableView} that draws without using {@link DrawableMapper} and {@link ITableModel}.
 * <br>
 * <br>
 * This particular instance draws the index with a background color.
 * <br>
 * <br>
 * What {@link IDrawable} are created? 2 {@link Drawable} for the partial repaints.
 * However all styling is done in this class.
 * <br>
 * <br>
 * Key methods from {@link TableView} that are overriden:
 * <li> {@link TableView#getSize()}
 * <li> {@link TableView#initDataDraw(GraphicsX)}
 * <li> {@link TableView#drawModelDrawable(GraphicsX, int, int, int, int, Config, int, int)}
 * <br>
 * <br>
 * Handy to experiment with different table policies but mainly will used to draw potentially thousands of data
 * which would be too memory inefficient to wrap in {@link Drawable} and {@link ObjectTableModel}.
 * <br>
 * <br>
 * {@link TableView} is already injecting Titles.
 * <br>
 * Simple Tech {@link ByteObject} to control the model genetics.
 * <br>
 * <br>
 * modelGenetics decides preferred size of cell. however table policy still decides if that size is to be honoured
 * <br>
 * <br>
 * @author Mordan
 * @see TableInjectionDemo
 */
public class TableDrawIndex extends TableView {

   /**
    * Model: Color values to be displayed.
    */
   private int[]   colors;

   /**
    * Style to be used
    * Why not using cell StyleClass root Style?
    */
   IMFont          f;

   /**
    * Draw selected cell using its own ways.
    */
   private boolean isDrawSelected = false;

   /**
    * Keep its own selection track
    * Why?
    */
   private int     selectedIndex  = -1;

   /**
    * 
    * @param style
    * @param policy
    * @param colors
    * @param modelGenetics decides preferred size of cell. however table policy still decides if that size is to be honoured
    */
   public TableDrawIndex(GuiCtx gc, StyleClass sc, ByteObject policy, int[] colors, ByteObject modelGenetics) {
      super(gc, sc, policy);
      if (colors == null)
         throw new NullPointerException();
      this.setColors(colors);
      this.modelGenetics = modelGenetics;
      //TODO full repaint does not call ViewPane move
      //Besides how to not make a full repaint? getModelDrawable
      setHelperFlag(ITechTable.HELPER_FLAG_20_SPECIAL_REPAINT, true);
   }

   /**
    * 
    */
   protected void drawModelDrawable(GraphicsXD g, int x, int y, int w, int h, int colAbs, int rowAbs) {
      int index = getVisualIndex(colAbs, rowAbs);
      drawModelIndex(g, x, y, w, h, index);
   }

   /**
    * Draw index and bg color for that cell.
    */
   private void drawModelIndex(GraphicsXD g, int x, int y, int w, int h, int index) {
      if (index >= getColors().length) {
         return;
      }
      //if selectable
      if (isDrawSelected && index == selectedIndex) {
         //selected index
         g.setColor(IColors.FULLY_OPAQUE_WHITE);
         g.fillRect(x, y, w, h);
         g.setColor(IColors.FULLY_OPAQUE_RED);
      } else {
         g.setColor(getColors()[index]);
         g.fillRect(x, y, w, h);
         g.setColor(0);
      }
      String ow = String.valueOf(index);
      int objectwidth = f.stringWidth(ow);
      x = AnchorUtils.getXAlign(ITechAnchor.ALIGN_6_CENTER, x, w, objectwidth);
      y = AnchorUtils.getYAlign(ITechAnchor.ALIGN_6_CENTER, y, h, f.getHeight());
      g.drawString(ow, x, y, IBOBox.ANCHOR);
   }

   public int[] getColors() {
      return colors;
   }

   public int getSize() {
      return getColors().length;
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
   public void initDataDraw(GraphicsXD g) {
      f = getStyleOp().getStyleFont(style);
      g.setFont(f);
      selectedIndex = getSelectedIndex();
      isDrawSelected = !hasFlagX(IBOTableView.T_FLAGX_1_NO_SELECTION);
   }

   public void modelInit(int w, int h, int index) {
      //nothing to init
   }

   public void setColors(int[] colors) {
      this.colors = colors;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, TableDrawIndex.class, 156);
      toStringPrivate(dc);
      super.toString(dc.sup());
      dc.appendVarWithSpace("selectedIndex", selectedIndex);
      dc.append(" size=" + getSize());

      dc.nlLvl(modelGenetics, "modelGenetics");
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, TableDrawIndex.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }
   //#enddebug

}
