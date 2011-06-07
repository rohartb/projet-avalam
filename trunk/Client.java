import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

class Client {

	InetAddress addr;

	public Client(String s) {
		try {
		addr = InetAddress.getByName(s);
		} catch (Exception e) {}
	}


	public void  xx(Reseau r) {
        try {
            int port = 8100;
            Socket sock = new Socket(addr, port);

            InputStream in = sock.getInputStream();
            PrintStream print = new PrintStream(sock.getOutputStream());

            byte [] buffer = new byte [1024];
                int number;

                while ((number = in.read(buffer)) != -1) {
                    System.out.write(buffer, 0, number);
                }
            print.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
