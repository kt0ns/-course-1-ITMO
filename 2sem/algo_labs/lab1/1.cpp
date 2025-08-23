#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

struct tree {
    long long sum;
    long long pref;
    long long suf;
    long long answ;

    tree() : sum(0), pref(0), suf(0), answ(0) {}
    tree(long long value) : sum(value), pref(max(value, 0LL)), suf(max(value, 0LL)), answ(max(value, 0LL)) {}
};

vector<long long> dataArray;
vector<tree> arr;

int nearestPowerOfTwo(int n) {
    int exp = 1;
    while (exp < n) {
        exp *= 2;
    }
    return exp;
}

tree father(const tree& left, const tree& right) {
    tree res;
    res.sum = left.sum + right.sum;
    res.pref = max(left.pref, left.sum + right.pref);
    res.suf = max(right.suf, right.sum + left.suf);
    res.answ = max({left.answ, right.answ, left.suf + right.pref});
    return res;
}

void build(int n) {
    int r = nearestPowerOfTwo(n);
    for (int i = 0; i < n; i++) {
        arr[r + i] = tree(dataArray[i]);
    }
    for (int i = r - 1; i > 0; i--) {
        arr[i] = father(arr[2 * i], arr[2 * i + 1]);
    }
}

void update(int node, long long value) {
    arr[node] = tree(value);
    for (node /= 2; node >= 1; node /= 2) {
        arr[node] = father(arr[node * 2], arr[node * 2 + 1]);
    }
}

tree getMax(int node, int start, int end, int l, int r) {
    if (l >= end || r <= start) {
        return tree();
    }
    if (l <= start && end <= r) {
        return arr[node];
    }
    int mid = (start + end) / 2;
    return father(getMax(2 * node, start, mid, l, r), getMax(2 * node + 1, mid, end, l, r));
}

int main() {
    ios_base::sync_with_stdio(0); cin.tie(0); cout.tie(0);

    int n, q;
    cin >> n >> q;
    dataArray.resize(n);
    for (int i = 0; i < n; i++) {
        cin >> dataArray[i];
    }
    int nHight = nearestPowerOfTwo(n);
    arr.resize(2 * nHight);
    build(n);
    while (q--) {
        string command;
        int l, r;
        cin >> command >> l >> r;
        l--;

        if (command == "get") {
            cout << getMax(1, 0, nHight, l, r).answ << endl;
        } else {
            update(nHight + l, r);
        }
    }
    return 0;
}