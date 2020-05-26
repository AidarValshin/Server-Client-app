import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

public class Main {
    //запустим пулл из 5 потоков

    static ExecutorService exec = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws InterruptedException {

        // стартуем цикл в котором  стартуем Runnable 5 клиентов,

        exec.submit(new ClientTesterRunnable(1, 1, "+", 1000));
        sleep(5000);
        exec.submit(new ClientTesterRunnable(2, 4, "-", 1000));
        exec.submit(new ClientTesterRunnable(3, 5, "*", 50));
        exec.submit(new ClientTesterRunnable(9, 5, "/", 5000));
        exec.submit(new ClientTesterRunnable(6, 36, "-", 100));

        // закрываем фабрику
        exec.shutdown();
    }
}