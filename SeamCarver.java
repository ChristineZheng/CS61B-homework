import edu.princeton.cs.algs4.Picture;

/**
 * Created by ChristineZheng on 4/22/16.
 */
public class SeamCarver {

    private Picture pic;
    private double[] energy;
    private double[] distTo;
    private int[] edgeTo;

    // constructor
    public SeamCarver(Picture picture) {
        this.pic = new Picture(picture);

    }

    public Picture picture() {
        return new Picture(this.pic);
    }

    public int width() {
        return pic.width();
    }

    public int height() {
        return pic.height();
    }

    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new IndexOutOfBoundsException();
        }
        int xL, xR, yL, yR;
        if (x == 0) {
            xR = x + 1;
            xL = width() - 1;
            if (y == 0) {
                yR = y + 1;
                yL = height() - 1;
            } else if (y == (height() - 1)) {
                yR = 0;
                yL = y - 1;
            } else {
                yR = y + 1;
                yL = y - 1;
            }
        } else if (x == (width() - 1)) {
            xR = 0;
            xL = x - 1;
            if (y == 0) {
                yR = y + 1;
                yL = height() - 1;
            } else if (y == (height() - 1)) {
                yR = 0;
                yL = y - 1;
            } else {
                yR = y + 1;
                yL = y - 1;
            }
        } else {
            xR = x + 1;
            xL = x - 1;
            if (y == 0) {
                yR = y + 1;
                yL = height() - 1;
            } else if (y == (height() - 1)) {
                yR = 0;
                yL = y - 1;
            } else {
                yR = y + 1;
                yL = y - 1;
            }
        }

        if (width() == 1) {
            xR = x;
            xL = x;
        }
        if (height() == 1) {
            yR = y;
            yL = y;
        }
        double delXRed, delXGreen, delXBlue, delYRed, delYGreen, delYBlue;
        delXRed = pic.get(xR, y).getRed() - pic.get(xL, y).getRed();
        delXGreen = pic.get(xR, y).getGreen() - pic.get(xL, y).getGreen();
        delXBlue = pic.get(xR, y).getBlue() - pic.get(xL, y).getBlue();
        delYRed = pic.get(x, yR).getRed() - pic.get(x, yL).getRed();
        delYGreen = pic.get(x, yR).getGreen() - pic.get(x, yL).getGreen();
        delYBlue = pic.get(x, yR).getBlue() - pic.get(x, yL).getBlue();

        double xE = delXRed * delXRed + delXGreen * delXGreen + delXBlue * delXBlue;
        double yE = delYRed * delYRed + delYGreen * delYGreen + delYBlue * delYBlue;
        return xE + yE;
    }

    private int position(int col, int row) {
        return width() * row + col;
    }

    private int pRow(int position) {
        return position / width();
    }

    private int pColumn(int position) {
        return position % width();
    }

    private void relax(int from, int to) {
        if (distTo[to] > distTo[from] + energy[to]) {
            distTo[to] = distTo[from] + energy[to];
            edgeTo[to] = from;
        }
    }
    private int[] horizontalSeam(int end) {
        int[] result = new int[width()];
        int tmp = end;

        while (tmp >= 0) {
            result[pColumn(tmp)] = pRow(tmp);
            tmp = edgeTo[tmp];
        }

        return result;
    }

    private int[] verticalSeam(int end) {
        int[] result = new int[height()];
        int tmp = end;

        while (tmp >= 0) {
            result[pRow(tmp)] = pColumn(tmp);
            tmp = edgeTo[tmp];
        }
        return result;
    }


    public int[] findHorizontalSeam() {
        int size = width() * height();

        energy = new double[size];
        distTo = new double[size];
        edgeTo = new int[size];
        int p;

        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                p = position(col, row);

                energy[p] = energy(col, row);
                if (col == width() - 1) {
                    distTo[p] = energy[p];
                } else {
                    distTo[p] = Double.POSITIVE_INFINITY;
                }

                edgeTo[p] = -1;
            }
        }

        for (int col = width() - 1; col > 0; col--) {
            for (int row = 0; row < height(); row++) {
                p = position(col, row);

                if (row - 1 >= 0) {
                    relax(p, position(col - 1, row - 1));
                }

                relax(p, position(col - 1, row));

                if (row + 1 < height()) {
                    relax(p, position(col - 1, row + 1));
                }
            }
        }

        double min = Double.POSITIVE_INFINITY;
        int end = 0;

        for (int row = 0; row < height(); row++) {
            if (distTo[position(0, row)] < min) {
                min = distTo[position(0, row)];
                end = position(0, row);
            }
        }

        return horizontalSeam(end);
    }

    public int[] findVerticalSeam() {
        int size = width() * height();

        energy = new double[size];
        distTo = new double[size];
        edgeTo = new int[size];
        int p;

        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                p = position(col, row);

                energy[p] = energy(col, row);
                if (row == height() - 1) {
                    distTo[p] = energy[p];
                } else {
                    distTo[p] = Double.POSITIVE_INFINITY;
                }

                edgeTo[p] = -1;
            }
        }
        for (int row = height() - 1; row > 0; row--) {
            for (int col = 0; col < width(); col++) {
                p = position(col, row);
                if (col - 1 >= 0) {
                    relax(p, position(col - 1, row - 1));
                }

                relax(p, position(col, row - 1));

                if (col + 1 < width()) {
                    relax(p, position(col + 1, row - 1));
                }
            }
        }
        double min = Double.POSITIVE_INFINITY;
        int end = 0;

        for (int col = 0; col < width(); col++) {
            if (distTo[position(col, 0)] < min) {
                min = distTo[position(col, 0)];
                end = position(col, 0);
            }
        }

        return verticalSeam(end);
    }

    public void removeHorizontalSeam(int[] seam) {

        if (height() <= 1 || seam.length != width()) {
            throw new IllegalArgumentException();
        }
        Picture result = new Picture(width(), height() - 1);
        int prev = seam[0];

        for (int col = 0; col < width(); col++) {
            if (seam[col] < 0 || seam[col] >= height()) {
                throw new IndexOutOfBoundsException();
            }

            if (seam[col] < prev - 1 || seam[col] > prev + 1) {
                throw new IllegalArgumentException();
            }

            prev = seam[col];

            for (int row = 0; row < height() - 1; row++) {
                if (row < prev) {
                    result.set(col, row, pic.get(col, row));
                } else {
                    result.set(col, row, pic.get(col, row + 1));
                }
            }
        }

        pic = result;

        distTo = null;
        edgeTo = null;
        energy = null;
    }

    public void removeVerticalSeam(int[] seam) {

        if (width() <= 1 || seam.length != height()) {
            throw new IllegalArgumentException();
        }
        Picture result = new Picture(width() - 1, height());
        int prev = seam[0];

        for (int row = 0; row < height(); row++) {
            if (seam[row] < 0 || seam[row] >= width()) {
                throw new IndexOutOfBoundsException();
            }

            if (seam[row] < prev - 1 || seam[row] > prev + 1) {
                throw new IllegalArgumentException();
            }

            prev = seam[row];

            for (int col = 0; col < width() - 1; col++) {
                if (col < prev) {
                    result.set(col, row, pic.get(col, row));
                } else {
                    result.set(col, row, pic.get(col + 1, row));
                }
            }
        }

        pic = result;

        distTo = null;
        edgeTo = null;
        energy = null;
    }
}
