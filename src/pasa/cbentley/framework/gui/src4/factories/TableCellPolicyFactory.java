package pasa.cbentley.framework.gui.src4.factories;

import pasa.cbentley.byteobjects.src4.core.BOAbstractFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.table.CellPolicy;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOCellPolicy;
import pasa.cbentley.framework.gui.src4.table.interfaces.ITechCell;
import pasa.cbentley.framework.gui.src4.tech.ITechLayoutDrawable;
import pasa.cbentley.layouter.src4.ctx.IBOTypesLayout;
import pasa.cbentley.layouter.src4.tech.IBOSizer;
import pasa.cbentley.layouter.src4.tech.ITechLayout;

public class TableCellPolicyFactory extends BOAbstractFactory implements IBOCellPolicy, ITechCell {
   /**
    * The number of processed array of integers
    */
   public static final int INDEX_ARRAY_SIZE    = 5;

   public static final int INDEX_CONSUMED_SIZE = 8;

   public static final int INDEX_FLAGS         = 9;

   protected final GuiCtx  gc;

   public TableCellPolicyFactory(GuiCtx gc) {
      super(gc.getBOC());
      this.gc = gc;
   }

   private void addSizer(ByteObject p, ByteObject sizer) {
      //#debug
      sizer.checkType(IBOTypesLayout.FTYPE_3_SIZER);

      p.setFlag(CELLP_OFFSET_04_FLAGZ, CELLP_FLAGZ_8_SIZER, true);
      p.addByteObject(sizer);
   }

   /**
    * An opaque {@link CellPolicy} {@link ByteObject}
    * <br>
    * <br>
    * @param type {@link IBOCellPolicy#CELLP_OFFSET_01_TYPE1}
    * @param numCells {@link IBOCellPolicy#CELLP_OFFSET_05_CELL_NUM2}
    * @param numVisible {@link IBOCellPolicy#CELLP_OFFSET_06_NUM_VISIBLE1}
    * @param policy {@link IBOCellPolicy#CELLP_OFFSET_07_POLICY1}
    * @param sizeCell {@link IBOCellPolicy#CELLP_OFFSET_09_SIZE4}
    * @param sizeMin {@link IBOCellPolicy#CELLP_OFFSET_10_SIZE_MIN2}
    * @param sizeMax {@link IBOCellPolicy#CELLP_OFFSET_11_SIZE_MAX2}
    * @return
    */
   private ByteObject get(int type, int numCells, int numVisible, int policy, int sizeCell, int sizeMin, int sizeMax) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_06_CELL_POLICY, CELLP_BASIC_SIZE);
      p.setValue(CELLP_OFFSET_01_TYPE1, type, 1);
      p.setValue(CELLP_OFFSET_05_CELL_NUM2, numCells, 2);
      p.setValue(CELLP_OFFSET_06_NUM_VISIBLE1, numVisible, 1);
      p.setValue(CELLP_OFFSET_07_POLICY1, policy, 1);
      p.setValue(CELLP_OFFSET_09_SIZE4, sizeCell, 4);
      p.setValue(CELLP_OFFSET_10_SIZE_MIN2, sizeMin, 2);
      p.setValue(CELLP_OFFSET_11_SIZE_MAX2, sizeMax, 2);
      return p;
   }

   /**
    * Fixed number of cells <br>
    * 0 means as many cells as needed.
    * <br>
    * @param numCells
    * @return DrwParam
    */
   public ByteObject getFillStrong(int numCells) {
      return get(TYPE_0_GENERIC, numCells, 0, CELL_3_FILL_STRONG, 0, 0, 0);
   }

   public ByteObject getFlow(ByteObject sizer, boolean isStrong) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_06_CELL_POLICY, CELLP_BASIC_SIZE);
      p.setValue(CELLP_OFFSET_01_TYPE1, TYPE_1_FLOW, 1);
      p.setFlag(CELLP_OFFSET_02_FLAG, CELLP_FLAG_5_STRONG_FLOW, isStrong);
      addSizer(p, sizer);
      return p;
   }

   /**
    * Flow type when the number of cells is undefined
    * <br>
    * <br>
    * Weak Flow?
    * @param sizeCell postive/implicit(0)/logical(negative)
    * @param isStrong for {@link IBOCellPolicy#CELLP_FLAG_5_STRONG_FLOW}
    * @return
    */
   public ByteObject getFlow(int sizeCell, boolean isStrong) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_06_CELL_POLICY, CELLP_BASIC_SIZE);
      p.setValue(CELLP_OFFSET_01_TYPE1, TYPE_1_FLOW, 1);
      p.setValue(CELLP_OFFSET_09_SIZE4, sizeCell, 4);
      p.setFlag(CELLP_OFFSET_02_FLAG, CELLP_FLAG_5_STRONG_FLOW, isStrong);
      return p;
   }

   /**
    * 
    * @param numCells {@link IBOCellPolicy#CELLP_OFFSET_05_CELL_NUM2}
    * @param sizer {@link IBOSizer}
    * @return
    */
   public ByteObject getGeneric(int numCells, ByteObject sizer) {

      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_06_CELL_POLICY, CELLP_BASIC_SIZE);
      p.set1(CELLP_OFFSET_01_TYPE1, TYPE_0_GENERIC);
      p.set2(CELLP_OFFSET_05_CELL_NUM2, numCells);
      p.set1(CELLP_OFFSET_07_POLICY1, CELL_1_EXPLICIT_SET);
      addSizer(p, sizer);
      return p;
   }

   /**
    * Policy is Explicit in this case, except when size is zero.
    * 
    * @param numCells
    * @param size Size of the Cells in coded 
    * @return
    */
   public ByteObject getGeneric(int numCells, int size) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_06_CELL_POLICY, CELLP_BASIC_SIZE);
      p.setValue(CELLP_OFFSET_01_TYPE1, TYPE_0_GENERIC, 1);
      p.setValue(CELLP_OFFSET_05_CELL_NUM2, numCells, 2);
      if (size == 0) {
         p.setValue(CELLP_OFFSET_07_POLICY1, CELL_0_IMPLICIT_SET, 1);
      } else {
         p.setValue(CELLP_OFFSET_07_POLICY1, CELL_1_EXPLICIT_SET, 1);
      }
      p.setValue(CELLP_OFFSET_09_SIZE4, size, 4);
      return p;
   }

   public ByteObject getGeneric(int numCells, int policy, int size, int minSize, int maxSize) {
      return get(TYPE_0_GENERIC, numCells, 0, policy, size, minSize, maxSize);
   }

   /**
    * 
    * @param numCells
    * @param policies
    * @param sizes
    * @return
    * @see TableCellPolicyFactory#getGeneric(int, int[], int[], int[], int[])
    */
   public ByteObject getGeneric(int numCells, int[] policies, int[] sizes) {
      return getGeneric(numCells, policies, sizes, null, null);
   }

   /**
    * Most generic.
    * <br>
    * <br>
    * There is cyclical phasse {@link IBOCellPolicy#CELLP_FLAG_6_CYCLICAL} when policy or size array is 2 or more
    * and the number of cells does not match.
    * <br>
    * For example, 0, {@link ITechCell#CELL_1_EXPLICIT_SET} and Size[20,40].
    * <br>
    * 
    * When numCells is bigger than policies or sizes, that means we have a cyclical policy.
    * <br>
    * <br>
    * Example Set 30, Set 50. Numcell is 0. (weak)
    * <br>
    * if any of the array is size 2 or more, those array must be of equal size
    * <br>
    * <br>
    * Integer arrays are written in append mode with start index 
    * @param numCells, when 0, weakCosize.
    * @param policies 
    * @param sizes
    * @param minSize
    * @param maxSize
    * @return
    */
   public ByteObject getGeneric(int numCells, int[] policies, int[] sizes, int[] minSize, int[] maxSize) {

      int max1 = (policies != null) ? policies.length : 0;
      int max2 = (sizes != null) ? sizes.length : 0;
      int max3 = (minSize != null) ? minSize.length : 0;
      int max4 = (maxSize != null) ? maxSize.length : 0;

      //holder index 0,1,2,3 holds the bytesize of each array
      int[] holders = new int[(2 * INDEX_ARRAY_SIZE) + 2];
      //tracks the array start offset
      int[] offsets = new int[INDEX_ARRAY_SIZE + 1];

      //start writing at the end of the fixed size.
      offsets[0] = CELLP_BASIC_SIZE;
      CellPolicy cp = gc.getCellPolicy();
      //compute the size consumed by all arrays.
      cp.size4(policies, holders, 0, offsets);
      cp.size4(sizes, holders, 1, offsets);
      cp.size4(minSize, holders, 2, offsets);
      cp.size4(maxSize, holders, 3, offsets);

      int totalSize = holders[INDEX_CONSUMED_SIZE] + CELLP_BASIC_SIZE;

      ByteObject p = gc.getBOC().getByteObjectFactory().createByteObject(IBOTypesGui.TYPE_GUI_06_CELL_POLICY, totalSize);

      p.setValue(CELLP_OFFSET_01_TYPE1, TYPE_0_GENERIC, 1);
      p.setValue(CELLP_OFFSET_05_CELL_NUM2, numCells, 2);

      if (max1 > numCells || max2 > numCells) {
         p.setFlag(CELLP_OFFSET_02_FLAG, CELLP_FLAG_6_CYCLICAL, true);
      }

      //sets the array flag 
      p.setValue(CELLP_OFFSET_04_FLAGZ, holders[INDEX_FLAGS], 1);

      //write the arrays
      if (holders[INDEX_ARRAY_SIZE] == 1) {
         p.setValue(CELLP_OFFSET_07_POLICY1, policies[0], 1);
      } else {
         p.setDynOverWriteValues(offsets[0], policies, holders[0]);
      }
      if (holders[INDEX_ARRAY_SIZE + 1] == 1) {
         p.setValue(CELLP_OFFSET_09_SIZE4, sizes[0], 4);
      } else {
         p.setDynOverWriteValues(offsets[1], sizes, holders[1]);
      }
      if (holders[INDEX_ARRAY_SIZE + 2] == 1) {
         p.setValue(CELLP_OFFSET_10_SIZE_MIN2, minSize[0], 2);
      } else {
         p.setDynOverWriteValues(offsets[2], minSize, holders[2]);
      }
      if (holders[INDEX_ARRAY_SIZE + 3] == 1) {
         p.setValue(CELLP_OFFSET_11_SIZE_MAX2, maxSize[0], 2);
      } else {
         p.setDynOverWriteValues(offsets[3], maxSize, holders[3]);
      }

      return p;
   }

   public ByteObject getGeneric(int numCells, int[] policies, int[] sizes, int[] minSize, int[] maxSize, int[] frames) {
      int max1 = (policies != null) ? policies.length : 0;
      int max2 = (sizes != null) ? sizes.length : 0;
      int max3 = (minSize != null) ? minSize.length : 0;
      int max4 = (maxSize != null) ? maxSize.length : 0;
      int max5 = (frames != null) ? frames.length : 0;
      //holder index 0,1,2,3 holds the bytesize of each array
      int[] holders = new int[(2 * INDEX_ARRAY_SIZE) + 2];
      //tracks the array start offset
      int[] offsets = new int[INDEX_ARRAY_SIZE + 1];

      //start writing at the end of the fixed size.
      offsets[0] = CELLP_BASIC_SIZE;
      //compute the size consumed by all arrays.
      //array index must match the flag!
      CellPolicy cp = gc.getCellPolicy();
      cp.size4(policies, holders, 0, offsets);
      cp.size4(sizes, holders, 1, offsets);
      cp.size4(minSize, holders, 2, offsets);
      cp.size4(maxSize, holders, 3, offsets);
      cp.size4(frames, holders, 4, offsets);

      int totalSize = holders[INDEX_CONSUMED_SIZE] + CELLP_BASIC_SIZE;

      ByteObject cellPolicy = gc.getBOC().getByteObjectFactory().createByteObject(IBOTypesGui.TYPE_GUI_06_CELL_POLICY, totalSize);

      cellPolicy.setValue(CELLP_OFFSET_01_TYPE1, TYPE_0_GENERIC, 1);
      cellPolicy.setValue(CELLP_OFFSET_05_CELL_NUM2, numCells, 2);

      if (max1 > numCells || max2 > numCells) {
         cellPolicy.setFlag(CELLP_OFFSET_02_FLAG, CELLP_FLAG_6_CYCLICAL, true);
      }

      //sets the array flag 
      cellPolicy.setValue(CELLP_OFFSET_04_FLAGZ, holders[INDEX_FLAGS], 1);

      //write the arrays
      if (holders[INDEX_ARRAY_SIZE] == 1) {
         cellPolicy.setValue(CELLP_OFFSET_07_POLICY1, policies[0], 1);
      } else {
         cellPolicy.setDynOverWriteValues(offsets[0], policies, holders[0]);
      }
      if (holders[INDEX_ARRAY_SIZE + 1] == 1) {
         cellPolicy.setValue(CELLP_OFFSET_09_SIZE4, sizes[0], 4);
      } else {
         cellPolicy.setDynOverWriteValues(offsets[1], sizes, holders[1]);
      }
      if (holders[INDEX_ARRAY_SIZE + 2] == 1) {
         cellPolicy.setValue(CELLP_OFFSET_10_SIZE_MIN2, minSize[0], 2);
      } else {
         cellPolicy.setDynOverWriteValues(offsets[2], minSize, holders[2]);
      }
      if (holders[INDEX_ARRAY_SIZE + 3] == 1) {
         cellPolicy.setValue(CELLP_OFFSET_11_SIZE_MAX2, maxSize[0], 2);
      } else {
         cellPolicy.setDynOverWriteValues(offsets[3], maxSize, holders[3]);
      }
      if (holders[INDEX_ARRAY_SIZE + 4] == 1) {
         cellPolicy.setValue(CELLP_OFFSET_12_FRAMES1, frames[0], 1);
      } else {
         cellPolicy.setDynOverWriteValues(offsets[4], frames, holders[4]);
      }
      return cellPolicy;
   }

   /**
    * Generic policy.
    * <br>
    * <br>
    * Number of cells is decided by the array size.
    * 
    * @param policies
    * @param sizes
    * @return
    * @see TableCellPolicyFactory#getGeneric(int[], int[], int[], int[])
    */
   public ByteObject getGeneric(int[] policies, int[] sizes) {
      return getGeneric(policies, sizes, null, null);
   }

   public ByteObject getGeneric(int[] policies, int[] sizes, int[] frames) {
      return getGeneric(getMaxSize(policies, sizes), policies, sizes, null, null, frames);
   }

   /**
    * Number of cells is computed from sizes array.
    * <br>
    * <br>
    * Calling this method will generate a Strong coSize, unless it is reduced afterwards.
    * @param policies
    * @param sizes
    * @param minSize
    * @param maxSize
    * @return
    */
   public ByteObject getGeneric(int[] policies, int[] sizes, int[] minSize, int[] maxSize) {
      return getGeneric(getMaxSize(policies, sizes), policies, sizes, minSize, maxSize);
   }

   public ByteObject getGenericEtalon(int numCells, int size, int etalon) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_06_CELL_POLICY, CELLP_BASIC_SIZE);
      p.setValue(CELLP_OFFSET_01_TYPE1, TYPE_0_GENERIC, 1);
      p.setValue(CELLP_OFFSET_05_CELL_NUM2, numCells, 2);
      p.setValue(CELLP_OFFSET_07_POLICY1, CELL_1_EXPLICIT_SET, 1);
      p.setValue(CELLP_OFFSET_09_SIZE4, size, 4);
      p.setValue(CELLP_OFFSET_08_ETALON1, etalon, 1);
      p.setFlag(CELLP_OFFSET_02_FLAG, CELLP_FLAG_2_ETALON, true);
      return p;
   }

   /**
    * {@link IBOCellPolicy#CELLP_FLAGP_4_IMPLICIT}
    * 
    * @param numCells
    * @return
    */
   public ByteObject getGenericImplicit(int numCells) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_06_CELL_POLICY, CELLP_BASIC_SIZE);
      p.set1(CELLP_OFFSET_01_TYPE1, TYPE_0_GENERIC);
      p.set2(CELLP_OFFSET_05_CELL_NUM2, numCells);
      p.set1(CELLP_OFFSET_07_POLICY1, CELL_0_IMPLICIT_SET);
      p.set4(CELLP_OFFSET_09_SIZE4, 0);
      return p;
   }

   public int getMaxSize(int[] ar1, int[] ar2) {
      int s = 0;
      if (ar1 != null) {
         s = ar1.length;
      }
      if (ar2 != null) {
         if (ar2.length > s)
            s = ar2.length;
      }
      return s;
   }

   /**
    * 
    * @param numCells
    * @param maxSizer
    * @return
    */
   public ByteObject getPrefered(int numCells, ByteObject maxSizer) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_06_CELL_POLICY, CELLP_BASIC_SIZE);
      p.set1(CELLP_OFFSET_01_TYPE1, TYPE_0_GENERIC);
      p.set2(CELLP_OFFSET_05_CELL_NUM2, numCells);
      p.set1(CELLP_OFFSET_07_POLICY1, CELL_0_IMPLICIT_SET);
      if (maxSizer != null) {
         p.addSub(maxSizer);
      }
      return p;
   }

   /**
    * Preferred size with a maximum to the TableView viewport.
    * <br>
    * @param mod
    * @param numCells
    * @param maxSizer
    * @return
    */
   public ByteObject getPreferedMaxView(int numCells) {
      //max is ctx content of viewport of parent
      int etalon = ITechLayout.ETALON_4_PARENT;
      //TODO viewport, scrollbar, other properties
      int etalonSub = ITechLayoutDrawable.ETALON_09_VIEWPORT;
      int etalonFunction = ITechLayout.ET_FUN_0_CTX;
      int etalonProp = ITechLayout.SIZER_PROP_05_CONTENT;
      ByteObject maxSizer = gc.getLAC().getSizerFactory().getSizerRatio100(100, etalon, etalonFunction, etalonProp);

      return getPrefered(numCells, maxSizer);
   }

   public ByteObject getRatio(int numCells, int numVisible) {
      ByteObject p = get(TYPE_2_RATIO, numCells, numVisible, CELL_2_RATIO, 0, 0, 0);
      p.setFlag(CELLP_OFFSET_02_FLAG, CELLP_FLAG_4_RATIO_EVEN, true);
      return p;
   }

   /**
    * 
    * {@link ByteObject} opaque with Even Ratio {@link IBOCellPolicy#CELLP_FLAG_4_RATIO_EVEN}
    * <br>
    * <br>
    * @param numCells total number of cells
    * @param numVisible number of cells visible
    * @param size
    * @return
    */
   public ByteObject getRatio(int numCells, int numVisible, int size) {
      ByteObject p = get(TYPE_2_RATIO, numCells, numVisible, CELL_2_RATIO, size, 0, 0);
      p.setFlag(CELLP_OFFSET_02_FLAG, CELLP_FLAG_4_RATIO_EVEN, true);
      return p;
   }

}
