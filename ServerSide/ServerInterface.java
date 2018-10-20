
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
import java.util.Timer;
import java.util.TimerTask;
import java.io.*;

public class ServerInterface {
	List<FrameObject> currentFrames;

	public ServerInterface() {
		currentFrames = new ArrayList<>();
		Timer refresh = new Timer();
		refresh.schedule(t, 0, 10);
	}

	public void addFrame(int lifeSpan, int targetVibes) {
		currentFrames.add(new FrameObject(System.currentTimeMillis(),lifeSpan, targetVibes));
	}

	TimerTask t = new TimerTask() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			for (int i = 0; i < currentFrames.size(); i++) {
				if (currentFrames.get(i).refresh())
					currentFrames.remove(i);

			}
		}
	};

	public static void main(String[] args) throws IOException {
		ServerInterface si = new ServerInterface();
		int portNumber;
		if (args.length != 1) {
			portNumber = 1; // Set to something official
		} else
			portNumber = Integer.parseInt(args[0]);

		try (ServerSocket serverSocket = new ServerSocket(portNumber);
				Socket clientSocket = serverSocket.accept();

				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
			String inputLine, outputLine;

			// Initiate conversation with client
			ServerProtocol kkp = new ServerProtocol();
			outputLine = kkp.processInput(null);
			out.println(outputLine);
			
			while ((inputLine = in.readLine()) != null) {
				if (inputLine.equals("Break"))
					break;

				for (int i = 0; i < si.currentFrames.size(); i++)
					if (si.currentFrames.get(i).refresh()) {
						si.currentFrames.remove(i);
					}
				
				
				// outputLine = kkp.processInput(inputLine);
				// out.println(outputLine);
				// if (outputLine.equals("Bye."))
				// break;
			}
		} catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}
	}
}
