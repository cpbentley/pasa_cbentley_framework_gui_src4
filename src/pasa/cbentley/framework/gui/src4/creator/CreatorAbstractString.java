package pasa.cbentley.framework.gui.src4.creator;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.framework.coredraw.src4.interfaces.ITechFont;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.factories.DrawableStringFactory;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStrAuxData;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStrAuxEdit;
import pasa.cbentley.framework.gui.src4.tech.ITechStringDrawable;

public abstract class CreatorAbstractString extends CreatorAbstract {

   protected DrawableStringFactory fac;

   public CreatorAbstractString(CreateContext cc) {
      super(cc);
      fac = gc.getStringDrawableFactory();
   }

   /**
    * 
    * @return
    */
   public ByteObject getBOStringDataTitle() {
      int stringType = ITechStringDrawable.PRESET_CONFIG_1_TITLE;
      int stringMode = ITechStringDrawable.S_ACTION_MODE_0_READ;
      ByteObject bo = fac.getBOStringData(stringType, stringMode);
      return bo;
   }

   public ByteObject getBOStringData() {
      int stringType = ITechStringDrawable.PRESET_CONFIG_0_NONE;
      int stringMode = ITechStringDrawable.S_ACTION_MODE_0_READ;
      ByteObject bo = fac.getBOStringData(stringType, stringMode);
      return bo;
   }

   public ByteObject getBOStringFigureScrollReader() {
      int face = ITechFont.FACE_02_PROPORTIONAL;
      int style = ITechFont.STYLE_0_PLAIN;
      int size = ITechFont.SIZE_3_MEDIUM;
      int color = getRepo().getContent1();

      ByteObject format = dc.getStringAuxFactory().getStrAuxFormat_NiceWordNormalTrim();
      ByteObject specials = dc.getStringAuxFactory().getStrAuxSpecials_NewLineWorkSingleSpaceTab();
      ByteObject effects = null;
      ByteObject mask = null;
      ByteObject scale = null;
      ByteObject anchor = null;
      ByteObject bo = facFigure.getFigString(null, face, style, size, color, effects, mask, scale, anchor, format, specials);

      return bo;
   }

   public ByteObject getBOStringFigurePreset() {
      int face = ITechFont.FACE_02_PROPORTIONAL;
      int style = ITechFont.STYLE_0_PLAIN;
      int size = ITechFont.SIZE_3_MEDIUM;
      int color = getRepo().getContent1();

      ByteObject format = null;
      ByteObject specials = null;
      ByteObject effects = null;
      ByteObject mask = null;
      ByteObject scale = null;
      ByteObject anchor = null;
      ByteObject bo = facFigure.getFigString(null, face, style, size, color, effects, mask, scale, anchor, format, specials);

      return bo;
   }

   public ByteObject getBOStringFigure() {

      int face = ITechFont.FACE_01_MONOSPACE;
      int style = ITechFont.STYLE_0_PLAIN;
      int size = ITechFont.SIZE_3_MEDIUM;
      int color = getRepo().getContent1();

      ByteObject format = dc.getStringAuxFactory().getStrAuxFormat_NiceWordNormalTrim();
      ByteObject specials = dc.getStringAuxFactory().getStrAuxSpecials_NewLineWorkSingleSpaceTab();
      ByteObject effects = null;
      ByteObject mask = null;
      ByteObject scale = null;
      ByteObject anchor = null;
      ByteObject bo = facFigure.getFigString(null, face, style, size, color, effects, mask, scale, anchor, format, specials);

      return bo;
   }

   public ByteObject getBOStringEdit() {
      ByteObject tech = fac.getBOStringEditDefault();
      int symbolTable = 1;
      tech.set1(IBOStrAuxEdit.SEDIT_OFFSET_09_SYMBOL_TABLE1, symbolTable);
      return tech;
   }

   protected ByteObject getBOStringDataNoPreset() {
      int stringType = ITechStringDrawable.PRESET_CONFIG_0_NONE;
      int stringMode = ITechStringDrawable.S_ACTION_MODE_1_SELECT;
      ByteObject bo = fac.getBOStringData(stringType, stringMode);
      return bo;
   }

   protected ByteObject getBOStringDataReader() {
      int stringType = ITechStringDrawable.PRESET_CONFIG_3_SCROLL_V;
      int stringMode = ITechStringDrawable.S_ACTION_MODE_1_SELECT;
      ByteObject bo = fac.getBOStringData(stringType, stringMode);
      return bo;
   }

   protected ByteObject getBOStringDataReaderNaturalNoWrap() {
      int stringType = ITechStringDrawable.PRESET_CONFIG_4_NATURAL_NO_WRAP;
      int stringMode = ITechStringDrawable.S_ACTION_MODE_1_SELECT;
      ByteObject bo = fac.getBOStringData(stringType, stringMode);
      return bo;
   }

   protected ByteObject getBOStringDataReaderH() {
      int stringType = ITechStringDrawable.PRESET_CONFIG_2_SCROLL_H;
      int stringMode = ITechStringDrawable.S_ACTION_MODE_1_SELECT;
      ByteObject bo = fac.getBOStringData(stringType, stringMode);
      return bo;
   }
}
