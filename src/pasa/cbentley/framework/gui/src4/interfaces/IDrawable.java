package pasa.cbentley.framework.gui.src4.interfaces;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.stator.IStatorable;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.cmd.src4.interfaces.INavTech;
import pasa.cbentley.framework.coreui.src4.interfaces.IBentleyFwSerial;
import pasa.cbentley.framework.coreui.src4.tech.IInput;
import pasa.cbentley.framework.coreui.src4.utils.ViewState;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.engine.RgbImage;
import pasa.cbentley.framework.gui.src4.anim.move.Move;
import pasa.cbentley.framework.gui.src4.canvas.CanvasAppliDrawable;
import pasa.cbentley.framework.gui.src4.canvas.CanvasResultDrawable;
import pasa.cbentley.framework.gui.src4.canvas.ExecutionCtxDraw;
import pasa.cbentley.framework.gui.src4.canvas.FocusCtrl;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.canvas.TopologyDLayer;
import pasa.cbentley.framework.gui.src4.canvas.ViewContext;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceDrawable;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.LayEngineDrawable;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.layouter.src4.engine.LayEngine;
import pasa.cbentley.layouter.src4.interfaces.ILayoutable;
import pasa.cbentley.layouter.src4.tech.ITechSizer;

/**
 * Main Interface to any drawable components which supports the {@link IAnimable} framework.
 * <br>
 * <br>
 * <b>Styles</b> : <br>
 * {@link StyleClass} identifies the root class.
 * <li> Root style of IDrawable is the one given by its {@link StyleClass}.
 * <li> State styles are styles associated to style states like {@link ITechDrawable#STYLE_05_SELECTED}.
 * <li> Structural styles are {@link ByteObject} styles  {@link IDrawable#setStructStyleKey(int)} given by the layout.
 * <li> CType styles are loose {@link ByteObject} styles that are linked to the StyleClass.
 * <li> Active style is a merge of root style + structural style + ctype + states by the {@link StyleClass}.
 * <br>
 * <br>
 * <b>Getters</b>:
 * <br>
 * <li> StyleClass {@link IDrawable#getStyleClass()}.
 * <li> Active {@link ByteObject} style is retrieved by {@link IDrawable#getStyleKey()}.
 * <li> {@link IDrawable#getStructKey()}.
 * <li> Ctype style is set outside the IDrawable. The Drawable keeps the CType value {@link IDrawable#getCType()} 
 * <br>
 * <br>
 * <b>Setters</b>:
 * 
 * <li> For Ctype style is set externally using
 * <li> {@link IDrawable#setCType(int)}.
 * <br>
 * <br>
 * 
 * <b>Genetics</b>
 * Fixed in time item properties.
 * When a gene is activated, a state and/or a behavior flag is set.
 * <br>
 * Genes are internally managed. An external class is not supposed to change the genes of a Drawable.
 * <br>
 * <br>
 * <b>Behaviors</b> tells about the capabilities and how it will behave when faced with a problem.
 * Behavior switches that may stop.
 * <li> {@link ITechDrawable#BEHAVIOR_13_REPAINT_PARENT_STYLE} asks any parent to be redrawn in. It might not always be on.
 * <li> {@link ITechDrawable#BEHAVIOR_01_NOT_SELECTABLE}. When a TableView has a policy given to not select a cell, it sets
 * this flag. Thus the cell is not selected.
 * When a gene is activated, gene_state is set
 * Behavior are switches. Else use Genes.
 * Behavior may be externally managed
 * <br>
 * <br>
 * <b>States</b> tell about the current situation<br>
 * <li>{@link ITechDrawable#STYLE_05_SELECTED}
 * <br>
 * <br>
 * <b>User CType</b>
 * <br>
 * User integer property. Used to differentiate similar Drawables between themselves based on a user criteria.
 * Log message system might use a differentiation between Errors and Warnings.
 * <br>
 * <br>
 * <b>Events</b> allow the Drawable to respond to incoming situation in a decoupled way.<br>
 *  Events and states are closely related in that events are notified just after the state change.
 *  <li>State {@link ITechDrawable#STATE_11_ANIMATING STATE_D_ANIMATING} <font color=#00CF0F>True</font> 
 *  <li>Event {@link ITechDrawable#EVENT_06_ANIM_STARTED}  
 *  <li>State {@link ITechDrawable#STATE_11_ANIMATING STATE_D_ANIMATING} <font color=#CF000F>False</font> 
 *  <li>Event {@link ITechDrawable#EVENT_05_ANIM_FINISHED}
 *  <br>
 *  Likewise <br>
 *  
 *  <li>State {@link ITechDrawable#STYLE_06_FOCUSED_KEY} <font color=#00CF0F>True</font>  
 *  <li>Event {@link ITechDrawable#EVENT_03_KEY_FOCUS_GAIN}  
 *  <li>State {@link ITechDrawable#STATE_11_ANIMATING} <font color=#CF000F>False</font>  
 *  <li>Event {@link ITechDrawable#EVENT_04_KEY_FOCUS_LOSS}
 *  <br>
 * 
 * <br>
 * <b>Life Cycle</b> :
 * <ol>
 * <li> {@link ITechDrawable#STATE_03_HIDDEN} <font color=#00CF0F>True</font>  when created.
 * <li> {@link ITechDrawable#STATE_06_STYLED} <font color=#00CF0F>True</font>  when style is finalized.
 * <li> {@link ITechDrawable#STATE_05_LAYOUTED} <font color=#00CF0F>True</font>  when method {@link IDrawable#init(int, int)} has been called.
 * <li> {@link IDrawable#draw(GraphicsX)} is called.
 * <li> {@link ITechDrawable#STATE_02_DRAWN}.  {@link ITechDrawable#STATE_03_HIDDEN} is removed.
 * <li> {@link ITechDrawable#STATE_11_ANIMATING} is <font color=#00CF0F>True</font>  if {@link ByteObject#ANIM_TIME_1_ENTRY} exists.
 * <li> {@link ITechDrawable#EVENT_03_KEY_FOCUS_GAIN} when Drawable {@link CmdCtx} is active.
 * <li> {@link ITechDrawable#EVENT_04_KEY_FOCUS_LOSS}
 * </ol>
 * <b>Animation Life Cycle</b> :
 * <li>
 * <li>
 * <li>
 * <br>
 * <br>
 * <b>ViewState</b> : {@link ViewState} <br>
 * Allows the framework to save the precise state in which the drawable is in at the moment of the 
 * {@link IDrawable#getViewState} call.
 * <br>
 * It includes dynamic parameters.
 * <br>
 * Since {@link IDrawable} framework heavily uses ByteObject for styles, it implements the {@link IDrwListener} interface.
 * with automatic registration.
 * So the {@link ByteObjectRepository} does not garbage
 * <br>
 * <br>
 * <b>Scenes</b> : <br>
 * Scenes like in Flash or MegaDrive games. A Scene is a set of animated and non animated Drawable.
 * Scene is an {@link IDrawable} that controls many other {@link IDrawable} on a timeline. 
 * It orchestrates.
 * <li>Drawable is shown at that frame.
 * <li>Life cycle is computed 
 * <li> Clocking may be absolute (X number of frames)
 * <li> Or relative (When this Drawable animation finishes)
 * <br>
 * 
 * <b>Commands</b>
 * <br>
 * An {@link IDrawable} gets Input Events through
 * <li> {@link IDrawable#manageInput(InputConfig)}
 * <li> {@link IDrawable#manageKeyInput(InputConfig)}
 * <li> {@link IDrawable#managePointerInput(InputConfig)}
 * <br>
 * <br>
 * {@link IDrawable} is a {@link ICommandDrawable} and provides a {@link CmdCtx}.
 * If Drawable does not have such a context, return the parent or root ctx if no parent
 * @author Charles-Philip Bentley
 *
 * @see IAnimable
 */
public interface IDrawable extends IStringable, ITechDrawable, IStatorable, IBentleyFwSerial, IViewSerializable, ICommandDrawable, ILayoutable {

   public void addFullAnimation(IAnimable anim);

   /**
    * Uses flow control Draw using last init.
    * <br>
    * If {@link ITechDrawable#STATE_05_LAYOUTED} is false, call {@link IDrawable#init(int, int)} with original values. 
    * When the new dw dh are computed, if not equal to before, call layout init on parent. (chain reaction)
    * .
    * <br>
    * Similar as {@link IDrawable#layoutInvalidate(boolean)}.
    * <br>
    * if {@link ITechDrawable#STATE_15_EMPTY}, draws nothing.<br>
    * If not {@link ITechDrawable#STATE_06_STYLED}, do a style, do a init and draw.<br>
    * When state {@link ITechDrawable#STATE_03_HIDDEN} is set, nothing is drawn.<br>
    * <br>
    * <br>
    * @param g {@link GraphicsX}
    */
   public void draw(GraphicsX g);

   /**
    * Simply draws on {@link GraphicsX} without control flow.
    * <br>
    * <br>
    * To draw on an Image.
    * <br>
    * Snapshot of the Drawable with its state.
    * <br>
    * @param g {@link GraphicsX}
    */
   public void drawDrawable(GraphicsX g);

   public CanvasAppliDrawable getCanvas();

   /**
    * Children {@link IDrawable} managed by this {@link IDrawable}.
    * <br>
    * <br>
    * All Children are within the same layer.
    * <br>
    * <br>
    * How can loops can be broken? Children is the parent
    * <br>
    * <br>
    * This method allows to know which drawable are impacted by a repaint over a boundary
    * <br>
    * <br>
    * In case of an aggregate {@link ITechDrawable#STATE_25_AGGREGATE}, returns those aggregate Drawable that make 
    * this Drawable.
    * @return
    */
   public IDrawable[] getChildren();

   /**
    * The bit numberal state 16 bits by default
    * @return
    */
   public int getCType();

   public IDrawable getDrawable(int x, int y, ExecutionCtxDraw ex);

   /**
    * 
    * @return
    */
   public int getDrawnHeight();

   /**
    * Number of pixels drawn horizontally
    * @return
    */
   public int getDrawnWidth();

   public int getHCPosInside();

   /**
    * Returns int array defining depending on the state
    * <li> {@link ITechDrawable#STATE_24_HOLED} => 4 values per holes.
    * <br>
    * <br>
    * @return
    */
   public int[] getHoles();

   public LayEngineDrawable getLayEngine();

   /**
    * 
    * @return
    */
   public String getName();

   /**
    * Returns the target {@link IDrawable} reachable by the given navEvent
    * 
    * <li>{@link INavTech#NAV_1_UP}
    * <li>{@link INavTech#NAV_2_DOWN}
    * <li>{@link INavTech#NAV_3_LEFT}
    * <li>{@link INavTech#NAV_4_RIGHT}
    * <br>
    * <br>
    * 
    * Null if none
    * 
    * @param navEvent
    * @return
    */
   public IDrawable getNavigate(int navEvent);

   /**
    * Parent {@link IDrawable} by construction. within a given DLayer?
    * <br>
    * {@link IDrawable#setParent(IDrawable)}
    * When no parent is set, returns the root Drawable
    * @return null if root of a DLayer
    */
   public IDrawable getParent();

   /**
    * When Parent is null, creates a wrapper 
    * When null, create a wrapper around the {@link ViewContext}
    * @return
    */
   public IDrawable getParentNotNull();

   /**
    * Sharable {@link RgbImage} whose source pixel are updatable.
    * <br> cache whose
    * <li> {@link ITechDrawable#CACHE_0_NONE}
    * @param type
    * @return
    */
   public RgbImage getRgbCache(int type);

   /**
    * Returns this Drawable as an {@link RgbImage}. 
    * <br>
    * <br>
    * <li> {@link ITechDrawable#IMAGE_0_ALL}
    * <li> {@link ITechDrawable#IMAGE_1_CONTENT}
    * <li> {@link ITechDrawable#IMAGE_2_BG}
    * <li> {@link ITechDrawable#IMAGE_3_FG}
    * 
    * <br>
    * <br>
    * 3 cases:
    * <ol>
    * <li>Full
    * <ol>
    * <li>Width is dw.
    * <li>Height is dh.
    * </ol>
    * <li>Decoration
    * <li>Content
    * </ol>
    * When {@link IDrawable} is cached, the cached image is returned.
    * 
    * <br>
    * This method does not change the caching behavior of the {@link IDrawable}. 
    * <br>
    * <br>
    * 
    * If the {@link RgbImage} is to be shared between several {@link IAnimable} animations,
    * (For example, 2 colorfilters at two different locations) the code cannot change the
    * cache content. 
    * <br>
    * In other words, if a consumer modifies the cache buffer, it MUST be the unique
    * user in the whole application and if logic requires it, asks the Drawable to refresh
    * the cache.<br>
    * <b>Locking.</b>
    * <br>
    * {@link RgbImage#acquireLock(int)}.
    * <br>
    * it may also modifies,draw,then undo modifications
    * <br>
    * If {@link IDrawable} does not support RGB drawing or it is not efficient
    * The {@link RgbImage} is in primitive mode. (an Image object).
    * <br>
    * {@link RgbImage} will convert to int[] if necessary. For drawing purpose, it won't be necessary.
    * <br>
    * <br>
    * Related to the {@link IDrawable} cache. To disconnect it, take a copy.
    * @param type Content/Decoration/Full
    * @return an {@link RgbImage}
    * 
    */
   public RgbImage getRgbImage(int type);

   /**
    * @see IDrawable#getSizeW(int)
    * @param sizeType
    * @return
    */
   public int getSizeH(int sizeType);

   /**
    * Returns the sizing value based on the styling
    * 
    * <li> {@link ITechSizer#SIZER_PROP_05_CONTENT} Width without pad/border/margin values
    * <li> {@link ITechSizer#SIZER_PROP_06_CONTENT_PAD}
    * <li> {@link ITechSizer#SIZER_PROP_07_CONTENT_PAD_BORDER}
    * <li> {@link ITechSizer#SIZER_PROP_10_PAD}
    * <li> {@link ITechSizer#SIZER_PROP_11_PAD_BORDER}
    * <li> {@link ITechSizer#SIZER_PROP_12_PAD_BORDER_MARGIN}
    * <li> {@link ITechSizer#SIZER_PROP_13_BORDER}
    * <li> {@link ITechSizer#SIZER_PROP_14_BORDER_MARGIN}
    * <li> {@link ITechSizer#SIZER_PROP_15_MARGIN}
    * 
    * Sub class must implement other sizes.
    * 
    * @param sizeType
    * @return
    */
   public int getSizeW(int sizeType);

   /**
    * Similar to {@link IDrawable#getSizeW(int)} but for the X coordinate.
    * <li> {@link ITechSizer#SIZER_PROP_07_CONTENT_PAD_BORDER} would compute the X coord with padding and border
    * @param sizeType
    * @return
    */
   public int getSizeX(int sizeType);

   /**
    * @see IDrawable#getSizeW(int)
    * @param sizeType
    * @return
    */
   public int getSizeY(int sizeType);

   /**
    * Structural style {@link ByteObject} that was set using {@link IDrawable#setStructStyle(ByteObject)}.
    * <br>
    * <br>
    * 
    * @return
    */
   public ByteObject getStructStyle();

   /**
    * Return the current style
    * @return
    */
   public ByteObject getStyle();

   /**
    * Current {@link StyleClass}.
    * @return
    */
   public StyleClass getStyleClass();

   public int getUIID();

   /**
    * The {@link ViewContext} in which the drawable is sized and positioned
    * @return
    */
   public ViewContext getVC();

   public int getVCPosInside();

   public ViewContext getViewContext();

   /**
    * Encapuslate state so that {@link IDrawable} can be serialized and brought back
    * from the ether with {@link IDrawable#setViewState(ViewState)}.
    * <br>
    * <br>
    * Data/model serialization is made only if the data cannot be sourced to a database. 
    * <br>
    * <br>
    * A drawable that was assigned a String by the user, will save it.
    * @return
    */
   public ViewState getViewState();

   /**
    * Absolute X coordinate relative to {@link ViewContext}
    *  at which the {@link IDrawable} is drawn.
    * <br>
    * @return
    */
   public int getX();

   /**
    * Absolute Y coordinate at which the {@link IDrawable} is drawn.
    * <br>
    * @return
    */
   public int getY();

   /**
    * The ZIndex in the parent's coordinate space
    * @return
    */
   public int getZIndex();

   public boolean hasBehavior(int flag);

   public boolean hasState(int flag);

   public boolean hasStateNav(int flag);

   public boolean hasStateStyle(int flag);

   /**
    * Init with the defaults sizer or the sizers set by {@link IDrawable#setSizers(ByteObject, ByteObject)}
    */
   public void init();

   /**
    * Initialize the drawable width and height of the {@link IDrawable} using {@link ITechDrawable#DIMENSION_API} semantics.
    * <br>
    * <br>
    * In order to be layouted, {@link IDrawable} must be styled using the the system state {@link ITechDrawable#STATE_06_STYLED}.
    * <br>
    * <br>
    * After this method is called, the system state {@link ITechDrawable#STATE_05_LAYOUTED} is set to true.
    * <br>
    * <br>
    * {@link ITechDrawable#STATE_05_LAYOUTED} is set to false when layout is invalidated or when style is changed.
    * Meaning of non positive values is stored in the dimension constraint value. 
    * <br>
    * <br>
    * To get <b>effective</b> dimensions, call 
    * <li> {@link IDrawable#getDrawnWidth()}
    * <li> {@link IDrawable#getDrawnHeight()}
    * <br>
    * <br>
    * @param width available drawable width. {@link ITechDrawable#DIMENSION_API} semantics.
    * @param height available drawable height. {@link ITechDrawable#DIMENSION_API} semantics.
    */
   public void init(int width, int height);

   public void invalidateLayout();

   /**
    * True when all drawn pixels are 100% opaque.
    * <br>
    * Uses 
    * <li>{@link ITechDrawable#STATE_17_OPAQUE}
    * <li>{@link ITechDrawable#STATE_18_OPAQUE_COMPUTED}
    * <br>
    * Each Drawable may have its own way to compute whether or not it is opaque.
    * <br>
    * <br>
    * @return
    */
   public boolean isOpaque();

   /**
    * Updates the computation, possibly changing drawn dimension and calling
    * the updateLayout on parent drawable if a change occurs.
    * 
    * @parent use false when not knowing
    */
   public void layoutInvalidate(boolean topDown);

   /**
    * Force the resizing of everything drawable in the chain.
    * <br>
    * The layout flags are first set to false, then reinit of with
    * {@link Drawable#init()}
    * @param child not null when cause of update was a change in 
    * Drawable will decide if it needs to update its dimension.
    */
   public void layoutUpdate(IDrawable child);

   /**
    * 
    * @param ic
    */
   public void manageGestureInput(InputConfig ic);

   /**
    * Manage an input event.
    * @param ic
    */
   public void manageInput(InputConfig ic);

   /**
    * {@link Controller} asking {@link IDrawable} to process its {@link InputConfig} for any key action.
    * <br>
    * <br>
    * If such action is done, it then printed on {@link ScreenResult}.
    * <br>
    * <br>
    * All references are fetched on the {@link Controller} singleton.
    * <br>
    * Part of the Focused and TopDown event processing of the {@link Controller}.
    * <br>
    * May Moves the Key Focus of {@link FocusCtrl}.
    * <br>
    * This is flagged in the {@link CanvasResultDrawable} object.
    * <br>
    * @param ic
    */
   public void manageKeyInput(InputConfig ic);

   public void manageNavigate(CmdInstanceDrawable cd, int navEvent);

   /**
    * All other events kinds are sent here.
    */
   public void manageOtherInput(InputConfig ic);

   /**
    * Asks the Controlled View, its context given the pointer configuration generates a command.
    * <br>
    * <br>
    * For {@link IInput#MOD_3_MOVED}, sets {@link ITechDrawable#STYLE_07_FOCUSED_POINTER} on
    * <br>
    * On Pointer Move supporting platforms, this method is called only when pointer is inside area of {@link IDrawable} 
    * <br>
    * Returns which drawable must be in Pointer Over focus and Pointer Selection Focus,
    * In pointer pressed event.
    * This is flagged in the {@link CanvasResultDrawable} object.
    * 
    * @param ic
    */
   public void managePointerInput(InputConfig ic);

   /**
    * 
    * @param ic
    */
   public void managePointerStateStyle(InputConfig ic);

   /**
    * When a mouse/key combination is repeated
    * @param ic
    */
   public void manageRepeatInput(InputConfig ic);

   //   /**
   //    * Call Back for DownCmd
   //    *
   //    */
   //   public void navigateDown(InputConfig ic);
   //
   //   /**
   //    * Called only if {@link ITechDrawable#BEHAVIOR_27_NAV_HORIZONTAL} is true.
   //    * <br>
   //    * First call in cycle with {@link InputConfig#isFirstNavCycle()}
   //    * <br>
   //    * Second call, implements any second cycle stuff like cycles or other stuff.
   //    * @param ic
   //    */
   //   public void navigateLeft(InputConfig ic);
   //
   //   /**
   //    * Implement to the right horizontal traversal
   //    * @param ic
   //    */
   //   public void navigateRight(InputConfig ic);
   //
   //   /**
   //    * 
   //    * @param ic
   //    */
   //   public void navigateSelect(InputConfig ic);
   //
   //   /**
   //    * Call Back for Up Command
   //    *
   //    */
   //   public void navigateUp(InputConfig ic);

   /**
    * Same as {@link IDrawable#notifyEvent(int, Object)} but with a null Object
    * @param event
    */
   public void notifyEvent(int event);

   /**
    * <b>Events</b>:
    * <li> {@link ITechDrawable#EVENT_01_NOTIFY_SHOW}
    * <li> {@link ITechDrawable#EVENT_02_NOTIFY_HIDE}
    * <li> {@link ITechDrawable#EVENT_03_KEY_FOCUS_GAIN}
    * <li> {@link ITechDrawable#EVENT_04_KEY_FOCUS_LOSS}
    * <li> {@link ITechDrawable#EVENT_05_ANIM_FINISHED}
    * <li> {@link ITechDrawable#EVENT_06_ANIM_STARTED}
    * <br>
    * Contract is method is called before the state associated with the event has been changed.
    * @param event
    * @param o
    */
   public void notifyEvent(int event, Object o);

   /**
    * Bypass sizers and pozers, set 
    * @param x
    * @param y
    * @param w
    * @param h
    */
   public void setArea(int x, int y, int w, int h);

   /**
    * Change the behaviour of the Drawable
    * @param flag
    * @param value
    */
   public void setBehaviorFlag(int flag, boolean value);

   /**
    * <li>{@link ITechDrawable#CACHE_0_NONE}
    * <li>{@link ITechDrawable#CACHE_1_CONTENT}
    * <li>{@link ITechDrawable#CACHE_2_FULL}
    * <li>{@link ITechDrawable#CACHE_3_BG_DECO}
    * @param type
    */
   public void setCacheType(int type);

   /**
    * Sets the bit numberal state 16 bits by default
    * <br>
    * Invalidate style {@link ITechDrawable#STATE_06_STYLED}
    * <br>
    * @return
    */
   public void setCType(int num);

   public void setNavigate(int navEvent, IDrawable d, boolean doInverse);

   /**
    * Sets the owner of this Drawable
    * @param d
    */
   public void setParent(IDrawable d);

   /**
    * Sets sizers. When null nothing happens
    * @param w
    * @param h
    */
   public void setSizers(ByteObject w, ByteObject h);

   /**
    * @param flag
    * @param value
    */
   public void setStateFlag(int flag, boolean value);

   public void setStateNav(int flag, boolean value);

   public void setStateStyle(int flag, boolean value);

   /**
    * Set an additional style layer.
    * <br>
    * A static 0 style key linking to a transparent style definition
    * <br>
    * @param styleKey
    */
   public void setStructStyle(ByteObject style);

   public void setStyleClass(StyleClass sc);

   /**
    * Sets the {@link ViewContext} to which the {@link IDrawable} belongs.
    * 
    * @param vc cannot be null
    */
   public void setViewContext(ViewContext vc);

   public void setViewState(ViewState vs);

   /**
    * Sets the x TOP LEFT coordinate
    * <br>
    * <br>
    * The location will be relative to the parent's coordinate space. 
    * <br>
    * <br>
    * @param x
    */
   public void setX(int x);

   public void setXY(ByteObject pozerX, ByteObject pozerY);

   /**
    * Sets both Screen Coordinates at the same time
    * <br>
    * <br>
    * The coordinate is relative to the Screen 0,0 space. 
    * <br>
    * <br>
    * TODO How to deal with a Move animation across 2 different coordinate spaces?
    * <br>
    * <br>
    * We want Drawable to move in root coordinate space rx,ry to a point in child coordinate space
    * cx,cy.
    * <br>
    * {@link Move} uses this method to set coordinate.
    * @param x
    * @param y
    */
   public void setXY(int x, int y);

   /**
    * Activate Functional Positioning relative to the Parent, and if null the ViewContext.
    * <br>
    * Its a shortcut to {@link LayEngine}
    * 
    * <li> {@link C#LOGIC_1_TOP_LEFT}
    * <li> {@link C#LOGIC_2_CENTER}
    * <li> {@link C#LOGIC_3_BOTTOM_RIGHT}
    * 
    * This method is convenience around the definition of the Function definition.
    */
   public void setXYLogic(int xLogic, int yLogic);

   /**
    * Sets the y TOP Left coordinate, bypass the pozerY
    * 
    * This call should be avoided and Pozers should be used
    * 
    * @param y
    */
   public void setY(int y);

   /**
    * ZIndex field, i.e DLayer index in {@link TopologyDLayer}.
    * <br>
    * <br>
    * Set by controlling {@link TopologyDLayer}.
    * <br>
    * When a Lone Drawable is repainted
    * <br>
    * @param zindex
    */
   public void setZIndex(int zindex);

   public void shHideDrawable(InputConfig ic);

   /**
    * Moves drawable from current position
    * @param dx
    * @param dy
    */
   public void shiftXY(int dx, int dy);

   /**
    * Draws Drawable on {@link GraphicsX} with animation control flow. 
    * <br>
    * <br>
    * Any {@link ByteObject#ANIM_TIME_1_ENTRY} animation is launched. 
    * <br>
    * If animation requires it, Drawable stays hidden until the animation finishes, then a final repaint
    * is called and {@link IDrawable#draw(GraphicsX)} do its thing. <br>
    * Entry animation might just animate a FG layer while the Drawable is visible
    * Hidden, when all Drawable pixel are animated into the screen.
    * <br>
    * <br>
    * <b>Implementation Note</b> : <br>
    * Sets {@link ITechDrawable#STATE_03_HIDDEN} to false. <br>
    * Notify events {@link ITechDrawable#EVENT_01_NOTIFY_SHOW} and {@link ITechDrawable#EVENT_08_POINTER_FOCUS_GAIN}. <br>
    * Finally, calls {@link IDrawable#draw(GraphicsX)}.
    * <br>
    * <br>
    * @param g
    */
   public void show(GraphicsX g);

   /**
    * 
    * @param ic
    */
   public void shShowDrawableOver(InputConfig ic);

   public void stringUpdate();

   //#mdebug
   /**
    * Short Description so that Debug reader knows which Drawable on screen it is.
    * @return
    */

   public String toStringOneLineStates();
   //#enddebug
}
