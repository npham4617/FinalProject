package LibrarySystem;



import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;

import java.awt.GridLayout;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.BorderFactory;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class Groupchat extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField emailtextField;
	private static Connection conn = null;
	private static JButton userDeleteButton;
	private static JPanel userpanel;
	private static JScrollPane scrollPane;
	private static List<User> userList;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Groupchat frame = new Groupchat(0L);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void addUsertoPanel(User user) {
		// Create a panel for each user
        JPanel userRowPanel = new JPanel(new GridLayout(1, 2)); // Use GridLayout with 1 row and 2 columns
        userRowPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add padding
       
        
        JLabel nameLabel = new JLabel(user.getEmail());
        
        // Create buttons panel for the "DELETE" actions
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Align buttons to the right
                
        userDeleteButton = new JButton("DELETE");
        userDeleteButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                // Get the source of the event (the delete button)
                JButton button = (JButton) e.getSource();
                
                // Get the parent of the button (the buttons panel)
                JPanel buttonsPanel = (JPanel) button.getParent();
                
                // Get the parent of the buttons panel (the userRowPanel)
                JPanel userRowPanel = (JPanel) buttonsPanel.getParent();
                
                // Get the index of the userRowPanel in the userpanel
                int index = userpanel.getComponentZOrder(userRowPanel);
                
                // Check if the user is at index 0
                if (index == 0) {
                    JOptionPane.showMessageDialog(null, "Unable to remove this user from the list.", "ADD GROUP", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Remove the userRowPanel from the userpanel
                    userpanel.remove(userRowPanel);

                    // Remove the user from the userList
                    userList.remove(index);

                    // Refresh the scroll pane
                    scrollPane.revalidate();
                    scrollPane.repaint();
                }
            }
        });
               
        buttonsPanel.add(userDeleteButton);     
                
        userRowPanel.add(nameLabel);
        userRowPanel.add(buttonsPanel);
       
        // Add labels to the panel
      	userpanel.add(userRowPanel);
	
       // Refresh the scroll pane
       scrollPane.revalidate();
       scrollPane.repaint();     
	}

	public static void addUser(String text) {
		String query;
		String email = null;
		String name = null;
		boolean userExists = false;
		PreparedStatement pst;
		try {	
			query = "select * from User where Email = ? OR Name = ?";
			pst = conn.prepareStatement(query);
			pst.setString(1, text); 
			pst.setString(2, text); 
			ResultSet rs = pst.executeQuery();
			int count = 0;
			while (rs.next()) {
				name = rs.getString("Name");
				email = rs.getString("Email");
				count = count + 1;
			}
			if (count < 1) {
				JOptionPane.showMessageDialog(null, "The user does not exist.", "ADD GROUP", JOptionPane.INFORMATION_MESSAGE);
			}
			else if (count >= 1) {   
				for (User user : userList) {
					if (user.getEmail().equals(email) && user.getName().equals(name)) {
						JOptionPane.showMessageDialog(null, "The user already exists.", "ADD GROUP", JOptionPane.INFORMATION_MESSAGE);
						userExists = true;
						break;
					}
				}	
				if (!userExists) {
					if (userList.size() < 5) {
		                userList.add(new User(name, email));
		            } else {
		                JOptionPane.showMessageDialog(null, "Maximum 5 users allowed.", "ADD GROUP", JOptionPane.INFORMATION_MESSAGE);
		            }
			
					for (User user : userList) {
					    addUsertoPanel(user);
				    }

				}
			}
			pst.close();
			rs.close();
			// Add the new users to the panel

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame.
	 */
	public Groupchat(Long userid) {
		userList = new ArrayList<>();
		conn = SqliteConnect.connect();
		setTitle("Group Chat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 485, 445);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		emailtextField = new JTextField();
		emailtextField.setBounds(51, 97, 191, 23);
		contentPane.add(emailtextField);
		emailtextField.setColumns(10);
		
		JButton btnAddButton = new JButton("ADD");
		btnAddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				userpanel.removeAll();
				addUser(emailtextField.getText());
				emailtextField.setText("");
			}
		});
		btnAddButton.setBounds(256, 97, 89, 23);
		contentPane.add(btnAddButton);
		

	/*	JButton btnjoinButton = new JButton("JOIN CHAT");
		btnjoinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Window> windows = new ArrayList<>();
		        	        
		        // Create and configure windows for each user
				int numUsers = Math.min(5, userList.size()); 
		        for (int i = 0; i < numUsers; i++) {
		            Window window = new Window();
		            window.username.setText(userList.get(i).getName());
		            window.setUndecorated(true);
		            window.setLocationRelativeTo(null);
		            windows.add(window);
		        }   
		        
		        // Set otherWindow for each window
		        for (int i = 0; i < numUsers; i++) {
		            Window currentWindow = windows.get(i);
		            for (int j = 0; j < numUsers; j++) {
		            	Window otherWindow = windows.get(j);
		                if (i != j) {
		                    currentWindow.setOtherWindow(otherWindow);
		                    otherWindow.setOtherWindow(currentWindow);
		                }
		            }
		        }
		   
		        // Make all windows visible
		        for (Window window : windows) {
		            window.setVisible(true);
		        }
		        
		        // Close the current window
		        dispose();
			}
		});
		btnjoinButton.setBounds(191, 357, 118, 23);
		contentPane.add(btnjoinButton);
*/
		JButton btnjoinButton = new JButton("JOIN CHAT");
		btnjoinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<groupwindow> windows = new ArrayList<>();
	        
		        // Create and configure windows for each user
				int numUsers = Math.min(5, userList.size()); 
		        for (int i = 0; i < numUsers; i++) {
		        	groupwindow window = new groupwindow();
		            window.username.setText(userList.get(i).getName());
		            window.setUndecorated(true);
		            window.setLocationRelativeTo(null);
		            windows.add(window);
		        }   
		        
		        // Make all windows visible
		        for (groupwindow window : windows) {
		            window.setVisible(true);
		        }
		        
		        // Close the current window
		        dispose();
			}
		});
		btnjoinButton.setBounds(191, 357, 118, 23);
		contentPane.add(btnjoinButton);
		
		
		
		
		JLabel lblTitleLabel = new JLabel("GROUP CHAT");
		lblTitleLabel.setBounds(138, 11, 207, 30);
		contentPane.add(lblTitleLabel);
		lblTitleLabel.setFont(new Font("Rockwell", Font.PLAIN, 25));
		
		JLabel lblEmailLabel = new JLabel("Enter your name or email address (maximum 5 people):");
		lblEmailLabel.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblEmailLabel.setBounds(51, 65, 316, 23);
		contentPane.add(lblEmailLabel);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(51, 131, 363, 205);
		contentPane.add(scrollPane);
		
		userpanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		userpanel.setPreferredSize(new Dimension(300, 300));
		scrollPane.setViewportView(userpanel);

		String query;
		PreparedStatement pst;

		try {	
			query = "select * from User where ID_User = ? ";
			pst = conn.prepareStatement(query);
			//pst.setLong(1, userid); 
			
			String na = "2020";
			pst.setLong(1, Long.parseLong(na)); 
			
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String name = rs.getString("Name");
				String email = rs.getString("Email");
				userList.add(new User(name, email));    
			}  
			for(User user:userList) {
				addUsertoPanel(user);
		    }
		
			pst.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Set Group background
		JLabel lblImageLabel = new JLabel("");
		lblImageLabel.setBounds(0, -94, 543, 550);
		lblImageLabel.setIcon(new ImageIcon(Library.class.getResource("/Image/pink.png")));
		contentPane.add(lblImageLabel);
		

	}
}

class User {
    private String name;
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}