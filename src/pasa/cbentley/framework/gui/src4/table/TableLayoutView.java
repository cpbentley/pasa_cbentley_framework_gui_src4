package pasa.cbentley.framework.gui.src4.table;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.interfaces.ICommandable;
import pasa.cbentley.framework.datamodel.src4.table.ObjectTableModel;
import pasa.cbentley.framework.drawx.src4.engine.RgbImage;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceDrawable;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStringDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsView;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IUIView;
import pasa.cbentley.framework.gui.src4.menu.NestedMenus;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;
import pasa.cbentley.framework.gui.src4.string.StringEditControl;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;

/**
 * A Table layout for editing text.
 * <br>
 * Default two columns forms.
 * <br>
 * One column is the title
 * Second column is a StringEdit for a Number or a String
 * <br>
 * 
 * About Live Edition Mode.
 * <br>
 * 
 * With this class, about 50% of the screen is taken by the virtual keyboard.
 * 
 * The {@link StringEditControl} behaves differently than inside edition
 * 
 * Prev/Next navigates from editable {@link StringDrawable}.
 * 
 * @author Charles-Philip
 *
 */
public class TableLayoutView extends TableView implements IBOStringDrawable, ICommandable, ICmdsView {

   public static final int SC_TITLE_LINK_43 = 43;

   private CmdInstance     ci;

   /**
    * The command context in which this {@link TableLayoutView} is invoked.
    * <br>
    * Every events will execute this commands as root command.
    * <br>
    * OK / CANCEL
    */
   private CmdInstanceDrawable     executionCtx;

   private ICommandable    icon;

   private NestedMenus     menuHeader;

   ObjectTableModel        model            = null;

   private StringDrawable  titleDrawable;

   public TableLayoutView(GuiCtx gc, StyleClass styleKey) {
      this(gc, styleKey, 2);
   }

   public TableLayoutView(GuiCtx gc, StyleClass styleKey, ByteObject tablePolicy) {
      super(gc, styleKey, tablePolicy);
      model = new ObjectTableModel(gc.getDMC(), getNumCols());
      setDataModel(model);
   }

   /**
    * A table layout view with 2 columns model
    * @param styleKey
    */
   public TableLayoutView(GuiCtx gc, StyleClass styleKey, int numCols) {
      super(gc, styleKey, gc.getTablePolicyC().getTwoColumns());
      model = new ObjectTableModel(gc.getDMC(), numCols);
      setDataModel(model);

   }

   /**
    * 
    * @param styleKey
    */
   public TableLayoutView(GuiCtx gc, StyleClass sc, int numCols, int selType) {
      this(gc, sc, numCols);
   }

   /**
    * TODO string validation?
    * Form parsing is done by the caller
    */
   public void commandAction(CmdInstance cmd) {
      if (cmd.cmdID == CMD_04_OK) {
         ci.paramO = this;
         icon.commandAction(ci);
      } else {
         hideMe((InputConfig) ci.getFeedback());
      }
   }

   public void formAppend(IDrawable d) {
      //getTableModel().T
      model.appendObject(d);
   }

   public void formAppend(String str, RgbImage img) {

   }

   public void formAppendAll(IDrawable[] ds) {
      for (int i = 0; i < ds.length; i++) {
         formAppend(ds[i]);
      }
   }

   /**
    * A row of scrollable commands.
    * <br>
    * Uses the Menu Item style
    * @param cmdid
    */
   public void formAppendCmd(int cmdid) {
      if (menuHeader == null) {
         //override tech with special tech
         StyleClass sc = gc.getClass(IUIView.SC_1_MENU);
         menuHeader = new NestedMenus(gc,sc);
         setHeader(menuHeader, C.POS_1_BOT, ITechViewPane.PLANET_MODE_0_EAT);
      }
      menuHeader.addCommand(cmdid);

   }

   /**
    * Appends a string and its text field input
    * <br>
    * <br>
    * 
    * @param str
    * @param tech
    */
   public void formAppendInput(String str, ByteObject tech) {

   }

   /**
    * Append a String to the {@link TableLayoutView} model
    * @param str
    * @param editTable
    */
   public void formAppendString(String str, boolean editTable) {
      StyleClass sc = this.styleClass;
      if (editTable) {

      }
      StringDrawable sd = new StringDrawable(gc, sc, str);

      model.appendObject(sd, 0);

   }

   public void formDelete(IDrawable d) {

   }

   public void formDelete(int index) {

   }

   public void formDeleteAll() {

   }

   public int formSize() {
      return this.getSize();
   }

   /**
    * Returns the editable String as an integer.
    * <br>
    * @param index
    * @return
    * @throws NumberFormatException
    */
   public int getValueInt(int index) throws NumberFormatException {
      String str = getValueString(index);
      Integer g = Integer.valueOf(str);
      return g.intValue();
   }

   public String getValueString(int index) {
      StringDrawable sd = (StringDrawable) model.getObject(index);
      String str = sd.getString();
      return str;
   }

   /**
    * Insert at position index in the model
    * @param index
    * @param d
    */
   public void insert(int index, IDrawable d) {

   }

   public void insert(int index, String d, RgbImage img) {

   }

   public void setExecutionCtx(CmdInstanceDrawable cd) {
      executionCtx = cd;
   }

   /**
    * Sets the Title on a StringDrawable on Top.
    * <br>
    * The Title will eat and will not influence the width of the Drawable
    * @param title
    */
   public void setFormTitle(String title) {
      titleDrawable = new StringDrawable(gc,styleClass.getSCNotNull(SC_TITLE_LINK_43), title);
      setHeader(titleDrawable, C.POS_0_TOP, ITechViewPane.PLANET_MODE_0_EAT);
   }

   public void setString(int index, int str) {
      setString(index, String.valueOf(str));
   }

   public void setString(int index, String str) {
      StringDrawable sd = (StringDrawable) model.getObject(index);
      sd.setStringNoUpdate(str);
   }

   public void show(ICommandable icon, CmdInstance ci) {
      this.icon = icon;
      this.ci = ci;
      this.getVC().getDrawCtrlAppli().shShowDrawable((InputConfig) ci.getFeedback(), this, ITechCanvasDrawable.SHOW_TYPE_1_OVER);
   }

   public void toString(Dctx sb) {
      sb.root(this, "TableLayoutView");
      super.toString(sb.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "TableLayoutView");
      dc.appendWithSpace(" " + getDebugName());
   }

}
