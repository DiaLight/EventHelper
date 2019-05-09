package dialight.guilib.indexcache;

import dialight.guilib.slot.Vec2i;

/**
 *
 * Index cache, that implements spiral page style.
 *
 *    <------- W ------->
 *  ^ [ _ _ _ _ _ _ _ _ ]    [ _ _ _ _ _ _ _ ]    [ _ _ _ _ _ _ _ ]
 *  | [ _ _ _ _ _ _ _ _ ]    [ _ _ _ _ _ _ _ ]    [ _ _ _ _ _ _ _ ]
 *  | [ _ _ 6 7 8 9 _ _ ]    [ _ _ _ _ _ _ _ ]    [ _ _ _ _ _ _ _ ]
 *  H [ _ _ 5 0 1 . _ _ ] -> [ _ _ _ _ _ _ _ ] -> [ _ _ _ _ _ _ _ ]
 *  | [ _ _ 4 3 2 . _ _ ]    [ _ _ _ _ _ _ _ ]    [ _ _ _ _ _ _ _ ]
 *  | [ _ _ _ _ _ . _ _ ]    [ _ _ _ _ _ _ _ ]    [ _ _ _ _ _ _ _ ]
 *  | [ _ _ _ _ _ _ _ _ ]    [ _ _ _ _ _ _ _ ]    [ _ _ _ _ _ _ _ ]
 *  ⌄ [ _ _ _ _ _ _ _ _ ]    [ _ _ _ _ _ _ _ ]    [ _ _ _ _ _ _ _ ]
 *
 *  W - spiralWidth
 *  H - spiralHeight
 *
 */
public class SpiralIndexCache extends IndexCache {

    public SpiralIndexCache(int spiralWidth, int spiralHeight) {
        super(spiralWidth, spiralHeight);
    }

    protected void buildCache() {
        int rhalfw = width / 2;
        int lhalfw = -((width % 2) + rhalfw);
        int rhalfh = height / 2;
        int lhalfh = -((height % 2) + rhalfh);
        int x = 0;
        int y = 0;
        int segment_length = 1;
        int segment_passed = 0;
        int dx = 1;
        int dy = 0;
        boolean right = true;
        if((width % 2) != 0 && (height % 2 == 0)) {
            dx = 0;
            dy = 1;
            right = false;
        }
        int sq = Math.max(width, height);
        sq = sq * sq;
        int i = 0;
        for (int j = 0; j < sq; j++) {
            if((lhalfw < x && x <= rhalfw) && (lhalfh < y && y <= rhalfh)) {
                Vec2i loc = new Vec2i(x - lhalfw - 1, y - lhalfh - 1);
                locCache[loc.x][loc.y] = i;
                indexCache[i] = loc;
                i++;
            }
            x += dx;
            y += dy;
            segment_passed++;
            if (segment_passed == segment_length) {
                segment_passed = 0;
                int tmp = dx;
                dx = -dy;
                dy = tmp;
                if ((right ? dy : dx) == 0) segment_length++;
            }
        }
    }

}
