package es.usefulearnings.engine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ThreadPool {

  private final ExecutorService executor;
  private final int MAX_POOL_SIZE;

  private static final ThreadPool INSTANCE = new ThreadPool();

  public static ThreadPool getInstance(){
    return INSTANCE;
  }

  void shutDown() {
    executor.shutdown();
  }

  void execute(final Runnable runnable){
    executor.execute(runnable);
  }

  int getPoolSize() {
    return MAX_POOL_SIZE;
  }

  private ThreadPool() {
    MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
    executor = Executors.newFixedThreadPool(MAX_POOL_SIZE);
  }
}
