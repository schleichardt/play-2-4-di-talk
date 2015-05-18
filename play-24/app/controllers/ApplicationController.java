package controllers;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import greetings.HelloService;
import play.mvc.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class ApplicationController extends Controller {
    private final HelloService helloService;
    private final MongoClient mongoClient;

    @Inject
    public ApplicationController(final HelloService helloService, final MongoClient mongoClient) {
        this.helloService = helloService;
        this.mongoClient = mongoClient;
    }

    public Result hello() {
        return ok(helloService.getGreeting());
    }

    public Result index() {
        DB db = mongoClient.getDB("test");
        DBCollection col = db.getCollection("testCol");
        final List<DBObject> dbObjects1 = col.find().toArray();
        return ok("" + dbObjects1);
    }
}