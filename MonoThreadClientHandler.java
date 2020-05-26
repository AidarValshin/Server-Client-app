import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class MonoThreadClientHandler implements Runnable {

    private Socket clientDialog;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Package myPackage;

    public MonoThreadClientHandler(Socket client) throws IOException, ClassNotFoundException {
        // инициируем каналы общения в сокете, для сервера
        clientDialog = client;
        out = new ObjectOutputStream(clientDialog.getOutputStream());
        in = new ObjectInputStream(clientDialog.getInputStream());
        myPackage = (Package) in.readObject();
    }

    @Override
    public void run() {
        try {
            // если клиент еще не закрыл сокет
            if (!clientDialog.isClosed()) {
                // создаем потоки чтения и записи
                System.out.println("DataInputStream created");
                System.out.println("DataOutputStream  created");
                System.out.println(" info " + myPackage.getFirst() + " " + myPackage.getOperator() + " " + myPackage.getSecond());
                // основная рабочая часть

                System.out.println("Server reading from channel");
                out.flush();

                // оповещаем клиента о начале операции над его результатом
                sleep(Math.abs(myPackage.getPeriod()));
                out.writeUTF("start");
                out.flush();

                System.out.println("Server try writing to channel");
                String result = operation();
                out.writeUTF(result);
                System.out.println("Server Wrote message to clientDialog " + myPackage.getFirst() + " " + myPackage.getOperator() + " " + myPackage.getSecond() + " = " + result);
                out.flush();
                if (!clientDialog.isClosed()) {
                    String entry = in.readUTF();
                    // проверка завершения сеанса  = проверка,что сообщение принято
                    int i = 0;
                    // ждем пока сокет не закрыт, клиент не ответил,что получил, 5 секунд
                    while (!clientDialog.isClosed() && !entry.equalsIgnoreCase("quit") && i < 100) {
                        // если кодовое слово получено то инициализируется закрытие серверного потока
                        entry = in.readUTF();
                        sleep(50);
                        i++;
                    }
                }
                // закрываем сначала каналы сокета
                in.close();
                out.close();
                // потом закрываем сокет общения с клиентом
                clientDialog.close();
            }
            System.out.println("Client disconnected");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String operation() {
        String result = null;
        if (myPackage.getOperator().equalsIgnoreCase("+")) {
            result = String.valueOf(myPackage.getFirst() + myPackage.getSecond());
        } else if (myPackage.getOperator().equalsIgnoreCase("-")) {
            result = String.valueOf(myPackage.getFirst() - myPackage.getSecond());
        } else if (myPackage.getOperator().equalsIgnoreCase("*")) {
            result = String.valueOf(myPackage.getFirst() * myPackage.getSecond());
        } else if (myPackage.getOperator().equalsIgnoreCase("/")) {
            result = String.valueOf((myPackage.getFirst()) / myPackage.getSecond());
        } else {
            result = "wrong operand";
        }
        return result;
    }
}