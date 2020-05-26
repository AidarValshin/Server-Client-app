import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class ClientTesterRunnable implements Runnable {

    private Socket socket;
    private Package myPackage;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public ClientTesterRunnable(int first, int second, String operand, int period) {
        try {
            // создаём сокет общения на стороне клиента в конструкторе объекта
            socket = new Socket("localhost", 3345);
            System.out.println("Client " + first + " wait");
            myPackage = new Package(first, second, operand, period);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("run " + myPackage.getFirst() + " " + myPackage.getOperator() + " " + myPackage.getSecond() + " wait");
            oos.writeObject(myPackage);
            oos.flush();
            String in = ois.readUTF();
            while (!in.equalsIgnoreCase("start")) {
                in = ois.readUTF();
                sleep(50);
            }
            // инициализация работы ,ждем овтет
            in = ois.readUTF();
            while (in.equalsIgnoreCase("start")) {
                in = ois.readUTF();
                sleep(50);
            }
            System.out.println("Server answered " + myPackage.getFirst() + " " + myPackage.getOperator() + " " + myPackage.getSecond() + " = " + in);

            if (!socket.isClosed()) {
                oos.writeUTF("quit");
                oos.flush();
            }
            // закрываем
            oos.close();
            ois.close();
            socket.close();

        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}




