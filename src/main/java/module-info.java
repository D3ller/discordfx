module org.karisben.discord {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires jdk.httpserver;


    opens org.discordfx.client to javafx.fxml;
    exports org.discordfx.client;
    exports org.discordfx.client.http;
    opens org.discordfx.client.http to javafx.fxml;
    exports org.discordfx.client.models;
    opens org.discordfx.client.models to javafx.fxml;
    exports org.discordfx.client.controllers;
    opens org.discordfx.client.controllers to javafx.fxml;
}