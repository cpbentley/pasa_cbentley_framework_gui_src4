package pasa.cbentley.framework.gui.src4.forms;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.structs.IntToStrings;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.cmd.src4.engine.CmdNode;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdsCmd;
import pasa.cbentley.framework.cmd.src4.interfaces.ICommandable;
import pasa.cbentley.framework.datamodel.src4.engine.MByteObjectTableModel;
import pasa.cbentley.framework.datamodel.src4.interfaces.IObjectStore;
import pasa.cbentley.framework.datamodel.src4.mbo.MBOByteObject;
import pasa.cbentley.framework.datamodel.src4.table.ITableModel;
import pasa.cbentley.framework.drawx.src4.engine.RgbImage;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.ImageText;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IValidable;
import pasa.cbentley.framework.gui.src4.string.InputRequestStr;
import pasa.cbentley.framework.gui.src4.string.T9;
import pasa.cbentley.framework.gui.src4.table.TableLayoutView;
import pasa.cbentley.framework.gui.src4.table.TableView;

/**
 * Graphical List that keeps track of the record id and the record store of the data.
 * <br>
 * <br>
 * Unlike the {@link BrowseSession} stuff, this class is used for simple types with a small dataset.
 * <br>
 * Can display an Image taken from an object property
 * <br>
 * <br>
 * 
 * This class is a {@link TableView} with a 1 column policy. <br>
 * A 2 columns policy is used when list items have separated {@link RgbImage}. It may not be necessary if
 * {@link ImageText} drawable is used. <br>
 * <br>
 * Uses a Object BluePrint to know the behavior of that object and retrieve Database connection methods
 * to it.
 * 
 * Possible actions on the List <br>
 * <li>Manage: 
 * <li>Hide stop showing.
 * <li>Show All. Unhides temporarily all.
 * <li>Select command, hides list and previous drawable
 * 
 * <br>
 * <br>
 * TODO server side data using JSON?
 * How do you display that? Next requests and Previous. Transparently from the source.
 * 
 * @author cbentley
 *
 */
public class ListUIRecords extends TableLayoutView implements IValidable {

   /**
    * Drawable which will be shown when List is hidden.
    */
   private Drawable              foward;

   private RgbImage[]            images;

   private boolean               isSorted;

   private MByteObjectTableModel model;

   /**
    * List Model
    */
   IntToStrings                  itsmodel;

   /**
    * If not null, links the List to the DataModel representing the type of data listed by the component.
    * <br>
    * <br>
    * It allow to get a list of all objects of this type
    */
   private IObjectStore          mobjectLink;

   // solves polish bug about selection
   private int                   selectedImplicit = 0;

   public static final int       IMPLICIT         = 0;

   public static final int       EXPLICIT         = 1;

   /**
    * select is implicit or not
    */
   private int                   typel;

   private int                   OPTIONS_STATE_ID = 0;

   private T9                    t9;

   protected String              title;

   private CmdCtx                cc;

   private CmdNode           ctxListUI;

   public ListUIRecords(GuiCtx gc, StyleClass sc, ByteObject pol, ITableModel model) {
      super(gc, sc);

      //which commands to use here? the command management 

   }

   public String getTitle() {
      return title;
   }

   public void addCmds(ICommandable com, CmdNode cc, MCmd[] cmds) {
      ctxListUI = cc.createChildCtx("ListUI" + title);
      ctxListUI.addMenuCmd(ICmdsCmd.CMD_04_OK);
      ctxListUI.addMenuCmd(ICmdsCmd.CMD_05_CANCEL);
      ctxListUI.setListener(com);
   }

   /**
    * 
    */
   public void appendAll() {
      if (images != null) {
         for (int i = 0; i < itsmodel.strings.length; i++) {
            this.formAppend(itsmodel.strings[i], images[i]);
         }
      } else {
         for (int i = 0; i < itsmodel.strings.length; i++) {
            this.formAppend(itsmodel.strings[i], null);
         }
      }
   }

   public IDrawable getFoward() {
      return foward;
   }

   /**
    * Return the model id
    * If model is empty, returns the Visual Index
    * @return 0
    */
   public int getSelectedID() {
      boolean[] selectedIDs = new boolean[getSize()];
      getSelectedFlags(selectedIDs);
      for (int i = 0; i < selectedIDs.length; i++) {
         if (selectedIDs[i]) {
            return itsmodel.ints[i];
         }
      }
      return 0;
   }

   public int[] getSelectedIDs() {
      boolean[] selectedIDs = new boolean[getSize()];
      int numSelected = getSelectedFlags(selectedIDs);
      int[] langs = new int[numSelected];
      int count = 0;
      for (int i = 0; i < selectedIDs.length; i++) {
         if (selectedIDs[i]) {
            langs[count] = itsmodel.ints[i];
         }
      }
      return langs;
   }

   public int getSelectedIndex() {
      return selectedImplicit;
   }

   public String getSelectedString() {
      if (itsmodel.strings.length == 0) {
         return "Emtpy";
      }
      if (selectedImplicit < itsmodel.strings.length) {
         return itsmodel.strings[selectedImplicit];
      }
      return "BIP:653 " + selectedImplicit + " is not < than " + itsmodel.strings.length;
   }

   /**
    * Gets the little T9
    * @return
    */
   public T9 getT9() {
      return t9;
   }

   public int getType() {
      return typel;
   }

   public void hide() {
   }

   private void init(IntToStrings its, RgbImage[] imgs, int type) {
      typel = type;
      itsmodel = its;
      t9 = new T9(gc.getUCtx(), itsmodel.strings);
      if (imgs != null) {
         if (imgs.length == its.ints.length) {
            images = imgs;
         } else {
            //#debug
            System.out.println("BIP:356 Non Null Image array is not the same length as the String array ");
         }
      }
   }

   public void setFoward(Drawable foward) {
      this.foward = foward;
   }

   public void setIts(IntToStrings its) {
      this.itsmodel = its;
   }

   public void setOptionsID(int id) {
      OPTIONS_STATE_ID = id;
   }

   /**
    * Modifies the selected element in the List
    * @param index
    */
   public void setSelected(int index) {
      //polish is buggy with list selection
      unSelect();
      selectedImplicit = index;
      ListUIRecords.setSelectedIndex(this, index);
   }

   public void setSelectedId(int id) {
      for (int i = 0; i < itsmodel.ints.length; i++) {
         if (itsmodel.ints[i] == id) {
            selectedImplicit = id;
            break;
         }
      }
   }

   public void setSorted(boolean sort) {
      isSorted = sort;
   }

   public void setT9(T9 t9) {
      this.t9 = t9;
   }

   public String toString() {
      return "UI:" + getTitle();
   }

   public void unSelect() {
      boolean[] selectedIDs = new boolean[getSize()];
      for (int i = 0; i < selectedIDs.length; i++) {
         selectedIDs[i] = false;
      }
      setSelectedFlags(selectedIDs);
   }

   /**
    * Call Back for validating the new String object. <br>
    * User was shown a {@link InputRequestStr} for write a new object string. <br>
    * <br>
    * When OK button is pressed, {@link IValidable#stringValidation(String)} is called.
    * New data is saved in this method.
    * <br>
    * InputRequest is hidden and this class is shown again.
    * @param s
    */
   public void stringValidation(String s) {
      if (s != null && !s.equals("")) {
         //adds a new object
         if (mobjectLink != null) {
            MBOByteObject mo = mobjectLink.getPrint().createInstance(s.toCharArray(), 0, s.length());
            mobjectLink.saveMObject(mo);
            if (mo.getRID() > 0) {
               model.enumClearReload();
            }
         } else {
            //#debug
            toDLog().pData("StoreCache is null for " + getTitle(), this, ListUIRecords.class, "stringValidation", LVL_09_WARNING, true);
         }
      } else {
         //display an info message TODO are we in UserEventThread or Business thread? how do we know?
         getCanvas().getRepaintCtrl().getScreenResult().setActionDoneRepaintMessage("Empty String was not saved");
      }
   }
}
