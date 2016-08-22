package me.hao0.nettyapp.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Author: haolin
 * Date:   8/9/16
 * Email:  haolin.h0@gmail.com
 */
public class BioServer {

    public static void main(String[] args) throws IOException {

        int port = Integer.parseInt(args[0]);

        ServerSocket serverSocket = new ServerSocket(port);

        Socket clientSocket = serverSocket.accept();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));

        PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);

        String request, response;
        while ((request = in.readLine()) != null) {
            if ("Done".equals(request)) {
                break; }
        }

        response = processRequest(request);

        out.println(response);
    }

    private static String processRequest(String request) {
        return null;
    }
}
