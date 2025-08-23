#include <bits/stdc++.h>

#define int long long

using namespace std;

int nextpow2(int n) {
    int p = 1;
    while (p < n) p <<= 1;
    return p;
}

struct tree {
    int size;
    vector<vector<int>> data;

    tree(const vector<int>& right_entry) {
        int n = right_entry.size();
        this->size = nextpow2(n);
        data.resize(2 * this->size);

        for (int i = 0; i < n; i++) {
            data[i + size].push_back(right_entry[i]);
        }

        for (int i = size - 1; i > 0; i--) {
            data[i].resize(data[2 * i].size() + data[2 * i + 1].size());
            merge(data[2 * i].begin(), data[2 * i].end(),
                  data[2 * i + 1].begin(), data[2 * i + 1].end(),
                  data[i].begin());
        }
    }

    int get(int l, int r, int R) {
        l += size;
        r += size;
        int result = 0;

        while (l < r) {
            if (l % 2 == 1) {
                result += data[l].end() - upper_bound(data[l].begin(), data[l].end(), R);
                l++;
            }
            if (r % 2 == 1) {
                r--;
                result += data[r].end() - upper_bound(data[r].begin(), data[r].end(), R);
            }
            l /= 2;
            r /= 2;
        }
        return result;
    }
};

signed main() {
    ios_base::sync_with_stdio(0); cin.tie(0); cout.tie(0);
    int n, m;
    cin >> n;
    vector<int> a(n);
    for (int i = 0; i < n; i++) {
        cin >> a[i];
    }

    vector<int> right_entry(n, n + 1);
    unordered_map<int, int> lastPos;
    for (int i = n - 1; i >= 0; i--) {
        if (lastPos.count(a[i])) {
            right_entry[i] = lastPos[a[i]];
        }
        lastPos[a[i]] = i;
    }

    tree t(right_entry);
    cin >> m;
    while (m--) {
        int l, r;
        cin >> l >> r;
        l--;
        int answ = t.get(l, r, r - 1);
        cout << answ << endl;
    }

    return 0;
}