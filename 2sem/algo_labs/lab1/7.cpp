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

    void update(int pos) {
        pos += size;
        tree[pos] = node(tree[pos].val + 1);
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

    void build(int n) {
        for (int i = 0; i < n; i++) {
            tree[size + i] = node(0);
        }
        for (int i = size - 1; i > 0; i--) {
            tree[i] = merge(tree[2 * i], tree[2 * i + 1]);
        }
    }
};

vector<int> compress(vector<int>& arr) {
    vector<int> sorted_arr = arr;
    sort(sorted_arr.begin(), sorted_arr.end());
    sorted_arr.erase(unique(sorted_arr.begin(), sorted_arr.end()), sorted_arr.end());

    vector<int> compressed(arr.size());
    for (size_t i = 0; i < arr.size(); i++) {
        compressed[i] = lower_bound(sorted_arr.begin(), sorted_arr.end(), arr[i]) - sorted_arr.begin();
    }

    return compressed;
}

signed main() {
    int n, answ = 0;
    cin >> n;
    vector<int> data(n);
    for (int i = 0; i < n; i++) {
        cin >> data[i];
    }
    vector<int> new_data = compress(data);
    sum_tree tree(new_data.size());
    tree.build(new_data.size());

    for (int i = n - 1; i >= 0; i--) {
        answ += tree.get(0, new_data[i], 1, 0, tree.size).val;
        tree.update(new_data[i]);
    }

    cout << answ << endl;
    return 0;
}