package com.iambillmeyer.apps;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import io.prometheus.client.exporter.HTTPServer;

public class ExampleExporter {

    static final Gauge g = Gauge.build().namespace("example").name("gauge").help("blah").register();
    static final Counter c = Counter.build().namespace("example").name("counter").help("meh").register();
    static final Summary s = Summary.build().namespace("example").name("summary").help("meh").register();
    static final Histogram h = Histogram.build().namespace("example").name("histogram").help("meh").register();
    static final Gauge l = Gauge.build().namespace("example").name("labels").help("blah").labelNames("l").register();

    public static void main(String[] args) throws Exception {
        new HTTPServer(1234);
        g.set(1);
        c.inc(2);
        s.observe(3);
        h.observe(4);
        l.labels("foo").inc(5);
    }
}