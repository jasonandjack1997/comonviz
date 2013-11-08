package au.uq.dke.comonviz.handler;

import java.awt.*;
import java.awt.event.*;

/** A listener that you attach to the top-level JFrame of
 *  your application, so that quitting the frame exits the 
 *  application.
 *
 *  Taken from Core Web Programming from 
 *  Prentice Hall and Sun Microsystems Press,
 *  http://www.corewebprogramming.com/.
 *  &copy; 2001 Marty Hall and Larry Brown;
 *  may be freely used or adapted. 
 */
public class ExitListener extends WindowAdapter {
  public void windowClosing(WindowEvent event) {
    System.exit(0);
  }
}