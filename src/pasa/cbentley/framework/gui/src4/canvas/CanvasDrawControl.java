package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.INavigational;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.nav.TopologyTBLRNav;
import pasa.cbentley.framework.gui.src4.string.StringEditControl;
import pasa.cbentley.framework.gui.src4.table.TableView;

/**
 * <li> Add/Remove Drawable to the Layers of the {@link TopologyDLayer}
 * <li> Manage the Focus
 * <li> Manage the Nav
 * 
 * <br>
 * <br>
 * {@link INavigational} are either controlled 
 * <li> by their parents, case of a {@link TableView}
 * <li> By the {@link TopologyTBLRNav}. Because Drawables 
 * <br>
 * Case of two string edits with a nav relationship.
 * Overlay of {@link StringEditControl} modifies temporarely that relationship.
 * It will be inserted between. But how does the {@link TableView} know that?
 * {@link StringEditControl} and modifie the relationship when appearing and disappearing.
 * <br>
 * {@link TableView} gets a NavEvent from {@link StringEditControl}, gets the original cell
 * and sends the Focus to the right drawable
 * 
 * @author Charles Bentley
 *
 */
public class CanvasDrawControl {

   private CanvasAppliInputGui canvas;

   protected final GuiCtx      gc;

   public CanvasDrawControl(GuiCtx gc, CanvasAppliInputGui canvas) {
      this.gc = gc;
      this.canvas = canvas;
   }

   /**
    * Adds by default to {@link C#ANC_4_CENTER_CENTER}
    * @param foreDrw
    * @param millis
    */
   public synchronized void addForeground(IDrawable foreDrw, int millis) {
      addForeground(foreDrw, millis, C.ANC_4_CENTER_CENTER);
   }

   /**
    * Add a foreground drawable to be painted for xxx ms.
    * Anchor Positioning is done relative to Drawable.
    * <br>
    * When null, relative to Appli Screen
    * <br> 
    * @param foreDrw
    * @param millis
    * @param anchor {@link C#ANC_0_TOP_LEFT} ...
    */
   public synchronized void addForeground(IDrawable foreDrw, int millis, int anchor) {
      switch (anchor) {
         case C.ANC_0_TOP_LEFT:
            foreDrw.setXYLogic(C.LOGIC_1_TOP_LEFT, C.LOGIC_1_TOP_LEFT);
            break;
         case C.ANC_1_TOP_CENTER:
            foreDrw.setXYLogic(C.LOGIC_2_CENTER, C.LOGIC_1_TOP_LEFT);
            break;
         case C.ANC_2_TOP_RIGHT:
            foreDrw.setXYLogic(C.LOGIC_3_BOTTOM_RIGHT, C.LOGIC_1_TOP_LEFT);
            break;
         case C.ANC_3_CENTER_LEFT:
            foreDrw.setXYLogic(C.LOGIC_1_TOP_LEFT, C.LOGIC_2_CENTER);
            break;
         case C.ANC_5_CENTER_RIGHT:
            foreDrw.setXYLogic(C.LOGIC_3_BOTTOM_RIGHT, C.LOGIC_2_CENTER);
            break;
         case C.ANC_6_BOT_LEFT:
            foreDrw.setXYLogic(C.LOGIC_1_TOP_LEFT, C.LOGIC_3_BOTTOM_RIGHT);
            break;
         case C.ANC_7_BOT_CENTER:
            foreDrw.setXYLogic(C.LOGIC_2_CENTER, C.LOGIC_3_BOTTOM_RIGHT);
            break;
         case C.ANC_8_BOT_RIGHT:
            foreDrw.setXYLogic(C.LOGIC_3_BOTTOM_RIGHT, C.LOGIC_3_BOTTOM_RIGHT);
            break;
         default:
            foreDrw.setXYLogic(C.LOGIC_2_CENTER, C.LOGIC_2_CENTER);
            break;
      }
      foreDrw.init(0, 0);
      canvas.getForegrounds().addForeToArray(foreDrw, millis, anchor);
   }

   /**
    * When null? 
    * @param view
    */
   private void checkD(IDrawable view) {
      if (view == null) {
         throw new NullPointerException("Null Drawable");
      }
      if (view.getVC() == null) {
         throw new IllegalArgumentException("Null ViewContext");
      }
   }
   //#enddebug

   /**
    * Removes drawable from Canvas if it is there. 
    * <br>
    * <br>
    * 
    * If successful, and if in current KeyFocus, puts parent to focus or newfocus
    * <br>
    * <br>
    * Which brings the window 
    * <br>
    * <br>
    * Only lib classes should call this method
    * <br>s
    * @param ic can be null, if so {@link Controller} is used.
    * @param d
    * @param newFocus the drawable which will get the focus
    */
   public void removeDrawable(ExecutionContextCanvasGui ec, IDrawable d, IDrawable newFocus) {
      //can you remove from another thread? NOPE. 
      TopologyDLayer topo = d.getVC().getTopo();

      topo.removeDrawable(d);

      IDrawable next = newFocus;
      if (next == null) {
         next = d.getParent();
      }
      if (next == null) {
         //take highest in top
         next = topo.getTop();
      }
      gc.getFocusCtrl().newFocusKey(ec, next);
      
      InputConfig ic = ec.getInputConfig();
      ic.srActionDoneRepaint(d);
      ic.srActionDoneRepaint(next);

   }

   /**
    * Gives back the key focus and command menu to the drawable below it
    * @param d
    */
   public void shHideDrawable(ExecutionContextCanvasGui ec, IDrawable d) {
      removeDrawable(null, d, null);
   }

   /**
    * Adds the {@link IDrawable} to the {@link TopologyDLayer}.
    * <br>
    * Gives focus and load the commands
    * <br>
    * 
    * Should the Pointer focus be modified based on last x,y position in {@link InputConfig}?
    * <li>By default yes
    * <br>
    * <br>
    * <b>Type</b>:
    * <li> {@link ITechCanvasDrawable#SHOW_TYPE_0_REPLACE_BOTTOM}
    * <li> {@link ITechCanvasDrawable#SHOW_TYPE_1_OVER_TOP}
    * <li> {@link ITechCanvasDrawable#SHOW_TYPE_2_OVER_INACTIVE}
    * <br>
    * @param view
    * @param type
    */
   public void shShowDrawable(ExecutionContextCanvasGui ec, IDrawable view, int type) {
      shShowDrawable(ec, view, type, true, true);
   }

   /**
    * What is the inner Drawable focus and CmdCtx focus?
    * <br>
    * They must listen to {@link ITechDrawable#EVENT_03_KEY_FOCUS_GAIN} and dispatch
    * focus to the child drawable.
    * <br>
    * Must be called in the GUI Thread.
    * <br>
    * The {@link IDrawable} is shown in its {@link ViewContext}.
    * <br>
    * 
    * @param ic
    * @param view
    * @param type
    * @param cmds
    * @param focus
    */
   public void shShowDrawable(ExecutionContextCanvasGui ec, IDrawable view, int type, boolean cmds, boolean focus) {
      //#debug
      canvas.doThreadGUI();

      if (focus) {
         gc.getFocusCtrl().newFocusKey(ec, view);
      }

      //topolgy will sort out the type
      ViewContext vc = view.getVC();
      vc.getTopo().addDLayer(view, type);
   }

   /**
    * Draws {@link IDrawable} using {@link ITechCanvasDrawable#SHOW_TYPE_1_OVER_TOP}.
    * <br>
    * <br>
    * Drawable takes the focus and loads its cmd in the Menu bar system.
    * <br>
    * <br>
    * @param d
    */
   public void shShowDrawableOver(ExecutionContextCanvasGui ec, IDrawable d) {
      shShowDrawable(ec, d, ITechCanvasDrawable.SHOW_TYPE_1_OVER_TOP, true, true);
   }

   public void shShowDrawableOverCmds(ExecutionContextCanvasGui ec, IDrawable d) {
      shShowDrawable(ec, d, ITechCanvasDrawable.SHOW_TYPE_1_OVER_TOP, true, true);
   }

   public void shShowDrawableOverNoCmds(ExecutionContextCanvasGui ec, IDrawable d) {
      shShowDrawable(ec, d, ITechCanvasDrawable.SHOW_TYPE_1_OVER_TOP, false, true);
   }

   public void shShowDrawableOverNoFocus(ExecutionContextCanvasGui ec, IDrawable d) {
      shShowDrawable(ec, d, ITechCanvasDrawable.SHOW_TYPE_1_OVER_TOP, true, false);
   }

}
