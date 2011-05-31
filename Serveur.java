import java.net.Socket;
import java.net.ServerSocket;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Serveur implements Runnable {


	public Serveur() {

	}

	public void run() {
        try {
            ServerSocket listener = new ServerSocket(8100);
            boolean finished = false;

            System.out.println("Serveur en ecoute sur le port : " +
                               listener.getLocalPort());
            while (!finished) {
                Socket client = listener.accept();
                InputStream in = client.getInputStream();
                OutputStream out = client.getOutputStream();
                PrintStream print = new PrintStream(out);

                byte [] buffer = new byte [1024];
                int number;

//                 while ((number = in.read(buffer)) != -1) {
//                     System.out.write(buffer, 0, number);
//                     try {
//                     Thread.sleep(50);
//                     } catch (InterruptedException ex) {}
//                     print.print("Coucou client, ici serveur");
//                 }
                //finished = true;

            }
                System.out.println("sorti");
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
