public class Rectangle {

    public static Rectangle copy(final Rectangle toCopy) {
        return Rectangle.newRectangle(toCopy.x, toCopy.y, toCopy.width, toCopy.height);
    }
    public static Rectangle intersection(final Rectangle... rectangles) {
        if (rectangles.length == 0) {
            return null;
        }
        Rectangle res = rectangles[0];
        for (int i = 1; i < rectangles.length; i++) {
            res = Rectangle.singleIntersection(res, rectangles[i]);
            if (res == null) {
                return null;
            }
        }
        return res;
    }
    public static Rectangle newRectangle(
        final int x,
        final int y,
        final int width,
        final int height
    ) {
        if (width < 0 || height < 0) {
            Utility.error(
                String.format(
                    "Invalid rectangle dimensions: height %d, width %d",
                    height,
                    width
                )
            );
            return null;
        }
        final Rectangle res = new Rectangle();
        res.x = x;
        res.y = y;
        res.width = width;
        res.height = height;
        return res;
    }
    public static Rectangle union(final Rectangle... rectangles) {
        if (rectangles.length == 0) {
            return null;
        } else {
            Rectangle res = rectangles[0];
            for (int i = 1; i < rectangles.length; i++) {
                res = Rectangle.singleUnion(res, rectangles[i]);
            }
            return res;
        }
    }

    private static Rectangle singleIntersection(
        final Rectangle aRect,
        final Rectangle anotherRect
    ) {
        final int nx = Utility.max(aRect.x, anotherRect.x);
        final int ny = Utility.max(aRect.y, anotherRect.y);
        final int nw =
            Utility.min(aRect.x + aRect.width, anotherRect.x + anotherRect.width) - nx;
        final int nh =
            Utility.min(aRect.y + aRect.height, anotherRect.y + anotherRect.height) - ny;
        if (nw < 0 || nh < 0) {
            return null;
        }
        return Rectangle.newRectangle(nx, ny, nw, nh);
    }

    private static Rectangle singleUnion(
        final Rectangle aRect,
        final Rectangle anotherRect
    ) {
        final int nx = Utility.min(aRect.x, anotherRect.x);
        final int ny = Utility.min(aRect.y, anotherRect.y);
        final int nw =
            Utility.max(aRect.x + aRect.width, anotherRect.x + anotherRect.width) - nx;
        final int nh =
            Utility.max(aRect.y + aRect.height, anotherRect.y + anotherRect.height) - ny;
        return Rectangle.newRectangle(nx, ny, nw, nh);
    }

    private int height;

    private int width;

    private int x;

    private int y;

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void performAction(final RectangleAction action) {
        switch (action) {
            case RAISE:
                this.height += 10;
                break;
            case LOWER:
                if (this.height >= 10) {
                    this.height -= 10;
                }
                break;
            case NARROW:
                if (this.width >= 10) {
                    this.width -= 10;
                }
                break;
            case WIDEN:
                this.width += 10;
                break;
            case ROTATE_RIGHT:
            case ROTATE_LEFT:
                // nothing to do
                break;
            default:
                Utility.error("Unknown action " + action);
                break;
        }
    }

    public void setHeight(final int height) {
        if (height < 0) {
            Utility.error("Trying to set height to negative value " + height + "!");
        } else {
            this.height = height;
        }
    }

    public void setWidth(final int width) {
        if (width < 0) {
            Utility.error("Trying to set width to negative value " + width + "!");
        } else {
            this.width = width;
        }
    }

    public void setX(final int x) {
        this.x = x;
    }

    public void setY(final int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        String res = "";
        for (int yp = 0; yp < this.y + this.height; yp++) {
            for (int xp = 0; xp < this.x + this.width; xp++) {
                if (this.contains(xp, yp)) {
                    res += "#";
                } else {
                    res += "_";
                }
            }
            res += "\n";
        }
        return res;
    }

    private boolean contains(final double x, final double y) {
        return (this.x <= x) &&
                (x <= this.x + this.width) &&
                (this.y <= y) &&
                (y <= this.y + this.height);
    }
}
