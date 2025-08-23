#include <bits/stdc++.h>
using namespace std;

#define int long long

struct Treap {
    struct node {
        int count, val, prio, mn;
        bool rev;
        node *l, *r;
        node(int v) : count(1), val(v),
            prio((rand() << 16) | (rand() << 1) | (rand() & 1)),
            mn(v), l(nullptr), r(nullptr) {}
    };

    node* root;

    Treap() : root(nullptr) {}

    int count(node* t) {
        return t ? t->count : 0;
    }

    void upd_cnt(node* t) {
        if (t) {
            t->count = count(t->l) + count(t->r) + 1;
            t->mn = t->val;
            if (t->l) t->mn = min(t->mn, t->l->mn);
            if (t->r) t->mn = min(t->mn, t->r->mn);
        }
    }

    void push(node* t) {
        if (t && t->rev) {
            t->rev = false;
            swap(t->l, t->r);
            if (t->l) {
                if (t->l->rev == true) t->l->rev = false;
                else t->l->rev = true;
            }
            
            if (t->r) {
                if (t->r->rev == true) t->r->rev = false;
                else t->r->rev = true;
            }
            
        }
    }

    void merge(node* &t, node* l, node* r) {
        push(l);
        push(r);
        if (!l || !r) t = l ? l : r;
        else if (l->prio > r->prio) {
            merge(l->r, l->r, r);
            t = l;
        } else {
            merge(r->l, l, r->l);
            t = r;
        }
        upd_cnt(t);
    }

    void split(node* t, node* &l, node* &r, int key, int add = 0) {
        if (!t) {
            l = r = nullptr;
            return;
        }
        push(t);
        int cur_key = add + count(t->l);
        if (key <= cur_key) {
            split(t->l, l, t->l, key, add);
            r = t;
        } else {
            split(t->r, t->r, r, key, add + 1 + count(t->l));
            l = t;
        }
        upd_cnt(t);
    }

    void reverse(int l, int r) {
        node *t1, *t2, *t3;
        split(root, t1, t2, l);
        split(t2, t2, t3, r - l + 1);
        if (t2) {
            if (t2->rev == true) t2->rev = false;
            else t2->rev = true;
        }
        
        merge(root, t1, t2);
        merge(root, root, t3);
    }

    int query(int l, int r) {
        node *t1, *t2, *t3;
        split(root, t1, t2, l);
        split(t2, t2, t3, r - l + 1);
        int res = (t2 ? t2->mn : LLONG_MAX);
        merge(t2, t2, t3);
        merge(root, t1, t2);
        return res;
    }

    void insert(int val) {
        node* newNode = new node(val);
        merge(root, root, newNode);
    }
};

signed main() {
    ios_base::sync_with_stdio(0); cin.tie(0); cout.tie(0);

    int n, m;
    cin >> n >> m;
    Treap treap;

    for (int i = 0; i < n; i++) {
        int val;
        cin >> val;
        treap.insert(val);
    }

    while(m--) {
        int type, l, r;
        cin >> type >> l >> r;
        l--; r--;
        if(type == 1) {
            treap.reverse(l, r);
        } else if(type == 2) {
            cout << treap.query(l, r) << "\n";
        }
    }

    return 0;
}