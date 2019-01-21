package gameUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

// panel with background image(overrides the paint component method)
class backImage extends JPanel {

	/*
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image i;

	// Creating Constructer
	public backImage(String path) {
		try {
			BufferedImage bf = ImageIO.read(new File(path));
			this.i = bf;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

//Overriding the paintComponent method
	@Override
	public void paintComponent(Graphics g) {

		g.drawImage(i, 0, 0, null); // Drawing image using drawImage method

	}
}

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private String spath;

	private static final long serialVersionUID = 0L;
	private static backImage contentPane;
	private static backImage startPane;
	private static JPanel helpPane;
	private static JPanel aboutPane;
	private static backImage savesPane;
	private static backImage previewPane;

	private int board[] = new int[64];
	private int humanMove[] = { 0, 0, 0 };
	private int steps[] = new int[200];
	private int numsteps = 0;
	private int maxsteps = 0;

	private String curFile;
	private String createDate;

	/*
	 * turn to main panel
	 */
	public void turnback(JPanel jpanel) {
		state = false;
		jpanel.setEnabled(false);
		jpanel.setVisible(false);
		contentPane.setEnabled(true);
		contentPane.setVisible(true);
		setContentPane(contentPane);
	}

	private static Boolean state = false;
	private static int humanColor = 1;
	private static int numMove = 0;
	private static int moves[] = new int[27];
	private static JLabel grid[] = new JLabel[64];

	// load the image(black, white queen, arrow, etc.)
	private static ImageIcon empty = new ImageIcon(MainFrame.class.getResource("images/empty.png"));
	private static ImageIcon arrow = new ImageIcon(MainFrame.class.getResource("images/arrow.png"));
	private static ImageIcon black = new ImageIcon(MainFrame.class.getResource("images/black.png"));
	private static ImageIcon white = new ImageIcon(MainFrame.class.getResource("images/white.png"));

	private static ImageIcon left = new ImageIcon(MainFrame.class.getResource("images/left.png"));
	private static ImageIcon right = new ImageIcon(MainFrame.class.getResource("images/right.png"));

	private static ImageIcon empty_h = new ImageIcon(MainFrame.class.getResource("images/empty-h.png"));
	private static ImageIcon empty_r = new ImageIcon(MainFrame.class.getResource("images/empty-r.png"));
	private static ImageIcon black_r = new ImageIcon(MainFrame.class.getResource("images/black-r.png"));
	private static ImageIcon white_r = new ImageIcon(MainFrame.class.getResource("images/white-r.png"));
	
	private static ImageIcon arrow_s = new ImageIcon(MainFrame.class.getResource("images/arrow_s.png"));
	private static ImageIcon empty_s = new ImageIcon(MainFrame.class.getResource("images/empty_s.png"));
	private static ImageIcon black_s = new ImageIcon(MainFrame.class.getResource("images/black_s.png"));
	private static ImageIcon white_s = new ImageIcon(MainFrame.class.getResource("images/white_s.png"));

	private static int len1 = 0;
	private static int len2 = 0;

	public void intpp() {
		numMove++;
	}

	// turn the board array to string as the parameter of executable file ai written
	// in c++.
	public String getBoard() {
		int i = 0;
		String sboard = "";
		for (i = 0; i < 64; i++) {
			sboard += String.valueOf(board[i]) + " ";
		}
		return sboard;
	}

	// list the array of all possible moves of a queen or arrow.
	public int possiblemove(int position) {
		int k = 0;
		int j = 1;
		for (j = 1; j < 8; j++) {
			int x = position - 9 * j;
			if (x >= 0 && x % 8 <= position % 8 && board[x] == 0) {
				moves[k] = x;
				k++;
			} else {
				break;
			}
		}
		for (j = 1; j < 8; j++) {
			int x = position - 7 * j;
			if (x >= 0 && x % 8 >= position % 8 && board[x] == 0) {
				moves[k] = x;
				k++;
			} else {
				break;
			}
		}
		for (j = 1; j < 8; j++) {
			int x = position + 9 * j;
			if (x < 64 && x % 8 >= position % 8 && board[x] == 0) {
				moves[k] = x;
				k++;
			} else {
				break;
			}
		}
		for (j = 1; j < 8; j++) {
			int x = position + 7 * j;
			if (x < 64 && x % 8 <= position % 8 && board[x] == 0) {
				moves[k] = x;
				k++;
			} else {
				break;
			}
		}
		for (j = 1; j < 8; j++) {
			int x = position - j;
			if (x >= (position / 8) * 8 && board[x] == 0) {
				moves[k] = x;
				k++;
			} else {
				break;
			}
		}
		for (j = 1; j < 8; j++) {
			int x = position + j;
			if (x < (position / 8 + 1) * 8 && board[x] == 0) {
				moves[k] = x;
				k++;
			} else {
				break;
			}
		}
		for (j = 1; j < 8; j++) {
			int x = position - 8 * j;
			if (x >= 0 && board[x] == 0) {
				moves[k] = x;
				k++;
			} else {
				break;
			}
		}
		for (j = 1; j < 8; j++) {
			int x = position + 8 * j;
			if (x < 64 && board[x] == 0) {
				moves[k] = x;
				k++;
			} else {
				break;
			}
		}
		return k;
	}

	// a function to examine whether the human player's move is valid by calling
	// possiblemove(), if true, update the board array.
//	public Boolean validMove() {
//		int i = 0;
//		int a = humanMove[0];
//		int b = humanMove[1];
//		int c = humanMove[2];
//		if (board[a] != humanColor) {
//			return false;
//		}
//		int queenmoves = possiblemove(a);
//		Boolean condition = false;
//		for (i = 0; i < queenmoves; i++) {
//			if (moves[i] == b) {
//				condition = true;
//			}
//		}
//		if (!condition) {
//			return false;
//		} else {
//
//		}
//		board[a] = 0;
//		int setblock = possiblemove(b);
//		condition = false;
//		for (i = 0; i < setblock; i++) {
//			if (moves[i] == c) {
//				condition = true;
//			}
//		}
//		if (!condition) {
//			board[a] = humanColor;
//			return false;
//		} else {
//			board[b] = humanColor;
//			board[c] = -1;
//			grid[a].setIcon(empty);
//			if (humanColor == 1) {
//				grid[b].setIcon(black);
//			} else {
//				grid[b].setIcon(white);
//			}
//			grid[c].setIcon(arrow);
//
//		}
//		steps[numsteps * 3] = a;
//		steps[numsteps * 3 + 1] = b;
//		steps[numsteps * 3 + 2] = c;
//		numsteps++;
//		maxsteps = numsteps;
//		return true;
//	}

	// assert whether human player can move (or assert whether human loses)
	public Boolean nomoreMoves() {
		int i = 0;
		int queen[] = new int[4];
		int k = 0;
		for (i = 0; i < 64; i++) {
			if (board[i] == humanColor) {
				queen[k] = i;
				k++;
			}
		}
		Boolean condition = true;
		for (i = 0; i < 4; i++) {
			if (possiblemove(queen[i]) != 0) {
				condition = false;
			}
		}
		return condition;
	}

	// the ai step, the java vm opens a shell and executes shell command using
	// Runtime.exec(). the c++ program ai is executed and its stdout is read by java
	// vm.
	public Boolean aiMoves() {
		String sboard = getBoard();
		System.out.println(sboard);
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(spath + "/Java/ai " + Integer.toString(3 - humanColor) + " " + sboard);
			InputStream stdin = proc.getInputStream();
			Scanner scan = new Scanner(stdin);
			Integer a = null;
			Integer b = null;
			Integer c = null;
			if (scan.hasNextInt()) {
				a = scan.nextInt();
			}
			if (a == -2) {
				scan.close();
				return false;
			}
			if (scan.hasNextInt()) {
				b = scan.nextInt();
			}
			if (scan.hasNextInt()) {
				c = scan.nextInt();
			}

			board[a] = 0;
			board[b] = 3 - humanColor;
			board[c] = -1;
			grid[a].setIcon(empty);
			if (humanColor == 2) {
				grid[b].setIcon(black);
			} else {
				grid[b].setIcon(white);
			}
			grid[c].setIcon(arrow);

			steps[numsteps * 3] = a;
			steps[numsteps * 3 + 1] = b;
			steps[numsteps * 3 + 2] = c;
			numsteps++;

			System.out.println("a = " + a);
			System.out.println("b = " + b);
			System.out.println("c = " + c);

			scan.close();
			int exitVal = proc.waitFor();
			if (exitVal != 0) {
				System.out.println("fatal error.Process exitValue: " + exitVal);
				System.exit(1);
			}

		} catch (Exception t) {
			t.printStackTrace();
			System.exit(1);
		}
		maxsteps = numsteps;
		return true;
	}

	public void backward() { // make one step backward, without parameter check.
		if (numsteps % 2 == 1) { // black
			numsteps--;
			board[steps[numsteps * 3 + 2]] = 0;
			board[steps[numsteps * 3 + 1]] = 0;
			board[steps[numsteps * 3]] = 1;
			grid[steps[numsteps * 3 + 2]].setIcon(empty);
			grid[steps[numsteps * 3 + 1]].setIcon(empty);
			grid[steps[numsteps * 3]].setIcon(black);
		} else {
			numsteps--;
			board[steps[numsteps * 3 + 2]] = 0;
			board[steps[numsteps * 3 + 1]] = 0;
			board[steps[numsteps * 3]] = 2;
			grid[steps[numsteps * 3 + 2]].setIcon(empty);
			grid[steps[numsteps * 3 + 1]].setIcon(empty);
			grid[steps[numsteps * 3]].setIcon(white);
		}
	}

	public void forward() { // make one step forward, without parameter check.
		if (numsteps % 2 == 0) { // black
			board[steps[numsteps * 3]] = 0;
			board[steps[numsteps * 3 + 1]] = 1;
			board[steps[numsteps * 3 + 2]] = -1;
			grid[steps[numsteps * 3]].setIcon(empty);
			grid[steps[numsteps * 3 + 1]].setIcon(black);
			grid[steps[numsteps * 3 + 2]].setIcon(arrow);
			numsteps++;
		} else {
			board[steps[numsteps * 3]] = 0;
			board[steps[numsteps * 3 + 1]] = 2;
			board[steps[numsteps * 3 + 2]] = -1;
			grid[steps[numsteps * 3]].setIcon(empty);
			grid[steps[numsteps * 3 + 1]].setIcon(white);
			grid[steps[numsteps * 3 + 2]].setIcon(arrow);
			numsteps++;
		}
	}

	public void clearHints() {// 把落子提示（高亮）清除（undo, redo前）
		int j;
		if (numMove == 1) {
			for (j = 0; j < len1; j++) {
				grid[moves[j]].setIcon(empty);
			}
			numMove = 0;
		} else if (numMove == 2) {

			for (j = 0; j < len2; j++) {
				if (board[moves[j]] == 1) {
					grid[moves[j]].setIcon(black);
				} else if (board[moves[j]] == 2) {
					grid[moves[j]].setIcon(white);
				} else if (board[moves[j]] == 0) {
					grid[moves[j]].setIcon(empty);
				} else {

				}
			}
			numMove = 0;
		}
	}

	public void undo() { // undo.
		if (state == false) {
			return;
		}
		state = false;
		clearHints();
		if ((numsteps >= 2 && humanColor == 1) || (numsteps >= 3 && humanColor == 2)) {
			backward(); // call backward() twice
			backward();
		}
		state = true;
	}

	public void redo() { // redo.
		if (state == false) {
			return;
		}
		state = false;
		clearHints();
		if (numsteps <= maxsteps - 2) {
			forward(); // call forward() twice
			forward();
		}
		state = true;
	}

	public void startPaneinit(int color, int type) {

		startPane.removeAll();

		startPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		startPane.setLayout(null);

		humanColor = color;
		state = true;

		int xgrid = 100;
		int ygrid = 30;

		/*
		 * console label
		 */
		JLabel lblinfo = new JLabel("[ready] human player\'s turn");
		lblinfo.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblinfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblinfo.setBounds(100, 450, 400, 30);
		startPane.add(lblinfo);

		int i = 0;
		for (i = 0; i < 64; i++) {
			grid[i] = new JLabel(empty);
			grid[i].setName(Integer.toString(i));
			grid[i].setBounds(xgrid + (i % 8) * 50, ygrid + (i / 8) * 50, 50, 50);
			grid[i].addMouseListener(new MouseListener() { // 为窗口添加鼠标事件监听器
				@Override
				public void mousePressed(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1 && state) { // 判断获取的按钮是否为鼠标的左击, 只有AI算完时可用。
						state = false;
						String ss = ((JLabel) e.getSource()).getName();
						System.out.println("button " + ss + " pressed.");
						humanMove[numMove] = Integer.parseInt(ss);
						intpp();
						if (numMove >= 3) {
							numMove = 0;
							int j = 0;
							for (j = 0; j < len2; j++) {
								if (board[moves[j]] == 1) {
									grid[moves[j]].setIcon(black);
								} else if (board[moves[j]] == 2) {
									grid[moves[j]].setIcon(white);
								} else if (board[moves[j]] == 0) {
									grid[moves[j]].setIcon(empty);
								} else {

								}
							}

							Boolean valid = false;
							for (j = 0; j < len2; j++) {
								if (humanMove[2] == moves[j]) {
									valid = true;
								}
							}
							if (!valid) {
								numMove = 0;
								lblinfo.setText("[warning] invalid move");
								state = true;
								return;
							}
							board[humanMove[0]] = 0;
							board[humanMove[1]] = humanColor;
							board[humanMove[2]] = -1;
							grid[humanMove[0]].setIcon(empty);
							if (humanColor == 1) {
								grid[humanMove[1]].setIcon(black);
							} else {
								grid[humanMove[1]].setIcon(white);
							}
							grid[humanMove[2]].setIcon(arrow);
							steps[numsteps * 3] = humanMove[0];
							steps[numsteps * 3 + 1] = humanMove[1];
							steps[numsteps * 3 + 2] = humanMove[2];
							numsteps++;
							maxsteps = numsteps;

							lblinfo.setText("[running] ai is thinking");
							state = false;
							if (aiMoves()) { // if ai steps
								if (nomoreMoves()) { // human loses.
									lblinfo.setText("[terminated] ai wins");
								} else {
									lblinfo.setText("[ready] human player\'s turn");
									state = true;
								}
							} else { // ai loses.
								lblinfo.setText("[terminated] human player wins");
							}

//							if (validMove()) { // if human steps, judge whether the step is valid.
//								lblinfo.setText("[running] ai is thinking");
//								state = false;
//								if (aiMoves()) { // if ai steps
//									if (nomoreMoves()) { // human loses.
//										lblinfo.setText("[terminated] ai wins");
//									} else {
//										lblinfo.setText("[ready] human player\'s turn");
//										state = true;
//									}
//								} else { // ai loses.
//									lblinfo.setText("[terminated] human player wins");
//								}
//							} else { // invalid move.
//								lblinfo.setText("[warning] invalid move");
//							}
						} else if (numMove == 1) {
							int j = 0;
							if (board[humanMove[0]] != humanColor) {
								numMove = 0;
								lblinfo.setText("[warning] invalid move");
								state = true;
								return;
							}
							len1 = possiblemove(Integer.parseInt(ss));

							for (j = 0; j < len1; j++) {
								grid[moves[j]].setIcon(empty_h);
							}
							state = true;
						} else if (numMove == 2) {
							int j = 0;
							for (j = 0; j < len1; j++) {
								grid[moves[j]].setIcon(empty);
							}
							Boolean valid = false;
							for (j = 0; j < len1; j++) {
								if (humanMove[1] == moves[j]) {
									valid = true;
								}
							}
							if (!valid) {
								numMove = 0;
								lblinfo.setText("[warning] invalid move");
								state = true;
								return;
							}

							board[humanMove[0]] = 0;
							len2 = possiblemove(Integer.parseInt(ss));
							board[humanMove[0]] = humanColor;
							for (j = 0; j < len2; j++) {
								if (board[moves[j]] == 1) {
									grid[moves[j]].setIcon(black_r);
								} else if (board[moves[j]] == 2) {
									grid[moves[j]].setIcon(white_r);
								} else if (board[moves[j]] == 0) {
									grid[moves[j]].setIcon(empty_r);
								} else {

								}
							}
							state = true;
						}

					}
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub

				}
			});
			startPane.add(grid[i]);
		}

		if (type == 0) { // player starts play from "start" or menu bar->play->human player first/ai first
			numsteps = 0;
			maxsteps = 0;
			for (i = 0; i < 64; i++) {
				board[i] = 0;
			}
			board[2] = 1;
			board[5] = 1;
			board[16] = 1;
			board[23] = 1;
			board[40] = 2;
			board[47] = 2;
			board[58] = 2;
			board[61] = 2;
			grid[2].setIcon(black);
			grid[5].setIcon(black);
			grid[16].setIcon(black);
			grid[23].setIcon(black);
			grid[40].setIcon(white);
			grid[47].setIcon(white);
			grid[58].setIcon(white);
			grid[61].setIcon(white);
		} else if (type == 1) { // player starts the play from saves.
			for (i = 0; i < 64; i++) {
				switch (board[i]) {
				case 0:
					grid[i].setIcon(empty);
					break;
				case 1:
					grid[i].setIcon(black);
					break;
				case 2:
					grid[i].setIcon(white);
					break;
				case -1:
					grid[i].setIcon(arrow);
					break;
				default: {
					System.out.println("save has been damaged.");
				}
				}

			}
		} else {

		}

		if (humanColor == 2 && type != 1) {
			if (aiMoves()) {
				if (nomoreMoves()) {
					lblinfo.setText("[terminated] ai wins");
				} else {
					lblinfo.setText("[ready] human player\'s turn");
					state = true;
				}
			} else {
				lblinfo.setText("[terminated] human player wins");
			}
		}

		/*
		 * back button
		 */
		JButton btnBack = new JButton("back");
		btnBack.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnBack.setBounds(10, 500, 80, 20);
		startPane.add(btnBack);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					state = false;
					turnback(startPane);
				} catch (Exception e2) {
					e2.printStackTrace();
					System.exit(0);
				}
			}
		});

		setContentPane(startPane);
	}

	/*
	 * help panel init
	 */
	public void helpPaneinit() {
		state = false;
		helpPane.removeAll();
		helpPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(helpPane);
		helpPane.setLayout(null);

		JLabel lblHelp = new JLabel("Help");
		lblHelp.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblHelp.setHorizontalAlignment(SwingConstants.CENTER);
		lblHelp.setBounds(219, 16, 162, 30);
		helpPane.add(lblHelp);

		JLabel lblhowCanI = new JLabel("1.how can I play?");
		lblhowCanI.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblhowCanI.setBounds(166, 58, 177, 36);
		helpPane.add(lblhowCanI);

		JLabel lblYouCanPress = new JLabel("you can press \"start\" to play with AI.");
		lblYouCanPress.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblYouCanPress.setBounds(118, 106, 296, 30);
		helpPane.add(lblYouCanPress);

		JLabel lblOrMenubarplayhumanPlayer = new JLabel("or MenuBar->play->human player first/AI first");
		lblOrMenubarplayhumanPlayer.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblOrMenubarplayhumanPlayer.setBounds(118, 148, 406, 30);
		helpPane.add(lblOrMenubarplayhumanPlayer);

		JLabel lblhowToSave = new JLabel("2.how to save my game?");
		lblhowToSave.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblhowToSave.setBounds(165, 190, 201, 36);
		helpPane.add(lblhowToSave);

		JLabel lblMenubareditsave = new JLabel("MenuBar->edit->save");
		lblMenubareditsave.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblMenubareditsave.setBounds(118, 238, 201, 30);
		helpPane.add(lblMenubareditsave);

		JLabel lblwhatsTheRules = new JLabel("4.what's the rules?");
		lblwhatsTheRules.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblwhatsTheRules.setBounds(166, 364, 169, 34);
		helpPane.add(lblwhatsTheRules);

		JLabel lblYouCanVisit = new JLabel("you can visit");
		lblYouCanVisit.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblYouCanVisit.setBounds(118, 410, 126, 30);
		helpPane.add(lblYouCanVisit);

		JLabel lblHttpsenwikipediaorgwikigameoftheamazons = new JLabel(
				"https://en.wikipedia.org/wiki/Game_of_the_Amazons");
		lblHttpsenwikipediaorgwikigameoftheamazons.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblHttpsenwikipediaorgwikigameoftheamazons.setBounds(118, 452, 434, 32);
		helpPane.add(lblHttpsenwikipediaorgwikigameoftheamazons);

		JLabel lblhowToPlay = new JLabel("3.how to play saved games?");
		lblhowToPlay.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblhowToPlay.setBounds(165, 280, 231, 30);
		helpPane.add(lblhowToPlay);

		JLabel lblMenubarplayplaySavedGames = new JLabel("MenuBar->play->play saved games");
		lblMenubarplayplaySavedGames.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblMenubarplayplaySavedGames.setBounds(118, 322, 296, 30);
		helpPane.add(lblMenubarplayplaySavedGames);

		/*
		 * back button
		 */
		JButton btnBack = new JButton("back");
		btnBack.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnBack.setBounds(10, 500, 80, 20);
		helpPane.add(btnBack);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					turnback(helpPane);
				} catch (Exception e2) {
					e2.printStackTrace();
					System.exit(0);
				}
			}
		});
	}

	/*
	 * about panel init.
	 */
	public void aboutPaneinit() {
		state = false;
		aboutPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(aboutPane);
		aboutPane.setLayout(null);

		JLabel lblContent = new JLabel("About");
		lblContent.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblContent.setHorizontalAlignment(SwingConstants.CENTER);
		lblContent.setBounds(219, 16, 162, 30);
		aboutPane.add(lblContent);

		JLabel lblAmazonzeroV = new JLabel("AmazonZero v0.7");
		lblAmazonzeroV.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblAmazonzeroV.setBounds(228, 96, 153, 30);
		aboutPane.add(lblAmazonzeroV);

		JLabel lblCopyrightYuchenxi = new JLabel("Copyright © 2018 yuchenxi ");
		lblCopyrightYuchenxi.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblCopyrightYuchenxi.setBounds(192, 150, 226, 30);
		aboutPane.add(lblCopyrightYuchenxi);

		JLabel label = new JLabel("2018.12.2");
		label.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		label.setBounds(257, 192, 93, 30);
		aboutPane.add(label);

		JLabel lblAnyRequestsPlease = new JLabel("any issues please contact me by email.");
		lblAnyRequestsPlease.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblAnyRequestsPlease.setBounds(150, 272, 336, 35);
		aboutPane.add(lblAnyRequestsPlease);

		JLabel lblEmailqqcom = new JLabel("email:2763057708@qq.com");
		lblEmailqqcom.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblEmailqqcom.setBounds(194, 319, 254, 35);
		aboutPane.add(lblEmailqqcom);

		/*
		 * back button
		 */
		JButton btnBack = new JButton("back");
		btnBack.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnBack.setBounds(10, 500, 80, 20);
		aboutPane.add(btnBack);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					turnback(aboutPane);
				} catch (Exception e2) {
					e2.printStackTrace();
					System.exit(0);
				}
			}
		});
	}

	/*
	 * saves manage panel init
	 */

	private int max_per_page = 3;
	private int curPage = 0;
	private int length = 0;

	public void savesPaneinit() {
		state = false;
		savesPane.removeAll();
		savesPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(savesPane);
		savesPane.setLayout(null);

		JLabel lblContent = new JLabel("Select one to play");
		lblContent.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblContent.setHorizontalAlignment(SwingConstants.CENTER);
		lblContent.setBounds(219, 16, 162, 30);
		savesPane.add(lblContent);

		String saves[] = new String[100];

		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec("ls " + spath + "/saves");
			InputStream stdin = proc.getInputStream();
			Scanner scan = new Scanner(stdin);
			int i = 0;
			while (scan.hasNext()) {
				saves[i] = scan.next();
//				System.out.println(saves[i]);
				i++;
				if (i >= 100) {
					break;
				}
			}
			length = i;
			scan.close();
			int returnVal = proc.waitFor();
			if (returnVal != 0) {
				System.out.println("ls script came across a error.");
				System.exit(1);
			}
		} catch (Exception e) {
			System.out.println("ls script came across a error.");
			e.printStackTrace();
			System.exit(1);
		}

		int i = 0;
		for (i = curPage * max_per_page; i < (curPage + 1) * max_per_page && i < length; i++) {
//			System.out.println(i);
			JLabel label = new JLabel(saves[i]); // save label, when clicked turn to start panel
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
			label.setBounds(100, (i - curPage * max_per_page + 1) * 100, 400, 50);
			label.addMouseListener(new MouseListener() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1) {
						try {
							// read the save and load the state of board, and turn to saves preview panel
							numsteps = 0;
							maxsteps = 0;
							curFile = ((JLabel) e.getSource()).getText();
							File f = new File(spath + "/saves/" + curFile);
							InputStream is = new FileInputStream(f);
							Scanner scan = new Scanner(is);
							createDate = scan.nextLine();
							humanColor = scan.nextInt();
							int i = 0;
							for (i = 0; i < 64; i++) {
								board[i] = scan.nextInt();
							}
							while (scan.hasNextInt()) {
								steps[numsteps * 3] = scan.nextInt();
								steps[numsteps * 3 + 1] = scan.nextInt();
								steps[numsteps * 3 + 2] = scan.nextInt();
								numsteps++;
							}

							scan.close();
							is.close();

							contentPane.setEnabled(false);
							startPane.setEnabled(false);
							savesPane.setEnabled(false);
							helpPane.setEnabled(false);
							aboutPane.setEnabled(false);
							previewPane.setEnabled(true);

							contentPane.setVisible(false);
							startPane.setVisible(false);
							savesPane.setVisible(false);
							helpPane.setVisible(false);
							aboutPane.setVisible(false);
							previewPane.setVisible(true);

							previewPaneinit();

							System.out.println("save opened successfully.");

						} catch (Exception ee) {
							ee.printStackTrace();
						}
					}
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub

				}
			});
			savesPane.add(label);
		}

		// insert turn left & right button.
		JLabel leftlabel = new JLabel();
		leftlabel.setIcon(left);
		leftlabel.setBounds(150, 430, 50, 50);
		leftlabel.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && curPage != 0) {
					curPage--; // go to previous page
					System.out.println("Page down.");
					savesPaneinit();
				}

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		savesPane.add(leftlabel);

		JLabel rightlabel = new JLabel();
		rightlabel.setIcon(right);
		rightlabel.setBounds(400, 430, 50, 50);
		rightlabel.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && (curPage + 1) * max_per_page < length) {
					curPage++; // go to next page.
					System.out.println("Page up.");
					savesPaneinit();
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		savesPane.add(rightlabel);

		// a label to display the current page number.
		JLabel pagelabel = new JLabel();
		pagelabel.setBounds(225, 436, 150, 30);
		pagelabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		pagelabel.setHorizontalAlignment(SwingConstants.CENTER);
		pagelabel.setText("page " + (curPage + 1) + " / " + (int)Math.ceil((double)length / 3.0));
		savesPane.add(pagelabel);

		/*
		 * back button
		 */
		JButton btnBack = new JButton("back");
		btnBack.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnBack.setBounds(10, 500, 80, 20);
		savesPane.add(btnBack);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					turnback(savesPane);
				} catch (Exception e2) {
					e2.printStackTrace();
					System.exit(0);
				}
			}
		});
	}

	public void previewPaneinit() {
		state = false;
		previewPane.removeAll();
		previewPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		previewPane.setLayout(null);
		setContentPane(previewPane);

		JLabel lblName = new JLabel("name");
		lblName.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblName.setBounds(148, 58, 54, 44);
		previewPane.add(lblName);

		JTextField textField = new JTextField();
		textField.setBounds(219, 69, 190, 26);
		previewPane.add(textField);
		textField.setColumns(10);
		textField.setText(curFile);

		int xgrid = 188;
		int ygrid = 161;
		int i = 0;
		for (i = 0; i < 64; i++) {
			grid[i] = new JLabel(empty);
			grid[i].setBounds(xgrid + (i % 8) * 30, ygrid + (i / 8) * 30, 30, 30);
			previewPane.add(grid[i]);
		}

		for (i = 0; i < 64; i++) {
			switch (board[i]) {
			case 0:
				grid[i].setIcon(empty_s);
				break;
			case 1:
				grid[i].setIcon(black_s);
				break;
			case 2:
				grid[i].setIcon(white_s);
				break;
			case -1:
				grid[i].setIcon(arrow_s);
				break;
			default: {
				System.out.println("save has been damaged.");
			}
			}

		}

		JLabel lblCreateDate = new JLabel("create date");
		lblCreateDate.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblCreateDate.setBounds(101, 100, 108, 30);
		previewPane.add(lblCreateDate);

//		JTextField textField_1 = new JTextField();
//		textField_1.setBounds(219, 104, 190, 26);
//		previewPane.add(textField_1);
//		textField_1.setColumns(10);
//		textField_1.setText(createDate);
		
		JLabel lblDate = new JLabel();
		lblDate.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		lblDate.setBounds(219, 104, 190, 26);
		lblDate.setText(createDate);
		previewPane.add(lblDate);

		JButton btnDelete = new JButton("delete");
		btnDelete.setBounds(123, 497, 117, 29);
		previewPane.add(btnDelete);
		btnDelete.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					try {
						Boolean checked = false;
						Runtime rt = Runtime.getRuntime();
						Process proc = rt.exec(spath + "/Java/ascript-rm-check");
						InputStream stdin = proc.getInputStream();
						Scanner scan = new Scanner(stdin);
						if (scan.hasNextLine()) {
							String ans = scan.nextLine();
							System.out.println(ans);
							if (ans.compareTo("button returned:好") == 0) {
								checked = true;
							}else {
								checked = false;
							}
						}
						scan.close();
						proc.waitFor();
//						if (returnVal != 0 && returnVal != -128) {
//							System.out.println("ascript came across a error.");
//							System.out.println(returnVal);
//							System.exit(1);
//						}
						if (checked) {
							
							System.out.println("cmd:\"rm -f " + spath + "/saves/" + curFile + "\"");
							rt.exec("rm -f " + spath + "/saves/" + curFile).waitFor();
							
							
							contentPane.setEnabled(false);
							startPane.setEnabled(false);
							savesPane.setEnabled(true);
							helpPane.setEnabled(false);
							aboutPane.setEnabled(false);
							previewPane.setEnabled(false);

							contentPane.setVisible(false);
							startPane.setVisible(false);
							savesPane.setVisible(true);
							helpPane.setVisible(false);
							aboutPane.setVisible(false);
							previewPane.setVisible(false);

							curPage = 0;
							savesPaneinit();

						}else {
							textField.setText(curFile);
						}

					} catch (Exception ee) {
						ee.printStackTrace();
					}
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		JButton btnSave = new JButton("save");
		btnSave.setBounds(264, 497, 117, 29);
		previewPane.add(btnSave);
		btnSave.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					try {
						Runtime rt = Runtime.getRuntime();
						
						Boolean checked = true;
						String newName = textField.getText();
						
						if (newName == null || newName.isEmpty()) {
							textField.setText(curFile);
							return;
						}
						if (newName.length() >= 64) {
							rt.exec(spath + "/Java/ascript-name-length-error").waitFor();
							textField.setText(curFile);
							return;
						}
						
						char charName[] = newName.toCharArray();
						for (char c : charName) {
							if ((c <= '9' && c >= '0') || (c <= 'z' && c >= 'a') || (c <= 'Z' && c >= 'A') || c == '_' || c == '-') {

							} else {
								checked = false;
							}
						}
						
						if (checked) {
							Boolean hasError = false;
							Process proc = rt.exec("mv " + spath + "/saves/" + curFile + ' ' + spath + "/saves/" + newName);
							int returnVal = proc.waitFor();
							if (returnVal != 0) {
								hasError = true;
								System.exit(1);
							}
							if (hasError) {
								rt.exec(spath + "/Java/ascript-mv-failed").waitFor();
							} else {
								curFile = newName;
								rt.exec(spath + "/Java/ascript-mv-success").waitFor();
							}

						} else {
							rt.exec(spath + "/Java/ascript-name-syntax-error").waitFor();
						}
						textField.setText(curFile);
					} catch (Exception ee) {
						ee.printStackTrace();
					}
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		JButton btnPlay = new JButton("play");
		btnPlay.setBounds(404, 497, 117, 29);
		previewPane.add(btnPlay);
		btnPlay.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					try {

						contentPane.setEnabled(false);
						startPane.setEnabled(true);
						savesPane.setEnabled(false);
						helpPane.setEnabled(false);
						aboutPane.setEnabled(false);
						previewPane.setEnabled(false);

						contentPane.setVisible(false);
						startPane.setVisible(true);
						savesPane.setVisible(false);
						helpPane.setVisible(false);
						aboutPane.setVisible(false);
						previewPane.setVisible(false);

						startPaneinit(humanColor, 1);

						System.out.println("start game from saves");

					} catch (Exception ee) {
						ee.printStackTrace();
					}
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		
		/*
		 * back button
		 */
		JButton btnBack = new JButton("back");
		btnBack.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnBack.setBounds(10, 500, 80, 20);
		previewPane.add(btnBack);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					contentPane.setEnabled(false);
					startPane.setEnabled(false);
					savesPane.setEnabled(true);
					helpPane.setEnabled(false);
					aboutPane.setEnabled(false);
					previewPane.setEnabled(false);

					contentPane.setVisible(false);
					startPane.setVisible(false);
					savesPane.setVisible(true);
					helpPane.setVisible(false);
					aboutPane.setVisible(false);
					previewPane.setVisible(false);

					savesPaneinit();
				} catch (Exception e2) {
					e2.printStackTrace();
					System.exit(0);
				}
			}
		});

	}

	/**
	 * Create the frame.
	 */
	public MainFrame(String spath) {
		state = false;
		this.spath = spath;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 600);
		setMaximumSize(new Dimension(600, 600));// set the maximum window size
		setMinimumSize(new Dimension(600, 600));// set the minimum window size

		contentPane = new backImage(spath + "/Resources/background.png");
		startPane = new backImage(spath + "/Resources/background.png");
		savesPane = new backImage(spath + "/Resources/background.png");
		helpPane = new backImage(spath + "/Resources/background.png");
		aboutPane = new backImage(spath + "/Resources/background.png");
		previewPane = new backImage(spath + "/Resources/background.png");

		// menu bar start
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("play"); // play menu, choose play mode and play from saves.
		menuBar.add(mnNewMenu);

		JMenuItem mntmHumanPlayerFirst = new JMenuItem("human player first");
		mntmHumanPlayerFirst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					contentPane.setEnabled(false);
					startPane.setEnabled(true);
					savesPane.setEnabled(false);
					helpPane.setEnabled(false);
					aboutPane.setEnabled(false);
					previewPane.setEnabled(false);

					contentPane.setVisible(false);
					startPane.setVisible(true);
					savesPane.setVisible(false);
					helpPane.setVisible(false);
					aboutPane.setVisible(false);
					previewPane.setVisible(false);

					startPaneinit(1, 0);
				} catch (Exception e2) {
					e2.printStackTrace();
					System.exit(0);
				}
			}
		});
		mnNewMenu.add(mntmHumanPlayerFirst);

		JMenuItem mntmAiFirst = new JMenuItem("AI first");
		mntmAiFirst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					contentPane.setEnabled(false);
					startPane.setEnabled(true);
					savesPane.setEnabled(false);
					helpPane.setEnabled(false);
					aboutPane.setEnabled(false);
					previewPane.setEnabled(false);

					contentPane.setVisible(false);
					startPane.setVisible(true);
					savesPane.setVisible(false);
					helpPane.setVisible(false);
					aboutPane.setVisible(false);
					previewPane.setVisible(false);
					startPaneinit(2, 0);
				} catch (Exception e2) {
					e2.printStackTrace();
					System.exit(0);
				}
			}
		});
		mnNewMenu.add(mntmAiFirst);

		JMenuItem mntmPlaySavedGames = new JMenuItem("play saved games"); // open saves manage panel
		mntmPlaySavedGames.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					contentPane.setEnabled(false);
					startPane.setEnabled(false);
					savesPane.setEnabled(true);
					helpPane.setEnabled(false);
					aboutPane.setEnabled(false);
					previewPane.setEnabled(false);

					contentPane.setVisible(false);
					startPane.setVisible(false);
					savesPane.setVisible(true);
					helpPane.setVisible(false);
					aboutPane.setVisible(false);
					previewPane.setVisible(false);
					savesPaneinit();
				} catch (Exception e2) {
					e2.printStackTrace();
					System.exit(0);
				}
			}
		});
		mnNewMenu.add(mntmPlaySavedGames);

		JMenu mnEdit = new JMenu("edit");
		menuBar.add(mnEdit);

		JMenuItem mntmSave = new JMenuItem("save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Runtime rt = Runtime.getRuntime();
					if (state) { // when there's a game and the game is not terminated, trying to save
						clearHints();
						numMove = 0;
						Date date = new Date();
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
						File f = new File(spath + "/saves/" + df.format(date)); // date as file name, avoid conflicts
						OutputStream os = new FileOutputStream(f);
						OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
						writer.write(df.format(date) + '\n');
						writer.write(Integer.toString(humanColor) + "\n");
						int i = 0;
						for (i = 0; i < 64; i++) {
							writer.write(board[i] + "\n");
						}
						for (i = 0; i < numsteps; i++) {
							writer.write(steps[i * 3] + "\n");
							writer.write(steps[i * 3 + 1] + "\n");
							writer.write(steps[i * 3 + 2] + "\n");
						}
						writer.close();
						os.close();
						System.out.println("saved successfully.");
						rt.exec(spath + "/Java/ascript-success").waitFor();
					} else {
						rt.exec(spath + "/Java/ascript-failed").waitFor();
					}

				} catch (Exception e2) {
					e2.printStackTrace();
					System.exit(0);
				}
			}
		});
		mnEdit.add(mntmSave);

		JMenuItem mntmUndo = new JMenuItem("undo");
		mntmUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		});
		mnEdit.add(mntmUndo);

		JMenuItem mntmRedo = new JMenuItem("redo");
		mntmRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		});
		mnEdit.add(mntmRedo);
		// menu bar end

		contentPane.setEnabled(true);
		contentPane.setVisible(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Game of Amazons");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(219, 16, 162, 30);
		contentPane.add(lblNewLabel);

		/*
		 * 开始按钮
		 */
		JButton btnStart = new JButton("start"); // turn to start panel
		btnStart.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnStart.setBounds(219, 90, 162, 40);
		contentPane.add(btnStart);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					contentPane.setEnabled(false);
					startPane.setEnabled(true);
					savesPane.setEnabled(false);
					helpPane.setEnabled(false);
					aboutPane.setEnabled(false);
					previewPane.setEnabled(false);

					contentPane.setVisible(false);
					startPane.setVisible(true);
					savesPane.setVisible(false);
					helpPane.setVisible(false);
					aboutPane.setVisible(false);
					previewPane.setVisible(false);
					startPaneinit(1, 0);
				} catch (Exception e2) {
					e2.printStackTrace();
					System.exit(0);
				}
			}
		});

		/*
		 * 帮助
		 */
		JButton btnHelp = new JButton("help"); // turn to help panel
		btnHelp.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnHelp.setBounds(219, 190, 162, 40);
		contentPane.add(btnHelp);
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					contentPane.setEnabled(false);
					startPane.setEnabled(false);
					savesPane.setEnabled(false);
					helpPane.setEnabled(true);
					aboutPane.setEnabled(false);
					previewPane.setEnabled(false);

					contentPane.setVisible(false);
					startPane.setVisible(false);
					savesPane.setVisible(false);
					helpPane.setVisible(true);
					aboutPane.setVisible(false);
					previewPane.setVisible(false);
					helpPaneinit();
				} catch (Exception e2) {
					e2.printStackTrace();
					System.exit(0);
				}
			}
		});

		/*
		 * 作者信息
		 */
		JButton btnAbout = new JButton("about"); // turn to about panel
		btnAbout.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnAbout.setBounds(219, 290, 162, 40);
		contentPane.add(btnAbout);
		btnAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					contentPane.setEnabled(false);
					startPane.setEnabled(false);
					savesPane.setEnabled(false);
					helpPane.setEnabled(false);
					aboutPane.setEnabled(true);
					previewPane.setEnabled(false);

					contentPane.setVisible(false);
					startPane.setVisible(false);
					savesPane.setVisible(false);
					helpPane.setVisible(false);
					aboutPane.setVisible(true);
					previewPane.setVisible(false);
					aboutPaneinit();
				} catch (Exception e2) {
					e2.printStackTrace();
					System.exit(0);
				}
			}
		});
	}

}
