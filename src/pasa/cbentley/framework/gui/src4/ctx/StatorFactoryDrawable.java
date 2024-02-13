package pasa.cbentley.framework.gui.src4.ctx;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.stator.StatorReaderBO;
import pasa.cbentley.core.src4.ctx.ICtx;
import pasa.cbentley.core.src4.stator.IStatorFactory;
import pasa.cbentley.core.src4.stator.IStatorable;
import pasa.cbentley.core.src4.stator.ITechStator;
import pasa.cbentley.core.src4.stator.StatorReader;
import pasa.cbentley.framework.gui.src4.canvas.CanvasAppliInputGui;

public class StatorFactoryDrawable implements ITechStatorableGui, IStatorFactory {

   private final GuiCtx gc;

   public StatorFactoryDrawable(GuiCtx gc) {
      this.gc = gc;
   }

   public Object[] createArray(int classID, int size) {
      switch (classID) {
         case CLASSID_01_CANVAS_DRAWABLE:
           return new CanvasAppliInputGui[size];
         default:
            break;
      }
      return null;
   }

   public boolean isSupported(IStatorable statorable) {
      if(statorable.getClass() == CanvasAppliInputGui.class) {
         return true;
      }
      return false;
   }

   public ICtx getCtx() {
      return gc;
   }

   public Object createObject(StatorReader reader, int classID) {
      switch (classID) {
         case CLASSID_01_CANVAS_DRAWABLE:
            return createCanvasDrawable(reader);
         default:
            break;
      }
      return null;
   }

   private Object createCanvasDrawable(StatorReader reader) {
      StatorReaderBO srbo = (StatorReaderBO)reader;
      srbo.checkInt(ITechStator.MAGIC_WORD_OBJECT_PARAM);
      ByteObject boCanvasHost = srbo.readByteObject();
      ByteObject boCanvasAppli = srbo.readByteObject();
      return new CanvasAppliInputGui(gc,boCanvasAppli,boCanvasHost);
   }
}
