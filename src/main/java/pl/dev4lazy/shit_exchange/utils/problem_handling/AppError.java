package pl.dev4lazy.shit_exchange.utils.problem_handling;

public class AppError {
    public static final boolean INTERRUPTING_PROCESS = true;
    private String message;
    private Exception exception;
    private boolean interruptingProcess;

    public AppError( String message, Exception exception ) {
        this.message = message;
        this.exception = exception;
        this.interruptingProcess = false;
    }

    public AppError( String message, Exception exception, boolean interruptingProcess ) {
        this.message = message;
        this.exception = exception;
        this.interruptingProcess = interruptingProcess;
    }

    public AppError( String message ) {
        this.message = message;
        this.interruptingProcess = false;
    }

    public AppError(String message, boolean interruptingProcess) {
        this.message = message;
        this.interruptingProcess = interruptingProcess;
    }

    public AppError(Exception exception ) {
        this.exception = exception;
        this.interruptingProcess = false;
    }

    public AppError(Exception exception, boolean interruptingProcess) {
        this.exception = exception;
        this.interruptingProcess = interruptingProcess;
    }

    public String getMessage() {
        return message;
    }

    public Exception getException() {
        return exception;
    }

    public boolean isInterruptingProcess() {
        return interruptingProcess;
    }
}
