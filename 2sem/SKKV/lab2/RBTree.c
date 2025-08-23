#include "RBtree.h"

#include "utils.h"

#ifdef __cplusplus
extern "C" {
#endif

#include <stddef.h>

static void rec_insert(TreeNode* inserting_node, TreeNode* parent, TreeNode* tree_node, TreeNode** root);

TreeNode fakeNode = { BLACK, 0, NULL, NULL, NULL };

TreeNode* getFakeNode()
{
	return &fakeNode;
}

void insert(TreeNode* inserting_node, TreeNode** root)
{
	rec_insert(inserting_node, *root, *root, root);
}

void rec_insert(TreeNode* inserting_node, TreeNode* parent, TreeNode* tree_node, TreeNode** root)
{
	// create root (insert first node)
	if (tree_node == NULL)
	{
		*root = inserting_node;
		inserting_node->parent = NULL;
		inserting_node->left_node = &fakeNode;
		inserting_node->right_node = &fakeNode;
		inserting_node->color = BLACK;

		return;
	}

	// insert new node
	if (tree_node == &fakeNode)
	{
		inserting_node->parent = parent;
		inserting_node->left_node = &fakeNode;
		inserting_node->right_node = &fakeNode;
		inserting_node->color = RED;

		if (parent->value > inserting_node->value)
		{
			parent->left_node = inserting_node;
		}
		else
		{
			parent->right_node = inserting_node;
		}

		balance_tree(inserting_node, root);
		return;
	}

	// recur search place for new node
	if (inserting_node->value < tree_node->value)
	{
		rec_insert(inserting_node, tree_node, tree_node->left_node, root);
	}
	else if (inserting_node->value > tree_node->value)
	{
		rec_insert(inserting_node, tree_node, tree_node->right_node, root);
	}
}

#ifdef __cplusplus
}
#endif