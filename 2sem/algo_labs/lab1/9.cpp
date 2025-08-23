#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

int nextpow2(int n) {
    int p = 1;
    while (p < n) p <<= 1;
    return p;
}

struct Tree {
    vector<int> tree;
    int n;

    Tree(int n) {
        this->n = n;
        int size = nextpow2(n);
        tree = vector<int>(2 * size, 0);
    }

    void update(int v, int tl, int tr, int pos, int delta) {
        if (tl == tr) {
            tree[v] += delta;
        }
        else {
            int tm = (tl + tr) / 2;
            if (pos <= tm)
                update(v * 2, tl, tm, pos, delta);
            else
                update(v * 2 + 1, tm + 1, tr, pos, delta);
            tree[v] = tree[v * 2] + tree[v * 2 + 1];
        }
    }

    int get(int v, int tl, int tr, int l, int r) {
        if (l > r) return 0;
        if (l == tl && r == tr) return tree[v];
        int tm = (tl + tr) / 2;
        return get(v * 2, tl, tm, l, min(r, tm)) +
            get(v * 2 + 1, tm + 1, tr, max(l, tm + 1), r);
    }
};

struct Event {
    int x, y1, y2, type, idx;
    bool operator<(const Event& other) const {
        if (x == other.x) return type < other.type; 
        return x < other.x;
    }

};

signed main() {
    int n, m;
    cin >> n;

    vector<Event> events;
    vector<int> y_coords;

    for (int i = 0; i < n; i++) {
        int x, y;
        cin >> x >> y;
        events.push_back({ x, y, y, 0, -1 });
        y_coords.push_back(y);
    }
    cin >> m;
    vector<int> ans(m);

    for (int i = 0; i < m; i++) {
        int x1, y1, x2, y2;
        cin >> x1 >> y1 >> x2 >> y2;
        events.push_back({ x1 - 1, y1, y2, 1, i });
        events.push_back({ x2, y1, y2, 2, i });
        y_coords.push_back(y1);
        y_coords.push_back(y2);
    }

    sort(y_coords.begin(), y_coords.end());
    y_coords.erase(unique(y_coords.begin(), y_coords.end()), y_coords.end());

    auto getYIndex = [&](int y)
    {
    return lower_bound(y_coords.begin(), y_coords.end(), y) - y_coords.begin() + 1;
    };

    for (auto& e : events) {
        e.y1 = getYIndex(e.y1);
        e.y2 = getYIndex(e.y2);
    }

    sort(events.begin(), events.end());

    Tree tree(y_coords.size());

    for (const auto& e : events) {
        if (e.type == 0) {
            tree.update(1, 1, y_coords.size(), e.y1, 1);
        }
        else if (e.type == 1) {
            ans[e.idx] -= tree.get(1, 1, y_coords.size(), e.y1, e.y2);
        }
        else {
            ans[e.idx] += tree.get(1, 1, y_coords.size(), e.y1, e.y2);
        }
    }

    for (int i = 0; i < m; i++) {
        cout << ans[i] << "\n";
    }

    return 0;
}