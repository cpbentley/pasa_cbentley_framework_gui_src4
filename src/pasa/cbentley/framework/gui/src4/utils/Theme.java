package pasa.cbentley.framework.gui.src4.utils;

/**
 * Framework supports hot reload of theme or "skin" without closing and restarting the application.
 * <br>
 * <br>
 * A theme is a ClassKey. <br>
 * So an application must implements a way to sk
 * <br>
 * <br>
 * Vision impaired theme are created at this level.
 * <br>
 * Technical options
 * Similarly, BIG mode switch is done here as well.
 * Big mode shows minimum information using the biggest font available.
 * <br>
 * <br>
 * User may change randomly color theme. 
 * opposing color switch may prevent Font color to be similar to bg color.
 * <br>
 * <br>
 * @author Charles-Philip Bentley
 *
 */
public interface Theme {

   public static final int KEY_0BACKGROUND_COLOR     = 0;

   public static final int KEY_1BORDER_COLOR         = 1;

   public static final int KEY_2FONT_COLOR           = 2;

   public static final int KEY_3BACKGROUND_COLOR_BIS = 3;

   public static final int KEY_4BORDER_COLOR_BIS     = 4;

}
