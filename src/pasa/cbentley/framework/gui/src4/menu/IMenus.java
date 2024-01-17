package pasa.cbentley.framework.gui.src4.menu;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.core.src4.interfaces.C;

/**
 * A Canvas may have up to 4 Menu Bars
 * <br>
 * <br>
 * Horizontal bars behave in a certain way.
 * <br>
 * <br>
 * 
 * Vertical bars take more room but there is more space to entries.
 * <br>
 * <br>
 * 
 * @author Charles-Philip Bentley
 *
 */
public interface IMenus extends IByteObject {

   public static final int MENUS_BASIC_SIZE           = A_OBJECT_BASIC_SIZE + 10;

   public static final int MENUS_OFFSET_01_FLAG       = A_OBJECT_BASIC_SIZE;

   /**
    * <li> {@link C#DIR_0_TOP}
    * <li> {@link C#DIR_1_BOTTOM}
    * <li> {@link C#DIR_2_LEFT}
    * 
    */
   public static final int MENUS_OFFSET_02_POSITION1  = A_OBJECT_BASIC_SIZE + 1;

   /**
    * How is the {@link MenuBar} displayed to the user
    * <li> {@link IMenus#SHOW_MODE_0_ALWAYS_ON} always showing and taking up screen space
    * <li> {@link IMenus#SHOW_MODE_1_ON_DEMAND}
    */
   public static final int MENUS_OFFSET_03_SHOW_MODE1 = A_OBJECT_BASIC_SIZE + 2;

   public static final int MENUS_OFFSET_04_FLAG       = A_OBJECT_BASIC_SIZE + 3;

   public static final int SHOW_MODE_0_ALWAYS_ON      = 0;

   public static final int SHOW_MODE_1_ON_DEMAND      = 1;

}
