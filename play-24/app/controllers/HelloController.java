package controllers;

import greetings.HelloService;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HelloController extends Controller {
    private final HelloService helloService;

    @Inject
    public HelloController(final HelloService helloService) {
        this.helloService = helloService;
    }

    public Result hello() {
        return ok("hello, " + helloService.getGreeting());
    }
}