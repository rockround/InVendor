package ServerSide;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.io.*;

public class ServerInterface {
	List<FrameObject> currentFrames;
	PrintWriter out;
	BufferedReader in;
	Scanner scn;
	String inputLine, outputLine, adminLine;
	public ServerInterface() {
		currentFrames = new ArrayList<>();
		Timer refresh = new Timer();
		Timer clock1 = new Timer();
		refresh.schedule(checkInput, 0,10);
		clock1.schedule(checkFrames, 1,10);
	}
	public void startServer(int port) throws IOException{

		ServerSocket serverSocket = new ServerSocket(port);
		Socket clientSocket = serverSocket.accept();
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		scn = new Scanner(System.in);
			// Initiate conversation with client
			//ServerProtocol kkp = new ServerProtocol();
			//outputLine = kkp.processInput(null);
			//out.println(outputLine);
			
	}
	TimerTask checkInput = new TimerTask(){
		

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
			adminLine = scn.nextLine();
			if (adminLine != null) {
				if (adminLine.equals("Break"))
					cancel();
				else if (adminLine.contains("Add")) {
					String[] data = adminLine.split(" ");
					if (data.length == 4)
						try {
							addFrame(data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]));
							System.out.println("Added");
						} catch (Exception e) {
							System.out.println("BAD");
						}

				}else{
					System.out.println("NOT RUNNING");
				}
			}
			}catch(Exception e){
				System.out.println("Wait");
			}


			//System.out.println("RUN Final");

			// outputLine = kkp.processInput(inputLine);
			// out.println(outputLine);
			// if (outputLine.equals("Bye."))
			// break;
		}
		
	};
	TimerTask checkFrames = new TimerTask(){

		@Override
		public void run() {
			for (int i = 0; i < currentFrames.size(); i++) {
				FrameObject currentFrame = currentFrames.get(i);
				if (currentFrame.refresh()) {
					System.out.println("Removed " + currentFrames.remove(i).name);
				}
			}
			
		}
		
	};
	public void addFrame(String name, int lifeSpan, int targetVibes) {
		currentFrames.add(new FrameObject(name, System.currentTimeMillis(), lifeSpan, targetVibes));
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
}
