package pasa.cbentley.framework.gui.src4.string;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.core.src4.event.IEventConsumer;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdListener;
import pasa.cbentley.framework.coreui.src4.tech.ITechCodes;
import pasa.cbentley.framework.coreui.src4.utils.ViewState;
import pasa.cbentley.framework.datamodel.src4.table.ITableModel;
import pasa.cbentley.framework.datamodel.src4.table.ObjectTableModel;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.string.Stringer;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.core.LayouterEngineDrawable;
import pasa.cbentley.framework.gui.src4.core.ScrollConfig;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.table.interfaces.ITechTable;
import pasa.cbentley.framework.gui.src4.tech.ITechStringDrawable;

/**
 * Special {@link StringDrawable} that acts like a {@link StringDrawable}.
 * <br>
 * <br>
 * Previously known as ListRoot.
 * Provides a String choosen from a given list.
 * <br>
 * When fire/action button is pressed, the list is shown as {@link TableView}. List items can be hidden.
 * managed.
 * <br>
 * <br>
 * StringListInput registers as a {@link ICmdListener} for the {@link TableView} Select command.
 * When it gets it, it hides the TableView and show the selected item in its stuff.

 * <li>hide
 * <li>add
 * <li>delete
 * <br> 
 * We quit T8 easy been added
 * 
 * In the {@link TableView}, Page down is right and page up is left as Tech Flags.
 * <br>
 * <br>
 * For the String draw, {@link Stringer} does it. This class only modifies its specific text effect index range
 * <br>
 * We want the virtual caret to show at least 2 letters in front. Same behavior as {@link ITechStringDrawable#PRESET_CONFIG_1_TITLE} in edit mode.
 * <br>
 * <br>
 *  Therefore
 * <br>
 * <br>
 * The {@link TableView} keeps the selected index. When drawing, {@link StringListInput}.
 * <br>
 * <br>
 * It remembers the last selected in its user-state
 * <br>
 * 7/08/14 When no keyboard, just do a list pop. with a search field on top, which if focus is given, opens up the virtual keyboard
 * <br>
 * @author Charles-Philip Bentley
 *
 */
public class StringListInput extends StringDrawable implements IEventConsumer {

   private static String CHOOSE   = "Choose";

   private static String EMTPY    = "Empty";

   /**
    * Flag in tech of StringListInput.
    */
   private boolean       isT9mode = true;

   /**
    * This drawable is only created on demand. So how is tracked the selected id? Using {@link ViewState}.
    * <br>
    * <br>
    * So this field can be null.
    */
   TableView             listView;

   private int           selectedModelIndex;

   /**
    * If not null, links the List to the DataModel representing the type of data listed by the component.
    * <br>
    * <br>
    * It allow to get a list of all objects of this type
    */
   ITableModel           model;

   /**
    * Title to show when displaying possiblities.
    */
   String                tableTitle;

   private T9            t9;

   public StringListInput(GuiCtx gc, StyleClass styleKey) {
      super(gc, styleKey, "");
   }

   /**
    * 
    */
   public void consumeEvent(BusEvent e) {
      if (e.getProducer() == listView) {
         if (e.getEventID() == ITechTable.EVENT_ID_00_SELECT) {
            //no parameter. just read the selected index from the TableView
            selectedModelIndex = listView.getSelectedIndex();
            updateString((InputConfig) e.getParamO2());
            //remove TV from view and send a full repaint
            //call this when inside User Event Thread. otherwise no repaint will be made.
            listView.removeDrawable((InputConfig) e.getParamO2(), this);
            e.setFlag(BusEvent.FLAG_1_ACTED, true);
         }
      }
   }
   public void initViewDrawable(LayouterEngineDrawable ds) {
      super.initViewDrawable(ds);
   }
   public void updateString(InputConfig ic) {
      if (model != null) {
         int selectedIndex = selectedModelIndex;
         String str = model.getObject(selectedIndex).toString();
         super.setStringNoUpdate(str);
         if (listView != null && ic != null) {
            listView.setSelectedIndex(selectedIndex, ic, false);
         }
      } else {
         setStringNoUpdate(EMTPY);
      }
   }

   public void drawViewDrawableContent(GraphicsX g, int x, int y, ScrollConfig scX, ScrollConfig scY) {
      //modify the characters offsets for the alternative style
      super.drawViewDrawableContent(g, x, y, scX, scY);
   }

   private void keyFunctions(int keyCode, InputConfig ic) {
      switch (keyCode) {
         case ITechCodes.KEY_STAR: {
            t9Decrement(ic);
            break;
         }
         case ITechCodes.KEY_POUND:
            t9Increment(ic);
            break;
         case -8:
            //remove one letter of the T9
            minusT9(ic);
            break;
         case ITechCodes.KEY_NUM0:
            selectX(ic, 0);
            break;
         case ITechCodes.KEY_NUM1:
            selectX(ic, 1);
            break;
         case ITechCodes.KEY_NUM2:
            selectX(ic, 2);
            break;
         case ITechCodes.KEY_NUM3:
            selectX(ic, 3);
            break;
         case ITechCodes.KEY_NUM4:
            selectX(ic, 4);
            break;
         case ITechCodes.KEY_NUM5:
            selectX(ic, 5);
            break;
         case ITechCodes.KEY_NUM6:
            selectX(ic, 6);
            break;
         case ITechCodes.KEY_NUM7:
            selectX(ic, 7);
            break;
         case ITechCodes.KEY_NUM8:
            selectX(ic, 8);
            break;
         case ITechCodes.KEY_NUM9:
            selectX(ic, 9);
            break;

         default:
            break;
      }
   }

   /**
    * Forwards to the {@link StringEditModule}.
    * <br>
    * <br>
    * When Enter/Fire is done, an INavigational Forward is done. 
    * Container hosting the {@link StringListInput} will know where to direct the key focus.
    * <br>
    * <br>
    * 
    */
   public void manageKeyInput(InputConfig ic) {
      if (ic.isFireP()) {
         //
         showTable(ic);
      }
      if (ic.isPressed()) {
         keyFunctions(ic.getIdKeyBut(), ic);
      }
      if (!ic.isActionDone()) {
         super.manageKeyInput(ic);
      }
   }

   /**
    * Display a button for the pointer to show the table of choices
    */
   public void managePointerInput(InputConfig ic) {

      if (ic.isPressed() && hasStateStyle(ITechDrawable.STYLE_08_PRESSED)) {
         showTable(ic);
      } else {
         super.managePointerInput(ic);
      }
   }

   /**
    * Removes a letter
    * @param ic TODO
    */
   private void minusT9(InputConfig ic) {
      String s = t9.decrement();
      if (s != null) {
         int newselected = t9.getLastI();
         selectedModelIndex = newselected;
         updateString(ic);
      } else {
      }
   }

   public static final int LAYER_1_SELECTION = 1;

   public void notifyEventKeyFocusGain(BusEvent e) {
      if (model != null && t9 != null) {
         Stringer st = getStringer();
         int layerID = LAYER_1_SELECTION;
         ByteObject fx = null; //TODO background style definition
         st.getStyleLayer(layerID).removeAllAndSetInterval(0, t9.getLetterCount(), fx);

      }
      //prevent the launch string edit control
      super.notifyEventKeyFocusGain(e);
   }

   /**
    * 
    */
   public void notifyEventKeyFocusLost(BusEvent e) {
      if (model != null && t9 != null) {
         Stringer st = getStringer();
         int layerID = LAYER_1_SELECTION;
         st.getStyleLayer(layerID).removeAllIntervals();
      }
      super.notifyEventKeyFocusLost(e);
   }

   /**
    * The key index
    * @param visualIndex
    */
   public void selectX(InputConfig ic, int visualIndex) {
      if (isT9mode) {
         String s = t9.increment(visualIndex);
         if (s != null) {
            int index = t9.getLastI();
            //set selected to table selection model, which generate an event for the TableView
            selectedModelIndex = index;
            updateString(ic);
         } else {
            if (t9.getLetterCount() > 0) {
            }
         }
      } else {
      }
      //update the stringer and ask for a relayout/repaint of item
      Stringer st = getStringer();
      int layerID = LAYER_1_SELECTION;
      ByteObject fx = null; //TODO background style definition
      st.getStyleLayer(layerID).removeAllAndSetInterval(0, t9.getLetterCount(), fx);
   }

   public void setList(ITableModel list) {
      this.model = list;
      String[] data = new String[model.getSizeModel()];
      for (int i = 0; i < data.length; i++) {
         data[i] = model.getObject(i).toString();
      }
      t9 = new T9(gc.getUCtx(), data);
   }

   public void setList(String[] list) {
      model = new ObjectTableModel(gc.getDMC(), list);
      t9 = new T9(gc.getUCtx(), list);
   }

   public void showTable(InputConfig ic) {
      if (model != null) {
         if (listView == null) {
            StyleClass symbolStyleClass = this.styleClass.getStyleClass(IBOTypesGui.LINK_45_STRING_LIST_VIEW);
            if (symbolStyleClass == null) {
               symbolStyleClass = this.styleClass;
            }
            listView = new TableView(gc, symbolStyleClass, model);
            listView.setParent(this);
            //listView.setBehaviorFlag(IDrawable.BEHAVIOR_20_FULL_CANVAS_W, true);
            //listView.setBehaviorFlag(IDrawable.BEHAVIOR_21_FULL_CANVAS_H, true);
            listView.setShrink(true, true);

            //how do you adapt this behavior to a big screen? It is not convenient or at least it should become a 2/3 columns if space allows for that
            int lw = getVC().getWidth();
            int lh = getVC().getHeight();
            listView.init(lw, lh);
            //position the table based on the area
            listView.setXY(this.getX(), this.getY());
            listView.addEventListener(this, ITechTable.EVENT_ID_00_SELECT);
         }
         //show it over
         listView.shShowDrawable(ic, ITechCanvasDrawable.SHOW_TYPE_1_OVER);
         ic.srActionDoneRepaint();
      }
   }

   private void t9Decrement(InputConfig ic) {
      if (model != null) {
         int modelSize = model.getSizeModel();
         if (modelSize == 0) {
            return;
         } else {
            selectedModelIndex--;
            if (selectedModelIndex < 0) {
               selectedModelIndex = modelSize - 1;
            }
         }
         if (listView != null) {
            listView.setSelectedIndex(selectedModelIndex, ic, false);
         }
         updateString(ic);
         ic.srActionDoneRepaint(this);
      }
   }

   /**
    * Displays next element from model.
    */
   private void t9Increment(InputConfig ic) {
      if (model != null) {
         int modelSize = model.getSizeModel();
         if (modelSize == 0) {
            return;
         } else {
            selectedModelIndex++;
            if (selectedModelIndex >= modelSize) {
               selectedModelIndex = 0;
            }
         }
         if (listView != null) {
            listView.setSelectedIndex(selectedModelIndex, ic, false);
         }
         updateString(ic);
         ic.srActionDoneRepaint(this);
      }
   }
}
