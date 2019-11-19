package com.rnd.jasperjava.exception;

public class ReportException extends Exception {

    public ReportException() {
        super("Report Exception");
    }

    public ReportException(String message) {
        super(message);
    }

    public ReportException(Exception e) {
        super(e);
    }
}
