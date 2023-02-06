package jw.study.spring_batch.ex_faultTolerant;

public class NoSkipException extends Exception{
    public NoSkipException(String message) {
        super(message);
    }
}
