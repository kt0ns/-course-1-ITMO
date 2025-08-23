#include "dot_worker.h"

#include "RBtree.h"
#include <graphviz/cgraph.h>

#include <ctype.h>
#include <stdlib.h>

static char is_integer(const char* str);
static int logic(Agnode_t* node, TreeNode* tree_node);
static TreeNode* find_root(TreeNode* node);

static char* get_string(int value);
static char buffer_for_convert_int_to_string[64];

int parse_input_graph(FILE* file, TreeNode** res, size_t* size)
{
	Agraph_t* g = agread(file, NULL);

	if (!g)
	{
		return ERROR_INVALID_INPUT_GRAPH;
	}

	const size_t size_graph = agnnodes(g);
	TreeNode* arr = malloc(size_graph * sizeof(TreeNode));
	if (!arr)
	{
		agclose(g);
		return ERROR_MEMORY_ALLOCATION;
	}
	*res = arr;

	size_t i = 0;
	Agnode_t* node = agfstnode(g);

	while (node)
	{
		if (logic(node, &arr[i++]))
		{
			agclose(g);
			return ERROR_WRONG_TYPE_OF_NUMBERS;
		}
		node = agnxtnode(g, node);
	}

	agclose(g);
	*size = size_graph;
	return 0;
}

void add_tree_nodes(Agraph_t** g, TreeNode* node)
{
	if (!node)
		return;

	Agnode_t* gv_node = agnode(*g, get_string(node->value), 1);
	agsafeset(gv_node, "color", node->color == RED ? "red" : "black", "");

	if (node->left_node->left_node)
	{
		Agnode_t* left_gv_node = agnode(*g, get_string(node->left_node->value), 1);
		agedge(*g, gv_node, left_gv_node, "", 1);
		add_tree_nodes(g, node->left_node);
	}

	if (node->right_node->right_node)
	{
		Agnode_t* right_gv_node = agnode(*g, get_string(node->right_node->value), 1);
		agedge(*g, gv_node, right_gv_node, "", 1);
		add_tree_nodes(g, node->right_node);
	}
}

int export_rbtree_to_dot(TreeNode* root, FILE* file)
{
	Agraph_t* g = agopen("g", (Agdesc_t){ 0 }, NULL);
	add_tree_nodes(&g, find_root(root));

	if (agwrite(g, file) < 0)
	{
		return ERROR_GRAPH_WRITE;
	}
	agclose(g);

	return 0;
}

static char is_integer(const char* str)
{
	if (*str == '-' || *str == '+')
		str++;

	if (*str == '\0')
		return 0;

	while (*str)
	{
		if (!isdigit(*str))
			return 0;
		str++;
	}
	return 1;
}

static int logic(Agnode_t* node, TreeNode* tree_node)
{
	if (!is_integer(agnameof(node)))
	{
		return ERROR_WRONG_TYPE_OF_NUMBERS;
	}

	tree_node->parent = NULL;
	tree_node->color = RED;

	tree_node->value = atoi(agnameof(node));

	tree_node->left_node = NULL;
	tree_node->right_node = NULL;
	return 0;
}

static TreeNode* find_root(TreeNode* node)
{
	if (!node->parent)
	{
		return node;
	}
	return find_root(node->parent);
}

static char* get_string(const int value)
{
	sprintf(buffer_for_convert_int_to_string, "%d", value);
	return buffer_for_convert_int_to_string;
}
