#include <iostream>  
#include <vector>    
#include <algorithm>

#define int long long

using namespace std;

struct node {
    vector<int> values;

    node() = default;
    node(int x) : values({ x }) {};
    node(vector<int> v) : values(v) {};
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

    sum_tree(int n) {
        this->n = n;
        this->size = nextpow2(n);
        tree = vector<node>(2 * this->size);
    }

    node merge(node left, node right) {
        if (right.values.empty()) return left;
        if (left.values.empty()) return right;

        node res;
        int i = 0, j = 0;

        while (i < left.values.size() && j < right.values.size()) {
            if (left.values[i] <= right.values[j]) {
                res.values.push_back(left.values[i]);
                i++;
            }
            else {
                res.values.push_back(right.values[j]);
                j++;
            }
        }

        while (i < left.values.size()) {
            res.values.push_back(left.values[i]);
            i++;
        }

        while (j < right.values.size()) {
            res.values.push_back(right.values[j]);
            j++;
        }

        return res;
    }

    int get(int x, int y, int k, int l, int pos, int leftPosTree, int rightPosTree) {
        if (rightPosTree <= x || y <= leftPosTree) {
            return 0;
        }
        if (x <= leftPosTree && rightPosTree <= y) {
            return getNum(tree[pos].values, k, l);
        }

        int mid = leftPosTree + (rightPosTree - leftPosTree) / 2;
        int left_result = get(x, y, k, l, 2 * pos, leftPosTree, mid);
        int right_result = get(x, y, k, l, 2 * pos + 1, mid, rightPosTree);
        return left_result + right_result;
    }

    int getNum(vector<int>& arr, int min, int max) {
        auto lower = lower_bound(arr.begin(), arr.end(), min);
        auto upper = upper_bound(arr.begin(), arr.end(), max);
        return distance(lower, upper);
    }

    void build(int n, vector<int> dataArray) {
        for (int i = 0; i < n; i++) {
            tree[size + i] = node(dataArray[i]);
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

    for (int i = 0; i < n; i++) {
        cin >> data[i];
    }

    sum_tree tree(n);
    tree.build(n, data);

    while (m--) {
        int x, y, k, l;
        cin >> x >> y >> k >> l;
        x--;
        int result = tree.get(x, y, k, l, 1, 0, tree.size);
        cout << result << '\n';
    }
    return 0;
}