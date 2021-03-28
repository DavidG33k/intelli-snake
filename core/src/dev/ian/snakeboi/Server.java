package dev.ian.snakeboi;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server extends Thread
{
    private ServerSocket server;
    private String comandoVocale;

    public Server() throws Exception
    {
        server = new ServerSocket(5051);
        System.out.println("Il Server è in attesa sulla porta 5051.");
        this.start();
    }

    public String getComandoVocale() {
        return comandoVocale;
    }

    public void setComandoVocale(String comandoVocale) {
        this.comandoVocale = comandoVocale;
    }

    public void run()
    {
        while(true)
        {
            try {
                System.out.println("In attesa di Connessione.");
                Socket client = server.accept();
                System.out.println("Connessione accettata da: " + client.getInetAddress());

                InputStream inputStream = client.getInputStream();
                DataInputStream in = new DataInputStream(inputStream);

                comandoVocale = in.readUTF();
                System.out.println("Messaggio: " + comandoVocale);
            }
            catch(Exception e) {}
        }


    }
}