package plugins;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import play.Plugin;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CompletionException;

public class EmbedMongoPlugin extends Plugin implements MongoPlugin {
    private final play.Application application;
    private MongodExecutable mongodExecutable;
    private MongodProcess mongod;

    //All Play plugins must define a constructor that accepts a single argument either of type play.Application
    //for Java plugins or play.api.Application for Scala plugins.
    public EmbedMongoPlugin(final play.Application application) {
        this.application = application;
    }

    @Override
    public boolean enabled() {
        return application.configuration().getBoolean("embedmongo.enabled", false);
    }

    @Override
    public void onStart() {
        try {
            final int port = getPort();
            final IMongodConfig mongodConfig = new MongodConfigBuilder()
                    .version(Version.Main.PRODUCTION)
                    .net(new Net(port, Network.localhostIsIPv6()))
                    .build();
            mongodExecutable = MongodStarter.getDefaultInstance().prepare(mongodConfig);
            mongod = mongodExecutable.start();

            MongoClient mongo = new MongoClient("localhost", port);
            DB db = mongo.getDB("test");
            DBCollection col = db.getCollection("testCol");
            col.save(new BasicDBObject("testDoc", new Date()));
            mongo.close();
        } catch (IOException e) {
            throw new CompletionException(e);
        }
    }

    @Override
    public int getPort() {
        return 12345;
    }

    @Override
    public void onStop() {
        if (mongodExecutable != null)
            mongodExecutable.stop();
        if (mongod != null)
            mongod.stop();
    }
}
