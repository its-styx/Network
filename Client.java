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
		
		
		//Try/Catch for server
		try
		{
			socket = new Socket("localhost", 6969);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStrean());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}