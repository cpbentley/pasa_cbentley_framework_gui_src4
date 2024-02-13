package pasa.cbentley.framework.gui.src4.string;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesDrw;
import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.core.src4.i8n.I8nString;
import pasa.cbentley.core.src4.io.XString;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.coredraw.src4.interfaces.IMFont;
import pasa.cbentley.framework.drawx.src4.ctx.ToStringStaticDrawx;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.factories.interfaces.IBOFigString;
import pasa.cbentley.framework.drawx.src4.string.StringMetrics;
import pasa.cbentley.framework.drawx.src4.string.Stringer;
import pasa.cbentley.framework.drawx.src4.string.interfaces.ITechStringDrw;
import pasa.cbentley.framework.drawx.src4.style.IBOStyle;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.canvas.ViewContext;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceDrawable;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.FigDrawable;
import pasa.cbentley.framework.gui.src4.core.LayouterEngineDrawable;
import pasa.cbentley.framework.gui.src4.core.ScrollConfig;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.core.ViewDrawable;
import pasa.cbentley.framework.gui.src4.core.ViewPane;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.ctx.ToStringStaticGui;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStringData;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawListener;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechViewDrawable;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.tech.ITechLayoutDrawable;
import pasa.cbentley.framework.gui.src4.tech.ITechStringDrawable;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;
import pasa.cbentley.framework.gui.src4.utils.DrawableUtilz;
import pasa.cbentley.layouter.src4.engine.SizerFactory;
import pasa.cbentley.layouter.src4.tech.IBOSizer;
import pasa.cbentley.layouter.src4.tech.ITechLayout;
import pasa.cbentley.powerdata.spec.src4.power.IPowerCharCollector;

/**
 * 
 * Draws a String using : 
 * <li> char[] array
 * <li> String
 * <li>Scaled single Char using {@link IDrwTypes#TYPE_070_TEXT_EFFECTS}
 * <br>
 * <br>
 * <li> Read only {@link ITechStringDrawable#S_ACTION_MODE_0_READ}
 * <li> chars are Selectable {@link ITechStringDrawable#S_ACTION_MODE_1_SELECT}
 * <li> chars are editable and selectable {@link ITechStringDrawable#S_ACTION_MODE_2_EDIT}
 * <br>
 * For drawing a scaled char figure, one can also use {@link FigDrawable} and {@link IDrw#FIG_TYPE_10_STRING}
 * <br>
 * <br>
 * See {@link FigDrawable} for single character figures.
 * <br>
 * Use {@link Stringer} for drawing. Which style should be used to initialize preferred sizes? It may be given
 * by TechString field providing init style flags. 
 * In which case, char widths must always be recomputed, since they change.
 * <br>
 * <b>Text effects</b> : <br>
 * An array of int[] decides which style to apply to the character at that index. <br>
 * {@link ByteObject} TxtEffect may be defined in the Style.<br>
 * <br>
 * <b>Long String Display Options</b> : <br>
 * <li>Scrolling.
 * <li>Ticker Animation.
 * <li>Trimming. {@link ITechDrawable#STATE_09_TRIMMED} is set.
 *  In case of Trim, the last two letters will be replaced with ..
 *  <br>
 * <br>
 * Strings are malleable in both X and Y axis.<br>
 * 
 * For ease of use presets have been defined. The default preset {@link ITechStringDrawable#PRESET_CONFIG_0_NONE} does nothing.
 *  Others override sizers width and sizer height values given by {@link StringDrawable#initViewDrawable(LayouterEngineDrawable)}.
 *  
 * The shrink behaviors are also decided by the preset. <br>
 * Concerning area constraints, the StringDrawable have preset constraint types.
 * <br>
 * <b>Preset Configurations</b> :
 * <li> {@link ITechStringDrawable#PRESET_CONFIG_0_NONE}. No default shrink behavior. Take into account implicit width and height values.
 * <ol>
 * <li>When both height and width is 0, behaves like {@link ITechStringDrawable#PRESET_CONFIG_4_NATURAL_NO_WRAP} but without scrolling.
 * Preferred size being equal to drawn size.
 * <li>When height is 0. Behaves like {@link ITechStringDrawable#PRESET_CONFIG_3_SCROLL_V} without the shrinkH and no scrolling.
 * <li>When width is 0, dw = pw.
 * </ol>
 * <li> {@link ITechStringDrawable#PRESET_CONFIG_1_TITLE}. One line of trimmed at width text. No Shrinking. Width and Height decides area.
 * <li> {@link ITechStringDrawable#PRESET_CONFIG_3_SCROLL_V}. X lines of text constrained at width. Shrink to fit as many lines as possible
 * allowed by height. Take into account negative init for the number of visual lines to be seen in the ViewPort
 * <li> {@link ITechStringDrawable#PRESET_CONFIG_2_SCROLL_H}. Horizontal scrolling.
 * <li> {@link ITechStringDrawable#PRESET_CONFIG_4_NATURAL_NO_WRAP}
 * <br>
 * Presets' shrink behavior is decided at construction time but may be changed afterwards.
 * <br>
 * <br>
 * <b>In a {@link TableView}</b> : <br>
 * {@link StringDrawable}s are often used in a {@link TableView}. In that case, the String type is dependant on the policy.
 * Usually they will be called with init(X,-1). A one line trimmed at X pixels. or
 * init(0,-1).
 * <br>
 * <br>
 * <b>Too Big Strings </b>:<br>
 * Mechanism to reduce overhead of huge strings for weak CPUs.
 * <li>Disable FX
 * <br>
 * <br>
 * <b>Scrolling </b>: <br>
 * <li>logical : lines vertically and characters horizontally
 * <li>pixel based : preferred height and width
 * <li>Page scrolling with Strings is smart. When a word is not fully displayed on a page,
 * next page starts with that word.
 * <br>
 * Partial Lines:
 * 
 * In Windows Explorer, the first visible increment (top) is always fully visible. That is, it stays
 * root increment, even during a scrolldown. A blank space is drawn when last
 * increment is reached and no partially visible unit is shown.
 * In showing text, the eyes are focused on the last line, so we want the root to be the last visible row
 * <br>
 * <br>
 * <b>String edition</b>
 * <br>
 * TODO Automatic Edition options in StringTech:
 * {@link StringDrawable#manageKeyInput(InputConfig)} Fire
 * See {@link StringEditModule}.
 * <br>
 * <br>
 * <b>String localization</b>
 * State localized:
 * Key to real string
 * <br>
 * <br>
 * 
 * @see I8nString
 * <br>
 * <br>
 * @author Charles-Philip Bentley
 * @see {@link ViewDrawable}
 * 
 *
 */
public class StringDrawable extends ViewDrawable implements IBOStringData, IDrawListener {

   /**
    * Controls the Input of characters.
    * <br>
    * {@link StringEditModule} calls back {@link StringDrawable} with
    * <li> {@link StringDrawable#deleteCharAt(int)}
    * <li> {@link StringDrawable#addChar(int, char)}
    * <br>
    * <br>
    * Set external by {@link StringEditControl} using {@link StringDrawable#setEditModule(StringEditModule)}.
    * <br>
    */
   StringEditModule    editModule;

   /**
    * Everytime, it is drawn, The Stringer must be updated if the {@link I8nString}
    * content has changed.
    */
   protected I8nString str;

   /**
    * Valid until invalidate by new string or init method
    */
   Stringer            stringer;

   private ByteObject  boFigString;

   public StringDrawable(GuiCtx gc, StyleClass sc, char[] cs, int offset, int len, ByteObject boStringData) {
      super(gc, sc);

      //before creating stringer, we need the style and boStringData decided
      initTech(boStringData);
      createStringer();
      setStringNoUpdate(cs, offset, len);
   }

   public StringDrawable(GuiCtx gc, StyleClass sc, char[] str, int offset, int len) {
      this(gc, sc, str, offset, len, null);

   }

   public StringDrawable(GuiCtx gc, StyleClass sc, I8nString str) {
      super(gc, sc);
      if (str == null)
         throw new NullPointerException("null string");
      this.str = str;
      initTech(null);
   }

   /**
    * Creates a {@link StringDrawable} to display the String using the given style.
    * <br>
    * <br>
    * The {@link IBOStringData#SDATA_OFFSET_02_PRESET_CONFIG1} decides the breaking during
    * initialization.
    * 
    * @param sc
    * @param str
    */
   public StringDrawable(GuiCtx gc, StyleClass sc, String str) {
      this(gc, sc, str.toCharArray(), 0, str.length());
   }

   /**
    * Overrides the {@link IBOStringData} from the style class
    * <br>
    * <br>
    * 
    * @param sc
    * @param str
    * @param boStringData
    */
   public StringDrawable(GuiCtx gc, StyleClass sc, String str, ByteObject boStringData) {
      super(gc, sc);

      initTech(boStringData);

      setBOStringData(boStringData);
      createStringer();
      setStringNoUpdate(str);
   }

   /**
    * Deprecated method that create a StringTech for the given type.
    * 
    * 
    * @param str
    * @param st
    * @param presetConfig Predefined type hardcode type from constructor.
    * It is too cumbersome to create a style class just for a type variation
    * But it was designed for that
    */
   public StringDrawable(GuiCtx gc, StyleClass sc, String str, int presetConfig) {
      super(gc, sc);

      ByteObject boStringDrawable = styleClass.getByteObject(IBOTypesGui.LINK_41_BO_STRING_DRAWABLE);
      if (boStringDrawable == null) {
         boStringDrawable = gc.getDrawableStringFactory().getStringTech(presetConfig);
      }
      if (presetConfig != -1) {
         boStringDrawable = boStringDrawable.cloneCopyHead();
         boStringDrawable.set1(IBOStringData.SDATA_OFFSET_02_PRESET_CONFIG1, presetConfig);
      }
      initTech(boStringDrawable);
      createStringer();
      setStringNoUpdate(str);

   }

   /**
    * Always do a copy.
    * <br>
    * Force the re layout of all the {@link StringDrawable}?
    * <br>
    * <br>
    * {@link StringDrawable#stringUpdate()} is called which updates the layout<br>
    * <br>
    * 
    * This is the entry method for Android Input Method
    * <br>
    * <br>
    * 
    * @param indexRelative
    * @param c
    */
   public synchronized void addChar(int indexRelative, char c) {
      throw new RuntimeException();
   }

   public synchronized void addChars(int indexRelative, char[] chars) {
      throw new RuntimeException();
   }

   /**
    * 
    * When receiving key focus {@link ITechDrawable#EVENT_03_KEY_FOCUS_GAIN}  and flag {@link IBOStringData#SDATA_FLAG_5_AUTO_EDIT_MODE} is set,
    * {@link StringDrawable} toggles on Edition mode.
    * <br>
    * When in mode {@link ITechStringDrawable#PRESET_CONFIG_1_TITLE}, type is temporarily set to {@link ITechStringDrawable#PRESET_CONFIG_3_SCROLL_V}
    * or {@link ITechStringDrawable#PRESET_CONFIG_2_SCROLL_H} using the given size at the time the switch is made.
    * <br>
    * On certain conditions, when size of {@link StringDrawable} is deemed too small (less than 10 letters space), an edit window appears above.
    * <br> 
    * <br>
    * or that window is activated by a button on the {@link StringEditControl}.
    * <br>
    * <br>
    * 
    * @param ic
    */
   protected void cmdToggleEdition(InputConfig ic) {
      //System.out.println("#StringDrawable#cmdToggleEdition");
      if (editModule == null) {
         editTakeCtrl();
      } else {
         editRemoveCtrl();
      }
      ic.srActionDoneRepaint();
   }

   private void initPreferredSizeFromViewPortArea(LayouterEngineDrawable ds, int breakWidth, int breakHeight) {

      //#debug
      toDLog().pInit("breakWidth=" + breakWidth + " breakHeight=" + breakHeight, this, StringDrawable.class, "initPreferredSizeFromViewPortArea@312", LVL_05_FINE, true);

      Stringer initStringer = getStringer();
      StringMetrics metrics = initStringer.getMetrics();
      int typeStr = getPresetConfig();
      if (typeStr == ITechStringDrawable.PRESET_CONFIG_5_CHAR) {
         initViewDrawableCharType(breakWidth, breakHeight);
      } else {
         //----------------------------
         //compute breaks, preferred dimension given dim parameters and type

         initStringer.setAreaWH(breakWidth, breakHeight);
         initStringer.setBreakOnArea();

         initStringer.buildAgain();

         int pw = metrics.getPrefWidth();
         int ph = metrics.getPrefHeight();

         ds.setPh(ph);
         ds.setPw(pw);

         //clipping is only necessary when partial strings are drawn.
         if ((hasFlagView(FLAG_VSTATE_22_SCROLLED))) {
            if (viewPane != null && viewPane.isContentClipped()) {
               //TODO is this possible?
               //clipping is also needed when init style is smaller than some state styles?
               setFlagView(ITechViewDrawable.FLAG_VSTATE_01_CLIP, true);
            } else {
               setFlagView(ITechViewDrawable.FLAG_VSTATE_01_CLIP, false);
            }
         } else {
            setFlagView(ITechViewDrawable.FLAG_VSTATE_01_CLIP, false);
         }
      }
   }

   /**
    * 
    * @param indexRelative
    */
   public synchronized void deleteCharAt(int indexRelative) {
      throw new RuntimeException();
   }

   public synchronized void deleteCharsAt(int indexRelative, int len) {
      throw new RuntimeException();
   }

   /**
    * Updates flags
    * <li>{@link ITechDrawable#BEHAVIOR_26_NAV_VERTICAL}
    * <li>{@link ITechDrawable#BEHAVIOR_27_NAV_HORIZONTAL}
    * <li>{@link ITechDrawable#BEHAVIOR_28_NAV_SELECTABLE}
    * <br>
    * 
    */
   public void doUpdateNavBehavior() {
      boolean isNavH = false;
      boolean isNavV = false;
      boolean isSelect = false;
      isSelect = (boStringDrawable.get1(SDATA_OFFSET_06_ACTION_MODE1) == ITechStringDrawable.S_ACTION_MODE_2_EDIT) ? true : false;
      if (editModule != null) {
         isNavH = true;
         isNavV = true;
      }
      setBehaviorFlag(ITechDrawable.BEHAVIOR_27_NAV_HORIZONTAL, isNavH);
      setBehaviorFlag(ITechDrawable.BEHAVIOR_26_NAV_VERTICAL, isNavV);
      setBehaviorFlag(ITechDrawable.BEHAVIOR_28_NAV_SELECTABLE, isSelect);
   }

   /**
    * 
    */
   public void drawContentListen(GraphicsX g, int x, int y, int w, int h, Drawable d) {
      if (editModule != null) {
         editModule.drawContentListen(g, x, y, w, h, d);
      }
   }

   /**
    * 
    */
   public void drawViewDrawableContent(GraphicsX g, int x, int y, ScrollConfig scX, ScrollConfig scY) {
      //System.out.println("#StringDrawableContent InputMode=" + techDrawable.get1(IStringDrawable.INPUT_OFFSET_06_MODE1));
      //draw the caret
      //TODO set clip only if needed. use a helper perf flag for that.
      if (hasFlagView(ITechViewDrawable.FLAG_VSTATE_01_CLIP)) {
         g.clipSet(getContentX(), getContentY(), getContentW(), getContentH());
      }
      //DrawableUtilz.debugScrollConfigs("StringDrawable.drawContent", scX, scY);
      //decides the number of lines to draw
      int hNum = getNumOfLines();
      int hOffset = 0;
      int wNum = stringer.getLen();
      int wOffset = 0;
      if (scX != null) {
         switch (scX.getTypeUnit()) {
            case ITechViewPane.SCROLL_TYPE_0_PIXEL_UNIT:
               //draw helper will automatically starts/stops when characters are over the content area.
               x -= scX.getSIStart();
               //compute within which logic unit it falls.
               break;
            case ITechViewPane.SCROLL_TYPE_1_LOGIC_UNIT:
               //could be a letter or a word
               wOffset = scX.getSIStart();
               wNum = scX.getSIVisible();
               x -= scX.getPartialOffset();
               break;
            case ITechViewPane.SCROLL_TYPE_2_PAGE_UNIT:
               int c = scX.getSIStart();//multiply this
               wNum = stringer.getLen() - wOffset;
               break;
            default:
               throw new IllegalArgumentException();
         }
      }
      if (scY != null) {
         //#debug
         toDLog().pFlow("msg", scY, StringDrawable.class, "drawViewDrawableContent", LVL_05_FINE, true);
         switch (scY.getTypeUnit()) {
            case ITechViewPane.SCROLL_TYPE_0_PIXEL_UNIT:
               //draw helper will automatically starts/stops when characters are over the content area.
               //compute offset
               y -= scY.getSIStart();
               break;
            case ITechViewPane.SCROLL_TYPE_1_LOGIC_UNIT:
               hOffset = scY.getSIStart();//start line visible
               hNum = scY.getSIVisible(); //number of lines visible. include partially visible if any.
               y -= scY.getPartialOffset();
               break;
            case ITechViewPane.SCROLL_TYPE_2_PAGE_UNIT:
               int c = scY.getSIStart();//multiply this
               hNum = getNumOfLines() - hOffset;
               break;
            default:
               throw new IllegalArgumentException();
         }
      }
      //System.out.println("@StringDrawable#draw wOffset=" + wOffset + " wNum=" + wNum + " hOffset=" + hOffset + " hNum=" + hNum);
      //alignment is applied when drawing area is bigger than content size
      Stringer stringer2 = getStringer();
      stringer2.setAreaWH(getContentW(), getContentH());
      ByteObject styleAnchor = getStyleOp().getStyleAnchor(style);
      stringer2.setAnchor(styleAnchor);
      stringer2.drawOffsets(g, x, y, wOffset, wNum, hOffset, hNum);
      //System.out.println(getStringer());

      if (editModule != null) {
         //ask the module to draw the caret and other active items such as the drop down choices
         editModule.draw(g);
      }

      //check to reset clip area.
      if (hasFlagView(ITechViewDrawable.FLAG_VSTATE_01_CLIP)) {
         g.clipReset();
      }

   }

   private void editRemoveCtrl() {
      StringEditControl sec = gc.getStrEditCtrl();
      sec.removeControl();
      vc.getDrawCtrlAppli().removeDrawable(null, this, null);
   }

   private void editTakeCtrl() {
      StringEditControl sec = gc.getStrEditCtrl();
      sec.doTakeControl(this);
   }

   public ByteObject getBOStringData() {
      return boStringDrawable;
   }

   /**
    * Decides which breaking method to use according to context {@link IBOStringData#SDATA_OFFSET_02_PRESET_CONFIG1}
    * <br>
    * <br>
    * <li>{@link ITechStringDrawable#PRESET_CONFIG_0_NONE}
    * <li>{@link ITechStringDrawable#PRESET_CONFIG_1_TITLE} of 1 or 2 characters are never trimmed.
    * <br>
    * Logical width prevents the following break types:
    * <li> {@link ITechStringDrw#BREAK_4_TRIM_SINGLE_LINE}
    * <br>
    * <br>
    * @param width
    * @param height
    * @param type {@link ITechStringDrawable#PRESET_CONFIG_0_NONE} ,  {@link ITechStringDrawable#PRESET_CONFIG_1_TITLE}, ...
    * @return
    */
   public int getBreakType(int width, int height, int type, int len) {
      int breakType = ITechStringDrw.BREAK_0_NONE;
      switch (type) {
         case ITechStringDrawable.PRESET_CONFIG_0_NONE:
            //
            if (width > 0) {
               if (height == 0) {
                  //infinite number of lines drawn. no scrolling no trimming.
                  breakType = ITechStringDrw.BREAK_1_WIDTH;
               } else if (height < 0) {
                  //height (1,2,3,...) visible lines //trim/scrolling the rest depending on behavior.
                  if (height == -1) {
                     breakType = ITechStringDrw.BREAK_4_TRIM_SINGLE_LINE;
                  } else {
                     breakType = ITechStringDrw.BREAK_5_TRIM_FIT_HEIGHT;
                  }
               } else {
                  breakType = ITechStringDrw.BREAK_5_TRIM_FIT_HEIGHT;
               }
            } else {
               if (width == 0) {
                  //everything 
                  breakType = ITechStringDrw.BREAK_0_NONE;
               } else if (width < 0) {
                  //only a few
               }
            }
            break;
         case ITechStringDrawable.PRESET_CONFIG_2_SCROLL_H:
            breakType = ITechStringDrw.BREAK_3_ONE_LINE;
            break;
         case ITechStringDrawable.PRESET_CONFIG_1_TITLE:
            breakType = ITechStringDrw.BREAK_4_TRIM_SINGLE_LINE;
            if (width <= 0) {
               //no trimming. take all the width needed
               breakType = ITechStringDrw.BREAK_0_NONE;
            } else if (width < 0) {
               //set clipping and ignore breaks
               breakType = ITechStringDrw.BREAK_0_NONE;
            }
            if (len <= 2) {
               //no trimming
               breakType = ITechStringDrw.BREAK_0_NONE;
            }
            break;
         case ITechStringDrawable.PRESET_CONFIG_4_NATURAL_NO_WRAP:
            breakType = ITechStringDrw.BREAK_2_NATURAL;
            break;
         case ITechStringDrawable.PRESET_CONFIG_3_SCROLL_V:
            breakType = ITechStringDrw.BREAK_1_WIDTH;
            if (width == 0) {
               breakType = ITechStringDrw.BREAK_0_NONE;
            }
            break;

         default:
            throw new IllegalArgumentException();
      }
      return breakType;
   }

   /**
    * 
    * @return {@link StringEditModule} initialized with this {@link StringDrawable}.
    */
   public StringEditModule getEditModule() {
      return editModule;
   }

   public int getLen() {
      return stringer.getLen();
   }

   /**
    * For a {@link StringDrawable}, 1 logic unit is the height of a line.
    */
   protected int getLogicalHeight(int h) {
      int unitSize = getStringer().getMetrics().getPrefCharHeight();
      return h * unitSize;
   }

   protected int getLogicalWidth(int w) {
      int unitSize = getStringer().getMetrics().getPrefCharWidth();
      return w * unitSize;
   }

   /**
    * Computes the maximum number of lines to be displayed.
    * 
    * -1 meaning any number
    * @param width
    * @param height
    * @param preset
    * @return
    */
   public int getMaxLines(int width, int height, int preset) {
      int maxLines = -1;
      switch (preset) {
         case ITechStringDrawable.PRESET_CONFIG_0_NONE:
            ByteObject sizerH = layEngine.getSizerH();
            if (sizerH != null) {
               int etalon = sizerH.get1(IBOSizer.SIZER_OFFSET_03_ETALON1);
               int prop = sizerH.get1(IBOSizer.SIZER_OFFSET_05_ET_PROPERTY1);
               if (etalon == ITechLayoutDrawable.ETALON_0_SIZEE_CTX && prop == ITechLayoutDrawable.SIZER_PROP_02_UNIT_LOGIC) {
                  int lines = sizerH.get2(IBOSizer.SIZER_OFFSET_08_FRACTION_TOP2);
                  maxLines = lines;
               }
            }
            break;
         case ITechStringDrawable.PRESET_CONFIG_1_TITLE:
         case ITechStringDrawable.PRESET_CONFIG_5_CHAR:
            maxLines = 1;
            break;
         case ITechStringDrawable.PRESET_CONFIG_2_SCROLL_H:
         case ITechStringDrawable.PRESET_CONFIG_3_SCROLL_V:
         case ITechStringDrawable.PRESET_CONFIG_4_NATURAL_NO_WRAP:
            break;
         default:
            throw new IllegalArgumentException();
      }
      return maxLines;
   }

   /**
    * As currently initialized what is the number of lines used to draw the String?
    * Default is 1
    * @return
    */
   public int getNumOfLines() {
      return stringer.getNumOfLines();
   }

   public int getOffset() {
      return stringer.getOffsetChar();
   }

   /**
    * From {@link IBOStringData#SDATA_OFFSET_02_PRESET_CONFIG1}
    * 
    * <li> {@link ITechStringDrawable#PRESET_CONFIG_0_NONE}
    * <li> {@link ITechStringDrawable#PRESET_CONFIG_1_TITLE}
    * <li> {@link ITechStringDrawable#PRESET_CONFIG_2_SCROLL_H}
    * <li> {@link ITechStringDrawable#PRESET_CONFIG_3_SCROLL_V}
    * <li> {@link ITechStringDrawable#PRESET_CONFIG_4_NATURAL_NO_WRAP}
    * <li> {@link ITechStringDrawable#PRESET_CONFIG_5_CHAR}
    * 
    * @return
    */
   public int getPresetConfig() {
      return boStringDrawable.get1(IBOStringData.SDATA_OFFSET_02_PRESET_CONFIG1);
   }

   /**
    * Overrides
    */
   public int getSizePreferredHeight() {
      //TODO
      if (hasFlagView(FLAG_VSTATE_08_PREF_SIZE_COMPUTED)) {
         return layEngine.getPh();
      } else {
         int preset = getPresetConfig();
         if (preset == ITechStringDrawable.PRESET_CONFIG_1_TITLE) {
            //a preferred title H is 1 line
            return getStringMetrics().getPrefCharHeight();
            //logic 2 is 2 lines
         }
         //when no computed.. it was called by init method
         return vc.getHeight();
      }
   }

   /**
    * String copy of the char array with offset and length.
    * <br.
    * @return non null
    */
   public String getString() {
      return stringer.getDisplayedString();
   }

   /**
    * Returns the String actually displayed on the Screen.
    * <br>
    * <br>
    * This will include
    * <li> .. trim cues
    * <li> prompt text.
    * <br>
    * <br>
    * 
    * @return
    */
   public String getStringDisplayed() {
      return stringer.getDisplayedString();
   }

   /**
    * Return drawing pipeline.
    * <br>
    * <br>
    * Gives access to {@link StringMetrics}.
    * <br>
    * Stringer must be updated when style has changed after a state change.
    * <br>
    * <br>
    * @return {@link Stringer}
    */
   public Stringer getStringer() {
      return stringer;
   }

   private Stringer createStringer() {
      ByteObject textFigure = getStringerStyle();
      stringer = new Stringer(gc.getDC());
      stringer.setTextFigure(textFigure);
      //old issue.. when x,y are modified afterwards? stringer needs to be updated
      stringer.setAreaXYWH(getContentX(), getContentY(), getContentW(), getContentH());
      return stringer;
   }

   /**
    * The {@link IBOFigString} overriding style's
    * 
    * <p>
    * When null, {@link StringDrawable} will use the {@link IBOStyle} from its {@link StyleClass}
    * </p>
    * 
    * <p>
    * Set it using {@link StringDrawable#setBOFigString(ByteObject)}
    * </p>
    * @return
    */
   public ByteObject getBOFigString() {
      return boFigString;
   }
   
   public void setBOFigString(ByteObject boFigString) {
      this.boFigString = boFigString;
      this.stringer.setTextFigure(boFigString);
   }

   /**
    * Gets the {@link IBOFigString} used for computing {@link StringMetrics} in {@link Stringer}.
    * 
    * @return cannot be null and must be {@link IBOTypesDrw#TYPE_050_FIGURE}
    */
   private ByteObject getStringerStyle() {
      int initStyle = boStringDrawable.get4(SDATA_OFFSET_07_INIT_STYLE_FLAGS4);
      ByteObject strFigure = null;
      //System.out.println("#StringDrawable InitStyle " + initStyle + " " + new String(chars, offset, Math.min(len, 15)) + " " + toStringDimension());
      if (initStyle == 0) {
         strFigure = getStyleOp().getContentStyle(style);
      } else {
         strFigure = getStyleOp().getContentStyle(styleClass.getStyle(initStyle, ctype, structStyle));
      }
      if (strFigure == null) {
         //#debug
         toDLog().pNull("Style could not be Found", this, StringDrawable.class, "getStringerStyle", LVL_09_WARNING, true);
         strFigure = gc.getStyleManager().getDefaultContentStyle();
      }
      //#mdebug
      strFigure.checkType(IBOTypesDrw.TYPE_050_FIGURE);
      //#enddebug
      return strFigure;
   }

   public StringMetrics getStringMetrics() {
      Stringer initStringer = getStringer();
      StringMetrics metrics = initStringer.getMetrics();
      return metrics;
   }

   /**
    * {@link XString} of the char array with offset and length.
    * <br>
    * Modifying has an impact
    * <br>
    * @return non null
    */
   public XString getStringX() {
      return stringer.getStringX();
   }

   public boolean initListen(ByteObject sizerW, ByteObject sizerH, Drawable d) {
      if (editModule != null) {
         return editModule.initListen(sizerW, sizerH, d);
      }
      return false;
   }

   /**
    * 
    */
   public void initScrollingConfig(ScrollConfig scX, ScrollConfig scY) {
      if (scX != null) {
         if (scX.getTypeUnit() == ITechViewPane.SCROLL_TYPE_1_LOGIC_UNIT) {
            int incrementPixelSize = getStyleOp().getPW(style, "  ");
            scX.initConfigLogic(incrementPixelSize, getContentW(), stringer.getLen());
         } else {
            DrawableUtilz.initConfigX(this, scX);
         }
      }
      if (scY != null) {
         if (scY.getTypeUnit() == ITechViewPane.SCROLL_TYPE_1_LOGIC_UNIT) {
            int incrementPixelSize = getStyleOp().getPH(style, 1);
            scY.initConfigLogic(incrementPixelSize, getContentH(), getNumOfLines());
         } else {
            DrawableUtilz.initConfigY(this, scY);
         }
      }
      //DrawableUtilz.debugScrollConfigs("StringDrawable.update", scX, scY);

   }

   /**
    * 
    */
   protected void initTech(ByteObject boStringData) {
      if (boStringData == null) {
         boStringData = styleClass.getByteObject(IBOTypesGui.LINK_41_BO_STRING_DRAWABLE);
         if (boStringData == null) {
            boStringData = gc.getDrawableStringFactory().getDefaultStringTech();
         }
      }
      this.boStringDrawable = boStringData;
      int type = boStringDrawable.get1(IBOStringData.SDATA_OFFSET_02_PRESET_CONFIG1);
      setTypeString(type);
   }

   /**
    * Compute preferred size based on String preset configuration and sizers.
    * <p>
    * 
    * For example {@link ITechStringDrawable#PRESET_CONFIG_3_SCROLL_V} will take preferred size 
    * from {@link ViewContext}.
    * </p>
    * 
    * The init method of Drawable has already been called by {@link ViewDrawable}. <br>
    * <br>
    * <p>
    * The {@link IBOStringData#SDATA_OFFSET_02_PRESET_CONFIG1} applies to decide how the preferred dimension is computed.
    * <li> {@link ITechStringDrawable#PRESET_CONFIG_2_SCROLL_H} gives all text on 1 line.  </li>
    * <li> {@link ITechStringDrawable#PRESET_CONFIG_4_NATURAL_NO_WRAP}  </li>
    * <li> {@link ITechStringDrawable#PRESET_CONFIG_1_TITLE} text is trimmed to fit area </li>
    * <li> {@link ITechStringDrawable#PRESET_CONFIG_3_SCROLL_V}  </li>
    * 
    * 
    * <p>
    * 
    * For {@link ITechStringDrawable#PRESET_CONFIG_0_NONE},
    * <li> When <i>width</i> and <i>height</i> values are <b>both positive</b>, fit and trim if no scrolling set.
    * <li>When height is <b>negative</b>, the Drawable Area must be for X lines + any ViewPane's planetaries.
    * Since String are malleable, when either width or height is Zero, drawable area will equal preferred area and no scrollbar is visible.
    * <li> When both <i>width</i> and <i>height</i> values are <b>Zero</b>, no scrollbar is created and type is overriden to
    * {@link ITechStringDrawable#PRESET_CONFIG_4_NATURAL_NO_WRAP}
    * <li>When <i>width</i> is <b>positive</b> and <i>height</i> is <b>0</b>. width is pixel set with at least one character and an infinite number of lines.
    * type is overriden to {@link ITechStringDrawable#PRESET_CONFIG_3_SCROLL_V} with infinite lines
    * <li>When <i>width</i> is <b>0</b> and <i>height</i> is <b>positive</b>, type is overriden to {@link ITechStringDrawable#PRESET_CONFIG_2_SCROLL_H} with characters
    * 
    * </p>
    * 
    * @param width drawable unchanged width raw width given to the {@link Drawable#init(int, int)} or diminished viewport width?
    * Negative width is used based on fwPrefCharWidth
    * @param height drawable height
    * 
    * <p>
    * 
    * What if the sizerH says {@link ITechLayout#SIZER_PROP_02_UNIT_LOGIC} of 2 ? SizerH tells about
    * the Drawable size! So that logic size is already computed into engine Height.
    * 
    * Here we compute the preferred size. In which case we take into
    * account the breakWidth to break a String
    * </p>
    * 
    * TODO decides which Style to use when doing the init. We want the SELECTED bold font for the char computations.
    * Since String is not editable, Stringer can relax some stuff.
    * <br>
    * This is automatic in {@link Drawable#init(int, int)} method using {@link StyleClass#getInitStyle()}
   
    * @param ds
    */
   public void initViewDrawable(LayouterEngineDrawable ds) {
      int breakWidth = ds.getW();
      int breakHeight = ds.getH();
      initPreferredSizeFromViewPortArea(ds, breakWidth, breakHeight);
      //set flags nav
      doUpdateNavBehavior();
   }

   private void initViewDrawableCharType(int width, int height) {
      if (width <= 0) {
         setDw(getStringer().getMetrics().getPrefCharWidth());
         layEngine.setPwAsDw();
      }
      if (height <= 0) {
         setDh(getStringer().getMetrics().getPrefCharHeight());
         layEngine.setPhAsDh();
      }
      setFlagView(ITechViewDrawable.FLAG_VSTATE_01_CLIP, false);
   }

   protected void initViewPortSub(int width, int height) {
      initPreferredSizeFromViewPortArea(layEngine, width, height);
   }

   /**
    * 
    * @param index
    * @return
    */
   public boolean isValidAbsoluteIndex(int index) {
      return stringer.isValidAbsoluteIndex(index);

   }

   /**
    * Forwards to the {@link StringEditModule}.
    * <br>
    */
   public void manageKeyInput(InputConfig ic) {
      toggleEdition(ic);
      if (editModule != null) {
         editModule.manageKeyInput(ic);
      }
      if (!ic.isActionDone()) {
         super.manageKeyInput(ic);
      }
   }

   public void manageNavigate(CmdInstanceDrawable cd, int navEvent) {
      if (editModule != null) {
         editModule.manageNavigate(cd, navEvent);
      }
   }

   /**
    * To finalize editing with the mouse, you have to press a Finish button.
    * <br>
    * <br>
    * 
    */
   public void managePointerInputViewPort(InputConfig ic) {
      //SystemLog.printFlow("#StringDrawable#managePointerInputViewPort Slaved=" + (ic.getSlaveDrawable() != null) + " " + this.toStringOneLine());
      if (boStringDrawable.get1(SDATA_OFFSET_06_ACTION_MODE1) == ITechStringDrawable.S_ACTION_MODE_2_EDIT) {
         //         if (editModule == null && ic.isDoubleTap) {
         //            Controller.getMe().giveControlEdit(this,null);
         //            ic.srActionDoneRepaint();
         //            return;
         //         }
      }
      if (editModule != null) {
         //take key focus anyways
         if (!hasStateStyle(ITechDrawable.STYLE_06_FOCUSED_KEY) && ic.isPressed()) {
            gc.getFocusCtrl().newFocusKey(ic, this);
         }
         editModule.managePointerInput(ic);
      }
   }

   /**
    * When in edit Mode, {@link StringEditModule} moves the caret and then updates the {@link ScrollConfig}
    * When not in edit mode, ask {@link ViewDrawable} to forward event to any {@link ViewPane}
    * <br>
    */
   public void navigateDown(InputConfig ic) {
      if (editModule != null) {
         editModule.navigateDown(ic);
      } else {
         if (viewPane != null) {
            viewPane.navigateDown(ic);
         }
      }

   }

   /**
    * 
    */
   public void navigateLeft(InputConfig ic) {
      if (editModule != null) {
         editModule.navigateLeft(ic);
      }
   }

   public void navigateRight(InputConfig ic) {
      if (editModule != null)
         editModule.navigateRight(ic);
   }

   /**
    * Automatic Edition options:
    */
   public void navigateSelect(InputConfig ic) {
      if (editModule != null)
         editModule.navigateSelect(ic);
   }

   public void navigateUp(InputConfig ic) {
      if (editModule != null)
         editModule.navigateUp(ic);
   }

   /**
    * {@link ITechDrawable#EVENT_02_NOTIFY_HIDE}
    */
   public void notifyEventHide() {
      super.notifyEventHide();
   }

   /**
    * If String is editable automatically EditModule is not null, gives itself to {@link StringEditControl}.
    * <br>
    * Creates
    */
   public void notifyEventKeyFocusGain(BusEvent e) {
      super.notifyEventKeyFocusGain(e);
      //      boolean isOldChildren = DrawableUtilz.isUpInFamily((IDrawable) e.paramO2, this);
      //      System.out.println("#StringDrawable FocusGain from Children" + isOldChildren);
      //      //check old children but also check if StringEdit control has changed its controlled.
      //      if (isOldChildren) {
      //
      //      } else {
      //         Controller.getMe().giveControlEdit(this, e);
      //      }

      //#debug
      toDLog().pFlow("FocusGain Giving Control to StringEditControl", this, StringDrawable.class, "notifyEventKeyFocusGain", LVL_05_FINE, true);
      editTakeCtrl();
   }

   /**
    * {@link ITechDrawable#EVENT_04_KEY_FOCUS_LOSS}.
    * <br>
    * Removes an {@link StringEditControl} when Focus goes to another Focus Group.
    * <br>
    * When {@link StringEditControl} gets the focus, we are still in the {@link StringDrawable} focus group.
    * <br>
    * {@link IFocusEvent#FLAG_1_LOSS}
    * <br>
    * <br>
    * Use the Focus History
    */
   public void notifyEventKeyFocusLost(BusEvent e) {
      super.notifyEventKeyFocusLost(e);
      IDrawable eventDrawable = (IDrawable) e.getParamO1();
      boolean isNewChildren = DrawableUtilz.isUpInFamily(eventDrawable, this);
      //#debug
      toDLog().pEvent("FocusLost to Children isNewChildren=" + isNewChildren, this, StringDrawable.class, "notifyEventKeyFocusLost", LVL_05_FINE, true);
      if (isNewChildren) {

      } else {
         //remove only if current
         if (gc.getStrEditCtrl().isControlling(this)) {
            editRemoveCtrl();
         }
      }
   }

   public void removeEditControl() {
      editRemoveCtrl();
   }

   /**
    * 
    * @param boStringData {@link IBOStringData}
    */
   public void setBOStringData(ByteObject boStringData) {
      styleInvalidate();
      this.boStringDrawable = boStringData;
      int presetConfig = boStringDrawable.get1(IBOStringData.SDATA_OFFSET_02_PRESET_CONFIG1);
      setTypeString(presetConfig);
   }

   public void setChar(char c) {
      setChars(new char[] { c }, 0, 1);
   }

   public void setCharAt(int i, char c) {
      stringer.setCharAt(i, c);
   }

   public void setChars(char[] c, int offset, int len) {
      stringer.setString(c, offset, len);
   }

   /**
    * Modifies the text effect
    * 
    * @param index
    * @param styleKey
    */
   public void setCharStyle(int index, int styleKey) {

   }

   /**
    * Sets the {@link StringEditModule} when the {@link StringDrawable} is edited.
    * @param module
    */
   public void setEditModule(StringEditModule module) {
      editModule = module;
      doUpdateNavBehavior();
   }

   public void setSizersPreset_1CharPerLines(int numLinesMax) {
      SizerFactory fac = getSizerFac();
      ByteObject sizerW = fac.getSizerLogicUnit(1);

      ByteObject sizerH = fac.getSizerPrefLazy();
      if (numLinesMax > 0) {
         sizerH = fac.getSizerLogicUnit(numLinesMax);
      }
      this.setSizers(sizerW, sizerH);
   }

   /**
    * 
    * @param width
    */
   public void setSizersPreset_1LineAtWidth(int width) {
      SizerFactory fac = getSizerFac();
      ByteObject sizerW = fac.getSizerPrefLazy();
      if (width > 0) {
         sizerW = fac.getSizerRaw(width);
      }
      ByteObject sizerH = fac.getSizerLogicUnit(1);
      this.setSizers(sizerW, sizerH);
   }

   public SizerFactory getSizerFac() {
      return gc.getLAC().getFactorySizer();
   }

   /**
    * One line as long as it needs to be
    */
   public void setSizersPreset_1LineLong() {
      setSizersPreset_1LineAtWidth(0);
   }

   public void setSizersPreset_Free() {
      SizerFactory fac = getSizerFac();
      ByteObject sizerPref = fac.getSizerPrefLazy();
      setSizers(sizerPref, sizerPref);
   }

   public void setSizersPreset_FreeWidthYLines(int numLinesMax) {
      SizerFactory fac = getSizerFac();
      ByteObject sizerPref = fac.getSizerPrefLazy();
      ByteObject sizerH = fac.getSizerPrefLazy();
      if (numLinesMax > 0) {
         sizerH = fac.getSizerLogicUnit(numLinesMax);
      }
      setSizers(sizerPref, sizerH);
   }

   /**
    * Trim on width, show as many lines. not scrollable
    * @param numLinesMax
    */
   public void setSizersPreset_TrimOnWidth(int width) {

   }

   /**
    * Change the string reference <b>without</b> updating the layout.
    * <br>
    * <br>
    * The {@link Stringer} is set to null which will force a layout?
    * of the whole stuff???
    * This method invalidates the layout?
    * <br>
    * <br>
    * What happens when this method is called outside the Event Thread?
    * <br>
    * <br>
    * @param str
    */
   public void setStringNoUpdate(String str) {
      if (str == null) {
         str = "";
      }
      stringer.setString(str);
      setStateFlag(ITechDrawable.STATE_32_CHANGED, true);
   }

   public void setStringNoUpdate(char[] cs, int offset, int len) {
      if (cs == null) {
         stringer.setString("");
      } else {
         stringer.setString(cs, offset, len);
      }
   }

   /**
    * 
    */
   public void setStyleClass(StyleClass sc) {
      super.setStyleClass(sc);
      initTech(null);
   }

   /**
    * Changes the way the String is constrained. Changes are applied at the next layout
    * @param type
    */
   private void setTypeString(int type) {
      setViewFlagsFromStringType(type);
      switch (type) {
         case ITechStringDrawable.PRESET_CONFIG_0_NONE:
            setShrink(false, false);
            break;
         case ITechStringDrawable.PRESET_CONFIG_1_TITLE:
            setFlagView(ITechViewDrawable.FLAG_GENE_28_NOT_SCROLLABLE, true);
            setFlagView(ITechViewDrawable.FLAG_VSTATE_12_CONTENT_W_DEPENDS_VIEWPORT, true);
            setShrink(false, true); //shrink vertically
            break;
         case ITechStringDrawable.PRESET_CONFIG_2_SCROLL_H:
            setShrink(false, true);
            setFlagView(ITechViewDrawable.FLAG_VSTATE_07_NO_EAT_H_MUST_EXPAND, true);
            break;
         case ITechStringDrawable.PRESET_CONFIG_3_SCROLL_V:
            setShrink(false, true);
            //setViewFlag(IViewDrawable.VIEWSTATE_10_CONTENT_WIDTH_FOLLOWS_VIEWPORT, true);
            setFlagView(ITechViewDrawable.FLAG_VSTATE_12_CONTENT_W_DEPENDS_VIEWPORT, true);
            break;
         case ITechStringDrawable.PRESET_CONFIG_4_NATURAL_NO_WRAP:
            setShrink(false, false);
            break;
         case ITechStringDrawable.PRESET_CONFIG_5_CHAR:
            setFlagView(ITechViewDrawable.FLAG_GENE_28_NOT_SCROLLABLE, true);
            break;
         default:
            break;
      }
   }

   /**
    * Sets flags like {@link ITechViewDrawable#FLAG_VSTATE_10_CONTENT_PW_VIEWPORT_DW}
    * @param type
    */
   private void setViewFlagsFromStringType(int type) {
      switch (type) {
         case ITechStringDrawable.PRESET_CONFIG_3_SCROLL_V:
            setFlagView(ITechViewDrawable.FLAG_VSTATE_10_CONTENT_PW_VIEWPORT_DW, true);
            break;
         default:
      }
   }

   public void setXY(int x, int y) {
      super.setXY(x, y);
      if (editModule != null) {
         editModule.set(x, y);
      }
      if (stringer != null) {
         stringer.setPosition(getContentX(), getContentY());
      }
   }

   public void initPosition() {
      super.initPosition();
      int x = getX();
      int y = getY();
      int contentW = getContentW();
      int contentH = getContentH();
      stringer.setAreaXYWH(x, y, contentW, contentH);

   }

   public void stringUpdate() {
      super.invalidateLayout();
      //layout 
      initSize();
   }

   /**
    * 
    * @param ic
    */
   private void toggleEdition(InputConfig ic) {
      if (boStringDrawable.get1(SDATA_OFFSET_06_ACTION_MODE1) == ITechStringDrawable.S_ACTION_MODE_2_EDIT) {
         //enter works with multiple lines? Yes with a short one
         if (ic.isFire() && ic.isReleased()) {
            //

            //getGuiContext().getCmd().executeMenuCmd(getCmdC().CMD_ACCEPT);

            //            //generate an event. fire maybe just going to the next elements
            //            if(getCmdCtx() != null) {
            //               //generate command in the current context
            //               
            //               getCmdCtx().getListener().commandAction();
            //            }
            //cmdToggleEdition(ic);
         }
      }
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, StringDrawable.class, 1440);
      toStringPrivate(dc);
      super.toString(dc.sup());
      IMFont f = getStyleOp().getStyleFont(style);
      dc.append(' ');
      dc.append(ToStringStaticDrawx.toStringFontBrackets(f));
      dc.nl();
      dc.nlLvl(boStringDrawable, "boStringDrawable");
      dc.nlLvl(stringer, "stringer");
      dc.nlLvl(editModule, "editModule");
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, StringDrawable.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
      dc.append("' ");
      if (styleStates != 0) {
         ToStringStaticGui.toStringBehavior(this, dc);
      }
      dc.append(" ");
      dc.append(style.getMyHashCode());
   }

   private void toStringPrivate(Dctx dc) {
      dc.append(ToStringStaticGui.debugStringType(boStringDrawable.get1(IBOStringData.SDATA_OFFSET_02_PRESET_CONFIG1)));

   }

   //#enddebug

}
