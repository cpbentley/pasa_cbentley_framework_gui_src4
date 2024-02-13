package pasa.cbentley.framework.gui.src4.factories;

import pasa.cbentley.byteobjects.src4.core.BOAbstractFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.ctx.ToStringStaticGui;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOScrollBar;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOViewPane;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;
import pasa.cbentley.layouter.src4.interfaces.ISizable;

public class DrawableFactory extends BOAbstractFactory implements IBOScrollBar, ITechViewPane {

   protected final GuiCtx gc;

   public DrawableFactory(GuiCtx gc) {
      super(gc.getBOC());
      this.gc = gc;
   }

   //#mdebug
   public void toStringTech(ByteObject tech, Dctx sb, ISizable sizer) {
      sb.append("#TechSB");
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
   public ByteObject getSbTechVertical(int size1, int size2, boolean wrapper) {
      return getSbTech(false, size1, size2, wrapper);
   }

   public ByteObject getSbTechHorizontal(int size1, int size2, boolean wrapper) {
      return getSbTech(true, size1, size2, wrapper);
   }

   public ByteObject getSbTech(boolean horiz, int size1, int size2, boolean wrapper) {
      ByteObject tech = getBOFactory().createByteObject(IBOTypesGui.TYPE_102_SCROLLBAR_TECH, SB_BASIC_SIZE);
      //create tech parameters for scrollbar
      tech.setValue(SB_OFFSET_03_PRIMARY_SIZE4, size1, 4);
      //for button height/width, uses 150 percent of Font Height 
      tech.setValue(SB_OFFSET_04_SECOND_SIZE4, size2, 4);

      tech.setFlag(SB_OFFSET_01_FLAG, SB_FLAG_1_VERT, !horiz);
      tech.setFlag(SB_OFFSET_01_FLAG, SB_FLAG_3_WRAPPER, wrapper);
      return tech;
   }

   public ByteObject getDefViewPaneTech() {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_101_VIEWPANE_TECH, IBOViewPane.VP_BASIC_SIZE);
      p.setValue(IBOViewPane.VP_OFFSET_11_COMPETITION_HEADER_TYPE1, COMPET_HEADER_0_NEUTRAL, 1);
      p.setFlag(IBOViewPane.VP_OFFSET_01_FLAG, IBOViewPane.VP_FLAG_3_SCROLLBAR_MASTER, false);
      p.setFlag(IBOViewPane.VP_OFFSET_01_FLAG, IBOViewPane.VP_FLAGX_1_STYLE_VIEWPANE, false);
      return p;
   }

   public void toStringViewPaneTech(ByteObject boViewPane, Dctx sb) {
      sb.rootN(boViewPane, "boViewPane", DrawableFactory.class, 76);

      sb.nl();
      sb.append("Header");
      sb.append(' ');
      sb.append("[TBLR=");
      sb.append(ToStringStaticGui.debugPlanetStruct(boViewPane.get2Bits1(IBOViewPane.VP_OFFSET_04_HEADER_PLANET_MODE1)));
      sb.append("," + ToStringStaticGui.debugPlanetStruct(boViewPane.get2Bits2(IBOViewPane.VP_OFFSET_04_HEADER_PLANET_MODE1)));
      sb.append(" " + ToStringStaticGui.debugPlanetStruct(boViewPane.get2Bits3(IBOViewPane.VP_OFFSET_04_HEADER_PLANET_MODE1)));
      sb.append("," + ToStringStaticGui.debugPlanetStruct(boViewPane.get2Bits4(IBOViewPane.VP_OFFSET_04_HEADER_PLANET_MODE1)));
      sb.append("] ");
      sb.append(" [");
      sb.append(" sbW=" + ToStringStaticGui.debugPlanetStruct(boViewPane.get2Bits1(IBOViewPane.VP_OFFSET_05_SCROLLBAR_MODE1)));
      sb.append(" sbH=" + ToStringStaticGui.debugPlanetStruct(boViewPane.get2Bits2(IBOViewPane.VP_OFFSET_05_SCROLLBAR_MODE1)));
      sb.append("]");

      sb.nl();
      if (boViewPane.hasFlag(IBOViewPane.VP_OFFSET_02_FLAGX, IBOViewPane.VP_FLAGX_1_STYLE_VIEWPANE)) {
         sb.append("ViewPane Style Turned On = ");
         ToStringStaticGui.toStringViewPaneStyleType(boViewPane.get1(IBOViewPane.VP_OFFSET_14_STYLE_VIEWPORT_MODE1));
      } else {
         sb.append("ViewPane Style Turned Off");
      }
      sb.nl();
      if (boViewPane.hasFlag(IBOViewPane.VP_OFFSET_02_FLAGX, IBOViewPane.VP_FLAGX_2_STYLE_VIEWPORT)) {
         sb.append("ViewPort Style Turned On = ");
         ToStringStaticGui.toStringViewPaneStyleType(boViewPane.get1(IBOViewPane.VP_OFFSET_14_STYLE_VIEWPORT_MODE1));
      } else {
         sb.append("ViewPort Style Turned Off");
      }
      sb.nl();
      if (boViewPane.hasFlag(IBOViewPane.VP_OFFSET_02_FLAGX, IBOViewPane.VP_FLAGX_3_STYLE_CONTENT)) {
         sb.append("ViewContent Style = On");
      } else {
         sb.append("ViewContent Style = Off");
      }

      sb.nl();
      String master = "Header";
      String slave = "Scrollbar";
      if (boViewPane.hasFlag(IBOViewPane.VP_OFFSET_01_FLAG, IBOViewPane.VP_FLAG_3_SCROLLBAR_MASTER)) {
         master = "Scrollbar";
         slave = "Header";
      }
      sb.appendVar("Master", master);
      sb.appendVarWithSpace("Slave", slave);

      sb.nl();
      /////////////
      int typeSB = boViewPane.getValue(IBOViewPane.VP_OFFSET_10_COMPETITION_SB_TYPE1, 1);
      sb.append("CompetitionTypeForScrollbars=");
      sb.append(ToStringStaticGui.toStringCompetitionType(typeSB));
      if (boViewPane.hasFlag(IBOViewPane.VP_OFFSET_01_FLAG, IBOViewPane.VP_FLAG_7_COMPET_OVERLAY_SB)) {
         sb.append(" Overlay");
      }

      sb.nl();
      int typeH = boViewPane.getValue(IBOViewPane.VP_OFFSET_11_COMPETITION_HEADER_TYPE1, 1);
      sb.append("CompetitionTypeForHeaders=");
      sb.append(ToStringStaticGui.toStringCompetitionType(typeH));
      if (boViewPane.hasFlag(IBOViewPane.VP_OFFSET_01_FLAG, IBOViewPane.VP_FLAG_6_COMPET_OVERLAY_HEADER)) {
         sb.append(" Overlay");
      }

      /////////////////////////////////////

      sb.nl();
      sb.append("Scrollbars [Horiz,Vert] ---> ScrollType=");
      sb.append('[');
      sb.append(ToStringStaticGui.toStringScrollbarMode(boViewPane.get2Bits3(IBOViewPane.VP_OFFSET_05_SCROLLBAR_MODE1)));
      sb.append(",");
      sb.append(ToStringStaticGui.toStringScrollbarMode(boViewPane.get2Bits4(IBOViewPane.VP_OFFSET_05_SCROLLBAR_MODE1)));
      sb.append(']');

      sb.append(" Visual=");
      sb.append('[');
      sb.append(ToStringStaticGui.toStringScrollVisual(boViewPane.get4Bits1(IBOViewPane.VP_OFFSET_06_VISUAL_LEFT_OVER1)));
      sb.append("," + ToStringStaticGui.toStringScrollVisual(boViewPane.get4Bits2(IBOViewPane.VP_OFFSET_06_VISUAL_LEFT_OVER1)));
      sb.append(']');
      
      sb.append(" Move=");
      sb.append('[');
      sb.append(ToStringStaticGui.toStringScrollMove(boViewPane.get2Bits1(IBOViewPane.VP_OFFSET_08_MOVE_TYPE1)));
      sb.append(",");
      sb.append(ToStringStaticGui.toStringScrollMove(boViewPane.get2Bits3(IBOViewPane.VP_OFFSET_08_MOVE_TYPE1)));
      sb.append(']');
      
      sb.append(" Partial=");
      sb.append('[');
      sb.append(ToStringStaticGui.toStringScrollPartial(boViewPane.get2Bits2(IBOViewPane.VP_OFFSET_08_MOVE_TYPE1)));
      sb.append(",");
      sb.append(ToStringStaticGui.toStringScrollPartial(boViewPane.get2Bits4(IBOViewPane.VP_OFFSET_08_MOVE_TYPE1)));
      sb.append(']');

   }

   /**
    * Size must be coded.
    * 
    * @param horiz
    * @param wrapper
    * @param wFill
    * @param arrowBlock
    * @param inverseBlock
    * @param clock
    * @param size
    * @return
    */
   public ByteObject getScrollBarTech(boolean horiz, boolean wrapper, boolean wFill, boolean arrowBlock, boolean inverseBlock, boolean clock, int size) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesGui.TYPE_102_SCROLLBAR_TECH, SB_BASIC_SIZE);
      p.setFlag(SB_OFFSET_01_FLAG, SB_FLAG_1_VERT, !horiz);
      p.setFlag(SB_OFFSET_01_FLAG, SB_FLAG_3_WRAPPER, wrapper);
      p.setFlag(SB_OFFSET_01_FLAG, SB_FLAG_4_WRAPPER_FILL, wFill);
      p.setFlag(SB_OFFSET_01_FLAG, SB_FLAG_5_ARROW_ON_BLOC, arrowBlock);
      p.setFlag(SB_OFFSET_01_FLAG, SB_FLAG_6_BLOCK_INVERSE, inverseBlock);
      p.setFlag(SB_OFFSET_01_FLAG, SB_FLAG_7_AROUND_THE_CLOCK, clock);
      p.setValue(SB_OFFSET_03_PRIMARY_SIZE4, size, 4);
      p.setValue(SB_OFFSET_04_SECOND_SIZE4, size, 4);
      return p;
   }

   /**
    * Depending on Horiz/Vert, uses the right link id for tech {@link ByteObject}.
    * @param sc
    * @param tech
    */
   public void linkTech(StyleClass sc, ByteObject tech) {
      int techid = IBOTypesGui.LINK_69_BO_H_SCROLLBAR;
      if (tech.hasFlag(SB_OFFSET_01_FLAG, SB_FLAG_1_VERT)) {
         techid = IBOTypesGui.LINK_68_BO_V_SCROLLBAR;
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
