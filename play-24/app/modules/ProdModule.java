package modules;

import com.mongodb.MongoClient;
import mongo.ProdMongoClientProvider;
import play.api.Configuration;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import scala.collection.Seq;

public class ProdModule extends Module {
    @Override
    public Seq<Binding<?>> bindings(final Environment environment, final Configuration configuration) {
        return seq(bind(MongoClient.class).toProvider(ProdMongoClientProvider.class));
    }
}