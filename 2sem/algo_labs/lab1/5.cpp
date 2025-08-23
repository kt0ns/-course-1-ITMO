#include <bits/stdc++.h>
using namespace std;

#define int long long
const int MOD = 1e9+7;

struct node {
    int max_len;
    int count;

    node() : max_len(0), count(0) {}
    node(int len, int cnt) : max_len(len), count(cnt) {}
};

struct sum_tree {
    int size;
    vector<node> tree;

    sum_tree(int n) {
        size = 1;
        while (size < n) size *= 2;
        tree.resize(2 * size);
    }

    node merge(const node &left, const node &right) {
        if (left.max_len == right.max_len) {
            return node(left.max_len, (left.count + right.count) % MOD);
        }
        return (left.max_len > right.max_len) ? left : right;
    }

    void update(int index, int max_len, int cnt) {
        index += size;
        if (tree[index].max_len == max_len) {
            tree[index].count = (tree[index].count + cnt) % MOD;
        } else if (tree[index].max_len < max_len) {
            tree[index] = node(max_len, cnt);
        }

        while (index > 1) {
            index /= 2;
            tree[index] = merge(tree[2 * index], tree[2 * index + 1]);
        }
    }

    node get(int l, int r, int x, int lx, int rx) {
        if (lx > r || rx < l) return node();
        if (lx >= l && rx <= r) return tree[x];
        int mid = (lx + rx) / 2;
        node left = get(l, r, 2 * x, lx, mid);
        node right = get(l, r, 2 * x + 1, mid + 1, rx);
        return merge(left, right);
    }

    node query(int l, int r) {
        return get(l, r, 1, 0, size - 1);
    }
};

int get_answ(int arr[], int n) {
    vector<int> sorted(arr, arr + n);
    sort(sorted.begin(), sorted.end());
    sorted.erase(unique(sorted.begin(), sorted.end()), sorted.end());

    sum_tree sum_tree(sorted.size());

    for (int i = 0; i < n; i++) {
        int idx = lower_bound(sorted.begin(), sorted.end(), arr[i]) - sorted.begin();
        
        node res = sum_tree.query(0, idx - 1);

        int new_len = res.max_len + 1;
        int new_cnt = res.count == 0 ? 1 : res.count;

        sum_tree.update(idx, new_len, new_cnt);
    }

    node result = sum_tree.query(0, sorted.size() - 1);
    return result.count;
}

signed main() {
    int n;
    cin >> n;
    int data[n];
    for (int i = 0; i < n; i++) {
        cin >> data[i];
    }
    
    cout << get_answ(data, n) << endl;
    return 0;
}