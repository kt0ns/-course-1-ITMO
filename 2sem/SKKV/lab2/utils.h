#pragma once

#include "RBtree.h"

void balance_tree(TreeNode* node, TreeNode** root);
void right_rotate(TreeNode* x);
void left_rotate(TreeNode* x);