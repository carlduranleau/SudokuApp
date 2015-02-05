package dev.zackland.application.sudoku;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class SudokuFrame extends javax.swing.JFrame implements ActionListener, KeyListener {
	
	public final int LEVEL_EASY = 0;
	public final int LEVEL_MEDIUM = 1;
	public final int LEVEL_HARD = 2;
	public final int LEVEL_EXPERT = 3;
	
	private JTable grid;
	private SudokuTable data = null;
	private Cell[] gameCells = null;
	private Vector holes = new Vector();
	private int _level = 0;
	
	private JCheckBoxMenuItem _levelEasyMenu = null;
	private JCheckBoxMenuItem _levelMediumMenu = null;
	private JCheckBoxMenuItem _levelHardMenu = null;
	private JCheckBoxMenuItem _levelExpertMenu = null;
	
	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public SudokuFrame(SudokuTable data) {
		super("Sudoku v1.0");
		this.data = data;
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		JMenu helpMenu = new JMenu("?");
		
		JMenu levelMenu = new JMenu("Level");
		levelMenu.setMnemonic(KeyEvent.VK_L);
		
		_levelEasyMenu = new JCheckBoxMenuItem("Easy");
		_levelEasyMenu.setMnemonic(KeyEvent.VK_E);
		_levelEasyMenu.setState(true);
		_levelMediumMenu = new JCheckBoxMenuItem("Medium");
		_levelMediumMenu.setMnemonic(KeyEvent.VK_M);
		_levelMediumMenu.setState(false);
		_levelHardMenu = new JCheckBoxMenuItem("Hard");
		_levelHardMenu.setMnemonic(KeyEvent.VK_H);
		_levelHardMenu.setState(false);
		_levelExpertMenu = new JCheckBoxMenuItem("Expert");
		_levelExpertMenu.setMnemonic(KeyEvent.VK_X);
		_levelExpertMenu.setState(false);
		
		JMenuItem fileNewMenu = new JMenuItem("New game");
		fileNewMenu.setMnemonic(KeyEvent.VK_N);
		JMenuItem fileQuitMenu = new JMenuItem("Quit");
		fileQuitMenu.setMnemonic(KeyEvent.VK_Q);
		JMenuItem helpAboutMenu = new JMenuItem("About");
		helpAboutMenu.setMnemonic(KeyEvent.VK_A);
		
		fileMenu.add(fileNewMenu);
		fileMenu.addSeparator();
		fileMenu.add(fileQuitMenu);
		levelMenu.add(_levelEasyMenu);
		levelMenu.add(_levelMediumMenu);
		levelMenu.add(_levelHardMenu);
		levelMenu.add(_levelExpertMenu);
		helpMenu.add(helpAboutMenu);
		menuBar.add(fileMenu);
		menuBar.add(levelMenu);
		menuBar.add(helpMenu);
		
		this.setJMenuBar(menuBar);
		
		fileNewMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				initGame();
			}
		});
		fileQuitMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exitApp();
			}
		});
		helpAboutMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				about();
			}
		});

		_levelEasyMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setLevel(LEVEL_EASY);
			}
		});
		_levelMediumMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setLevel(LEVEL_MEDIUM);
			}
		});
		_levelHardMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setLevel(LEVEL_HARD);
			}
		});
		_levelExpertMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setLevel(LEVEL_EXPERT);
			}
		});
		
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(270, 270);

		initGame();
	}
	
	public void setLevel(int level) {
		JCheckBoxMenuItem m = null;
		switch (_level) {
		case 0:
			m = _levelEasyMenu;
			break;
		case 1:
			m = _levelMediumMenu;
			break;
		case 2:
			m = _levelHardMenu;
			break;
		case 3:
			m = _levelExpertMenu;
			break;
		}
		
		m.setState(false);
		_level = level;
		switch (_level) {
		case 0:
			m = _levelEasyMenu;
			break;
		case 1:
			m = _levelMediumMenu;
			break;
		case 2:
			m = _levelHardMenu;
			break;
		case 3:
			m = _levelExpertMenu;
			break;
		}
		
		m.setState(true);
		initGame();
	}
	
	public int getLevel() {
		return _level;
	}
	
	private void exitApp() {
		this.dispose();
	}
	
	public void about() {
		JOptionPane.showMessageDialog(this, "Sudoku V1.0 by Carl Duranleau\n\nBuilt with Eclipse Technologies");
	}
	
	private void initGame() {
		data.generateTable();
		
		//For debug purpose only - Print the full Sudoku grill
		System.out.println(data);
		
		this.gameCells = this.data.getData().clone();
		generateHoles();
		getContentPane().removeAll();
		initGUI();
	}
	
	private void generateHoles() {
		int[] pool = new int[81];
		Random r = new Random();
		int maxholes = 0;
		
		switch (_level) {
		case 0:
			maxholes = r.nextInt(10)+ 7;
			break;
		case 1:
			maxholes = r.nextInt(17)+ 16;
			break;
		case 2:
			maxholes = r.nextInt(17)+ 32;
			break;
		case 3:
			maxholes = r.nextInt(17)+ 48;
			break;
		}
		
		int poolSize = 81;
		
		int pos;
		
		for(int i = 0; i < 81; i++) {
			pool[i]= i;
		}
		
		for(int i = 0; i < maxholes; i++) {
			pos = r.nextInt(poolSize);
			gameCells[pool[pos]] = null;
			if (pos != (poolSize - 1)) pool[pos] = pool[poolSize - 1];
			poolSize--;
		}
	}
	
	private void initGUI() {
		try {
			GridLayout grid = new GridLayout(9, 9, 0, 0);
			getContentPane().setLayout(grid);
			
			for (int i = 0; i < 81; i++) {
				Box verticalBox = Box.createVerticalBox();
				Box horizontalBox = Box.createHorizontalBox();
				Component cellComponent = null;

				
				if (gameCells[i] == null) {
					final int index = i;
					cellComponent = new TextField("", 1) {
						public String toString() {
							return Integer.toString(index);
						}
					};
					holes.add(cellComponent);
					((TextField)cellComponent).addActionListener(this);
					((TextField)cellComponent).addKeyListener(this);
				}
				else
					cellComponent = new Label(Integer.toString(gameCells[i].getValue()), Label.CENTER);
				
				verticalBox.setSize(30,30);
				horizontalBox.setSize(30,30);
				
				horizontalBox.add(Box.createHorizontalGlue());
				horizontalBox.add(cellComponent);
				horizontalBox.add(Box.createHorizontalGlue());

				verticalBox.add(Box.createVerticalGlue());
				verticalBox.add(horizontalBox);
				verticalBox.add(Box.createVerticalGlue());
				add(verticalBox);
			}
			
			setResizable(false);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == null || e.getActionCommand().equals("")) return;
		TextField component = (TextField)e.getSource();
		gameCells[Integer.parseInt(component.toString())] = data.createCell(Integer.parseInt(component.toString()), Integer.parseInt(component.getText()));
		boolean valid = data.isValid(gameCells[Integer.parseInt(component.toString())], gameCells);

		if (valid) {
			component.setBackground(Color.GREEN);
			if (validateAll()) {
				for(int i = 0; i < holes.size(); i++) {
					((TextField)holes.elementAt(i)).setBackground(Color.GREEN);
				}
				JOptionPane.showMessageDialog(this, "Well done!");
			}
		} else {
			component.setBackground(Color.RED);
		}
	}

	private boolean validateAll() {
		for (int i = 0; i < 81; i++) {
			if (!data.isValid(gameCells[i], gameCells)) return false;
		}
		return true;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		char c = e.getKeyChar();
		TextField component = (TextField)e.getComponent();
		component.setBackground(Color.WHITE);
		if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_ENTER) return;

		if (!(component.getText().equals(""))) {
			e.consume();
			return;
		}
		
		if ((c < '1') || (c > '9')) {
			getToolkit().beep();
			e.consume();
		}
	}

	@Override
	public void keyReleased(KeyEvent key) {
	}

	@Override
	public void keyTyped(KeyEvent key) {
	}
}
