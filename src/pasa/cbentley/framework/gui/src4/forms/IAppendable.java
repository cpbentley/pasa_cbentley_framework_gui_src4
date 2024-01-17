package pasa.cbentley.framework.gui.src4.forms;

/**
 * 
 * @author cbentley
 *
 */
public interface IAppendable {

    /**
     * Consumer of Title
     * @param title
     */
    public void setTitle(String title);
    
    /**
     * Delete all items in the current list
     */
    public void clean();
    /**
     * 
     * @param displayString the string to display
     * @param img imgage
     * @param visualIndex the visual id of that item. starts at 0
     * @param recordId the record id for future reference back to the record store. id >= 1
     */
    public void addItem(String str, String img, int visualIndex , int recordId);
    
    public void setMessage(String string);
}
