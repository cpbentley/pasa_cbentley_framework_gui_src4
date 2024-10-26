package pasa.cbentley.framework.gui.src4.table;

public class TableViewUtils {

   /**
    * Gets the integer value are index i.
    * <br>
    * <br>
    * only exception is if array is of length 1. it returns value at index 0
    * When index is bigger than array, return last value
    * <br>
    * <br>
    * @param sizes
    * @param i
    * @return integer at i or 0
    * @throws IndexOutOfBoundsException
    *             if i is too big
    */
   public static int getArrayFirstValOrIndex(int[] sizes, int i) {
      if (sizes.length == 0) {
         throw new IllegalArgumentException("No Sizes for i=" + i);
      }
      if (i >= sizes.length) {
         return sizes[sizes.length - 1];
      }
      return sizes[i];
   }

   /**
    * @param l
    * @return -1 if none selected
    */
   public static int getFirstSelected(TableView l) {
      boolean[] bs = new boolean[l.getSize()];
      l.getSelectedFlags(bs);
      for (int i = 0; i < bs.length; i++) {
         if (bs[i])
            return i;
      }
      return -1;
   }

   /**
    * 
    * @param l the List
    * @return first selected index or -1 if none
    */
   public static int getSelectedIndex(TableView l) {
      boolean[] b = new boolean[l.getSize()];
      l.getSelectedFlags(b);
      for (int i = 0; i < b.length; i++) {
         if (b[i] == true) {
            return i;
         }
      }
      return -1;
   }

   /**
    * 
    * @param l the List
    * @return first selected index or -1 if none
    */
   public static int[] getSelectedIndexes(TableView l) {
      boolean[] b = new boolean[l.getSize()];
      int si = l.getSelectedFlags(b);
      int[] ints = new int[si];
      int count = 0;
      for (int i = 0; i < b.length; i++) {
         if (b[i] == true) {
            ints[count] = i;
            count++;
         }
      }
      return ints;
   }

   public static String getSelectedString(TableView list) {
      return list.getString(getSelectedIndex(list));
   }

   public static String[] getSelectedStrings(TableView list) {
      boolean[] selectedIDs = new boolean[list.getSize()];
      int numSelected = list.getSelectedFlags(selectedIDs);
      String[] langs = new String[numSelected];
      int count = 0;
      for (int i = 0; i < selectedIDs.length; i++) {
         if (selectedIDs[i]) {
            langs[count] = list.getString(i);
         }
      }
      return langs;
   }

   /**
    * indexing starts at 0
    * @param l
    * @param index visualIndex - 1
    */
   public static void setSelectedIndex(TableView l, int index) {
      if (l.getSize() > index) {
         boolean[] bols = new boolean[l.getSize()];
         for (int i = 0; i < bols.length; i++) {
            if (i == index) {
               bols[i] = true;
            } else
               bols[i] = false;
         }
         l.setSelectedFlags(bols);
      }
   }

   /**
    * Attempts to select str in list. Deselects all others
    * @param list
    * @param str
    */
   public static void setSelectedString(TableView list, String str) {
      int size = list.getSize();
      for (int i = 0; i < size; i++) {
         if (list.getString(i).equals(str)) {
            setSelectedIndex(list, i);
            return;
         }
      }
   }

}
