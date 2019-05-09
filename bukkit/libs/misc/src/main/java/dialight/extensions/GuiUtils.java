package dialight.extensions;

import dialight.nms.ReflectionUtils;

public class GuiUtils {

    public static final String BACKWARD_NBT;
    public static final String FORWARD_NBT;

    static {
        String backwardTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQxZGQxMjc1OTVhMjVjMjQzOWM1ZGIzMWNjYjQ5MTQ1MDdhZTE2NDkyMWFhZmVjMmI5NzlhYWQxY2ZlNyJ9fX0=";
        String forwardTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDJkMDMxM2I2NjgwMTQxMjg2Mzk2ZTcxYzM2MWU1OTYyYTM5YmFmNTk2ZDdlNTQ3NzE3NzVkNWZhM2QifX19";
        if(ReflectionUtils.MINOR_VERSION < 8) {
            throw new RuntimeException("Uncompatible version");
        } else if(ReflectionUtils.MINOR_VERSION < 13) {
            BACKWARD_NBT = "{SkullOwner:{Id:\"378a4a5a-aaa1-41de-a6e7-2bd0844cc73e\",Properties:{textures:[{Value:\"" + backwardTexture + "\"}]}}}";
            FORWARD_NBT = "{SkullOwner:{Id:\"03cf7cdd-a24f-4bdc-9a69-ab63c39dba95\",Properties:{textures:[{Value:\"" + forwardTexture + "\"}]}}}";
        } else {
            BACKWARD_NBT = "{SkullOwner:{Id:\"378a4a5a-aaa1-41de-a6e7-2bd0844cc73e\",Properties:{textures:[{Value:\"" + backwardTexture + "\"}]}}}";
            FORWARD_NBT = "{SkullOwner:{Id:\"03cf7cdd-a24f-4bdc-9a69-ab63c39dba95\",Properties:{textures:[{Value:\"" + forwardTexture + "\"}]}}}";
        }
    }
}
