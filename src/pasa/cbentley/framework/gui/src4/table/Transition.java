package pasa.cbentley.framework.gui.src4.table;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.gui.src4.core.ScrollConfig;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ObjectGC;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOCellPolicy;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOTableView;

/**
 * Model the transition between two single selection states. 
 * <br>
 * <br>
 * Every time the user selects a new cell, an instance of this class models that event. 
 * <br>
 * <b>Why?</b>: because grid structure may change depending on selection state.
 * If that's the case, the helper flag {@link TableView#H}.
 * {@link IBOCellPolicy#CELLP_FLAGP_5_VARIABLE_SIZES}.
 * <br>
 * <br>
 * <b>Properties</b> : <br>
 * <li>Old selected Cell <br> <li>New selected Cell <br> <li>Vx vector in
 * cell unit <li>Vy vector in cell units.
 * 
 * <br>
 * <br>
 * For {@link IBOTableView#T_FLAGX_3_ROW_SELECTION} case, selection
 * transition... <br>
 * From there the class compute some values <br>
 * -the direction vector of the change <br>
 * Unrelated to {@link ScrollConfig}. <br>
 * 
 * @author Charles-Philip Bentley
 * 
 */
public class Transition extends ObjectGC implements IStringable {

   /**
    * Focus to a fully visible cell.
    */
   public static final int TRANSITION_0_INSIDE_VIEWPORT   = 0;

   /**
    * Focus goes to a partially shown cell
    */
   public static final int TRANSITION_1_INSIDE_TO_PARTIAL = 1;

   /**
    * Focus goes to an invisible cell.
    */
   public static final int TRANSITION_2_OUTSIDE           = 2;


   public Transition(GuiCtx gc) {
      super(gc);
   }

   public static String debugType(int type) {
      switch (type) {
         case TRANSITION_0_INSIDE_VIEWPORT:
            return "INSIDE";
         case TRANSITION_1_INSIDE_TO_PARTIAL:
            return "ToPartial";
         case TRANSITION_2_OUTSIDE:
            return "OUTSIDE";
         default:
            return "UNKNOWN";
      }
   }

   public Config newConfig;

   /**
    * New state selected column
    */
   public int    newSelectedColAbs;

   /**
    * New state selected row
    */
   public int    newSelectedRowAbs;

   public Config oldConfig;

   public int    oldSelectedColAbs;

   /**
    * Previous Row Index
    */
   public int    oldSelectedRowAbs;

   public int    type = 0;

   /**
    * if vx is positive, we have a move right
    */
   public int    vx;

   /**
    * if vy is positive, we have a move down
    */
   public int    vy;

   /**
    * Compute the {@link Transition} to [newRowAbs,newColAbs] from the given {@link Config}. 
    * <br>
    * <br>
    * Acts like a selection Model
    * <br>
    * <br>
    * @param old current configuration
    * @param newColAbs new selected column
    * @param newRowAbs new selected row
    */
   public void compute(Config old, int newColAbs, int newRowAbs, Config next) {
      this.oldConfig = old;
      this.newConfig = next;
      oldSelectedColAbs = old.selectedColAbs;
      oldSelectedRowAbs = old.selectedRowAbs;
      newSelectedColAbs = newColAbs;
      newSelectedRowAbs = newRowAbs;
      // defines the move as a vector
      // if vx is positive, we have a move right
      vx = newSelectedColAbs - oldSelectedColAbs;
      // if vy is positive, we have a move down
      vy = newSelectedRowAbs - oldSelectedRowAbs;
      //

      next.doUpdateFromTransition(old, this);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, Transition.class, 140);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {
      
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, Transition.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug
   


}