package twitter;

import com.twitter.hbc.httpclient.BasicClient;
import java.io.FileWriter;
import twitter.files.FileUtils;
import twitter.tweeutils.ClientSupplier;

import java.io.File;
import java.util.Random;
import java.util.concurrent.*;

public class TweeterReader {
    private static final String CONSUMER_KEY = "GPArF3hn3QxTf9v6GrwAczcGO";
    private static final String CONSUMER_SECRET = "7dUNMoPKPWtQZ7TcuhoqWTkuTpkSgRqSGk9vPBfXWPMK8NMXxn";
    private static final String TOKEN = "1908368161-IXatUs3pv25qo6C8gy2Rl7LPWzIz9PsCo9cG45l";
    private static final String SECRET = "S20EJhj1db8MIF5xpv5P9UxvCjBTA5A8JTtreeEZNLKei";

    public void run() throws Exception {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(10000);

        BasicClient client = ClientSupplier.prepareAndConnectDefaultClient(CONSUMER_KEY,
                CONSUMER_SECRET,
                TOKEN,
                SECRET,
                queue);

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newScheduledThreadPool(10);
        int filesCount = 50;
        CountDownLatch doneSignal = new CountDownLatch(filesCount);

        for (int i = 0; i < filesCount; i++) {
            executor.execute(() -> {
                Random random = new Random();
                File resultFile = FileUtils.createFile("C:\\temp\\result" + random.nextInt(100) + ".txt");

                FileUtils.writeToFile(resultFile, writer -> {
                    Integer rowCount = 0;
                    while (!client.isDone() && rowCount < 100) {
                        writer.write(queue.take());
                        rowCount++;
                    }
                }, doneSignal::countDown);
            });
        }

        doneSignal.await();
        client.stop();
    }

    public static void main(String[] args) throws Exception {
        Thread.sleep(10000);
        TweeterReader reader = new TweeterReader();
        reader.run();
    }
}
