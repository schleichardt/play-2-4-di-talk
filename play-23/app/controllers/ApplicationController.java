package controllers;

import com.mongodb.*;
import di.HelloService;
import play.Play;
import play.mvc.*;
import plugins.EmbedMongoPlugin;
import plugins.MongoPlugin;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class ApplicationController extends Controller {
    private final HelloService helloService;
    private final MongoPlugin mongoPlugin;

    @Inject
    public ApplicationController(final HelloService helloService, final MongoPlugin mongoPlugin) {
        this.helloService = helloService;
        this.mongoPlugin = mongoPlugin;
        //general known way:
        //this.mongoPlugin = Play.application().plugin(EmbedMongoPlugin.class);
        //works also with interface
    }

    public Result hello() {
        return ok(helloService.getGreeting());
    }

    public Result index() {
        final int port = mongoPlugin.getPort();
        MongoClient mongo = new MongoClient("localhost", port);
        DB db = mongo.getDB("test");
        DBCollection col = db.getCollection("testCol");
        final List<DBObject> dbObjects1 = col.find().toArray();
        return ok("" + dbObjects1);
    }
}
