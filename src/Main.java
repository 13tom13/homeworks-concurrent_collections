import java.util.Random;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        Thread textGenerate = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String text = generateText("abc", 100_000);
                try {
                    queueA.put(text);
                    queueB.put(text);
                    queueC.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        textGenerate.start();

        Thread threadA = getThread(queueA, 'a');
        Thread threadB = getThread(queueB, 'b');
        Thread threadC = getThread(queueC, 'c');

        threadA.start();
        threadB.start();
        threadC.start();

    }

    static BlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);

    static BlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);

    static BlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int maxChar(BlockingQueue<String> queue, char letter) {
        int count = 0;
        int max = 0;
        String text;
        try {
            for (int i = 0; i < 10000; i++) {
                text = queue.take();
                for (char c : text.toCharArray()) {
                    if (c == letter) count++;
                }
                if (count > max) max = count;
                count = 0;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return max;
    }

    public static Thread getThread(BlockingQueue<String> queue, char c) {
        return new Thread(() -> {
            int max = maxChar(queue, c);
            System.out.println("Максимальное количество символов " + c + " = " + max);
        });


    }

}
