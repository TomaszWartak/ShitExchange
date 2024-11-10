package pl.dev4lazy.shit_exchange.utils.problem_handling;

import java.util.Arrays;

public class ConsoleErrorHandler implements ErrorHandler {

    @Override
    public void handle( AppError appError ) {
        String message = appError.getMessage();
        String errorInfo = "";
        if (messageIsNotEmpty( message )) {
            errorInfo = message;
        }
        Exception exception = appError.getException();
        if (exception != null) {
            String stackTrace = Arrays.stream( exception.getStackTrace() )
                    .map( ste -> "\n" +ste.toString())
                    .toList()
                    .toString();
            errorInfo =
                    errorInfo + "\n" +
                    exception.getMessage() + "\n" +
                    stackTrace;
        }
        if (!errorInfo.isEmpty()) {
            System.err.println( errorInfo );
            System.out.println();
        }
        if (appError.isInterruptingProcess()) {
            System.out.println( "WYKONANIE PRZERWANE");
        }
        System.out.println();
    }

    private boolean messageIsNotEmpty( String message ) {
        return (message!=null) && (!message.isEmpty());
    }
}