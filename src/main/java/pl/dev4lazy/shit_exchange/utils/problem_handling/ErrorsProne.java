package pl.dev4lazy.shit_exchange.utils.problem_handling;

public interface ErrorsProne {
    void setErrorOccurred();
    boolean didErrorOccur();
    void clearErrorOccurred();
}
