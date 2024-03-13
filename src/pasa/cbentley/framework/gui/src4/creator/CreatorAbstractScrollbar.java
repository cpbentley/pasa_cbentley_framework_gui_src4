package pasa.cbentley.framework.gui.src4.creator;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.framework.drawx.src4.factories.interfaces.IBOFigure;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOScrollBar;
import pasa.cbentley.framework.gui.src4.tech.ITechLinks;

/**
 * Creators of anything related Scroll
 * @author Charles Bentley
 *
 */
public abstract class CreatorAbstractScrollbar extends CreatorAbstract implements IBOScrollBar {

   protected CreatorAbstractBOStyle creatorStyle;

   public CreatorAbstractScrollbar(CreateContext gc) {
      super(gc);
      creatorStyle = createCreatorStyle();
   }

   protected boolean isWrapper     = false;

   protected boolean isWrapperFill = false;

   public void setWrapper() {
      isWrapper = true;
   }

   public void setWrapperFill() {
      isWrapperFill = true;
   }

   protected abstract CreatorAbstractBOStyle createCreatorStyle();

   /**
    * Use for link {@link ITechLinks#LINK_68_BO_V_SCROLLBAR}.
    * @return {@link IBOScrollBar}
    */
   public abstract ByteObject getBOScrollbarHorizontal();

   /**
    * Use for link {@link ITechLinks#LINK_68_BO_V_SCROLLBAR}.
    * @return {@link IBOScrollBar}
    */
   public abstract ByteObject getBOScrollbarVertical();

   /**
    * TBLR directional figure for uses inside wrappers.
    * 
    * Use for link {@link ITechLinks#LINK_49_FIG_SCROLLBAR_WRAPPER}.
    * @return {@link IBOFigure}
    */
   public abstract ByteObject getBOFigWrapper();

   public abstract StyleClass getStyleClassScrollbarHoriz();

   public abstract StyleClass getStyleClassScrollbarVert();

   /**
    * <p>
    * Use for link {@link ITechLinks#LINK_50_STYLE_SCROLLBAR_BLOCK_BG}.
    * </p>
    * @return {@link StyleClass}
    */
   public abstract StyleClass getStyleClassBlockBgHoriz();

   /**
    * <p>
    * Use for link {@link ITechLinks#LINK_51_STYLE_SCROLLBAR_BLOCK_FIG}.
    * </p>
    * @return {@link StyleClass}
    */
   public abstract StyleClass getStyleClassBlockFigure();

   /**
    * <p>
    * Use for link {@link ITechLinks#LINK_52_STYLE_SCROLLBAR_TOP_LEFT_WRAPPER}.
    * </p>
    * @return {@link StyleClass}
    */
   public abstract StyleClass getStyleClassWrapperTopLeft();

   /**
    * <p>
    * Use for link {@link ITechLinks#LINK_53_STYLE_SCROLLBAR_BOT_RIGHT_WRAPPER}.
    * </p>
    * @return {@link StyleClass}
    */
   public abstract StyleClass getStyleClassWrapperBotRight();

}
