package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.utils.BitUtils;
import pasa.cbentley.core.src4.utils.IntUtils;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.engine.RgbImage;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ObjectGC;
import pasa.cbentley.framework.gui.src4.ctx.ToStringStaticGui;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOScrollBar;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOViewPane;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;

/**
 * Encapsulates data describing a scrolling state and behavior. Links {@link ViewDrawable} to a {@link ScrollBar}. 
 * 
 * <p>
 * 
 * An implementation does not have to implement anything. Links {@link ViewDrawable} via
 * 
 * </p>
 * <li>{@link ViewDrawable#initScrollingConfig(ScrollConfig, ScrollConfig)}
 * <li>{@link ViewDrawable#drawViewDrawableContent(GraphicsX, int, int, ScrollConfig, ScrollConfig)} 
 * <br>
 * <br>
 * An implementation that has its own internal navigation maybe duplicate/synchronize the values in this class.
 * <br>
 * <br>
 * Another process may create a new empty {@link ScrollConfig} by modifying variables.
 * <br>
 * Scrolling animations call with a visible increment that suit their needs. 
 * <br>
 * They draw on a {@link RgbImage} through {@link ViewDrawable#getViewDrawableContent(ScrollConfig, ScrollConfig)}. 
 * <br>
 * It then calls draw method.
 * <br>
 * <br>
 * <b>Life cycle </b>within the {@link ViewPane} context. <br>
 * <li>Created alongside its scrollbar.
 * <li>Init by {@link ViewDrawable#initScrollingConfig(ScrollConfig, ScrollConfig)}
 * <li>Used to draw {@link ScrollBar}
 * <li> Modified by {@link ScrollConfig#move(int)} by event manager of {@link ViewPane} 
 * <li>Repaint of {@link ScrollBar} and {@link ViewDrawable}.
 * <br>
 * 
 * <br>
 * <b>State Flags</b> <br>
 * When a {@link ScrollConfig} has been populated, a flag is set
 * 
 * <br>
 * <br>
 * Unit is the <b>increment</b> :
 * <li>{@link ITechViewPane#SCROLL_TYPE_0_PIXEL_UNIT}. Usually pixels for {@link ImageDrawable}
 * <li>{@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT}. Lines for {@link StringDrawable}, Cells for {@link TableView}.
 * <li>{@link ITechViewPane#SCROLL_TYPE_2_PAGE_UNIT}. Logic Unit becomes Page Visible
 * <br>
 * <br>
 * It is decided at the level of the {@link ViewPane}.
 * <br>
 * <br>
 * <b>Scrolling Increments</b> (SI) :
 * <li>total: number of units (pixels, number of lines). Provided by {@link ViewDrawable}.
 * <li>visible : number of visible increments in the ViewPort. Provided by {@link ViewDrawable}.
 * <li>pixel size: the number of pixel needed to draw an increment. Provided by {@link ViewDrawable}.
 * <li>move : Decided by {@link IBOScrollBar#SB_OFFSET_07_SCROLL_TYPES1}.
 * <li>direction : direction of scrolling/slide. Modified by {@link IDrawable} events.
 * <li>start : first shown increment. Modified by {@link IDrawable} events.
 * <br>
 * <br>
 * Variable pixel size increments implies the {@link ScrollConfig} has to be updated.
 * <br>
 * <br>
 * <b>World Applications Examples</b>:<br>
 * <li><font color=#FFCC00"> <b>Windows Explorer</b></font> : the first visible increment (top) is always fully visible. A blank space is drawn when last
 * increment is reached and no partially visible unit is shown. rootSI is always startSI.
 * <li><font color=#FFCC00"> <b>Eclipse Package Explorer</b> </font>: Partial is always on. Selected is last fully visible file. Otherwise same behavior
 * as Windows Explorer when reaching the last partially visible element.
 * <br>
 * <br>
 * <b>Animations</b>: <br>
 * <li>provides method to compute the number of pixels needed for drawing given increments.
 * <br>
 * <br>
 * In Swing, this class is java.awt.Adjustable.
 * <br>
 * <br>
 * @author Charles-Philip Bentley
 * @see ScrollBar
 * @see ViewPane
 *
 */
public class ScrollConfig extends ObjectGC implements IStringable {

   public static final int FLAG_1_UPDATED           = 1;

   /**
    * Decides the root
    */
   public static final int FLAG_3_TOP_LEFT_DIR      = 4;

   /**
    * Only relevant in Logic unit mode.
    */
   public static final int FLAG_4_PARTIAL_REMOVED   = 8;

   /**
    * when set, partial increment is always bottom/right
    */
   public static final int FLAG_7_ALWAYS_TOP        = 64;

   /**
    * when set, the partial increment is always top/left
    */
   public static final int FLAG_8_ALWAYS_BOTTOM     = 128;

   public static final int STATE_0_START            = 0;

   public static final int STATE_1_JUST_AFTER_START = 1;

   public static final int STATE_2_MIDDLE           = 2;

   public static final int STATE_3_END              = 3;

   public static final int STATE_4_JUST_BEFORE_END  = 4;


   /**
    * When siSize is zero, return zero.
    * <br>
    * @param sizeTotal
    * @param siSize size of a scrolling elemnt
    * @param sepSize
    * @return
    */
   public static int getNumInside(int sizeTotal, int siSize, int sepSize) {
      if(siSize == 0) {
         return 0;
      }
      if (sepSize == 0) {
         return sizeTotal / siSize;
      } else {
         int count = 0;
         int currTotal = 0;
         while (siSize + currTotal < sizeTotal) {
            count++;
            currTotal += siSize;
            currTotal += sepSize;
         }
         return count;
      }
   }

   /**
    * used to store scrollconfig previous last position
    */
   int               cacheData;

   /**
    * Control flags to keep track of states
    */
   private int       flags;

   /**
    * Direction of scrolling. Starts at false<br>
    * <li>False when up/left <br>
    * <li>True when down/right
    */
   boolean           isSiDecreasing = true;

   private int       maxAmplitude   = 10;

   /**
    * How does it behaves on increase/decrease around at the 2 boundaries. 
    * <li> {@link ITechViewPane#SB_MOVE_TYPE_0_FIXED} Do nothing
    * <li> {@link ITechViewPane#SB_MOVE_TYPE_1_CLOCK} Around the clock
    * <li> {@link ITechViewPane#SB_MOVE_TYPE_2_CIRCULAR} Circular. Torique when horizontal and vertical together
    */
   int               scrollMoveType;

   /**
    * Decides which increments is shown as partial in {@link ITechViewPane#SCROLL_TYPE_0_PIXEL_UNIT}.
    * <br>
    * <br>
    * <li> {@link ITechViewPane#PARTIAL_TYPE_0_BOTH}
    * <li> {@link ITechViewPane#PARTIAL_TYPE_1_TOP}
    * <li> {@link ITechViewPane#PARTIAL_TYPE_2_BOTTOM}
    * <br>
    */
   int               scrollPartialType;

   /**
    * Unit of scrolling used on this {@link ScrollConfig}. 
    * <p>
    * Decided by ViewPane's Tech Param {@link IBOViewPane#VP_OFFSET_05_SCROLLBAR_MODE1}. <br>
    * <li>{@link ITechViewPane#SCROLL_TYPE_0_PIXEL_UNIT}
    * <li>{@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT}
    * <li>{@link ITechViewPane#SCROLL_TYPE_2_PAGE_UNIT}
    * </p>
    */
   int               scrollUnitType;

   /**
    * Behavior on visual excess pixels. <br>
    * <br>
    * Decided by ViewPane's Tech Param. <br>
    * <li>{@link ITechViewPane#VISUAL_0_LEAVE}
    * <li>{@link ITechViewPane#VISUAL_1_PARTIAL}
    * <li>{@link ITechViewPane#VISUAL_2_SHRINK}
    * <li>{@link ITechViewPane#VISUAL_3_FILL}
    * <br>
    * 
    */
   int               scrollVisualType;

   /**
    * Records the increment delta of the last change.
    * <br>
    * Used by animation code.
    */
   int               siLastChange;

   /**
    * Increment that were effectively moved
    */
   int               siLastChangeEffective;

   /**
    * The absolute number of pixels moved in the last move call.
    * <li> {@link ScrollConfig#moveDecrease(int)}
    * <li> {@link ScrollConfig#moveIncrease(int)}
    * 
    */
   private int       siLastChangeEffectivePixels;

   /**
    * Increment index being partially visible. -1 when no partial increment.
    * <br>
    * Only relevant with {@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT} and visual {@link ITechViewPane#VISUAL_1_PARTIAL}.
    * <br>
    * <br>
    * Computed automatically by {@link ScrollConfig}.
    * Opposite increment to the root increment.
    * Boundary is [0,siTotal-1]
    */
   int               siPartial      = -1;

   /**
    * the number of pixel for an increment (height of a line of text)
    */
   public int        siPixelSize    = 1;

   /**
    * Used when increments have different pixel sizes. 
    * <br>
    * Gives.
    */
   public int[]      siPixelSizes;

   /**
    * Most important increment from user point of view. Root is relevant in {@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT} mode.
    * <br>
    * <br>
    * Depending on Config direction, Equals to first or last showing increment <br>
    * <br>
    * Root is always fully visible in {@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT} mode. 
    * <li>when equal to 0, we are in default start position
    * <li>when equal to totalScrolling-1, we are in end position. 
    * <br>
    * <br>
    * 
    * If flag {@link ScrollConfig#FLAG_8_ALWAYS_BOTTOM}
    * when press down, makes first partial increment fully visible. root becomes this increment
    */
   int               siRoot;

   /**
    * Size of the separator element between increments.
    * <br>
    * Shows only when at least 2 increments are visible.
    */
   int               siSeparatorSize;

   /**
    * The current start increment. Maybe partially visible. <br>
    * e.g. The index of the first line displayed
    */
   int               siStart;

   /**
    * The number of increment that the Scrollable has
    * e.g. The height of an image, the number of lines.
    * <br>
    * <br>
    * At all times, siStart + siVisible <= siTotal
    * <br>
    * <li>0 + 1 <= 1 : for a single increment configuration
    */
   int               siTotal;

   /**
    * The number of visible increments.
    * <br>
    * <br>
    * Includes partially visible increment.
    * <br>
    * Initialized by {@link ViewDrawable}. Then may be modified by {@link ScrollConfig}
    */
   int               siVisible;

   /**
    * Give the total pixel size over which the {@link ScrollConfig} rules.
    * <br>
    * <br>
    * i.e. Content Width/Height 
    */
   int               sizeTotalPixel;

   int               state;


   public ScrollConfig(GuiCtx gc) {
      super(gc);
   }

   public ScrollConfig(GuiCtx gc, int start, int visible, int total) {
      super(gc);
      this.siStart = start;
      this.siVisible = visible;
      this.siTotal = total;
   }

   /**
    * Check if increment state is consistent.
    * 14/8/14 : Bug in that resize J2SE frame
    * updates the siStart to avoid overflow of the siTotal
    */
   void checkStart() {
      if (siStart + siVisible > siTotal) {
         //#debug
         gc.toDLog().pNull("Overflow", this, ScrollConfig.class, "checkStart", LVL_05_FINE, true);
         
         int diff = siStart + siVisible - siTotal;
         siStart = siStart - diff;
         if (siStart < 0) {
            siStart = 0;
         }
      }
   }

   public Object clone() {
      ScrollConfig sc = new ScrollConfig(gc);
      sc.cacheData = cacheData;
      sc.isSiDecreasing = isSiDecreasing;
      sc.scrollMoveType = scrollMoveType;
      sc.scrollPartialType = scrollPartialType;
      sc.scrollUnitType = scrollUnitType;
      sc.scrollVisualType = scrollVisualType;
      sc.siLastChange = siLastChange;
      sc.siLastChangeEffective = siLastChangeEffective;
      sc.siLastChangeEffectivePixels = siLastChangeEffectivePixels;
      sc.siPartial = siPartial;
      sc.siPixelSize = siPixelSize;
      sc.siPixelSizes = siPixelSizes;
      sc.siRoot = siRoot;
      sc.siSeparatorSize = siSeparatorSize;
      sc.siStart = siStart;
      sc.siTotal = siTotal;
      sc.siVisible = siVisible;
      sc.sizeTotalPixel = sizeTotalPixel;
      sc.state = state;
      return sc;
   }

   /**
    * Create a new {@link ScrollConfig} object cloned but with 
    * 
    * <li>SiStart incremented
    * <li>siVisible incremented 
    * 
    * 
    * @param startMod sistart inc
    * @param visibleMod 
    * @return
    */
   public ScrollConfig cloneModVisible(int startMod, int visibleMod) {
      ScrollConfig sc = (ScrollConfig) clone();
      sc.siStart += startMod;
      if (sc.siStart < 0) {
         sc.siStart = 0;
      }
      sc.siVisible += visibleMod;
      if (sc.siStart + sc.siVisible > siTotal) {
         sc.siVisible = siTotal - sc.siVisible;
      }
      return sc;
   }

   public ScrollConfig cloneModVisibleSet(int startMod, int visibleMod) {
      ScrollConfig sc = (ScrollConfig) clone();
      sc.siStart = startMod;
      sc.siVisible = visibleMod;
      if (sc.siStart + sc.siVisible > siTotal) {
         sc.siVisible = siTotal - sc.siVisible;
      }
      return sc;
   }

   /**
    * number of full increments consumed by the pixels
    * @param pixels
    * @return
    */
   public int getIncrementSize(int pixels, boolean top) {
      if (scrollUnitType == ITechViewPane.SCROLL_TYPE_0_PIXEL_UNIT) {
         return pixels;
      } else {
         if (siPixelSizes == null) {
            return IntUtils.divideFloor(pixels, siPixelSize);
         } else {
            int count = 0;
            if (top) {
               int total = 0;
               for (int i = 0; i < siVisible; i++) {
                  total += siPixelSizes[siStart + i];
                  if (total >= pixels) {
                     break;
                  } else {
                     count++;
                  }

               }
            }
            return count;
         }
      }
   }

   /**
    * Increment units.
    * <br>
    * <br>
    * In the case of a partially visible logical increment made fully visible, this value will be 0.
    * <br>
    * 
    * @return
    */
   public int getLastEffectiveChange() {
      return siLastChangeEffective;
   }

   /**
    * Number of increments
    * @return
    */
   public int getLeftInvisible() {
      return siTotal - (siStart + getSIVisible());
   }

   public int getMaximumAmplitude() {
      return maxAmplitude;
   }

   /**
    * Number of pixels since the last change. 
    * <br>
    * <br>
    * usually increment size. depends on size of increment. <br>
    * Partial increment is computed by the {@link ScrollConfig} user.
    * @return
    */
   public int getMoveSize() {
      return siPixelSize;
   }

   /**
    * Positive value. Number of pixels hidden on the partial increment {@link ITechViewPane#SCROLL_TYPE_0_PIXEL_UNIT}.
    * <br>
    * <br>
    * Happens only when direction is down with scroll partial visible
    * <br>
    * @return
    */
   public int getPartialOffset() {
      if (isPartial()) {
         if (siPartial != -1) {
            int visibleSize = getPixelVisibleSize();
            return visibleSize - sizeTotalPixel;
         }
      }
      return 0;
   }

   public int getPartialType() {
      return scrollPartialType;
   }

   /**
    * Pixels consumed by the given increments, inclusive of startIcr,exclusive of endIcr
    * <br>
    * <br>
    * 0-1 returns the size of the first increment
    * <br>
    * <br>
    * Method include 1 outside separator. TODO WHY?
    * @param startIcr included
    * @param endIcr excluded
    * @return
    */
   public int getPixelSize(int startIcr, int endIcr) {
      int val = 0;
      int mul = endIcr - startIcr;
      if (siPixelSizes == null) {
         val = siPixelSize * mul;
      } else {
         for (int i = startIcr; i < endIcr; i++) {
            val += siPixelSizes[i];
         }
      }
      val += (mul) * siSeparatorSize;
      return val;
   }

   /**
    * Number of pixels consumed by fully visible increments.
    * <br>
    * <br>
    * @return
    */
   public int getPixelSizeFullVisible() {
      if (siPartial != -1) {
         return (siVisible - 1) * siPixelSize;
      } else {
         //check if increments have different sizes
         if (siPixelSizes != null) {
            int total = 0;
            for (int i = 0; i < siVisible; i++) {
               total += siPixelSizes[siStart + i];
            }
            return total;
         } else {
            return (siVisible) * siPixelSize;
         }
      }
   }

   public int getPixelSizeLastMove() {
      return siLastChangeEffectivePixels;
   }

   /**
    * The pixels consumed by all visible increments including the inside separators and partial increments
    * <br>
    * <br>
    * The result may be bigger than sizeTotalPixel because it includes full pixel of partial increment.
    * <br>
    * @return
    */
   public int getPixelVisibleSize() {
      return siVisible * siPixelSize + ((siVisible - 1) * siSeparatorSize);
   }

   public int getSILastChange() {
      return siLastChange;
   }

   /**
    * Return increment size
    * @param fromStart
    * @return
    */
   public int getSISize(int fromStart) {
      return siPixelSize;
   }

   /**
    * The first increment shown.
    * <br>
    * @return
    */
   public int getSIStart() {
      return siStart;
   }

   /**
    * The last possible si start increment with normal x visibles.
    * <br>
    * <br>
    * TODO test with different si sizes.
    * @return
    */
   public int getSIStartRangeEnd() {
      int re = siTotal;
      if (siPixelSizes == null) {
         return re - siVisible;
      } else {
         int len = siPixelSizes.length;
         int cont = 0;
         int siStart = len;
         for (int i = len - 1; i >= 0; i--) {
            cont += siPixelSizes[i];
            //TODO what if oversized increment?
            if (cont <= sizeTotalPixel) {
               siStart--;
            } else {
               break;
            }
            cont += siSeparatorSize;
         }
         return siStart;
      }
   }

   /**
    * The total number of increments.
    * @return
    */
   public int getSITotal() {
      return siTotal;
   }

   /**
    * The number of scrolling increments.
    * @return
    */
   public int getSITotalPixel() {
      if (scrollUnitType == ITechViewPane.SCROLL_TYPE_0_PIXEL_UNIT) {
         return siTotal * siPixelSize;
      } else {
         if (siPixelSizes != null) {
            return IntUtils.sum(siPixelSizes);
         } else {
            return siPixelSize * siTotal;
         }
      }
   }

   /**
    * Get the number of visible increments, including start and partially visible. <br>
    * Computed based on increment sizes and total size.
    * <br>
    * This value is model independant!
    * <br>
    * Caller must check model has enough cells for it
    * @return
    */
   public int getSIVisible() {
      return siVisible;
   }

   /**
    * Used by the Scrollbar
    * @return
    */
   public int getSIVisibleFully() {
      if (siPartial != -1) {
         return siVisible - 1;
      }
      return siVisible;
   }

   /**
    * The first increment that is fully visible. <br>
    * Used by Scrollbar
    * @return
    */
   public int getStartFirstFullyVisible() {
      if (siPartial != -1) {
         if (scrollPartialType == ITechViewPane.PARTIAL_TYPE_0_BOTH) {
            //when increasing.. siStart is partially visible
            if (!isSiDecreasing) {
               return siStart + 1;
            }
         } else if (scrollPartialType == ITechViewPane.PARTIAL_TYPE_2_BOTTOM) {
            //todo
            return siStart + 1;
         }
      }
      return siStart;
   }

   /**
    * Type of move around the boundaries. <br>
    * <li> {@link ITechViewPane#SB_MOVE_TYPE_0_FIXED} Do nothing
    * <li> {@link ITechViewPane#SB_MOVE_TYPE_1_CLOCK} Around the clock
    * <li> {@link ITechViewPane#SB_MOVE_TYPE_2_CIRCULAR} Circular. Torique when horizontal and vertical together
   
    * @return
    */
   public int getTypeMove() {
      return scrollMoveType;
   }

   /**
    * Scrolling Type set by {@link ViewPane}. <br>
    * <li>{@link ITechViewPane#SCROLL_TYPE_0_PIXEL_UNIT}
    * <li>{@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT}
    * <li>{@link ITechViewPane#SCROLL_TYPE_2_PAGE_UNIT}
    * @return
    */
   public int getTypeUnit() {
      return scrollUnitType;
   }

   /**
    * Visual Type set by {@link ViewPane}. <br>
    * <li>{@link ITechViewPane#VISUAL_0_LEAVE}
    * <li>{@link ITechViewPane#VISUAL_1_PARTIAL}
    * <li>{@link ITechViewPane#VISUAL_2_SHRINK}
    * <li>{@link ITechViewPane#VISUAL_3_FILL}
    * @return
    */
   public int getTypeVisual() {
      return scrollVisualType;
   }

   public int getZOffset() {
      return siStart;
   }

   /**
    * Returns start increment.
    * <br>
    * <br>
    * In pixel mode, that "unit".
    * <br>
    * <br>
    * @return
    */
   public int getZStart() {
      return siStart;
   }

   /**
    * 
    * @return
    */
   public int getZVisible() {
      return siVisible;
   }

   public boolean hasFlag(int flag) {
      return BitUtils.hasFlag(flags, flag);
   }

   /**
    * Number of increments is decided by array size.
    * <br>
    * @param siSizes
    * @param sizeTotal
    * @param siTotal
    */
   public void initConfig(int[] siSizes, int sizeTotal) {
      this.sizeTotalPixel = sizeTotal;
      this.siTotal = siSizes.length;
      this.siVisible = Math.min(sizeTotal / siPixelSize, siTotal);
   }

   public void initConfigLogic(int siPixelSizes[], int sizeTotal, int siTotal) {
      initConfigLogic(siPixelSizes, sizeTotal, siTotal, 0);
   }

   /**
    * Since all increments have the same size, this method is not dependant on siStart.
    * @param siPixelSize
    * @param sizeTotal pixel size available to draw increments. Usually content view port size
    * @param siTotal
    */
   public void initConfigLogic(int siPixelSize, int sizeTotal, int siTotal) {
      initConfigLogic(siPixelSize, sizeTotal, siTotal, 0);
   }

   /**
    * Initialize with different increment sizes.
    * @param siPixelSizes cannot be null
    * @param sizeTotal the total pixel size over which the {@link ScrollConfig} rules.
    * @param siTotal number of increments
    */
   public void initConfigLogic(int siPixelSizes[], int sizeTotal, int siTotal, int sepSize) {
      this.siPixelSizes = siPixelSizes;
      this.sizeTotalPixel = sizeTotal;
      this.siTotal = siTotal;
      this.siVisible = 0;
      //num visible
      int total = 0;
      for (int i = 0; i < siPixelSizes.length; i++) {
         if (total + siPixelSizes[i] <= sizeTotal) {
            total += siPixelSizes[i];
            total += sepSize;
            siVisible++;
         } else {
            //deal with partial
            if (isPartial()) {
               siPartial = siStart + siVisible;
               siVisible++;
            }
            break;
         }
      }
   }

   /**
    * Init method for the {@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT} types.
    * <br>
    * <br>
    * @param siPixelSize
    * @param sizeTotal pixel size available
    * @param siTotal number of increment
    * @param sepSize size of increment separator
    */
   public void initConfigLogic(int siPixelSize, int sizeTotal, int siTotal, int sepSize) {
      this.siPixelSize = siPixelSize;
      this.sizeTotalPixel = sizeTotal;
      this.siTotal = siTotal;
      this.siSeparatorSize = sepSize;
      if (isPartial()) {
         //compute if any, the partial increment 
         int numFullyVisible = sizeTotal / siPixelSize;
         //compute if there is pixel left to show an increment+its separator partially.
         int mod = sizeTotal % siPixelSize;
         if (sepSize != 0) {
            numFullyVisible = getNumInside(sizeTotal, siPixelSize, sepSize);
            mod = sizeTotal - ((numFullyVisible * siPixelSize) + ((numFullyVisible - 1) * sepSize));
         }
         if (mod != 0) {
            siPartial = siStart + numFullyVisible;
            numFullyVisible++;
         }
         this.siVisible = Math.min(numFullyVisible, siTotal);
      } else {
         int num = getNumInside(sizeTotal, siPixelSize, sepSize);
         this.siVisible = Math.min(num, siTotal);
      }
   }

   /**
    * In Pixel/Page  mode, all that is needed is the number of pixels, 
    * @param siPixelSize
    * @param siVisible
    * @param siTotal
    */
   public void initConfigPixel(int siPixelSize, int siVisible, int siTotal) {
      //#debug
      toDLog().pFlow("siPixelSize=" + siPixelSize + " siVisible=" + siVisible + " siTotal=" + siTotal, this, ScrollConfig.class, "initConfigPixel", LVL_05_FINE, true);
      
      this.siPixelSize = siPixelSize;
      this.siVisible = siVisible;
      this.siTotal = siTotal;
      this.sizeTotalPixel = siTotal;
   }

   /**
    * Is scrollconfig showing the last slate of increments.
    * @return
    */
   public boolean isEnd() {
      if (isPartial()) {
         return getStartFirstFullyVisible() + getSIVisibleFully() == siTotal;
      } else {
         return siStart + siVisible == siTotal;
      }
   }

   /**
    * Is scrolling visual type of this config {@link ITechViewPane#VISUAL_1_PARTIAL}.
    * <br>
    * To show partial increment as visible.
    * @return
    */
   public boolean isPartial() {
      return scrollVisualType == ITechViewPane.VISUAL_1_PARTIAL;
   }

   /**
    * Will this move increase reach the end
    * @param nextChange
    * @return
    */
   private boolean isReachingEnd(int nextChange) {
      int numF = getSIVisibleFully();
      int diff = siTotal - numF;
      int nStart = siStart + nextChange;
      //System.out.println("diff="+diff + " nStart="+nStart);
      return diff <= nStart;
   }

   /**
    * The direction of the last change. <br>
    * <li>true if the direction of scrolling is decreasing siStart top/left
    * <li>false when bottom/right
    * @return
    */
   public boolean isSiDecreasing() {
      return isSiDecreasing;
   }

   /**
    * Modifies the start increment by <code>i</code> .
    * <br>
    * <br>
    * <li>When i is negative, decrease start increment
    * <li>When i is positive, increase start increment
    * <br>
    * <br>
    * @param i the increment modification
    */
   public synchronized void move(int i) {
      if (i < 0) {
         moveDecrease(-i);
      } else {
         moveIncrease(i);
      }
   }

   /**
    * Decrease start increment.
    * <br>
    * <br>
    * So, isStart will always be smaller or equal in {@link ITechViewPane#SB_MOVE_TYPE_0_FIXED}
    * <br>
    * <br>
    * @param i
    * @return the number of increments that were actually moved down.
    */
   public synchronized void moveDecrease(int i) {
      siLastChange = i;
      int before = siStart;
      switch (scrollMoveType) {
         case ITechViewPane.SB_MOVE_TYPE_0_FIXED:
            if (isPartial()) {
               siFixedPartialDecrease(i);
            } else {
               int left = siStart;
               int nextChange = left;
               //simple
               if (i <= left) {
                  nextChange = i;
               }
               siLastChange = nextChange;
               siStart -= nextChange;
            }
            break;
         case ITechViewPane.SB_MOVE_TYPE_1_CLOCK:
            siStart -= i;
            if (siStart < 0) {
               int diff = -siStart;
               siStart = siTotal - siVisible - diff + 1;
            }
            break;
         case ITechViewPane.SB_MOVE_TYPE_2_CIRCULAR:
            siStart -= i;
            if (siStart < 0) {
               siStart = siTotal + siStart;
            }
            break;
         default:
            break;
      }
      siLastChangeEffective = before - siStart; //start is smaller after
      if (scrollUnitType != ITechViewPane.SCROLL_TYPE_0_PIXEL_UNIT) {
         siLastChangeEffectivePixels = getPixelSize(siStart, before);
      } else {
         siLastChangeEffectivePixels = siLastChangeEffective;
      }
      isSiDecreasing = true;
      checkStart();
   }

   /**
   * Navigation method to propagate changes to the scrollbars. 
   * <br>
   * <br>
   * If increase goes over
   * <br>
   * <br>
   * @param i the increase for start increment
   * @return true if configuration state has changed and a repaint is necessary.
   */
   public synchronized void moveIncrease(int i) {
      siLastChange = i;
      int before = siStart;
      switch (scrollMoveType) {
         case ITechViewPane.SB_MOVE_TYPE_0_FIXED:
            if (isPartial()) {
               siFixedPartialIncrease(i);
            } else {
               int left = siTotal - (siStart + getSIVisible());
               int nextChange = left;
               //simple
               if (i <= left) {
                  nextChange = i;
               }
               siLastChange = nextChange;
               siStart += nextChange;
            }
            break;
         case ITechViewPane.SB_MOVE_TYPE_1_CLOCK:
            siStart += i;
            siStart %= (siTotal - siVisible);
            break;
         case ITechViewPane.SB_MOVE_TYPE_2_CIRCULAR:
            siStart += i;
            siStart %= siTotal;
            break;
         default:
            break;
      }
      siLastChangeEffective = siStart - before; //start is bigger after
      if (scrollUnitType != ITechViewPane.SCROLL_TYPE_0_PIXEL_UNIT) {
         siLastChangeEffectivePixels = getPixelSize(before, siStart);
      } else {
         siLastChangeEffectivePixels = siLastChangeEffective;
      }
      //finally swtich to increasing mode
      isSiDecreasing = false;
      checkStart();
   }

   public void setFlag(int flag, boolean v) {
      flags = BitUtils.setFlag(flags, flag, v);
   }

   public void setMaxAmplitude(int ma) {
      maxAmplitude = ma;
   }

   /**
    * Sets the Unit of scrolling used on this {@link ScrollConfig}. 
    * <p>
    * Decided by ViewPane's Tech Param {@link IBOViewPane#VP_OFFSET_05_SCROLLBAR_MODE1}. <br>
    * <li>{@link ITechViewPane#SCROLL_TYPE_0_PIXEL_UNIT}
    * <li>{@link ITechViewPane#SCROLL_TYPE_1_LOGIC_UNIT}
    * <li>{@link ITechViewPane#SCROLL_TYPE_2_PAGE_UNIT}
    * </p>
    * @param unit
    */
   public void setScrollUnit(int unit) {
      scrollUnitType = unit;
   }

   /**
    * In case of Logic, gives the partially visible increment. Only 1 is possible.
    * @param partialincr
    */
   public void setSIPartial(int partialincr) {
      siPartial = partialincr;
   }


   public void setSISizes(int[] sizes) {
      siPixelSizes = sizes;
   }

   /**
    * Moves {@link ScrollConfig} to the given starts increment. 
    * <br>
    * <br>
    * If out of range, bring it back into range and apply around the clock policy
    * <br>
    * <br>
    * @param newStart can be positive or negative.
    */
   public void setSIStart(int newStart) {
      if (siStart != newStart) {
         if (newStart >= 0 && newStart + siVisible <= siTotal) {
            int diff = newStart - siStart;
            move(diff);
         } else {
            throw new IllegalArgumentException("Increment Overflow newStart=" + newStart + " siTotal=" + siTotal + " siVisible" + siVisible + " siStart=" + siStart);
         }
      }
   }

   /**
    * Generates a move
    * @param newStart
    */
   public void setSIStartNoEx(int newStart) {
      if (siStart != newStart) {
         int diff = newStart - siStart;
         move(diff);
      }
   }

   /**
    * Used by Animation code to create custom {@link ScrollConfig}.
    * <br> <br>
    * Keeps increment sizes and total size for further computation.
    * <br>
    * @param start
    * @param visible
    * @param total
    */
   public void setSIStartVisibleTotal(int start, int visible, int total) {
      this.siVisible = visible;
      this.siTotal = total;
      setSIStart(start);
   }

   /**
    * TODO move the business to CellModel? No because, it has to work for other kinds of Scrolling?
    * Cellmodel business moves here.
    * @param i number of increments to move
    */
   private void siFixedPartialDecrease(int i) {
      int left = siStart;
      int nextChange = left;
      switch (scrollPartialType) {
         case ITechViewPane.PARTIAL_TYPE_0_BOTH:
            //simple
            if (i <= left) {
               //middle case
               nextChange = i;
            }
            siLastChange = nextChange;
            //compute the partialIncrement
            if (getStartFirstFullyVisible() == siTotal - getSIVisibleFully()) {
               siPartial = siTotal - 1;
            } else if (!isSiDecreasing) {
               //middle giggle up/down/up/down
               //swaps the partial increment.just shows the 
               siPartial = siStart + siVisible;
            } else {
               //continuation
               siStart -= nextChange;
               siPartial -= nextChange;
            }
            break;
         case ITechViewPane.PARTIAL_TYPE_1_TOP:
            //check if we start from the end
            if (i <= left) {
               //middle case
               nextChange = i;
            }
            if (isEnd() && hasFlag(FLAG_7_ALWAYS_TOP)) {
               siPartial = siTotal - nextChange;
               siVisible++;
               setFlag(FLAG_7_ALWAYS_TOP, false);
            } else {
               siPartial -= nextChange;
            }
            siStart -= nextChange;
            siLastChange = nextChange;
            siRoot = siStart;//siStart in this partial mode
            break;
         case ITechViewPane.PARTIAL_TYPE_2_BOTTOM:
            siRoot = siTotal;
            siVisible = sizeTotalPixel / siPixelSize;
            break;
         default:
            break;
      }

   }

   /**
    * Up in a fixed context
    * @param i
    */
   private void siFixedPartialIncrease(int i) {
      //number 
      int leftInvisible = siTotal - (siStart + getSIVisible());
      int leftNotFullyVisible = siTotal - (siStart + getSIVisibleFully());
      int nextChange = leftNotFullyVisible;
      if (i <= leftNotFullyVisible) {
         //middle case
         nextChange = i;
      }
      switch (scrollPartialType) {
         case ITechViewPane.PARTIAL_TYPE_0_BOTH:
            //siPartial = siTotal - siVisible;
            //head is always visible
            leftInvisible = siTotal - (siStart + getSIVisible());
            nextChange = leftInvisible;
            if (i <= leftInvisible) {
               //middle case
               nextChange = i;
            }
            if (getStartFirstFullyVisible() == 0) {
               siPartial = 0;
            } else if (isSiDecreasing) {
               //swaps the partial increment.just shows the 
               siPartial = siStart;
            } else {
               //continuation
               siStart += nextChange;
               siPartial += nextChange;
            }
            siLastChange = nextChange;
            break;
         case ITechViewPane.PARTIAL_TYPE_1_TOP:
            if (leftNotFullyVisible == 0) {
               siLastChange = 0;
            } else if (isReachingEnd(nextChange)) {
               //steps reach the end.
               siVisible = sizeTotalPixel / siPixelSize;
               siPartial = -1;
               siStart = siTotal - siVisible;//the new siVisible
               siLastChange = nextChange;
               setFlag(FLAG_7_ALWAYS_TOP, true);
            } else {
               siStart += nextChange;
               siPartial += nextChange;
               siLastChange = nextChange;
            }
            siRoot = siStart;
            //flag special end state
            break;
         case ITechViewPane.PARTIAL_TYPE_2_BOTTOM:
            siRoot = siTotal;
            siVisible = sizeTotalPixel / siPixelSize;
            break;
         default:
            break;
      }
   }


   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, ScrollConfig.class, 1220);
      toStringPrivate(dc);
      super.toString(dc.sup());
      
      dc.append("start=" + siStart);
      dc.append('(');
      dc.append(getStartFirstFullyVisible());
      dc.append(')');
      dc.append(" visible=" + siVisible);
      dc.append('(');
      dc.append(getSIVisibleFully());
      dc.append(')');
      dc.append(" total=" + siTotal);
      if (siPixelSizes == null) {
         dc.append(" siPixelSize=" + siPixelSize);
      } else {
         dc.append('[');
         for (int i = 0; i < siPixelSizes.length; i++) {
            if (i != 0)
               dc.append(',');
            dc.append(siPixelSizes[i]);
         }
         dc.append(']');
      }
      dc.append(" sizeTotal=" + sizeTotalPixel);
      dc.append(" isDecreasing=" + isSiDecreasing);
      dc.append(" siLastChange=" + siLastChange);
      dc.nl();
      dc.append(" move=" + ToStringStaticGui.toStringScrollMove(scrollMoveType));
      dc.append(" type=" + ToStringStaticGui.toStringScrollbarMode(scrollUnitType));
      dc.append(" visual=" + ToStringStaticGui.toStringScrollVisual(scrollVisualType));
      if (isPartial()) {
         dc.append(" partial=" + ToStringStaticGui.toStringScrollPartial(scrollPartialType));
         dc.append(" siPartial=" + siPartial);
      }
      dc.append(" isEnd=" + isEnd());
   }

   private void toStringPrivate(Dctx dc) {
      
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ScrollConfig.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug
   
}
