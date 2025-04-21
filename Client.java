/*
	Christopher Pena
	April 21, 2025
	Purpose:
	Sources:
*/

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
	private JTextField fileName;
	private JTextArea results;
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public Client()
	{
		JFrame frame = new JFrame("Client");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500,400);
		
		fileName = new JTextField(20);
		results = new JTextArea();
		results.setEditable(false);
		JButton sendButton = new JButton("Send");
		
		JPanel panel = new JPanel();
		panel.add(new JLabel("Enter file name: "));
		panel.add(fileName);
		panel.add(sendButton);
		
		frame.add(panel, BorderLayout.NORTH);
		frame.add(new JScrollPane(results), BorderLayout.CENTER);
		
		sendButton.addActionListener(e -> handleFile());
		frame.setVisible(true);
		
		//Try/Catch for server
		try
		{
			socket = new Socket("localhost", 6969);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void handleFile()
	{
		String filename = fileName.getText();
		results.setText("");
		
		try
		{
			
		}
		catch
		{
			
		}

	}
}