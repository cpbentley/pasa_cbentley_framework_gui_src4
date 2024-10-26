package pasa.cbentley.framework.gui.src4.canvas;

import java.util.Hashtable;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.core.ByteObjectFactory;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.structs.IntBuffer;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.core.data.src4.db.IByteCache;
import pasa.cbentley.framework.core.data.src4.db.IByteStore;
import pasa.cbentley.framework.core.ui.src4.utils.ViewState;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ObjectGC;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOTopLevel;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IPageCreator;

/**
 * Page Manager. Backstack manager
 * 
 * A Page is like a Screen in Android or a form in J2ME. 
 * 
 * User action generates or removes pages.
 * <br>
 * Uses of back/forward
 * <br>
 * Form/Window manager.
 * <br>
 * <br>
 * Link to {@link TopologyDLayer} ? a  top level {@link IDrawable} fully replaces the previous {@link IDrawable}
 * 
 * <br>
 * Can several pages be visible on the screen? Yes.
 * But only one is in the key focus. another one might be in the pointer focus. unless exclusivity is granted.
 * <br>
 * <br>
 * The Menu appearing is not a Top level page.
 * <br>
 * <br>
 * in MIDP 2.0 a top level Page is a form. A Page has possibly a {@link CmdCtx} providing commands.
 * <br>
 * <br>
 * Do you remember adding the Back MIDP command to all Forms? Yes.
 * Here it can automatically be added to the Page.
 * <br>
 * <br>
 * 
 * @author Charles-Philip Bentley
 *
 */
public class TopLevelCtrl extends ObjectGC implements IStringable {

   private Hashtable      cache           = new Hashtable();

   private IPageCreator[] creators        = new IPageCreator[10];

   private int            currentPageID;

   private int            maxPage;

   private IntBuffer      pageForward;

   /**
    * Does not includes current page
    */
   private IntBuffer      pageHistory;

   /**
    * Local cache of Pages. Regularly purged
    */
   private IDrawable[]    roots           = new IDrawable[10];

   /**
    * Top Level {@link ViewState} saves
    */
   private ByteObject[]   topLevels       = new ByteObject[10];

   private IByteCache     toplevelsCache;

   private int[]          topLevelsHashes = new int[10];

   public TopLevelCtrl(GuiCtx gc) {
      super(gc);
      pageForward = new IntBuffer(gc.getUC());
      pageHistory = new IntBuffer(gc.getUC());
   }

   /**
    * Adds a {@link IPageCreator}.
    * <br>
    * {@link IPageCreator} create pages from static ID registered at
    * IVi
    * @param pc
    */
   public void addCreator(IPageCreator pc) {
      for (int i = 0; i < creators.length; i++) {
         if (creators[i] == null) {
            creators[i] = pc;
            break;
         }
      }
   }

   /**
    * Returns the number of pages remaining in the history
    * <br>
    * The back is always single choice
    * @return
    */
   public int backCommand(ExecutionContextCanvasGui ic) {
      if (pageHistory.getSize() != 0) {
         //#debug
         toDLog().pFlow("", this, TopLevelCtrl.class, "backCommand", LVL_05_FINE, true);
         int backid = pageHistory.removeLast();
         if (backid == 0) {
            backid = 1;
         }
         pageForward.addInt(backid);
         showPage(ic, backid, false);
      }
      return pageHistory.getSize();
   }

   public void destroyAllButCurrent() {
      for (int i = 0; i < roots.length; i++) {
         if (i != currentPageID) {
            destroyTopLevel(i);
         }
      }
   }

   /**
    * Called when the state of all Drawable has to be saved on disk
    * ...
    * TODO but what if we want to blob everything in a single byte array?
    * 
    * <br>
    */
   public void saveAllState() {
      for (int i = 0; i < roots.length; i++) {
         IDrawable drawable = roots[i];
         if (drawable != null) {

         }
      }
   }

   /**
    * Saves page view state and removes it from the cache
    * <br>
    * <br>
    * @param id
    */
   public void destroyTopLevel(int id) {
      IDrawable drawable = roots[id];
      if (drawable != null) {

         ByteObject topl = getTopLevel(id);
         topl.removeSub(IBOTypesBOC.TYPE_037_CLASS_STATE);

         saveTopLevel(id);
      }

   }

   /**
    * Puts the current page in the history
    * <br>
    * The forward is going into a tree of possibilities.
    * This is a special trie structure.
    */
   public int forwardCommand(ExecutionContextCanvasGui gc) {
      if (pageForward.getSize() != 0) {
         int backid = pageForward.removeLast();
         if (backid == 0) {
            backid = 1;
         }
         showPage(gc, backid);
      }
      return pageForward.getSize();
   }

   /**
    * Can it returns null?
    * No never Since it at least returns the first drawable.
    * <br>
    * <br>
    * 
    * @return
    */
   public IDrawable getCurrentPage() {
      return roots[currentPageID];
   }

   /**
    * Creates the {@link IDrawable} for the given page.
    * <br>
    * If page id does not exists, 
    * <br>
    * When a page is created {@link ViewState} of that page is loaded
    * and Drawable state init
    * @param id
    * @return
    */
   public IDrawable getPage(ExecutionContextCanvasGui ec, int id) {
      Object o = cache.get(new Integer(id));
      if (o == null) {

      } else if (o instanceof IDrawable) {
         return (IDrawable) o;
      } else if (o instanceof ViewState) {

      }
      if (roots[id] == null) {
         for (int i = 0; i < creators.length; i++) {
            if (creators[i] != null) {
               ByteObject topLevel = getTopLevel(id);
               ByteObject state = topLevel.getSubFirst(IBOTypesBOC.TYPE_037_CLASS_STATE);
               ViewState vs = null; //create viewstate from byteobject data
               if (state != null) {
                  vs = new ViewState(state);
               }
               roots[id] = creators[i].createPage(id, vs);
               if (roots[id] != null) {
                  break;
               }
            }
         }
      }
      return roots[id];
   }

   public IDrawable getPage(int id) {
      Object o = cache.get(new Integer(id));
      if (o == null) {

      } else if (o instanceof IDrawable) {
         return (IDrawable) o;
      } else if (o instanceof ViewState) {

      }
      if (roots[id] == null) {
         for (int i = 0; i < creators.length; i++) {
            if (creators[i] != null) {
               ByteObject topLevel = getTopLevel(id);
               ByteObject state = topLevel.getSubFirst(IBOTypesBOC.TYPE_037_CLASS_STATE);
               ViewState vs = null; //create viewstate from byteobject data
               if (state != null) {
                  vs = new ViewState(state);
               }
               roots[id] = creators[i].createPage(id, vs);
               if (roots[id] != null) {
                  break;
               }
            }
         }
      }
      return roots[id];
   }

   public int getPageID() {
      return currentPageID;
   }

   /**
    * Reads the {@link IBOTopLevel} {@link ByteObject} from the {@link IByteStore} of top levels.
    * <br>
    * <br>
    * <br>
    * <br>
    * 
    * @param toplevel
    * @return
    */
   public ByteObject getTopLevel(int toplevel) {
      BOCtx boc = gc.getBOC();
      if (toplevelsCache == null) {
         //profile based.. appli manages several profiles. 
         String dbname = gc.getAppli().getProfiledDBName("toplevels");
         toplevelsCache = gc.getCoreDataCtx().getByteStore().getByteCache(dbname, null);
      }
      if (topLevels[toplevel] == null) {
         byte[] data = toplevelsCache.getBytes(toplevel);
         ByteObject tl = null;
         if (data == null || data.length == 0) {
            data = new byte[IBOTopLevel.TOP_BASIC_SIZE];
            tl = new ByteObject(boc, data, 0);
         } else {
            ByteObjectFactory bofac = boc.getByteObjectFactory();
            tl = bofac.createByteObjectFromWrap(data, 0);
         }
         topLevels[toplevel] = tl;
      }
      return topLevels[toplevel];
   }

   public void homeCommand(ExecutionContextCanvasGui ic) {
      showPage(ic, 1);
   }

   /**
    * 
    * @return
    */
   public int prevPage() {
      int pid = getPageID();
      return pid - 1;
   }

   public int nextPage() {
      int pid = getPageID();
      return pid + 1;
   }

   /**
    * Iteration
    * @param ic
    */
   public void nextPage(ExecutionContextCanvasGui ic) {
      int pid = gc.getTopLvl().getPageID();
      pid++;
      //
      showPage(ic, pid, true);
   }

   public void previousPage(ExecutionContextCanvasGui ic) {
      int pid = gc.getTopLvl().getPageID();
      pid--;
      //
      showPage(ic, pid, true);
   }

   public void reloadLast(ExecutionContextCanvasGui ic) {
      IDrawable newFocus = getPage(ic, currentPageID);
      newFocus.getVC().getDrawCtrlAppli().shShowDrawableOver(ic, newFocus);
   }

   /**
    * Do we resize all? No.
    */
   public void resizeEvent() {
      for (int i = 0; i < roots.length; i++) {
         if (roots[i] != null) {
            roots[i].layoutInvalidate(true);
         }
      }
   }

   /**
    * Saves the ViewState inside
    * <br>
    * <br>
    * @param toplevel
    */
   public void saveTopLevel(int toplevel) {
      if (topLevels[toplevel] != null) {
         //check if it has been changed. otherwise do not write it.
         byte[] data = topLevels[toplevel].toByteArray();
         toplevelsCache.setBytes(toplevel, data);
      }
   }

   public void setFirstPage(int pageid) {
      pageHistory.addInt(pageid);
      currentPageID = pageid;
   }

   public void showPage(ExecutionContextCanvasGui ic, int newPageid) {
      this.showPage(ic, newPageid, true);
   }

   /**
    * Loads the Page from memory or reload it using the saved {@link ViewState}.
    * <br>
    * @param newPageid
    */
   public void showPage(ExecutionContextCanvasGui ec, int newPageid, boolean addToHistory) {
      if (newPageid != currentPageID) {
         int oldPageID = currentPageID;
         IDrawable old = roots[oldPageID];
         //not yet implemtend
         //saveTopLevel(currentPageID);
         if (addToHistory) {
            pageHistory.addInt(oldPageID);
         }
         old.shHideDrawable(ec);
         IDrawable newFocus = getPage(ec, newPageid);
         newFocus.shShowDrawableOver(ec);
         currentPageID = newPageid;
      }
   }
   
   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, TopLevelCtrl.class, 400);
      toStringPrivate(dc);
      super.toString(dc.sup());
      dc.appendVarWithSpace("CurrentPageID", currentPageID);
      dc.nlLvl(pageHistory, "History");
      dc.nlLvl(pageForward, "Forward");
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, TopLevelCtrl.class, 400);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
      dc.appendVarWithSpace("CurrentPageID", currentPageID);
      dc.nlLvl1Line(pageHistory, "History");
      dc.nlLvl1Line(pageForward, "Forward");
   }

   private void toStringPrivate(Dctx dc) {
      
   }
   //#enddebug

}
