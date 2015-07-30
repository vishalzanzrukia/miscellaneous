package com.db.lock.test;

public class MultipleThreadTest {

	public static void main(String args[]) {

		MySQLThread thread = null;
		for (int i = 0; i < 1900; i++) {
			thread = new MySQLThread(i + "");
			thread.start();
		}

		/*
		 * MyThread thread1 = new MyThread("thread1: "); MyThread thread2 = new MyThread("thread2: "); thread1.start(); thread2.start(); boolean thread1IsAlive = true; boolean thread2IsAlive = true; do { if (thread1IsAlive && !thread1.isAlive()) { thread1IsAlive = false;
		 * System.out.println("Thread 1 is dead."); } if (thread2IsAlive && !thread2.isAlive()) { thread2IsAlive = false; System.out.println("Thread 2 is dead."); } } while (thread1IsAlive || thread2IsAlive);
		 */

	}
}

class Test{
	
	public static void main(String args[]) {

		MySQLThread thread = null;
		for (int i = 0; i < 20; i++) {
			thread = new MySQLThread(i + "");
			thread.start();
		}

		/*
		 * MyThread thread1 = new MyThread("thread1: "); MyThread thread2 = new MyThread("thread2: "); thread1.start(); thread2.start(); boolean thread1IsAlive = true; boolean thread2IsAlive = true; do { if (thread1IsAlive && !thread1.isAlive()) { thread1IsAlive = false;
		 * System.out.println("Thread 1 is dead."); } if (thread2IsAlive && !thread2.isAlive()) { thread2IsAlive = false; System.out.println("Thread 2 is dead."); } } while (thread1IsAlive || thread2IsAlive);
		 */

	}
}

class MyThread extends Thread {
	static String message[] = { "Java", "is", "hot,", "aromatic,", "and", "invigorating." };

	public MyThread(String id) {
		super(id);
	}

	public void run() {

		String name = getName();
		for (int i = 0; i < message.length; ++i) {
			randomWait();
			System.out.println(name + message[i]);
		}
	}

	void randomWait() {
		try {
			sleep((long) (3000 * Math.random()));
		} catch (InterruptedException x) {
			System.out.println("Interrupted!");
		}
	}
}

class MySQLThread extends Thread {

	public MySQLThread(String id) {
		super(id);
	}

	@Override
	public void run() {
		System.out.println("Running thread : " + getId());
		MySQLConnection.updateStock();
	}
	
	void randomWait() {
		try {
			sleep((long) (3000 * Math.random()));
		} catch (InterruptedException x) {
			System.out.println("Interrupted!");
		}
	}

}
