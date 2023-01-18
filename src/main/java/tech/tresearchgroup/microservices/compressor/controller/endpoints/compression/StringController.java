package tech.tresearchgroup.microservices.compressor.controller.endpoints.compression;

import io.activej.http.HttpRequest;
import io.activej.http.HttpResponse;
import io.activej.promise.Promisable;
import org.jetbrains.annotations.NotNull;
import tech.tresearchgroup.libraries.compression.controller.StringCompressionController;
import tech.tresearchgroup.microservices.compressor.controller.CompressionController;
import tech.tresearchgroup.schemas.compression.model.CompressionMethodEnum;

import java.io.ByteArrayInputStream;
import java.util.Map;

public class StringController {
    public static Promisable<HttpResponse> compress(CompressionMethodEnum method, HttpRequest httpRequest) {
        try {
            String string = httpRequest.getQueryParameter("string");
            if(string == null) {
                return HttpResponse.ofCode(500);
            }
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(string.getBytes());
            byte[] data = CompressionController.compress(byteArrayInputStream, method);
            byteArrayInputStream.close();
            if(data == null) {
                return HttpResponse.ofCode(500);
            }
            System.out.println(string.length() + " : " + data.length);
            return HttpResponse.ok200().withBody(data);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResponse.ofCode(500);
        }
    }

    public static Promisable<HttpResponse> getBestCompressor(@NotNull HttpRequest httpRequest) {
        try {
            String string = httpRequest.getQueryParameter("string");
            if(string != null) {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(string.getBytes());
                Map<String, Double> results = CompressionController.getBestCompressor(byteArrayInputStream);
                byteArrayInputStream.close();
                return HttpResponse.ok200().withBody(results.toString().getBytes());
            }
            return HttpResponse.ofCode(400);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HttpResponse.ofCode(500);
    }

    public static Promisable<HttpResponse> smazCompress(@NotNull HttpRequest httpRequest) {
        return HttpResponse.ok200().withBody(StringCompressionController.smazCompress(httpRequest.getQueryParameter("string")));
    }
}
