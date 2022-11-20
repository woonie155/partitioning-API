package jw.spring_batch.ex_faultTolerant;

public class SkipException extends Exception{


    public SkipException(String s) {
        super(s);
    }
}


