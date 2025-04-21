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
		try
		{
			
		}
		catch
		{
			
		}
	}
	
	private void printMatrix(int[][] matrix)
	{
		for (int[] row : matrix)
		{
			for (int value : row)
			{
				System.out.print(val + " ");
			}
			System.out.println();
		}
	}
}