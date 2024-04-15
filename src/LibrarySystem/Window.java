package LibrarySystem;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

public class Window extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTextArea DisplaytextArea;
	public JTextArea SendtextArea;
	public JLabel username;
	public JButton btnSendButton;
	public Window otherWindow;
	static String name1;
	static String name2;
	private JButton btnCloseButton;
	private JLabel lbltitlelabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window frame = new Window();
					frame.setUndecorated(true);
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void setOtherWindow(Window otherWindow) {
        this.otherWindow = otherWindow;
    }
	
	private void sendMessage() {
        String message = SendtextArea.getText().trim();
        if (!message.isEmpty()) {
        	DisplaytextArea.append(username.getText() + ": " + message + "\n");
        	if (otherWindow != null) {
        		otherWindow.DisplaytextArea.append(username.getText() + ": " + message + "\n");
        	}
        	SendtextArea.setText("");
        }
    }

	/**
	 * Create the frame.
	 */
	public Window() {
		setResizable(false);
		setTitle("Chat Window");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 455, 437);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		DisplaytextArea = new JTextArea();
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
		
		JLabel lblNewLabel = new JLabel("Chat window for ");
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(10, 47, 96, 23);
		contentPane.add(lblNewLabel);
		
		username = new JLabel("");
		username.setForeground(new Color(255, 255, 255));
		username.setBounds(109, 47, 125, 23);
		contentPane.add(username);
		
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
		btnCloseButton.setBackground(new Color(255, 0, 0));
		
		
		// Set Window background
		JLabel lblImageLabel = new JLabel("");
		lblImageLabel.setBounds(0, 40, 439, 359);
		lblImageLabel.setIcon(new ImageIcon(Library.class.getResource("/Image/floral-ornaments.jpg")));
		contentPane.add(lblImageLabel);
	}


}
