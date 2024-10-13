package pasa.cbentley.framework.gui.src4.scenario;

import pasa.cbentley.core.src4.stator.StatorReader;
import pasa.cbentley.core.src4.stator.StatorWriter;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

public class ScenarioNode extends ScenarioItemAbstract {

   /**
    * Could be null if not already created, 
    * Factory of Scenario create it
    */
   private IDrawable drawable;

   private int       nodeID;

   /**
    * The scenario that starts from this node.
    * null if this node is just a single drawable witout its own scenario
    */
   private Scenario  scenario;

   /**
    * The owner of this node. Never null
    */
   private Scenario  scenarioParent;

   public ScenarioNode(GuiCtx gc) {
      super(gc);
   }

   public void addLink(ScenarioNodeLink link) {

   }

   public IDrawable getNodeDrawable() {
      return drawable;
   }

   public int getStatorableClassID() {
      throw new RuntimeException("Must be implemented by subclass");
   }

   public void stateReadFrom(StatorReader state) {
      // TODO Auto-generated method stub

   }

   public void stateWriteTo(StatorWriter state) {
      // TODO Auto-generated method stub

   }

   public void stateWriteToParamSub(StatorWriter state) {
      // TODO Auto-generated method stub
   }
}
