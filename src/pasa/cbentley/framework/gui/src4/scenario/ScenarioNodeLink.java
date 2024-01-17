package pasa.cbentley.framework.gui.src4.scenario;

import pasa.cbentley.core.src4.stator.StatorReader;
import pasa.cbentley.core.src4.stator.StatorWriter;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;

public class ScenarioNodeLink extends ScenarioItemAbstract {

   private ScenarioNode nodeOrigin;

   private ScenarioNode nodeDestination;

   private boolean isBackable;
   
   public ScenarioNodeLink(GuiCtx gc) {
      super(gc);
   }

   public void stateWriteTo(StatorWriter state) {
      // TODO Auto-generated method stub

   }

   public void stateReadFrom(StatorReader state) {
      // TODO Auto-generated method stub

   }

}
