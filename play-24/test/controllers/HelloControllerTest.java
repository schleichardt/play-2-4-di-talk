package controllers;

import com.google.inject.Binder;
import com.google.inject.Module;
import greetings.HelloService;
import modules.DevModule;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Result;
import play.test.FakeApplication;

import static org.assertj.core.api.Assertions.*;
import static play.test.Helpers.*;
import static play.inject.Bindings.*;

public class HelloControllerTest {
    @Test
    public void helloWithDefaultImplementation() throws Exception {
        final FakeApplication application = fakeApplication();
        running(application, () -> {
            final HelloController helloController = application.injector().instanceOf(HelloController.class);
            final Result result = helloController.hello();
            assertThat(contentAsString(result)).isEqualTo("hello, world");
        });
    }

    @Test
    public void supplyItAsConstructorArgumentWithoutDiMechanismn() throws Exception {
        //all deps directly, special case that no running application is required
        //faster!
        final HelloService helloService = () -> "test";
        final HelloController helloController = new HelloController(helloService);
        final Result result = helloController.hello();
        assertThat(contentAsString(result)).isEqualTo("hello, test");
    }

    @Test
    public void helloWithOverriddenBinding() throws Exception {
        final Application application = new GuiceApplicationBuilder()
                .overrides(bind(HelloService.class).toInstance(() -> "foo"))
                .build();
        running(application, () -> {
            final HelloController helloController = application.injector().instanceOf(HelloController.class);
            final Result result = helloController.hello();
            assertThat(contentAsString(result)).isEqualTo("hello, foo");
        });
    }

    private static class TestHelloModule implements Module {
        @Override
        public void configure(final Binder binder) {
            binder.bind(HelloService.class).toInstance(() -> "bar");
        }
    }

    @Test
    public void helloWithOverriddenModule() throws Exception {
        final Application application = new GuiceApplicationBuilder()
                .overrides(new TestHelloModule())
//                .disable(DevModule.class)//optional, if it contains eager bindings
                .build();
        running(application, () -> {
            final HelloController helloController = application.injector().instanceOf(HelloController.class);
            final Result result = helloController.hello();
            assertThat(contentAsString(result)).isEqualTo("hello, bar");
        });
    }
}