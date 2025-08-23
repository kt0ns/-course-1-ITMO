#pragma once

typedef enum
{
	RED,
	BLACK
} COLOR;

typedef struct tree
{
	COLOR color;
	int value;
	struct tree* parent;
	struct tree* left_node;
	struct tree* right_node;
} TreeNode;

extern TreeNode fakeNode;

TreeNode* getFakeNode();
void insert(TreeNode* inserting_node, TreeNode** root);
// void insert(TreeNode* inserting_node, TreeNode* parent, TreeNode* tree_node, TreeNode** root);
