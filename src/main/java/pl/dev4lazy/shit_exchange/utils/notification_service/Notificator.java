package pl.dev4lazy.shit_exchange.utils.notification_service;

public interface Notificator {
    /* TODO
        zamiast String mogłaby być klasa Notification:
         z body w postaci Stringa
         z różnymi konstruktorami w zależności od typu komunikatu
         z builderem .withNewLine .withoutNewLine itp.
     */

    void showNotification( String notification );
}

