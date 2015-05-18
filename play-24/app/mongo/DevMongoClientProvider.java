package mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodProcess;
import play.inject.ApplicationLifecycle;
import play.libs.F;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Date;
import java.util.concurrent.CompletionException;

@Singleton
public class DevMongoClientProvider implements Provider<MongoClient> {

    private final ApplicationLifecycle applicationLifecycle;
    private final MongodProcess mongodProcess;

    @Inject
    public DevMongoClientProvider(final play.inject.ApplicationLifecycle applicationLifecycle,
                                  final MongodProcess mongodProcess) {
        this.applicationLifecycle = applicationLifecycle;
        this.mongodProcess = mongodProcess;
    }

    @Override
    public MongoClient get() {
        try {
            final int port = mongodProcess.getConfig().net().getPort();
            final String host = mongodProcess.getConfig().net().getServerAddress().getHostName();
            final MongoClient mongoClient = new MongoClient(host, port);
            applicationLifecycle.addStopHook(() -> F.Promise.promise(() -> {
                mongoClient.close();
                return null;
            }));
            addFixtures(mongoClient);
            return mongoClient;
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }

    private void addFixtures(final MongoClient mongo) {
        DB db = mongo.getDB("test");
        DBCollection col = db.getCollection("testCol");
        col.save(new BasicDBObject("testDoc", new Date()));
    }
}
