package pasa.cbentley.framework.gui.src4.string;

import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.factories.interfaces.IBOStrAuxEdit;

/**
 * Can't we take advantage of device giving the character already?
 * 
 * @author Charles-Philip Bentley
 *
 */
public class KeyboardCharProducer implements ICharProducer {

   boolean     isInsertMode;

   private int speed;

   /**
    * Called when input config hasn't generated a command. tries to find the letter associated with it.
    */
   public void produce(ExecutionContextCanvasGui ec, StringEditModule editmodule) {
      speed = editmodule.getEditTech().get1(IBOStrAuxEdit.SEDIT_OFFSET_06_SPEED_NUMPAD1);
      InputConfig ic = ec.getInputConfig();
      if (ic.isPressed()) {
         int key = ic.getIdKeyBut();
         if (key == ' ' || key < 120 && key > 32) {
            //this is shared
            char c = (char) key;

            int caretIndex = editmodule.getCaretIndex();
            if (!isInsertMode) {
               editmodule.caretAtInsertChar(c);
            } else {
               editmodule.caretAtReplaceChar(ec, caretIndex - 1, c);
            }
         }
      }
   }
}
