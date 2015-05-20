package thenewglobals;

import play.filters.gzip.GzipFilter;
import play.http.HttpFilters;
import play.api.mvc.EssentialFilter;
import scala.collection.Seq;

import javax.inject.Inject;

public class CustomHttpFilters implements HttpFilters {
    private final GzipFilter gzip;

    @Inject
    public CustomHttpFilters(GzipFilter gzip) {
        this.gzip = gzip;
    }

    @Override
    public EssentialFilter[] filters() {
        return new EssentialFilter[] { gzip };
    }
}
