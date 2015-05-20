package thenewglobals;

import play.Logger;
import play.http.DefaultHttpRequestHandler;
import play.mvc.Action;
import play.mvc.Http;

import java.lang.reflect.Method;

public class CustomHttpRequestHandler extends DefaultHttpRequestHandler {
    @Override
    public Action createAction(final Http.Request request, final Method method) {
        Logger.info("request " + request + " " + method);
        return super.createAction(request, method);
    }
}
