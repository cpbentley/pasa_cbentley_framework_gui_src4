package pasa.cbentley.framework.gui.src4.forms;

import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;

/**
 * Displays a filling gauge depending on the data
 * <br>
 * <br>
 * 
 * @author Charles-Philip
 *
 */
public class Gauge extends Drawable {

   private String  label;

   private boolean interactive;

   private int     maxValue;

   private int     value;

   public Gauge(GuiCtx gc, StyleClass sc, String label, boolean interactive, int maxValue, int initialValue) {
      super(gc,sc);
      this.label = label;
      this.interactive = interactive;
      this.maxValue = maxValue;
      this.value = initialValue;
   }

   public void setValue(int nv) {
      this.value = nv;
   }

   public String getLabel() {
      return label;
   }

   public void setLabel(String label) {
      this.label = label;
   }

   public boolean isInteractive() {
      return interactive;
   }

   public int getValue() {
      return value;
   }

   public void setMaxValue(int maxValue) {
      this.maxValue = maxValue;
   }

}
