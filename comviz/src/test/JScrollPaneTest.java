import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JDialog;


public class JScrollPaneTest {
	
	public static void main(String args[]){
		JDialog jFrame = new JDialog();
		jFrame.setLayout(new BorderLayout());
		JButton jbt = new JButton("hehe");
		JButton jbt2 = new JButton("hehe2");
		jbt.setPreferredSize(new Dimension(400, 400));
		jbt.setMinimumSize(new Dimension(100, 100));
		jFrame.add(jbt, BorderLayout.NORTH);
		jFrame.add(jbt2,BorderLayout.CENTER);
		jFrame.pack();
		jFrame.setVisible(true);
	}

}
