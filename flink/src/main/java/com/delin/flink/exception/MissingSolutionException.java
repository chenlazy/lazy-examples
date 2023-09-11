package com.delin.flink.exception;

/**
 * MissingSolutionException
 *
 * @author: chendl
 * @date: Created in 2023/8/8 9:57
 * @description: com.delin.flink.exception.MissingSolutionException
 */
public class MissingSolutionException extends Exception {
    /** Create new exception. */
    public MissingSolutionException() {}

    /** Determine if the root cause of a failure is a MissingSolutionException. */
    public static boolean ultimateCauseIsMissingSolution(Throwable e) {
        while (e != null) {
            if (e instanceof MissingSolutionException) {
                return true;
            } else {
                e = e.getCause();
            }
        }
        return false;
    }
}
