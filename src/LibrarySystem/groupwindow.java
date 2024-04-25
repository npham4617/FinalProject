package LibrarySystem;


import java.awt.Color;
import java.awt.EventQueue;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;


public class groupwindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTextArea SendtextArea;
	private JLabel username;
	public JButton btnSendButton;
	public Window otherWindow;
	private JButton btnCloseButton;
	private JLabel lbltitlelabel;
	private int initialX = 0;
    private int initialY = 0;
    private static Connection conn = null;
    public JTextArea DisplaytextArea;  
   
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					groupwindow frame = new groupwindow(0L);
					frame.setVisible(true);
					frame.listenForMessage();
					frame.sendMessage();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
    /**
	 * Create the frame.
	 */
	
	public void listenForMessage() {
		new Thread(() -> {
				String msgFromGroupChat = null;
				
				while (socket.isConnected()) {
					try {
						msgFromGroupChat = bufferedReader.readLine();
						final String messageToAppend = msgFromGroupChat;
						SwingUtilities.invokeLater(() -> {
		                    DisplaytextArea.append(messageToAppend + "\n"); // Ensure thread-safe UI updates
		                });
					}catch(IOException e) {
						closeEverytthing(socket, bufferedReader, bufferedWriter);
						break;
					}
				}
		}).start();
	}
	
	public void sendMessage() {
		if (socket == null || socket.isClosed()) {
	        // Ensure socket is initialized
	        try {
	            socket = new Socket("localhost", 9806);
	            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	        } catch (IOException e) {
	            closeEverytthing(socket, bufferedReader, bufferedWriter);
	            return; // Exit if there's an error initializing the socket
	        }
	    }

	    try {
	        String messageToSend = username.getText() + ": " + SendtextArea.getText().trim(); // Prepare message
	        bufferedWriter.write(messageToSend); // Write to the output stream
	        bufferedWriter.newLine(); // New line for separation
	        bufferedWriter.flush(); // Ensure the message is sent
	        
	        // Clear the input text area after sending the message
	        SendtextArea.setText("");
	    } catch (IOException e) {
	        closeEverytthing(socket, bufferedReader, bufferedWriter);
	    }
	}

	public groupwindow(long userid) {
		conn = SqliteConnect.connect();

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
		
		setResizable(false);
		setTitle("Chat Window");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 455, 437);
		contentPane = new JPanel();
		contentPane.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
                initialX = e.getX();
                initialY = e.getY();
            }
		});
		
		contentPane.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                int newX = getLocation().x + e.getX() - initialX;
                int newY = getLocation().y + e.getY() - initialY;
                setLocation(newX, newY);
            }
        });
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Chat window for ");
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(10, 47, 96, 23);
		contentPane.add(lblNewLabel);
		
		username = new JLabel("");
		username.setText(name);
		username.setForeground(new Color(255, 255, 255));
		username.setBounds(109, 47, 125, 23);
		contentPane.add(username);
		
		try {
	        socket = new Socket("localhost", 9806); // Try to connect to the server
	        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    } catch (IOException e) { // Catch connection issues
	        System.err.println("Error initializing connection: " + e.getMessage()); // Log the error
	        closeEverytthing(socket, bufferedReader, bufferedWriter); // Clean up resources
	    }
	
		DisplaytextArea = new JTextArea();
		DisplaytextArea.setEditable(false);
		DisplaytextArea.setBounds(10, 83, 419, 239);
		contentPane.add(DisplaytextArea);
		
		SendtextArea = new JTextArea();
		SendtextArea.setBounds(10, 333, 311, 51);
		contentPane.add(SendtextArea);
		
		btnSendButton = new JButton("SEND");
		btnSendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		
		btnSendButton.setBounds(333, 338, 89, 38);
		contentPane.add(btnSendButton);
		
		JButton btnClearButton = new JButton("CLEAR");
		btnClearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DisplaytextArea.setText("");
			}
		});
		btnClearButton.setBounds(340, 50, 89, 23);
		contentPane.add(btnClearButton);
		
		lbltitlelabel = new JLabel("Chat Room");
		lbltitlelabel.setBounds(10, 10, 363, 23);
		contentPane.add(lbltitlelabel);
		lbltitlelabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		btnCloseButton = new JButton("X");
		btnCloseButton.setBounds(383, 11, 46, 23);
		contentPane.add(btnCloseButton);
		btnCloseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCloseButton.setBackground(new Color(255, 128, 192));
			
		// Set Window background
		JLabel lblImageLabel = new JLabel("");
		lblImageLabel.setBounds(0, 40, 439, 359);
		lblImageLabel.setIcon(new ImageIcon(Library.class.getResource("/Image/pink.png")));
		contentPane.add(lblImageLabel);
		
		DisplaytextArea = new JTextArea();
		DisplaytextArea.setBounds(10, 81, 412, 232);
		contentPane.add(DisplaytextArea);
	}

	public void closeEverytthing(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
		try {
			if (bufferedReader!=null) {
				bufferedReader.close();
			}
			if (bufferedWriter!=null) {
				bufferedWriter.close();
			}
			if (socket!=null) {
				socket.close();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
