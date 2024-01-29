package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.byteobjects.src4.core.ByteObject;

public class CanvasBOHelper implements IBOCanvasAppliGui {

   private CanvasAppliInputGui canvas;

   public CanvasBOHelper(CanvasAppliInputGui canvas) {
      this.canvas = canvas;
   }

   public int getFontScreenRatio() {
      return getTechCanvasAppli().get1(CANVAS_APP_DRW_OFFSET_10_FONT_SCREEN_RATIO1);
   }

   /**
    * {@link IBOCanvasAppliGui}
    * @return
    */
   private ByteObject getTechCanvasAppli() {
      return canvas.getBOCanvasAppli();
   }

   /**
    * {@link IBOCanvasAppliGui#CANVAS_APP_DRW_OFFSET_09_PAINT_MODE1}
    * @return
    */
   public int getPaintMode() {
      return getTechCanvasAppli().get1(CANVAS_APP_DRW_OFFSET_09_PAINT_MODE1);
   }

   public int getDebugBarPosition() {
      return getTechCanvasAppli().get1(CANVAS_APP_DRW_OFFSET_05_DEBUG_BAR_POSITION1);
   }

   public int getDebugMode() {
      return getTechCanvasAppli().get1(CANVAS_APP_DRW_OFFSET_07_DEBUG_MODE1);
   }

   public int getMenuBarPosition() {
      return getTechCanvasAppli().get1(CANVAS_APP_DRW_OFFSET_04_MENU_BAR_POSITION1);
   }

   public boolean isUsingMenuBar() {
      boolean useMenuBar = getTechCanvasAppli().hasFlag(CANVAS_APP_DRW_OFFSET_01_FLAG1, CANVAS_APP_DRW_FLAG_4_USE_MENU_BAR);
      return useMenuBar;
   }
   

}
