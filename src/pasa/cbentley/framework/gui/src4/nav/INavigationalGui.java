package pasa.cbentley.framework.gui.src4.nav;

import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.engine.CmdNode;
import pasa.cbentley.framework.cmd.src4.interfaces.ITechNavCmd;
import pasa.cbentley.framework.cmd.src4.nav.INavigational;
import pasa.cbentley.framework.cmd.src4.nav.MCmdNav;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.table.TableView;

/**
 * {@link INavigationalGui} is a specialization of {@link INavigational} for {@link IDrawable}s.
 * 
 * <p>
 * A the command focus of a Device is moved with {@link MCmdNav} instances
 * The focus of the keyboard, with a few keys, we can reach any CmdNode
 * The Nav {@link CmdNode} is a patch applied to {@link INavigational}
 * Controls the behavior of competing {@link IDrawable} on the same screen using only key event for navigation.
 * <br>
 * When a Navigational is active, it gets the key Nav events before all other Drawables.
 * When a Drawable is a Navigational controlled by a parent Drawable,
 * <br>
 * For {@link IDrawable} classes, they need the state {@link ITechDrawable#STYLE_06_FOCUSED_KEY} for the Nav event 
 * to be sent.
 * </p>
 * <p>
 * A Navigational drawable may release the focus as the result of a one of the methods in this interface.
 * <br>
 * <p>
 * Around the Clock behavior implies that it cannot focusout on that axis
 * <br>
 * In many ways, concepts are similar to traversal of {@link javax.microedition.lcdui.CustomItem}. 
 * In J2ME, the method {@link javax.microedition.lcdui.CustomItem#getInteractionModes()}
 * asks the phone implementation if it supports horizontal and vertical traversal. Returns 0 when none is possible.
 * Here we assume nothing. A {@link IDrawable} with both traversals will need UP-DOWN-LEFT_RIGHT keys to work.
 * In J2ME the method {@link javax.microedition.lcdui.CustomItem#traverse()} returns false when next event in that direction traverses out.
 * And the method {@link CustomItem#traverseOut()} acts like the event {@link ITechDrawable#EVENT_04_KEY_FOCUS_LOSS} 
 * <p>
 * When a {@link IDrawable} only supports horizontal navigation,  vertical navigation key events are sent to the first
 *  {@link IDrawable} in the linked list that supports vertical navigation.
 * <br>
 * 
 * 
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
 * 
 * @author Charles-Philip Bentley
 *
 * @see IDrawable
 */
public interface INavigationalGui extends INavigational {

   /**
    * Returns an integer Flags.<br>
    * Flag {@link IDrawable#FLAG_1_HORIZ} is set when horizontal traversal is supported. <br>
    * Flag {@link IDrawable#FLAG_2_VERTI} is set when vertical traversal is supported. <br>
    * Flag {@link IDrawable#FLAG_3_SELECTABLE} is set when Z traversal is supported. <br>
    * <br>
    * Similar to {@link CustomItem#getInteractionModes()}
    * @return
    * 
    */
   public int getNavFlag();

   public void manageNavigate(ExecutionContextCanvasGui ec, int navEvent);

   /**
    * Query current capabilities.
    * <br>
    * this allows a {@link INavigationalGui} to disable calls to 
    * {@link INavigationalGui#navigateCmd(CmdInstance)} because
    * @param navEvent
    * @return
    */
   public Object navigateCheck(int navEvent);

   /**
    * Try to navigate. If it could not navigate, because
    * of internal state reasons.
    * <br>
    * The nav command contains info about 
    * <li> {@link ITechNavCmd#NAV_01_UP}
    * <li> {@link ITechNavCmd#NAV_02_DOWN}
    * <li> {@link ITechNavCmd#NAV_03_LEFT}
    * 
    * <br>
    * <br>
    * When nav was a success, 
    * @param navCmd
    * 
    *
    */
   public void navigateCmd(CmdInstance navCmd);

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
     * Call Back for Up Command
    * @param ec TODO
     *
     */
   public void navigateUp(ExecutionContextCanvasGui ec);

}
