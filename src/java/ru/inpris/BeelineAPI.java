package ru.inpris;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.security.cert.X509Certificate;

@WebServlet(name = "BeelineAPI", urlPatterns = "/api")
public class BeelineAPI extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        StringBuilder targetUrl = new StringBuilder("https://cloudpbx.beeline.ru/apis/portal/abonents/");
        targetUrl.append(request.getParameter("numFrom"));
        targetUrl.append("/call?phoneNumber=");
        targetUrl.append(request.getParameter("numTo"));

        System.out.println("targetUrl: " + targetUrl);

        TrustManager[] trustManager = new TrustManager[] {
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
                    }

                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };

        try {
            SSLContext sc = SSLContext.getInstance("ssl");
            sc.init(null, trustManager, null);
            Client client = ClientBuilder.newBuilder().sslContext(sc).build();

            final WebTarget target = client.target(targetUrl.toString());
            Response res = target.request().header("X-MPBX-API-AUTH-TOKEN", request.getParameter("token")).method("POST");
            System.out.println(res.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
