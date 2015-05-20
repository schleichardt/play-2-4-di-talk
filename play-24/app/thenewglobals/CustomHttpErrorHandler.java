package thenewglobals;

import play.Configuration;
import play.Environment;
import play.Logger;
import play.api.OptionalSourceMapper;
import play.api.UsefulException;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Provider;

public class CustomHttpErrorHandler extends DefaultHttpErrorHandler {

    @Inject
    public CustomHttpErrorHandler(final Configuration configuration, final Environment environment, final OptionalSourceMapper optionalSourceMapper, final Provider<Router> provider) {
        super(configuration, environment, optionalSourceMapper, provider);
    }

    @Override
    protected F.Promise<Result> onBadRequest(final Http.RequestHeader requestHeader, final String s) {
        return super.onBadRequest(requestHeader, s);
    }

    @Override
    public F.Promise<Result> onClientError(final Http.RequestHeader requestHeader, final int i, final String s) {
        return super.onClientError(requestHeader, i, s);
    }

    @Override
    protected F.Promise<Result> onDevServerError(final Http.RequestHeader requestHeader, final UsefulException e) {
        return super.onDevServerError(requestHeader, e);
    }

    @Override
    protected F.Promise<Result> onForbidden(final Http.RequestHeader requestHeader, final String s) {
        return super.onForbidden(requestHeader, s);
    }

    @Override
    protected F.Promise<Result> onNotFound(final Http.RequestHeader requestHeader, final String s) {
        return super.onNotFound(requestHeader, s);
    }

    @Override
    protected F.Promise<Result> onProdServerError(final Http.RequestHeader requestHeader, final UsefulException e) {
        return super.onProdServerError(requestHeader, e);
    }

    @Override
    public F.Promise<Result> onServerError(final Http.RequestHeader requestHeader, final Throwable throwable) {
        return super.onServerError(requestHeader, throwable);
    }
}
