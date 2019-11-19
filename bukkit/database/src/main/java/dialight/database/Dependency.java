package dialight.database;

import com.google.common.collect.ImmutableList;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.List;

public class Dependency {

    private static final String MAVEN_CENTRAL_REPO = "https://repo1.maven.org/maven2/";
    private static final String MAVEN_FORMAT = "%s/%s/%s/%s-%s.jar";

    private final List<URL> urls;
    private final String version;
    private final byte[] checksum;
    private final String name;

    Dependency(String name, String groupId, String artifactId, String version, String checksum) {
        this.name = name;
        String path = String.format(MAVEN_FORMAT,
                rewriteEscaping(groupId).replace(".", "/"),
                rewriteEscaping(artifactId),
                version,
                rewriteEscaping(artifactId),
                version
        );
        try {
            this.urls = ImmutableList.of(
                    new URL(MAVEN_CENTRAL_REPO + path)
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e); // propagate
        }
        this.version = version;
        this.checksum = Base64.getDecoder().decode(checksum);
    }

    private static String rewriteEscaping(String s) {
        return s.replace("{}", ".");
    }

    public List<URL> getUrls() {
        return this.urls;
    }

    public String getVersion() {
        return this.version;
    }

    public byte[] getChecksum() {
        return this.checksum;
    }

    public String getName() {
        return this.name;
    }

}
