// Program B: Fine-Grained Synchronization with Concurrent Collections

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProgramB {
    public static void main(String[] args) {
        BufferInterface buffer = new Buffer2();

        // Starting producer threads
        for (int i = 0; i < 5; i++) {
            new Thread(new Producer2(buffer), "Producer-" + i).start();
        }

        // Starting consumer threads
        for (int i = 0; i < 5; i++) {
            new Thread(new Consumer2(buffer), "Consumer-" + i).start();
        }
    }
}

interface BufferInterface {
    void produce(int value) throws InterruptedException;

    int consume() throws InterruptedException;
}

class Buffer2 implements BufferInterface {
    private BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(10);
    public void produce(int value) throws InterruptedException {
        queue.put(value);
    }

    public int consume() throws InterruptedException {
        return queue.take();
    }
}

class Producer2 implements Runnable {
    private BufferInterface buffer;

    public Producer2(BufferInterface buffer) {
        this.buffer = buffer;
    }

    public void run() {
        int value = 0;
        try {
            while (true) {
                buffer.produce(value++);
                System.out.println(Thread.currentThread().getName() + " produced " + value);
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer2 implements Runnable {
    private BufferInterface buffer;

    public Consumer2(BufferInterface buffer) {
        this.buffer = buffer;
    }

    public void run() {
        try {
            while (true) {
                int value = buffer.consume();
                System.out.println(Thread.currentThread().getName() + " consumed " + value);
                Thread.sleep(150);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}