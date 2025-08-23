#include <bits/stdc++.h>
using namespace std;

#define int long long

struct SparseTable {
    int n;
    vector<vector<int>> table;
    
    SparseTable(const vector<int>& a) {
        n = a.size() - 1;
        int maxLog = 64 - __builtin_clzll(n);
        table.assign(n + 1, vector<int>(maxLog, 0));
        for (int i = 1; i <= n; ++i)
            table[i][0] = a[i];
        for (int j = 1; (1LL << j) <= n; ++j)
            for (int i = 1; i + (1LL << j) - 1 <= n; ++i)
                table[i][j] = min(table[i][j - 1], table[i + (1LL << (j - 1))][j - 1]);
    }
    
    int query(int u, int v) const {
        if (u > v)
            swap(u, v);
        int length = v - u + 1;
        int k = 63 - __builtin_clzll(length);
        return min(table[u][k], table[v - (1LL << k) + 1][k]);
    }
};

signed main() {
    ios_base::sync_with_stdio(0); cin.tie(0); cout.tie(0);
    
    int n, m, a1, u, v;
    cin >> n >> m >> a1 >> u >> v;
    
    vector<int> a(n + 1);
    a[1] = a1;
    for (int i = 2; i <= n; ++i) {
        a[i] = (23 * a[i - 1] + 21563) % 16714589;
    }
    
    SparseTable st(a);
    
    int ri = st.query(u, v);
    for (int i = 2; i <= m; ++i) {
        u = ((17 * u + 751 + ri + 2 * (i - 1)) % n) + 1;
        v = ((13 * v + 593 + ri + 5 * (i - 1)) % n) + 1;
        ri = st.query(u, v);
    }
    
    cout << u << " " << v << " " << ri;
    
    return 0;
}