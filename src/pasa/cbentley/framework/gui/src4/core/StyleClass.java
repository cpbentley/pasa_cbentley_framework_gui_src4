package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesDrw;
import pasa.cbentley.core.src4.ctx.ICtx;
import pasa.cbentley.core.src4.ctx.IToStringFlags;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.io.BAByteOS;
import pasa.cbentley.core.src4.io.BADataIS;
import pasa.cbentley.core.src4.io.BADataOS;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.stator.IStatorable;
import pasa.cbentley.core.src4.stator.StatorReader;
import pasa.cbentley.core.src4.stator.StatorWriter;
import pasa.cbentley.core.src4.structs.IntToObjects;
import pasa.cbentley.core.src4.utils.ArrayUtils;
import pasa.cbentley.core.src4.utils.BitUtils;
import pasa.cbentley.core.src4.utils.IntUtils;
import pasa.cbentley.framework.drawx.src4.style.IBOStyle;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ToStringStaticGui;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.table.interfaces.IBOTableView;

/**
 * {@link StyleClass} defines the style and behaviours of Drawable objects.
 * 
 * Tech definitions of scrollbars, tables, views are linked here.
 * When a Gui need a table, it asks its {@link StyleClass} for the {@link IBOTableView} definition.
 * 
 * Manages the links between a class of style and its sub items.
 * <br>
 * <br>
 * A class can be shared between {@link IDrawable}s.
 * <br>
 * <br>
 * The designer creates a root {@link ByteObject} style and different sub styles ({@link IBOTypesDrw#TYPE_071_STYLE}).
 * 
 * From these, {@link StyleClass} create variations of the root style. 
 * <br>
 * Root style is usually opaque and sub styles are semi transparent i.e. flagged with {@link IBOStyle#STYLE_FLAG_PERF_7_INCOMPLETE}
 * <br>
 * <br>
 * <b>Style Variations </b>:
 * <li>root
 * <li>state. style dimension given by {@link IDrawable} states such as {@link ITechDrawable#STYLE_05_SELECTED}.
 * <li>ctype. style dimension given by data model properties (User)
 * <li>structural. style dimension give by layout structure.
 * <br>
 * <br>
 * 3 additional dimension is a lot. It means cubic complexity.
 * Reason only styles that will change frequently are cached in this class.
 * <br>
 * Ctype styles are structural styles are managed at the {@link Drawable} class.
 * <br>
 * <br>
 * <b>Structural Styles</b>.
 * <br>
 * <br>
 * <br>
 * <b>State Styles</b>.
 * The class handles all combinations of state flags with root and custom
 * One instance of this class for each Style class.<br>
 * However they are hosted in the same StyleID space of the root style.
 * <br>
 * <br>
 * Relationship with {@link Drawable}
 * The StyleClass hosts a int[] of custom type style. Index is the custom type
 * <br>
 * <br>
 * {@link ByteObject}:
 * <br>
 * One can link as many {@link ByteObject} to a {@link StyleClass} using a SC_LK_ID.
 * <li>ViewPane techninal options
 * <li>Scrollbar options for a given style
 * <li>TableOptions for StyleClass.
 * <br>
 * <br>
 * <b>Technical Styles</b> (TechIDs) define the options/technical options of drawables. 
 * Example of technical style, the relation in a {@link ViewPane} between scrollbars and headers.
 * {@link IDrwTypes#TYPE_TECH}.
 * <br>
 * <br>
 * <b>Root Style</b>:
 * Key with which it is possible to get everything using Link IDs.
 * <br>
 * Link IDs are used with the following methods:
 * <br>
 * <li> state style
 * <li> tech styles
 * <li> ctypes "{@link StyleClass#linkCTypeStyles(int, int[])} </li>
 * <br>
 * <br>
 * The <b>Static</b> StyleClass. It has an integer ID value of 0 in the style key.
 * <br>
 * Style definitions that don't need a {@link StyleClass}.
 * <br>
 * <br>
 * They are not supposed to be linked to. And methods suchs as
 * <li> {@link Presentation#linkSubSKToRootStyleClass(int, int)}</li>
 * <li> {@link StyleClass#linkCTypeStyles(int, int[])} </li>
 * will throw an {@link IllegalArgumentException}
 * 
 * <br>
 * <br>
 * @author Charles-Philip Bentley
 *
 */
public class StyleClass implements IStringable, IStatorable {

   public static final int SERIAL_TYPE_0_NULL       = 0;

   public static final int SERIAL_TYPE_1_BYTEOBJECT = 1;

   public static final int SERIAL_TYPE_2_STYLECLASS = 2;

   /**
    * Used for caching.
    * Each index has linkers between states -> style refid
    * 
    * index 0 being the possible state combinations for the root style
    * index 1 being the possible state combinations for the ctype 1
    */
   private IntToObjects[]  cachePossibles;

   private IntToObjects    classes;

   private IntToObjects    ctypeObjects;

   /**
    * reference id to ctypes styles merged with the rootstyle
    */
   private int[]           ctypesRef;

   /**
    * ctypes style repository ctype:0 ctype:1 ...
    * <br>
    * <br>
    * Built by {@link StyleClass#linkCTypeStyle(ByteObject, int)}
    */
   private IntToObjects    ctypeStyles;

   public boolean          FLAG_CACHE_STYLE         = true;

   protected final GuiCtx  gc;

   private int             initStyle;

   private boolean         isImmutable              = false;

   /**
    * Name of style class. Mainly used for debugging purposes.
    */
   private String          name;

   /**
    * Initialized on demand. Links other {@link ByteObject} like
    * <li> ViewPane sub style 
    * <li> ViewPane view pane technical options
    * <br>
    * <br>
    * Holds objects for
    * <li> {@link StyleClass#linkByteObject(ByteObject, int)}
    * <li> {@link StyleClass#getByteObject(int)}
    * <br>
    * <br>
    * 
    */
   private IntToObjects    objects;

   /**
    * When null, root style class
    * <br>
    * When not null, request not fulfilled by {@link StyleClass}
    * will be forwarded to parent first, and then normal process.
    * <br>
    * Parent does not generates a dev warn.
    * 
    * because it is done by design.
    */
   protected StyleClass    parent;

   /**
    * Keeps all modifications of the root style with ctypes and structural styles.
    * <br>
    * <br>
    * Drawable keep the index handle.
    * <br>
    * Root in index 0 + differentiations in subsequent indexes
    * 
    */
   private ByteObject[]    rootMODs;

   /**
    * tracks for each styleids which flags have a StyleDefinition 
    */
   private int             stateStyleFlags;

   /**
    * {@link ByteObject} style definition linked to IDrawable style state integer.
    * <br>
    * <br>
    * Tracks styles associated with
    * <li> {@link ITechDrawable#STYLE_04_GREYED}
    * <li> {@link ITechDrawable#STYLE_05_SELECTED}
    * <li> {@link ITechDrawable#STYLE_06_FOCUSED_KEY}
    * <li> {@link ITechDrawable#STYLE_07_FOCUSED_POINTER}
    * 
    */
   private IntToObjects    stateStyles;

   /**
    * Reflexive defining the index in which this class style is store in the repository.
    */
   private int             styleclassID             = -1;

   protected final UCtx    uc;

   private StyleClass(GuiCtx gc) {
      this.gc = gc;
      this.uc = gc.getUCtx();
   }
   public ICtx getCtxOwner() {
      return gc;
   }
   /**
    * 
    * @param id
    * @param rootID the repository reference ID for the style
    */
   public StyleClass(GuiCtx gc, ByteObject rootStyle) {
      this.gc = gc;
      this.uc = gc.getUCtx();
      if (rootStyle == null) {
         throw new NullPointerException("RootStyle is null");
      }
      rootMODs = new ByteObject[] { rootStyle };
      cacheStyleInit();
      FLAG_CACHE_STYLE = gc.getSettingsWrapper().hasStyleClassCache();
   }
   public int getStatorableClassID() {
      throw new RuntimeException("Must be implemented by subclass");
   }

   /**
    * Builds a state integer from a fullState and state styles.
    * <br>
    * If only Selected Flag has a style here. and fullState does not
    * have Selected flag ON, returned value is 0 (i.e. root) 
    * <br>
    * <br>
    * @param fullState states with several state bits.
    * @return
    */
   private int buildPossibleStateFlags(int fullState) {
      if (stateStyles == null) {
         return 0;
      }
      int possibles = 0;
      int num = stateStyles.getLength();
      for (int i = 0; i < num; i++) {
         int stateFlag = stateStyles.ints[i];
         if ((fullState & stateFlag) == stateFlag) {
            possibles |= stateFlag;
         }
      }
      return possibles;
   }

   private void cacheStyleAdd(int handle, int possibles, ByteObject p) {
      if (FLAG_CACHE_STYLE) {
         //add the style combination in the bag of possibles.
         if (cachePossibles[handle] == null) {
            cachePossibles[handle] = new IntToObjects(gc.getUCtx());
         }
         cachePossibles[handle].add(possibles, p);
      }
   }

   private void cacheStyleCreateSpot() {
      if (FLAG_CACHE_STYLE) {
         IntToObjects[] old = cachePossibles;
         cachePossibles = new IntToObjects[old.length + 1];
         for (int i = 0; i < old.length; i++) {
            cachePossibles[i] = old[i];
         }
      }
   }

   private ByteObject cacheStyleGet(int handle, int possibles) {
      if (FLAG_CACHE_STYLE) {
         if (cachePossibles[handle] != null) {
            return (ByteObject) cachePossibles[handle].findIntObject(possibles);
         }
      }
      return null;
   }

   private void cacheStyleInit() {
      if (FLAG_CACHE_STYLE) {
         cachePossibles = new IntToObjects[1];
      }
   }

   /**
    * Merge current {@link StyleClass} to the given style class.
    * <br>
    * <br>
    * @param sc
    */
   public void cloneTo(StyleClass sc) {
      ByteObject boSt = sc.rootMODs[0].mergeByteObject(rootMODs[0]);
      if (ctypeObjects != null) {
         for (int i = 0; i < ctypeStyles.nextempty; i++) {
            sc.linkCTypeStyle((ByteObject) ctypeStyles.objects[i], ctypeStyles.ints[i]);
         }
      }
      if (stateStyles != null) {
         for (int i = 0; i < stateStyles.nextempty; i++) {
            sc.linkStateStyle((ByteObject) stateStyles.objects[i], stateStyles.ints[i]);
         }
      }
      if (objects != null) {
         for (int i = 0; i < objects.nextempty; i++) {
            sc.linkByteObject((ByteObject) objects.objects[i], objects.ints[i]);
         }
      }
      if (classes != null) {
         for (int i = 0; i < classes.nextempty; i++) {
            sc.linkStyleClass((StyleClass) classes.objects[i], classes.ints[i]);
         }
      }
   }

   /**
    * Default {@link ByteObject} associated with ID.
    * <br>
    * <br>
    * CType, object are fetched and merged with the default one.
    * @param techKey
    * @return
    */
   public ByteObject getByteObject(int linkID) {
      if (objects == null) {
         return null;
      } else {
         return (ByteObject) objects.findIntObject(linkID);
      }
   }

   /**
    * When null in current {@link StyleClass}, then look up in parent
    * if null or null, look up in root which must define the link otherwise
    * an exception is thrown
    * @param linkID
    * @return
    */
   public ByteObject getByteObjectNotNull(int linkID) {
      ByteObject bo = null;
      if (objects != null) {
         bo = (ByteObject) objects.findIntObject(linkID);
      }
      if (bo == null) {
         bo = gc.getDefaultSC().getByteObject(linkID);
      }
      if (bo == null) {
         throw new StyleException(this, StyleException.LINK_TYPE_0_BYTEOBJECT, linkID);
      }
      return bo;
   }

   public ByteObject getCTypeObject(int ctype) {
      if (ctypeObjects == null) {
         return null;
      } else {
         return (ByteObject) ctypeObjects.findIntObject(ctype);
      }
   }

   /**
    * The (Transparent) style definition for the ctype in this space
    * <br>
    * <br>
    * 
    * @param ctype
    * @return null if no style for CType 
    */
   public ByteObject getCTypeStyle(int ctype) {
      if (ctypeStyles == null) {
         return null;
      } else {
         return (ByteObject) ctypeStyles.findIntObject(ctype);
      }
   }

   /**
    * When the state of a {@link IDrawable} changes, the {@link Drawable} asks its {@link StyleClass} for a style pointer update.
    * <br>
    * <br>
    * {@link StyleClass} creates a style from the style pointer.
    * <br>
    * Reads the individual state style definition and builds a new style object.
    * <br>
    * <br>
    * When there are several states, the order of the merge is bit wise for style states.
    * <li>{@link ITechDrawable#STYLE_01_CUSTOM}
    * <li>{@link ITechDrawable#STYLE_03_MARKED}
    * <li>{@link ITechDrawable#STYLE_04_GREYED}
    * <li>{@link ITechDrawable#STYLE_05_SELECTED}
    * <br>
    * <br>
    * @param allStates
    * @param handle identifies the substyle (ctype + struct) requesting the state style update
    * @return
    */
   public int getFullStyle(int allStates, int handle) {
      if (stateStyles == null || allStates == 0) {
         //only root is available
      }

      ByteObject rootMOD = rootMODs[handle];

      //builds the possible state for which we have a style definition
      int possibles = buildPossibleStateFlags(allStates);
      if (possibles == 0) {
         //no specific states, thus we have to return the root style for that ctype/struct modification handle
         return 0;
      }
      //if style has already been merged/computed in a cache, return it. 
      ByteObject stateStyleRefID = cacheStyleGet(handle, possibles);
      //the style of the states' combination
      ByteObject p = rootMOD;
      //take all the state style definitions and incrementally merge them
      int num = stateStyles.getLength();
      for (int i = 0; i < num; i++) {
         ByteObject stateStyle = (ByteObject) stateStyles.objects[i];
         int stateFlag = stateStyles.ints[i];
         if ((allStates & stateFlag) == stateFlag) {
            p = p.mergeByteObject(stateStyle);
         }
      }
      //p is the style
      cacheStyleAdd(handle, possibles, p);
      return linkSubStyle(p);
   }

   public int getID() {
      return styleclassID;
   }

   /**
    * The style state flags to be used when computing drawable width and height during init.
    * 
    * All {@link IDrawable} will compute the {@link IDrawable#init(int, int)} using this style flags definition.
    * Unless equal to zero.
    * <br>
    * <br>
    * This is to allow a Drawable to compute its size on the relevant style combination.
    * <br>
    * <br>
    * @return
    */
   public int getInitStyle() {
      return initStyle;
   }

   public String getName() {
      return name;
   }

   /**
    * Gets the reference to the root ByteObject style that was
    * set in the constructor and that is the basis for all 
    * other state styles
    * @return non null {@link ByteObject}.
    */
   public ByteObject getRootStyle() {
      return rootMODs[0];
   }

   /**
    * This method is used to in order to catch un linked style class.
    * <br>
    * <br>
    * Don't call this method if the style class could be null.
    * <br>
    * <br>
    * 
    * @param linkID
    * @return
    */
   public StyleClass getSCNotNull(int linkID) {
      Object o = null;
      if (classes != null) {
         o = classes.findIntObject(linkID);
      }
      if (o == null) {
         StyleClass sc = gc.getDefaultSC();
         if (this != sc) {
            o = sc.getStyleClass(linkID);
         }
         if (o == null) {
            //generate warning

            //#debug
            String msg = "StyleClass not found for LinkID=" + linkID + " " + ToStringStaticGui.toStringLinkStatic(linkID) + ". Default Class will be used";

            toDLog().pInit1(msg, this, StyleClass.class, "getSCNotNull");

            o = sc;
         }
      }
      return (StyleClass) o;
   }

   /**
    * Search
    * @param styleFlag
    * @return null if no style for that flag
    */
   public ByteObject getStateStyle(int styleFlag) {
      if (stateStyles != null && BitUtils.hasFlag(stateStyleFlags, styleFlag)) {
         return (ByteObject) stateStyles.findIntObject(styleFlag);
      } else {
         return null;
      }
   }

   public ByteObject getStyle(int states) {
      return getStyle(states, 0, null);
   }

   public ByteObject getStyle(int states, int ctype) {
      return getStyle(states, ctype, null);
   }

   /**
    * 
    * If caching is done, it must be done on ctype/structStyle and states
    * <br>
    * <br>
    * @param states states style are merged from {@link IDrawable#STYL}
    * @param ctype a ctype must be different than zero to have an impact on the styling.
    * @param structStyle
    * @return
    */
   public ByteObject getStyle(int states, int ctype, ByteObject structStyle) {
      //first combine ctype and structStyle
      ByteObject newStyle = rootMODs[0];
      if (newStyle == null) {
         throw new RuntimeException("Root Style of StyleClass is null");
      }
      if (ctype != 0) {
         //get the ctype style if it exists are stored in the StyleID space
         ByteObject ctypeStyle = getCTypeStyle(ctype);
         if (ctypeStyle != null) {
            //the style here does not have a static id yet
            newStyle = newStyle.mergeByteObject(ctypeStyle);
         }
      }
      if (structStyle != null) {
         //the style here does not have a static id yet
         newStyle = newStyle.mergeByteObject(structStyle);
      }
      if (states != 0 && stateStyles != null) {
         //apply
         int possibles = buildPossibleStateFlags(states);
         //reads all the state style definitions and incrementally merge them
         int num = stateStyles.getLength();
         for (int i = 0; i < num; i++) {
            ByteObject stateStyle = (ByteObject) stateStyles.objects[i];
            int stateFlag = stateStyles.ints[i];
            if (stateStyle == null) {
               throw new NullPointerException();
            }
            if ((possibles & stateFlag) == stateFlag) {
               try {
                  newStyle = newStyle.mergeByteObject(stateStyle);
               } catch (ArrayIndexOutOfBoundsException e) {
                  //#debug
                  toDLog().pNull("msg", stateStyle, StyleClass.class, "getStyle", LVL_05_FINE, true);
                  throw e;
               }
            }
         }
      }
      if (newStyle == null)
         throw new NullPointerException("StyleClass#getStyle for created Style is null");
      return newStyle;
   }

   /**
    * A style class can never be null. so returns default and log a warning on the {@link IUserLink} style channel
    * Null if cannot find a {@link StyleClass} linked to that ID.
    * <br>
    * <br>
    * Why should a {@link StyleClass} belong to another?
    * <br>
    * Because, not all the same class items want to use the same style
    * <br>
    * <br>
    * Isn't this decision made at the code level then? Saying the scrollbars X will use a style
    * and scrollbar Y use another style?
    * In J2ME polish, the style is named. this goes by named style class. So here it behaves the same.
    * It does need some work to link the mesh of style classes.
    * <li> Viewpane
    * <li>Scrollbar
    * <li> 4 Style class for the 4 scrollbar elements.
    * <br>
    * <br>
    * Each ViewPane style class is independant of each other.
    * <br>
    * <br>
    * 
    * @param linkID
    * @return
    */
   public StyleClass getStyleClass(int linkID) {
      return getSCNotNull(linkID);
      //      if (classes == null) {
      //         return null;
      //      } else {
      //         Object o = classes.findIntObject(linkID);
      //         if (o instanceof StyleClass) {
      //            return (StyleClass) o;
      //         } else {
      //            if (o != null) {
      //               //#debug
      //               throw new IllegalStateException("");
      //            }
      //            return null;
      //         }
      //      }
   }

   /**
    * 
    * @param linkID
    * @param backup
    * @return
    */
   public StyleClass getStyleClass(int linkID, int backup) {
      StyleClass sc = getStyleClass(linkID);
      if (sc == null) {
         return getSCNotNull(backup);
      }
      return sc;
   }

   public ByteObject getStyleDrwP(int handle) {
      return rootMODs[handle];
   }

   /**
    * 
    * @param flag
    * @return true if a specific style definition exists for this state flag
    */
   public boolean hasFlagStyle(int flag) {
      return (stateStyleFlags & flag) == flag;
   }

   public boolean isEquals(Object o) {
      // self check
      if (this == o)
         return true;
      // null check
      if (o == null)
         return false;
      // type check and cast
      if (getClass() != o.getClass())
         return false;
      StyleClass sc2 = (StyleClass) o;

      throw new RuntimeException();
   }

   /**
    * Associates a given TechParam to its TechID for this style
    * @param techID
    * @param refID reference ID to the ByteObject TechParam
    */
   public void linkByteObject(ByteObject bo, int linkID) {
      if (objects == null) {
         objects = new IntToObjects(uc);
      }
      objects.add(bo, linkID);
   }

   /**
    * Recipient for linking a {@link ByteObject} other than styling. Usually a Tech paran.
    * <br>
    * <br>
    * Example different String tech param depending on ctype.
    * <br>
    * <br>
    * 
    * @param obj
    * @param ctype
    */
   public void linkCTypeObject(ByteObject obj, int ctype) {
      if (isImmutable) {
         throw new IllegalArgumentException("Cannot Modify Immutable StyleClass");
      }

      if (ctypeObjects == null) {
         ctypeObjects = new IntToObjects(uc);
      }
      ctypeObjects.add(obj, ctype);
   }

   /**
    * Add a ctype linked to its style definition
    * @param refid id to the style (usual transparent) definition of the ctype
    * @param ctype
    */
   public void linkCTypeStyle(ByteObject style, int ctype) {
      if (isImmutable) {
         throw new IllegalArgumentException("Cannot Modify Immutable StyleClass");
      }

      if (ctypeStyles == null) {
         ctypeStyles = new IntToObjects(uc);
      }
      ctypeStyles.add(style, ctype);
   }

   /**
    * Repository of CType style definitions
    * RefIDs will be fetched with
    * 
    * @param array of style refids
    */
   public void linkCTypeStyles(ByteObject[] ctypes) {
      if (isImmutable) {
         throw new IllegalArgumentException("Cannot Modify Immutable StyleClass");
      }

      for (int i = 0; i < ctypes.length; i++) {
         linkCTypeStyle(ctypes[i], i);
      }
   }

   /**
   * Links that style to the state flag defined in {@link IDrawable}.
   * <br>
   * <br>
   * @param style defintion. when null, nothing happens.
    * @param stateStyle {@link ITechDrawable#STYLE_04_GREYED} or {@link ITechDrawable#STYLE_05_SELECTED}, ...
   */
   public void linkStateStyle(ByteObject style, int stateStyle) {
      if (isImmutable) {
         throw new IllegalArgumentException("Cannot Modify Immutable StyleClass");
      }

      if (style == null)
         return;
      //System.out.println("#StyleID Linking Style (ref ID = " + refID + ") for state " + Drawable.debugState(stateFlag));
      if (stateStyles == null) {
         stateStyles = new IntToObjects(uc);
      }
      stateStyleFlags |= stateStyle;
      stateStyles.add(style, stateStyle);
   }

   /**
    * Links a whole instance of a {@link StyleClass}.
    * <br>
    * <br>
    * ViewDrawable code retrieves ViewPane's {@link StyleClass} that was linked using this method.
    * @param sc
    * @param linkID
    */
   public void linkStyleClass(StyleClass sc, int linkID) {
      if (isImmutable) {
         throw new IllegalArgumentException("Cannot Modify Immutable StyleClass");
      }

      if (classes == null) {
         classes = new IntToObjects(uc);
      }
      classes.add(sc, linkID);
   }

   /**
    * Link a style modification.
    * <br>
    * <br>
    * Returns a handle for future call back with getFullStyle
    * <br>
    * @param refid reference to a ByteObject style
    * @return integer
    */
   public int linkSubStyle(ByteObject style) {
      if (isImmutable) {
         throw new IllegalArgumentException("Cannot Modify Immutable StyleClass");
      }

      int index = ArrayUtils.getFirstIndex(rootMODs, style);
      if (index == -1) {
         rootMODs = gc.getBOC().getBOU().addByteObject(rootMODs, style);
         cacheStyleCreateSpot();
         index = rootMODs.length - 1;
      }
      return index;
   }

   /**
    * Style classes can be read from a program file that was compiled.
    * <br>
    * <br>
    * The app launcher reads that file instead of creating StyleClass from static definitions.
    * <br>
    * <br>
    * 
    * @param data
    * @return
    */
   public StyleClass[] loadFromByteArray(byte[] data) {
      BADataIS bc = gc.getUCtx().createNewBADataIS(data);
      int num = bc.readInt();
      int numObjects = bc.readInt();
      StyleClass[] ar = new StyleClass[num];
      IntToObjects ito = new IntToObjects(gc.getUCtx(), numObjects);
      for (int i = 0; i < ar.length; i++) {
         StyleClass sc = null;
         int magicword = bc.readInt();
         if (magicword == IByteObject.MAGIC_WORD_DEF) {
            sc = new StyleClass(gc);
            sc.serializeFrom(ito, bc);
         } else {
            int index = bc.readInt();
            sc = (StyleClass) ito.objects[index];
         }
         ar[i] = sc;

      }
      return ar;
   }

   public IntToObjects readStruct(IntToObjects ito, BADataIS bc) {
      int objectsNum = bc.readInt();
      IntToObjects objects = null;
      if (objectsNum != 0) {
         objects = new IntToObjects(uc, objectsNum);
         for (int i = 0; i < objectsNum; i++) {
            int linkValue = bc.readInt();
            int readType = bc.readInt();
            if (readType == StyleClass.SERIAL_TYPE_0_NULL) {
               objects.add(null, linkValue);
            } else if (readType == StyleClass.SERIAL_TYPE_1_BYTEOBJECT) {
               ByteObject bo = gc.getBOC().getByteObjectFactory().createByteObjectFromWrapIto(bc, ito);
               objects.add(bo, linkValue);
            } else if (readType == StyleClass.SERIAL_TYPE_2_STYLECLASS) {
               StyleClass sc = serializeUnwrap(ito, bc);
               objects.add(sc, linkValue);
            }
         }
      }
      return objects;
   }

   /**
    * Serialize the current array of {@link StyleClass}
    * <br>
    * <br>
    * 
    * @return
    */
   public byte[] serialize(StyleClass[] classes) {
      UCtx uc = gc.getUCtx();
      BADataOS dos = uc.createNewBADataOS();
      IntToObjects ito = new IntToObjects(uc, 20);
      //4 byte for number of classes.
      dos.writeInt(classes.length);
      dos.writeInt(0); //future number of objects
      for (int i = 0; i < classes.length; i++) {
         classes[i].serializeTo(ito, dos);
      }

      byte[] data = dos.getByteCopy();
      //write total number of objects
      int numOfObjects = ito.nextempty;
      int offsetNumOfObjects = 4;
      IntUtils.writeIntBE(data, offsetNumOfObjects, numOfObjects);
      return data;
   }

   public void serializeFrom(IntToObjects ito, BADataIS bc) {
      ito.add(this, 0);
      name = bc.readString();
      styleclassID = bc.readInt();
      initStyle = bc.readInt();

      ByteObject rootStyle = gc.getBOC().getByteObjectFactory().createByteObjectFromWrapIto(bc, ito);
      rootMODs = new ByteObject[] { rootStyle };

      objects = readStruct(ito, bc);
      ctypeStyles = readStruct(ito, bc);
      stateStyles = readStruct(ito, bc);
      classes = readStruct(ito, bc);
   }

   /**
    * Unwraps the {@link StyleClass}
    * <br>
    * <br>
    * 
    * @param dc
    * @param data
    * @return
    */
   public StyleClass serializeReverseSC(byte[] data) {
      UCtx uc = gc.getUCtx();
      BADataIS dis = uc.createNewBADataIS(data);
      return serializeUnwrap(new IntToObjects(uc), dis);
   }

   /**
    * Tracks index reference of object
    * <br>
    * <br>
    * When a new object is written, add it t
    * <br>
    * <br>
    * For a correct full serialization, each sub object of each ByteObject must be
    * @param ito
    * @return
    */
   public void serializeTo(IntToObjects ito, BADataOS dos) {
      int index = ito.findObjectRef(this);
      if (index == -1) {
         ito.add(this, dos.size());
         //do it
         dos.writeInt(IByteObject.MAGIC_WORD_DEF);
         dos.writeString(name);
         dos.writeInt(styleclassID);
         dos.writeInt(initStyle);

         rootMODs[0].serializeTo(ito, dos);

         serializeToByteObject(ito, dos, objects);
         serializeToByteObject(ito, dos, ctypeStyles);
         serializeToByteObject(ito, dos, stateStyles);
         serializeToByteObject(ito, dos, classes);

      } else {
         //already there
         dos.writeInt(IByteObject.MAGIC_WORD_POINTER);
         dos.writeInt(index);
      }

   }

   /**
    * Serialize the StyleClass.
    * <br>
    * <br>
    * Manage cycling.
    * <br>
    * <br>
    * 
    * @return
    */
   public byte[] serializeToByteArray() {

      BAByteOS bo = new BAByteOS(uc);
      BADataOS dos = new BADataOS(uc, bo);
      IntToObjects ito = new IntToObjects(uc);
      serializeTo(ito, dos);
      return bo.toByteArray();
   }

   /**
    * Serialize stuff inside an {@link IntToObjects}.
    * <br>
    * <br>
    * 
    * @param ito
    * @param dos
    * @param objects
    */
   private void serializeToByteObject(IntToObjects ito, BADataOS dos, IntToObjects objects) {
      if (objects != null) {
         dos.writeInt(objects.nextempty);
         for (int i = 0; i < objects.nextempty; i++) {
            Object o = objects.objects[i];
            dos.writeInt(objects.ints[i]);
            if (o == null) {
               dos.writeInt(SERIAL_TYPE_0_NULL);
            } else if (o instanceof ByteObject) {
               dos.writeInt(SERIAL_TYPE_1_BYTEOBJECT);
               ByteObject bo = (ByteObject) o;
               //header for saying a ByteO?
               bo.serializeTo(ito, dos);
            } else if (o instanceof StyleClass) {
               dos.writeInt(SERIAL_TYPE_2_STYLECLASS);
               StyleClass sc = (StyleClass) o;
               sc.serializeTo(ito, dos);
            }
         }
      } else {
         dos.writeInt(0);
      }
   }

   public StyleClass serializeUnwrap(IntToObjects ito, BADataIS bc) {
      int magic = bc.readInt();
      if (magic == IByteObject.MAGIC_WORD_DEF) {
         StyleClass sc = new StyleClass(gc);
         sc.serializeFrom(ito, bc);
         return sc;
      } else {
         int pointer = bc.readShort();
         return (StyleClass) ito.objects[pointer];
      }
   }

   public void setCTypeStyle(ByteObject style, int ctype) {
      if (isImmutable) {
         throw new IllegalArgumentException("Cannot Modify Immutable StyleClass");
      }

      if (ctypeStyles == null) {
         ctypeStyles = new IntToObjects(uc);
      }
      ctypeStyles.ensureRoom(ctype, 4);
      ctypeStyles.setObject(style, ctype);
   }

   public void setImmutable() {
      isImmutable = true;
      if (ctypeObjects != null) {
         for (int i = 0; i < ctypeStyles.nextempty; i++) {
            ((ByteObject) ctypeStyles.objects[i]).setImmutable();
         }
      }
      if (stateStyles != null) {
         for (int i = 0; i < stateStyles.nextempty; i++) {
            ((ByteObject) stateStyles.objects[i]).setImmutable();
         }
      }
      if (objects != null) {
         for (int i = 0; i < objects.nextempty; i++) {
            ((ByteObject) objects.objects[i]).setImmutable();
         }
      }
      if (classes != null) {
         for (int i = 0; i < classes.nextempty; i++) {
            ((StyleClass) classes.objects[i]).setImmutable();
         }
      }
   }

   /**
    * See {@link StyleClass#getInitStyle()}
    * @param is
    */
   public void setInitStyle(int is) {
      if (isImmutable) {
         throw new IllegalArgumentException("Cannot Modify Immutable StyleClass");
      }
      initStyle = is;
   }

   public void setName(String str) {
      name = str;
   }

   public void setParent(StyleClass sc) {
      parent = sc;
   }

   public void setRoot(ByteObject style) {
      rootMODs[0] = style;
   }

   public void stateReadFrom(StatorReader state) {
      IntToObjects ito = new IntToObjects(uc);
      StyleClass sc = new StyleClass(gc);
      BADataIS dataReader = state.getReader();
      sc.serializeFrom(ito, dataReader);
   }

   public void stateWriteTo(StatorWriter state) {
      BADataOS dataWriter = state.getWriter();
      IntToObjects ito = new IntToObjects(uc);
      serializeTo(ito, dataWriter);
   }

   public int subStringWidth(int styleKey, String str, int offset, int len) {
      ByteObject style = getStyleDrwP(styleKey);
      return gc.getDC().getFxStringOperator().getStringSubStringWidth(style, str, offset, len);
   }

   //#mdebug

   public IDLog toDLog() {
      return gc.toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, StyleClass.class);
      if (name != null) {
         dc.append(' ');
         dc.append(name);
      }

      ByteObject rootStyle = getRootStyle();
      if (dc.hasFlagData(uc, IToStringFlags.FLAG_DATA_01_SUCCINT)) {
         dc.nl();
         dc.append("#RootStyle " + rootStyle.getMyHashCode());
      } else {
         dc.nlLvl("RootStyle", rootStyle);
      }

      dc.nl();
      dc.append("#Root Modified Styles (");
      dc.append((rootMODs.length - 1));
      dc.append(")");
      for (int i = 1; i < rootMODs.length; i++) {
         if (rootMODs[i] != null) {
            dc.append("#" + i + "");
            dc.nlLvl("", rootMODs[i]);
         }
      }
      ToStringStates stringableInt = new ToStringStates();
      dc.nlLvl(stateStyles, "States Styles Linked", "State", stringableInt);

      ToStringLinkIDs stringableLinks = new ToStringLinkIDs();
      dc.nlLvl(objects, "ByteObjects Linked", "LinkID", stringableLinks);
      dc.nlLvl(ctypeStyles, "CTypes Styles", "CType", null);
      dc.nlLvl(classes, "Sub StyleClass", "SC #", stringableLinks);

      dc.newLevel();

      dc.appendVarWithSpace("FLAG_CACHE_STYLE", FLAG_CACHE_STYLE);

      if (FLAG_CACHE_STYLE) {
         dc.nlLvl(classes, "Possibles cached", "", stringableLinks);
      }

   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "StyleClass");
      if (name != null) {
         dc.append("Name=");
         dc.append(name);
      }
      //find if it is top class
      int id = -1;
      StyleClass[] cc = gc.getClasses();
      for (int i = 0; i < cc.length; i++) {
         if (cc[i] == this) {
            id = i;
         }
      }
      dc.appendVarWithSpace("TopID", id);
      dc.append(' ');
      dc.append(getRootStyle().getMyHashCode());
      dc.append(' ');
      dc.append(hashCode());
   }

   public UCtx toStringGetUCtx() {
      return gc.getUCtx();
   }
   //#enddebug
}
