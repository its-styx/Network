/*
	Christopher Pena
	April 21, 2025
	Purpose:
	Sources:
*/

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
	public Server()
	{
		try (ServerSocket serverSocket = new ServerSocket(6969))
		{
			System.out.println("Server started. Waiting for client...");
			
			while(true)
			{
				Socket clientSocket = serverSocket.accept();
				new Thread(() -> handleClient(clientSocket)).start();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void handleClient(Socket socket)
	{
		try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream()))
		{
			while(true)
			{
				Object obj = in.readObject();
				if (obj instanceof String && obj.equals("TERMINATE"))
				{
					System.out.println("Client terminated connection");
					break;
				}
				else if (obj instanceof int[][])
				{
					int[][] matrix1 = (int[][]) obj;
					int[][] matrix2 = (int[][]) in.readObject();
					
					System.out.println("Recieved matrix1: ");
					printMatrix(matrix1);
					System.out.println("Recieved matrix2: ");
					printMatrix(matrix2);
				}
			}
		}
		catch (IOException | ClassNotFoundException e)
		{
			System.out.println("Client disconnected");
		}
	}
	
	private void printMatrix(int[][] matrix)
	{
		for (int[] row : matrix)
		{
			for (int val : row)
			{
				System.out.print(val + " ");
			}
			System.out.println();
		}
	}
}