package pasa.cbentley.framework.gui.src4.ctx;

import pasa.cbentley.core.src4.ctx.IToStringFlags;
import pasa.cbentley.framework.drawx.src4.ctx.DrwCtx;

/**
 * Flags that modifies how ToString methods include some information for {@link GuiCtx} classes.
 * 
 * @author Charles Bentley
 *
 */
public interface IToStringFlagsGui extends IToStringFlags {

   public static final int D_FLAG_01_STYLE                   = 1 << 0;

   public static final int D_FLAG_02_STYLE_CLASS             = 1 << 1;

   public static final int D_FLAG_03_STRINGER                = 1 << 2;

   public static final int D_FLAG_04_ANIMATIONS              = 1 << 3;

   public static final int D_FLAG_06_TABLE_MODEL             = 1 << 5;

   public static final int D_FLAG_07_TABLE_MAPPER            = 1 << 6;

   public static final int D_FLAG_10_TABLE_SELECTED_DRAWABLE = 1 << 9;

   public static final int D_FLAG_11_VP_HEADERS              = 1 << 10;

   public static final int D_FLAG_12_VP_SCROLLBARS           = 1 << 12;

   public static final int D_FLAG_15_VP_SCROLL_CONFIGS       = 1 << 14;

   public static final int D_FLAG_17_VIEW_PANE               = 1 << 16;

   public static final int D_FLAG_27_CACHE                   = 1 << 26;

   public static final int D_FLAG_28_MASTER_CANVAS           = 1 << 27;

}
