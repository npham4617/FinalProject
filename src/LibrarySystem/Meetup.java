package LibrarySystem;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;



import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


import javax.swing.JScrollPane;

import javax.swing.JTextPane;


public class Meetup extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private static Connection conn = null;
	public JLabel lblName;
	public static JTextPane profiletextPanel;
	public JButton userFollowButton;
	public JButton userProfileButton;
	public JButton userChatButton;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Meetup frame = new Meetup(0L); // Pass a default value for demonstration
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void ProfileData(Long i) {
		profiletextPanel.setText(null);
        StyledDocument doc = profiletextPanel.getStyledDocument();
        
        // Define styles (colors) for each row
        MutableAttributeSet BoldStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(BoldStyle, Color.BLACK);
        StyleConstants.setBold(BoldStyle, true);
        
        MutableAttributeSet orgStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(orgStyle, Color.ORANGE);
        
        MutableAttributeSet greenStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(greenStyle, Color.green);
        
		String query;
		PreparedStatement pst;

		try {	
			query = "select * from User join `Profile` on User.ID_User = Profile.ID_User where User.ID_User = ? AND Profile.Shared='YES' ";
			pst = conn.prepareStatement(query);
			pst.setLong(1, i); 
			ResultSet rs = pst.executeQuery();
			if (!rs.isBeforeFirst()) {
				doc.insertString(doc.getLength(), "The information for this user is unavailable.", null);
		    } else if (rs.getString("Shared").equals("YES")) {
				while (rs.next()) {
					String name = rs.getString("Name");
			        String gender = rs.getString("Gender");
			        String favbook = rs.getString("FavoriteBooks");
			        String habits = rs.getString("ReadingHabits");
			        String preferences = rs.getString("LiteraryPreferences");
			        String shortdes = rs.getString("Bio");
			        // Append data to the JTextPane with the appropriate style 
			        doc.insertString(doc.getLength(), "\t" + name + " Profile\n\n", BoldStyle);
	                
					if (rs.getString("LiteraryPreferences")== null) {
						doc.insertString(doc.getLength(), "Literary Preferences: None \n", orgStyle);
					} else {
						doc.insertString(doc.getLength(), "Literary Preferences: " + preferences + "\n", orgStyle);
					}
					
					if (rs.getString("ReadingHabits")== null) {
						doc.insertString(doc.getLength(), "Reading Habits: None \n", greenStyle);
					} else {
						doc.insertString(doc.getLength(), "Reading Habits: " + habits + "\n", greenStyle);
					}
					
					if (rs.getString("FavoriteBooks")== null) {
						doc.insertString(doc.getLength(), "Favorite Books: None \n", orgStyle);
					} else {
						doc.insertString(doc.getLength(), "Favorite Books: " + favbook + "\n", orgStyle);
					}
								
					if (rs.getString("Gender")== null) {
						doc.insertString(doc.getLength(), "Gender: None \n", greenStyle);
					} else {
						doc.insertString(doc.getLength(), "Gender: " + gender + "\n", greenStyle);
					}
					
					if (rs.getString("Bio")== null) {
						doc.insertString(doc.getLength(), "Short description: \tNone \n", orgStyle);
					} else {
						doc.insertString(doc.getLength(), "Short description: " + shortdes + "\n", orgStyle);
					}
				} 

			} 
			pst.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	
	public static void FollowData(Long i) {
		profiletextPanel.setText(null);
        StyledDocument doc = profiletextPanel.getStyledDocument();
        
        // Define styles (colors) for each row
        MutableAttributeSet BoldStyle = new SimpleAttributeSet();
        StyleConstants.setBold(BoldStyle, true);
        
		String query;
		PreparedStatement pst;

		try {	
			query = "select User.Name, Book.Title, Feedback.Feedback from User join `Feedback` on User.ID_User = Feedback.ID_User join `Book` on Book.ISBN = Feedback.ISBN where Feedback.ID_User = ? ";
			pst = conn.prepareStatement(query);
			pst.setLong(1, i); 
			ResultSet rs = pst.executeQuery();
			if (!rs.isBeforeFirst()) {
				doc.insertString(doc.getLength(), "The information for this user is unavailable.", null);
		    } else {
				while (rs.next()) {
					String name = rs.getString("Name");
					String title = rs.getString("Title");
			        String feedback = rs.getString("Feedback");
			        // Append data to the JTextPane with the appropriate style            
	                doc.insertString(doc.getLength(), name, BoldStyle);
	                doc.insertString(doc.getLength(), " commented on ", null);
	                doc.insertString(doc.getLength(), title + ":\n", BoldStyle);
	                doc.insertString(doc.getLength(), feedback + "\n\n", null);
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
	
	public Meetup(Long userid) {
		conn = SqliteConnect.connect();
		setResizable(false);		
		setTitle("Library System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 677, 437);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitleWindowLabel = new JLabel("GATHERING");
		lblTitleWindowLabel.setForeground(new Color(255, 255, 255));
		lblTitleWindowLabel.setFont(new Font("Rockwell", Font.PLAIN, 25));
		lblTitleWindowLabel.setBounds(279, 11, 179, 30);
		contentPane.add(lblTitleWindowLabel);
		
		JScrollPane userscrollPane = new JScrollPane();
		userscrollPane.setBounds(23, 130, 288, 167);
		contentPane.add(userscrollPane);
		
		JPanel userpanel = new JPanel();
		userscrollPane.setViewportView(userpanel);
		userpanel.setLayout(new BoxLayout(userpanel, BoxLayout.Y_AXIS));
		
			
		JScrollPane profilescrollPane = new JScrollPane();
		profilescrollPane.setBounds(349, 130, 288, 167);
		contentPane.add(profilescrollPane);
		
		profiletextPanel = new JTextPane();
		profiletextPanel.setEditable(false);
		profilescrollPane.setViewportView(profiletextPanel);
		profiletextPanel.setBounds(349, 130, 288, 167);
		profiletextPanel.insertIcon(new ImageIcon(Library.class.getResource("/Image/textpanelmeet.png")));

		JLabel lblWelcome = new JLabel("Hi, ");
		lblWelcome.setForeground(new Color(255, 255, 255));
		lblWelcome.setBounds(301, 52, 28, 23);
		contentPane.add(lblWelcome);
		
		// Show user's name
		String username = null;
		try {
			String query = "select * from User where ID_User = ?";
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setLong(1, userid);
			ResultSet rs = pst.executeQuery();
					
			while(rs.next()) {
				username = rs.getString("Name");
			}	
			pst.close();							
		} catch (Exception e) {
				e.printStackTrace();
		}
				
		lblName = new JLabel("");
		lblName.setForeground(new Color(255, 255, 255));
		lblName.setBounds(325, 52, 144, 23);
		lblName.setText(username);
		contentPane.add(lblName);
		
		JButton btnshareProfile = new JButton("SHARE PROFILE");
		btnshareProfile.setBounds(159, 343, 125, 23);
		contentPane.add(btnshareProfile);
		
		JButton btnMainmenu = new JButton("MAIN MENU");
		btnMainmenu.addActionListener(new ActionListener() {
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
		
		btnMainmenu.setBounds(469, 343, 125, 23);
		contentPane.add(btnMainmenu);
		
		JButton btngroup = new JButton("CREATE GROUP");
		btngroup.setBounds(312, 343, 125, 23);
		contentPane.add(btngroup);
		
		JLabel lblwelcomeMessage = new JLabel("Welcome to explore our community!");
		lblwelcomeMessage.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblwelcomeMessage.setForeground(new Color(255, 255, 255));
		lblwelcomeMessage.setBounds(23, 96, 341, 23);
		contentPane.add(lblwelcomeMessage);
		
		// Show all users into the Library
		try {
			String query = "select * from User";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String name = rs.getString("name");
		        String id_user = rs.getString("ID_User");
		        
		        // Create a panel for each user
		        JPanel userRowPanel = new JPanel(new GridLayout(1, 3)); // Use GridLayout with 1 row and 2 columns
		        userRowPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add padding
		        userRowPanel.setBackground(new Color(255, 255, 102));
		        		
		        JLabel nameLabel = new JLabel(name);
		                
		        // Create buttons panel for the "CHAT" and "PROFILE" actions
		        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Align buttons to the right
		        buttonsPanel.setBackground(new Color(255, 255, 102));
		                
		        userChatButton = new JButton();
		        //Welcome to explore our community!
		        userChatButton.addActionListener(new ActionListener() {
		        	public void actionPerformed(ActionEvent e) {
		        		ChatRoom chat = new ChatRoom(userid, Long.parseLong(id_user));
						chat.setVisible(true);
						chat.setLocationRelativeTo(null);
		        	}
		        });
		                              
		       userProfileButton = new JButton();
		       userProfileButton.addActionListener(new ActionListener() {
		    	   public void actionPerformed(ActionEvent e) {
			    		// ProfileData(Long.parseLong(id_user));
			    		String er = "2024";
			        	ProfileData(Long.parseLong(er));	 
		        	}
		       });
		                
		       userFollowButton = new JButton();
		       userFollowButton.addActionListener(new ActionListener() {
		        	public void actionPerformed(ActionEvent e) {
		        		//FollowData(Long.parseLong(id_user));
		        		String er = "2023";
		        		FollowData(Long.parseLong(er));	        		
		        	}
		       });

		       int buttonWidth = 30; // Set the desired width
		       int buttonHeight = 30; // Set the desired height
		       userChatButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
		       userChatButton.setToolTipText("Start a chat with this user");
		       userProfileButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
		       userProfileButton.setToolTipText("View profile of this user");
		       userFollowButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
		       userFollowButton.setToolTipText("Click to follow this user");
		                
		       ImageIcon chatIcon = new ImageIcon(getClass().getResource("/Image/chat.png"));
		       ImageIcon profileIcon = new ImageIcon(getClass().getResource("/Image/profile.png"));
		       ImageIcon followIcon = new ImageIcon(getClass().getResource("/Image/follow.png"));
		       userChatButton.setIcon(chatIcon);
		       userProfileButton.setIcon(profileIcon);
		       userFollowButton.setIcon(followIcon);
		                
		       buttonsPanel.add(userChatButton);
		       buttonsPanel.add(userProfileButton);
		       buttonsPanel.add(userFollowButton);
		                
		                
		       userRowPanel.add(nameLabel);
		       userRowPanel.add(buttonsPanel);
		                
		       // Add labels to the panel
		       userpanel.add(userRowPanel);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		// Set Meetup background
		JLabel lblImageLabel = new JLabel("");
		lblImageLabel.setForeground(new Color(255, 255, 255));
		lblImageLabel.setBounds(0, 0, 721, 448);
		lblImageLabel.setIcon(new ImageIcon(Library.class.getResource("/Image/blue.jpg")));
		contentPane.add(lblImageLabel);
	
	}
}



