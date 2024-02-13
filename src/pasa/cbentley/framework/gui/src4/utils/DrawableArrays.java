package pasa.cbentley.framework.gui.src4.utils;

import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;

public class DrawableArrays {
   
   
   public static StringDrawable[] ensureCapacity(StringDrawable[] ar, int index) {
      if (index < ar.length)
         return ar;
      StringDrawable[] nar = new StringDrawable[index + 1];
      for (int i = 0; i < ar.length; i++) {
         nar[i] = ar[i];
      }
      return nar;
   }

   /**
    * Returns the new nextempty
    * @param data
    * @param nextEmpty
    * @param d
    * @return
    */
   public static int removeDrawable(IDrawable[] data, int nextEmpty, IDrawable d) {
      for (int i = 0; i < nextEmpty; i++) {
         if (data[i] == d) {
            //we must do the animation hook is required. it might generate other animations in child
            //they all will have a layer ID to play
            data[i] = null;
            //bring back object above
            int count = 0;
            for (int j = i + 1; j < nextEmpty; j++) {
               data[i + count] = data[j];
               count++;
            }
            nextEmpty--;
         }
      }
      return nextEmpty;
   }

   /**
    * 
    * @param cache
    * @param index
    * @return
    */
   public static IDrawable[] ensureCapacity(IDrawable[] cache, int index) {
      if (index >= cache.length) {
         IDrawable[] d = new IDrawable[index + 1];
         for (int i = 0; i < cache.length; i++) {
            d[i] = cache[i];
         }
         return d;
      } else {
         return cache;
      }
   }

   /**
    * Union drawable into a single array. Remove reference duplicates.
    * <br>
    * Each array is not supposed to contain reference duplicates
    * <br>
    * Similar to {@link MUtils#getUnion(int[], int[])}
    * @param src
    * @param dest
    * @return
    */
   public static IDrawable[] getUnion(IDrawable[] src, int numSrc, IDrawable[] dest, int numDest) {
      IDrawable[] far = new IDrawable[numSrc + numDest];
      System.arraycopy(src, 0, far, 0, numSrc);
      int count = numSrc;
      for (int i = 0; i < numDest; i++) {
         if (!isContained(far, dest[i])) {
            far[count] = dest[i];
            count++;
         }
      }
      if (count == far.length) {
         return far;
      } else {
         return resize(far, count);
      }
   }

   public static boolean isContained(IDrawable[] ar, IDrawable drawable) {
      return isContained(ar, 0, ar.length, drawable);
   }

   public static boolean isContained(IDrawable[] ar, int offset, int len, IDrawable drawable) {
      for (int i = offset; i < len; i++) {
         if (ar[i] == drawable)
            return true;
      }
      return false;
   }

   /**
    * 
    * @param ar
    * @param size
    * @return
    */
   public static IDrawable[] resize(IDrawable[] ar, int size) {
      IDrawable[] oldData = ar;
      ar = new IDrawable[size];
      size = Math.min(size, oldData.length);
      System.arraycopy(oldData, 0, ar, 0, size);
      return ar;
   }

   /**
    * In effect frees up the first spot
    * @param ds
    */
   public static void shiftUp(IDrawable[] ds) {
      for (int i = ds.length - 1; i > 0; i--) {
         ds[i] = ds[i - 1];
      }
      ds[0] = null;
   }

}
