#include <bits/stdc++.h>

#define int long long

using namespace std;

struct node {
    int max;

    node() = default;
    node(int W): max(W) {}
};

int nextpow2(int n) {
    int p = 1;
    while (p < n) p <<= 1;
    return p;
}

struct max_tree {
    vector<node> tree;
    int size;
    int W;

    max_tree(int n, int w) {
        this->size = nextpow2(n);
        this->W = w;
        tree = vector<node>(2 * this->size);
    }

    void build(int n) {
        for (int i = 0; i < n; i++) {
            tree[size + i] = node(W);
        }
        for (int i = size - 1; i > 0; i--) {
            tree[i] = merge(tree[2 * i], tree[2 * i + 1]);
        }
    }

    void update(int pos, int value) {
        pos += size;
        tree[pos] = node(tree[pos].max - value); 
        while (pos > 1) {
            pos /= 2;
            tree[pos] = merge(tree[2 * pos], tree[2 * pos + 1]);
        }
    }

    node merge(node left, node right) {
        return node(max(left.max, right.max));
    }

    int find_pos(int Wi) {
        int pos = 1;
        if (tree[pos].max < Wi) return -1; 
        
        while (pos < size) {
            if (tree[2 * pos].max >= Wi) {
                pos = 2 * pos; 
            } else {
                pos = 2 * pos + 1; 
            }
        }
        
        return pos - size + 1;
    }
};

signed main() {
    int H, W, N;
    cin >> H >> W >> N;
    int c;
    if (N < H) {
        c = N;
    } else {
        c = H;
    }
    max_tree tree(c, W);
    tree.build(c);

    for (int i = 0; i < N; ++i) {
        int Wi;
        cin >> Wi;

        int row = tree.find_pos(Wi);

        if (row > H || row < 0) {
            cout << -1 << endl;
        } else {
            tree.update(row - 1, Wi); 
            cout << row << endl;
        }
    }

    return 0;
}