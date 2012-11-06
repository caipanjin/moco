package com.github.dreamhead.moco;

import com.github.dreamhead.moco.handler.SequenceResponseHandler;
import com.github.dreamhead.moco.internal.MocoHttpServer;
import com.github.dreamhead.moco.matcher.*;
import com.github.dreamhead.moco.model.ContentStream;
import com.github.dreamhead.moco.model.Uri;
import com.github.dreamhead.moco.model.XPath;

import java.io.InputStream;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class Moco {
    public static MocoHttpServer httpserver(int port) {
        return new MocoHttpServer(port);
    }

    public static RequestMatcher by(String content) {
        return by(text(content));
    }

    public static RequestMatcher by(ContentStream stream) {
        return new ContentMatcher(stream.asByteArray());
    }

    public static RequestMatcher by(Uri uri) {
        return new UriRequestMatcher(uri.getUri());
    }

    public static RequestMatcher eq(XPath xpath, String expected) {
        return new XPathRequestMatcher(xpath, expected);
    }

    public static RequestMatcher and(RequestMatcher... matchers) {
        return new AndRequestMatcher(matchers);
    }

    public static RequestMatcher or(RequestMatcher... matchers) {
        return new OrRequestMatcher(matchers);
    }

    public static ContentStream text(String text) {
        return new ContentStream(text);
    }

    public static Uri uri(String uri) {
        return new Uri(uri);
    }

    public static XPath xpath(String xpath) {
        return new XPath(xpath);
    }

    public static ResponseHandler seq(String... contents) {
        List<ContentStream> streams = newArrayList();
        for (String content : contents) {
            streams.add(text(content));
        }

        return new SequenceResponseHandler(streams.toArray(new ContentStream[streams.size()]));
    }

    public static ResponseHandler seq(ContentStream... contents) {
        return new SequenceResponseHandler(contents);
    }

    public static ContentStream stream(InputStream is) {
        return new ContentStream(is);
    }

    public static void running(MocoHttpServer httpServer, Runnable runnable) {
        try {
            httpServer.start();
            runnable.run();
        } catch (Throwable t) {
            t.printStackTrace(System.err);
            throw new RuntimeException(t);
        } finally {
            httpServer.stop();
        }
    }
}