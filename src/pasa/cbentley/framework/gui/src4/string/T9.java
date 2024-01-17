package pasa.cbentley.framework.gui.src4.string;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.utils.StringUtils;

/**
 * From a set of number 0 to 9, match letters and strings.
 * <br>
 * <br>
 * Small class for small data set.
 * <br>
 * <br>
 * Convert an strings 
 * <br>
 * <br>
 * Allows to return a string matching an incrementing key serie
 * 
 * @author Charles-Philip Bentley
 *
 */
public class T9 implements IStringable {

   public int[][] initT9(String[] s) {
      int[][] t9 = new int[s.length][];
      for (int i = 0; i < s.length; i++) {
         t9[i] = StringUtils.getT9Translate(s[i]);
      }
      return t9;
   }

   /**
    * The word coded in numbers.
    */
   int[]                currentword = new int[20];

   /**
    * Each string is translated to T9 numbers.
    */
   int[][]              dataT9;

   /**
    * next position
    */
   int                  indexCaret  = 0;

   /**
    * the last word that returned a positive match.
    */
   int                  lastWord    = 0;

   /**
    * Array repository from which String references will be returned.
    */
   String[]             strings;

   protected final UCtx uc;

   /**
    * Initialize the {@link T9} with the data model.
    * @param s
    */
   public T9(UCtx uc, String[] s) {
      this.uc = uc;
      strings = s;
      dataT9 = initT9(strings);
   }

   /**
    * Returns null when nothing to decrement.
    * 
    * @return
    */
   public String decrement() {
      if (indexCaret != 0) {
         indexCaret--;
         return getString(currentword[indexCaret]);
      } else {
         return null;
      }
   }

   /**
    * Last index
    * @return
    */
   public int getLastI() {
      return lastWord;
   }

   /**
    * The number of letters used for matching a String.
    * <br>
    * <br>
    * 
    * @return
    */
   public int getLetterCount() {
      return indexCaret;
   }

   /**
    * Gets the String matching the current word augmented by the given key.
    * <br>
    * <br>
    * If it match a word, adds the key to current word. Else return null.
    * <br>
    * <br>
    *  
    * @param key
    * @return
    */
   private String getString(int key) {
      for (int i = 0; i < dataT9.length; i++) {
         int[] strData = dataT9[i];
         //check if current word supports current caret position and given key
         if (indexCaret < strData.length) {
            boolean isWordMatching = true;
            //check from 0 to index if
            for (int j = 0; j <= indexCaret; j++) {
               if (currentword[j] != strData[j]) {
                  isWordMatching = false;
               }
            }
            if (isWordMatching) {
               lastWord = i;
               return strings[i];
            }
         }
      }
      return null;
   }

   /**
    * Increment T9 search with a new key
    * <br>
    * <br>
    * Return the String that match the augmented pattern.
    * <br>
    * <br>
    * Null if none.
    * <br>
    * <br>
    * 
    * @param key
    * @return
    */
   public String increment(int key) {
      currentword[indexCaret] = key;
      String s = getString(key);
      if (s != null) {
         indexCaret++;
      }
      return s;
   }

   public void reset() {
      indexCaret = 0;
      lastWord = 0;
      for (int i = 0; i < currentword.length; i++) {
         currentword[i] = 0;
      }
   }

   //#mdebug
   public IDLog toDLog() {
      return toStringGetUCtx().toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "T9");
      toStringPrivate(dc);
      for (int i = 0; i < strings.length; i++) {
         dc.append(strings[i]);
         dc.append('=');
         for (int j = 0; j < dataT9[i].length; j++) {
            dc.append(dataT9[i][j]);
         }
         dc.append(" index=" + indexCaret + " lasti=" + lastWord);
         dc.append(':');
         for (int j = 0; j < currentword.length; j++) {
            dc.append(currentword[j]);
         }
         dc.nl();
      }
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "T9");
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return uc;
   }

   //#enddebug

}
