package LibrarySystem;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import javax.swing.border.EmptyBorder;

import javax.swing.table.DefaultTableModel;


import net.proteanit.sql.DbUtils;
import javax.swing.JTextArea;

public class Events extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static Connection conn = null;
	
	private JTextField EventtextField;
	private JTextField HosttextField;
	private JTextField DatetextField;
	private JTextField TimetextField;
	private JTextArea participanttextArea;
	private static JTable tableBookList;
	private JTextField LocationtextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Events frame = new Events(0L);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
    // Retrieve the list of participant IDs
    public ArrayList<String> getParticipantIDs(String participants) throws SQLException {
    	ArrayList<String> participantIDs = new ArrayList<>();
        for (String id : participants.split(",")) {
            participantIDs.add(id.trim());
        }
        return participantIDs;
    }

    // Retrieve user information from the list of IDs
    public ArrayList<User> getUsersByIDs(ArrayList<String> participantIDs) throws SQLException {
    	ArrayList<User> users = new ArrayList<>();
        if (participantIDs.isEmpty()) {
            return users;
        }

        // Construct a query to retrieve user information
        String ids = participantIDs.toString().replaceAll("[\\[\\]]", ""); // Convert to "2001, 2002, 2023"
        String[] idArray = ids.split(","); // Split by comma
        ArrayList<Long> idList = new ArrayList<>();

        // Convert each string element to long after trimming spaces
        for (String id : idArray) {
            idList.add(Long.parseLong(id.trim())); // Convert to long
        }
        String inClause = String.join(",", idList.stream().map(String::valueOf).toArray(String[]::new));
        String query = "SELECT * FROM User WHERE ID_User IN (" + inClause + ")";
        try {
        	PreparedStatement pst = conn.prepareStatement(query);
        	ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                User user = new User(
                    rs.getString("ID_User"),
                    rs.getString("Name"),
                    rs.getString("Email")
                );
                users.add(user);
    		} 
        	pst.close();
        }catch (Exception e) {
    			e.printStackTrace();
    	}
        return users;
    }
    
    public void displayUsers(ArrayList<User> users) {
    	participanttextArea.setText(""); // Clear existing content
        for (User user : users) {
        	participanttextArea.append(user.toString() + "\n"); // Display each user
        }
    }
    
    // User class to hold user information
    public static class User {
        @SuppressWarnings("unused")
		private String iduser;
        @SuppressWarnings("unused")
		private String name;
        @SuppressWarnings("unused")
		private String email;

        public User(String iduser, String name, String email) {
            this.iduser = iduser;
            this.name = name;
            this.email = email;
        }

        @Override
        public String toString() {
            return name;
        }
    }
    
	public Events(Long userid) {
		conn = SqliteConnect.connect();
		setResizable(false);
		setTitle("Library System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 672, 442);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitleWindowLabel = new JLabel("BOOK EVENTS");
		lblTitleWindowLabel.setBounds(260, 11, 248, 29);
		lblTitleWindowLabel.setFont(new Font("Rockwell", Font.PLAIN, 25));
		contentPane.add(lblTitleWindowLabel);
		
		// Show user's name
		String name = null;
		try {
			String query = "select * from User where ID_User = ?";
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setLong(1, userid);
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()) {
				name = rs.getString("Name");
			}	
			pst.close();							
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JLabel lblUserLabel = new JLabel("Hi, ");
		lblUserLabel.setBounds(287, 51, 30, 14);
		lblUserLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		contentPane.add(lblUserLabel);
		
		JLabel lblName = new JLabel("");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblName.setText(name);
		lblName.setBounds(311, 51, 93, 14);
		contentPane.add(lblName);
		
		JLabel lblISBNLabel = new JLabel("Event");
		lblISBNLabel.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblISBNLabel.setBounds(346, 96, 46, 14);
		contentPane.add(lblISBNLabel);
		
		EventtextField = new JTextField();
		EventtextField.setFont(new Font("Tahoma", Font.PLAIN, 10));
		EventtextField.setBounds(400, 93, 229, 20);
		EventtextField.setEditable(false);
		contentPane.add(EventtextField);
		EventtextField.setColumns(10);
		
		JLabel lblHostLabel = new JLabel("Host By");
		lblHostLabel.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblHostLabel.setBounds(346, 128, 46, 14);
		contentPane.add(lblHostLabel);
		
		HosttextField = new JTextField();
		HosttextField.setFont(new Font("Tahoma", Font.PLAIN, 10));
		HosttextField.setBounds(400, 125, 113, 20);
		HosttextField.setEditable(false);
		contentPane.add(HosttextField);
		HosttextField.setColumns(10);
		
		JLabel lblDateLabel = new JLabel("Date");
		lblDateLabel.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblDateLabel.setBounds(346, 163, 46, 14);
		contentPane.add(lblDateLabel);
		
		DatetextField = new JTextField();
		DatetextField.setFont(new Font("Tahoma", Font.PLAIN, 10));
		DatetextField.setBounds(400, 160, 93, 20);
		DatetextField.setEditable(false);
		contentPane.add(DatetextField);
		DatetextField.setColumns(10);
		
		JLabel lblparticipantLabel = new JLabel("Participants");
		lblparticipantLabel.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblparticipantLabel.setBounds(346, 240, 67, 14);
		contentPane.add(lblparticipantLabel);
		
	
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 114, 317, 201);
		scrollPane.setFont(new Font("Verdana", Font.PLAIN, 11));
		contentPane.add(scrollPane);
		
		tableBookList = new JTable() {
				@Override
			    public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    };
		};
		tableBookList.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		scrollPane.setViewportView(tableBookList);
		tableBookList.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Event Name", "Host By", "Type"
			}
			
			
		));
					
		// Show all events
		try {
			String query = "select  Event.EventName AS 'Event Name', User.Name AS 'Host By', Event.Type from Event join User on Event.HostBy = User.ID_User";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			tableBookList.setModel(DbUtils.resultSetToTableModel(rs));
			pst.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		tableBookList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					int row = tableBookList.getSelectedRow();
					String id = (tableBookList.getModel().getValueAt(row, 0).toString());
					String query = "select * from Event join User on Event.HostBy = User.ID_User where Event.EventName='"+id+"'";
					PreparedStatement pst = conn.prepareStatement(query);
					ResultSet rs = pst.executeQuery();

					while (rs.next()) {
						EventtextField.setText(rs.getString("EventName"));
						HosttextField.setText(rs.getString("Name"));
						DatetextField.setText(rs.getString("Date"));
						TimetextField.setText(rs.getString("Time"));
						LocationtextField.setText(rs.getString("Location"));
						ArrayList<String> participantIDs = getParticipantIDs(rs.getString("Participants"));
			            // Retrieve user information based on the IDs
			            ArrayList<User> users = getUsersByIDs(participantIDs);
			            displayUsers(users);
					}		
					pst.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		JLabel lblTimeLabel = new JLabel("Time");
		lblTimeLabel.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblTimeLabel.setBounds(513, 163, 30, 14);
		contentPane.add(lblTimeLabel);
		
		TimetextField = new JTextField();
		TimetextField.setFont(new Font("Tahoma", Font.PLAIN, 10));
		TimetextField.setBounds(542, 160, 93, 20);
		TimetextField.setEditable(false);
		contentPane.add(TimetextField);
		TimetextField.setColumns(10);
		
		JButton btnRegisterButton = new JButton("REGISTER");
		btnRegisterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PreparedStatement pst;
				String query;
				try {
					query = "Select * from Event where EventName = '"+EventtextField.getText()+"'";
					pst = conn.prepareStatement(query);
					ResultSet rs = pst.executeQuery();
					String oldparticipant = rs.getString("Participants");
					boolean exists = oldparticipant.contains(userid.toString());
					if (!exists) {
						String newparticipant = oldparticipant + "," + userid.toString();
						query = "Update Event set Participants = '"+ newparticipant +"' where EventName = '"+EventtextField.getText()+"'";
						pst = conn.prepareStatement(query);
						pst.execute();
					} else {
						JOptionPane.showMessageDialog(null, "You already exists in the participant list.", "EVENTS", JOptionPane.INFORMATION_MESSAGE);
					}
					pst.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btnRegisterButton.setBounds(380, 350, 113, 23);
		contentPane.add(btnRegisterButton);
		
		JButton btnReturnButton = new JButton("BACK TO MAIN MENU");
		btnReturnButton.addActionListener(new ActionListener() {
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
		btnReturnButton.setBounds(174, 350, 173, 23);
		contentPane.add(btnReturnButton);
		
		JScrollPane scrollPane_participant = new JScrollPane();
		scrollPane_participant.setBounds(423, 240, 212, 75);
		contentPane.add(scrollPane_participant);
		
		participanttextArea = new JTextArea();
		participanttextArea.setFont(new Font("Tahoma", Font.ITALIC, 10));
		participanttextArea.setEditable(false);
		scrollPane_participant.setViewportView(participanttextArea);
		
		JLabel lblLocationLabel = new JLabel("Location");
		lblLocationLabel.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblLocationLabel.setBounds(346, 201, 58, 14);
		contentPane.add(lblLocationLabel);
		
		LocationtextField = new JTextField();
		LocationtextField.setFont(new Font("Tahoma", Font.PLAIN, 10));
		LocationtextField.setEditable(false);
		LocationtextField.setBounds(400, 198, 235, 20);
		contentPane.add(LocationtextField);
		LocationtextField.setColumns(10);
		
		
		JLabel lblNewLabel = new JLabel("~~~~ Discover Upcoming Book Events ~~~~");
		lblNewLabel.setFont(new Font("Tahoma", Font.ITALIC, 12));
		lblNewLabel.setBounds(10, 76, 317, 42);
		contentPane.add(lblNewLabel);
		
		// Set Borrow background
		JLabel lblImageLabel = new JLabel("");
		lblImageLabel.setForeground(new Color(255, 255, 255));
		lblImageLabel.setBounds(0, 0, 672, 518);
		lblImageLabel.setIcon(new ImageIcon(Library.class.getResource("/Image/group.png")));
		contentPane.add(lblImageLabel);
	}
}
