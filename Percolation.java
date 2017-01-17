package hw2;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF parent;
    private WeightedQuickUnionUF newParent;
    private boolean[] open;
    private int N;
    private int length;
    private int openSites;
    private int topSite;
    private int bottomSite;

    // constructor
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        N = n;
        length = n * n + 2;
        topSite = n * n;
        bottomSite = n * n + 1;
        open = new boolean[length];
        for (int i = 0; i < length; i++) {
            open[i] = false;
        }
        parent = new WeightedQuickUnionUF(length);
        newParent = new WeightedQuickUnionUF(length - 1);
        openSites = 0;
    }

    // get
    public int get(int row, int col) {
        return row * N + col;
    }
    // validate
    public void isValidated(int row, int col) {
        if (row >= N | row < 0 || col >= N || col < 0) {
            throw new IndexOutOfBoundsException();
        }
    }
    public boolean validated(int row, int col) {
        if (row >= N | row < 0 || col >= N || col < 0) {
            return false;
        }
        return true;
    }

    // open
    public void open(int row, int col) {
        isValidated(row, col);

        // already open
        if (isOpen(row, col)) {
            return;

            // not open yet
        } else {
            int index = get(row, col);
            open[index] = true;
            openSites += 1;

            // union top row open box with topSite
            if (row == 0) {
                parent.union(index, topSite);
                newParent.union(index, topSite);
            }
            // union bottom row open box with bottomSite
            if (row == N - 1) {
                parent.union(index, bottomSite);
            }

            // top
            if (validated(row - 1, col) && isOpen(row - 1, col)) {
                int top = get(row - 1, col);
                parent.union(index, top);
                newParent.union(index, top);
            }
            // left
            if (validated(row, col - 1) && isOpen(row, col - 1)) {
                int left = get(row, col - 1);
                parent.union(index, left);
                newParent.union(index, left);
            }
            // right
            if (validated(row, col + 1) && isOpen(row, col + 1)) {
                int right = get(row, col + 1);
                parent.union(index, right);
                newParent.union(index, right);
            }
            // bottom
            if (validated(row + 1, col) && isOpen(row + 1, col)) {
                int bottom = get(row + 1, col);
                parent.union(index, bottom);
                newParent.union(index, bottom);
            }
        }
    }


            // union bottom row open box with bottomSite
 /*           if (row == N - 1) {
                parent.union(index, bottomSite);
                // top
                if (validated(row - 1, col) && isFull(row - 1, col)) {
                    int top = get(row - 1, col);
                    parent.union(index, bottomSite);
                }
                // left
                if (validated(row, col - 1) && isFull(row, col - 1)) {
                    int left = get(row, col - 1);
                    parent.union(index, bottomSite);
                }
                // right
                if (validated(row, col + 1) && isFull(row, col + 1)) {
                    int right = get(row, col + 1);
                    parent.union(index, bottomSite);
                }
*/

    // isOopen
    public boolean isOpen(int row, int col) {
        isValidated(row, col);
        int index = get(row, col);
        return open[index];

    }
    // isFull
    public boolean isFull(int row, int col) {
        isValidated(row, col);
        int index = get(row, col);
        if (newParent.connected(index, topSite)) {
            return true;
        }
        return false;
    }

    // numberOfOpenSites
    public int numberOfOpenSites() {
        return openSites;

    }
    // percolates
    public boolean percolates() {
        return parent.connected(topSite, bottomSite);
    }

}
