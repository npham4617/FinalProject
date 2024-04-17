package LibrarySystem;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Profile extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private static Connection conn = null;
	
	private static JTextArea DesbtextArea;
	private static JTextArea PreferencestextArea;
	private static JTextArea readhabittextArea;
	private static JTextArea favbooktextArea;
	private static JTextField LocationtextField;
	private static JTextField AgetextField;
	private static JTextField NametextField;
	private static JComboBox<String> GendercomboBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Profile frame = new Profile(0L); // Pass a default value for demonstration
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void performDatabaseOperation(Long userid) {
		String query;
		PreparedStatement pst;
		try {	
			query = "select * from User join Profile on Profile.ID_User = User.ID_User where User.ID_User=?";
			pst = conn.prepareStatement(query);
			pst.setLong(1, userid); 
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				if (rs.getString("Bio")== null) {
					DesbtextArea.setText("");
				} else {
					DesbtextArea.setText(rs.getString("Bio"));
				}
				
				if (rs.getString("LiteraryPreferences")== null) {
					PreferencestextArea.setText("");
				} else {
					PreferencestextArea.setText(rs.getString("LiteraryPreferences"));
				}
				
				if (rs.getString("ReadingHabits")== null) {
					readhabittextArea.setText("");
				} else {
					readhabittextArea.setText(rs.getString("ReadingHabits"));
				}
				
				if (rs.getString("FavoriteBooks")== null) {
					favbooktextArea.setText("");
				} else {
					favbooktextArea.setText(rs.getString("FavoriteBooks"));
				}
				
				if (rs.getString("Location")== null) {
					LocationtextField.setText("");
				} else {
					LocationtextField.setText(rs.getString("Location"));
				}
				
				if (rs.getString("Age")== null) {
					AgetextField.setText("");
				} else {
					AgetextField.setText(String.valueOf(rs.getLong("Age")));
				}
				
				if (rs.getString("Name")== null) {
					NametextField.setText("");
				} else {
					NametextField.setText(rs.getString("Name"));
				}
				if (rs.getString("Gender")== null) {
					GendercomboBox.setSelectedItem("None");
				} else {
					for (int i = 0; i < GendercomboBox.getItemCount(); i ++ ) {
						if(GendercomboBox.getItemAt(i).toString().equalsIgnoreCase(rs.getString("Gender"))){
							GendercomboBox.setSelectedIndex(i);
							break;
						}
					}
				}
			}
			pst.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the frame.
	 */
	public Profile(Long userid) {
		conn = SqliteConnect.connect();


		setResizable(false);		
		setTitle("Library System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 601, 695);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitleWindowLabel = new JLabel("USER PROFILE");
		lblTitleWindowLabel.setFont(new Font("Rockwell", Font.PLAIN, 25));
		lblTitleWindowLabel.setBounds(210, 11, 166, 30);
		contentPane.add(lblTitleWindowLabel);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(120, 71, 46, 23);
		contentPane.add(lblName);
		
		NametextField = new JTextField();
		NametextField.setBounds(186, 71, 276, 23);
		contentPane.add(NametextField);
		NametextField.setColumns(10);
		
		JLabel lblAge = new JLabel("Age");
		lblAge.setBounds(120, 105, 46, 23);
		contentPane.add(lblAge);
		
		AgetextField = new JTextField("");
		AgetextField.setBounds(186, 105, 75, 23);
		contentPane.add(AgetextField);
		AgetextField.setColumns(10);
		
		JLabel lblGender = new JLabel("Gender");
		lblGender.setBounds(302, 105, 54, 23);
		contentPane.add(lblGender);
		
		GendercomboBox = new JComboBox<String>();
		GendercomboBox.setBounds(364, 105, 98, 22);
		GendercomboBox.setFont(new Font("Verdana", Font.PLAIN, 11));
		GendercomboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Female", "Male", "None"}));
		contentPane.add(GendercomboBox);
		
		
		JLabel lblLocation = new JLabel("Location");
		lblLocation.setBounds(120, 139, 76, 23);
		contentPane.add(lblLocation);
		
		LocationtextField = new JTextField("");
		LocationtextField.setBounds(186, 139, 276, 23);
		contentPane.add(LocationtextField);
		LocationtextField.setColumns(10);
		
		JLabel lblfavbook = new JLabel("Favorite Books");
		lblfavbook.setBounds(120, 177, 85, 23);
		contentPane.add(lblfavbook);
		
		JScrollPane favbookscrollPane = new JScrollPane();
		favbookscrollPane.setBounds(224, 177, 238, 74);
		contentPane.add(favbookscrollPane);
		
		favbooktextArea = new JTextArea("");
		favbooktextArea.setLineWrap(true);
		favbookscrollPane.setViewportView(favbooktextArea);
		
		JLabel lblhabit = new JLabel("Reading Habits");
		lblhabit.setBounds(120, 271, 85, 23);
		contentPane.add(lblhabit);
		
		JScrollPane readhabitscrollPane = new JScrollPane();
		readhabitscrollPane.setBounds(224, 271, 238, 74);
		contentPane.add(readhabitscrollPane);
		
		readhabittextArea = new JTextArea("");
		readhabittextArea.setLineWrap(true);
		readhabitscrollPane.setViewportView(readhabittextArea);
		
		JLabel lblPreferences = new JLabel("Preferences");
		lblPreferences.setBounds(120, 367, 85, 23);
		contentPane.add(lblPreferences);
		
		
		JScrollPane scrollPanePreferences = new JScrollPane();
		scrollPanePreferences.setBounds(224, 367, 238, 74);
		contentPane.add(scrollPanePreferences);
		
		PreferencestextArea = new JTextArea("");
		PreferencestextArea.setLineWrap(true);
		scrollPanePreferences.setViewportView(PreferencestextArea);
		
	
		JLabel lbldescription = new JLabel("Short description");
		lbldescription.setBounds(120, 462, 98, 23);
		contentPane.add(lbldescription);
		
		JScrollPane DesbscrollPane = new JScrollPane();
		DesbscrollPane.setBounds(224, 462, 238, 127);
		contentPane.add(DesbscrollPane);
		
		DesbtextArea = new JTextArea("");
		DesbtextArea.setLineWrap(true);
		DesbscrollPane.setViewportView(DesbtextArea);

		
		JButton btnSaveButton = new JButton("SAVE");
		btnSaveButton.addActionListener(new ActionListener() {
			@SuppressWarnings("resource")
			public void actionPerformed(ActionEvent e) {
				String query;
				PreparedStatement pst;
				ResultSet rs;
				String GenderItem = GendercomboBox.getSelectedItem().toString();
				Random rand = new Random();
				int ran_id = rand.nextInt(100);
				try {
					query = "update User set Age = ?, Location = ?, Gender = ? where ID_User = ?";
					pst = conn.prepareStatement(query);
					pst.setString(1, AgetextField.getText());
					pst.setString(2, LocationtextField.getText());
					pst.setString(3, GenderItem);
					pst.setLong(4, userid);
					pst.execute();
					pst.close();
					
					query = "select * from Profile where ID_User = ?";
					pst = conn.prepareStatement(query);
					pst.setLong(1, userid);
					rs = pst.executeQuery();
					int count =0;
					while(rs.next()) {
						count=count+1;
					}
					if(count==0) {
						query = "insert into Profile (ID, ID_User, FavoriteBooks, ReadingHabits, LiteraryPreferences, Bio, Shared) values (?, ?, ?, ?, ?, ?, ?)";
						pst = conn.prepareStatement(query);
						pst.setInt(1, ran_id);
						pst.setLong(2, userid);
						pst.setString(3, favbooktextArea.getText());
						pst.setString(4, readhabittextArea.getText());
						pst.setString(5, PreferencestextArea.getText());
						pst.setString(6, DesbtextArea.getText());
						pst.setString(7, "unshared");
						pst.execute();
						pst.close();
						JOptionPane.showMessageDialog(null, "Your profile is updated successfully", "PROFILE", JOptionPane.INFORMATION_MESSAGE);
					} else if(count==1) {
						query = "update Profile set FavoriteBooks=?, ReadingHabits=?, LiteraryPreferences=?, Bio=?, Shared=? where ID_User=?";
						pst = conn.prepareStatement(query);
						pst.setString(1, favbooktextArea.getText());
						pst.setString(2, readhabittextArea.getText());
						pst.setString(3, PreferencestextArea.getText());
						pst.setString(4, DesbtextArea.getText());
						pst.setString(5, "unshared");
						pst.setLong(6, userid);
						pst.execute();
						pst.close();
						JOptionPane.showMessageDialog(null, "Your profile is updated successfully", "PROFILE", JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btnSaveButton.setBounds(196, 615, 89, 23);
		contentPane.add(btnSaveButton);
		
		JButton btnMainButton = new JButton("MAIN MENU");
		btnMainButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String query = "select * from User where ID_User=?";
					PreparedStatement pst = conn.prepareStatement(query);
					pst.setLong(1, userid);
					ResultSet rs=pst.executeQuery();
					while(rs.next()) {
						String type = rs.getString("Type");
						if (type.equals("Faculty")) {
								AdminMenu adm = new AdminMenu(userid);
								adm.setVisible(true);
								adm.setLocationRelativeTo(null);
								dispose(); 
								
							} else if (type.equals("Student")) {
								UserMenu userm = new UserMenu(userid);
								userm.setVisible(true);
								userm.setLocationRelativeTo(null);
								dispose(); 
							}
					}
					rs.close();
					pst.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btnMainButton.setBounds(314, 615, 108, 23);
		contentPane.add(btnMainButton);
		
		performDatabaseOperation(userid);
		
		// Set Profile background
		JLabel lblImageLabel = new JLabel("");
		lblImageLabel.setBounds(0, 0, 601, 665);
		lblImageLabel.setIcon(new ImageIcon(Library.class.getResource("/Image/group.png")));
		contentPane.add(lblImageLabel);		
	}
}
