// Program A: Coarse-Grained Synchronization

import java.util.LinkedList;

public class ProgramA {
    public static void main(String[] args) {
        Buffer1 buffer = new Buffer1();

        // Starting producer threads
        for (int i = 0; i < 5; i++) {
            new Producer1(buffer, "Producer-" + i).start();
        }

        // Starting consumer threads
        for (int i = 0; i < 5; i++) {
            new Consumer1(buffer, "Consumer-" + i).start();
        }
    }
}

class Buffer1 {
    private LinkedList<Integer> list = new LinkedList<>();
    private final int CAPACITY = 10;

    public synchronized void produce(int value) throws InterruptedException {
        while (list.size() == CAPACITY) {
            wait();
        }
        list.add(value);
        notifyAll();
    }

    public synchronized int consume() throws InterruptedException {
        while (list.isEmpty()) {
            wait();
        }
        int value = list.removeFirst();
        notifyAll();
        return value;
    }
}

class Producer1 extends Thread {
    private Buffer1 buffer;

    public Producer1(Buffer1 buffer, String name) {
        super(name);
        this.buffer = buffer;
    }

    public void run() {
        int value = 0;
        try {
            while (true) {
                buffer.produce(value++);
                System.out.println(getName() + " produced " + value);
                sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer1 extends Thread {
    private Buffer1 buffer;

    public Consumer1(Buffer1 buffer, String name) {
        super(name);
        this.buffer = buffer;
    }

    public void run() {
        try {
            while (true) {
                int value = buffer.consume();
                System.out.println(getName() + " consumed " + value);
                sleep(150);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}