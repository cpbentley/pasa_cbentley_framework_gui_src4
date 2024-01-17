package pasa.cbentley.framework.gui.src4.scenario;

import pasa.cbentley.core.src4.stator.IStatorable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

/**
 * @author Charles Bentley
 *
 */
public interface IScenarioDrawableFactory extends IStatorable {

   /**
    * Use the node/scenario data to create the right drawable for it
    * @param node
    * @return
    */
   public IDrawable createDrawable(ScenarioNode node);
}
