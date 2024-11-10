package pl.dev4lazy.shit_exchange.utils.problem_handling;

public class ConsoleLogger implements Logger {

    @Override
    public void logErr(String message) {
        /* (1) musiałem zakomentować gdyż err i out nie działały synchronicznie
        i na ekranie kolejność była inna niż kolejność wywołań
        System.err.println( message );
        System.err.flush();*/
        System.out.println( "> > > > > > > > > > >"+message );
    }

    @Override
    public void logInfo(String message) {
        System.out.println( message );
        // (1) System.out.flush();
    }

    @Override
    public void emptyLine() {
        System.out.println();
        // (1) System.out.flush();
    }
}
