#include <bits/stdc++.h>
using namespace std;

#define int long long

struct node {
    int count, val, prio, sum;
    node *left, *right;
    
    node() : count(0), val(0), prio(0), sum(0), left(nullptr), right(nullptr) {}
    node(int v) : count(1), val(v),
        prio((rand() << 16) | (rand() << 1) | (rand() & 1)),
        sum(v), left(nullptr), right(nullptr) {}
};

node *oddTree = nullptr, *evenTree = nullptr;

int getCount(node* t) { return t ? t->count : 0; }
int getSum(node* t) { return t ? t->sum : 0; }

void update(node* t) {
    if(t) {
        t->count = 1 + getCount(t->left) + getCount(t->right);
        t->sum = t->val + getSum(t->left) + getSum(t->right);
    }
}

void split(node* t, node* &a, node* &b, int pos) {
    if(!t) {
        a = b = nullptr;
        return;
    }
    if(pos <= getCount(t->left)) {
        split(t->left, a, t->left, pos);
        b = t;
    } else {
        split(t->right, t->right, b, pos - getCount(t->left) - 1);
        a = t;
    }
    update(t);
}

void mergeTreap(node* a, node* b, node* &t) {
    if(!a || !b) {
        t = a ? a : b;
    } else if(a->prio > b->prio) {
        mergeTreap(a->right, b, a->right);
        t = a;
    } else {
        mergeTreap(a, b->left, b->left);
        t = b;
    }
    update(t);
}

int rangeSum(int L, int R) {
    int ans = 0;
    
    int oddL = (L % 2 == 0) ? L / 2 : (L + 1) / 2;
    int oddR = (R % 2 == 0) ? R / 2 : (R - 1) / 2;
    if(oddL <= oddR) {
        int lenOdd = oddR - oddL + 1;
        node *a1, *b1, *c1;
        split(oddTree, a1, b1, oddL);
        split(b1, b1, c1, lenOdd);
        ans += getSum(b1);
        mergeTreap(a1, b1, oddTree);
        mergeTreap(oddTree, c1, oddTree);
    }
    
    int evenL = (L % 2 == 1) ? (L - 1) / 2 : (L + 1) / 2;
    int evenR = (R % 2 == 1) ? (R - 1) / 2 : ((R - 1) >= 0 ? (R - 2) / 2 : -1);
    if(evenL <= evenR && evenR >= 0) {
        int lenEven = evenR - evenL + 1;
        node *a2, *b2, *c2;
        split(evenTree, a2, b2, evenL);
        split(b2, b2, c2, lenEven);
        ans += getSum(b2);
        mergeTreap(a2, b2, evenTree);
        mergeTreap(evenTree, c2, evenTree);
    }
    
    return ans;
}

void rangeSwap(int L, int R) {
    node *a1 = nullptr, *b1 = nullptr, *c1 = nullptr;
    node *a2 = nullptr, *b2 = nullptr, *c2 = nullptr;
    
    int oddL = (L % 2 == 0) ? L / 2 : (L + 1) / 2;
    int oddR = (R % 2 == 0) ? R / 2 : (R - 1) / 2;
    if(oddL <= oddR) {
        split(oddTree, a1, b1, oddL);
        split(b1, b1, c1, oddR - oddL + 1);
    } else {
        a1 = oddTree; b1 = nullptr; c1 = nullptr;
    }
    
    int evenL = (L % 2 == 1) ? (L - 1) / 2 : (L + 1) / 2;
    int evenR = (R % 2 == 1) ? (R - 1) / 2 : ((R - 1) >= 0 ? (R - 2) / 2 : -1);
    if(evenL <= evenR && evenR >= 0) {
        split(evenTree, a2, b2, evenL);
        split(b2, b2, c2, evenR - evenL + 1);
    } else {
        a2 = evenTree; b2 = nullptr; c2 = nullptr;
    }
    
    mergeTreap(a1, b2, oddTree);
    mergeTreap(oddTree, c1, oddTree);
    mergeTreap(a2, b1, evenTree);
    mergeTreap(evenTree, c2, evenTree);
}

signed main() {
    ios_base::sync_with_stdio(0); cin.tie(0); cout.tie(0);
    
    int n, m, caseNum = 1;
    while (cin >> n >> m, n + m) {
        oddTree = evenTree = nullptr;
        
        if (caseNum != 1) cout << "\n";
        cout << "Swapper " << caseNum++ << ":\n";
        
        for (int i = 1; i <= n; i++) {
            int x;
            cin >> x;
            node* n = new node(x);
            if (i & 1) mergeTreap(oddTree, n, oddTree);
            else mergeTreap(evenTree, n, evenTree);
        }
        
        for (int i = 0; i < m; i++) {
            int type, a, b;
            cin >> type >> a >> b;
            a--; b--;
            
            if (type == 1)
                rangeSwap(a, b);
            else
                cout << rangeSum(a, b) << "\n";
        }
    }
    return 0;
}