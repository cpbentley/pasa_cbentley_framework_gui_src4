package pasa.cbentley.framework.gui.src4.string;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.interfaces.ITechNav;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.utils.StringUtils;
import pasa.cbentley.framework.cmd.src4.engine.CmdFactoryCore;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.engine.CmdNode;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.cmd.src4.nav.INavigational;
import pasa.cbentley.framework.core.ui.src4.tech.ITechCodes;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.string.StringMetrics;
import pasa.cbentley.framework.drawx.src4.string.Stringer;
import pasa.cbentley.framework.gui.src4.canvas.GraphicsXD;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceGui;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.DrawableInjected;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IEventsGui;
import pasa.cbentley.framework.gui.src4.ctx.ObjectGC;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStrAuxData;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStrAuxEdit;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsGui;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawListener;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.tech.ITechLinks;
import pasa.cbentley.framework.gui.src4.tech.ITechStringDrawable;
import pasa.cbentley.powerdata.spec.src4.guicontrols.T9PrefixTrieSearch;
import pasa.cbentley.powerdata.spec.src4.guicontrols.TrieTranslationSearch;

/**
 * Manage edition and selection state and drawing in a {@link StringDrawable}. 
 * <br>
 * <br>
 * Initially Edition focus was a singleton. But connecting 2 or more keyboards
 * and having several edition focuses at the same time is supported in the design.
 * 
 * Since EditionFocus is never duplicated. The mapping of event code to characters is done by the {@link ICharProducer} of
 * the {@link StringEditControl}.
 * <br>
 * Caret may select more than 
 * <br>
 * <br>
 * The editing may be forced on one line. Usually this means a type {@link ITechStringDrawable#PRESET_CONFIG_2_SCROLL_H} with no scrollbars.
 * {@link ITechStringDrawable#PRESET_CONFIG_3_SCROLL_V} means editing on several lines, class uses {@link StringDrawable#breakText}
 * But if only one line is needed, shrink flag show only one line.
 * <br>
 * <br>
 * Fire may mean breakLine or validate: in those cases, the UI asks which choice the user wants .
 * <br>
 * Shows question 
 * <li>Nouvelle Ligne, if text supports multi lines.
 * <li>What do you want ?
 * <li>Valider
 * <li>Pressing Up and Down 
 * <br>
 * <br>
 * <b>Caret</b> <br>
 * The caret is a figure drawn at the start of a character. A colored small rectangle(1 or 2 pixels) at the start of eid.
 * It is drawn a {@link Drawable} injected by EditModule. Blinking is implemented by EditModule. When repaint is for caret only,
 * erase old char area. draw char and then draw caret.
 * <br>
 * Insert toggle4
 * the String is drawn normally using {@link StringDrawable} methods.
 * After content is drawn, the caret is drawn in overlay. The style of this sub element
 * <br>
 * <br>
 * <b>Input Assist </b><br>
 * Different schemes may be offered. <br>
 * <li>Light {@link T9} for small data sets (100 max)
 * <li>Drop Down Trie with user word trie. option like on the P990i to show the next word in full. 
 * <br>
 * <br>
 * <b>Edition Internal Update</b>:
 * <li>Every time a new character or word is added, 
 * <li>{@link Stringer} char array and len value is updated
 * <li>{@link StringMetrics} is asked if a new init call is needed. new char width is computed
 * <li>{@link StringDrawable} value is updated
 * <li>{@link StringDrawable#init(int, int)} with initial value is called if needed.
 * <li>Thus generating a full repaint, else only a content repaint
 * <br>
 * <br>
 * @author Charles-Philip Bentley
 *
 */
public class StringEditModule extends ObjectGC implements INavigational, IDrawListener, IBOStrAuxData, IStringable, IEventsGui {

   /**
    * Active caret.
    * <br>
    * Caret is moved each time navigation occurs or when a new character is added
    * <br>
    * At the end of a line, it is drawn as if there was a blank space.
    */
   private Drawable              caret;

   private long                  caretBlinkMillis = 1000;

   /**
    * 
    */
   private ByteObject            caretFigure;

   /**
    * Caret index is relative to offset. So its range is [0,len]
    * <br>
    * 
    */
   int                           caretIndex;

   /**
    * Previous location of the caret.
    * <br>
    * When caretIndex is last and a delete is made, last character must be erased.
    * <br>
    * Caret index is relative to offset.
    */
   private int                   caretIndexPrevious;

   /**
    * 
    */
   private int                   caretLineIndex   = 0;

   /**
    * String Edit Style taken from device
    */
   private int                   caretWidth       = 0;

   private Drawable              caretX;

   /**
    * Second caret. To be drawn when moving
    */
   private Drawable              caretY;

   /**
    * 
    */
   private char[][]              charset          = Symbs.setEnglish;

   public final MCmd             CMD_SET_EDIT;

   private CmdNode               cmdNode;

   int                           currentKey;

   int                           currentKeyStep;

   /**
    * Cannot be null.
    * <br>
    * Inited with Default.
    */
   private ByteObject            editTech;

   boolean                       isBlinking       = true;

   private boolean               isCaretOn        = true;

   /**
    * Control value set to true when key is repeateadly pressed for going over the characters associated with that key.
    */
   private boolean               isGoToNextChar   = true;

   private boolean               isInsertMode;

   private long                  lastKeyTimestamp;

   private int                   lastPressedKey;

   private int                   numEditLines     = 1;

   /**
    * {@link TableView} to display predictions from Trie dictionnary.
    * <br>
    * May be drawn below or on top depending on space available below/above caret.
    * <br>
    * Trie thread modifies the model of the table.
    */
   private TableView             predictionTable;

   /**
    * Provides the selected char set
    */
   StringEditControl             sec;

   private StringDrawable        stringDrawable;

   /**
    * Only accessible when T9 is active
    */
   private T9SearchThread        t9ds;

   /**
    * Used when T9 is enabled and trie dic is available.
    */
   private T9PrefixTrieSearch    t9search;

   /**
    * predictive search
    */
   private TrieTranslationSearch tds;

   /**
    * 
    */
   public StringEditModule(GuiCtx gc) {
      super(gc);
      editTech = gc.getDrawableStringFactory().getBOStringEditNormal();
      CmdFactoryCore cmdFac = cc.getCmdFactoryCore();
      CMD_SET_EDIT = cmdFac.createCmd(ICmdsGui.CMD_GUI_20_EDIT_MODE, "Edit Mode True");
      cmdNode = cc.createCmdNode("StringEdit");
   }

   /**
    * Deletes the character at the current caret index position
    */
   public void caretAtDeleteChar() {
      if (stringDrawable.getLen() > 0 && caretIndex > 0) {
         caretIndexPrevious = caretIndex;
         caretIndex--;
         stringDrawable.deleteCharAt(caretIndex);
         positionCaret();
      }
   }

   /**
    * Insert character c at caretIndex if maximum size allows it.
    * <br>
    * <br>
    * TODO edition on TITLE ? Switch to Horiz scrolling?
    * <br>
    * <br>
    * @param c
    */
   public void caretAtInsertChar(char c) {
      if (sec.isMajuscule()) {
         c = StringUtils.toUpperCase(c);
      } else {
         c = StringUtils.toLowerCase(c);
      }
      int maxChar = stringDrawable.getBOStringData().get1(IBOStrAuxData.SDATA_OFFSET_05_MAX_SIZE1);
      //
      if (maxChar == 0 || stringDrawable.getLen() < maxChar) {
         stringDrawable.addChar(caretIndex, c);

         //generate event
         //since a character has been added
         caretIndex++;
         positionCaret();

         //#debug
         toDLog().pFlow("char " + c + " inserted in ", stringDrawable, StringEditModule.class, "caretAtInsertChar", LVL_05_FINE, true);

         gc.getEventsBusGui().sendNewEvent(IEventsGui.PID_02_CHAR, IEventsGui.PID_02_CHAR_01_ADDED, this);
      }
      sec.predictionStart();
   }

   public void caretAtReplaceChar(ExecutionContextCanvasGui ec, int index, char c) {
      //TODO possible with caretIndex at 0?
      stringDrawable.setCharAt(index, c);
      positionCaret();
      ec.srActionDoneRepaint(caret);
   }

   /**
    * Dispose of resources used by the Module.
    * <br>
    * Absolutely must be called when Drawable is no more drawn. Otherwise, we have a loose Caret Blinking thread.
    * <br>
    * Sets the Caret Blink Thread State to SHUT_DOWN
    * <br>
    */
   public void dispose() {
      isBlinking = false;
   }

   /**
    * Draw caret state and other artifacts.
    * <br>
    * @param g
    */
   public void draw(GraphicsXD g) {
      //System.out.println("#EditModule: Drawing Caret \n\t" + caret.toString("\n\t"));
      caret.setStateFlag(ITechDrawable.STATE_03_HIDDEN, false);
      caret.draw(g);

      //TODO check if the Drawable for word choice is to be drawn in the foreground.
      //program it here.
   }

   /**
    * Caret width is 3 pixels
    * Caret height is line height
    * <br>
    * However caret drawable espouse the current char width.
    * @param g {@link GraphicsX}
    * @param x
    * @param y
    * @param w size of caret determine the clip. Size of caret when moving is the whole interval
    * @param h
    */
   private void drawCaretFromContentListener(GraphicsXD g, int x, int y, int w, int h) {
      Stringer stringer = stringDrawable.getStringer();
      int cw = 0;
      int ch = stringer.getMetrics().getLineHeight();
      if (isCaretEndOfLine()) {
         cw = stringer.getMetrics().getCharWidthEtalon();
      } else {
         cw = stringer.getMetrics().getCharWidth(caretIndex);
      }

      //#debug
      String msg = "Drawing Caret " + x + "," + y + " " + w + "," + h + " cw=" + cw + " ch=" + ch + " caretIndex=" + caretIndex;
      //#debug
      toDLog().pDraw(msg, this, StringEditModule.class, "drawCaretFromContentListener", LVL_05_FINE, true);

      //for simplicity do a clipping
      boolean isClipping = true;
      //isClipping = false;
      if (isClipping) {
         g.clipSet(x, y, cw, ch);
      }
      stringDrawable.drawDrawableBg(g);
      int figW = caretWidth;
      int figH = ch;
      if (isInsertMode) {
         figW = cw;
      }
      if (caretIndexPrevious != caretIndex && stringDrawable.isValidAbsoluteIndex(caretIndexPrevious)) {
         //re-draw the previous char on which the caret is no more.
         drawOldCharCaret(g, caretIndexPrevious);
      }
      if (isCaretEndOfLine()) {
         drawMyCaret(g, x, y, figW, figH);
      } else {
         if (editTech.hasFlag(IBOStrAuxEdit.SEDIT_OFFSET_01_FLAG, IBOStrAuxEdit.SEDIT_FLAG_1_CARET_BG)) {
            //first draw the caret figure in the Background
            drawMyCaret(g, x, y, figW, figH);
            //and then the character
            stringer.drawChar(g, caretIndex);
         } else {
            //first paint the character and 
            stringer.drawChar(g, caretIndex);
            // then in overlay the caret figure in Foreground
            drawMyCaret(g, x, y, figW, figH);
         }
      }

      //draw a single line on a RgbImage. Draw char on another image. Add Pixels background. invert pixel for character 
      if (isClipping) {
         g.clipReset();
      }
      //redraw the previous caret char
      caretIndexPrevious = caretIndex;
   }

   /**
    * Draw the caret ddfigure and char using when {@link Controller} asks to repaint only the caret.
    * <br>
    * <br>
    * TODO when full draw, only draw caret bg or fg
    * <br>
    * <br>
    * 
    */
   public void drawContentListen(GraphicsXD g, int x, int y, int w, int h, Drawable d) {
      drawCaretFromContentListener(g, x, y, w, h);
   }

   /**
    * 
    * @param g
    * @param x
    * @param y
    * @param figW
    * @param figH
    */
   private void drawMyCaret(GraphicsXD g, int x, int y, int figW, int figH) {
      if (isCaretOn) {
         //System.out.println("#EditModule Drawing Caret [" + x + "," + y + " " + figW + "," + figH + "] Figure : " + caretFigure);
         gc.getDC().getFigureOperator().paintFigure(g, x, y, figW, figH, caretFigure);
      }
   }

   /**
    * Sets a new clip overrding any other clip.
    * 
    * @param g
    */
   private void drawOldCharCaret(GraphicsXD g, int index) {
      Stringer stringer = stringDrawable.getStringer();
      int cw = 0;

      int ch = stringer.getMetrics().getLineHeight();
      if (index == stringDrawable.getLen()) {
         cw = stringer.getMetrics().getCharWidthEtalon();
      } else {
         cw = stringer.getMetrics().getCharWidth(index);
      }
      int dx = stringer.getMetrics().getCharX(index);
      int dy = stringer.getMetrics().getCharY(index);
      int finalX = stringDrawable.getContentX() + dx;
      int finalY = stringDrawable.getContentY() + dy;
      //for simplicity do a clipping
      boolean isClipping = true;
      //isClipping = false;
      if (isClipping) {
         g.clipSet(finalX, finalY, cw, ch, GraphicsX.CLIP_DIRECTIVE_1_OVERRIDE);
      }
      stringDrawable.drawDrawableBg(g);
      stringer.drawChar(g, index);

      if (isClipping) {
         g.clipReset();
      }
   }

   public void forceCaretRepaint(boolean isVisible) {
      isCaretOn = isVisible;
      //#debug
      toDLog().pFlow("Caret Blinking " + isCaretOn + " Position " + caretIndex, this, StringEditModule.class, "forceCaretRepaint", LVL_05_FINE, true);

      //ask the controller to initiate a repaint 
      caret.getCanvas().getRepaintCtrlDraw().repaintDrawableCycleBusiness(caret);
   }

   public int getCaretIndex() {
      return caretIndex;
   }

   public int getCaretWidth() {
      return caretWidth;
   }

   public char[][] getCharSetActive() {
      return charset;
   }

   public CmdNode getCmdNode() {
      return cmdNode;
   }

   /**
    * If {@link StringDrawable} is not loaded or does not have one, return the Default One.
    * @return null if not loaded?
    */
   public ByteObject getEditTech() {
      return editTech;
   }

   private StringMetrics getMetrics() {
      return stringDrawable.getStringer().getMetrics();
   }

   public int getNavFlag() {
      // TODO Auto-generated method stub
      return 0;
   }

   /**
    * Decides of the size of the caret {@link Drawable}.
    * <br>
    * <br>
    * But the size may change in Height with text using different fonts.
    */
   public boolean initListen(ByteObject sizerW, ByteObject sizerH, Drawable d) {
      int dh = stringDrawable.getStringer().getMetrics().getLineHeight();
      caretWidth = stringDrawable.getCanvas().getStyleAdapter().getCaretWidth();
      int dw = caretWidth;
      d.setDrawableSize(dw, dh, true);
      return true;
   }

   public boolean isCaretEndOfLine() {
      return caretIndex == stringDrawable.getLen();
   }

   public boolean isLeftMost() {
      return caretIndex == 0;
   }

   public boolean isNavEventSupported(int navEvent) {
      switch (navEvent) {
         case ITechNav.NAV_01_UP:
         case ITechNav.NAV_02_DOWN:
         case ITechNav.NAV_03_LEFT:
         case ITechNav.NAV_04_RIGHT:
            return true;
         default:
            return false;
      }
   }

   /**
    * Called when a 0-9 key is pressed and Phone Keyboard is enabled.
    * <br>
    * <br>
    * @param ic 
    * @param key
    */
   private void manageKey09PressedWrite(ExecutionContextCanvasGui ec, int key) {
      currentKey = key;
      //when a same key is pressed is fast, the next char in the key charset is shown 
      long diff = System.currentTimeMillis() - lastKeyTimestamp;
      long time = (getEditTech().get1(IBOStrAuxEdit.SEDIT_OFFSET_06_SPEED_NUMPAD1) * 10);
      if (isGoToNextChar || key != lastPressedKey || diff > time) {
         isGoToNextChar = true;
         lastPressedKey = key;
         currentKeyStep = 0;
      } else {
         currentKeyStep++;
         //update sec
      }

      //this is shared
      char[] chars = Symbs.getKeyCharSet(charset, key);
      if (chars != null) {
         if (currentKeyStep >= chars.length) {
            currentKeyStep -= chars.length;
         }
         if (isGoToNextChar) {
            caretAtInsertChar(chars[currentKeyStep]);
         } else {
            //TODO possible with caretIndex at 0?
            stringDrawable.setCharAt(caretIndex - 1, chars[currentKeyStep]);
            positionCaret();
         }
         //after insertion, repaint the whole
         ec.srActionDoneRepaint(stringDrawable);
         lastKeyTimestamp = System.currentTimeMillis();
         isGoToNextChar = false;
      }
      ec.srActionDoneRepaint(sec.getLetterChoices());
   }

   /**
    * Modifies caret position and scrolling configuration of {@link StringDrawable}.
    * <br>
    * <br>
    * First delegates to {@link StringEditControl}.
    * <br>
    * Then adds a new character if key event match.
    * <br>
    * Behavior depends on {@link FrameworkCtx} and 
    * {@link IBOStrAuxEdit#SEDIT_FLAG_8_KEYBOARD}
    * <br>
    * <br>
    * @param ic
    */
   public void manageKeyInput(ExecutionContextCanvasGui ec) {
      //ask if StringEditControl has something relevant to do
      sec.manageKeyInput(ec);
      InputConfig ic = ec.getInputConfig();
      if (!ic.isActionDone()) {
         //generate an edit command
         //edit control did not action a command
         if (ic.isPressed()) {
            int key = ic.getIdKeyBut();
            //System.out.println("#EditModule Pressed Key " + key + " char=" + ((char) key) + " = " + TrigCodes.getString(key));
            //maj 
            if (ic.isCancel()) {
               caretAtDeleteChar();
               isGoToNextChar = true;
               ic.setActionDoneRepaint();
            } else if (key >= ITechCodes.KEY_NUM0 && key <= ITechCodes.KEY_NUM9) {
               manageKey09PressedWrite(ec, key);
               ic.setActionDoneRepaint();
            } else if (ic.isFireP()) {
               //become edit
               //the item become the top Drawable if KeyHelp is true, move input item to the top / bottom
               //draw half transparent gr
               ic.setActionDoneRepaint();
            } else {
               //System.out.println("#EditModule char [" + (char) key + "]");
               //check if key is a valid alpha numeri character depending on the current plane
               if (key == ' ' || key < 120 && key > 32) {
                  manageKeyPressedWrite(key);
                  ic.srActionDoneRepaint(stringDrawable);
               }
            }
         }
      }
   }

   /**
    * Manage the normal keyboard key a,b,c,d,e,f.
    * <br>
    * <br>
    * When {@link StringEditControl} has MAJ set, put letter in capital letter.
    * <br>
    * <br>
    * @param key
    */
   public void manageKeyPressedWrite(int key) {
      //this is shared
      char c = (char) key;
      if (isInsertMode) {
         stringDrawable.setCharAt(caretIndex - 1, c);
      } else {
         //add
         caretAtInsertChar(c);
      }
   }

   /**
    *  
    * @param ic
    * @param navEvent
    */
   public void manageNavigate(CmdInstanceGui ic, int navEvent) {

   }

   /**
    * Pressed Pointing event will move the caret.
    * <br>
    * <br>
    * If a VirtualKeyboard is used, it delegates pointing event to it.
    * <br>
    * <br>
    * @param ic
    */
   public void managePointerInput(ExecutionContextCanvasGui ec) {
      //compute caret position from pointer x and y coordinates.
      //check which line
      InputConfig ic = ec.getInputConfig();
      if (ic.isPressed()) {
         StringMetrics sm = stringDrawable.getStringer().getMetrics();

         //#debug
         toDLog().pFlow("Pointer @ " + ic.getInputStateDrawable().getX() + "," + ic.getInputStateDrawable().getY(), this, StringEditModule.class, "managePointerInput", LVL_05_FINE, true);

         int lh = sm.getLineHeight();
         int lineid = ic.getInputStateDrawable().getY();
         if (ic.getInputStateDrawable().getY() > stringDrawable.getContentY() && ic.getInputStateDrawable().getY() < stringDrawable.getContentY() + lh) {
            //first line
            int charid = stringDrawable.getContentX();
            for (int i = 0; i < stringDrawable.getLen(); i++) {
               int cw = sm.getCharWidth(i);
               if (ic.getInputStateDrawable().getX() < charid + cw) {
                  caretIndexPrevious = caretIndex;
                  caretIndex = i;
                  positionCaret();
                  //repaint the whole because caret has moved.
                  ic.srActionDoneRepaint(stringDrawable);
                  break;
               } else {
                  charid += cw;
               }
            }
         }
      }
   }

   public void navigateCmd(CmdInstance navCmd) {
      // TODO Auto-generated method stub

   }

   /**
    * Call Back for DownCmd. What about the NavFlag?
    * <br>
    * <br>
    * Move Caret down a line. When caret is on the last line, around the clock vertical, caret goes up to first line unless
    * there is a topology override. So that for example {@link TableView} gets the navigateDown Event instead.
    * <br>
    * <br>
    * 
    * <br>
    * The Event {@link ITechDrawable#EVENT_04_KEY_FOCUS_LOSS} is fired to
    * Selected state is handled by the TableView
    * <br>
    * <br>
    * 
    */
   public void navigateDown(CmdInstanceGui ci) {
      //inside navitation. updates StringDrawable viewpane
      if (numEditLines > 1) {
         //TODO even in multiline, if a prediction table is shown. down will select first element in table
         //
      }

      gc.getFocusCtrl().newFocusKey(ci.getExecutionContextGui(), stringDrawable);

   }

   /**
    * Move the Caret Left. Once reached the end of the line. Go up a line.
    * <br>
    * <br>
    * If horizontal around the clock, go.
    * Caret Drawable coordinates are updated.
    * @param InputConfig
    */
   public void navigateLeft(ExecutionContextCanvasGui ec) {
      InputConfig ic = ec.getInputConfig();
      if (!ic.isPressed()) {
         return;
      }
      if (!isLeftMost()) {
         caretIndexPrevious = caretIndex;
         caretIndex--;
         currentKeyStep = 0;
         isGoToNextChar = true;
         positionCaret();
         //repaint caret from older position as well.
         ic.srActionDoneRepaint(caret);
      } else {
         if (caretIndex == 0) {
            //No navigation => delegate to hierarchy
            //when no other possiblity, it will be given the red light to do a cycle
         }
      }
   }

   /**
    * {@link IDrawable#navigateRight(ExecutionContextCanvasGui)}
    * <br>
    * @param ic
    */
   public void navigateRight(ExecutionContextCanvasGui ec) {
      InputConfig ic = ec.getInputConfig();
      if (!ic.isPressed()) {
         return;
      }
      if (caretIndex + 1 <= stringDrawable.getLen()) {
         caretIndexPrevious = caretIndex;
         caretIndex++;
         currentKeyStep = 0;
         isGoToNextChar = true;
         positionCaret();
         ic.srActionDoneRepaint(caret);
      }
   }

   public void navigateSelect(ExecutionContextCanvasGui ec) {
   }

   /**
    * Check if Caret is on first line.
    * <br>
    * Then do a focus out to {@link StringEditControl}.
    * {@link Controller} tracks that a Down Focus Out bring back the focus on this {@link StringDrawable}.
    * When key focus in on {@link StringEditControl} and a Down event is registered? it comes to parent of {@link StringEditControl}
    * that takes focus out. But for Up? {@link StringDrawable} does not act, so delegated to {@link StringDrawable} parent.
    * <br>
    * <br>
    * When the choice of word table is active Down gives focus to that table first cell while still redirecting other key events
    * to {@link StringDrawable}. It is the parent of the Table.
    * <br>
    * Left and Right events are controlled by {@link StringDrawable} as well.
    * <br>
    * <br>
    * @param ic
    */
   public void navigateUp(ExecutionContextCanvasGui ec) {
      //moves caret position
      if (numEditLines == 1) {
         gc.getFocusCtrl().newFocusKey(ec, sec);
      }
   }

   /**
    * Called after modifying caretIndex to position caret {@link Drawable}. 
    * <br>
    * <br>
    * Updates the {@link StringMetrics} values.
    * <br>
    * TODO special case when caretIndex = len
    * <br>
    * <br>
    */
   public void positionCaret() {
      StringMetrics metrics = getMetrics();
      int dx = 0;
      int dy = 0;
      int dw = 0;
      int dh = metrics.getLineHeight();
      if (caretIndex == stringDrawable.getLen()) {
         if (caretIndex == 0) {

         } else {
            dx = metrics.getWidthConsumed(caretIndex - 1);
            dy = metrics.getCharY(caretIndex - 1);
         }
      } else {
         dx = metrics.getCharX(caretIndex);
         dy = metrics.getCharY(caretIndex);
         dw = metrics.getCharWidth(caretIndex);
      }

      int finalX = stringDrawable.getContentX() + dx;
      int finalY = stringDrawable.getContentY() + dy;
      caret.setXY(finalX, finalY);
      caret.setDrawableSize(dw, dh, true);

      //#debug
      String msg = "[" + finalX + "," + finalY + "] dxdy=" + +dx + "," + dy + "]" + " size=" + dw + "," + dh;
      //#debug
      toDLog().pInit(msg, this, StringEditModule.class, "positionCaret", LVL_05_FINE, true);
   }

   /**
    * Called when {@link StringDrawable#setXY(int, int)} is called.
    * <br>
    * <br>
    * Force the {@link StringEditControl} to be repositioned if needed.
    * <br>
    * <br>
    * @param x
    * @param y
    */
   public void set(int x, int y) {
      sec.positionControl();
   }

   /**
    * Update to a new {@link StringDrawable}.
    * <br>
    * Uses the Edit tech {@link ITechLinks#LINK_42_BO_STRING_EDIT} from the {@link StringDrawable} style class.
    * <br>
    * Position the caret.
    * 
    * @param sd
    */
   public void setStringDrawable(StringDrawable sd) {
      stringDrawable = sd;
      StyleClass sc = sd.getStyleClass();

      ByteObject editTech = sc.getByteObject(ITechLinks.LINK_42_BO_STRING_EDIT);
      if (editTech != null) {
         this.editTech = editTech;
      } else {
         this.editTech = gc.getDrawableStringFactory().getBOStringEditNormal();
      }
      caretFigure = sc.getByteObject(ITechLinks.LINK_43_CARET_FIGURE);
      if (caretFigure == null) {
         caretFigure = gc.getStyleManager().getDefaultCaretFigure();
      }

      caretIndex = sd.getStringer().getEditor().getCaretIndex();

      StyleClass scc = gc.getEmptySC();
      //caret size and position depends on current line
      caret = new DrawableInjected(gc, scc, sd, sd);
      //class controls the repaint of StringDrawable background
      caret.setStateFlag(ITechDrawable.STATE_17_OPAQUE, true); //prevent repaint
      caret.init(0, 0);
      //position caret to first caretIndex
      positionCaret();

      //#debug
      toDLog().pInit("Caret Init", caret, StringEditModule.class, "setStringDrawable", LVL_05_FINE, false);

   }

   public void setStringEditController(StringEditControl sec) {
      this.sec = sec;
   }

   /**
    * Start the Trie Dic thead with current string as prefix for search
    */
   public void startDicSearching() {

   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, StringEditModule.class, toStringGetLine(850));
      toStringPrivate(dc);
      super.toString(dc.sup());

      dc.appendVarWithSpace("isBlinking", isBlinking);
      dc.append(" keyStep=" + currentKeyStep);
      dc.nlLvl("Caret Figure ", caretFigure);
      dc.nlLvl(editTech);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, StringEditModule.class, toStringGetLine(850));
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }
   //#enddebug

}
