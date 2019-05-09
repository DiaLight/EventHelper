package dialight.guilib.indexcache;

import dialight.guilib.slot.Vec2i;
import org.jetbrains.annotations.Nullable;

/**
 * Linear discrete space to 2D discrete space bidirectional converter. Both spaces are finite.
 */
public abstract class IndexCache {

    protected final int width;
    protected final int height;

    protected final Integer[][] locCache;
    protected final Vec2i[] indexCache;

    public IndexCache(int width, int height) {
        this.width = width;
        this.height = height;
        this.locCache = new Integer[width][height];
        this.indexCache = new Vec2i[width * height];
        buildCache();
    }

    protected abstract void buildCache();

    @Nullable public Vec2i getLoc(int index) {
        if(index < 0) return null;
        if(index >= indexCache.length) return null;
        return indexCache[index];
    }

    public int getIndex(int x, int y) {
        if(x < 0) return -1;
        if(x >= width) return -1;
        if(y < 0) return -1;
        if(y >= height) return -1;
        Integer index = locCache[x][y];
        if(index == null) return -1;
        return index;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
