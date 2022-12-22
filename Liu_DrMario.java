/* Evan Liu
 * Period 6
 * Dr.Mario
 * This program creates board of x panels with germs and allows users to clear germs of the
 * same type near each other using mouse click input
 * and has any remaining germs to "fall." Implements recursion to remove germs.
 */
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;


public class Liu_DrMario extends JFrame{

	private final String[] FILE_NAMES = {"red.jpg","blue.jpg","yellow.jpg","purple.jpg","green.jpg","pink.jpg"};
	private final int[] VERT_DISP = {-1,-1,-1,0,0,1,1,1};  //determines neighbors
	private final int[] HORZ_DISP = {-1,0,1,-1,1,-1,0,1};

	private PicPanel[][] germs;

	private int turnsLeft = 10;

	private JLabel message;

	private final int colorPanelLength = 485;
	private final int colorPanelHeight = 500;
	private int dim;							//dimensions for the grid (user input)

	public Liu_DrMario(){

		dim = Integer.parseInt(JOptionPane.showInputDialog(null,"What is the dimension of the matrix?"));

		germs = new PicPanel[dim][dim];

		setLayout(null);
		setSize(500,700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//stores all panels in a grid layout
		JPanel colorPanel = new JPanel();
		colorPanel.setLayout(new GridLayout(dim,dim,5,5));
		colorPanel.setBackground(Color.black);
		colorPanel.setBounds(1,0,colorPanelLength ,colorPanelHeight);
		colorPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		//the message to be displayed
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(null);
		messagePanel.setBounds(0,500,500,200);
		messagePanel.setBackground(Color.white);

		message = new JLabel("Turns Left: "+turnsLeft);
		message.setFont(new Font("Comic Sans",Font.ITALIC,20));
		message.setBounds(175,-125,175,300);
		messagePanel.add(message);

		//randomly assign colors to and make the panels
		Random r = new Random();
		for(int row = 0; row < dim; row++){
			for(int col = 0; col < dim; col++){
				germs[row][col]= new PicPanel(row,col,FILE_NAMES[r.nextInt(FILE_NAMES.length)]);				
				colorPanel.add(germs[row][col]);
			}
		}

		add(colorPanel);
		add(messagePanel);
		setVisible(true);
	}

	//removes germ at spot r, c as long as it contains the file name represented by toRemove
	//recursively repeats for all neighbors and neighbor's neighbors.

	private void removeGerms(int r, int c, String toRemove){

		//removes the germ
		germs[r][c].clearPanel();

		for(int i = 0; i < HORZ_DISP.length; i++) {

			//checks if square is in bounds and not empty
			if(r + VERT_DISP[i] >= 0 && c + HORZ_DISP[i] >= 0 && r + VERT_DISP[i] < dim && c + HORZ_DISP[i] < dim && !(germs[VERT_DISP[i] + r][HORZ_DISP[i] + c].fname == null)) {

				//if name of neighboring square is same as one selected
				if(germs[VERT_DISP[i] + r][HORZ_DISP[i] + c].fname.equals(toRemove)) {

					removeGerms(VERT_DISP[i] + r, HORZ_DISP[i] + c, toRemove);
				}
			}
		}
	}

	//examines the entire grid and makes germs fall as necessary	
	private void fall(){	

		for(int c = 0; c < germs.length; c++) {

			//starts from bottom to top
			for(int r = dim - 1; r >= 0; r--) {

				if(!germs[r][c].isEmpty()) {

					int pos = r;

					//while there are empty spots below
					while(pos+1 < dim && germs[pos+1][c].isEmpty()) {

						if(!(germs[pos][c].fname == null)) {
							
							//switches the one above with the one below 
							germs[pos+1][c].setImage(germs[pos][c].fname);
							germs[pos][c].clearPanel();

						}                    	          
						pos++;
					}
				}
			}
		}
	}

	//type of panel that will accept mouse input
	public class PicPanel extends JPanel implements MouseListener{
		private int row;
		private int col;

		private String fname;
		private Image image;

		public PicPanel(int r, int c, String name){

			row = r;
			col = c;

			this.addMouseListener(this);
			setImage(name);
		}

		//places a germ based off the file name at the given panel
		public void setImage(String name) {
			fname = name;

			int newLength = (colorPanelLength-(dim+1)*5)/dim;
			int newHeight = (colorPanelHeight-(dim+1)*5)/dim;


			try {

				image = ImageIO.read(new File(fname));
				image = image.getScaledInstance(newLength, newHeight, Image.SCALE_DEFAULT);

			} catch (IOException ioe) {
				System.out.println("Can't open "+ fname);
				System.exit(-1);
			}

			repaint();
		}

		// "blanks" out a panel
		public void clearPanel() {
			fname = null;
			image = null;
			repaint();
		}

		public boolean isEmpty() {
			return fname == null;
		}

		public void paintComponent(Graphics g){
			super.paintComponent(g);
			if(image != null)
				g.drawImage(image,0,0,this);
		}

		//the only method you will fill in this class
		public void mouseClicked(MouseEvent arg0) {

			//shows error when number of turns reaches zero
			if(turnsLeft <= 0) {

				JOptionPane.showMessageDialog(null, "You have run out of turns", "Error", JOptionPane.ERROR_MESSAGE);
			}

			else if(arg0.getButton() == MouseEvent.BUTTON1 && !this.isEmpty()) {

				removeGerms(this.row, this.col, this.fname);
				turnsLeft--;

				message.setText("Turns Left: "+turnsLeft);
				fall();
				repaint();
			}
		}

		//DO NOT IMPLEMENT THESE METHODS
		public void mouseEntered(MouseEvent arg0) {


		}


		public void mouseExited(MouseEvent arg0) {


		}


		public void mousePressed(MouseEvent arg0) {


		}


		public void mouseReleased(MouseEvent arg0) {


		}
	}

	public static void main(String[] args){
		new Liu_DrMario ();

	}
}
