package pasa.cbentley.framework.gui.src4.forms;

import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.framework.coredraw.src4.interfaces.IGraphics;
import pasa.cbentley.framework.coredraw.src4.interfaces.IMFont;
import pasa.cbentley.framework.coredraw.src4.interfaces.ITechGraphics;
import pasa.cbentley.framework.coreui.src4.tech.ITechCodes;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;
import pasa.cbentley.framework.gui.src4.table.TableView;

/**
 * NOW StringListInput
 * <br>
 * <br>
 * Widget to display a String, whose dataset is a list of String.
 * Special string edition that acts like a {@link StringDrawable}.
 * <br>
 * We quit T8 easy been added
 * @author Mordan
 *
 */
public class ListRoot extends StringDrawable {

   public int            fontHeight;

   /**
    * The Model of Objects that is displayed in a {@link TableView} when user press fire
    * <br>
    * <br>
    * 
    */
   private ListUIRecords listStr;

   private int           fontColor;

   private IMFont          font;

   private String        startingText;

   private boolean       active = true;

   private boolean       t9mode = true;

   private static String CHOOSE = "Choose";

   private static String EMTPY  = "Empty";

   /**
    * @param text the Text displayed as the header of the ListRoot
    */
   public ListRoot(GuiCtx gc, StyleClass sc, String text, ListUIRecords list) {
      super(gc,sc, text);
      init(text, list);
   }

   public void setActivation(boolean b) {
      active = b;
   }

   public boolean isActivated() {
      return active;
   }

   private void init(String text, ListUIRecords list) {
   }

   public ListUIRecords getList() {
      return listStr;
   }

   protected int getMinContentWidth() {
      return font.stringWidth(getText());
   }

   public String getText() {
      String s = computeText();
      if (s == null) {
         //#debug
         System.out.println("BIP:925 Null Text in ListRoot ");
         return "BIP:925";
      } else {
         return s;
      }
   }

   private String computeText() {
      if (listStr.getType() == ListUIRecords.IMPLICIT) {
         return getImplicitText();
      } else {
         return getNonImplicitText();
      }
   }

   private String getNonImplicitText() {
      boolean[] ar = new boolean[listStr.getSize()];
      int selectedSize = listStr.getSelectedFlags(ar);
      if (listStr.getSize() == 0)
         return EMTPY;
      int selIndex = ListUIRecords.getFirstSelected(listStr);
      if (selIndex == -1) {
         if (startingText != null)
            return startingText;
         return CHOOSE;
      }
      if (selectedSize == 1)
         return listStr.getString(selIndex);
      if (selectedSize > 1) {
         return listStr.getString(selIndex) + "...(" + selectedSize + ")";
      }
      return EMTPY;
   }

   /**
    * 
    * @return
    */
   private String getImplicitText() {
      if (listStr.getSize() == 0) {
         return EMTPY;
      }
      return listStr.getSelectedString();
   }

   protected int getMinContentHeight() {
      return fontHeight;
   }

   protected int getPrefContentWidth(int arg0) {
      int suw = super.getPreferredWidth();
      int id = font.stringWidth(getText());
      //#debug
      System.out.println(id + " new width for :" + getText() + ". old =" + suw);
      return id;
   }

   protected int getPrefContentHeight(int arg0) {
      return fontHeight;
   }

   protected void paint(IGraphics g, int w, int h) {
      g.setFont(font);
      String t = getText();
      int lc = listStr.getT9().getLetterCount();
      int rectanglew = 0;
      if (lc != 0) {
         g.setColor(fontColor);
         rectanglew = font.substringWidth(t, 0, lc);
         g.fillRect(0, 0, rectanglew, getPrefContentHeight(0));
      }
      g.setColor(255, 255, 255);
      g.drawString(t.substring(0, lc), 0, 0, ITechGraphics.TOP | ITechGraphics.LEFT);
      g.setColor(fontColor);
      g.drawString(t.substring(lc, t.length()), rectanglew, 0, ITechGraphics.TOP | ITechGraphics.LEFT);

   }

   public void notifyEventKeyFocusLost(BusEvent e) {
      super.notifyEventKeyFocusLost(e);
      listStr.getT9().reset();
   }

   public void manageKeyInput(InputConfig ic) {
      keyReleased(ic.getIdKeyBut());
      //super.manageKeyInput(ic);
   }

   protected void keyReleased(int keyCode) {
      switch (keyCode) {
         case ITechCodes.KEY_STAR: {
            decrement();
            break;
         }
         case ITechCodes.KEY_POUND:
            increment();
            break;
         case -8:
            //remove one letter of the T9
            minusT9();
            break;
         case ITechCodes.KEY_NUM0:
            selectX(0);
            break;
         case ITechCodes.KEY_NUM1:
            selectX(1);
            break;
         case ITechCodes.KEY_NUM2:
            selectX(2);
            break;
         case ITechCodes.KEY_NUM3:
            selectX(3);
            break;
         case ITechCodes.KEY_NUM4:
            selectX(4);
            break;
         case ITechCodes.KEY_NUM5:
            selectX(5);
            break;
         case ITechCodes.KEY_NUM6:
            selectX(6);
            break;
         case ITechCodes.KEY_NUM7:
            selectX(7);
            break;
         case ITechCodes.KEY_NUM8:
            selectX(8);
            break;
         case ITechCodes.KEY_NUM9:
            selectX(9);
            break;

         default:
            break;
      }
   }

   private void minusT9() {
      String s = listStr.getT9().decrement();
      if (s != null) {
         listStr.setSelected(listStr.getT9().getLastI());
      } else {
      }
      this.notifyStateChanged();
   }

   private void decrement() {
      if (listStr.getSize() == 0)
         return;
      int newx = listStr.getSelectedIndex() - 1;
      if (newx > 0) {
         listStr.setSelected(newx);
         this.notifyStateChanged();
      } else {
         listStr.setSelected(listStr.getSize() - 1);
         this.notifyStateChanged();
      }
   }

   private void increment() {
      if (listStr.getSize() == 0)
         return;
      int newx = listStr.getSelectedIndex() + 1;
      if (newx < listStr.getSize() - 1) {
         listStr.setSelected(newx);
         this.invalidateLayout();
         this.notifyStateChanged();
      } else {
         listStr.setSelected(0);
         this.notifyStateChanged();
      }
   }

   public void selectX(int visualIndex) {
      if (t9mode) {
         String s = listStr.getT9().increment(visualIndex);
         if (s != null) {
            listStr.setSelected(listStr.getT9().getLastI());
         } else {
            if (listStr.getT9().getLetterCount() > 0) {
            }
         }
         //System.out.println(_list.getT9().debugData());
         this.notifyStateChanged();
      } else {
         if (listStr.getSize() >= visualIndex && listStr.getSize() != 0) {
            listStr.setSelected(visualIndex - 1);
            this.notifyStateChanged();
         }
      }
   }

   /**
    * Must be repainted in the paint cycle
    */
   public void notifyStateChanged() {
      
      getRepaintCtrlDraw().repaintDrawableCycleBusiness(this);
   }

   public void setStartingText(String str) {
      startingText = str;
   }

   public String getString(int selectedIndex) {
      return listStr.getString(selectedIndex);
   }

   public void setList(ListUIRecords list) {
      listStr = list;
   }

   public ListUIRecords getRList() {
      return (ListUIRecords) listStr;
   }

   public boolean isT9mode() {
      return t9mode;
   }

   public void setT9mode(boolean t9mode) {
      this.t9mode = t9mode;
   }

}
