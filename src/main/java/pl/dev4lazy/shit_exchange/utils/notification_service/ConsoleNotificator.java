package pl.dev4lazy.shit_exchange.utils.notification_service;

public class ConsoleNotificator implements Notificator {

    @Override
    public void showNotification( String notification ) {
        System.out.println( notification );
    }
}
