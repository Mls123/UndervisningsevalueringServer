import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import logic.ConfigLoader;
import logic.TeacherController;
import shared.AdminDTO;
import shared.Logging;
import view.TUIMainMenu;

public class Run {

    public static void main(String[] args) throws IOException {

        HttpServer server = null;

        /**
         * Loader configfilen
         */
        ConfigLoader.parseConfig();

        try {
            PrintStream stdout = System.out;
            System.setOut(null);
            /**
             * Her oprettes serverens URL
             */
            server = HttpServerFactory.create("http://" + ConfigLoader.SERVER_ADDRESS + ":" + ConfigLoader.SERVER_PORT + "/");
            System.setOut(stdout);
        }catch(ArrayIndexOutOfBoundsException a){
            /**
             * Hvis en ArrayIndexOutOfBoundsException sker, så sørger linje 34 for at det bliver logget
             */
            Logging.log(a, 3, "Fejl. Sysem startede ikke!");
            System.exit(20);
        }

        server.start();

        //Setup logLevel and prepare to log
        Logging.initiateLog(ConfigLoader.DEBUG);

        try {
            /**
             * Parseren loader alle API infomrtaionerne ind i databasen, denne er udkommenteret da der mangler en exception der validere om dataen allerede findes.
             */
            //CBSParser.parseCBSData();
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println("Server running");
        System.out.println("Visit: http://" + ConfigLoader.SERVER_ADDRESS + ":" + ConfigLoader.SERVER_PORT + "/");

        AdminDTO adminDTO = new AdminDTO();
        TUIMainMenu tuiMainMenu = new TUIMainMenu();
        tuiMainMenu.tUILogIn(adminDTO);

        System.in.read();
        System.out.println("Hit return to stop...");
        System.out.println("Stopping server");
        System.out.println("Server stopped");
        System.out.println();
    }

}
