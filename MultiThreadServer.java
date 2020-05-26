import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Aidar
 */
public class MultiThreadServer {
    //пулл потоков- очередь
    static ThreadPoolExecutor executeIt ;
    static ServerSocket server;


    public static void main(String args [] ) throws IOException, ClassNotFoundException {
        executeIt= (ThreadPoolExecutor) Executors.newFixedThreadPool(Integer.parseInt(args[0]));
        //3345
        server = new ServerSocket(Integer.parseInt(args[1]));

            System.out.println("Server socket created, command console reader for listen to server commands");

            // стартуем цикл при условии что серверный сокет не закрыт
            while (!server.isClosed()) {
                // проверяем поступившие комманды из консоли сервера если такие были

                Socket client = server.accept();
                // после получения запроса на подключение сервер создаёт сокет
                // для общения с клиентом и отправляет его в отдельный поток
                // 1 поток  - MonoThreadClientHandler
                //он  продолжает общение от лица сервера
                // ставим в очередь потоков
                executeIt.execute(new MonoThreadClientHandler(client));
                System.out.println("pools " + executeIt.getPoolSize());
            }
            // закрытие пула потоков после завершения работы всех нитей
            executeIt.shutdown();
        }
    }

