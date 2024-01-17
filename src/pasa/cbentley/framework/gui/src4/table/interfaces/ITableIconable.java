package pasa.cbentley.framework.gui.src4.table.interfaces;

public interface ITableIconable {

   
   /**
    * Returns the Object to represent the {@link ITableIconable}.
    * <br>
    * Usually
    * <li> A String for Menus
    * <li> A FigDrawable for more complex ojects
    * <li>
    * @return
    */
   public Object getTableIcon();
}
