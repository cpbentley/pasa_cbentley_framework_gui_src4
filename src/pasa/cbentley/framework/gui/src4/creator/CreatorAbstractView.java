package pasa.cbentley.framework.gui.src4.creator;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOViewPane;
import pasa.cbentley.framework.gui.src4.tech.ITechLinks;

public abstract class CreatorAbstractView extends CreatorAbstract {

   protected CreatorAbstractBOStyle creatorStyle;

   public CreatorAbstractView(CreateContext crc) {
      super(crc);
      creatorStyle = createCreatorStyle();
   }

   public CreatorAbstractBOStyle getCreatorStyle() {
      return creatorStyle;
   }

   protected abstract CreatorAbstractBOStyle createCreatorStyle();

   /**
    * Use for link {@link ITechLinks#LINK_66_BO_VIEWPANE}.
    * @return {@link IBOViewPane}
    */
   public abstract ByteObject getBOViewPane();

   public abstract ByteObject getBOMenu();

   public abstract StyleClass getStyleClassViewPaneHoleHeader();

   public abstract StyleClass getStyleClassViewPaneHoleSB();

   public abstract ByteObject getBOStyleViewPane();

   public abstract ByteObject getBOStyleViewPort();

}
