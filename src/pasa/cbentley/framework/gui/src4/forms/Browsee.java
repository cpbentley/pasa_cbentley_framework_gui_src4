package pasa.cbentley.framework.gui.src4.forms;

import pasa.cbentley.framework.core.data.src4.db.IByteCache;
import pasa.cbentley.framework.core.data.src4.db.IByteInterpreter;
import pasa.cbentley.framework.core.data.src4.interfaces.IBoIndex;
import pasa.cbentley.framework.datamodel.src4.engine.MByteObjectEnum;
import pasa.cbentley.framework.datamodel.src4.engine.MByteObjectStore;
import pasa.cbentley.framework.datamodel.src4.engine.MByteObjectTableModel;
import pasa.cbentley.framework.datamodel.src4.filter.Filter;
import pasa.cbentley.framework.datamodel.src4.filter.FilterCondition;
import pasa.cbentley.framework.datamodel.src4.filter.IFilterable;
import pasa.cbentley.framework.datamodel.src4.filter.MFilterSet;

/**
 * The subject being browsed. Provides more fine grain control of what is shown from the List of MObjects. <br>
 * <br>
 * When starting a browse session, a {@link Browsee} is created.
 * you need
 * <li> a title
 * <li> string of the record store
 * <li> the {@link MByteObjectStore} of the record store
 * <li> a {@link IByteInterpreter} for displaying the bytes
 * 
 * <br>
 * <br>
 * The {@link RecordStoreBrowser} needs a {@link IBrowseeView}
 * <br>
 * <br>
 * {@link MByteObjectTableModel} provides the {@link MByteObjectEnum} to feed the {@link RecordStoreBrowser}
 * <br>
 * <br>
 * <br>
 * <br>
 * 
 * Model Class.
 * Provides the link to delete Objects and all related dependencies
 * <br>
 * <br>
 * 
 * Will update the View with the 
 * <br>
 * 
 * To Help the Browsing<br>
 * <br>
 * 
 * the BSession object is created
 * The BSession hosts the
 * ViewModelLink
 * The VisualIndex counter
 * The performanceflag
 * 
 * @author cbentley
 *
 */
public class Browsee {

   private IByteInterpreter ibi;


   /**
    * may be null
    */
   private IFilterable      print;

   protected String         rs;

   protected String         title;

   public Browsee(String rs, String title, IFilterable print) {
      this(rs, title, print, null);
   }

   public Browsee(String rs, String title, IFilterable print, IByteInterpreter bi) {
      this.rs = rs;
      this.title = title;
      this.print = print;
      this.ibi = bi;
   }

   public void delete(int i) {
   }

   public String getBrowseeTitle() {
      return title;
   }

   /**
    * The Data Interpreter that will interpret byte arrays and return
    * String via the ViewModelLink 
    * @return
    */
   public IByteInterpreter getByteInterpreter() {
      return ibi;
   }

   public byte[] getBytes(int rid) {
      return null;
   }

   public IByteCache getCache() {
      return null;
   }

   public IFilterable getFilterable() {
      return print;
   }

   public IBoIndex[] getIndexes(Filter[] filters) {
      int l = filters.length;
      IBoIndex[] ibs = new IBoIndex[l];
      return ibs;
   }

   public String getRecordStore() {
      return rs;
   }

   /**
    * This is called
    * @param indexes
    * @param filters
    * @return
    */
   public int[][] getRIDs(IBoIndex[] indexes, Filter[] filters) {
      int numIndexes = indexes.length;
      final int[][] rids = new int[numIndexes][];
      for (int i = 0; i < indexes.length; i++) {
         if (indexes[i] != null) {
            int[] keys =  filters[i].filterCondition.getIndexKeys(indexes[i]);
            rids[i] = indexes[i].getBIDs(keys);
         }
      }
      return rids;
   }

   public int maxSize() {
      return -1;
   }

   public void setFilterSet(IFilterable fil, MFilterSet fs) {
   }

   public void setInterpreter(IByteInterpreter ibi) {
      this.ibi = ibi;
   }

}
