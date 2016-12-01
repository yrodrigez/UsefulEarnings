package es.usefulearnings.engine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ThreadPool {

  private ExecutorService executor;
  private final int MAX_POOL_SIZE;

  private static ThreadPool _instance = new ThreadPool();

  static ThreadPool get_instance(){
    return _instance;
  }

  void shutDown() {
    executor.shutdown();
  }

  void execute(Runnable runnable){
    executor.execute(runnable);
  }

  int getPoolSize() {
    return MAX_POOL_SIZE;
  }

  private ThreadPool(){
    MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
    executor = Executors.newFixedThreadPool(MAX_POOL_SIZE);
  }
}
