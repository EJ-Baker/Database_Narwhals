import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Window extends JFrame implements ActionListener {

	private JLabel imageLabel = imageLabel();
	private JLabel nameLabel = nameLabel();
	private JPanel mainPanel;
	private SqlConnector sqlConnector;

	private HomePanel home = new HomePanel(this);
	private SearchPanel search = new SearchPanel(this);
	private ListPanel list = new ListPanel(this);
	private UpdatePanel update = new UpdatePanel(this);
	private DeletePanel delete = new DeletePanel(this);
	private final JPanel[] panels = { home, search, list, update, delete };

	public static final int WIDTH = 500;
	public static final int HEIGHT = 600;
	public static final int PHOTO_HEIGHT = 271;
	public static final int NAME_POSITION = 550;

	public Window() {

		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.add(imageLabel);
		mainPanel.add(nameLabel);

		home.setVisible(true);
		search.setVisible(false);
		list.setVisible(false);
		update.setVisible(false);
		delete.setVisible(false);

		getContentPane().add(mainPanel, BorderLayout.CENTER);

		try {
			sqlConnector = new SqlConnector();
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
		}
	}

	private JLabel imageLabel() {

		JLabel label = null;

		try {
			// Download:
			// https://en.wikipedia.org/wiki/Marvel_Studios#/media/File:Marvel_Studios_logo.jpg
			BufferedImage image = ImageIO.read(new File(
					"Marvel_Studios_logo.jpg"));
			label = new JLabel(new ImageIcon(image));
			label.setBounds(0, 0, WIDTH, PHOTO_HEIGHT); // Aspect: 500 * 271
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return label;
	}

	private JLabel nameLabel() {

		JLabel label = new JLabel(
				" Implemented by Database_Narwhals. ~May 4, 2016~");
		label.setBounds(0, NAME_POSITION, WIDTH, 30);
		label.setBackground(Color.LIGHT_GRAY);
		label.setBorder(BorderFactory
				.createMatteBorder(1, 0, 0, 0, Color.BLACK));
		return label;
	}

	// May not needed
	public void actionPerformed(ActionEvent e) {

	}

	private void replacePanel(JPanel panel) {

		for (int i = 0; i < panels.length; i++) {
			if (panels[i] == panel) {
				panels[i].setVisible(true);
			} else {
				panels[i].setVisible(false);
			}
		}
	}

	private String getUserInput(String attribute) {

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JLabel label = new JLabel("Input " + attribute + ": ");
		JTextField form = new JTextField();
		form.addActionListener(this);
		panel.add(label, BorderLayout.PAGE_START);
		panel.add(form, BorderLayout.PAGE_END);
		JOptionPane.showMessageDialog(this, panel, "User input",
				JOptionPane.DEFAULT_OPTION);
		return form.getText();
	}

	private void displayResults(ArrayList<String> resultSet) {

		String result = "";

		for (int i = 0; i < resultSet.size(); i++) {
			result += resultSet.get(i) + " <br/>";
		}

		if (result.equals("")) {
			result = "No results.";
		}

		JLabel text = new JLabel("<html><p>" + result + "</p><html>",
				SwingConstants.CENTER);
		JScrollPane scrollPane = new JScrollPane(text);
		scrollPane.setPreferredSize(new Dimension(300, 300));
		JOptionPane.showMessageDialog(this, scrollPane, "Results",
				JOptionPane.DEFAULT_OPTION);
	}

	public static void main(String[] args) {

		Window w = new Window();
		w.setTitle("Database_Narwhals");
		w.setBounds(0, 0, WIDTH, HEIGHT);
		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		w.setVisible(true);
	}

	private class HomePanel extends JPanel implements ActionListener {

		private Window window;
		private JLabel label, title;
		private JComboBox<String> box;
		private JButton submit;
		public final String[] OPTIONS = { "Search from database",
				"List from database", "Update database",
				"Add/delete from database" };

		public HomePanel(Window win) {

			this.window = win;
			this.setBounds(100, 300, 300, 200);
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			title = new JLabel("Welcome to Movie Database !!");
			title.setAlignmentX(0.5f);
			title.setAlignmentY(1.0f);
			title.setForeground(Color.BLUE);
			title.setFont(new Font("Century", Font.ITALIC, 18));
			this.add(title);

			label = new JLabel("What would you like to do today ?");
			label.setAlignmentX(0.5f);
			label.setAlignmentY(0.0f);
			this.add(label);

			box = new JComboBox<String>(OPTIONS);
			box.setSelectedIndex(0);
			box.addActionListener(this);
			box.setAlignmentY(0.0f);
			this.add(box);

			submit = new JButton("Submit");
			submit.addActionListener(this);
			submit.setAlignmentX(0.5f);
			submit.setAlignmentY(0.0f);
			this.add(submit);

			window.add(this);
		}

		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == submit) {
				switch (box.getSelectedIndex()) {
				case 0:
					window.replacePanel(search);
					break;
				case 1:
					window.replacePanel(list);
					break;
				case 2:
					window.replacePanel(update);
					break;
				case 3:
					window.replacePanel(delete);
					break;
				}
			}
		}
	}

	private class SearchPanel extends JPanel implements ActionListener {

		private Window window;
		private JLabel label, title;
		private JComboBox<String> box1, box2;
		private JButton submit, home;
		public final String[] OPTIONS = { "Movies", "Character", "Abilities" };
		public final String[][] SEARCH_ATTRIBUTES = {
				{ "Character", "Director", "Actor", "World" },
				{ "Popularity" }, { "Character" } };

		public SearchPanel(Window win) {

			this.window = win;
			this.setBounds(100, 300, 300, 250);
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			title = new JLabel("What would you like to search ? ");
			title.setAlignmentX(0.5f);
			title.setForeground(Color.BLUE);
			title.setFont(new Font("Century", Font.ITALIC, 15));
			this.add(title);

			box1 = new JComboBox<String>(OPTIONS);
			// box1.setSelectedIndex(0);
			box1.addActionListener(this);
			box1.setAlignmentY(0.0f);
			this.add(box1);

			label = new JLabel("By what attribute ? ");
			label.setAlignmentX(0.5f);
			label.setForeground(Color.BLUE);
			label.setFont(new Font("Century", Font.ITALIC, 15));
			this.add(label);

			box2 = new JComboBox<String>(
					SEARCH_ATTRIBUTES[box1.getSelectedIndex()]);
			// box2.setSelectedIndex(0);
			box2.addActionListener(this);
			box2.setAlignmentY(0.0f);
			this.add(box2);

			submit = new JButton("Submit");
			submit.addActionListener(this);
			submit.setAlignmentX(0.5f);
			submit.setAlignmentY(0.0f);
			this.add(submit);

			home = new JButton("<html><u>" + "Back home" + "</u></html>");
			home.setBorderPainted(false);
			home.setForeground(Color.DARK_GRAY);
			home.addActionListener(this);
			home.setAlignmentX(0.5f);
			home.setAlignmentY(0.0f);
			this.add(home);

			window.add(this);
		}

		public void actionPerformed(ActionEvent e) {

			if (box1.getSelectedIndex() == 0) {
				box2.setModel(new JComboBox<>(SEARCH_ATTRIBUTES[0]).getModel());
			} else if (box1.getSelectedIndex() == 1) {
				box2.setModel(new JComboBox<>(SEARCH_ATTRIBUTES[1]).getModel());
			} else if (box1.getSelectedIndex() == 2) {
				box2.setModel(new JComboBox<>(SEARCH_ATTRIBUTES[2]).getModel());
			}

			if (e.getSource() == submit) {
				try {
					if (box1.getSelectedIndex() == 0) {
						String user_Input = getUserInput(SEARCH_ATTRIBUTES[box1
								.getSelectedIndex()][box2.getSelectedIndex()]
								.toLowerCase());

						switch (box2.getSelectedIndex()) {
						case 0:
							displayResults(sqlConnector
									.searchMovieByCharacter(user_Input));
							break;
						case 1:
							displayResults(sqlConnector
									.searchMovieByDirector(user_Input));
							break;
						case 2:
							displayResults(sqlConnector
									.searchMovieByActor(user_Input));
							break;
						case 3:
							displayResults(sqlConnector
									.moviesByTimeline(user_Input));
							break;
						}
					} else if (box1.getSelectedIndex() == 1) {
						final ArrayList<String> list = new ArrayList<String>();
						list.add(sqlConnector.characterInMostMovies());
						displayResults(list);
					} else if (box1.getSelectedIndex() == 2) {
						//
					}
				} catch (SQLException sqle) {
					System.err.println(sqle.getMessage());
				}
			} else if (e.getSource() == home) {
				window.replacePanel(window.home);
			}
		}
	}

	private class ListPanel extends JPanel implements ActionListener {

		private Window window;
		private JLabel label, title;
		private JComboBox<String> box1, box2;
		private JButton submit, home;
		public final String[] OPTIONS = { "Movies", "Characters", "Series",
				"Abilities" };
		public final String[][] SEARCH_ATTRIBUTES = { { "Release date" },
				{ "Without super power", "All of them" }, { "All of them" },
				{ "By character" } };

		public ListPanel(Window win) {

			this.window = win;
			this.setBounds(100, 300, 300, 250);
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			title = new JLabel("What would you like to list ? ");
			title.setAlignmentX(0.5f);
			title.setForeground(Color.BLUE);
			title.setFont(new Font("Century", Font.ITALIC, 15));
			this.add(title);

			box1 = new JComboBox<String>(OPTIONS);
			// box1.setSelectedIndex(0);
			box1.addActionListener(this);
			box1.setAlignmentY(0.0f);
			this.add(box1);

			label = new JLabel("Specify a condition: ");
			label.setAlignmentX(0.5f);
			// label.setAlignmentY(1.0f);
			label.setForeground(Color.BLUE);
			label.setFont(new Font("Century", Font.ITALIC, 15));
			this.add(label);

			box2 = new JComboBox<String>(
					SEARCH_ATTRIBUTES[box1.getSelectedIndex()]);
			// box2.setSelectedIndex(0);
			box2.addActionListener(this);
			box2.setAlignmentY(0.0f);
			this.add(box2);

			submit = new JButton("Submit");
			submit.addActionListener(this);
			submit.setAlignmentX(0.5f);
			submit.setAlignmentY(0.0f);
			this.add(submit);

			home = new JButton("<html><u>" + "Back home" + "</u></html>");
			home.setBorderPainted(false);
			home.setForeground(Color.DARK_GRAY);
			home.addActionListener(this);
			home.setAlignmentX(0.5f);
			home.setAlignmentY(0.0f);
			this.add(home);

			window.add(this);
		}

		public void actionPerformed(ActionEvent e) {

			if (box1.getSelectedIndex() == 0) {
				box2.setModel(new JComboBox<>(SEARCH_ATTRIBUTES[0]).getModel());
			} else if (box1.getSelectedIndex() == 1) {
				box2.setModel(new JComboBox<>(SEARCH_ATTRIBUTES[1]).getModel());
			} else if (box1.getSelectedIndex() == 2) {
				box2.setModel(new JComboBox<>(SEARCH_ATTRIBUTES[2]).getModel());
			} else if (box1.getSelectedIndex() == 3) {
				box2.setModel(new JComboBox<>(SEARCH_ATTRIBUTES[3]).getModel());
			}

			if (e.getSource() == submit) {
				try {
					if (box1.getSelectedIndex() == 0) {
						displayResults(sqlConnector.moviesByReleaseDate());
					} else if (box1.getSelectedIndex() == 1) {
						if (box2.getSelectedIndex() == 0) {
							displayResults(sqlConnector
									.listSuperheroesWithNoPower());
						} else if (box2.getSelectedIndex() == 1) {
							displayResults(sqlConnector.listCharacters());
						}
					} else if (box1.getSelectedIndex() == 2) {
						displayResults(sqlConnector.listSeries());
					} else if (box1.getSelectedIndex() == 3) {
						String user_Input = getUserInput(SEARCH_ATTRIBUTES[box1
								.getSelectedIndex()][box2.getSelectedIndex()]
								.toLowerCase());
						displayResults(sqlConnector
								.listAbilitiesOfCharacter(user_Input));
					}
				} catch (SQLException sqle) {
					System.err.println(sqle.getMessage());
				}
			} else if (e.getSource() == home) {
				window.replacePanel(window.home);
			}
		}
	}

	private class UpdatePanel extends JPanel implements ActionListener {

		private Window window;
		private JLabel title;
		private JComboBox<String> box1, box2;
		private JButton submit, home;
		public final String[] OPTIONS = { "Ability" };

		public UpdatePanel(Window win) {

			this.window = win;
			this.setBounds(100, 300, 300, 250);
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			title = new JLabel("What would you like to update ? ");
			title.setAlignmentX(0.5f);
			title.setAlignmentY(1.0f);
			title.setForeground(Color.BLUE);
			title.setFont(new Font("Century", Font.ITALIC, 18));
			this.add(title);

			box1 = new JComboBox<String>(OPTIONS);
			// box1.setSelectedIndex(0);
			box1.addActionListener(this);
			box1.setAlignmentY(0.0f);
			this.add(box1);

			submit = new JButton("Submit");
			submit.addActionListener(this);
			submit.setAlignmentX(0.5f);
			submit.setAlignmentY(0.0f);
			this.add(submit);

			home = new JButton("<html><u>" + "Back home" + "</u></html>");
			home.setBorderPainted(false);
			home.setForeground(Color.DARK_GRAY);
			home.addActionListener(this);
			home.setAlignmentX(0.5f);
			home.setAlignmentY(0.0f);
			this.add(home);

			window.add(this);
		}

		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == submit) {
				try {
					String old_Value = getUserInput("current "
							+ OPTIONS[box1.getSelectedIndex()].toLowerCase());
					String new_Value = getUserInput("new "
							+ OPTIONS[box1.getSelectedIndex()].toLowerCase());
					sqlConnector.renameAnAbility(old_Value, new_Value);
					JOptionPane.showMessageDialog(this,
							"Your update has been saved.");
				} catch (SQLException sqle) {
					System.err.println(sqle.getMessage());
				}
			} else if (e.getSource() == home) {
				window.replacePanel(window.home);
			}
		}
	}

	private class DeletePanel extends JPanel implements ActionListener {

		private Window window;
		private JLabel title, label;
		private JComboBox<String> box1, box2;
		private JButton submit, home;
		public final String[] OPTIONS = { "Add value into database",
				"Delete value from database" };
		public final String[][] SEARCH_ATTRIBUTES = {
				{ "New ability to character" }, { "Ability from character" } };

		public DeletePanel(Window win) {

			this.window = win;
			this.setBounds(100, 300, 300, 250);
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			title = new JLabel("How would you like to modify database ? ");
			title.setAlignmentX(0.5f);
			title.setAlignmentY(1.0f);
			title.setForeground(Color.BLUE);
			title.setFont(new Font("Century", Font.ITALIC, 13));
			this.add(title);

			box1 = new JComboBox<String>(OPTIONS);
			// box1.setSelectedIndex(0);
			box1.addActionListener(this);
			box1.setAlignmentY(0.0f);
			this.add(box1);

			label = new JLabel("Specify an objective: ");
			label.setAlignmentX(0.5f);
			// label.setAlignmentY(1.0f);
			label.setForeground(Color.BLUE);
			label.setFont(new Font("Century", Font.ITALIC, 13));
			this.add(label);

			box2 = new JComboBox<String>(
					SEARCH_ATTRIBUTES[box1.getSelectedIndex()]);
			// box2.setSelectedIndex(0);
			box2.addActionListener(this);
			box2.setAlignmentY(0.0f);
			this.add(box2);

			submit = new JButton("Submit");
			submit.addActionListener(this);
			submit.setAlignmentX(0.5f);
			submit.setAlignmentY(0.0f);
			this.add(submit);

			home = new JButton("<html><u>" + "Back home" + "</u></html>");
			home.setBorderPainted(false);
			home.setForeground(Color.DARK_GRAY);
			home.addActionListener(this);
			home.setAlignmentX(0.5f);
			home.setAlignmentY(0.0f);
			this.add(home);

			window.add(this);
		}

		public void actionPerformed(ActionEvent e) {

			if (box1.getSelectedIndex() == 0) {
				box2.setModel(new JComboBox<>(SEARCH_ATTRIBUTES[0]).getModel());
			} else if (box1.getSelectedIndex() == 1) {
				box2.setModel(new JComboBox<>(SEARCH_ATTRIBUTES[1]).getModel());
			}

			if (e.getSource() == submit) {
				try {
					String subject = getUserInput("value to be added/deleted");
					String object = getUserInput("target value.");
					if (box1.getSelectedIndex() == 0) {
						sqlConnector.userInsertIntoAbility(subject, object);
					} else if (box1.getSelectedIndex() == 1) {
						sqlConnector.userDeleteAbility(subject, object);
					}
					JOptionPane.showMessageDialog(this,
							"Your update has been saved.");
				} catch (SQLException sqle) {
					System.err.println(sqle.getMessage());
				}
			} else if (e.getSource() == home) {
				window.replacePanel(window.home);
			}
		}
	}
}
