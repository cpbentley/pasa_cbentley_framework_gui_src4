package pasa.cbentley.framework.gui.src4.utils;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesDrw;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.i8n.IString;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.datamodel.src4.mbo.MBOByteObject;
import pasa.cbentley.framework.datamodel.src4.old.objects.MObject;
import pasa.cbentley.framework.datamodel.src4.table.ITableModel;
import pasa.cbentley.framework.drawx.src4.engine.RgbImage;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.FigDrawable;
import pasa.cbentley.framework.gui.src4.core.ImageDrawable;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IFlagsToStringGui;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.table.interfaces.ITableIconable;

/**
 * Mapper that cache {@link IDrawable}.
 * <br>
 * <br>
 * When Model updates data, {@link TableView} gets events and update data in Drawable.
 * <br>
 * {@link StyleClass} to use
 * <br>
 * <br>
 * TODO For huge data sets, disable the caching.
 * <br>
 * @author Charles-Philip Bentley
 *
 */
public class DrawableMapper implements IStringable {

   /**
    * Hosts the Drawable encapsulating the Model objects
    */
   private IDrawable[]    cache;

   private IDrawable      nullDrawable;

   /**
    * 
    */
   private StyleClass     styleClass = null;

   public ByteObject[]    techSpecifics;

   protected final GuiCtx gc;

   /**
    * 
    * @param init
    */
   public DrawableMapper(GuiCtx gc, int init) {
      this.gc = gc;
      cache = new IDrawable[init];
   }

   /**
    * Sets all {@link IDrawable} to null. Attention this removes the current view state?
    * ..
    * Do we ask each drawable to save its state?
    * <br>
    * <br>
    * 
    */
   public void clearCache() {
      for (int i = 0; i < cache.length; i++) {
         if (cache[i] != null) {
            cache[i].notifyEvent(ITechDrawable.EVENT_10_STYLE_STATE_CHANGE);
            cache[i] = null;
         }
      }
   }

   //#enddebug
   /**
    * Specifically asks for an update of the cache at position index.
    * <br>
    * <br>
    * @param o
    * @param index
    * @return
    */
   public void doUpdateDrawable(Object o, int index) {
      if (cache[index] != null) {
         //the Drawable is already created. do an update if possible
         if (o instanceof String && cache[index] instanceof StringDrawable) {
            ((StringDrawable) cache[index]).setStringNoUpdate((String) o);
         } else if (o instanceof RgbImage && cache[index] instanceof ImageDrawable) {
            ((ImageDrawable) cache[index]).setImage((RgbImage) o);
         } else if (o instanceof MObject) {
            String s = ((MObject) o).getDisplayString(0);
            ((StringDrawable) cache[index]).setStringNoUpdate((String) s);
         } else if (o instanceof MBOByteObject) {
            String s = ((MBOByteObject) o).getDisplayString(0);
            ((StringDrawable) cache[index]).setStringNoUpdate((String) s);
         } else if (o instanceof MCmd) {
            //
            String s = ((MCmd) o).getLabel();
            ((StringDrawable) cache[index]).setStringNoUpdate((String) s);
         } else {
            //create new with the new type of object
            getDrawable(o, index);
         }
      } else {
         getDrawable(o, index);
      }
   }

   /**
    * No caching.
    * Does the following mapping:
    * <li>
    * <li>when object is null, {@link StringDrawable} or 
    * <li> {@link DrawableMapper#nullDrawable}
    * <li> {@link IString}
    * <li> {@link MCmd}
    * 
    * <br>
    * <br>
    * @param o
    * @return never null.
    */
   public IDrawable getDrawable(Object o) {
      IDrawable d = null;
      if (o instanceof ITableIconable) {
         o = ((ITableIconable) o).getTableIcon();
      }
      if (o instanceof IDrawable) {
         d = (IDrawable) o;
         //do we have to force the style class? TODO Not necessarily.
         d.setStyleClass(styleClass);
      } else if (o instanceof String) {
         d = new StringDrawable(gc, styleClass, (String) o);
         if (techSpecifics != null && techSpecifics[ITableModel.TECH_SPECIFIC_0_STRINGDRAWABLE] != null) {
            ((StringDrawable) d).setTechDrawable(techSpecifics[ITableModel.TECH_SPECIFIC_0_STRINGDRAWABLE]);
         }
      } else if (o instanceof IString) {
         d = new StringDrawable(gc, styleClass, (IString) o);
      } else if (o instanceof ByteObject) {
         ByteObject drw = (ByteObject) o;
         if (drw.getType() == IBOTypesDrw.TYPE_050_FIGURE) {
            d = new FigDrawable(gc, styleClass, drw);
         } else {
            throw new RuntimeException();
         }
      } else if (o instanceof RgbImage) {
         d = new ImageDrawable(gc, styleClass, (RgbImage) o);
      } else if (o instanceof MObject) {
         String s = ((MObject) o).getDisplayString(0);
         d = new StringDrawable(gc, styleClass, s);
      } else if (o instanceof MCmd) {
         //
         String s = ((MCmd) o).getLabel();
         d = new StringDrawable(gc, styleClass, s);
      } else if (o instanceof Boolean) {
         //TODO how to configure it to use a check box? When check box is changed, content is updated
         d = new StringDrawable(gc, styleClass, ((Boolean) o).toString());
      } else if (o instanceof Integer) {
         d = new StringDrawable(gc, styleClass, ((Integer) o).toString());
      }
      if (d == null) {
         if (nullDrawable != null) {
            d = nullDrawable;
         } else {
            String str = "";
            if (o != null) {
               str = o.toString();
            }
            d = new StringDrawable(gc, styleClass, str);
         }
      }
      return d;
   }

   /**
    * Retrieves {@link Drawable} at index. Object o is ignored unless null, then creates a {@link Drawable} matching for Object o type. 
    * <br>
    * <br>
    * Newly created {@link Drawable} are neither styled {@link ITechDrawable#STATE_06_STYLED}, nor layouted {@link ITechDrawable#STATE_05_LAYOUTED}. 
    * <br>
    * <br>
    * @param o
    * @param index no checks. using getSize to avoid exceptions
    * @return {@link IDrawable} or null
    */
   public IDrawable getDrawable(Object o, int index) {
      if (index < 0)
         throw new ArrayIndexOutOfBoundsException("Negative Index " + index);
      if (index >= cache.length) {
         cache = DrawableArrays.ensureCapacity(cache, index);
      }
      if (cache[index] == null) {
         cache[index] = getDrawable(o);
      }
      // update the Drawable if needed
      return cache[index];
   }

   /**
    * Sets the style class to all {@link IDrawable}.
    * @param sc
    */
   public void setStyleClass(StyleClass sc) {
      this.styleClass = sc;
      for (int i = 0; i < cache.length; i++) {
         if (cache[i] != null) {
            cache[i].setStyleClass(styleClass);
         }
      }
   }

   //#mdebug

   public String toString() {
      return Dctx.toString(this);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "DrawableMapper ");

      String sc = "StyleClass of Mapper";
      if (styleClass != null && dc.hasFlagData(gc, IFlagsToStringGui.D_FLAG_02_STYLE_CLASS)) {
         dc.nlLvl(sc, styleClass);
      } else {
         dc.nlLvl1Line(styleClass, sc);
      }

      dc.nlLvlArray("#Drawables", cache);
   }

   public void toString1Line(Dctx dc) {
      dc.root(this, "DrawableMapper");
   }

   public UCtx toStringGetUCtx() {
      return gc.getUCtx();
   }
   //#enddebug
}
