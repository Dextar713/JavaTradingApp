package backend.services;

import backend.models.Asset;
import backend.repositories.IAssetRepo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;


public class PriceUpdateService {
    private final String apiUrl = "https://api.api-ninjas.com/v1/cryptoprice?symbol=";
    private final IAssetRepo assetRepo;
    private long updateFrequency;

    public PriceUpdateService(IAssetRepo assetRepo) {
        this.assetRepo = assetRepo;
        updateFrequency = 60;
    }

    public double getUpdateFrequency() {
        return updateFrequency;
    }

    public void setUpdateFrequency(long updateFrequency) {
        this.updateFrequency = updateFrequency;
    }

    private void updateSymbol(Asset asset) throws ProtocolException {
        URL url = null;
        try {
            url = new URI(apiUrl+asset.getName()+"USDT").toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = null;
        try {
            assert url != null;
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert connection != null;
        connection.setRequestMethod("GET");
        connection.setRequestProperty("accept", "application/json");
        String apiKey = "n8oOTuXl0vMoX7Lzg48lHg==XT3hmxsa6FKCVdxG";
        connection.setRequestProperty("X-Api-Key", apiKey);
        InputStream responseStream = null;
        try {
            responseStream = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert responseStream != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
        StringBuilder response = new StringBuilder();

        String line;
        while (true) {
            try {
                line = reader.readLine();
                if(line == null || line.isEmpty()) {
                    break;
                }
                response.append(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //System.out.println("Response: " + response.toString());
        JsonReader jsonReader = Json.createReader(new StringReader(response.toString()));
        JsonObject jsonObject = jsonReader.readObject();
        //String symbolRaw = jsonObject.getString("symbol");
        //String symbol = symbolRaw.substring(0, symbolRaw.length()-4);
        double price = Double.parseDouble(jsonObject.getString("price"));
        System.out.println(asset.getName() + " " + price);
        assetRepo.updateAssetPrice(asset.getId(), price);
    }

    public void startUpdates() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                List<Asset> assets = assetRepo.getAllAssets();
                for (Asset asset : assets) {
                    try {
                        updateSymbol(asset);
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, 1000*updateFrequency);
    }
}
