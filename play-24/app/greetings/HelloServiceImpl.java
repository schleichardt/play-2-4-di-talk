package greetings;

public class HelloServiceImpl implements HelloService {
    @Override
    public String getGreeting() {
        return "world";
    }
}
