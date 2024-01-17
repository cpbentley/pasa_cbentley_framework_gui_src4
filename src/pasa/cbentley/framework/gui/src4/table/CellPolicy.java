package pasa.cbentley.framework.gui.src4.table;

import pasa.cbentley.byteobjects.src4.core.BOAbstractOperator;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.BitUtils;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ToStringStaticGui;
import pasa.cbentley.framework.gui.src4.factories.CellPolicyFactory;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOCellPolicy;

public class CellPolicy extends BOAbstractOperator implements IBOCellPolicy {

   protected final GuiCtx gc;

   public CellPolicy(GuiCtx gc) {
      super(gc.getBOC());
      this.gc = gc;
   }

   public void toStringFlag(Dctx sb, ByteObject p, int offset, int flag, String str) {
      if (p.hasFlag(offset, flag)) {
         sb.append(' ');
         sb.append(str);
      }
   }

   public void setEtalon(ByteObject tp, int i) {
      tp.setValue(OFFSET_08_ETALON1, i, 1);
      tp.setFlag(OFFSET_02_FLAG, FLAG_2_ETALON, true);
   }

   /**
    * Method used to write flagged array data at the end of a DrwParam.
    * <br>
    * <br>
    * @param array array of data item whose size must be computed
    * @param holders index 8 computes total size consumed. index 9 flags whose index has data
    * @param arrayIndex
    * @param offsets
    */
   public void size4(int[] array, int[] holders, int arrayIndex, int[] offsets) {
      int sizeConsumed = 0;
      if (array != null) {
         if (array.length == 1) {
            //flag as single value
            holders[arrayIndex + CellPolicyFactory.INDEX_ARRAY_SIZE] = 1;
         } else {
            int max = BitUtils.getMaxByteSize(array);
            sizeConsumed = 3 + (max * array.length);
            holders[arrayIndex] = max;
            holders[CellPolicyFactory.INDEX_CONSUMED_SIZE] += sizeConsumed;
            holders[CellPolicyFactory.INDEX_FLAGS] = BitUtils.setFlag(holders[CellPolicyFactory.INDEX_FLAGS], 1 << arrayIndex, true);
         }
      }
      offsets[arrayIndex + 1] = offsets[arrayIndex] + sizeConsumed;//track for the next array
   }

   /**
    * Array cannot be not null and not fit num or be of length 1
    * @param ar
    * @param num
    */
   private void throwEx(int[] ar, int num) {
      if (ar != null) {
         if (ar.length != 1 && ar.length != num) {
            throw new IllegalArgumentException("len=" + ar.length + " num=" + num);
         }
      }
   }

   public String toString(ByteObject policy, Dctx sb, String colRow) {
      if (policy != null) {
         toStringNotNull(policy, sb, colRow);
      } else {
         sb.append("NullPolicy");
         sb.append(colRow);
      }
      return sb.toString();
   }

   public void toStringNotNull(ByteObject policy, Dctx sb, String colRow) {
      sb.root(policy, colRow + "Policy");
      sb.append(" ");
      final int type = policy.get1(OFFSET_01_TYPE1);
      sb.append(ToStringStaticGui.toStringType(type));
      sb.nl();
      sb.append(colRow + "Size=" + policy.getValue(OFFSET_09_SIZE4, 4));
      sb.append(' ');
      sb.append(colRow + "Num=" + policy.getValue(OFFSET_05_CELL_NUM2, 2));
      if (type != TYPE_1_FLOW) {
         sb.append(' ');
         sb.append(colRow + "Policy=" + ToStringStaticGui.toStringPolicyType(policy.getValue(OFFSET_07_POLICY1, 1)));
      }
      int visNum = policy.getValue(OFFSET_06_NUM_VISIBLE1, 1);
      if (visNum != 0) {
         sb.append(' ');
         sb.append(colRow + "Frame=" + visNum);
      }
      if (policy.hasFlag(OFFSET_02_FLAG, IBOCellPolicy.FLAG_2_ETALON)) {
         sb.append(' ');
         sb.append("Etalon=" + policy.get1(OFFSET_08_ETALON1));
      }
      if (policy.get1(OFFSET_02_FLAG) != 0) {
         sb.append(" Flags=");
         toStringFlag(sb, policy, OFFSET_02_FLAG, FLAG_5_STRONG_FLOW, "Strong");
         toStringFlag(sb, policy, OFFSET_02_FLAG, FLAG_3_MAX, "Max");
         toStringFlag(sb, policy, OFFSET_02_FLAG, FLAG_4_RATIO_EVEN, "RatioEven");
         toStringFlag(sb, policy, OFFSET_02_FLAG, FLAG_6_CYCLICAL, "Cyclical");
         toStringFlag(sb, policy, OFFSET_02_FLAG, FLAG_7_OVERSIZE, "Oversize");
      }
      int min = policy.getValue(OFFSET_10_SIZE_MIN2, 2);
      if (min != 0) {
         sb.append(' ');
         sb.append(colRow + "MinSize=" + min);
      }
      int max = policy.getValue(OFFSET_11_SIZE_MAX2, 2);
      if (max != 0) {
         sb.append(' ');
         sb.append(colRow + "MaxSize=" + max);
      }
      toStringCellPolicyDebug(sb, policy, colRow + "Policies", FLAGZ_1_POLICIES);
      toStringArray(sb, policy, colRow + "Sizes", FLAGZ_2_SIZES);
      toStringArray(sb, policy, colRow + "minSize", FLAGZ_3_MIN_SIZES);
      toStringArray(sb, policy, colRow + "maxSize", FLAGZ_4_MAX_SIZES);
      toStringArray(sb, policy, colRow + "frames", FLAGZ_5_FRAMES);

      if (policy.hasFlag(OFFSET_02_FLAG, FLAG_2_ETALON)) {
         sb.append("Etalon=");
      }
   }

   public void toStringArray(Dctx sb, ByteObject policy, String title, int flag) {
      if (policy.hasFlag(OFFSET_04_FLAGZ, flag)) {
         int[] ar = policy.getValuesFlag(CELLP_BASIC_SIZE, OFFSET_04_FLAGZ, flag);
         sb.nl();
         sb.append(title);
         sb.append("=");
         gc.getUCtx().getIU().debugAlone(ar, sb, ",");
      }
   }

   private void toStringCellPolicyDebug(Dctx sb, ByteObject policy, String title, int flag) {
      if (policy.hasFlag(OFFSET_04_FLAGZ, flag)) {
         int[] ar = policy.getValuesFlag(CELLP_BASIC_SIZE, OFFSET_04_FLAGZ, flag);
         sb.nl();
         sb.append(title);
         sb.append(" = ");
         for (int i = 0; i < ar.length; i++) {
            sb.append(ToStringStaticGui.toStringPolicyType(ar[i]) + ",");
         }
      }
   }



}
