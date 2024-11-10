package pl.dev4lazy.shit_exchange.model;

import pl.dev4lazy.shit_exchange.ShitExchangeApplication;
import pl.dev4lazy.shit_exchange.utils.KeyboardReader;
import pl.dev4lazy.shit_exchange.utils.notification_service.NotificationHandler;
import pl.dev4lazy.shit_exchange.utils.problem_handling.ErrorHandler;
import pl.dev4lazy.shit_exchange.utils.problem_handling.Problem;
import pl.dev4lazy.shit_exchange.utils.problem_handling.ProblemHandler;
import pl.dev4lazy.shit_exchange.utils.problem_handling.AppError;

public class ToolsGrip {

    private static ToolsGrip instance;
    private static ErrorHandler errorHandler;
    private static ProblemHandler problemHandler;
    private static NotificationHandler notificationHandler;
    private static KeyboardReader keyboardReader;
    private static ShitExchangeApplication app;


    private ToolsGrip() {
        // Prywatny konstruktor, aby uniemożliwić tworzenie wielu instancji.
    }

    public static ToolsGrip getInstance() {
        if (instance == null) {
            instance = new ToolsGrip();
        }
        return instance;
    }

    public static void setErrorHandler( ErrorHandler _errorHandler) {
        errorHandler = _errorHandler;
    }

    public static void setProblemHandler( ProblemHandler _problemHandler) {
        problemHandler = _problemHandler;
    }

    public static void setNotificationHandler( NotificationHandler _notificationHandler ) {
        notificationHandler = _notificationHandler;
    }

    public static void setKeyboardReader( KeyboardReader _keyboardReader ) {
        keyboardReader = _keyboardReader;
    }

    public static KeyboardReader getKeyboardReader() {
        return keyboardReader;
    }

    public static void setApp( ShitExchangeApplication shitExchangeApplication ) {
        app = shitExchangeApplication;
    }

    public static ShitExchangeApplication getApp() {
        return app;
    }

    public static void handleError( AppError appError ) {
        errorHandler.handle( appError );
    }

    public static void handleProblem( Problem problem ) {
        problemHandler.handle( problem );
    }

    public static void handleNotification( String notification ) {
        notificationHandler.handle( notification );
    }

}
