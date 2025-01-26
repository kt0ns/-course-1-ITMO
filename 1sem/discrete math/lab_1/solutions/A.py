n = int(input())
matrix1 = [list(map(int, input().split())) for _ in range(n)]
matrix2 = [list(map(int, input().split())) for _ in range(n)]

def Reflex(matrix):
    for i in range(len(matrix)):
        if matrix[i][i] != 1:
            return 0
    return 1

def noReflex(matrix):
    for i in range(len(matrix)):
        if matrix[i][i] == 1:
            return 0
    return 1

def Sym(matrix):
    for i in range(len(matrix)):
        for j in range(len(matrix)):
            if (matrix[i][j] != matrix[j][i] and i != j and matrix[i][j] == 1):
                return 0
    return 1


def noSym(matrix):
    for i in range(len(matrix)):
        for j in range(len(matrix)):
            if (i != j and matrix[i][j] == matrix[j][i] and matrix[i][j] == 1):
                return 0
    return 1


def Trans(matrix):
    for i in range(len(matrix)):
        for j in range(len(matrix)):
            for l in range(len(matrix)):
                if (matrix[i][j] == 1 and matrix[j][l] == 1 and matrix[i][l] != 1):
                    return 0
    return 1

comp = [[0]*n for _ in range(n)]

for i in range(n):
    for l in range(n):
        for j in range(n):
            if matrix1[i][l] == 1 and matrix2[l][j] == 1:
                comp[i][j] = 1


print(Reflex(matrix1), noReflex(matrix1), Sym(matrix1), noSym(matrix1), Trans(matrix1))
print(Reflex(matrix2), noReflex(matrix2), Sym(matrix2), noSym(matrix2), Trans(matrix2))
for line in comp:
    print(*line)