package dialight.database;

import com.google.common.io.ByteStreams;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DependencyManager {

    private final MessageDigest digest;

    public DependencyManager() {
        try {
            this.digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        DependencyManager dependencyManager = new DependencyManager();

        for (Dependency dependency : Arrays.asList(
                Dependencies.MYSQL_DRIVER
        )) {
            try {
                Path libs = Paths.get("libs");
                if(!Files.exists(libs)) Files.createDirectory(libs);
                Path path = dependencyManager.downloadDependency(libs, dependency);
                System.out.println("downloaded " + path);
//                sources.add(new Source(dependency, file));
            } catch (Throwable e) {
//                this.plugin.getLogger().severe("Exception whilst downloading dependency " + dependency.getName());
                e.printStackTrace();
            }
        }
    }

    public Path downloadDependency(Path saveDirectory, Dependency dependency) throws Exception {
        String fileName = dependency.getName().toLowerCase() + "-" + dependency.getVersion() + ".jar";
        Path file = saveDirectory.resolve(fileName);

        // if the file already exists, don't attempt to re-download it.
        if (Files.exists(file)) {
            return file;
        }

        boolean success = false;
        Exception lastError = null;

        // getUrls returns two possible sources of the dependency.
        // [0] is a mirror of Maven Central, used to reduce load on central. apparently they don't like being used as a CDN
        // [1] is Maven Central itself

        // side note: the relative "security" of the mirror is less than central, but it actually doesn't matter.
        // we compare the downloaded file against a checksum here, so even if the mirror became compromised, RCE wouldn't be possible.
        // if the mirror download doesn't match the checksum, we just try maven central instead.

        List<URL> urls = dependency.getUrls();
        for (int i = 0; i < urls.size() && !success; i++) {
            URL url = urls.get(i);

            try {
                URLConnection connection = url.openConnection();

                // i == 0 when we're trying to use the mirror repo.
                // set some timeout properties so when/if this repository goes offline, we quickly fallback to central.
                if (i == 0) {
                    connection.setRequestProperty("User-Agent", "eventhelper");
                    connection.setConnectTimeout((int) TimeUnit.SECONDS.toMillis(5));
                    connection.setReadTimeout((int) TimeUnit.SECONDS.toMillis(10));
                }

                try (InputStream in = connection.getInputStream()) {
                    // download the jar content
                    byte[] bytes = ByteStreams.toByteArray(in);
                    if (bytes.length == 0) {
                        throw new RuntimeException("Empty stream");
                    }


                    // compute a hash for the downloaded file
                    byte[] hash = this.digest.digest(bytes);

                    // ensure the hash matches the expected checksum
                    if (!Arrays.equals(hash, dependency.getChecksum())) {
                        throw new RuntimeException("Downloaded file had an invalid hash. " +
                                "Expected: " + Base64.getEncoder().encodeToString(dependency.getChecksum()) + " " +
                                "Actual: " + Base64.getEncoder().encodeToString(hash));
                    }

                    // if the checksum matches, save the content to disk
                    Files.write(file, bytes);
                    success = true;
                }
            } catch (Exception e) {
                lastError = e;
            }
        }

        if (!success) {
            throw new RuntimeException("Unable to download", lastError);
        }

        // ensure the file saved correctly
        if (!Files.exists(file)) {
            throw new IllegalStateException("File not present: " + file.toString());
        } else {
            return file;
        }
    }

}
