package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.table.TableView;

/**
 * Represents Drawable adjacent to each other that will share
 * the style or just some style layer. used in grouping cells in a {@link TableView}
 * as well.
 * <br>
 * The first Drawables added will dictate the style to be used.
 * <br>
 * Drawables are spaced together with the padding value.
 * <br>
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 *
 * 
 * {@link CmdCtx} are still separate. 
 * <br>
 * This class only merge the visual styles.
 * <br>
 * @author Charles Bentley
 *
 */
public class MyDrawables extends Drawable {

   public MyDrawables(GuiCtx gc, StyleClass sc) {
      super(gc,sc);
   }

   public void addDrawable(IDrawable d) {

   }
}
