package pl.dev4lazy.shit_exchange.utils.notification_service;

public class AppNotificationHandler implements NotificationHandler {
    private Notificator notificator;

    public AppNotificationHandler( Notificator notificator ) {
        this.notificator = notificator;
    }

    @Override
    public void handle( String notification) {
        notificator.showNotification( notification );
    }
}
