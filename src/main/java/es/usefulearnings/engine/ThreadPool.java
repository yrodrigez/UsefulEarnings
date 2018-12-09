package es.usefulearnings.engine;

import java.util.*;

class ThreadPool {
  private final int MAX_POOL_SIZE;

  private final List<Thread> threads;
  private int current_jobs;
  private Queue<Job> pendingJobs;
  private static final ThreadPool INSTANCE = new ThreadPool();
  private boolean terminate;

  public static ThreadPool getInstance() {
    return INSTANCE;
  }

  synchronized void invokeAll(final Collection<? extends Runnable> runnableCollection) {
    runnableCollection.forEach(this::execute);
  }

  synchronized private void onJobFinished(final Job finishedJob) {
    threads.remove(finishedJob);
    pendingJobs.notify();
  }

  private Job createNewJob(final Runnable runnable) {
    return new Job(runnable, this, this.current_jobs);
  }

  private void launch() {
    while (!terminate) {
      while (pendingJobs.isEmpty() || threads.size() >= MAX_POOL_SIZE) {
        try {
          pendingJobs.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

      final Job job = pendingJobs.poll();
      final Thread thread = new Thread(job);
      threads.add(thread);
      thread.start();
    }
  }

  synchronized void execute(final Runnable runnable) {
    final Job job = this.createNewJob(runnable);
    pendingJobs.add(job);
    pendingJobs.notify();
    launch();
  }


  int getPoolSize() {
    return MAX_POOL_SIZE;
  }

  private ThreadPool() {
    MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
    this.threads = new LinkedList<>();
    this.current_jobs = 0;
    this.pendingJobs = new LinkedList<>();
    this.terminate = false;
  }

  public void terminate(){
    this.terminate = true;
  }

  private class Job implements Runnable {
    final ThreadPool threadPool;
    final Runnable runnable;
    final int job_number;

    Job(final Runnable runnable, final ThreadPool threadPool, final int job_number) {
      this.runnable = runnable;
      this.threadPool = threadPool;
      this.job_number = job_number;
    }

    public void run() {
      this.runnable.run();
      threadPool.onJobFinished(this);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      final Job job = (Job) o;
      return job_number == job.job_number &&
        Objects.equals(threadPool, job.threadPool) &&
        Objects.equals(runnable, job.runnable);
    }

    @Override
    public int hashCode() {
      return Objects.hash(threadPool, runnable, job_number);
    }
  }

}


















