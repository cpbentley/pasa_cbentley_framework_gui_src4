package pasa.cbentley.framework.gui.src4.string;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesDrw;
import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.core.src4.i8n.IString;
import pasa.cbentley.core.src4.io.XString;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.coredraw.src4.interfaces.IMFont;
import pasa.cbentley.framework.coreui.src4.utils.ViewState;
import pasa.cbentley.framework.drawx.src4.ctx.ToStringStaticDrawx;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.string.StringMetrics;
import pasa.cbentley.framework.drawx.src4.string.Stringer;
import pasa.cbentley.framework.drawx.src4.string.interfaces.ITechStringDrw;
import pasa.cbentley.framework.drawx.src4.string.interfaces.ITechStringer;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.canvas.ViewContext;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceDrawable;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.FigDrawable;
import pasa.cbentley.framework.gui.src4.core.LayEngineDrawable;
import pasa.cbentley.framework.gui.src4.core.ScrollConfig;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.core.ViewDrawable;
import pasa.cbentley.framework.gui.src4.core.ViewPane;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IFlagsToStringGui;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.ctx.ToStringStaticGui;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStringDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawListener;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechViewDrawable;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.tech.ITechStringDrawable;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;
import pasa.cbentley.framework.gui.src4.utils.DrawableUtilz;
import pasa.cbentley.powerdata.spec.src4.power.IPowerCharCollector;

/**
 * 
 * Draws a String using : 
 * <li> char[] array
 * <li> String
 * <li>Scaled single Char using {@link IDrwTypes#TYPE_070_TEXT_EFFECTS}
 * <br>
 * <br>
 * <li> Read only {@link ITechStringDrawable#MODE_0_READ}
 * <li> chars are Selectable {@link ITechStringDrawable#MODE_1_SELECT}
 * <li> chars are editable and selectable {@link ITechStringDrawable#MODE_2_EDIT}
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
 * For ease of use presets have been defined. The default preset {@link ITechStringDrawable#TYPE_0_NONE} does nothing.
 *  Others override implicit width and height values given by 
 * {@link StringDrawable#initViewDrawable(int, int)}. The shrink behaviors are also decided by the preset. <br>
 * Concerning area constraints, the StringDrawable have preset constraint types.
 * <br>
 * <b>Preset Configurations</b> :
 * <li> {@link ITechStringDrawable#TYPE_0_NONE}. No default shrink behavior. Take into account implicit width and height values.
 * <ol>
 * <li>When both height and width is 0, behaves like {@link ITechStringDrawable#TYPE_4_NATURAL_NO_WRAP} but without scrolling.
 * Preferred size being equal to drawn size.
 * <li>When height is 0. Behaves like {@link ITechStringDrawable#TYPE_3_SCROLL_V} without the shrinkH and no scrolling.
 * <li>When width is 0, dw = pw.
 * </ol>
 * <li> {@link ITechStringDrawable#TYPE_1_TITLE}. One line of trimmed at width text. No Shrinking. Width and Height decides area.
 * <li> {@link ITechStringDrawable#TYPE_3_SCROLL_V}. X lines of text constrained at width. Shrink to fit as many lines as possible
 * allowed by height. Take into account negative init for the number of visual lines to be seen in the ViewPort
 * <li> {@link ITechStringDrawable#TYPE_2_SCROLL_H}. Horizontal scrolling.
 * <li> {@link ITechStringDrawable#TYPE_4_NATURAL_NO_WRAP}
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
 * See {@link EditModule}.
 * <br>
 * <br>
 * <b>String localization</b>
 * State localized:
 * Key to real string
 * <br>
 * <br>
 * 
 * @see IString
 * <br>
 * <br>
 * @author Charles-Philip Bentley
 * @see {@link ViewDrawable}
 * 
 *
 */
public class StringDrawable extends ViewDrawable implements IBOStringDrawable, IDrawListener {

   public static final int STR_VIEWSTATE_STR = 10;

   public static StringDrawable[] ensureCapacity(StringDrawable[] ar, int index) {
      if (index < ar.length)
         return ar;
      StringDrawable[] nar = new StringDrawable[index + 1];
      for (int i = 0; i < ar.length; i++) {
         nar[i] = ar[i];
      }
      return nar;
   }

   /**
    * Decides which breaking method to use according to context {@link IBOStringDrawable#INPUT_OFFSET_02_STRING_TYPE1}
    * <br>
    * <br>
    * <li>{@link ITechStringDrawable#TYPE_0_NONE}
    * <li>{@link ITechStringDrawable#TYPE_1_TITLE} of 1 or 2 characters are never trimmed.
    * <br>
    * Logical width prevents the following break types:
    * <li> {@link ITechStringDrw#BREAK_4_TRIM_SINGLE_LINE}
    * <br>
    * <br>
    * @param width
    * @param height
    * @param type {@link ITechStringDrawable#TYPE_0_NONE} ,  {@link ITechStringDrawable#TYPE_1_TITLE}, ...
    * @return
    */
   public static int getBreakType(int width, int height, int type, int len, Stringer st) {
      int breakType = ITechStringDrw.BREAK_0_NONE;
      switch (type) {
         case ITechStringDrawable.TYPE_0_NONE:
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
         case ITechStringDrawable.TYPE_2_SCROLL_H:
            breakType = ITechStringDrw.BREAK_3_ONE_LINE;
            break;
         case ITechStringDrawable.TYPE_1_TITLE:
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
         case ITechStringDrawable.TYPE_4_NATURAL_NO_WRAP:
            breakType = ITechStringDrw.BREAK_2_NATURAL;
            break;
         case ITechStringDrawable.TYPE_3_SCROLL_V:
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

   public static int getMaxLines(int width, int height, int type) {
      int maxLines = -1;
      switch (type) {
         case ITechStringDrawable.TYPE_0_NONE:
            if (width > 0) {
               if (height == 0) {
               } else if (height < 0) {
                  int numLines = 0 - height;
                  maxLines = numLines;
               } else {
               }
            } else {
               if (width == 0) {
               }
            }
            break;
         case ITechStringDrawable.TYPE_2_SCROLL_H:
            break;
         case ITechStringDrawable.TYPE_1_TITLE:
            maxLines = 1;
            break;
         case ITechStringDrawable.TYPE_4_NATURAL_NO_WRAP:
            break;
         case ITechStringDrawable.TYPE_3_SCROLL_V:
            break;
         default:
            throw new IllegalArgumentException();
      }
      return maxLines;
   }



   /**
    * Whenever edition occurs, a new array is created, edition will never occurs in the source array
    * 
    */
   char[]          chars;

   /**
    * Controls the Input of characters.
    * <br>
    * {@link EditModule} calls back {@link StringDrawable} with
    * <li> {@link StringDrawable#deleteCharAt(int)}
    * <li> {@link StringDrawable#addChar(int, char)}
    * <br>
    * <br>
    * Set external by {@link StringEditControl} using {@link StringDrawable#setEditModule(EditModule)}.
    * <br>
    */
   EditModule      editModule;

   /**
    * 
    */
   int             len;

   /**
    * When offset is different than zero, it means the String data is taken from a read only. Unless the special 
    * flag {@link IBOStringDrawable#INPUT_FLAG_6_KEEP_ARRAY} is set. in which case, the array reference
    * is never changed and neither the offset.
    */
   int             offset;

   /**
    * Everytime, it is drawn, The Stringer must be updated if the {@link IString}
    * content has changed.
    */
   private IString str;

   /**
    * Valid until invalidate by new string or init method
    */
   Stringer        stringer;

   /**
    * 
    */
   //int           typeStr;

   public StringDrawable(GuiCtx gc, StyleClass sc, char[] str, int offset, int len) {
      super(gc, sc);
      if (str == null)
         throw new NullPointerException("null string");
      this.chars = str;
      this.offset = offset;
      this.len = len;
      initTech();
   }

   public StringDrawable(GuiCtx gc, StyleClass sc, IPowerCharCollector charco) {
      super(gc, sc);

   }

   public StringDrawable(GuiCtx gc, StyleClass sc, IString str) {
      super(gc, sc);
      if (str == null)
         throw new NullPointerException("null string");
      this.str = str;
      initTech();
   }

   /**
    * Creates a {@link StringDrawable} to display the String using the given style.
    * <br>
    * <br>
    * The {@link IBOStringDrawable#INPUT_OFFSET_02_STRING_TYPE1} decides the breaking during
    * initialization.
    * 
    * @param sc
    * @param str
    */
   public StringDrawable(GuiCtx gc, StyleClass sc, String str) {
      this(gc, sc, str.toCharArray(), 0, str.length());
   }

   /**
    * Overrides the Tech from the style class
    * <br>
    * <br>
    * 
    * @param sc
    * @param str
    * @param tech
    */
   public StringDrawable(GuiCtx gc, StyleClass sc, String str, ByteObject tech) {
      super(gc, sc);
      if (str == null) {
         str = "";
      }
      setStringNoUpdate(str);
      if (tech != null) {
         techDrawable = tech;
      } else {
         techDrawable = styleClass.getByteObject(IBOTypesGui.LINK_41_TECH_STRING);
         if (techDrawable == null) {
            techDrawable = gc.getDrawableStringFactory().getStringTech(ITechStringDrawable.TYPE_1_TITLE);
         }
      }
      setTypeString(techDrawable.get1(IBOStringDrawable.INPUT_OFFSET_02_STRING_TYPE1));
   }

   /**
    * Deprecated method that create a StringTech for the given type.
    * 
    * 
    * @param str
    * @param st
    * @param type Predefined type hardcode type from constructor.
    * It is too cumbersome to create a style class just for a type variation
    * But it was designed for that
    */
   public StringDrawable(GuiCtx gc, StyleClass sc, String str, int type) {
      super(gc, sc);
      if (str == null) {
         str = "";
         //throw new NullPointerException();
      }
      setStringNoUpdate(str);
      techDrawable = styleClass.getByteObject(IBOTypesGui.LINK_41_TECH_STRING);
      if (techDrawable == null) {
         techDrawable = gc.getDrawableStringFactory().getStringTech(type);
      }
      if (type != -1) {
         techDrawable = techDrawable.cloneCopyHead();
         techDrawable.set1(IBOStringDrawable.INPUT_OFFSET_02_STRING_TYPE1, type);
      }
      type = techDrawable.get1(IBOStringDrawable.INPUT_OFFSET_02_STRING_TYPE1);
      setTypeString(type);
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
      chars = gc.getUCtx().getMem().ensureCapacity(chars, len + 1, 1);
      for (int i = len - 1; i >= indexRelative; i--) {
         chars[i + 1] = chars[i];
      }
      chars[indexRelative] = c;
      len++;
      getStringer().getEditor().addChar(chars, indexRelative, c);
      stringUpdate();
   }

   public synchronized void addChars(int indexRelative, char[] chars) {

   }

   /**
    * 
    * When receiving key focus {@link ITechDrawable#EVENT_03_KEY_FOCUS_GAIN}  and flag {@link IBOStringDrawable#INPUT_FLAG_5_AUTO_EDIT_MODE} is set,
    * {@link StringDrawable} toggles on Edition mode.
    * <br>
    * When in mode {@link ITechStringDrawable#TYPE_1_TITLE}, type is temporarily set to {@link ITechStringDrawable#TYPE_3_SCROLL_V}
    * or {@link ITechStringDrawable#TYPE_2_SCROLL_H} using the given size at the time the switch is made.
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

   /**
    * 
    * @param indexRelative
    */
   public synchronized void deleteCharAt(int indexRelative) {
      int start = offset + indexRelative;
      for (int i = start; i < len - 1; i++) {
         chars[i] = chars[i + 1];
      }
      len--;
      getStringer().deleteCharAt(chars, indexRelative);
      stringUpdate();

   }

   public synchronized void deleteCharsAt(int indexRelative, int len) {

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
      isSelect = (techDrawable.get1(INPUT_OFFSET_06_MODE1) == ITechStringDrawable.MODE_2_EDIT) ? true : false;
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
      if (hasViewFlag(ITechViewDrawable.VIEWSTATE_01_CLIP)) {
         //g.clipSet(getContentX(), getContentY(), getContentW(), getContentH());
      }
      //DrawableUtilz.debugScrollConfigs("StringDrawable.drawContent", scX, scY);
      //decides the number of lines to draw
      int hNum = getNumOfLines();
      int hOffset = 0;
      int wNum = len;
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
               wNum = len - wOffset;
               break;
            default:
               throw new IllegalArgumentException();
         }
      }
      if (scY != null) {
         //System.out.println(scY.toString());
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
      getStringer().setAreaWH(getContentW(), getContentH());
      ByteObject styleAnchor = getStyleOp().getStyleAnchor(style);
      getStringer().setAnchor(styleAnchor);
      getStringer().drawOffsets(g, x, y, wOffset, wNum, hOffset, hNum);
      //System.out.println(getStringer());

      if (editModule != null) {
         //ask the module to draw the caret and other active items such as the drop down choices
         editModule.draw(g);
      }

      //check to reset clip area.
      if (hasViewFlag(ITechViewDrawable.VIEWSTATE_01_CLIP)) {
         // g.clipReset();
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


   /**
    * From type of display String, compute and override drawable width <b>dw</b> and drawable height <b>dh</b>.
    * <br>
    * Negative values with variable units size: <br>
    * <li>Take the biggest of the units.
    * <li>
    * <br>
    * Set {@link ITechViewDrawable#VIEWSTATE_06_NO_EAT_W_MUST_EXPAND} when width <= 0. 
    * <br>
    * Set {@link ITechViewDrawable#VIEWSTATE_07_NO_EAT_H_MUST_EXPAND} when height <= 0.
    *  <br>
    * <br>
    * <br>
    * @param width
    * @param height
    */
   //   protected void setDwDh(int width, int height) {
   //      int typeStr = techDrawable.get1(IStringDrawable.INPUT_OFFSET_02_STRING_TYPE1);
   //      switch (typeStr) {
   //         case IStringDrawable.TYPE_0_NONE:
   //            super.setDwDh(width, height);
   //            break;
   //         case IStringDrawable.TYPE_2_SCROLL_H:
   //            super.setDwDh(width, -1);
   //            break;
   //         case IStringDrawable.TYPE_1_TITLE:
   //            //ignore h
   //            super.setDwDh(width, 1);
   //            break;
   //         case IStringDrawable.TYPE_4_NATURAL_NO_WRAP:
   //            super.setDwDh(width, height);
   //            break;
   //         case IStringDrawable.TYPE_3_SCROLL_V:
   //            super.setDwDh(width, height);
   //            break;
   //         default:
   //            throw new IllegalArgumentException();
   //
   //      }
   //   }

   protected int getDesiredVisualWidth(int w) {
      int typeStr = techDrawable.get1(IBOStringDrawable.INPUT_OFFSET_02_STRING_TYPE1);
      switch (typeStr) {
         case ITechStringDrawable.TYPE_0_NONE:
            return w;
         case ITechStringDrawable.TYPE_2_SCROLL_H:
            return w;
         case ITechStringDrawable.TYPE_1_TITLE:
            return w;
         case ITechStringDrawable.TYPE_4_NATURAL_NO_WRAP:
            return w;
         case ITechStringDrawable.TYPE_3_SCROLL_V:
            return w;
         default:
            throw new IllegalArgumentException();

      }
   }

   /**
    * 
    * @return {@link EditModule} initialized with this {@link StringDrawable}.
    */
   public EditModule getEditModule() {
      return editModule;
   }

   public int getLen() {
      return len;
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
    * As currently initialized what is the number of lines used to draw the String?
    * Default is 1
    * @return
    */
   public int getNumOfLines() {
      return stringer.getNumOfLines();
   }

   public int getOffset() {
      return offset;
   }

   /**
    * String copy of the char array with offset and length.
    * <br.
    * @return non null
    */
   public String getString() {
      return new String(chars, offset, len);
   }

   /**
    * copies the techdrawable.
    * Invalidates the layout
    * @param type
    */
   //   public void setStringType(int type) {
   //      techDrawable = techDrawable.cloneCopyHeadRefParams();
   //      techDrawable.set1(IStringDrawable.INPUT_OFFSET_02_STRING_TYPE1, type);
   //      invalidateLayout();
   //   }

   //#enddebug
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
      if (stringer != null) {
         return stringer.getDisplayedString();
      }
      return new String(chars, offset, len);
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
      ByteObject strStyle = getStringerStyle();
      if (stringer == null) {
         stringer = new Stringer(gc.getDC());
         if (chars == null) {
            setStringNoUpdate("nullChars");
         }
         //old issue.. when x,y are modified afterwards? stringer needs to be updated
         stringer.setAreaXYWH(getContentX(), getContentY(), getContentW(), getContentH());
         stringer.setStringFig(strStyle, chars, offset, len);
      } else {
         //check identity if not matchin, re-initialize Stringer.
         stringer.setTextObjectArea(strStyle, getContentX(), getContentY(), getContentW(), getContentH());
      }
      return stringer;
   }

   /**
    * Gets the {@link ByteObject} style to be used for computing {@link StringMetrics} in {@link Stringer}.
    * <br>
    * <br>
    * @return cannot be null and must be {@link IBOTypesDrw#TYPE_050_FIGURE}
    */
   private ByteObject getStringerStyle() {
      int initStyle = techDrawable.get4(INPUT_OFFSET_07_INIT_STYLE_FLAGS4);
      ByteObject strStyle = null;
      //System.out.println("#StringDrawable InitStyle " + initStyle + " " + new String(chars, offset, Math.min(len, 15)) + " " + toStringDimension());
      if (initStyle == 0) {
         strStyle = getStyleOp().getContentStyle(style);
      } else {
         strStyle = getStyleOp().getContentStyle(styleClass.getStyle(initStyle, ctype, structStyle));
      }
      if (strStyle == null) {
         //#debug
         toDLog().pNull("Style could not be Found", this, StringDrawable.class, "getStringerStyle", LVL_09_WARNING, true);
         strStyle = gc.getStyleManager().getDefaultContentStyle();
      }
      //#mdebug
      strStyle.checkType(IBOTypesDrw.TYPE_050_FIGURE);
      //#enddebug
      return strStyle;
   }

   /**
    * From tech gets {@link IBOStringDrawable#INPUT_OFFSET_02_STRING_TYPE1}
    * <br>
    * <li> {@link ITechStringDrawable#TYPE_1_TITLE}
    * <li> {@link ITechStringDrawable#TYPE_3_SCROLL_V}
    * <li> {@link ITechStringDrawable#TYPE_2_SCROLL_H}
    * <li> {@link ITechStringDrawable#TYPE_1_TITLE}
    * <li> {@link ITechStringDrawable#TYPE_1_TITLE}
    * 
    * @return
    */
   public int getStringType() {
      return techDrawable.get1(IBOStringDrawable.INPUT_OFFSET_02_STRING_TYPE1);
   }

   /**
    * {@link XString} of the char array with offset and length.
    * <br>
    * Modifying has an impact
    * <br>
    * @return non null
    */
   public XString getStringX() {
      return new XString(gc.getUCtx(), chars, offset, len);
   }

   public ByteObject getTechDrawable() {
      return techDrawable;
   }

   /**
    * Saves string when it is user mutable. Otherwise the string is code bound.
    */
   public ViewState getViewState() {
      ViewState vs = super.getViewState();

      //only save string when string is mutable, i.e 
      if (techDrawable.get1(INPUT_OFFSET_06_MODE1) == ITechStringDrawable.MODE_2_EDIT) {
         vs.itos.add(getString(), STR_VIEWSTATE_STR);
      }
      return vs;
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
            scX.initConfigLogic(incrementPixelSize, getContentW(), len);
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
   protected void initTech() {
      techDrawable = styleClass.getByteObject(IBOTypesGui.LINK_41_TECH_STRING);
      if (techDrawable == null) {
         techDrawable = gc.getDrawableStringFactory().getDefaultStringTech();
      }
      int type = techDrawable.get1(IBOStringDrawable.INPUT_OFFSET_02_STRING_TYPE1);
      setTypeString(type);
   }

   /**
    * Compute preferred size based on String type and sizers.
    * <br>
    * For example {@link ITechStringDrawable#TYPE_3_SCROLL_V} will take preferred size 
    * from {@link ViewContext}.
    * <br>
    * What is sizerH says {@link ISizer#ET_TYPE_1_LOGIC_SIZE} of -2 ? SizerH tells about
    * the Drawable size! Here we compute the preferred size. In which case we take into
    * account the breakWidth to break a String
    * <br>
    * @param ds
    */
   public void initViewDrawable(LayEngineDrawable ds) {
      //nullify stringer to force reinitialization
      Stringer initStringer = getStringer();
      StringMetrics metrics = initStringer.getMetrics();

      int breakWidth = ds.getW();
      int breakHeight = ds.getH();

      //#debug
      toDLog().pInit("breakWidth=" + breakWidth + " breakHeight=" + breakHeight, this, StringDrawable.class, "initViewDrawable", LVL_05_FINE, true);

      initBreak(breakWidth, breakHeight);
      //use the content area to size ourselves
      //set view flag that preferred size has been computed
      //set flags nav
      doUpdateNavBehavior();
   }

   public StringMetrics getStringMetrics() {
      Stringer initStringer = getStringer();
      StringMetrics metrics = initStringer.getMetrics();
      return metrics;
   }

   /**
    * Overrides
    */
   public int getSizePreferredHeight() {
      //TODO
      if (hasViewFlag(VIEWSTATE_08_PREF_SIZE_COMPUTED)) {
         return ph;
      } else {
         int strType = getStringType();
         if (strType == ITechStringDrawable.TYPE_1_TITLE) {
            //a preferred title H is 1 line
            return getStringMetrics().getPrefCharHeight();
            //logic 2 is 2 lines
         }
         //when no computed.. it was called by init method
         return vc.getHeight();
      }
   }

   private void initBreak(int breakWidth, int breakHeight) {
      Stringer initStringer = getStringer();
      StringMetrics metrics = initStringer.getMetrics();
      int typeStr = getStringType();
      if (typeStr == ITechStringDrawable.TYPE_5_CHAR) {
         initViewDrawableCharType(breakWidth, breakHeight);
      } else {
         //----------------------------
         //compute breaks, preferred dimension given dim parameters and type
         //#debug
         toDLog().pInit("", initStringer, StringDrawable.class, "initBreak", LVL_05_FINE, false);

         int breakType = getBreakType(breakWidth, breakHeight, typeStr, len, initStringer);
         int maxLines = getMaxLines(breakWidth, breakHeight, typeStr);
         initStringer.setBreakWH(breakWidth, breakHeight);
         initStringer.setBreakMaxLines(maxLines);
         initStringer.buildAgain();
         pw = metrics.getPrefWidth();
         ph = metrics.getPrefHeight();
         //

         //clipping is only necessary when partial strings are drawn.
         if ((hasViewFlag(VIEWSTATE_22_SCROLLED))) {
            if (viewPane != null && viewPane.isContentClipped()) {
               //TODO is this possible?
               //clipping is also needed when init style is smaller than some state styles?
               setViewFlag(ITechViewDrawable.VIEWSTATE_01_CLIP, true);
            } else {
               setViewFlag(ITechViewDrawable.VIEWSTATE_01_CLIP, false);
            }
         } else {
            setViewFlag(ITechViewDrawable.VIEWSTATE_01_CLIP, false);
         }
      }
   }

   /**
    * The init method of Drawable has already been called by {@link ViewDrawable}. <br>
    * <br>
    * <p>
    * The {@link IBOStringDrawable#INPUT_OFFSET_02_STRING_TYPE1} applies to decide how the preferred dimension is computed.
    * <li> {@link ITechStringDrawable#TYPE_2_SCROLL_H} gives all text on 1 line.  </li>
    * <li> {@link ITechStringDrawable#TYPE_4_NATURAL_NO_WRAP}  </li>
    * <li> {@link ITechStringDrawable#TYPE_1_TITLE} text is trimmed to fit area </li>
    * <li> {@link ITechStringDrawable#TYPE_3_SCROLL_V}  </li>
    * <br>
    * <br>
    * For {@link ITechStringDrawable#TYPE_0_NONE},
    * <li> When <i>width</i> and <i>height</i> values are <b>both positive</b>, fit and trim if no scrolling set.
    * <li>When height is <b>negative</b>, the Drawable Area must be for X lines + any ViewPane's planetaries.
    * <br>
    * Since String are malleable, when either width or height is Zero, drawable area will equal preferred area and no scrollbar is visible.
    * <li> When both <i>width</i> and <i>height</i> values are <b>Zero</b>, no scrollbar is created and type is overriden to
    * {@link ITechStringDrawable#TYPE_4_NATURAL_NO_WRAP}
    * <li>When <i>width</i> is <b>positive</b> and <i>height</i> is <b>0</b>. width is pixel set with at least one character and an infinite number of lines.
    * type is overriden to {@link ITechStringDrawable#TYPE_3_SCROLL_V} with infinite lines
    * <li>When <i>width</i> is <b>0</b> and <i>height</i> is <b>positive</b>, type is overriden to {@link ITechStringDrawable#TYPE_2_SCROLL_H} with characters
    * <br>
    * <br>
    * @param width drawable unchanged width raw width given to the {@link Drawable#init(int, int)} or diminished viewport width?
    * Negative width is used based on fwPrefCharWidth
    * @param height drawable height
    * 
    * 
    * TODO decides which Style to use when doing the init. We want the SELECTED bold font for the char computations.
    * Since String is not editable, Stringer can relax some stuff.
    * <br>
    * This is automatic in {@link Drawable#init(int, int)} method using {@link StyleClass#getInitStyle()}
    */
   public void initViewDrawable(int width, int height) {
      //nullify stringer to force reinitialization
      stringer = null;
      int typeStr = getStringType();
      if (typeStr == ITechStringDrawable.TYPE_5_CHAR) {
         initViewDrawableCharType(width, height);
      } else {
         Stringer initStringer = getStringer();
         StringMetrics metrics = initStringer.getMetrics();
         int breakWidth = width;
         if (breakWidth > 0) {
            breakWidth = getContentW();
         } else if (breakWidth < 0) {
            breakWidth = -breakWidth * metrics.getPrefCharWidth();
         }
         int breakHeight = height;
         if (breakHeight > 0) {
            breakHeight = getContentH();
         } else if (breakHeight < 0) {
            breakHeight = -breakHeight * metrics.getPrefCharHeight();
         }
         setViewFlags(typeStr);

         //----------------------------
         //compute breaks, preferred dimension given dim parameters and type
         getBreakType(breakWidth, breakHeight, typeStr, len, initStringer);
         int maxLines = getMaxLines(breakWidth, breakHeight, typeStr);
         initStringer.setBreakWH(breakWidth, breakHeight);
         initStringer.setBreakMaxLines(maxLines);
         initStringer.buildAgain();
         pw = metrics.getPrefWidth();
         ph = metrics.getPrefHeight();
         super.addStyleToPrefSize();
         //----------------------------

         //         if (hasBehavior(IDrawable.BEHAVIOR_02_EMPTY) && (pw == 0 || ph == 0)) {
         //            System.out.println(this);
         //            System.out.println("pw=" + pw + " ph=" + ph);
         //            throw new IllegalStateException();
         //         }
         //setDwDh(width, height); string type overrides sizer
         //clipping is only necessary when partial strings are drawn.
         if ((hasViewFlag(VIEWSTATE_22_SCROLLED))) {
            if (viewPane != null && viewPane.isContentClipped()) {
               //TODO is this possible?
               //clipping is also needed when init style is smaller than some state styles?
               setViewFlag(ITechViewDrawable.VIEWSTATE_01_CLIP, true);
            } else {
               setViewFlag(ITechViewDrawable.VIEWSTATE_01_CLIP, false);
            }
         } else {
            setViewFlag(ITechViewDrawable.VIEWSTATE_01_CLIP, false);
         }

         //set flags nav
         doUpdateNavBehavior();

      }

   }

   private void initViewDrawableCharType(int width, int height) {
      if (width <= 0) {
         setDw(getStringer().getMetrics().getPrefCharWidth());
         pw = getDw();
      }
      if (height <= 0) {
         setDh(getStringer().getMetrics().getPrefCharHeight());
         ph = getDh();
      }
      setViewFlag(ITechViewDrawable.VIEWSTATE_01_CLIP, false);
   }

   /**
    * Called when a ViewPort is created or changed
    */
   protected void initViewPortSub(int width, int height) {
      initBreak(width, height);
   }

   /**
    * 
    * @param index
    * @return
    */
   public boolean isValidAbsoluteIndex(int index) {
      return index >= offset && index < offset + len;
   }

   /**
    * Forwards to the {@link EditModule}.
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

   /**
    * To finalize editing with the mouse, you have to press a Finish button.
    * <br>
    * <br>
    * 
    */
   public void managePointerInputViewPort(InputConfig ic) {
      //SystemLog.printFlow("#StringDrawable#managePointerInputViewPort Slaved=" + (ic.getSlaveDrawable() != null) + " " + this.toStringOneLine());
      if (techDrawable.get1(INPUT_OFFSET_06_MODE1) == ITechStringDrawable.MODE_2_EDIT) {
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
    * When in edit Mode, {@link EditModule} moves the caret and then updates the {@link ScrollConfig}
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

   public void manageNavigate(CmdInstanceDrawable cd, int navEvent) {
      if (editModule != null) {
         editModule.manageNavigate(cd, navEvent);
      }
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

   public void setChar(char c) {
      setChars(new char[] { c }, 0, 1);
   }

   public void setCharAt(int i, char c) {
      chars[i] = c;
      getStringer().setCharAt(i, c);
   }

   public void setChars(char[] c, int offset, int len) {
      this.chars = c;
      this.offset = offset;
      this.len = len;
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
    * Sets the {@link EditModule} when the {@link StringDrawable} is edited.
    * @param module
    */
   public void setEditModule(EditModule module) {
      editModule = module;
      doUpdateNavBehavior();
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
      if (str == null)
         throw new NullPointerException();
      stringer = null;
      this.chars = str.toCharArray();
      this.offset = 0;
      this.len = str.length();
      //update Stringer?
      setStateFlag(ITechDrawable.STATE_32_CHANGED, true);
   }

   /**
    * 
    */
   public void setStyleClass(StyleClass sc) {
      super.setStyleClass(sc);
      initTech();
   }

   /**
    * Un style the {@link Drawable}/
    * @param t
    */
   public void setTechDrawable(ByteObject t) {
      styleInvalidate();
      techDrawable = t;
   }

   /**
    * Changes the way the String is constrainted. Changes are applied at the next layout
    * @param type
    */
   private void setTypeString(int type) {
      setViewFlags(type);
      switch (type) {
         case ITechStringDrawable.TYPE_0_NONE:
            setShrink(false, false);
            break;
         case ITechStringDrawable.TYPE_1_TITLE:
            setViewFlag(ITechViewDrawable.VIEW_GENE_28_NOT_SCROLLABLE, true);
            setViewFlag(ITechViewDrawable.VIEWSTATE_12_CONTENT_W_DEPENDS_VIEWPORT, true);
            setShrink(false, true); //shrink vertically
            break;
         case ITechStringDrawable.TYPE_2_SCROLL_H:
            setShrink(false, true);
            setViewFlag(ITechViewDrawable.VIEWSTATE_07_NO_EAT_H_MUST_EXPAND, true);
            break;
         case ITechStringDrawable.TYPE_3_SCROLL_V:
            setShrink(false, true);
            //setViewFlag(IViewDrawable.VIEWSTATE_10_CONTENT_WIDTH_FOLLOWS_VIEWPORT, true);
            setViewFlag(ITechViewDrawable.VIEWSTATE_12_CONTENT_W_DEPENDS_VIEWPORT, true);
            break;
         case ITechStringDrawable.TYPE_4_NATURAL_NO_WRAP:
            setShrink(false, false);
            break;
         case ITechStringDrawable.TYPE_5_CHAR:
            setViewFlag(ITechViewDrawable.VIEW_GENE_28_NOT_SCROLLABLE, true);
            break;
         default:
            break;
      }
   }

   /**
    * Sets flags like {@link ITechViewDrawable#VIEWSTATE_10_CONTENT_PW_VIEWPORT_DW}
    * @param type
    */
   public void setViewFlags(int type) {
      switch (type) {
         case ITechStringDrawable.TYPE_3_SCROLL_V:
            setViewFlag(ITechViewDrawable.VIEWSTATE_10_CONTENT_PW_VIEWPORT_DW, true);
            break;
         default:
      }
   }

   /**
    * When setting viewstate, the element with focus, takes the focus.
    * 
    */
   public void setViewState(ViewState vs) {
      super.setViewState(vs);
      if (techDrawable.get1(INPUT_OFFSET_06_MODE1) == ITechStringDrawable.MODE_2_EDIT) {
         String str = (String) vs.itos.findIntObject(STR_VIEWSTATE_STR);
         if (str != null) {
            chars = str.toCharArray();
            len = str.length();
            offset = 0;
         }
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

   public void shiftXY(int dx, int dy) {
      setXY(getX() + dx, getY() + dy);
   }

   public void stringUpdate() {
      this.layoutInvalidate(false);
      //layout 
      init();
   }

   /**
    * 
    * @param ic
    */
   private void toggleEdition(InputConfig ic) {
      if (techDrawable.get1(INPUT_OFFSET_06_MODE1) == ITechStringDrawable.MODE_2_EDIT) {
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

   public void toString(Dctx sb) {
      sb.root(this, "StringDrawable");
      sb.append(ToStringStaticGui.debugStringType(techDrawable.get1(IBOStringDrawable.INPUT_OFFSET_02_STRING_TYPE1)));
      IMFont f = getStyleOp().getStyleFont(style);
      sb.append(' ');
      sb.append(ToStringStaticDrawx.debugFontBrackets(f));
      sb.nl();
      String str = new String(chars, offset, len);
      sb.append(str);
      sb.append(" len=" + str.length() + "");
      //--------
      //---
      sb.nl();
      gc.getDrawableStringOperator().toStringTech(techDrawable, sb.newLevel());

      if (sb.hasFlagData(gc, IFlagsToStringGui.D_FLAG_03_STRINGER) && stringer != null) {
         sb.nlLvl(stringer);
      }
      if (editModule != null) {
         sb.nlLvl(editModule);
      }
      super.toString(sb.sup());
   }

   public void toString1Line(Dctx sb) {
      sb.root1Line(this, "StringDrawable");
      sb.append("'");
      sb.append(new String(chars, offset, Math.min(len, 15)));
      sb.append("' ");
      if (styleStates != 0) {
         ToStringStaticGui.toStringBehavior(this, sb);
      }
      sb.append(" ");
      sb.append(style.getMyHashCode());
   }

}
