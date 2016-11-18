package esm.distribution.extension;

import esm.distribution.messaging.presentation.MethodInvocation;
import esm.distribution.messaging.presentation.MethodResult;
import esm.util.ThrowableFunction;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The QOSObserverInterceptor (QOS -> Quality Of Service) is responsible to get information about a conjunct of
 * invocations in the same {@link esm.distribution.invocation.Skeleton}, each time thats the method
 * {@link esm.distribution.invocation.Skeleton#processRemoteInvocation(MethodInvocation)} is called in the object to
 * check, it should be called using the {@link #intercept(ThrowableFunction, MethodInvocation)} method, this method will
 * get the information about the time to execute the method, the number of invocations done, and the current number of
 * invocations.
 *
 * @author Pedro Henrique
 */
public class QOSObserverInterceptor implements InvocationInterceptor<MethodInvocation, MethodResult> {

    /**
     * The number of invocations intercepted.
     */
    private AtomicInteger invocationCount;

    /**
     * The number of invocations executing at this moment.
     */
    private AtomicInteger currentInvocationCount;

    /**
     * The last invocation execution times.
     */
    private long[] executionTimes;

    /**
     * The last invocation execution times index.
     */
    private AtomicInteger executionTimesIndex;

    /**
     * Creates the QOSObserver no intercepted invocations registered.
     */
    public QOSObserverInterceptor() {
        invocationCount = new AtomicInteger();
        currentInvocationCount = new AtomicInteger();
        executionTimes = new long[20];
        executionTimesIndex = new AtomicInteger();
    }

    @Override
    public MethodResult intercept(ThrowableFunction<MethodInvocation, MethodResult> intercepted,
                                  MethodInvocation argument) throws Throwable {
        invocationCount.incrementAndGet();
        currentInvocationCount.incrementAndGet();
        long startTime = System.currentTimeMillis();
        MethodResult methodResult = intercepted.apply(argument);
        long finishTime = System.currentTimeMillis();
        currentInvocationCount.decrementAndGet();
        executionTimes[executionTimesIndex.getAndUpdate(i -> (i + 1) % executionTimes.length)] = finishTime - startTime;
        return methodResult;
    }

    /**
     * Returns the number of intercepted invocations by this interceptor.
     *
     * @return the number of intercepted invocations
     */
    public int getInvocationCount() {
        return invocationCount.intValue();
    }

    /**
     * Returns the number of intercepted invocations exactly in the moment that this method is called.
     *
     * @return the number of current intercepted invocations
     */
    public int getCurrentInvocationCount() {
        return currentInvocationCount.intValue();
    }

    /**
     * Gets the average time of the last invocations.
     *
     * @return the average time of the last invocations
     */
    public long getAverageExecutionTime() {
        long averageExecutionTime = 0;
        long receivedInvocations = 0;
        for (long executionTime : executionTimes) {
            averageExecutionTime += executionTime;
            receivedInvocations++;
        }
        return averageExecutionTime / receivedInvocations;
    }
}
