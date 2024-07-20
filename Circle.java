public class Circle {

    public static Circle newCircle(final int x, final int y, final int radius) {
        if (radius < 0) {
            Utility.error("Trying to create circle with negative radius " + radius + "!");
            return null;
        }
        final Circle res = new Circle();
        res.x = x;
        res.y = y;
        res.radius = radius;
        return res;
    }
    public static double size(final Circle... circles) {
        double res = 0;
        for (final Circle r : circles) {
            res += r.singleSize();
        }
        return res;
    }
    private int radius;

    private int x;

    private int y;

    @Override
    public Circle clone() {
        return Circle.newCircle(this.x, this.y, this.radius);
    }

    public boolean contains(final Circle... others) {
        for (final Circle o : others) {
            if (!this.containsOne(o)) {
                return false;
            }
        }
        return true;
    }

    public int getRadius() {
        return this.radius;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void performAction(final CircleAction action) {
        switch (action) {
            case UP:
                this.y -= 10;
                break;
            case DOWN:
                this.y += 10;
                break;
            case LEFT:
                this.x -= 10;
                break;
            case RIGHT:
                this.x += 10;
                break;
            case BIGGER:
                this.radius += 10;
                break;
            case SMALLER:
                if (this.radius >= 10) {
                    this.radius -= 10;
                }
                break;
            default:
                Utility.error("Unknown action " + action);
                break;
        }
    }

    public void setRadius(final int radius) {
        if (radius < 0) {
            Utility.error("Trying to set radius to negative value " + radius + "!");
        } else {
            this.radius = radius;
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
        return "(" + this.x + "|" + this.y + ")," + this.radius;
    }

    private boolean containsOne(final Circle that) {
        final double distance = Utility.distance(this.x, this.y, that.x, that.y);
        return this.radius >= that.radius + distance;
    }

    private double singleSize() {
        return this.radius * this.radius * Utility.PI;
    }

}
