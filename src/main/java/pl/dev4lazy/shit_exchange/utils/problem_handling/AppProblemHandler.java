package pl.dev4lazy.shit_exchange.utils.problem_handling;

public class AppProblemHandler implements ProblemHandler {

    @Override
    public void handle(Problem problem) {
        problem.handle();
    }
}
