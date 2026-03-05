package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;


@WebServlet(name = "CurrencyConverterServlet", urlPatterns = "/currency/convert")
public class CurrencyConverterServlet extends HttpServlet {
    Logger LOGGER = Logger.getLogger(CurrencyConverterServlet.class.getName());

    private static final String CURRENCY_CONVERTER_API_KEY = "your_api_key";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // converting
        LOGGER.log(Level.INFO, request.getParameter("amount"));
        LOGGER.log(Level.INFO, request.getParameter("to"));
        LOGGER.log(Level.INFO, request.getParameter("from"));
        String amount = request.getParameter("amount");
        String toCurrency = request.getParameter("to");
        String fromCurrency = request.getParameter("from");

        String apiUrl = String.format(
                "https://v6.exchangerate-api.com/v6/%s/pair/%s/%s/%s",
                CURRENCY_CONVERTER_API_KEY,
                URLEncoder.encode(fromCurrency, "UTF-8"),
                URLEncoder.encode(toCurrency, "UTF-8"),
                URLEncoder.encode(amount, "UTF-8")
        );
        LOGGER.info(apiUrl);

        URL url = new URL(apiUrl);
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setRequestMethod("GET");
        huc.setConnectTimeout(5000);
        huc.setReadTimeout(5000);

        BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        LOGGER.log(Level.INFO, sb.toString());
        response.getWriter().write(sb.toString());

    }
}
