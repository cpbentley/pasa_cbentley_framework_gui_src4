package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;

/**
 * Top level Gui element.
 * 
 * When user want to show a new page, he may want to do it 
 * <li>Over the current Page 
 * <li>on another Screen if available.
 * <li> Or in a new tab in a Pager
 * @author Charles Bentley
 *
 */
public class DrawableReferencePage extends DrawableReference {

   private int pageID;

   public DrawableReferencePage(GuiCtx sc) {
      super(sc);
   }

   public int getPageID() {
      return pageID;
   }

   public void setPageID(int pageID) {
      this.pageID = pageID;
   }

}
