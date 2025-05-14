
import java.util.*;
import java.util.stream.Stream;


class ConfigParser {
    private Map<String, String> config;
    ConfigParser(Map<String,String> config){
        this.config = config;
    }
    protected Optional<Integer> getIntValue(String key) {
        // Return an Optional<Integer> parsed from the string value for the key
        Optional<Integer> parsedKey = Optional.ofNullable(Optional.ofNullable(config.get(key)).map(n -> Integer.parseInt(n)).orElse(null));
        return parsedKey;
    }
    protected String getStringValue(String key, String defaultValue) {
        // Return the string value for the key or the default value if absent
        return Optional.ofNullable(config.get(key)).orElse(defaultValue);
    }
}

class ConfigurationParser {
    public static void main(String[] args) {
        Map<String,String> configdb = new HashMap<>();
        configdb.put("server.host", "localhost");
        configdb.put("server.port", "8080");
        configdb.put("database.url", "jdbc:h2:mem:testdb");
        configdb.put("database.username", "samual");
        configdb.put("database.password", "sam@123");
        configdb.put("api.timeout", "5000");
        configdb.put("log.level", "INFO");
        configdb.put("retry.attempts", "3");
        configdb.put("default.value", "fallback");
        ConfigParser configParser = new ConfigParser(configdb);

        Optional<Integer> configParserIntValue = configParser.getIntValue("api.timeout");
        System.out.println("api.timeout (Integer) " + configParserIntValue);
        String configParserStringValue = configParser.getStringValue("database.username","root");
        System.out.println("database.username (String) "+configParserStringValue);

        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        Stream<String> nameStream = names.stream();
        System.out.println(nameStream);

    }
}
