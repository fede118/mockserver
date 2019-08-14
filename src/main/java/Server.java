import com.google.gson.Gson;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.model.Delay;
import org.mockserver.model.Header;

import java.util.concurrent.TimeUnit;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class Server {
    static MockServerClient mockServer = startClientAndServer(8081);

    public static void consulta(String method, String path, int statusCode, String content, String body, long delay) {

        mockServer.when(
                request()
                        .withMethod(method)
                        .withPath(path)
        ).respond(
                response()
                        .withStatusCode(statusCode)
                        .withHeader(new Header("Content-Type", content))
                        .withBody(body)
                        .withDelay(new Delay(TimeUnit.MILLISECONDS, delay))
        );

    }

    public static void main(String[] args) {

        Gson gson = new Gson();

        Site[] sites = createMockedSites();

        Category[] categories = createMockedCategories();

        TokenResponse tokenResponse = new TokenResponse("ArnoldSchwartzenegger", "TOKEN1234567890MUSCULO");


        /**
         * crea un usuario con { user, pass }
         * devuelve el token y usuario
         */
        consulta("POST", "/login", 200, "application/json", gson.toJson(tokenResponse), 1000);

        /**
         * mockea todos los sites que en la implementacion de la api va a ser un llamado a la api del eze
         * devuelve todos los sites
         */
        consulta("GET", "/sites", 200, "application/json", gson.toJson(sites), 1000);

        /**
         * mockea las categorias que enrelidad despues va a hacer un pedido a la del eze
         *  devuelve todas las categorias
         */
        consulta("GET", "/sites/MLA/categories", 200, "application/json", gson.toJson(categories), 1000);

    }

    public static Site[] createMockedSites() {
        String[] ids = { "MLA", "MLB", "MLC", "MLD", "MLZ", "MLJ", "MLH", "MLQ", "MLY", "MLT" };
        String[] names = { "Argentina", "Brasil", "Colombia", "Dinamarca", "Zambia", "Jamaica", "Honduras", "Qatar", "Yolanda", "Tazmania" };

        Site[] sites = new Site[10];

        for (int i = 0; i < 10; i++) {
            sites[i] = new Site(ids[i], names[i]);
        }

        return sites;
    }

    public static Category[] createMockedCategories() {
        String[] ids = {"MLA100", "MLA101", "MLA102", "MLA103", "MLA104", "MLA105", "MLA106", "MLA107", "MLA108", "MLA109"};
        String[] names = {"Accesorios", "Libros", "Cosas", "Ranas", "Sillas", "Osos", "Poderosos", "Sueros", "Ezequieles", "Rumba"};

        Category[] categories = new Category[10];

        for (int i = 0; i < 10; i++) {
            categories[i] = new Category(ids[i], names[i]);
        }

        return categories;
    }

}
