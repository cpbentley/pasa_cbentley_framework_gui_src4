package pasa.cbentley.framework.gui.src4.interfaces;

import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.interfaces.INavTech;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.table.TableView;

/**
 * Any {@link IDrawable} with the concept of horizontal and vertical navigation implements {@link INavigational}.
 * <br>
 * <br>
 * Navigates Focus among Focusable {@link Drawable}.
 * <br>
 * <br>
 * Controls the behavior of competing {@link IDrawable} on the same screen using only key event for navigation.
 * <br>
 * When a Navigational is active, it gets the key Nav events before all other Drawables.
 * When a Drawable is a Navigational controlled by a parent Drawable,
 * <br>
 * For {@link IDrawable} classes, they need the state {@link ITechDrawable#STYLE_06_FOCUSED_KEY} for the Nav event 
 * to be sent.
 * The {@link MasterCanvas} keeps a linked list of active {@link IDrawable} on the screen. 
 * <br>
 * 
 * If it acts on the InputConfig, the event finishes and screen is repainted.
 * <br>
 * <p>
 * A Navigational drawable may release the focus as the result of a one of the methods in this interface.
 * Using {@link Controller#focusRelease()}
 * <br>
 * <p>
 * Around the Clock behavior implies that it cannot focusout on that axis
 * <br>
 * In many ways, concepts are similar to traversal of {@link javax.microedition.lcdui.CustomItem}. In J2ME, the method {@link javax.microedition.lcdui.CustomItem#getInteractionModes()}
 * asks the phone implementation if it supports horizontal and vertical traversal. Returns 0 when none is possible.
 * Here we assume nothing. A {@link IDrawable} with both traversals will need UP-DOWN-LEFT_RIGHT keys to work.
 * In J2ME the method {@link javax.microedition.lcdui.CustomItem#traverse()} returns false when next event in that direction traverses out.
 * And the method {@link CustomItem#traverseOut()} acts like the event {@link ITechDrawable#EVENT_04_KEY_FOCUS_LOSS} 
 * <p>
 * When a {@link IDrawable} only supports horizontal navigation,  vertical navigation key events are sent to the first
 *  {@link IDrawable} in the linked list that supports vertical navigation.
 * <br>
 * <p>
 * Navigational focus is sometimes related to the state {@link ITechDrawable#STYLE_05_SELECTED} and the events
 *  {@link ITechDrawable#EVENT_03_KEY_FOCUS_GAIN}
 *  {@link ITechDrawable#EVENT_04_KEY_FOCUS_LOSS}
 * However, a Drawable may be selected with a pointer event, while the key focus stays somewhere.
 * A pointer may action a button that generate a Navigation command. This is an ergonomic use of the pointer.
 * Likewise, a drawable may have {@link ITechDrawable#STYLE_05_SELECTED} and {@link ITechDrawable#STYLE_06_FOCUSED_KEY},
 * but then lose only {@link ITechDrawable#STYLE_06_FOCUSED_KEY} but stays visually selected.
 * <br>
 * In a {@link TableView}, the cell with the {@link ITechDrawable#STYLE_05_SELECTED} and {@link ITechDrawable#STYLE_06_FOCUSED_KEY}
 * states is the same.
 * <br>
 * For Device like the P990i with a Scrolling wheel for Up/Down events, Horizontal and Vertical navigation may be merged.
 * That means scrolling down the wheel will navigate on the first row of the TableView, then go to next row etc.
 * <br>
 * @author Mordan
 *
 * @see IDrawable
 */
public interface INavigational extends INavTech {

   public void manageNavigate(ExecutionContextCanvasGui ec, int navEvent);

   /**
    * Try to navigate. If it could not navigate, because
    * of internal state reasons.
    * <br>
    * The nav command contains info about 
    * <li> {@link INavTech#NAV_1_UP}
    * <li> {@link INavTech#NAV_2_DOWN}
    * <li> {@link INavTech#NAV_3_LEFT}
    * 
    * <br>
    * <br>
    * When nav was a success, 
    * @param navCmd
    * 
    *
    */
   public void manageNavigate(CmdInstance navCmd);

   /**
    * Query current capabilities.
    * <br>
    * this allows a {@link INavigational} to disable calls to 
    * {@link INavigational#manageNavigate(CmdInstance)} because
    * @param navEvent
    * @return
    */
   public boolean canNavigate(int navEvent);
   /**
     * Call Back for Up Command
    * @param ec TODO
     *
     */
   public void navigateUp(ExecutionContextCanvasGui ec);

   /**
    * Call Back for DownCmd
    * @param ec TODO
    *
    */
   public void navigateDown(ExecutionContextCanvasGui ec);

   /**
    * Called only if {@link ITechDrawable#BEHAVIOR_27_NAV_HORIZONTAL} is true.
    * <br>
    * First call in cycle with {@link InputConfig#isFirstNavCycle()}
    * <br>
    * Second call, implements any second cycle stuff like cycles or other stuff.
    * @param ec TODO
    */
   public void navigateLeft(ExecutionContextCanvasGui ec);

   /**
    * Implement to the right horizontal traversal
    * @param ec TODO
    */
   public void navigateRight(ExecutionContextCanvasGui ec);

   /**
    * 
    * @param ec TODO
    */
   public void navigateSelect(ExecutionContextCanvasGui ec);

   /**
    * Returns an integer Flags.<br>
    * Flag {@link IDrawable#FLAG_HORIZ} is set when horizontal traversal is supported. <br>
    * Flag {@link IDrawable#FLAG_VERTI} is set when vertical traversal is supported. <br>
    * Flag {@link IDrawable#FLAG_SELECTABLE} is set when Z traversal is supported. <br>
    * <br>
    * Similar to {@link CustomItem#getInteractionModes()}
    * @return
    * 
    */
   public int getNavFlag();

}
