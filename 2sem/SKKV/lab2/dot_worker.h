#pragma once

#include "RBtree.h"

#include <stdio.h>

typedef enum
{
	ERROR_WRONG_TYPE_OF_NUMBERS = 100,
	ERROR_INVALID_INPUT_GRAPH,
	ERROR_MEMORY_ALLOCATION,
	ERROR_EMPTY_GRAPH,
	ERROR_GRAPH_WRITE,
} PARSING_ERRORS;

int parse_input_graph(FILE* file, TreeNode** res, size_t* size);
int export_rbtree_to_dot(TreeNode* root, FILE* file);
