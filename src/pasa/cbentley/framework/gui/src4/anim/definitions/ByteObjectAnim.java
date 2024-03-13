package pasa.cbentley.framework.gui.src4.anim.definitions;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.objects.color.GradientFunction;
import pasa.cbentley.byteobjects.src4.objects.function.Function;
import pasa.cbentley.byteobjects.src4.objects.function.IBOAction;
import pasa.cbentley.byteobjects.src4.objects.function.ITechAction;
import pasa.cbentley.byteobjects.src4.objects.function.ITechFunction;
import pasa.cbentley.byteobjects.src4.objects.pointer.PointerOperator;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.drawx.src4.style.IBOStyle;
import pasa.cbentley.framework.gui.src4.anim.IBOAnim;
import pasa.cbentley.framework.gui.src4.anim.ITechAnim;
import pasa.cbentley.framework.gui.src4.anim.base.DrawableAnim;
import pasa.cbentley.framework.gui.src4.core.FigDrawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IAnimable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

/**
 * Animation that changes the value of a target {@link ByteObject} using a function.
 * <p>
 * An {@link IDrawable} is given most of the time. When no {@link IDrawable} is given animation is not able to know where
 * are the {@link IDrawable} using the target {@link ByteObject}. Therefore a full repaint is done.
 * </p>
 * 
 * <p>
 * When the Animation Page has finished
 * </p>
 * 
 * The Target may be obtained from the {@link IDrawable}
 * <li> {@link IDrawable#getStyle()}
 * 
 * 
 * 
 * <p>
 * Action : 
 * <li>start {@link IAction#ACTION_TIME_4_ANIM_START}
 * <li>end {@link IAction#ACTION_TIME_4_ANIM_START}
 * <li>reset {@link IAction#ACTION_TIME_3_RESET}
 * <li>step {@link IAction#ACTION_TIME_2_STEP}
 * </p>
 * 
 * @author Charles-Philip Bentley
 *
 */
public class ByteObjectAnim extends DrawableAnim {

   /**
    * Action to be executed on the current target.
    * <br>
    * <br>
    * 
    * Example:
    * <li>A figure with a Gradient is the target.
    * <li>Action pointer toggles a flag of the gradient definition.
    * <br>
    * <br>
    *   
    */
   private ByteObject action;

   /**
    * Pointer to the value to be modified in the target
    */
   private ByteObject pointer;

   /**
    * Real definitive Target.
    * <br>
    * When Target is null, Animation has no effect
    */
   private ByteObject target;

   /**
    * 
    * @param d
    * @param animDef
    */
   public ByteObjectAnim(GuiCtx gc, IDrawable d, ByteObject animDef) {
      super(gc, d, animDef);
      action = definition.getSubFirst(IBOTypesBOC.TYPE_025_ACTION);
      pointer = definition.getSubFirst(IBOTypesBOC.TYPE_010_POINTER);
   }

   /**
    * With no Drawable defined.
    * <br>
    * <br>
    * 
    * @param d Drawable who will get the repaint call
    * @param target
    * @param index
    * @param bytesize
    * @param f
    */
   public ByteObjectAnim(GuiCtx gc, IDrawable d, Function f, ByteObject target, ByteObject pointer) {
      super(gc, d, f);
      this.pointer = pointer;
      this.target = target;
   }

   /**
    * Extract the target {@link ByteObject} from {@link IDrawable}.
    * <br>
    * Option may required to update the ByteObject at each step.
    */
   public void buildTarget() {
      int targetType = getTarget();
      if (targetType == 0) {
         target = d.getStyle();
      } else if (targetType == ITechAnim.ANIM_TARGET_9_CONTENT && d instanceof FigDrawable) {
         target = ((FigDrawable) d).getFigure();
      } else {
         ByteObject style = d.getStyle();
         int offset = IBOStyle.STYLE_OFFSET_2_FLAGB;
         int flag = 1;
         for (int i = 0; i < targetType; i++) {
            flag <<= 1;
         }
         if (targetType == 1) {
            target = gc.getDC().getStyleOperator().getStyleDrw(style, offset, flag);
         }
      }
   }

   public void lifeEnd() {
      // do we roll back the ByteObject to the original paramters?
      //yes if flag says so
      if (action != null && action.get1(IBOAction.ACTION_OFFSET_03_TIME1) == ITechAction.ACTION_TIME_1_ANIM_END) {
        gc.getBOC().getActionOp().doAction(action, target);
      }
      super.lifeEnd();
   }

   /**
    * We may have to initialized the Animation function.
    * <br>
    * Some gradient function take into input the drawable color and/or dimensions.
    * <br>
    * <br>
    * 
    */
   public void lifeStart() {
      if (definition.get1(IBOAnim.ANIM_OFFSET_01_TYPE1) == ITechAnim.ANIM_TYPE_08_GRADIENT) {

         //decides of the target
         buildTarget();
         //once target is known, Gradient function maybe built
         GradientFunction gf = new GradientFunction(gc.getBOC());
         ByteObject grad = definition.getSubFirst(IBOTypesBOC.TYPE_038_GRADIENT);
         //in an animation context, we use the step for the gradient size
         int size = definition.get2(IBOAnim.ANIM_OFFSET_09_NUM_STEPS2);
         int primaryColor = gc.getBOC().getPointerOperator().getPointerValueEx(this.pointer, target);
         gf.init(primaryColor, size, grad);
         int[] colors = gf.getColors();
         Function f = new Function(gc.getBOC(), colors, ITechFunction.FUN_COUNTER_OP_0_ASC);
         this.stepFunction = f;
      } else {
         ByteObject function = definition.getSubFirst(IBOTypesBOC.TYPE_021_FUNCTION);
         this.stepFunction =  gc.getBOC().getFunctionFactory().createFunction(function);

      }
      super.lifeStart();
      // we don't have to use a RgbImage in this animation
      if (action != null && action.get1(IBOAction.ACTION_OFFSET_03_TIME1) == ITechAction.ACTION_TIME_4_ANIM_START) {
         gc.getBOC().getActionOp().doAction(action, target);
      }
   }

   /**
    * why {@link Function#fx(int)} ?
    * That depends on the pointer characteristics.
    * <br>
    * Here, this class deals with one value.
    * <li>
    * 
    */
   public void nextTurnSub() {
      //must be done in the render thread before rendering
      BOCtx boc = gc.getBOC();
      PointerOperator pointerOperator = boc.getPointerOperator();
      ByteObject realTarget = pointerOperator.getTarget(pointer, target);
      if (realTarget != null) {
         int val = pointerOperator.getPointerValue(pointer, realTarget);
         val = stepFunction.fx(val);
         pointerOperator.setPointerValue(pointer, realTarget, val);
         if (action != null && action.get1(IBOAction.ACTION_OFFSET_03_TIME1) == ITechAction.ACTION_TIME_2_STEP) {
            boc.getActionOp().doAction(action, target);
         }
         //notifies of the change
         d.layoutInvalidate(true);
      } else {
      }
   }

   public void reset() {
      super.reset();
      if (action != null && action.get1(IBOAction.ACTION_OFFSET_03_TIME1) == ITechAction.ACTION_TIME_3_RESET) {
         gc.getBOC().getActionOp().doAction(action, target);
      }
   }

   /**
    * Action to be activated
    * <li> {@link IAnimable#lifeStart()}
    * <li> {@link IAnimable#lifeEnd()}
    * 
    * @param action
    */
   public void setAction(ByteObject action) {
      this.action = action;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "ByteObjectAnim");
      toStringPrivate(dc);
      dc.nlLvl(pointer, "In Target");
      dc.nlLvl(target, "In Target");
      dc.nlLvl(action, "Action");
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {
      
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "ByteObjectAnim");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug
   

   
}
