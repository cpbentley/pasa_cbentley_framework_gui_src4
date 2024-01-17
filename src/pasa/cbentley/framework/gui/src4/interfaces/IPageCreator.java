package pasa.cbentley.framework.gui.src4.interfaces;

import pasa.cbentley.framework.coreui.src4.utils.ViewState;
import pasa.cbentley.framework.gui.src4.canvas.TopLevelCtrl;

/**
 * Create {@link IDrawable} for the {@link TopLevelCtrl} using {@link IPageCreator#createPage(int, ViewState)}.
 * <br>
 * <br>
 * This will be implemented by the view model part of the application.
 * <br>
 * <br>
 * It organizes the different screens with integer IDs. Each screen page may be shown on top of the previous
 * one, showing the previous one because its size is smaller. That's fine.
 * <br>
 * <br>
 * @author Charles-Philip Bentley
 *
 */
public interface IPageCreator {

   
   /**
    * Creates the Page matching for the ID.
    * <br>
    * <br>
    * Return null if ID is unknown.
    * <br>
    * <br>
    * TODO later create a create with ViewState. ViewStates are stored in a ID table.
    * So the {@link TopLevelCtrl} reads the ViewState if present and sets the ViewState
    * with {@link IDrawable#setViewState(mordan.bentleyfw.core.ViewState)}
    * <br>
    * <br>
    * 
    * @param id
    * @return
    */
   public IDrawable createPage(int id, ViewState vs);
}
