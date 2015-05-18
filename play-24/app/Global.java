import play.GlobalSettings;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http;

import java.lang.reflect.Method;

public class Global extends GlobalSettings {
    @Override
    public Action onRequest(final Http.Request request, final Method method) {
        Logger.info("request " + request);
        return super.onRequest(request, method);
    }
}
