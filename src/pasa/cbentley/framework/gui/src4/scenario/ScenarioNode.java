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

   /**
    * The owner of this node. Never null
    */
   private Scenario scenarioParent;

   /**
    * The scenario that starts from this node.
    * null if this node is just a single drawable witout its own scenario
    */
   private Scenario scenario;
   
   private int nodeID;

   public ScenarioNode(GuiCtx gc) {
      super(gc);
   }

   public void addLink(ScenarioNodeLink link) {
      
   }
   
   public void stateWriteTo(StatorWriter state) {
      // TODO Auto-generated method stub
      
   }

   public void stateReadFrom(StatorReader state) {
      // TODO Auto-generated method stub
      
   }

   
   public IDrawable getNodeDrawable() {
      return drawable;
   }
}
