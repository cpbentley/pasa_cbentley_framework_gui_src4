package pasa.cbentley.framework.gui.src4.nav;

import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.cmd.src4.nav.INavigational;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ObjectGC;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.string.StringEditControl;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.utils.DrawableArrays;

/**
 * Class that manages the TBLR visual topology relationships between {@link IDrawable}s.
 * 
 * <p>
 * <li>Used by devices (keyboard, gamepad) to move their focus
 * <li>Used by Floating {@link Drawable}s such as {@link StringEditControl}.
 * <li>When a Popup Drawable appears on a n+1 DLayer, it can be TBLR associated to a TableView cell.
 * </p>
 * <br>
 * <p>
 * Existing structural topoligies:
 * <li>Some container {@link IDrawable} like {@link TableView} have their own TBLR structural topology  management.
 * However that management must check if current drawable cell does not have the topology override flag.
 * This class is used a generic that may override other topologies such as {@link TableView} one.
 * </p>
 * <br>
 * <p>
 * Popup may be set to be Bottom of Cell {@link Drawable}. It will be drawn as such on Screen. When a Down Nav event is recieved by CellDrawable
 * having KeyFocus, CellDrawable does not have Vertical navigation. Usually, TableView would take the event and give keyfocus to the TableCell below.
 * </p>
 * 
 * @author Charles-Philip Bentley
 *
 */
public class TopologyTBLRNav extends ObjectGC implements ITechNavCmdGui {

   /**
    * 
    */
   private IDrawable[] d1;

   private IDrawable[] d2;

   private int[]       pos;

   private int         posNextEmpty;

   public TopologyTBLRNav(GuiCtx gc) {
      super(gc);
      int startSize = 3;
      d1 = new IDrawable[startSize];
      d2 = new IDrawable[startSize];
      pos = new int[startSize];

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


   /**
    * The relation of d1 {@link IDrawable} relative to d2 {@link IDrawable}.
    * <p>
    * <b>Position:</b> <br>
    * <li> {@link C#POS_0_TOP}
    * <li> {@link C#POS_1_BOT}
    * <li> {@link C#POS_2_LEFT}
    * <li> {@link C#POS_3_RIGHT}
    * </p>
    * 
    * @param d1 d1 is on Top of D2
    * @param d2
    * @return -1 if no position relation was set between d1 and d2
    */
   public int getDrawableRelation(IDrawable d1, IDrawable d2) {
      for (int i = 0; i < posNextEmpty; i++) {
         if (this.d1[i] == d1 && this.d2[i] == d2) {
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
    * Relative positioning of nav1 relative to nav2. 
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
    * 
    * 
    * <p>
    * <b>Position:</b> <br>
    * <li> {@link C#POS_0_TOP}
    * <li> {@link C#POS_1_BOT}
    * <li> {@link C#POS_2_LEFT}
    * <li> {@link C#POS_3_RIGHT}
    * </p>
    * <br>
    * 
    * The old position is not lost. The topolgy is done over {@link ITechNavCmdGui#NAV_TOPO_OP_0_OVER}. Which
    * means that when this topo link is removed, the old link is be prime active again.
    * 
    * <p>
    * {@link INavigationalGui}
    * Irrespective whether {@link IDrawable} are {@link INavigational}.
    * </p>
    * 
    * @param nav1
    * @param position
    * @param nav2
    * 
    * @see INavigationalGui
    */
   public void positionToplogy(IDrawable nav1, int position, IDrawable nav2) {
      positionToplogy(nav1, position, nav2, ITechNavCmdGui.NAV_TOPO_OP_0_OVER);
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

      int index = -1;
      for (int i = 0; i < d1.length; i++) {
         if (d1[i] == nav1 && pos[i] == position) {
            //
            //add drawable in list on the first position
            d2[i] = nav2;
            return;
         }
         if (d1[i] == null) {
            index = i;
         }
      }
      //
      if (index == -1) {
         index = getFirstNull();
      }
      d1[index] = nav1;
      pos[index] = position;
      d2[index] = nav2;

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

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, TopologyTBLRNav.class, toStringGetLine(336));
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, TopologyTBLRNav.class, toStringGetLine(336));
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }
   //#enddebug

}
