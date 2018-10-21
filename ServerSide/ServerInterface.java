
/*
 * Copyright (c) 1995, 2014, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.io.*;

public class ServerInterface {
	List<FrameObject> currentFrames;
	Scanner scn;
	String inputLine, outputLine, adminLine;
	ServerSocket serverSocket;

	public ServerInterface() {
		currentFrames = new ArrayList<>();
	}

	public void startServer(int port) throws IOException {

		serverSocket = new ServerSocket(port);
		// Socket clientSocket = serverSocket.accept();
		Timer refresh = new Timer();
		refresh.schedule(checkFrames, 5, 10);
		// refresh.schedule(checkNewUser, 10, 10);
		for (int i = 0; i < 10; i++) { // Creates 5 threads that wait for new server.
			makeUserThread(i);
		}
		// scn = new Scanner(System.in);
		// Initiate conversation with client
		// ServerProtocol kkp = new ServerProtocol();
		// outputLine = kkp.processInput(null);
		// out.println(outputLine);

	}

	void makeUserThread(int id) {
		Thread thread = new Thread(id + "") {

			public void run() {
				try {
					Socket clientSocket;
					clientSocket = serverSocket.accept();
					System.out.println("Connected");
					setName(clientSocket.getLocalAddress().getHostName());
					BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

					Timer checker = new Timer();
					TimerTask checkInput = new TimerTask() {

						@Override
						public void run() {
							// System.out.println(getName());
							// TODO Auto-generated method stub
							try {
								adminLine = in.readLine();
								if (adminLine != null) {
									System.out.println(adminLine);
									String[] data = adminLine.split(" ");
									if (adminLine.equals("Break")) {
										cancel();
									} else if (adminLine.contains("Add")) {
										if (data.length == 4)
											try {
												addFrame(data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]));
												System.out.println("Added");
											} catch (Exception e) {
												System.out.println("BAD");
											}

									} else if (data[0].equals("Upvote")) {
										if (data.length == 3) {
											upvoteFrame(getName(),Integer.parseInt(data[1]), Integer.parseInt(data[2]));
											//code to register upvote by user = getName();
										}
									} else if (data[0].equals("Verify")) {
										if(verify(data[1],data[2])==0) {
											out.println("Success");
											setName(data[1]);
										}else {
											out.println("Failure");
										}
									} else if (data[0].equals("Create")) {
										if(!create(data[1], data[2], data[3], data[4].equals("0"))) {
											out.println("Bad Incorrect Attempt. Try again with better shit.");
										}
										else {
											out.println("");
										}
									}
									else {
										System.out.println("NOT RUNNING");
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							// outputLine = kkp.processInput(inputLine);
							// out.println(outputLine); //talk to output
							// if (outputLine.equals("Bye."))
							// break;
						}
					};
					checker.schedule(checkInput, 0, 10);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		};
		thread.start();
	}


	TimerTask checkFrames = new TimerTask() {

		@Override
		public void run() {
			for (int i = 0; i < currentFrames.size(); i++) {
				FrameObject currentFrame = currentFrames.get(i);
				if (currentFrame.refresh()) {
					if (currentFrame.isFunded())
						System.out.println(currentFrames.get(i).name + " was successfully funded by: " +currentFrames.remove(i).toString());
					else
						System.out.println("Removed " + currentFrames.remove(i).name);
				}
			}

		}

	};

	public void addFrame(String name, int lifeSpan, int targetVibes) {
		currentFrames.add(new FrameObject(name, System.currentTimeMillis(), lifeSpan, targetVibes));
	}

	public void upvoteFrame(String user, int index, int vibes) {
		currentFrames.get(index).addVibes(user, vibes);
	}

	public static void main(String[] args) throws IOException {
		ServerInterface si = new ServerInterface();
		int portNumber;
		if (args.length != 1) {
			portNumber = 1; // Set to something official
		} else
			portNumber = Integer.parseInt(args[0]);
		si.startServer(portNumber);
	}

	private static int verify(String username, String password) throws SQLException {
		Connection con = DriverManager.getConnection(
				"jdbc:mysql://invendor-database.mysql.database.azure.com:3306/data_schema?serverTimezone=UTC",
				"invendoradmin@invendor-database", "UTDhack2018");
		Statement stmt = con.createStatement();

		ResultSet result = stmt
				.executeQuery("SELECT password_hash FROM data_schema.user where userName = '" + username + "'");
		int returnVal = 1;
		result.next();
		try {
			long tempPassword = result.getLong(1);
			con.close();
			return (int) (Math.abs(hashPassword(password) - tempPassword));
		} catch (SQLException e) {
			con.close();
			return -1;
		}
	}

	private static long hashPassword(String password) {
		// replace with more meaningful shit
		return (long) (password.hashCode());
	}
	
	private static boolean create(String username, String email, String password, boolean inventor) throws SQLException
	{
		Connection con = DriverManager.getConnection("jdbc:mysql://invendor-database.mysql.database.azure.com:3306/data_schema?serverTimezone=UTC", "invendoradmin@invendor-database", "UTDhack2018");
		Statement stmt = con.createStatement();
		long hashPassword = hashPassword(password);
		try {
			stmt.execute("INSERT INTO user values ('" + username + "', '" + email + "', '" + hashPassword + "')");
			stmt.execute("INSERT INTO " + (inventor ? "inventor" : "peer") + " values ('" + username + "')");
			return true;
		}
		catch (SQLException e) {
			return false;
		}
		
	}
	
	private String getName()
	{
		
		return "";
	}
}
