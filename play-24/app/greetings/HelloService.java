package greetings;

import com.google.inject.ImplementedBy;

@ImplementedBy(HelloServiceImpl.class)
public interface HelloService {
    String getGreeting();
}
