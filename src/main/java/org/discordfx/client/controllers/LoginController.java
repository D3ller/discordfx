package org.discordfx.client.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.discordfx.client.http.ApiClient;
import org.discordfx.client.services.AuthService;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    TextField totpCode;

    @FXML
    private VBox basePanel;

    @FXML
    private VBox totpPanel;

    @FXML
    protected void onFormSubmit() throws IOException {

        final String email = emailField.getText().trim();
        final String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            return;
        }

        ApiClient apiClient = new ApiClient();
        AuthService authService = new AuthService(apiClient);

        var login = authService.login(email, password);
        login.thenAccept(authResult -> {
            System.out.println(authResult);
            Platform.runLater(() -> {
                if (authResult.requiresMfa()) {
                    totpPanel.setVisible(true);
                    totpPanel.setManaged(true);

                    basePanel.setVisible(false);
                    basePanel.setManaged(false);
                }
            });
        });
    }

    protected void onTotpSubmit() {

    }
}