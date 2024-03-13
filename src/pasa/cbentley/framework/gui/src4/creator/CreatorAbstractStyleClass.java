package pasa.cbentley.framework.gui.src4.creator;

import pasa.cbentley.framework.gui.src4.core.ScrollBar;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.tech.ITechLinks;

public abstract class CreatorAbstractStyleClass extends CreatorAbstract {

   protected CreatorAbstractScrollbar creatorScrollbar;

   protected CreatorAbstractString    creatorString;

   protected CreatorAbstractBOStyle   creatorStyle;

   protected CreatorAbstractTable     creatorTable;

   protected CreatorAbstractView      creatorView;

   public CreatorAbstractStyleClass(CreateContext gc) {
      super(gc);
      creatorString = createCreatorString();
      creatorScrollbar = createCreatorScrollbar();
      creatorStyle = createCreatorStyle();
      creatorTable = createCreatorTable();
      creatorView = createCreatorView();
   }

   protected abstract CreatorAbstractScrollbar createCreatorScrollbar();

   protected abstract CreatorAbstractString createCreatorString();

   protected abstract CreatorAbstractBOStyle createCreatorStyle();

   protected abstract CreatorAbstractTable createCreatorTable();

   protected abstract CreatorAbstractView createCreatorView();

   public CreatorAbstractScrollbar getCreatorScrollbar() {
      return creatorScrollbar;
   }

   public CreatorAbstractString getCreatorString() {
      return creatorString;
   }

   public CreatorAbstractBOStyle getCreatorStyle() {
      return creatorStyle;
   }

   public CreatorAbstractTable getCreatorTable() {
      return creatorTable;
   }

   public CreatorAbstractView getCreatorView() {
      return creatorView;
   }

   public abstract StyleClass getStyleClassFigure();

   /**
    * Returns the {@link StyleClass} used for the {@link ScrollBar}.
    * 
    * <p>
    * It is the one linked to the root and viewpane with {@link ITechLinks#LINK_58_STYLE_VIEWPANE_HOLE_HEADER} 
    * </p>
    * @return
    */
   public abstract StyleClass getStyleClassHoleHeader();

   /**
    * Returns the root {@link StyleClass} of this creator.
    * @return
    */
   public abstract StyleClass getStyleClassRoot();

   /**
    * Returns the {@link StyleClass} used for the block background of the {@link ScrollBar}.
    * 
    * <p>
    * It is the one linked to the root and viewpane with {@link ITechLinks#LINK_50_STYLE_SCROLLBAR_BLOCK_BG} 
    * </p>
    * @return
    */
   public abstract StyleClass getStyleClassScrollbarBlockBg();

   /**
    * Returns the {@link StyleClass} used for the block figure of the {@link ScrollBar}.
    * 
    * <p>
    * It is the one linked to the root and viewpane with {@link ITechLinks#LINK_51_STYLE_SCROLLBAR_BLOCK_FIG} 
    * </p>
    * @return
    */
   public abstract StyleClass getStyleClassScrollbarBlockFigure();

   /**
    * Returns the {@link StyleClass} used for the horizontal {@link ScrollBar}.
    * 
    * <p>
    * It is the one linked to the root and viewpane with {@link ITechLinks#LINK_71_STYLE_VIEWPANE_H_SCROLLBAR} 
    * </p>
    * 
    * The returned {@link StyleClass} has the following links
    * <li> {@link ITechLinks#LINK_49_FIG_SCROLLBAR_WRAPPER}
    * <li> {@link ITechLinks#LINK_50_STYLE_SCROLLBAR_BLOCK_BG}
    * <li> {@link ITechLinks#LINK_51_STYLE_SCROLLBAR_BLOCK_FIG}
    * <li> {@link ITechLinks#LINK_52_STYLE_SCROLLBAR_TOP_LEFT_WRAPPER}
    * <li> {@link ITechLinks#LINK_53_STYLE_SCROLLBAR_BOT_RIGHT_WRAPPER}
    * @return
    */
   public abstract StyleClass getStyleClassScrollbarH();

   /**
    * Returns the {@link StyleClass} used for the vertical {@link ScrollBar}.
    * 
    * <p>
    * It is the one linked to the root and viewpane with {@link ITechLinks#LINK_72_STYLE_VIEWPANE_V_SCROLLBAR} 
    * </p>
    * @return
    */
   public abstract StyleClass getStyleClassScrollbarV();

   /**
    * Returns the {@link StyleClass} used for the botright wrapper of the {@link ScrollBar}.
    * 
    * <p>
    * It is the one linked to the root and viewpane with {@link ITechLinks#LINK_53_STYLE_SCROLLBAR_BOT_RIGHT_WRAPPER} 
    * </p>
    * @return
    */
   public abstract StyleClass getStyleClassScrollbarWrapperBotRight();

   /**
    * Returns the {@link StyleClass} used for the topleft wrapper of the {@link ScrollBar}.
    * 
    * <p>
    * It is the one linked to the root and viewpane with {@link ITechLinks#LINK_52_STYLE_SCROLLBAR_TOP_LEFT_WRAPPER} 
    * </p>
    * @return
    */
   public abstract StyleClass getStyleClassScrollbarWrapperTopLeft();

   public abstract StyleClass getStyleClassSelected();

   public abstract StyleClass getStyleClassTextReader();

   public abstract StyleClass getStyleClassTitle();

   public abstract StyleClass getStyleClassViewPane();

}
