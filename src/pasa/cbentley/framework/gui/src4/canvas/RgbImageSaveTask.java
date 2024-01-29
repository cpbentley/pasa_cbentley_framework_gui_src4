package pasa.cbentley.framework.gui.src4.canvas;

import java.io.IOException;
import java.io.OutputStream;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.coreio.src4.IConnector;
import pasa.cbentley.framework.coreio.src4.ctx.CoreIOCtx;
import pasa.cbentley.framework.coreio.src4.file.IFileConnection;
import pasa.cbentley.framework.drawx.src4.engine.RgbImage;
import pasa.cbentley.framework.drawx.src4.image.PngEncoder;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;

public class RgbImageSaveTask extends GuiBackgroundTask implements IStringable {

   private RgbImage img;

   private String   fileName;

   public RgbImageSaveTask(GuiCtx gc, RepaintCtrlGui ctrl, RgbImage img) {
      super(gc, ctrl);

      this.img = img;
   }

   public RgbImageSaveTask(GuiCtx gc, RepaintCtrlGui ctrl) {
      super(gc, ctrl);
   }

   public String getFileNameAuto() {
      String name = gc.getAppli().getAppCtx().getConfigApp().getAppName();
      name = name + "_" + System.currentTimeMillis() + ".png";
      return name;
   }

   public void saveImage(RgbImage img, RepaintCtrlGui ctrl) {
      PngEncoder pngEncoder = gc.getDC().getPngEncoder();
      byte[] pngdata = pngEncoder.encodePNG(img);
      saveByteData(pngdata, ctrl);
   }

   public void saveByteData(byte[] pngdata, int offset, int len, RepaintCtrlGui ctrl) {
      try {
         String name = getFileName();
         if (name == null) {
            name = getFileNameAuto();
         }
         //don't need the connection protocol anymore.
         //TODO how to parametrize destination dir? external or internal storage

         CoreIOCtx coreIOCtx = gc.getCFC().getCoreIOCtx();
         IFileConnection fileConnection = coreIOCtx.getFileConnection(name);
         OutputStream os = fileConnection.openOutputStream();
         os.write(pngdata, offset, len);
         fileConnection.close();
         //screenresult
         ctrl.getScreenResultBusi().setActionDoneRepaintMessage(fileConnection.getName() + " saved", 3000);
      } catch (IOException e) {
         //#debug
         gc.toDLog().pEx("msg", this, RgbImageSaveTask.class, "saveImage", e);

         //we want a UI message feedback
         ctrl.getScreenResultBusi().setActionDoneRepaintMessage("IOException " + e.getMessage(), 6000);
      }

      ctrl.repaintDrawableCycleBusiness();
   }

   public void saveByteData(byte[] pngdata, RepaintCtrlGui ctrl) {
      saveByteData(pngdata, 0, pngdata.length, ctrl);
   }

   public void runAbstract() {
      //write the image. TODO what is the Android App is killed
      saveImage(img, ctrl);
   }

   public String getFileName() {
      return fileName;
   }

   public void setFileName(String fileName) {
      this.fileName = fileName;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "RgbImageSaveTask");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "RgbImageSaveTask");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

}
