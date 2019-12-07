package dialight.extensions;

import dialight.misc.Colorizer;
import dialight.misc.Hex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class UuidEx {

    private final UUID uuid;

    private UuidEx(UUID uuid) {
        this.uuid = uuid;
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES * 2);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return buffer.array();
    }

    public static UUID fromBytes(byte[] bytes) {
        return new UUID(bytesToLong(bytes, 0, 8), bytesToLong(bytes, 8, 8));
    }

    public static long bytesToLong(byte[] bytes, int offset, int size) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes, offset, size);
        buffer.flip();  // need flip
        return buffer.getLong();
    }

    public static UuidEx of(UUID uuid) {
        return new UuidEx(uuid);
    }

    public static void main(String[] args) {
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
        byte[] bytes = UuidEx.of(uuid).toBytes();
        System.out.println(UuidEx.fromBytes(bytes));
    }

    public List<String> toLore() {
        String hex = Hex.encode(toBytes()).toLowerCase();
        return Arrays.asList(Colorizer.apply(
                "|w|uuid|y|: |w|",
                "|y| " + hex.substring(0, 16),
                "|y| " + hex.substring(16, 32)
        ));
    }
}
