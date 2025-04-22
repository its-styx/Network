/*
	Christopher Pena
	April 21, 2025
	Purpose: Recieve matrices from client to server
	Sources:
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Server
{
	private volatile boolean running = true; //Volatile runnability
	private ServerSocket serverSocket;
	private final ExecutorService executor = Executors.newFixedThreadPool(4);
	
	public Server()
	{
		Thread serverThread = new Thread(this::runServer);
		serverThread.start();
		
		//To close the server without client sending the file
		try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in)))
		{
			String command;
			while ((command = console.readLine()) != null)
			{
				if (command.equalsIgnoreCase("close"))
				{
					System.out.println("Server shutting down...");
					running = false;
					if (serverSocket != null && !serverSocket.isClosed())
					{
						serverSocket.close();
					}
					System.exit(0);
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void runServer()
	{
		try (ServerSocket serverSocket = new ServerSocket(6969))
		{
			System.out.println("Server started. Waiting for client...");
			this.serverSocket = serverSocket;
			
			while(running)
			{
				Socket clientSocket = null;
				try
				{
					// Just learned about lambda referencing exceptions. This sucked to figure out
					clientSocket = serverSocket.accept();
					final Socket socketThread = clientSocket;
					new Thread(() -> handleClient(socketThread)).start();
				}
				catch (IOException e)
				{
					if (running)
					{
						e.printStackTrace();
					}
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (serverSocket != null && !serverSocket.isClosed())
				{
					serverSocket.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private void handleClient(Socket socket)
	{
		try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream()))
		{
			//Had to place this here to fix bit corruption
			out.flush();
			while(true)
			{
				Object obj = in.readObject();
				if (obj instanceof String && obj.equals("TERMINATE"))
				{
					//Exits after addition and send back
					System.out.println("Client terminated connection");
					System.exit(0);
				}
				else if (obj instanceof int[][])
				{
					int[][] matrix1 = (int[][]) obj;
					int[][] matrix2 = (int[][]) in.readObject();
					
					System.out.println("Recieved matrix1: ");
					printMatrix(matrix1);
					System.out.println("Recieved matrix2: ");
					printMatrix(matrix2);
					
					
					int[][] resultMatrix = sumMatrix(matrix1, matrix2);
					out.writeObject(resultMatrix);
					out.flush();
				}
			}
		}
		catch (IOException | ClassNotFoundException e)
		{
			System.out.println("Client disconnected");
		}
	}
	
	private int[][] sumMatrix(int[][] matrix1, int[][] matrix2)
	{
		int rows = matrix1.length;
		int cols = matrix1[0].length;
		int[][] result = new int[rows][cols];
		
		//Learned about future from a handful of places. Definitely very useful
		List<Future<Void>> futures = new ArrayList<>();
		
		for (int i = 0; i < 2; i++)
		{
			for (int j = 0; j < 2; j++)
			{
				final int startRow = i * (rows/2);
				final int endRow = (i + 1) * (rows/2);
				final int startCol = j * (cols/2);
				final int endCol = (j + 1) * (cols/2);
				
				futures.add(executor.submit(() -> {
					for (int r = startRow; r < endRow; r++)
					{
						for (int c = startCol; c < endCol; c++)
						{
							result[r][c] = matrix1[r][c] + matrix2[r][c];
						}
					}
					return null;
				}));
			}
		}
		
		futures.forEach(future -> {
			try
			{
				future.get();
			}
			catch (Exception e)
			{
				throw new RuntimeException("Error during addition",e);
			}
		});
		
		return result;
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