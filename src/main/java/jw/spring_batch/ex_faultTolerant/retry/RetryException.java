package jw.spring_batch.ex_faultTolerant.retry;

public class RetryException extends RuntimeException{
    public RetryException() {
    }

    public RetryException(String message) {
        super(message);
    }
}
