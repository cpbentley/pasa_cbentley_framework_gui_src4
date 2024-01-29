package pasa.cbentley.framework.gui.src4.interfaces;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.engine.RgbImage;
import pasa.cbentley.framework.gui.src4.anim.ITechAnim;
import pasa.cbentley.framework.gui.src4.anim.move.Move;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.canvas.TopologyDLayer;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.DrawableAnimator;
import pasa.cbentley.framework.gui.src4.core.ImageDrawable;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.core.ViewDrawable;
import pasa.cbentley.framework.gui.src4.utils.DrawableUtilz;

public interface ITechDrawable extends IByteObject {



   public static final int DTECH_BEHAVIOR_FLAG_B_1_EMPTY_HIDE    = 1 << 0;

   public static final int DTECH_BEHAVIOR_FLAG_B_2_EMPTY_DEF     = 1 << 1;

   /**
    * Genetical switches of the Drawable.
    */
   public static final int BEHAVIOR                              = 0;

   /**
    * When a TableView has a policy given to not select a cell, it sets
    * this flag. Thus the cell is not selected.
    */
   public static final int BEHAVIOR_01_NOT_SELECTABLE            = 1 << 0;

   /**
    * Allows a {@link IDrawable} to have 0 for dw,dh,pw,or ph.
    */
   public static final int BEHAVIOR_02_EMPTY                     = 1 << 1;

   /**
    * In Bottom Up device events mechanism, If set, The parent container will not receive the InputFlow.
    * Similar to a blocking {@link CmdCtx}.
    */
   public static final int BEHAVIOR_03_ROOT_KEY_INPUT            = 1 << 2;

   /**
    * When this flag is set, style is not drawn. only the content.
    * <br>
    * 
    */
   public static final int BEHAVIOR_04_CONTENT_ONLY              = 1 << 3;

   /**
    * Drawable has repetitive animations when active on screen.
    */
   public static final int BEHAVIOR_05_ANIMATING                 = 1 << 4;

   /**
    * Entry animation is defined for this drawable. <br>
    * Fade in or move in for example.
    * When behavior is live, the state {@link ITechDrawable#STATE_12_APPEARING} is set.
    */
   public static final int BEHAVIOR_06_ANIMATING_ENTRY           = 1 << 5;

   /**
    * Exit animation is defined for this drawable. <br>
    * Fade out or move out.
    * When behavior is live, the state {@link ITechDrawable#STATE_13_DISAPPEARING} is set.
    */
   public static final int BEHAVIOR_07_ANIMATING_EXIT            = 1 << 6;

   /**
    * Parent drawable dimensions are dependant on children.
    * <br>
    * When children have sizers with 
    * <li> {@link ISizer#LINK_0_PARENT}
    * <li> {@link ISizer#ET_TYPE_2_PARENT}
    * <br>
    * <br>
    * It becomes 
    * <li> {@link ISizer#ETALON_1_VIEWCONTEXT}
    * <li> {@link ISizer#ET_TYPE_5_VIEW_CONTEXT}
    * Parent drawable knows how to size and draw.
    */
   public static final int BEHAVIOR_08_SIZE_RELATIVE_TO_CHILDREN = 1 << 7;

   /**
    * Supports rgb drawing instead of primitive drawing
    * Not set by default<br>
    * Set by Rgb {@link ByteObject} figures.
    */
   public static final int BEHAVIOR_09_RGB_DRAWING               = 1 << 8;

   /**
    * Switch to enable/disable caching services.
    * <br>
    * <br>
    * Drawable may be heavy and complex. For those items, it is desirable to cache pixel data
    * for fast repetitive drawings. 
    * <br>
    * Most animations will convert {@link IDrawable} to a {@link RgbImage}. 
    * It is not always required but it is efficient for animations like {@link Move}.
    * <br>
    * <br>
    * Caching uses the cache directive. Therefore, if {@link ITechDrawable#CACHE_0_NONE} is set, no caching is done.
    * <li> {@link ITechDrawable#CACHE_0_NONE}
    * <li> {@link ITechDrawable#CACHE_1_CONTENT}
    * <li> {@link ITechDrawable#CACHE_2_FULL}
    * <li> {@link ITechDrawable#CACHE_3_BG_DECO}
    * 
    */
   public static final int BEHAVIOR_10_FORCE_CACHING             = 1 << 9;

   /**
    * Overrides all flag to disable caching. 
    * TODO set also a Global safe switch that apply to all drawables
    */
   public static final int BEHAVIOR_11_DISABLE_CACHING           = 1 << 10;

   /** 
    * In some case, dev wants a soft set(XY) where nothing is done until the draw method is called.
    * This is called {@link ITechDrawable#BEHAVIOR_12_ZERO_COORDINATE} behavior.
    */
   public static final int BEHAVIOR_12_ZERO_COORDINATE           = 1 << 11;

   /**
    * Repaint erase directive flag
    * <br>
    * When {@link GraphicsX} repaint flag is not FULL, asks parent {@link IDrawable} to repaint as well.
    * when partial repaint on drawable, asks to repaint the parent drawable if any.
    * <br>
    * TODO when Drawable request to be repainted, if it is not opaque,
    * we must force the repaint of the drawable located below? Usually the parent, BUT NOT necessarily.
    * MasterCanvas must use the boundary of Drawable and check which Drawable is currently
    * located there. Parent boundary must also be checked.
    * <br>
    * When Parent container lays its children without overlapping.
    * <br>
    * See {@link ITechDrawable#BEHAVIOR_24_PARENT_CHILD_OVERLAP}
    */
   public static final int BEHAVIOR_13_REPAINT_PARENT_STYLE      = 1 << 12;

   /**
    * Repaint erase directiv flag. Drawable draws {@link MasterCanvas} over its area
    */
   public static final int BEHAVIOR_14_REPAINT_MASTER_CANVAS     = 1 << 13;

   /**
    * Holed
    * Logical positioning is always done relative to Canvas, even when Parent is not null.
    * 
    */
   public static final int BEHAVIOR_15_CANVAS_POSITIONING        = 1 << 14;

   /**
    * Behaves like a Ghost.
    * <br>
    * <br>
    * Returns 0 for both {@link IDrawable#getDrawnHeight()} and {@link IDrawable#getDrawnWidth()}.
    * <br>
    * <br>
    * ScrollBar immateral must also returns
    * 
    * <br>
    */
   public static final int BEHAVIOR_16_IMMATERIAL                = 1 << 15;

   /**
    * When this s
    */
   public static final int BEHAVIOR_17_CLIP_CONTROLLED           = 1 << 16;

   /**
    * {@link IDrawable#init(int, int)} takes maximum width available as drawable width.
    * Overrides any given value
    */
   public static final int BEHAVIOR_20_FULL_CANVAS_W             = 1 << 19;

   /**
    * {@link IDrawable#init(int, int)} takes maximum height as drawable height
    */
   public static final int BEHAVIOR_21_                          = 1 << 20;

   /**
    * Content is supposed to be cached for repetitive draws.
    */
   public static final int BEHAVIOR_22_TIP_HEAVY_CONTENT         = 1 << 21;

   /**
    * Set when Parent wants to control event of their child drawable.
    * <br>
    * Internal flag. For instance there might be a Drawable in a TableView Title.
    * <br>
    * 
    */
   public static final int BEHAVIOR_23_PARENT_EVENT_CONTROL      = 1 << 22;

   /**
    * Sets when Drawable container such {@link PinBoardDrawable} draws its chilren in overlap.
    * <br>
    * In those cases, it uses a {@link TopologyDLayer} with different Zindex.
    * <br>
    * Non flat topologies require a {@link TopologyDLayer}.
    * <br>
    * {@link TableView} does not set this flag.
    */
   public static final int BEHAVIOR_24_PARENT_CHILD_OVERLAP      = 1 << 23;

   /**
    * When Drawable is supposed to contain other Drawables, i.e. is the parent
    * <br>
    * <li> {@link TableView}
    * <li> {@link PinBoardDrawable}
    * <li> {@link ViewDrawable} ? for {@link ScrollBar}
    */
   public static final int BEHAVIOR_25_CONTAINER                 = 1 << 24;

   /**
    * Set to tell that Drawable has inner vertical navigation.
    * <br>
    * Decided when {@link IDrawable#initSize()} is called.
    * <br>
    */
   public static final int BEHAVIOR_26_NAV_VERTICAL              = 1 << 25;

   /**
    * Set to tell that Drawable has inner horizontal navigation.
    * <br>
    * Decided when {@link IDrawable#initSize()} is called.
    * <br>
    */
   public static final int BEHAVIOR_27_NAV_HORIZONTAL            = 1 << 26;

   public static final int BEHAVIOR_28_NAV_SELECTABLE            = 1 << 27;

   public static final int BEHAVIOR_29_NAV_CLOCK_VERTICAL        = 1 << 28;

   public static final int BEHAVIOR_30_NAV_CLOCK_HORIZONTAL      = 1 << 29;

   public static final int BEHAVIOR_31_NAVIGATABLE               = 1 << 30;

   /**
    * No cache layer
    */
   public static final int CACHE_0_NONE                          = 0;

   /**
    * Cache layer is only caching drawable content
    * <br>
    * Actually this creates an {@link RgbImage} for the content
    */
   public static final int CACHE_1_CONTENT                       = 1;

   /**
    * Cache layer caches style and content
    */
   public static final int CACHE_2_FULL                          = 2;

   /**
    * Cache layer only host bg layers of the style
    * <br>
    * This creates a {@link RgbImage} of background layers.
    */
   public static final int CACHE_3_BG_DECO                       = 3;

   /**
    * Based on drawing times and number of primitive calls, cache different layers of Drawable automatically.
    * <br>
    * Basic strategy is to cache the whole drawable.
    */
   public static final int CACHE_4_AUTO                          = 4;

   /**
    * Set when Parent wants to control event of their child drawable
    */
   public static final int CTRL_01_PARENT                        = 1 << 0;

   public static final int CTRL_02_PARENT_POINTER_PRESS          = 1 << 1;

   public static final int CTRL_03_PARENT_POINTER_DRAG           = 1 << 2;

   public static final int CTRL_04_PARENT_POINTER_RELEASE        = 1 << 3;

   public static final int CTRL_05_PARENT_POINTER_MOVE           = 1 << 4;

   public static final int CTRL_06_PARENT_KEY                    = 1 << 5;

   /**
    * 3 different interpretations exist for the width and height values in the {@link IDrawable#init(int, int)} method.
    * <ol>
    * <li> <b>Positve</b>. The pixel size is fully known. It must be that value. Axis is fully constrained.
    * <li> <b>0</b>. The pixel size is fully computed based on content, capped to screen size. Axis is free.
    * <li> <b>Negative</b>. The size is logical. -2 means 2 lines for a StringDrawable. Axis is constrained.
    * </ol>
    * </p>
    */
   public static final int DIMENSION_API                         = 0;

   /**
    * Sent by {@link MasterCanvas} to {@link Drawable} when the drawable is loaded in a DLayer and thus its
    * {@link IDrawable#draw(GraphicsX)} method will be called in the next repaint cycle.
    * <br> 
    * <br>
    * Maybe called in a worker thread.
    * <br>
    * <br>
    * The default implementation of this method should manage internal state {@link ITechDrawable#STATE_03_HIDDEN} and animations hooks
    * for {@link ITechAnim#ANIM_TIME_1_ENTRY} and {@link ITechAnim#ANIM_TIME_0_MAIN} 
    * <br>
    * <br>
    */
   public static final int EVENT_01_NOTIFY_SHOW                  = 1;

   /**
    * Similar to {@link CustomItem#hideNotify} <br>
    * Called by the system to notify the item that it is now completely invisible,
    * when it previously had been at least partially visible. 
    * <br>
    * No further paint() calls will be made on this item until after a showNotify() has been called again. <br>
    * The default implementation of this method does nothing.
    * <br>
    * Use this event to unload data from memory. Makes heavy content garbage collectable.
    * <br>
    * A Focus Loss is always sent before a Notify Hide
    */
   public static final int EVENT_02_NOTIFY_HIDE                  = 2;

   /**
    * Event sent when key is and Drawable is inside the Key Event Chain.
    * <br>
    * Focus hierarchy
    * <br>
    * State {@link ITechDrawable#STYLE_06_FOCUSED_KEY} is managed with FOCUS events
    * <br>
    * <br>
    * When gain focus, it may check the History of focuses.
    */
   public static final int EVENT_03_KEY_FOCUS_GAIN               = 3;

   /**
    * Event sent when key is and {@link IDrawable} is set outside of the Key Event Chain.
    * <br>
    * <br>
    * State {@link ITechDrawable#STYLE_06_FOCUSED_KEY} is managed with FOCUS events 
    * <br>
    * <br>
    * When Losing focus, A Drawable may ask to which Drawable it goes.
    */
   public static final int EVENT_04_KEY_FOCUS_LOSS               = 4;

   /**
    * When an animation animating a Drawable is finished, it sends
    * this event to the Drawable which is thus able to change it states.
    */
   public static final int EVENT_05_ANIM_FINISHED                = 5;

   /**
    * Drawable gets this event when an animation is started in the Animated Thread
    * <br>
    * Just before drawing of the first frame.
    */
   public static final int EVENT_06_ANIM_STARTED                 = 6;

   /**
    * MouseOut
    */
   public static final int EVENT_07_POINTER_FOCUS_LOSS           = 7;

   /**
    * Event when the pointer comes over
    * On the PC, it is the mouse, on a phone device.
    * Sometimes, when a pop up menu comes, we want the pointer focus to follow the keyfocus
    * so the pointer is over the OK button. 
    * This is kind the MouseOver in Javascript.
    */
   public static final int EVENT_08_POINTER_FOCUS_GAIN           = 8;

   /**
    * Event sent when the IDrawable is about to be disposed. If it has any user view state
    * to save, it should do so.
    */
   public static final int EVENT_09_DISPOSED                     = 9;

   /**
    * Event parameter is the style state flag, plus a boolean
    * <br>
    * Other than the ones already defined.
    */
   public static final int EVENT_10_STYLE_STATE_CHANGE           = 10;

   public static final int EVENT_11_REFRESH_CLEAN                = 11;

   /**
    * Called just after the flag {@link ITechDrawable#STATE_05_LAYOUTED}
    * is set to true after the change
    */
   public static final int EVENT_12_SIZE_CHANGED                 = 12;

   public static final int EVENT_13_KEY_EVENT                    = 13;

   public static final int EVENT_14_POINTER_EVENT                = 14;

   /**
    * Drawable must compute a width
    */
   public static final int EVENT_15_INIT_SIZE_W                  = 15;

   public static final int EVENT_16_INIT_SIZE_H                  = 16;

   public static final int EVENT_17_ANIM_NEXT_TURN               = 17;

   public static final int HOLE_0_X                              = 0;

   public static final int HOLE_1_Y                              = 1;

   public static final int HOLE_2_W                              = 2;

   public static final int HOLE_3_H                              = 3;

   public static final int IMAGE_0_ALL                           = 0;

   public static final int IMAGE_1_CONTENT                       = 1;

   public static final int IMAGE_2_BG                            = 2;

   public static final int IMAGE_3_FG                            = 3;

   public static final int IMAGE_4_CONTENT_BG                    = 4;

   public static final int IMAGE_5_CONTENT_FG                    = 5;

   /**
    * Flag set when drawable is upped of something.
    */
   public static final int NAV_01_TOPO_UP                        = 1 << 0;

   /**
    * Flag set when drawable is downed of something.
    */
   public static final int NAV_02_TOPO_DOWN                      = 1 << 1;

   public static final int NAV_03_TOPO_LEFT                      = 1 << 2;

   public static final int NAV_04_TOPO_RIGHT                     = 1 << 3;

   /**
    * When set, any {@link IDrawable#navigateUp(InputConfig)} by this Drawable must be sent to the topology controller.
    * 
    */
   public static final int NAV_05_OVERRIDE_UP                    = 1 << 4;

   public static final int NAV_06_OVERRIDE_DOWN                  = 1 << 5;

   public static final int NAV_07_OVERRIDE_LEFT                  = 1 << 6;

   public static final int NAV_08_OVERRIDE_RIGHT                 = 1 << 7;

   /**
    * Actual state of the {@link IDrawable}.
    */
   public static final int STATE                                 = 0;

   public static final int STATE_01_DRAWING                      = 1 << 0;

   /**
    * Flag that allows the Control Flow to know whether the Drawable is visible on screen's {@link GraphicsX} at any moment.
    * <br>
    * <br>
    * Only the {@link IDrawable#draw(GraphicsX)} sets it to true. 
    * <br>
    * Drawing on an GraphicsX other than the Screen, does not set it.
    * <br>
    * Setting it to false should be handled with care.
    * <br>
    * When code does so, it must be sure that the Drawable pixels are no more visible.
    * <br>
    * After an exit animation for instance.
    */
   public static final int STATE_02_DRAWN                        = 1 << 1;

   /**
    * Set to true when code knows that the Drawable is fully hidden
    * below an opaque layer.
    * <br>
    * Or to hide it before it should not be drawn.
    * <br>
    * Draw Control Flow Flag preventing {@link IDrawable#draw(GraphicsX)} from drawing anything because
    * 
    * <br>
    * <br>
    * When a structure {@link IDrawable} recieves such a state change, it has to propagate change to its children {@link IDrawable}.
    * <br>
    * Usually will be set to true just before event {@link ITechDrawable#EVENT_01_NOTIFY_SHOW} is sent.
    * <br>
    * Used to hide an Overlay Header. <br>
    * When completely hidden, not even animating
    * <br>
    * <br>
    * When animating, the flag is set to true.
    */
   public static final int STATE_03_HIDDEN                       = 1 << 2;

   /**
    * When drawable is drawn outside its boundary.
    * <br>
    * This usually happens with drawable having a postponed style layer. {@link IDrw#FIG_FLAGP_8POSTPONE}
    * <br>
    * <br>
    * Special {@link Drawable} with style drawing outside regular dimension.
    * <br>
    * TODO Special algo to see what needs to be repainted. Similar to DLayers of MasterCanvas.
    * <br>
    * <br>
    * Most common case is inside a {@link TableView} and select state draws a border on the cell separator
    * and content has padding.
    * In those cases, we fall back to drawable and repaint parent BG layers
    * <br>
    */
   public static final int STATE_04_DRAWN_OUTSIDE                = 1 << 3;

   /**
    * State set when Drawable has been inited width and height and thus is ready
    * to be drawn.
    * Invalides this flag to force init method to do a layout
    * even if dw = w and dh = h.
    * <br>
    * Cannot be true, when {@link ITechDrawable#STATE_06_STYLED} is false.
    * If layouted, then styled.
    * If drawn, then layouted, then styled.
    */
   public static final int STATE_05_LAYOUTED                     = 1 << 4;

   /**
    * Set when it is decided that {@link IDrawable} has all its styles.
    * Setting it to true, merges all styles (root,ctype,structural and state)
    * <br>
    * When not styled, drawable cannot be layouted.
    * <br>
    * Animations from the style layers are loaded into the framework.
    * <br>
    * 
    * @see ITechDrawable#STATE_05_LAYOUTED
    */
   public static final int STATE_06_STYLED                       = 1 << 5;

   /**
    * Internal flag used by cache to know if the Drawable have been modified
    * <br>
    * <br>
    * Setting it to true will forces a cache update for those using the cache.
    * <br>
    * Set to true when
    * <li>size of drawable is modified
    * <li>command/event modifies visual state
    * <br>
    * <br>
    * Set to false when cache is updated.
    */
   public static final int STATE_07_CACHE_INVALIDATED            = 1 << 6;

   /**
    * Inner state used by {@link Drawable} to tell {@link ViewDrawable} that no relayouting is needed.
    * <br>
    * Set when dimension and style haven't changed.
    */
   public static final int STATE_08_NO_RELAYOUTING               = 1 << 7;

   /**
    * Data is trimmed to fit inside the given dimension
    */
   public static final int STATE_09_TRIMMED                      = 1 << 8;

   /**
    * System flag set when the content cannot be shrunk anymore and will be drawn over the boundary
    * of the drawable. This implies a clip must be set when drawing it
    */
   public static final int STATE_10_CLIPPED                      = 1 << 9;

   /**
    * Drawable is running an animation on either of its layers. 
    * <br>
    * <br>
    * Animation uses {@link IDrawable#draw(GraphicsX)} method and its control flow to draw each of its step.
    * <br>
    * Draw Control Flow Flag. By default the draw method delegates to the animation the draw process
    * (draw method does nothing in this state) 
    */
   public static final int STATE_11_ANIMATING                    = 1 << 10;

   /**
    * Drawable is currently appearing as a whole using an {@link IAnimable}.
    * <br>
    */
   public static final int STATE_12_APPEARING                    = 1 << 11;

   /**
    * Drawable is currently disappearing as a whole using an {@link IAnimable}
    * <br>
    */
   public static final int STATE_13_DISAPPEARING                 = 1 << 12;

   /**
    * State set when a cache is available
    */
   public static final int STATE_14_CACHED                       = 1 << 13;

   /**
    * When init method cannot compute dh or dw as positive.
    */
   public static final int STATE_15_EMPTY                        = 1 << 14;

   /**
    * State flag set if a least one pixel in the drawable
    * dimension has an alpha value smaller than 255
    * Should not be set from the outside as the Drawable
    * computes this state internally.
    * <br>
    * Content + aggregate of style layer figures' flag {@link IDrw#FIG_FLAGP_3OPAQUE}.
    */
   public static final int STATE_16_TRANSLUCENT                  = 1 << 15;

   /**
    * Opaque pixels cover the whole area, including margin.
    * <br>
    * <br>
    * Only gives a certainty when set to true. It means the drawable items <br>
    * <li>bg layers
    * <li>content
    * <li>fg layer
    * <br>
    * covers all its area pixel with fully opaque pixels
    * <br>
    * When false, one may only assume there are transparent pixels. <br>
    * This flag is used for drawing {@link IDrawable} on an image. It allows to only use
    * a single {@link Image} layer.
    * <br>
    * Impact on Draw Flow Control.
    */
   public static final int STATE_17_OPAQUE                       = 1 << 16;

   /**
    * Set to false, every time, content style changes
    * TODO
    */
   public static final int STATE_18_OPAQUE_COMPUTED              = 1 << 17;

   /**
    * When hidden but should not fire animation hook up
    */
   public static final int STATE_19_HIDDEN_OVER                  = 1 << 18;

   /**
    * {@link Drawable#draw(GraphicsX)} method is in "stand by" as long as animations requiring it are running
    * <br>
    * <br>
    * Implementation Animation mechanism micro manages this flag,
    * State set when the item should not be drawn by the control painter thread<br>
    * <br>
    * Set when animation draws the drawable itself sets this flag.
    * <br>
    * <br>
    * Some animation ask the drawable to draw using {@link IDrawable#draw(GraphicsX)}.
    * <br>
    * @see IAnimable#ANIM_24_OVERRIDE_DRAW
    */
   public static final int STATE_20_ANIMATED_FULL_HIDDEN         = 1 << 19;

   /**
    * Layer Animation:
    * <li> Style Background
    * <li> foreground layers
    * <li> just content
    * <br>
    * <br>
    * Animation class repeatedly calls {@link IDrawable#draw(GraphicsX)}
    * <br>
    * <br>
    */
   public static final int STATE_21_ANIMATED_LAYERS              = 1 << 20;

   /**
    * {@link DrawableAnimator} called when time is to draw the content
    * <br>
    * Animation states only work when {@link ITechCanvasDrawable#REPAINT_14_SYSTEM_REPAINT} is set.
    * <br>
    * We don't want animation when doing cache repaints.
    */
   public static final int STATE_22_ANIMATED_CONTENT_HIDDEN      = 1 << 21;

   /**
    * Sets when 
    */
   public static final int STATE_23_RELATIVE_TOPOLOGY            = 1 << 22;

   /**
    * State set when Drawable has holes in its area that should be considered when managing
    * pointing devices and repainting purposes.
    * <br>
    * Therefore, holes should be fetched using {@link IDrawable#getHoles}
    * <br>
    * Aggregate is another solution.. The inverse function of holes.
    * <br>
    * Affects the {@link DrawableUtilz#isInside(InputConfig, IDrawable)}
    * <br>
    * Also an {@link ImageDrawable} that wants to do pixel matching sets this flag?
    */
   public static final int STATE_24_HOLED                        = 1 << 23;

   /**
    * Sometimes, it is more elegant to talk about aggregates than holes
    */
   public static final int STATE_25_AGGREGATE                    = 1 << 24;

   /**
    * True when position needs to be computed was set / computed
    */
   public static final int STATE_26_POSITIONED                   = 1 << 25;

   /**
    * Flag telling drawable was moved and need
    */
   public static final int ZSTATE_26_MOVED_SINCE_LAST_REPAINT    = 1 << 25;

   /**
    * When Drawable has another drawable drawn over it and thus bottom up pointer events cannot work
    */
   public static final int STATE_27_OVERLAYED                    = 1 << 26;

   /**
    * Set by parent when Drawable is completely outside Parent area for some reason.
    */
   public static final int STATE_28_NOT_CONTAINED_IN_PARENT_AREA = 1 << 27;

   /**
    * When {@link IDrawable} was drawn clipped on screen
    */
   public static final int STATE_29_CLIPPED                      = 1 << 28;

   /**
    * When true when dw and dh are not computed
    * <br>
    * When false, dw and dh are computed but may still be modified
    * by sub class
    */
   public static final int STATE_30_LAYOUTING                    = 1 << 29;

   public static final int STATE_31_STYLE_CLASS_REFRESH          = 1 << 30;

   /**
    * Set when Drawable state has changed
    * <br>
    * <br>
    * Set to false each time it is painted
    * {@link ITechDrawable#STATE_07_CACHE_INVALIDATED}
    */
   public static final int STATE_32_CHANGED                      = 1 << 31;

   /**
    * State style that modifies the style key of the Drawable.
    * <br>
    * {@link StyleClass} applies
    * Style state are otherwise still used as states for business.
    * <br>
    * States that often change during the lifetime and for which it is not elegant to create
    * a root style class.
    */
   public static final int STYLE                                 = 0;

   /**
    * First applied
    */
   public static final int STYLE_01_CUSTOM                       = 1 << 0;

   /**
    * 
    */
   public static final int STYLE_02_CUSTOM                       = 1 << 1;

   /**
    * Simple mark style. Usually combo with check boxes.
    */
   public static final int STYLE_03_MARKED                       = 1 << 2;

   /**
    * When a scrollbar button is disabled because pointer will have no effect.
    */
   public static final int STYLE_04_GREYED                       = 1 << 3;

   /**
    * Mostly used by end user.
    */
   public static final int STYLE_05_SELECTED                     = 1 << 4;

   /**
    * Key state that tracks which drawable has the key focus.
    * <br>
    * Some styles will have a foreground dotted border associated with this flag.
    */
   public static final int STYLE_06_FOCUSED_KEY                  = 1 << 5;

   /**
    * State that tracks if Drawable has the pointer focus. This style is set when pointer movement are recorded.
    * <br>
    * <br>
    * See {@link Controller#newFocusPointerPress(mordan.controller.InputConfig, IDrawable)}
    * <br>
    * <br>
    * In a Table, when a cell has pointer focus, the Table maybe have or may not. It depends if Tech design wants parent to keep
    * {@link ITechDrawable#STYLE_07_FOCUSED_POINTER} while a child also has it.
    * <br>
    * <br>
    * In both cases, Table is inside the pointer focus hierachy through its parenting of tables cells.
    */
   public static final int STYLE_07_FOCUSED_POINTER              = 1 << 6;

   /**
    * State set when Pointer is pressed on {@link IDrawable}.
    * <br>
    * Most of the time, pressing will change the key focus as well.
    */
   public static final int STYLE_08_PRESSED                      = 1 << 7;

   /**
    * When a Drawable is dragged, you may want to change the color of the border.
    * <br>
    * <br>
    * Dragged state is decided by {@link PointerGesture}. After a press and a given amount of movement.
    */
   public static final int STYLE_09_DRAGGED                      = 1 << 8;

   /**
    * Gives a visual cue to the recieving {@link IDrawable} that will get the event if the {@link IDrawable} is dragged off.
    * <br>
    * This states logically overrides others
    */
   public static final int STYLE_10_DRAGGED_OVER                 = 1 << 9;

   public static final int STYLE_11_CUSTOM                       = 1 << 10;

   /**
    * this last style overrides previous styles item definitions.
    */
   public static final int STYLE_12_CUSTOM                       = 1 << 11;

}
