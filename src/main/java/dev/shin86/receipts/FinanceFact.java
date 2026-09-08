package dev.shin86.receipts;

import com.google.gson.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

final class FinanceFact {
    final String name, period, source, retrievedAt;
    final double receipts;
    FinanceFact(String name, String period, String source, String retrievedAt, double receipts) {
        this.name = name; this.period = period; this.source = source; this.retrievedAt = retrievedAt; this.receipts = receipts;
    }
    static List<FinanceFact> load() throws IOException {
        InputStream stream = FinanceFact.class.getResourceAsStream("/politicians.json");
        if (stream == null) { throw new IOException("Bundled FEC data is missing"); }
        try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            JsonObject root = new Gson().fromJson(reader, JsonObject.class);
            String retrieved = root.getAsJsonObject("meta").get("retrievedAt").getAsString();
            List<FinanceFact> facts = new ArrayList<>();
            for (JsonElement element : root.getAsJsonArray("politicians")) {
                JsonObject person = element.getAsJsonObject();
                JsonObject finance = person.getAsJsonObject("finance");
                if (!"available".equals(finance.get("status").getAsString())) { continue; }
                facts.add(new FinanceFact(person.get("name").getAsString(),
                    finance.get("coverageStart").getAsString() + " to " + finance.get("coverageEnd").getAsString(),
                    person.get("sourceUrl").getAsString(), retrieved, finance.get("receipts").getAsDouble()));
            }
            return Collections.unmodifiableList(facts);
        }
    }
}
