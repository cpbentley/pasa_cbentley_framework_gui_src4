package pasa.cbentley.framework.gui.src4.creator;

import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.objects.color.ColorRepo;
import pasa.cbentley.byteobjects.src4.objects.color.ColorSet;
import pasa.cbentley.core.src4.utils.interfaces.IColors;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ObjectGC;

public class CreateContext extends ObjectGC {

   protected final ColorRepo colorRepo;

   protected final BOCtx     boc;

   public CreateContext(GuiCtx gc) {
      super(gc);

      boc = gc.getBOC();
      colorRepo = new ColorRepo(boc);

      ColorSet set = colorRepo.getActive();
      set.setColor(IColorsKey.COLOR_01_FONT_1, IColors.FULLY_OPAQUE_BLACK);
      set.setColor(IColorsKey.COLOR_02_FONT_2, IColors.FULLY_OPAQUE_BEIGE);
      set.setColor(IColorsKey.COLOR_03_FONT_3, IColors.FULLY_OPAQUE_SKY_BLUE);
      set.setColor(IColorsKey.COLOR_06_BG_1, IColors.FULLY_OPAQUE_CYAN);
      set.setColor(IColorsKey.COLOR_07_BG_2, IColors.FULLY_OPAQUE_GREY);
      set.setColor(IColorsKey.COLOR_08_BG_3, IColors.FULLY_OPAQUE_YELLOW);
      set.setColor(IColorsKey.COLOR_22_BORDER_1, IColors.FULLY_OPAQUE_SKY_BLUE);
      set.setColor(IColorsKey.COLOR_23_BORDER_2, IColors.FULLY_OPAQUE_SKY_GREEN);
      set.setColor(IColorsKey.COLOR_24_BORDER_3, IColors.FULLY_OPAQUE_BLUE);
      set.setColor(IColorsKey.COLOR_51_CONTENT_1, IColors.FULLY_OPAQUE_RED);
      set.setColor(IColorsKey.COLOR_52_CONTENT_2, IColors.FULLY_OPAQUE_SKY_GREEN);
      set.setColor(IColorsKey.COLOR_53_CONTENT_3, IColors.FULLY_OPAQUE_BEIGE);
      set.setColor(IColorsKey.COLOR_66_FG_1, IColors.FULLY_OPAQUE_PURPLE);
      set.setColor(IColorsKey.COLOR_67_FG_2, IColors.FULLY_OPAQUE_BLACK);
   }

   public ColorRepo getRepo() {
      return colorRepo;
   }
}
