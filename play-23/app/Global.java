import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import di.HelloService;
import di.HelloServiceImpl;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import plugins.EmbedMongoPlugin;
import plugins.MongoPlugin;

public class Global extends GlobalSettings {
    private Injector injector;
    private Application application;

    @Override
    public void onStart(final Application application) {
        this.application = application;
        super.onStart(application);
        injector = createInjector();
        Logger.info(Global.class.toString() + "started");
    }

    @Override
    public <A> A getControllerInstance(Class<A> controllerClass) throws Exception {
        return injector.getInstance(controllerClass);
    }

    private Injector createInjector() {
        return Guice.createInjector(new Module() {
            @Override
            public void configure(final Binder binder) {
                binder.bind(HelloService.class).toInstance(new HelloServiceImpl("demo"));
                binder.bind(MongoPlugin.class).toInstance(application.plugin(EmbedMongoPlugin.class));
            }
        });
    }
}
