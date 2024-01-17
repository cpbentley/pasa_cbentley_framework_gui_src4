package pasa.cbentley.framework.gui.src4.mui;

public interface IMLog {

   /**
    * Constant @value.
    */
   public static final int TYPE_1_ERROR    = 1;

   public static final int TYPE_3_CRITICAL = 3;

   /**
    * {@value}
    */
   public static final int TYPE_2_WARNING  = 2;

   /**
    * {@value}
    */
   public static final int TYPE_0_MSG      = 0;

   /**
    * {@value}
    */
   public static final int TYPE_MAX        = 3;

   public void log(String msg);

   public void log(String msg, int type);

   /**
    * 
    * @param bip
    * @param msg
    * @param type
    */
   public void log(int bip, String msg, int type);
}
