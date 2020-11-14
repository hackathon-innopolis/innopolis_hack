package com.voronin.jinn;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Network {
    URL url = new URL("192.168.88.29");

    public Network() throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
    }

}
