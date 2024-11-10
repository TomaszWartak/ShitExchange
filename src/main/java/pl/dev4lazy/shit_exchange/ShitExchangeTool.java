package pl.dev4lazy.shit_exchange;

import pl.dev4lazy.shit_exchange.model.ToolsGrip;
import pl.dev4lazy.shit_exchange.utils.ConsoleReader;
import pl.dev4lazy.shit_exchange.utils.notification_service.AppNotificationHandler;
import pl.dev4lazy.shit_exchange.utils.notification_service.ConsoleNotificator;
import pl.dev4lazy.shit_exchange.utils.problem_handling.AppProblemHandler;
import pl.dev4lazy.shit_exchange.utils.problem_handling.ConsoleErrorHandler;

public class ShitExchangeTool {

    public static void main(String[] args) {
        ToolsGrip.setErrorHandler( new ConsoleErrorHandler() );
        ToolsGrip.setProblemHandler( new AppProblemHandler() );
        ToolsGrip.setNotificationHandler( new AppNotificationHandler( new ConsoleNotificator() ) );
        ToolsGrip.setKeyboardReader( new ConsoleReader() );
        ShitExchangeApplication app = new ShitExchangeApplication( );
        ToolsGrip.setApp( app );
        app.run();
    }

}