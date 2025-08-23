#include "avl.h"
#include <algorithm>

using namespace std;

size_t h(node* n) { 
    return n ? n->h : 0; 
}

int checkB(node* node) {
    if (node == nullptr) return 0;
    return h(node->l) - h(node->r);
}

node* minNode(node* root) {
    if (!root) return nullptr;
    node* current = root;
    while (current->l) {
        current = current->l;
    }
    return current;
}

node *right(node *y) { 
    node *x = y->l; 
    node *T2 = x->r; 

    x->r = y; 
    y->l = T2; 

    y->h = 1 + max(h(y->l), h(y->r)); 
    x->h = 1 + max(h(x->l), h(x->r)); 

    return x; 
} 

node *left(node *x) { 
    node *y = x->r; 
    node *T2 = y->l; 

    y->l = x; 
    x->r = T2; 

    x->h = 1 + max(h(x->l), h(x->r)); 
    y->h = 1 + max(h(y->l), h(y->r)); 

    return y; 
} 

node* insert(node *root, int key) {
    if (root == nullptr)
        return new node {
            nullptr,
            nullptr,
            key,
            1          
        };

    if (key < root->key)
        root->l = insert(root->l, key);
    else if (key > root->key)
        root->r = insert(root->r, key);
    else return root;

    root->h = 1 + max(h(root->l), h(root->r));

    int balance = checkB(root);

    if (balance > 1 && key < root->l->key)
        return right(root);

    if (balance < -1 && key > root->r->key)
        return left(root);

    if (balance > 1 && key > root->l->key) {
        root->l = left(root->l);
        return right(root);
    }

    if (balance < -1 && key < root->r->key) {
        root->r = right(root->r);
        return left(root);
    }

    return root;
}

node* remove(node *root, int key) {
    if (root == nullptr) return root;

    if (key < root->key) root->l = remove(root->l, key);
    else if (key > root->key) root->r = remove(root->r, key);
    else {
        if ((root->l == nullptr) || (root->r == nullptr)) {
            node* temp = root->l ? root->l : root->r;
            if (temp == nullptr) {
                temp = root;
                root = nullptr;
            } else *root = *temp;
            delete temp;
        } else {
            node* temp= minNode(root->r);
            root->key = temp->key;
            root->r= remove(root->r, temp->key);
        }
    }

    if (root == nullptr) return root;

    root->h = 1 + max(h(root->l), h(root->r));

    int balance = checkB(root);

    if (balance > 1 && checkB(root->l) >= 0) return right(root);

    if (balance > 1 && checkB(root->l) < 0) {
        root->l = left(root->l);
        return right(root);
    }

    if (balance < -1 && checkB(root->r) <= 0) return left(root);

    if (balance < -1 && checkB(root->r) > 0) {
        root->r = right(root->r);
        return left(root);
    }

    return root;
}

bool exists(node *root, int key) {
	if (root == nullptr)
		return false;
	if (root->key == key)
		return true;
	if (key < root->key)
		return exists(root->l, key);
	return exists(root->r, key);
}