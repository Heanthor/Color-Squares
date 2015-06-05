package imageDislplay;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.*;

import java.util.Arrays;
import java.util.Random;

public class UI {
	JFrame frame, frame2;
	JPanel[][] square = new JPanel[40][40]; //Stores colored square grid
	Random r1, r2, r3;
	int[] lastLoc;
	boolean colorMode = true;
	Color cPick;
	int resolution = 1;

	public void initialize() {
		//Create general frame actions
		frame = new JFrame("Simple Drawing v1");
		frame.getContentPane().setLayout(null);
		frame.setLayout(new BorderLayout());
		frame.setBounds(200, 200, 416, 438);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));

		//Secondary button panel
		frame2 = new JFrame("Settings");
		frame2.getContentPane().setLayout(null);
		frame2.setBounds(200, 700, 416, 150);
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Draw mode button
		JButton draw = new JButton("Drawing Mode");
		draw.setBounds(20, 15, 150, 30);
		frame2.getContentPane().add(draw);

		//Color mode button
		JButton color = new JButton("Color Mode");
		color.setBounds(225, 15, 150, 30);
		frame2.getContentPane().add(color);

		//Color pick field
		final JTextField pick = new JTextField("Enter color id (hex)");
		pick.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
				//Clears text
				pick.setText("");
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {
			}

			public void mouseReleased(MouseEvent arg0) {
			}


		});
		pick.setBounds(30, 60, 130, 30);
		frame2.getContentPane().add(pick);

		//Resolution field
		final JTextField res = new JTextField("Resolution Factor");
		res.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
				//Clears text
				res.setText("");
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {
			}

			public void mouseReleased(MouseEvent arg0) {
			}


		});
		res.setBounds(235, 60, 130, 30);
		frame2.getContentPane().add(res);

		frame2.setVisible(true);
		frame2.revalidate();

		//Populating array
		for (int i = 0; i < 40; i++) {
			for (int j = 0; j < 40; j++) {
				JPanel temp = new JPanel();
				square[i][j] = temp;

				//Creates grid
				temp.setBounds(10*i, 10*j, 10, 10);

				//Append square to frame
				frame.getContentPane().add(temp);
				frame.revalidate();
			}
		}

		//Adds random colors by default
		populateColors();

		//Normalize the frame background color
		JPanel background = new JPanel();
		background.setBackground(UIManager.getColor("Panel.background"));
		frame.getContentPane().add(background);

		//Mouse movement listener
		frame.addMouseMotionListener(new MouseMotionListener() {
			//Mouse clicked and dragged across frame
			public void mouseDragged(MouseEvent e) {
				//Dependent on mode
				if (colorMode) {
					invertColor(e, false);
				} else {
					draw(e, cPick);
				}
			}

			public void mouseMoved(MouseEvent e) {

			}
		});

		frame.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
				//Dependent on mode
				if (colorMode) {
					invertColor(arg0, true);
				} else {
					draw(arg0, cPick);
				}
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {
			}

			public void mouseReleased(MouseEvent arg0) {
			}
		});

		//Draw mode button action
		draw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Drawing mode
				colorMode = false;
				//Enters drawing color
				try {
					cPick = Color.decode("0x" + pick.getText());
				} catch (NumberFormatException e) {
					pick.setText("Not a valid hex color!");
				}

				for (int i = 0; i < 40; i++) {
					for (int j = 0; j < 40; j++) {
						square[i][j].setBackground(Color.WHITE);
					}
				}
			}

		});

		//Color mode button action
		color.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Color mode
				colorMode = true;
				populateColors();
			}
		});

		frame.revalidate();
		frame.setVisible(true);
	}

	private void populateColors() {
		for (int i = 0; i < 40; i++) {
			for (int j = 0; j < 40; j++) {
				square[i][j].setBackground(new Color(new Random().nextInt(256),
						new Random().nextInt(256), new Random().nextInt(256))); 
			}
		}
	}

	//Uses truncation to get the "coordinates" of clicked frame in double array
	private int[] gridLocation(Point p) {
		int[] container = new int[2];
		container[0] = (p.x / 10) - 1; //Correct for x discrepancy
		container[1] = (p.y / 10) - 3; //Correct for y discrepancy

		System.out.println(p + " into [" + container[0] + ", " +
				container[1] + "].");

		return container;
	}

	//Inverts background color of panel hovered over by mouse at click
	private void invertColor(MouseEvent arg0, boolean click) {
		//Gets coordinates of click relative to frame
		int[] loc = gridLocation(arg0.getPoint());

		if (lastLoc == null) { //Runs on first call only
			lastLoc = loc;
		}

		//Don't run twice on same square, saves processor
		if (Arrays.equals(lastLoc, loc) && click == false) { 
			return;				  		   
		}

		//Don't draw if out of bounds
		if (loc[0] < 0 || loc[0] > 39) {
			return;
		}

		if (loc[1] < 0 || loc[1] > 39) {
			return;
		}

		//Otherwise, invert color
		JPanel temp = square[loc[0]][loc[1]];
		Color current = temp.getBackground();
		//Finds inverse color
		Color inverse = new Color(255 - current.getRed(), 
				255 - current.getGreen(), 255 - current.getBlue());
		temp.setBackground(inverse);
		lastLoc = loc;
	}

	//Draw on a solid color grid
	private void draw(MouseEvent arg0, Color color) {
		int[] loc = gridLocation(arg0.getPoint());

		//Don't draw if out of bounds
		if (loc[0] < 0 || loc[0] > 39) {
			return;
		}

		if (loc[1] < 0 || loc[1] > 39) {
			return;
		}
		
		Color newColor = color;
	/*	//System.out.println(arg0.getButton());
		if (arg0.getButton() == MouseEvent.BUTTON3) { //Right mouse button
			newColor = Color.white;
			System.out.println("************************");
		}
		*/
		//Draw
		square[loc[0]][loc[1]].setBackground(newColor);
	}
}
