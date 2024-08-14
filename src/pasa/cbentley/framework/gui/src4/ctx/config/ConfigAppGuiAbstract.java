package pasa.cbentley.framework.gui.src4.ctx.config;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.core.framework.src4.app.ConfigAppAbstract;
import pasa.cbentley.framework.core.ui.src4.tech.ITechInputConstants;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;

public abstract class ConfigAppGuiAbstract extends ConfigAppAbstract {

   public ConfigAppGuiAbstract(UCtx uc, String name) {
      super(uc, name);
   }

   
   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, ConfigAppGuiAbstract.class, "@line5");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {
      
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ConfigAppGuiAbstract.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug
   

}
