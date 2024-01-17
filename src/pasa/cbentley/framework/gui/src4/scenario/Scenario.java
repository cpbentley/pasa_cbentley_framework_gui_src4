package pasa.cbentley.framework.gui.src4.scenario;

import pasa.cbentley.core.src4.io.BADataIS;
import pasa.cbentley.core.src4.io.BADataOS;
import pasa.cbentley.core.src4.stator.IStatorable;
import pasa.cbentley.core.src4.stator.StatorReader;
import pasa.cbentley.core.src4.stator.StatorWriter;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;

/**
 * 1 run scenario
 * 
 * <li>license agreement node is starting node. this node may have its own sub scenario
 * In this end, 
 * <li>Connection Node 
 * Link is not backable or backable to are you sure you want to exit
 *  if failure, node explanation
 *  if success, node main window
 * <li>
 * 
 * Node main window is a Tab. it has its own
 * 
 * 
 * @author Charles Bentley
 *
 */
public class Scenario extends ScenarioItemAbstract implements IStatorable {

   private IScenarioDrawableFactory factory;

   private String                   name;

   private int                      scenarioID;

   private ScenarioNode             startingNode;

   /**
    * The current node in this scenario
    */
   private ScenarioNode             nodeCurrent;

   public Scenario(GuiCtx gc) {
      super(gc);
   }

   public void stateWriteTo(StatorWriter state) {
      BADataOS dataWriter = state.getDataWriter();
      dataWriter.writeInt(scenarioID);
      dataWriter.writeString(name);
      state.stateWriteOf(startingNode);
      state.stateWriteOf(factory);
   }

   public void stateReadFrom(StatorReader state) {
      BADataIS dataReader = state.getDataReader();
      scenarioID = dataReader.readInt();
      name = dataReader.readString();
      startingNode = (ScenarioNode) state.createObject(ScenarioNode.class, startingNode);
      factory = (IScenarioDrawableFactory) state.createObject(IScenarioDrawableFactory.class, factory);
   }
}
