package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import org.w3c.dom.Document;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.F;
import play.libs.ws.*;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.*;

public class WebServicesTest {
    public static class WsUsingController extends Controller {
        private final WSAPI wsapi;

        @Inject
        public WsUsingController(final WSAPI wsapi) {
            this.wsapi = wsapi;
        }

        public F.Promise<Result> index() {
            return wsapi.url("http://google.de").get()
                    .map(res -> ok(res.getBody()));
        }
    }

    //uses ning
    @Test
    public void usingWs() throws Exception {
        final Application app = new GuiceApplicationBuilder().build();
        running(app, () -> {
            final WsUsingController controller = app.injector().instanceOf(WsUsingController.class);
            final Result result = controller.index().get(10000);
            assertThat(contentAsString(result))
                    .containsIgnoringCase("google")
                    .doesNotContain("hduhuishdvudshivdr77h347hv745hv747h75h75");
        });
    }

    //uses stub
    //but you could also inject a mocked object
    @Test
    public void usingWsStub() throws Exception {
        //or use injector and bind a
        final WsUsingController controller = new WsUsingController(new CustomWSAPI());
        final Result result = controller.index().get(100);
        assertThat(contentAsString(result))
                .contains("hduhuishdvudshivdr77h347hv745hv747h75h75");
    }

    private static class CustomWSAPI implements WSAPI {
        @Override
        public WSClient client() {
            throw new UnsupportedOperationException();
        }

        @Override
        public WSRequestHolder url(final String s) {
            return new WSRequestHolder() {

                @Override
                public F.Promise<WSResponse> get() {
                    return F.Promise.pure(new WSResponse() {
                        @Override
                        public String getBody() {
                            return "hduhuishdvudshivdr77h347hv745hv747h75h75";
                        }

                        @Override
                        public JsonNode asJson() {
                            return null;
                        }

                        @Override
                        public Map<String, List<String>> getAllHeaders() {
                            return null;
                        }

                        @Override
                        public Object getUnderlying() {
                            return null;
                        }

                        @Override
                        public int getStatus() {
                            return 0;
                        }

                        @Override
                        public String getStatusText() {
                            return null;
                        }

                        @Override
                        public String getHeader(final String s) {
                            return null;
                        }

                        @Override
                        public List<WSCookie> getCookies() {
                            return null;
                        }

                        @Override
                        public WSCookie getCookie(final String s) {
                            return null;
                        }

                        @Override
                        public Document asXml() {
                            return null;
                        }

                        @Override
                        public InputStream getBodyAsStream() {
                            return null;
                        }

                        @Override
                        public byte[] asByteArray() {
                            return new byte[0];
                        }

                        @Override
                        public URI getUri() {
                            return null;
                        }
                    });
                }

                @Override
                public String getUsername() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public String getPassword() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public WSAuthScheme getScheme() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public WSSignatureCalculator getCalculator() {
                    throw new UnsupportedOperationException();
                }

                public int getTimeout() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public long getRequestTimeout() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public Boolean getFollowRedirects() {
                    throw new UnsupportedOperationException();
                }

                public F.Promise<WSResponse> patch(final File s) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public F.Promise<WSResponse> patch(final String s) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public F.Promise<WSResponse> post(final String s) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public F.Promise<WSResponse> put(final String s) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public F.Promise<WSResponse> patch(final JsonNode jsonNode) {
                    return null;
                }

                @Override
                public F.Promise<WSResponse> post(final JsonNode jsonNode) {
                    return null;
                }

                @Override
                public F.Promise<WSResponse> put(final JsonNode jsonNode) {
                    return null;
                }

                @Override
                public F.Promise<WSResponse> patch(final InputStream inputStream) {
                    return null;
                }

                @Override
                public F.Promise<WSResponse> post(final InputStream inputStream) {
                    return null;
                }

                @Override
                public F.Promise<WSResponse> put(final InputStream inputStream) {
                    return null;
                }

                @Override
                public F.Promise<WSResponse> post(final File file) {
                    return null;
                }

                @Override
                public F.Promise<WSResponse> put(final File file) {
                    return null;
                }

                @Override
                public F.Promise<WSResponse> delete() {
                    return null;
                }

                @Override
                public F.Promise<WSResponse> head() {
                    return null;
                }

                @Override
                public F.Promise<WSResponse> options() {
                    return null;
                }

                @Override
                public F.Promise<WSResponse> execute(final String s) {
                    return null;
                }

                @Override
                public F.Promise<WSResponse> execute() {
                    return null;
                }

                @Override
                public WSRequestHolder setMethod(final String s) {
                    return null;
                }

                @Override
                public WSRequestHolder setBody(final String s) {
                    return null;
                }

                @Override
                public WSRequestHolder setBody(final JsonNode jsonNode) {
                    return null;
                }

                @Override
                public WSRequestHolder setBody(final InputStream inputStream) {
                    return null;
                }

                @Override
                public WSRequestHolder setBody(final File file) {
                    return null;
                }

                @Override
                public WSRequestHolder setHeader(final String s, final String s1) {
                    return null;
                }

                @Override
                public WSRequestHolder setQueryString(final String s) {
                    return null;
                }

                @Override
                public WSRequestHolder setQueryParameter(final String s, final String s1) {
                    return null;
                }

                @Override
                public WSRequestHolder setAuth(final String s) {
                    return null;
                }

                @Override
                public WSRequestHolder setAuth(final String s, final String s1) {
                    return null;
                }

                @Override
                public WSRequestHolder setAuth(final String s, final String s1, final WSAuthScheme wsAuthScheme) {
                    return null;
                }

                @Override
                public WSRequestHolder sign(final WSSignatureCalculator wsSignatureCalculator) {
                    return null;
                }

                @Override
                public WSRequestHolder setFollowRedirects(final Boolean aBoolean) {
                    return null;
                }

                @Override
                public WSRequestHolder setVirtualHost(final String s) {
                    return null;
                }

                public WSRequestHolder setTimeout(final int i) {
                    return null;
                }

                @Override
                public WSRequestHolder setRequestTimeout(final long l) {
                    return null;
                }

                @Override
                public WSRequestHolder setContentType(final String s) {
                    return null;
                }

                @Override
                public String getUrl() {
                    return null;
                }

                @Override
                public Map<String, Collection<String>> getHeaders() {
                    return null;
                }

                @Override
                public Map<String, Collection<String>> getQueryParameters() {
                    return null;
                }
            };
        }
    }
}
