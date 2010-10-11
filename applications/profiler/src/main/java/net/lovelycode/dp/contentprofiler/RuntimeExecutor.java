/**
 * 
 */
package net.lovelycode.dp.contentprofiler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Execute command from the Runtime. This class will make sure the external
 * application will not hung the application by specifying a timeout in which
 * the application must return. If it does not an InterruptedException will be
 * thrown
 * 
 * @author Aviran Mordo
 * 
 */
public class RuntimeExecutor {
    private long timeout = Long.MAX_VALUE;

    /**
     * Default constructor - Timeout set to Long.MAX_VALUE
     */
    public RuntimeExecutor() {
        // Do nothing
    }

    /**
     * Constructor
     * 
     * @param timeout
     *            Set the timeout for the external application to run
     */
    public RuntimeExecutor(long timeout) {
        this.timeout = timeout;
    }

    /**
     * Execute a Runtime process
     * 
     * @param command
     *            - The command to execute
     * @param env
     *            - Environment variables to put in the Runtime process
     * @return The output from the process
     * @throws IOException
     * @throws TimeoutException
     *             - Process timed out and did not return in the specified
     *             amount of time
     */
    public String execute(String command, String[] env) throws IOException,
            TimeoutException {
        return execute( Runtime.getRuntime().exec(command, env) );
    }
    
    /**
     * 
     * @param pb
     * @return
     * @throws IOException
     * @throws TimeoutException
     */
    public String execute( ProcessBuilder pb ) throws IOException, TimeoutException {
        return this.execute(pb.start());
    }

    /**
     * 
     * @param p
     * @return
     * @throws IOException
     * @throws TimeoutException
     */
    public String execute(Process p) throws IOException, TimeoutException { 
        long now = System.currentTimeMillis();
        long finish = now + timeout;
        while ( isAlive( p ) && ( System.currentTimeMillis() < finish ) )
        {
            try {
                Thread.sleep( 10 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if ( isAlive( p ) )
        {
            throw new TimeoutException( "Process timeout out after " + (int)(timeout/1000) + " seconds" );
        }
        //returnValue = p.exitValue();
        return "";
    }
    public static boolean isAlive( Process p ) {
        try
        {
            p.exitValue();
            return false;
        } catch (IllegalThreadStateException e) {
            return true;
        }
    }
    
    
    public String executeInterupt( Process p ) throws IOException, TimeoutException {
        // Set a timer to interrupt the process if it does not return within the
        // timeout period
        Timer timer = new Timer();
        InterruptScheduler is = new InterruptScheduler(Thread.currentThread());
        timer.schedule( is, this.timeout );

        try {
            p.waitFor();
        } catch (InterruptedException e) {
            // Stop the process from running
            p.destroy();
            throw new TimeoutException("Process did not return after "
                    + this.timeout + " milliseconds");
        } finally {
            // Stop the timer
            timer.cancel();
            is.cancel();
        }

        // Get the output from the external application
        StringBuilder buffer = new StringBuilder();
        BufferedInputStream br = new BufferedInputStream(p.getInputStream());
        while (br.available() != 0) {
            buffer.append((char) br.read());
        }
        String res = buffer.toString().trim();
        return res;
    }

    // ///////////////////////////////////////////
    private class InterruptScheduler extends TimerTask {
        Thread target = null;

        public InterruptScheduler(Thread target) {
            this.target = target;
        }

        @Override
        public void run() {
            target.interrupt();
        }

    }

    public static void main(String[] args) {
        // Set timeout to 3 seconds
        RuntimeExecutor r = new RuntimeExecutor(3000);
        try {
            System.out.println(r.execute("grep java", null));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        try {
            int returnCode = timedCall(new Callable<Integer>() {
                public Integer call() throws Exception
                {
                    java.lang.Process process = Runtime.getRuntime().exec("echo hello"); 
                    return process.waitFor();
                }}, 10, TimeUnit.SECONDS);
            System.out.println("Return code: "+returnCode);
        } catch (TimeoutException e) {
            // Handle timeout here
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("STOPPED");
    }

    private static final ExecutorService THREAD_POOL = Executors
            .newCachedThreadPool();

    private static <T> T timedCall(Callable<T> c, long timeout,
            TimeUnit timeUnit) throws InterruptedException, ExecutionException,
            TimeoutException {
        FutureTask<T> task = new FutureTask<T>(c);
        THREAD_POOL.execute(task);
        return task.get(timeout, timeUnit);
    }

}