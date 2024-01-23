package pasa.cbentley.framework.gui.src4.string;

import java.util.Timer;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.ctx.IEventsCore;
import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.core.src4.event.IEventConsumer;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.structs.IntToStrings;
import pasa.cbentley.core.src4.thread.IBRunnable;
import pasa.cbentley.core.src4.thread.PulseThread;
import pasa.cbentley.framework.cmd.src4.interfaces.INavTech;
import pasa.cbentley.framework.core.src4.interfaces.IBOHost;
import pasa.cbentley.framework.coredraw.src4.interfaces.IMFont;
import pasa.cbentley.framework.coreui.src4.ctx.CoreUiCtx;
import pasa.cbentley.framework.coreui.src4.ctx.IEventsCoreUI;
import pasa.cbentley.framework.coreui.src4.tech.ITechCodes;
import pasa.cbentley.framework.coreui.src4.tech.ITechHostUI;
import pasa.cbentley.framework.datamodel.src4.table.ObjectTableModel;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.factories.interfaces.IBOBox;
import pasa.cbentley.framework.drawx.src4.string.Stringer;
import pasa.cbentley.framework.drawx.src4.style.IBOStyle;
import pasa.cbentley.framework.gui.src4.canvas.CanvasAppliDrawable;
import pasa.cbentley.framework.gui.src4.canvas.CanvasResultDrawable;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.canvas.InputStateDrawable;
import pasa.cbentley.framework.gui.src4.canvas.MEventDrawable;
import pasa.cbentley.framework.gui.src4.canvas.RepaintCtrlDrawable;
import pasa.cbentley.framework.gui.src4.canvas.TopologyDLayer;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.DrawableInjected;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStringDrawable;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStringEdit;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawListener;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.menu.MenuBar;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.tech.ITechStringDrawable;
import pasa.cbentley.framework.gui.src4.utils.DrawableUtilz;
import pasa.cbentley.framework.input.src4.interfaces.ITechPaintThread;
import pasa.cbentley.powerdata.spec.src4.guicontrols.T9PrefixTrieSearch;
import pasa.cbentley.powerdata.spec.src4.power.trie.IPowerCharTrie;
import pasa.cbentley.powerdata.spec.src4.spec.CharTrieUtilz;

/**
 * Special Drawable that manages visual Edit Artifacts on the Screen.
 * <li>Virtual Keyboard
 * <li>Editable {@link StringDrawable}
 * <li>Edit Commands
 * <li>Prediction Tables
 * <br>
 * <br>
 *  
 * Controls the character input in {@link StringDrawable}. Singleton instance is fetched using {@link GuiContext#getStrEditCtrl()}.
 * <br>
 * <br>
 * A {@link StringDrawable} can customize the look and parameters of the {@link StringEditControl}
 * with the {@link StyleClass} at {@link IBOTypesGui#LINK_55_STYLE_STRING_EDIT_CONTROL}.
 * <br>
 * <br>
 * When the key focus is given to another
 * {@link StringDrawable}, the {@link StringEditControl} control is moved to the new {@link StringDrawable}.
 * More generally, when a {@link StringDrawable} loses active focus, the {@link StringEditControl} disappears.
 * <br>
 * <br>
 * 
 * Uses {@link StringTech} tech parameters descriptors. 
 * <li> {@link IBOTypesGui#TYPE_124_STRING_TECH} as data descriptor 
 * <li> {@link IBOTypesGui#TYPE_125_STRING_EDIT_TECH} as descriptor of data
 * <br>
 * <br>
 * <b>Location and Position of the {@link StringEditControl}</b>: 
 * <br>
 * <li>Relative to {@link StringDrawable} with {@link TopologyDLayer}.
 * <li>Absolute controlled by {@link MasterCanvas}. A Cmd switch key/nav focus between {@link StringEditControl} and {@link StringDrawable}
 * <br>
 * <br>
 * In all cases, it adds a Edit sub menu in the {@link MenuBar}.
 * <br>
 * <br>
 * When going in Edit Mode {@link StringDrawable} fires {@link StringEditControl} with {@link Controller#giveControlEdit} at a given positon on a DLayer + 1
 * <br>
 * <br>
 * When losing focus, it must hide some Drawables like the CharSet popup window if it is active.
 * <br>
 * The {@link MasterCanvas} / {@link Controller} keeps a reference to the unique {@link StringEditControl} instance shared between several {@link StringDrawable}.
 * <br>
 * <br>
 * Depending on {@link FrameworkCtx}, Input is either KEYBOARD / NUMPAD / VIRTUAL_KB (only touch screen) mode . Some devices may switch between one mode or the other
 * <br>
 * <br>
 * <b>Components inside the Control:</b> <br>
 * <li>T9 sign Green/Red
 * <li>MAJ sign Green/Red
 * <li>Language/Charset {@link StringListInput}.
 * <li>Speed of Char Acceptance
 * <li>Current key char set.
 * <li>Table structure that display result of Trie dictionnary searches.
 * <br>
 * <br>
 * Each {@link StringDrawable} keeps a view state for the {@link StringEditControl}, and  sets it when getting focus to switch  to edit mode.
 * <br>
 * <br>
 * <b>Navigation to Commands</b> : <br>
 * Devices with pointer support have an easy access to the control. Other must use key shortcuts or navigate to it.
 * <br>
 * They also have a contextual menu appearing.
 * <br>
 * When a key is pressed in the {@link StringDrawable} and then {@link EditModule}, it first forwards it to the {@link StringEditControl}. 
 * If the Control match a command to the {@link InputConfig}, {@link EditModule} drops control flow.
 * <br>
 * <br>
 * Control updates the state of the key.
 * <br>
 * <br>
 * The current key step is managed here.
 * <br>
 * CharSet ID may be changed. It modifies InputState {@link IBOStringDrawable#INPUT_OFFSET_03_CHARSET_ID1} of the root {@link ByteObject}.
 * <br>
 * <br>
 * <b>Structure</b> :
 * <br>
 * Uses a Single Line Table Flow.
 * <br>
 * <br>
 * <b>Prediction with Trie Dictionnary Search</b>
 * <br>
 * When 
 * The {@link T9SearchThread} is used
 * <br>
 * @author Charles-Philip Bentley
 *
 */
public class StringEditControl extends TableView implements IDrawListener, IBOStringDrawable, IEventConsumer {

   public static final char   etalonChar             = 'z';

   public static final int    LINK_911_STYLE_T9      = 911;

   public static final int    LINK_912_STYLE_NORMAL  = 912;

   public static final int    LINK_913_LETTER_CHOICE = 913;

   public static final int    LINK_914_SYMBOL_TABLE  = 914;

   public static final int    maxNumChars            = 5;

   public static final int    TECH_FLAG1_VERTICAL    = 1;

   /**
    * 
    */
   private IPowerCharTrie     activeTrie;

   /**
    * Can be null
    */
   private ICharProducer      charProducer;

   /**
    * Set of chars associated with Number Keys
    */
   private char[][]           charSets               = Symbs.setEnglish;

   /**
    * {@link TableView} showing character set choices
    */
   private TableView          charSetsTableView;

   private StringDrawable     controlledSD;

   private int                currentKey             = ITechCodes.KEY_NUM1;

   private int                currentLanguageID;

   /**
    * Loaded from the charset language type given by {@link IBOTypesGui#TYPE_124_STRING_TECH}.
    */
   private IPowerCharTrie     dictionnaryTrie;

   /**
    * When explicitely activated, a Foreground Table with CharSet choice is shown.
    */
   private StringList         drawableCharSet;

   /**
    * Display the current key letters in a 0-9 keyboard context. Depends on the device.
    * <br>
    * <br>
    * The active letter is shown with a colored background.
    * This Drawable is controlled by this class and maybe positionned in the foreground just a line
    * above the current edited line.
    * <br>
    * This drawable is not selectable in the {@link TableView}.
    */
   private Drawable           drawableLetterChoices;

   /**
    * Style depends if MAJ lock is activated or not.
    * <br>
    * In lowercase only {@link IBOStringDrawable#INPUT_FLAG_3_MAJ} , this item is not shown.
    * <br>
    * Not shown on platform/devices that have a built-in MAJ (J2SE)
    * <br.
    */
   private StringDrawable     drawableMAJ;

   /**
    * Displays the number of characters in the controlled {@link StringDrawable}
    * <br>
    */
   private StringDrawable     drawableNumChars;

   private StringDrawable     drawableOptions;

   private StringDrawable     drawablePhoneMode;

   /**
    * Button for selecting a special character
    */
   private StringDrawable     drawableSpecial;

   private StringDrawable     drawableSpeed;

   /**
    * Style depends if T9 is activated or not
    */
   private StringDrawable     drawableT9;

   private EditModule         editModule             = null;

   private int                indexCharSet           = 4;

   private int                indexLetterChoices     = 5;

   private int                indexMaj               = 2;

   private int                indexOptions           = 6;

   private int                indexSpecial           = 0;

   private int                indexSpeed             = 3;

   private int                indexT9                = 1;

   private boolean            isPredictionJustAdded;

   private IPowerCharTrie[][] languageTries;

   private int                lastPredictionSpace;

   /**
    * 
    */
   PredictionRunner           pr;

   /**
    * {@link TableView} to display predictions from Trie dictionnary.
    * <br>
    * May be drawn below or on top depending on space available below/above caret.
    * <br>
    * Trie thread modifies the model of the table.
    * <br>
    * <br>
    * The {@link IDrawable#init(int, int)} is called whenever the table is positioned.
    * This is done to prevent the prediciton table to be drawn outside the visible canvas.
    * <br>
    * <br>
    * 
    */
   private TableView          predictionTable;

   private PulseThread        pulseThread            = null;

   private ObjectTableModel   puncts                 = null;

   String[]                   punctuations           = new String[] { ",", ".", "?", "!", ":", ";", "(", ")" };

   private TableView          punctuationTable;

   private int                size                   = 6;

   private SymbolTable        symbolTable;

   private T9SearchThread     t9ds;

   /**
    * Used when T9 is enabled and trie dic is available.
    */
   private T9PrefixTrieSearch t9search;

   private ByteObject         techStr;

   private Timer              timer;

   private IPowerCharTrie     userTrie;

   private int                pulseEventPID;

   /**
    * Controlled by TECH Param
    * @param styleKey
    */
   public StringEditControl(GuiCtx gc, StyleClass sc) {
      super(gc, sc, gc.getTablePolicyC().getButtonLine(false, 0, 0));

      pr = new PredictionRunner(gc, this);
      pulseEventPID = gc.getEventsBusGui().createNewProducerID(1);
      pulseThread = new PulseThread(gc.getEventsBusGui(), PulseThread.STATE_3_PAUSED, pulseEventPID);

      languageTries = new IPowerCharTrie[5][];

      //create a style Red/Green. may be null
      StyleClass t9SK = styleClass.getStyleClass(LINK_911_STYLE_T9);
      StyleClass normalSK = styleClass.getStyleClass(LINK_912_STYLE_NORMAL);
      StyleClass letterSK = styleClass.getStyleClass(LINK_913_LETTER_CHOICE);

      //we want to disable global cell style
      setHelperFlag(HELPER_FLAG_21_MODEL_STYLE, true);

      //link selected style
      ByteObject selectedStyle = styleClass.getStateStyle(ITechDrawable.STYLE_05_SELECTED);

      if (t9SK != null) {
         t9SK.linkStateStyle(selectedStyle, ITechDrawable.STYLE_05_SELECTED);
      }
      if (normalSK != null) {
         normalSK.linkStateStyle(selectedStyle, ITechDrawable.STYLE_05_SELECTED);
      }
      if (letterSK != null) {
         letterSK.linkStateStyle(selectedStyle, ITechDrawable.STYLE_05_SELECTED);

         //#debug
         toDLog().pInit("Letter Style Class", letterSK, StringEditControl.class, "StringEditControl", LVL_05_FINE, false);
      }

      editModule = new EditModule(gc);
      //check about the tech strings.. we want total control over those here.

      techStr = gc.getDrawableStringFactory().getStringTech(ITechStringDrawable.TYPE_1_TITLE);

      drawableSpecial = new StringDrawable(gc, normalSK, "#", ITechStringDrawable.TYPE_1_TITLE);

      drawablePhoneMode = new StringDrawable(gc, normalSK, "@", ITechStringDrawable.TYPE_1_TITLE);

      drawableT9 = new StringDrawable(gc, t9SK, "T9", ITechStringDrawable.TYPE_1_TITLE);

      //create a style Red/Green
      drawableMAJ = new StringDrawable(gc, t9SK, "MAJ", ITechStringDrawable.TYPE_1_TITLE);

      drawableSpeed = new StringDrawable(gc, normalSK, "S", ITechStringDrawable.TYPE_1_TITLE);

      //list root of data
      drawableCharSet = new StringList(gc, normalSK, "En", ITechStringDrawable.TYPE_1_TITLE);

      //uses draw injection
      drawableLetterChoices = new DrawableInjected(gc, letterSK, this, this);
      //EventChannel.addConsumerProducer(this, ITableView.EVENT_ID_0SELECT, tv, tv.getProducerID());

      //modify the tech so that no cell is selected 
      ObjectTableModel predictionModel = pr.getModel();
      predictionTable = new TableView(gc, sc, gc.getTablePolicyC().getButtonLine(true, 0, 0), predictionModel);
      predictionTable.init(0, 0);

      predictionTable.addEventListener(this, TableView.EVENT_ID_00_SELECT);

      puncts = new ObjectTableModel(gc.getDMC(), punctuations);
      punctuationTable = new TableView(gc, sc, gc.getTablePolicyC().getButtonLine(true, 0, 0), puncts);
      punctuationTable.init(0, 0);

      punctuationTable.addEventListener(this, TableView.EVENT_ID_00_SELECT);
      //parenting
      setParentLink(drawableSpecial);
      setParentLink(drawableT9);
      setParentLink(drawableMAJ);
      setParentLink(drawableSpeed);
      setParentLink(drawableCharSet);
      setParentLink(drawableLetterChoices);

      //setParentLink(predictionTable);
      //setParentLink(punctuationTable);

      boolean isFullKB = getKeyboardType() == IBOHost.KB_TYPE_1_FULL;

      if (isFullKB) {
         //sort out what is shown
      } else {

      }
      //System.out.println("#StringEditControl isOpaque? "+ isOpaque());
   }

   public void addTrie(IPowerCharTrie c, String langID) {
      
   }
   
   public void addTrie(IPowerCharTrie c, int langID) {
      IPowerCharTrie[] ar = languageTries[langID];
      if (ar == null) {
         ar = new IPowerCharTrie[] { c };
      } else {
         ar = CharTrieUtilz.increaseCapacity(ar, 1);
         ar[ar.length - 1] = c;
      }
      languageTries[langID] = ar;
   }

   /**
    * Call back when CharSet ID is changed
    * <br>
    * <br>
    * 
    */
   public void consumeEvent(BusEvent e) {
      if (e instanceof MEventDrawable) {
         consumeEventDrawable((MEventDrawable) e);
      }
   }

   public void consumeEventDrawable(MEventDrawable e) {
      //#debug
      toDLog().pEvent("msg", e, StringEditControl.class, "consumeEventDrawable", LVL_05_FINE, true);

      if (e.getProducer() == charSetsTableView) {
         if (e.getEventID() == TableView.EVENT_ID_00_SELECT) {
            eventCharSetSelected(e);
         }
      } else if (e.getProducer() == symbolTable) {
         if (e.getEventID() == TableView.EVENT_ID_00_SELECT) {
            eventSymbolSelected(e);
         }
      } else if (e.getProducer() == predictionTable) {
         if (e.getEventID() == TableView.EVENT_ID_00_SELECT) {
            eventPredictionSelected(e);
         }
      } else if (e.getProducer() == punctuationTable) {
         if (e.getEventID() == TableView.EVENT_ID_00_SELECT) {
            eventPunctuationSelected(e);
         }
      } else if (e.getProducer() == pulseThread) {
         editModule.forceCaretRepaint(e.getParam1() == PulseThread.STATE_0_ON);
         e.setFlag(BusEvent.FLAG_1_ACTED, true);
      }
   }

   /**
    * Setup the active trie
    */
   private void doPredictions() {
      if (drawableT9.hasStateStyle(ITechDrawable.STYLE_03_MARKED)) {
         int lid = getLanguageID();
         if (languageTries[lid] != null && languageTries[lid].length != 0) {
            activeTrie = languageTries[lid][0];
         }
      } else {
         activeTrie = null;
      }
   }

   public void doTakeControl(StringDrawable sd) {
      takeControl(sd, null);
   }

   /**
    * We only draw letter choice in listener mode.
    * <br>
    * <br>
    * 
    */
   public void drawContentListen(GraphicsX g, int x, int y, int w, int h, Drawable d) {
      if (d == drawableLetterChoices) {
         //draw content with selected letter in colored background
         ByteObject style = d.getStyle();
         int key = editModule.currentKey;
         char[] chars = new char[0];
         if (key > ITechCodes.KEY_NUM0 && key < ITechCodes.KEY_NUM9) {
            chars = Symbs.getKeyCharSet(charSets, editModule.currentKey);

         } else {
            if (key == ITechCodes.KB_069_E) {
               chars = new char[] { 'E', 'É', 'È' };
            }
         }
         ByteObject txt = getStyleOp().getContentStyle(style);
         //the background figure to draw below the selected
         ByteObject bgFigure = getStyleOp().getStyleDrw(style, IBOStyle.STYLE_OFFSET_2_FLAGB, IBOStyle.STYLE_FLAGB_1_BG);
         int color = getOpStringFx().getStringColor(txt);
         IMFont f = getOpStringFx().getStringFont(txt);
         g.setFont(f);
         g.setColor(color);
         int cw = f.charWidth(etalonChar);
         int dx = x;
         for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            int ow = f.charWidth(c);
            if (editModule.currentKeyStep == i) {
               gc.getDC().getFigureOperator().paintFigure(g, dx, y, ow, f.getHeight(), bgFigure);
               g.setColor(color);
            }
            //int dx = Anchor.getXAlign(IDrw.ALIGN_CENTER, x, cw, ow);
            g.drawChar(c, dx, y, IBOBox.ANCHOR);
            dx += ow;
         }
      }
   }

   public void eventCharSetSelected(MEventDrawable e) {
      //no parameter. just read the selected index from the TableView
      int selectedIndex = charSetsTableView.getSelectedIndex();
      String str = Symbs.charsets[selectedIndex];

      //#debug
      toDLog().pEvent("Selected" + selectedIndex + " - " + str, this, StringEditControl.class, "eventCharSetSelected", LVL_05_FINE, true);

      drawableCharSet.setStringNoUpdate(str);
      //remove TV from view and send a full repaint
      //call this when inside User Event Thread. otherwise no repaint will be made.
      hidePopup(e.getIC());
      e.setFlag(BusEvent.FLAG_1_ACTED, true);
   }

   /**
    * Called in the GUI update thread.
    * <br>
    * @param e
    */
   private void eventPredictionSelected(MEventDrawable e) {
      int selectedIndex = predictionTable.getSelectedIndex();
      String str = pr.getPred(selectedIndex);
      //overwrite or insert word on prefix 
      int delStart = pr.caretWordStart;
      int delLen = pr.prefix.length();
      controlledSD.deleteCharsAt(delStart, delLen);
      controlledSD.addChars(delStart, str.toCharArray());

      lastPredictionSpace = delStart + str.length();
      controlledSD.addChar(lastPredictionSpace, ' ');
      int px = controlledSD.getStringer().getMetrics().getCharX(lastPredictionSpace);
      int py = controlledSD.getStringer().getMetrics().getCharY(lastPredictionSpace);
      punctuationTable.setXY(px, py);

      isPredictionJustAdded = true;

      predictionTable.removeDrawable(e.getIC(), null);
      punctuationTable.shShowDrawable(e.getIC(), ITechCanvasDrawable.SHOW_TYPE_1_OVER);

      e.setFlag(BusEvent.FLAG_1_ACTED, true);
   }

   private void eventPunctuationSelected(BusEvent e) {
      int selectedIndex = punctuationTable.getSelectedIndex();
      String str = (String) puncts.getObject(selectedIndex);

      str += " ";

      int delStart = editModule.caretIndex - 1;
      int delLen = str.length();

      controlledSD.deleteCharsAt(delStart, delLen);
      controlledSD.addChars(delStart, str.toCharArray());

      e.setFlag(BusEvent.FLAG_1_ACTED, true);
   }

   /**
    * 
    * @param e
    */
   public void eventSymbolSelected(BusEvent e) {
      char selectedChar = symbolTable.getSelectedChar();
      editModule.caretAtInsertChar(selectedChar);
      symbolTable.removeDrawable(null, controlledSD);
      e.setFlag(BusEvent.FLAG_1_ACTED, true);
   }

   public EditModule getEditModule() {
      return editModule;
   }

   public int getKeyboardType() {
      return gc.getCFC().getHostCore().getHostInt(ITechHostUI.DATA_ID_23_KEYBOARD_TYPE);
   }

   /**
    * Should be read only
    * @return
    */
   public char[] getKeyChars() {
      return Symbs.getKeyCharSet(charSets, currentKey);
   }

   public IPowerCharTrie[] getLangTries(int id) {
      return languageTries[id];
   }

   public IPowerCharTrie[] getLangTries(String id) {
      return languageTries[0];
   }

   /**
    * Selected language str id
    * @return
    */
   public String getLanguageStr() {
      String str = drawableCharSet.getString();
      return str;
   }

   /**
    * 
    * @return
    */
   public int getLanguageID() {
      String str = drawableCharSet.getString();
      //TODO
      return 0;
   }

   public IDrawable getLetterChoices() {
      return drawableLetterChoices;
   }

   public IDrawable getModelDrawable(int index) {
      if (index == indexSpecial) {
         return drawableSpecial;
      } else if (index == indexT9) {
         return drawableT9;
      } else if (index == indexMaj) {
         return drawableMAJ;
      } else if (index == indexOptions) {
         return drawableOptions;
      } else if (index == indexSpeed) {
         return drawableSpeed;
      } else if (index == indexCharSet) {
         return drawableCharSet;
      } else if (index == indexLetterChoices) {
         return drawableLetterChoices;
      }
      return null;
   }

   public PulseThread getPulseThread() {
      return pulseThread;
   }

   public int getSize() {
      return size;
   }

   /**
    * Makes sure the popups controlled by {@link StringEditControl} are hidden.
    * <br>
    * <br>
    * @param ic
    */
   public void hidePopup(InputConfig ic) {
      if (charSetsTableView != null && !charSetsTableView.hasState(ITechDrawable.STATE_03_HIDDEN)) {
         charSetsTableView.removeDrawable(ic, this);
         ic.srActionDoneRepaint();
      }
   }

   /**
    * 
    */
   public boolean initInject(int w, int h, Drawable d) {
      if (d == drawableLetterChoices) {
         //set the 
         ByteObject style = d.getStyle();
         ByteObject txt = getStyleOp().getContentStyle(style);
         IMFont f = gc.getDC().getFxStringOperator().getStringFont(txt);
         int height = getContentH(); //height will be content height
         //height +=  + MStyle.getPadH(getStyle());
         boolean isStyleIncluded = true;
         d.setDrawableSize(f.charWidth(etalonChar) * maxNumChars, height, isStyleIncluded);
         return true;
      }
      return false;
   }

   public boolean isControlling(StringDrawable sd) {
      return sd == controlledSD;
   }

   public boolean isMajuscule() {
      return drawableMAJ.hasStateStyle(ITechDrawable.STYLE_03_MARKED);
   }

   /**
    * Deal with commands.
    * InputThread
    */
   public void manageKeyInput(InputConfig ic) {
      super.manageKeyInput(ic);
      if (!ic.isActionDone()) {
         //edit control did not action a command
         if (ic.isPressed()) {

            //is the table currently being shown
            //we need to check volatile state
            if (!predictionTable.hasStateVolatile(ITechDrawable.STATE_03_HIDDEN)) {
               //commands for quick selection of
               int num = -1;
               if (ic.isPressedKeyboard0(ITechCodes.KEY_FIRE)) {
                  if (ic.is1()) {
                     num = 0;
                  } else if (ic.is2()) {
                     num = 1;
                  }
               }
               if (num != -1) {
                  //upgrade lock to writeLock
                  predictionTable.setSelectedIndex(num, ic, true);
                  ic.srActionDoneRepaint(predictionTable);
               }
            }
            if (ic.isCancel()) {

               editModule.caretAtDeleteChar();
               ic.srActionDoneRepaint();
            } else if (ic.isFireP()) {
               //accept?
            } else if (charProducer != null) {
               charProducer.produce(ic, editModule);
            }
         }
      }
      if (isPredictionJustAdded) {
         //hide
         punctuationTable.removeDrawable(ic, controlledSD);

      }
      //when prediction table is active, selection can occurs with keypad numbers. this only enabled when
      //there are enough predictions in the table.
   }

   /**
    * TODO Selection.
    * 
    * <br>
    * Called by {@link EditModule#manageKeyInput(InputConfig)} before anything else is done.
    * <br>
    * <br>
    * Must be noticed when it moves horizontally to make sure the popup is hidden.
    * When hiding popup, popup must clear its area with Canvas bg color and
    *  a repaint of {@link Drawable} below the popup area must be made.
    * <br>
    * <br>
    * <li> manage edition commands
    * Sends event to {@link ICharProducer}.
    * <br>
    * <br>
    * When a command hooks on a prediction,  prediction tale is selected and event goes to {@link StringEditControl#eventPredictionSelected(BusEvent)}
    * <br>
    * 
    */
   public void manageKeyInput(InputStateDrawable ic, CanvasResultDrawable srd) {
      //when an action has already been registered
      if (srd.isActionDone()) {
         return;
      }
      //input processing thread what happens if several events. Input State are
      if (ic.isModPressed()) {

      }

   }

   /**
    * MenuBar .
    * <br>
    * Menu selection works like this:
    * 1: pointer event in a cell of table -> cell goes selected style
    * 2: for the menu action to be fired, the
    * <br>
    * When popup menu is not modal, pointer still works.
    * <br>
    * <br>
    * 
    */
   public void managePointerInput(InputConfig ic) {
      super.managePointerInput(ic);
   }

   /**
    * Delegate to the {@link Controller} who manages the Navigational topolgy. 
    * <br>
    * <br>
    * {@link StringEditControl} registers its topology.
    * <br>
    * {@link StringEditControl} is not aware of the topoly that going down brings back {@link StringDrawable} into focus. 
    * <br>
    * <br>
    * It is never going since no NavFlag
    * <br>
    * <br>
    * When a prediction table is visible, depending on its position, a navigate down or up.
    * 
    */
   public void navigateDown(InputConfig ic) {
      gc.getFocusCtrl().navigateOut(ic, this, C.POS_1_BOT);
   }

   /**
    * 
    */
   public void navigateLeft(InputConfig ic) {
      hidePopup(ic);
      super.navigateLeft(ic);
   }

   public void navigateRight(InputConfig ic) {
      hidePopup(ic);
      super.navigateRight(ic);
   }

   /**
    * When going up, bring back focus to the {@link TableView} holding the {@link StringDrawable} .
    * <br>
    * <br>
    * 
    */
   public void navigateUp(InputConfig ic) {
      gc.getFocusCtrl().navigateOut(ic, this, C.POS_0_TOP);
   }

   /**
    * Position {@link StringEditControl} depending on {@link IBOStringEdit#SEDIT_OFFSET_05_CONTROL_POSITION1} option
    * <br>
    * Loads it in the painting chain
    */
   public void positionControl() {
      int position = editModule.getEditTech().get1(IBOStringEdit.SEDIT_OFFSET_05_CONTROL_POSITION1);
      if (position == ITechStringDrawable.SEDIT_CONTROL_0_CANVAS) {
      } else if (position == ITechStringDrawable.SEDIT_CONTROL_1_TOP && controlledSD != null) {
         //create a Top Center Pozer
         gc.getNavigator().navInsert(null, controlledSD, INavTech.NAV_1_UP, this);

      } else {
         //position it top of canvas as overlay??? depends on settings
      }
   }

   /**
    * 
    */
   public void predictionStart() {
      if (pr != null && controlledSD != null) {
         String prefix = "";
         Stringer st = controlledSD.getStringer();
         pr.caretInit = editModule.caretIndex - 1;
         for (int i = pr.caretInit; i >= 0; i--) {
            char ca = st.getCharAtRelative(i);
            if (ca != ' ' || ca != '.') {
               prefix = ca + prefix;
            } else {
               //record position of character
               pr.px = st.getMetrics().getCharX(i);
               pr.py = st.getMetrics().getCharY(i);
               pr.caretWordStart = i;
               break;
            }
         }
         pr.prefix = prefix;
         if (prefix != null && activeTrie != null) {
            if (editModule.getEditTech().hasFlag(IBOStringEdit.SEDIT_OFFSET_01_FLAG, IBOStringEdit.SEDIT_FLAG_6_SAME_THREAD)) {
               pr.setRunFlag(IBRunnable.FLAG_01_BLOCKING, true);
            } else {
               pr.setRunFlag(IBRunnable.FLAG_01_BLOCKING, false);
            }
            gc.runAsWorker(pr);
         }
      }
   }

   /**
    * called by {@link Controller#removeControlEdit(StringDrawable)}
    */
   public void removeControl() {
      if (getKeyboardType() == IBOHost.KB_TYPE_2_FULL_VIRTUAL) {
         produceCoreUIEvent(IEventsCoreUI.DEVICE_VIRT_KEYB_OFF);
      }
      if (pulseThread.isPulseRunning()) {
         pulseThread.setPulseState(PulseThread.STATE_3_PAUSED);
      }
      if (controlledSD != null) {
         controlledSD.setEditModule(null);
      }
      controlledSD = null;
      this.removeDrawable(this);
   }

   private void produceCoreUIEvent(int eid) {
      gc.getCUC().getEventBus().sendNewEvent(IEventsCoreUI.PID_1_DEVICE, eid, this);

   }

   /**
    * This method is called either in worker thread or gui thread.
    * <br>
    * If called in the {@link ITechPaintThread#THREAD_0_HOST_HUI}, the Runnable is called.
    * Otherwise, the code is queued
    * <br>
    * @param prefix
    */
   public void searchPrefix(final InputConfig ic, String prefix) {

      //we have a special command

      //this is run in worker thread
      final IntToStrings its = activeTrie.searchPrefix(5).searchWait(prefix);

      //TODO create a new Table so we don't run into sync issues
      Runnable uiUpdate = new SearchPrefixRunnable(gc, ic, its);
      //syncrhonize once on the ScreenResult
      RepaintCtrlDrawable repaintCtrlDraw = gc.getCanvasCtxRoot().getCanvas().getRepaintCtrlDraw();
      repaintCtrlDraw.repaintDrawableCycleBusiness(predictionTable, uiUpdate);

   }

   /**
    * Override {@link TableView#selectionSelectEvent(InputConfig)} method.
    * <br>
    * <br>
    * 
    */
   protected void selectionSelectEvent(InputConfig ic) {
      int selIndex = getSelectedIndex();
      IDrawable d = getModelDrawable(selIndex);
      if (d == drawableT9) {
         d.setStateStyle(ITechDrawable.STYLE_03_MARKED, !d.hasStateStyle(ITechDrawable.STYLE_03_MARKED));
         doPredictions();
         ic.srActionDoneRepaint(d);
      } else if (d == drawableMAJ) {
         CoreUiCtx cuc = gc.getCFC().getCUC();
         if (d.hasStateStyle(ITechDrawable.STYLE_03_MARKED)) {
            cuc.setMajOn(false);
            d.setStateStyle(ITechDrawable.STYLE_03_MARKED, false);
         } else {
            cuc.setMajOn(false);
            d.setStateStyle(ITechDrawable.STYLE_03_MARKED, true);
         }
         ic.srActionDoneRepaint(d);
      } else if (d == drawableCharSet) {
         //show menu of available charsets
         if (charSetsTableView == null) {
            //TODO char sets may be record set
            String[] dataModel = Symbs.charsets;
            ObjectTableModel otm = new ObjectTableModel(gc.getDMC(), dataModel);
            //ByteObject tablePol = TablePolicyC.getButtonLine(true, 40, 20);
            ByteObject tablePol = gc.getTablePolicyC().getSimple1ColPolicy();
            charSetsTableView = new TableView(gc, this.styleClass, tablePol);
            charSetsTableView.setDataModel(otm);
            //register 
            charSetsTableView.setXY(drawableCharSet.getX() + drawableCharSet.getDrawnWidth(), drawableCharSet.getY());
            charSetsTableView.init(0, 0);
            //register select event
            charSetsTableView.addEventListener(this, TableView.EVENT_ID_00_SELECT);
         }
         //transfer key focus to the newly visible Drawable.
         //this drawable gets the vertical navigation
         drawableCharSet.setStateStyle(ITechDrawable.STYLE_05_SELECTED, false);
         ic.srActionDoneRepaint(drawableCharSet);
         charSetsTableView.shShowDrawable(ic, ITechCanvasDrawable.SHOW_TYPE_1_OVER);
      } else if (d == drawableSpeed) {
         //show general options pane to modifies speed and stuff
      } else if (d == drawableOptions) {
         //show option menu
      } else if (d == drawableSpecial) {
         //show symbol table
         if (symbolTable == null) {
            StyleClass symbolStyleClass = this.styleClass.getStyleClass(LINK_914_SYMBOL_TABLE);
            if (symbolStyleClass == null) {
               symbolStyleClass = this.styleClass;
            }
            symbolTable = new SymbolTable(gc, symbolStyleClass);
            symbolTable.getLay().layFullViewContext();
            //tries if space allows it to use it like a virtual keyboard
            symbolTable.addEventListener(this, TableView.EVENT_ID_00_SELECT);
         }
         //show it over
         symbolTable.shShowDrawable(ic, ITechCanvasDrawable.SHOW_TYPE_1_OVER);
         ic.srActionDoneRepaint();
      }
   }

   /**
    * @param currentKey
    */
   public void setCurrentKey(int currentKey) {
      this.currentKey = currentKey;
   }

   /**
    * Sets the {@link StringEditControl} state:
    * <br>
    * <li>T9 use
    * <li>Dictionary ID
    * <li>MAJ Set
    * <li>Input Mode Uppercase/Lower/Mixed
    * <li>Input Constraint
    * <br>
    * <br>
    * @param sid
    */
   private void setInputState(ByteObject inputState) {
      int charSetID = inputState.get1(INPUT_OFFSET_03_CHARSET_ID1);

      drawableCharSet.setStringNoUpdate(Symbs.getCharSetName(charSetID));
      charSets = Symbs.getCharSet(charSetID);
      boolean isMaj = inputState.hasFlag(IBOStringDrawable.INPUT_OFFSET_01_FLAG, IBOStringDrawable.INPUT_FLAG_3_MAJ);
      drawableMAJ.setStateStyle(ITechDrawable.STYLE_03_MARKED, isMaj);

      ByteObject editTech = editModule.getEditTech();
      boolean isPred = editTech.hasFlag(IBOStringEdit.SEDIT_OFFSET_01_FLAG, IBOStringEdit.SEDIT_FLAG_4_KB_PREDICTIVE);
      drawableT9.setStateStyle(ITechDrawable.STYLE_03_MARKED, isPred);
      doPredictions();
   }

   private void showControl(InputConfig ic) {

   }

   /**
    * Make it shown on a DLayer above Content X.
    * <br>
    * Insert itself in the {@link IDrawable} navigational topoly, just above the {@link StringDrawable}.
    * <br>
    * <br>
    * Controls the {@link EditModule} and {@link PulseThread} for the caret repaints.
    * <br>
    * <br>
    * Checks if {@link StringDrawable} is editable.
    * <br>
    * Depending on Focus change event,
    * <br>
    * <br>
    * @param sd {@link StringDrawable}
    * @param e {@link BusEvent} specified by {@link IFocusEvent}.
    */
   public void takeControl(StringDrawable sd, MEventDrawable e) {
      if (controlledSD == sd) {
         return;
      }
      ByteObject tech = sd.getTechDrawable();
      if (tech.get1(INPUT_OFFSET_06_MODE1) != ITechStringDrawable.MODE_2_EDIT) {

         //#debug
         toDLog().pFlow("InputMode is not EDIT. Not taking control of", sd, StringEditControl.class, "takeControl", LVL_05_FINE, true);
         return;
      }
      if (e != null) {
         //is the previous key focus drawable a children of StringDrawable
         boolean isOldChildren = DrawableUtilz.isUpInFamily((IDrawable) e.getParamO2(), sd);

         //#debug
         toDLog().pEvent("StringDrawable FocusGain from Children=" + isOldChildren, sd, StringEditControl.class, "takeControl", LVL_05_FINE, true);

         //check old children but also check if StringEdit control has changed its controlled.
         if (isOldChildren) {
            return;
         }
      }
      controlledSD = sd;

      styleClass = controlledSD.getStyleClass().getStyleClass(IBOTypesGui.LINK_55_STYLE_STRING_EDIT_CONTROL);

      controlledSD.setEditModule(editModule);
      editModule.setStringEditController(this);
      editModule.setStringDrawable(sd);//this method may use sec

      setInputState(tech);

      //set it visible only on demand
      drawableLetterChoices.setStateFlag(ITechDrawable.STATE_03_HIDDEN, true);

      positionControl();

      showControl(e.getIC());
      //Controller.getMe().giveLayerFocus(this);

      ByteObject editTech = editModule.getEditTech();

      //CARET Management
      boolean isBlinkCaret = editTech.hasFlag(IBOStringEdit.SEDIT_OFFSET_01_FLAG, IBOStringEdit.SEDIT_FLAG_2_CARET_BLINK);
      if (isBlinkCaret) {
         pulseThread.setOnOffWaitTimes(editTech.get1(IBOStringEdit.SEDIT_OFFSET_10_CARET_BLINK_SPEED_ON1) * 10, editTech.get1(IBOStringEdit.SEDIT_OFFSET_11_CARET_BLINK_SPEED_OFF1) * 10);
         if (pulseThread.isPulseRunning()) {
            pulseThread.resetToOn();
         } else {
            pulseThread.addEventConsumer(this, IEventsCore.PID_5_THREAD_0_ANY);
            pulseThread.setPulseState(PulseThread.STATE_0_ON);
            pulseThread.start();
         }
      } else {
         if (pulseThread.isPulseRunning()) {
            pulseThread.setPulseState(PulseThread.STATE_3_PAUSED);
         }
      }

      this.setParent(controlledSD);

      //asks the device virtual keyboard to appear
      if (getKeyboardType() == IBOHost.KB_TYPE_2_FULL_VIRTUAL) {
         produceCoreUIEvent(IEventsCoreUI.DEVICE_VIRT_KEYB_REQUEST);
      }
      //Controller.getMe().getTopologyNav().positionToplogy(this, C.POS_0_TOP, sd);
      //request a repaint
      CanvasAppliDrawable cd = gc.getCanvasCtxRoot().getCanvas();
      //we don't know in which thread we are running. let the ctrl figure it
      cd.getRepaintCtrlDraw().repaintDrawableCycleBusiness(sd);
      cd.getRepaintCtrlDraw().repaintDrawableCycleBusiness(this);

   }

   //#mdebug
   public void toString(Dctx sb) {
      sb.root(this, "StringEditControl");
      sb.nl();
      sb.append("#SpecialCharacter");
      sb.nl();
      sb.append(drawableSpecial.toString());
      sb.nl();
      sb.append("#T9");
      sb.nl();
      sb.append(drawableT9.toString());
      sb.nl();
      sb.append("#MAJ");
      sb.nl();
      sb.append(drawableMAJ.toString());
      sb.nl();
      sb.append("#CharSet");
      sb.nl();
      sb.append(drawableCharSet.toString());
      sb.nl();
      sb.append("#Delay");
      sb.nl();
      sb.append(drawableSpeed.toString());
      sb.nl();
      sb.append("#LetterChoices");
      sb.nl();
      sb.append(drawableLetterChoices.toString());
      super.toString(sb.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "StringEditControl");
   }
   //#enddebug
}
