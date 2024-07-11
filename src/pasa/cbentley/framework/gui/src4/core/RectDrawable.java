/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.layouter.src4.ctx.LayouterCtx;
import pasa.cbentley.layouter.src4.engine.Zer2DRect;
import pasa.cbentley.layouter.src4.interfaces.ILayoutable;

/**
 * Encapsulates pw,ph of a rectangle in a {@link ILayoutable} context.
 * 
 * Preferred width/height situations arise when
 * <li>Preferred size is wehn have a w sizer with 2 pozers.. that's pref size
 * The 2 pozers override the w inner size.
 * <li> Externally decided by the {@link ILayoutable} using this {@link Zer2DRect}
 * 
 * @author Charles Bentley
 */
public class RectDrawable extends Zer2DRect implements IStringable {

   private int ph;

   private int pw;

   public RectDrawable(LayouterCtx lac) {
      super(lac);
   }

   /**
    * Returns the preferred height of the rect.
    * @return int
    */
   public int getPh() {
      return ph;
   }

   /**
    * Returns the preferred width of the rect.
    * @return int
    */
   public int getPw() {
      return pw;
   }

   /**
    * Increment the preferred height of the rect by <code>incr</code>.
    * @param incr 
    */
   public void incrPh(int incr) {
      this.ph += incr;
   }

   /**
    * Increment the preferred width of the rect by <code>incr</code>.
    * @param incr 
    */
   public void incrPw(int incr) {
      this.pw += incr;
   }

   /**
    * Set the preferred height of the rect.
    * @param ph 
    */
   public void setPh(int ph) {
      this.ph = ph;
   }

   /**
    * Set the preferred height of the rect as the current height.
    */
   public void setPhAsDh() {
      ph = getH();
   }

   /**
    * Set the preferred width of the rect.
    * @param pw 
    */
   public void setPw(int pw) {
      this.pw = pw;
   }

   /**
    * Set the preferred width of the rect as the current width.
    */
   public void setPwAsDw() {
      pw = getW();
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, Zer2DRect.class, 185);
      toStringPrivate(dc);
      super.toString(dc.sup());
      dc.nl();
      dc.append(' ');
      dc.append('p');
      dc.append('[');
      dc.append(pw);
      dc.append(',');
      dc.append(ph);
      dc.append(']');
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, Zer2DRect.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   /**
    * 
    *
    * @param dc 
    */
   private void toStringPrivate(Dctx dc) {
      dc.appendVarWithSpace("pw", pw);
      dc.appendVarWithSpace("ph", ph);
   }

   //#enddebug

}
