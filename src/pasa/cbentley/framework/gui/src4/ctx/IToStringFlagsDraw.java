package pasa.cbentley.framework.gui.src4.ctx;

import pasa.cbentley.core.src4.ctx.IToStringFlags;

public interface IToStringFlagsDraw extends IToStringFlags {

   public static final int FLAG_DRAW_01_SCROLL_BAR_BOUNDARY      = 1 << 0;

   public static final int FLAG_DRAW_02_FIG_DRAWABLE_BOUNDARY    = 1 << 1;

   public static final int FLAG_DRAW_03_DRAWABLE_BOUNDARY        = 1 << 2;

   public static final int FLAG_DRAW_04_VIEW_DRAWABLE_BOUNDARY   = 1 << 3;

   public static final int FLAG_DRAW_05_IMAGE_DRAWABLE_BOUNDARY  = 1 << 4;

   public static final int FLAG_DRAW_07_IMAGE_BOUNDARY           = 1 << 6;

   public static final int FLAG_DRAW_06_STRING_DRAWABLE_BOUNDARY = 1 << 5;

   public static final int FLAG_DRAW_25_IGNORE_CLIP              = 1 << 24;

}
