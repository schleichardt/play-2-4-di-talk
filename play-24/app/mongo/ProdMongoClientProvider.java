package mongo;

import com.mongodb.MongoClient;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class ProdMongoClientProvider implements Provider<MongoClient> {
    @Override
    public MongoClient get() {
        return new MongoClient("localhost", 27017);
    }
}
