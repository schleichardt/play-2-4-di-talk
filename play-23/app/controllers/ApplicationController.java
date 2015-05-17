package controllers;

import com.mongodb.*;
import di.HelloService;
import play.mvc.*;
import plugins.MongoPlugin;

import javax.inject.Inject;
import java.util.List;


public class ApplicationController extends Controller {
    private final HelloService helloService;
    private final MongoPlugin mongoPlugin;

    @Inject
    public ApplicationController(final HelloService helloService, final MongoPlugin plugin) {
        this.helloService = helloService;
        mongoPlugin = plugin;
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
