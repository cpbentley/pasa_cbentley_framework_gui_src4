package pasa.cbentley.framework.gui.src4.factories;

import pasa.cbentley.byteobjects.src4.core.BOAbstractOperator;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStrAuxData;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStrAuxEdit;
import pasa.cbentley.framework.gui.src4.tech.ITechLinks;

public class DrawableStringOperator extends BOAbstractOperator implements IBOStrAuxData {

   protected final GuiCtx gc;

   public DrawableStringOperator(GuiCtx gc) {
      super(gc.getBOC());
      this.gc = gc;
   }

   public void linkStringTechs(StyleClass sc) {

      ByteObject strTech = gc.getDrawableStringFactory().getBOStringDataEmpty();
      sc.linkByteObject(strTech, ITechLinks.LINK_41_BO_STRING_DATA);

      ByteObject strTechEdit = gc.getDrawableStringFactory().getBOStringEditEmpty();
      sc.linkByteObject(strTechEdit, ITechLinks.LINK_42_BO_STRING_EDIT);

   }



}
