package dialight.guilib.indexcache;

import dialight.guilib.slot.Vec2i;

/**
 *
 * Index cache, that implements spark page slot layout.
 * Every pair of indices crosses the center.
 *
 *    <------- W ------->
 *  ^ [ _ _ _ _ _ _ _ _ ]    [ _ _ _ _ _ _ _ ]    [ _ _ _ _ _ _ _ ]
 *  | [ _ _ _ _ _ _ _ _ ]    [ _ _ _ _ _ _ _ ]    [ _ _ _ _ _ _ _ ]
 *  | [ _ _ _ 8 _ _ _ _ ]    [ _ _ _ _ _ _ _ ]    [ _ _ _ _ _ _ _ ]
 *  H [ _ _ 4 0 2 6 _ _ ] -> [ _ _ _ _ _ _ _ ] -> [ _ _ _ _ _ _ _ ]
 *  | [ _ _ 7 3 1 5 _ _ ]    [ _ _ _ _ _ _ _ ]    [ _ _ _ _ _ _ _ ]
 *  | [ _ _ _ _ 9 _ _ _ ]    [ _ _ _ _ _ _ _ ]    [ _ _ _ _ _ _ _ ]
 *  | [ _ _ _ _ _ _ _ _ ]    [ _ _ _ _ _ _ _ ]    [ _ _ _ _ _ _ _ ]
 *  ⌄ [ _ _ _ _ _ _ _ _ ]    [ _ _ _ _ _ _ _ ]    [ _ _ _ _ _ _ _ ]
 *
 *  W - sparkWidth
 *  H - sparkHeight
 *
 */
public class SparkIndexCache extends IndexCache {

    public SparkIndexCache(int sparkWidth, int sparkHeight) {
        super(sparkWidth, sparkHeight);
    }

    protected void buildCache() {
        Builder bld = new Builder();
        bld.build();
    }

    private final class Builder {

        private final int rhalfw = width / 2;
        private final int lhalfw = -((width % 2) + rhalfw);
        private final int rhalfh = height / 2;
        private final int lhalfh = -((height % 2) + rhalfh);
        private int x = 0;
        private int y = 0;
        private int i = 0;
        private int left = 0;
        private int up = 0;

        void build() {
            if(((width % 2) != 0) && ((height % 2) != 0)) {
                flush();
            }
            while(left <= rhalfw || up <= rhalfh) {
                renderHorizontal();
                renderLeft();
                left++;
                renderVertical();
                renderUp();
                up++;
            }
        }
        void renderLeft() {
            for (int y = 0; y < up; y++) {
                this.x = -left;
                this.y = -y;
                if((height % 2) != 0) this.y--;
                if((width % 2) != 0) this.x--;
                sectorPoint();
            }
        }
        void renderUp() {
            for (int x = 0; x < left; x++) {
                this.x = -x;
                this.y = -up;
                if((height % 2) != 0) this.y--;
                if((width % 2) != 0) this.x--;
                sectorPoint();
            }
        }
        void sectorPoint() {
            flush();

            x = -x;
            y = -y;
            if((width % 2) == 0) x++;
            if((height % 2) == 0) y++;
            flush();
            if((width % 2) == 0) x--;
            if((height % 2) == 0) y--;

            y = -y;

            if((width % 2) == 0) x++;
            flush();
            if((width % 2) == 0) x--;

            x = -x;
            y = -y;
            if((height % 2) == 0) y++;
            flush();
            if((height % 2) == 0) y--;
        }
        void renderVertical() {
            if((width % 2) == 0) return;
            x = 0;
            y = -up;
            if((height % 2) != 0) y--;
            flush();
            y = -y;
            if((height % 2) == 0) y++;
            flush();
        }
        void renderHorizontal() {
            if((height % 2) == 0) return;
            x = -left;
            y = 0;
            if((width % 2) != 0) x--;
            flush();
            x = -x;
            if((width % 2) == 0) x++;
            flush();
        }
        void flush() {
            if(x <= lhalfw || x > rhalfw) return;
            if(y <= lhalfh || y > rhalfh) return;
            Vec2i loc = new Vec2i(x - lhalfw - 1, y - lhalfh - 1);
            Integer oldi = locCache[loc.x][loc.y];
            if(oldi != null) {
                throw new IllegalArgumentException("index collision");
            } else {
                locCache[loc.x][loc.y] = i;
                indexCache[i] = loc;
            }
            i++;
        }
    }

}
