package pasa.cbentley.framework.gui.src4.nav;

import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.INavigational;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.string.StringEditControl;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.utils.DrawableArrays;

/**
 * Manage a loose TBLR relationships of {@link IDrawable} between themselves with a Drawable container.
 * <br>
 * Used by Floating {@link Drawable}s such as {@link StringEditControl}.
 * <br>
 * Some container {@link IDrawable} like {@link TableView} have their own TBLR topology closed management.
 * <br>
 * However that management must check if current drawable cell does not have the topology override flag.
 * <br>
 * <br>
 * 
 * This class is used a generic that may override other topologies such as {@link TableView} one.
 * <br>
 * 
 * When a Popup Drawable appears on a n+1 DLayer, it can be TBLR associated to a TableView cell.
 * <br>
 * Popup may be set to be Bottom of Cell {@link Drawable}. It will be drawn as such on Screen. When a Down Nav event is recieved by CellDrawable
 * having KeyFocus, CellDrawable does not have Vertical navigation. Usually, TableView would take the event and give keyfocus to the TableCell below.
 * <br>
 * <br>
 * 
 * @author Charles-Philip Bentley
 *
 */
public class TopologyTBLRNav {

   public static final int OP_0_OVER   = 0;

   public static final int OP_1_INSERT = 1;

   public static final int START_SIZE = 3;

   /**
    * 
    */
   private IDrawable[]     d1         = new IDrawable[START_SIZE];

   private IDrawable[]     d2         = new IDrawable[START_SIZE];

   protected final GuiCtx gc;

   private int[]           pos        = new int[START_SIZE];

   private int             posNextEmpty;

   public TopologyTBLRNav(GuiCtx gc) {
      this.gc = gc;
   }
   /**
    * Return the {@link Drawable} in the given position relationship with d {@link Drawable}.
    * <br>
    * <br>
    * One {@link Drawable} may only have 
    * <li>one position with a given drawable.
    * <li>two same positions with 2 different drawable
    * <br>
    * <br>
    * @param position
    * @param d
    * @return null if none
    */
   public IDrawable getDrawableD1(int position, IDrawable d) {
      for (int i = 0; i < posNextEmpty; i++) {
         if (d2[i] == d && pos[i] == position) {
            return d1[i];
         }
      }
      return null;
   }

   /**
    * Return the {@link Drawable} in the given position relationship with d {@link Drawable}.
    * <br>
    * @param position
    * @param d
    * @return null if none
    */
   public IDrawable getDrawableD2(int position, IDrawable d) {
      for (int i = 0; i < posNextEmpty; i++) {
         if (d1[i] == d && pos[i] == position) {
            return d2[i];
         }
      }
      return null;
   }

   public int getDrawableRelation(IDrawable dr1, IDrawable dr2) {
      for (int i = 0; i < posNextEmpty; i++) {
         if (d1[i] == dr1 && d2[i] == dr2) {
            return pos[i];
         }
      }
      return -1;
   }

   /**
    * Returns the first null index
    * @return
    */
   public int getFirstNull() {
      for (int i = 0; i < d1.length; i++) {
         if (d1[i] == null) {
            return i;
         }
      }
      int newSize = d1.length + 1;
      d1 = DrawableArrays.ensureCapacity(d1, newSize);
      d2 = DrawableArrays.ensureCapacity(d2, newSize);
      pos = gc.getUC().getMem().ensureCapacity(pos, newSize);
      return newSize - 1;
   }

   /**
    * 
    * @param d
    * @return
    */
   public IDrawable[] getTBLR(IDrawable d) {
      IDrawable[] tblr = new IDrawable[4];
      return tblr;
   }

   private void moveTo(ExecutionContextCanvasGui ec, IDrawable iDrawable, IDrawable nav) {
      gc.getFocusCtrl().newFocusKey(ec, iDrawable);
   }

   public void navigateOutDown(ExecutionContextCanvasGui ic, IDrawable nav) {
      if (nav.hasStateNav(ITechDrawable.NAV_01_TOPO_UP)) {
         //second go up the chain
         for (int i = 0; i < d1.length; i++) {
            if (d1[i] == nav) {
               //nav is to the right of d2[i]
               if (pos[i] == C.POS_0_TOP) {
                  moveTo(ic, d2[i], nav);
               }
            }
            if (d2[i] == nav) {
               //nav is to the right of "d2 is to the left of Nav"
               if (pos[i] == C.POS_1_BOT) {
                  moveTo(ic, d1[i], nav);
               }
            }
         }
      }
   }

   /**
    * Key focus in drawable, and this method checks if there is a drawable on the left of the drawable to which
    * to give the key focus.
    * <br>
    * <br>
    * 
    * @param ic
    * @param nav
    */
   public void navigateOutLeft(ExecutionContextCanvasGui ic, IDrawable nav) {
      //first check topology if there is something to the left so that this drawable is righted of something
      if (nav.hasStateNav(ITechDrawable.NAV_04_TOPO_RIGHT)) {
         //second go up the chain
         for (int i = 0; i < d1.length; i++) {
            if (d1[i] == nav) {
               //nav is to the right of d2[i]
               if (pos[i] == C.POS_3_RIGHT) {
                  moveTo(ic, d2[i], nav);
               }
            }
            if (d2[i] == nav) {
               //nav is to the right of "d2 is to the left of Nav" so we move left to d2
               if (pos[i] == C.POS_2_LEFT) {
                  moveTo(ic, d1[i], nav);
               }
            }
         }
      }

      //third go down the hierarchy to accept AroundTheClock events

   }

   public void navigateOutRight(ExecutionContextCanvasGui ic, IDrawable nav) {
      //first check topology if there is something to the left so that this drawable is righted of something
      if (nav.hasStateNav(ITechDrawable.NAV_03_TOPO_LEFT)) {
         //second go up the chain
         for (int i = 0; i < posNextEmpty; i++) {
            if (d1[i] == nav) {
               if (pos[i] == C.POS_2_LEFT) {
                  moveTo(ic, d2[i], nav);
               }
            }
            if (d2[i] == nav) {
               if (pos[i] == C.POS_3_RIGHT) {
                  moveTo(ic, d1[i], nav);
               }
            }
         }
      }
   }

   /**
    * 
    * @param ic
    * @param nav
    */
   public void navigateOutUp(ExecutionContextCanvasGui ic, IDrawable nav) {
      if (nav.hasStateNav(ITechDrawable.NAV_02_TOPO_DOWN)) {
         //second go up the chain
         for (int i = 0; i < d1.length; i++) {
            if (d1[i] == nav) {
               //nav is to the right of d2[i]
               if (pos[i] == C.POS_1_BOT) {
                  moveTo(ic, d2[i], nav);
               }
            }
            if (d2[i] == nav) {
               //nav is to the right of "d2 is to the left of Nav"
               if (pos[i] == C.POS_0_TOP) {
                  moveTo(ic, d1[i], nav);
               }
            }
         }
      }
   }

   /**
    * Relative positioning of nav1 relative to nav2
    * <br>
    * <br>
    * Does not automatically register the inverse relationship.
    * <br>
    * <br>
    * Sets the state {@link ITechDrawable#STATE_23_RELATIVE_TOPOLOGY}.
    * <br>
    * nav1 visually positioned on top/bottom/left/right of nav2
    * <li>nav1 top of nav2
    * <li>nav1 left of nav2
    * <br>
    * <br>
    * Position: <br>
    * <li> {@link C#POS_0_TOP}
    * <li> {@link C#POS_1_BOT}
    * <li> {@link C#POS_2_LEFT}
    * <li> {@link C#POS_3_RIGHT}
    * <br>
    * 
    * <br>
    * The old position is not lost. The topolgy is done over {@link TopologyTBLRNav#OP_0_OVER}. Which
    * means that when this topo link is removed, the old link is be prime active again.
    * <br>
    * {@link INavigational}
    * <br>
    * @param nav1
    * @param position
    * @param nav2
    * 
    * @see INavigational
    */
   public void positionToplogy(IDrawable nav1, int position, IDrawable nav2) {
      positionToplogy(nav1, position, nav2, OP_0_OVER);
   }

   /**
    * 
    * @param nav1
    * @param position
    * @param nav2
    * @param op
    */
   public void positionToplogy(IDrawable nav1, int position, IDrawable nav2, int op) {
      //
      if (position == C.POS_0_TOP) {
         nav1.setStateNav(ITechDrawable.NAV_01_TOPO_UP, true);
         nav2.setStateNav(ITechDrawable.NAV_02_TOPO_DOWN, true);
      } else if (position == C.POS_1_BOT) {
         nav1.setStateNav(ITechDrawable.NAV_02_TOPO_DOWN, true);
         nav2.setStateNav(ITechDrawable.NAV_01_TOPO_UP, true);
      }
      int firstNull = -1;
      for (int i = 0; i < d1.length; i++) {
         if (d1[i] == nav1 && pos[i] == position) {
            //
            //add drawable in list on the first position
            d2[i] = nav2;
            return;
         }
         if (d1[i] == null) {
            firstNull = i;
         }
      }
      //
      if (firstNull == -1) {
         firstNull = getFirstNull();
      }
      d1[firstNull] = nav1;
      pos[firstNull] = position;
      d2[firstNull] = nav2;

   }

   /**
    * Removes any topology link to the {@link IDrawable}.
    * <br>
    * <br>
    * 
    * @param d
    */
   public void removeFromTopology(IDrawable d) {
      for (int i = 0; i < d1.length; i++) {
         if (d1[i] == d || d2[i] == d) {
            d1[i] = null;
            d2[i] = null;
         }
      }
   }
}
