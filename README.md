https://www.playframework.com/documentation/2.4.0-RC3/Home
* [x] go through Highlights 2.4
* [x] https://www.playframework.com/documentation/2.4.0-RC3/JavaDependencyInjection
* [x] https://www.playframework.com/documentation/2.4.0-RC3/JavaTestingWithGuice
* [x] https://www.playframework.com/documentation/2.4.0-RC3/JavaTestingWithDatabases
* [ ] https://github.com/playframework/playframework/pull/4500/files
* [ ] https://www.playframework.com/documentation/2.4.x/ScalaHttpRequestHandlers
* [ ] Java RequestHandlers for custom rooting
* [ ] https://www.playframework.com/documentation/2.4.0-RC3/JavaTestingWebServiceClients
* [x] go through Migration guide
* [ ] Fixed Java GlobalSettings.onRequest regression in RC5
* [ ] go through mailing list (search)
* [ ] go through dev mailing list (search)
* [ ] go through Javadoc
* [ ] Intercepting application start-up and shutdown, onSart/onStop
* [ ] old global -> to DI, testclass
* [x] DI Play questions stackoverflow
* [ ] Plugin class deprecated, weird without di and extra file for priority, now by dependency lifecycle
* [ ] use DI for WS in Java stub
* [ ] Guice default: "Out of the box we provide and encourage the use of Guice for dependency injection, but many other dependency injection tools and techniques, including compile time dependency injection techniques in Scala are possible." https://www.playframework.com/documentation/2.4.0-RC3/Migration24 
* [ ] remove global state => Play 3.0, tiny steps to migrate
* [ ] DI router, see https://www.playframework.com/documentation/2.4.0-RC3/Migration24
* [ ] circular deps example, resolve with @, vielleicht ist das wie lazy val
* [ ] how to use Spring
* [ ] show providers
* [ ] Dependency Injected Components
   * [ ] stub for WS in test
   * [ ] MessagesApi via MongoDB using embed mongo
* [ ] @Named annotation
* [ ] setup an actor for Jobs, dependency to actor system
* [ ] play.Play.application().injector().instanceOf
* [ ] testing
* [ ]  https://www.playframework.com/documentation/2.4.x/JavaDependencyInjection
* [ ] https://www.playframework.com/documentation/2.4.x/api/java/play/Plugin.html deprecated
* [ ] bad service example
* use Singleton for ApplicationLifecycle, otherwise memory leaks

2.3/plugins.EmbedMongoPlugin
* constructor must have one argument
* play.plugins file with explicit order
* injection of dependencies possible: no, constructor single argument, @Inject for fields ignored
* providing data: 
```
final EmbedMongoPlugin plugin = Play.application().plugin(EmbedMongoPlugin.class);
final int port = plugin.getPort();
```
* not lazy loading plugins, eager
* does work with workaround, which is very explicit:
```
@Inject
    public ApplicationController(MongoPlugin plugin) {
```

```
//injector needed to be created lazily in Global.onStart
binder.bind(EmbedMongoPlugin.class).toInstance(application.plugin(EmbedMongoPlugin.class));
//or using interface 
binder.bind(MongoPlugin.class).toInstance(application.plugin(EmbedMongoPlugin.class));
```
* typical: forget @Singleton in controller
