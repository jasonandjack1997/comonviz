package test;


import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.tree.*;

import util.WindowUtilities;
import au.uq.dke.comonviz.handler.ExitListener;

public class TreeExplorer extends JFrame {
  public static void main(String[] args) {
    new TreeExplorer();
  }
 
  public TreeExplorer() {
    super("Creating a Simple JTree");
    WindowUtilities.setNativeLookAndFeel();
    addWindowListener(new ExitListener());
    Container content = getContentPane();
    Object[] hierarchy =
      { "javax.swing",
        "javax.swing.border",
        "javax.swing.colorchooser",
        "javax.swing.event",
        "javax.swing.filechooser",
        new Object[] { "javax.swing.plaf",
                       "javax.swing.plaf.basic",
                       "javax.swing.plaf.metal",
                       "javax.swing.plaf.multi" },
        "javax.swing.table",
        new Object[] { "javax.swing.text",
                       new Object[] { "javax.swing.text.html",
                                      "javax.swing.text.html.parser" },
                       "javax.swing.text.rtf" },
        "javax.swing.tree",
        "javax.swing.undo" };
    DefaultMutableTreeNode root = processHierarchy(hierarchy);
    JTree tree = new JTree(root);
    content.add(new JScrollPane(tree), BorderLayout.CENTER);
    setSize(275, 300);
    setVisible(true);
    
    tree.setCellRenderer(new DefaultTreeCellRenderer() {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
        public Component getTreeCellRendererComponent(JTree tree,
                Object value, boolean sel, boolean expanded, boolean leaf,
                int row, boolean hasFocus) {
			
			this.setLeafIcon(null);
			this.setOpenIcon(null);
			this.setClosedIcon(null);

/*            String search = "javax";
            String text = value.toString();

            StringBuffer html = new StringBuffer("<html>");
            Matcher m = Pattern.compile(Pattern.quote(search)).matcher(text);
            while (m.find())
                m.appendReplacement(html, "<b>" + m.group() + "</b>");
            m.appendTail(html).append("</html>");
*/

            
			StringBuffer html = new StringBuffer("<html><b style= \"color: #000000; background-color: #fff3ff\">T</b>  ");
			html.append(value.toString());
			html.append("</html>");
            return super.getTreeCellRendererComponent(
                    tree, html.toString(), sel, expanded, leaf, row, hasFocus);
        }
    });
  }

  /** Small routine that will make node out of the first entry
   *  in the array, then make nodes out of subsequent entries
   *  and make them child nodes of the first one. The process is
   *  repeated recursively for entries that are arrays.
   */
    
  private DefaultMutableTreeNode processHierarchy(Object[] hierarchy) {
    DefaultMutableTreeNode node =
      new DefaultMutableTreeNode(hierarchy[0]);
    DefaultMutableTreeNode child;
    for(int i=1; i<hierarchy.length; i++) {
      Object nodeSpecifier = hierarchy[i];
      if (nodeSpecifier instanceof Object[])  // Ie node with children
        child = processHierarchy((Object[])nodeSpecifier);
      else
        child = new DefaultMutableTreeNode(nodeSpecifier); // Ie Leaf
      node.add(child);
    }
    return(node);
  }
}