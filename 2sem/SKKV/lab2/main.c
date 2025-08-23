#include "RBtree.h"
#include "dot_worker.h"

#include <stdio.h>
#include <stdlib.h>

typedef enum
{
	SUCCESS = 0,
	ERROR_FILE_READING,
	ERROR_FILE_WRITING,
	INVALID_INPUT,
} ERROR_CODES;

static const char* error_message_table[] = {
	[SUCCESS] = "",
	[ERROR_FILE_READING] = "Failed to read the file.",
	[ERROR_FILE_WRITING] = "Failed to write to the file.",
	[INVALID_INPUT] = "Invalid input.",
	[ERROR_WRONG_TYPE_OF_NUMBERS] = "Node's names can't cast to int.",
	[ERROR_INVALID_INPUT_GRAPH] = "Invalid input graph.",
	[ERROR_MEMORY_ALLOCATION] = "Memory allocation failed.",
	[ERROR_EMPTY_GRAPH] = "Empty graph.",
	[ERROR_GRAPH_WRITE] = "Failed to write to the file.",
};

typedef struct context
{
	FILE* file;
	TreeNode* nodes;
	int ERROR_TYPE;
} Context;

static void release_context(const Context* context);
static int open_file(FILE** file, const char* file_name, const char* type_of_opening);
static void inserting_nodes_in_tree(TreeNode** root, TreeNode* nodes, size_t size);

int main(const int argc, char** argv)
{
	Context context = { NULL, NULL, 0 };
	// if (argc < 3)
	// {
	// 	context.ERROR_TYPE = INVALID_INPUT;
	// 	release_context(&context);
	// 	return 1;
	// }
	// context.ERROR_TYPE = open_file(&context.file, argv[1], "r");
	context.ERROR_TYPE = open_file(&context.file, "input.dot", "r");

	if (context.ERROR_TYPE)
	{
		release_context(&context);
		return 1;
	}

	TreeNode* root = NULL;
	size_t size_of_tree;
	context.ERROR_TYPE = parse_input_graph(context.file, &context.nodes, &size_of_tree);

	if (context.ERROR_TYPE)
	{
		release_context(&context);
		return 1;
	}
	fclose(context.file);

	// context.ERROR_TYPE = open_file(&context.file, argv[2], "w");
	context.ERROR_TYPE = open_file(&context.file, "output.dot", "w");
	if (context.ERROR_TYPE)
	{
		release_context(&context);
		return 1;
	}

	inserting_nodes_in_tree(&root, context.nodes, size_of_tree);

	context.ERROR_TYPE = export_rbtree_to_dot(root, context.file);
	if (context.ERROR_TYPE)
	{
		release_context(&context);
		return 1;
	}

	release_context(&context);

	return SUCCESS;
}

static void release_context(const Context* context)
{
	if (context->ERROR_TYPE)
	{
		fprintf(stderr, "%s\n", error_message_table[context->ERROR_TYPE]);
	}
	if (context->nodes)
	{
		free(context->nodes);
	}
	if (context->file)
	{
		if (fclose(context->file) == EOF)
		{
			fprintf(stderr, "Failed to close the file.\n");
		}
	}
}

static int open_file(FILE** file, const char* file_name, const char* type_of_opening)
{
	*file = fopen(file_name, type_of_opening);

	if (!*file)
	{
		return type_of_opening[0] == 'r' ? ERROR_FILE_READING : ERROR_FILE_WRITING;
	}
	return SUCCESS;
}

static void inserting_nodes_in_tree(TreeNode** root, TreeNode* nodes, const size_t size)
{
	for (size_t i = 0; i < size; i++)
	{
		insert(&nodes[i], root);
	}
}
