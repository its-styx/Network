/*
	Christopher Pena
	April 21, 2025
	Purpose: Send matrices from client to server
	Sources:
			https://www.geeksforgeeks.org/java-util-timer-class-java/ for java.util.Timer
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
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Client
{
	//Instantiate GUI and Timer Variables
	private JTextField fileName;
	private JTextArea results;
	private JButton sendButton;
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Timer connectionTimer;
	
	//Client Object
	public Client()
	{
		//GUI
		JFrame frame = new JFrame("Client");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500,400);
		
		fileName = new JTextField(20);
		results = new JTextArea();
		results.setEditable(false);
		sendButton = new JButton("Send");
		sendButton.setEnabled(false);
		
		JPanel panel = new JPanel();
		panel.add(new JLabel("Enter file name: "));
		panel.add(fileName);
		panel.add(sendButton);
		
		frame.add(panel, BorderLayout.NORTH);
		frame.add(new JScrollPane(results), BorderLayout.CENTER);
		
		sendButton.addActionListener(e -> {
			handleFile();
			try
			{
				out.writeObject("TERMINATE");
				out.close();
				in.close();
				socket.close();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		});
		frame.setVisible(true);
		
		//Attempt Connection
		attemptConnection();
	}
	
	private void attemptConnection()
	{
		//Creates a timer
		connectionTimer = new Timer();
		connectionTimer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				//Try/Catch for server
				try
				{
					socket = new Socket("localhost", 6969);
					out = new ObjectOutputStream(socket.getOutputStream());
					out.flush();
					in = new ObjectInputStream(socket.getInputStream());
					results.append("Connected to server\n");
					sendButton.setEnabled(true);
					connectionTimer.cancel();
				}
				catch (IOException e)
				{
					e.printStackTrace();
					results.append("Failed to connect to server. Retrying in 5 seconds.\n");
				}
			}
		}, 0, 5000); //5 second timer
	}
	
	private void handleFile()
	{
		// Handles file input and sends over to server
		String filename = fileName.getText();
		results.setText("");
		
		try (Scanner scanner = new Scanner(new File(filename)))
		{
			int rows = scanner.nextInt();
			int cols = scanner.nextInt();
			
			int[][] matrix1 = new int[rows][cols];
			int[][] matrix2 = new int[rows][cols];
			
			for (int i = 0; i < rows; i++)
			{
				for (int j = 0; j < cols; j++)
				{
					matrix1[i][j] = scanner.nextInt();
				}
			}
			
			for (int i = 0; i < rows; i++)
			{
				for (int j = 0; j < cols; j++)
				{
					matrix2[i][j] = scanner.nextInt();
				}
			}
			
			out.writeObject(matrix1);
			out.writeObject(matrix2);
			out.flush();
			results.append("Matrices sent\n");
			
			int[][] resultMatrix = (int[][]) in.readObject();
			results.append("Resulting Matrix:\n");
			for (int[] row : resultMatrix)
			{
				for (int val : row)
				{
					results.append(val + " ");
				}
				results.append("\n");
			}
			out.flush();
		}
		catch (IOException | NoSuchElementException | ClassNotFoundException e)
		{
			results.append("Error: " + e.getMessage() + "\n");
		}
	}
}