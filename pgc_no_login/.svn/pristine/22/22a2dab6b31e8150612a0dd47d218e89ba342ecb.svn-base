package cn.dezhisoft.cloud.mi.newugc.common.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class WeakRunUtil {
    private static final int CORE_POOL_SIZE = 0;
    private static final int MAXIMUM_POOL_SIZE = 20;
    private static final int KEEP_ALIVE = 1;
    private static final BlockingQueue<Runnable> sWorkQueue = new LinkedBlockingQueue<Runnable>(50);

    private WeakRunUtil(){}
    
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread( r, "WeakRun Thread #" + mCount.getAndIncrement() );
        }
    };
   
    private static final RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardOldestPolicy();

    private static final ThreadPoolExecutor sExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sWorkQueue, sThreadFactory, handler );


    public static void execute( Runnable runnable ){
    	sExecutor.execute( runnable );
    }
}
