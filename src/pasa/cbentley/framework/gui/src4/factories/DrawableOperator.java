package pasa.cbentley.framework.gui.src4.factories;

import pasa.cbentley.byteobjects.src4.core.BOAbstractOperator;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.ctx.ToStringStaticGui;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOScrollBar;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOViewPane;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;
import pasa.cbentley.layouter.src4.engine.LayoutOperator;

public class DrawableOperator extends BOAbstractOperator implements IBOScrollBar, ITechViewPane {

   protected final GuiCtx gc;

   public DrawableOperator(GuiCtx gc) {
      super(gc.getBOC());
      this.gc = gc;
   }

   //#mdebug
   public void toStringTech(ByteObject tech, Dctx sb) {
      sb.append("#TechSB");
      LayoutOperator sizer = gc.getLAC().getLayoutOperator();
      if (sizer != null) {
         sb.nl();
         sb.append("1st size = " + sizer.codedSizeToString1Line(tech.getValue(SB_OFFSET_03_PRIMARY_SIZE4, 4)));
         sb.append("; ");
         sb.append("2nd size = " + sizer.codedSizeToString1Line(tech.getValue(SB_OFFSET_04_SECOND_SIZE4, 4)));
         sb.append("; ");
         sb.append("3rd size = " + sizer.codedSizeToString1Line(tech.getValue(SB_OFFSET_05_THIRD_SIZE4, 4)));
      } else {
         sb.append("Size is Null");
      }
      sb.nl();
      sb.append("Wrapper=" + tech.hasFlag(SB_OFFSET_01_FLAG, SB_FLAG_3_WRAPPER));
      sb.append(" WrapperFill=" + tech.hasFlag(SB_OFFSET_01_FLAG, SB_FLAG_4_WRAPPER_FILL));
      sb.append(" ButtonOnBlock=" + tech.hasFlag(SB_OFFSET_01_FLAG, SB_FLAG_5_ARROW_ON_BLOC));
      sb.append(" 6BLOCK_INVERSE=" + tech.hasFlag(SB_OFFSET_01_FLAG, SB_FLAG_6_BLOCK_INVERSE));
      sb.append(" 7AROUND_THE_CLOCK=" + tech.hasFlag(SB_OFFSET_01_FLAG, SB_FLAG_7_AROUND_THE_CLOCK));
      sb.append(" ShowConfig=" + tech.hasFlag(SB_OFFSET_02_FLAGX, SB_FLAGX_1_SHOW_CONFIG));
   }

   //#enddebug

   public void toStringViewPaneTech(ByteObject tech, Dctx sb) {
      sb.append("#Tech ");
      if (tech.hasFlag(IBOViewPane.VP_OFFSET_01_FLAG, IBOViewPane.VP_FLAG_3_SCROLLBAR_MASTER)) {
         sb.append("Master:Header Slave:Sb");
      } else {
         sb.append("Slave:Header Master:Sb");
      }
      int typeSB = tech.getValue(IBOViewPane.VP_OFFSET_10_COMPETITION_SB_TYPE1, 1);
      sb.nl();
      sb.append("CompetSb:");
      sb.append(ToStringStaticGui.debugCompetType(typeSB));
      if (tech.hasFlag(IBOViewPane.VP_OFFSET_01_FLAG, IBOViewPane.VP_FLAG_7_COMPET_OVERLAY_SB)) {
         sb.append(" Overlay");
      }
      sb.nl();
      int typeH = tech.getValue(IBOViewPane.VP_OFFSET_11_COMPETITION_HEADER_TYPE1, 1);
      sb.append("CompetHeader:");
      sb.append(ToStringStaticGui.debugCompetType(typeH));
      if (tech.hasFlag(IBOViewPane.VP_OFFSET_01_FLAG, IBOViewPane.VP_FLAG_6_COMPET_OVERLAY_HEADER)) {
         sb.append(" Overlay");
      }
      sb.append("Header");
      sb.append(' ');
      sb.append("[TBLR=");
      sb.append(ToStringStaticGui.debugPlanetStruct(tech.get2Bits1(IBOViewPane.VP_OFFSET_04_HEADER_PLANET_MODE1)));
      sb.append("," + ToStringStaticGui.debugPlanetStruct(tech.get2Bits2(IBOViewPane.VP_OFFSET_04_HEADER_PLANET_MODE1)));
      sb.append(" " + ToStringStaticGui.debugPlanetStruct(tech.get2Bits3(IBOViewPane.VP_OFFSET_04_HEADER_PLANET_MODE1)));
      sb.append("," + ToStringStaticGui.debugPlanetStruct(tech.get2Bits4(IBOViewPane.VP_OFFSET_04_HEADER_PLANET_MODE1)));

      sb.append(" sbW=" + ToStringStaticGui.debugPlanetStruct(tech.get2Bits1(IBOViewPane.VP_OFFSET_05_SCROLLBAR_MODE1)));
      sb.append(" sbH=" + ToStringStaticGui.debugPlanetStruct(tech.get2Bits2(IBOViewPane.VP_OFFSET_05_SCROLLBAR_MODE1)));
      sb.append("]");

      if (tech.hasFlag(IBOViewPane.VP_OFFSET_01_FLAG, IBOViewPane.VP_FLAGX_1_STYLE_VIEWPANE)) {
         sb.append(" ViewPane Style Applied");
      }
      sb.nl();

      sb.append("[H,V] type=" + ToStringStaticGui.debugScrollType(tech.get2Bits3(IBOViewPane.VP_OFFSET_05_SCROLLBAR_MODE1)));
      sb.append("," + ToStringStaticGui.debugScrollType(tech.get2Bits4(IBOViewPane.VP_OFFSET_05_SCROLLBAR_MODE1)));
      sb.append(" visual=" + ToStringStaticGui.debugScrollVisual(tech.get4Bits1(IBOViewPane.VP_OFFSET_06_VISUAL_LEFT_OVER1)));
      sb.append("," + ToStringStaticGui.debugScrollVisual(tech.get4Bits2(IBOViewPane.VP_OFFSET_06_VISUAL_LEFT_OVER1)));
      sb.append(" move=" + ToStringStaticGui.debugScrollMove(tech.get2Bits1(IBOViewPane.VP_OFFSET_08_MOVE_TYPE1)));
      sb.append("," + ToStringStaticGui.debugScrollMove(tech.get2Bits3(IBOViewPane.VP_OFFSET_08_MOVE_TYPE1)));
      sb.append(" partial=" + ToStringStaticGui.debugScrollPartial(tech.get2Bits2(IBOViewPane.VP_OFFSET_08_MOVE_TYPE1)));
      sb.append("," + ToStringStaticGui.debugScrollPartial(tech.get2Bits4(IBOViewPane.VP_OFFSET_08_MOVE_TYPE1)));

   }

   /**
    * Depending on Horiz/Vert, uses the right link id for tech {@link ByteObject}.
    * @param sc
    * @param tech
    */
   public void linkTech(StyleClass sc, ByteObject tech) {
      int techid = IBOTypesGui.LINK_69_TECH_H_SCROLLBAR;
      if (tech.hasFlag(SB_OFFSET_01_FLAG, SB_FLAG_1_VERT)) {
         techid = IBOTypesGui.LINK_68_TECH_V_SCROLLBAR;
      }
      sc.linkByteObject(tech, techid);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "DrawableCoreFactory");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "DrawableCoreFactory");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

}
