package mongo;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import play.inject.ApplicationLifecycle;
import play.libs.F;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.concurrent.CompletionException;

@Singleton
public class MongodProcessProvider implements Provider<MongodProcess> {
    private final ApplicationLifecycle applicationLifecycle;

    @Inject
    public MongodProcessProvider(final play.inject.ApplicationLifecycle applicationLifecycle) {
        this.applicationLifecycle = applicationLifecycle;
    }

    @Override
    public MongodProcess get() {
        try {
            final Net localhostWithRandomPort = new Net();
            final IMongodConfig mongodConfig = new MongodConfigBuilder()
                    .version(Version.Main.PRODUCTION)
                    .net(localhostWithRandomPort)
                    .build();
            final MongodExecutable mongodExecutable = MongodStarter.getDefaultInstance().prepare(mongodConfig);
            final MongodProcess mongod = mongodExecutable.start();
            applicationLifecycle.addStopHook(() -> stopEmbedMongo(mongodExecutable, mongod));
            return mongod;
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }

    private static F.Promise<Void> stopEmbedMongo(final MongodExecutable mongodExecutable, final MongodProcess mongod) {
        return F.Promise.promise(() -> {
            if (mongodExecutable != null)
                mongodExecutable.stop();
            if (mongod != null)
                mongod.stop();
            return null;
        });
    }
}
