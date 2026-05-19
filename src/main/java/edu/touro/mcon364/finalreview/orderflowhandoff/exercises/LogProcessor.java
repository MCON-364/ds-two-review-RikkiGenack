package edu.touro.mcon364.finalreview.orderflowhandoff.exercises;

import edu.touro.mcon364.finalreview.model.LogLevel;
import edu.touro.mcon364.finalreview.model.LogMessage;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * LogProcessor.
 *
 * A server receives log messages from different parts of an application:
 * authentication, payments, reporting, background jobs, and so on. Messages may
 * arrive while earlier messages are still being processed. We want one part of
 * the program to submit log messages, and a small group of worker threads to
 * process those messages in the background.
 *
 * This class represents that log-processing service.
 *
 * The main problem you are solving:
 * - incoming messages need to wait somewhere until a worker is ready for them;
 * - more than one worker may be running at the same time;
 * - every submitted message should be processed once;
 * - while messages are processed, the class must keep accurate summary counts.
 *
 * Requirements:
 * - submit(message) accepts one log message for later processing.
 * - start(workerCount) starts exactly workerCount background workers.
 * - workerCount must be positive.
 * - workers should keep processing while the processor is still accepting work
 *   or while there is still unprocessed work waiting.
 * - stop() tells the processor to stop accepting/expecting more work and waits
 *   until the already-submitted work has been handled.
 * - getTotalProcessed() returns how many log messages have been processed.
 * - getCountsByLevel() returns how many processed messages there were for each
 *   LogLevel.
 * - getCountsByLevel() must not allow callers to mutate this class's internal
 *   state.
 * - The class must behave correctly when multiple threads interact with it.
 *
 * Questions to think about before coding:
 * - Where should submitted messages wait before a worker processes them?
 * - What behavior do we need from that structure: newest first, oldest first,
 *   priority order, or something else?
 * - Which state is shared by multiple threads?
 * - Which operations must be protected so the statistics stay correct?
 * - How will worker threads know when to continue waiting for work and when to
 *   finish?
 * - What should happen if stop() is called while messages are still waiting?
 * - What should the public getter methods return so outside code cannot damage
 *   the processor's internal state?
 */
public class LogProcessor {

    /*
     * Decide what fields this class needs.
     *
     * Think about:
     * - pending work
     * - worker threads
     * - whether the processor is still running
     * - total processed count
     * - count by log level
     */

    /**
     * Accept one message for processing.
     */

    LinkedBlockingQueue<LogMessage> messages = new LinkedBlockingQueue();
    public void submit(LogMessage message) {
        // TODO: implement
        messages.add(message);
    }

    /**
     * Start the requested number of background workers.
     */
    ExecutorService pool;
    public void start(int workerCount) {
        // TODO: implement
        if (workerCount<0){
            throw new IllegalArgumentException();
        }
        pool = Executors.newFixedThreadPool(workerCount);
    }

    /**
     * The work done by one background worker.
     *
     * You may keep this helper method, rename it, or replace it with another
     * private helper if your design is clearer that way.
     */
    private void workerLoop() {
        // TODO: implement
    }

    /**
     * Process one message and update whatever statistics this class tracks.
     */
    AtomicInteger counter = new AtomicInteger(0);
    private void process(LogMessage message) {
        // TODO: implement
        submit(message);
        pool.submit(()->
                {
                    messages.poll();
                    counter.incrementAndGet();
                }
        );

    }

    /**
     * Stop the processor and wait for worker threads to finish.
     */
    public void stop() throws InterruptedException {
        // TODO: implement
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.NANOSECONDS);
        pool.shutdownNow();
    }

    /**
     * Return the number of messages processed so far.
     */
    public int getTotalProcessed() {
        // TODO: implement
        return counter.get();
    }

    /**
     * Return a safe snapshot of the counts by level.
     */
    ConcurrentHashMap<LogLevel, Integer> levelCounts = new ConcurrentHashMap<>();
    public Map<LogLevel, Integer> getCountsByLevel() {
        // TODO: implement
        levelCounts.put(LogLevel.INFO,0);
        levelCounts.put(LogLevel.WARN, 0);
        levelCounts.put(LogLevel.ERROR, 0);
        return Map.of();
        //return Map.of(levelCounts);
    }
}
