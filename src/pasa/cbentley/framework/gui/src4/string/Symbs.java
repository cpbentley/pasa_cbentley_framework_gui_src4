package pasa.cbentley.framework.gui.src4.string;

import pasa.cbentley.framework.core.ui.src4.interfaces.BCodes;
import pasa.cbentley.framework.core.ui.src4.tech.ITechCodes;

public class Symbs {

   public static final int      CHAR_SET_0_DEFAULT = 0;

   public static final int      CHAR_SET_3_EN      = 3;

   public static final int      CHAR_SET_1_Fr      = 1;

   public static final int      CHAR_SET_2_Ru      = 2;

   public static final String   CHAR_SET_EN        = "En";

   public static final String   CHAR_SET_FR        = "Fr";

   public static final String   CHAR_SET_RU        = "Ru";

   static final char[]          RU_NUM1_CHARS      = new char[] { '1', '.', '?', '!' };

   static final char[]          RU_NUM2_CHARS      = new char[] { 'а', 'б', 'в', 'г', '2' };

   static final char[]          RU_NUM3_CHARS      = new char[] { 'д', 'е', 'ё', 'ж', 'з', '3' };

   static final char[]          RU_NUM4_CHARS      = new char[] { 'и', 'й', 'к', 'л', '4' };

   static final char[]          RU_NUM5_CHARS      = new char[] { 'м', 'н', 'о', 'п', '5' };

   static final char[]          RU_NUM6_CHARS      = new char[] { 'р', '\u0441', 'т', 'у', '6' };

   static final char[]          RU_NUM7_CHARS      = new char[] { 'ф', 'х', 'ц', 'ч', '7' };

   static final char[]          RU_NUM8_CHARS      = new char[] { 'ш', 'щ', 'ъ', 'ы', '8' };

   /**
    * For some archane reason just a few characters cannot be mapped with ant....
    */
   static final char[]          RU_NUM9_CHARS      = new char[] { 'ь', '\u044D', 'ю', '\u044F', '9' };

   static final char[]          RU_NUM0_CHARS      = new char[] { ' ', '0' };

   static final char[]          EN_NUM1_CHARS      = new char[] { '1', '.', ',', '?', '!' };

   static final char[]          EN_NUM2_CHARS      = new char[] { 'a', 'b', 'c', '2' };

   static final char[]          EN_NUM3_CHARS      = new char[] { 'd', 'e', 'f', '3' };

   static final char[]          EN_NUM4_CHARS      = new char[] { 'g', 'h', 'i', '4' };

   static final char[]          EN_NUM5_CHARS      = new char[] { 'j', 'k', 'l', '5' };

   static final char[]          EN_NUM6_CHARS      = new char[] { 'm', 'n', 'o', '6' };

   static final char[]          EN_NUM7_CHARS      = new char[] { 'p', 'q', 'r', 's', '7' };

   static final char[]          EN_NUM8_CHARS      = new char[] { 't', 'u', 'v', '8' };

   static final char[]          EN_NUM9_CHARS      = new char[] { 'w', 'x', 'y', 'z', '9' };

   static final char[]          EN_NUM0_CHARS      = new char[] { ' ', '0' };

   static final char[]          FR_NUM1_CHARS      = new char[] { '1', '.', ',', '?', '!' };

   static final char[]          FR_NUM2_CHARS      = new char[] { 'a', 'b', 'c', 'à', 'â', 'ç', '2' };

   static final char[]          FR_NUM3_CHARS      = new char[] { 'd', 'e', 'f', 'é', 'è', 'ê', '3' };

   static final char[]          FR_NUM4_CHARS      = new char[] { 'g', 'h', 'i', 'î', '4' };

   static final char[]          FR_NUM5_CHARS      = new char[] { 'j', 'k', 'l', '5' };

   static final char[]          FR_NUM6_CHARS      = new char[] { 'm', 'n', 'o', 'ô', '6' };

   static final char[]          FR_NUM7_CHARS      = new char[] { 'p', 'q', 'r', 's', '7' };

   static final char[]          FR_NUM8_CHARS      = new char[] { 't', 'u', 'v', 'ù', '8' };

   static final char[]          FR_NUM9_CHARS      = new char[] { 'w', 'x', 'y', 'z', '9' };

   static final char[]          FR_NUM0_CHARS      = new char[] { ' ', '0' };

   /**
    * For each 0=9 keys, the character for that key
    */
   public static final char[][] setEnglish         = new char[][] { EN_NUM0_CHARS, EN_NUM1_CHARS, EN_NUM2_CHARS, EN_NUM3_CHARS, EN_NUM4_CHARS, EN_NUM5_CHARS, EN_NUM6_CHARS, EN_NUM7_CHARS,
         EN_NUM8_CHARS, EN_NUM9_CHARS             };

   public static final char[][] setFrench          = new char[][] { FR_NUM0_CHARS, FR_NUM1_CHARS, FR_NUM2_CHARS, FR_NUM3_CHARS, FR_NUM4_CHARS, FR_NUM5_CHARS, FR_NUM6_CHARS, FR_NUM7_CHARS,
         FR_NUM8_CHARS, FR_NUM9_CHARS             };

   public static final char[][] setRussian         = new char[][] { RU_NUM0_CHARS, RU_NUM1_CHARS, RU_NUM2_CHARS, RU_NUM3_CHARS, RU_NUM4_CHARS, RU_NUM5_CHARS, RU_NUM6_CHARS, RU_NUM7_CHARS,
         RU_NUM8_CHARS, RU_NUM9_CHARS             };

   public static final int      MAX_CHARSET_ID     = 2;

   public static String[]       charsets           = new String[] { "En", "Fr", "Ru" };

   public static final int      CTYPE_0_ENGLISH    = 0;

   public static final int      CTYPE_1_FRENCH     = 1;

   public static final int      CTYPE_2_RUSSIAN    = 2;

   public static String getCharSetName(int id) {
      return charsets[id];
   }

   public static char[][] getCharSet(int id) {
      switch (id) {
         case 0:
            return setEnglish;
         case 1:
            return setFrench;
         case 2:
            return setRussian;
         default:
            throw new IllegalArgumentException();
      }
   }

   /**
    * Returns the charset associated with the [0 1 2 3 4 5 6 7 8 9] key.
    * @param set
    * @param key {@link ITechCodes#KEY_NUM0} to {@link ITechCodes#KEY_NUM9}
    * @return
    */
   public static char[] getKeyCharSet(char[][] set, int key) {
      key -= 48;
      if (key < 0 || key > set.length) {
         throw new IllegalArgumentException("key=" + key + " (" + BCodes.getChar(key) + ") set length=" + set.length);
      }
      return set[key];
   }
}
