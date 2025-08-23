#include <stdio.h>

int main(const int argc, char** argv)
{
	int result = 0;
	FILE* fin = NULL;
	if (argc < 3)
	{
		fprintf(stderr, "Not enought info about input/output files for operation");
		return 1;
	}

	fin = fopen(argv[1], "r");
	if (!fin)
	{
		perror("Can`t open your file for reading");
		return 1;
	}

	int countOfReadedOperands;
	int a = 0, b = 0;
	countOfReadedOperands = fscanf(fin, "%d %d", &a, &b);
	if (countOfReadedOperands < 2)
	{
		fprintf(stderr, "Not enought arguments for sum");
		result = 1;
		goto VV;
	}

	if (countOfReadedOperands > 2)
	{
		fprintf(stderr, "Too many arguments for my unrealistic program");
		result = 1;
		goto VV;
	}

VV:
	if (fin)
	{
		fclose(fin);
		fin = NULL;
	}

	if (result != 0)
	{
		return result;
	}

	FILE* fout = NULL;
	fout = fopen(argv[2], "w");
	if (!fout)
	{
		perror("Can`t open your file for writing result");
		return 1;
	}

	int sum = a + b;

	fprintf(fout, "%d\n", sum);

	fclose(fout);

	return 0;
}
