package pasa.cbentley.framework.gui.src4.ctx;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.ToStringStaticBase;
import pasa.cbentley.framework.gui.src4.anim.ITechAnim;
import pasa.cbentley.framework.gui.src4.core.ViewDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechViewDrawable;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOTablePolicy;
import pasa.cbentley.framework.gui.src4.table.interfaces.ITechCell;
import pasa.cbentley.framework.gui.src4.table.interfaces.ITechTable;
import pasa.cbentley.framework.gui.src4.tech.ITechLinks;
import pasa.cbentley.framework.gui.src4.tech.ITechStringDrawable;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;

public class ToStringStaticGui extends ToStringStaticBase {

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

   public static void debugBehaviorFlag(IDrawable d, int flag, Dctx sb) {
      if (d.hasBehavior(flag)) {
         sb.append(toStringBehavior(flag));
         sb.append(' ');
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

   public static String debugPlanetStruct(int val) {
      switch (val) {
         case ITechViewPane.PLANET_MODE_0_EAT:
            return "Eat";
         case ITechViewPane.PLANET_MODE_1_EXPAND:
            return "Expand";
         case ITechViewPane.PLANET_MODE_2_OVERLAY:
            return "Overlay";
         case ITechViewPane.PLANET_MODE_3_GHOST:
            return "Immaterial";
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

   public static String toStringStateStyle(int sty) {
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
         sb.append(toStringStateStyle(flag));
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

   private static void debugViewBehavior(ViewDrawable d, int flag, Dctx sb) {
      if (d.hasFlagView(flag)) {
         sb.append(toStringViewFlagGene(flag));
         sb.append(" ");
      }
   }

   private static void debugViewStateFlag(ViewDrawable d, int flag, Dctx sb) {
      if (d.hasFlagView(flag)) {
         sb.append(toStringViewState(flag));
         sb.append(" ");
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

   public static String toStringCompetitionType(int value) {
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

   public static String toStringDebugMode(int mode) {
      switch (mode) {
         case ITechCanvasDrawable.DEBUG_0_NONE:
            return "None";
         case ITechCanvasDrawable.DEBUG_1_LIGHT:
            return "Light";
         case ITechCanvasDrawable.DEBUG_2_COMPLETE_1LINE:
            return "Complete";
         case ITechCanvasDrawable.DEBUG_3_COMPLETE_2LINES:
            return "2Complete";
         default:
            return "Unknown" + mode;
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

   /**
    * Debug name for link IDs such as 
    * <li>{@link ITechLinks#LINK_42_BO_STRING_EDIT}
    * <li>{@link ITechLinks#LINK_50_STYLE_SCROLLBAR_BLOCK_BG}
    * <li>{@link ITechLinks#LINK_65_STYLE_VIEWPANE}
    * 
    * @param link
    * @return
    */
   public static String toStringLinkStatic(int link) {
      switch (link) {
         case ITechLinks.LINK_10_BASE_TECH:
            return "BaseTech";
         case ITechLinks.LINK_42_BO_STRING_EDIT:
            return "BO_StringEdit";
         case ITechLinks.LINK_41_BO_STRING_DATA:
            return "BO_String";
         case ITechLinks.LINK_43_CARET_FIGURE:
            return "Figure_Caret";
         case ITechLinks.LINK_45_STRING_LIST_VIEW:
            return "StringListView";
         case ITechLinks.LINK_49_FIG_SCROLLBAR_WRAPPER:
            return "ScrollBar_FigureWrapper";
         case ITechLinks.LINK_50_STYLE_SCROLLBAR_BLOCK_BG:
            return "StyleClass_Scrollbar_BlockBg";
         case ITechLinks.LINK_51_STYLE_SCROLLBAR_BLOCK_FIG:
            return "StyleClass_Scrollbar_BlockFig";
         case ITechLinks.LINK_52_STYLE_SCROLLBAR_TOP_LEFT_WRAPPER:
            return "StyleClass_Scrollbar_TopLeft_Wrapper";
         case ITechLinks.LINK_53_STYLE_SCROLLBAR_BOT_RIGHT_WRAPPER:
            return "StyleClass_Scrollbar_BotRight_Wrapper";
         case ITechLinks.LINK_58_STYLE_VIEWPANE_HOLE_HEADER:
            return "StyleClass_Hole_Headers";
         case ITechLinks.LINK_59_STYLE_VIEWPANE_HOLE_SB:
            return "StyleClass_Hole_Scrollbars";
         case ITechLinks.LINK_64_STYLE_VIEWPORT:
            return "StyleClass_ViewPort";
         case ITechLinks.LINK_65_STYLE_VIEWPANE:
            return "StyleClass_ViewPane";
         case ITechLinks.LINK_66_BO_VIEWPANE:
            return "BO_ViewPane";
         case ITechLinks.LINK_68_BO_V_SCROLLBAR:
            return "BO_Scrollbar_Vertical";
         case ITechLinks.LINK_69_BO_H_SCROLLBAR:
            return "BO_Scrollbar_Horizontal";
         case ITechLinks.LINK_71_STYLE_VIEWPANE_H_SCROLLBAR:
            return "StyleClass_Scrollbar_Horizontal";
         case ITechLinks.LINK_72_STYLE_VIEWPANE_V_SCROLLBAR:
            return "StyleClass_Scrollbar_Vertical";
         case ITechLinks.LINK_74_STYLE_CLASS_MENU:
            return "StyleClass_menubar";
         case ITechLinks.LINK_75_MENU_BAR_TECH:
            return "BO_MenuBar";
         case ITechLinks.LINK_80_BO_TABLEVIEW:
            return "BO_Table";
         case ITechLinks.LINK_81_STYLE_CLASS_TABLE_CELL:
            return "StyleClass_Table_Cell";
         case ITechLinks.LINK_82_STYLE_TABLE_COL_TITLE:
            return "StyleClass_Table_Column";
         case ITechLinks.LINK_83_STYLE_TABLE_ROW_TITLE:
            return "StyleClass_Table_Row";
         case ITechLinks.LINK_84_STYLE_TABLE_OVERLAY_FIGURE:
            return "StyleClass_Table_Overlay_Figure";
         case ITechLinks.LINK_85_STYLE_TABLE_TITLE_TOP:
            return "Table Title Top";
         case ITechLinks.LINK_86_STYLE_TABLE_TITLE_BOT:
            return "Table Title Bottom";
         case ITechLinks.LINK_90_STRING_EDIT_CONTROL:
            return "BO_StringEditControl";
         default:
            return "UnknownLink " + link;
      }
   }

   public static String toStringPolicyType(final int type) {
      switch (type) {
         case ITechCell.CELL_3_FILL_STRONG:
            return "FILL_STRONG";
         case ITechCell.CELL_4_FILL_AVERAGE:
            return "FILL_AVERAGE";
         case ITechCell.CELL_5_FILL_WEAK:
            return "FILL_WEAK";
         case ITechCell.CELL_1_EXPLICIT_SET:
            return "EXPLICIT";
         case ITechCell.CELL_2_RATIO:
            return "RATIO";
         case ITechCell.CELL_0_IMPLICIT_SET:
            return "IMPLICIT";
         default:
            return "UNKNOWN" + type;
      }
   }

   public static String toStringPSelectMode(int value) {
      switch (value) {
         case ITechTable.PSELECT_0_NONE:
            return "SELECT_NONE";
         case ITechTable.PSELECT_1_PRESS:
            return "PRESS";
         case ITechTable.PSELECT_2_PRESS_RELEASE:
            return "PRESS_RELEASE";
         case ITechTable.PSELECT_4_PRESS_DOUBLE:
            return "PRESS_DOUBLE";
         case ITechTable.PSELECT_3_SELECTED_PRESS:
            return "PRESS_SELECTED";
         case ITechTable.PSELECT_5_PRESS_RELEASE_DOUBLE:
            return "PRESS_RELEASE_DOUBLE";
         default:
            return "Unknown" + value;
      }
   }

   public static String toStringScrollbarMode(int t) {
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

   public static String toStringScrollMove(int t) {
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

   public static String toStringScrollPartial(int t) {
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

   public static String toStringScrollVisual(int t) {
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
            return "AnimatedLayer";
         case ITechDrawable.STATE_22_ANIMATED_CONTENT_HIDDEN:
            return "AnimatedContentHidden";
         case ITechDrawable.STATE_23_RELATIVE_TOPOLOGY:
            return "RelativeTopology";
         case ITechDrawable.STATE_24_HOLED:
            return "Holed";
         case ITechDrawable.STATE_25_AGGREGATE:
            return "Aggregate";
         case ITechDrawable.STATE_26_POSITIONED:
            return "Positioned";
         case ITechDrawable.STATE_27_OVERLAYED:
            return "Overlyaed";
         case ITechDrawable.STATE_28_NOT_CONTAINED_IN_PARENT_AREA:
            return "OutsideParentArea";
         case ITechDrawable.STATE_29_CLIPPED:
            return "Clipped";
         case ITechDrawable.STATE_30_LAYOUTING:
            return "Layouting";
         case ITechDrawable.STATE_31_STYLE_CLASS_REFRESH:
            return "31StyleClassRefresh";
         case ITechDrawable.STATE_32_CHANGED:
            return "32Changed";
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

   public static String toStringStringPreset(int type) {
      switch (type) {
         case ITechStringDrawable.PRESET_CONFIG_0_NONE:
            return "None";
         case ITechStringDrawable.PRESET_CONFIG_1_TITLE:
            return "Title";
         case ITechStringDrawable.PRESET_CONFIG_3_SCROLL_V:
            return "ScrollV";
         case ITechStringDrawable.PRESET_CONFIG_2_SCROLL_H:
            return "ScrollH";
         case ITechStringDrawable.PRESET_CONFIG_4_NATURAL_NO_WRAP:
            return "NaturalNoWrap";
         case ITechStringDrawable.PRESET_CONFIG_5_CHAR:
            return "Char";
         default:
            throw new IllegalArgumentException("" + type);
      }
   }

   public static String toStringTableType(int type) {
      switch (type) {
         case IBOTablePolicy.TABLE_TYPE_2_GENERIC_ROW:
            return "GENERIC_ROW";
         case IBOTablePolicy.TABLE_TYPE_1_GENERIC_COL:
            return "GENERIC_COL";
         case IBOTablePolicy.TABLE_TYPE_4_FLOW_ROW:
            return "FLOW_ROW";
         case IBOTablePolicy.TABLE_TYPE_3_FLOW_COL:
            return "FLOW_COL";
         case IBOTablePolicy.TABLE_TYPE_0_STRICT:
            return "STRICT";
         default:
            return "UNKOWN";
      }

   }

   public static String toStringType(final int type) {
      switch (type) {
         case ITechCell.TYPE_0_GENERIC:
            return "GENERIC";
         case ITechCell.TYPE_2_RATIO:
            return "RATIO";
         case ITechCell.TYPE_1_FLOW:
            return "FLOW";
         default:
            return "UNKNOWN " + type;
      }
   }


   public static String toStringTypeBO(final int type) {
      switch (type) {
         case IBOTypesGui.TYPE_GUI_00_VIEWPANE:
            return "ViewPaneTech";
         case IBOTypesGui.TYPE_GUI_01_SCROLLBAR:
            return "ScrollbarTech";
         case IBOTypesGui.TYPE_GUI_02_TABLE:
            return "TableTech";
         case IBOTypesGui.TYPE_GUI_03_TABLE_GENETICS:
            return "Genetics";
         case IBOTypesGui.TYPE_GUI_05_CELL_SPANNING:
            return "Spanning";
         case IBOTypesGui.TYPE_GUI_06_CELL_POLICY:
            return "CellPolicy";
         default:
            return null;
      }
   }

   public static String toStringViewFlagGene(int state) {
      switch (state) {
         case ITechViewDrawable.FLAG_GENE_28_NOT_SCROLLABLE:
            return "NotScrollable";
         case ITechViewDrawable.FLAG_GENE_29_SHRINKABLE_W:
            return "ShrinkableW";
         case ITechViewDrawable.FLAG_GENE_30_SHRINKABLE_H:
            return "ShrinkableH";
         case ITechViewDrawable.FLAG_GENE_31_EXPANDABLE_W:
            return "ExpandableW";
         case ITechViewDrawable.FLAG_GENE_32_EXPANDABLE_H:
            return "ExpandableH";
         default:
            return "Unknown" + state;
      }
   }

   public static void toStringViewGenes(ViewDrawable d, Dctx sb) {
      sb.append("ViewBehavior=[");
      int oc = sb.getCount();
      debugViewBehavior(d, ITechViewDrawable.FLAG_GENE_28_NOT_SCROLLABLE, sb);
      debugViewBehavior(d, ITechViewDrawable.FLAG_GENE_29_SHRINKABLE_W, sb);
      debugViewBehavior(d, ITechViewDrawable.FLAG_GENE_30_SHRINKABLE_H, sb);
      debugViewBehavior(d, ITechViewDrawable.FLAG_GENE_31_EXPANDABLE_W, sb);
      debugViewBehavior(d, ITechViewDrawable.FLAG_GENE_32_EXPANDABLE_H, sb);

      if (sb.getCount() != oc) {
         sb.reverse(1);
      }
      sb.append("]");
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

   public static String toStringViewState(int state) {
      switch (state) {
         case ITechViewDrawable.FLAG_VSTATE_01_CLIP:
            return "Clip";
         case ITechViewDrawable.FLAG_VSTATE_02_REPAINTING_CONTENT:
            return "RepaintContent";
         case ITechViewDrawable.FLAG_VSTATE_03_VIEWPANE_OVERLAY:
            return "ViewPaneOverlay";
         case ITechViewDrawable.FLAG_VSTATE_04_NO_CONTENT_STATE:
            return "NoContentState";
         case ITechViewDrawable.FLAG_VSTATE_05_:
            return "";
         case ITechViewDrawable.FLAG_VSTATE_06_NO_EAT_W_MUST_EXPAND:
            return "ExpandW";
         case ITechViewDrawable.FLAG_VSTATE_07_NO_EAT_H_MUST_EXPAND:
            return "ExpandH";
         case ITechViewDrawable.FLAG_VSTATE_08_PREF_SIZE_COMPUTED:
            return "PrefSizeComputed";
         case ITechViewDrawable.FLAG_VSTATE_09_:
            return "";
         case ITechViewDrawable.FLAG_VSTATE_10_CONTENT_PW_VIEWPORT_DW:
            return "ContentWFollows";
         case ITechViewDrawable.FLAG_VSTATE_11_CONTENT_PH_VIEWPORT_DH:
            return "ContentHFollow";
         case ITechViewDrawable.FLAG_VSTATE_12_CONTENT_W_DEPENDS_VIEWPORT:
            return "ContentWDepends";
         case ITechViewDrawable.FLAG_VSTATE_13_CONTENT_H_DEPENDS_VIEWPORT:
            return "ContentHDepends";
         case ITechViewDrawable.FLAG_VSTATE_14_LOGICAL_WIDTH:
            return "LogicalW";
         case ITechViewDrawable.FLAG_VSTATE_15_LOGICAL_HEIGHT:
            return "LogicalH";
         case ITechViewDrawable.FLAG_VSTATE_16_SHRANK_W:
            return "ShrankW";
         case ITechViewDrawable.FLAG_VSTATE_17_SHRANK_H:
            return "ShrankH";
         case ITechViewDrawable.FLAG_VSTATE_18_EXPANDED_W:
            return "ExpandedW";
         case ITechViewDrawable.FLAG_VSTATE_19_EXPANDED_H:
            return "ExpandedH";
         case ITechViewDrawable.FLAG_VSTATE_20_:
            return "";
         case ITechViewDrawable.FLAG_VSTATE_21_VP_STYLE_DRAWN:
            return "VpStyleDrawn";
         case ITechViewDrawable.FLAG_VSTATE_22_SCROLLED:
            return "Scrolled";
         case ITechViewDrawable.FLAG_VSTATE_23_SCROLLED_H:
            return "ScrolledH";
         case ITechViewDrawable.FLAG_VSTATE_24_SCROLLED_V:
            return "ScrolledV";
         case ITechViewDrawable.FLAG_VSTATE_25_HEADERED:
            return "Headered";
         case ITechViewDrawable.FLAG_VSTATE_26_:
            return "";
         case ITechViewDrawable.FLAG_VSTATE_27_HEADER_TOP_PW:
            return "HeaderTopPw";
         default:
            return "Unknown";
      }
   }

   
   public static void toStringViewStates(ViewDrawable d, Dctx sb) {
      sb.append("ViewState=[");
      int oc = sb.getCount();
      debugViewStateFlag(d, ITechViewDrawable.FLAG_VSTATE_01_CLIP, sb);
      debugViewStateFlag(d, ITechViewDrawable.FLAG_VSTATE_02_REPAINTING_CONTENT, sb);
      debugViewStateFlag(d, ITechViewDrawable.FLAG_VSTATE_06_NO_EAT_W_MUST_EXPAND, sb);
      debugViewStateFlag(d, ITechViewDrawable.FLAG_VSTATE_07_NO_EAT_H_MUST_EXPAND, sb);
      debugViewStateFlag(d, ITechViewDrawable.FLAG_VSTATE_10_CONTENT_PW_VIEWPORT_DW, sb);
      debugViewStateFlag(d, ITechViewDrawable.FLAG_VSTATE_11_CONTENT_PH_VIEWPORT_DH, sb);
      debugViewStateFlag(d, ITechViewDrawable.FLAG_VSTATE_12_CONTENT_W_DEPENDS_VIEWPORT, sb);
      debugViewStateFlag(d, ITechViewDrawable.FLAG_VSTATE_13_CONTENT_H_DEPENDS_VIEWPORT, sb);
      debugViewStateFlag(d, ITechViewDrawable.FLAG_VSTATE_14_LOGICAL_WIDTH, sb);
      debugViewStateFlag(d, ITechViewDrawable.FLAG_VSTATE_15_LOGICAL_HEIGHT, sb);
      debugViewStateFlag(d, ITechViewDrawable.FLAG_VSTATE_16_SHRANK_W, sb);
      debugViewStateFlag(d, ITechViewDrawable.FLAG_VSTATE_17_SHRANK_H, sb);
      debugViewStateFlag(d, ITechViewDrawable.FLAG_VSTATE_18_EXPANDED_W, sb);
      debugViewStateFlag(d, ITechViewDrawable.FLAG_VSTATE_19_EXPANDED_H, sb);

      if (sb.getCount() != oc) {
         sb.reverse(1);
      }
      sb.append("]");
   }
}
