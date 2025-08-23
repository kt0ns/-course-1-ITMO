#include "utils.h"

#include "RBtree.h"

#ifdef __cplusplus
extern "C" {
#endif

static void black_uncle_balance(TreeNode* x, TreeNode** root);
static void red_uncle_balance(TreeNode* node, TreeNode** root);

void balance_tree(TreeNode* node, TreeNode** root)
{
	if (!node->parent || node->parent->color != RED)
	{
		return;
	}

	TreeNode* uncle =
		(node->parent == node->parent->parent->left_node) ? node->parent->parent->right_node : node->parent->parent->left_node;

	if (uncle->color == RED)
	{
		red_uncle_balance(node, root);
	}

	else if (uncle->color == BLACK)
	{
		black_uncle_balance(node, root);
	}
}

static void red_uncle_balance(TreeNode* node, TreeNode** root)
{
	if (!node->parent || !node->parent->parent)
	{
		return;
	}

	TreeNode* grand_parent = node->parent->parent;
	if (grand_parent->color == BLACK && grand_parent->left_node->color == RED && grand_parent->right_node->color == RED)
	{
		if (grand_parent->parent)
			grand_parent->color = RED;
		grand_parent->left_node->color = BLACK;
		grand_parent->right_node->color = BLACK;
		balance_tree(grand_parent, root);
	}
}

static void black_uncle_balance(TreeNode* x, TreeNode** root)
{
	if (!x || !x->parent || !x->parent->parent)
		return;

	TreeNode* y = x->parent;
	TreeNode* g = y->parent;
	TreeNode* temp;

	// LR
	if (x == y->right_node && y == g->left_node)
	{
		left_rotate(x);
		temp = x;
		x = y;
		y = temp;
	}

	// RL
	else if (x == y->left_node && y == g->right_node)
	{
		right_rotate(x);
		temp = x;
		x = y;
		y = temp;
	}

	if (x == y->left_node && y == g->left_node)
	{
		right_rotate(y);
		g = y->right_node;
	}

	// RR
	else if (x == y->right_node && y == g->right_node)
	{
		left_rotate(y);
		g = y->left_node;
	}

	if (!y->parent)
	{
		*root = y;
	}
	x->color = RED;
	y->color = BLACK;
	g->color = RED;
}

static void left_rotate(TreeNode* x)
{
	TreeNode* y = x->parent;
	TreeNode* grand_p = y->parent;
	TreeNode* B = x->left_node;

	y->parent = x;
	x->parent = grand_p;

	if (grand_p)
	{
		if (grand_p->left_node == y)
			grand_p->left_node = x;
		else
			grand_p->right_node = x;
	}

	x->left_node = y;
	y->right_node = B;

	if (B)
		B->parent = y;
}

static void right_rotate(TreeNode* x)
{
	TreeNode* y = x->parent;
	TreeNode* grand_p = y->parent;
	TreeNode* B = x->right_node;

	y->parent = x;
	x->parent = grand_p;

	if (grand_p)
	{
		if (grand_p->left_node == y)
			grand_p->left_node = x;
		else
			grand_p->right_node = x;
	}

	x->right_node = y;
	y->left_node = B;

	if (B)
		B->parent = y;
}

#ifdef __cplusplus
}
#endif