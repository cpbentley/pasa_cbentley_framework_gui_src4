package pasa.cbentley.framework.gui.src4.ctx;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.ToStringStaticBase;
import pasa.cbentley.framework.gui.src4.anim.ITechAnim;
import pasa.cbentley.framework.gui.src4.anim.move.ITechMoveFunction;
import pasa.cbentley.framework.gui.src4.core.ViewDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechViewDrawable;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOCellPolicy;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOTablePolicy;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOTableView;
import pasa.cbentley.framework.gui.src4.tech.ITechStringDrawable;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;

public class ToStringStaticGui extends ToStringStaticBase {

   public static String toStringViewBehavior(int state) {
      switch (state) {
         case ITechViewDrawable.VIEW_GENE_28_NOT_SCROLLABLE:
            return "NotScrollable";
         case ITechViewDrawable.VIEW_GENE_29_SHRINKABLE_W:
            return "ShrinkableW";
         case ITechViewDrawable.VIEW_GENE_30_SHRINKABLE_H:
            return "ShrinkableH";
         case ITechViewDrawable.VIEW_GENE_31_EXPANDABLE_W:
            return "ExpandableW";
         case ITechViewDrawable.VIEW_GENE_32_EXPANDABLE_H:
            return "ExpandableH";
         default:
            return "Unknown" + state;
      }
   }

   public static String toStringViewPaneStyleType(int type) {
      switch (type) {
         case ITechViewPane.DRW_STYLE_0_VIEWDRAWABLE:
            return "ViewDrawabe";
         case ITechViewPane.DRW_STYLE_1_VIEWPANE:
            return "ViewPane";
         case ITechViewPane.DRW_STYLE_2_VIEWPORT:
            return "ViewPort";
         case ITechViewPane.DRW_STYLE_3_IGNORED:
            return "Ignored";
         default:
            return "UnknownStyle " + type;
      }
   }

   public static String toStringMoveType(int v) {
      switch (v) {
         case ITechMoveFunction.TYPE_MOVE_0_ASAP:
            return "ASAP";
         case ITechMoveFunction.TYPE_MOVE_1_BRESENHAM:
            return "Bresenham";
         default:
            return "Unknown" + v;
      }
   }

   public static String toStringMoveIncrementType(int v) {
      switch (v) {
         case ITechMoveFunction.INCREMENT_0_PARAM:
            return "Linear";
         case ITechMoveFunction.INCREMENT_1_FIB:
            return "Fib";
         default:
            return "Unknown" + v;
      }
   }

   public static String toStringPolicyType(final int type) {
      switch (type) {
         case IBOCellPolicy.CELL_3_FILL_STRONG:
            return "FILL_STRONG";
         case IBOCellPolicy.CELL_4_FILL_AVERAGE:
            return "FILL_AVERAGE";
         case IBOCellPolicy.CELL_5_FILL_WEAK:
            return "FILL_WEAK";
         case IBOCellPolicy.CELL_1_EXPLICIT_SET:
            return "EXPLICIT";
         case IBOCellPolicy.CELL_2_RATIO:
            return "RATIO";
         case IBOCellPolicy.CELL_0_IMPLICIT_SET:
            return "IMPLICIT";
         default:
            return "UNKNOWN" + type;
      }
   }

   public static String toStringType(final int type) {
      switch (type) {
         case IBOCellPolicy.TYPE_0_GENERIC:
            return "GENERIC";
         case IBOCellPolicy.TYPE_2_RATIO:
            return "RATIO";
         case IBOCellPolicy.TYPE_1_FLOW:
            return "FLOW";
         default:
            return "UNKNOWN " + type;
      }
   }

   public static String toStringDebugMode(int mode) {
      switch (mode) {
         case ITechCanvasDrawable.DEBUG_0_NONE:
            return "None";
         case ITechCanvasDrawable.DEBUG_1_LIGHT:
            return "Light";
         case ITechCanvasDrawable.DEBUG_2_COMPLETE:
            return "Complete";
         case ITechCanvasDrawable.DEBUG_3_2COMPLETE:
            return "2Complete";
         default:
            return "Unknown" + mode;
      }

   }

   /**
    * Debug name for link IDs such as 
    * <li>{@link IBOTypesGui#LINK_40_TECH_STRING_EDIT}
    * <li>{@link IBOTypesGui#LINK_50_STYLE_SCROLLBAR_BLOCK_BG}
    * <li>{@link IBOTypesGui#LINK_65_STYLE_VIEWPANE}
    * 
    * @param link
    * @return
    */
   public static String toStringLinkStatic(int link) {
      switch (link) {
         case IBOTypesGui.LINK_40_TECH_STRING_EDIT:
            return "TechStringEdit";
         case IBOTypesGui.LINK_41_TECH_STRING:
            return "TechString";
         case IBOTypesGui.LINK_50_STYLE_SCROLLBAR_BLOCK_BG:
            return "ScrollBar BlockBg";
         case IBOTypesGui.LINK_51_STYLE_SCROLLBAR_BLOCK_FIG:
            return "ScrollBar BlockFig";
         case IBOTypesGui.LINK_52_STYLE_SCROLLBAR_TOP_LEFT_WRAPPER:
            return "ScrollBar TopLeftWrapper";
         case IBOTypesGui.LINK_53_STYLE_SCROLLBAR_BOT_RIGHT_WRAPPER:
            return "ScrollBar BotRightWrapper";
         case IBOTypesGui.LINK_65_STYLE_VIEWPANE:
            return "ViewPaneStyle";
         case IBOTypesGui.LINK_66_TECH_VIEWPANE:
            return "ViewPaneStyle";
         case IBOTypesGui.LINK_67_STYLE_VIEWPANE_HOLE:
            return "ViewPaneStyle";
         case IBOTypesGui.LINK_68_TECH_V_SCROLLBAR:
            return "SBVTech";
         case IBOTypesGui.LINK_69_TECH_H_SCROLLBAR:
            return "SBHTech";
         case IBOTypesGui.LINK_71_STYLE_VIEWPANE_H_SCROLLBAR:
            return "ViewPaneHSBStyle";
         case IBOTypesGui.LINK_72_STYLE_VIEWPANE_V_SCROLLBAR:
            return "ViewPaneVSBStyle";
         case IBOTypesGui.LINK_80_TECH_TABLE:
            return "Table Tech";
         case IBOTypesGui.LINK_81_STYLE_CLASS_TABLE_CELL:
            return "Table CellStyle";
         case IBOTypesGui.LINK_82_STYLE_TABLE_COL_TITLE:
            return "Table ColStyle";
         case IBOTypesGui.LINK_83_STYLE_TABLE_ROW_TITLE:
            return "Table RowStyle";
         default:
            return "UnknownLink " + link;
      }
   }

   public static String toStringTypeBO(final int type) {
      switch (type) {
         case IBOTypesGui.TYPE_101_VIEWPANE_TECH:
            return "ViewPaneTech";
         case IBOTypesGui.TYPE_102_SCROLLBAR_TECH:
            return "ScrollbarTech";
         case IBOTypesGui.TYPE_103_TABLE_TECH:
            return "TableTech";
         case IBOTypesGui.TYPE_119_GENETICS:
            return "Genetics";
         case IBOTypesGui.TYPE_121_SPANNING:
            return "Spanning";
         case IBOTypesGui.TYPE_122_CELL_POLICY:
            return "CellPolicy";
         case IBOTypesGui.TYPE_124_STRING_TECH:
            return "StringTech";
         case IBOTypesGui.TYPE_125_STRING_EDIT_TECH:
            return "StringEditTech";
         case IBOTypesGui.TYPE_126_STRING_GLOBAL:
            return "StringGlobal";
         case IBOTypesGui.TYPE_127_STRING_PREDITION:
            return "StringPred";
         default:
            return null;
      }
   }

   public static String debugAnimType(int type) {
      switch (type) {
         case ITechAnim.ANIM_TYPE_01_VALUE:
            return "Values";
         case ITechAnim.ANIM_TYPE_02_REVERSE:
            return "Reverse";
         case ITechAnim.ANIM_TYPE_03_MOVE:
            return "Move";
         case ITechAnim.ANIM_TYPE_04_PIXELATE:
            return "Pixelate";
         case ITechAnim.ANIM_TYPE_05_LINE_SHIFT:
            return "LineShift";
         case ITechAnim.ANIM_TYPE_06_ALPHA:
            return "Alpha";
         case ITechAnim.ANIM_TYPE_07_ALPHA_TRAIL:
            return "Alpha Trail";
         default:
            return "Unknown";
      }
   }

   public static String debugDrawType(int t) {

      switch (t) {
         case ITechAnim.ANIM_DRAW_0_OVERRIDE:
            return "Override";
         case ITechAnim.ANIM_DRAW_1_CACHE:
            return "Cache";
         case ITechAnim.ANIM_DRAW_2_DRAWABLE:
            return "Drawable";
         default:
            return "Unknown" + t;
      }
   }

   public static String debugTiming(int t) {

      switch (t) {
         case ITechAnim.ANIM_TIME_0_MAIN:
            return "Main";
         case ITechAnim.ANIM_TIME_1_ENTRY:
            return "Entry";
         case ITechAnim.ANIM_TIME_2_EXIT:
            return "Exit";
         default:
            return "Unknown" + t;
      }
   }

   public static void debugBehaviorFlag(IDrawable d, int flag, Dctx sb) {
      if (d.hasBehavior(flag)) {
         sb.append(toStringBehavior(flag));
         sb.append(' ');
      }
   }

   public static String debugCompetType(int value) {
      switch (value) {
         case ITechViewPane.COMPET_HEADER_0_NEUTRAL:
            return "Neutral";
         case ITechViewPane.COMPET_HEADER_1_HORIZ:
            return "Horizontal";
         case ITechViewPane.COMPET_HEADER_2_VERTICAL:
            return "Vertical";
         case ITechViewPane.COMPET_HEADER_3_WHEEL:
            return "WheelCC";
         case ITechViewPane.COMPET_HEADER_4_WHEEL_CCW:
            return "WheelCCW";
         default:
            return "Unknown " + value;
      }
   }

   public static String debugPlanetStruct(int val) {
      switch (val) {
         case ITechViewPane.PLANET_MODE_0_EAT:
            return "Eat";
         case ITechViewPane.PLANET_MODE_1_EXPAND:
            return "Expand";
         case ITechViewPane.PLANET_MODE_2_OVERLAY:
            return "Overlay";
         case ITechViewPane.PLANET_MODE_3_IMMATERIAL:
            return "Immaterial";
         default:
            return "Unknown";
      }
   }

   public static String debugScrollMove(int t) {
      switch (t) {
         case ITechViewPane.SB_MOVE_TYPE_0_FIXED:
            return "Fixed";
         case ITechViewPane.SB_MOVE_TYPE_1_CLOCK:
            return "Clock";
         case ITechViewPane.SB_MOVE_TYPE_2_CIRCULAR:
            return "Circular";
         default:
            return "UnknownType" + t;
      }
   }

   public static String debugScrollPartial(int t) {
      switch (t) {
         case ITechViewPane.PARTIAL_TYPE_0_BOTH:
            return "Both";
         case ITechViewPane.PARTIAL_TYPE_1_TOP:
            return "Top";
         case ITechViewPane.PARTIAL_TYPE_2_BOTTOM:
            return "Bottom";
         default:
            return "UnknownType" + t;
      }
   }

   public static String debugScrollType(int t) {
      switch (t) {
         case ITechViewPane.SCROLL_TYPE_0_PIXEL_UNIT:
            return "Pixel";
         case ITechViewPane.SCROLL_TYPE_1_LOGIC_UNIT:
            return "Logic";
         case ITechViewPane.SCROLL_TYPE_2_PAGE_UNIT:
            return "Page";
         default:
            return "UnknownType" + t;
      }
   }

   public static String debugScrollVisual(int t) {
      switch (t) {
         case ITechViewPane.VISUAL_0_LEAVE:
            return "Leave";
         case ITechViewPane.VISUAL_1_PARTIAL:
            return "Partial";
         case ITechViewPane.VISUAL_2_SHRINK:
            return "Shrink";
         case ITechViewPane.VISUAL_3_FILL:
            return "Fill";
         default:
            return "UnknownVisual" + t;
      }
   }

   public static String debugStringType(int type) {
      switch (type) {
         case ITechStringDrawable.TYPE_0_NONE:
            return "BASIC";
         case ITechStringDrawable.TYPE_1_TITLE:
            return "TITLE";
         case ITechStringDrawable.TYPE_3_SCROLL_V:
            return "STRING_TYPE_XLINES_V_SCROLL";
         case ITechStringDrawable.TYPE_2_SCROLL_H:
            return "STRING_TYPE_TEXT_H_SCROLL";
         case ITechStringDrawable.TYPE_4_NATURAL_NO_WRAP:
            return "STRING_TYPE_NATURAL_NO_WRAP";
         case ITechStringDrawable.TYPE_5_CHAR:
            return "STRING_TYPE_CHAR";
         default:
            throw new IllegalArgumentException("" + type);
      }
   }

   public static String debugCacheType(int cacheType) {
      switch (cacheType) {
         case ITechDrawable.CACHE_0_NONE:
            return "NONE";
         case ITechDrawable.CACHE_1_CONTENT:
            return "Content";
         case ITechDrawable.CACHE_2_FULL:
            return "Full";
         case ITechDrawable.CACHE_3_BG_DECO:
            return "Deco";
         default:
            return "Unknown";
      }
   }

   private static void debugStateFlag(IDrawable d, int flag, Dctx sb) {
      if (d.hasState(flag)) {
         sb.append(toStringState(flag));
         sb.append(" ");
      }
   }

   public static String debugStateStyle(int sty) {
      switch (sty) {
         case ITechDrawable.STYLE_01_CUSTOM:
            return "Custom1";
         case ITechDrawable.STYLE_02_CUSTOM:
            return "Custom2";
         case ITechDrawable.STYLE_03_MARKED:
            return "Marked";
         case ITechDrawable.STYLE_04_GREYED:
            return "Greyed";
         case ITechDrawable.STYLE_05_SELECTED:
            return "Selected";
         case ITechDrawable.STYLE_06_FOCUSED_KEY:
            return "KeyFocus";
         case ITechDrawable.STYLE_07_FOCUSED_POINTER:
            return "PointerFocus";
         case ITechDrawable.STYLE_08_PRESSED:
            return "Pressed";
         case ITechDrawable.STYLE_09_DRAGGED:
            return "Dragged";
         case ITechDrawable.STYLE_10_DRAGGED_OVER:
            return "DraggedOver";
         case ITechDrawable.STYLE_11_CUSTOM:
            return "Custom11";
         case ITechDrawable.STYLE_12_CUSTOM:
            return "Custom12";
         default:
            return "Unknown";
      }
   }

   private static void debugStateStyleFlag(IDrawable d, int flag, Dctx sb) {
      if (d.hasStateStyle(flag)) {
         sb.append(debugStateStyle(flag));
         sb.append(" ");
      }
   }

   public static void debugStateStyles(IDrawable d, Dctx sb) {
      sb.append("StateStyle [");
      int oc = sb.getCount();
      debugStateStyleFlag(d, ITechDrawable.STYLE_01_CUSTOM, sb);
      debugStateStyleFlag(d, ITechDrawable.STYLE_02_CUSTOM, sb);
      debugStateStyleFlag(d, ITechDrawable.STYLE_03_MARKED, sb);
      debugStateStyleFlag(d, ITechDrawable.STYLE_04_GREYED, sb);
      debugStateStyleFlag(d, ITechDrawable.STYLE_05_SELECTED, sb);
      debugStateStyleFlag(d, ITechDrawable.STYLE_06_FOCUSED_KEY, sb);
      debugStateStyleFlag(d, ITechDrawable.STYLE_07_FOCUSED_POINTER, sb);
      debugStateStyleFlag(d, ITechDrawable.STYLE_08_PRESSED, sb);
      debugStateStyleFlag(d, ITechDrawable.STYLE_09_DRAGGED, sb);
      debugStateStyleFlag(d, ITechDrawable.STYLE_10_DRAGGED_OVER, sb);
      debugStateStyleFlag(d, ITechDrawable.STYLE_11_CUSTOM, sb);
      debugStateStyleFlag(d, ITechDrawable.STYLE_12_CUSTOM, sb);
      if (sb.getCount() == oc) {
         sb.reverse(2);
      } else {
         sb.reverse(1);
         sb.append("]");
      }
   }

   //#mdebug
   public static String toStringBehavior(IDrawable d, Dctx sb) {
      sb.append("Behaviors=[");
      int oc = sb.getCount();
      debugBehaviorFlag(d, ITechDrawable.BEHAVIOR_01_NOT_SELECTABLE, sb);
      debugBehaviorFlag(d, ITechDrawable.BEHAVIOR_02_EMPTY, sb);
      debugBehaviorFlag(d, ITechDrawable.BEHAVIOR_03_ROOT_KEY_INPUT, sb);
      debugBehaviorFlag(d, ITechDrawable.BEHAVIOR_04_CONTENT_ONLY, sb);
      debugBehaviorFlag(d, ITechDrawable.BEHAVIOR_05_ANIMATING, sb);
      debugBehaviorFlag(d, ITechDrawable.BEHAVIOR_06_ANIMATING_ENTRY, sb);
      debugBehaviorFlag(d, ITechDrawable.BEHAVIOR_07_ANIMATING_EXIT, sb);
      debugBehaviorFlag(d, ITechDrawable.BEHAVIOR_09_RGB_DRAWING, sb);
      debugBehaviorFlag(d, ITechDrawable.BEHAVIOR_10_FORCE_CACHING, sb);
      debugBehaviorFlag(d, ITechDrawable.BEHAVIOR_11_DISABLE_CACHING, sb);
      debugBehaviorFlag(d, ITechDrawable.BEHAVIOR_12_ZERO_COORDINATE, sb);
      debugBehaviorFlag(d, ITechDrawable.BEHAVIOR_13_REPAINT_PARENT_STYLE, sb);
      debugBehaviorFlag(d, ITechDrawable.BEHAVIOR_08_SIZE_RELATIVE_TO_CHILDREN, sb);
      debugBehaviorFlag(d, ITechDrawable.BEHAVIOR_20_FULL_CANVAS_W, sb);
      debugBehaviorFlag(d, ITechDrawable.BEHAVIOR_21_, sb);
      debugBehaviorFlag(d, ITechDrawable.BEHAVIOR_24_PARENT_CHILD_OVERLAP, sb);
      debugBehaviorFlag(d, ITechDrawable.BEHAVIOR_25_CONTAINER, sb);
      debugBehaviorFlag(d, ITechDrawable.BEHAVIOR_26_NAV_VERTICAL, sb);
      debugBehaviorFlag(d, ITechDrawable.BEHAVIOR_27_NAV_HORIZONTAL, sb);
      debugBehaviorFlag(d, ITechDrawable.BEHAVIOR_28_NAV_SELECTABLE, sb);

      if (sb.getCount() != oc) {
         sb.reverse(1);
      }
      sb.append("]");
      return sb.toString();
   }

   public static String toStringBehavior(int beh) {
      switch (beh) {
         case ITechDrawable.BEHAVIOR_01_NOT_SELECTABLE:
            return "NotSelectable";
         case ITechDrawable.BEHAVIOR_02_EMPTY:
            return "Empty";
         case ITechDrawable.BEHAVIOR_03_ROOT_KEY_INPUT:
            return "RootKeyInput";
         case ITechDrawable.BEHAVIOR_05_ANIMATING:
            return "Animating";
         case ITechDrawable.BEHAVIOR_06_ANIMATING_ENTRY:
            return "AnimatingEntry";
         case ITechDrawable.BEHAVIOR_07_ANIMATING_EXIT:
            return "AnimatingExit";
         case ITechDrawable.BEHAVIOR_09_RGB_DRAWING:
            return "RgbDrawing";
         case ITechDrawable.BEHAVIOR_10_FORCE_CACHING:
            return "ForceCaching";
         case ITechDrawable.BEHAVIOR_11_DISABLE_CACHING:
            return "DisableCaching";
         case ITechDrawable.BEHAVIOR_12_ZERO_COORDINATE:
            return "ZeroCoordinate";
         case ITechDrawable.BEHAVIOR_13_REPAINT_PARENT_STYLE:
            return "RepainParentStyle";
         case ITechDrawable.BEHAVIOR_08_SIZE_RELATIVE_TO_CHILDREN:
            return "";
         case ITechDrawable.BEHAVIOR_20_FULL_CANVAS_W:
            return "FullCanvasW";
         case ITechDrawable.BEHAVIOR_21_:
            return "FullCanvasH";
         case ITechDrawable.BEHAVIOR_26_NAV_VERTICAL:
            return "NavVert";
         case ITechDrawable.BEHAVIOR_27_NAV_HORIZONTAL:
            return "NavHoriz";
         case ITechDrawable.BEHAVIOR_28_NAV_SELECTABLE:
            return "NavSelect";

         default:
            return "Unknown Behaior";
      }
   }

   public static String toStringEvent(int event) {
      switch (event) {
         case ITechDrawable.EVENT_01_NOTIFY_SHOW:
            return "NotifyShow";
         case ITechDrawable.EVENT_03_KEY_FOCUS_GAIN:
            return "KeyFocusGain";
         case ITechDrawable.EVENT_04_KEY_FOCUS_LOSS:
            return "KEY_FOCUS_LOSS";
         case ITechDrawable.EVENT_02_NOTIFY_HIDE:
            return "HIDE";
         case ITechDrawable.EVENT_05_ANIM_FINISHED:
            return "ANIM_FINISHED";
         case ITechDrawable.EVENT_06_ANIM_STARTED:
            return "AnimStart";
         case ITechDrawable.EVENT_13_KEY_EVENT:
            return "KeyEvent";
         case ITechDrawable.EVENT_12_SIZE_CHANGED:
            return "SizeChanged";
         case ITechDrawable.EVENT_14_POINTER_EVENT:
            return "PointerEvent";

         default:
            return "UNKNOWN_EVENT" + event;
      }
   }

   public static String toStringState(int state) {
      switch (state) {
         case ITechDrawable.STATE_14_CACHED:
            return "Cached";
         case ITechDrawable.STATE_01_DRAWING:
            return "Drawing";
         case ITechDrawable.STATE_02_DRAWN:
            return "Drawn";
         case ITechDrawable.STATE_03_HIDDEN:
            return "Hidden";
         case ITechDrawable.STATE_04_DRAWN_OUTSIDE:
            return "DrawnOutside";
         case ITechDrawable.STATE_05_LAYOUTED:
            return "Layouted";
         case ITechDrawable.STATE_06_STYLED:
            return "Styled";
         case ITechDrawable.STATE_07_CACHE_INVALIDATED:
            return "Modified";
         case ITechDrawable.STATE_09_TRIMMED:
            return "Trimmed";
         case ITechDrawable.STATE_10_CLIPPED:
            return "Clipped";
         case ITechDrawable.STATE_11_ANIMATING:
            return "Animating";
         case ITechDrawable.STATE_12_APPEARING:
            return "Appearing";
         case ITechDrawable.STATE_13_DISAPPEARING:
            return "Disappearing";
         case ITechDrawable.STATE_15_EMPTY:
            return "Empty";
         case ITechDrawable.STATE_16_TRANSLUCENT:
            return "Translucent";
         case ITechDrawable.STATE_17_OPAQUE:
            return "Opaque";
         case ITechDrawable.STATE_19_HIDDEN_OVER:
            return "HiddenOver";
         case ITechDrawable.STATE_20_ANIMATED_FULL_HIDDEN:
            return "HiddenAnim";
         case ITechDrawable.STATE_21_ANIMATED_LAYERS:
            return "21AnimatedLayer";
         case ITechDrawable.STATE_22_ANIMATED_CONTENT_HIDDEN:
            return "22AnimatedContentHidden";
         case ITechDrawable.STATE_23_RELATIVE_TOPOLOGY:
            return "23RelativeTopology";
         case ITechDrawable.STATE_24_HOLED:
            return "24Holed";
         case ITechDrawable.STATE_25_AGGREGATE:
            return "25Aggregate";
         case ITechDrawable.STATE_26_POSITIONED:
            return "Positioned";
         case ITechDrawable.STATE_27_OVERLAYED:
            return "27Overlyaed";
         case ITechDrawable.STATE_28_NOT_CONTAINED_IN_PARENT_AREA:
            return "28OutsideParentArea";
         case ITechDrawable.STATE_29_CLIPPED:
            return "29Clipped";
         case ITechDrawable.STATE_30_LAYOUTING:
            return "Layouting";
         case ITechDrawable.STATE_31_STYLE_CLASS_REFRESH:
            return "31";
         case ITechDrawable.STATE_32_CHANGED:
            return "32";
         default:
            return "Unknown State";
      }
   }

   public static String toStringStates(IDrawable d) {
      Dctx dc = new Dctx(d.toStringGetUCtx());
      toStringStates(d, dc);
      return dc.toString();
   }

   public static String toStringStates(IDrawable d, Dctx sb) {
      sb.append("States=[");
      int oc = sb.getCount();
      debugStateFlag(d, ITechDrawable.STATE_14_CACHED, sb);
      debugStateFlag(d, ITechDrawable.STATE_01_DRAWING, sb);
      debugStateFlag(d, ITechDrawable.STATE_02_DRAWN, sb);
      debugStateFlag(d, ITechDrawable.STATE_03_HIDDEN, sb);
      debugStateFlag(d, ITechDrawable.STATE_04_DRAWN_OUTSIDE, sb);
      debugStateFlag(d, ITechDrawable.STATE_05_LAYOUTED, sb);
      debugStateFlag(d, ITechDrawable.STATE_06_STYLED, sb);
      debugStateFlag(d, ITechDrawable.STATE_07_CACHE_INVALIDATED, sb);
      debugStateFlag(d, ITechDrawable.STATE_09_TRIMMED, sb);
      debugStateFlag(d, ITechDrawable.STATE_10_CLIPPED, sb);
      debugStateFlag(d, ITechDrawable.STATE_11_ANIMATING, sb);
      debugStateFlag(d, ITechDrawable.STATE_12_APPEARING, sb);
      debugStateFlag(d, ITechDrawable.STATE_13_DISAPPEARING, sb);
      debugStateFlag(d, ITechDrawable.STATE_15_EMPTY, sb);
      debugStateFlag(d, ITechDrawable.STATE_16_TRANSLUCENT, sb);
      debugStateFlag(d, ITechDrawable.STATE_17_OPAQUE, sb);
      debugStateFlag(d, ITechDrawable.STATE_19_HIDDEN_OVER, sb);
      debugStateFlag(d, ITechDrawable.STATE_20_ANIMATED_FULL_HIDDEN, sb);
      debugStateFlag(d, ITechDrawable.STATE_21_ANIMATED_LAYERS, sb);
      debugStateFlag(d, ITechDrawable.STATE_22_ANIMATED_CONTENT_HIDDEN, sb);
      debugStateFlag(d, ITechDrawable.STATE_23_RELATIVE_TOPOLOGY, sb);
      debugStateFlag(d, ITechDrawable.STATE_24_HOLED, sb);
      debugStateFlag(d, ITechDrawable.STATE_25_AGGREGATE, sb);
      debugStateFlag(d, ITechDrawable.STATE_26_POSITIONED, sb);
      debugStateFlag(d, ITechDrawable.STATE_27_OVERLAYED, sb);
      debugStateFlag(d, ITechDrawable.STATE_28_NOT_CONTAINED_IN_PARENT_AREA, sb);
      debugStateFlag(d, ITechDrawable.STATE_29_CLIPPED, sb);
      debugStateFlag(d, ITechDrawable.STATE_30_LAYOUTING, sb);
      debugStateFlag(d, ITechDrawable.STATE_31_STYLE_CLASS_REFRESH, sb);
      debugStateFlag(d, ITechDrawable.STATE_32_CHANGED, sb);
      if (sb.getCount() != oc) {
         sb.reverse(1);
      }
      sb.append("]");
      return sb.toString();
   }

   private static void debugViewBehavior(ViewDrawable d, int flag, Dctx sb) {
      if (d.hasViewFlag(flag)) {
         sb.append(toStringViewBehavior(flag));
         sb.append(" ");
      }
   }

   private static void debugViewStateFlag(ViewDrawable d, int flag, Dctx sb) {
      if (d.hasViewFlag(flag)) {
         sb.append(debugViewState(flag));
         sb.append(" ");
      }
   }

   public static void toStringViewBehavior(ViewDrawable d, Dctx sb) {
      sb.append("ViewBehavior=[");
      int oc = sb.getCount();
      debugViewBehavior(d, ITechViewDrawable.VIEW_GENE_28_NOT_SCROLLABLE, sb);
      debugViewBehavior(d, ITechViewDrawable.VIEW_GENE_29_SHRINKABLE_W, sb);
      debugViewBehavior(d, ITechViewDrawable.VIEW_GENE_30_SHRINKABLE_H, sb);
      debugViewBehavior(d, ITechViewDrawable.VIEW_GENE_31_EXPANDABLE_W, sb);
      debugViewBehavior(d, ITechViewDrawable.VIEW_GENE_32_EXPANDABLE_H, sb);

      if (sb.getCount() != oc) {
         sb.reverse(1);
      }
      sb.append("]");
   }

   public static String toStringViewFlag(int flag) {
      switch (flag) {
         case ITechViewDrawable.VIEW_GENE_29_SHRINKABLE_W:
            return "Shrinkable_W";
         case ITechViewDrawable.VIEW_GENE_32_EXPANDABLE_H:
            return "Expandable_H";
         case ITechViewDrawable.VIEW_GENE_30_SHRINKABLE_H:
            return "Shrinkable_H";
         case ITechViewDrawable.VIEW_GENE_31_EXPANDABLE_W:
            return "Expandable_W";
         default:
            return "Unknown";
      }
   }

   public static String debugViewState(int state) {
      switch (state) {
         case ITechViewDrawable.VIEWSTATE_01_CLIP:
            return "Clip";
         case ITechViewDrawable.VIEWSTATE_02_REPAINTING_CONTENT:
            return "RepaintContent";
         case ITechViewDrawable.VIEWSTATE_06_NO_EAT_W_MUST_EXPAND:
            return "ExpandW";
         case ITechViewDrawable.VIEWSTATE_07_NO_EAT_H_MUST_EXPAND:
            return "ExpandH";
         case ITechViewDrawable.VIEWSTATE_10_CONTENT_PW_VIEWPORT_DW:
            return "ContentWFollows";
         case ITechViewDrawable.VIEWSTATE_11_CONTENT_PH_VIEWPORT_DH:
            return "ContentHFollow";
         case ITechViewDrawable.VIEWSTATE_12_CONTENT_W_DEPENDS_VIEWPORT:
            return "ContentWDepends";
         case ITechViewDrawable.VIEWSTATE_13_CONTENT_H_DEPENDS_VIEWPORT:
            return "ContentHDepends";
         case ITechViewDrawable.VIEWSTATE_14_LOGICAL_WIDTH:
            return "LogicalW";
         case ITechViewDrawable.VIEWSTATE_15_LOGICAL_HEIGHT:
            return "LogicalH";
         case ITechViewDrawable.VIEWSTATE_16_SHRANK_W:
            return "ShrankW";
         case ITechViewDrawable.VIEWSTATE_17_SHRANK_H:
            return "ShrankH";
         case ITechViewDrawable.VIEWSTATE_18_EXPANDED_W:
            return "ExpandedW";
         case ITechViewDrawable.VIEWSTATE_19_EXPANDED_H:
            return "ExpandedH";
         default:
            return "Unknown";
      }
   }

   public static void toStringViewStates(ViewDrawable d, Dctx sb) {
      sb.append("ViewState=[");
      int oc = sb.getCount();
      debugViewStateFlag(d, ITechViewDrawable.VIEWSTATE_01_CLIP, sb);
      debugViewStateFlag(d, ITechViewDrawable.VIEWSTATE_02_REPAINTING_CONTENT, sb);
      debugViewStateFlag(d, ITechViewDrawable.VIEWSTATE_06_NO_EAT_W_MUST_EXPAND, sb);
      debugViewStateFlag(d, ITechViewDrawable.VIEWSTATE_07_NO_EAT_H_MUST_EXPAND, sb);
      debugViewStateFlag(d, ITechViewDrawable.VIEWSTATE_10_CONTENT_PW_VIEWPORT_DW, sb);
      debugViewStateFlag(d, ITechViewDrawable.VIEWSTATE_11_CONTENT_PH_VIEWPORT_DH, sb);
      debugViewStateFlag(d, ITechViewDrawable.VIEWSTATE_12_CONTENT_W_DEPENDS_VIEWPORT, sb);
      debugViewStateFlag(d, ITechViewDrawable.VIEWSTATE_13_CONTENT_H_DEPENDS_VIEWPORT, sb);
      debugViewStateFlag(d, ITechViewDrawable.VIEWSTATE_14_LOGICAL_WIDTH, sb);
      debugViewStateFlag(d, ITechViewDrawable.VIEWSTATE_15_LOGICAL_HEIGHT, sb);
      debugViewStateFlag(d, ITechViewDrawable.VIEWSTATE_16_SHRANK_W, sb);
      debugViewStateFlag(d, ITechViewDrawable.VIEWSTATE_17_SHRANK_H, sb);
      debugViewStateFlag(d, ITechViewDrawable.VIEWSTATE_18_EXPANDED_W, sb);
      debugViewStateFlag(d, ITechViewDrawable.VIEWSTATE_19_EXPANDED_H, sb);

      if (sb.getCount() != oc) {
         sb.reverse(1);
      }
      sb.append("]");
   }

   public static String toStringTableType(int type) {
      switch (type) {
         case IBOTablePolicy.TABLE_TYPE_2GENERIC_ROW:
            return "GENERIC_ROW";
         case IBOTablePolicy.TABLE_TYPE_1GENERIC_COL:
            return "GENERIC_COL";
         case IBOTablePolicy.TABLE_TYPE_4FLOW_ROW:
            return "FLOW_ROW";
         case IBOTablePolicy.TABLE_TYPE_3FLOW_COL:
            return "FLOW_COL";
         case IBOTablePolicy.TABLE_TYPE_0STRICT:
            return "STRICT";
         default:
            return "UNKOWN";
      }

   }

   public static String toStringPSelectMode(int value) {
      switch (value) {
         case IBOTableView.PSELECT_0_NONE:
            return "SELECT_NONE";
         case IBOTableView.PSELECT_1_PRESS:
            return "PRESS";
         case IBOTableView.PSELECT_2_PRESS_RELEASE:
            return "PRESS_RELEASE";
         case IBOTableView.PSELECT_4_PRESS_DOUBLE:
            return "PRESS_DOUBLE";
         case IBOTableView.PSELECT_3_SELECTED_PRESS:
            return "PRESS_SELECTED";
         case IBOTableView.PSELECT_5_PRESS_RELEASE_DOUBLE:
            return "PRESS_RELEASE_DOUBLE";
         default:
            return "Unknown" + value;
      }
   }
}
