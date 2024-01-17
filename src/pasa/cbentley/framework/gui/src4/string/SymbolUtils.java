package pasa.cbentley.framework.gui.src4.string;

import pasa.cbentley.framework.coredraw.src4.interfaces.IMFont;

public class SymbolUtils {

   public static int[]  planes      = new int[] { 0, 1, 2, 3, 4, 14, 32, 33, 35, 36, 38, 48 };

//   public static char[] commonChars = new char[] { '!', '?', '.', ',', ';', ':', '&', '�', '$', '�', '�', '{', '}', '#', '@', '%', '^', '*', '(', ')', '[', ']', '<', '>', '|', '=', '+', '-', '_',
//         '"', '\'', '\\', '/', '�' };
   
   public static char[] commonChars = new char[] { '!', '?', '.', ',', ';', ':', '&', '', '$', '£', '°', '{', '}', '#', '@', '%', '^', '*', '(', ')', '[', ']', '<', '>', '|', '=', '+', '-', '_',
      '"', '\'', '\\', '/', '§' };

   /**
    * Empiric method to filter useful and usefull characters
    * <br>
    * <br>
    * @param plane
    * @param i
    * @return
    */
   public static boolean accept(int plane, int i) {
      switch (plane) {
         case 0: {
            if (i == 127)
               return false;
            if (i <= 31)
               return false;
            if (i > 127 && i < 223)
               return false;
            return true;
         }
         case 1: {
            if (i < 128)
               return true;
            if (i == 160 || i == 161 || i == 175 || i == 176)
               return true;
            return false;
         }
         case 3: {
            if (i == 116 || i == 117 || i == 122 || i == 126)
               return true;
            if (i <= 131)
               return false;
            if (i == 139 || i == 162 || i == 141)
               return false;
            if (i >= 207)
               return false;
            return true;
         }
         case 4: {
            if (i == 0 || i == 93)
               return false;
            if (i > 95)
               return false;
            return true;
         }
         case 14: {
            if (i == 0 || i == 59 || i == 60 || i == 61 || i == 62)
               return false;
            if (i < 92)
               return true;
            return false;
         }
         case 32:
            if (i == 21 || i == 24 || i == 25 || i == 28 || i == 29 || i == 34 || i == 37 || i == 38)
               return true;
            if (i < 52)
               return false;
            return true;
         case 33:
            if (i == 3 || i == 9 || i == 22 || i == 33 || i == 34 || i == 43)
               return true;
            if (i < 96)
               return false;
            if (i >= 144 && i <= 147)
               return true;
            if (i >= 150 && i <= 153)
               return true;
            if (i == 210 || i == 212 || i == 214 || i == 215 || i == 216 || i == 217)
               return true;
            return false;
         case 35:
            if (i == 8 || i == 11 || i == 18)
               return true;
            return false;
         case 36:
            if (i >= 96 && i <= 115)
               return true;
            return false;
         case 38:
            if (i == 5 || i == 6 || i == 64 || i == 66 || i == 106 || i == 109 || i == 111)
               return true;
            return false;
         case 48: {
            if (i == 4)
               return false;
            if (i == 48)
               return true;
            if (i >= 32 && i <= 64)
               return false;
            return true;
         }
         default:
            return true;
      }
   }

   public static char[] getPlaneChars(int plane) {
      char[] fgs = new char[256];
      int count = 0;
      for (int i = 0; i < 256; i++) {
         if (accept(plane, i)) {
            fgs[count] = (char) ((plane << 8) + i << 0);
            count++;
         }
      }
      char[] end = new char[count];
      for (int i = 0; i < count; i++) {
         //	    System.out.println(fgs[i]);
         end[i] = fgs[i];
      }
      return end;
   }

   public static char[] getPlaneChars(int plane, boolean filter) {
      if (filter)
         return getPlaneChars(plane);
      char[] fgs = new char[256];
      for (int i = 0; i < 256; i++) {
         fgs[i] = (char) ((plane << 8) + i << 0);
      }
      return fgs;
   }

   public static char[] getPlaneChars(int plane, boolean nonEmpty, IMFont f) {
      char[] fgs = new char[256];
      for (int i = 0; i < 256; i++) {
         fgs[i] = (char) ((plane << 8) + i);
      }
      if (nonEmpty) {
         //we have to remove those who don't draw anything on screen
         return fgs;
      } else {
         return fgs;
      }
   }

   public static String[] getPlaneCharStrings(int plane) {
      char[] fgs = new char[256];
      int count = 0;
      for (int i = 0; i < 256; i++) {
         if (accept(plane, i)) {
            fgs[count] = (char) ((plane << 8) + i << 0);
            count++;
         }
      }
      String[] end = new String[count];
      for (int i = 0; i < count; i++) {
         //	    System.out.println(fgs[i]);
         end[i] = String.valueOf(fgs[i]);
      }
      return end;
   }

}
