#include <iostream>  
#include <vector>    
#include <algorithm>

#define int long long

using namespace std;

struct node {
    int left_val;
    int right_val;
    int max_left;
    int max_right;

    node() : left_val(-1e18), right_val(-1e18), max_left(-1e18), max_right(-1e18) {}
    node(int left, int right, int delta_left, int delta_right) : left_val(left), right_val(right), max_left(delta_left), max_right(delta_right) {}
};

int nextpow2(int n) {
    int p = 1;
    while (p < n) p <<= 1;
    return p;
}

struct sum_tree {
    int size;
    int n;
    vector<node> tree;
    vector<int> lazy;

    sum_tree(int n) {
        this->n = n;
        this->size = nextpow2(n);
        tree = vector<node>(2 * this->size);
        lazy = vector<int>(2 * this->size, 0);
    }

    node merge(node left, node right) {
        node res;
        res.left_val = left.left_val;
        res.right_val = right.right_val;
        res.max_left = max(left.max_left, right.max_left);
        if (left.right_val != -1e18 && right.left_val != -1e18) {
            res.max_left = max(res.max_left, right.left_val - left.right_val);
        }
        res.max_right = max(left.max_right, right.max_right);
        if (right.left_val != -1e18 && left.right_val != -1e18) {
            res.max_right = max(res.max_right, left.right_val - right.left_val);
        }
        return res;
    }


    void push(int pos, int l, int r) {
        if (lazy[pos] != 0) {
            tree[pos].left_val += lazy[pos];
            tree[pos].right_val += lazy[pos];

            if (r - l > 1) { 
                lazy[2 * pos] += lazy[pos];
                lazy[2 * pos + 1] += lazy[pos];
            }
            lazy[pos] = 0;
        }
    }

    void update(int l, int r, int val, int pos, int tl, int tr) {
        push(pos, tl, tr);
        if (r <= tl || tr <= l) {
            return;
        }
        if (l <= tl && tr <= r) {
            lazy[pos] += val;
            push(pos, tl, tr);
            return;
        }
        int tm = (tl + tr) / 2;
        update(l, r, val, 2 * pos, tl, tm);
        update(l, r, val, 2 * pos + 1, tm, tr);
        tree[pos] = merge(tree[2 * pos], tree[2 * pos + 1]);
    }

    void update(int l, int r, int val) {
        update(l, r + 1, val, 1, 0, size);
    }

    node get(int gl, int gr, int pos, int l, int r) {
        push(pos, l, r);
        if (r <= gl || gr <= l) {
            return node();
        }
        if (gl <= l && r <= gr) {
            return tree[pos];
        }

        int mid = l + (r - l) / 2;
        node left_node = get(gl, gr, 2 * pos, l, mid);
        node right_node = get(gl, gr, 2 * pos + 1, mid, r);
        return merge(left_node, right_node);
    }

    void build(int n, vector<int> dataArray, vector<int> deltaArrayLeft, vector<int> deltaArrayRight) {
        for (int i = 0; i < n; i++) {
            tree[size + i] = node(dataArray[i], dataArray[i], 0, 0);
        }
        for (int i = size - 1; i > 0; i--) {
            tree[i] = merge(tree[2 * i], tree[2 * i + 1]);
        }
    }
};

signed main() {
    ios_base::sync_with_stdio(0); cin.tie(0); cout.tie(0);
    int n, m;
    cin >> n >> m;

    vector<int> data(n);
    vector<int> data_left(n, 0);
    vector<int> data_right(n, 0);

    for (int i = 0; i < n; i++) {
        cin >> data[i];
    }
    for (int i = 1; i < n; i++) {
        data_left[i] = data[i] - data[i - 1];
    }
    for (int i = n - 2; i >= 0; i--) {
        data_right[i] = data[i] - data[i + 1];
    }

    sum_tree tree(n);
    tree.build(n, data, data_left, data_right);

    int op;
    while (m--) {
        cin >> op;
        if (op == 1) {
            int x, y;
            cin >> x >> y;
            x--, y--;

            int result;
            if (x < y) {
                result = tree.get(x, y + 1, 1, 0, tree.size).max_left;
            }
            else {
                result = tree.get(y, x + 1, 1, 0, tree.size).max_right;
            }


            if (result < 2) {
                cout << "Yes" << endl;
            }
            else {
                cout << "No" << endl;
            }
        }
        else if (op == 2) {
            int l, r, d;
            cin >> l >> r >> d;
            l--, r--;

            tree.update(l, r, d);
        }
    }
    return 0;
}