#include <iostream>
#include <vector>
#include <algorithm>

#define int long long

using namespace std;

const int SHIFT = 1e9;

struct Rectangle {
    int x1, y1, x2, y2;
    Rectangle(int x1, int y1, int x2, int y2)
        : x1(x1), y1(y1), x2(x2), y2(y2) {
    }
};

struct Event {
    int y, x1, x2, type;
    Event(int y, int x1, int x2, int type)
        : y(y), x1(x1), x2(x2), type(type) {
    }
    bool operator<(const Event& other) const { return y < other.y; }
};

struct Node {
    int status = 0;
    int countPos = 0;
    Node* left = nullptr;
    Node* right = nullptr;

    void recalculate(int l, int r) {
        if (status > 0) {
            countPos = r - l + 1;
        }
        else {
            countPos = 0;
            if (left) countPos += left->countPos;
            if (right) countPos += right->countPos;
        }
    }
};

struct ImplicitSegmentTree {
    Node* root = new Node();

    void push(Node* node, int l, int r) {
        if (!node) return;
        if (l == r) return;
        if (!node->left) node->left = new Node();
        if (!node->right) node->right = new Node();
    }

    void up(Node* node, int l, int r, int ql, int qr) {
        if (ql > r || qr < l) return;
        if (ql <= l && r <= qr) {
            node->status++;
            node->recalculate(l, r);
            return;
        }
        push(node, l, r);
        int mid = (l + r) / 2;
        up(node->left, l, mid, ql, qr);
        up(node->right, mid + 1, r, ql, qr);
        node->recalculate(l, r);
    }

    void down(Node* node, int l, int r, int ql, int qr) {
        if (ql > r || qr < l) return;
        if (ql <= l && r <= qr) {
            node->status--;
            node->recalculate(l, r);
            return;
        }
        push(node, l, r);
        int mid = (l + r) / 2;
        down(node->left, l, mid, ql, qr);
        down(node->right, mid + 1, r, ql, qr);
        node->recalculate(l, r);
    }

    void updateUp(int l, int r) {
        up(root, 0, 2e9, l, r);
    }

    void updateDown(int l, int r) {
        down(root, 0, 2e9, l, r);
    }

    int getCountPos() {
        return root->countPos;
    }
};

signed main() {
    ios_base::sync_with_stdio(0); cin.tie(0); cout.tie(0);

    int N;
    cin >> N;

    vector<Rectangle> rectangles;
    vector<Event> events;
    ImplicitSegmentTree st;

    for (int i = 0; i < N; ++i) {
        int x1, y1, x2, y2;
        cin >> x1 >> y1 >> x2 >> y2;
        x1 += SHIFT;
        y1 += SHIFT;
        x2 += SHIFT;
        y2 += SHIFT;
        rectangles.emplace_back(x1, y1, x2, y2);

        events.emplace_back(y1, x1, x2 - 1, 1);
        events.emplace_back(y2, x1, x2 - 1, -1);
    }

    sort(events.begin(), events.end());
    int totalArea = 0;
    int prevY = events[0].y;

    for (const auto& event : events) {
        totalArea += (event.y - prevY) * st.getCountPos();
        prevY = event.y;

        if (event.type == 1) {
            st.updateUp(event.x1, event.x2);
        }
        else {
            st.updateDown(event.x1, event.x2);
        }
    }

    cout << totalArea << '\n';
    return 0;
}