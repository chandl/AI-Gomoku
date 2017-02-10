/*
    Southern Oregon University - CS455 Artificial Intelligence - Lab 2 - Gomoku

    Authors: Chandler Severson, Janelle Bakey, Gabriela Navarrete
    Date: 2/10/2017
    Class: GomokuConnector.java
        Desc: The singleton class for managing the connection between the gomoku server.
 */


import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class GomokuConnector
{
    private static String hostName;
    private static int portNo;
    private static Socket socket; //new socket to communicate with server
    private static PrintWriter output; //output stream for sockets
    private static BufferedReader input; //input reading
    private static GomokuConnector instance = null;
    private boolean DEBUG = false;

    private GomokuConnector(String hostName, int portNo) {
        this.hostName = hostName;
        this.portNo = portNo;
        connectToServer(hostName, portNo);
        try{
            if(DEBUG)System.out.println("WAITING FOR DATA!");
            while (!input.ready());   //wait until we receive data
            if(DEBUG)System.out.println("RECEIVED DATA!");

        }catch(IOException ioe){}

    }

    //Making this class a 'Singleton' - only one instance.
    public static GomokuConnector getInstance(){
        if(instance == null){
            System.err.println("getInstance() called on a null GomokuConnector.");
            return null;
        }
        return instance;
    }

    public static String getHostName() {
        return hostName;
    }

    public static int getPortNo() {
        return portNo;
    }

    public static GomokuConnector newInstance(String hostname, int portNo){
        if(instance != null){
            return instance;
        }else{
            instance = new GomokuConnector(hostname, portNo);
            return instance;
        }
    }

    public static BufferedReader getInputReader(){
        return input;
    }

    public static PrintWriter getOutputWriter(){
        return output;
    }

    private void connectToServer(String hostName, int portNo)
    {
        try {
            socket = new Socket(hostName, portNo); //connect to machine (networkName) on port (portNo)
            output = new PrintWriter(socket.getOutputStream()); //output stream to communicate with server
            input = new BufferedReader(new InputStreamReader(socket.getInputStream())); //read input from server

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + hostName);
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + hostName);
            e.printStackTrace();
            System.exit(1);
        }
    }

}