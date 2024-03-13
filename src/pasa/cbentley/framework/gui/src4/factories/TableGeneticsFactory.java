package pasa.cbentley.framework.gui.src4.factories;

import pasa.cbentley.byteobjects.src4.core.BOAbstractFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOGenetics;

public class TableGeneticsFactory extends BOAbstractFactory implements IBOGenetics {

   protected final GuiCtx gc;

   public TableGeneticsFactory(GuiCtx gc) {
      super(gc.getBOC());
      this.gc = gc;
   }

   public  ByteObject getGenetics(boolean isStatic, boolean isDrwSelectability, boolean isModelSelectability) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_03_TABLE_GENETICS, GENE_BASIC_SIZE);
      p.setFlag(GENE_OFFSET_01_FLAG, GENE_FLAG_2_STATIC, isStatic);
      p.setFlag(GENE_OFFSET_01_FLAG, GENE_FLAG_6_SELECTABILITY_DRAWABLE, isDrwSelectability);
      p.setFlag(GENE_OFFSET_01_FLAG, GENE_FLAG_7_SELECTABILITY_MODEL, isModelSelectability);
      return p;
   }

   public  ByteObject getGenetics(ByteObject titles, boolean is, boolean isDrwSelectability, boolean isModelSelectability) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_03_TABLE_GENETICS, GENE_BASIC_SIZE);
      p.setFlag(GENE_OFFSET_01_FLAG, GENE_FLAG_2_STATIC, is);
      p.setFlag(GENE_OFFSET_01_FLAG, GENE_FLAG_6_SELECTABILITY_DRAWABLE, isDrwSelectability);
      p.setFlag(GENE_OFFSET_01_FLAG, GENE_FLAG_7_SELECTABILITY_MODEL, isModelSelectability);
      
      return p;
   }

   public  ByteObject getGenetics(int map) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_03_TABLE_GENETICS, GENE_BASIC_SIZE);
      p.set2(GENE_OFFSET_05_INT_MAPPER_TYPE2, map);
      p.setFlag(GENE_OFFSET_01_FLAG, GENE_FLAG_5_MAPPERINT_TYPE, true);
      return p;
   }
   
   public  void addTitlesString(ByteObject gen, String[] ar) {
      ByteObject bo = gc.getBOC().getLitteralStringFactory().getLitteralArrayString(ar);
      gen.setFlag(GENE_OFFSET_02_FLAGX, GENE_FLAGX_1_TITLE_STRING_DEF, true);
      gen.addSub(bo);
   }

   public  void addTitlesString(ByteObject gen, int[] ar) {
      ByteObject bo = gc.getBOC().getLitteralIntFactory().getLitteralArray(ar);
      gen.setFlag(GENE_OFFSET_02_FLAGX, GENE_FLAGX_2_TITLE_INT_DEF, true);
      gen.addSub(bo);
   }

   /**
    * 
    * @param geneW
    * @param geneH
    * @return
    */
   public  ByteObject getGenetics(int geneW, int geneH) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_GUI_03_TABLE_GENETICS, GENE_BASIC_SIZE);
      p.setValue(GENE_OFFSET_03_WIDTH2, geneW, 2);
      p.setValue(GENE_OFFSET_04_HEIGHT2, geneH, 2);
      p.setFlag(GENE_OFFSET_01_FLAG, GENE_FLAG_3_SAME_SIZE_W, true);
      p.setFlag(GENE_OFFSET_01_FLAG, GENE_FLAG_4_SAME_SIZE_H, true);
      return p;
   }

}
