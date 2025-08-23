#include <bits/stdc++.h>

#define int long long

using namespace std;

struct node {
    int val;

    node() = default;
    node(int x): val(x) {}
};

int nextpow2(int n) {
    int p = 1;
    while (p < n) p <<= 1;
    return p;
}

struct sum_tree {
    int size;
    vector<node> tree;
    sum_tree(int n) {
        this->size = nextpow2(n);
        tree = vector<node>(2 * this->size);
    }

    void update(int pos, int val) {
        pos += size;
        tree[pos] = node(val);
        while (pos > 0) {
            pos /= 2;
            tree[pos] = merge(tree[2 * pos], tree[2 * pos + 1]);
        }
    }

    node merge(node left, node right) {
        return node(left.val + right.val);
    }

    node get(int gl, int gr, int pos, int l, int r) {
        if (r <= gl || gr <= l) {
            return node(0);
        }
        if (gl <= l && r <= gr) {
            return tree[pos];
        }
        
        int mid = l + (r - l) / 2;
        node left_node = get(gl, gr, 2 * pos, l, mid);
        node right_node = get(gl, gr, 2 * pos + 1, mid, r);
        return merge(left_node, right_node);
    }

    void build(int dataArray[], int n, int type) {
        for (int i = 0; i < n; i++) {
            if ((i + 1) % 2 == type) {
                tree[size + i] = node(dataArray[i]);
            } else {
                tree[size + i] = node(0);
            }
        }
        for (int i = size - 1; i > 0; i--) {
            tree[i] = merge(tree[2 * i], tree[2 * i + 1]);
        }
    }
};

signed main() {
    int n, m;
    cin >> n;
    int data[n];
    for (int i = 0; i < n; i++) {
        cin >> data[i];
    }
    cin >> m;
    sum_tree tree_0(n);
    sum_tree tree_1(n);
    tree_0.build(data, n, 0);
    tree_1.build(data, n, 1);
    int op;
    while (m--) {
        cin >> op;
        if (op == 0) {
            int i, j;
            cin >> i >> j;
            i--;
            if ((i + 1) % 2 == 0) {
                tree_0.update(i, j);
            } else {
                tree_1.update(i, j);
            }
        }
        if (op == 1) {
            int l, r;
            cin >> l >> r;
            l--;
            r--;
            int sum0 = tree_0.get(l, r + 1, 1, 0, tree_0.size).val;
            int sum1 = tree_1.get(l, r + 1, 1, 0, tree_1.size).val;
            int answ = sum0 - sum1;
            if ((l + 1) % 2 == 0) {
                cout << answ << endl;
            } else {
                cout << -answ << endl;
            }
        }
    }
    return 0;
}