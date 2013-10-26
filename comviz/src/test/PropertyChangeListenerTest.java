package test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class PropertyChangeListenerTest {

	public static void main(String args[]) {

		JFrame frame = new JFrame("Button Sample");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JButton button1 = new JButton("Press me 1");

		final JButton button2 = new JButton("Press me 2");
		


		ActionListener actionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actionEvent) {

				JButton jButton = (JButton) actionEvent.getSource();

				int r = (int) (Math.random() * 100);

				int g = (int) (Math.random() * 100);

				int b = (int) (Math.random() * 100);

				jButton.setBackground(new Color(r, g, b));
				//jButton.setForeground(new Color(r, g, b));

			}

		};

		PropertyChangeListener propChangeListn = new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent event) {

				String property = event.getPropertyName();

				if ("background".equals(property)) {

					((Component) event.getSource()).setBackground(Color.yellow);
					

				}

			}

		};

		button1.addActionListener(actionListener);

		button2.addActionListener(actionListener);
		button2.addPropertyChangeListener(propChangeListn);
		//button2.addPropertyChangeListener(propChangeListn);

		//button2.addActionListener(actionListener);
		

		Container cPane = frame.getContentPane();

		cPane.add(button1, BorderLayout.NORTH);

		cPane.add(button2, BorderLayout.SOUTH);

		frame.setSize(500, 300);

		frame.setVisible(true);
	}
}
