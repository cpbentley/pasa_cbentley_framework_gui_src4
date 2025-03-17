package pasa.cbentley.framework.gui.src4.nav;

import pasa.cbentley.framework.cmd.src4.interfaces.ITechNavCmd;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

/**
 * 
 * @author Charles Bentley
 *
 */
public interface ITechNavCmdGui extends ITechNavCmd {

   /**
    * Replaces the previous {@link IDrawable} in the {@link TopologyTBLRNav}
    */
   public static final int NAV_TOPO_OP_0_OVER   = 0;

   /**
    * 
    */
   public static final int NAV_TOPO_OP_1_INSERT = 1;

}
