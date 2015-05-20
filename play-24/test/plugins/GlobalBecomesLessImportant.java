package plugins;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.typesafe.config.ConfigFactory;
import greetings.HelloService;
import greetings.HelloServiceImpl;
import play.*;
import play.api.Environment;
import play.api.OptionalSourceMapper;
import play.api.UsefulException;
import play.api.inject.Binding;
import play.api.inject.Module;
import play.api.mvc.EssentialFilter;
import play.filters.gzip.GzipFilter;
import play.http.DefaultHttpRequestHandler;
import play.http.HttpFilters;
import play.inject.guice.GuiceApplicationBuilder;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.inject.ApplicationLifecycle;
import play.libs.F;
import play.mvc.*;
import scala.collection.Seq;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.File;
import java.lang.reflect.Method;
import static play.inject.Bindings.*;
import static play.inject.Bindings.bind;

public class GlobalBecomesLessImportant {
    public abstract class GlobalSettingsLikeInPlay23 {
        //removed in favor of default dependency injection system
        public abstract <A> A getControllerInstance(Class<A> controllerClass) throws Exception;

        /*
            don't use it, use dependency injection instead
         */
        public abstract void beforeStart(Application app);
        public abstract void onStart(Application app);

        private class DemoController extends Controller {
            private final HelloService helloService;

            @Inject
            public DemoController(final HelloService helloService) {
                this.helloService = helloService;
            }

            public Result hello() {
                return ok(helloService.getGreeting());
            }
        }

        //examples for lazy binding
        private class LazyBindingModuleExample extends Module {
            @Override
            public Seq<Binding<?>> bindings(final Environment environment, final play.api.Configuration configuration) {
                return seq(
                        bind(HelloService.class).to(HelloServiceImpl.class)
                );
            }
        }

        private class LazyBindingGuiceModule extends AbstractModule {
            @Override
            protected void configure() {
                bind(HelloService.class).to(HelloServiceImpl.class);
            }
        }

        //examples for eager binding
        private class EagerBindingModuleExample extends Module {
            @Override
            public Seq<Binding<?>> bindings(final Environment environment, final play.api.Configuration configuration) {
                return seq(
                        bind(HelloService.class).to(HelloServiceImpl.class).eagerly(),
                        //alternative: instances are also eager, only use one of the examples here!
                        bind(HelloService.class).toInstance(new HelloServiceImpl())
                );
            }
        }

        private class EagerBindingGuiceModule extends AbstractModule {
            @Override
            protected void configure() {
                bind(HelloService.class).to(HelloServiceImpl.class).asEagerSingleton();
                //alternative: instances are also eager, only use one of the examples here!
                bind(HelloService.class).toInstance(new HelloServiceImpl());
            }
        }

        //don't use it, use dependency injection and add a shutdown hook
        public abstract void onStop(Application app);

        //example from https://www.playframework.com/documentation/2.4.x/JavaDependencyInjection
        //this has flaws
        // - it only works with classes you posses
        // - you need an import for a Play class, so the class is bound to play
        // - it returns a promise, but is still a blocking operation
        @Singleton
        public class MessageQueueConnection {
            private final MessageQueue connection;

            @Inject
            public MessageQueueConnection(ApplicationLifecycle lifecycle) {
                connection = MessageQueue.connect();

                lifecycle.addStopHook(() -> {
                    connection.stop();
                    return F.Promise.pure(null);
                });
            }

            // ...
        }

        //suggestion, use a provider instead
        @Singleton
        public class MessageQueueProvider implements Provider<MessageQueue> {
            private final ApplicationLifecycle lifecycle;

            public MessageQueueProvider(final ApplicationLifecycle lifecycle) {
                this.lifecycle = lifecycle;
            }

            @Override
            public MessageQueue get() {
                final MessageQueue messageQueue = MessageQueue.connect();

                lifecycle.addStopHook(() -> F.Promise.promise(() -> {
                    messageQueue.stop();
                    return null;
                }));

                return messageQueue;
            }
        }

        //don't use it, use a custom HttpErrorHandler (interface) instead, there is also a default implementation
        public abstract F.Promise<Result> onError(Http.RequestHeader request, Throwable t);
        public abstract F.Promise<Result> onHandlerNotFound(Http.RequestHeader request);//client error
        public abstract F.Promise<Result> onBadRequest(Http.RequestHeader request, String error);

        //you need to wire it into your application.conf play.http.errorHandler = "thenewglobals.CustomHttpErrorHandler"
        private class CustomHttpErrorHandler extends DefaultHttpErrorHandler {

            @javax.inject.Inject
            public CustomHttpErrorHandler(final Configuration configuration, final play.Environment environment, final OptionalSourceMapper optionalSourceMapper, final Provider<Router> provider) {
                super(configuration, environment, optionalSourceMapper, provider);
            }

            @Override
            protected F.Promise<Result> onBadRequest(final Http.RequestHeader requestHeader, final String message) {
                return super.onBadRequest(requestHeader, message);
            }

            @Override
            public F.Promise<Result> onClientError(final Http.RequestHeader requestHeader, final int statusCode, final String message) {
                return super.onClientError(requestHeader, statusCode, message);
            }

            @Override
            protected F.Promise<Result> onDevServerError(final Http.RequestHeader requestHeader, final UsefulException e) {
                return super.onDevServerError(requestHeader, e);
            }

            @Override
            protected F.Promise<Result> onForbidden(final Http.RequestHeader requestHeader, final String message) {
                return super.onForbidden(requestHeader, message);
            }

            @Override
            protected F.Promise<Result> onNotFound(final Http.RequestHeader requestHeader, final String message) {
                return F.Promise.pure(Results.notFound("bad request, use your own template"));
            }

            @Override
            protected F.Promise<Result> onProdServerError(final Http.RequestHeader requestHeader, final UsefulException e) {
                return super.onProdServerError(requestHeader, e);
            }

            @Override
            public F.Promise<Result> onServerError(final Http.RequestHeader requestHeader, final Throwable throwable) {
                return super.onServerError(requestHeader, throwable);
            }
        }

        /*
        - really deprecated
        - use env vars, other config files: -Dconfig.resource,  -Dconfig.file, -Dconfig.url
        - or use custom ApplicationLoader
         */
        public abstract Configuration onLoadConfig(Configuration config, File path, ClassLoader classloader);
        public abstract Configuration onLoadConfig(Configuration config, File path, ClassLoader classloader, Mode mode);

        private void customApplicationLoaderWithConfigExample(){
            //for tests
            final Application application = new GuiceApplicationBuilder()
                    .loadConfig(environment -> new Configuration(ConfigFactory.parseString("mysettings.key=value"))
                            .withFallback(Configuration.load(environment)))
                    .build();
        }

        //use action composition instead
        //or a custom implementation for HttpRequestHandler (interface)
        public abstract Action onRequest(Http.Request request, Method method);

        @With(DemoActionComposition.class)
        public class ActionCompositionExampleController extends Controller {
            public Result hello() {
                final int value = (int) ctx().args.getOrDefault("computedValue", 0);//getOrDefault is new in Java 8, no null
                return ok("result =" + value);
            }
        }

        public class DemoActionComposition extends Action.Simple {
            @Override
            public F.Promise<Result> call(final Http.Context context) throws Throwable {
                Logger.info("request " + context.request());//but here is not the "root" action method available
                context.args.put("computedValue", 1 + 1);
                return delegate.call(context);
            }
        }

        //custom HttpRequestHandler example
        //need to wire it into the application.conf: "path.to.CustomHttpRequestHandler"
        public class CustomHttpRequestHandler extends DefaultHttpRequestHandler {
            @Override
            public Action createAction(final Http.Request request, final Method method) {
                //log sth like [info] application - request GET /hello public play.mvc.Result controllers.HelloController.hello()
                Logger.info("request " + request + " " + method);
                return super.createAction(request, method);
            }
        }

        //if you need this, you still need the Global class :D
        public abstract play.api.mvc.Handler onRouteRequest(Http.RequestHeader request);

        //don't use it
        //implement the interface HttpFilters and register the class
        //in application.conf: play.http.filters="path.to.CustomFilters"
        //
        //also: this was inconvenient if you need parameterized filters, which is possible in scala
        public abstract <T extends play.api.mvc.EssentialFilter> Class<T>[] filters();

        //example from https://github.com/playframework/playframework/pull/4500/files
        public class Filters implements HttpFilters {

            private final GzipFilter gzip;

            @Inject
            public Filters(GzipFilter gzip) {
                this.gzip = gzip;
            }

            @Override
            public EssentialFilter[] filters() {
                //in this example it uses DI, but now you can in Java use parameterized filters
                //instead of using just the no param constructors
                //à la return new EssentialFilter[] { new WhatEverFilter(5, "another param") };
                return new EssentialFilter[] { gzip };
            }
        }

        //additional notes, in application.conf you can dermine the class for
        // HttpRequestHandlers, HttpErrorHandlers and Filters
        //in tests you can override it
        private void confOverrideForFilters() {
            final Application application = new GuiceApplicationBuilder()
                    //overrides one value
                    .configure("play.http.filters", Filters.class.getCanonicalName())
                    .build();
        }

    }


}
