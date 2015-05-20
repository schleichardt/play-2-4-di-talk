package greetings;

import javax.inject.Singleton;

@Singleton
final class GenericHelloServiceImpl implements HelloService {
    private final String greeting;

    public GenericHelloServiceImpl(final String greeting) {
        this.greeting = greeting;
    }

    @Override
    public String getGreeting() {
        return greeting;
    }
}
